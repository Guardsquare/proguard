package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.visitor.ParameterVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UsedParameterFilter#visitParameter(Clazz, Member, int, int, int, int, String, Clazz)}.
 *
 * The visitParameter method filters parameter visits based on whether the parameter is marked as used.
 * It delegates to ParameterUsageMarker.isParameterUsed() to determine if a parameter is used, then:
 * - If the parameter IS used: delegates to usedParameterVisitor (if not null)
 * - If the parameter is NOT used: delegates to unusedParameterVisitor (if not null)
 *
 * Key behavior:
 * - Uses parameterOffset to check if parameter is used
 * - Member parameter must be castable to Method
 * - Null visitors are handled gracefully (no delegation occurs)
 * - All parameters are passed through to the delegated visitor
 *
 * Parameter indices explained:
 * - parameterIndex: the logical parameter number (0-based, excluding 'this')
 * - parameterOffset: the actual variable slot index (includes 'this', accounts for category-2 types)
 * - parameterSize: 1 for most types, 2 for long/double
 */
public class UsedParameterFilterClaude_visitParameterTest {

    private ProgramClass testClass;
    private TrackingParameterVisitor usedVisitor;
    private TrackingParameterVisitor unusedVisitor;
    private UsedParameterFilter filter;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
        usedVisitor = new TrackingParameterVisitor();
        unusedVisitor = new TrackingParameterVisitor();
        filter = new UsedParameterFilter(usedVisitor, unusedVisitor);
    }

    /**
     * Tests that visitParameter delegates to usedParameterVisitor when parameter is used.
     */
    @Test
    public void testVisitParameter_withUsedParameter_delegatesToUsedVisitor() {
        // Arrange - create method and mark parameter 1 as used
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called");
        assertEquals(1, usedVisitor.visitCount, "Used visitor should be called once");
        assertFalse(unusedVisitor.visited, "Unused visitor should not be called");
        assertEquals(0, unusedVisitor.visitCount, "Unused visitor should not be called");
    }

    /**
     * Tests that visitParameter delegates to unusedParameterVisitor when parameter is not used.
     */
    @Test
    public void testVisitParameter_withUnusedParameter_delegatesToUnusedVisitor() {
        // Arrange - create method, do NOT mark parameter as used
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);

        // Assert
        assertFalse(usedVisitor.visited, "Used visitor should not be called");
        assertEquals(0, usedVisitor.visitCount, "Used visitor should not be called");
        assertTrue(unusedVisitor.visited, "Unused visitor should be called");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor should be called once");
    }

    /**
     * Tests that visitParameter passes all parameters correctly to the delegate visitor.
     */
    @Test
    public void testVisitParameter_passesAllParametersToDelegate() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        ProgramClass referencedClass = new ProgramClass();

        // Act
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "Ljava/lang/String;", referencedClass);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called");
        assertSame(testClass, usedVisitor.lastClazz, "Should pass correct clazz");
        assertSame(method, usedVisitor.lastMember, "Should pass correct member");
        assertEquals(0, usedVisitor.lastParameterIndex, "Should pass correct parameterIndex");
        assertEquals(1, usedVisitor.lastParameterCount, "Should pass correct parameterCount");
        assertEquals(1, usedVisitor.lastParameterOffset, "Should pass correct parameterOffset");
        assertEquals(1, usedVisitor.lastParameterSize, "Should pass correct parameterSize");
        assertEquals("Ljava/lang/String;", usedVisitor.lastParameterType, "Should pass correct parameterType");
        assertSame(referencedClass, usedVisitor.lastReferencedClass, "Should pass correct referencedClass");
    }

    /**
     * Tests visitParameter with null usedParameterVisitor does not throw when parameter is used.
     */
    @Test
    public void testVisitParameter_withNullUsedVisitor_doesNotThrow() {
        // Arrange
        UsedParameterFilter filterWithNullUsed = new UsedParameterFilter(null, unusedVisitor);
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertDoesNotThrow(() -> {
            filterWithNullUsed.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        }, "Should not throw when usedParameterVisitor is null");

        assertFalse(unusedVisitor.visited, "Unused visitor should not be called");
    }

    /**
     * Tests visitParameter with null unusedParameterVisitor does not throw when parameter is unused.
     */
    @Test
    public void testVisitParameter_withNullUnusedVisitor_doesNotThrow() {
        // Arrange
        UsedParameterFilter filterWithNullUnused = new UsedParameterFilter(usedVisitor, null);
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        // Do NOT mark parameter as used

        // Act & Assert
        assertDoesNotThrow(() -> {
            filterWithNullUnused.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        }, "Should not throw when unusedParameterVisitor is null");

        assertFalse(usedVisitor.visited, "Used visitor should not be called");
    }

    /**
     * Tests visitParameter with both visitors null does not throw.
     */
    @Test
    public void testVisitParameter_withBothVisitorsNull_doesNotThrow() {
        // Arrange
        UsedParameterFilter filterWithNullBoth = new UsedParameterFilter(null, null);
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertDoesNotThrow(() -> {
            filterWithNullBoth.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        }, "Should not throw when both visitors are null");
    }

    /**
     * Tests visitParameter with multiple parameters, some used and some unused.
     */
    @Test
    public void testVisitParameter_withMixedParameters_delegatesCorrectly() {
        // Arrange - method with 3 parameters (plus 'this')
        // (I, I, I)V -> 'this' at 0, param0 at 1, param1 at 2, param2 at 3
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(III)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark only parameter at offset 1 and 3 as used
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 3);

        // Act - visit all three parameters
        filter.visitParameter(testClass, method, 0, 3, 1, 1, "I", null); // param0
        filter.visitParameter(testClass, method, 1, 3, 2, 1, "I", null); // param1
        filter.visitParameter(testClass, method, 2, 3, 3, 1, "I", null); // param2

        // Assert
        assertEquals(2, usedVisitor.visitCount, "Used visitor should be called twice (params 0 and 2)");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor should be called once (param 1)");
    }

    /**
     * Tests visitParameter with long parameter (category-2 type).
     */
    @Test
    public void testVisitParameter_withLongParameter_usesParameterOffset() {
        // Arrange - method with long parameter
        // (J)V -> 'this' at 0, long at 1-2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(J)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1); // Mark first slot of long as used

        // Act - visit long parameter (parameterSize=2, parameterOffset=1)
        filter.visitParameter(testClass, method, 0, 1, 1, 2, "J", null);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called for used long parameter");
        assertEquals(2, usedVisitor.lastParameterSize, "Should pass size 2 for long");
        assertEquals(1, usedVisitor.lastParameterOffset, "Should pass offset 1 for long");
    }

    /**
     * Tests visitParameter with double parameter (category-2 type).
     */
    @Test
    public void testVisitParameter_withDoubleParameter_usesParameterOffset() {
        // Arrange - method with double parameter
        // (D)V -> 'this' at 0, double at 1-2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(D)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1); // Mark first slot of double as used

        // Act - visit double parameter (parameterSize=2, parameterOffset=1)
        filter.visitParameter(testClass, method, 0, 1, 1, 2, "D", null);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called for used double parameter");
        assertEquals(2, usedVisitor.lastParameterSize, "Should pass size 2 for double");
        assertEquals(1, usedVisitor.lastParameterOffset, "Should pass offset 1 for double");
    }

    /**
     * Tests visitParameter with static method (no 'this' parameter).
     */
    @Test
    public void testVisitParameter_withStaticMethod_worksCorrectly() {
        // Arrange - static method with int parameter
        // static (I)V -> param at offset 0
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act
        filter.visitParameter(testClass, method, 0, 1, 0, 1, "I", null);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called");
        assertEquals(0, usedVisitor.lastParameterOffset, "Should use offset 0 for first param in static method");
    }

    /**
     * Tests visitParameter visits same parameter multiple times.
     */
    @Test
    public void testVisitParameter_calledMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act - visit same parameter 3 times
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);

        // Assert
        assertEquals(3, usedVisitor.visitCount, "Used visitor should be called 3 times");
    }

    /**
     * Tests visitParameter with array parameter type.
     */
    @Test
    public void testVisitParameter_withArrayParameter_delegatesCorrectly() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "([I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "[I", null);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called");
        assertEquals("[I", usedVisitor.lastParameterType, "Should pass correct array type");
    }

    /**
     * Tests visitParameter with object parameter type.
     */
    @Test
    public void testVisitParameter_withObjectParameter_delegatesCorrectly() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        ProgramClass objectClass = new ProgramClass();

        // Act
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "Ljava/lang/Object;", objectClass);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called");
        assertEquals("Ljava/lang/Object;", usedVisitor.lastParameterType, "Should pass correct object type");
        assertSame(objectClass, usedVisitor.lastReferencedClass, "Should pass referenced class");
    }

    /**
     * Tests visitParameter with method having many parameters.
     */
    @Test
    public void testVisitParameter_withManyParameters_delegatesAllCorrectly() {
        // Arrange - method with 5 parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IIIIII)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters 1, 3, 5 as used (offsets in instance method)
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 3);
        ParameterUsageMarker.markParameterUsed(method, 5);

        // Act - visit all 5 parameters
        for (int i = 0; i < 5; i++) {
            filter.visitParameter(testClass, method, i, 5, i + 1, 1, "I", null);
        }

        // Assert
        assertEquals(3, usedVisitor.visitCount, "Used visitor should be called 3 times");
        assertEquals(2, unusedVisitor.visitCount, "Unused visitor should be called 2 times");
    }

    /**
     * Tests visitParameter with mixed parameter types.
     */
    @Test
    public void testVisitParameter_withMixedTypes_delegatesCorrectly() {
        // Arrange - (I, J, Ljava/lang/String;)V
        // 'this' at 0, int at 1, long at 2-3, String at 4
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IJLjava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark int and String as used
        ParameterUsageMarker.markParameterUsed(method, 1); // int
        ParameterUsageMarker.markParameterUsed(method, 4); // String

        // Act
        filter.visitParameter(testClass, method, 0, 3, 1, 1, "I", null);
        filter.visitParameter(testClass, method, 1, 3, 2, 2, "J", null);
        filter.visitParameter(testClass, method, 2, 3, 4, 1, "Ljava/lang/String;", null);

        // Assert
        assertEquals(2, usedVisitor.visitCount, "Used visitor should be called for int and String");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor should be called for long");
    }

    /**
     * Tests visitParameter checks parameter usage via parameterOffset not parameterIndex.
     */
    @Test
    public void testVisitParameter_usesParameterOffsetNotIndex() {
        // Arrange - static method (IJ)V
        // int at offset 0, long at offset 1-2
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(IJ)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark only the long parameter (offset 1) as used
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act - visit int (index 0, offset 0) and long (index 1, offset 1)
        filter.visitParameter(testClass, method, 0, 2, 0, 1, "I", null);
        filter.visitParameter(testClass, method, 1, 2, 1, 2, "J", null);

        // Assert
        assertEquals(1, usedVisitor.visitCount, "Used visitor should be called once (for long)");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor should be called once (for int)");
    }

    /**
     * Tests visitParameter with no parameters (only 'this').
     */
    @Test
    public void testVisitParameter_withNoParameters_canVisitThis() {
        // Arrange - method ()V with just 'this'
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act - visiting 'this' would typically not happen through normal parameter visiting
        // but the method should handle it
        filter.visitParameter(testClass, method, 0, 0, 0, 1, "Ljava/lang/Object;", testClass);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called for 'this'");
    }

    /**
     * Tests visitParameter delegates to correct visitor based on runtime parameter usage state.
     */
    @Test
    public void testVisitParameter_checksUsageAtCallTime() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        // Initially parameter is not used

        // Act - visit when unused
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        assertEquals(0, usedVisitor.visitCount, "Used visitor should not be called");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor should be called");

        // Now mark parameter as used
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act - visit when used
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);

        // Assert
        assertEquals(1, usedVisitor.visitCount, "Used visitor should be called after marking");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor count should remain 1");
    }

    /**
     * Tests visitParameter with different methods using same filter.
     */
    @Test
    public void testVisitParameter_withDifferentMethods_delegatesIndependently() {
        // Arrange
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ParameterUsageMarker.markParameterUsed(method1, 1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        // Do not mark method2's parameter as used

        // Act
        filter.visitParameter(testClass, method1, 0, 1, 1, 1, "I", null);
        filter.visitParameter(testClass, method2, 0, 1, 1, 1, "I", null);

        // Assert
        assertEquals(1, usedVisitor.visitCount, "Used visitor should be called for method1");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor should be called for method2");
    }

    /**
     * Tests visitParameter does not throw with null clazz parameter.
     */
    @Test
    public void testVisitParameter_withNullClazz_doesNotThrow() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertDoesNotThrow(() -> {
            filter.visitParameter(null, method, 0, 1, 1, 1, "I", null);
        }, "Should not throw with null clazz");

        assertTrue(usedVisitor.visited, "Should still delegate to visitor");
        assertNull(usedVisitor.lastClazz, "Should pass null clazz to visitor");
    }

    /**
     * Tests visitParameter with null referencedClass parameter.
     */
    @Test
    public void testVisitParameter_withNullReferencedClass_delegatesCorrectly() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);

        // Assert
        assertTrue(usedVisitor.visited, "Used visitor should be called");
        assertNull(usedVisitor.lastReferencedClass, "Should pass null referencedClass");
    }

    /**
     * Tests visitParameter with primitive types (all category-1 except long/double).
     */
    @Test
    public void testVisitParameter_withPrimitiveTypes_delegatesCorrectly() {
        // Test byte, short, char, int, float, boolean
        String[] descriptors = {"(B)V", "(S)V", "(C)V", "(I)V", "(F)V", "(Z)V"};
        String[] types = {"B", "S", "C", "I", "F", "Z"};

        for (int i = 0; i < descriptors.length; i++) {
            // Arrange
            setUp(); // Reset visitors
            ProgramMethod method = createMethodWithDescriptor(testClass, "test" + i, descriptors[i]);
            ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
            ParameterUsageMarker.markParameterUsed(method, 1);

            // Act
            filter.visitParameter(testClass, method, 0, 1, 1, 1, types[i], null);

            // Assert
            assertTrue(usedVisitor.visited, "Used visitor should be called for type " + types[i]);
            assertEquals(types[i], usedVisitor.lastParameterType, "Should pass correct type");
        }
    }

    /**
     * Tests visitParameter behavior is consistent across multiple calls with same parameters.
     */
    @Test
    public void testVisitParameter_consistentBehavior() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act - call 10 times
        for (int i = 0; i < 10; i++) {
            filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        }

        // Assert
        assertEquals(10, usedVisitor.visitCount, "Used visitor should be called 10 times");
        assertEquals(0, unusedVisitor.visitCount, "Unused visitor should never be called");
    }

    /**
     * Tests visitParameter with complex descriptor.
     */
    @Test
    public void testVisitParameter_withComplexDescriptor_delegatesCorrectly() {
        // Arrange - method with complex parameters
        // (Ljava/util/List;[Ljava/lang/String;Ljava/util/Map;)V
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/util/List;[Ljava/lang/String;Ljava/util/Map;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark List and Map as used
        ParameterUsageMarker.markParameterUsed(method, 1); // List
        ParameterUsageMarker.markParameterUsed(method, 3); // Map

        // Act
        filter.visitParameter(testClass, method, 0, 3, 1, 1, "Ljava/util/List;", null);
        filter.visitParameter(testClass, method, 1, 3, 2, 1, "[Ljava/lang/String;", null);
        filter.visitParameter(testClass, method, 2, 3, 3, 1, "Ljava/util/Map;", null);

        // Assert
        assertEquals(2, usedVisitor.visitCount, "Used visitor should be called for List and Map");
        assertEquals(1, unusedVisitor.visitCount, "Unused visitor should be called for String array");
    }

    /**
     * Tests visitParameter does not modify the filter's state.
     */
    @Test
    public void testVisitParameter_doesNotModifyFilterState() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act - multiple calls
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);
        filter.visitParameter(testClass, method, 0, 1, 1, 1, "I", null);

        // Assert - behavior should be identical
        assertEquals(2, usedVisitor.visitCount, "Each call should independently delegate");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Creates a ProgramClass with a minimal constant pool setup.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[100];
        constantPool[0] = null; // Index 0 is always null
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 100;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a static ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createStaticMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.STATIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Finds the next available index in the constant pool.
     */
    private int findNextAvailableConstantPoolIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("No available constant pool index");
    }

    /**
     * Helper class that tracks parameter visits.
     */
    private static class TrackingParameterVisitor implements ParameterVisitor {
        boolean visited = false;
        int visitCount = 0;
        Clazz lastClazz = null;
        Member lastMember = null;
        int lastParameterIndex = -1;
        int lastParameterCount = -1;
        int lastParameterOffset = -1;
        int lastParameterSize = -1;
        String lastParameterType = null;
        Clazz lastReferencedClass = null;

        @Override
        public void visitParameter(Clazz clazz, Member member, int parameterIndex,
                                  int parameterCount, int parameterOffset, int parameterSize,
                                  String parameterType, Clazz referencedClass) {
            visited = true;
            visitCount++;
            lastClazz = clazz;
            lastMember = member;
            lastParameterIndex = parameterIndex;
            lastParameterCount = parameterCount;
            lastParameterOffset = parameterOffset;
            lastParameterSize = parameterSize;
            lastParameterType = parameterType;
            lastReferencedClass = referencedClass;
        }
    }
}
