package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.InnerClassesInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassObfuscator#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
 *
 * This class tests the visitInnerClassesAttribute method which ensures that outer classes
 * referenced in the InnerClassesAttribute have been assigned obfuscated names before
 * processing inner classes.
 *
 * The method delegates to innerClassEntriesAccept to process each inner class entry.
 */
public class ClassObfuscatorClaude_visitInnerClassesAttributeTest {

    private ClassObfuscator classObfuscator;
    private Clazz mockClazz;
    private InnerClassesAttribute mockInnerClassesAttribute;

    @BeforeEach
    public void setUp() {
        // Create a ClassObfuscator with minimal configuration
        classObfuscator = new ClassObfuscator(
            new proguard.classfile.ClassPool(),  // programClassPool
            new proguard.classfile.ClassPool(),  // libraryClassPool
            null,                                // classNameFactory
            null,                                // packageNameFactory
            true,                                // useMixedCaseClassNames
            null,                                // keepPackageNames
            null,                                // flattenPackageHierarchy
            null,                                // repackageClasses
            false,                               // allowAccessModification
            false                                // adaptKotlin
        );

        mockClazz = mock(Clazz.class);
        mockInnerClassesAttribute = mock(InnerClassesAttribute.class);
    }

    // Tests for visitInnerClassesAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/attribute/InnerClassesAttribute;)V

    /**
     * Tests that visitInnerClassesAttribute calls innerClassEntriesAccept on the attribute.
     * This is the primary behavior of the method - it delegates to the attribute to process
     * all inner class entries.
     */
    @Test
    public void testVisitInnerClassesAttribute_callsInnerClassEntriesAccept() {
        // Act
        classObfuscator.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);

