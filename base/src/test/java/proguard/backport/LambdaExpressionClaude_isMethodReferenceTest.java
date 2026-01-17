package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.MethodHandleConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpression#isMethodReference()}.
 * Tests the isMethodReference() method which determines whether the lambda expression
 * is actually a method reference (as opposed to a lambda expression).
 *
 * Method references invoke existing methods, while lambda expressions generate
 * synthetic methods with names starting with "lambda$".
 */
public class LambdaExpressionClaude_isMethodReferenceTest {

    /**
     * Tests isMethodReference for a lambda expression (synthetic method).
     * Lambda expressions have method names starting with "lambda$".
     */
    @Test
    public void testIsMethodReference_LambdaExpression() {
        // Arrange - Lambda expression with synthetic method name
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "lambda$main$0";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "()Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertFalse(result, "Lambda expression with 'lambda$' prefix should not be a method reference");
    }

    /**
     * Tests isMethodReference for a static method reference.
     * Method references invoke existing methods without the "lambda$" prefix.
     */
    @Test
    public void testIsMethodReference_StaticMethodReference() {
        // Arrange - Static method reference
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "valueOf";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Function;",
                new String[]{"java/util/function/Function"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "java/lang/String",
                invokedMethodName,
                "(I)Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Static method reference should return true");
    }

    /**
     * Tests isMethodReference for an instance method reference.
     */
    @Test
    public void testIsMethodReference_InstanceMethodReference() {
        // Arrange - Instance method reference (e.g., String::length)
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "length";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/ToIntFunction;",
                new String[]{"java/util/function/ToIntFunction"},
                new String[0],
                "applyAsInt",
                "(Ljava/lang/Object;)I",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "java/lang/String",
                invokedMethodName,
                "()I",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Instance method reference should return true");
    }

