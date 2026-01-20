package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
 *
 * The visitAnyRefConstant method processes reference constants (field refs, method refs, etc.)
 * by checking both the referenced class and the referenced member. It does three things:
 * 1. Calls constantPoolEntryAccept with the class index to check the class constant
 * 2. Calls referencedClassAccept to visit the referenced class
 * 3. Calls referencedMemberAccept to visit the referenced member
 * This allows the AccessMethodMarker to track access levels (private, protected, package)
 * accessed through reference constants.
 */
public class AccessMethodMarkerClaude_visitAnyRefConstantTest {

    private AccessMethodMarker marker;
    private Clazz clazz;
    private RefConstant refConstant;

    @BeforeEach
    public void setUp() {
        marker = new AccessMethodMarker();
        clazz = mock(ProgramClass.class);
        refConstant = mock(RefConstant.class);
    }

    /**
     * Tests that visitAnyRefConstant calls constantPoolEntryAccept with the correct class index.
     * This is the first action - checking the referenced class constant.
     */
    @Test
    public void testVisitAnyRefConstant_callsConstantPoolEntryAccept() {
        // Arrange
        refConstant.u2classIndex = 5;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify constantPoolEntryAccept was called with the class index
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant calls referencedClassAccept on the RefConstant.
     * This is the second action - visiting the referenced class.
     */
    @Test
    public void testVisitAnyRefConstant_callsReferencedClassAccept() {
        // Arrange
        refConstant.u2classIndex = 1;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify referencedClassAccept was called
        verify(refConstant, times(1)).referencedClassAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant calls referencedMemberAccept on the RefConstant.
     * This is the third action - visiting the referenced member.
     */
    @Test
    public void testVisitAnyRefConstant_callsReferencedMemberAccept() {
        // Arrange
        refConstant.u2classIndex = 1;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify referencedMemberAccept was called
        verify(refConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant calls all three methods.
     * This verifies the complete flow of the method.
     */
    @Test
    public void testVisitAnyRefConstant_callsAllThreeMethods() {
        // Arrange
        refConstant.u2classIndex = 3;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify all three methods were called
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(refConstant, times(1)).referencedClassAccept(eq(marker));
        verify(refConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant passes the marker itself as the visitor.
     * The marker implements ClassVisitor, MemberVisitor, and ConstantVisitor.
     */
    @Test
    public void testVisitAnyRefConstant_passesMarkerAsVisitor() {
        // Arrange
        refConstant.u2classIndex = 1;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify the marker itself is passed as the visitor
        verify(clazz).constantPoolEntryAccept(anyInt(), same(marker));
        verify(refConstant).referencedClassAccept(same(marker));
        verify(refConstant).referencedMemberAccept(same(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitAnyRefConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        refConstant.u2classIndex = 1;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyRefConstant(clazz, refConstant));
    }

    /**
     * Tests that visitAnyRefConstant can be called multiple times in succession.
     * Each call should invoke all three operations.
     */
    @Test
    public void testVisitAnyRefConstant_calledMultipleTimes_invokesAllMethodsEachTime() {
        // Arrange
        refConstant.u2classIndex = 5;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);
        marker.visitAnyRefConstant(clazz, refConstant);
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify all methods were called three times each
        verify(clazz, times(3)).constantPoolEntryAccept(eq(5), eq(marker));
        verify(refConstant, times(3)).referencedClassAccept(eq(marker));
        verify(refConstant, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with different class indices.
     */
    @Test
    public void testVisitAnyRefConstant_withDifferentClassIndices_accessesCorrectEntries() {
        // Arrange
        RefConstant constant1 = mock(RefConstant.class);
        RefConstant constant10 = mock(RefConstant.class);
        RefConstant constant100 = mock(RefConstant.class);
        constant1.u2classIndex = 1;
        constant10.u2classIndex = 10;
        constant100.u2classIndex = 100;

        // Act
        marker.visitAnyRefConstant(clazz, constant1);
        marker.visitAnyRefConstant(clazz, constant10);
        marker.visitAnyRefConstant(clazz, constant100);

        // Assert - verify each class index was accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with different RefConstant instances.
     */
    @Test
    public void testVisitAnyRefConstant_withDifferentRefConstants_processesBoth() {
        // Arrange
        RefConstant constant1 = mock(RefConstant.class);
        RefConstant constant2 = mock(RefConstant.class);
        constant1.u2classIndex = 1;
        constant2.u2classIndex = 2;

        // Act
        marker.visitAnyRefConstant(clazz, constant1);
        marker.visitAnyRefConstant(clazz, constant2);

        // Assert - verify both constants were fully processed
        verify(constant1).referencedClassAccept(eq(marker));
        verify(constant1).referencedMemberAccept(eq(marker));
        verify(constant2).referencedClassAccept(eq(marker));
        verify(constant2).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests the order of method calls in visitAnyRefConstant.
     * constantPoolEntryAccept should be called before referencedClassAccept and referencedMemberAccept.
     */
    @Test
    public void testVisitAnyRefConstant_methodCallOrder() {
        // Arrange
        refConstant.u2classIndex = 1;
        org.mockito.InOrder inOrder = inOrder(clazz, refConstant);

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify the order of calls
        inOrder.verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        inOrder.verify(refConstant).referencedClassAccept(eq(marker));
        inOrder.verify(refConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works correctly with FieldrefConstant.
     * FieldrefConstant is a subtype of RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withFieldrefConstant_worksCorrectly() {
        // Arrange
        FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
        fieldrefConstant.u2classIndex = 5;

        // Act
        marker.visitAnyRefConstant(clazz, fieldrefConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(fieldrefConstant).referencedClassAccept(eq(marker));
        verify(fieldrefConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works correctly with MethodrefConstant.
     * MethodrefConstant is a subtype of RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withMethodrefConstant_worksCorrectly() {
        // Arrange
        MethodrefConstant methodrefConstant = mock(MethodrefConstant.class);
        methodrefConstant.u2classIndex = 7;

        // Act
        marker.visitAnyRefConstant(clazz, methodrefConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(7), eq(marker));
        verify(methodrefConstant).referencedClassAccept(eq(marker));
        verify(methodrefConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works correctly with InterfaceMethodrefConstant.
     * InterfaceMethodrefConstant is a subtype of RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withInterfaceMethodrefConstant_worksCorrectly() {
        // Arrange
        InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);
        interfaceMethodrefConstant.u2classIndex = 9;

        // Act
        marker.visitAnyRefConstant(clazz, interfaceMethodrefConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(9), eq(marker));
        verify(interfaceMethodrefConstant).referencedClassAccept(eq(marker));
        verify(interfaceMethodrefConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with different clazz instances.
     */
    @Test
    public void testVisitAnyRefConstant_withDifferentClazzInstances_accessesCorrectConstantPools() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        refConstant.u2classIndex = 1;

        // Act
        marker.visitAnyRefConstant(clazz1, refConstant);
        marker.visitAnyRefConstant(clazz2, refConstant);

        // Assert - verify each clazz's constant pool was accessed
        verify(clazz1).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that multiple AccessMethodMarker instances work independently.
     */
    @Test
    public void testVisitAnyRefConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        RefConstant constant1 = mock(RefConstant.class);
        RefConstant constant2 = mock(RefConstant.class);
        constant1.u2classIndex = 1;
        constant2.u2classIndex = 2;

        // Act
        marker1.visitAnyRefConstant(clazz, constant1);
        marker2.visitAnyRefConstant(clazz, constant2);

        // Assert - verify each marker processed its respective constant
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(constant1).referencedClassAccept(eq(marker1));
        verify(constant1).referencedMemberAccept(eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker2));
        verify(constant2).referencedClassAccept(eq(marker2));
        verify(constant2).referencedMemberAccept(eq(marker2));
    }

    /**
     * Tests that visitAnyRefConstant marker implements ClassVisitor.
     */
    @Test
    public void testVisitAnyRefConstant_markerImplementsClassVisitor() {
        // Assert - verify marker is a ClassVisitor
        assertTrue(marker instanceof ClassVisitor,
                "AccessMethodMarker should implement ClassVisitor");
    }

    /**
     * Tests that visitAnyRefConstant marker implements MemberVisitor.
     */
    @Test
    public void testVisitAnyRefConstant_markerImplementsMemberVisitor() {
        // Assert - verify marker is a MemberVisitor
        assertTrue(marker instanceof MemberVisitor,
                "AccessMethodMarker should implement MemberVisitor");
    }

    /**
     * Tests that visitAnyRefConstant marker implements ConstantVisitor.
     */
    @Test
    public void testVisitAnyRefConstant_markerImplementsConstantVisitor() {
        // Assert - verify marker is a ConstantVisitor
        assertTrue(marker instanceof ConstantVisitor,
                "AccessMethodMarker should implement ConstantVisitor");
    }

    /**
     * Tests that visitAnyRefConstant handles rapid successive calls.
     */
    @Test
    public void testVisitAnyRefConstant_rapidSuccessiveCalls_allProcessed() {
        // Arrange
        refConstant.u2classIndex = 1;

        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitAnyRefConstant(clazz, refConstant);
        }

        // Assert
        verify(clazz, times(100)).constantPoolEntryAccept(eq(1), eq(marker));
        verify(refConstant, times(100)).referencedClassAccept(eq(marker));
        verify(refConstant, times(100)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant with class index 0 works correctly.
     */
    @Test
    public void testVisitAnyRefConstant_withClassIndex0_accessesConstantPool() {
        // Arrange
        refConstant.u2classIndex = 0;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
        verify(refConstant).referencedClassAccept(eq(marker));
        verify(refConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with large class index values.
     */
    @Test
    public void testVisitAnyRefConstant_withLargeClassIndex_accessesConstantPool() {
        // Arrange
        refConstant.u2classIndex = 65535;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with ProgramClass.
     */
    @Test
    public void testVisitAnyRefConstant_withProgramClass_worksCorrectly() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        refConstant.u2classIndex = 5;

        // Act
        marker.visitAnyRefConstant(programClass, refConstant);

        // Assert
        verify(programClass).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with LibraryClass.
     */
    @Test
    public void testVisitAnyRefConstant_withLibraryClass_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass = mock(LibraryClass.class);
        refConstant.u2classIndex = 5;

        // Act
        marker.visitAnyRefConstant(libraryClass, refConstant);

        // Assert
        verify(libraryClass).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant can be invoked through the ConstantVisitor interface.
     */
    @Test
    public void testVisitAnyRefConstant_throughConstantVisitorInterface_worksCorrectly() {
        // Arrange - use marker as ConstantVisitor
        ConstantVisitor constantVisitor = marker;
        refConstant.u2classIndex = 1;

        // Act
        constantVisitor.visitAnyRefConstant(clazz, refConstant);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(refConstant).referencedClassAccept(eq(marker));
        verify(refConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant sequentially calls with different constants are all processed.
     */
    @Test
    public void testVisitAnyRefConstant_sequentialCallsWithDifferentConstants_allProcessed() {
        // Arrange & Act
        for (int i = 1; i <= 20; i++) {
            RefConstant constant = mock(RefConstant.class);
            constant.u2classIndex = i;
            marker.visitAnyRefConstant(clazz, constant);

            // Assert - verify this constant was fully processed
            verify(clazz).constantPoolEntryAccept(eq(i), eq(marker));
            verify(constant).referencedClassAccept(eq(marker));
            verify(constant).referencedMemberAccept(eq(marker));
        }
    }

    /**
     * Tests that visitAnyRefConstant alternating between different clazz instances works correctly.
     */
    @Test
    public void testVisitAnyRefConstant_alternatingClazzInstances_allAccepted() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        refConstant.u2classIndex = 1;

        // Act
        marker.visitAnyRefConstant(clazz1, refConstant);
        marker.visitAnyRefConstant(clazz2, refConstant);
        marker.visitAnyRefConstant(clazz1, refConstant);
        marker.visitAnyRefConstant(clazz2, refConstant);

        // Assert
        verify(clazz1, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
        verify(refConstant, times(4)).referencedClassAccept(eq(marker));
        verify(refConstant, times(4)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant correctly reads the u2classIndex field.
     */
    @Test
    public void testVisitAnyRefConstant_readsU2ClassIndexField() {
        // Arrange
        refConstant.u2classIndex = 42;

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify the u2classIndex field value was used
        verify(clazz).constantPoolEntryAccept(eq(42), eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant works with all RefConstant subtypes.
     */
    @Test
    public void testVisitAnyRefConstant_withAllRefConstantSubtypes_allWorkCorrectly() {
        // Arrange
        FieldrefConstant fieldref = mock(FieldrefConstant.class);
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        InterfaceMethodrefConstant interfaceMethodref = mock(InterfaceMethodrefConstant.class);
        fieldref.u2classIndex = 1;
        methodref.u2classIndex = 2;
        interfaceMethodref.u2classIndex = 3;

        // Act
        marker.visitAnyRefConstant(clazz, fieldref);
        marker.visitAnyRefConstant(clazz, methodref);
        marker.visitAnyRefConstant(clazz, interfaceMethodref);

        // Assert - verify all three subtypes were processed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(fieldref).referencedClassAccept(eq(marker));
        verify(fieldref).referencedMemberAccept(eq(marker));

        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(methodref).referencedClassAccept(eq(marker));
        verify(methodref).referencedMemberAccept(eq(marker));

        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(interfaceMethodref).referencedClassAccept(eq(marker));
        verify(interfaceMethodref).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant handles same constant multiple times with different clazz instances.
     */
    @Test
    public void testVisitAnyRefConstant_sameConstantDifferentClazz_allProcessed() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(LibraryClass.class);
        refConstant.u2classIndex = 5;

        // Act
        marker.visitAnyRefConstant(clazz1, refConstant);
        marker.visitAnyRefConstant(clazz2, refConstant);
        marker.visitAnyRefConstant(clazz3, refConstant);

        // Assert
        verify(clazz1).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz3).constantPoolEntryAccept(eq(5), eq(marker));
        verify(refConstant, times(3)).referencedClassAccept(eq(marker));
        verify(refConstant, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant execution completes successfully.
     */
    @Test
    public void testVisitAnyRefConstant_completesSuccessfully() {
        // Arrange
        refConstant.u2classIndex = 1;
        boolean[] completed = {false};

        // Act
        marker.visitAnyRefConstant(clazz, refConstant);
        completed[0] = true;

        // Assert
        assertTrue(completed[0], "visitAnyRefConstant should complete successfully");
        verify(clazz).constantPoolEntryAccept(anyInt(), any());
        verify(refConstant).referencedClassAccept(any());
        verify(refConstant).referencedMemberAccept(any());
    }

    /**
     * Tests that visitAnyRefConstant performs all three operations even if the constant
     * has no actual referenced class or member (the methods will still be called).
     */
    @Test
    public void testVisitAnyRefConstant_withNoReferencedClassOrMember_stillCallsAllMethods() {
        // Arrange
        RefConstant emptyRefConstant = mock(RefConstant.class);
        emptyRefConstant.u2classIndex = 1;
        doNothing().when(emptyRefConstant).referencedClassAccept(any(ClassVisitor.class));
        doNothing().when(emptyRefConstant).referencedMemberAccept(any(MemberVisitor.class));

        // Act
        marker.visitAnyRefConstant(clazz, emptyRefConstant);

        // Assert - verify all methods were called
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(emptyRefConstant).referencedClassAccept(eq(marker));
        verify(emptyRefConstant).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant passes both parameters correctly to all method calls.
     */
    @Test
    public void testVisitAnyRefConstant_passesParametersCorrectly() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class);
        RefConstant specificConstant = mock(RefConstant.class);
        AccessMethodMarker specificMarker = new AccessMethodMarker();
        specificConstant.u2classIndex = 7;

        // Act
        specificMarker.visitAnyRefConstant(specificClazz, specificConstant);

        // Assert - verify clazz and marker are passed correctly
        verify(specificClazz).constantPoolEntryAccept(eq(7), same(specificMarker));
        verify(specificConstant).referencedClassAccept(same(specificMarker));
        verify(specificConstant).referencedMemberAccept(same(specificMarker));
    }

    /**
     * Tests that visitAnyRefConstant handles boundary values for class index.
     */
    @Test
    public void testVisitAnyRefConstant_withBoundaryClassIndices_accessesConstantPool() {
        // Arrange
        RefConstant constant0 = mock(RefConstant.class);
        RefConstant constant1 = mock(RefConstant.class);
        RefConstant constantMax = mock(RefConstant.class);
        constant0.u2classIndex = 0;
        constant1.u2classIndex = 1;
        constantMax.u2classIndex = 65535;

        // Act
        marker.visitAnyRefConstant(clazz, constant0);
        marker.visitAnyRefConstant(clazz, constant1);
        marker.visitAnyRefConstant(clazz, constantMax);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitAnyRefConstant is stateless between calls.
     */
    @Test
    public void testVisitAnyRefConstant_isStatelessBetweenCalls() {
        // Arrange
        RefConstant constant1 = mock(RefConstant.class);
        RefConstant constant2 = mock(RefConstant.class);
        RefConstant constant3 = mock(RefConstant.class);
        constant1.u2classIndex = 1;
        constant2.u2classIndex = 2;
        constant3.u2classIndex = 3;

        // Act - interleave calls
        marker.visitAnyRefConstant(clazz, constant1);
        marker.visitAnyRefConstant(clazz, constant2);
        marker.visitAnyRefConstant(clazz, constant3);
        marker.visitAnyRefConstant(clazz, constant1);

        // Assert - verify each call accessed the correct constant
        verify(clazz, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(3), eq(marker));
    }
}
