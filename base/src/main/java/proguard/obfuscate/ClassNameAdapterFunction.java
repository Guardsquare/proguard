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
package proguard.obfuscate;

import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;
import proguard.util.*;

import java.util.*;

/**
 * This StringFunction maps given names on obfuscated names, based on the
 * renamed classes in the given ClassPool. If no corresponding class is found,
 * it returns null.
 *
 * @author Johan Leys
 */
public class ClassNameAdapterFunction implements StringFunction
{
    private final ClassPool classPool;
    private final Map       packagePrefixMap;


    /**
     * Creates a new ClassNameAdapterFunction for the given class pool.
     */
    public ClassNameAdapterFunction(ClassPool classPool)
    {
        this.classPool        = classPool;
        this.packagePrefixMap = createPackagePrefixMap(classPool);
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String fileName)
    {
        // Try to find a corresponding class name by removing increasingly
        // long suffixes.
        for (int suffixIndex = fileName.length() - 1;
             suffixIndex > 0;
             suffixIndex--)
        {
            char c = fileName.charAt(suffixIndex);
            if (!Character.isLetterOrDigit(c))
            {
                // Chop off the suffix.
                String className = fileName.substring(0, suffixIndex);

                // Did we get to the package separator?
                if (c == TypeConstants.PACKAGE_SEPARATOR)
                {
                    break;
                }

                // Is there a class corresponding to the data entry?
                Clazz clazz = classPool.getClass(className);
                if (clazz != null)
                {
                    // Did the class get a new name?
                    String newClassName = clazz.getName();
                    if (!className.equals(newClassName))
                    {
                        return newClassName + fileName.substring(suffixIndex);
                    }
                    else
                    {
                        // Otherwise stop looking.
                        return fileName;
                    }
                }
            }
        }

        // Try to find a corresponding package name by increasingly removing
        // more subpackages.
        String packagePrefix = fileName;
        while (true)
        {
            // Chop off the class name or the last subpackage name.
            packagePrefix = ClassUtil.internalPackagePrefix(packagePrefix);

            // Stop when the package prefix is empty.
            // Don't match against the default package.
            if (packagePrefix.length() == 0)
            {
                break;
            }

            // Is there a package corresponding to the package prefix?
            String newPackagePrefix = (String)packagePrefixMap.get(packagePrefix);
            if (newPackagePrefix != null)
            {
                // Did the package get a new name?
                if (!packagePrefix.equals(newPackagePrefix))
                {
                    // Rename the resource.
                    return newPackagePrefix + fileName.substring(packagePrefix.length());
                }
                else
                {
                    // Otherwise stop looking.
                    return null;
                }
            }
        }

        return null;
    }


    /**
     * Creates a map of old package prefixes to new package prefixes, based on
     * the given class pool.
     */
    private static Map createPackagePrefixMap(ClassPool classPool)
    {
        Map packagePrefixMap = new HashMap();

        Iterator iterator = classPool.classNames();
        while (iterator.hasNext())
        {
            String className     = (String)iterator.next();
            String packagePrefix = ClassUtil.internalPackagePrefix(className);

            String mappedNewPackagePrefix = (String)packagePrefixMap.get(packagePrefix);
            if (mappedNewPackagePrefix == null ||
                !mappedNewPackagePrefix.equals(packagePrefix))
            {
                String newClassName     = classPool.getClass(className).getName();
                String newPackagePrefix = ClassUtil.internalPackagePrefix(newClassName);

                packagePrefixMap.put(packagePrefix, newPackagePrefix);
            }
        }

        return packagePrefixMap;
    }
}
