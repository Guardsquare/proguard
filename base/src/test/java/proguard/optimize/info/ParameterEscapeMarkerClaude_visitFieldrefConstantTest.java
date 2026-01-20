package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ParameterEscapeMarker#visitFieldrefConstant(Clazz, FieldrefConstant)}.
 *
 * The visitFieldrefConstant method processes field reference constants by delegating to the
 * ClassConstant referenced by the FieldrefConstant. This allows the marker to check if the
 * class containing the field may have static initializers with side effects, which could
 * affect parameter escape analysis.
 *
 * The method calls: clazz.constantPoolEntryAccept(fieldrefConstant.u2classIndex, this)
 * This delegates to visiting the class constant at the specified index, triggering
 * visitClassConstant which checks for side effects via SideEffectClassChecker.
 */
public class ParameterEscapeMarkerClaude_visitFieldrefConstantTest {

    private ParameterEscapeMarker marker;
    private Clazz clazz;
    private FieldrefConstant fieldrefConstant;

    @BeforeEach
    public void setUp() {
        marker = new ParameterEscapeMarker();
        clazz = mock(ProgramClass.class);
        fieldrefConstant = new FieldrefConstant();
    }

    /**
     * Tests that visitFieldrefConstant delegates to the constant pool entry accept method.
     * The method should call constantPoolEntryAccept with the class index from the fieldref.
     */
    @Test
    public void testVisitFieldrefConstant_delegatesToConstantPoolEntry() {
        // Arrange
        int classIndex = 42;
        fieldrefConstant.u2classIndex = classIndex;

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify that constantPoolEntryAccept was called with the correct index
        verify(clazz, times(1)).constantPoolEntryAccept(eq(classIndex), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitFieldrefConstant passes itself as the ConstantVisitor.
     * This ensures the marker continues to be used for visiting the referenced class constant.
     */
    @Test
    public void testVisitFieldrefConstant_passesSelfAsVisitor() {
        // Arrange
        int classIndex = 10;
        fieldrefConstant.u2classIndex = classIndex;

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify that the marker itself is passed as the visitor
        verify(clazz, times(1)).constantPoolEntryAccept(eq(classIndex), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant can be called with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitFieldrefConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        fieldrefConstant.u2classIndex = 5;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, fieldrefConstant));
    }

    /**
     * Tests that visitFieldrefConstant works with different class indices.
     * The method should handle various valid constant pool indices.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClassIndices() {
        // Arrange & Act & Assert
        for (int i = 0; i < 100; i++) {
            fieldrefConstant.u2classIndex = i;
            assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, fieldrefConstant));
            verify(clazz, times(1)).constantPoolEntryAccept(eq(i), eq(marker));
            reset(clazz);
        }
    }

    /**
     * Tests that visitFieldrefConstant with class index 0 works correctly.
     * Index 0 is a special case in the constant pool (reserved).
     */
    @Test
    public void testVisitFieldrefConstant_withZeroClassIndex() {
        // Arrange
        fieldrefConstant.u2classIndex = 0;

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - should still attempt to visit the constant pool entry
        verify(clazz, times(1)).constantPoolEntryAccept(eq(0), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant with maximum class index works correctly.
     * Tests the upper boundary of possible constant pool indices (65535 max for u2).
     */
    @Test
    public void testVisitFieldrefConstant_withMaxClassIndex() {
        // Arrange
        fieldrefConstant.u2classIndex = 65535;

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - should attempt to visit the constant pool entry at max index
        verify(clazz, times(1)).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant can be called multiple times in succession.
     * The method should handle repeated calls without issues.
     */
    @Test
    public void testVisitFieldrefConstant_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        fieldrefConstant.u2classIndex = 15;

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
        });

        // Verify called three times
        verify(clazz, times(3)).constantPoolEntryAccept(eq(15), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant properly delegates for different fieldref constants.
     * Each fieldref may reference a different class in the constant pool.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentFieldrefConstants() {
        // Arrange
        FieldrefConstant fieldref1 = new FieldrefConstant();
        FieldrefConstant fieldref2 = new FieldrefConstant();
        FieldrefConstant fieldref3 = new FieldrefConstant();

        fieldref1.u2classIndex = 10;
        fieldref2.u2classIndex = 20;
        fieldref3.u2classIndex = 30;

        // Act
        marker.visitFieldrefConstant(clazz, fieldref1);
        marker.visitFieldrefConstant(clazz, fieldref2);
        marker.visitFieldrefConstant(clazz, fieldref3);

        // Assert - each should delegate to its respective class index
        verify(clazz, times(1)).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(20), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(30), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant works with different Clazz instances.
     * The method should handle different class contexts.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        fieldrefConstant.u2classIndex = 5;

        // Act
        marker.visitFieldrefConstant(clazz1, fieldrefConstant);
        marker.visitFieldrefConstant(clazz2, fieldrefConstant);

        // Assert - each clazz should have its constant pool entry accessed
        verify(clazz1, times(1)).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz2, times(1)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant maintains correct behavior across sequential calls.
     * The delegation should happen for each invocation independently.
     */
    @Test
    public void testVisitFieldrefConstant_sequentialCalls_delegatesIndependently() {
        // Arrange
        fieldrefConstant.u2classIndex = 7;

        // Act - call multiple times
        marker.visitFieldrefConstant(clazz, fieldrefConstant);
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify delegation happened twice
        verify(clazz, times(2)).constantPoolEntryAccept(eq(7), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant properly implements the ConstantVisitor interface.
     * This ensures the method signature matches the interface.
     */
    @Test
    public void testVisitFieldrefConstant_implementsConstantVisitorInterface() {
        // Assert - ParameterEscapeMarker should be a ConstantVisitor
        assertTrue(marker instanceof ConstantVisitor,
                "ParameterEscapeMarker should implement ConstantVisitor");
    }

    /**
     * Tests that visitFieldrefConstant uses the u2classIndex field from FieldrefConstant.
     * This is the critical field that points to the class constant in the pool.
     */
    @Test
    public void testVisitFieldrefConstant_usesU2classIndexField() {
        // Arrange
        int expectedIndex = 123;
        fieldrefConstant.u2classIndex = expectedIndex;

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the exact index value was used
        verify(clazz, times(1)).constantPoolEntryAccept(eq(expectedIndex), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitFieldrefConstant works correctly when called on multiple markers.
     * Each marker should delegate independently.
     */
    @Test
    public void testVisitFieldrefConstant_withMultipleMarkers_delegatesIndependently() {
        // Arrange
        ParameterEscapeMarker marker1 = new ParameterEscapeMarker();
        ParameterEscapeMarker marker2 = new ParameterEscapeMarker();
        fieldrefConstant.u2classIndex = 8;

        // Act
        marker1.visitFieldrefConstant(clazz, fieldrefConstant);
        marker2.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify both markers triggered delegation
        verify(clazz, times(2)).constantPoolEntryAccept(eq(8), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitFieldrefConstant properly chains the visitor pattern.
     * The delegation continues the visitor traversal through the constant pool.
     */
    @Test
    public void testVisitFieldrefConstant_chainsVisitorPattern() {
        // Arrange
        int classIndex = 25;
        fieldrefConstant.u2classIndex = classIndex;

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the visitor pattern chain continues with the same marker
        verify(clazz, times(1)).constantPoolEntryAccept(classIndex, marker);
    }

    /**
     * Tests that visitFieldrefConstant processes typical constant pool indices.
     * Tests common index values that would appear in real bytecode.
     */
    @Test
    public void testVisitFieldrefConstant_withTypicalIndices() {
        // Arrange - typical constant pool indices in real classes
        int[] typicalIndices = {1, 2, 3, 5, 7, 10, 15, 20, 50, 100};

        // Act & Assert - all typical indices should work
        for (int index : typicalIndices) {
            fieldrefConstant.u2classIndex = index;
            assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, fieldrefConstant));
            verify(clazz, times(1)).constantPoolEntryAccept(eq(index), eq(marker));
            reset(clazz);
        }
    }

    /**
     * Tests that visitFieldrefConstant executes quickly.
     * The delegation should have minimal overhead.
     */
    @Test
    public void testVisitFieldrefConstant_executesQuickly() {
        // Arrange
        fieldrefConstant.u2classIndex = 10;
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete reasonably quickly
        assertTrue(durationMs < 1000, "visitFieldrefConstant should execute quickly");
    }

    /**
     * Tests that visitFieldrefConstant works with a newly created marker.
     * The method should work correctly regardless of marker initialization state.
     */
    @Test
    public void testVisitFieldrefConstant_withNewlyCreatedMarker() {
        // Arrange
        ParameterEscapeMarker newMarker = new ParameterEscapeMarker();
        fieldrefConstant.u2classIndex = 12;

        // Act & Assert
        assertDoesNotThrow(() -> newMarker.visitFieldrefConstant(clazz, fieldrefConstant));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(12), eq(newMarker));
    }

    /**
     * Tests that visitFieldrefConstant can handle edge case of class index 1.
     * Index 1 is typically the first usable constant pool entry.
     */
    @Test
    public void testVisitFieldrefConstant_withClassIndexOne() {
        // Arrange
        fieldrefConstant.u2classIndex = 1;

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(clazz, times(1)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant maintains consistent behavior across invocations.
     * Every call should behave identically with the same parameters.
     */
    @Test
    public void testVisitFieldrefConstant_consistentBehavior() {
        // Arrange
        fieldrefConstant.u2classIndex = 33;

        // Act - call multiple times and verify consistent behavior
        for (int i = 0; i < 10; i++) {
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
        }

        // Assert - verify delegation happened consistently 10 times
        verify(clazz, times(10)).constantPoolEntryAccept(eq(33), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant correctly delegates regardless of the marker's
     * previous usage history.
     */
    @Test
    public void testVisitFieldrefConstant_afterPreviousVisits_stillDelegatesCorrectly() {
        // Arrange
        fieldrefConstant.u2classIndex = 44;

        // Simulate previous visits
        marker.visitFieldrefConstant(clazz, fieldrefConstant);
        reset(clazz);

        // Act - visit again
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - should still delegate correctly
        verify(clazz, times(1)).constantPoolEntryAccept(eq(44), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant works in a scenario with rapid index changes.
     * Simulates processing multiple different fieldrefs in quick succession.
     */
    @Test
    public void testVisitFieldrefConstant_withRapidIndexChanges() {
        // Act & Assert - simulate rapid changes to different fieldrefs
        for (int i = 0; i < 50; i++) {
            fieldrefConstant.u2classIndex = i;
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
            verify(clazz, times(1)).constantPoolEntryAccept(eq(i), eq(marker));
            reset(clazz);
        }
    }

    /**
     * Tests that visitFieldrefConstant delegates to the correct constant pool entry
     * when processing fieldrefs that might reference the same class.
     */
    @Test
    public void testVisitFieldrefConstant_multipleFieldrefsSameClass() {
        // Arrange - multiple fieldrefs referencing the same class
        FieldrefConstant fieldref1 = new FieldrefConstant();
        FieldrefConstant fieldref2 = new FieldrefConstant();
        int sharedClassIndex = 17;
        fieldref1.u2classIndex = sharedClassIndex;
        fieldref2.u2classIndex = sharedClassIndex;

        // Act
        marker.visitFieldrefConstant(clazz, fieldref1);
        marker.visitFieldrefConstant(clazz, fieldref2);

        // Assert - both should delegate to the same class index
        verify(clazz, times(2)).constantPoolEntryAccept(eq(sharedClassIndex), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant maintains the visitor pattern correctly
     * when used as part of a larger constant pool traversal.
     */
    @Test
    public void testVisitFieldrefConstant_asPartOfConstantPoolTraversal() {
        // Arrange
        fieldrefConstant.u2classIndex = 99;

        // Act - simulate a constant pool traversal scenario
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the delegation maintains the traversal pattern
        verify(clazz, times(1)).constantPoolEntryAccept(eq(99), eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant works correctly when the u2classIndex
     * is at various boundary values within the valid u2 range.
     */
    @Test
    public void testVisitFieldrefConstant_withBoundaryValues() {
        // Arrange & Act & Assert
        int[] boundaryValues = {0, 1, 255, 256, 32767, 32768, 65534, 65535};

        for (int value : boundaryValues) {
            fieldrefConstant.u2classIndex = value;
            assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, fieldrefConstant),
                    "Should handle boundary value: " + value);
            verify(clazz, times(1)).constantPoolEntryAccept(eq(value), eq(marker));
            reset(clazz);
        }
    }
}
