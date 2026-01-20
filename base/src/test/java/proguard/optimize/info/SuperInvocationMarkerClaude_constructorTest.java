package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SuperInvocationMarker} default constructor.
 *
 * The SuperInvocationMarker is an InstructionVisitor and ConstantVisitor that marks
 * methods invoking super methods (other than initializers).
 *
 * The default constructor initializes the instance with default field values.
 */
public class SuperInvocationMarkerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates an instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        SuperInvocationMarker marker = new SuperInvocationMarker();

        // Assert
        assertNotNull(marker, "SuperInvocationMarker instance should be created");
    }

    /**
     * Tests that the constructor creates an instance that is an InstructionVisitor.
     */
    @Test
    public void testConstructor_createsInstructionVisitor() {
        // Act
        SuperInvocationMarker marker = new SuperInvocationMarker();

        // Assert
        assertInstanceOf(InstructionVisitor.class, marker,
                "SuperInvocationMarker should be an instance of InstructionVisitor");
    }

    /**
     * Tests that the constructor creates an instance that is a ConstantVisitor.
     */
    @Test
    public void testConstructor_createsConstantVisitor() {
        // Act
        SuperInvocationMarker marker = new SuperInvocationMarker();

        // Assert
        assertInstanceOf(ConstantVisitor.class, marker,
                "SuperInvocationMarker should be an instance of ConstantVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        SuperInvocationMarker marker1 = new SuperInvocationMarker();
        SuperInvocationMarker marker2 = new SuperInvocationMarker();
        SuperInvocationMarker marker3 = new SuperInvocationMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances");
        assertNotSame(marker2, marker3, "Markers should be different instances");
        assertNotSame(marker1, marker3, "Markers should be different instances");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new SuperInvocationMarker(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that constructed instances are usable as InstructionVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableAsInstructionVisitorImmediately() {
        // Arrange
        SuperInvocationMarker marker = new SuperInvocationMarker();
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        Instruction instruction = new SimpleInstruction();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 0, instruction),
                "Newly constructed marker should be usable as an InstructionVisitor");
    }

    /**
     * Tests that the constructor creates instances that are separate objects.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        SuperInvocationMarker marker1 = new SuperInvocationMarker();
        SuperInvocationMarker marker2 = new SuperInvocationMarker();

        // Assert
        assertEquals(marker1.getClass(), marker2.getClass(),
                "Both instances should be of the same class");
        assertNotSame(marker1, marker2,
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
                new SuperInvocationMarker();
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed markers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<SuperInvocationMarker> markers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            markers.add(new SuperInvocationMarker());
        }

        // Assert - all instances should be distinct
        assertEquals(10, markers.size(),
                "All 10 instances should be distinct and stored in the set");
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
            new SuperInvocationMarker();
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
        final SuperInvocationMarker[][] results = new SuperInvocationMarker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new SuperInvocationMarker();
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
     * Tests that the constructor creates a new instance each time it's called,
     * ensuring no singleton pattern or caching is applied.
     */
    @Test
    public void testConstructor_noSingletonBehavior() {
        // Act
        SuperInvocationMarker marker1 = new SuperInvocationMarker();
        SuperInvocationMarker marker2 = new SuperInvocationMarker();
        SuperInvocationMarker marker3 = new SuperInvocationMarker();
        SuperInvocationMarker marker4 = new SuperInvocationMarker();

        // Assert - all should be different instances
        assertNotSame(marker1, marker2, "Constructor should not return cached instance");
        assertNotSame(marker3, marker4, "Constructor should not return cached instance");
        assertNotSame(marker1, marker3, "Constructor should not return cached instance");
        assertNotSame(marker2, marker4, "Constructor should not return cached instance");
    }

    /**
     * Tests that instances created concurrently are all distinct.
     */
    @Test
    public void testConstructor_concurrentCreation_allDistinct() throws InterruptedException {
        // Arrange
        final int threadCount = 20;
        Thread[] threads = new Thread[threadCount];
        final SuperInvocationMarker[] results = new SuperInvocationMarker[threadCount];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                results[threadIndex] = new SuperInvocationMarker();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            assertNotNull(results[i], "Instance " + i + " should be created");
        }

        // Verify all instances are unique
        java.util.Set<SuperInvocationMarker> uniqueMarkers = new java.util.HashSet<>();
        for (SuperInvocationMarker marker : results) {
            uniqueMarkers.add(marker);
        }
        assertEquals(threadCount, uniqueMarkers.size(),
                "All " + threadCount + " instances should be unique");
    }

    /**
     * Tests that the constructor allows immediate use for visiting instructions.
     */
    @Test
    public void testConstructor_immediatelyUsableForVisiting() {
        // Arrange
        SuperInvocationMarker marker = new SuperInvocationMarker();
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert - should be able to visit multiple instructions without issues
        assertDoesNotThrow(() -> {
            marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 0, new SimpleInstruction());
            marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 1, new SimpleInstruction());
            marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 2, new SimpleInstruction());
        }, "Newly constructed marker should support visiting multiple instructions");
    }

    /**
     * Tests that markers behave as visitor pattern implementations.
     * Each marker can visit multiple instructions independently.
     */
    @Test
    public void testConstructor_supportsVisitorPattern() {
        // Arrange
        SuperInvocationMarker marker = new SuperInvocationMarker();
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        Instruction instruction1 = new SimpleInstruction();
        Instruction instruction2 = new SimpleInstruction();

        // Act & Assert - should be able to visit multiple instructions across different contexts
        assertDoesNotThrow(() -> {
            marker.visitAnyInstruction(class1, method1, codeAttribute1, 0, instruction1);
            marker.visitAnyInstruction(class1, method1, codeAttribute1, 1, instruction2);
            marker.visitAnyInstruction(class2, method2, codeAttribute2, 0, instruction1);
        }, "Marker should support visiting multiple instructions as per visitor pattern");
    }

    /**
     * Tests that constructed markers can be used in a collection-based workflow.
     */
    @Test
    public void testConstructor_worksInCollectionBasedWorkflow() {
        // Arrange
        java.util.List<SuperInvocationMarker> markers = new java.util.ArrayList<>();

        // Act - create multiple markers and add to list
        for (int i = 0; i < 5; i++) {
            markers.add(new SuperInvocationMarker());
        }

        // Assert - all markers should be usable
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        Instruction instruction = new SimpleInstruction();

        for (SuperInvocationMarker marker : markers) {
            assertDoesNotThrow(() -> marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 0, instruction),
                    "Each marker in collection should be usable");
        }
    }

    /**
     * Tests that multiple markers created in sequence are independent.
     */
    @Test
    public void testConstructor_sequentialCreation_independentInstances() {
        // Arrange & Act
        SuperInvocationMarker marker1 = new SuperInvocationMarker();
        SuperInvocationMarker marker2 = new SuperInvocationMarker();
        SuperInvocationMarker marker3 = new SuperInvocationMarker();
        SuperInvocationMarker marker4 = new SuperInvocationMarker();
        SuperInvocationMarker marker5 = new SuperInvocationMarker();

        // Assert - all should be distinct
        java.util.Set<SuperInvocationMarker> uniqueMarkers = new java.util.HashSet<>();
        uniqueMarkers.add(marker1);
        uniqueMarkers.add(marker2);
        uniqueMarkers.add(marker3);
        uniqueMarkers.add(marker4);
        uniqueMarkers.add(marker5);

        assertEquals(5, uniqueMarkers.size(),
                "All sequentially created markers should be independent instances");
    }

    /**
     * Tests that the constructor handles being called in a loop with immediate usage.
     */
    @Test
    public void testConstructor_loopCreationWithImmediateUsage() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        Instruction instruction = new SimpleInstruction();

        // Act & Assert - create and use markers in a loop
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                SuperInvocationMarker marker = new SuperInvocationMarker();
                marker.visitAnyInstruction(programClass, programMethod, codeAttribute, i, instruction);
            }
        }, "Constructor should support loop-based creation and immediate usage");
    }

    /**
     * Tests that markers can be created and used across different method contexts.
     */
    @Test
    public void testConstructor_usableAcrossDifferentContexts() {
        // Arrange
        SuperInvocationMarker marker = new SuperInvocationMarker();

        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();

        Instruction instruction = new SimpleInstruction();

        // Act & Assert - should work across different contexts
        assertDoesNotThrow(() -> {
            marker.visitAnyInstruction(class1, method1, codeAttribute1, 0, instruction);
            marker.visitAnyInstruction(class1, method2, codeAttribute1, 0, instruction);
            marker.visitAnyInstruction(class2, method1, codeAttribute2, 0, instruction);
            marker.visitAnyInstruction(class2, method3, codeAttribute2, 0, instruction);
        }, "Marker should be usable across different class and method contexts");
    }

    /**
     * Tests that the constructor creates instances that are all of the same type.
     */
    @Test
    public void testConstructor_allInstancesSameType() {
        // Act
        SuperInvocationMarker marker1 = new SuperInvocationMarker();
        SuperInvocationMarker marker2 = new SuperInvocationMarker();
        SuperInvocationMarker marker3 = new SuperInvocationMarker();

        // Assert
        assertEquals(SuperInvocationMarker.class, marker1.getClass());
        assertEquals(SuperInvocationMarker.class, marker2.getClass());
        assertEquals(SuperInvocationMarker.class, marker3.getClass());
        assertEquals(marker1.getClass(), marker2.getClass());
        assertEquals(marker2.getClass(), marker3.getClass());
    }

    /**
     * Tests that the constructor initializes the marker in a valid state for constant visiting.
     */
    @Test
    public void testConstructor_validForConstantVisiting() {
        // Arrange
        SuperInvocationMarker marker = new SuperInvocationMarker();

        // Assert - marker should be a ConstantVisitor and ready to use
        assertInstanceOf(ConstantVisitor.class, marker,
                "Marker should be a ConstantVisitor");

        // The marker can be used as a ConstantVisitor without throwing exceptions
        // (actual usage requires proper constant setup, but the instance is valid)
        assertNotNull(marker, "Marker should be ready for constant visiting");
    }

    /**
     * Tests that markers created in rapid succession maintain independence.
     */
    @Test
    public void testConstructor_rapidCreation_maintainsIndependence() {
        // Arrange
        java.util.List<SuperInvocationMarker> markers = new java.util.ArrayList<>();

        // Act - create many markers rapidly
        for (int i = 0; i < 100; i++) {
            markers.add(new SuperInvocationMarker());
        }

        // Assert - all should be distinct
        java.util.Set<SuperInvocationMarker> uniqueMarkers = new java.util.HashSet<>(markers);
        assertEquals(100, uniqueMarkers.size(),
                "All 100 rapidly created markers should be independent");
    }

    /**
     * Tests that the constructor doesn't throw when invoked multiple times in nested loops.
     */
    @Test
    public void testConstructor_nestedLoopCreation() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    new SuperInvocationMarker();
                }
            }
        }, "Constructor should handle nested loop creation");
    }

    /**
     * Tests that instances can be assigned to InstructionVisitor interface variables.
     */
    @Test
    public void testConstructor_assignableToInstructionVisitor() {
        // Act
        InstructionVisitor visitor = new SuperInvocationMarker();

        // Assert
        assertNotNull(visitor, "Marker should be assignable to InstructionVisitor");
        assertInstanceOf(SuperInvocationMarker.class, visitor,
                "Assigned visitor should still be a SuperInvocationMarker");
    }

    /**
     * Tests that instances can be assigned to ConstantVisitor interface variables.
     */
    @Test
    public void testConstructor_assignableToConstantVisitor() {
        // Act
        ConstantVisitor visitor = new SuperInvocationMarker();

        // Assert
        assertNotNull(visitor, "Marker should be assignable to ConstantVisitor");
        assertInstanceOf(SuperInvocationMarker.class, visitor,
                "Assigned visitor should still be a SuperInvocationMarker");
    }

    /**
     * Tests that the constructor works correctly when called from different methods.
     */
    @Test
    public void testConstructor_worksFromDifferentMethods() {
        // Act
        SuperInvocationMarker marker1 = createMarkerHelper();
        SuperInvocationMarker marker2 = createAnotherMarkerHelper();

        // Assert
        assertNotNull(marker1, "Marker from helper method 1 should be created");
        assertNotNull(marker2, "Marker from helper method 2 should be created");
        assertNotSame(marker1, marker2, "Markers from different methods should be distinct");
    }

    // Helper methods for the above test
    private SuperInvocationMarker createMarkerHelper() {
        return new SuperInvocationMarker();
    }

    private SuperInvocationMarker createAnotherMarkerHelper() {
        return new SuperInvocationMarker();
    }
}
