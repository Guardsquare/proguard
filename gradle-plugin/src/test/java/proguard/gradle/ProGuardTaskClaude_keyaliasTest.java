package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ProGuardTask.keyalias(String) method.
 * This method adds key aliases to the configuration for APK signing.
 * It adds the alias string to configuration.keyAliases list.
 */
public class ProGuardTaskClaude_keyaliasTest {

    @TempDir
    public Path temporaryFolder;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(temporaryFolder.toFile())
                .build();
        task = project.getTasks().create("proguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        task = null;
    }

    // ========================================
    // Tests for keyalias(String) Method
    // ========================================

    @Test
    public void testKeyalias_initializesKeyAliases() throws Exception {
        assertNull(task.configuration.keyAliases, "keyAliases should initially be null");

        task.keyalias("mykey");

        assertNotNull(task.configuration.keyAliases, "keyAliases should be initialized");
        assertEquals(1, task.configuration.keyAliases.size(), "Should have 1 alias");
    }

    @Test
    public void testKeyalias_addsAlias() throws Exception {
        task.keyalias("releasekey");

        assertTrue(task.configuration.keyAliases.contains("releasekey"),
                  "Should contain the alias");
    }

    @Test
    public void testKeyalias_multipleAliases() throws Exception {
        task.keyalias("debugkey");
        task.keyalias("releasekey");

        assertEquals(2, task.configuration.keyAliases.size(), "Should have 2 aliases");
        assertTrue(task.configuration.keyAliases.contains("debugkey"), "Should contain debugkey");
        assertTrue(task.configuration.keyAliases.contains("releasekey"), "Should contain releasekey");
    }

    @Test
    public void testKeyalias_withEmptyString() throws Exception {
        task.keyalias("");

        assertTrue(task.configuration.keyAliases.contains(""),
                  "Should allow empty string alias");
        assertEquals(1, task.configuration.keyAliases.size(), "Should have 1 alias");
    }

    @Test
    public void testKeyalias_withNullAlias() throws Exception {
        task.keyalias(null);

        assertTrue(task.configuration.keyAliases.contains(null),
                  "Should allow null alias");
        assertEquals(1, task.configuration.keyAliases.size(), "Should have 1 alias");
    }

