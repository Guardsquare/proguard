/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.optimize;

import proguard.AppView;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.editor.LineNumberTableAttributeTrimmer;
import proguard.pass.Pass;

/**
 * Trims the line number table attributes of all program classes.
 *
 * @author Tim Van Den Broecke
 */
public class LineNumberTrimmer
implements Pass
{
    @Override
    public void execute(AppView appView)
    {
        appView.programClassPool.classesAccept(new AllAttributeVisitor(true,
                                                                       new LineNumberTableAttributeTrimmer()));
    }
}
