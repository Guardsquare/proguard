package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstanceofClassFilter} constructor.
 * Tests InstanceofClassFilter(ClassVisitor) constructor.
 */
public class InstanceofClassFilterClaude_constructorTest {

    /**
     * Tests the constructor InstanceofClassFilter(ClassVisitor) with a valid ClassVisitor.
     * Verifies that the InstanceofClassFilter instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidClassVisitor() {
        // Arrange - Create a valid ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create InstanceofClassFilter with the visitor
        InstanceofClassFilter filter = new InstanceofClassFilter(visitor);

        // Assert - Verify the InstanceofClassFilter instance was created successfully
        assertNotNull(filter, "InstanceofClassFilter should be instantiated successfully");
    }

    /**
     * Tests the constructor InstanceofClassFilter(ClassVisitor) with a null ClassVisitor.
     * Verifies that the InstanceofClassFilter constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullClassVisitor() {
        // Act - Create InstanceofClassFilter with null visitor
        InstanceofClassFilter filter = new InstanceofClassFilter(null);

        // Assert - Verify the InstanceofClassFilter instance was created
        assertNotNull(filter, "InstanceofClassFilter should be instantiated even with null visitor");
    }

    /**
     * Tests the constructor InstanceofClassFilter(ClassVisitor) multiple times.
     * Verifies that multiple InstanceofClassFilter instances can be created independently.
     */
    @Test
    public void testMultipleInstanceofClassFilterInstances() {
        // Arrange - Create two different ClassVisitors
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();

        // Act - Create two InstanceofClassFilter instances
        InstanceofClassFilter filter1 = new InstanceofClassFilter(visitor1);
        InstanceofClassFilter filter2 = new InstanceofClassFilter(visitor2);

        // Assert - Verify both InstanceofClassFilter instances were created successfully
        assertNotNull(filter1, "First InstanceofClassFilter instance should be created");
        assertNotNull(filter2, "Second InstanceofClassFilter instance should be created");
        assertNotSame(filter1, filter2, "InstanceofClassFilter instances should be different objects");
    }

    /**
     * Tests the constructor InstanceofClassFilter(ClassVisitor) with the same ClassVisitor instance multiple times.
     * Verifies that the same visitor can be used to create multiple InstanceofClassFilter instances.
     */
    @Test
    public void testMultipleInstanceofClassFilterInstancesWithSameVisitor() {
        // Arrange - Create a single ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create multiple InstanceofClassFilter instances with the same visitor
        InstanceofClassFilter filter1 = new InstanceofClassFilter(visitor);
        InstanceofClassFilter filter2 = new InstanceofClassFilter(visitor);

        // Assert - Verify both InstanceofClassFilter instances were created successfully
        assertNotNull(filter1, "First InstanceofClassFilter instance should be created");
        assertNotNull(filter2, "Second InstanceofClassFilter instance should be created");
        assertNotSame(filter1, filter2, "InstanceofClassFilter instances should be different objects");
    }

    /**
     * Tests that the constructor properly stores the visitor by checking behavior.
     * Verifies that the stored visitor is called correctly when filtering instanceofed classes.
     */
    @Test
    public void testConstructorStoresVisitorCorrectly() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        InstanceofClassFilter filter = new InstanceofClassFilter(trackingVisitor);

        // Create a ProgramClass with instanceof marker
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstanceofed();

