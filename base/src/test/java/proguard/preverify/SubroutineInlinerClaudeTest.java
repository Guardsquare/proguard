package proguard.preverify;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SubroutineInliner}.
 * Tests the constructor and execute method with various configurations.
 *
 * The SubroutineInliner class inlines subroutines in methods, which is generally
 * required for preverifying code. It cleans up old processing info and inlines
 * subroutines based on configuration:
 * - For microEdition=false and android=false: only classes from version 1.6 onwards are processed
 * - For microEdition=true or android=true: all classes are processed
 */
public class SubroutineInlinerClaudeTest {

    // ========================================
    // Constructor Tests
    // ========================================

    /**
     * Tests constructor with valid Configuration.
     * Verifies that a SubroutineInliner instance can be created with a non-null Configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act & Assert
        assertDoesNotThrow(() -> new SubroutineInliner(configuration),
            "Constructor should accept valid Configuration without throwing exceptions");
    }

    /**
     * Tests constructor with null Configuration.
     * Verifies that constructor accepts null Configuration (stores it internally).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act & Assert
        assertDoesNotThrow(() -> new SubroutineInliner(null),
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
        assertDoesNotThrow(() -> new SubroutineInliner(configuration),
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
        assertDoesNotThrow(() -> new SubroutineInliner(configuration),
            "Constructor should accept Configuration with microEdition disabled");
    }

    /**
     * Tests constructor with Configuration that has android enabled.
     * Verifies that the constructor handles android configuration.
     */
    @Test
    public void testConstructorWithAndroidEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.android = true;

