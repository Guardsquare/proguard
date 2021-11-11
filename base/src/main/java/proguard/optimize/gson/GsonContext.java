/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.*;

/**
 * This class groups all information about how the Gson library is being used
 * in a program class pool.
 *
 * @author Lars Vandenbergh
 */
public class GsonContext
{
    public ClassPool           gsonDomainClassPool;
    public GsonRuntimeSettings gsonRuntimeSettings;


    /**
     * Sets up the Gson context for the given program class pool.
     * Notes will be printed to the given printer if provided.
     *
     * @param programClassPool the program class pool
     * @param libraryClassPool the library class pool
     * @param warningPrinter   the optional warning printer to which notes
     *                         can be printed.
     */
    public void setupFor(ClassPool      programClassPool,
                         ClassPool      libraryClassPool,
                         WarningPrinter warningPrinter)
    {
        // Only apply remaining optimizations to classes that are not part of
        // Gson itself.
        ClassPool filteredClasses = new ClassPool();
        programClassPool.classesAccept(
            new ClassNameFilter("!com/google/gson/**",
                                new ClassPoolFiller(filteredClasses)));

        // Find all GsonBuilder invocations.
        gsonRuntimeSettings      = new GsonRuntimeSettings();
        GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
            new GsonBuilderInvocationFinder(
                programClassPool,
                libraryClassPool,
                gsonRuntimeSettings,
                new ClassPoolFiller(gsonRuntimeSettings.instanceCreatorClassPool),
                new ClassPoolFiller(gsonRuntimeSettings.typeAdapterClassPool));

        filteredClasses.classesAccept(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new AllInstructionVisitor(gsonBuilderInvocationFinder))));

        // Find all Gson invocations.
        gsonDomainClassPool = new ClassPool();
        GsonDomainClassFinder domainClassFinder  =
            new GsonDomainClassFinder(gsonRuntimeSettings,
                                      gsonDomainClassPool,
                                      warningPrinter);

        filteredClasses.accept(
            new AllClassVisitor(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new AllInstructionVisitor(
            new MultiInstructionVisitor(
                new GsonSerializationInvocationFinder(programClassPool,
                                                      libraryClassPool,
                                                      domainClassFinder,
                                                      warningPrinter),
                new GsonDeserializationInvocationFinder(programClassPool,
                                                        libraryClassPool,
                                                        domainClassFinder,
                                                        warningPrinter)))))));
    }

}
