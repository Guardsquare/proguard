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
 * Comprehensive tests for ProGuardTask.keystore(Object) method.
 * This method adds keystore files to the configuration for APK signing.
 * It converts the Object parameter to a File and adds it to configuration.keyStores list.
 */
public class ProGuardTaskClaude_keystoreTest {

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
    // Tests for keystore(Object) Method
    // ========================================

    @Test
    public void testKeystore_initializesKeyStores() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        assertNull(task.configuration.keyStores, "keyStores should initially be null");

        task.keystore(keystoreFile);

        assertNotNull(task.configuration.keyStores, "keyStores should be initialized");
        assertEquals(1, task.configuration.keyStores.size(), "Should have 1 keystore");
    }

    @Test
    public void testKeystore_addsKeystoreFile() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should contain the keystore file");
    }

    @Test
    public void testKeystore_multipleKeystores() throws Exception {
        File keystore1 = temporaryFolder.resolve("keystore1.jks").toFile();
        File keystore2 = temporaryFolder.resolve("keystore2.jks").toFile();
        keystore1.createNewFile();
        keystore2.createNewFile();

        task.keystore(keystore1);
        task.keystore(keystore2);

        assertEquals(2, task.configuration.keyStores.size(), "Should have 2 keystores");
        assertTrue(task.configuration.keyStores.contains(keystore1), "Should contain keystore1");
        assertTrue(task.configuration.keyStores.contains(keystore2), "Should contain keystore2");
    }

    @Test
    public void testKeystore_withStringPath() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();
        String keystorePath = keystoreFile.getAbsolutePath();

        task.keystore(keystorePath);

        assertNotNull(task.configuration.keyStores, "keyStores should be initialized");
        assertEquals(1, task.configuration.keyStores.size(), "Should have 1 keystore");
        assertEquals(keystoreFile, task.configuration.keyStores.get(0),
                    "Should contain the keystore file");
    }

    @Test
    public void testKeystore_withRelativePath() throws Exception {
        File keystoreFile = new File(temporaryFolder.toFile(), "relative.keystore");
        keystoreFile.createNewFile();

        task.keystore("relative.keystore");

        assertNotNull(task.configuration.keyStores, "keyStores should be initialized");
        assertFalse(task.configuration.keyStores.isEmpty(), "Should have at least 1 keystore");
    }

    @Test
    public void testKeystore_configurationInitialized() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keystore(keystoreFile);

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.keyStores, "keyStores should be initialized");
    }

    @Test
    public void testKeystore_doesNotReturnValue() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        // This is a void method, just verify it executes without error
        task.keystore(keystoreFile);

        assertNotNull(task.configuration.keyStores, "keyStores should be initialized");
    }

    @Test
    public void testKeystore_accumulatesKeystores() throws Exception {
        File keystore1 = temporaryFolder.resolve("keystore1.jks").toFile();
        File keystore2 = temporaryFolder.resolve("keystore2.jks").toFile();
        File keystore3 = temporaryFolder.resolve("keystore3.jks").toFile();
        keystore1.createNewFile();
        keystore2.createNewFile();
        keystore3.createNewFile();

        task.keystore(keystore1);
        task.keystore(keystore2);
        task.keystore(keystore3);

        assertEquals(3, task.configuration.keyStores.size(), "Should accumulate all keystores");
        assertTrue(task.configuration.keyStores.contains(keystore1));
        assertTrue(task.configuration.keyStores.contains(keystore2));
        assertTrue(task.configuration.keyStores.contains(keystore3));
    }

    @Test
    public void testKeystore_preservesOrder() throws Exception {
        File keystore1 = temporaryFolder.resolve("first.jks").toFile();
        File keystore2 = temporaryFolder.resolve("second.jks").toFile();
        File keystore3 = temporaryFolder.resolve("third.jks").toFile();
        keystore1.createNewFile();
        keystore2.createNewFile();
        keystore3.createNewFile();

        task.keystore(keystore1);
        task.keystore(keystore2);
        task.keystore(keystore3);

        assertEquals(keystore1, task.configuration.keyStores.get(0), "First keystore should be at index 0");
        assertEquals(keystore2, task.configuration.keyStores.get(1), "Second keystore should be at index 1");
        assertEquals(keystore3, task.configuration.keyStores.get(2), "Third keystore should be at index 2");
    }

    @Test
    public void testKeystore_allowsDuplicates() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.keystore(keystoreFile);

        assertEquals(2, task.configuration.keyStores.size(), "Should allow duplicate keystores");
        assertEquals(keystoreFile, task.configuration.keyStores.get(0));
        assertEquals(keystoreFile, task.configuration.keyStores.get(1));
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testKeystore_withOtherConfigurationSettings() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.verbose();
        task.android();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile), "Should contain keystore");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testKeystore_doesNotAffectOtherSettings() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.keystore(keystoreFile);

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    @Test
    public void testKeystore_withKeystorePassword() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.keystorepassword("password123");

        assertTrue(task.configuration.keyStores.contains(keystoreFile), "Should contain keystore");
        assertNotNull(task.configuration.keyStorePasswords, "Should have keystore passwords");
    }

    @Test
    public void testKeystore_withKeyAlias() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.keyalias("mykey");

        assertTrue(task.configuration.keyStores.contains(keystoreFile), "Should contain keystore");
        assertNotNull(task.configuration.keyAliases, "Should have key aliases");
    }

    @Test
    public void testKeystore_withKeyPassword() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.keypassword("keypass123");

        assertTrue(task.configuration.keyStores.contains(keystoreFile), "Should contain keystore");
        assertNotNull(task.configuration.keyPasswords, "Should have key passwords");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testKeystore_forAndroidAPKSigning() throws Exception {
        // Android APK signing with keystore
        File keystoreFile = temporaryFolder.resolve("release.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("storepass");
        task.keyalias("releasekey");
        task.keypassword("keypass");

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should have keystore for signing");
    }

    @Test
    public void testKeystore_withJKSKeystore() throws Exception {
        // Java KeyStore (JKS) format
        File keystoreFile = temporaryFolder.resolve("keystore.jks").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should support JKS keystore format");
    }

    @Test
    public void testKeystore_withPKCS12Keystore() throws Exception {
        // PKCS12 keystore format
        File keystoreFile = temporaryFolder.resolve("keystore.p12").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should support PKCS12 keystore format");
    }

    @Test
    public void testKeystore_withBKSKeystore() throws Exception {
        // BKS (BouncyCastle KeyStore) format - used in Android
        File keystoreFile = temporaryFolder.resolve("keystore.bks").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should support BKS keystore format");
    }

    @Test
    public void testKeystore_forDebugBuild() throws Exception {
        // Debug build with debug keystore
        File debugKeystore = temporaryFolder.resolve("debug.keystore").toFile();
        debugKeystore.createNewFile();

        task.android();
        task.dontobfuscate();
        task.keystore(debugKeystore);

        assertTrue(task.configuration.keyStores.contains(debugKeystore),
                  "Should support debug keystore");
    }

    @Test
    public void testKeystore_forReleaseBuild() throws Exception {
        // Release build with release keystore
        File releaseKeystore = temporaryFolder.resolve("release.keystore").toFile();
        releaseKeystore.createNewFile();

        task.android();
        task.keystore(releaseKeystore);
        task.keystorepassword("releasepass");
        task.keyalias("release");

        assertTrue(task.configuration.keyStores.contains(releaseKeystore),
                  "Should support release keystore");
    }

    @Test
    public void testKeystore_multipleSigningConfigs() throws Exception {
        // Multiple signing configurations
        File debugKeystore = temporaryFolder.resolve("debug.keystore").toFile();
        File releaseKeystore = temporaryFolder.resolve("release.keystore").toFile();
        debugKeystore.createNewFile();
        releaseKeystore.createNewFile();

        task.keystore(debugKeystore);
        task.keystore(releaseKeystore);

        assertEquals(2, task.configuration.keyStores.size(),
                    "Should support multiple signing configurations");
    }

    @Test
    public void testKeystore_inBuildDirectory() throws Exception {
        // Keystore in project build directory
        File buildDir = temporaryFolder.resolve("build").toFile();
        buildDir.mkdir();
        File keystoreFile = new File(buildDir, "release.keystore");
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should support keystore in build directory");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testKeystore_withFileObject() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should handle File object");
    }

    @Test
    public void testKeystore_withAbsolutePath() throws Exception {
        File keystoreFile = temporaryFolder.resolve("absolute.keystore").toFile();
        keystoreFile.createNewFile();
        String absolutePath = keystoreFile.getAbsolutePath();

        task.keystore(absolutePath);

        assertNotNull(task.configuration.keyStores, "keyStores should be initialized");
        assertFalse(task.configuration.keyStores.isEmpty(), "Should have keystore");
    }

    @Test
    public void testKeystore_afterManuallySettingList() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.configuration.keyStores = new java.util.ArrayList<>();
        File existingKeystore = temporaryFolder.resolve("existing.keystore").toFile();
        existingKeystore.createNewFile();
        task.configuration.keyStores.add(existingKeystore);

        task.keystore(keystoreFile);

        assertEquals(2, task.configuration.keyStores.size(), "Should add to existing list");
        assertTrue(task.configuration.keyStores.contains(existingKeystore),
                  "Should preserve existing keystore");
        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should add new keystore");
    }

    @Test
    public void testKeystore_calledFirst() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        // Test calling keystore before any other configuration
        task.keystore(keystoreFile);

        assertNotNull(task.configuration.keyStores, "keyStores should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeystore_calledLast() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        // Test calling keystore after other configuration
        task.verbose();
        task.android();
        task.dontoptimize();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should contain keystore");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testKeystore_androidDebugKeystore() throws Exception {
        // Android debug.keystore location
        File debugKeystore = temporaryFolder.resolve("debug.keystore").toFile();
        debugKeystore.createNewFile();

        task.android();
        task.keystore(debugKeystore);

        assertTrue(task.configuration.android, "Should be in Android mode");
        assertTrue(task.configuration.keyStores.contains(debugKeystore),
                  "Should have debug keystore");
    }

    @Test
    public void testKeystore_androidReleaseKeystore() throws Exception {
        // Android release keystore
        File releaseKeystore = temporaryFolder.resolve("release.keystore").toFile();
        releaseKeystore.createNewFile();

        task.android();
        task.keystore(releaseKeystore);
        task.keystorepassword("release_password");
        task.keyalias("release_key");
        task.keypassword("key_password");

        assertTrue(task.configuration.keyStores.contains(releaseKeystore),
                  "Should have release keystore with credentials");
    }

    @Test
    public void testKeystore_androidMultipleVariants() throws Exception {
        // Multiple Android build variants with different keystores
        File debugKeystore = temporaryFolder.resolve("debug.keystore").toFile();
        File qaKeystore = temporaryFolder.resolve("qa.keystore").toFile();
        File releaseKeystore = temporaryFolder.resolve("release.keystore").toFile();
        debugKeystore.createNewFile();
        qaKeystore.createNewFile();
        releaseKeystore.createNewFile();

        task.android();
        task.keystore(debugKeystore);
        task.keystore(qaKeystore);
        task.keystore(releaseKeystore);

        assertEquals(3, task.configuration.keyStores.size(),
                    "Should support multiple variant keystores");
    }

    @Test
    public void testKeystore_androidGooglePlay() throws Exception {
        // Google Play signing keystore
        File playKeystore = temporaryFolder.resolve("google-play.keystore").toFile();
        playKeystore.createNewFile();

        task.android();
        task.keystore(playKeystore);
        task.keystorepassword("play_password");
        task.keyalias("upload_key");

        assertTrue(task.configuration.keyStores.contains(playKeystore),
                  "Should support Google Play signing keystore");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testKeystore_beforeAndroid() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);
        task.android();

        assertTrue(task.configuration.keyStores.contains(keystoreFile), "keystore should be set");
        assertTrue(task.configuration.android, "android should be set");
    }

    @Test
    public void testKeystore_afterAndroid() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.android();
        task.keystore(keystoreFile);

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.keyStores.contains(keystoreFile), "keystore should be set");
    }

    @Test
    public void testKeystore_betweenOtherCalls() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.verbose();
        task.keystore(keystoreFile);
        task.android();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keyStores.contains(keystoreFile), "keystore should be set");
        assertTrue(task.configuration.android, "android should be set");
    }

    @Test
    public void testKeystore_withComplexConfiguration() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.injars("input.jar");
        task.outjars("output.jar");
        task.android();
        task.keystore(keystoreFile);
        task.keystorepassword("password");
        task.keyalias("key");
        task.keypassword("keypass");
        task.verbose();

        assertTrue(task.configuration.keyStores.contains(keystoreFile), "keystore should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testKeystore_configurationStatePreserved() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.verbose();
        task.dontoptimize();
        task.android();

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.keystore(keystoreFile);

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.keyStores.contains(keystoreFile), "keystore should be set");
    }

    @Test
    public void testKeystore_doesNotClearOtherBooleanFlags() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.allowaccessmodification();
        task.forceprocessing();
        task.android();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.android, "android should remain set");
        assertTrue(task.configuration.keyStores.contains(keystoreFile), "keystore should be set");
    }

    @Test
    public void testKeystore_doesNotClearKeepRules() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.keystore(keystoreFile);

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.keyStores.contains(keystoreFile), "keystore should be set");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testKeystore_withCustomKeystoreLocation() throws Exception {
        // Custom keystore location
        File customDir = temporaryFolder.resolve("custom").toFile();
        customDir.mkdir();
        File keystoreFile = new File(customDir, "custom.keystore");
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                  "Should support custom keystore location");
    }

    @Test
    public void testKeystore_withEnvironmentSpecificKeystore() throws Exception {
        // Environment-specific keystores (dev, staging, prod)
        File devKeystore = temporaryFolder.resolve("dev.keystore").toFile();
        File stagingKeystore = temporaryFolder.resolve("staging.keystore").toFile();
        File prodKeystore = temporaryFolder.resolve("prod.keystore").toFile();
        devKeystore.createNewFile();
        stagingKeystore.createNewFile();
        prodKeystore.createNewFile();

        task.keystore(devKeystore);
        task.keystore(stagingKeystore);
        task.keystore(prodKeystore);

        assertEquals(3, task.configuration.keyStores.size(),
                    "Should support environment-specific keystores");
    }

    @Test
    public void testKeystore_forCodeSigning() throws Exception {
        // Code signing keystore
        File signingKeystore = temporaryFolder.resolve("signing.keystore").toFile();
        signingKeystore.createNewFile();

        task.keystore(signingKeystore);
        task.keystorepassword("signing_password");
        task.keyalias("code_signing_key");

        assertTrue(task.configuration.keyStores.contains(signingKeystore),
                  "Should support code signing keystore");
    }

    @Test
    public void testKeystore_withUserHomeKeystore() throws Exception {
        // Keystore in user home directory (common pattern)
        File userHomeKeystore = temporaryFolder.resolve(".android/release.keystore").toFile();
        userHomeKeystore.getParentFile().mkdirs();
        userHomeKeystore.createNewFile();

        task.keystore(userHomeKeystore);

        assertTrue(task.configuration.keyStores.contains(userHomeKeystore),
                  "Should support keystore in user home directory");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testKeystore_configurationNotNull() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testKeystore_taskStateValid() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testKeystore_listIsNotNull() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertNotNull(task.configuration.keyStores,
                     "keyStores should never be null after adding keystore");
    }

    @Test
    public void testKeystore_semanticMeaning() throws Exception {
        // Verify the semantic meaning: adds keystore for signing
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        task.keystore(keystoreFile);

        assertTrue(task.configuration.keyStores.contains(keystoreFile),
                   "keystore should add keystore file for APK signing");
    }

    @Test
    public void testKeystore_changesConfiguration() throws Exception {
        File keystoreFile = temporaryFolder.resolve("test.keystore").toFile();
        keystoreFile.createNewFile();

        int sizeBefore = task.configuration.keyStores == null ? 0 : task.configuration.keyStores.size();

        task.keystore(keystoreFile);
        int sizeAfter = task.configuration.keyStores.size();

        assertEquals(sizeBefore + 1, sizeAfter, "Configuration should have changed");
    }
}
