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

import java.util.*;

/**
 * A key-values map that can have multiple values associated with each key.
 *
 * There is an efficient lookup method to retrieve all values of all keys.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Johan Leys
 */
public class MultiValueMap<K, V>
{
    private final Map<K, Set<V>> keyValueMap = createKeyMap();

    private final Set<V> values = createValueSet();


    protected Set<V> createValueSet()
    {
        return new HashSet<V>();
    }


    protected Map<K, Set<V>> createKeyMap()
    {
        return new HashMap<K, Set<V>>();
    }


    public int size()
    {
        return keyValueMap.size();
    }


    public Set<K> keySet()
    {
        return keyValueMap.keySet();
    }


    public Collection<Set<V>> values()
    {
        return keyValueMap.values();
    }


    public Set<Map.Entry<K, Set<V>>> entrySet()
    {
        return keyValueMap.entrySet();
    }


    public void put(K key, V value)
    {
        putAll(key, Collections.singleton(value));
    }


    public void putAll(Set<K> key, V value)
    {
        putAll(key, Collections.singleton(value));
    }


    public void putAll(Set<K> keys, Set<V> values)
    {
        for (K key : keys)
        {
            putAll(key, values);
        }
    }


    public void putAll(K key, Set<V> values)
    {
        this.values.addAll(values);
        Set<V> existingValues = keyValueMap.get(key);
        if (existingValues == null)
        {
            existingValues = createValueSet();
            keyValueMap.put(key, existingValues);
        }
        existingValues.addAll(values);
    }


    public boolean remove(K key, V value)
    {
        Set<V> values = keyValueMap.get(key);
        return values != null && values.remove(value);
    }


    public Set<V> get(K key)
    {
        return keyValueMap.get(key);
    }


    /**
     * Returns a Set with all values of all keys.
     *
     * @return a Set with all values of all keys.
     */
    public Set<V> getValues()
    {
        return values;
    }


    public void clear()
    {
        keyValueMap.clear();
        values.clear();
    }
}
