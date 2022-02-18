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

import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.resources.kotlinmodule.KotlinModule;
import proguard.util.kotlin.asserter.Reporter;

public abstract class AbstractKotlinMetadataConstraint
implements KotlinAsserterConstraint,
           KotlinMetadataVisitor
{
    protected Reporter  reporter;
    protected ClassPool programClassPool;
    protected ClassPool libraryClassPool;


    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void check(Reporter       reporter,
                      ClassPool      programClassPool,
                      ClassPool      libraryClassPool,
                      Clazz          clazz,
                      KotlinMetadata metadata)
    {
        this.reporter         = reporter;
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;


        try
        {
            metadata.accept(clazz, this);
        }
        catch (Exception e)
        {
            reporter.report( "Encountered unexpected Exception when checking constraint: " + e.getMessage());
        }
    }

    @Override
    public void check(Reporter reporter, KotlinModule kotlinModule)
    {

    }
}
