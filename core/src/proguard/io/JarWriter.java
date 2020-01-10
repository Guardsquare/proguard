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

import proguard.util.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * This {@link DataEntryWriter} sends data entries to another given data entry writer,
 * automatically adding a manifest file.
 * <p/>
 * You'll typically wrap a {@link ZipWriter} or one of its extensions:
 * <pre>
 *     new JarWriter(new ZipWriter(...))
 * </pre>
 *
 * @author Eric Lafortune
 */
public class JarWriter implements DataEntryWriter
{
    public  static final String        DEFAULT_DIGEST_ALGORITHM   = "SHA-256";
    private static final String        FORCED_DIGEST_ALGORITHM    = System.getProperty("digest.algorithm");
    private static final String        MANIFEST_MF                = "META-INF/MANIFEST.MF";
    private static final StringMatcher EXCLUDED_FILE_NAME_MATCHER =
        new ListParser(new FileNameParser()).parse("META-INF/MANIFEST.MF,META-INF/*.SF,META-INF/*.DSA,META-INF/*.RSA,META-INF/SIG-*");

    protected final String[]        digestAlgorithms;
    protected final String          creator;
    private   final String          manifestFileName;
    private   final StringFunction  manifestEntryNameFunction;
    protected final DataEntryWriter zipEntryWriter;
    protected final DataEntryWriter manifestEntryWriter;

    protected DataEntry   currentManifestEntry;
    private   PrintWriter manifestWriter;


    /**
     * Creates a new JarWriter wth default manifest digest "SHA-256" and
     * manifest file name "META-INF/MANIFEST.MF".
     * @param zipEntryWriter the data entry writer that can provide output
     *                       streams for the jar entries.
     */
    public JarWriter(DataEntryWriter zipEntryWriter)
    {
        this(new String[] { DEFAULT_DIGEST_ALGORITHM },
             zipEntryWriter);
    }


    /**
     * Creates a new JarWriter wth default manifest file name
     * "META-INF/MANIFEST.MF".
     * @param digestAlgorithms the manifest digest algorithms, e.g. "SHA-256".
     * @param zipEntryWriter   the data entry writer that can provide output
     *                         streams for the jar entries.
     */
    public JarWriter(String[]        digestAlgorithms,
                     DataEntryWriter zipEntryWriter)
    {
        this(digestAlgorithms,
             null,
             zipEntryWriter);
    }


    /**
     * Creates a new JarWriter wth default manifest file name
     * "META-INF/MANIFEST.MF".
     * @param digestAlgorithms the manifest digest algorithms, e.g. "SHA-256".
     * @param creator          the creator to mention in the manifest file.
     * @param zipEntryWriter   the data entry writer that can provide output
     *                         streams for the jar entries.
     */
    public JarWriter(String[]        digestAlgorithms,
                     String          creator,
                     DataEntryWriter zipEntryWriter)
    {
        this(digestAlgorithms,
             creator,
             MANIFEST_MF,
             StringFunction.IDENTITY_FUNCTION,
             zipEntryWriter,
             zipEntryWriter);
    }


    /**
     * Creates a new JarWriter.
     * @param digestAlgorithms          the manifest digest algorithms, e.g.
     *                                  "SHA-256".
     * @param creator                   the creator to mention in the manifest
     *                                  file.
     * @param manifestFileName          the manifest file name, e.g.
     *                                  "META-INF/MANIFEST.MF".
     * @param manifestEntryNameFunction the function to transform entry
     *                                  names in the manifest (not in the jar).
     * @param zipEntryWriter            the data entry writer that can provide
     *                                  output streams for the jar entries.
     * @param manifestEntryWriter       the data entry writer that can provide
     *                                  an output stream for the manifest file.
     */
    public JarWriter(String[]        digestAlgorithms,
                     String          creator,
                     String          manifestFileName,
                     StringFunction  manifestEntryNameFunction,
                     DataEntryWriter zipEntryWriter,
                     DataEntryWriter manifestEntryWriter)
    {
        this.digestAlgorithms          = FORCED_DIGEST_ALGORITHM != null ?
            new String[] { FORCED_DIGEST_ALGORITHM } :
            digestAlgorithms;
        this.creator                   = creator;
        this.manifestFileName          = manifestFileName;
        this.manifestEntryNameFunction = manifestEntryNameFunction;
        this.zipEntryWriter            = zipEntryWriter;
        this.manifestEntryWriter       = manifestEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return zipEntryWriter.createDirectory(dataEntry);
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2)
    throws IOException
    {
        return zipEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        String dataEntryName = dataEntry.getName();

        finishIfNecessary(dataEntry);

        // Don't sign or even include the signature-related files.
        if (EXCLUDED_FILE_NAME_MATCHER.matches(dataEntryName))
        {
            return null;
        }

        setUp(dataEntry);

        // Get the output stream, with prepared manifest and digest entries.
        OutputStream outputStream = zipEntryWriter.createOutputStream(dataEntry);

        // Digest the output stream (if any), except for the manifest itself
        // (when the manifest writer isn't open yet). We need to create new
        // digest instances, because multiple files can be open at the same
        // time.
        return
            outputStream   == null ||
            manifestWriter == null ? outputStream :
                new MyMultiDigestOutputStream(
                    manifestEntryNameFunction.transform(dataEntryName),
                    createMessageDigests(digestAlgorithms),
                    manifestWriter,
                    outputStream);
    }


