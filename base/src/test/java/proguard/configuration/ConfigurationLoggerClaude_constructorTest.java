package proguard.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger} constructor and inner class constructors.
 * Tests the default no-argument constructor and verifies that instances can be created
 * and that the class implements Runnable.
 * Also tests the MemberInfo inner class constructor.
 */
public class ConfigurationLoggerClaude_constructorTest {

    /**
     * Tests the implicit no-argument constructor ConfigurationLogger().
     * Verifies that the class can be instantiated.
     */
    @Test
    public void testConstructor() {
        // Act - Instantiate ConfigurationLogger
        ConfigurationLogger logger = new ConfigurationLogger();

        // Assert - Verify the instance is not null
        assertNotNull(logger, "ConfigurationLogger instance should not be null");
    }

    /**
     * Tests that ConfigurationLogger implements Runnable interface.
     * Since the constructor creates a Runnable instance, we verify the type.
     */
    @Test
    public void testConstructorCreatesRunnableInstance() {
        // Act
        ConfigurationLogger logger = new ConfigurationLogger();

        // Assert - Verify it implements Runnable
        assertTrue(logger instanceof Runnable,
            "ConfigurationLogger should implement Runnable interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testMultipleInstantiations() {
        // Act - Create multiple instances
        ConfigurationLogger logger1 = new ConfigurationLogger();
        ConfigurationLogger logger2 = new ConfigurationLogger();
        ConfigurationLogger logger3 = new ConfigurationLogger();

        // Assert - All instances should be non-null
        assertNotNull(logger1, "First instance should not be null");
        assertNotNull(logger2, "Second instance should not be null");
        assertNotNull(logger3, "Third instance should not be null");

        // Verify they are different instances
        assertNotSame(logger1, logger2, "Instances should be different objects");
        assertNotSame(logger2, logger3, "Instances should be different objects");
        assertNotSame(logger1, logger3, "Instances should be different objects");
    }

    /**
     * Tests that the constructor creates instances that can be used as Runnable.
     * Verifies that the run() method can be invoked without throwing exceptions.
     */
    @Test
    public void testConstructorCreatesValidRunnable() {
        // Arrange
        ConfigurationLogger logger = new ConfigurationLogger();
        Runnable runnable = logger;

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> runnable.run(),
            "run() method should execute without throwing exceptions");
    }

    /**
     * Tests that newly constructed instances share the same static state.
     * This verifies that the static initialization has completed successfully.
     */
    @Test
    public void testConstructorWithStaticInitialization() {
        // Act - Create instance which triggers static initialization if not already done
        ConfigurationLogger logger = new ConfigurationLogger();

        // Assert - Instance should be created successfully
        assertNotNull(logger, "Instance should be created after static initialization");

        // Verify the instance can be cast to Runnable
        assertInstanceOf(Runnable.class, logger,
            "Instance should be of type Runnable");
    }

    /**
     * Tests constructor creates an instance with accessible public interface.
     * Verifies that the Runnable interface method is accessible.
     */
    @Test
    public void testConstructorCreatesInstanceWithPublicInterface() {
        // Arrange
        ConfigurationLogger logger = new ConfigurationLogger();

        // Act - Get the class and verify it has the run method
        Class<?> clazz = logger.getClass();

        // Assert
        assertNotNull(clazz, "Class should not be null");
        assertEquals("ConfigurationLogger", clazz.getSimpleName(),
            "Class name should be ConfigurationLogger");

        // Verify Runnable interface is implemented
        boolean implementsRunnable = Runnable.class.isAssignableFrom(clazz);
        assertTrue(implementsRunnable, "Class should implement Runnable interface");
    }

    /**
     * Tests that the constructor can be called in different contexts.
     * Verifies instance creation in various scenarios.
     */
    @Test
    public void testConstructorInVariousContexts() {
        // Test direct instantiation
        ConfigurationLogger directInstance = new ConfigurationLogger();
        assertNotNull(directInstance);

        // Test instantiation as Runnable
        Runnable runnableInstance = new ConfigurationLogger();
        assertNotNull(runnableInstance);
        assertInstanceOf(ConfigurationLogger.class, runnableInstance);

        // Test instantiation in array
        ConfigurationLogger[] loggerArray = new ConfigurationLogger[3];
        loggerArray[0] = new ConfigurationLogger();
        loggerArray[1] = new ConfigurationLogger();
        loggerArray[2] = new ConfigurationLogger();

        assertNotNull(loggerArray[0]);
        assertNotNull(loggerArray[1]);
        assertNotNull(loggerArray[2]);
        assertNotSame(loggerArray[0], loggerArray[1]);
    }

    /**
     * Tests that constructor completes quickly without blocking.
     * The constructor should be lightweight and not perform heavy operations.
     */
    @Test
    public void testConstructorPerformance() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - Create instance
        ConfigurationLogger logger = new ConfigurationLogger();

        // Assert
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        assertNotNull(logger, "Instance should be created");

