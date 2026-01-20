package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassMarker#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in WrapperClassMarker is an empty implementation (no-op).
 * This is the default implementation from the ClassVisitor interface that does nothing.
 * The actual logic is in visitProgramClass. These tests verify that calling visitAnyClass
 * has no side effects and completes successfully.
 */
public class WrapperClassMarkerClaude_visitAnyClassTest {

    private WrapperClassMarker wrapperClassMarker;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        wrapperClassMarker = new WrapperClassMarker();
        clazz = mock(Clazz.class);
    }

    /**
     * Tests that visitAnyClass completes successfully with a valid mock Clazz.
     * Since it's a no-op, it should simply return without doing anything.
     */
    @Test
    public void testVisitAnyClass_withValidMock_completesSuccessfully() {
        // Act & Assert - should complete without throwing
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz),
                "visitAnyClass should complete successfully");
    }

    /**
     * Tests that visitAnyClass with null does not throw NullPointerException.
     * Since the method body is empty, it should not attempt to access the parameter.
     */
    @Test
    public void testVisitAnyClass_withNull_completesSuccessfully() {
        // Act & Assert - should not throw even with null since it's a no-op
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(null),
                "visitAnyClass should complete successfully even with null");
    }

    /**
     * Tests that visitAnyClass can be called multiple times without issues.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_completesSuccessfully() {
        // Act & Assert - each call should complete successfully
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with different Clazz mock instances.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzes_completesSuccessfully() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz1));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz2));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz3));
    }

    /**
     * Tests that visitAnyClass does not interact with the Clazz parameter.
     * Since it's a no-op, no methods should be called on the clazz.
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithClazz() {
        // Arrange
        Clazz spyClazz = mock(Clazz.class);

        // Act
        wrapperClassMarker.visitAnyClass(spyClazz);

        // Assert - verify no methods were called on the clazz
        verifyNoInteractions(spyClazz);
    }

    /**
     * Tests visitAnyClass on multiple WrapperClassMarker instances.
     * Verifies that different instances behave consistently.
     */
    @Test
    public void testVisitAnyClass_multipleMarkerInstances_allCompleteSuccessfully() {
        // Arrange
        WrapperClassMarker marker1 = new WrapperClassMarker();
        WrapperClassMarker marker2 = new WrapperClassMarker();

        // Act & Assert
        assertDoesNotThrow(() -> marker1.visitAnyClass(clazz));
        assertDoesNotThrow(() -> marker2.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass can be called after visitProgramClass
     * and still completes successfully.
     */
    @Test
    public void testVisitAnyClass_afterVisitProgramClass_completesSuccessfully() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.subClassCount = 1;

        // Act - call visitProgramClass first
        assertDoesNotThrow(() -> wrapperClassMarker.visitProgramClass(programClass));

        // Assert - then visitAnyClass should still complete successfully
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass completes successfully with rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Act & Assert - all calls should complete successfully
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz),
                    "Call " + i + " should complete successfully");
        }
    }

    /**
     * Tests visitAnyClass with a ProgramClass instance.
     * Even though ProgramClass is a specific subtype, visitAnyClass should handle it.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_completesSuccessfully() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(programClass));
    }

    /**
     * Tests visitAnyClass with a LibraryClass instance.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_completesSuccessfully() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(libraryClass));
    }

    /**
     * Tests that visitAnyClass behavior is consistent across alternating calls
     * with other visitor methods.
     */
    @Test
    public void testVisitAnyClass_alternatingWithOtherMethods_consistentBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.subClassCount = 1;

        // Act & Assert - alternate calls
        assertDoesNotThrow(() -> wrapperClassMarker.visitProgramClass(programClass));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
        assertDoesNotThrow(() -> wrapperClassMarker.visitProgramClass(programClass));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass with different Clazz mock configurations
     * completes successfully regardless of configuration.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzMockConfigurations_completesSuccessfully() {
        // Arrange - create mocks with different configurations
        Clazz clazz1 = mock(Clazz.class);
        when(clazz1.getName()).thenReturn("TestClass1");

        Clazz clazz2 = mock(Clazz.class);
        when(clazz2.getName()).thenReturn("TestClass2");
        when(clazz2.toString()).thenReturn("Clazz[TestClass2]");

        // Act & Assert - both should complete successfully
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz1));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz2));
    }

    /**
     * Tests that multiple different WrapperClassMarker instances all complete
     * visitAnyClass successfully and consistently.
     */
    @Test
    public void testVisitAnyClass_multipleMarkers_allCompleteSuccessfully() {
        // Arrange
        WrapperClassMarker marker1 = new WrapperClassMarker();
        WrapperClassMarker marker2 = new WrapperClassMarker();
        WrapperClassMarker marker3 = new WrapperClassMarker();

        // Act & Assert - all should complete successfully
        assertDoesNotThrow(() -> marker1.visitAnyClass(clazz));
        assertDoesNotThrow(() -> marker2.visitAnyClass(clazz));
        assertDoesNotThrow(() -> marker3.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass does not modify any state.
     * Since it's a no-op, internal state should remain unchanged.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyState() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.subClassCount = 0;

        // First establish some state by calling visitProgramClass
        wrapperClassMarker.visitProgramClass(programClass);

        // Act - call visitAnyClass
        wrapperClassMarker.visitAnyClass(clazz);

        // Assert - calling visitProgramClass again should work the same way
        assertDoesNotThrow(() -> wrapperClassMarker.visitProgramClass(programClass));
    }

    /**
     * Tests that visitAnyClass can be called before any other methods.
     */
    @Test
    public void testVisitAnyClass_beforeAnyOtherMethods_completesSuccessfully() {
        // Arrange - fresh marker, no previous calls
        WrapperClassMarker freshMarker = new WrapperClassMarker();

        // Act & Assert - should complete successfully even as first call
        assertDoesNotThrow(() -> freshMarker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with null after successful calls with valid objects.
     */
    @Test
    public void testVisitAnyClass_nullAfterValidCalls_completesSuccessfully() {
        // Act & Assert - alternate between valid clazz and null
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(null));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(clazz));
        assertDoesNotThrow(() -> wrapperClassMarker.visitAnyClass(null));
    }
}
