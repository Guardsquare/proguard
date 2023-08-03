package proguard.optimize.inline.lambda_locator;

import proguard.classfile.visitor.ClassPrinter;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassPoolFiller;
import proguard.io.*;
import proguard.io.util.IOUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Util {
    /*public static ClassPool loadApk(String filename) throws IOException {
        return IOUtil.read(
                new File(filename),
                false,
                false,
                (dataEntryReader, classVisitor) -> new NameFilteredDataEntryReader(
                        "classes*.dex",
                        new DexClassReader(
                                true,
                                classVisitor
                        ),
                        dataEntryReader)
        );
    }*/

    public static ClassPool loadJar(String filename) throws IOException {
        ClassPool classPool = new ClassPool();

        DataEntrySource source =
                new FileSource(
                        new File(filename));

        source.pumpDataEntries(
                new JarReader(false,
                        new ClassFilter(
                                new ClassReader(false, false, false, false, null,
                                        new ClassPoolFiller(classPool)))));
        return classPool;
    }

    public static String getMainClassFromJar(String filename) throws IOException {
        String mainClass = null;
        ZipFile zipFile = new ZipFile(filename);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filename), StandardCharsets.UTF_8);
        while (true) {
            ZipEntry entry = zipInputStream.getNextEntry();
            if (entry == null) break;

            if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                System.out.println(entry);
                InputStream iStream = zipFile.getInputStream(entry);
                BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;

                    if (line.startsWith("Main-Class: "))
                        mainClass = line.substring(line.indexOf(':')+1).trim();
                }
            }
        }
        zipInputStream.close();
        zipFile.close();
        return mainClass;
    }

    public static void writeJar(ClassPool programClassPool, String outputJarFileName) throws IOException {
        JarWriter jarWriter =
                new JarWriter(
                        new ZipWriter(
                                new FixedFileWriter(
                                        new File(outputJarFileName))));

        programClassPool.classesAccept(
                new DataEntryClassWriter(jarWriter));

        jarWriter.close();
    }

    public static void printClass(ClassPool classPool, String className) {
        classPool.classAccept(className, new ClassPrinter());
    }
}
