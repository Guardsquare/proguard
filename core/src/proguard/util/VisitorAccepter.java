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
 * This interface is a base interface for visitor accepters. It allows
 * visitors to set and get any temporary information they desire on the
 * objects they are visiting. Note that every visitor accepter has only one
 * such property, so visitors will have to take care not to overwrite each
 * other's information, if it is still required.
 *
 * @author Eric Lafortune
 */
public interface VisitorAccepter
{
    /**
     * Gets the visitor information of the visitor accepter.
     */
    public Object getVisitorInfo();


    /**
     * Sets the visitor information of the visitor accepter.
     */
    public void setVisitorInfo(Object visitorInfo);
}
