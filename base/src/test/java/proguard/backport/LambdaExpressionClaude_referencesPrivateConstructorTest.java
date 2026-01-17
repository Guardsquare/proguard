package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.MethodHandleConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpression#referencesPrivateConstructor()}.
 * Tests the referencesPrivateConstructor() method which determines whether the lambda
 * expression is a method reference to a private constructor.
 *
 * The method returns true if ALL of the following conditions are met:
 * 1. The invoked reference kind is REF_NEW_INVOKE_SPECIAL
 * 2. The invoked method name equals "<init>" (constructor)
 * 3. The referenced invoked method has the PRIVATE access flag
 */
public class LambdaExpressionClaude_referencesPrivateConstructorTest {

    /**
     * Tests referencesPrivateConstructor when referencing a private constructor.
     * This is the typical case for method references to private constructors.
     */
    @Test
    public void testReferencesPrivateConstructor_PrivateConstructor() {
        // Arrange - Method reference to a private constructor
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertTrue(result, "Should return true for private constructor reference");
    }

    /**
     * Tests referencesPrivateConstructor when referencing a public constructor.
     * Public constructors should return false.
     */
    @Test
    public void testReferencesPrivateConstructor_PublicConstructor() {
        // Arrange - Method reference to a public constructor
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod publicConstructor = new ProgramMethod();
        publicConstructor.u2accessFlags = AccessConstants.PUBLIC;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                publicConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for public constructor reference");
    }

    /**
     * Tests referencesPrivateConstructor when referencing a protected constructor.
     * Protected constructors should return false.
     */
    @Test
    public void testReferencesPrivateConstructor_ProtectedConstructor() {
        // Arrange - Method reference to a protected constructor
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod protectedConstructor = new ProgramMethod();
        protectedConstructor.u2accessFlags = AccessConstants.PROTECTED;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                protectedConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for protected constructor reference");
    }

    /**
     * Tests referencesPrivateConstructor when referencing a package-private constructor.
     * Package-private constructors (no access flag) should return false.
     */
    @Test
    public void testReferencesPrivateConstructor_PackagePrivateConstructor() {
        // Arrange - Method reference to a package-private constructor
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod packagePrivateConstructor = new ProgramMethod();
        packagePrivateConstructor.u2accessFlags = 0; // No access flags

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                packagePrivateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for package-private constructor reference");
    }

    /**
     * Tests referencesPrivateConstructor with a private regular method (not constructor).
     * Should return false because the method name is not "<init>".
     */
    @Test
    public void testReferencesPrivateConstructor_PrivateRegularMethod() {
        // Arrange - Private method (not constructor)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateMethod = new ProgramMethod();
        privateMethod.u2accessFlags = AccessConstants.PRIVATE;

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
                "MyClass",
                "privateMethod",
                "()Ljava/lang/Object;",
                invokedClass,
                privateMethod
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for private regular method");
    }

    /**
     * Tests referencesPrivateConstructor with wrong reference kind (REF_INVOKE_STATIC).
     * Should return false because it's not REF_NEW_INVOKE_SPECIAL.
     */
    @Test
    public void testReferencesPrivateConstructor_WrongReferenceKind_Static() {
        // Arrange - Static invocation of constructor (invalid but testing)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

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
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for REF_INVOKE_STATIC");
    }

    /**
     * Tests referencesPrivateConstructor with wrong reference kind (REF_INVOKE_VIRTUAL).
     * Should return false because it's not REF_NEW_INVOKE_SPECIAL.
     */
    @Test
    public void testReferencesPrivateConstructor_WrongReferenceKind_Virtual() {
        // Arrange - Virtual invocation of constructor (invalid but testing)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for REF_INVOKE_VIRTUAL");
    }

    /**
     * Tests referencesPrivateConstructor with wrong reference kind (REF_INVOKE_SPECIAL).
     * Should return false because it's not REF_NEW_INVOKE_SPECIAL.
     */
    @Test
    public void testReferencesPrivateConstructor_WrongReferenceKind_Special() {
        // Arrange - Special invocation (not new)
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for REF_INVOKE_SPECIAL");
    }

    /**
     * Tests referencesPrivateConstructor with wrong reference kind (REF_INVOKE_INTERFACE).
     * Should return false because it's not REF_NEW_INVOKE_SPECIAL.
     */
    @Test
    public void testReferencesPrivateConstructor_WrongReferenceKind_Interface() {
        // Arrange - Interface invocation
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertFalse(result, "Should return false for REF_INVOKE_INTERFACE");
    }

