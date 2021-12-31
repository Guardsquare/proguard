/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.preverify;

import proguard.AppView;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.editor.NamedAttributeDeleter;
import proguard.classfile.visitor.*;
import proguard.pass.Pass;

/**
 * This pass clears any JSE preverification information from the program classes.
 */
public class PreverificationClearer implements Pass
{
    @Override
    public void execute(AppView appView)
    {
        appView.programClassPool.classesAccept(
                new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_6,
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new NamedAttributeDeleter(Attribute.STACK_MAP_TABLE)))));
    }
}
