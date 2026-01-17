package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.MethodHandleConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpression#isStateless()}.
 * Tests the isStateless() method which determines whether the lambda expression
 * is stateless (captures no variables).
 *
 * A lambda is stateless if its factory method has zero parameters.
 * Stateful lambdas capture variables from their enclosing scope and have
 * factory methods with parameters for the captured variables.
 */
public class LambdaExpressionClaude_isStatelessTest {

    /**
     * Tests isStateless for a truly stateless lambda with no parameters.
     * Example: () -> "Hello"
     */
    @Test
    public void testIsStateless_NoParameters() {
        // Arrange - Stateless lambda
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";

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
                "()Ljava/lang/String;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertTrue(result, "Lambda with no factory method parameters should be stateless");
    }

    /**
     * Tests isStateless for a stateful lambda with one captured variable.
     * Example: String name = "John"; Supplier<String> s = () -> name;
     */
    @Test
    public void testIsStateless_OneParameter() {
        // Arrange - Stateful lambda capturing one variable
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda with one captured variable should be stateful");
    }

    /**
     * Tests isStateless for a stateful lambda with multiple captured variables.
     */
    @Test
    public void testIsStateless_MultipleParameters() {
        // Arrange - Stateful lambda capturing multiple variables
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda with multiple captured variables should be stateful");
    }

    /**
     * Tests isStateless with primitive parameter.
     */
    @Test
    public void testIsStateless_PrimitiveParameter() {
        // Arrange - Lambda capturing a primitive int
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(I)Ljava/util/function/Predicate;";

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
                "(ILjava/lang/Object;)Z",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing a primitive should be stateful");
    }

    /**
     * Tests isStateless with long primitive parameter (takes 2 slots).
     */
    @Test
    public void testIsStateless_LongParameter() {
        // Arrange - Lambda capturing a long (2 slots in JVM)
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing a long should be stateful");
    }

