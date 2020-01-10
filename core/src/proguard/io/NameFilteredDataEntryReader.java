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

import proguard.util.*;

import java.util.List;

/**
 * This {@link DataEntryReader} delegates to one of two other {@link DataEntryReader} instances,
 * depending on the name of the data entry.
 *
 * @author Eric Lafortune
 */
public class NameFilteredDataEntryReader extends FilteredDataEntryReader
{
    /**
     * Creates a new NameFilteredDataEntryReader that delegates to the given
     * reader, depending on the given list of filters.
     */
    public NameFilteredDataEntryReader(String          regularExpression,
                                       DataEntryReader acceptedDataEntryReader)
    {
        this(regularExpression, acceptedDataEntryReader, null);
    }


    /**
     * Creates a new NameFilteredDataEntryReader that delegates to either of
     * the two given readers, depending on the given list of filters.
     */
    public NameFilteredDataEntryReader(String          regularExpression,
                                       DataEntryReader acceptedDataEntryReader,
                                       DataEntryReader rejectedDataEntryReader)
    {
        super(new DataEntryNameFilter(new ListParser(new FileNameParser()).parse(regularExpression)),
              acceptedDataEntryReader,
              rejectedDataEntryReader);
    }


    /**
     * Creates a new NameFilteredDataEntryReader that delegates to the given
     * reader, depending on the given list of filters.
     */
    public NameFilteredDataEntryReader(List            regularExpressions,
                                       DataEntryReader acceptedDataEntryReader)
    {
        this(regularExpressions, acceptedDataEntryReader, null);
    }


    /**
     * Creates a new NameFilteredDataEntryReader that delegates to either of
     * the two given readers, depending on the given list of filters.
     */
    public NameFilteredDataEntryReader(List            regularExpressions,
                                       DataEntryReader acceptedDataEntryReader,
                                       DataEntryReader rejectedDataEntryReader)
    {
        super(new DataEntryNameFilter(new ListParser(new FileNameParser()).parse(regularExpressions)),
              acceptedDataEntryReader,
              rejectedDataEntryReader);
    }
}
