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
import java.security.cert.*;
import java.util.*;

/**
 * This output stream provides methods to write signature data in PKCS7 format
 * (= Cryptographic Message Syntax (CMS), part of the Public Key Cryptography
 * Standards) to an underlying DER output stream.
 *
 * This is for example the format of signature files like META-INF/CERT.RSA
 *
 * You can view the resulting file with
 *     openssl asn1parse -inform DER -in META-INF/CERT.RSA
 *
 * You can check a signed jar with
 *     jarsigner -verify -verbose example.jar
 *
 * @author Eric Lafortune
 */
public class PKCS7OutputStream
{
    private static final int  PKCS7_VERSION  = 1;
    private static final int  SIGNER_VERSION = 1;

    private static final byte[] DATA_CONTENT  = { 0x2a, (byte)0x86, 0x48, (byte)0x86, (byte)0xf7, 0xd, 0x1, 0x7, 0x1 }; // 1.2.840.113549.1.7.1
    private static final byte[] SDATA_CONTENT = { 0x2a, (byte)0x86, 0x48, (byte)0x86, (byte)0xf7, 0xd, 0x1, 0x7, 0x2 }; // 1.2.840.113549.1.7.2

    private static final byte[] SHA1_DIGEST_ALGORITHM_ID    = { 0x2b, 0xe, 0x3, 0x2, 0x1a }; // 1.3.14.3.2.26
    private static final byte[] SHA256_DIGEST_ALGORITHM_ID  = { 0x60, (byte)0x86, 0x48, 0x1, 0x65, 0x3, 0x4, 0x2, 0x1 }; // 2.16.840.1.101.3.4.2.1
    private static final byte[] SHA384_DIGEST_ALGORITHM_ID  = { 0x60, (byte)0x86, 0x48, 0x1, 0x65, 0x3, 0x4, 0x2, 0x2 }; // 2.16.840.1.101.3.4.2.2
    private static final byte[] SHA512_DIGEST_ALGORITHM_ID  = { 0x60, (byte)0x86, 0x48, 0x1, 0x65, 0x3, 0x4, 0x2, 0x3 }; // 2.16.840.1.101.3.4.2.3

    private static final byte[] RSA_ENCRYPTION_ALGORITHM_ID = { 0x2a, (byte)0x86, 0x48, (byte)0x86, (byte)0xf7, 0xd, 0x1, 0x1, 0x1 }; // 1.2.840.113549.1.1.1
    private static final byte[] DH_ENCRYPTION_ALGORITHM_ID  = { 0x2a, (byte)0x86, 0x48, (byte)0x86, (byte)0xf7, 0xd, 0x1, 0x3, 0x1 }; // 1.2.840.113549.1.3.1
    private static final byte[] DSA_ENCRYPTION_ALGORITHM_ID = { 0x2a, (byte)0x86, 0x48, (byte)0xce, 0x38, 0x4, 0x1 };                 // 1.2.840.10040.4.1
    private static final byte[] EC_ENCRYPTION_ALGORITHM_ID  = { 0x2a, (byte)0x86, 0x48, (byte)0xce, 0x3d, 0x2, 0x1 };                 // 1.2.840.10045.2.1

    private static final byte[] COMMON_NAME_OBJECT_ID       = { 0x55, 0x04, 0x03 }; // 2.5.4.3
    private static final byte[] SURNAME_OBJECT_ID           = { 0x55, 0x04, 0x04 }; // 2.5.4.4
    private static final byte[] SERIAL_NUMBER_OBJECT_ID     = { 0x55, 0x04, 0x05 }; // 2.5.4.5
    private static final byte[] COUNTRY_NAME_OBJECT_ID      = { 0x55, 0x04, 0x06 }; // 2.5.4.6
    private static final byte[] LOCALITY_NAME_OBJECT_ID     = { 0x55, 0x04, 0x07 }; // 2.5.4.7
    private static final byte[] STATE_NAME_OBJECT_ID        = { 0x55, 0x04, 0x08 }; // 2.5.4.8
    private static final byte[] STREET_ADDRESS_OBJECT_ID    = { 0x55, 0x04, 0x09 }; // 2.5.4.9
    private static final byte[] ORGANIZATION_NAME_OBJECT_ID = { 0x55, 0x04, 0x0a }; // 2.5.4.10
    private static final byte[] UNIT_NAME_OBJECT_ID         = { 0x55, 0x04, 0x0b }; // 2.5.4.11
    private static final byte[] TITLE_OBJECT_ID             = { 0x55, 0x04, 0x0c }; // 2.5.4.12
    private static final byte[] GIVEN_NAME_OBJECT_ID        = { 0x55, 0x04, 0x2a }; // 2.5.4.42
    private static final byte[] EMAIL_ADDRESS_OBJECT_ID     = { 0x2a, (byte)0x86, 0x48, (byte)0x86, (byte)0xf7, 0xd, 0x1, 0x9, 0x1 }; // 1.2.840.113549.1.9.1

