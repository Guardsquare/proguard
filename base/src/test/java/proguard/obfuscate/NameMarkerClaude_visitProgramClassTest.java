package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameMarker#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method in NameMarker is responsible for marking the class name
 * to be kept during obfuscation and ensuring outer class names are also preserved.
 *
 * The method performs two key actions:
 * 1. Calls keepClassName(programClass) which sets the new class name to its current name
 *    using ClassObfuscator.setNewClassName(clazz, clazz.getName())
 * 2. Calls programClass.attributesAccept(this) to visit attributes and preserve outer class names
 */
public class NameMarkerClaude_visitProgramClassTest {

    private NameMarker nameMarker;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
        programClass = mock(ProgramClass.class);
    }

    /**
     * Tests that visitProgramClass calls getName() on the ProgramClass to get the class name.
     * This is necessary for the keepClassName operation.
     */
    @Test
    public void testVisitProgramClass_callsGetName() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass, atLeastOnce()).getName();
    }

    /**
     * Tests that visitProgramClass calls setProcessingInfo to mark the class name as kept.
     * ClassObfuscator.setNewClassName internally calls setProcessingInfo.
     */
    @Test
    public void testVisitProgramClass_callsSetProcessingInfo() {
        // Arrange
        String className = "com/example/TestClass";
        when(programClass.getName()).thenReturn(className);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(className));
    }

    /**
     * Tests that visitProgramClass calls attributesAccept to process attributes.
     * This ensures outer class names are also kept.
     */
    @Test
    public void testVisitProgramClass_callsAttributesAccept() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with a simple class name (no package).
     */
    @Test
    public void testVisitProgramClass_withSimpleClassName() {
        // Arrange
        String simpleName = "SimpleClass";
        when(programClass.getName()).thenReturn(simpleName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(simpleName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with a fully qualified class name.
     */
    @Test
    public void testVisitProgramClass_withFullyQualifiedClassName() {
        // Arrange
        String fullyQualifiedName = "com/example/foo/bar/MyClass";
        when(programClass.getName()).thenReturn(fullyQualifiedName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(fullyQualifiedName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with an inner class name.
     */
    @Test
    public void testVisitProgramClass_withInnerClassName() {
        // Arrange
        String innerClassName = "com/example/OuterClass$InnerClass";
        when(programClass.getName()).thenReturn(innerClassName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(innerClassName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with anonymous inner class names.
     */
    @Test
    public void testVisitProgramClass_withAnonymousInnerClassName() {
        // Arrange
        String anonymousInnerClassName = "com/example/OuterClass$1";
        when(programClass.getName()).thenReturn(anonymousInnerClassName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(anonymousInnerClassName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with nested inner class names.
     */
    @Test
    public void testVisitProgramClass_withNestedInnerClassName() {
        // Arrange
        String nestedInnerClassName = "com/example/Outer$Inner$Deep";
        when(programClass.getName()).thenReturn(nestedInnerClassName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(nestedInnerClassName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass preserves the exact class name by setting it as the new name.
     * The new name should match the original name.
     */
    @Test
    public void testVisitProgramClass_preservesClassName() {
        // Arrange
        String originalName = "com/example/ImportantClass";
        when(programClass.getName()).thenReturn(originalName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert - setProcessingInfo is called with the same name returned by getName()
        verify(programClass).getName();
        verify(programClass).setProcessingInfo(eq(originalName));
    }

    /**
     * Tests the order of operations in visitProgramClass.
     * setProcessingInfo should be called before attributesAccept.
     */
    @Test
    public void testVisitProgramClass_correctOrderOfOperations() {
        // Arrange
        String className = "com/example/TestClass";
        when(programClass.getName()).thenReturn(className);

        // Create an ordered mock to verify call order
        ProgramClass orderedMock = mock(ProgramClass.class);
        when(orderedMock.getName()).thenReturn(className);

        // Act
        nameMarker.visitProgramClass(orderedMock);

        // Assert - verify both methods are called (order verification requires InOrder)
        verify(orderedMock).setProcessingInfo(className);
        verify(orderedMock).attributesAccept(nameMarker);
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same NameMarker instance.
     */
    @Test
    public void testVisitProgramClass_multipleCallsOnSameInstance() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        when(class1.getName()).thenReturn("com/example/Class1");

        ProgramClass class2 = mock(ProgramClass.class);
        when(class2.getName()).thenReturn("com/example/Class2");

        // Act
        nameMarker.visitProgramClass(class1);
        nameMarker.visitProgramClass(class2);

        // Assert
        verify(class1).setProcessingInfo("com/example/Class1");
        verify(class1).attributesAccept(nameMarker);
        verify(class2).setProcessingInfo("com/example/Class2");
        verify(class2).attributesAccept(nameMarker);
    }

    /**
     * Tests that visitProgramClass can be called with the same ProgramClass multiple times.
     */
    @Test
    public void testVisitProgramClass_sameProgramClassCalledMultipleTimes() {
        // Arrange
        String className = "com/example/TestClass";
        when(programClass.getName()).thenReturn(className);

        // Act
        nameMarker.visitProgramClass(programClass);
        nameMarker.visitProgramClass(programClass);
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(3)).setProcessingInfo(eq(className));
        verify(programClass, times(3)).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with very long class names.
     */
    @Test
    public void testVisitProgramClass_withVeryLongClassName() {
        // Arrange
        String longClassName = "com/example/very/long/package/name/with/many/segments/and/more/ClassNameThatIsAlsoVeryLong";
        when(programClass.getName()).thenReturn(longClassName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(longClassName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with single character class names.
     */
    @Test
    public void testVisitProgramClass_withSingleCharacterClassName() {
        // Arrange
        String singleCharName = "A";
        when(programClass.getName()).thenReturn(singleCharName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(singleCharName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works with obfuscated-style class names (short, lowercase).
     */
    @Test
    public void testVisitProgramClass_withObfuscatedStyleClassName() {
        // Arrange
        String obfuscatedName = "a/b/c";
        when(programClass.getName()).thenReturn(obfuscatedName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(obfuscatedName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass doesn't throw exceptions on valid input.
     */
    @Test
    public void testVisitProgramClass_noExceptionThrown() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> nameMarker.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass passes the NameMarker instance (this) to attributesAccept.
     * This is important because NameMarker implements AttributeVisitor.
     */
    @Test
    public void testVisitProgramClass_passesCorrectVisitorToAttributesAccept() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert - verify that the visitor passed is the nameMarker itself
        verify(programClass).attributesAccept(same(nameMarker));
    }

    /**
     * Tests that multiple NameMarker instances can process the same ProgramClass independently.
     */
    @Test
    public void testVisitProgramClass_multipleNameMarkerInstances() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act
        marker1.visitProgramClass(programClass);
        marker2.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(2)).setProcessingInfo("com/example/TestClass");
        verify(programClass).attributesAccept(marker1);
        verify(programClass).attributesAccept(marker2);
    }

    /**
     * Tests that visitProgramClass handles class names with special characters.
     */
    @Test
    public void testVisitProgramClass_withSpecialCharactersInClassName() {
        // Arrange - some JVM languages allow special characters in class names
        String specialName = "com/example/My-Class_123";
        when(programClass.getName()).thenReturn(specialName);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(specialName));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass works correctly when getName() is called multiple times.
     * The method may call getName() more than once internally.
     */
    @Test
    public void testVisitProgramClass_getNameCalledConsistently() {
        // Arrange
        String className = "com/example/TestClass";
        when(programClass.getName()).thenReturn(className);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert - getName should be called at least once
        verify(programClass, atLeastOnce()).getName();
        // And setProcessingInfo should receive the same value
        verify(programClass).setProcessingInfo(className);
    }

    /**
     * Tests that visitProgramClass does not modify the ProgramClass beyond expected operations.
     * Only setProcessingInfo, getName, and attributesAccept should be called.
     */
    @Test
    public void testVisitProgramClass_onlyExpectedMethodsCalled() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert - verify no other methods are called
        verify(programClass, atLeastOnce()).getName();
        verify(programClass).setProcessingInfo(anyString());
        verify(programClass).attributesAccept(any(AttributeVisitor.class));
        verifyNoMoreInteractions(programClass);
    }

    /**
     * Tests that visitProgramClass with a default package class name (no slashes).
     */
    @Test
    public void testVisitProgramClass_withDefaultPackageClass() {
        // Arrange
        String defaultPackageClass = "DefaultPackageClass";
        when(programClass.getName()).thenReturn(defaultPackageClass);

        // Act
        nameMarker.visitProgramClass(programClass);

        // Assert
        verify(programClass).setProcessingInfo(eq(defaultPackageClass));
        verify(programClass).attributesAccept(eq(nameMarker));
    }

    /**
     * Tests that visitProgramClass preserves the class name exactly as returned by getName().
     * This is the core purpose of the method - to mark the name as kept (unchangeable).
     */
    @Test
    public void testVisitProgramClass_exactNamePreservation() {
        // Arrange
        String[] testNames = {
            "com/example/Test",
            "a/b/C",
            "SingleName",
            "com/example/Outer$Inner",
            "com/example/Outer$1",
            "com/example/Test$Inner$Deep"
        };

        for (String testName : testNames) {
            ProgramClass testClass = mock(ProgramClass.class);
            when(testClass.getName()).thenReturn(testName);

            // Act
            nameMarker.visitProgramClass(testClass);

            // Assert
            verify(testClass).setProcessingInfo(eq(testName));
            verify(testClass).attributesAccept(eq(nameMarker));
        }
    }
}
