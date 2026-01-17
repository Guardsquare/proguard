package proguard;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Configuration}.
 * Tests the default constructor to ensure proper initialization of all fields.
 */
public class ConfigurationClaudeTest {

    /**
     * Tests the no-argument constructor Configuration().
     * Verifies that all fields are initialized to their expected default values.
     */
    @Test
    public void testConstructorInitializesAllFields() {
        // Act
        Configuration config = new Configuration();

        // Assert - Input and output options
        assertNull(config.programJars, "programJars should be null by default");
        assertNull(config.libraryJars, "libraryJars should be null by default");
        assertFalse(config.skipNonPublicLibraryClasses, "skipNonPublicLibraryClasses should be false by default");
        assertTrue(config.skipNonPublicLibraryClassMembers, "skipNonPublicLibraryClassMembers should be true by default");
        assertNull(config.keepDirectories, "keepDirectories should be null by default");
        assertNull(config.dontCompress, "dontCompress should be null by default");
        assertEquals(1, config.zipAlign, "zipAlign should be 1 by default");
        assertEquals(0, config.targetClassVersion, "targetClassVersion should be 0 by default");
        assertEquals(0L, config.lastModified, "lastModified should be 0L by default");

        // Assert - Keep options for code
        assertNull(config.keep, "keep should be null by default");
        assertNull(config.printSeeds, "printSeeds should be null by default");

        // Assert - Shrinking options
        assertTrue(config.shrink, "shrink should be true by default");
        assertNull(config.printUsage, "printUsage should be null by default");
        assertNull(config.whyAreYouKeeping, "whyAreYouKeeping should be null by default");

        // Assert - Optimization options
        assertTrue(config.optimize, "optimize should be true by default");
        assertNull(config.optimizations, "optimizations should be null by default");
        assertEquals(1, config.optimizationPasses, "optimizationPasses should be 1 by default");
        assertNull(config.assumeNoSideEffects, "assumeNoSideEffects should be null by default");
        assertNull(config.assumeNoExternalSideEffects, "assumeNoExternalSideEffects should be null by default");
        assertNull(config.assumeNoEscapingParameters, "assumeNoEscapingParameters should be null by default");
        assertNull(config.assumeNoExternalReturnValues, "assumeNoExternalReturnValues should be null by default");
        assertNull(config.assumeValues, "assumeValues should be null by default");
        assertFalse(config.allowAccessModification, "allowAccessModification should be false by default");
        assertFalse(config.mergeInterfacesAggressively, "mergeInterfacesAggressively should be false by default");

        // Assert - Obfuscation options
        assertTrue(config.obfuscate, "obfuscate should be true by default");
        assertNull(config.printMapping, "printMapping should be null by default");
        assertNull(config.applyMapping, "applyMapping should be null by default");
        assertNull(config.obfuscationDictionary, "obfuscationDictionary should be null by default");
        assertNull(config.classObfuscationDictionary, "classObfuscationDictionary should be null by default");
        assertNull(config.packageObfuscationDictionary, "packageObfuscationDictionary should be null by default");
        assertFalse(config.overloadAggressively, "overloadAggressively should be false by default");
        assertFalse(config.useUniqueClassMemberNames, "useUniqueClassMemberNames should be false by default");
        assertTrue(config.useMixedCaseClassNames, "useMixedCaseClassNames should be true by default");
        assertNull(config.keepPackageNames, "keepPackageNames should be null by default");
        assertNull(config.flattenPackageHierarchy, "flattenPackageHierarchy should be null by default");
        assertNull(config.repackageClasses, "repackageClasses should be null by default");
        assertNull(config.keepAttributes, "keepAttributes should be null by default");
        assertFalse(config.keepParameterNames, "keepParameterNames should be false by default");
        assertNull(config.newSourceFileAttribute, "newSourceFileAttribute should be null by default");
        assertNull(config.adaptClassStrings, "adaptClassStrings should be null by default");
        assertNull(config.adaptResourceFileNames, "adaptResourceFileNames should be null by default");
        assertNull(config.adaptResourceFileContents, "adaptResourceFileContents should be null by default");

        // Assert - Preverification options
        assertTrue(config.preverify, "preverify should be true by default");
        assertFalse(config.microEdition, "microEdition should be false by default");
        assertFalse(config.android, "android should be false by default");

        // Assert - Jar signing options
        assertNull(config.keyStores, "keyStores should be null by default");
        assertNull(config.keyStorePasswords, "keyStorePasswords should be null by default");
        assertNull(config.keyAliases, "keyAliases should be null by default");
        assertNull(config.keyPasswords, "keyPasswords should be null by default");

        // Assert - General options
        assertFalse(config.verbose, "verbose should be false by default");
        assertNull(config.note, "note should be null by default");
        assertNull(config.warn, "warn should be null by default");
        assertFalse(config.ignoreWarnings, "ignoreWarnings should be false by default");
        assertNull(config.printConfiguration, "printConfiguration should be null by default");
        assertNull(config.dump, "dump should be null by default");
        assertFalse(config.addConfigurationDebugging, "addConfigurationDebugging should be false by default");
        assertFalse(config.backport, "backport should be false by default");
        assertFalse(config.keepKotlinMetadata, "keepKotlinMetadata should be false by default");
        assertFalse(config.dontProcessKotlinMetadata, "dontProcessKotlinMetadata should be false by default");

        // Assert - Internal options
        assertTrue(config.enableKotlinAsserter, "enableKotlinAsserter should be true by default");
        assertNull(config.extraJar, "extraJar should be null by default");
        assertTrue(config.optimizeConservatively, "optimizeConservatively should be true by default");
    }

