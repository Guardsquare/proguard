package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link RenamedFlagSetter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method is an empty implementation (no-op) that serves as a default
 * handler in the ClassVisitor pattern. The actual renaming logic is handled by visitProgramClass.
 */
public class RenamedFlagSetterClaude_visitAnyClassTest {

    private RenamedFlagSetter setter;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        setter = new RenamedFlagSetter();
        clazz = mock(ProgramClass.class);
    }

    /**
     * Tests that visitAnyClass can be called with a valid mock object without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyClass_withValidMock_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> setter.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> setter.visitAnyClass(null));
    }

    /**
     * Tests that visitAnyClass can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            setter.visitAnyClass(clazz);
            setter.visitAnyClass(clazz);
            setter.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithClazz() {
        // Act
        setter.visitAnyClass(clazz);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            setter.visitAnyClass(clazz1);
            setter.visitAnyClass(clazz2);
            setter.visitAnyClass(clazz3);
        });
    }

    /**
     * Tests that visitAnyClass execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            setter.visitAnyClass(clazz);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyClass should execute quickly as it's a no-op");
    }

    /**
     * Tests that multiple setters can independently call visitAnyClass.
     * Each setter should maintain its own independent state.
     */
    @Test
    public void testVisitAnyClass_multipleSettersIndependent() {
        // Arrange
        RenamedFlagSetter setter1 = new RenamedFlagSetter();
        RenamedFlagSetter setter2 = new RenamedFlagSetter();

        // Act
        setter1.visitAnyClass(clazz);
        setter2.visitAnyClass(clazz);

        // Assert - both operations should complete without errors
        assertDoesNotThrow(() -> {
            setter1.visitAnyClass(clazz);
            setter2.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass with null followed by valid clazz works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyClass_mixedNullAndValidCalls_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            setter.visitAnyClass(null);
            setter.visitAnyClass(clazz);
            setter.visitAnyClass(null);
            setter.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass doesn't affect subsequent operations.
     * Calling visitAnyClass should not interfere with other setter methods.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectSubsequentOperations() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        when(programClass.getName()).thenReturn("TestClass");

        // Act - call visitAnyClass
        setter.visitAnyClass(clazz);

        // Call visitProgramClass after visitAnyClass
        assertDoesNotThrow(() -> setter.visitProgramClass(programClass));

        // Assert - verify visitProgramClass still works normally
        verify(programClass, atLeastOnce()).getName();
    }

    /**
     * Tests that visitAnyClass can be called alternately with visitProgramClass.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyClass_alternatingWithVisitProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        when(programClass.getName()).thenReturn("TestClass");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            setter.visitAnyClass(clazz);
            setter.visitProgramClass(programClass);
            setter.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass behavior is consistent across rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Act & Assert - all calls should complete without issues
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> setter.visitAnyClass(clazz),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that visitAnyClass can be safely called on the same clazz instance multiple times.
     * The method should be safe for repeated calls with the same object.
     */
    @Test
    public void testVisitAnyClass_sameInstanceMultipleTimes_doesNotThrowException() {
        // Arrange
        Clazz sameClazz = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception on repeated calls
        assertDoesNotThrow(() -> {
            setter.visitAnyClass(sameClazz);
            setter.visitAnyClass(sameClazz);
            setter.visitAnyClass(sameClazz);
            setter.visitAnyClass(sameClazz);
            setter.visitAnyClass(sameClazz);
        });

        // Verify no interactions with the clazz
        verifyNoInteractions(sameClazz);
    }

    /**
     * Tests that visitAnyClass does not modify the internal state of RenamedFlagSetter.
     * Since RenamedFlagSetter is stateless, the method should have no side effects.
     */
    @Test
    public void testVisitAnyClass_doesNotModifySetterState() {
        // Arrange - call visitAnyClass
        setter.visitAnyClass(clazz);

        // Act - use the setter with visitProgramClass
        ProgramClass programClass = mock(ProgramClass.class);
        when(programClass.getName()).thenReturn("TestClass");

        // Assert - setter should still work normally after visitAnyClass
        assertDoesNotThrow(() -> setter.visitProgramClass(programClass));
    }

    /**
     * Tests that visitAnyClass can handle concurrent-like sequential calls.
     * Simulates rapid usage pattern that might occur in visitor traversal.
     */
    @Test
    public void testVisitAnyClass_concurrentLikeSequentialCalls_doesNotThrowException() {
        // Arrange
        Clazz[] clazzes = new Clazz[5];
        for (int i = 0; i < clazzes.length; i++) {
            clazzes[i] = mock(ProgramClass.class);
        }

        // Act & Assert - simulate rapid visitor traversal
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                setter.visitAnyClass(c);
            }
            for (Clazz c : clazzes) {
                setter.visitAnyClass(c);
            }
        });
    }
}
