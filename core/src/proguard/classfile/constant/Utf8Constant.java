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
package proguard.classfile.constant;

import proguard.classfile.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.util.StringUtil;

import java.io.UnsupportedEncodingException;

/**
 * This {@link Constant} represents a UTF-8 constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class Utf8Constant extends Constant
{
    // There are a lot of Utf8Constant objects, so we're optimising their storage.
    // Initially, we're storing the UTF-8 bytes in a byte array.
    // When the corresponding String is requested, we ditch the array and just
    // store the String.

    //private int u2length;
    private byte[] bytes;

    private String string;


    /**
     * Creates an uninitialized Utf8Constant.
     *
     */
    public Utf8Constant()
    {
    }


    /**
     * Creates a Utf8Constant containing the given string.
     */
    public Utf8Constant(String string)
    {
        this.bytes  = null;
        this.string = string;
    }


    /**
     * Initializes the UTF-8 data with an array of bytes.
     */
    public void setBytes(byte[] bytes)
    {
        this.bytes  = bytes;
        this.string = null;
    }


    /**
     * Returns the UTF-8 data as an array of bytes.
     */
    public byte[] getBytes()
    {
        switchToByteArrayRepresentation();

        return bytes;
    }


    /**
     * Initializes the UTF-8 data with a String.
     */
    public void setString(String utf8String)
    {
        this.bytes  = null;
        this.string = utf8String;
    }


    /**
     * Returns the UTF-8 data as a String.
     */
    public String getString()
    {
        switchToStringRepresentation();

        return string;
    }


    // Implementations for Constant.

    public int getTag()
    {
        return Constant.UTF8;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitUtf8Constant(clazz, this);
    }


    // Small utility methods.

    /**
     * Switches to a byte array representation of the UTF-8 data.
     */
    private void switchToByteArrayRepresentation()
    {
        if (bytes == null)
        {
            bytes  = StringUtil.getModifiedUtf8Bytes(string);
            string = null;
        }
    }


    /**
     * Switches to a String representation of the UTF-8 data.
     */
    private void switchToStringRepresentation()
    {
        if (string == null)
        {
            string = StringUtil.getString(bytes);
            bytes  = null;
        }
    }
}
