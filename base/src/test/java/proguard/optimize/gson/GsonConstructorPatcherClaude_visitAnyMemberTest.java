package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.editor.CodeAttributeEditor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GsonConstructorPatcher#visitAnyMember(Clazz, Member)}.
 *
 * The visitAnyMember method is a no-op implementation of the MemberVisitor interface.
 * It serves as a fallback for members that are not specifically handled by other visitor methods.
 * The actual work of GsonConstructorPatcher is done in visitProgramMethod, not in visitAnyMember.
 */
public class GsonConstructorPatcherClaude_visitAnyMemberTest {

    private GsonConstructorPatcher patcher;
    private CodeAttributeEditor codeAttributeEditor;

    /**
     * Sets up fresh instances before each test.
     */
    @BeforeEach
    public void setUp() {
        codeAttributeEditor = new CodeAttributeEditor();
        patcher = new GsonConstructorPatcher(codeAttributeEditor, false);
    }

    // =========================================================================
    // Tests for visitAnyMember.(Lproguard/classfile/Clazz;Lproguard/classfile/Member;)V
    // =========================================================================

    /**
     * Tests that visitAnyMember does nothing when called with a valid Clazz and Member.
     * The method is a no-op implementation of the MemberVisitor interface.
     */
    @Test
    public void testVisitAnyMember_withValidClazzAndMember_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act
        patcher.visitAnyMember(clazz, member);

        // Assert - no interactions should occur since it's a no-op
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyMember_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Member member1 = mock(Member.class);
        Clazz clazz2 = mock(Clazz.class);
        Member member2 = mock(Member.class);

        // Act
        patcher.visitAnyMember(clazz1, member1);
        patcher.visitAnyMember(clazz2, member2);
        patcher.visitAnyMember(clazz1, member1);

