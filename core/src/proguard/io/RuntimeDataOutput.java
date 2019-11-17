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
package proguard.io;

import java.io.*;

/**
 * This class delegates its method calls to the corresponding DataOutput methods,
 * converting its IOExceptions to RuntimeExceptions.
 *
 * The class provides two convenience methods that additionally check whether the
 * written values are unsigned resp. signed short values before writing them.
 *
 * @author Eric Lafortune
 */
public class RuntimeDataOutput
{
    private final DataOutput dataOutput;


    public RuntimeDataOutput(DataOutput dataOutput)
    {
        this.dataOutput = dataOutput;
    }


    // Methods delegating to DataOutput.

    public void write(byte[] b)
    {
        try
        {
            dataOutput.write(b);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void write(byte[] b, int off, int len)
    {
        try
        {
            dataOutput.write(b, off, len);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void write(int b)
    {
        try
        {
            dataOutput.write(b);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeBoolean(boolean v)
    {
        try
        {
            dataOutput.writeBoolean(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeByte(int v)
    {
        try
        {
            dataOutput.writeByte(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeBytes(String s)
    {
        try
        {
            dataOutput.writeBytes(s);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeChar(int v)
    {
        try
        {
            dataOutput.writeChar(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeChars(String s)
    {
        try
        {
            dataOutput.writeChars(s);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeDouble(double v)
    {
        try
        {
            dataOutput.writeDouble(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeFloat(float v)
    {
        try
        {
            dataOutput.writeFloat(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeInt(int v)
    {
        try
        {
            dataOutput.writeInt(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeLong(long v)
    {
        try
        {
            dataOutput.writeLong(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    /**
     * Checks if the given value is an unsigned short value before writing it.
     *
     * @throws IllegalArgumentException if the value is not an unsigned short value.
     * @see #writeShort(int)
     */
    public void writeUnsignedShort(int v)
    {
        if ((v & 0xffff) != v)
        {
            throw new IllegalArgumentException("Overflow of unsigned short value ["+v+"]");
        }

        writeShort(v);
    }


    /**
     * Checks if the given value is a signed short value before writing it.
     *
     * @throws IllegalArgumentException if the value is not a signed short value.
     * @see #writeShort(int)
     */
    public void writeSignedShort(int v)
    {
        if ((short)v != v)
        {
            throw new IllegalArgumentException("Overflow of signed short value ["+v+"]");
        }

        writeShort(v);
    }


    public void writeShort(int v)
    {
        try
        {
            dataOutput.writeShort(v);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public void writeUTF(String str)
    {
        try
        {
            dataOutput.writeUTF(str);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
