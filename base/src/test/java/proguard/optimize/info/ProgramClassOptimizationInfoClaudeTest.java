package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramClassOptimizationInfo}.
 *
 * This class stores optimization information that can be attached to a class during
 * ProGuard's optimization process. The tests cover all public methods including:
 * - Constructor and initialization
 * - Boolean flag setters and getters for various optimization states
 * - Clazz reference setters and getters (wrapped class, target class)
 * - merge() method for combining optimization info
 * - Static helper methods for setting and retrieving optimization info
 */
public class ProgramClassOptimizationInfoClaudeTest {

    private ProgramClassOptimizationInfo info;
    private Clazz mockClazz;
    private Clazz mockWrappedClazz;
    private Clazz mockTargetClazz;

    @BeforeEach
    public void setUp() {
        info = new ProgramClassOptimizationInfo();
        mockClazz = mock(Clazz.class);
        mockWrappedClazz = mock(Clazz.class);
        mockTargetClazz = mock(Clazz.class);

        when(mockClazz.getName()).thenReturn("com/example/TestClass");
        when(mockWrappedClazz.getName()).thenReturn("com/example/WrappedClass");
        when(mockTargetClazz.getName()).thenReturn("com/example/TargetClass");
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a ProgramClassOptimizationInfo instance
     * with all boolean flags initialized to their default values.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        ProgramClassOptimizationInfo newInfo = new ProgramClassOptimizationInfo();

        // Assert
        assertNotNull(newInfo, "ProgramClassOptimizationInfo instance should be created");
    }

