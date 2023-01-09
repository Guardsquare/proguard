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


package proguard.util.kotlin;

import proguard.*;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.classfile.util.kotlin.KotlinMetadataInitializer;
import proguard.classfile.visitor.ClassVisitor;
import proguard.pass.Pass;

import static proguard.classfile.util.kotlin.KotlinMetadataInitializer.MAX_SUPPORTED_VERSION;
import static proguard.classfile.util.kotlin.KotlinMetadataInitializer.isSupportedMetadataVersion;

/**
 * This pass checks the attached a class' attached Kotlin metadata for unsupported metadata.
 * Unsupported metadata may be attached to a class if {@link KotlinMetadataInitializer} could
 * not initialize the metadata model.
 * <p>
 * An exception is thrown if the cause is that the {@link KotlinMetadataInitializer} does not
 * support the specific version; otherwise, the metadata is simply removed.
 *
 * @author James Hamilton
 */
public class KotlinUnsupportedVersionChecker implements Pass
{

    @Override
    public void execute(AppView appView) throws Exception
    {
        ClassVisitor unsupportedMetadataChecker = new ReferencedKotlinMetadataVisitor(
            new MyUnsupportedKotlinMetadataChecker()
        );

        appView.programClassPool.classesAccept(unsupportedMetadataChecker);
        appView.libraryClassPool.classesAccept(unsupportedMetadataChecker);
    }

    private static class MyUnsupportedKotlinMetadataChecker implements KotlinMetadataVisitor
    {
        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}


        @Override
        public void visitUnsupportedKotlinMetadata(Clazz clazz, UnsupportedKotlinMetadata kotlinMetadata)
        {
            if (kotlinMetadata.mv == null || kotlinMetadata.mv.length < 3 ||
                !isSupportedMetadataVersion(new KotlinMetadataVersion(kotlinMetadata.mv)))
            {
                throw new RuntimeException(
                    "Unsupported Kotlin metadata version found on class '" + clazz.getName() + "'." +
                    System.lineSeparator() +
                    "Kotlin versions up to " + MAX_SUPPORTED_VERSION.major + "." + MAX_SUPPORTED_VERSION.minor + " are supported.");
            }
            else
            {
                // Unsupported for some other reason, just remove the metadata.
                clazz.accept(new KotlinMetadataRemover());
            }
        }
    }
}
