/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */

package proguard.obfuscate.kotlin;

import proguard.obfuscate.NameFactory;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.*;
import proguard.resources.kotlinmodule.KotlinModule;

/**
 * Obfuscate module names using the given {@link NameFactory}.
 *
 * @author James Hamilton
 */
public class KotlinModuleNameObfuscator
implements   ResourceFileVisitor
{
    private final NameFactory nameFactory;

    public KotlinModuleNameObfuscator(NameFactory nameFactory)
    {
        this.nameFactory = nameFactory;
        this.nameFactory.reset();
    }


    // Implementations for ResourceFileVisitor.

    @Override
    public void visitKotlinModule(KotlinModule kotlinModule)
    {
        kotlinModule.name = nameFactory.nextName();
    }
}
