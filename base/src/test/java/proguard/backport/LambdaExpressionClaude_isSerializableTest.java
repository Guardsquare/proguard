package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.MethodHandleConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpression#isSerializable()}.
 * Tests the isSerializable() method which returns whether the lambda expression
 * implements the Serializable interface.
 */
public class LambdaExpressionClaude_isSerializableTest {

    /**
     * Tests isSerializable when the lambda implements only Serializable interface.
     */
    @Test
    public void testIsSerializable_WithOnlySerializableInterface() {
        // Arrange - Lambda with only Serializable interface
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{"java/io/Serializable"};

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/io/Serializable;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Lambda implementing Serializable should return true");
    }

    /**
     * Tests isSerializable when the lambda does not implement Serializable.
     */
    @Test
    public void testIsSerializable_WithoutSerializableInterface() {
        // Arrange - Lambda with non-Serializable interface
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{"java/util/function/Supplier"};

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "Lambda not implementing Serializable should return false");
    }

    /**
     * Tests isSerializable when the lambda implements Serializable among other interfaces.
     */
    @Test
    public void testIsSerializable_WithSerializableAmongMultipleInterfaces() {
        // Arrange - Lambda implementing multiple interfaces including Serializable
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/util/function/Supplier",
                "java/io/Serializable"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Lambda implementing Serializable among other interfaces should return true");
    }

    /**
     * Tests isSerializable when Serializable is the first interface.
     */
    @Test
    public void testIsSerializable_WithSerializableAsFirstInterface() {
        // Arrange - Serializable as first interface
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/io/Serializable",
                "java/util/function/Consumer"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Consumer;",
                interfaces,
                new String[0],
                "accept",
                "(Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/Object;)V",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Lambda with Serializable as first interface should return true");
    }

    /**
     * Tests isSerializable when Serializable is in the middle of interface list.
     */
    @Test
    public void testIsSerializable_WithSerializableInMiddle() {
        // Arrange - Serializable in the middle
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/util/function/Function",
                "java/io/Serializable",
                "java/util/function/Predicate"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Function;",
                interfaces,
                new String[0],
                "apply",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Lambda with Serializable in middle should return true");
    }

    /**
     * Tests isSerializable when Serializable is the last interface.
     */
    @Test
    public void testIsSerializable_WithSerializableAsLastInterface() {
        // Arrange - Serializable as last interface
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/util/function/BiFunction",
                "java/util/function/BiConsumer",
                "java/io/Serializable"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/BiFunction;",
                interfaces,
                new String[0],
                "apply",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Lambda with Serializable as last interface should return true");
    }

    /**
     * Tests isSerializable with an empty interfaces array.
     */
    @Test
    public void testIsSerializable_WithEmptyInterfacesArray() {
        // Arrange - Empty interfaces array
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[0];

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/lang/Object;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "Lambda with empty interfaces array should return false");
    }

    /**
     * Tests isSerializable with null interfaces array.
     * This tests the robustness of the method.
     */
    @Test
    public void testIsSerializable_WithNullInterfacesArray() {
        // Arrange - Null interfaces array
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = null;

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/lang/Object;",
                interfaces,
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
        // Testing to document current behavior
        assertThrows(NullPointerException.class, () -> {
            lambdaExpression.isSerializable();
        }, "isSerializable should throw NPE with null interfaces array");
    }

    /**
     * Tests isSerializable with multiple interfaces, none being Serializable.
     */
    @Test
    public void testIsSerializable_WithMultipleNonSerializableInterfaces() {
        // Arrange - Multiple non-Serializable interfaces
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/util/function/Supplier",
                "java/util/function/Consumer",
                "java/util/function/Function",
                "java/util/function/Predicate"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "Lambda with multiple non-Serializable interfaces should return false");
    }

    /**
     * Tests isSerializable with case-sensitive matching.
     * Verifies that only exact match with "java/io/Serializable" returns true.
     */
    @Test
    public void testIsSerializable_CaseSensitiveMatching() {
        // Arrange - Interface with wrong case
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{"java/io/serializable"}; // lowercase 's'

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/lang/Object;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "isSerializable should be case-sensitive");
    }

    /**
     * Tests isSerializable with similar but different interface name.
     */
    @Test
    public void testIsSerializable_WithSimilarButDifferentInterfaceName() {
        // Arrange - Similar interface name
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{"java/io/Serializables"}; // Extra 's' at end

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/lang/Object;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "Interface name must match exactly");
    }

    /**
     * Tests isSerializable returns consistent results on multiple invocations.
     */
    @Test
    public void testIsSerializable_ConsistentResults() {
        // Arrange - Serializable lambda
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/util/function/Supplier",
                "java/io/Serializable"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                interfaces,
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
        boolean result1 = lambdaExpression.isSerializable();
        boolean result2 = lambdaExpression.isSerializable();
        boolean result3 = lambdaExpression.isSerializable();

        // Assert - All should be true and consistent
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests isSerializable with Runnable (common functional interface).
     */
    @Test
    public void testIsSerializable_WithRunnableInterface() {
        // Arrange - Runnable without Serializable
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{"java/lang/Runnable"};

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/lang/Runnable;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "Runnable alone is not Serializable");
    }

    /**
     * Tests isSerializable with Runnable and Serializable (intersection type).
     */
    @Test
    public void testIsSerializable_WithRunnableAndSerializable() {
        // Arrange - Runnable & Serializable (intersection type)
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/lang/Runnable",
                "java/io/Serializable"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/lang/Runnable;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Runnable & Serializable intersection type should be serializable");
    }

    /**
     * Tests isSerializable with Comparator and Serializable.
     */
    @Test
    public void testIsSerializable_WithComparatorAndSerializable() {
        // Arrange - Comparator & Serializable
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/util/Comparator",
                "java/io/Serializable"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/Comparator;",
                interfaces,
                new String[0],
                "compare",
                "(Ljava/lang/Object;Ljava/lang/Object;)I",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass",
                "lambda$main$0",
                "(Ljava/lang/Object;Ljava/lang/Object;)I",
                new ProgramClass(),
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Comparator & Serializable should be serializable");
    }

    /**
     * Tests isSerializable with an interface array containing null elements.
     */
    @Test
    public void testIsSerializable_WithNullElementInInterfacesArray() {
        // Arrange - Interface array with null element
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/util/function/Supplier",
                null,
                "java/io/Serializable"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Should find Serializable even with null elements in array");
    }

    /**
     * Tests isSerializable with duplicate Serializable entries.
     */
    @Test
    public void testIsSerializable_WithDuplicateSerializableEntries() {
        // Arrange - Multiple Serializable entries (edge case)
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{
                "java/io/Serializable",
                "java/util/function/Supplier",
                "java/io/Serializable"
        };

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertTrue(result, "Should return true with duplicate Serializable entries");
    }

    /**
     * Tests isSerializable with custom Serializable-like interface.
     */
    @Test
    public void testIsSerializable_WithCustomSerializableInterface() {
        // Arrange - Custom interface that's not java.io.Serializable
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{"com/example/CustomSerializable"};

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Lcom/example/CustomSerializable;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "Custom Serializable interface should not be recognized");
    }

    /**
     * Tests isSerializable with Externalizable interface.
     * Note: Externalizable extends Serializable, but the lambda might only declare Externalizable.
     */
    @Test
    public void testIsSerializable_WithExternalizableInterface() {
        // Arrange - Externalizable (which extends Serializable in Java)
        // But in bytecode, we only see the declared interface
        ProgramClass referencedClass = new ProgramClass();
        String[] interfaces = new String[]{"java/io/Externalizable"};

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/io/Externalizable;",
                interfaces,
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
        boolean result = lambdaExpression.isSerializable();

        // Assert
        assertFalse(result, "Externalizable alone should not match (only exact Serializable match)");
    }
}
