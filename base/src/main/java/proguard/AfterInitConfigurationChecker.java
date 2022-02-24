/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard;

import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassVisitor;
import proguard.pass.Pass;

/**
 * This pass performs configuration checks for which class pools or resource information
 * should already have been initialized.
 */
public class AfterInitConfigurationChecker implements Pass
{
    private final Configuration configuration;

    public AfterInitConfigurationChecker(Configuration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void execute(AppView appView)
    {
        if (configuration.targetClassVersion != 0)
        {
            // Fail if -target is set and program class pool contains a class with class version > 11.
            appView.programClassPool.classesAccept(new BackportMaxVersionVisitor(VersionConstants.CLASS_VERSION_11,
                                                                                 configuration.targetClassVersion));
        }
    }

    private static class BackportMaxVersionVisitor implements ClassVisitor {

        private final int maxClassFileVersion;

        private final int target;

        private BackportMaxVersionVisitor(int maxClassFileVersion, int target)
        {
            this.maxClassFileVersion = maxClassFileVersion;
            this.target = target;
        }

        // Implementations of ClassVisitor.

        @Override
        public void visitProgramClass(ProgramClass programClass)
        {
            if (programClass.u4version > maxClassFileVersion)
            {
                throw new RuntimeException("-target can only be used with class file versions <= " + ClassUtil.internalMajorClassVersion(maxClassFileVersion) +
                                           " (Java " + ClassUtil.externalClassVersion(maxClassFileVersion) + ")." + System.lineSeparator() +
                                           "The input classes contain version " + ClassUtil.internalMajorClassVersion(programClass.u4version)   +
                                           " class files which cannot be backported to target version (" + ClassUtil.internalMajorClassVersion(target) + ").");
            }
        }

        @Override
        public void visitAnyClass(Clazz clazz)
        {
        }
    }
}
