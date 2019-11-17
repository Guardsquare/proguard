/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
