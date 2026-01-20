package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.ExceptionInfoVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UnreachableExceptionRemover}.
 *
 * Tests all methods in the UnreachableExceptionRemover class:
 * - Constructor with no parameters
 * - Constructor with ExceptionInfoVisitor parameter
 * - visitAnyAttribute (no-op method)
 * - visitCodeAttribute (main processing method)
 * - visitExceptionInfo (exception handler visitor)
 */
public class UnreachableExceptionRemoverClaudeTest {

    private UnreachableExceptionRemover remover;
    private ExceptionInfoVisitor mockExceptionInfoVisitor;

    @BeforeEach
    public void setUp() {
        mockExceptionInfoVisitor = mock(ExceptionInfoVisitor.class);
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests the default constructor creates a UnreachableExceptionRemover.
     */
    @Test
    public void testConstructor_noParameters_createsInstance() {
        // Act
        UnreachableExceptionRemover remover = new UnreachableExceptionRemover();

        // Assert
        assertNotNull(remover, "Constructor should create a non-null instance");
    }

    /**
     * Tests the constructor with null ExceptionInfoVisitor parameter.
     */
    @Test
    public void testConstructor_withNullVisitor_createsInstance() {
        // Act
        UnreachableExceptionRemover remover = new UnreachableExceptionRemover(null);

        // Assert
        assertNotNull(remover, "Constructor should accept null visitor");
    }

    /**
     * Tests the constructor with a valid ExceptionInfoVisitor parameter.
     */
    @Test
    public void testConstructor_withValidVisitor_createsInstance() {
        // Arrange
        ExceptionInfoVisitor visitor = mock(ExceptionInfoVisitor.class);

        // Act
        UnreachableExceptionRemover remover = new UnreachableExceptionRemover(visitor);

        // Assert
        assertNotNull(remover, "Constructor should create instance with visitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_independent() {
        // Act
        UnreachableExceptionRemover remover1 = new UnreachableExceptionRemover();
        UnreachableExceptionRemover remover2 = new UnreachableExceptionRemover(mockExceptionInfoVisitor);
        UnreachableExceptionRemover remover3 = new UnreachableExceptionRemover(null);

        // Assert
        assertNotNull(remover1);
        assertNotNull(remover2);
        assertNotNull(remover3);
        assertNotSame(remover1, remover2);
        assertNotSame(remover2, remover3);
    }

    // =========================================================================
    // visitAnyAttribute Tests
    // =========================================================================

    /**
     * Tests visitAnyAttribute is a no-op that doesn't throw exceptions.
     * This method serves as a default handler for non-Code attributes.
     *
     * Mocking is used here because visitAnyAttribute is a no-op method with an empty body.
     * There is no meaningful behavior to test without mocking.
     */
    @Test
    public void testVisitAnyAttribute_withValidParameters_doesNotThrow() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        Clazz clazz = mock(ProgramClass.class);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> remover.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests visitAnyAttribute with null parameters.
     */
    @Test
    public void testVisitAnyAttribute_withNullParameters_doesNotThrow() {
        // Arrange
        remover = new UnreachableExceptionRemover();

        // Act & Assert
        assertDoesNotThrow(() -> remover.visitAnyAttribute(null, null));
    }

    /**
     * Tests visitAnyAttribute doesn't interact with its parameters.
     *
     * Mocking is used because this is a no-op method - we can only verify
     * it doesn't call any methods on its parameters.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithParameters() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        Clazz clazz = mock(ProgramClass.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        remover.visitAnyAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    // =========================================================================
    // visitCodeAttribute Tests
    // =========================================================================

    /**
     * Tests visitCodeAttribute with a CodeAttribute that has no exceptions.
     */
    @Test
    public void testVisitCodeAttribute_withNoExceptions_completes() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");
        CodeAttribute codeAttribute = createCodeAttribute(clazz, new byte[]{0}, 0);

        // Act & Assert
        assertDoesNotThrow(() -> remover.visitCodeAttribute(clazz, method, codeAttribute));
    }

    /**
     * Tests visitCodeAttribute with empty exception table.
     */
    @Test
    public void testVisitCodeAttribute_withEmptyExceptionTable_leavesTableEmpty() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");
        CodeAttribute codeAttribute = createCodeAttribute(clazz, new byte[]{0}, 0);

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertEquals(0, codeAttribute.u2exceptionTableLength);
    }

