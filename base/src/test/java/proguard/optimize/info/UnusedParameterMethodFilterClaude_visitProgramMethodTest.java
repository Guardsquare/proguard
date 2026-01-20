package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod method checks if a method has unused parameters
 * and delegates to the wrapped visitor only if unused parameters exist.
 */
public class UnusedParameterMethodFilterClaude_visitProgramMethodTest {

    /**
     * Tests that visitProgramMethod delegates to the wrapped visitor when method has unused parameters.
     * This is the primary behavior of the filter - it should pass through methods with unused parameters.
     */
    @Test
    public void testVisitProgramMethodWithUnusedParameters() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V"); // Two int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with unused parameters
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(2);
        methodInfo.setParameterUsed(0); // Mark only first parameter as used, second is unused
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should have been called for method with unused parameters");
        assertSame(mockClass, trackingVisitor.lastVisitedClass, "Correct class should be passed to visitor");
        assertSame(mockMethod, trackingVisitor.lastVisitedMethod, "Correct method should be passed to visitor");
    }

    /**
     * Tests that visitProgramMethod does NOT delegate when method has all parameters used.
     * This tests the filtering behavior - methods without unused parameters should be filtered out.
     */
    @Test
    public void testVisitProgramMethodWithAllParametersUsed() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V"); // Two int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with all parameters marked as used
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(2);
        methodInfo.updateUsedParameters(0b11L); // Mark both as used
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertFalse(trackingVisitor.visitedProgramMethod, "Visitor should NOT have been called for method without unused parameters");
        assertNull(trackingVisitor.lastVisitedClass, "No class should be passed to visitor");
        assertNull(trackingVisitor.lastVisitedMethod, "No method should be passed to visitor");
    }

    /**
     * Tests that visitProgramMethod delegates when no parameters are marked as used (all unused).
     */
    @Test
    public void testVisitProgramMethodWithNoParametersUsed() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(III)V"); // Three int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with no parameters marked as used
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(3);
        // Don't mark any parameters as used
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should have been called when all parameters are unused");
        assertSame(mockClass, trackingVisitor.lastVisitedClass, "Correct class should be passed to visitor");
        assertSame(mockMethod, trackingVisitor.lastVisitedMethod, "Correct method should be passed to visitor");
    }

    /**
     * Tests visitProgramMethod with a method that has no parameters (parameterSize = 0).
     * Methods with no parameters should not have unused parameters.
     */
    @Test
    public void testVisitProgramMethodWithNoParameters() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("()V"); // No parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with no parameters
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(0);
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertFalse(trackingVisitor.visitedProgramMethod, "Visitor should NOT be called for method with no parameters");
    }

    /**
     * Tests visitProgramMethod with a non-static method where 'this' parameter is used but others are not.
     */
    @Test
    public void testVisitProgramMethodWithThisParameterUsedOthersUnused() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V"); // Two int parameters
        when(mockMethod.getAccessFlags()).thenReturn(0); // Non-static

        // Set up method optimization info - 'this' + 2 int parameters = 3 parameters total
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(3);
        methodInfo.setParameterUsed(0); // Mark 'this' parameter as used
        // Parameters at indices 1 and 2 are unused
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should be called when some parameters are unused");
    }

    /**
     * Tests visitProgramMethod with a method that has one parameter used and one unused.
     */
    @Test
    public void testVisitProgramMethodWithMixedUsedUnusedParameters() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(IIII)V"); // Four int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Set up method optimization info with mixed used/unused
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(4);
        methodInfo.setParameterUsed(0); // First used
        methodInfo.setParameterUsed(2); // Third used
        // Second and fourth are unused
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should be called when some parameters are unused");
    }

    /**
     * Tests visitProgramMethod is called multiple times with different methods.
     * Verifies that the filter correctly handles each invocation independently.
     */
    @Test
    public void testVisitProgramMethodMultipleTimes() {
        // Arrange
        CountingMemberVisitor countingVisitor = new CountingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(countingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);

        // Create first method with unused parameters
        ProgramMethod method1 = mock(ProgramMethod.class);
        when(method1.getDescriptor(any())).thenReturn("(II)V");
        when(method1.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info1 = new ProgramMethodOptimizationInfo(mockClass, method1);
        info1.setParameterSize(2);
        info1.setParameterUsed(0);
        when(method1.getProcessingInfo()).thenReturn(info1);

        // Create second method without unused parameters
        ProgramMethod method2 = mock(ProgramMethod.class);
        when(method2.getDescriptor(any())).thenReturn("(II)V");
        when(method2.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info2 = new ProgramMethodOptimizationInfo(mockClass, method2);
        info2.setParameterSize(2);
        info2.updateUsedParameters(0b11L);
        when(method2.getProcessingInfo()).thenReturn(info2);

        // Create third method with unused parameters
        ProgramMethod method3 = mock(ProgramMethod.class);
        when(method3.getDescriptor(any())).thenReturn("(I)V");
        when(method3.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info3 = new ProgramMethodOptimizationInfo(mockClass, method3);
        info3.setParameterSize(1);
        // Don't mark as used
        when(method3.getProcessingInfo()).thenReturn(info3);

        // Act
        filter.visitProgramMethod(mockClass, method1);
        filter.visitProgramMethod(mockClass, method2);
        filter.visitProgramMethod(mockClass, method3);

        // Assert
        assertEquals(2, countingVisitor.visitProgramMethodCount, "Visitor should be called exactly twice");
    }

    /**
     * Tests that visitProgramMethod correctly passes both arguments to the wrapped visitor.
     */
    @Test
    public void testVisitProgramMethodPassesCorrectArguments() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass1 = mock(ProgramClass.class);
        ProgramMethod mockMethod1 = mock(ProgramMethod.class);
        when(mockMethod1.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod1.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass1, mockMethod1);
        methodInfo.setParameterSize(1);
        when(mockMethod1.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass1, mockMethod1);

        // Assert
        assertSame(mockClass1, trackingVisitor.lastVisitedClass, "Correct class should be passed");
        assertSame(mockMethod1, trackingVisitor.lastVisitedMethod, "Correct method should be passed");
    }

    /**
     * Tests visitProgramMethod with different ProgramClass objects.
     */
    @Test
    public void testVisitProgramMethodWithDifferentClasses() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass1 = mock(ProgramClass.class);
        ProgramClass mockClass2 = mock(ProgramClass.class);

        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass1, mockMethod);
        methodInfo.setParameterSize(1);
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act - visit with different classes
        trackingVisitor.reset();
        filter.visitProgramMethod(mockClass1, mockMethod);
        ProgramClass firstClass = trackingVisitor.lastVisitedClass;

        trackingVisitor.reset();
        filter.visitProgramMethod(mockClass2, mockMethod);
        ProgramClass secondClass = trackingVisitor.lastVisitedClass;

        // Assert
        assertSame(mockClass1, firstClass, "First class should be passed correctly");
        assertSame(mockClass2, secondClass, "Second class should be passed correctly");
        assertNotSame(firstClass, secondClass, "Classes should be different");
    }

    /**
     * Tests visitProgramMethod with a method having large number of parameters.
     * This tests the edge case where parameterSize approaches or exceeds 64.
     */
    @Test
    public void testVisitProgramMethodWithManyParameters() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        // Method with 10 int parameters
        when(mockMethod.getDescriptor(any())).thenReturn("(IIIIIIIIII)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(10);
        // Mark first 9 as used, leave last one unused
        for (int i = 0; i < 9; i++) {
            methodInfo.setParameterUsed(i);
        }
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should be called for method with many parameters and some unused");
    }

    /**
     * Tests that visitProgramMethod works correctly when the same method is visited multiple times.
     */
    @Test
    public void testVisitProgramMethodSameMethodMultipleTimes() {
        // Arrange
        CountingMemberVisitor countingVisitor = new CountingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(countingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(1);
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act - visit the same method 3 times
        filter.visitProgramMethod(mockClass, mockMethod);
        filter.visitProgramMethod(mockClass, mockMethod);
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertEquals(3, countingVisitor.visitProgramMethodCount, "Visitor should be called each time for method with unused parameters");
    }

    /**
     * Tests visitProgramMethod with a method having only the last parameter unused.
     */
    @Test
    public void testVisitProgramMethodWithOnlyLastParameterUnused() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(III)V"); // Three int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(3);
        methodInfo.setParameterUsed(0);
        methodInfo.setParameterUsed(1);
        // Parameter 2 is unused
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should be called when last parameter is unused");
    }

    /**
     * Tests visitProgramMethod with a method having only the first parameter unused.
     */
    @Test
    public void testVisitProgramMethodWithOnlyFirstParameterUnused() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(III)V"); // Three int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(3);
        // Parameter 0 is unused
        methodInfo.setParameterUsed(1);
        methodInfo.setParameterUsed(2);
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should be called when first parameter is unused");
    }

    /**
     * Tests visitProgramMethod with a method having alternating used/unused parameters.
     */
    @Test
    public void testVisitProgramMethodWithAlternatingUsedUnused() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(IIIIII)V"); // Six int parameters
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(6);
        // Mark even indices as used (0, 2, 4)
        methodInfo.setParameterUsed(0);
        methodInfo.setParameterUsed(2);
        methodInfo.setParameterUsed(4);
        // Odd indices (1, 3, 5) are unused
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should be called when some parameters are unused");
    }

    /**
     * Tests visitProgramMethod ensures the visitor is called exactly once per valid call.
     */
    @Test
    public void testVisitProgramMethodCallsVisitorOnlyOnce() {
        // Arrange
        CountingMemberVisitor countingVisitor = new CountingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(countingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(1);
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertEquals(1, countingVisitor.visitProgramMethodCount, "Visitor should be called exactly once");
    }

    /**
     * Tests visitProgramMethod with various parameter types (long, double which are Category 2).
     */
    @Test
    public void testVisitProgramMethodWithCategory2Parameters() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter = new UnusedParameterMethodFilter(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);
        when(mockMethod.getDescriptor(any())).thenReturn("(JD)V"); // long and double
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // long takes 2 slots (index 0-1), double takes 2 slots (index 2-3)
        ProgramMethodOptimizationInfo methodInfo = new ProgramMethodOptimizationInfo(mockClass, mockMethod);
        methodInfo.setParameterSize(4); // Total of 4 parameter slots
        methodInfo.setParameterUsed(0); // Mark first slot of long as used
        methodInfo.setParameterUsed(1); // Mark second slot of long as used
        // double parameter (indices 2-3) are unused
        when(mockMethod.getProcessingInfo()).thenReturn(methodInfo);

        // Act
        filter.visitProgramMethod(mockClass, mockMethod);

        // Assert
        assertTrue(trackingVisitor.visitedProgramMethod, "Visitor should be called when Category 2 parameters are unused");
    }

    /**
     * Helper class: MemberVisitor that tracks if it was called and stores the last visited items.
     */
    private static class TrackingMemberVisitor implements MemberVisitor {
        boolean visitedProgramMethod = false;
        ProgramClass lastVisitedClass = null;
        ProgramMethod lastVisitedMethod = null;

        void reset() {
            visitedProgramMethod = false;
            lastVisitedClass = null;
            lastVisitedMethod = null;
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // Not used in this test
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitedProgramMethod = true;
            lastVisitedClass = programClass;
            lastVisitedMethod = programMethod;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // Not used in this test
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // Not used in this test
        }
    }

    /**
     * Helper class: MemberVisitor that counts how many times each method is called.
     */
    private static class CountingMemberVisitor implements MemberVisitor {
        int visitProgramMethodCount = 0;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // Not used in this test
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitProgramMethodCount++;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // Not used in this test
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // Not used in this test
        }
    }
}
