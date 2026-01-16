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
 * Comprehensive tests for ProGuardTask.keypassword(String) method.
 * This method adds key passwords to the configuration for APK signing.
 * It adds the password string to configuration.keyPasswords list.
 * The key password is used to protect the private key within the keystore.
 */
public class ProGuardTaskClaude_keypasswordTest {

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
    // Tests for keypassword(String) Method
    // ========================================

    @Test
    public void testKeypassword_initializesKeyPasswords() throws Exception {
        assertNull(task.configuration.keyPasswords, "keyPasswords should initially be null");

        task.keypassword("keypass123");

        assertNotNull(task.configuration.keyPasswords, "keyPasswords should be initialized");
        assertEquals(1, task.configuration.keyPasswords.size(), "Should have 1 password");
    }

    @Test
    public void testKeypassword_addsPassword() throws Exception {
        task.keypassword("myKeyPassword");

        assertTrue(task.configuration.keyPasswords.contains("myKeyPassword"),
                  "Should contain the password");
    }

    @Test
    public void testKeypassword_multiplePasswords() throws Exception {
        task.keypassword("keypass1");
        task.keypassword("keypass2");

        assertEquals(2, task.configuration.keyPasswords.size(), "Should have 2 passwords");
        assertTrue(task.configuration.keyPasswords.contains("keypass1"), "Should contain keypass1");
        assertTrue(task.configuration.keyPasswords.contains("keypass2"), "Should contain keypass2");
    }

    @Test
    public void testKeypassword_withEmptyString() throws Exception {
        task.keypassword("");

        assertTrue(task.configuration.keyPasswords.contains(""),
                  "Should allow empty string password");
        assertEquals(1, task.configuration.keyPasswords.size(), "Should have 1 password");
    }

    @Test
    public void testKeypassword_withNullPassword() throws Exception {
        task.keypassword(null);

        assertTrue(task.configuration.keyPasswords.contains(null),
                  "Should allow null password");
        assertEquals(1, task.configuration.keyPasswords.size(), "Should have 1 password");
    }

