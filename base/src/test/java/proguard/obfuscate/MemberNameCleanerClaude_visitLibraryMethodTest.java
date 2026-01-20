package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MemberNameCleaner#visitLibraryMethod(LibraryClass, LibraryMethod)}.
 *
 * The visitLibraryMethod method clears the new name of a library method by setting it to null.
 * This is part of the member name cleaning process during obfuscation.
 */
public class MemberNameCleanerClaude_visitLibraryMethodTest {

    private MemberNameCleaner memberNameCleaner;
    private LibraryClass libraryClass;
    private LibraryMethod libraryMethod;

    @BeforeEach
    public void setUp() {
        memberNameCleaner = new MemberNameCleaner();
        libraryClass = new LibraryClass();
        libraryMethod = new LibraryMethod();
    }

    /**
     * Tests that visitLibraryMethod clears the new member name by setting it to null.
     * This is the primary behavior - clearing any previously set obfuscated name.
     */
    @Test
    public void testVisitLibraryMethod_clearsNewMemberName() {
        // Arrange - Set a new member name first
        MemberObfuscator.setNewMemberName(libraryMethod, "obfuscatedName");

        // Verify it was set
        assertEquals("obfuscatedName", MemberObfuscator.newMemberName(libraryMethod));

        // Act - Visit the method to clear the name
        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - The new member name should be null
        assertNull(MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works correctly when the method has no new name set.
     * Should handle null gracefully.
     */
    @Test
    public void testVisitLibraryMethod_whenNoNewNameSet() {
        // Arrange - libraryMethod has no new name set (null by default)
        assertNull(MemberObfuscator.newMemberName(libraryMethod));

        // Act
        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - Should still be null (no exception thrown)
        assertNull(MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitLibraryMethod_doesNotThrowException() {
        // Arrange
        MemberObfuscator.setNewMemberName(libraryMethod, "someName");

        // Act & Assert
        assertDoesNotThrow(() -> memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times on the same method.
     * Each call should leave the new name as null.
     */
    @Test
    public void testVisitLibraryMethod_calledMultipleTimes() {
        // Arrange
        MemberObfuscator.setNewMemberName(libraryMethod, "name1");

        // Act - Call multiple times
        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);
        assertNull(MemberObfuscator.newMemberName(libraryMethod));

        // Set a new name again
        MemberObfuscator.setNewMemberName(libraryMethod, "name2");
        assertEquals("name2", MemberObfuscator.newMemberName(libraryMethod));

        // Call again
        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);
        assertNull(MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with different LibraryMethod instances.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentMethods() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        MemberObfuscator.setNewMemberName(method1, "name1");
        MemberObfuscator.setNewMemberName(method2, "name2");
        MemberObfuscator.setNewMemberName(method3, "name3");

        // Act
        memberNameCleaner.visitLibraryMethod(libraryClass, method1);
        memberNameCleaner.visitLibraryMethod(libraryClass, method2);
        memberNameCleaner.visitLibraryMethod(libraryClass, method3);

        // Assert - All should have null names
        assertNull(MemberObfuscator.newMemberName(method1));
        assertNull(MemberObfuscator.newMemberName(method2));
        assertNull(MemberObfuscator.newMemberName(method3));
    }

    /**
     * Tests that visitLibraryMethod works with different LibraryClass instances.
     * The LibraryClass parameter is passed but not used in the current implementation.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentClasses() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryClass class3 = new LibraryClass();

        MemberObfuscator.setNewMemberName(libraryMethod, "methodName");

        // Act - Visit with different classes
        memberNameCleaner.visitLibraryMethod(class1, libraryMethod);

        // Assert - Name should be cleared regardless of which class was passed
        assertNull(MemberObfuscator.newMemberName(libraryMethod));

        // Set again and test with another class
        MemberObfuscator.setNewMemberName(libraryMethod, "anotherName");
        memberNameCleaner.visitLibraryMethod(class2, libraryMethod);
        assertNull(MemberObfuscator.newMemberName(libraryMethod));

        // Set again and test with third class
        MemberObfuscator.setNewMemberName(libraryMethod, "thirdName");
        memberNameCleaner.visitLibraryMethod(class3, libraryMethod);
        assertNull(MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that the same MemberNameCleaner instance can be reused for multiple methods.
     */
    @Test
    public void testVisitLibraryMethod_reuseSameCleaner() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        MemberObfuscator.setNewMemberName(method1, "first");
        MemberObfuscator.setNewMemberName(method2, "second");

        // Act - Use same cleaner for both methods
        memberNameCleaner.visitLibraryMethod(libraryClass, method1);
        memberNameCleaner.visitLibraryMethod(libraryClass, method2);

        // Assert
        assertNull(MemberObfuscator.newMemberName(method1));
        assertNull(MemberObfuscator.newMemberName(method2));
    }

    /**
     * Tests that visitLibraryMethod correctly clears names set via setFixedNewMemberName.
     * Fixed names are special markers that indicate the name should not be changed,
     * but the cleaner should still clear them.
     */
    @Test
    public void testVisitLibraryMethod_clearsFixedNewMemberName() {
        // Arrange - Set a fixed new member name
        MemberObfuscator.setFixedNewMemberName(libraryMethod, "fixedName");

        // Verify it was set and is marked as fixed
        assertEquals("fixedName", MemberObfuscator.newMemberName(libraryMethod));
        assertTrue(MemberObfuscator.hasFixedNewMemberName(libraryMethod));

        // Act
        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - The new member name should be null
        assertNull(MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that multiple MemberNameCleaner instances work independently.
     */
    @Test
    public void testVisitLibraryMethod_multipleCleanersIndependent() {
        // Arrange
        MemberNameCleaner cleaner1 = new MemberNameCleaner();
        MemberNameCleaner cleaner2 = new MemberNameCleaner();

        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        MemberObfuscator.setNewMemberName(method1, "name1");
        MemberObfuscator.setNewMemberName(method2, "name2");

        // Act
        cleaner1.visitLibraryMethod(libraryClass, method1);
        cleaner2.visitLibraryMethod(libraryClass, method2);

        // Assert - Both cleaners should work correctly
        assertNull(MemberObfuscator.newMemberName(method1));
        assertNull(MemberObfuscator.newMemberName(method2));
    }

    /**
     * Tests that visitLibraryMethod works correctly in a sequence of operations.
     */
    @Test
    public void testVisitLibraryMethod_sequentialOperations() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        MemberObfuscator.setNewMemberName(method1, "a");
        MemberObfuscator.setNewMemberName(method2, "b");
        MemberObfuscator.setNewMemberName(method3, "c");

        // Act - Process in sequence
        memberNameCleaner.visitLibraryMethod(libraryClass, method1);
        assertNull(MemberObfuscator.newMemberName(method1));

        memberNameCleaner.visitLibraryMethod(libraryClass, method2);
        assertNull(MemberObfuscator.newMemberName(method2));

        memberNameCleaner.visitLibraryMethod(libraryClass, method3);
        assertNull(MemberObfuscator.newMemberName(method3));
    }

    /**
     * Tests that visitLibraryMethod is consistent - calling it multiple times
     * with the same parameters produces the same result.
     */
    @Test
    public void testVisitLibraryMethod_isConsistent() {
        // Arrange
        MemberObfuscator.setNewMemberName(libraryMethod, "testName");

        // Act - Call multiple times
        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);
        assertNull(MemberObfuscator.newMemberName(libraryMethod));

        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);
        assertNull(MemberObfuscator.newMemberName(libraryMethod));

        memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);
        assertNull(MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works as part of the MemberVisitor interface.
     */
    @Test
    public void testVisitLibraryMethod_asMemberVisitor() {
        // Arrange
        MemberObfuscator.setNewMemberName(libraryMethod, "visitorTest");
        proguard.classfile.visitor.MemberVisitor visitor = memberNameCleaner;

        // Act - Call through the interface
        visitor.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertNull(MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests the integration of visitLibraryMethod with the overall cleaning workflow.
     */
    @Test
    public void testVisitLibraryMethod_integrationWorkflow() {
        // Arrange - Simulate a workflow where methods are obfuscated then cleaned
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        // Simulate obfuscation phase
        MemberObfuscator.setNewMemberName(method1, "a");
        MemberObfuscator.setNewMemberName(method2, "b");

        assertEquals("a", MemberObfuscator.newMemberName(method1));
        assertEquals("b", MemberObfuscator.newMemberName(method2));

        // Act - Cleaning phase
        memberNameCleaner.visitLibraryMethod(libraryClass, method1);
        memberNameCleaner.visitLibraryMethod(libraryClass, method2);

        // Assert - All names should be cleared
        assertNull(MemberObfuscator.newMemberName(method1));
        assertNull(MemberObfuscator.newMemberName(method2));
    }

    /**
     * Tests that clearing one method's name doesn't affect other methods.
     */
    @Test
    public void testVisitLibraryMethod_doesNotAffectOtherMethods() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        MemberObfuscator.setNewMemberName(method1, "name1");
        MemberObfuscator.setNewMemberName(method2, "name2");
        MemberObfuscator.setNewMemberName(method3, "name3");

        // Act - Clear only method1
        memberNameCleaner.visitLibraryMethod(libraryClass, method1);

        // Assert - Only method1 should be cleared
        assertNull(MemberObfuscator.newMemberName(method1));
        assertEquals("name2", MemberObfuscator.newMemberName(method2));
        assertEquals("name3", MemberObfuscator.newMemberName(method3));
    }

    /**
     * Tests that visitLibraryMethod works correctly when interleaved with name setting operations.
     */
    @Test
    public void testVisitLibraryMethod_interleavedWithNameSetting() {
        // Arrange
        LibraryMethod method = new LibraryMethod();

        // Act & Assert - Interleave operations
        MemberObfuscator.setNewMemberName(method, "name1");
        assertEquals("name1", MemberObfuscator.newMemberName(method));

        memberNameCleaner.visitLibraryMethod(libraryClass, method);
        assertNull(MemberObfuscator.newMemberName(method));

        MemberObfuscator.setNewMemberName(method, "name2");
        assertEquals("name2", MemberObfuscator.newMemberName(method));

        memberNameCleaner.visitLibraryMethod(libraryClass, method);
        assertNull(MemberObfuscator.newMemberName(method));

        MemberObfuscator.setNewMemberName(method, "name3");
        assertEquals("name3", MemberObfuscator.newMemberName(method));

        memberNameCleaner.visitLibraryMethod(libraryClass, method);
        assertNull(MemberObfuscator.newMemberName(method));
    }
}
