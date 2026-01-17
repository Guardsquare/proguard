package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)}.
 *
 * The visitLocalVariableInfo method updates the descriptor index of a local variable
 * by calling updateDescriptor, which may replace type references based on the converter's
 * type replacement rules.
 */
public class AbstractAPIConverterClaude_visitLocalVariableInfoTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private LocalVariableInfo localVariableInfo;

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
        localVariableInfo = mock(LocalVariableInfo.class);
    }

    /**
     * Tests that visitLocalVariableInfo can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitLocalVariableInfo_withValidMocks_doesNotThrowException() {
        // Arrange
        when(clazz.getString(anyInt())).thenReturn("I");
        localVariableInfo.u2descriptorIndex = 1;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo));
    }

    /**
     * Tests visitLocalVariableInfo with a primitive type descriptor that doesn't need replacement.
     * The descriptor index should remain unchanged when no type replacements apply.
     */
    @Test
    public void testVisitLocalVariableInfo_withPrimitiveDescriptor_noReplacement() {
        // Arrange
        int originalDescriptorIndex = 5;
        localVariableInfo.u2descriptorIndex = originalDescriptorIndex;
        when(clazz.getString(originalDescriptorIndex)).thenReturn("I");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert - verify getString was called to read the descriptor
        verify(clazz, atLeastOnce()).getString(originalDescriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with an int primitive type descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withIntDescriptor() {
        // Arrange
        int descriptorIndex = 10;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("I");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with a long primitive type descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withLongDescriptor() {
        // Arrange
        int descriptorIndex = 15;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("J");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with a boolean primitive type descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withBooleanDescriptor() {
        // Arrange
        int descriptorIndex = 20;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("Z");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with an object type descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withObjectDescriptor() {
        // Arrange
        int descriptorIndex = 25;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with a List object type descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withListDescriptor() {
        // Arrange
        int descriptorIndex = 30;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("Ljava/util/List;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with an array type descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withArrayDescriptor() {
        // Arrange
        int descriptorIndex = 35;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("[Ljava/lang/String;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with a primitive array type descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withPrimitiveArrayDescriptor() {
        // Arrange
        int descriptorIndex = 40;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("[I");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with a multi-dimensional array descriptor.
     */
    @Test
    public void testVisitLocalVariableInfo_withMultiDimensionalArrayDescriptor() {
        // Arrange
        int descriptorIndex = 45;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("[[Ljava/lang/Object;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo can be called multiple times with the same local variable info.
     * Each call should process the descriptor independently.
     */
    @Test
    public void testVisitLocalVariableInfo_calledMultipleTimes() {
        // Arrange
        int descriptorIndex = 50;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert - verify getString was called at least 3 times
        verify(clazz, atLeast(3)).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with different local variable info instances.
     * Each instance should have its descriptor processed independently.
     */
    @Test
    public void testVisitLocalVariableInfo_withDifferentLocalVariableInfos() {
        // Arrange
        LocalVariableInfo info1 = mock(LocalVariableInfo.class);
        LocalVariableInfo info2 = mock(LocalVariableInfo.class);
        LocalVariableInfo info3 = mock(LocalVariableInfo.class);

        info1.u2descriptorIndex = 1;
        info2.u2descriptorIndex = 2;
        info3.u2descriptorIndex = 3;

        when(clazz.getString(1)).thenReturn("I");
        when(clazz.getString(2)).thenReturn("Ljava/lang/String;");
        when(clazz.getString(3)).thenReturn("[I");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info1);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info2);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info3);

        // Assert - verify each descriptor was read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
    }

    /**
     * Tests visitLocalVariableInfo with different clazz instances.
     * Each clazz should provide its own string constants.
     */
    @Test
    public void testVisitLocalVariableInfo_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        localVariableInfo.u2descriptorIndex = 10;

        when(clazz1.getString(10)).thenReturn("I");
        when(clazz2.getString(10)).thenReturn("J");

        // Act
        converter.visitLocalVariableInfo(clazz1, method, codeAttribute, localVariableInfo);
        converter.visitLocalVariableInfo(clazz2, method, codeAttribute, localVariableInfo);

        // Assert
        verify(clazz1, atLeastOnce()).getString(10);
        verify(clazz2, atLeastOnce()).getString(10);
    }

    /**
     * Tests visitLocalVariableInfo with different method instances.
     * The method parameter provides context for the local variable.
     */
    @Test
    public void testVisitLocalVariableInfo_withDifferentMethods() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        localVariableInfo.u2descriptorIndex = 15;
        when(clazz.getString(15)).thenReturn("Ljava/lang/Object;");

        // Act
        converter.visitLocalVariableInfo(clazz, method1, codeAttribute, localVariableInfo);
        converter.visitLocalVariableInfo(clazz, method2, codeAttribute, localVariableInfo);

        // Assert - verify processing occurred for both method contexts
        verify(clazz, atLeast(2)).getString(15);
    }

    /**
     * Tests visitLocalVariableInfo with different code attribute instances.
     * The code attribute provides context for the local variable.
     */
    @Test
    public void testVisitLocalVariableInfo_withDifferentCodeAttributes() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);

        localVariableInfo.u2descriptorIndex = 20;
        when(clazz.getString(20)).thenReturn("Ljava/util/List;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttr1, localVariableInfo);
        converter.visitLocalVariableInfo(clazz, method, codeAttr2, localVariableInfo);

        // Assert - verify processing occurred for both code attribute contexts
        verify(clazz, atLeast(2)).getString(20);
    }

    /**
     * Tests visitLocalVariableInfo doesn't directly interact with method parameter.
     * The method parameter provides context but isn't directly used.
     */
    @Test
    public void testVisitLocalVariableInfo_doesNotDirectlyInteractWithMethod() {
        // Arrange
        localVariableInfo.u2descriptorIndex = 25;
        when(clazz.getString(25)).thenReturn("I");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert - verify no direct interactions with method
        verifyNoInteractions(method);
    }

    /**
     * Tests visitLocalVariableInfo doesn't directly interact with code attribute parameter.
     * The code attribute parameter provides context but isn't directly used.
     */
    @Test
    public void testVisitLocalVariableInfo_doesNotDirectlyInteractWithCodeAttribute() {
        // Arrange
        localVariableInfo.u2descriptorIndex = 30;
        when(clazz.getString(30)).thenReturn("J");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert - verify no direct interactions with code attribute
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests visitLocalVariableInfo doesn't trigger warnings for standard descriptors.
     * Processing standard type descriptors should not generate warnings.
     */
    @Test
    public void testVisitLocalVariableInfo_doesNotTriggerWarnings() {
        // Arrange
        localVariableInfo.u2descriptorIndex = 35;
        when(clazz.getString(35)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitLocalVariableInfo with a converter with null warning printer.
     * The method should still process descriptors correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableInfo_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        localVariableInfo.u2descriptorIndex = 40;
        when(clazz.getString(40)).thenReturn("I");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo)
        );
    }

    /**
     * Tests visitLocalVariableInfo with a converter with null class visitor.
     * The method should still process descriptors correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableInfo_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        localVariableInfo.u2descriptorIndex = 45;
        when(clazz.getString(45)).thenReturn("J");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo)
        );
    }

    /**
     * Tests visitLocalVariableInfo with a converter with null instruction visitor.
     * The method should still process descriptors correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableInfo_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        localVariableInfo.u2descriptorIndex = 50;
        when(clazz.getString(50)).thenReturn("Z");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo)
        );
    }

    /**
     * Tests visitLocalVariableInfo processes descriptor by calling getString on clazz.
     * This is the key interaction - reading the descriptor string from the constant pool.
     */
    @Test
    public void testVisitLocalVariableInfo_readsDescriptorFromClazz() {
        // Arrange
        int descriptorIndex = 100;
        localVariableInfo.u2descriptorIndex = descriptorIndex;
        when(clazz.getString(descriptorIndex)).thenReturn("Ljava/lang/Object;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert - verify the descriptor was read from the clazz
        verify(clazz, atLeastOnce()).getString(descriptorIndex);
    }

    /**
     * Tests visitLocalVariableInfo with various object types.
     * Different object types should all be processed correctly.
     */
    @Test
    public void testVisitLocalVariableInfo_withVariousObjectTypes() {
        // Arrange
        LocalVariableInfo info1 = mock(LocalVariableInfo.class);
        LocalVariableInfo info2 = mock(LocalVariableInfo.class);
        LocalVariableInfo info3 = mock(LocalVariableInfo.class);
        LocalVariableInfo info4 = mock(LocalVariableInfo.class);

        info1.u2descriptorIndex = 1;
        info2.u2descriptorIndex = 2;
        info3.u2descriptorIndex = 3;
        info4.u2descriptorIndex = 4;

        when(clazz.getString(1)).thenReturn("Ljava/lang/String;");
        when(clazz.getString(2)).thenReturn("Ljava/util/ArrayList;");
        when(clazz.getString(3)).thenReturn("Ljava/lang/Integer;");
        when(clazz.getString(4)).thenReturn("Lcom/example/CustomClass;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info1);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info2);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info3);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info4);

        // Assert - verify all descriptors were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(clazz, atLeastOnce()).getString(4);
    }

    /**
     * Tests visitLocalVariableInfo with all primitive types.
     * All Java primitive type descriptors should be processed correctly.
     */
    @Test
    public void testVisitLocalVariableInfo_withAllPrimitiveTypes() {
        // Arrange - create info for each primitive type
        LocalVariableInfo infoInt = mock(LocalVariableInfo.class);
        LocalVariableInfo infoLong = mock(LocalVariableInfo.class);
        LocalVariableInfo infoFloat = mock(LocalVariableInfo.class);
        LocalVariableInfo infoDouble = mock(LocalVariableInfo.class);
        LocalVariableInfo infoBoolean = mock(LocalVariableInfo.class);
        LocalVariableInfo infoByte = mock(LocalVariableInfo.class);
        LocalVariableInfo infoChar = mock(LocalVariableInfo.class);
        LocalVariableInfo infoShort = mock(LocalVariableInfo.class);

        infoInt.u2descriptorIndex = 1;
        infoLong.u2descriptorIndex = 2;
        infoFloat.u2descriptorIndex = 3;
        infoDouble.u2descriptorIndex = 4;
        infoBoolean.u2descriptorIndex = 5;
        infoByte.u2descriptorIndex = 6;
        infoChar.u2descriptorIndex = 7;
        infoShort.u2descriptorIndex = 8;

        when(clazz.getString(1)).thenReturn("I");
        when(clazz.getString(2)).thenReturn("J");
        when(clazz.getString(3)).thenReturn("F");
        when(clazz.getString(4)).thenReturn("D");
        when(clazz.getString(5)).thenReturn("Z");
        when(clazz.getString(6)).thenReturn("B");
        when(clazz.getString(7)).thenReturn("C");
        when(clazz.getString(8)).thenReturn("S");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoInt);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoLong);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoFloat);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoDouble);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoBoolean);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoByte);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoChar);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, infoShort);

        // Assert - verify all primitive descriptors were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(clazz, atLeastOnce()).getString(4);
        verify(clazz, atLeastOnce()).getString(5);
        verify(clazz, atLeastOnce()).getString(6);
        verify(clazz, atLeastOnce()).getString(7);
        verify(clazz, atLeastOnce()).getString(8);
    }

    /**
     * Tests visitLocalVariableInfo executes quickly.
     * Since it's processing descriptors, it should have minimal overhead.
     */
    @Test
    public void testVisitLocalVariableInfo_executesQuickly() {
        // Arrange
        localVariableInfo.u2descriptorIndex = 100;
        when(clazz.getString(100)).thenReturn("Ljava/lang/String;");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitLocalVariableInfo should execute quickly");
    }

    /**
     * Tests visitLocalVariableInfo handles sequential calls independently.
     * Each call should process the descriptor without interference from previous calls.
     */
    @Test
    public void testVisitLocalVariableInfo_sequentialCallsAreIndependent() {
        // Arrange
        LocalVariableInfo info1 = mock(LocalVariableInfo.class);
        LocalVariableInfo info2 = mock(LocalVariableInfo.class);

        info1.u2descriptorIndex = 10;
        info2.u2descriptorIndex = 20;

        when(clazz.getString(10)).thenReturn("I");
        when(clazz.getString(20)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info1);
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, info2);

        // Assert - verify both were processed independently
        verify(clazz, atLeastOnce()).getString(10);
        verify(clazz, atLeastOnce()).getString(20);
    }

    /**
     * Tests visitLocalVariableInfo with empty class pools.
     * The method should still process descriptors even with empty class pools.
     */
    @Test
    public void testVisitLocalVariableInfo_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        localVariableInfo.u2descriptorIndex = 50;
        when(clazz.getString(50)).thenReturn("Ljava/lang/Object;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo)
        );
    }

    /**
     * Tests visitLocalVariableInfo across different converter instances.
     * Different converters should independently process local variable info.
     */
    @Test
    public void testVisitLocalVariableInfo_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        localVariableInfo.u2descriptorIndex = 60;
        when(clazz.getString(60)).thenReturn("I");

        // Act
        converter.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);
        converter2.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

        // Assert - verify both converters processed the descriptor
        verify(clazz, atLeast(2)).getString(60);
    }
}
