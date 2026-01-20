package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Test class for {@link UnusedParameterMethodFilter} constructor.
 * Tests UnusedParameterMethodFilter(MemberVisitor) constructor.
 *
 * The UnusedParameterMethodFilter is a MemberVisitor that filters methods
 * based on whether they have unused parameters. The constructor takes a
 * MemberVisitor parameter that will be used for delegation when a method
 * with unused parameters is visited.
 */
public class UnusedParameterMethodFilterClaude_constructorTest {

    /**
     * Tests the constructor with a valid MemberVisitor.
     * Verifies that the UnusedParameterMethodFilter instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidMemberVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create UnusedParameterMethodFilter with the visitor
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(visitor);

        // Assert - Verify the UnusedParameterMethodFilter instance was created successfully
        assertNotNull(filter, "UnusedParameterMethodFilter should be instantiated successfully");
    }

    /**
     * Tests the constructor with a null MemberVisitor.
     * Verifies that the UnusedParameterMethodFilter constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullMemberVisitor() {
        // Act - Create UnusedParameterMethodFilter with null visitor
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(null);

        // Assert - Verify the UnusedParameterMethodFilter instance was created
        assertNotNull(filter, "UnusedParameterMethodFilter should be instantiated even with null visitor");
    }

    /**
     * Tests the constructor multiple times.
     * Verifies that multiple UnusedParameterMethodFilter instances can be created independently.
     */
    @Test
    public void testMultipleUnusedParameterMethodFilterInstances() {
        // Arrange - Create two different MemberVisitors
        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new TestMemberVisitor();

        // Act - Create two UnusedParameterMethodFilter instances
        UnusedParameterMethodFilter filter1 = new UnusedParameterMethodFilter(visitor1);
        UnusedParameterMethodFilter filter2 = new UnusedParameterMethodFilter(visitor2);

        // Assert - Verify both UnusedParameterMethodFilter instances were created successfully
        assertNotNull(filter1, "First UnusedParameterMethodFilter instance should be created");
        assertNotNull(filter2, "Second UnusedParameterMethodFilter instance should be created");
        assertNotSame(filter1, filter2, "UnusedParameterMethodFilter instances should be different objects");
    }

    /**
     * Tests the constructor with the same MemberVisitor instance multiple times.
     * Verifies that the same visitor can be used to create multiple UnusedParameterMethodFilter instances.
     */
    @Test
    public void testMultipleUnusedParameterMethodFilterInstancesWithSameVisitor() {
        // Arrange - Create a single MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create multiple UnusedParameterMethodFilter instances with the same visitor
        UnusedParameterMethodFilter filter1 = new UnusedParameterMethodFilter(visitor);
        UnusedParameterMethodFilter filter2 = new UnusedParameterMethodFilter(visitor);

        // Assert - Verify both UnusedParameterMethodFilter instances were created successfully
        assertNotNull(filter1, "First UnusedParameterMethodFilter instance should be created");
        assertNotNull(filter2, "Second UnusedParameterMethodFilter instance should be created");
        assertNotSame(filter1, filter2, "UnusedParameterMethodFilter instances should be different objects");
    }

    /**
     * Tests that the constructor properly stores the visitor by checking behavior.
     * Verifies that the stored visitor is called correctly when filtering methods with unused parameters.
     */
    @Test
    public void testConstructorStoresVisitorCorrectly() {
        // Arrange - Create a tracking visitor
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        // Create a ProgramMethod with unused parameters using mocks
        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V"); // Two int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with unused parameters
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(2);
        methodInfo.setParameterUsed(0); // Mark only first parameter as used, second is unused
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act - Visit the program method
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert - Verify the visitor was called
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should have been called for method with unused parameters");
    }

