package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AccessMethodMarker#visitClassConstant(Clazz, ClassConstant)}.
 *
 * The visitClassConstant method checks the referenced class in a class constant.
 * It delegates to the class constant to visit the referenced class, allowing the
 * AccessMethodMarker to track what access levels (private, protected, package) are
 * accessed through class constants. This is important for marking methods that
 * access non-public classes through class constant references (e.g., for instanceof,
 * checkcast, new instructions).
 */
public class AccessMethodMarkerClaude_visitClassConstantTest {

    private AccessMethodMarker marker;
    private Clazz clazz;
    private ClassConstant classConstant;

    @BeforeEach
    public void setUp() {
        marker = new AccessMethodMarker();
        clazz = mock(ProgramClass.class);
        classConstant = mock(ClassConstant.class);
    }

    /**
     * Tests that visitClassConstant calls referencedClassAccept on the ClassConstant.
     * This is the core behavior - the method should delegate to the class constant
     * to visit the referenced class.
     */
    @Test
    public void testVisitClassConstant_callsReferencedClassAccept() {
        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert - verify that referencedClassAccept was called with the marker
        verify(classConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant passes the marker itself as the visitor.
     * The marker implements ClassVisitor.
     */
    @Test
    public void testVisitClassConstant_passesMarkerAsVisitor() {
        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert - verify the marker itself is passed as the visitor
        verify(classConstant).referencedClassAccept(same(marker));
    }

    /**
     * Tests that visitClassConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitClassConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitClassConstant(clazz, classConstant));
    }

    /**
     * Tests that visitClassConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitClassConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitClassConstant(null, classConstant));

        // Verify the method still calls the accept method
        verify(classConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant with null ClassConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls methods on classConstant.
     */
    @Test
    public void testVisitClassConstant_withNullClassConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> marker.visitClassConstant(clazz, null));
    }

    /**
     * Tests that visitClassConstant can be called multiple times in succession.
     * Each call should invoke the accept method.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes_invokesAcceptMethodEachTime() {
        // Act
        marker.visitClassConstant(clazz, classConstant);
        marker.visitClassConstant(clazz, classConstant);
        marker.visitClassConstant(clazz, classConstant);

        // Assert - verify the method was called three times
        verify(classConstant, times(3)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitClassConstant_doesNotInteractWithClazz() {
        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitClassConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = marker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitClassConstant(clazz, classConstant));
        verify(classConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitClassConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitClassConstant(realClass, classConstant));
        verify(classConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitClassConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitClassConstant(libraryClass, classConstant));
        verify(classConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant works with different ClassConstant instances.
     */
    @Test
    public void testVisitClassConstant_withDifferentClassConstants_callsAcceptOnEach() {
        // Arrange
        ClassConstant classConstant1 = mock(ClassConstant.class);
        ClassConstant classConstant2 = mock(ClassConstant.class);
        ClassConstant classConstant3 = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, classConstant1);
        marker.visitClassConstant(clazz, classConstant2);
        marker.visitClassConstant(clazz, classConstant3);

        // Assert - verify each class constant had the method called
        verify(classConstant1).referencedClassAccept(eq(marker));
        verify(classConstant2).referencedClassAccept(eq(marker));
        verify(classConstant3).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that multiple AccessMethodMarker instances work independently.
     */
    @Test
    public void testVisitClassConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        ClassConstant classConstant1 = mock(ClassConstant.class);
        ClassConstant classConstant2 = mock(ClassConstant.class);

        // Act
        marker1.visitClassConstant(clazz, classConstant1);
        marker2.visitClassConstant(clazz, classConstant2);

