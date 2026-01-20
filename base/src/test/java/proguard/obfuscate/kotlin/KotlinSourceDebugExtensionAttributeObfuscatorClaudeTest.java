package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.SourceDebugExtensionAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinSourceDebugExtensionAttributeObfuscator}.
 * Tests all public methods:
 * - Constructor: KotlinSourceDebugExtensionAttributeObfuscator()
 * - visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)
 * - visitAnyAttribute(Clazz, Attribute)
 */
public class KotlinSourceDebugExtensionAttributeObfuscatorClaudeTest {

    private static final String EXPECTED_MINIMUM_SMAP = "SMAP\n" +
                                                        "\n" +
                                                        "Kotlin\n" +
                                                        "*S Kotlin\n" +
                                                        "*F\n" +
                                                        "+ 1 \n" +
                                                        "\n" +
                                                        "*L\n" +
                                                        "1#1,1:1\n" +
                                                        "*E";

    // ========== Constructor Tests ==========

    /**
     * Tests that the constructor creates a valid instance.
     * Verifies that the instance is created successfully.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();

        // Assert
        assertNotNull(obfuscator, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance implementing AttributeVisitor.
     * Verifies the correct interface implementation.
     */
    @Test
    public void testConstructor_implementsAttributeVisitor() {
        // Act
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();

        // Assert
        assertTrue(obfuscator instanceof AttributeVisitor,
                "KotlinSourceDebugExtensionAttributeObfuscator should implement AttributeVisitor");
    }

    /**
     * Tests that the constructor completes without throwing exceptions.
     * Verifies exception-safe construction.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new KotlinSourceDebugExtensionAttributeObfuscator());
    }

    /**
     * Tests that multiple instances can be created.
     * Verifies that multiple instances can coexist.
     */
    @Test
    public void testConstructor_multipleInstances() {
        // Act
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator1 =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator2 =
            new KotlinSourceDebugExtensionAttributeObfuscator();

        // Assert
        assertNotNull(obfuscator1);
        assertNotNull(obfuscator2);
        assertNotSame(obfuscator1, obfuscator2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that the constructor can be called multiple times in succession.
     * Verifies constructor stability.
     */
    @Test
    public void testConstructor_multipleSequentialCalls() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
                new KotlinSourceDebugExtensionAttributeObfuscator();
            assertNotNull(obfuscator, "Constructor should create instance " + i);
        }
    }

    /**
     * Tests that the constructor assigns to an AttributeVisitor variable.
     * Verifies polymorphic usage.
     */
    @Test
    public void testConstructor_assignableToAttributeVisitor() {
        // Act
        AttributeVisitor visitor = new KotlinSourceDebugExtensionAttributeObfuscator();

        // Assert
        assertNotNull(visitor, "Should be assignable to AttributeVisitor");
        assertTrue(visitor instanceof KotlinSourceDebugExtensionAttributeObfuscator);
    }

    /**
     * Tests that the constructor creates distinct instances.
     * Verifies that each call creates a new object.
     */
    @Test
    public void testConstructor_createsDistinctInstances() {
        // Act
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator1 =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator2 =
            new KotlinSourceDebugExtensionAttributeObfuscator();

        // Assert
        assertNotSame(obfuscator1, obfuscator2,
                "Each constructor call should create a new instance");
    }

    // ========== visitSourceDebugExtensionAttribute Tests ==========

