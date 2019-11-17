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
 * This class contains utility methods operating on objects.
 */
public class ObjectUtil
{
    /**
     * Returns whether the given objects are the same.
     * @param object1 the first object, may be null.
     * @param object2 the second object, may be null.
     * @return whether the objects are the same.
     */
    public static boolean equal(Object object1, Object object2)
    {
        return object1 == null ?
            object2 == null :
            object1.equals(object2);
    }


    /**
     * Returns the hash code of the given object, or 0 if it is null.
     * @param object the object, may be null.
     * @return the hash code.
     */
    public static int hashCode(Object object)
    {
        return object == null ? 0 : object.hashCode();
    }


    /**
     * Returns a comparison of the two given objects.
     * @param object1 the first object, may be null.
     * @param object2 the second object, may be null.
     * @return -1, 0, or 1.
     * @see Comparable#compareTo(Object)
     */
    public static int compare(Comparable object1, Comparable object2)
    {
        return object1 == null ?
            object2 == null ? 0 : -1 :
            object2 == null ? 1 : object1.compareTo(object2);
    }
}
