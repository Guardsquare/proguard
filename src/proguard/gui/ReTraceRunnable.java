/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2014 Eric Lafortune (eric@graphics.cornell.edu)
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
package proguard.gui;

import proguard.retrace.ReTrace;

import javax.swing.*;
import java.awt.*;
import java.io.*;


/**
 * This <code>Runnable</code> runs ReTrace, sending console output to a text
 * area and any exceptions to message dialogs.
 *
 * @see ReTrace
 * @author Eric Lafortune
 */
final class ReTraceRunnable implements Runnable
{
    private final JTextArea consoleTextArea;
    private final boolean   verbose;
    private final File      mappingFile;
    private final String    stackTrace;


    /**
     * Creates a new ProGuardRunnable object.
     * @param consoleTextArea the text area to send the console output to.
     * @param verbose         specifies whether the de-obfuscated stack trace
     *                        should be verbose.
     * @param mappingFile     the mapping file that was written out by ProGuard.
     */
    public ReTraceRunnable(JTextArea consoleTextArea,
                           boolean   verbose,
                           File      mappingFile,
                           String    stackTrace)
    {
        this.consoleTextArea = consoleTextArea;
        this.verbose         = verbose;
        this.mappingFile     = mappingFile;
        this.stackTrace      = stackTrace;
    }


    // Implementation for Runnable.

    public void run()
    {
        consoleTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        consoleTextArea.setText("");

        // Redirect the stack trace string to the System's in stream, and the
        // out and err streams to the console text area.
        InputStream oldIn  = System.in;
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;

        ByteArrayInputStream inputStream =
           new ByteArrayInputStream(stackTrace.getBytes());

        PrintStream printStream =
            new PrintStream(new TextAreaOutputStream(consoleTextArea), true);

        System.setIn(inputStream);
        System.setOut(printStream);
        System.setErr(printStream);

        try
        {
            // Create a new ProGuard object with the GUI's configuration.
            ReTrace reTrace = new ReTrace(ReTrace.STACK_TRACE_EXPRESSION,
                                          verbose,
                                          mappingFile);

            // Run it.
            reTrace.execute();
        }
        catch (Exception ex)
        {
            // Print out the exception message.
            System.out.println(ex.getMessage());

            // Show a dialog as well.
            MessageDialogRunnable.showMessageDialog(consoleTextArea,
                                                    ex.getMessage(),
                                                    msg("errorReTracing"),
                                                    JOptionPane.ERROR_MESSAGE);
        }
        catch (OutOfMemoryError er)
        {
            // Forget about the ProGuard object as quickly as possible.
            System.gc();

            // Print out a message suggesting what to do next.
            System.out.println(msg("outOfMemory"));

            // Show a dialog as well.
            MessageDialogRunnable.showMessageDialog(consoleTextArea,
                                                    msg("outOfMemory"),
                                                    msg("errorReTracing"),
                                                    JOptionPane.ERROR_MESSAGE);
        }

        // Make sure all output has been sent to the console text area.
        printStream.flush();

        // Restore the old System's in, out, and err streams.
        System.setIn(oldIn);
        System.setOut(oldOut);
        System.setErr(oldErr);

        consoleTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        consoleTextArea.setCaretPosition(0);

        // Reset the global static redirection lock.
        ProGuardGUI.systemOutRedirected = false;
    }


    // Small utility methods.

    /**
     * Returns the message from the GUI resources that corresponds to the given
     * key.
     */
    private String msg(String messageKey)
    {
         return GUIResources.getMessage(messageKey);
    }
}
