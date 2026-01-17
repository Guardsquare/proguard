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
 * Test class for {@link GetEnclosingMethodChecker}.
 * Tests constructor and visitor methods for checking enclosing method access.
 */
public class GetEnclosingMethodCheckerClaudeTest {

    private WarningPrinter warningPrinter;
    private GetEnclosingMethodChecker checker;
    private Clazz mockClazz;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        checker = new GetEnclosingMethodChecker(warningPrinter);
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
        GetEnclosingMethodChecker newChecker = new GetEnclosingMethodChecker(warningPrinter);

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
        GetEnclosingMethodChecker newChecker = new GetEnclosingMethodChecker(null);

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
        GetEnclosingMethodChecker checker1 = new GetEnclosingMethodChecker(printer1);
        GetEnclosingMethodChecker checker2 = new GetEnclosingMethodChecker(printer2);

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
     * Tests visitMethodrefConstant with Class.getEnclosingMethod().
     * Should trigger a warning since this is an enclosing method access method.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetEnclosingMethod() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getEnclosingMethod"));
    }

    /**
     * Tests visitMethodrefConstant with Class.getEnclosingConstructor().
     * Should trigger a warning since this is an enclosing method access method.
     */
    @Test
    public void testVisitMethodrefConstant_withClassGetEnclosingConstructor() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingConstructor");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(eq("com/example/TestClass"), contains("getEnclosingConstructor"));
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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

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
        GetEnclosingMethodChecker nullPrinterChecker = new GetEnclosingMethodChecker(null);
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(3)).print(eq("com/example/TestClass"), contains("getEnclosingMethod"));
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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

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
     * Verifies that both getEnclosingMethod and getEnclosingConstructor are detected.
     */
    @Test
    public void testVisitMethodrefConstant_withBothCheckedMethods() {
        // Arrange
        MethodrefConstant methodref1 = mock(MethodrefConstant.class);
        when(methodref1.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref1.getName(mockClazz)).thenReturn("getEnclosingMethod");

        MethodrefConstant methodref2 = mock(MethodrefConstant.class);
        when(methodref2.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref2.getName(mockClazz)).thenReturn("getEnclosingConstructor");

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
        when(methodref.getName(mockClazz)).thenReturn("GetEnclosingMethod"); // Capital G

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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

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
        when(methodref1.getName(mockClazz)).thenReturn("getEnclosingClass");

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
        when(methodref.getName(any())).thenReturn("getEnclosingMethod");

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
    public void testVisitMethodrefConstant_warningMessageFormatForGetEnclosingMethod() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Note:") &&
                              msg.contains("getEnclosingMethod") &&
                              msg.contains("TestClass"))
        );
    }

    /**
     * Tests that warning message contains the correct format for getEnclosingConstructor.
     * The message should include "Note:", the class name, and the method call.
     */
    @Test
    public void testVisitMethodrefConstant_warningMessageFormatForGetEnclosingConstructor() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingConstructor");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Note:") &&
                              msg.contains("getEnclosingConstructor") &&
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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

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
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with getEnclosingClass instead of getEnclosingMethod.
     * Should not trigger warning as it's a different method.
     */
    @Test
    public void testVisitMethodrefConstant_withGetEnclosingClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with getDeclaringClass.
     * Should not trigger warning as it's a different method.
     */
    @Test
    public void testVisitMethodrefConstant_withGetDeclaringClass() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getDeclaringClass");

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
        when(methodref.getName(mockClazz)).thenReturn(" getEnclosingMethod ");

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
        when(exactMatch.getName(mockClazz)).thenReturn("getEnclosingMethod");

        // Arrange - Test with slash variations
        MethodrefConstant dotMatch = mock(MethodrefConstant.class);
        when(dotMatch.getClassName(mockClazz)).thenReturn("java.lang.Class");
        when(dotMatch.getName(mockClazz)).thenReturn("getEnclosingMethod");

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
        MethodrefConstant enclosingMethod = mock(MethodrefConstant.class);
        when(enclosingMethod.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(enclosingMethod.getName(mockClazz)).thenReturn("getEnclosingMethod");

        MethodrefConstant enclosingConstructor = mock(MethodrefConstant.class);
        when(enclosingConstructor.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(enclosingConstructor.getName(mockClazz)).thenReturn("getEnclosingConstructor");

        // Reset to have a clean slate
        reset(warningPrinter);

        // Act - Test getEnclosingMethod alone
        checker.visitMethodrefConstant(mockClazz, enclosingMethod);

        // Assert
        verify(warningPrinter, times(1)).print(anyString(), anyString());

        // Reset for second test
        reset(warningPrinter);

        // Act - Test getEnclosingConstructor alone
        checker.visitMethodrefConstant(mockClazz, enclosingConstructor);

        // Assert
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that the warning message contains "Class" in the formatted output.
     * This verifies the ClassUtil.externalShortClassName is working correctly.
     */
    @Test
    public void testVisitMethodrefConstant_warningMessageContainsClassName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethod");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, times(1)).print(
                eq("com/example/TestClass"),
                argThat(msg -> msg.contains("Class.getEnclosingMethod") ||
                              (msg.contains("Class") && msg.contains("getEnclosingMethod")))
        );
    }

    /**
     * Tests visitMethodrefConstant with multiple different methods in sequence.
     * Verifies proper behavior when switching between different method calls.
     */
    @Test
    public void testVisitMethodrefConstant_multipleDifferentMethods() {
        // Arrange
        MethodrefConstant triggering1 = mock(MethodrefConstant.class);
        when(triggering1.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(triggering1.getName(mockClazz)).thenReturn("getEnclosingMethod");

        MethodrefConstant nonTriggering = mock(MethodrefConstant.class);
        when(nonTriggering.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(nonTriggering.getName(mockClazz)).thenReturn("getName");

        MethodrefConstant triggering2 = mock(MethodrefConstant.class);
        when(triggering2.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(triggering2.getName(mockClazz)).thenReturn("getEnclosingConstructor");

        // Act
        checker.visitMethodrefConstant(mockClazz, triggering1);
        checker.visitMethodrefConstant(mockClazz, nonTriggering);
        checker.visitMethodrefConstant(mockClazz, triggering2);

        // Assert - Only 2 warnings should be triggered
        verify(warningPrinter, times(2)).print(eq("com/example/TestClass"), anyString());
    }

    /**
     * Tests visitMethodrefConstant doesn't trigger for other reflection methods.
     * Methods like getDeclaredMethods, getConstructor, etc. should not trigger.
     */
    @Test
    public void testVisitMethodrefConstant_withOtherReflectionMethods() {
        // Arrange
        MethodrefConstant methodref1 = mock(MethodrefConstant.class);
        when(methodref1.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref1.getName(mockClazz)).thenReturn("getDeclaredMethods");

        MethodrefConstant methodref2 = mock(MethodrefConstant.class);
        when(methodref2.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref2.getName(mockClazz)).thenReturn("getConstructor");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref1);
        checker.visitMethodrefConstant(mockClazz, methodref2);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests visitMethodrefConstant with method names that contain the target method names.
     * Should not trigger warning for superstrings.
     */
    @Test
    public void testVisitMethodrefConstant_withSuperstringMethodName() {
        // Arrange
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        when(methodref.getClassName(mockClazz)).thenReturn("java/lang/Class");
        when(methodref.getName(mockClazz)).thenReturn("getEnclosingMethodAndClass");

        // Act
        checker.visitMethodrefConstant(mockClazz, methodref);

        // Assert
        verify(warningPrinter, never()).print(anyString(), anyString());
    }
}