    @Test
    public void testKeyalias_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keyalias("mykey");

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.keyAliases, "keyAliases should be initialized");
    }

    @Test
    public void testKeyalias_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.keyalias("mykey");

        assertNotNull(task.configuration.keyAliases, "keyAliases should be initialized");
    }

    @Test
    public void testKeyalias_accumulatesAliases() throws Exception {
        task.keyalias("key1");
        task.keyalias("key2");
        task.keyalias("key3");

        assertEquals(3, task.configuration.keyAliases.size(), "Should accumulate all aliases");
        assertTrue(task.configuration.keyAliases.contains("key1"));
        assertTrue(task.configuration.keyAliases.contains("key2"));
        assertTrue(task.configuration.keyAliases.contains("key3"));
    }

    @Test
    public void testKeyalias_preservesOrder() throws Exception {
        task.keyalias("first");
        task.keyalias("second");
        task.keyalias("third");

        assertEquals("first", task.configuration.keyAliases.get(0), "First alias should be at index 0");
        assertEquals("second", task.configuration.keyAliases.get(1), "Second alias should be at index 1");
        assertEquals("third", task.configuration.keyAliases.get(2), "Third alias should be at index 2");
    }

    @Test
    public void testKeyalias_allowsDuplicates() throws Exception {
        task.keyalias("samekey");
        task.keyalias("samekey");

        assertEquals(2, task.configuration.keyAliases.size(), "Should allow duplicate aliases");
        assertEquals("samekey", task.configuration.keyAliases.get(0));
        assertEquals("samekey", task.configuration.keyAliases.get(1));
    }

    @Test
    public void testKeyalias_withSpecialCharacters() throws Exception {
        String aliasWithSpecial = "key-alias_2024";
        task.keyalias(aliasWithSpecial);

        assertTrue(task.configuration.keyAliases.contains(aliasWithSpecial),
                  "Should handle aliases with special characters");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testKeyalias_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.android();

        task.keyalias("mykey");

        assertTrue(task.configuration.keyAliases.contains("mykey"), "Should contain alias");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testKeyalias_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.keyalias("mykey");

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    @Test
    public void testKeyalias_withKeystore() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.keyalias("mykey");

        assertNotNull(task.configuration.keyStores, "Should have keystores");
        assertTrue(task.configuration.keyAliases.contains("mykey"),
                  "Should have key alias");
    }

    @Test
    public void testKeyalias_withKeystorePassword() throws Exception {
        task.keystorepassword("storepass");
        task.keyalias("mykey");

        assertNotNull(task.configuration.keyStorePasswords, "Should have keystore passwords");
        assertTrue(task.configuration.keyAliases.contains("mykey"),
                  "Should have key alias");
    }

    @Test
    public void testKeyalias_withKeyPassword() throws Exception {
        task.keyalias("mykey");
        task.keypassword("keypass");

        assertTrue(task.configuration.keyAliases.contains("mykey"),
                  "Should have key alias");
        assertNotNull(task.configuration.keyPasswords, "Should have key passwords");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testKeyalias_forAndroidAPKSigning() throws Exception {
        // Android APK signing with key alias
        File keystoreFile = temporaryFolder.resolve("release.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("releaseStorePassword");
        task.keyalias("releasekey");
        task.keypassword("releaseKeyPassword");

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyAliases.contains("releasekey"),
                  "Should have key alias");
    }

    @Test
    public void testKeyalias_forDebugBuild() throws Exception {
        // Debug build with debug key alias
        task.android();
        task.dontobfuscate();
        task.keyalias("androiddebugkey");

        assertTrue(task.configuration.keyAliases.contains("androiddebugkey"),
                  "Should support debug key alias");
    }

    @Test
    public void testKeyalias_forReleaseBuild() throws Exception {
        // Release build with release key alias
        task.android();
        task.keyalias("release");

        assertTrue(task.configuration.keyAliases.contains("release"),
                  "Should support release key alias");
    }

    @Test
    public void testKeyalias_multipleSigningConfigs() throws Exception {
        // Multiple signing configurations with different aliases
        task.keyalias("debugkey");
        task.keyalias("releasekey");

        assertEquals(2, task.configuration.keyAliases.size(),
                    "Should support multiple signing configuration aliases");
    }

    @Test
    public void testKeyalias_descriptiveAlias() throws Exception {
        // Descriptive key alias
        task.keyalias("my-company-release-key-2024");

        assertTrue(task.configuration.keyAliases.contains("my-company-release-key-2024"),
                  "Should support descriptive aliases");
    }

    @Test
    public void testKeyalias_forGooglePlaySigning() throws Exception {
        // Google Play signing with upload key
        File playKeystore = temporaryFolder.resolve("google-play.keystore").toFile();
        playKeystore.createNewFile();

        task.android();
        task.keystore(playKeystore);
        task.keystorepassword("uploadKeyStorePassword");
        task.keyalias("upload");

        assertTrue(task.configuration.keyAliases.contains("upload"),
                  "Should support Google Play signing alias");
    }

    @Test
    public void testKeyalias_withVersionInName() throws Exception {
        // Key alias with version
        task.keyalias("releasekey-v2");

        assertTrue(task.configuration.keyAliases.contains("releasekey-v2"),
                  "Should support aliases with version");
    }

    @Test
    public void testKeyalias_withEnvironmentInName() throws Exception {
        // Key alias with environment name
        task.keyalias("production-key");

        assertTrue(task.configuration.keyAliases.contains("production-key"),
                  "Should support aliases with environment");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testKeyalias_withWhitespace() throws Exception {
        String aliasWithSpaces = "key with spaces";
        task.keyalias(aliasWithSpaces);

        assertTrue(task.configuration.keyAliases.contains(aliasWithSpaces),
                  "Should handle aliases with whitespace");
    }

    @Test
    public void testKeyalias_withUpperCase() throws Exception {
        task.keyalias("RELEASEKEY");

        assertTrue(task.configuration.keyAliases.contains("RELEASEKEY"),
                  "Should handle uppercase aliases");
    }

    @Test
    public void testKeyalias_withMixedCase() throws Exception {
        task.keyalias("ReleaseKey");

        assertTrue(task.configuration.keyAliases.contains("ReleaseKey"),
                  "Should handle mixed case aliases");
    }

    @Test
    public void testKeyalias_afterManuallySettingList() throws Exception {
        task.configuration.keyAliases = new java.util.ArrayList<>();
        task.configuration.keyAliases.add("existingKey");

        task.keyalias("newKey");

        assertEquals(2, task.configuration.keyAliases.size(), "Should add to existing list");
        assertTrue(task.configuration.keyAliases.contains("existingKey"),
                  "Should preserve existing alias");
        assertTrue(task.configuration.keyAliases.contains("newKey"),
                  "Should add new alias");
    }

    @Test
    public void testKeyalias_calledFirst() throws Exception {
        // Test calling keyalias before any other configuration
        task.keyalias("mykey");

        assertNotNull(task.configuration.keyAliases, "keyAliases should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeyalias_calledLast() throws Exception {
        // Test calling keyalias after other configuration
        task.verbose();
        task.android();
        task.dontoptimize();

        task.keyalias("mykey");

        assertTrue(task.configuration.keyAliases.contains("mykey"),
                  "Should contain alias");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testKeyalias_androidDebugKey() throws Exception {
        // Android debug.keystore default alias is "androiddebugkey"
        task.android();
        task.keyalias("androiddebugkey");

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyAliases.contains("androiddebugkey"),
                  "Should have debug key alias");
    }

    @Test
    public void testKeyalias_androidReleaseKey() throws Exception {
        // Android release key with custom alias
        task.android();
        task.keyalias("my-release-key");

        assertTrue(task.configuration.keyAliases.contains("my-release-key"),
                  "Should have release key alias");
    }

    @Test
    public void testKeyalias_androidMultipleVariants() throws Exception {
        // Multiple Android build variants with different aliases
        task.android();
        task.keyalias("debug");
        task.keyalias("qa");
        task.keyalias("release");

        assertEquals(3, task.configuration.keyAliases.size(),
                    "Should support multiple variant aliases");
    }

    @Test
    public void testKeyalias_androidAppBundle() throws Exception {
        // Android App Bundle signing
        task.android();
        task.keyalias("bundle-key");

        assertTrue(task.configuration.keyAliases.contains("bundle-key"),
                  "Should support App Bundle signing alias");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testKeyalias_beforeKeystore() throws Exception {
        task.keyalias("mykey");

        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();
        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyAliases.contains("mykey"),
                  "alias should be set");
    }

    @Test
    public void testKeyalias_afterKeystore() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();
        task.keystore(keystoreFile);

        task.keyalias("mykey");

        assertTrue(task.configuration.keyAliases.contains("mykey"),
                  "alias should be set");
    }

    @Test
    public void testKeyalias_betweenOtherCalls() throws Exception {
        task.verbose();
        task.keyalias("mykey");
        task.android();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keyAliases.contains("mykey"), "alias should be set");
        assertTrue(task.configuration.android, "android should be set");
    }

    @Test
    public void testKeyalias_withCompleteSigningConfig() throws Exception {
        File keystoreFile = temporaryFolder.resolve("release.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("storePassword123");
        task.keyalias("releaseKey");
        task.keypassword("keyPassword456");
        task.verbose();

        assertTrue(task.configuration.keyAliases.contains("releaseKey"),
                  "alias should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testKeyalias_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.android();

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.keyalias("mykey");

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.keyAliases.contains("mykey"), "alias should be set");
    }

    @Test
    public void testKeyalias_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.android();

        task.keyalias("mykey");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.android, "android should remain set");
        assertTrue(task.configuration.keyAliases.contains("mykey"), "alias should be set");
    }

    @Test
    public void testKeyalias_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.keyalias("mykey");

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.keyAliases.contains("mykey"), "alias should be set");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testKeyalias_fromGradleProperties() throws Exception {
        // Simulating alias from gradle.properties
        String gradlePropertiesAlias = "aliasFromProperties";
        task.keyalias(gradlePropertiesAlias);

        assertTrue(task.configuration.keyAliases.contains(gradlePropertiesAlias),
                  "Should support aliases from gradle.properties");
    }

    @Test
    public void testKeyalias_fromEnvironmentVariable() throws Exception {
        // Simulating alias from environment variable
        String envAlias = System.getenv().getOrDefault("KEY_ALIAS", "defaultAlias");
        task.keyalias(envAlias);

        assertTrue(task.configuration.keyAliases.contains(envAlias),
                  "Should support aliases from environment variables");
    }

    @Test
    public void testKeyalias_forCICD() throws Exception {
        // CI/CD environment with alias from secrets
        String ciAlias = "CI_RELEASE_KEY";
        task.keyalias(ciAlias);

        assertTrue(task.configuration.keyAliases.contains(ciAlias),
                  "Should support aliases for CI/CD");
    }

    @Test
    public void testKeyalias_multipleEnvironments() throws Exception {
        // Different aliases for different environments
        task.keyalias("dev-key");
        task.keyalias("staging-key");
        task.keyalias("prod-key");

        assertEquals(3, task.configuration.keyAliases.size(),
                    "Should support multiple environment aliases");
    }

    @Test
    public void testKeyalias_withNumericSuffix() throws Exception {
        // Key alias with numeric suffix
        task.keyalias("key1");

        assertTrue(task.configuration.keyAliases.contains("key1"),
                  "Should support aliases with numeric suffix");
    }

    @Test
    public void testKeyalias_withHyphenatedName() throws Exception {
        // Hyphenated key alias
        task.keyalias("my-company-key");

        assertTrue(task.configuration.keyAliases.contains("my-company-key"),
                  "Should support hyphenated aliases");
    }

    @Test
    public void testKeyalias_withUnderscoreName() throws Exception {
        // Underscore key alias
        task.keyalias("my_company_key");

        assertTrue(task.configuration.keyAliases.contains("my_company_key"),
                  "Should support underscored aliases");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testKeyalias_configurationNotNull() throws Exception {
        task.keyalias("mykey");

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testKeyalias_taskStateValid() throws Exception {
        task.keyalias("mykey");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testKeyalias_listIsNotNull() throws Exception {
        task.keyalias("mykey");

        assertNotNull(task.configuration.keyAliases,
                     "keyAliases should never be null after adding alias");
    }

    @Test
    public void testKeyalias_semanticMeaning() throws Exception {
        // Verify the semantic meaning: adds key alias for signing
        task.keyalias("mykey");

        assertTrue(task.configuration.keyAliases.contains("mykey"),
                   "keyalias should add alias for key identification in keystore");
    }

    @Test
    public void testKeyalias_changesConfiguration() throws Exception {
        int sizeBefore = task.configuration.keyAliases == null ? 0 : task.configuration.keyAliases.size();

        task.keyalias("mykey");
        int sizeAfter = task.configuration.keyAliases.size();

        assertEquals(sizeBefore + 1, sizeAfter, "Configuration should have changed");
    }
}
