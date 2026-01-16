/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#extraJar(File)}.
 *
 * This test class verifies that the extraJar(File) method correctly sets
 * the extra jar file in the configuration.
 *
 * The method signature is: extraJar(File extraJar)
 * - Sets the configuration.extraJar field to the provided File
 * - This is a simple setter method (not accumulating like other jar methods)
 * - Used internally to write extra data entries to a specific jar
 *
 * Key behavior: Unlike injars/outjars/libraryjars, this is not a list accumulator.
 * Each call overwrites the previous value. The extraJar is used for internal purposes
 * to write extra data entries.
 */
public class ProGuardTaskClaude_extraJarTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("testProguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        task = null;
    }

    /**
     * Tests that extraJar() sets a single File object.
     */
    @Test
    public void testExtraJar_singleFile() throws Exception {
        // Given: A File object
        File jarFile = new File(tempDir.toFile(), "extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The configuration should have the File set
        assertNotNull(task.configuration.extraJar, "ExtraJar should not be null");
        assertSame(jarFile, task.configuration.extraJar, "ExtraJar should be the same File object");
    }

    /**
     * Tests that extraJar() initially has no value.
     */
    @Test
    public void testExtraJar_initiallyNull() throws Exception {
        // Given: A newly created task
        // When: Not calling extraJar()
        // Then: The configuration extraJar should be null
        assertNull(task.configuration.extraJar, "ExtraJar should initially be null");
    }

    /**
     * Tests that extraJar() overwrites previous value.
     */
    @Test
    public void testExtraJar_overwritesPreviousValue() throws Exception {
        // Given: Two different File objects
        File firstJar = new File(tempDir.toFile(), "first.jar");
        File secondJar = new File(tempDir.toFile(), "second.jar");

        // When: Calling extraJar() twice
        task.extraJar(firstJar);
        task.extraJar(secondJar);

        // Then: Only the second value should be stored
        assertSame(secondJar, task.configuration.extraJar, "ExtraJar should be the second File object");
        assertNotSame(firstJar, task.configuration.extraJar, "ExtraJar should not be the first File object");
    }

    /**
     * Tests that extraJar() accepts null value.
     */
    @Test
    public void testExtraJar_nullValue() throws Exception {
        // Given: A null File
        File nullFile = null;

        // When: Calling extraJar() with null
        task.extraJar(nullFile);

        // Then: The configuration should have null
        assertNull(task.configuration.extraJar, "ExtraJar should be null");
    }

    /**
     * Tests that extraJar() can clear previous value with null.
     */
    @Test
    public void testExtraJar_clearWithNull() throws Exception {
        // Given: An initial File set
        File jarFile = new File(tempDir.toFile(), "extra.jar");
        task.extraJar(jarFile);

        // When: Calling extraJar() with null
        task.extraJar(null);

        // Then: The configuration should be null
        assertNull(task.configuration.extraJar, "ExtraJar should be null after clearing");
    }

    /**
     * Tests that extraJar() preserves object identity.
     */
    @Test
    public void testExtraJar_preservesObjectIdentity() throws Exception {
        // Given: A specific File object
        File jarFile = new File(tempDir.toFile(), "extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The exact same object should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store the exact same File object");
    }

    /**
     * Tests that extraJar() handles absolute path.
     */
    @Test
    public void testExtraJar_absolutePath() throws Exception {
        // Given: A File with absolute path
        File jarFile = new File(tempDir.toFile(), "extra.jar");
        String absolutePath = jarFile.getAbsolutePath();

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertEquals(absolutePath, task.configuration.extraJar.getAbsolutePath(),
                     "Should store File with correct absolute path");
    }

    /**
     * Tests that extraJar() handles relative path.
     */
    @Test
    public void testExtraJar_relativePath() throws Exception {
        // Given: A File with relative path
        File jarFile = new File("build/extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store File with relative path");
    }

    /**
     * Tests that extraJar() handles non-existent file.
     */
    @Test
    public void testExtraJar_nonExistentFile() throws Exception {
        // Given: A File that doesn't exist
        File jarFile = new File(tempDir.toFile(), "nonexistent.jar");
        assertFalse(jarFile.exists(), "File should not exist");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should still be stored (no validation)
        assertSame(jarFile, task.configuration.extraJar, "Should store non-existent File");
    }

    /**
     * Tests that extraJar() handles directory instead of file.
     */
    @Test
    public void testExtraJar_directory() throws Exception {
        // Given: A directory File
        File directory = new File(tempDir.toFile(), "extraDir");
        directory.mkdir();
        assertTrue(directory.isDirectory(), "Should be a directory");

        // When: Calling extraJar()
        task.extraJar(directory);

        // Then: The directory should be stored (no validation)
        assertSame(directory, task.configuration.extraJar, "Should store directory File");
    }

    /**
     * Tests that extraJar() handles path with spaces.
     */
    @Test
    public void testExtraJar_pathWithSpaces() throws Exception {
        // Given: A File path with spaces
        File jarFile = new File(tempDir.toFile(), "my extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store File with spaces in path");
        assertTrue(task.configuration.extraJar.getName().contains(" "),
                   "File name should contain space");
    }

    /**
     * Tests that extraJar() handles path with special characters.
     */
    @Test
    public void testExtraJar_pathWithSpecialCharacters() throws Exception {
        // Given: A File path with special characters
        File jarFile = new File(tempDir.toFile(), "extra-v1.0_final@release.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store File with special characters");
    }

    /**
     * Tests that extraJar() handles Windows-style path.
     */
    @Test
    public void testExtraJar_windowsPath() throws Exception {
        // Given: A File with Windows-style path
        File jarFile = new File("C:\\Users\\Dev\\extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store File with Windows path");
    }

    /**
     * Tests that extraJar() handles Unix-style path.
     */
    @Test
    public void testExtraJar_unixPath() throws Exception {
        // Given: A File with Unix-style path
        File jarFile = new File("/home/dev/extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store File with Unix path");
    }

    /**
     * Tests that extraJar() is independent from injars.
     */
    @Test
    public void testExtraJar_independentFromInjars() throws Exception {
        // Given: Input jar and extra jar
        String inputJar = "input.jar";
        File extraJarFile = new File(tempDir.toFile(), "extra.jar");

        // When: Calling both injars() and extraJar()
        task.injars(inputJar);
        task.extraJar(extraJarFile);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertSame(extraJarFile, task.configuration.extraJar, "ExtraJar should be set");
    }

    /**
     * Tests that extraJar() is independent from outjars.
     */
    @Test
    public void testExtraJar_independentFromOutjars() throws Exception {
        // Given: Output jar and extra jar
        String outputJar = "output.jar";
        File extraJarFile = new File(tempDir.toFile(), "extra.jar");

        // When: Calling both outjars() and extraJar()
        task.outjars(outputJar);
        task.extraJar(extraJarFile);

        // Then: Both should be set independently
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertSame(extraJarFile, task.configuration.extraJar, "ExtraJar should be set");
    }

    /**
     * Tests that extraJar() is independent from libraryjars.
     */
    @Test
    public void testExtraJar_independentFromLibraryjars() throws Exception {
        // Given: Library jar and extra jar
        String libraryJar = "android.jar";
        File extraJarFile = new File(tempDir.toFile(), "extra.jar");

        // When: Calling both libraryjars() and extraJar()
        task.libraryjars(libraryJar);
        task.extraJar(extraJarFile);

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertSame(extraJarFile, task.configuration.extraJar, "ExtraJar should be set");
    }

    /**
     * Tests that extraJar() works in a complex workflow.
     */
    @Test
    public void testExtraJar_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");

        File extraJarFile = new File(tempDir.toFile(), "extra.jar");
        task.extraJar(extraJarFile);

        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertSame(extraJarFile, task.configuration.extraJar, "ExtraJar should be set");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that extraJar() handles multiple calls in sequence.
     */
    @Test
    public void testExtraJar_multipleCallsInSequence() throws Exception {
        // Given: Three different File objects
        File jar1 = new File(tempDir.toFile(), "extra1.jar");
        File jar2 = new File(tempDir.toFile(), "extra2.jar");
        File jar3 = new File(tempDir.toFile(), "extra3.jar");

        // When: Calling extraJar() multiple times
        task.extraJar(jar1);
        task.extraJar(jar2);
        task.extraJar(jar3);

        // Then: Only the last value should be stored
        assertSame(jar3, task.configuration.extraJar, "ExtraJar should be the last File object");
    }

    /**
     * Tests that extraJar() handles realistic Android scenario.
     */
    @Test
    public void testExtraJar_realisticAndroidScenario() throws Exception {
        // Given: Android project with extra jar for resources
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");

        File extraJarFile = new File(tempDir.toFile(), "build/intermediates/extra.jar");
        task.extraJar(extraJarFile);

        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: ExtraJar should be set
        assertSame(extraJarFile, task.configuration.extraJar, "ExtraJar should be set for Android");
    }

    /**
     * Tests that extraJar() handles Gradle build directory path.
     */
    @Test
    public void testExtraJar_gradleBuildDirectory() throws Exception {
        // Given: Gradle-style build directory path
        File jarFile = new File("build/tmp/proguard/extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store Gradle build directory path");
    }

    /**
     * Tests that extraJar() handles .aar file extension.
     */
    @Test
    public void testExtraJar_aarFileExtension() throws Exception {
        // Given: A File with .aar extension
        File aarFile = new File(tempDir.toFile(), "extra.aar");

        // When: Calling extraJar()
        task.extraJar(aarFile);

        // Then: The File should be stored (no validation on extension)
        assertSame(aarFile, task.configuration.extraJar, "Should store .aar file");
    }

    /**
     * Tests that extraJar() handles .zip file extension.
     */
    @Test
    public void testExtraJar_zipFileExtension() throws Exception {
        // Given: A File with .zip extension
        File zipFile = new File(tempDir.toFile(), "extra.zip");

        // When: Calling extraJar()
        task.extraJar(zipFile);

        // Then: The File should be stored (no validation on extension)
        assertSame(zipFile, task.configuration.extraJar, "Should store .zip file");
    }

    /**
     * Tests that extraJar() handles file without extension.
     */
    @Test
    public void testExtraJar_noExtension() throws Exception {
        // Given: A File without extension
        File fileNoExt = new File(tempDir.toFile(), "extrajar");

        // When: Calling extraJar()
        task.extraJar(fileNoExt);

        // Then: The File should be stored (no validation)
        assertSame(fileNoExt, task.configuration.extraJar, "Should store file without extension");
    }

    /**
     * Tests that extraJar() can be called before other jar methods.
     */
    @Test
    public void testExtraJar_calledBeforeOtherMethods() throws Exception {
        // Given: An extra jar set first
        File extraJarFile = new File(tempDir.toFile(), "extra.jar");

        // When: Calling extraJar() before other methods
        task.extraJar(extraJarFile);
        task.injars("input.jar");
        task.outjars("output.jar");

        // Then: ExtraJar should still be set
        assertSame(extraJarFile, task.configuration.extraJar, "ExtraJar should remain set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that extraJar() can be called after other jar methods.
     */
    @Test
    public void testExtraJar_calledAfterOtherMethods() throws Exception {
        // Given: Other jars set first
        task.injars("input.jar");
        task.outjars("output.jar");

        File extraJarFile = new File(tempDir.toFile(), "extra.jar");

        // When: Calling extraJar() after other methods
        task.extraJar(extraJarFile);

        // Then: All should be set correctly
        assertSame(extraJarFile, task.configuration.extraJar, "ExtraJar should be set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that extraJar() handles nested directory path.
     */
    @Test
    public void testExtraJar_nestedDirectoryPath() throws Exception {
        // Given: A File with nested directory path
        File jarFile = new File(tempDir.toFile(), "a/b/c/extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store nested path");
    }

    /**
     * Tests that extraJar() setting and resetting.
     */
    @Test
    public void testExtraJar_setAndReset() throws Exception {
        // Given: Initial File
        File initialJar = new File(tempDir.toFile(), "initial.jar");
        task.extraJar(initialJar);
        assertSame(initialJar, task.configuration.extraJar, "Initial jar should be set");

        // When: Resetting to null
        task.extraJar(null);
        assertNull(task.configuration.extraJar, "ExtraJar should be null after reset");

        // Then: Setting again
        File newJar = new File(tempDir.toFile(), "new.jar");
        task.extraJar(newJar);
        assertSame(newJar, task.configuration.extraJar, "New jar should be set");
    }

    /**
     * Tests that extraJar() handles parent directory reference.
     */
    @Test
    public void testExtraJar_parentDirectoryReference() throws Exception {
        // Given: A File with parent directory reference
        File jarFile = new File("../extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store path with parent reference");
    }

    /**
     * Tests that extraJar() handles current directory reference.
     */
    @Test
    public void testExtraJar_currentDirectoryReference() throws Exception {
        // Given: A File with current directory reference
        File jarFile = new File("./extra.jar");

        // When: Calling extraJar()
        task.extraJar(jarFile);

        // Then: The File should be stored
        assertSame(jarFile, task.configuration.extraJar, "Should store path with current dir reference");
    }

    /**
     * Tests that extraJar() is truly a setter (not an accumulator).
     */
    @Test
    public void testExtraJar_isSetterNotAccumulator() throws Exception {
        // Given: Multiple File objects
        File jar1 = new File(tempDir.toFile(), "extra1.jar");
        File jar2 = new File(tempDir.toFile(), "extra2.jar");

        // When: Calling extraJar() twice
        task.extraJar(jar1);
        task.extraJar(jar2);

        // Then: Only one value should be stored (the last one)
        assertSame(jar2, task.configuration.extraJar, "Should only store last value");
        assertNotNull(task.configuration.extraJar, "Should have a value");
        // Note: This is different from injars/outjars/libraryjars which accumulate
    }
}
