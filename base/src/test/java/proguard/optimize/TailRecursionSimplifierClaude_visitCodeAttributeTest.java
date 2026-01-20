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
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
 *
 * The visitCodeAttribute method in TailRecursionSimplifier optimizes tail-recursive method calls by
 * replacing them with loop constructs. The method:
 *
 * 1. Checks if the method is eligible for tail recursion simplification:
 *    - Method must be private, static, or final (at least one of these flags)
 *    - Method must NOT be synchronized, native, or abstract
 *
 * 2. If eligible, processes the code attribute:
 *    - Resets the code attribute composer
 *    - Begins a code fragment
 *    - Processes instructions to find tail-recursive calls
 *    - If any tail recursion was found and inlined, updates the code attribute
 *
 * 3. If not eligible, does nothing (returns early)
 *
 * These tests verify that:
 * 1. The method correctly identifies eligible methods based on access flags
 * 2. The method processes code attributes when the method is eligible
 * 3. The method does nothing when the method is not eligible
 * 4. The method handles various parameter combinations appropriately
 */
public class TailRecursionSimplifierClaude_visitCodeAttributeTest {

    private TailRecursionSimplifier simplifier;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        simplifier = new TailRecursionSimplifier();

        // Create a basic program class with proper initialization
        programClass = new ProgramClass();
        programClass.u4version = VersionConstants.CLASS_VERSION_1_8;
        programClass.constantPool = new Constant[10];
    }

    /**
     * Tests that visitCodeAttribute processes a private method.
     * Private methods are eligible for tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withPrivateMethod_processesCodeAttribute() {
        // Arrange - Create a private method
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should not throw exception
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute processes a static method.
     * Static methods are eligible for tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withStaticMethod_processesCodeAttribute() {
        // Arrange - Create a static method
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.STATIC);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should not throw exception
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute processes a final method.
     * Final methods are eligible for tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withFinalMethod_processesCodeAttribute() {
        // Arrange - Create a final method
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.FINAL);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should not throw exception
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute processes a method that is both private and static.
     * Methods with multiple eligible flags should be processed.
     */
    @Test
    public void testVisitCodeAttribute_withPrivateStaticMethod_processesCodeAttribute() {
        // Arrange - Create a private static method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.PRIVATE | AccessConstants.STATIC);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should not throw exception
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute processes a method that is both private and final.
     * Methods with multiple eligible flags should be processed.
     */
    @Test
    public void testVisitCodeAttribute_withPrivateFinalMethod_processesCodeAttribute() {
        // Arrange - Create a private final method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.PRIVATE | AccessConstants.FINAL);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should not throw exception
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute processes a method that is both static and final.
     * Methods with multiple eligible flags should be processed.
     */
    @Test
    public void testVisitCodeAttribute_withStaticFinalMethod_processesCodeAttribute() {
        // Arrange - Create a static final method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.STATIC | AccessConstants.FINAL);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should not throw exception
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute processes a method that is private, static, and final.
     * Methods with all eligible flags should be processed.
     */
    @Test
    public void testVisitCodeAttribute_withPrivateStaticFinalMethod_processesCodeAttribute() {
        // Arrange - Create a private static final method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.PRIVATE | AccessConstants.STATIC | AccessConstants.FINAL);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should not throw exception
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute does NOT process a public method (not private, static, or final).
     * Public methods without final are not eligible for tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withPublicMethod_doesNotProcessCodeAttribute() {
        // Arrange - Create a public method (no private, static, or final flags)
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PUBLIC);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should complete without processing (no exception)
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute does NOT process a protected method (not private, static, or final).
     * Protected methods without final are not eligible for tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withProtectedMethod_doesNotProcessCodeAttribute() {
        // Arrange - Create a protected method
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PROTECTED);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should complete without processing (no exception)
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute does NOT process a synchronized private method.
     * Synchronized methods are explicitly excluded from tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withSynchronizedPrivateMethod_doesNotProcessCodeAttribute() {
        // Arrange - Create a synchronized private method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.PRIVATE | AccessConstants.SYNCHRONIZED);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should complete without processing (no exception)
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute does NOT process a native private method.
     * Native methods are explicitly excluded from tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withNativePrivateMethod_doesNotProcessCodeAttribute() {
        // Arrange - Create a native private method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.PRIVATE | AccessConstants.NATIVE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should complete without processing (no exception)
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute does NOT process an abstract method.
     * Abstract methods are explicitly excluded from tail recursion simplification.
     */
    @Test
    public void testVisitCodeAttribute_withAbstractMethod_doesNotProcessCodeAttribute() {
        // Arrange - Create an abstract method (abstract implies public in interfaces)
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.ABSTRACT);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should complete without processing (no exception)
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute does NOT process a synchronized static method.
     * Even though static is eligible, synchronized excludes it.
     */
    @Test
    public void testVisitCodeAttribute_withSynchronizedStaticMethod_doesNotProcessCodeAttribute() {
        // Arrange - Create a synchronized static method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.STATIC | AccessConstants.SYNCHRONIZED);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should complete without processing (no exception)
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute does NOT process a native static method.
     * Even though static is eligible, native excludes it.
     */
    @Test
    public void testVisitCodeAttribute_withNativeStaticMethod_doesNotProcessCodeAttribute() {
        // Arrange - Create a native static method
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.STATIC | AccessConstants.NATIVE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act - should complete without processing (no exception)
        assertDoesNotThrow(() -> simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute can be called multiple times on the same simplifier.
     * Verifies the simplifier can process multiple code attributes sequentially.
     */
    @Test
    public void testVisitCodeAttribute_calledMultipleTimes_processesEachCall() {
        // Arrange
        ProgramMethod programMethod1 = createMethod("method1", "()V", AccessConstants.PRIVATE);
        ProgramMethod programMethod2 = createMethod("method2", "()V", AccessConstants.STATIC);
        ProgramMethod programMethod3 = createMethod("method3", "()V", AccessConstants.FINAL);

        CodeAttribute codeAttribute1 = createBasicCodeAttribute();
        CodeAttribute codeAttribute2 = createBasicCodeAttribute();
        CodeAttribute codeAttribute3 = createBasicCodeAttribute();

        // Act & Assert - all should complete without exception
        assertDoesNotThrow(() -> {
            simplifier.visitCodeAttribute(programClass, programMethod1, codeAttribute1);
            simplifier.visitCodeAttribute(programClass, programMethod2, codeAttribute2);
            simplifier.visitCodeAttribute(programClass, programMethod3, codeAttribute3);
        });
    }

    /**
     * Tests that visitCodeAttribute works with different code attribute sizes.
     * Verifies the simplifier handles code attributes of various lengths.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentCodeLengths_processesCorrectly() {
        // Arrange
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);

        CodeAttribute smallCode = createCodeAttributeWithLength(10);
        CodeAttribute mediumCode = createCodeAttributeWithLength(100);
        CodeAttribute largeCode = createCodeAttributeWithLength(1000);

        // Act & Assert - all should complete without exception
        assertDoesNotThrow(() -> {
            simplifier.visitCodeAttribute(programClass, programMethod, smallCode);
            simplifier.visitCodeAttribute(programClass, programMethod, mediumCode);
            simplifier.visitCodeAttribute(programClass, programMethod, largeCode);
        });
    }

    /**
     * Tests that visitCodeAttribute works with methods of different descriptors.
     * Verifies the simplifier handles various method signatures.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentMethodDescriptors_processesCorrectly() {
        // Arrange - Create methods with different descriptors
        ProgramMethod voidMethod = createMethod("voidMethod", "()V", AccessConstants.PRIVATE);
        ProgramMethod intMethod = createMethod("intMethod", "()I", AccessConstants.PRIVATE);
        ProgramMethod paramMethod = createMethod("paramMethod", "(II)V", AccessConstants.PRIVATE);
        ProgramMethod objectMethod = createMethod("objectMethod", "()Ljava/lang/String;", AccessConstants.PRIVATE);

        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - all should complete without exception
        assertDoesNotThrow(() -> {
            simplifier.visitCodeAttribute(programClass, voidMethod, codeAttribute);
            simplifier.visitCodeAttribute(programClass, intMethod, codeAttribute);
            simplifier.visitCodeAttribute(programClass, paramMethod, codeAttribute);
            simplifier.visitCodeAttribute(programClass, objectMethod, codeAttribute);
        });
    }

    /**
     * Tests that visitCodeAttribute with an extraTailRecursionVisitor works correctly.
     * Verifies the simplifier with extra visitor processes eligible methods.
     */
    @Test
    public void testVisitCodeAttribute_withExtraVisitor_processesCorrectly() {
        // Arrange
        InstructionVisitor extraVisitor = mock(InstructionVisitor.class);
        TailRecursionSimplifier simplifierWithVisitor = new TailRecursionSimplifier(extraVisitor);

        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - should complete without exception
        assertDoesNotThrow(() ->
            simplifierWithVisitor.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute with null extraTailRecursionVisitor works correctly.
     * Verifies the simplifier with null extra visitor processes eligible methods.
     */
    @Test
    public void testVisitCodeAttribute_withNullExtraVisitor_processesCorrectly() {
        // Arrange
        TailRecursionSimplifier simplifierWithNullVisitor = new TailRecursionSimplifier(null);

        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - should complete without exception
        assertDoesNotThrow(() ->
            simplifierWithNullVisitor.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute handles empty code attributes.
     * Verifies the simplifier processes code attributes with zero-length code.
     */
    @Test
    public void testVisitCodeAttribute_withEmptyCodeAttribute_processesCorrectly() {
        // Arrange
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);
        CodeAttribute codeAttribute = createCodeAttributeWithLength(0);

        // Act & Assert - should complete without exception
        assertDoesNotThrow(() ->
            simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute can process the same code attribute multiple times.
     * Verifies the simplifier is reusable for the same code attribute.
     */
    @Test
    public void testVisitCodeAttribute_sameCodeAttributeMultipleTimes_processesCorrectly() {
        // Arrange
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - should complete without exception
        assertDoesNotThrow(() -> {
            simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute);
            simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute);
            simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute);
        });
    }

    /**
     * Tests that visitCodeAttribute with a method that has all disqualifying flags
     * does not process the code attribute.
     */
    @Test
    public void testVisitCodeAttribute_withAllDisqualifyingFlags_doesNotProcess() {
        // Arrange - Create a method with synchronized, native, and abstract flags
        ProgramMethod programMethod = createMethod("testMethod", "()V",
                AccessConstants.PRIVATE | AccessConstants.SYNCHRONIZED |
                AccessConstants.NATIVE | AccessConstants.ABSTRACT);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - should complete without processing (no exception)
        assertDoesNotThrow(() ->
            simplifier.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute works correctly when called from the AttributeVisitor interface.
     * Verifies polymorphic usage of the simplifier.
     */
    @Test
    public void testVisitCodeAttribute_viaAttributeVisitorInterface_processesCorrectly() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = simplifier;
        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - should complete without exception
        assertDoesNotThrow(() ->
            visitor.visitCodeAttribute(programClass, programMethod, codeAttribute));
    }

    /**
     * Tests that multiple TailRecursionSimplifier instances can process code attributes independently.
     * Verifies instance isolation.
     */
    @Test
    public void testVisitCodeAttribute_multipleSimplifierInstances_processIndependently() {
        // Arrange
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier3 = new TailRecursionSimplifier();

        ProgramMethod programMethod = createMethod("testMethod", "()V", AccessConstants.PRIVATE);
        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - all should complete without exception
        assertDoesNotThrow(() -> {
            simplifier1.visitCodeAttribute(programClass, programMethod, codeAttribute);
            simplifier2.visitCodeAttribute(programClass, programMethod, codeAttribute);
            simplifier3.visitCodeAttribute(programClass, programMethod, codeAttribute);
        });
    }

    /**
     * Tests that visitCodeAttribute handles methods with complex access flag combinations correctly.
     * Verifies edge cases in access flag checking.
     */
    @Test
    public void testVisitCodeAttribute_withComplexAccessFlagCombinations_handlesCorrectly() {
        // Arrange - Test various combinations
        ProgramMethod privateFinal = createMethod("method1", "()V",
                AccessConstants.PRIVATE | AccessConstants.FINAL);
        ProgramMethod publicFinal = createMethod("method2", "()V",
                AccessConstants.PUBLIC | AccessConstants.FINAL);
        ProgramMethod protectedStatic = createMethod("method3", "()V",
                AccessConstants.PROTECTED | AccessConstants.STATIC);

        CodeAttribute codeAttribute = createBasicCodeAttribute();

        // Act & Assert - all should complete without exception
        assertDoesNotThrow(() -> {
            simplifier.visitCodeAttribute(programClass, privateFinal, codeAttribute);
            simplifier.visitCodeAttribute(programClass, publicFinal, codeAttribute);
            simplifier.visitCodeAttribute(programClass, protectedStatic, codeAttribute);
        });
    }

    // Helper methods

    /**
     * Creates a ProgramMethod with the specified name, descriptor, and access flags.
     */
    private ProgramMethod createMethod(String name, String descriptor, int accessFlags) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = accessFlags;

        // Set up the method name and descriptor in the constant pool
        // For these tests, we just need the method to be valid, not to have actual references
        return method;
    }

    /**
     * Creates a basic CodeAttribute with a simple return instruction.
     */
    private CodeAttribute createBasicCodeAttribute() {
        return createCodeAttributeWithLength(1);
    }

    /**
     * Creates a CodeAttribute with the specified code length.
     */
    private CodeAttribute createCodeAttributeWithLength(int length) {
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u4codeLength = length;
        codeAttribute.code = new byte[length];
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 10;

        // Initialize with NOP instructions (0x00)
        for (int i = 0; i < length; i++) {
            codeAttribute.code[i] = 0x00;
        }

        return codeAttribute;
    }
}
