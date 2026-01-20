package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the GsonDomainClassFinder constructor.
 * Tests the constructor
 * (Lproguard/optimize/gson/GsonRuntimeSettings;Lproguard/classfile/ClassPool;Lproguard/classfile/util/WarningPrinter;)V
 */
public class GsonDomainClassFinderClaude_constructorTest {

    private GsonRuntimeSettings gsonRuntimeSettings;
    private ClassPool gsonDomainClassPool;
    private WarningPrinter warningPrinter;

    @BeforeEach
    public void setUp() {
        gsonRuntimeSettings = new GsonRuntimeSettings();
        gsonDomainClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
    }

    /**
     * Tests that the constructor successfully creates a non-null instance with all valid parameters.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            gsonRuntimeSettings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor properly implements ClassVisitor interface.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            gsonRuntimeSettings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertTrue(finder instanceof proguard.classfile.visitor.ClassVisitor,
            "GsonDomainClassFinder should implement ClassVisitor");
    }

    /**
     * Tests that the constructor works with a fresh GsonRuntimeSettings instance.
     */
    @Test
    public void testConstructor_withFreshGsonRuntimeSettings() {
        // Arrange
        GsonRuntimeSettings freshSettings = new GsonRuntimeSettings();

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            freshSettings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with fresh GsonRuntimeSettings");
    }

    /**
     * Tests that the constructor works with a configured GsonRuntimeSettings.
     */
    @Test
    public void testConstructor_withConfiguredGsonRuntimeSettings() {
        // Arrange
        GsonRuntimeSettings configuredSettings = new GsonRuntimeSettings();
        configuredSettings.setVersion = true;
        configuredSettings.excludeFieldsWithModifiers = true;
        configuredSettings.serializeNulls = true;
        configuredSettings.excludeFieldsWithoutExposeAnnotation = true;

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            configuredSettings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with configured GsonRuntimeSettings");
    }

    /**
     * Tests that the constructor works with an empty ClassPool.
     */
    @Test
    public void testConstructor_withEmptyClassPool() {
        // Arrange
        ClassPool emptyClassPool = new ClassPool();

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            gsonRuntimeSettings,
            emptyClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with empty ClassPool");
    }

    /**
     * Tests that the constructor works with a mocked WarningPrinter.
     */
    @Test
    public void testConstructor_withMockedWarningPrinter() {
        // Arrange
        WarningPrinter mockPrinter = mock(WarningPrinter.class);

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            gsonRuntimeSettings,
            gsonDomainClassPool,
            mockPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with mocked WarningPrinter");
    }

    /**
     * Tests that the constructor works with a null WarningPrinter.
     * The implementation checks for null before using the warningPrinter.
     */
    @Test
    public void testConstructor_withNullWarningPrinter() {
        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            gsonRuntimeSettings,
            gsonDomainClassPool,
            null
        );

