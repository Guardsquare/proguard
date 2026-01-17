package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitSignatureAttribute(Clazz, SignatureAttribute)}.
 *
 * The visitSignatureAttribute method updates the signature index of a signature attribute
 * by calling updateDescriptor, which may modify the descriptor if type replacements apply.
 */
public class AbstractAPIConverterClaude_visitSignatureAttributeTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private SignatureAttribute signatureAttribute;

    /**
     * Creates a concrete test subclass of AbstractAPIConverter for testing purposes.
     * This is necessary because AbstractAPIConverter is abstract.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter,
                        ClassVisitor modifiedClassVisitor,
                        InstructionVisitor extraInstructionVisitor) {
            super(programClassPool, libraryClassPool, warningPrinter,
                  modifiedClassVisitor, extraInstructionVisitor);

            // Initialize with empty replacements to avoid NullPointerExceptions
            setTypeReplacements(new TypeReplacement[0]);
            setMethodReplacements(new MethodReplacement[0]);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        modifiedClassVisitor = mock(ClassVisitor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);

        converter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        clazz = mock(ProgramClass.class);
        signatureAttribute = mock(SignatureAttribute.class);
    }

    /**
     * Tests that visitSignatureAttribute can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitSignatureAttribute_withValidMocks_doesNotThrowException() {
        // Arrange
        signatureAttribute.u2signatureIndex = 1;
        when(clazz.getString(anyInt())).thenReturn("Ljava/lang/Object;");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitSignatureAttribute(clazz, signatureAttribute));
    }

    /**
     * Tests visitSignatureAttribute with a simple class signature that doesn't need replacement.
     * The signature index should remain unchanged when no type replacements apply.
     */
    @Test
    public void testVisitSignatureAttribute_withSimpleClassSignature_noReplacement() {
        // Arrange
        int originalSignatureIndex = 5;
        signatureAttribute.u2signatureIndex = originalSignatureIndex;
        when(clazz.getString(originalSignatureIndex)).thenReturn("Ljava/lang/Object;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(originalSignatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a generic class signature.
     * Signature: Ljava/util/List<Ljava/lang/String;>;
     */
    @Test
    public void testVisitSignatureAttribute_withGenericClassSignature() {
        // Arrange
        int signatureIndex = 10;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a complex generic signature.
     * Signature: Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;
     */
    @Test
    public void testVisitSignatureAttribute_withComplexGenericSignature() {
        // Arrange
        int signatureIndex = 15;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a method signature.
     * Signature: (Ljava/lang/String;)Ljava/lang/Object;
     */
    @Test
    public void testVisitSignatureAttribute_withMethodSignature() {
        // Arrange
        int signatureIndex = 20;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("(Ljava/lang/String;)Ljava/lang/Object;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a generic method signature.
     * Signature: <T:Ljava/lang/Object;>(TT;)TT;
     */
    @Test
    public void testVisitSignatureAttribute_withGenericMethodSignature() {
        // Arrange
        int signatureIndex = 25;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("<T:Ljava/lang/Object;>(TT;)TT;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a class signature containing extends clause.
     * Signature: <T:Ljava/lang/Object;>Ljava/lang/Object;
     */
    @Test
    public void testVisitSignatureAttribute_withExtendsClause() {
        // Arrange
        int signatureIndex = 30;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("<T:Ljava/lang/Object;>Ljava/lang/Object;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a class signature containing implements clauses.
     * Signature: Ljava/lang/Object;Ljava/io/Serializable;
     */
    @Test
    public void testVisitSignatureAttribute_withImplementsClause() {
        // Arrange
        int signatureIndex = 35;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/lang/Object;Ljava/io/Serializable;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a wildcard signature.
     * Signature: Ljava/util/List<*>;
     */
    @Test
    public void testVisitSignatureAttribute_withWildcardSignature() {
        // Arrange
        int signatureIndex = 40;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<*>;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a bounded wildcard signature (extends).
     * Signature: Ljava/util/List<+Ljava/lang/Number;>;
     */
    @Test
    public void testVisitSignatureAttribute_withBoundedWildcardExtends() {
        // Arrange
        int signatureIndex = 45;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<+Ljava/lang/Number;>;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with a bounded wildcard signature (super).
     * Signature: Ljava/util/List<-Ljava/lang/Integer;>;
     */
    @Test
    public void testVisitSignatureAttribute_withBoundedWildcardSuper() {
        // Arrange
        int signatureIndex = 50;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<-Ljava/lang/Integer;>;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with nested generic types.
     * Signature: Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;
     */
    @Test
    public void testVisitSignatureAttribute_withNestedGenerics() {
        // Arrange
        int signatureIndex = 55;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with an array signature.
     * Signature: [Ljava/lang/String;
     */
    @Test
    public void testVisitSignatureAttribute_withArraySignature() {
        // Arrange
        int signatureIndex = 60;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("[Ljava/lang/String;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with multiple type parameters.
     * Signature: <K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/lang/Object;
     */
    @Test
    public void testVisitSignatureAttribute_withMultipleTypeParameters() {
        // Arrange
        int signatureIndex = 65;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("<K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/lang/Object;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute can be called multiple times with the same attribute.
     * The method should be idempotent in terms of accessing the signature.
     */
    @Test
    public void testVisitSignatureAttribute_calledMultipleTimes() {
        // Arrange
        int signatureIndex = 70;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/lang/Object;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);
        converter.visitSignatureAttribute(clazz, signatureAttribute);
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed at least 3 times
        verify(clazz, atLeast(3)).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with different signature attributes.
     * Each attribute should have its signature index processed independently.
     */
    @Test
    public void testVisitSignatureAttribute_withDifferentAttributes() {
        // Arrange
        SignatureAttribute attr1 = mock(SignatureAttribute.class);
        SignatureAttribute attr2 = mock(SignatureAttribute.class);
        attr1.u2signatureIndex = 10;
        attr2.u2signatureIndex = 20;
        when(clazz.getString(10)).thenReturn("Ljava/lang/String;");
        when(clazz.getString(20)).thenReturn("Ljava/lang/Integer;");

        // Act
        converter.visitSignatureAttribute(clazz, attr1);
        converter.visitSignatureAttribute(clazz, attr2);

        // Assert - verify both signature indices were accessed
        verify(clazz, atLeastOnce()).getString(10);
        verify(clazz, atLeastOnce()).getString(20);
    }

    /**
     * Tests visitSignatureAttribute with different clazz instances.
     * Each clazz should be used to retrieve the signature string independently.
     */
    @Test
    public void testVisitSignatureAttribute_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        signatureAttribute.u2signatureIndex = 5;
        when(clazz1.getString(5)).thenReturn("Ljava/lang/String;");
        when(clazz2.getString(5)).thenReturn("Ljava/lang/Integer;");

        // Act
        converter.visitSignatureAttribute(clazz1, signatureAttribute);
        converter.visitSignatureAttribute(clazz2, signatureAttribute);

        // Assert - verify each clazz was used
        verify(clazz1, atLeastOnce()).getString(5);
        verify(clazz2, atLeastOnce()).getString(5);
    }

    /**
     * Tests visitSignatureAttribute doesn't trigger warnings with valid signatures.
     * No warnings should be generated for well-formed signatures.
     */
    @Test
    public void testVisitSignatureAttribute_withValidSignature_doesNotTriggerWarnings() {
        // Arrange
        signatureAttribute.u2signatureIndex = 1;
        when(clazz.getString(1)).thenReturn("Ljava/lang/Object;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify no interactions with warning printer
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitSignatureAttribute processes the signature attribute correctly.
     * The method should access the clazz to get the signature string.
     */
    @Test
    public void testVisitSignatureAttribute_processesAttribute() {
        // Arrange
        int signatureIndex = 100;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/ArrayList;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the clazz was queried for the signature
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with inner class signature.
     * Signature: Lcom/example/Outer$Inner;
     */
    @Test
    public void testVisitSignatureAttribute_withInnerClassSignature() {
        // Arrange
        int signatureIndex = 80;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Lcom/example/Outer$Inner;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with primitive type in generic signature.
     * Note: Primitives can't actually appear in generic signatures, but we test
     * what would happen if the signature contains primitive-like patterns.
     */
    @Test
    public void testVisitSignatureAttribute_withPrimitiveWrapper() {
        // Arrange
        int signatureIndex = 85;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/lang/Integer;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitSignatureAttribute with signature index 0.
     * This tests edge case behavior with a zero index.
     */
    @Test
    public void testVisitSignatureAttribute_withZeroIndex() {
        // Arrange
        signatureAttribute.u2signatureIndex = 0;
        when(clazz.getString(0)).thenReturn("Ljava/lang/Object;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(0);
    }

    /**
     * Tests visitSignatureAttribute with a very simple object type signature.
     * Signature: Ljava/lang/String;
     */
    @Test
    public void testVisitSignatureAttribute_withSimpleObjectType() {
        // Arrange
        int signatureIndex = 90;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests that visitSignatureAttribute properly integrates with the converter's state.
     * Multiple sequential calls should maintain consistent behavior.
     */
    @Test
    public void testVisitSignatureAttribute_maintainsConsistentBehavior() {
        // Arrange
        SignatureAttribute attr1 = mock(SignatureAttribute.class);
        SignatureAttribute attr2 = mock(SignatureAttribute.class);
        attr1.u2signatureIndex = 1;
        attr2.u2signatureIndex = 2;
        when(clazz.getString(1)).thenReturn("Ljava/util/List;");
        when(clazz.getString(2)).thenReturn("Ljava/util/Map;");

        // Act
        converter.visitSignatureAttribute(clazz, attr1);
        converter.visitSignatureAttribute(clazz, attr2);

        // Assert - verify both were processed independently
        verify(clazz, times(1)).getString(1);
        verify(clazz, times(1)).getString(2);
    }

    /**
     * Tests visitSignatureAttribute with package-private class signature.
     * Signature: Lcom/example/PackagePrivate;
     */
    @Test
    public void testVisitSignatureAttribute_withPackagePrivateClass() {
        // Arrange
        int signatureIndex = 95;
        signatureAttribute.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Lcom/example/PackagePrivate;");

        // Act
        converter.visitSignatureAttribute(clazz, signatureAttribute);

        // Assert - verify the descriptor was accessed
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }
}
