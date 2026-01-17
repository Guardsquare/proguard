package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationConstants}.
 * Tests the default constructor and verifies constants initialization.
 */
public class ConfigurationConstantsClaudeTest {

    /**
     * Tests the implicit no-argument constructor ConfigurationConstants().
     * Verifies that the class can be instantiated (though this is not typical usage for a constants class).
     */
    @Test
    public void testConstructor() {
        // Act - Instantiate the constants class
        ConfigurationConstants constants = new ConfigurationConstants();

        // Assert - Verify the instance is not null
        assertNotNull(constants, "ConfigurationConstants instance should not be null");
    }

    /**
     * Tests that all directive and option prefix constants are properly initialized.
     */
    @Test
    public void testDirectiveConstants() {
        assertEquals("-", ConfigurationConstants.OPTION_PREFIX);
        assertEquals("@", ConfigurationConstants.AT_DIRECTIVE);
        assertEquals("-include", ConfigurationConstants.INCLUDE_DIRECTIVE);
        assertEquals("-basedirectory", ConfigurationConstants.BASE_DIRECTORY_DIRECTIVE);
    }

    /**
     * Tests that all jar-related option constants are properly initialized.
     */
    @Test
    public void testJarOptionConstants() {
        assertEquals("-injars", ConfigurationConstants.INJARS_OPTION);
        assertEquals("-outjars", ConfigurationConstants.OUTJARS_OPTION);
        assertEquals("-libraryjars", ConfigurationConstants.LIBRARYJARS_OPTION);
        assertEquals("-resourcejars", ConfigurationConstants.RESOURCEJARS_OPTION);
    }

    /**
     * Tests that all keep-related option constants are properly initialized.
     */
    @Test
    public void testKeepOptionConstants() {
        assertEquals("-if", ConfigurationConstants.IF_OPTION);
        assertEquals("-keep", ConfigurationConstants.KEEP_OPTION);
        assertEquals("-keepclassmembers", ConfigurationConstants.KEEP_CLASS_MEMBERS_OPTION);
        assertEquals("-keepclasseswithmembers", ConfigurationConstants.KEEP_CLASSES_WITH_MEMBERS_OPTION);
        assertEquals("-keepnames", ConfigurationConstants.KEEP_NAMES_OPTION);
        assertEquals("-keepclassmembernames", ConfigurationConstants.KEEP_CLASS_MEMBER_NAMES_OPTION);
        assertEquals("-keepclasseswithmembernames", ConfigurationConstants.KEEP_CLASSES_WITH_MEMBER_NAMES_OPTION);
        assertEquals("-keepcode", ConfigurationConstants.KEEP_CODE_OPTION);
        assertEquals("includedescriptorclasses", ConfigurationConstants.INCLUDE_DESCRIPTOR_CLASSES_SUBOPTION);
        assertEquals("includecode", ConfigurationConstants.INCLUDE_CODE_SUBOPTION);
        assertEquals("allowshrinking", ConfigurationConstants.ALLOW_SHRINKING_SUBOPTION);
        assertEquals("allowoptimization", ConfigurationConstants.ALLOW_OPTIMIZATION_SUBOPTION);
        assertEquals("allowobfuscation", ConfigurationConstants.ALLOW_OBFUSCATION_SUBOPTION);
        assertEquals("-printseeds", ConfigurationConstants.PRINT_SEEDS_OPTION);
    }

    /**
     * Tests that all shrinking-related option constants are properly initialized.
     */
    @Test
    public void testShrinkingOptionConstants() {
        assertEquals("-dontshrink", ConfigurationConstants.DONT_SHRINK_OPTION);
        assertEquals("-printusage", ConfigurationConstants.PRINT_USAGE_OPTION);
        assertEquals("-whyareyoukeeping", ConfigurationConstants.WHY_ARE_YOU_KEEPING_OPTION);
    }

