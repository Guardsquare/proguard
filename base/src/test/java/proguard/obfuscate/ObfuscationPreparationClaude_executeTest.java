package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.KeepClassSpecification;
import proguard.classfile.ClassPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ObfuscationPreparation#execute(AppView)} method.
 * Tests the execute method that takes an AppView parameter and prepares it for obfuscation.
 */
public class ObfuscationPreparationClaude_executeTest {

    /**
     * Test that execute throws IOException when configuration has no keep, applyMapping, or printMapping.
     * According to the implementation, at least one of these must be present.
     */
    @Test
    public void testExecuteThrowsIOExceptionWhenNoKeepOptions() {
        // Arrange - Create configuration without keep, applyMapping, or printMapping
        Configuration configuration = new Configuration();
        configuration.keep = null;
        configuration.applyMapping = null;
        configuration.printMapping = null;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert - Verify IOException is thrown
        IOException exception = assertThrows(IOException.class,
            () -> obfuscationPreparation.execute(appView),
            "execute should throw IOException when no keep options are specified");

        assertTrue(exception.getMessage().contains("keep") || exception.getMessage().contains("obfuscation"),
            "Exception message should mention keep options or obfuscation step");
    }

    /**
     * Test that execute succeeds when configuration has keep rules.
     */
    @Test
    public void testExecuteWithKeepRules() {
        // Arrange - Create configuration with keep rules
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert - Verify execute succeeds
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should not throw when keep rules are present");
    }

    /**
     * Test that execute succeeds when configuration has applyMapping set.
     */
    @Test
    public void testExecuteWithApplyMapping() {
        // Arrange - Create configuration with applyMapping
        Configuration configuration = new Configuration();
        configuration.applyMapping = Configuration.STD_OUT;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert - Verify execute succeeds
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should not throw when applyMapping is set");
    }

    /**
     * Test that execute succeeds when configuration has printMapping set.
     */
    @Test
    public void testExecuteWithPrintMapping() {
        // Arrange - Create configuration with printMapping
        Configuration configuration = new Configuration();
        configuration.printMapping = Configuration.STD_OUT;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert - Verify execute succeeds
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should not throw when printMapping is set");
    }

    /**
     * Test that execute succeeds when configuration has both applyMapping and printMapping.
     */
    @Test
    public void testExecuteWithBothApplyAndPrintMapping() {
        // Arrange - Create configuration with both mapping options
        Configuration configuration = new Configuration();
        configuration.applyMapping = Configuration.STD_OUT;
        configuration.printMapping = Configuration.STD_OUT;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert - Verify execute succeeds
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should not throw when both mapping options are set");
    }

    /**
     * Test that execute succeeds when configuration has keep rules and applyMapping.
     */
    @Test
    public void testExecuteWithKeepRulesAndApplyMapping() {
        // Arrange - Create configuration with keep rules and applyMapping
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.applyMapping = Configuration.STD_OUT;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert - Verify execute succeeds
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should not throw when both keep rules and applyMapping are set");
    }

