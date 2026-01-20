package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SideEffectMethodMarker} constructor:
 * SideEffectMethodMarker(MemberVisitor, boolean).
 *
 * This is the two-parameter constructor that:
 * - Takes an optional MemberVisitor (extraMemberVisitor) that can be null
 * - Takes a boolean (optimizeConservatively) parameter
 * - Stores the extraMemberVisitor field
 * - Creates a SideEffectInstructionChecker with (false, true, optimizeConservatively)
 */
public class SideEffectMethodMarkerClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates an instance with null visitor and false parameter.
     */
    @Test
    public void testConstructor_withNullVisitorAndFalse_createsInstance() {
        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, false);

        // Assert
        assertNotNull(marker, "SideEffectMethodMarker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates an instance with null visitor and true parameter.
     */
    @Test
    public void testConstructor_withNullVisitorAndTrue_createsInstance() {
        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, true);

        // Assert
        assertNotNull(marker, "SideEffectMethodMarker instance should be created");
    }

    /**
     * Tests that the constructor successfully creates an instance with a non-null visitor and false parameter.
     */
    @Test
    public void testConstructor_withVisitorAndFalse_createsInstance() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(extraVisitor, false);

        // Assert
        assertNotNull(marker, "SideEffectMethodMarker instance should be created with visitor");
    }

    /**
     * Tests that the constructor successfully creates an instance with a non-null visitor and true parameter.
     */
    @Test
    public void testConstructor_withVisitorAndTrue_createsInstance() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(extraVisitor, true);

        // Assert
        assertNotNull(marker, "SideEffectMethodMarker instance should be created with visitor");
    }

    /**
     * Tests that the constructed SideEffectMethodMarker is an instance of MemberVisitor.
     */
    @Test
    public void testConstructor_createsMemberVisitor() {
        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, false);

        // Assert
        assertInstanceOf(MemberVisitor.class, marker,
                "SideEffectMethodMarker should be an instance of MemberVisitor");
    }

    /**
     * Tests that the constructed SideEffectMethodMarker is an instance of InstructionVisitor.
     */
    @Test
    public void testConstructor_createsInstructionVisitor() {
        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, true);

        // Assert
        assertInstanceOf(InstructionVisitor.class, marker,
                "SideEffectMethodMarker should be an instance of InstructionVisitor");
    }

    /**
     * Tests that multiple instances can be created independently with same parameters.
     */
    @Test
    public void testConstructor_createsMultipleInstancesWithSameParameters() {
        // Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(null, false);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(null, false);
        SideEffectMethodMarker marker3 = new SideEffectMethodMarker(null, false);

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances");
        assertNotSame(marker2, marker3, "Markers should be different instances");
        assertNotSame(marker1, marker3, "Markers should be different instances");
    }

    /**
     * Tests that multiple instances can be created with different parameter combinations.
     */
    @Test
    public void testConstructor_createsMultipleInstancesWithDifferentParameters() {
        // Arrange
        MemberVisitor visitor1 = new NoSideEffectMethodMarker();
        MemberVisitor visitor2 = new NoExternalSideEffectMethodMarker();

        // Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(null, false);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(null, true);
        SideEffectMethodMarker marker3 = new SideEffectMethodMarker(visitor1, false);
        SideEffectMethodMarker marker4 = new SideEffectMethodMarker(visitor2, true);

        // Assert
        assertNotNull(marker1, "Marker with (null, false) should be created");
        assertNotNull(marker2, "Marker with (null, true) should be created");
        assertNotNull(marker3, "Marker with (visitor, false) should be created");
        assertNotNull(marker4, "Marker with (visitor, true) should be created");
    }

    /**
     * Tests that the constructor completes quickly without expensive initialization.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();
        long startTime = System.nanoTime();

        // Act - create many instances with various parameter combinations
        for (int i = 0; i < 1000; i++) {
            new SideEffectMethodMarker(i % 2 == 0 ? null : extraVisitor, i % 3 == 0);
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
        final SideEffectMethodMarker[][] results = new SideEffectMethodMarker[threadCount][instancesPerThread];
        final MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    MemberVisitor visitor = (threadIndex + j) % 2 == 0 ? null : extraVisitor;
                    results[threadIndex][j] = new SideEffectMethodMarker(visitor, threadIndex % 2 == 0);
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
     * Tests that constructed instances can be used as MemberVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableAsMemberVisitorImmediately() {
        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, false);

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> {
            ProgramClass programClass = new ProgramClass();
            ProgramMethod programMethod = new ProgramMethod();
            marker.visitProgramMethod(programClass, programMethod);
        }, "Newly constructed marker should be usable as a MemberVisitor");
    }

    /**
     * Tests that constructed instances with extra visitor can be used immediately.
     */
    @Test
    public void testConstructor_withExtraVisitor_instanceIsUsableImmediately() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(extraVisitor, true);

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> {
            ProgramClass programClass = new ProgramClass();
            ProgramMethod programMethod = new ProgramMethod();
            marker.visitProgramMethod(programClass, programMethod);
        }, "Marker with extra visitor should be usable immediately");
    }

    /**
     * Tests that constructed instances can be used as InstructionVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableAsInstructionVisitorImmediately() {
        // Act
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, true);

        // Assert - should be able to call instruction visitor methods without NPE
        assertDoesNotThrow(() -> {
            ProgramClass programClass = new ProgramClass();
            ProgramMethod programMethod = new ProgramMethod();
            CodeAttribute codeAttribute = new CodeAttribute();
            Instruction instruction = new SimpleInstruction();
            marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 0, instruction);
        }, "Newly constructed marker should be usable as an InstructionVisitor");
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(null, false);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(null, false);

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
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                new SideEffectMethodMarker(i % 2 == 0 ? null : extraVisitor, i % 2 == 0);
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed markers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<SideEffectMethodMarker> markers = new java.util.HashSet<>();
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            markers.add(new SideEffectMethodMarker(i % 2 == 0 ? null : extraVisitor, i % 2 == 0));
        }

        // Assert - all instances should be distinct
        assertEquals(10, markers.size(),
                "All 10 instances should be distinct and stored in the set");
    }

    /**
     * Tests that the constructor does not throw any exceptions with null visitor and false.
     */
    @Test
    public void testConstructor_withNullAndFalse_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new SideEffectMethodMarker(null, false),
                "Constructor should not throw any exception with (null, false)");
    }

    /**
     * Tests that the constructor does not throw any exceptions with null visitor and true.
     */
    @Test
    public void testConstructor_withNullAndTrue_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new SideEffectMethodMarker(null, true),
                "Constructor should not throw any exception with (null, true)");
    }

    /**
     * Tests that the constructor does not throw any exceptions with a visitor and false.
     */
    @Test
    public void testConstructor_withVisitorAndFalse_doesNotThrowException() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act & Assert
        assertDoesNotThrow(() -> new SideEffectMethodMarker(extraVisitor, false),
                "Constructor should not throw any exception with (visitor, false)");
    }

    /**
     * Tests that the constructor does not throw any exceptions with a visitor and true.
     */
    @Test
    public void testConstructor_withVisitorAndTrue_doesNotThrowException() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act & Assert
        assertDoesNotThrow(() -> new SideEffectMethodMarker(extraVisitor, true),
                "Constructor should not throw any exception with (visitor, true)");
    }

    /**
     * Tests that different visitor instances create different marker instances.
     */
    @Test
    public void testConstructor_withDifferentVisitors_createsDifferentInstances() {
        // Arrange
        MemberVisitor visitor1 = new NoSideEffectMethodMarker();
        MemberVisitor visitor2 = new NoExternalSideEffectMethodMarker();

        // Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(visitor1, false);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(visitor2, false);

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotSame(marker1, marker2, "Markers with different visitors should be different instances");
    }

    /**
     * Tests that the same visitor instance can be used to create multiple markers.
     */
    @Test
    public void testConstructor_withSameVisitor_createsMultipleMarkers() {
        // Arrange
        MemberVisitor sharedVisitor = new NoSideEffectMethodMarker();

        // Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(sharedVisitor, false);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(sharedVisitor, true);
        SideEffectMethodMarker marker3 = new SideEffectMethodMarker(sharedVisitor, false);

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances");
        assertNotSame(marker2, marker3, "Markers should be different instances");
        assertNotSame(marker1, marker3, "Markers should be different instances");
    }

    /**
     * Tests that a marker with null visitor can visit methods.
     */
    @Test
    public void testConstructor_withNullVisitor_canVisitProgramMethod() {
        // Arrange
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, false);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod),
                "Marker with null visitor should be able to visit program methods");
    }

    /**
     * Tests that a marker with non-null visitor can visit methods.
     */
    @Test
    public void testConstructor_withNonNullVisitor_canVisitProgramMethod() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();
        SideEffectMethodMarker marker = new SideEffectMethodMarker(extraVisitor, true);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod),
                "Marker with non-null visitor should be able to visit program methods");
    }

    /**
     * Tests that a marker created with false can visit instructions.
     */
    @Test
    public void testConstructor_withFalseParameter_canVisitAnyInstruction() {
        // Arrange
        SideEffectMethodMarker marker = new SideEffectMethodMarker(null, false);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        Instruction instruction = new SimpleInstruction();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 0, instruction),
                "Marker with optimizeConservatively=false should be able to visit instructions");
    }

    /**
     * Tests that a marker created with true can visit instructions.
     */
    @Test
    public void testConstructor_withTrueParameter_canVisitAnyInstruction() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();
        SideEffectMethodMarker marker = new SideEffectMethodMarker(extraVisitor, true);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        Instruction instruction = new SimpleInstruction();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> marker.visitAnyInstruction(programClass, programMethod, codeAttribute, 0, instruction),
                "Marker with optimizeConservatively=true should be able to visit instructions");
    }

    /**
     * Tests that markers behave as visitor pattern implementations.
     * Each marker can visit multiple methods independently.
     */
    @Test
    public void testConstructor_supportsVisitorPattern() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();
        SideEffectMethodMarker marker = new SideEffectMethodMarker(extraVisitor, false);
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        // Act & Assert - should be able to visit multiple methods
        assertDoesNotThrow(() -> {
            marker.visitProgramMethod(class1, method1);
            marker.visitProgramMethod(class1, method2);
            marker.visitProgramMethod(class2, method3);
        }, "Marker should support visiting multiple methods as per visitor pattern");
    }

    /**
     * Tests that the constructor creates a new instance each time it's called,
     * ensuring no singleton pattern or caching is applied.
     */
    @Test
    public void testConstructor_noSingletonBehavior() {
        // Arrange
        MemberVisitor visitor = new NoSideEffectMethodMarker();

        // Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(null, true);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(null, true);
        SideEffectMethodMarker marker3 = new SideEffectMethodMarker(visitor, false);
        SideEffectMethodMarker marker4 = new SideEffectMethodMarker(visitor, false);

        // Assert - all should be different instances
        assertNotSame(marker1, marker2, "Constructor should not return cached instance");
        assertNotSame(marker3, marker4, "Constructor should not return cached instance");
        assertNotSame(marker1, marker3, "Constructor should not return cached instance");
        assertNotSame(marker2, marker4, "Constructor should not return cached instance");
    }

    /**
     * Tests edge case: rapidly alternating between different parameter combinations.
     */
    @Test
    public void testConstructor_alternatingParameters() {
        // Arrange
        MemberVisitor extraVisitor = new NoSideEffectMethodMarker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                new SideEffectMethodMarker(null, true);
                new SideEffectMethodMarker(extraVisitor, false);
                new SideEffectMethodMarker(null, false);
                new SideEffectMethodMarker(extraVisitor, true);
            }
        }, "Constructor should handle rapid alternation between parameter values");
    }

    /**
     * Tests that instances created with different parameter combinations are distinct.
     */
    @Test
    public void testConstructor_distinctInstancesForDifferentCombinations() {
        // Arrange
        MemberVisitor visitor = new NoSideEffectMethodMarker();
        java.util.Set<SideEffectMethodMarker> markers = new java.util.HashSet<>();

        // Act
        markers.add(new SideEffectMethodMarker(null, false));
        markers.add(new SideEffectMethodMarker(null, true));
        markers.add(new SideEffectMethodMarker(visitor, false));
        markers.add(new SideEffectMethodMarker(visitor, true));

        // Assert - all four combinations should create distinct instances
        assertEquals(4, markers.size(), "All four parameter combinations should create distinct instances");
    }

    /**
     * Tests that the constructor can be called from different threads simultaneously
     * with different parameter combinations.
     */
    @Test
    public void testConstructor_concurrentCreationWithVariousParameters() throws InterruptedException {
        // Arrange
        final int threadCount = 20;
        Thread[] threads = new Thread[threadCount];
        final SideEffectMethodMarker[] results = new SideEffectMethodMarker[threadCount];
        final MemberVisitor[] visitors = {
                new NoSideEffectMethodMarker(),
                new NoExternalSideEffectMethodMarker(),
                null
        };

        // Act - create instances in multiple threads with different parameter combinations
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                MemberVisitor visitor = visitors[threadIndex % 3];
                results[threadIndex] = new SideEffectMethodMarker(visitor, threadIndex % 2 == 0);
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
        java.util.Set<SideEffectMethodMarker> uniqueMarkers = new java.util.HashSet<>();
        for (SideEffectMethodMarker marker : results) {
            uniqueMarkers.add(marker);
        }
        assertEquals(threadCount, uniqueMarkers.size(),
                "All " + threadCount + " instances should be unique");
    }

    /**
     * Tests that markers can be created with various MemberVisitor implementations.
     */
    @Test
    public void testConstructor_withVariousMemberVisitorImplementations() {
        // Arrange & Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(new NoSideEffectMethodMarker(), false);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(new NoExternalSideEffectMethodMarker(), true);
        SideEffectMethodMarker marker3 = new SideEffectMethodMarker(new SideEffectMethodMarker(null, false), false);

        // Assert
        assertNotNull(marker1, "Marker with NoSideEffectMethodMarker should be created");
        assertNotNull(marker2, "Marker with NoExternalSideEffectMethodMarker should be created");
        assertNotNull(marker3, "Marker with nested SideEffectMethodMarker should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances");
        assertNotSame(marker2, marker3, "Markers should be different instances");
    }

    /**
     * Tests that null can be explicitly passed as the first parameter.
     */
    @Test
    public void testConstructor_withExplicitNull_createsInstance() {
        // Act
        SideEffectMethodMarker marker1 = new SideEffectMethodMarker(null, false);
        SideEffectMethodMarker marker2 = new SideEffectMethodMarker(null, true);

        // Assert
        assertNotNull(marker1, "Marker with explicit null and false should be created");
        assertNotNull(marker2, "Marker with explicit null and true should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances");
    }

    /**
     * Tests that the constructor handles being called with the same visitor instance repeatedly.
     */
    @Test
    public void testConstructor_reusingVisitorInstance_createsDistinctMarkers() {
        // Arrange
        MemberVisitor sharedVisitor = new NoSideEffectMethodMarker();
        java.util.List<SideEffectMethodMarker> markers = new java.util.ArrayList<>();

        // Act
        for (int i = 0; i < 10; i++) {
            markers.add(new SideEffectMethodMarker(sharedVisitor, i % 2 == 0));
        }

        // Assert - all markers should be distinct
        java.util.Set<SideEffectMethodMarker> uniqueMarkers = new java.util.HashSet<>(markers);
        assertEquals(10, uniqueMarkers.size(),
                "Reusing the same visitor should still create distinct marker instances");
    }
}
