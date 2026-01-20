package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link EscapingClassFilter}.
 *
 * The EscapingClassFilter is a ClassVisitor that delegates to one of two ClassVisitor instances,
 * depending on whether the classes are marked as escaping or not.
 *
 * These tests verify:
 * 1. Both constructors properly store the delegate ClassVisitor(s)
 * 2. visitAnyClass delegates to escapingClassVisitor when the class is marked as escaping
 * 3. visitAnyClass delegates to otherClassVisitor when the class is not marked as escaping
 * 4. visitAnyClass handles null visitors correctly (does not throw exceptions)
 */
public class EscapingClassFilterClaudeTest {

    private TestClassVisitor escapingVisitor;
    private TestClassVisitor otherVisitor;
    private EscapingClassFilter filter;

    @BeforeEach
    public void setUp() {
        escapingVisitor = new TestClassVisitor();
        otherVisitor = new TestClassVisitor();
        filter = new EscapingClassFilter(escapingVisitor, otherVisitor);
    }

    /**
     * Helper class to track whether visitAnyClass was called.
     */
    private static class TestClassVisitor implements ClassVisitor {
        private int visitCount = 0;
        private ProgramClass lastVisitedClass = null;

        @Override
        public void visitAnyClass(proguard.classfile.Clazz clazz) {
            visitCount++;
            if (clazz instanceof ProgramClass) {
                lastVisitedClass = (ProgramClass) clazz;
            }
        }

        public int getVisitCount() {
            return visitCount;
        }

        public ProgramClass getLastVisitedClass() {
            return lastVisitedClass;
        }

        public void reset() {
            visitCount = 0;
            lastVisitedClass = null;
        }
    }

    // Constructor tests - single parameter constructor

    /**
     * Tests that the single-parameter constructor successfully creates an EscapingClassFilter instance.
     */
    @Test
    public void testConstructor_singleParam_createsInstance() {
        // Arrange & Act
        ClassVisitor visitor = new TestClassVisitor();
        EscapingClassFilter newFilter = new EscapingClassFilter(visitor);

        // Assert
        assertNotNull(newFilter, "EscapingClassFilter instance should be created");
    }

    /**
     * Tests that the single-parameter constructor creates a ClassVisitor instance.
     */
    @Test
    public void testConstructor_singleParam_createsClassVisitor() {
        // Arrange & Act
        ClassVisitor visitor = new TestClassVisitor();
        EscapingClassFilter newFilter = new EscapingClassFilter(visitor);

        // Assert
        assertInstanceOf(ClassVisitor.class, newFilter,
                "EscapingClassFilter should be an instance of ClassVisitor");
    }

    /**
     * Tests that the single-parameter constructor does not throw an exception.
     */
    @Test
    public void testConstructor_singleParam_doesNotThrowException() {
        // Arrange
        ClassVisitor visitor = new TestClassVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new EscapingClassFilter(visitor),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that the single-parameter constructor accepts null as visitor.
     */
    @Test
    public void testConstructor_singleParam_acceptsNull() {
        // Act & Assert
        assertDoesNotThrow(() -> new EscapingClassFilter(null),
                "Constructor should accept null visitor");
    }

    /**
     * Tests that the single-parameter constructor properly delegates escaping classes.
     */
    @Test
    public void testConstructor_singleParam_delegatesEscapingClasses() {
        // Arrange
        TestClassVisitor visitor = new TestClassVisitor();
        EscapingClassFilter singleParamFilter = new EscapingClassFilter(visitor);

        ProgramClass escapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act
        singleParamFilter.visitAnyClass(escapingClass);

        // Assert
        assertEquals(1, visitor.getVisitCount(),
                "Escaping class should be delegated to the visitor");
    }

    /**
     * Tests that the single-parameter constructor ignores non-escaping classes.
     */
    @Test
    public void testConstructor_singleParam_ignoresNonEscapingClasses() {
        // Arrange
        TestClassVisitor visitor = new TestClassVisitor();
        EscapingClassFilter singleParamFilter = new EscapingClassFilter(visitor);

        ProgramClass nonEscapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonEscapingClass);

        // Act
        singleParamFilter.visitAnyClass(nonEscapingClass);

        // Assert
        assertEquals(0, visitor.getVisitCount(),
                "Non-escaping class should not be delegated when otherClassVisitor is null");
    }

