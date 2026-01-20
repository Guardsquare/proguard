package proguard.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Logging.
 * Tests the constructor and configureVerbosity method with comprehensive coverage.
 */
public class LoggingClaudeTest {

    /**
     * Test that the Logging class can be instantiated.
     * The class has an implicit default constructor that should work.
     */
    @Test
    public void testConstructor() {
        // Act
        Logging logging = new Logging();

        // Assert
        assertNotNull(logging, "Logging instance should be created");
    }

    /**
     * Test configureVerbosity with verbose=true.
     * When verbose is true, the log level should be set to INFO.
     */
    @Test
    public void testConfigureVerbosityTrue() {
        // Act
        Logging.configureVerbosity(true);

        // Assert
        LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getRootLogger();

        assertEquals(Level.INFO, loggerConfig.getLevel(),
            "When verbose is true, log level should be INFO");
    }

    /**
     * Test configureVerbosity with verbose=false.
     * When verbose is false, the log level should be set to WARN.
     */
    @Test
    public void testConfigureVerbosityFalse() {
        // Act
        Logging.configureVerbosity(false);

        // Assert
        LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getRootLogger();

        assertEquals(Level.WARN, loggerConfig.getLevel(),
            "When verbose is false, log level should be WARN");
    }

    /**
     * Test toggling configureVerbosity from false to true.
     * Tests that the method correctly updates the logging level when called multiple times.
     */
    @Test
    public void testConfigureVerbosityToggleFalseToTrue() {
        // Arrange - Set to false first
        Logging.configureVerbosity(false);

        LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getRootLogger();
        assertEquals(Level.WARN, loggerConfig.getLevel(),
            "Initial state should be WARN");

        // Act - Toggle to true
        Logging.configureVerbosity(true);

        // Assert
        assertEquals(Level.INFO, loggerConfig.getLevel(),
            "After toggling to true, log level should be INFO");
    }

    /**
     * Test toggling configureVerbosity from true to false.
     * Tests that the method correctly updates the logging level when called multiple times.
     */
    @Test
    public void testConfigureVerbosityToggleTrueToFalse() {
        // Arrange - Set to true first
        Logging.configureVerbosity(true);

        LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getRootLogger();
        assertEquals(Level.INFO, loggerConfig.getLevel(),
            "Initial state should be INFO");

        // Act - Toggle to false
        Logging.configureVerbosity(false);

        // Assert
        assertEquals(Level.WARN, loggerConfig.getLevel(),
            "After toggling to false, log level should be WARN");
    }

    /**
     * Test calling configureVerbosity with same value twice.
     * Tests that repeated calls with the same parameter are idempotent.
     */
    @Test
    public void testConfigureVerbosityIdempotentTrue() {
        // Act
        Logging.configureVerbosity(true);
        Logging.configureVerbosity(true);

        // Assert
        LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getRootLogger();

        assertEquals(Level.INFO, loggerConfig.getLevel(),
            "Repeated calls with true should maintain INFO level");
    }

    /**
     * Test calling configureVerbosity with same value twice.
     * Tests that repeated calls with the same parameter are idempotent.
     */
    @Test
    public void testConfigureVerbosityIdempotentFalse() {
        // Act
        Logging.configureVerbosity(false);
        Logging.configureVerbosity(false);

        // Assert
        LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getRootLogger();

        assertEquals(Level.WARN, loggerConfig.getLevel(),
            "Repeated calls with false should maintain WARN level");
    }

    /**
     * Test that configureVerbosity properly updates the logger context.
     * Verifies that the ctx.updateLoggers() method has its intended effect
     * by checking that the configuration is properly applied.
     */
    @Test
    public void testConfigureVerbosityUpdatesContext() {
        // Arrange
        LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getRootLogger();

        // Set to one level
        Logging.configureVerbosity(false);
        Level initialLevel = loggerConfig.getLevel();

        // Act - Change to another level
        Logging.configureVerbosity(true);

        // Assert - The level should have changed
        Level newLevel = loggerConfig.getLevel();
        assertNotEquals(initialLevel, newLevel,
            "Logger level should change after calling configureVerbosity with different value");
        assertEquals(Level.INFO, newLevel,
            "New level should be INFO");
    }
}
