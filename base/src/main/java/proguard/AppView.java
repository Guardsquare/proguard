/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard;

import proguard.classfile.*;
import proguard.configuration.InitialStateInfo;
import proguard.io.ExtraDataEntryNameMap;
import proguard.resources.file.ResourceFilePool;

public class AppView
{
    // App model.
    public final ClassPool             programClassPool;
    public final ClassPool             libraryClassPool;
    public final ResourceFilePool      resourceFilePool;

    public final ExtraDataEntryNameMap extraDataEntryNameMap;

    /**
     * Stores information about the original state of the program class pool used for configuration debugging.
     */
    public       InitialStateInfo      initialStateInfo;


    public AppView()
    {
        this(new ClassPool(), new ClassPool(), new ResourceFilePool(), new ExtraDataEntryNameMap());
    }

    public AppView(ClassPool             programClassPool,
                   ClassPool             libraryClassPool,
                   ResourceFilePool      resourceFilePool,
                   ExtraDataEntryNameMap extraDataEntryNameMap)
    {
        this.programClassPool      = programClassPool;
        this.resourceFilePool      = resourceFilePool;
        this.libraryClassPool      = libraryClassPool;
        this.extraDataEntryNameMap = extraDataEntryNameMap;
    }
}
