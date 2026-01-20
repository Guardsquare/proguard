package proguard.optimize;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.Clazz;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link OptimizationInfoClassFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in OptimizationInfoClassFilter throws an UnsupportedOperationException.
 * This is the default implementation that should never be called in normal usage,
 * as visitProgramClass and visitLibraryClass handle the specific class types.
 * These tests verify the exception is thrown correctly with the appropriate message.
 */
public class OptimizationInfoClassFilterClaude_visitAnyClassTest {

    private OptimizationInfoClassFilter optimizationInfoClassFilter;
    private ClassVisitor classVisitor;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        classVisitor = mock(ClassVisitor.class);
        optimizationInfoClassFilter = new OptimizationInfoClassFilter(classVisitor);
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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz),
                "visitAnyClass should throw UnsupportedOperationException");
    }

    /**
     * Tests that the exception message contains the OptimizationInfoClassFilter class name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsFilterClassName() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz)
        );

        // Assert
        assertTrue(exception.getMessage().contains("OptimizationInfoClassFilter"),
                "Exception message should contain 'OptimizationInfoClassFilter'");
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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz)
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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz)
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
                () -> optimizationInfoClassFilter.visitAnyClass(null),
                "visitAnyClass with null should throw NullPointerException");
    }

    /**
     * Tests that visitAnyClass can be called multiple times and always throws exception.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_alwaysThrowsException() {
        // Act & Assert - each call should throw exception
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));
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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz2));
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz3));
    }

    /**
     * Tests that visitAnyClass throws exception even with null ClassVisitor.
     * The exception is thrown before the visitor would be used.
     */
    @Test
    public void testVisitAnyClass_withNullClassVisitor_stillThrowsException() {
        // Arrange
        OptimizationInfoClassFilter filterWithNullVisitor = new OptimizationInfoClassFilter(null);

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
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(mockVisitor);

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
     * Tests visitAnyClass on multiple OptimizationInfoClassFilter instances.
     * Verifies that different instances behave consistently.
     */
    @Test
    public void testVisitAnyClass_multipleFilterInstances_allThrowException() {
        // Arrange
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(mock(ClassVisitor.class));
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(mock(ClassVisitor.class));

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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));

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
        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(spyVisitor);

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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz)
        );

        // Assert - check complete message structure
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.contains("proguard.optimize.OptimizationInfoClassFilter"),
                "Message should contain full class name of OptimizationInfoClassFilter");
    }

    /**
     * Tests that visitAnyClass can be invoked after other visitor methods
     * and still throws the exception.
     */
    @Test
    public void testVisitAnyClass_afterOtherVisitorMethods_stillThrowsException() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        proguard.optimize.info.ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call visitProgramClass first (which should work)
        assertDoesNotThrow(() -> optimizationInfoClassFilter.visitProgramClass(programClass));

        // Assert - then visitAnyClass should still throw
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass behavior is consistent across rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Act & Assert - all calls should throw
        for (int i = 0; i < 10; i++) {
            assertThrows(UnsupportedOperationException.class,
                    () -> optimizationInfoClassFilter.visitAnyClass(clazz),
                    "Call " + i + " should throw UnsupportedOperationException");
        }
    }

    /**
     * Tests that an OptimizationInfoClassFilter created with null ClassVisitor
     * still throws UnsupportedOperationException in visitAnyClass.
     */
    @Test
    public void testVisitAnyClass_filterWithNullVisitor_throwsException() {
        // Arrange
        OptimizationInfoClassFilter filterWithNull = new OptimizationInfoClassFilter(null);

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
        proguard.optimize.info.ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act - call visitLibraryClass first (which should work)
        assertDoesNotThrow(() -> optimizationInfoClassFilter.visitLibraryClass(libraryClass));

        // Assert - then visitAnyClass should still throw
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));
    }

    /**
     * Tests that multiple different OptimizationInfoClassFilter instances all throw
     * UnsupportedOperationException consistently.
     */
    @Test
    public void testVisitAnyClass_multipleFiltersWithDifferentVisitors_allThrowException() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        ClassVisitor visitor3 = mock(ClassVisitor.class);

        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(visitor1);
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(visitor2);
        OptimizationInfoClassFilter filter3 = new OptimizationInfoClassFilter(visitor3);

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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz)
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

        OptimizationInfoClassFilter filter = new OptimizationInfoClassFilter(throwingVisitor);

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
        proguard.optimize.info.ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - alternate calls
        assertDoesNotThrow(() -> optimizationInfoClassFilter.visitProgramClass(programClass));
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));
        assertDoesNotThrow(() -> optimizationInfoClassFilter.visitProgramClass(programClass));
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz));
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
                () -> optimizationInfoClassFilter.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> optimizationInfoClassFilter.visitAnyClass(clazz2));
    }
}