    /**
     * Test that execute handles an empty AppView without throwing exceptions.
     */
    @Test
    public void testExecuteWithEmptyAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle empty AppView without throwing exceptions");
    }

    /**
     * Test that execute throws NullPointerException when AppView is null.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> obfuscationPreparation.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Test that execute handles AppView with empty class pools.
     */
    @Test
    public void testExecuteWithEmptyClassPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle empty class pools without throwing exceptions");
    }

    /**
     * Test that execute can be called multiple times on the same AppView.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscationPreparation.execute(appView);
            obfuscationPreparation.execute(appView);
        }, "execute should handle being called multiple times on the same AppView");
    }

    /**
     * Test that execute can be called by different ObfuscationPreparation instances on the same AppView.
     */
    @Test
    public void testExecuteWithDifferentInstances() {
        // Arrange
        Configuration configuration1 = new Configuration();
        configuration1.keep = new ArrayList<>();
        Configuration configuration2 = new Configuration();
        configuration2.printMapping = Configuration.STD_OUT;

        ObfuscationPreparation obfuscationPreparation1 = new ObfuscationPreparation(configuration1);
        ObfuscationPreparation obfuscationPreparation2 = new ObfuscationPreparation(configuration2);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscationPreparation1.execute(appView);
            obfuscationPreparation2.execute(appView);
        }, "execute should handle different instances operating on the same AppView");
    }

    /**
     * Test that execute with null configuration throws NullPointerException.
     */
    @Test
    public void testExecuteWithNullConfiguration() {
        // Arrange
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(null);
        AppView appView = new AppView();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> obfuscationPreparation.execute(appView),
            "execute should throw NullPointerException when configuration is null");
    }

    /**
     * Test that execute works with obfuscate flag enabled.
     */
    @Test
    public void testExecuteWithObfuscateEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.obfuscate = true;
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle obfuscate enabled without throwing exceptions");
    }

    /**
     * Test that execute works with obfuscate flag disabled.
     */
    @Test
    public void testExecuteWithObfuscateDisabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.obfuscate = false;
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle obfuscate disabled without throwing exceptions");
    }

    /**
     * Test that execute works with a custom File for applyMapping.
     */
    @Test
    public void testExecuteWithCustomApplyMappingFile() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.applyMapping = new File("/tmp/mapping.txt");

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle custom applyMapping file without throwing exceptions");
    }

    /**
     * Test that execute works with a custom File for printMapping.
     */
    @Test
    public void testExecuteWithCustomPrintMappingFile() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.printMapping = new File("/tmp/output-mapping.txt");

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle custom printMapping file without throwing exceptions");
    }

    /**
     * Test that execute works with all three options (keep, applyMapping, printMapping) set.
     */
    @Test
    public void testExecuteWithAllOptionsSet() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.applyMapping = Configuration.STD_OUT;
        configuration.printMapping = Configuration.STD_OUT;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle all options set without throwing exceptions");
    }

    /**
     * Test that execute works with a fully initialized AppView.
     */
    @Test
    public void testExecuteWithFullyInitializedAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();

        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool,
            new proguard.resources.file.ResourceFilePool(), new proguard.io.ExtraDataEntryNameMap());

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    /**
     * Test that execute throws IOException with appropriate message when no options are set.
     */
    @Test
    public void testExecuteIOExceptionMessageContent() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = null;
        configuration.applyMapping = null;
        configuration.printMapping = null;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        IOException exception = assertThrows(IOException.class,
            () -> obfuscationPreparation.execute(appView));

        assertNotNull(exception.getMessage(),
            "IOException should have a non-null message");
        assertFalse(exception.getMessage().isEmpty(),
            "IOException should have a non-empty message");
    }

    /**
     * Test that execute works when keep list is empty (but not null).
     */
    @Test
    public void testExecuteWithEmptyKeepList() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should not throw when keep list is empty but not null");
    }

    /**
     * Test that execute works with various configuration flags set.
     */
    @Test
    public void testExecuteWithVariousConfigurationFlags() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keep = new ArrayList<>();
        configuration.obfuscate = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.preverify = false;
        configuration.verbose = true;

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle various configuration flags without throwing exceptions");
    }

    /**
     * Test that execute works with applyMapping set to an empty File.
     */
    @Test
    public void testExecuteWithEmptyFileForApplyMapping() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.applyMapping = new File("");

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle empty File for applyMapping without throwing exceptions");
    }

    /**
     * Test that execute works with printMapping set to an empty File.
     */
    @Test
    public void testExecuteWithEmptyFileForPrintMapping() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.printMapping = new File("");

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle empty File for printMapping without throwing exceptions");
    }

    /**
     * Test that execute works with all processing flags disabled but keep rules present.
     */
    @Test
    public void testExecuteWithAllProcessingDisabledButKeepPresent() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;
        configuration.preverify = false;
        configuration.keep = new ArrayList<>();

        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscationPreparation.execute(appView),
            "execute should handle all processing disabled with keep rules present");
    }
}
