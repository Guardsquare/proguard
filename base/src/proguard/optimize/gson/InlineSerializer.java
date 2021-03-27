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
import proguard.classfile.editor.*;

/**
 * Interface for injecting optimized code for serializing a field of a class
 * to Json.
 *
 * @author Lars Vandenbergh
 */
interface InlineSerializer
{
    /**
     * Indicates whether the serializer can inject optimized code given which
     * GSON builder invocations are utilized in the program code.
     *
     * @param programClassPool     The class pool containing the program classes.
     * @param gsonRuntimeSettings  tracks the GSON parameters that are utilized
     *                             in the code.
     * @return true if and only if the serializer can inject optimized code.
     */
    boolean canSerialize(ClassPool           programClassPool,
                         GsonRuntimeSettings gsonRuntimeSettings);

    /**
     * Appends optimized code for serializing the given field of the given class
     * using the given code attribute editor and instruction sequence builder.
     *
     * The current locals are:
     * 0 this (the domain object)
     * 1 gson
     * 2 jsonWriter
     * 3 optimizedJsonWriter
     *
     * @param programClass         The domain class containing the field to
     *                             serialize.
     * @param programField         The field of the domain class to serialize.
     * @param composer             the composer to be used for adding
     *                             instructions.
     * @param gsonRuntimeSettings  tracks the GSON parameters that are utilized
     *                             in the code.
     */
    void serialize(ProgramClass                 programClass,
                   ProgramField                 programField,
                   CompactCodeAttributeComposer composer,
                   GsonRuntimeSettings          gsonRuntimeSettings);
}
