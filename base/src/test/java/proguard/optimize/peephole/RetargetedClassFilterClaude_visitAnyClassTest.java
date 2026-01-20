package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.Clazz;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link RetargetedClassFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in RetargetedClassFilter throws an UnsupportedOperationException.
 * This is the default implementation that should never be called in normal usage,
 * as visitProgramClass and visitLibraryClass handle the specific class types.
 * These tests verify the exception is thrown correctly with the appropriate message.
 */
public class RetargetedClassFilterClaude_visitAnyClassTest {

    private RetargetedClassFilter retargetedClassFilter;
    private ClassVisitor retargetedClassVisitor;
    private ClassVisitor otherClassVisitor;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        retargetedClassVisitor = mock(ClassVisitor.class);
        otherClassVisitor = mock(ClassVisitor.class);
        retargetedClassFilter = new RetargetedClassFilter(retargetedClassVisitor, otherClassVisitor);
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
                () -> retargetedClassFilter.visitAnyClass(clazz),
                "visitAnyClass should throw UnsupportedOperationException");
    }

    /**
     * Tests that the exception message contains the RetargetedClassFilter class name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsFilterClassName() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz)
        );

        // Assert
        assertTrue(exception.getMessage().contains("RetargetedClassFilter"),
                "Exception message should contain 'RetargetedClassFilter'");
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
                () -> retargetedClassFilter.visitAnyClass(clazz)
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
                () -> retargetedClassFilter.visitAnyClass(clazz)
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
                () -> retargetedClassFilter.visitAnyClass(null),
                "visitAnyClass with null should throw NullPointerException");
    }

    /**
     * Tests that visitAnyClass can be called multiple times and always throws exception.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_alwaysThrowsException() {
        // Act & Assert - each call should throw exception
        assertThrows(UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz));
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
                () -> retargetedClassFilter.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz2));
        assertThrows(UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz3));
    }

    /**
     * Tests that visitAnyClass throws exception even with null ClassVisitors.
     * The exception is thrown before the visitors would be used.
     */
    @Test
    public void testVisitAnyClass_withNullClassVisitors_stillThrowsException() {
        // Arrange
        RetargetedClassFilter filterWithNullVisitors = new RetargetedClassFilter(null, null);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> filterWithNullVisitors.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass does not call any ClassVisitor.
     * The exception is thrown before any visitor logic is reached.
     */
    @Test
    public void testVisitAnyClass_doesNotUseClassVisitors() {
        // Arrange
        ClassVisitor mockRetargetedVisitor = mock(ClassVisitor.class);
        ClassVisitor mockOtherVisitor = mock(ClassVisitor.class);
        RetargetedClassFilter filter = new RetargetedClassFilter(mockRetargetedVisitor, mockOtherVisitor);

        // Act - catch exception to continue test
        try {
            filter.visitAnyClass(clazz);
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Assert - verify neither visitor was used
        verifyNoInteractions(mockRetargetedVisitor);
        verifyNoInteractions(mockOtherVisitor);
    }

    /**
     * Tests visitAnyClass on multiple RetargetedClassFilter instances.
     * Verifies that different instances behave consistently.
     */
    @Test
    public void testVisitAnyClass_multipleFilterInstances_allThrowException() {
        // Arrange
        RetargetedClassFilter filter1 = new RetargetedClassFilter(mock(ClassVisitor.class));
        RetargetedClassFilter filter2 = new RetargetedClassFilter(mock(ClassVisitor.class), mock(ClassVisitor.class));

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
                () -> retargetedClassFilter.visitAnyClass(clazz));

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
        ClassVisitor spyRetargetedVisitor = mock(ClassVisitor.class);
        ClassVisitor spyOtherVisitor = mock(ClassVisitor.class);
        RetargetedClassFilter filter = new RetargetedClassFilter(spyRetargetedVisitor, spyOtherVisitor);

        // Act - attempt to call visitAnyClass
        try {
            filter.visitAnyClass(clazz);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected - verify no side effects occurred
            verifyNoInteractions(spyRetargetedVisitor);
            verifyNoInteractions(spyOtherVisitor);
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
                () -> retargetedClassFilter.visitAnyClass(clazz)
        );

        // Assert - check complete message structure
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.contains("proguard.optimize.peephole.RetargetedClassFilter"),
                "Message should contain full class name of RetargetedClassFilter");
    }

    /**
     * Tests that visitAnyClass behavior is consistent across rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Act & Assert - all calls should throw
        for (int i = 0; i < 10; i++) {
            assertThrows(UnsupportedOperationException.class,
                    () -> retargetedClassFilter.visitAnyClass(clazz),
                    "Call " + i + " should throw UnsupportedOperationException");
        }
    }

    /**
     * Tests that a RetargetedClassFilter created with single parameter constructor
     * still throws UnsupportedOperationException in visitAnyClass.
     */
    @Test
    public void testVisitAnyClass_singleParameterConstructor_throwsException() {
        // Arrange
        RetargetedClassFilter filterWithSingleParam = new RetargetedClassFilter(retargetedClassVisitor);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> filterWithSingleParam.visitAnyClass(clazz));
    }

    /**
     * Tests that multiple different RetargetedClassFilter instances all throw
     * UnsupportedOperationException consistently.
     */
    @Test
    public void testVisitAnyClass_multipleFiltersWithDifferentVisitors_allThrowException() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        ClassVisitor visitor3 = mock(ClassVisitor.class);
        ClassVisitor visitor4 = mock(ClassVisitor.class);

        RetargetedClassFilter filter1 = new RetargetedClassFilter(visitor1);
        RetargetedClassFilter filter2 = new RetargetedClassFilter(visitor2, visitor3);
        RetargetedClassFilter filter3 = new RetargetedClassFilter(visitor4, null);

        // Act & Assert - all should throw
        assertThrows(UnsupportedOperationException.class,
                () -> filter1.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> filter2.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> filter3.visitAnyClass(clazz));

        // Verify none of the visitors were called
        verifyNoInteractions(visitor1, visitor2, visitor3, visitor4);
    }

    /**
     * Tests that the exception message includes the package name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageIncludesPackage() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz)
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

        RetargetedClassFilter filter = new RetargetedClassFilter(throwingVisitor);

        // Act
        Exception exception = assertThrows(Exception.class,
                () -> filter.visitAnyClass(clazz));

        // Assert - should be UnsupportedOperationException, not the visitor's RuntimeException
        assertEquals(UnsupportedOperationException.class, exception.getClass(),
                "Should throw UnsupportedOperationException before visitor is called");
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
                () -> retargetedClassFilter.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> retargetedClassFilter.visitAnyClass(clazz2));
    }
}