    /**
     * Tests that the static STD_OUT field is properly initialized.
     */
    @Test
    public void testStaticSTD_OUTField() {
        // Assert
        assertNotNull(Configuration.STD_OUT, "STD_OUT should not be null");
        assertEquals("", Configuration.STD_OUT.getPath(), "STD_OUT should be an empty path File");
    }

    /**
     * Tests that multiple Configuration instances are independent.
     * Verifies that modifying one instance doesn't affect another.
     */
    @Test
    public void testMultipleConfigurationsAreIndependent() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();

        // Act - Modify config1
        config1.shrink = false;
        config1.optimize = false;
        config1.obfuscate = false;
        config1.verbose = true;
        config1.zipAlign = 4;

        // Assert - config2 should remain unchanged
        assertTrue(config2.shrink, "config2.shrink should remain true");
        assertTrue(config2.optimize, "config2.optimize should remain true");
        assertTrue(config2.obfuscate, "config2.obfuscate should remain true");
        assertFalse(config2.verbose, "config2.verbose should remain false");
        assertEquals(1, config2.zipAlign, "config2.zipAlign should remain 1");
    }

    /**
     * Tests that boolean fields with default true values are correctly initialized.
     */
    @Test
    public void testBooleanFieldsDefaultTrue() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertTrue(config.shrink, "shrink should default to true");
        assertTrue(config.optimize, "optimize should default to true");
        assertTrue(config.obfuscate, "obfuscate should default to true");
        assertTrue(config.preverify, "preverify should default to true");
        assertTrue(config.skipNonPublicLibraryClassMembers, "skipNonPublicLibraryClassMembers should default to true");
        assertTrue(config.useMixedCaseClassNames, "useMixedCaseClassNames should default to true");
        assertTrue(config.enableKotlinAsserter, "enableKotlinAsserter should default to true");
        assertTrue(config.optimizeConservatively, "optimizeConservatively should default to true");
    }

    /**
     * Tests that boolean fields with default false values are correctly initialized.
     */
    @Test
    public void testBooleanFieldsDefaultFalse() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertFalse(config.skipNonPublicLibraryClasses, "skipNonPublicLibraryClasses should default to false");
        assertFalse(config.allowAccessModification, "allowAccessModification should default to false");
        assertFalse(config.mergeInterfacesAggressively, "mergeInterfacesAggressively should default to false");
        assertFalse(config.overloadAggressively, "overloadAggressively should default to false");
        assertFalse(config.useUniqueClassMemberNames, "useUniqueClassMemberNames should default to false");
        assertFalse(config.keepParameterNames, "keepParameterNames should default to false");
        assertFalse(config.microEdition, "microEdition should default to false");
        assertFalse(config.android, "android should default to false");
        assertFalse(config.verbose, "verbose should default to false");
        assertFalse(config.ignoreWarnings, "ignoreWarnings should default to false");
        assertFalse(config.addConfigurationDebugging, "addConfigurationDebugging should default to false");
        assertFalse(config.backport, "backport should default to false");
        assertFalse(config.keepKotlinMetadata, "keepKotlinMetadata should default to false");
        assertFalse(config.dontProcessKotlinMetadata, "dontProcessKotlinMetadata should default to false");
    }

    /**
     * Tests that numeric fields are correctly initialized with their default values.
     */
    @Test
    public void testNumericFieldsInitialization() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertEquals(1, config.zipAlign, "zipAlign should default to 1");
        assertEquals(0, config.targetClassVersion, "targetClassVersion should default to 0");
        assertEquals(0L, config.lastModified, "lastModified should default to 0L");
        assertEquals(1, config.optimizationPasses, "optimizationPasses should default to 1");
    }

    /**
     * Tests that all File fields are initialized to null.
     */
    @Test
    public void testFileFieldsInitializedToNull() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertNull(config.printSeeds, "printSeeds should be null by default");
        assertNull(config.printUsage, "printUsage should be null by default");
        assertNull(config.printMapping, "printMapping should be null by default");
        assertNull(config.applyMapping, "applyMapping should be null by default");
        assertNull(config.printConfiguration, "printConfiguration should be null by default");
        assertNull(config.dump, "dump should be null by default");
        assertNull(config.extraJar, "extraJar should be null by default");
    }

    /**
     * Tests that all URL fields are initialized to null.
     */
    @Test
    public void testURLFieldsInitializedToNull() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertNull(config.obfuscationDictionary, "obfuscationDictionary should be null by default");
        assertNull(config.classObfuscationDictionary, "classObfuscationDictionary should be null by default");
        assertNull(config.packageObfuscationDictionary, "packageObfuscationDictionary should be null by default");
    }

    /**
     * Tests that all String fields are initialized to null.
     */
    @Test
    public void testStringFieldsInitializedToNull() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertNull(config.flattenPackageHierarchy, "flattenPackageHierarchy should be null by default");
        assertNull(config.repackageClasses, "repackageClasses should be null by default");
        assertNull(config.newSourceFileAttribute, "newSourceFileAttribute should be null by default");
    }

    /**
     * Tests that all List fields are initialized to null.
     */
    @Test
    public void testListFieldsInitializedToNull() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertNull(config.keepDirectories, "keepDirectories should be null by default");
        assertNull(config.dontCompress, "dontCompress should be null by default");
        assertNull(config.keep, "keep should be null by default");
        assertNull(config.whyAreYouKeeping, "whyAreYouKeeping should be null by default");
        assertNull(config.optimizations, "optimizations should be null by default");
        assertNull(config.assumeNoSideEffects, "assumeNoSideEffects should be null by default");
        assertNull(config.assumeNoExternalSideEffects, "assumeNoExternalSideEffects should be null by default");
        assertNull(config.assumeNoEscapingParameters, "assumeNoEscapingParameters should be null by default");
        assertNull(config.assumeNoExternalReturnValues, "assumeNoExternalReturnValues should be null by default");
        assertNull(config.assumeValues, "assumeValues should be null by default");
        assertNull(config.keepPackageNames, "keepPackageNames should be null by default");
        assertNull(config.keepAttributes, "keepAttributes should be null by default");
        assertNull(config.adaptClassStrings, "adaptClassStrings should be null by default");
        assertNull(config.adaptResourceFileNames, "adaptResourceFileNames should be null by default");
        assertNull(config.adaptResourceFileContents, "adaptResourceFileContents should be null by default");
        assertNull(config.keyStores, "keyStores should be null by default");
        assertNull(config.keyStorePasswords, "keyStorePasswords should be null by default");
        assertNull(config.keyAliases, "keyAliases should be null by default");
        assertNull(config.keyPasswords, "keyPasswords should be null by default");
        assertNull(config.note, "note should be null by default");
        assertNull(config.warn, "warn should be null by default");
    }

    /**
     * Tests that ClassPath fields are initialized to null.
     */
    @Test
    public void testClassPathFieldsInitializedToNull() {
        // Act
        Configuration config = new Configuration();

        // Assert
        assertNull(config.programJars, "programJars should be null by default");
        assertNull(config.libraryJars, "libraryJars should be null by default");
    }

    /**
     * Tests that fields can be modified after construction.
     * Verifies that the Configuration object is mutable.
     */
    @Test
    public void testFieldsAreMutable() {
        // Arrange
        Configuration config = new Configuration();

        // Act - Modify various fields
        config.shrink = false;
        config.optimize = false;
        config.obfuscate = false;
        config.zipAlign = 4;
        config.targetClassVersion = 52;
        config.optimizationPasses = 5;
        config.verbose = true;

        // Assert - Verify changes were applied
        assertFalse(config.shrink, "shrink should be modifiable");
        assertFalse(config.optimize, "optimize should be modifiable");
        assertFalse(config.obfuscate, "obfuscate should be modifiable");
        assertEquals(4, config.zipAlign, "zipAlign should be modifiable");
        assertEquals(52, config.targetClassVersion, "targetClassVersion should be modifiable");
        assertEquals(5, config.optimizationPasses, "optimizationPasses should be modifiable");
        assertTrue(config.verbose, "verbose should be modifiable");
    }

    /**
     * Tests that File fields can be set after construction.
     */
    @Test
    public void testFileFieldsAreMutable() {
        // Arrange
        Configuration config = new Configuration();
        File testFile = new File("test.txt");

        // Act
        config.printSeeds = testFile;
        config.extraJar = testFile;

        // Assert
        assertSame(testFile, config.printSeeds, "printSeeds should be modifiable");
        assertSame(testFile, config.extraJar, "extraJar should be modifiable");
    }
}