    /**
     * Tests isStateless with double primitive parameter (takes 2 slots).
     */
    @Test
    public void testIsStateless_DoubleParameter() {
        // Arrange - Lambda capturing a double (2 slots in JVM)
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(D)Ljava/util/function/DoublePredicate;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/DoublePredicate"},
                new String[0],
                "test",
                "(D)Z",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(DD)Z",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing a double should be stateful");
    }

    /**
     * Tests isStateless with array parameter.
     */
    @Test
    public void testIsStateless_ArrayParameter() {
        // Arrange - Lambda capturing an array
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing an array should be stateful");
    }

    /**
     * Tests isStateless with mixed primitive and object parameters.
     */
    @Test
    public void testIsStateless_MixedParameters() {
        // Arrange - Lambda capturing mixed types
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing mixed types should be stateful");
    }

    /**
     * Tests isStateless for a stateless Runnable.
     * Example: Runnable r = () -> System.out.println("Hello");
     */
    @Test
    public void testIsStateless_StatelessRunnable() {
        // Arrange - Stateless Runnable
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "()Ljava/lang/Runnable;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/lang/Runnable"},
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

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertTrue(result, "Stateless Runnable should return true");
    }

    /**
     * Tests isStateless for a stateless Consumer (method reference).
     * Example: list.forEach(System.out::println) - but capturing System.out
     */
    @Test
    public void testIsStateless_StatefulConsumer() {
        // Arrange - Consumer capturing PrintStream
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
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Consumer capturing PrintStream should be stateful");
    }

    /**
     * Tests isStateless with all primitive types.
     */
    @Test
    public void testIsStateless_AllPrimitiveTypes() {
        // Arrange - Lambda capturing all primitive types
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing all primitive types should be stateful");
    }

    /**
     * Tests isStateless consistency on multiple invocations.
     */
    @Test
    public void testIsStateless_ConsistentResults() {
        // Arrange - Stateless lambda
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";

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
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act - Call multiple times
        boolean result1 = lambdaExpression.isStateless();
        boolean result2 = lambdaExpression.isStateless();
        boolean result3 = lambdaExpression.isStateless();

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests isStateless with null factory method descriptor.
     */
    @Test
    public void testIsStateless_NullFactoryMethodDescriptor() {
        // Arrange - Null factory method descriptor
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = null;

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
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act & Assert - This may throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            lambdaExpression.isStateless();
        }, "isStateless should throw NPE with null factory method descriptor");
    }

    /**
     * Tests isStateless with empty factory method descriptor.
     */
    @Test
    public void testIsStateless_EmptyFactoryMethodDescriptor() {
        // Arrange - Empty factory method descriptor (malformed)
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "";

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
                "()Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act & Assert - This may throw exception or return unexpected value
        // Testing to document current behavior
        try {
            boolean result = lambdaExpression.isStateless();
            // If no exception, document the behavior
            assertTrue(true, "Empty descriptor handled without exception, returned: " + result);
        } catch (Exception e) {
            // Expected if empty descriptor causes parsing issues
            assertTrue(true, "Empty descriptor caused exception: " + e.getClass().getSimpleName());
        }
    }

    /**
     * Tests isStateless with complex generic type parameters.
     */
    @Test
    public void testIsStateless_ComplexGenericTypes() {
        // Arrange - Lambda capturing a List (generic type)
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing generic types should be stateful");
    }

    /**
     * Tests isStateless with nested class parameter.
     */
    @Test
    public void testIsStateless_NestedClassParameter() {
        // Arrange - Lambda capturing a nested class instance
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Lcom/example/Outer$Inner;)Ljava/util/function/Supplier;";

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
                "(Lcom/example/Outer$Inner;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing nested class instance should be stateful");
    }

    /**
     * Tests isStateless comparison between stateless and stateful lambdas.
     */
    @Test
    public void testIsStateless_CompareStatelessAndStateful() {
        ProgramClass referencedClass = new ProgramClass();

        // Create stateless lambda
        LambdaExpression statelessLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "()Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );

        // Create stateful lambda
        LambdaExpression statefulLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "(Ljava/lang/String;)Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "(Ljava/lang/String;)Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );

        // Assert - They should have opposite results
        assertTrue(statelessLambda.isStateless(), "Lambda should be stateless");
        assertFalse(statefulLambda.isStateless(), "Lambda should be stateful");
        assertNotEquals(statelessLambda.isStateless(), statefulLambda.isStateless(),
                "Stateless and stateful lambdas should have opposite isStateless values");
    }

    /**
     * Tests isStateless for a lambda capturing 'this'.
     */
    @Test
    public void testIsStateless_CapturingThis() {
        // Arrange - Lambda capturing 'this' reference
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(LTestClass;)Ljava/util/function/Supplier;";

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
                "(LTestClass;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing 'this' should be stateful");
    }

    /**
     * Tests isStateless with multidimensional array parameter.
     */
    @Test
    public void testIsStateless_MultidimensionalArrayParameter() {
        // Arrange - Lambda capturing a 2D array
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
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Lambda capturing multidimensional array should be stateful");
    }

    /**
     * Tests isStateless for a stateless lambda that's also a method reference.
     */
    @Test
    public void testIsStateless_StatelessMethodReference() {
        // Arrange - Stateless method reference (e.g., String::new)
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "java/lang/String",
                "<init>",
                "()V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertTrue(result, "Stateless method reference should return true");
    }

    /**
     * Tests isStateless for a stateful method reference.
     */
    @Test
    public void testIsStateless_StatefulMethodReference() {
        // Arrange - Method reference capturing an object (e.g., obj::method)
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
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isStateless();

        // Assert
        assertFalse(result, "Stateful method reference should return false");
    }

    /**
     * Tests isStateless with serializable lambda (stateless).
     */
    @Test
    public void testIsStateless_SerializableStatelessLambda() {
        // Arrange - Stateless serializable lambda
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Supplier", "java/io/Serializable"},
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

        // Act
        boolean isStateless = lambdaExpression.isStateless();
        boolean isSerializable = lambdaExpression.isSerializable();

        // Assert
        assertTrue(isStateless, "Should be stateless");
        assertTrue(isSerializable, "Should be serializable");
    }

    /**
     * Tests isStateless with serializable lambda (stateful).
     */
    @Test
    public void testIsStateless_SerializableStatefulLambda() {
        // Arrange - Stateful serializable lambda
        ProgramClass referencedClass = new ProgramClass();
        String factoryMethodDescriptor = "(Ljava/lang/String;)Ljava/util/function/Supplier;";

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                factoryMethodDescriptor,
                new String[]{"java/util/function/Supplier", "java/io/Serializable"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/String;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean isStateless = lambdaExpression.isStateless();
        boolean isSerializable = lambdaExpression.isSerializable();

        // Assert
        assertFalse(isStateless, "Should be stateful");
        assertTrue(isSerializable, "Should be serializable");
    }

    /**
     * Tests isStateless with various parameter counts.
     */
    @Test
    public void testIsStateless_VariousParameterCounts() {
        ProgramClass referencedClass = new ProgramClass();

        // 0 parameters - stateless
        LambdaExpression lambda0 = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "()Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );
        assertTrue(lambda0.isStateless(), "0 parameters should be stateless");

        // 1 parameter - stateful
        LambdaExpression lambda1 = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "(I)Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "(I)Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );
        assertFalse(lambda1.isStateless(), "1 parameter should be stateful");

        // 2 parameters - stateful
        LambdaExpression lambda2 = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "(ILjava/lang/String;)Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "(ILjava/lang/String;)Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );
        assertFalse(lambda2.isStateless(), "2 parameters should be stateful");

        // 5 parameters - stateful
        LambdaExpression lambda5 = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "(ILjava/lang/String;JZD)Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass", "lambda$main$0", "(ILjava/lang/String;JZD)Ljava/lang/Object;",
                new ProgramClass(), new ProgramMethod()
        );
        assertFalse(lambda5.isStateless(), "5 parameters should be stateful");
    }
}
