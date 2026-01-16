package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.Configuration;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ProGuardTask.getdump()Ljava/lang/Object; method.
 * This is a Groovy DSL getter that returns null and calls dump()
 * to set configuration.dump = Configuration.STD_OUT.
 * Dump mode enables dumping the internal structure of class files to stdout during ProGuard processing.
 */
public class ProGuardTaskClaude_getdumpTest {

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
    public void testGetdump_returnsNull() throws Exception {
        Object result = task.getdump();

        assertNull(result, "getdump should return null for Groovy DSL support");
    }

    @Test
    public void testGetdump_setsDumpToStdOut() throws Exception {
        assertNull(task.configuration.dump, "dump should initially be null");

        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be set to Configuration.STD_OUT");
    }

    @Test
    public void testGetdump_callsDump() throws Exception {
        // getdump should call dump()
        assertNull(task.configuration.dump, "dump should initially be null");

        task.getdump();

        assertNotNull(task.configuration.dump, "dump() should have been called");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be set to stdout");
    }

    @Test
    public void testGetdump_isIdempotent() throws Exception {
        task.getdump();
        File firstResult = task.configuration.dump;

        task.getdump();

        assertSame(firstResult, task.configuration.dump,
                "Multiple calls should not change the state");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should remain STD_OUT");
    }

    @Test
    public void testGetdump_multipleCalls() throws Exception {
        task.getdump();
        task.getdump();
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should remain STD_OUT after multiple calls");
    }

    @Test
    public void testGetdump_alwaysReturnsNull() throws Exception {
        Object result1 = task.getdump();
        Object result2 = task.getdump();
        Object result3 = task.getdump();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetdump_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getdump();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_afterManuallySetToNull() throws Exception {
        task.configuration.dump = null;

        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be set to STD_OUT");
    }

    @Test
    public void testGetdump_whenAlreadyStdOut() throws Exception {
        task.configuration.dump = Configuration.STD_OUT;

        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should remain STD_OUT");
    }

    @Test
    public void testGetdump_supportsGroovyDSL() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        Object result = task.getdump();

        assertNull(result, "Should return null for Groovy property access");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should enable dump mode to stdout");
    }

    @Test
    public void testGetdump_returnValueIsNull() throws Exception {
        // Verify return type is Object and value is null
        Object result = task.getdump();

        assertNull(result, "Return type should be Object and value should be null");
    }