    // Constructor tests - two parameter constructor

    /**
     * Tests that the two-parameter constructor successfully creates an EscapingClassFilter instance.
     */
    @Test
    public void testConstructor_twoParam_createsInstance() {
        // Arrange & Act
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();
        EscapingClassFilter newFilter = new EscapingClassFilter(visitor1, visitor2);

        // Assert
        assertNotNull(newFilter, "EscapingClassFilter instance should be created");
    }

    /**
     * Tests that the two-parameter constructor creates a ClassVisitor instance.
     */
    @Test
    public void testConstructor_twoParam_createsClassVisitor() {
        // Arrange & Act
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();
        EscapingClassFilter newFilter = new EscapingClassFilter(visitor1, visitor2);

        // Assert
        assertInstanceOf(ClassVisitor.class, newFilter,
                "EscapingClassFilter should be an instance of ClassVisitor");
    }

    /**
     * Tests that the two-parameter constructor does not throw an exception.
     */
    @Test
    public void testConstructor_twoParam_doesNotThrowException() {
        // Arrange
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new EscapingClassFilter(visitor1, visitor2),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that the two-parameter constructor accepts null visitors.
     */
    @Test
    public void testConstructor_twoParam_acceptsNullVisitors() {
        // Act & Assert
        assertDoesNotThrow(() -> new EscapingClassFilter(null, null),
                "Constructor should accept null visitors");
        assertDoesNotThrow(() -> new EscapingClassFilter(new TestClassVisitor(), null),
                "Constructor should accept null otherClassVisitor");
        assertDoesNotThrow(() -> new EscapingClassFilter(null, new TestClassVisitor()),
                "Constructor should accept null escapingClassVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_twoParam_createsMultipleInstances() {
        // Arrange & Act
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();
        ClassVisitor visitor3 = new TestClassVisitor();
        ClassVisitor visitor4 = new TestClassVisitor();
        EscapingClassFilter filter1 = new EscapingClassFilter(visitor1, visitor2);
        EscapingClassFilter filter2 = new EscapingClassFilter(visitor3, visitor4);

        // Assert
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotSame(filter1, filter2, "Filters should be different instances");
    }

    // visitAnyClass tests - escaping classes

    /**
     * Tests that visitAnyClass delegates to escapingClassVisitor when the class is escaping.
     */
    @Test
    public void testVisitAnyClass_escapingClass_delegatesToEscapingVisitor() {
        // Arrange
        ProgramClass escapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Verify the class is escaping
        assertTrue(EscapingClassMarker.isClassEscaping(escapingClass),
                "Class should be marked as escaping");

        // Act
        filter.visitAnyClass(escapingClass);

        // Assert
        assertEquals(1, escapingVisitor.getVisitCount(),
                "Escaping visitor should have been called once");
        assertEquals(0, otherVisitor.getVisitCount(),
                "Other visitor should not have been called");
        assertEquals(escapingClass, escapingVisitor.getLastVisitedClass(),
                "Escaping visitor should have received the same class");
    }

    /**
     * Tests that visitAnyClass delegates to otherClassVisitor when the class is not escaping.
     */
    @Test
    public void testVisitAnyClass_nonEscapingClass_delegatesToOtherVisitor() {
        // Arrange
        ProgramClass nonEscapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonEscapingClass);

        // Verify the class is not escaping
        assertFalse(EscapingClassMarker.isClassEscaping(nonEscapingClass),
                "Class should not be marked as escaping");

        // Act
        filter.visitAnyClass(nonEscapingClass);

        // Assert
        assertEquals(0, escapingVisitor.getVisitCount(),
                "Escaping visitor should not have been called");
        assertEquals(1, otherVisitor.getVisitCount(),
                "Other visitor should have been called once");
        assertEquals(nonEscapingClass, otherVisitor.getLastVisitedClass(),
                "Other visitor should have received the same class");
    }

    /**
     * Tests that visitAnyClass can handle multiple escaping classes.
     */
    @Test
    public void testVisitAnyClass_multipleEscapingClasses_allDelegated() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setEscaping();
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class2).setEscaping();
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setEscaping();

