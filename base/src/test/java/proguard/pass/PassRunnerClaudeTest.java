package proguard.pass;

import org.junit.jupiter.api.Test;
import proguard.AppView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PassRunner}.
 * Tests constructor and run() method to ensure proper functionality.
 */
public class PassRunnerClaudeTest {

    /**
     * Tests that the constructor creates a valid PassRunner instance.
     * The constructor is parameterless and should initialize internal state.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act
        PassRunner passRunner = new PassRunner();

        // Assert
        assertNotNull(passRunner, "Constructor should create a non-null instance");
    }

    /**
     * Tests that multiple PassRunner instances can be created independently.
     */
    @Test
    public void testMultipleInstancesCanBeCreated() {
        // Act
        PassRunner passRunner1 = new PassRunner();
        PassRunner passRunner2 = new PassRunner();

        // Assert
        assertNotNull(passRunner1, "First instance should not be null");
        assertNotNull(passRunner2, "Second instance should not be null");
        assertNotSame(passRunner1, passRunner2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that run() executes the pass's execute() method.
     * This verifies the basic functionality of the run method.
     */
    @Test
    public void testRunExecutesPass() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        AtomicBoolean passExecuted = new AtomicBoolean(false);
        Pass pass = new TestPass(() -> passExecuted.set(true));
        AppView appView = new AppView();

        // Act
        passRunner.run(pass, appView);

        // Assert
        assertTrue(passExecuted.get(), "Pass execute() method should be called");
    }

    /**
     * Tests that run() passes the correct AppView to the Pass.
     */
    @Test
    public void testRunPassesCorrectAppView() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        AppView expectedAppView = new AppView();
        AtomicBoolean correctAppView = new AtomicBoolean(false);

        Pass pass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                if (appView == expectedAppView) {
                    correctAppView.set(true);
                }
            }
        };

        // Act
        passRunner.run(pass, expectedAppView);

        // Assert
        assertTrue(correctAppView.get(), "The exact AppView instance should be passed to the Pass");
    }

    /**
     * Tests that run() completes successfully with a simple pass.
     */
    @Test
    public void testRunCompletesWithSimplePass() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        Pass pass = new TestPass(() -> {});
        AppView appView = new AppView();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> passRunner.run(pass, appView),
                          "run() should complete without throwing exceptions for simple pass");
    }

    /**
     * Tests that run() propagates exceptions from the pass.
     */
    @Test
    public void testRunPropagatesExceptions() {
        // Arrange
        PassRunner passRunner = new PassRunner();
        String errorMessage = "Test exception from pass";
        Pass pass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                throw new RuntimeException(errorMessage);
            }
        };
        AppView appView = new AppView();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> passRunner.run(pass, appView),
                                                  "run() should propagate exceptions from the pass");
        assertEquals(errorMessage, exception.getMessage(),
                    "Exception message should be preserved");
    }

    /**
     * Tests that run() can handle passes that modify the AppView.
     */
    @Test
    public void testRunWithPassThatModifiesAppView() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        AppView appView = new AppView();

        Pass pass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                // Simulate modifying the AppView by accessing its fields
                assertNotNull(appView.programClassPool);
                assertNotNull(appView.libraryClassPool);
            }
        };

        // Act & Assert
        assertDoesNotThrow(() -> passRunner.run(pass, appView),
                          "run() should handle passes that access AppView fields");
    }

    /**
     * Tests that run() can be called multiple times on the same PassRunner instance.
     */
    @Test
    public void testRunCanBeCalledMultipleTimes() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        AtomicInteger executionCount = new AtomicInteger(0);
        Pass pass = new TestPass(() -> executionCount.incrementAndGet());
        AppView appView = new AppView();

        // Act
        passRunner.run(pass, appView);
        passRunner.run(pass, appView);
        passRunner.run(pass, appView);

        // Assert
        assertEquals(3, executionCount.get(),
                    "run() should execute the pass each time it is called");
    }

    /**
     * Tests that run() can be called with different passes sequentially.
     */
    @Test
    public void testRunWithDifferentPassesSequentially() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        AtomicBoolean pass1Executed = new AtomicBoolean(false);
        AtomicBoolean pass2Executed = new AtomicBoolean(false);

        Pass pass1 = new TestPass(() -> pass1Executed.set(true));
        Pass pass2 = new TestPass(() -> pass2Executed.set(true));
        AppView appView = new AppView();

        // Act
        passRunner.run(pass1, appView);
        passRunner.run(pass2, appView);

        // Assert
        assertTrue(pass1Executed.get(), "First pass should be executed");
        assertTrue(pass2Executed.get(), "Second pass should be executed");
    }

    /**
     * Tests that run() works correctly with passes that have custom names.
     */
    @Test
    public void testRunWithPassWithCustomName() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        String customName = "CustomTestPass";
        AtomicBoolean executed = new AtomicBoolean(false);

        Pass pass = new Pass() {
            @Override
            public String getName() {
                return customName;
            }

            @Override
            public void execute(AppView appView) throws Exception {
                executed.set(true);
            }
        };
        AppView appView = new AppView();

        // Act
        passRunner.run(pass, appView);

        // Assert
        assertTrue(executed.get(), "Pass with custom name should be executed");
        assertEquals(customName, pass.getName(), "Pass should maintain its custom name");
    }

    /**
     * Tests that run() handles passes with different execution times.
     * This implicitly tests the benchmarking functionality.
     */
    @Test
    public void testRunWithPassesOfDifferentExecutionTimes() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();

        Pass fastPass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                // Execute immediately
            }
        };

        Pass slowerPass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                // Small delay to ensure different execution time
                Thread.sleep(10);
            }
        };

        AppView appView = new AppView();

        // Act & Assert - both should complete successfully
        assertDoesNotThrow(() -> passRunner.run(fastPass, appView),
                          "run() should handle fast passes");
        assertDoesNotThrow(() -> passRunner.run(slowerPass, appView),
                          "run() should handle passes with delays");
    }

    /**
     * Tests that run() handles checked exceptions from the pass.
     */
    @Test
    public void testRunPropagatesCheckedException() {
        // Arrange
        PassRunner passRunner = new PassRunner();
        Pass pass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                throw new Exception("Checked exception from pass");
            }
        };
        AppView appView = new AppView();

        // Act & Assert
        assertThrows(Exception.class,
                    () -> passRunner.run(pass, appView),
                    "run() should propagate checked exceptions");
    }

    /**
     * Tests that run() can handle a pass that performs no operations.
     */
    @Test
    public void testRunWithNoOpPass() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        Pass noOpPass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                // No operation
            }
        };
        AppView appView = new AppView();

        // Act & Assert
        assertDoesNotThrow(() -> passRunner.run(noOpPass, appView),
                          "run() should handle no-op passes");
    }

    /**
     * Tests that PassRunner instance can execute multiple passes with different AppViews.
     */
    @Test
    public void testRunWithDifferentAppViews() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        AtomicInteger executionCount = new AtomicInteger(0);
        Pass pass = new TestPass(() -> executionCount.incrementAndGet());

        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act
        passRunner.run(pass, appView1);
        passRunner.run(pass, appView2);

        // Assert
        assertEquals(2, executionCount.get(),
                    "Pass should be executed with each AppView");
    }

    /**
     * Tests that run() correctly handles passes using default getName() implementation.
     */
    @Test
    public void testRunWithDefaultGetName() throws Exception {
        // Arrange
        PassRunner passRunner = new PassRunner();
        AtomicBoolean executed = new AtomicBoolean(false);
        Pass pass = new TestPass(() -> executed.set(true));
        AppView appView = new AppView();

        // Act
        passRunner.run(pass, appView);

        // Assert
        assertTrue(executed.get(), "Pass with default getName() should execute");
        assertNotNull(pass.getName(), "Pass should have a non-null name");
    }

    // Helper classes for testing

    /**
     * Simple test implementation of Pass with configurable behavior.
     */
    private static class TestPass implements Pass {
        private final Runnable action;

        public TestPass(Runnable action) {
            this.action = action;
        }

        @Override
        public void execute(AppView appView) throws Exception {
            action.run();
        }
    }
}
