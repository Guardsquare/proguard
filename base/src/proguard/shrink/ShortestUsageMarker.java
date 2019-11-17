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
package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;
import proguard.util.VisitorAccepter;

/**
 * This SimpleUsageMarker keeps track of the shortest dependency chains.
 *
 * @author Johan Leys
 */
public class ShortestUsageMarker
extends      SimpleUsageMarker
{
    // A field acting as a parameter to the visitor methods.
    public ShortestUsageMark currentUsageMark;


    // A utility object to check for direct or indirect references.
    private final MyReferenceChecker referenceChecker = new MyReferenceChecker();


    public void setCurrentUsageMark(ShortestUsageMark currentUsageMark)
    {
        this.currentUsageMark = currentUsageMark;
    }


    // Implementations for SimpleUsageMarker.

    public void markAsUsed(VisitorAccepter visitorAccepter)
    {
        Object visitorInfo = visitorAccepter.getVisitorInfo();

        ShortestUsageMark shortestUsageMark =
            visitorInfo instanceof ShortestUsageMark      &&
            !((ShortestUsageMark)visitorInfo).isCertain() &&
            !currentUsageMark.isShorter((ShortestUsageMark)visitorInfo) ?
                new ShortestUsageMark((ShortestUsageMark)visitorInfo, true):
                currentUsageMark;

        visitorAccepter.setVisitorInfo(shortestUsageMark);
    }


    public boolean isUsed(VisitorAccepter visitorAccepter)
    {
        Object visitorInfo = visitorAccepter.getVisitorInfo();

        return visitorInfo != null                      &&
               visitorInfo instanceof ShortestUsageMark &&
               ((ShortestUsageMark)visitorInfo).isCertain();
    }


    public boolean shouldBeMarkedAsUsed(ProgramClass programClass)
    {
        Object visitorInfo = programClass.getVisitorInfo();

        return visitorInfo == null                           ||
               !(visitorInfo instanceof ShortestUsageMark)   ||
               !((ShortestUsageMark)visitorInfo).isCertain() ||
               currentUsageMark.isShorter((ShortestUsageMark)visitorInfo) &&
               !referencesClassMember(currentUsageMark, programClass);
    }


    public boolean shouldBeMarkedAsUsed(ProgramClass  programClass,
                                        ProgramMember programMember)
    {
        Object visitorInfo = programMember.getVisitorInfo();

        return visitorInfo == null                           ||
               !(visitorInfo instanceof ShortestUsageMark)   ||
               !((ShortestUsageMark)visitorInfo).isCertain() ||
               currentUsageMark.isShorter((ShortestUsageMark)visitorInfo) &&
               !referencesClass(currentUsageMark, programClass);
    }


    public boolean shouldBeMarkedAsUsed(VisitorAccepter visitorAccepter)
    {
        Object visitorInfo = visitorAccepter.getVisitorInfo();

        return visitorInfo == null                           ||
               !(visitorInfo instanceof ShortestUsageMark)   ||
               !((ShortestUsageMark)visitorInfo).isCertain() ||
               currentUsageMark.isShorter((ShortestUsageMark)visitorInfo);
    }


    public void markAsPossiblyUsed(VisitorAccepter visitorAccepter)
    {
        visitorAccepter.setVisitorInfo(new ShortestUsageMark(currentUsageMark, false));
    }


    public boolean shouldBeMarkedAsPossiblyUsed(ProgramClass  programClass,
                                                ProgramMember programMember)
    {
        Object visitorInfo = programMember.getVisitorInfo();

        return visitorInfo == null                         ||
               !(visitorInfo instanceof ShortestUsageMark) ||
               currentUsageMark.isShorter((ShortestUsageMark)visitorInfo) &&
               // Do not overwrite a certain mark with a shorter potential mark.
               !((ShortestUsageMark)visitorInfo).isCertain()              &&
               !referencesClass(currentUsageMark, programClass);
    }


    public boolean shouldBeMarkedAsPossiblyUsed(VisitorAccepter visitorAccepter)
    {
        Object visitorInfo = visitorAccepter.getVisitorInfo();

        return visitorInfo == null                         ||
               !(visitorInfo instanceof ShortestUsageMark) ||
               currentUsageMark.isShorter((ShortestUsageMark)visitorInfo) &&
               // Do not overwrite a certain mark with a shorter potential mark.
               !((ShortestUsageMark)visitorInfo).isCertain();
    }


    public boolean isPossiblyUsed(VisitorAccepter visitorAccepter)
    {
        Object visitorInfo = visitorAccepter.getVisitorInfo();

        return visitorInfo != null                      &&
               visitorInfo instanceof ShortestUsageMark &&
               !((ShortestUsageMark)visitorInfo).isCertain();
    }


    protected ShortestUsageMark getShortestUsageMark(VisitorAccepter visitorAccepter)
    {
        Object visitorInfo = visitorAccepter.getVisitorInfo();

        return (ShortestUsageMark)visitorInfo;
    }


    // Small utility methods.

    /**
     * Returns whether the given usage mark references the given class,
     * directly or indirectly.
     */
    private boolean referencesClass(ShortestUsageMark shortestUsageMark,
                                    Clazz             clazz)
    {
        return referenceChecker.referencesClass(shortestUsageMark, clazz);
    }


    /**
     * Returns whether the given usage mark references a member of the given
     * class, directly or indirectly.
     */
    private boolean referencesClassMember(ShortestUsageMark shortestUsageMark,
                                          Clazz             clazz)
    {
        return referenceChecker.referencesClassMember(shortestUsageMark, clazz);
    }


    /**
     * This class checks whether a given usage mark is caused by a given
     * class or a member of a given class, directly or indirectly.
     */
    private class MyReferenceChecker
    extends       SimplifiedVisitor
    implements    ClassVisitor,
                  MemberVisitor
    {
        private Clazz   checkClass;
        private boolean checkMember;
        private boolean isReferencing;


        public boolean referencesClass(ShortestUsageMark shortestUsageMark,
                                       Clazz             clazz)
        {
            checkClass    = clazz;
            checkMember   = false;
            isReferencing = false;

            checkReferenceFrom(shortestUsageMark);

            return isReferencing;
        }


        public boolean referencesClassMember(ShortestUsageMark shortestUsageMark,
                                             Clazz             clazz)
        {
            checkClass    = clazz;
            checkMember   = true;
            isReferencing = false;

            checkReferenceFrom(shortestUsageMark);

            return isReferencing;
        }


        // Implementations for ClassVisitor.

        public void visitLibraryClass(LibraryClass libraryClass) {}


        public void visitProgramClass(ProgramClass programClass)
        {
            checkReferenceFrom(programClass);
        }


        // Implementations for MemberVisitor.

        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}


        public void visitProgramField(ProgramClass programClass, ProgramField programField)
        {
            checkReferenceFrom(programField);
        }


        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
        {
            checkReferenceFrom(programMethod);
        }


       // Small utility members.

        private void checkReferenceFrom(VisitorAccepter visitorAccepter)
        {
            // Check the causing class or member, if still necessary.
            if (!isReferencing)
            {
                checkReferenceFrom(getShortestUsageMark(visitorAccepter));
            }
        }


        private void checkReferenceFrom(ShortestUsageMark shortestUsageMark)
        {
            // Check whether the class is marked because of a member of the
            // class, or the class member is marked because of the class.
            isReferencing = checkMember ?
                shortestUsageMark.isCausedByMember(checkClass) :
                shortestUsageMark.isCausedBy(checkClass);

            shortestUsageMark.acceptClassVisitor(this);
            shortestUsageMark.acceptMemberVisitor(this);
        }
    }
}
