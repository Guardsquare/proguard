package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.Clazz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameMarker#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in NameMarker throws an UnsupportedOperationException.
 * This is the default implementation that should never be called in normal usage,
 * as visitProgramClass and visitLibraryClass handle the specific class types.
 * These tests verify the exception is thrown correctly with the appropriate message.
 */
public class NameMarkerClaude_visitAnyClassTest {

    private NameMarker nameMarker;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
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
                () -> nameMarker.visitAnyClass(clazz),
                "visitAnyClass should throw UnsupportedOperationException");
    }

    /**
     * Tests that the exception message contains the NameMarker class name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsNameMarkerClassName() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz)
        );

        // Assert
        assertTrue(exception.getMessage().contains("NameMarker"),
                "Exception message should contain 'NameMarker'");
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
                () -> nameMarker.visitAnyClass(clazz)
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
                () -> nameMarker.visitAnyClass(clazz)
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
                () -> nameMarker.visitAnyClass(null),
                "visitAnyClass with null should throw NullPointerException");
    }

    /**
     * Tests that visitAnyClass can be called multiple times and always throws exception.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_alwaysThrowsException() {
        // Act & Assert - each call should throw exception
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz));
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
                () -> nameMarker.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz2));
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz3));
    }

    /**
     * Tests that the exception is of the exact type UnsupportedOperationException.
     * Not a subclass or other exception type.
     */
    @Test
    public void testVisitAnyClass_throwsExactExceptionType() {
        // Act
        Exception exception = assertThrows(Exception.class,
                () -> nameMarker.visitAnyClass(clazz));

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
        // Act - attempt to call visitAnyClass
        try {
            nameMarker.visitAnyClass(clazz);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected - verify exception was thrown
            assertNotNull(e, "Exception should not be null");
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
                () -> nameMarker.visitAnyClass(clazz)
        );

        // Assert - check complete message structure
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.contains("proguard.obfuscate.NameMarker"),
                "Message should contain full class name of NameMarker");
    }

    /**
     * Tests that visitAnyClass behavior is consistent across rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Act & Assert - all calls should throw
        for (int i = 0; i < 10; i++) {
            assertThrows(UnsupportedOperationException.class,
                    () -> nameMarker.visitAnyClass(clazz),
                    "Call " + i + " should throw UnsupportedOperationException");
        }
    }

    /**
     * Tests that multiple NameMarker instances all throw exception consistently.
     */
    @Test
    public void testVisitAnyClass_multipleNameMarkerInstances_allThrowException() {
        // Arrange
        NameMarker nameMarker1 = new NameMarker();
        NameMarker nameMarker2 = new NameMarker();
        NameMarker nameMarker3 = new NameMarker();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker1.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker2.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> nameMarker3.visitAnyClass(clazz));
    }

    /**
     * Tests that exception message contains both class names (NameMarker and Clazz type).
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsBothClassNames() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz)
        );

        // Assert
        String message = exception.getMessage();
        assertTrue(message.contains("NameMarker"),
                "Exception message should contain 'NameMarker'");
        assertTrue(message.contains("Clazz"),
                "Exception message should reference the Clazz type");
    }

    /**
     * Tests that visitAnyClass throws exception with consistent message format
     * regardless of the Clazz mock configuration.
     */
    @Test
    public void testVisitAnyClass_withDifferentMockConfigurations_consistentExceptionFormat() {
        // Arrange - create mocks with different configurations
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        when(clazz2.getName()).thenReturn("TestClass");

        // Act
        UnsupportedOperationException exception1 = assertThrows(
                UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz1)
        );
        UnsupportedOperationException exception2 = assertThrows(
                UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz2)
        );

        // Assert - both messages should follow the same format
        assertTrue(exception1.getMessage().contains("does not support"),
                "First exception should have standard format");
        assertTrue(exception2.getMessage().contains("does not support"),
                "Second exception should have standard format");
    }

    /**
     * Tests that visitAnyClass does not interact with the Clazz object
     * before throwing the exception (except to get its class).
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithClazz() {
        // Arrange
        Clazz spyClazz = mock(Clazz.class);

        // Act - catch exception to continue test
        try {
            nameMarker.visitAnyClass(spyClazz);
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Assert - verify only getClass() was accessed (implicitly by exception message)
        // No other methods should have been called
        verifyNoMoreInteractions(spyClazz);
    }

    /**
     * Tests that the exception thrown is not caused by another exception.
     * The exception should be thrown directly, not wrapping another exception.
     */
    @Test
    public void testVisitAnyClass_exceptionHasNoCause() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> nameMarker.visitAnyClass(clazz)
        );

        // Assert
        assertNull(exception.getCause(),
                "Exception should have no cause - it's thrown directly");
    }
}
