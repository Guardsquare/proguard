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
 * Comprehensive tests for ProGuardTask.keystorepassword(String) method.
 * This method adds keystore passwords to the configuration for APK signing.
 * It adds the password string to configuration.keyStorePasswords list.
 */
public class ProGuardTaskClaude_keystorepasswordTest {

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
    // Tests for keystorepassword(String) Method
    // ========================================

    @Test
    public void testKeystorepassword_initializesKeyStorePasswords() throws Exception {
        assertNull(task.configuration.keyStorePasswords, "keyStorePasswords should initially be null");

        task.keystorepassword("password123");

        assertNotNull(task.configuration.keyStorePasswords, "keyStorePasswords should be initialized");
        assertEquals(1, task.configuration.keyStorePasswords.size(), "Should have 1 password");
    }

    @Test
    public void testKeystorepassword_addsPassword() throws Exception {
        task.keystorepassword("mypassword");

        assertTrue(task.configuration.keyStorePasswords.contains("mypassword"),
                  "Should contain the password");
    }

    @Test
    public void testKeystorepassword_multiplePasswords() throws Exception {
        task.keystorepassword("password1");
        task.keystorepassword("password2");

        assertEquals(2, task.configuration.keyStorePasswords.size(), "Should have 2 passwords");
        assertTrue(task.configuration.keyStorePasswords.contains("password1"), "Should contain password1");
        assertTrue(task.configuration.keyStorePasswords.contains("password2"), "Should contain password2");
    }

    @Test
    public void testKeystorepassword_withEmptyString() throws Exception {
        task.keystorepassword("");

        assertTrue(task.configuration.keyStorePasswords.contains(""),
                  "Should allow empty string password");
        assertEquals(1, task.configuration.keyStorePasswords.size(), "Should have 1 password");
    }

    @Test
    public void testKeystorepassword_withNullPassword() throws Exception {
        task.keystorepassword(null);

        assertTrue(task.configuration.keyStorePasswords.contains(null),
                  "Should allow null password");
        assertEquals(1, task.configuration.keyStorePasswords.size(), "Should have 1 password");
    }

