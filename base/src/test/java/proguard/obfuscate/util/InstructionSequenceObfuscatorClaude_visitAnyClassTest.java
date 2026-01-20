package proguard.obfuscate.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link InstructionSequenceObfuscator#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method is an empty implementation (no-op) that serves as a default
 * handler in the ClassVisitor pattern. The actual obfuscation logic is handled by
 * visitProgramClass which processes methods of program classes.
 */
public class InstructionSequenceObfuscatorClaude_visitAnyClassTest {

    private InstructionSequenceObfuscator obfuscator;
    private ReplacementSequences replacementSequences;

    @BeforeEach
    public void setUp() {
        replacementSequences = createSimpleReplacementSequences();
        obfuscator = new InstructionSequenceObfuscator(replacementSequences);
    }

    /**
     * Tests that visitAnyClass can be called with a ProgramClass without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyClass(programClass));
    }

    /**
     * Tests that visitAnyClass can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyClass(null));
    }

    /**
     * Tests that visitAnyClass can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     * Using mocking here to verify no interactions occur.
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithClazz() {
        // Arrange
        Clazz clazz = mock(ProgramClass.class);

        // Act
        obfuscator.visitAnyClass(clazz);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass works with different Clazz instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        ProgramClass clazz1 = new ProgramClass();
        ProgramClass clazz2 = new ProgramClass();
        ProgramClass clazz3 = new ProgramClass();

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(clazz1);
            obfuscator.visitAnyClass(clazz2);
            obfuscator.visitAnyClass(clazz3);
        });
    }

    /**
     * Tests that visitAnyClass execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            obfuscator.visitAnyClass(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyClass should execute quickly as it's a no-op");
    }

    /**
     * Tests that multiple obfuscators can independently call visitAnyClass.
     * Each obfuscator should maintain its own independent state.
     */
    @Test
    public void testVisitAnyClass_multipleObfuscatorsIndependent() {
        // Arrange
        ReplacementSequences sequences1 = createSimpleReplacementSequences();
        ReplacementSequences sequences2 = createSimpleReplacementSequences();
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(sequences1);
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(sequences2);
        ProgramClass programClass = new ProgramClass();

        // Act
        obfuscator1.visitAnyClass(programClass);
        obfuscator2.visitAnyClass(programClass);

        // Assert - both operations should complete without errors
        assertDoesNotThrow(() -> {
            obfuscator1.visitAnyClass(programClass);
            obfuscator2.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass with null followed by valid clazz works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyClass_mixedNullAndValidCalls_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(null);
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitAnyClass(null);
            obfuscator.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass doesn't affect subsequent operations.
     * Calling visitAnyClass should not interfere with other obfuscator methods.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectSubsequentOperations() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyClass
        obfuscator.visitAnyClass(programClass);

        // Call visitProgramClass after visitAnyClass
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitAnyClass can be called alternately with visitProgramClass.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyClass_alternatingWithVisitProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitProgramClass(programClass);
            obfuscator.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass behavior is consistent across rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - all calls should complete without issues
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> obfuscator.visitAnyClass(programClass),
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
        ProgramClass sameClazz = new ProgramClass();

        // Act & Assert - should not throw any exception on repeated calls
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(sameClazz);
            obfuscator.visitAnyClass(sameClazz);
            obfuscator.visitAnyClass(sameClazz);
            obfuscator.visitAnyClass(sameClazz);
            obfuscator.visitAnyClass(sameClazz);
        });
    }

