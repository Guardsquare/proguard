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
package proguard.classfile.attribute.annotation.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.annotation.*;

/**
 * This interface specifies the methods for a visitor of {@link ElementValue}
 * instances.
 *
 * @author Eric Lafortune
 */
public interface ElementValueVisitor
{
    public void visitConstantElementValue(    Clazz clazz,                Annotation annotation, ConstantElementValue     constantElementValue);
    public void visitEnumConstantElementValue(Clazz clazz,                Annotation annotation, EnumConstantElementValue enumConstantElementValue);
    public void visitClassElementValue(       Clazz clazz,                Annotation annotation, ClassElementValue        classElementValue);
    public void visitAnnotationElementValue(  Clazz clazz,                Annotation annotation, AnnotationElementValue   annotationElementValue);
    public void visitArrayElementValue(       Clazz clazz,                Annotation annotation, ArrayElementValue        arrayElementValue);

//    public void visitConstantElementValue(    Clazz clazz, Field  field,  Annotation annotation, ConstantElementValue     constantElementValue);
//    public void visitEnumConstantElementValue(Clazz clazz, Field  field,  Annotation annotation, EnumConstantElementValue enumConstantElementValue);
//    public void visitClassElementValue(       Clazz clazz, Field  field,  Annotation annotation, ClassElementValue        classElementValue);
//    public void visitAnnotationElementValue(  Clazz clazz, Field  field,  Annotation annotation, AnnotationElementValue   annotationElementValue);
//    public void visitArrayElementValue(       Clazz clazz, Field  field,  Annotation annotation, ArrayElementValue        arrayElementValue);
//
//    public void visitConstantElementValue(    Clazz clazz, Method method, Annotation annotation, ConstantElementValue     constantElementValue);
//    public void visitEnumConstantElementValue(Clazz clazz, Method method, Annotation annotation, EnumConstantElementValue enumConstantElementValue);
//    public void visitClassElementValue(       Clazz clazz, Method method, Annotation annotation, ClassElementValue        classElementValue);
//    public void visitAnnotationElementValue(  Clazz clazz, Method method, Annotation annotation, AnnotationElementValue   annotationElementValue);
//    public void visitArrayElementValue(       Clazz clazz, Method method, Annotation annotation, ArrayElementValue        arrayElementValue);
}
