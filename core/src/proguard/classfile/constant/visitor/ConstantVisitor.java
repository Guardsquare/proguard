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
import proguard.classfile.constant.*;


/**
 * This interface specifies the methods for a visitor of {@link Constant}
 * instances.
 *
 * @author Eric Lafortune
 */
public interface ConstantVisitor
{
    public void visitIntegerConstant(           Clazz clazz, IntegerConstant            integerConstant);
    public void visitLongConstant(              Clazz clazz, LongConstant               longConstant);
    public void visitFloatConstant(             Clazz clazz, FloatConstant              floatConstant);
    public void visitDoubleConstant(            Clazz clazz, DoubleConstant             doubleConstant);
    public void visitPrimitiveArrayConstant(    Clazz clazz, PrimitiveArrayConstant     primitiveArrayConstant);
    public void visitStringConstant(            Clazz clazz, StringConstant             stringConstant);
    public void visitUtf8Constant(              Clazz clazz, Utf8Constant               utf8Constant);
    public void visitDynamicConstant(           Clazz clazz, DynamicConstant            dynamicConstant);
    public void visitInvokeDynamicConstant(     Clazz clazz, InvokeDynamicConstant      invokeDynamicConstant);
    public void visitMethodHandleConstant(      Clazz clazz, MethodHandleConstant       methodHandleConstant);
    public void visitFieldrefConstant(          Clazz clazz, FieldrefConstant           fieldrefConstant);
    public void visitInterfaceMethodrefConstant(Clazz clazz, InterfaceMethodrefConstant interfaceMethodrefConstant);
    public void visitMethodrefConstant(         Clazz clazz, MethodrefConstant          methodrefConstant);
    public void visitClassConstant(             Clazz clazz, ClassConstant              classConstant);
    public void visitMethodTypeConstant(        Clazz clazz, MethodTypeConstant         methodTypeConstant);
    public void visitNameAndTypeConstant(       Clazz clazz, NameAndTypeConstant        nameAndTypeConstant);
    public void visitModuleConstant(            Clazz clazz, ModuleConstant             moduleConstant);
    public void visitPackageConstant(           Clazz clazz, PackageConstant            packageConstant);
}
