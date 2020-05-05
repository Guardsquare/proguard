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
import proguard.classfile.kotlin.*;
import proguard.classfile.util.ClassUtil;
import proguard.resources.kotlinmodule.KotlinModule;
import proguard.resources.kotlinmodule.visitor.KotlinModuleVisitor;
import proguard.util.kotlin.asserter.*;

import java.util.List;

/**
 * @author James Hamilton
 */
public class KotlinModuleIntegrity
implements   KotlinAsserterConstraint,
             KotlinModuleVisitor
{
    private Reporter reporter;

    @Override
    public void check(Reporter reporter, Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void check(Reporter reporter, KotlinModule kotlinModule)
    {
        this.reporter = reporter;
        kotlinModule.accept(this);
    }


    @Override
    public void visitKotlinModule(KotlinModule kotlinModule)
    {
        AssertUtil util = new AssertUtil("Kotlin module", reporter);

        util.reportIfNull("Module name", kotlinModule.name);

        if (!kotlinModule.fileName.startsWith("META-INF/"))
        {
            reporter.report("Module should be in the META-INF folder");
        }

        if (!kotlinModule.fileName.endsWith(".kotlin_module"))
        {
            reporter.report("Module file name extension should be .kotlin_module");
        }

        if (!kotlinModule.fileName.equals("META-INF/" + kotlinModule.name + ".kotlin_module"))
        {
            reporter.report("Module name does not match filename: \"" + kotlinModule.fileName + "\" != \"META-INF/" + kotlinModule.name + ".kotlin_module\"");
        }

        kotlinModule.modulePackagesAccept((_kotlinModule, kotlinModulePart) -> {
            util.setParentElement("Kotlin module part '" + ClassUtil.externalClassName(kotlinModulePart.fqName) + "'");

            util.reportIfNull("fqName", kotlinModulePart.fqName);

            if (kotlinModulePart.fileFacadeNames.size() != kotlinModulePart.referencedFileFacades.size())
            {
                reporter.report("Mismatch between file facade names and references: " + kotlinModulePart.fileFacadeNames.size() + " != " + kotlinModulePart.referencedFileFacades.size());
            }

            if (kotlinModulePart.multiFileClassParts.size() != kotlinModulePart.referencedMultiFileParts.size())
            {
                reporter.report("Mismatch between multi-file class parts and references: " + kotlinModulePart.multiFileClassParts.size() + " != " + kotlinModulePart.referencedMultiFileParts.size());
            }

            List<KotlinFileFacadeKindMetadata> referencedFileFacades = kotlinModulePart.referencedFileFacades;
            for (int i = 0; i < referencedFileFacades.size(); i++)
            {
                KotlinFileFacadeKindMetadata ff = referencedFileFacades.get(i);
                String                       ffName;
                try
                {
                    ffName = ClassUtil.externalClassName(kotlinModulePart.fileFacadeNames.get(i));
                }
                catch (IndexOutOfBoundsException ignored)
                {
                    ffName = "unknown file facade";
                }

                util.reportIfNull("referenced file facade for '" + ffName + "'", ff);

                if (ff != null)
                {
                    util.reportIfNull("referenced file facade owner reference for '" + ffName + "'", ff.ownerReferencedClass);
                }
            }

            kotlinModulePart.referencedMultiFileParts
                .forEach((mfpName, mfp) -> {
                    util.reportIfNull("referenced multi-file part for '" + mfpName + "'", mfp);
                    if (mfp != null)
                    {
                        util.reportIfNull("referenced multi-file part for '" + mfpName + "'", mfp.ownerReferencedClass);
                    }
                });
        });
    }
}