        // Act - Visit the program class
        filter.visitAnyClass(programClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.visited, "Visitor should have been called for instanceofed class");
    }

    /**
     * Tests that the constructor properly stores the visitor and filters non-instanceofed classes.
     * Verifies that the stored visitor is NOT called for classes that are not instanceofed.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyWithNonInstanceofedClass() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        InstanceofClassFilter filter = new InstanceofClassFilter(trackingVisitor);

        // Create a ProgramClass WITHOUT instanceof marker
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - Visit the program class
        filter.visitAnyClass(programClass);

        // Assert - Verify the visitor was NOT called
        assertFalse(trackingVisitor.visited, "Visitor should NOT have been called for non-instanceofed class");
    }

    /**
     * Tests that the constructor properly handles a null visitor when used.
     * Verifies that InstanceofClassFilter can handle null visitor gracefully during operation.
     */
    @Test
    public void testConstructorWithNullVisitorDuringOperation() {
        // Arrange - Create InstanceofClassFilter with null visitor
        InstanceofClassFilter filter = new InstanceofClassFilter(null);

        // Create a ProgramClass with instanceof marker
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstanceofed();

        // Act & Assert - Verify that visiting with null visitor throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            filter.visitAnyClass(programClass);
        }, "Null visitor should cause NullPointerException when used");
    }

    /**
     * Tests that the constructor accepts different ClassVisitor implementations.
     * Verifies that InstanceofClassFilter works with various ClassVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentClassVisitorImplementations() {
        // Arrange & Act - Create InstanceofClassFilter with different visitor types
        InstanceofClassFilter filter1 = new InstanceofClassFilter(new TestClassVisitor());
        InstanceofClassFilter filter2 = new InstanceofClassFilter(new TrackingClassVisitor());
        InstanceofClassFilter filter3 = new InstanceofClassFilter(new AnotherTestClassVisitor());

        // Assert - Verify all InstanceofClassFilter instances were created successfully
        assertNotNull(filter1, "InstanceofClassFilter should work with TestClassVisitor");
        assertNotNull(filter2, "InstanceofClassFilter should work with TrackingClassVisitor");
        assertNotNull(filter3, "InstanceofClassFilter should work with AnotherTestClassVisitor");
    }

    /**
     * Tests that the constructor properly stores the visitor for LibraryClass.
     * Verifies that the stored visitor is called correctly when filtering instanceofed library classes.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyForLibraryClass() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        InstanceofClassFilter filter = new InstanceofClassFilter(trackingVisitor);

        // Create a LibraryClass with instanceof marker
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(libraryClass).setInstanceofed();

        // Act - Visit the library class
        filter.visitAnyClass(libraryClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.visited, "Visitor should have been called for instanceofed library class");
    }

    /**
     * Tests that the filter properly delegates based on instanceof status with multiple calls.
     * Verifies that the constructor stores the visitor correctly for repeated filtering operations.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyForMultipleCalls() {
        // Arrange - Create a counting visitor
        CountingClassVisitor countingVisitor = new CountingClassVisitor();
        InstanceofClassFilter filter = new InstanceofClassFilter(countingVisitor);

        // Create instanceofed and non-instanceofed classes
        ProgramClass instanceofedClass1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass1).setInstanceofed();

        ProgramClass instanceofedClass2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass2).setInstanceofed();

        ProgramClass nonInstanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstanceofedClass);

        // Act - Visit multiple classes
        filter.visitAnyClass(instanceofedClass1);
        filter.visitAnyClass(nonInstanceofedClass);
        filter.visitAnyClass(instanceofedClass2);

        // Assert - Verify the visitor was called only for instanceofed classes
        assertEquals(2, countingVisitor.visitCount, "Visitor should have been called exactly twice for instanceofed classes");
    }

    /**
     * Simple test ClassVisitor implementation for testing purposes.
     */
    private static class TestClassVisitor implements ClassVisitor {
        @Override
        public void visitAnyClass(Clazz clazz) {
            // No-op for testing
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            // No-op for testing
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            // No-op for testing
        }
    }

    /**
     * ClassVisitor implementation that tracks whether it was called.
     */
    private static class TrackingClassVisitor implements ClassVisitor {
        boolean visited = false;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visited = true;
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            visited = true;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            visited = true;
        }
    }

    /**
     * ClassVisitor implementation that counts how many times it was called.
     */
    private static class CountingClassVisitor implements ClassVisitor {
        int visitCount = 0;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visitCount++;
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            visitCount++;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            visitCount++;
        }
    }

    /**
     * Another test ClassVisitor implementation for testing purposes.
     */
    private static class AnotherTestClassVisitor implements ClassVisitor {
        @Override
        public void visitAnyClass(Clazz clazz) {
            // No-op for testing
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            // No-op for testing
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            // No-op for testing
        }
    }
}
