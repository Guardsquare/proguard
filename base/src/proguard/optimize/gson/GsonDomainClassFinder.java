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
package proguard.optimize.gson;

import proguard.classfile.*;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.util.ProcessingFlags;

import java.util.Arrays;

/**
 * This class visitor determines whether a given domain class can be optimized
 * by the GSON optimizations and traverses both the class and field hierarchy
 * to look for further domain classes.
 *
 * @author Lars Vandenbergh
 */
public class GsonDomainClassFinder
implements   ClassVisitor
{
    //*
    public static final boolean DEBUG = false;
    /*/
    public static       boolean DEBUG = System.getProperty("gdcf") != null;
    //*/


    private final GsonRuntimeSettings           gsonRuntimeSettings;
    private final ClassPool                     gsonDomainClassPool;
    private final WarningPrinter                warningPrinter;

    private final ClassPool                     unoptimizedClassPool         = new ClassPool();
    private final LocalOrAnonymousClassChecker  localOrAnonymousClassChecker = new LocalOrAnonymousClassChecker();
    private final TypeParameterClassChecker     typeParameterClassChecker    = new TypeParameterClassChecker();
    private final DuplicateJsonFieldNameChecker duplicateFieldNameChecker    = new DuplicateJsonFieldNameChecker();


    /**
     * Creates a new GsonDomainClassFinder.
     *
     * @param gsonRuntimeSettings keeps track of all GsonBuilder invocations.
     * @param gsonDomainClassPool the class pool to which the found domain
     *                            classes are added.
     * @param warningPrinter      used to print notes about domain classes that
     *                            can not be handled by the Gson optimization.
     */
    public GsonDomainClassFinder(GsonRuntimeSettings gsonRuntimeSettings,
                                 ClassPool           gsonDomainClassPool,
                                 WarningPrinter      warningPrinter)
    {
        this.gsonRuntimeSettings = gsonRuntimeSettings;
        this.gsonDomainClassPool = gsonDomainClassPool;
        this.warningPrinter      = warningPrinter;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // For classes that are Gson "seeds" (they immediately occur in Gson
        // invocations or are a field of another Gson domain class), we also
        // want to visit the super and sub classes in the class hierarchy.
        handleDomainClass(programClass, new HierarchyClassVisitor());
    }

    // Utility methods.

    private void handleDomainClass(ProgramClass programClass, ClassVisitor hierarchyClassVisitor)
    {
        if (gsonDomainClassPool.getClass(programClass.getName())  == null &&
            unoptimizedClassPool.getClass(programClass.getName()) == null)
        {
            // Local or anonymous classes are excluded by GSON.
            programClass.accept(localOrAnonymousClassChecker);
            if (localOrAnonymousClassChecker.isLocalOrAnonymous())
            {
                // No need to note here because this is not handled
                // by GSON either.
                return;
            }

            if(librarySuperClassCount(programClass) != 0)
            {
                note(programClass,
                     "Warning: " + ClassUtil.externalClassName(programClass.getName() +
                     " can not be optimized for GSON because" +
                     " it is or inherits from a library class."));
                return;
            }

            if(gsonSuperClassCount(programClass) != 0)
            {
                note(programClass,
                     "Warning: " + ClassUtil.externalClassName(programClass.getName() +
                     " can not be optimized for GSON because" +
                     " it is or inherits from a GSON API class."));
                return;
            }

            // Classes with fields that have generic type parameters are
            // not supported by our optimization as it is rather complex
            // to derive all possible type arguments and generate methods
            // for each case.
            typeParameterClassChecker.hasFieldWithTypeParameter = false;
            programClass.hierarchyAccept(true,
                                         true,
                                         false,
                                         false,
                                         typeParameterClassChecker);
            if (typeParameterClassChecker.hasFieldWithTypeParameter)
            {
                note(programClass,
                     "Warning: " + ClassUtil.externalClassName(programClass.getName() +
                     " can not be optimized for GSON because" +
                     " it uses generic type variables."));
                return;
            }

            // Class with duplicate field names are not supported by
            // GSON either.
            duplicateFieldNameChecker.hasDuplicateJsonFieldNames = false;
            programClass.hierarchyAccept(true,
                                         true,
                                         false,
                                         false,
                                         duplicateFieldNameChecker);
            if (duplicateFieldNameChecker.hasDuplicateJsonFieldNames)
            {
                note(programClass,
                     "Warning: " + ClassUtil.externalClassName(programClass.getName() +
                     " can not be optimized for GSON because" +
                     " it contains duplicate field names in its JSON representation."));
                return;
            }

            // Classes for which type adapters were registered are not optimized.
            ClassCounter typeAdapterClassCounter = new ClassCounter();
            programClass.hierarchyAccept(true,
                                         true,
                                         false,
                                         false,
                                         new ClassPresenceFilter(
                                             gsonRuntimeSettings.typeAdapterClassPool,
                                             typeAdapterClassCounter,
                                             null));
            if (typeAdapterClassCounter.getCount() > 0)
            {
                note(programClass,
                     "Warning: " + ClassUtil.externalClassName(programClass.getName() +
                     " can not be optimized for GSON because" +
                     " a custom type adapter is registered for it."));
                return;
            }

            // Classes that contain any JsonAdapter annotations are not optimized.
            AnnotationFinder annotationFinder = new AnnotationFinder();
            programClass.hierarchyAccept(true,
                                         true,
                                         false,
                                         false,
                                         new MultiClassVisitor(
                                             new AllAttributeVisitor(true,
                                             new AllAnnotationVisitor(
                                             new AnnotationTypeFilter(GsonClassConstants.ANNOTATION_TYPE_JSON_ADAPTER,
                                             annotationFinder)))));
            if (annotationFinder.found)
            {
                note(programClass,
                     "Warning: " + ClassUtil.externalClassName(programClass.getName() +
                     " can not be optimized for GSON because" +
                     " it contains a JsonAdapter annotation."));
                return;
            }

            if ((programClass.getAccessFlags() & AccessConstants.INTERFACE) == 0)
            {
                if (DEBUG)
                {
                    System.out.println("GsonDomainClassFinder: adding domain class " +
                                       programClass.getName());
                }

                // Add type occurring in toJson() invocation to domain class pool.
                gsonDomainClassPool.addClass(programClass);

                // Recursively visit the fields of the domain class and consider
                // their classes as domain classes too.
                int requiredUnsetAccessFlags = AccessConstants.SYNTHETIC;
                if (!gsonRuntimeSettings.excludeFieldsWithModifiers)
                {
                    // If fields are not excluded based on modifiers, we assume
                    // the default behavior of Gson, which is to exclude
                    // transient and static fields.
                    requiredUnsetAccessFlags |= AccessConstants.TRANSIENT |
                                                AccessConstants.STATIC;
                }
                programClass.fieldsAccept(
                    new MemberAccessFilter(0, requiredUnsetAccessFlags,
                    new MultiMemberVisitor(
                        new MemberDescriptorReferencedClassVisitor(this),
                        new AllAttributeVisitor(
                        new SignatureAttributeReferencedClassVisitor(this)))));
            }

            // Consider super and sub classes as domain classes too, except for
            // sub classes of enum types that are generated by the compiler
            // but don't contain any additional fields serialized by Gson.
            if (hierarchyClassVisitor != null &&
                (programClass.getAccessFlags() & AccessConstants.ENUM) == 0)
            {
                programClass.hierarchyAccept(false,
                                             true,
                                             false,
                                             true,
                                             hierarchyClassVisitor);
            }
        }
    }


    private int librarySuperClassCount(ProgramClass programClass)
    {
        ClassCounter nonObjectLibrarySuperClassCounter = new ClassCounter();
        programClass.hierarchyAccept(true,
                                     true,
                                     false,
                                     false,
                                     new LibraryClassFilter(
                                     new ClassNameFilter(Arrays.asList("!java/lang/Object", "!java/lang/Enum"),
                                     nonObjectLibrarySuperClassCounter)));
        return nonObjectLibrarySuperClassCounter.getCount();
    }

    private int gsonSuperClassCount(ProgramClass programClass)
    {
        ClassCounter gsonSuperClassCounter = new ClassCounter();
        programClass.hierarchyAccept(true,
                                     true,
                                     false,
                                     false,
                                     new ProgramClassFilter(
                                     new ClassNameFilter("com/google/gson/**",
                                     gsonSuperClassCounter)));
        return gsonSuperClassCounter.getCount();
    }


    private void note(ProgramClass programClass, String note)
    {
        if (warningPrinter != null && !isKept(programClass))
        {
            warningPrinter.print(programClass.getName(), note);
            warningPrinter.print(programClass.getName(),
                                 "      You should consider including dexguard-gson.pro " +
                                 "or keeping this class and its members in your configuration " +
                                 "as follows:");
            warningPrinter.print(programClass.getName(),
                                 "      -keep class " +
                                 ClassUtil.externalClassName(programClass.getName()) + " { *; }");
        }

        unoptimizedClassPool.addClass(programClass);
    }


    private boolean isKept(ProgramClass programClass)
    {
        if((programClass.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0 &&
           (programClass.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0){
            UnkeptFieldFinder unkeptFieldFinder = new UnkeptFieldFinder();
            programClass.fieldsAccept(unkeptFieldFinder);
            return !unkeptFieldFinder.found;
        }

        return false;
    }


    private class HierarchyClassVisitor
    implements    ClassVisitor
    {
        // Implementations for ClassVisitor.

        @Override
        public void visitAnyClass(Clazz clazz) {}


        @Override
        public void visitProgramClass(ProgramClass programClass)
        {
            // For classes that are only a subclass or a superclass of a Gson
            // "seed", we don't want to visit the super and sub classes in the
            // class hierarchy.
            handleDomainClass(programClass, null);
        }
    }


    private class AnnotationFinder
    implements    AnnotationVisitor
    {
        private boolean found;

        // Implementations for AnnotationVisitor.

        @Override
        public void visitAnnotation(Clazz clazz, Annotation annotation)
        {
            found = true;
        }
    }


    private class UnkeptFieldFinder
    implements    MemberVisitor
    {
        private boolean found;

        // Implementations for MemberVisitor.

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField)
        {
            found = found || !isKept(programField);
        }

        // Utility methods.

        private boolean isKept(ProgramField programField)
        {
            return (programField.getProcessingFlags() & ProcessingFlags.DONT_SHRINK)    != 0 &&
                   (programField.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0;
        }
    }
}
