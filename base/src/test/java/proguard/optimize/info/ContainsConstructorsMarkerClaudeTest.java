package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ContainsConstructorsMarker}.
 *
 * This class is a MemberVisitor that marks program classes as containing constructors
 * when it encounters methods named "<init>". The tests cover:
 * - Constructor initialization
 * - visitAnyMember (no-op method)
 * - visitProgramMethod (marks classes with constructors)
 * - containsConstructors (static helper method to check if a class contains constructors)
 *
 * The marker uses ProgramClassOptimizationInfo to store the containsConstructors flag.
 */
public class ContainsConstructorsMarkerClaudeTest {

    private ContainsConstructorsMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new ContainsConstructorsMarker();
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a ContainsConstructorsMarker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        ContainsConstructorsMarker newMarker = new ContainsConstructorsMarker();

        // Assert
        assertNotNull(newMarker, "ContainsConstructorsMarker instance should be created");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        ContainsConstructorsMarker marker1 = new ContainsConstructorsMarker();
        ContainsConstructorsMarker marker2 = new ContainsConstructorsMarker();
        ContainsConstructorsMarker marker3 = new ContainsConstructorsMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "First and second marker should be different instances");
        assertNotSame(marker2, marker3, "Second and third marker should be different instances");
        assertNotSame(marker1, marker3, "First and third marker should be different instances");
    }

    // =========================================================================
    // visitAnyMember Tests
    // =========================================================================

    /**
     * Tests that visitAnyMember does nothing (is a no-op).
     * This method is part of the MemberVisitor interface but not used by this marker.
     */
    @Test
    public void testVisitAnyMember_doesNothing() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod method = createMethodWithName(programClass, "regularMethod", "()V");

        // Verify initial state
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not contain constructors initially");

        // Act
        marker.visitAnyMember(programClass, method);

        // Assert - state should not change
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "visitAnyMember should not mark class as containing constructors");
    }

    /**
     * Tests that visitAnyMember does not throw an exception.
     */
    @Test
    public void testVisitAnyMember_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramMethod method = createMethodWithName(programClass, "someMethod", "()V");

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyMember(programClass, method),
                "visitAnyMember should not throw an exception");
    }

    /**
     * Tests that visitAnyMember can be called with null parameters.
     */
    @Test
    public void testVisitAnyMember_withNullParameters_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyMember(null, null),
                "visitAnyMember should handle null parameters gracefully");
    }

    // =========================================================================
    // visitProgramMethod Tests - Constructor Detection
    // =========================================================================

    /**
     * Tests that visitProgramMethod marks a class when visiting a constructor method.
     */
    @Test
    public void testVisitProgramMethod_withConstructor_marksClass() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod constructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");

        // Verify initial state
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not contain constructors initially");

        // Act
        marker.visitProgramMethod(programClass, constructor);

        // Assert
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should be marked as containing constructors after visiting constructor");
    }

    /**
     * Tests that visitProgramMethod does not mark a class when visiting a regular method.
     */
    @Test
    public void testVisitProgramMethod_withRegularMethod_doesNotMarkClass() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod regularMethod = createMethodWithName(programClass, "doSomething", "()V");

        // Verify initial state
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not contain constructors initially");

        // Act
        marker.visitProgramMethod(programClass, regularMethod);

        // Assert
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not be marked as containing constructors for regular methods");
    }

    /**
     * Tests that visitProgramMethod can be called multiple times on the same constructor.
     */
    @Test
    public void testVisitProgramMethod_calledMultipleTimes_remainsMarked() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod constructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");

        // Act - call multiple times
        marker.visitProgramMethod(programClass, constructor);
        marker.visitProgramMethod(programClass, constructor);
        marker.visitProgramMethod(programClass, constructor);

        // Assert - should still be marked
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should remain marked as containing constructors after multiple visits");
    }

    /**
     * Tests that visitProgramMethod marks multiple different classes correctly.
     */
    @Test
    public void testVisitProgramMethod_multipleDifferentClasses_independentMarking() {
        // Arrange
        ProgramClass class1 = createProgramClassWithConstantPool();
        ProgramClass class2 = createProgramClassWithConstantPool();
        ProgramClass class3 = createProgramClassWithConstantPool();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        ProgramMethod constructor1 = createMethodWithName(class1, ClassConstants.METHOD_NAME_INIT, "()V");
        ProgramMethod regularMethod = createMethodWithName(class2, "regularMethod", "()V");
        ProgramMethod constructor3 = createMethodWithName(class3, ClassConstants.METHOD_NAME_INIT, "(I)V");

        // Act
        marker.visitProgramMethod(class1, constructor1);
        marker.visitProgramMethod(class2, regularMethod);
        marker.visitProgramMethod(class3, constructor3);

        // Assert
        assertTrue(ContainsConstructorsMarker.containsConstructors(class1),
                "Class1 should be marked as containing constructors");
        assertFalse(ContainsConstructorsMarker.containsConstructors(class2),
                "Class2 should not be marked as containing constructors");
        assertTrue(ContainsConstructorsMarker.containsConstructors(class3),
                "Class3 should be marked as containing constructors");
    }

    /**
     * Tests that visitProgramMethod recognizes constructors with different descriptors.
     */
    @Test
    public void testVisitProgramMethod_constructorsWithDifferentDescriptors_allRecognized() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramMethod defaultConstructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");
        ProgramMethod parameterizedConstructor1 = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "(I)V");
        ProgramMethod parameterizedConstructor2 = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "(Ljava/lang/String;)V");

        // Act - visit any one constructor
        marker.visitProgramMethod(programClass, parameterizedConstructor1);

        // Assert - should be marked after visiting any constructor
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should be marked when visiting any constructor regardless of descriptor");
    }

    /**
     * Tests that visitProgramMethod does not throw an exception.
     */
    @Test
    public void testVisitProgramMethod_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod constructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, constructor),
                "visitProgramMethod should not throw an exception");
    }

    /**
     * Tests that visitProgramMethod works correctly with method names that are similar but not constructors.
     */
    @Test
    public void testVisitProgramMethod_similarButNotConstructor_notMarked() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Create methods with names similar to but not exactly "<init>"
        ProgramMethod initMethod = createMethodWithName(programClass, "init", "()V");
        ProgramMethod _initMethod = createMethodWithName(programClass, "_init", "()V");
        ProgramMethod initUnderscoreMethod = createMethodWithName(programClass, "init_", "()V");

        // Act - visit all similar methods
        marker.visitProgramMethod(programClass, initMethod);
        marker.visitProgramMethod(programClass, _initMethod);
        marker.visitProgramMethod(programClass, initUnderscoreMethod);

        // Assert - should not be marked since none are actual constructors
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not be marked for methods with similar but non-matching names");
    }

    /**
     * Tests that the static initializer method does not mark a class as containing constructors.
     */
    @Test
    public void testVisitProgramMethod_staticInitializer_notMarked() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod staticInitializer = createMethodWithName(programClass, ClassConstants.METHOD_NAME_CLINIT, "()V");

        // Act
        marker.visitProgramMethod(programClass, staticInitializer);

        // Assert
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not be marked for static initializers");
    }

    /**
     * Tests that multiple marker instances can mark the same class.
     */
    @Test
    public void testVisitProgramMethod_multipleMarkerInstances_sameBehavior() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod constructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");

        ContainsConstructorsMarker marker1 = new ContainsConstructorsMarker();
        ContainsConstructorsMarker marker2 = new ContainsConstructorsMarker();

        // Act - use first marker
        marker1.visitProgramMethod(programClass, constructor);

        // Assert - verify marked
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should be marked after first marker visits");

        // Act - use second marker (should have no additional effect)
        marker2.visitProgramMethod(programClass, constructor);

        // Assert - should still be marked
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should still be marked after second marker visits");
    }

    /**
     * Tests that visitProgramMethod is stateless and independent across different classes.
     */
    @Test
    public void testVisitProgramMethod_statelessBehavior() {
        // Arrange
        ProgramClass class1 = createProgramClassWithConstantPool();
        ProgramClass class2 = createProgramClassWithConstantPool();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        ProgramMethod constructor1 = createMethodWithName(class1, ClassConstants.METHOD_NAME_INIT, "()V");
        ProgramMethod regularMethod2 = createMethodWithName(class2, "regularMethod", "()V");

        // Act - mark only class1
        marker.visitProgramMethod(class1, constructor1);

        // Assert - only class1 should be marked
        assertTrue(ContainsConstructorsMarker.containsConstructors(class1),
                "Class1 should be marked as containing constructors");
        assertFalse(ContainsConstructorsMarker.containsConstructors(class2),
                "Class2 should not be marked");

        // Act - now mark class2 with a constructor
        ProgramMethod constructor2 = createMethodWithName(class2, ClassConstants.METHOD_NAME_INIT, "()V");
        marker.visitProgramMethod(class2, constructor2);

        // Assert - both should be marked now
        assertTrue(ContainsConstructorsMarker.containsConstructors(class1),
                "Class1 should still be marked");
        assertTrue(ContainsConstructorsMarker.containsConstructors(class2),
                "Class2 should now be marked");
    }

    // =========================================================================
    // containsConstructors Static Method Tests
    // =========================================================================

    /**
     * Tests that containsConstructors returns false for a class that hasn't been marked.
     */
    @Test
    public void testContainsConstructors_unmarkedClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "containsConstructors should return false for unmarked class");
    }

    /**
     * Tests that containsConstructors returns true for a class that has been marked.
     */
    @Test
    public void testContainsConstructors_markedClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod constructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");

        // Act - mark the class
        marker.visitProgramMethod(programClass, constructor);

        // Assert
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "containsConstructors should return true for marked class");
    }

    /**
     * Tests that containsConstructors works with LibraryClass.
     */
    @Test
    public void testContainsConstructors_withLibraryClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert
        // LibraryClass uses ClassOptimizationInfo which always returns true for containsConstructors
        assertTrue(ContainsConstructorsMarker.containsConstructors(libraryClass),
                "containsConstructors should return true for LibraryClass (conservative default)");
    }

    /**
     * Tests that containsConstructors persists after multiple checks.
     */
    @Test
    public void testContainsConstructors_persistsAfterMultipleChecks() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramMethod constructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");

        // Act - mark the class
        marker.visitProgramMethod(programClass, constructor);

        // Assert - check multiple times
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "containsConstructors should return true - first check");
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "containsConstructors should return true - second check");
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "containsConstructors should return true - third check");
    }

    /**
     * Tests that containsConstructors is independent for different classes.
     */
    @Test
    public void testContainsConstructors_independentForDifferentClasses() {
        // Arrange
        ProgramClass class1 = createProgramClassWithConstantPool();
        ProgramClass class2 = createProgramClassWithConstantPool();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        ProgramMethod constructor = createMethodWithName(class1, ClassConstants.METHOD_NAME_INIT, "()V");

        // Act - mark only class1
        marker.visitProgramMethod(class1, constructor);

        // Assert
        assertTrue(ContainsConstructorsMarker.containsConstructors(class1),
                "Class1 should contain constructors");
        assertFalse(ContainsConstructorsMarker.containsConstructors(class2),
                "Class2 should not contain constructors");
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests the complete workflow: create marker, visit methods, check status.
     */
    @Test
    public void testIntegration_completeWorkflow() {
        // Arrange
        ProgramClass programClass = createProgramClassWithConstantPool();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramMethod constructor = createMethodWithName(programClass, ClassConstants.METHOD_NAME_INIT, "()V");
        ProgramMethod method1 = createMethodWithName(programClass, "toString", "()Ljava/lang/String;");
        ProgramMethod method2 = createMethodWithName(programClass, "getValue", "()I");

        ContainsConstructorsMarker marker = new ContainsConstructorsMarker();

        // Verify initial state
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not contain constructors initially");

        // Act - visit regular methods first
        marker.visitProgramMethod(programClass, method1);
        marker.visitProgramMethod(programClass, method2);

        // Assert - still not marked
        assertFalse(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should not be marked after visiting regular methods");

        // Act - visit constructor
        marker.visitProgramMethod(programClass, constructor);

        // Assert - now marked
        assertTrue(ContainsConstructorsMarker.containsConstructors(programClass),
                "Class should be marked after visiting constructor");
    }

    /**
     * Tests concurrent access from multiple threads.
     */
    @Test
    public void testConcurrentAccess_multipleThreads() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int classesPerThread = 10;
        Thread[] threads = new Thread[threadCount];
        final ProgramClass[][] classes = new ProgramClass[threadCount][classesPerThread];

        // Initialize all classes
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                classes[i][j] = createProgramClassWithConstantPool();
                ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i][j]);
            }
        }

        // Act - mark classes in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                ContainsConstructorsMarker threadMarker = new ContainsConstructorsMarker();
                for (int j = 0; j < classesPerThread; j++) {
                    ProgramMethod constructor = createMethodWithName(
                            classes[threadIndex][j],
                            ClassConstants.METHOD_NAME_INIT,
                            "()V");
                    threadMarker.visitProgramMethod(classes[threadIndex][j], constructor);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all classes should be marked
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                assertTrue(ContainsConstructorsMarker.containsConstructors(classes[i][j]),
                        "Class [" + i + "][" + j + "] should be marked as containing constructors");
            }
        }
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Creates a ProgramClass with a minimal constant pool setup.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[100];
        constantPool[0] = null; // Index 0 is always null
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 100;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithName(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Finds the next available index in the constant pool.
     */
    private int findNextAvailableConstantPoolIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("No available constant pool index");
    }
}
