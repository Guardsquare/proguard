package proguard.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger.ClassInfo} and {@link ConfigurationLogger.MemberInfo}.
 * Tests the constructor and toString() method of the ClassInfo and MemberInfo inner classes.
 */
public class ConfigurationLoggerClaudeTest {

    /**
     * Tests the ClassInfo constructor with valid parameters.
     * Verifies that all fields are correctly initialized.
     */
    @Test
    public void testClassInfoConstructorWithValidParameters() {
        // Arrange
        String originalClassName = "com.example.TestClass";
        String superClassName = "java.lang.Object";
        short flags = (short) (ConfigurationLogger.CLASS_KEPT | ConfigurationLogger.CLASS_SHRUNK);
        int[] fieldHashes = {12345, 67890};
        byte[] fieldFlags = {1, 2};
        int[] methodHashes = {111, 222, 333};
        byte[] methodFlags = {4, 5, 6};

        // Act
        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Assert
        assertEquals(originalClassName, classInfo.originalClassName);
        assertEquals(superClassName, classInfo.superClassName);
        assertEquals(flags, classInfo.flags);
        assertArrayEquals(fieldHashes, classInfo.fieldHashes);
        assertArrayEquals(fieldFlags, classInfo.fieldFlags);
        assertArrayEquals(methodHashes, classInfo.methodHashes);
        assertArrayEquals(methodFlags, classInfo.methodFlags);
    }

    /**
     * Tests the ClassInfo constructor with null class names.
     * The constructor should accept null values without throwing exceptions.
     */
    @Test
    public void testClassInfoConstructorWithNullClassNames() {
        // Arrange
        String originalClassName = null;
        String superClassName = null;
        short flags = 0;
        int[] fieldHashes = {};
        byte[] fieldFlags = {};
        int[] methodHashes = {};
        byte[] methodFlags = {};

        // Act
        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Assert
        assertNull(classInfo.originalClassName);
        assertNull(classInfo.superClassName);
    }

    /**
     * Tests the ClassInfo constructor with empty arrays.
     * Verifies that empty arrays are correctly stored.
     */
    @Test
    public void testClassInfoConstructorWithEmptyArrays() {
        // Arrange
        String originalClassName = "com.example.EmptyClass";
        String superClassName = "java.lang.Object";
        short flags = 0;
        int[] fieldHashes = {};
        byte[] fieldFlags = {};
        int[] methodHashes = {};
        byte[] methodFlags = {};

        // Act
        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Assert
        assertEquals(0, classInfo.fieldHashes.length);
        assertEquals(0, classInfo.methodHashes.length);
    }

    /**
     * Tests the ClassInfo constructor with null arrays.
     * The constructor should accept null arrays without throwing exceptions during construction.
     */
    @Test
    public void testClassInfoConstructorWithNullArrays() {
        // Arrange
        String originalClassName = "com.example.TestClass";
        String superClassName = "java.lang.Object";
        short flags = 0;
        int[] fieldHashes = null;
        byte[] fieldFlags = null;
        int[] methodHashes = null;
        byte[] methodFlags = null;

        // Act
        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Assert
        assertNull(classInfo.fieldHashes);
        assertNull(classInfo.methodHashes);
    }

    /**
     * Tests toString() with CLASS_KEPT flag set and no CLASS_SHRUNK.
     * The output should indicate the class is kept and not shrunk.
     */
    @Test
    public void testToStringWithClassKeptOnly() {
        // Arrange
        String originalClassName = "com.example.TestClass";
        String superClassName = "java.lang.Object";
        short flags = (short) ConfigurationLogger.CLASS_KEPT;
        int[] fieldHashes = {1, 2, 3};
        byte[] fieldFlags = {0, 0, 0};
        int[] methodHashes = {10, 20};
        byte[] methodFlags = {0, 0};

        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result = classInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.TestClass"));
        assertTrue(result.contains("extends java.lang.Object"));
        assertTrue(result.contains("kept"));
        assertTrue(result.contains("not shrunk"));
        assertTrue(result.contains("3 fields"));
        assertTrue(result.contains("2 methods"));
    }

    /**
     * Tests toString() with CLASS_SHRUNK flag set and no CLASS_KEPT.
     * The output should indicate the class is shrunk and not kept.
     */
    @Test
    public void testToStringWithClassShrunkOnly() {
        // Arrange
        String originalClassName = "com.example.ShrunkClass";
        String superClassName = "";
        short flags = (short) ConfigurationLogger.CLASS_SHRUNK;
        int[] fieldHashes = {};
        byte[] fieldFlags = {};
        int[] methodHashes = {100};
        byte[] methodFlags = {1};

        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result = classInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.ShrunkClass"));
        assertFalse(result.contains("extends")); // No superclass since superClassName is empty
        assertTrue(result.contains("not kept"));
        assertTrue(result.contains("shrunk"));
        assertTrue(result.contains("0 fields"));
        assertTrue(result.contains("1 methods"));
    }

