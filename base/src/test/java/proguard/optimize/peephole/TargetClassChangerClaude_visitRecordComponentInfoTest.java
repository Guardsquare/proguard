package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.RecordComponentInfo;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
 *
 * The visitRecordComponentInfo method delegates to the attributesAccept method
 * of the RecordComponentInfo, which processes the attributes of the record component
 * by calling back to the TargetClassChanger's attribute visitor methods.
 * This is part of the visitor pattern for processing record components during class retargeting.
 *
 * Note: The method intentionally does NOT change the referenced field as mentioned in the
 * comment (lines 413-414) - "it's still the original one in this class."
 */
public class TargetClassChangerClaude_visitRecordComponentInfoTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private RecordComponentInfo recordComponentInfo;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        recordComponentInfo = mock(RecordComponentInfo.class);
    }

    /**
     * Tests that visitRecordComponentInfo correctly delegates to attributesAccept.
     * This verifies the core functionality of the method - delegation to process attributes.
     */
    @Test
    public void testVisitRecordComponentInfo_delegatesToAttributesAccept() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify that attributesAccept was called with correct parameters
        verify(recordComponentInfo).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitRecordComponentInfo_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            changer.visitRecordComponentInfo(clazz, recordComponentInfo)
        );
    }

    /**
     * Tests that visitRecordComponentInfo can be called multiple times.
     * Each call should independently delegate to attributesAccept.
     */
    @Test
    public void testVisitRecordComponentInfo_calledMultipleTimes_delegatesEachTime() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify attributesAccept was called exactly 3 times
        verify(recordComponentInfo, times(3)).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo works with different RecordComponentInfo instances.
     * Each instance should have its attributesAccept method called.
     */
    @Test
    public void testVisitRecordComponentInfo_withDifferentRecordComponents_delegatesToEach() {
        // Arrange
        RecordComponentInfo component1 = mock(RecordComponentInfo.class);
        RecordComponentInfo component2 = mock(RecordComponentInfo.class);
        RecordComponentInfo component3 = mock(RecordComponentInfo.class);

        // Act
        changer.visitRecordComponentInfo(clazz, component1);
        changer.visitRecordComponentInfo(clazz, component2);
        changer.visitRecordComponentInfo(clazz, component3);

        // Assert - verify each component's attributesAccept was called once
        verify(component1).attributesAccept(clazz, changer);
        verify(component2).attributesAccept(clazz, changer);
        verify(component3).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo passes the changer itself as the visitor.
     * This is crucial because the changer implements AttributeVisitor.
     */
    @Test
    public void testVisitRecordComponentInfo_passesChangerAsVisitor() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify that the changer itself is passed as the visitor parameter
        verify(recordComponentInfo).attributesAccept(
            eq(clazz),
            same(changer)  // The changer itself should be passed as visitor
        );
    }

    /**
     * Tests that visitRecordComponentInfo works with different clazz instances.
     * Each clazz should be correctly passed through to attributesAccept.
     */
    @Test
    public void testVisitRecordComponentInfo_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        changer.visitRecordComponentInfo(clazz1, recordComponentInfo);
        changer.visitRecordComponentInfo(clazz2, recordComponentInfo);

        // Assert - verify the correct clazz was passed in each call
        verify(recordComponentInfo).attributesAccept(clazz1, changer);
        verify(recordComponentInfo).attributesAccept(clazz2, changer);
    }

    /**
     * Tests that visitRecordComponentInfo doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz.
     */
    @Test
    public void testVisitRecordComponentInfo_doesNotDirectlyInteractWithParameters() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify no direct interactions with clazz (it's only passed through)
        verifyNoInteractions(clazz);
        // recordComponentInfo should have been called via delegation
        verify(recordComponentInfo, times(1)).attributesAccept(any(), any(AttributeVisitor.class));
    }

    /**
     * Tests that visitRecordComponentInfo maintains correct order when called with multiple components.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitRecordComponentInfo_sequentialCalls_maintainIndependence() {
        // Arrange
        RecordComponentInfo component1 = mock(RecordComponentInfo.class);
        RecordComponentInfo component2 = mock(RecordComponentInfo.class);

        // Act - call with first component
        changer.visitRecordComponentInfo(clazz, component1);
        verify(component1).attributesAccept(clazz, changer);

        // Act - call with second component
        changer.visitRecordComponentInfo(clazz, component2);
        verify(component2).attributesAccept(clazz, changer);

        // Assert - first component should not have been called again
        verify(component1, times(1)).attributesAccept(any(), any(AttributeVisitor.class));
    }

    /**
     * Tests that visitRecordComponentInfo integrates correctly with the visitor pattern.
     * The changer implements AttributeVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitRecordComponentInfo_changerIsValidVisitor() {
        // Arrange & Assert - verify the changer is an instance of AttributeVisitor
        assertTrue(changer instanceof AttributeVisitor,
            "Changer should implement AttributeVisitor to be used as a visitor");

        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify it's passed as an AttributeVisitor
        verify(recordComponentInfo).attributesAccept(
            any(Clazz.class),
            any(AttributeVisitor.class)
        );
    }

    /**
     * Tests that visitRecordComponentInfo handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitRecordComponentInfo_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify delegation happened twice with identical parameters
        verify(recordComponentInfo, times(2)).attributesAccept(same(clazz), same(changer));
    }

    /**
     * Tests that visitRecordComponentInfo properly integrates with both parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitRecordComponentInfo_integratesBothParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        RecordComponentInfo specificComponent = mock(RecordComponentInfo.class, "specificComponent");

        // Act
        changer.visitRecordComponentInfo(specificClazz, specificComponent);

        // Assert - verify both specific parameters were passed correctly
        verify(specificComponent).attributesAccept(specificClazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo works correctly across different changer instances.
     * Different changers should independently delegate to their record components.
     */
    @Test
    public void testVisitRecordComponentInfo_withDifferentChangers_delegatesIndependently() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();

        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        changer2.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify each changer was passed independently
        verify(recordComponentInfo, times(1)).attributesAccept(clazz, changer);
        verify(recordComponentInfo, times(1)).attributesAccept(clazz, changer2);
    }

    /**
     * Tests that visitRecordComponentInfo handles the visitor pattern delegation correctly.
     * The method should pass the changer (which is an AttributeVisitor) to process attributes.
     */
    @Test
    public void testVisitRecordComponentInfo_delegationFollowsVisitorPattern() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify the method delegates by calling attributesAccept exactly once
        verify(recordComponentInfo, times(1)).attributesAccept(any(Clazz.class), any(AttributeVisitor.class));
    }

    /**
     * Tests visitRecordComponentInfo can handle multiple different combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitRecordComponentInfo_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        RecordComponentInfo component1 = mock(RecordComponentInfo.class, "component1");
        RecordComponentInfo component2 = mock(RecordComponentInfo.class, "component2");

        // Act
        changer.visitRecordComponentInfo(clazz1, component1);
        changer.visitRecordComponentInfo(clazz1, component2);
        changer.visitRecordComponentInfo(clazz2, component1);
        changer.visitRecordComponentInfo(clazz2, component2);

        // Assert - verify all combinations were processed
        verify(component1).attributesAccept(clazz1, changer);
        verify(component2).attributesAccept(clazz1, changer);
        verify(component1).attributesAccept(clazz2, changer);
        verify(component2).attributesAccept(clazz2, changer);
    }

    /**
     * Tests that visitRecordComponentInfo properly delegates without modifying the component.
     * The method should only delegate, not modify the component directly.
     */
    @Test
    public void testVisitRecordComponentInfo_doesNotModifyComponentDirectly() {
        // Arrange
        RecordComponentInfo spyComponent = mock(RecordComponentInfo.class);

        // Act
        changer.visitRecordComponentInfo(clazz, spyComponent);

        // Assert - verify only attributesAccept was called, nothing else
        verify(spyComponent, times(1)).attributesAccept(any(), any());
        verifyNoMoreInteractions(spyComponent);
    }

    /**
     * Tests visitRecordComponentInfo execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitRecordComponentInfo_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitRecordComponentInfo should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitRecordComponentInfo is used for record component processing.
     * Record components are a feature introduced in Java 14 for record classes.
     */
    @Test
    public void testVisitRecordComponentInfo_handlesRecordComponents() {
        // Arrange
        RecordComponentInfo recordComponent = mock(RecordComponentInfo.class, "recordComponent");

        // Act
        changer.visitRecordComponentInfo(clazz, recordComponent);

        // Assert - verify delegation occurred for record component
        verify(recordComponent).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo works with LibraryClass as well as ProgramClass.
     * The method should handle both types of Clazz implementations.
     */
    @Test
    public void testVisitRecordComponentInfo_withLibraryClass_delegatesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitRecordComponentInfo(libraryClass, recordComponentInfo);

        // Assert - verify delegation occurred with library class
        verify(recordComponentInfo).attributesAccept(libraryClass, changer);
    }

    /**
     * Tests that visitRecordComponentInfo works with a mix of ProgramClass and LibraryClass.
     * This ensures the method is polymorphic over Clazz implementations.
     */
    @Test
    public void testVisitRecordComponentInfo_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitRecordComponentInfo(programClass, recordComponentInfo);
        changer.visitRecordComponentInfo(libraryClass, recordComponentInfo);

        // Assert - verify both types were processed
        verify(recordComponentInfo).attributesAccept(programClass, changer);
        verify(recordComponentInfo).attributesAccept(libraryClass, changer);
    }

    /**
     * Tests that visitRecordComponentInfo can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitRecordComponentInfo_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitRecordComponentInfo(clazz, recordComponentInfo);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify delegation occurred for all calls
        verify(recordComponentInfo, times(1000)).attributesAccept(clazz, changer);
    }

    /**
     * Tests that multiple changers can call visitRecordComponentInfo independently.
     * Each changer should operate independently without interfering with others.
     */
    @Test
    public void testVisitRecordComponentInfo_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        RecordComponentInfo component1 = mock(RecordComponentInfo.class);
        RecordComponentInfo component2 = mock(RecordComponentInfo.class);

        // Act - different changers process different components
        changer1.visitRecordComponentInfo(clazz1, component1);
        changer2.visitRecordComponentInfo(clazz2, component2);
        changer3.visitRecordComponentInfo(clazz1, component2);

        // Assert - verify correct delegations occurred
        verify(component1).attributesAccept(clazz1, changer1);
        verify(component2).attributesAccept(clazz2, changer2);
        verify(component2).attributesAccept(clazz1, changer3);
    }

    /**
     * Tests that visitRecordComponentInfo works correctly when the TargetClassChanger
     * is used polymorphically as a RecordComponentInfoVisitor.
     */
    @Test
    public void testVisitRecordComponentInfo_asRecordComponentInfoVisitorInterface_doesNotThrowException() {
        // Arrange - treat changer as RecordComponentInfoVisitor interface
        proguard.classfile.attribute.visitor.RecordComponentInfoVisitor visitor = changer;

        // Act & Assert - should work the same way through the interface
        assertDoesNotThrow(() -> visitor.visitRecordComponentInfo(clazz, recordComponentInfo));
        verify(recordComponentInfo).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo called on newly created changer instance behaves consistently.
     * Verifies the method works immediately after construction without initialization.
     */
    @Test
    public void testVisitRecordComponentInfo_onNewInstance_behavesConsistently() {
        // Arrange - create a fresh changer
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act - immediately call visitRecordComponentInfo
        freshChanger.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - should delegate properly
        verify(recordComponentInfo).attributesAccept(clazz, freshChanger);
    }

    /**
     * Tests that visitRecordComponentInfo handles edge case of calling with various
     * combinations of different clazz and component pairs in sequence.
     */
    @Test
    public void testVisitRecordComponentInfo_sequentialCallsWithDifferentParameters_noIssues() {
        // Arrange
        Clazz[] clazzes = {
            mock(ProgramClass.class),
            mock(LibraryClass.class),
            mock(ProgramClass.class)
        };
        RecordComponentInfo[] components = {
            mock(RecordComponentInfo.class),
            mock(RecordComponentInfo.class),
            mock(RecordComponentInfo.class)
        };

        // Act - call with different combinations
        for (Clazz c : clazzes) {
            for (RecordComponentInfo component : components) {
                changer.visitRecordComponentInfo(c, component);
            }
        }

        // Assert - verify delegations for all combinations
        for (Clazz c : clazzes) {
            for (RecordComponentInfo component : components) {
                verify(component).attributesAccept(c, changer);
            }
        }
    }

    /**
     * Tests that the visitRecordComponentInfo method signature matches the RecordComponentInfoVisitor interface.
     * This ensures the method properly implements the interface method.
     */
    @Test
    public void testVisitRecordComponentInfo_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be a RecordComponentInfoVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.visitor.RecordComponentInfoVisitor,
                "TargetClassChanger should implement RecordComponentInfoVisitor");
    }

    /**
     * Tests that visitRecordComponentInfo doesn't affect the changer's internal state.
     * Since it's a pure delegation method, it should not modify any internal fields.
     */
    @Test
    public void testVisitRecordComponentInfo_doesNotAffectInternalState() {
        // Act - call visitRecordComponentInfo multiple times
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify delegations occurred without side effects to clazz
        verifyNoInteractions(clazz);
        verify(recordComponentInfo, times(2)).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo with varying combinations of parameters
     * all result in proper delegation.
     */
    @Test
    public void testVisitRecordComponentInfo_varyingParameterCombinations_consistentDelegation() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        RecordComponentInfo component2 = mock(RecordComponentInfo.class);

        // Act - call with various parameter combinations
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        changer.visitRecordComponentInfo(clazz2, component2);
        changer.visitRecordComponentInfo(clazz, component2);

        // Assert - verify proper delegation for all combinations
        verify(recordComponentInfo).attributesAccept(clazz, changer);
        verify(component2).attributesAccept(clazz2, changer);
        verify(component2).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitRecordComponentInfo_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitRecordComponentInfo should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitRecordComponentInfo handles the same component instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitRecordComponentInfo_sameComponentInstanceMultipleTimes_noAccumulation() {
        // Arrange
        RecordComponentInfo singleComponent = mock(RecordComponentInfo.class);

        // Act - call multiple times with the same component
        for (int i = 0; i < 10; i++) {
            changer.visitRecordComponentInfo(clazz, singleComponent);
        }

        // Assert - verify delegation occurred 10 times
        verify(singleComponent, times(10)).attributesAccept(clazz, changer);
    }

    /**
     * Tests that visitRecordComponentInfo returns immediately without performing operations beyond delegation.
     * This confirms the pure delegation nature of the method.
     */
    @Test
    public void testVisitRecordComponentInfo_returnsImmediatelyAfterDelegation() {
        // Arrange
        RecordComponentInfo trackedComponent = mock(RecordComponentInfo.class);

        // Act
        changer.visitRecordComponentInfo(clazz, trackedComponent);

        // Assert - only delegation call should have been made
        verify(trackedComponent, times(1)).attributesAccept(any(), any());
        verifyNoMoreInteractions(trackedComponent);
    }

    /**
     * Tests that visitRecordComponentInfo called after multiple other operations still delegates correctly.
     * This verifies consistent behavior regardless of the changer's usage history.
     */
    @Test
    public void testVisitRecordComponentInfo_afterMultipleOperations_stillDelegates() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitRecordComponentInfo(clazz, recordComponentInfo);
        }

        // Act - call visitRecordComponentInfo again
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify delegation occurred 6 times total
        verify(recordComponentInfo, times(6)).attributesAccept(clazz, changer);
    }

    /**
     * Tests that the visitor passed to attributesAccept is indeed the changer itself.
     * This verifies that the changer can receive callbacks from the component.
     */
    @Test
    public void testVisitRecordComponentInfo_changerAsVisitorReceivesCallbacks() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - verify the exact same changer instance is passed
        verify(recordComponentInfo).attributesAccept(
            argThat(c -> c == clazz),
            argThat(v -> v == changer)
        );
    }

    /**
     * Tests that visitRecordComponentInfo does not modify the referenced field.
     * As mentioned in the implementation comment, the referenced field remains the original one.
     */
    @Test
    public void testVisitRecordComponentInfo_doesNotModifyReferencedField() {
        // Act
        changer.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - only attributesAccept should be called, the component itself is not modified
        verify(recordComponentInfo, times(1)).attributesAccept(any(), any());
        verifyNoMoreInteractions(recordComponentInfo);
    }
}
