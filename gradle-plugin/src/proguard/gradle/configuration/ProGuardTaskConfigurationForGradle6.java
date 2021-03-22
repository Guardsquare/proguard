package proguard.gradle.configuration;

import org.gradle.api.Transformer;
import org.gradle.api.internal.provider.ProviderInternal;
import org.gradle.api.internal.provider.TransformBackedProvider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import proguard.Configuration;

class ProGuardTaskConfigurationForGradle6 extends ProGuardTaskConfiguration {
    public ProGuardTaskConfigurationForGradle6(Provider<Configuration> configurationProvider, ObjectFactory objectFactory) {
        super(configurationProvider, objectFactory);
    }

    /**
     * There is a bug in Gradle versions prior to 7.0 that forces a call to `Provider.isPresent()` for every `InputFile` property
     * when constructing the task graph. This breaks Proguard, since we may not know all of the input files at this time.
     * This is a hack to workaround this limitation. It is not required for Gradle 7+.
     */
    protected <T> Provider<T> mappedProvider(Transformer<T, Configuration> transformer) {
        return new TransformBackedProvider<T, Configuration>(transformer, (ProviderInternal<? extends Configuration>) configurationProvider) {
            @Override
            public boolean isPresent() {
                return true;
            }

            @Override
            public T get() {
                return super.getOrNull();
            }
        };
    }
}
