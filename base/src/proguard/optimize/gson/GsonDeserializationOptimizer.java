/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.gson;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.editor.CompactCodeAttributeComposer.Label;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter;

import java.util.*;

import static proguard.classfile.ClassConstants.*;
import static proguard.optimize.gson.OptimizedClassConstants.*;

/**
 * This visitor injects a fromJson$xxx() method into the classes that it visits
 * that deserializes its fields from Json.
 *
 * @author Lars Vandenbergh
 * @author Rob Coekaerts
 */
public class GsonDeserializationOptimizer
implements   ClassVisitor,
             MemberVisitor,
             AttributeVisitor
{
    private static final boolean DEBUG = false;

    private static final Map<String, InlineDeserializer> inlineDeserializers = new HashMap<String, InlineDeserializer>();

    private final ClassPool             programClassPool;
    private final ClassPool             libraryClassPool;
    private final GsonRuntimeSettings   gsonRuntimeSettings;
    private final OptimizedJsonInfo     deserializationInfo;
    private final boolean               supportExposeAnnotation;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;

    private OptimizedJsonInfo.ClassJsonInfo    classDeserializationInfo;
    private Map<String, String[]>              javaToJsonFieldNames;
    private Map<String, Label>                 caseLabelByJavaFieldName;
    private CompactCodeAttributeComposer.Label endSwitch;

    static
    {
        inlineDeserializers.put(TypeConstants.BYTE + "",
                                new InlineDeserializers.InlinePrimitiveIntegerDeserializer(byte.class));
        inlineDeserializers.put(TypeConstants.SHORT + "",
                                new InlineDeserializers.InlinePrimitiveIntegerDeserializer(short.class));
        inlineDeserializers.put(TypeConstants.INT + "",
                                new InlineDeserializers.InlinePrimitiveIntegerDeserializer());
        inlineDeserializers.put(ClassConstants.TYPE_JAVA_LANG_STRING,
                                new InlineDeserializers.InlineStringDeserializer());
    }


    /**
     * Creates a new GsonDeserializationOptimizer.
     *
     * @param programClassPool      the program class pool to initialize added
     *                              references.
     * @param libraryClassPool      the library class pool to initialize added
     *                              references.
     * @param gsonRuntimeSettings   keeps track of all GsonBuilder invocations.
     * @param deserializationInfo   contains information on which class and
     *                              fields need to be optimized and how.
     * @param extraDataEntryNameMap the map that keeps track of injected
     *                              classes.
     */
    public GsonDeserializationOptimizer(ClassPool             programClassPool,
                                        ClassPool             libraryClassPool,
                                        GsonRuntimeSettings   gsonRuntimeSettings,
                                        OptimizedJsonInfo     deserializationInfo,
                                        ExtraDataEntryNameMap extraDataEntryNameMap)
    {
        this.programClassPool        = programClassPool;
        this.libraryClassPool        = libraryClassPool;
        this.gsonRuntimeSettings     = gsonRuntimeSettings;
        this.deserializationInfo     = deserializationInfo;
        this.supportExposeAnnotation = gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation;
        this.extraDataEntryNameMap   = extraDataEntryNameMap;
    }


    // Implementations for ClassVisitor.

    public void visitAnyClass(Clazz clazz) {}


    public void visitProgramClass(ProgramClass programClass)
    {
        // Make access public for _OptimizedTypeAdapterFactory and _OptimizedTypeAdapterImpl.
        programClass.u2accessFlags &= ~AccessConstants.PRIVATE;
        programClass.u2accessFlags |=  AccessConstants.PUBLIC;

        // Make default constructor public for _OptimizedTypeAdapterImpl.
        MemberCounter constructorCounter = new MemberCounter();
        programClass.methodsAccept(
            new MemberNameFilter(ClassConstants.METHOD_NAME_INIT,
            new MemberDescriptorFilter(ClassConstants.METHOD_TYPE_INIT,
            new MultiMemberVisitor(
                new MemberAccessSetter(AccessConstants.PUBLIC),
                constructorCounter))));

        // Start adding new deserialization methods.
        ClassBuilder classBuilder = new ClassBuilder(programClass,
                                                     programClassPool,
                                                     libraryClassPool);

        if (constructorCounter.getCount() == 0)
        {
            addDefaultConstructor(classBuilder);
        }

        int classIndex = deserializationInfo.classIndices.get(programClass.getName());

        addFromJsonMethod(classBuilder,
                          classIndex);

        addFromJsonFieldMethod(classBuilder,
                               classIndex);

        // Link related methods in the class hierarchy.
        programClass.accept(new MethodLinker());
    }


    private void addDefaultConstructor(ClassBuilder classBuilder)
    {
        ProgramClass programClass = classBuilder.getProgramClass();

        if (DEBUG)
        {
            System.out.println(
                "GsonDeserializationOptimizer: adding default constructor to " +
                programClass.getName());
        }

        classBuilder.addMethod(
            AccessConstants.PUBLIC |
            AccessConstants.SYNTHETIC,
            ClassConstants.METHOD_NAME_INIT,
            ClassConstants.METHOD_TYPE_INIT,
            10,
            code -> code
                .aload_0()
                .invokespecial(programClass.getSuperName(),
                               ClassConstants.METHOD_NAME_INIT,
                               ClassConstants.METHOD_TYPE_INIT)
                .return_(),

            // Add optimization info to the new method.
            new ProgramMemberOptimizationInfoSetter());
    }


    private void addFromJsonMethod(ClassBuilder classBuilder,
                                   int          classIndex)
    {
        String methodNameFromJson = METHOD_NAME_FROM_JSON + classIndex;

        if (DEBUG)
        {
            System.out.println(
                "GsonDeserializationOptimizer: adding " +
                methodNameFromJson +
                " method to " + classBuilder.getProgramClass().getName());
        }

        classBuilder.addMethod(
            AccessConstants.PUBLIC | AccessConstants.SYNTHETIC,
            methodNameFromJson,
            OptimizedClassConstants.METHOD_TYPE_FROM_JSON,
            10,
            new FromJsonCodeAttributeVisitor(),

            // Add optimization info to the new method.
            new ProgramMemberOptimizationInfoSetter());
    }


    private class FromJsonCodeAttributeVisitor
        implements ClassBuilder.CodeBuilder
    {
        // Implementations for ClassBuilder.CodeBuilder.

        public void compose(CompactCodeAttributeComposer ____)
        {
            String className = ____.getTargetClass().getName();

            // Begin Json object.
            ____.aload(FromJsonLocals.JSON_READER)
                .invokevirtual(GsonClassConstants.NAME_JSON_READER,
                               GsonClassConstants.METHOD_NAME_READER_BEGIN_OBJECT,
                               GsonClassConstants.METHOD_TYPE_READER_BEGIN_OBJECT);

            // Assign locals for nextFieldIndex.
            int nextFieldIndexLocalIndex = 1;

            // Start while loop that iterates over Json fields.
            CompactCodeAttributeComposer.Label startWhile    = ____.createLabel();
            CompactCodeAttributeComposer.Label endJsonObject = ____.createLabel();

            // Is there a next field? If not, terminate loop and end object.
            ____.label(startWhile)
                .aload(FromJsonLocals.JSON_READER)
                .invokevirtual(GsonClassConstants.NAME_JSON_READER,
                               GsonClassConstants.METHOD_NAME_HAS_NEXT,
                               GsonClassConstants.METHOD_TYPE_HAS_NEXT)
                .ifeq(endJsonObject);

            // Get next field index and store it in a local.
            ____.aload(FromJsonLocals.OPTIMIZED_JSON_READER)
                .aload(FromJsonLocals.JSON_READER)
                .invokeinterface(OptimizedClassConstants.NAME_OPTIMIZED_JSON_READER,
                                 OptimizedClassConstants.METHOD_NAME_NEXT_FIELD_INDEX,
                                 OptimizedClassConstants.METHOD_TYPE_NEXT_FIELD_INDEX)
                .istore(nextFieldIndexLocalIndex);

            // Invoke fromJsonField$ with the stored field index.
            classDeserializationInfo = deserializationInfo.classJsonInfos.get(className);
            javaToJsonFieldNames     = classDeserializationInfo.javaToJsonFieldNames;
            Integer classIndex       = deserializationInfo.classIndices.get(className);

            String methodNameFromJsonField = METHOD_NAME_FROM_JSON_FIELD + classIndex;
            ____.aload(FromJsonLocals.THIS)
                .aload(FromJsonLocals.GSON)
                .aload(FromJsonLocals.JSON_READER)
                .iload(nextFieldIndexLocalIndex)
                .invokevirtual(className,
                               methodNameFromJsonField,
                               METHOD_TYPE_FROM_JSON_FIELD);

            // Jump to start of while loop.
            ____.goto_(startWhile);

            // End Json object.
            ____.label(endJsonObject)
                .aload(FromJsonLocals.JSON_READER)
                .invokevirtual(GsonClassConstants.NAME_JSON_READER,
                               GsonClassConstants.METHOD_NAME_READER_END_OBJECT,
                               GsonClassConstants.METHOD_TYPE_READER_END_OBJECT)
                .return_();
        }
    }


    private void addFromJsonFieldMethod(ClassBuilder classBuilder,
                                        int          classIndex)
    {
        String methodNameFromJsonField = METHOD_NAME_FROM_JSON_FIELD + classIndex;

        if (DEBUG)
        {
            System.out.println(
                "GsonDeserializationOptimizer: adding " +
                methodNameFromJsonField +
                " method to " + classBuilder.getProgramClass().getName());
        }

        classBuilder.addMethod(
            AccessConstants.PROTECTED | AccessConstants.SYNTHETIC,
            methodNameFromJsonField,
            METHOD_TYPE_FROM_JSON_FIELD,
            50,
            new FromJsonFieldCodeBuilder(),

            // Add optimization info to the new method.
            new ProgramMemberOptimizationInfoSetter());
    }


    private class FromJsonFieldCodeBuilder
    implements    ClassBuilder.CodeBuilder,
                  MemberVisitor
    {
        private CompactCodeAttributeComposer ____;
        private int                          isNullLocalIndex;


        // Implementations for ClassBuilder.CodeBuilder.

        public void compose(CompactCodeAttributeComposer ____)
        {
            // Remember the composer.
            this.____ = ____;

            Clazz clazz = ____.getTargetClass();

            endSwitch = ____.createLabel();

            // Are there any fields to be deserialized at the level of this class?
            if (javaToJsonFieldNames.size() > 0)
            {
                // Check for NULL token and store result in boolean local variable.
                CompactCodeAttributeComposer.Label tokenNotNull = ____.createLabel();
                CompactCodeAttributeComposer.Label assignIsNull = ____.createLabel();

                isNullLocalIndex = 1;
                ____.aload(FromJsonLocals.JSON_READER)
                    .invokevirtual(GsonClassConstants.NAME_JSON_READER,
                                   GsonClassConstants.METHOD_NAME_PEEK,
                                   GsonClassConstants.METHOD_TYPE_PEEK)
                    .getstatic(GsonClassConstants.NAME_JSON_TOKEN,
                               GsonClassConstants.FIELD_NAME_NULL,
                               GsonClassConstants.FIELD_TYPE_NULL);

                ____.ifacmpeq(tokenNotNull)
                    .iconst_1()
                    .goto_(assignIsNull)
                    .label(tokenNotNull)
                    .iconst_0()
                    .label(assignIsNull)
                    .istore(isNullLocalIndex);

                generateSwitchTables(clazz);
            }

            // If no known field index was returned for this class and
            // field, delegate to super method if it exists or skip the value.
            if (!clazz.getSuperName().equals(ClassConstants.NAME_JAVA_LANG_OBJECT))
            {
                // Call the superclass fromJsonField$.
                Integer superClassIndex =
                    deserializationInfo.classIndices.get(clazz.getSuperName());
                String superMethodNameFromJsonField =
                    METHOD_NAME_FROM_JSON_FIELD + superClassIndex;

                ____.aload(FromJsonFieldLocals.THIS)
                    .aload(FromJsonFieldLocals.GSON)
                    .aload(FromJsonFieldLocals.JSON_READER)
                    .iload(FromJsonFieldLocals.FIELD_INDEX)
                    .invokevirtual(clazz.getSuperName(),
                                   superMethodNameFromJsonField,
                                   METHOD_TYPE_FROM_JSON_FIELD);
            }
            else
            {
                // Skip field in default case of switch or when no switch is generated.
                ____.aload(FromJsonLocals.JSON_READER)
                    .invokevirtual(GsonClassConstants.NAME_JSON_READER,
                                   GsonClassConstants.METHOD_NAME_SKIP_VALUE,
                                   GsonClassConstants.METHOD_TYPE_SKIP_VALUE);
            }

            // End of switch.
            ____.label(endSwitch)
                .return_();
        }


        private void generateSwitchTables(Clazz clazz)
        {
            Set<String> exposedJavaFieldNames = classDeserializationInfo.exposedJavaFieldNames;

            Set<String> exposedOrAllJavaFieldNames = supportExposeAnnotation ? exposedJavaFieldNames :
                                                                               javaToJsonFieldNames.keySet();

            generateSwitchTable(clazz,
                                javaToJsonFieldNames,
                                exposedOrAllJavaFieldNames);

            if (supportExposeAnnotation)
            {
                // Runtime check whether excludeFieldsWithoutExposeAnnotation is enabled.
                // If so, skip this switch statement.
                CompactCodeAttributeComposer.Label nonExposedCasesEnd = ____.createLabel();

                ____.aload(ToJsonLocals.GSON)
                    .getfield(GsonClassConstants.NAME_GSON,     FIELD_NAME_EXCLUDER,       FIELD_TYPE_EXCLUDER)
                    .getfield(GsonClassConstants.NAME_EXCLUDER, FIELD_NAME_REQUIRE_EXPOSE, FIELD_TYPE_REQUIRE_EXPOSE)
                    .ifne(nonExposedCasesEnd);

                Set<String> nonExposedJavaFieldNames = new HashSet<String>();
                for (String javaFieldName: javaToJsonFieldNames.keySet())
                {
                    if (!exposedJavaFieldNames.contains(javaFieldName))
                    {
                        nonExposedJavaFieldNames.add((javaFieldName));
                    }
                }
                generateSwitchTable(clazz,
                                    javaToJsonFieldNames,
                                    nonExposedJavaFieldNames);

                ____.label(nonExposedCasesEnd);
            }
        }

        private void generateSwitchTable(Clazz                 clazz,
                                         Map<String, String[]> javaToJsonFieldNames,
                                         Set<String>           javaFieldNamesToProcess)
        {
            ArrayList<FromJsonFieldCase> fromJsonFieldCases = new ArrayList<FromJsonFieldCase>();
            for (Map.Entry<String, String[]> javaToJsonFieldNameEntry : javaToJsonFieldNames.entrySet())
            {
                if (javaFieldNamesToProcess.contains(javaToJsonFieldNameEntry.getKey()))
                {
                    // Add cases for the alternative Json names with the same label.
                    String[]                           jsonFieldNames = javaToJsonFieldNameEntry.getValue();
                    CompactCodeAttributeComposer.Label caseLabel      = ____.createLabel();

                    for (String jsonFieldName : jsonFieldNames)
                    {
                        fromJsonFieldCases.add(new FromJsonFieldCase(javaToJsonFieldNameEntry.getKey(),
                                                                     caseLabel,
                                                                     deserializationInfo.jsonFieldIndices.get(jsonFieldName)));
                    }
                }
            }
            Collections.sort(fromJsonFieldCases);

            int[] cases                                      = new int[fromJsonFieldCases.size()];
            CompactCodeAttributeComposer.Label[] jumpOffsets = new CompactCodeAttributeComposer.Label[fromJsonFieldCases.size()];

            caseLabelByJavaFieldName = new HashMap<String, Label>();

            for (int caseIndex = 0; caseIndex < fromJsonFieldCases.size(); caseIndex++)
            {
                FromJsonFieldCase fromJsonFieldCase = fromJsonFieldCases.get(caseIndex);
                cases[caseIndex]                    = fromJsonFieldCase.fieldIndex;
                jumpOffsets[caseIndex]              = fromJsonFieldCase.label;
                caseLabelByJavaFieldName.put(fromJsonFieldCase.javaFieldName, fromJsonFieldCase.label);
            }

            CompactCodeAttributeComposer.Label defaultCase = ____.createLabel();

            ____.iload(FromJsonFieldLocals.FIELD_INDEX)
                .lookupswitch(defaultCase,
                              cases,
                              jumpOffsets);

            // Apply non static member visitor to all fields to visit.
            clazz.fieldsAccept(new MemberAccessFilter(0,
                                                      AccessConstants.SYNTHETIC |
                                                      AccessConstants.STATIC,
                                                      this));
            ____.label(defaultCase);
        }

        // Implementations for MemberVisitor.

        @Override
        public void visitProgramField(ProgramClass programClass,
                                      ProgramField programField)
        {
            Label fromJsonFieldCaseLabel = caseLabelByJavaFieldName.get(programField.getName(programClass));
            if (fromJsonFieldCaseLabel != null)
            {
                // Make sure the field is not final anymore so we can safely write it from the injected method.
                programField.accept(programClass, new MemberAccessFlagCleaner(AccessConstants.FINAL));

                // Check if value is null
                CompactCodeAttributeComposer.Label isNull = ____.createLabel();

                ____.label(fromJsonFieldCaseLabel)
                    .iload(isNullLocalIndex)
                    .ifeq(isNull);

                String fieldDescriptor = programField.getDescriptor(programClass);
                FieldSignatureCollector signatureAttributeCollector = new FieldSignatureCollector();
                programField.attributesAccept(programClass, signatureAttributeCollector);

                InlineDeserializer inlineDeserializer = inlineDeserializers.get(fieldDescriptor);
                if (inlineDeserializer != null &&
                    inlineDeserializer.canDeserialize(gsonRuntimeSettings))
                {
                    inlineDeserializer.deserialize(programClass,
                                                   programField,
                                                   ____,
                                                   gsonRuntimeSettings);
                }
                else
                {
                    // Derive the field class and type name for which we want to retrieve the type adapter from Gson.
                    String fieldTypeName;
                    String fieldClassName;
                    if (ClassUtil.isInternalPrimitiveType(fieldDescriptor))
                    {
                        fieldClassName = ClassUtil.internalNumericClassNameFromPrimitiveType(fieldDescriptor.charAt(0));
                        fieldTypeName = fieldClassName;
                    }
                    else
                    {
                        fieldClassName = ClassUtil.internalClassNameFromClassType(fieldDescriptor);
                        fieldTypeName = fieldDescriptor;
                    }

                    // Derive type token class name if there is a field signature.
                    String typeTokenClassName = null;
                    if (signatureAttributeCollector.getFieldSignature() != null)
                    {
                        // Create a new token sub-class that has the appropriate type parameter.
                        ProgramClass typeTokenClass =
                            new TypeTokenClassBuilder(programClass,
                                                      programField,
                                                      signatureAttributeCollector.getFieldSignature())
                                .build();

                        // Add it to the class pool.
                        programClassPool.addClass(typeTokenClass);

                        // Initialize it.
                        typeTokenClass.accept(
                            new MultiClassVisitor(
                                new ClassSuperHierarchyInitializer(programClassPool, libraryClassPool),
                                new ClassSubHierarchyInitializer(),
                                new ClassReferenceInitializer(programClassPool, libraryClassPool)
                            ));

                        // Attach it to the original class.
                        extraDataEntryNameMap.addExtraClassToClass(programClass, typeTokenClass);
                    }

                    // Retrieve type adapter and deserialize value from Json.
                    if (typeTokenClassName == null)
                    {
                        ____.aload(FromJsonLocals.THIS)
                            .aload(FromJsonLocals.GSON)
                            .ldc(fieldTypeName, programClassPool.getClass(fieldClassName))
                            .invokevirtual(GsonClassConstants.NAME_GSON,
                                           GsonClassConstants.METHOD_NAME_GET_ADAPTER_CLASS,
                                           GsonClassConstants.METHOD_TYPE_GET_ADAPTER_CLASS);
                    }
                    else
                    {
                        ____.aload(FromJsonLocals.THIS)
                            .aload(FromJsonLocals.GSON)
                            .new_(typeTokenClassName)
                            .dup()
                            .invokespecial(typeTokenClassName,
                                           ClassConstants.METHOD_NAME_INIT,
                                           ClassConstants.METHOD_TYPE_INIT)
                            .invokevirtual(GsonClassConstants.NAME_GSON,
                                           GsonClassConstants.METHOD_NAME_GET_ADAPTER_TYPE_TOKEN,
                                           GsonClassConstants.METHOD_TYPE_GET_ADAPTER_TYPE_TOKEN);
                    }

                    ____.aload(FromJsonLocals.JSON_READER)
                        .invokevirtual(GsonClassConstants.NAME_TYPE_ADAPTER,
                                       GsonClassConstants.METHOD_NAME_READ,
                                       GsonClassConstants.METHOD_TYPE_READ)
                        .checkcast(fieldTypeName, programClassPool.getClass(fieldClassName));

                    // If the field is primitive, unbox the value before assigning it.
                    switch (fieldDescriptor.charAt(0))
                    {
                        case TypeConstants.BOOLEAN:
                            ____.invokevirtual(NAME_JAVA_LANG_BOOLEAN,
                                               METHOD_NAME_BOOLEAN_VALUE,
                                               METHOD_TYPE_BOOLEAN_VALUE);
                            break;
                        case TypeConstants.BYTE:
                            ____.invokevirtual(NAME_JAVA_LANG_BYTE,
                                               METHOD_NAME_BYTE_VALUE,
                                               METHOD_TYPE_BYTE_VALUE);
                            break;
                        case TypeConstants.CHAR:
                            ____.invokevirtual(NAME_JAVA_LANG_CHARACTER,
                                               METHOD_NAME_CHAR_VALUE,
                                               METHOD_TYPE_CHAR_VALUE);
                            break;
                        case TypeConstants.SHORT:
                            ____.invokevirtual(NAME_JAVA_LANG_SHORT,
                                               METHOD_NAME_SHORT_VALUE,
                                               METHOD_TYPE_SHORT_VALUE);
                            break;
                        case TypeConstants.INT:
                            ____.invokevirtual(NAME_JAVA_LANG_INTEGER,
                                               METHOD_NAME_INT_VALUE,
                                               METHOD_TYPE_INT_VALUE);
                            break;
                        case TypeConstants.LONG:
                            ____.invokevirtual(NAME_JAVA_LANG_LONG,
                                               METHOD_NAME_LONG_VALUE,
                                               METHOD_TYPE_LONG_VALUE);
                            break;
                        case TypeConstants.FLOAT:
                            ____.invokevirtual(NAME_JAVA_LANG_FLOAT,
                                               METHOD_NAME_FLOAT_VALUE,
                                               METHOD_TYPE_FLOAT_VALUE);
                            break;
                        case TypeConstants.DOUBLE:
                            ____.invokevirtual(NAME_JAVA_LANG_DOUBLE,
                                               METHOD_NAME_DOUBLE_VALUE,
                                               METHOD_TYPE_DOUBLE_VALUE);
                            break;
                    }

                    // Assign deserialized value to field.
                    ____.putfield(programClass, programField);
                }

                // Jump to the end of the switch.
                ____.goto_(endSwitch);

                // Either skip the null (in case of a primitive) or assign the null
                // (in case of an object) and jump to the end of the switch.
                ____.label(isNull);

                // Why is it necessary to specifically assign a null value?
                if (!ClassUtil.isInternalPrimitiveType(fieldDescriptor))
                {
                    ____.aload(FromJsonLocals.THIS)
                        .aconst_null()
                        .putfield(programClass, programField);
                }
                ____.aload(FromJsonLocals.JSON_READER)
                    .invokevirtual(GsonClassConstants.NAME_JSON_READER,
                                   GsonClassConstants.METHOD_NAME_NEXT_NULL,
                                   GsonClassConstants.METHOD_TYPE_NEXT_NULL)
                    .goto_(endSwitch);
            }
        }
    }


    public static class FromJsonFieldCase implements Comparable<FromJsonFieldCase>
    {
        private String                             javaFieldName;
        private CompactCodeAttributeComposer.Label label;
        private int                                fieldIndex;

        public FromJsonFieldCase(String                             javaFieldName,
                                 CompactCodeAttributeComposer.Label label,
                                 int                                fieldIndex)
        {
            this.javaFieldName = javaFieldName;
            this.label         = label;
            this.fieldIndex    = fieldIndex;
        }

        @Override
        public int compareTo(FromJsonFieldCase fromJsonFieldCase)
        {
            return this.fieldIndex - fromJsonFieldCase.fieldIndex;
        }
    }
}
