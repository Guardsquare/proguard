## Streaming classes from a jar file

You can read classes from class files and various types of (nested) jar files
or jmod files, with some convenient utility classes and visitors. For example,
you can read the classes from a jar file and print them out in a streaming
fashion, without collecting their representations:

    DirectoryPump directoryPump =
        new DirectoryPump(
        new File(inputJarFileName));

    directoryPump.pumpDataEntries(
        new JarReader(
        new ClassFilter(
        new ClassReader(false, false, false, false, null,
        new ClassPrinter()))));

Note the constructor-based dependency injection of visitor classes. We
typically use a slightly unconventional indentation to make this construct
easy to read.

Complete example: PrintClasses.java

## Writing out streamed classes

You can read classes, optionally perform some small modifications, and write
them out right away, again in a streaming fashion.

    JarWriter jarWriter =
        new JarWriter(
        new ZipWriter(
        new FixedFileWriter(
        new File(outputJarFileName))));
    
    DirectoryPump directoryPump =
        new DirectoryPump(
        new File(inputJarFileName));
    
    directoryPump.pumpDataEntries(
        new JarReader(
        new ClassFilter(
        new ClassReader(false, false, false, false, null,
        new DataEntryClassWriter(jarWriter)))));
    
    jarWriter.close();

Complete example: ApplyPeepholeOptimizations.java

## Collecting classes

Alternatively, you may want to collect the classes in a so-called class pool
first, so you can perform more extensive analyses on them:

    ClassPool classPool = new ClassPool();

    DirectoryPump directoryPump =
        new DirectoryPump(
        new File(jarFileName));

    directoryPump.pumpDataEntries(
        new JarReader(false,
        new ClassFilter(
        new ClassReader(false, false, false, false, null,
        new ClassPoolFiller(classPool)))));

Complete example: Preverify.java

## Writing out a set of classes

If you've collected a set of classes in a class pool, you can write them out
with the same visitors as before.

    JarWriter jarWriter =
        new JarWriter(
        new ZipWriter(
        new FixedFileWriter(
        new File(outputJarFileName))));

    classPool.classesAccept(
        new DataEntryClassWriter(jarWriter));

    jarWriter.close();

Complete example: Preverify.java
