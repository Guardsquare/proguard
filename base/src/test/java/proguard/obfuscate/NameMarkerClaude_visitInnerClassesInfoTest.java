package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.InnerClassesInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/attribute/InnerClassesInfo;)V
 *
 * The visitInnerClassesInfo method ensures that outer class names are kept (not obfuscated)
 * by processing individual inner class entries. The method:
 * 1. Checks if both inner and outer class indices are non-zero
 * 2. Verifies if the inner class name matches the current class name
 * 3. If conditions are met, visits the outer class constant to mark it for preservation
 */
public class NameMarkerClaude_visitInnerClassesInfoTest {

    private NameMarker nameMarker;
    private Clazz mockClazz;
    private InnerClassesInfo mockInnerClassesInfo;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
        mockClazz = mock(ProgramClass.class);
        mockInnerClassesInfo = mock(InnerClassesInfo.class);
    }

    // ========== Tests for visitInnerClassesInfo - Basic Functionality ==========

    /**
     * Tests that visitInnerClassesInfo handles the case where both inner and outer class
     * indices are non-zero and the inner class name matches the current class.
     * This is the main use case where the method should visit the outer class constant.
     */
    @Test
    public void testVisitInnerClassesInfo_withValidIndicesAndMatchingInnerClass() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
        verify(mockClazz, atLeastOnce()).getClassName(1);
    }

    /**
     * Tests that visitInnerClassesInfo does nothing when inner class index is zero.
     * When the inner class index is 0, the method should skip processing.
     */
    @Test
    public void testVisitInnerClassesInfo_withZeroInnerClassIndex() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 0;
        mockInnerClassesInfo.u2outerClassIndex = 1;

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitInnerClassesInfo does nothing when outer class index is zero.
     * When the outer class index is 0, the method should skip processing.
     */
    @Test
    public void testVisitInnerClassesInfo_withZeroOuterClassIndex() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 0;

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitInnerClassesInfo does nothing when both indices are zero.
     */
    @Test
    public void testVisitInnerClassesInfo_withBothIndicesZero() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 0;
        mockInnerClassesInfo.u2outerClassIndex = 0;

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitInnerClassesInfo does nothing when the inner class name does not
     * match the current class name. This happens when processing other inner classes
     * of an outer class.
     */
    @Test
    public void testVisitInnerClassesInfo_withNonMatchingInnerClassName() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String currentClassName = "com/example/Outer$Inner1";
        String innerClassName = "com/example/Outer$Inner2"; // Different

        when(mockClazz.getName()).thenReturn(currentClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    // ========== Tests for Different Class Name Patterns ==========

    /**
     * Tests that visitInnerClassesInfo correctly handles a numeric inner class name.
     * Numeric inner classes are anonymous or local classes like Outer$1, Outer$2, etc.
     */
    @Test
    public void testVisitInnerClassesInfo_withNumericInnerClassName() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$1";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with a multi-digit numeric inner class name.
     */
    @Test
    public void testVisitInnerClassesInfo_withMultiDigitNumericInnerClassName() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$123";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with a named (non-numeric) inner class.
     */
    @Test
    public void testVisitInnerClassesInfo_withNamedInnerClass() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$InnerClass";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    // ========== Tests with Different Class Configurations ==========

    /**
     * Tests with nested inner classes (Outer$Inner$Deep).
     */
    @Test
    public void testVisitInnerClassesInfo_withNestedInnerClass() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner$Deep";
        String outerClassName = "com/example/Outer$Inner";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with a nested numeric inner class (Outer$Inner$1).
     */
    @Test
    public void testVisitInnerClassesInfo_withNestedNumericInnerClass() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner$1";
        String outerClassName = "com/example/Outer$Inner";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with a class in the default package.
     */
    @Test
    public void testVisitInnerClassesInfo_withDefaultPackage() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "Outer$Inner";
        String outerClassName = "Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that the method does not throw when processing valid inputs.
     */
    @Test
    public void testVisitInnerClassesInfo_doesNotThrowException() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn("com/example/Outer");

        // Act & Assert
        assertDoesNotThrow(() -> nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo));
    }

    /**
     * Tests multiple calls to visitInnerClassesInfo with the same parameters.
     */
    @Test
    public void testVisitInnerClassesInfo_calledMultipleTimes() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(3)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with different InnerClassesInfo instances.
     */
    @Test
    public void testVisitInnerClassesInfo_withDifferentInfoInstances() {
        // Arrange
        InnerClassesInfo info1 = mock(InnerClassesInfo.class);
        info1.u2innerClassIndex = 1;
        info1.u2outerClassIndex = 2;

        InnerClassesInfo info2 = mock(InnerClassesInfo.class);
        info2.u2innerClassIndex = 3;
        info2.u2outerClassIndex = 4;

        String className = "com/example/Outer$Inner";
        when(mockClazz.getName()).thenReturn(className);
        when(mockClazz.getClassName(1)).thenReturn(className);
        when(mockClazz.getClassName(2)).thenReturn("com/example/Outer");
        when(mockClazz.getClassName(3)).thenReturn(className);
        when(mockClazz.getClassName(4)).thenReturn("com/example/Outer");

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, info1);
        nameMarker.visitInnerClassesInfo(mockClazz, info2);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(4), eq(nameMarker));
    }

    /**
     * Tests with different Clazz instances.
     */
    @Test
    public void testVisitInnerClassesInfo_withDifferentClazzInstances() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName1 = "com/example/Outer$Inner1";
        String innerClassName2 = "com/example/Outer$Inner2";
        String outerClassName = "com/example/Outer";

        when(clazz1.getName()).thenReturn(innerClassName1);
        when(clazz1.getClassName(1)).thenReturn(innerClassName1);
        when(clazz1.getClassName(2)).thenReturn(outerClassName);

        when(clazz2.getName()).thenReturn(innerClassName2);
        when(clazz2.getClassName(1)).thenReturn(innerClassName2);
        when(clazz2.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(clazz1, mockInnerClassesInfo);
        nameMarker.visitInnerClassesInfo(clazz2, mockInnerClassesInfo);

        // Assert
        verify(clazz1, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
        verify(clazz2, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests that multiple NameMarker instances work independently.
     */
    @Test
    public void testVisitInnerClassesInfo_withMultipleNameMarkers() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();

        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        InnerClassesInfo info1 = mock(InnerClassesInfo.class);
        InnerClassesInfo info2 = mock(InnerClassesInfo.class);

        info1.u2innerClassIndex = 1;
        info1.u2outerClassIndex = 2;
        info2.u2innerClassIndex = 1;
        info2.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(clazz1.getName()).thenReturn(innerClassName);
        when(clazz1.getClassName(1)).thenReturn(innerClassName);
        when(clazz1.getClassName(2)).thenReturn(outerClassName);

        when(clazz2.getName()).thenReturn(innerClassName);
        when(clazz2.getClassName(1)).thenReturn(innerClassName);
        when(clazz2.getClassName(2)).thenReturn(outerClassName);

        // Act
        marker1.visitInnerClassesInfo(clazz1, info1);
        marker2.visitInnerClassesInfo(clazz2, info2);

        // Assert
        verify(clazz1, times(1)).constantPoolEntryAccept(eq(2), eq(marker1));
        verify(clazz2, times(1)).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests with very high index values.
     */
    @Test
    public void testVisitInnerClassesInfo_withHighIndexValues() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 255;
        mockInnerClassesInfo.u2outerClassIndex = 256;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(255)).thenReturn(innerClassName);
        when(mockClazz.getClassName(256)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(256), eq(nameMarker));
    }

    /**
     * Tests that the method works correctly as part of the InnerClassesInfoVisitor interface.
     */
    @Test
    public void testVisitInnerClassesInfo_asInnerClassesInfoVisitor() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act - Call through the InnerClassesInfoVisitor interface
        proguard.classfile.attribute.visitor.InnerClassesInfoVisitor visitor = nameMarker;
        visitor.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with an inner class that has a partially numeric name (e.g., Inner1Class).
     * This should NOT be considered a numeric-only class name.
     */
    @Test
    public void testVisitInnerClassesInfo_withPartiallyNumericInnerClassName() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$1Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with same inner class index and outer class index (edge case).
     */
    @Test
    public void testVisitInnerClassesInfo_withSameInnerAndOuterIndices() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 1; // Same as inner

        String className = "com/example/SomeClass";

        when(mockClazz.getName()).thenReturn(className);
        when(mockClazz.getClassName(1)).thenReturn(className);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert - Should visit the constant since indices are non-zero and names match
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(1), eq(nameMarker));
    }

    /**
     * Tests sequential processing of multiple InnerClassesInfo objects.
     */
    @Test
    public void testVisitInnerClassesInfo_sequentialProcessing() {
        // Arrange
        InnerClassesInfo info1 = mock(InnerClassesInfo.class);
        info1.u2innerClassIndex = 1;
        info1.u2outerClassIndex = 2;

        InnerClassesInfo info2 = mock(InnerClassesInfo.class);
        info2.u2innerClassIndex = 3;
        info2.u2outerClassIndex = 4;

        InnerClassesInfo info3 = mock(InnerClassesInfo.class);
        info3.u2innerClassIndex = 5;
        info3.u2outerClassIndex = 6;

        String className = "com/example/Outer$Inner";
        when(mockClazz.getName()).thenReturn(className);
        when(mockClazz.getClassName(1)).thenReturn(className);
        when(mockClazz.getClassName(2)).thenReturn("com/example/Outer");
        when(mockClazz.getClassName(3)).thenReturn(className);
        when(mockClazz.getClassName(4)).thenReturn("com/example/Outer");
        when(mockClazz.getClassName(5)).thenReturn(className);
        when(mockClazz.getClassName(6)).thenReturn("com/example/Outer");

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, info1);
        nameMarker.visitInnerClassesInfo(mockClazz, info2);
        nameMarker.visitInnerClassesInfo(mockClazz, info3);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(4), eq(nameMarker));
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(6), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesInfo passes the correct NameMarker instance (itself)
     * as the visitor when calling constantPoolEntryAccept.
     */
    @Test
    public void testVisitInnerClassesInfo_passesCorrectVisitor() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert - Verify the nameMarker instance itself is passed as the visitor
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), same(nameMarker));
    }

    /**
     * Tests that visitInnerClassesInfo correctly checks the condition before
     * calling constantPoolEntryAccept.
     */
    @Test
    public void testVisitInnerClassesInfo_onlyCallsConstantPoolEntryAcceptWhenConditionsMet() {
        // Arrange - Set up a scenario where only one condition fails
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String currentClassName = "com/example/Outer$Inner1";
        String innerClassIndexName = "com/example/Outer$Inner2"; // Different

        when(mockClazz.getName()).thenReturn(currentClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassIndexName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert - Should NOT call constantPoolEntryAccept
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());
        verify(mockClazz, atLeastOnce()).getName();
        verify(mockClazz, atLeastOnce()).getClassName(1);
    }

    /**
     * Tests that visitInnerClassesInfo accesses the correct fields of InnerClassesInfo.
     */
    @Test
    public void testVisitInnerClassesInfo_accessesCorrectInfoFields() {
        // Arrange
        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 10;
        info.u2outerClassIndex = 20;

        String innerClassName = "com/example/Test$Inner";
        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(10)).thenReturn(innerClassName);
        when(mockClazz.getClassName(20)).thenReturn("com/example/Test");

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, info);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(20), eq(nameMarker));
        verify(mockClazz, times(1)).getClassName(10);
    }

    /**
     * Tests rapid sequential calls with different parameter combinations.
     */
    @Test
    public void testVisitInnerClassesInfo_rapidSequentialCalls() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act - Rapid calls
        for (int i = 0; i < 50; i++) {
            nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);
        }

        // Assert
        verify(mockClazz, times(50)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests with an inner class that uses special characters (e.g., lambda classes).
     */
    @Test
    public void testVisitInnerClassesInfo_withSpecialCharactersInClassName() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$$Lambda$1";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests that the method correctly handles the equals comparison for class names.
     */
    @Test
    public void testVisitInnerClassesInfo_classNameEqualsComparison() {
        // Arrange - Use two different String objects with same content
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName1 = new String("com/example/Outer$Inner");
        String innerClassName2 = new String("com/example/Outer$Inner");
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName1);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName2);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert - Should match because of equals comparison
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests visitInnerClassesInfo with both ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitInnerClassesInfo_withDifferentClazzTypes() {
        // Arrange
        Clazz programClazz = mock(ProgramClass.class);
        Clazz libraryClazz = mock(proguard.classfile.LibraryClass.class);

        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(programClazz.getName()).thenReturn(innerClassName);
        when(programClazz.getClassName(1)).thenReturn(innerClassName);
        when(programClazz.getClassName(2)).thenReturn(outerClassName);

        when(libraryClazz.getName()).thenReturn(innerClassName);
        when(libraryClazz.getClassName(1)).thenReturn(innerClassName);
        when(libraryClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(programClazz, mockInnerClassesInfo);
        nameMarker.visitInnerClassesInfo(libraryClazz, mockInnerClassesInfo);

        // Assert
        verify(programClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
        verify(libraryClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesInfo ensures outer class names are kept by
     * delegating to constantPoolEntryAccept which triggers the ConstantVisitor chain.
     */
    @Test
    public void testVisitInnerClassesInfo_ensuresOuterClassNameIsKept() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/MyClass$InnerClass";
        String outerClassName = "com/example/MyClass";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert - The method should call constantPoolEntryAccept with the outer class index
        // This will eventually lead to visitClassConstant and then keepClassName being called
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesInfo works when index 1 is used for both inner and outer.
     */
    @Test
    public void testVisitInnerClassesInfo_withMinimalNonZeroIndices() {
        // Arrange
        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 1;

        String className = "com/example/Test";

        when(mockClazz.getName()).thenReturn(className);
        when(mockClazz.getClassName(1)).thenReturn(className);

        // Act
        nameMarker.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(1), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesInfo correctly evaluates all three conditions
     * in the if statement before proceeding.
     */
    @Test
    public void testVisitInnerClassesInfo_evaluatesAllConditions() {
        // Arrange - Test each condition independently

        // Test 1: innerClassIndex == 0
        InnerClassesInfo info1 = mock(InnerClassesInfo.class);
        info1.u2innerClassIndex = 0;
        info1.u2outerClassIndex = 1;
        nameMarker.visitInnerClassesInfo(mockClazz, info1);
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());

        // Test 2: outerClassIndex == 0
        InnerClassesInfo info2 = mock(InnerClassesInfo.class);
        info2.u2innerClassIndex = 1;
        info2.u2outerClassIndex = 0;
        nameMarker.visitInnerClassesInfo(mockClazz, info2);
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());

        // Test 3: Class names don't match
        InnerClassesInfo info3 = mock(InnerClassesInfo.class);
        info3.u2innerClassIndex = 1;
        info3.u2outerClassIndex = 2;
        when(mockClazz.getName()).thenReturn("com/example/Test1");
        when(mockClazz.getClassName(1)).thenReturn("com/example/Test2");
        nameMarker.visitInnerClassesInfo(mockClazz, info3);
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());
    }
}