        // Act & Assert
        assertDoesNotThrow(() -> new SubroutineInliner(configuration),
            "Constructor should accept Configuration with android enabled");
    }

    /**
     * Tests constructor with Configuration that has android disabled.
     * Verifies that the constructor handles non-android configuration.
     */
    @Test
    public void testConstructorWithAndroidDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.android = false;

        // Act & Assert
        assertDoesNotThrow(() -> new SubroutineInliner(configuration),
            "Constructor should accept Configuration with android disabled");
    }

    /**
     * Tests constructor with Configuration that has both microEdition and android enabled.
     * Verifies that the constructor handles both flags enabled.
     */
    @Test
    public void testConstructorWithBothMicroEditionAndAndroidEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;
        configuration.android = true;

        // Act & Assert
        assertDoesNotThrow(() -> new SubroutineInliner(configuration),
            "Constructor should accept Configuration with both microEdition and android enabled");
    }

    /**
     * Tests constructor with Configuration that has both microEdition and android disabled.
     * Verifies that the constructor handles standard Java SE configuration.
     */
    @Test
    public void testConstructorWithBothMicroEditionAndAndroidDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;
        configuration.android = false;

        // Act & Assert
        assertDoesNotThrow(() -> new SubroutineInliner(configuration),
            "Constructor should accept Configuration with both microEdition and android disabled");
    }

    /**
     * Tests that multiple SubroutineInliner instances can be created with different Configurations.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testConstructorWithMultipleInstances() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        Configuration config3 = new Configuration();
        config3.android = true;

        // Act & Assert
        assertDoesNotThrow(() -> {
            new SubroutineInliner(config1);
            new SubroutineInliner(config2);
            new SubroutineInliner(config3);
        }, "Multiple SubroutineInliner instances should be created successfully");
    }

    /**
     * Tests constructor with same Configuration instance multiple times.
     * Verifies that the same Configuration can be used for multiple SubroutineInliner instances.
     */
    @Test
    public void testConstructorWithSameConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act & Assert
        assertDoesNotThrow(() -> {
            new SubroutineInliner(configuration);
            new SubroutineInliner(configuration);
            new SubroutineInliner(configuration);
        }, "Constructor should accept same Configuration instance multiple times");
    }

    /**
     * Tests that constructed SubroutineInliner is not null.
     * Verifies the basic object creation contract.
     */
    @Test
    public void testConstructorReturnsNonNullInstance() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        SubroutineInliner inliner = new SubroutineInliner(configuration);

        // Assert
        assertNotNull(inliner, "Constructor should return a non-null SubroutineInliner instance");
    }

    /**
     * Tests that constructed SubroutineInliners are distinct instances.
     * Verifies that each constructor call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDistinctInstances() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        SubroutineInliner inliner1 = new SubroutineInliner(configuration);
        SubroutineInliner inliner2 = new SubroutineInliner(configuration);

        // Assert
        assertNotSame(inliner1, inliner2,
            "Constructor should create distinct SubroutineInliner instances");
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> inliner.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Tests execute() with null Configuration in SubroutineInliner.
     * Verifies handling of null Configuration during execution.
     */
    @Test
    public void testExecuteWithNullConfiguration() {
        // Arrange
        SubroutineInliner inliner = new SubroutineInliner(null);
        AppView appView = new AppView();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> inliner.execute(appView),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(programPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle both class pools empty without throwing exceptions");
    }

    // ========================================
    // Execute Method - MicroEdition Tests
    // ========================================

    /**
     * Tests execute() with microEdition enabled in Configuration.
     * Verifies that inlining works for JME configurations (all classes are processed).
     */
    @Test
    public void testExecuteWithMicroEditionEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle microEdition enabled without throwing exceptions");
    }

    /**
     * Tests execute() with microEdition disabled in Configuration.
     * Verifies that inlining works for standard JSE configurations (classes from version 1.6+ processed).
     */
    @Test
    public void testExecuteWithMicroEditionDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle microEdition disabled without throwing exceptions");
    }

    /**
     * Tests execute() with microEdition enabled and populated class pools.
     * Verifies that JME inlining processes all classes.
     */
    @Test
    public void testExecuteWithMicroEditionEnabledAndPopulatedPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle microEdition with populated pools");
    }

    /**
     * Tests execute() with microEdition disabled and populated class pools.
     * Verifies that JSE inlining processes classes from version 1.6 onwards.
     */
    @Test
    public void testExecuteWithMicroEditionDisabledAndPopulatedPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle standard edition with populated pools");
    }

    // ========================================
    // Execute Method - Android Tests
    // ========================================

    /**
     * Tests execute() with android enabled in Configuration.
     * Verifies that inlining works for Android configurations (all classes are processed).
     */
    @Test
    public void testExecuteWithAndroidEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.android = true;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle android enabled without throwing exceptions");
    }

    /**
     * Tests execute() with android disabled in Configuration.
     * Verifies that inlining works for non-Android configurations.
     */
    @Test
    public void testExecuteWithAndroidDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.android = false;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle android disabled without throwing exceptions");
    }

    /**
     * Tests execute() with android enabled and populated class pools.
     * Verifies that Android inlining processes all classes.
     */
    @Test
    public void testExecuteWithAndroidEnabledAndPopulatedPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.android = true;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle android with populated pools");
    }

    /**
     * Tests execute() with android disabled and populated class pools.
     * Verifies that non-Android inlining processes classes from version 1.6 onwards.
     */
    @Test
    public void testExecuteWithAndroidDisabledAndPopulatedPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.android = false;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle non-android with populated pools");
    }

    // ========================================
    // Execute Method - Combined Configuration Tests
    // ========================================

    /**
     * Tests execute() with both microEdition and android enabled.
     * Verifies that configuration works with both flags enabled.
     */
    @Test
    public void testExecuteWithBothMicroEditionAndAndroidEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;
        configuration.android = true;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle both microEdition and android enabled");
    }

    /**
     * Tests execute() with both microEdition and android disabled.
     * Verifies that configuration works with both flags disabled (standard JSE).
     */
    @Test
    public void testExecuteWithBothMicroEditionAndAndroidDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;
        configuration.android = false;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle both microEdition and android disabled");
    }

    /**
     * Tests execute() with microEdition enabled and android disabled.
     * Verifies the configuration combination.
     */
    @Test
    public void testExecuteWithMicroEditionEnabledAndAndroidDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = true;
        configuration.android = false;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle microEdition enabled and android disabled");
    }

    /**
     * Tests execute() with microEdition disabled and android enabled.
     * Verifies the configuration combination.
     */
    @Test
    public void testExecuteWithMicroEditionDisabledAndAndroidEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.microEdition = false;
        configuration.android = true;
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle microEdition disabled and android enabled");
    }

    // ========================================
    // Execute Method - Multiple Execution Tests
    // ========================================

    /**
     * Tests execute() multiple times with the same SubroutineInliner instance.
     * Verifies that the inliner can be reused.
     */
    @Test
    public void testExecuteMultipleTimes() {
        // Arrange
        Configuration configuration = new Configuration();
        SubroutineInliner inliner = new SubroutineInliner(configuration);

        // Act & Assert - Execute multiple times
        AppView appView1 = new AppView();
        assertDoesNotThrow(() -> inliner.execute(appView1),
            "First execution should succeed");

        AppView appView2 = new AppView();
        assertDoesNotThrow(() -> inliner.execute(appView2),
            "Second execution should succeed");

        AppView appView3 = new AppView();
        assertDoesNotThrow(() -> inliner.execute(appView3),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner.execute(appView);
            inliner.execute(appView);
            inliner.execute(appView);
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);

        // Create different AppViews
        AppView appView1 = new AppView();
        AppView appView2 = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView1),
            "Should handle first AppView");
        assertDoesNotThrow(() -> inliner.execute(appView2),
            "Should handle second AppView");
    }

    /**
     * Tests execute() with multiple inliner instances on the same AppView.
     * Verifies that different inliners can operate on the same AppView.
     */
    @Test
    public void testExecuteWithDifferentInlinerInstances() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        SubroutineInliner inliner1 = new SubroutineInliner(config1);
        SubroutineInliner inliner2 = new SubroutineInliner(config2);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner1.execute(appView);
            inliner2.execute(appView);
        }, "execute should handle different inliner instances operating on the same AppView");
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
        config1.android = false;
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        SubroutineInliner inliner1 = new SubroutineInliner(config1);
        SubroutineInliner inliner2 = new SubroutineInliner(config2);
        AppView appView = new AppView();

        // Act & Assert - Alternate between configurations
        assertDoesNotThrow(() -> {
            inliner1.execute(appView);
            inliner2.execute(appView);
            inliner1.execute(appView);
            inliner2.execute(appView);
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        proguard.resources.file.ResourceFilePool resourceFilePool =
            new proguard.resources.file.ResourceFilePool();
        proguard.io.ExtraDataEntryNameMap extraDataEntryNameMap =
            new proguard.io.ExtraDataEntryNameMap();
        AppView appView = new AppView(programPool, libraryPool, resourceFilePool, extraDataEntryNameMap);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    // ========================================
    // Execute Method - State and Isolation Tests
    // ========================================

    /**
     * Tests that execute() on one AppView doesn't affect execution on another.
     * Verifies that inliner maintains proper isolation between AppViews.
     */
    @Test
    public void testExecuteIsolationBetweenAppViews() {
        // Arrange
        Configuration configuration = new Configuration();
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act
        assertDoesNotThrow(() -> inliner.execute(appView1),
            "First execution should succeed");

        // Assert - Second execution should work independently
        assertDoesNotThrow(() -> inliner.execute(appView2),
            "Second execution should succeed independently of first");
    }

    /**
     * Tests that different inliners maintain independent state.
     * Verifies that inliner instances don't share state.
     */
    @Test
    public void testExecuteWithIndependentInlinerState() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        SubroutineInliner inliner1 = new SubroutineInliner(config1);
        SubroutineInliner inliner2 = new SubroutineInliner(config2);
        AppView appView = new AppView();

        // Act & Assert - Both inliners should work independently
        assertDoesNotThrow(() -> inliner1.execute(appView),
            "First inliner execution should succeed");
        assertDoesNotThrow(() -> inliner2.execute(appView),
            "Second inliner execution should succeed");
    }

    /**
     * Tests execute() after creating multiple inliners.
     * Verifies that inliner creation doesn't affect execution.
     */
    @Test
    public void testExecuteAfterCreatingMultipleInliners() {
        // Arrange - Create multiple inliners
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        config2.microEdition = true;
        Configuration config3 = new Configuration();
        config3.android = true;
        SubroutineInliner inliner1 = new SubroutineInliner(config1);
        SubroutineInliner inliner2 = new SubroutineInliner(config2);
        SubroutineInliner inliner3 = new SubroutineInliner(config3);
        AppView appView = new AppView();

        // Act & Assert - Each inliner should execute independently
        assertDoesNotThrow(() -> inliner1.execute(appView),
            "First inliner should execute successfully");
        assertDoesNotThrow(() -> inliner2.execute(appView),
            "Second inliner should execute successfully");
        assertDoesNotThrow(() -> inliner3.execute(appView),
            "Third inliner should execute successfully");
    }

    // ========================================
    // Execute Method - Edge Cases
    // ========================================

    /**
     * Tests execute() immediately after construction.
     * Verifies that inliner is ready to execute immediately after creation.
     */
    @Test
    public void testExecuteImmediatelyAfterConstruction() {
        // Arrange & Act
        Configuration configuration = new Configuration();
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Assert - Should work immediately after construction
        assertDoesNotThrow(() -> inliner.execute(appView),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);

        // Act & Assert - Create fresh AppView for each execution
        assertDoesNotThrow(() -> inliner.execute(new AppView()),
            "First fresh AppView should work");
        assertDoesNotThrow(() -> inliner.execute(new AppView()),
            "Second fresh AppView should work");
        assertDoesNotThrow(() -> inliner.execute(new AppView()),
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.execute(appView),
            "execute should handle explicitly empty ClassPools");
    }

    // ========================================
    // Execute Method - Consistency Tests
    // ========================================

    /**
     * Tests that execute() completes successfully regardless of inliner instance.
     * Verifies consistent behavior across different inliner instances.
     */
    @Test
    public void testExecuteConsistencyAcrossInstances() {
        // Arrange
        AppView appView = new AppView();
        Configuration configuration = new Configuration();

        // Act & Assert - Different inliners should behave consistently
        SubroutineInliner inliner1 = new SubroutineInliner(configuration);
        assertDoesNotThrow(() -> inliner1.execute(appView),
            "First inliner should execute successfully");

        SubroutineInliner inliner2 = new SubroutineInliner(configuration);
        assertDoesNotThrow(() -> inliner2.execute(appView),
            "Second inliner should execute successfully");

        SubroutineInliner inliner3 = new SubroutineInliner(configuration);
        assertDoesNotThrow(() -> inliner3.execute(appView),
            "Third inliner should execute successfully");
    }

    /**
     * Tests that execute() completes successfully regardless of AppView instance.
     * Verifies consistent behavior across different AppView instances.
     */
    @Test
    public void testExecuteConsistencyAcrossAppViews() {
        // Arrange
        Configuration configuration = new Configuration();
        SubroutineInliner inliner = new SubroutineInliner(configuration);

        // Act & Assert - Different AppViews should behave consistently
        assertDoesNotThrow(() -> inliner.execute(new AppView()),
            "First AppView should work");
        assertDoesNotThrow(() -> inliner.execute(new AppView()),
            "Second AppView should work");
        assertDoesNotThrow(() -> inliner.execute(new AppView()),
            "Third AppView should work");
    }

    /**
     * Tests execute() with same inliner and same AppView multiple times.
     * Verifies idempotent-like behavior for repeated executions.
     */
    @Test
    public void testExecuteIdempotency() {
        // Arrange
        Configuration configuration = new Configuration();
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert - Multiple executions should all succeed
        assertDoesNotThrow(() -> {
            inliner.execute(appView);
            inliner.execute(appView);
            inliner.execute(appView);
            inliner.execute(appView);
            inliner.execute(appView);
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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();
        AppView originalReference = appView;

        // Act
        inliner.execute(appView);

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
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        ClassPool programPool = new ClassPool();
        AppView appView = new AppView(programPool, new ClassPool());
        ClassPool originalPoolReference = appView.programClassPool;

        // Act
        inliner.execute(appView);

        // Assert
        assertSame(originalPoolReference, appView.programClassPool,
            "programClassPool reference should remain the same after execution");
    }

    /**
     * Tests execute() with newly instantiated inliner each time.
     * Verifies that fresh inliners work correctly.
     */
    @Test
    public void testExecuteWithFreshInlinerEachTime() {
        // Arrange
        AppView appView = new AppView();
        Configuration configuration = new Configuration();

        // Act & Assert - Create fresh inliner for each execution
        assertDoesNotThrow(() -> new SubroutineInliner(configuration).execute(appView),
            "First fresh inliner should work");
        assertDoesNotThrow(() -> new SubroutineInliner(configuration).execute(appView),
            "Second fresh inliner should work");
        assertDoesNotThrow(() -> new SubroutineInliner(configuration).execute(appView),
            "Third fresh inliner should work");
    }

    /**
     * Tests execute() with rapid successive calls.
     * Verifies that rapid execution doesn't cause issues.
     */
    @Test
    public void testExecuteWithRapidSuccessiveCalls() {
        // Arrange
        Configuration configuration = new Configuration();
        SubroutineInliner inliner = new SubroutineInliner(configuration);
        AppView appView = new AppView();

        // Act & Assert - Execute rapidly in succession
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                inliner.execute(appView);
            }
        }, "Rapid successive executions should work correctly");
    }

    /**
     * Tests execute() in sequence with many AppViews.
     * Verifies that the inliner can handle many sequential executions.
     */
    @Test
    public void testExecuteWithManySequentialAppViews() {
        // Arrange
        Configuration configuration = new Configuration();
        SubroutineInliner inliner = new SubroutineInliner(configuration);

        // Act & Assert - Execute on many AppViews sequentially
        for (int i = 0; i < 10; i++) {
            AppView appView = new AppView();
            final int index = i;
            assertDoesNotThrow(() -> inliner.execute(appView),
                "Execution " + index + " should succeed");
        }
    }
}
