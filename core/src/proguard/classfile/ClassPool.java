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
package proguard.classfile;

import proguard.classfile.visitor.*;
import proguard.util.*;

import java.util.*;

/**
 * This is a set of representations of classes. They can be enumerated or
 * retrieved by name. They can also be accessed by means of class visitors.
 *
 * @author Eric Lafortune
 */
public class ClassPool
{
    // We're using a sorted tree map instead of a hash map to store the classes,
    // in order to make the processing more deterministic.
    private final Map<String, Clazz> classes = new TreeMap<String, Clazz>();


    /**
     * Creates a new empty ClassPool.
     */
    public ClassPool() {}


    /**
     * Creates a new ClassPool with the given classes.
     *
     * @param classes the classes to be added.
     */
    public ClassPool(Clazz... classes)
    {
        for (Clazz clazz : classes)
        {
            addClass(clazz);
        }
    }


    /**
     * Creates a new ClassPool with the given classes.
     *
     * @param classes the classes to be added.
     */
    public ClassPool(Iterable<? extends Clazz> classes)
    {
        for (Clazz clazz : classes)
        {
            addClass(clazz);
        }
    }


    /**
     * Creates a new ClassPool with the given classes.
     * The keys are taken from the Clazz instances.
     *
     * @param classPool the classes to be added.
     */
    public ClassPool(ClassPool classPool)
    {
        this(classPool.classes());
    }


    /**
     * Clears the class pool.
     */
    public void clear()
    {
        classes.clear();
    }


    /**
     * Adds the given Clazz to the class pool.
     */
    public void addClass(Clazz clazz)
    {
        classes.put(clazz.getName(), clazz);
    }

    /**
     * Adds the given Clazz with the given name to the class pool.
     */
    public void addClass(String name, Clazz clazz)
    {
        classes.put(name, clazz);
    }


    /**
     * Removes the given Clazz from the class pool.
     */
    public void removeClass(Clazz clazz)
    {
        removeClass(clazz.getName());
    }


    /**
     * Removes the Class with the specified name from the class pool.
     */
    public Clazz removeClass(String className)
    {
        return classes.remove(className);
    }


    /**
     * Returns a Clazz from the class pool based on its name. Returns
     * <code>null</code> if the class with the given name is not in the class
     * pool.
     */
    public Clazz getClass(String className)
    {
        return classes.get(className);
    }


    // Note: for consistency, use visitors whenever possible.
    /**
     * Returns an Iterator of all class names in the class pool.
     */
    public Iterator<String> classNames()
    {
        return classes.keySet().iterator();
    }


    // Note: for consistency, use visitors whenever possible.
    /**
     * Returns an Iterable of all classes in the class pool.
     */
    public Iterable<Clazz> classes()
    {
        return classes.values();
    }


    /**
     * Returns the number of classes in the class pool.
     */
    public int size()
    {
        return classes.size();
    }


    /**
     * Returns a ClassPool with the same classes, but with the keys that
     * correspond to the names of the class instances. This can be useful
     * to create a class pool with obfuscated names.
     */
    public ClassPool refreshedCopy()
    {
        return new ClassPool(this);
    }


    /**
     * Returns a Map with the same contents as the given map, but with keys
     * that have been mapped based from the names in the class pool to the
     * names in the corresponding classes. This can be useful to create a
     * map with obfuscated names as keys.
     */
    public <T> Map<String, T> refreshedKeysCopy(Map<String, T> map)
    {
        Map<String, T> refreshedMap = new HashMap<String, T>(map.size());

        // Iterate over all entries.
        Iterator<Map.Entry<String, T>> entries = map.entrySet().iterator();
        while (entries.hasNext())
        {
            // Find the class.
            Map.Entry<String, T> entry = entries.next();
            String className = entry.getKey();
            Clazz  clazz     = classes.get(className);
            if (clazz != null)
            {
                // Add the mapped entry.
                refreshedMap.put(clazz.getName(), entry.getValue());
            }
        }

        return refreshedMap;
    }


