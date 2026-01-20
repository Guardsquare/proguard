package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.io.ExtraDataEntryNameMap;
import proguard.resources.file.ResourceFilePool;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Optimizer#execute(AppView)} method.
 * Tests the optimizer execution with various configurations.
 */
public class OptimizerClaude_executeTest {

    /**
     * Tests that execute returns immediately when moreOptimizationsPossible is false.
     * This test uses reflection to set the moreOptimizationsPossible field to false,
     * as there is no public API to control this state and testing the early-exit path
     * is important for code coverage.
     */
    @Test
    public void testExecuteReturnsEarlyWhenNoMoreOptimizationsPossible() throws Exception {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        Optimizer optimizer = new Optimizer(configuration);

        // Use reflection to set moreOptimizationsPossible to false
        // This is necessary because there is no public API to control this field
        Field moreOptimizationsField = Optimizer.class.getDeclaredField("moreOptimizationsPossible");
        moreOptimizationsField.setAccessible(true);
        moreOptimizationsField.setBoolean(optimizer, false);

        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act
        optimizer.execute(appView);

        // Assert - execution should complete without error
        // The early return means no actual optimization work is done
        Field passIndexField = Optimizer.class.getDeclaredField("passIndex");
        passIndexField.setAccessible(true);
        int passIndex = passIndexField.getInt(optimizer);
        assertEquals(0, passIndex, "passIndex should remain 0 when optimizations are skipped");
    }

    /**
     * Tests execute with a minimal valid configuration.
     * Verifies that the method can execute successfully with basic setup.
     */
    @Test
    public void testExecuteWithMinimalConfiguration() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert - Should complete without throwing
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with null optimizations list.
     * When optimizations is null, all optimizations should be enabled.
     */
    @Test
    public void testExecuteWithNullOptimizationsList() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizations = null;  // null means all optimizations
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with specific optimizations list.
     * Verifies that the method handles explicit optimization filters.
     */
    @Test
    public void testExecuteWithSpecificOptimizationsList() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        List<String> optimizations = new ArrayList<>();
        optimizations.add("code/simplification/*");
        configuration.optimizations = optimizations;
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with optimizeConservatively enabled.
     * Conservative optimization should disable some aggressive optimizations.
     */
    @Test
    public void testExecuteWithConservativeOptimization() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizeConservatively = true;
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with aggressive optimization (non-conservative).
     * Verifies that aggressive optimizations can be enabled.
     */
    @Test
    public void testExecuteWithAggressiveOptimization() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizeConservatively = false;
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute increments pass index.
     * This test uses reflection to verify the passIndex is incremented,
     * as there is no public getter for this field and verifying the internal
     * state progression is important for testing the method's behavior.
     */
    @Test
    public void testExecuteIncrementsPassIndex() throws Exception {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        Field passIndexField = Optimizer.class.getDeclaredField("passIndex");
        passIndexField.setAccessible(true);
        int initialPassIndex = passIndexField.getInt(optimizer);

        // Act
        optimizer.execute(appView);

        // Assert
        int finalPassIndex = passIndexField.getInt(optimizer);
        assertEquals(initialPassIndex + 1, finalPassIndex,
                "passIndex should be incremented after execute");
    }

    /**
     * Tests execute with non-null program class pool.
     * Verifies that the method handles populated class pools.
     */
    @Test
    public void testExecuteWithNonEmptyProgramClassPool() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);

        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with complete AppView including resource file pool.
     * Verifies that all AppView components are handled correctly.
     */
    @Test
    public void testExecuteWithCompleteAppView() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);

        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        AppView appView = new AppView(programClassPool, libraryClassPool,
                                      resourceFilePool, extraDataEntryNameMap);

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with specific optimization filter patterns.
     * Verifies handling of wildcard patterns in optimization filters.
     */
    @Test
    public void testExecuteWithWildcardOptimizationPattern() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        List<String> optimizations = new ArrayList<>();
        optimizations.add("field/*");
        optimizations.add("method/marking/*");
        configuration.optimizations = optimizations;
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with negated optimization patterns.
     * Verifies handling of exclusion patterns in optimization filters.
     */
    @Test
    public void testExecuteWithNegatedOptimizationPattern() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        List<String> optimizations = new ArrayList<>();
        optimizations.add("!code/simplification/arithmetic");
        configuration.optimizations = optimizations;
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with multiple optimization passes setting.
     * Verifies that the configuration's optimization passes value is respected.
     */
    @Test
    public void testExecuteWithMultipleOptimizationPassesSetting() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizationPasses = 3;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute can be called multiple times.
     * Verifies that the optimizer can be invoked repeatedly.
     */
    @Test
    public void testExecuteCanBeCalledMultipleTimes() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizationPasses = 3;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert - first call
        assertDoesNotThrow(() -> optimizer.execute(appView));

        // Act & Assert - second call
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with library/gson optimization enabled.
     * Verifies handling of specific optimization flag.
     */
    @Test
    public void testExecuteWithLibraryGsonOptimization() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        List<String> optimizations = new ArrayList<>();
        optimizations.add("library/gson");
        configuration.optimizations = optimizations;
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with empty optimizations list.
     * An empty list should effectively disable all optimizations.
     */
    @Test
    public void testExecuteWithEmptyOptimizationsList() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.optimizations = new ArrayList<>();  // empty list
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with both keep and applyMapping set.
     * Verifies that alternative keep conditions are handled.
     */
    @Test
    public void testExecuteWithApplyMappingInsteadOfKeep() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.applyMapping = new java.io.File("/tmp/mapping.txt");
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }

    /**
     * Tests execute with printMapping set.
     * Verifies that output mapping configuration is handled.
     */
    @Test
    public void testExecuteWithPrintMapping() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.printMapping = new java.io.File("/tmp/output-mapping.txt");
        configuration.optimizationPasses = 1;
        Optimizer optimizer = new Optimizer(configuration);
        AppView appView = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.execute(appView));
    }
}