    private static final String COMMON_NAME_ATTRIBUTE       = "CN";
    private static final String SURNAME_ATTRIBUTE           = "SN"; // Not supported by keytool.
    private static final String SERIAL_NUMBER_ATTRIBUTE     = "SERIALNUMBER";
    private static final String COUNTRY_NAME_ATTRIBUTE      = "C";
    private static final String LOCALITY_NAME_ATTRIBUTE     = "L";
    private static final String STATE_NAME_ATTRIBUTE        = "ST";
    private static final String STREET_ADDRESS_ATTRIBUTE    = "STREET";
    private static final String ORGANIZATION_NAME_ATTRIBUTE = "O";
    private static final String UNIT_NAME_ATTRIBUTE         = "OU";
    private static final String TITLE_ATTRIBUTE             = "T";
    private static final String GIVEN_NAME_ATTRIBUTE        = "GN"; // Not supported by keytool.
    private static final String EMAIL_ADDRESS_ATTRIBUTE     = "EMAILADDRESS";


    private final DEROutputStream derOutputStream;


    /**
     * Creates a new PKCS7OutputStream.
     * @param derOutputStream the DER output stream to which data will be
     *                        written.
     */
    public PKCS7OutputStream(DEROutputStream derOutputStream)
    {
        this.derOutputStream = derOutputStream;
    }


    /**
     * Writes the given signature data.
     */
    public void writeSignature(X509Certificate certificate,
                               String          digestAlgorithm,
                               String          encryptionAlgorithm,
                               byte[]          digitalSignatureBytes)
    throws IOException, CertificateEncodingException
    {
        derOutputStream.startSequence();
        {
            // Write the content type.
            derOutputStream.writeObjectIdentifier(SDATA_CONTENT);

            derOutputStream.startImplicit();
            {
                derOutputStream.startSequence();

                // Write the PKCS7 version.
                derOutputStream.writeInteger(PKCS7_VERSION);

                // Write the signing algorithm.
                derOutputStream.startSet();
                writeNullAttribute(encodedDigestAlgorithmOid(digestAlgorithm));
                derOutputStream.endSet();

                // Write the content type.
                derOutputStream.startSequence();
                derOutputStream.writeObjectIdentifier(DATA_CONTENT);
                derOutputStream.endSequence();

                // Write the encoded certificate.
                derOutputStream.writeImplicit(certificate.getEncoded());

                // Write the signature itself.
                writeSignerSingleton(certificate,
                                     digestAlgorithm,
                                     encryptionAlgorithm,
                                     digitalSignatureBytes);

                derOutputStream.endSequence();
            }
            derOutputStream.endImplicit();
        }
        derOutputStream.endSequence();
    }


    public void flush() throws IOException
    {
        derOutputStream.flush();
    }


    public void close() throws IOException
    {
        derOutputStream.close();
    }


    // Small utility methods.

    /**
     * Writes a set with a single signature for the given certificate.
     */
    private void writeSignerSingleton(X509Certificate certificate,
                                      String          digestAlgorithm,
                                      String          encryptionAlgorithm,
                                      byte[]          digitalSignatureBytes)
    throws IOException
    {
        derOutputStream.startSet();

        writeSigner(certificate,
                    digestAlgorithm,
                    encryptionAlgorithm,
                    digitalSignatureBytes);

        derOutputStream.endSet();
    }


