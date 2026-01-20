package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}.
 *
 * The visitStringConstant method checks for referenced classes or class members in
 * string constants. It delegates to the string constant to visit any referenced class
 * and any referenced member, allowing the AccessMethodMarker to track what access
 * levels are accessed by methods. This is important for marking methods that access
 * private, protected, or package-private code through string constant references
 * (e.g., for reflection).
 */
public class AccessMethodMarkerClaude_visitStringConstantTest {

    private AccessMethodMarker marker;
    private Clazz clazz;
    private StringConstant stringConstant;

    @BeforeEach
    public void setUp() {
        marker = new AccessMethodMarker();
        clazz = mock(ProgramClass.class);
        stringConstant = mock(StringConstant.class);
    }

    /**
     * Tests that visitStringConstant calls referencedClassAccept on the StringConstant.
     * This is one of the two core behaviors - the method should delegate to the string
     * constant to visit any referenced class.
     */
    @Test
    public void testVisitStringConstant_callsReferencedClassAccept() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify that referencedClassAccept was called with the marker
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant calls referencedMemberAccept on the StringConstant.
     * This is the second core behavior - the method should delegate to the string
     * constant to visit any referenced member.
     */
    @Test
    public void testVisitStringConstant_callsReferencedMemberAccept() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify that referencedMemberAccept was called with the marker
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant calls both referencedClassAccept and referencedMemberAccept.
     * This verifies that both checks are performed on each invocation.
     */
    @Test
    public void testVisitStringConstant_callsBothAcceptMethods() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify both methods were called
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant passes the marker itself as the visitor.
     * The marker implements both ClassVisitor and MemberVisitor.
     */
    @Test
    public void testVisitStringConstant_passesMarkerAsVisitor() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify the marker itself is passed as the visitor
        verify(stringConstant).referencedClassAccept(same(marker));
        verify(stringConstant).referencedMemberAccept(same(marker));
    }

    /**
     * Tests that visitStringConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitStringConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitStringConstant(clazz, stringConstant));
    }

    /**
     * Tests that visitStringConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitStringConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitStringConstant(null, stringConstant));

        // Verify the method still calls both accept methods
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant with null StringConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls methods on stringConstant.
     */
    @Test
    public void testVisitStringConstant_withNullStringConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> marker.visitStringConstant(clazz, null));
    }

    /**
     * Tests that visitStringConstant can be called multiple times in succession.
     * Each call should invoke both accept methods.
     */
    @Test
    public void testVisitStringConstant_calledMultipleTimes_invokesBothAcceptMethodsEachTime() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);
        marker.visitStringConstant(clazz, stringConstant);
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify both methods were called three times each
        verify(stringConstant, times(3)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitStringConstant_doesNotInteractWithClazz() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitStringConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = marker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitStringConstant(clazz, stringConstant));
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitStringConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(realClass, stringConstant));
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitStringConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(libraryClass, stringConstant));
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant works with different StringConstant instances.
     */
    @Test
    public void testVisitStringConstant_withDifferentStringConstants_callsAcceptOnEach() {
        // Arrange
        StringConstant stringConstant1 = mock(StringConstant.class);
        StringConstant stringConstant2 = mock(StringConstant.class);
        StringConstant stringConstant3 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz, stringConstant1);
        marker.visitStringConstant(clazz, stringConstant2);
        marker.visitStringConstant(clazz, stringConstant3);

        // Assert - verify each string constant had both methods called
        verify(stringConstant1).referencedClassAccept(eq(marker));
        verify(stringConstant1).referencedMemberAccept(eq(marker));
        verify(stringConstant2).referencedClassAccept(eq(marker));
        verify(stringConstant2).referencedMemberAccept(eq(marker));
        verify(stringConstant3).referencedClassAccept(eq(marker));
        verify(stringConstant3).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that multiple AccessMethodMarker instances work independently.
     */
    @Test
    public void testVisitStringConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        StringConstant stringConstant1 = mock(StringConstant.class);
        StringConstant stringConstant2 = mock(StringConstant.class);

        // Act
        marker1.visitStringConstant(clazz, stringConstant1);
        marker2.visitStringConstant(clazz, stringConstant2);

        // Assert - verify each marker processed its respective constant
        verify(stringConstant1).referencedClassAccept(eq(marker1));
        verify(stringConstant1).referencedMemberAccept(eq(marker1));
        verify(stringConstant2).referencedClassAccept(eq(marker2));
        verify(stringConstant2).referencedMemberAccept(eq(marker2));
    }

    /**
     * Tests that visitStringConstant verifies marker implements ClassVisitor.
     */
    @Test
    public void testVisitStringConstant_markerImplementsClassVisitor() {
        // Assert - verify marker is a ClassVisitor
        assertTrue(marker instanceof ClassVisitor,
                "AccessMethodMarker should implement ClassVisitor");
    }

    /**
     * Tests that visitStringConstant verifies marker implements MemberVisitor.
     */
    @Test
    public void testVisitStringConstant_markerImplementsMemberVisitor() {
        // Assert - verify marker is a MemberVisitor
        assertTrue(marker instanceof MemberVisitor,
                "AccessMethodMarker should implement MemberVisitor");
    }

    /**
     * Tests that visitStringConstant handles the case where stringConstant has no referenced class.
     * The method should still attempt to call the accept methods even if they do nothing.
     */
    @Test
    public void testVisitStringConstant_withNoReferencedClass_stillCallsAcceptMethods() {
        // Arrange - mock that does nothing when accept methods are called
        StringConstant emptyStringConstant = mock(StringConstant.class);
        doNothing().when(emptyStringConstant).referencedClassAccept(any(ClassVisitor.class));
        doNothing().when(emptyStringConstant).referencedMemberAccept(any(MemberVisitor.class));

        // Act
        marker.visitStringConstant(clazz, emptyStringConstant);

        // Assert - verify both methods were called
        verify(emptyStringConstant).referencedClassAccept(eq(marker));
        verify(emptyStringConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant handles the case where stringConstant has no referenced member.
     * The method should still attempt to call the accept methods.
     */
    @Test
    public void testVisitStringConstant_withNoReferencedMember_stillCallsAcceptMethods() {
        // Arrange
        StringConstant stringConstantNoMember = mock(StringConstant.class);
        doNothing().when(stringConstantNoMember).referencedClassAccept(any(ClassVisitor.class));
        doNothing().when(stringConstantNoMember).referencedMemberAccept(any(MemberVisitor.class));

        // Act
        marker.visitStringConstant(clazz, stringConstantNoMember);

        // Assert - verify both methods were called
        verify(stringConstantNoMember).referencedClassAccept(eq(marker));
        verify(stringConstantNoMember).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests the order of method calls - referencedClassAccept should be called before referencedMemberAccept.
     */
    @Test
    public void testVisitStringConstant_callsReferencedClassAcceptBeforeReferencedMemberAccept() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(stringConstant);

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify the order of calls
        inOrder.verify(stringConstant).referencedClassAccept(eq(marker));
        inOrder.verify(stringConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant can handle rapid successive calls.
     */
    @Test
    public void testVisitStringConstant_rapidSuccessiveCalls_allProcessed() {
        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        // Assert
        verify(stringConstant, times(100)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(100)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant works correctly with different clazz instances.
     */
    @Test
    public void testVisitStringConstant_withDifferentClazzInstances_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);

        // Act
        marker.visitStringConstant(clazz1, stringConstant);
        marker.visitStringConstant(clazz2, stringConstant);

        // Assert - verify both calls processed the string constant
        verify(stringConstant, times(2)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(2)).referencedMemberAccept(eq(marker));

        // Verify neither clazz was interacted with
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    /**
     * Tests that visitStringConstant only interacts with the StringConstant parameter.
     */
    @Test
    public void testVisitStringConstant_onlyInteractsWithStringConstant() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify only stringConstant was interacted with
        verify(stringConstant).referencedClassAccept(any());
        verify(stringConstant).referencedMemberAccept(any());
        verifyNoMoreInteractions(stringConstant);
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant can be invoked through the ConstantVisitor interface.
     */
    @Test
    public void testVisitStringConstant_throughConstantVisitorInterface_worksCorrectly() {
        // Arrange - use marker as ConstantVisitor
        proguard.classfile.constant.visitor.ConstantVisitor constantVisitor = marker;

        // Act
        constantVisitor.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant).referencedClassAccept(eq(marker));
        verify(stringConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant with both null clazz and different string constants works.
     */
    @Test
    public void testVisitStringConstant_withNullClazzAndDifferentStringConstants_worksCorrectly() {
        // Arrange
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(null, sc1);
        marker.visitStringConstant(null, sc2);

        // Assert
        verify(sc1).referencedClassAccept(eq(marker));
        verify(sc1).referencedMemberAccept(eq(marker));
        verify(sc2).referencedClassAccept(eq(marker));
        verify(sc2).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant preserves the marker's visitor implementation.
     * This ensures the marker can properly visit referenced classes and members.
     */
    @Test
    public void testVisitStringConstant_preservesVisitorImplementation() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify the marker was passed as both ClassVisitor and MemberVisitor
        verify(stringConstant).referencedClassAccept(isA(ClassVisitor.class));
        verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant executes both accept calls even if one might fail.
     * Both method calls should be attempted regardless.
     */
    @Test
    public void testVisitStringConstant_attemptsBoAccessCalls() {
        // Arrange
        StringConstant stringConst = mock(StringConstant.class);
        // Don't set up any special behavior - just verify both are called

        // Act
        marker.visitStringConstant(clazz, stringConst);

        // Assert - verify both were attempted
        verify(stringConst, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(stringConst, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant works correctly when called on the same
     * string constant multiple times with different clazz instances.
     */
    @Test
    public void testVisitStringConstant_sameStringConstantDifferentClazz_allProcessed() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(LibraryClass.class);

        // Act
        marker.visitStringConstant(clazz1, stringConstant);
        marker.visitStringConstant(clazz2, stringConstant);
        marker.visitStringConstant(clazz3, stringConstant);

        // Assert
        verify(stringConstant, times(3)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant correctly delegates to the string constant
     * without attempting to perform the class/member visitation itself.
     */
    @Test
    public void testVisitStringConstant_delegatesToStringConstant_doesNotVisitDirectly() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - only the string constant should be interacted with
        verify(stringConstant).referencedClassAccept(any());
        verify(stringConstant).referencedMemberAccept(any());

        // The clazz parameter should not be touched
        verifyNoInteractions(clazz);
    }
}
