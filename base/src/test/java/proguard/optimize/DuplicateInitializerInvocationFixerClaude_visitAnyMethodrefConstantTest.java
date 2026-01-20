package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateInitializerInvocationFixer#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
 *
 * The visitAnyMethodrefConstant method checks if the method reference is to a constructor (<init>),
 * and if so, stores the descriptor and calls referencedMethodAccept to visit the referenced method.
 * This is used to detect when constructor descriptors have been modified by duplicate initializer fixing.
 */
public class DuplicateInitializerInvocationFixerClaude_visitAnyMethodrefConstantTest {

    private DuplicateInitializerInvocationFixer fixer;
    private InstructionVisitor extraAddedInstructionVisitor;
    private Clazz clazz;
    private AnyMethodrefConstant methodrefConstant;

    @BeforeEach
    public void setUp() {
        extraAddedInstructionVisitor = mock(InstructionVisitor.class);
        fixer = new DuplicateInitializerInvocationFixer(extraAddedInstructionVisitor);
        clazz = mock(ProgramClass.class);
        methodrefConstant = mock(MethodrefConstant.class);
    }

    /**
     * Tests that visitAnyMethodrefConstant with a constructor (<init>) processes the method.
     * The method should store the descriptor and call referencedMethodAccept.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withConstructor_processesMethod() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(I)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - should call referencedMethodAccept since it's a constructor
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with a non-constructor method does nothing.
     * Only <init> methods should be processed.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNonConstructor_doesNothing() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("someMethod");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - should not call referencedMethodAccept for non-constructor methods
        verify(methodrefConstant, never()).referencedMethodAccept(any());
    }

    /**
     * Tests that visitAnyMethodrefConstant retrieves the descriptor for constructors.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withConstructor_retrievesDescriptor() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(Ljava/lang/String;)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - getType should be called to retrieve the descriptor
        verify(methodrefConstant).getType(clazz);
    }

    /**
     * Tests that visitAnyMethodrefConstant with empty parameter constructor works.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNoArgConstructor_processesMethod() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert
        verify(methodrefConstant).getName(clazz);
        verify(methodrefConstant).getType(clazz);
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with multi-parameter constructor works.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMultiParamConstructor_processesMethod() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(ILjava/lang/String;Z)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with static initializer does not process.
     * Only instance constructors (<init>) should be processed, not static initializers (<clinit>).
     */
    @Test
    public void testVisitAnyMethodrefConstant_withStaticInitializer_doesNotProcess() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<clinit>");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - should not process static initializers
        verify(methodrefConstant, never()).referencedMethodAccept(any());
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called multiple times.
     */
    @Test
    public void testVisitAnyMethodrefConstant_calledMultipleTimes_handlesEachIndependently() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(MethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("<init>");
        when(methodref1.getType(clazz)).thenReturn("(I)V");

        when(methodref2.getName(clazz)).thenReturn("regularMethod");
        when(methodref2.getType(clazz)).thenReturn("(I)V");

        when(methodref3.getName(clazz)).thenReturn("<init>");
        when(methodref3.getType(clazz)).thenReturn("(Ljava/lang/String;)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodref1);
        fixer.visitAnyMethodrefConstant(clazz, methodref2);
        fixer.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert - only constructors should call referencedMethodAccept
        verify(methodref1).referencedMethodAccept(fixer);
        verify(methodref2, never()).referencedMethodAccept(any());
        verify(methodref3).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with different method names works correctly.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withVariousMethodNames_filtersCorrectly() {
        // Test various method names
        String[] nonConstructorNames = {
            "toString",
            "equals",
            "hashCode",
            "getValue",
            "setValue",
            "init",  // Not "<init>"
            "INIT",  // Wrong case
            "<init",  // Missing >
            "init>",  // Missing <
            ""
        };

        for (String methodName : nonConstructorNames) {
            // Arrange
            AnyMethodrefConstant testMethodref = mock(MethodrefConstant.class);
            when(testMethodref.getName(clazz)).thenReturn(methodName);

            // Act
            fixer.visitAnyMethodrefConstant(clazz, testMethodref);

            // Assert - none of these should trigger referencedMethodAccept
            verify(testMethodref, never()).referencedMethodAccept(any());
        }
    }

    /**
     * Tests that visitAnyMethodrefConstant with constructor checks name using exact match.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withExactConstructorName_processes() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - should use equals for exact match
        verify(methodrefConstant).getName(clazz);
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't throw with null fixer.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullExtraVisitor_doesNotThrow() {
        // Arrange
        DuplicateInitializerInvocationFixer fixerWithoutVisitor = new DuplicateInitializerInvocationFixer();
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(I)V");

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> fixerWithoutVisitor.visitAnyMethodrefConstant(clazz, methodrefConstant));
    }

    /**
     * Tests that visitAnyMethodrefConstant stores descriptor before calling referencedMethodAccept.
     * The descriptor is needed when visitProgramMethod is called.
     */
    @Test
    public void testVisitAnyMethodrefConstant_storesDescriptorBeforeVisiting() {
        // Arrange
        ProgramMethod programMethod = mock(ProgramMethod.class);
        ProgramClass programClass = mock(ProgramClass.class);
        String originalDescriptor = "(I)V";
        String modifiedDescriptor = "(II)V";

        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn(originalDescriptor);

        // Setup the referenced method to return a modified descriptor
        when(programMethod.getDescriptor(programClass)).thenReturn(modifiedDescriptor);

        // Configure to call visitProgramMethod when referencedMethodAccept is called
        doAnswer(invocation -> {
            DuplicateInitializerInvocationFixer visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(programClass, programMethod);
            return null;
        }).when(methodrefConstant).referencedMethodAccept(any());

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - the descriptor should have been retrieved before the visit
        verify(methodrefConstant).getType(clazz);
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with constructor having primitive parameters works.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withPrimitiveParameters_processes() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(IJDZFBCS)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with constructor having object parameters works.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withObjectParameters_processes() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(Ljava/lang/Object;Ljava/util/List;)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with constructor having array parameters works.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withArrayParameters_processes() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("([I[[Ljava/lang/String;)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant can handle different clazz instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentClazz_usesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        when(methodrefConstant.getName(clazz1)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz1)).thenReturn("(I)V");

        when(methodrefConstant.getName(clazz2)).thenReturn("someMethod");

        // Act
        fixer.visitAnyMethodrefConstant(clazz1, methodrefConstant);
        fixer.visitAnyMethodrefConstant(clazz2, methodrefConstant);

        // Assert - should use the correct clazz for each call
        verify(methodrefConstant).getName(clazz1);
        verify(methodrefConstant).getName(clazz2);
        verify(methodrefConstant, times(1)).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant with multiple fixer instances works independently.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMultipleFixers_operateIndependently() {
        // Arrange
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);
        DuplicateInitializerInvocationFixer fixer1 = new DuplicateInitializerInvocationFixer(visitor1);
        DuplicateInitializerInvocationFixer fixer2 = new DuplicateInitializerInvocationFixer(visitor2);

        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(I)V");

        // Act
        fixer1.visitAnyMethodrefConstant(clazz, methodrefConstant);
        fixer2.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - both fixers should process independently
        verify(methodrefConstant, times(2)).referencedMethodAccept(any());
    }

    /**
     * Tests that visitAnyMethodrefConstant correctly identifies constructors vs regular methods.
     */
    @Test
    public void testVisitAnyMethodrefConstant_distinguishesConstructorFromRegularMethod() {
        // Arrange
        AnyMethodrefConstant constructorRef = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodRef = mock(MethodrefConstant.class);

        when(constructorRef.getName(clazz)).thenReturn("<init>");
        when(constructorRef.getType(clazz)).thenReturn("()V");

        when(methodRef.getName(clazz)).thenReturn("method");
        when(methodRef.getType(clazz)).thenReturn("()V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, constructorRef);
        fixer.visitAnyMethodrefConstant(clazz, methodRef);

        // Assert
        verify(constructorRef).referencedMethodAccept(fixer);
        verify(methodRef, never()).referencedMethodAccept(any());
    }

    /**
     * Tests that visitAnyMethodrefConstant handles sequential constructor calls.
     */
    @Test
    public void testVisitAnyMethodrefConstant_sequentialConstructorCalls_processesEach() {
        // Arrange
        AnyMethodrefConstant ctor1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant ctor2 = mock(MethodrefConstant.class);
        AnyMethodrefConstant ctor3 = mock(MethodrefConstant.class);

        when(ctor1.getName(clazz)).thenReturn("<init>");
        when(ctor1.getType(clazz)).thenReturn("()V");

        when(ctor2.getName(clazz)).thenReturn("<init>");
        when(ctor2.getType(clazz)).thenReturn("(I)V");

        when(ctor3.getName(clazz)).thenReturn("<init>");
        when(ctor3.getType(clazz)).thenReturn("(Ljava/lang/String;)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, ctor1);
        fixer.visitAnyMethodrefConstant(clazz, ctor2);
        fixer.visitAnyMethodrefConstant(clazz, ctor3);

        // Assert - all should be processed
        verify(ctor1).referencedMethodAccept(fixer);
        verify(ctor2).referencedMethodAccept(fixer);
        verify(ctor3).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant only checks method name, not descriptor.
     */
    @Test
    public void testVisitAnyMethodrefConstant_checksNameNotDescriptor() {
        // Arrange - constructor with complex descriptor
        when(methodrefConstant.getName(clazz)).thenReturn("<init>");
        when(methodrefConstant.getType(clazz)).thenReturn("(Ljava/util/Map;Ljava/util/List;[IZ)V");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - should process regardless of descriptor complexity
        verify(methodrefConstant).getName(clazz);
        verify(methodrefConstant).getType(clazz);
        verify(methodrefConstant).referencedMethodAccept(fixer);
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't call getType for non-constructors.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNonConstructor_doesNotGetType() {
        // Arrange
        when(methodrefConstant.getName(clazz)).thenReturn("regularMethod");

        // Act
        fixer.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - should not call getType since it's not a constructor
        verify(methodrefConstant).getName(clazz);
        verify(methodrefConstant, never()).getType(any());
    }

    /**
     * Tests that visitAnyMethodrefConstant works with edge case method names.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withEdgeCaseNames_handlesCorrectly() {
        // Test edge cases
        String[][] testCases = {
            {"<init>", "should process"},
            {"< init>", "should not process"},
            {"<init >", "should not process"},
            {" <init>", "should not process"},
            {"<Init>", "should not process"},
            {"<INIT>", "should not process"}
        };

        for (String[] testCase : testCases) {
            // Arrange
            AnyMethodrefConstant testRef = mock(MethodrefConstant.class);
            when(testRef.getName(clazz)).thenReturn(testCase[0]);
            when(testRef.getType(clazz)).thenReturn("()V");

            // Act
            fixer.visitAnyMethodrefConstant(clazz, testRef);

            // Assert
            if (testCase[0].equals("<init>")) {
                verify(testRef).referencedMethodAccept(fixer);
            } else {
                verify(testRef, never()).referencedMethodAccept(any());
            }
        }
    }
}
