package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.MethodHandleConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpression#invokesStaticInterfaceMethod()}.
 * Tests the invokesStaticInterfaceMethod() method which determines whether the lambda
 * invokes a static interface method.
 *
 * The method returns true if ALL of the following conditions are met:
 * 1. The invoked reference kind is REF_INVOKE_STATIC
 * 2. The referenced invoked class is not null
 * 3. The referenced invoked class has the INTERFACE access flag
 */
public class LambdaExpressionClaude_invokesStaticInterfaceMethodTest {

    /**
     * Tests invokesStaticInterfaceMethod when invoking a static interface method.
     * This is the typical case for static interface methods (Java 8+).
     */
    @Test
    public void testInvokesStaticInterfaceMethod_StaticInterfaceMethod() {
        // Arrange - Static method in an interface
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

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
                "MyInterface",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertTrue(result, "Should return true for static interface method");
    }

    /**
     * Tests invokesStaticInterfaceMethod when invoking a static class method.
     * Static methods in classes should return false.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_StaticClassMethod() {
        // Arrange - Static method in a class (not interface)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.PUBLIC; // No INTERFACE flag

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
                "MyClass",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false for static class method");
    }

    /**
     * Tests invokesStaticInterfaceMethod when invoking a virtual interface method.
     * Non-static interface methods should return false.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_VirtualInterfaceMethod() {
        // Arrange - Virtual method in an interface (default or abstract)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "MyInterface",
                "virtualMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false for virtual interface method");
    }

    /**
     * Tests invokesStaticInterfaceMethod when invoking an interface method.
     * Using REF_INVOKE_INTERFACE should return false.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_InvokeInterface() {
        // Arrange - Interface method invocation
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_INTERFACE,
                "MyInterface",
                "interfaceMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false for REF_INVOKE_INTERFACE");
    }

    /**
     * Tests invokesStaticInterfaceMethod when invoking a special method in interface.
     * Using REF_INVOKE_SPECIAL should return false.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_InvokeSpecial() {
        // Arrange - Special method invocation in interface
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_SPECIAL,
                "MyInterface",
                "specialMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false for REF_INVOKE_SPECIAL");
    }

    /**
     * Tests invokesStaticInterfaceMethod with null referenced invoked class.
     * Should return false when the class is null.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_NullReferencedClass() {
        // Arrange - Null referenced invoked class
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
                "MyInterface",
                "staticMethod",
                "()Ljava/lang/String;",
                null, // Null referenced class
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false when referenced class is null");
    }

    /**
     * Tests invokesStaticInterfaceMethod with interface having abstract flag.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_AbstractInterface() {
        // Arrange - Abstract interface with static method
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.ABSTRACT | AccessConstants.PUBLIC;

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
                "MyInterface",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertTrue(result, "Should return true for static method in abstract interface");
    }

    /**
     * Tests invokesStaticInterfaceMethod with interface having only interface flag.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_InterfaceFlagOnly() {
        // Arrange - Interface with only INTERFACE flag
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE;

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
                "MyInterface",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertTrue(result, "Should return true with just INTERFACE flag");
    }

    /**
     * Tests invokesStaticInterfaceMethod with abstract class.
     * Abstract classes without INTERFACE flag should return false.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_AbstractClass() {
        // Arrange - Abstract class (not interface) with static method
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.ABSTRACT | AccessConstants.PUBLIC; // No INTERFACE flag

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
                "MyAbstractClass",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false for abstract class without INTERFACE flag");
    }

    /**
     * Tests invokesStaticInterfaceMethod with final class.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_FinalClass() {
        // Arrange - Final class with static method
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.FINAL | AccessConstants.PUBLIC;

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
                "MyFinalClass",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false for final class");
    }

    /**
     * Tests invokesStaticInterfaceMethod with class having zero access flags.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_NoAccessFlags() {
        // Arrange - Class with no access flags
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = 0;

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
                "MyClass",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false with zero access flags");
    }

    /**
     * Tests invokesStaticInterfaceMethod consistency on multiple invocations.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_ConsistentResults() {
        // Arrange - Static interface method
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

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
                "MyInterface",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act - Call multiple times
        boolean result1 = lambdaExpression.invokesStaticInterfaceMethod();
        boolean result2 = lambdaExpression.invokesStaticInterfaceMethod();
        boolean result3 = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests invokesStaticInterfaceMethod with REF_NEW_INVOKE_SPECIAL (constructor).
     */
    @Test
    public void testInvokesStaticInterfaceMethod_Constructor() {
        // Arrange - Constructor in interface (edge case)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

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
                "MyInterface",
                "<init>",
                "()V",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertFalse(result, "Should return false for constructor reference");
    }

