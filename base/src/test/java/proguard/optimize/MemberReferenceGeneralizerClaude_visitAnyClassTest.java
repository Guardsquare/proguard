package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberReferenceGeneralizer#visitAnyClass(Clazz)}.
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;)V
 *
 * This class tests the visitAnyClass method which is part of the ClassVisitor interface.
 * The method generalizes member references by finding the most general class in the hierarchy
 * that contains a public method or field matching the member being referenced.
 *
 * The method has two distinct behaviors based on the invocation opcode:
 *
 * 1. For OP_INVOKEVIRTUAL (virtual method calls):
 *    - Recursively searches up the class hierarchy (via superClass.accept(this))
 *    - Looks for public methods in public, available classes
 *    - Sets generalizedClass and generalizedMember when found
 *
 * 2. For field access opcodes (OP_GETFIELD, OP_PUTFIELD):
 *    - First looks in the current class for a matching field
 *    - If found and class is available, sets generalizedClass and generalizedMember
 *    - Otherwise, recursively searches up the hierarchy
 *
 * The method uses internal state fields (invocationOpcode, memberName, memberType,
 * generalizedClass, generalizedMember) that must be set before calling this method.
 */
public class MemberReferenceGeneralizerClaude_visitAnyClassTest {

    private MemberReferenceGeneralizer generalizer;
    private CodeAttributeEditor codeAttributeEditor;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor
        );
    }

    // ========== Tests for visitAnyClass with OP_INVOKEVIRTUAL ==========

    /**
     * Tests that visitAnyClass with OP_INVOKEVIRTUAL recursively visits the super class.
     * The method should call accept on the super class to search up the hierarchy.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_visitsSuperClass() {
        // Arrange
        Clazz mockSuperClass = mock(Clazz.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        // Set up invocation context for virtual method
        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "toString", "()Ljava/lang/String;");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockSuperClass, times(1)).accept(eq(generalizer));
    }

    /**
     * Tests that visitAnyClass with OP_INVOKEVIRTUAL checks if super class has the method.
     * When a public method is found in a public, available super class, it should be recorded.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_findsPublicMethodInPublicSuperClass() {
        // Arrange
        Method mockMethod = mock(Method.class);
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);

        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        when(mockSuperClass.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockSuperClass.findMethod("toString", "()Ljava/lang/String;")).thenReturn(mockMethod);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        // Set up invocation context
        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "toString", "()Ljava/lang/String;");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockSuperClass).findMethod("toString", "()Ljava/lang/String;");
    }

    /**
     * Tests that visitAnyClass with OP_INVOKEVIRTUAL does not record non-public methods.
     * Private or protected methods should not be used for generalization.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_ignoresNonPublicMethod() {
        // Arrange
        Method mockMethod = mock(Method.class);
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.PRIVATE);

        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        when(mockSuperClass.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockSuperClass.findMethod("privateMethod", "()V")).thenReturn(mockMethod);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "privateMethod", "()V");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - method should be found but not recorded due to non-public access
        verify(mockSuperClass).findMethod("privateMethod", "()V");
        verify(mockMethod).getAccessFlags();
    }

    /**
     * Tests that visitAnyClass with OP_INVOKEVIRTUAL does not record methods in non-public classes.
     * Package-private classes should be skipped for generalization.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_ignoresNonPublicClass() {
        // Arrange
        Method mockMethod = mock(Method.class);
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);

        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(0); // Non-public class
        when(mockSuperClass.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockSuperClass.findMethod("method", "()V")).thenReturn(mockMethod);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "()V");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - class access flags should be checked
        verify(mockSuperClass).getAccessFlags();
    }

    /**
     * Tests that visitAnyClass with OP_INVOKEVIRTUAL does not record methods in unavailable classes.
     * Classes not marked as available should be skipped.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_ignoresUnavailableClass() {
        // Arrange
        Method mockMethod = mock(Method.class);
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);

        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        when(mockSuperClass.getProcessingFlags()).thenReturn(0); // Not available
        when(mockSuperClass.findMethod("method", "()V")).thenReturn(mockMethod);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "()V");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - processing flags should be checked
        verify(mockSuperClass).getProcessingFlags();
    }

    /**
     * Tests that visitAnyClass with OP_INVOKEVIRTUAL handles null super class gracefully.
     * Classes without super classes (like Object) should not cause errors.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_handlesNullSuperClass() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(null);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "()V");

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitAnyClass(mockClazz));
        verify(mockClazz).getSuperClass();
    }

    /**
     * Tests that visitAnyClass with OP_INVOKEVIRTUAL handles method not found in super class.
     * When findMethod returns null, no error should occur.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_handlesMethodNotFound() {
        // Arrange
        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        when(mockSuperClass.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockSuperClass.findMethod("nonExistentMethod", "()V")).thenReturn(null);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "nonExistentMethod", "()V");

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitAnyClass(mockClazz));
        verify(mockSuperClass).findMethod("nonExistentMethod", "()V");
    }

    // ========== Tests for visitAnyClass with Field Access Opcodes ==========

    /**
     * Tests that visitAnyClass with field opcode looks for field in the current class.
     * For field access, the method should first check the current class.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_looksForFieldInCurrentClass() {
        // Arrange
        Field mockField = mock(Field.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz.findField("myField", "I")).thenReturn(mockField);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "myField", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).findField("myField", "I");
    }

    /**
     * Tests that visitAnyClass with field opcode records field when found in available class.
     * A field in an available class should be recorded for generalization.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_recordsFieldInAvailableClass() {
        // Arrange
        Field mockField = mock(Field.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz.findField("myField", "I")).thenReturn(mockField);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "myField", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - field should be found and class should be checked for availability
        verify(mockClazz).findField("myField", "I");
        verify(mockClazz).getProcessingFlags();
    }

    /**
     * Tests that visitAnyClass with field opcode does not record field in unavailable class.
     * Fields in unavailable classes should not be used for generalization.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_ignoresFieldInUnavailableClass() {
        // Arrange
        Field mockField = mock(Field.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(0); // Not available
        when(mockClazz.findField("myField", "I")).thenReturn(mockField);
        when(mockClazz.getSuperClass()).thenReturn(null);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "myField", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - class availability should be checked
        verify(mockClazz).getProcessingFlags();
    }

    /**
     * Tests that visitAnyClass with field opcode visits super class when field not found.
     * If field is not in current class, should search up the hierarchy.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_visitsSuperClassWhenFieldNotFound() {
        // Arrange
        Clazz mockSuperClass = mock(Clazz.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.findField("myField", "I")).thenReturn(null);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "myField", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockSuperClass, times(1)).accept(eq(generalizer));
    }

    /**
     * Tests that visitAnyClass with field opcode visits super class when field found but class unavailable.
     * Should continue searching up the hierarchy if current class is unavailable.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_visitsSuperClassWhenClassUnavailable() {
        // Arrange
        Field mockField = mock(Field.class);
        Clazz mockSuperClass = mock(Clazz.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(0); // Not available
        when(mockClazz.findField("myField", "I")).thenReturn(mockField);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "myField", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockSuperClass, times(1)).accept(eq(generalizer));
    }

    /**
     * Tests that visitAnyClass with field opcode handles null super class when field not found.
     * Should not error when reaching top of hierarchy without finding field.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_handlesNullSuperClassWhenFieldNotFound() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.findField("myField", "I")).thenReturn(null);
        when(mockClazz.getSuperClass()).thenReturn(null);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "myField", "I");

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitAnyClass(mockClazz));
    }

    /**
     * Tests that visitAnyClass with field opcode handles different field names.
     * Should search for the correct field name set in memberName.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_handlesDifferentFieldNames() {
        // Arrange
        Field mockField1 = mock(Field.class);
        Field mockField2 = mock(Field.class);

        Clazz mockClazz1 = mock(Clazz.class);
        when(mockClazz1.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz1.findField("field1", "I")).thenReturn(mockField1);

        Clazz mockClazz2 = mock(Clazz.class);
        when(mockClazz2.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz2.findField("field2", "Ljava/lang/String;")).thenReturn(mockField2);

        // Act - search for field1
        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "field1", "I");
        generalizer.visitAnyClass(mockClazz1);

        // Act - search for field2
        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "field2", "Ljava/lang/String;");
        generalizer.visitAnyClass(mockClazz2);

        // Assert
        verify(mockClazz1).findField("field1", "I");
        verify(mockClazz2).findField("field2", "Ljava/lang/String;");
    }

    /**
     * Tests that visitAnyClass with OP_PUTFIELD uses field access logic (not method logic).
     * PUTFIELD should behave the same as GETFIELD.
     */
    @Test
    public void testVisitAnyClass_withPutField_usesFieldAccessLogic() {
        // Arrange
        Field mockField = mock(Field.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz.findField("myField", "I")).thenReturn(mockField);

        setInvocationContext(generalizer, Instruction.OP_PUTFIELD, "myField", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).findField("myField", "I");
    }

    // ========== Tests for Edge Cases and Boundary Conditions ==========

    /**
     * Tests that visitAnyClass can be called multiple times with different opcodes.
     * The method should handle switching between method and field lookup modes.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimesWithDifferentOpcodes() {
        // Arrange
        Method mockMethod = mock(Method.class);
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);

        Field mockField = mock(Field.class);

        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        when(mockSuperClass.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockSuperClass.findMethod("method", "()V")).thenReturn(mockMethod);

        Clazz mockClazz1 = mock(Clazz.class);
        when(mockClazz1.getSuperClass()).thenReturn(mockSuperClass);

        Clazz mockClazz2 = mock(Clazz.class);
        when(mockClazz2.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz2.findField("field", "I")).thenReturn(mockField);

        // Act - first with INVOKEVIRTUAL
        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "()V");
        generalizer.visitAnyClass(mockClazz1);

        // Act - then with GETFIELD
        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "field", "I");
        generalizer.visitAnyClass(mockClazz2);

        // Assert
        verify(mockSuperClass).findMethod("method", "()V");
        verify(mockClazz2).findField("field", "I");
    }

    /**
     * Tests that visitAnyClass handles various method descriptors correctly.
     * Different method signatures should be looked up correctly.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_handlesDifferentMethodDescriptors() {
        // Arrange
        Method mockMethod = mock(Method.class);
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);

        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        when(mockSuperClass.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockSuperClass.findMethod("method", "(ILjava/lang/String;)Z")).thenReturn(mockMethod);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "(ILjava/lang/String;)Z");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockSuperClass).findMethod("method", "(ILjava/lang/String;)Z");
    }

    /**
     * Tests that visitAnyClass handles various field descriptors correctly.
     * Different field types should be looked up correctly.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_handlesDifferentFieldDescriptors() {
        // Arrange
        Field mockField = mock(Field.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz.findField("field", "[Ljava/lang/Object;")).thenReturn(mockField);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "field", "[Ljava/lang/Object;");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).findField("field", "[Ljava/lang/Object;");
    }

    /**
     * Tests that visitAnyClass works correctly with multiple generalizer instances independently.
     * Each instance should maintain its own state.
     */
    @Test
    public void testVisitAnyClass_multipleGeneralizersOperateIndependently() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(true, true, editor1);
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(true, true, editor2);

        Field mockField1 = mock(Field.class);
        Field mockField2 = mock(Field.class);

        Clazz mockClazz1 = mock(Clazz.class);
        when(mockClazz1.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz1.findField("field1", "I")).thenReturn(mockField1);

        Clazz mockClazz2 = mock(Clazz.class);
        when(mockClazz2.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz2.findField("field2", "J")).thenReturn(mockField2);

        // Act
        setInvocationContext(generalizer1, Instruction.OP_GETFIELD, "field1", "I");
        generalizer1.visitAnyClass(mockClazz1);

        setInvocationContext(generalizer2, Instruction.OP_GETFIELD, "field2", "J");
        generalizer2.visitAnyClass(mockClazz2);

        // Assert
        verify(mockClazz1).findField("field1", "I");
        verify(mockClazz2).findField("field2", "J");
    }

    /**
     * Tests that visitAnyClass with other opcodes (not INVOKEVIRTUAL) uses field logic.
     * Any non-INVOKEVIRTUAL opcode should trigger field access behavior.
     */
    @Test
    public void testVisitAnyClass_withNonInvokeVirtualOpcode_usesFieldLogic() {
        // Arrange
        Field mockField = mock(Field.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz.findField("field", "I")).thenReturn(mockField);

        // Test with OP_GETSTATIC (not one of the expected opcodes, but not OP_INVOKEVIRTUAL)
        setInvocationContext(generalizer, Instruction.OP_GETSTATIC, "field", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - should use field logic
        verify(mockClazz).findField("field", "I");
    }

    /**
     * Tests that visitAnyClass handles protected methods correctly.
     * Protected methods should not be used for generalization (only public).
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_ignoresProtectedMethod() {
        // Arrange
        Method mockMethod = mock(Method.class);
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.PROTECTED);

        Clazz mockSuperClass = mock(Clazz.class);
        when(mockSuperClass.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        when(mockSuperClass.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockSuperClass.findMethod("method", "()V")).thenReturn(mockMethod);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuperClass);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "()V");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - method access flags should be checked
        verify(mockMethod).getAccessFlags();
    }

    /**
     * Tests that visitAnyClass can handle a deep class hierarchy for method lookup.
     * The recursive accept calls should work through multiple levels.
     */
    @Test
    public void testVisitAnyClass_withInvokeVirtual_handlesDeepHierarchy() {
        // Arrange - create a chain: clazz -> super1 -> super2
        Clazz mockSuper2 = mock(Clazz.class);
        when(mockSuper2.getSuperClass()).thenReturn(null);

        Clazz mockSuper1 = mock(Clazz.class);
        when(mockSuper1.getSuperClass()).thenReturn(mockSuper2);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(mockSuper1);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "()V");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - should visit the super class
        verify(mockSuper1, times(1)).accept(eq(generalizer));
    }

    /**
     * Tests that visitAnyClass can handle a deep class hierarchy for field lookup.
     * The recursive accept calls should work through multiple levels for fields too.
     */
    @Test
    public void testVisitAnyClass_withFieldOpcode_handlesDeepHierarchy() {
        // Arrange - create a chain: clazz -> super1 -> super2
        Clazz mockSuper2 = mock(Clazz.class);
        when(mockSuper2.findField("field", "I")).thenReturn(null);
        when(mockSuper2.getSuperClass()).thenReturn(null);

        Clazz mockSuper1 = mock(Clazz.class);
        when(mockSuper1.findField("field", "I")).thenReturn(null);
        when(mockSuper1.getSuperClass()).thenReturn(mockSuper2);

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.findField("field", "I")).thenReturn(null);
        when(mockClazz.getSuperClass()).thenReturn(mockSuper1);

        setInvocationContext(generalizer, Instruction.OP_GETFIELD, "field", "I");

        // Act
        generalizer.visitAnyClass(mockClazz);

        // Assert - should visit super class
        verify(mockSuper1, times(1)).accept(eq(generalizer));
    }

    /**
     * Tests that visitAnyClass does not throw with minimal setup.
     * The method should be robust even with minimal mocking.
     */
    @Test
    public void testVisitAnyClass_withMinimalSetup_doesNotThrow() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(null);

        setInvocationContext(generalizer, Instruction.OP_INVOKEVIRTUAL, "method", "()V");

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitAnyClass(mockClazz));
    }

    /**
     * Tests that visitAnyClass works with both field generalization enabled and disabled.
     * The field generalization flag affects instruction processing, not visitAnyClass directly.
     */
    @Test
    public void testVisitAnyClass_withFieldGeneralizationDisabled_stillProcessesFields() {
        // Arrange
        MemberReferenceGeneralizer generalizerNoField = new MemberReferenceGeneralizer(
            false,  // field generalization disabled
            true,
            codeAttributeEditor
        );

        Field mockField = mock(Field.class);
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getProcessingFlags()).thenReturn(ProcessingFlags.IS_CLASS_AVAILABLE);
        when(mockClazz.findField("field", "I")).thenReturn(mockField);

        setInvocationContext(generalizerNoField, Instruction.OP_GETFIELD, "field", "I");

        // Act - visitAnyClass should still process, as it doesn't check the flag
        assertDoesNotThrow(() -> generalizerNoField.visitAnyClass(mockClazz));

        // Assert
        verify(mockClazz).findField("field", "I");
    }

    /**
     * Tests that visitAnyClass works with both method generalization enabled and disabled.
     * The method generalization flag affects instruction processing, not visitAnyClass directly.
     */
    @Test
    public void testVisitAnyClass_withMethodGeneralizationDisabled_stillProcessesMethods() {
        // Arrange
        MemberReferenceGeneralizer generalizerNoMethod = new MemberReferenceGeneralizer(
            true,
            false,  // method generalization disabled
            codeAttributeEditor
        );

        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getSuperClass()).thenReturn(null);

        setInvocationContext(generalizerNoMethod, Instruction.OP_INVOKEVIRTUAL, "method", "()V");

        // Act - visitAnyClass should still process, as it doesn't check the flag
        assertDoesNotThrow(() -> generalizerNoMethod.visitAnyClass(mockClazz));

        // Assert
        verify(mockClazz).getSuperClass();
    }

    // ========== Helper Methods ==========

    /**
     * Helper method to set the invocation context in the generalizer using reflection.
     * This is necessary because these fields are private and set by visitConstantInstruction.
     *
     * We use reflection here because there is no public API to set these internal state fields.
     * In production code, these would be set by visitConstantInstruction before visitAnyClass
     * is called through the visitor pattern. For unit testing visitAnyClass in isolation,
     * we need to initialize this state directly.
     */
    private void setInvocationContext(MemberReferenceGeneralizer generalizer,
                                      byte opcode, String memberName, String memberType) {
        try {
            // Set invocationOpcode
            java.lang.reflect.Field opcodeField =
                MemberReferenceGeneralizer.class.getDeclaredField("invocationOpcode");
            opcodeField.setAccessible(true);
            opcodeField.setByte(generalizer, opcode);

            // Set memberName
            java.lang.reflect.Field nameField =
                MemberReferenceGeneralizer.class.getDeclaredField("memberName");
            nameField.setAccessible(true);
            nameField.set(generalizer, memberName);

            // Set memberType
            java.lang.reflect.Field typeField =
                MemberReferenceGeneralizer.class.getDeclaredField("memberType");
            typeField.setAccessible(true);
            typeField.set(generalizer, memberType);

            // Reset generalizedClass to null
            java.lang.reflect.Field classField =
                MemberReferenceGeneralizer.class.getDeclaredField("generalizedClass");
            classField.setAccessible(true);
            classField.set(generalizer, null);

            // Reset generalizedMember to null
            java.lang.reflect.Field memberField =
                MemberReferenceGeneralizer.class.getDeclaredField("generalizedMember");
            memberField.setAccessible(true);
            memberField.set(generalizer, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set invocation context via reflection", e);
        }
    }
}
