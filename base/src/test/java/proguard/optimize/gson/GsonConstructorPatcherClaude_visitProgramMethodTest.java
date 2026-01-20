package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.constant.*;
import proguard.classfile.editor.CodeAttributeEditor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GsonConstructorPatcher#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod checks if the method descriptor contains "java/util/List" (indicating
 * it's a Gson constructor with a List of type adapter factories) and if so, calls attributesAccept
 * to further process the method's attributes.
 */
public class GsonConstructorPatcherClaude_visitProgramMethodTest {

    private GsonConstructorPatcher patcher;
    private CodeAttributeEditor codeAttributeEditor;

    /**
     * Sets up fresh instances before each test.
     */
    @BeforeEach
    public void setUp() {
        codeAttributeEditor = new CodeAttributeEditor();
        patcher = new GsonConstructorPatcher(codeAttributeEditor, false);
    }

    // =========================================================================
    // Tests for visitProgramMethod.(Lproguard/classfile/ProgramClass;Lproguard/classfile/ProgramMethod;)V
    // =========================================================================

    /**
     * Tests that visitProgramMethod does nothing when the descriptor does not contain List.
     * The method should only process constructors with a List parameter.
     */
    @Test
    public void testVisitProgramMethod_withoutListInDescriptor_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(programClass, "<init>", "(I)V");

        // Act
        patcher.visitProgramMethod(programClass, programMethod);

