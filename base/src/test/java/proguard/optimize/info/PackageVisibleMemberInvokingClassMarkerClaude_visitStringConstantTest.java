package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link PackageVisibleMemberInvokingClassMarker#visitStringConstant(Clazz, StringConstant)}.
 *
 * The visitStringConstant method checks for referenced classes or class members in
 * string constants. It has a conditional check: if the stringConstant.referencedClass
 * is different from the current clazz, it:
 * 1. Sets the referencingClass field to the current clazz
 * 2. Delegates to the string constant to visit any referenced class
 * 3. Delegates to the string constant to visit any referenced member
 *
 * If the referenced class is the same as the current clazz, the method does nothing.
 * This prevents marking classes that reference themselves.
 */
public class PackageVisibleMemberInvokingClassMarkerClaude_visitStringConstantTest {

    private PackageVisibleMemberInvokingClassMarker marker;
    private Clazz clazz;
    private StringConstant stringConstant;

    @BeforeEach
    public void setUp() {
        marker = new PackageVisibleMemberInvokingClassMarker();
        clazz = mock(ProgramClass.class);
        stringConstant = mock(StringConstant.class);
    }

    /**
     * Tests that visitStringConstant calls referencedClassAccept when referencedClass differs from clazz.
     * This is one of the core behaviors - the method should delegate to the string
     * constant to visit any referenced class when the classes are different.
     */
    @Test
    public void testVisitStringConstant_withDifferentReferencedClass_callsReferencedClassAccept() {
        // Arrange - set up referencedClass to be different from clazz
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify that referencedClassAccept was called with the marker
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant calls referencedMemberAccept when referencedClass differs from clazz.
     * This is the second core behavior - the method should delegate to the string
     * constant to visit any referenced member when the classes are different.
     */
    @Test
    public void testVisitStringConstant_withDifferentReferencedClass_callsReferencedMemberAccept() {
        // Arrange - set up referencedClass to be different from clazz
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify that referencedMemberAccept was called with the marker
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant calls both accept methods when referencedClass differs.
     * This verifies that both checks are performed on each invocation.
     */
    @Test
    public void testVisitStringConstant_withDifferentReferencedClass_callsBothAcceptMethods() {
        // Arrange - set up referencedClass to be different from clazz
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify both methods were called
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant does NOT call accept methods when referencedClass equals clazz.
     * This is the key conditional behavior - when a class references itself, no processing occurs.
     */
    @Test
    public void testVisitStringConstant_withSameReferencedClass_doesNotCallAcceptMethods() {
        // Arrange - set up referencedClass to be the same as clazz
        stringConstant.referencedClass = clazz;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify no methods were called on stringConstant
        verifyNoInteractions(stringConstant);
    }

    /**
     * Tests that visitStringConstant does NOT call referencedClassAccept when referencedClass equals clazz.
     */
    @Test
    public void testVisitStringConstant_withSameReferencedClass_doesNotCallReferencedClassAccept() {
        // Arrange
        stringConstant.referencedClass = clazz;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, never()).referencedClassAccept(any());
    }

    /**
     * Tests that visitStringConstant does NOT call referencedMemberAccept when referencedClass equals clazz.
     */
    @Test
    public void testVisitStringConstant_withSameReferencedClass_doesNotCallReferencedMemberAccept() {
        // Arrange
        stringConstant.referencedClass = clazz;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, never()).referencedMemberAccept(any());
    }

    /**
     * Tests that visitStringConstant with null referencedClass triggers the accept methods.
     * null is not equal to clazz, so the condition should pass.
     */
    @Test
    public void testVisitStringConstant_withNullReferencedClass_callsAcceptMethods() {
        // Arrange - referencedClass is null by default
        stringConstant.referencedClass = null;

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
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

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
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitStringConstant(clazz, stringConstant));
    }