        // Assert
        assertNotNull(finder, "Constructor should work with null WarningPrinter");
    }

    /**
     * Tests that the constructor works when GsonRuntimeSettings has excludeFieldsWithModifiers set to true.
     */
    @Test
    public void testConstructor_withExcludeFieldsWithModifiersTrue() {
        // Arrange
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        settings.excludeFieldsWithModifiers = true;

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            settings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with excludeFieldsWithModifiers=true");
    }

    /**
     * Tests that the constructor works when GsonRuntimeSettings has excludeFieldsWithModifiers set to false.
     */
    @Test
    public void testConstructor_withExcludeFieldsWithModifiersFalse() {
        // Arrange
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        settings.excludeFieldsWithModifiers = false;

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            settings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with excludeFieldsWithModifiers=false");
    }

    /**
     * Tests that the constructor works with all GsonRuntimeSettings flags set to true.
     */
    @Test
    public void testConstructor_withAllGsonRuntimeSettingsFlagsTrue() {
        // Arrange
        GsonRuntimeSettings allTrueSettings = new GsonRuntimeSettings();
        allTrueSettings.setVersion = true;
        allTrueSettings.excludeFieldsWithModifiers = true;
        allTrueSettings.generateNonExecutableJson = true;
        allTrueSettings.excludeFieldsWithoutExposeAnnotation = true;
        allTrueSettings.serializeNulls = true;
        allTrueSettings.disableInnerClassSerialization = true;
        allTrueSettings.setLongSerializationPolicy = true;
        allTrueSettings.setFieldNamingPolicy = true;
        allTrueSettings.setFieldNamingStrategy = true;
        allTrueSettings.setExclusionStrategies = true;
        allTrueSettings.addSerializationExclusionStrategy = true;
        allTrueSettings.addDeserializationExclusionStrategy = true;
        allTrueSettings.registerTypeAdapterFactory = true;
        allTrueSettings.serializeSpecialFloatingPointValues = true;

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            allTrueSettings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with all GsonRuntimeSettings flags set to true");
    }

    /**
     * Tests that the constructor works with all GsonRuntimeSettings flags set to false.
     */
    @Test
    public void testConstructor_withAllGsonRuntimeSettingsFlagsFalse() {
        // Arrange
        GsonRuntimeSettings allFalseSettings = new GsonRuntimeSettings();
        allFalseSettings.setVersion = false;
        allFalseSettings.excludeFieldsWithModifiers = false;
        allFalseSettings.generateNonExecutableJson = false;
        allFalseSettings.excludeFieldsWithoutExposeAnnotation = false;
        allFalseSettings.serializeNulls = false;
        allFalseSettings.disableInnerClassSerialization = false;
        allFalseSettings.setLongSerializationPolicy = false;
        allFalseSettings.setFieldNamingPolicy = false;
        allFalseSettings.setFieldNamingStrategy = false;
        allFalseSettings.setExclusionStrategies = false;
        allFalseSettings.addSerializationExclusionStrategy = false;
        allFalseSettings.addDeserializationExclusionStrategy = false;
        allFalseSettings.registerTypeAdapterFactory = false;
        allFalseSettings.serializeSpecialFloatingPointValues = false;

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            allFalseSettings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with all GsonRuntimeSettings flags set to false");
    }

    /**
     * Tests that the constructor works with GsonRuntimeSettings containing populated ClassPools.
     */
    @Test
    public void testConstructor_withPopulatedGsonRuntimeSettingsClassPools() {
        // Arrange
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        // The ClassPools in GsonRuntimeSettings are initialized by default
        assertNotNull(settings.instanceCreatorClassPool);
        assertNotNull(settings.typeAdapterClassPool);

        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            settings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert
        assertNotNull(finder, "Constructor should work with populated GsonRuntimeSettings ClassPools");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_createIndependently() {
        // Act
        GsonDomainClassFinder finder1 = new GsonDomainClassFinder(
            gsonRuntimeSettings, gsonDomainClassPool, warningPrinter);

        GsonDomainClassFinder finder2 = new GsonDomainClassFinder(
            new GsonRuntimeSettings(), new ClassPool(), mock(WarningPrinter.class));

        GsonDomainClassFinder finder3 = new GsonDomainClassFinder(
            gsonRuntimeSettings, new ClassPool(), null);

        // Assert
        assertNotNull(finder1, "First instance should be created");
        assertNotNull(finder2, "Second instance should be created");
        assertNotNull(finder3, "Third instance should be created");
        assertNotSame(finder1, finder2, "Instances should be distinct");
        assertNotSame(finder2, finder3, "Instances should be distinct");
        assertNotSame(finder1, finder3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid inputs.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonDomainClassFinder(
            gsonRuntimeSettings,
            gsonDomainClassPool,
            warningPrinter
        ), "Constructor should not throw any exception with valid inputs");
    }

    /**
     * Tests that the constructor can be called multiple times consecutively.
     */
    @Test
    public void testConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            GsonDomainClassFinder finder = new GsonDomainClassFinder(
                new GsonRuntimeSettings(),
                new ClassPool(),
                i % 2 == 0 ? mock(WarningPrinter.class) : null
            );
            assertNotNull(finder, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests that the constructor correctly initializes the finder to be ready for visiting classes.
     */
    @Test
    public void testConstructor_createsReadyToUseVisitor() {
        // Act
        GsonDomainClassFinder finder = new GsonDomainClassFinder(
            gsonRuntimeSettings,
            gsonDomainClassPool,
            warningPrinter
        );

        // Assert - Should be able to call visitor methods without error
        assertDoesNotThrow(() -> finder.visitAnyClass(null),
            "Should be able to call visitAnyClass after construction");
    }

    /**
     * Tests that different finder instances can be created with different configurations.
     */
    @Test
    public void testConstructor_withDifferentConfigurations_createsIndependentInstances() {
        // Arrange
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        settings1.excludeFieldsWithModifiers = true;

        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();
        settings2.excludeFieldsWithModifiers = false;

        ClassPool pool1 = new ClassPool();
        ClassPool pool2 = new ClassPool();

        // Act
        GsonDomainClassFinder finder1 = new GsonDomainClassFinder(
            settings1, pool1, warningPrinter);

        GsonDomainClassFinder finder2 = new GsonDomainClassFinder(
            settings2, pool2, null);

        // Assert
        assertNotNull(finder1, "First finder should be created");
        assertNotNull(finder2, "Second finder should be created");
        assertNotSame(finder1, finder2, "Different configurations should create distinct instances");
    }

    /**
     * Tests that the constructor works when the same ClassPool is used for multiple finders.
     */
    @Test
    public void testConstructor_withSharedClassPool() {
        // Arrange
        ClassPool sharedPool = new ClassPool();

        // Act
        GsonDomainClassFinder finder1 = new GsonDomainClassFinder(
            gsonRuntimeSettings, sharedPool, warningPrinter);

        GsonDomainClassFinder finder2 = new GsonDomainClassFinder(
            new GsonRuntimeSettings(), sharedPool, mock(WarningPrinter.class));

        // Assert
        assertNotNull(finder1, "First finder should be created");
        assertNotNull(finder2, "Second finder should be created");
        assertNotSame(finder1, finder2, "Finders should be distinct even with shared ClassPool");
    }

    /**
     * Tests that the constructor works when the same GsonRuntimeSettings is used for multiple finders.
     */
    @Test
    public void testConstructor_withSharedGsonRuntimeSettings() {
        // Arrange
        GsonRuntimeSettings sharedSettings = new GsonRuntimeSettings();

        // Act
        GsonDomainClassFinder finder1 = new GsonDomainClassFinder(
            sharedSettings, gsonDomainClassPool, warningPrinter);

        GsonDomainClassFinder finder2 = new GsonDomainClassFinder(
            sharedSettings, new ClassPool(), mock(WarningPrinter.class));

        // Assert
        assertNotNull(finder1, "First finder should be created");
        assertNotNull(finder2, "Second finder should be created");
        assertNotSame(finder1, finder2, "Finders should be distinct even with shared settings");
    }

    /**
     * Tests that the constructor accepts all parameter combinations including null WarningPrinter.
     */
    @Test
    public void testConstructor_withVariousParameterCombinations() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            new GsonDomainClassFinder(new GsonRuntimeSettings(), new ClassPool(), null);
            new GsonDomainClassFinder(new GsonRuntimeSettings(), new ClassPool(), mock(WarningPrinter.class));
            new GsonDomainClassFinder(gsonRuntimeSettings, gsonDomainClassPool, null);
            new GsonDomainClassFinder(gsonRuntimeSettings, gsonDomainClassPool, warningPrinter);
        }, "Constructor should accept various parameter combinations");
    }
}
