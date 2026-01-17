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
 * Test class for {@link GetSignatureChecker}.
 * Tests constructor and visitor methods for checking signature access via reflection.
 */
public class GetSignatureCheckerClaudeTest {

    private WarningPrinter warningPrinter;
    private GetSignatureChecker checker;
    private Clazz mockClazz;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        checker = new GetSignatureChecker(warningPrinter);
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
        GetSignatureChecker newChecker = new GetSignatureChecker(warningPrinter);

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
        GetSignatureChecker newChecker = new GetSignatureChecker(null);

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
        GetSignatureChecker checker1 = new GetSignatureChecker(printer1);
        GetSignatureChecker checker2 = new GetSignatureChecker(printer2);

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

    /**
     * Tests that visitAnyConstant does not trigger any warning prints.
     */
    @Test
    public void testVisitAnyConstant_doesNotTriggerWarning() {
        // Arrange
        Constant mockConstant = mock(Constant.class);

        // Act
        checker.visitAnyConstant(mockClazz, mockConstant);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    // ========== visitMethodrefConstant Tests - Class.getType* methods ==========

    /**
     * Tests visitMethodrefConstant with Class.getType().
     * Should trigger a warning since method name starts with "getType".
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetType() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getType"));
    }

    /**
     * Tests visitMethodrefConstant with Class.getTypeName().
     * Should trigger a warning since method name starts with "getType".
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetTypeName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getTypeName");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getTypeName"));
    }

    /**
     * Tests visitMethodrefConstant with Class.getTypeParameters().
     * Should trigger a warning since method name starts with "getType".
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetTypeParameters() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getTypeParameters");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getTypeParameters"));
    }

    // ========== visitMethodrefConstant Tests - Class.getGeneric* methods ==========

    /**
     * Tests visitMethodrefConstant with Class.getGenericSuperclass().
     * Should trigger a warning since method name starts with "getGeneric".
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetGenericSuperclass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getGenericSuperclass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getGenericSuperclass"));
    }

    /**
     * Tests visitMethodrefConstant with Class.getGenericInterfaces().
     * Should trigger a warning since method name starts with "getGeneric".
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetGenericInterfaces() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getGenericInterfaces");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getGenericInterfaces"));
    }

    // ========== visitMethodrefConstant Tests - Field.getType* methods ==========

    /**
     * Tests visitMethodrefConstant with Field.getType().
     * Should trigger a warning since method name starts with "getType".
     */
    @Test
    public void testVisitMethodrefConstant_withFieldGetType() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getType"));
    }

