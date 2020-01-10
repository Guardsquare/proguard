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
import java.math.BigInteger;
import java.util.Stack;

/**
 * This class provides methods to write data with Distinguished Encoding Rules
 * to an underlying output stream. DER is an encoding of ASN.1 (Abstract
 * Syntax Notation).
 * <p/>
 * This is for example the format of signature files like META-INF/CERT.RSA
 * <p/>
 * You can view the resulting file with
 *   openssl asn1parse -inform DER META-INF/CERT.RSA
 *
 * @author Eric Lafortune
 */
class DEROutputStream // extends OutputStream
{
    private static final byte BOOLEAN_TAG           = 0x01;
    private static final byte INTEGER_TAG           = 0x02;
    private static final byte BIT_STRING_TAG        = 0x03;
    private static final byte OCTET_STRING_TAG      = 0x04;
    private static final byte NULL_TAG              = 0x05;
    private static final byte OBJECT_IDENTIFIER_TAG = 0x06;
    private static final byte UTF8_STRING_TAG       = 0x0c;
    private static final byte PRINTABLE_STRING_TAG  = 0x13;
    private static final byte T61_STRING_TAG        = 0x14;
    private static final byte IA5_STRING_TAG        = 0x16;
    private static final byte UTC_TIME_TAG 	        = 0x17;
    private static final byte SEQUENCE_TAG          = 0x30;
    private static final byte SET_TAG               = 0x31;
    private static final byte IMPLICIT_TAG          = (byte)0xa0;


    private OutputStream outputStream;

    // A stack to push output streams when we're starting sequences, sets,
    // and implicits.
    private final Stack outputStreams = new Stack();


    /**
     * Creates a new DEROutputStream.
     * @param outputStream  the delegate output stream.
     */
    public DEROutputStream(OutputStream  outputStream)
    {
        this.outputStream = outputStream;
    }


    /**
     * Writes the given integer, compressed in 2 to 5 bytes.
     */
    public void writeInteger(int i) throws IOException
    {
        outputStream.write(INTEGER_TAG);
        if      (i << 24 >>> 24 == i)
        {
            outputStream.write(1);
            outputStream.write(i);
        }
        else if (i << 16 >>> 16 == i)
        {
            outputStream.write(2);
            outputStream.write(i >> 8);
            outputStream.write(i     );
        }
        else if (i << 8 >>> 8 == i)
        {
            outputStream.write(3);
            outputStream.write(i >> 16);
            outputStream.write(i >>  8);
            outputStream.write(i      );
        }
        else
        {
            outputStream.write(4);
            outputStream.write(i >> 24);
            outputStream.write(i >> 16);
            outputStream.write(i >>  8);
            outputStream.write(i      );
        }
    }


    /**
     * Writes the given big integer.
     */
    public void writeInteger(BigInteger bigInteger) throws IOException
    {
        byte[] bytes = bigInteger.toByteArray();

        write(INTEGER_TAG, bytes);
    }


    /**
     * Writes the given byte array.
     */
    public void writeOctetString(byte[] bytes) throws IOException
    {
        write(OCTET_STRING_TAG, bytes);
    }


    /**
     * Writes a null element.
     */
    public void writeNull() throws IOException
    {
        outputStream.write(NULL_TAG);
        outputStream.write(0);
    }


    /**
     * Writes the given object identifier.
     */
    public void writeObjectIdentifier(byte[] bytes) throws IOException
    {
        write(OBJECT_IDENTIFIER_TAG, bytes);
    }


    /**
     * Writes the given string as a printable string, as a UTF-8 string
     * (if necessary), or as an IA5 string (if specified).
     */
    public void writeString(String s, boolean asIA5String) throws IOException
    {
        if (asIA5String)
        {
            writeIA5String(s);
        }
        else if (isPrintable(s))
        {
            writePrintableString(s);
        }
        else
        {
            writeUtf8String(s);
        }
    }


    /**
     * Writes the given string in UTF-8 encoding.
     */
    public void writeUtf8String(String s) throws IOException
    {
        byte[] bytes = s.getBytes("UTF8");

        write(UTF8_STRING_TAG, bytes);
    }


    /**
     * Writes the given string in T61 encoding.
     */
    public void writeT61String(String s) throws IOException
    {
        byte[] bytes = s.getBytes("ASCII");

        write(T61_STRING_TAG, bytes);
    }


    /**
     * Writes the given string in IA5 encoding.
     */
    public void writeIA5String(String s) throws IOException
    {
        byte[] bytes = s.getBytes("ASCII");

        write(IA5_STRING_TAG, bytes);
    }


