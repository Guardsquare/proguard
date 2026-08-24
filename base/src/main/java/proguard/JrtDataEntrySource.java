package proguard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import proguard.io.DataEntryReader;
import proguard.io.DataEntrySource;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * Data entry source which reads JDK classes using the {@code jrt:/} file system.
 *
 * @see <a href="https://openjdk.org/jeps/220">JEP 220</a>
 */
public class JrtDataEntrySource implements DataEntrySource, Closeable {
    @Nullable
    private final String moduleName;
    private final FileSystem jrtFileSystem;
    private final URLClassLoader fallbackClassLoader;

    /**
     * @param javaHome path to JAVA_HOME whose classes should be read
     * @param moduleName name of the module whose classes should be read; {@code null} to read the classes of
     *                   all modules
     */
    public JrtDataEntrySource(Path javaHome, @Nullable String moduleName) {
        this.moduleName = moduleName;

        Path jrtFsJarPath = getJrtFsJarPath(javaHome);
        if (!Files.isRegularFile(jrtFsJarPath)) {
            throw new IllegalArgumentException("Missing file in JAVA_HOME: " + jrtFsJarPath);
        }
        try {
            // This class loader is used as fallback when the current JDK is JDK 8 and does not itself have the
            // provider for the `jrt:/` file system
            fallbackClassLoader = new URLClassLoader(new URL[] {jrtFsJarPath.toUri().toURL()});
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed converting file path to URL", e);
        }

        try {
            jrtFileSystem = FileSystems.newFileSystem(URI.create("jrt:/"), Collections.singletonMap("java.home", javaHome.toString()), fallbackClassLoader);
        } catch (Exception e) {
            // Should not happen normally
            throw new RuntimeException("Failed creating jrt:/ file system for JAVA_HOME " + javaHome);
        }
    }

    @Override
    public void pumpDataEntries(DataEntryReader dataEntryReader) throws IOException {
        Path modulesPath = jrtFileSystem.getPath("modules");
        if (moduleName != null) {
            Path moduleDir = modulesPath.resolve(moduleName);
            if (!Files.isDirectory(moduleDir)) {
                throw new IOException("Module '" + moduleName + "' does not exist");
            }
            processModuleFiles(moduleDir, dataEntryReader);
        } else {
            try (Stream<Path> moduleDirs = Files.list(modulesPath)) {
                for (Path moduleDir : (Iterable<? extends Path>) moduleDirs::iterator) {
                    processModuleFiles(moduleDir, dataEntryReader);
                }
            }
        }
    }

    private void processModuleFiles(Path moduleDir, DataEntryReader dataEntryReader) throws IOException {
        Files.walkFileTree(moduleDir, new SimpleFileVisitor<Path>() {
            @Override
            public @NotNull FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                String name = moduleDir.relativize(file).toString();
                dataEntryReader.read(new JrtDataEntry(file, name, attrs.size()));
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void close() throws IOException {
        jrtFileSystem.close();
        // Note: Closing the class loader might not be thread-safe because by default the JDK caches the underlying
        //   JarFile, so closing it implicitly here might affect other users of that jar
        fallbackClassLoader.close();
    }

    /**
     * Gets the path for the {@code jrt-fs.jar}.
     */
    public static Path getJrtFsJarPath(Path javaHome) {
        return javaHome.resolve("lib").resolve("jrt-fs.jar");
    }
}
