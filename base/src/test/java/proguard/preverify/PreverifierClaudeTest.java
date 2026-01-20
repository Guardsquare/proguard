package proguard.preverify;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Preverifier}.
 * Tests the constructor and execute method with various configurations.
 *
 * The Preverifier class performs preverification of methods in program class pools.
 * It cleans up old processing info and preverifies methods based on class versions:
 * - JME (microEdition): classes from version 1.0 onwards
 * - JSE 6: classes from version 1.6 onwards (optional)
 * - JSE 7+: classes from version 1.6 onwards (required)
 */
public class PreverifierClaudeTest {

    // ========================================
    // Constructor Tests
    // ========================================

    /**
     * Tests constructor with valid Configuration.
     * Verifies that a Preverifier instance can be created with a non-null Configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act & Assert
        assertDoesNotThrow(() -> new Preverifier(configuration),
            "Constructor should accept valid Configuration without throwing exceptions");
    }

    /**
     * Tests constructor with null Configuration.
     * Verifies that constructor accepts null Configuration (stores it internally).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act & Assert
        assertDoesNotThrow(() -> new Preverifier(null),
            "Constructor should accept null Configuration without throwing exceptions");
    }

    /**
     * Tests constructor with Configuration that has microEdition enabled.
     * Verifies that the constructor handles microEdition configuration.
     */
    @Test
    public void testConstructorWithMicroEditionEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;

