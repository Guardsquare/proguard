package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AccessMethodMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
 *
 * The visitInvokeDynamicConstant method processes InvokeDynamic constants by calling
 * bootstrapMethodHandleAccept on the constant, which triggers further processing of the
 * bootstrap method handle. This is part of the visitor pattern used to analyze what
 * access levels (private, protected, package) are accessed through invokedynamic instructions.
 */
public class AccessMethodMarkerClaude_visitInvokeDynamicConstantTest {

    private AccessMethodMarker marker;
    private Clazz clazz;
    private InvokeDynamicConstant invokeDynamicConstant;

    @BeforeEach
    public void setUp() {
        marker = new AccessMethodMarker();
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);
    }

    /**
     * Tests that visitInvokeDynamicConstant calls bootstrapMethodHandleAccept with the correct parameters.
     * This verifies the basic flow of the method.
     */
    @Test
    public void testVisitInvokeDynamicConstant_callsBootstrapMethodHandleAccept() {
        // Act
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify bootstrapMethodHandleAccept was called with the clazz and marker
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant passes the marker itself as the visitor.
     * This is important because the marker implements ConstantVisitor.
     */
    @Test
    public void testVisitInvokeDynamicConstant_passesMarkerAsVisitor() {
        // Act
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify the marker itself is passed as the visitor
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(any(Clazz.class), same(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant works with different clazz instances.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withDifferentClazzInstances_acceptsCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act
        marker.visitInvokeDynamicConstant(clazz1, invokeDynamicConstant);
        marker.visitInvokeDynamicConstant(clazz2, invokeDynamicConstant);
        marker.visitInvokeDynamicConstant(clazz3, invokeDynamicConstant);

        // Assert - verify each clazz was passed to bootstrapMethodHandleAccept
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz1), eq(marker));
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz2), eq(marker));
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz3), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant can be called multiple times.
     */
    @Test
    public void testVisitInvokeDynamicConstant_calledMultipleTimes_acceptsEachTime() {
        // Act
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify bootstrapMethodHandleAccept was called 3 times
        verify(invokeDynamicConstant, times(3)).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant works with different InvokeDynamicConstant instances.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withDifferentInvokeDynamicConstants_acceptsCorrectly() {
        // Arrange
        InvokeDynamicConstant constant1 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant2 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant3 = mock(InvokeDynamicConstant.class);

        // Act
        marker.visitInvokeDynamicConstant(clazz, constant1);
        marker.visitInvokeDynamicConstant(clazz, constant2);
        marker.visitInvokeDynamicConstant(clazz, constant3);

        // Assert - verify each constant's bootstrapMethodHandleAccept was called
        verify(constant1).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
        verify(constant2).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
        verify(constant3).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant doesn't throw exceptions with valid parameters.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withValidParameters_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant));
    }

    /**
     * Tests that multiple AccessMethodMarker instances work independently.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        InvokeDynamicConstant constant1 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant2 = mock(InvokeDynamicConstant.class);

        // Act
        marker1.visitInvokeDynamicConstant(clazz, constant1);
        marker2.visitInvokeDynamicConstant(clazz, constant2);

        // Assert - verify each marker accessed its respective constant
        verify(constant1).bootstrapMethodHandleAccept(eq(clazz), eq(marker1));
        verify(constant2).bootstrapMethodHandleAccept(eq(clazz), eq(marker2));
    }

    /**
     * Tests that visitInvokeDynamicConstant can handle being called in rapid succession.
     */
    @Test
    public void testVisitInvokeDynamicConstant_rapidSuccessiveCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }

        // Assert
        verify(invokeDynamicConstant, times(100)).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant is stateless between calls.
     * Each call should independently call bootstrapMethodHandleAccept.
     */
    @Test
    public void testVisitInvokeDynamicConstant_isStatelessBetweenCalls() {
        // Arrange
        InvokeDynamicConstant constant1 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant2 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant3 = mock(InvokeDynamicConstant.class);

        // Act - interleave calls
        marker.visitInvokeDynamicConstant(clazz, constant1);
        marker.visitInvokeDynamicConstant(clazz, constant2);
        marker.visitInvokeDynamicConstant(clazz, constant3);
        marker.visitInvokeDynamicConstant(clazz, constant1);

        // Assert - verify each call was processed correctly
        verify(constant1, times(2)).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
        verify(constant2, times(1)).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
        verify(constant3, times(1)).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant passes the correct clazz parameter.
     */
    @Test
    public void testVisitInvokeDynamicConstant_passesCorrectClazz() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class);

        // Act
        marker.visitInvokeDynamicConstant(specificClazz, invokeDynamicConstant);

        // Assert - verify the specific clazz was passed
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(same(specificClazz), any());
    }

    /**
     * Tests that visitInvokeDynamicConstant works with different combinations of parameters.
     */
    @Test
    public void testVisitInvokeDynamicConstant_variousCombinations_allWorkCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        InvokeDynamicConstant constant1 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant2 = mock(InvokeDynamicConstant.class);

        // Act
        marker.visitInvokeDynamicConstant(clazz1, constant1);
        marker.visitInvokeDynamicConstant(clazz1, constant2);
        marker.visitInvokeDynamicConstant(clazz2, constant1);
        marker.visitInvokeDynamicConstant(clazz2, constant2);

        // Assert - verify all combinations were processed
        verify(constant1).bootstrapMethodHandleAccept(eq(clazz1), eq(marker));
        verify(constant2).bootstrapMethodHandleAccept(eq(clazz1), eq(marker));
        verify(constant1).bootstrapMethodHandleAccept(eq(clazz2), eq(marker));
        verify(constant2).bootstrapMethodHandleAccept(eq(clazz2), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant correctly delegates to the constant.
     * The method should not perform any additional logic beyond calling bootstrapMethodHandleAccept.
     */
    @Test
    public void testVisitInvokeDynamicConstant_delegatesToConstant() {
        // Act
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify only bootstrapMethodHandleAccept was called on the constant
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(any(), any());
        verifyNoMoreInteractions(invokeDynamicConstant);
    }

    /**
     * Tests that visitInvokeDynamicConstant works with ProgramClass.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withProgramClass_worksCorrectly() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        marker.visitInvokeDynamicConstant(programClass, invokeDynamicConstant);

        // Assert
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(programClass), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant works with LibraryClass.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withLibraryClass_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass = mock(LibraryClass.class);

        // Act
        marker.visitInvokeDynamicConstant(libraryClass, invokeDynamicConstant);

        // Assert
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(libraryClass), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant can be called sequentially with different constants.
     */
    @Test
    public void testVisitInvokeDynamicConstant_sequentialCallsWithDifferentConstants_allProcessed() {
        // Arrange & Act
        for (int i = 1; i <= 20; i++) {
            InvokeDynamicConstant constant = mock(InvokeDynamicConstant.class);
            marker.visitInvokeDynamicConstant(clazz, constant);

            // Assert - verify this constant's bootstrapMethodHandleAccept was called
            verify(constant).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
        }
    }

    /**
     * Tests that visitInvokeDynamicConstant doesn't modify the clazz parameter.
     */
    @Test
    public void testVisitInvokeDynamicConstant_doesNotModifyClazz() {
        // Act
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify clazz was only passed to the constant, not modified
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz), any());
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitInvokeDynamicConstant execution completes successfully.
     */
    @Test
    public void testVisitInvokeDynamicConstant_completesSuccessfully() {
        // Arrange
        boolean[] completed = {false};

        // Act
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        completed[0] = true;

        // Assert
        assertTrue(completed[0], "visitInvokeDynamicConstant should complete successfully");
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(any(), any());
    }

    /**
     * Tests that visitInvokeDynamicConstant with the same constant called multiple times
     * calls bootstrapMethodHandleAccept each time.
     */
    @Test
    public void testVisitInvokeDynamicConstant_sameConstantMultipleCalls_acceptsEachTime() {
        // Act
        for (int i = 0; i < 10; i++) {
            marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }

        // Assert
        verify(invokeDynamicConstant, times(10)).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant handles alternating clazz instances correctly.
     */
    @Test
    public void testVisitInvokeDynamicConstant_alternatingClazzInstances_allAccepted() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        marker.visitInvokeDynamicConstant(clazz1, invokeDynamicConstant);
        marker.visitInvokeDynamicConstant(clazz2, invokeDynamicConstant);
        marker.visitInvokeDynamicConstant(clazz1, invokeDynamicConstant);
        marker.visitInvokeDynamicConstant(clazz2, invokeDynamicConstant);

        // Assert
        verify(invokeDynamicConstant, times(2)).bootstrapMethodHandleAccept(eq(clazz1), eq(marker));
        verify(invokeDynamicConstant, times(2)).bootstrapMethodHandleAccept(eq(clazz2), eq(marker));
    }

    /**
     * Tests that visitInvokeDynamicConstant passes both parameters correctly in order.
     */
    @Test
    public void testVisitInvokeDynamicConstant_passesParametersInCorrectOrder() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class);
        AccessMethodMarker specificMarker = new AccessMethodMarker();

        // Act
        specificMarker.visitInvokeDynamicConstant(specificClazz, invokeDynamicConstant);

        // Assert - verify both parameters are passed in correct order (clazz, then marker)
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(same(specificClazz), same(specificMarker));
    }

    /**
     * Tests that visitInvokeDynamicConstant can handle many sequential invocations.
     */
    @Test
    public void testVisitInvokeDynamicConstant_manySequentialInvocations_allProcessed() {
        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }

        verify(invokeDynamicConstant, times(1000)).bootstrapMethodHandleAccept(eq(clazz), eq(marker));
    }

    /**
     * Tests that multiple markers calling visitInvokeDynamicConstant with the same constant
     * pass themselves as the visitor.
     */
    @Test
    public void testVisitInvokeDynamicConstant_multipleMarkersWithSameConstant_eachPassesSelf() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        AccessMethodMarker marker3 = new AccessMethodMarker();

        // Act
        marker1.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        marker2.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        marker3.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify each marker passed itself
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz), eq(marker1));
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz), eq(marker2));
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(eq(clazz), eq(marker3));
    }

    /**
     * Tests that visitInvokeDynamicConstant only interacts with the InvokeDynamicConstant parameter.
     */
    @Test
    public void testVisitInvokeDynamicConstant_onlyInteractsWithConstant() {
        // Act
        marker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify only the constant was interacted with (not the clazz)
        verify(invokeDynamicConstant).bootstrapMethodHandleAccept(any(), any());
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitInvokeDynamicConstant behavior is consistent across multiple marker instances.
     */
    @Test
    public void testVisitInvokeDynamicConstant_consistentBehaviorAcrossMarkerInstances() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        InvokeDynamicConstant constant1 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant2 = mock(InvokeDynamicConstant.class);

        // Act
        marker1.visitInvokeDynamicConstant(clazz, constant1);
        marker2.visitInvokeDynamicConstant(clazz, constant2);

        // Assert - both should have called bootstrapMethodHandleAccept once
        verify(constant1, times(1)).bootstrapMethodHandleAccept(eq(clazz), eq(marker1));
        verify(constant2, times(1)).bootstrapMethodHandleAccept(eq(clazz), eq(marker2));
    }
}
