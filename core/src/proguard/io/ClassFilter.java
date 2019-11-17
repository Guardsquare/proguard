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
package proguard.io;

import proguard.classfile.ClassConstants;
import proguard.util.ExtensionMatcher;


/**
 * This DataEntryReader delegates to one of two other DataEntryReader instances,
 * depending on the extension of the data entry.
 *
 * @author Eric Lafortune
 */
public class ClassFilter extends FilteredDataEntryReader
{
    /**
     * Creates a new ClassFilter that delegates reading classes to the
     * given reader.
     */
    public ClassFilter(DataEntryReader classReader)
    {
        this(classReader, null);
    }


    /**
     * Creates a new ClassFilter that delegates to either of the two given
     * readers.
     */
    public ClassFilter(DataEntryReader classReader,
                       DataEntryReader dataEntryReader)
    {
        super(new DataEntryNameFilter(
              new ExtensionMatcher(ClassConstants.CLASS_FILE_EXTENSION)),
              classReader,
              dataEntryReader);
    }
}
