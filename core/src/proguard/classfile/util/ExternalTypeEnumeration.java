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
package proguard.classfile.util;

import proguard.classfile.JavaTypeConstants;

/**
 * An <code>ExternalTypeEnumeration</code> provides an enumeration of all
 * types listed in a given external descriptor string. The method name can
 * be retrieved separately.
 * <p>
 * A <code>ExternalTypeEnumeration</code> object can be reused for processing
 * different subsequent descriptors, by means of the <code>setDescriptor</code>
 * method.
 *
 * @author Eric Lafortune
 */
public class ExternalTypeEnumeration
{
    private String descriptor;
    private int    index;


    public ExternalTypeEnumeration(String descriptor)
    {
        setDescriptor(descriptor);
    }


    ExternalTypeEnumeration()
    {
    }


    void setDescriptor(String descriptor)
    {
        this.descriptor = descriptor;

        reset();
    }


    public void reset()
    {
        index = descriptor.indexOf(JavaTypeConstants.METHOD_ARGUMENTS_OPEN) + 1;

        if (index < 1)
        {
            throw new IllegalArgumentException("Missing opening parenthesis in descriptor ["+descriptor+"]");
        }
    }


    public boolean hasMoreTypes()
    {
        return index < descriptor.length() - 1;
    }


    public String nextType()
    {
        int startIndex = index;

        // Find the next separating comma.
        index = descriptor.indexOf(JavaTypeConstants.METHOD_ARGUMENTS_SEPARATOR,
                                   startIndex);

        // Otherwise find the closing parenthesis.
        if (index < 0)
        {
            index = descriptor.indexOf(JavaTypeConstants.METHOD_ARGUMENTS_CLOSE,
                                       startIndex);
            if (index < 0)
            {
                throw new IllegalArgumentException("Missing closing parenthesis in descriptor ["+descriptor+"]");
            }
        }

        return descriptor.substring(startIndex, index++).trim();
    }


    public String methodName()
    {
        return descriptor.substring(0, descriptor.indexOf(JavaTypeConstants.METHOD_ARGUMENTS_OPEN)).trim();
    }
}