    /**
     * Tests that the constructor properly stores the visitor and filters methods without unused parameters.
     * Verifies that the stored visitor is NOT called for methods that don't have unused parameters.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyWithMethodWithoutUnusedParameters() {
        // Arrange - Create a tracking visitor
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        // Create a ProgramMethod without unused parameters (all used)
        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V"); // Two int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with all parameters marked as used
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(2);
        methodInfo.updateUsedParameters(0b11L); // Mark both as used
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act - Visit the program method
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert - Verify the visitor was NOT called
        assertFalse(trackingVisitor.visitedProgramMethod, "Visitor should NOT have been called for method without unused parameters");
    }

    /**
     * Tests that the constructor properly handles a null visitor when used.
     * Verifies that UnusedParameterMethodFilter can handle null visitor gracefully during operation.
     */
    @Test
    public void testConstructorWithNullVisitorDuringOperation() {
        // Arrange - Create UnusedParameterMethodFilter with null visitor
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(null);

        // Create a ProgramMethod with unused parameters using mocks
        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with unused parameters
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(2);
        methodInfo.setParameterUsed(0);
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act & Assert - Verify that visiting with null visitor throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            filter.visitProgramMethod(mockClass, mockMethod);
        }, "Null visitor should cause NullPointerException when used");
    }

    /**
     * Tests that the constructor accepts different MemberVisitor implementations.
     * Verifies that UnusedParameterMethodFilter works with various MemberVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentMemberVisitorImplementations() {
        // Arrange & Act - Create UnusedParameterMethodFilter with different visitor types
        UnusedParameterMethodFilter filter1 = new UnusedParameterMethodFilter(new TestMemberVisitor());
        UnusedParameterMethodFilter filter2 = new UnusedParameterMethodFilter(new TrackingMemberVisitor());
        UnusedParameterMethodFilter filter3 = new UnusedParameterMethodFilter(new AnotherTestMemberVisitor());

        // Assert - Verify all UnusedParameterMethodFilter instances were created successfully
        assertNotNull(filter1, "UnusedParameterMethodFilter should work with TestMemberVisitor");
        assertNotNull(filter2, "UnusedParameterMethodFilter should work with TrackingMemberVisitor");
        assertNotNull(filter3, "UnusedParameterMethodFilter should work with AnotherTestMemberVisitor");
    }

    /**
     * Tests that the filter properly delegates based on unused parameter status with multiple calls.
     * Verifies that the constructor stores the visitor correctly for repeated filtering operations.
     */
    @Test
    public void testConstructorStoresVisitorCorrectlyForMultipleCalls() {
        // Arrange - Create a counting visitor
        CountingMemberVisitor countingVisitor = new CountingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(countingVisitor);

        // Create methods with and without unused parameters
        ProgramClass mockClass = mock(ProgramClass.class);

        ProgramMethod methodWithUnusedParams1 = mock(ProgramMethod.class);
        when(methodWithUnusedParams1.getDescriptor(any())).thenReturn("(II)V");
        when(methodWithUnusedParams1.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info1 = new ProgramMethodOptimizationInfo(mockClass, methodWithUnusedParams1);
        info1.setParameterSize(2);
        info1.setParameterUsed(0);
        when(methodWithUnusedParams1.getProcessingInfo()).thenReturn(info1);

        ProgramMethod methodWithUnusedParams2 = mock(ProgramMethod.class);
        when(methodWithUnusedParams2.getDescriptor(any())).thenReturn("(II)V");
        when(methodWithUnusedParams2.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info2 = new ProgramMethodOptimizationInfo(mockClass, methodWithUnusedParams2);
        info2.setParameterSize(2);
        info2.setParameterUsed(0);
        when(methodWithUnusedParams2.getProcessingInfo()).thenReturn(info2);

        ProgramMethod methodWithoutUnusedParams = mock(ProgramMethod.class);
        when(methodWithoutUnusedParams.getDescriptor(any())).thenReturn("(II)V");
        when(methodWithoutUnusedParams.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info3 = new ProgramMethodOptimizationInfo(mockClass, methodWithoutUnusedParams);
        info3.setParameterSize(2);
        info3.updateUsedParameters(0b11L);
        when(methodWithoutUnusedParams.getProcessingInfo()).thenReturn(info3);

        // Act - Visit multiple methods
        filter.visitProgramMethod(mockClass, methodWithUnusedParams1);
        filter.visitProgramMethod(mockClass, methodWithoutUnusedParams);
        filter.visitProgramMethod(mockClass, methodWithUnusedParams2);

        // Assert - Verify the visitor was called only for methods with unused parameters
        assertEquals(2, countingVisitor.visitCount, "Visitor should have been called exactly twice for methods with unused parameters");
    }

    /**
     * Tests that the constructed instance implements MemberVisitor interface.
     */
    @Test
    public void testConstructorCreatesMemberVisitor() {
        // Arrange & Act
        MemberVisitor visitor = new TestMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(visitor);

        // Assert
        assertTrue(filter instanceof MemberVisitor,
                "UnusedParameterMethodFilter should implement MemberVisitor");
    }

    /**
     * Tests that the filter can be used through the MemberVisitor interface.
     */
    @Test
    public void testConstructorCreatesUsableMemberVisitorInterface() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        MemberVisitor filter = new UnusedParameterMethodFilter(trackingVisitor);

        // Create a ProgramMethod with unused parameters
        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(2);
        methodInfo.setParameterUsed(0);
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act - Call through interface
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod,
                "Filter should work when called through MemberVisitor interface");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new UnusedParameterMethodFilter(new TestMemberVisitor()),
                "Constructor should not throw exception with valid visitor");
        assertDoesNotThrow(() -> new UnusedParameterMethodFilter(null),
                "Constructor should not throw exception with null visitor");
    }

    /**
     * Tests that field visitors are not delegated regardless of constructor parameter.
     * Verifies that the filter only delegates method visits, not field visits.
     */
    @Test
    public void testConstructorVisitorNotUsedForFields() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert - Field visits should never be delegated
        assertFalse(trackingVisitor.visitedProgramField,
                "Visitor should NOT be called for field visits");
    }

    /**
     * Tests that library method visitors are not delegated regardless of constructor parameter.
     * Verifies that the filter only delegates program method visits, not library method visits.
     */
    @Test
    public void testConstructorVisitorNotUsedForLibraryMethods() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act
        filter.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - Library method visits should never be delegated
        assertFalse(trackingVisitor.visitedLibraryMethod,
                "Visitor should NOT be called for library method visits");
    }

    /**
     * Simple test MemberVisitor implementation for testing purposes.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }
    }

    /**
     * MemberVisitor implementation that tracks whether it was visited.
     */
    private static class TrackingMemberVisitor implements MemberVisitor {
        boolean visitedProgramField = false;
        boolean visitedProgramMethod = false;
        boolean visitedLibraryField = false;
        boolean visitedLibraryMethod = false;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitedProgramField = true;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitedProgramMethod = true;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitedLibraryField = true;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitedLibraryMethod = true;
        }
    }

    /**
     * MemberVisitor implementation that counts how many times it was called.
     */
    private static class CountingMemberVisitor implements MemberVisitor {
        int visitCount = 0;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitCount++;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitCount++;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitCount++;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitCount++;
        }
    }

    /**
     * Another test MemberVisitor implementation for testing purposes.
     */
    private static class AnotherTestMemberVisitor implements MemberVisitor {
        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }
    }
}
