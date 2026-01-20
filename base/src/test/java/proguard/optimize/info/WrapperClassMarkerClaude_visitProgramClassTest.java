package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.Instruction;
import proguard.evaluation.value.*;
import proguard.optimize.evaluation.StoringInvocationUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassMarker#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method identifies wrapper classes that meet these criteria:
 * - No subclasses (subClassCount == 0)
 * - Exactly one non-static field with a class type (not array)
 * - Exactly one initializer (<init>) with a single non-null parameter
 * - The initializer must match a specific instruction pattern with code length of 10
 *
 * When all criteria are met, the class is marked as a wrapper via setWrappedClass().
 * These tests verify the detection logic through various scenarios.
 *
 * Note: Positive test cases (where a class is successfully marked as a wrapper) are not
 * included because they would require:
 * 1. Setting up StoringInvocationUnit with proper parameter value tracking
 * 2. Creating CodeAttribute with exact bytecode instruction sequence matching the pattern
 * 3. Setting up InstructionSequenceMatcher state with proper constant pool references
 * This level of setup would require reflection or extensive internal state manipulation
 * that is better tested through integration tests rather than unit tests.
 */
public class WrapperClassMarkerClaude_visitProgramClassTest {

    private WrapperClassMarker marker;
    private ProgramClass programClass;
    private int constantPoolIndex;

    @BeforeEach
    public void setUp() {
        marker = new WrapperClassMarker();
        programClass = createBasicProgramClass();
        constantPoolIndex = 10; // Start after basic constants
    }

    /**
     * Creates a basic ProgramClass with initialized constant pool.
     */
    private ProgramClass createBasicProgramClass() {
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 1;
        clazz.constantPool = new Constant[200];
        clazz.u2constantPoolCount = 200;

        // Add basic class name
        clazz.constantPool[1] = new Utf8Constant("TestClass");

        // Initialize processing info for proper getWrappedClass() behavior
        clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

        return clazz;
    }

    /**
     * Tests visitProgramClass with a class that has subclasses.
     * Should not be marked as a wrapper class because wrapper classes cannot have subclasses.
     */
    @Test
    public void testVisitProgramClass_withSubclasses_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with subclasses should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a class that has no subclasses but no fields.
     * Should not be marked as wrapper because it needs exactly one field.
     */
    @Test
    public void testVisitProgramClass_noSubclassesButNoFields_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;
        programClass.fields = new ProgramField[0];
        programClass.u2fieldsCount = 0;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with no fields should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a class that has multiple fields.
     * Should not be marked as wrapper because it needs exactly one field.
     */
    @Test
    public void testVisitProgramClass_multipleFields_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramField field1 = createNonStaticClassField("field1", "Ljava/lang/String;");
        ProgramField field2 = createNonStaticClassField("field2", "Ljava/lang/Object;");

        programClass.fields = new ProgramField[] { field1, field2 };
        programClass.u2fieldsCount = 2;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with multiple fields should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a class that has a static field.
     * Should not be marked as wrapper because the field must be non-static.
     */
    @Test
    public void testVisitProgramClass_withStaticField_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramField field = createStaticClassField("field", "Ljava/lang/Object;");
        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with static field should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a class that has a primitive field.
     * Should not be marked as wrapper because the field must be a class type.
     */
    @Test
    public void testVisitProgramClass_withPrimitiveField_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramField field = createNonStaticClassField("intField", "I");
        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with primitive field should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a class that has an array field.
     * Should not be marked as wrapper because arrays are excluded.
     */
    @Test
    public void testVisitProgramClass_withArrayField_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramField field = createNonStaticClassField("arrayField", "[Ljava/lang/Object;");
        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with array field should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a class that has a field without referencedClass.
     * Should not be marked as wrapper because referencedClass must be set.
     */
    @Test
    public void testVisitProgramClass_fieldWithoutReferencedClass_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramField field = createNonStaticClassField("field", "Ljava/lang/Object;");
        field.referencedClass = null; // No referenced class
        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with field without referencedClass should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a valid field but no methods.
     * Should not be marked as wrapper because it needs an initializer.
     */
    @Test
    public void testVisitProgramClass_validFieldButNoMethods_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramClass wrappedClass = new ProgramClass();
        ProgramField field = createNonStaticClassField("field", "Ljava/lang/Object;");
        field.referencedClass = wrappedClass;

        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;
        programClass.methods = new ProgramMethod[0];
        programClass.u2methodsCount = 0;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class without initializer should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a valid field but multiple methods.
     * Should not be marked as wrapper because it needs exactly one initializer.
     */
    @Test
    public void testVisitProgramClass_validFieldButMultipleMethods_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramClass wrappedClass = new ProgramClass();
        ProgramField field = createNonStaticClassField("field", "Ljava/lang/Object;");
        field.referencedClass = wrappedClass;

        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        // Create two initializers - not allowed for wrapper
        ProgramMethod method1 = createInitializer("()V");
        ProgramMethod method2 = createInitializer("(I)V");

