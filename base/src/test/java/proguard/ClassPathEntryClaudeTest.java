package proguard;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ClassPathEntry}.
 * Tests all methods to ensure proper functionality of ClassPathEntry.
 */
public class ClassPathEntryClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the two-argument constructor ClassPathEntry(File, boolean).
     * Verifies that file and output flag are correctly initialized.
     */
    @Test
    public void testConstructorTwoArgs() {
        // Arrange
        File file = new File("test.jar");

        // Act
        ClassPathEntry entry = new ClassPathEntry(file, false);

        // Assert
        assertSame(file, entry.getFile(), "File should be set correctly");
        assertFalse(entry.isOutput(), "Output flag should be false");
        assertNull(entry.getFeatureName(), "Feature name should be null by default");
    }

    /**
     * Tests the two-argument constructor with output flag set to true.
     */
    @Test
    public void testConstructorTwoArgsWithOutput() {
        // Arrange
        File file = new File("output.jar");

        // Act
        ClassPathEntry entry = new ClassPathEntry(file, true);

        // Assert
        assertSame(file, entry.getFile(), "File should be set correctly");
        assertTrue(entry.isOutput(), "Output flag should be true");
    }

    /**
     * Tests the three-argument constructor ClassPathEntry(File, boolean, String).
     * Verifies that file, output flag, and feature name are correctly initialized.
     */
    @Test
    public void testConstructorThreeArgs() {
        // Arrange
        File file = new File("feature.jar");
        String featureName = "myFeature";

        // Act
        ClassPathEntry entry = new ClassPathEntry(file, false, featureName);

        // Assert
        assertSame(file, entry.getFile(), "File should be set correctly");
        assertFalse(entry.isOutput(), "Output flag should be false");
        assertEquals(featureName, entry.getFeatureName(), "Feature name should be set correctly");
    }

    /**
     * Tests the three-argument constructor with null feature name.
     */
    @Test
    public void testConstructorThreeArgsWithNullFeature() {
        // Arrange
        File file = new File("test.jar");

        // Act
        ClassPathEntry entry = new ClassPathEntry(file, true, null);

        // Assert
        assertSame(file, entry.getFile(), "File should be set correctly");
        assertTrue(entry.isOutput(), "Output flag should be true");
        assertNull(entry.getFeatureName(), "Feature name should be null");
    }

    // ========== getName() Tests ==========

    /**
     * Tests getName() returns the file path.
     * Uses a simple file name for predictable behavior.
     */
    @Test
    public void testGetName() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);

        // Act
        String name = entry.getName();

        // Assert
        assertNotNull(name, "Name should not be null");
        assertTrue(name.contains("test.jar"), "Name should contain the file name");
    }

    /**
     * Tests getName() caching behavior.
     * Multiple calls should return the same cached value.
     */
    @Test
    public void testGetNameCaching() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);

        // Act
        String name1 = entry.getName();
        String name2 = entry.getName();

        // Assert
        assertSame(name1, name2, "getName() should return the same cached instance");
    }

    /**
     * Tests getName() after setFile().
     * The cached name should be cleared when file is changed.
     */
    @Test
    public void testGetNameAfterSetFile() {
        // Arrange
        File file1 = new File("test1.jar");
        File file2 = new File("test2.jar");
        ClassPathEntry entry = new ClassPathEntry(file1, false);
        String name1 = entry.getName();

        // Act
        entry.setFile(file2);
        String name2 = entry.getName();

        // Assert
        assertNotSame(name1, name2, "Name should be different after changing file");
        assertTrue(name2.contains("test2.jar"), "New name should contain new file name");
    }

    // ========== getFile() and setFile() Tests ==========

    /**
     * Tests getFile() returns the file set in constructor.
     */
    @Test
    public void testGetFile() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);

        // Act & Assert
        assertSame(file, entry.getFile(), "getFile() should return the same file instance");
    }

    /**
     * Tests setFile() updates the file.
     */
    @Test
    public void testSetFile() {
        // Arrange
        File file1 = new File("test1.jar");
        File file2 = new File("test2.jar");
        ClassPathEntry entry = new ClassPathEntry(file1, false);

        // Act
        entry.setFile(file2);

        // Assert
        assertSame(file2, entry.getFile(), "File should be updated");
    }

    /**
     * Tests setFile() clears the cached name.
     */
    @Test
    public void testSetFileClearsCachedName() {
        // Arrange
        File file1 = new File("test1.jar");
        File file2 = new File("test2.jar");
        ClassPathEntry entry = new ClassPathEntry(file1, false);
        String name1 = entry.getName(); // Cache the name

        // Act
        entry.setFile(file2);
        String name2 = entry.getName();

        // Assert
        assertNotEquals(name1, name2, "Cached name should be cleared when file is set");
    }

    // ========== isOutput() and setOutput() Tests ==========

    /**
     * Tests isOutput() returns the value set in constructor.
     */
    @Test
    public void testIsOutput() {
        // Arrange & Act
        ClassPathEntry inputEntry = new ClassPathEntry(new File("input.jar"), false);
        ClassPathEntry outputEntry = new ClassPathEntry(new File("output.jar"), true);

        // Assert
        assertFalse(inputEntry.isOutput(), "Input entry should return false");
        assertTrue(outputEntry.isOutput(), "Output entry should return true");
    }

    /**
     * Tests setOutput() changes the output flag.
     */
    @Test
    public void testSetOutput() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);

        // Act
        entry.setOutput(true);

        // Assert
        assertTrue(entry.isOutput(), "Output flag should be updated to true");
    }

    /**
     * Tests setOutput() can toggle the output flag.
     */
    @Test
    public void testSetOutputToggle() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), true);

        // Act
        entry.setOutput(false);

        // Assert
        assertFalse(entry.isOutput(), "Output flag should be updated to false");
    }

    // ========== getFeatureName() and setFeatureName() Tests ==========

    /**
     * Tests getFeatureName() returns null when not set.
     */
    @Test
    public void testGetFeatureNameNull() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);

        // Act & Assert
        assertNull(entry.getFeatureName(), "Feature name should be null by default");
    }

    /**
     * Tests getFeatureName() returns the value set in constructor.
     */
    @Test
    public void testGetFeatureNameFromConstructor() {
        // Arrange
        String featureName = "myFeature";
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false, featureName);

        // Act & Assert
        assertEquals(featureName, entry.getFeatureName(), "Feature name should be set correctly");
    }

    /**
     * Tests setFeatureName() updates the feature name.
     */
    @Test
    public void testSetFeatureName() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        String featureName = "newFeature";

        // Act
        entry.setFeatureName(featureName);

        // Assert
        assertEquals(featureName, entry.getFeatureName(), "Feature name should be updated");
    }

    /**
     * Tests setFeatureName() can set to null.
     */
    @Test
    public void testSetFeatureNameToNull() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false, "feature");

        // Act
        entry.setFeatureName(null);

        // Assert
        assertNull(entry.getFeatureName(), "Feature name should be null after setting to null");
    }

    // ========== File Type Detection Tests ==========

    /**
     * Tests isDex() returns true for .dex files.
     */
    @Test
    public void testIsDex() {
        // Arrange
        ClassPathEntry dexEntry = new ClassPathEntry(new File("classes.dex"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(dexEntry.isDex(), ".dex file should be detected");
        assertFalse(jarEntry.isDex(), ".jar file should not be detected as dex");
    }

    /**
     * Tests isDex() is case-insensitive.
     */
    @Test
    public void testIsDexCaseInsensitive() {
        // Arrange
        ClassPathEntry entry1 = new ClassPathEntry(new File("classes.DEX"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("classes.Dex"), false);

        // Assert
        assertTrue(entry1.isDex(), ".DEX should be detected");
        assertTrue(entry2.isDex(), ".Dex should be detected");
    }

    /**
     * Tests isApk() returns true for .apk and .ap_ files.
     */
    @Test
    public void testIsApk() {
        // Arrange
        ClassPathEntry apkEntry = new ClassPathEntry(new File("app.apk"), false);
        ClassPathEntry apUnderscoreEntry = new ClassPathEntry(new File("app.ap_"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(apkEntry.isApk(), ".apk file should be detected");
        assertTrue(apUnderscoreEntry.isApk(), ".ap_ file should be detected");
        assertFalse(jarEntry.isApk(), ".jar file should not be detected as apk");
    }

    /**
     * Tests isApk() is case-insensitive.
     */
    @Test
    public void testIsApkCaseInsensitive() {
        // Arrange
        ClassPathEntry entry1 = new ClassPathEntry(new File("app.APK"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("app.AP_"), false);

        // Assert
        assertTrue(entry1.isApk(), ".APK should be detected");
        assertTrue(entry2.isApk(), ".AP_ should be detected");
    }

    /**
     * Tests isAab() returns true for .aab files.
     */
    @Test
    public void testIsAab() {
        // Arrange
        ClassPathEntry aabEntry = new ClassPathEntry(new File("app.aab"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(aabEntry.isAab(), ".aab file should be detected");
        assertFalse(jarEntry.isAab(), ".jar file should not be detected as aab");
    }

    /**
     * Tests isAab() is case-insensitive.
     */
    @Test
    public void testIsAabCaseInsensitive() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("app.AAB"), false);

        // Assert
        assertTrue(entry.isAab(), ".AAB should be detected");
    }

    /**
     * Tests isJar() returns true for .jar files.
     */
    @Test
    public void testIsJar() {
        // Arrange
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);
        ClassPathEntry warEntry = new ClassPathEntry(new File("test.war"), false);

        // Assert
        assertTrue(jarEntry.isJar(), ".jar file should be detected");
        assertFalse(warEntry.isJar(), ".war file should not be detected as jar");
    }

    /**
     * Tests isJar() is case-insensitive.
     */
    @Test
    public void testIsJarCaseInsensitive() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.JAR"), false);

        // Assert
        assertTrue(entry.isJar(), ".JAR should be detected");
    }

    /**
     * Tests isAar() returns true for .aar files.
     */
    @Test
    public void testIsAar() {
        // Arrange
        ClassPathEntry aarEntry = new ClassPathEntry(new File("library.aar"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(aarEntry.isAar(), ".aar file should be detected");
        assertFalse(jarEntry.isAar(), ".jar file should not be detected as aar");
    }

    /**
     * Tests isAar() is case-insensitive.
     */
    @Test
    public void testIsAarCaseInsensitive() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("library.AAR"), false);

        // Assert
        assertTrue(entry.isAar(), ".AAR should be detected");
    }

    /**
     * Tests isWar() returns true for .war files.
     */
    @Test
    public void testIsWar() {
        // Arrange
        ClassPathEntry warEntry = new ClassPathEntry(new File("app.war"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(warEntry.isWar(), ".war file should be detected");
        assertFalse(jarEntry.isWar(), ".jar file should not be detected as war");
    }

    /**
     * Tests isWar() is case-insensitive.
     */
    @Test
    public void testIsWarCaseInsensitive() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("app.WAR"), false);

        // Assert
        assertTrue(entry.isWar(), ".WAR should be detected");
    }

    /**
     * Tests isEar() returns true for .ear files.
     */
    @Test
    public void testIsEar() {
        // Arrange
        ClassPathEntry earEntry = new ClassPathEntry(new File("app.ear"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(earEntry.isEar(), ".ear file should be detected");
        assertFalse(jarEntry.isEar(), ".jar file should not be detected as ear");
    }

    /**
     * Tests isEar() is case-insensitive.
     */
    @Test
    public void testIsEarCaseInsensitive() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("app.EAR"), false);

        // Assert
        assertTrue(entry.isEar(), ".EAR should be detected");
    }

    /**
     * Tests isJmod() returns true for .jmod files.
     */
    @Test
    public void testIsJmod() {
        // Arrange
        ClassPathEntry jmodEntry = new ClassPathEntry(new File("module.jmod"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(jmodEntry.isJmod(), ".jmod file should be detected");
        assertFalse(jarEntry.isJmod(), ".jar file should not be detected as jmod");
    }

    /**
     * Tests isJmod() is case-insensitive.
     */
    @Test
    public void testIsJmodCaseInsensitive() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("module.JMOD"), false);

        // Assert
        assertTrue(entry.isJmod(), ".JMOD should be detected");
    }

    /**
     * Tests isZip() returns true for .zip files.
     */
    @Test
    public void testIsZip() {
        // Arrange
        ClassPathEntry zipEntry = new ClassPathEntry(new File("archive.zip"), false);
        ClassPathEntry jarEntry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertTrue(zipEntry.isZip(), ".zip file should be detected");
        assertFalse(jarEntry.isZip(), ".jar file should not be detected as zip");
    }

    /**
     * Tests isZip() is case-insensitive.
     */
    @Test
    public void testIsZipCaseInsensitive() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("archive.ZIP"), false);

        // Assert
        assertTrue(entry.isZip(), ".ZIP should be detected");
    }

    /**
     * Tests file type detection with paths containing multiple dots.
     */
    @Test
    public void testFileTypeDetectionWithMultipleDots() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("my.app.version.1.0.jar"), false);

        // Assert
        assertTrue(entry.isJar(), "Should detect .jar extension even with multiple dots");
        assertFalse(entry.isWar(), "Should not detect as .war");
    }

    // ========== isFiltered() Tests ==========

    /**
     * Tests isFiltered() returns false when no filters are set.
     */
    @Test
    public void testIsFilteredNoFilters() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);

        // Assert
        assertFalse(entry.isFiltered(), "Entry with no filters should not be filtered");
    }

    /**
     * Tests isFiltered() returns true when filter is set.
     */
    @Test
    public void testIsFilteredWithFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setFilter(Arrays.asList("**.class"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with filter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when apkFilter is set.
     */
    @Test
    public void testIsFilteredWithApkFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setApkFilter(Arrays.asList("*.apk"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with apkFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when aabFilter is set.
     */
    @Test
    public void testIsFilteredWithAabFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setAabFilter(Arrays.asList("*.aab"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with aabFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when jarFilter is set.
     */
    @Test
    public void testIsFilteredWithJarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setJarFilter(Arrays.asList("*.jar"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with jarFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when aarFilter is set.
     */
    @Test
    public void testIsFilteredWithAarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setAarFilter(Arrays.asList("*.aar"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with aarFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when warFilter is set.
     */
    @Test
    public void testIsFilteredWithWarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setWarFilter(Arrays.asList("*.war"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with warFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when earFilter is set.
     */
    @Test
    public void testIsFilteredWithEarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setEarFilter(Arrays.asList("*.ear"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with earFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when jmodFilter is set.
     */
    @Test
    public void testIsFilteredWithJmodFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setJmodFilter(Arrays.asList("*.jmod"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with jmodFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when zipFilter is set.
     */
    @Test
    public void testIsFilteredWithZipFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setZipFilter(Arrays.asList("*.zip"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with zipFilter should be filtered");
    }

    /**
     * Tests isFiltered() returns true when multiple filters are set.
     */
    @Test
    public void testIsFilteredWithMultipleFilters() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setFilter(Arrays.asList("**.class"));
        entry.setJarFilter(Arrays.asList("*.jar"));
        entry.setZipFilter(Arrays.asList("*.zip"));

        // Assert
        assertTrue(entry.isFiltered(), "Entry with multiple filters should be filtered");
    }

    // ========== Filter Getter/Setter Tests ==========

    /**
     * Tests getFilter() and setFilter().
     */
    @Test
    public void testGetSetFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("**.class", "!**.xml");

        // Act
        entry.setFilter(filter);

        // Assert
        assertSame(filter, entry.getFilter(), "Filter should be set correctly");
    }

    /**
     * Tests setFilter() with null sets filter to null.
     */
    @Test
    public void testSetFilterNull() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setFilter(Arrays.asList("**.class"));

        // Act
        entry.setFilter(null);

        // Assert
        assertNull(entry.getFilter(), "Filter should be null after setting to null");
    }

    /**
     * Tests setFilter() with empty list sets filter to null.
     */
    @Test
    public void testSetFilterEmptyList() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setFilter(Arrays.asList("**.class"));

        // Act
        entry.setFilter(Collections.emptyList());

        // Assert
        assertNull(entry.getFilter(), "Filter should be null after setting to empty list");
    }

    /**
     * Tests getApkFilter() and setApkFilter().
     */
    @Test
    public void testGetSetApkFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.apk");

        // Act
        entry.setApkFilter(filter);

        // Assert
        assertSame(filter, entry.getApkFilter(), "ApkFilter should be set correctly");
    }

    /**
     * Tests setApkFilter() with null.
     */
    @Test
    public void testSetApkFilterNull() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setApkFilter(Arrays.asList("*.apk"));

        // Act
        entry.setApkFilter(null);

        // Assert
        assertNull(entry.getApkFilter(), "ApkFilter should be null");
    }

    /**
     * Tests setApkFilter() with empty list.
     */
    @Test
    public void testSetApkFilterEmptyList() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setApkFilter(Arrays.asList("*.apk"));

        // Act
        entry.setApkFilter(Collections.emptyList());

        // Assert
        assertNull(entry.getApkFilter(), "ApkFilter should be null after setting to empty list");
    }

    /**
     * Tests getAabFilter() and setAabFilter().
     */
    @Test
    public void testGetSetAabFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.aab");

        // Act
        entry.setAabFilter(filter);

        // Assert
        assertSame(filter, entry.getAabFilter(), "AabFilter should be set correctly");
    }

    /**
     * Tests setAabFilter() with null and empty list.
     */
    @Test
    public void testSetAabFilterNullAndEmpty() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setAabFilter(Arrays.asList("*.aab"));

        // Act & Assert - null
        entry.setAabFilter(null);
        assertNull(entry.getAabFilter(), "AabFilter should be null");

        // Act & Assert - empty list
        entry.setAabFilter(Arrays.asList("*.aab"));
        entry.setAabFilter(Collections.emptyList());
        assertNull(entry.getAabFilter(), "AabFilter should be null after empty list");
    }

    /**
     * Tests getJarFilter() and setJarFilter().
     */
    @Test
    public void testGetSetJarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.jar");

        // Act
        entry.setJarFilter(filter);

        // Assert
        assertSame(filter, entry.getJarFilter(), "JarFilter should be set correctly");
    }

    /**
     * Tests setJarFilter() with null and empty list.
     */
    @Test
    public void testSetJarFilterNullAndEmpty() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setJarFilter(Arrays.asList("*.jar"));

        // Act & Assert - null
        entry.setJarFilter(null);
        assertNull(entry.getJarFilter(), "JarFilter should be null");

        // Act & Assert - empty list
        entry.setJarFilter(Arrays.asList("*.jar"));
        entry.setJarFilter(new ArrayList<>());
        assertNull(entry.getJarFilter(), "JarFilter should be null after empty list");
    }

    /**
     * Tests getAarFilter() and setAarFilter().
     */
    @Test
    public void testGetSetAarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.aar");

        // Act
        entry.setAarFilter(filter);

        // Assert
        assertSame(filter, entry.getAarFilter(), "AarFilter should be set correctly");
    }

    /**
     * Tests setAarFilter() with null and empty list.
     */
    @Test
    public void testSetAarFilterNullAndEmpty() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setAarFilter(Arrays.asList("*.aar"));

        // Act & Assert
        entry.setAarFilter(null);
        assertNull(entry.getAarFilter(), "AarFilter should be null");

        entry.setAarFilter(Arrays.asList("*.aar"));
        entry.setAarFilter(Collections.emptyList());
        assertNull(entry.getAarFilter(), "AarFilter should be null after empty list");
    }

    /**
     * Tests getWarFilter() and setWarFilter().
     */
    @Test
    public void testGetSetWarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.war");

        // Act
        entry.setWarFilter(filter);

        // Assert
        assertSame(filter, entry.getWarFilter(), "WarFilter should be set correctly");
    }

    /**
     * Tests setWarFilter() with null and empty list.
     */
    @Test
    public void testSetWarFilterNullAndEmpty() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setWarFilter(Arrays.asList("*.war"));

        // Act & Assert
        entry.setWarFilter(null);
        assertNull(entry.getWarFilter(), "WarFilter should be null");

        entry.setWarFilter(Arrays.asList("*.war"));
        entry.setWarFilter(Collections.emptyList());
        assertNull(entry.getWarFilter(), "WarFilter should be null after empty list");
    }

    /**
     * Tests getEarFilter() and setEarFilter().
     */
    @Test
    public void testGetSetEarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.ear");

        // Act
        entry.setEarFilter(filter);

        // Assert
        assertSame(filter, entry.getEarFilter(), "EarFilter should be set correctly");
    }

    /**
     * Tests setEarFilter() with null and empty list.
     */
    @Test
    public void testSetEarFilterNullAndEmpty() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setEarFilter(Arrays.asList("*.ear"));

        // Act & Assert
        entry.setEarFilter(null);
        assertNull(entry.getEarFilter(), "EarFilter should be null");

        entry.setEarFilter(Arrays.asList("*.ear"));
        entry.setEarFilter(Collections.emptyList());
        assertNull(entry.getEarFilter(), "EarFilter should be null after empty list");
    }

    /**
     * Tests getJmodFilter() and setJmodFilter().
     */
    @Test
    public void testGetSetJmodFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.jmod");

        // Act
        entry.setJmodFilter(filter);

        // Assert
        assertSame(filter, entry.getJmodFilter(), "JmodFilter should be set correctly");
    }

    /**
     * Tests setJmodFilter() with null and empty list.
     */
    @Test
    public void testSetJmodFilterNullAndEmpty() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setJmodFilter(Arrays.asList("*.jmod"));

        // Act & Assert
        entry.setJmodFilter(null);
        assertNull(entry.getJmodFilter(), "JmodFilter should be null");

        entry.setJmodFilter(Arrays.asList("*.jmod"));
        entry.setJmodFilter(Collections.emptyList());
        assertNull(entry.getJmodFilter(), "JmodFilter should be null after empty list");
    }

    /**
     * Tests getZipFilter() and setZipFilter().
     */
    @Test
    public void testGetSetZipFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        List<String> filter = Arrays.asList("*.zip");

        // Act
        entry.setZipFilter(filter);

        // Assert
        assertSame(filter, entry.getZipFilter(), "ZipFilter should be set correctly");
    }

    /**
     * Tests setZipFilter() with null and empty list.
     */
    @Test
    public void testSetZipFilterNullAndEmpty() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setZipFilter(Arrays.asList("*.zip"));

        // Act & Assert
        entry.setZipFilter(null);
        assertNull(entry.getZipFilter(), "ZipFilter should be null");

        entry.setZipFilter(Arrays.asList("*.zip"));
        entry.setZipFilter(Collections.emptyList());
        assertNull(entry.getZipFilter(), "ZipFilter should be null after empty list");
    }

    // ========== toString() Tests ==========

    /**
     * Tests toString() without any filters.
     * Should return just the file name.
     */
    @Test
    public void testToStringNoFilters() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);

        // Act
        String result = entry.toString();

        // Assert
        assertNotNull(result, "toString() should not return null");
        assertTrue(result.contains("test.jar"), "toString() should contain the file name");
        assertFalse(result.contains("("), "toString() should not contain opening parenthesis without filters");
    }

    /**
     * Tests toString() with a single filter.
     * Should include the filter in the output.
     */
    @Test
    public void testToStringWithFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setFilter(Arrays.asList("**.class"));

        // Act
        String result = entry.toString();

        // Assert
        assertNotNull(result, "toString() should not return null");
        assertTrue(result.contains("test.jar"), "toString() should contain the file name");
        assertTrue(result.contains("("), "toString() should contain opening parenthesis");
        assertTrue(result.contains(")"), "toString() should contain closing parenthesis");
        assertTrue(result.contains("**.class"), "toString() should contain the filter");
    }

    /**
     * Tests toString() with jar filter.
     */
    @Test
    public void testToStringWithJarFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setJarFilter(Arrays.asList("lib*.jar"));

        // Act
        String result = entry.toString();

        // Assert
        assertTrue(result.contains("("), "toString() should contain opening parenthesis");
        assertTrue(result.contains(")"), "toString() should contain closing parenthesis");
        assertTrue(result.contains("lib*.jar"), "toString() should contain jar filter");
    }

    /**
     * Tests toString() with multiple filters.
     * Should include all filters separated by semicolons.
     */
    @Test
    public void testToStringWithMultipleFilters() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setJarFilter(Arrays.asList("lib*.jar"));
        entry.setZipFilter(Arrays.asList("*.zip"));
        entry.setFilter(Arrays.asList("**.class"));

        // Act
        String result = entry.toString();

        // Assert
        assertTrue(result.contains("("), "toString() should contain opening parenthesis");
        assertTrue(result.contains(")"), "toString() should contain closing parenthesis");
        assertTrue(result.contains(";"), "toString() should contain semicolons as separators");
        assertTrue(result.contains("lib*.jar"), "toString() should contain jar filter");
        assertTrue(result.contains("*.zip"), "toString() should contain zip filter");
        assertTrue(result.contains("**.class"), "toString() should contain filter");
    }

    /**
     * Tests toString() with all types of filters.
     */
    @Test
    public void testToStringWithAllFilters() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setAarFilter(Arrays.asList("*.aar"));
        entry.setAabFilter(Arrays.asList("*.aab"));
        entry.setApkFilter(Arrays.asList("*.apk"));
        entry.setZipFilter(Arrays.asList("*.zip"));
        entry.setJmodFilter(Arrays.asList("*.jmod"));
        entry.setEarFilter(Arrays.asList("*.ear"));
        entry.setWarFilter(Arrays.asList("*.war"));
        entry.setJarFilter(Arrays.asList("*.jar"));
        entry.setFilter(Arrays.asList("**.class"));

        // Act
        String result = entry.toString();

        // Assert
        assertTrue(result.contains("("), "toString() should contain opening parenthesis");
        assertTrue(result.contains(")"), "toString() should contain closing parenthesis");
        assertTrue(result.contains("*.aar"), "toString() should contain aar filter");
        assertTrue(result.contains("*.aab"), "toString() should contain aab filter");
        assertTrue(result.contains("*.apk"), "toString() should contain apk filter");
        assertTrue(result.contains("*.zip"), "toString() should contain zip filter");
        assertTrue(result.contains("*.jmod"), "toString() should contain jmod filter");
        assertTrue(result.contains("*.ear"), "toString() should contain ear filter");
        assertTrue(result.contains("*.war"), "toString() should contain war filter");
        assertTrue(result.contains("*.jar"), "toString() should contain jar filter");
        assertTrue(result.contains("**.class"), "toString() should contain filter");
    }

    /**
     * Tests toString() with multiple entries in a single filter.
     */
    @Test
    public void testToStringWithMultipleFilterEntries() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setJarFilter(Arrays.asList("lib*.jar", "util*.jar", "!test*.jar"));

        // Act
        String result = entry.toString();

        // Assert
        assertTrue(result.contains("lib*.jar"), "toString() should contain first jar filter entry");
        assertTrue(result.contains("util*.jar"), "toString() should contain second jar filter entry");
        assertTrue(result.contains("!test*.jar"), "toString() should contain third jar filter entry");
    }

    /**
     * Tests toString() after clearing a filter.
     */
    @Test
    public void testToStringAfterClearingFilter() {
        // Arrange
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        entry.setFilter(Arrays.asList("**.class"));
        entry.setFilter(null);

        // Act
        String result = entry.toString();

        // Assert
        assertFalse(result.contains("("), "toString() should not contain parenthesis after clearing filter");
    }

    // ========== Integration Tests ==========

    /**
     * Integration test: Create entry, modify all properties, verify state.
     */
    @Test
    public void testCompleteWorkflow() {
        // Arrange
        File file1 = new File("input.jar");
        File file2 = new File("output.jar");

        // Act & Assert - Initial state
        ClassPathEntry entry = new ClassPathEntry(file1, false);
        assertSame(file1, entry.getFile());
        assertFalse(entry.isOutput());
        assertNull(entry.getFeatureName());
        assertFalse(entry.isFiltered());

        // Act & Assert - Modify properties
        entry.setFile(file2);
        entry.setOutput(true);
        entry.setFeatureName("testFeature");
        entry.setFilter(Arrays.asList("**.class"));
        entry.setJarFilter(Arrays.asList("*.jar"));

        assertSame(file2, entry.getFile());
        assertTrue(entry.isOutput());
        assertEquals("testFeature", entry.getFeatureName());
        assertTrue(entry.isFiltered());
        assertNotNull(entry.getFilter());
        assertNotNull(entry.getJarFilter());

        // Act & Assert - Clear filters
        entry.setFilter(null);
        entry.setJarFilter(Collections.emptyList());
        assertFalse(entry.isFiltered());
    }

    /**
     * Integration test: Test file type detection works across different file paths.
     */
    @Test
    public void testFileTypeDetectionIntegration() {
        // Create entries with different file types
        ClassPathEntry jarEntry = new ClassPathEntry(new File("/path/to/app.jar"), false);
        ClassPathEntry warEntry = new ClassPathEntry(new File("/path/to/app.war"), false);
        ClassPathEntry earEntry = new ClassPathEntry(new File("/path/to/app.ear"), false);
        ClassPathEntry zipEntry = new ClassPathEntry(new File("/path/to/app.zip"), false);
        ClassPathEntry apkEntry = new ClassPathEntry(new File("/path/to/app.apk"), false);
        ClassPathEntry dexEntry = new ClassPathEntry(new File("/path/to/classes.dex"), false);

        // Verify each entry is detected correctly and only as its type
        assertTrue(jarEntry.isJar());
        assertFalse(jarEntry.isWar());
        assertFalse(jarEntry.isEar());
        assertFalse(jarEntry.isZip());

        assertTrue(warEntry.isWar());
        assertFalse(warEntry.isJar());

        assertTrue(earEntry.isEar());
        assertFalse(earEntry.isJar());

        assertTrue(zipEntry.isZip());
        assertFalse(zipEntry.isJar());

        assertTrue(apkEntry.isApk());
        assertFalse(apkEntry.isJar());

        assertTrue(dexEntry.isDex());
        assertFalse(dexEntry.isJar());
    }
}