    @Test
    public void testKeypassword_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keypassword("password");

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.keyPasswords, "keyPasswords should be initialized");
    }

    @Test
    public void testKeypassword_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.keypassword("password");

        assertNotNull(task.configuration.keyPasswords, "keyPasswords should be initialized");
    }

    @Test
    public void testKeypassword_accumulatesPasswords() throws Exception {
        task.keypassword("password1");
        task.keypassword("password2");
        task.keypassword("password3");

        assertEquals(3, task.configuration.keyPasswords.size(), "Should accumulate all passwords");
        assertTrue(task.configuration.keyPasswords.contains("password1"));
        assertTrue(task.configuration.keyPasswords.contains("password2"));
        assertTrue(task.configuration.keyPasswords.contains("password3"));
    }

    @Test
    public void testKeypassword_preservesOrder() throws Exception {
        task.keypassword("first");
        task.keypassword("second");
        task.keypassword("third");

        assertEquals("first", task.configuration.keyPasswords.get(0), "First password should be at index 0");
        assertEquals("second", task.configuration.keyPasswords.get(1), "Second password should be at index 1");
        assertEquals("third", task.configuration.keyPasswords.get(2), "Third password should be at index 2");
    }

    @Test
    public void testKeypassword_allowsDuplicates() throws Exception {
        task.keypassword("samepassword");
        task.keypassword("samepassword");

        assertEquals(2, task.configuration.keyPasswords.size(), "Should allow duplicate passwords");
        assertEquals("samepassword", task.configuration.keyPasswords.get(0));
        assertEquals("samepassword", task.configuration.keyPasswords.get(1));
    }

    @Test
    public void testKeypassword_withSpecialCharacters() throws Exception {
        String complexPassword = "K3y!P@ss#W0rd$";
        task.keypassword(complexPassword);

        assertTrue(task.configuration.keyPasswords.contains(complexPassword),
                  "Should handle passwords with special characters");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testKeypassword_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.android();

        task.keypassword("keypass123");

        assertTrue(task.configuration.keyPasswords.contains("keypass123"), "Should contain password");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testKeypassword_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.keypassword("password");

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    @Test
    public void testKeypassword_withKeystore() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.keypassword("keypass123");

        assertNotNull(task.configuration.keyStores, "Should have keystores");
        assertTrue(task.configuration.keyPasswords.contains("keypass123"),
                  "Should have key password");
    }

    @Test
    public void testKeypassword_withKeystorePassword() throws Exception {
        task.keystorepassword("storepass");
        task.keypassword("keypass");

        assertNotNull(task.configuration.keyStorePasswords, "Should have keystore passwords");
        assertTrue(task.configuration.keyPasswords.contains("keypass"),
                  "Should have key password");
    }

    @Test
    public void testKeypassword_withKeyAlias() throws Exception {
        task.keyalias("mykey");
        task.keypassword("keypass");

        assertNotNull(task.configuration.keyAliases, "Should have key aliases");
        assertTrue(task.configuration.keyPasswords.contains("keypass"),
                  "Should have key password");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testKeypassword_forAndroidAPKSigning() throws Exception {
        // Android APK signing with key password
        File keystoreFile = temporaryFolder.resolve("release.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("releaseStorePassword");
        task.keyalias("releasekey");
        task.keypassword("releaseKeyPassword");

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyPasswords.contains("releaseKeyPassword"),
                  "Should have key password");
    }

    @Test
    public void testKeypassword_forDebugBuild() throws Exception {
        // Debug build with debug key password
        task.android();
        task.dontobfuscate();
        task.keypassword("android");

        assertTrue(task.configuration.keyPasswords.contains("android"),
                  "Should support debug key password");
    }

    @Test
    public void testKeypassword_forReleaseBuild() throws Exception {
        // Release build with release key password
        task.android();
        task.keypassword("secureKeyPassword123!");

        assertTrue(task.configuration.keyPasswords.contains("secureKeyPassword123!"),
                  "Should support release key password");
    }

    @Test
    public void testKeypassword_multipleSigningConfigs() throws Exception {
        // Multiple signing configurations with different passwords
        task.keypassword("debugKeyPassword");
        task.keypassword("releaseKeyPassword");

        assertEquals(2, task.configuration.keyPasswords.size(),
                    "Should support multiple signing configuration passwords");
    }

    @Test
    public void testKeypassword_withStrongPassword() throws Exception {
        // Strong password for production
        String strongPassword = "K3y_P@$$w0rd_2024#Secure!";
        task.keypassword(strongPassword);

        assertTrue(task.configuration.keyPasswords.contains(strongPassword),
                  "Should support strong passwords");
    }

    @Test
    public void testKeypassword_fromEnvironmentVariable() throws Exception {
        // Simulating password from environment variable
        String envPassword = System.getenv().getOrDefault("KEY_PASSWORD", "defaultKeyPassword");
        task.keypassword(envPassword);

        assertTrue(task.configuration.keyPasswords.contains(envPassword),
                  "Should support passwords from environment variables");
    }

    @Test
    public void testKeypassword_forGooglePlaySigning() throws Exception {
        // Google Play signing with upload key
        File playKeystore = temporaryFolder.resolve("google-play.keystore").toFile();
        playKeystore.createNewFile();

        task.android();
        task.keystore(playKeystore);
        task.keystorepassword("uploadKeyStorePassword");
        task.keyalias("upload");
        task.keypassword("uploadKeyPassword");

        assertTrue(task.configuration.keyPasswords.contains("uploadKeyPassword"),
                  "Should support Google Play signing key password");
    }

    @Test
    public void testKeypassword_withLongPassword() throws Exception {
        // Very long password
        String longPassword = "ThisIsAVeryLongKeyPasswordThatSomeoneDecidedToUseForTheirPrivateKey123456!@#";
        task.keypassword(longPassword);

        assertTrue(task.configuration.keyPasswords.contains(longPassword),
                  "Should support long passwords");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testKeypassword_withWhitespace() throws Exception {
        String passwordWithSpaces = "key password with spaces";
        task.keypassword(passwordWithSpaces);

        assertTrue(task.configuration.keyPasswords.contains(passwordWithSpaces),
                  "Should handle passwords with whitespace");
    }

    @Test
    public void testKeypassword_withUnicodeCharacters() throws Exception {
        String unicodePassword = "ключ密码🔑";
        task.keypassword(unicodePassword);

        assertTrue(task.configuration.keyPasswords.contains(unicodePassword),
                  "Should handle Unicode characters in passwords");
    }

    @Test
    public void testKeypassword_afterManuallySettingList() throws Exception {
        task.configuration.keyPasswords = new java.util.ArrayList<>();
        task.configuration.keyPasswords.add("existingPassword");

        task.keypassword("newPassword");

        assertEquals(2, task.configuration.keyPasswords.size(), "Should add to existing list");
        assertTrue(task.configuration.keyPasswords.contains("existingPassword"),
                  "Should preserve existing password");
        assertTrue(task.configuration.keyPasswords.contains("newPassword"),
                  "Should add new password");
    }

    @Test
    public void testKeypassword_calledFirst() throws Exception {
        // Test calling keypassword before any other configuration
        task.keypassword("password");

        assertNotNull(task.configuration.keyPasswords, "keyPasswords should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeypassword_calledLast() throws Exception {
        // Test calling keypassword after other configuration
        task.verbose();
        task.android();
        task.dontoptimize();

        task.keypassword("password");

        assertTrue(task.configuration.keyPasswords.contains("password"),
                  "Should contain password");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testKeypassword_androidDebugKey() throws Exception {
        // Android debug.keystore default key password is "android"
        task.android();
        task.keypassword("android");

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyPasswords.contains("android"),
                  "Should have debug key password");
    }

    @Test
    public void testKeypassword_androidReleaseKey() throws Exception {
        // Android release key with secure password
        task.android();
        task.keypassword("SecureReleaseKeyPassword!2024");

        assertTrue(task.configuration.keyPasswords.contains("SecureReleaseKeyPassword!2024"),
                  "Should have release key password");
    }

    @Test
    public void testKeypassword_androidMultipleVariants() throws Exception {
        // Multiple Android build variants with different passwords
        task.android();
        task.keypassword("debugKeyPassword");
        task.keypassword("qaKeyPassword");
        task.keypassword("releaseKeyPassword");

        assertEquals(3, task.configuration.keyPasswords.size(),
                    "Should support multiple variant passwords");
    }

    @Test
    public void testKeypassword_androidAppBundle() throws Exception {
        // Android App Bundle signing
        task.android();
        task.keypassword("bundleKeyPassword");

        assertTrue(task.configuration.keyPasswords.contains("bundleKeyPassword"),
                  "Should support App Bundle signing key password");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testKeypassword_beforeKeystore() throws Exception {
        task.keypassword("keypass123");

        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();
        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyPasswords.contains("keypass123"),
                  "password should be set");
    }

    @Test
    public void testKeypassword_afterKeystore() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();
        task.keystore(keystoreFile);

        task.keypassword("keypass123");

        assertTrue(task.configuration.keyPasswords.contains("keypass123"),
                  "password should be set");
    }

    @Test
    public void testKeypassword_betweenOtherCalls() throws Exception {
        task.verbose();
        task.keypassword("password");
        task.android();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keyPasswords.contains("password"), "password should be set");
        assertTrue(task.configuration.android, "android should be set");
    }

    @Test
    public void testKeypassword_withCompleteSigningConfig() throws Exception {
        File keystoreFile = temporaryFolder.resolve("release.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("storePassword123");
        task.keyalias("releaseKey");
        task.keypassword("keyPassword456");
        task.verbose();

        assertTrue(task.configuration.keyPasswords.contains("keyPassword456"),
                  "password should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testKeypassword_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.android();

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.keypassword("password");

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.keyPasswords.contains("password"), "password should be set");
    }

    @Test
    public void testKeypassword_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.android();

        task.keypassword("password");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.android, "android should remain set");
        assertTrue(task.configuration.keyPasswords.contains("password"), "password should be set");
    }

    @Test
    public void testKeypassword_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.keypassword("password");

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.keyPasswords.contains("password"), "password should be set");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testKeypassword_passwordFromGradleProperties() throws Exception {
        // Simulating password from gradle.properties
        String gradlePropertiesPassword = "keyPasswordFromProperties";
        task.keypassword(gradlePropertiesPassword);

        assertTrue(task.configuration.keyPasswords.contains(gradlePropertiesPassword),
                  "Should support passwords from gradle.properties");
    }

    @Test
    public void testKeypassword_passwordFromSecureStorage() throws Exception {
        // Simulating password from secure storage (like keychain)
        String securePassword = "securelyStoredKeyPassword";
        task.keypassword(securePassword);

        assertTrue(task.configuration.keyPasswords.contains(securePassword),
                  "Should support passwords from secure storage");
    }

    @Test
    public void testKeypassword_forCICD() throws Exception {
        // CI/CD environment with password from secrets
        String ciPassword = "CI_CD_KEY_SECRET_PASSWORD";
        task.keypassword(ciPassword);

        assertTrue(task.configuration.keyPasswords.contains(ciPassword),
                  "Should support passwords for CI/CD");
    }

    @Test
    public void testKeypassword_multipleEnvironments() throws Exception {
        // Different passwords for different environments
        task.keypassword("devKeyPassword");
        task.keypassword("stagingKeyPassword");
        task.keypassword("prodKeyPassword");

        assertEquals(3, task.configuration.keyPasswords.size(),
                    "Should support multiple environment passwords");
    }

    @Test
    public void testKeypassword_withNumericOnlyPassword() throws Exception {
        // Numeric-only password
        task.keypassword("987654321");

        assertTrue(task.configuration.keyPasswords.contains("987654321"),
                  "Should support numeric passwords");
    }

    @Test
    public void testKeypassword_withMixedCasePassword() throws Exception {
        // Mixed case password
        task.keypassword("MiXeDcAsEkEyPaSsWoRd");

        assertTrue(task.configuration.keyPasswords.contains("MiXeDcAsEkEyPaSsWoRd"),
                  "Should support mixed case passwords");
    }

    @Test
    public void testKeypassword_sameAsKeystorePassword() throws Exception {
        // Sometimes key password is same as keystore password
        String samePassword = "samePassword123";
        task.keystorepassword(samePassword);
        task.keypassword(samePassword);

        assertTrue(task.configuration.keyStorePasswords.contains(samePassword),
                  "Should have keystore password");
        assertTrue(task.configuration.keyPasswords.contains(samePassword),
                  "Should have key password");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testKeypassword_configurationNotNull() throws Exception {
        task.keypassword("password");

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testKeypassword_taskStateValid() throws Exception {
        task.keypassword("password");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testKeypassword_listIsNotNull() throws Exception {
        task.keypassword("password");

        assertNotNull(task.configuration.keyPasswords,
                     "keyPasswords should never be null after adding password");
    }

    @Test
    public void testKeypassword_semanticMeaning() throws Exception {
        // Verify the semantic meaning: adds key password for signing
        task.keypassword("myKeyPassword");

        assertTrue(task.configuration.keyPasswords.contains("myKeyPassword"),
                   "keypassword should add password for private key authentication");
    }

    @Test
    public void testKeypassword_changesConfiguration() throws Exception {
        int sizeBefore = task.configuration.keyPasswords == null ? 0 : task.configuration.keyPasswords.size();

        task.keypassword("password");
        int sizeAfter = task.configuration.keyPasswords.size();

        assertEquals(sizeBefore + 1, sizeAfter, "Configuration should have changed");
    }
}
