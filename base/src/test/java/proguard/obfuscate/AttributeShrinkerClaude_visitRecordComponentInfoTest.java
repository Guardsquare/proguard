package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.RuntimeVisibleAnnotationsAttribute;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link AttributeShrinker#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
 *
 * The visitRecordComponentInfo method shrinks the attributes array of a RecordComponentInfo
 * by removing attributes that are not marked as used by AttributeUsageMarker.
 *
 * Testing approach: RecordComponentInfo is from proguard-core library and has public fields
 * (attributes and u2attributesCount) that can be directly accessed and modified. We instantiate
 * real RecordComponentInfo and Attribute objects to test the actual shrinking behavior.
 */
public class AttributeShrinkerClaude_visitRecordComponentInfoTest {

    private AttributeShrinker attributeShrinker;
    private AttributeUsageMarker attributeUsageMarker;

    @BeforeEach
    public void setUp() {
        attributeShrinker = new AttributeShrinker();
        attributeUsageMarker = new AttributeUsageMarker();
    }

    /**
     * Tests that visitRecordComponentInfo removes all unmarked attributes.
     * When all attributes are unmarked (not used), the attributes array should be empty
     * and the count should be 0.
     */
    @Test
    public void testVisitRecordComponentInfo_removesAllUnmarkedAttributes() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        // Create 3 unmarked attributes
        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr2 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr3 = new RuntimeVisibleAnnotationsAttribute();

