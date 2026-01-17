package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KotlinMetadataAdapter}.
 * Tests all methods to ensure proper functionality of KotlinMetadataAdapter.
 */
public class KotlinMetadataAdapterClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the no-argument constructor.
     * Verifies that the KotlinMetadataAdapter can be instantiated.
     */
    @Test
    public void testConstructor() {
        // Act
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();

        // Assert
        assertNotNull(adapter, "KotlinMetadataAdapter should be instantiated");
    }

    /**
     * Tests that the KotlinMetadataAdapter implements the Pass interface.
     */
    @Test
    public void testImplementsPassInterface() {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();

        // Assert
        assertTrue(adapter instanceof proguard.pass.Pass, "KotlinMetadataAdapter should implement Pass interface");
    }

    // ========== execute() Tests ==========

    /**
     * Tests execute() with an empty AppView.
     * Verifies that execution completes without errors when there are no classes.
     */
    @Test
    public void testExecuteWithEmptyAppView() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();
        AppView appView = new AppView();

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> adapter.execute(appView));
    }

    /**
     * Tests execute() with an AppView containing empty class pools.
     * Verifies that execution completes without errors.
     */
    @Test
    public void testExecuteWithEmptyClassPools() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> adapter.execute(appView));
    }

    /**
     * Tests execute() with an AppView to ensure it processes successfully.
     * Verifies that the method completes without throwing exceptions.
     */
    @Test
    public void testExecuteDoesNotThrowException() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act
        adapter.execute(appView);

        // Assert - If we reach here, no exception was thrown
        assertTrue(true, "execute() should complete without throwing an exception");
    }

    /**
     * Tests execute() can be called multiple times on the same adapter instance.
     * Verifies that the adapter is reusable.
     */
    @Test
    public void testExecuteMultipleTimes() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act & Assert - Should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            adapter.execute(appView1);
            adapter.execute(appView2);
        });
    }

    /**
     * Tests execute() with the same AppView multiple times.
     * Verifies that the adapter can process the same view multiple times.
     */
    @Test
    public void testExecuteSameAppViewMultipleTimes() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();
        AppView appView = new AppView();

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> {
            adapter.execute(appView);
            adapter.execute(appView);
            adapter.execute(appView);
        });
    }

    /**
     * Tests execute() with different adapter instances on the same AppView.
     * Verifies that multiple adapter instances can process the same view.
     */
    @Test
    public void testExecuteWithMultipleAdapterInstances() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter1 = new KotlinMetadataAdapter();
        KotlinMetadataAdapter adapter2 = new KotlinMetadataAdapter();
        AppView appView = new AppView();

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> {
            adapter1.execute(appView);
            adapter2.execute(appView);
        });
    }

    /**
     * Tests that execute() doesn't modify the AppView structure.
     * Verifies that the class pools remain non-null after execution.
     */
    @Test
    public void testExecuteDoesNotModifyAppViewStructure() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act
        adapter.execute(appView);

        // Assert - AppView structure should remain intact
        assertNotNull(appView.programClassPool, "Program class pool should not be null");
        assertNotNull(appView.libraryClassPool, "Library class pool should not be null");
        assertSame(programClassPool, appView.programClassPool, "Program class pool should be the same instance");
        assertSame(libraryClassPool, appView.libraryClassPool, "Library class pool should be the same instance");
    }

    // ========== Integration Tests ==========

    /**
     * Integration test: Create adapter, execute, and verify no exceptions.
     */
    @Test
    public void testCompleteWorkflow() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter = new KotlinMetadataAdapter();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act
        adapter.execute(appView);

        // Assert - Verify the process completed successfully
        assertNotNull(appView.programClassPool);
        assertNotNull(appView.libraryClassPool);
    }

    /**
     * Integration test: Verify multiple adapters can be created and used independently.
     */
    @Test
    public void testMultipleAdaptersIndependence() throws Exception {
        // Arrange
        KotlinMetadataAdapter adapter1 = new KotlinMetadataAdapter();
        KotlinMetadataAdapter adapter2 = new KotlinMetadataAdapter();
        KotlinMetadataAdapter adapter3 = new KotlinMetadataAdapter();
        AppView appView = new AppView();

        // Act & Assert - All adapters should work independently
        assertDoesNotThrow(() -> {
            adapter1.execute(appView);
            adapter2.execute(appView);
            adapter3.execute(appView);
        });
    }
}