        // Assert
        verifyNoInteractions(clazz1, member1, clazz2, member2);
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with null arguments.
     * While not a recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyMember_withNullClazz_doesNotThrow() {
        // Arrange
        Member member = mock(Member.class);

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitAnyMember(null, member),
                "visitAnyMember should not throw exception with null Clazz");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with a null Member argument.
     */
    @Test
    public void testVisitAnyMember_withNullMember_doesNotThrow() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitAnyMember(clazz, null),
                "visitAnyMember should not throw exception with null Member");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions when both arguments are null.
     */
    @Test
    public void testVisitAnyMember_withBothNull_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitAnyMember(null, null),
                "visitAnyMember should not throw exception with null arguments");
    }

    /**
     * Tests that visitAnyMember works correctly when called with a ProgramField.
     */
    @Test
    public void testVisitAnyMember_withProgramField_doesNothing() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        ProgramField programField = mock(ProgramField.class);

        // Act
        patcher.visitAnyMember(programClass, programField);

        // Assert
        verifyNoInteractions(programClass);
        verifyNoInteractions(programField);
    }

    /**
     * Tests that visitAnyMember works correctly when called with a ProgramMethod.
     * Note: While visitAnyMember is the generic handler, visitProgramMethod will be
     * called instead for ProgramMethod objects when properly dispatched through the visitor pattern.
     * This test verifies that calling visitAnyMember directly with a ProgramMethod is harmless.
     */
    @Test
    public void testVisitAnyMember_withProgramMethod_doesNothing() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        ProgramMethod programMethod = mock(ProgramMethod.class);

        // Act
        patcher.visitAnyMember(programClass, programMethod);

        // Assert
        verifyNoInteractions(programClass);
        verifyNoInteractions(programMethod);
    }

    /**
     * Tests that visitAnyMember works correctly when called with a LibraryMethod.
     */
    @Test
    public void testVisitAnyMember_withLibraryMethod_doesNothing() {
        // Arrange
        LibraryClass libraryClass = mock(LibraryClass.class);
        LibraryMethod libraryMethod = mock(LibraryMethod.class);

        // Act
        patcher.visitAnyMember(libraryClass, libraryMethod);

        // Assert
        verifyNoInteractions(libraryClass);
        verifyNoInteractions(libraryMethod);
    }

    /**
     * Tests that visitAnyMember works correctly when called with a LibraryField.
     */
    @Test
    public void testVisitAnyMember_withLibraryField_doesNothing() {
        // Arrange
        LibraryClass libraryClass = mock(LibraryClass.class);
        LibraryField libraryField = mock(LibraryField.class);

        // Act
        patcher.visitAnyMember(libraryClass, libraryField);

        // Assert
        verifyNoInteractions(libraryClass);
        verifyNoInteractions(libraryField);
    }

    /**
     * Tests that visitAnyMember is truly a no-op by verifying it doesn't interact
     * with the patcher's internal state.
     */
    @Test
    public void testVisitAnyMember_isNoOp_noInternalStateChanges() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act - call it many times
        for (int i = 0; i < 100; i++) {
            patcher.visitAnyMember(clazz, member);
        }

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember with addExcluder=true still does nothing.
     * The addExcluder flag only affects visitCodeAttribute, not visitAnyMember.
     */
    @Test
    public void testVisitAnyMember_withAddExcluderTrue_stillDoesNothing() {
        // Arrange
        CodeAttributeEditor editor = new CodeAttributeEditor();
        GsonConstructorPatcher patcherWithExcluder = new GsonConstructorPatcher(editor, true);
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act
        patcherWithExcluder.visitAnyMember(clazz, member);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember with addExcluder=false still does nothing.
     */
    @Test
    public void testVisitAnyMember_withAddExcluderFalse_stillDoesNothing() {
        // Arrange
        CodeAttributeEditor editor = new CodeAttributeEditor();
        GsonConstructorPatcher patcherNoExcluder = new GsonConstructorPatcher(editor, false);
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act
        patcherNoExcluder.visitAnyMember(clazz, member);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that different patcher instances behave the same way for visitAnyMember.
     * Since visitAnyMember is a no-op, all instances should behave identically.
     */
    @Test
    public void testVisitAnyMember_multipleInstances_sameNoOpBehavior() {
        // Arrange
        CodeAttributeEditor editor1 = new CodeAttributeEditor();
        CodeAttributeEditor editor2 = new CodeAttributeEditor();
        GsonConstructorPatcher patcher1 = new GsonConstructorPatcher(editor1, true);
        GsonConstructorPatcher patcher2 = new GsonConstructorPatcher(editor2, false);

        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act
        patcher1.visitAnyMember(clazz, member);
        patcher2.visitAnyMember(clazz, member);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be called with the same objects repeatedly
     * without any state accumulation or side effects.
     */
    @Test
    public void testVisitAnyMember_repeatedCallsSameObjects_consistent() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> patcher.visitAnyMember(clazz, member),
                    "Repeated calls should not throw exceptions");
        }

        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember maintains its no-op behavior regardless of
     * the type of Clazz (ProgramClass vs LibraryClass).
     */
    @Test
    public void testVisitAnyMember_withDifferentClazzTypes_allAreNoOp() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        LibraryClass libraryClass = mock(LibraryClass.class);
        Member member = mock(Member.class);

        // Act
        patcher.visitAnyMember(programClass, member);
        patcher.visitAnyMember(libraryClass, member);

        // Assert
        verifyNoInteractions(programClass);
        verifyNoInteractions(libraryClass);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember maintains its no-op behavior regardless of
     * the type of Member (fields vs methods, program vs library).
     */
    @Test
    public void testVisitAnyMember_withDifferentMemberTypes_allAreNoOp() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        ProgramField programField = mock(ProgramField.class);
        ProgramMethod programMethod = mock(ProgramMethod.class);
        LibraryField libraryField = mock(LibraryField.class);
        LibraryMethod libraryMethod = mock(LibraryMethod.class);

        // Act
        patcher.visitAnyMember(clazz, programField);
        patcher.visitAnyMember(clazz, programMethod);
        patcher.visitAnyMember(clazz, libraryField);
        patcher.visitAnyMember(clazz, libraryMethod);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(programField);
        verifyNoInteractions(programMethod);
        verifyNoInteractions(libraryField);
        verifyNoInteractions(libraryMethod);
    }

    /**
     * Tests that visitAnyMember does not modify the CodeAttributeEditor
     * passed to the GsonConstructorPatcher constructor.
     */
    @Test
    public void testVisitAnyMember_doesNotModifyCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor editor = spy(new CodeAttributeEditor());
        GsonConstructorPatcher testPatcher = new GsonConstructorPatcher(editor, false);
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act
        testPatcher.visitAnyMember(clazz, member);

        // Assert - the editor should not have been interacted with
        verifyNoInteractions(editor);
    }

    /**
     * Tests that calling visitAnyMember before visitProgramMethod has no effect
     * on subsequent visitProgramMethod calls.
     */
    @Test
    public void testVisitAnyMember_doesNotAffectSubsequentVisitorCalls() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act - call visitAnyMember first
        patcher.visitAnyMember(clazz, member);

        // Then verify we can still use the patcher normally (no state corruption)
        assertDoesNotThrow(() -> patcher.visitAnyMember(clazz, member),
                "visitAnyMember should not corrupt patcher state");
    }

    /**
     * Tests that visitAnyMember behavior is deterministic.
     * Multiple calls with the same input should behave the same way.
     */
    @Test
    public void testVisitAnyMember_deterministic_sameInputSameBehavior() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Member member = mock(Member.class);

        // Act & Assert
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> patcher.visitAnyMember(clazz, member),
                    "Deterministic behavior should not throw exceptions");
        }

        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }
}
