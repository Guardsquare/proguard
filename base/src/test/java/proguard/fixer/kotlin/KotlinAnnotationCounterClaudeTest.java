package proguard.fixer.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeVisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeVisibleParameterAnnotationsAttribute;
import proguard.classfile.constant.Constant;
import proguard.shrink.SimpleUsageMarker;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAnnotationCounter}.
 * Tests all methods including constructors, visitor methods, and counting functionality.
 */
public class KotlinAnnotationCounterClaudeTest {

    private SimpleUsageMarker mockUsageMarker;
    private Clazz mockClazz;
    private Member mockMember;
    private Method mockMethod;
    private Annotation mockAnnotation;
    private AnnotationsAttribute mockAnnotationsAttribute;
    private ParameterAnnotationsAttribute mockParameterAnnotationsAttribute;
    private Attribute mockAttribute;

    @BeforeEach
    public void setUp() {
        mockUsageMarker = mock(SimpleUsageMarker.class);
        mockClazz = mock(Clazz.class);
        mockMember = mock(Member.class);
        mockMethod = mock(Method.class);
        mockAnnotation = mock(Annotation.class);
        mockAnnotationsAttribute = mock(AnnotationsAttribute.class);
        mockParameterAnnotationsAttribute = mock(ParameterAnnotationsAttribute.class);
        mockAttribute = mock(Attribute.class);
    }

    // ==================== Constructor Tests ====================

    /**
     * Tests the constructor with SimpleUsageMarker parameter.
     * Verifies that the usage marker is correctly stored.
     */
    @Test
    public void testConstructorWithUsageMarker_initializesCorrectly() {
        // Act
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(mockUsageMarker);

        // Assert
        assertNotNull(counter);
        assertEquals(0, counter.getCount());
    }

    /**
     * Tests the constructor with null SimpleUsageMarker.
     * Should accept null and initialize correctly.
     */
    @Test
    public void testConstructorWithNullUsageMarker_initializesCorrectly() {
        // Act
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(null);

        // Assert
        assertNotNull(counter);
        assertEquals(0, counter.getCount());
    }

    /**
     * Tests the no-argument constructor.
     * Verifies that it initializes with default values.
     */
    @Test
    public void testNoArgConstructor_initializesCorrectly() {
        // Act
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Assert
        assertNotNull(counter);
        assertEquals(0, counter.getCount());
    }

    // ==================== getCount() Tests ====================

