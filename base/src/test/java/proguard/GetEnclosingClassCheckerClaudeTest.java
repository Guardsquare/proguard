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
 * Test class for {@link GetEnclosingClassChecker}.
 * Tests constructor and visitor methods for checking enclosing class access.
 */
public class GetEnclosingClassCheckerClaudeTest {

    private WarningPrinter warningPrinter;
    private GetEnclosingClassChecker checker;
    private Clazz mockClazz;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        checker = new GetEnclosingClassChecker(warningPrinter);
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
        GetEnclosingClassChecker newChecker = new GetEnclosingClassChecker(warningPrinter);

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
        GetEnclosingClassChecker newChecker = new GetEnclosingClassChecker(null);

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
        GetEnclosingClassChecker checker1 = new GetEnclosingClassChecker(printer1);
        GetEnclosingClassChecker checker2 = new GetEnclosingClassChecker(printer2);

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

    // ========== visitMethodrefConstant Tests ==========

    /**
     * Tests visitMethodrefConstant with Class.getEnclosingClass().
     * Should trigger a warning since this is an enclosing class access method.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetEnclosingClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getEnclosingClass"));
    }

    /**
     * Tests visitMethodrefConstant with Class.getDeclaringClass().
     * Should trigger a warning since this is an enclosing class access method.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetDeclaringClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getDeclaringClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getDeclaringClass"));
    }

    /**
     * Tests visitMethodrefConstant with a non-checked method from Class.
     * Should not trigger a warning.
     */
    @Test
    public void testVisitMethodrefConstant_withNonCheckedClassMethod() {
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
     * Tests visitMethodrefConstant with a method from a different class.
     * Should not trigger a warning even if method name matches.
     */
    @Test
    public void testVisitMethodrefConstant_withDifferentClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("com/example/MyClass");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

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
        GetEnclosingClassChecker nullPrinterChecker = new GetEnclosingClassChecker(null);
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(3)).print(eq("com/example/TestClass"), contains("getEnclosingClass"));
    }

    /**
     * Tests visitMethodrefConstant with empty class name.
     * Should not trigger warning since empty string doesn't match java/lang/Class.
     */
    @Test
    public void testVisitMethodrefConstant_withEmptyClassName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

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
     * Tests visitMethodrefConstant with both checked methods.
     * Verifies that both getEnclosingClass and getDeclaringClass are detected.
     */
    @Test
    public void testVisitMethodrefConstant_withBothCheckedMethods() {
        // Arrange
        MethodrefConstant methodref1 = mock(MethodrefConstant.class);
        when(methodref1.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref1.getName(mockClazz)).thenReturn("getEnclosingClass");

        MethodrefConstant methodref2 = mock(MethodrefConstant.class);
        when(methodref2.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref2.getName(mockClazz)).thenReturn("getDeclaringClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref1);
        checker.visitMethodrefConstant(mockClazz, methodref2);

        // Assert
        verify(warningPrinter, times(2)).print(eq("com/example/TestClass"), anyString());
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
        when(methodref.getName(mockClazz)).thenReturn("GetEnclosingClass"); // Capital G

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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

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
        when(methodref1.getName(mockClazz)).thenReturn("getEnclosingMethod");

        MethodrefConstant methodref2 = mock(MethodrefConstant.class);
        when(methodref2.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref2.getName(mockClazz)).thenReturn("getSimpleName");

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
        when(methodref.getName(any())).thenReturn("getEnclosingClass");

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
    public void testVisitMethodrefConstant_warningMessageFormatForGetEnclosingClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Note:") &&
                              msg.contains("getEnclosingClass") &&
                              msg.contains("TestClass"))
        );
    }

    /**
     * Tests that warning message contains the correct format for getDeclaringClass.
     * The message should include "Note:", the class name, and the method call.
     */
    @Test
    public void testVisitMethodrefConstant_warningMessageFormatForGetDeclaringClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getDeclaringClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Note:") &&
                              msg.contains("getDeclaringClass") &&
                              msg.contains("TestClass"))
        );
    }

    /**
     * Tests visitMethodrefConstant with partial class name match.
     * Should not trigger for substrings of the target class name.
     */
    @Test
    public void testVisitMethodrefConstant_withPartialClassNameMatch() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/ClassLoader");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with only class name prefix match.
     * Should not trigger for classes that start with java/lang/Class but have more characters.
     */
    @Test
    public void testVisitMethodrefConstant_withClassNamePrefix() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/ClassValue");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with getEnclosingMethod instead of getEnclosingClass.
     * Should not trigger warning as it's a different method.
     */
    @Test
    public void testVisitMethodrefConstant_withGetEnclosingMethod() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with getEnclosingConstructor.
     * Should not trigger warning as it's a different method.
     */
    @Test
    public void testVisitMethodrefConstant_withGetEnclosingConstructor() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingConstructor");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with a method name that's a substring of checked methods.
     * Should not trigger warning for partial matches.
     */
    @Test
    public void testVisitMethodrefConstant_withSubstringMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosing");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with whitespace in method name.
     * Should not trigger warning as exact match is required.
     */
    @Test
    public void testVisitMethodrefConstant_withWhitespaceInMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn(" getEnclosingClass ");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests that the checker correctly identifies exact class name match.
     * The class name must be exactly "java/lang/Class" to trigger.
     */
    @Test
    public void testVisitMethodrefConstant_exactClassNameMatch() {
        // Arrange - Test with exact match
        MethodrefConstant exactMatch = mock(MethodrefConstant.class);
        when(exactMatch.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(exactMatch.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Arrange - Test with slash variations
        MethodrefConstant dotMatch = mock(MethodrefConstant.class);
        when(dotMatch.getClassName(mockClazz)).thenReturn("java.lang.Class");
        when(dotMatch.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, exactMatch);
        checker.visitMethodrefConstant(mockClazz, dotMatch);

        // Assert - Only exact match should trigger
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that two different method names both trigger warnings.
     * Ensures the OR condition works correctly.
     */
    @Test
    public void testVisitMethodrefConstant_bothMethodNamesIndependentlyTrigger() {
        // Arrange
        MethodrefConstant enclosingClass = mock(MethodrefConstant.class);
        when(enclosingClass.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(enclosingClass.getName(mockClazz)).thenReturn("getEnclosingClass");

        MethodrefConstant declaringClass = mock(MethodrefConstant.class);
        when(declaringClass.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(declaringClass.getName(mockClazz)).thenReturn("getDeclaringClass");

        // Reset to have a clean slate
        reset(warningPrinter);

        // Act - Test getEnclosingClass alone
        checker.visitMethodrefConstant(mockClazz, enclosingClass);

        // Assert
        verify(warningPrinter, times(1)).print(anyString(), anyString());

        // Reset for second test
        reset(warningPrinter);

        // Act - Test getDeclaringClass alone
        checker.visitMethodrefConstant(mockClazz, declaringClass);

        // Assert
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }
}