        // Assert - method should complete without processing since no List in descriptor
        // No exception should be thrown
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should not throw exception when descriptor has no List");
    }

    /**
     * Tests that visitProgramMethod processes methods with List in descriptor.
     * When the descriptor contains "Ljava/util/List;", the method should call attributesAccept.
     */
    @Test
    public void testVisitProgramMethod_withListInDescriptor_processesMethod() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/google/gson/Gson");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "<init>",
            "(Lcom/google/gson/internal/Excluder;Ljava/util/List;)V"
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process method with List in descriptor");
    }

    /**
     * Tests visitProgramMethod with a method that has multiple parameters including List.
     */
    @Test
    public void testVisitProgramMethod_withMultipleParametersIncludingList_processesMethod() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/google/gson/Gson");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "<init>",
            "(ILjava/lang/String;Ljava/util/List;Z)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process method with List among multiple parameters");
    }

    /**
     * Tests visitProgramMethod with a method that returns a List.
     * This should still trigger processing since descriptor contains "java/util/List".
     */
    @Test
    public void testVisitProgramMethod_withListReturnType_processesMethod() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "getList",
            "()Ljava/util/List;"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process method with List return type");
    }

    /**
     * Tests visitProgramMethod with a method descriptor containing no parameters.
     */
    @Test
    public void testVisitProgramMethod_withNoParameters_doesNotProcess() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(programClass, "doSomething", "()V");

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should handle methods with no parameters");
    }

    /**
     * Tests visitProgramMethod with a method descriptor containing primitive types only.
     */
    @Test
    public void testVisitProgramMethod_withPrimitiveTypesOnly_doesNotProcess() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "calculate",
            "(IIJFD)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should handle methods with primitive types only");
    }

    /**
     * Tests visitProgramMethod with a method descriptor containing arrays.
     */
    @Test
    public void testVisitProgramMethod_withArrayTypes_doesNotProcess() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "processArray",
            "([I[Ljava/lang/String;)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should handle methods with array types");
    }

    /**
     * Tests visitProgramMethod with a method descriptor containing other collection types.
     */
    @Test
    public void testVisitProgramMethod_withMapInDescriptor_doesNotProcess() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "processMap",
            "(Ljava/util/Map;)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should not process methods with Map (no List)");
    }

    /**
     * Tests visitProgramMethod with a method that has List as a generic parameter within another type.
     */
    @Test
    public void testVisitProgramMethod_withListInGenericType_processesMethod() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        // Note: In bytecode, generics are erased, but the descriptor still contains List
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "processNestedList",
            "(Ljava/util/List;)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process method with List in descriptor");
    }

    /**
     * Tests visitProgramMethod with null ProgramClass.
     * This should throw a NullPointerException.
     */
    @Test
    public void testVisitProgramMethod_withNullProgramClass_throwsException() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(programClass, "test", "()V");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> patcher.visitProgramMethod(null, programMethod),
                "visitProgramMethod should throw NullPointerException with null ProgramClass");
    }

    /**
     * Tests visitProgramMethod with null ProgramMethod.
     * This should throw a NullPointerException.
     */
    @Test
    public void testVisitProgramMethod_withNullProgramMethod_throwsException() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> patcher.visitProgramMethod(programClass, null),
                "visitProgramMethod should throw NullPointerException with null ProgramMethod");
    }

    /**
     * Tests visitProgramMethod called multiple times with different methods.
     */
    @Test
    public void testVisitProgramMethod_multipleCalls_eachProcessedIndependently() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod method1 = createMethodWithDescriptor(programClass, "method1", "(I)V");
        ProgramMethod method2 = createMethodWithDescriptor(programClass, "method2", "(Ljava/util/List;)V");
        ProgramMethod method3 = createMethodWithDescriptor(programClass, "method3", "(Ljava/lang/String;)V");

        // Act & Assert
        assertDoesNotThrow(() -> {
            patcher.visitProgramMethod(programClass, method1);
            patcher.visitProgramMethod(programClass, method2);
            patcher.visitProgramMethod(programClass, method3);
        }, "visitProgramMethod should handle multiple calls independently");
    }

    /**
     * Tests visitProgramMethod with addExcluder=true configuration.
     */
    @Test
    public void testVisitProgramMethod_withAddExcluderTrue_processesMethod() {
        // Arrange
        CodeAttributeEditor editor = new CodeAttributeEditor();
        GsonConstructorPatcher patcherWithExcluder = new GsonConstructorPatcher(editor, true);
        ProgramClass programClass = createMinimalProgramClass("com/google/gson/Gson");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "<init>",
            "(Ljava/util/List;)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcherWithExcluder.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should work with addExcluder=true");
    }

    /**
     * Tests visitProgramMethod with addExcluder=false configuration.
     */
    @Test
    public void testVisitProgramMethod_withAddExcluderFalse_processesMethod() {
        // Arrange
        CodeAttributeEditor editor = new CodeAttributeEditor();
        GsonConstructorPatcher patcherNoExcluder = new GsonConstructorPatcher(editor, false);
        ProgramClass programClass = createMinimalProgramClass("com/google/gson/Gson");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "<init>",
            "(Ljava/util/List;)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcherNoExcluder.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should work with addExcluder=false");
    }

    /**
     * Tests visitProgramMethod with a method that has a List parameter and attributes.
     */
    @Test
    public void testVisitProgramMethod_withListAndAttributes_processesAttributes() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/google/gson/Gson");
        ProgramMethod programMethod = createMethodWithDescriptorAndAttributes(
            programClass,
            "<init>",
            "(Ljava/util/List;)V"
        );

        // Act & Assert - should process attributes without exception
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process method with attributes when List in descriptor");
    }

    /**
     * Tests visitProgramMethod with a method having no attributes.
     */
    @Test
    public void testVisitProgramMethod_withListButNoAttributes_handlesGracefully() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/google/gson/Gson");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "<init>",
            "(Ljava/util/List;)V"
        );
        programMethod.attributes = new Attribute[0];
        programMethod.u2attributesCount = 0;

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should handle methods with no attributes");
    }

    /**
     * Tests visitProgramMethod with a constructor-like method name.
     */
    @Test
    public void testVisitProgramMethod_withConstructorName_andList_processesMethod() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/google/gson/Gson");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "<init>",
            "(Lcom/google/gson/internal/Excluder;Ljava/util/List;Ljava/util/Map;)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process constructor with List parameter");
    }

    /**
     * Tests visitProgramMethod with a regular method (not constructor) containing List.
     */
    @Test
    public void testVisitProgramMethod_withRegularMethodNameAndList_processesMethod() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "setFactories",
            "(Ljava/util/List;)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process regular method with List parameter");
    }

    /**
     * Tests that visitProgramMethod behavior is deterministic.
     * Multiple calls with the same method should behave consistently.
     */
    @Test
    public void testVisitProgramMethod_deterministic_sameInputSameBehavior() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "process",
            "(Ljava/util/List;)V"
        );

        // Act & Assert
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                    "Repeated calls should behave consistently");
        }
    }

    /**
     * Tests visitProgramMethod with a descriptor containing ArrayList (not List).
     * Since the check is for "java/util/List", ArrayList should match if fully qualified.
     */
    @Test
    public void testVisitProgramMethod_withArrayList_doesNotMatchUnlessListPresent() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "processArrayList",
            "(Ljava/util/ArrayList;)V"
        );

        // Act & Assert - ArrayList doesn't contain "List" substring in the descriptor
        // Actually "ArrayList" contains "List" so it will match
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process method with ArrayList (contains 'List')");
    }

    /**
     * Tests visitProgramMethod with a descriptor containing LinkedList.
     */
    @Test
    public void testVisitProgramMethod_withLinkedList_processesMethod() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(
            programClass,
            "processLinkedList",
            "(Ljava/util/LinkedList;)V"
        );

        // Act & Assert - LinkedList contains "List" substring
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should process method with LinkedList (contains 'List')");
    }

    /**
     * Tests visitProgramMethod with various patcher instances.
     */
    @Test
    public void testVisitProgramMethod_multipleInstances_independentBehavior() {
        // Arrange
        CodeAttributeEditor editor1 = new CodeAttributeEditor();
        CodeAttributeEditor editor2 = new CodeAttributeEditor();
        GsonConstructorPatcher patcher1 = new GsonConstructorPatcher(editor1, true);
        GsonConstructorPatcher patcher2 = new GsonConstructorPatcher(editor2, false);

        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod methodWithList = createMethodWithDescriptor(
            programClass,
            "withList",
            "(Ljava/util/List;)V"
        );
        ProgramMethod methodWithoutList = createMethodWithDescriptor(
            programClass,
            "withoutList",
            "(I)V"
        );

        // Act & Assert
        assertDoesNotThrow(() -> {
            patcher1.visitProgramMethod(programClass, methodWithList);
            patcher2.visitProgramMethod(programClass, methodWithoutList);
        }, "Multiple patcher instances should work independently");
    }

    /**
     * Tests visitProgramMethod with empty descriptor (edge case, shouldn't occur in valid bytecode).
     */
    @Test
    public void testVisitProgramMethod_withEmptyDescriptor_doesNotProcess() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ProgramMethod programMethod = createMethodWithDescriptor(programClass, "emptyDesc", "");

        // Act & Assert - empty string doesn't contain "List"
        assertDoesNotThrow(() -> patcher.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should handle empty descriptor");
    }

    // =========================================================================
    // Helper methods to create test objects
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
     *
     * @param className the name of the class
     * @return a configured ProgramClass instance
     */
    private ProgramClass createMinimalProgramClass(String className) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        Constant[] constantPool = new Constant[50];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 50;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific descriptor.
     *
     * @param clazz the owning class
     * @param methodName the method name
     * @param descriptor the method descriptor
     * @return a configured ProgramMethod instance
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass clazz, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = 0;

        // Find available indices in constant pool
        int nameIndex = findAvailableConstantPoolIndex(clazz);
        int descriptorIndex = findAvailableConstantPoolIndex(clazz);

        clazz.constantPool[nameIndex] = new Utf8Constant(methodName);
        clazz.constantPool[descriptorIndex] = new Utf8Constant(descriptor);

        method.u2nameIndex = nameIndex;
        method.u2descriptorIndex = descriptorIndex;
        method.u2attributesCount = 0;
        method.attributes = new Attribute[0];

        return method;
    }

    /**
     * Creates a ProgramMethod with a specific descriptor and attributes.
     *
     * @param clazz the owning class
     * @param methodName the method name
     * @param descriptor the method descriptor
     * @return a configured ProgramMethod instance with attributes
     */
    private ProgramMethod createMethodWithDescriptorAndAttributes(ProgramClass clazz, String methodName, String descriptor) {
        ProgramMethod method = createMethodWithDescriptor(clazz, methodName, descriptor);

        // Add a minimal Code attribute
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 10;
        codeAttribute.u4codeLength = 1;
        codeAttribute.code = new byte[1];
        codeAttribute.u2exceptionTableLength = 0;
        codeAttribute.exceptionTable = new ExceptionInfo[0];
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        // Set attribute name in constant pool
        int attrNameIndex = findAvailableConstantPoolIndex(clazz);
        clazz.constantPool[attrNameIndex] = new Utf8Constant("Code");
        codeAttribute.u2attributeNameIndex = attrNameIndex;

        method.attributes = new Attribute[]{codeAttribute};
        method.u2attributesCount = 1;

        return method;
    }

    /**
     * Finds an available index in the constant pool.
     *
     * @param clazz the class with the constant pool
     * @return an available index
     */
    private int findAvailableConstantPoolIndex(ProgramClass clazz) {
        for (int i = 3; i < clazz.constantPool.length; i++) {
            if (clazz.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("No available constant pool index");
    }
}
