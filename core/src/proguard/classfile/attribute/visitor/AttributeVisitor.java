/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.attribute.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.module.*;
import proguard.classfile.attribute.preverification.*;

/**
 * This interface specifies the methods for a visitor of {@link Attribute}
 * instances.
 *
 * @author Eric Lafortune
 */
public interface AttributeVisitor
{
    // Attributes that are attached to classes.

    public void visitUnknownAttribute(               Clazz clazz,                UnknownAttribute              unknownAttribute);
    public void visitBootstrapMethodsAttribute(      Clazz clazz,                BootstrapMethodsAttribute     bootstrapMethodsAttribute);
    public void visitSourceFileAttribute(            Clazz clazz,                SourceFileAttribute           sourceFileAttribute);
    public void visitSourceDirAttribute(             Clazz clazz,                SourceDirAttribute            sourceDirAttribute);
    public void visitSourceDebugExtensionAttribute(  Clazz clazz,                SourceDebugExtensionAttribute sourceDebugExtensionAttribute);
    public void visitInnerClassesAttribute(          Clazz clazz,                InnerClassesAttribute         innerClassesAttribute);
    public void visitEnclosingMethodAttribute(       Clazz clazz,                EnclosingMethodAttribute      enclosingMethodAttribute);
    public void visitNestHostAttribute(              Clazz clazz,                NestHostAttribute             nestHostAttribute);
    public void visitNestMembersAttribute(           Clazz clazz,                NestMembersAttribute          nestMembersAttribute);
    public void visitModuleAttribute(                Clazz clazz,                ModuleAttribute               moduleAttribute);
    public void visitModuleMainClassAttribute(       Clazz clazz,                ModuleMainClassAttribute      moduleMainClassAttribute);
    public void visitModulePackagesAttribute(        Clazz clazz,                ModulePackagesAttribute       modulePackagesAttribute);

    // Attributes that are attached to classes, fields, or methods.

    public void visitDeprecatedAttribute(            Clazz clazz,                DeprecatedAttribute deprecatedAttribute);
    public void visitDeprecatedAttribute(            Clazz clazz, Field  field,  DeprecatedAttribute deprecatedAttribute);
    public void visitDeprecatedAttribute(            Clazz clazz, Method method, DeprecatedAttribute deprecatedAttribute);

    public void visitSyntheticAttribute(             Clazz clazz,                SyntheticAttribute  syntheticAttribute);
    public void visitSyntheticAttribute(             Clazz clazz, Field  field,  SyntheticAttribute  syntheticAttribute);
    public void visitSyntheticAttribute(             Clazz clazz, Method method, SyntheticAttribute  syntheticAttribute);

    public void visitSignatureAttribute(             Clazz clazz,                SignatureAttribute  signatureAttribute);
    public void visitSignatureAttribute(             Clazz clazz, Field  field,  SignatureAttribute  signatureAttribute);
    public void visitSignatureAttribute(             Clazz clazz, Method method, SignatureAttribute  signatureAttribute);

    // Attributes that are attached to fields.

    public void visitConstantValueAttribute(         Clazz clazz, Field  field,  ConstantValueAttribute constantValueAttribute);

    // Attributes that are attached to methods.

    public void visitMethodParametersAttribute(      Clazz clazz, Method method, MethodParametersAttribute methodParametersAttribute);
    public void visitExceptionsAttribute(            Clazz clazz, Method method, ExceptionsAttribute       exceptionsAttribute);
    public void visitCodeAttribute(                  Clazz clazz, Method method, CodeAttribute             codeAttribute);

    // Attributes that are attached to code attributes.

    public void visitStackMapAttribute(              Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute               stackMapAttribute);
    public void visitStackMapTableAttribute(         Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute          stackMapTableAttribute);
    public void visitLineNumberTableAttribute(       Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute        lineNumberTableAttribute);
    public void visitLocalVariableTableAttribute(    Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute     localVariableTableAttribute);
    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute);

    // Annotation attributes.

    public void visitRuntimeVisibleAnnotationsAttribute(           Clazz clazz,                RuntimeVisibleAnnotationsAttribute   runtimeVisibleAnnotationsAttribute);
    public void visitRuntimeVisibleAnnotationsAttribute(           Clazz clazz, Field  field,  RuntimeVisibleAnnotationsAttribute   runtimeVisibleAnnotationsAttribute);
    public void visitRuntimeVisibleAnnotationsAttribute(           Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute   runtimeVisibleAnnotationsAttribute);

    public void visitRuntimeInvisibleAnnotationsAttribute(         Clazz clazz,                RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute);
    public void visitRuntimeInvisibleAnnotationsAttribute(         Clazz clazz, Field  field,  RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute);
    public void visitRuntimeInvisibleAnnotationsAttribute(         Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute);

    public void visitRuntimeVisibleParameterAnnotationsAttribute(  Clazz clazz, Method method, RuntimeVisibleParameterAnnotationsAttribute   runtimeVisibleParameterAnnotationsAttribute);
    public void visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute);

    public void visitRuntimeVisibleTypeAnnotationsAttribute(       Clazz clazz,                                             RuntimeVisibleTypeAnnotationsAttribute   runtimeVisibleTypeAnnotationsAttribute);
    public void visitRuntimeVisibleTypeAnnotationsAttribute(       Clazz clazz, Field  field,                               RuntimeVisibleTypeAnnotationsAttribute   runtimeVisibleTypeAnnotationsAttribute);
    public void visitRuntimeVisibleTypeAnnotationsAttribute(       Clazz clazz, Method method,                              RuntimeVisibleTypeAnnotationsAttribute   runtimeVisibleTypeAnnotationsAttribute);
    public void visitRuntimeVisibleTypeAnnotationsAttribute(       Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeVisibleTypeAnnotationsAttribute   runtimeVisibleTypeAnnotationsAttribute);

    public void visitRuntimeInvisibleTypeAnnotationsAttribute(     Clazz clazz,                                             RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute);
    public void visitRuntimeInvisibleTypeAnnotationsAttribute(     Clazz clazz, Field  field,                               RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute);
    public void visitRuntimeInvisibleTypeAnnotationsAttribute(     Clazz clazz, Method method,                              RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute);
    public void visitRuntimeInvisibleTypeAnnotationsAttribute(     Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute);

    public void visitAnnotationDefaultAttribute(                   Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute);
}
