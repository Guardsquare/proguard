package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.util.WarningPrinter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberNameConflictFixer#visitLibraryField(LibraryClass, LibraryField)}.
 *
 * The visitLibraryField method is a no-op implementation that does nothing.
 * Library members are not processed by the conflict fixer - only program members are.
 * These tests verify that the method can be called without errors and doesn't modify any state.
 */
public class MemberNameConflictFixerClaude_visitLibraryFieldTest {

    private MemberNameConflictFixer fixer;
    private LibraryClass libraryClass;
    private LibraryField libraryField;
    private Map<String, Map<String, String>> descriptorMap;
    private MemberObfuscator memberObfuscator;

    @BeforeEach
    public void setUp() {
        descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        memberObfuscator = mock(MemberObfuscator.class);

        fixer = new MemberNameConflictFixer(
            true,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        libraryClass = new LibraryClass();
        libraryField = new LibraryField();
    }

    /**
     * Tests that visitLibraryField can be called without throwing any exceptions.
     * This is the primary behavior - the method is a no-op that should complete without errors.
     */
    @Test
    public void testVisitLibraryField_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitLibraryField(libraryClass, libraryField),
            "visitLibraryField should not throw any exception");
    }

    /**
     * Tests that visitLibraryField does not interact with the MemberObfuscator.
     * Since this is a no-op for library fields, the obfuscator should never be called.
     */
    @Test
    public void testVisitLibraryField_doesNotInvokeMemberObfuscator() {
        // Act
        fixer.visitLibraryField(libraryClass, libraryField);

        // Assert
        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryField does not modify the descriptor map.
     * The map should remain unchanged after visiting a library field.
     */
    @Test
    public void testVisitLibraryField_doesNotModifyDescriptorMap() {
        // Arrange
        assertTrue(descriptorMap.isEmpty(), "Descriptor map should be empty initially");

        // Act
        fixer.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertTrue(descriptorMap.isEmpty(), "Descriptor map should remain empty");
    }

    /**
     * Tests that visitLibraryField can be called multiple times without issues.
     * Multiple calls should all be no-ops.
     */
    @Test
    public void testVisitLibraryField_multipleCallsSucceed() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitLibraryField(libraryClass, libraryField);
            fixer.visitLibraryField(libraryClass, libraryField);
            fixer.visitLibraryField(libraryClass, libraryField);
        }, "Multiple calls to visitLibraryField should not throw any exception");
    }

    /**
     * Tests that visitLibraryField works with different library field instances.
     * The method should handle any LibraryField instance.
     */
    @Test
    public void testVisitLibraryField_withDifferentFields() {
        // Arrange
        LibraryField field1 = new LibraryField();
        LibraryField field2 = new LibraryField();
        LibraryField field3 = new LibraryField();

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitLibraryField(libraryClass, field1);
            fixer.visitLibraryField(libraryClass, field2);
            fixer.visitLibraryField(libraryClass, field3);
        }, "visitLibraryField should work with different field instances");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryField works with different library class instances.
     * The method should handle any LibraryClass instance.
     */
    @Test
    public void testVisitLibraryField_withDifferentClasses() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryClass class3 = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitLibraryField(class1, libraryField);
            fixer.visitLibraryField(class2, libraryField);
            fixer.visitLibraryField(class3, libraryField);
        }, "visitLibraryField should work with different class instances");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryField does not modify the field's state.
     * Since no member name operations occur on library fields, any pre-existing state should be unchanged.
     */
    @Test
    public void testVisitLibraryField_doesNotModifyFieldState() {
        // Arrange - Set a new member name on the library field
        MemberObfuscator.setNewMemberName(libraryField, "someObfuscatedName");
        String nameBefore = MemberObfuscator.newMemberName(libraryField);

        // Act
        fixer.visitLibraryField(libraryClass, libraryField);

        // Assert - The name should remain unchanged
        String nameAfter = MemberObfuscator.newMemberName(libraryField);
        assertEquals(nameBefore, nameAfter, "Library field's new member name should remain unchanged");
    }

    /**
     * Tests that visitLibraryField works correctly with aggressive overloading enabled.
     * The aggressive overloading setting should have no effect on library fields.
     */
    @Test
    public void testVisitLibraryField_withAggressiveOverloadingEnabled() {
        // Arrange
        MemberNameConflictFixer aggressiveFixer = new MemberNameConflictFixer(
            true, // aggressive overloading enabled
            descriptorMap,
            null,
            memberObfuscator
        );

        // Act & Assert
        assertDoesNotThrow(() -> aggressiveFixer.visitLibraryField(libraryClass, libraryField),
            "visitLibraryField should work with aggressive overloading enabled");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryField works correctly with aggressive overloading disabled.
     * The aggressive overloading setting should have no effect on library fields.
     */
    @Test
    public void testVisitLibraryField_withAggressiveOverloadingDisabled() {
        // Arrange
        MemberNameConflictFixer conservativeFixer = new MemberNameConflictFixer(
            false, // aggressive overloading disabled
            descriptorMap,
            null,
            memberObfuscator
        );

        // Act & Assert
        assertDoesNotThrow(() -> conservativeFixer.visitLibraryField(libraryClass, libraryField),
            "visitLibraryField should work with aggressive overloading disabled");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryField works with a populated descriptor map.
     * Even with existing mappings, library fields should not be processed.
     */
    @Test
    public void testVisitLibraryField_withPopulatedDescriptorMap() {
        // Arrange - Populate the descriptor map
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("newName1", "oldName1");
        nameMap.put("newName2", "oldName2");
        descriptorMap.put("()V", nameMap);
        descriptorMap.put("(I)V", new HashMap<>());

        // Act
        fixer.visitLibraryField(libraryClass, libraryField);

        // Assert - Map should remain unchanged
        assertEquals(2, descriptorMap.size(), "Descriptor map size should remain unchanged");
        assertEquals(2, descriptorMap.get("()V").size(), "Name map should remain unchanged");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryField works when warning printer is null.
     * Null warning printer should not cause any issues for library fields.
     */
    @Test
    public void testVisitLibraryField_withNullWarningPrinter() {
        // Arrange
        MemberNameConflictFixer fixerWithNullWarnings = new MemberNameConflictFixer(
            true,
            descriptorMap,
            null, // null warning printer
            memberObfuscator
        );

        // Act & Assert
        assertDoesNotThrow(() -> fixerWithNullWarnings.visitLibraryField(libraryClass, libraryField),
            "visitLibraryField should work with null warning printer");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that multiple fixers can visit the same library field without interference.
     * Each fixer should independently do nothing.
     */
    @Test
    public void testVisitLibraryField_withMultipleFixers() {
        // Arrange
        MemberObfuscator obfuscator1 = mock(MemberObfuscator.class);
        MemberObfuscator obfuscator2 = mock(MemberObfuscator.class);

        MemberNameConflictFixer fixer1 = new MemberNameConflictFixer(
            true, new HashMap<>(), null, obfuscator1
        );

        MemberNameConflictFixer fixer2 = new MemberNameConflictFixer(
            false, new HashMap<>(), null, obfuscator2
        );

        // Act
        fixer1.visitLibraryField(libraryClass, libraryField);
        fixer2.visitLibraryField(libraryClass, libraryField);

        // Assert
        verifyNoInteractions(obfuscator1);
        verifyNoInteractions(obfuscator2);
    }

    /**
     * Tests that visitLibraryField completes quickly as a no-op operation.
     * Since it does nothing, it should execute very efficiently.
     */
    @Test
    public void testVisitLibraryField_isEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        fixer.visitLibraryField(libraryClass, libraryField);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 1_000_000L, // Less than 1 millisecond
            "visitLibraryField should complete very quickly (took " + duration + " ns)");
    }

    /**
     * Tests that visitLibraryField can be called in sequence with other visitor methods.
     * This verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitLibraryField_sequenceWithOtherVisitorMethods() {
        // Arrange
        LibraryField field1 = new LibraryField();
        LibraryField field2 = new LibraryField();

        // Act & Assert - Call visitLibraryField in sequence
        assertDoesNotThrow(() -> {
            fixer.visitLibraryField(libraryClass, field1);
            fixer.visitLibraryField(libraryClass, field2);
            fixer.visitLibraryField(libraryClass, libraryField);
        }, "visitLibraryField should work correctly in sequence");

        verifyNoInteractions(memberObfuscator);
    }
}