    @Test
    public void testGetdump_doesNotThrowException() throws Exception {
        // Should not throw any exception
        assertDoesNotThrow(() -> task.getdump(),
                "getdump should not throw any exception");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetdump_withOtherConfigurationSettings() throws Exception {
        task.dontoptimize();
        task.android();

        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testGetdump_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getdump();

        assertEquals(optimizeBefore, task.configuration.optimize,
                "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                "obfuscate setting should not change");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetdump_withVerbose() throws Exception {
        task.verbose();
        task.getdump();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_withDontShrink() throws Exception {
        task.dontshrink();
        task.getdump();

        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_withPrintOptions() throws Exception {
        task.printseeds();
        task.printmapping();
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_withPrintconfiguration() throws Exception {
        task.printconfiguration();
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_doesNotAffectShrinkSetting() throws Exception {
        boolean initialShrink = task.configuration.shrink;

        task.getdump();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
    }

    @Test
    public void testGetdump_doesNotAffectOptimizeSetting() throws Exception {
        boolean initialOptimize = task.configuration.optimize;

        task.getdump();

        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
    }

    @Test
    public void testGetdump_doesNotAffectObfuscateSetting() throws Exception {
        boolean initialObfuscate = task.configuration.obfuscate;

        task.getdump();

        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    // ============================================================
    // Call Order Tests
    // ============================================================

    @Test
    public void testGetdump_canBeCalledBeforeOtherConfiguration() throws Exception {
        task.getdump();
        task.verbose();
        task.keep("class * { *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testGetdump_canBeCalledAfterOtherConfiguration() throws Exception {
        task.verbose();
        task.keep("class * { *; }");
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testGetdump_canBeCalledBetweenOtherConfiguration() throws Exception {
        task.verbose();
        task.getdump();
        task.keep("class * { *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testGetdump_withComplexConfigurationChain() throws Exception {
        task.verbose();
        task.getdump();
        task.dontoptimize();
        task.printseeds();
        task.keep("public class * { *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.optimize, "optimize should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetdump_forDebugging() throws Exception {
        // Enable dump mode for debugging ProGuard issues
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should enable dump mode for debugging");
    }

    @Test
    public void testGetdump_forDetailedAnalysis() throws Exception {
        // Dump mode provides detailed internal structure of class files
        task.getdump();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should output to stdout");
        assertTrue(task.configuration.verbose,
                "verbose provides additional logging");
    }

    @Test
    public void testGetdump_forDevelopmentBuild() throws Exception {
        // Development build with dumping for analysis
        task.dontobfuscate();
        task.getdump();
        task.verbose();

        assertFalse(task.configuration.obfuscate,
                "obfuscation should be disabled for development");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should output to stdout");
    }

    @Test
    public void testGetdump_forTroubleshooting() throws Exception {
        // Troubleshooting build issues
        task.getdump();
        task.printconfiguration();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should output to stdout");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "configuration should output to stdout");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
    }

    @Test
    public void testGetdump_withFullReportingConfiguration() throws Exception {
        // Full reporting for CI/CD
        task.getdump();
        task.printseeds();
        task.printusage();
        task.printmapping();
        task.printconfiguration();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should output to stdout");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should output to stdout");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should output to stdout");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should output to stdout");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should output to stdout");
    }

    @Test
    public void testGetdump_forUnderstandingClassStructure() throws Exception {
        // Understanding internal class structure
        task.getdump();
        task.keep("class com.example.MyClass { *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should output class structure to stdout");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetdump_forCiBuild() throws Exception {
        // CI build with all output to stdout for log capture
        task.getdump();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should output to stdout for CI logs");
        assertTrue(task.configuration.verbose,
                "verbose should be enabled for CI logs");
    }

    @Test
    public void testGetdump_forInvestigatingProcessing() throws Exception {
        // Investigating what ProGuard is processing
        task.getdump();
        task.dontoptimize();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should show processing details");
        assertFalse(task.configuration.optimize,
                "optimization disabled for clearer analysis");
        assertFalse(task.configuration.obfuscate,
                "obfuscation disabled for clearer analysis");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetdump_equivalentToDumpCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.getdump();
        task2.dump();

        assertEquals(task1.configuration.dump, task2.configuration.dump,
                "Both methods should set the same value");
        assertEquals(Configuration.STD_OUT, task1.configuration.dump,
                "task1 should have STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.dump,
                "task2 should have STD_OUT");
    }

    @Test
    public void testGetdump_canBeMixedWithDumpCall() throws Exception {
        task.getdump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "After getter call, should be STD_OUT");

        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "After direct call, should still be STD_OUT");

        task.getdump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "After another getter call, should still be STD_OUT");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetdump_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getdump();
        task2.getdump();
        task3.getdump();

        assertEquals(Configuration.STD_OUT, task1.configuration.dump,
                "task1 dump should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.dump,
                "task2 dump should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task3.configuration.dump,
                "task3 dump should be STD_OUT");
    }

    @Test
    public void testGetdump_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "task dump should be STD_OUT");
        assertNull(otherTask.configuration.dump,
                "otherTask should not be affected");
    }

    @Test
    public void testGetdump_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertNull(freshTask.configuration.dump,
                "Fresh task should have dump as null");

        Object result = freshTask.getdump();

        assertNull(result, "Should return null");
        assertEquals(Configuration.STD_OUT, freshTask.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getdump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should be STD_OUT after first call");

        task.verbose();
        task.getdump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should remain STD_OUT after second call");

        task.keep("class * { *; }");
        task.getdump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should remain STD_OUT after third call");
    }

    @Test
    public void testGetdump_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getdump
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getdump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetdump_inAndroidDebugVariant() throws Exception {
        // Android debug variant with dump enabled
        task.android();
        task.getdump();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertFalse(task.configuration.obfuscate,
                "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetdump_inAndroidReleaseVariant() throws Exception {
        // Android release variant with dump for verification
        task.android();
        task.getdump();

        assertTrue(task.configuration.android, "android should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_forAndroidLibraryModule() throws Exception {
        // Android library with dump enabled
        task.android();
        task.getdump();
        task.keep("public class * { public *; }");

        assertTrue(task.configuration.android, "android should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetdump_forDebugBuild() throws Exception {
        // Debug build with dump and verbose
        task.getdump();
        task.verbose();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.obfuscate,
                "obfuscation should be disabled");
    }

    @Test
    public void testGetdump_forReleaseBuild() throws Exception {
        // Release build with dump for final verification
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_forStagingBuild() throws Exception {
        // Staging build with dump for testing
        task.getdump();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetdump_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.dump, "Initial state should be null");

        task.getdump();

        assertNotNull(task.configuration.dump, "State should be modified");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_preservesOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontobfuscate();

        task.getdump();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertFalse(task.configuration.obfuscate, "obfuscate should be preserved");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testGetdump_taskStateValid() throws Exception {
        task.getdump();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetdump_semanticMeaning() throws Exception {
        // Verify the semantic meaning: outputs internal class structure to stdout
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "getdump should enable dumping of internal class structure to stdout");
    }

    // ============================================================
    // Return Value Tests
    // ============================================================

    @Test
    public void testGetdump_returnValueConsistency() throws Exception {
        Object result1 = task.getdump();
        Object result2 = task.getdump();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetdump_alwaysReturnsNullRegardlessOfState() throws Exception {
        Object result1 = task.getdump();
        assertNull(result1, "First call should return null");

        Object result2 = task.getdump();
        assertNull(result2, "Second call should return null even though config is already set");

        Object result3 = task.getdump();
        assertNull(result3, "Third call should still return null");
    }

    @Test
    public void testGetdump_returnTypeIsObject() throws Exception {
        // Verify the return type is Object (not void)
        Object result = task.getdump();
        // If the method returns void, this would be a compilation error
        assertNull(result, "Return type should be Object and value should be null");
    }

    // ============================================================
    // Annotation Tests
    // ============================================================

    @Test
    public void testGetdump_isAnnotatedAsInternal() throws Exception {
        // The method should be annotated with @Internal for Gradle
        // This is a compile-time check, but we verify the method exists and works
        Object result = task.getdump();
        assertNull(result, "Method should exist and return null");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetdump_supportsGroovyPropertySyntax() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: task.dump (without parentheses) calls task.getdump()
        Object result = task.getdump();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should set dump configuration");
    }

    @Test
    public void testGetdump_behavesLikeUnderlyingMethod() throws Exception {
        // getdump() should behave like dump()
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.getdump();
        task2.dump();

        assertEquals(task1.configuration.dump, task2.configuration.dump,
                "Both should have the same dump value");
    }

    // ============================================================
    // Output Configuration Tests
    // ============================================================

    @Test
    public void testGetdump_outputsToStdOut() throws Exception {
        // dump outputs internal structure to standard output
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should be configured to output to stdout");
    }

    @Test
    public void testGetdump_forReviewingInternalStructure() throws Exception {
        // Review internal class structure
        task.keep("class * { *; }");
        task.getdump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "Should output internal structure");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ============================================================
    // Verification Tests
    // ============================================================

    @Test
    public void testGetdump_configurationNotNull() throws Exception {
        task.getdump();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetdump_dumpNotNull() throws Exception {
        task.getdump();

        assertNotNull(task.configuration.dump, "dump should not be null after getdump()");
    }

    @Test
    public void testGetdump_verifyStdOutMarker() throws Exception {
        task.getdump();

        assertEquals("", task.configuration.dump.getPath(),
                "Configuration.STD_OUT should have empty path as marker");
    }
}