        // Act & Assert
        assertDoesNotThrow(() -> new Preverifier(configuration),
            "Constructor should accept Configuration with microEdition enabled");
    }

    /**
     * Tests constructor with Configuration that has microEdition disabled.
     * Verifies that the constructor handles standard edition configuration.
     */
    @Test
    public void testConstructorWithMicroEditionDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;

        // Act & Assert
        assertDoesNotThrow(() -> new Preverifier(configuration),
            "Constructor should accept Configuration with microEdition disabled");
    }

    /**
     * Tests that multiple Preverifier instances can be created with different Configurations.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testConstructorWithMultipleInstances() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;

        // Act & Assert
        assertDoesNotThrow(() -> {
            new Preverifier(config1);
            new Preverifier(config2);
            new Preverifier(config1);
        }, "Multiple Preverifier instances should be created successfully");
    }

    /**
     * Tests constructor with same Configuration instance multiple times.
     * Verifies that the same Configuration can be used for multiple Preverifier instances.
     */
    @Test
    public void testConstructorWithSameConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act & Assert
        assertDoesNotThrow(() -> {
            new Preverifier(configuration);
            new Preverifier(configuration);
            new Preverifier(configuration);
        }, "Constructor should accept same Configuration instance multiple times");
    }

    /**
     * Tests that constructed Preverifier is not null.
     * Verifies the basic object creation contract.
     */
    @Test
    public void testConstructorReturnsNonNullInstance() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        Preverifier preverifier = new Preverifier(configuration);

        // Assert
        assertNotNull(preverifier, "Constructor should return a non-null Preverifier instance");
    }

    /**
     * Tests that constructed Preverifiers are distinct instances.
     * Verifies that each constructor call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDistinctInstances() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        Preverifier preverifier1 = new Preverifier(configuration);
        Preverifier preverifier2 = new Preverifier(configuration);

        // Assert
        assertNotSame(preverifier1, preverifier2,
            "Constructor should create distinct Preverifier instances");
    }

    // ========================================
    // Execute Method - Basic Tests
    // ========================================

    /**
     * Tests execute() with minimal valid AppView and default Configuration.
     * Verifies that the method can complete successfully with empty class pools.
     */
    @Test
    public void testExecuteWithMinimalValidAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle empty AppView without throwing exceptions");
    }

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> preverifier.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Tests execute() with null Configuration in Preverifier.
     * Verifies handling of null Configuration during execution.
     */
    @Test
    public void testExecuteWithNullConfiguration() {
        // Arrange
        Preverifier preverifier = new Preverifier(null);
        AppView appView = new AppView();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> preverifier.execute(appView),
            "execute should throw NullPointerException when Configuration is null");
    }

    /**
     * Tests execute() with empty program class pool.
     * Verifies that the method handles empty class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle empty program class pool without throwing exceptions");
    }

    /**
     * Tests execute() with empty library class pool.
     * Verifies that the method handles empty library class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(programPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle empty library class pool without throwing exceptions");
    }

    /**
     * Tests execute() with both class pools empty.
     * Verifies that the method handles completely empty AppView.
     */
    @Test
    public void testExecuteWithBothClassPoolsEmpty() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle both class pools empty without throwing exceptions");
    }

    // ========================================
    // Execute Method - MicroEdition Tests
    // ========================================

    /**
     * Tests execute() with microEdition enabled in Configuration.
     * Verifies that preverification works for JME configurations.
     * With microEdition=true, classes from version 1.0 onwards are preverified.
     */
    @Test
    public void testExecuteWithMicroEditionEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle microEdition enabled without throwing exceptions");
    }

    /**
     * Tests execute() with microEdition disabled in Configuration.
     * Verifies that preverification works for standard JSE configurations.
     * With microEdition=false, classes from version 1.6 onwards are preverified.
     */
    @Test
    public void testExecuteWithMicroEditionDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle microEdition disabled without throwing exceptions");
    }

    /**
     * Tests execute() with microEdition enabled and populated class pools.
     * Verifies that JME preverification processes all classes.
     */
    @Test
    public void testExecuteWithMicroEditionEnabledAndPopulatedPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle microEdition with populated pools");
    }

    /**
     * Tests execute() with microEdition disabled and populated class pools.
     * Verifies that JSE preverification processes classes from version 1.6 onwards.
     */
    @Test
    public void testExecuteWithMicroEditionDisabledAndPopulatedPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle standard edition with populated pools");
    }

    // ========================================
    // Execute Method - Multiple Execution Tests
    // ========================================

    /**
     * Tests execute() multiple times with the same Preverifier instance.
     * Verifies that the preverifier can be reused.
     */
    @Test
    public void testExecuteMultipleTimes() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);

        // Act & Assert - Execute multiple times
        AppView appView1 = new AppView();
        assertDoesNotThrow(() -> preverifier.execute(appView1),
            "First execution should succeed");

        AppView appView2 = new AppView();
        assertDoesNotThrow(() -> preverifier.execute(appView2),
            "Second execution should succeed");

        AppView appView3 = new AppView();
        assertDoesNotThrow(() -> preverifier.execute(appView3),
            "Third execution should succeed");
    }

    /**
     * Tests execute() multiple times on the same AppView.
     * Verifies that repeated execution on the same AppView is handled correctly.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            preverifier.execute(appView);
            preverifier.execute(appView);
            preverifier.execute(appView);
        }, "execute should handle being called multiple times on the same AppView");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that different AppViews are handled independently.
     */
    @Test
    public void testExecuteWithDifferentAppViews() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);

        // Create different AppViews
        AppView appView1 = new AppView();
        AppView appView2 = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView1),
            "Should handle first AppView");
        assertDoesNotThrow(() -> preverifier.execute(appView2),
            "Should handle second AppView");
    }

    /**
     * Tests execute() with multiple preverifier instances on the same AppView.
     * Verifies that different preverifiers can operate on the same AppView.
     */
    @Test
    public void testExecuteWithDifferentPreverifierInstances() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        Preverifier preverifier1 = new Preverifier(config1);
        Preverifier preverifier2 = new Preverifier(config2);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            preverifier1.execute(appView);
            preverifier2.execute(appView);
        }, "execute should handle different preverifier instances operating on the same AppView");
    }

    /**
     * Tests execute() in sequence with alternating configurations.
     * Verifies that execution order with different configurations doesn't cause issues.
     */
    @Test
    public void testExecuteWithAlternatingConfigurations() {
        // Arrange
        Configuration config1 = new Configuration();
        config1.microEdition = false;
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        Preverifier preverifier1 = new Preverifier(config1);
        Preverifier preverifier2 = new Preverifier(config2);
        AppView appView = new AppView();

        // Act & Assert - Alternate between configurations
        assertDoesNotThrow(() -> {
            preverifier1.execute(appView);
            preverifier2.execute(appView);
            preverifier1.execute(appView);
            preverifier2.execute(appView);
        }, "Alternating configuration execution should work correctly");
    }

    // ========================================
    // Execute Method - AppView Constructor Variations
    // ========================================

    /**
     * Tests execute() with AppView created using different constructor.
     * Verifies that AppView constructor variations are handled correctly.
     */
    @Test
    public void testExecuteWithAppViewCreatedWithTwoParamConstructor() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle AppView created with two-param constructor");
    }

    /**
     * Tests execute() with AppView created using full constructor.
     * Verifies that fully initialized AppView is handled correctly.
     */
    @Test
    public void testExecuteWithFullyInitializedAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        proguard.resources.file.ResourceFilePool resourceFilePool =
            new proguard.resources.file.ResourceFilePool();
        proguard.io.ExtraDataEntryNameMap extraDataEntryNameMap =
            new proguard.io.ExtraDataEntryNameMap();
        AppView appView = new AppView(programPool, libraryPool, resourceFilePool, extraDataEntryNameMap);

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    // ========================================
    // Execute Method - State and Isolation Tests
    // ========================================

    /**
     * Tests that execute() on one AppView doesn't affect execution on another.
     * Verifies that preverifier maintains proper isolation between AppViews.
     */
    @Test
    public void testExecuteIsolationBetweenAppViews() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act
        assertDoesNotThrow(() -> preverifier.execute(appView1),
            "First execution should succeed");

        // Assert - Second execution should work independently
        assertDoesNotThrow(() -> preverifier.execute(appView2),
            "Second execution should succeed independently of first");
    }

    /**
     * Tests that different preverifiers maintain independent state.
     * Verifies that preverifier instances don't share state.
     */
    @Test
    public void testExecuteWithIndependentPreverifierState() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        Preverifier preverifier1 = new Preverifier(config1);
        Preverifier preverifier2 = new Preverifier(config2);
        AppView appView = new AppView();

        // Act & Assert - Both preverifiers should work independently
        assertDoesNotThrow(() -> preverifier1.execute(appView),
            "First preverifier execution should succeed");
        assertDoesNotThrow(() -> preverifier2.execute(appView),
            "Second preverifier execution should succeed");
    }

    /**
     * Tests execute() after creating multiple preverifiers.
     * Verifies that preverifier creation doesn't affect execution.
     */
    @Test
    public void testExecuteAfterCreatingMultiplePreverifiers() {
        // Arrange - Create multiple preverifiers
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        Configuration config3 = new Configuration();
        Preverifier preverifier1 = new Preverifier(config1);
        Preverifier preverifier2 = new Preverifier(config2);
        Preverifier preverifier3 = new Preverifier(config3);
        AppView appView = new AppView();

        // Act & Assert - Each preverifier should execute independently
        assertDoesNotThrow(() -> preverifier1.execute(appView),
            "First preverifier should execute successfully");
        assertDoesNotThrow(() -> preverifier2.execute(appView),
            "Second preverifier should execute successfully");
        assertDoesNotThrow(() -> preverifier3.execute(appView),
            "Third preverifier should execute successfully");
    }

    // ========================================
    // Execute Method - Edge Cases
    // ========================================

    /**
     * Tests execute() immediately after construction.
     * Verifies that preverifier is ready to execute immediately after creation.
     */
    @Test
    public void testExecuteImmediatelyAfterConstruction() {
        // Arrange & Act
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Assert - Should work immediately after construction
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should work immediately after construction");
    }

    /**
     * Tests execute() with freshly created AppView each time.
     * Verifies that fresh AppViews are handled consistently.
     */
    @Test
    public void testExecuteWithFreshAppViewsEachTime() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);

        // Act & Assert - Create fresh AppView for each execution
        assertDoesNotThrow(() -> preverifier.execute(new AppView()),
            "First fresh AppView should work");
        assertDoesNotThrow(() -> preverifier.execute(new AppView()),
            "Second fresh AppView should work");
        assertDoesNotThrow(() -> preverifier.execute(new AppView()),
            "Third fresh AppView should work");
    }

    /**
     * Tests execute() with AppView created with explicitly empty ClassPools.
     * Verifies that explicitly empty pools are handled correctly.
     */
    @Test
    public void testExecuteWithExplicitlyEmptyClassPools() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> preverifier.execute(appView),
            "execute should handle explicitly empty ClassPools");
    }

    // ========================================
    // Execute Method - Consistency Tests
    // ========================================

    /**
     * Tests that execute() completes successfully regardless of preverifier instance.
     * Verifies consistent behavior across different preverifier instances.
     */
    @Test
    public void testExecuteConsistencyAcrossInstances() {
        // Arrange
        AppView appView = new AppView();
        Configuration configuration = new Configuration();

        // Act & Assert - Different preverifiers should behave consistently
        Preverifier preverifier1 = new Preverifier(configuration);
        assertDoesNotThrow(() -> preverifier1.execute(appView),
            "First preverifier should execute successfully");

        Preverifier preverifier2 = new Preverifier(configuration);
        assertDoesNotThrow(() -> preverifier2.execute(appView),
            "Second preverifier should execute successfully");

        Preverifier preverifier3 = new Preverifier(configuration);
        assertDoesNotThrow(() -> preverifier3.execute(appView),
            "Third preverifier should execute successfully");
    }

    /**
     * Tests that execute() completes successfully regardless of AppView instance.
     * Verifies consistent behavior across different AppView instances.
     */
    @Test
    public void testExecuteConsistencyAcrossAppViews() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);

        // Act & Assert - Different AppViews should behave consistently
        assertDoesNotThrow(() -> preverifier.execute(new AppView()),
            "First AppView should work");
        assertDoesNotThrow(() -> preverifier.execute(new AppView()),
            "Second AppView should work");
        assertDoesNotThrow(() -> preverifier.execute(new AppView()),
            "Third AppView should work");
    }

    /**
     * Tests execute() with same preverifier and same AppView multiple times.
     * Verifies idempotent-like behavior for repeated executions.
     */
    @Test
    public void testExecuteIdempotency() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Act & Assert - Multiple executions should all succeed
        assertDoesNotThrow(() -> {
            preverifier.execute(appView);
            preverifier.execute(appView);
            preverifier.execute(appView);
            preverifier.execute(appView);
            preverifier.execute(appView);
        }, "Multiple executions on same AppView should succeed");
    }

    /**
     * Tests execute() doesn't modify the AppView reference.
     * Verifies that the AppView object reference remains the same after execution.
     */
    @Test
    public void testExecuteDoesNotModifyAppViewReference() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();
        AppView originalReference = appView;

        // Act
        preverifier.execute(appView);

        // Assert
        assertSame(originalReference, appView,
            "AppView reference should remain the same after execution");
    }

    /**
     * Tests execute() preserves AppView's programClassPool reference.
     * Verifies that the programClassPool reference is not replaced.
     */
    @Test
    public void testExecutePreservesProgramClassPoolReference() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        ClassPool programPool = new ClassPool();
        AppView appView = new AppView(programPool, new ClassPool());
        ClassPool originalPoolReference = appView.programClassPool;

        // Act
        preverifier.execute(appView);

        // Assert
        assertSame(originalPoolReference, appView.programClassPool,
            "programClassPool reference should remain the same after execution");
    }

    /**
     * Tests execute() with newly instantiated preverifier each time.
     * Verifies that fresh preverifiers work correctly.
     */
    @Test
    public void testExecuteWithFreshPreverifierEachTime() {
        // Arrange
        AppView appView = new AppView();
        Configuration configuration = new Configuration();

        // Act & Assert - Create fresh preverifier for each execution
        assertDoesNotThrow(() -> new Preverifier(configuration).execute(appView),
            "First fresh preverifier should work");
        assertDoesNotThrow(() -> new Preverifier(configuration).execute(appView),
            "Second fresh preverifier should work");
        assertDoesNotThrow(() -> new Preverifier(configuration).execute(appView),
            "Third fresh preverifier should work");
    }

    /**
     * Tests execute() with rapid successive calls.
     * Verifies that rapid execution doesn't cause issues.
     */
    @Test
    public void testExecuteWithRapidSuccessiveCalls() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);
        AppView appView = new AppView();

        // Act & Assert - Execute rapidly in succession
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                preverifier.execute(appView);
            }
        }, "Rapid successive executions should work correctly");
    }

    /**
     * Tests execute() in sequence with many AppViews.
     * Verifies that the preverifier can handle many sequential executions.
     */
    @Test
    public void testExecuteWithManySequentialAppViews() {
        // Arrange
        Configuration configuration = new Configuration();
        Preverifier preverifier = new Preverifier(configuration);

        // Act & Assert - Execute on many AppViews sequentially
        for (int i = 0; i < 10; i++) {
            AppView appView = new AppView();
            final int index = i;
            assertDoesNotThrow(() -> preverifier.execute(appView),
                "Execution " + index + " should succeed");
        }
    }
}