    /**
     * Tests that visitAnyClass does not modify the internal state of InstructionSequenceObfuscator.
     * The method should have no side effects on the obfuscator's configuration.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyObfuscatorState() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();

        // Act - call visitAnyClass
        obfuscator.visitAnyClass(programClass1);

        // Assert - obfuscator should still work normally after visitAnyClass
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass2));
    }

    /**
     * Tests that visitAnyClass can handle concurrent-like sequential calls.
     * Simulates rapid usage pattern that might occur in visitor traversal.
     */
    @Test
    public void testVisitAnyClass_concurrentLikeSequentialCalls_doesNotThrowException() {
        // Arrange
        ProgramClass[] clazzes = new ProgramClass[5];
        for (int i = 0; i < clazzes.length; i++) {
            clazzes[i] = new ProgramClass();
        }

        // Act & Assert - simulate rapid visitor traversal
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                obfuscator.visitAnyClass(c);
            }
            for (Clazz c : clazzes) {
                obfuscator.visitAnyClass(c);
            }
        });
    }

    /**
     * Tests that visitAnyClass works regardless of the ReplacementSequences configuration.
     * The method's behavior should be independent of constructor parameters.
     */
    @Test
    public void testVisitAnyClass_withDifferentReplacementSequences_doesNotThrowException() {
        // Arrange - create obfuscators with different replacement sequences
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ReplacementSequences sequences1 = new proguard.obfuscate.kotlin.KotlinIntrinsicsReplacementSequences(
            programClassPool, libraryClassPool);
        ReplacementSequences sequences2 = createEmptyReplacementSequences();

        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(sequences1);
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(sequences2);

        ProgramClass programClass = new ProgramClass();

        // Act & Assert - all should work without exception
        assertDoesNotThrow(() -> {
            obfuscator1.visitAnyClass(programClass);
            obfuscator2.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass is safe for sequential calls from a single thread.
     * Multiple sequential calls should not cause any issues.
     */
    @Test
    public void testVisitAnyClass_sequentialCallsFromSingleThread_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        int iterations = 100;

        // Act & Assert - sequential calls should all succeed
        for (int i = 0; i < iterations; i++) {
            assertDoesNotThrow(() -> obfuscator.visitAnyClass(programClass),
                    "Iteration " + i + " should not throw exception");
        }
    }

    /**
     * Tests that visitAnyClass doesn't modify the clazz object.
     * The clazz should remain unchanged after the call.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyClazz() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0x0021; // public class
        int originalAccessFlags = programClass.u2accessFlags;

        // Act
        obfuscator.visitAnyClass(programClass);

        // Assert - clazz should remain unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
            "visitAnyClass should not modify the clazz");
    }

    /**
     * Tests that visitAnyClass completes successfully with an initialized ProgramClass.
     * Verifies behavior with a more complex class setup.
     */
    @Test
    public void testVisitAnyClass_withInitializedProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0x0021; // public class
        programClass.u2thisClass = 1;

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitAnyClass(programClass));
    }

    /**
     * Tests that visitAnyClass can be called before and after visitProgramClass.
     * Verifies that the order of calls doesn't matter for the no-op method.
     */
    @Test
    public void testVisitAnyClass_beforeAndAfterVisitProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitProgramClass(programClass);
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitProgramClass(programClass);
            obfuscator.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass with multiple different obfuscators on same clazz.
     * Verifies that different obfuscators don't interfere with each other.
     */
    @Test
    public void testVisitAnyClass_multipleObfuscatorsOnSameClazz_doesNotThrowException() {
        // Arrange
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());
        InstructionSequenceObfuscator obfuscator3 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator1.visitAnyClass(programClass);
            obfuscator2.visitAnyClass(programClass);
            obfuscator3.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass handles repeated null calls.
     * Verifies robustness with null inputs.
     */
    @Test
    public void testVisitAnyClass_repeatedNullCalls_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(null);
            obfuscator.visitAnyClass(null);
            obfuscator.visitAnyClass(null);
            obfuscator.visitAnyClass(null);
            obfuscator.visitAnyClass(null);
        });
    }

    /**
     * Tests that visitAnyClass returns immediately without blocking.
     * Verifies the method is non-blocking.
     */
    @Test
    public void testVisitAnyClass_returnsImmediately() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        long maxExpectedTimeNs = 1_000_000; // 1ms

        // Act
        long startTime = System.nanoTime();
        obfuscator.visitAnyClass(programClass);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Assert - should return very quickly
        assertTrue(duration < maxExpectedTimeNs,
            "visitAnyClass should return immediately (took " + duration + "ns)");
    }

    /**
     * Tests that visitAnyClass with various ProgramClass configurations.
     * Verifies the method works regardless of class state.
     */
    @Test
    public void testVisitAnyClass_withVariousClassConfigurations_doesNotThrowException() {
        // Arrange - create classes with different configurations
        ProgramClass emptyClass = new ProgramClass();

        ProgramClass publicClass = new ProgramClass();
        publicClass.u2accessFlags = 0x0001; // public

        ProgramClass finalClass = new ProgramClass();
        finalClass.u2accessFlags = 0x0010; // final

        ProgramClass abstractClass = new ProgramClass();
        abstractClass.u2accessFlags = 0x0400; // abstract

        // Act & Assert - all should work without exception
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(emptyClass);
            obfuscator.visitAnyClass(publicClass);
            obfuscator.visitAnyClass(finalClass);
            obfuscator.visitAnyClass(abstractClass);
        });
    }

    /**
     * Tests that visitAnyClass can be chained with other visitor methods.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyClass_chainedWithOtherVisitorMethods_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - chained calls should work
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitProgramClass(programClass);
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitAnyClass(programClass);
        });
    }

    // Helper methods

    private ReplacementSequences createSimpleReplacementSequences() {
        return new ReplacementSequences() {
            @Override
            public Instruction[][][] getSequences() {
                return new Instruction[0][][];
            }

            @Override
            public Constant[] getConstants() {
                return new Constant[0];
            }
        };
    }

    private ReplacementSequences createEmptyReplacementSequences() {
        return new ReplacementSequences() {
            @Override
            public Instruction[][][] getSequences() {
                return new Instruction[0][][];
            }

            @Override
            public Constant[] getConstants() {
                return new Constant[0];
            }
        };
    }
}