        // Act
        filter.visitAnyClass(class1);
        filter.visitAnyClass(class2);
        filter.visitAnyClass(class3);

        // Assert
        assertEquals(3, escapingVisitor.getVisitCount(),
                "Escaping visitor should have been called three times");
        assertEquals(0, otherVisitor.getVisitCount(),
                "Other visitor should not have been called");
    }

    /**
     * Tests that visitAnyClass can handle multiple non-escaping classes.
     */
    @Test
    public void testVisitAnyClass_multipleNonEscapingClasses_allDelegated() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Act
        filter.visitAnyClass(class1);
        filter.visitAnyClass(class2);
        filter.visitAnyClass(class3);

        // Assert
        assertEquals(0, escapingVisitor.getVisitCount(),
                "Escaping visitor should not have been called");
        assertEquals(3, otherVisitor.getVisitCount(),
                "Other visitor should have been called three times");
    }

    /**
     * Tests that visitAnyClass correctly routes mixed escaping and non-escaping classes.
     */
    @Test
    public void testVisitAnyClass_mixedClasses_delegatedCorrectly() {
        // Arrange
        ProgramClass escapingClass1 = new ProgramClass();
        ProgramClass nonEscapingClass = new ProgramClass();
        ProgramClass escapingClass2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonEscapingClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass2);

        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass1).setEscaping();
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass2).setEscaping();

        // Act
        filter.visitAnyClass(escapingClass1);
        filter.visitAnyClass(nonEscapingClass);
        filter.visitAnyClass(escapingClass2);

        // Assert
        assertEquals(2, escapingVisitor.getVisitCount(),
                "Escaping visitor should have been called twice");
        assertEquals(1, otherVisitor.getVisitCount(),
                "Other visitor should have been called once");
    }

    // visitAnyClass tests - null handling

    /**
     * Tests that visitAnyClass handles null escapingClassVisitor gracefully for escaping classes.
     */
    @Test
    public void testVisitAnyClass_nullEscapingVisitor_doesNotThrow() {
        // Arrange
        EscapingClassFilter nullEscapingFilter = new EscapingClassFilter(null, otherVisitor);
        ProgramClass escapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act & Assert
        assertDoesNotThrow(() -> nullEscapingFilter.visitAnyClass(escapingClass),
                "Should not throw when escapingClassVisitor is null");
        assertEquals(0, otherVisitor.getVisitCount(),
                "Other visitor should not be called for escaping class");
    }

    /**
     * Tests that visitAnyClass handles null otherClassVisitor gracefully for non-escaping classes.
     */
    @Test
    public void testVisitAnyClass_nullOtherVisitor_doesNotThrow() {
        // Arrange
        EscapingClassFilter nullOtherFilter = new EscapingClassFilter(escapingVisitor, null);
        ProgramClass nonEscapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonEscapingClass);

        // Act & Assert
        assertDoesNotThrow(() -> nullOtherFilter.visitAnyClass(nonEscapingClass),
                "Should not throw when otherClassVisitor is null");
        assertEquals(0, escapingVisitor.getVisitCount(),
                "Escaping visitor should not be called for non-escaping class");
    }

    /**
     * Tests that visitAnyClass handles both visitors being null.
     */
    @Test
    public void testVisitAnyClass_bothVisitorsNull_doesNotThrow() {
        // Arrange
        EscapingClassFilter nullFilter = new EscapingClassFilter(null, null);
        ProgramClass escapingClass = new ProgramClass();
        ProgramClass nonEscapingClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonEscapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act & Assert
        assertDoesNotThrow(() -> nullFilter.visitAnyClass(escapingClass),
                "Should not throw when both visitors are null (escaping class)");
        assertDoesNotThrow(() -> nullFilter.visitAnyClass(nonEscapingClass),
                "Should not throw when both visitors are null (non-escaping class)");
    }

    // visitAnyClass tests - edge cases and behavior

    /**
     * Tests that visitAnyClass doesn't throw an exception.
     */
    @Test
    public void testVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass escapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitAnyClass(escapingClass),
                "visitAnyClass should not throw an exception");
    }

    /**
     * Tests that visitAnyClass can be called in rapid succession.
     */
    @Test
    public void testVisitAnyClass_rapidSuccession_handlesProperly() {
        // Arrange
        ProgramClass escapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act
        for (int i = 0; i < 100; i++) {
            filter.visitAnyClass(escapingClass);
        }

        // Assert
        assertEquals(100, escapingVisitor.getVisitCount(),
                "Escaping visitor should have been called 100 times");
    }

    /**
     * Tests that the filter is stateless and doesn't affect subsequent calls.
     */
    @Test
    public void testVisitAnyClass_statelessBehavior() {
        // Arrange
        ProgramClass escapingClass = new ProgramClass();
        ProgramClass nonEscapingClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonEscapingClass);

        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act & Assert - First call with escaping class
        filter.visitAnyClass(escapingClass);
        assertEquals(1, escapingVisitor.getVisitCount(), "First call should delegate to escaping visitor");

        // Reset and test with non-escaping class
        escapingVisitor.reset();
        filter.visitAnyClass(nonEscapingClass);
        assertEquals(0, escapingVisitor.getVisitCount(), "Second call should not delegate to escaping visitor");
        assertEquals(1, otherVisitor.getVisitCount(), "Second call should delegate to other visitor");

        // Test again with escaping class to ensure no state pollution
        otherVisitor.reset();
        filter.visitAnyClass(escapingClass);
        assertEquals(1, escapingVisitor.getVisitCount(), "Third call should delegate to escaping visitor again");
        assertEquals(0, otherVisitor.getVisitCount(), "Third call should not delegate to other visitor");
    }

    /**
     * Tests that multiple EscapingClassFilter instances work independently.
     */
    @Test
    public void testVisitAnyClass_multipleFilters_independentBehavior() {
        // Arrange
        TestClassVisitor visitor1 = new TestClassVisitor();
        TestClassVisitor visitor2 = new TestClassVisitor();
        TestClassVisitor visitor3 = new TestClassVisitor();
        TestClassVisitor visitor4 = new TestClassVisitor();
        EscapingClassFilter filter1 = new EscapingClassFilter(visitor1, visitor2);
        EscapingClassFilter filter2 = new EscapingClassFilter(visitor3, visitor4);

        ProgramClass escapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act
        filter1.visitAnyClass(escapingClass);
        filter2.visitAnyClass(escapingClass);

        // Assert
        assertEquals(1, visitor1.getVisitCount(), "First filter's escaping visitor should be called once");
        assertEquals(0, visitor2.getVisitCount(), "First filter's other visitor should not be called");
        assertEquals(1, visitor3.getVisitCount(), "Second filter's escaping visitor should be called once");
        assertEquals(0, visitor4.getVisitCount(), "Second filter's other visitor should not be called");
    }

    /**
     * Tests that visitAnyClass respects the escaping flag for each individual class.
     */
    @Test
    public void testVisitAnyClass_respectsIndividualClassFlags() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[10];
        for (int i = 0; i < 10; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            // Mark only even-indexed classes as escaping
            if (i % 2 == 0) {
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(classes[i]).setEscaping();
            }
        }

        // Act
        for (ProgramClass clazz : classes) {
            filter.visitAnyClass(clazz);
        }

        // Assert
        assertEquals(5, escapingVisitor.getVisitCount(),
                "Escaping visitor should have been called for 5 escaping classes");
        assertEquals(5, otherVisitor.getVisitCount(),
                "Other visitor should have been called for 5 non-escaping classes");
    }

    /**
     * Tests that the same class can be visited multiple times with changing escaping status.
     */
    @Test
    public void testVisitAnyClass_sameClassMultipleTimes_respondsToStatusChanges() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - Initially not escaping
        filter.visitAnyClass(programClass);
        assertEquals(0, escapingVisitor.getVisitCount(),
                "Should not delegate to escaping visitor when not escaping");
        assertEquals(1, otherVisitor.getVisitCount(),
                "Should delegate to other visitor when not escaping");

        // Mark as escaping and visit again
        otherVisitor.reset();
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setEscaping();
        filter.visitAnyClass(programClass);
        assertEquals(1, escapingVisitor.getVisitCount(),
                "Should delegate to escaping visitor when escaping");
        assertEquals(0, otherVisitor.getVisitCount(),
                "Should not delegate to other visitor when escaping");

        // Visit again - should still delegate to escaping visitor
        filter.visitAnyClass(programClass);
        assertEquals(2, escapingVisitor.getVisitCount(),
                "Should continue to delegate to escaping visitor while escaping");
        assertEquals(0, otherVisitor.getVisitCount(),
                "Should not delegate to other visitor while escaping");
    }

    /**
     * Tests that the filter works correctly in a concurrent environment.
     */
    @Test
    public void testVisitAnyClass_concurrentAccess_threadsafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int callsPerThread = 10;
        Thread[] threads = new Thread[threadCount];

        // Use thread-safe counters to track total visits
        AtomicInteger escapingVisits = new AtomicInteger(0);
        AtomicInteger otherVisits = new AtomicInteger(0);
        ClassVisitor escapingCounter = clazz -> escapingVisits.incrementAndGet();
        ClassVisitor otherCounter = clazz -> otherVisits.incrementAndGet();
        EscapingClassFilter concurrentFilter = new EscapingClassFilter(escapingCounter, otherCounter);

        // Create classes for each thread - half escaping, half not
        ProgramClass[] classes = new ProgramClass[threadCount];
        for (int i = 0; i < threadCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            if (i % 2 == 0) {
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(classes[i]).setEscaping();
            }
        }

        // Act - visit classes in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    concurrentFilter.visitAnyClass(classes[threadIndex]);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        assertEquals(threadCount / 2 * callsPerThread, escapingVisits.get(),
                "Half of the visits should be to escaping visitor");
        assertEquals(threadCount / 2 * callsPerThread, otherVisits.get(),
                "Half of the visits should be to other visitor");
    }

    /**
     * Tests that the filter works correctly when the delegate visitor is a chain of visitors.
     */
    @Test
    public void testVisitAnyClass_chainedVisitors_allReceiveCall() {
        // Arrange
        TestClassVisitor visitor1 = new TestClassVisitor();
        TestClassVisitor visitor2 = new TestClassVisitor();

        // Create a chaining visitor for escaping classes
        ClassVisitor chainVisitor = clazz -> {
            visitor1.visitAnyClass(clazz);
            visitor2.visitAnyClass(clazz);
        };

        EscapingClassFilter chainFilter = new EscapingClassFilter(chainVisitor, null);

        ProgramClass escapingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(escapingClass).setEscaping();

        // Act
        chainFilter.visitAnyClass(escapingClass);

        // Assert
        assertEquals(1, visitor1.getVisitCount(), "First visitor in chain should be called");
        assertEquals(1, visitor2.getVisitCount(), "Second visitor in chain should be called");
    }

    /**
     * Tests that a class without ProgramClassOptimizationInfo is handled properly.
     * Note: A class without ProgramClassOptimizationInfo will have a default ClassOptimizationInfo
     * which returns true for isEscaping(), so it should be routed to the escaping visitor.
     */
    @Test
    public void testVisitAnyClass_classWithDefaultOptimizationInfo_routesToEscapingVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        // Set a default ClassOptimizationInfo (not ProgramClassOptimizationInfo)
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Verify that the default ClassOptimizationInfo returns true for isEscaping
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Default ClassOptimizationInfo should return true for isEscaping");

        // Act
        filter.visitAnyClass(programClass);

        // Assert - should be routed to escaping visitor since default isEscaping() returns true
        assertEquals(1, escapingVisitor.getVisitCount(),
                "Escaping visitor should be called for class with default optimization info");
        assertEquals(0, otherVisitor.getVisitCount(),
                "Other visitor should not be called");
    }
}
