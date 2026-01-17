package proguard.configuration;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.classfile.ClassPool;
import proguard.io.ExtraDataEntryNameMap;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLoggingAdder}.
 * Tests the constructor and execute method of the ConfigurationLoggingAdder class.
 */
public class ConfigurationLoggingAdderClaudeTest {

    /**
     * Tests the constructor of ConfigurationLoggingAdder.
     * Verifies that the object can be instantiated successfully.
     */
    @Test
    public void testConstructor() {
        // Act
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();

        // Assert
        assertNotNull(adder, "ConfigurationLoggingAdder should be instantiated successfully");
    }

    /**
     * Tests the execute method with a minimal AppView containing empty class pools.
     * Verifies that the method handles empty class pools without throwing exceptions.
     */
    @Test
    public void testExecuteWithEmptyClassPools() throws IOException {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> adder.execute(appView),
            "execute should handle empty class pools without throwing exceptions");
    }

    /**
     * Tests the execute method with an AppView containing empty program and library class pools.
     * Verifies that the ConfigurationLogger classes are loaded into the program class pool.
     */
    @Test
    public void testExecuteLoadsConfigurationLoggerClasses() throws IOException {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act
        adder.execute(appView);

        // Assert - Check that ConfigurationLogger classes were loaded
        assertNotNull(programClassPool.getClass("proguard/configuration/ConfigurationLogger"),
            "ConfigurationLogger should be loaded into program class pool");
        assertNotNull(programClassPool.getClass("proguard/configuration/ConfigurationLogger$ClassInfo"),
            "ConfigurationLogger$ClassInfo should be loaded into program class pool");
        assertNotNull(programClassPool.getClass("proguard/configuration/ConfigurationLogger$MemberInfo"),
            "ConfigurationLogger$MemberInfo should be loaded into program class pool");
    }

    /**
     * Tests the execute method with a fully initialized AppView.
     * Verifies that the method completes successfully with all components initialized.
     */
    @Test
    public void testExecuteWithFullyInitializedAppView() throws IOException {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        AppView appView = new AppView(programClassPool, libraryClassPool,
            new proguard.resources.file.ResourceFilePool(), extraDataEntryNameMap);

        // Act & Assert
        assertDoesNotThrow(() -> adder.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    /**
     * Tests the execute method with null AppView.
     * Verifies that the method throws an appropriate exception.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> adder.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Tests the execute method multiple times on the same AppView.
     * Verifies that calling execute multiple times doesn't cause issues.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() throws IOException {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act
        adder.execute(appView);

        // Assert - Second execution should also complete successfully
        assertDoesNotThrow(() -> adder.execute(appView),
            "execute should handle being called multiple times on the same AppView");
    }

    /**
     * Tests that the execute method properly initializes the ConfigurationLogger class.
     * Verifies that after execution, the ConfigurationLogger class is available and properly initialized.
     */
    @Test
    public void testExecuteInitializesConfigurationLogger() throws IOException {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act
        adder.execute(appView);

        // Assert
        assertNotNull(programClassPool.getClass("proguard/configuration/ConfigurationLogger"),
            "ConfigurationLogger class should be present in program class pool after execution");
    }

    /**
     * Tests the execute method with an AppView that has a library class pool with some classes.
     * Verifies that the method can handle AppView with library classes.
     */
    @Test
    public void testExecuteWithLibraryClasses() throws IOException {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        // Library class pool would typically contain standard library classes
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> adder.execute(appView),
            "execute should handle AppView with library class pool");
    }

    /**
     * Tests that ConfigurationLoggingAdder is a Pass implementation.
     * Verifies that the class implements the Pass interface.
     */
    @Test
    public void testConfigurationLoggingAdderIsPass() {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();

        // Assert
        assertTrue(adder instanceof proguard.pass.Pass,
            "ConfigurationLoggingAdder should implement Pass interface");
    }

    /**
     * Tests the execute method to ensure the ConfigurationLogger inner classes are loaded.
     * Verifies that both ClassInfo and MemberInfo inner classes are properly loaded.
     */
    @Test
    public void testExecuteLoadsInnerClasses() throws IOException {
        // Arrange
        ConfigurationLoggingAdder adder = new ConfigurationLoggingAdder();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act
        adder.execute(appView);

        // Assert - Verify all three required classes are loaded
        int loadedClassCount = 0;
        if (programClassPool.getClass("proguard/configuration/ConfigurationLogger") != null) {
            loadedClassCount++;
        }
        if (programClassPool.getClass("proguard/configuration/ConfigurationLogger$ClassInfo") != null) {
            loadedClassCount++;
        }
        if (programClassPool.getClass("proguard/configuration/ConfigurationLogger$MemberInfo") != null) {
            loadedClassCount++;
        }

        assertEquals(3, loadedClassCount,
            "All three ConfigurationLogger classes should be loaded into the program class pool");
    }
}
