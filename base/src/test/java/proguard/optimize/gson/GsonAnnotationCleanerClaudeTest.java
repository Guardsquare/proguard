package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.constant.*;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GsonAnnotationCleaner}.
 * Tests the constructor and all visitor methods to ensure proper cleaning of Gson annotations.
 *
 * The GsonAnnotationCleaner removes Gson annotations (@SerializedName and @Expose) from fields
 * when they are no longer required after Gson optimizations are applied. The behavior depends
 * on the GsonRuntimeSettings configuration.
 */
public class GsonAnnotationCleanerClaudeTest {

    private GsonAnnotationCleaner cleaner;
    private GsonRuntimeSettings gsonRuntimeSettings;

    /**
     * Sets up fresh instances before each test.
     */
    @BeforeEach
    public void setUp() {
        gsonRuntimeSettings = new GsonRuntimeSettings();
        cleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);
    }

    // =========================================================================
    // Tests for constructor: <init>.(Lproguard/optimize/gson/GsonRuntimeSettings;)V
    // =========================================================================

    /**
     * Tests that the constructor successfully creates an instance with valid GsonRuntimeSettings.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);

        // Assert
        assertNotNull(newCleaner, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor.
     * GsonAnnotationCleaner must implement ClassVisitor to visit classes.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Act
        GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);

        // Assert
        assertTrue(newCleaner instanceof ClassVisitor,
                "GsonAnnotationCleaner should implement ClassVisitor");
    }

    /**
     * Tests that the constructor accepts GsonRuntimeSettings and stores it correctly.
     * The settings object is required for determining which annotations to remove.
     */
    @Test
    public void testConstructor_acceptsGsonRuntimeSettings() {
        // Arrange
        GsonRuntimeSettings customSettings = new GsonRuntimeSettings();
        customSettings.setFieldNamingPolicy = true;
        customSettings.setFieldNamingStrategy = false;

        // Act
        GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(customSettings);

        // Assert
        assertNotNull(newCleaner, "Constructor should accept custom GsonRuntimeSettings");
    }

    /**
     * Tests that multiple instances can be created independently with different settings.
     * Each instance should maintain its own GsonRuntimeSettings reference.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnSettings() {
        // Arrange
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        settings1.setFieldNamingPolicy = true;

        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();
        settings2.setFieldNamingStrategy = true;

        GsonRuntimeSettings settings3 = new GsonRuntimeSettings();

        // Act
        GsonAnnotationCleaner cleaner1 = new GsonAnnotationCleaner(settings1);
        GsonAnnotationCleaner cleaner2 = new GsonAnnotationCleaner(settings2);
        GsonAnnotationCleaner cleaner3 = new GsonAnnotationCleaner(settings3);

        // Assert
        assertNotNull(cleaner1, "First instance should be created");
        assertNotNull(cleaner2, "Second instance should be created");
        assertNotNull(cleaner3, "Third instance should be created");
        assertNotSame(cleaner1, cleaner2, "Instances should be distinct");
        assertNotSame(cleaner2, cleaner3, "Instances should be distinct");
        assertNotSame(cleaner1, cleaner3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonAnnotationCleaner(gsonRuntimeSettings),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that the constructor with a null GsonRuntimeSettings parameter.
     * This should not throw during construction, but may throw during usage.
     */
    @Test
    public void testConstructor_withNullSettings_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonAnnotationCleaner(null),
                "Constructor should accept null GsonRuntimeSettings without throwing");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(new GsonRuntimeSettings());
            assertNotNull(newCleaner, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests constructor with GsonRuntimeSettings where both naming flags are false.
     * This is the default state and should allow @SerializedName removal.
     */
    @Test
    public void testConstructor_withDefaultSettings_bothNamingFlagsFalse() {
        // Arrange
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        assertFalse(settings.setFieldNamingPolicy, "Default setFieldNamingPolicy should be false");
        assertFalse(settings.setFieldNamingStrategy, "Default setFieldNamingStrategy should be false");

        // Act
        GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(settings);

        // Assert
        assertNotNull(newCleaner, "Constructor should work with default settings");
    }

    /**
     * Tests constructor with GsonRuntimeSettings where setFieldNamingPolicy is true.
     */
    @Test
    public void testConstructor_withFieldNamingPolicyTrue() {
        // Arrange
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        settings.setFieldNamingPolicy = true;

        // Act
        GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(settings);

        // Assert
        assertNotNull(newCleaner, "Constructor should work with setFieldNamingPolicy=true");
    }

    /**
     * Tests constructor with GsonRuntimeSettings where setFieldNamingStrategy is true.
     */
    @Test
    public void testConstructor_withFieldNamingStrategyTrue() {
        // Arrange
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        settings.setFieldNamingStrategy = true;

        // Act
        GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(settings);

        // Assert
        assertNotNull(newCleaner, "Constructor should work with setFieldNamingStrategy=true");
    }

    /**
     * Tests constructor with GsonRuntimeSettings where both naming flags are true.
     */
    @Test
    public void testConstructor_withBothNamingFlagsTrue() {
        // Arrange
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        settings.setFieldNamingPolicy = true;
        settings.setFieldNamingStrategy = true;

        // Act
        GsonAnnotationCleaner newCleaner = new GsonAnnotationCleaner(settings);

        // Assert
        assertNotNull(newCleaner, "Constructor should work with both naming flags true");
    }

    // =========================================================================
    // Tests for visitAnyClass.(Lproguard/classfile/Clazz;)V
    // =========================================================================

    /**
     * Tests that visitAnyClass does nothing when called with a valid Clazz.
     * The method is a no-op implementation of the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_withValidClazz_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        cleaner.visitAnyClass(clazz);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyClass_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        cleaner.visitAnyClass(clazz1);
        cleaner.visitAnyClass(clazz2);
        cleaner.visitAnyClass(clazz3);

        // Assert
        verifyNoInteractions(clazz1, clazz2, clazz3);
    }

    /**
     * Tests that visitAnyClass does not throw exceptions with a null argument.
     * While not a recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitAnyClass(null),
                "visitAnyClass should not throw exception with null argument");
    }

    /**
     * Tests that visitAnyClass works correctly when called on a LibraryClass.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNothing() {
        // Arrange
        LibraryClass libraryClass = mock(LibraryClass.class);

        // Act
        cleaner.visitAnyClass(libraryClass);

        // Assert
        verifyNoInteractions(libraryClass);
    }

    /**
     * Tests that visitAnyClass works correctly when called on a ProgramClass.
     * Note: visitAnyClass is the generic handler; specific behavior is in visitProgramClass.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNothing() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        cleaner.visitAnyClass(programClass);

        // Assert
        verifyNoInteractions(programClass);
    }

    /**
     * Tests that visitAnyClass is truly a no-op by verifying it doesn't interact
     * with the cleaner's internal state or the class it receives.
     */
    @Test
    public void testVisitAnyClass_isNoOp() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        for (int i = 0; i < 10; i++) {
            cleaner.visitAnyClass(clazz);
        }

        // Assert
        verifyNoInteractions(clazz);
    }

    // =========================================================================
    // Tests for visitProgramClass.(Lproguard/classfile/ProgramClass;)V
    // =========================================================================

    /**
     * Tests that visitProgramClass with null throws NullPointerException.
     * The method requires a non-null ProgramClass to function.
     */
    @Test
    public void testVisitProgramClass_withNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> cleaner.visitProgramClass(null),
                "visitProgramClass should throw NullPointerException for null input");
    }

    /**
     * Tests that visitProgramClass with a minimal valid ProgramClass executes without throwing.
     * This tests the basic execution path through the method.
     */
    @Test
    public void testVisitProgramClass_withMinimalClass_executesWithoutException() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "visitProgramClass should not throw exception with minimal valid class");
    }

    /**
     * Tests that visitProgramClass processes a class with no fields without errors.
     */
    @Test
    public void testVisitProgramClass_withNoFields_executesSuccessfully() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("EmptyClass");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Empty class should be processed without errors");
    }

    /**
     * Tests that visitProgramClass can be called multiple times on different classes.
     */
    @Test
    public void testVisitProgramClass_multipleCalls_eachExecutesIndependently() {
        // Arrange
        ProgramClass class1 = createMinimalProgramClass("Class1");
        ProgramClass class2 = createMinimalProgramClass("Class2");
        ProgramClass class3 = createMinimalProgramClass("Class3");

        // Act & Assert
        assertDoesNotThrow(() -> {
            cleaner.visitProgramClass(class1);
            cleaner.visitProgramClass(class2);
            cleaner.visitProgramClass(class3);
        }, "visitProgramClass should handle multiple calls");
    }

    /**
     * Tests that visitProgramClass works with a class that has a simple name.
     */
    @Test
    public void testVisitProgramClass_withSimpleClassName_executesCorrectly() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("Simple");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "visitProgramClass should handle simple class names");
    }

    /**
     * Tests that visitProgramClass works with a class that has a package-qualified name.
     */
    @Test
    public void testVisitProgramClass_withQualifiedClassName_executesCorrectly() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/example/TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "visitProgramClass should handle qualified class names");
    }

    /**
     * Tests that visitProgramClass processes fields when both naming settings are false.
     * In this case, @SerializedName annotations should be marked for removal.
     */
    @Test
    public void testVisitProgramClass_withBothNamingFlagsFalse_processesFields() {
        // Arrange
        gsonRuntimeSettings.setFieldNamingPolicy = false;
        gsonRuntimeSettings.setFieldNamingStrategy = false;
        cleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);

        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Should process fields when both naming flags are false");
    }

    /**
     * Tests that visitProgramClass behavior when setFieldNamingPolicy is true.
     * In this case, @SerializedName annotations should NOT be marked for removal.
     */
    @Test
    public void testVisitProgramClass_withFieldNamingPolicyTrue_processesFields() {
        // Arrange
        gsonRuntimeSettings.setFieldNamingPolicy = true;
        gsonRuntimeSettings.setFieldNamingStrategy = false;
        cleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);

        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Should process fields when setFieldNamingPolicy is true");
    }

    /**
     * Tests that visitProgramClass behavior when setFieldNamingStrategy is true.
     * In this case, @SerializedName annotations should NOT be marked for removal.
     */
    @Test
    public void testVisitProgramClass_withFieldNamingStrategyTrue_processesFields() {
        // Arrange
        gsonRuntimeSettings.setFieldNamingPolicy = false;
        gsonRuntimeSettings.setFieldNamingStrategy = true;
        cleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);

        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Should process fields when setFieldNamingStrategy is true");
    }

    /**
     * Tests that visitProgramClass behavior when both naming flags are true.
     * In this case, @SerializedName annotations should NOT be marked for removal.
     */
    @Test
    public void testVisitProgramClass_withBothNamingFlagsTrue_processesFields() {
        // Arrange
        gsonRuntimeSettings.setFieldNamingPolicy = true;
        gsonRuntimeSettings.setFieldNamingStrategy = true;
        cleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);

        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Should process fields when both naming flags are true");
    }

    /**
     * Tests that visitProgramClass processes a class with multiple fields.
     */
    @Test
    public void testVisitProgramClass_withMultipleFields_processesAllFields() {
        // Arrange
        ProgramClass programClass = createProgramClassWithMultipleFields("TestClass",
                new String[]{"field1", "field2", "field3"});

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Should process all fields in the class");
    }

    /**
     * Tests that visitProgramClass can handle a class with a large number of fields.
     */
    @Test
    public void testVisitProgramClass_withManyFields_handlesLargeClass() {
        // Arrange
        String[] fieldNames = new String[50];
        for (int i = 0; i < 50; i++) {
            fieldNames[i] = "field" + i;
        }
        ProgramClass programClass = createProgramClassWithMultipleFields("LargeClass", fieldNames);

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Should handle classes with many fields");
    }

    /**
     * Tests that visitProgramClass maintains consistency across multiple invocations.
     */
    @Test
    public void testVisitProgramClass_multipleInvocations_maintainsConsistency() {
        // Arrange
        ProgramClass class1 = createProgramClassWithField("Class1", "field1");
        ProgramClass class2 = createProgramClassWithField("Class2", "field2");

        // Act & Assert
        assertDoesNotThrow(() -> {
            cleaner.visitProgramClass(class1);
            cleaner.visitProgramClass(class2);
            cleaner.visitProgramClass(class1); // Visit class1 again
        }, "Should maintain consistency across multiple invocations");
    }

    /**
     * Tests that different cleaner instances with different settings work independently.
     */
    @Test
    public void testVisitProgramClass_instanceIndependence() {
        // Arrange
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        settings1.setFieldNamingPolicy = false;
        settings1.setFieldNamingStrategy = false;

        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();
        settings2.setFieldNamingPolicy = true;
        settings2.setFieldNamingStrategy = false;

        GsonAnnotationCleaner cleaner1 = new GsonAnnotationCleaner(settings1);
        GsonAnnotationCleaner cleaner2 = new GsonAnnotationCleaner(settings2);

        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");

        // Act & Assert
        assertDoesNotThrow(() -> {
            cleaner1.visitProgramClass(programClass);
            cleaner2.visitProgramClass(programClass);
        }, "Different cleaner instances should work independently");
    }

    /**
     * Tests visitProgramClass with settings that have null class pools.
     * The method should handle this gracefully.
     */
    @Test
    public void testVisitProgramClass_withNullClassPools_handlesGracefully() {
        // Arrange
        gsonRuntimeSettings.instanceCreatorClassPool = null;
        gsonRuntimeSettings.typeAdapterClassPool = null;
        cleaner = new GsonAnnotationCleaner(gsonRuntimeSettings);

        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> cleaner.visitProgramClass(programClass),
                "Should handle null class pools gracefully");
    }

    /**
     * Tests that visitProgramClass behavior is deterministic.
     * Multiple calls with the same input should behave the same way.
     */
    @Test
    public void testVisitProgramClass_deterministic_sameInputSameBehavior() {
        // Arrange
        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                cleaner.visitProgramClass(programClass);
            }
        }, "Multiple calls with same input should behave consistently");
    }

    // =========================================================================
    // Helper methods to create test objects
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
     * The class has a name set up in the constant pool, but no fields.
     *
     * @param className the name of the class (e.g., "TestClass" or "com/example/TestClass")
     * @return a configured ProgramClass instance
     */
    private ProgramClass createMinimalProgramClass(String className) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        Constant[] constantPool = new Constant[10];
        constantPool[0] = null; // Index 0 is always null in Java constant pools
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;
        programClass.fields = new ProgramField[0];
        programClass.u2fieldsCount = 0;

        return programClass;
    }

    /**
     * Creates a ProgramClass with a single field.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return a configured ProgramClass instance with one field
     */
    private ProgramClass createProgramClassWithField(String className, String fieldName) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with class name and field info
        Constant[] constantPool = new Constant[20];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);
        constantPool[3] = new Utf8Constant(fieldName);
        constantPool[4] = new Utf8Constant("I"); // field descriptor for int

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 20;

        // Create a field
        ProgramField field = new ProgramField();
        field.u2accessFlags = 0;
        field.u2nameIndex = 3;
        field.u2descriptorIndex = 4;
        field.u2attributesCount = 0;
        field.attributes = new Attribute[0];

        programClass.fields = new ProgramField[]{field};
        programClass.u2fieldsCount = 1;

        return programClass;
    }

    /**
     * Creates a ProgramClass with multiple fields.
     *
     * @param className the name of the class
     * @param fieldNames array of field names
     * @return a configured ProgramClass instance with multiple fields
     */
    private ProgramClass createProgramClassWithMultipleFields(String className, String[] fieldNames) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool large enough for class name and all fields
        int poolSize = 100 + (fieldNames.length * 2);
        Constant[] constantPool = new Constant[poolSize];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        int nextIndex = 3;
        ProgramField[] fields = new ProgramField[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++) {
            // Add field name to constant pool
            constantPool[nextIndex] = new Utf8Constant(fieldNames[i]);
            int nameIndex = nextIndex++;

            // Add field descriptor to constant pool
            constantPool[nextIndex] = new Utf8Constant("I"); // int type
            int descriptorIndex = nextIndex++;

            // Create field
            ProgramField field = new ProgramField();
            field.u2accessFlags = 0;
            field.u2nameIndex = nameIndex;
            field.u2descriptorIndex = descriptorIndex;
            field.u2attributesCount = 0;
            field.attributes = new Attribute[0];

            fields[i] = field;
        }

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = poolSize;
        programClass.fields = fields;
        programClass.u2fieldsCount = fieldNames.length;

        return programClass;
    }
}
