package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.AccessConstants;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter.MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
 *
 * The matches method checks if a given method reference constant matches the criteria specified
 * in the MethodReplacement. It verifies:
 * 1. Referenced class and method are not null
 * 2. Class name matches (by pattern or inheritance)
 * 3. Method name matches (by pattern or special matchers like &lt;static&gt;/&lt;default&gt;)
 * 4. Method descriptor matches the specified pattern
 *
 * Returns true if all criteria match, false otherwise.
 */
public class AbstractAPIConverterClaude_matchesTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;

    /**
     * Concrete test subclass that exposes the MethodReplacement for testing.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter) {
            super(programClassPool, libraryClassPool, warningPrinter, null, null);
        }

        public MethodReplacement createMethodReplacement(String className,
                                                         String methodName,
                                                         String methodDesc,
                                                         String replacementClassName,
                                                         String replacementMethodName,
                                                         String replacementMethodDesc) {
            return new MethodReplacement(className, methodName, methodDesc,
                                        replacementClassName, replacementMethodName, replacementMethodDesc);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
    }

    /**
     * Tests that matches returns false when referenced class is null.
     * This can happen when the project setup is incorrect and the class is not present.
     */
    @Test
    public void testMatches_withNullReferencedClass_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");
        methodRef.referencedClass = null;
        methodRef.referencedMethod = mock(Method.class);

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when referencedClass is null");
    }

    /**
     * Tests that matches returns false when referenced method is null.
     * This can happen when the project setup is incorrect and the method is not present.
     */
    @Test
    public void testMatches_withNullReferencedMethod_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");
        methodRef.referencedClass = mock(Clazz.class);
        methodRef.referencedMethod = null;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when referencedMethod is null");
    }

    /**
     * Tests that matches returns false when both referenced class and method are null.
     */
    @Test
    public void testMatches_withBothReferencedClassAndMethodNull_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");
        methodRef.referencedClass = null;
        methodRef.referencedMethod = null;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when both referencedClass and referencedMethod are null");
    }

    /**
     * Tests that matches returns true when class name matches exactly.
     */
    @Test
    public void testMatches_withExactClassNameMatch_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true when all criteria match exactly");
    }

    /**
     * Tests that matches returns true when method name matches exactly.
     */
    @Test
    public void testMatches_withExactMethodNameMatch_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "com/example/MyClass", "myMethod", "()V",
                "com/example/NewClass", "newMethod", "()V");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("com/example/MyClass");
        when(methodRef.getName(clazz)).thenReturn("myMethod");
        when(methodRef.getType(clazz)).thenReturn("()V");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("com/example/MyClass");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true when method name matches exactly");
    }

    /**
     * Tests that matches returns true when descriptor matches exactly.
     */
    @Test
    public void testMatches_withExactDescriptorMatch_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "com/example/MyClass", "calculate", "(IILjava/lang/String;)I",
                "com/example/NewClass", "compute", "(IILjava/lang/String;)I");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("com/example/MyClass");
        when(methodRef.getName(clazz)).thenReturn("calculate");
        when(methodRef.getType(clazz)).thenReturn("(IILjava/lang/String;)I");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("com/example/MyClass");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true when descriptor matches exactly");
    }

    /**
     * Tests that matches returns false when class name does not match.
     */
    @Test
    public void testMatches_withNonMatchingClassName_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/Integer");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/Integer");
        when(referencedClass.extendsOrImplements(any(Clazz.class))).thenReturn(false);
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when class name does not match");
    }

    /**
     * Tests that matches returns false when method name does not match.
     */
    @Test
    public void testMatches_withNonMatchingMethodName_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("toString");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when method name does not match");
    }

    /**
     * Tests that matches returns false when descriptor does not match.
     */
    @Test
    public void testMatches_withNonMatchingDescriptor_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(J)Ljava/lang/String;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when descriptor does not match");
    }

    /**
     * Tests that matches returns true with wildcard descriptor pattern "**".
     * The "**" pattern should match any descriptor.
     */
    @Test
    public void testMatches_withWildcardDescriptor_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "**",
                "java/lang/Integer", "toString", "**");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true when descriptor pattern is **");
    }

    /**
     * Tests that matches returns true when matching static methods with &lt;static&gt; pattern.
     * The special &lt;static&gt; pattern should match any static method.
     */
    @Test
    public void testMatches_withStaticMethodPattern_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/Math", "<static>", "(D)D",
                "java/lang/StrictMath", "<static>", "(D)D");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/Math");
        when(methodRef.getName(clazz)).thenReturn("sin");
        when(methodRef.getType(clazz)).thenReturn("(D)D");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/Math");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true for static methods when pattern is <static>");
    }

    /**
     * Tests that matches returns false when &lt;static&gt; pattern is used but method is not static.
     */
    @Test
    public void testMatches_withStaticPatternButNonStaticMethod_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/Math", "<static>", "(D)D",
                "java/lang/StrictMath", "<static>", "(D)D");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/Math");
        when(methodRef.getName(clazz)).thenReturn("sin");
        when(methodRef.getType(clazz)).thenReturn("(D)D");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/Math");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0); // Not static
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when <static> pattern is used but method is not static");
    }

    /**
     * Tests that matches returns true when matching default methods with &lt;default&gt; pattern.
     * Default methods are interface methods that are not abstract.
     */
    @Test
    public void testMatches_withDefaultMethodPattern_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/util/List", "<default>", "()V",
                "com/example/ListBackport", "sort", "(Ljava/util/List;)V");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/util/List");
        when(methodRef.getName(clazz)).thenReturn("sort");
        when(methodRef.getType(clazz)).thenReturn("()V");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/util/List");
        when(referencedClass.getAccessFlags()).thenReturn(AccessConstants.INTERFACE);
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        // Default method: interface method without abstract flag
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true for default methods when pattern is <default>");
    }

    /**
     * Tests that matches returns false when &lt;default&gt; pattern is used but method is abstract.
     */
    @Test
    public void testMatches_withDefaultPatternButAbstractMethod_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/util/List", "<default>", "()V",
                "com/example/ListBackport", "sort", "(Ljava/util/List;)V");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/util/List");
        when(methodRef.getName(clazz)).thenReturn("size");
        when(methodRef.getType(clazz)).thenReturn("()V");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/util/List");
        when(referencedClass.getAccessFlags()).thenReturn(AccessConstants.INTERFACE);
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        // Abstract method
        when(referencedMethod.getAccessFlags()).thenReturn(AccessConstants.ABSTRACT);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when <default> pattern is used but method is abstract");
    }

    /**
     * Tests that matches returns false when &lt;default&gt; pattern is used but class is not interface.
     */
    @Test
    public void testMatches_withDefaultPatternButNotInterface_returnsFalse() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "<default>", "()V",
                "com/example/StringBackport", "method", "(Ljava/lang/String;)V");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("trim");
        when(methodRef.getType(clazz)).thenReturn("()V");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        when(referencedClass.getAccessFlags()).thenReturn(0); // Not an interface
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertFalse(result, "matches should return false when <default> pattern is used but class is not an interface");
    }

    /**
     * Tests that matches returns true when class name matches with wildcard pattern.
     */
    @Test
    public void testMatches_withWildcardInClassName_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/time/*", "now", "()Ljava/time/Instant;",
                "org/threeten/bp/*", "now", "()Lorg/threeten/bp/Instant;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/time/Instant");
        when(methodRef.getName(clazz)).thenReturn("now");
        when(methodRef.getType(clazz)).thenReturn("()Ljava/time/Instant;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/time/Instant");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true when class name matches wildcard pattern");
    }

    /**
     * Tests that matches returns true when method name matches with wildcard pattern.
     */
    @Test
    public void testMatches_withWildcardInMethodName_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "get*", "()V",
                "java/lang/StringBuilder", "get*", "()V");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("getBytes");
        when(methodRef.getType(clazz)).thenReturn("()V");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true when method name matches wildcard pattern");
    }

    /**
     * Tests that matches can be called multiple times with same parameters.
     * The method should be idempotent and return consistent results.
     */
    @Test
    public void testMatches_calledMultipleTimes_returnsConsistentResults() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result1 = replacement.matches(clazz, methodRef);
        boolean result2 = replacement.matches(clazz, methodRef);
        boolean result3 = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that matches works with different method references.
     * Each method reference should be evaluated independently.
     */
    @Test
    public void testMatches_withDifferentMethodRefs_evaluatesIndependently() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);

        // First method ref (matching)
        AnyMethodrefConstant methodRef1 = mock(AnyMethodrefConstant.class);
        when(methodRef1.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef1.getName(clazz)).thenReturn("valueOf");
        when(methodRef1.getType(clazz)).thenReturn("(I)Ljava/lang/String;");
        Clazz referencedClass1 = mock(Clazz.class);
        when(referencedClass1.getName()).thenReturn("java/lang/String");
        methodRef1.referencedClass = referencedClass1;
        Method referencedMethod1 = mock(ProgramMethod.class);
        when(referencedMethod1.getAccessFlags()).thenReturn(0);
        methodRef1.referencedMethod = referencedMethod1;

        // Second method ref (not matching)
        AnyMethodrefConstant methodRef2 = mock(AnyMethodrefConstant.class);
        when(methodRef2.getClassName(clazz)).thenReturn("java/lang/Integer");
        when(methodRef2.getName(clazz)).thenReturn("parseInt");
        when(methodRef2.getType(clazz)).thenReturn("(Ljava/lang/String;)I");
        Clazz referencedClass2 = mock(Clazz.class);
        when(referencedClass2.getName()).thenReturn("java/lang/Integer");
        when(referencedClass2.extendsOrImplements(any(Clazz.class))).thenReturn(false);
        methodRef2.referencedClass = referencedClass2;
        Method referencedMethod2 = mock(ProgramMethod.class);
        when(referencedMethod2.getAccessFlags()).thenReturn(0);
        methodRef2.referencedMethod = referencedMethod2;

        // Act
        boolean result1 = replacement.matches(clazz, methodRef1);
        boolean result2 = replacement.matches(clazz, methodRef2);

        // Assert
        assertTrue(result1, "First method ref should match");
        assertFalse(result2, "Second method ref should not match");
    }

    /**
     * Tests that matches works with constructor methods (&lt;init&gt;).
     */
    @Test
    public void testMatches_withConstructorMethod_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "<init>", "(Ljava/lang/String;)V",
                "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("<init>");
        when(methodRef.getType(clazz)).thenReturn("(Ljava/lang/String;)V");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true for constructor methods");
    }

    /**
     * Tests that matches works with array types in descriptors.
     */
    @Test
    public void testMatches_withArrayTypeDescriptor_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "getBytes", "()[B",
                "java/lang/StringBuilder", "getBytes", "()[B");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("getBytes");
        when(methodRef.getType(clazz)).thenReturn("()[B");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true with array type descriptors");
    }

    /**
     * Tests that matches works with complex descriptors containing multiple parameters.
     */
    @Test
    public void testMatches_withComplexDescriptor_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "com/example/MyClass", "complexMethod", "(Ljava/lang/String;IZLjava/util/List;[Ljava/lang/Object;)Ljava/util/Map;",
                "com/example/NewClass", "newComplexMethod", "(Ljava/lang/String;IZLjava/util/List;[Ljava/lang/Object;)Ljava/util/Map;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("com/example/MyClass");
        when(methodRef.getName(clazz)).thenReturn("complexMethod");
        when(methodRef.getType(clazz)).thenReturn("(Ljava/lang/String;IZLjava/util/List;[Ljava/lang/Object;)Ljava/util/Map;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("com/example/MyClass");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true with complex descriptors");
    }

    /**
     * Tests that matches works with primitive return types.
     */
    @Test
    public void testMatches_withPrimitiveReturnTypes_returnsTrue() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "length", "()I",
                "java/lang/CharSequence", "length", "()I");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("length");
        when(methodRef.getType(clazz)).thenReturn("()I");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act
        boolean result = replacement.matches(clazz, methodRef);

        // Assert
        assertTrue(result, "matches should return true with primitive return types");
    }

    /**
     * Tests that matches doesn't throw exceptions with valid inputs.
     */
    @Test
    public void testMatches_withValidInputs_doesNotThrowException() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.MethodReplacement replacement = converter.createMethodReplacement(
                "java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                "java/lang/Integer", "toString", "(I)Ljava/lang/String;");

        Clazz clazz = mock(ProgramClass.class);
        AnyMethodrefConstant methodRef = mock(AnyMethodrefConstant.class);
        when(methodRef.getClassName(clazz)).thenReturn("java/lang/String");
        when(methodRef.getName(clazz)).thenReturn("valueOf");
        when(methodRef.getType(clazz)).thenReturn("(I)Ljava/lang/String;");

        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("java/lang/String");
        methodRef.referencedClass = referencedClass;

        Method referencedMethod = mock(ProgramMethod.class);
        when(referencedMethod.getAccessFlags()).thenReturn(0);
        methodRef.referencedMethod = referencedMethod;

        // Act & Assert
        assertDoesNotThrow(() -> replacement.matches(clazz, methodRef));
    }
}