    /**
     * Tests that the constructor initializes all boolean fields to their default values.
     */
    @Test
    public void testConstructor_initializesDefaultValues() {
        // Act
        ProgramClassOptimizationInfo newInfo = new ProgramClassOptimizationInfo();

        // Assert - verify default values
        assertFalse(newInfo.isKept(), "isKept should return false by default");
        assertFalse(newInfo.containsConstructors(), "containsConstructors should be false by default");
        assertFalse(newInfo.isInstantiated(), "isInstantiated should be false by default");
        assertFalse(newInfo.isInstanceofed(), "isInstanceofed should be false by default");
        assertFalse(newInfo.isDotClassed(), "isDotClassed should be false by default");
        assertFalse(newInfo.isCaught(), "isCaught should be false by default");
        assertFalse(newInfo.isSimpleEnum(), "isSimpleEnum should be false by default");
        assertFalse(newInfo.isEscaping(), "isEscaping should be false by default");
        assertFalse(newInfo.hasSideEffects(), "hasSideEffects should be false by default");
        assertFalse(newInfo.containsPackageVisibleMembers(), "containsPackageVisibleMembers should be false by default");
        assertFalse(newInfo.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should be false by default");
        assertTrue(newInfo.mayBeMerged(), "mayBeMerged should be true by default");
        assertNull(newInfo.getWrappedClass(), "wrappedClass should be null by default");
        assertNull(newInfo.getTargetClass(), "targetClass should be null by default");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        ProgramClassOptimizationInfo info1 = new ProgramClassOptimizationInfo();
        ProgramClassOptimizationInfo info2 = new ProgramClassOptimizationInfo();
        ProgramClassOptimizationInfo info3 = new ProgramClassOptimizationInfo();

        // Assert
        assertNotNull(info1, "First info should be created");
        assertNotNull(info2, "Second info should be created");
        assertNotNull(info3, "Third info should be created");
        assertNotSame(info1, info2, "First and second info should be different instances");
        assertNotSame(info2, info3, "Second and third info should be different instances");
        assertNotSame(info1, info3, "First and third info should be different instances");
    }

    // =========================================================================
    // isKept() Tests
    // =========================================================================

    /**
     * Tests that isKept() returns false for ProgramClassOptimizationInfo.
     * This differs from the parent class which returns true.
     */
    @Test
    public void testIsKept_returnsFalse() {
        // Act
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept() should always return false for ProgramClassOptimizationInfo");
    }

    /**
     * Tests that isKept() consistently returns false across multiple invocations.
     */
    @Test
    public void testIsKept_consistentlyReturnsFalse() {
        // Act & Assert
        assertFalse(info.isKept(), "First call should return false");
        assertFalse(info.isKept(), "Second call should return false");
        assertFalse(info.isKept(), "Third call should return false");
    }

    // =========================================================================
    // setContainsConstructors() and containsConstructors() Tests
    // =========================================================================

    /**
     * Tests that containsConstructors() returns false initially.
     */
    @Test
    public void testContainsConstructors_returnsFalse_initially() {
        // Assert
        assertFalse(info.containsConstructors(), "containsConstructors should be false initially");
    }

    /**
     * Tests that setContainsConstructors() sets the flag to true.
     */
    @Test
    public void testSetContainsConstructors_setsFlag() {
        // Act
        info.setContainsConstructors();

        // Assert
        assertTrue(info.containsConstructors(), "containsConstructors should be true after setting");
    }

    /**
     * Tests that setContainsConstructors() can be called multiple times.
     */
    @Test
    public void testSetContainsConstructors_idempotent() {
        // Act
        info.setContainsConstructors();
        info.setContainsConstructors();
        info.setContainsConstructors();

        // Assert
        assertTrue(info.containsConstructors(), "containsConstructors should remain true after multiple sets");
    }

    // =========================================================================
    // setInstantiated() and isInstantiated() Tests
    // =========================================================================

    /**
     * Tests that isInstantiated() returns false initially.
     */
    @Test
    public void testIsInstantiated_returnsFalse_initially() {
        // Assert
        assertFalse(info.isInstantiated(), "isInstantiated should be false initially");
    }

    /**
     * Tests that setInstantiated() marks the class as instantiated.
     */
    @Test
    public void testSetInstantiated_setsFlag() {
        // Act
        info.setInstantiated();

        // Assert
        assertTrue(info.isInstantiated(), "isInstantiated should be true after setting");
    }

    /**
     * Tests that setInstantiated() is idempotent.
     */
    @Test
    public void testSetInstantiated_idempotent() {
        // Act
        info.setInstantiated();
        info.setInstantiated();
        info.setInstantiated();

        // Assert
        assertTrue(info.isInstantiated(), "isInstantiated should remain true after multiple sets");
    }

    // =========================================================================
    // setInstanceofed() and isInstanceofed() Tests
    // =========================================================================

    /**
     * Tests that isInstanceofed() returns false initially.
     */
    @Test
    public void testIsInstanceofed_returnsFalse_initially() {
        // Assert
        assertFalse(info.isInstanceofed(), "isInstanceofed should be false initially");
    }

    /**
     * Tests that setInstanceofed() marks the class as part of instanceof check.
     */
    @Test
    public void testSetInstanceofed_setsFlag() {
        // Act
        info.setInstanceofed();

        // Assert
        assertTrue(info.isInstanceofed(), "isInstanceofed should be true after setting");
    }

    /**
     * Tests that setInstanceofed() is idempotent.
     */
    @Test
    public void testSetInstanceofed_idempotent() {
        // Act
        info.setInstanceofed();
        info.setInstanceofed();

        // Assert
        assertTrue(info.isInstanceofed(), "isInstanceofed should remain true after multiple sets");
    }

    // =========================================================================
    // setDotClassed() and isDotClassed() Tests
    // =========================================================================

    /**
     * Tests that isDotClassed() returns false initially.
     */
    @Test
    public void testIsDotClassed_returnsFalse_initially() {
        // Assert
        assertFalse(info.isDotClassed(), "isDotClassed should be false initially");
    }

    /**
     * Tests that setDotClassed() marks the class as loaded with ldc instruction.
     */
    @Test
    public void testSetDotClassed_setsFlag() {
        // Act
        info.setDotClassed();

        // Assert
        assertTrue(info.isDotClassed(), "isDotClassed should be true after setting");
    }

    /**
     * Tests that setDotClassed() is idempotent.
     */
    @Test
    public void testSetDotClassed_idempotent() {
        // Act
        info.setDotClassed();
        info.setDotClassed();

        // Assert
        assertTrue(info.isDotClassed(), "isDotClassed should remain true after multiple sets");
    }

    // =========================================================================
    // setCaught() and isCaught() Tests
    // =========================================================================

    /**
     * Tests that isCaught() returns false initially.
     */
    @Test
    public void testIsCaught_returnsFalse_initially() {
        // Assert
        assertFalse(info.isCaught(), "isCaught should be false initially");
    }

    /**
     * Tests that setCaught() marks the class as caught in exception handler.
     */
    @Test
    public void testSetCaught_setsFlag() {
        // Act
        info.setCaught();

        // Assert
        assertTrue(info.isCaught(), "isCaught should be true after setting");
    }

    /**
     * Tests that setCaught() is idempotent.
     */
    @Test
    public void testSetCaught_idempotent() {
        // Act
        info.setCaught();
        info.setCaught();

        // Assert
        assertTrue(info.isCaught(), "isCaught should remain true after multiple sets");
    }

    // =========================================================================
    // setSimpleEnum() and isSimpleEnum() Tests
    // =========================================================================

    /**
     * Tests that isSimpleEnum() returns false initially.
     */
    @Test
    public void testIsSimpleEnum_returnsFalse_initially() {
        // Assert
        assertFalse(info.isSimpleEnum(), "isSimpleEnum should be false initially");
    }

    /**
     * Tests that setSimpleEnum(true) marks the class as simple enum.
     */
    @Test
    public void testSetSimpleEnum_withTrue_setsFlag() {
        // Act
        info.setSimpleEnum(true);

        // Assert
        assertTrue(info.isSimpleEnum(), "isSimpleEnum should be true after setting to true");
    }

    /**
     * Tests that setSimpleEnum(false) keeps the flag as false.
     */
    @Test
    public void testSetSimpleEnum_withFalse_keepsFlag() {
        // Act
        info.setSimpleEnum(false);

        // Assert
        assertFalse(info.isSimpleEnum(), "isSimpleEnum should remain false after setting to false");
    }

    /**
     * Tests that setSimpleEnum() can toggle the flag value.
     */
    @Test
    public void testSetSimpleEnum_canToggle() {
        // Act & Assert
        info.setSimpleEnum(true);
        assertTrue(info.isSimpleEnum(), "isSimpleEnum should be true after setting to true");

        info.setSimpleEnum(false);
        assertFalse(info.isSimpleEnum(), "isSimpleEnum should be false after setting to false");

        info.setSimpleEnum(true);
        assertTrue(info.isSimpleEnum(), "isSimpleEnum should be true again after setting to true");
    }

    // =========================================================================
    // setEscaping() and isEscaping() Tests
    // =========================================================================

    /**
     * Tests that isEscaping() returns false initially.
     */
    @Test
    public void testIsEscaping_returnsFalse_initially() {
        // Assert
        assertFalse(info.isEscaping(), "isEscaping should be false initially");
    }

    /**
     * Tests that setEscaping() marks instances as escaping to heap.
     */
    @Test
    public void testSetEscaping_setsFlag() {
        // Act
        info.setEscaping();

        // Assert
        assertTrue(info.isEscaping(), "isEscaping should be true after setting");
    }

    /**
     * Tests that setEscaping() is idempotent.
     */
    @Test
    public void testSetEscaping_idempotent() {
        // Act
        info.setEscaping();
        info.setEscaping();

        // Assert
        assertTrue(info.isEscaping(), "isEscaping should remain true after multiple sets");
    }

    // =========================================================================
    // setSideEffects() and hasSideEffects() Tests
    // =========================================================================

    /**
     * Tests that hasSideEffects() returns false initially.
     */
    @Test
    public void testHasSideEffects_returnsFalse_initially() {
        // Assert
        assertFalse(info.hasSideEffects(), "hasSideEffects should be false initially");
    }

    /**
     * Tests that setSideEffects() marks loading the class as having side effects.
     */
    @Test
    public void testSetSideEffects_setsFlag() {
        // Act
        info.setSideEffects();

        // Assert
        assertTrue(info.hasSideEffects(), "hasSideEffects should be true after setting");
    }

    /**
     * Tests that setSideEffects() is idempotent.
     */
    @Test
    public void testSetSideEffects_idempotent() {
        // Act
        info.setSideEffects();
        info.setSideEffects();

        // Assert
        assertTrue(info.hasSideEffects(), "hasSideEffects should remain true after multiple sets");
    }

    /**
     * Tests that hasSideEffects() respects the hasNoSideEffects flag from parent class.
     * When hasNoSideEffects is true, hasSideEffects should return false even after setSideEffects().
     */
    @Test
    public void testHasSideEffects_respectsNoSideEffectsFlag() {
        // Arrange
        info.setNoSideEffects();  // Set the parent class flag

        // Act
        info.setSideEffects();

        // Assert - hasNoSideEffects takes precedence
        assertFalse(info.hasSideEffects(), "hasSideEffects should be false when hasNoSideEffects is true");
    }

    // =========================================================================
    // setContainsPackageVisibleMembers() and containsPackageVisibleMembers() Tests
    // =========================================================================

    /**
     * Tests that containsPackageVisibleMembers() returns false initially.
     */
    @Test
    public void testContainsPackageVisibleMembers_returnsFalse_initially() {
        // Assert
        assertFalse(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should be false initially");
    }

    /**
     * Tests that setContainsPackageVisibleMembers() sets the flag.
     */
    @Test
    public void testSetContainsPackageVisibleMembers_setsFlag() {
        // Act
        info.setContainsPackageVisibleMembers();

        // Assert
        assertTrue(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should be true after setting");
    }

    /**
     * Tests that setContainsPackageVisibleMembers() is idempotent.
     */
    @Test
    public void testSetContainsPackageVisibleMembers_idempotent() {
        // Act
        info.setContainsPackageVisibleMembers();
        info.setContainsPackageVisibleMembers();

        // Assert
        assertTrue(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should remain true after multiple sets");
    }

    // =========================================================================
    // setInvokesPackageVisibleMembers() and invokesPackageVisibleMembers() Tests
    // =========================================================================

    /**
     * Tests that invokesPackageVisibleMembers() returns false initially.
     */
    @Test
    public void testInvokesPackageVisibleMembers_returnsFalse_initially() {
        // Assert
        assertFalse(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should be false initially");
    }

    /**
     * Tests that setInvokesPackageVisibleMembers() sets the flag.
     */
    @Test
    public void testSetInvokesPackageVisibleMembers_setsFlag() {
        // Act
        info.setInvokesPackageVisibleMembers();

        // Assert
        assertTrue(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should be true after setting");
    }

    /**
     * Tests that setInvokesPackageVisibleMembers() is idempotent.
     */
    @Test
    public void testSetInvokesPackageVisibleMembers_idempotent() {
        // Act
        info.setInvokesPackageVisibleMembers();
        info.setInvokesPackageVisibleMembers();

        // Assert
        assertTrue(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should remain true after multiple sets");
    }

    // =========================================================================
    // setMayNotBeMerged() and mayBeMerged() Tests
    // =========================================================================

    /**
     * Tests that mayBeMerged() returns true initially.
     */
    @Test
    public void testMayBeMerged_returnsTrue_initially() {
        // Assert
        assertTrue(info.mayBeMerged(), "mayBeMerged should be true initially");
    }

    /**
     * Tests that setMayNotBeMerged() marks the class as not mergeable.
     */
    @Test
    public void testSetMayNotBeMerged_setsFlag() {
        // Act
        info.setMayNotBeMerged();

        // Assert
        assertFalse(info.mayBeMerged(), "mayBeMerged should be false after setMayNotBeMerged");
    }

    /**
     * Tests that setMayNotBeMerged() is idempotent.
     */
    @Test
    public void testSetMayNotBeMerged_idempotent() {
        // Act
        info.setMayNotBeMerged();
        info.setMayNotBeMerged();

        // Assert
        assertFalse(info.mayBeMerged(), "mayBeMerged should remain false after multiple calls");
    }

    // =========================================================================
    // setWrappedClass() and getWrappedClass() Tests
    // =========================================================================

    /**
     * Tests that getWrappedClass() returns null initially.
     */
    @Test
    public void testGetWrappedClass_returnsNull_initially() {
        // Assert
        assertNull(info.getWrappedClass(), "wrappedClass should be null initially");
    }

    /**
     * Tests that setWrappedClass() stores the class reference.
     */
    @Test
    public void testSetWrappedClass_storesClass() {
        // Act
        info.setWrappedClass(mockWrappedClazz);

        // Assert
        assertSame(mockWrappedClazz, info.getWrappedClass(), "getWrappedClass should return the set class");
    }

    /**
     * Tests that setWrappedClass() can update the wrapped class.
     */
    @Test
    public void testSetWrappedClass_canUpdate() {
        // Arrange
        Clazz anotherClazz = mock(Clazz.class);
        when(anotherClazz.getName()).thenReturn("com/example/AnotherClass");

        // Act
        info.setWrappedClass(mockWrappedClazz);
        assertSame(mockWrappedClazz, info.getWrappedClass());

        info.setWrappedClass(anotherClazz);

        // Assert
        assertSame(anotherClazz, info.getWrappedClass(), "getWrappedClass should return the updated class");
        assertNotSame(mockWrappedClazz, info.getWrappedClass(), "Old wrapped class should be replaced");
    }

    /**
     * Tests that setWrappedClass() can be set to null.
     */
    @Test
    public void testSetWrappedClass_canSetNull() {
        // Arrange
        info.setWrappedClass(mockWrappedClazz);
        assertNotNull(info.getWrappedClass());

        // Act
        info.setWrappedClass(null);

        // Assert
        assertNull(info.getWrappedClass(), "wrappedClass should be null after setting to null");
    }

    // =========================================================================
    // setTargetClass() and getTargetClass() Tests
    // =========================================================================

    /**
     * Tests that getTargetClass() returns null initially.
     */
    @Test
    public void testGetTargetClass_returnsNull_initially() {
        // Assert
        assertNull(info.getTargetClass(), "targetClass should be null initially");
    }

    /**
     * Tests that setTargetClass() stores the class reference.
     */
    @Test
    public void testSetTargetClass_storesClass() {
        // Act
        info.setTargetClass(mockTargetClazz);

        // Assert
        assertSame(mockTargetClazz, info.getTargetClass(), "getTargetClass should return the set class");
    }

    /**
     * Tests that setTargetClass() can update the target class.
     */
    @Test
    public void testSetTargetClass_canUpdate() {
        // Arrange
        Clazz anotherClazz = mock(Clazz.class);
        when(anotherClazz.getName()).thenReturn("com/example/AnotherTarget");

        // Act
        info.setTargetClass(mockTargetClazz);
        assertSame(mockTargetClazz, info.getTargetClass());

        info.setTargetClass(anotherClazz);

        // Assert
        assertSame(anotherClazz, info.getTargetClass(), "getTargetClass should return the updated class");
        assertNotSame(mockTargetClazz, info.getTargetClass(), "Old target class should be replaced");
    }

    /**
     * Tests that setTargetClass() can be set to null.
     */
    @Test
    public void testSetTargetClass_canSetNull() {
        // Arrange
        info.setTargetClass(mockTargetClazz);
        assertNotNull(info.getTargetClass());

        // Act
        info.setTargetClass(null);

        // Assert
        assertNull(info.getTargetClass(), "targetClass should be null after setting to null");
    }

    /**
     * Tests that wrappedClass and targetClass are independent.
     */
    @Test
    public void testWrappedAndTargetClass_areIndependent() {
        // Act
        info.setWrappedClass(mockWrappedClazz);
        info.setTargetClass(mockTargetClazz);

        // Assert
        assertSame(mockWrappedClazz, info.getWrappedClass(), "wrappedClass should be set independently");
        assertSame(mockTargetClazz, info.getTargetClass(), "targetClass should be set independently");
        assertNotSame(info.getWrappedClass(), info.getTargetClass(), "wrappedClass and targetClass should be different");
    }

    // =========================================================================
    // merge() Tests
    // =========================================================================

    /**
     * Tests that merge() combines isInstantiated flags.
     */
    @Test
    public void testMerge_combinesIsInstantiated() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setInstantiated();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.isInstantiated(), "isInstantiated should be true after merging");
    }

    /**
     * Tests that merge() combines isInstanceofed flags.
     */
    @Test
    public void testMerge_combinesIsInstanceofed() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setInstanceofed();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.isInstanceofed(), "isInstanceofed should be true after merging");
    }

    /**
     * Tests that merge() combines isDotClassed flags.
     */
    @Test
    public void testMerge_combinesIsDotClassed() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setDotClassed();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.isDotClassed(), "isDotClassed should be true after merging");
    }

    /**
     * Tests that merge() combines isCaught flags.
     */
    @Test
    public void testMerge_combinesIsCaught() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setCaught();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.isCaught(), "isCaught should be true after merging");
    }

    /**
     * Tests that merge() combines isSimpleEnum flags.
     */
    @Test
    public void testMerge_combinesIsSimpleEnum() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setSimpleEnum(true);

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.isSimpleEnum(), "isSimpleEnum should be true after merging");
    }

    /**
     * Tests that merge() combines isEscaping flags.
     */
    @Test
    public void testMerge_combinesIsEscaping() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setEscaping();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.isEscaping(), "isEscaping should be true after merging");
    }

    /**
     * Tests that merge() combines hasSideEffects flags.
     */
    @Test
    public void testMerge_combinesHasSideEffects() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setSideEffects();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.hasSideEffects(), "hasSideEffects should be true after merging");
    }

    /**
     * Tests that merge() combines containsPackageVisibleMembers flags.
     */
    @Test
    public void testMerge_combinesContainsPackageVisibleMembers() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setContainsPackageVisibleMembers();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should be true after merging");
    }

    /**
     * Tests that merge() combines invokesPackageVisibleMembers flags.
     */
    @Test
    public void testMerge_combinesInvokesPackageVisibleMembers() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setInvokesPackageVisibleMembers();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should be true after merging");
    }

    /**
     * Tests that merge() combines containsConstructors flags.
     */
    @Test
    public void testMerge_combinesContainsConstructors() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setContainsConstructors();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.containsConstructors(), "containsConstructors should be true after merging");
    }

    /**
     * Tests that merge() combines multiple flags at once.
     */
    @Test
    public void testMerge_combinesMultipleFlags() {
        // Arrange
        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setInstantiated();
        other.setInstanceofed();
        other.setDotClassed();
        other.setCaught();
        other.setEscaping();

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.isInstantiated(), "isInstantiated should be true after merging");
        assertTrue(info.isInstanceofed(), "isInstanceofed should be true after merging");
        assertTrue(info.isDotClassed(), "isDotClassed should be true after merging");
        assertTrue(info.isCaught(), "isCaught should be true after merging");
        assertTrue(info.isEscaping(), "isEscaping should be true after merging");
    }

    /**
     * Tests that merge() preserves existing true flags.
     */
    @Test
    public void testMerge_preservesExistingFlags() {
        // Arrange
        info.setInstantiated();
        info.setEscaping();

        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();
        other.setInstanceofed();

        // Act
        info.merge(other);

        // Assert - original flags should be preserved
        assertTrue(info.isInstantiated(), "isInstantiated should remain true after merging");
        assertTrue(info.isEscaping(), "isEscaping should remain true after merging");
        // And new flag should be added
        assertTrue(info.isInstanceofed(), "isInstanceofed should be true after merging");
    }

    /**
     * Tests that merge() with empty other info doesn't change anything.
     */
    @Test
    public void testMerge_withEmptyOther_noChange() {
        // Arrange
        info.setInstantiated();
        info.setEscaping();

        ProgramClassOptimizationInfo other = new ProgramClassOptimizationInfo();

        // Act
        info.merge(other);

        // Assert - flags should remain unchanged
        assertTrue(info.isInstantiated(), "isInstantiated should remain true");
        assertTrue(info.isEscaping(), "isEscaping should remain true");
        assertFalse(info.isInstanceofed(), "isInstanceofed should remain false");
        assertFalse(info.isDotClassed(), "isDotClassed should remain false");
    }

    /**
     * Tests that merge() can be called multiple times.
     */
    @Test
    public void testMerge_canBeCalledMultipleTimes() {
        // Arrange
        ProgramClassOptimizationInfo other1 = new ProgramClassOptimizationInfo();
        other1.setInstantiated();

        ProgramClassOptimizationInfo other2 = new ProgramClassOptimizationInfo();
        other2.setInstanceofed();

        ProgramClassOptimizationInfo other3 = new ProgramClassOptimizationInfo();
        other3.setDotClassed();

        // Act
        info.merge(other1);
        info.merge(other2);
        info.merge(other3);

        // Assert
        assertTrue(info.isInstantiated(), "isInstantiated should be true after first merge");
        assertTrue(info.isInstanceofed(), "isInstanceofed should be true after second merge");
        assertTrue(info.isDotClassed(), "isDotClassed should be true after third merge");
    }

    /**
     * Tests that merge() works with ClassOptimizationInfo (parent class).
     */
    @Test
    public void testMerge_withParentClassType() {
        // Arrange
        ClassOptimizationInfo other = new ClassOptimizationInfo();
        // ClassOptimizationInfo has default implementations that return true for most methods

        // Act
        info.merge(other);

        // Assert - should merge based on the getter methods
        assertTrue(info.isInstantiated(), "isInstantiated should be true (parent returns true)");
        assertTrue(info.isCaught(), "isCaught should be true (parent returns true)");
        assertTrue(info.isEscaping(), "isEscaping should be true (parent returns true)");
        assertTrue(info.containsConstructors(), "containsConstructors should be true (parent returns true)");
        assertTrue(info.containsPackageVisibleMembers(), "containsPackageVisibleMembers should be true (parent returns true)");
        assertTrue(info.invokesPackageVisibleMembers(), "invokesPackageVisibleMembers should be true (parent returns true)");
    }

    // =========================================================================
    // setProgramClassOptimizationInfo() Tests
    // =========================================================================

    /**
     * Tests that setProgramClassOptimizationInfo() correctly sets the optimization info
     * on a Clazz.
     */
    @Test
    public void testSetProgramClassOptimizationInfo_setsProcessingInfo() {
        // Arrange
        assertNull(mockClazz.getProcessingInfo(), "Processing info should be null initially");

        // Act
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(mockClazz);

        // Assert
        verify(mockClazz).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that setProgramClassOptimizationInfo() creates a new instance each time.
     */
    @Test
    public void testSetProgramClassOptimizationInfo_createsNewInstance() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(clazz1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(clazz2);

        // Assert - verify each gets its own instance
        verify(clazz1).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
        verify(clazz2).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    // =========================================================================
    // getProgramClassOptimizationInfo() Tests
    // =========================================================================

    /**
     * Tests that getProgramClassOptimizationInfo() returns null when no info is set.
     */
    @Test
    public void testGetProgramClassOptimizationInfo_returnsNull_whenNotSet() {
        // Arrange
        when(mockClazz.getProcessingInfo()).thenReturn(null);

        // Act
        ProgramClassOptimizationInfo result =
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(mockClazz);

        // Assert
        assertNull(result, "Should return null when no optimization info is set");
    }

    /**
     * Tests that getProgramClassOptimizationInfo() returns the correct info after it's set.
     */
    @Test
    public void testGetProgramClassOptimizationInfo_returnsSetInfo() {
        // Arrange
        ProgramClassOptimizationInfo expectedInfo = new ProgramClassOptimizationInfo();
        when(mockClazz.getProcessingInfo()).thenReturn(expectedInfo);

        // Act
        ProgramClassOptimizationInfo result =
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(mockClazz);

        // Assert
        assertSame(expectedInfo, result, "Should return the set optimization info");
    }

    /**
     * Tests that getProgramClassOptimizationInfo() throws ClassCastException
     * when processing info is of wrong type.
     */
    @Test
    public void testGetProgramClassOptimizationInfo_withWrongType_throwsClassCastException() {
        // Arrange
        when(mockClazz.getProcessingInfo()).thenReturn("Not a ProgramClassOptimizationInfo");

        // Act & Assert
        assertThrows(ClassCastException.class, () -> {
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(mockClazz);
        }, "Should throw ClassCastException when processing info is of wrong type");
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests a complete workflow of setting and getting optimization info.
     */
    @Test
    public void testIntegration_completeWorkflow() {
        // Arrange
        ProgramClassOptimizationInfo newInfo = new ProgramClassOptimizationInfo();
        when(mockClazz.getProcessingInfo()).thenReturn(newInfo);

        // Act
        newInfo.setInstantiated();
        newInfo.setInstanceofed();
        newInfo.setDotClassed();
        newInfo.setWrappedClass(mockWrappedClazz);
        newInfo.setTargetClass(mockTargetClazz);
        newInfo.setMayNotBeMerged();

        ProgramClassOptimizationInfo retrieved =
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(mockClazz);

        // Assert
        assertSame(newInfo, retrieved, "Retrieved info should be the same instance");
        assertTrue(retrieved.isInstantiated(), "isInstantiated should be true");
        assertTrue(retrieved.isInstanceofed(), "isInstanceofed should be true");
        assertTrue(retrieved.isDotClassed(), "isDotClassed should be true");
        assertSame(mockWrappedClazz, retrieved.getWrappedClass(), "wrappedClass should match");
        assertSame(mockTargetClazz, retrieved.getTargetClass(), "targetClass should match");
        assertFalse(retrieved.mayBeMerged(), "mayBeMerged should be false");
    }

    /**
     * Tests that all flags can be set independently without affecting each other.
     */
    @Test
    public void testIntegration_allFlagsIndependent() {
        // Act
        info.setContainsConstructors();

        // Assert - only containsConstructors should be true
        assertTrue(info.containsConstructors());
        assertFalse(info.isInstantiated());
        assertFalse(info.isInstanceofed());
        assertFalse(info.isDotClassed());
        assertFalse(info.isCaught());
        assertFalse(info.isSimpleEnum());
        assertFalse(info.isEscaping());
        assertFalse(info.hasSideEffects());
        assertFalse(info.containsPackageVisibleMembers());
        assertFalse(info.invokesPackageVisibleMembers());
    }
}
