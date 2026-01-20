package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DotClassFilter}.
 *
 * The DotClassFilter is a ClassVisitor that delegates to another ClassVisitor,
 * but only for classes that are marked as "dot classed" (i.e., used in a .class construct).
 *
 * These tests verify:
 * 1. The constructor properly stores the delegate ClassVisitor
 * 2. visitAnyClass only delegates when the class is marked as dot classed
 * 3. visitAnyClass does not delegate when the class is not dot classed
 */
public class DotClassFilterClaudeTest {

    private DotClassFilter filter;
    private TestClassVisitor testVisitor;

    @BeforeEach
    public void setUp() {
        testVisitor = new TestClassVisitor();
        filter = new DotClassFilter(testVisitor);
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

    // Constructor tests

    /**
     * Tests that the constructor successfully creates a DotClassFilter instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Arrange & Act
        ClassVisitor visitor = new TestClassVisitor();
        DotClassFilter newFilter = new DotClassFilter(visitor);

        // Assert
        assertNotNull(newFilter, "DotClassFilter instance should be created");
    }

    /**
     * Tests that the constructed DotClassFilter is an instance of ClassVisitor.
     */
    @Test
    public void testConstructor_createsClassVisitor() {
        // Arrange & Act
        ClassVisitor visitor = new TestClassVisitor();
        DotClassFilter newFilter = new DotClassFilter(visitor);

        // Assert
        assertInstanceOf(ClassVisitor.class, newFilter,
                "DotClassFilter should be an instance of ClassVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Arrange & Act
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();
        DotClassFilter filter1 = new DotClassFilter(visitor1);
        DotClassFilter filter2 = new DotClassFilter(visitor2);

        // Assert
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotSame(filter1, filter2, "Filters should be different instances");
    }

    /**
     * Tests that the constructor does not throw an exception.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        ClassVisitor visitor = new TestClassVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new DotClassFilter(visitor),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that the constructor can accept any ClassVisitor implementation.
     */
    @Test
    public void testConstructor_acceptsAnyClassVisitor() {
        // Arrange - Create different ClassVisitor implementations
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = clazz -> {}; // Lambda implementation

        // Act & Assert
        assertDoesNotThrow(() -> new DotClassFilter(visitor1),
                "Constructor should accept TestClassVisitor");
        assertDoesNotThrow(() -> new DotClassFilter(visitor2),
                "Constructor should accept lambda ClassVisitor");
    }

    // visitAnyClass tests

    /**
     * Tests that visitAnyClass delegates to the wrapped visitor when the class is dot classed.
     */
    @Test
    public void testVisitAnyClass_dotClassedClass_delegatesToVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setDotClassed();

        // Verify the class is dot classed
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be marked as dot classed");

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(1, testVisitor.getVisitCount(),
                "Visitor should have been called once");
        assertEquals(programClass, testVisitor.getLastVisitedClass(),
                "Visitor should have received the same class");
    }

    /**
     * Tests that visitAnyClass does not delegate when the class is not dot classed.
     */
    @Test
    public void testVisitAnyClass_notDotClassedClass_doesNotDelegate() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify the class is not dot classed
        assertFalse(DotClassMarker.isDotClassed(programClass),
                "Class should not be marked as dot classed");

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(0, testVisitor.getVisitCount(),
                "Visitor should not have been called");
    }

