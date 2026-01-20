package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link FieldSignatureCollector}.
 * Tests the constructor and all visitor methods to ensure proper collection of field signatures.
 *
 * The FieldSignatureCollector is a simple visitor that collects the signature string from
 * SignatureAttribute instances attached to fields. It implements AttributeVisitor and provides
 * a getter to retrieve the collected signature.
 */
public class FieldSignatureCollectorClaudeTest {

    private FieldSignatureCollector collector;

    /**
     * Sets up a fresh FieldSignatureCollector instance before each test.
     */
    @BeforeEach
    public void setUp() {
        collector = new FieldSignatureCollector();
    }

    // =========================================================================
    // Tests for constructor: <init>.()V
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates an instance.
     * The constructor should initialize the collector with a null fieldSignature.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        FieldSignatureCollector newCollector = new FieldSignatureCollector();

        // Assert
        assertNotNull(newCollector, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor initializes fieldSignature to null.
     * This is the initial state before any signature has been collected.
     */
    @Test
    public void testConstructor_initializesFieldSignatureToNull() {
        // Act
        FieldSignatureCollector newCollector = new FieldSignatureCollector();

        // Assert
        assertNull(newCollector.getFieldSignature(),
                "fieldSignature should be initialized to null");
    }

    /**
     * Tests that the constructor creates an instance that implements AttributeVisitor.
     * FieldSignatureCollector must implement AttributeVisitor to visit attributes.
     */
    @Test
    public void testConstructor_implementsAttributeVisitor() {
        // Act
        FieldSignatureCollector newCollector = new FieldSignatureCollector();

        // Assert
        assertTrue(newCollector instanceof AttributeVisitor,
                "FieldSignatureCollector should implement AttributeVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Each instance should have its own state.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Act
        FieldSignatureCollector collector1 = new FieldSignatureCollector();
        FieldSignatureCollector collector2 = new FieldSignatureCollector();
        FieldSignatureCollector collector3 = new FieldSignatureCollector();

        // Assert
        assertNotNull(collector1, "First instance should be created");
        assertNotNull(collector2, "Second instance should be created");
        assertNotNull(collector3, "Third instance should be created");
        assertNotSame(collector1, collector2, "Instances should be distinct");
        assertNotSame(collector2, collector3, "Instances should be distinct");
        assertNotSame(collector1, collector3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new FieldSignatureCollector(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     * Verifies that each instance starts with null fieldSignature.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            FieldSignatureCollector newCollector = new FieldSignatureCollector();
            assertNotNull(newCollector, "Instance " + i + " should be created");
            assertNull(newCollector.getFieldSignature(),
                    "Instance " + i + " should have fieldSignature initialized to null");
        }
    }

    // =========================================================================
    // Tests for getFieldSignature.()Ljava/lang/String;
    // =========================================================================

    /**
     * Tests that getFieldSignature returns null when no signature has been collected.
     * This is the initial state after construction.
     */
    @Test
    public void testGetFieldSignature_initiallyReturnsNull() {
        // Act
        String signature = collector.getFieldSignature();

        // Assert
        assertNull(signature, "getFieldSignature should return null initially");
    }

    /**
     * Tests that getFieldSignature returns null after visiting a non-signature attribute.
     * Only SignatureAttribute should set the fieldSignature value.
     */
    @Test
    public void testGetFieldSignature_returnsNullAfterVisitingNonSignatureAttribute() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        collector.visitAnyAttribute(clazz, attribute);
        String signature = collector.getFieldSignature();

        // Assert
        assertNull(signature, "getFieldSignature should return null after visiting non-signature attribute");
    }

    /**
     * Tests that getFieldSignature can be called multiple times.
     * The method should return the same value consistently.
     */
    @Test
    public void testGetFieldSignature_multipleCalls_returnsSameValue() {
        // Act
        String signature1 = collector.getFieldSignature();
        String signature2 = collector.getFieldSignature();
        String signature3 = collector.getFieldSignature();

        // Assert
        assertNull(signature1, "First call should return null");
        assertNull(signature2, "Second call should return null");
        assertNull(signature3, "Third call should return null");
        assertSame(signature1, signature2, "Multiple calls should return same value");
        assertSame(signature2, signature3, "Multiple calls should return same value");
    }

    /**
     * Tests that getFieldSignature returns the collected signature after visiting SignatureAttribute.
     */
    @Test
    public void testGetFieldSignature_returnsCollectedSignature() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);
        String signature = collector.getFieldSignature();

        // Assert
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", signature,
                "getFieldSignature should return the collected signature");
    }

    /**
     * Tests that getFieldSignature returns the most recent signature when multiple signatures are visited.
     * Each call to visitSignatureAttribute should overwrite the previous signature.
     */
    @Test
    public void testGetFieldSignature_returnsLatestSignatureAfterMultipleVisits() {
        // Arrange
        TestClassWithSignature testClass1 = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        TestClassWithSignature testClass2 = createClassWithSignature("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;");
        TestClassWithSignature testClass3 = createClassWithSignature("Ljava/util/Set<Ljava/lang/Object;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass1.clazz, field, testClass1.signatureAttribute);
        collector.visitSignatureAttribute(testClass2.clazz, field, testClass2.signatureAttribute);
        collector.visitSignatureAttribute(testClass3.clazz, field, testClass3.signatureAttribute);
        String signature = collector.getFieldSignature();

        // Assert
        assertEquals("Ljava/util/Set<Ljava/lang/Object;>;", signature,
                "getFieldSignature should return the latest collected signature");
    }

    // =========================================================================
    // Tests for visitAnyAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/attribute/Attribute;)V
    // =========================================================================

    /**
     * Tests that visitAnyAttribute does nothing when called with valid arguments.
     * The method is a no-op implementation of the AttributeVisitor interface.
     */
    @Test
    public void testVisitAnyAttribute_withValidArguments_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);
        String initialSignature = collector.getFieldSignature();

        // Act
        collector.visitAnyAttribute(clazz, attribute);

        // Assert
        assertEquals(initialSignature, collector.getFieldSignature(),
                "visitAnyAttribute should not modify fieldSignature");
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyAttribute_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);

        // Act
        collector.visitAnyAttribute(clazz1, attribute1);
        collector.visitAnyAttribute(clazz2, attribute2);
        collector.visitAnyAttribute(clazz1, attribute2);

        // Assert
        assertNull(collector.getFieldSignature(),
                "fieldSignature should remain null after multiple visitAnyAttribute calls");
        verifyNoInteractions(clazz1, clazz2, attribute1, attribute2);
    }

    /**
     * Tests that visitAnyAttribute does not throw exceptions with null arguments.
     * While not recommended, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyAttribute_withNullArguments_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> collector.visitAnyAttribute(null, null),
                "visitAnyAttribute should not throw exception with null arguments");
    }

    /**
     * Tests that visitAnyAttribute does not affect previously collected signature.
     * The method should not interfere with the state set by visitSignatureAttribute.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectPreviouslyCollectedSignature() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        ProgramField field = mock(ProgramField.class);
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        Clazz otherClazz = mock(Clazz.class);
        Attribute otherAttribute = mock(Attribute.class);

        // Act
        collector.visitAnyAttribute(otherClazz, otherAttribute);

        // Assert
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", collector.getFieldSignature(),
                "visitAnyAttribute should not affect previously collected signature");
    }

    /**
     * Tests that visitAnyAttribute works with different attribute types.
     * The method should be a no-op regardless of attribute type.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeTypes_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute codeAttribute = mock(CodeAttribute.class);
        Attribute constantValueAttribute = mock(ConstantValueAttribute.class);
        Attribute deprecatedAttribute = mock(DeprecatedAttribute.class);

        // Act
        collector.visitAnyAttribute(clazz, codeAttribute);
        collector.visitAnyAttribute(clazz, constantValueAttribute);
        collector.visitAnyAttribute(clazz, deprecatedAttribute);

        // Assert
        assertNull(collector.getFieldSignature(),
                "fieldSignature should remain null for all non-signature attribute types");
    }

    // =========================================================================
    // Tests for visitSignatureAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/Field;Lproguard/classfile/attribute/SignatureAttribute;)V
    // =========================================================================

    /**
     * Tests that visitSignatureAttribute collects the signature from the attribute.
     * This is the primary functionality of the FieldSignatureCollector.
     */
    @Test
    public void testVisitSignatureAttribute_collectsSignature() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", collector.getFieldSignature(),
                "visitSignatureAttribute should collect the signature");
    }

    /**
     * Tests that visitSignatureAttribute works with a simple generic signature.
     */
    @Test
    public void testVisitSignatureAttribute_withSimpleGenericSignature() {
        // Arrange
        String expectedSignature = "Ljava/util/List<Ljava/lang/Integer;>;";
        TestClassWithSignature testClass = createClassWithSignature(expectedSignature);
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertEquals(expectedSignature, collector.getFieldSignature(),
                "visitSignatureAttribute should collect simple generic signature");
    }

    /**
     * Tests that visitSignatureAttribute works with a complex nested generic signature.
     */
    @Test
    public void testVisitSignatureAttribute_withComplexNestedGenericSignature() {
        // Arrange
        String expectedSignature = "Ljava/util/Map<Ljava/lang/String;Ljava/util/List<Ljava/lang/Integer;>;>;";
        TestClassWithSignature testClass = createClassWithSignature(expectedSignature);
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertEquals(expectedSignature, collector.getFieldSignature(),
                "visitSignatureAttribute should collect complex nested generic signature");
    }

    /**
     * Tests that visitSignatureAttribute works with a wildcard generic signature.
     */
    @Test
    public void testVisitSignatureAttribute_withWildcardGenericSignature() {
        // Arrange
        String expectedSignature = "Ljava/util/List<*>;";
        TestClassWithSignature testClass = createClassWithSignature(expectedSignature);
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertEquals(expectedSignature, collector.getFieldSignature(),
                "visitSignatureAttribute should collect wildcard generic signature");
    }

    /**
     * Tests that visitSignatureAttribute works with an array signature.
     */
    @Test
    public void testVisitSignatureAttribute_withArraySignature() {
        // Arrange
        String expectedSignature = "[Ljava/lang/String;";
        TestClassWithSignature testClass = createClassWithSignature(expectedSignature);
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertEquals(expectedSignature, collector.getFieldSignature(),
                "visitSignatureAttribute should collect array signature");
    }

    /**
     * Tests that visitSignatureAttribute overwrites previous signature.
     * Each call should replace the previously collected signature.
     */
    @Test
    public void testVisitSignatureAttribute_overwritesPreviousSignature() {
        // Arrange
        TestClassWithSignature testClass1 = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        TestClassWithSignature testClass2 = createClassWithSignature("Ljava/util/Set<Ljava/lang/Integer;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass1.clazz, field, testClass1.signatureAttribute);
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", collector.getFieldSignature(),
                "First signature should be collected");

        collector.visitSignatureAttribute(testClass2.clazz, field, testClass2.signatureAttribute);

        // Assert
        assertEquals("Ljava/util/Set<Ljava/lang/Integer;>;", collector.getFieldSignature(),
                "Second signature should overwrite first signature");
    }

    /**
     * Tests that visitSignatureAttribute can be called multiple times consecutively.
     * The most recent signature should always be retained.
     */
    @Test
    public void testVisitSignatureAttribute_multipleConsecutiveCalls() {
        // Arrange
        TestClassWithSignature testClass1 = createClassWithSignature("Ljava/lang/String;");
        TestClassWithSignature testClass2 = createClassWithSignature("Ljava/lang/Integer;");
        TestClassWithSignature testClass3 = createClassWithSignature("Ljava/lang/Boolean;");
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass1.clazz, field, testClass1.signatureAttribute);
        collector.visitSignatureAttribute(testClass2.clazz, field, testClass2.signatureAttribute);
        collector.visitSignatureAttribute(testClass3.clazz, field, testClass3.signatureAttribute);

        // Assert
        assertEquals("Ljava/lang/Boolean;", collector.getFieldSignature(),
                "Latest signature should be retained after multiple calls");
    }

    /**
     * Tests that visitSignatureAttribute works with an empty signature string.
     */
    @Test
    public void testVisitSignatureAttribute_withEmptySignature() {
        // Arrange
        String expectedSignature = "";
        TestClassWithSignature testClass = createClassWithSignature(expectedSignature);
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertEquals(expectedSignature, collector.getFieldSignature(),
                "visitSignatureAttribute should collect empty signature");
    }

    /**
     * Tests that visitSignatureAttribute works correctly with different field objects.
     * The field parameter is passed but not directly used by the implementation.
     */
    @Test
    public void testVisitSignatureAttribute_withDifferentFields() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        ProgramField field1 = mock(ProgramField.class);
        ProgramField field2 = mock(ProgramField.class);

        // Act
        FieldSignatureCollector collector1 = new FieldSignatureCollector();
        collector1.visitSignatureAttribute(testClass.clazz, field1, testClass.signatureAttribute);

        FieldSignatureCollector collector2 = new FieldSignatureCollector();
        collector2.visitSignatureAttribute(testClass.clazz, field2, testClass.signatureAttribute);

        // Assert
        assertEquals(collector1.getFieldSignature(), collector2.getFieldSignature(),
                "Signature collection should not depend on field object");
    }

    /**
     * Tests that visitSignatureAttribute can handle bounded type parameters.
     */
    @Test
    public void testVisitSignatureAttribute_withBoundedTypeParameter() {
        // Arrange
        String expectedSignature = "Ljava/util/List<+Ljava/lang/Number;>;";
        TestClassWithSignature testClass = createClassWithSignature(expectedSignature);
        ProgramField field = mock(ProgramField.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertEquals(expectedSignature, collector.getFieldSignature(),
                "visitSignatureAttribute should collect bounded type parameter signature");
    }

    /**
     * Tests that visitSignatureAttribute interacts correctly with visitAnyAttribute.
     * visitAnyAttribute should not affect a previously collected signature.
     */
    @Test
    public void testVisitSignatureAttribute_interactionWithVisitAnyAttribute() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        ProgramField field = mock(ProgramField.class);
        Attribute otherAttribute = mock(Attribute.class);

        // Act
        collector.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);
        String signatureAfterVisit = collector.getFieldSignature();

        collector.visitAnyAttribute(testClass.clazz, otherAttribute);
        String signatureAfterAnyAttribute = collector.getFieldSignature();

        // Assert
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", signatureAfterVisit,
                "Signature should be collected after visitSignatureAttribute");
        assertEquals(signatureAfterVisit, signatureAfterAnyAttribute,
                "Signature should not change after visitAnyAttribute");
    }

    // =========================================================================
    // Tests for instance independence
    // =========================================================================

    /**
     * Tests that multiple collector instances maintain independent state.
     * Each collector should track its own signature independently.
     */
    @Test
    public void testInstanceIndependence_multipleCollectors() {
        // Arrange
        FieldSignatureCollector collector1 = new FieldSignatureCollector();
        FieldSignatureCollector collector2 = new FieldSignatureCollector();

        TestClassWithSignature testClass1 = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        TestClassWithSignature testClass2 = createClassWithSignature("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        collector1.visitSignatureAttribute(testClass1.clazz, field, testClass1.signatureAttribute);
        collector2.visitSignatureAttribute(testClass2.clazz, field, testClass2.signatureAttribute);

        // Assert
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", collector1.getFieldSignature(),
                "First collector should have its own signature");
        assertEquals("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;", collector2.getFieldSignature(),
                "Second collector should have its own independent signature");
    }

    /**
     * Tests that a collector can be reused for multiple collections.
     * The collector should properly update its state with each new signature.
     */
    @Test
    public void testReusability_collectorCanBeReused() {
        // Arrange
        TestClassWithSignature testClass1 = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        TestClassWithSignature testClass2 = createClassWithSignature("Ljava/util/Set<Ljava/lang/Integer;>;");
        ProgramField field = mock(ProgramField.class);

        // Act & Assert - First use
        collector.visitSignatureAttribute(testClass1.clazz, field, testClass1.signatureAttribute);
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", collector.getFieldSignature(),
                "First signature should be collected");

        // Act & Assert - Second use (reuse)
        collector.visitSignatureAttribute(testClass2.clazz, field, testClass2.signatureAttribute);
        assertEquals("Ljava/util/Set<Ljava/lang/Integer;>;", collector.getFieldSignature(),
                "Collector should be reusable and update to new signature");
    }

    // =========================================================================
    // Helper methods to create test objects
    // =========================================================================

    /**
     * Creates a ProgramClass with a SignatureAttribute containing the given signature.
     * The signature is stored in the constant pool and accessible via SignatureAttribute.
     *
     * @param signatureString the signature string to store
     * @return a TestClassWithSignature containing the class and signature attribute
     */
    private TestClassWithSignature createClassWithSignature(String signatureString) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[10];
        constantPool[0] = null; // Index 0 is always null in Java constant pools
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant("TestClass");

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;

        // Create the SignatureAttribute
        SignatureAttribute signatureAttribute = new SignatureAttribute();
        int signatureIndex = getNextAvailableIndex(programClass);
        programClass.constantPool[signatureIndex] = new Utf8Constant(signatureString);
        signatureAttribute.u2signatureIndex = signatureIndex;

        return new TestClassWithSignature(programClass, signatureAttribute);
    }

    /**
     * Gets the next available index in the constant pool.
     */
    private int getNextAvailableIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("Constant pool is full");
    }

    /**
     * Helper class to hold a ProgramClass and its associated SignatureAttribute.
     */
    private static class TestClassWithSignature {
        final ProgramClass clazz;
        final SignatureAttribute signatureAttribute;

        TestClassWithSignature(ProgramClass clazz, SignatureAttribute signatureAttribute) {
            this.clazz = clazz;
            this.signatureAttribute = signatureAttribute;
        }
    }
}