        // Assert - verify each marker processed its respective constant
        verify(classConstant1).referencedClassAccept(eq(marker1));
        verify(classConstant2).referencedClassAccept(eq(marker2));
    }

    /**
     * Tests that visitClassConstant verifies marker implements ClassVisitor.
     */
    @Test
    public void testVisitClassConstant_markerImplementsClassVisitor() {
        // Assert - verify marker is a ClassVisitor
        assertTrue(marker instanceof ClassVisitor,
                "AccessMethodMarker should implement ClassVisitor");
    }

    /**
     * Tests that visitClassConstant handles the case where classConstant has no referenced class.
     * The method should still attempt to call the accept method even if it does nothing.
     */
    @Test
    public void testVisitClassConstant_withNoReferencedClass_stillCallsAcceptMethod() {
        // Arrange - mock that does nothing when accept method is called
        ClassConstant emptyClassConstant = mock(ClassConstant.class);
        doNothing().when(emptyClassConstant).referencedClassAccept(any(ClassVisitor.class));

        // Act
        marker.visitClassConstant(clazz, emptyClassConstant);

        // Assert - verify the method was called
        verify(emptyClassConstant).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant can handle rapid successive calls.
     */
    @Test
    public void testVisitClassConstant_rapidSuccessiveCalls_allProcessed() {
        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitClassConstant(clazz, classConstant);
        }

        // Assert
        verify(classConstant, times(100)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant works correctly with different clazz instances.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazzInstances_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);

        // Act
        marker.visitClassConstant(clazz1, classConstant);
        marker.visitClassConstant(clazz2, classConstant);

        // Assert - verify both calls processed the class constant
        verify(classConstant, times(2)).referencedClassAccept(eq(marker));

        // Verify neither clazz was interacted with
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    /**
     * Tests that visitClassConstant only interacts with the ClassConstant parameter.
     */
    @Test
    public void testVisitClassConstant_onlyInteractsWithClassConstant() {
        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert - verify only classConstant was interacted with
        verify(classConstant).referencedClassAccept(any());
        verifyNoMoreInteractions(classConstant);
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant can be invoked through the ConstantVisitor interface.
     */
    @Test
    public void testVisitClassConstant_throughConstantVisitorInterface_worksCorrectly() {
        // Arrange - use marker as ConstantVisitor
        proguard.classfile.constant.visitor.ConstantVisitor constantVisitor = marker;

        // Act
        constantVisitor.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant with both null clazz and different class constants works.
     */
    @Test
    public void testVisitClassConstant_withNullClazzAndDifferentClassConstants_worksCorrectly() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(null, cc1);
        marker.visitClassConstant(null, cc2);

        // Assert
        verify(cc1).referencedClassAccept(eq(marker));
        verify(cc2).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant preserves the marker's visitor implementation.
     * This ensures the marker can properly visit referenced classes.
     */
    @Test
    public void testVisitClassConstant_preservesVisitorImplementation() {
        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert - verify the marker was passed as ClassVisitor
        verify(classConstant).referencedClassAccept(isA(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works correctly when called on the same
     * class constant multiple times with different clazz instances.
     */
    @Test
    public void testVisitClassConstant_sameClassConstantDifferentClazz_allProcessed() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(LibraryClass.class);

        // Act
        marker.visitClassConstant(clazz1, classConstant);
        marker.visitClassConstant(clazz2, classConstant);
        marker.visitClassConstant(clazz3, classConstant);

        // Assert
        verify(classConstant, times(3)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant correctly delegates to the class constant
     * without attempting to perform the class visitation itself.
     */
    @Test
    public void testVisitClassConstant_delegatesToClassConstant_doesNotVisitDirectly() {
        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert - only the class constant should be interacted with
        verify(classConstant).referencedClassAccept(any());

        // The clazz parameter should not be touched
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant passes the correct marker instance.
     */
    @Test
    public void testVisitClassConstant_passesCorrectMarkerInstance() {
        // Arrange
        AccessMethodMarker specificMarker = new AccessMethodMarker();

        // Act
        specificMarker.visitClassConstant(clazz, classConstant);

        // Assert - verify the specific marker was passed
        verify(classConstant).referencedClassAccept(same(specificMarker));
    }

    /**
     * Tests that visitClassConstant with various class constant instances all work correctly.
     */
    @Test
    public void testVisitClassConstant_withVariousClassConstants_allWorkCorrectly() {
        // Arrange
        ClassConstant[] constants = new ClassConstant[10];
        for (int i = 0; i < 10; i++) {
            constants[i] = mock(ClassConstant.class);
        }

        // Act
        for (ClassConstant constant : constants) {
            marker.visitClassConstant(clazz, constant);
        }

        // Assert - verify each constant was processed
        for (ClassConstant constant : constants) {
            verify(constant).referencedClassAccept(eq(marker));
        }
    }

    /**
     * Tests that visitClassConstant execution completes successfully.
     */
    @Test
    public void testVisitClassConstant_completesSuccessfully() {
        // Arrange
        boolean[] completed = {false};

        // Act
        marker.visitClassConstant(clazz, classConstant);
        completed[0] = true;

        // Assert
        assertTrue(completed[0], "visitClassConstant should complete successfully");
        verify(classConstant).referencedClassAccept(any());
    }

    /**
     * Tests that visitClassConstant is stateless between calls.
     */
    @Test
    public void testVisitClassConstant_isStatelessBetweenCalls() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        // Act - interleave calls
        marker.visitClassConstant(clazz, constant1);
        marker.visitClassConstant(clazz, constant2);
        marker.visitClassConstant(clazz, constant3);
        marker.visitClassConstant(clazz, constant1);

        // Assert - verify each call processed the correct constant
        verify(constant1, times(2)).referencedClassAccept(eq(marker));
        verify(constant2, times(1)).referencedClassAccept(eq(marker));
        verify(constant3, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant alternating between different markers works correctly.
     */
    @Test
    public void testVisitClassConstant_alternatingMarkers_allProcessed() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();

        // Act
        marker1.visitClassConstant(clazz, classConstant);
        marker2.visitClassConstant(clazz, classConstant);
        marker1.visitClassConstant(clazz, classConstant);
        marker2.visitClassConstant(clazz, classConstant);

        // Assert - verify the class constant was visited by each marker twice
        verify(classConstant, times(4)).referencedClassAccept(any(AccessMethodMarker.class));
    }

    /**
     * Tests that visitClassConstant with ProgramClass and different class constants works.
     */
    @Test
    public void testVisitClassConstant_withProgramClassAndDifferentConstants_allProcessed() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(programClass, cc1);
        marker.visitClassConstant(programClass, cc2);

        // Assert
        verify(cc1).referencedClassAccept(eq(marker));
        verify(cc2).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant with LibraryClass and different class constants works.
     */
    @Test
    public void testVisitClassConstant_withLibraryClassAndDifferentConstants_allProcessed() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(libraryClass, cc1);
        marker.visitClassConstant(libraryClass, cc2);

        // Assert
        verify(cc1).referencedClassAccept(eq(marker));
        verify(cc2).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant delegates exactly once per call.
     */
    @Test
    public void testVisitClassConstant_delegatesExactlyOncePerCall() {
        // Arrange
        ClassConstant cc = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, cc);

        // Assert - verify exactly one call
        verify(cc, times(1)).referencedClassAccept(any());
        verifyNoMoreInteractions(cc);
    }

    /**
     * Tests that visitClassConstant behavior is consistent across multiple invocations.
     */
    @Test
    public void testVisitClassConstant_consistentBehaviorAcrossInvocations() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, cc1);
        marker.visitClassConstant(clazz, cc2);

        // Assert - both should have been processed identically
        verify(cc1, times(1)).referencedClassAccept(eq(marker));
        verify(cc2, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant handles sequential calls with the same constant.
     */
    @Test
    public void testVisitClassConstant_sequentialCallsWithSameConstant_allProcessed() {
        // Act
        for (int i = 0; i < 10; i++) {
            marker.visitClassConstant(clazz, classConstant);
        }

        // Assert
        verify(classConstant, times(10)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant works with mixed ProgramClass and LibraryClass instances.
     */
    @Test
    public void testVisitClassConstant_withMixedClazzTypes_allProcessed() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(programClass, cc1);
        marker.visitClassConstant(libraryClass, cc2);

        // Assert
        verify(cc1).referencedClassAccept(eq(marker));
        verify(cc2).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant properly implements the ConstantVisitor contract.
     */
    @Test
    public void testVisitClassConstant_implementsConstantVisitorContract() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = marker;

        // Act
        visitor.visitClassConstant(clazz, classConstant);

        // Assert - verify the method behaves correctly through the interface
        verify(classConstant).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant handles edge case of many rapid calls.
     */
    @Test
    public void testVisitClassConstant_manyRapidCalls_allProcessed() {
        // Act
        for (int i = 0; i < 1000; i++) {
            marker.visitClassConstant(clazz, classConstant);
        }

        // Assert
        verify(classConstant, times(1000)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitClassConstant only makes the expected single delegation call.
     */
    @Test
    public void testVisitClassConstant_onlyMakesSingleDelegationCall() {
        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert - verify only referencedClassAccept was called, nothing else
        verify(classConstant).referencedClassAccept(any());
        verifyNoMoreInteractions(classConstant);
    }
}
