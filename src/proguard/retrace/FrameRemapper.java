/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2015 Eric Lafortune @ GuardSquare
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
package proguard.retrace;

import proguard.obfuscate.MappingProcessor;

import java.util.*;

/**
 * This class accumulates mapping information and then transforms stack frames
 * accordingly.
 *
 * @author Eric Lafortune
 */
public class FrameRemapper implements MappingProcessor
{
    private final Map classMap       = new HashMap();
    private final Map classFieldMap  = new HashMap();
    private final Map classMethodMap = new HashMap();


    /**
     * Transforms the given obfuscated frame back to one or more original frames.
     */
    public List transform(FrameInfo obfuscatedFrame)
    {
        // First remap the class name.
        String originalClassName = originalClassName(obfuscatedFrame.getClassName());
        if (originalClassName == null)
        {
            return null;
        }

        List originalFrames = new ArrayList();

        // Create any transformed frames with remapped field names.
        transformFieldInfo(obfuscatedFrame,
                           originalClassName,
                           originalFrames);

        // Create any transformed frames with remapped method names.
        transformMethodInfo(obfuscatedFrame,
                            originalClassName,
                            originalFrames);

        if (originalFrames.isEmpty())
        {
            // Create a transformed frame with the remapped class name.
            originalFrames.add(new FrameInfo(originalClassName,
                                             sourceFileName(originalClassName),
                                             obfuscatedFrame.getLineNumber(),
                                             obfuscatedFrame.getType(),
                                             obfuscatedFrame.getFieldName(),
                                             obfuscatedFrame.getMethodName(),
                                             obfuscatedFrame.getArguments()));
        }

        return originalFrames;
    }


    /**
     * Transforms the obfuscated frame into one or more original frames,
     * if the frame contains information about a field that can be remapped.
     * @param obfuscatedFrame     the obfuscated frame.
     * @param originalFieldFrames the list in which remapped frames can be
     *                            collected.
     */
    private void transformFieldInfo(FrameInfo obfuscatedFrame,
                                    String    originalClassName,
                                    List      originalFieldFrames)
    {
        // Class name -> obfuscated field names.
        Map fieldMap = (Map)classFieldMap.get(originalClassName);
        if (fieldMap != null)
        {
            // Obfuscated field names -> fields.
            String obfuscatedFieldName = obfuscatedFrame.getFieldName();
            List fieldList = (List)fieldMap.get(obfuscatedFieldName);
            if (fieldList != null)
            {
                String obfuscatedType = obfuscatedFrame.getType();
                String originalType   = obfuscatedType == null ? null :
                    originalType(obfuscatedType);

                // Find all matching fields.
                Iterator fieldInfoIterator = fieldList.iterator();
                while (fieldInfoIterator.hasNext())
                {
                    FieldInfo fieldInfo = (FieldInfo)fieldInfoIterator.next();
                    if (fieldInfo.matches(originalType))
                    {
                        originalFieldFrames.add(new FrameInfo(fieldInfo.originalClassName,
                                                              sourceFileName(fieldInfo.originalClassName),
                                                              obfuscatedFrame.getLineNumber(),
                                                              fieldInfo.originalType,
                                                              fieldInfo.originalName,
                                                              obfuscatedFrame.getMethodName(),
                                                              obfuscatedFrame.getArguments()));
                    }
                }
            }
        }
    }


