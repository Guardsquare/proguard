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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;
import proguard.resources.file.ResourceFile;

/**
 * This class can add constant pool entries to a given class.
 *
 * @author Eric Lafortune
 */
public class ConstantPoolEditor
{
    private static final boolean DEBUG = false;

    private final ProgramClass    targetClass;
    private final ConstantVisitor constantReferenceInitializer;


    /**
     * Creates a new ConstantPoolEditor.
     * @param targetClass the target class in which constants are to be edited.
     */
    public ConstantPoolEditor(ProgramClass targetClass)
    {
        this(targetClass, null, null);
    }


    /**
     * Creates a new ConstantPoolEditor that automatically initializes class
     * references and class member references in new constants.
     * @param targetClass      the target class in which constants are to be
     *                         edited.
     * @param programClassPool the program class pool from which new constants
     *                         can be initialized.
     * @param libraryClassPool the library class pool from which new constants
     *                         can be initialized.
     */
    public ConstantPoolEditor(ProgramClass targetClass,
                              ClassPool    programClassPool,
                              ClassPool    libraryClassPool)
    {
        this.targetClass = targetClass;

        constantReferenceInitializer = programClassPool == null ? null :
            new WildcardConstantFilter(
            new ClassReferenceInitializer(programClassPool, libraryClassPool));
    }


    /**
     * Returns the target class in which constants are edited.
     */
    public ProgramClass getTargetClass()
    {
        return targetClass;
    }


