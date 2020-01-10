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
package proguard.resources.file.visitor;

import proguard.resources.file.*;

/**
 * This {@link ResourceFileVisitor} delegates its visits to one of two
 * {@link ResourceFileVisitor} instances, depending on whether the name of
 * the visited resource file is present in a given {@link ResourceFilePool} or not.
 *
 * @author Thomas Neidhart
 */
public class ResourceFilePresenceFilter
implements   ResourceFileVisitor
{
    private final ResourceFilePool    resourceFilePool;
    private final ResourceFileVisitor presentResourceFileVisitor;
    private final ResourceFileVisitor missingResourceFileVisitor;


    /**
     * Creates a new ResourceFilePresenceFilter.
     * @param resourceFilePool           the <code>ResourceFilePool</code> in which the
     *                                   presence will be tested.
     * @param presentResourceFileVisitor the <code>ResourceFileVisitor</code> to which visits
     *                                   of present resource files will be delegated.
     * @param missingResourceFileVisitor the <code>ResourceFileVisitor</code> to which visits
     *                                   of missing resource files will be delegated.
     */
    public ResourceFilePresenceFilter(ResourceFilePool    resourceFilePool,
                                      ResourceFileVisitor presentResourceFileVisitor,
                                      ResourceFileVisitor missingResourceFileVisitor)
    {
        this.resourceFilePool           = resourceFilePool;
        this.presentResourceFileVisitor = presentResourceFileVisitor;
        this.missingResourceFileVisitor = missingResourceFileVisitor;
    }


    // Implementations for ResourceFileVisitor.

    @Override
    public void visitResourceFile(ResourceFile resourceFile)
    {
        ResourceFileVisitor resourceFileVisitor = resourceFileVisitor(resourceFile);

        if (resourceFileVisitor != null)
        {
            resourceFileVisitor.visitResourceFile(resourceFile);
        }
    }


    // Small utility methods.

    /**
     * Returns the appropriate <code>ResourceFileVisitor</code>.
     */
    private ResourceFileVisitor resourceFileVisitor(ResourceFile resourceFile)
    {
        return resourceFilePool.getResourceFile(resourceFile.getFileName()) != null ?
            presentResourceFileVisitor :
            missingResourceFileVisitor;
    }
}