        recordComponentInfo.attributes = new Attribute[] { attr1, attr2, attr3 };
        recordComponentInfo.u2attributesCount = 3;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - all attributes should be removed since none are marked as used
        assertEquals(0, recordComponentInfo.u2attributesCount,
            "All unmarked attributes should be removed");
        assertNull(recordComponentInfo.attributes[0], "Array slot 0 should be null");
        assertNull(recordComponentInfo.attributes[1], "Array slot 1 should be null");
        assertNull(recordComponentInfo.attributes[2], "Array slot 2 should be null");
    }

    /**
     * Tests that visitRecordComponentInfo keeps only marked (used) attributes.
     * When some attributes are marked as used, they should be retained and compacted
     * to the beginning of the array.
     */
    @Test
    public void testVisitRecordComponentInfo_keepsMarkedAttributes() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        // Create attributes - mark only the second one as used
        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr2 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr3 = new RuntimeVisibleAnnotationsAttribute();

        // Mark only attr2 as used
        attributeUsageMarker.visitAnyAttribute(clazz, attr2);

        recordComponentInfo.attributes = new Attribute[] { attr1, attr2, attr3 };
        recordComponentInfo.u2attributesCount = 3;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - only attr2 should remain, at position 0
        assertEquals(1, recordComponentInfo.u2attributesCount,
            "Only the marked attribute should remain");
        assertSame(attr2, recordComponentInfo.attributes[0],
            "Marked attribute should be compacted to position 0");
        assertNull(recordComponentInfo.attributes[1], "Unused slot should be null");
        assertNull(recordComponentInfo.attributes[2], "Unused slot should be null");
    }

    /**
     * Tests that visitRecordComponentInfo compacts multiple marked attributes to
     * the beginning of the array in their original order.
     */
    @Test
    public void testVisitRecordComponentInfo_compactsMultipleMarkedAttributes() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        // Create 5 attributes - mark 1st, 3rd, and 5th as used
        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr2 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr3 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr4 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr5 = new RuntimeVisibleAnnotationsAttribute();

        // Mark attr1, attr3, and attr5 as used
        attributeUsageMarker.visitAnyAttribute(clazz, attr1);
        attributeUsageMarker.visitAnyAttribute(clazz, attr3);
        attributeUsageMarker.visitAnyAttribute(clazz, attr5);

        recordComponentInfo.attributes = new Attribute[] { attr1, attr2, attr3, attr4, attr5 };
        recordComponentInfo.u2attributesCount = 5;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - marked attributes should be compacted to the front in original order
        assertEquals(3, recordComponentInfo.u2attributesCount,
            "Three marked attributes should remain");
        assertSame(attr1, recordComponentInfo.attributes[0],
            "First marked attribute should be at position 0");
        assertSame(attr3, recordComponentInfo.attributes[1],
            "Second marked attribute should be at position 1");
        assertSame(attr5, recordComponentInfo.attributes[2],
            "Third marked attribute should be at position 2");
        assertNull(recordComponentInfo.attributes[3], "Unused slot should be null");
        assertNull(recordComponentInfo.attributes[4], "Unused slot should be null");
    }

    /**
     * Tests that visitRecordComponentInfo handles an empty attributes array correctly.
     */
    @Test
    public void testVisitRecordComponentInfo_withEmptyAttributesArray() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        recordComponentInfo.attributes = new Attribute[0];
        recordComponentInfo.u2attributesCount = 0;

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo),
            "Empty attributes array should be handled without exception");
        assertEquals(0, recordComponentInfo.u2attributesCount,
            "Empty array should remain empty");
    }

    /**
     * Tests that visitRecordComponentInfo preserves all attributes when all are marked as used.
     */
    @Test
    public void testVisitRecordComponentInfo_allAttributesMarked() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        // Create 3 attributes and mark all as used
        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr2 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr3 = new RuntimeVisibleAnnotationsAttribute();

        attributeUsageMarker.visitAnyAttribute(clazz, attr1);
        attributeUsageMarker.visitAnyAttribute(clazz, attr2);
        attributeUsageMarker.visitAnyAttribute(clazz, attr3);

        recordComponentInfo.attributes = new Attribute[] { attr1, attr2, attr3 };
        recordComponentInfo.u2attributesCount = 3;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - all attributes should remain in their original positions
        assertEquals(3, recordComponentInfo.u2attributesCount,
            "All marked attributes should be preserved");
        assertSame(attr1, recordComponentInfo.attributes[0],
            "First attribute should remain at position 0");
        assertSame(attr2, recordComponentInfo.attributes[1],
            "Second attribute should remain at position 1");
        assertSame(attr3, recordComponentInfo.attributes[2],
            "Third attribute should remain at position 2");
    }

    /**
     * Tests that visitRecordComponentInfo can be called multiple times on the same
     * RecordComponentInfo (idempotent behavior when array is already shrunk).
     */
    @Test
    public void testVisitRecordComponentInfo_idempotentBehavior() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr2 = new RuntimeVisibleAnnotationsAttribute();

        // Mark only attr1 as used
        attributeUsageMarker.visitAnyAttribute(clazz, attr1);

        recordComponentInfo.attributes = new Attribute[] { attr1, attr2 };
        recordComponentInfo.u2attributesCount = 2;

        // Act - call twice
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);
        int countAfterFirst = recordComponentInfo.u2attributesCount;
        Attribute firstAttrAfterFirst = recordComponentInfo.attributes[0];

        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - second call should have no effect since array was already shrunk
        assertEquals(countAfterFirst, recordComponentInfo.u2attributesCount,
            "Count should remain the same after second shrink");
        assertSame(firstAttrAfterFirst, recordComponentInfo.attributes[0],
            "Attribute should remain the same after second shrink");
    }

    /**
     * Tests that visitRecordComponentInfo works with a single attribute that is marked.
     */
    @Test
    public void testVisitRecordComponentInfo_singleMarkedAttribute() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();

        // Mark the attribute as used
        attributeUsageMarker.visitAnyAttribute(clazz, attr1);

        recordComponentInfo.attributes = new Attribute[] { attr1 };
        recordComponentInfo.u2attributesCount = 1;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - single marked attribute should remain
        assertEquals(1, recordComponentInfo.u2attributesCount,
            "Single marked attribute should be preserved");
        assertSame(attr1, recordComponentInfo.attributes[0],
            "Marked attribute should remain at position 0");
    }

    /**
     * Tests that visitRecordComponentInfo works with a single attribute that is unmarked.
     */
    @Test
    public void testVisitRecordComponentInfo_singleUnmarkedAttribute() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();

        recordComponentInfo.attributes = new Attribute[] { attr1 };
        recordComponentInfo.u2attributesCount = 1;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - single unmarked attribute should be removed
        assertEquals(0, recordComponentInfo.u2attributesCount,
            "Single unmarked attribute should be removed");
        assertNull(recordComponentInfo.attributes[0], "Array slot should be null");
    }

    /**
     * Tests that visitRecordComponentInfo does not throw when passed null clazz.
     * The clazz parameter is not actually used in the implementation.
     */
    @Test
    public void testVisitRecordComponentInfo_withNullClazz() {
        // Arrange
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);
        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();

        recordComponentInfo.attributes = new Attribute[] { attr1 };
        recordComponentInfo.u2attributesCount = 1;

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> attributeShrinker.visitRecordComponentInfo(null, recordComponentInfo),
            "Null clazz should be handled gracefully");
        assertEquals(0, recordComponentInfo.u2attributesCount,
            "Unmarked attribute should still be removed with null clazz");
    }

    /**
     * Tests that attributes are cleared (set to null) after shrinking.
     * This is important for garbage collection.
     */
    @Test
    public void testVisitRecordComponentInfo_clearsUnusedSlots() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr2 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr3 = new RuntimeVisibleAnnotationsAttribute();

        // Mark only first attribute
        attributeUsageMarker.visitAnyAttribute(clazz, attr1);

        recordComponentInfo.attributes = new Attribute[] { attr1, attr2, attr3 };
        recordComponentInfo.u2attributesCount = 3;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert - unused slots should be explicitly set to null for garbage collection
        assertEquals(1, recordComponentInfo.u2attributesCount,
            "Only one attribute should remain");
        assertNotNull(recordComponentInfo.attributes[0], "Used slot should not be null");
        assertNull(recordComponentInfo.attributes[1], "Unused slot 1 should be null for GC");
        assertNull(recordComponentInfo.attributes[2], "Unused slot 2 should be null for GC");
    }

    /**
     * Tests shrinking with alternating marked and unmarked attributes.
     */
    @Test
    public void testVisitRecordComponentInfo_alternatingMarkedUnmarked() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass();
        RecordComponentInfo recordComponentInfo = new RecordComponentInfo(0, 0, 0, null);

        Attribute attr1 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr2 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr3 = new RuntimeVisibleAnnotationsAttribute();
        Attribute attr4 = new RuntimeVisibleAnnotationsAttribute();

        // Mark alternating attributes (1st and 3rd)
        attributeUsageMarker.visitAnyAttribute(clazz, attr1);
        attributeUsageMarker.visitAnyAttribute(clazz, attr3);

        recordComponentInfo.attributes = new Attribute[] { attr1, attr2, attr3, attr4 };
        recordComponentInfo.u2attributesCount = 4;

        // Act
        attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

        // Assert
        assertEquals(2, recordComponentInfo.u2attributesCount,
            "Two marked attributes should remain");
        assertSame(attr1, recordComponentInfo.attributes[0],
            "First marked attribute at position 0");
        assertSame(attr3, recordComponentInfo.attributes[1],
            "Second marked attribute at position 1");
        assertNull(recordComponentInfo.attributes[2], "Unused slot should be null");
        assertNull(recordComponentInfo.attributes[3], "Unused slot should be null");
    }

    // Helper method to create a minimal ProgramClass for testing
    private ProgramClass createMinimalProgramClass() {
        return new ProgramClass();
    }
}
