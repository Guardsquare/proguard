package proguard.configuration;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.BranchTargetFinder;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static proguard.configuration.ConfigurationLoggingInstructionSequenceConstants.*;

/**
 * Test class for {@link ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method, CodeAttribute, int, int)} method.
 *
 * This test uses reflection to access the protected matchedArgument method because:
 * - The matchedArgument method is protected and used internally by the InstructionSequenceReplacer framework
 * - There is no public API that directly exposes or triggers this method in a testable way
 * - The method is called by the parent class InstructionSequenceReplacer during instruction sequence matching,
 *   but setting up the entire instruction matching pipeline would require extensive mocking of internal
 *   ProGuard bytecode manipulation infrastructure (instruction sequences, matchers, visitors, etc.)
 * - Testing via reflection is the most direct and maintainable approach to verify the logic of this method
 */
public class ConfigurationLoggingInstructionSequenceReplacerClaude_matchedArgumentTest {

    /**
     * Creates a minimal ConfigurationLoggingInstructionSequenceReplacer instance for testing.
     * Uses minimal mocks to satisfy constructor requirements.
     */
    private ConfigurationLoggingInstructionSequenceReplacer createReplacer() {
        Constant[] patternConstants = new Constant[0];
        Instruction[] patternInstructions = new Instruction[0];
        Constant[] replacementConstants = new Constant[0];
        Instruction[] replacementInstructions = new Instruction[0];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        return new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                codeAttributeEditor);
    }

    /**
     * Invokes the protected matchedArgument method using reflection.
     */
    private int invokeMatchedArgument(ConfigurationLoggingInstructionSequenceReplacer replacer,
                                      Clazz clazz, Method method, CodeAttribute codeAttribute,
                                      int offset, int argument)
            throws Exception {
        java.lang.reflect.Method matchedArgumentMethod = ConfigurationLoggingInstructionSequenceReplacer.class
                .getDeclaredMethod("matchedArgument", Clazz.class, Method.class, CodeAttribute.class, int.class, int.class);
        matchedArgumentMethod.setAccessible(true);
        return (int) matchedArgumentMethod.invoke(replacer, clazz, method, codeAttribute, offset, argument);
    }

    /**
     * Tests matchedArgument with LOCAL_VARIABLE_INDEX_1.
     * Should return codeAttribute.u2maxLocals.
     */
    @Test
    public void testMatchedArgumentWithLocalVariableIndex1() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 5;

        // Act
        int result = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 10, LOCAL_VARIABLE_INDEX_1);

        // Assert
        assertEquals(5, result, "LOCAL_VARIABLE_INDEX_1 should return codeAttribute.u2maxLocals");
    }

    /**
     * Tests matchedArgument with LOCAL_VARIABLE_INDEX_2.
     * Should return codeAttribute.u2maxLocals + 1.
     */
    @Test
    public void testMatchedArgumentWithLocalVariableIndex2() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 5;

        // Act
        int result = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 10, LOCAL_VARIABLE_INDEX_2);

        // Assert
        assertEquals(6, result, "LOCAL_VARIABLE_INDEX_2 should return codeAttribute.u2maxLocals + 1");
    }

    /**
     * Tests matchedArgument with LOCAL_VARIABLE_INDEX_3.
     * Should return codeAttribute.u2maxLocals + 2.
     */
    @Test
    public void testMatchedArgumentWithLocalVariableIndex3() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 5;

        // Act
        int result = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 10, LOCAL_VARIABLE_INDEX_3);

        // Assert
        assertEquals(7, result, "LOCAL_VARIABLE_INDEX_3 should return codeAttribute.u2maxLocals + 2");
    }

    /**
     * Tests matchedArgument with zero maxLocals for LOCAL_VARIABLE_INDEX_1.
     * Should return 0.
     */
    @Test
    public void testMatchedArgumentWithZeroMaxLocalsIndex1() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 0;

        // Act
        int result = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1);

        // Assert
        assertEquals(0, result, "LOCAL_VARIABLE_INDEX_1 with u2maxLocals=0 should return 0");
    }

    /**
     * Tests matchedArgument with zero maxLocals for LOCAL_VARIABLE_INDEX_2.
     * Should return 1.
     */
    @Test
    public void testMatchedArgumentWithZeroMaxLocalsIndex2() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 0;

        // Act
        int result = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2);

        // Assert
        assertEquals(1, result, "LOCAL_VARIABLE_INDEX_2 with u2maxLocals=0 should return 1");
    }

    /**
     * Tests matchedArgument with zero maxLocals for LOCAL_VARIABLE_INDEX_3.
     * Should return 2.
     */
    @Test
    public void testMatchedArgumentWithZeroMaxLocalsIndex3() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 0;

        // Act
        int result = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3);

        // Assert
        assertEquals(2, result, "LOCAL_VARIABLE_INDEX_3 with u2maxLocals=0 should return 2");
    }

    /**
     * Tests matchedArgument with large maxLocals value.
     */
    @Test
    public void testMatchedArgumentWithLargeMaxLocals() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 1000;

        // Act & Assert
        assertEquals(1000, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1));
        assertEquals(1001, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2));
        assertEquals(1002, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3));
    }

    /**
     * Tests matchedArgument with maximum possible maxLocals value (u2 = 65535).
     */
    @Test
    public void testMatchedArgumentWithMaximumMaxLocals() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 65535; // Maximum value for u2

        // Act & Assert
        assertEquals(65535, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1));
        assertEquals(65536, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2));
        assertEquals(65537, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3));
    }

    /**
     * Tests matchedArgument with different offset values (offset should not affect the result).
     */
    @Test
    public void testMatchedArgumentWithDifferentOffsets() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 10;

        // Act & Assert - offset should not affect the result
        assertEquals(10, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1));
        assertEquals(10, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 100, LOCAL_VARIABLE_INDEX_1));
        assertEquals(10, invokeMatchedArgument(replacer, clazz, method, codeAttribute, -1, LOCAL_VARIABLE_INDEX_1));

        assertEquals(11, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2));
        assertEquals(11, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 999, LOCAL_VARIABLE_INDEX_2));
    }

    /**
     * Tests matchedArgument with various maxLocals values to ensure consistent calculation.
     */
    @Test
    public void testMatchedArgumentWithVariousMaxLocals() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Test with different maxLocals values
        int[] maxLocalsValues = {1, 2, 3, 10, 50, 100, 255, 256, 512, 1024};

        for (int maxLocals : maxLocalsValues) {
            codeAttribute.u2maxLocals = maxLocals;

            // Act & Assert
            assertEquals(maxLocals, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1),
                    "LOCAL_VARIABLE_INDEX_1 should return " + maxLocals);
            assertEquals(maxLocals + 1, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2),
                    "LOCAL_VARIABLE_INDEX_2 should return " + (maxLocals + 1));
            assertEquals(maxLocals + 2, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3),
                    "LOCAL_VARIABLE_INDEX_3 should return " + (maxLocals + 2));
        }
    }

    /**
     * Tests that matchedArgument returns consistent values when called multiple times.
     */
    @Test
    public void testMatchedArgumentConsistency() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 7;

        // Act - Call multiple times
        int result1 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1);
        int result2 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1);
        int result3 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1);

        // Assert - Results should be consistent
        assertEquals(7, result1);
        assertEquals(result1, result2, "Multiple calls should return consistent results");
        assertEquals(result2, result3, "Multiple calls should return consistent results");
    }

    /**
     * Tests matchedArgument with all three index constants in sequence.
     */
    @Test
    public void testMatchedArgumentAllIndicesInSequence() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 15;

        // Act
        int index1 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1);
        int index2 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2);
        int index3 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3);

        // Assert
        assertEquals(15, index1);
        assertEquals(16, index2);
        assertEquals(17, index3);
        assertEquals(index1 + 1, index2, "INDEX_2 should be INDEX_1 + 1");
        assertEquals(index1 + 2, index3, "INDEX_3 should be INDEX_1 + 2");
    }

    /**
     * Tests matchedArgument with boundary value for u2maxLocals (just below maximum).
     */
    @Test
    public void testMatchedArgumentWithNearMaximumMaxLocals() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 65533; // Near maximum

        // Act & Assert
        assertEquals(65533, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1));
        assertEquals(65534, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2));
        assertEquals(65535, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3));
    }

    /**
     * Tests matchedArgument verifies that the offset parameter is not used in the calculation
     * for local variable index arguments.
     */
    @Test
    public void testMatchedArgumentOffsetNotUsedForLocalVariableIndices() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 8;

        // Act & Assert - Different offsets should not affect results for local variable indices
        int[] offsets = {0, 1, 10, 100, -1, -10, Integer.MAX_VALUE, Integer.MIN_VALUE};

        for (int offset : offsets) {
            assertEquals(8, invokeMatchedArgument(replacer, clazz, method, codeAttribute, offset, LOCAL_VARIABLE_INDEX_1),
                    "LOCAL_VARIABLE_INDEX_1 should return 8 regardless of offset " + offset);
            assertEquals(9, invokeMatchedArgument(replacer, clazz, method, codeAttribute, offset, LOCAL_VARIABLE_INDEX_2),
                    "LOCAL_VARIABLE_INDEX_2 should return 9 regardless of offset " + offset);
            assertEquals(10, invokeMatchedArgument(replacer, clazz, method, codeAttribute, offset, LOCAL_VARIABLE_INDEX_3),
                    "LOCAL_VARIABLE_INDEX_3 should return 10 regardless of offset " + offset);
        }
    }

    /**
     * Tests that different CodeAttribute instances with same maxLocals return same values.
     */
    @Test
    public void testMatchedArgumentWithDifferentCodeAttributeInstances() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);

        CodeAttribute codeAttribute1 = mock(CodeAttribute.class);
        codeAttribute1.u2maxLocals = 20;

        CodeAttribute codeAttribute2 = mock(CodeAttribute.class);
        codeAttribute2.u2maxLocals = 20;

        // Act
        int result1 = invokeMatchedArgument(replacer, clazz, method, codeAttribute1, 0, LOCAL_VARIABLE_INDEX_1);
        int result2 = invokeMatchedArgument(replacer, clazz, method, codeAttribute2, 0, LOCAL_VARIABLE_INDEX_1);

        // Assert
        assertEquals(20, result1);
        assertEquals(result1, result2, "Different CodeAttribute instances with same maxLocals should return same value");
    }

    /**
     * Tests matchedArgument with typical realistic maxLocals values.
     * Most methods have a small number of local variables.
     */
    @Test
    public void testMatchedArgumentWithRealisticMaxLocals() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Test with typical method local variable counts
        int[] realisticValues = {1, 2, 3, 4, 5, 8, 10, 15, 20};

        for (int maxLocals : realisticValues) {
            codeAttribute.u2maxLocals = maxLocals;

            // Act & Assert
            assertEquals(maxLocals, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1));
            assertEquals(maxLocals + 1, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2));
            assertEquals(maxLocals + 2, invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3));
        }
    }

    /**
     * Tests matchedArgument to verify the calculation is purely additive.
     */
    @Test
    public void testMatchedArgumentCalculationIsAdditive() throws Exception {
        // Arrange
        ConfigurationLoggingInstructionSequenceReplacer replacer = createReplacer();
        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        codeAttribute.u2maxLocals = 42;

        // Act
        int base = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_1);
        int plus1 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_2);
        int plus2 = invokeMatchedArgument(replacer, clazz, method, codeAttribute, 0, LOCAL_VARIABLE_INDEX_3);

        // Assert - Verify additive relationship
        assertEquals(base, 42);
        assertEquals(plus1, base + 1);
        assertEquals(plus2, base + 2);
        assertEquals(plus2 - plus1, 1, "Difference between INDEX_3 and INDEX_2 should be 1");
        assertEquals(plus1 - base, 1, "Difference between INDEX_2 and INDEX_1 should be 1");
    }
}