    /**
     * Writes a signature for the given certificate.
     */
    private void writeSigner(X509Certificate certificate,
                             String          digestAlgorithm,
                             String          encryptionAlgorithm,
                             byte[]          digitalSignatureBytes)
    throws IOException
    {
        derOutputStream.startSequence();

        // Write the signer version.
        derOutputStream.writeInteger(SIGNER_VERSION);

        // Write the relevant info from the certificate.
        writeSignerCertificateInfo(certificate);

        // Write the digest and signing algorithms (hardcoded).
        writeNullAttribute(encodedDigestAlgorithmOid(digestAlgorithm));
        writeNullAttribute(encodedEncryptionAlgorithmOid(encryptionAlgorithm));

        // Write the signature bytes themselves.
        derOutputStream.writeOctetString(digitalSignatureBytes);

        derOutputStream.endSequence();
    }


    /**
     * Writes the certificate part of the signer info.
     */
    private void writeSignerCertificateInfo(X509Certificate certificate)
    throws IOException
    {
        derOutputStream.startSequence();

        {
            derOutputStream.startSequence();

            // We need the distinguished name of the issuer,
            // which might be different from the name of the subject.
            String distinguishedName = certificate.getIssuerDN().getName();

            // Apparently, the attribute sequence has to be in the reverse
            // order of the comma-separated list in the distinguished name.
            // The "jarsigner -verify" prints out "java.lang.SecurityException:
            // cannot verify signature block file META-INF/CERT" if the order,
            // the contents, or the even the string formats are wrong.

            // Start parsing at the end.
            int endIndex = distinguishedName.length();
            do
            {
                // We have to strip any quotes from attribute values.
                // Quoted attribute values may contain commas.
                boolean quoted = distinguishedName.charAt(endIndex-1) == '"';

                int commaEndIndex = quoted ?
                    distinguishedName.lastIndexOf('"', endIndex-2) :
                    endIndex - 1;

                // Extract the attribute name/value pair.
                int    commaIndex = distinguishedName.lastIndexOf(',', commaEndIndex);
                String attribute  = distinguishedName.substring(commaIndex+1, endIndex);

                // Extract the name and the value from the attribute.
                // Apparently, the value can contain unquoted control characters
                // (e.g. EMAILADDRESS=^Vbustia.mobilitat@caixabank.com).
                int    equalsIndex    = attribute.indexOf('=');
                String attributeName  = attribute.substring(0, equalsIndex).trim();
                String attributeValue = quoted ?
                    attribute.substring(equalsIndex+2, attribute.length()-1) :
                    trimSpaces(attribute.substring(equalsIndex + 1));

                // Write out the name and value.
                // Note that the email address needs to be written out as an
                // IA5 string.
                if      (attributeName.equals(COMMON_NAME_ATTRIBUTE      )) writeStringAttributeSingleton(COMMON_NAME_OBJECT_ID      , attributeValue, false);
                else if (attributeName.equals(SURNAME_ATTRIBUTE          )) writeStringAttributeSingleton(SURNAME_OBJECT_ID          , attributeValue, false);
                else if (attributeName.equals(SERIAL_NUMBER_ATTRIBUTE    )) writeStringAttributeSingleton(SERIAL_NUMBER_OBJECT_ID    , attributeValue, false);
                else if (attributeName.equals(COUNTRY_NAME_ATTRIBUTE     )) writeStringAttributeSingleton(COUNTRY_NAME_OBJECT_ID     , attributeValue, false);
                else if (attributeName.equals(LOCALITY_NAME_ATTRIBUTE    )) writeStringAttributeSingleton(LOCALITY_NAME_OBJECT_ID    , attributeValue, false);
                else if (attributeName.equals(STATE_NAME_ATTRIBUTE       )) writeStringAttributeSingleton(STATE_NAME_OBJECT_ID       , attributeValue, false);
                else if (attributeName.equals(STREET_ADDRESS_ATTRIBUTE   )) writeStringAttributeSingleton(STREET_ADDRESS_OBJECT_ID   , attributeValue, false);
                else if (attributeName.equals(ORGANIZATION_NAME_ATTRIBUTE)) writeStringAttributeSingleton(ORGANIZATION_NAME_OBJECT_ID, attributeValue, false);
                else if (attributeName.equals(UNIT_NAME_ATTRIBUTE        )) writeStringAttributeSingleton(UNIT_NAME_OBJECT_ID        , attributeValue, false);
                else if (attributeName.equals(TITLE_ATTRIBUTE            )) writeStringAttributeSingleton(TITLE_OBJECT_ID            , attributeValue, false);
                else if (attributeName.equals(GIVEN_NAME_ATTRIBUTE       )) writeStringAttributeSingleton(GIVEN_NAME_OBJECT_ID       , attributeValue, false);
                else if (attributeName.equals(EMAIL_ADDRESS_ATTRIBUTE    )) writeStringAttributeSingleton(EMAIL_ADDRESS_OBJECT_ID    , attributeValue, true);

                endIndex = commaIndex;
            }
            while (endIndex > 0);

            derOutputStream.endSequence();
        }

        // Certificate serial number.
        derOutputStream.writeInteger(certificate.getSerialNumber());

        derOutputStream.endSequence();
    }


