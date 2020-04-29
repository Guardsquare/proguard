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
import proguard.classfile.visitor.*;
import proguard.util.Processable;

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

    public void markAsUsed(Processable processable)
    {
        Object processingInfo = processable.getProcessingInfo();

        ShortestUsageMark shortestUsageMark =
            processingInfo instanceof ShortestUsageMark      &&
            !((ShortestUsageMark)processingInfo).isCertain() &&
            !currentUsageMark.isShorter((ShortestUsageMark)processingInfo) ?
                new ShortestUsageMark((ShortestUsageMark)processingInfo, true):
                currentUsageMark;

        processable.setProcessingInfo(shortestUsageMark);
    }


    public boolean isUsed(Processable processable)
    {
        Object processingInfo = processable.getProcessingInfo();

        return processingInfo != null                      &&
               processingInfo instanceof ShortestUsageMark &&
               ((ShortestUsageMark)processingInfo).isCertain();
    }


    public boolean shouldBeMarkedAsUsed(ProgramClass programClass)
    {
        Object processingInfo = programClass.getProcessingInfo();

        return processingInfo == null                           ||
               !(processingInfo instanceof ShortestUsageMark)   ||
               !((ShortestUsageMark)processingInfo).isCertain() ||
               currentUsageMark.isShorter((ShortestUsageMark)processingInfo) &&
               !referencesClassMember(currentUsageMark, programClass);
    }


    public boolean shouldBeMarkedAsUsed(ProgramClass  programClass,
                                        ProgramMember programMember)
    {
        Object processingInfo = programMember.getProcessingInfo();

        return processingInfo == null                           ||
               !(processingInfo instanceof ShortestUsageMark)   ||
               !((ShortestUsageMark)processingInfo).isCertain() ||
               currentUsageMark.isShorter((ShortestUsageMark)processingInfo) &&
               !referencesClass(currentUsageMark, programClass);
    }


    public boolean shouldBeMarkedAsUsed(Processable processable)
    {
        Object processingInfo = processable.getProcessingInfo();

        return processingInfo == null                           ||
               !(processingInfo instanceof ShortestUsageMark)   ||
               !((ShortestUsageMark)processingInfo).isCertain() ||
               currentUsageMark.isShorter((ShortestUsageMark)processingInfo);
    }


    public void markAsPossiblyUsed(Processable processable)
    {
        processable.setProcessingInfo(new ShortestUsageMark(currentUsageMark, false));
    }


    public boolean shouldBeMarkedAsPossiblyUsed(ProgramClass  programClass,
                                                ProgramMember programMember)
    {
        Object processingInfo = programMember.getProcessingInfo();

        return processingInfo == null                         ||
               !(processingInfo instanceof ShortestUsageMark) ||
               currentUsageMark.isShorter((ShortestUsageMark)processingInfo) &&
               // Do not overwrite a certain mark with a shorter potential mark.
               !((ShortestUsageMark)processingInfo).isCertain()              &&
               !referencesClass(currentUsageMark, programClass);
    }


    public boolean shouldBeMarkedAsPossiblyUsed(Processable processable)
    {
        Object processingInfo = processable.getProcessingInfo();

        return processingInfo == null                         ||
               !(processingInfo instanceof ShortestUsageMark) ||
               currentUsageMark.isShorter((ShortestUsageMark)processingInfo) &&
               // Do not overwrite a certain mark with a shorter potential mark.
               !((ShortestUsageMark)processingInfo).isCertain();
    }


    public boolean isPossiblyUsed(Processable processable)
    {
        Object processingInfo = processable.getProcessingInfo();

        return processingInfo != null                      &&
               processingInfo instanceof ShortestUsageMark &&
               !((ShortestUsageMark)processingInfo).isCertain();
    }


    protected ShortestUsageMark getShortestUsageMark(Processable processable)
    {
        Object processingInfo = processable.getProcessingInfo();

        return (ShortestUsageMark)processingInfo;
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

        private void checkReferenceFrom(Processable processable)
        {
            // Check the causing class or member, if still necessary.
            if (!isReferencing)
            {
                checkReferenceFrom(getShortestUsageMark(processable));
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
