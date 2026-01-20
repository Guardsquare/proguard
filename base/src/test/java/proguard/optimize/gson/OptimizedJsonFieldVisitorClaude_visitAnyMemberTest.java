package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the visitAnyMember method of {@link OptimizedJsonFieldVisitor}.
 * Tests the method: visitAnyMember.(Lproguard/classfile/Clazz;Lproguard/classfile/Member;)V
 *
 * The visitAnyMember method is a no-op implementation of the MemberVisitor interface,
 * meaning it performs no operations when called.
 */
public class OptimizedJsonFieldVisitorClaude_visitAnyMemberTest {

    private OptimizedJsonFieldVisitor visitor;
    private TestClassVisitor testClassVisitor;
    private TestMemberVisitor testMemberVisitor;

    @BeforeEach
    public void setUp() {
        testClassVisitor = new TestClassVisitor();
        testMemberVisitor = new TestMemberVisitor();
        visitor = new OptimizedJsonFieldVisitor(testClassVisitor, testMemberVisitor);
    }

    // =========================================================================
    // Tests for visitAnyMember.(Lproguard/classfile/Clazz;Lproguard/classfile/Member;)V
    // =========================================================================

    /**
     * Tests that visitAnyMember does nothing when called with valid Clazz and Member.
     * The method is a no-op implementation of the MemberVisitor interface.
     */
    @Test
    public void testVisitAnyMember_withValidParameters_doesNothing() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField member = new ProgramField();