    /**
     * Tests toString() with both CLASS_KEPT and CLASS_SHRUNK flags set.
     * The output should indicate both kept and shrunk.
     */
    @Test
    public void testToStringWithBothFlagsSet() {
        // Arrange
        String originalClassName = "com.example.BothFlags";
        String superClassName = "com.example.BaseClass";
        short flags = (short) (ConfigurationLogger.CLASS_KEPT | ConfigurationLogger.CLASS_SHRUNK);
        int[] fieldHashes = {1, 2};
        byte[] fieldFlags = {0, 1};
        int[] methodHashes = {10, 20, 30};
        byte[] methodFlags = {0, 1, 2};

        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result = classInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.BothFlags"));
        assertTrue(result.contains("extends com.example.BaseClass"));
        assertTrue(result.contains("kept"));
        assertTrue(result.contains("shrunk"));
        assertFalse(result.contains("not kept"));
        assertFalse(result.contains("not shrunk"));
        assertTrue(result.contains("2 fields"));
        assertTrue(result.contains("3 methods"));
    }

    /**
     * Tests toString() with no flags set.
     * The output should indicate not kept and not shrunk.
     */
    @Test
    public void testToStringWithNoFlagsSet() {
        // Arrange
        String originalClassName = "com.example.NoFlags";
        String superClassName = "";
        short flags = 0;
        int[] fieldHashes = {5};
        byte[] fieldFlags = {0};
        int[] methodHashes = {};
        byte[] methodFlags = {};

        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result = classInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.NoFlags"));
        assertFalse(result.contains("extends"));
        assertTrue(result.contains("not kept"));
        assertTrue(result.contains("not shrunk"));
        assertTrue(result.contains("1 fields"));
        assertTrue(result.contains("0 methods"));
    }

    /**
     * Tests toString() with an empty superClassName.
     * The output should not include "extends" when superClassName is empty.
     */
    @Test
    public void testToStringWithEmptySuperClassName() {
        // Arrange
        String originalClassName = "com.example.TopLevel";
        String superClassName = "";
        short flags = 0;
        int[] fieldHashes = {};
        byte[] fieldFlags = {};
        int[] methodHashes = {};
        byte[] methodFlags = {};

        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result = classInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.TopLevel"));
        assertFalse(result.contains("extends"));
    }

    /**
     * Tests toString() with a non-empty superClassName.
     * The output should include "extends" followed by the superclass name.
     */
    @Test
    public void testToStringWithNonEmptySuperClassName() {
        // Arrange
        String originalClassName = "com.example.DerivedClass";
        String superClassName = "com.example.BaseClass";
        short flags = 0;
        int[] fieldHashes = {123};
        byte[] fieldFlags = {1};
        int[] methodHashes = {456};
        byte[] methodFlags = {1};

        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result = classInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.DerivedClass"));
        assertTrue(result.contains("extends com.example.BaseClass"));
        assertTrue(result.contains("1 fields"));
        assertTrue(result.contains("1 methods"));
    }

    /**
     * Tests toString() with large numbers of fields and methods.
     * Verifies that the counts are correctly displayed.
     */
    @Test
    public void testToStringWithLargeNumbersOfFieldsAndMethods() {
        // Arrange
        String originalClassName = "com.example.LargeClass";
        String superClassName = "java.lang.Object";
        short flags = (short) ConfigurationLogger.CLASS_KEPT;
        int[] fieldHashes = new int[100];
        byte[] fieldFlags = new byte[100];
        int[] methodHashes = new int[50];
        byte[] methodFlags = new byte[50];

        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result = classInfo.toString();

        // Assert
        assertTrue(result.contains("100 fields"));
        assertTrue(result.contains("50 methods"));
    }

    /**
     * Tests that two ClassInfo objects with identical parameters produce identical toString output.
     */
    @Test
    public void testToStringConsistency() {
        // Arrange
        String originalClassName = "com.example.Consistent";
        String superClassName = "java.lang.Object";
        short flags = (short) ConfigurationLogger.CLASS_KEPT;
        int[] fieldHashes = {1, 2};
        byte[] fieldFlags = {0, 0};
        int[] methodHashes = {3, 4};
        byte[] methodFlags = {0, 0};

        ConfigurationLogger.ClassInfo classInfo1 = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        ConfigurationLogger.ClassInfo classInfo2 = new ConfigurationLogger.ClassInfo(
            originalClassName,
            superClassName,
            flags,
            fieldHashes,
            fieldFlags,
            methodHashes,
            methodFlags
        );

        // Act
        String result1 = classInfo1.toString();
        String result2 = classInfo2.toString();

        // Assert
        assertEquals(result1, result2, "Two ClassInfo objects with identical parameters should produce identical toString output");
    }