    /**
     * Transforms the obfuscated frame into one or more original frames,
     * if the frame contains information about a method that can be remapped.
     * @param obfuscatedFrame      the obfuscated frame.
     * @param originalMethodFrames the list in which remapped frames can be
     *                             collected.
     */
    private void transformMethodInfo(FrameInfo obfuscatedFrame,
                                     String    originalClassName,
                                     List      originalMethodFrames)
    {
        // Class name -> obfuscated method names.
        Map methodMap = (Map)classMethodMap.get(originalClassName);
        if (methodMap != null)
        {
            // Obfuscated method names -> methods.
            String obfuscatedMethodName = obfuscatedFrame.getMethodName();
            Set methodList = (Set)methodMap.get(obfuscatedMethodName);
            if (methodList != null)
            {
                int obfuscatedLineNumber = obfuscatedFrame.getLineNumber();

                String obfuscatedType = obfuscatedFrame.getType();
                String originalType   = obfuscatedType == null ? null :
                    originalType(obfuscatedType);

                String obfuscatedArguments = obfuscatedFrame.getArguments();
                String originalArguments   = obfuscatedArguments == null ? null :
                    originalArguments(obfuscatedArguments);

                // Find all matching methods.
                Iterator methodInfoIterator = methodList.iterator();
                while (methodInfoIterator.hasNext())
                {
                    MethodInfo methodInfo = (MethodInfo)methodInfoIterator.next();
                    if (methodInfo.matches(obfuscatedLineNumber,
                                           originalType,
                                           originalArguments))
                    {
                        // Do we have a different original first line number?
                        // We're allowing unknown values, represented as 0.
                        int lineNumber = obfuscatedFrame.getLineNumber();
                        if (methodInfo.originalFirstLineNumber != methodInfo.obfuscatedFirstLineNumber)
                        {
                            // Do we have an original line number range and
                            // sufficient information to shift the line number?
                            lineNumber = methodInfo.originalLastLineNumber    != 0                                  &&
                                         methodInfo.originalLastLineNumber    != methodInfo.originalFirstLineNumber &&
                                         methodInfo.obfuscatedFirstLineNumber != 0                                  &&
                                         lineNumber                           != 0 ?
                                methodInfo.originalFirstLineNumber - methodInfo.obfuscatedFirstLineNumber + lineNumber :
                                methodInfo.originalFirstLineNumber;
                        }

                        originalMethodFrames.add(new FrameInfo(methodInfo.originalClassName,
                                                               sourceFileName(methodInfo.originalClassName),
                                                               lineNumber,
                                                               methodInfo.originalType,
                                                               obfuscatedFrame.getFieldName(),
                                                               methodInfo.originalName,
                                                               methodInfo.originalArguments));
                    }
                }
            }
        }
    }


    /**
     * Returns the original argument types.
     */
    private String originalArguments(String obfuscatedArguments)
    {
        StringBuffer originalArguments = new StringBuffer();

        int startIndex = 0;
        while (true)
        {
            int endIndex = obfuscatedArguments.indexOf(',', startIndex);
            if (endIndex < 0)
            {
                break;
            }

            originalArguments.append(originalType(obfuscatedArguments.substring(startIndex, endIndex).trim())).append(',');

            startIndex = endIndex + 1;
        }

        originalArguments.append(originalType(obfuscatedArguments.substring(startIndex).trim()));

        return originalArguments.toString();
    }


    /**
     * Returns the original type.
     */
    private String originalType(String obfuscatedType)
    {
        int index = obfuscatedType.indexOf('[');

        return index >= 0 ?
            originalClassName(obfuscatedType.substring(0, index)) + obfuscatedType.substring(index) :
            originalClassName(obfuscatedType);
    }


    /**
     * Returns the original class name.
     */
    private String originalClassName(String obfuscatedClassName)
    {
        String originalClassName = (String)classMap.get(obfuscatedClassName);

        return originalClassName != null ?
            originalClassName :
            obfuscatedClassName;
    }


    /**
     * Returns the Java source file name that typically corresponds to the
     * given class name.
     */
    private String sourceFileName(String className)
    {
        int index1 = className.lastIndexOf('.') + 1;
        int index2 = className.indexOf('$', index1);

        return (index2 > 0 ?
            className.substring(index1, index2) :
            className.substring(index1)) +
            ".java";
    }


    // Implementations for MappingProcessor.

    public boolean processClassMapping(String className,
                                       String newClassName)
    {
        // Obfuscated class name -> original class name.
        classMap.put(newClassName, className);

        return true;
    }