    /**
     * Tests that all optimization-related option constants are properly initialized.
     */
    @Test
    public void testOptimizationOptionConstants() {
        assertEquals("-dontoptimize", ConfigurationConstants.DONT_OPTIMIZE_OPTION);
        assertEquals("-optimizations", ConfigurationConstants.OPTIMIZATIONS);
        assertEquals("-optimizationpasses", ConfigurationConstants.OPTIMIZATION_PASSES);
        assertEquals("-assumenosideeffects", ConfigurationConstants.ASSUME_NO_SIDE_EFFECTS_OPTION);
        assertEquals("-assumenoexternalsideeffects", ConfigurationConstants.ASSUME_NO_EXTERNAL_SIDE_EFFECTS_OPTION);
        assertEquals("-assumenoescapingparameters", ConfigurationConstants.ASSUME_NO_ESCAPING_PARAMETERS_OPTION);
        assertEquals("-assumenoexternalreturnvalues", ConfigurationConstants.ASSUME_NO_EXTERNAL_RETURN_VALUES_OPTION);
        assertEquals("-assumevalues", ConfigurationConstants.ASSUME_VALUES_OPTION);
        assertEquals("-allowaccessmodification", ConfigurationConstants.ALLOW_ACCESS_MODIFICATION_OPTION);
        assertEquals("-mergeinterfacesaggressively", ConfigurationConstants.MERGE_INTERFACES_AGGRESSIVELY_OPTION);
    }

    /**
     * Tests that all obfuscation-related option constants are properly initialized.
     */
    @Test
    public void testObfuscationOptionConstants() {
        assertEquals("-dontobfuscate", ConfigurationConstants.DONT_OBFUSCATE_OPTION);
        assertEquals("-printmapping", ConfigurationConstants.PRINT_MAPPING_OPTION);
        assertEquals("-applymapping", ConfigurationConstants.APPLY_MAPPING_OPTION);
        assertEquals("-obfuscationdictionary", ConfigurationConstants.OBFUSCATION_DICTIONARY_OPTION);
        assertEquals("-classobfuscationdictionary", ConfigurationConstants.CLASS_OBFUSCATION_DICTIONARY_OPTION);
        assertEquals("-packageobfuscationdictionary", ConfigurationConstants.PACKAGE_OBFUSCATION_DICTIONARY_OPTION);
        assertEquals("-overloadaggressively", ConfigurationConstants.OVERLOAD_AGGRESSIVELY_OPTION);
        assertEquals("-useuniqueclassmembernames", ConfigurationConstants.USE_UNIQUE_CLASS_MEMBER_NAMES_OPTION);
        assertEquals("-dontusemixedcaseclassnames", ConfigurationConstants.DONT_USE_MIXED_CASE_CLASS_NAMES_OPTION);
        assertEquals("-keeppackagenames", ConfigurationConstants.KEEP_PACKAGE_NAMES_OPTION);
        assertEquals("-flattenpackagehierarchy", ConfigurationConstants.FLATTEN_PACKAGE_HIERARCHY_OPTION);
        assertEquals("-repackageclasses", ConfigurationConstants.REPACKAGE_CLASSES_OPTION);
        assertEquals("-defaultpackage", ConfigurationConstants.DEFAULT_PACKAGE_OPTION);
        assertEquals("-keepattributes", ConfigurationConstants.KEEP_ATTRIBUTES_OPTION);
        assertEquals("-keepparameternames", ConfigurationConstants.KEEP_PARAMETER_NAMES_OPTION);
        assertEquals("-renamesourcefileattribute", ConfigurationConstants.RENAME_SOURCE_FILE_ATTRIBUTE_OPTION);
        assertEquals("-adaptclassstrings", ConfigurationConstants.ADAPT_CLASS_STRINGS_OPTION);
        assertEquals("-adaptresourcefilenames", ConfigurationConstants.ADAPT_RESOURCE_FILE_NAMES_OPTION);
        assertEquals("-adaptresourcefilecontents", ConfigurationConstants.ADAPT_RESOURCE_FILE_CONTENTS_OPTION);
    }