    /**
     * Tests referencesPrivateConstructor with private constructor with additional flags.
     * Should return true when PRIVATE flag is present along with other flags.
     */
    @Test
    public void testReferencesPrivateConstructor_PrivateWithSynthetic() {
        // Arrange - Private synthetic constructor
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateSyntheticConstructor = new ProgramMethod();
        privateSyntheticConstructor.u2accessFlags = AccessConstants.PRIVATE | AccessConstants.SYNTHETIC;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateSyntheticConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertTrue(result, "Should return true for private synthetic constructor");
    }

    /**
     * Tests referencesPrivateConstructor with constructor that has parameters.
     * Should return true as long as it's private with REF_NEW_INVOKE_SPECIAL.
     */
    @Test
    public void testReferencesPrivateConstructor_PrivateConstructorWithParameters() {
        // Arrange - Private constructor with parameters
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                0,
                new BootstrapMethodInfo(),
                "(Ljava/lang/String;I)Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"},
                new String[0],
                "get",
                "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "(Ljava/lang/String;I)V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertTrue(result, "Should return true for private constructor with parameters");
    }

    /**
     * Tests referencesPrivateConstructor consistency on multiple invocations.
     * Should return the same result when called multiple times.
     */
    @Test
    public void testReferencesPrivateConstructor_ConsistentResults() {
        // Arrange - Private constructor
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateConstructor
        );

        // Act - Call multiple times
        boolean result1 = lambdaExpression.referencesPrivateConstructor();
        boolean result2 = lambdaExpression.referencesPrivateConstructor();
        boolean result3 = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests referencesPrivateConstructor with all reference kinds.
     * Only REF_NEW_INVOKE_SPECIAL should return true.
     */
    @Test
    public void testReferencesPrivateConstructor_AllReferenceKinds() {
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

        // Test REF_NEW_INVOKE_SPECIAL - should be true
        LambdaExpression newSpecialLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertTrue(newSpecialLambda.referencesPrivateConstructor(), "REF_NEW_INVOKE_SPECIAL should be true");

        // Test REF_INVOKE_STATIC - should be false
        LambdaExpression staticLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertFalse(staticLambda.referencesPrivateConstructor(), "REF_INVOKE_STATIC should be false");

        // Test REF_INVOKE_VIRTUAL - should be false
        LambdaExpression virtualLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertFalse(virtualLambda.referencesPrivateConstructor(), "REF_INVOKE_VIRTUAL should be false");

        // Test REF_INVOKE_SPECIAL - should be false
        LambdaExpression specialLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertFalse(specialLambda.referencesPrivateConstructor(), "REF_INVOKE_SPECIAL should be false");

        // Test REF_INVOKE_INTERFACE - should be false
        LambdaExpression interfaceLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_INTERFACE,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertFalse(interfaceLambda.referencesPrivateConstructor(), "REF_INVOKE_INTERFACE should be false");
    }

    /**
     * Tests referencesPrivateConstructor with all access modifiers.
     * Only PRIVATE should return true.
     */
    @Test
    public void testReferencesPrivateConstructor_AllAccessModifiers() {
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();

        // Test PRIVATE - should be true
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;
        LambdaExpression privateLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertTrue(privateLambda.referencesPrivateConstructor(), "PRIVATE should be true");

        // Test PUBLIC - should be false
        ProgramMethod publicConstructor = new ProgramMethod();
        publicConstructor.u2accessFlags = AccessConstants.PUBLIC;
        LambdaExpression publicLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, publicConstructor
        );
        assertFalse(publicLambda.referencesPrivateConstructor(), "PUBLIC should be false");

        // Test PROTECTED - should be false
        ProgramMethod protectedConstructor = new ProgramMethod();
        protectedConstructor.u2accessFlags = AccessConstants.PROTECTED;
        LambdaExpression protectedLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, protectedConstructor
        );
        assertFalse(protectedLambda.referencesPrivateConstructor(), "PROTECTED should be false");

        // Test package-private (no access flag) - should be false
        ProgramMethod packagePrivateConstructor = new ProgramMethod();
        packagePrivateConstructor.u2accessFlags = 0;
        LambdaExpression packagePrivateLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, packagePrivateConstructor
        );
        assertFalse(packagePrivateLambda.referencesPrivateConstructor(), "Package-private should be false");
    }

    /**
     * Tests that all three conditions must be met for true result.
     */
    @Test
    public void testReferencesPrivateConstructor_AllConditionsMustBeMet() {
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;
        ProgramMethod publicConstructor = new ProgramMethod();
        publicConstructor.u2accessFlags = AccessConstants.PUBLIC;

        // Condition 1: REF_NEW_INVOKE_SPECIAL = YES, Method = <init> = YES, PRIVATE = YES → TRUE
        LambdaExpression allTrue = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertTrue(allTrue.referencesPrivateConstructor(), "All conditions met should be true");

        // Condition 2: REF_NEW_INVOKE_SPECIAL = NO, Method = <init> = YES, PRIVATE = YES → FALSE
        LambdaExpression noNewSpecial = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );
        assertFalse(noNewSpecial.referencesPrivateConstructor(), "Missing REF_NEW_INVOKE_SPECIAL should be false");

        // Condition 3: REF_NEW_INVOKE_SPECIAL = YES, Method = <init> = NO, PRIVATE = YES → FALSE
        LambdaExpression noInit = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", "someMethod", "()V",
                invokedClass, privateConstructor
        );
        assertFalse(noInit.referencesPrivateConstructor(), "Missing <init> should be false");

        // Condition 4: REF_NEW_INVOKE_SPECIAL = YES, Method = <init> = YES, PRIVATE = NO → FALSE
        LambdaExpression noPrivate = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, publicConstructor
        );
        assertFalse(noPrivate.referencesPrivateConstructor(), "Missing PRIVATE flag should be false");
    }

    /**
     * Tests referencesPrivateConstructor comparing private vs public constructor.
     */
    @Test
    public void testReferencesPrivateConstructor_ComparePrivateAndPublic() {
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();

        // Create private constructor case
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;
        LambdaExpression privateLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, privateConstructor
        );

        // Create public constructor case
        ProgramMethod publicConstructor = new ProgramMethod();
        publicConstructor.u2accessFlags = AccessConstants.PUBLIC;
        LambdaExpression publicLambda = new LambdaExpression(
                referencedClass, 0, new BootstrapMethodInfo(),
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL,
                "MyClass", ClassConstants.METHOD_NAME_INIT, "()V",
                invokedClass, publicConstructor
        );

        // Assert - They should have opposite results
        assertTrue(privateLambda.referencesPrivateConstructor(), "Private constructor should return true");
        assertFalse(publicLambda.referencesPrivateConstructor(), "Public constructor should return false");
        assertNotEquals(privateLambda.referencesPrivateConstructor(),
                publicLambda.referencesPrivateConstructor(),
                "Private and public constructors should have opposite results");
    }

    /**
     * Tests referencesPrivateConstructor with private constructor in nested class.
     */
    @Test
    public void testReferencesPrivateConstructor_NestedClass() {
        // Arrange - Private constructor in nested class
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE;

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
                "Outer$Inner",
                ClassConstants.METHOD_NAME_INIT,
                "(LOuter;)V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertTrue(result, "Should return true for private constructor in nested class");
    }

    /**
     * Tests referencesPrivateConstructor with private constructor with varargs.
     */
    @Test
    public void testReferencesPrivateConstructor_PrivateConstructorVarargs() {
        // Arrange - Private varargs constructor
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateConstructor = new ProgramMethod();
        privateConstructor.u2accessFlags = AccessConstants.PRIVATE | AccessConstants.VARARGS;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "([Ljava/lang/String;)V",
                invokedClass,
                privateConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertTrue(result, "Should return true for private varargs constructor");
    }

    /**
     * Tests referencesPrivateConstructor with final flag combined with private.
     * FINAL doesn't make sense on constructors but testing flag combination.
     */
    @Test
    public void testReferencesPrivateConstructor_PrivateFinalConstructor() {
        // Arrange - Constructor with both PRIVATE and FINAL flags
        ProgramClass referencedClass = new ProgramClass();
        ProgramClass invokedClass = new ProgramClass();
        ProgramMethod privateFinalConstructor = new ProgramMethod();
        privateFinalConstructor.u2accessFlags = AccessConstants.PRIVATE | AccessConstants.FINAL;

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
                "MyClass",
                ClassConstants.METHOD_NAME_INIT,
                "()V",
                invokedClass,
                privateFinalConstructor
        );

        // Act
        boolean result = lambdaExpression.referencesPrivateConstructor();

        // Assert
        assertTrue(result, "Should return true for constructor with PRIVATE and FINAL flags");
    }
}
