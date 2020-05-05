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
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.reflect.*;
import proguard.classfile.kotlin.reflect.visitor.CallableReferenceInfoVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.util.kotlin.asserter.AssertUtil;

import static proguard.classfile.kotlin.KotlinConstants.*;

public class SyntheticClassIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinMetadataVisitor

{

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        AssertUtil util = new AssertUtil("Synthetic class", reporter);

        switch (kotlinSyntheticClassKindMetadata.flavor)
        {
            case DEFAULT_IMPLS:
                if (!clazz.getName().endsWith(DEFAULT_IMPLEMENTATIONS_SUFFIX))
                {
                    reporter.report("Default implementations class name does not end with " + DEFAULT_IMPLEMENTATIONS_SUFFIX);
                }
                break;
            case WHEN_MAPPINGS:
                if (!clazz.getName().endsWith(WHEN_MAPPINGS_SUFFIX))
                {
                    reporter.report("When mappings class name does not end with " + WHEN_MAPPINGS_SUFFIX);
                }
                break;
            case LAMBDA:
                try {
                    Integer.parseInt(clazz.getName().substring(clazz.getName().lastIndexOf("$") + 1));
                }
                catch (NumberFormatException e)
                {
                    reporter.report( "Lambda inner classname is not an integer.");
                }

                if (kotlinSyntheticClassKindMetadata.functions.isEmpty())
                {
                    reporter.report("Lambda class has no functions");
                }
                else if (kotlinSyntheticClassKindMetadata.functions.size() > 1)
                {
                    reporter.report("Lambda class has multiple functions");
                }
                break;
            case REGULAR:
        }

        if (clazz.extendsOrImplements(REFLECTION.CALLABLE_REFERENCE_CLASS_NAME))
        {
            util.setParentElement("Synthetic callable reference class");
            util.reportIfNullReference("callable reference info", kotlinSyntheticClassKindMetadata.callableReferenceInfo);

            kotlinSyntheticClassKindMetadata.callableReferenceInfoAccept(new CallableReferenceInfoVisitor()
            {
                @Override
                public void visitAnyCallableReferenceInfo(CallableReferenceInfo callableReferenceInfo)
                {
                    util.setParentElement("Synthetic callable reference (" + callableReferenceInfo.getClass().getSimpleName() + ")");
                    util.reportIfNull("name", callableReferenceInfo.getName());
                    util.reportIfNull("signature", callableReferenceInfo.getSignature());
                }

                @Override
                public void visitFunctionReferenceInfo(FunctionReferenceInfo functionReferenceInfo)
                {
                    visitAnyCallableReferenceInfo(functionReferenceInfo);
                    checkOwner(functionReferenceInfo);
                }

                @Override
                public void visitPropertyReferenceInfo(PropertyReferenceInfo propertyReferenceInfo)
                {
                    visitAnyCallableReferenceInfo(propertyReferenceInfo);
                    checkOwner(propertyReferenceInfo);
                }

                @Override
                public void visitLocalVariableReferenceInfo(LocalVariableReferenceInfo localVariableReferenceInfo)
                {
                    visitAnyCallableReferenceInfo(localVariableReferenceInfo);
                    checkOwner(localVariableReferenceInfo);
                }

                private void checkOwner(CallableReferenceInfo callableReferenceInfo)
                {
                    // We don't check this for JavaReferenceInfo.

                    util.reportIfNull("owner", callableReferenceInfo.getOwner());
                    if (callableReferenceInfo.getOwner() != null)
                    {
                        // We need the module to update the getOwner() method for file facades and multi-file class parts.
                        if (callableReferenceInfo.getOwner().k == METADATA_KIND_FILE_FACADE ||
                            callableReferenceInfo.getOwner().k == METADATA_KIND_MULTI_FILE_CLASS_PART)
                        {
                            util.reportIfNull("referenced module", callableReferenceInfo.getOwner().referencedModule);
                        }
                    }
                }
            });
        }
    }
}