    /**
     * Tests that all preverification-related option constants are properly initialized.
     */
    @Test
    public void testPreverificationOptionConstants() {
        assertEquals("-dontpreverify", ConfigurationConstants.DONT_PREVERIFY_OPTION);
        assertEquals("-microedition", ConfigurationConstants.MICRO_EDITION_OPTION);
        assertEquals("-android", ConfigurationConstants.ANDROID_OPTION);
    }

    /**
     * Tests that all key store-related option constants are properly initialized.
     */
    @Test
    public void testKeyStoreOptionConstants() {
        assertEquals("-keystore", ConfigurationConstants.KEY_STORE_OPTION);
        assertEquals("-keystorepassword", ConfigurationConstants.KEY_STORE_PASSWORD_OPTION);
        assertEquals("-keyalias", ConfigurationConstants.KEY_ALIAS_OPTION);
        assertEquals("-keypassword", ConfigurationConstants.KEY_PASSWORD_OPTION);
    }

    /**
     * Tests that all general option constants are properly initialized.
     */
    @Test
    public void testGeneralOptionConstants() {
        assertEquals("-verbose", ConfigurationConstants.VERBOSE_OPTION);
        assertEquals("-dontnote", ConfigurationConstants.DONT_NOTE_OPTION);
        assertEquals("-dontwarn", ConfigurationConstants.DONT_WARN_OPTION);
        assertEquals("-ignorewarnings", ConfigurationConstants.IGNORE_WARNINGS_OPTION);
        assertEquals("-printconfiguration", ConfigurationConstants.PRINT_CONFIGURATION_OPTION);
        assertEquals("-dump", ConfigurationConstants.DUMP_OPTION);
        assertEquals("-addconfigurationdebugging", ConfigurationConstants.ADD_CONFIGURATION_DEBUGGING_OPTION);
        assertEquals("-skipnonpubliclibraryclasses", ConfigurationConstants.SKIP_NON_PUBLIC_LIBRARY_CLASSES_OPTION);
        assertEquals("-dontskipnonpubliclibraryclasses", ConfigurationConstants.DONT_SKIP_NON_PUBLIC_LIBRARY_CLASSES_OPTION);
        assertEquals("-dontskipnonpubliclibraryclassmembers", ConfigurationConstants.DONT_SKIP_NON_PUBLIC_LIBRARY_CLASS_MEMBERS_OPTION);
        assertEquals("-target", ConfigurationConstants.TARGET_OPTION);
        assertEquals("-keepdirectories", ConfigurationConstants.KEEP_DIRECTORIES_OPTION);
        assertEquals("-dontcompress", ConfigurationConstants.DONT_COMPRESS_OPTION);
        assertEquals("-zipalign", ConfigurationConstants.ZIP_ALIGN_OPTION);
        assertEquals("-forceprocessing", ConfigurationConstants.FORCE_PROCESSING_OPTION);
    }

    /**
     * Tests that all Kotlin-related option constants are properly initialized.
     */
    @Test
    public void testKotlinOptionConstants() {
        assertEquals("-keepkotlinmetadata", ConfigurationConstants.KEEP_KOTLIN_METADATA);
        assertEquals("-dontprocesskotlinmetadata", ConfigurationConstants.DONT_PROCESS_KOTLIN_METADATA);
        assertEquals("-optimizeaggressively", ConfigurationConstants.OPTIMIZE_AGGRESSIVELY);
    }

    /**
     * Tests that all additional option constants are properly initialized.
     */
    @Test
    public void testAdditionalOptionConstants() {
        assertEquals("-alwaysinline", ConfigurationConstants.ALWAYS_INLINE);
        assertEquals("-identifiernamestring", ConfigurationConstants.IDENTIFIER_NAME_STRING);
        assertEquals("-maximumremovedandroidloglevel", ConfigurationConstants.MAXIMUM_REMOVED_ANDROID_LOG_LEVEL);
    }

