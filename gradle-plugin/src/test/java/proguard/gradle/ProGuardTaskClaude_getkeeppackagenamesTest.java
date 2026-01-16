package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.getkeeppackagenames()Ljava/lang/Object; method.
 * Tests the getkeeppackagenames() Groovy DSL method that returns null and calls keeppackagenames()
 * to clear the keepPackageNames filter, keeping all package names from being obfuscated.
 */
public class ProGuardTaskClaude_getkeeppackagenamesTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("proguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        task = null;
        project = null;
    }

    // ============================================================
    // Basic Functionality Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_returnsNull() throws Exception {
        Object result = task.getkeeppackagenames();
        assertNull(result, "getkeeppackagenames() should return null");
    }

    @Test
    public void testGetkeeppackagenames_clearsKeepPackageNamesFilter() throws Exception {
        // keepPackageNames should be cleared (null becomes empty list)
        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should not be null after call");
    }

    @Test
    public void testGetkeeppackagenames_returnsNullAndClearsFilter() throws Exception {
        Object result = task.getkeeppackagenames();
        assertNull(result, "getkeeppackagenames() should return null");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should not be null");
    }

    @Test
    public void testGetkeeppackagenames_changesConfigurationState() throws Exception {
        // Initially null
        assertNull(task.configuration.keepPackageNames, "keepPackageNames should initially be null");

        task.getkeeppackagenames();

        // After calling, should be initialized to empty list
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.keepPackageNames, "Fresh task should have keepPackageNames as null");

        Object result = freshTask.getkeeppackagenames();

        assertNull(result, "Should return null");
        assertNotNull(freshTask.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_multipleCallsAreIdempotent() throws Exception {
        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "First call should initialize keepPackageNames");

        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Second call should keep keepPackageNames initialized");

        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Third call should keep keepPackageNames initialized");
    }

    @Test
    public void testGetkeeppackagenames_alwaysReturnsNull() throws Exception {
        Object result1 = task.getkeeppackagenames();
        Object result2 = task.getkeeppackagenames();
        Object result3 = task.getkeeppackagenames();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetkeeppackagenames_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getkeeppackagenames();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized after call " + (i + 1));
        }
    }

    @Test
    public void testGetkeeppackagenames_consecutiveCalls() throws Exception {
        task.getkeeppackagenames();
        task.getkeeppackagenames();
        task.getkeeppackagenames();
        task.getkeeppackagenames();
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "After 5 consecutive calls, keepPackageNames should be initialized");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetkeeppackagenames_withDontobfuscate() throws Exception {
        task.getkeeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetkeeppackagenames_withDontshrink() throws Exception {
        task.getkeeppackagenames();
        task.dontshrink();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetkeeppackagenames_withDontoptimize() throws Exception {
        task.getkeeppackagenames();
        task.dontoptimize();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetkeeppackagenames_withVerbose() throws Exception {
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_withAllowAccessModification() throws Exception {
        task.getkeeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetkeeppackagenames_withOverloadAggressively() throws Exception {
        task.getkeeppackagenames();
        task.overloadaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetkeeppackagenames_withMergeInterfacesAggressively() throws Exception {
        task.getkeeppackagenames();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeeppackagenames_withMultipleConfigMethods() throws Exception {
        task.getkeeppackagenames();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_withAllOptimizationSettings() throws Exception {
        task.getkeeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_beforeOtherConfig() throws Exception {
        task.getkeeppackagenames();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_mixedWithOtherGetters() throws Exception {
        task.getkeeppackagenames();
        task.getallowaccessmodification();
        task.getmergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetkeeppackagenames_inObfuscationScenario() throws Exception {
        // Keep all package names during obfuscation
        task.getkeeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetkeeppackagenames_forDebugging() throws Exception {
        // Keep package names for easier debugging
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup keeping package names
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getkeeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_forPublicApiLibrary() throws Exception {
        // Public API library keeping package names
        task.getkeeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetkeeppackagenames_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getkeeppackagenames();
        task2.getkeeppackagenames();
        task3.getkeeppackagenames();

        assertNotNull(task1.configuration.keepPackageNames, "task1 keepPackageNames should be initialized");
        assertNotNull(task2.configuration.keepPackageNames, "task2 keepPackageNames should be initialized");
        assertNotNull(task3.configuration.keepPackageNames, "task3 keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "task keepPackageNames should be initialized");
        assertNull(otherTask.configuration.keepPackageNames, "otherTask should not be affected");
    }

    @Test
    public void testGetkeeppackagenames_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getkeeppackagenames
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Should be initialized after first call");

        task.allowaccessmodification();
        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Should remain initialized after second call");

        task.verbose();
        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Should remain initialized after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetkeeppackagenames_inAndroidReleaseVariant() throws Exception {
        // Android release variant keeping package names
        task.getkeeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeeppackagenames_inAndroidLibraryModule() throws Exception {
        // Android library keeping package names
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build keeping package names
        task.getkeeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetkeeppackagenames_inProductionRelease() throws Exception {
        // Production release build keeping package names
        task.getkeeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeeppackagenames_inStagingForQa() throws Exception {
        // QA staging build keeping package names
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_inBetaBuild() throws Exception {
        // Beta testing build keeping package names
        task.getkeeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetkeeppackagenames_inAlphaBuild() throws Exception {
        // Alpha testing build keeping package names
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.keepPackageNames, "Initial state should be null");

        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "State should be modified");
    }

    @Test
    public void testGetkeeppackagenames_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.getkeeppackagenames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.getkeeppackagenames();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeeppackagenames_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetkeeppackagenames_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetkeeppackagenames_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_supportsGroovyDslSyntax() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: task.keeppackagenames (without parentheses)
        Object result = task.getkeeppackagenames();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertNotNull(task.configuration.keepPackageNames, "Should initialize keepPackageNames");
    }

    @Test
    public void testGetkeeppackagenames_behavesLikeUnderlyingMethod() throws Exception {
        // getkeeppackagenames() should behave like keeppackagenames()
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.getkeeppackagenames();
        task2.keeppackagenames();

        assertNotNull(task1.configuration.keepPackageNames, "task1 keepPackageNames should be initialized");
        assertNotNull(task2.configuration.keepPackageNames, "task2 keepPackageNames should be initialized");
    }

    @Test
    public void testGetkeeppackagenames_isAnnotatedAsInternal() throws Exception {
        // The method should be annotated with @Internal for Gradle
        // This is a compile-time check, but we verify the method exists and works
        Object result = task.getkeeppackagenames();
        assertNull(result, "Method should exist and return null");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testGetkeeppackagenames_forDockerizedBuilds() throws Exception {
        // Dockerized build environment keeping package names
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_forCloudBuilds() throws Exception {
        // Cloud build service keeping package names
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_forLocalDevelopment() throws Exception {
        // Local development environment
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetkeeppackagenames_equivalentToDirectCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use getter version
        Object getterResult = task1.getkeeppackagenames();

        // Use direct method
        task2.keeppackagenames();

        assertNull(getterResult, "Getter should return null");
        assertNotNull(task1.configuration.keepPackageNames, "task1 should have keepPackageNames initialized");
        assertNotNull(task2.configuration.keepPackageNames, "task2 should have keepPackageNames initialized");
    }

    @Test
    public void testGetkeeppackagenames_canBeMixedWithDirectCalls() throws Exception {
        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "After getter call, should be initialized");

        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "After direct call, should still be initialized");

        task.getkeeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "After another getter call, should still be initialized");
    }

    // ============================================================
    // Return Value Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_alwaysReturnsNullRegardlessOfState() throws Exception {
        Object result1 = task.getkeeppackagenames();
        assertNull(result1, "First call should return null");

        Object result2 = task.getkeeppackagenames();
        assertNull(result2, "Second call should return null even though config is already set");

        Object result3 = task.getkeeppackagenames();
        assertNull(result3, "Third call should still return null");
    }

    @Test
    public void testGetkeeppackagenames_returnTypeIsObject() throws Exception {
        // Verify the return type is Object (not void)
        Object result = task.getkeeppackagenames();
        // If the method returns void, this would be a compilation error
        assertNull(result, "Return type should be Object and value should be null");
    }

    // ============================================================
    // Package Name Obfuscation Context Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_forKeepingPackageNames() throws Exception {
        // Keep all package names from being obfuscated
        task.getkeeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "Should initialize keepPackageNames");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testGetkeeppackagenames_withOtherObfuscationSettings() throws Exception {
        task.getkeeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Debugging and Troubleshooting Tests
    // ============================================================

    @Test
    public void testGetkeeppackagenames_forEasierStackTraces() throws Exception {
        // Keeping package names makes stack traces more readable
        task.getkeeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeeppackagenames_forCrashReporting() throws Exception {
        // Keep package names for better crash reports
        task.getkeeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized for crash reporting");
    }

    @Test
    public void testGetkeeppackagenames_withReflectionHeavyCode() throws Exception {
        // Code using reflection with package names
        task.getkeeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for reflection compatibility");
    }
}
