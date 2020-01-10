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
 * This {@link DataEntryWriter} delegates to one of two other {@link DataEntryWriter} instances,
 * depending on the name of the data entry.
 *
 * @author Eric Lafortune
 */
public class NameFilteredDataEntryWriter extends FilteredDataEntryWriter
{
    /**
     * Creates a new NameFilteredDataEntryWriter that delegates to the given
     * writer, depending on the given list of filters.
     */
    public NameFilteredDataEntryWriter(String          regularExpression,
                                       DataEntryWriter acceptedDataEntryWriter)
    {
        this(regularExpression, acceptedDataEntryWriter, null);
    }


    /**
     * Creates a new NameFilteredDataEntryWriter that delegates to either of
     * the two given writers, depending on the given list of filters.
     */
    public NameFilteredDataEntryWriter(String          regularExpression,
                                       DataEntryWriter acceptedDataEntryWriter,
                                       DataEntryWriter rejectedDataEntryWriter)
    {
        this(new ListParser(new FileNameParser()).parse(regularExpression),
             acceptedDataEntryWriter,
             rejectedDataEntryWriter);
    }


    /**
     * Creates a new NameFilteredDataEntryWriter that delegates to the given
     * writer, depending on the given list of filters.
     */
    public NameFilteredDataEntryWriter(List            regularExpressions,
                                       DataEntryWriter acceptedDataEntryWriter)
    {
        this(regularExpressions, acceptedDataEntryWriter, null);
    }


    /**
     * Creates a new NameFilteredDataEntryWriter that delegates to either of
     * the two given writers, depending on the given list of filters.
     */
    public NameFilteredDataEntryWriter(List            regularExpressions,
                                       DataEntryWriter acceptedDataEntryWriter,
                                       DataEntryWriter rejectedDataEntryWriter)
    {
        this(new ListParser(new FileNameParser()).parse(regularExpressions),
             acceptedDataEntryWriter,
             rejectedDataEntryWriter);
    }


    /**
     * Creates a new NameFilteredDataEntryWriter that delegates to the given
     * writer, depending on the given string matcher.
     */
    public NameFilteredDataEntryWriter(StringMatcher   stringMatcher,
                                       DataEntryWriter acceptedDataEntryWriter)
    {
        this(stringMatcher, acceptedDataEntryWriter, null);
    }


    /**
     * Creates a new NameFilteredDataEntryWriter that delegates to either of
     * the two given writers, depending on the given string matcher.
     */
    public NameFilteredDataEntryWriter(StringMatcher   stringMatcher,
                                       DataEntryWriter acceptedDataEntryWriter,
                                       DataEntryWriter rejectedDataEntryWriter)
    {
        super(new DataEntryNameFilter(stringMatcher),
              acceptedDataEntryWriter,
              rejectedDataEntryWriter);
    }
}