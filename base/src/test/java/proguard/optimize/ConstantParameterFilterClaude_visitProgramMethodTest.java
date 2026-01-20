package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.AccessConstants;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.VersionConstants;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.value.Value;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ConstantParameterFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod method in ConstantParameterFilter delegates to an inner MemberVisitor
 * for each method parameter that has been marked as constant (particular).
 *
 * The method:
 * 1. Determines whether the method is static or not (affects parameter indexing)
 * 2. Calculates the parameter start index (0 for static, 1 for instance methods due to 'this')
 * 3. Iterates through all method parameters based on the method descriptor
 * 4. For each parameter with a particular (constant) value, delegates to the constantParameterVisitor
 *
 * These tests verify:
 * - Correct handling of static vs instance methods
 * - Correct parameter counting and iteration
 * - Delegation only for parameters with particular values
 * - No delegation for parameters with non-particular or null values
 * - Proper handling of methods with various parameter types
 */
public class ConstantParameterFilterClaude_visitProgramMethodTest {

    private MemberVisitor mockInnerVisitor;
    private ConstantParameterFilter filter;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        mockInnerVisitor = mock(MemberVisitor.class);
        filter = new ConstantParameterFilter(mockInnerVisitor);

        // Create a basic program class with proper initialization
        programClass = new ProgramClass();
        programClass.u4version = VersionConstants.CLASS_VERSION_1_8;
    }

    /**
     * Tests that a static method with one constant parameter delegates once to the visitor.
     * Static methods start parameter indexing at 0.
     */
    @Test
    public void testVisitProgramMethod_staticMethodWithOneConstantParameter_delegatesOnce() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;
        programMethod.u2nameIndex = 0;
        programMethod.u2descriptorIndex = 0;

        // Set up a method descriptor: (I)V - one int parameter, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(I)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with one particular parameter at index 0
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that an instance method with one constant parameter delegates once.
     * Instance methods start parameter indexing at 1 (index 0 is 'this').
     */
    @Test
    public void testVisitProgramMethod_instanceMethodWithOneConstantParameter_delegatesOnce() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = 0; // Not static

        // Set up a method descriptor: (I)V - one int parameter, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(I)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with one particular parameter at index 1 (index 0 is 'this')
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(1)).thenReturn(particularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that a method with no constant parameters doesn't delegate.
     */
    @Test
    public void testVisitProgramMethod_noConstantParameters_doesNotDelegate() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        // Set up a method descriptor: (I)V - one int parameter, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(I)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with non-particular parameter
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);
        when(optimizationInfo.getParameterValue(0)).thenReturn(nonParticularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, never()).visitProgramMethod(any(), any());
    }

    /**
     * Tests that a method with multiple constant parameters delegates multiple times.
     */
    @Test
    public void testVisitProgramMethod_staticMethodWithMultipleConstantParameters_delegatesMultipleTimes() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        // Set up a method descriptor: (III)V - three int parameters, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(III)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with three particular parameters
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        for (int i = 0; i < 3; i++) {
            Value particularValue = mock(Value.class);
            when(particularValue.isParticular()).thenReturn(true);
            when(optimizationInfo.getParameterValue(i)).thenReturn(particularValue);
        }
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - should delegate once per constant parameter
        verify(mockInnerVisitor, times(3)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that a method with mixed constant and non-constant parameters
     * delegates only for the constant ones.
     */
    @Test
    public void testVisitProgramMethod_mixedConstantAndNonConstantParameters_delegatesSelectively() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        // Set up a method descriptor: (III)V - three int parameters, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(III)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info: param 0 is particular, param 1 is not, param 2 is particular
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);

        Value particularValue0 = mock(Value.class);
        when(particularValue0.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue0);

        Value nonParticularValue1 = mock(Value.class);
        when(nonParticularValue1.isParticular()).thenReturn(false);
        when(optimizationInfo.getParameterValue(1)).thenReturn(nonParticularValue1);

        Value particularValue2 = mock(Value.class);
        when(particularValue2.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(2)).thenReturn(particularValue2);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - should delegate twice (for params 0 and 2)
        verify(mockInnerVisitor, times(2)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that a method with null parameter values doesn't delegate.
     */
    @Test
    public void testVisitProgramMethod_nullParameterValue_doesNotDelegate() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        // Set up a method descriptor: (I)V - one int parameter, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(I)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with null parameter value
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getParameterValue(0)).thenReturn(null);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, never()).visitProgramMethod(any(), any());
    }

    /**
     * Tests that a method with no parameters doesn't delegate.
     */
    @Test
    public void testVisitProgramMethod_noParameters_doesNotDelegate() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        // Set up a method descriptor: ()V - no parameters, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2descriptorIndex = 1;

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - no parameters means no delegation
        verify(mockInnerVisitor, never()).visitProgramMethod(any(), any());
    }

    /**
     * Tests that a static method with long and double parameters (category 2 types) is handled correctly.
     * Long and double take 2 slots in the local variable frame but count as single parameters.
     */
    @Test
    public void testVisitProgramMethod_staticMethodWithLongAndDoubleParameters_delegatesCorrectly() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        // Set up a method descriptor: (JD)V - long and double parameters, void return
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(JD)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with particular parameters
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue0 = mock(Value.class);
        when(particularValue0.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue0);

        Value particularValue1 = mock(Value.class);
        when(particularValue1.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(1)).thenReturn(particularValue1);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - should delegate twice (once for each parameter)
        verify(mockInnerVisitor, times(2)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that an instance method with reference type parameters is handled correctly.
     */
    @Test
    public void testVisitProgramMethod_instanceMethodWithObjectParameters_delegatesCorrectly() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = 0; // Not static

        // Set up a method descriptor: (Ljava/lang/String;Ljava/lang/Object;)V - two object parameters
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("(Ljava/lang/String;Ljava/lang/Object;)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with particular parameters (starting at index 1 for instance method)
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue1 = mock(Value.class);
        when(particularValue1.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(1)).thenReturn(particularValue1);

        Value particularValue2 = mock(Value.class);
        when(particularValue2.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(2)).thenReturn(particularValue2);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - should delegate twice (once for each parameter)
        verify(mockInnerVisitor, times(2)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that the filter can be reused across multiple method visits.
     */
    @Test
    public void testVisitProgramMethod_multipleMethodCalls_filterIsReusable() {
        // Arrange
        ProgramMethod method1 = createStaticMethodWithDescriptor("(I)V");
        ProgramMethod method2 = createStaticMethodWithDescriptor("(I)V");

        // Both methods have one constant parameter
        for (ProgramMethod method : new ProgramMethod[]{method1, method2}) {
            ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
            Value particularValue = mock(Value.class);
            when(particularValue.isParticular()).thenReturn(true);
            when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue);
            method.setProcessingInfo(optimizationInfo);
        }

        // Act
        filter.visitProgramMethod(programClass, method1);
        filter.visitProgramMethod(programClass, method2);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, method1);
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, method2);
    }

    /**
     * Tests that exceptions from the inner visitor are propagated.
     */
    @Test
    public void testVisitProgramMethod_visitorThrowsException_propagatesException() {
        // Arrange
        ProgramMethod programMethod = createStaticMethodWithDescriptor("(I)V");

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException).when(mockInnerVisitor).visitProgramMethod(any(), any());

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            filter.visitProgramMethod(programClass, programMethod));
        assertEquals(expectedException, thrownException);
    }

    /**
     * Tests that a method with array parameters is handled correctly.
     */
    @Test
    public void testVisitProgramMethod_staticMethodWithArrayParameter_delegatesCorrectly() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        // Set up a method descriptor: ([I[Ljava/lang/String;)V - int array and String array parameters
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("([I[Ljava/lang/String;)V");
        programMethod.u2descriptorIndex = 1;

        // Set up optimization info with particular parameters
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue0 = mock(Value.class);
        when(particularValue0.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue0);

        Value particularValue1 = mock(Value.class);
        when(particularValue1.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(1)).thenReturn(particularValue1);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - should delegate twice
        verify(mockInnerVisitor, times(2)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that an instance method with no parameters (only 'this') doesn't delegate.
     */
    @Test
    public void testVisitProgramMethod_instanceMethodWithNoParameters_doesNotDelegate() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = 0; // Not static

        // Set up a method descriptor: ()V - no parameters (only implicit 'this')
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2descriptorIndex = 1;

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - no explicit parameters means no delegation
        verify(mockInnerVisitor, never()).visitProgramMethod(any(), any());
    }

    /**
     * Tests that multiple filters work independently without interfering with each other.
     */
    @Test
    public void testVisitProgramMethod_multipleFilters_workIndependently() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);
        ConstantParameterFilter filter1 = new ConstantParameterFilter(visitor1);
        ConstantParameterFilter filter2 = new ConstantParameterFilter(visitor2);

        ProgramMethod programMethod = createStaticMethodWithDescriptor("(I)V");
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter1.visitProgramMethod(programClass, programMethod);
        filter2.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(visitor1, times(1)).visitProgramMethod(programClass, programMethod);
        verify(visitor2, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that the filter passes the correct ProgramClass and ProgramMethod to the visitor.
     */
    @Test
    public void testVisitProgramMethod_passesCorrectParameters() {
        // Arrange
        ProgramMethod programMethod = createStaticMethodWithDescriptor("(I)V");
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - verify correct parameters are passed
        verify(mockInnerVisitor).visitProgramMethod(
            argThat(arg -> arg == programClass),
            argThat(arg -> arg == programMethod)
        );
    }

    /**
     * Helper method to create a static method with a given descriptor.
     */
    private ProgramMethod createStaticMethodWithDescriptor(String descriptor) {
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = AccessConstants.STATIC;

        if (programClass.constantPool == null) {
            programClass.constantPool = new proguard.classfile.constant.Constant[3];
        }
        programClass.constantPool[0] = new proguard.classfile.constant.Utf8Constant("testMethod");
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant(descriptor);
        programMethod.u2descriptorIndex = 1;

        return programMethod;
    }

    /**
     * Tests that a method with many parameters where only the first is constant delegates once.
     */
    @Test
    public void testVisitProgramMethod_manyParametersFirstIsConstant_delegatesOnce() {
        // Arrange
        ProgramMethod programMethod = createStaticMethodWithDescriptor("(IIIIII)V");

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);

        // Only first parameter is particular
        Value particularValue0 = mock(Value.class);
        when(particularValue0.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue0);

        // Rest are non-particular
        for (int i = 1; i < 6; i++) {
            Value nonParticularValue = mock(Value.class);
            when(nonParticularValue.isParticular()).thenReturn(false);
            when(optimizationInfo.getParameterValue(i)).thenReturn(nonParticularValue);
        }

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - should delegate once
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that a method with many parameters where only the last is constant delegates once.
     */
    @Test
    public void testVisitProgramMethod_manyParametersLastIsConstant_delegatesOnce() {
        // Arrange
        ProgramMethod programMethod = createStaticMethodWithDescriptor("(IIIIII)V");

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);

        // First 5 parameters are non-particular
        for (int i = 0; i < 5; i++) {
            Value nonParticularValue = mock(Value.class);
            when(nonParticularValue.isParticular()).thenReturn(false);
            when(optimizationInfo.getParameterValue(i)).thenReturn(nonParticularValue);
        }

        // Last parameter is particular
        Value particularValue5 = mock(Value.class);
        when(particularValue5.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(5)).thenReturn(particularValue5);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - should delegate once
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that calling the same method multiple times with the same constant parameter
     * delegates each time.
     */
    @Test
    public void testVisitProgramMethod_sameMethodMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramMethod programMethod = createStaticMethodWithDescriptor("(I)V");
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act - visit 5 times
        for (int i = 0; i < 5; i++) {
            filter.visitProgramMethod(programClass, programMethod);
        }

        // Assert - should delegate 5 times
        verify(mockInnerVisitor, times(5)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that only visitProgramMethod is called on the visitor, not other methods.
     */
    @Test
    public void testVisitProgramMethod_onlyCallsVisitProgramMethod() {
        // Arrange
        ProgramMethod programMethod = createStaticMethodWithDescriptor("(I)V");
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        when(optimizationInfo.getParameterValue(0)).thenReturn(particularValue);
        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - only visitProgramMethod should be called
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
        verifyNoMoreInteractions(mockInnerVisitor);
    }
}
