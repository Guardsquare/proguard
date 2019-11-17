/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.io;

import proguard.classfile.*;
import proguard.util.MultiValueMap;

import java.util.*;

import static proguard.classfile.util.ClassUtil.internalClassName;


/**
 * Map keeping track of all data entries that have associated extra data entries with them.
 *
 * It also supports extra data entries that are not attached to a particular data entry: these entries should always
 * be included in the output, independent of the contents of the output.
 *
 * @author Johan Leys
 */
public class ExtraDataEntryNameMap
{
    private final MultiValueMap<String, String> nameMap = new MultiValueMap<String, String>();


    /**
     * Clears all extra data entries.
     */
    public void clear()
    {
        nameMap.clear();
    }


    /**
     * Clears all extra class data entries.
     */
    public void clearExtraClasses()
    {
        for (String dataEntryKey : nameMap.keySet())
        {
            for (String dataEntryValue : new ArrayList<String>(nameMap.get(dataEntryKey)))
            {
                if (dataEntryValue.endsWith(ClassConstants.CLASS_FILE_EXTENSION))
                {
                    nameMap.remove(dataEntryKey, dataEntryValue);
                }
            }
        }
    }


    /**
     * Adds an extra data entry that is not linked to a particular data entry.
     */
    public void addExtraDataEntry(String extraDataEntryName)
    {
        nameMap.put(null, extraDataEntryName);
    }


    /**
     * Adds an extra class data entry that is not linked to a particular data entry.
     */
    public void addExtraClass(String extraDataEntryName)
    {
        addExtraDataEntry(getClassDataEntryName(extraDataEntryName));
    }


    /**
     * Adds an extra data entry to the given data entry.
     */
    public void addExtraDataEntry(String keyDataEntryName,
                                  String extraDataEntryName)
    {
        nameMap.put(keyDataEntryName, extraDataEntryName);
    }


    /**
     * Registers an extra data entry to the given class data entry.
     */
    public void addExtraDataEntryToClass(String keyClassName,
                                         String extraDataEntryName)
    {
        addExtraDataEntry(getClassDataEntryName(keyClassName),
                          extraDataEntryName);
    }


    /**
     * Registers an extra class data entry to the given class data entry.
     */
    public void addExtraClassToClass(Clazz keyClass,
                                     Clazz extraClass)
    {
        addExtraDataEntry(getClassDataEntryName(keyClass),
                          getClassDataEntryName(extraClass));
    }


    /**
     * Registers an extra class data entry to the given class data entry.
     */
    public void addExtraClassToClass(Clazz keyClass,
                                     Class extraClass)
    {
        addExtraDataEntry(getClassDataEntryName(keyClass),
                          getClassDataEntryName(extraClass));
    }


    /**
     * Registers an extra class data entry to the given class data entry.
     */
    public void addExtraClassToClass(Clazz  keyClass,
                                     String extraClassName)
    {
        addExtraDataEntry(getClassDataEntryName(keyClass),
                          getClassDataEntryName(extraClassName));
    }


    /**
     * Registers an extra class data entry to the given class data entry.
     */
    public void addExtraClassToClass(String keyClassName,
                                     Class  extraClass)
    {
        addExtraDataEntry(getClassDataEntryName(keyClassName),
                          getClassDataEntryName(extraClass));
    }


    /**
     * Registers an extra class data entry to the given class data entry.
     */
    public void addExtraClassToClass(String keyClassName,
                                     String extraClassName)
    {
        addExtraDataEntry(getClassDataEntryName(keyClassName),
                          getClassDataEntryName(extraClassName));
    }


    /**
     * Returns the names of all data entries that have extra data entries attached to them.
     */
    public Set<String> getKeyDataEntryNames()
    {
        return nameMap.keySet();
    }


    /**
     * Returns the names of all extra data entries.
     */
    public Set<String> getAllExtraDataEntryNames()
    {
        return nameMap.getValues();
    }


    /**
     * Returns the names of all extra data entries that are not attached to a particular data entry.
     */
    public Set<String> getDefaultExtraDataEntryNames()
    {
        return nameMap.get(null);
    }


    /**
     * Returns the names of all extra data entries attached to the given data entry.
     */
    public Set<String> getExtraDataEntryNames(String keyDataEntryName)
    {
        return nameMap.get(keyDataEntryName);
    }


    // Small utility methods.

    private String getClassDataEntryName(Clazz clazz)
    {
        return clazz.getName() + ClassConstants.CLASS_FILE_EXTENSION;
    }


    private String getClassDataEntryName(Class clazz)
    {
        return internalClassName(clazz.getName()) + ClassConstants.CLASS_FILE_EXTENSION;
    }


    public String getClassDataEntryName(String className)
    {
        return className + ClassConstants.CLASS_FILE_EXTENSION;
    }
}
