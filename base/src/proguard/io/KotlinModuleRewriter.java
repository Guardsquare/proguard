/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.io;

import kotlinx.metadata.KmAnnotation;
import kotlinx.metadata.internal.metadata.jvm.deserialization.JvmMetadataVersion;
import kotlinx.metadata.jvm.*;
import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class KotlinModuleRewriter
extends DataEntryCopier
{

    private final ClassPool programClassPool;


    public KotlinModuleRewriter(ClassPool programClassPool, Charset charset, DataEntryWriter writer)
    {
        super(writer);
        this.programClassPool = programClassPool;
    }


    public void read(DataEntry dataEntry) throws IOException
    {
        super.read(dataEntry);
    }


    protected void copyData(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        super.copyData(inputStream, byteStream);
        byte[]               bytes                = byteStream.toByteArray();
        KotlinModuleMetadata kotlinModuleMetadata = KotlinModuleMetadata.read(bytes);
        KmModule             kmModule             = kotlinModuleMetadata.toKmModule();

        ModuleTransformer moduleTransformer = new ModuleTransformer();
        kmModule.accept(moduleTransformer);

        KotlinModuleMetadata.Writer writer = new KotlinModuleMetadata.Writer();
        moduleTransformer.getResult().accept(writer);
        byte[] transformedBytes = writer.write(JvmMetadataVersion.INSTANCE.toArray()).getBytes();
        outputStream.write(transformedBytes);
    }


    private class PackageInformation
    {
        private final String              fqName;
        private final List<String>        fileFacades         = new ArrayList<>();
        private final Map<String, String> multiFileClassParts = new HashMap<>();


        private PackageInformation(String fqName) {this.fqName = fqName;}


        public void addToModule(KmModule out)
        {
            out.visitPackageParts(fqName, fileFacades, multiFileClassParts);
        }
    }

    private class ModuleTransformer extends KmModuleVisitor
    {
        private final Map<String, PackageInformation> newModuleInfo = new HashMap<>();


        private PackageInformation getPackageInformation(String fqName)
        {
            if (newModuleInfo.containsKey(fqName))
            {
                return newModuleInfo.get(fqName);
            }
            else
            {
                PackageInformation packageInformation = new PackageInformation(fqName);
                newModuleInfo.put(fqName, packageInformation);
                return packageInformation;
            }
        }


        private PackageInformation getPackageInformation(Clazz clazz)
        {
            return getPackageInformation(ClassUtil.externalPackageName(ClassUtil.externalClassName(clazz.getName())));
        }


        public void visitPackageParts(String fqName, List<String> fileFacades, Map<String, String> multiFileClassParts)
        {
            for (String fileFacade : fileFacades)
            {
                Clazz newClass = programClassPool.getClass(fileFacade);

                if (newClass == null)
                {
                    // This can occur in the case of wrong input modules.
                    // For instance the kotlin.reflect module declares facades which are actually absent.
                    continue;
                }

                getPackageInformation(newClass).fileFacades.add(newClass.getName());
            }

            for (Map.Entry<String, String> entry : multiFileClassParts.entrySet())
            {
                Clazz  keyClass   = programClassPool.getClass(entry.getKey());
                Clazz  valueClass = programClassPool.getClass(entry.getValue());

                if (keyClass   == null ||
                    valueClass == null)
                {
                    continue;
                }

                getPackageInformation(keyClass).multiFileClassParts.put(keyClass  .getName(),
                                                                        valueClass.getName());
            }
        }


        public KmModule getResult()
        {
            KmModule out = new KmModule();
            for (PackageInformation packageInformation : newModuleInfo.values())
            {
                packageInformation.addToModule(out);
            }
            return out;
        }


        public void visitAnnotation(KmAnnotation annotation)
        {
            System.err.println("Cannot handle annotations yet");
        }


        public void visitEnd() {}
    }
}
