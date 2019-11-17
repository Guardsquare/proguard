/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.util;

/**
 * Constants used by DexGuard for marking entities (classes, class members, resource files, ...) during processing.
 *
 * @author Johan Leys
 */
public class ProcessingFlags
{
    // External configuration flags.
    public static final int DONT_SHRINK         = 0x00100000; // Marks whether an entity should not be shrunk.
    public static final int DONT_OPTIMIZE       = 0x00200000; // Marks whether an entity should not be optimized.
    public static final int DONT_OBFUSCATE      = 0x00400000; // Marks whether an entity should not be obfuscated.
    public static final int DONT_MULTIDEX       = 0x00800000; // Marks whether a class should be added to the primary dex file.
    public static final int ENCRYPT             = 0x01000000; // Marks whether an entity should be encrypted.
    public static final int REFLECT             = 0x04000000; // Marks whether a class or class member should be accessed through reflection.
    public static final int ENCRYPT_REFLECTION  = 0x08000000; // Marks whether reflection strings should be encrypted.
    public static final int VIRTUALIZE_CODE     = 0x10000000; // Marks whether a code attribute should be virtualized.
    public static final int OBFUSCATE_CODE      = 0xc0000000; // Marks whether a code attribute should be obfuscated.

    // Internal processing flags.
    public static final int IS_CLASS_AVAILABLE            = 0x00000001; // Marks whether a class member can be used for generalization or specialization.
    public static final int RENAMED                       = 0x00000002; // Marks whether an entity has been renamed.
    public static final int REMOVED_FIELDS                = 0x00000004; // Marks whether a class has (at least one) fields removed.
    public static final int REMOVED_PUBLIC_FIELDS         = 0x00000008; // Marks whether a class has (at least one) public fields removed.
    public static final int REMOVED_CONSTRUCTORS          = 0x00000010; // Marks whether a class has (at least one) constructors removed.
    public static final int REMOVED_PUBLIC_CONSTRUCTORS   = 0x00000020; // Marks whether a class has (at least one) public constructors removed.
    public static final int REMOVED_METHODS               = 0x00000040; // Marks whether a class has (at least one) methods removed.
    public static final int REMOVED_PUBLIC_METHODS        = 0x00000080; // Marks whether a class has (at least one) public methods removed.
    public static final int ENCRYPTED_PREDICATE           = 0x00000100; // Marks whether fields in a class are predicates to be encrypted.
    public static final int INJECTED                      = 0x00000200; // Marks whether an entity was injected by DexGuard.
    public static final int ENCRYPTED_CLASS_LOADER        = 0x00000400; // Marks whether a class is an encrypted class loader, injected by DexGuard.
    public static final int ENCRYPTED_RESOURCE_FILE_NAMES = 0x00000800; // Marks whether resource file names in a method should be encrypted.


    // A mask for processing flags that can be copied as well when e.g. inlining a method / merging a class.
    // TODO: needs to be extended, e.g. with OBFUSCATE_CODE.
    public static final int COPYABLE_PROCESSING_FLAGS = ENCRYPTED_RESOURCE_FILE_NAMES;

}
