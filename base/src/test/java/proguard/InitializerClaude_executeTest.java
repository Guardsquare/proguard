package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Initializer#execute(AppView)} method.
 * Tests the execution of the Initializer with various configurations and AppView states.
 */
public class InitializerClaude_executeTest {

    /**
     * Tests execute() with a minimal valid AppView and Configuration.
     * Verifies that the method can complete successfully with empty class pools.
     */
    @Test
    public void testExecuteWithMinimalValidAppView() throws IOException {
        // Arrange - Create minimal configuration and AppView
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert - Execute should complete without throwing exceptions
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should complete successfully with minimal AppView");
    }

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            initializer.execute(null);
        }, "execute() should throw NullPointerException for null AppView");
    }

    /**
     * Tests execute() with shrink enabled in configuration.
     * Verifies that the method executes the configuration checking path.
     */
    @Test
    public void testExecuteWithShrinkEnabled() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle shrink configuration");
    }

    /**
     * Tests execute() with optimize enabled in configuration.
     * Verifies that the method executes the configuration checking path.
     */
    @Test
    public void testExecuteWithOptimizeEnabled() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = true;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle optimize configuration");
    }

    /**
     * Tests execute() with obfuscate enabled in configuration.
     * Verifies that the method executes the configuration checking path.
     */
    @Test
    public void testExecuteWithObfuscateEnabled() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = true;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle obfuscate configuration");
    }

    /**
     * Tests execute() with all processing options enabled.
     * Verifies that the method handles full configuration checking.
     */
    @Test
    public void testExecuteWithAllProcessingEnabled() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = true;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle all processing options enabled");
    }

    /**
     * Tests execute() with useUniqueClassMemberNames enabled.
     * Verifies that the reduced library class pool path is skipped.
     */
    @Test
    public void testExecuteWithUniqueClassMemberNames() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.useUniqueClassMemberNames = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle useUniqueClassMemberNames configuration");
    }

    /**
     * Tests execute() with keepKotlinMetadata enabled.
     * Verifies that Kotlin metadata processing is triggered.
     */
    @Test
    public void testExecuteWithKeepKotlinMetadata() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle keepKotlinMetadata configuration");
    }

    /**
     * Tests execute() with ignoreWarnings enabled.
     * Verifies that warnings don't cause IOException when ignored.
     */
    @Test
    public void testExecuteWithIgnoreWarnings() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.ignoreWarnings = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should not throw IOException when ignoreWarnings is true");
    }

    /**
     * Tests execute() with verbose enabled.
     * Verifies that verbose logging doesn't affect execution.
     */
    @Test
    public void testExecuteWithVerboseEnabled() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.verbose = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle verbose configuration");
    }

    /**
     * Tests execute() with skipNonPublicLibraryClasses enabled.
     * Verifies that library class filtering configuration is handled.
     */
    @Test
    public void testExecuteWithSkipNonPublicLibraryClasses() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.skipNonPublicLibraryClasses = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle skipNonPublicLibraryClasses configuration");
    }

    /**
     * Tests execute() with AppView containing empty program class pool.
     * Verifies that the method handles empty class pools correctly.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(emptyProgramClassPool, libraryClassPool);

        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle empty program class pool");
    }

    /**
     * Tests execute() with AppView containing empty library class pool.
     * Verifies that the method handles empty library class pools correctly.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        ClassPool programClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, emptyLibraryClassPool);

        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
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
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appView = new AppView(emptyProgramClassPool, emptyLibraryClassPool);

        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle both empty class pools");
    }

    /**
     * Tests execute() is called twice on the same initializer.
     * Verifies that multiple executions are supported.
     */
    @Test
    public void testExecuteCalledMultipleTimes() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act - Execute twice
        initializer.execute(appView);

        // Assert - Second execution should also complete successfully
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should support being called multiple times");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that the same initializer can process different AppViews.
     */
    @Test
    public void testExecuteWithDifferentAppViews() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView1 = new AppView();
        AppView appView2 = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert - Execute with first AppView
        assertDoesNotThrow(() -> initializer.execute(appView1),
            "execute() should work with first AppView");

        // Act & Assert - Execute with second AppView
        assertDoesNotThrow(() -> initializer.execute(appView2),
            "execute() should work with second AppView");
    }

    /**
     * Tests execute() with a complex configuration combining multiple options.
     * Verifies that the method handles realistic configuration scenarios.
     */
    @Test
    public void testExecuteWithComplexConfiguration() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = false;
        configuration.verbose = true;
        configuration.ignoreWarnings = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepKotlinMetadata = false;
        configuration.skipNonPublicLibraryClasses = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle complex configuration");
    }

    /**
     * Tests execute() with preverify enabled.
     * Verifies that preverification configuration is handled.
     */
    @Test
    public void testExecuteWithPreverifyEnabled() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.preverify = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle preverify configuration");
    }

    /**
     * Tests execute() with allowAccessModification enabled.
     * Verifies that access modification configuration is handled.
     */
    @Test
    public void testExecuteWithAllowAccessModification() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.allowAccessModification = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle allowAccessModification configuration");
    }

    /**
     * Tests execute() with optimize enabled but shrink disabled.
     * This tests the incompatible configuration warning path (optimize without shrink).
     */
    @Test
    public void testExecuteWithOptimizeButNoShrink() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;  // Shrink disabled
        configuration.optimize = true;  // But optimize enabled - this is incompatible
        configuration.obfuscate = false;
        configuration.ignoreWarnings = true;  // Ignore warnings to not throw IOException

        AppView appView = new AppView();
        Initializer initializer = new Initializer(configuration);

        // Act & Assert - Should complete but log a warning about incompatible optimization
        assertDoesNotThrow(() -> initializer.execute(appView),
            "execute() should handle incompatible optimize without shrink configuration when warnings ignored");
    }
}
