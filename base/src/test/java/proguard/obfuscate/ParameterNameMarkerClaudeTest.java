package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.attribute.LocalVariableTypeInfo;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ParameterNameMarker}.
 *
 * This class tests the ParameterNameMarker which trims and marks local variable (type) table
 * attributes by keeping parameter names and types (entries starting at offset 0) and removing
 * ordinary local variable names and types.
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyAttribute behavior (should do nothing)
 * - visitLocalVariableTableAttribute trimming and marking logic
 * - visitLocalVariableTypeTableAttribute trimming and marking logic
 */
public class ParameterNameMarkerClaudeTest {

    private ParameterNameMarker parameterNameMarker;
    private AttributeVisitor mockAttributeUsageMarker;
    private Clazz mockClazz;
    private Method mockMethod;
    private CodeAttribute mockCodeAttribute;

    @BeforeEach
    public void setUp() {
        mockAttributeUsageMarker = mock(AttributeVisitor.class);
        parameterNameMarker = new ParameterNameMarker(mockAttributeUsageMarker);
        mockClazz = mock(Clazz.class);
        mockMethod = mock(Method.class);
        mockCodeAttribute = mock(CodeAttribute.class);
    }

    // Tests for constructor: <init>.(Lproguard/classfile/attribute/visitor/AttributeVisitor;)V

    /**
     * Tests that the constructor successfully creates a ParameterNameMarker instance
     * with the given AttributeVisitor.
     */
    @Test
    public void testConstructor_createsValidInstance() {
        // Act
        ParameterNameMarker marker = new ParameterNameMarker(mockAttributeUsageMarker);

        // Assert
        assertNotNull(marker, "Constructor should create a non-null instance");
    }

    /**
     * Tests that constructor accepts null AttributeVisitor.
     * This tests if the constructor handles null gracefully.
     */
    @Test
    public void testConstructor_acceptsNullAttributeVisitor() {
        // Act & Assert
        assertDoesNotThrow(() -> new ParameterNameMarker(null),
            "Constructor should accept null AttributeVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleIndependentInstances() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);

        // Act
        ParameterNameMarker marker1 = new ParameterNameMarker(visitor1);
        ParameterNameMarker marker2 = new ParameterNameMarker(visitor2);

        // Assert
        assertNotNull(marker1);
        assertNotNull(marker2);
        assertNotSame(marker1, marker2, "Each constructor call should create a distinct instance");
    }

    // Tests for visitAnyAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/attribute/Attribute;)V

    /**
     * Tests that visitAnyAttribute does nothing (no-op).
     * This method is implemented but has an empty body.
     */
    @Test
    public void testVisitAnyAttribute_doesNothing() {
        // Arrange
        Attribute mockAttribute = mock(Attribute.class);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> parameterNameMarker.visitAnyAttribute(mockClazz, mockAttribute),
            "visitAnyAttribute should execute without throwing exceptions");

        // Verify no interactions with the mock objects
        verifyNoInteractions(mockAttributeUsageMarker);
        verifyNoInteractions(mockAttribute);
    }

    /**
     * Tests that visitAnyAttribute handles null parameters gracefully.
     */
    @Test
    public void testVisitAnyAttribute_withNullParameters() {
        // Act & Assert
        assertDoesNotThrow(() -> parameterNameMarker.visitAnyAttribute(null, null),
            "visitAnyAttribute should handle null parameters");
    }

    // Tests for visitLocalVariableTableAttribute
    // Note: We cannot directly test the "already marked" branch because AttributeUsageMarker.isUsed()
    // checks for a private static final USED object that cannot be mocked without PowerMock.
    // The implementation will short-circuit if the attribute is already marked, but we test
    // the normal processing path where attributes are not yet marked.

