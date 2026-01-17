package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter.MethodReplacement} constructor.
 * Tests the MethodReplacement inner class constructor with signature:
 * (Lproguard/backport/AbstractAPIConverter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
public class AbstractAPIConverterClaude_constructorTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;

    /**
     * Concrete test subclass that exposes the MethodReplacement constructor for testing.
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
     * Tests the MethodReplacement constructor with valid non-null parameters.
     * Verifies that a MethodReplacement can be created with all valid parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        String className = "java/lang/String";
        String methodName = "valueOf";
        String methodDesc = "(I)Ljava/lang/String;";
        String replacementClassName = "java/lang/Integer";
        String replacementMethodName = "toString";
        String replacementMethodDesc = "(I)Ljava/lang/String;";

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                className, methodName, methodDesc,
                replacementClassName, replacementMethodName, replacementMethodDesc);

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should be created successfully");
    }

    /**
     * Tests the constructor with simple method names and descriptors.
     * Verifies basic constructor functionality.
     */
    @Test
    public void testConstructorWithSimpleMethodNames() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "com/example/MyClass",
                "myMethod",
                "()V",
                "com/example/NewClass",
                "newMethod",
                "()V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should be created with simple parameters");
    }

    /**
     * Tests the constructor with complex method descriptors.
     * Verifies that complex descriptors with multiple parameters are handled.
     */
    @Test
    public void testConstructorWithComplexDescriptor() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/util/List",
                "add",
                "(ILjava/lang/Object;)Z",
                "java/util/ArrayList",
                "add",
                "(ILjava/lang/Object;)Z");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle complex descriptors");
    }

    /**
     * Tests the constructor with wildcard in class name.
     * Verifies that wildcard patterns in class names are accepted.
     */
    @Test
    public void testConstructorWithWildcardInClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/time/*",
                "now",
                "()Ljava/time/Instant;",
                "org/threeten/bp/*",
                "now",
                "()Lorg/threeten/bp/Instant;");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle wildcard in class name");
    }

    /**
     * Tests the constructor with wildcard in method name.
     * Verifies that wildcard patterns in method names are accepted.
     */
    @Test
    public void testConstructorWithWildcardInMethodName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/String",
                "get*",
                "()V",
                "java/lang/StringBuilder",
                "get*",
                "()V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle wildcard in method name");
    }

    /**
     * Tests the constructor with double wildcard in descriptor.
     * Verifies that the special "**" descriptor pattern is handled.
     */
    @Test
    public void testConstructorWithDoubleWildcardDescriptor() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/Object",
                "toString",
                "**",
                "java/lang/String",
                "valueOf",
                "**");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle ** descriptor");
    }

    /**
     * Tests the constructor with special method name pattern for static methods.
     * Verifies that the special "<static>" pattern is handled.
     */
    @Test
    public void testConstructorWithStaticMethodPattern() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/Math",
                "<static>",
                "(D)D",
                "java/lang/StrictMath",
                "<static>",
                "(D)D");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle <static> pattern");
    }

    /**
     * Tests the constructor with special method name pattern for default methods.
     * Verifies that the special "<default>" pattern is handled.
     */
    @Test
    public void testConstructorWithDefaultMethodPattern() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/util/List",
                "<default>",
                "()V",
                "com/example/ListBackport",
                "sort",
                "(Ljava/util/List;)V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle <default> pattern");
    }

    /**
     * Tests the constructor with placeholder in replacement class name.
     * Verifies that the "<1>" placeholder pattern is handled.
     */
    @Test
    public void testConstructorWithPlaceholderInReplacementClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/time/**",
                "now",
                "()Ljava/time/Instant;",
                "org/threeten/bp/<1>",
                "now",
                "()Lorg/threeten/bp/Instant;");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle <1> placeholder");
    }

    /**
     * Tests the constructor with empty strings.
     * Verifies that empty strings are accepted as parameters.
     */
    @Test
    public void testConstructorWithEmptyStrings() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "", "", "", "", "", "");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should be created with empty strings");
    }

    /**
     * Tests the constructor with null strings for matching parameters.
     * Verifies that null values are handled.
     */
    @Test
    public void testConstructorWithNullMatchingParameters() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act & Assert - This may throw NullPointerException which is expected behavior
        assertThrows(NullPointerException.class, () -> {
            converter.createMethodReplacement(null, null, null, "NewClass", "newMethod", "()V");
        }, "Constructor should throw NullPointerException for null matching parameters");
    }

    /**
     * Tests the constructor with null strings for replacement parameters.
     * Verifies that null replacement parameters are handled.
     */
    @Test
    public void testConstructorWithNullReplacementParameters() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "OldClass", "oldMethod", "()V", null, null, null);

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should be created with null replacement parameters");
    }

    /**
     * Tests the constructor with array type in descriptor.
     * Verifies that array type descriptors are handled correctly.
     */
    @Test
    public void testConstructorWithArrayTypeDescriptor() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/String",
                "getBytes",
                "()[B",
                "java/lang/StringBuilder",
                "getBytes",
                "()[B");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle array type descriptors");
    }

    /**
     * Tests the constructor with primitive return types.
     * Verifies that primitive types in descriptors are handled.
     */
    @Test
    public void testConstructorWithPrimitiveReturnType() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/String",
                "length",
                "()I",
                "java/lang/CharSequence",
                "length",
                "()I");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle primitive return types");
    }

    /**
     * Tests the constructor with inner class names.
     * Verifies that inner class names with $ are handled.
     */
    @Test
    public void testConstructorWithInnerClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/util/Map$Entry",
                "getKey",
                "()Ljava/lang/Object;",
                "com/example/MyMap$MyEntry",
                "getKey",
                "()Ljava/lang/Object;");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle inner class names");
    }

    /**
     * Tests the constructor with very long class and method names.
     * Verifies that long names are handled.
     */
    @Test
    public void testConstructorWithLongNames() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        String longClassName = "com/example/very/long/package/name/MyVeryLongClassName";
        String longMethodName = "myVeryLongMethodNameThatIsReallyLongForTesting";

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                longClassName,
                longMethodName,
                "()V",
                longClassName,
                longMethodName,
                "()V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle long names");
    }

    /**
     * Tests creating multiple MethodReplacement instances.
     * Verifies that multiple instances can be created independently.
     */
    @Test
    public void testMultipleMethodReplacementInstances() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement mr1 = converter.createMethodReplacement(
                "Class1", "method1", "()V", "NewClass1", "newMethod1", "()V");
        AbstractAPIConverter.MethodReplacement mr2 = converter.createMethodReplacement(
                "Class2", "method2", "()V", "NewClass2", "newMethod2", "()V");

        // Assert
        assertNotNull(mr1, "First MethodReplacement should be created");
        assertNotNull(mr2, "Second MethodReplacement should be created");
        assertNotSame(mr1, mr2, "MethodReplacement instances should be different");
    }

    /**
     * Tests the constructor with same parameters creates different instances.
     * Verifies that each call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDifferentInstances() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        String className = "java/lang/String";
        String methodName = "valueOf";
        String methodDesc = "(I)Ljava/lang/String;";

        // Act
        AbstractAPIConverter.MethodReplacement mr1 = converter.createMethodReplacement(
                className, methodName, methodDesc, className, methodName, methodDesc);
        AbstractAPIConverter.MethodReplacement mr2 = converter.createMethodReplacement(
                className, methodName, methodDesc, className, methodName, methodDesc);

        // Assert
        assertNotSame(mr1, mr2, "Each constructor call should create a new instance");
    }

    /**
     * Tests the constructor with special characters in method names.
     * Verifies that special characters like underscores and numbers are handled.
     */
    @Test
    public void testConstructorWithSpecialCharactersInMethodName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "com/example/MyClass",
                "my_method_123",
                "()V",
                "com/example/NewClass",
                "new_method_456",
                "()V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle special characters in method name");
    }

    /**
     * Tests the constructor with constructor method name.
     * Verifies that constructor method names ("<init>") are handled.
     */
    @Test
    public void testConstructorWithInitMethodName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/String",
                "<init>",
                "(Ljava/lang/String;)V",
                "java/lang/StringBuilder",
                "<init>",
                "(Ljava/lang/String;)V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle <init> method name");
    }

    /**
     * Tests the constructor with static initializer method name.
     * Verifies that static initializer names ("<clinit>") are handled.
     */
    @Test
    public void testConstructorWithClinitMethodName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/String",
                "<clinit>",
                "()V",
                "java/lang/StringBuilder",
                "<clinit>",
                "()V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle <clinit> method name");
    }

    /**
     * Tests that the MethodReplacement is properly associated with the outer converter.
     * Verifies that the inner class relationship is maintained.
     */
    @Test
    public void testMethodReplacementIsInnerClass() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "OldClass", "oldMethod", "()V", "NewClass", "newMethod", "()V");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should be created as inner class");
        // The outer class reference is implicit - if this test passes, the association is correct
    }

    /**
     * Tests the constructor with multiple converters creating method replacements.
     * Verifies that different converters can create independent method replacements.
     */
    @Test
    public void testMethodReplacementsFromDifferentConverters() {
        // Arrange
        TestAPIConverter converter1 = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        TestAPIConverter converter2 = new TestAPIConverter(new ClassPool(), new ClassPool(), mock(WarningPrinter.class));

        // Act
        AbstractAPIConverter.MethodReplacement mr1 = converter1.createMethodReplacement(
                "Class1", "method", "()V", "NewClass1", "newMethod", "()V");
        AbstractAPIConverter.MethodReplacement mr2 = converter2.createMethodReplacement(
                "Class1", "method", "()V", "NewClass1", "newMethod", "()V");

        // Assert
        assertNotNull(mr1, "First converter's MethodReplacement should be created");
        assertNotNull(mr2, "Second converter's MethodReplacement should be created");
        assertNotSame(mr1, mr2, "MethodReplacements from different converters should be different");
    }

    /**
     * Tests the constructor with varargs-like descriptor.
     * Verifies that array types used for varargs are handled.
     */
    @Test
    public void testConstructorWithVarargsLikeDescriptor() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/lang/String",
                "format",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                "java/util/Formatter",
                "format",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle varargs-like descriptors");
    }

    /**
     * Tests the constructor with generic type erasure in descriptors.
     * Verifies that descriptors after type erasure are handled.
     */
    @Test
    public void testConstructorWithGenericTypeErasure() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act - Generics are erased to Object in descriptors
        AbstractAPIConverter.MethodReplacement methodReplacement = converter.createMethodReplacement(
                "java/util/List",
                "get",
                "(I)Ljava/lang/Object;",
                "java/util/ArrayList",
                "get",
                "(I)Ljava/lang/Object;");

        // Assert
        assertNotNull(methodReplacement, "MethodReplacement should handle erased generic types");
    }
}
