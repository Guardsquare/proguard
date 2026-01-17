package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassMemberChecker#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod method is a simple logging method that logs information about a method
 * that might be what the user meant in their configuration. It doesn't modify state or return
 * values, so tests focus on ensuring the method can be invoked without errors.
 */
public class ClassMemberCheckerClaude_visitProgramMethodTest {

    private ClassMemberChecker classMemberChecker;
    private ClassPool programClassPool;
    private WarningPrinter notePrinter;
    private ProgramClass programClass;
    private ProgramMethod programMethod;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        notePrinter = mock(WarningPrinter.class);
        classMemberChecker = new ClassMemberChecker(programClassPool, notePrinter);

        // Create mock instances for ProgramClass and ProgramMethod
        programClass = mock(ProgramClass.class);
        programMethod = mock(ProgramMethod.class);
    }

    /**
     * Tests that visitProgramMethod can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitProgramMethod_withValidMocks_doesNotThrowException() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/MyClass");
        when(programMethod.getName(programClass)).thenReturn("myMethod");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));
    }

    /**
     * Tests visitProgramMethod with a simple method name and void return type.
     */
    @Test
    public void testVisitProgramMethod_withSimpleMethodName() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");
        when(programMethod.getName(programClass)).thenReturn("doSomething");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        // Verify the method information was accessed
        verify(programClass, atLeastOnce()).getName();
        verify(programMethod, atLeastOnce()).getName(programClass);
        verify(programMethod, atLeastOnce()).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with a method that has parameters.
     */
    @Test
    public void testVisitProgramMethod_withParameters() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Calculator");
        when(programMethod.getName(programClass)).thenReturn("add");
        when(programMethod.getDescriptor(programClass)).thenReturn("(II)I");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with a method that has object parameters.
     */
    @Test
    public void testVisitProgramMethod_withObjectParameters() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Service");
        when(programMethod.getName(programClass)).thenReturn("process");
        when(programMethod.getDescriptor(programClass)).thenReturn("(Ljava/lang/String;)V");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with a method that returns an object type.
     */
    @Test
    public void testVisitProgramMethod_withObjectReturnType() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Factory");
        when(programMethod.getName(programClass)).thenReturn("create");
        when(programMethod.getDescriptor(programClass)).thenReturn("()Ljava/lang/Object;");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with a method that has multiple parameters of various types.
     */
    @Test
    public void testVisitProgramMethod_withMultipleParameters() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Processor");
        when(programMethod.getName(programClass)).thenReturn("process");
        when(programMethod.getDescriptor(programClass)).thenReturn("(ILjava/lang/String;Z)Ljava/util/List;");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with an array parameter.
     */
    @Test
    public void testVisitProgramMethod_withArrayParameter() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/ArrayHandler");
        when(programMethod.getName(programClass)).thenReturn("handleArray");
        when(programMethod.getDescriptor(programClass)).thenReturn("([Ljava/lang/String;)V");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with an array return type.
     */
    @Test
    public void testVisitProgramMethod_withArrayReturnType() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/ArrayFactory");
        when(programMethod.getName(programClass)).thenReturn("createArray");
        when(programMethod.getDescriptor(programClass)).thenReturn("()[I");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with a constructor (special method name).
     */
    @Test
    public void testVisitProgramMethod_withConstructor() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/MyClass");
        when(programMethod.getName(programClass)).thenReturn("<init>");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with a static initializer (special method name).
     */
    @Test
    public void testVisitProgramMethod_withStaticInitializer() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/MyClass");
        when(programMethod.getName(programClass)).thenReturn("<clinit>");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with a method that has a long name.
     */
    @Test
    public void testVisitProgramMethod_withLongMethodName() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Service");
        when(programMethod.getName(programClass)).thenReturn("thisIsAVeryLongMethodNameForTesting");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with primitive return types.
     */
    @Test
    public void testVisitProgramMethod_withIntReturnType() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Counter");
        when(programMethod.getName(programClass)).thenReturn("getCount");
        when(programMethod.getDescriptor(programClass)).thenReturn("()I");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with boolean return type.
     */
    @Test
    public void testVisitProgramMethod_withBooleanReturnType() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Validator");
        when(programMethod.getName(programClass)).thenReturn("isValid");
        when(programMethod.getDescriptor(programClass)).thenReturn("()Z");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with long return type.
     */
    @Test
    public void testVisitProgramMethod_withLongReturnType() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Timer");
        when(programMethod.getName(programClass)).thenReturn("getTimestamp");
        when(programMethod.getDescriptor(programClass)).thenReturn("()J");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with double return type.
     */
    @Test
    public void testVisitProgramMethod_withDoubleReturnType() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Calculator");
        when(programMethod.getName(programClass)).thenReturn("sqrt");
        when(programMethod.getDescriptor(programClass)).thenReturn("(D)D");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests that visitProgramMethod can be called multiple times on the same instance.
     */
    @Test
    public void testVisitProgramMethod_calledMultipleTimes() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/MyClass");
        when(programMethod.getName(programClass)).thenReturn("method1");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert - call multiple times
        assertDoesNotThrow(() -> {
            classMemberChecker.visitProgramMethod(programClass, programMethod);
            classMemberChecker.visitProgramMethod(programClass, programMethod);
            classMemberChecker.visitProgramMethod(programClass, programMethod);
        });

        // Verify it was called multiple times
        verify(programClass, atLeast(3)).getName();
        verify(programMethod, atLeast(3)).getName(programClass);
        verify(programMethod, atLeast(3)).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with different methods sequentially.
     */
    @Test
    public void testVisitProgramMethod_withDifferentMethods() {
        // Arrange
        ProgramMethod method1 = mock(ProgramMethod.class);
        ProgramMethod method2 = mock(ProgramMethod.class);

        when(programClass.getName()).thenReturn("com/example/MyClass");

        when(method1.getName(programClass)).thenReturn("method1");
        when(method1.getDescriptor(programClass)).thenReturn("()V");

        when(method2.getName(programClass)).thenReturn("method2");
        when(method2.getDescriptor(programClass)).thenReturn("(I)I");

        // Act & Assert
        assertDoesNotThrow(() -> {
            classMemberChecker.visitProgramMethod(programClass, method1);
            classMemberChecker.visitProgramMethod(programClass, method2);
        });

        verify(method1).getName(programClass);
        verify(method1).getDescriptor(programClass);
        verify(method2).getName(programClass);
        verify(method2).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with different classes.
     */
    @Test
    public void testVisitProgramMethod_withDifferentClasses() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);

        when(class1.getName()).thenReturn("com/example/ClassOne");
        when(class2.getName()).thenReturn("com/example/ClassTwo");

        when(programMethod.getName(class1)).thenReturn("method");
        when(programMethod.getDescriptor(class1)).thenReturn("()V");

        when(programMethod.getName(class2)).thenReturn("method");
        when(programMethod.getDescriptor(class2)).thenReturn("()V");

        // Act & Assert
        assertDoesNotThrow(() -> {
            classMemberChecker.visitProgramMethod(class1, programMethod);
            classMemberChecker.visitProgramMethod(class2, programMethod);
        });

        verify(class1).getName();
        verify(class2).getName();
        verify(programMethod).getName(class1);
        verify(programMethod).getDescriptor(class1);
        verify(programMethod).getName(class2);
        verify(programMethod).getDescriptor(class2);
    }

    /**
     * Tests that ClassMemberChecker can be instantiated with null WarningPrinter.
     * The visitProgramMethod method should still work as it doesn't use the WarningPrinter.
     */
    @Test
    public void testVisitProgramMethod_withNullWarningPrinter() {
        // Arrange
        ClassMemberChecker checkerWithNullPrinter = new ClassMemberChecker(programClassPool, null);
        when(programClass.getName()).thenReturn("com/example/MyClass");
        when(programMethod.getName(programClass)).thenReturn("testMethod");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert
        assertDoesNotThrow(() -> checkerWithNullPrinter.visitProgramMethod(programClass, programMethod));
    }

    /**
     * Tests visitProgramMethod with a method that has nested generic types in descriptor.
     */
    @Test
    public void testVisitProgramMethod_withComplexGenericDescriptor() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/GenericClass");
        when(programMethod.getName(programClass)).thenReturn("process");
        when(programMethod.getDescriptor(programClass)).thenReturn("(Ljava/util/Map;)Ljava/util/List;");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramMethod with fully qualified nested class name.
     */
    @Test
    public void testVisitProgramMethod_withNestedClassName() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/OuterClass$InnerClass");
        when(programMethod.getName(programClass)).thenReturn("innerMethod");
        when(programMethod.getDescriptor(programClass)).thenReturn("()V");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramMethod(programClass, programMethod));

        verify(programClass).getName();
        verify(programMethod).getName(programClass);
        verify(programMethod).getDescriptor(programClass);
    }
}
