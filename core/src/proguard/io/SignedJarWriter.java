/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.io;

import proguard.util.*;
import sun.security.pkcs.*;
import sun.security.x509.*;

import java.io.*;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * This JarWriter sends data entries to a given jar file, automatically
 * adding a manifest file and signing it with JAR signature scheme v1.
 *
 * You'll typically wrap a {@link ZipWriter} or one of its extensions:
 * <pre>
 *     new SignedJarWriter(privateKeyEntry, new ZipWriter(...))
 * </pre>
 *
 * @author Eric Lafortune
 */
public class SignedJarWriter extends JarWriter
{
    private static final boolean SUN_SECURITY_SIGNING = System.getProperty("sun.security.signing") != null;

    private final KeyStore.PrivateKeyEntry privateKeyEntry;
    private final int[]                    apkSignatureSchemeIDs;

    private MessageDigest         globalManifestDigest;
    private MessageDigest         manifestEntryDigest;
    private Signature             digitalSignature;

    private OutputStream          signatureFileStream;
    private PrintWriter           signatureWriter1;
    private PrintWriter           signatureWriter2;
    private ByteArrayOutputStream signatureResultStream1;
    private ByteArrayOutputStream signatureResultStream2;
    private OutputStream          digitalSignatureFileStream;


    /**
     * Creates a new SignedJarWriter.
     * @param privateKeyEntry the private key to be used for signing the zip
     *                        entries.
     * @param zipEntryWriter  the data entry writer that can provide output
     *                        streams for the jar entries.
     */
    public SignedJarWriter(KeyStore.PrivateKeyEntry privateKeyEntry,
                           DataEntryWriter          zipEntryWriter)
    {
        this(privateKeyEntry,
             new String[] { DEFAULT_DIGEST_ALGORITHM },
             null,
             null,
             zipEntryWriter);
    }


