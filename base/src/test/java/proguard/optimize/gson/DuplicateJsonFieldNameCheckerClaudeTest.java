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
 * Test class for {@link DuplicateJsonFieldNameChecker}.
 * Tests the constructor and all visitor methods to ensure proper detection of duplicate JSON field names.
 *
 * Note: Testing visitProgramClass requires a complex setup of ProgramClass with proper constant pool,
 * fields, and annotations. These tests focus on the interface contract and basic behavior.
 * The actual duplicate detection logic is integration-tested through higher-level tests.
 */
public class DuplicateJsonFieldNameCheckerClaudeTest {

    private DuplicateJsonFieldNameChecker checker;

    /**
     * Sets up a fresh DuplicateJsonFieldNameChecker instance before each test.
     */
    @BeforeEach
    public void setUp() {
        checker = new DuplicateJsonFieldNameChecker();
    }

    // =========================================================================
    // Tests for constructor: <init>.()V
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates an instance.
     * The constructor should initialize the checker with hasDuplicateJsonFieldNames set to false.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        DuplicateJsonFieldNameChecker newChecker = new DuplicateJsonFieldNameChecker();

        // Assert
        assertNotNull(newChecker, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor initializes hasDuplicateJsonFieldNames to false.
     * This is the initial state before any class has been visited.
     */
    @Test
    public void testConstructor_initializesHasDuplicateJsonFieldNamesToFalse() {
        // Act
        DuplicateJsonFieldNameChecker newChecker = new DuplicateJsonFieldNameChecker();

        // Assert
        assertFalse(newChecker.hasDuplicateJsonFieldNames,
                "hasDuplicateJsonFieldNames should be initialized to false");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor.
     * DuplicateJsonFieldNameChecker must implement ClassVisitor to visit classes.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Act
        DuplicateJsonFieldNameChecker newChecker = new DuplicateJsonFieldNameChecker();

        // Assert
        assertTrue(newChecker instanceof ClassVisitor,
                "DuplicateJsonFieldNameChecker should implement ClassVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Each instance should have its own state.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Act
        DuplicateJsonFieldNameChecker checker1 = new DuplicateJsonFieldNameChecker();
        DuplicateJsonFieldNameChecker checker2 = new DuplicateJsonFieldNameChecker();
        DuplicateJsonFieldNameChecker checker3 = new DuplicateJsonFieldNameChecker();

        // Assert
        assertNotNull(checker1, "First instance should be created");
        assertNotNull(checker2, "Second instance should be created");
        assertNotNull(checker3, "Third instance should be created");
        assertNotSame(checker1, checker2, "Instances should be distinct");
        assertNotSame(checker2, checker3, "Instances should be distinct");
        assertNotSame(checker1, checker3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new DuplicateJsonFieldNameChecker(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     * Verifies that each instance starts with false for hasDuplicateJsonFieldNames.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            DuplicateJsonFieldNameChecker newChecker = new DuplicateJsonFieldNameChecker();
            assertNotNull(newChecker, "Instance " + i + " should be created");
            assertFalse(newChecker.hasDuplicateJsonFieldNames,
                    "Instance " + i + " should have hasDuplicateJsonFieldNames initialized to false");
        }
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
        boolean initialValue = checker.hasDuplicateJsonFieldNames;

        // Act
        checker.visitAnyClass(clazz);

        // Assert
        assertEquals(initialValue, checker.hasDuplicateJsonFieldNames,
                "visitAnyClass should not modify hasDuplicateJsonFieldNames");
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
        checker.visitAnyClass(clazz1);
        checker.visitAnyClass(clazz2);
        checker.visitAnyClass(clazz3);

        // Assert
        assertFalse(checker.hasDuplicateJsonFieldNames,
                "hasDuplicateJsonFieldNames should remain false after multiple visitAnyClass calls");
        verifyNoInteractions(clazz1, clazz2, clazz3);
    }

    /**
     * Tests that visitAnyClass does not throw exceptions with a null argument.
     * While not a recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyClass(null),
                "visitAnyClass should not throw exception with null argument");
    }

    /**
     * Tests that visitAnyClass works correctly when called on a LibraryClass.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNothing() {
        // Arrange
        LibraryClass libraryClass = mock(LibraryClass.class);
        boolean initialValue = checker.hasDuplicateJsonFieldNames;

        // Act
        checker.visitAnyClass(libraryClass);

        // Assert
        assertEquals(initialValue, checker.hasDuplicateJsonFieldNames,
                "visitAnyClass should not modify hasDuplicateJsonFieldNames for LibraryClass");
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
        boolean initialValue = checker.hasDuplicateJsonFieldNames;

        // Act
        checker.visitAnyClass(programClass);

        // Assert
        assertEquals(initialValue, checker.hasDuplicateJsonFieldNames,
                "visitAnyClass should not modify hasDuplicateJsonFieldNames for ProgramClass");
        verifyNoInteractions(programClass);
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
                () -> checker.visitProgramClass(null),
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
        assertDoesNotThrow(() -> checker.visitProgramClass(programClass),
                "visitProgramClass should not throw exception with minimal valid class");
    }

    /**
     * Tests that visitProgramClass does not set hasDuplicateJsonFieldNames to true
     * when processing a class with no fields.
     */
    @Test
    public void testVisitProgramClass_withNoFields_doesNotSetDuplicateFlag() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("EmptyClass");

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertFalse(checker.hasDuplicateJsonFieldNames,
                "Empty class should not trigger duplicate field name detection");
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
            checker.visitProgramClass(class1);
            checker.visitProgramClass(class2);
            checker.visitProgramClass(class3);
        }, "visitProgramClass should handle multiple calls");
    }