    public void processFieldMapping(String className,
                                    String fieldType,
                                    String fieldName,
                                    String newClassName,
                                    String newFieldName)
    {
        // Obfuscated class name -> obfuscated field names.
        Map fieldMap = (Map)classFieldMap.get(newClassName);
        if (fieldMap == null)
        {
            fieldMap = new HashMap();
            classFieldMap.put(newClassName, fieldMap);
        }

        // Obfuscated field name -> fields.
        Set fieldList = (Set)fieldMap.get(newFieldName);
        if (fieldList == null)
        {
            fieldList = new LinkedHashSet();
            fieldMap.put(newFieldName, fieldList);
        }

        // Add the field information.
        fieldList.add(new FieldInfo(className,
                                    fieldType,
                                    fieldName));
    }


    public void processMethodMapping(String className,
                                     int    firstLineNumber,
                                     int    lastLineNumber,
                                     String methodReturnType,
                                     String methodName,
                                     String methodArguments,
                                     String newClassName,
                                     int    newFirstLineNumber,
                                     int    newLastLineNumber,
                                     String newMethodName)
    {
        // Original class name -> obfuscated method names.
        Map methodMap = (Map)classMethodMap.get(newClassName);
        if (methodMap == null)
        {
            methodMap = new HashMap();
            classMethodMap.put(newClassName, methodMap);
        }

        // Obfuscated method name -> methods.
        Set methodList = (Set)methodMap.get(newMethodName);
        if (methodList == null)
        {
            methodList = new LinkedHashSet();
            methodMap.put(newMethodName, methodList);
        }

        // Add the method information.
        methodList.add(new MethodInfo(newFirstLineNumber,
                                      newLastLineNumber,
                                      className,
                                      firstLineNumber,
                                      lastLineNumber,
                                      methodReturnType,
                                      methodName,
                                      methodArguments));
    }


    /**
     * Information about the original version and the obfuscated version of
     * a field (without the obfuscated class name or field name).
     */
    private static class FieldInfo
    {
        private final String originalClassName;
        private final String originalType;
        private final String originalName;


        /**
         * Creates a new FieldInfo with the given properties.
         */
        private FieldInfo(String originalClassName,
                          String originalType,
                          String originalName)
        {
            this.originalClassName = originalClassName;
            this.originalType      = originalType;
            this.originalName      = originalName;
        }


        /**
         * Returns whether the given type matches the original type of this field.
         * The given type may be a null wildcard.
         */
        private boolean matches(String originalType)
        {
            return
                originalType == null || originalType.equals(this.originalType);
        }
    }


    /**
     * Information about the original version and the obfuscated version of
     * a method (without the obfuscated class name or method name).
     */
    private static class MethodInfo
    {
        private final int    obfuscatedFirstLineNumber;
        private final int    obfuscatedLastLineNumber;
        private final String originalClassName;
        private final int    originalFirstLineNumber;
        private final int    originalLastLineNumber;
        private final String originalType;
        private final String originalName;
        private final String originalArguments;


        /**
         * Creates a new MethodInfo with the given properties.
         */
        private MethodInfo(int    obfuscatedFirstLineNumber,
                           int    obfuscatedLastLineNumber,
                           String originalClassName,
                           int    originalFirstLineNumber,
                           int    originalLastLineNumber,
                           String originalType,
                           String originalName,
                           String originalArguments)
        {
            this.obfuscatedFirstLineNumber = obfuscatedFirstLineNumber;
            this.obfuscatedLastLineNumber  = obfuscatedLastLineNumber;
            this.originalType              = originalType;
            this.originalArguments         = originalArguments;
            this.originalClassName         = originalClassName;
            this.originalName              = originalName;
            this.originalFirstLineNumber   = originalFirstLineNumber;
            this.originalLastLineNumber    = originalLastLineNumber;
        }


        /**
         * Returns whether the given properties match the properties of this
         * method. The given properties may be null wildcards.
         */
        private boolean matches(int    obfuscatedLineNumber,
                                String originalType,
                                String originalArguments)
        {
            return
                (obfuscatedLineNumber == 0 ? obfuscatedLastLineNumber == 0 :
                     obfuscatedFirstLineNumber <= obfuscatedLineNumber && obfuscatedLineNumber <= obfuscatedLastLineNumber) &&
                (originalType         == null || originalType.equals(this.originalType))                                    &&
                (originalArguments    == null || originalArguments.equals(this.originalArguments));
        }
    }
}
