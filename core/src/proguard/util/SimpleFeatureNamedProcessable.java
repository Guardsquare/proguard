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
 * A {@link SimpleProcessable} that additionally implements {@link FeatureNamed}.
 *
 * @author Eric Lafortune
 */
public class SimpleFeatureNamedProcessable
extends      SimpleProcessable
implements   FeatureNamed
{
    public String featureName;


    /**
     * Creates an uninitialized SimpleFeatureNamedProcessable.
     */
    public SimpleFeatureNamedProcessable() {}


    /**
     * Creates an initialized SimpleFeatureNamedProcessable.
     */
    public SimpleFeatureNamedProcessable(String featureName,
                                         int    processingFlags,
                                         Object processingInfo)
    {
        super(processingFlags, processingInfo);

        this.featureName = featureName;
    }


    // Implementations for FeatureNamed.

    @Override
    public String getFeatureName()
    {
        return featureName;
    }

    @Override
    public void setFeatureName(String featureName)
    {
        this.featureName = featureName;
    }
}