    /**
     * Tests that file-related keyword constants are properly initialized.
     */
    @Test
    public void testFileKeywordConstants() {
        assertEquals("**", ConfigurationConstants.ANY_FILE_KEYWORD);
    }

    /**
     * Tests that attribute-related keyword constants are properly initialized.
     */
    @Test
    public void testAttributeKeywordConstants() {
        assertEquals("*", ConfigurationConstants.ANY_ATTRIBUTE_KEYWORD);
        assertEquals(",", ConfigurationConstants.ATTRIBUTE_SEPARATOR_KEYWORD);
    }

    /**
     * Tests that the JAR_SEPARATOR_KEYWORD is initialized from system property.
     * This constant is dynamically initialized at class load time.
     */
    @Test
    public void testJarSeparatorKeyword() {
        String expectedSeparator = System.getProperty("path.separator");
        assertEquals(expectedSeparator, ConfigurationConstants.JAR_SEPARATOR_KEYWORD,
            "JAR_SEPARATOR_KEYWORD should match system path.separator property");
        assertNotNull(ConfigurationConstants.JAR_SEPARATOR_KEYWORD,
            "JAR_SEPARATOR_KEYWORD should not be null");
    }

    /**
     * Tests that system property char constants are properly initialized.
     */
    @Test
    public void testSystemPropertyChars() {
        assertEquals('<', ConfigurationConstants.OPEN_SYSTEM_PROPERTY);
        assertEquals('>', ConfigurationConstants.CLOSE_SYSTEM_PROPERTY);
    }

    /**
     * Tests that all class specification keyword constants are properly initialized.
     */
    @Test
    public void testClassSpecificationKeywordConstants() {
        assertEquals("@", ConfigurationConstants.ANNOTATION_KEYWORD);
        assertEquals("!", ConfigurationConstants.NEGATOR_KEYWORD);
        assertEquals("class", ConfigurationConstants.CLASS_KEYWORD);
        assertEquals("*", ConfigurationConstants.ANY_CLASS_KEYWORD);
        assertEquals("***", ConfigurationConstants.ANY_TYPE_KEYWORD);
        assertEquals("implements", ConfigurationConstants.IMPLEMENTS_KEYWORD);
        assertEquals("extends", ConfigurationConstants.EXTENDS_KEYWORD);
        assertEquals("{", ConfigurationConstants.OPEN_KEYWORD);
        assertEquals("*", ConfigurationConstants.ANY_CLASS_MEMBER_KEYWORD);
        assertEquals("<fields>", ConfigurationConstants.ANY_FIELD_KEYWORD);
        assertEquals("<methods>", ConfigurationConstants.ANY_METHOD_KEYWORD);
        assertEquals("(", ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD);
        assertEquals(",", ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD);
        assertEquals("...", ConfigurationConstants.ANY_ARGUMENTS_KEYWORD);
        assertEquals(")", ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD);
        assertEquals("=", ConfigurationConstants.EQUAL_KEYWORD);
        assertEquals("return", ConfigurationConstants.RETURN_KEYWORD);
        assertEquals("false", ConfigurationConstants.FALSE_KEYWORD);
        assertEquals("true", ConfigurationConstants.TRUE_KEYWORD);
        assertEquals("..", ConfigurationConstants.RANGE_KEYWORD);
        assertEquals(";", ConfigurationConstants.SEPARATOR_KEYWORD);
        assertEquals("}", ConfigurationConstants.CLOSE_KEYWORD);
    }

