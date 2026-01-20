package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SourceFileRenamer#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method accepts a ProgramClass and calls attributesAccept on it,
 * passing itself as the AttributeVisitor. This allows the SourceFileRenamer to visit
 * and potentially modify the attributes of the program class.
 */
public class SourceFileRenamerClaude_visitProgramClassTest {

    private SourceFileRenamer renamer;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        renamer = new SourceFileRenamer("NewSourceFile.java");
        programClass = mock(ProgramClass.class);
    }

    /**
     * Tests that visitProgramClass calls attributesAccept on the ProgramClass.
     * This is the primary behavior of the method.
     */
    @Test
    public void testVisitProgramClass_callsAttributesAccept() {
        // Act
        renamer.visitProgramClass(programClass);

        // Assert - verify attributesAccept was called with the renamer as the visitor
        verify(programClass, times(1)).attributesAccept(eq(renamer));
    }

    /**
     * Tests that visitProgramClass passes the SourceFileRenamer itself as the AttributeVisitor.
     * The renamer acts as both ClassVisitor and AttributeVisitor.
     */
    @Test
    public void testVisitProgramClass_passesItselfAsAttributeVisitor() {
        // Act
        renamer.visitProgramClass(programClass);

        // Assert - verify the renamer is passed as the AttributeVisitor
        verify(programClass).attributesAccept(any(AttributeVisitor.class));
        verify(programClass).attributesAccept(eq(renamer));
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same ProgramClass.
     * Each call should trigger attributesAccept.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_callsAttributesAcceptEachTime() {
        // Act
        renamer.visitProgramClass(programClass);
        renamer.visitProgramClass(programClass);
        renamer.visitProgramClass(programClass);

        // Assert - verify attributesAccept was called 3 times
        verify(programClass, times(3)).attributesAccept(eq(renamer));
    }

    /**
     * Tests that visitProgramClass works with different SourceFileRenamer instances.
     * Each renamer should pass itself as the AttributeVisitor.
     */
    @Test
    public void testVisitProgramClass_withDifferentRenamers_eachPassesItself() {
        // Arrange
        SourceFileRenamer renamer1 = new SourceFileRenamer("Source1.java");
        SourceFileRenamer renamer2 = new SourceFileRenamer("Source2.java");
        ProgramClass programClass1 = mock(ProgramClass.class);
        ProgramClass programClass2 = mock(ProgramClass.class);

        // Act
        renamer1.visitProgramClass(programClass1);
        renamer2.visitProgramClass(programClass2);

        // Assert
        verify(programClass1).attributesAccept(eq(renamer1));
        verify(programClass2).attributesAccept(eq(renamer2));
    }

    /**
     * Tests that visitProgramClass doesn't throw an exception with a valid ProgramClass.
     */
    @Test
    public void testVisitProgramClass_withValidClass_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> renamer.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass can work with different ProgramClass instances.
     */
    @Test
    public void testVisitProgramClass_withDifferentClasses_callsAttributesAcceptOnEach() {
        // Arrange
        ProgramClass programClass1 = mock(ProgramClass.class);
        ProgramClass programClass2 = mock(ProgramClass.class);
        ProgramClass programClass3 = mock(ProgramClass.class);

        // Act
        renamer.visitProgramClass(programClass1);
        renamer.visitProgramClass(programClass2);
        renamer.visitProgramClass(programClass3);

        // Assert
        verify(programClass1, times(1)).attributesAccept(eq(renamer));
        verify(programClass2, times(1)).attributesAccept(eq(renamer));
        verify(programClass3, times(1)).attributesAccept(eq(renamer));
    }

    /**
     * Tests that visitProgramClass executes quickly and efficiently.
     * Should have minimal overhead since it's a simple delegation.
     */
    @Test
    public void testVisitProgramClass_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            renamer.visitProgramClass(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitProgramClass should execute quickly");
    }

    /**
     * Tests that visitProgramClass works correctly after renamer is instantiated with null name.
     * The method should still call attributesAccept even if the renamer has a null source file name.
     */
    @Test
    public void testVisitProgramClass_withNullSourceFileName_stillCallsAttributesAccept() {
        // Arrange
        SourceFileRenamer nullNameRenamer = new SourceFileRenamer(null);

        // Act
        nullNameRenamer.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(1)).attributesAccept(eq(nullNameRenamer));
    }

    /**
     * Tests that visitProgramClass works correctly after renamer is instantiated with empty string.
     */
    @Test
    public void testVisitProgramClass_withEmptySourceFileName_stillCallsAttributesAccept() {
        // Arrange
        SourceFileRenamer emptyNameRenamer = new SourceFileRenamer("");

        // Act
        emptyNameRenamer.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(1)).attributesAccept(eq(emptyNameRenamer));
    }

    /**
     * Tests that visitProgramClass can be called in rapid succession without issues.
     */
    @Test
    public void testVisitProgramClass_rapidSequentialCalls_allSucceed() {
        // Act & Assert - all calls should complete without issues
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> renamer.visitProgramClass(programClass),
                    "Call " + i + " should not throw exception");
        }

        // Verify all calls went through
        verify(programClass, times(10)).attributesAccept(eq(renamer));
    }

    /**
     * Tests that visitProgramClass doesn't call any other methods on the ProgramClass.
     * The only interaction should be with attributesAccept.
     */
    @Test
    public void testVisitProgramClass_onlyCallsAttributesAccept() {
        // Act
        renamer.visitProgramClass(programClass);

        // Assert - verify only attributesAccept was called
        verify(programClass, times(1)).attributesAccept(eq(renamer));
        verifyNoMoreInteractions(programClass);
    }

    /**
     * Tests that multiple renamers can visit the same ProgramClass independently.
     */
    @Test
    public void testVisitProgramClass_multipleRenamersOnSameClass_eachCallsAttributesAccept() {
        // Arrange
        SourceFileRenamer renamer1 = new SourceFileRenamer("Source1.java");
        SourceFileRenamer renamer2 = new SourceFileRenamer("Source2.java");
        SourceFileRenamer renamer3 = new SourceFileRenamer("Source3.java");

        // Act
        renamer1.visitProgramClass(programClass);
        renamer2.visitProgramClass(programClass);
        renamer3.visitProgramClass(programClass);

        // Assert
        verify(programClass).attributesAccept(eq(renamer1));
        verify(programClass).attributesAccept(eq(renamer2));
        verify(programClass).attributesAccept(eq(renamer3));
        verify(programClass, times(3)).attributesAccept(any(AttributeVisitor.class));
    }

    /**
     * Tests that visitProgramClass works with various source file name formats.
     */
    @Test
    public void testVisitProgramClass_withVariousSourceFileNames_callsAttributesAccept() {
        // Arrange
        String[] sourceFileNames = {
            "SourceFile.java",
            "Test.java",
            "MyClass.kt",
            "package/File.java",
            "some/deep/path/File.java",
            "File.scala",
            "NoExtension",
            "With Spaces.java",
            "特殊字符.java"
        };

        // Act & Assert
        for (String fileName : sourceFileNames) {
            SourceFileRenamer testRenamer = new SourceFileRenamer(fileName);
            ProgramClass testClass = mock(ProgramClass.class);

            testRenamer.visitProgramClass(testClass);

            verify(testClass, times(1)).attributesAccept(eq(testRenamer));
        }
    }

    /**
     * Tests that visitProgramClass behavior is consistent across calls.
     */
    @Test
    public void testVisitProgramClass_consistentBehaviorAcrossCalls() {
        // Act
        renamer.visitProgramClass(programClass);
        reset(programClass); // Reset mock to verify second call independently
        renamer.visitProgramClass(programClass);

        // Assert - second call should have the same behavior
        verify(programClass, times(1)).attributesAccept(eq(renamer));
    }

    /**
     * Tests that visitProgramClass doesn't modify the SourceFileRenamer's state.
     * The renamer should be usable for subsequent operations after visiting a class.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyRenamerState() {
        // Arrange
        ProgramClass programClass1 = mock(ProgramClass.class);
        ProgramClass programClass2 = mock(ProgramClass.class);

        // Act - visit first class
        renamer.visitProgramClass(programClass1);

        // Act - visit second class
        renamer.visitProgramClass(programClass2);

        // Assert - both should be visited successfully
        verify(programClass1).attributesAccept(eq(renamer));
        verify(programClass2).attributesAccept(eq(renamer));
    }

    /**
     * Tests that visitProgramClass can alternate with visitAnyClass calls.
     * Both methods should work independently.
     */
    @Test
    public void testVisitProgramClass_alternatingWithVisitAnyClass() {
        // Act
        renamer.visitAnyClass(programClass);
        renamer.visitProgramClass(programClass);
        renamer.visitAnyClass(programClass);
        renamer.visitProgramClass(programClass);

        // Assert - only visitProgramClass calls should trigger attributesAccept
        verify(programClass, times(2)).attributesAccept(eq(renamer));
    }
}
