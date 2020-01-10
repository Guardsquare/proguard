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
package proguard.classfile.attribute.preverification.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.preverification.*;

/**
 * This interface specifies the methods for a visitor of
 * {@link VerificationType} instances. There a methods for stack entries
 * and methods for variable entries.
 *
 * @author Eric Lafortune
 */
public interface VerificationTypeVisitor
{
    public void visitIntegerType(          Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, IntegerType           integerType);
    public void visitFloatType(            Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, FloatType             floatType);
    public void visitLongType(             Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, LongType              longType);
    public void visitDoubleType(           Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, DoubleType            doubleType);
    public void visitTopType(              Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, TopType               topType);
    public void visitObjectType(           Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ObjectType            objectType);
    public void visitNullType(             Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, NullType              nullType);
    public void visitUninitializedType(    Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, UninitializedType     uninitializedType);
    public void visitUninitializedThisType(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, UninitializedThisType uninitializedThisType);

    public void visitStackIntegerType(          Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, IntegerType           integerType);
    public void visitStackFloatType(            Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, FloatType             floatType);
    public void visitStackLongType(             Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, LongType              longType);
    public void visitStackDoubleType(           Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, DoubleType            doubleType);
    public void visitStackTopType(              Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, TopType               topType);
    public void visitStackObjectType(           Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, ObjectType            objectType);
    public void visitStackNullType(             Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, NullType              nullType);
    public void visitStackUninitializedType(    Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, UninitializedType     uninitializedType);
    public void visitStackUninitializedThisType(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, UninitializedThisType uninitializedThisType);

    public void visitVariablesIntegerType(          Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, IntegerType           integerType);
    public void visitVariablesFloatType(            Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, FloatType             floatType);
    public void visitVariablesLongType(             Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, LongType              longType);
    public void visitVariablesDoubleType(           Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, DoubleType            doubleType);
    public void visitVariablesTopType(              Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, TopType               topType);
    public void visitVariablesObjectType(           Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, ObjectType            objectType);
    public void visitVariablesNullType(             Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, NullType              nullType);
    public void visitVariablesUninitializedType(    Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, UninitializedType     uninitializedType);
    public void visitVariablesUninitializedThisType(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int index, UninitializedThisType uninitializedThisType);
}
