package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AccessMethodMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
 *
 * The visitMethodHandleConstant method processes MethodHandle constants by calling
 * constantPoolEntryAccept on the clazz with the reference index from the MethodHandleConstant.
 * This allows the AccessMethodMarker to check the method reference and track what access
 * levels (private, protected, package) are accessed through method handles.
 */
public class AccessMethodMarkerClaude_visitMethodHandleConstantTest {

    private AccessMethodMarker marker;
    private Clazz clazz;
    private MethodHandleConstant methodHandleConstant;

    @BeforeEach
    public void setUp() {
        marker = new AccessMethodMarker();
        clazz = mock(ProgramClass.class);
        methodHandleConstant = mock(MethodHandleConstant.class);
    }

    /**
     * Tests that visitMethodHandleConstant calls constantPoolEntryAccept with the correct reference index.
     * This verifies the basic flow of the method.
     */
    @Test
    public void testVisitMethodHandleConstant_callsConstantPoolEntryAccept() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 5;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify constantPoolEntryAccept was called with index 5
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant works with different reference indices.
     */
    @Test
    public void testVisitMethodHandleConstant_withDifferentReferenceIndices_accessesCorrectEntries() {
        // Arrange
        MethodHandleConstant constant1 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant10 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant100 = mock(MethodHandleConstant.class);
        constant1.u2referenceIndex = 1;
        constant10.u2referenceIndex = 10;
        constant100.u2referenceIndex = 100;

        // Act
        marker.visitMethodHandleConstant(clazz, constant1);
        marker.visitMethodHandleConstant(clazz, constant10);
        marker.visitMethodHandleConstant(clazz, constant100);

        // Assert - verify each constant index was accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant passes the marker itself as the visitor.
     * This is important because the marker implements ConstantVisitor.
     */
    @Test
    public void testVisitMethodHandleConstant_passesMarkerAsVisitor() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify the marker itself is passed as the visitor
        verify(clazz).constantPoolEntryAccept(anyInt(), same(marker));
    }

    /**
     * Tests that visitMethodHandleConstant can be called multiple times.
     */
    @Test
    public void testVisitMethodHandleConstant_calledMultipleTimes_acceptsEachTime() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 5;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify constantPoolEntryAccept was called 3 times
        verify(clazz, times(3)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant works with different MethodHandleConstant instances.
     */
    @Test
    public void testVisitMethodHandleConstant_withDifferentMethodHandleConstants_accessesCorrectly() {
        // Arrange
        MethodHandleConstant constant1 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant2 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant3 = mock(MethodHandleConstant.class);
        constant1.u2referenceIndex = 1;
        constant2.u2referenceIndex = 2;
        constant3.u2referenceIndex = 3;

        // Act
        marker.visitMethodHandleConstant(clazz, constant1);
        marker.visitMethodHandleConstant(clazz, constant2);
        marker.visitMethodHandleConstant(clazz, constant3);

        // Assert - verify each constant's reference index was used
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant doesn't throw exceptions with valid parameters.
     */
    @Test
    public void testVisitMethodHandleConstant_withValidParameters_doesNotThrow() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 1;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitMethodHandleConstant(clazz, methodHandleConstant));
    }

    /**
     * Tests that multiple AccessMethodMarker instances work independently.
     */
    @Test
    public void testVisitMethodHandleConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        MethodHandleConstant constant1 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant2 = mock(MethodHandleConstant.class);
        constant1.u2referenceIndex = 1;
        constant2.u2referenceIndex =2;

        // Act
        marker1.visitMethodHandleConstant(clazz, constant1);
        marker2.visitMethodHandleConstant(clazz, constant2);