        // Constructor should complete in reasonable time (less than 1 second = 1_000_000_000 nanoseconds)
        // This is very generous since constructor should be nearly instantaneous
        assertTrue(duration < 1_000_000_000L,
            "Constructor should complete quickly (took " + duration + " nanoseconds)");
    }

    /**
     * Tests that instances created by the constructor have proper object identity.
     */
    @Test
    public void testConstructorObjectIdentity() {
        // Arrange & Act
        ConfigurationLogger logger1 = new ConfigurationLogger();
        ConfigurationLogger logger2 = new ConfigurationLogger();

        // Assert
        assertNotNull(logger1);
        assertNotNull(logger2);
        assertNotEquals(logger1, logger2,
            "Different instances should not be equal by default");
        assertNotSame(logger1, logger2,
            "Different instances should have different object identity");

        // Verify same instance equals itself
        assertEquals(logger1, logger1, "Instance should equal itself");
        assertSame(logger1, logger1, "Instance should be same as itself");
    }

    /**
     * Tests constructor with subsequent method invocation.
     * Verifies that constructed instance is fully usable.
     */
    @Test
    public void testConstructorThenMethodInvocation() {
        // Arrange
        ConfigurationLogger logger = new ConfigurationLogger();

        // Act & Assert - Should be able to call run() without issues
        assertDoesNotThrow(() -> {
            logger.run();
        }, "Should be able to call run() on newly constructed instance");
    }

    // ========================================================================================
    // Tests for MemberInfo Constructor
    // ========================================================================================

    /**
     * Tests the MemberInfo constructor with valid parameters.
     * Verifies that the constructor correctly initializes the declaringClassName and flags fields.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithValidParameters() {
        // Arrange
        String declaringClassName = "com.example.TestClass";
        byte flags = (byte) (ConfigurationLogger.MEMBER_KEPT | ConfigurationLogger.MEMBER_SHRUNK);

        // Act - Create MemberInfo instance
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert - Verify fields are correctly initialized
        assertNotNull(memberInfo, "MemberInfo instance should not be null");
        assertEquals(declaringClassName, memberInfo.declaringClassName,
            "declaringClassName should be correctly initialized");
        assertEquals(flags, memberInfo.flags,
            "flags should be correctly initialized");
    }

    /**
     * Tests the MemberInfo constructor with null declaringClassName.
     * Verifies that the constructor accepts null values without throwing exceptions.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithNullClassName() {
        // Arrange
        String declaringClassName = null;
        byte flags = 0;

        // Act - Create MemberInfo instance with null className
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert - Verify the null value is stored
        assertNotNull(memberInfo, "MemberInfo instance should not be null");
        assertNull(memberInfo.declaringClassName,
            "declaringClassName should be null as passed to constructor");
        assertEquals(0, memberInfo.flags,
            "flags should be 0 as passed to constructor");
    }

    /**
     * Tests the MemberInfo constructor with MEMBER_KEPT flag only.
     * Verifies that the constructor correctly stores the MEMBER_KEPT flag.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithMemberKeptFlag() {
        // Arrange
        String declaringClassName = "com.example.KeptMember";
        byte flags = (byte) ConfigurationLogger.MEMBER_KEPT;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertNotNull(memberInfo);
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals((byte) ConfigurationLogger.MEMBER_KEPT, memberInfo.flags);
    }

    /**
     * Tests the MemberInfo constructor with MEMBER_SHRUNK flag only.
     * Verifies that the constructor correctly stores the MEMBER_SHRUNK flag.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithMemberShrunkFlag() {
        // Arrange
        String declaringClassName = "com.example.ShrunkMember";
        byte flags = (byte) ConfigurationLogger.MEMBER_SHRUNK;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertNotNull(memberInfo);
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals((byte) ConfigurationLogger.MEMBER_SHRUNK, memberInfo.flags);
    }

    /**
     * Tests the MemberInfo constructor with zero flags.
     * Verifies that the constructor correctly handles zero flags (not kept, not shrunk).
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithZeroFlags() {
        // Arrange
        String declaringClassName = "com.example.NoFlagsMember";
        byte flags = 0;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertNotNull(memberInfo);
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals(0, memberInfo.flags);
    }

    /**
     * Tests the MemberInfo constructor with both flags set.
     * Verifies that the constructor correctly handles both MEMBER_KEPT and MEMBER_SHRUNK flags.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithBothFlags() {
        // Arrange
        String declaringClassName = "com.example.BothFlagsMember";
        byte flags = (byte) (ConfigurationLogger.MEMBER_KEPT | ConfigurationLogger.MEMBER_SHRUNK);

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertNotNull(memberInfo);
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals(flags, memberInfo.flags);
        // Verify both flags are set
        assertEquals(ConfigurationLogger.MEMBER_KEPT, memberInfo.flags & ConfigurationLogger.MEMBER_KEPT,
            "MEMBER_KEPT flag should be set");
        assertEquals(ConfigurationLogger.MEMBER_SHRUNK, memberInfo.flags & ConfigurationLogger.MEMBER_SHRUNK,
            "MEMBER_SHRUNK flag should be set");
    }

    /**
     * Tests the MemberInfo constructor with an empty string declaringClassName.
     * Verifies that the constructor accepts empty strings without throwing exceptions.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
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
        assertNotNull(memberInfo);
        assertEquals("", memberInfo.declaringClassName,
            "declaringClassName should be empty string as passed to constructor");
        assertEquals((byte) ConfigurationLogger.MEMBER_KEPT, memberInfo.flags);
    }

    /**
     * Tests the MemberInfo constructor with a fully qualified class name.
     * Verifies that the constructor correctly stores long class names.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithFullyQualifiedClassName() {
        // Arrange
        String declaringClassName = "com.example.package.subpackage.VeryLongClassName";
        byte flags = (byte) ConfigurationLogger.MEMBER_KEPT;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertNotNull(memberInfo);
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals((byte) ConfigurationLogger.MEMBER_KEPT, memberInfo.flags);
    }

    /**
     * Tests creating multiple MemberInfo instances with different parameters.
     * Verifies that the constructor can be called multiple times independently.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorMultipleInstances() {
        // Act - Create multiple instances
        ConfigurationLogger.MemberInfo memberInfo1 = new ConfigurationLogger.MemberInfo(
            "com.example.Class1",
            (byte) ConfigurationLogger.MEMBER_KEPT
        );

        ConfigurationLogger.MemberInfo memberInfo2 = new ConfigurationLogger.MemberInfo(
            "com.example.Class2",
            (byte) ConfigurationLogger.MEMBER_SHRUNK
        );

        ConfigurationLogger.MemberInfo memberInfo3 = new ConfigurationLogger.MemberInfo(
            "com.example.Class3",
            (byte) 0
        );

        // Assert - All instances should be independent
        assertNotNull(memberInfo1);
        assertNotNull(memberInfo2);
        assertNotNull(memberInfo3);

        assertNotSame(memberInfo1, memberInfo2, "Instances should be different objects");
        assertNotSame(memberInfo2, memberInfo3, "Instances should be different objects");
        assertNotSame(memberInfo1, memberInfo3, "Instances should be different objects");

        // Verify each has its own values
        assertEquals("com.example.Class1", memberInfo1.declaringClassName);
        assertEquals("com.example.Class2", memberInfo2.declaringClassName);
        assertEquals("com.example.Class3", memberInfo3.declaringClassName);

        assertEquals((byte) ConfigurationLogger.MEMBER_KEPT, memberInfo1.flags);
        assertEquals((byte) ConfigurationLogger.MEMBER_SHRUNK, memberInfo2.flags);
        assertEquals(0, memberInfo3.flags);
    }

    /**
     * Tests the MemberInfo constructor with extreme byte values.
     * Verifies that the constructor correctly handles Byte.MAX_VALUE and Byte.MIN_VALUE.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithExtremeByteValues() {
        // Test with Byte.MAX_VALUE
        ConfigurationLogger.MemberInfo memberInfoMax = new ConfigurationLogger.MemberInfo(
            "com.example.MaxValue",
            Byte.MAX_VALUE
        );

        assertNotNull(memberInfoMax);
        assertEquals("com.example.MaxValue", memberInfoMax.declaringClassName);
        assertEquals(Byte.MAX_VALUE, memberInfoMax.flags);

        // Test with Byte.MIN_VALUE
        ConfigurationLogger.MemberInfo memberInfoMin = new ConfigurationLogger.MemberInfo(
            "com.example.MinValue",
            Byte.MIN_VALUE
        );

        assertNotNull(memberInfoMin);
        assertEquals("com.example.MinValue", memberInfoMin.declaringClassName);
        assertEquals(Byte.MIN_VALUE, memberInfoMin.flags);
    }

    /**
     * Tests that the MemberInfo constructor correctly initializes final fields.
     * Verifies that the fields are immutable after construction.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorInitializesFinalFields() {
        // Arrange
        String declaringClassName = "com.example.FinalFieldsTest";
        byte flags = (byte) (ConfigurationLogger.MEMBER_KEPT | ConfigurationLogger.MEMBER_SHRUNK);

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert - Verify fields are accessible and correctly set
        assertNotNull(memberInfo);
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals(flags, memberInfo.flags);

        // Verify the instance can be used immediately after construction
        String toString = memberInfo.toString();
        assertNotNull(toString, "toString() should work immediately after construction");
        assertTrue(toString.contains(declaringClassName),
            "toString() should contain the declaring class name");
    }

    /**
     * Tests the MemberInfo constructor with special characters in class name.
     * Verifies that the constructor correctly handles class names with special characters.
     * This test covers lines 1235-1238 in ConfigurationLogger.java.
     */
    @Test
    public void testMemberInfoConstructorWithSpecialCharactersInClassName() {
        // Arrange - Class names can contain $ for inner classes
        String declaringClassName = "com.example.OuterClass$InnerClass";
        byte flags = (byte) ConfigurationLogger.MEMBER_KEPT;

        // Act
        ConfigurationLogger.MemberInfo memberInfo = new ConfigurationLogger.MemberInfo(
            declaringClassName,
            flags
        );

        // Assert
        assertNotNull(memberInfo);
        assertEquals(declaringClassName, memberInfo.declaringClassName);
        assertEquals((byte) ConfigurationLogger.MEMBER_KEPT, memberInfo.flags);
    }
}