    /**
     * Tests invokesStaticInterfaceMethod with all reference kinds.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_AllReferenceKinds() {
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

        // Test REF_INVOKE_STATIC - should be true
        LambdaExpression staticLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "MyInterface", "method", "()Ljava/lang/Object;",
                invokedClass, new ProgramMethod()
        );
        assertTrue(staticLambda.invokesStaticInterfaceMethod(), "REF_INVOKE_STATIC should be true");

        // Test REF_INVOKE_VIRTUAL - should be false
        LambdaExpression virtualLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "MyInterface", "method", "()Ljava/lang/Object;",
                invokedClass, new ProgramMethod()
        );
        assertFalse(virtualLambda.invokesStaticInterfaceMethod(), "REF_INVOKE_VIRTUAL should be false");

        // Test REF_INVOKE_SPECIAL - should be false
        LambdaExpression specialLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_SPECIAL,
                "MyInterface", "method", "()Ljava/lang/Object;",
                invokedClass, new ProgramMethod()
        );
        assertFalse(specialLambda.invokesStaticInterfaceMethod(), "REF_INVOKE_SPECIAL should be false");

        // Test REF_INVOKE_INTERFACE - should be false
        LambdaExpression interfaceLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_INTERFACE,
                "MyInterface", "method", "()Ljava/lang/Object;",
                invokedClass, new ProgramMethod()
        );
        assertFalse(interfaceLambda.invokesStaticInterfaceMethod(), "REF_INVOKE_INTERFACE should be false");

        // Test REF_NEW_INVOKE_SPECIAL - should be false
        LambdaExpression newLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyInterface", "<init>", "()V",
                invokedClass, new ProgramMethod()
        );
        assertFalse(newLambda.invokesStaticInterfaceMethod(), "REF_NEW_INVOKE_SPECIAL should be false");
    }

    /**
     * Tests invokesStaticInterfaceMethod with annotation interface.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_AnnotationInterface() {
        // Arrange - Annotation interface with static method (Java 8+)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.ANNOTATION | AccessConstants.ABSTRACT;

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
                "MyAnnotation",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertTrue(result, "Should return true for static method in annotation interface");
    }

    /**
     * Tests invokesStaticInterfaceMethod comparing interface vs class.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_CompareInterfaceAndClass() {
        ProgramClass referencedClass = new ProgramClass();

        // Create interface case
        ProgramClass interfaceClass = new ProgramClass();
        interfaceClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

        LambdaExpression interfaceLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "MyInterface", "staticMethod", "()Ljava/lang/Object;",
                interfaceClass, new ProgramMethod()
        );

        // Create class case
        ProgramClass regularClass = new ProgramClass();
        regularClass.u2accessFlags = AccessConstants.PUBLIC;

        LambdaExpression classLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "MyClass", "staticMethod", "()Ljava/lang/Object;",
                regularClass, new ProgramMethod()
        );

        // Assert - They should have opposite results
        assertTrue(interfaceLambda.invokesStaticInterfaceMethod(), "Interface should return true");
        assertFalse(classLambda.invokesStaticInterfaceMethod(), "Class should return false");
        assertNotEquals(interfaceLambda.invokesStaticInterfaceMethod(),
                classLambda.invokesStaticInterfaceMethod(),
                "Interface and class should have opposite results");
    }

    /**
     * Tests invokesStaticInterfaceMethod with synthetic interface.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_SyntheticInterface() {
        // Arrange - Synthetic interface with static method
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.SYNTHETIC;

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
                "MyInterface",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertTrue(result, "Should return true for static method in synthetic interface");
    }

    /**
     * Tests invokesStaticInterfaceMethod with private nested interface.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_PrivateNestedInterface() {
        // Arrange - Private nested interface with static method
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        invokedClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PRIVATE;

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
                "Outer$Inner",
                "staticMethod",
                "()Ljava/lang/String;",
                invokedClass,
                new ProgramMethod()
        );

        // Act
        boolean result = lambdaExpression.invokesStaticInterfaceMethod();

        // Assert
        assertTrue(result, "Should return true for static method in private nested interface");
    }

    /**
     * Tests that all three conditions must be met for true result.
     */
    @Test
    public void testInvokesStaticInterfaceMethod_AllConditionsMustBeMet() {
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass interfaceClass = new ProgramClass();
        interfaceClass.u2accessFlags = AccessConstants.INTERFACE | AccessConstants.PUBLIC;

        // Condition 1: REF_INVOKE_STATIC = YES, Class != null = YES, INTERFACE = YES → TRUE
        LambdaExpression allTrue = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "MyInterface", "method", "()Ljava/lang/Object;",
                interfaceClass, new ProgramMethod()
        );
        assertTrue(allTrue.invokesStaticInterfaceMethod(), "All conditions met should be true");

        // Condition 2: REF_INVOKE_STATIC = NO, Class != null = YES, INTERFACE = YES → FALSE
        LambdaExpression noStatic = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "MyInterface", "method", "()Ljava/lang/Object;",
                interfaceClass, new ProgramMethod()
        );
        assertFalse(noStatic.invokesStaticInterfaceMethod(), "Missing static kind should be false");

        // Condition 3: REF_INVOKE_STATIC = YES, Class = null, INTERFACE = N/A → FALSE
        LambdaExpression nullClass = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "MyInterface", "method", "()Ljava/lang/Object;",
                null, new ProgramMethod()
        );
        assertFalse(nullClass.invokesStaticInterfaceMethod(), "Null class should be false");

        // Condition 4: REF_INVOKE_STATIC = YES, Class != null = YES, INTERFACE = NO → FALSE
        ProgramClass regularClass = new ProgramClass();
        regularClass.u2accessFlags = AccessConstants.PUBLIC;
        LambdaExpression noInterface = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "MyClass", "method", "()Ljava/lang/Object;",
                regularClass, new ProgramMethod()
        );
        assertFalse(noInterface.invokesStaticInterfaceMethod(), "Missing interface flag should be false");
    }
}