    /**
     * Returns the given string with any leading or trailing spaces trimmed.
     */
    private String trimSpaces(String string)
    {
        // Find the first non-space character.
        int startIndex = 0;
        while (startIndex < string.length() && string.charAt(startIndex) == ' ')
        {
            startIndex++;
        }

        // Find the last non-space character.
        int endIndex = string.length();
        while (endIndex >= startIndex && string.charAt(endIndex - 1) == ' ')
        {
            endIndex--;
        }

        return string.substring(startIndex, endIndex);
    }


    /**
     * Writes a X.509 Relative-Distinguished-Name (RDN, a set of attributes),
     * with the specified X.500 Attribute-Value-Assertion (AVA, an attribute).
     */
    private void writeStringAttributeSingleton(byte[]  attributeIdentifier,
                                               String  attributeValue,
                                               boolean asIA5String)
    throws IOException
    {
        derOutputStream.startSet();

        // Write just a single attribute.
        writeStringAttribute(attributeIdentifier, attributeValue, asIA5String);

        derOutputStream.endSet();
    }


    /**
     * Writes the specified X.500 Attribute-Value-Assertion (AVA),
     * with string value.
     */
    private void writeStringAttribute(byte[]  attributeIdentifier,
                                      String  attributeValue,
                                      boolean asIA5String)
    throws IOException
    {
        derOutputStream.startSequence();
        derOutputStream.writeObjectIdentifier(attributeIdentifier);
        derOutputStream.writeString(attributeValue, asIA5String);
        derOutputStream.endSequence();
    }


    /**
     * Writes the specified X.500 Attribute-Value-Assertion (AVA),
     * with a null value.
     */
    private void writeNullAttribute(byte[] attributeName) throws IOException
    {
        derOutputStream.startSequence();
        derOutputStream.writeObjectIdentifier(attributeName);
        derOutputStream.writeNull();
        derOutputStream.endSequence();
    }


    /**
     * Returns the encoded object ID of the specified digest algorithm.
     */
    private byte[] encodedDigestAlgorithmOid(String digestAlgorithm)
    {
        return
            digestAlgorithm.equals("SHA")    ||
            digestAlgorithm.equals("SHA1")   ||
            digestAlgorithm.equals("SHA-1")   ? SHA1_DIGEST_ALGORITHM_ID   :
            digestAlgorithm.equals("SHA256") ||
            digestAlgorithm.equals("SHA-256") ? SHA256_DIGEST_ALGORITHM_ID :
            digestAlgorithm.equals("SHA384") ||
            digestAlgorithm.equals("SHA-384") ? SHA384_DIGEST_ALGORITHM_ID :
            digestAlgorithm.equals("SHA512") ||
            digestAlgorithm.equals("SHA-512") ? SHA512_DIGEST_ALGORITHM_ID :
            throwNewIllegalArgumentException("Unsupported digest algorithm ["+digestAlgorithm+"]");
    }


    /**
     * Returns the encoded object ID of the specified encryption algorithm.
     */
    private byte[] encodedEncryptionAlgorithmOid(String encryptionAlgorithm)
    {
        return
            encryptionAlgorithm.equals("RSA")            ? RSA_ENCRYPTION_ALGORITHM_ID :
            encryptionAlgorithm.equals("DH") ||
            encryptionAlgorithm.equals("Diffie-Hellman") ? DH_ENCRYPTION_ALGORITHM_ID  :
            encryptionAlgorithm.equals("DSA")            ? DSA_ENCRYPTION_ALGORITHM_ID :
            encryptionAlgorithm.equals("EC")             ? EC_ENCRYPTION_ALGORITHM_ID  :
            throwNewIllegalArgumentException("Unsupported encryption algorithm ["+encryptionAlgorithm+"]");
    }