    /**
     * Creates a new SignedJarWriter with the given settings.
     * @param privateKeyEntry       the private key to be used for signing the
     *                              zip entries.
     * @param digestAlgorithms      the manifest digest algorithms, e.g.
     *                              "SHA-256".
     * @param creator               the creator to mention in the manifest
     *                              file.
     * @param apkSignatureSchemeIDs an optional list of external APK signature
     *                              scheme IDs to be specified with the
     *                              attribute "X-Android-APK-Signed" in *.SF
     *                              files.
     * @param zipEntryWriter        the data entry writer that can provide
     *                              output streams for the jar entries.
     */
    public SignedJarWriter(KeyStore.PrivateKeyEntry privateKeyEntry,
                           String[]                 digestAlgorithms,
                           String                   creator,
                           int[]                    apkSignatureSchemeIDs,
                           DataEntryWriter          zipEntryWriter)
    {
        super(digestAlgorithms, creator, zipEntryWriter);

        this.privateKeyEntry       = privateKeyEntry;
        this.apkSignatureSchemeIDs = apkSignatureSchemeIDs;

        try
        {
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

            // We have a digest algorithm (SHA1, SHA-256, SHA-384, SHA-512)
            // and an encryption algorithm (RSA, DSA, EC).
            // Derive the signature algorithm (SHA1withRSA,
            // SHA256withDSA, SHA256withECDSA,...)
            String encryptionAlgorithm = privateKey.getAlgorithm();
            String digestAlgorithm     = digestAlgorithms[0];
            String signatureAlgorithm  = digestAlgorithm.replace("-", "") +
                                         "with" +
                                         (encryptionAlgorithm.equals("EC") ?
                                              "ECDSA" :
                                              encryptionAlgorithm);

            // We need a global digest of the entire manifest,
            // and small digests of the individual entries.
            globalManifestDigest = MessageDigest.getInstance(digestAlgorithm);
            manifestEntryDigest  = MessageDigest.getInstance(digestAlgorithm);
            digitalSignature     = Signature.getInstance(signatureAlgorithm);
            digitalSignature.initSign(privateKey);
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }


    // Implementations for DataEntryWriter.

    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        // Get the output stream, with prepared manifest and digest entries.
        OutputStream outputStream = super.createOutputStream(dataEntry);

        // Digest the output stream (if any), except for the signature itself
        // (when the signature writer isn't open yet).
        return
            outputStream     == null ||
            signatureWriter2 == null ? null :
                new MyMultiDigestOutputStream(
                    dataEntry.getName(),
                    new MessageDigest[] { manifestEntryDigest },
                    signatureWriter2,
                    outputStream);
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "SignedJarWriter");
        zipEntryWriter.println(pw, prefix + "  ");
    }


    // Overriding methods for JarWriter.

    @Override
    protected void openManifestFiles() throws IOException
    {
        // Open the manifest file.
        super.openManifestFiles();

        // Open the signature file.
        signatureFileStream =
            manifestEntryWriter.createOutputStream(new RenamedDataEntry(currentManifestEntry,
                                                                        "META-INF/CERT.SF"));

        if (signatureFileStream != null)
        {
            String encryptionAlgorithm = privateKeyEntry.getPrivateKey().getAlgorithm();

            // Open the signature file.
            digitalSignatureFileStream =
                manifestEntryWriter.createOutputStream(new RenamedDataEntry(currentManifestEntry,
                                                                            "META-INF/CERT." + encryptionAlgorithm));

            // Digest the above main manifest section for the signature file.
            signatureResultStream1 = new ByteArrayOutputStream();
            signatureWriter1       = printWriter(signatureResultStream1);

            signatureWriter1.println("Signature-Version: 1.0");
            if (apkSignatureSchemeIDs != null)
            {
                signatureWriter1.print("X-Android-APK-Signed: ");
                for (int index = 0; index < apkSignatureSchemeIDs.length; index++)
                {
                    signatureWriter1.print(apkSignatureSchemeIDs[index]);
                    if (index < apkSignatureSchemeIDs.length-1)
                    {
                        signatureWriter1.print(',');
                    }
                }
                signatureWriter1.println();
            }
            if (creator != null)
            {
                signatureWriter1.println("Created-By: " + creator);
            }
            signatureWriter1.println(manifestEntryDigest.getAlgorithm() + "-Digest-Manifest-Main-Attributes: " +
                                     Base64Util.encode(manifestEntryDigest.digest()));

            // We'll have to append the digest of the entire manifest file
            // when we have collected it.

            // Start digesting the file sections in a separate writer.
            signatureResultStream2 = new ByteArrayOutputStream();
            signatureWriter2       = printWriter(signatureResultStream2);
        }
    }


    @Override
    protected OutputStream createManifestOutputStream(DataEntry manifestEntry)
    throws IOException
    {
        // Create the standard manifest stream.
        OutputStream manifestOutputStream =
            super.createManifestOutputStream(manifestEntry);

        if (manifestOutputStream == null)
        {
            return null;
        }

        // We need a global digest of the entire manifest,
        // and small digests of the individual entries,
        // so we wrap the manifest stream with two digest streams.
        return new DigestOutputStream(
               new DigestOutputStream(manifestOutputStream,
                                      globalManifestDigest),
                                      manifestEntryDigest);
    }


    @Override
    protected void finish() throws IOException
    {
        // Finish the manifest file.
        super.finish();

        if (digitalSignatureFileStream != null)
        {
            String digestAlgorithm = globalManifestDigest.getAlgorithm();

            // Add the global manifest digest after the main section in the
            // signature.
            signatureWriter1.println(digestAlgorithm + "-Digest-Manifest: " + Base64Util.encode(globalManifestDigest.digest()));
            signatureWriter1.println();
            signatureWriter1.flush();

            // Work around a bug that is reported in the source code of the
            // Android SDK class SignedJarBuilder.java. Up to version 1.6, the
            // java.util.jar run-time throws a spurious IOException if the
            // length of the signature file is a multiple of 1024 bytes.
            if ((signatureResultStream1.size() + signatureResultStream2.size())
                % 1024 == 0)
            {
                signatureWriter2.println();
                signatureWriter2.flush();
            }

            // Get the contents of the signature file (main section and file
            // sections) and write them out to META-INF/CERT.SF
            byte[] signatureBytes1 = signatureResultStream1.toByteArray();
            byte[] signatureBytes2 = signatureResultStream2.toByteArray();
            signatureFileStream.write(signatureBytes1);
            signatureFileStream.write(signatureBytes2);
            signatureFileStream.close();

            // Get the contents of the signature file, sign them digitally and
            // write them out to META-INF/CERT.RSA (or similar).
            try
            {
                // Get the digitally signed contents.
                digitalSignature.update(signatureBytes1);
                digitalSignature.update(signatureBytes2);
                byte[] digitalSignatureBytes = digitalSignature.sign();

                // Get the certificate.
                X509Certificate certificate = (X509Certificate)privateKeyEntry.getCertificate();

                // Write them out in a PKCS7 container.
                String  encryptionAlgorithm = privateKeyEntry.getPrivateKey().getAlgorithm();

                if (SUN_SECURITY_SIGNING)
                {
                    // Using the internal sun.security API.
                    SignerInfo signerInfo =
                        new SignerInfo(new X500Name(certificate.getIssuerX500Principal().getName()),
                                       certificate.getSerialNumber(),
                                       AlgorithmId.get(digestAlgorithm),
                                       AlgorithmId.get(encryptionAlgorithm),
                                       digitalSignatureBytes);

                    PKCS7 pkcs7 =
                        new PKCS7(new AlgorithmId[] { AlgorithmId.get(digestAlgorithm) },
                                  new ContentInfo(ContentInfo.DATA_OID, null),
                                  new X509Certificate[] { certificate },
                                  new SignerInfo[] { signerInfo });

                    pkcs7.encodeSignedData(digitalSignatureFileStream);
                    digitalSignatureFileStream.close();
                }
                else
                {
                    // Using our own implementation.
                    PKCS7OutputStream pkcs7OutputStream =
                        new PKCS7OutputStream(
                        new DEROutputStream(digitalSignatureFileStream));

                    pkcs7OutputStream.writeSignature(certificate,
                                                     digestAlgorithm,
                                                     encryptionAlgorithm,
                                                     digitalSignatureBytes);
                    pkcs7OutputStream.close();
                }
            }
            catch (IOException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new UnsupportedOperationException(e.getMessage(), e);
            }

            signatureFileStream    = null;
            signatureWriter1       = null;
            signatureWriter2       = null;
            signatureResultStream1 = null;
            signatureResultStream2 = null;

            digitalSignatureFileStream = null;
        }
    }


    /**
     * Provides a simple test for this class, creating a signed apk file (only
     * v1) with the given name and a few aligned/compressed/uncompressed
     * zip entries.
     * Arguments:
     *     keystore keystore_password alias password jar_filename
     * Create a keystore with:
     *     keytool -genkey -dname 'CN=John Doe, OU=Development, O=GuardSquare, STREET=Tervuursevest, L=Leuven, ST=Brabant, C=Belgium' -keystore /tmp/test.keystore -storepass android -alias AndroidDebugKey -keypass android -keyalg RSA -keysize 512
     * List the contents of the keystore with
     *     keytool -v -list -keystore /tmp/test.keystore -alias androiddebugkey -storepass android
     * Verify the signed jar with:
     *     jarsigner -verify -verbose /tmp/test.jar
     * or with:
     *     android-sdk/build-tools/24.0.3/apksigner verify --min-sdk-version 19 -v /tmp/test.jar
     */
    public static void main(String[] args)
    {
        try
        {
            // Get the arguments.
            String keyStoreFileName = args[0];
            String keyStorePassword = args[1];
            String keyAlias         = args[2];
            String keyPassword      = args[3];
            String jarFileName      = args[4];

            // Get the private key from the key store.
            FileInputStream keyStoreInputStream =
                new FileInputStream(keyStoreFileName);

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
            keyStoreInputStream.close();

            KeyStore.ProtectionParameter protectionParameter =
                new KeyStore.PasswordProtection(keyPassword.toCharArray());

            KeyStore.PrivateKeyEntry privateKeyEntry =
                 (KeyStore.PrivateKeyEntry)keyStore.getEntry(keyAlias,
                                                             protectionParameter);

            // Start writing out the signed jar file.
            DataEntryWriter writer =
                new SignedJarWriter(privateKeyEntry, new String[] { DEFAULT_DIGEST_ALGORITHM }, "ProGuard", null,
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