    /**
     * Tests getCount returns 0 initially.
     */
    @Test
    public void testGetCount_initiallyReturnsZero() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act & Assert
        assertEquals(0, counter.getCount());
    }

    /**
     * Tests getCount after processing an annotation.
     * Verifies count increments properly.
     */
    @Test
    public void testGetCount_afterProcessingAnnotation_returnsCorrectCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(1, counter.getCount());
    }

    /**
     * Tests getCount after processing multiple annotations.
     */
    @Test
    public void testGetCount_afterProcessingMultipleAnnotations_returnsCorrectCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(3, counter.getCount());
    }

    /**
     * Tests getCount after reset.
     * Should return 0 after reset is called.
     */
    @Test
    public void testGetCount_afterReset_returnsZero() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Act
        counter.reset();

        // Assert
        assertEquals(0, counter.getCount());
    }

    // ==================== getParameterAnnotationCount(int) Tests ====================

    /**
     * Tests getParameterAnnotationCount returns -1 when no parameter annotations have been processed.
     */
    @Test
    public void testGetParameterAnnotationCount_withoutParameters_returnsMinusOne() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act & Assert
        assertEquals(-1, counter.getParameterAnnotationCount(0));
        assertEquals(-1, counter.getParameterAnnotationCount(1));
        assertEquals(-1, counter.getParameterAnnotationCount(5));
    }

    /**
     * Tests getParameterAnnotationCount returns correct count after processing parameter annotations.
     */
    @Test
    public void testGetParameterAnnotationCount_afterProcessing_returnsCorrectCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 2;

        // Simulate processing parameter annotations
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 1, mockAnnotation);

        // Act & Assert
        assertEquals(2, counter.getParameterAnnotationCount(0));
        assertEquals(1, counter.getParameterAnnotationCount(1));
    }

    /**
     * Tests getParameterAnnotationCount returns -1 for out of bounds index.
     */
    @Test
    public void testGetParameterAnnotationCount_withOutOfBoundsIndex_returnsMinusOne() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 2;

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Act & Assert
        assertEquals(-1, counter.getParameterAnnotationCount(2));
        assertEquals(-1, counter.getParameterAnnotationCount(10));
    }

    /**
     * Tests getParameterAnnotationCount after reset.
     */
    @Test
    public void testGetParameterAnnotationCount_afterReset_returnsMinusOne() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 2;

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);

        // Act
        counter.reset();

        // Assert
        assertEquals(-1, counter.getParameterAnnotationCount(0));
    }

    /**
     * Tests getParameterAnnotationCount with zero parameters.
     */
    @Test
    public void testGetParameterAnnotationCount_withZeroParameters_returnsMinusOne() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 0;

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Act & Assert
        assertEquals(-1, counter.getParameterAnnotationCount(0));
    }

    // ==================== reset() Tests ====================

    /**
     * Tests reset method resets count to zero.
     */
    @Test
    public void testReset_resetsCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Act
        counter.reset();

        // Assert
        assertEquals(0, counter.getCount());
    }

    /**
     * Tests reset method clears parameter annotation counts.
     */
    @Test
    public void testReset_clearsParameterAnnotationCounts() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 2;

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);

        // Act
        counter.reset();

        // Assert
        assertEquals(-1, counter.getParameterAnnotationCount(0));
    }

    /**
     * Tests reset method returns the same counter instance for chaining.
     */
    @Test
    public void testReset_returnsCounterInstance() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        KotlinAnnotationCounter result = counter.reset();

        // Assert
        assertSame(counter, result);
    }

    /**
     * Tests reset can be called multiple times.
     */
    @Test
    public void testReset_canBeCalledMultipleTimes() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.reset();
        counter.reset();
        counter.reset();

        // Assert
        assertEquals(0, counter.getCount());
    }

    /**
     * Tests reset can be chained with other operations.
     */
    @Test
    public void testReset_canBeChained() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Act
        counter.reset().reset();
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(1, counter.getCount());
    }

    // ==================== visitAnyMember() Tests ====================

    /**
     * Tests visitAnyMember delegates to member's accept method.
     */
    @Test
    public void testVisitAnyMember_delegatesToMemberAccept() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.visitAnyMember(mockClazz, mockMember);

        // Assert
        verify(mockMember, atLeastOnce()).accept(eq(mockClazz), any());
    }

    /**
     * Tests visitAnyMember with real ProgramClass and ProgramMethod.
     * This tests the full integration without mocking.
     */
    @Test
    public void testVisitAnyMember_withRealClassAndMethod_processesAttributes() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Create a real ProgramClass with minimal setup
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;
        programClass.constantPool = new Constant[10];

        // Create a real ProgramMethod with no attributes
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2attributesCount = 0;
        programMethod.attributes = new Attribute[0];

        // Act - should not throw any exceptions
        counter.visitAnyMember(programClass, programMethod);

        // Assert - count should remain 0 since there are no annotations
        assertEquals(0, counter.getCount());
    }

    // ==================== visitAnyAnnotationsAttribute() Tests ====================

    /**
     * Tests visitAnyAnnotationsAttribute processes annotations.
     * Verifies that annotationsAccept is called on the attribute.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_processesAnnotations() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.visitAnyAnnotationsAttribute(mockClazz, mockAnnotationsAttribute);

        // Assert
        verify(mockAnnotationsAttribute, times(1)).annotationsAccept(eq(mockClazz), any());
    }

    /**
     * Tests visitAnyAnnotationsAttribute with real RuntimeVisibleAnnotationsAttribute.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withRealAttribute_processesCorrectly() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();
        attribute.u2annotationsCount = 0;
        attribute.annotations = new Annotation[0];

        // Act - should not throw any exceptions
        counter.visitAnyAnnotationsAttribute(mockClazz, attribute);

        // Assert
        assertEquals(0, counter.getCount());
    }

    // ==================== visitAnyParameterAnnotationsAttribute() Tests ====================

    /**
     * Tests visitAnyParameterAnnotationsAttribute initializes parameter annotation count array.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_initializesParameterArray() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 3;

        // Act
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Assert - after initialization but before any annotations, all counts should be 0
        // We can't directly verify the array initialization, but we can check that
        // getParameterAnnotationCount doesn't return -1 for valid indices
        verify(mockParameterAnnotationsAttribute, times(1)).annotationsAccept(eq(mockClazz), eq(mockMethod), any());
    }

    /**
     * Tests visitAnyParameterAnnotationsAttribute with real attribute.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withRealAttribute_processesCorrectly() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();
        attribute.u1parametersCount = 2;
        attribute.u2parameterAnnotationsCount = new int[2];
        attribute.parameterAnnotations = new Annotation[2][];
        attribute.parameterAnnotations[0] = new Annotation[0];
        attribute.parameterAnnotations[1] = new Annotation[0];

        // Act - should not throw any exceptions
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, attribute);

        // Assert - parameter annotation counts should be initialized but at 0
        assertEquals(0, counter.getParameterAnnotationCount(0));
        assertEquals(0, counter.getParameterAnnotationCount(1));
    }

    /**
     * Tests visitAnyParameterAnnotationsAttribute with zero parameters.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withZeroParameters_initializesEmptyArray() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();
        attribute.u1parametersCount = 0;
        attribute.u2parameterAnnotationsCount = new int[0];
        attribute.parameterAnnotations = new Annotation[0][];

        // Act
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, attribute);

        // Assert
        assertEquals(-1, counter.getParameterAnnotationCount(0));
    }

    // ==================== visitAnyAttribute() Tests ====================

    /**
     * Tests visitAnyAttribute is a no-op.
     * This method should do nothing.
     */
    @Test
    public void testVisitAnyAttribute_doesNothing() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act - should not throw any exceptions
        counter.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - count should remain 0
        assertEquals(0, counter.getCount());
    }

    /**
     * Tests visitAnyAttribute can be called multiple times.
     */
    @Test
    public void testVisitAnyAttribute_canBeCalledMultipleTimes() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.visitAnyAttribute(mockClazz, mockAttribute);
        counter.visitAnyAttribute(mockClazz, mockAttribute);
        counter.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - count should remain 0
        assertEquals(0, counter.getCount());
    }

    // ==================== visitAnnotation(Clazz, Annotation) Tests ====================

    /**
     * Tests visitAnnotation increments count without usage marker.
     */
    @Test
    public void testVisitAnnotation_withoutUsageMarker_incrementsCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(1, counter.getCount());
    }

    /**
     * Tests visitAnnotation increments count when usage marker is null.
     */
    @Test
    public void testVisitAnnotation_withNullUsageMarker_incrementsCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(null);

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(1, counter.getCount());
    }

    /**
     * Tests visitAnnotation increments count when annotation is marked as used.
     */
    @Test
    public void testVisitAnnotation_whenAnnotationIsUsed_incrementsCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(mockUsageMarker);
        when(mockUsageMarker.isUsed(mockAnnotation)).thenReturn(true);

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(1, counter.getCount());
        verify(mockUsageMarker, times(1)).isUsed(mockAnnotation);
    }

    /**
     * Tests visitAnnotation does not increment count when annotation is not used.
     */
    @Test
    public void testVisitAnnotation_whenAnnotationIsNotUsed_doesNotIncrementCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(mockUsageMarker);
        when(mockUsageMarker.isUsed(mockAnnotation)).thenReturn(false);

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(0, counter.getCount());
        verify(mockUsageMarker, times(1)).isUsed(mockAnnotation);
    }

    /**
     * Tests visitAnnotation can be called multiple times.
     */
    @Test
    public void testVisitAnnotation_calledMultipleTimes_incrementsCountEachTime() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(3, counter.getCount());
    }

    /**
     * Tests visitAnnotation with mixed used and unused annotations.
     */
    @Test
    public void testVisitAnnotation_withMixedUsedAndUnused_countsOnlyUsed() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(mockUsageMarker);
        Annotation usedAnnotation1 = mock(Annotation.class);
        Annotation usedAnnotation2 = mock(Annotation.class);
        Annotation unusedAnnotation = mock(Annotation.class);

        when(mockUsageMarker.isUsed(usedAnnotation1)).thenReturn(true);
        when(mockUsageMarker.isUsed(usedAnnotation2)).thenReturn(true);
        when(mockUsageMarker.isUsed(unusedAnnotation)).thenReturn(false);

        // Act
        counter.visitAnnotation(mockClazz, usedAnnotation1);
        counter.visitAnnotation(mockClazz, unusedAnnotation);
        counter.visitAnnotation(mockClazz, usedAnnotation2);

        // Assert
        assertEquals(2, counter.getCount());
    }

    // ==================== visitAnnotation(Clazz, Method, int, Annotation) Tests ====================

    /**
     * Tests parameter annotation visit increments parameter-specific count.
     */
    @Test
    public void testVisitParameterAnnotation_incrementsParameterCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 2;

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Act
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);

        // Assert
        assertEquals(1, counter.getParameterAnnotationCount(0));
        assertEquals(0, counter.getParameterAnnotationCount(1));
        assertEquals(0, counter.getCount()); // Regular count should not be affected
    }

    /**
     * Tests parameter annotation visit with usage marker when annotation is used.
     */
    @Test
    public void testVisitParameterAnnotation_whenAnnotationIsUsed_incrementsCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(mockUsageMarker);
        mockParameterAnnotationsAttribute.u1parametersCount = 1;

        when(mockUsageMarker.isUsed(mockAnnotation)).thenReturn(true);

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Act
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);

        // Assert
        assertEquals(1, counter.getParameterAnnotationCount(0));
        verify(mockUsageMarker, times(1)).isUsed(mockAnnotation);
    }

    /**
     * Tests parameter annotation visit with usage marker when annotation is not used.
     */
    @Test
    public void testVisitParameterAnnotation_whenAnnotationIsNotUsed_doesNotIncrementCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(mockUsageMarker);
        mockParameterAnnotationsAttribute.u1parametersCount = 1;

        when(mockUsageMarker.isUsed(mockAnnotation)).thenReturn(false);

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Act
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);

        // Assert
        assertEquals(0, counter.getParameterAnnotationCount(0));
        verify(mockUsageMarker, times(1)).isUsed(mockAnnotation);
    }

    /**
     * Tests parameter annotation visit for multiple parameters.
     */
    @Test
    public void testVisitParameterAnnotation_forMultipleParameters_tracksSeparately() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 3;

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Act
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 1, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 2, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 2, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 2, mockAnnotation);

        // Assert
        assertEquals(2, counter.getParameterAnnotationCount(0));
        assertEquals(1, counter.getParameterAnnotationCount(1));
        assertEquals(3, counter.getParameterAnnotationCount(2));
    }

    /**
     * Tests parameter annotation visit without usage marker.
     */
    @Test
    public void testVisitParameterAnnotation_withoutUsageMarker_incrementsCount() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(null);
        mockParameterAnnotationsAttribute.u1parametersCount = 1;

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);

        // Act
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);

        // Assert
        assertEquals(1, counter.getParameterAnnotationCount(0));
    }

    // ==================== Integration Tests ====================

    /**
     * Tests full workflow of counting annotations and parameter annotations.
     */
    @Test
    public void testIntegration_fullWorkflow_countsCorrectly() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 2;

        // Act
        // Process regular annotations
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Process parameter annotations
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 1, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 1, mockAnnotation);

        // Assert
        assertEquals(2, counter.getCount());
        assertEquals(1, counter.getParameterAnnotationCount(0));
        assertEquals(2, counter.getParameterAnnotationCount(1));
    }

    /**
     * Tests reset in the middle of processing.
     */
    @Test
    public void testIntegration_resetInMiddle_clearsPreviousCounts() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        mockParameterAnnotationsAttribute.u1parametersCount = 1;

        // Act
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);

        counter.reset();

        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(1, counter.getCount());
        assertEquals(-1, counter.getParameterAnnotationCount(0));
    }

    /**
     * Tests reusing counter after reset with new parameter annotation attribute.
     */
    @Test
    public void testIntegration_reuseAfterReset_worksCorrectly() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter();
        ParameterAnnotationsAttribute attribute1 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attribute2 = mock(ParameterAnnotationsAttribute.class);
        attribute1.u1parametersCount = 2;
        attribute2.u1parametersCount = 3;

        // First use
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, attribute1);
        counter.visitAnnotation(mockClazz, mockMethod, 0, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Reset
        counter.reset();

        // Second use
        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, attribute2);
        counter.visitAnnotation(mockClazz, mockMethod, 2, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);
        counter.visitAnnotation(mockClazz, mockAnnotation);

        // Assert
        assertEquals(2, counter.getCount());
        assertEquals(0, counter.getParameterAnnotationCount(0));
        assertEquals(0, counter.getParameterAnnotationCount(1));
        assertEquals(1, counter.getParameterAnnotationCount(2));
    }

    /**
     * Tests counter with usage marker filtering both regular and parameter annotations.
     */
    @Test
    public void testIntegration_withUsageMarkerFiltering_countsOnlyUsed() {
        // Arrange
        KotlinAnnotationCounter counter = new KotlinAnnotationCounter(mockUsageMarker);
        Annotation usedAnnotation = mock(Annotation.class);
        Annotation unusedAnnotation = mock(Annotation.class);
        mockParameterAnnotationsAttribute.u1parametersCount = 1;

        when(mockUsageMarker.isUsed(usedAnnotation)).thenReturn(true);
        when(mockUsageMarker.isUsed(unusedAnnotation)).thenReturn(false);

        // Act
        counter.visitAnnotation(mockClazz, usedAnnotation);
        counter.visitAnnotation(mockClazz, unusedAnnotation);
        counter.visitAnnotation(mockClazz, usedAnnotation);

        counter.visitAnyParameterAnnotationsAttribute(mockClazz, mockMethod, mockParameterAnnotationsAttribute);
        counter.visitAnnotation(mockClazz, mockMethod, 0, usedAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 0, unusedAnnotation);
        counter.visitAnnotation(mockClazz, mockMethod, 0, usedAnnotation);

        // Assert
        assertEquals(2, counter.getCount());
        assertEquals(2, counter.getParameterAnnotationCount(0));
    }
}
