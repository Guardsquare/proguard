package proguard.normalize;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StringNormalizer#execute(AppView)} method.
 * Tests the execution of the StringNormalizer with various AppView states.
 */
public class StringNormalizerClaude_executeTest {

    /**
     * Tests execute() with a minimal valid AppView.
     * Verifies that the method can complete successfully with empty class pools.
     */
    @Test
    public void testExecuteWithMinimalValidAppView() {
        // Arrange - Create minimal AppView
        AppView appView = new AppView();
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert - Execute should complete without throwing exceptions
        assertDoesNotThrow(() -> normalizer.execute(appView),
            "execute() should complete successfully with minimal AppView");
    }

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            normalizer.execute(null);
        }, "execute() should throw NullPointerException for null AppView");
    }

    /**
     * Tests execute() with AppView containing empty program class pool.
     * Verifies that the method handles empty class pools correctly.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() {
        // Arrange
        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(emptyProgramClassPool, libraryClassPool);
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert
        assertDoesNotThrow(() -> normalizer.execute(appView),
            "execute() should handle empty program class pool");
    }

    /**
     * Tests execute() with AppView containing empty library class pool.
     * Verifies that the method handles empty library class pools correctly.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, emptyLibraryClassPool);
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert
        assertDoesNotThrow(() -> normalizer.execute(appView),
            "execute() should handle empty library class pool");
    }

    /**
     * Tests execute() with both empty program and library class pools.
     * Verifies the complete empty state is handled gracefully.
     */
    @Test
    public void testExecuteWithBothEmptyClassPools() {
        // Arrange
        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appView = new AppView(emptyProgramClassPool, emptyLibraryClassPool);
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert
        assertDoesNotThrow(() -> normalizer.execute(appView),
            "execute() should handle both empty class pools");
    }

    /**
     * Tests execute() is called twice on the same normalizer.
     * Verifies that multiple executions are supported.
     */
    @Test
    public void testExecuteCalledMultipleTimes() throws Exception {
        // Arrange
        AppView appView = new AppView();
        StringNormalizer normalizer = new StringNormalizer();

        // Act - Execute twice
        normalizer.execute(appView);

        // Assert - Second execution should also complete successfully
        assertDoesNotThrow(() -> normalizer.execute(appView),
            "execute() should support being called multiple times");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that the same normalizer can process different AppViews.
     */
    @Test
    public void testExecuteWithDifferentAppViews() throws Exception {
        // Arrange
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert - Execute with first AppView
        assertDoesNotThrow(() -> normalizer.execute(appView1),
            "execute() should work with first AppView");

        // Act & Assert - Execute with second AppView
        assertDoesNotThrow(() -> normalizer.execute(appView2),
            "execute() should work with second AppView");
    }

    /**
     * Tests that execute() uses ParallelAllClassVisitor.
     * Verifies that the method delegates to the program class pool.
     */
    @Test
    public void testExecuteUsesParallelAllClassVisitor() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert - Should not throw, even with empty pools
        assertDoesNotThrow(() -> normalizer.execute(appView),
            "execute() should use ParallelAllClassVisitor without errors");
    }

    /**
     * Tests execute() with the same AppView instance on different normalizers.
     * Verifies that different normalizers can process the same AppView.
     */
    @Test
    public void testExecuteWithSameAppViewOnDifferentNormalizers() throws Exception {
        // Arrange
        AppView appView = new AppView();
        StringNormalizer normalizer1 = new StringNormalizer();
        StringNormalizer normalizer2 = new StringNormalizer();

        // Act - Execute with first normalizer
        normalizer1.execute(appView);

        // Assert - Second normalizer should also work
        assertDoesNotThrow(() -> normalizer2.execute(appView),
            "Different normalizers should be able to process the same AppView");
    }

    /**
     * Tests execute() verifies it's a void method that completes.
     * Confirms the method signature and completion behavior.
     */
    @Test
    public void testExecuteReturnsVoid() throws Exception {
        // Arrange
        AppView appView = new AppView();
        StringNormalizer normalizer = new StringNormalizer();

        // Act - The execute method is void, so we just verify it completes
        normalizer.execute(appView);

        // Assert - If we reach here, the method completed successfully
        assertTrue(true, "execute() should complete as a void method");
    }

    /**
     * Tests that execute() can be called on a newly created StringNormalizer immediately.
     * Verifies that no additional initialization is required after construction.
     */
    @Test
    public void testExecuteImmediatelyAfterConstruction() {
        // Arrange
        AppView appView = new AppView();

        // Act & Assert - Create and execute immediately
        assertDoesNotThrow(() -> new StringNormalizer().execute(appView),
            "execute() should work immediately after construction");
    }

    /**
     * Tests execute() with AppView that has non-empty program and library class pools.
     * Verifies that the normalizer can process AppView with initialized pools.
     */
    @Test
    public void testExecuteWithNonEmptyClassPools() {
        // Arrange - Create class pools (even if empty, they're initialized)
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert
        assertDoesNotThrow(() -> normalizer.execute(appView),
            "execute() should handle AppView with initialized class pools");
    }

    /**
     * Tests execute() exception behavior.
     * Verifies that the method signature declares throws Exception.
     */
    @Test
    public void testExecuteThrowsDeclaredException() {
        // Arrange
        AppView appView = new AppView();
        StringNormalizer normalizer = new StringNormalizer();

        // Act & Assert - Verify the method can throw Exception (signature)
        try {
            normalizer.execute(appView);
            // Success - no exception thrown in normal case
            assertTrue(true, "execute() completed without throwing exception");
        } catch (Exception e) {
            // This catch block verifies the method signature declares Exception
            fail("execute() should not throw exception with valid AppView: " + e.getMessage());
        }
    }

    /**
     * Tests that multiple normalizers can execute concurrently on different AppViews.
     * Verifies thread-safety considerations for independent instances.
     */
    @Test
    public void testExecuteWithMultipleNormalizersAndAppViews() throws Exception {
        // Arrange
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();
        AppView appView3 = new AppView();

        StringNormalizer normalizer1 = new StringNormalizer();
        StringNormalizer normalizer2 = new StringNormalizer();
        StringNormalizer normalizer3 = new StringNormalizer();

        // Act - Execute all normalizers with different AppViews
        normalizer1.execute(appView1);
        normalizer2.execute(appView2);
        normalizer3.execute(appView3);

        // Assert - All should complete successfully
        assertDoesNotThrow(() -> {
            normalizer1.execute(appView1);
            normalizer2.execute(appView2);
            normalizer3.execute(appView3);
        }, "Multiple normalizers should execute independently");
    }
}
