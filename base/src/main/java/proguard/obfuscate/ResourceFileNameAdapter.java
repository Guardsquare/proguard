/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.obfuscate;

import proguard.AppView;
import proguard.Configuration;
import proguard.pass.Pass;
import proguard.util.FileNameParser;
import proguard.util.ListParser;

/**
 * This pass adapts resource file names that correspond to class names, if necessary.
 *
 * @author Tim Van Den Broecke
 */
public class ResourceFileNameAdapter
implements   Pass
{
    private final Configuration configuration;

    public ResourceFileNameAdapter(Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void execute(AppView appView)
    {
        appView.resourceFilePool.resourceFilesAccept(
            new ListParser(new FileNameParser()).parse(configuration.adaptResourceFileNames),
            new ResourceFileNameObfuscator(new ClassNameAdapterFunction(appView.programClassPool), true));
    }
}
