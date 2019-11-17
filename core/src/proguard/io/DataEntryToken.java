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
 * Represents a character sequence that is part of a {@link DataEntry} and that
 * has a certain meaning denoted by its {@link DataEntryTokenType}.
 *
 * @author Lars Vandenbergh
 */
public class DataEntryToken
{
    public String             string;
    public DataEntryTokenType type;


    public DataEntryToken(String string, DataEntryTokenType type)
    {
        this.string = string;
        this.type = type;
    }
}
