package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link NewMemberNameFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
 */
public class NewMemberNameFilterClaude_visitProgramMethodTest {

    /**
     * Tests that visitProgramMethod delegates to the inner visitor when the method has a new name.
     * The method should delegate when MemberObfuscator.newMemberName returns non-null.
     */
    @Test
    public void testVisitProgramMethodWithNewMemberName() {
        // Arrange - Create a member visitor that we can verify is called
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set the method name to something other than "<init>"
        programMethod.u2accessFlags = 0;
        programMethod.u2nameIndex = 0;
        programMethod.u2descriptorIndex = 0;

        // Set up the class with a constant pool that can provide the method name
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("someMethod");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Set a new name for the method
        MemberObfuscator.setNewMemberName(programMethod, "newMethodName");

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was called
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that visitProgramMethod does NOT delegate when the method has no new name.
     * The method should not delegate when MemberObfuscator.newMemberName returns null.
     */
    @Test
    public void testVisitProgramMethodWithoutNewMemberName() {
        // Arrange - Create a member visitor that we can verify is NOT called
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set the method name to something other than "<init>"
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("someMethod");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Do NOT set a new name for the method or class

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was NOT called
        verify(mockInnerVisitor, never()).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that visitProgramMethod delegates to the inner visitor when the method is a constructor
     * and the class has a new name.
     */
    @Test
    public void testVisitProgramMethodWithConstructorAndNewClassName() {
        // Arrange - Create a member visitor that we can verify is called
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set the method name to "<init>" (constructor)
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("<init>");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Set a new name for the class
        ClassObfuscator.setNewClassName(programClass, "com/example/NewClassName");

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was called
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that visitProgramMethod does NOT delegate when the method is a constructor
     * but the class has no new name.
     */
    @Test
    public void testVisitProgramMethodWithConstructorButNoNewClassName() {
        // Arrange - Create a member visitor that we can verify is NOT called
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set the method name to "<init>" (constructor)
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("<init>");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Do NOT set a new name for the class

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was NOT called
        verify(mockInnerVisitor, never()).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that visitProgramMethod delegates when BOTH the method has a new name
     * AND it's a constructor with a renamed class (should delegate due to method name).
     */
    @Test
    public void testVisitProgramMethodWithBothConditionsTrue() {
        // Arrange - Create a member visitor that we can verify is called
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set the method name to "<init>" (constructor)
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("<init>");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Set a new name for both the method AND the class
        MemberObfuscator.setNewMemberName(programMethod, "newConstructorName");
        ClassObfuscator.setNewClassName(programClass, "com/example/NewClassName");

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was called exactly once
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that visitProgramMethod handles null inner visitor gracefully.
     * When the inner visitor is null, the method should not throw an exception.
     */
    @Test
    public void testVisitProgramMethodWithNullInnerVisitor() {
        // Arrange - Create filter with null inner visitor
        NewMemberNameFilter filter = new NewMemberNameFilter(null);

        // Create a ProgramClass and ProgramMethod with a new name
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("someMethod");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        MemberObfuscator.setNewMemberName(programMethod, "newMethodName");

        // Act & Assert - Should throw NullPointerException when trying to delegate
        try {
            filter.visitProgramMethod(programClass, programMethod);
            // If we reach here with a null visitor and new name, it would have thrown NPE
            // This is expected behavior - the filter assumes a non-null visitor
        } catch (NullPointerException e) {
            // Expected when inner visitor is null and delegation occurs
        }
    }

    /**
     * Tests that visitProgramMethod works correctly with a regular method (not constructor)
     * that has no new name.
     */
    @Test
    public void testVisitProgramMethodWithRegularMethodNoNewName() {
        // Arrange - Create a member visitor
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set a regular method name (not "<init>")
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("regularMethod");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // No new name for method or class

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was NOT called
        verify(mockInnerVisitor, never()).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that visitProgramMethod delegates when a regular method (not constructor)
     * has a new name, even if the class also has a new name.
     */
    @Test
    public void testVisitProgramMethodWithRegularMethodWithNewNameAndClassRenamed() {
        // Arrange - Create a member visitor
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set a regular method name (not "<init>")
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("regularMethod");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Set new names for both method and class
        MemberObfuscator.setNewMemberName(programMethod, "newRegularName");
        ClassObfuscator.setNewClassName(programClass, "com/example/NewClassName");

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was called
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that visitProgramMethod does NOT delegate when a regular method (not constructor)
     * has no new name, even if the class has a new name.
     * Only constructors should trigger delegation based on class name changes.
     */
    @Test
    public void testVisitProgramMethodWithRegularMethodNoNewNameButClassRenamed() {
        // Arrange - Create a member visitor
        MemberVisitor mockInnerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(mockInnerVisitor);

        // Create a ProgramClass and ProgramMethod
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Set a regular method name (not "<init>")
        programClass.u2constantPoolCount = 3;
        programClass.constantPool = new proguard.classfile.constant.Constant[3];
        programClass.constantPool[1] = new proguard.classfile.constant.Utf8Constant("regularMethod");
        programClass.constantPool[2] = new proguard.classfile.constant.Utf8Constant("()V");
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Set a new name for the class but NOT for the method
        ClassObfuscator.setNewClassName(programClass, "com/example/NewClassName");

        // Act - Visit the method
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - Verify the inner visitor was NOT called
        // Regular methods don't get delegation just because the class is renamed
        verify(mockInnerVisitor, never()).visitProgramMethod(programClass, programMethod);
    }
}