    /**
     * Tests that visitLocalVariableTableAttribute does nothing when the method
     * has no parameters (descriptor starts with "()").
     */
    @Test
    public void testVisitLocalVariableTableAttribute_methodHasNoParameters_doesNothing() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);
        when(attribute.getProcessingInfo()).thenReturn(null); // Not marked as used
        when(mockMethod.getDescriptor(mockClazz)).thenReturn("()V"); // No parameters

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert - marker should not be called
        verifyNoInteractions(mockAttributeUsageMarker);
    }

    /**
     * Tests that visitLocalVariableTableAttribute trims the table by keeping only
     * entries that start at offset 0 (parameters).
     */
    @Test
    public void testVisitLocalVariableTableAttribute_trimsNonParameterEntries() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 4;
        attribute.localVariableTable = new LocalVariableInfo[4];

        // Create local variable entries: 2 at startPC=0 (parameters), 2 at startPC>0 (local vars)
        LocalVariableInfo param1 = new LocalVariableInfo();
        param1.u2startPC = 0;

        LocalVariableInfo param2 = new LocalVariableInfo();
        param2.u2startPC = 0;

        LocalVariableInfo local1 = new LocalVariableInfo();
        local1.u2startPC = 5;

        LocalVariableInfo local2 = new LocalVariableInfo();
        local2.u2startPC = 10;

        // Mix parameters and local variables in the table
        attribute.localVariableTable[0] = param1;
        attribute.localVariableTable[1] = local1;
        attribute.localVariableTable[2] = param2;
        attribute.localVariableTable[3] = local2;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(II)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(2, attribute.u2localVariableTableLength,
            "Table should be trimmed to only parameter entries");
        assertEquals(param1, attribute.localVariableTable[0],
            "First parameter should be at index 0");
        assertEquals(param2, attribute.localVariableTable[1],
            "Second parameter should be at index 1");

        // Verify the marker was called
        verify(mockAttributeUsageMarker).visitLocalVariableTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests that visitLocalVariableTableAttribute does not mark the attribute
     * when all entries are filtered out (no parameters at startPC=0).
     */
    @Test
    public void testVisitLocalVariableTableAttribute_allEntriesFiltered_doesNotMark() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 2;
        attribute.localVariableTable = new LocalVariableInfo[2];

        // Create only local variables (no parameters)
        LocalVariableInfo local1 = new LocalVariableInfo();
        local1.u2startPC = 5;

        LocalVariableInfo local2 = new LocalVariableInfo();
        local2.u2startPC = 10;

        attribute.localVariableTable[0] = local1;
        attribute.localVariableTable[1] = local2;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(I)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(0, attribute.u2localVariableTableLength,
            "Table should be empty when no parameter entries found");

        // Verify the marker was NOT called since there are no entries
        verifyNoInteractions(mockAttributeUsageMarker);
    }

    /**
     * Tests that visitLocalVariableTableAttribute marks the attribute when
     * there's at least one parameter entry.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withParameterEntries_marksAttribute() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 1;
        attribute.localVariableTable = new LocalVariableInfo[1];

        LocalVariableInfo param = new LocalVariableInfo();
        param.u2startPC = 0;
        attribute.localVariableTable[0] = param;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(I)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(1, attribute.u2localVariableTableLength,
            "Table should have one parameter entry");
        verify(mockAttributeUsageMarker).visitLocalVariableTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests that visitLocalVariableTableAttribute handles an empty table.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_emptyTable() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = new LocalVariableInfo[0];

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(I)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(0, attribute.u2localVariableTableLength, "Table should remain empty");
        verifyNoInteractions(mockAttributeUsageMarker);
    }

    /**
     * Tests that visitLocalVariableTableAttribute preserves all parameter entries
     * when all entries are at startPC=0.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_allEntriesAreParameters() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 3;
        attribute.localVariableTable = new LocalVariableInfo[3];

        LocalVariableInfo param1 = new LocalVariableInfo();
        param1.u2startPC = 0;

        LocalVariableInfo param2 = new LocalVariableInfo();
        param2.u2startPC = 0;

        LocalVariableInfo param3 = new LocalVariableInfo();
        param3.u2startPC = 0;

        attribute.localVariableTable[0] = param1;
        attribute.localVariableTable[1] = param2;
        attribute.localVariableTable[2] = param3;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(III)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(3, attribute.u2localVariableTableLength,
            "All parameter entries should be preserved");
        verify(mockAttributeUsageMarker).visitLocalVariableTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    // Tests for visitLocalVariableTypeTableAttribute

    /**
     * Tests that visitLocalVariableTypeTableAttribute does nothing when the method
     * has no parameters.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_methodHasNoParameters_doesNothing() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);
        when(attribute.getProcessingInfo()).thenReturn(null); // Not marked as used
        when(mockMethod.getDescriptor(mockClazz)).thenReturn("()Ljava/lang/String;"); // No parameters

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert - marker should not be called
        verifyNoInteractions(mockAttributeUsageMarker);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute trims the table by keeping only
     * entries that start at offset 0 (parameters).
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_trimsNonParameterEntries() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 4;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[4];

        // Create local variable type entries: 2 at startPC=0 (parameters), 2 at startPC>0 (local vars)
        LocalVariableTypeInfo param1 = new LocalVariableTypeInfo();
        param1.u2startPC = 0;

        LocalVariableTypeInfo param2 = new LocalVariableTypeInfo();
        param2.u2startPC = 0;

        LocalVariableTypeInfo local1 = new LocalVariableTypeInfo();
        local1.u2startPC = 5;

        LocalVariableTypeInfo local2 = new LocalVariableTypeInfo();
        local2.u2startPC = 10;

        // Mix parameters and local variables in the table
        attribute.localVariableTypeTable[0] = param1;
        attribute.localVariableTypeTable[1] = local1;
        attribute.localVariableTypeTable[2] = param2;
        attribute.localVariableTypeTable[3] = local2;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(Ljava/util/List;Ljava/lang/String;)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(2, attribute.u2localVariableTypeTableLength,
            "Table should be trimmed to only parameter entries");
        assertEquals(param1, attribute.localVariableTypeTable[0],
            "First parameter should be at index 0");
        assertEquals(param2, attribute.localVariableTypeTable[1],
            "Second parameter should be at index 1");

        // Verify the marker was called
        verify(mockAttributeUsageMarker).visitLocalVariableTypeTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute does not mark the attribute
     * when all entries are filtered out.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_allEntriesFiltered_doesNotMark() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 2;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[2];

        // Create only local variables (no parameters)
        LocalVariableTypeInfo local1 = new LocalVariableTypeInfo();
        local1.u2startPC = 5;

        LocalVariableTypeInfo local2 = new LocalVariableTypeInfo();
        local2.u2startPC = 10;

        attribute.localVariableTypeTable[0] = local1;
        attribute.localVariableTypeTable[1] = local2;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(Ljava/util/List;)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(0, attribute.u2localVariableTypeTableLength,
            "Table should be empty when no parameter entries found");

        // Verify the marker was NOT called since there are no entries
        verifyNoInteractions(mockAttributeUsageMarker);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute marks the attribute when
     * there's at least one parameter entry.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withParameterEntries_marksAttribute() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 1;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[1];

        LocalVariableTypeInfo param = new LocalVariableTypeInfo();
        param.u2startPC = 0;
        attribute.localVariableTypeTable[0] = param;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(Ljava/util/List;)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(1, attribute.u2localVariableTypeTableLength,
            "Table should have one parameter entry");
        verify(mockAttributeUsageMarker).visitLocalVariableTypeTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute handles an empty table.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_emptyTable() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[0];

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(Ljava/util/List;)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(0, attribute.u2localVariableTypeTableLength, "Table should remain empty");
        verifyNoInteractions(mockAttributeUsageMarker);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute preserves all parameter entries
     * when all entries are at startPC=0.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_allEntriesAreParameters() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 3;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[3];

        LocalVariableTypeInfo param1 = new LocalVariableTypeInfo();
        param1.u2startPC = 0;

        LocalVariableTypeInfo param2 = new LocalVariableTypeInfo();
        param2.u2startPC = 0;

        LocalVariableTypeInfo param3 = new LocalVariableTypeInfo();
        param3.u2startPC = 0;

        attribute.localVariableTypeTable[0] = param1;
        attribute.localVariableTypeTable[1] = param2;
        attribute.localVariableTypeTable[2] = param3;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(Ljava/util/List;Ljava/lang/String;Ljava/lang/Integer;)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(3, attribute.u2localVariableTypeTableLength,
            "All parameter entries should be preserved");
        verify(mockAttributeUsageMarker).visitLocalVariableTypeTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute handles method with Object array parameter.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withArrayParameter() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 1;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[1];

        LocalVariableTypeInfo param = new LocalVariableTypeInfo();
        param.u2startPC = 0;
        attribute.localVariableTypeTable[0] = param;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("([Ljava/lang/Object;)V"); // Array parameter

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(1, attribute.u2localVariableTypeTableLength,
            "Table should have one parameter entry");
        verify(mockAttributeUsageMarker).visitLocalVariableTypeTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute handles method with primitive parameters.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withPrimitiveParameters() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 2;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[2];

        LocalVariableTypeInfo param1 = new LocalVariableTypeInfo();
        param1.u2startPC = 0;

        LocalVariableTypeInfo param2 = new LocalVariableTypeInfo();
        param2.u2startPC = 0;

        attribute.localVariableTypeTable[0] = param1;
        attribute.localVariableTypeTable[1] = param2;

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(IJ)V"); // int and long parameters

        // Act
        parameterNameMarker.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(2, attribute.u2localVariableTypeTableLength,
            "Table should have two parameter entries");
        verify(mockAttributeUsageMarker).visitLocalVariableTypeTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests the edge case where the attribute table has been partially processed
     * (some entries at the end are null).
     */
    @Test
    public void testVisitLocalVariableTableAttribute_partiallyFilledTable() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 2;
        attribute.localVariableTable = new LocalVariableInfo[5]; // Larger array

        LocalVariableInfo param = new LocalVariableInfo();
        param.u2startPC = 0;

        LocalVariableInfo local = new LocalVariableInfo();
        local.u2startPC = 5;

        attribute.localVariableTable[0] = param;
        attribute.localVariableTable[1] = local;
        // Entries 2-4 are null

        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(I)V"); // Has parameters

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert
        assertEquals(1, attribute.u2localVariableTableLength,
            "Table should be trimmed to only the parameter entry");
        assertEquals(param, attribute.localVariableTable[0],
            "Parameter should be at index 0");
        verify(mockAttributeUsageMarker).visitLocalVariableTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests constructor with a specific implementation of AttributeVisitor.
     */
    @Test
    public void testConstructor_withAttributeUsageMarker() {
        // Arrange
        AttributeUsageMarker usageMarker = new AttributeUsageMarker();

        // Act
        ParameterNameMarker marker = new ParameterNameMarker(usageMarker);

        // Assert
        assertNotNull(marker, "Constructor should create instance with AttributeUsageMarker");
    }

    /**
     * Tests that the marker correctly identifies methods with parameters vs without.
     * This is an indirect test of the private hasParameters method.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_methodWithSingleParameter() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 1;
        attribute.localVariableTable = new LocalVariableInfo[1];

        LocalVariableInfo param = new LocalVariableInfo();
        param.u2startPC = 0;
        attribute.localVariableTable[0] = param;

        // Test with a single primitive parameter
        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(I)V");

        // Act
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attribute);

        // Assert - marker should be called because method has parameters
        verify(mockAttributeUsageMarker).visitLocalVariableTableAttribute(
            mockClazz, mockMethod, mockCodeAttribute, attribute);
    }

    /**
     * Tests behavior with multiple descriptor formats to ensure hasParameters
     * logic works correctly.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_variousDescriptorFormats() {
        // Test 1: Multiple primitive parameters
        LocalVariableTableAttribute attr1 = createAttributeWithParameterEntry();
        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(IJZ)V");
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attr1);
        verify(mockAttributeUsageMarker, times(1)).visitLocalVariableTableAttribute(any(), any(), any(), any());

        // Test 2: Object parameter
        reset(mockAttributeUsageMarker);
        LocalVariableTableAttribute attr2 = createAttributeWithParameterEntry();
        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(Ljava/lang/String;)V");
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attr2);
        verify(mockAttributeUsageMarker, times(1)).visitLocalVariableTableAttribute(any(), any(), any(), any());

        // Test 3: Mixed parameters
        reset(mockAttributeUsageMarker);
        LocalVariableTableAttribute attr3 = createAttributeWithParameterEntry();
        when(mockMethod.getDescriptor(mockClazz)).thenReturn("(ILjava/lang/String;Z)V");
        parameterNameMarker.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, attr3);
        verify(mockAttributeUsageMarker, times(1)).visitLocalVariableTableAttribute(any(), any(), any(), any());
    }

    /**
     * Helper method to create a LocalVariableTableAttribute with a single parameter entry.
     */
    private LocalVariableTableAttribute createAttributeWithParameterEntry() {
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 1;
        attribute.localVariableTable = new LocalVariableInfo[1];

        LocalVariableInfo param = new LocalVariableInfo();
        param.u2startPC = 0;
        attribute.localVariableTable[0] = param;

        return attribute;
    }
}
