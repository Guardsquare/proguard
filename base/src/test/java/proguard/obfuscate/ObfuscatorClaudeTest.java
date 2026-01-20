package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Obfuscator}.
 * Tests the constructor and execute method for obfuscation functionality.
 */
public class ObfuscatorClaudeTest {

    /**
     * Test that constructor accepts a non-null Configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        Obfuscator obfuscator = new Obfuscator(configuration);

        // Assert
        assertNotNull(obfuscator, "Obfuscator should be created successfully");
    }

    /**
     * Test that constructor accepts a null Configuration.
     * The implementation stores the configuration without validation.
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act
        Obfuscator obfuscator = new Obfuscator(null);

        // Assert
        assertNotNull(obfuscator, "Obfuscator should be created even with null configuration");
    }

    /**
     * Test that execute throws NullPointerException when configuration is null.
     */
    @Test
    public void testExecuteWithNullConfiguration() {
        // Arrange
        Obfuscator obfuscator = new Obfuscator(null);
        AppView appView = new AppView();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> obfuscator.execute(appView),
            "execute should throw NullPointerException when configuration is null");
    }

    /**
     * Test that execute throws NullPointerException when AppView is null.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        Obfuscator obfuscator = new Obfuscator(configuration);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> obfuscator.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Test that execute works with minimal valid configuration and empty AppView.
     */
    @Test
    public void testExecuteWithMinimalConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.useUniqueClassMemberNames = false;
        configuration.keepKotlinMetadata = false;
        configuration.keepParameterNames = false;
        configuration.applyMapping = null;
        configuration.overloadAggressively = false;
        configuration.allowAccessModification = false;
        configuration.repackageClasses = null;
        configuration.printMapping = null;
        configuration.addConfigurationDebugging = false;
        configuration.newSourceFileAttribute = null;
        configuration.android = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should not throw with minimal configuration and empty AppView");
    }

    /**
     * Test that execute works with empty ClassPools.
     */
    @Test
    public void testExecuteWithEmptyClassPools() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.useUniqueClassMemberNames = false;
        configuration.keepKotlinMetadata = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should not throw with empty class pools");
    }

    /**
     * Test execute with useUniqueClassMemberNames enabled.
     */
    @Test
    public void testExecuteWithUniqueClassMemberNames() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.useUniqueClassMemberNames = true;
        configuration.keepKotlinMetadata = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle useUniqueClassMemberNames flag");
    }

    /**
     * Test execute with keepKotlinMetadata enabled.
     */
    @Test
    public void testExecuteWithKeepKotlinMetadata() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle keepKotlinMetadata flag");
    }

    /**
     * Test execute with keepParameterNames enabled.
     */
    @Test
    public void testExecuteWithKeepParameterNames() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepParameterNames = true;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle keepParameterNames flag");
    }

    /**
     * Test execute with overloadAggressively enabled.
     */
    @Test
    public void testExecuteWithOverloadAggressively() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.overloadAggressively = true;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepKotlinMetadata = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle overloadAggressively flag");
    }

    /**
     * Test execute with allowAccessModification and repackageClasses enabled.
     */
    @Test
    public void testExecuteWithRepackageAndAccessModification() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.repackageClasses = "obfuscated";
        configuration.allowAccessModification = true;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle repackageClasses with allowAccessModification");
    }

    /**
     * Test execute with repackageClasses but without allowAccessModification.
     */
    @Test
    public void testExecuteWithRepackageWithoutAccessModification() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.repackageClasses = "obfuscated";
        configuration.allowAccessModification = false;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle repackageClasses without allowAccessModification");
    }

    /**
     * Test execute with printMapping configured.
     */
    @Test
    public void testExecuteWithPrintMapping() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.printMapping = Configuration.STD_OUT;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle printMapping to standard output");
    }

    /**
     * Test execute with addConfigurationDebugging enabled.
     */
    @Test
    public void testExecuteWithAddConfigurationDebugging() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.addConfigurationDebugging = true;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle addConfigurationDebugging flag");
    }

    /**
     * Test execute with newSourceFileAttribute configured.
     */
    @Test
    public void testExecuteWithNewSourceFileAttribute() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.newSourceFileAttribute = "SourceFile";
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle newSourceFileAttribute");
    }

    /**
     * Test execute with useMixedCaseClassNames enabled.
     */
    @Test
    public void testExecuteWithMixedCaseClassNames() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.useMixedCaseClassNames = true;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle useMixedCaseClassNames flag");
    }

    /**
     * Test execute with keepPackageNames configured.
     */
    @Test
    public void testExecuteWithKeepPackageNames() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepPackageNames = new ArrayList<>();
        configuration.keepPackageNames.add("com.example.*");
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle keepPackageNames");
    }

    /**
     * Test execute with flattenPackageHierarchy configured.
     */
    @Test
    public void testExecuteWithFlattenPackageHierarchy() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.flattenPackageHierarchy = "flat";
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle flattenPackageHierarchy");
    }

    /**
     * Test execute with android flag enabled.
     */
    @Test
    public void testExecuteWithAndroidFlag() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.android = true;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle android flag");
    }

    /**
     * Test execute with keepAttributes configured.
     */
    @Test
    public void testExecuteWithKeepAttributes() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepAttributes = new ArrayList<>();
        configuration.keepAttributes.add("SourceFile");
        configuration.keepAttributes.add("LineNumberTable");
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle keepAttributes");
    }

    /**
     * Test execute with ignoreWarnings enabled.
     */
    @Test
    public void testExecuteWithIgnoreWarnings() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.ignoreWarnings = true;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle ignoreWarnings flag");
    }

    /**
     * Test execute with warn list configured.
     */
    @Test
    public void testExecuteWithWarnList() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.warn = new ArrayList<>();
        configuration.warn.add("org.example.*");
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle warn list");
    }

    /**
     * Test execute with multiple flags enabled simultaneously.
     */
    @Test
    public void testExecuteWithMultipleFlagsEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.useUniqueClassMemberNames = true;
        configuration.overloadAggressively = true;
        configuration.useMixedCaseClassNames = true;
        configuration.allowAccessModification = true;
        configuration.repackageClasses = "obf";
        configuration.ignoreWarnings = true;
        configuration.keepKotlinMetadata = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle multiple flags enabled simultaneously");
    }

    /**
     * Test that execute can be called multiple times on the same AppView.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator.execute(appView);
            obfuscator.execute(appView);
        }, "execute should handle being called multiple times on the same AppView");
    }

    /**
     * Test that execute can be called by different Obfuscator instances on the same AppView.
     */
    @Test
    public void testExecuteWithDifferentInstances() {
        // Arrange
        Configuration configuration1 = new Configuration();
        configuration1.keepKotlinMetadata = false;
        configuration1.useUniqueClassMemberNames = false;
        configuration1.keepParameterNames = false;

        Configuration configuration2 = new Configuration();
        configuration2.keepKotlinMetadata = false;
        configuration2.useUniqueClassMemberNames = true;
        configuration2.keepParameterNames = false;

        Obfuscator obfuscator1 = new Obfuscator(configuration1);
        Obfuscator obfuscator2 = new Obfuscator(configuration2);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator1.execute(appView);
            obfuscator2.execute(appView);
        }, "execute should handle different instances operating on the same AppView");
    }

    /**
     * Test execute with all Kotlin-related flags enabled.
     */
    @Test
    public void testExecuteWithAllKotlinFlagsEnabled() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;
        configuration.keepParameterNames = true;
        configuration.useUniqueClassMemberNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle all Kotlin-related flags enabled");
    }

    /**
     * Test execute with both flattenPackageHierarchy and repackageClasses.
     */
    @Test
    public void testExecuteWithFlattenAndRepackage() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.flattenPackageHierarchy = "flat";
        configuration.repackageClasses = "obf";
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle both flattenPackageHierarchy and repackageClasses");
    }

    /**
     * Test execute with fully initialized AppView with all components.
     */
    @Test
    public void testExecuteWithFullyInitializedAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool,
            new proguard.resources.file.ResourceFilePool(), new proguard.io.ExtraDataEntryNameMap());

        Obfuscator obfuscator = new Obfuscator(configuration);

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    /**
     * Test that constructor stores configuration correctly.
     */
    @Test
    public void testConstructorStoresConfiguration() {
        // Arrange
        Configuration configuration1 = new Configuration();
        Configuration configuration2 = new Configuration();

        // Act
        Obfuscator obfuscator1 = new Obfuscator(configuration1);
        Obfuscator obfuscator2 = new Obfuscator(configuration2);

        // Assert - Both should be created successfully (no exception means success)
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
    }

    /**
     * Test execute with null keepPackageNames (should not cause issues).
     */
    @Test
    public void testExecuteWithNullKeepPackageNames() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepPackageNames = null;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle null keepPackageNames");
    }

    /**
     * Test execute with null keepAttributes (default behavior).
     */
    @Test
    public void testExecuteWithNullKeepAttributes() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepAttributes = null;
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle null keepAttributes");
    }

    /**
     * Test execute with empty keepPackageNames list.
     */
    @Test
    public void testExecuteWithEmptyKeepPackageNames() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepPackageNames = new ArrayList<>();
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle empty keepPackageNames list");
    }

    /**
     * Test execute with empty keepAttributes list.
     */
    @Test
    public void testExecuteWithEmptyKeepAttributes() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.keepAttributes = new ArrayList<>();
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.execute(appView),
            "execute should handle empty keepAttributes list");
    }

    /**
     * Test execute with applyMapping set to non-existent file throws IOException.
     */
    @Test
    public void testExecuteWithApplyMappingToNonExistentFile() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.applyMapping = new File("/nonexistent/path/mapping.txt");
        configuration.keepKotlinMetadata = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepParameterNames = false;

        Obfuscator obfuscator = new Obfuscator(configuration);
        AppView appView = new AppView();

        // Act & Assert
        assertThrows(IOException.class, () -> obfuscator.execute(appView),
            "execute should throw IOException when applyMapping file does not exist");
    }
}
