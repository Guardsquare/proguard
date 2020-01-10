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
package proguard.resources.file;

import proguard.resources.file.visitor.*;
import proguard.util.*;

import java.util.*;

/**
 * This is a set of {@link ResourceFile} instances. They can be enumerated or
 * retrieved by name. They can also be accessed by means of resource file visitors.
 *
 * @author Johan Leys
 */
public class ResourceFilePool
{
    // We're using a sorted tree map instead of a hash map to store the
    // instances, in order to make the processing more deterministic.
    private final Map<String, ResourceFile>  resourceFileMap  = new TreeMap<String, ResourceFile>();

    /**
     * Creates a new empty ResourceFilePool.
     */
    public ResourceFilePool() {}


    /**
     * Creates a new ResourceFilePool with the given resource files.
     *
     * @param resourceFiles the resource files to be added.
     */
    public ResourceFilePool(ResourceFile... resourceFiles)
    {
        for (ResourceFile resourceFile : resourceFiles)
        {
            addResourceFile(resourceFile);
        }
    }


    /**
     * Creates a new ResourceFilePool with the given resource files.
     *
     * @param resourceFiles the resource files to be added.
     */
    public ResourceFilePool(Iterable<? extends ResourceFile> resourceFiles)
    {
        for (ResourceFile resourceFile : resourceFiles)
        {
            addResourceFile(resourceFile);
        }
    }


    /**
     * Clears the pool.
     */
    public void clear()
    {
        resourceFileMap.clear();
    }


    /**
     * Adds the given ResourceFile to this pool.
     */
    public void addResourceFile(ResourceFile resourceFile)
    {
        addResourceFile(resourceFile.getFileName(), resourceFile);
    }


    /**
     * Adds the given ResourceFile with the given name to this pool.
     */
    public void addResourceFile(String fileName, ResourceFile resourceFile)
    {
        resourceFileMap.put(fileName, resourceFile);
    }


    /**
     * Removes the specified ResourceFile from this pool.
     */
    public void removeResourceFile(String fileName)
    {
        resourceFileMap.remove(fileName);
    }


    /**
     * Returns a ResourceFile from this pool, based on its name.
     * Returns <code>null</code> if the instance with the given name is not in the pool.
     */
    public ResourceFile getResourceFile(String fileName)
    {
        return resourceFileMap.get(fileName);
    }


    /**
     * Returns the number of resource files in this pool.
     */
    public int size()
    {
        return resourceFileMap.size();
    }


    /**
     * Returns a ResourceFilePool with the same resource files, but with the
     * keys that correspond to the names of the resource file instances.
     * This can be useful to create a resource file pool with obfuscated names.
     */
    public ResourceFilePool refreshedCopy()
    {
        return new ResourceFilePool(resourceFileMap.values());
    }


    // Note: for consistency, use visitors whenever possible.
    /**
     * Returns a Set of all resource file names in this resource file pool.
     */
    public Set<String> resourceFileNames()
    {
        return resourceFileMap.keySet();
    }


    /**
     * Applies the given ResourceFileVisitor to all instances in this pool.
     */
    public void resourceFilesAccept(ResourceFileVisitor resourceFileVisitor)
    {
        for (Map.Entry<String, ResourceFile> entry : resourceFileMap.entrySet())
        {
            entry.getValue().accept(resourceFileVisitor);
        }
    }


    /**
     * Applies the given ResourceFileVisitor to all resource files in this pool matching the given file name filter.
     */
    public void resourceFilesAccept(String              fileNameFilter,
                                    ResourceFileVisitor resourceFileVisitor)
    {
        resourceFilesAccept(new ListParser(new FileNameParser()).parse(fileNameFilter),
                            resourceFileVisitor);
    }


    /**
     * Applies the given ResourceFileVisitor to all resource files in this pool matching the given file name filters.
     */
    public void resourceFilesAccept(List<String>        fileNameFilter,
                                    ResourceFileVisitor resourceFileVisitor)
    {
        resourceFilesAccept(new ListParser(new FileNameParser()).parse(fileNameFilter),
                            resourceFileVisitor);
    }


    /**
     * Applies the given ResourceFileVisitor to all resource files in this pool matching the given file name filter.
     */
    public void resourceFilesAccept(StringMatcher       fileNameFilter,
                                    ResourceFileVisitor resourceFileVisitor)
    {
        for (Map.Entry<String,ResourceFile> entry : resourceFileMap.entrySet())
        {
            String fileName = entry.getKey();

            if (fileNameFilter.matches(fileName))
            {
                ResourceFile resourceFile = entry.getValue();
                resourceFile.accept(resourceFileVisitor);
            }
        }
    }


    /**
     * Applies the given ResourceFileVisitor to the instance with the given name, if it is present in this pool.
     */
    public void resourceFileAccept(String              fileName,
                                   ResourceFileVisitor resourceFileVisitor)
    {
        ResourceFile resourceFile = getResourceFile(fileName);
        if (resourceFile != null)
        {
            resourceFile.accept(resourceFileVisitor);
        }
    }


    // Implementations for Object.

    @Override
    public String toString()
    {
        return this.resourceFileMap.toString();
    }
}
