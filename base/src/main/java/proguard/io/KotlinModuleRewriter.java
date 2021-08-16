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
package proguard.io;

import kotlinx.metadata.KmAnnotation;
import kotlinx.metadata.internal.metadata.jvm.deserialization.JvmMetadataVersion;
import kotlinx.metadata.jvm.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class KotlinModuleRewriter
extends DataEntryCopier
{
    private static final Logger logger = LogManager.getLogger(KotlinModuleRewriter.class);

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
            logger.error("Cannot handle annotations yet");
        }


        public void visitEnd() {}
    }
}
