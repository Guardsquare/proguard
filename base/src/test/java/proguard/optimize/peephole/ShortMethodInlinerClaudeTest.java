package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ShortMethodInliner}.
 * Tests all methods including:
 * - Constructor with three boolean parameters
 * - Constructor with three boolean parameters and InstructionVisitor
 * - shouldInline method (determines if a method should be inlined based on code length)
 *
 * ShortMethodInliner extends MethodInliner and decides whether to inline methods based on
 * their bytecode length. It uses different thresholds for JVM (8 bytes) and Android (32 bytes).
 */
public class ShortMethodInlinerClaudeTest {

    private ShortMethodInliner inliner;
    private InstructionVisitor mockExtraVisitor;
    private Clazz mockClazz;
    private Method mockMethod;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        mockExtraVisitor = mock(InstructionVisitor.class);
        mockClazz = mock(ProgramClass.class);
        mockMethod = mock(ProgramMethod.class);
        codeAttribute = new CodeAttribute();
    }

    // ========================================
    // Constructor Tests - Three Parameters
    // ========================================

    /**
     * Tests the three-parameter constructor creates a valid instance with JVM settings.
     */
    @Test
    public void testThreeParamConstructor_JVM_NoAccessModification() {
        // Act
        inliner = new ShortMethodInliner(false, false, false);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created successfully");
        assertInstanceOf(MethodInliner.class, inliner, "ShortMethodInliner should extend MethodInliner");
    }

    /**
     * Tests the three-parameter constructor with microEdition=true.
     */
    @Test
    public void testThreeParamConstructor_MicroEdition() {
        // Act
        inliner = new ShortMethodInliner(true, false, false);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with microEdition=true");
    }

    /**
     * Tests the three-parameter constructor with android=true.
     */
    @Test
    public void testThreeParamConstructor_Android() {
        // Act
        inliner = new ShortMethodInliner(false, true, false);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with android=true");
    }

    /**
     * Tests the three-parameter constructor with allowAccessModification=true.
     */
    @Test
    public void testThreeParamConstructor_AllowAccessModification() {
        // Act
        inliner = new ShortMethodInliner(false, false, true);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with allowAccessModification=true");
    }

    /**
     * Tests the three-parameter constructor with all parameters set to true.
     */
    @Test
    public void testThreeParamConstructor_AllParametersTrue() {
        // Act
        inliner = new ShortMethodInliner(true, true, true);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with all parameters true");
    }

    /**
     * Tests that multiple instances can be created with different configurations.
     */
    @Test
    public void testThreeParamConstructor_MultipleInstancesAreIndependent() {
        // Act
        ShortMethodInliner inliner1 = new ShortMethodInliner(false, false, false);
        ShortMethodInliner inliner2 = new ShortMethodInliner(true, true, true);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotSame(inliner1, inliner2, "Inliner instances should be different");
    }

    /**
     * Tests that the three-parameter constructor completes quickly.
     */
    @Test
    public void testThreeParamConstructor_IsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        inliner = new ShortMethodInliner(false, false, true);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(inliner, "ShortMethodInliner should be created");
        assertTrue(duration < 10_000_000L, "Constructor should complete quickly (took " + duration + " ns)");
    }

    // ========================================
    // Constructor Tests - Four Parameters
    // ========================================

    /**
     * Tests the four-parameter constructor with valid non-null InstructionVisitor.
     */
    @Test
    public void testFourParamConstructor_WithValidInstructionVisitor() {
        // Act
        inliner = new ShortMethodInliner(false, false, true, mockExtraVisitor);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with InstructionVisitor");
    }

    /**
     * Tests the four-parameter constructor with null InstructionVisitor.
     */
    @Test
    public void testFourParamConstructor_WithNullInstructionVisitor() {
        // Act
        inliner = new ShortMethodInliner(false, false, true, null);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with null InstructionVisitor");
    }

    /**
     * Tests that the four-parameter constructor doesn't invoke any methods on the InstructionVisitor.
     */
    @Test
    public void testFourParamConstructor_DoesNotInvokeInstructionVisitor() {
        // Act
        inliner = new ShortMethodInliner(false, false, true, mockExtraVisitor);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created");
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests the four-parameter constructor with microEdition=true and InstructionVisitor.
     */
    @Test
    public void testFourParamConstructor_MicroEdition_WithVisitor() {
        // Act
        inliner = new ShortMethodInliner(true, false, false, mockExtraVisitor);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with microEdition=true and visitor");
    }

    /**
     * Tests the four-parameter constructor with android=true and InstructionVisitor.
     */
    @Test
    public void testFourParamConstructor_Android_WithVisitor() {
        // Act
        inliner = new ShortMethodInliner(false, true, false, mockExtraVisitor);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with android=true and visitor");
    }

    /**
     * Tests the four-parameter constructor with all boolean parameters true and InstructionVisitor.
     */
    @Test
    public void testFourParamConstructor_AllParametersTrue_WithVisitor() {
        // Act
        inliner = new ShortMethodInliner(true, true, true, mockExtraVisitor);

        // Assert
        assertNotNull(inliner, "ShortMethodInliner should be created with all parameters true and visitor");
    }

    /**
     * Tests that multiple instances can be created with different configurations including visitors.
     */
    @Test
    public void testFourParamConstructor_MultipleInstancesAreIndependent() {
        // Arrange
        InstructionVisitor mockVisitor2 = mock(InstructionVisitor.class);

        // Act
        ShortMethodInliner inliner1 = new ShortMethodInliner(false, false, false, mockExtraVisitor);
        ShortMethodInliner inliner2 = new ShortMethodInliner(true, true, true, mockVisitor2);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotSame(inliner1, inliner2, "Inliner instances should be different");
    }

    /**
     * Tests that the four-parameter constructor completes quickly.
     */
    @Test
    public void testFourParamConstructor_IsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        inliner = new ShortMethodInliner(false, true, true, mockExtraVisitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(inliner, "ShortMethodInliner should be created");
        assertTrue(duration < 10_000_000L, "Constructor should complete quickly (took " + duration + " ns)");
    }

    // ========================================
    // shouldInline Tests - JVM Mode
    // ========================================

    /**
     * Tests shouldInline returns true for code length below JVM threshold (8 bytes).
     */
    @Test
    public void testShouldInline_JVM_CodeLengthBelowThreshold_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 5;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length (5) is below JVM threshold (8)");
    }

    /**
     * Tests shouldInline returns true for code length exactly at JVM threshold (8 bytes).
     */
    @Test
    public void testShouldInline_JVM_CodeLengthAtThreshold_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 8;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length (8) equals JVM threshold (8)");
    }

    /**
     * Tests shouldInline returns false for code length above JVM threshold (8 bytes).
     */
    @Test
    public void testShouldInline_JVM_CodeLengthAboveThreshold_ReturnsFalse() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 9;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertFalse(result, "Should not inline when code length (9) is above JVM threshold (8)");
    }

    /**
     * Tests shouldInline returns true for zero code length in JVM mode.
     */
    @Test
    public void testShouldInline_JVM_ZeroCodeLength_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 0;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length is 0 in JVM mode");
    }

    /**
     * Tests shouldInline returns true for minimum code length (1 byte) in JVM mode.
     */
    @Test
    public void testShouldInline_JVM_MinimumCodeLength_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 1;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length is 1 in JVM mode");
    }

    /**
     * Tests shouldInline returns false for very large code length in JVM mode.
     */
    @Test
    public void testShouldInline_JVM_VeryLargeCodeLength_ReturnsFalse() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 1000;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertFalse(result, "Should not inline when code length (1000) is much larger than JVM threshold (8)");
    }

    // ========================================
    // shouldInline Tests - Android Mode
    // ========================================

    /**
     * Tests shouldInline returns true for code length below Android threshold (32 bytes).
     */
    @Test
    public void testShouldInline_Android_CodeLengthBelowThreshold_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 20;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length (20) is below Android threshold (32)");
    }

    /**
     * Tests shouldInline returns true for code length exactly at Android threshold (32 bytes).
     */
    @Test
    public void testShouldInline_Android_CodeLengthAtThreshold_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 32;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length (32) equals Android threshold (32)");
    }

    /**
     * Tests shouldInline returns false for code length above Android threshold (32 bytes).
     */
    @Test
    public void testShouldInline_Android_CodeLengthAboveThreshold_ReturnsFalse() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 33;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertFalse(result, "Should not inline when code length (33) is above Android threshold (32)");
    }

    /**
     * Tests shouldInline returns true for zero code length in Android mode.
     */
    @Test
    public void testShouldInline_Android_ZeroCodeLength_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 0;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length is 0 in Android mode");
    }

    /**
     * Tests shouldInline returns true for minimum code length (1 byte) in Android mode.
     */
    @Test
    public void testShouldInline_Android_MinimumCodeLength_ReturnsTrue() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 1;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length is 1 in Android mode");
    }

    /**
     * Tests shouldInline returns false for very large code length in Android mode.
     */
    @Test
    public void testShouldInline_Android_VeryLargeCodeLength_ReturnsFalse() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 1000;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertFalse(result, "Should not inline when code length (1000) is much larger than Android threshold (32)");
    }

    /**
     * Tests that Android mode has a higher threshold than JVM mode.
     * Code length of 16 should be inlined in Android mode but not in JVM mode.
     */
    @Test
    public void testShouldInline_AndroidVsJVM_DifferentThresholds() {
        // Arrange
        ShortMethodInliner jvmInliner = new ShortMethodInliner(false, false, true);
        ShortMethodInliner androidInliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 16;

        // Act
        boolean jvmResult = jvmInliner.shouldInline(mockClazz, mockMethod, codeAttribute);
        boolean androidResult = androidInliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertFalse(jvmResult, "Should not inline in JVM mode when code length (16) > 8");
        assertTrue(androidResult, "Should inline in Android mode when code length (16) < 32");
    }

    // ========================================
    // shouldInline Tests - Edge Cases
    // ========================================

    /**
     * Tests shouldInline with boundary value just below JVM threshold.
     */
    @Test
    public void testShouldInline_JVM_BoundaryBelowThreshold() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 7;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length (7) is just below JVM threshold (8)");
    }

    /**
     * Tests shouldInline with boundary value just above JVM threshold.
     */
    @Test
    public void testShouldInline_JVM_BoundaryAboveThreshold() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 9;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertFalse(result, "Should not inline when code length (9) is just above JVM threshold (8)");
    }

    /**
     * Tests shouldInline with boundary value just below Android threshold.
     */
    @Test
    public void testShouldInline_Android_BoundaryBelowThreshold() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 31;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline when code length (31) is just below Android threshold (32)");
    }

    /**
     * Tests shouldInline with boundary value just above Android threshold.
     */
    @Test
    public void testShouldInline_Android_BoundaryAboveThreshold() {
        // Arrange
        inliner = new ShortMethodInliner(false, true, true);
        codeAttribute.u4codeLength = 33;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertFalse(result, "Should not inline when code length (33) is just above Android threshold (32)");
    }

    /**
     * Tests shouldInline is consistent for the same input across multiple calls.
     */
    @Test
    public void testShouldInline_ConsistentAcrossMultipleCalls() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 5;

        // Act
        boolean result1 = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);
        boolean result2 = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);
        boolean result3 = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests shouldInline doesn't depend on microEdition parameter (only android matters).
     */
    @Test
    public void testShouldInline_MicroEditionDoesNotAffectThreshold() {
        // Arrange
        ShortMethodInliner withMicroEdition = new ShortMethodInliner(true, false, true);
        ShortMethodInliner withoutMicroEdition = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 5;

        // Act
        boolean resultWith = withMicroEdition.shouldInline(mockClazz, mockMethod, codeAttribute);
        boolean resultWithout = withoutMicroEdition.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertEquals(resultWith, resultWithout, "microEdition parameter should not affect shouldInline decision");
    }

    /**
     * Tests shouldInline doesn't depend on allowAccessModification parameter.
     */
    @Test
    public void testShouldInline_AllowAccessModificationDoesNotAffectThreshold() {
        // Arrange
        ShortMethodInliner withAccess = new ShortMethodInliner(false, false, true);
        ShortMethodInliner withoutAccess = new ShortMethodInliner(false, false, false);
        codeAttribute.u4codeLength = 5;

        // Act
        boolean resultWith = withAccess.shouldInline(mockClazz, mockMethod, codeAttribute);
        boolean resultWithout = withoutAccess.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertEquals(resultWith, resultWithout, "allowAccessModification parameter should not affect shouldInline decision");
    }

    /**
     * Tests shouldInline doesn't invoke any methods on the Clazz parameter.
     */
    @Test
    public void testShouldInline_DoesNotInteractWithClazz() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 5;

        // Act
        inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests shouldInline doesn't invoke any methods on the Method parameter.
     */
    @Test
    public void testShouldInline_DoesNotInteractWithMethod() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 5;

        // Act
        inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        verifyNoInteractions(mockMethod);
    }

    /**
     * Tests shouldInline only reads the u4codeLength field from CodeAttribute.
     */
    @Test
    public void testShouldInline_OnlyReadsCodeLength() {
        // Arrange
        inliner = new ShortMethodInliner(false, false, true);
        codeAttribute.u4codeLength = 5;

        // Act
        boolean result = inliner.shouldInline(mockClazz, mockMethod, codeAttribute);

        // Assert
        assertTrue(result, "Should inline based on code length");
        // No other fields should be accessed - we only set u4codeLength
    }
}
