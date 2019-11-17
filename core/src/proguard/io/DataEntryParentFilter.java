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

/**
 * This DataEntryFilter delegates filtering to a DataEntryFilter for its parent.
 *
 * @author Eric Lafortune
 */
public class DataEntryParentFilter
implements   DataEntryFilter
{
    private final DataEntryFilter dataEntryFilter;


    /**
     * Creates a new ParentFilter.
     * @param dataEntryFilter the filter that will be applied to the data
     *                        entry's parent.
     */
    public DataEntryParentFilter(DataEntryFilter dataEntryFilter)
    {
        this.dataEntryFilter = dataEntryFilter;
    }


    // Implementations for DataEntryFilter.

    @Override
    public boolean accepts(DataEntry dataEntry)
    {
        return dataEntry != null && dataEntryFilter.accepts(dataEntry.getParent());
    }
}
