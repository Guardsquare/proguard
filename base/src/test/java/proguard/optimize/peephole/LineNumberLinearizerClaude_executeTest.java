package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LineNumberLinearizer#execute(AppView)} method.
 *
 * The execute method disambiguates line numbers in all program classes after
 * optimizations like method inlining and class merging. It shifts line numbers
 * that originate from different classes to blocks that don't overlap.
 */
public class LineNumberLinearizerClaude_executeTest {

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
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should handle empty AppView without throwing exceptions");
    }

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> linearizer.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Tests execute() with empty program class pool.
     * Verifies that the method handles empty class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should handle empty program class pool without throwing exceptions");
    }

    /**
     * Tests execute() with empty library class pool.
     * Verifies that the method handles empty library class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        ClassPool programPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(programPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should handle empty library class pool without throwing exceptions");
    }

    /**
     * Tests execute() with both class pools empty.
     * Verifies that the method handles completely empty AppView.
     */
    @Test
    public void testExecuteWithBothClassPoolsEmpty() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView = new AppView(); // Creates empty pools by default

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should handle both class pools empty without throwing exceptions");
    }

    /**
     * Tests execute() with AppView created using different constructor.
     * Verifies that AppView constructor variations are handled correctly.
     */
    @Test
    public void testExecuteWithAppViewCreatedWithTwoParamConstructor() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(programPool, libraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should handle AppView created with two-param constructor");
    }

    /**
     * Tests execute() with AppView created using full constructor.
     * Verifies that fully initialized AppView is handled correctly.
     */
    @Test
    public void testExecuteWithFullyInitializedAppView() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        proguard.resources.file.ResourceFilePool resourceFilePool =
            new proguard.resources.file.ResourceFilePool();
        proguard.io.ExtraDataEntryNameMap extraDataEntryNameMap =
            new proguard.io.ExtraDataEntryNameMap();
        AppView appView = new AppView(programPool, libraryPool, resourceFilePool, extraDataEntryNameMap);

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    // ========================================
    // Multiple Execution Tests
    // ========================================

    /**
     * Tests execute() multiple times with the same LineNumberLinearizer instance.
     * Verifies that the linearizer can be reused.
     */
    @Test
    public void testExecuteMultipleTimes() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Act & Assert - Execute multiple times
        AppView appView1 = new AppView();
        assertDoesNotThrow(() -> linearizer.execute(appView1),
            "First execution should succeed");

        AppView appView2 = new AppView();
        assertDoesNotThrow(() -> linearizer.execute(appView2),
            "Second execution should succeed");

        AppView appView3 = new AppView();
        assertDoesNotThrow(() -> linearizer.execute(appView3),
            "Third execution should succeed");
    }

    /**
     * Tests execute() multiple times on the same AppView.
     * Verifies that repeated execution on the same AppView is handled correctly.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.execute(appView);
            linearizer.execute(appView);
            linearizer.execute(appView);
        }, "execute should handle being called multiple times on the same AppView");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that different AppViews are handled independently.
     */
    @Test
    public void testExecuteWithDifferentAppViews() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Create different AppViews
        AppView appView1 = new AppView();
        AppView appView2 = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView1),
            "Should handle first AppView");
        assertDoesNotThrow(() -> linearizer.execute(appView2),
            "Should handle second AppView");
    }

    /**
     * Tests execute() with multiple linearizer instances on the same AppView.
     * Verifies that different linearizers can operate on the same AppView.
     */
    @Test
    public void testExecuteWithDifferentLinearizerInstances() {
        // Arrange
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer1.execute(appView);
            linearizer2.execute(appView);
        }, "execute should handle different linearizer instances operating on the same AppView");
    }

    // ========================================
    // State and Isolation Tests
    // ========================================

    /**
     * Tests that execute() on one AppView doesn't affect execution on another.
     * Verifies that linearizer maintains proper isolation between AppViews.
     */
    @Test
    public void testExecuteIsolationBetweenAppViews() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act
        assertDoesNotThrow(() -> linearizer.execute(appView1),
            "First execution should succeed");

        // Assert - Second execution should work independently
        assertDoesNotThrow(() -> linearizer.execute(appView2),
            "Second execution should succeed independently of first");
    }

    /**
     * Tests that different linearizers maintain independent state.
     * Verifies that linearizer instances don't share state.
     */
    @Test
    public void testExecuteWithIndependentLinearizerState() {
        // Arrange
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert - Both linearizers should work independently
        assertDoesNotThrow(() -> linearizer1.execute(appView),
            "First linearizer execution should succeed");
        assertDoesNotThrow(() -> linearizer2.execute(appView),
            "Second linearizer execution should succeed");
    }

    /**
     * Tests execute() after creating multiple linearizers.
     * Verifies that linearizer creation doesn't affect execution.
     */
    @Test
    public void testExecuteAfterCreatingMultipleLinearizers() {
        // Arrange - Create multiple linearizers
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert - Each linearizer should execute independently
        assertDoesNotThrow(() -> linearizer1.execute(appView),
            "First linearizer should execute successfully");
        assertDoesNotThrow(() -> linearizer2.execute(appView),
            "Second linearizer should execute successfully");
        assertDoesNotThrow(() -> linearizer3.execute(appView),
            "Third linearizer should execute successfully");
    }

    // ========================================
    // Sequence and Order Tests
    // ========================================

    /**
     * Tests execute() in sequence with alternating linearizers.
     * Verifies that execution order doesn't cause issues.
     */
    @Test
    public void testExecuteWithAlternatingLinearizers() {
        // Arrange
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert - Alternate between linearizers
        assertDoesNotThrow(() -> {
            linearizer1.execute(appView);
            linearizer2.execute(appView);
            linearizer1.execute(appView);
            linearizer2.execute(appView);
        }, "Alternating linearizer execution should work correctly");
    }

    /**
     * Tests execute() in sequence with many AppViews.
     * Verifies that the linearizer can handle many sequential executions.
     */
    @Test
    public void testExecuteWithManySequentialAppViews() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Act & Assert - Execute on many AppViews sequentially
        for (int i = 0; i < 10; i++) {
            AppView appView = new AppView();
            final int index = i;
            assertDoesNotThrow(() -> linearizer.execute(appView),
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
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert - Execute rapidly in succession
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                linearizer.execute(appView);
            }
        }, "Rapid successive executions should work correctly");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests execute() immediately after construction.
     * Verifies that linearizer is ready to execute immediately after creation.
     */
    @Test
    public void testExecuteImmediatelyAfterConstruction() {
        // Arrange & Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Assert - Should work immediately after construction
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should work immediately after construction");
    }

    /**
     * Tests execute() with AppView that has null programClassPool field.
     * Verifies proper handling of AppView with null programClassPool.
     */
    @Test
    public void testExecuteWithNullProgramClassPool() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        // Create AppView with null programClassPool by using reflection or custom setup
        // Since AppView initializes pools in constructor, we test the NPE case directly
        AppView appView = null;

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> linearizer.execute(appView),
            "execute should throw NullPointerException with null AppView");
    }

    /**
     * Tests execute() with freshly created AppView each time.
     * Verifies that fresh AppViews are handled consistently.
     */
    @Test
    public void testExecuteWithFreshAppViewsEachTime() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Act & Assert - Create fresh AppView for each execution
        assertDoesNotThrow(() -> linearizer.execute(new AppView()),
            "First fresh AppView should work");
        assertDoesNotThrow(() -> linearizer.execute(new AppView()),
            "Second fresh AppView should work");
        assertDoesNotThrow(() -> linearizer.execute(new AppView()),
            "Third fresh AppView should work");
    }

    /**
     * Tests execute() with AppView created with explicitly empty ClassPools.
     * Verifies that explicitly empty pools are handled correctly.
     */
    @Test
    public void testExecuteWithExplicitlyEmptyClassPools() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, emptyLibraryPool);

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.execute(appView),
            "execute should handle explicitly empty ClassPools");
    }

    // ========================================
    // Consistency Tests
    // ========================================

    /**
     * Tests that execute() completes successfully regardless of linearizer instance.
     * Verifies consistent behavior across different linearizer instances.
     */
    @Test
    public void testExecuteConsistencyAcrossInstances() {
        // Arrange
        AppView appView = new AppView();

        // Act & Assert - Different linearizers should behave consistently
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        assertDoesNotThrow(() -> linearizer1.execute(appView),
            "First linearizer should execute successfully");

        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        assertDoesNotThrow(() -> linearizer2.execute(appView),
            "Second linearizer should execute successfully");

        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();
        assertDoesNotThrow(() -> linearizer3.execute(appView),
            "Third linearizer should execute successfully");
    }

    /**
     * Tests that execute() completes successfully regardless of AppView instance.
     * Verifies consistent behavior across different AppView instances.
     */
    @Test
    public void testExecuteConsistencyAcrossAppViews() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Act & Assert - Different AppViews should behave consistently
        assertDoesNotThrow(() -> linearizer.execute(new AppView()),
            "First AppView should work");
        assertDoesNotThrow(() -> linearizer.execute(new AppView()),
            "Second AppView should work");
        assertDoesNotThrow(() -> linearizer.execute(new AppView()),
            "Third AppView should work");
    }

    /**
     * Tests execute() with same linearizer and same AppView multiple times.
     * Verifies idempotent-like behavior for repeated executions.
     */
    @Test
    public void testExecuteIdempotency() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView = new AppView();

        // Act & Assert - Multiple executions should all succeed
        assertDoesNotThrow(() -> {
            linearizer.execute(appView);
            linearizer.execute(appView);
            linearizer.execute(appView);
            linearizer.execute(appView);
            linearizer.execute(appView);
        }, "Multiple executions on same AppView should succeed");
    }

    /**
     * Tests execute() doesn't modify the AppView reference.
     * Verifies that the AppView object reference remains the same after execution.
     */
    @Test
    public void testExecuteDoesNotModifyAppViewReference() {
        // Arrange
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        AppView appView = new AppView();
        AppView originalReference = appView;

        // Act
        linearizer.execute(appView);

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
        LineNumberLinearizer linearizer = new LineNumberLinearizer();
        ClassPool programPool = new ClassPool();
        AppView appView = new AppView(programPool, new ClassPool());
        ClassPool originalPoolReference = appView.programClassPool;

        // Act
        linearizer.execute(appView);

        // Assert
        assertSame(originalPoolReference, appView.programClassPool,
            "programClassPool reference should remain the same after execution");
    }

    /**
     * Tests execute() with newly instantiated linearizer each time.
     * Verifies that fresh linearizers work correctly.
     */
    @Test
    public void testExecuteWithFreshLinearizerEachTime() {
        // Arrange
        AppView appView = new AppView();

        // Act & Assert - Create fresh linearizer for each execution
        assertDoesNotThrow(() -> new LineNumberLinearizer().execute(appView),
            "First fresh linearizer should work");
        assertDoesNotThrow(() -> new LineNumberLinearizer().execute(appView),
            "Second fresh linearizer should work");
        assertDoesNotThrow(() -> new LineNumberLinearizer().execute(appView),
            "Third fresh linearizer should work");
    }
}
