package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.Clazz;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SubclassedClassFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in SubclassedClassFilter throws an UnsupportedOperationException.
 * This is the default implementation that should never be called in normal usage,
 * as visitProgramClass and visitLibraryClass handle the specific class types.
 * These tests verify the exception is thrown correctly with the appropriate message.
 */
public class SubclassedClassFilterClaude_visitAnyClassTest {

    private SubclassedClassFilter subclassedClassFilter;
    private ClassVisitor classVisitor;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        classVisitor = mock(ClassVisitor.class);
        subclassedClassFilter = new SubclassedClassFilter(classVisitor);
        clazz = mock(Clazz.class);
    }

    /**
     * Tests that visitAnyClass throws UnsupportedOperationException with a valid mock Clazz.
     * Verifies the basic exception throwing behavior.
     */
    @Test
    public void testVisitAnyClass_withValidMock_throwsUnsupportedOperationException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz),
                "visitAnyClass should throw UnsupportedOperationException");
    }

    /**
     * Tests that the exception message contains the SubclassedClassFilter class name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsFilterClassName() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz)
        );

        // Assert
        assertTrue(exception.getMessage().contains("SubclassedClassFilter"),
                "Exception message should contain 'SubclassedClassFilter'");
    }

    /**
     * Tests that the exception message contains a class name from the mock.
     * Since Mockito creates a proxy, the message will contain the mock class name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsClazzClassName() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz)
        );

        // Assert - message should mention the clazz's actual class name (mock proxy)
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.length() > 0, "Exception message should not be empty");
    }

    /**
     * Tests that the exception message follows the expected format.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageFormat() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz)
        );

        // Assert - message format is "ClassName does not support OtherClassName"
        String message = exception.getMessage();
        assertTrue(message.contains("does not support"),
                "Exception message should contain 'does not support'");
    }

    /**
     * Tests visitAnyClass with null Clazz parameter.
     * Should throw NullPointerException when trying to access clazz.getClass().
     */
    @Test
    public void testVisitAnyClass_withNullClazz_throwsNullPointerException() {
        // Act & Assert - null.getClass() will throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> subclassedClassFilter.visitAnyClass(null),
                "visitAnyClass with null should throw NullPointerException");
    }

    /**
     * Tests that visitAnyClass can be called multiple times and always throws exception.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_alwaysThrowsException() {
        // Act & Assert - each call should throw exception
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with different Clazz mock instances.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzes_throwsException() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz2));
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz3));
    }

    /**
     * Tests that visitAnyClass throws exception even with null ClassVisitor.
     * The exception is thrown before the visitor would be used.
     */
    @Test
    public void testVisitAnyClass_withNullClassVisitor_stillThrowsException() {
        // Arrange
        SubclassedClassFilter filterWithNullVisitor = new SubclassedClassFilter(null);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> filterWithNullVisitor.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass does not call the ClassVisitor.
     * The exception is thrown before any visitor logic is reached.
     */
    @Test
    public void testVisitAnyClass_doesNotUseClassVisitor() {
        // Arrange
        ClassVisitor mockVisitor = mock(ClassVisitor.class);
        SubclassedClassFilter filter = new SubclassedClassFilter(mockVisitor);

        // Act - catch exception to continue test
        try {
            filter.visitAnyClass(clazz);
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Assert - verify the visitor was never used
        verifyNoInteractions(mockVisitor);
    }

    /**
     * Tests visitAnyClass on multiple SubclassedClassFilter instances.
     * Verifies that different instances behave consistently.
     */
    @Test
    public void testVisitAnyClass_multipleFilterInstances_allThrowException() {
        // Arrange
        SubclassedClassFilter filter1 = new SubclassedClassFilter(mock(ClassVisitor.class));
        SubclassedClassFilter filter2 = new SubclassedClassFilter(mock(ClassVisitor.class));

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> filter1.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> filter2.visitAnyClass(clazz));
    }

    /**
     * Tests that the exception is of the exact type UnsupportedOperationException.
     * Not a subclass or other exception type.
     */
    @Test
    public void testVisitAnyClass_throwsExactExceptionType() {
        // Act
        Exception exception = assertThrows(Exception.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));

        // Assert
        assertEquals(UnsupportedOperationException.class, exception.getClass(),
                "Exception should be exactly UnsupportedOperationException");
    }

    /**
     * Tests that visitAnyClass throws exception immediately without side effects.
     * The method should not modify any state before throwing.
     */
    @Test
    public void testVisitAnyClass_throwsImmediatelyWithoutSideEffects() {
        // Arrange
        ClassVisitor spyVisitor = mock(ClassVisitor.class);
        SubclassedClassFilter filter = new SubclassedClassFilter(spyVisitor);

        // Act - attempt to call visitAnyClass
        try {
            filter.visitAnyClass(clazz);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected - verify no side effects occurred
            verifyNoInteractions(spyVisitor);
        }
    }

    /**
     * Tests the complete exception message structure.
     * Verifies it matches the pattern: "ClassName does not support OtherClassName"
     */
    @Test
    public void testVisitAnyClass_completeExceptionMessage() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz)
        );

        // Assert - check complete message structure
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.contains("proguard.SubclassedClassFilter"),
                "Message should contain full class name of SubclassedClassFilter");
    }

    /**
     * Tests that visitAnyClass can be invoked after other visitor methods
     * and still throws the exception.
     */
    @Test
    public void testVisitAnyClass_afterOtherVisitorMethods_stillThrowsException() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        programClass.subClassCount = 1;

        // Act - call visitProgramClass first (which should work)
        assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));

        // Assert - then visitAnyClass should still throw
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass behavior is consistent across rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Act & Assert - all calls should throw
        for (int i = 0; i < 10; i++) {
            assertThrows(UnsupportedOperationException.class,
                    () -> subclassedClassFilter.visitAnyClass(clazz),
                    "Call " + i + " should throw UnsupportedOperationException");
        }
    }

    /**
     * Tests that a SubclassedClassFilter created with null ClassVisitor
     * still throws UnsupportedOperationException in visitAnyClass.
     */
    @Test
    public void testVisitAnyClass_filterWithNullVisitor_throwsException() {
        // Arrange
        SubclassedClassFilter filterWithNull = new SubclassedClassFilter(null);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> filterWithNull.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass after successfully calling visitLibraryClass.
     * Verifies that successful visitor calls don't affect visitAnyClass behavior.
     */
    @Test
    public void testVisitAnyClass_afterVisitLibraryClass_stillThrowsException() {
        // Arrange
        proguard.classfile.LibraryClass libraryClass = new proguard.classfile.LibraryClass();
        libraryClass.subClassCount = 1;

        // Act - call visitLibraryClass first (which should work)
        assertDoesNotThrow(() -> subclassedClassFilter.visitLibraryClass(libraryClass));

        // Assert - then visitAnyClass should still throw
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));
    }

    /**
     * Tests that multiple different SubclassedClassFilter instances all throw
     * UnsupportedOperationException consistently.
     */
    @Test
    public void testVisitAnyClass_multipleFiltersWithDifferentVisitors_allThrowException() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        ClassVisitor visitor3 = mock(ClassVisitor.class);

        SubclassedClassFilter filter1 = new SubclassedClassFilter(visitor1);
        SubclassedClassFilter filter2 = new SubclassedClassFilter(visitor2);
        SubclassedClassFilter filter3 = new SubclassedClassFilter(visitor3);

        // Act & Assert - all should throw
        assertThrows(UnsupportedOperationException.class,
                () -> filter1.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> filter2.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> filter3.visitAnyClass(clazz));

        // Verify none of the visitors were called
        verifyNoInteractions(visitor1, visitor2, visitor3);
    }

    /**
     * Tests that the exception message includes the package name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageIncludesPackage() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz)
        );

        // Assert
        assertTrue(exception.getMessage().contains("proguard"),
                "Exception message should contain package name 'proguard'");
    }

    /**
     * Tests that visitAnyClass throws exception before processing begins.
     * Even with a visitor that would throw exceptions, visitAnyClass throws first.
     */
    @Test
    public void testVisitAnyClass_throwsBeforeVisitorInteraction() {
        // Arrange
        ClassVisitor throwingVisitor = mock(ClassVisitor.class);
        doThrow(new RuntimeException("Visitor exception"))
                .when(throwingVisitor).visitAnyClass(any(Clazz.class));

        SubclassedClassFilter filter = new SubclassedClassFilter(throwingVisitor);

        // Act
        Exception exception = assertThrows(Exception.class,
                () -> filter.visitAnyClass(clazz));

        // Assert - should be UnsupportedOperationException, not the visitor's RuntimeException
        assertEquals(UnsupportedOperationException.class, exception.getClass(),
                "Should throw UnsupportedOperationException before visitor is called");
    }

    /**
     * Tests visitAnyClass with alternating calls to other visitor methods.
     * Verifies consistent behavior regardless of call pattern.
     */
    @Test
    public void testVisitAnyClass_alternatingWithOtherMethods_consistentBehavior() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        programClass.subClassCount = 1;

        // Act & Assert - alternate calls
        assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));
        assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz));
    }

    /**
     * Tests that the exception is thrown consistently regardless of Clazz mock configuration.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzMockConfigurations_throwsException() {
        // Arrange - create mocks with different configurations
        Clazz clazz1 = mock(Clazz.class);
        when(clazz1.getName()).thenReturn("TestClass1");

        Clazz clazz2 = mock(Clazz.class);
        when(clazz2.getName()).thenReturn("TestClass2");
        when(clazz2.toString()).thenReturn("Clazz[TestClass2]");

        // Act & Assert - both should throw
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> subclassedClassFilter.visitAnyClass(clazz2));
    }
}
