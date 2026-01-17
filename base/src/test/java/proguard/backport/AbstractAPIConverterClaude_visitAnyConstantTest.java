package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method is a no-op (empty) method that serves as the default
 * implementation for constant processing in the ConstantVisitor interface. Specific constant
 * types (like ClassConstant, FieldrefConstant, AnyMethodrefConstant) have their own
 * overridden implementations. This method is called for constants that don't need
 * special processing.
 */
public class AbstractAPIConverterClaude_visitAnyConstantTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Constant constant;

    /**
     * Creates a concrete test subclass of AbstractAPIConverter for testing purposes.
     * This is necessary because AbstractAPIConverter is abstract.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter,
                        ClassVisitor modifiedClassVisitor,
                        InstructionVisitor extraInstructionVisitor) {
            super(programClassPool, libraryClassPool, warningPrinter,
                  modifiedClassVisitor, extraInstructionVisitor);

            // Initialize with empty replacements to avoid NullPointerExceptions
            setTypeReplacements(new TypeReplacement[0]);
            setMethodReplacements(new MethodReplacement[0]);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        modifiedClassVisitor = mock(ClassVisitor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);

        converter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        clazz = mock(ProgramClass.class);
        constant = mock(Constant.class);
    }

    /**
     * Tests that visitAnyConstant can be called without throwing exceptions.
     * This verifies the method executes successfully as a no-op.
     */
    @Test
    public void testVisitAnyConstant_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant does not interact with the clazz parameter.
     * Since the method is a no-op, it should not read or modify the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithClazz() {
        // Act
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyConstant does not interact with the constant parameter.
     * Since the method is a no-op, it should not read or modify the constant.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithConstant() {
        // Act
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions with constant
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant does not trigger warnings.
     * A no-op method should not generate any warnings.
     */
    @Test
    public void testVisitAnyConstant_doesNotTriggerWarnings() {
        // Act
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnyConstant does not trigger the modified class visitor.
     * A no-op method should not mark the class as modified.
     */
    @Test
    public void testVisitAnyConstant_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnyConstant does not trigger the extra instruction visitor.
     * A no-op method should not interact with the instruction visitor.
     */
    @Test
    public void testVisitAnyConstant_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitAnyConstant can be called multiple times without side effects.
     * Since it's a no-op, multiple calls should remain safe.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes_noSideEffects() {
        // Act
        converter.visitAnyConstant(clazz, constant);
        converter.visitAnyConstant(clazz, constant);
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant works with different constant instances.
     * The no-op behavior should be consistent across different instances.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstants() {
        // Arrange
        Constant constant1 = mock(Constant.class);
        Constant constant2 = mock(Constant.class);
        Constant constant3 = mock(Constant.class);

        // Act
        converter.visitAnyConstant(clazz, constant1);
        converter.visitAnyConstant(clazz, constant2);
        converter.visitAnyConstant(clazz, constant3);

        // Assert - verify no interactions occurred with any constants
        verifyNoInteractions(constant1);
        verifyNoInteractions(constant2);
        verifyNoInteractions(constant3);
    }

    /**
     * Tests that visitAnyConstant works with different clazz instances.
     * The no-op behavior should be consistent across different classes.
     */
    @Test
    public void testVisitAnyConstant_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act
        converter.visitAnyConstant(clazz1, constant);
        converter.visitAnyConstant(clazz2, constant);
        converter.visitAnyConstant(clazz3, constant);

        // Assert - verify no interactions occurred with any classes
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(clazz3);
    }

    /**
     * Tests visitAnyConstant with a converter with null warning printer.
     * The no-op method should work even with null optional dependencies.
     */
    @Test
    public void testVisitAnyConstant_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitAnyConstant(clazz, constant)
        );
    }

    /**
     * Tests visitAnyConstant with a converter with null class visitor.
     * The no-op method should work even with null optional dependencies.
     */
    @Test
    public void testVisitAnyConstant_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitAnyConstant(clazz, constant)
        );
    }

    /**
     * Tests visitAnyConstant with a converter with null instruction visitor.
     * The no-op method should work even with null optional dependencies.
     */
    @Test
    public void testVisitAnyConstant_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitAnyConstant(clazz, constant)
        );
    }

    /**
     * Tests visitAnyConstant with all null optional dependencies.
     * The no-op method should work even with all optional dependencies null.
     */
    @Test
    public void testVisitAnyConstant_withAllNullDependencies() {
        // Arrange
        TestAPIConverter converterWithAllNulls = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            null, // null class visitor
            null  // null instruction visitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithAllNulls.visitAnyConstant(clazz, constant)
        );
    }

    /**
     * Tests that visitAnyConstant executes extremely quickly.
     * Since it's a no-op, it should have virtually no overhead.
     */
    @Test
    public void testVisitAnyConstant_executesVeryQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 100000; i++) {
            converter.visitAnyConstant(clazz, constant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 100000 calls)
        assertTrue(durationMs < 100,
            "visitAnyConstant should execute very quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyConstant handles null clazz parameter.
     * Even with null parameters, the no-op method should not throw (though this is
     * not recommended in practice).
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_doesNotThrow() {
        // Act & Assert - should not throw even with null clazz
        assertDoesNotThrow(() ->
            converter.visitAnyConstant(null, constant)
        );
    }

    /**
     * Tests that visitAnyConstant handles null constant parameter.
     * Even with null parameters, the no-op method should not throw.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_doesNotThrow() {
        // Act & Assert - should not throw even with null constant
        assertDoesNotThrow(() ->
            converter.visitAnyConstant(clazz, null)
        );
    }

    /**
     * Tests that visitAnyConstant handles all null parameters.
     * Even with all null parameters, the no-op method should not throw.
     */
    @Test
    public void testVisitAnyConstant_withAllNullParameters_doesNotThrow() {
        // Act & Assert - should not throw even with all null parameters
        assertDoesNotThrow(() ->
            converter.visitAnyConstant(null, null)
        );
    }

    /**
     * Tests visitAnyConstant with empty class pools.
     * The no-op method should work with empty class pools.
     */
    @Test
    public void testVisitAnyConstant_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitAnyConstant(clazz, constant)
        );

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests visitAnyConstant across different converter instances.
     * Different converters should all exhibit the same no-op behavior.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converter.visitAnyConstant(clazz, constant);
        converter2.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with either converter
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant maintains no-op behavior in all contexts.
     * This comprehensive test verifies the no-op behavior with various parameter combinations.
     */
    @Test
    public void testVisitAnyConstant_noOpBehaviorInAllContexts() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);

        // Act - call with various combinations
        converter.visitAnyConstant(clazz1, const1);
        converter.visitAnyConstant(clazz1, const2);
        converter.visitAnyConstant(clazz2, const1);
        converter.visitAnyConstant(clazz2, const2);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
    }

    /**
     * Tests that visitAnyConstant is truly a no-op by verifying no state changes.
     * This test ensures the method doesn't have any hidden side effects.
     */
    @Test
    public void testVisitAnyConstant_noStateChanges() {
        // Arrange
        TestAPIConverter converterSpy = spy(converter);

        // Act
        converterSpy.visitAnyConstant(clazz, constant);

        // Assert - verify only visitAnyConstant was called, no other methods
        verify(converterSpy, times(1)).visitAnyConstant(any(), any());
        verifyNoMoreInteractions(converterSpy);
    }

    /**
     * Tests that visitAnyConstant can handle concurrent calls safely.
     * Since it's a no-op with no shared state modification, it should be thread-safe.
     */
    @Test
    public void testVisitAnyConstant_threadSafe() {
        // Arrange
        int threadCount = 10;
        int callsPerThread = 1000;

        // Act - call from multiple threads
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    converter.visitAnyConstant(clazz, constant);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            assertDoesNotThrow(() -> thread.join());
        }

        // Assert - verify no interactions occurred despite concurrent calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant serves as the default implementation.
     * This method is called for constants that don't have specialized implementations,
     * such as primitive constants (int, float, etc.) that don't need type replacement.
     */
    @Test
    public void testVisitAnyConstant_servesAsDefaultImplementation() {
        // Arrange
        Constant primitiveConstant = mock(Constant.class, "primitiveConstant");
        Constant stringConstant = mock(Constant.class, "stringConstant");
        Constant doubleConstant = mock(Constant.class, "doubleConstant");

        // Act - these would represent constants that don't need processing
        converter.visitAnyConstant(clazz, primitiveConstant);
        converter.visitAnyConstant(clazz, stringConstant);
        converter.visitAnyConstant(clazz, doubleConstant);

        // Assert - verify no processing occurred, which is correct for these types
        verifyNoInteractions(primitiveConstant);
        verifyNoInteractions(stringConstant);
        verifyNoInteractions(doubleConstant);
    }

    /**
     * Tests that visitAnyConstant has zero memory allocation overhead.
     * Since it's an empty method, it shouldn't allocate any objects.
     */
    @Test
    public void testVisitAnyConstant_zeroAllocationOverhead() {
        // Act - call multiple times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnyConstant(clazz, constant);
        }

        // Assert - verify no interactions, implying no object creation for processing
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant maintains consistency across invocations.
     * Every call should behave identically as a no-op.
     */
    @Test
    public void testVisitAnyConstant_consistentBehavior() {
        // Act - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                converter.visitAnyConstant(clazz, constant)
            );
        }

        // Assert - verify no interactions occurred in any of the calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant can be used as a base implementation.
     * Subclasses can override this method if they need specific behavior,
     * but the default is to do nothing.
     */
    @Test
    public void testVisitAnyConstant_usableAsBaseImplementation() {
        // Arrange - the TestAPIConverter doesn't override this method

        // Act
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify the base implementation (no-op) was used
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
        verifyNoInteractions(warningPrinter);
        verifyNoInteractions(modifiedClassVisitor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitAnyConstant with various parameter combinations to ensure robustness.
     */
    @Test
    public void testVisitAnyConstant_withVariousParameterCombinations() {
        // Arrange
        Clazz[] classes = {
            mock(ProgramClass.class, "class1"),
            mock(ProgramClass.class, "class2"),
            null
        };
        Constant[] constants = {
            mock(Constant.class, "constant1"),
            mock(Constant.class, "constant2"),
            null
        };

        // Act - test all combinations
        for (Clazz c : classes) {
            for (Constant con : constants) {
                assertDoesNotThrow(() -> converter.visitAnyConstant(c, con),
                    "visitAnyConstant should not throw with any parameter combination");
            }
        }
    }

    /**
     * Tests that visitAnyConstant can be invoked in rapid succession without issues.
     */
    @Test
    public void testVisitAnyConstant_rapidSuccessiveCalls() {
        // Act & Assert - rapid calls should not cause issues
        for (int i = 0; i < 10000; i++) {
            assertDoesNotThrow(() -> converter.visitAnyConstant(clazz, constant));
        }

        // Verify no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests visitAnyConstant with mixed converter configurations.
     */
    @Test
    public void testVisitAnyConstant_withMixedConverterConfigurations() {
        // Arrange
        TestAPIConverter[] converters = {
            new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter, modifiedClassVisitor, extraInstructionVisitor),
            new TestAPIConverter(programClassPool, libraryClassPool, null, modifiedClassVisitor, extraInstructionVisitor),
            new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter, null, extraInstructionVisitor),
            new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter, modifiedClassVisitor, null),
            new TestAPIConverter(programClassPool, libraryClassPool, null, null, null)
        };

        // Act - test with all configurations
        for (TestAPIConverter conv : converters) {
            assertDoesNotThrow(() -> conv.visitAnyConstant(clazz, constant),
                "visitAnyConstant should work with all converter configurations");
        }
    }

    /**
     * Tests that visitAnyConstant doesn't affect class pool state.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectClassPools() {
        // Arrange
        int programPoolSizeBefore = programClassPool.size();
        int libraryPoolSizeBefore = libraryClassPool.size();

        // Act
        converter.visitAnyConstant(clazz, constant);

        // Assert - verify class pools are unchanged
        assertEquals(programPoolSizeBefore, programClassPool.size(),
            "Program class pool size should remain unchanged");
        assertEquals(libraryPoolSizeBefore, libraryClassPool.size(),
            "Library class pool size should remain unchanged");
    }
}