    // ========================================================================================
    // Tests for MemberInfo
    // ========================================================================================

    /**
     * Tests the MemberInfo constructor with valid parameters.
     * Verifies that all fields are correctly initialized.
     */
    @Test
    public void testMemberInfoConstructorWithValidParameters() {
        // Arrange
        String declaringClassName = "com.example.TestClass";
        byte flags = (byte) (ConfigurationLogger.MEMBER_KEPT | ConfigurationLogger.MEMBER_SHRUNK);

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals(flags, memberInfo.flags);
    }

    /**
     * Tests the MemberInfo constructor with null declaring class name.
     * The constructor should accept null values without throwing exceptions.
     */
    @Test
    public void testMemberInfoConstructorWithNullClassName() {
        // Arrange
        String declaringClassName = null;
        byte flags = 0;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertNull(memberInfo.declaringClassName);
        assertEquals(0, memberInfo.flags);
    }

    /**
     * Tests the MemberInfo constructor with empty declaring class name.
     * The constructor should accept empty strings without throwing exceptions.
     */
    @Test
    public void testMemberInfoConstructorWithEmptyClassName() {
        // Arrange
        String declaringClassName = "";
        byte flags = (byte) ConfigurationLogger.MEMBER_KEPT;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertEquals("", memberInfo.declaringClassName);
        assertEquals((byte) ConfigurationLogger.MEMBER_KEPT, memberInfo.flags);
    }

    /**
     * Tests the MemberInfo constructor with zero flags.
     * Verifies that zero flags (not kept, not shrunk) are correctly stored.
     */
    @Test
    public void testMemberInfoConstructorWithZeroFlags() {
        // Arrange
        String declaringClassName = "com.example.TestClass";
        byte flags = 0;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals(0, memberInfo.flags);
    }

    /**
     * Tests toString() with MEMBER_KEPT flag set and no MEMBER_SHRUNK.
     * The output should indicate the member is kept and not shrunk.
     */
    @Test
    public void testMemberInfoToStringWithMemberKeptOnly() {
        // Arrange
        String declaringClassName = "com.example.TestClass";
        byte flags = (byte) ConfigurationLogger.MEMBER_KEPT;

        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Act
        String result = memberInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.TestClass"));
        assertTrue(result.contains("kept"));
        assertTrue(result.contains("not shrunk"));
        assertFalse(result.contains("not kept"));
    }

    /**
     * Tests toString() with MEMBER_SHRUNK flag set and no MEMBER_KEPT.
     * The output should indicate the member is shrunk and not kept.
     */
    @Test
    public void testMemberInfoToStringWithMemberShrunkOnly() {
        // Arrange
        String declaringClassName = "com.example.ShrunkMember";
        byte flags = (byte) ConfigurationLogger.MEMBER_SHRUNK;

        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Act
        String result = memberInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.ShrunkMember"));
        assertTrue(result.contains("not kept"));
        assertTrue(result.contains("shrunk"));
        assertFalse(result.contains("not shrunk"));
    }

    /**
     * Tests toString() with both MEMBER_KEPT and MEMBER_SHRUNK flags set.
     * The output should indicate both kept and shrunk.
     */
    @Test
    public void testMemberInfoToStringWithBothFlagsSet() {
        // Arrange
        String declaringClassName = "com.example.BothFlags";
        byte flags = (byte) (ConfigurationLogger.MEMBER_KEPT | ConfigurationLogger.MEMBER_SHRUNK);

        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Act
        String result = memberInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.BothFlags"));
        assertTrue(result.contains("kept"));
        assertTrue(result.contains("shrunk"));
        assertFalse(result.contains("not kept"));
        assertFalse(result.contains("not shrunk"));
    }

    /**
     * Tests toString() with no flags set.
     * The output should indicate not kept and not shrunk.
     */
    @Test
    public void testMemberInfoToStringWithNoFlagsSet() {
        // Arrange
        String declaringClassName = "com.example.NoFlags";
        byte flags = 0;

        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Act
        String result = memberInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.NoFlags"));
        assertTrue(result.contains("not kept"));
        assertTrue(result.contains("not shrunk"));
    }

    /**
     * Tests toString() with a fully qualified class name.
     * Verifies that the full class name is included in the output.
     */
    @Test
    public void testMemberInfoToStringWithFullyQualifiedClassName() {
        // Arrange
        String declaringClassName = "com.example.package.subpackage.VeryLongClassName";
        byte flags = (byte) ConfigurationLogger.MEMBER_KEPT;

        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Act
        String result = memberInfo.toString();

        // Assert
        assertTrue(result.contains("com.example.package.subpackage.VeryLongClassName"));
        assertTrue(result.contains("kept"));
    }

