package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SubclassedClassFilter} constructor.
 * Tests SubclassedClassFilter(ClassVisitor) constructor.
 */
public class SubclassedClassFilterClaude_constructorTest {

    /**
     * Tests the constructor SubclassedClassFilter(ClassVisitor) with a valid ClassVisitor.
     * Verifies that the SubclassedClassFilter instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidClassVisitor() {
        // Arrange - Create a valid ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create SubclassedClassFilter with the visitor
        SubclassedClassFilter filter = new SubclassedClassFilter(visitor);

        // Assert - Verify the SubclassedClassFilter instance was created successfully
        assertNotNull(filter, "SubclassedClassFilter should be instantiated successfully");
    }

    /**
     * Tests the constructor SubclassedClassFilter(ClassVisitor) with a null ClassVisitor.
     * Verifies that the SubclassedClassFilter constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullClassVisitor() {
        // Act - Create SubclassedClassFilter with null visitor
        SubclassedClassFilter filter = new SubclassedClassFilter(null);

        // Assert - Verify the SubclassedClassFilter instance was created
        assertNotNull(filter, "SubclassedClassFilter should be instantiated even with null visitor");
    }

    /**
     * Tests the constructor SubclassedClassFilter(ClassVisitor) multiple times.
     * Verifies that multiple SubclassedClassFilter instances can be created independently.
     */
    @Test
    public void testMultipleSubclassedClassFilterInstances() {
        // Arrange - Create two different ClassVisitors
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();

        // Act - Create two SubclassedClassFilter instances
        SubclassedClassFilter filter1 = new SubclassedClassFilter(visitor1);
        SubclassedClassFilter filter2 = new SubclassedClassFilter(visitor2);

        // Assert - Verify both SubclassedClassFilter instances were created successfully
        assertNotNull(filter1, "First SubclassedClassFilter instance should be created");
        assertNotNull(filter2, "Second SubclassedClassFilter instance should be created");
        assertNotSame(filter1, filter2, "SubclassedClassFilter instances should be different objects");
    }

    /**
     * Tests the constructor SubclassedClassFilter(ClassVisitor) with the same ClassVisitor instance multiple times.
     * Verifies that the same visitor can be used to create multiple SubclassedClassFilter instances.
     */
    @Test
    public void testMultipleSubclassedClassFilterInstancesWithSameVisitor() {
        // Arrange - Create a single ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create multiple SubclassedClassFilter instances with the same visitor
        SubclassedClassFilter filter1 = new SubclassedClassFilter(visitor);
        SubclassedClassFilter filter2 = new SubclassedClassFilter(visitor);

        // Assert - Verify both SubclassedClassFilter instances were created successfully
        assertNotNull(filter1, "First SubclassedClassFilter instance should be created");
        assertNotNull(filter2, "Second SubclassedClassFilter instance should be created");
        assertNotSame(filter1, filter2, "SubclassedClassFilter instances should be different objects");
    }

    /**
     * Tests that the constructor properly stores the visitor by checking behavior.
     * Verifies that the stored visitor is called correctly when filtering subclassed classes.
     */
    @Test
    public void testConstructorStoresVisitorCorrectly() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        SubclassedClassFilter filter = new SubclassedClassFilter(trackingVisitor);

        // Create a ProgramClass with subclasses
        ProgramClass programClass = new ProgramClass();
        programClass.subClassCount = 1;

        // Act - Visit the program class
        filter.visitProgramClass(programClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.programClassVisited, "Visitor should have been called for subclassed program class");
    }

    /**
     * Tests that the constructor properly stores the visitor by checking behavior with LibraryClass.
     * Verifies that the stored visitor is called correctly when filtering subclassed library classes.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyForLibraryClass() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        SubclassedClassFilter filter = new SubclassedClassFilter(trackingVisitor);

        // Create a LibraryClass with subclasses
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.subClassCount = 1;

        // Act - Visit the library class
        filter.visitLibraryClass(libraryClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.libraryClassVisited, "Visitor should have been called for subclassed library class");
    }

    /**
     * Tests that the constructor properly handles a null visitor when used.
     * Verifies that SubclassedClassFilter can handle null visitor gracefully during operation.
     */
    @Test
    public void testConstructorWithNullVisitorDuringOperation() {
        // Arrange - Create SubclassedClassFilter with null visitor
        SubclassedClassFilter filter = new SubclassedClassFilter(null);

        // Create a ProgramClass with subclasses
        ProgramClass programClass = new ProgramClass();
        programClass.subClassCount = 1;

        // Act & Assert - Verify that visiting with null visitor throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            filter.visitProgramClass(programClass);
        }, "Null visitor should cause NullPointerException when used");
    }

    /**
     * Tests that the constructor accepts different ClassVisitor implementations.
     * Verifies that SubclassedClassFilter works with various ClassVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentClassVisitorImplementations() {
        // Arrange & Act - Create SubclassedClassFilter with different visitor types
        SubclassedClassFilter filter1 = new SubclassedClassFilter(new TestClassVisitor());
        SubclassedClassFilter filter2 = new SubclassedClassFilter(new TrackingClassVisitor());
        SubclassedClassFilter filter3 = new SubclassedClassFilter(new AnotherTestClassVisitor());

        // Assert - Verify all SubclassedClassFilter instances were created successfully
        assertNotNull(filter1, "SubclassedClassFilter should work with TestClassVisitor");
        assertNotNull(filter2, "SubclassedClassFilter should work with TrackingClassVisitor");
        assertNotNull(filter3, "SubclassedClassFilter should work with AnotherTestClassVisitor");
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
        boolean programClassVisited = false;
        boolean libraryClassVisited = false;

        @Override
        public void visitAnyClass(Clazz clazz) {
            // No-op for testing
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            programClassVisited = true;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            libraryClassVisited = true;
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
