/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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

package proguard.gradle.configuration;

import org.gradle.api.Transformer;
import org.gradle.api.internal.provider.ProviderInternal;
import org.gradle.api.internal.provider.TransformBackedProvider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import proguard.Configuration;

class ProGuardTaskConfigurationForGradle6 extends ProGuardTaskConfiguration
{
    public ProGuardTaskConfigurationForGradle6(Provider<Configuration> configurationProvider, ObjectFactory objectFactory)
    {
        super(configurationProvider, objectFactory);
    }

    /**
     * There is a bug in Gradle versions prior to 7.0 that forces a call to `Provider.isPresent()` for every `InputFile` property
     * when constructing the task graph. This breaks Proguard, since we may not know all of the input files at this time.
     * This is a hack to workaround this limitation. It is not required for Gradle 7+.
     */
    protected <T> Provider<T> mappedProvider(Transformer<T, Configuration> transformer)
    {
        return new TransformBackedProvider<T, Configuration>(transformer, (ProviderInternal<? extends Configuration>) configurationProvider)
        {
            @Override
            public boolean isPresent()
            {
                return true;
            }

            @Override
            public T get()
            {
                return super.getOrNull();
            }
        };
    }
}
