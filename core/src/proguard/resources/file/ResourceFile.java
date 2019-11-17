/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.resources.file;

import proguard.resources.file.visitor.ResourceFileVisitor;
import proguard.util.*;

import java.util.*;

/**
 * This class represents a resource file in an application.
 *
 * @author Johan Leys
 */
public class ResourceFile
extends      SimpleFeatureNamedProcessableVisitorAccepter
{
    public String                     fileName;
    public long                       fileSize;
    public Set<ResourceJavaReference> references;


    /**
     * Creates a new {@link ResourceFile}.
     * @param fileName the name of the resource file.
     * @param fileSize the size of the resource file.
     */
    public ResourceFile(String fileName, long fileSize)
    {
        this(fileName, fileSize, 0);
    }


    public ResourceFile(String fileName, long fileSize, int processingFlags)
    {
        this.fileName        = fileName;
        this.fileSize        = fileSize;
        this.processingFlags = processingFlags;
        this.references      = new HashSet<ResourceJavaReference>();
    }


    /**
     * Returns the file name.
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Returns the file size.
     */
    public long getFileSize()
    {
        return fileSize;
    }


    public void accept(ResourceFileVisitor resourceFileVisitor)
    {
        resourceFileVisitor.visitResourceFile(this);
    }


    // Implementations for Object.

    public String toString()
    {
        return getClass().getSimpleName()+"("+fileName+")";
    }
}
