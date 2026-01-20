package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameMarker#visitLibraryClass(LibraryClass)}.
 *
 * The visitLibraryClass method in NameMarker is responsible for marking the class name
 * to be kept during obfuscation.
 *
 * The method performs one key action:
 * 1. Calls keepClassName(libraryClass) which sets the new class name to its current name
 *    using ClassObfuscator.setNewClassName(clazz, clazz.getName())
 *
 * Unlike visitProgramClass, this method does NOT call attributesAccept because
 * LibraryClass instances typically don't have attributes that need processing.
 */
public class NameMarkerClaude_visitLibraryClassTest {

    private NameMarker nameMarker;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
        libraryClass = mock(LibraryClass.class);
    }

    /**
     * Tests that visitLibraryClass calls getName() on the LibraryClass to get the class name.
     * This is necessary for the keepClassName operation.
     */
    @Test
    public void testVisitLibraryClass_callsGetName() {
        // Arrange
        when(libraryClass.getName()).thenReturn("java/lang/String");

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass, atLeastOnce()).getName();
    }

    /**
     * Tests that visitLibraryClass calls setProcessingInfo to mark the class name as kept.
     * ClassObfuscator.setNewClassName internally calls setProcessingInfo.
     */
    @Test
    public void testVisitLibraryClass_callsSetProcessingInfo() {
        // Arrange
        String className = "java/lang/String";
        when(libraryClass.getName()).thenReturn(className);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(className));
    }

    /**
     * Tests that visitLibraryClass works with a simple class name (no package).
     */
    @Test
    public void testVisitLibraryClass_withSimpleClassName() {
        // Arrange
        String simpleName = "SimpleClass";
        when(libraryClass.getName()).thenReturn(simpleName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(simpleName));
    }

    /**
     * Tests that visitLibraryClass works with a fully qualified class name.
     */
    @Test
    public void testVisitLibraryClass_withFullyQualifiedClassName() {
        // Arrange
        String fullyQualifiedName = "java/util/ArrayList";
        when(libraryClass.getName()).thenReturn(fullyQualifiedName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(fullyQualifiedName));
    }

    /**
     * Tests that visitLibraryClass works with an inner class name.
     */
    @Test
    public void testVisitLibraryClass_withInnerClassName() {
        // Arrange
        String innerClassName = "java/util/Map$Entry";
        when(libraryClass.getName()).thenReturn(innerClassName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(innerClassName));
    }

    /**
     * Tests that visitLibraryClass preserves the exact class name by setting it as the new name.
     * The new name should match the original name.
     */
    @Test
    public void testVisitLibraryClass_preservesClassName() {
        // Arrange
        String originalName = "java/lang/Object";
        when(libraryClass.getName()).thenReturn(originalName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert - setProcessingInfo is called with the same name returned by getName()
        verify(libraryClass).getName();
        verify(libraryClass).setProcessingInfo(eq(originalName));
    }

    /**
     * Tests that visitLibraryClass can be called multiple times on the same NameMarker instance.
     */
    @Test
    public void testVisitLibraryClass_multipleCallsOnSameInstance() {
        // Arrange
        LibraryClass class1 = mock(LibraryClass.class);
        when(class1.getName()).thenReturn("java/lang/String");

        LibraryClass class2 = mock(LibraryClass.class);
        when(class2.getName()).thenReturn("java/lang/Integer");

        // Act
        nameMarker.visitLibraryClass(class1);
        nameMarker.visitLibraryClass(class2);

        // Assert
        verify(class1).setProcessingInfo("java/lang/String");
        verify(class2).setProcessingInfo("java/lang/Integer");
    }

    /**
     * Tests that visitLibraryClass can be called with the same LibraryClass multiple times.
     */
    @Test
    public void testVisitLibraryClass_sameLibraryClassCalledMultipleTimes() {
        // Arrange
        String className = "java/lang/String";
        when(libraryClass.getName()).thenReturn(className);

        // Act
        nameMarker.visitLibraryClass(libraryClass);
        nameMarker.visitLibraryClass(libraryClass);
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass, times(3)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that visitLibraryClass works with very long class names.
     */
    @Test
    public void testVisitLibraryClass_withVeryLongClassName() {
        // Arrange
        String longClassName = "java/util/concurrent/atomic/AtomicReferenceFieldUpdater$AtomicReferenceFieldUpdaterImpl";
        when(libraryClass.getName()).thenReturn(longClassName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(longClassName));
    }

    /**
     * Tests that visitLibraryClass works with single character class names.
     */
    @Test
    public void testVisitLibraryClass_withSingleCharacterClassName() {
        // Arrange
        String singleCharName = "Z";
        when(libraryClass.getName()).thenReturn(singleCharName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(singleCharName));
    }

    /**
     * Tests that visitLibraryClass doesn't throw exceptions on valid input.
     */
    @Test
    public void testVisitLibraryClass_noExceptionThrown() {
        // Arrange
        when(libraryClass.getName()).thenReturn("java/lang/String");

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> nameMarker.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that multiple NameMarker instances can process the same LibraryClass independently.
     */
    @Test
    public void testVisitLibraryClass_multipleNameMarkerInstances() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();
        when(libraryClass.getName()).thenReturn("java/lang/String");

        // Act
        marker1.visitLibraryClass(libraryClass);
        marker2.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass, times(2)).setProcessingInfo("java/lang/String");
    }

    /**
     * Tests that visitLibraryClass works correctly when getName() is called multiple times.
     * The method may call getName() more than once internally.
     */
    @Test
    public void testVisitLibraryClass_getNameCalledConsistently() {
        // Arrange
        String className = "java/lang/String";
        when(libraryClass.getName()).thenReturn(className);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert - getName should be called at least once
        verify(libraryClass, atLeastOnce()).getName();
        // And setProcessingInfo should receive the same value
        verify(libraryClass).setProcessingInfo(className);
    }

    /**
     * Tests that visitLibraryClass does not modify the LibraryClass beyond expected operations.
     * Only setProcessingInfo and getName should be called (no attributesAccept).
     */
    @Test
    public void testVisitLibraryClass_onlyExpectedMethodsCalled() {
        // Arrange
        when(libraryClass.getName()).thenReturn("java/lang/String");

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert - verify no other methods are called
        verify(libraryClass, atLeastOnce()).getName();
        verify(libraryClass).setProcessingInfo(anyString());
        verifyNoMoreInteractions(libraryClass);
    }

    /**
     * Tests that visitLibraryClass with a default package class name (no slashes).
     */
    @Test
    public void testVisitLibraryClass_withDefaultPackageClass() {
        // Arrange
        String defaultPackageClass = "DefaultPackageClass";
        when(libraryClass.getName()).thenReturn(defaultPackageClass);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(defaultPackageClass));
    }

    /**
     * Tests that visitLibraryClass preserves the class name exactly as returned by getName().
     * This is the core purpose of the method - to mark the name as kept (unchangeable).
     */
    @Test
    public void testVisitLibraryClass_exactNamePreservation() {
        // Arrange
        String[] testNames = {
            "java/lang/String",
            "java/util/Map$Entry",
            "SingleName",
            "java/lang/Character$UnicodeBlock",
            "java/util/concurrent/ConcurrentHashMap$Node"
        };

        for (String testName : testNames) {
            LibraryClass testClass = mock(LibraryClass.class);
            when(testClass.getName()).thenReturn(testName);

            // Act
            nameMarker.visitLibraryClass(testClass);

            // Assert
            verify(testClass).setProcessingInfo(eq(testName));
        }
    }

    /**
     * Tests that visitLibraryClass does NOT call attributesAccept.
     * Unlike visitProgramClass, library classes do not need attribute processing.
     */
    @Test
    public void testVisitLibraryClass_doesNotCallAttributesAccept() {
        // Arrange
        when(libraryClass.getName()).thenReturn("java/lang/String");

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert - verify attributesAccept is NOT called
        verify(libraryClass, never()).attributesAccept(any());
    }

    /**
     * Tests that visitLibraryClass handles nested inner class names.
     */
    @Test
    public void testVisitLibraryClass_withNestedInnerClassName() {
        // Arrange
        String nestedInnerClassName = "java/lang/Character$Subset$UnicodeBlock";
        when(libraryClass.getName()).thenReturn(nestedInnerClassName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(nestedInnerClassName));
    }

    /**
     * Tests that visitLibraryClass handles Android library class names.
     */
    @Test
    public void testVisitLibraryClass_withAndroidLibraryClassName() {
        // Arrange
        String androidClassName = "android/content/Context";
        when(libraryClass.getName()).thenReturn(androidClassName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(androidClassName));
    }

    /**
     * Tests that visitLibraryClass handles Java standard library class names correctly.
     */
    @Test
    public void testVisitLibraryClass_withJavaStandardLibraryClasses() {
        // Arrange
        String[] javaLibraryClasses = {
            "java/lang/Object",
            "java/lang/String",
            "java/util/ArrayList",
            "java/io/InputStream",
            "java/net/URL"
        };

        for (String className : javaLibraryClasses) {
            LibraryClass testClass = mock(LibraryClass.class);
            when(testClass.getName()).thenReturn(className);

            // Act
            nameMarker.visitLibraryClass(testClass);

            // Assert
            verify(testClass).setProcessingInfo(eq(className));
        }
    }

    /**
     * Tests that visitLibraryClass works with array type descriptors if they appear.
     */
    @Test
    public void testVisitLibraryClass_withArrayTypeClassName() {
        // Arrange - array types might appear in some contexts
        String arrayTypeName = "[Ljava/lang/String;";
        when(libraryClass.getName()).thenReturn(arrayTypeName);

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert
        verify(libraryClass).setProcessingInfo(eq(arrayTypeName));
    }

    /**
     * Tests that the difference between visitProgramClass and visitLibraryClass:
     * LibraryClass should NOT have attributesAccept called.
     */
    @Test
    public void testVisitLibraryClass_differenceFromVisitProgramClass() {
        // Arrange
        when(libraryClass.getName()).thenReturn("java/lang/String");

        // Act
        nameMarker.visitLibraryClass(libraryClass);

        // Assert - the key difference is that attributesAccept is never called
        verify(libraryClass, atLeastOnce()).getName();
        verify(libraryClass).setProcessingInfo(anyString());
        verify(libraryClass, never()).attributesAccept(any());
        verifyNoMoreInteractions(libraryClass);
    }
}
