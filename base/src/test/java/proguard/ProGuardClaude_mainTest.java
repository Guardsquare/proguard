package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.security.Permission;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ProGuard#main(String[])}.
 * Tests the static main method entry point.
 *
 * Note: Testing main() is complex because it calls System.exit().
 * We use a SecurityManager to catch System.exit() calls without actually terminating the JVM.
 */
public class ProGuardClaude_mainTest {

    private SecurityManager originalSecurityManager;
    private PrintStream originalErr;
    private ByteArrayOutputStream errContent;

    /**
     * Custom SecurityManager to prevent System.exit() from terminating the JVM.
     * Instead, it throws a SecurityException that we can catch in tests.
     */
    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            // Allow everything
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
            // Allow everything
        }

        @Override
        public void checkExit(int status) {
            // Prevent System.exit() by throwing an exception
            throw new ExitException(status);
        }
    }

    /**
     * Exception thrown when System.exit() is called.
     * Captures the exit status code.
     */
    private static class ExitException extends SecurityException {
        public final int status;

        public ExitException(int status) {
            super("System.exit(" + status + ") called");
            this.status = status;
        }
    }

    @BeforeEach
    public void setUp() {
        // Save original security manager
        originalSecurityManager = System.getSecurityManager();

        // Install our custom security manager to catch System.exit()
        System.setSecurityManager(new NoExitSecurityManager());

        // Capture System.err output
        originalErr = System.err;
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void tearDown() {
        // Restore original security manager
        System.setSecurityManager(originalSecurityManager);

        // Restore System.err
        System.setErr(originalErr);
    }

    /**
     * Tests main() with no arguments.
     * Should print usage message and exit with status 1.
     */
    @Test
    public void testMainWithNoArguments() {
        // Arrange
        String[] args = new String[0];

        // Act & Assert
        ExitException exitException = assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should call System.exit() when no arguments provided");

        // Verify exit status
        assertEquals(1, exitException.status, "Should exit with status 1 for no arguments");
    }

    /**
     * Tests main() with null arguments array.
     * Should throw NullPointerException or exit with error.
     */
    @Test
    public void testMainWithNullArguments() {
        // Arrange
        String[] args = null;

        // Act & Assert - Either NPE or ExitException is acceptable
        assertThrows(Exception.class, () -> {
            ProGuard.main(args);
        }, "main() should throw exception when arguments are null");
    }

    /**
     * Tests main() with a single empty string argument.
     * Should attempt to parse the configuration and likely fail.
     */
    @Test
    public void testMainWithEmptyStringArgument() {
        // Arrange
        String[] args = new String[]{""};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should eventually exit when given empty string argument");
    }

    /**
     * Tests main() with an invalid configuration option.
     * Should fail to parse configuration and exit with status 1.
     */
    @Test
    public void testMainWithInvalidOption() {
        // Arrange
        String[] args = new String[]{"-invalidoption12345"};

        // Act & Assert
        ExitException exitException = assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should exit when given invalid option");

        assertEquals(1, exitException.status, "Should exit with status 1 for invalid option");
    }

    /**
     * Tests main() with multiple invalid arguments.
     * Should fail during parsing and exit with error status.
     */
    @Test
    public void testMainWithMultipleInvalidArguments() {
        // Arrange
        String[] args = new String[]{"invalid1", "invalid2", "invalid3"};

        // Act & Assert
        ExitException exitException = assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should exit when given multiple invalid arguments");

        assertEquals(1, exitException.status, "Should exit with status 1 for invalid arguments");
    }

    /**
     * Tests main() with a non-existent configuration file.
     * Should fail to read the file and exit with status 1.
     */
    @Test
    public void testMainWithNonExistentConfigFile() {
        // Arrange
        String[] args = new String[]{"@/nonexistent/path/to/config.pro"};

        // Act & Assert
        ExitException exitException = assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should exit when configuration file doesn't exist");

        assertEquals(1, exitException.status, "Should exit with status 1 for non-existent file");
    }

    /**
     * Tests main() with a valid help option (if supported).
     * The behavior depends on whether ProGuard recognizes help options.
     */
    @Test
    public void testMainWithHelpOption() {
        // Arrange
        String[] args = new String[]{"-help"};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should exit when help option provided");
    }

    /**
     * Tests main() with whitespace-only arguments.
     * Should attempt to parse and likely fail.
     */
    @Test
    public void testMainWithWhitespaceArguments() {
        // Arrange
        String[] args = new String[]{"   ", "\t", "\n"};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should exit when given whitespace-only arguments");
    }

    /**
     * Tests main() with very long argument.
     * Should handle long arguments without crashing.
     */
    @Test
    public void testMainWithVeryLongArgument() {
        // Arrange
        StringBuilder longArg = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longArg.append("x");
        }
        String[] args = new String[]{longArg.toString()};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should handle very long arguments");
    }

    /**
     * Tests main() with special characters in arguments.
     * Should handle special characters appropriately.
     */
    @Test
    public void testMainWithSpecialCharacters() {
        // Arrange
        String[] args = new String[]{"!@#$%^&*()", "<>?:\"{}|"};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should handle special characters in arguments");
    }

    /**
     * Tests main() with a mix of valid-looking but incorrect options.
     * Should fail during configuration parsing or execution.
     */
    @Test
    public void testMainWithMixedInvalidOptions() {
        // Arrange
        String[] args = new String[]{"-injars", "nonexistent.jar", "-outjars", "output.jar"};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should exit when configuration execution fails");
    }

    /**
     * Tests that main() creates a Configuration object internally.
     * This is indirectly tested by verifying the method processes arguments.
     */
    @Test
    public void testMainCreatesConfiguration() {
        // Arrange
        String[] args = new String[]{"-invalidtest"};

        // Act & Assert
        // The fact that it tries to parse means it created a Configuration
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should create Configuration and attempt to parse");
    }

    /**
     * Tests main() with array containing null element.
     * Should handle null elements gracefully or throw exception.
     */
    @Test
    public void testMainWithNullElementInArray() {
        // Arrange
        String[] args = new String[]{null};

        // Act & Assert
        assertThrows(Exception.class, () -> {
            ProGuard.main(args);
        }, "main() should handle null element in arguments array");
    }

    /**
     * Tests main() with multiple arguments including nulls.
     * Should handle mixed null and non-null arguments.
     */
    @Test
    public void testMainWithMixedNullArguments() {
        // Arrange
        String[] args = new String[]{"valid", null, "another"};

        // Act & Assert
        assertThrows(Exception.class, () -> {
            ProGuard.main(args);
        }, "main() should handle mixed null and non-null arguments");
    }

    /**
     * Tests main() behavior consistency with repeated calls.
     * Each call should behave independently.
     */
    @Test
    public void testMainMultipleCalls() {
        // Arrange
        String[] args = new String[0];

        // Act & Assert - Call multiple times
        for (int i = 0; i < 3; i++) {
            ExitException exitException = assertThrows(ExitException.class, () -> {
                ProGuard.main(args);
            }, "main() should exit on call " + i);

            assertEquals(1, exitException.status,
                    "Should exit with status 1 on call " + i);
        }
    }

    /**
     * Tests main() doesn't modify the input arguments array.
     * The original array should remain unchanged.
     */
    @Test
    public void testMainDoesNotModifyArguments() {
        // Arrange
        String[] args = new String[]{"-test", "value"};
        String[] originalArgs = args.clone();

        // Act
        try {
            ProGuard.main(args);
        } catch (ExitException e) {
            // Expected
        }

        // Assert - Arguments should not be modified
        assertArrayEquals(originalArgs, args,
                "main() should not modify the input arguments array");
    }

    /**
     * Tests main() with a large number of arguments.
     * Should handle many arguments without issues.
     */
    @Test
    public void testMainWithManyArguments() {
        // Arrange
        String[] args = new String[100];
        for (int i = 0; i < 100; i++) {
            args[i] = "-option" + i;
        }

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should handle large number of arguments");
    }

    /**
     * Tests main() with Unicode characters in arguments.
     * Should handle Unicode appropriately.
     */
    @Test
    public void testMainWithUnicodeArguments() {
        // Arrange
        String[] args = new String[]{"测试", "テスト", "🎉"};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should handle Unicode characters in arguments");
    }

    /**
     * Tests main() with argument that looks like a path.
     * Should attempt to process the path-like argument.
     */
    @Test
    public void testMainWithPathLikeArgument() {
        // Arrange
        String[] args = new String[]{"/some/path/to/file.pro"};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should handle path-like arguments");
    }

    /**
     * Tests main() with Windows-style path argument.
     * Should handle Windows paths appropriately.
     */
    @Test
    public void testMainWithWindowsPathArgument() {
        // Arrange
        String[] args = new String[]{"C:\\some\\path\\config.pro"};

        // Act & Assert
        assertThrows(ExitException.class, () -> {
            ProGuard.main(args);
        }, "main() should handle Windows-style paths");
    }
}
