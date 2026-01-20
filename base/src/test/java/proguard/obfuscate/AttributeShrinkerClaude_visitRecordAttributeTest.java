package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.RecordAttribute;
import proguard.classfile.attribute.visitor.RecordComponentInfoVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AttributeShrinker#visitRecordAttribute(Clazz, RecordAttribute)}.
 *
 * The visitRecordAttribute method accepts record components by calling
 * recordAttribute.componentsAccept(clazz, this), which triggers the shrinking
 * of attributes in each record component.
 */
public class AttributeShrinkerClaude_visitRecordAttributeTest {

    private AttributeShrinker attributeShrinker;
    private Clazz clazz;
    private RecordAttribute recordAttribute;

    @BeforeEach
    public void setUp() {
        attributeShrinker = new AttributeShrinker();
        clazz = mock(Clazz.class);
        recordAttribute = mock(RecordAttribute.class);
    }

    /**
     * Tests that visitRecordAttribute invokes componentsAccept on the record attribute.
     * This is the main behavior of the method - delegating to the record attribute
     * to accept the visitor for each component.
     */
    @Test
    public void testVisitRecordAttribute_invokesComponentsAccept() {
        // Act
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute);

        // Assert - verify componentsAccept was called with the correct parameters
        verify(recordAttribute, times(1)).componentsAccept(clazz, attributeShrinker);
    }

    /**
     * Tests that visitRecordAttribute can be called without throwing exceptions.
     * This is a smoke test to ensure basic functionality.
     */
    @Test
    public void testVisitRecordAttribute_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> attributeShrinker.visitRecordAttribute(clazz, recordAttribute));
    }

    /**
     * Tests visitRecordAttribute with a null Clazz.
     * The method should handle this gracefully or delegate to componentsAccept.
     */
    @Test
    public void testVisitRecordAttribute_withNullClazz() {
        // Act & Assert - should not throw, delegating behavior to componentsAccept
        assertDoesNotThrow(() -> attributeShrinker.visitRecordAttribute(null, recordAttribute));

        // Verify componentsAccept was still called
        verify(recordAttribute, times(1)).componentsAccept(null, attributeShrinker);
    }

    /**
     * Tests that visitRecordAttribute passes the AttributeShrinker instance itself
     * as the RecordComponentInfoVisitor to componentsAccept.
     */
    @Test
    public void testVisitRecordAttribute_passesItselfAsVisitor() {
        // Act
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute);

        // Assert - verify the visitor passed is the AttributeShrinker itself
        verify(recordAttribute).componentsAccept(eq(clazz), same(attributeShrinker));
    }

    /**
     * Tests calling visitRecordAttribute multiple times with the same record attribute.
     * Each call should invoke componentsAccept.
     */
    @Test
    public void testVisitRecordAttribute_calledMultipleTimes() {
        // Act
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute);
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute);
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute);

        // Assert - verify componentsAccept was called three times
        verify(recordAttribute, times(3)).componentsAccept(clazz, attributeShrinker);
    }

    /**
     * Tests visitRecordAttribute with different record attributes sequentially.
     * Each should have componentsAccept called.
     */
    @Test
    public void testVisitRecordAttribute_withDifferentRecordAttributes() {
        // Arrange
        RecordAttribute recordAttribute1 = mock(RecordAttribute.class);
        RecordAttribute recordAttribute2 = mock(RecordAttribute.class);
        RecordAttribute recordAttribute3 = mock(RecordAttribute.class);

        // Act
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute1);
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute2);
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute3);

        // Assert - verify each had componentsAccept called exactly once
        verify(recordAttribute1, times(1)).componentsAccept(clazz, attributeShrinker);
        verify(recordAttribute2, times(1)).componentsAccept(clazz, attributeShrinker);
        verify(recordAttribute3, times(1)).componentsAccept(clazz, attributeShrinker);
    }

    /**
     * Tests visitRecordAttribute with different Clazz instances.
     * The method should work correctly with different class contexts.
     */
    @Test
    public void testVisitRecordAttribute_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        attributeShrinker.visitRecordAttribute(clazz1, recordAttribute);
        attributeShrinker.visitRecordAttribute(clazz2, recordAttribute);

        // Assert - verify componentsAccept was called with each clazz
        verify(recordAttribute, times(1)).componentsAccept(clazz1, attributeShrinker);
        verify(recordAttribute, times(1)).componentsAccept(clazz2, attributeShrinker);
    }

    /**
     * Tests that visitRecordAttribute works correctly when used with a fresh
     * AttributeShrinker instance each time.
     */
    @Test
    public void testVisitRecordAttribute_withFreshInstances() {
        // Arrange
        AttributeShrinker shrinker1 = new AttributeShrinker();
        AttributeShrinker shrinker2 = new AttributeShrinker();

        RecordAttribute attr1 = mock(RecordAttribute.class);
        RecordAttribute attr2 = mock(RecordAttribute.class);

        // Act
        shrinker1.visitRecordAttribute(clazz, attr1);
        shrinker2.visitRecordAttribute(clazz, attr2);

        // Assert
        verify(attr1, times(1)).componentsAccept(clazz, shrinker1);
        verify(attr2, times(1)).componentsAccept(clazz, shrinker2);
    }

    /**
     * Tests that the method correctly implements the AttributeVisitor interface contract.
     * The AttributeShrinker should be usable as a RecordComponentInfoVisitor when
     * passed to componentsAccept.
     */
    @Test
    public void testVisitRecordAttribute_implementsVisitorContract() {
        // Act
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute);

        // Assert - verify the visitor passed is an instance of RecordComponentInfoVisitor
        verify(recordAttribute).componentsAccept(any(Clazz.class),
                                                  any(RecordComponentInfoVisitor.class));
    }

    /**
     * Tests that visitRecordAttribute does not perform any direct modifications
     * to the RecordAttribute before calling componentsAccept.
     * All work is delegated to the componentsAccept method.
     */
    @Test
    public void testVisitRecordAttribute_doesNotModifyRecordAttributeDirectly() {
        // Act
        attributeShrinker.visitRecordAttribute(clazz, recordAttribute);

        // Assert - only componentsAccept should be called, no other methods
        verify(recordAttribute, only()).componentsAccept(clazz, attributeShrinker);
    }
}
