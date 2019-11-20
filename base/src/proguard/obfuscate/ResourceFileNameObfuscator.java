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
package proguard.obfuscate;

import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.*;
import proguard.util.*;

/**
 * This ResourceFileVisitor obfuscates the names of all visited resource files,
 * using a given StringFunction to map given names on new, obfuscated names.
 *
 * @author Johan Leys
 */
public class ResourceFileNameObfuscator
extends      SimplifiedResourceFileVisitor
implements   ResourceFileVisitor
{
    private final StringFunction      nameObfuscationFunction;
    private final boolean             overrideAlreadyObfuscatedNames;
    private final ResourceFileVisitor extraVisitor;


    public ResourceFileNameObfuscator(StringFunction nameObfuscationFunction,
                                      boolean        overrideAlreadyObfuscatedNames)
    {
        this(nameObfuscationFunction, overrideAlreadyObfuscatedNames, null);
    }


    public ResourceFileNameObfuscator(StringFunction      nameObfuscationFunction,
                                      boolean             overrideAlreadyObfuscatedNames,
                                      ResourceFileVisitor extraVisitor)
    {
        this.nameObfuscationFunction        = nameObfuscationFunction;
        this.overrideAlreadyObfuscatedNames = overrideAlreadyObfuscatedNames;
        this.extraVisitor                   = extraVisitor;
    }


    // Implementations for ResourceFileVisitor.

    @Override
    public void visitAnyResourceFile(ResourceFile resourceFile)
    {
        if (overrideAlreadyObfuscatedNames || !isObfuscated(resourceFile))
        {
            String obfuscatedFileName = nameObfuscationFunction.transform(resourceFile.fileName);
            if (obfuscatedFileName != null)
            {
                setNewResourceFileName(resourceFile, obfuscatedFileName);
            }

            if (extraVisitor != null)
            {
                resourceFile.accept(extraVisitor);
            }
        }
    }


    /**
     * Assigns a new name to the given resource file.
     * @param resourceFile the given resource file.
     * @param newFileName  the new name.
     */
    private static void setNewResourceFileName(ResourceFile resourceFile, String newFileName)
    {
        // Store the original filename as visitor info.
        resourceFile.setVisitorInfo(resourceFile.fileName);
        resourceFile.fileName = newFileName;
    }


    /**
     * Returns whether the given resource file has been obfuscated by an instance of this class.
     * @param resourceFile the given resource file.
     * @return true if the given resource file has been obfuscated by an instance of this class, false otherwise.
     */
    public static boolean isObfuscated(ResourceFile resourceFile)
    {
        String originalResourceFileName = getOriginalResourceFileName(resourceFile);
        return !resourceFile.getFileName().equals(originalResourceFileName);
    }


    /**
     * Retrieves the original name of the given resource file.
     * @param resourceFile the given resource file.
     * @return the resource file's original name.
     */
    public static String getOriginalResourceFileName(ResourceFile resourceFile)
    {
        Object visitorInfo = resourceFile.getVisitorInfo();

        return visitorInfo instanceof String ?
            (String)visitorInfo :
            resourceFile.fileName;
    }
}
