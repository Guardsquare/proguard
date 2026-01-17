package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTypeInfo;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo)}.
 *
 * The visitLocalVariableTypeInfo method updates the signature index of a local variable type
 * by calling updateDescriptor, which may replace type references based on the converter's
 * type replacement rules. This is used for generic type information in the LocalVariableTypeTable.
 */
public class AbstractAPIConverterClaude_visitLocalVariableTypeInfoTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private LocalVariableTypeInfo localVariableTypeInfo;

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
        method = mock(Method.class);
        codeAttribute = mock(CodeAttribute.class);
        localVariableTypeInfo = mock(LocalVariableTypeInfo.class);
    }

    /**
     * Tests that visitLocalVariableTypeInfo can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withValidMocks_doesNotThrowException() {
        // Arrange
        when(clazz.getString(anyInt())).thenReturn("Ljava/lang/String;");
        localVariableTypeInfo.u2signatureIndex = 1;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo));
    }

    /**
     * Tests visitLocalVariableTypeInfo with a simple generic type signature.
     * Generic type signatures contain parametric type information.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withGenericListSignature() {
        // Arrange
        int signatureIndex = 5;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert - verify getString was called to read the signature
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a Map generic signature.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withGenericMapSignature() {
        // Arrange
        int signatureIndex = 10;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a wildcard generic signature.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withWildcardSignature() {
        // Arrange
        int signatureIndex = 15;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<*>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with an extends bounded generic signature.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withExtendsBoundedSignature() {
        // Arrange
        int signatureIndex = 20;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<+Ljava/lang/Number;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a super bounded generic signature.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withSuperBoundedSignature() {
        // Arrange
        int signatureIndex = 25;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<-Ljava/lang/Integer;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with nested generic signatures.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withNestedGenerics() {
        // Arrange
        int signatureIndex = 30;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a type variable signature.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withTypeVariable() {
        // Arrange
        int signatureIndex = 35;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("TT;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a complex generic signature containing multiple parameters.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withComplexGenericSignature() {
        // Arrange
        int signatureIndex = 40;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/Map<Ljava/lang/String;Ljava/util/List<Ljava/lang/Integer;>;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with an array of generic types.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withGenericArraySignature() {
        // Arrange
        int signatureIndex = 45;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("[Ljava/util/List<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo can be called multiple times with the same local variable type info.
     * Each call should process the signature independently.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_calledMultipleTimes() {
        // Arrange
        int signatureIndex = 50;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert - verify getString was called at least 3 times
        verify(clazz, atLeast(3)).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with different local variable type info instances.
     * Each instance should have its signature processed independently.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withDifferentLocalVariableTypeInfos() {
        // Arrange
        LocalVariableTypeInfo info1 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info2 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info3 = mock(LocalVariableTypeInfo.class);

        info1.u2signatureIndex = 1;
        info2.u2signatureIndex = 2;
        info3.u2signatureIndex = 3;

        when(clazz.getString(1)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");
        when(clazz.getString(2)).thenReturn("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;");
        when(clazz.getString(3)).thenReturn("TT;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info1);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info2);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info3);

        // Assert - verify each signature was read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
    }

    /**
     * Tests visitLocalVariableTypeInfo with different clazz instances.
     * Each clazz should provide its own string constants.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        localVariableTypeInfo.u2signatureIndex = 10;

        when(clazz1.getString(10)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");
        when(clazz2.getString(10)).thenReturn("Ljava/util/Set<Ljava/lang/Integer;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz1, method, codeAttribute, localVariableTypeInfo);
        converter.visitLocalVariableTypeInfo(clazz2, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz1, atLeastOnce()).getString(10);
        verify(clazz2, atLeastOnce()).getString(10);
    }

    /**
     * Tests visitLocalVariableTypeInfo with different method instances.
     * The method parameter provides context for the local variable type.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withDifferentMethods() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        localVariableTypeInfo.u2signatureIndex = 15;
        when(clazz.getString(15)).thenReturn("Ljava/util/Collection<*>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method1, codeAttribute, localVariableTypeInfo);
        converter.visitLocalVariableTypeInfo(clazz, method2, codeAttribute, localVariableTypeInfo);

        // Assert - verify processing occurred for both method contexts
        verify(clazz, atLeast(2)).getString(15);
    }

    /**
     * Tests visitLocalVariableTypeInfo with different code attribute instances.
     * The code attribute provides context for the local variable type.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withDifferentCodeAttributes() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);

        localVariableTypeInfo.u2signatureIndex = 20;
        when(clazz.getString(20)).thenReturn("Ljava/util/ArrayList<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttr1, localVariableTypeInfo);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttr2, localVariableTypeInfo);

        // Assert - verify processing occurred for both code attribute contexts
        verify(clazz, atLeast(2)).getString(20);
    }

    /**
     * Tests visitLocalVariableTypeInfo doesn't directly interact with method parameter.
     * The method parameter provides context but isn't directly used.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_doesNotDirectlyInteractWithMethod() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 25;
        when(clazz.getString(25)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert - verify no direct interactions with method
        verifyNoInteractions(method);
    }

    /**
     * Tests visitLocalVariableTypeInfo doesn't directly interact with code attribute parameter.
     * The code attribute parameter provides context but isn't directly used.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_doesNotDirectlyInteractWithCodeAttribute() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 30;
        when(clazz.getString(30)).thenReturn("Ljava/util/Map<TK;TV;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert - verify no direct interactions with code attribute
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests visitLocalVariableTypeInfo doesn't trigger warnings for standard signatures.
     * Processing standard type signatures should not generate warnings.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_doesNotTriggerWarnings() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 35;
        when(clazz.getString(35)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a converter with null warning printer.
     * The method should still process signatures correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        localVariableTypeInfo.u2signatureIndex = 40;
        when(clazz.getString(40)).thenReturn("Ljava/util/Set<*>;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo)
        );
    }

    /**
     * Tests visitLocalVariableTypeInfo with a converter with null class visitor.
     * The method should still process signatures correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        localVariableTypeInfo.u2signatureIndex = 45;
        when(clazz.getString(45)).thenReturn("Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/Integer;>;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo)
        );
    }

    /**
     * Tests visitLocalVariableTypeInfo with a converter with null instruction visitor.
     * The method should still process signatures correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        localVariableTypeInfo.u2signatureIndex = 50;
        when(clazz.getString(50)).thenReturn("Ljava/util/LinkedList<Ljava/lang/String;>;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo)
        );
    }

    /**
     * Tests visitLocalVariableTypeInfo processes signature by calling getString on clazz.
     * This is the key interaction - reading the signature string from the constant pool.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_readsSignatureFromClazz() {
        // Arrange
        int signatureIndex = 100;
        localVariableTypeInfo.u2signatureIndex = signatureIndex;
        when(clazz.getString(signatureIndex)).thenReturn("Ljava/util/Optional<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert - verify the signature was read from the clazz
        verify(clazz, atLeastOnce()).getString(signatureIndex);
    }

    /**
     * Tests visitLocalVariableTypeInfo with various generic collection types.
     * Different generic collections should all be processed correctly.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withVariousCollectionTypes() {
        // Arrange
        LocalVariableTypeInfo info1 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info2 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info3 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info4 = mock(LocalVariableTypeInfo.class);

        info1.u2signatureIndex = 1;
        info2.u2signatureIndex = 2;
        info3.u2signatureIndex = 3;
        info4.u2signatureIndex = 4;

        when(clazz.getString(1)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");
        when(clazz.getString(2)).thenReturn("Ljava/util/Set<Ljava/lang/Integer;>;");
        when(clazz.getString(3)).thenReturn("Ljava/util/Queue<Ljava/lang/Object;>;");
        when(clazz.getString(4)).thenReturn("Ljava/util/Deque<Ljava/lang/Double;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info1);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info2);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info3);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info4);

        // Assert - verify all signatures were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(clazz, atLeastOnce()).getString(4);
    }

    /**
     * Tests visitLocalVariableTypeInfo with multiple type parameters.
     * Generic types with multiple type parameters should be handled correctly.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withMultipleTypeParameters() {
        // Arrange
        LocalVariableTypeInfo info1 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info2 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info3 = mock(LocalVariableTypeInfo.class);

        info1.u2signatureIndex = 1;
        info2.u2signatureIndex = 2;
        info3.u2signatureIndex = 3;

        when(clazz.getString(1)).thenReturn("Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;");
        when(clazz.getString(2)).thenReturn("Ljava/util/Map<Ljava/lang/Integer;Ljava/util/List<Ljava/lang/String;>;>;");
        when(clazz.getString(3)).thenReturn("Lcom/example/Pair<TK;TV;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info1);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info2);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info3);

        // Assert - verify all signatures were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
    }

    /**
     * Tests visitLocalVariableTypeInfo executes quickly.
     * Since it's processing signatures, it should have minimal overhead.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_executesQuickly() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 100;
        when(clazz.getString(100)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitLocalVariableTypeInfo should execute quickly");
    }

    /**
     * Tests visitLocalVariableTypeInfo handles sequential calls independently.
     * Each call should process the signature without interference from previous calls.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_sequentialCallsAreIndependent() {
        // Arrange
        LocalVariableTypeInfo info1 = mock(LocalVariableTypeInfo.class);
        LocalVariableTypeInfo info2 = mock(LocalVariableTypeInfo.class);

        info1.u2signatureIndex = 10;
        info2.u2signatureIndex = 20;

        when(clazz.getString(10)).thenReturn("Ljava/util/List<Ljava/lang/String;>;");
        when(clazz.getString(20)).thenReturn("Ljava/util/Map<Ljava/lang/Integer;Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info1);
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, info2);

        // Assert - verify both were processed independently
        verify(clazz, atLeastOnce()).getString(10);
        verify(clazz, atLeastOnce()).getString(20);
    }

    /**
     * Tests visitLocalVariableTypeInfo with empty class pools.
     * The method should still process signatures even with empty class pools.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        localVariableTypeInfo.u2signatureIndex = 50;
        when(clazz.getString(50)).thenReturn("Ljava/util/Optional<Ljava/lang/String;>;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo)
        );
    }

    /**
     * Tests visitLocalVariableTypeInfo across different converter instances.
     * Different converters should independently process local variable type info.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        localVariableTypeInfo.u2signatureIndex = 60;
        when(clazz.getString(60)).thenReturn("Ljava/util/concurrent/ConcurrentHashMap<Ljava/lang/String;Ljava/lang/Integer;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);
        converter2.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert - verify both converters processed the signature
        verify(clazz, atLeast(2)).getString(60);
    }

    /**
     * Tests visitLocalVariableTypeInfo with functional interface signatures.
     * Lambda and method reference types use generic signatures.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withFunctionalInterfaceSignature() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 70;
        when(clazz.getString(70)).thenReturn("Ljava/util/function/Function<Ljava/lang/String;Ljava/lang/Integer;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(70);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a consumer functional interface signature.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withConsumerSignature() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 75;
        when(clazz.getString(75)).thenReturn("Ljava/util/function/Consumer<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(75);
    }

    /**
     * Tests visitLocalVariableTypeInfo with a supplier functional interface signature.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withSupplierSignature() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 80;
        when(clazz.getString(80)).thenReturn("Ljava/util/function/Supplier<Ljava/lang/Double;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(80);
    }

    /**
     * Tests visitLocalVariableTypeInfo with stream types.
     * Stream types are commonly used with generic parameters.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withStreamSignature() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 85;
        when(clazz.getString(85)).thenReturn("Ljava/util/stream/Stream<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(85);
    }

    /**
     * Tests visitLocalVariableTypeInfo with Optional types.
     * Optional is a common generic wrapper type.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withOptionalSignature() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 90;
        when(clazz.getString(90)).thenReturn("Ljava/util/Optional<Ljava/lang/Integer;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(90);
    }

    /**
     * Tests visitLocalVariableTypeInfo with CompletableFuture signature.
     * CompletableFuture is used for asynchronous programming with generics.
     */
    @Test
    public void testVisitLocalVariableTypeInfo_withCompletableFutureSignature() {
        // Arrange
        localVariableTypeInfo.u2signatureIndex = 95;
        when(clazz.getString(95)).thenReturn("Ljava/util/concurrent/CompletableFuture<Ljava/lang/String;>;");

        // Act
        converter.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(95);
    }
}
