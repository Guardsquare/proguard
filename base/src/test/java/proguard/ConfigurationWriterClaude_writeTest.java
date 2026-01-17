package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationWriter#write(Configuration)} method.
 * Tests the writing of various ProGuard configuration options to output.
 */
public class ConfigurationWriterClaude_writeTest {

    /**
     * Tests the write method with an empty configuration.
     * Verifies that writing an empty configuration doesn't throw exceptions
     * and produces valid output.
     */
    @Test
    public void testWriteWithEmptyConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration writer and empty configuration
        File outputFile = tempDir.resolve("output-config.txt").toFile();
        Configuration config = new Configuration();

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify file was created
        assertTrue(outputFile.exists(), "Output file should exist after writing");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify content is readable
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertNotNull(content, "Output content should not be null");
    }

    /**
     * Tests the write method with verbose option enabled.
     * Verifies that the verbose option is correctly written to the output.
     */
    @Test
    public void testWriteWithVerboseOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with verbose enabled
        File outputFile = tempDir.resolve("verbose-config.txt").toFile();
        Configuration config = new Configuration();
        config.verbose = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify verbose option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-verbose"),
                   "Output should contain -verbose option");
    }

    /**
     * Tests the write method with shrink disabled.
     * Verifies that the dontshrink option is correctly written.
     */
    @Test
    public void testWriteWithDontShrinkOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with shrink disabled
        File outputFile = tempDir.resolve("dontshrink-config.txt").toFile();
        Configuration config = new Configuration();
        config.shrink = false;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify dontshrink option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-dontshrink"),
                   "Output should contain -dontshrink option");
    }

    /**
     * Tests the write method with optimize disabled.
     * Verifies that the dontoptimize option is correctly written.
     */
    @Test
    public void testWriteWithDontOptimizeOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with optimize disabled
        File outputFile = tempDir.resolve("dontoptimize-config.txt").toFile();
        Configuration config = new Configuration();
        config.optimize = false;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify dontoptimize option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-dontoptimize"),
                   "Output should contain -dontoptimize option");
    }

    /**
     * Tests the write method with obfuscate disabled.
     * Verifies that the dontobfuscate option is correctly written.
     */
    @Test
    public void testWriteWithDontObfuscateOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with obfuscate disabled
        File outputFile = tempDir.resolve("dontobfuscate-config.txt").toFile();
        Configuration config = new Configuration();
        config.obfuscate = false;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify dontobfuscate option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-dontobfuscate"),
                   "Output should contain -dontobfuscate option");
    }

    /**
     * Tests the write method with preverify disabled.
     * Verifies that the dontpreverify option is correctly written.
     */
    @Test
    public void testWriteWithDontPreverifyOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with preverify disabled
        File outputFile = tempDir.resolve("dontpreverify-config.txt").toFile();
        Configuration config = new Configuration();
        config.preverify = false;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify dontpreverify option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-dontpreverify"),
                   "Output should contain -dontpreverify option");
    }

    /**
     * Tests the write method with multiple boolean options enabled.
     * Verifies that all options are correctly written to output.
     */
    @Test
    public void testWriteWithMultipleBooleanOptions(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with multiple options
        File outputFile = tempDir.resolve("multiple-config.txt").toFile();
        Configuration config = new Configuration();
        config.verbose = true;
        config.shrink = false;
        config.optimize = false;
        config.obfuscate = false;
        config.ignoreWarnings = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify all options are in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-verbose"), "Output should contain -verbose");
        assertTrue(content.contains("-dontshrink"), "Output should contain -dontshrink");
        assertTrue(content.contains("-dontoptimize"), "Output should contain -dontoptimize");
        assertTrue(content.contains("-dontobfuscate"), "Output should contain -dontobfuscate");
        assertTrue(content.contains("-ignorewarnings"), "Output should contain -ignorewarnings");
    }

    /**
     * Tests the write method with a PrintWriter instead of File.
     * Verifies that writing to a PrintWriter works correctly.
     */
    @Test
    public void testWriteWithPrintWriter(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration and PrintWriter
        File outputFile = tempDir.resolve("printwriter-config.txt").toFile();
        Configuration config = new Configuration();
        config.verbose = true;

        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile));

        // Act - Write the configuration using PrintWriter constructor
        try (ConfigurationWriter writer = new ConfigurationWriter(printWriter)) {
            writer.write(config);
        }

        // Assert - Verify file was created and contains expected content
        assertTrue(outputFile.exists(), "Output file should exist after writing");
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-verbose"), "Output should contain -verbose option");
    }

    /**
     * Tests the write method with program jars (input).
     * Verifies that injars option is correctly written.
     */
    @Test
    public void testWriteWithProgramJarsInput(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with input jar
        File outputFile = tempDir.resolve("injars-config.txt").toFile();
        File inputJar = tempDir.resolve("input.jar").toFile();
        inputJar.createNewFile();

        Configuration config = new Configuration();
        config.programJars = new ClassPath();
        config.programJars.add(new ClassPathEntry(inputJar, false)); // false = input

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify injars option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-injars"),
                   "Output should contain -injars option");
        assertTrue(content.contains("input.jar"),
                   "Output should contain the jar filename");
    }

    /**
     * Tests the write method with program jars (output).
     * Verifies that outjars option is correctly written.
     */
    @Test
    public void testWriteWithProgramJarsOutput(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with output jar
        File outputFile = tempDir.resolve("outjars-config.txt").toFile();
        File outputJar = tempDir.resolve("output.jar").toFile();

        Configuration config = new Configuration();
        config.programJars = new ClassPath();
        config.programJars.add(new ClassPathEntry(outputJar, true)); // true = output

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify outjars option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-outjars"),
                   "Output should contain -outjars option");
        assertTrue(content.contains("output.jar"),
                   "Output should contain the jar filename");
    }

    /**
     * Tests the write method with library jars.
     * Verifies that libraryjars option is correctly written.
     */
    @Test
    public void testWriteWithLibraryJars(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with library jar
        File outputFile = tempDir.resolve("libraryjars-config.txt").toFile();
        File libraryJar = tempDir.resolve("library.jar").toFile();
        libraryJar.createNewFile();

        Configuration config = new Configuration();
        config.libraryJars = new ClassPath();
        config.libraryJars.add(new ClassPathEntry(libraryJar, false));

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify libraryjars option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-libraryjars"),
                   "Output should contain -libraryjars option");
        assertTrue(content.contains("library.jar"),
                   "Output should contain the jar filename");
    }

    /**
     * Tests the write method with skip non-public library classes option.
     * Verifies that the option is correctly written when enabled.
     */
    @Test
    public void testWriteWithSkipNonPublicLibraryClasses(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with skip option enabled
        File outputFile = tempDir.resolve("skip-config.txt").toFile();
        Configuration config = new Configuration();
        config.skipNonPublicLibraryClasses = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-skipnonpubliclibraryclasses"),
                   "Output should contain -skipnonpubliclibraryclasses option");
    }

    /**
     * Tests the write method with optimization passes.
     * Verifies that optimization passes option is correctly written.
     */
    @Test
    public void testWriteWithOptimizationPasses(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with custom optimization passes
        File outputFile = tempDir.resolve("optimizationpasses-config.txt").toFile();
        Configuration config = new Configuration();
        config.optimizationPasses = 5; // Default is 1, so != 1 should be written

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify optimization passes is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-optimizationpasses"),
                   "Output should contain -optimizationpasses option");
        assertTrue(content.contains("5"),
                   "Output should contain the pass count");
    }

    /**
     * Tests the write method with allow access modification option.
     * Verifies that the option is correctly written.
     */
    @Test
    public void testWriteWithAllowAccessModification(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with allow access modification
        File outputFile = tempDir.resolve("allowaccess-config.txt").toFile();
        Configuration config = new Configuration();
        config.allowAccessModification = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-allowaccessmodification"),
                   "Output should contain -allowaccessmodification option");
    }

    /**
     * Tests the write method with Android option.
     * Verifies that the android option is correctly written.
     */
    @Test
    public void testWriteWithAndroidOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with android enabled
        File outputFile = tempDir.resolve("android-config.txt").toFile();
        Configuration config = new Configuration();
        config.android = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify android option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-android"),
                   "Output should contain -android option");
    }

    /**
     * Tests that the write method properly handles IOException from writer.
     * This is tested by closing the writer before calling write, which should
     * cause an error condition that is detected by checkError().
     */
    @Test
    public void testWriteWithIOException(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration and a PrintWriter that will fail
        File outputFile = tempDir.resolve("fail-config.txt").toFile();
        Configuration config = new Configuration();
        config.verbose = true;

        // Create a PrintWriter that will be closed before writing
        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile));
        ConfigurationWriter writer = new ConfigurationWriter(printWriter);

        // Close the underlying writer to cause checkError() to return true
        printWriter.close();

        // Act & Assert - Writing should throw IOException
        assertThrows(IOException.class, () -> {
            writer.write(config);
        }, "Writing to a closed writer should throw IOException");
    }

    /**
     * Tests the write method with keep directories option.
     * Verifies that the keepdirectories option is correctly written.
     */
    @Test
    public void testWriteWithKeepDirectories(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with keep directories
        File outputFile = tempDir.resolve("keepdirectories-config.txt").toFile();
        Configuration config = new Configuration();
        config.keepDirectories = new ArrayList<>();
        config.keepDirectories.add("com/example/myapp/**");

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-keepdirectories"),
                   "Output should contain -keepdirectories option");
    }

    /**
     * Tests the write method with merge interfaces aggressively option.
     * Verifies that the option is correctly written.
     */
    @Test
    public void testWriteWithMergeInterfacesAggressively(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with merge interfaces enabled
        File outputFile = tempDir.resolve("mergeinterfaces-config.txt").toFile();
        Configuration config = new Configuration();
        config.mergeInterfacesAggressively = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-mergeinterfacesaggressively"),
                   "Output should contain -mergeinterfacesaggressively option");
    }

    /**
     * Tests the write method with overload aggressively option.
     * Verifies that the option is correctly written.
     */
    @Test
    public void testWriteWithOverloadAggressively(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with overload aggressively enabled
        File outputFile = tempDir.resolve("overloadaggressively-config.txt").toFile();
        Configuration config = new Configuration();
        config.overloadAggressively = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-overloadaggressively"),
                   "Output should contain -overloadaggressively option");
    }

    /**
     * Tests the write method with microedition option.
     * Verifies that the microedition option is correctly written.
     */
    @Test
    public void testWriteWithMicroEdition(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration with microedition enabled
        File outputFile = tempDir.resolve("microedition-config.txt").toFile();
        Configuration config = new Configuration();
        config.microEdition = true;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify option is in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-microedition"),
                   "Output should contain -microedition option");
    }

    /**
     * Tests the write method with real-world like configuration.
     * This test combines multiple options to simulate a realistic scenario.
     */
    @Test
    public void testWriteWithRealisticConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a realistic configuration
        File outputFile = tempDir.resolve("realistic-config.txt").toFile();
        File inputJar = tempDir.resolve("app.jar").toFile();
        File outputJar = tempDir.resolve("app-obfuscated.jar").toFile();
        File libraryJar = tempDir.resolve("android.jar").toFile();
        inputJar.createNewFile();
        libraryJar.createNewFile();

        Configuration config = new Configuration();

        // Set up jars
        config.programJars = new ClassPath();
        config.programJars.add(new ClassPathEntry(inputJar, false));
        config.programJars.add(new ClassPathEntry(outputJar, true));

        config.libraryJars = new ClassPath();
        config.libraryJars.add(new ClassPathEntry(libraryJar, false));

        // Set up options
        config.verbose = true;
        config.optimize = true;
        config.obfuscate = true;
        config.shrink = true;
        config.preverify = false;
        config.allowAccessModification = true;
        config.optimizationPasses = 3;

        // Act - Write the configuration
        try (ConfigurationWriter writer = new ConfigurationWriter(outputFile)) {
            writer.write(config);
        }

        // Assert - Verify key options are in output
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertTrue(content.contains("-verbose"), "Should contain -verbose");
        assertTrue(content.contains("-injars"), "Should contain -injars");
        assertTrue(content.contains("-outjars"), "Should contain -outjars");
        assertTrue(content.contains("-libraryjars"), "Should contain -libraryjars");
        assertTrue(content.contains("-dontpreverify"), "Should contain -dontpreverify");
        assertTrue(content.contains("-allowaccessmodification"), "Should contain -allowaccessmodification");
        assertTrue(content.contains("-optimizationpasses"), "Should contain -optimizationpasses");
        assertTrue(content.contains("app.jar"), "Should contain input jar name");
        assertTrue(content.contains("app-obfuscated.jar"), "Should contain output jar name");
        assertTrue(content.contains("android.jar"), "Should contain library jar name");
    }
}
