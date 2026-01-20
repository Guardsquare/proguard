package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.CodeAttribute;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CodeAttributeOptimizationInfo}.
 *
 * This class stores optimization information that can be attached to a code attribute
 * during ProGuard's optimization process. The tests cover all public methods including:
 * - Constructor and initialization
 * - isKept() method to check if the code attribute is marked as kept
 * - Static helper methods for setting and retrieving optimization info
 */
public class CodeAttributeOptimizationInfoClaudeTest {

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a CodeAttributeOptimizationInfo instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        CodeAttributeOptimizationInfo info = new CodeAttributeOptimizationInfo();

        // Assert
        assertNotNull(info, "CodeAttributeOptimizationInfo instance should be created");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        CodeAttributeOptimizationInfo info1 = new CodeAttributeOptimizationInfo();
        CodeAttributeOptimizationInfo info2 = new CodeAttributeOptimizationInfo();
        CodeAttributeOptimizationInfo info3 = new CodeAttributeOptimizationInfo();

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
     * Tests that isKept() returns true, indicating the code attribute should be preserved.
     * The implementation always returns true, marking all code attributes with this info as kept.
     */
    @Test
    public void testIsKept_returnsTrue() {
        // Arrange
        CodeAttributeOptimizationInfo info = new CodeAttributeOptimizationInfo();

        // Act
        boolean result = info.isKept();

        // Assert
        assertTrue(result, "isKept() should always return true");
    }

    /**
     * Tests that isKept() consistently returns true across multiple invocations.
     */
    @Test
    public void testIsKept_consistentlyReturnsTrue() {
        // Arrange
        CodeAttributeOptimizationInfo info = new CodeAttributeOptimizationInfo();

        // Act & Assert - multiple calls should all return true
        assertTrue(info.isKept(), "First call should return true");
        assertTrue(info.isKept(), "Second call should return true");
        assertTrue(info.isKept(), "Third call should return true");
    }

    /**
     * Tests that different instances of CodeAttributeOptimizationInfo all return true from isKept().
     */
    @Test
    public void testIsKept_returnsTrue_forMultipleInstances() {
        // Arrange
        CodeAttributeOptimizationInfo info1 = new CodeAttributeOptimizationInfo();
        CodeAttributeOptimizationInfo info2 = new CodeAttributeOptimizationInfo();
        CodeAttributeOptimizationInfo info3 = new CodeAttributeOptimizationInfo();

        // Act & Assert
        assertTrue(info1.isKept(), "First instance should return true");
        assertTrue(info2.isKept(), "Second instance should return true");
        assertTrue(info3.isKept(), "Third instance should return true");
    }

    // =========================================================================
    // setCodeAttributeOptimizationInfo() Tests
    // =========================================================================

    /**
     * Tests that setCodeAttributeOptimizationInfo() correctly sets the optimization info
     * on a CodeAttribute.
     */
    @Test
    public void testSetCodeAttributeOptimizationInfo_setsProcessingInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        assertNull(codeAttribute.getProcessingInfo(), "Processing info should be null initially");

