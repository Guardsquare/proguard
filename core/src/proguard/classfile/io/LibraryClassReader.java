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
package proguard.classfile.io;

import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.RuntimeDataInput;

import java.io.DataInput;

/**
 * This {@link ClassVisitor} fills out the {@link LibraryClass} instances that it visits with data
 * from the given {@link DataInput} object.
 *
 * @author Eric Lafortune
 */
public class LibraryClassReader
extends      SimplifiedVisitor
implements   ClassVisitor,
             MemberVisitor,
             ConstantVisitor
{
    private static final LibraryField[]  EMPTY_LIBRARY_FIELDS  = new LibraryField[0];
    private static final LibraryMethod[] EMPTY_LIBRARY_METHODS = new LibraryMethod[0];


    private final RuntimeDataInput dataInput;
    private final boolean          skipNonPublicClasses;
    private final boolean          skipNonPublicClassMembers;

    // A global array that acts as a parameter for the visitor methods.
    private Constant[]      constantPool;


    /**
     * Creates a new ProgramClassReader for reading from the given DataInput.
     */
    public LibraryClassReader(DataInput dataInput,
                              boolean   skipNonPublicClasses,
                              boolean   skipNonPublicClassMembers)
    {
        this.dataInput                 = new RuntimeDataInput(dataInput);
        this.skipNonPublicClasses      = skipNonPublicClasses;
        this.skipNonPublicClassMembers = skipNonPublicClassMembers;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass libraryClass)
    {
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Read and check the magic number.
        int u4magic = dataInput.readInt();

        ClassUtil.checkMagicNumber(u4magic);

        // Read and check the version numbers.
        int u2minorVersion = dataInput.readUnsignedShort();
        int u2majorVersion = dataInput.readUnsignedShort();

        int u4version = ClassUtil.internalClassVersion(u2majorVersion,
                                                       u2minorVersion);

        ClassUtil.checkVersionNumbers(u4version);

        // Read the constant pool. Note that the first entry is not used.
        int u2constantPoolCount = dataInput.readUnsignedShort();

        // Create the constant pool array.
        constantPool = new Constant[u2constantPoolCount];

        for (int index = 1; index < u2constantPoolCount; index++)
        {
            Constant constant = createConstant();
            constant.accept(libraryClass, this);

            int tag = constant.getTag();
            if (tag == Constant.CLASS ||
                tag == Constant.UTF8)
            {
                constantPool[index] = constant;
            }

            // Long constants and double constants take up two entries in the
            // constant pool.
            if (tag == Constant.LONG ||
                tag == Constant.DOUBLE)
            {
                index++;
            }
        }

        // Read the general class information.
        libraryClass.u2accessFlags = dataInput.readUnsignedShort();

        // We may stop parsing this library class if it's not public anyway.
        // E.g. only about 60% of all rt.jar classes need to be parsed.
        if (skipNonPublicClasses &&
            AccessUtil.accessLevel(libraryClass.getAccessFlags()) < AccessUtil.PUBLIC)
        {
            return;
        }

        // Read the class and super class indices.
        int u2thisClass  = dataInput.readUnsignedShort();
        int u2superClass = dataInput.readUnsignedShort();

        // Store their actual names.
        libraryClass.thisClassName  = getClassName(u2thisClass);
        libraryClass.superClassName = (u2superClass == 0) ? null :
                                      getClassName(u2superClass);

        // Read the interfaces
        int u2interfacesCount = dataInput.readUnsignedShort();

        libraryClass.interfaceNames = new String[u2interfacesCount];
        for (int index = 0; index < u2interfacesCount; index++)
        {
            // Store the actual interface name.
            int u2interface = dataInput.readUnsignedShort();
            libraryClass.interfaceNames[index] = getClassName(u2interface);
        }

        // Read the fields.
        int u2fieldsCount = dataInput.readUnsignedShort();

        // Create the fields array.
        LibraryField[] reusableFields = new LibraryField[u2fieldsCount];

        int visibleFieldsCount = 0;
        for (int index = 0; index < u2fieldsCount; index++)
        {
            LibraryField field = new LibraryField();
            this.visitLibraryMember(libraryClass, field);

            // Only store fields that are visible.
            if (AccessUtil.accessLevel(field.getAccessFlags()) >=
                (skipNonPublicClassMembers ? AccessUtil.PROTECTED :
                                             AccessUtil.PACKAGE_VISIBLE))
            {
                reusableFields[visibleFieldsCount++] = field;
            }
        }

        // Copy the visible fields (if any) into a fields array of the right size.
        if (visibleFieldsCount == 0)
        {
            libraryClass.fields = EMPTY_LIBRARY_FIELDS;
        }
        else
        {
            libraryClass.fields = new LibraryField[visibleFieldsCount];
            System.arraycopy(reusableFields, 0, libraryClass.fields, 0, visibleFieldsCount);
        }

        // Read the methods.
        int u2methodsCount = dataInput.readUnsignedShort();

        // Create the methods array.
        LibraryMethod[] reusableMethods = new LibraryMethod[u2methodsCount];

        int visibleMethodsCount = 0;
        for (int index = 0; index < u2methodsCount; index++)
        {
            LibraryMethod method = new LibraryMethod();
            this.visitLibraryMember(libraryClass, method);

            // Only store methods that are visible.
            if (AccessUtil.accessLevel(method.getAccessFlags()) >=
                (skipNonPublicClassMembers ? AccessUtil.PROTECTED :
                                             AccessUtil.PACKAGE_VISIBLE))
            {
                reusableMethods[visibleMethodsCount++] = method;
            }
        }

        // Copy the visible methods (if any) into a methods array of the right size.
        if (visibleMethodsCount == 0)
        {
            libraryClass.methods = EMPTY_LIBRARY_METHODS;
        }
        else
        {
            libraryClass.methods = new LibraryMethod[visibleMethodsCount];
            System.arraycopy(reusableMethods, 0, libraryClass.methods, 0, visibleMethodsCount);
        }

        // Skip the class attributes.
        skipAttributes();
    }


    // Implementations for MemberVisitor.

    public void visitProgramMember(ProgramClass libraryClass, ProgramMember libraryMember)
    {
    }


    public void visitLibraryMember(LibraryClass libraryClass, LibraryMember libraryMember)
    {
        // Read the general field information.
        libraryMember.u2accessFlags = dataInput.readUnsignedShort();
        libraryMember.name          = getString(dataInput.readUnsignedShort());
        libraryMember.descriptor    = getString(dataInput.readUnsignedShort());

        // Skip the field attributes.
        skipAttributes();
    }


    // Implementations for ConstantVisitor.

    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant)
    {
        dataInput.skipBytes(4);
    }


    public void visitLongConstant(Clazz clazz, LongConstant longConstant)
    {
        dataInput.skipBytes(8);
    }


    public void visitFloatConstant(Clazz clazz, FloatConstant floatConstant)
    {
        dataInput.skipBytes(4);
    }


    public void visitDoubleConstant(Clazz clazz, DoubleConstant doubleConstant)
    {
        dataInput.skipBytes(8);
    }


    public void visitPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant)
    {
        char u2primitiveType = dataInput.readChar();
        int  u4length        = dataInput.readInt();

        dataInput.skipBytes(primitiveSize(u2primitiveType) * u4length);
    }


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        dataInput.skipBytes(2);
    }


    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        int u2length = dataInput.readUnsignedShort();

        // Read the UTF-8 bytes.
        byte[] bytes = new byte[u2length];
        dataInput.readFully(bytes);
        utf8Constant.setBytes(bytes);
    }


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        dataInput.skipBytes(4);
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        dataInput.skipBytes(4);
    }


    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        dataInput.skipBytes(3);
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        dataInput.skipBytes(4);
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        classConstant.u2nameIndex = dataInput.readUnsignedShort();
    }


    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        dataInput.skipBytes(2);
    }


    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant)
    {
        dataInput.skipBytes(4);
    }


    public void visitModuleConstant(Clazz clazz, ModuleConstant moduleConstant)
    {
        dataInput.skipBytes(2);
    }


    public void visitPackageConstant(Clazz clazz, PackageConstant packageConstant)
    {
        dataInput.skipBytes(2);
    }

    // Small utility methods.

    /**
     * Returns the class name of the ClassConstant at the specified index in the
     * reusable constant pool.
     */
    private String getClassName(int constantIndex)
    {
        ClassConstant classEntry = (ClassConstant)constantPool[constantIndex];

        return getString(classEntry.u2nameIndex);
    }


    /**
     * Returns the string of the Utf8Constant at the specified index in the
     * reusable constant pool.
     */
    private String getString(int constantIndex)
    {
        return ((Utf8Constant)constantPool[constantIndex]).getString();
    }


    private Constant createConstant()
    {
        int u1tag = dataInput.readUnsignedByte();

        switch (u1tag)
        {
            case Constant.INTEGER:             return new IntegerConstant();
            case Constant.FLOAT:               return new FloatConstant();
            case Constant.LONG:                return new LongConstant();
            case Constant.DOUBLE:              return new DoubleConstant();
            case Constant.STRING:              return new StringConstant();
            case Constant.UTF8:                return new Utf8Constant();
            case Constant.DYNAMIC:             return new DynamicConstant();
            case Constant.INVOKE_DYNAMIC:      return new InvokeDynamicConstant();
            case Constant.METHOD_HANDLE:       return new MethodHandleConstant();
            case Constant.FIELDREF:            return new FieldrefConstant();
            case Constant.METHODREF:           return new MethodrefConstant();
            case Constant.INTERFACE_METHODREF: return new InterfaceMethodrefConstant();
            case Constant.CLASS:               return new ClassConstant();
            case Constant.METHOD_TYPE:         return new MethodTypeConstant();
            case Constant.NAME_AND_TYPE:       return new NameAndTypeConstant();
            case Constant.MODULE:              return new ModuleConstant();
            case Constant.PACKAGE:             return new PackageConstant();

            default: throw new RuntimeException("Unknown constant type ["+u1tag+"] in constant pool");
        }
    }


    private void skipAttributes()
    {
        int u2attributesCount = dataInput.readUnsignedShort();

        for (int index = 0; index < u2attributesCount; index++)
        {
            skipAttribute();
        }
    }


    private void skipAttribute()
    {
        dataInput.skipBytes(2);
        int u4attributeLength = dataInput.readInt();
        dataInput.skipBytes(u4attributeLength);
    }


    /**
     * Returns the size in bytes of the given primitive type.
     */
    private int primitiveSize(char primitiveType)
    {
        switch (primitiveType)
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:    return 1;
            case TypeConstants.CHAR:
            case TypeConstants.SHORT:   return 2;
            case TypeConstants.INT:
            case TypeConstants.FLOAT:   return 4;
            case TypeConstants.LONG:
            case TypeConstants.DOUBLE:  return 8;
        }

        return 0;
    }
}
