package proguard.optimize.info;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SideEffectInstructionChecker} constructor.
 *
 * The SideEffectInstructionChecker constructor takes three boolean parameters:
 * - includeReturnInstructions: whether return instructions count as side effects
 * - includeArrayStoreInstructions: whether storing values in arrays counts as side effects
 * - includeBuiltInExceptions: whether built-in exceptions count as side effects
 */
public class SideEffectInstructionCheckerClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with all parameters set to false.
     */
    @Test
    public void testConstructor_allFalse() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with all parameters set to true.
     */
    @Test
    public void testConstructor_allTrue() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with only includeReturnInstructions set to true.
     */
    @Test
    public void testConstructor_onlyIncludeReturnInstructions() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with only includeArrayStoreInstructions set to true.
     */
    @Test
    public void testConstructor_onlyIncludeArrayStoreInstructions() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with only includeBuiltInExceptions set to true.
     */
    @Test
    public void testConstructor_onlyIncludeBuiltInExceptions() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with includeReturnInstructions and includeArrayStoreInstructions set to true.
     */
    @Test
    public void testConstructor_returnAndArrayStore() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, false);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with includeReturnInstructions and includeBuiltInExceptions set to true.
     */
    @Test
    public void testConstructor_returnAndBuiltInExceptions() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, true);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates a SideEffectInstructionChecker instance
     * with includeArrayStoreInstructions and includeBuiltInExceptions set to true.
     */
    @Test
    public void testConstructor_arrayStoreAndBuiltInExceptions() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, true);

        // Assert
        assertNotNull(checker, "SideEffectInstructionChecker instance should be created");
    }

    /**
     * Tests that multiple instances can be created independently with different parameters.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(true, false, false);
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(false, true, false);
        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(false, false, true);

        // Assert
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotNull(checker3, "Third checker should be created");
        assertNotSame(checker1, checker2, "First and second checkers should be different instances");
        assertNotSame(checker2, checker3, "Second and third checkers should be different instances");
        assertNotSame(checker1, checker3, "First and third checkers should be different instances");
    }

    /**
     * Tests that the constructor completes quickly without expensive initialization.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - create many instances
        for (int i = 0; i < 1000; i++) {
            new SideEffectInstructionChecker(i % 2 == 0, i % 3 == 0, i % 5 == 0);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 instances)
        assertTrue(durationMs < 100, "Constructor should execute quickly with minimal overhead");
    }

    /**
     * Tests that the constructor is thread-safe and can be called concurrently.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int instancesPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final SideEffectInstructionChecker[][] results = new SideEffectInstructionChecker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new SideEffectInstructionChecker(
                        threadIndex % 2 == 0,
                        j % 2 == 0,
                        (threadIndex + j) % 2 == 0
                    );
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < instancesPerThread; j++) {
                assertNotNull(results[i][j],
                        "Instance [" + i + "][" + j + "] should be created");
            }
        }
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(true, true, true);
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(true, true, true);

        // Assert
        assertEquals(checker1.getClass(), checker2.getClass(),
                "Both instances should be of the same class");
        assertNotSame(checker1, checker2,
                "Instances should be separate objects in memory");
    }

    /**
     * Tests that the constructor can be called repeatedly in quick succession.
     */
    @Test
    public void testConstructor_rapidSuccession() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                new SideEffectInstructionChecker(i % 2 == 0, i % 3 == 0, i % 5 == 0);
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed checkers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<SideEffectInstructionChecker> checkers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            checkers.add(new SideEffectInstructionChecker(i % 2 == 0, i % 3 == 0, i % 5 == 0));
        }

        // Assert - all instances should be distinct
        assertEquals(10, checkers.size(),
                "All 10 instances should be distinct and stored in the set");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new SideEffectInstructionChecker(true, false, true),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that constructed instances are of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);

        // Assert
        assertInstanceOf(SideEffectInstructionChecker.class, checker,
                "Instance should be of type SideEffectInstructionChecker");
    }

    /**
     * Tests that the constructor creates instances with Object as superclass.
     */
    @Test
    public void testConstructor_extendsObject() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, false);

        // Assert
        assertInstanceOf(Object.class, checker,
                "SideEffectInstructionChecker should extend Object");
        assertEquals(Object.class, checker.getClass().getSuperclass(),
                "SideEffectInstructionChecker should have Object as direct superclass");
    }

    /**
     * Tests that the constructor implements the expected visitor interfaces.
     */
    @Test
    public void testConstructor_implementsVisitorInterfaces() {
        // Act
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);

        // Assert
        assertInstanceOf(proguard.classfile.instruction.visitor.InstructionVisitor.class, checker,
                "Instance should implement InstructionVisitor");
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, checker,
                "Instance should implement ConstantVisitor");
        assertInstanceOf(proguard.classfile.visitor.MemberVisitor.class, checker,
                "Instance should implement MemberVisitor");
    }
}
