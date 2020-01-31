/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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
package proguard;

import proguard.classfile.ClassPool;
import proguard.classfile.util.ClassUtil;
import proguard.configuration.ConfigurationLogger;
import proguard.io.*;
import proguard.resources.file.ResourceFilePool;
import proguard.resources.file.util.ResourceFilePoolNameFunction;
import proguard.util.*;

import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * This class writes the output class files and resource files, packaged in
 * jar files, etc, if required.
 *
 * @author Eric Lafortune
 */
public class OutputWriter
{
    private final Configuration configuration;


    /**
     * Creates a new OutputWriter to write output class files as specified by
     * the given configuration.
     */
    public OutputWriter(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Writes the given class pool to class files, based on the current
     * configuration.
     */
    public void execute(ClassPool             programClassPool,
                        ResourceFilePool      resourceFilePool,
                        ExtraDataEntryNameMap extraDataEntryNameMap) throws IOException
    {
        ClassPath programJars = configuration.programJars;

        // Construct a filter for files that shouldn't be compressed.
        StringMatcher uncompressedFilter =
            configuration.dontCompress == null ? null :
                new ListParser(new FileNameParser()).parse(configuration.dontCompress);

        // Get the private key from the key store.
        KeyStore.PrivateKeyEntry[] privateKeyEntries =
            retrievePrivateKeys(configuration);

        // Convert the current time into DOS date and time.
        Date currentDate = new Date();
        int modificationTime =
            (currentDate.getYear()  - 80) << 25 |
            (currentDate.getMonth() + 1 ) << 21 |
             currentDate.getDate()        << 16 |
             currentDate.getHours()       << 11 |
             currentDate.getMinutes()     << 5  |
             currentDate.getSeconds()     >> 1;

        // Create a main data entry writer factory for all nested archives.
        DataEntryWriterFactory dataEntryWriterFactory =
            new DataEntryWriterFactory(programClassPool,
                                       resourceFilePool,
                                       modificationTime,
                                       uncompressedFilter,
                                       configuration.zipAlign,
                                       configuration.android, //resourceInfo.pageAlignNativeLibs,
                                       configuration.obfuscate,
                                       privateKeyEntries);

        int firstInputIndex = 0;
        int lastInputIndex  = 0;

        // Go over all program class path entries.
        for (int index = 0; index < programJars.size(); index++)
        {
            // Is it an input entry?
            ClassPathEntry entry = programJars.get(index);
            if (!entry.isOutput())
            {
                // It's an input entry. Remember the highest index.
                lastInputIndex = index;
            }
            else
            {
                // It's an output entry. Is it the last one in a
                // series of output entries?
                int nextIndex = index + 1;
                if (nextIndex == programJars.size() ||
                    !programJars.get(nextIndex).isOutput())
                {
                    // Write the processed input entries to the output entries.
                    writeOutput(dataEntryWriterFactory,
                                programClassPool,
                                resourceFilePool,
                                extraDataEntryNameMap,
                                programJars,
                                firstInputIndex,
                                lastInputIndex + 1,
                                nextIndex);

                    // Start with the next series of input entries.
                    firstInputIndex = nextIndex;
                }
            }
        }
    }


    /**
     * Gets the private keys from the key stores, based on the given configuration.
     */
    private KeyStore.PrivateKeyEntry[] retrievePrivateKeys(Configuration configuration)
    throws IOException
    {
        // Check the signing variables.
        List<File>   keyStoreFiles     = configuration.keyStores;
        List<String> keyStorePasswords = configuration.keyStorePasswords;
        List<String> keyAliases        = configuration.keyAliases;
        List<String> keyPasswords      = configuration.keyPasswords;

        // Don't sign if not all of the signing parameters have been
        // specified.
        if (keyStoreFiles     == null ||
            keyStorePasswords == null ||
            keyAliases        == null ||
            keyPasswords      == null)
        {
            // Print a note if any of the signing parameters have been
            // specified.
            if ((keyStoreFiles     != null ||
                 keyStorePasswords != null ||
                 keyAliases        != null ||
                 keyPasswords      != null) &&
                (configuration.note == null ||
                 !configuration.note.isEmpty()))
            {
                StringBuffer missing   = new StringBuffer();
                StringBuffer specified = new StringBuffer();

                (keyStoreFiles     == null ? missing : specified).append("a key store file, ");
                (keyStorePasswords == null ? missing : specified).append("a key store password, ");
                (keyAliases        == null ? missing : specified).append("a key alias, ");
                (keyPasswords      == null ? missing : specified).append("a key password, ");

                System.out.println("Note: you've specified "+specified.toString());
                System.out.println("      but not "+missing.substring(0, missing.length()-2)+".");
                System.out.println("      You should specify the missing parameters to sign the output jars.");
            }

            return null;
        }

       try
       {
           // We'll interpret the configuration in a flexible way,
           // e.g. with a single key store and multiple keys, or vice versa.
           int keyCount = Math.max(keyStoreFiles.size(), keyAliases.size());

           KeyStore.PrivateKeyEntry[] privateKeys =
               new KeyStore.PrivateKeyEntry[keyCount];

           Map certificates = new HashMap(keyCount);

           for (int index = 0; index < keyCount; index++)
           {
               // Create the private key
               File   keyStoreFile     = keyStoreFiles    .get(Math.min(index, keyStoreFiles    .size()-1));
               String keyStorePassword = keyStorePasswords.get(Math.min(index, keyStorePasswords.size()-1));
               String keyAlias         = keyAliases       .get(Math.min(index, keyAliases       .size()-1));
               String keyPassword      = keyPasswords     .get(Math.min(index, keyPasswords     .size()-1));

               KeyStore.PrivateKeyEntry privateKeyEntry =
                   retrievePrivateKey(keyStoreFile,
                                      keyStorePassword,
                                      keyAlias,
                                      keyPassword);

               // Check if the certificate accidentally is a duplicate,
               // to avoid basic configuration errors.
               X509Certificate certificate    = (X509Certificate)privateKeyEntry.getCertificate();
               Integer         duplicateIndex = (Integer)certificates.put(certificate, Integer.valueOf(index));
               if (duplicateIndex != null)
               {
                   throw new IllegalArgumentException("Duplicate specified signing certificates #"+(duplicateIndex.intValue()+1)+" and #"+(index+1)+" out of "+keyCount+" ["+certificate.getSubjectDN().getName()+"]");
               }

               // Add the private key to the list.
               privateKeys[index] = privateKeyEntry;
           }

           return privateKeys;
       }
       catch (Exception e)
       {
           throw (IOException)new IOException("Can't sign jar ("+e.getMessage()+")", e);
        }
    }


    private KeyStore.PrivateKeyEntry retrievePrivateKey(File   keyStoreFile,
                                                        String keyStorePassword,
                                                        String keyAlias,
                                                        String keyPassword)
    throws IOException, GeneralSecurityException
    {
        // Get the private key from the key store.
        FileInputStream keyStoreInputStream =
            new FileInputStream(keyStoreFile);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
        keyStoreInputStream.close();

        KeyStore.ProtectionParameter protectionParameter =
            new KeyStore.PasswordProtection(keyPassword.toCharArray());

        KeyStore.PrivateKeyEntry entry =
            (KeyStore.PrivateKeyEntry)keyStore.getEntry(keyAlias, protectionParameter);

        if (entry == null)
        {
            throw new GeneralSecurityException("Can't find key alias '"+keyAlias+"' in key store ["+keyStoreFile.getPath()+"]");
        }

        return entry;
    }


    /**
     * Transfers the specified input jars to the specified output jars.
     */
    private void writeOutput(DataEntryWriterFactory dataEntryWriterFactory,
                             ClassPool              programClassPool,
                             ResourceFilePool       resourceFilePool,
                             ExtraDataEntryNameMap  extraDataEntryNameMap,
                             ClassPath              classPath,
                             int                    fromInputIndex,
                             int                    fromOutputIndex,
                             int                    toOutputIndex)
    throws IOException
    {
        // Debugging tip: your can wrap data entry writers and readers with
        //     new DebugDataEntryWriter("...", ....)
        //     new DebugDataEntryReader("...", ....)

        try
        {
            // Construct the writer that can write apks, jars, wars, ears, zips,
            // and directories, cascading over the specified output entries.
            DataEntryWriter writer =
                dataEntryWriterFactory.createDataEntryWriter(classPath,
                                                             fromOutputIndex,
                                                             toOutputIndex,
                                                             null);

            if (configuration.addConfigurationDebugging)
            {
                writer = new ExtraDataEntryWriter(ConfigurationLogger.CLASS_MAP_FILENAME,
                    writer,
                    new ClassMapDataEntryWriter(programClassPool, writer));
                System.err.println("Warning: -addconfigurationdebugging is enabled; the resulting build will contain obfuscation information.");
                System.err.println("It should only be used for debugging purposes.");
            }

            DataEntryWriter resourceWriter = writer;

            // Adapt plain resource file names that correspond to class names,
            // if necessary.
            if (configuration.obfuscate &&
                configuration.adaptResourceFileNames != null)
            {
                // Rename processed general resources.
                resourceWriter =
                    renameResourceFiles(resourceFilePool,
                                        resourceWriter);
            }

            // By default, just copy resource files into the above writers.
            DataEntryReader resourceCopier =
                new DataEntryCopier(resourceWriter);

            // We're now switching to the reader side, operating on the
            // contents possibly parsed from the input streams.
            DataEntryReader resourceRewriter = resourceCopier;

            // Adapt resource file contents, if allowed.
            if ((configuration.shrink   ||
                 configuration.optimize ||
                 configuration.obfuscate) &&
                configuration.adaptResourceFileContents != null)
            {
                DataEntryReader adaptingContentWriter = resourceRewriter;

                // Adapt the contents of general resource files (manifests
                // and native libraries).
                if (configuration.obfuscate)
                {
                    adaptingContentWriter =
                        adaptResourceFiles(programClassPool,
                                           resourceWriter);
                }

                // Add the overall filter for adapting resource file contents.
                resourceRewriter =
                    new NameFilteredDataEntryReader(configuration.adaptResourceFileContents,
                        adaptingContentWriter,
                        resourceRewriter);
            }

            // Write any kept directories.
            DataEntryReader reader =
                writeDirectories(programClassPool,
                                 resourceCopier,
                                 resourceRewriter);

            // Trigger writing classes.
            reader =
                new ClassFilter(new IdleRewriter(writer),
                                reader);

            // Inject any attached data entries.
            reader = new ExtraDataEntryReader(extraDataEntryNameMap,
                                              reader);

            // Go over the specified input entries and write their processed
            // versions.
            new InputReader(configuration).readInput("  Copying resources from program ",
                                                     classPath,
                                                     fromInputIndex,
                                                     fromOutputIndex,
                                                     reader);

            // Close all output entries.
            writer.close();
        }
        catch (IOException ex)
        {
            throw (IOException)new IOException("Can't write [" + classPath.get(fromOutputIndex).getName() + "] (" + ex.getMessage() + ")").initCause(ex);
        }
    }


    /**
     * Returns a writer that writes possibly renamed resource files to the
     * given resource writer.
     */
    private DataEntryWriter renameResourceFiles(ResourceFilePool resourceFilePool,
                                                DataEntryWriter  dataEntryWriter)
    {
        return new RenamedDataEntryWriter(new ResourceFilePoolNameFunction(resourceFilePool),
                                          dataEntryWriter);
    }


    /**
     * Returns a reader that writes all general resource files (manifest,
     * native libraries, text files) with shrunk, optimized, and obfuscated
     * contents to the given writer.
     */
    private DataEntryReader adaptResourceFiles(ClassPool       programClassPool,
                                               DataEntryWriter writer)
    {
        // Pick a suitable encoding.
        Charset charset = configuration.android ?
            Charset.forName("UTF-8") :
            Charset.defaultCharset();

        // Filter between the various general resource files.
        return
            new NameFilteredDataEntryReader("META-INF/MANIFEST.MF,META-INF/*.SF",
                new ManifestRewriter(programClassPool, charset, writer),
            new DataEntryRewriter(programClassPool, charset, writer));
    }


    /**
     * Writes possibly renamed directories that should be preserved to the
     * given resource copier, and non-directories to the given file copier.
     */
    private DirectoryFilter writeDirectories(ClassPool       programClassPool,
                                             DataEntryReader directoryCopier,
                                             DataEntryReader fileCopier)
    {
        DataEntryReader directoryRewriter = null;

        // Wrap the directory copier with a filter and a data entry renamer.
        if (configuration.keepDirectories != null)
        {
            StringFunction packagePrefixFunction =
                new MapStringFunction(createPackagePrefixMap(programClassPool));

            directoryRewriter =
                new NameFilteredDataEntryReader(configuration.keepDirectories,
                new RenamedDataEntryReader(packagePrefixFunction,
                                           directoryCopier,
                                           directoryCopier));
        }

        // Filter on directories and files.
        return new DirectoryFilter(directoryRewriter, fileCopier);
    }


    /**
     * Creates a map of old package prefixes to new package prefixes, based on
     * the given class pool.
     */
    private static Map createPackagePrefixMap(ClassPool classPool)
    {
        Map packagePrefixMap = new HashMap();

        Iterator iterator = classPool.classNames();
        while (iterator.hasNext())
        {
            String className     = (String)iterator.next();
            String packagePrefix = ClassUtil.internalPackagePrefix(className);

            String mappedNewPackagePrefix = (String)packagePrefixMap.get(packagePrefix);
            if (mappedNewPackagePrefix == null ||
                !mappedNewPackagePrefix.equals(packagePrefix))
            {
                String newClassName     = classPool.getClass(className).getName();
                String newPackagePrefix = ClassUtil.internalPackagePrefix(newClassName);

                packagePrefixMap.put(packagePrefix, newPackagePrefix);
            }
        }

        return packagePrefixMap;
    }
}
