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
package proguard.optimize.kotlin.visitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * This {@link ClassVisitor} groups the visited classes per package,
 * after which the classes can be visited per package.
 * @author Joren Van Hecke
 * @see proguard.optimize.kotlin.KotlinLambdaMerger
 */
public class PackageGrouper implements ClassVisitor {

    private final Map<String, ClassPool> packageClassPools = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(PackageGrouper.class);

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        String classPackageName = ClassUtil.internalPackageName(clazz.getName());
        if (!packageClassPools.containsKey(classPackageName))
        {
            logger.info("New package found: {}",
                        ClassUtil.externalPackageName(ClassUtil.externalClassName(clazz.getName())));
            packageClassPools.put(classPackageName, new ClassPool());
        }
        packageClassPools.get(classPackageName).addClass(clazz);
    }

    public int size()
    {
        return packageClassPools.size();
    }

    public boolean containsPackage(String packageName)
    {
        return packageClassPools.containsKey(packageName);
    }

    public Iterable<String> packageNames()
    {
        return packageClassPools.keySet();
    }

    public void packagesAccept(ClassPoolVisitor classPoolVisitor)
    {
        for (ClassPool packageClassPool : packageClassPools.values())
        {
            classPoolVisitor.visitClassPool(packageClassPool);
        }
    }

    public void packageAccept(String packageName, ClassPoolVisitor classPoolVisitor)
    {
        ClassPool packageClassPool = this.packageClassPools.get(packageName);
        if (packageClassPool != null)
        {
            classPoolVisitor.visitClassPool(packageClassPool);
        }
    }
}
