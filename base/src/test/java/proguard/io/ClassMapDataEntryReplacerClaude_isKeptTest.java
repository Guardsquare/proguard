package proguard.io;

import org.junit.jupiter.api.Test;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassMapDataEntryReplacer.isKept method.
 * Tests the logic that determines if a class or member is "kept" based on processing flags.
 */
public class ClassMapDataEntryReplacerClaude_isKeptTest {

    /**
     * Test that isKept returns true when both DONT_OBFUSCATE and DONT_SHRINK flags are set.
     */
    @Test
    public void testIsKeptReturnsTrueWhenBothFlagsSet() {
        // Arrange
        int processingFlags = ProcessingFlags.DONT_OBFUSCATE | ProcessingFlags.DONT_SHRINK;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertTrue(result, "isKept should return true when both DONT_OBFUSCATE and DONT_SHRINK are set");
    }

    /**
     * Test that isKept returns false when only DONT_OBFUSCATE flag is set.
     */
    @Test
    public void testIsKeptReturnsFalseWhenOnlyDontObfuscateSet() {
        // Arrange
        int processingFlags = ProcessingFlags.DONT_OBFUSCATE;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertFalse(result, "isKept should return false when only DONT_OBFUSCATE is set");
    }

    /**
     * Test that isKept returns false when only DONT_SHRINK flag is set.
     */
    @Test
    public void testIsKeptReturnsFalseWhenOnlyDontShrinkSet() {
        // Arrange
        int processingFlags = ProcessingFlags.DONT_SHRINK;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertFalse(result, "isKept should return false when only DONT_SHRINK is set");
    }

    /**
     * Test that isKept returns false when no flags are set.
     */
    @Test
    public void testIsKeptReturnsFalseWhenNoFlagsSet() {
        // Arrange
        int processingFlags = 0;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertFalse(result, "isKept should return false when no flags are set");
    }

    /**
     * Test that isKept returns true when both required flags are set along with other flags.
     */
    @Test
    public void testIsKeptReturnsTrueWhenBothFlagsSetWithOtherFlags() {
        // Arrange
        int processingFlags = ProcessingFlags.DONT_OBFUSCATE |
                             ProcessingFlags.DONT_SHRINK |
                             ProcessingFlags.DONT_OPTIMIZE;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertTrue(result, "isKept should return true when both required flags are set even with other flags");
    }

    /**
     * Test that isKept returns false when DONT_SHRINK is set with other flags but not DONT_OBFUSCATE.
     */
    @Test
    public void testIsKeptReturnsFalseWhenDontShrinkSetWithOtherFlags() {
        // Arrange
        int processingFlags = ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OPTIMIZE;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertFalse(result, "isKept should return false when DONT_OBFUSCATE is not set");
    }

    /**
     * Test that isKept returns false when DONT_OBFUSCATE is set with other flags but not DONT_SHRINK.
     */
    @Test
    public void testIsKeptReturnsFalseWhenDontObfuscateSetWithOtherFlags() {
        // Arrange
        int processingFlags = ProcessingFlags.DONT_OBFUSCATE | ProcessingFlags.DONT_OPTIMIZE;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertFalse(result, "isKept should return false when DONT_SHRINK is not set");
    }

    /**
     * Test that isKept returns true when both flags are set using bitwise OR with zero.
     */
    @Test
    public void testIsKeptWithBitwiseOrZero() {
        // Arrange
        int processingFlags = 0 | ProcessingFlags.DONT_OBFUSCATE | ProcessingFlags.DONT_SHRINK;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertTrue(result, "isKept should return true when both flags are set via bitwise OR with zero");
    }

    /**
     * Test that isKept returns false when given a negative value with neither required flag.
     */
    @Test
    public void testIsKeptWithNegativeValueNoRequiredFlags() {
        // Arrange
        // Create a negative value that doesn't have the required bits set
        int processingFlags = -1 & ~ProcessingFlags.DONT_OBFUSCATE;

        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertFalse(result, "isKept should return false when DONT_OBFUSCATE bit is not set");
    }

    /**
     * Test that isKept returns true when both required flags are set in a complex flag combination.
     */
    @Test
    public void testIsKeptWithComplexFlagCombination() {
        // Arrange
        // Set all possible flags to ensure the method only checks the required ones
        int processingFlags = -1; // All bits set

        // Since all bits are set, both DONT_OBFUSCATE and DONT_SHRINK must be set
        // Act
        boolean result = ClassMapDataEntryReplacer.isKept(processingFlags);

        // Assert
        assertTrue(result, "isKept should return true when both required flags are set in a full flag combination");
    }
}