    /**
     * Finds or creates a IntegerConstant constant pool entry with the given
     * value.
     * @return the constant pool index of the Utf8Constant.
     */
    public int addIntegerConstant(int value)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.INTEGER)
            {
                IntegerConstant integerConstant = (IntegerConstant)constant;
                if (integerConstant.getValue() == value)
                {
                    return index;
                }
            }
        }

        return addConstant(new IntegerConstant(value));
    }


    /**
     * Finds or creates a LongConstant constant pool entry with the given value.
     * @return the constant pool index of the LongConstant.
     */
    public int addLongConstant(long value)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.LONG)
            {
                LongConstant longConstant = (LongConstant)constant;
                if (longConstant.getValue() == value)
                {
                    return index;
                }
            }
        }

        return addConstant(new LongConstant(value));
    }


    /**
     * Finds or creates a FloatConstant constant pool entry with the given
     * value.
     * @return the constant pool index of the FloatConstant.
     */
    public int addFloatConstant(float value)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.FLOAT)
            {
                FloatConstant floatConstant = (FloatConstant)constant;
                if (floatConstant.getValue() == value)
                {
                    return index;
                }
            }
        }

        return addConstant(new FloatConstant(value));
    }


    /**
     * Finds or creates a DoubleConstant constant pool entry with the given
     * value.
     * @return the constant pool index of the DoubleConstant.
     */
    public int addDoubleConstant(double value)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.DOUBLE)
            {
                DoubleConstant doubleConstant = (DoubleConstant)constant;
                if (doubleConstant.getValue() == value)
                {
                    return index;
                }
            }
        }

        return addConstant(new DoubleConstant(value));
    }


    /**
     * Finds or creates a PrimitiveArrayConstant constant pool entry with the
     * given values.
     * @return the constant pool index of the PrimitiveArrayConstant.
     */
    public int addPrimitiveArrayConstant(Object values)
    {
        return addConstant(new PrimitiveArrayConstant(values));
    }


    /**
     * Finds or creates a StringConstant constant pool entry with the given
     * value.
     * @return the constant pool index of the StringConstant.
     */
    public int addStringConstant(String string,
                                 Clazz  referencedClass,
                                 Member referencedMember)
    {
        return addStringConstant(string, referencedClass, referencedMember, 0, null);
    }


    /**
     * Finds or creates a StringConstant constant pool entry with the given
     * value.
     * @return the constant pool index of the StringConstant.
     */
    public int addStringConstant(String       string,
                                 ResourceFile referencedResourceFile)
    {
        return addStringConstant(string, null, null, 0, referencedResourceFile);
    }


    /**
     * Finds or creates a StringConstant constant pool entry with the given
     * value.
     * @return the constant pool index of the StringConstant.
     */
    public int addStringConstant(String       string,
                                 Clazz        referencedClass,
                                 Member       referencedMember,
                                 int          resourceFileId,
                                 ResourceFile resourceFile)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.STRING)
            {
                StringConstant stringConstant = (StringConstant)constant;
                if (stringConstant.u2stringIndex < constantPoolCount          &&
                    stringConstant.getString(targetClass).equals(string)      &&
                    stringConstant.referencedClass        == referencedClass  &&
                    stringConstant.referencedMember       == referencedMember &&
                    stringConstant.referencedResourceId   == resourceFileId   &&
                    stringConstant.referencedResourceFile == resourceFile)
                {
                    return index;
                }
            }
        }

        return addConstant(new StringConstant(addUtf8Constant(string),
                                              referencedClass,
                                              referencedMember,
                                              resourceFileId,
                                              resourceFile));
    }


    /**
     * Finds or creates a InvokeDynamicConstant constant pool entry with the
     * given bootstrap method constant pool entry index, method name, and
     * descriptor.
     * @return the constant pool index of the InvokeDynamicConstant.
     */
    public int addInvokeDynamicConstant(int     bootstrapMethodIndex,
                                        String  name,
                                        String  descriptor,
                                        Clazz[] referencedClasses)
    {
        return addInvokeDynamicConstant(bootstrapMethodIndex,
                                        addNameAndTypeConstant(name, descriptor),
                                        referencedClasses);
    }


    /**
     * Finds or creates a DynamicConstant constant pool entry with the given
     * class constant pool entry index and name and type constant pool entry
     * index.
     * @return the constant pool index of the DynamicConstant.
     */
    public int addDynamicConstant(int     bootstrapMethodIndex,
                                  int     nameAndTypeIndex,
                                  Clazz[] referencedClasses)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.DYNAMIC)
            {
                DynamicConstant dynamicConstant = (DynamicConstant)constant;
                if (dynamicConstant.u2bootstrapMethodAttributeIndex == bootstrapMethodIndex &&
                    dynamicConstant.u2nameAndTypeIndex              == nameAndTypeIndex)
                {
                    return index;
                }
            }
        }

        return addConstant(new DynamicConstant(bootstrapMethodIndex,
                                               nameAndTypeIndex,
                                               referencedClasses));
    }


    /**
     * Finds or creates an InvokeDynamicConstant constant pool entry with the
     * given class constant pool entry index and name and type constant pool
     * entry index.
     * @return the constant pool index of the InvokeDynamicConstant.
     */
    public int addInvokeDynamicConstant(int     bootstrapMethodIndex,
                                        int     nameAndTypeIndex,
                                        Clazz[] referencedClasses)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.INVOKE_DYNAMIC)
            {
                InvokeDynamicConstant invokeDynamicConstant = (InvokeDynamicConstant)constant;
                if (invokeDynamicConstant.u2bootstrapMethodAttributeIndex == bootstrapMethodIndex &&
                    invokeDynamicConstant.u2nameAndTypeIndex              == nameAndTypeIndex)
                {
                    return index;
                }
            }
        }

        return addConstant(new InvokeDynamicConstant(bootstrapMethodIndex,
                                                     nameAndTypeIndex,
                                                     referencedClasses));
    }


    /**
     * Finds or creates a MethodHandleConstant constant pool entry of the
     * specified kind and with the given field ref, interface method ref,
     * or method ref constant pool entry index.
     * @return the constant pool index of the MethodHandleConstant.
     */
    public int addMethodHandleConstant(int referenceKind,
                                       int referenceIndex)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.METHOD_HANDLE)
            {
                MethodHandleConstant methodHandleConstant = (MethodHandleConstant)constant;
                if (methodHandleConstant.u1referenceKind  == referenceKind &&
                    methodHandleConstant.u2referenceIndex == referenceIndex)
                {
                    return index;
                }
            }
        }

        return addConstant(new MethodHandleConstant(referenceKind,
                                                    referenceIndex));
    }

    /**
     * Finds or creates a ModuleConstant constant pool entry with the given name.
     * @return the constant pool index of the ModuleConstant.
     */
    public int addModuleConstant(String name)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.MODULE)
            {
                ModuleConstant moduleConstant = (ModuleConstant)constant;
                if (moduleConstant.getName(targetClass).equals(name))
                {
                    return index;
                }
            }
        }

        int nameIndex = addUtf8Constant(name);

        return addConstant(new ModuleConstant(nameIndex));
    }

    /**
     * Finds or creates a PackageConstant constant pool entry with the given name.
     * @return the constant pool index of the PackageConstant.
     */
    public int addPackageConstant(String name)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.PACKAGE)
            {
                PackageConstant packageConstant = (PackageConstant)constant;
                if (packageConstant.getName(targetClass).equals(name))
                {
                    return index;
                }
            }
        }

        int nameIndex = addUtf8Constant(name);

        return addConstant(new PackageConstant(nameIndex));
    }


    /**
     * Finds or creates a FieldrefConstant constant pool entry for the given
     * class and field.
     * @return the constant pool index of the FieldrefConstant.
     */
    public int addFieldrefConstant(Clazz referencedClass,
                                   Field referencedField)
    {
        return addFieldrefConstant(referencedClass.getName(),
                                   referencedField.getName(referencedClass),
                                   referencedField.getDescriptor(referencedClass),
                                   referencedClass,
                                   referencedField);
    }


    /**
     * Finds or creates a FieldrefConstant constant pool entry with the given
     * class name, field name, and descriptor.
     * @return the constant pool index of the FieldrefConstant.
     */
    public int addFieldrefConstant(String className,
                                   String name,
                                   String descriptor,
                                   Clazz  referencedClass,
                                   Field  referencedField)
    {
        return addFieldrefConstant(className,
                                   addNameAndTypeConstant(name, descriptor),
                                   referencedClass,
                                   referencedField);
    }


    /**
     * Finds or creates a FieldrefConstant constant pool entry with the given
     * class name, field name, and descriptor.
     * @return the constant pool index of the FieldrefConstant.
     */
    public int addFieldrefConstant(String className,
                                   int    nameAndTypeIndex,
                                   Clazz  referencedClass,
                                   Field  referencedField)
    {
        return addFieldrefConstant(addClassConstant(className, referencedClass),
                                   nameAndTypeIndex,
                                   referencedClass,
                                   referencedField);
    }


    /**
     * Finds or creates a FieldrefConstant constant pool entry with the given
     * class constant pool entry index, field name, and descriptor.
     * @return the constant pool index of the FieldrefConstant.
     */
    public int addFieldrefConstant(int    classIndex,
                                   String name,
                                   String descriptor,
                                   Clazz  referencedClass,
                                   Field  referencedField)
    {
        return addFieldrefConstant(classIndex,
                                   addNameAndTypeConstant(name, descriptor),
                                   referencedClass,
                                   referencedField);
    }


    /**
     * Finds or creates a FieldrefConstant constant pool entry with the given
     * class constant pool entry index and name and type constant pool entry
     * index.
     * @return the constant pool index of the FieldrefConstant.
     */
    public int addFieldrefConstant(int   classIndex,
                                   int   nameAndTypeIndex,
                                   Clazz referencedClass,
                                   Field referencedField)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.FIELDREF)
            {
                FieldrefConstant fieldrefConstant = (FieldrefConstant)constant;
                if (fieldrefConstant.u2classIndex       == classIndex &&
                    fieldrefConstant.u2nameAndTypeIndex == nameAndTypeIndex)
                {
                    return index;
                }
            }
        }

        return addConstant(new FieldrefConstant(classIndex,
                                                nameAndTypeIndex,
                                                referencedClass,
                                                referencedField));
    }


    /**
     * Finds or creates a InterfaceMethodrefConstant constant pool entry with the
     * given class name, method name, and descriptor.
     * @return the constant pool index of the InterfaceMethodrefConstant.
     */
    public int addInterfaceMethodrefConstant(String className,
                                             String name,
                                             String descriptor,
                                             Clazz  referencedClass,
                                             Method referencedMethod)
    {
        return addInterfaceMethodrefConstant(className,
                                             addNameAndTypeConstant(name, descriptor),
                                             referencedClass,
                                             referencedMethod);
    }


    /**
     * Finds or creates a InterfaceMethodrefConstant constant pool entry with the
     * given class name, method name, and descriptor.
     * @return the constant pool index of the InterfaceMethodrefConstant.
     */
    public int addInterfaceMethodrefConstant(String className,
                                             int    nameAndTypeIndex,
                                             Clazz  referencedClass,
                                             Method referencedMethod)
    {
        return addInterfaceMethodrefConstant(addClassConstant(className, referencedClass),
                                             nameAndTypeIndex,
                                             referencedClass,
                                             referencedMethod);
    }


    /**
     * Finds or creates a InterfaceMethodrefConstant constant pool entry for the
     * given class and method.
     * @return the constant pool index of the InterfaceMethodrefConstant.
     */
    public int addInterfaceMethodrefConstant(Clazz  referencedClass,
                                             Method referencedMethod)
    {
        return addInterfaceMethodrefConstant(referencedClass.getName(),
                                             referencedMethod.getName(referencedClass),
                                             referencedMethod.getDescriptor(referencedClass),
                                             referencedClass,
                                             referencedMethod);
    }


    /**
     * Finds or creates a InterfaceMethodrefConstant constant pool entry with the
     * given class constant pool entry index, method name, and descriptor.
     * @return the constant pool index of the InterfaceMethodrefConstant.
     */
    public int addInterfaceMethodrefConstant(int    classIndex,
                                             String name,
                                             String descriptor,
                                             Clazz  referencedClass,
                                             Method referencedMethod)
    {
        return addInterfaceMethodrefConstant(classIndex,
                                             addNameAndTypeConstant(name, descriptor),
                                             referencedClass,
                                             referencedMethod);
    }


    /**
     * Finds or creates a InterfaceMethodrefConstant constant pool entry with the
     * given class constant pool entry index and name and type constant pool
     * entry index.
     * @return the constant pool index of the InterfaceMethodrefConstant.
     */
    public int addInterfaceMethodrefConstant(int    classIndex,
                                             int    nameAndTypeIndex,
                                             Clazz  referencedClass,
                                             Method referencedMethod)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.INTERFACE_METHODREF)
            {
                InterfaceMethodrefConstant methodrefConstant = (InterfaceMethodrefConstant)constant;
                if (methodrefConstant.u2classIndex       == classIndex &&
                    methodrefConstant.u2nameAndTypeIndex == nameAndTypeIndex)
                {
                    return index;
                }
            }
        }

        return addConstant(new InterfaceMethodrefConstant(classIndex,
                                                          nameAndTypeIndex,
                                                          referencedClass,
                                                          referencedMethod));
    }


    /**
     * Finds or creates a MethodrefConstant constant pool entry for the given
     * class and method.
     * @return the constant pool index of the MethodrefConstant.
     */
    public int addMethodrefConstant(Clazz  referencedClass,
                                    Method referencedMethod)
    {
        return addMethodrefConstant(referencedClass.getName(),
                                    referencedMethod.getName(referencedClass),
                                    referencedMethod.getDescriptor(referencedClass),
                                    referencedClass,
                                    referencedMethod);
    }


    /**
     * Finds or creates a MethodrefConstant constant pool entry with the given
     * class name, method name, and descriptor.
     * @return the constant pool index of the MethodrefConstant.
     */
    public int addMethodrefConstant(String className,
                                    String name,
                                    String descriptor,
                                    Clazz  referencedClass,
                                    Method referencedMethod)
    {
        return addMethodrefConstant(className,
                                    addNameAndTypeConstant(name, descriptor),
                                    referencedClass,
                                    referencedMethod);
    }


    /**
     * Finds or creates a MethodrefConstant constant pool entry with the given
     * class name, method name, and descriptor.
     * @return the constant pool index of the MethodrefConstant.
     */
    public int addMethodrefConstant(String className,
                                    int    nameAndTypeIndex,
                                    Clazz  referencedClass,
                                    Method referencedMethod)
    {
        return addMethodrefConstant(addClassConstant(className, referencedClass),
                                    nameAndTypeIndex,
                                    referencedClass,
                                    referencedMethod);
    }


    /**
     * Finds or creates a MethodrefConstant constant pool entry with the given
     * class constant pool entry index, method name, and descriptor.
     * @return the constant pool index of the MethodrefConstant.
     */
    public int addMethodrefConstant(int    classIndex,
                                    String name,
                                    String descriptor,
                                    Clazz  referencedClass,
                                    Method referencedMethod)
    {
        return addMethodrefConstant(classIndex,
                                    addNameAndTypeConstant(name, descriptor),
                                    referencedClass,
                                    referencedMethod);
    }


    /**
     * Finds or creates a MethodrefConstant constant pool entry with the given
     * class constant pool entry index and name and type constant pool entry
     * index.
     * @return the constant pool index of the MethodrefConstant.
     */
    public int addMethodrefConstant(int    classIndex,
                                    int    nameAndTypeIndex,
                                    Clazz  referencedClass,
                                    Method referencedMethod)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.METHODREF)
            {
                MethodrefConstant methodrefConstant = (MethodrefConstant)constant;
                if (methodrefConstant.u2classIndex       == classIndex &&
                    methodrefConstant.u2nameAndTypeIndex == nameAndTypeIndex)
                {
                    return index;
                }
            }
        }

        return addConstant(new MethodrefConstant(classIndex,
                                                 nameAndTypeIndex,
                                                 referencedClass,
                                                 referencedMethod));
    }


    /**
     * Finds or creates a ClassConstant constant pool entry for the given class.
     * @return the constant pool index of the ClassConstant.
     */
    public int addClassConstant(Clazz referencedClass)
    {
        return addClassConstant(referencedClass.getName(),
                                referencedClass);
    }


    /**
     * Finds or creates a ClassConstant constant pool entry with the given name.
     * @return the constant pool index of the ClassConstant.
     */
    public int addClassConstant(String name,
                                Clazz  referencedClass)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.CLASS)
            {
                ClassConstant classConstant = (ClassConstant)constant;
                if (classConstant.u2nameIndex < constantPoolCount &&
                    classConstant.getName(targetClass).equals(name))
                {
                    return index;
                }
            }
        }

        int nameIndex = addUtf8Constant(name);

        return addConstant(new ClassConstant(nameIndex, referencedClass));
    }


    /**
     * Finds or creates a MethodTypeConstant constant pool entry with the given
     * type.
     * @return the constant pool index of the MethodTypeConstant.
     */
    public int addMethodTypeConstant(String  type,
                                     Clazz[] referencedClasses)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.METHOD_TYPE)
            {
                MethodTypeConstant methodTypeConstant = (MethodTypeConstant)constant;
                if (methodTypeConstant.u2descriptorIndex < constantPoolCount &&
                    methodTypeConstant.getType(targetClass).equals(type))
                {
                    return index;
                }
            }
        }

        return addConstant(new MethodTypeConstant(addUtf8Constant(type),
                                                  referencedClasses));
    }


    /**
     * Finds or creates a NameAndTypeConstant constant pool entry with the given
     * name and type.
     * @return the constant pool index of the NameAndTypeConstant.
     */
    public int addNameAndTypeConstant(String name,
                                      String type)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.NAME_AND_TYPE)
            {
                NameAndTypeConstant nameAndTypeConstant = (NameAndTypeConstant)constant;
                if (nameAndTypeConstant.u2nameIndex       < constantPoolCount &&
                    nameAndTypeConstant.u2descriptorIndex < constantPoolCount &&
                    nameAndTypeConstant.getName(targetClass).equals(name)     &&
                    nameAndTypeConstant.getType(targetClass).equals(type))
                {
                    return index;
                }
            }
        }

        return addConstant(new NameAndTypeConstant(addUtf8Constant(name),
                                                   addUtf8Constant(type)));
    }


    /**
     * Finds or creates a Utf8Constant constant pool entry for the given string.
     * @return the constant pool index of the Utf8Constant.
     */
    public int addUtf8Constant(String string)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Check if the entry already exists.
        for (int index = 1; index < constantPoolCount; index++)
        {
            Constant constant = constantPool[index];

            if (constant != null &&
                constant.getTag() == Constant.UTF8)
            {
                Utf8Constant utf8Constant = (Utf8Constant)constant;
                if (utf8Constant.getString().equals(string))
                {
                    return index;
                }
            }
        }

        return addConstant(new Utf8Constant(string));
    }


    /**
     * Adds a given constant pool entry to the end of the constant pool.
     * @return the constant pool index for the added entry.
     */
    public int addConstant(Constant constant)
    {
        int        constantPoolCount = targetClass.u2constantPoolCount;
        Constant[] constantPool      = targetClass.constantPool;

        // Make sure there is enough space for another constant pool entry.
        if (constantPool.length < constantPoolCount+2)
        {
            Constant[] newConstantPool = new Constant[constantPoolCount+2];
            System.arraycopy(constantPool, 0,
                             newConstantPool, 0,
                             constantPoolCount);

            // Assign the newly created constant pool after all entries
            // have been copied to avoid race-conditions.
            targetClass.constantPool = newConstantPool;
            constantPool             = targetClass.constantPool;
        }

        if (DEBUG)
        {
            System.out.println("ConstantPoolEditor: ["+(targetClass.u2thisClass > 0 ? targetClass.getName() : "(dummy)")+"] adding ["+constant.getClass().getName()+"] at index "+targetClass.u2constantPoolCount);
        }

        // Add the new entry to the end of the constant pool.
        constantPool[targetClass.u2constantPoolCount++] = constant;

        // Long constants and double constants take up two entries in the
        // constant pool.
        int tag = constant.getTag();
        if (tag == Constant.LONG ||
            tag == Constant.DOUBLE)
        {
            constantPool[targetClass.u2constantPoolCount++] = null;
        }

        // Initialize the class references and class member references in the
        // constant, if necessary.
        if (constantReferenceInitializer != null)
        {
            constant.accept(targetClass, constantReferenceInitializer);
        }

        return constantPoolCount;
    }
}