    /**
     * Throws an IllegalArgumentException with the given message.
     */
    private byte[] throwNewIllegalArgumentException(String message)
    {
        throw new IllegalArgumentException(message);
    }


//    /**
//     * Writes out encoded objects IDs of algorithms.
//     */
//    public static void main(String[] args)
//    {
//        ObjectIdentifier[] oids =
//        {
//            AlgorithmId.SHA_oid,
//            AlgorithmId.SHA256_oid,
//            AlgorithmId.SHA384_oid,
//            AlgorithmId.SHA512_oid,
//
//            AlgorithmId.RSAEncryption_oid,
//            AlgorithmId.DH_oid,
//            AlgorithmId.DSA_oid,
//            AlgorithmId.EC_oid,
//        };
//
//        try
//        {
//            Field field = ObjectIdentifier.class.getDeclaredField("encoding");
//            field.setAccessible(true);
//
//            for (int oidIndex = 0; oidIndex < oids.length; oidIndex++)
//            {
//                ObjectIdentifier oid = oids[oidIndex];
//
//                System.out.print("{ ");
//                byte[] bytes = (byte[])field.get(oid);
//                for (int byteIndex = 0; byteIndex < bytes.length; byteIndex++)
//                {
//                    byte b = bytes[byteIndex];
//                    System.out.print((b < 0 ? "(byte)":"") + "0x" + Integer.toHexString(b & 0xff) + ", ");
//                }
//                System.out.println("}; // "+oid);
//            }
//        }
//        catch (IllegalAccessException e)
//        {
//            e.printStackTrace();
//        }
//        catch (NoSuchFieldException e)
//        {
//            e.printStackTrace();
//        }
//    }


    /**
     * Reads a certificate with java.security.cert and writes a copy with this class.
     * For example:
     *     java proguard.io.PKCS7OutputStream META-INF/CERT.RSA copy.RSA
     *
     * Useful to debug incorrect writing of certificates.
     * For example:
     *     openssl asn1parse -inform DER -in META-INF/CERT.RSA
     *     openssl asn1parse -inform DER -in copy.RSA
     */
    public static void main(String[] args)
    {
        String inputCertificateFileName  = args[0];
        String outputCertificateFileName = args[1];

        try
        {
            System.out.println("Reading ["+inputCertificateFileName+"]");

            InputStream        inputStream        = new FileInputStream(inputCertificateFileName);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection         certificates       = certificateFactory.generateCertificates(inputStream);

            int      counter  = 0;
            Iterator iterator = certificates.iterator();
            while (iterator.hasNext())
            {
                X509Certificate cert509 = (X509Certificate) iterator.next();
                System.out.println("IssuerDN:   " + cert509.getIssuerDN());
                System.out.println("SigAlgName: " + cert509.getSigAlgName());
                System.out.println("Signature:  " + Arrays.toString(cert509.getSignature()));

                String fileName = counter == 0 ?
                    outputCertificateFileName :
                    outputCertificateFileName + counter;

                System.out.println("Writing ["+fileName+"]");

                OutputStream      outputStream      = new FileOutputStream(fileName);
                DEROutputStream   derOutputStream   = new DEROutputStream(outputStream);
                PKCS7OutputStream pkcs7OutputStream = new PKCS7OutputStream(derOutputStream);

                String sigAlgName = cert509.getSigAlgName();
                int    withIndex  = sigAlgName.indexOf("with");

                String digestAlgorithm     = withIndex < 0 ? "SHA1" : sigAlgName.substring(0, withIndex);
                String encryptionAlgorithm = withIndex < 0 ? "RSA"  : sigAlgName.substring(withIndex + 4);

                pkcs7OutputStream.writeSignature(cert509,
                                                 digestAlgorithm,
                                                 encryptionAlgorithm,
                                                 new byte[cert509.getSignature().length]);
                pkcs7OutputStream.close();

                System.out.println();

                counter++;
            }

            inputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
