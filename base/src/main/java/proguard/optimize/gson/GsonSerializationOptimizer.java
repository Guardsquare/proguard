/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.MethodLinker;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberAccessFilter;
import proguard.classfile.visitor.MemberVisitor;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter;

import java.util.HashMap;
import java.util.Map;

import static proguard.optimize.gson.OptimizedClassConstants.*;

/**
 * This visitor injects a toJson$xxx() method into the classes that it visits
 * that serializes its fields to Json.
 *
 * @author Lars Vandenbergh
 * @author Rob Coekaerts
 */
public class GsonSerializationOptimizer
implements   MemberVisitor,
             ClassVisitor,
             ElementValueVisitor,
             AttributeVisitor,
             AnnotationVisitor
{
    private static final boolean DEBUG = false;

    private static final int VALUE_VARIABLE_INDEX = ClassUtil.internalMethodParameterSize(METHOD_TYPE_TO_JSON_BODY, false);

    private static final Map<String,InlineSerializer> inlineSerializers = new HashMap<String, InlineSerializer>();

    private final ClassPool             programClassPool;
    private final ClassPool             libraryClassPool;
    private final GsonRuntimeSettings   gsonRuntimeSettings;
//    private final CodeAttributeEditor   codeAttributeEditor;
    private final OptimizedJsonInfo     serializationInfo;
    private final boolean               supportExposeAnnotation;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;

//    private InstructionSequenceBuilder ____;

    static
    {
        inlineSerializers.put(TypeConstants.BOOLEAN + "",
                              new InlineSerializers.InlinePrimitiveBooleanSerializer());
        inlineSerializers.put(ClassConstants.TYPE_JAVA_LANG_BOOLEAN,
                              new InlineSerializers.InlineBooleanSerializer());
        inlineSerializers.put(TypeConstants.BYTE + "",
                              new InlineSerializers.InlinePrimitiveIntegerSerializer());
        inlineSerializers.put(TypeConstants.SHORT + "",
                              new InlineSerializers.InlinePrimitiveIntegerSerializer());
        inlineSerializers.put(TypeConstants.INT + "",
                              new InlineSerializers.InlinePrimitiveIntegerSerializer());
        inlineSerializers.put(ClassConstants.TYPE_JAVA_LANG_STRING,
                              new InlineSerializers.InlineStringSerializer());
    }

    /**
     * Creates a new GsonSerializationOptimizer.
     *
     * @param programClassPool      the program class pool to initialize
     *                              added references.
     * @param libraryClassPool      the library class pool to initialize
     *                              added references.
     * @param gsonRuntimeSettings   keeps track of all GsonBuilder
     *                              invocations.
     * @param serializationInfo     contains information on which class
     *                              and fields need to be optimized and how.
     * @param extraDataEntryNameMap the map that keeps track of injected
     *                              classes.
     */
    public GsonSerializationOptimizer(ClassPool             programClassPool,
                                      ClassPool             libraryClassPool,
                                      GsonRuntimeSettings   gsonRuntimeSettings,
                                      OptimizedJsonInfo     serializationInfo,
                                      ExtraDataEntryNameMap extraDataEntryNameMap)
    {
        this.programClassPool        = programClassPool;
        this.libraryClassPool        = libraryClassPool;
        this.gsonRuntimeSettings     = gsonRuntimeSettings;
        this.serializationInfo       = serializationInfo;
        this.supportExposeAnnotation = gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation;
        this.extraDataEntryNameMap   = extraDataEntryNameMap;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // Make access public for _OptimizedTypeAdapterFactory.
        programClass.u2accessFlags &= ~AccessConstants.PRIVATE;
        programClass.u2accessFlags |= AccessConstants.PUBLIC;

        // Start adding new serialization methods.
        ClassBuilder classBuilder =
            new ClassBuilder(programClass);

        // Add toJson$ method.
        Integer classIndex           = serializationInfo.classIndices.get(programClass.getName());
        String  methodNameToJson     = METHOD_NAME_TO_JSON             + classIndex;
        String  methodNameToJsonBody = METHOD_NAME_TO_JSON_BODY        + classIndex;

        if (DEBUG)
        {
            System.out.println(
                "GsonSerializationOptimizer: adding " +
                methodNameToJson +
                " method to " + programClass.getName());
        }

        classBuilder.addMethod(
            AccessConstants.PUBLIC | AccessConstants.SYNTHETIC,
            methodNameToJson,
            METHOD_TYPE_TO_JSON,
            100,
            ____ -> ____
                // Begin Json object.
                .aload(OptimizedClassConstants.ToJsonLocals.JSON_WRITER)
                .invokevirtual(GsonClassConstants.NAME_JSON_WRITER,
                               GsonClassConstants.METHOD_NAME_WRITER_BEGIN_OBJECT,
                               GsonClassConstants.METHOD_TYPE_WRITER_BEGIN_OBJECT)

                // Invoke toJsonBody$.
                .aload(OptimizedClassConstants.ToJsonLocals.THIS)
                .aload(OptimizedClassConstants.ToJsonLocals.GSON)
                .aload(OptimizedClassConstants.ToJsonLocals.JSON_WRITER)
                .aload(OptimizedClassConstants.ToJsonLocals.OPTIMIZED_JSON_WRITER)
                .invokevirtual(programClass.getName(),
                               methodNameToJsonBody,
                               METHOD_TYPE_TO_JSON_BODY)

                // End Json object.
                .aload(OptimizedClassConstants.ToJsonLocals.JSON_WRITER)
                .invokevirtual(GsonClassConstants.NAME_JSON_WRITER,
                               GsonClassConstants.METHOD_NAME_WRITER_END_OBJECT,
                               GsonClassConstants.METHOD_TYPE_WRITER_END_OBJECT)
                .return_(),

            new ProgramMemberOptimizationInfoSetter());

        addToJsonBodyMethod(programClass, classBuilder);

        // Make sure all references in the class are initialized.
        programClass.accept(new ClassReferenceInitializer(programClassPool, libraryClassPool));

        // Link all methods with related ones.
        programClass.accept(new MethodLinker());
    }


    private void addToJsonBodyMethod(ProgramClass programClass,
                                     ClassBuilder classBuilder)
    {
        Integer classIndex = serializationInfo.classIndices.get(programClass.getName());
        String  methodName = METHOD_NAME_TO_JSON_BODY + classIndex;

        // Add toJsonBody$ method.
        if (DEBUG)
        {
            System.out.println(
                "GsonSerializationOptimizer: adding " +
                methodName +
                " method to " + programClass.getName());
        }

        classBuilder.addMethod(
            AccessConstants.PROTECTED | AccessConstants.SYNTHETIC,
            methodName,
            METHOD_TYPE_TO_JSON_BODY,
            1000,
            ____ ->
            {
                // Apply non static member visitor to all fields to visit.
                programClass.fieldsAccept(
                    new MemberAccessFilter(0,
                                           AccessConstants.SYNTHETIC |
                                           AccessConstants.STATIC,
                    new ToJsonFieldSerializationCodeAdder(____)));

                if (programClass.getSuperClass() != null)
                {
                    // Call the superclass toJsonBody$ if there is one.
                    if (!programClass.getSuperClass().getName().equals(ClassConstants.NAME_JAVA_LANG_OBJECT))
                    {
                        Integer superClassIndex =
                            serializationInfo.classIndices.get(programClass.getSuperClass().getName());
                        String superMethodNameToJsonBody = METHOD_NAME_TO_JSON_BODY + superClassIndex;

                        ____.aload(OptimizedClassConstants.ToJsonLocals.THIS)
                            .aload(OptimizedClassConstants.ToJsonLocals.GSON)
                            .aload(OptimizedClassConstants.ToJsonLocals.JSON_WRITER)
                            .aload(OptimizedClassConstants.ToJsonLocals.OPTIMIZED_JSON_WRITER)
                            .invokevirtual(programClass.getSuperClass().getName(),
                                           superMethodNameToJsonBody,
                                           METHOD_TYPE_TO_JSON_BODY);
                    }
                }
                else
                {
                    throw new RuntimeException ("Cannot find super class of " + programClass.getName() + " for Gson optimization. Please check your configuration includes all the expected library jars.");
                }

                ____.return_();
            },
            new ProgramMemberOptimizationInfoSetter());
    }


    private class ToJsonFieldSerializationCodeAdder
    implements    MemberVisitor
    {
        private final CompactCodeAttributeComposer ____;


        public ToJsonFieldSerializationCodeAdder(CompactCodeAttributeComposer ____)
        {
            this.____ = ____;
        }


        // Implementations for MemberVisitor.

        public void visitProgramField(ProgramClass programClass,
                                      ProgramField programField)
        {
            OptimizedJsonInfo.ClassJsonInfo classSerializationInfo =
                serializationInfo.classJsonInfos.get(programClass.getName());
            String[] jsonFieldNames = classSerializationInfo.javaToJsonFieldNames.get(programField.getName(programClass));
            String   javaFieldName  = programField.getName(programClass);

            if (jsonFieldNames != null)
            {
                // Derive field descriptor and signature (if it exists).
                String                  fieldDescriptor             = programField.getDescriptor(programClass);
                FieldSignatureCollector signatureAttributeCollector = new FieldSignatureCollector();
                programField.attributesAccept(programClass, signatureAttributeCollector);
                boolean retrieveAdapterByTypeToken = false;

                // Check for recursion first if it is an object
                CompactCodeAttributeComposer.Label end = ____.createLabel();
                if(ClassUtil.isInternalClassType(fieldDescriptor))
                {
                    CompactCodeAttributeComposer.Label noRecursion = ____.createLabel();
                    ____.aload(OptimizedClassConstants.ToJsonLocals.THIS)
                        .aload(OptimizedClassConstants.ToJsonLocals.THIS)
                        .getfield(programClass, programField)
                        .ifacmpne(noRecursion)
                        .goto_(end)
                        .label(noRecursion);
                }

                if (supportExposeAnnotation &&
                    !classSerializationInfo.exposedJavaFieldNames.contains(javaFieldName))
                {
                    ____.aload(ToJsonLocals.GSON)
                        .getfield(GsonClassConstants.NAME_GSON,     FIELD_NAME_EXCLUDER,       FIELD_TYPE_EXCLUDER)
                        .getfield(GsonClassConstants.NAME_EXCLUDER, FIELD_NAME_REQUIRE_EXPOSE, FIELD_TYPE_REQUIRE_EXPOSE)
                        .ifne(end);
                }


                // Write field name.
                Integer fieldIndex = serializationInfo.jsonFieldIndices.get(jsonFieldNames[0]);
                ____.aload(OptimizedClassConstants.ToJsonLocals.OPTIMIZED_JSON_WRITER)
                    .aload(OptimizedClassConstants.ToJsonLocals.JSON_WRITER)
                    .ldc(fieldIndex.intValue())
                    .invokeinterface(OptimizedClassConstants.NAME_OPTIMIZED_JSON_WRITER,
                                     OptimizedClassConstants.METHOD_NAME_NAME,
                                     OptimizedClassConstants.METHOD_TYPE_NAME);

                // Write field value.
                InlineSerializer inlineSerializer = inlineSerializers.get(fieldDescriptor);
                if (!gsonRuntimeSettings.registerTypeAdapterFactory &&
                    inlineSerializer != null &&
                    inlineSerializer.canSerialize(programClassPool, gsonRuntimeSettings))
                {
                    inlineSerializer.serialize(programClass,
                                               programField,
                                               ____,
                                               gsonRuntimeSettings);
                }
                else
                {
                    // Write value to Json writer based on declared type and runtime value/type.
                    ____.aload(OptimizedClassConstants.ToJsonLocals.GSON);

                    switch (fieldDescriptor.charAt(0))
                    {
                        case TypeConstants.BOOLEAN:
                        case TypeConstants.CHAR:
                        case TypeConstants.BYTE:
                        case TypeConstants.SHORT:
                        case TypeConstants.INT:
                        case TypeConstants.FLOAT:
                        case TypeConstants.LONG:
                        case TypeConstants.DOUBLE:
                        {
                            String className = ClassUtil.internalNumericClassNameFromPrimitiveType(fieldDescriptor.charAt(0));
                            ____.getstatic(className, ClassConstants.FIELD_NAME_TYPE, ClassConstants.FIELD_TYPE_TYPE);
                            break;
                        }
                        case TypeConstants.CLASS_START:
                        {
                            if (signatureAttributeCollector.getFieldSignature() == null)
                            {
                                String fieldClassName = fieldDescriptor.substring(1, fieldDescriptor.length() - 1);
                                Clazz  fieldClass     = programClassPool.getClass(fieldClassName);
                                if (fieldClass == null)
                                {
                                    fieldClass = libraryClassPool.getClass(fieldClassName);
                                }
                                ____.ldc(fieldClassName, fieldClass);
                            }
                            else
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
                                extraDataEntryNameMap.addExtraClassToClass(programClass.getName(),
                                                                           typeTokenClass.getName());

                                // Instantiate type token.
                                ____.new_(typeTokenClass.getName())
                                    .dup()
                                    .invokespecial(typeTokenClass.getName(),
                                                   ClassConstants.METHOD_NAME_INIT,
                                                   ClassConstants.METHOD_TYPE_INIT);
                                retrieveAdapterByTypeToken = true;
                            }
                            break;
                        }
                        case TypeConstants.ARRAY:
                        {
                            String elementType = ClassUtil.internalTypeFromArrayType(fieldDescriptor);

                            Clazz fieldClass;
                            if (ClassUtil.isInternalPrimitiveType(elementType))
                            {
                                String primitiveClassName =
                                    ClassUtil.internalNumericClassNameFromPrimitiveType(elementType.charAt(0));
                                fieldClass = libraryClassPool.getClass(primitiveClassName);
                                ____.ldc(fieldDescriptor, fieldClass);
                            }
                            else
                            {
                                String className = ClassUtil.internalClassNameFromClassType(elementType);

                                fieldClass = programClassPool.getClass(className);
                                if (fieldClass == null)
                                {
                                    fieldClass = libraryClassPool.getClass(className);
                                }
                                ____.ldc(fieldDescriptor, fieldClass);
                            }

                            break;
                        }
                    }

                    ____.aload(OptimizedClassConstants.ToJsonLocals.THIS)
                        .getfield(programClass, programField);

                    // Box primitive value before passing it to type adapter.
                    switch (fieldDescriptor.charAt(0))
                    {
                        case TypeConstants.BOOLEAN:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_BOOLEAN,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_BOOLEAN);
                            break;
                        case TypeConstants.CHAR:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_CHARACTER,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_CHAR);
                            break;
                        case TypeConstants.BYTE:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_BYTE,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_BYTE);
                            break;
                        case TypeConstants.SHORT:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_SHORT,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_SHORT);
                            break;
                        case TypeConstants.INT:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_INTEGER,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_INT);
                            break;
                        case TypeConstants.FLOAT:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_FLOAT,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_FLOAT);
                            break;
                        case TypeConstants.LONG:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_LONG,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_LONG);
                            break;
                        case TypeConstants.DOUBLE:
                            ____.invokestatic(ClassConstants.NAME_JAVA_LANG_DOUBLE,
                                              ClassConstants.METHOD_NAME_VALUE_OF,
                                              ClassConstants.METHOD_TYPE_VALUE_OF_DOUBLE);
                            break;
                    }

                    // Copy value to local.
                    ____.dup()
                        .astore(VALUE_VARIABLE_INDEX);

                    // Retrieve type adapter.
                    if(retrieveAdapterByTypeToken)
                    {
                        ____.invokestatic(OptimizedClassConstants.NAME_GSON_UTIL,
                                          OptimizedClassConstants.METHOD_NAME_GET_TYPE_ADAPTER_TYPE_TOKEN,
                                          OptimizedClassConstants.METHOD_TYPE_GET_TYPE_ADAPTER_TYPE_TOKEN);
                    }
                    else
                    {
                        ____.invokestatic(OptimizedClassConstants.NAME_GSON_UTIL,
                                          OptimizedClassConstants.METHOD_NAME_GET_TYPE_ADAPTER_CLASS,
                                          OptimizedClassConstants.METHOD_TYPE_GET_TYPE_ADAPTER_CLASS);
                    }

                    // Write value using type adapter.
                    ____.aload(OptimizedClassConstants.ToJsonLocals.JSON_WRITER)
                        .aload(VALUE_VARIABLE_INDEX)
                        .invokevirtual(GsonClassConstants.NAME_TYPE_ADAPTER,
                                       GsonClassConstants.METHOD_NAME_WRITE,
                                       GsonClassConstants.METHOD_TYPE_WRITE);
                }

                // Label for skipping writing of field in case of recursion or
                // a non-exposed field with excludeFieldsWithoutExposeAnnotation
                // enabled.
                ____.label(end);
            }
        }
    }
}