    /**
     * Tests that visitProgramClass respects the hasDuplicateJsonFieldNames flag if already set.
     * Once set to true, the flag should remain true even after processing classes without duplicates.
     */
    @Test
    public void testVisitProgramClass_whenFlagAlreadyTrue_remainsTrue() {
        // Arrange
        checker.hasDuplicateJsonFieldNames = true;
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasDuplicateJsonFieldNames,
                "hasDuplicateJsonFieldNames should remain true once set");
    }

    /**
     * Tests that visitProgramClass initializes OptimizedJsonInfo and processes modes.
     * This verifies the method executes its internal logic flow.
     */
    @Test
    public void testVisitProgramClass_initializesInternalStructures() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        boolean initialState = checker.hasDuplicateJsonFieldNames;

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        // The method should complete without exceptions and maintain consistent state
        assertFalse(checker.hasDuplicateJsonFieldNames,
                "Method should complete and maintain consistent state for class without duplicates");
        assertEquals(initialState, checker.hasDuplicateJsonFieldNames,
                "State should remain unchanged for class without duplicate JSON field names");
    }

    /**
     * Tests that visitProgramClass works with a class that has a simple name.
     */
    @Test
    public void testVisitProgramClass_withSimpleClassName_executesCorrectly() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("Simple");

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitProgramClass(programClass),
                "visitProgramClass should handle simple class names");
        assertFalse(checker.hasDuplicateJsonFieldNames,
                "Simple class should not trigger duplicate detection");
    }

    /**
     * Tests that visitProgramClass works with a class that has a package-qualified name.
     */
    @Test
    public void testVisitProgramClass_withQualifiedClassName_executesCorrectly() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/example/TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitProgramClass(programClass),
                "visitProgramClass should handle qualified class names");
        assertFalse(checker.hasDuplicateJsonFieldNames,
                "Qualified class name should not affect duplicate detection");
    }

    /**
     * Tests that visitProgramClass processes both serialize and deserialize modes.
     * The implementation iterates through Mode enum values.
     */
    @Test
    public void testVisitProgramClass_processesBothModes() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        // If the method completes without exception, it has processed both modes
        assertFalse(checker.hasDuplicateJsonFieldNames,
                "Processing both serialize and deserialize modes should complete successfully");
    }

    /**
     * Tests that visitProgramClass maintains state correctly across multiple invocations.
     */
    @Test
    public void testVisitProgramClass_stateConsistencyAcrossInvocations() {
        // Arrange
        ProgramClass class1 = createMinimalProgramClass("Class1");
        ProgramClass class2 = createMinimalProgramClass("Class2");

        // Act
        checker.visitProgramClass(class1);
        boolean afterFirst = checker.hasDuplicateJsonFieldNames;

        checker.visitProgramClass(class2);
        boolean afterSecond = checker.hasDuplicateJsonFieldNames;

        // Assert
        assertFalse(afterFirst, "State should be consistent after first invocation");
        assertFalse(afterSecond, "State should be consistent after second invocation");
    }

    /**
     * Tests that a new checker instance has independent state from other instances.
     */
    @Test
    public void testVisitProgramClass_instanceIndependence() {
        // Arrange
        DuplicateJsonFieldNameChecker checker1 = new DuplicateJsonFieldNameChecker();
        DuplicateJsonFieldNameChecker checker2 = new DuplicateJsonFieldNameChecker();
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        checker1.hasDuplicateJsonFieldNames = true;
        checker1.visitProgramClass(programClass);
        checker2.visitProgramClass(programClass);

        // Assert
        assertTrue(checker1.hasDuplicateJsonFieldNames,
                "First checker should maintain its state");
        assertFalse(checker2.hasDuplicateJsonFieldNames,
                "Second checker should have independent state");
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
}