    /**
     * Tests visitMethodrefConstant with Field.getGenericType().
     * Should trigger a warning since method name starts with "getGeneric".
     */
    @Test
    public void testVisitMethodrefConstant_withFieldGetGenericType() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getGenericType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getGenericType"));
    }

    // ========== visitMethodrefConstant Tests - Method.getType* and getGeneric* methods ==========

    /**
     * Tests visitMethodrefConstant with Method.getGenericReturnType().
     * Should trigger a warning since method name starts with "getGeneric".
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetGenericReturnType() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getGenericReturnType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getGenericReturnType"));
    }

    /**
     * Tests visitMethodrefConstant with Method.getGenericParameterTypes().
     * Should trigger a warning since method name starts with "getGeneric".
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetGenericParameterTypes() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getGenericParameterTypes");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getGenericParameterTypes"));
    }

    /**
     * Tests visitMethodrefConstant with Method.getGenericExceptionTypes().
     * Should trigger a warning since method name starts with "getGeneric".
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetGenericExceptionTypes() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getGenericExceptionTypes");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getGenericExceptionTypes"));
    }

    // ========== visitMethodrefConstant Tests - Non-triggering methods ==========

    /**
     * Tests visitMethodrefConstant with Class.getName().
     * Should not trigger a warning since method name doesn't start with "getType" or "getGeneric".
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetName() {
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
     * Tests visitMethodrefConstant with Class.getSimpleName().
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetSimpleName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getSimpleName");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with Field.getName().
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withFieldGetName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getName");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with Method.getName().
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withMethodGetName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getName");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with Method.invoke().
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withMethodInvoke() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("invoke");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    // ========== visitMethodrefConstant Tests - Different classes ==========

    /**
     * Tests visitMethodrefConstant with a method from a different class.
     * Should not trigger a warning even if method name matches.
     */
    @Test
    public void testVisitMethodrefConstant_withDifferentClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("com/example/MyClass");
        when(methodref.getName(mockClazz)).thenReturn("getType");

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
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with Constructor class (not monitored).
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withConstructorClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Constructor");
        when(methodref.getName(mockClazz)).thenReturn("getGenericParameterTypes");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    // ========== visitMethodrefConstant Tests - Null handling ==========

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
        GetSignatureChecker nullPrinterChecker = new GetSignatureChecker(null);
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> nullPrinterChecker.visitMethodrefConstant(mockClazz, methodref));
    }

    // ========== visitMethodrefConstant Tests - Edge cases ==========

    /**
     * Tests visitMethodrefConstant called multiple times with same method.
     * Should print warning each time.
     */
    @Test
    public void testVisitMethodrefConstant_calledMultipleTimes() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(3)).print(eq("com/example/TestClass"), contains("getType"));
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
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with empty method name.
     * Should not trigger warning since empty string doesn't match checked method prefixes.
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
     * Tests visitMethodrefConstant with case-sensitive method name check.
     * Method names with different casing should not trigger warning.
     */
    @Test
    public void testVisitMethodrefConstant_caseSensitiveMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("GetType"); // Capital G

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
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with partial class name match.
     * Should not trigger for classes that contain target class names as substring.
     */
    @Test
    public void testVisitMethodrefConstant_withPartialClassNameMatch() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/ClassLoader");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with class name prefix match.
     * Should not trigger for classes that start with target class name but have more characters.
     */
    @Test
    public void testVisitMethodrefConstant_withClassNamePrefix() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/ClassValue");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with whitespace in method name.
     * Should not trigger warning as exact prefix match is required.
     */
    @Test
    public void testVisitMethodrefConstant_withWhitespaceInMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn(" getType ");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with different clazz objects.
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
        when(methodref.getName(any())).thenReturn("getType");

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
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Note:") &&
                              msg.contains("getType") &&
                              msg.contains("TestClass"))
        );
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
        when(classMethodref.getName(mockClazz)).thenReturn("getType");

        MethodrefConstant fieldMethodref = mock(MethodrefConstant.class);
        when(fieldMethodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(fieldMethodref.getName(mockClazz)).thenReturn("getType");

        MethodrefConstant methodMethodref = mock(MethodrefConstant.class);
        when(methodMethodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodMethodref.getName(mockClazz)).thenReturn("getGenericReturnType");

        // Act
        checker.visitMethodrefConstant(mockClazz, classMethodref);
        checker.visitMethodrefConstant(mockClazz, fieldMethodref);
        checker.visitMethodrefConstant(mockClazz, methodMethodref);

        // Assert
        verify(warningPrinter, times(3)).print(anyString(), anyString());
    }

    /**
     * Tests that both getType and getGeneric prefixes independently trigger warnings.
     * Ensures the OR condition works correctly for both prefixes.
     */
    @Test
    public void testVisitMethodrefConstant_bothPrefixesIndependentlyTrigger() {
        // Arrange
        MethodrefConstant getTypeMethodref = mock(MethodrefConstant.class);
        when(getTypeMethodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(getTypeMethodref.getName(mockClazz)).thenReturn("getTypeParameters");

        MethodrefConstant getGenericMethodref = mock(MethodrefConstant.class);
        when(getGenericMethodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(getGenericMethodref.getName(mockClazz)).thenReturn("getGenericSuperclass");

        // Reset to have a clean slate
        reset(warningPrinter);

        // Act - Test getType prefix alone
        checker.visitMethodrefConstant(mockClazz, getTypeMethodref);

        // Assert
        verify(warningPrinter, times(1)).print(anyString(), anyString());

        // Reset for second test
        reset(warningPrinter);

        // Act - Test getGeneric prefix alone
        checker.visitMethodrefConstant(mockClazz, getGenericMethodref);

        // Assert
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with multiple different methods in sequence.
     * Verifies proper behavior when switching between triggering and non-triggering methods.
     */
    @Test
    public void testVisitMethodrefConstant_multipleDifferentMethods() {
        // Arrange
        MethodrefConstant triggering1 = mock(MethodrefConstant.class);
        when(triggering1.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(triggering1.getName(mockClazz)).thenReturn("getType");

        MethodrefConstant nonTriggering = mock(MethodrefConstant.class);
        when(nonTriggering.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(nonTriggering.getName(mockClazz)).thenReturn("getName");

        MethodrefConstant triggering2 = mock(MethodrefConstant.class);
        when(triggering2.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(triggering2.getName(mockClazz)).thenReturn("getGenericType");

        // Act
        checker.visitMethodrefConstant(mockClazz, triggering1);
        checker.visitMethodrefConstant(mockClazz, nonTriggering);
        checker.visitMethodrefConstant(mockClazz, triggering2);

        // Assert - Only 2 warnings should be triggered
        verify(warningPrinter, times(2)).print(eq("com/example/TestClass"), anyString());
    }

    /**
     * Tests that the warning message contains "Class" in the formatted output.
     * Verifies the ClassUtil.externalShortClassName is working correctly.
     */
    @Test
    public void testVisitMethodrefConstant_warningMessageContainsClassName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Class.getType") ||
                              (msg.contains("Class") && msg.contains("getType")))
        );
    }

    /**
     * Tests that the warning message contains "Field" for Field reflection.
     */
    @Test
    public void testVisitMethodrefConstant_warningMessageContainsFieldClassName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Field");
        when(methodref.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Field.getType") ||
                              (msg.contains("Field") && msg.contains("getType")))
        );
    }

    /**
     * Tests that the warning message contains "Method" for Method reflection.
     */
    @Test
    public void testVisitMethodrefConstant_warningMessageContainsMethodClassName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/reflect/Method");
        when(methodref.getName(mockClazz)).thenReturn("getGenericReturnType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Method.getGenericReturnType") ||
                              (msg.contains("Method") && msg.contains("getGenericReturnType")))
        );
    }

    /**
     * Tests visitMethodrefConstant doesn't trigger for "get" prefix alone.
     * Methods must start with "getType" or "getGeneric", not just "get".
     */
    @Test
    public void testVisitMethodrefConstant_withGetPrefixOnly() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getDeclaredMethods");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with method names that are superstrings.
     * Methods containing but not starting with "getType" or "getGeneric" should not trigger.
     */
    @Test
    public void testVisitMethodrefConstant_withSuperstringMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("determineGetType");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests that the exact class name match is required.
     * Should not match dotted notation of class names.
     */
    @Test
    public void testVisitMethodrefConstant_exactClassNameMatch() {
        // Arrange - Test with exact match (internal format)
        MethodrefConstant exactMatch = mock(MethodrefConstant.class);
        when(exactMatch.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(exactMatch.getName(mockClazz)).thenReturn("getType");

        // Arrange - Test with dotted notation
        MethodrefConstant dotMatch = mock(MethodrefConstant.class);
        when(dotMatch.getClassName(mockClazz)).thenReturn("java.lang.Class");
        when(dotMatch.getName(mockClazz)).thenReturn("getType");

        // Act
        checker.visitMethodrefConstant(mockClazz, exactMatch);
        checker.visitMethodrefConstant(mockClazz, dotMatch);

        // Assert - Only exact match (slash format) should trigger
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with method names at the boundary.
     * Tests exact prefix match: "getType" and "getGeneric" should trigger, but not shorter.
     */
    @Test
    public void testVisitMethodrefConstant_exactPrefixMatch() {
        // Arrange - Test with exact "getType" (should trigger)
        MethodrefConstant exactGetType = mock(MethodrefConstant.class);
        when(exactGetType.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(exactGetType.getName(mockClazz)).thenReturn("getType");

        // Arrange - Test with exact "getGeneric" (should trigger)
        MethodrefConstant exactGetGeneric = mock(MethodrefConstant.class);
        when(exactGetGeneric.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(exactGetGeneric.getName(mockClazz)).thenReturn("getGeneric");

        // Arrange - Test with shorter "getTyp" (should not trigger)
        MethodrefConstant shorterGetTyp = mock(MethodrefConstant.class);
        when(shorterGetTyp.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(shorterGetTyp.getName(mockClazz)).thenReturn("getTyp");

        // Act
        checker.visitMethodrefConstant(mockClazz, exactGetType);
        checker.visitMethodrefConstant(mockClazz, exactGetGeneric);
        checker.visitMethodrefConstant(mockClazz, shorterGetTyp);

        // Assert - Only 2 matches should trigger
        verify(warningPrinter, times(2)).print(anyString(), anyString());
    }
}