        // Act
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        Object processingInfo = codeAttribute.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should be set");
        assertInstanceOf(CodeAttributeOptimizationInfo.class, processingInfo,
                "Processing info should be an instance of CodeAttributeOptimizationInfo");
    }

    /**
     * Tests that setCodeAttributeOptimizationInfo() creates a new instance each time it's called.
     */
    @Test
    public void testSetCodeAttributeOptimizationInfo_createsNewInstance() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        Object firstInfo = codeAttribute.getProcessingInfo();

        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        Object secondInfo = codeAttribute.getProcessingInfo();

        // Assert
        assertNotNull(firstInfo, "First info should be set");
        assertNotNull(secondInfo, "Second info should be set");
        assertNotSame(firstInfo, secondInfo, "Each call should create a new instance");
    }

    /**
     * Tests that setCodeAttributeOptimizationInfo() can be called on multiple CodeAttributes.
     */
    @Test
    public void testSetCodeAttributeOptimizationInfo_worksForMultipleCodeAttributes() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        CodeAttribute codeAttribute3 = new CodeAttribute();

        // Act
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute1);
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute2);
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute3);

        // Assert
        assertNotNull(codeAttribute1.getProcessingInfo(), "First code attribute should have info");
        assertNotNull(codeAttribute2.getProcessingInfo(), "Second code attribute should have info");
        assertNotNull(codeAttribute3.getProcessingInfo(), "Third code attribute should have info");

        assertInstanceOf(CodeAttributeOptimizationInfo.class, codeAttribute1.getProcessingInfo());
        assertInstanceOf(CodeAttributeOptimizationInfo.class, codeAttribute2.getProcessingInfo());
        assertInstanceOf(CodeAttributeOptimizationInfo.class, codeAttribute3.getProcessingInfo());
    }

    /**
     * Tests that setCodeAttributeOptimizationInfo() replaces existing processing info.
     */
    @Test
    public void testSetCodeAttributeOptimizationInfo_replacesExistingInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        Object existingInfo = new Object();
        codeAttribute.setProcessingInfo(existingInfo);

        // Act
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        Object newInfo = codeAttribute.getProcessingInfo();
        assertNotNull(newInfo, "New info should be set");
        assertNotSame(existingInfo, newInfo, "Old info should be replaced");
        assertInstanceOf(CodeAttributeOptimizationInfo.class, newInfo,
                "New info should be CodeAttributeOptimizationInfo");
    }

    // =========================================================================
    // getCodeAttributeOptimizationInfo() Tests
    // =========================================================================

    /**
     * Tests that getCodeAttributeOptimizationInfo() returns null when no info is set.
     */
    @Test
    public void testGetCodeAttributeOptimizationInfo_returnsNull_whenNotSet() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        CodeAttributeOptimizationInfo result =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        assertNull(result, "Should return null when no optimization info is set");
    }

    /**
     * Tests that getCodeAttributeOptimizationInfo() returns the correct info after it's set.
     */
    @Test
    public void testGetCodeAttributeOptimizationInfo_returnsSetInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act
        CodeAttributeOptimizationInfo result =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        assertNotNull(result, "Should return the set optimization info");
        assertInstanceOf(CodeAttributeOptimizationInfo.class, result);
        assertTrue(result.isKept(), "Retrieved info should have isKept return true");
    }

    /**
     * Tests that getCodeAttributeOptimizationInfo() returns the same instance that was set.
     */
    @Test
    public void testGetCodeAttributeOptimizationInfo_returnsSameInstance() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        Object firstRetrieval = CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Act
        CodeAttributeOptimizationInfo secondRetrieval =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        assertNotNull(firstRetrieval, "First retrieval should not be null");
        assertNotNull(secondRetrieval, "Second retrieval should not be null");
        assertSame(firstRetrieval, secondRetrieval, "Multiple retrievals should return the same instance");
    }

    /**
     * Tests that getCodeAttributeOptimizationInfo() returns null when processing info
     * is set to a different type.
     */
    @Test
    public void testGetCodeAttributeOptimizationInfo_withDifferentProcessingInfo_throwsClassCastException() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.setProcessingInfo("Not a CodeAttributeOptimizationInfo");

        // Act & Assert
        assertThrows(ClassCastException.class, () -> {
            CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);
        }, "Should throw ClassCastException when processing info is of wrong type");
    }

    /**
     * Tests that getCodeAttributeOptimizationInfo() works correctly for multiple CodeAttributes.
     */
    @Test
    public void testGetCodeAttributeOptimizationInfo_worksForMultipleCodeAttributes() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        CodeAttribute codeAttribute3 = new CodeAttribute();

        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute1);
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute2);
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute3);

        // Act
        CodeAttributeOptimizationInfo info1 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute1);
        CodeAttributeOptimizationInfo info2 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute2);
        CodeAttributeOptimizationInfo info3 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute3);

        // Assert
        assertNotNull(info1, "First code attribute should return info");
        assertNotNull(info2, "Second code attribute should return info");
        assertNotNull(info3, "Third code attribute should return info");

        assertNotSame(info1, info2, "Each code attribute should have different info");
        assertNotSame(info2, info3, "Each code attribute should have different info");
        assertNotSame(info1, info3, "Each code attribute should have different info");
    }

    // =========================================================================
    // Integration Tests - Testing set/get together
    // =========================================================================

    /**
     * Tests the full workflow: set optimization info, retrieve it, and verify isKept().
     */
    @Test
    public void testIntegration_setGetAndIsKept() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        CodeAttributeOptimizationInfo info =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        assertNotNull(info, "Info should be retrievable after setting");
        assertTrue(info.isKept(), "Retrieved info should mark code as kept");
    }

    /**
     * Tests that setting new optimization info replaces old info correctly.
     */
    @Test
    public void testIntegration_setReplacesExistingInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        CodeAttributeOptimizationInfo firstInfo =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Act
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        CodeAttributeOptimizationInfo secondInfo =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        assertNotNull(firstInfo, "First info should be set");
        assertNotNull(secondInfo, "Second info should be set");
        assertNotSame(firstInfo, secondInfo, "Second set should create new instance");
        assertTrue(firstInfo.isKept(), "First info should have isKept return true");
        assertTrue(secondInfo.isKept(), "Second info should have isKept return true");
    }

    /**
     * Tests that each CodeAttribute can have its own independent optimization info.
     */
    @Test
    public void testIntegration_independentCodeAttributes() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();

        // Act
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute1);
        CodeAttributeOptimizationInfo info1 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute1);

        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute2);
        CodeAttributeOptimizationInfo info2 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute2);

        // Assert
        assertNotNull(info1, "First code attribute should have info");
        assertNotNull(info2, "Second code attribute should have info");
        assertNotSame(info1, info2, "Each code attribute should have independent info");
        assertTrue(info1.isKept(), "First info should be kept");
        assertTrue(info2.isKept(), "Second info should be kept");
    }
}
