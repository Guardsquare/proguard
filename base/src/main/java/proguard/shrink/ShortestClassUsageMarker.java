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
package proguard.shrink;

import proguard.classfile.*;
import proguard.util.Processable;

/**
 * This UsageMarker constructs the shortest chain of dependencies.
 *
 * @author Eric Lafortune
 * @see ClassShrinker
 * @see ShortestUsagePrinter
 */
public class ShortestClassUsageMarker
extends      ClassUsageMarker
{
    /**
     * Creates a new ShortestUsageMarker with the given initial reason.
     */
    public ShortestClassUsageMarker(ShortestUsageMarker usageMarker,
                                    String              reason)
    {
        super(usageMarker);
        setCurrentUsageMark(new ShortestUsageMark(reason));
    }


    // Overriding implementations for ClassUsageMarker.

    public ShortestUsageMarker getUsageMarker()
    {
        return (ShortestUsageMarker)super.getUsageMarker();
    }


    protected void markProgramClassBody(ProgramClass programClass)
    {
        ShortestUsageMark previousUsageMark = getCurrentUsageMark();

        setCurrentUsageMark(new ShortestUsageMark(getShortestUsageMark(programClass),
                                                  "is extended by   ",
                                                  10000,
                                                  programClass));

        super.markProgramClassBody(programClass);

        setCurrentUsageMark(previousUsageMark);
    }


    protected void markProgramFieldBody(ProgramClass programClass, ProgramField programField)
    {
        ShortestUsageMark previousUsageMark = getCurrentUsageMark();

        setCurrentUsageMark(new ShortestUsageMark(getShortestUsageMark(programField),
                                                  "is referenced by ",
                                                  1,
                                                  programClass,
                                                  programField));

        super.markProgramFieldBody(programClass, programField);

        setCurrentUsageMark(previousUsageMark);
    }


    protected void markProgramMethodBody(ProgramClass programClass, ProgramMethod programMethod)
    {
        ShortestUsageMark previousUsageMark = getCurrentUsageMark();

        setCurrentUsageMark(new ShortestUsageMark(getShortestUsageMark(programMethod),
                                                  "is invoked by    ",
                                                  1,
                                                  programClass,
                                                  programMethod));

        super.markProgramMethodBody(programClass, programMethod);

        setCurrentUsageMark(previousUsageMark);
    }


    protected void markMethodHierarchy(Clazz clazz, Method method)
    {
        ShortestUsageMark previousUsageMark = getCurrentUsageMark();

        setCurrentUsageMark(new ShortestUsageMark(getShortestUsageMark(method),
                                                  "implements       ",
                                                  100,
                                                  clazz,
                                                  method));

        super.markMethodHierarchy(clazz, method);

        setCurrentUsageMark(previousUsageMark);
    }


    public boolean shouldBeMarkedAsUsed(ProgramClass programClass)
    {
        return getUsageMarker().shouldBeMarkedAsUsed(programClass);
    }


    public boolean shouldBeMarkedAsUsed(ProgramClass programClass,
                                        ProgramMember programMember)
    {
        return getUsageMarker().shouldBeMarkedAsUsed(programClass, programMember);
    }


    public boolean shouldBeMarkedAsUsed(Processable processable)
    {
        return getUsageMarker().shouldBeMarkedAsUsed(processable);
    }


    public boolean isUsed(Processable processable)
    {
        return getUsageMarker().isUsed(processable);
    }


    public void markAsPossiblyUsed(Processable processable)
    {
        getUsageMarker().markAsPossiblyUsed(processable);
    }


    public boolean shouldBeMarkedAsPossiblyUsed(ProgramClass programClass,
                                                ProgramMember programMember)
    {
        return getUsageMarker().shouldBeMarkedAsPossiblyUsed(programClass, programMember);
    }


    public boolean shouldBeMarkedAsPossiblyUsed(Processable processable)
    {
        return getUsageMarker().shouldBeMarkedAsPossiblyUsed(processable);
    }


    public boolean isPossiblyUsed(Processable processable)
    {
        return getUsageMarker().isPossiblyUsed(processable);
    }


    protected ShortestUsageMark getShortestUsageMark(Processable processable)
    {
        return getUsageMarker().getShortestUsageMark(processable);
    }


    // Small utility methods.

    private ShortestUsageMark getCurrentUsageMark()
    {
        return getUsageMarker().currentUsageMark;
    }


    private void setCurrentUsageMark(ShortestUsageMark shortestUsageMark)
    {
        getUsageMarker().setCurrentUsageMark(shortestUsageMark);
    }
}
