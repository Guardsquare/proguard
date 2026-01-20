package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TypeArgumentFinder.visitAnyInstruction method.
 * Tests the method (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/Instruction;)V
 */
public class TypeArgumentFinderClaude_visitAnyInstructionTest {

    private TypeArgumentFinder finder;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private PartialEvaluator partialEvaluator;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        partialEvaluator = PartialEvaluator.Builder.create().build();
        finder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
    }

    /**
     * Tests that visitAnyInstruction does not throw an exception with all null parameters.
     */
    @Test
    public void testVisitAnyInstruction_withAllNullParameters_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, null),
            "visitAnyInstruction should not throw with all null parameters");
    }

    /**
     * Tests that visitAnyInstruction does not modify typeArgumentClasses when called with null parameters.
     */
    @Test
    public void testVisitAnyInstruction_withNullParameters_doesNotModifyState() {
        // Arrange
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyInstruction(null, null, null, 0, null);

        // Assert
        assertSame(initialState, finder.typeArgumentClasses,
            "visitAnyInstruction should not modify typeArgumentClasses");
    }

    /**
     * Tests that visitAnyInstruction can be called multiple times consecutively without issues.
     */
    @Test
    public void testVisitAnyInstruction_multipleConsecutiveCalls_succeeds() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                finder.visitAnyInstruction(null, null, null, i, null);
            }
        }, "Multiple consecutive calls to visitAnyInstruction should not throw");
    }

    /**
     * Tests that visitAnyInstruction works with different offset values.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentOffsets_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, null),
            "Should work with offset 0");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 100, null),
            "Should work with offset 100");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, -1, null),
            "Should work with negative offset");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, Integer.MAX_VALUE, null),
            "Should work with max int offset");
    }

    /**
     * Tests that visitAnyInstruction with a valid instruction parameter does not throw.
     */
    @Test
    public void testVisitAnyInstruction_withValidInstruction_doesNotThrow() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, instruction),
            "visitAnyInstruction should not throw with valid instruction");
    }

    /**
     * Tests that visitAnyInstruction does not modify state when called with valid instruction.
     */
    @Test
    public void testVisitAnyInstruction_withValidInstruction_doesNotModifyState() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyInstruction(null, null, null, 0, instruction);

        // Assert
        assertSame(initialState, finder.typeArgumentClasses,
            "visitAnyInstruction should not modify typeArgumentClasses even with valid instruction");
    }

    /**
     * Tests that visitAnyInstruction can be called with various instruction types.
     */
    @Test
    public void testVisitAnyInstruction_withVariousInstructionTypes_doesNotThrow() {
        // Arrange
        Instruction nop = new SimpleInstruction(Instruction.OP_NOP);
        Instruction aconst_null = new SimpleInstruction(Instruction.OP_ACONST_NULL);
        Instruction return_instr = new SimpleInstruction(Instruction.OP_RETURN);

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, nop),
            "Should work with NOP instruction");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 1, aconst_null),
            "Should work with ACONST_NULL instruction");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 2, return_instr),
            "Should work with RETURN instruction");
    }

    /**
     * Tests that visitAnyInstruction maintains null state after being called multiple times.
     */
    @Test
    public void testVisitAnyInstruction_multipleCalls_maintainsNullState() {
        // Arrange
        assertNull(finder.typeArgumentClasses, "Initial state should be null");

        // Act
        finder.visitAnyInstruction(null, null, null, 0, null);
        finder.visitAnyInstruction(null, null, null, 1, null);
        finder.visitAnyInstruction(null, null, null, 2, null);

        // Assert
        assertNull(finder.typeArgumentClasses,
            "typeArgumentClasses should remain null after multiple calls");
    }

    /**
     * Tests that visitAnyInstruction can be called after typeArgumentClasses has been set.
     */
    @Test
    public void testVisitAnyInstruction_afterTypeArgumentClassesSet_doesNotModify() {
        // Arrange
        finder.typeArgumentClasses = new String[] { "com.example.TestClass" };
        String[] expectedState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyInstruction(null, null, null, 0, null);

        // Assert
        assertSame(expectedState, finder.typeArgumentClasses,
            "visitAnyInstruction should not modify previously set typeArgumentClasses");
        assertArrayEquals(new String[] { "com.example.TestClass" }, finder.typeArgumentClasses,
            "typeArgumentClasses content should remain unchanged");
    }

    /**
     * Tests that visitAnyInstruction works when called with a non-null Clazz parameter.
     */
    @Test
    public void testVisitAnyInstruction_withNonNullClazz_doesNotThrow() {
        // Arrange
        Clazz clazz = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(clazz, null, null, 0, null),
            "visitAnyInstruction should not throw with non-null Clazz");
    }

    /**
     * Tests that visitAnyInstruction maintains state with non-null Clazz parameter.
     */
    @Test
    public void testVisitAnyInstruction_withNonNullClazz_doesNotModifyState() {
        // Arrange
        Clazz clazz = new ProgramClass();
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyInstruction(clazz, null, null, 0, null);

        // Assert
        assertSame(initialState, finder.typeArgumentClasses,
            "visitAnyInstruction should not modify state with non-null Clazz");
    }

    /**
     * Tests that visitAnyInstruction is idempotent - calling it multiple times has the same effect as calling it once.
     */
    @Test
    public void testVisitAnyInstruction_isIdempotent() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyInstruction(null, null, null, 0, instruction);
        String[] stateAfterFirstCall = finder.typeArgumentClasses;

        finder.visitAnyInstruction(null, null, null, 0, instruction);
        String[] stateAfterSecondCall = finder.typeArgumentClasses;

        // Assert
        assertSame(initialState, stateAfterFirstCall,
            "First call should not modify state");
        assertSame(stateAfterFirstCall, stateAfterSecondCall,
            "Second call should not modify state");
    }

    /**
     * Tests that visitAnyInstruction with zero offset works correctly.
     */
    @Test
    public void testVisitAnyInstruction_withZeroOffset_doesNotThrow() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, instruction),
            "visitAnyInstruction should work with zero offset");
    }

    /**
     * Tests that visitAnyInstruction with large offset value works correctly.
     */
    @Test
    public void testVisitAnyInstruction_withLargeOffset_doesNotThrow() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 999999, instruction),
            "visitAnyInstruction should work with large offset");
    }

    /**
     * Tests that multiple finder instances don't interfere with each other when calling visitAnyInstruction.
     */
    @Test
    public void testVisitAnyInstruction_multipleInstances_independent() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        finder1.typeArgumentClasses = new String[] { "Class1" };
        finder2.typeArgumentClasses = new String[] { "Class2" };

        // Act
        finder1.visitAnyInstruction(null, null, null, 0, null);
        finder2.visitAnyInstruction(null, null, null, 1, null);

        // Assert
        assertArrayEquals(new String[] { "Class1" }, finder1.typeArgumentClasses,
            "finder1 state should remain unchanged");
        assertArrayEquals(new String[] { "Class2" }, finder2.typeArgumentClasses,
            "finder2 state should remain unchanged");
    }

    /**
     * Tests that visitAnyInstruction can handle rapid successive calls.
     */
    @Test
    public void testVisitAnyInstruction_rapidSuccessiveCalls_succeed() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                finder.visitAnyInstruction(null, null, null, i, instruction);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that visitAnyInstruction does not throw when called before any other method.
     */
    @Test
    public void testVisitAnyInstruction_calledFirst_doesNotThrow() {
        // Arrange
        TypeArgumentFinder newFinder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        // Act & Assert
        assertDoesNotThrow(() -> newFinder.visitAnyInstruction(null, null, null, 0, null),
            "visitAnyInstruction should work when called immediately after construction");
    }

    /**
     * Tests that visitAnyInstruction preserves empty array state.
     */
    @Test
    public void testVisitAnyInstruction_withEmptyArrayState_preservesState() {
        // Arrange
        finder.typeArgumentClasses = new String[0];
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyInstruction(null, null, null, 0, null);

        // Assert
        assertSame(initialState, finder.typeArgumentClasses,
            "visitAnyInstruction should not modify empty array state");
        assertEquals(0, finder.typeArgumentClasses.length,
            "Empty array should remain empty");
    }

    /**
     * Tests that visitAnyInstruction can be called with different combinations of null and non-null parameters.
     */
    @Test
    public void testVisitAnyInstruction_mixedNullAndNonNullParameters_doesNotThrow() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(clazz, null, null, 0, null),
            "Should work with non-null clazz");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, instruction),
            "Should work with non-null instruction");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(clazz, null, null, 0, instruction),
            "Should work with non-null clazz and instruction");
    }

    /**
     * Tests that visitAnyInstruction behavior is consistent across different finder instances.
     */
    @Test
    public void testVisitAnyInstruction_consistentBehaviorAcrossInstances() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        String[] state1Before = finder1.typeArgumentClasses;
        String[] state2Before = finder2.typeArgumentClasses;

        // Act
        finder1.visitAnyInstruction(null, null, null, 0, instruction);
        finder2.visitAnyInstruction(null, null, null, 0, instruction);

        // Assert
        assertSame(state1Before, finder1.typeArgumentClasses,
            "finder1 should maintain its state");
        assertSame(state2Before, finder2.typeArgumentClasses,
            "finder2 should maintain its state");
    }

    /**
     * Tests that visitAnyInstruction with various instruction opcodes does not throw.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOpcodes_doesNotThrow() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0,
            new SimpleInstruction(Instruction.OP_ICONST_0)), "Should work with ICONST_0");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 1,
            new SimpleInstruction(Instruction.OP_ICONST_1)), "Should work with ICONST_1");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 2,
            new SimpleInstruction(Instruction.OP_POP)), "Should work with POP");
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 3,
            new SimpleInstruction(Instruction.OP_DUP)), "Should work with DUP");
    }

    /**
     * Tests that visitAnyInstruction maintains thread safety for independent instances.
     */
    @Test
    public void testVisitAnyInstruction_independentInstancesThreadSafe() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(new ClassPool(), new ClassPool(),
            PartialEvaluator.Builder.create().build());
        TypeArgumentFinder finder2 = new TypeArgumentFinder(new ClassPool(), new ClassPool(),
            PartialEvaluator.Builder.create().build());

        // Act
        finder1.visitAnyInstruction(null, null, null, 0, null);
        finder2.visitAnyInstruction(null, null, null, 0, null);

        // Assert
        assertNull(finder1.typeArgumentClasses, "finder1 state should be null");
        assertNull(finder2.typeArgumentClasses, "finder2 state should be null");
    }

    /**
     * Tests that visitAnyInstruction can be called in a sequence with other visitor methods.
     */
    @Test
    public void testVisitAnyInstruction_inSequenceWithOtherMethods_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(null, null, null, 0, null);
            finder.visitAnyConstant(null, null);
            finder.visitAnyInstruction(null, null, null, 1, null);
        }, "visitAnyInstruction should work in sequence with other visitor methods");
    }
}
