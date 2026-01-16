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
 * Comprehensive tests for ProGuardTask.addconfigurationdebugging()V method.
 * This is a void method that sets configuration.addConfigurationDebugging = true.
 * This option adds debugging information to configuration files, useful for troubleshooting
 * ProGuard configuration issues.
 */
public class ProGuardTaskClaude_addconfigurationdebuggingTest {

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
    // Tests for addconfigurationdebugging() Method
    // ========================================

    @Test
    public void testAddconfigurationdebugging_setsAddConfigurationDebuggingToTrue() throws Exception {
        assertFalse(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should initially be false");

        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set to true");
    }

    @Test
    public void testAddconfigurationdebugging_isIdempotent() throws Exception {
        task.addconfigurationdebugging();
        boolean firstResult = task.configuration.addConfigurationDebugging;

        task.addconfigurationdebugging();

        assertEquals(firstResult, task.configuration.addConfigurationDebugging,
                "Multiple calls should not change the state");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should remain true");
    }

    @Test
    public void testAddconfigurationdebugging_multipleCalls() throws Exception {
        task.addconfigurationdebugging();
        task.addconfigurationdebugging();
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should remain true after multiple calls");
    }

    @Test
    public void testAddconfigurationdebugging_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.addconfigurationdebugging();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testAddconfigurationdebugging_afterManuallySetToFalse() throws Exception {
        task.configuration.addConfigurationDebugging = false;

        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set to true");
    }

    @Test
    public void testAddconfigurationdebugging_whenAlreadyTrue() throws Exception {
        task.configuration.addConfigurationDebugging = true;

        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should remain true");
    }

    @Test
    public void testAddconfigurationdebugging_initialStateIsFalse() throws Exception {
        assertFalse(task.configuration.addConfigurationDebugging,
                "Default state should be false");
    }

    @Test
    public void testAddconfigurationdebugging_changesStateFromFalseToTrue() throws Exception {
        boolean before = task.configuration.addConfigurationDebugging;
        assertFalse(before, "State should be false before calling");

        task.addconfigurationdebugging();

        boolean after = task.configuration.addConfigurationDebugging;
        assertTrue(after, "State should be true after calling");
        assertNotEquals(before, after, "State should have changed");
    }

    // ========================================
    // Integration with Other Configuration Methods
    // ========================================

    @Test
    public void testAddconfigurationdebugging_withVerbose() throws Exception {
        task.verbose();
        task.addconfigurationdebugging();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testAddconfigurationdebugging_withPrintConfiguration() throws Exception {
        task.printconfiguration("config.txt");
        task.addconfigurationdebugging();

        assertNotNull(task.configuration.printConfiguration,
                "printConfiguration should be set");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testAddconfigurationdebugging_withDontShrink() throws Exception {
        task.dontshrink();
        task.addconfigurationdebugging();

        assertFalse(task.configuration.shrink, "shrink should be false");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testAddconfigurationdebugging_withDontOptimize() throws Exception {
        task.dontoptimize();
        task.addconfigurationdebugging();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testAddconfigurationdebugging_withDontObfuscate() throws Exception {
        task.dontobfuscate();
        task.addconfigurationdebugging();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testAddconfigurationdebugging_withIgnoreWarnings() throws Exception {
        task.ignorewarnings();
        task.addconfigurationdebugging();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be true");
    }

    @Test
    public void testAddconfigurationdebugging_withKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work with keep rules");
    }

    @Test
    public void testAddconfigurationdebugging_withFullConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { public *; }");
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work in full configuration");
    }

    @Test
    public void testAddconfigurationdebugging_withAllPrintOptions() throws Exception {
        task.printconfiguration("config.txt");
        task.printseeds("seeds.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work with all print options");
    }

    @Test
    public void testAddconfigurationdebugging_withDump() throws Exception {
        task.dump("dump.txt");
        task.addconfigurationdebugging();

        assertNotNull(task.configuration.dump, "dump should be set");
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should work with dump");
    }

    // ========================================
    // Configuration Order Tests
    // ========================================

    @Test
    public void testAddconfigurationdebugging_beforeOtherConfiguration() throws Exception {
        task.addconfigurationdebugging();
        task.verbose();
        task.printconfiguration("config.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set when called first");
    }

    @Test
    public void testAddconfigurationdebugging_afterOtherConfiguration() throws Exception {
        task.verbose();
        task.printconfiguration("config.txt");
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set when called last");
    }

    @Test
    public void testAddconfigurationdebugging_inMiddleOfConfiguration() throws Exception {
        task.verbose();
        task.addconfigurationdebugging();
        task.printconfiguration("config.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should be set when called in middle");
    }

    @Test
    public void testAddconfigurationdebugging_multipleCallsInterspersed() throws Exception {
        task.verbose();
        task.addconfigurationdebugging();
        task.printconfiguration("config.txt");
        task.addconfigurationdebugging();
        task.keep("class * { *; }");
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should remain true with interspersed calls");
    }

    // ========================================
    // Does Not Affect Other Settings Tests
    // ========================================

    @Test
    public void testAddconfigurationdebugging_doesNotAffectShrink() throws Exception {
        boolean initialShrink = task.configuration.shrink;

        task.addconfigurationdebugging();

        assertEquals(initialShrink, task.configuration.shrink,
                "addconfigurationdebugging should not affect shrink setting");
    }

    @Test
    public void testAddconfigurationdebugging_doesNotAffectOptimize() throws Exception {
        boolean initialOptimize = task.configuration.optimize;

        task.addconfigurationdebugging();

        assertEquals(initialOptimize, task.configuration.optimize,
                "addconfigurationdebugging should not affect optimize setting");
    }

    @Test
    public void testAddconfigurationdebugging_doesNotAffectObfuscate() throws Exception {
        boolean initialObfuscate = task.configuration.obfuscate;

        task.addconfigurationdebugging();

        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "addconfigurationdebugging should not affect obfuscate setting");
    }

    @Test
    public void testAddconfigurationdebugging_doesNotAffectVerbose() throws Exception {
        boolean initialVerbose = task.configuration.verbose;

        task.addconfigurationdebugging();

        assertEquals(initialVerbose, task.configuration.verbose,
                "addconfigurationdebugging should not affect verbose setting");
    }

    @Test
    public void testAddconfigurationdebugging_doesNotAffectIgnoreWarnings() throws Exception {
        boolean initialIgnoreWarnings = task.configuration.ignoreWarnings;

        task.addconfigurationdebugging();

        assertEquals(initialIgnoreWarnings, task.configuration.ignoreWarnings,
                "addconfigurationdebugging should not affect ignoreWarnings setting");
    }

    @Test
    public void testAddconfigurationdebugging_doesNotAffectPrintConfiguration() throws Exception {
        task.printconfiguration("config.txt");
        java.io.File initialPrintConfig = task.configuration.printConfiguration;

        task.addconfigurationdebugging();

        assertSame(initialPrintConfig, task.configuration.printConfiguration,
                "addconfigurationdebugging should not affect printConfiguration setting");
    }

    @Test
    public void testAddconfigurationdebugging_doesNotAffectDump() throws Exception {
        task.dump("dump.txt");
        java.io.File initialDump = task.configuration.dump;

        task.addconfigurationdebugging();

        assertSame(initialDump, task.configuration.dump,
                "addconfigurationdebugging should not affect dump setting");
    }

    // ========================================
    // Multiple Task Instances Tests
    // ========================================

    @Test
    public void testAddconfigurationdebugging_independentTaskInstances() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.addconfigurationdebugging();

        assertTrue(task1.configuration.addConfigurationDebugging,
                "Task1 should have addConfigurationDebugging enabled");
        assertFalse(task2.configuration.addConfigurationDebugging,
                "Task2 should not be affected");
    }

    @Test
    public void testAddconfigurationdebugging_multipleTasksCanEnable() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.addconfigurationdebugging();
        task2.addconfigurationdebugging();

        assertTrue(task1.configuration.addConfigurationDebugging,
                "Task1 should have addConfigurationDebugging enabled");
        assertTrue(task2.configuration.addConfigurationDebugging,
                "Task2 should have addConfigurationDebugging enabled");
    }

    @Test
    public void testAddconfigurationdebugging_differentTasksDifferentStates() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.addconfigurationdebugging();
        // Don't call on task2

        assertTrue(task1.configuration.addConfigurationDebugging);
        assertFalse(task2.configuration.addConfigurationDebugging,
                "Each task maintains its own state");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testAddconfigurationdebugging_debugBuildScenario() throws Exception {
        // Debug build with configuration debugging
        task.verbose();
        task.addconfigurationdebugging();
        task.printconfiguration("build/debug/config.txt");
        task.dontobfuscate();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Debug builds should enable configuration debugging");
        assertTrue(task.configuration.verbose);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    public void testAddconfigurationdebugging_troubleshootingScenario() throws Exception {
        // Troubleshooting with verbose output and configuration debugging
        task.verbose();
        task.addconfigurationdebugging();
        task.printconfiguration("troubleshooting/config.txt");
        task.printseeds("troubleshooting/seeds.txt");
        task.printmapping("troubleshooting/mapping.txt");
        task.dump("troubleshooting/dump.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "Troubleshooting should enable configuration debugging");
        assertTrue(task.configuration.verbose);
    }

    @Test
    public void testAddconfigurationdebugging_developmentMode() throws Exception {
        // Development mode with extra debugging
        task.dontobfuscate();
        task.addconfigurationdebugging();
        task.verbose();
        task.dontshrink();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Development mode should enable configuration debugging");
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
    }

    @Test
    public void testAddconfigurationdebugging_configurationAudit() throws Exception {
        // Auditing configuration for compliance
        task.addconfigurationdebugging();
        task.printconfiguration("audit/config.txt");
        task.keep("class * { *; }");
        task.verbose();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Audit should enable configuration debugging for transparency");
    }

    @Test
    public void testAddconfigurationdebugging_cicdDebugging() throws Exception {
        // CI/CD with debugging enabled
        task.verbose();
        task.addconfigurationdebugging();
        task.printconfiguration(); // stdout

        assertTrue(task.configuration.addConfigurationDebugging,
                "CI/CD debugging should enable configuration debugging");
    }

    @Test
    public void testAddconfigurationdebugging_productionBuildWithoutDebugging() throws Exception {
        // Production build typically doesn't enable configuration debugging
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { public *; }");
        // Don't call addconfigurationdebugging()

        assertFalse(task.configuration.addConfigurationDebugging,
                "Production builds typically don't enable configuration debugging");
    }

    @Test
    public void testAddconfigurationdebugging_comparisonDebugVsRelease() throws Exception {
        // Debug variant
        ProGuardTask debugTask = project.getTasks().create("proguardDebug", ProGuardTask.class);
        debugTask.addconfigurationdebugging();
        debugTask.verbose();
        debugTask.dontobfuscate();

        // Release variant
        ProGuardTask releaseTask = project.getTasks().create("proguardRelease", ProGuardTask.class);
        // No configuration debugging

        assertTrue(debugTask.configuration.addConfigurationDebugging,
                "Debug variant should have debugging");
        assertFalse(releaseTask.configuration.addConfigurationDebugging,
                "Release variant should not have debugging");
    }

    // ========================================
    // Edge Cases and Validation
    // ========================================

    @Test
    public void testAddconfigurationdebugging_onFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);

        assertFalse(freshTask.configuration.addConfigurationDebugging,
                "Fresh task should have addConfigurationDebugging disabled");

        freshTask.addconfigurationdebugging();

        assertTrue(freshTask.configuration.addConfigurationDebugging,
                "Fresh task should enable addConfigurationDebugging after call");
    }

    @Test
    public void testAddconfigurationdebugging_afterReset() throws Exception {
        task.addconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging);

        // Manual reset
        task.configuration.addConfigurationDebugging = false;
        assertFalse(task.configuration.addConfigurationDebugging);

        // Call again
        task.addconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging,
                "Should be able to enable again after manual reset");
    }

    @Test
    public void testAddconfigurationdebugging_booleanValue() throws Exception {
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging);
        assertEquals(true, task.configuration.addConfigurationDebugging,
                "Should be exactly true");
    }

    @Test
    public void testAddconfigurationdebugging_persistsAcrossCalls() throws Exception {
        task.addconfigurationdebugging();
        assertTrue(task.configuration.addConfigurationDebugging);

        // Make other configuration changes
        task.verbose();
        task.keep("class * { *; }");
        task.printconfiguration("config.txt");

        // Configuration debugging should still be enabled
        assertTrue(task.configuration.addConfigurationDebugging,
                "addConfigurationDebugging should persist across other configuration calls");
    }

    // ========================================
    // Semantic Meaning Tests
    // ========================================

    @Test
    public void testAddconfigurationdebugging_semanticMeaning() throws Exception {
        // Verify the semantic purpose: adds debugging info to configuration
        assertFalse(task.configuration.addConfigurationDebugging,
                "Initially should not add debugging info");

        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Method should enable adding debugging information to configuration");
    }

    @Test
    public void testAddconfigurationdebugging_usedForTroubleshooting() throws Exception {
        // This option is typically used when troubleshooting configuration issues
        task.addconfigurationdebugging();
        task.printconfiguration("troubleshoot.txt");

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should be enabled for troubleshooting");
    }

    // ========================================
    // Combination Tests
    // ========================================

    @Test
    public void testAddconfigurationdebugging_withVerboseAndAllOutputs() throws Exception {
        task.verbose();
        task.addconfigurationdebugging();
        task.printconfiguration("config.txt");
        task.printseeds("seeds.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");
        task.dump("dump.txt");

        assertTrue(task.configuration.addConfigurationDebugging);
        assertTrue(task.configuration.verbose);
        assertNotNull(task.configuration.printConfiguration);
    }

    @Test
    public void testAddconfigurationdebugging_withOptimizationSettings() throws Exception {
        task.addconfigurationdebugging();
        task.dontoptimize();
        task.dontobfuscate();
        task.dontshrink();

        assertTrue(task.configuration.addConfigurationDebugging);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
    }

    @Test
    public void testAddconfigurationdebugging_withKeepAttributes() throws Exception {
        task.addconfigurationdebugging();
        task.keepattributes("Signature,Exceptions,*Annotation*");

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should work with keepattributes");
    }

    // ========================================
    // Call Sequence Tests
    // ========================================

    @Test
    public void testAddconfigurationdebugging_canBeCalledFirst() throws Exception {
        task.addconfigurationdebugging();
        task.verbose();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should work when called first");
    }

    @Test
    public void testAddconfigurationdebugging_canBeCalledLast() throws Exception {
        task.verbose();
        task.printconfiguration("config.txt");
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should work when called last");
    }

    @Test
    public void testAddconfigurationdebugging_canBeCalledAlone() throws Exception {
        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should work when called alone without other configuration");
    }

    @Test
    public void testAddconfigurationdebugging_complexConfigurationSequence() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.verbose();
        task.addconfigurationdebugging();
        task.keep("public class * { public *; }");
        task.printconfiguration("config.txt");
        task.printmapping("mapping.txt");
        task.dontobfuscate();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Should work in complex configuration sequence");
        assertTrue(task.configuration.verbose);
        assertFalse(task.configuration.obfuscate);
    }

    // ========================================
    // Comparison with getaddconfigurationdebugging()
    // ========================================

    @Test
    public void testAddconfigurationdebugging_sameEffectAsGetter() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.addconfigurationdebugging();
        task2.getaddconfigurationdebugging();

        assertEquals(task1.configuration.addConfigurationDebugging,
                task2.configuration.addConfigurationDebugging,
                "Both methods should have identical effect on configuration");
        assertTrue(task1.configuration.addConfigurationDebugging);
        assertTrue(task2.configuration.addConfigurationDebugging);
    }

    @Test
    public void testAddconfigurationdebugging_voidMethod() throws Exception {
        // This is a void method, returns nothing
        assertFalse(task.configuration.addConfigurationDebugging);

        task.addconfigurationdebugging();

        assertTrue(task.configuration.addConfigurationDebugging,
                "Void method should set configuration flag");
    }
}
