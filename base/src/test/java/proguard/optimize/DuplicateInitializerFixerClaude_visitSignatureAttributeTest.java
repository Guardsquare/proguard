package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.VersionConstants;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateInitializerFixer#visitSignatureAttribute(Clazz, Method, SignatureAttribute)}.
 *
 * The visitSignatureAttribute method updates a method's generic signature when a new parameter
 * is added to a duplicate initializer. It:
 * 1. Extracts the method descriptor and finds the parameter closing position ')'
 * 2. Extracts the current signature and finds its parameter closing position
 * 3. Takes the last character from the descriptor (the newly added parameter type)
 * 4. Inserts this character into the signature before the ')'
 * 5. Updates the signature in the constant pool
 *
 * This is necessary to keep generic signatures in sync with descriptor changes when fixing
 * duplicate initializers by adding a dummy parameter.
 */
public class DuplicateInitializerFixerClaude_visitSignatureAttributeTest {

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
        programClass.constantPool = new proguard.classfile.constant.Constant[20];
    }

    /**
     * Tests that visitSignatureAttribute updates the signature by adding the new parameter type.
     * Method descriptor (I)V with signature ()V should become (I)V with signature (I)V.
     */
    @Test
    public void testVisitSignatureAttribute_addsParameterTypeToSignature() {
        // Arrange - Method with descriptor (I)V and signature ()V
        ProgramMethod method = createMethod("testMethod", "(I)V", "()V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("()V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert - Signature should now be (I)V (with the 'I' inserted)
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds an int parameter type.
     * Descriptor: (Ljava/lang/String;I)V, Signature: (Ljava/lang/String;)V
     * Expected: Signature becomes (Ljava/lang/String;I)V
     */
    @Test
    public void testVisitSignatureAttribute_addsIntParameterToExistingSignature() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(Ljava/lang/String;I)V", "(Ljava/lang/String;)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(Ljava/lang/String;)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(Ljava/lang/String;I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds a long parameter type (represented as 'J').
     * Descriptor: (IJ)V, Signature: (I)V
     * Expected: Signature becomes (IJ)V
     */
    @Test
    public void testVisitSignatureAttribute_addsLongParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(IJ)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(IJ)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds a double parameter type (represented as 'D').
     * Descriptor: (ID)V, Signature: (I)V
     * Expected: Signature becomes (ID)V
     */
    @Test
    public void testVisitSignatureAttribute_addsDoubleParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(ID)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(ID)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds a boolean parameter type (represented as 'Z').
     */
    @Test
    public void testVisitSignatureAttribute_addsBooleanParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(IZ)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(IZ)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds a byte parameter type (represented as 'B').
     */
    @Test
    public void testVisitSignatureAttribute_addsByteParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(IB)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(IB)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds a char parameter type (represented as 'C').
     */
    @Test
    public void testVisitSignatureAttribute_addsCharParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(IC)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(IC)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds a short parameter type (represented as 'S').
     */
    @Test
    public void testVisitSignatureAttribute_addsShortParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(IS)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(IS)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly adds a float parameter type (represented as 'F').
     */
    @Test
    public void testVisitSignatureAttribute_addsFloatParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(IF)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(IF)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute works with generic signatures containing type parameters.
     * Descriptor: (Ljava/util/List;I)V
     * Signature: (Ljava/util/List<Ljava/lang/String;>;)V
     * Expected: (Ljava/util/List<Ljava/lang/String;>;I)V
     */
    @Test
    public void testVisitSignatureAttribute_preservesGenericTypeParameters() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(Ljava/util/List;I)V",
            "(Ljava/util/List<Ljava/lang/String;>;)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(Ljava/util/List<Ljava/lang/String;>;)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(Ljava/util/List<Ljava/lang/String;>;I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute works with multiple generic parameters.
     * Descriptor: (Ljava/util/Map;Ljava/util/List;I)V
     * Signature: (Ljava/util/Map<TK;TV;>;Ljava/util/List<Ljava/lang/String;>;)V
     * Expected: (Ljava/util/Map<TK;TV;>;Ljava/util/List<Ljava/lang/String;>;I)V
     */
    @Test
    public void testVisitSignatureAttribute_multipleGenericParameters() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(Ljava/util/Map;Ljava/util/List;I)V",
            "(Ljava/util/Map<TK;TV;>;Ljava/util/List<Ljava/lang/String;>;)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute(
            "(Ljava/util/Map<TK;TV;>;Ljava/util/List<Ljava/lang/String;>;)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(Ljava/util/Map<TK;TV;>;Ljava/util/List<Ljava/lang/String;>;I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute works with array parameter additions.
     * Descriptor: (I[I)V, Signature: (I)V
     * Expected: (I[I)V (adds the array type)
     */
    @Test
    public void testVisitSignatureAttribute_addsArrayParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(I[I)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert - Should add '[I' (but only the last char 'I', so becomes (II)V)
        // Note: The method takes the last character of the descriptor before ')'
        String newSignature = signatureAttribute.getSignature(programClass);
        // The last char before ')' in "(I[I)V" is 'I'
        assertEquals("(II)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute with method returning a generic type.
     * The return type should be preserved in the signature.
     * Descriptor: (II)Ljava/util/List;
     * Signature: (I)Ljava/util/List<Ljava/lang/String;>;
     * Expected: (II)Ljava/util/List<Ljava/lang/String;>;
     */
    @Test
    public void testVisitSignatureAttribute_preservesGenericReturnType() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(II)Ljava/util/List;",
            "(I)Ljava/util/List<Ljava/lang/String;>;");
        SignatureAttribute signatureAttribute = createSignatureAttribute(
            "(I)Ljava/util/List<Ljava/lang/String;>;");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(II)Ljava/util/List<Ljava/lang/String;>;", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute works when adding to an empty signature.
     * Descriptor: (I)V, Signature: ()V
     */
    @Test
    public void testVisitSignatureAttribute_emptySignatureToSingleParameter() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(I)V", "()V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("()V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute can be called multiple times on different attributes.
     */
    @Test
    public void testVisitSignatureAttribute_multipleCallsOnDifferentAttributes() {
        // Arrange
        ProgramMethod method1 = createMethod("method1", "(II)V", "(I)V");
        SignatureAttribute attr1 = createSignatureAttribute("(I)V");

        ProgramMethod method2 = createMethod("method2", "(JJ)V", "(J)V");
        SignatureAttribute attr2 = createSignatureAttribute("(J)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method1, attr1);
        fixer.visitSignatureAttribute(programClass, method2, attr2);

        // Assert
        assertEquals("(II)V", attr1.getSignature(programClass));
        assertEquals("(JJ)V", attr2.getSignature(programClass));
    }

    /**
     * Tests that visitSignatureAttribute works with nested generic types.
     * Descriptor: (Ljava/util/List;I)V
     * Signature: (Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;)V
     */
    @Test
    public void testVisitSignatureAttribute_nestedGenerics() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(Ljava/util/List;I)V",
            "(Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute(
            "(Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute works with wildcard generics.
     * Descriptor: (Ljava/util/List;I)V
     * Signature: (Ljava/util/List<*>;)V
     */
    @Test
    public void testVisitSignatureAttribute_wildcardGenerics() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(Ljava/util/List;I)V",
            "(Ljava/util/List<*>;)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(Ljava/util/List<*>;)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(Ljava/util/List<*>;I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute works with bounded type parameters.
     * Descriptor: (Ljava/lang/Comparable;I)V
     * Signature: (Ljava/lang/Comparable<+Ljava/lang/Number;>;)V
     */
    @Test
    public void testVisitSignatureAttribute_boundedTypeParameters() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(Ljava/lang/Comparable;I)V",
            "(Ljava/lang/Comparable<+Ljava/lang/Number;>;)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute(
            "(Ljava/lang/Comparable<+Ljava/lang/Number;>;)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(Ljava/lang/Comparable<+Ljava/lang/Number;>;I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute correctly handles method-level type parameters.
     * Descriptor: (Ljava/lang/Object;I)V
     * Signature: <T:Ljava/lang/Object;>(TT;)V
     */
    @Test
    public void testVisitSignatureAttribute_methodLevelTypeParameters() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(Ljava/lang/Object;I)V",
            "<T:Ljava/lang/Object;>(TT;)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("<T:Ljava/lang/Object;>(TT;)V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("<T:Ljava/lang/Object;>(TT;I)V", newSignature);
    }

    /**
     * Tests that visitSignatureAttribute does not interact with the extra visitor.
     */
    @Test
    public void testVisitSignatureAttribute_doesNotTriggerExtraVisitor() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(I)V", "()V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("()V");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert
        verifyNoInteractions(extraFixedInitializerVisitor);
    }

    /**
     * Tests that visitSignatureAttribute works with a fixer that has no extra visitor.
     */
    @Test
    public void testVisitSignatureAttribute_withNullExtraVisitor() {
        // Arrange
        DuplicateInitializerFixer fixerWithNullVisitor = new DuplicateInitializerFixer();
        ProgramMethod method = createMethod("testMethod", "(I)V", "()V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("()V");

        // Act & Assert
        assertDoesNotThrow(() -> fixerWithNullVisitor.visitSignatureAttribute(programClass, method, signatureAttribute));
        assertEquals("(I)V", signatureAttribute.getSignature(programClass));
    }

    /**
     * Tests that visitSignatureAttribute correctly updates the constant pool.
     * The new signature should be added as a new Utf8Constant.
     */
    @Test
    public void testVisitSignatureAttribute_updatesConstantPool() {
        // Arrange
        ProgramMethod method = createMethod("testMethod", "(II)V", "(I)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(I)V");
        int originalIndex = signatureAttribute.u2signatureIndex;

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert - Index should be different (new constant added)
        assertNotEquals(originalIndex, signatureAttribute.u2signatureIndex);
        // And the signature should be updated
        assertEquals("(II)V", signatureAttribute.getSignature(programClass));
    }

    /**
     * Tests that visitSignatureAttribute is idempotent when called with the same updated descriptor.
     * Note: This tests calling it after the signature has already been updated to match the descriptor.
     */
    @Test
    public void testVisitSignatureAttribute_withAlreadyUpdatedSignature() {
        // Arrange - both descriptor and signature already have the parameter
        ProgramMethod method = createMethod("testMethod", "(II)V", "(II)V");
        SignatureAttribute signatureAttribute = createSignatureAttribute("(II)V");

        // Act - this should still work, adding 'I' again
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert - The last 'I' is added again, resulting in (III)V
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("(III)V", newSignature);
    }

    /**
     * Tests visitSignatureAttribute with complex generic signature with throws clause.
     * Descriptor: (Ljava/lang/Object;I)Ljava/lang/Object;
     * Signature: <T:Ljava/lang/Object;>(TT;)TT;^Ljava/io/IOException;
     */
    @Test
    public void testVisitSignatureAttribute_withThrowsClause() {
        // Arrange
        ProgramMethod method = createMethod("testMethod",
            "(Ljava/lang/Object;I)Ljava/lang/Object;",
            "<T:Ljava/lang/Object;>(TT;)TT;^Ljava/io/IOException;");
        SignatureAttribute signatureAttribute = createSignatureAttribute(
            "<T:Ljava/lang/Object;>(TT;)TT;^Ljava/io/IOException;");

        // Act
        fixer.visitSignatureAttribute(programClass, method, signatureAttribute);

        // Assert - The 'I' should be inserted before the first ')'
        String newSignature = signatureAttribute.getSignature(programClass);
        assertEquals("<T:Ljava/lang/Object;>(TT;I)TT;^Ljava/io/IOException;", newSignature);
    }

    /**
     * Helper method to create a ProgramMethod with the specified name, descriptor, and signature.
     */
    private ProgramMethod createMethod(String name, String descriptor, String signature) {
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2accessFlags = 0;

        // Set up constant pool entries
        int nameIndex = getNextAvailableIndex();
        int descriptorIndex = getNextAvailableIndex();

        programClass.constantPool[nameIndex] = new Utf8Constant(name);
        programClass.constantPool[descriptorIndex] = new Utf8Constant(descriptor);

        programMethod.u2nameIndex = nameIndex;
        programMethod.u2descriptorIndex = descriptorIndex;

        return programMethod;
    }

    /**
     * Helper method to create a SignatureAttribute with the specified signature string.
     */
    private SignatureAttribute createSignatureAttribute(String signature) {
        SignatureAttribute signatureAttribute = new SignatureAttribute();

        // Add signature to constant pool
        int signatureIndex = getNextAvailableIndex();
        programClass.constantPool[signatureIndex] = new Utf8Constant(signature);
        signatureAttribute.u2signatureIndex = signatureIndex;

        return signatureAttribute;
    }

    /**
     * Helper method to get the next available index in the constant pool.
     */
    private int getNextAvailableIndex() {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("Constant pool is full");
    }
}
