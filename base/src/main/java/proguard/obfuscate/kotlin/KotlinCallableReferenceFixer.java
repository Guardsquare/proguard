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

package proguard.obfuscate.kotlin;

import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.reflect.visitor.CallableReferenceInfoToOwnerVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.kotlin.visitor.filter.KotlinDeclarationContainerFilter;
import proguard.classfile.util.InstructionSequenceMatcher;
import proguard.classfile.visitor.MultiClassVisitor;
import proguard.classfile.visitor.NamedMethodVisitor;
import proguard.obfuscate.util.InstructionSequenceObfuscator;
import proguard.obfuscate.util.ReplacementSequences;
import proguard.resources.kotlinmodule.visitor.KotlinMetadataToModuleVisitor;

import static proguard.classfile.ClassConstants.METHOD_NAME_INIT;
import static proguard.classfile.util.InstructionSequenceMatcher.*;

/**
 * This class fixes the CallableReference implementations of function and property references.
 *
 * @author James Hamilton
 */
public class KotlinCallableReferenceFixer
implements   KotlinMetadataVisitor
{
    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;

    public KotlinCallableReferenceFixer(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
    }

    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata syntheticClass)
    {
        if (syntheticClass.callableReferenceInfo != null)
        {
            if (syntheticClass.mv[0] == 1 && syntheticClass.mv[1] > 3)
            {
                clazz.methodAccept(METHOD_NAME_INIT,null,
                new InstructionSequenceObfuscator(
                        new NameAndSignatureReplacementSequences(
                            syntheticClass.callableReferenceInfo.getName(),
                            syntheticClass.callableReferenceInfo.getSignature(),
                            programClassPool,
                            libraryClassPool)));
            }
            else
            {
                clazz.accept(
                    new MultiClassVisitor(
                        new NamedMethodVisitor(
                            KotlinConstants.REFLECTION.GETNAME_METHOD_NAME,
                            KotlinConstants.REFLECTION.GETNAME_METHOD_DESC,
                            // getName() returns the Kotlin name of the callable, the one which was declared in the source code (@JvmName doesn't change it).
                            new InstructionSequenceObfuscator(
                            new NameOrSignatureReplacementSequences(
                                syntheticClass.callableReferenceInfo.getName(), programClassPool, libraryClassPool))),

                        new NamedMethodVisitor(
                            KotlinConstants.REFLECTION.GETSIGNATURE_METHOD_NAME,
                            KotlinConstants.REFLECTION.GETSIGNATURE_METHOD_DESC,
                            //getSignature() returns the signature.
                            new InstructionSequenceObfuscator(
                            new NameOrSignatureReplacementSequences(
                                syntheticClass.callableReferenceInfo.getSignature(), programClassPool, libraryClassPool)))
                        ));

                // getOwner() returns the Kotlin class or package (for file facades and multi-file class parts)
                // where the callable should be located, usually specified on the LHS of the '::' operator but it could also be a superclass.
                // We update getOwner() only for file facades and multi-file class parts because creating a Kotlin package
                // requires the module name.

                syntheticClass.callableReferenceInfoAccept(
                    new CallableReferenceInfoToOwnerVisitor(
                    new KotlinDeclarationContainerFilter(
                        declarationContainer -> declarationContainer.k == KotlinConstants.METADATA_KIND_FILE_FACADE ||
                                                declarationContainer.k == KotlinConstants.METADATA_KIND_MULTI_FILE_CLASS_PART,
                    new KotlinMetadataToModuleVisitor(
                        kotlinModule         -> clazz.accept(
                                                    new NamedMethodVisitor(
                                                        KotlinConstants.REFLECTION.GETOWNER_METHOD_NAME,
                                                        KotlinConstants.REFLECTION.GETOWNER_METHOD_DESC,
                                                        new InstructionSequenceObfuscator(
                                                        new OwnerReplacementSequences(
                                                            kotlinModule.name, programClassPool, libraryClassPool))))))));
            }
        }
    }

    public static final class NameOrSignatureReplacementSequences
    implements                ReplacementSequences
    {
        private final Instruction[][][] SEQUENCES;
        private final Constant[]        CONSTANTS;

        NameOrSignatureReplacementSequences(String name, ClassPool programClassPool, ClassPool libraryClassPool)
        {
            InstructionSequenceBuilder ____ = new InstructionSequenceBuilder(programClassPool, libraryClassPool);

            SEQUENCES = new Instruction[][][] {
                {
                    ____
                        .ldc_(InstructionSequenceMatcher.X)
                        .areturn().__(),

                    ____
                        .ldc(name)
                        .areturn().__()
                },
            };

            CONSTANTS = ____.constants();
        }

        @Override
        public Instruction[][][] getSequences()
        {
            return SEQUENCES;
        }

        @Override
        public Constant[] getConstants()
        {
            return CONSTANTS;
        }
    }

    public static final class NameAndSignatureReplacementSequences
    implements                ReplacementSequences
    {
        private static final int OWNER_INDEX     = A;
        private static final int NAME_INDEX      = B;
        private static final int SIGNATURE_INDEX = C;
        private static final int FLAGS_INDEX     = D;

        private final Instruction[][][] SEQUENCES;
        private final Constant[]        CONSTANTS;

        NameAndSignatureReplacementSequences(String name, String signature, ClassPool programClassPool, ClassPool libraryClassPool)
        {
            InstructionSequenceBuilder ____ = new InstructionSequenceBuilder(programClassPool, libraryClassPool);

            SEQUENCES = new Instruction[][][] {
                {
                    ____.ldc_(OWNER_INDEX)
                        .ldc_(NAME_INDEX)
                        .ldc_(SIGNATURE_INDEX)
                        .ldc_(FLAGS_INDEX)
                        .invokespecial(X).__(),

                    ____.ldc_(OWNER_INDEX)
                        .ldc(name)
                        .ldc(signature)
                        .ldc_(FLAGS_INDEX)
                        .invokespecial(X).__(),
                },
            };

            CONSTANTS = ____.constants();
        }

        @Override
        public Instruction[][][] getSequences()
        {
            return SEQUENCES;
        }

        @Override
        public Constant[] getConstants()
        {
            return CONSTANTS;
        }
    }

    public static final class OwnerReplacementSequences
    implements                ReplacementSequences
    {
        private final Instruction[][][] SEQUENCES;
        private final Constant[]        CONSTANTS;

        OwnerReplacementSequences(String name, ClassPool programClassPool, ClassPool libraryClassPool)
        {
            InstructionSequenceBuilder ____ = new InstructionSequenceBuilder(programClassPool, libraryClassPool);

            SEQUENCES = new Instruction[][][] {
                {
                    ____
                        .ldc_(InstructionSequenceMatcher.X)
                        .ldc_(InstructionSequenceMatcher.Y)
                        .invokestatic(KotlinConstants.REFLECTION.CLASS_NAME,
                                      KotlinConstants.REFLECTION.GETORCREATEKOTLINPACKAGE_METHOD_NAME,
                                      KotlinConstants.REFLECTION.GETORCREATEKOTLINPACKAGE_METHOD_DESC)
                        .areturn().__(),

                    ____
                        .ldc_(InstructionSequenceMatcher.X)
                        .ldc(name)
                        .invokestatic(KotlinConstants.REFLECTION.CLASS_NAME,
                                      KotlinConstants.REFLECTION.GETORCREATEKOTLINPACKAGE_METHOD_NAME,
                                      KotlinConstants.REFLECTION.GETORCREATEKOTLINPACKAGE_METHOD_DESC)
                        .areturn().__(),
                },
            };

            CONSTANTS = ____.constants();
        }

        @Override
        public Instruction[][][] getSequences()
        {
            return SEQUENCES;
        }

        @Override
        public Constant[] getConstants()
        {
            return CONSTANTS;
        }
    }
}
