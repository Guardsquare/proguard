/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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

import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.evaluation.value.InstructionOffsetValue;
import proguard.util.ArrayUtil;

/**
 * This instructions visitor is used for 2 purposes.
 *
 * 1. This instruction visitor searches for types behind Class and TypeToken
 * (from the Gson library) arguments. The intent is to let a producing instruction
 * that put a Type/Class/TypeToken on the stack accept this visitor, which determines
 * the actual type that is being deserialized.
 *
 * Currently two types of situations are supported:
 * - You pass the type via a .class call (ldc instruction)
 *   example:
 *     fromJson("", MyClass.class)
 *   result: MyClass
 * - You pass the type from a getType call via a variable (aload instruction)
 *   example:
 *     Type type = new TypeToken<MyClass>() {}.getType();
 *     fromJson("", type);
 *   result: MyClass
 *
 * 2. This instruction visitor returns the type of a TypeAdapter argument.
 *
 * - When a TypeAdapter is created (new instruction)
 *   example:
 *     registerTypeHierarchyAdapter(MyClass.class, new MyTypeAdapter())
 *   result: MyTypeAdapter
 */
class      TypeArgumentFinder
implements InstructionVisitor,
           ConstantVisitor
{
    private final ClassPool        programClassPool;
    private final ClassPool        libraryClassPool;
    private final PartialEvaluator partialEvaluator;
                  String[]         typeArgumentClasses;


    /**
     * Creates a new TypeArgumentFinder.
     *
     * @param programClassPool the program class pool used for looking up
     *                         class references.
     * @param libraryClassPool the library class pool used for looking up
     *                         class references.
     * @param partialEvaluator the partial evaluator used to evaluate visited
     *                         code attributes.
     */
    TypeArgumentFinder(ClassPool        programClassPool,
                       ClassPool        libraryClassPool,
                       PartialEvaluator partialEvaluator)
    {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
        this.partialEvaluator = partialEvaluator;
    }


    // Implementations for InstructionVisitor.

    @Override
    public void visitAnyInstruction(Clazz         clazz,
                                    Method        method,
                                    CodeAttribute codeAttribute,
                                    int           offset,
                                    Instruction   instruction) {}

    @Override
    public void visitVariableInstruction(Clazz               clazz,
                                         Method              method,
                                         CodeAttribute       codeAttribute,
                                         int                 offset,
                                         VariableInstruction variableInstruction)
    {
        if (variableInstruction.canonicalOpcode() == Instruction.OP_ALOAD)
        {
            // Find the operation that stored the loaded Type.
            LastStoreFinder lastStoreFinder = new LastStoreFinder(variableInstruction.variableIndex);
            codeAttribute.instructionsAccept(clazz, method, 0, offset, lastStoreFinder);

            if (lastStoreFinder.lastStore != null)
            {
                // Find out which instruction produced the stored Type.
                TracedStack stackBeforeStore = partialEvaluator.getStackBefore(lastStoreFinder.lastStoreOffset);
                InstructionOffsetValue instructionOffsetValue = stackBeforeStore.getTopProducerValue(0).instructionOffsetValue();

                // Derive the signature of the subclass of TypeToken from which the Type is retrieved.
                TypeTokenSignatureFinder typeTokenFinder = new TypeTokenSignatureFinder();
                for (int offsetIndex = 0; offsetIndex < instructionOffsetValue.instructionOffsetCount(); offsetIndex++)
                {
                    int instructionOffset = instructionOffsetValue.instructionOffset(offsetIndex);
                    codeAttribute.instructionAccept(clazz, method, instructionOffset, typeTokenFinder);
                }

                // Derive the classes from the signature of the TypeToken subclass.
                if (typeTokenFinder.typeTokenSignature != null)
                {
                    typeArgumentClasses = new String[0];
                    Clazz[] referencedClasses = typeTokenFinder.typeTokenSignature.referencedClasses;
                    for (Clazz referencedClass : referencedClasses)
                    {
                        if (referencedClass!= null &&
                            !referencedClass.getName().equals(GsonClassConstants.NAME_TYPE_TOKEN))
                        {
                            typeArgumentClasses = ArrayUtil.add(typeArgumentClasses,
                                                                typeArgumentClasses.length,
                                                                referencedClass.getName());
                        }
                    }
                }
            }
        }
    }


    public void visitConstantInstruction(Clazz               clazz,
                                         Method              method,
                                         CodeAttribute       codeAttribute,
                                         int                 offset,
                                         ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            // Used in cases where a .class argument is passed.
            case Instruction.OP_LDC: // Fallthrough
            case Instruction.OP_LDC2_W: // Fallthrough
            case Instruction.OP_LDC_W: // Fallthrough
            // Used in the case where a new TypeAdapter is passed.
            case Instruction.OP_NEW:
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;
            default:
                // These cases are not supported, so we do not update the typeArgumentClasses array.
        }
    }

    // Implementations for ConstantVisitor.


    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant)
    {
    }

    @Override
    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        typeArgumentClasses = new String[] { refConstant.getClassName(clazz) };
    }

    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        typeArgumentClasses = new String[] { classConstant.getName(clazz) };
    }

    private static class LastStoreFinder
    implements           InstructionVisitor
    {
        private final int           variableIndex;
        private int                 lastStoreOffset;
        private VariableInstruction lastStore;

        public LastStoreFinder(int variableIndex)
        {
            this.variableIndex = variableIndex;
        }

        // Implementations for InstructionVisitor.

        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
        {
        }

        @Override
        public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
        {
            if(variableInstruction.variableIndex == variableIndex &&
               variableInstruction.canonicalOpcode() == Instruction.OP_ASTORE){
                lastStoreOffset = offset;
                lastStore = variableInstruction;
            }
        }
    }

    private class TypeTokenSignatureFinder
    implements    InstructionVisitor,
                  ConstantVisitor,
                  AttributeVisitor
    {

        private SignatureAttribute typeTokenSignature;

        // Implementations for InstructionVisitor.

        @Override
        public void visitAnyInstruction(Clazz         clazz,
                                        Method        method,
                                        CodeAttribute codeAttribute,
                                        int           offset,
                                        Instruction   instruction)
        {
        }

        @Override
        public void visitConstantInstruction(Clazz               clazz,
                                             Method              method,
                                             CodeAttribute       codeAttribute,
                                             int                 offset,
                                             ConstantInstruction constantInstruction)
        {
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
        }

        // Implementations for ConstantVisitor.

        @Override
        public void visitAnyConstant(Clazz clazz, Constant constant)
        {
        }

        @Override
        public void visitMethodrefConstant(Clazz             clazz,
                                           MethodrefConstant methodrefConstant)
        {
            if (methodrefConstant.referencedClass != null)
            {
                if (GsonClassConstants.NAME_TYPE_TOKEN.equals(methodrefConstant.referencedClass.getName()) &&
                    GsonClassConstants.METHOD_NAME_GET_TYPE.equals(methodrefConstant.getName(clazz)))
                {
                    programClassPool.classAccept(methodrefConstant.getClassName(clazz),
                                                 new AllAttributeVisitor(this));
                    libraryClassPool.classAccept(methodrefConstant.getClassName(clazz),
                                                 new AllAttributeVisitor(this));
                }
            }
        }

        // Implementations for AttributeVisitor.

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute)
        {
        }

        @Override
        public void visitSignatureAttribute(Clazz              clazz,
                                            SignatureAttribute signatureAttribute)
        {
            typeTokenSignature = signatureAttribute;
        }
    }
}