    /**
     * Tests that visitAnyClass can be called multiple times on dot classed classes.
     */
    @Test
    public void testVisitAnyClass_multipleDotClassedClasses_allDelegated() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setDotClassed();
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class2).setDotClassed();
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setDotClassed();

        // Act
        filter.visitAnyClass(class1);
        filter.visitAnyClass(class2);
        filter.visitAnyClass(class3);

        // Assert
        assertEquals(3, testVisitor.getVisitCount(),
                "Visitor should have been called three times");
    }

    /**
     * Tests that visitAnyClass filters out non-dot classed classes when mixed with dot classed ones.
     */
    @Test
    public void testVisitAnyClass_mixedClasses_onlyDotClassedDelegated() {
        // Arrange
        ProgramClass dotClassedClass1 = new ProgramClass();
        ProgramClass notDotClassedClass = new ProgramClass();
        ProgramClass dotClassedClass2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(dotClassedClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(notDotClassedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(dotClassedClass2);

        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(dotClassedClass1).setDotClassed();
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(dotClassedClass2).setDotClassed();

        // Act
        filter.visitAnyClass(dotClassedClass1);
        filter.visitAnyClass(notDotClassedClass);
        filter.visitAnyClass(dotClassedClass2);

        // Assert
        assertEquals(2, testVisitor.getVisitCount(),
                "Visitor should have been called twice (only for dot classed classes)");
    }

    /**
     * Tests that visitAnyClass doesn't throw an exception.
     */
    @Test
    public void testVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setDotClassed();

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitAnyClass(programClass),
                "visitAnyClass should not throw an exception");
    }

    /**
     * Tests that visitAnyClass can be called in rapid succession.
     */
    @Test
    public void testVisitAnyClass_rapidSuccession_handlesProperly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setDotClassed();

        // Act
        for (int i = 0; i < 100; i++) {
            filter.visitAnyClass(programClass);
        }

        // Assert
        assertEquals(100, testVisitor.getVisitCount(),
                "Visitor should have been called 100 times");
    }

    /**
     * Tests that the filter is stateless and doesn't affect subsequent calls.
     */
    @Test
    public void testVisitAnyClass_statelessBehavior() {
        // Arrange
        ProgramClass dotClassedClass = new ProgramClass();
        ProgramClass notDotClassedClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(dotClassedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(notDotClassedClass);

        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(dotClassedClass).setDotClassed();

        // Act & Assert - First call with dot classed class
        filter.visitAnyClass(dotClassedClass);
        assertEquals(1, testVisitor.getVisitCount(), "First call should delegate");

        // Reset and test with non-dot classed class
        testVisitor.reset();
        filter.visitAnyClass(notDotClassedClass);
        assertEquals(0, testVisitor.getVisitCount(), "Second call should not delegate");

        // Test again with dot classed class to ensure no state pollution
        filter.visitAnyClass(dotClassedClass);
        assertEquals(1, testVisitor.getVisitCount(), "Third call should delegate again");
    }

    /**
     * Tests that multiple DotClassFilter instances work independently.
     */
    @Test
    public void testVisitAnyClass_multipleFilters_independentBehavior() {
        // Arrange
        TestClassVisitor visitor1 = new TestClassVisitor();
        TestClassVisitor visitor2 = new TestClassVisitor();
        DotClassFilter filter1 = new DotClassFilter(visitor1);
        DotClassFilter filter2 = new DotClassFilter(visitor2);

        ProgramClass dotClassedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(dotClassedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(dotClassedClass).setDotClassed();

        // Act
        filter1.visitAnyClass(dotClassedClass);
        filter2.visitAnyClass(dotClassedClass);

        // Assert
        assertEquals(1, visitor1.getVisitCount(), "First visitor should be called once");
        assertEquals(1, visitor2.getVisitCount(), "Second visitor should be called once");
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

        // Use a thread-safe counter to track total visits
        AtomicInteger totalVisits = new AtomicInteger(0);
        ClassVisitor countingVisitor = clazz -> totalVisits.incrementAndGet();
        DotClassFilter concurrentFilter = new DotClassFilter(countingVisitor);

        // Create dot classed classes for each thread
        ProgramClass[] classes = new ProgramClass[threadCount];
        for (int i = 0; i < threadCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(classes[i]).setDotClassed();
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
        assertEquals(threadCount * callsPerThread, totalVisits.get(),
                "All visits should have been counted");
    }

    /**
     * Tests that visitAnyClass respects the dot classed flag for each individual class.
     */
    @Test
    public void testVisitAnyClass_respectsIndividualClassFlags() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[10];
        for (int i = 0; i < 10; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            // Mark only even-indexed classes as dot classed
            if (i % 2 == 0) {
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(classes[i]).setDotClassed();
            }
        }

        // Act
        for (ProgramClass clazz : classes) {
            filter.visitAnyClass(clazz);
        }

        // Assert
        assertEquals(5, testVisitor.getVisitCount(),
                "Only the 5 dot classed classes should have been visited");
    }

    /**
     * Tests that the filter works correctly when the delegate visitor is a chain of visitors.
     */
    @Test
    public void testVisitAnyClass_chainedVisitors_allReceiveCall() {
        // Arrange
        TestClassVisitor visitor1 = new TestClassVisitor();
        TestClassVisitor visitor2 = new TestClassVisitor();

        // Create a chaining visitor
        ClassVisitor chainVisitor = clazz -> {
            visitor1.visitAnyClass(clazz);
            visitor2.visitAnyClass(clazz);
        };

        DotClassFilter chainFilter = new DotClassFilter(chainVisitor);

        ProgramClass dotClassedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(dotClassedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(dotClassedClass).setDotClassed();

        // Act
        chainFilter.visitAnyClass(dotClassedClass);

        // Assert
        assertEquals(1, visitor1.getVisitCount(), "First visitor in chain should be called");
        assertEquals(1, visitor2.getVisitCount(), "Second visitor in chain should be called");
    }

    /**
     * Tests that the same class can be visited multiple times with changing dot classed status.
     */
    @Test
    public void testVisitAnyClass_sameclassMultipleTimes_respondsToStatusChanges() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - Initially not dot classed
        filter.visitAnyClass(programClass);
        assertEquals(0, testVisitor.getVisitCount(),
                "Should not delegate when not dot classed");

        // Mark as dot classed and visit again
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setDotClassed();
        filter.visitAnyClass(programClass);
        assertEquals(1, testVisitor.getVisitCount(),
                "Should delegate when dot classed");

        // Visit again - should still delegate
        filter.visitAnyClass(programClass);
        assertEquals(2, testVisitor.getVisitCount(),
                "Should continue to delegate while dot classed");
    }
}
