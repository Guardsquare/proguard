package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.VersionConstants;
import proguard.classfile.editor.ClassBuilder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link GsonOptimizer#execute(AppView)} method.
 * Tests the execution of GsonOptimizer with various configurations and AppView states.
 */
public class GsonOptimizerClaude_executeTest {

    private Configuration configuration;
    private AppView appView;

    @BeforeEach
    public void setUp() {
        configuration = new Configuration();
        appView = new AppView();
    }

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            optimizer.execute(null);
        }, "execute() should throw NullPointerException for null AppView");
    }

    /**
     * Tests execute() with empty program class pool (no Gson class).
     * The method should return early without performing any optimizations.
     * Per line 121-124 of GsonOptimizer.java, if there's no Gson class, the method returns early.
     */
    @Test
    public void testExecuteWithoutGsonClass() throws IOException {
        // Arrange
        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appViewWithoutGson = new AppView(emptyProgramClassPool, libraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert - Should complete successfully without doing anything
        assertDoesNotThrow(() -> optimizer.execute(appViewWithoutGson),
                "execute() should complete successfully when no Gson class is present");
    }

    /**
     * Tests execute() with Gson class present in program class pool.
     * This tests that the optimizer begins processing when Gson is detected.
     */
    @Test
    public void testExecuteWithGsonClassPresent() throws IOException {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        // Create a minimal Gson class
        ClassBuilder gsonClassBuilder = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                0x0021, // public class
                "com/google/gson/Gson",
                "java/lang/Object"
        );
        ProgramClass gsonClass = gsonClassBuilder.getProgramClass();
        programClassPool.addClass(gsonClass);

        AppView appViewWithGson = new AppView(programClassPool, libraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert - Should complete successfully
        assertDoesNotThrow(() -> optimizer.execute(appViewWithGson),
                "execute() should complete successfully when Gson class is present");
    }

    /**
     * Tests execute() with both empty program and library class pools.
     * Verifies the complete empty state is handled gracefully.
     */
    @Test
    public void testExecuteWithBothEmptyClassPools() throws IOException {
        // Arrange
        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView emptyAppView = new AppView(emptyProgramClassPool, emptyLibraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(emptyAppView),
                "execute() should handle both empty class pools");
    }

    /**
     * Tests execute() is called twice on the same optimizer.
     * Verifies that multiple executions are supported.
     */
    @Test
    public void testExecuteCalledMultipleTimes() throws IOException {
        // Arrange
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act - Execute twice
        optimizer.execute(appView);

        // Assert - Second execution should also complete successfully
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should support being called multiple times");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that the same optimizer can process different AppViews.
     */
    @Test
    public void testExecuteWithDifferentAppViews() throws IOException {
        // Arrange
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert - Execute with first AppView
        assertDoesNotThrow(() -> optimizer.execute(appView1),
                "execute() should work with first AppView");

        // Act & Assert - Execute with second AppView
        assertDoesNotThrow(() -> optimizer.execute(appView2),
                "execute() should work with second AppView");
    }

    /**
     * Tests execute() with configuration having optimizeConservatively set to true.
     * Per line 239 and 247, optimizeConservatively is passed to optimizers.
     */
    @Test
    public void testExecuteWithOptimizeConservativelyTrue() throws IOException {
        // Arrange
        configuration.optimizeConservatively = true;
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should handle optimizeConservatively=true");
    }

    /**
     * Tests execute() with configuration having optimizeConservatively set to false.
     */
    @Test
    public void testExecuteWithOptimizeConservativelyFalse() throws IOException {
        // Arrange
        configuration.optimizeConservatively = false;
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should handle optimizeConservatively=false");
    }

    /**
     * Tests execute() with configuration having warn list set to null.
     * Per line 145, configuration.warn is passed to WarningLogger.
     */
    @Test
    public void testExecuteWithNullWarnList() throws IOException {
        // Arrange
        configuration.warn = null;
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should handle null warn list in configuration");
    }

    /**
     * Tests execute() with configuration having empty warn list.
     */
    @Test
    public void testExecuteWithEmptyWarnList() throws IOException {
        // Arrange
        configuration.warn = new java.util.ArrayList<>();
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should handle empty warn list in configuration");
    }

    /**
     * Tests execute() with configuration having populated warn list.
     */
    @Test
    public void testExecuteWithPopulatedWarnList() throws IOException {
        // Arrange
        configuration.warn = new java.util.ArrayList<>();
        configuration.warn.add("*");
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should handle populated warn list in configuration");
    }

    /**
     * Tests execute() with a fresh Configuration instance.
     */
    @Test
    public void testExecuteWithFreshConfiguration() throws IOException {
        // Arrange
        Configuration freshConfig = new Configuration();
        GsonOptimizer optimizer = new GsonOptimizer(freshConfig);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should work with fresh Configuration");
    }

    /**
     * Tests execute() with AppView having empty resource file pool.
     */
    @Test
    public void testExecuteWithEmptyResourceFilePool() throws IOException {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        proguard.resources.file.ResourceFilePool emptyResourcePool =
            new proguard.resources.file.ResourceFilePool();
        proguard.io.ExtraDataEntryNameMap extraDataEntryNameMap =
            new proguard.io.ExtraDataEntryNameMap();

        AppView appViewWithEmptyResources = new AppView(
            programClassPool,
            libraryClassPool,
            emptyResourcePool,
            extraDataEntryNameMap
        );
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appViewWithEmptyResources),
                "execute() should handle AppView with empty resource file pool");
    }

    /**
     * Tests execute() completes without throwing IOException when no Gson is present.
     */
    @Test
    public void testExecuteDoesNotThrowIOExceptionWithoutGson() {
        // Arrange
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should not throw IOException when no Gson is present");
    }

    /**
     * Tests execute() with default AppView constructor.
     */
    @Test
    public void testExecuteWithDefaultAppView() throws IOException {
        // Arrange
        AppView defaultAppView = new AppView();
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(defaultAppView),
                "execute() should work with default AppView constructor");
    }

    /**
     * Tests execute() with AppView constructed with two ClassPools.
     */
    @Test
    public void testExecuteWithTwoClassPoolConstructor() throws IOException {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appViewTwoArgs = new AppView(programClassPool, libraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appViewTwoArgs),
                "execute() should work with two-argument AppView constructor");
    }

    /**
     * Tests execute() with AppView having non-empty but non-Gson program class pool.
     */
    @Test
    public void testExecuteWithNonGsonClasses() throws IOException {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        // Add a non-Gson class
        ClassBuilder testClassBuilder = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                0x0021, // public class
                "com/example/TestClass",
                "java/lang/Object"
        );
        ProgramClass testClass = testClassBuilder.getProgramClass();
        programClassPool.addClass(testClass);

        AppView appViewWithNonGsonClass = new AppView(programClassPool, libraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert - Should return early because there's no Gson class
        assertDoesNotThrow(() -> optimizer.execute(appViewWithNonGsonClass),
                "execute() should complete successfully when only non-Gson classes are present");
    }

    /**
     * Tests execute() with multiple non-Gson classes in program class pool.
     */
    @Test
    public void testExecuteWithMultipleNonGsonClasses() throws IOException {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        // Add multiple non-Gson classes
        for (int i = 0; i < 5; i++) {
            ClassBuilder classBuilder = new ClassBuilder(
                    VersionConstants.CLASS_VERSION_1_8,
                    0x0021,
                    "com/example/TestClass" + i,
                    "java/lang/Object"
            );
            programClassPool.addClass(classBuilder.getProgramClass());
        }

        AppView appViewWithMultipleClasses = new AppView(programClassPool, libraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appViewWithMultipleClasses),
                "execute() should handle multiple non-Gson classes");
    }

    /**
     * Tests execute() with different Configuration settings.
     */
    @Test
    public void testExecuteWithVariousConfigurationSettings() throws IOException {
        // Arrange
        Configuration configuredConfig = new Configuration();
        configuredConfig.verbose = true;
        configuredConfig.optimizeConservatively = true;
        configuredConfig.note = new java.util.ArrayList<>();
        configuredConfig.warn = new java.util.ArrayList<>();
        configuredConfig.warn.add("com.example.*");

        GsonOptimizer optimizer = new GsonOptimizer(configuredConfig);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should work with various configuration settings");
    }

    /**
     * Tests execute() can be called consecutively on different instances.
     */
    @Test
    public void testExecuteWithDifferentOptimizerInstances() throws IOException {
        // Arrange
        GsonOptimizer optimizer1 = new GsonOptimizer(configuration);
        GsonOptimizer optimizer2 = new GsonOptimizer(configuration);
        GsonOptimizer optimizer3 = new GsonOptimizer(new Configuration());

        // Act & Assert
        assertDoesNotThrow(() -> {
            optimizer1.execute(appView);
            optimizer2.execute(appView);
            optimizer3.execute(appView);
        }, "execute() should work with different optimizer instances");
    }

    /**
     * Tests execute() with configuration having all boolean flags set to true.
     */
    @Test
    public void testExecuteWithAllConfigurationFlagsTrue() throws IOException {
        // Arrange
        Configuration fullConfig = new Configuration();
        fullConfig.verbose = true;
        fullConfig.optimizeConservatively = true;
        fullConfig.shrink = true;
        fullConfig.optimize = true;
        fullConfig.obfuscate = true;
        fullConfig.preverify = true;
        fullConfig.allowAccessModification = true;
        fullConfig.mergeInterfacesAggressively = true;

        GsonOptimizer optimizer = new GsonOptimizer(fullConfig);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should work with all configuration flags set to true");
    }

    /**
     * Tests execute() with configuration having all boolean flags set to false.
     */
    @Test
    public void testExecuteWithAllConfigurationFlagsFalse() throws IOException {
        // Arrange
        Configuration minimalConfig = new Configuration();
        minimalConfig.verbose = false;
        minimalConfig.optimizeConservatively = false;
        minimalConfig.shrink = false;
        minimalConfig.optimize = false;
        minimalConfig.obfuscate = false;
        minimalConfig.preverify = false;
        minimalConfig.allowAccessModification = false;
        minimalConfig.mergeInterfacesAggressively = false;

        GsonOptimizer optimizer = new GsonOptimizer(minimalConfig);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView),
                "execute() should work with all configuration flags set to false");
    }

    /**
     * Tests execute() when called on the same AppView instance multiple times.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() throws IOException {
        // Arrange
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert - Multiple executions on same AppView
        assertDoesNotThrow(() -> {
            optimizer.execute(appView);
            optimizer.execute(appView);
            optimizer.execute(appView);
        }, "execute() should support multiple calls on the same AppView");
    }

    /**
     * Tests execute() with empty library class pool.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() throws IOException {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appViewEmptyLibrary = new AppView(programClassPool, emptyLibraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appViewEmptyLibrary),
                "execute() should handle empty library class pool");
    }

    /**
     * Tests execute() with empty program class pool but populated library class pool.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() throws IOException {
        // Arrange
        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        // Add a class to library pool (though it won't be used since program pool is empty)
        ClassBuilder libClassBuilder = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                0x0021,
                "java/util/ArrayList",
                "java/lang/Object"
        );
        libraryClassPool.addClass(libClassBuilder.getProgramClass());

        AppView appViewEmptyProgram = new AppView(emptyProgramClassPool, libraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appViewEmptyProgram),
                "execute() should handle empty program class pool with populated library pool");
    }

    /**
     * Tests that execute() returns without exception when given minimal valid inputs.
     */
    @Test
    public void testExecuteReturnsSuccessfullyWithMinimalInputs() throws IOException {
        // Arrange
        Configuration minimalConfig = new Configuration();
        AppView minimalAppView = new AppView();
        GsonOptimizer optimizer = new GsonOptimizer(minimalConfig);

        // Act
        optimizer.execute(minimalAppView);

        // Assert - No exception thrown means success
        assertTrue(true, "execute() completed successfully with minimal inputs");
    }

    /**
     * Tests execute() when configuration and AppView are both fully initialized.
     */
    @Test
    public void testExecuteWithFullyInitializedInputs() throws IOException {
        // Arrange
        Configuration fullConfig = new Configuration();
        fullConfig.verbose = true;
        fullConfig.warn = new java.util.ArrayList<>();
        fullConfig.note = new java.util.ArrayList<>();

        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView fullAppView = new AppView(programClassPool, libraryClassPool);

        GsonOptimizer optimizer = new GsonOptimizer(fullConfig);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(fullAppView),
                "execute() should work with fully initialized inputs");
    }

    /**
     * Tests that execute() method signature matches the Pass interface requirement.
     */
    @Test
    public void testExecuteMethodSignature() {
        // Arrange
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act - Verify that execute method exists and can be called
        assertDoesNotThrow(() -> {
            optimizer.execute(appView);
        }, "execute() method should match Pass interface signature");
    }

    /**
     * Tests execute() with the same configuration used by multiple optimizer instances.
     */
    @Test
    public void testExecuteWithSharedConfiguration() throws IOException {
        // Arrange
        Configuration sharedConfig = new Configuration();
        sharedConfig.optimizeConservatively = true;

        GsonOptimizer optimizer1 = new GsonOptimizer(sharedConfig);
        GsonOptimizer optimizer2 = new GsonOptimizer(sharedConfig);

        // Act & Assert - Both optimizers should work with shared configuration
        assertDoesNotThrow(() -> {
            optimizer1.execute(appView);
            optimizer2.execute(appView);
        }, "execute() should work when configuration is shared across instances");
    }

    /**
     * Tests that execute() does not modify the input AppView's class pools to be null.
     */
    @Test
    public void testExecuteDoesNotNullifyAppViewClassPools() throws IOException {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView testAppView = new AppView(programClassPool, libraryClassPool);
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Act
        optimizer.execute(testAppView);

        // Assert - Class pools should still be accessible and not null
        assertNotNull(testAppView.programClassPool, "Program class pool should not be null after execute");
        assertNotNull(testAppView.libraryClassPool, "Library class pool should not be null after execute");
    }

    /**
     * Tests that execute() does not modify the configuration.
     */
    @Test
    public void testExecuteDoesNotModifyConfiguration() throws IOException {
        // Arrange
        Configuration testConfig = new Configuration();
        testConfig.optimizeConservatively = true;
        testConfig.verbose = false;
        boolean originalOptimizeConservatively = testConfig.optimizeConservatively;
        boolean originalVerbose = testConfig.verbose;

        GsonOptimizer optimizer = new GsonOptimizer(testConfig);

        // Act
        optimizer.execute(appView);

        // Assert - Configuration should remain unchanged
        assertEquals(originalOptimizeConservatively, testConfig.optimizeConservatively,
                "optimizeConservatively should not be modified");
        assertEquals(originalVerbose, testConfig.verbose,
                "verbose should not be modified");
    }
}
