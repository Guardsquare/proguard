package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.evaluation.value.ReferenceValue;
import proguard.evaluation.value.Value;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodOptimizationInfo}.
 *
 * This class stores optimization information that can be attached to a method during
 * ProGuard's optimization process. The tests cover all public methods including:
 * - Constructor and initialization
 * - Side effects management (no side effects, no external side effects)
 * - Parameter management (escaping, modified, used, escaped)
 * - Return value tracking
 * - Various method analysis methods
 * - Static helper methods for setting and retrieving optimization info
 */
public class MethodOptimizationInfoClaudeTest {

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a MethodOptimizationInfo instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Assert
        assertNotNull(info, "MethodOptimizationInfo instance should be created");
    }

    /**
     * Tests that the constructor initializes all fields to false/null by default.
     */
    @Test
    public void testConstructor_initializesDefaults() {
        // Act
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Assert - internal state defaults
        assertFalse(info.hasNoSideEffects(), "hasNoSideEffects should be false by default");
        assertFalse(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should be false by default");
        assertFalse(info.hasNoEscapingParameters(), "hasNoEscapingParameters should be false by default");
        assertFalse(info.hasNoExternalReturnValues(), "hasNoExternalReturnValues should be false by default");
        assertNull(info.getReturnValue(), "returnValue should be null by default");
    }

    /**
     * Tests that the constructor initializes conservative defaults for analysis methods.
     */
    @Test
    public void testConstructor_initializesConservativeDefaults() {
        // Act
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Assert - conservative defaults (assume everything is true for safety)
        assertTrue(info.isKept(), "isKept should return true by default");
        assertTrue(info.hasSideEffects(), "hasSideEffects should return true by default");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should return false by default");
        assertTrue(info.catchesExceptions(), "catchesExceptions should return true by default");
        assertTrue(info.branchesBackward(), "branchesBackward should return true by default");
        assertTrue(info.invokesSuperMethods(), "invokesSuperMethods should return true by default");
        assertTrue(info.invokesDynamically(), "invokesDynamically should return true by default");
        assertTrue(info.accessesPrivateCode(), "accessesPrivateCode should return true by default");
        assertTrue(info.accessesPackageCode(), "accessesPackageCode should return true by default");
        assertTrue(info.accessesProtectedCode(), "accessesProtectedCode should return true by default");
        assertTrue(info.hasSynchronizedBlock(), "hasSynchronizedBlock should return true by default");
        assertTrue(info.assignsFinalField(), "assignsFinalField should return true by default");
        assertFalse(info.returnsWithNonEmptyStack(), "returnsWithNonEmptyStack should return false by default");
        assertEquals(Integer.MAX_VALUE, info.getInvocationCount(), "getInvocationCount should return Integer.MAX_VALUE by default");
        assertEquals(0, info.getParameterSize(), "getParameterSize should return 0 by default");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        MethodOptimizationInfo info1 = new MethodOptimizationInfo();
        MethodOptimizationInfo info2 = new MethodOptimizationInfo();
        MethodOptimizationInfo info3 = new MethodOptimizationInfo();

        // Assert
        assertNotNull(info1, "First info should be created");
        assertNotNull(info2, "Second info should be created");
        assertNotNull(info3, "Third info should be created");
        assertNotSame(info1, info2, "First and second info should be different instances");
        assertNotSame(info2, info3, "Second and third info should be different instances");
        assertNotSame(info1, info3, "First and third info should be different instances");
    }

    // =========================================================================
    // isKept Tests
    // =========================================================================

    /**
     * Tests isKept always returns true (conservative default).
     */
    @Test
    public void testIsKept_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.isKept(), "isKept should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.isKept(), "isKept should still return true after setNoSideEffects");
    }

    // =========================================================================
    // Side Effects Tests (setNoSideEffects, hasNoSideEffects, hasSideEffects)
    // =========================================================================

    /**
     * Tests setNoSideEffects sets the hasNoSideEffects flag to true.
     */
    @Test
    public void testSetNoSideEffects_setsHasNoSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        assertFalse(info.hasNoSideEffects(), "Should initially have side effects");

        // Act
        info.setNoSideEffects();

        // Assert
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should be true after setNoSideEffects");
    }

    /**
     * Tests setNoSideEffects also sets hasNoExternalSideEffects to true.
     */
    @Test
    public void testSetNoSideEffects_setsHasNoExternalSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        assertFalse(info.hasNoExternalSideEffects(), "Should initially have external side effects");

        // Act
        info.setNoSideEffects();

        // Assert
        assertTrue(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should be true after setNoSideEffects");
    }

    /**
     * Tests setNoSideEffects also sets hasNoEscapingParameters to true.
     */
    @Test
    public void testSetNoSideEffects_setsHasNoEscapingParameters() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        assertFalse(info.hasNoEscapingParameters(), "Should initially have escaping parameters");

        // Act
        info.setNoSideEffects();

        // Assert
        assertTrue(info.hasNoEscapingParameters(), "hasNoEscapingParameters should be true after setNoSideEffects");
    }

    /**
     * Tests hasNoSideEffects returns false initially.
     */
    @Test
    public void testHasNoSideEffects_returnsFalseInitially() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertFalse(info.hasNoSideEffects(), "hasNoSideEffects should be false initially");
    }

    /**
     * Tests hasNoSideEffects returns true after setNoSideEffects is called.
     */
    @Test
    public void testHasNoSideEffects_returnsTrueAfterSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
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
        MethodOptimizationInfo info = new MethodOptimizationInfo();

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
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act - call multiple times
        info.setNoSideEffects();
        info.setNoSideEffects();
        info.setNoSideEffects();

        // Assert - should still be true
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should remain true after multiple calls");
        assertFalse(info.hasSideEffects(), "hasSideEffects should remain false after multiple calls");
    }

    // =========================================================================
    // External Side Effects Tests
    // =========================================================================

    /**
     * Tests setNoExternalSideEffects sets the flag to true.
     */
    @Test
    public void testSetNoExternalSideEffects_setsFlag() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        assertFalse(info.hasNoExternalSideEffects(), "Should initially have external side effects");

        // Act
        info.setNoExternalSideEffects();

        // Assert
        assertTrue(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should be true after set");
    }

    /**
     * Tests setNoExternalSideEffects also sets hasNoEscapingParameters to true.
     */
    @Test
    public void testSetNoExternalSideEffects_setsHasNoEscapingParameters() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        assertFalse(info.hasNoEscapingParameters(), "Should initially have escaping parameters");

        // Act
        info.setNoExternalSideEffects();

        // Assert
        assertTrue(info.hasNoEscapingParameters(), "hasNoEscapingParameters should be true after setNoExternalSideEffects");
    }

    /**
     * Tests hasNoExternalSideEffects returns false initially.
     */
    @Test
    public void testHasNoExternalSideEffects_returnsFalseInitially() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertFalse(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should be false initially");
    }

    /**
     * Tests hasNoExternalSideEffects returns true after being set.
     */
    @Test
    public void testHasNoExternalSideEffects_returnsTrueAfterSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoExternalSideEffects();

        // Act & Assert
        assertTrue(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should return true after being set");
    }

    /**
     * Tests that setNoExternalSideEffects can be called multiple times.
     */
    @Test
    public void testSetNoExternalSideEffects_canBeCalledMultipleTimes() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act - call multiple times
        info.setNoExternalSideEffects();
        info.setNoExternalSideEffects();
        info.setNoExternalSideEffects();

        // Assert - should still be true
        assertTrue(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should remain true after multiple calls");
    }

    // =========================================================================
    // Escaping Parameters Tests
    // =========================================================================

    /**
     * Tests setNoEscapingParameters sets the flag to true.
     */
    @Test
    public void testSetNoEscapingParameters_setsFlag() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        assertFalse(info.hasNoEscapingParameters(), "Should initially have escaping parameters");

        // Act
        info.setNoEscapingParameters();

        // Assert
        assertTrue(info.hasNoEscapingParameters(), "hasNoEscapingParameters should be true after set");
    }

    /**
     * Tests hasNoEscapingParameters returns false initially.
     */
    @Test
    public void testHasNoEscapingParameters_returnsFalseInitially() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertFalse(info.hasNoEscapingParameters(), "hasNoEscapingParameters should be false initially");
    }

    /**
     * Tests hasNoEscapingParameters returns true after being set.
     */
    @Test
    public void testHasNoEscapingParameters_returnsTrueAfterSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoEscapingParameters();

        // Act & Assert
        assertTrue(info.hasNoEscapingParameters(), "hasNoEscapingParameters should return true after being set");
    }

    /**
     * Tests that setNoEscapingParameters can be called multiple times.
     */
    @Test
    public void testSetNoEscapingParameters_canBeCalledMultipleTimes() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act - call multiple times
        info.setNoEscapingParameters();
        info.setNoEscapingParameters();
        info.setNoEscapingParameters();

        // Assert - should still be true
        assertTrue(info.hasNoEscapingParameters(), "hasNoEscapingParameters should remain true after multiple calls");
    }

    // =========================================================================
    // External Return Values Tests
    // =========================================================================

    /**
     * Tests setNoExternalReturnValues sets the flag to true.
     */
    @Test
    public void testSetNoExternalReturnValues_setsFlag() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        assertFalse(info.hasNoExternalReturnValues(), "Should initially have external return values");

        // Act
        info.setNoExternalReturnValues();

        // Assert
        assertTrue(info.hasNoExternalReturnValues(), "hasNoExternalReturnValues should be true after set");
    }

    /**
     * Tests hasNoExternalReturnValues returns false initially.
     */
    @Test
    public void testHasNoExternalReturnValues_returnsFalseInitially() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertFalse(info.hasNoExternalReturnValues(), "hasNoExternalReturnValues should be false initially");
    }

    /**
     * Tests hasNoExternalReturnValues returns true after being set.
     */
    @Test
    public void testHasNoExternalReturnValues_returnsTrueAfterSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoExternalReturnValues();

        // Act & Assert
        assertTrue(info.hasNoExternalReturnValues(), "hasNoExternalReturnValues should return true after being set");
    }

    /**
     * Tests that setNoExternalReturnValues can be called multiple times.
     */
    @Test
    public void testSetNoExternalReturnValues_canBeCalledMultipleTimes() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act - call multiple times
        info.setNoExternalReturnValues();
        info.setNoExternalReturnValues();
        info.setNoExternalReturnValues();

        // Assert - should still be true
        assertTrue(info.hasNoExternalReturnValues(), "hasNoExternalReturnValues should remain true after multiple calls");
    }

    // =========================================================================
    // Return Value Tests
    // =========================================================================

    /**
     * Tests setReturnValue stores the value.
     */
    @Test
    public void testSetReturnValue_storesValue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        Value mockValue = mock(Value.class);

        // Act
        info.setReturnValue(mockValue);

        // Assert
        assertSame(mockValue, info.getReturnValue(), "getReturnValue should return the set value");
    }

    /**
     * Tests getReturnValue returns null initially.
     */
    @Test
    public void testGetReturnValue_returnsNullInitially() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertNull(info.getReturnValue(), "getReturnValue should return null initially");
    }

    /**
     * Tests getReturnValue returns the value after being set.
     */
    @Test
    public void testGetReturnValue_returnsValueAfterSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        Value mockValue = mock(Value.class);
        info.setReturnValue(mockValue);

        // Act & Assert
        assertSame(mockValue, info.getReturnValue(), "getReturnValue should return the set value");
    }

    /**
     * Tests setReturnValue can be called with null.
     */
    @Test
    public void testSetReturnValue_canBeSetToNull() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        Value mockValue = mock(Value.class);
        info.setReturnValue(mockValue);

        // Act
        info.setReturnValue(null);

        // Assert
        assertNull(info.getReturnValue(), "getReturnValue should return null after setting to null");
    }

    /**
     * Tests setReturnValue can be called multiple times (replaces previous value).
     */
    @Test
    public void testSetReturnValue_canBeReplacedMultipleTimes() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        Value mockValue1 = mock(Value.class);
        Value mockValue2 = mock(Value.class);
        Value mockValue3 = mock(Value.class);

        // Act & Assert
        info.setReturnValue(mockValue1);
        assertSame(mockValue1, info.getReturnValue(), "Should return first value");

        info.setReturnValue(mockValue2);
        assertSame(mockValue2, info.getReturnValue(), "Should return second value");

        info.setReturnValue(mockValue3);
        assertSame(mockValue3, info.getReturnValue(), "Should return third value");
    }

    // =========================================================================
    // Conservative Analysis Methods Tests
    // =========================================================================

    /**
     * Tests canBeMadePrivate always returns false (conservative default).
     */
    @Test
    public void testCanBeMadePrivate_alwaysReturnsFalse() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should always return false");

        // Even after setting various flags
        info.setNoSideEffects();
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should still return false");
    }

    /**
     * Tests catchesExceptions always returns true (conservative default).
     */
    @Test
    public void testCatchesExceptions_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.catchesExceptions(), "catchesExceptions should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.catchesExceptions(), "catchesExceptions should still return true");
    }

    /**
     * Tests branchesBackward always returns true (conservative default).
     */
    @Test
    public void testBranchesBackward_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.branchesBackward(), "branchesBackward should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.branchesBackward(), "branchesBackward should still return true");
    }

    /**
     * Tests invokesSuperMethods always returns true (conservative default).
     */
    @Test
    public void testInvokesSuperMethods_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.invokesSuperMethods(), "invokesSuperMethods should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.invokesSuperMethods(), "invokesSuperMethods should still return true");
    }

    /**
     * Tests invokesDynamically always returns true (conservative default).
     */
    @Test
    public void testInvokesDynamically_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.invokesDynamically(), "invokesDynamically should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.invokesDynamically(), "invokesDynamically should still return true");
    }

    /**
     * Tests accessesPrivateCode always returns true (conservative default).
     */
    @Test
    public void testAccessesPrivateCode_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.accessesPrivateCode(), "accessesPrivateCode should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.accessesPrivateCode(), "accessesPrivateCode should still return true");
    }

    /**
     * Tests accessesPackageCode always returns true (conservative default).
     */
    @Test
    public void testAccessesPackageCode_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.accessesPackageCode(), "accessesPackageCode should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.accessesPackageCode(), "accessesPackageCode should still return true");
    }

    /**
     * Tests accessesProtectedCode always returns true (conservative default).
     */
    @Test
    public void testAccessesProtectedCode_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.accessesProtectedCode(), "accessesProtectedCode should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.accessesProtectedCode(), "accessesProtectedCode should still return true");
    }

    /**
     * Tests hasSynchronizedBlock always returns true (conservative default).
     */
    @Test
    public void testHasSynchronizedBlock_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.hasSynchronizedBlock(), "hasSynchronizedBlock should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.hasSynchronizedBlock(), "hasSynchronizedBlock should still return true");
    }

    /**
     * Tests assignsFinalField always returns true (conservative default).
     */
    @Test
    public void testAssignsFinalField_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.assignsFinalField(), "assignsFinalField should always return true");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.assignsFinalField(), "assignsFinalField should still return true");
    }

    /**
     * Tests returnsWithNonEmptyStack always returns false.
     */
    @Test
    public void testReturnsWithNonEmptyStack_alwaysReturnsFalse() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertFalse(info.returnsWithNonEmptyStack(), "returnsWithNonEmptyStack should always return false");

        // Even after setting various flags
        info.setNoSideEffects();
        assertFalse(info.returnsWithNonEmptyStack(), "returnsWithNonEmptyStack should still return false");
    }

    /**
     * Tests getInvocationCount always returns Integer.MAX_VALUE (conservative default).
     */
    @Test
    public void testGetInvocationCount_alwaysReturnsMaxValue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertEquals(Integer.MAX_VALUE, info.getInvocationCount(), "getInvocationCount should return Integer.MAX_VALUE");

        // Even after setting various flags
        info.setNoSideEffects();
        assertEquals(Integer.MAX_VALUE, info.getInvocationCount(), "getInvocationCount should still return Integer.MAX_VALUE");
    }

    // =========================================================================
    // Parameter Size Tests
    // =========================================================================

    /**
     * Tests getParameterSize returns 0 by default.
     */
    @Test
    public void testGetParameterSize_returnsZero() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertEquals(0, info.getParameterSize(), "getParameterSize should return 0 by default");
    }

    // =========================================================================
    // Unused Parameters Tests
    // =========================================================================

    /**
     * Tests hasUnusedParameters returns false by default.
     */
    @Test
    public void testHasUnusedParameters_returnsFalse() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertFalse(info.hasUnusedParameters(), "hasUnusedParameters should return false by default");
    }

    /**
     * Tests isParameterUsed returns true for any index (conservative default).
     */
    @Test
    public void testIsParameterUsed_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.isParameterUsed(0), "isParameterUsed should return true for index 0");
        assertTrue(info.isParameterUsed(1), "isParameterUsed should return true for index 1");
        assertTrue(info.isParameterUsed(5), "isParameterUsed should return true for index 5");
        assertTrue(info.isParameterUsed(10), "isParameterUsed should return true for index 10");
    }

    /**
     * Tests getUsedParameters returns -1 (all bits set, conservative default).
     */
    @Test
    public void testGetUsedParameters_returnsAllBitsSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertEquals(-1L, info.getUsedParameters(), "getUsedParameters should return -1 (all parameters used)");
    }

    // =========================================================================
    // Escaped Parameters Tests
    // =========================================================================

    /**
     * Tests hasParameterEscaped returns true for any index (conservative default).
     */
    @Test
    public void testHasParameterEscaped_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.hasParameterEscaped(0), "hasParameterEscaped should return true for index 0");
        assertTrue(info.hasParameterEscaped(1), "hasParameterEscaped should return true for index 1");
        assertTrue(info.hasParameterEscaped(5), "hasParameterEscaped should return true for index 5");
        assertTrue(info.hasParameterEscaped(10), "hasParameterEscaped should return true for index 10");
    }

    /**
     * Tests getEscapedParameters returns -1 (all bits set, conservative default).
     */
    @Test
    public void testGetEscapedParameters_returnsAllBitsSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertEquals(-1L, info.getEscapedParameters(), "getEscapedParameters should return -1 (all parameters escaped)");
    }

    // =========================================================================
    // Escaping Parameters Tests (isParameterEscaping, getEscapingParameters)
    // =========================================================================

    /**
     * Tests isParameterEscaping returns true by default (when hasNoEscapingParameters is false).
     */
    @Test
    public void testIsParameterEscaping_returnsTrueByDefault() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.isParameterEscaping(0), "isParameterEscaping should return true for index 0");
        assertTrue(info.isParameterEscaping(1), "isParameterEscaping should return true for index 1");
        assertTrue(info.isParameterEscaping(5), "isParameterEscaping should return true for index 5");
    }

    /**
     * Tests isParameterEscaping returns false after setNoEscapingParameters.
     */
    @Test
    public void testIsParameterEscaping_returnsFalseAfterSetNoEscapingParameters() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoEscapingParameters();

        // Act & Assert
        assertFalse(info.isParameterEscaping(0), "isParameterEscaping should return false after setNoEscapingParameters");
        assertFalse(info.isParameterEscaping(1), "isParameterEscaping should return false after setNoEscapingParameters");
        assertFalse(info.isParameterEscaping(5), "isParameterEscaping should return false after setNoEscapingParameters");
    }

    /**
     * Tests getEscapingParameters returns -1 by default.
     */
    @Test
    public void testGetEscapingParameters_returnsAllBitsSetByDefault() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertEquals(-1L, info.getEscapingParameters(), "getEscapingParameters should return -1 by default");
    }

    /**
     * Tests getEscapingParameters returns 0 after setNoEscapingParameters.
     */
    @Test
    public void testGetEscapingParameters_returnsZeroAfterSetNoEscapingParameters() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoEscapingParameters();

        // Act & Assert
        assertEquals(0L, info.getEscapingParameters(), "getEscapingParameters should return 0 after setNoEscapingParameters");
    }

    // =========================================================================
    // Modified Parameters Tests
    // =========================================================================

    /**
     * Tests isParameterModified returns true by default for all parameters except after setNoSideEffects.
     */
    @Test
    public void testIsParameterModified_returnsTrueByDefault() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert - default behavior (has side effects, has external side effects)
        assertTrue(info.isParameterModified(0), "isParameterModified should return true for 'this' (index 0)");
        assertTrue(info.isParameterModified(1), "isParameterModified should return true for other parameters");
        assertTrue(info.isParameterModified(5), "isParameterModified should return true for other parameters");
    }

    /**
     * Tests isParameterModified returns false after setNoSideEffects.
     */
    @Test
    public void testIsParameterModified_returnsFalseAfterSetNoSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoSideEffects();

        // Act & Assert
        assertFalse(info.isParameterModified(0), "isParameterModified should return false after setNoSideEffects");
        assertFalse(info.isParameterModified(1), "isParameterModified should return false after setNoSideEffects");
        assertFalse(info.isParameterModified(5), "isParameterModified should return false after setNoSideEffects");
    }

    /**
     * Tests isParameterModified with setNoExternalSideEffects.
     * Only parameter 0 (this) can be modified.
     */
    @Test
    public void testIsParameterModified_withNoExternalSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoExternalSideEffects();

        // Act & Assert
        assertTrue(info.isParameterModified(0), "isParameterModified should return true for 'this' (index 0)");
        assertFalse(info.isParameterModified(1), "isParameterModified should return false for other parameters");
        assertFalse(info.isParameterModified(5), "isParameterModified should return false for other parameters");
    }

    /**
     * Tests getModifiedParameters returns -1 by default.
     */
    @Test
    public void testGetModifiedParameters_returnsAllBitsSetByDefault() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertEquals(-1L, info.getModifiedParameters(), "getModifiedParameters should return -1 by default");
    }

    /**
     * Tests getModifiedParameters returns 0 after setNoSideEffects.
     */
    @Test
    public void testGetModifiedParameters_returnsZeroAfterSetNoSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoSideEffects();

        // Act & Assert
        assertEquals(0L, info.getModifiedParameters(), "getModifiedParameters should return 0 after setNoSideEffects");
    }

    /**
     * Tests getModifiedParameters returns 1 after setNoExternalSideEffects (only 'this' can be modified).
     */
    @Test
    public void testGetModifiedParameters_returnsOneAfterSetNoExternalSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoExternalSideEffects();

        // Act & Assert
        assertEquals(1L, info.getModifiedParameters(), "getModifiedParameters should return 1 after setNoExternalSideEffects");
    }

    // =========================================================================
    // modifiesAnything Tests
    // =========================================================================

    /**
     * Tests modifiesAnything returns true by default.
     */
    @Test
    public void testModifiesAnything_returnsTrueByDefault() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.modifiesAnything(), "modifiesAnything should return true by default");
    }

    /**
     * Tests modifiesAnything returns false after setNoExternalSideEffects.
     */
    @Test
    public void testModifiesAnything_returnsFalseAfterSetNoExternalSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoExternalSideEffects();

        // Act & Assert
        assertFalse(info.modifiesAnything(), "modifiesAnything should return false after setNoExternalSideEffects");
    }

    /**
     * Tests modifiesAnything returns false after setNoSideEffects.
     */
    @Test
    public void testModifiesAnything_returnsFalseAfterSetNoSideEffects() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoSideEffects();

        // Act & Assert
        assertFalse(info.modifiesAnything(), "modifiesAnything should return false after setNoSideEffects");
    }

    // =========================================================================
    // getParameterValue Tests
    // =========================================================================

    /**
     * Tests getParameterValue returns null for any index (default behavior).
     */
    @Test
    public void testGetParameterValue_alwaysReturnsNull() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertNull(info.getParameterValue(0), "getParameterValue should return null for index 0");
        assertNull(info.getParameterValue(1), "getParameterValue should return null for index 1");
        assertNull(info.getParameterValue(5), "getParameterValue should return null for index 5");
        assertNull(info.getParameterValue(10), "getParameterValue should return null for index 10");
    }

    // =========================================================================
    // Returned Parameters Tests
    // =========================================================================

    /**
     * Tests returnsParameter returns true for any index (conservative default).
     */
    @Test
    public void testReturnsParameter_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.returnsParameter(0), "returnsParameter should return true for index 0");
        assertTrue(info.returnsParameter(1), "returnsParameter should return true for index 1");
        assertTrue(info.returnsParameter(5), "returnsParameter should return true for index 5");
        assertTrue(info.returnsParameter(10), "returnsParameter should return true for index 10");
    }

    /**
     * Tests getReturnedParameters returns -1 (all bits set, conservative default).
     */
    @Test
    public void testGetReturnedParameters_returnsAllBitsSet() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertEquals(-1L, info.getReturnedParameters(), "getReturnedParameters should return -1 (all parameters can be returned)");
    }

    // =========================================================================
    // returnsNewInstances Tests
    // =========================================================================

    /**
     * Tests returnsNewInstances returns true by default (conservative default).
     */
    @Test
    public void testReturnsNewInstances_alwaysReturnsTrue() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.returnsNewInstances(), "returnsNewInstances should return true by default");

        // Even after setting various flags
        info.setNoSideEffects();
        assertTrue(info.returnsNewInstances(), "returnsNewInstances should still return true");
    }

    // =========================================================================
    // returnsExternalValues Tests
    // =========================================================================

    /**
     * Tests returnsExternalValues returns true by default.
     */
    @Test
    public void testReturnsExternalValues_returnsTrueByDefault() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act & Assert
        assertTrue(info.returnsExternalValues(), "returnsExternalValues should return true by default");
    }

    /**
     * Tests returnsExternalValues returns false after setNoExternalReturnValues.
     */
    @Test
    public void testReturnsExternalValues_returnsFalseAfterSetNoExternalReturnValues() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoExternalReturnValues();

        // Act & Assert
        assertFalse(info.returnsExternalValues(), "returnsExternalValues should return false after setNoExternalReturnValues");
    }

    // =========================================================================
    // Static Helper Methods Tests (setMethodOptimizationInfo, getMethodOptimizationInfo)
    // =========================================================================

    /**
     * Tests setMethodOptimizationInfo creates and attaches info to a ProgramMethod.
     */
    @Test
    public void testSetMethodOptimizationInfo_attachesToMethod() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();

        // Act
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method);

        // Assert
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertNotNull(info, "MethodOptimizationInfo should be attached to the method");
        assertInstanceOf(MethodOptimizationInfo.class, info, "Processing info should be MethodOptimizationInfo");
    }

    /**
     * Tests getMethodOptimizationInfo retrieves the info from a method.
     */
    @Test
    public void testGetMethodOptimizationInfo_retrievesFromMethod() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method);

        // Act
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);

        // Assert
        assertNotNull(info, "Retrieved info should not be null");
        assertInstanceOf(MethodOptimizationInfo.class, info, "Retrieved info should be MethodOptimizationInfo");
    }

    /**
     * Tests getMethodOptimizationInfo returns the same instance that was set.
     */
    @Test
    public void testGetMethodOptimizationInfo_returnsSameInstance() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method);

        // Act
        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(method);

        // Assert
        assertSame(info1, info2, "Multiple retrievals should return the same instance");
    }

    /**
     * Tests that modifications to retrieved info are reflected in subsequent retrievals.
     */
    @Test
    public void testGetMethodOptimizationInfo_modificationsArePersisted() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method);

        // Act - retrieve and modify
        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertFalse(info1.hasNoSideEffects(), "Should initially have side effects");
        info1.setNoSideEffects();

        // Retrieve again
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(method);

        // Assert - modification should be visible
        assertTrue(info2.hasNoSideEffects(), "Modification should be visible in subsequent retrieval");
    }

    /**
     * Tests setMethodOptimizationInfo can be called multiple times (replaces previous info).
     */
    @Test
    public void testSetMethodOptimizationInfo_canBeCalledMultipleTimes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();

        // Act - call multiple times
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method);
        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(method);

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method);
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(method);

        // Assert - should get different instances
        assertNotNull(info1, "First info should not be null");
        assertNotNull(info2, "Second info should not be null");
        assertNotSame(info1, info2, "Multiple calls should create new instances");
    }

    /**
     * Tests integration of setMethodOptimizationInfo and getMethodOptimizationInfo.
     */
    @Test
    public void testSetAndGetMethodOptimizationInfo_integration() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();

        // Act - set and get
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method);
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);

        // Assert - verify it's a fresh instance with default values
        assertNotNull(info, "Retrieved info should not be null");
        assertFalse(info.hasNoSideEffects(), "New info should have side effects by default");
        assertTrue(info.isKept(), "New info should return true for isKept");
        assertNull(info.getReturnValue(), "New info should return null for getReturnValue");

        // Act - modify and verify
        info.setNoSideEffects();
        assertTrue(info.hasNoSideEffects(), "Modification should be applied");
    }

    // =========================================================================
    // Edge Cases and Integration Tests
    // =========================================================================

    /**
     * Tests that setNoSideEffects correctly sets all related flags.
     */
    @Test
    public void testSetNoSideEffects_setsAllRelatedFlags() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act
        info.setNoSideEffects();

        // Assert - all three flags should be set
        assertTrue(info.hasNoSideEffects(), "hasNoSideEffects should be true");
        assertTrue(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should be true");
        assertTrue(info.hasNoEscapingParameters(), "hasNoEscapingParameters should be true");
    }

    /**
     * Tests that setNoExternalSideEffects sets hasNoEscapingParameters.
     */
    @Test
    public void testSetNoExternalSideEffects_setsHasNoEscapingParametersOnly() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Act
        info.setNoExternalSideEffects();

        // Assert
        assertFalse(info.hasNoSideEffects(), "hasNoSideEffects should still be false");
        assertTrue(info.hasNoExternalSideEffects(), "hasNoExternalSideEffects should be true");
        assertTrue(info.hasNoEscapingParameters(), "hasNoEscapingParameters should be true");
    }

    /**
     * Tests that MethodOptimizationInfo works correctly when attached to multiple methods.
     */
    @Test
    public void testMethodOptimizationInfo_worksWithMultipleMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        // Act - set optimization info on all methods
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method3);

        // Act - modify one
        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(method1);
        info1.setNoSideEffects();

        // Retrieve all
        MethodOptimizationInfo retrievedInfo1 = MethodOptimizationInfo.getMethodOptimizationInfo(method1);
        MethodOptimizationInfo retrievedInfo2 = MethodOptimizationInfo.getMethodOptimizationInfo(method2);
        MethodOptimizationInfo retrievedInfo3 = MethodOptimizationInfo.getMethodOptimizationInfo(method3);

        // Assert - each method has independent info
        assertTrue(retrievedInfo1.hasNoSideEffects(), "Method1 should have no side effects");
        assertFalse(retrievedInfo2.hasNoSideEffects(), "Method2 should still have side effects");
        assertFalse(retrievedInfo3.hasNoSideEffects(), "Method3 should still have side effects");
    }

    /**
     * Tests that all methods can be called without throwing exceptions.
     */
    @Test
    public void testAllMethods_noExceptions() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        Value mockValue = mock(Value.class);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> {
            info.isKept();
            info.setNoSideEffects();
            info.hasNoSideEffects();
            info.setNoExternalSideEffects();
            info.hasNoExternalSideEffects();
            info.setNoEscapingParameters();
            info.hasNoEscapingParameters();
            info.setNoExternalReturnValues();
            info.hasNoExternalReturnValues();
            info.setReturnValue(mockValue);
            info.getReturnValue();
            info.hasSideEffects();
            info.canBeMadePrivate();
            info.catchesExceptions();
            info.branchesBackward();
            info.invokesSuperMethods();
            info.invokesDynamically();
            info.accessesPrivateCode();
            info.accessesPackageCode();
            info.accessesProtectedCode();
            info.hasSynchronizedBlock();
            info.assignsFinalField();
            info.returnsWithNonEmptyStack();
            info.getInvocationCount();
            info.getParameterSize();
            info.hasUnusedParameters();
            info.isParameterUsed(0);
            info.getUsedParameters();
            info.hasParameterEscaped(0);
            info.getEscapedParameters();
            info.isParameterEscaping(0);
            info.getEscapingParameters();
            info.isParameterModified(0);
            info.getModifiedParameters();
            info.modifiesAnything();
            info.getParameterValue(0);
            info.returnsParameter(0);
            info.getReturnedParameters();
            info.returnsNewInstances();
            info.returnsExternalValues();
        }, "All methods should execute without exceptions");
    }

    /**
     * Tests that MethodOptimizationInfo instances are independent.
     */
    @Test
    public void testInstances_areIndependent() {
        // Arrange
        MethodOptimizationInfo info1 = new MethodOptimizationInfo();
        MethodOptimizationInfo info2 = new MethodOptimizationInfo();

        // Act - modify first instance
        info1.setNoSideEffects();

        // Assert - second instance should not be affected
        assertTrue(info1.hasNoSideEffects(), "First instance should have no side effects");
        assertFalse(info2.hasNoSideEffects(), "Second instance should still have side effects");
        assertFalse(info1.modifiesAnything(), "First instance modifiesAnything should be false");
        assertTrue(info2.modifiesAnything(), "Second instance modifiesAnything should still be true");
    }

    /**
     * Tests complex interaction between side effects flags.
     */
    @Test
    public void testComplexInteraction_sideEffectsFlags() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();

        // Initially everything assumes the worst
        assertTrue(info.hasSideEffects());
        assertTrue(info.modifiesAnything());
        assertEquals(-1L, info.getModifiedParameters());
        assertTrue(info.isParameterEscaping(0));

        // After setting no external side effects
        info.setNoExternalSideEffects();
        assertTrue(info.hasSideEffects(), "Should still have side effects (not all)");
        assertFalse(info.modifiesAnything(), "Should not modify external objects");
        assertEquals(1L, info.getModifiedParameters(), "Only 'this' can be modified");
        assertFalse(info.isParameterEscaping(0), "Parameters should not escape");
        assertTrue(info.hasNoEscapingParameters());
    }

    /**
     * Tests that setting return value to different types works correctly.
     */
    @Test
    public void testReturnValue_differentTypes() {
        // Arrange
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        ReferenceValue mockRefValue = mock(ReferenceValue.class);

        // Act & Assert - can set different Value types
        info.setReturnValue(mockRefValue);
        assertSame(mockRefValue, info.getReturnValue(), "Should store ReferenceValue");

        Value mockValue = mock(Value.class);
        info.setReturnValue(mockValue);
        assertSame(mockValue, info.getReturnValue(), "Should store generic Value");
    }
}