    /**
     * Tests that visitSourceDebugExtensionAttribute sets the attribute info to minimum SMAP.
     * Verifies the basic functionality.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_setsMinimumSMAP() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = "original content".getBytes();
        attribute.u4attributeLength = attribute.info.length;

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        assertNotNull(attribute.info, "Info should not be null after visit");
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute.info),
                "Info should be set to minimum SMAP");
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute sets the attribute length correctly.
     * Verifies that u4attributeLength is updated to match the info length.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_setsAttributeLength() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = "some initial data".getBytes();
        attribute.u4attributeLength = 999;

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        assertEquals(attribute.info.length, attribute.u4attributeLength,
                "Attribute length should match info length");
        assertEquals(EXPECTED_MINIMUM_SMAP.getBytes().length, attribute.u4attributeLength,
                "Attribute length should be the length of minimum SMAP");
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute replaces existing content.
     * Verifies that any existing content is replaced.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_replacesExistingContent() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        String originalContent = "SMAP\nOriginal.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Original.kt\ncom/example/Original\n*L\n1#1,100:1\n*E";
        attribute.info = originalContent.getBytes();
        attribute.u4attributeLength = attribute.info.length;

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        assertNotEquals(originalContent, new String(attribute.info),
                "Original content should be replaced");
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute.info),
                "Content should be replaced with minimum SMAP");
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute works with empty initial content.
     * Verifies handling of empty info arrays.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_withEmptyInitialContent() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = new byte[0];
        attribute.u4attributeLength = 0;

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        assertTrue(attribute.info.length > 0, "Info should have content after visit");
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute.info),
                "Info should be set to minimum SMAP");
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute can be called multiple times.
     * Verifies that the method is reusable.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_calledMultipleTimes() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);

        SourceDebugExtensionAttribute attribute1 = new SourceDebugExtensionAttribute();
        attribute1.info = "content1".getBytes();

        SourceDebugExtensionAttribute attribute2 = new SourceDebugExtensionAttribute();
        attribute2.info = "content2".getBytes();

        SourceDebugExtensionAttribute attribute3 = new SourceDebugExtensionAttribute();
        attribute3.info = "content3".getBytes();

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute1);
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute2);
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute3);

        // Assert
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute1.info));
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute2.info));
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute3.info));
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute works with different Clazz instances.
     * Verifies that the method processes attributes independently.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_withDifferentClazzes() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();

        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        SourceDebugExtensionAttribute attribute1 = new SourceDebugExtensionAttribute();
        attribute1.info = "data1".getBytes();

        SourceDebugExtensionAttribute attribute2 = new SourceDebugExtensionAttribute();
        attribute2.info = "data2".getBytes();

        SourceDebugExtensionAttribute attribute3 = new SourceDebugExtensionAttribute();
        attribute3.info = "data3".getBytes();

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz1, attribute1);
        obfuscator.visitSourceDebugExtensionAttribute(clazz2, attribute2);
        obfuscator.visitSourceDebugExtensionAttribute(clazz3, attribute3);

        // Assert
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute1.info));
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute2.info));
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute3.info));
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute does not interact with the Clazz.
     * Verifies that the Clazz is not modified.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_doesNotModifyClazz() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = "content".getBytes();

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute completes without throwing exceptions.
     * Verifies exception-safe execution.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_doesNotThrowException() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = "content".getBytes();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute));
    }

    /**
     * Tests that the minimum SMAP format is correct.
     * Verifies the SMAP structure and content.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_minimumSMAPFormat() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = "original".getBytes();

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        String result = new String(attribute.info);
        assertTrue(result.startsWith("SMAP\n"), "Should start with SMAP header");
        assertTrue(result.contains("Kotlin\n"), "Should contain Kotlin identifier");
        assertTrue(result.contains("*S Kotlin\n"), "Should contain Kotlin stratum");
        assertTrue(result.contains("*F\n"), "Should contain file section");
        assertTrue(result.contains("*L\n"), "Should contain lines section");
        assertTrue(result.endsWith("*E"), "Should end with *E");
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute produces consistent results.
     * Verifies that the same input always produces the same output.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_consistentResults() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);

        SourceDebugExtensionAttribute attribute1 = new SourceDebugExtensionAttribute();
        attribute1.info = "content".getBytes();

        SourceDebugExtensionAttribute attribute2 = new SourceDebugExtensionAttribute();
        attribute2.info = "different content".getBytes();

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute1);
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute2);

        // Assert
        assertArrayEquals(attribute1.info, attribute2.info,
                "Same obfuscator should produce identical results");
    }

    /**
     * Tests that multiple obfuscators produce the same result.
     * Verifies consistency across different instances.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_multipleObfuscatorsSameResult() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator1 =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator2 =
            new KotlinSourceDebugExtensionAttributeObfuscator();

        Clazz clazz = mock(Clazz.class);

        SourceDebugExtensionAttribute attribute1 = new SourceDebugExtensionAttribute();
        attribute1.info = "content".getBytes();

        SourceDebugExtensionAttribute attribute2 = new SourceDebugExtensionAttribute();
        attribute2.info = "content".getBytes();

        // Act
        obfuscator1.visitSourceDebugExtensionAttribute(clazz, attribute1);
        obfuscator2.visitSourceDebugExtensionAttribute(clazz, attribute2);

        // Assert
        assertArrayEquals(attribute1.info, attribute2.info,
                "Different obfuscators should produce identical results");
    }

    /**
     * Tests that the info byte array is actually replaced, not just modified.
     * Verifies that a new byte array is created.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_replacesInfoArray() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        byte[] originalInfo = "original content".getBytes();
        attribute.info = originalInfo;

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        assertNotSame(originalInfo, attribute.info,
                "Info should be replaced with a new byte array");
    }

    /**
     * Tests that visitSourceDebugExtensionAttribute works through the AttributeVisitor interface.
     * Verifies polymorphic behavior.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_throughAttributeVisitorInterface() {
        // Arrange
        AttributeVisitor visitor = new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = "content".getBytes();

        // Act
        visitor.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute.info));
    }

    // ========== visitAnyAttribute Tests ==========

    /**
     * Tests that visitAnyAttribute does nothing.
     * Verifies that the method has no side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNothing() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        obfuscator.visitAnyAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute completes without throwing exceptions.
     * Verifies exception-safe execution.
     */
    @Test
    public void testVisitAnyAttribute_doesNotThrowException() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times.
     * Verifies that the method is reusable.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        obfuscator.visitAnyAttribute(clazz, attribute);
        obfuscator.visitAnyAttribute(clazz, attribute);
        obfuscator.visitAnyAttribute(clazz, attribute);