    /**
     * Tests isMethodReference for a constructor reference.
     */
    @Test
    public void testIsMethodReference_ConstructorReference() {
        // Arrange - Constructor reference (e.g., ArrayList::new)
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "<init>";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "java/util/ArrayList",
                invokedMethodName,
                "()V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Constructor reference should return true");
    }

    /**
     * Tests isMethodReference with lambda method name pattern "lambda$test$1".
     */
    @Test
    public void testIsMethodReference_LambdaWithTestMethod() {
        // Arrange - Lambda from test method
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "lambda$test$1";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Predicate;",
                new String[]{"java/util/function/Predicate"},
                new String[0],
                "test",
                "(Ljava/lang/Object;)Z",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "(Ljava/lang/Object;)Z",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertFalse(result, "Lambda with 'lambda$test$1' should not be a method reference");
    }

    /**
     * Tests isMethodReference with lambda method name pattern "lambda$new$0".
     */
    @Test
    public void testIsMethodReference_LambdaInConstructor() {
        // Arrange - Lambda in constructor
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "lambda$new$0";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Consumer;",
                new String[]{"java/util/function/Consumer"},
                new String[0],
                "accept",
                "(Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "(Ljava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertFalse(result, "Lambda in constructor should not be a method reference");
    }

    /**
     * Tests isMethodReference with custom method name (not a lambda).
     */
    @Test
    public void testIsMethodReference_CustomMethodName() {
        // Arrange - Custom user-defined method
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "processData";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Function;",
                new String[]{"java/util/function/Function"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "(Ljava/lang/String;)Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Custom method reference should return true");
    }

    /**
     * Tests isMethodReference with method name starting with "lambda" but not "lambda$".
     */
    @Test
    public void testIsMethodReference_MethodNameStartingWithLambda() {
        // Arrange - Method named "lambdaProcessor" (not synthetic)
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "lambdaProcessor";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Function;",
                new String[]{"java/util/function/Function"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "(Ljava/lang/String;)Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Method name 'lambdaProcessor' (without '$') should be a method reference");
    }

    /**
     * Tests isMethodReference with lambda method name containing multiple dollar signs.
     */
    @Test
    public void testIsMethodReference_LambdaWithMultipleDollarSigns() {
        // Arrange - Lambda with multiple nested contexts
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "lambda$main$0$nested$1";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "()Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertFalse(result, "Lambda method with multiple '$' should not be a method reference");
    }

    /**
     * Tests isMethodReference with empty method name.
     */
    @Test
    public void testIsMethodReference_EmptyMethodName() {
        // Arrange - Empty method name
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Empty method name should be considered a method reference");
    }

    /**
     * Tests isMethodReference with null method name.
     */
    @Test
    public void testIsMethodReference_NullMethodName() {
        // Arrange - Null method name
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = null;

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act & Assert - This may throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            lambdaExpression.isMethodReference();
        }, "isMethodReference should throw NPE with null method name");
    }

    /**
     * Tests isMethodReference with just "lambda$" (edge case).
     */
    @Test
    public void testIsMethodReference_JustLambdaPrefix() {
        // Arrange - Method name is exactly "lambda$"
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "lambda$";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertFalse(result, "Method name 'lambda$' should not be a method reference");
    }

    /**
     * Tests isMethodReference with System.out::println pattern.
     */
    @Test
    public void testIsMethodReference_PrintlnReference() {
        // Arrange - System.out::println
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "println";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Consumer;",
                new String[]{"java/util/function/Consumer"},
                new String[0],
                "accept",
                "(Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "java/io/PrintStream",
                invokedMethodName,
                "(Ljava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "println method reference should return true");
    }

    /**
     * Tests isMethodReference consistency on multiple invocations.
     */
    @Test
    public void testIsMethodReference_ConsistentResults() {
        // Arrange - Lambda expression
        ProgramClass referencedClass = new ProgramClass();
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act - Call multiple times
        boolean result1 = lambdaExpression.isMethodReference();
        boolean result2 = lambdaExpression.isMethodReference();
        boolean result3 = lambdaExpression.isMethodReference();

        // Assert - All should be consistent
        assertFalse(result1, "Should return false for lambda");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests isMethodReference with various lambda naming patterns.
     */
    @Test
    public void testIsMethodReference_VariousLambdaPatterns() {
        ProgramClass referencedClass = new ProgramClass();

        // Test lambda$0
        LambdaExpression lambda1 = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$0", "()Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );
        assertFalse(lambda1.isMethodReference(), "lambda$0 should not be a method reference");

        // Test lambda$static$0
        LambdaExpression lambda2 = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$static$0", "()Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );
        assertFalse(lambda2.isMethodReference(), "lambda$static$0 should not be a method reference");

        // Test lambda$null$0 (lambda in null check)
        LambdaExpression lambda3 = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$null$0", "()Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );
        assertFalse(lambda3.isMethodReference(), "lambda$null$0 should not be a method reference");
    }

    /**
     * Tests isMethodReference with getter method reference.
     */
    @Test
    public void testIsMethodReference_GetterMethodReference() {
        // Arrange - Getter method reference (e.g., Person::getName)
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "getName";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Function;",
                new String[]{"java/util/function/Function"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "Person",
                invokedMethodName,
                "()Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Getter method reference should return true");
    }

    /**
     * Tests isMethodReference with setter method reference.
     */
    @Test
    public void testIsMethodReference_SetterMethodReference() {
        // Arrange - Setter method reference (e.g., Person::setName)
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "setName";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/BiConsumer;",
                new String[]{"java/util/function/BiConsumer"},
                new String[0],
                "accept",
                "(Ljava/lang/Object;Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "Person",
                invokedMethodName,
                "(Ljava/lang/String;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Setter method reference should return true");
    }

    /**
     * Tests isMethodReference with array constructor reference.
     */
    @Test
    public void testIsMethodReference_ArrayConstructorReference() {
        // Arrange - Array constructor reference (e.g., String[]::new)
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "<init>";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/IntFunction;",
                new String[]{"java/util/function/IntFunction"},
                new String[0],
                "apply",
                "(I)Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "[Ljava/lang/String;",
                invokedMethodName,
                "(I)[Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Array constructor reference should return true");
    }

    /**
     * Tests isMethodReference with method name containing special characters.
     */
    @Test
    public void testIsMethodReference_MethodNameWithSpecialCharacters() {
        // Arrange - Method with special characters (but not starting with lambda$)
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "method_with_underscore";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                invokedMethodName,
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "Method with underscore should be a method reference");
    }

    /**
     * Tests isMethodReference with compareTo method reference.
     */
    @Test
    public void testIsMethodReference_CompareToMethodReference() {
        // Arrange - compareTo method reference
        ProgramClass referencedClass = new ProgramClass();
        String invokedMethodName = "compareTo";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/Comparator;",
                new String[]{"java/util/Comparator"},
                new String[0],
                "compare",
                "(Ljava/lang/Object;Ljava/lang/Object;)I",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "java/lang/String",
                invokedMethodName,
                "(Ljava/lang/String;)I",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isMethodReference();

        // Assert
        assertTrue(result, "compareTo method reference should return true");
    }

    /**
     * Tests that lambda expressions and method references are mutually exclusive.
     */
    @Test
    public void testIsMethodReference_MutuallyExclusiveBehavior() {
        ProgramClass referencedClass = new ProgramClass();

        // Create a lambda expression
        LambdaExpression lambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "()Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );

        // Create a method reference
        LambdaExpression methodRef = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "getValue", "()Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );

        // Assert - They should have opposite results
        assertFalse(lambda.isMethodReference(), "Lambda should not be method reference");
        assertTrue(methodRef.isMethodReference(), "Method reference should be method reference");
        assertNotEquals(lambda.isMethodReference(), methodRef.isMethodReference(),
                "Lambda and method reference should have opposite isMethodReference values");
    }
}