        // Act
        visitor.visitAnyMember(clazz, member);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyMember_multipleCalls_noSideEffects() {
        // Arrange
        ProgramClass clazz1 = new ProgramClass();
        ProgramField member1 = new ProgramField();
        ProgramClass clazz2 = new ProgramClass();
        ProgramField member2 = new ProgramField();

        // Act
        visitor.visitAnyMember(clazz1, member1);
        visitor.visitAnyMember(clazz2, member2);
        visitor.visitAnyMember(clazz1, member2);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with null Clazz.
     * While not recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyMember_withNullClazz_doesNotThrow() {
        // Arrange
        ProgramField member = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyMember(null, member),
                "visitAnyMember should not throw exception with null Clazz");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with null Member.
     * While not recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyMember_withNullMember_doesNotThrow() {
        // Arrange
        ProgramClass clazz = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyMember(clazz, null),
                "visitAnyMember should not throw exception with null Member");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with both parameters null.
     */
    @Test
    public void testVisitAnyMember_withBothParametersNull_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyMember(null, null),
                "visitAnyMember should not throw exception with null parameters");
    }

    /**
     * Tests that visitAnyMember works correctly when called with a ProgramField.
     */
    @Test
    public void testVisitAnyMember_withProgramField_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        visitor.visitAnyMember(programClass, programField);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember works correctly when called with a ProgramMethod.
     */
    @Test
    public void testVisitAnyMember_withProgramMethod_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        visitor.visitAnyMember(programClass, programMethod);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember works correctly when called with a LibraryField.
     */
    @Test
    public void testVisitAnyMember_withLibraryField_doesNothing() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();

        // Act
        visitor.visitAnyMember(libraryClass, libraryField);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember works correctly when called with a LibraryMethod.
     */
    @Test
    public void testVisitAnyMember_withLibraryMethod_doesNothing() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act
        visitor.visitAnyMember(libraryClass, libraryMethod);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember does not invoke the delegate classVisitor.
     */
    @Test
    public void testVisitAnyMember_doesNotInvokeDelegateClassVisitor() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField member = new ProgramField();

        // Act
        visitor.visitAnyMember(clazz, member);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember does not invoke the delegate memberVisitor.
     */
    @Test
    public void testVisitAnyMember_doesNotInvokeDelegateMemberVisitor() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField member = new ProgramField();

        // Act
        visitor.visitAnyMember(clazz, member);

        // Assert
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember can be called repeatedly on the same Clazz and Member.
     */
    @Test
    public void testVisitAnyMember_repeatedCallsOnSameParameters_noSideEffects() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField member = new ProgramField();

        // Act
        visitor.visitAnyMember(clazz, member);
        visitor.visitAnyMember(clazz, member);
        visitor.visitAnyMember(clazz, member);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");
    }

    /**
     * Tests that visitAnyMember does not throw when called with null delegate visitors.
     */
    @Test
    public void testVisitAnyMember_withNullDelegates_doesNotThrow() {
        // Arrange
        OptimizedJsonFieldVisitor visitorWithNulls =
            new OptimizedJsonFieldVisitor(null, null);
        ProgramClass clazz = new ProgramClass();
        ProgramField member = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> visitorWithNulls.visitAnyMember(clazz, member),
                "visitAnyMember should not throw even with null delegates");
    }

    /**
     * Tests that visitAnyMember behaves consistently across different visitor instances.
     */
    @Test
    public void testVisitAnyMember_differentInstances_consistentBehavior() {
        // Arrange
        OptimizedJsonFieldVisitor visitor1 = new OptimizedJsonFieldVisitor(
            new TestClassVisitor(), new TestMemberVisitor());
        OptimizedJsonFieldVisitor visitor2 = new OptimizedJsonFieldVisitor(
            new TestClassVisitor(), new TestMemberVisitor());
        ProgramClass clazz = new ProgramClass();
        ProgramField member = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor1.visitAnyMember(clazz, member);
            visitor2.visitAnyMember(clazz, member);
        }, "Both visitors should handle visitAnyMember consistently");
    }

    /**
     * Tests that visitAnyMember is truly a no-op by verifying no state changes occur.
     */
    @Test
    public void testVisitAnyMember_isNoOp_verifyNoStateChange() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField member = new ProgramField();

        // Act
        visitor.visitAnyMember(clazz, member);

        // Assert
        assertNotNull(visitor, "Visitor should remain valid after visitAnyMember");
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked");
        assertFalse(testMemberVisitor.wasVisited, "MemberVisitor should not be invoked");

        // Verify we can still call other methods without issues
        assertDoesNotThrow(() -> visitor.visitAnyMember(new ProgramClass(), new ProgramField()),
                "Visitor should remain functional after visitAnyMember");
    }

    /**
     * Tests that visitAnyMember does not affect subsequent calls to visitProgramField.
     * This verifies that visitAnyMember truly does nothing and doesn't alter internal state.
     */
    @Test
    public void testVisitAnyMember_doesNotAffectVisitProgramField() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField field = new ProgramField();

        // Act
        visitor.visitAnyMember(clazz, field);
        visitor.visitProgramField(clazz, field);

        // Assert
        assertFalse(testClassVisitor.wasVisited, "ClassVisitor should not be invoked by visitAnyMember");
        assertTrue(testMemberVisitor.wasVisited, "MemberVisitor should be invoked by visitProgramField");
    }

    /**
     * Tests visitAnyMember with various combinations of ProgramClass and different member types.
     */
    @Test
    public void testVisitAnyMember_withProgramClassAndVariousMembers_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyMember(programClass, programField);
            visitor.visitAnyMember(programClass, programMethod);
        });

        assertFalse(testClassVisitor.wasVisited);
        assertFalse(testMemberVisitor.wasVisited);
    }

    /**
     * Tests visitAnyMember with various combinations of LibraryClass and different member types.
     */
    @Test
    public void testVisitAnyMember_withLibraryClassAndVariousMembers_doesNothing() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyMember(libraryClass, libraryField);
            visitor.visitAnyMember(libraryClass, libraryMethod);
        });

        assertFalse(testClassVisitor.wasVisited);
        assertFalse(testMemberVisitor.wasVisited);
    }

    /**
     * Tests visitAnyMember with mixed class and member types.
     */
    @Test
    public void testVisitAnyMember_withMixedTypes_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        ProgramField programField = new ProgramField();
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyMember(programClass, programField);
            visitor.visitAnyMember(libraryClass, libraryMethod);
            visitor.visitAnyMember(programClass, libraryMethod);
            visitor.visitAnyMember(libraryClass, programField);
        });

        assertFalse(testClassVisitor.wasVisited);
        assertFalse(testMemberVisitor.wasVisited);
    }

    // =========================================================================
    // Helper classes
    // =========================================================================

    /**
     * Test implementation of ClassVisitor for verifying behavior.
     */
    private static class TestClassVisitor implements ClassVisitor {
        boolean wasVisited = false;

        @Override
        public void visitAnyClass(Clazz clazz) {
            wasVisited = true;
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            wasVisited = true;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            wasVisited = true;
        }
    }

    /**
     * Test implementation of MemberVisitor for verifying behavior.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        boolean wasVisited = false;

        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            wasVisited = true;
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            wasVisited = true;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            wasVisited = true;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            wasVisited = true;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            wasVisited = true;
        }
    }
}
