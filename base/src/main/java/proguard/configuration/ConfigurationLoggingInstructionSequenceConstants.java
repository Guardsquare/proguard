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
package proguard.configuration;

import proguard.classfile.ClassPool;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.InstructionSequenceMatcher;


/**
 * This class contains a set of instruction sequences for accessing class
 * information via reflection, and replacement instructions that add logging
 * information on the reflection that is used.
 *
 * @author Johan Leys
 */
public class ConfigurationLoggingInstructionSequenceConstants
{
    static final String LOGGER_CLASS_NAME = ClassUtil.internalClassName(ConfigurationLogger.class.getName());


    // Matched constants.
    public static final int CLASS_NAME             = 0x30000000;
    public static final int LOCAL_VARIABLE_INDEX_1 = 0x30000001;
    public static final int LOCAL_VARIABLE_INDEX_2 = 0x30000002;
    public static final int LOCAL_VARIABLE_INDEX_3 = 0x30000003;

    public static final int CONSTANT_INDEX = InstructionSequenceMatcher.X;

    public final Instruction[][][] RESOURCE;
    public final Constant[]        CONSTANTS;


    /**
     * Creates a new instance of ResourceIdInstructionSequenceConstants,
     * with constants that reference classes from the given class pools.
     */
    public ConfigurationLoggingInstructionSequenceConstants(ClassPool programClassPool,
                                                            ClassPool libraryClassPool)
    {
        InstructionSequenceBuilder ____ =
            new InstructionSequenceBuilder(programClassPool, libraryClassPool);

        RESOURCE = new Instruction[][][]
            {
                // Classes.
                {
                    // Automatically detected and kept - don't check anything.
                    ____.ldc_(CONSTANT_INDEX)
                        .invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;").__(),

                    ____.ldc_(CONSTANT_INDEX)
                        .invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;").__()
                },
                {
                    ____.invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkForName", "(Ljava/lang/String;Ljava/lang/String;)V")
                        .invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;").__()
                },
                {
                    ____.invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;").__(),

                    ____.dup_x2()
                        .pop()
                        .dup_x2()
                        .pop()
                        .dup_x2()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkForName", "(Ljava/lang/String;Ljava/lang/String;)V")
                        .invokestatic("java/lang/Class", "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;").__()
                },
                {
                    ____.invokevirtual("java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkLoadClass", "(Ljava/lang/String;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;").__(),
                },

                // Constructors.
                {
                    ____.invokevirtual("java/lang/Class", "getDeclaredConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;").__(),

                    ____.dup2()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetDeclaredConstructor", "(Ljava/lang/Class;[Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getDeclaredConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;").__(),

                    ____.dup2()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetConstructor", "(Ljava/lang/Class;[Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetDeclaredConstructors", "(Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getConstructors", "()[Ljava/lang/reflect/Constructor;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetConstructors", "(Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getConstructors", "()[Ljava/lang/reflect/Constructor;").__()
                },

                // Methods.
                {
                    ____.invokevirtual("java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;").__(),

                    ____.dup_x2()
                        .astore(LOCAL_VARIABLE_INDEX_1)
                        .dup_x2()
                        .astore(LOCAL_VARIABLE_INDEX_2)
                        .dup_x2()
                        .astore(LOCAL_VARIABLE_INDEX_3)
                        .aload(LOCAL_VARIABLE_INDEX_3)
                        .aload(LOCAL_VARIABLE_INDEX_2)
                        .aload(LOCAL_VARIABLE_INDEX_1)
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetDeclaredMethod", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;").__(),

                    ____.dup_x2()
                        .astore(LOCAL_VARIABLE_INDEX_1)
                        .dup_x2()
                        .astore(LOCAL_VARIABLE_INDEX_2)
                        .dup_x2()
                        .astore(LOCAL_VARIABLE_INDEX_3)
                        .aload(LOCAL_VARIABLE_INDEX_3)
                        .aload(LOCAL_VARIABLE_INDEX_2)
                        .aload(LOCAL_VARIABLE_INDEX_1)
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetMethod", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getDeclaredMethods", "()[Ljava/lang/reflect/Method;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetDeclaredMethods", "(Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getDeclaredMethods", "()[Ljava/lang/reflect/Method;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getMethods", "()[Ljava/lang/reflect/Method;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetMethods", "(Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getMethods", "()[Ljava/lang/reflect/Method;").__()
                },

                // Fields.

                {
                    ____.invokevirtual("java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;").__(),

                    ____.dup2()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetDeclaredField", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;").__(),

                    ____.dup2()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetField", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getDeclaredFields", "()[Ljava/lang/reflect/Field;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetDeclaredFields", "(Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getDeclaredFields", "()[Ljava/lang/reflect/Field;").__()
                },
                {
                    ____.invokevirtual("java/lang/Class", "getFields", "()[Ljava/lang/reflect/Field;").__(),

                    ____.dup()
                        .ldc_(CLASS_NAME)
                        .invokestatic(LOGGER_CLASS_NAME, "checkGetFields", "(Ljava/lang/Class;Ljava/lang/String;)V")
                        .invokevirtual("java/lang/Class", "getFields", "()[Ljava/lang/reflect/Field;").__()
                },
            };

        CONSTANTS = ____.constants();
    }
}
