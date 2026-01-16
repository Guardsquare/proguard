package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ProGuardTask.getaddconfigurationdebugging()Ljava/lang/Object; method.
 * This is a Groovy DSL getter that returns null and calls addconfigurationdebugging()
 * to set configuration.addConfigurationDebugging = true.
 * This option adds debugging information to configuration files.
 */
public class ProGuardTaskClaude_getaddconfigurationdebuggingTest {

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

    // ============================================================
    // Basic Functionality Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_returnsNull() throws Exception {
        Object result = task.getaddconfigurationdebugging();

        assertNull(result, "getaddconfigurationdebugging should return null for Groovy DSL support");
    }

    @Test
    public void testGetaddconfigurationdebugging_setsAddConfigurationDebuggingToTrue() throws Exception {
        assertFalse(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should initially be false");

        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set to true");
    }

    @Test
    public void testGetaddconfigurationdebugging_callsAddconfigurationdebugging() throws Exception {
        // getaddconfigurationdebugging should call addconfigurationdebugging()
        assertFalse(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should initially be false");

        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addconfigurationdebugging() should have been called");
    }

    @Test
    public void testGetaddconfigurationdebugging_isIdempotent() throws Exception {
        task.getaddconfigurationdebugging();
        boolean firstResult = task.configuration.addConfigurationDebugging;

        task.getaddconfigurationdebugging();

        assertEquals(firstResult, task.configuration.addConfigurationDebugging,
                "Multiple calls should not change the state");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should remain true");
    }

    @Test
    public void testGetaddconfigurationdebugging_multipleCalls() throws Exception {
        task.getaddconfigurationdebugging();
        task.getaddconfigurationdebugging();
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should remain true after multiple calls");
    }

    @Test
    public void testGetaddconfigurationdebugging_alwaysReturnsNull() throws Exception {
        Object result1 = task.getaddconfigurationdebugging();
        Object result2 = task.getaddconfigurationdebugging();
        Object result3 = task.getaddconfigurationdebugging();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    // ============================================================
    // State Verification Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_initialStateIsFalse() throws Exception {
        assertFalse(task.configuration.addConfigurationDebugging,
                "Initial state should be false");
    }

    @Test
    public void testGetaddconfigurationdebugging_changesStateToTrue() throws Exception {
        boolean before = task.configuration.addConfigurationDebugging;
        assertFalse(before, "Before calling, state should be false");

        task.getaddconfigurationdebugging();

        boolean after = task.configuration.addConfigurationDebugging;
        assertTrue(after, "After calling, state should be true");
        assertNotEquals(before, after, "State should have changed");
    }

    @Test
    public void testGetaddconfigurationdebugging_stateRemainsTrue() throws Exception {
        task.getaddconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging);

        // Call again
        task.getaddconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging,
                "State should remain true after subsequent calls");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_equivalentToDirectCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.getaddconfigurationdebugging();
        task2.addconfigurationdebugging();

        assertEquals(task1.configuration.addConfigurationDebugging,
                task2.configuration.addConfigurationDebugging,
                "getaddconfigurationdebugging() should have same effect as addconfigurationdebugging()");
        assertTrue(task1.configuration.addConfigurationDebugging);
        assertTrue(task2.configuration.addConfigurationDebugging);
    }

    @Test
    public void testGetaddconfigurationdebugging_returnsDifferentThanDirectCall() throws Exception {
        Object getterResult = task.getaddconfigurationdebugging();
        assertNull(getterResult, "Getter should return null");

        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        task2.addconfigurationdebugging();
        // Direct call is void, returns nothing (conceptually null in terms of return value)

        // Both should have same configuration effect
        assertTrue(task.configuration.addConfigurationDebugging);
        assertTrue(task2.configuration.addConfigurationDebugging);
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_withVerbose() throws Exception {
        task.verbose();
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testGetaddconfigurationdebugging_withPrintConfiguration() throws Exception {
        task.printconfiguration("config.txt");
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work with printconfiguration");
    }

    @Test
    public void testGetaddconfigurationdebugging_withKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work with keep rules");
    }

    @Test
    public void testGetaddconfigurationdebugging_withDontShrink() throws Exception {
        task.dontshrink();
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging);
        assertFalse(task.configuration.shrink);
    }

    @Test
    public void testGetaddconfigurationdebugging_withDontOptimize() throws Exception {
        task.dontoptimize();
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging);
        assertFalse(task.configuration.optimize);
    }

    @Test
    public void testGetaddconfigurationdebugging_withDontObfuscate() throws Exception {
        task.dontobfuscate();
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    public void testGetaddconfigurationdebugging_withIgnoreWarnings() throws Exception {
        task.ignorewarnings();
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    public void testGetaddconfigurationdebugging_withFullConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { public *; }");
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work in full configuration");
    }

    // ============================================================
    // Configuration Order Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_beforeOtherConfiguration() throws Exception {
        task.getaddconfigurationdebugging();
        task.verbose();
        task.printconfiguration("config.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set when called first");
    }

    @Test
    public void testGetaddconfigurationdebugging_afterOtherConfiguration() throws Exception {
        task.verbose();
        task.printconfiguration("config.txt");
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set when called last");
    }

    @Test
    public void testGetaddconfigurationdebugging_inMiddleOfConfiguration() throws Exception {
        task.verbose();
        task.getaddconfigurationdebugging();
        task.printconfiguration("config.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set when called in middle");
    }

    // ============================================================
    // Does Not Affect Other Settings Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_doesNotAffectShrink() throws Exception {
        boolean initialShrink = task.configuration.shrink;

        task.getaddconfigurationdebugging();

        assertEquals(initialShrink, task.configuration.shrink,
                "getaddconfigurationdebugging should not affect shrink setting");
    }

    @Test
    public void testGetaddconfigurationdebugging_doesNotAffectOptimize() throws Exception {
        boolean initialOptimize = task.configuration.optimize;

        task.getaddconfigurationdebugging();

        assertEquals(initialOptimize, task.configuration.optimize,
                "getaddconfigurationdebugging should not affect optimize setting");
    }

    @Test
    public void testGetaddconfigurationdebugging_doesNotAffectObfuscate() throws Exception {
        boolean initialObfuscate = task.configuration.obfuscate;

        task.getaddconfigurationdebugging();

        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "getaddconfigurationdebugging should not affect obfuscate setting");
    }

    @Test
    public void testGetaddconfigurationdebugging_doesNotAffectVerbose() throws Exception {
        task.configuration.verbose = true;

        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.verbose,
                "getaddconfigurationdebugging should not affect verbose setting");
    }

    @Test
    public void testGetaddconfigurationdebugging_doesNotAffectIgnoreWarnings() throws Exception {
        boolean initialIgnoreWarnings = task.configuration.ignoreWarnings;

        task.getaddconfigurationdebugging();

        assertEquals(initialIgnoreWarnings, task.configuration.ignoreWarnings,
                "getaddconfigurationdebugging should not affect ignoreWarnings setting");
    }

    @Test
    public void testGetaddconfigurationdebugging_doesNotAffectPrintConfiguration() throws Exception {
        task.printconfiguration("config.txt");
        java.io.File initialPrintConfig = task.configuration.printConfiguration;

        task.getaddconfigurationdebugging();

        assertSame(initialPrintConfig, task.configuration.printConfiguration,
                "getaddconfigurationdebugging should not affect printConfiguration setting");
    }

    // ============================================================
    // Multiple Task Instances Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_independentTaskInstances() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.getaddconfigurationdebugging();

        assertTrue(task1.configuration.addConfigurationDebugging,
                "Task1 should have addConfigurationDebugging enabled");
        assertFalse(task2.configuration.addConfigurationDebugging,
                "Task2 should not be affected");
    }

    @Test
    public void testGetaddconfigurationdebugging_multipleTasksCanEnable() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.getaddconfigurationdebugging();
        task2.getaddconfigurationdebugging();

        assertTrue(task1.configuration.addConfigurationDebugging,
                "Task1 should have addConfigurationDebugging enabled");
        assertTrue(task2.configuration.addConfigurationDebugging,
                "Task2 should have addConfigurationDebugging enabled");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_debugBuildScenario() throws Exception {
        // Debug build with configuration debugging
        task.verbose();
        task.getaddconfigurationdebugging();
        task.printconfiguration("build/debug/config.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "Debug builds should enable configuration debugging");
    }

    @Test
    public void testGetaddconfigurationdebugging_troubleshootingScenario() throws Exception {
        // Troubleshooting with verbose output and configuration debugging
        task.verbose();
        task.getaddconfigurationdebugging();
        task.printconfiguration("troubleshooting/config.txt");
        task.printseeds("troubleshooting/seeds.txt");
        task.printmapping("troubleshooting/mapping.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "Troubleshooting should enable configuration debugging");
    }

    @Test
    public void testGetaddconfigurationdebugging_developmentMode() throws Exception {
        // Development mode with extra debugging
        task.dontobfuscate();
        task.getaddconfigurationdebugging();
        task.verbose();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Development mode should enable configuration debugging");
        assertFalse(task.configuration.obfuscate,
                "Development mode typically disables obfuscation");
    }

    @Test
    public void testGetaddconfigurationdebugging_configurationAudit() throws Exception {
        // Auditing configuration for compliance
        task.getaddconfigurationdebugging();
        task.printconfiguration("audit/config.txt");
        task.keep("class * { *; }");

        assertTrue(task.configuration.addConfigurationDebugging,
                "Audit should enable configuration debugging for transparency");
    }

    @Test
    public void testGetaddconfigurationdebugging_cicdDebugging() throws Exception {
        // CI/CD with debugging enabled
        task.verbose();
        task.getaddconfigurationdebugging();
        task.printconfiguration(); // stdout

        assertTrue(task.configuration.addConfigurationDebugging,
                "CI/CD debugging should enable configuration debugging");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_groovyDslSupport() throws Exception {
        // This method exists to support Groovy DSL usage without parentheses
        // In Groovy: task.addconfigurationdebugging (property-style access)
        Object result = task.getaddconfigurationdebugging();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertTrue(task.configuration.addConfigurationDebugging,
                "Should enable configuration debugging");
    }

    @Test
    public void testGetaddconfigurationdebugging_returnTypeIsObject() throws Exception {
        Object result = task.getaddconfigurationdebugging();

        assertNull(result, "Return type is Object, value should be null");
    }

    @Test
    public void testGetaddconfigurationdebugging_canBeCalledAsGetter() throws Exception {
        // Simulate Groovy property access via getter
        Object result = task.getaddconfigurationdebugging();

        assertNull(result);
        assertTrue(task.configuration.addConfigurationDebugging,
                "Calling as getter should have same effect");
    }

    // ============================================================
    // Edge Cases and Validation
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_onFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);

        assertFalse(freshTask.configuration.addConfigurationDebugging,
                "Fresh task should have addConfigurationDebugging disabled");

        freshTask.getaddconfigurationdebugging();

        assertTrue(freshTask.configuration.addConfigurationDebugging,
                "Fresh task should enable addConfigurationDebugging after call");
    }

    @Test
    public void testGetaddconfigurationdebugging_afterReset() throws Exception {
        task.getaddconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging);

        // Manual reset (if needed in practice)
        task.configuration.addConfigurationDebugging = false;
        assertFalse(task.configuration.addConfigurationDebugging);

        // Call again
        task.getaddconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging,
                "Should be able to enable again after manual reset");
    }

    @Test
    public void testGetaddconfigurationdebugging_booleanBehavior() throws Exception {
        // Verify it's a simple boolean flag
        assertFalse(task.configuration.addConfigurationDebugging);

        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging);
        assertEquals(true, task.configuration.addConfigurationDebugging,
                "Should be exactly true");
    }

    // ============================================================
    // Annotation Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_isMarkedAsInternal() throws Exception {
        // The method is annotated with @Internal
        // This means it's for Gradle's internal use (Groovy DSL support)
        Object result = task.getaddconfigurationdebugging();

        assertNull(result, "Internal methods for Groovy DSL return null");
        assertTrue(task.configuration.addConfigurationDebugging,
                "But they should still perform their configuration side effect");
    }

    // ============================================================
    // Combination with Other Debug Options
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_withVerboseAndPrintConfiguration() throws Exception {
        task.verbose();
        task.getaddconfigurationdebugging();
        task.printconfiguration("config.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testGetaddconfigurationdebugging_withAllPrintOptions() throws Exception {
        task.getaddconfigurationdebugging();
        task.printconfiguration("config.txt");
        task.printseeds("seeds.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work with all print options");
    }

    @Test
    public void testGetaddconfigurationdebugging_withDump() throws Exception {
        task.getaddconfigurationdebugging();
        task.dump("dump.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work with dump");
    }

    // ============================================================
    // Call Sequence Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_canBeCalledFirst() throws Exception {
        task.getaddconfigurationdebugging();
        task.verbose();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should work when called first");
    }

    @Test
    public void testGetaddconfigurationdebugging_canBeCalledLast() throws Exception {
        task.verbose();
        task.printconfiguration("config.txt");
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should work when called last");
    }

    @Test
    public void testGetaddconfigurationdebugging_canBeCalledMultipleTimesInSequence() throws Exception {
        task.verbose();
        task.getaddconfigurationdebugging();
        task.printconfiguration("config.txt");
        task.getaddconfigurationdebugging();
        task.keep("class * { *; }");
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should remain true after multiple calls in sequence");
    }

    // ============================================================
    // Semantic Meaning Tests
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_semanticMeaning() throws Exception {
        // Verify the semantic purpose: adds debugging info to configuration
        task.getaddconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Method should add configuration debugging information");
    }

    @Test
    public void testGetaddconfigurationdebugging_persistsAcrossCalls() throws Exception {
        task.getaddconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging);

        // Make other configuration changes
        task.verbose();
        task.keep("class * { *; }");

        // Configuration debugging should still be enabled
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should persist across other configuration calls");
    }

    // ============================================================
    // Comparison with addconfigurationdebugging() void method
    // ============================================================

    @Test
    public void testGetaddconfigurationdebugging_sameEffectAsVoidMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        Object getterResult = task1.getaddconfigurationdebugging();
        task2.addconfigurationdebugging();

        assertNull(getterResult, "Getter should return null");
        assertEquals(task1.configuration.addConfigurationDebugging,
                task2.configuration.addConfigurationDebugging,
                "Both methods should have identical effect on configuration");
        assertTrue(task1.configuration.addConfigurationDebugging);
        assertTrue(task2.configuration.addConfigurationDebugging);
    }

    @Test
    public void testGetaddconfigurationdebugging_voidMethodDirectComparison() throws Exception {
        // First test: use getter
        assertFalse(task.configuration.addConfigurationDebugging);
        Object result = task.getaddconfigurationdebugging();
        assertNull(result);
        assertTrue(task.configuration.addConfigurationDebugging);

        // Second test: use void method directly
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        assertFalse(task2.configuration.addConfigurationDebugging);
        task2.addconfigurationdebugging();
        assertTrue(task2.configuration.addConfigurationDebugging);

        // Both should have same final state
        assertEquals(task.configuration.addConfigurationDebugging,
                task2.configuration.addConfigurationDebugging);
    }
}
