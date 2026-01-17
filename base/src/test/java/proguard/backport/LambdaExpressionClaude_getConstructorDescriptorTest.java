package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.MethodHandleConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpression#getConstructorDescriptor()}.
 * Tests the getConstructorDescriptor() method which returns the constructor descriptor
 * for the lambda class based on whether the lambda is stateless or stateful.
 */
public class LambdaExpressionClaude_getConstructorDescriptorTest {

    /**
     * Tests getConstructorDescriptor for a stateless lambda (no factory method parameters).
     * A stateless lambda should return "()V" as its constructor descriptor.
     */
    @Test
    public void testGetConstructorDescriptor_StatelessLambda() {
        // Arrange - Create a lambda with no factory method parameters
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0, // bootstrapMethodIndex
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "()Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("()V", constructorDescriptor,
                "Stateless lambda should have ()V constructor descriptor");
    }

    /**
     * Tests getConstructorDescriptor for a stateful lambda with a single parameter.
     * The constructor descriptor should match the factory method parameters with void return type.
     */
    @Test
    public void testGetConstructorDescriptor_StatefulLambdaWithSingleParameter() {
        // Arrange - Create a lambda with one factory method parameter
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Ljava/lang/String;)Ljava/util/function/Supplier;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/String;)Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(Ljava/lang/String;)V", constructorDescriptor,
                "Stateful lambda with one parameter should have matching constructor descriptor with void return");
    }

    /**
     * Tests getConstructorDescriptor for a stateful lambda with multiple parameters.
     */
    @Test
    public void testGetConstructorDescriptor_StatefulLambdaWithMultipleParameters() {
        // Arrange - Create a lambda with multiple factory method parameters
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Ljava/lang/String;I)Ljava/util/function/Consumer;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Consumer"},
                new String[0],
                "accept",
                "(Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/String;ILjava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(Ljava/lang/String;I)V", constructorDescriptor,
                "Stateful lambda with multiple parameters should have matching constructor descriptor");
    }

    /**
     * Tests getConstructorDescriptor with primitive parameters.
     */
    @Test
    public void testGetConstructorDescriptor_WithPrimitiveParameters() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(IZD)Ljava/util/function/Predicate;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Predicate"},
                new String[0],
                "test",
                "(Ljava/lang/Object;)Z",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(IZDLjava/lang/Object;)Z",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(IZD)V", constructorDescriptor,
                "Constructor descriptor should preserve primitive parameter types");
    }

    /**
     * Tests getConstructorDescriptor with array parameters.
     */
    @Test
    public void testGetConstructorDescriptor_WithArrayParameters() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "([Ljava/lang/String;)Ljava/util/function/Function;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Function"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "([Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("([Ljava/lang/String;)V", constructorDescriptor,
                "Constructor descriptor should handle array parameters correctly");
    }

    /**
     * Tests getConstructorDescriptor with mixed object and primitive parameters.
     */
    @Test
    public void testGetConstructorDescriptor_WithMixedParameters() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Ljava/lang/String;ILjava/lang/Object;J)Ljava/util/function/BiFunction;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/BiFunction"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/String;ILjava/lang/Object;JLjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(Ljava/lang/String;ILjava/lang/Object;J)V", constructorDescriptor,
                "Constructor descriptor should handle mixed object and primitive parameters");
    }

    /**
     * Tests that getConstructorDescriptor for a stateless lambda always returns the same value.
     */
    @Test
    public void testGetConstructorDescriptor_StatelessLambda_ConsistentResult() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "()Ljava/util/function/Runnable;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Runnable"},
                new String[0],
                "run",
                "()V",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "()V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act - Call multiple times
        String result1 = lambdaExpression.getConstructorDescriptor();
        String result2 = lambdaExpression.getConstructorDescriptor();
        String result3 = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals(result1, result2, "Multiple calls should return consistent results");
        assertEquals(result2, result3, "Multiple calls should return consistent results");
        assertEquals("()V", result1, "Result should be ()V for stateless lambda");
    }

    /**
     * Tests getConstructorDescriptor with complex nested generic types.
     */
    @Test
    public void testGetConstructorDescriptor_WithComplexGenericTypes() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Ljava/util/List;)Ljava/util/function/Function;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Function"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/util/List;Ljava/lang/Object;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(Ljava/util/List;)V", constructorDescriptor,
                "Constructor descriptor should handle complex generic types");
    }

    /**
     * Tests getConstructorDescriptor with a long parameter.
     */
    @Test
    public void testGetConstructorDescriptor_WithLongParameter() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(J)Ljava/util/function/LongPredicate;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/LongPredicate"},
                new String[0],
                "test",
                "(J)Z",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(JJ)Z",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(J)V", constructorDescriptor,
                "Constructor descriptor should handle long parameter correctly");
    }

    /**
     * Tests getConstructorDescriptor with all primitive types.
     */
    @Test
    public void testGetConstructorDescriptor_WithAllPrimitiveTypes() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(BCIJFDZ)Ljava/util/function/Consumer;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Consumer"},
                new String[0],
                "accept",
                "(Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(BCIJFDZLjava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(BCIJFDZ)V", constructorDescriptor,
                "Constructor descriptor should handle all primitive types");
    }

    /**
     * Tests getConstructorDescriptor with multidimensional arrays.
     */
    @Test
    public void testGetConstructorDescriptor_WithMultidimensionalArrays() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "([[I)Ljava/util/function/Function;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Function"},
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "([[ILjava/lang/Object;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("([[I)V", constructorDescriptor,
                "Constructor descriptor should handle multidimensional arrays");
    }

    /**
     * Tests getConstructorDescriptor with a Consumer that captures one variable.
     */
    @Test
    public void testGetConstructorDescriptor_ConsumerWithCapturedVariable() {
        // Arrange - Consumer capturing a single variable
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Ljava/io/PrintStream;)Ljava/util/function/Consumer;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Consumer"},
                new String[0],
                "accept",
                "(Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/io/PrintStream;Ljava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(Ljava/io/PrintStream;)V", constructorDescriptor,
                "Constructor descriptor should match captured variables");
    }

    /**
     * Tests getConstructorDescriptor with multiple captured variables of different types.
     */
    @Test
    public void testGetConstructorDescriptor_MultipleCapturedVariables() {
        // Arrange - Lambda capturing multiple variables
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Ljava/lang/String;ILjava/util/List;)Ljava/util/function/Predicate;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Predicate"},
                new String[0],
                "test",
                "(Ljava/lang/Object;)Z",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/String;ILjava/util/List;Ljava/lang/Object;)Z",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        String constructorDescriptor = lambdaExpression.getConstructorDescriptor();

        // Assert
        assertEquals("(Ljava/lang/String;ILjava/util/List;)V", constructorDescriptor,
                "Constructor descriptor should match all captured variables");
    }

    /**
     * Tests that isStateless() and getConstructorDescriptor() are consistent.
     * When isStateless() returns true, getConstructorDescriptor() should return "()V".
     */
    @Test
    public void testGetConstructorDescriptor_ConsistentWithIsStateless() {
        // Arrange - Create both stateless and stateful lambdas
        ProgramClass referencedClass = new ProgramClass();

        LambdaExpression statelessLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "()Ljava/lang/String;",
                new ProgramClass(), new ProgramMethod()
        );

        LambdaExpression statefulLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "(Ljava/lang/String;)Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "(Ljava/lang/String;)Ljava/lang/String;",
                new ProgramClass(), new ProgramMethod()
        );

        // Act & Assert - Stateless
        assertTrue(statelessLambda.isStateless(), "Lambda should be stateless");
        assertEquals("()V", statelessLambda.getConstructorDescriptor(),
                "Stateless lambda should have ()V constructor");

        // Act & Assert - Stateful
        assertFalse(statefulLambda.isStateless(), "Lambda should be stateful");
        assertNotEquals("()V", statefulLambda.getConstructorDescriptor(),
                "Stateful lambda should not have ()V constructor");
        assertTrue(statefulLambda.getConstructorDescriptor().contains("Ljava/lang/String;"),
                "Stateful lambda constructor should include captured parameters");
    }
}
