package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.classfile.ClassPool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SeedPrinter#execute(AppView)} method.
 * Tests the execution of SeedPrinter with various configurations and AppView states.
 */
public class SeedPrinterClaude_executeTest {

    @TempDir
    Path tempDir;

    /**
     * Tests execute() throws IOException when configuration.keep is null.
     * This is a requirement per line 64-67 of SeedPrinter.java.
     */
    @Test
    public void testExecuteWithNullKeep() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = null; // No keep rules
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert - Should throw IOException
        IOException exception = assertThrows(IOException.class, () -> {
            seedPrinter.execute(appView);
        }, "execute() should throw IOException when configuration.keep is null");

        assertTrue(exception.getMessage().contains("keep"),
                "Exception message should mention 'keep'");
        assertTrue(exception.getMessage().contains("printseeds"),
                "Exception message should mention 'printseeds'");
    }

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.printSeeds = Configuration.STD_OUT;
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            seedPrinter.execute(null);
        }, "execute() should throw NullPointerException for null AppView");
    }

    /**
     * Tests execute() with minimal valid AppView and Configuration with empty keep list.
     * Verifies that the method can complete successfully with empty class pools and empty keep rules.
     */
    @Test
    public void testExecuteWithEmptyKeepList() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>(); // Empty list, not null
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert - Execute should complete without throwing exceptions
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should complete successfully with empty keep list");
    }

    /**
     * Tests execute() with a single keep rule.
     * Verifies that the method processes keep rules correctly.
     */
    @Test
    public void testExecuteWithSingleKeepRule() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        // Create a simple keep rule
        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = "com/example/Test";

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should complete successfully with single keep rule");
    }

    /**
     * Tests execute() with multiple keep rules.
     * Verifies that the method processes multiple keep specifications.
     */
    @Test
    public void testExecuteWithMultipleKeepRules() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        // Create multiple keep rules
        for (int i = 0; i < 3; i++) {
            ClassSpecification classSpec = new ClassSpecification();
            classSpec.className = "com/example/Test" + i;

            KeepClassSpecification keepSpec = new KeepClassSpecification(
                    true, false, false, false, false, false, false, false, null, classSpec
            );
            keepList.add(keepSpec);
        }

        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should complete successfully with multiple keep rules");
    }

    /**
     * Tests execute() with printSeeds pointing to a file.
     * Verifies that output can be written to a file instead of stdout.
     */
    @Test
    public void testExecuteWithFileOutput() throws IOException {
        // Arrange
        File outputFile = tempDir.resolve("seeds.txt").toFile();

        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.printSeeds = outputFile;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act
        seedPrinter.execute(appView);

        // Assert - File should be created
        assertTrue(outputFile.exists(), "Output file should be created");
    }

    /**
     * Tests execute() with empty program class pool.
     * Verifies that the method handles empty class pools correctly.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.printSeeds = Configuration.STD_OUT;

        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(emptyProgramClassPool, libraryClassPool);

        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle empty program class pool");
    }

    /**
     * Tests execute() with empty library class pool.
     * Verifies that the method handles empty library class pools correctly.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.printSeeds = Configuration.STD_OUT;

        ClassPool programClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, emptyLibraryClassPool);

        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle empty library class pool");
    }

    /**
     * Tests execute() with both empty program and library class pools.
     * Verifies the complete empty state is handled gracefully.
     */
    @Test
    public void testExecuteWithBothEmptyClassPools() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.printSeeds = Configuration.STD_OUT;

        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appView = new AppView(emptyProgramClassPool, emptyLibraryClassPool);

        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle both empty class pools");
    }

    /**
     * Tests execute() is called twice on the same printer.
     * Verifies that multiple executions are supported.
     */
    @Test
    public void testExecuteCalledMultipleTimes() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act - Execute twice
        seedPrinter.execute(appView);

        // Assert - Second execution should also complete successfully
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should support being called multiple times");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that the same printer can process different AppViews.
     */
    @Test
    public void testExecuteWithDifferentAppViews() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView1 = new AppView();
        AppView appView2 = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert - Execute with first AppView
        assertDoesNotThrow(() -> seedPrinter.execute(appView1),
                "execute() should work with first AppView");

        // Act & Assert - Execute with second AppView
        assertDoesNotThrow(() -> seedPrinter.execute(appView2),
                "execute() should work with second AppView");
    }

    /**
     * Tests execute() with keep rules that mark class members.
     * Verifies that keep specifications with member marking work correctly.
     */
    @Test
    public void testExecuteWithKeepRulesMarkingMembers() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = "com/example/Test";

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                true,  // markClassMembers - now enabled
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle keep rules that mark class members");
    }

    /**
     * Tests execute() with keep rules allowing shrinking.
     * Verifies that keep specifications with allowShrinking work correctly.
     */
    @Test
    public void testExecuteWithKeepRulesAllowingShrinking() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = "com/example/Test";

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                true,  // allowShrinking - now enabled
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle keep rules allowing shrinking");
    }

    /**
     * Tests execute() with keep rules allowing optimization.
     * Verifies that keep specifications with allowOptimization work correctly.
     */
    @Test
    public void testExecuteWithKeepRulesAllowingOptimization() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = "com/example/Test";

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                true,  // allowOptimization - now enabled
                false, // allowObfuscation
                null,  // condition
                classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle keep rules allowing optimization");
    }

    /**
     * Tests execute() with keep rules allowing obfuscation.
     * Verifies that keep specifications with allowObfuscation work correctly.
     */
    @Test
    public void testExecuteWithKeepRulesAllowingObfuscation() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = "com/example/Test";

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                true,  // allowObfuscation - now enabled
                null,  // condition
                classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle keep rules allowing obfuscation");
    }

    /**
     * Tests execute() with a complex keep rule configuration.
     * Verifies that all keep specification flags can be combined.
     */
    @Test
    public void testExecuteWithComplexKeepRule() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = "com/example/Test";

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                true,  // markClassMembers
                true,  // markConditionally
                true,  // markDescriptorClasses
                true,  // markCodeAttributes
                true,  // allowShrinking
                true,  // allowOptimization
                true,  // allowObfuscation
                null,  // condition
                classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle complex keep rules with all flags enabled");
    }

    /**
     * Tests execute() with wildcard class name in keep rule.
     * Verifies that wildcard patterns are handled correctly.
     */
    @Test
    public void testExecuteWithWildcardKeepRule() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = "com/example/**"; // Wildcard pattern

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false, null, classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle wildcard patterns in keep rules");
    }

    /**
     * Tests execute() with null class name in keep rule.
     * Verifies that null class names (matching all classes) are handled.
     */
    @Test
    public void testExecuteWithNullClassNameInKeepRule() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        List<KeepClassSpecification> keepList = new ArrayList<>();

        ClassSpecification classSpec = new ClassSpecification();
        classSpec.className = null; // Matches all classes

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false, null, classSpec
        );

        keepList.add(keepSpec);
        configuration.keep = keepList;
        configuration.printSeeds = Configuration.STD_OUT;

        AppView appView = new AppView();
        SeedPrinter seedPrinter = new SeedPrinter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> seedPrinter.execute(appView),
                "execute() should handle null class name in keep rules");
    }
}