    /**
     * Returns a Map with the same contents as the given map, but with values
     * that have been mapped based from the names in the class pool to the
     * names in the corresponding classes. This can be useful to create a
     * map with obfuscated names as values.
     */
    public <T> Map<T, String> refreshedValuesCopy(Map<T, String> map)
    {
        Map<T, String> refreshedMap = new HashMap<T, String>(map.size());

        // Iterate over all entries.
        Iterator<Map.Entry<T, String>> entries = map.entrySet().iterator();
        while (entries.hasNext())
        {
            // Find the class.
            Map.Entry<T, String> entry = entries.next();
            String className = entry.getValue();
            Clazz  clazz     = classes.get(className);
            if (clazz != null)
            {
                // Add the mapped entry.
                refreshedMap.put(entry.getKey(), clazz.getName());
            }
        }

        return refreshedMap;
    }


    /**
     * Returns a Map that represents a mapping from every Clazz in the ClassPool
     * to its original name. This can be useful to retrieve the original name
     * of classes after name obfuscation has been applied.
     */
    public Map<Clazz, String> reverseMapping()
    {
        Map<Clazz, String> reversedMap = new HashMap<Clazz, String>(classes.size());

        // Reverse each entry.
        for (String originalClassName : classes.keySet())
        {
            Clazz processedClazz = classes.get(originalClassName);
            reversedMap.put(processedClazz, originalClassName);
        }

        return reversedMap;
    }


    /**
     * Applies the given ClassPoolVisitor to the class pool.
     */
    public void accept(ClassPoolVisitor classPoolVisitor)
    {
        classPoolVisitor.visitClassPool(this);
    }


    /**
     * Applies the given ClassVisitor to all classes in the class pool,
     * in random order.
     */
    public void classesAccept(ClassVisitor classVisitor)
    {
        Iterator iterator = classes.values().iterator();
        while (iterator.hasNext())
        {
            Clazz clazz = (Clazz)iterator.next();
            clazz.accept(classVisitor);
        }
    }


    /**
     * Applies the given ClassVisitor to all classes in the class pool,
     * in sorted order.
     */
    public void classesAcceptAlphabetically(ClassVisitor classVisitor)
    {
        // We're already using a tree map.
        //TreeMap sortedClasses = new TreeMap(classes);
        //Iterator iterator = sortedClasses.values().iterator();

        Iterator iterator = classes.values().iterator();
        while (iterator.hasNext())
        {
            Clazz clazz = (Clazz)iterator.next();
            clazz.accept(classVisitor);
        }
    }


    /**
     * Applies the given ClassVisitor to all matching classes in the class pool.
     */
    public void classesAccept(String        classNameFilter,
                              ClassVisitor  classVisitor)
    {
        classesAccept(new ListParser(new ClassNameParser()).parse(classNameFilter),
                      classVisitor);
    }


    /**
     * Applies the given ClassVisitor to all matching classes in the class pool.
     */
    public void classesAccept(List          classNameFilter,
                              ClassVisitor  classVisitor)
    {
        classesAccept(new ListParser(new ClassNameParser()).parse(classNameFilter),
                      classVisitor);
    }


    /**
     * Applies the given ClassVisitor to all matching classes in the class pool.
     */
    public void classesAccept(StringMatcher classNameFilter,
                              ClassVisitor  classVisitor)
    {
        Iterator iterator = classes.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry entry     = (Map.Entry)iterator.next();
            String    className = (String   )entry.getKey();

            if (classNameFilter.matches(className))
            {
                Clazz clazz = (Clazz)entry.getValue();
                clazz.accept(classVisitor);
            }
        }
    }


    /**
     * Applies the given ClassVisitor to the class with the given name,
     * if it is present in the class pool.
     */
    public void classAccept(String className, ClassVisitor classVisitor)
    {
        Clazz clazz = getClass(className);
        if (clazz != null)
        {
            clazz.accept(classVisitor);
        }
    }
}
