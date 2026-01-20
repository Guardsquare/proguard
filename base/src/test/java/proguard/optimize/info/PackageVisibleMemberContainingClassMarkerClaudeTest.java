package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.AccessConstants;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PackageVisibleMemberContainingClassMarker}.
 *
 * The PackageVisibleMemberContainingClassMarker is a visitor that marks classes containing
 * package-visible members. A class is marked if:
 * 1. The class itself is not public (package-private class)
 * 2. The class contains fields or methods that are package-visible (neither private nor public)
 *
 * Key behavior:
 * - visitAnyClass: Checks if class is public. If not public, marks the class.
 *   If public, visits all fields and methods to check for package-visible members.
 * - visitAnyMember: Checks if member is package-visible (neither private nor public).
 *   If so, marks the containing class.
 * - containsPackageVisibleMembers: Static method to query if a class has been marked.
 *
 * These tests verify:
 * 1. Constructor works correctly
 * 2. Non-public classes are marked
 * 3. Public classes with package-visible members are marked
 * 4. Public classes with only public/private members are not marked
 * 5. The static query method returns correct results
 */
public class PackageVisibleMemberContainingClassMarkerClaudeTest {

    private PackageVisibleMemberContainingClassMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new PackageVisibleMemberContainingClassMarker();
    }

    /**
     * Tests that the constructor creates a valid marker instance.
     */
    @Test
    public void testConstructor_createsValidInstance() {
        // Act
        PackageVisibleMemberContainingClassMarker newMarker = new PackageVisibleMemberContainingClassMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a valid instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_areIndependent() {
        // Act
        PackageVisibleMemberContainingClassMarker marker1 = new PackageVisibleMemberContainingClassMarker();
        PackageVisibleMemberContainingClassMarker marker2 = new PackageVisibleMemberContainingClassMarker();

        // Assert
        assertNotNull(marker1);
        assertNotNull(marker2);
        assertNotSame(marker1, marker2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests visitAnyClass with a non-public (package-private) class.
     * Non-public classes should be marked as containing package-visible members.
     */
    @Test
    public void testVisitAnyClass_withPackagePrivateClass_marksClass() {
        // Arrange - Create a package-private class (no PUBLIC flag)
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // No access flags = package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Package-private class should be marked as containing package-visible members");
    }

    /**
     * Tests visitAnyClass with a public class that has no members.
     * Public classes without members should not be marked.
     */
    @Test
    public void testVisitAnyClass_withPublicClassNoMembers_doesNotMark() {
        // Arrange - Create a public class with no members
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;
        programClass.u2fieldsCount = 0;
        programClass.fields = new ProgramField[0];
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with no members should not be marked");
    }

    /**
     * Tests visitAnyClass with a public class that has only public fields.
     * Should not be marked.
     */
    @Test
    public void testVisitAnyClass_withPublicClassPublicField_doesNotMark() {
        // Arrange - Create a public class with a public field
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField publicField = new ProgramField();
        publicField.u2accessFlags = AccessConstants.PUBLIC;

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { publicField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with only public fields should not be marked");
    }

    /**
     * Tests visitAnyClass with a public class that has only private fields.
     * Should not be marked.
     */
    @Test
    public void testVisitAnyClass_withPublicClassPrivateField_doesNotMark() {
        // Arrange - Create a public class with a private field
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField privateField = new ProgramField();
        privateField.u2accessFlags = AccessConstants.PRIVATE;

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { privateField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with only private fields should not be marked");
    }

    /**
     * Tests visitAnyClass with a public class that has a package-visible field.
     * Should be marked.
     */
    @Test
    public void testVisitAnyClass_withPublicClassPackageVisibleField_marksClass() {
        // Arrange - Create a public class with a package-visible field
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0; // No PUBLIC or PRIVATE = package-visible

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { packageField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with package-visible field should be marked");
    }

    /**
     * Tests visitAnyClass with a public class that has a package-visible method.
     * Should be marked.
     */
    @Test
    public void testVisitAnyClass_withPublicClassPackageVisibleMethod_marksClass() {
        // Arrange - Create a public class with a package-visible method
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramMethod packageMethod = new ProgramMethod();
        packageMethod.u2accessFlags = 0; // No PUBLIC or PRIVATE = package-visible

        programClass.u2fieldsCount = 0;
        programClass.fields = new ProgramField[0];
        programClass.u2methodsCount = 1;
        programClass.methods = new ProgramMethod[] { packageMethod };
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with package-visible method should be marked");
    }

    /**
     * Tests visitAnyClass with a public class that has multiple members with mixed access.
     * Should be marked if any member is package-visible.
     */
    @Test
    public void testVisitAnyClass_withPublicClassMixedAccess_marksClass() {
        // Arrange - Create a public class with mixed access members
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField publicField = new ProgramField();
        publicField.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField privateField = new ProgramField();
        privateField.u2accessFlags = AccessConstants.PRIVATE;

        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0; // package-visible

        programClass.u2fieldsCount = 3;
        programClass.fields = new ProgramField[] { publicField, privateField, packageField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with at least one package-visible member should be marked");
    }

    /**
     * Tests visitAnyClass with a public class that has protected field.
     * Protected is not private and not public (at the flag level), so should be marked.
     */
    @Test
    public void testVisitAnyClass_withPublicClassProtectedField_marksClass() {
        // Arrange - Create a public class with a protected field
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField protectedField = new ProgramField();
        protectedField.u2accessFlags = AccessConstants.PROTECTED;

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { protectedField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with protected field should be marked (protected is neither private nor public)");
    }

    /**
     * Tests visitAnyMember with a package-visible field.
     * Should mark the class.
     */
    @Test
    public void testVisitAnyMember_withPackageVisibleField_marksClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0; // package-visible

        // Act
        marker.visitAnyMember(programClass, packageField);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should be marked when visiting package-visible member");
    }

    /**
     * Tests visitAnyMember with a public field.
     * Should not mark the class.
     */
    @Test
    public void testVisitAnyMember_withPublicField_doesNotMark() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramField publicField = new ProgramField();
        publicField.u2accessFlags = AccessConstants.PUBLIC;

        // Act
        marker.visitAnyMember(programClass, publicField);

        // Assert
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should not be marked when visiting public member");
    }

    /**
     * Tests visitAnyMember with a private field.
     * Should not mark the class.
     */
    @Test
    public void testVisitAnyMember_withPrivateField_doesNotMark() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramField privateField = new ProgramField();
        privateField.u2accessFlags = AccessConstants.PRIVATE;

        // Act
        marker.visitAnyMember(programClass, privateField);

        // Assert
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should not be marked when visiting private member");
    }

    /**
     * Tests visitAnyMember with a protected field.
     * Protected is neither private nor public, so should mark.
     */
    @Test
    public void testVisitAnyMember_withProtectedField_marksClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramField protectedField = new ProgramField();
        protectedField.u2accessFlags = AccessConstants.PROTECTED;

        // Act
        marker.visitAnyMember(programClass, protectedField);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should be marked when visiting protected member");
    }

    /**
     * Tests visitAnyMember with a method that is public and private (unusual but testing the logic).
     * If both flags are set, should not mark (the condition checks if NEITHER is set).
     */
    @Test
    public void testVisitAnyMember_withPublicAndPrivateFlags_doesNotMark() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.PRIVATE;

        // Act
        marker.visitAnyMember(programClass, method);

        // Assert
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should not be marked when member has both public and private flags");
    }

    /**
     * Tests containsPackageVisibleMembers with a class that has not been marked.
     * Should return false.
     */
    @Test
    public void testContainsPackageVisibleMembers_withUnmarkedClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        boolean result = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);

        // Assert
        assertFalse(result, "Unmarked class should return false");
    }

    /**
     * Tests containsPackageVisibleMembers with a class that has been marked.
     * Should return true.
     */
    @Test
    public void testContainsPackageVisibleMembers_withMarkedClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Mark the class
        marker.visitAnyClass(programClass);

        // Act
        boolean result = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);

        // Assert
        assertTrue(result, "Marked class should return true");
    }

    /**
     * Tests containsPackageVisibleMembers is independent across different classes.
     */
    @Test
    public void testContainsPackageVisibleMembers_independentAcrossClasses() {
        // Arrange
        ProgramClass markedClass = new ProgramClass();
        markedClass.u2accessFlags = 0; // package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(markedClass);

        ProgramClass unmarkedClass = new ProgramClass();
        unmarkedClass.u2accessFlags = AccessConstants.PUBLIC;
        unmarkedClass.u2fieldsCount = 0;
        unmarkedClass.fields = new ProgramField[0];
        unmarkedClass.u2methodsCount = 0;
        unmarkedClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unmarkedClass);

        // Mark only one class
        marker.visitAnyClass(markedClass);
        marker.visitAnyClass(unmarkedClass);

        // Act & Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(markedClass),
                "Marked class should return true");
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(unmarkedClass),
                "Unmarked class should return false");
    }

    /**
     * Tests that visitAnyClass can be called multiple times on the same class.
     * The marking should be idempotent.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_idempotent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);

        // Assert - should still be marked
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should remain marked after multiple visits");
    }

    /**
     * Tests that visitAnyMember can be called multiple times on the same class.
     * The marking should be idempotent.
     */
    @Test
    public void testVisitAnyMember_calledMultipleTimes_idempotent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0;

        // Act - call multiple times
        marker.visitAnyMember(programClass, packageField);
        marker.visitAnyMember(programClass, packageField);
        marker.visitAnyMember(programClass, packageField);

        // Assert - should still be marked
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should remain marked after multiple member visits");
    }

    /**
     * Tests visitAnyClass with a class that has public static final field.
     * Public members should not cause marking.
     */
    @Test
    public void testVisitAnyClass_withPublicStaticFinalField_doesNotMark() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField publicStaticFinalField = new ProgramField();
        publicStaticFinalField.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL;

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { publicStaticFinalField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with public static final field should not be marked");
    }

    /**
     * Tests visitAnyClass with a class that has static package-visible field.
     * Static package-visible members should still cause marking.
     */
    @Test
    public void testVisitAnyClass_withStaticPackageVisibleField_marksClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField staticPackageField = new ProgramField();
        staticPackageField.u2accessFlags = AccessConstants.STATIC; // static but not public or private

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { staticPackageField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with static package-visible field should be marked");
    }

    /**
     * Tests visitAnyClass with multiple marker instances on the same class.
     * Each marker should independently mark the class.
     */
    @Test
    public void testVisitAnyClass_withMultipleMarkers_allMarkIndependently() {
        // Arrange
        PackageVisibleMemberContainingClassMarker marker1 = new PackageVisibleMemberContainingClassMarker();
        PackageVisibleMemberContainingClassMarker marker2 = new PackageVisibleMemberContainingClassMarker();

        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker1.visitAnyClass(programClass);

        // Assert - marked by first marker
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should be marked by first marker");

        // Act - second marker visits the same class
        marker2.visitAnyClass(programClass);

        // Assert - still marked (idempotent)
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should remain marked after second marker visit");
    }

    /**
     * Tests visitAnyClass with final class that is package-private.
     * Access modifiers other than visibility should not affect the marking logic.
     */
    @Test
    public void testVisitAnyClass_withFinalPackagePrivateClass_marksClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.FINAL; // final but not public
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Final package-private class should be marked");
    }

    /**
     * Tests visitAnyClass with abstract class that is package-private.
     */
    @Test
    public void testVisitAnyClass_withAbstractPackagePrivateClass_marksClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.ABSTRACT; // abstract but not public
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Abstract package-private class should be marked");
    }

    /**
     * Tests visitAnyClass with interface that is package-private.
     */
    @Test
    public void testVisitAnyClass_withPackagePrivateInterface_marksClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.INTERFACE; // interface but not public
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Package-private interface should be marked");
    }

    /**
     * Tests visitAnyMember with a method marked as synchronized but package-visible.
     */
    @Test
    public void testVisitAnyMember_withSynchronizedPackageVisibleMethod_marksClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramMethod syncMethod = new ProgramMethod();
        syncMethod.u2accessFlags = AccessConstants.SYNCHRONIZED; // synchronized but not public or private

        // Act
        marker.visitAnyMember(programClass, syncMethod);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Class should be marked when visiting synchronized package-visible method");
    }

    /**
     * Tests that the marker works with the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - use as ClassVisitor interface
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Marker should work correctly through ClassVisitor interface");
    }

    /**
     * Tests that visitAnyMember works with the MemberVisitor interface.
     */
    @Test
    public void testVisitAnyMember_throughMemberVisitorInterface() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0;

        // Act - use as MemberVisitor interface
        marker.visitAnyMember(programClass, packageField);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Marker should work correctly through MemberVisitor interface");
    }

    /**
     * Tests containsPackageVisibleMembers with class that has no optimization info.
     * Should use default value from base ClassOptimizationInfo (returns true).
     */
    @Test
    public void testContainsPackageVisibleMembers_withNoOptimizationInfo_returnsDefault() {
        // Arrange - Create a class without optimization info
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        boolean result = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);

        // Assert
        assertTrue(result, "Class with base optimization info should return default value (true)");
    }

    /**
     * Tests that marking is persistent across queries.
     */
    @Test
    public void testContainsPackageVisibleMembers_markingIsPersistent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // package-private
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        marker.visitAnyClass(programClass);

        // Act - query multiple times
        boolean result1 = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);
        boolean result2 = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);
        boolean result3 = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);

        // Assert - all queries should return true
        assertTrue(result1, "First query should return true");
        assertTrue(result2, "Second query should return true");
        assertTrue(result3, "Third query should return true");
    }

    /**
     * Tests visitAnyClass does not throw with null optimization info initially set.
     * The marker should set optimization info if needed.
     */
    @Test
    public void testVisitAnyClass_withNullOptimizationInfo_throwsException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // package-private
        // Note: Not setting optimization info

        // Act & Assert - this will throw NullPointerException because marker expects optimization info
        assertThrows(NullPointerException.class, () -> {
            marker.visitAnyClass(programClass);
        }, "Marker should throw NullPointerException if optimization info is not set");
    }

    /**
     * Tests visitAnyMember does not throw with null optimization info.
     */
    @Test
    public void testVisitAnyMember_withNullOptimizationInfo_throwsException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0;
        // Note: Not setting optimization info

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitAnyMember(programClass, packageField);
        }, "Marker should throw NullPointerException if optimization info is not set");
    }

    /**
     * Tests edge case where class is both marked by visitAnyClass and visitAnyMember.
     */
    @Test
    public void testMarking_byBothVisitMethods_classIsMarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField packageField = new ProgramField();
        packageField.u2accessFlags = 0;

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { packageField };
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - first mark through visitAnyClass
        marker.visitAnyClass(programClass);
        boolean markedByVisitClass = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);

        // Then mark through visitAnyMember
        marker.visitAnyMember(programClass, packageField);
        boolean markedByVisitMember = PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass);

        // Assert
        assertTrue(markedByVisitClass, "Class should be marked by visitAnyClass");
        assertTrue(markedByVisitMember, "Class should remain marked after visitAnyMember");
    }

    /**
     * Tests visitAnyClass with a public class containing both fields and methods.
     * Should check all members and mark if any is package-visible.
     */
    @Test
    public void testVisitAnyClass_withPublicClassMultipleMembers_checksAll() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField publicField = new ProgramField();
        publicField.u2accessFlags = AccessConstants.PUBLIC;

        ProgramMethod packageMethod = new ProgramMethod();
        packageMethod.u2accessFlags = 0; // package-visible

        programClass.u2fieldsCount = 1;
        programClass.fields = new ProgramField[] { publicField };
        programClass.u2methodsCount = 1;
        programClass.methods = new ProgramMethod[] { packageMethod };
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(programClass),
                "Public class with package-visible method should be marked");
    }
}
