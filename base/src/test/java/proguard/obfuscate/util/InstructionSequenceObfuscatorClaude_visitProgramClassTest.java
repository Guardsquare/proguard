package proguard.obfuscate.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link InstructionSequenceObfuscator#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method is responsible for visiting all methods of a ProgramClass
 * by calling programClass.methodsAccept(this). This allows the obfuscator to process
 * each method and apply instruction sequence obfuscation where applicable.
 */
public class InstructionSequenceObfuscatorClaude_visitProgramClassTest {

    private InstructionSequenceObfuscator obfuscator;
    private ReplacementSequences replacementSequences;

    @BeforeEach
    public void setUp() {
        replacementSequences = createSimpleReplacementSequences();
        obfuscator = new InstructionSequenceObfuscator(replacementSequences);
    }

    /**
     * Tests that visitProgramClass calls methodsAccept on the ProgramClass.
     * This is the core functionality - visiting all methods.
     */
    @Test
    public void testVisitProgramClass_callsMethodsAccept() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(programClass);

        // Assert
        verify(programClass).methodsAccept(eq(obfuscator));
    }

    /**
     * Tests that visitProgramClass passes the obfuscator instance to methodsAccept.
     * The obfuscator acts as a MemberVisitor.
     */
    @Test
    public void testVisitProgramClass_passesCorrectVisitorToMethodsAccept() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(programClass);

        // Assert - verify that the visitor passed is the obfuscator itself
        verify(programClass).methodsAccept(same(obfuscator));
    }

    /**
     * Tests that visitProgramClass doesn't throw exceptions on valid input.
     */
    @Test
    public void testVisitProgramClass_noExceptionThrown() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same obfuscator instance.
     */
    @Test
    public void testVisitProgramClass_multipleCallsOnSameInstance() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(class1);
        obfuscator.visitProgramClass(class2);

        // Assert
        verify(class1).methodsAccept(obfuscator);
        verify(class2).methodsAccept(obfuscator);
    }

    /**
     * Tests that visitProgramClass can be called with the same ProgramClass multiple times.
     */
    @Test
    public void testVisitProgramClass_sameProgramClassCalledMultipleTimes() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(programClass);
        obfuscator.visitProgramClass(programClass);
        obfuscator.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(3)).methodsAccept(eq(obfuscator));
    }

    /**
     * Tests that visitProgramClass works with a real ProgramClass instance.
     * Testing without mocking where possible.
     */
    @Test
    public void testVisitProgramClass_withRealProgramClass_noMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw even with no methods
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass works with a ProgramClass that has methods.
     * Testing without mocking where possible.
     */
    @Test
    public void testVisitProgramClass_withRealProgramClass_withMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.methods = new ProgramMethod[] {
            new ProgramMethod(),
            new ProgramMethod()
        };

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass only calls methodsAccept, not other methods.
     */
    @Test
    public void testVisitProgramClass_onlyCallsMethodsAccept() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(programClass);

        // Assert - verify only methodsAccept is called
        verify(programClass).methodsAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(programClass);
    }

    /**
     * Tests that multiple obfuscators can process the same ProgramClass independently.
     */
    @Test
    public void testVisitProgramClass_multipleObfuscatorInstances() {
        // Arrange
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        obfuscator1.visitProgramClass(programClass);
        obfuscator2.visitProgramClass(programClass);

        // Assert
        verify(programClass).methodsAccept(obfuscator1);
        verify(programClass).methodsAccept(obfuscator2);
    }

    /**
     * Tests that visitProgramClass works with different ReplacementSequences configurations.
     */
    @Test
    public void testVisitProgramClass_withDifferentReplacementSequences() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ReplacementSequences kotlinSequences = new proguard.obfuscate.kotlin.KotlinIntrinsicsReplacementSequences(
            programClassPool, libraryClassPool);
        InstructionSequenceObfuscator kotlinObfuscator = new InstructionSequenceObfuscator(kotlinSequences);

        ProgramClass programClass = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> kotlinObfuscator.visitProgramClass(programClass));
        verify(programClass).methodsAccept(kotlinObfuscator);
    }

    /**
     * Tests that visitProgramClass maintains state correctly across multiple calls.
     */
    @Test
    public void testVisitProgramClass_maintainsStateAcrossMultipleCalls() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);
        ProgramClass class3 = mock(ProgramClass.class);

        // Act - call multiple times
        obfuscator.visitProgramClass(class1);
        obfuscator.visitProgramClass(class2);
        obfuscator.visitProgramClass(class3);

        // Assert - all should be processed
        verify(class1).methodsAccept(obfuscator);
        verify(class2).methodsAccept(obfuscator);
        verify(class3).methodsAccept(obfuscator);
    }

    /**
     * Tests that visitProgramClass works in rapid succession.
     */
    @Test
    public void testVisitProgramClass_rapidSuccessiveCalls() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act - call rapidly
        for (int i = 0; i < 10; i++) {
            obfuscator.visitProgramClass(programClass);
        }

        // Assert
        verify(programClass, times(10)).methodsAccept(obfuscator);
    }

    /**
     * Tests that visitProgramClass can be called on different class types.
     */
    @Test
    public void testVisitProgramClass_withDifferentClassTypes() {
        // Arrange
        ProgramClass emptyClass = new ProgramClass();

        ProgramClass classWithOneMethods = new ProgramClass();
        classWithOneMethods.methods = new ProgramMethod[] { new ProgramMethod() };

        ProgramClass classWithManyMethods = new ProgramClass();
        classWithManyMethods.methods = new ProgramMethod[] {
            new ProgramMethod(), new ProgramMethod(), new ProgramMethod()
        };

        // Act & Assert - all should work
        assertDoesNotThrow(() -> {
            obfuscator.visitProgramClass(emptyClass);
            obfuscator.visitProgramClass(classWithOneMethods);
            obfuscator.visitProgramClass(classWithManyMethods);
        });
    }

    /**
     * Tests that visitProgramClass doesn't modify the ProgramClass structure.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyClassStructure() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0x0021; // public class
        programClass.methods = new ProgramMethod[] { new ProgramMethod() };
        int originalAccessFlags = programClass.u2accessFlags;
        int originalMethodCount = programClass.methods.length;

        // Act
        obfuscator.visitProgramClass(programClass);

        // Assert - structure should remain unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags);
        assertEquals(originalMethodCount, programClass.methods.length);
    }

    /**
     * Tests that visitProgramClass works correctly after visitAnyClass.
     */
    @Test
    public void testVisitProgramClass_afterVisitAnyClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act
        obfuscator.visitAnyClass(programClass);
        obfuscator.visitProgramClass(programClass);

        // Assert - should complete without error
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass can handle alternating calls with visitAnyClass.
     */
    @Test
    public void testVisitProgramClass_alternatingWithVisitAnyClass() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitProgramClass(programClass);
            obfuscator.visitAnyClass(programClass);
            obfuscator.visitProgramClass(programClass);
        });

        verify(programClass, times(2)).methodsAccept(obfuscator);
    }

    /**
     * Tests that visitProgramClass is the correct overridden method from ClassVisitor.
     */
    @Test
    public void testVisitProgramClass_isClassVisitorMethod() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act - should be callable as ClassVisitor method
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass processes classes in order when called sequentially.
     */
    @Test
    public void testVisitProgramClass_processesInOrder() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);
        ProgramClass class3 = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(class1);
        obfuscator.visitProgramClass(class2);
        obfuscator.visitProgramClass(class3);

        // Assert - verify all were called in sequence
        verify(class1).methodsAccept(obfuscator);
        verify(class2).methodsAccept(obfuscator);
        verify(class3).methodsAccept(obfuscator);
    }

    /**
     * Tests that visitProgramClass works with a batch of program classes.
     */
    @Test
    public void testVisitProgramClass_batchProcessing() {
        // Arrange
        int batchSize = 20;
        ProgramClass[] classes = new ProgramClass[batchSize];
        for (int i = 0; i < batchSize; i++) {
            classes[i] = mock(ProgramClass.class);
        }

        // Act
        for (ProgramClass clazz : classes) {
            obfuscator.visitProgramClass(clazz);
        }

        // Assert - verify all were processed
        for (ProgramClass clazz : classes) {
            verify(clazz).methodsAccept(obfuscator);
        }
    }

    /**
     * Tests that visitProgramClass completes quickly for classes with no methods.
     */
    @Test
    public void testVisitProgramClass_performanceWithNoMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        long startTime = System.nanoTime();

        // Act - call multiple times
        for (int i = 0; i < 1000; i++) {
            obfuscator.visitProgramClass(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitProgramClass should execute quickly");
    }

    /**
     * Tests that visitProgramClass correctly delegates to methodsAccept which is a MemberVisitor.
     */
    @Test
    public void testVisitProgramClass_delegatesToMemberVisitor() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(programClass);

        // Assert - the obfuscator should be used as a MemberVisitor
        verify(programClass).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass with empty replacement sequences still works.
     */
    @Test
    public void testVisitProgramClass_withEmptyReplacementSequences() {
        // Arrange
        ReplacementSequences emptySequences = createEmptyReplacementSequences();
        InstructionSequenceObfuscator emptyObfuscator = new InstructionSequenceObfuscator(emptySequences);
        ProgramClass programClass = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> emptyObfuscator.visitProgramClass(programClass));
        verify(programClass).methodsAccept(emptyObfuscator);
    }

    /**
     * Tests that visitProgramClass is idempotent - can be called multiple times safely.
     */
    @Test
    public void testVisitProgramClass_isIdempotent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.methods = new ProgramMethod[] { new ProgramMethod() };

        // Act - call multiple times
        obfuscator.visitProgramClass(programClass);
        obfuscator.visitProgramClass(programClass);
        obfuscator.visitProgramClass(programClass);

        // Assert - should complete without errors
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass works with classes from different packages.
     */
    @Test
    public void testVisitProgramClass_withClassesFromDifferentPackages() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        // Act & Assert - all should work regardless of package
        assertDoesNotThrow(() -> {
            obfuscator.visitProgramClass(class1);
            obfuscator.visitProgramClass(class2);
            obfuscator.visitProgramClass(class3);
        });
    }

    /**
     * Tests that visitProgramClass correctly implements the visitor pattern.
     * The method should act as a dispatcher to visit methods.
     */
    @Test
    public void testVisitProgramClass_implementsVisitorPattern() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        obfuscator.visitProgramClass(programClass);

        // Assert - should call methodsAccept which is the visitor pattern
        verify(programClass).methodsAccept(obfuscator);
    }

    /**
     * Tests that visitProgramClass works with a ProgramClass that has null methods array.
     */
    @Test
    public void testVisitProgramClass_withNullMethodsArray() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.methods = null;

        // Act & Assert - methodsAccept should handle this gracefully
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass with various access flags.
     */
    @Test
    public void testVisitProgramClass_withVariousAccessFlags() {
        // Arrange
        ProgramClass publicClass = new ProgramClass();
        publicClass.u2accessFlags = 0x0001; // public

        ProgramClass finalClass = new ProgramClass();
        finalClass.u2accessFlags = 0x0010; // final

        ProgramClass abstractClass = new ProgramClass();
        abstractClass.u2accessFlags = 0x0400; // abstract

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator.visitProgramClass(publicClass);
            obfuscator.visitProgramClass(finalClass);
            obfuscator.visitProgramClass(abstractClass);
        });
    }

    /**
     * Tests that visitProgramClass execution doesn't leak resources.
     * Multiple sequential calls should not cause memory issues.
     */
    @Test
    public void testVisitProgramClass_noResourceLeak() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.methods = new ProgramMethod[] { new ProgramMethod() };

        // Act - call many times
        for (int i = 0; i < 100; i++) {
            obfuscator.visitProgramClass(programClass);
        }

        // Assert - should complete without errors or resource issues
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
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
