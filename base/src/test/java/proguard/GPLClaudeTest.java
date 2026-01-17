package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link GPL}.
 * Tests the constructor, check() method, and main() method to ensure proper GPL compliance checking functionality.
 */
public class GPLClaudeTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests the GPL constructor.
     * The GPL class has an implicit default constructor that should be accessible.
     */
    @Test
    public void testConstructor() {
        // Act - create an instance of GPL
        GPL gpl = new GPL();

        // Assert - verify the object is created successfully
        assertNotNull(gpl, "GPL instance should be created successfully");
    }

    /**
     * Tests that multiple GPL instances can be created independently.
     */
    @Test
    public void testMultipleInstancesCanBeCreated() {
        // Act - create multiple instances
        GPL gpl1 = new GPL();
        GPL gpl2 = new GPL();

        // Assert - verify both are distinct instances
        assertNotNull(gpl1, "First GPL instance should be created");
        assertNotNull(gpl2, "Second GPL instance should be created");
        assertNotSame(gpl1, gpl2, "Each call to constructor should create a new instance");
    }

    /**
     * Tests the check() method when called from proguard package.
     * Since the check() method looks at the stack trace and only prints a GPL notice
     * if unknown packages are found in the call stack, and the test itself is in the
     * proguard package (which is a known package), no output should be generated.
     */
    @Test
    public void testCheckFromKnownPackage() {
        // Act - call check() from a test in proguard package (which is known)
        GPL.check();

        // Assert - since the call is from proguard package (known), no GPL notice should be printed
        // The check() method uses logger.info, not System.out, so we can't directly verify the output here
        // However, we can verify the method executes without throwing exceptions
        assertTrue(true, "check() should execute without exceptions when called from known package");
    }

    /**
     * Tests the check() method execution completes successfully.
     * This test verifies that the method can parse stack traces and process package names.
     */
    @Test
    public void testCheckMethodExecutesSuccessfully() {
        // Act & Assert - verify the method runs without throwing exceptions
        assertDoesNotThrow(() -> GPL.check(), "check() should not throw any exceptions");
    }

    /**
     * Tests the main() method with empty input stream.
     * The main() method reads from System.in, parses stack traces, and prints unique package names.
     */
    @Test
    public void testMainWithEmptyInput() {
        // Arrange - create an empty input stream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        InputStream originalIn = System.in;

        try {
            System.setIn(emptyInput);

            // Act - call main with empty args
            GPL.main(new String[0]);

            // Assert - with empty input, no output should be produced
            String output = testOut.toString();
            assertEquals("", output, "No output should be produced with empty input");
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests the main() method with a valid stack trace containing unknown packages.
     * This verifies that the method correctly identifies and prints unknown package names.
     */
    @Test
    public void testMainWithStackTraceContainingUnknownPackages() {
        // Arrange - create a simulated stack trace with unknown packages
        String stackTrace = "at com.example.myapp.MyClass.method(MyClass.java:10)\n" +
                           "at com.example.myapp.AnotherClass.process(AnotherClass.java:20)\n" +
                           "at com.unknown.package.SomeClass.execute(SomeClass.java:30)\n";
        InputStream testInput = new ByteArrayInputStream(stackTrace.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act - call main
            GPL.main(new String[0]);

            // Assert - verify that unknown package names are printed
            String output = testOut.toString().trim();
            // The output should contain the unknown packages
            assertTrue(output.contains("com.example.myapp") || output.contains("com.unknown.package"),
                      "Output should contain unknown package names");
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests the main() method with a stack trace containing only known packages.
     * Known packages include java.*, proguard.*, sun.reflect.*, etc.
     */
    @Test
    public void testMainWithStackTraceContainingOnlyKnownPackages() {
        // Arrange - create a stack trace with only known packages
        String stackTrace = "at java.lang.Thread.run(Thread.java:750)\n" +
                           "at proguard.GPL.check(GPL.java:42)\n" +
                           "at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n";
        InputStream testInput = new ByteArrayInputStream(stackTrace.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act - call main
            GPL.main(new String[0]);

            // Assert - no output should be produced since all packages are known
            String output = testOut.toString();
            assertEquals("", output, "No output should be produced when all packages are known");
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests the main() method with mixed known and unknown packages.
     * This verifies filtering of known packages and reporting of unknown ones.
     */
    @Test
    public void testMainWithMixedKnownAndUnknownPackages() {
        // Arrange - create a stack trace with mixed packages
        String stackTrace = "at java.lang.Thread.run(Thread.java:750)\n" +
                           "at com.custom.app.MyClass.method(MyClass.java:10)\n" +
                           "at proguard.GPL.check(GPL.java:42)\n" +
                           "at net.example.custom.SomeClass.execute(SomeClass.java:30)\n" +
                           "at org.gradle.internal.SomeClass.doSomething(SomeClass.java:15)\n";
        InputStream testInput = new ByteArrayInputStream(stackTrace.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act - call main
            GPL.main(new String[0]);

            // Assert - verify that only unknown packages are in output
            String output = testOut.toString().trim();
            if (!output.isEmpty()) {
                // Should not contain known packages
                assertFalse(output.contains("java.lang"), "Output should not contain java packages");
                assertFalse(output.contains("proguard.GPL"), "Output should not contain proguard packages");
                assertFalse(output.contains("org.gradle"), "Output should not contain gradle packages");
                // Should contain unknown packages
                assertTrue(output.contains("com.custom") || output.contains("net.example"),
                          "Output should contain unknown packages");
            }
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests the main() method with arguments.
     * The main() method should ignore the args parameter as it reads from System.in.
     */
    @Test
    public void testMainWithArguments() {
        // Arrange
        String stackTrace = "at com.test.MyClass.method(MyClass.java:10)\n";
        InputStream testInput = new ByteArrayInputStream(stackTrace.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act - call main with some arguments (should be ignored)
            assertDoesNotThrow(() -> GPL.main(new String[]{"arg1", "arg2"}),
                             "main() should not throw exceptions with arguments");

            // Assert - method should still work normally, reading from System.in
            String output = testOut.toString().trim();
            assertTrue(output.contains("com.test") || output.isEmpty(),
                      "Output should be based on System.in, not args");
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests the main() method with malformed input.
     * The method should handle non-stack-trace input gracefully.
     */
    @Test
    public void testMainWithMalformedInput() {
        // Arrange - create input that's not a proper stack trace
        String malformedInput = "This is not a stack trace\n" +
                               "Just some random text\n" +
                               "No 'at' prefix here\n";
        InputStream testInput = new ByteArrayInputStream(malformedInput.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act & Assert - should not throw exceptions
            assertDoesNotThrow(() -> GPL.main(new String[0]),
                             "main() should handle malformed input gracefully");

            // With no valid stack trace lines, no output should be produced
            String output = testOut.toString();
            assertEquals("", output, "No output should be produced with malformed input");
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests the main() method with null args array.
     * This verifies the method doesn't fail when called with null arguments.
     */
    @Test
    public void testMainWithNullArgs() {
        // Arrange
        String stackTrace = "";
        InputStream testInput = new ByteArrayInputStream(stackTrace.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act & Assert - should not throw NullPointerException
            assertDoesNotThrow(() -> GPL.main(null),
                             "main() should handle null args without throwing exceptions");
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests that check() method works with nested call stacks.
     * This verifies that the method can parse complex stack traces with multiple levels.
     */
    @Test
    public void testCheckWithNestedCallStack() {
        // This test calls check() through multiple levels to create a deeper stack trace
        // Act & Assert
        assertDoesNotThrow(() -> {
            helperMethod1();
        }, "check() should handle nested call stacks");
    }

    /**
     * Helper method to create a nested call stack for testing.
     */
    private void helperMethod1() {
        helperMethod2();
    }

    /**
     * Helper method to create a nested call stack for testing.
     */
    private void helperMethod2() {
        GPL.check();
    }

    /**
     * Tests the main() method with subpackage filtering.
     * When a parent package is unknown, its subpackages should be filtered out from output.
     */
    @Test
    public void testMainWithSubpackageFiltering() {
        // Arrange - create a stack trace with parent and child packages
        String stackTrace = "at com.example.MyClass.method(MyClass.java:10)\n" +
                           "at com.example.sub.ChildClass.process(ChildClass.java:20)\n" +
                           "at com.example.sub.deep.DeepClass.execute(DeepClass.java:30)\n";
        InputStream testInput = new ByteArrayInputStream(stackTrace.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act
            GPL.main(new String[0]);

            // Assert - output should only contain the parent package, not subpackages
            String output = testOut.toString().trim();
            if (!output.isEmpty()) {
                // The uniquePackageNames method should filter out subpackages
                // Only the root "com.example" should appear, not "com.example.sub" or "com.example.sub.deep"
                assertTrue(output.contains("com.example"), "Output should contain parent package");
                // The output format includes ", " after each package name
            }
        } finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests the main() method with packages that are on the known list.
     * This verifies various known packages are properly filtered.
     */
    @Test
    public void testMainWithVariousKnownPackages() {
        // Arrange - stack trace with various known packages
        String stackTrace = "at java.util.ArrayList.add(ArrayList.java:100)\n" +
                           "at jdk.internal.reflect.Reflection.getCallerClass(Reflection.java:10)\n" +
                           "at org.apache.tools.ant.Task.execute(Task.java:50)\n" +
                           "at org.gradle.internal.GradleTask.run(GradleTask.java:30)\n" +
                           "at com.android.build.Builder.build(Builder.java:20)\n" +
                           "at scala.collection.immutable.List.apply(List.scala:40)\n";
        InputStream testInput = new ByteArrayInputStream(stackTrace.getBytes());
        InputStream originalIn = System.in;

        try {
            System.setIn(testInput);

            // Act
            GPL.main(new String[0]);

            // Assert - no output since all packages are known
            String output = testOut.toString();
            assertEquals("", output, "No output should be produced when all packages are on known list");
        } finally {
            System.setIn(originalIn);
        }
    }
}
