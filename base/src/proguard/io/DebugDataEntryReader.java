/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.io;

import java.io.IOException;
import java.io.PrintStream;

/**
 * This DataEntryReader delegates to another DataEntryReader, after having
 * printed out a message and the data entry name.
 *
 * @author Eric Lafortune
 */
public class DebugDataEntryReader implements DataEntryReader
{
    private final String          message;
    private final PrintStream     printStream;
    private final DataEntryReader dataEntryReader;


    public DebugDataEntryReader(String message, DataEntryReader dataEntryReader)
    {
        this(message, System.err, dataEntryReader);
    }


    /**
     * Creates a new DebugDataEntryReader with the given message,
     * delegating to the given other reader.
     */
    public DebugDataEntryReader(String          message,
                                PrintStream     printStream,
                                DataEntryReader dataEntryReader)
    {
        this.message         = message;
        this.printStream     = printStream;
        this.dataEntryReader = dataEntryReader;
    }


    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        printStream.println(message + " ["+dataEntry+"]");

        dataEntryReader.read(dataEntry);
    }
}