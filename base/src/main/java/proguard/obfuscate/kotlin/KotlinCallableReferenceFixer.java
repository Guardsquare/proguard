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

import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.reflect.visitor.CallableReferenceInfoToOwnerVisitor;
import proguard.classfile.kotlin.visitor.filter.KotlinDeclarationContainerFilter;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.util.InstructionSequenceMatcher;
import proguard.classfile.visitor.*;
import proguard.obfuscate.util.*;
import proguard.resources.kotlinmodule.visitor.KotlinMetadataToModuleVisitor;

/**
 * This class fixes the CallableReference implementations of function and property references.
 *
 *    public String getName();
 *    public String getSignature();
 *    public KDeclarationContainer getOwner();
 *
 * See https://github.com/JetBrains/kotlin/blob/4718ae418672a3e1927f29403fd4a8c916bc08ff/libraries/stdlib/jvm/runtime/kotlin/jvm/internal/CallableReference.java#L25
 *
 * @author James Hamilton
 */
public class KotlinCallableReferenceFixer
implements   KotlinMetadataVisitor
{
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;

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
