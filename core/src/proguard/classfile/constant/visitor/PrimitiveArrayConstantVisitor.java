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
package proguard.classfile.constant.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.PrimitiveArrayConstant;

/**
 * This interface specifies the methods for a visitor of PrimitiveArrayConstant
 * instances containing different types of arrays.
 *
 * @author Eric Lafortune
 */
public interface PrimitiveArrayConstantVisitor
{
    public void visitBooleanArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, boolean[] values);
    public void visitByteArrayConstant(   Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, byte[]    values);
    public void visitCharArrayConstant(   Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, char[]    values);
    public void visitShortArrayConstant(  Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, short[]   values);
    public void visitIntArrayConstant(    Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, int[]     values);
    public void visitFloatArrayConstant(  Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, float[]   values);
    public void visitLongArrayConstant(   Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, long[]    values);
    public void visitDoubleArrayConstant( Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant, double[]  values);
}