    /**
     * Tests that visitStringConstant can be called with null Clazz parameter.
     * With null clazz, the condition (referencedClass != clazz) should be true if referencedClass is not null.
     */
    @Test
    public void testVisitStringConstant_withNullClazz_andNonNullReferencedClass_callsAcceptMethods() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitStringConstant(null, stringConstant));

        // Verify the method calls both accept methods
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant with both null clazz and null referencedClass.
     * Both are null, so referencedClass == clazz (null == null), should not call accept methods.
     */
    @Test
    public void testVisitStringConstant_withBothNull_doesNotCallAcceptMethods() {
        // Arrange
        stringConstant.referencedClass = null;

        // Act
        marker.visitStringConstant(null, stringConstant);

        // Assert - since both are null, they're equal, so no accept methods should be called
        verify(stringConstant, never()).referencedClassAccept(any());
        verify(stringConstant, never()).referencedMemberAccept(any());
    }

    /**
     * Tests that visitStringConstant with null StringConstant throws NullPointerException.
     * This should result in a NullPointerException when trying to access referencedClass.
     */
    @Test
    public void testVisitStringConstant_withNullStringConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> marker.visitStringConstant(clazz, null));
    }

    /**
     * Tests that visitStringConstant can be called multiple times with different referenced classes.
     * Each call with different referencedClass should invoke both accept methods.
     */
    @Test
    public void testVisitStringConstant_calledMultipleTimesWithDifferentReferences_invokesBothAcceptMethodsEachTime() {
        // Arrange
        Clazz referencedClass1 = mock(ProgramClass.class);
        Clazz referencedClass2 = mock(LibraryClass.class);
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);
        sc1.referencedClass = referencedClass1;
        sc2.referencedClass = referencedClass2;

        // Act
        marker.visitStringConstant(clazz, sc1);
        marker.visitStringConstant(clazz, sc2);

        // Assert - verify both methods were called for each string constant
        verify(sc1, times(1)).referencedClassAccept(eq(marker));
        verify(sc1, times(1)).referencedMemberAccept(eq(marker));
        verify(sc2, times(1)).referencedClassAccept(eq(marker));
        verify(sc2, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant doesn't directly interact with the Clazz parameter.
     * The clazz is used only for comparison, not for method calls.
     */
    @Test
    public void testVisitStringConstant_doesNotInteractWithClazz() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests the order of method calls - referencedClassAccept should be called before referencedMemberAccept.
     */
    @Test
    public void testVisitStringConstant_callsReferencedClassAcceptBeforeReferencedMemberAccept() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;
        org.mockito.InOrder inOrder = inOrder(stringConstant);

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify the order of calls
        inOrder.verify(stringConstant).referencedClassAccept(eq(marker));
        inOrder.verify(stringConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitStringConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;
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
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

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
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(libraryClass, stringConstant));
        verify(stringConstant, times(1)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that multiple PackageVisibleMemberInvokingClassMarker instances work independently.
     */
    @Test
    public void testVisitStringConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        PackageVisibleMemberInvokingClassMarker marker1 = new PackageVisibleMemberInvokingClassMarker();
        PackageVisibleMemberInvokingClassMarker marker2 = new PackageVisibleMemberInvokingClassMarker();
        StringConstant stringConstant1 = mock(StringConstant.class);
        StringConstant stringConstant2 = mock(StringConstant.class);
        Clazz refClass1 = mock(ProgramClass.class);
        Clazz refClass2 = mock(ProgramClass.class);
        stringConstant1.referencedClass = refClass1;
        stringConstant2.referencedClass = refClass2;

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
                "PackageVisibleMemberInvokingClassMarker should implement ClassVisitor");
    }

    /**
     * Tests that visitStringConstant verifies marker implements MemberVisitor.
     */
    @Test
    public void testVisitStringConstant_markerImplementsMemberVisitor() {
        // Assert - verify marker is a MemberVisitor
        assertTrue(marker instanceof MemberVisitor,
                "PackageVisibleMemberInvokingClassMarker should implement MemberVisitor");
    }

    /**
     * Tests that visitStringConstant handles the case where stringConstant has no referenced member.
     * The method should still attempt to call the accept methods.
     */
    @Test
    public void testVisitStringConstant_withNoReferencedMember_stillCallsAcceptMethods() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        StringConstant stringConstantNoMember = mock(StringConstant.class);
        stringConstantNoMember.referencedClass = referencedClass;
        doNothing().when(stringConstantNoMember).referencedClassAccept(any(ClassVisitor.class));
        doNothing().when(stringConstantNoMember).referencedMemberAccept(any(MemberVisitor.class));

        // Act
        marker.visitStringConstant(clazz, stringConstantNoMember);

        // Assert - verify both methods were called
        verify(stringConstantNoMember).referencedClassAccept(eq(marker));
        verify(stringConstantNoMember).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant can handle rapid successive calls with different references.
     */
    @Test
    public void testVisitStringConstant_rapidSuccessiveCallsWithDifferentReferences_allProcessed() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        // Assert
        verify(stringConstant, times(100)).referencedClassAccept(eq(marker));
        verify(stringConstant, times(100)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant with same reference multiple times does nothing.
     */
    @Test
    public void testVisitStringConstant_rapidSuccessiveCallsWithSameReference_neverProcessed() {
        // Arrange
        stringConstant.referencedClass = clazz;

        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        // Assert - should never call accept methods
        verify(stringConstant, never()).referencedClassAccept(any());
        verify(stringConstant, never()).referencedMemberAccept(any());
    }

    /**
     * Tests that visitStringConstant works correctly with different clazz instances.
     */
    @Test
    public void testVisitStringConstant_withDifferentClazzInstances_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

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
     * Tests that visitStringConstant only interacts with the StringConstant parameter when condition is met.
     */
    @Test
    public void testVisitStringConstant_whenConditionMet_onlyInteractsWithStringConstant() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify only stringConstant was interacted with
        verify(stringConstant).referencedClassAccept(any());
        verify(stringConstant).referencedMemberAccept(any());
        verifyNoMoreInteractions(stringConstant);
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant preserves the marker's visitor implementation.
     * This ensures the marker can properly visit referenced classes and members.
     */
    @Test
    public void testVisitStringConstant_preservesVisitorImplementation() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify the marker was passed as both ClassVisitor and MemberVisitor
        verify(stringConstant).referencedClassAccept(isA(ClassVisitor.class));
        verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant correctly delegates to the string constant
     * without attempting to perform the class/member visitation itself.
     */
    @Test
    public void testVisitStringConstant_delegatesToStringConstant_doesNotVisitDirectly() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - only the string constant should be interacted with
        verify(stringConstant).referencedClassAccept(any());
        verify(stringConstant).referencedMemberAccept(any());

        // The clazz parameter should not be touched
        verifyNoInteractions(clazz);
    }

    /**
     * Tests boundary case where referencedClass and clazz are the exact same instance.
     */
    @Test
    public void testVisitStringConstant_withSameInstanceReference_doesNotCallAcceptMethods() {
        // Arrange - use the exact same instance
        stringConstant.referencedClass = clazz;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - no interactions should occur
        verifyNoInteractions(stringConstant);
    }

    /**
     * Tests that the marker correctly sets referencingClass field when processing.
     * This is verified indirectly through the accept method calls which would use the field.
     */
    @Test
    public void testVisitStringConstant_setsReferencingClassField_beforeAcceptCalls() {
        // Arrange
        Clazz referencedClass = mock(ProgramClass.class);
        stringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - the accept methods should be called, indicating referencingClass was set
        verify(stringConstant).referencedClassAccept(marker);
        verify(stringConstant).referencedMemberAccept(marker);
    }

    /**
     * Tests integration scenario: visitStringConstant followed by actual class/member visitation.
     * This tests the full flow including the marker's ClassVisitor and MemberVisitor implementations.
     */
    @Test
    public void testVisitStringConstant_integrationWithClassAndMemberVisitation() {
        // Arrange
        ProgramClass referencingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencingClass);

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0; // package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        StringConstant realStringConstant = new StringConstant();
        realStringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(referencingClass, realStringConstant);
        // Manually invoke the class visitor to complete the flow
        realStringConstant.referencedClassAccept(marker);

        // Assert - the referencing class should be marked
        assertTrue(PackageVisibleMemberInvokingClassMarker.invokesPackageVisibleMembers(referencingClass),
                "Referencing class should be marked as invoking package-visible members");
    }

    /**
     * Tests integration scenario where referenced class is public - should not mark.
     */
    @Test
    public void testVisitStringConstant_integrationWithPublicClass_doesNotMark() {
        // Arrange
        ProgramClass referencingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencingClass);

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = AccessConstants.PUBLIC;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        StringConstant realStringConstant = new StringConstant();
        realStringConstant.referencedClass = referencedClass;

        // Act
        marker.visitStringConstant(referencingClass, realStringConstant);
        realStringConstant.referencedClassAccept(marker);

        // Assert - the referencing class should NOT be marked
        assertFalse(PackageVisibleMemberInvokingClassMarker.invokesPackageVisibleMembers(referencingClass),
                "Referencing class should not be marked when referencing public class");
    }

    /**
     * Tests integration scenario with package-visible member.
     */
    @Test
    public void testVisitStringConstant_integrationWithPackageVisibleMember_marks() {
        // Arrange
        ProgramClass referencingClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencingClass);

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = AccessConstants.PUBLIC;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0; // package-private

        StringConstant realStringConstant = new StringConstant();
        realStringConstant.referencedClass = referencedClass;
        realStringConstant.referencedMember = packageField;

        // Act
        marker.visitStringConstant(referencingClass, realStringConstant);
        realStringConstant.referencedMemberAccept(marker);

        // Assert - the referencing class should be marked
        assertTrue(PackageVisibleMemberInvokingClassMarker.invokesPackageVisibleMembers(referencingClass),
                "Referencing class should be marked when referencing package-visible member");
    }

    /**
     * Tests that condition properly handles case where both clazz and referencedClass are null.
     */
    @Test
    public void testVisitStringConstant_withBothNullClasses_treatsAsEqual() {
        // Arrange
        stringConstant.referencedClass = null;

        // Act
        marker.visitStringConstant(null, stringConstant);

        // Assert - null == null, so should not process
        verify(stringConstant, never()).referencedClassAccept(any());
        verify(stringConstant, never()).referencedMemberAccept(any());
    }

    /**
     * Tests alternating between same and different references.
     */
    @Test
    public void testVisitStringConstant_alternatingReferences_onlyProcessesDifferent() {
        // Arrange
        Clazz differentClass = mock(ProgramClass.class);
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);
        sc1.referencedClass = clazz; // same
        sc2.referencedClass = differentClass; // different

        // Act
        marker.visitStringConstant(clazz, sc1); // should not process
        marker.visitStringConstant(clazz, sc2); // should process
        marker.visitStringConstant(clazz, sc1); // should not process
        marker.visitStringConstant(clazz, sc2); // should process

        // Assert
        verify(sc1, never()).referencedClassAccept(any());
        verify(sc1, never()).referencedMemberAccept(any());
        verify(sc2, times(2)).referencedClassAccept(eq(marker));
        verify(sc2, times(2)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that marker state is correctly maintained across multiple calls.
     */
    @Test
    public void testVisitStringConstant_multipleCallsWithDifferentClazz_updatesReferencingClass() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz refClass = mock(ProgramClass.class);
        StringConstant sc = mock(StringConstant.class);
        sc.referencedClass = refClass;

        // Act - call with different clazz instances
        marker.visitStringConstant(clazz1, sc);
        marker.visitStringConstant(clazz2, sc);

        // Assert - both should be processed
        verify(sc, times(2)).referencedClassAccept(eq(marker));
        verify(sc, times(2)).referencedMemberAccept(eq(marker));
    }
}
