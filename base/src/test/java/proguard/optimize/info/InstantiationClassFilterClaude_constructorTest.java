package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstantiationClassFilter} constructor.
 * Tests InstantiationClassFilter(ClassVisitor) constructor.
 */
public class InstantiationClassFilterClaude_constructorTest {

    /**
     * Tests the constructor InstantiationClassFilter(ClassVisitor) with a valid ClassVisitor.
     * Verifies that the InstantiationClassFilter instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidClassVisitor() {
        // Arrange - Create a valid ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create InstantiationClassFilter with the visitor
        InstantiationClassFilter filter = new InstantiationClassFilter(visitor);

        // Assert - Verify the InstantiationClassFilter instance was created successfully
        assertNotNull(filter, "InstantiationClassFilter should be instantiated successfully");
    }

    /**
     * Tests the constructor InstantiationClassFilter(ClassVisitor) with a null ClassVisitor.
     * Verifies that the InstantiationClassFilter constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullClassVisitor() {
        // Act - Create InstantiationClassFilter with null visitor
        InstantiationClassFilter filter = new InstantiationClassFilter(null);

        // Assert - Verify the InstantiationClassFilter instance was created
        assertNotNull(filter, "InstantiationClassFilter should be instantiated even with null visitor");
    }

    /**
     * Tests the constructor InstantiationClassFilter(ClassVisitor) multiple times.
     * Verifies that multiple InstantiationClassFilter instances can be created independently.
     */
    @Test
    public void testMultipleInstantiationClassFilterInstances() {
        // Arrange - Create two different ClassVisitors
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();

        // Act - Create two InstantiationClassFilter instances
        InstantiationClassFilter filter1 = new InstantiationClassFilter(visitor1);
        InstantiationClassFilter filter2 = new InstantiationClassFilter(visitor2);

        // Assert - Verify both InstantiationClassFilter instances were created successfully
        assertNotNull(filter1, "First InstantiationClassFilter instance should be created");
        assertNotNull(filter2, "Second InstantiationClassFilter instance should be created");
        assertNotSame(filter1, filter2, "InstantiationClassFilter instances should be different objects");
    }

    /**
     * Tests the constructor InstantiationClassFilter(ClassVisitor) with the same ClassVisitor instance multiple times.
     * Verifies that the same visitor can be used to create multiple InstantiationClassFilter instances.
     */
    @Test
    public void testMultipleInstantiationClassFilterInstancesWithSameVisitor() {
        // Arrange - Create a single ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create multiple InstantiationClassFilter instances with the same visitor
        InstantiationClassFilter filter1 = new InstantiationClassFilter(visitor);
        InstantiationClassFilter filter2 = new InstantiationClassFilter(visitor);

        // Assert - Verify both InstantiationClassFilter instances were created successfully
        assertNotNull(filter1, "First InstantiationClassFilter instance should be created");
        assertNotNull(filter2, "Second InstantiationClassFilter instance should be created");
        assertNotSame(filter1, filter2, "InstantiationClassFilter instances should be different objects");
    }

    /**
     * Tests that the constructor properly stores the visitor by checking behavior.
     * Verifies that the stored visitor is called correctly when filtering instantiated classes.
     */
    @Test
    public void testConstructorStoresVisitorCorrectly() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        InstantiationClassFilter filter = new InstantiationClassFilter(trackingVisitor);

        // Create a ProgramClass with instantiation marker
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstantiated();

        // Act - Visit the program class
        filter.visitAnyClass(programClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.visited, "Visitor should have been called for instantiated class");
    }

    /**
     * Tests that the constructor properly stores the visitor and filters non-instantiated classes.
     * Verifies that the stored visitor is NOT called for classes that are not instantiated.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyWithNonInstantiatedClass() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        InstantiationClassFilter filter = new InstantiationClassFilter(trackingVisitor);

        // Create a ProgramClass WITHOUT instantiation marker
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - Visit the program class
        filter.visitAnyClass(programClass);

        // Assert - Verify the visitor was NOT called
        assertFalse(trackingVisitor.visited, "Visitor should NOT have been called for non-instantiated class");
    }

    /**
     * Tests that the constructor properly handles a null visitor when used.
     * Verifies that InstantiationClassFilter can handle null visitor gracefully during operation.
     */
    @Test
    public void testConstructorWithNullVisitorDuringOperation() {
        // Arrange - Create InstantiationClassFilter with null visitor
        InstantiationClassFilter filter = new InstantiationClassFilter(null);

        // Create a ProgramClass with instantiation marker
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstantiated();

        // Act & Assert - Verify that visiting with null visitor throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            filter.visitAnyClass(programClass);
        }, "Null visitor should cause NullPointerException when used");
    }

    /**
     * Tests that the constructor accepts different ClassVisitor implementations.
     * Verifies that InstantiationClassFilter works with various ClassVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentClassVisitorImplementations() {
        // Arrange & Act - Create InstantiationClassFilter with different visitor types
        InstantiationClassFilter filter1 = new InstantiationClassFilter(new TestClassVisitor());
        InstantiationClassFilter filter2 = new InstantiationClassFilter(new TrackingClassVisitor());
        InstantiationClassFilter filter3 = new InstantiationClassFilter(new AnotherTestClassVisitor());

        // Assert - Verify all InstantiationClassFilter instances were created successfully
        assertNotNull(filter1, "InstantiationClassFilter should work with TestClassVisitor");
        assertNotNull(filter2, "InstantiationClassFilter should work with TrackingClassVisitor");
        assertNotNull(filter3, "InstantiationClassFilter should work with AnotherTestClassVisitor");
    }

    /**
     * Tests that the constructor properly stores the visitor for LibraryClass.
     * Verifies that the stored visitor is called correctly when filtering instantiated library classes.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyForLibraryClass() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        InstantiationClassFilter filter = new InstantiationClassFilter(trackingVisitor);

        // Create a LibraryClass with instantiation marker
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(libraryClass).setInstantiated();

        // Act - Visit the library class
        filter.visitAnyClass(libraryClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.visited, "Visitor should have been called for instantiated library class");
    }

    /**
     * Tests that the filter properly delegates based on instantiation status with multiple calls.
     * Verifies that the constructor stores the visitor correctly for repeated filtering operations.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyForMultipleCalls() {
        // Arrange - Create a counting visitor
        CountingClassVisitor countingVisitor = new CountingClassVisitor();
        InstantiationClassFilter filter = new InstantiationClassFilter(countingVisitor);

        // Create instantiated and non-instantiated classes
        ProgramClass instantiatedClass1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass1).setInstantiated();

        ProgramClass instantiatedClass2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass2).setInstantiated();

        ProgramClass nonInstantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstantiatedClass);

        // Act - Visit multiple classes
        filter.visitAnyClass(instantiatedClass1);
        filter.visitAnyClass(nonInstantiatedClass);
        filter.visitAnyClass(instantiatedClass2);

        // Assert - Verify the visitor was called only for instantiated classes
        assertEquals(2, countingVisitor.visitCount, "Visitor should have been called exactly twice for instantiated classes");
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