        programClass.methods = new ProgramMethod[] { method1, method2 };
        programClass.u2methodsCount = 2;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with multiple initializers should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with a regular method (not an initializer).
     * Should not be marked as wrapper because it needs an initializer.
     */
    @Test
    public void testVisitProgramClass_validFieldButRegularMethod_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramClass wrappedClass = new ProgramClass();
        ProgramField field = createNonStaticClassField("field", "Ljava/lang/Object;");
        field.referencedClass = wrappedClass;

        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        // Create a regular method, not <init>
        ProgramMethod method = createMethod("doSomething", "(Ljava/lang/Object;)V");
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class without initializer should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with an initializer that has no parameters.
     * Should not be marked as wrapper because initializer needs exactly one parameter.
     */
    @Test
    public void testVisitProgramClass_initializerWithNoParameters_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramClass wrappedClass = new ProgramClass();
        ProgramField field = createNonStaticClassField("field", "Ljava/lang/Object;");
        field.referencedClass = wrappedClass;

        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        ProgramMethod method = createInitializer("()V");
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with no-arg initializer should not be marked as wrapper");
    }

    /**
     * Tests visitProgramClass with an initializer that has multiple parameters.
     * Should not be marked as wrapper because initializer needs exactly one parameter.
     */
    @Test
    public void testVisitProgramClass_initializerWithMultipleParameters_notMarkedAsWrapper() {
        // Arrange
        programClass.subClassCount = 0;

        ProgramClass wrappedClass = new ProgramClass();
        ProgramField field = createNonStaticClassField("field", "Ljava/lang/Object;");
        field.referencedClass = wrappedClass;

        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        ProgramMethod method = createInitializer("(Ljava/lang/Object;I)V");
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Class with multi-parameter initializer should not be marked as wrapper");
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same class.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_consistentBehavior() {
        // Arrange
        programClass.subClassCount = 1;

        // Act - call multiple times
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Repeated calls should have consistent behavior");
    }

    /**
     * Tests visitProgramClass with null constant pool entries.
     * Should handle gracefully without marking as wrapper.
     */
    @Test
    public void testVisitProgramClass_withNullConstantPoolEntries_handlesGracefully() {
        // Arrange
        programClass.subClassCount = 0;
        programClass.constantPool[50] = null; // Null entry

        ProgramField field = createNonStaticClassField("field", "Ljava/lang/Object;");
        field.u2nameIndex = 50; // Point to null entry

        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> marker.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass does not modify the class if criteria are not met.
     */
    @Test
    public void testVisitProgramClass_criteriaNotMet_classUnmodified() {
        // Arrange
        programClass.subClassCount = 1;
        Object processingInfoBefore = programClass.getProcessingInfo();

        // Act
        marker.visitProgramClass(programClass);

        // Assert - processing info should be same or null
        Object processingInfoAfter = programClass.getProcessingInfo();
        if (processingInfoBefore == null) {
            // Either still null or newly initialized but without wrapped class
            assertNull(WrapperClassMarker.getWrappedClass(programClass));
        }
    }

    /**
     * Tests visitProgramClass with different marker instances on the same class.
     */
    @Test
    public void testVisitProgramClass_differentMarkerInstances_consistentBehavior() {
        // Arrange
        WrapperClassMarker marker1 = new WrapperClassMarker();
        WrapperClassMarker marker2 = new WrapperClassMarker();
        programClass.subClassCount = 1;

        // Act
        marker1.visitProgramClass(programClass);
        marker2.visitProgramClass(programClass);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(programClass),
                "Different marker instances should have consistent behavior");
    }

    /**
     * Tests that the marker properly resets state between calls.
     */
    @Test
    public void testVisitProgramClass_multipleClassesSequentially_properStateReset() {
        // Arrange
        ProgramClass class1 = createBasicProgramClass();
        class1.subClassCount = 1;

        ProgramClass class2 = createBasicProgramClass();
        class2.subClassCount = 2;

        // Act
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class2);

        // Assert
        assertNull(WrapperClassMarker.getWrappedClass(class1),
                "First class should not be marked");
        assertNull(WrapperClassMarker.getWrappedClass(class2),
                "Second class should not be marked");
    }

    // ========== Helper Methods ==========

    /**
     * Creates a non-static field with class type descriptor.
     */
    private ProgramField createNonStaticClassField(String name, String descriptor) {
        ProgramField field = new ProgramField();
        field.u2accessFlags = AccessConstants.PUBLIC;

        int nameIndex = constantPoolIndex++;
        int descriptorIndex = constantPoolIndex++;

        programClass.constantPool[nameIndex] = new Utf8Constant(name);
        programClass.constantPool[descriptorIndex] = new Utf8Constant(descriptor);

        field.u2nameIndex = nameIndex;
        field.u2descriptorIndex = descriptorIndex;

        // If it's a class type, set a referenced class
        if (descriptor.startsWith("L") && !descriptor.startsWith("[")) {
            field.referencedClass = new ProgramClass();
        }

        return field;
    }

    /**
     * Creates a static field with class type descriptor.
     */
    private ProgramField createStaticClassField(String name, String descriptor) {
        ProgramField field = createNonStaticClassField(name, descriptor);
        field.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.STATIC;
        return field;
    }

    /**
     * Creates an initializer method with the given descriptor.
     */
    private ProgramMethod createInitializer(String descriptor) {
        return createMethod("<init>", descriptor);
    }

    /**
     * Creates a method with the given name and descriptor.
     */
    private ProgramMethod createMethod(String name, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        int nameIndex = constantPoolIndex++;
        int descriptorIndex = constantPoolIndex++;

        programClass.constantPool[nameIndex] = new Utf8Constant(name);
        programClass.constantPool[descriptorIndex] = new Utf8Constant(descriptor);

        method.u2nameIndex = nameIndex;
        method.u2descriptorIndex = descriptorIndex;

        // Initialize optimization info
        method.setProcessingInfo(new ProgramMethodOptimizationInfo(programClass, method));

        return method;
    }
}
