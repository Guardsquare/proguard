/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.obfuscate;

import proguard.classfile.Clazz;
import proguard.classfile.util.ClassUtil;
import proguard.resources.file.*;
import proguard.resources.file.visitor.ResourceFileVisitor;

import java.util.Set;

/**
 * This {@link ResourceFileVisitor} fixes the class names of referenced Java
 * classes whose name have changed.
 *
 * @author Lars Vandenbergh
 */
public class ResourceJavaReferenceFixer implements ResourceFileVisitor
{
    // Implementations for ResourceFileVisitor.

    @Override
    public void visitResourceFile(ResourceFile resourceFile)
    {
        Set<ResourceJavaReference> references = resourceFile.references;

        if (references != null)
        {
            for (ResourceJavaReference reference : references)
            {
                Clazz referencedClass = reference.referencedClass;

                if (referencedClass != null)
                {
                    reference.externalClassName =
                        ClassUtil.externalClassName(referencedClass.getName());
                }
            }
        }
    }
}