        // Assert - verify each marker accessed its respective constant
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests that visitMethodHandleConstant can handle being called in rapid succession.
     */
    @Test
    public void testVisitMethodHandleConstant_rapidSuccessiveCalls_worksCorrectly() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitMethodHandleConstant(clazz, methodHandleConstant);
        }

        // Assert
        verify(clazz, times(100)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant is stateless between calls.
     * Each call should independently access the constant pool.
     */
    @Test
    public void testVisitMethodHandleConstant_isStatelessBetweenCalls() {
        // Arrange
        MethodHandleConstant constant1 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant2 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant3 = mock(MethodHandleConstant.class);
        constant1.u2referenceIndex = 1;
        constant2.u2referenceIndex = 2;
        constant3.u2referenceIndex = 3;

        // Act - interleave calls
        marker.visitMethodHandleConstant(clazz, constant1);
        marker.visitMethodHandleConstant(clazz, constant2);
        marker.visitMethodHandleConstant(clazz, constant3);
        marker.visitMethodHandleConstant(clazz, constant1);

        // Assert - verify each call accessed the correct constant
        verify(clazz, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(3), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant works with reference index 0.
     */
    @Test
    public void testVisitMethodHandleConstant_withReferenceIndex0_accessesConstantPool() {
        // Arrange
        methodHandleConstant.u2referenceIndex =0;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant works with different clazz instances.
     */
    @Test
    public void testVisitMethodHandleConstant_withDifferentClazzInstances_accessesCorrectConstantPools() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        marker.visitMethodHandleConstant(clazz1, methodHandleConstant);
        marker.visitMethodHandleConstant(clazz2, methodHandleConstant);

        // Assert - verify each clazz's constant pool was accessed
        verify(clazz1).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant passes the correct clazz parameter.
     */
    @Test
    public void testVisitMethodHandleConstant_passesCorrectClazz() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class);
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        marker.visitMethodHandleConstant(specificClazz, methodHandleConstant);

        // Assert - verify the specific clazz was used
        verify(specificClazz).constantPoolEntryAccept(eq(1), any());
    }

    /**
     * Tests that visitMethodHandleConstant works with ProgramClass.
     */
    @Test
    public void testVisitMethodHandleConstant_withProgramClass_worksCorrectly() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        methodHandleConstant.u2referenceIndex = 5;

        // Act
        marker.visitMethodHandleConstant(programClass, methodHandleConstant);

        // Assert
        verify(programClass).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant works with LibraryClass.
     */
    @Test
    public void testVisitMethodHandleConstant_withLibraryClass_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass = mock(LibraryClass.class);
        methodHandleConstant.u2referenceIndex = 5;

        // Act
        marker.visitMethodHandleConstant(libraryClass, methodHandleConstant);

        // Assert
        verify(libraryClass).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant can be called sequentially with different constants.
     */
    @Test
    public void testVisitMethodHandleConstant_sequentialCallsWithDifferentConstants_allProcessed() {
        // Arrange & Act
        for (int i = 1; i <= 20; i++) {
            MethodHandleConstant constant = mock(MethodHandleConstant.class);
            constant.u2referenceIndex = i;
            marker.visitMethodHandleConstant(clazz, constant);

            // Assert - verify this constant's reference index was accessed
            verify(clazz).constantPoolEntryAccept(eq(i), eq(marker));
        }
    }

    /**
     * Tests that visitMethodHandleConstant doesn't modify the clazz parameter.
     * It should only call constantPoolEntryAccept on it.
     */
    @Test
    public void testVisitMethodHandleConstant_doesNotModifyClazz() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify only constantPoolEntryAccept was called on clazz
        verify(clazz).constantPoolEntryAccept(anyInt(), any());
        verifyNoMoreInteractions(clazz);
    }

    /**
     * Tests that visitMethodHandleConstant with the same constant called multiple times
     * accesses the constant pool each time.
     */
    @Test
    public void testVisitMethodHandleConstant_sameConstantMultipleCalls_accessesEachTime() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 5;

        // Act
        for (int i = 0; i < 10; i++) {
            marker.visitMethodHandleConstant(clazz, methodHandleConstant);
        }

        // Assert
        verify(clazz, times(10)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant handles alternating clazz instances correctly.
     */
    @Test
    public void testVisitMethodHandleConstant_alternatingClazzInstances_allAccepted() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        marker.visitMethodHandleConstant(clazz1, methodHandleConstant);
        marker.visitMethodHandleConstant(clazz2, methodHandleConstant);
        marker.visitMethodHandleConstant(clazz1, methodHandleConstant);
        marker.visitMethodHandleConstant(clazz2, methodHandleConstant);

        // Assert
        verify(clazz1, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant passes both parameters correctly.
     */
    @Test
    public void testVisitMethodHandleConstant_passesParametersCorrectly() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class);
        AccessMethodMarker specificMarker = new AccessMethodMarker();
        methodHandleConstant.u2referenceIndex = 7;

        // Act
        specificMarker.visitMethodHandleConstant(specificClazz, methodHandleConstant);

        // Assert - verify clazz and marker are passed correctly
        verify(specificClazz).constantPoolEntryAccept(eq(7), same(specificMarker));
    }

    /**
     * Tests that visitMethodHandleConstant can handle many sequential invocations.
     */
    @Test
    public void testVisitMethodHandleConstant_manySequentialInvocations_allProcessed() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 1;

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            marker.visitMethodHandleConstant(clazz, methodHandleConstant);
        }

        verify(clazz, times(1000)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that multiple markers calling visitMethodHandleConstant with the same constant
     * pass themselves as the visitor.
     */
    @Test
    public void testVisitMethodHandleConstant_multipleMarkersWithSameConstant_eachPassesSelf() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        AccessMethodMarker marker3 = new AccessMethodMarker();
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        marker1.visitMethodHandleConstant(clazz, methodHandleConstant);
        marker2.visitMethodHandleConstant(clazz, methodHandleConstant);
        marker3.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify each marker passed itself
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker2));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker3));
    }

    /**
     * Tests that visitMethodHandleConstant only interacts with the clazz parameter's constantPoolEntryAccept.
     */
    @Test
    public void testVisitMethodHandleConstant_onlyCallsConstantPoolEntryAccept() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 1;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify only constantPoolEntryAccept was called
        verify(clazz).constantPoolEntryAccept(anyInt(), any());
        verifyNoMoreInteractions(clazz);
    }

    /**
     * Tests that visitMethodHandleConstant behavior is consistent across multiple marker instances.
     */
    @Test
    public void testVisitMethodHandleConstant_consistentBehaviorAcrossMarkerInstances() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        MethodHandleConstant constant1 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant2 = mock(MethodHandleConstant.class);
        constant1.u2referenceIndex = 1;
        constant2.u2referenceIndex =2;

        // Act
        marker1.visitMethodHandleConstant(clazz, constant1);
        marker2.visitMethodHandleConstant(clazz, constant2);

        // Assert - both should have called constantPoolEntryAccept once with their respective indices
        verify(clazz, times(1)).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests that visitMethodHandleConstant works with large reference index values.
     */
    @Test
    public void testVisitMethodHandleConstant_withLargeReferenceIndex_accessesConstantPool() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 65535;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant with boundary reference index values works correctly.
     */
    @Test
    public void testVisitMethodHandleConstant_withBoundaryReferenceIndices_accessesConstantPool() {
        // Arrange
        MethodHandleConstant constant0 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant1 = mock(MethodHandleConstant.class);
        MethodHandleConstant constantMax = mock(MethodHandleConstant.class);
        constant0.u2referenceIndex = 0;
        constant1.u2referenceIndex = 1;
        constantMax.u2referenceIndex = 65535;

        // Act
        marker.visitMethodHandleConstant(clazz, constant0);
        marker.visitMethodHandleConstant(clazz, constant1);
        marker.visitMethodHandleConstant(clazz, constantMax);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant works correctly with various combinations of parameters.
     */
    @Test
    public void testVisitMethodHandleConstant_variousCombinations_allWorkCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        MethodHandleConstant constant1 = mock(MethodHandleConstant.class);
        MethodHandleConstant constant2 = mock(MethodHandleConstant.class);
        constant1.u2referenceIndex = 1;
        constant2.u2referenceIndex =2;

        // Act
        marker.visitMethodHandleConstant(clazz1, constant1);
        marker.visitMethodHandleConstant(clazz1, constant2);
        marker.visitMethodHandleConstant(clazz2, constant1);
        marker.visitMethodHandleConstant(clazz2, constant2);

        // Assert - verify all combinations were processed
        verify(clazz1).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz1).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(2), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant correctly reads the u2referenceIndex field.
     */
    @Test
    public void testVisitMethodHandleConstant_readsU2ReferenceIndexField() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 42;

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify the u2referenceIndex field value was used
        verify(clazz).constantPoolEntryAccept(eq(42), eq(marker));
    }

    /**
     * Tests that visitMethodHandleConstant execution completes successfully.
     */
    @Test
    public void testVisitMethodHandleConstant_completesSuccessfully() {
        // Arrange
        methodHandleConstant.u2referenceIndex = 1;
        boolean[] completed = {false};

        // Act
        marker.visitMethodHandleConstant(clazz, methodHandleConstant);
        completed[0] = true;

        // Assert
        assertTrue(completed[0], "visitMethodHandleConstant should complete successfully");
        verify(clazz).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitMethodHandleConstant marker implements ConstantVisitor.
     */
    @Test
    public void testVisitMethodHandleConstant_markerImplementsConstantVisitor() {
        // Assert - verify marker is a ConstantVisitor
        assertTrue(marker instanceof ConstantVisitor,
                "AccessMethodMarker should implement ConstantVisitor");
    }

    /**
     * Tests that visitMethodHandleConstant handles sequential calls with different reference indices.
     */
    @Test
    public void testVisitMethodHandleConstant_sequentialCallsWithDifferentIndices_allProcessed() {
        // Arrange & Act
        for (int i = 1; i <= 20; i++) {
            MethodHandleConstant constant = mock(MethodHandleConstant.class);
            constant.u2referenceIndex = i;
            marker.visitMethodHandleConstant(clazz, constant);
        }

        // Assert - verify all 20 different reference indices were accessed
        for (int i = 1; i <= 20; i++) {
            verify(clazz).constantPoolEntryAccept(eq(i), eq(marker));
        }
    }
}
