package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.InnerClassesInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassObfuscator#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
 *
 * This class tests the visitInnerClassesInfo method which processes individual inner class
 * entries within an InnerClassesAttribute. The method:
 * 1. Checks if both inner and outer class indices are non-zero
 * 2. Verifies if the inner class name matches the current class name
 * 3. Visits the outer class constant to ensure it has an obfuscated name
 * 4. Determines if the inner class has a numeric name (e.g., OuterClass$1)
 */
public class ClassObfuscatorClaude_visitInnerClassesInfoTest {

    private ClassObfuscator classObfuscator;
    private Clazz mockClazz;
    private InnerClassesInfo mockInnerClassesInfo;

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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
        verify(mockClazz, atLeastOnce()).getClassName(1);
        verify(mockClazz, atLeastOnce()).getClassName(2);
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    // ========== Tests for Numeric Class Name Detection ==========

    /**
     * Tests that visitInnerClassesInfo correctly identifies a numeric inner class name.
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
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
        assertDoesNotThrow(() -> classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(3)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, info1);
        classObfuscator.visitInnerClassesInfo(mockClazz, info2);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(4), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(clazz1, mockInnerClassesInfo);
        classObfuscator.visitInnerClassesInfo(clazz2, mockInnerClassesInfo);

        // Assert
        verify(clazz1, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
        verify(clazz2, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
    }

    /**
     * Tests that multiple ClassObfuscator instances work independently.
     */
    @Test
    public void testVisitInnerClassesInfo_withMultipleObfuscators() {
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
        obfuscator1.visitInnerClassesInfo(clazz1, info1);
        obfuscator2.visitInnerClassesInfo(clazz2, info2);

        // Assert
        verify(clazz1, times(1)).constantPoolEntryAccept(eq(2), eq(obfuscator1));
        verify(clazz2, times(1)).constantPoolEntryAccept(eq(2), eq(obfuscator2));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(256), eq(classObfuscator));
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
        proguard.classfile.attribute.visitor.InnerClassesInfoVisitor visitor = classObfuscator;
        visitor.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
    }

    /**
     * Tests with an inner class that has a partially numeric name (e.g., Inner1Class).
     * This should NOT be considered a numeric class name.
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
    }

    /**
     * Tests with different ClassObfuscator configurations to ensure behavior is consistent.
     */
    @Test
    public void testVisitInnerClassesInfo_withDifferentConfigurations() {
        // Arrange - Create obfuscator with different configuration
        ClassObfuscator obfuscator = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, false, null, null, null, true, true
        );

        mockInnerClassesInfo.u2innerClassIndex = 1;
        mockInnerClassesInfo.u2outerClassIndex = 2;

        String innerClassName = "com/example/Outer$Inner";
        String outerClassName = "com/example/Outer";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getClassName(1)).thenReturn(innerClassName);
        when(mockClazz.getClassName(2)).thenReturn(outerClassName);

        // Act
        obfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(obfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, mockInnerClassesInfo);

        // Assert - Should visit the constant since indices are non-zero and names match
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(1), eq(classObfuscator));
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
        classObfuscator.visitInnerClassesInfo(mockClazz, info1);
        classObfuscator.visitInnerClassesInfo(mockClazz, info2);
        classObfuscator.visitInnerClassesInfo(mockClazz, info3);

        // Assert
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(2), eq(classObfuscator));
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(4), eq(classObfuscator));
        verify(mockClazz, times(1)).constantPoolEntryAccept(eq(6), eq(classObfuscator));
    }
}
