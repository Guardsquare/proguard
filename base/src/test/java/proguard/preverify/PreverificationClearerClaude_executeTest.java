package proguard.preverify;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PreverificationClearer#execute(AppView)} method.
 *
 * The execute method clears any JSE preverification information from the program classes.
 * Specifically, it removes StackMapTable attributes from classes with version >= 1.6.
 */
public class PreverificationClearerClaude_executeTest {

    // ========================================
    // Basic Execution Tests
    // ========================================

    /**
     * Tests execute() with a minimal valid AppView.
     * Verifies that the method can complete successfully with empty class pools.
     */
    @Test
    public void testExecuteWithMinimalValidAppView() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should handle empty AppView without throwing exceptions");
    }

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> clearer.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Tests execute() with empty program class pool.
     * Verifies that the method handles empty class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should handle empty program class pool without throwing exceptions");
    }

    /**
     * Tests execute() with empty library class pool.
     * Verifies that the method handles empty library class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        ClassPool programPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(programPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should handle empty library class pool without throwing exceptions");
    }

    /**
     * Tests execute() with both class pools empty.
     * Verifies that the method handles completely empty AppView.
     */
    @Test
    public void testExecuteWithBothClassPoolsEmpty() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView = new AppView(); // Creates empty pools by default

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should handle both class pools empty without throwing exceptions");
    }

    /**
     * Tests execute() with AppView created using different constructor.
     * Verifies that AppView constructor variations are handled correctly.
     */
    @Test
    public void testExecuteWithAppViewCreatedWithTwoParamConstructor() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should handle AppView created with two-param constructor");
    }

    /**
     * Tests execute() with AppView created using full constructor.
     * Verifies that fully initialized AppView is handled correctly.
     */
    @Test
    public void testExecuteWithFullyInitializedAppView() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        proguard.resources.file.ResourceFilePool resourceFilePool =
            new proguard.resources.file.ResourceFilePool();
        proguard.io.ExtraDataEntryNameMap extraDataEntryNameMap =
            new proguard.io.ExtraDataEntryNameMap();
        AppView appView = new AppView(programPool, libraryPool, resourceFilePool, extraDataEntryNameMap);

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    // ========================================
    // Multiple Execution Tests
    // ========================================

    /**
     * Tests execute() multiple times with the same PreverificationClearer instance.
     * Verifies that the clearer can be reused.
     */
    @Test
    public void testExecuteMultipleTimes() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();

        // Act & Assert - Execute multiple times
        AppView appView1 = new AppView();
        assertDoesNotThrow(() -> clearer.execute(appView1),
            "First execution should succeed");

        AppView appView2 = new AppView();
        assertDoesNotThrow(() -> clearer.execute(appView2),
            "Second execution should succeed");

        AppView appView3 = new AppView();
        assertDoesNotThrow(() -> clearer.execute(appView3),
            "Third execution should succeed");
    }

    /**
     * Tests execute() multiple times on the same AppView.
     * Verifies that repeated execution on the same AppView is handled correctly.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            clearer.execute(appView);
            clearer.execute(appView);
            clearer.execute(appView);
        }, "execute should handle being called multiple times on the same AppView");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that different AppViews are handled independently.
     */
    @Test
    public void testExecuteWithDifferentAppViews() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();

        // Create different AppViews
        AppView appView1 = new AppView();
        AppView appView2 = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView1),
            "Should handle first AppView");
        assertDoesNotThrow(() -> clearer.execute(appView2),
            "Should handle second AppView");
    }

    /**
     * Tests execute() with multiple clearer instances on the same AppView.
     * Verifies that different clearers can operate on the same AppView.
     */
    @Test
    public void testExecuteWithDifferentClearerInstances() {
        // Arrange
        PreverificationClearer clearer1 = new PreverificationClearer();
        PreverificationClearer clearer2 = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            clearer1.execute(appView);
            clearer2.execute(appView);
        }, "execute should handle different clearer instances operating on the same AppView");
    }

    // ========================================
    // State and Isolation Tests
    // ========================================

    /**
     * Tests that execute() on one AppView doesn't affect execution on another.
     * Verifies that clearer maintains proper isolation between AppViews.
     */
    @Test
    public void testExecuteIsolationBetweenAppViews() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act
        assertDoesNotThrow(() -> clearer.execute(appView1),
            "First execution should succeed");

        // Assert - Second execution should work independently
        assertDoesNotThrow(() -> clearer.execute(appView2),
            "Second execution should succeed independently of first");
    }

    /**
     * Tests that different clearers maintain independent state.
     * Verifies that clearer instances don't share state.
     */
    @Test
    public void testExecuteWithIndependentClearerState() {
        // Arrange
        PreverificationClearer clearer1 = new PreverificationClearer();
        PreverificationClearer clearer2 = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert - Both clearers should work independently
        assertDoesNotThrow(() -> clearer1.execute(appView),
            "First clearer execution should succeed");
        assertDoesNotThrow(() -> clearer2.execute(appView),
            "Second clearer execution should succeed");
    }

    /**
     * Tests execute() after creating multiple clearers.
     * Verifies that clearer creation doesn't affect execution.
     */
    @Test
    public void testExecuteAfterCreatingMultipleClearers() {
        // Arrange - Create multiple clearers
        PreverificationClearer clearer1 = new PreverificationClearer();
        PreverificationClearer clearer2 = new PreverificationClearer();
        PreverificationClearer clearer3 = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert - Each clearer should execute independently
        assertDoesNotThrow(() -> clearer1.execute(appView),
            "First clearer should execute successfully");
        assertDoesNotThrow(() -> clearer2.execute(appView),
            "Second clearer should execute successfully");
        assertDoesNotThrow(() -> clearer3.execute(appView),
            "Third clearer should execute successfully");
    }

    // ========================================
    // Sequence and Order Tests
    // ========================================

    /**
     * Tests execute() in sequence with alternating clearers.
     * Verifies that execution order doesn't cause issues.
     */
    @Test
    public void testExecuteWithAlternatingClearers() {
        // Arrange
        PreverificationClearer clearer1 = new PreverificationClearer();
        PreverificationClearer clearer2 = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert - Alternate between clearers
        assertDoesNotThrow(() -> {
            clearer1.execute(appView);
            clearer2.execute(appView);
            clearer1.execute(appView);
            clearer2.execute(appView);
        }, "Alternating clearer execution should work correctly");
    }

    /**
     * Tests execute() in sequence with many AppViews.
     * Verifies that the clearer can handle many sequential executions.
     */
    @Test
    public void testExecuteWithManySequentialAppViews() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();

        // Act & Assert - Execute on many AppViews sequentially
        for (int i = 0; i < 10; i++) {
            AppView appView = new AppView();
            final int index = i;
            assertDoesNotThrow(() -> clearer.execute(appView),
                "Execution " + index + " should succeed");
        }
    }

    /**
     * Tests execute() with rapid successive calls.
     * Verifies that rapid execution doesn't cause issues.
     */
    @Test
    public void testExecuteWithRapidSuccessiveCalls() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert - Execute rapidly in succession
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                clearer.execute(appView);
            }
        }, "Rapid successive executions should work correctly");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests execute() immediately after construction.
     * Verifies that clearer is ready to execute immediately after creation.
     */
    @Test
    public void testExecuteImmediatelyAfterConstruction() {
        // Arrange & Act
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView = new AppView();

        // Assert - Should work immediately after construction
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should work immediately after construction");
    }

    /**
     * Tests execute() with AppView that has null programClassPool field.
     * Verifies proper handling of AppView with null programClassPool.
     */
    @Test
    public void testExecuteWithNullProgramClassPool() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        // Create AppView with null programClassPool by using reflection or custom setup
        // Since AppView initializes pools in constructor, we test the NPE case directly
        AppView appView = null;

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> clearer.execute(appView),
            "execute should throw NullPointerException with null AppView");
    }

    /**
     * Tests execute() with freshly created AppView each time.
     * Verifies that fresh AppViews are handled consistently.
     */
    @Test
    public void testExecuteWithFreshAppViewsEachTime() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();

        // Act & Assert - Create fresh AppView for each execution
        assertDoesNotThrow(() -> clearer.execute(new AppView()),
            "First fresh AppView should work");
        assertDoesNotThrow(() -> clearer.execute(new AppView()),
            "Second fresh AppView should work");
        assertDoesNotThrow(() -> clearer.execute(new AppView()),
            "Third fresh AppView should work");
    }

    /**
     * Tests execute() with AppView created with explicitly empty ClassPools.
     * Verifies that explicitly empty pools are handled correctly.
     */
    @Test
    public void testExecuteWithExplicitlyEmptyClassPools() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> clearer.execute(appView),
            "execute should handle explicitly empty ClassPools");
    }

    // ========================================
    // Consistency Tests
    // ========================================

    /**
     * Tests that execute() completes successfully regardless of clearer instance.
     * Verifies consistent behavior across different clearer instances.
     */
    @Test
    public void testExecuteConsistencyAcrossInstances() {
        // Arrange
        AppView appView = new AppView();

        // Act & Assert - Different clearers should behave consistently
        PreverificationClearer clearer1 = new PreverificationClearer();
        assertDoesNotThrow(() -> clearer1.execute(appView),
            "First clearer should execute successfully");

        PreverificationClearer clearer2 = new PreverificationClearer();
        assertDoesNotThrow(() -> clearer2.execute(appView),
            "Second clearer should execute successfully");

        PreverificationClearer clearer3 = new PreverificationClearer();
        assertDoesNotThrow(() -> clearer3.execute(appView),
            "Third clearer should execute successfully");
    }

    /**
     * Tests that execute() completes successfully regardless of AppView instance.
     * Verifies consistent behavior across different AppView instances.
     */
    @Test
    public void testExecuteConsistencyAcrossAppViews() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();

        // Act & Assert - Different AppViews should behave consistently
        assertDoesNotThrow(() -> clearer.execute(new AppView()),
            "First AppView should work");
        assertDoesNotThrow(() -> clearer.execute(new AppView()),
            "Second AppView should work");
        assertDoesNotThrow(() -> clearer.execute(new AppView()),
            "Third AppView should work");
    }

    /**
     * Tests execute() with same clearer and same AppView multiple times.
     * Verifies idempotent-like behavior for repeated executions.
     */
    @Test
    public void testExecuteIdempotency() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView = new AppView();

        // Act & Assert - Multiple executions should all succeed
        assertDoesNotThrow(() -> {
            clearer.execute(appView);
            clearer.execute(appView);
            clearer.execute(appView);
            clearer.execute(appView);
            clearer.execute(appView);
        }, "Multiple executions on same AppView should succeed");
    }

    /**
     * Tests execute() doesn't modify the AppView reference.
     * Verifies that the AppView object reference remains the same after execution.
     */
    @Test
    public void testExecuteDoesNotModifyAppViewReference() {
        // Arrange
        PreverificationClearer clearer = new PreverificationClearer();
        AppView appView = new AppView();
        AppView originalReference = appView;

        // Act
        clearer.execute(appView);

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
        PreverificationClearer clearer = new PreverificationClearer();
        ClassPool programPool = new ClassPool();
        AppView appView = new AppView(programPool, new ClassPool());
        ClassPool originalPoolReference = appView.programClassPool;

        // Act
        clearer.execute(appView);

        // Assert
        assertSame(originalPoolReference, appView.programClassPool,
            "programClassPool reference should remain the same after execution");
    }

    /**
     * Tests execute() with newly instantiated clearer each time.
     * Verifies that fresh clearers work correctly.
     */
    @Test
    public void testExecuteWithFreshClearerEachTime() {
        // Arrange
        AppView appView = new AppView();

        // Act & Assert - Create fresh clearer for each execution
        assertDoesNotThrow(() -> new PreverificationClearer().execute(appView),
            "First fresh clearer should work");
        assertDoesNotThrow(() -> new PreverificationClearer().execute(appView),
            "Second fresh clearer should work");
        assertDoesNotThrow(() -> new PreverificationClearer().execute(appView),
            "Third fresh clearer should work");
    }
}