    /**
     * Tests that two MemberInfo objects with identical parameters produce identical toString output.
     */
    @Test
    public void testMemberInfoToStringConsistency() {
        // Arrange
        String declaringClassName = "com.example.Consistent";
        byte flags = (byte) (ConfigurationLogger.MEMBER_KEPT | ConfigurationLogger.MEMBER_SHRUNK);

        ConfigurationLogger.MemberInfo memberInfo1 = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        ConfigurationLogger.MemberInfo memberInfo2 = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Act
        String result1 = memberInfo1.toString();
        String result2 = memberInfo2.toString();

        // Assert
        assertEquals(result1, result2, "Two MemberInfo objects with identical parameters should produce identical toString output");
    }

    /**
     * Tests toString() format to ensure parentheses and commas are properly formatted.
     * The expected format is: "className (kept/not kept, shrunk/not shrunk)"
     */
    @Test
    public void testMemberInfoToStringFormat() {
        // Arrange
        String declaringClassName = "com.example.TestClass";
        byte flags = (byte) ConfigurationLogger.MEMBER_KEPT;

        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Act
        String result = memberInfo.toString();

        // Assert
        // Check that the format matches: "className (status, status)"
        assertTrue(result.matches(".*\\(.*,.*\\)"), "toString should contain parentheses with comma-separated statuses");
        assertTrue(result.startsWith(declaringClassName), "toString should start with the declaring class name");
    }

    /**
     * Tests toString() with all possible flag combinations.
     * Verifies that each combination produces the correct output.
     */
    @Test
    public void testMemberInfoToStringAllFlagCombinations() {
        String className = "com.example.Test";

        // No flags
        ConfigurationLogger.MemberInfo memberInfo0 = new ConfigurationLogger.MemberInfo(className, (byte) 0);
        String result0 = memberInfo0.toString();
        assertTrue(result0.contains("not kept") && result0.contains("not shrunk"));

        // Only MEMBER_KEPT
        ConfigurationLogger.MemberInfo memberInfo1 = new ConfigurationLogger.MemberInfo(className, (byte) ConfigurationLogger.MEMBER_KEPT);
        String result1 = memberInfo1.toString();
        assertTrue(result1.contains("kept") && result1.contains("not shrunk"));
        assertFalse(result1.contains("not kept"));

        // Only MEMBER_SHRUNK
        ConfigurationLogger.MemberInfo memberInfo2 = new ConfigurationLogger.MemberInfo(className, (byte) ConfigurationLogger.MEMBER_SHRUNK);
        String result2 = memberInfo2.toString();
        assertTrue(result2.contains("not kept") && result2.contains("shrunk"));
        assertFalse(result2.contains("not shrunk"));

        // Both MEMBER_KEPT and MEMBER_SHRUNK
        ConfigurationLogger.MemberInfo memberInfo3 = new ConfigurationLogger.MemberInfo(className, (byte) (ConfigurationLogger.MEMBER_KEPT | ConfigurationLogger.MEMBER_SHRUNK));
        String result3 = memberInfo3.toString();
        assertTrue(result3.contains("kept") && result3.contains("shrunk"));
        assertFalse(result3.contains("not kept") || result3.contains("not shrunk"));
    }

    /**
     * Tests MemberInfo constructor and toString with maximum byte value.
     * Verifies that extreme flag values are handled correctly.
     */
    @Test
    public void testMemberInfoWithMaxByteValue() {
        // Arrange
        String declaringClassName = "com.example.MaxFlags";
        byte flags = Byte.MAX_VALUE;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertEquals(Byte.MAX_VALUE, memberInfo.flags);
        // With all bits set, both MEMBER_KEPT and MEMBER_SHRUNK will be set
        String result = memberInfo.toString();
        assertTrue(result.contains("kept") && result.contains("shrunk"));
    }

    /**
     * Tests MemberInfo constructor and toString with minimum byte value.
     * Verifies that negative flag values are handled correctly.
     */
    @Test
    public void testMemberInfoWithMinByteValue() {
        // Arrange
        String declaringClassName = "com.example.MinFlags";
        byte flags = Byte.MIN_VALUE;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertEquals(Byte.MIN_VALUE, memberInfo.flags);
        // Byte.MIN_VALUE is -128 (10000000 in binary), which doesn't have MEMBER_KEPT or MEMBER_SHRUNK bits set
        String result = memberInfo.toString();
        assertTrue(result.contains("not kept") && result.contains("not shrunk"));
    }
}