    /**
     * Tests visitCodeAttribute with reachable exception handler.
     * Code with invokevirtual (0xB6) can throw exceptions.
     */
    @Test
    public void testVisitCodeAttribute_withReachableException_keepsException() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Create code with invokevirtual instruction (can throw exceptions)
        byte[] code = new byte[]{
            (byte)0xB6, 0x00, 0x01  // invokevirtual (can throw)
        };
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 1);

        // Add an exception that covers the invokevirtual
        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 3, 3, 0);
        codeAttribute.exceptionTable[0] = exceptionInfo;

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - exception should be kept because code can throw
        assertEquals(1, codeAttribute.u2exceptionTableLength);
        assertSame(exceptionInfo, codeAttribute.exceptionTable[0]);
    }

    /**
     * Tests visitCodeAttribute with unreachable exception handler.
     * Code with only return (0xB1) doesn't throw exceptions.
     */
    @Test
    public void testVisitCodeAttribute_withUnreachableException_removesException() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Create code with only return instruction (cannot throw)
        byte[] code = new byte[]{
            (byte)0xB1  // return (cannot throw exceptions)
        };
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 1);

        // Add an exception handler
        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 1, 1, 0);
        codeAttribute.exceptionTable[0] = exceptionInfo;

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - exception should be removed because code can't throw
        assertEquals(0, codeAttribute.u2exceptionTableLength);
    }

    /**
     * Tests visitCodeAttribute with multiple exceptions - some reachable, some not.
     */
    @Test
    public void testVisitCodeAttribute_withMixedExceptions_removesOnlyUnreachable() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Create code: nop, invokevirtual, nop, return
        byte[] code = new byte[]{
            0x00,              // nop (offset 0)
            (byte)0xB6, 0x00, 0x01,  // invokevirtual (offset 1-3, can throw)
            0x00,              // nop (offset 4)
            (byte)0xB1         // return (offset 5)
        };
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 3);

        // Exception 1: covers invokevirtual (reachable)
        codeAttribute.exceptionTable[0] = new ExceptionInfo(1, 4, 5, 0);
        // Exception 2: covers only nop (unreachable)
        codeAttribute.exceptionTable[1] = new ExceptionInfo(0, 1, 5, 0);
        // Exception 3: covers return (unreachable)
        codeAttribute.exceptionTable[2] = new ExceptionInfo(5, 6, 6, 0);

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - only the first exception should remain
        assertEquals(1, codeAttribute.u2exceptionTableLength);
        assertEquals(1, codeAttribute.exceptionTable[0].u2startPC);
    }

    /**
     * Tests visitCodeAttribute with exception visitor callback.
     */
    @Test
    public void testVisitCodeAttribute_withExceptionVisitor_callsVisitorForRemovedExceptions() {
        // Arrange
        remover = new UnreachableExceptionRemover(mockExceptionInfoVisitor);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Code that doesn't throw exceptions
        byte[] code = new byte[]{(byte)0xB1};  // return
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 1);
        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 1, 1, 0);
        codeAttribute.exceptionTable[0] = exceptionInfo;

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - visitor should be called for removed exception
        verify(mockExceptionInfoVisitor, times(1))
            .visitExceptionInfo(eq(clazz), eq(method), eq(codeAttribute), any(ExceptionInfo.class));
    }

    /**
     * Tests visitCodeAttribute doesn't call visitor when no exceptions are removed.
     */
    @Test
    public void testVisitCodeAttribute_withExceptionVisitor_doesNotCallVisitorWhenNoRemoval() {
        // Arrange
        remover = new UnreachableExceptionRemover(mockExceptionInfoVisitor);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Code that can throw exceptions
        byte[] code = new byte[]{(byte)0xB6, 0x00, 0x01};  // invokevirtual
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 1);
        codeAttribute.exceptionTable[0] = new ExceptionInfo(0, 3, 3, 0);

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - visitor should not be called since exception is kept
        verify(mockExceptionInfoVisitor, never())
            .visitExceptionInfo(any(), any(), any(), any());
    }

    // =========================================================================
    // visitExceptionInfo Tests
    // =========================================================================

    /**
     * Tests visitExceptionInfo marks exception as empty when code can't throw.
     */
    @Test
    public void testVisitExceptionInfo_withNonThrowingCode_marksExceptionEmpty() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        byte[] code = new byte[]{(byte)0xB1};  // return
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 1, 5, 0);

        // Act
        remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

        // Assert - exception range should be collapsed (startPC == endPC)
        assertEquals(exceptionInfo.u2startPC, exceptionInfo.u2endPC);
    }

    /**
     * Tests visitExceptionInfo keeps exception when code can throw.
     */
    @Test
    public void testVisitExceptionInfo_withThrowingCode_keepsException() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        byte[] code = new byte[]{(byte)0xB6, 0x00, 0x01};  // invokevirtual
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 3, 5, 0);
        int originalEndPC = exceptionInfo.u2endPC;

        // Act
        remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

        // Assert - exception range should remain unchanged
        assertEquals(originalEndPC, exceptionInfo.u2endPC);
        assertTrue(exceptionInfo.u2startPC < exceptionInfo.u2endPC);
    }

    /**
     * Tests visitExceptionInfo with extra visitor for removed exceptions.
     */
    @Test
    public void testVisitExceptionInfo_withExtraVisitor_callsVisitorWhenRemoved() {
        // Arrange
        remover = new UnreachableExceptionRemover(mockExceptionInfoVisitor);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        byte[] code = new byte[]{(byte)0xB1};  // return (doesn't throw)
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 1, 5, 0);

        // Act
        remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

        // Assert
        verify(mockExceptionInfoVisitor, times(1))
            .visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);
    }

    /**
     * Tests visitExceptionInfo with extra visitor doesn't call when exception is kept.
     */
    @Test
    public void testVisitExceptionInfo_withExtraVisitor_doesNotCallWhenKept() {
        // Arrange
        remover = new UnreachableExceptionRemover(mockExceptionInfoVisitor);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        byte[] code = new byte[]{(byte)0xB6, 0x00, 0x01};  // invokevirtual (can throw)
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 3, 5, 0);

        // Act
        remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

        // Assert
        verify(mockExceptionInfoVisitor, never())
            .visitExceptionInfo(any(), any(), any(), any());
    }

    /**
     * Tests visitExceptionInfo with zero-length exception range.
     */
    @Test
    public void testVisitExceptionInfo_withZeroLengthRange_handlesGracefully() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        byte[] code = new byte[]{(byte)0xB1};
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        // Exception with startPC == endPC (already empty)
        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 0, 5, 0);

        // Act & Assert
        assertDoesNotThrow(() ->
            remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo)
        );
    }

    /**
     * Tests visitExceptionInfo with exception covering multiple instructions.
     */
    @Test
    public void testVisitExceptionInfo_withMultipleInstructions_evaluatesAll() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Multiple nop instructions followed by invokevirtual
        byte[] code = new byte[]{
            0x00, 0x00, 0x00,           // nop, nop, nop
            (byte)0xB6, 0x00, 0x01      // invokevirtual (can throw)
        };
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        // Exception covering all instructions including the invokevirtual
        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 6, 6, 0);
        int originalEndPC = exceptionInfo.u2endPC;

        // Act
        remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

        // Assert - should keep exception because code contains throwing instruction
        assertEquals(originalEndPC, exceptionInfo.u2endPC);
    }

    /**
     * Tests visitExceptionInfo with exception covering only non-throwing instructions.
     */
    @Test
    public void testVisitExceptionInfo_withOnlyNonThrowingInstructions_removesException() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Only nop instructions (cannot throw)
        byte[] code = new byte[]{
            0x00, 0x00, 0x00, 0x00, 0x00  // nop x5
        };
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 5, 5, 0);

        // Act
        remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

        // Assert - should mark as empty (startPC == endPC)
        assertEquals(exceptionInfo.u2startPC, exceptionInfo.u2endPC);
    }

    /**
     * Tests visitExceptionInfo with various throwing instructions.
     */
    @Test
    public void testVisitExceptionInfo_withVariousThrowingInstructions_keepsException() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Test with athrow instruction (explicitly throws)
        byte[] code = new byte[]{(byte)0xBF};  // athrow
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 0);
        codeAttribute.code = code;

        ExceptionInfo exceptionInfo = new ExceptionInfo(0, 1, 5, 0);
        int originalEndPC = exceptionInfo.u2endPC;

        // Act
        remover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

        // Assert - should keep exception
        assertEquals(originalEndPC, exceptionInfo.u2endPC);
    }

    /**
     * Tests the complete flow with visitCodeAttribute and visitExceptionInfo.
     */
    @Test
    public void testCompleteFlow_multipleExceptions_correctlyFiltered() {
        // Arrange
        ExceptionInfoVisitor trackingVisitor = mock(ExceptionInfoVisitor.class);
        remover = new UnreachableExceptionRemover(trackingVisitor);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Code with both throwing and non-throwing sections
        byte[] code = new byte[]{
            0x00,                       // nop (offset 0)
            (byte)0xB6, 0x00, 0x01,    // invokevirtual (offset 1-3, throws)
            0x00,                       // nop (offset 4)
            (byte)0xB1                  // return (offset 5, doesn't throw)
        };
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 3);

        // Three exceptions: one reachable, two unreachable
        codeAttribute.exceptionTable[0] = new ExceptionInfo(1, 4, 5, 0);  // reachable (covers invokevirtual)
        codeAttribute.exceptionTable[1] = new ExceptionInfo(0, 1, 5, 0);  // unreachable (only nop)
        codeAttribute.exceptionTable[2] = new ExceptionInfo(5, 6, 6, 0);  // unreachable (only return)

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertEquals(1, codeAttribute.u2exceptionTableLength, "Should keep only reachable exception");
        assertEquals(1, codeAttribute.exceptionTable[0].u2startPC, "First exception should cover invokevirtual");

        // Visitor should be called twice (for two removed exceptions)
        verify(trackingVisitor, times(2))
            .visitExceptionInfo(eq(clazz), eq(method), eq(codeAttribute), any(ExceptionInfo.class));
    }

    /**
     * Tests behavior when all exceptions are unreachable.
     */
    @Test
    public void testVisitCodeAttribute_allExceptionsUnreachable_removesAll() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Code with only non-throwing instructions
        byte[] code = new byte[]{0x00, 0x00, (byte)0xB1};  // nop, nop, return
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 2);

        codeAttribute.exceptionTable[0] = new ExceptionInfo(0, 1, 3, 0);
        codeAttribute.exceptionTable[1] = new ExceptionInfo(1, 2, 3, 0);

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertEquals(0, codeAttribute.u2exceptionTableLength);
    }

    /**
     * Tests behavior when all exceptions are reachable.
     */
    @Test
    public void testVisitCodeAttribute_allExceptionsReachable_keepsAll() {
        // Arrange
        remover = new UnreachableExceptionRemover();
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMethodWithDescriptor(clazz, "test", "()V");

        // Code with multiple throwing instructions
        byte[] code = new byte[]{
            (byte)0xB6, 0x00, 0x01,    // invokevirtual
            (byte)0xB6, 0x00, 0x02     // invokevirtual
        };
        CodeAttribute codeAttribute = createCodeAttribute(clazz, code, 2);

        codeAttribute.exceptionTable[0] = new ExceptionInfo(0, 3, 6, 0);
        codeAttribute.exceptionTable[1] = new ExceptionInfo(3, 6, 6, 0);

        // Act
        remover.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertEquals(2, codeAttribute.u2exceptionTableLength);
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
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
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass clazz, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = 0;

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
     * Creates a CodeAttribute with specific code and exception table size.
     */
    private CodeAttribute createCodeAttribute(ProgramClass clazz, byte[] code, int exceptionTableSize) {
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 10;
        codeAttribute.u4codeLength = code.length;
        codeAttribute.code = code;
        codeAttribute.u2exceptionTableLength = exceptionTableSize;
        codeAttribute.exceptionTable = new ExceptionInfo[Math.max(exceptionTableSize, 1)];
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        // Set attribute name in constant pool
        int attrNameIndex = findAvailableConstantPoolIndex(clazz);
        clazz.constantPool[attrNameIndex] = new Utf8Constant("Code");
        codeAttribute.u2attributeNameIndex = attrNameIndex;

        return codeAttribute;
    }

    /**
     * Finds an available index in the constant pool.
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
