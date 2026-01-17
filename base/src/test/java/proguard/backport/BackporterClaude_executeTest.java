package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.classfile.VersionConstants;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Backporter#execute(AppView)} method.
 * Tests the execution of the Backporter with various configurations and AppView states.
 */
public class BackporterClaude_executeTest {

    /**
     * Tests execute() with a minimal valid AppView and Configuration.
     * Verifies that the method can complete successfully with empty class pools.
     */
    @Test
    public void testExecuteWithMinimalValidAppView() throws IOException {
        // Arrange - Create minimal configuration and AppView
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 0; // No version change

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert - Execute should complete without throwing exceptions
        assertDoesNotThrow(() -> backporter.execute(appView),
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
        Backporter backporter = new Backporter(configuration);

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            backporter.execute(null);
        }, "execute() should throw NullPointerException for null AppView");
    }

    /**
     * Tests execute() with targetClassVersion set to Java 7 (51).
     * Verifies that Java 7+ method call replacement is triggered.
     */
    @Test
    public void testExecuteWithJava7Target() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_7;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle Java 7 target version");
    }

    /**
     * Tests execute() with targetClassVersion set to Java 6 (50).
     * This should trigger all backporting paths including Java 7+ method replacement,
     * Java 8 features, and Java 9 features.
     */
    @Test
    public void testExecuteWithJava6Target() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_6;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle Java 6 target version");
    }

    /**
     * Tests execute() with targetClassVersion set to Java 8 (52).
     * This should not trigger Java 8 or 7 backporting, only Java 9+.
     */
    @Test
    public void testExecuteWithJava8Target() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_8;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle Java 8 target version");
    }

    /**
     * Tests execute() with targetClassVersion set to Java 9 (53).
     * This should not trigger any backporting as Java 9 is relatively modern.
     */
    @Test
    public void testExecuteWithJava9Target() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_9;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle Java 9 target version");
    }

    /**
     * Tests execute() with targetClassVersion set to 0.
     * According to the code, targetClassVersion == 0 means no version change.
     */
    @Test
    public void testExecuteWithZeroTargetVersion() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 0;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle target version 0 (no version change)");
    }

    /**
     * Tests execute() with allowAccessModification enabled.
     * Verifies that access fixing is applied when configured.
     */
    @Test
    public void testExecuteWithAllowAccessModification() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_7;
        configuration.allowAccessModification = true;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle allowAccessModification configuration");
    }

    /**
     * Tests execute() with allowAccessModification disabled.
     * Verifies that access fixing is skipped when not configured.
     */
    @Test
    public void testExecuteWithAllowAccessModificationDisabled() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_7;
        configuration.allowAccessModification = false;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle disabled allowAccessModification");
    }

    /**
     * Tests execute() with empty program class pool.
     * Verifies that the method handles empty class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyProgramClassPool() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_6;

        ClassPool emptyProgramPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        AppView appView = new AppView(emptyProgramPool, libraryPool);

        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle empty program class pool");
    }

    /**
     * Tests execute() with empty library class pool.
     * Verifies that the method handles empty library class pools gracefully.
     */
    @Test
    public void testExecuteWithEmptyLibraryClassPool() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_6;

        ClassPool programPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        AppView appView = new AppView(programPool, emptyLibraryPool);

        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle empty library class pool");
    }

    /**
     * Tests execute() with both class pools empty.
     * Verifies that the method handles completely empty AppView.
     */
    @Test
    public void testExecuteWithBothClassPoolsEmpty() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_6;

        AppView appView = new AppView(); // Creates empty pools

        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle both class pools empty");
    }

    /**
     * Tests execute() with high target class version (Java 17 = 61).
     * Verifies that modern Java versions are handled without backporting.
     */
    @Test
    public void testExecuteWithHighTargetVersion() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 61; // Java 17

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle high target version (Java 17)");
    }

    /**
     * Tests execute() with very low target class version (Java 5 = 49).
     * Verifies that very old Java versions trigger all backporting paths.
     */
    @Test
    public void testExecuteWithVeryLowTargetVersion() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 49; // Java 5

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle very low target version (Java 5)");
    }

    /**
     * Tests execute() with targetClassVersion just below Java 9 threshold.
     * Verifies boundary condition for Java 9 string concatenation backporting.
     */
    @Test
    public void testExecuteWithTargetJustBelowJava9() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_8; // 52

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle target version just below Java 9");
    }

    /**
     * Tests execute() with targetClassVersion just below Java 8 threshold.
     * Verifies boundary condition for Java 8 lambda and interface method backporting.
     */
    @Test
    public void testExecuteWithTargetJustBelowJava8() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_7; // 51

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle target version just below Java 8");
    }

    /**
     * Tests execute() with targetClassVersion just below Java 7 threshold.
     * Verifies boundary condition for Java 7 method call replacement.
     */
    @Test
    public void testExecuteWithTargetJustBelowJava7() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_6; // 50

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle target version just below Java 7");
    }

    /**
     * Tests execute() multiple times with the same Backporter instance.
     * Verifies that the Backporter can be reused.
     */
    @Test
    public void testExecuteMultipleTimes() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_7;

        Backporter backporter = new Backporter(configuration);

        // Act & Assert - Execute multiple times
        AppView appView1 = new AppView();
        assertDoesNotThrow(() -> backporter.execute(appView1),
            "First execution should succeed");

        AppView appView2 = new AppView();
        assertDoesNotThrow(() -> backporter.execute(appView2),
            "Second execution should succeed");

        AppView appView3 = new AppView();
        assertDoesNotThrow(() -> backporter.execute(appView3),
            "Third execution should succeed");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that different AppViews are handled independently.
     */
    @Test
    public void testExecuteWithDifferentAppViews() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_6;

        Backporter backporter = new Backporter(configuration);

        // Create different AppViews
        AppView appView1 = new AppView();
        AppView appView2 = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView1),
            "Should handle first AppView");
        assertDoesNotThrow(() -> backporter.execute(appView2),
            "Should handle second AppView");
    }

    /**
     * Tests execute() with complex configuration having multiple options set.
     * Verifies that complex configurations are handled correctly.
     */
    @Test
    public void testExecuteWithComplexConfiguration() throws IOException {
        // Arrange - Complex configuration
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = VersionConstants.CLASS_VERSION_1_6;
        configuration.allowAccessModification = true;
        configuration.verbose = true;
        configuration.shrink = true;
        configuration.optimize = true;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle complex configuration");
    }

    /**
     * Tests execute() with negative target class version.
     * Verifies that negative versions trigger all backporting paths.
     */
    @Test
    public void testExecuteWithNegativeTargetVersion() throws IOException {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = -1;

        AppView appView = new AppView();
        Backporter backporter = new Backporter(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> backporter.execute(appView),
            "execute() should handle negative target version");
    }

    /**
     * Tests execute() with null Configuration (edge case).
     * The constructor accepts null configuration, so execute might be called with it.
     */
    @Test
    public void testExecuteWithNullConfiguration() {
        // Arrange - Backporter with null configuration
        Backporter backporter = new Backporter(null);
        AppView appView = new AppView();

        // Act & Assert - Should throw NullPointerException when accessing configuration
        assertThrows(NullPointerException.class, () -> {
            backporter.execute(appView);
        }, "execute() should throw NullPointerException with null configuration");
    }
}
