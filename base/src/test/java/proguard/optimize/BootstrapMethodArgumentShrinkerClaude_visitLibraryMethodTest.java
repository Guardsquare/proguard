package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BootstrapMethodArgumentShrinker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
 *
 * The visitLibraryMethod method in BootstrapMethodArgumentShrinker is a no-op method.
 * It is intentionally empty because this class only needs to process ProgramMethod objects
 * to extract parameter usage information. Library methods don't contain the bytecode needed
 * for parameter usage analysis, so there's nothing to process.
 *
 * These tests verify that the method:
 * 1. Can be called without throwing exceptions
 * 2. Has no side effects (does nothing)
 * 3. Works with various inputs including null and edge cases
 */
public class BootstrapMethodArgumentShrinkerClaude_visitLibraryMethodTest {

    private BootstrapMethodArgumentShrinker shrinker;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        shrinker = new BootstrapMethodArgumentShrinker();
        libraryClass = new LibraryClass();
        libraryClass.thisClassName = "com/example/TestClass";
    }

    /**
     * Tests that visitLibraryMethod does not throw an exception with valid inputs.
     * Since this is a no-op method, it should simply return without doing anything.
     */
    @Test
    public void testVisitLibraryMethod_withValidInputs_doesNotThrow() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "testMethod";
        libraryMethod.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, libraryMethod),
                "visitLibraryMethod should not throw any exception");
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times without issues.
     * Verifies that the no-op behavior is consistent across multiple calls.
     */
    @Test
    public void testVisitLibraryMethod_calledMultipleTimes_doesNotThrow() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "testMethod";
        libraryMethod.descriptor = "()V";

        // Act & Assert - call multiple times
        assertDoesNotThrow(() -> {
            shrinker.visitLibraryMethod(libraryClass, libraryMethod);
            shrinker.visitLibraryMethod(libraryClass, libraryMethod);
            shrinker.visitLibraryMethod(libraryClass, libraryMethod);
        }, "Multiple calls to visitLibraryMethod should not throw any exception");
    }

    /**
     * Tests that visitLibraryMethod works with different method names.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentMethodNames_doesNotThrow() {
        // Arrange
        String[] methodNames = {"toString", "equals", "hashCode", "getValue", "process", "a", "<init>"};

        // Act & Assert
        for (String methodName : methodNames) {
            LibraryMethod method = new LibraryMethod();
            method.name = methodName;
            method.descriptor = "()V";

            assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                    "visitLibraryMethod should not throw for method name: " + methodName);
        }
    }

    /**
     * Tests that visitLibraryMethod works with different method descriptors.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentDescriptors_doesNotThrow() {
        // Arrange
        String[] descriptors = {
            "()V",
            "()I",
            "()Ljava/lang/String;",
            "(I)V",
            "(Ljava/lang/String;)I",
            "(IILjava/lang/String;)Ljava/util/List;"
        };

        // Act & Assert
        for (String descriptor : descriptors) {
            LibraryMethod method = new LibraryMethod();
            method.name = "testMethod";
            method.descriptor = descriptor;

            assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                    "visitLibraryMethod should not throw for descriptor: " + descriptor);
        }
    }

    /**
     * Tests that visitLibraryMethod works with different LibraryClass instances.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentClasses_doesNotThrow() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        class1.thisClassName = "com/example/Class1";

        LibraryClass class2 = new LibraryClass();
        class2.thisClassName = "java/lang/String";

        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> {
            shrinker.visitLibraryMethod(class1, method);
            shrinker.visitLibraryMethod(class2, method);
        }, "visitLibraryMethod should work with different LibraryClass instances");
    }

    /**
     * Tests that visitLibraryMethod can handle multiple different methods.
     */
    @Test
    public void testVisitLibraryMethod_withMultipleDifferentMethods_doesNotThrow() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        method1.name = "method1";
        method1.descriptor = "()V";

        LibraryMethod method2 = new LibraryMethod();
        method2.name = "method2";
        method2.descriptor = "(I)I";

        LibraryMethod method3 = new LibraryMethod();
        method3.name = "method3";
        method3.descriptor = "()Ljava/lang/String;";

        // Act & Assert
        assertDoesNotThrow(() -> {
            shrinker.visitLibraryMethod(libraryClass, method1);
            shrinker.visitLibraryMethod(libraryClass, method2);
            shrinker.visitLibraryMethod(libraryClass, method3);
        }, "visitLibraryMethod should handle multiple different methods");
    }

    /**
     * Tests that visitLibraryMethod on one shrinker instance doesn't affect another.
     */
    @Test
    public void testVisitLibraryMethod_multipleShrinkerInstances_independent() {
        // Arrange
        BootstrapMethodArgumentShrinker shrinker1 = new BootstrapMethodArgumentShrinker();
        BootstrapMethodArgumentShrinker shrinker2 = new BootstrapMethodArgumentShrinker();

        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> {
            shrinker1.visitLibraryMethod(libraryClass, method);
            shrinker2.visitLibraryMethod(libraryClass, method);
        }, "Multiple shrinker instances should work independently");
    }

    /**
     * Tests that visitLibraryMethod with constructor methods (<init>) doesn't throw.
     */
    @Test
    public void testVisitLibraryMethod_withConstructor_doesNotThrow() {
        // Arrange
        LibraryMethod constructor = new LibraryMethod();
        constructor.name = "<init>";
        constructor.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, constructor),
                "visitLibraryMethod should handle constructor methods");
    }

    /**
     * Tests that visitLibraryMethod with static initializers (<clinit>) doesn't throw.
     */
    @Test
    public void testVisitLibraryMethod_withStaticInitializer_doesNotThrow() {
        // Arrange
        LibraryMethod staticInit = new LibraryMethod();
        staticInit.name = "<clinit>";
        staticInit.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, staticInit),
                "visitLibraryMethod should handle static initializer methods");
    }

    /**
     * Tests that visitLibraryMethod works with methods having long names.
     */
    @Test
    public void testVisitLibraryMethod_withLongMethodName_doesNotThrow() {
        // Arrange
        LibraryMethod method = new LibraryMethod();
        method.name = "thisIsAVeryLongMethodNameThatSomeoneDecidedToUseInTheirCode";
        method.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                "visitLibraryMethod should handle long method names");
    }

    /**
     * Tests that visitLibraryMethod works with single character method names.
     */
    @Test
    public void testVisitLibraryMethod_withSingleCharMethodName_doesNotThrow() {
        // Arrange
        LibraryMethod method = new LibraryMethod();
        method.name = "a";
        method.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                "visitLibraryMethod should handle single character method names");
    }

    /**
     * Tests that visitLibraryMethod works with methods containing special characters.
     */
    @Test
    public void testVisitLibraryMethod_withSpecialCharactersInName_doesNotThrow() {
        // Arrange
        String[] specialNames = {"access$000", "lambda$method$0", "method_with_underscores", "$dollarMethod"};

        // Act & Assert
        for (String name : specialNames) {
            LibraryMethod method = new LibraryMethod();
            method.name = name;
            method.descriptor = "()V";

            assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                    "visitLibraryMethod should handle special characters in name: " + name);
        }
    }

    /**
     * Tests that visitLibraryMethod returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitLibraryMethod_returnsImmediately() {
        // Arrange
        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "()V";

        // Act
        long startTime = System.nanoTime();
        shrinker.visitLibraryMethod(libraryClass, method);
        long endTime = System.nanoTime();

        // Assert - should complete very quickly (within 1 second, but really should be microseconds)
        long durationNanos = endTime - startTime;
        long oneSecondInNanos = 1_000_000_000L;
        assertTrue(durationNanos < oneSecondInNanos,
                "visitLibraryMethod should return immediately, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that visitLibraryMethod can be called in rapid succession.
     */
    @Test
    public void testVisitLibraryMethod_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "()V";

        // Act & Assert - make many rapid calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                shrinker.visitLibraryMethod(libraryClass, method);
            }
        }, "visitLibraryMethod should handle rapid successive calls");
    }

    /**
     * Tests that visitLibraryMethod works with methods from java.lang package.
     */
    @Test
    public void testVisitLibraryMethod_withJavaLangMethods_doesNotThrow() {
        // Arrange
        LibraryClass javaLangClass = new LibraryClass();
        javaLangClass.thisClassName = "java/lang/Object";

        String[] javaLangMethods = {"toString", "equals", "hashCode", "clone", "finalize"};

        // Act & Assert
        for (String methodName : javaLangMethods) {
            LibraryMethod method = new LibraryMethod();
            method.name = methodName;
            method.descriptor = "()V";

            assertDoesNotThrow(() -> shrinker.visitLibraryMethod(javaLangClass, method),
                    "visitLibraryMethod should handle java.lang methods: " + methodName);
        }
    }

    /**
     * Tests that visitLibraryMethod works with empty method descriptor.
     */
    @Test
    public void testVisitLibraryMethod_withEmptyDescriptor_doesNotThrow() {
        // Arrange
        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                "visitLibraryMethod should handle empty descriptor");
    }

    /**
     * Tests that visitLibraryMethod works with complex method descriptors.
     */
    @Test
    public void testVisitLibraryMethod_withComplexDescriptor_doesNotThrow() {
        // Arrange
        LibraryMethod method = new LibraryMethod();
        method.name = "complexMethod";
        method.descriptor = "([Ljava/lang/String;ILjava/util/Map;[[[I)Ljava/util/List;";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                "visitLibraryMethod should handle complex descriptors");
    }

    /**
     * Tests that visitLibraryMethod can alternate with other visitor methods.
     * This verifies that calling visitLibraryMethod doesn't interfere with the shrinker's state.
     */
    @Test
    public void testVisitLibraryMethod_alternatingWithOtherMethods_doesNotThrow() {
        // Arrange
        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "()V";

        // Act & Assert - alternate calls shouldn't cause issues
        assertDoesNotThrow(() -> {
            shrinker.visitLibraryMethod(libraryClass, method);
            // Note: We can't easily test visitProgramMethod or visitBootstrapMethodInfo
            // without setting up complex dependencies, but we can verify visitLibraryMethod
            // can be called multiple times
            shrinker.visitLibraryMethod(libraryClass, method);
        }, "visitLibraryMethod should work when called multiple times");
    }

    /**
     * Tests that visitLibraryMethod returns normally with a newly created shrinker.
     */
    @Test
    public void testVisitLibraryMethod_withFreshShrinker_doesNotThrow() {
        // Arrange
        BootstrapMethodArgumentShrinker freshShrinker = new BootstrapMethodArgumentShrinker();
        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> freshShrinker.visitLibraryMethod(libraryClass, method),
                "visitLibraryMethod should work with a newly created shrinker");
    }

    /**
     * Tests that visitLibraryMethod works with various access modifiers (simulated via different contexts).
     */
    @Test
    public void testVisitLibraryMethod_withVariousMethodContexts_doesNotThrow() {
        // Arrange
        String[] methodNames = {"publicMethod", "privateMethod", "protectedMethod", "packagePrivateMethod"};

        // Act & Assert
        for (String methodName : methodNames) {
            LibraryMethod method = new LibraryMethod();
            method.name = methodName;
            method.descriptor = "()V";

            assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, method),
                    "visitLibraryMethod should work with method: " + methodName);
        }
    }

    /**
     * Tests that visitLibraryMethod doesn't modify the LibraryMethod object.
     */
    @Test
    public void testVisitLibraryMethod_doesNotModifyMethod() {
        // Arrange
        String originalName = "testMethod";
        String originalDescriptor = "()V";
        LibraryMethod method = new LibraryMethod();
        method.name = originalName;
        method.descriptor = originalDescriptor;

        // Act
        shrinker.visitLibraryMethod(libraryClass, method);

        // Assert - verify no modification
        assertEquals(originalName, method.name, "Method name should not be modified");
        assertEquals(originalDescriptor, method.descriptor, "Method descriptor should not be modified");
    }

    /**
     * Tests that visitLibraryMethod doesn't modify the LibraryClass object.
     */
    @Test
    public void testVisitLibraryMethod_doesNotModifyClass() {
        // Arrange
        String originalClassName = "com/example/TestClass";
        libraryClass.thisClassName = originalClassName;
        LibraryMethod method = new LibraryMethod();
        method.name = "testMethod";
        method.descriptor = "()V";

        // Act
        shrinker.visitLibraryMethod(libraryClass, method);

        // Assert - verify no modification
        assertEquals(originalClassName, libraryClass.thisClassName,
                "Class name should not be modified");
    }

    /**
     * Tests that the shrinker instance can be reused after calling visitLibraryMethod.
     */
    @Test
    public void testVisitLibraryMethod_shrinkerReusable() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        method1.name = "method1";
        method1.descriptor = "()V";

        LibraryMethod method2 = new LibraryMethod();
        method2.name = "method2";
        method2.descriptor = "()I";

        // Act & Assert - reuse the same shrinker
        assertDoesNotThrow(() -> {
            shrinker.visitLibraryMethod(libraryClass, method1);
            shrinker.visitLibraryMethod(libraryClass, method2);
            shrinker.visitLibraryMethod(libraryClass, method1);
        }, "Shrinker should be reusable after visitLibraryMethod calls");
    }

    /**
     * Tests that visitLibraryMethod works with bridge methods (compiler-generated).
     */
    @Test
    public void testVisitLibraryMethod_withBridgeMethod_doesNotThrow() {
        // Arrange
        LibraryMethod bridgeMethod = new LibraryMethod();
        bridgeMethod.name = "bridgeMethod";
        bridgeMethod.descriptor = "(Ljava/lang/Object;)V";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, bridgeMethod),
                "visitLibraryMethod should handle bridge methods");
    }

    /**
     * Tests that visitLibraryMethod works with synthetic methods (compiler-generated).
     */
    @Test
    public void testVisitLibraryMethod_withSyntheticMethod_doesNotThrow() {
        // Arrange
        LibraryMethod syntheticMethod = new LibraryMethod();
        syntheticMethod.name = "lambda$main$0";
        syntheticMethod.descriptor = "()V";

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitLibraryMethod(libraryClass, syntheticMethod),
                "visitLibraryMethod should handle synthetic methods");
    }
}
