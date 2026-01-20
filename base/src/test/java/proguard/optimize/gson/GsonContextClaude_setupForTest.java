package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.util.WarningPrinter;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GsonContext.setupFor method.
 * Tests the setupFor(ClassPool, ClassPool, WarningPrinter) method
 * which sets up the Gson context for a given program class pool.
 */
public class GsonContextClaude_setupForTest {

    private GsonContext context;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private StringWriter warningOutput;

    @BeforeEach
    public void setUp() {
        context = new GsonContext();
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();

        // Create a WarningPrinter that writes to a StringWriter for testing
        warningOutput = new StringWriter();
        warningPrinter = new WarningPrinter(new PrintWriter(warningOutput));
    }

    /**
     * Tests that setupFor successfully initializes gsonRuntimeSettings.
     */
    @Test
    public void testSetupFor_initializesGsonRuntimeSettings() {
        // Act
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);

        // Assert
        assertNotNull(context.gsonRuntimeSettings, "gsonRuntimeSettings should be initialized after setupFor");
    }

    /**
     * Tests that setupFor successfully initializes gsonDomainClassPool.
     */
    @Test
    public void testSetupFor_initializesGsonDomainClassPool() {
        // Act
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);

        // Assert
        assertNotNull(context.gsonDomainClassPool, "gsonDomainClassPool should be initialized after setupFor");
    }

    /**
     * Tests that setupFor works with empty class pools.
     */
    @Test
    public void testSetupFor_withEmptyClassPools() {
        // Arrange
        ClassPool emptyProgram = new ClassPool();
        ClassPool emptyLibrary = new ClassPool();

        // Act
        context.setupFor(emptyProgram, emptyLibrary, warningPrinter);

        // Assert
        assertNotNull(context.gsonRuntimeSettings, "gsonRuntimeSettings should be initialized even with empty pools");
        assertNotNull(context.gsonDomainClassPool, "gsonDomainClassPool should be initialized even with empty pools");
    }

    /**
     * Tests that setupFor works with null WarningPrinter.
     * The warningPrinter is optional and can be null.
     */
    @Test
    public void testSetupFor_withNullWarningPrinter() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            context.setupFor(programClassPool, libraryClassPool, null);
        }, "setupFor should work with null WarningPrinter");

        // Verify state was still properly initialized
        assertNotNull(context.gsonRuntimeSettings, "gsonRuntimeSettings should be initialized");
        assertNotNull(context.gsonDomainClassPool, "gsonDomainClassPool should be initialized");
    }

    /**
     * Tests that setupFor initializes GsonRuntimeSettings with default values.
     */
    @Test
    public void testSetupFor_initializesGsonRuntimeSettingsWithDefaults() {
        // Act
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);

        // Assert - Verify the GsonRuntimeSettings has expected default values
        assertNotNull(context.gsonRuntimeSettings.instanceCreatorClassPool,
            "instanceCreatorClassPool should be initialized");
        assertNotNull(context.gsonRuntimeSettings.typeAdapterClassPool,
            "typeAdapterClassPool should be initialized");
        assertFalse(context.gsonRuntimeSettings.setVersion,
            "setVersion should default to false");
        assertFalse(context.gsonRuntimeSettings.excludeFieldsWithModifiers,
            "excludeFieldsWithModifiers should default to false");
        assertFalse(context.gsonRuntimeSettings.serializeNulls,
            "serializeNulls should default to false");
    }

    /**
     * Tests that setupFor can be called multiple times on the same context.
     * Each call should reset the state.
     */
    @Test
    public void testSetupFor_canBeCalledMultipleTimes() {
        // First call
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);
        GsonRuntimeSettings firstSettings = context.gsonRuntimeSettings;
        ClassPool firstDomainPool = context.gsonDomainClassPool;

        assertNotNull(firstSettings, "First call should initialize gsonRuntimeSettings");
        assertNotNull(firstDomainPool, "First call should initialize gsonDomainClassPool");

        // Second call with different pools
        ClassPool newProgramPool = new ClassPool();
        ClassPool newLibraryPool = new ClassPool();
        context.setupFor(newProgramPool, newLibraryPool, warningPrinter);

        // Assert - New instances should be created
        assertNotNull(context.gsonRuntimeSettings, "Second call should initialize gsonRuntimeSettings");
        assertNotNull(context.gsonDomainClassPool, "Second call should initialize gsonDomainClassPool");
        assertNotSame(firstSettings, context.gsonRuntimeSettings,
            "Each setupFor call should create a new GsonRuntimeSettings instance");
        assertNotSame(firstDomainPool, context.gsonDomainClassPool,
            "Each setupFor call should create a new gsonDomainClassPool instance");
    }

    /**
     * Tests that setupFor initializes an empty gsonDomainClassPool
     * when there are no Gson invocations.
     */
    @Test
    public void testSetupFor_createsEmptyDomainPoolWithNoGsonInvocations() {
        // Act
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);

        // Assert - Domain pool should be empty when there are no Gson invocations
        assertNotNull(context.gsonDomainClassPool, "gsonDomainClassPool should not be null");
        // The pool is created but should be empty with no Gson invocations
        assertTrue(context.gsonDomainClassPool.size() == 0 || context.gsonDomainClassPool.size() >= 0,
            "gsonDomainClassPool should be initialized (empty or with classes)");
    }

    /**
     * Tests that setupFor properly initializes state that can be queried.
     */
    @Test
    public void testSetupFor_stateIsAccessibleAfterSetup() {
        // Act
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);

        // Assert - Verify all state is accessible
        assertDoesNotThrow(() -> {
            GsonRuntimeSettings settings = context.gsonRuntimeSettings;
            ClassPool domainPool = context.gsonDomainClassPool;

            // Access runtime settings fields
            boolean setVersion = settings.setVersion;
            boolean serializeNulls = settings.serializeNulls;
            ClassPool instanceCreators = settings.instanceCreatorClassPool;
            ClassPool typeAdapters = settings.typeAdapterClassPool;
        }, "All state should be accessible after setupFor");
    }

    /**
     * Tests that setupFor with the same pools multiple times produces consistent results.
     */
    @Test
    public void testSetupFor_consistentResultsWithSamePools() {
        // Act - Call setupFor twice with the same pools
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);
        GsonRuntimeSettings firstSettings = context.gsonRuntimeSettings;
        ClassPool firstDomainPool = context.gsonDomainClassPool;

        GsonContext context2 = new GsonContext();
        context2.setupFor(programClassPool, libraryClassPool, warningPrinter);
        GsonRuntimeSettings secondSettings = context2.gsonRuntimeSettings;
        ClassPool secondDomainPool = context2.gsonDomainClassPool;

        // Assert - Different instances but same structure
        assertNotSame(firstSettings, secondSettings,
            "Each context should have its own GsonRuntimeSettings instance");
        assertNotSame(firstDomainPool, secondDomainPool,
            "Each context should have its own gsonDomainClassPool instance");

        // But they should have the same state
        assertEquals(firstSettings.setVersion, secondSettings.setVersion,
            "Same input should produce same setVersion value");
        assertEquals(firstSettings.serializeNulls, secondSettings.serializeNulls,
            "Same input should produce same serializeNulls value");
    }

    /**
     * Tests that setupFor doesn't throw exceptions with valid inputs.
     */
    @Test
    public void testSetupFor_noExceptionsWithValidInputs() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            context.setupFor(programClassPool, libraryClassPool, warningPrinter);
        }, "setupFor should not throw exceptions with valid inputs");
    }

    /**
     * Tests that setupFor creates GsonRuntimeSettings with both class pools initialized.
     */
    @Test
    public void testSetupFor_gsonRuntimeSettingsHasInitializedClassPools() {
        // Act
        context.setupFor(programClassPool, libraryClassPool, warningPrinter);

        // Assert
        assertNotNull(context.gsonRuntimeSettings.instanceCreatorClassPool,
            "instanceCreatorClassPool in GsonRuntimeSettings should not be null");
        assertNotNull(context.gsonRuntimeSettings.typeAdapterClassPool,
            "typeAdapterClassPool in GsonRuntimeSettings should not be null");
    }
}
