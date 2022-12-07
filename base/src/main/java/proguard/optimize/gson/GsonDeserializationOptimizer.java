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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.MethodLinker;
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
    private static final Logger logger = LogManager.getLogger(GsonDeserializationOptimizer.class);

    private static final int IS_NULL_VARIABLE_INDEX = ClassUtil.internalMethodParameterSize(METHOD_TYPE_FROM_JSON_FIELD, false);

    private static final Map<String, InlineDeserializer> inlineDeserializers = new HashMap<String, InlineDeserializer>();

    private final ClassPool             programClassPool;
    private final ClassPool             libraryClassPool;
    private final GsonRuntimeSettings   gsonRuntimeSettings;
    private final OptimizedJsonInfo     deserializationInfo;
    private final boolean               supportExposeAnnotation;
    private final boolean               optimizeConservatively;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;

    private OptimizedJsonInfo.ClassJsonInfo                 classDeserializationInfo;
    private Map<String, String[]>                           javaToJsonFieldNames;
    private Map<String, CompactCodeAttributeComposer.Label> caseLabelByJavaFieldName;

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
     * @param programClassPool       the program class pool to initialize added
     *                               references.
     * @param libraryClassPool       the library class pool to initialize added
     *                               references.
     * @param gsonRuntimeSettings    keeps track of all GsonBuilder invocations.
     * @param deserializationInfo    contains information on which class and
     *                               fields need to be optimized and how.
     * @param optimizeConservatively specifies whether conservative
     *                               optimization should be applied
     * @param extraDataEntryNameMap  the map that keeps track of injected
     *                               classes.
     */
    public GsonDeserializationOptimizer(ClassPool             programClassPool,
                                        ClassPool             libraryClassPool,
                                        GsonRuntimeSettings   gsonRuntimeSettings,
                                        OptimizedJsonInfo     deserializationInfo,
                                        boolean               optimizeConservatively,
                                        ExtraDataEntryNameMap extraDataEntryNameMap)
    {
        this.programClassPool        = programClassPool;
        this.libraryClassPool        = libraryClassPool;
        this.gsonRuntimeSettings     = gsonRuntimeSettings;
        this.deserializationInfo     = deserializationInfo;
        this.supportExposeAnnotation = gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation;
        this.optimizeConservatively  = optimizeConservatively;
        this.extraDataEntryNameMap   = extraDataEntryNameMap;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
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
        ClassBuilder classBuilder =
            new ClassBuilder(programClass);

        if (constructorCounter.getCount() == 0)
        {
            addDefaultConstructor(programClass,
                                  classBuilder);
        }

        int classIndex = deserializationInfo.classIndices.get(programClass.getName());

        addFromJsonMethod(programClass,
                          classBuilder,
                          classIndex);

        addFromJsonFieldMethod(programClass,
                               classBuilder,
                               classIndex);

        // Make sure all references in the class are initialized.
        programClass.accept(new ClassReferenceInitializer(programClassPool, libraryClassPool));

        // Link all methods with related ones.
        programClass.accept(new MethodLinker());
    }

    private void addDefaultConstructor(ProgramClass programClass,
                                       ClassBuilder classBuilder)
    {
        logger.debug("GsonDeserializationOptimizer: adding default constructor to {}", programClass.getName());

        classBuilder.addMethod(
            AccessConstants.PUBLIC |
            AccessConstants.SYNTHETIC,
            ClassConstants.METHOD_NAME_INIT,
            ClassConstants.METHOD_TYPE_INIT,
            10,
            ____ -> ____
                .aload_0() // this
                .invokespecial(programClass.getSuperName(),
                               ClassConstants.METHOD_NAME_INIT,
                               ClassConstants.METHOD_TYPE_INIT)
                .return_(),
            new ProgramMemberOptimizationInfoSetter(false, optimizeConservatively));
    }


    private void addFromJsonMethod(ProgramClass programClass,
                                   ClassBuilder classBuilder,
                                   int          classIndex)
    {
        String methodNameFromJson = METHOD_NAME_FROM_JSON + classIndex;

        logger.debug("GsonDeserializationOptimizer: adding {} method to {}",
                     methodNameFromJson,
                     programClass.getName()
        );

        classBuilder.addMethod(
            AccessConstants.PUBLIC | AccessConstants.SYNTHETIC,
            methodNameFromJson,
            OptimizedClassConstants.METHOD_TYPE_FROM_JSON,
            1000,
            ____ ->
            {
                // Begin Json object.
                ____.aload(FromJsonLocals.JSON_READER)
                    .invokevirtual(GsonClassConstants.NAME_JSON_READER,
                                   GsonClassConstants.METHOD_NAME_READER_BEGIN_OBJECT,
                                   GsonClassConstants.METHOD_TYPE_READER_BEGIN_OBJECT);

            // Assign locals for nextFieldIndex.
            int nextFieldIndexLocalIndex = 4;

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
            classDeserializationInfo = deserializationInfo.classJsonInfos.get(programClass.getName());
            javaToJsonFieldNames     = classDeserializationInfo.javaToJsonFieldNames;

            String methodNameFromJsonField = METHOD_NAME_FROM_JSON_FIELD + classIndex;
            ____.aload(FromJsonLocals.THIS)
                .aload(FromJsonLocals.GSON)
                .aload(FromJsonLocals.JSON_READER)
                .iload(nextFieldIndexLocalIndex)
                .invokevirtual(programClass.getName(),

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
            },
            new ProgramMemberOptimizationInfoSetter(false, optimizeConservatively));
    }


    private void addFromJsonFieldMethod(ProgramClass programClass,
                                        ClassBuilder classBuilder,
                                        int          classIndex)
    {
        String methodNameFromJsonField = METHOD_NAME_FROM_JSON_FIELD + classIndex;

        logger.debug("GsonDeserializationOptimizer: adding {} method to {}",
                     methodNameFromJsonField,
                     programClass.getName()
        );

        classBuilder.addMethod(
            AccessConstants.PROTECTED | AccessConstants.SYNTHETIC,
            methodNameFromJsonField,
            METHOD_TYPE_FROM_JSON_FIELD,
            1000,
            ____ ->
            {
                CompactCodeAttributeComposer.Label endSwitch = ____.createLabel();

            // Are there any fields to be deserialized at the level of this class?
            if (javaToJsonFieldNames.size() > 0)
            {
                // Check for NULL token and store result in boolean local variable.
                CompactCodeAttributeComposer.Label tokenNotNull = ____.createLabel();
                CompactCodeAttributeComposer.Label assignIsNull = ____.createLabel();

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
                    .istore(IS_NULL_VARIABLE_INDEX);

                generateSwitchTables(____, endSwitch);
            }

            if (programClass.getSuperClass() != null)
            {
                // If no known field index was returned for this class and
                // field, delegate to super method if it exists or skip the value.
                if (!programClass.getSuperClass().getName().equals(ClassConstants.NAME_JAVA_LANG_OBJECT))
                {
                    // Call the superclass fromJsonField$.
                    Integer superClassIndex =
                        deserializationInfo.classIndices.get(programClass.getSuperClass().getName());
                    String superMethodNameFromJsonField =
                        METHOD_NAME_FROM_JSON_FIELD + superClassIndex;

                    ____.aload(FromJsonFieldLocals.THIS)
                        .aload(FromJsonFieldLocals.GSON)
                        .aload(FromJsonFieldLocals.JSON_READER)
                        .iload(FromJsonFieldLocals.FIELD_INDEX)
                        .invokevirtual(programClass.getSuperClass().getName(),
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
            }
            else
            {
                throw new RuntimeException ("Cannot find super class of " + programClass.getName() + " for Gson optimization. Please check your configuration includes all the expected library jars.");
            }

            // End of switch.
            ____.label(endSwitch)
                .return_();
            },
            new ProgramMemberOptimizationInfoSetter(false, optimizeConservatively));
    }


    private void generateSwitchTables(CompactCodeAttributeComposer       ____,
                                      CompactCodeAttributeComposer.Label endSwitch)
    {
        Set<String> exposedJavaFieldNames = classDeserializationInfo.exposedJavaFieldNames;

        Set<String> exposedOrAllJavaFieldNames = supportExposeAnnotation ? exposedJavaFieldNames :
            javaToJsonFieldNames.keySet();

        generateSwitchTable(____,
                            endSwitch,
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
            generateSwitchTable(____,
                                endSwitch,
                                javaToJsonFieldNames,
                                nonExposedJavaFieldNames);

            ____.label(nonExposedCasesEnd);
        }
    }

    private void generateSwitchTable(CompactCodeAttributeComposer       ____,
                                     CompactCodeAttributeComposer.Label endSwitch,
                                     Map<String, String[]>              javaToJsonFieldNames,
                                     Set<String>                        javaFieldNamesToProcess)
    {
        ArrayList<FromJsonFieldCase> fromJsonFieldCases = new ArrayList<FromJsonFieldCase>();
        for (Map.Entry<String, String[]> javaToJsonFieldNameEntry : javaToJsonFieldNames.entrySet())
        {
            if (javaFieldNamesToProcess.contains(javaToJsonFieldNameEntry.getKey()))
            {
                // Add cases for the alternative Json names with the same label.
                String[] jsonFieldNames = javaToJsonFieldNameEntry.getValue();
                CompactCodeAttributeComposer.Label caseLabel = ____.createLabel();
                for (String jsonFieldName : jsonFieldNames)
                {
                    fromJsonFieldCases.add(new FromJsonFieldCase(javaToJsonFieldNameEntry.getKey(),
                                                                 caseLabel,
                                                                 deserializationInfo.jsonFieldIndices.get(jsonFieldName)));
                }
            }
        }
        Collections.sort(fromJsonFieldCases);

        // Don't add switch cases for fields for which we won't create deserialization code later,
        // otherwise we'll end up with dangling labels.
        fromJsonFieldCases.removeIf(fromJsonFieldCase -> {
            Field field = ____.getTargetClass().findField(fromJsonFieldCase.javaFieldName, null);
            return field == null ||
                   (field.getAccessFlags() & (AccessConstants.STATIC | AccessConstants.SYNTHETIC)) != 0;
        });

        int[]                                cases       = new int[fromJsonFieldCases.size()];
        CompactCodeAttributeComposer.Label[] jumpOffsets = new CompactCodeAttributeComposer.Label[fromJsonFieldCases.size()];
        caseLabelByJavaFieldName = new HashMap<String, CompactCodeAttributeComposer.Label>();
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
        ____.getTargetClass().fieldsAccept(
            new MemberAccessFilter(0,
                                   AccessConstants.SYNTHETIC |
                                   AccessConstants.STATIC,
            new FromJsonFieldDeserializationCodeAdder(____, endSwitch)));
        ____.label(defaultCase);
    }


    private class FromJsonFieldDeserializationCodeAdder
    implements    MemberVisitor
    {
        private final CompactCodeAttributeComposer       ____;
        private final CompactCodeAttributeComposer.Label endSwitch;


        public FromJsonFieldDeserializationCodeAdder(CompactCodeAttributeComposer       ____,
                                                     CompactCodeAttributeComposer.Label endSwitch)
        {
            this.____      = ____;
            this.endSwitch = endSwitch;
        }


        // Implementations for MemberVisitor.

        @Override
        public void visitProgramField(ProgramClass programClass,
                                      ProgramField programField)
        {
            CompactCodeAttributeComposer.Label fromJsonFieldCaseLabel =
                caseLabelByJavaFieldName.get(programField.getName(programClass));

            if (fromJsonFieldCaseLabel != null)
            {
                // Make sure the field is not final anymore so we can safely write it from the injected method.
                programField.accept(programClass, new MemberAccessFlagCleaner(AccessConstants.FINAL));

                // Check if value is null
                CompactCodeAttributeComposer.Label isNull = ____.createLabel();
                ____.label(fromJsonFieldCaseLabel)
                    .iload(IS_NULL_VARIABLE_INDEX)
                    .ifeq(isNull);

                String fieldDescriptor = programField.getDescriptor(programClass);
                FieldSignatureCollector signatureAttributeCollector = new FieldSignatureCollector();
                programField.attributesAccept(programClass, signatureAttributeCollector);

                InlineDeserializer inlineDeserializer = inlineDeserializers.get(fieldDescriptor);
                if (!gsonRuntimeSettings.registerTypeAdapterFactory &&
                    inlineDeserializer != null &&
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
                        fieldTypeName  = fieldClassName;
                    }
                    else
                    {
                        fieldClassName = ClassUtil.internalClassNameFromClassType(fieldDescriptor);
                        fieldTypeName  = ClassUtil.internalClassTypeFromType(fieldDescriptor);
                    }

                    // Derive type token class name if there is a field signature.
                    String typeTokenClassName = null;
                    if (signatureAttributeCollector.getFieldSignature() != null)
                    {
                        // Add type token sub-class that has the appropriate type parameter.
                        ProgramClass typeTokenClass =
                            new TypeTokenClassBuilder(programClass,
                                                      programField,
                                                      signatureAttributeCollector.getFieldSignature())
                                .build(programClassPool);
                        programClassPool.addClass(typeTokenClass);
                        typeTokenClass.accept(new ClassReferenceInitializer(programClassPool,
                                                                            libraryClassPool));
                        typeTokenClassName = typeTokenClass.getName();
                        extraDataEntryNameMap.addExtraClassToClass(programClass.getName(),
                                                                   typeTokenClassName);
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


    private static class FromJsonFieldCase implements Comparable<FromJsonFieldCase>
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