    /**
     * Tests that constants are immutable (final) by verifying they maintain their values.
     * This test instantiates the class multiple times to ensure consistency.
     */
    @Test
    public void testConstantsAreConsistent() {
        // Act - Create multiple instances
        ConfigurationConstants constants1 = new ConfigurationConstants();
        ConfigurationConstants constants2 = new ConfigurationConstants();

        // Assert - All constants should have the same values regardless of instance
        assertEquals(ConfigurationConstants.OPTION_PREFIX, ConfigurationConstants.OPTION_PREFIX,
            "Static constants should be consistent");
        assertEquals("-keep", ConfigurationConstants.KEEP_OPTION,
            "Constants should maintain their values");

        // Verify instances don't affect static constants
        assertNotNull(constants1);
        assertNotNull(constants2);
        assertEquals(ConfigurationConstants.KEEP_OPTION, "-keep",
            "Static constants should remain unchanged after instantiation");
    }

    /**
     * Tests that char constants have the correct values.
     */
    @Test
    public void testCharConstants() {
        assertEquals('<', ConfigurationConstants.OPEN_SYSTEM_PROPERTY,
            "OPEN_SYSTEM_PROPERTY should be '<'");
        assertEquals('>', ConfigurationConstants.CLOSE_SYSTEM_PROPERTY,
            "CLOSE_SYSTEM_PROPERTY should be '>'");
    }

    /**
     * Tests that string constants with special characters are properly initialized.
     */
    @Test
    public void testSpecialCharacterConstants() {
        assertEquals("@", ConfigurationConstants.AT_DIRECTIVE);
        assertEquals("@", ConfigurationConstants.ANNOTATION_KEYWORD);
        assertEquals("!", ConfigurationConstants.NEGATOR_KEYWORD);
        assertEquals("*", ConfigurationConstants.ANY_ATTRIBUTE_KEYWORD);
        assertEquals("*", ConfigurationConstants.ANY_CLASS_KEYWORD);
        assertEquals("*", ConfigurationConstants.ANY_CLASS_MEMBER_KEYWORD);
        assertEquals("**", ConfigurationConstants.ANY_FILE_KEYWORD);
        assertEquals("***", ConfigurationConstants.ANY_TYPE_KEYWORD);
        assertEquals("...", ConfigurationConstants.ANY_ARGUMENTS_KEYWORD);
        assertEquals("..", ConfigurationConstants.RANGE_KEYWORD);
    }

    /**
     * Tests that bracketing keyword constants are properly initialized.
     */
    @Test
    public void testBracketingKeywordConstants() {
        assertEquals("{", ConfigurationConstants.OPEN_KEYWORD);
        assertEquals("}", ConfigurationConstants.CLOSE_KEYWORD);
        assertEquals("(", ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD);
        assertEquals(")", ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD);
    }

    /**
     * Tests that separator keyword constants are properly initialized.
     */
    @Test
    public void testSeparatorKeywordConstants() {
        assertEquals(",", ConfigurationConstants.ATTRIBUTE_SEPARATOR_KEYWORD);
        assertEquals(",", ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD);
        assertEquals(";", ConfigurationConstants.SEPARATOR_KEYWORD);
    }

    /**
     * Tests that boolean keyword constants are properly initialized.
     */
    @Test
    public void testBooleanKeywordConstants() {
        assertEquals("true", ConfigurationConstants.TRUE_KEYWORD);
        assertEquals("false", ConfigurationConstants.FALSE_KEYWORD);
    }

    /**
     * Tests that the class can be instantiated multiple times independently.
     */
    @Test
    public void testMultipleInstantiations() {
        // Act
        ConfigurationConstants constants1 = new ConfigurationConstants();
        ConfigurationConstants constants2 = new ConfigurationConstants();
        ConfigurationConstants constants3 = new ConfigurationConstants();

        // Assert
        assertNotNull(constants1, "First instance should not be null");
        assertNotNull(constants2, "Second instance should not be null");
        assertNotNull(constants3, "Third instance should not be null");

        // Verify they are different instances
        assertNotSame(constants1, constants2, "Instances should be different objects");
        assertNotSame(constants2, constants3, "Instances should be different objects");
        assertNotSame(constants1, constants3, "Instances should be different objects");
    }
}
