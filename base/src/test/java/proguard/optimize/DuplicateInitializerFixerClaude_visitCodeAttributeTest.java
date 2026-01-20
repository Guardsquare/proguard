package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.VersionConstants;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateInitializerFixer#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
 *
 * The visitCodeAttribute method ensures that the CodeAttribute's maxLocals value is at least
 * as large as the minimum required for the method's parameters. This is necessary when a new
 * parameter is added to a duplicate initializer method.
 *
 * The method:
 * 1. Calculates the minimum variable size based on method parameters using ClassUtil.internalMethodParameterSize
 * 2. If the current maxLocals is less than this minimum, updates maxLocals to the required value
 * 3. If maxLocals is already sufficient, leaves it unchanged
 */
public class DuplicateInitializerFixerClaude_visitCodeAttributeTest {

    private DuplicateInitializerFixer fixer;
    private MemberVisitor extraFixedInitializerVisitor;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        extraFixedInitializerVisitor = mock(MemberVisitor.class);
        fixer = new DuplicateInitializerFixer(extraFixedInitializerVisitor);

        // Create a basic program class with proper initialization
        programClass = new ProgramClass();
        programClass.u4version = VersionConstants.CLASS_VERSION_1_8;
        programClass.constantPool = new proguard.classfile.constant.Constant[10];
    }

    /**
     * Tests that visitCodeAttribute updates maxLocals when it is less than the required parameter size.
     * For an instance method (I)V (one int parameter), the required size is 2 (this + int).
     */
    @Test
    public void testVisitCodeAttribute_maxLocalsLessThanRequired_updatesMaxLocals() {
        // Arrange - Create method with descriptor (I)V (one int parameter)
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1; // Too small - needs at least 2 (this + int)

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 2 (this + int parameter)
        assertEquals(2, codeAttribute.u2maxLocals);
    }

    /**
     * Tests that visitCodeAttribute does not change maxLocals when it is already sufficient.
     * If maxLocals is already larger than required, it should remain unchanged.
     */
    @Test
    public void testVisitCodeAttribute_maxLocalsAlreadySufficient_doesNotChange() {
        // Arrange - Create method with descriptor (I)V (one int parameter)
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 10; // Already sufficient

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should remain unchanged
        assertEquals(10, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a static method that has no parameters.
     * Static method ()V requires 0 locals.
     */
    @Test
    public void testVisitCodeAttribute_staticMethodNoParameters_maxLocalsZero() {
        // Arrange - Create static method with descriptor ()V (no parameters)
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.STATIC);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 0;

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should remain 0 as no parameters are needed
        assertEquals(0, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a static method that has one int parameter.
     * Static method (I)V requires 1 local (just the int, no 'this').
     */
    @Test
    public void testVisitCodeAttribute_staticMethodOneIntParameter_maxLocalsOne() {
        // Arrange - Create static method with descriptor (I)V (one int parameter)
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", AccessConstants.STATIC);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 0; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 1
        assertEquals(1, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a method that has multiple parameters of different types.
     * Instance method (IJD)V requires: 1 (this) + 1 (int) + 2 (long) + 2 (double) = 6 locals.
     */
    @Test
    public void testVisitCodeAttribute_multipleParametersDifferentTypes_calculatesCorrectSize() {
        // Arrange - Create method with descriptor (IJD)V (int, long, double)
        ProgramMethod programMethod = createMethod("testMethod", "(IJD)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 3; // Too small - needs 6

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 6
        assertEquals(6, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a method that has long and double parameters.
     * Long and double take 2 local variable slots each.
     */
    @Test
    public void testVisitCodeAttribute_longAndDoubleParameters_countsDoubleSlots() {
        // Arrange - Create method with descriptor (JD)V (long, double)
        ProgramMethod programMethod = createMethod("testMethod", "(JD)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 2; // Too small - needs 5 (this + long + double)

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 5 (1 + 2 + 2)
        assertEquals(5, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with an instance method with no parameters.
     * Instance method ()V requires 1 local for 'this'.
     */
    @Test
    public void testVisitCodeAttribute_instanceMethodNoParameters_requiresThisSlot() {
        // Arrange - Create instance method with descriptor ()V
        ProgramMethod programMethod = createMethod("testMethod", "()V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 0; // Too small - needs at least 1 for 'this'

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 1 for 'this'
        assertEquals(1, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a method that has many small parameters.
     * Instance method (IIIIII)V with 6 int parameters requires 7 locals (this + 6 ints).
     */
    @Test
    public void testVisitCodeAttribute_manySmallParameters_sumsCorrectly() {
        // Arrange - Create method with descriptor (IIIIII)V (6 int parameters)
        ProgramMethod programMethod = createMethod("testMethod", "(IIIIII)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 4; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 7 (this + 6 ints)
        assertEquals(7, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a method that has object reference parameters.
     * Object references take 1 slot like primitives (except long/double).
     */
    @Test
    public void testVisitCodeAttribute_objectParameters_countsSingleSlot() {
        // Arrange - Create method with descriptor (Ljava/lang/String;Ljava/lang/Object;)V
        ProgramMethod programMethod = createMethod("testMethod", "(Ljava/lang/String;Ljava/lang/Object;)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 3 (this + String + Object)
        assertEquals(3, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a method that has array parameters.
     * Array references take 1 slot like object references.
     */
    @Test
    public void testVisitCodeAttribute_arrayParameters_countsSingleSlot() {
        // Arrange - Create method with descriptor ([I[Ljava/lang/String;)V (int array, String array)
        ProgramMethod programMethod = createMethod("testMethod", "([I[Ljava/lang/String;)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be updated to 3 (this + int[] + String[])
        assertEquals(3, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute when maxLocals equals the required size exactly.
     * Should remain unchanged.
     */
    @Test
    public void testVisitCodeAttribute_maxLocalsEqualsRequired_remainsUnchanged() {
        // Arrange - Create method with descriptor (II)V (two int parameters)
        ProgramMethod programMethod = createMethod("testMethod", "(II)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 3; // Exactly right (this + 2 ints)

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should remain 3
        assertEquals(3, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with mixed parameter types including long and double.
     * Instance method (ILjava/lang/String;JD)V requires complex slot calculation.
     */
    @Test
    public void testVisitCodeAttribute_mixedParameterTypes_calculatesCorrectly() {
        // Arrange - Create method with mixed types: int, String, long, double
        ProgramMethod programMethod = createMethod("testMethod", "(ILjava/lang/String;JD)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 3; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be 7: this(1) + int(1) + String(1) + long(2) + double(2) = 7
        assertEquals(7, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with boolean, byte, char, short parameters.
     * These all take 1 slot like int.
     */
    @Test
    public void testVisitCodeAttribute_smallPrimitiveTypes_countsSingleSlotEach() {
        // Arrange - Create method with descriptor (ZBCS)V (boolean, byte, char, short)
        ProgramMethod programMethod = createMethod("testMethod", "(ZBCS)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 2; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be 5 (this + 4 small primitives)
        assertEquals(5, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a float parameter.
     * Float takes 1 slot.
     */
    @Test
    public void testVisitCodeAttribute_floatParameter_countsSingleSlot() {
        // Arrange - Create method with descriptor (F)V (float)
        ProgramMethod programMethod = createMethod("testMethod", "(F)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be 2 (this + float)
        assertEquals(2, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute does not interact with the extra visitor.
     * The method only modifies the CodeAttribute and should not trigger any visitor callbacks.
     */
    @Test
    public void testVisitCodeAttribute_doesNotTriggerExtraVisitor() {
        // Arrange
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", 0);
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1;

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - verify the extra visitor was not invoked
        verifyNoInteractions(extraFixedInitializerVisitor);
    }

    /**
     * Tests visitCodeAttribute with a fixer that has no extra visitor.
     * Should work without issues.
     */
    @Test
    public void testVisitCodeAttribute_withNullExtraVisitor_worksCorrectly() {
        // Arrange - Create fixer without extra visitor
        DuplicateInitializerFixer fixerWithNullVisitor = new DuplicateInitializerFixer();
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1;

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> fixerWithNullVisitor.visitCodeAttribute(programClass, programMethod, codeAttribute));
        assertEquals(2, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute can be called multiple times with different CodeAttributes.
     * Each call should be independent.
     */
    @Test
    public void testVisitCodeAttribute_multipleCallsWithDifferentAttributes_independentUpdates() {
        // Arrange
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", 0);

        CodeAttribute codeAttribute1 = new CodeAttribute();
        codeAttribute1.u2maxLocals = 1;

        CodeAttribute codeAttribute2 = new CodeAttribute();
        codeAttribute2.u2maxLocals = 5;

        CodeAttribute codeAttribute3 = new CodeAttribute();
        codeAttribute3.u2maxLocals = 0;

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute1);
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute2);
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute3);

        // Assert - each should be updated independently
        assertEquals(2, codeAttribute1.u2maxLocals); // Updated from 1
        assertEquals(5, codeAttribute2.u2maxLocals); // Unchanged (already sufficient)
        assertEquals(2, codeAttribute3.u2maxLocals); // Updated from 0
    }

    /**
     * Tests visitCodeAttribute with the same CodeAttribute called multiple times.
     * Should be idempotent.
     */
    @Test
    public void testVisitCodeAttribute_repeatedCallsSameAttribute_idempotent() {
        // Arrange
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", 0);
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1;

        // Act - call multiple times
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - should only be set once, remain at 2
        assertEquals(2, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a static method with multiple parameters including long/double.
     * Static method (IJD)V requires: int(1) + long(2) + double(2) = 5 locals (no 'this').
     */
    @Test
    public void testVisitCodeAttribute_staticMethodMultipleParams_noThisParameter() {
        // Arrange - Create static method with descriptor (IJD)V
        ProgramMethod programMethod = createMethod("testMethod", "(IJD)V", AccessConstants.STATIC);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 2; // Too small

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be 5 (no 'this' for static)
        assertEquals(5, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a method returning a value (not void).
     * Return type should not affect maxLocals calculation.
     */
    @Test
    public void testVisitCodeAttribute_nonVoidReturnType_doesNotAffectMaxLocals() {
        // Arrange - Create method with descriptor (I)Ljava/lang/String; (returns String)
        ProgramMethod programMethod = createMethod("testMethod", "(I)Ljava/lang/String;", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1;

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be 2 (this + int), return type doesn't matter
        assertEquals(2, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with multi-dimensional array parameters.
     * Multi-dimensional arrays still take only 1 slot.
     */
    @Test
    public void testVisitCodeAttribute_multiDimensionalArrayParameter_countsSingleSlot() {
        // Arrange - Create method with descriptor ([[I)V (2D int array)
        ProgramMethod programMethod = createMethod("testMethod", "([[I)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1;

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be 2 (this + array reference)
        assertEquals(2, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a very large maxLocals that is already sufficient.
     * Should not be reduced even if it's larger than necessary.
     */
    @Test
    public void testVisitCodeAttribute_veryLargeMaxLocals_notReduced() {
        // Arrange - Create method with descriptor (I)V
        ProgramMethod programMethod = createMethod("testMethod", "(I)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1000; // Very large but valid

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - should remain unchanged
        assertEquals(1000, codeAttribute.u2maxLocals);
    }

    /**
     * Tests visitCodeAttribute with a constructor method (init).
     * Constructors are instance methods, so 'this' should be counted.
     */
    @Test
    public void testVisitCodeAttribute_constructorMethod_includesThisParameter() {
        // Arrange - Create constructor with descriptor (I)V
        ProgramMethod programMethod = createMethod("<init>", "(I)V", 0);

        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxLocals = 1;

        // Act
        fixer.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - maxLocals should be 2 (this + int)
        assertEquals(2, codeAttribute.u2maxLocals);
    }

    /**
     * Helper method to create a ProgramMethod with the specified name, descriptor, and access flags.
     */
    private ProgramMethod createMethod(String name, String descriptor, int accessFlags) {
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = accessFlags;

        // Set up constant pool entries for name and descriptor
        int nameIndex = programClass.constantPool.length - 2;
        int descriptorIndex = programClass.constantPool.length - 1;

        programClass.constantPool[nameIndex] = new proguard.classfile.constant.Utf8Constant(name);
        programClass.constantPool[descriptorIndex] = new proguard.classfile.constant.Utf8Constant(descriptor);

        programMethod.u2nameIndex = nameIndex;
        programMethod.u2descriptorIndex = descriptorIndex;

        return programMethod;
    }
}