    @Test
    public void testKeystorepassword_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keystorepassword("password");

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.keyStorePasswords, "keyStorePasswords should be initialized");
    }

    @Test
    public void testKeystorepassword_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.keystorepassword("password");

        assertNotNull(task.configuration.keyStorePasswords, "keyStorePasswords should be initialized");
    }

    @Test
    public void testKeystorepassword_accumulatesPasswords() throws Exception {
        task.keystorepassword("password1");
        task.keystorepassword("password2");
        task.keystorepassword("password3");

        assertEquals(3, task.configuration.keyStorePasswords.size(), "Should accumulate all passwords");
        assertTrue(task.configuration.keyStorePasswords.contains("password1"));
        assertTrue(task.configuration.keyStorePasswords.contains("password2"));
        assertTrue(task.configuration.keyStorePasswords.contains("password3"));
    }

    @Test
    public void testKeystorepassword_preservesOrder() throws Exception {
        task.keystorepassword("first");
        task.keystorepassword("second");
        task.keystorepassword("third");

        assertEquals("first", task.configuration.keyStorePasswords.get(0), "First password should be at index 0");
        assertEquals("second", task.configuration.keyStorePasswords.get(1), "Second password should be at index 1");
        assertEquals("third", task.configuration.keyStorePasswords.get(2), "Third password should be at index 2");
    }

    @Test
    public void testKeystorepassword_allowsDuplicates() throws Exception {
        task.keystorepassword("samepassword");
        task.keystorepassword("samepassword");

        assertEquals(2, task.configuration.keyStorePasswords.size(), "Should allow duplicate passwords");
        assertEquals("samepassword", task.configuration.keyStorePasswords.get(0));
        assertEquals("samepassword", task.configuration.keyStorePasswords.get(1));
    }

    @Test
    public void testKeystorepassword_withSpecialCharacters() throws Exception {
        String complexPassword = "P@ssw0rd!#$%^&*()";
        task.keystorepassword(complexPassword);

        assertTrue(task.configuration.keyStorePasswords.contains(complexPassword),
                  "Should handle passwords with special characters");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testKeystorepassword_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.android();

        task.keystorepassword("password123");

        assertTrue(task.configuration.keyStorePasswords.contains("password123"), "Should contain password");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testKeystorepassword_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.keystorepassword("password");

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    @Test
    public void testKeystorepassword_withKeystore() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.keystorepassword("password123");

        assertNotNull(task.configuration.keyStores, "Should have keystores");
        assertTrue(task.configuration.keyStorePasswords.contains("password123"),
                  "Should have keystore password");
    }

    @Test
    public void testKeystorepassword_withKeyAlias() throws Exception {
        task.keystorepassword("storepass");
        task.keyalias("mykey");

        assertTrue(task.configuration.keyStorePasswords.contains("storepass"),
                  "Should have keystore password");
        assertNotNull(task.configuration.keyAliases, "Should have key aliases");
    }

    @Test
    public void testKeystorepassword_withKeyPassword() throws Exception {
        task.keystorepassword("storepass");
        task.keypassword("keypass");

        assertTrue(task.configuration.keyStorePasswords.contains("storepass"),
                  "Should have keystore password");
        assertNotNull(task.configuration.keyPasswords, "Should have key passwords");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testKeystorepassword_forAndroidAPKSigning() throws Exception {
        // Android APK signing with keystore password
        File keystoreFile = temporaryFolder.resolve("release.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("releaseStorePassword");
        task.keyalias("releasekey");
        task.keypassword("releaseKeyPassword");

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyStorePasswords.contains("releaseStorePassword"),
                  "Should have keystore password");
    }

    @Test
    public void testKeystorepassword_forDebugBuild() throws Exception {
        // Debug build with debug keystore password
        task.android();
        task.dontobfuscate();
        task.keystorepassword("android");

        assertTrue(task.configuration.keyStorePasswords.contains("android"),
                  "Should support debug keystore password");
    }

    @Test
    public void testKeystorepassword_forReleaseBuild() throws Exception {
        // Release build with release keystore password
        task.android();
        task.keystorepassword("secureReleasePassword123!");

        assertTrue(task.configuration.keyStorePasswords.contains("secureReleasePassword123!"),
                  "Should support release keystore password");
    }

    @Test
    public void testKeystorepassword_multipleSigningConfigs() throws Exception {
        // Multiple signing configurations with different passwords
        task.keystorepassword("debugPassword");
        task.keystorepassword("releasePassword");

        assertEquals(2, task.configuration.keyStorePasswords.size(),
                    "Should support multiple signing configuration passwords");
    }

    @Test
    public void testKeystorepassword_withStrongPassword() throws Exception {
        // Strong password for production
        String strongPassword = "Pr0dU(t!0n_P@$$w0rd_2024#";
        task.keystorepassword(strongPassword);

        assertTrue(task.configuration.keyStorePasswords.contains(strongPassword),
                  "Should support strong passwords");
    }

    @Test
    public void testKeystorepassword_fromEnvironmentVariable() throws Exception {
        // Simulating password from environment variable
        String envPassword = System.getenv().getOrDefault("KEYSTORE_PASSWORD", "defaultPassword");
        task.keystorepassword(envPassword);

        assertTrue(task.configuration.keyStorePasswords.contains(envPassword),
                  "Should support passwords from environment variables");
    }

    @Test
    public void testKeystorepassword_forGooglePlaySigning() throws Exception {
        // Google Play signing with upload key
        File playKeystore = temporaryFolder.resolve("google-play.keystore").toFile();
        playKeystore.createNewFile();

        task.android();
        task.keystore(playKeystore);
        task.keystorepassword("uploadKeyStorePassword");
        task.keyalias("upload");

        assertTrue(task.configuration.keyStorePasswords.contains("uploadKeyStorePassword"),
                  "Should support Google Play signing password");
    }

    @Test
    public void testKeystorepassword_withLongPassword() throws Exception {
        // Very long password
        String longPassword = "ThisIsAVeryLongPasswordThatSomeoneDecidedToUseForTheirKeystore123456!@#";
        task.keystorepassword(longPassword);

        assertTrue(task.configuration.keyStorePasswords.contains(longPassword),
                  "Should support long passwords");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testKeystorepassword_withWhitespace() throws Exception {
        String passwordWithSpaces = "password with spaces";
        task.keystorepassword(passwordWithSpaces);

        assertTrue(task.configuration.keyStorePasswords.contains(passwordWithSpaces),
                  "Should handle passwords with whitespace");
    }

    @Test
    public void testKeystorepassword_withUnicodeCharacters() throws Exception {
        String unicodePassword = "пароль密码🔒";
        task.keystorepassword(unicodePassword);

        assertTrue(task.configuration.keyStorePasswords.contains(unicodePassword),
                  "Should handle Unicode characters in passwords");
    }

    @Test
    public void testKeystorepassword_afterManuallySettingList() throws Exception {
        task.configuration.keyStorePasswords = new java.util.ArrayList<>();
        task.configuration.keyStorePasswords.add("existingPassword");

        task.keystorepassword("newPassword");

        assertEquals(2, task.configuration.keyStorePasswords.size(), "Should add to existing list");
        assertTrue(task.configuration.keyStorePasswords.contains("existingPassword"),
                  "Should preserve existing password");
        assertTrue(task.configuration.keyStorePasswords.contains("newPassword"),
                  "Should add new password");
    }

    @Test
    public void testKeystorepassword_calledFirst() throws Exception {
        // Test calling keystorepassword before any other configuration
        task.keystorepassword("password");

        assertNotNull(task.configuration.keyStorePasswords, "keyStorePasswords should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeystorepassword_calledLast() throws Exception {
        // Test calling keystorepassword after other configuration
        task.verbose();
        task.android();
        task.dontoptimize();

        task.keystorepassword("password");

        assertTrue(task.configuration.keyStorePasswords.contains("password"),
                  "Should contain password");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testKeystorepassword_androidDebugKeystore() throws Exception {
        // Android debug.keystore default password is "android"
        task.android();
        task.keystorepassword("android");

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyStorePasswords.contains("android"),
                  "Should have debug keystore password");
    }

    @Test
    public void testKeystorepassword_androidReleaseKeystore() throws Exception {
        // Android release keystore with secure password
        task.android();
        task.keystorepassword("SecureReleasePassword!2024");

        assertTrue(task.configuration.keyStorePasswords.contains("SecureReleasePassword!2024"),
                  "Should have release keystore password");
    }

    @Test
    public void testKeystorepassword_androidMultipleVariants() throws Exception {
        // Multiple Android build variants with different passwords
        task.android();
        task.keystorepassword("debugPassword");
        task.keystorepassword("qaPassword");
        task.keystorepassword("releasePassword");

        assertEquals(3, task.configuration.keyStorePasswords.size(),
                    "Should support multiple variant passwords");
    }

    @Test
    public void testKeystorepassword_androidAppBundle() throws Exception {
        // Android App Bundle signing
        task.android();
        task.keystorepassword("bundleSigningPassword");

        assertTrue(task.configuration.keyStorePasswords.contains("bundleSigningPassword"),
                  "Should support App Bundle signing password");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testKeystorepassword_beforeKeystore() throws Exception {
        task.keystorepassword("password123");

        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();
        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStorePasswords.contains("password123"),
                  "password should be set");
    }

    @Test
    public void testKeystorepassword_afterKeystore() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();
        task.keystore(keystoreFile);

        task.keystorepassword("password123");

        assertTrue(task.configuration.keyStorePasswords.contains("password123"),
                  "password should be set");
    }

    @Test
    public void testKeystorepassword_betweenOtherCalls() throws Exception {
        task.verbose();
        task.keystorepassword("password");
        task.android();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keyStorePasswords.contains("password"), "password should be set");
        assertTrue(task.configuration.android, "android should be set");
    }

    @Test
    public void testKeystorepassword_withCompleteSigningConfig() throws Exception {
        File keystoreFile = temporaryFolder.resolve("release.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("storePassword123");
        task.keyalias("releaseKey");
        task.keypassword("keyPassword456");
        task.verbose();

        assertTrue(task.configuration.keyStorePasswords.contains("storePassword123"),
                  "password should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testKeystorepassword_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.android();

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.keystorepassword("password");

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.keyStorePasswords.contains("password"), "password should be set");
    }

    @Test
    public void testKeystorepassword_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.android();

        task.keystorepassword("password");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.android, "android should remain set");
        assertTrue(task.configuration.keyStorePasswords.contains("password"), "password should be set");
    }

    @Test
    public void testKeystorepassword_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.keystorepassword("password");

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.keyStorePasswords.contains("password"), "password should be set");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testKeystorepassword_passwordFromGradleProperties() throws Exception {
        // Simulating password from gradle.properties
        String gradlePropertiesPassword = "passwordFromProperties";
        task.keystorepassword(gradlePropertiesPassword);

        assertTrue(task.configuration.keyStorePasswords.contains(gradlePropertiesPassword),
                  "Should support passwords from gradle.properties");
    }

    @Test
    public void testKeystorepassword_passwordFromSecureStorage() throws Exception {
        // Simulating password from secure storage (like keychain)
        String securePassword = "securelyStoredPassword";
        task.keystorepassword(securePassword);

        assertTrue(task.configuration.keyStorePasswords.contains(securePassword),
                  "Should support passwords from secure storage");
    }

    @Test
    public void testKeystorepassword_forCICD() throws Exception {
        // CI/CD environment with password from secrets
        String ciPassword = "CI_CD_SECRET_PASSWORD";
        task.keystorepassword(ciPassword);

        assertTrue(task.configuration.keyStorePasswords.contains(ciPassword),
                  "Should support passwords for CI/CD");
    }

    @Test
    public void testKeystorepassword_multipleEnvironments() throws Exception {
        // Different passwords for different environments
        task.keystorepassword("devPassword");
        task.keystorepassword("stagingPassword");
        task.keystorepassword("prodPassword");

        assertEquals(3, task.configuration.keyStorePasswords.size(),
                    "Should support multiple environment passwords");
    }

    @Test
    public void testKeystorepassword_withNumericOnlyPassword() throws Exception {
        // Numeric-only password
        task.keystorepassword("123456789");

        assertTrue(task.configuration.keyStorePasswords.contains("123456789"),
                  "Should support numeric passwords");
    }

    @Test
    public void testKeystorepassword_withMixedCasePassword() throws Exception {
        // Mixed case password
        task.keystorepassword("MiXeDcAsEpAsSwOrD");

        assertTrue(task.configuration.keyStorePasswords.contains("MiXeDcAsEpAsSwOrD"),
                  "Should support mixed case passwords");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testKeystorepassword_configurationNotNull() throws Exception {
        task.keystorepassword("password");

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testKeystorepassword_taskStateValid() throws Exception {
        task.keystorepassword("password");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testKeystorepassword_listIsNotNull() throws Exception {
        task.keystorepassword("password");

        assertNotNull(task.configuration.keyStorePasswords,
                     "keyStorePasswords should never be null after adding password");
    }

    @Test
    public void testKeystorepassword_semanticMeaning() throws Exception {
        // Verify the semantic meaning: adds keystore password for signing
        task.keystorepassword("mypassword");

        assertTrue(task.configuration.keyStorePasswords.contains("mypassword"),
                   "keystorepassword should add password for keystore authentication");
    }

    @Test
    public void testKeystorepassword_changesConfiguration() throws Exception {
        int sizeBefore = task.configuration.keyStorePasswords == null ? 0 : task.configuration.keyStorePasswords.size();

        task.keystorepassword("password");
        int sizeAfter = task.configuration.keyStorePasswords.size();

        assertEquals(sizeBefore + 1, sizeAfter, "Configuration should have changed");
    }
}
