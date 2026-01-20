package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.util.WarningPrinter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberNameConflictFixer#visitLibraryMethod(LibraryClass, LibraryMethod)}.
 *
 * The visitLibraryMethod method is a no-op implementation that does nothing.
 * Library members are not processed by the conflict fixer - only program members are.
 * These tests verify that the method can be called without errors and doesn't modify any state.
 */
public class MemberNameConflictFixerClaude_visitLibraryMethodTest {

    private MemberNameConflictFixer fixer;
    private LibraryClass libraryClass;
    private LibraryMethod libraryMethod;
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
        libraryMethod = new LibraryMethod();
    }

    /**
     * Tests that visitLibraryMethod can be called without throwing any exceptions.
     * This is the primary behavior - the method is a no-op that should complete without errors.
     */
    @Test
    public void testVisitLibraryMethod_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitLibraryMethod(libraryClass, libraryMethod),
            "visitLibraryMethod should not throw any exception");
    }

    /**
     * Tests that visitLibraryMethod does not interact with the MemberObfuscator.
     * Since this is a no-op for library methods, the obfuscator should never be called.
     */
    @Test
    public void testVisitLibraryMethod_doesNotInvokeMemberObfuscator() {
        // Act
        fixer.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryMethod does not modify the descriptor map.
     * The map should remain unchanged after visiting a library method.
     */
    @Test
    public void testVisitLibraryMethod_doesNotModifyDescriptorMap() {
        // Arrange
        assertTrue(descriptorMap.isEmpty(), "Descriptor map should be empty initially");

        // Act
        fixer.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertTrue(descriptorMap.isEmpty(), "Descriptor map should remain empty");
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times without issues.
     * Multiple calls should all be no-ops.
     */
    @Test
    public void testVisitLibraryMethod_multipleCallsSucceed() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitLibraryMethod(libraryClass, libraryMethod);
            fixer.visitLibraryMethod(libraryClass, libraryMethod);
            fixer.visitLibraryMethod(libraryClass, libraryMethod);
        }, "Multiple calls to visitLibraryMethod should not throw any exception");
    }

    /**
     * Tests that visitLibraryMethod works with different library method instances.
     * The method should handle any LibraryMethod instance.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentMethods() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitLibraryMethod(libraryClass, method1);
            fixer.visitLibraryMethod(libraryClass, method2);
            fixer.visitLibraryMethod(libraryClass, method3);
        }, "visitLibraryMethod should work with different method instances");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryMethod works with different library class instances.
     * The method should handle any LibraryClass instance.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentClasses() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryClass class3 = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitLibraryMethod(class1, libraryMethod);
            fixer.visitLibraryMethod(class2, libraryMethod);
            fixer.visitLibraryMethod(class3, libraryMethod);
        }, "visitLibraryMethod should work with different class instances");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryMethod does not modify the method's state.
     * Since no member name operations occur on library methods, any pre-existing state should be unchanged.
     */
    @Test
    public void testVisitLibraryMethod_doesNotModifyMethodState() {
        // Arrange - Set a new member name on the library method
        MemberObfuscator.setNewMemberName(libraryMethod, "someObfuscatedName");
        String nameBefore = MemberObfuscator.newMemberName(libraryMethod);

        // Act
        fixer.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - The name should remain unchanged
        String nameAfter = MemberObfuscator.newMemberName(libraryMethod);
        assertEquals(nameBefore, nameAfter, "Library method's new member name should remain unchanged");
    }

    /**
     * Tests that visitLibraryMethod works correctly with aggressive overloading enabled.
     * The aggressive overloading setting should have no effect on library methods.
     */
    @Test
    public void testVisitLibraryMethod_withAggressiveOverloadingEnabled() {
        // Arrange
        MemberNameConflictFixer aggressiveFixer = new MemberNameConflictFixer(
            true, // aggressive overloading enabled
            descriptorMap,
            null,
            memberObfuscator
        );

        // Act & Assert
        assertDoesNotThrow(() -> aggressiveFixer.visitLibraryMethod(libraryClass, libraryMethod),
            "visitLibraryMethod should work with aggressive overloading enabled");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryMethod works correctly with aggressive overloading disabled.
     * The aggressive overloading setting should have no effect on library methods.
     */
    @Test
    public void testVisitLibraryMethod_withAggressiveOverloadingDisabled() {
        // Arrange
        MemberNameConflictFixer conservativeFixer = new MemberNameConflictFixer(
            false, // aggressive overloading disabled
            descriptorMap,
            null,
            memberObfuscator
        );

        // Act & Assert
        assertDoesNotThrow(() -> conservativeFixer.visitLibraryMethod(libraryClass, libraryMethod),
            "visitLibraryMethod should work with aggressive overloading disabled");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryMethod works with a populated descriptor map.
     * Even with existing mappings, library methods should not be processed.
     */
    @Test
    public void testVisitLibraryMethod_withPopulatedDescriptorMap() {
        // Arrange - Populate the descriptor map
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("newName1", "oldName1");
        nameMap.put("newName2", "oldName2");
        descriptorMap.put("()V", nameMap);
        descriptorMap.put("(I)V", new HashMap<>());

        // Act
        fixer.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - Map should remain unchanged
        assertEquals(2, descriptorMap.size(), "Descriptor map size should remain unchanged");
        assertEquals(2, descriptorMap.get("()V").size(), "Name map should remain unchanged");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that visitLibraryMethod works when warning printer is null.
     * Null warning printer should not cause any issues for library methods.
     */
    @Test
    public void testVisitLibraryMethod_withNullWarningPrinter() {
        // Arrange
        MemberNameConflictFixer fixerWithNullWarnings = new MemberNameConflictFixer(
            true,
            descriptorMap,
            null, // null warning printer
            memberObfuscator
        );

        // Act & Assert
        assertDoesNotThrow(() -> fixerWithNullWarnings.visitLibraryMethod(libraryClass, libraryMethod),
            "visitLibraryMethod should work with null warning printer");

        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that multiple fixers can visit the same library method without interference.
     * Each fixer should independently do nothing.
     */
    @Test
    public void testVisitLibraryMethod_withMultipleFixers() {
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
        fixer1.visitLibraryMethod(libraryClass, libraryMethod);
        fixer2.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        verifyNoInteractions(obfuscator1);
        verifyNoInteractions(obfuscator2);
    }

    /**
     * Tests that visitLibraryMethod completes quickly as a no-op operation.
     * Since it does nothing, it should execute very efficiently.
     */
    @Test
    public void testVisitLibraryMethod_isEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        fixer.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 1_000_000L, // Less than 1 millisecond
            "visitLibraryMethod should complete very quickly (took " + duration + " ns)");
    }

    /**
     * Tests that visitLibraryMethod can be called in sequence with other visitor methods.
     * This verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitLibraryMethod_sequenceWithOtherVisitorMethods() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        // Act & Assert - Call visitLibraryMethod in sequence
        assertDoesNotThrow(() -> {
            fixer.visitLibraryMethod(libraryClass, method1);
            fixer.visitLibraryMethod(libraryClass, method2);
            fixer.visitLibraryMethod(libraryClass, libraryMethod);
        }, "visitLibraryMethod should work correctly in sequence");

        verifyNoInteractions(memberObfuscator);
    }
}