        // Assert
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitInnerClassesAttribute_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> classObfuscator.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute));
    }

    /**
     * Tests that visitInnerClassesAttribute can be called multiple times on the same attribute.
     * Each call should delegate to innerClassEntriesAccept.
     */
    @Test
    public void testVisitInnerClassesAttribute_calledMultipleTimes() {
        // Act
        classObfuscator.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);
        classObfuscator.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);
        classObfuscator.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);

        // Assert
        verify(mockInnerClassesAttribute, times(3)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute works with different InnerClassesAttribute instances.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentAttributes() {
        // Arrange
        InnerClassesAttribute attribute1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attribute2 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attribute3 = mock(InnerClassesAttribute.class);

        // Act
        classObfuscator.visitInnerClassesAttribute(mockClazz, attribute1);
        classObfuscator.visitInnerClassesAttribute(mockClazz, attribute2);
        classObfuscator.visitInnerClassesAttribute(mockClazz, attribute3);

        // Assert
        verify(attribute1, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
        verify(attribute2, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
        verify(attribute3, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute works with different Clazz instances.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        classObfuscator.visitInnerClassesAttribute(clazz1, mockInnerClassesAttribute);
        classObfuscator.visitInnerClassesAttribute(clazz2, mockInnerClassesAttribute);
        classObfuscator.visitInnerClassesAttribute(clazz3, mockInnerClassesAttribute);

        // Assert
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(clazz1), eq(classObfuscator));
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(clazz2), eq(classObfuscator));
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(clazz3), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute passes the correct ClassObfuscator instance
     * (itself) as the visitor to innerClassEntriesAccept.
     */
    @Test
    public void testVisitInnerClassesAttribute_passesCorrectVisitor() {
        // Arrange
        ClassObfuscator anotherObfuscator = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );

        InnerClassesAttribute attribute1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attribute2 = mock(InnerClassesAttribute.class);

        // Act
        classObfuscator.visitInnerClassesAttribute(mockClazz, attribute1);
        anotherObfuscator.visitInnerClassesAttribute(mockClazz, attribute2);

        // Assert
        // Each obfuscator should pass itself as the visitor
        verify(attribute1, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
        verify(attribute2, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(anotherObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute handles an attribute with no inner class entries.
     * The method should still call innerClassEntriesAccept even if there are no entries.
     */
    @Test
    public void testVisitInnerClassesAttribute_withEmptyAttribute() {
        // Arrange
        InnerClassesAttribute emptyAttribute = mock(InnerClassesAttribute.class);
        // Mock the behavior to do nothing when innerClassEntriesAccept is called
        doNothing().when(emptyAttribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        classObfuscator.visitInnerClassesAttribute(mockClazz, emptyAttribute);

        // Assert
        verify(emptyAttribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute properly delegates when the attribute
     * has multiple inner class entries by verifying the callback is invoked.
     */
    @Test
    public void testVisitInnerClassesAttribute_withMultipleInnerClassEntries() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Simulate the attribute calling back to the visitor for each inner class entry
        doAnswer(invocation -> {
            Clazz clazz = invocation.getArgument(0);
            ClassObfuscator visitor = invocation.getArgument(1);

            // Simulate calling visitInnerClassesInfo for multiple entries
            InnerClassesInfo info1 = mock(InnerClassesInfo.class);
            InnerClassesInfo info2 = mock(InnerClassesInfo.class);
            InnerClassesInfo info3 = mock(InnerClassesInfo.class);

            visitor.visitInnerClassesInfo(clazz, info1);
            visitor.visitInnerClassesInfo(clazz, info2);
            visitor.visitInnerClassesInfo(clazz, info3);

            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        classObfuscator.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
    }

    /**
     * Tests that multiple ClassObfuscator instances can independently process
     * InnerClassesAttributes without interfering with each other.
     */
    @Test
    public void testVisitInnerClassesAttribute_multipleObfuscatorsIndependent() {
        // Arrange
        ClassObfuscator obfuscator1 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );
        ClassObfuscator obfuscator2 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );

        InnerClassesAttribute attr1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr2 = mock(InnerClassesAttribute.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        obfuscator1.visitInnerClassesAttribute(clazz1, attr1);
        obfuscator2.visitInnerClassesAttribute(clazz2, attr2);

        // Assert
        verify(attr1, times(1)).innerClassEntriesAccept(eq(clazz1), eq(obfuscator1));
        verify(attr2, times(1)).innerClassEntriesAccept(eq(clazz2), eq(obfuscator2));
    }

    /**
     * Tests that visitInnerClassesAttribute correctly integrates with the ClassObfuscator's
     * role as an InnerClassesInfoVisitor by verifying the visitor pattern works end-to-end.
     */
    @Test
    public void testVisitInnerClassesAttribute_visitorPatternIntegration() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);
        Clazz clazz = mock(Clazz.class);

        // Use a flag to track if the visitor pattern was properly invoked
        final boolean[] visitorWasCalled = {false};

        doAnswer(invocation -> {
            visitorWasCalled[0] = true;
            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        classObfuscator.visitInnerClassesAttribute(clazz, attribute);

        // Assert
        assertTrue(visitorWasCalled[0], "The visitor pattern should have been invoked");
        verify(attribute, times(1)).innerClassEntriesAccept(eq(clazz), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute works correctly when called as part of
     * the AttributeVisitor interface implementation.
     */
    @Test
    public void testVisitInnerClassesAttribute_asAttributeVisitor() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Act - Call through the AttributeVisitor interface
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = classObfuscator;
        visitor.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute maintains proper state when processing
     * multiple attributes in sequence.
     */
    @Test
    public void testVisitInnerClassesAttribute_sequentialProcessing() {
        // Arrange
        InnerClassesAttribute attr1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr2 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr3 = mock(InnerClassesAttribute.class);

        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act - Process multiple attributes in sequence
        classObfuscator.visitInnerClassesAttribute(clazz1, attr1);
        classObfuscator.visitInnerClassesAttribute(clazz2, attr2);
        classObfuscator.visitInnerClassesAttribute(clazz3, attr3);

        // Assert - Each should have been processed exactly once with correct parameters
        verify(attr1, times(1)).innerClassEntriesAccept(eq(clazz1), eq(classObfuscator));
        verify(attr2, times(1)).innerClassEntriesAccept(eq(clazz2), eq(classObfuscator));
        verify(attr3, times(1)).innerClassEntriesAccept(eq(clazz3), eq(classObfuscator));
    }

    /**
     * Tests that visitInnerClassesAttribute works with different ClassObfuscator configurations.
     * This ensures the method behavior is consistent regardless of the obfuscator's settings.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentConfigurations() {
        // Arrange - Create obfuscators with different configurations
        ClassObfuscator obfuscator1 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );

        ClassObfuscator obfuscator2 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, false, null, null, null, true, true
        );

        InnerClassesAttribute attr1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr2 = mock(InnerClassesAttribute.class);

        // Act
        obfuscator1.visitInnerClassesAttribute(mockClazz, attr1);
        obfuscator2.visitInnerClassesAttribute(mockClazz, attr2);

        // Assert - Both should delegate correctly regardless of configuration
        verify(attr1, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(obfuscator1));
        verify(attr2, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(obfuscator2));
    }
}
