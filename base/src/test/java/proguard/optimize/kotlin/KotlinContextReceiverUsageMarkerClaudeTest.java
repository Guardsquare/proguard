package proguard.optimize.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinTypeMetadata;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;
import proguard.optimize.info.ParameterUsageMarker;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinContextReceiverUsageMarker}.
 *
 * Tests all methods in KotlinContextReceiverUsageMarker:
 * - <init>()V
 * - visitAnyKotlinMetadata
 * - visitKotlinClassMetadata
 * - visitAnyFunction
 * - visitConstructor
 * - visitAnyProperty
 *
 * The KotlinContextReceiverUsageMarker marks parameters as used if they correspond to
 * Kotlin context receivers. This ensures that context receiver parameters are not
 * removed during optimization.
 */
public class KotlinContextReceiverUsageMarkerClaudeTest {

    private KotlinContextReceiverUsageMarker marker;
    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        marker = new KotlinContextReceiverUsageMarker();
        testClass = createProgramClassWithConstantPool();
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the constructor creates a non-null instance.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        KotlinContextReceiverUsageMarker newMarker = new KotlinContextReceiverUsageMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a non-null instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsIndependentInstances() {
        // Act
        KotlinContextReceiverUsageMarker marker1 = new KotlinContextReceiverUsageMarker();
        KotlinContextReceiverUsageMarker marker2 = new KotlinContextReceiverUsageMarker();

        // Assert
        assertNotNull(marker1);
        assertNotNull(marker2);
        assertNotSame(marker1, marker2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that a newly constructed marker can be used immediately.
     */
    @Test
    public void testConstructor_instanceIsImmediatelyUsable() {
        // Arrange
        KotlinContextReceiverUsageMarker newMarker = new KotlinContextReceiverUsageMarker();
        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockMetadata = mock(KotlinMetadata.class);

        // Act & Assert
        assertDoesNotThrow(() -> newMarker.visitAnyKotlinMetadata(mockClazz, mockMetadata),
                "Newly constructed marker should be immediately usable");
    }

    // =========================================================================
    // visitAnyKotlinMetadata Tests
    // =========================================================================

    /**
     * Tests that visitAnyKotlinMetadata is a no-op (does nothing).
     */
    @Test
    public void testVisitAnyKotlinMetadata_isNoOp() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockMetadata = mock(KotlinMetadata.class);

        // Act
        marker.visitAnyKotlinMetadata(mockClazz, mockMetadata);

        // Assert - should not interact with parameters
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockMetadata);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called with null parameters.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withNullParameters_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyKotlinMetadata(null, null),
                "visitAnyKotlinMetadata should handle null parameters gracefully");
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called multiple times.
     */
    @Test
    public void testVisitAnyKotlinMetadata_canBeCalledMultipleTimes() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockMetadata = mock(KotlinMetadata.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(mockClazz, mockMetadata);
            marker.visitAnyKotlinMetadata(mockClazz, mockMetadata);
            marker.visitAnyKotlinMetadata(mockClazz, mockMetadata);
        }, "visitAnyKotlinMetadata should be callable multiple times");
    }

    // =========================================================================
    // visitKotlinClassMetadata Tests
    // =========================================================================

    /**
     * Tests that visitKotlinClassMetadata calls constructorsAccept on the metadata.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsConstructorsAccept() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockClassMetadata);

        // Assert
        verify(mockClassMetadata, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata passes the marker as the visitor.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesMarkerAsVisitor() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockClassMetadata);

        // Assert
        verify(mockClassMetadata, times(1)).constructorsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinClassMetadata can be called multiple times.
     */
    @Test
    public void testVisitKotlinClassMetadata_canBeCalledMultipleTimes() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockClassMetadata);
        marker.visitKotlinClassMetadata(mockClazz, mockClassMetadata);

        // Assert
        verify(mockClassMetadata, times(2)).constructorsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinClassMetadata with null clazz still calls constructorsAccept.
     */
    @Test
    public void testVisitKotlinClassMetadata_withNullClazz_callsConstructorsAccept() {
        // Arrange
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);

        // Act
        marker.visitKotlinClassMetadata(null, mockClassMetadata);

        // Assert
        verify(mockClassMetadata, times(1)).constructorsAccept(null, marker);
    }

    /**
     * Tests that visitKotlinClassMetadata with null metadata throws NullPointerException.
     */
    @Test
    public void testVisitKotlinClassMetadata_withNullMetadata_throwsNullPointerException() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                marker.visitKotlinClassMetadata(mockClazz, null),
                "Should throw NullPointerException when metadata is null");
    }

    // =========================================================================
    // visitAnyFunction Tests
    // =========================================================================

    /**
     * Tests visitAnyFunction with null context receivers does not mark parameters.
     */
    @Test
    public void testVisitAnyFunction_withNullContextReceivers_doesNotMarkParameters() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        mockFunctionMetadata.contextReceivers = null;
        mockFunctionMetadata.referencedMethod = method;

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - no parameters should be marked as used
        assertFalse(info.isParameterUsed(0), "Parameter 0 should not be marked");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be marked");
    }

    /**
     * Tests visitAnyFunction with empty context receivers list does not mark parameters.
     */
    @Test
    public void testVisitAnyFunction_withEmptyContextReceivers_doesNotMarkParameters() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        mockFunctionMetadata.contextReceivers = new ArrayList<>();
        mockFunctionMetadata.referencedMethod = method;

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - no parameters should be marked as used
        assertFalse(info.isParameterUsed(0), "Parameter 0 should not be marked");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be marked");
    }

    /**
     * Tests visitAnyFunction with one context receiver marks the correct parameter for instance method.
     */
    @Test
    public void testVisitAnyFunction_withOneContextReceiver_instanceMethod_marksFirstParameter() {
        // Arrange - instance method(contextReceiver, actualParam)
        // this=0, contextReceiver=1, actualParam=2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/String;I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        KotlinTypeMetadata mockContextReceiver = mock(KotlinTypeMetadata.class);
        mockFunctionMetadata.contextReceivers = Arrays.asList(mockContextReceiver);
        mockFunctionMetadata.referencedMethod = method;

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - parameter at index 1 (first context receiver after 'this') should be marked
        assertFalse(info.isParameterUsed(0), "Parameter 0 (this) should not be marked");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (context receiver) should be marked");
        assertFalse(info.isParameterUsed(2), "Parameter 2 (actual param) should not be marked");
    }

    /**
     * Tests visitAnyFunction with one context receiver marks the correct parameter for static method.
     */
    @Test
    public void testVisitAnyFunction_withOneContextReceiver_staticMethod_marksFirstParameter() {
        // Arrange - static method(contextReceiver, actualParam)
        // contextReceiver=0, actualParam=1
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/String;I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        KotlinTypeMetadata mockContextReceiver = mock(KotlinTypeMetadata.class);
        mockFunctionMetadata.contextReceivers = Arrays.asList(mockContextReceiver);
        mockFunctionMetadata.referencedMethod = method;

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - parameter at index 0 (first context receiver) should be marked
        assertTrue(info.isParameterUsed(0), "Parameter 0 (context receiver) should be marked");
        assertFalse(info.isParameterUsed(1), "Parameter 1 (actual param) should not be marked");
    }

    /**
     * Tests visitAnyFunction with multiple context receivers marks all of them for instance method.
     */
    @Test
    public void testVisitAnyFunction_withMultipleContextReceivers_instanceMethod_marksAllReceivers() {
        // Arrange - instance method(context1, context2, context3, actualParam)
        // this=0, context1=1, context2=2, context3=3, actualParam=4
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(
                mock(KotlinTypeMetadata.class),
                mock(KotlinTypeMetadata.class),
                mock(KotlinTypeMetadata.class)
        );
        mockFunctionMetadata.contextReceivers = contextReceivers;
        mockFunctionMetadata.referencedMethod = method;

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - parameters 1, 2, 3 should be marked (context receivers after 'this')
        assertFalse(info.isParameterUsed(0), "Parameter 0 (this) should not be marked");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (context1) should be marked");
        assertTrue(info.isParameterUsed(2), "Parameter 2 (context2) should be marked");
        assertTrue(info.isParameterUsed(3), "Parameter 3 (context3) should be marked");
        assertFalse(info.isParameterUsed(4), "Parameter 4 (actual param) should not be marked");
    }

    /**
     * Tests visitAnyFunction with multiple context receivers marks all of them for static method.
     */
    @Test
    public void testVisitAnyFunction_withMultipleContextReceivers_staticMethod_marksAllReceivers() {
        // Arrange - static method(context1, context2, actualParam)
        // context1=0, context2=1, actualParam=2
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;Ljava/lang/String;I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(
                mock(KotlinTypeMetadata.class),
                mock(KotlinTypeMetadata.class)
        );
        mockFunctionMetadata.contextReceivers = contextReceivers;
        mockFunctionMetadata.referencedMethod = method;

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - parameters 0, 1 should be marked (context receivers)
        assertTrue(info.isParameterUsed(0), "Parameter 0 (context1) should be marked");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (context2) should be marked");
        assertFalse(info.isParameterUsed(2), "Parameter 2 (actual param) should not be marked");
    }

    /**
     * Tests visitAnyFunction with null referenced method does not throw.
     */
    @Test
    public void testVisitAnyFunction_withNullReferencedMethod_doesNotThrow() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockFunctionMetadata.contextReceivers = contextReceivers;
        mockFunctionMetadata.referencedMethod = null;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata),
                "Should handle null referenced method gracefully");
    }

    /**
     * Tests visitAnyFunction with method that has no optimization info does not throw.
     */
    @Test
    public void testVisitAnyFunction_withMethodWithoutOptimizationInfo_doesNotThrow() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/String;)V");
        // Don't set optimization info

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockFunctionMetadata.contextReceivers = contextReceivers;
        mockFunctionMetadata.referencedMethod = method;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata),
                "Should handle method without optimization info gracefully");
    }

    /**
     * Tests visitAnyFunction with default method also marks parameters.
     */
    @Test
    public void testVisitAnyFunction_withDefaultMethod_marksParameters() {
        // Arrange
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/String;)V");
        ProgramMethod defaultMethod = createMethodWithDescriptor(testClass, "testMethod$default",
                "(Ljava/lang/String;ILjava/lang/Object;)V");

        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, defaultMethod);

        ProgramMethodOptimizationInfo methodInfo = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        ProgramMethodOptimizationInfo defaultInfo = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(defaultMethod);

        Clazz mockClazz = mock(Clazz.class);
        KotlinMetadata mockKotlinMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockFunctionMetadata.contextReceivers = contextReceivers;
        mockFunctionMetadata.referencedMethod = method;
        mockFunctionMetadata.referencedDefaultMethod = defaultMethod;

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - both methods should have context receiver marked
        assertTrue(methodInfo.isParameterUsed(1), "Method parameter 1 should be marked");
        assertTrue(defaultInfo.isParameterUsed(1), "Default method parameter 1 should be marked");
    }

    // =========================================================================
    // visitConstructor Tests
    // =========================================================================

    /**
     * Tests visitConstructor with null context receivers does not mark parameters.
     */
    @Test
    public void testVisitConstructor_withNullContextReceivers_doesNotMarkParameters() {
        // Arrange
        ProgramMethod constructor = createMethodWithDescriptor(testClass, "<init>", "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, constructor);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(constructor);

        Clazz mockClazz = mock(Clazz.class);
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.contextReceivers = null;
        KotlinConstructorMetadata mockConstructorMetadata = mock(KotlinConstructorMetadata.class);
        mockConstructorMetadata.referencedMethod = constructor;

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert
        assertFalse(info.isParameterUsed(0), "Parameter 0 should not be marked");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be marked");
    }

    /**
     * Tests visitConstructor with one context receiver marks the correct parameter.
     */
    @Test
    public void testVisitConstructor_withOneContextReceiver_marksFirstParameter() {
        // Arrange - constructor(this, contextReceiver, actualParam)
        // this=0, contextReceiver=1, actualParam=2
        ProgramMethod constructor = createMethodWithDescriptor(testClass, "<init>",
                "(Ljava/lang/String;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, constructor);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(constructor);

        Clazz mockClazz = mock(Clazz.class);
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockClassMetadata.contextReceivers = contextReceivers;
        KotlinConstructorMetadata mockConstructorMetadata = mock(KotlinConstructorMetadata.class);
        mockConstructorMetadata.referencedMethod = constructor;

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - parameter at index 1 (context receiver after 'this') should be marked
        assertFalse(info.isParameterUsed(0), "Parameter 0 (this) should not be marked");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (context receiver) should be marked");
        assertFalse(info.isParameterUsed(2), "Parameter 2 (actual param) should not be marked");
    }

    /**
     * Tests visitConstructor with multiple context receivers marks all of them.
     */
    @Test
    public void testVisitConstructor_withMultipleContextReceivers_marksAllReceivers() {
        // Arrange - constructor(this, context1, context2, actualParam)
        // this=0, context1=1, context2=2, actualParam=3
        ProgramMethod constructor = createMethodWithDescriptor(testClass, "<init>",
                "(Ljava/lang/String;Ljava/lang/String;I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, constructor);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(constructor);

        Clazz mockClazz = mock(Clazz.class);
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(
                mock(KotlinTypeMetadata.class),
                mock(KotlinTypeMetadata.class)
        );
        mockClassMetadata.contextReceivers = contextReceivers;
        KotlinConstructorMetadata mockConstructorMetadata = mock(KotlinConstructorMetadata.class);
        mockConstructorMetadata.referencedMethod = constructor;

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - parameters 1, 2 should be marked
        assertFalse(info.isParameterUsed(0), "Parameter 0 (this) should not be marked");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (context1) should be marked");
        assertTrue(info.isParameterUsed(2), "Parameter 2 (context2) should be marked");
        assertFalse(info.isParameterUsed(3), "Parameter 3 (actual param) should not be marked");
    }

    /**
     * Tests visitConstructor with null referenced method does not throw.
     */
    @Test
    public void testVisitConstructor_withNullReferencedMethod_doesNotThrow() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockClassMetadata.contextReceivers = contextReceivers;
        KotlinConstructorMetadata mockConstructorMetadata = mock(KotlinConstructorMetadata.class);
        mockConstructorMetadata.referencedMethod = null;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata),
                "Should handle null referenced method gracefully");
    }

    // =========================================================================
    // visitAnyProperty Tests
    // =========================================================================

    /**
     * Tests visitAnyProperty with null context receivers does not mark parameters.
     */
    @Test
    public void testVisitAnyProperty_withNullContextReceivers_doesNotMarkParameters() {
        // Arrange
        ProgramMethod getter = createMethodWithDescriptor(testClass, "getProperty", "()Ljava/lang/String;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, getter);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(getter);

        Clazz mockClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata mockPropertyMetadata = mock(KotlinPropertyMetadata.class);
        mockPropertyMetadata.contextReceivers = null;
        mockPropertyMetadata.referencedGetterMethod = getter;

        // Act
        marker.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        assertFalse(info.isParameterUsed(0), "Parameter 0 should not be marked");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be marked");
    }

    /**
     * Tests visitAnyProperty with one context receiver marks getter parameter.
     */
    @Test
    public void testVisitAnyProperty_withOneContextReceiver_marksGetterParameter() {
        // Arrange - getter(this, contextReceiver)
        // this=0, contextReceiver=1
        ProgramMethod getter = createMethodWithDescriptor(testClass, "getProperty",
                "(Ljava/lang/String;)Ljava/lang/String;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, getter);
        ProgramMethodOptimizationInfo getterInfo = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(getter);

        Clazz mockClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata mockPropertyMetadata = mock(KotlinPropertyMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockPropertyMetadata.contextReceivers = contextReceivers;
        mockPropertyMetadata.referencedGetterMethod = getter;

        // Act
        marker.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - parameter at index 1 should be marked
        assertFalse(getterInfo.isParameterUsed(0), "Parameter 0 (this) should not be marked");
        assertTrue(getterInfo.isParameterUsed(1), "Parameter 1 (context receiver) should be marked");
    }

    /**
     * Tests visitAnyProperty with one context receiver marks setter parameter.
     */
    @Test
    public void testVisitAnyProperty_withOneContextReceiver_marksSetterParameter() {
        // Arrange - setter(this, contextReceiver, value)
        // this=0, contextReceiver=1, value=2
        ProgramMethod setter = createMethodWithDescriptor(testClass, "setProperty",
                "(Ljava/lang/String;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, setter);
        ProgramMethodOptimizationInfo setterInfo = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(setter);

        Clazz mockClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata mockPropertyMetadata = mock(KotlinPropertyMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockPropertyMetadata.contextReceivers = contextReceivers;
        mockPropertyMetadata.referencedSetterMethod = setter;

        // Act
        marker.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - parameter at index 1 should be marked
        assertFalse(setterInfo.isParameterUsed(0), "Parameter 0 (this) should not be marked");
        assertTrue(setterInfo.isParameterUsed(1), "Parameter 1 (context receiver) should be marked");
        assertFalse(setterInfo.isParameterUsed(2), "Parameter 2 (value) should not be marked");
    }

    /**
     * Tests visitAnyProperty with context receivers marks both getter and setter.
     */
    @Test
    public void testVisitAnyProperty_withContextReceivers_marksBothGetterAndSetter() {
        // Arrange
        ProgramMethod getter = createMethodWithDescriptor(testClass, "getProperty",
                "(Ljava/lang/String;)Ljava/lang/String;");
        ProgramMethod setter = createMethodWithDescriptor(testClass, "setProperty",
                "(Ljava/lang/String;Ljava/lang/String;)V");

        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, getter);
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, setter);

        ProgramMethodOptimizationInfo getterInfo = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(getter);
        ProgramMethodOptimizationInfo setterInfo = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(setter);

        Clazz mockClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata mockPropertyMetadata = mock(KotlinPropertyMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockPropertyMetadata.contextReceivers = contextReceivers;
        mockPropertyMetadata.referencedGetterMethod = getter;
        mockPropertyMetadata.referencedSetterMethod = setter;

        // Act
        marker.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - both methods should have parameter 1 marked
        assertTrue(getterInfo.isParameterUsed(1), "Getter parameter 1 should be marked");
        assertTrue(setterInfo.isParameterUsed(1), "Setter parameter 1 should be marked");
    }

    /**
     * Tests visitAnyProperty with null getter and setter does not throw.
     */
    @Test
    public void testVisitAnyProperty_withNullGetterAndSetter_doesNotThrow() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata mockPropertyMetadata = mock(KotlinPropertyMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(mock(KotlinTypeMetadata.class));
        mockPropertyMetadata.contextReceivers = contextReceivers;
        mockPropertyMetadata.referencedGetterMethod = null;
        mockPropertyMetadata.referencedSetterMethod = null;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata),
                "Should handle null getter and setter gracefully");
    }

    /**
     * Tests visitAnyProperty with multiple context receivers marks all of them.
     */
    @Test
    public void testVisitAnyProperty_withMultipleContextReceivers_marksAllReceivers() {
        // Arrange - getter(this, context1, context2)
        ProgramMethod getter = createMethodWithDescriptor(testClass, "getProperty",
                "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, getter);
        ProgramMethodOptimizationInfo getterInfo = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(getter);

        Clazz mockClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata mockPropertyMetadata = mock(KotlinPropertyMetadata.class);
        List<KotlinTypeMetadata> contextReceivers = Arrays.asList(
                mock(KotlinTypeMetadata.class),
                mock(KotlinTypeMetadata.class)
        );
        mockPropertyMetadata.contextReceivers = contextReceivers;
        mockPropertyMetadata.referencedGetterMethod = getter;

        // Act
        marker.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - parameters 1, 2 should be marked
        assertFalse(getterInfo.isParameterUsed(0), "Parameter 0 (this) should not be marked");
        assertTrue(getterInfo.isParameterUsed(1), "Parameter 1 (context1) should be marked");
        assertTrue(getterInfo.isParameterUsed(2), "Parameter 2 (context2) should be marked");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Creates a ProgramClass with a minimal constant pool setup.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        Constant[] constantPool = new Constant[100];
        constantPool[0] = null;
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 100;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a static ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createStaticMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.STATIC;

        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Finds the next available index in the constant pool.
     */
    private int findNextAvailableConstantPoolIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("No available constant pool index");
    }
}