    @Override
    public void close() throws IOException
    {
        finish();

        zipEntryWriter.close();
        //manifestEntryWriter.close();
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "JarWriter");
        zipEntryWriter.println(pw, prefix + "  ");
    }


    /**
     * Sets up streams and writers for capturing digests and signatures for
     * a given parent entry.
     */
    private void setUp(DataEntry dataEntry) throws IOException
    {
        if (currentManifestEntry == null)
        {
            currentManifestEntry =
                new RenamedDataEntry(dataEntry, manifestFileName);

            openManifestFiles();
        }
    }


    /**
     * Prepares streams and writers for capturing digests of a parent entry.
     */
    protected void openManifestFiles() throws IOException
    {
        // Create the manifest entry, so it comes first in the central
        // directory of the zip. Its local file header and actual contents
        // will come last, since it will be closed last.
        OutputStream manifestOutputStream =
            createManifestOutputStream(currentManifestEntry);

        if (manifestOutputStream != null)
        {
            // Write out a main section for the manifest file.
            manifestWriter = printWriter(manifestOutputStream);

            manifestWriter.println("Manifest-Version: 1.0");
            if (creator != null)
            {
                manifestWriter.println("Created-By: " + creator);
            }
            manifestWriter.println();
            manifestWriter.flush();
        }
    }


    /**
     * Creates an output stream for the specified manifest file.
     */
    protected OutputStream createManifestOutputStream(DataEntry manifestEntry)
    throws IOException
    {
        return manifestEntryWriter.createOutputStream(manifestEntry);
    }


    /**
     * Writes out the collected manifest file for the current jar, if we're
     * entering a new jar with this data entry.
     */
    protected void finishIfNecessary(DataEntry dataEntry) throws IOException
    {
        // Would the new manifest entry end up in a different jar?
        if (currentManifestEntry != null &&
            !zipEntryWriter.sameOutputStream(currentManifestEntry, new RenamedDataEntry(dataEntry, manifestFileName)))
        {
            finish();
        }
    }


    /**
     * Writes out the collected manifest file before closing the jar, if any.
     */
    protected void finish() throws IOException
    {
        if (manifestWriter != null)
        {
            manifestWriter.close();

            currentManifestEntry = null;
            manifestWriter       = null;
        }
    }


    // Small utility methods.

    /**
     * Creates a convenience writer.
     * @param outputStream the underlying output stream.
     */
    protected PrintWriter printWriter(OutputStream outputStream)
    throws IOException
    {
        return new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }


    /**
     * Creates message digests for the specified algorithms.
     */
    private MessageDigest[] createMessageDigests(String[] digestAlgorithms)
    {
        try
        {
            MessageDigest[] manifestDigests =
                new MessageDigest[digestAlgorithms.length];

            for (int index = 0; index < digestAlgorithms.length; index++)
            {
                manifestDigests[index] =
                    MessageDigest.getInstance(digestAlgorithms[index]);
            }

            return manifestDigests;
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }


    // Helper classes.

    /**
     * This FilterOutputStream automatically appends a file digest entry
     * to a given manifest writer, when the stream is closed.
     */
    protected static class MyMultiDigestOutputStream
    extends                FilterOutputStream
    {
        private final String          entryName;
        private final MessageDigest[] manifestDigests;
        private final PrintWriter     manifestWriter;


        public MyMultiDigestOutputStream(String          entryName,
                                         MessageDigest[] manifestDigests,
                                         PrintWriter     manifestWriter,
                                         OutputStream    outputStream)
        {
            super(outputStream);

            this.entryName       = entryName;
            this.manifestDigests = manifestDigests;
            this.manifestWriter  = manifestWriter;
        }


        // Overridden methods for OutputStream.

        @Override
        public void write(int b) throws IOException
        {
            out.write(b);

            // Update the digests.
            for (int index = 0; index < manifestDigests.length; index++)
            {
                manifestDigests[index].update((byte) b);
            }
        }


        @Override
        public void write(byte[] bytes, int off, int len) throws IOException
        {
            out.write(bytes, off, len);

            // Update the digests.
            for (int index = 0; index < manifestDigests.length; index++)
            {
                manifestDigests[index].update(bytes, off, len);
            }
        }


        @Override
        public void close() throws IOException
        {
            super.close();

            // Write out the digests.
            appendDigests(entryName);
        }


        /**
         * Appends the collected digests and signatures to the proper writer.
         */
        private void appendDigests(String entryName)
        {
            manifestWriter.println("Name: " + entryName);

            for (int index = 0; index < manifestDigests.length; index++)
            {
                MessageDigest manifestDigest = manifestDigests[index];

                // Digest the written data entry for the manifest file.
                manifestWriter.println(manifestDigest.getAlgorithm() + "-Digest: " +
                                       Base64Util.encode(manifestDigest.digest()));
            }

            manifestWriter.println();
            manifestWriter.flush();
        }
    }


    /**
     * Provides a simple test for this class, creating a signed apk file (only
     * v1) with the given name and a few aligned/compressed/uncompressed
     * zip entries.
     * Arguments:
     *     jar_filename
     * Verify the jar with:
     *     jarsigner -verify -verbose /tmp/test.jar
     */
    public static void main(String[] args)
    {
        try
        {
            // Get the arguments.
            String jarFileName = args[0];

            // Start writing out the jar file.
            DataEntryWriter writer =
                new JarWriter(
                new ZipWriter(new FixedStringMatcher("file1.txt"), 4, 0,
                new FixedFileWriter(new File(jarFileName))));

            PrintWriter printWriter =
                new PrintWriter(writer.createOutputStream(new DummyDataEntry(null, "file1.txt", 0L, false)));
            printWriter.println("Hello, world!");
            printWriter.close();

            printWriter =
                new PrintWriter(writer.createOutputStream(new DummyDataEntry(null, "file2.txt", 0L, false)));
            printWriter.println("Hello again, world!");
            printWriter.close();

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
