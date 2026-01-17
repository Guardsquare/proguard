package proguard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GetAnnotationChecker}.
 * Tests constructor and visitor methods for checking annotation access.
 */
public class GetAnnotationCheckerClaudeTest {

    private WarningPrinter warningPrinter;
    private GetAnnotationChecker checker;
    private Clazz mockClazz;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        checker = new GetAnnotationChecker(warningPrinter);
        mockClazz = mock(Clazz.class);
        when(mockClazz.getName()).thenReturn("com/example/TestClass");
    }

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor with a valid WarningPrinter.
     */
    @Test
    public void testConstructor_withValidWarningPrinter() {
        // Arrange & Act
        GetAnnotationChecker newChecker = new GetAnnotationChecker(warningPrinter);

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated successfully");
    }

    /**
     * Tests the constructor with null WarningPrinter.
     * The constructor should accept null, though it may cause NPE later if warning is triggered.
     */
    @Test
    public void testConstructor_withNullWarningPrinter() {
        // Act
        GetAnnotationChecker newChecker = new GetAnnotationChecker(null);

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated with null warning printer");
    }

    /**
     * Tests that the checker implements ConstantVisitor interface.
     */
    @Test
    public void testConstructor_implementsConstantVisitor() {
        // Assert
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, checker,
                "Checker should implement ConstantVisitor interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances() {
        // Arrange
        WarningPrinter printer1 = mock(WarningPrinter.class);
        WarningPrinter printer2 = mock(WarningPrinter.class);

        // Act
        GetAnnotationChecker checker1 = new GetAnnotationChecker(printer1);
        GetAnnotationChecker checker2 = new GetAnnotationChecker(printer2);

        // Assert
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotSame(checker1, checker2, "Each constructor call should create a new instance");
    }

    // ========== visitAnyConstant Tests ==========

    /**
     * Tests visitAnyConstant with a valid constant.
     * This method should be a no-op and not throw any exceptions.
     */
    @Test
    public void testVisitAnyConstant_withValidConstant() {
        // Arrange
        Constant mockConstant = mock(Constant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyConstant(mockClazz, mockConstant),
                "visitAnyConstant should not throw exceptions");
    }

    /**
     * Tests visitAnyConstant with null clazz.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz() {
        // Arrange
        Constant mockConstant = mock(Constant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyConstant(null, mockConstant),
                "visitAnyConstant should handle null clazz gracefully");
    }

    /**
     * Tests visitAnyConstant with null constant.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant() {
        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyConstant(mockClazz, null),
                "visitAnyConstant should handle null constant gracefully");
    }

    /**
     * Tests visitAnyConstant with both null parameters.
     */
    @Test
    public void testVisitAnyConstant_withBothNull() {
        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyConstant(null, null),
                "visitAnyConstant should handle both null parameters gracefully");
    }

    /**
     * Tests visitAnyConstant called multiple times.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes() {
        // Arrange
        Constant mockConstant = mock(Constant.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyConstant(mockClazz, mockConstant);
            checker.visitAnyConstant(mockClazz, mockConstant);
            checker.visitAnyConstant(mockClazz, mockConstant);
        }, "visitAnyConstant should be idempotent");
    }

    // ========== visitMethodrefConstant Tests ==========

    /**
     * Tests visitMethodrefConstant with Class.getAnnotation().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetAnnotation() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getAnnotation"));
    }

    /**
     * Tests visitMethodrefConstant with Class.getAnnotations().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetAnnotations() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotations");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getAnnotations"));
    }

    /**
     * Tests visitMethodrefConstant with Class.getDeclaredAnnotations().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetDeclaredAnnotations() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getDeclaredAnnotations");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getDeclaredAnnotations"));
    }

    /**
     * Tests visitMethodrefConstant with Field.getAnnotation().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withFieldGetAnnotation() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getAnnotation"));
    }

    /**
     * Tests visitMethodrefConstant with Field.getAnnotations().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withFieldGetAnnotations() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotations");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getAnnotations"));
    }

    /**
     * Tests visitMethodrefConstant with Field.getDeclaredAnnotations().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withFieldGetDeclaredAnnotations() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getDeclaredAnnotations");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getDeclaredAnnotations"));
    }

    /**
     * Tests visitMethodrefConstant with Method.getAnnotation().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetAnnotation() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getAnnotation"));
    }

    /**
     * Tests visitMethodrefConstant with Method.getAnnotations().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetAnnotations() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotations");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getAnnotations"));
    }

    /**
     * Tests visitMethodrefConstant with Method.getDeclaredAnnotations().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetDeclaredAnnotations() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getDeclaredAnnotations");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getDeclaredAnnotations"));
    }

    /**
     * Tests visitMethodrefConstant with Method.getParameterAnnotations().
     * Should trigger a warning since this is an annotation access method.
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetParameterAnnotations() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getParameterAnnotations");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getParameterAnnotations"));
    }

    /**
     * Tests visitMethodrefConstant with a non-annotation method from Class.
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withNonAnnotationClassMethod() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getName");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with a non-annotation method from Field.
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withNonAnnotationFieldMethod() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with a non-annotation method from Method.
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withNonAnnotationMethodMethod() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("invoke");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with a method from a different class.
     * Should not trigger a warning even if method name matches.
     */
    @Test
    public void testVisitMethodrefConstant_withDifferentClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("com/example/MyClass");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with String class.
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withStringClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/String");
        when(methodref.getName(mockClazz)).thenReturn("length");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with null clazz.
     * May throw NullPointerException when accessing getName.
     */
    @Test
    public void testVisitMethodrefConstant_withNullClazz() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> checker.visitMethodrefConstant(null, methodref));
    }

    /**
     * Tests visitMethodrefConstant with null methodref constant.
     * Should throw NullPointerException when accessing getClassName.
     */
    @Test
    public void testVisitMethodrefConstant_withNullMethodref() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> checker.visitMethodrefConstant(mockClazz, null));
    }

    /**
     * Tests visitMethodrefConstant with checker having null warning printer.
     * Should throw NullPointerException when trying to print warning.
     */
    @Test
    public void testVisitMethodrefConstant_withNullWarningPrinter() {
        // Arrange
        GetAnnotationChecker nullPrinterChecker = new GetAnnotationChecker(null);
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> nullPrinterChecker.visitMethodrefConstant(mockClazz, methodref));
    }

    /**
     * Tests visitMethodrefConstant called multiple times with same method.
     * Should print warning each time.
     */
    @Test
    public void testVisitMethodrefConstant_calledMultipleTimes() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(3)).print(eq("com/example/TestClass"), contains("getAnnotation"));
    }

    /**
     * Tests visitMethodrefConstant with empty class name.
     * Should not trigger warning since empty string doesn't match checked classes.
     */
    @Test
    public void testVisitMethodrefConstant_withEmptyClassName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with empty method name.
     * Should not trigger warning since empty string doesn't match checked method names.
     */
    @Test
    public void testVisitMethodrefConstant_withEmptyMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with all three target classes.
     * Verifies that all three classes (Class, Field, Method) are checked.
     */
    @Test
    public void testVisitMethodrefConstant_withAllTargetClasses() {
        // Arrange
        MethodrefConstant classMethodref = mock(MethodrefConstant.class);
        when(classMethodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(classMethodref.getName(mockClazz)).thenReturn("getAnnotation");

        MethodrefConstant fieldMethodref = mock(MethodrefConstant.class);
        when(fieldMethodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(fieldMethodref.getName(mockClazz)).thenReturn("getAnnotation");

        MethodrefConstant methodMethodref = mock(MethodrefConstant.class);
        when(methodMethodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodMethodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, classMethodref);
        checker.visitMethodrefConstant(mockClazz, fieldMethodref);
        checker.visitMethodrefConstant(mockClazz, methodMethodref);

        // Assert
        verify(warningPrinter, times(3)).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with case-sensitive method name check.
     * Method names with different casing should not trigger warning.
     */
    @Test
    public void testVisitMethodrefConstant_caseSensitiveMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("GetAnnotation"); // Capital G

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with case-sensitive class name check.
     * Class names with different casing should not trigger warning.
     */
    @Test
    public void testVisitMethodrefConstant_caseSensitiveClassName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("Java/Lang/Class"); // Capital letters
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with similar but different method names.
     * Should not trigger warning for method names that are substrings or similar.
     */
    @Test
    public void testVisitMethodrefConstant_withSimilarMethodNames() {
        // Arrange
        MethodrefConstant methodref1 = mock(MethodrefConstant.class);
        when(methodref1.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref1.getName(mockClazz)).thenReturn("getAnnotationsByType");

        MethodrefConstant methodref2 = mock(MethodrefConstant.class);
        when(methodref2.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref2.getName(mockClazz)).thenReturn("isAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref1);
        checker.visitMethodrefConstant(mockClazz, methodref2);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with different clazz objects but same methodref.
     * Should print warnings with different class names.
     */
    @Test
    public void testVisitMethodrefConstant_withDifferentClazzObjects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        when(clazz1.getName()).thenReturn("com/example/Class1");

        Clazz clazz2 = mock(Clazz.class);
        when(clazz2.getName()).thenReturn("com/example/Class2");

        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(any())).thenReturn("java/lang/Class");
        when(methodref.getName(any())).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(clazz1, methodref);
        checker.visitMethodrefConstant(clazz2, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/Class1"), anyString());
        verify(warningPrinter, times(1)).print(eq("com/example/Class2"), anyString());
    }

    /**
     * Tests that warning message contains the correct format.
     * The message should include "Note:", the class name, and the method call.
     */
    @Test
    public void testVisitMethodrefConstant_warningMessageFormat() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Note:") &&
                              msg.contains("getAnnotation") &&
                              msg.contains("TestClass"))
        );
    }

    /**
     * Tests visitMethodrefConstant doesn't trigger for Constructor class.
     * Only Class, Field, and Method are monitored.
     */
    @Test
    public void testVisitMethodrefConstant_withConstructorClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Constructor");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with partial class name match.
     * Should not trigger for substrings of the target class names.
     */
    @Test
    public void testVisitMethodrefConstant_withPartialClassNameMatch() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/ClassLoader");
        when(methodref.getName(mockClazz)).thenReturn("getAnnotation");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }
}
