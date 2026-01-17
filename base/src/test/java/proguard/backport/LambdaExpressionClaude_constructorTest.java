package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.MethodHandleConstant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpression} constructor.
 * Tests LambdaExpression(ProgramClass, int, BootstrapMethodInfo, String, String[], String[], String, String, int, String, String, String, Clazz, Method) constructor.
 */
public class LambdaExpressionClaude_constructorTest {

    /**
     * Tests the constructor with all valid parameters.
     * Verifies that all fields are properly assigned.
     */
    @Test
    public void testConstructorWithAllValidParameters() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2thisClass = 1;
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated");
        assertSame(referencedClass, lambdaExpression.referencedClass);
        assertEquals(bootstrapMethodIndex, lambdaExpression.bootstrapMethodIndex);
        assertSame(bootstrapMethodInfo, lambdaExpression.bootstrapMethodInfo);
        assertEquals(factoryMethodDescriptor, lambdaExpression.factoryMethodDescriptor);
        assertSame(interfaces, lambdaExpression.interfaces);
        assertSame(bridgeMethodDescriptors, lambdaExpression.bridgeMethodDescriptors);
        assertEquals(interfaceMethod, lambdaExpression.interfaceMethod);
        assertEquals(interfaceMethodDescriptor, lambdaExpression.interfaceMethodDescriptor);
        assertEquals(invokedReferenceKind, lambdaExpression.invokedReferenceKind);
        assertEquals(invokedClassName, lambdaExpression.invokedClassName);
        assertEquals(invokedMethodName, lambdaExpression.invokedMethodName);
        assertEquals(invokedMethodDesc, lambdaExpression.invokedMethodDesc);
        assertSame(referencedInvokedClass, lambdaExpression.referencedInvokedClass);
        assertSame(referencedInvokedMethod, lambdaExpression.referencedInvokedMethod);
    }

    /**
     * Tests the constructor with null ProgramClass.
     * Verifies that null referencedClass is accepted.
     */
    @Test
    public void testConstructorWithNullProgramClass() {
        // Arrange
        ProgramClass referencedClass = null;
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with null referencedClass");
        assertNull(lambdaExpression.referencedClass);
    }

    /**
     * Tests the constructor with null BootstrapMethodInfo.
     * Verifies that null bootstrapMethodInfo is accepted.
     */
    @Test
    public void testConstructorWithNullBootstrapMethodInfo() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 1;
        BootstrapMethodInfo bootstrapMethodInfo = null;
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with null bootstrapMethodInfo");
        assertNull(lambdaExpression.bootstrapMethodInfo);
    }

    /**
     * Tests the constructor with null String parameters.
     * Verifies that null string values are accepted.
     */
    @Test
    public void testConstructorWithNullStrings() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = null;
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = null;
        String interfaceMethodDescriptor = null;
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = null;
        String invokedMethodName = null;
        String invokedMethodDesc = null;
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with null strings");
        assertNull(lambdaExpression.factoryMethodDescriptor);
        assertNull(lambdaExpression.interfaceMethod);
        assertNull(lambdaExpression.interfaceMethodDescriptor);
        assertNull(lambdaExpression.invokedClassName);
        assertNull(lambdaExpression.invokedMethodName);
        assertNull(lambdaExpression.invokedMethodDesc);
    }

    /**
     * Tests the constructor with null arrays.
     * Verifies that null array parameters are accepted.
     */
    @Test
    public void testConstructorWithNullArrays() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = null;
        String[] bridgeMethodDescriptors = null;
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with null arrays");
        assertNull(lambdaExpression.interfaces);
        assertNull(lambdaExpression.bridgeMethodDescriptors);
    }

    /**
     * Tests the constructor with null Clazz and Method.
     * Verifies that null referencedInvokedClass and referencedInvokedMethod are accepted.
     */
    @Test
    public void testConstructorWithNullClazzAndMethod() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = null;
        Method referencedInvokedMethod = null;

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with null clazz and method");
        assertNull(lambdaExpression.referencedInvokedClass);
        assertNull(lambdaExpression.referencedInvokedMethod);
    }

    /**
     * Tests the constructor with all null parameters.
     * Verifies that the constructor accepts all null values.
     */
    @Test
    public void testConstructorWithAllNullParameters() {
        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                null, // referencedClass
                0,    // bootstrapMethodIndex
                null, // bootstrapMethodInfo
                null, // factoryMethodDescriptor
                null, // interfaces
                null, // bridgeMethodDescriptors
                null, // interfaceMethod
                null, // interfaceMethodDescriptor
                0,    // invokedReferenceKind
                null, // invokedClassName
                null, // invokedMethodName
                null, // invokedMethodDesc
                null, // referencedInvokedClass
                null  // referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with all null parameters");
        assertNull(lambdaExpression.referencedClass);
        assertNull(lambdaExpression.bootstrapMethodInfo);
        assertNull(lambdaExpression.factoryMethodDescriptor);
        assertNull(lambdaExpression.interfaces);
        assertNull(lambdaExpression.bridgeMethodDescriptors);
        assertNull(lambdaExpression.interfaceMethod);
        assertNull(lambdaExpression.interfaceMethodDescriptor);
        assertNull(lambdaExpression.invokedClassName);
        assertNull(lambdaExpression.invokedMethodName);
        assertNull(lambdaExpression.invokedMethodDesc);
        assertNull(lambdaExpression.referencedInvokedClass);
        assertNull(lambdaExpression.referencedInvokedMethod);
    }

    /**
     * Tests the constructor with different bootstrap method indices.
     * Verifies that various bootstrap method index values are accepted.
     */
    @Test
    public void testConstructorWithDifferentBootstrapMethodIndices() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Test with index 0
        LambdaExpression lambda0 = new LambdaExpression(
                referencedClass, 0, bootstrapMethodInfo, factoryMethodDescriptor,
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                invokedReferenceKind, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals(0, lambda0.bootstrapMethodIndex);

        // Test with index 1
        LambdaExpression lambda1 = new LambdaExpression(
                referencedClass, 1, bootstrapMethodInfo, factoryMethodDescriptor,
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                invokedReferenceKind, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals(1, lambda1.bootstrapMethodIndex);

        // Test with index 100
        LambdaExpression lambda100 = new LambdaExpression(
                referencedClass, 100, bootstrapMethodInfo, factoryMethodDescriptor,
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                invokedReferenceKind, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals(100, lambda100.bootstrapMethodIndex);
    }

    /**
     * Tests the constructor with negative bootstrap method index.
     * Verifies that negative values are accepted (edge case).
     */
    @Test
    public void testConstructorWithNegativeBootstrapMethodIndex() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = -1;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with negative index");
        assertEquals(-1, lambdaExpression.bootstrapMethodIndex);
    }

    /**
     * Tests the constructor with different invoked reference kinds.
     * Verifies that various reference kinds are accepted.
     */
    @Test
    public void testConstructorWithDifferentInvokedReferenceKinds() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Test with REF_INVOKE_STATIC
        LambdaExpression lambdaStatic = new LambdaExpression(
                referencedClass, bootstrapMethodIndex, bootstrapMethodInfo, factoryMethodDescriptor,
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                MethodHandleConstant.REF_INVOKE_STATIC, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals(MethodHandleConstant.REF_INVOKE_STATIC, lambdaStatic.invokedReferenceKind);

        // Test with REF_INVOKE_VIRTUAL
        LambdaExpression lambdaVirtual = new LambdaExpression(
                referencedClass, bootstrapMethodIndex, bootstrapMethodInfo, factoryMethodDescriptor,
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                MethodHandleConstant.REF_INVOKE_VIRTUAL, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals(MethodHandleConstant.REF_INVOKE_VIRTUAL, lambdaVirtual.invokedReferenceKind);

        // Test with REF_NEW_INVOKE_SPECIAL
        LambdaExpression lambdaNew = new LambdaExpression(
                referencedClass, bootstrapMethodIndex, bootstrapMethodInfo, factoryMethodDescriptor,
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                MethodHandleConstant.REF_NEW_INVOKE_SPECIAL, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals(MethodHandleConstant.REF_NEW_INVOKE_SPECIAL, lambdaNew.invokedReferenceKind);
    }

    /**
     * Tests the constructor with empty string arrays.
     * Verifies that empty arrays are accepted.
     */
    @Test
    public void testConstructorWithEmptyArrays() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[0];
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with empty arrays");
        assertEquals(0, lambdaExpression.interfaces.length);
        assertEquals(0, lambdaExpression.bridgeMethodDescriptors.length);
    }

    /**
     * Tests the constructor with multiple interfaces.
     * Verifies that multiple interfaces are properly stored.
     */
    @Test
    public void testConstructorWithMultipleInterfaces() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier", "java/io/Serializable"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with multiple interfaces");
        assertEquals(2, lambdaExpression.interfaces.length);
        assertEquals("java/util/function/Supplier", lambdaExpression.interfaces[0]);
        assertEquals("java/io/Serializable", lambdaExpression.interfaces[1]);
    }

    /**
     * Tests the constructor with multiple bridge method descriptors.
     * Verifies that multiple bridge method descriptors are properly stored.
     */
    @Test
    public void testConstructorWithMultipleBridgeMethodDescriptors() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Function;";
        String[] interfaces = new String[]{"java/util/function/Function"};
        String[] bridgeMethodDescriptors = new String[]{"(Ljava/lang/Object;)Ljava/lang/Object;", "(Ljava/lang/String;)Ljava/lang/Integer;"};
        String interfaceMethod = "apply";
        String interfaceMethodDescriptor = "(Ljava/lang/Object;)Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "(Ljava/lang/String;)Ljava/lang/Integer;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with multiple bridge method descriptors");
        assertEquals(2, lambdaExpression.bridgeMethodDescriptors.length);
        assertEquals("(Ljava/lang/Object;)Ljava/lang/Object;", lambdaExpression.bridgeMethodDescriptors[0]);
        assertEquals("(Ljava/lang/String;)Ljava/lang/Integer;", lambdaExpression.bridgeMethodDescriptors[1]);
    }

    /**
     * Tests the constructor with empty strings.
     * Verifies that empty string values are accepted.
     */
    @Test
    public void testConstructorWithEmptyStrings() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "";
        String interfaceMethodDescriptor = "";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "";
        String invokedMethodName = "";
        String invokedMethodDesc = "";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with empty strings");
        assertEquals("", lambdaExpression.factoryMethodDescriptor);
        assertEquals("", lambdaExpression.interfaceMethod);
        assertEquals("", lambdaExpression.interfaceMethodDescriptor);
        assertEquals("", lambdaExpression.invokedClassName);
        assertEquals("", lambdaExpression.invokedMethodName);
        assertEquals("", lambdaExpression.invokedMethodDesc);
    }

    /**
     * Tests the constructor with various factory method descriptors.
     * Verifies that different factory method descriptors are properly stored.
     */
    @Test
    public void testConstructorWithVariousFactoryMethodDescriptors() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        int bootstrapMethodIndex = 0;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String[] interfaces = new String[]{"java/util/function/Function"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "apply";
        String interfaceMethodDescriptor = "(Ljava/lang/Object;)Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "TestClass";
        String invokedMethodName = "lambda$main$0";
        String invokedMethodDesc = "(Ljava/lang/String;)Ljava/lang/Integer;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Test with no-argument factory method
        LambdaExpression lambda1 = new LambdaExpression(
                referencedClass, bootstrapMethodIndex, bootstrapMethodInfo,
                "()Ljava/util/function/Function;",
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                invokedReferenceKind, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals("()Ljava/util/function/Function;", lambda1.factoryMethodDescriptor);

        // Test with single argument factory method
        LambdaExpression lambda2 = new LambdaExpression(
                referencedClass, bootstrapMethodIndex, bootstrapMethodInfo,
                "(Ljava/lang/String;)Ljava/util/function/Function;",
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                invokedReferenceKind, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals("(Ljava/lang/String;)Ljava/util/function/Function;", lambda2.factoryMethodDescriptor);

        // Test with multiple arguments factory method
        LambdaExpression lambda3 = new LambdaExpression(
                referencedClass, bootstrapMethodIndex, bootstrapMethodInfo,
                "(Ljava/lang/String;I)Ljava/util/function/Function;",
                interfaces, bridgeMethodDescriptors, interfaceMethod, interfaceMethodDescriptor,
                invokedReferenceKind, invokedClassName, invokedMethodName, invokedMethodDesc,
                referencedInvokedClass, referencedInvokedMethod
        );
        assertEquals("(Ljava/lang/String;I)Ljava/util/function/Function;", lambda3.factoryMethodDescriptor);
    }

    /**
     * Tests that multiple LambdaExpression instances are independent.
     * Verifies that each instance maintains its own state.
     */
    @Test
    public void testMultipleLambdaExpressionInstances() {
        // Arrange
        ProgramClass referencedClass1 = new ProgramClass();
        ProgramClass referencedClass2 = new ProgramClass();
        BootstrapMethodInfo bootstrapMethodInfo1 = new BootstrapMethodInfo();
        BootstrapMethodInfo bootstrapMethodInfo2 = new BootstrapMethodInfo();

        // Act - Create two different lambda expressions
        LambdaExpression lambda1 = new LambdaExpression(
                referencedClass1, 0, bootstrapMethodInfo1,
                "()Ljava/util/function/Supplier;",
                new String[]{"java/util/function/Supplier"}, new String[0],
                "get", "()Ljava/lang/Object;",
                MethodHandleConstant.REF_INVOKE_STATIC,
                "TestClass1", "lambda$main$0", "()Ljava/lang/String;",
                new ProgramClass(), new ProgramMethod()
        );

        LambdaExpression lambda2 = new LambdaExpression(
                referencedClass2, 1, bootstrapMethodInfo2,
                "()Ljava/util/function/Consumer;",
                new String[]{"java/util/function/Consumer"}, new String[0],
                "accept", "(Ljava/lang/Object;)V",
                MethodHandleConstant.REF_INVOKE_VIRTUAL,
                "TestClass2", "lambda$main$1", "(Ljava/lang/String;)V",
                new ProgramClass(), new ProgramMethod()
        );

        // Assert - Verify that instances are independent
        assertNotSame(lambda1, lambda2);
        assertNotSame(lambda1.referencedClass, lambda2.referencedClass);
        assertNotEquals(lambda1.bootstrapMethodIndex, lambda2.bootstrapMethodIndex);
        assertNotSame(lambda1.bootstrapMethodInfo, lambda2.bootstrapMethodInfo);
        assertNotEquals(lambda1.factoryMethodDescriptor, lambda2.factoryMethodDescriptor);
        assertNotEquals(lambda1.interfaceMethod, lambda2.interfaceMethod);
        assertNotEquals(lambda1.invokedClassName, lambda2.invokedClassName);
    }

    /**
     * Tests the constructor with ProgramClass having a name set.
     * Verifies that referencedClass is properly assigned.
     */
    @Test
    public void testConstructorWithNamedProgramClass() {
        // Arrange
        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2thisClass = 1;
        int bootstrapMethodIndex = 5;
        BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
        String factoryMethodDescriptor = "()Ljava/util/function/Supplier;";
        String[] interfaces = new String[]{"java/util/function/Supplier"};
        String[] bridgeMethodDescriptors = new String[0];
        String interfaceMethod = "get";
        String interfaceMethodDescriptor = "()Ljava/lang/Object;";
        int invokedReferenceKind = MethodHandleConstant.REF_INVOKE_STATIC;
        String invokedClassName = "com/example/TestClass";
        String invokedMethodName = "lambda$main$5";
        String invokedMethodDesc = "()Ljava/lang/String;";
        Clazz referencedInvokedClass = new ProgramClass();
        Method referencedInvokedMethod = new ProgramMethod();

        // Act
        LambdaExpression lambdaExpression = new LambdaExpression(
                referencedClass,
                bootstrapMethodIndex,
                bootstrapMethodInfo,
                factoryMethodDescriptor,
                interfaces,
                bridgeMethodDescriptors,
                interfaceMethod,
                interfaceMethodDescriptor,
                invokedReferenceKind,
                invokedClassName,
                invokedMethodName,
                invokedMethodDesc,
                referencedInvokedClass,
                referencedInvokedMethod
        );

        // Assert
        assertNotNull(lambdaExpression, "LambdaExpression should be instantiated with named ProgramClass");
        assertSame(referencedClass, lambdaExpression.referencedClass);
        assertEquals(5, lambdaExpression.bootstrapMethodIndex);
        assertEquals("com/example/TestClass", lambdaExpression.invokedClassName);
    }
}