        // Assert - No exceptions should be thrown
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works with different attribute types.
     * Verifies that any Attribute implementation is handled.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeTypes() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);

        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        Attribute attribute3 = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitAnyAttribute(clazz, attribute1));
        assertDoesNotThrow(() -> obfuscator.visitAnyAttribute(clazz, attribute2));
        assertDoesNotThrow(() -> obfuscator.visitAnyAttribute(clazz, attribute3));
    }

    /**
     * Tests that visitAnyAttribute works through the AttributeVisitor interface.
     * Verifies polymorphic behavior.
     */
    @Test
    public void testVisitAnyAttribute_throughAttributeVisitorInterface() {
        // Arrange
        AttributeVisitor visitor = new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    // ========== Integration Tests ==========

    /**
     * Tests the complete workflow of visiting a SourceDebugExtensionAttribute.
     * Verifies that the obfuscator can handle a realistic scenario.
     */
    @Test
    public void testCompleteWorkflow_visitSourceDebugExtension() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        attribute.info = "SMAP\nMyClass.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MyClass.kt\ncom/example/MyClass\n*L\n1#1,50:1\n*E".getBytes();
        attribute.u4attributeLength = attribute.info.length;

        int originalLength = attribute.info.length;

        // Act
        obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        assertNotNull(attribute.info);
        assertNotEquals(originalLength, attribute.info.length);
        assertEquals(attribute.info.length, attribute.u4attributeLength);
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute.info));
    }

    /**
     * Tests that class name and package are correct.
     * Verifies class metadata.
     */
    @Test
    public void testMetadata_classNameAndPackage() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();

        // Assert
        assertEquals("KotlinSourceDebugExtensionAttributeObfuscator",
                obfuscator.getClass().getSimpleName());
        assertEquals("proguard.obfuscate.kotlin",
                obfuscator.getClass().getPackage().getName());
    }

    /**
     * Tests processing multiple attributes in sequence.
     * Verifies that the obfuscator maintains state correctly.
     */
    @Test
    public void testCompleteWorkflow_multipleAttributes() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);

        SourceDebugExtensionAttribute[] attributes = new SourceDebugExtensionAttribute[5];
        for (int i = 0; i < 5; i++) {
            attributes[i] = new SourceDebugExtensionAttribute();
            attributes[i].info = ("content" + i).getBytes();
            attributes[i].u4attributeLength = attributes[i].info.length;
        }

        // Act
        for (SourceDebugExtensionAttribute attribute : attributes) {
            obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);
        }

        // Assert
        for (SourceDebugExtensionAttribute attribute : attributes) {
            assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute.info));
            assertEquals(attribute.info.length, attribute.u4attributeLength);
        }
    }

    /**
     * Tests that visitAnyAttribute doesn't affect visitSourceDebugExtensionAttribute behavior.
     * Verifies that the two methods are independent.
     */
    @Test
    public void testBothMethods_independentBehavior() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);

        Attribute anyAttribute = mock(Attribute.class);
        SourceDebugExtensionAttribute debugAttribute = new SourceDebugExtensionAttribute();
        debugAttribute.info = "content".getBytes();

        // Act
        obfuscator.visitAnyAttribute(clazz, anyAttribute);
        obfuscator.visitSourceDebugExtensionAttribute(clazz, debugAttribute);
        obfuscator.visitAnyAttribute(clazz, anyAttribute);

        // Assert
        assertEquals(EXPECTED_MINIMUM_SMAP, new String(debugAttribute.info));
        verifyNoInteractions(anyAttribute);
    }

    /**
     * Tests the obfuscator with a realistic scenario of many attributes.
     * Verifies behavior in a typical use case with many invocations.
     */
    @Test
    public void testRealisticScenario_obfuscateManyAttributes() {
        // Arrange
        KotlinSourceDebugExtensionAttributeObfuscator obfuscator =
            new KotlinSourceDebugExtensionAttributeObfuscator();
        Clazz clazz = mock(Clazz.class);

        // Act & Assert - Process 100 attributes
        for (int i = 0; i < 100; i++) {
            SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
            attribute.info = ("content" + i).getBytes();
            attribute.u4attributeLength = attribute.info.length;

            obfuscator.visitSourceDebugExtensionAttribute(clazz, attribute);

            assertEquals(EXPECTED_MINIMUM_SMAP, new String(attribute.info));
            assertEquals(EXPECTED_MINIMUM_SMAP.getBytes().length, attribute.u4attributeLength);
        }
    }
}
