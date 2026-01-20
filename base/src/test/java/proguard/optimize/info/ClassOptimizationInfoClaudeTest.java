package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ClassOptimizationInfo}.
 *
 * This class stores optimization information that can be attached to a class during
 * ProGuard's optimization process. The tests cover all public methods including:
 * - Constructor and initialization
 * - Side effects management
 * - Various class analysis methods
 * - Static helper methods for setting and retrieving optimization info
 */
public class ClassOptimizationInfoClaudeTest {

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a ClassOptimizationInfo instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Assert
        assertNotNull(info, "ClassOptimizationInfo instance should be created");
    }

    /**
     * Tests that the constructor initializes hasNoSideEffects to false by default.
     */
    @Test
    public void testConstructor_initializesHasNoSideEffectsToFalse() {
        // Act
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Assert
        assertFalse(info.hasNoSideEffects(), "hasNoSideEffects should be false by default");
        assertTrue(info.hasSideEffects(), "hasSideEffects should be true by default");
    }

    /**
     * Tests that the constructor initializes all conservative defaults correctly.
     */
    @Test
    public void testConstructor_initializesConservativeDefaults() {
        // Act
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Assert - conservative defaults (assume everything is true for safety)
        assertTrue(info.isKept(), "isKept should return true by default");
        assertTrue(info.containsConstructors(), "containsConstructors should return true by default");
        assertTrue(info.isInstantiated(), "isInstantiated should return true by default");
        assertTrue(info.isDotClassed(), "isDotClassed should return true by default");
        assertTrue(info.isInstanceofed(), "isInstanceofed should return true by default");
        assertTrue(info.isCaught(), "isCaught should return true by default");
        assertTrue(info.isEscaping(), "isEscaping should return true by default");
        assertTrue(info.hasSideEffects(), "hasSideEffects should return true by default");
        assertTrue(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should return true by default");
        assertTrue(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should return true by default");

        // Non-conservative defaults
        assertFalse(info.isSimpleEnum(), "isSimpleEnum should return false by default");
        assertFalse(info.mayBeMerged(), "mayBeMerged should return false by default");
        assertNull(info.getWrappedClass(), "getWrappedClass should return null by default");
        assertNull(info.getTargetClass(), "getTargetClass should return null by default");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        ClassOptimizationInfo info1 = new ClassOptimizationInfo();
        ClassOptimizationInfo info2 = new ClassOptimizationInfo();
        ClassOptimizationInfo info3 = new ClassOptimizationInfo();

        // Assert
        assertNotNull(info1, "First info should be created");
        assertNotNull(info2, "Second info should be created");
        assertNotNull(info3, "Third info should be created");
        assertNotSame(info1, info2, "First and second info should be different instances");
        assertNotSame(info2, info3, "Second and third info should be different instances");
        assertNotSame(info1, info3, "First and third info should be different instances");
    }

    // =========================================================================
    // Side Effects Management Tests
    // =========================================================================

    /**
     * Tests setNoSideEffects method changes hasNoSideEffects to true.
     */
    @Test
    public void testSetNoSideEffects_changesHasNoSideEffectsToTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();
        assertFalse(info.hasNoSideEffects(), "Should initially have side effects");

        // Act
        info.setNoSideEffects();

        // Assert
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should be true after setNoSideEffects");
    }

    /**
     * Tests hasNoSideEffects returns false initially.
     */
    @Test
    public void testHasNoSideEffects_returnsFalseInitially() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertFalse(info.hasNoSideEffects(), "hasNoSideEffects should be false initially");
    }

    /**
     * Tests hasNoSideEffects returns true after setNoSideEffects is called.
     */
    @Test
    public void testHasNoSideEffects_returnsTrueAfterSet() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();
        info.setNoSideEffects();

        // Act & Assert
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should return true after being set");
    }

    /**
     * Tests hasSideEffects returns opposite of hasNoSideEffects.
     */
    @Test
    public void testHasSideEffects_returnsOppositeOfHasNoSideEffects() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert - initial state
        assertTrue(info.hasSideEffects(), "hasSideEffects should be true initially");
        assertFalse(info.hasNoSideEffects(), "hasNoSideEffects should be false initially");

        // Act - set no side effects
        info.setNoSideEffects();

        // Assert - after setting
        assertFalse(info.hasSideEffects(), "hasSideEffects should be false after setNoSideEffects");
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should be true after setNoSideEffects");
    }

    /**
     * Tests that setNoSideEffects can be called multiple times.
     */
    @Test
    public void testSetNoSideEffects_canBeCalledMultipleTimes() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act - call multiple times
        info.setNoSideEffects();
        info.setNoSideEffects();
        info.setNoSideEffects();

        // Assert - should still be true
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should remain true after multiple calls");
        assertFalse(info.hasSideEffects(), "hasSideEffects should remain false after multiple calls");
    }

    /**
     * Tests that setNoSideEffects affects isInstanceofed behavior.
     */
    @Test
    public void testSetNoSideEffects_affectsIsInstanceofed() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert - initially
        assertTrue(info.isInstanceofed(), "isInstanceofed should be true initially");

        // Act - set no side effects
        info.setNoSideEffects();

        // Assert - after setting
        assertFalse(info.isInstanceofed(), "isInstanceofed should be false after setNoSideEffects");
    }

    /**
     * Tests that setNoSideEffects affects isDotClassed behavior.
     */
    @Test
    public void testSetNoSideEffects_affectsIsDotClassed() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert - initially
        assertTrue(info.isDotClassed(), "isDotClassed should be true initially");

        // Act - set no side effects
        info.setNoSideEffects();

        // Assert - after setting
        assertFalse(info.isDotClassed(), "isDotClassed should be false after setNoSideEffects");
    }

    // =========================================================================
    // Class Analysis Methods Tests
    // =========================================================================

    /**
     * Tests isKept always returns true (conservative default).
     */
    @Test
    public void testIsKept_alwaysReturnsTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertTrue(info.isKept(), "isKept should always return true");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertTrue(info.isKept(), "isKept should still return true after setNoSideEffects");
    }

    /**
     * Tests containsConstructors always returns true (conservative default).
     */
    @Test
    public void testContainsConstructors_alwaysReturnsTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertTrue(info.containsConstructors(), "containsConstructors should always return true");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertTrue(info.containsConstructors(), "containsConstructors should still return true after setNoSideEffects");
    }

    /**
     * Tests isInstantiated always returns true (conservative default).
     */
    @Test
    public void testIsInstantiated_alwaysReturnsTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertTrue(info.isInstantiated(), "isInstantiated should always return true");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertTrue(info.isInstantiated(), "isInstantiated should still return true after setNoSideEffects");
    }

    /**
     * Tests isInstanceofed returns true initially but false after setNoSideEffects.
     */
    @Test
    public void testIsInstanceofed_dependsOnHasNoSideEffects() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert - initially (hasNoSideEffects is false)
        assertTrue(info.isInstanceofed(), "isInstanceofed should return true when hasNoSideEffects is false");

        // Act - set no side effects
        info.setNoSideEffects();

        // Assert - after setting (hasNoSideEffects is true)
        assertFalse(info.isInstanceofed(), "isInstanceofed should return false when hasNoSideEffects is true");
    }

    /**
     * Tests isDotClassed returns true initially but false after setNoSideEffects.
     */
    @Test
    public void testIsDotClassed_dependsOnHasNoSideEffects() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert - initially (hasNoSideEffects is false)
        assertTrue(info.isDotClassed(), "isDotClassed should return true when hasNoSideEffects is false");

        // Act - set no side effects
        info.setNoSideEffects();

        // Assert - after setting (hasNoSideEffects is true)
        assertFalse(info.isDotClassed(), "isDotClassed should return false when hasNoSideEffects is true");
    }

    /**
     * Tests isCaught always returns true (conservative default).
     */
    @Test
    public void testIsCaught_alwaysReturnsTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertTrue(info.isCaught(), "isCaught should always return true");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertTrue(info.isCaught(), "isCaught should still return true after setNoSideEffects");
    }

    /**
     * Tests isSimpleEnum always returns false.
     */
    @Test
    public void testIsSimpleEnum_alwaysReturnsFalse() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertFalse(info.isSimpleEnum(), "isSimpleEnum should always return false");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertFalse(info.isSimpleEnum(), "isSimpleEnum should still return false after setNoSideEffects");
    }

    /**
     * Tests isEscaping always returns true (conservative default).
     */
    @Test
    public void testIsEscaping_alwaysReturnsTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertTrue(info.isEscaping(), "isEscaping should always return true");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertTrue(info.isEscaping(), "isEscaping should still return true after setNoSideEffects");
    }

    /**
     * Tests containsPackageVisibleMembers always returns true (conservative default).
     */
    @Test
    public void testContainsPackageVisibleMembers_alwaysReturnsTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertTrue(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should always return true");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertTrue(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should still return true after setNoSideEffects");
    }

    /**
     * Tests invokesPackageVisibleMembers always returns true (conservative default).
     */
    @Test
    public void testInvokesPackageVisibleMembers_alwaysReturnsTrue() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertTrue(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should always return true");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertTrue(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should still return true after setNoSideEffects");
    }

    /**
     * Tests mayBeMerged always returns false.
     */
    @Test
    public void testMayBeMerged_alwaysReturnsFalse() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertFalse(info.mayBeMerged(), "mayBeMerged should always return false");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertFalse(info.mayBeMerged(), "mayBeMerged should still return false after setNoSideEffects");
    }

    /**
     * Tests getWrappedClass always returns null.
     */
    @Test
    public void testGetWrappedClass_alwaysReturnsNull() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertNull(info.getWrappedClass(), "getWrappedClass should always return null");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertNull(info.getWrappedClass(), "getWrappedClass should still return null after setNoSideEffects");
    }

    /**
     * Tests getTargetClass always returns null.
     */
    @Test
    public void testGetTargetClass_alwaysReturnsNull() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert
        assertNull(info.getTargetClass(), "getTargetClass should always return null");

        // Even after setting no side effects
        info.setNoSideEffects();
        assertNull(info.getTargetClass(), "getTargetClass should still return null after setNoSideEffects");
    }

    // =========================================================================
    // Static Helper Methods Tests
    // =========================================================================

    /**
     * Tests setClassOptimizationInfo creates and attaches info to a ProgramClass.
     */
    @Test
    public void testSetClassOptimizationInfo_attachesToProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Assert
        Object processingInfo = programClass.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should not be null");
        assertInstanceOf(ClassOptimizationInfo.class, processingInfo, "Processing info should be ClassOptimizationInfo");
    }

    /**
     * Tests setClassOptimizationInfo creates and attaches info to a LibraryClass.
     */
    @Test
    public void testSetClassOptimizationInfo_attachesToLibraryClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Assert
        Object processingInfo = libraryClass.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should not be null");
        assertInstanceOf(ClassOptimizationInfo.class, processingInfo, "Processing info should be ClassOptimizationInfo");
    }

    /**
     * Tests setClassOptimizationInfo can be called multiple times (replaces previous info).
     */
    @Test
    public void testSetClassOptimizationInfo_canBeCalledMultipleTimes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call multiple times
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        ClassOptimizationInfo info1 = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        ClassOptimizationInfo info2 = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Assert - should get different instances
        assertNotNull(info1, "First info should not be null");
        assertNotNull(info2, "Second info should not be null");
        assertNotSame(info1, info2, "Multiple calls should create new instances");
    }

    /**
     * Tests getClassOptimizationInfo retrieves the info from a ProgramClass.
     */
    @Test
    public void testGetClassOptimizationInfo_retrievesFromProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Assert
        assertNotNull(info, "Retrieved info should not be null");
        assertInstanceOf(ClassOptimizationInfo.class, info, "Retrieved info should be ClassOptimizationInfo");
    }

    /**
     * Tests getClassOptimizationInfo retrieves the info from a LibraryClass.
     */
    @Test
    public void testGetClassOptimizationInfo_retrievesFromLibraryClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(libraryClass);

        // Assert
        assertNotNull(info, "Retrieved info should not be null");
        assertInstanceOf(ClassOptimizationInfo.class, info, "Retrieved info should be ClassOptimizationInfo");
    }

    /**
     * Tests getClassOptimizationInfo returns the same instance that was set.
     */
    @Test
    public void testGetClassOptimizationInfo_returnsSameInstance() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        ClassOptimizationInfo info1 = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        ClassOptimizationInfo info2 = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Assert
        assertSame(info1, info2, "Multiple retrievals should return the same instance");
    }

    /**
     * Tests that modifications to retrieved info are reflected in subsequent retrievals.
     */
    @Test
    public void testGetClassOptimizationInfo_modificationsArePersisted() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act - retrieve and modify
        ClassOptimizationInfo info1 = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertFalse(info1.hasNoSideEffects(), "Should initially have side effects");
        info1.setNoSideEffects();

        // Retrieve again
        ClassOptimizationInfo info2 = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Assert - modification should be visible
        assertTrue(info2.hasNoSideEffects(), "Modification should be visible in subsequent retrieval");
    }

    /**
     * Tests integration of setClassOptimizationInfo and getClassOptimizationInfo.
     */
    @Test
    public void testSetAndGetClassOptimizationInfo_integration() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - set and get
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Assert - verify it's a fresh instance with default values
        assertNotNull(info, "Retrieved info should not be null");
        assertFalse(info.hasNoSideEffects(), "New info should have side effects by default");
        assertTrue(info.isKept(), "New info should return true for isKept");
        assertNull(info.getWrappedClass(), "New info should return null for getWrappedClass");

        // Act - modify and verify
        info.setNoSideEffects();
        assertTrue(info.hasNoSideEffects(), "Modification should be applied");
    }

    // =========================================================================
    // Edge Cases and Integration Tests
    // =========================================================================

    /**
     * Tests that side effects flag correctly affects multiple dependent methods.
     */
    @Test
    public void testSideEffects_affectsMultipleMethods() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert - before setting no side effects
        assertTrue(info.hasSideEffects(), "hasSideEffects should be true");
        assertFalse(info.hasNoSideEffects(), "hasNoSideEffects should be false");
        assertTrue(info.isInstanceofed(), "isInstanceofed should be true");
        assertTrue(info.isDotClassed(), "isDotClassed should be true");

        // Act - set no side effects
        info.setNoSideEffects();

        // Assert - after setting no side effects
        assertFalse(info.hasSideEffects(), "hasSideEffects should be false");
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should be true");
        assertFalse(info.isInstanceofed(), "isInstanceofed should be false");
        assertFalse(info.isDotClassed(), "isDotClassed should be false");
    }

    /**
     * Tests that ClassOptimizationInfo works correctly when attached to multiple classes.
     */
    @Test
    public void testClassOptimizationInfo_worksWithMultipleClasses() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        LibraryClass class3 = new LibraryClass();

        // Act - set optimization info on all classes
        ClassOptimizationInfo.setClassOptimizationInfo(class1);
        ClassOptimizationInfo.setClassOptimizationInfo(class2);
        ClassOptimizationInfo.setClassOptimizationInfo(class3);

        // Act - modify one
        ClassOptimizationInfo info1 = ClassOptimizationInfo.getClassOptimizationInfo(class1);
        info1.setNoSideEffects();

        // Retrieve all
        ClassOptimizationInfo retrievedInfo1 = ClassOptimizationInfo.getClassOptimizationInfo(class1);
        ClassOptimizationInfo retrievedInfo2 = ClassOptimizationInfo.getClassOptimizationInfo(class2);
        ClassOptimizationInfo retrievedInfo3 = ClassOptimizationInfo.getClassOptimizationInfo(class3);

        // Assert - each class has independent info
        assertTrue(retrievedInfo1.hasNoSideEffects(), "Class1 should have no side effects");
        assertFalse(retrievedInfo2.hasNoSideEffects(), "Class2 should still have side effects");
        assertFalse(retrievedInfo3.hasNoSideEffects(), "Class3 should still have side effects");
    }

    /**
     * Tests that all methods can be called without throwing exceptions.
     */
    @Test
    public void testAllMethods_noExceptions() {
        // Arrange
        ClassOptimizationInfo info = new ClassOptimizationInfo();

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> {
            info.setNoSideEffects();
            info.hasNoSideEffects();
            info.isKept();
            info.containsConstructors();
            info.isInstantiated();
            info.isInstanceofed();
            info.isDotClassed();
            info.isCaught();
            info.isSimpleEnum();
            info.isEscaping();
            info.hasSideEffects();
            info.containsPackageVisibleMembers();
            info.invokesPackageVisibleMembers();
            info.mayBeMerged();
            info.getWrappedClass();
            info.getTargetClass();
        }, "All methods should execute without exceptions");
    }

    /**
     * Tests thread safety of creating multiple ClassOptimizationInfo instances.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int instancesPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final ClassOptimizationInfo[][] results = new ClassOptimizationInfo[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new ClassOptimizationInfo();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < instancesPerThread; j++) {
                assertNotNull(results[i][j], "Instance [" + i + "][" + j + "] should be created");
                assertFalse(results[i][j].hasNoSideEffects(), "Instance should have default state");
            }
        }
    }

    /**
     * Tests that ClassOptimizationInfo instances are independent.
     */
    @Test
    public void testInstances_areIndependent() {
        // Arrange
        ClassOptimizationInfo info1 = new ClassOptimizationInfo();
        ClassOptimizationInfo info2 = new ClassOptimizationInfo();

        // Act - modify first instance
        info1.setNoSideEffects();

        // Assert - second instance should not be affected
        assertTrue(info1.hasNoSideEffects(), "First instance should have no side effects");
        assertFalse(info2.hasNoSideEffects(), "Second instance should still have side effects");
        assertFalse(info1.isInstanceofed(), "First instance isInstanceofed should be false");
        assertTrue(info2.isInstanceofed(), "Second instance isInstanceofed should still be true");
    }
}
