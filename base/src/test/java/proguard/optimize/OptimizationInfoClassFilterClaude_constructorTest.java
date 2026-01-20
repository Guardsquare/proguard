package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OptimizationInfoClassFilter} constructor.
 * Tests OptimizationInfoClassFilter(ClassVisitor) constructor.
 */
public class OptimizationInfoClassFilterClaude_constructorTest {

    /**
     * Tests the constructor OptimizationInfoClassFilter(ClassVisitor) with a valid ClassVisitor.
     * Verifies that the OptimizationInfoClassFilter instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidClassVisitor() {
        // Arrange - Create a valid ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create OptimizationInfoClassFilter with the visitor
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(visitor);

        // Assert - Verify the OptimizationInfoClassFilter instance was created successfully
        assertNotNull(filter, "OptimizationInfoClassFilter should be instantiated successfully");
    }

    /**
     * Tests the constructor OptimizationInfoClassFilter(ClassVisitor) with a null ClassVisitor.
     * Verifies that the OptimizationInfoClassFilter constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullClassVisitor() {
        // Act - Create OptimizationInfoClassFilter with null visitor
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(null);

        // Assert - Verify the OptimizationInfoClassFilter instance was created
        assertNotNull(filter, "OptimizationInfoClassFilter should be instantiated even with null visitor");
    }

    /**
     * Tests the constructor OptimizationInfoClassFilter(ClassVisitor) multiple times.
     * Verifies that multiple OptimizationInfoClassFilter instances can be created independently.
     */
    @Test
    public void testMultipleOptimizationInfoClassFilterInstances() {
        // Arrange - Create two different ClassVisitors
        ClassVisitor visitor1 = new TestClassVisitor();
        ClassVisitor visitor2 = new TestClassVisitor();

        // Act - Create two OptimizationInfoClassFilter instances
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(visitor1);
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(visitor2);

        // Assert - Verify both OptimizationInfoClassFilter instances were created successfully
        assertNotNull(filter1, "First OptimizationInfoClassFilter instance should be created");
        assertNotNull(filter2, "Second OptimizationInfoClassFilter instance should be created");
        assertNotSame(filter1, filter2, "OptimizationInfoClassFilter instances should be different objects");
    }

    /**
     * Tests the constructor OptimizationInfoClassFilter(ClassVisitor) with the same ClassVisitor instance multiple times.
     * Verifies that the same visitor can be used to create multiple OptimizationInfoClassFilter instances.
     */
    @Test
    public void testMultipleOptimizationInfoClassFilterInstancesWithSameVisitor() {
        // Arrange - Create a single ClassVisitor
        ClassVisitor visitor = new TestClassVisitor();

        // Act - Create multiple OptimizationInfoClassFilter instances with the same visitor
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(visitor);
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(visitor);

        // Assert - Verify both OptimizationInfoClassFilter instances were created successfully
        assertNotNull(filter1, "First OptimizationInfoClassFilter instance should be created");
        assertNotNull(filter2, "Second OptimizationInfoClassFilter instance should be created");
        assertNotSame(filter1, filter2, "OptimizationInfoClassFilter instances should be different objects");
    }

    /**
     * Tests that the constructor properly stores the visitor by checking behavior.
     * Verifies that the stored visitor is called correctly when filtering classes with ProgramClassOptimizationInfo.
     */
    @Test
    public void testConstructorStoresVisitorCorrectly() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(trackingVisitor);

        // Create a ProgramClass with ProgramClassOptimizationInfo
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - Visit the program class
        filter.visitProgramClass(programClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.programClassVisited, "Visitor should have been called for program class with ProgramClassOptimizationInfo");
    }

    /**
     * Tests that the constructor properly stores the visitor by checking behavior with LibraryClass.
     * Verifies that the stored visitor is called correctly when filtering library classes with ProgramClassOptimizationInfo.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyForLibraryClass() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(trackingVisitor);

        // Create a LibraryClass with ProgramClassOptimizationInfo
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act - Visit the library class
        filter.visitLibraryClass(libraryClass);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.libraryClassVisited, "Visitor should have been called for library class with ProgramClassOptimizationInfo");
    }

    /**
     * Tests that the constructor properly handles a null visitor when used.
     * Verifies that OptimizationInfoClassFilter can handle null visitor gracefully during operation.
     */
    @Test
    public void testConstructorWithNullVisitorDuringOperation() {
        // Arrange - Create OptimizationInfoClassFilter with null visitor
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(null);

        // Create a ProgramClass with ProgramClassOptimizationInfo
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - Verify that visiting with null visitor throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            filter.visitProgramClass(programClass);
        }, "Null visitor should cause NullPointerException when used");
    }

    /**
     * Tests that the constructor accepts different ClassVisitor implementations.
     * Verifies that OptimizationInfoClassFilter works with various ClassVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentClassVisitorImplementations() {
        // Arrange & Act - Create OptimizationInfoClassFilter with different visitor types
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(new TestClassVisitor());
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(new TrackingClassVisitor());
        OptimizationInfoClassFilter filter3 = new OptimizationInfoClassFilter(new AnotherTestClassVisitor());

        // Assert - Verify all OptimizationInfoClassFilter instances were created successfully
        assertNotNull(filter1, "OptimizationInfoClassFilter should work with TestClassVisitor");
        assertNotNull(filter2, "OptimizationInfoClassFilter should work with TrackingClassVisitor");
        assertNotNull(filter3, "OptimizationInfoClassFilter should work with AnotherTestClassVisitor");
    }

    /**
     * Tests that the constructor stores the visitor correctly and the filter does not call the visitor
     * when the class has basic ClassOptimizationInfo (not ProgramClassOptimizationInfo).
     */
    @Test
    public void testConstructorStoresVisitorButDoesNotCallWhenBasicOptimizationInfo() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(trackingVisitor);

        // Create a ProgramClass with basic ClassOptimizationInfo (not editable)
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act - Visit the program class
        filter.visitProgramClass(programClass);

        // Assert - Verify the visitor was NOT called (because info is not ProgramClassOptimizationInfo)
        assertFalse(trackingVisitor.programClassVisited, "Visitor should not be called for program class without ProgramClassOptimizationInfo");
    }

    /**
     * Tests that the constructor stores the visitor correctly and the filter does not call the visitor
     * when the library class has basic ClassOptimizationInfo (not ProgramClassOptimizationInfo).
     */
    @Test
    public void testConstructorStoresVisitorButDoesNotCallForLibraryClassWithBasicInfo() {
        // Arrange - Create a tracking visitor
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(trackingVisitor);

        // Create a LibraryClass with basic ClassOptimizationInfo (not editable)
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act - Visit the library class
        filter.visitLibraryClass(libraryClass);

        // Assert - Verify the visitor was NOT called (because info is not ProgramClassOptimizationInfo)
        assertFalse(trackingVisitor.libraryClassVisited, "Visitor should not be called for library class without ProgramClassOptimizationInfo");
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
