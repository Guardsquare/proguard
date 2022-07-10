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
package proguard.optimize.kotlin;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.*;
import proguard.classfile.visitor.MemberVisitor;

public class KotlinLambdaGroupInitUpdater implements AttributeVisitor, MemberVisitor {

    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaGroupInitUpdater.class);

    public KotlinLambdaGroupInitUpdater(ClassPool programClassPool,
                                        ClassPool libraryClassPool)
    {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        logger.trace("Updating method {} in class {}",
                     ClassUtil.externalFullMethodDescription(programClass.getName(),
                                                             programMethod.getAccessFlags(),
                                                             programMethod.getName(programClass),
                                                             programMethod.getDescriptor(programClass)),
                                                             ClassUtil.externalClassName(programClass.getName()));
        updateInitMethodDescriptor(programClass, programMethod);
        programMethod.attributesAccept(programClass, this);
    }

    private void updateInitMethodDescriptor(ProgramClass programClass, ProgramMethod programMethod) {
        String newInitDescriptor = getNewInitMethodDescriptor(programClass, programMethod);
        programMethod.u2descriptorIndex = new ConstantAdder(programClass).addConstant(programClass,
                                                                                      new Utf8Constant(newInitDescriptor));
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // This attribute visitor should only be used for program classes.
        visitCodeAttribute((ProgramClass) clazz, (ProgramMethod) method, codeAttribute);
    }

    public void visitCodeAttribute(ProgramClass programClass, ProgramMethod programMethod, CodeAttribute codeAttribute)
    {
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

        // The arity argument is the last of all and has a size of 1 byte.
        // Note that the arguments start counting from 1, as the class itself is at byte 0.
        // long and double arguments take 2 bytes.
        // The classId and arity arguments are the 2 last, which take 1 byte each, as they are of type int.
        int arityIndex = ClassUtil.internalMethodParameterSize(programMethod.getDescriptor(programClass), false) - 1;

        InstructionSequencesReplacer replacer = createInstructionSequenceReplacer(branchTargetFinder,
                                                                                  codeAttributeEditor,
                                                                                  arityIndex,
                                                                                  programClass);

        codeAttribute.accept(programClass,
                             programMethod,
                             new PeepholeEditor(branchTargetFinder,
                                                codeAttributeEditor,
                                                replacer));
    }

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    public static String getNewInitMethodDescriptor(ProgramClass programClass, ProgramMethod programMethod)
    {
        String oldInitDescriptor = programMethod.getDescriptor(programClass);
        return oldInitDescriptor.substring(0, oldInitDescriptor.length() - 2) + "II)V";
    }

    private InstructionSequencesReplacer createInstructionSequenceReplacer(BranchTargetFinder branchTargetFinder,
                                                                           CodeAttributeEditor codeAttributeEditor,
                                                                           int arityIndex,
                                                                           ProgramClass lambdaGroup)
    {
        InstructionSequenceBuilder builder = new InstructionSequenceBuilder(programClassPool, libraryClassPool);
        Instruction[][][] replacementPatterns = createReplacementPatternsForInit(builder, arityIndex, lambdaGroup);
        return new InstructionSequencesReplacer(builder.constants(),
                                                replacementPatterns,
                                                branchTargetFinder,
                                                codeAttributeEditor);
    }

    private Instruction[][][] createReplacementPatternsForInit(InstructionSequenceBuilder builder,
                                                               int arityIndex,
                                                               ProgramClass lambdaGroup)
    {
        final int X = InstructionSequenceMatcher.X;
        return new Instruction[][][]
                {
                        {
                                builder.aload_0()
                                       .iconst(X)
                                       .invokespecial(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA,
                                                      ClassConstants.METHOD_NAME_INIT,
                                                      "(I)V")
                                       .__(),
                                builder.aload_0()
                                       .iload(arityIndex - 1)
                                       .putfield(lambdaGroup, // store the id in a field
                                                 lambdaGroup
                                                 .findField(KotlinLambdaGroupBuilder.FIELD_NAME_ID,
                                                            KotlinLambdaGroupBuilder.FIELD_TYPE_ID))
                                       .aload_0()
                                       .iload(arityIndex)
                                       .invokespecial(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA,
                                                      ClassConstants.METHOD_NAME_INIT,
                                                      "(I)V")
                                       .__()
                        }
                };
    }
}
