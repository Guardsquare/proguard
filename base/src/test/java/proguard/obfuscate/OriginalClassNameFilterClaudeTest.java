package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.visitor.ClassVisitor;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link OriginalClassNameFilter}.
 *
 * This class tests the OriginalClassNameFilter, which is a ClassVisitor that delegates
 * visits to one of two other ClassVisitors based on whether the visited class still has
 * its original name or not.
 *
 * The class under test:
 * - Constructor takes two ClassVisitor parameters: acceptedClassVisitor (for classes with
 *   original names) and rejectedClassVisitor (for classes with changed names)
 * - visitAnyClass method uses ClassObfuscator.hasOriginalClassName to determine which
 *   visitor to delegate to
 * - If the delegate visitor is not null, it calls clazz.accept(delegateVisitor)
 */
public class OriginalClassNameFilterClaudeTest {

    // ========== Tests for Constructor ==========

    /**
     * Tests that the constructor accepts two non-null ClassVisitor instances.
     */
    @Test
    public void testConstructor_withTwoNonNullVisitors() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);

        // Act
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        // Assert
        // Constructor should complete without throwing exceptions
        // We verify behavior in visitAnyClass tests
    }

    /**
     * Tests that the constructor accepts null for acceptedClassVisitor.
     */
    @Test
    public void testConstructor_withNullAcceptedVisitor() {
        // Arrange
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);

        // Act
        OriginalClassNameFilter filter = new OriginalClassNameFilter(null, rejectedVisitor);

        // Assert
        // Constructor should complete without throwing exceptions
    }

    /**
     * Tests that the constructor accepts null for rejectedClassVisitor.
     */
    @Test
    public void testConstructor_withNullRejectedVisitor() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);

        // Act
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, null);

        // Assert
        // Constructor should complete without throwing exceptions
    }

    /**
     * Tests that the constructor accepts both visitors as null.
     */
    @Test
    public void testConstructor_withBothVisitorsNull() {
        // Act
        OriginalClassNameFilter filter = new OriginalClassNameFilter(null, null);

        // Assert
        // Constructor should complete without throwing exceptions
    }

    // ========== Tests for visitAnyClass - Class with Original Name ==========

    /**
     * Tests that visitAnyClass delegates to acceptedClassVisitor when the class
     * has its original name.
     */
    @Test
    public void testVisitAnyClass_delegatesToAcceptedVisitor_whenClassHasOriginalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";

        // Setup: Class has original name (getName equals processingInfo)
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(originalName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(acceptedVisitor);
        verify(mockClazz, never()).accept(rejectedVisitor);
    }

    /**
     * Tests that visitAnyClass does not call any visitor when acceptedClassVisitor
     * is null and the class has its original name.
     */
    @Test
    public void testVisitAnyClass_doesNotDelegate_whenAcceptedVisitorIsNull() {
        // Arrange
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(null, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";

        // Setup: Class has original name
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(originalName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz, never()).accept(any(ClassVisitor.class));
    }

    // ========== Tests for visitAnyClass - Class without Original Name ==========

    /**
     * Tests that visitAnyClass delegates to rejectedClassVisitor when the class
     * has been renamed.
     */
    @Test
    public void testVisitAnyClass_delegatesToRejectedVisitor_whenClassIsRenamed() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        // Setup: Class has different name (has been renamed)
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
        verify(mockClazz, never()).accept(acceptedVisitor);
    }

    /**
     * Tests that visitAnyClass delegates to rejectedClassVisitor when the class
     * has no new name set (processingInfo is null).
     */
    @Test
    public void testVisitAnyClass_delegatesToRejectedVisitor_whenNoNewNameSet() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";

        // Setup: Class has no new name set (processingInfo is null)
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(null);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
        verify(mockClazz, never()).accept(acceptedVisitor);
    }

    /**
     * Tests that visitAnyClass does not call any visitor when rejectedClassVisitor
     * is null and the class has been renamed.
     */
    @Test
    public void testVisitAnyClass_doesNotDelegate_whenRejectedVisitorIsNull() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, null);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        // Setup: Class has been renamed
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz, never()).accept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyClass delegates to rejectedClassVisitor when the class
     * has non-String processingInfo.
     */
    @Test
    public void testVisitAnyClass_delegatesToRejectedVisitor_whenProcessingInfoIsNotString() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        Object nonStringInfo = new Object();

        // Setup: Class has non-String processingInfo
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(nonStringInfo);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
        verify(mockClazz, never()).accept(acceptedVisitor);
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that visitAnyClass does nothing when both visitors are null
     * and the class has its original name.
     */
    @Test
    public void testVisitAnyClass_doesNothing_whenBothVisitorsNullAndOriginalName() {
        // Arrange
        OriginalClassNameFilter filter = new OriginalClassNameFilter(null, null);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";

        // Setup: Class has original name
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(originalName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz, never()).accept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyClass does nothing when both visitors are null
     * and the class has been renamed.
     */
    @Test
    public void testVisitAnyClass_doesNothing_whenBothVisitorsNullAndRenamed() {
        // Arrange
        OriginalClassNameFilter filter = new OriginalClassNameFilter(null, null);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        // Setup: Class has been renamed
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz, never()).accept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyClass can be called multiple times with the same result
     * for a class with original name.
     */
    @Test
    public void testVisitAnyClass_multipleCallsConsistent_withOriginalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(originalName);

        // Act
        filter.visitAnyClass(mockClazz);
        filter.visitAnyClass(mockClazz);
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz, times(3)).accept(acceptedVisitor);
        verify(mockClazz, never()).accept(rejectedVisitor);
    }

    /**
     * Tests that visitAnyClass can be called multiple times with the same result
     * for a class without original name.
     */
    @Test
    public void testVisitAnyClass_multipleCallsConsistent_withRenamedClass() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);
        filter.visitAnyClass(mockClazz);
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz, times(3)).accept(rejectedVisitor);
        verify(mockClazz, never()).accept(acceptedVisitor);
    }

    /**
     * Tests that visitAnyClass handles different classes with different name states
     * correctly in sequence.
     */
    @Test
    public void testVisitAnyClass_handlesDifferentClasses_withMixedNameStates() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        // Class 1: Has original name
        Clazz clazz1 = mock(Clazz.class);
        when(clazz1.getName()).thenReturn("com/example/Class1");
        when(clazz1.getProcessingInfo()).thenReturn("com/example/Class1");

        // Class 2: Has been renamed
        Clazz clazz2 = mock(Clazz.class);
        when(clazz2.getName()).thenReturn("com/example/Class2");
        when(clazz2.getProcessingInfo()).thenReturn("com/example/a");

        // Class 3: No new name set
        Clazz clazz3 = mock(Clazz.class);
        when(clazz3.getName()).thenReturn("com/example/Class3");
        when(clazz3.getProcessingInfo()).thenReturn(null);

        // Class 4: Has original name
        Clazz clazz4 = mock(Clazz.class);
        when(clazz4.getName()).thenReturn("com/example/Class4");
        when(clazz4.getProcessingInfo()).thenReturn("com/example/Class4");

        // Act
        filter.visitAnyClass(clazz1);
        filter.visitAnyClass(clazz2);
        filter.visitAnyClass(clazz3);
        filter.visitAnyClass(clazz4);

        // Assert
        verify(clazz1).accept(acceptedVisitor);
        verify(clazz2).accept(rejectedVisitor);
        verify(clazz3).accept(rejectedVisitor);
        verify(clazz4).accept(acceptedVisitor);

        // Verify no cross-contamination
        verify(clazz1, never()).accept(rejectedVisitor);
        verify(clazz2, never()).accept(acceptedVisitor);
        verify(clazz3, never()).accept(acceptedVisitor);
        verify(clazz4, never()).accept(rejectedVisitor);
    }

    // ========== Tests for Different Class Name Scenarios ==========

    /**
     * Tests filtering with simple class names.
     */
    @Test
    public void testVisitAnyClass_withSimpleClassName_originalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String simpleName = "TestClass";

        when(mockClazz.getName()).thenReturn(simpleName);
        when(mockClazz.getProcessingInfo()).thenReturn(simpleName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(acceptedVisitor);
    }

    /**
     * Tests filtering with fully qualified class names.
     */
    @Test
    public void testVisitAnyClass_withFullyQualifiedName_originalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String fqName = "com/example/foo/bar/TestClass";

        when(mockClazz.getName()).thenReturn(fqName);
        when(mockClazz.getProcessingInfo()).thenReturn(fqName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(acceptedVisitor);
    }

    /**
     * Tests filtering with inner class names.
     */
    @Test
    public void testVisitAnyClass_withInnerClassName_originalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String innerClassName = "com/example/OuterClass$InnerClass";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getProcessingInfo()).thenReturn(innerClassName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(acceptedVisitor);
    }

    /**
     * Tests filtering when inner class is renamed.
     */
    @Test
    public void testVisitAnyClass_withInnerClassName_renamed() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/OuterClass$InnerClass";
        String newName = "com/example/a$b";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
    }

    /**
     * Tests filtering with numeric inner class names.
     */
    @Test
    public void testVisitAnyClass_withNumericInnerClass_originalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String numericInnerClass = "com/example/OuterClass$1";

        when(mockClazz.getName()).thenReturn(numericInnerClass);
        when(mockClazz.getProcessingInfo()).thenReturn(numericInnerClass);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(acceptedVisitor);
    }

    /**
     * Tests filtering when package name changes but class name stays same.
     */
    @Test
    public void testVisitAnyClass_packageNameChanged_classNameSame() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/obfuscated/TestClass";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
    }

    /**
     * Tests filtering when class name changes but package stays same.
     */
    @Test
    public void testVisitAnyClass_classNameChanged_packageSame() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
    }

    /**
     * Tests filtering with empty string class name.
     */
    @Test
    public void testVisitAnyClass_withEmptyString_originalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String emptyName = "";

        when(mockClazz.getName()).thenReturn(emptyName);
        when(mockClazz.getProcessingInfo()).thenReturn(emptyName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(acceptedVisitor);
    }

    /**
     * Tests that filtering is case-sensitive.
     */
    @Test
    public void testVisitAnyClass_caseSensitive() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/testclass";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
    }

    /**
     * Tests filtering with single character class name.
     */
    @Test
    public void testVisitAnyClass_withSingleCharacter_originalName() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String singleChar = "a";

        when(mockClazz.getName()).thenReturn(singleChar);
        when(mockClazz.getProcessingInfo()).thenReturn(singleChar);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(acceptedVisitor);
    }

    /**
     * Tests filtering when names differ by whitespace.
     */
    @Test
    public void testVisitAnyClass_withWhitespaceDifference() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newNameWithSpace = "com/example/TestClass ";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newNameWithSpace);

        // Act
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz).accept(rejectedVisitor);
    }

    // ========== Tests for Same Filter Instance with Different Classes ==========

    /**
     * Tests that the same filter instance can properly handle multiple different
     * classes in succession.
     */
    @Test
    public void testVisitAnyClass_sameFilterInstance_multipleClasses() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz originalNameClazz1 = mock(Clazz.class);
        when(originalNameClazz1.getName()).thenReturn("com/example/Class1");
        when(originalNameClazz1.getProcessingInfo()).thenReturn("com/example/Class1");

        Clazz renamedClazz = mock(Clazz.class);
        when(renamedClazz.getName()).thenReturn("com/example/Class2");
        when(renamedClazz.getProcessingInfo()).thenReturn("com/example/a");

        Clazz originalNameClazz2 = mock(Clazz.class);
        when(originalNameClazz2.getName()).thenReturn("com/example/Class3");
        when(originalNameClazz2.getProcessingInfo()).thenReturn("com/example/Class3");

        // Act
        filter.visitAnyClass(originalNameClazz1);
        filter.visitAnyClass(renamedClazz);
        filter.visitAnyClass(originalNameClazz2);

        // Assert
        verify(originalNameClazz1).accept(acceptedVisitor);
        verify(renamedClazz).accept(rejectedVisitor);
        verify(originalNameClazz2).accept(acceptedVisitor);

        verify(originalNameClazz1, never()).accept(rejectedVisitor);
        verify(renamedClazz, never()).accept(acceptedVisitor);
        verify(originalNameClazz2, never()).accept(rejectedVisitor);
    }

    /**
     * Tests that the filter correctly handles a class whose name changes
     * between calls (simulating dynamic renaming scenario).
     */
    @Test
    public void testVisitAnyClass_classNameChanges_betweenCalls() {
        // Arrange
        ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
        ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
        OriginalClassNameFilter filter = new OriginalClassNameFilter(acceptedVisitor, rejectedVisitor);

        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        // First call: class has original name
        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(originalName);
        filter.visitAnyClass(mockClazz);

        // Second call: class has been renamed
        when(mockClazz.getProcessingInfo()).thenReturn(newName);
        filter.visitAnyClass(mockClazz);

        // Third call: class reverted to original name
        when(mockClazz.getProcessingInfo()).thenReturn(originalName);
        filter.visitAnyClass(mockClazz);

        // Assert
        verify(mockClazz, times(2)).accept(acceptedVisitor);
        verify(mockClazz, times(1)).accept(rejectedVisitor);
    }
}
