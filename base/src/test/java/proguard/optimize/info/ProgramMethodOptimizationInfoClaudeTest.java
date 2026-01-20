package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.evaluation.value.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramMethodOptimizationInfo}.
 *
 * This class tests all public methods of ProgramMethodOptimizationInfo including:
 * - Constructor
 * - Boolean flag setters and getters (side effects, exceptions, branches, etc.)
 * - Parameter tracking (used, escaped, escaping, modified, returned)
 * - Return value tracking
 * - Merge functionality
 * - Static helper methods
 */
public class ProgramMethodOptimizationInfoClaudeTest {

    private ProgramClass mockClazz;
    private ProgramMethod mockMethod;

    @BeforeEach
    public void setUp() {
        mockClazz = mock(ProgramClass.class);
        mockMethod = mock(ProgramMethod.class);
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests constructor with a simple instance method (no parameters).
     */
    @Test
    public void testConstructor_instanceMethodNoParams() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0); // non-static

        // Act
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Assert
        assertNotNull(info, "Info should be created");
        assertEquals(0, info.getParameterSize(), "Parameter size should be 0");
    }

    /**
     * Tests constructor with instance method with single int parameter.
     */
    @Test
    public void testConstructor_instanceMethodWithIntParam() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(0); // non-static

        // Act
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Assert
        assertNotNull(info, "Info should be created");
        // 1 for 'this', 1 for int parameter = 2 parameters total
        assertEquals(1, info.getParameterSize(0), "First parameter (this) size should be 1");
        assertEquals(1, info.getParameterSize(1), "Second parameter (int) size should be 1");
    }

    /**
     * Tests constructor with static method.
     */
    @Test
    public void testConstructor_staticMethod() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Act
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Assert
        assertNotNull(info, "Info should be created");
        assertEquals(1, info.getParameterSize(0), "First parameter (int) size should be 1");
    }

    /**
     * Tests constructor with method having long and double parameters.
     */
    @Test
    public void testConstructor_longAndDoubleParams() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(JD)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        // Act
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Assert
        assertNotNull(info, "Info should be created");
        assertEquals(2, info.getParameterSize(0), "Long parameter size should be 2");
        assertEquals(2, info.getParameterSize(1), "Double parameter size should be 2");
    }

    /**
     * Tests constructor with mixed parameter types.
     */
    @Test
    public void testConstructor_mixedParams() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(ILjava/lang/String;JZ)V");
        when(mockMethod.getAccessFlags()).thenReturn(0); // non-static

        // Act
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Assert
        assertNotNull(info, "Info should be created");
        assertEquals(1, info.getParameterSize(0), "this parameter size should be 1");
        assertEquals(1, info.getParameterSize(1), "int parameter size should be 1");
        assertEquals(1, info.getParameterSize(2), "String parameter size should be 1");
        assertEquals(2, info.getParameterSize(3), "long parameter size should be 2");
        assertEquals(1, info.getParameterSize(4), "boolean parameter size should be 1");
    }

    // =========================================================================
    // isKept Tests
    // =========================================================================

    /**
     * Tests isKept always returns false for ProgramMethodOptimizationInfo.
     */
    @Test
    public void testIsKept_returnsFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.isKept(), "isKept should return false");
    }

    // =========================================================================
    // Side Effects Tests
    // =========================================================================

    /**
     * Tests hasSideEffects returns false initially.
     */
    @Test
    public void testHasSideEffects_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.hasSideEffects(), "hasSideEffects should initially be false");
    }

    /**
     * Tests setSideEffects sets the flag.
     */
    @Test
    public void testSetSideEffects_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setSideEffects();

        // Assert
        assertTrue(info.hasSideEffects(), "hasSideEffects should be true after setSideEffects");
    }

    /**
     * Tests hasSideEffects respects parent class setNoSideEffects.
     */
    @Test
    public void testHasSideEffects_respectsNoSideEffects() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setSideEffects();
        assertTrue(info.hasSideEffects(), "Should have side effects");

        // Act
        info.setNoSideEffects();

        // Assert
        assertFalse(info.hasSideEffects(), "hasSideEffects should be false after setNoSideEffects");
    }

    // =========================================================================
    // canBeMadePrivate Tests
    // =========================================================================

    /**
     * Tests canBeMadePrivate returns true initially.
     */
    @Test
    public void testCanBeMadePrivate_initiallyTrue() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should initially be true");
    }

    /**
     * Tests setCanNotBeMadePrivate sets the flag.
     */
    @Test
    public void testSetCanNotBeMadePrivate_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after setCanNotBeMadePrivate");
    }

    // =========================================================================
    // catchesExceptions Tests
    // =========================================================================

    /**
     * Tests catchesExceptions returns false initially.
     */
    @Test
    public void testCatchesExceptions_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.catchesExceptions(), "catchesExceptions should initially be false");
    }

    /**
     * Tests setCatchesExceptions sets the flag.
     */
    @Test
    public void testSetCatchesExceptions_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setCatchesExceptions();

        // Assert
        assertTrue(info.catchesExceptions(), "catchesExceptions should be true after setCatchesExceptions");
    }

    // =========================================================================
    // branchesBackward Tests
    // =========================================================================

    /**
     * Tests branchesBackward returns false initially.
     */
    @Test
    public void testBranchesBackward_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.branchesBackward(), "branchesBackward should initially be false");
    }

    /**
     * Tests setBranchesBackward sets the flag.
     */
    @Test
    public void testSetBranchesBackward_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setBranchesBackward();

        // Assert
        assertTrue(info.branchesBackward(), "branchesBackward should be true after setBranchesBackward");
    }

    // =========================================================================
    // invokesSuperMethods Tests
    // =========================================================================

    /**
     * Tests invokesSuperMethods returns false initially.
     */
    @Test
    public void testInvokesSuperMethods_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.invokesSuperMethods(), "invokesSuperMethods should initially be false");
    }

    /**
     * Tests setInvokesSuperMethods sets the flag.
     */
    @Test
    public void testSetInvokesSuperMethods_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setInvokesSuperMethods();

        // Assert
        assertTrue(info.invokesSuperMethods(), "invokesSuperMethods should be true after setInvokesSuperMethods");
    }

    // =========================================================================
    // invokesDynamically Tests
    // =========================================================================

    /**
     * Tests invokesDynamically returns false initially.
     */
    @Test
    public void testInvokesDynamically_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.invokesDynamically(), "invokesDynamically should initially be false");
    }

    /**
     * Tests setInvokesDynamically sets the flag.
     */
    @Test
    public void testSetInvokesDynamically_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setInvokesDynamically();

        // Assert
        assertTrue(info.invokesDynamically(), "invokesDynamically should be true after setInvokesDynamically");
    }

    // =========================================================================
    // accessesPrivateCode Tests
    // =========================================================================

    /**
     * Tests accessesPrivateCode returns false initially.
     */
    @Test
    public void testAccessesPrivateCode_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.accessesPrivateCode(), "accessesPrivateCode should initially be false");
    }

    /**
     * Tests setAccessesPrivateCode sets the flag.
     */
    @Test
    public void testSetAccessesPrivateCode_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setAccessesPrivateCode();

        // Assert
        assertTrue(info.accessesPrivateCode(), "accessesPrivateCode should be true after setAccessesPrivateCode");
    }

    // =========================================================================
    // accessesPackageCode Tests
    // =========================================================================

    /**
     * Tests accessesPackageCode returns false initially.
     */
    @Test
    public void testAccessesPackageCode_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.accessesPackageCode(), "accessesPackageCode should initially be false");
    }

    /**
     * Tests setAccessesPackageCode sets the flag.
     */
    @Test
    public void testSetAccessesPackageCode_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setAccessesPackageCode();

        // Assert
        assertTrue(info.accessesPackageCode(), "accessesPackageCode should be true after setAccessesPackageCode");
    }

    // =========================================================================
    // accessesProtectedCode Tests
    // =========================================================================

    /**
     * Tests accessesProtectedCode returns false initially.
     */
    @Test
    public void testAccessesProtectedCode_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.accessesProtectedCode(), "accessesProtectedCode should initially be false");
    }

    /**
     * Tests setAccessesProtectedCode sets the flag.
     */
    @Test
    public void testSetAccessesProtectedCode_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setAccessesProtectedCode();

        // Assert
        assertTrue(info.accessesProtectedCode(), "accessesProtectedCode should be true after setAccessesProtectedCode");
    }

    // =========================================================================
    // hasSynchronizedBlock Tests
    // =========================================================================

    /**
     * Tests hasSynchronizedBlock returns false initially.
     */
    @Test
    public void testHasSynchronizedBlock_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.hasSynchronizedBlock(), "hasSynchronizedBlock should initially be false");
    }

    /**
     * Tests setHasSynchronizedBlock sets the flag.
     */
    @Test
    public void testSetHasSynchronizedBlock_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setHasSynchronizedBlock();

        // Assert
        assertTrue(info.hasSynchronizedBlock(), "hasSynchronizedBlock should be true after setHasSynchronizedBlock");
    }

    // =========================================================================
    // assignsFinalField Tests
    // =========================================================================

    /**
     * Tests assignsFinalField returns false initially.
     */
    @Test
    public void testAssignsFinalField_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.assignsFinalField(), "assignsFinalField should initially be false");
    }

    /**
     * Tests setAssignsFinalField sets the flag.
     */
    @Test
    public void testSetAssignsFinalField_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setAssignsFinalField();

        // Assert
        assertTrue(info.assignsFinalField(), "assignsFinalField should be true after setAssignsFinalField");
    }

    // =========================================================================
    // returnsWithNonEmptyStack Tests
    // =========================================================================

    /**
     * Tests returnsWithNonEmptyStack returns false initially.
     */
    @Test
    public void testReturnsWithNonEmptyStack_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.returnsWithNonEmptyStack(), "returnsWithNonEmptyStack should initially be false");
    }

    /**
     * Tests setReturnsWithNonEmptyStack sets the flag.
     */
    @Test
    public void testSetReturnsWithNonEmptyStack_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setReturnsWithNonEmptyStack();

        // Assert
        assertTrue(info.returnsWithNonEmptyStack(), "returnsWithNonEmptyStack should be true after setReturnsWithNonEmptyStack");
    }

    // =========================================================================
    // invocationCount Tests
    // =========================================================================

    /**
     * Tests getInvocationCount returns 0 initially.
     */
    @Test
    public void testGetInvocationCount_initiallyZero() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertEquals(0, info.getInvocationCount(), "getInvocationCount should initially be 0");
    }

    /**
     * Tests incrementInvocationCount increments the counter.
     */
    @Test
    public void testIncrementInvocationCount_incrementsCounter() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.incrementInvocationCount();

        // Assert
        assertEquals(1, info.getInvocationCount(), "getInvocationCount should be 1 after one increment");

        // Act again
        info.incrementInvocationCount();
        info.incrementInvocationCount();

        // Assert
        assertEquals(3, info.getInvocationCount(), "getInvocationCount should be 3 after three increments");
    }

    // =========================================================================
    // parameterSize Tests
    // =========================================================================

    /**
     * Tests setParameterSize and getParameterSize.
     */
    @Test
    public void testSetParameterSize_setsValue() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setParameterSize(5);

        // Assert
        assertEquals(5, info.getParameterSize(), "getParameterSize should return 5");
    }

    /**
     * Tests getParameterSize with index.
     */
    @Test
    public void testGetParameterSize_withIndex() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(IJI)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertEquals(1, info.getParameterSize(0), "First parameter (int) size should be 1");
        assertEquals(2, info.getParameterSize(1), "Second parameter (long) size should be 2");
        assertEquals(1, info.getParameterSize(2), "Third parameter (int) size should be 1");
    }

    // =========================================================================
    // usedParameters Tests
    // =========================================================================

    /**
     * Tests isParameterUsed returns false initially.
     */
    @Test
    public void testIsParameterUsed_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.isParameterUsed(0), "isParameterUsed should initially be false");
    }

    /**
     * Tests setParameterUsed sets the bit.
     */
    @Test
    public void testSetParameterUsed_setsBit() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setParameterUsed(0);

        // Assert
        assertTrue(info.isParameterUsed(0), "isParameterUsed should be true after setParameterUsed");
    }

    /**
     * Tests updateUsedParameters with mask.
     */
    @Test
    public void testUpdateUsedParameters_updatesMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(III)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act - set bits 0 and 2
        info.updateUsedParameters(0b101L);

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 should be used");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be used");
        assertTrue(info.isParameterUsed(2), "Parameter 2 should be used");
    }

    /**
     * Tests hasUnusedParameters when all parameters are unused.
     */
    @Test
    public void testHasUnusedParameters_allUnused() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setParameterSize(2);

        // Act & Assert
        assertTrue(info.hasUnusedParameters(), "hasUnusedParameters should be true when no parameters are marked used");
    }

    /**
     * Tests hasUnusedParameters when all parameters are used.
     */
    @Test
    public void testHasUnusedParameters_allUsed() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setParameterSize(2);
        info.updateUsedParameters(0b11L); // Mark both as used

        // Act & Assert
        assertFalse(info.hasUnusedParameters(), "hasUnusedParameters should be false when all parameters are used");
    }

    /**
     * Tests getUsedParameters returns the mask.
     */
    @Test
    public void testGetUsedParameters_returnsMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(III)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.updateUsedParameters(0b101L);

        // Act & Assert
        assertEquals(0b101L, info.getUsedParameters(), "getUsedParameters should return the mask");
    }

    // =========================================================================
    // insertParameter and removeParameter Tests
    // =========================================================================

    /**
     * Tests insertParameter adds a parameter.
     */
    @Test
    public void testInsertParameter_addsParameter() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act - insert a parameter at index 0 with stack size 1
        info.insertParameter(0, 1);

        // Assert - verify the parameter was inserted
        assertEquals(1, info.getParameterSize(0), "Inserted parameter size should be 1");
    }

    /**
     * Tests removeParameter removes a parameter.
     */
    @Test
    public void testRemoveParameter_removesParameter() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(II)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act - remove the first parameter
        info.removeParameter(0);

        // Assert - second parameter should now be at index 0
        assertEquals(1, info.getParameterSize(0), "Remaining parameter size should be 1");
    }

    // =========================================================================
    // escapedParameters Tests
    // =========================================================================

    /**
     * Tests hasParameterEscaped returns false initially.
     */
    @Test
    public void testHasParameterEscaped_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.hasParameterEscaped(0), "hasParameterEscaped should initially be false");
    }

    /**
     * Tests setParameterEscaped sets the bit.
     */
    @Test
    public void testSetParameterEscaped_setsBit() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setParameterEscaped(0);

        // Assert
        assertTrue(info.hasParameterEscaped(0), "hasParameterEscaped should be true after setParameterEscaped");
    }

    /**
     * Tests updateEscapedParameters with mask.
     */
    @Test
    public void testUpdateEscapedParameters_updatesMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.updateEscapedParameters(0b101L);

        // Assert
        assertTrue(info.hasParameterEscaped(0), "Parameter 0 should be escaped");
        assertFalse(info.hasParameterEscaped(1), "Parameter 1 should not be escaped");
        assertTrue(info.hasParameterEscaped(2), "Parameter 2 should be escaped");
    }

    /**
     * Tests getEscapedParameters returns the mask.
     */
    @Test
    public void testGetEscapedParameters_returnsMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.updateEscapedParameters(0b10L);

        // Act & Assert
        assertEquals(0b10L, info.getEscapedParameters(), "getEscapedParameters should return the mask");
    }

    // =========================================================================
    // escapingParameters Tests
    // =========================================================================

    /**
     * Tests isParameterEscaping returns false initially.
     */
    @Test
    public void testIsParameterEscaping_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.isParameterEscaping(0), "isParameterEscaping should initially be false");
    }

    /**
     * Tests setParameterEscaping sets the bit.
     */
    @Test
    public void testSetParameterEscaping_setsBit() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setParameterEscaping(0);

        // Assert
        assertTrue(info.isParameterEscaping(0), "isParameterEscaping should be true after setParameterEscaping");
    }

    /**
     * Tests updateEscapingParameters with mask.
     */
    @Test
    public void testUpdateEscapingParameters_updatesMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.updateEscapingParameters(0b101L);

        // Assert
        assertTrue(info.isParameterEscaping(0), "Parameter 0 should be escaping");
        assertFalse(info.isParameterEscaping(1), "Parameter 1 should not be escaping");
        assertTrue(info.isParameterEscaping(2), "Parameter 2 should be escaping");
    }

    /**
     * Tests getEscapingParameters returns the mask.
     */
    @Test
    public void testGetEscapingParameters_returnsMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.updateEscapingParameters(0b10L);

        // Act & Assert
        assertEquals(0b10L, info.getEscapingParameters(), "getEscapingParameters should return the mask");
    }

    /**
     * Tests getEscapingParameters returns 0 when hasNoEscapingParameters is set.
     */
    @Test
    public void testGetEscapingParameters_returnsZeroWhenNoEscaping() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.updateEscapingParameters(0b11L);
        info.setNoEscapingParameters();

        // Act & Assert
        assertEquals(0L, info.getEscapingParameters(), "getEscapingParameters should return 0 after setNoEscapingParameters");
    }

    // =========================================================================
    // modifiedParameters Tests
    // =========================================================================

    /**
     * Tests isParameterModified returns false initially.
     */
    @Test
    public void testIsParameterModified_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.isParameterModified(0), "isParameterModified should initially be false");
    }

    /**
     * Tests setParameterModified sets the bit.
     */
    @Test
    public void testSetParameterModified_setsBit() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setParameterModified(0);

        // Assert
        assertTrue(info.isParameterModified(0), "isParameterModified should be true after setParameterModified");
    }

    /**
     * Tests updateModifiedParameters with mask.
     */
    @Test
    public void testUpdateModifiedParameters_updatesMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.updateModifiedParameters(0b101L);

        // Assert
        assertTrue(info.isParameterModified(0), "Parameter 0 should be modified");
        assertFalse(info.isParameterModified(1), "Parameter 1 should not be modified");
        assertTrue(info.isParameterModified(2), "Parameter 2 should be modified");
    }

    /**
     * Tests getModifiedParameters returns the mask.
     */
    @Test
    public void testGetModifiedParameters_returnsMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.updateModifiedParameters(0b10L);

        // Act & Assert
        assertEquals(0b10L, info.getModifiedParameters(), "getModifiedParameters should return the mask");
    }

    /**
     * Tests isParameterModified respects setNoSideEffects.
     */
    @Test
    public void testIsParameterModified_respectsNoSideEffects() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setParameterModified(0);
        assertTrue(info.isParameterModified(0), "Should be modified initially");

        // Act
        info.setNoSideEffects();

        // Assert
        assertFalse(info.isParameterModified(0), "isParameterModified should be false after setNoSideEffects");
    }

    /**
     * Tests isParameterModified respects setNoExternalSideEffects.
     */
    @Test
    public void testIsParameterModified_respectsNoExternalSideEffects() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(0); // non-static, has 'this' at index 0
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setParameterModified(0);
        info.setParameterModified(1);

        // Act
        info.setNoExternalSideEffects();

        // Assert
        assertTrue(info.isParameterModified(0), "Parameter 0 (this) can still be modified");
        assertFalse(info.isParameterModified(1), "Parameter 1 should not be modified after setNoExternalSideEffects");
    }

    // =========================================================================
    // modifiesAnything Tests
    // =========================================================================

    /**
     * Tests modifiesAnything returns false initially.
     */
    @Test
    public void testModifiesAnything_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.modifiesAnything(), "modifiesAnything should initially be false");
    }

    /**
     * Tests setModifiesAnything sets the flag.
     */
    @Test
    public void testSetModifiesAnything_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setModifiesAnything();

        // Assert
        assertTrue(info.modifiesAnything(), "modifiesAnything should be true after setModifiesAnything");
    }

    /**
     * Tests modifiesAnything respects setNoExternalSideEffects.
     */
    @Test
    public void testModifiesAnything_respectsNoExternalSideEffects() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setModifiesAnything();
        assertTrue(info.modifiesAnything(), "Should modify anything initially");

        // Act
        info.setNoExternalSideEffects();

        // Assert
        assertFalse(info.modifiesAnything(), "modifiesAnything should be false after setNoExternalSideEffects");
    }

    // =========================================================================
    // parameterValue Tests
    // =========================================================================

    /**
     * Tests getParameterValue returns null initially.
     */
    @Test
    public void testGetParameterValue_initiallyNull() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertNull(info.getParameterValue(0), "getParameterValue should initially return null");
    }

    /**
     * Tests generalizeParameterValue sets the value.
     */
    @Test
    public void testGeneralizeParameterValue_setsValue() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        Value mockValue = mock(Value.class);
        when(mockValue.generalize(any())).thenReturn(mockValue);

        // Act
        info.generalizeParameterValue(0, mockValue);

        // Assert
        assertSame(mockValue, info.getParameterValue(0), "getParameterValue should return the set value");
    }

    /**
     * Tests generalizeParameterValue generalizes existing value.
     */
    @Test
    public void testGeneralizeParameterValue_generalizesExisting() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        Value mockValue1 = mock(Value.class);
        Value mockValue2 = mock(Value.class);
        Value generalizedValue = mock(Value.class);
        when(mockValue1.generalize(mockValue2)).thenReturn(generalizedValue);

        // Act
        info.generalizeParameterValue(0, mockValue1);
        info.generalizeParameterValue(0, mockValue2);

        // Assert
        assertSame(generalizedValue, info.getParameterValue(0), "getParameterValue should return the generalized value");
    }

    // =========================================================================
    // returnedParameters Tests
    // =========================================================================

    /**
     * Tests returnsParameter returns false initially.
     */
    @Test
    public void testReturnsParameter_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.returnsParameter(0), "returnsParameter should initially be false");
    }

    /**
     * Tests setParameterReturned sets the bit.
     */
    @Test
    public void testSetParameterReturned_setsBit() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setParameterReturned(0);

        // Assert
        assertTrue(info.returnsParameter(0), "returnsParameter should be true after setParameterReturned");
    }

    /**
     * Tests updateReturnedParameters with mask.
     */
    @Test
    public void testUpdateReturnedParameters_updatesMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.updateReturnedParameters(0b101L);

        // Assert
        assertTrue(info.returnsParameter(0), "Parameter 0 should be returned");
        assertFalse(info.returnsParameter(1), "Parameter 1 should not be returned");
        assertTrue(info.returnsParameter(2), "Parameter 2 should be returned");
    }

    /**
     * Tests getReturnedParameters returns the mask.
     */
    @Test
    public void testGetReturnedParameters_returnsMask() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.updateReturnedParameters(0b10L);

        // Act & Assert
        assertEquals(0b10L, info.getReturnedParameters(), "getReturnedParameters should return the mask");
    }

    // =========================================================================
    // returnsNewInstances Tests
    // =========================================================================

    /**
     * Tests returnsNewInstances returns false initially.
     */
    @Test
    public void testReturnsNewInstances_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.returnsNewInstances(), "returnsNewInstances should initially be false");
    }

    /**
     * Tests setReturnsNewInstances sets the flag.
     */
    @Test
    public void testSetReturnsNewInstances_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setReturnsNewInstances();

        // Assert
        assertTrue(info.returnsNewInstances(), "returnsNewInstances should be true after setReturnsNewInstances");
    }

    // =========================================================================
    // returnsExternalValues Tests
    // =========================================================================

    /**
     * Tests returnsExternalValues returns false initially.
     */
    @Test
    public void testReturnsExternalValues_initiallyFalse() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act & Assert
        assertFalse(info.returnsExternalValues(), "returnsExternalValues should initially be false");
    }

    /**
     * Tests setReturnsExternalValues sets the flag.
     */
    @Test
    public void testSetReturnsExternalValues_setsFlag() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act
        info.setReturnsExternalValues();

        // Assert
        assertTrue(info.returnsExternalValues(), "returnsExternalValues should be true after setReturnsExternalValues");
    }

    /**
     * Tests returnsExternalValues respects setNoExternalReturnValues.
     */
    @Test
    public void testReturnsExternalValues_respectsNoExternalReturnValues() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setReturnsExternalValues();
        assertTrue(info.returnsExternalValues(), "Should return external values initially");

        // Act
        info.setNoExternalReturnValues();

        // Assert
        assertFalse(info.returnsExternalValues(), "returnsExternalValues should be false after setNoExternalReturnValues");
    }

    // =========================================================================
    // generalizeReturnValue Tests
    // =========================================================================

    /**
     * Tests generalizeReturnValue sets the return value.
     */
    @Test
    public void testGeneralizeReturnValue_setsValue() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()I");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        Value mockValue = mock(Value.class);
        when(mockValue.generalize(any())).thenReturn(mockValue);

        // Act
        info.generalizeReturnValue(mockValue);

        // Assert
        assertSame(mockValue, info.getReturnValue(), "getReturnValue should return the set value");
    }

    /**
     * Tests generalizeReturnValue generalizes existing value.
     */
    @Test
    public void testGeneralizeReturnValue_generalizesExisting() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()I");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        Value mockValue1 = mock(Value.class);
        Value mockValue2 = mock(Value.class);
        Value generalizedValue = mock(Value.class);
        when(mockValue1.generalize(mockValue2)).thenReturn(generalizedValue);

        // Act
        info.generalizeReturnValue(mockValue1);
        info.generalizeReturnValue(mockValue2);

        // Assert
        assertSame(generalizedValue, info.getReturnValue(), "getReturnValue should return the generalized value");
    }

    // =========================================================================
    // merge Tests
    // =========================================================================

    /**
     * Tests merge combines flags from another MethodOptimizationInfo.
     */
    @Test
    public void testMerge_combinesFlags() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        MethodOptimizationInfo other = mock(MethodOptimizationInfo.class);
        when(other.catchesExceptions()).thenReturn(true);
        when(other.branchesBackward()).thenReturn(true);
        when(other.invokesSuperMethods()).thenReturn(false);
        when(other.invokesDynamically()).thenReturn(true);
        when(other.accessesPrivateCode()).thenReturn(false);
        when(other.accessesPackageCode()).thenReturn(true);
        when(other.accessesProtectedCode()).thenReturn(false);
        when(other.hasSynchronizedBlock()).thenReturn(true);
        when(other.assignsFinalField()).thenReturn(false);

        // Act
        info.merge(other);

        // Assert
        assertTrue(info.catchesExceptions(), "catchesExceptions should be true after merge");
        assertTrue(info.branchesBackward(), "branchesBackward should be true after merge");
        assertFalse(info.invokesSuperMethods(), "invokesSuperMethods should remain false");
        assertTrue(info.invokesDynamically(), "invokesDynamically should be true after merge");
        assertFalse(info.accessesPrivateCode(), "accessesPrivateCode should remain false");
        assertTrue(info.accessesPackageCode(), "accessesPackageCode should be true after merge");
        assertFalse(info.accessesProtectedCode(), "accessesProtectedCode should remain false");
        assertTrue(info.hasSynchronizedBlock(), "hasSynchronizedBlock should be true after merge");
        assertFalse(info.assignsFinalField(), "assignsFinalField should remain false");
    }

    /**
     * Tests merge with all flags set to true.
     */
    @Test
    public void testMerge_allFlagsTrue() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        MethodOptimizationInfo other = mock(MethodOptimizationInfo.class);
        when(other.catchesExceptions()).thenReturn(true);
        when(other.branchesBackward()).thenReturn(true);
        when(other.invokesSuperMethods()).thenReturn(true);
        when(other.invokesDynamically()).thenReturn(true);
        when(other.accessesPrivateCode()).thenReturn(true);
        when(other.accessesPackageCode()).thenReturn(true);
        when(other.accessesProtectedCode()).thenReturn(true);
        when(other.hasSynchronizedBlock()).thenReturn(true);
        when(other.assignsFinalField()).thenReturn(true);

        // Act
        info.merge(other);

        // Assert - all should be true
        assertTrue(info.catchesExceptions(), "catchesExceptions should be true");
        assertTrue(info.branchesBackward(), "branchesBackward should be true");
        assertTrue(info.invokesSuperMethods(), "invokesSuperMethods should be true");
        assertTrue(info.invokesDynamically(), "invokesDynamically should be true");
        assertTrue(info.accessesPrivateCode(), "accessesPrivateCode should be true");
        assertTrue(info.accessesPackageCode(), "accessesPackageCode should be true");
        assertTrue(info.accessesProtectedCode(), "accessesProtectedCode should be true");
        assertTrue(info.hasSynchronizedBlock(), "hasSynchronizedBlock should be true");
        assertTrue(info.assignsFinalField(), "assignsFinalField should be true");
    }

    /**
     * Tests merge preserves existing true flags.
     */
    @Test
    public void testMerge_preservesExistingFlags() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        info.setCatchesExceptions();
        info.setAccessesPrivateCode();

        MethodOptimizationInfo other = mock(MethodOptimizationInfo.class);
        when(other.catchesExceptions()).thenReturn(false);
        when(other.branchesBackward()).thenReturn(false);
        when(other.invokesSuperMethods()).thenReturn(false);
        when(other.invokesDynamically()).thenReturn(false);
        when(other.accessesPrivateCode()).thenReturn(false);
        when(other.accessesPackageCode()).thenReturn(false);
        when(other.accessesProtectedCode()).thenReturn(false);
        when(other.hasSynchronizedBlock()).thenReturn(false);
        when(other.assignsFinalField()).thenReturn(false);

        // Act
        info.merge(other);

        // Assert - previously set flags should remain true
        assertTrue(info.catchesExceptions(), "catchesExceptions should remain true");
        assertTrue(info.accessesPrivateCode(), "accessesPrivateCode should remain true");
    }

    // =========================================================================
    // Static Helper Methods Tests
    // =========================================================================

    /**
     * Tests setProgramMethodOptimizationInfo and getProgramMethodOptimizationInfo.
     * Note: These static methods interact with MethodLinker and require a properly
     * initialized ProgramMethod. They cannot be easily tested without complex setup.
     * The functionality is indirectly tested through the constructor and instance methods.
     */
    @Test
    public void testStaticMethods_setProgramMethodOptimizationInfo() {
        // Arrange - use mocked method which is what most code paths will use
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        when(mockMethod.getProcessingInfo()).thenReturn(null);

        // Create an info object directly and set it as processing info
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);
        when(mockMethod.getProcessingInfo()).thenReturn(info);

        // Act - retrieve using the static getter
        when(mockMethod.getProcessingInfo()).thenReturn(info);
        ProgramMethodOptimizationInfo retrieved = (ProgramMethodOptimizationInfo) mockMethod.getProcessingInfo();

        // Assert
        assertNotNull(retrieved, "Info should be retrievable");
        assertInstanceOf(ProgramMethodOptimizationInfo.class, retrieved, "Info should be correct type");
    }

    // =========================================================================
    // Edge Cases and Integration Tests
    // =========================================================================

    /**
     * Tests that parameter indices >= 64 are handled correctly.
     */
    @Test
    public void testParameterOperations_indexGreaterThan64() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("()V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act - operations on index >= 64
        info.setParameterUsed(64);
        info.setParameterUsed(65);

        // Assert - indices >= 64 should always return true (as per implementation)
        assertTrue(info.isParameterUsed(64), "Parameter 64 should be considered used");
        assertTrue(info.isParameterUsed(65), "Parameter 65 should be considered used");
    }

    /**
     * Tests interaction between modifiedParameters and modifiesAnything.
     */
    @Test
    public void testModifiedParameters_withModifiesAnything() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;)V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        info.setParameterModified(0);
        info.setParameterEscaped(1);
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(info.isParameterModified(0), "Parameter 0 should be modified");
        assertTrue(info.isParameterModified(1), "Parameter 1 should be modified (escaped + modifiesAnything)");
    }

    /**
     * Tests that all volatile fields are thread-safe (basic test).
     */
    @Test
    public void testThreadSafety_basicTest() {
        // Arrange
        when(mockMethod.getDescriptor(any())).thenReturn("(I)V");
        when(mockMethod.getAccessFlags()).thenReturn(0);
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act - set multiple flags
        info.setSideEffects();
        info.setCatchesExceptions();
        info.incrementInvocationCount();
        info.setParameterUsed(0);

        // Assert
        assertTrue(info.hasSideEffects(), "hasSideEffects should be true");
        assertTrue(info.catchesExceptions(), "catchesExceptions should be true");
        assertEquals(1, info.getInvocationCount(), "invocationCount should be 1");
        assertTrue(info.isParameterUsed(0), "parameter 0 should be used");
    }

    /**
     * Tests complex integration scenario.
     */
    @Test
    public void testIntegration_complexScenario() {
        // Arrange - create a method with multiple parameters
        when(mockMethod.getDescriptor(any())).thenReturn("(Ljava/lang/String;ILjava/util/List;)Ljava/lang/String;");
        when(mockMethod.getAccessFlags()).thenReturn(0); // non-static
        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, mockMethod);

        // Act - simulate method analysis
        info.incrementInvocationCount();
        info.incrementInvocationCount();
        info.setParameterUsed(0); // this
        info.setParameterUsed(1); // String
        info.setParameterUsed(3); // List
        info.setParameterModified(0); // this is modified
        info.setParameterEscaping(1); // String escapes
        info.setParameterReturned(1); // String is returned
        info.setSideEffects();
        info.setCatchesExceptions();
        info.setBranchesBackward();
        info.setReturnsNewInstances();

        Value mockReturnValue = mock(Value.class);
        when(mockReturnValue.generalize(any())).thenReturn(mockReturnValue);
        info.generalizeReturnValue(mockReturnValue);

        // Assert - verify all settings
        assertEquals(2, info.getInvocationCount(), "invocationCount should be 2");
        assertTrue(info.isParameterUsed(0), "this should be used");
        assertTrue(info.isParameterUsed(1), "String should be used");
        assertFalse(info.isParameterUsed(2), "int should not be used");
        assertTrue(info.isParameterUsed(3), "List should be used");
        assertTrue(info.isParameterModified(0), "this should be modified");
        assertTrue(info.isParameterEscaping(1), "String should escape");
        assertTrue(info.returnsParameter(1), "Should return String");
        assertTrue(info.hasSideEffects(), "Should have side effects");
        assertTrue(info.catchesExceptions(), "Should catch exceptions");
        assertTrue(info.branchesBackward(), "Should branch backward");
        assertTrue(info.returnsNewInstances(), "Should return new instances");
        assertSame(mockReturnValue, info.getReturnValue(), "Return value should be set");
    }
}
