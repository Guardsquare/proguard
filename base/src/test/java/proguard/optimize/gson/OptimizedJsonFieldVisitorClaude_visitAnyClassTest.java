package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the visitAnyClass method of {@link OptimizedJsonFieldVisitor}.
 * Tests the method: visitAnyClass.(Lproguard/classfile/Clazz;)V
 *
 * The visitAnyClass method is a no-op implementation of the ClassVisitor interface,
 * meaning it performs no operations when called.
 */
public class OptimizedJsonFieldVisitorClaude_visitAnyClassTest {

    private OptimizedJsonFieldVisitor visitor;
    private ClassVisitor mockClassVisitor;
    private MemberVisitor mockMemberVisitor;

    @BeforeEach
    public void setUp() {
        mockClassVisitor = mock(ClassVisitor.class);
        mockMemberVisitor = mock(MemberVisitor.class);
        visitor = new OptimizedJsonFieldVisitor(mockClassVisitor, mockMemberVisitor);
    }

    // =========================================================================
    // Tests for visitAnyClass.(Lproguard/classfile/Clazz;)V
    // =========================================================================

    /**
     * Tests that visitAnyClass does nothing when called with a valid Clazz.
     * The method is a no-op implementation of the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_withValidClazz_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        visitor.visitAnyClass(clazz);

        // Assert
        // The method should not interact with the clazz or the delegate visitors
        verifyNoInteractions(clazz);
        verifyNoInteractions(mockClassVisitor);
        verifyNoInteractions(mockMemberVisitor);
    }

    /**
     * Tests that visitAnyClass can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyClass_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        visitor.visitAnyClass(clazz1);
        visitor.visitAnyClass(clazz2);
        visitor.visitAnyClass(clazz3);

        // Assert
        verifyNoInteractions(clazz1, clazz2, clazz3);
        verifyNoInteractions(mockClassVisitor);
        verifyNoInteractions(mockMemberVisitor);
    }

    /**
     * Tests that visitAnyClass does not throw exceptions with a null argument.
     * While not a recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyClass(null),
                "visitAnyClass should not throw exception with null argument");
    }

    /**
     * Tests that visitAnyClass works correctly when called on a LibraryClass.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNothing() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act
        visitor.visitAnyClass(libraryClass);

        // Assert
        // Should not delegate to classVisitor or memberVisitor
        verifyNoInteractions(mockClassVisitor);
        verifyNoInteractions(mockMemberVisitor);
    }

    /**
     * Tests that visitAnyClass works correctly when called on a ProgramClass.
     * Note: visitAnyClass is still a no-op even for ProgramClass.
     * The actual logic for ProgramClass is in visitProgramClass method.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act
        visitor.visitAnyClass(programClass);

        // Assert
        // Should not delegate to classVisitor or memberVisitor
        verifyNoInteractions(mockClassVisitor);
        verifyNoInteractions(mockMemberVisitor);
    }

    /**
     * Tests that visitAnyClass does not invoke the delegate classVisitor.
     */
    @Test
    public void testVisitAnyClass_doesNotInvokeDelegateClassVisitor() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        visitor.visitAnyClass(clazz);

        // Assert
        verifyNoInteractions(mockClassVisitor);
    }

    /**
     * Tests that visitAnyClass does not invoke the delegate memberVisitor.
     */
    @Test
    public void testVisitAnyClass_doesNotInvokeDelegateMemberVisitor() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        visitor.visitAnyClass(clazz);

        // Assert
        verifyNoInteractions(mockMemberVisitor);
    }

    /**
     * Tests that visitAnyClass can be called repeatedly on the same Clazz instance.
     */
    @Test
    public void testVisitAnyClass_repeatedCallsOnSameClazz_noSideEffects() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        visitor.visitAnyClass(clazz);
        visitor.visitAnyClass(clazz);
        visitor.visitAnyClass(clazz);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(mockClassVisitor);
        verifyNoInteractions(mockMemberVisitor);
    }

    /**
     * Tests that visitAnyClass does not throw when called with null delegate visitors.
     */
    @Test
    public void testVisitAnyClass_withNullDelegates_doesNotThrow() {
        // Arrange
        OptimizedJsonFieldVisitor visitorWithNulls =
            new OptimizedJsonFieldVisitor(null, null);
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> visitorWithNulls.visitAnyClass(clazz),
                "visitAnyClass should not throw even with null delegates");
    }

    /**
     * Tests that visitAnyClass behaves consistently across different visitor instances.
     */
    @Test
    public void testVisitAnyClass_differentInstances_consistentBehavior() {
        // Arrange
        OptimizedJsonFieldVisitor visitor1 = new OptimizedJsonFieldVisitor(
            mock(ClassVisitor.class), mock(MemberVisitor.class));
        OptimizedJsonFieldVisitor visitor2 = new OptimizedJsonFieldVisitor(
            mock(ClassVisitor.class), mock(MemberVisitor.class));
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor1.visitAnyClass(clazz);
            visitor2.visitAnyClass(clazz);
        }, "Both visitors should handle visitAnyClass consistently");
    }

    /**
     * Tests that visitAnyClass is truly a no-op by verifying no state changes occur.
     */
    @Test
    public void testVisitAnyClass_isNoOp_verifyNoStateChange() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        visitor.visitAnyClass(clazz);

        // Assert
        // Verify the visitor itself hasn't changed (it should still be usable)
        assertNotNull(visitor, "Visitor should remain valid after visitAnyClass");

        // Verify we can still call other methods without issues
        assertDoesNotThrow(() -> visitor.visitAnyClass(mock(Clazz.class)),
                "Visitor should remain functional after visitAnyClass");
    }
}