    /**
     * Writes the given string in printable encoding.
     */
    public void writePrintableString(String s) throws IOException
    {
        byte[] bytes = s.getBytes("ASCII");

        write(PRINTABLE_STRING_TAG, bytes);
    }


    /**
     * Writes the given integer as a length, compressed in 1 to 5 bytes.
     */
    private void writeLength(int i) throws IOException
    {
        if      (i <= 0x0000007f)
        {
            outputStream.write(i);
        }
        else if (i <= 0x000000ff)
        {
            outputStream.write(0x81);
            outputStream.write(i);
        }
        else if (i <= 0x0000ffff)
        {
            outputStream.write(0x82);
            outputStream.write(i >> 8);
            outputStream.write(i     );
        }
        else if (i <= 0x00ffffff)
        {
            outputStream.write(0x83);
            outputStream.write(i >> 16);
            outputStream.write(i >>  8);
            outputStream.write(i      );
        }
        else
        {
            outputStream.write(0x84);
            outputStream.write(i >> 24);
            outputStream.write(i >> 16);
            outputStream.write(i >>  8);
            outputStream.write(i      );
        }
    }


    /**
     * Starts a sequence.
     */
    public void startSequence() throws IOException
    {
        pushOutputStream();
    }


    /**
     * Ends the most recently started sequence and writes it out.
     */
    public void endSequence() throws IOException
    {
        byte[] bytes = popOutputStream();
        writeSequence(bytes);
    }


    /**
     * Writes the given byte array representation of a sequence.
     */
    private void writeSequence(byte[] bytes) throws IOException
    {
        write(SEQUENCE_TAG, bytes);
    }


    /**
     * Starts a set.
     */
    public void startSet() throws IOException
    {
        pushOutputStream();
    }


    /**
     * Ends the most recently started set and writes it out.
     */
    public void endSet() throws IOException
    {
        byte[] bytes = popOutputStream();
        writeSet(bytes);
    }


    /**
     * Writes the given byte array representation of a set.
     */
    private void writeSet(byte[] bytes) throws IOException
    {
        write(SET_TAG, bytes);
    }


    /**
     * Starts an implicit.
     */
    public void startImplicit() throws IOException
    {
        pushOutputStream();
    }


    /**
     * Ends the most recently started implicit and writes it out.
     */
    public void endImplicit() throws IOException
    {
        byte[] bytes = popOutputStream();
        writeImplicit(bytes);
    }


    /**
     * Writes the given byte array representation of an implicit.
     */
    public void writeImplicit(byte[] bytes) throws IOException
    {
        write(IMPLICIT_TAG, bytes);
    }


    /**
     * Writes the given tag and byte array, with the proper length.
     */
    public void write(byte tag, byte[] bytes) throws IOException
    {
        outputStream.write(tag);
        writeLength(bytes.length);
        outputStream.write(bytes);
    }


    // Implementations for OutputStream.

    //public void write(int b) throws IOException
    //{
    //    outputStream.write(b);
    //}
    //
    //
    //public void write(byte[] bytes) throws IOException
    //{
    //    write(bytes, 0, bytes.length);
    //}
    //
    //
    //public void write(byte[] bytes, int offset, int size) throws IOException
    //{
    //    outputStream.write(bytes, offset, size);
    //}


    /**
     * Flushes the output stream.
     */
    public void flush() throws IOException
    {
        outputStream.flush();
    }


    /**
     * Closes the output stream.
     */
    public void close() throws IOException
    {
        if (!outputStreams.isEmpty())
        {
            throw new IllegalStateException("DER sequences/sets not ended properly");
        }

        outputStream.close();
    }


    // Small utility methods.

    /**
     * Returns whether the given string can be represented as a printable
     * string.
     */
    private boolean isPrintable(String s)
    {
        for (int index = 0; index < s.length(); index++)
        {
            char c = s.charAt(index);
            if (c < 32 || c >= 128)
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Pushes the current output stream, replacing it by a byte array output
     * stream to temporarily collect output.
     */
    private void pushOutputStream()
    {
        outputStreams.push(outputStream);
        outputStream = new ByteArrayOutputStream();
    }


    /**
     * Returns the bytes from the current byte array output stream
     * and restores the previous output stream.
     */
    private byte[] popOutputStream()
    {
        byte[] bytes = ((ByteArrayOutputStream)outputStream).toByteArray();
        outputStream = (OutputStream)outputStreams.pop();
        return bytes;
    }
}
