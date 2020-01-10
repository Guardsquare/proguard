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
 * This class delegates its method calls to the corresponding {@link DataInput} methods,
 * converting its IOExceptions to RuntimeExceptions.
 *
 * @author Eric Lafortune
 */
public class RuntimeDataInput
{
    private final DataInput dataInput;


    public RuntimeDataInput(DataInput dataInput)
    {
        this.dataInput = dataInput;
    }


    // Methods delegating to DataInput.

    public boolean readBoolean()
    {
        try
        {
            return dataInput.readBoolean();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public byte readByte()
    {
        try
        {
            return dataInput.readByte();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public char readChar()
    {
        try
        {
            return dataInput.readChar();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public double readDouble()
    {
        try
        {
            return dataInput.readDouble();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public float readFloat()
    {
        try
        {
            return dataInput.readFloat();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public void readFully(byte[] b)
    {
        try
        {
            dataInput.readFully(b);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public void readFully(byte[] b, int off, int len)
    {
        try
        {
            dataInput.readFully(b, off, len);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public int readInt()
    {
        try
        {
            return dataInput.readInt();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public String readLine()
    {
        try
        {
            return dataInput.readLine();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public long readLong()
    {
        try
        {
            return dataInput.readLong();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public short readShort()
    {
        try
        {
            return dataInput.readShort();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public int readUnsignedByte()
    {
        try
        {
            return dataInput.readUnsignedByte();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public int readUnsignedShort()
    {
        try
        {
            return dataInput.readUnsignedShort();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public String readUTF()
    {
        try
        {
            return dataInput.readUTF();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public int skipBytes(int n)
    {
        try
        {
            return dataInput.skipBytes(n);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
