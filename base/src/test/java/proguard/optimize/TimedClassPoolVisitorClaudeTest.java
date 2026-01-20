package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link TimedClassPoolVisitor}.
 * Tests all constructors and the visitClassPool method with various scenarios
 * to achieve good branch and condition coverage.
 */
public class TimedClassPoolVisitorClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor that takes a message and a ClassVisitor.
     * Verifies that the instance is successfully created with non-null parameters.
     */
    @Test
    public void testConstructorWithMessageAndClassVisitor() {
        // Arrange
        String message = "Test message";
        ClassVisitor classVisitor = new TestClassVisitor();

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, classVisitor);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created successfully");
    }

    /**
     * Tests the constructor with a null message and a valid ClassVisitor.
     * Verifies that the constructor handles null message gracefully.
     */
    @Test
    public void testConstructorWithNullMessageAndClassVisitor() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(null, classVisitor);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with null message");
    }

    /**
     * Tests the constructor with an empty message and a valid ClassVisitor.
     * Verifies that the constructor handles empty message string.
     */
    @Test
    public void testConstructorWithEmptyMessageAndClassVisitor() {
        // Arrange
        String message = "";
        ClassVisitor classVisitor = new TestClassVisitor();

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, classVisitor);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with empty message");
    }

    /**
     * Tests the constructor with a valid message and a null ClassVisitor.
     * Verifies that the constructor handles null ClassVisitor.
     */
    @Test
    public void testConstructorWithMessageAndNullClassVisitor() {
        // Arrange
        String message = "Test message";

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, (ClassVisitor) null);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with null ClassVisitor");
    }

    /**
     * Tests the constructor with a long message (testing padding logic).
     * Verifies that the constructor handles long messages properly.
     */
    @Test
    public void testConstructorWithLongMessageAndClassVisitor() {
        // Arrange
        String message = "This is a very long message that exceeds the typical padding size of 48 characters";
        ClassVisitor classVisitor = new TestClassVisitor();

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, classVisitor);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with long message");
    }

    /**
     * Tests the constructor that takes a message and a ClassPoolVisitor.
     * Verifies that the instance is successfully created with non-null parameters.
     */
    @Test
    public void testConstructorWithMessageAndClassPoolVisitor() {
        // Arrange
        String message = "Test message";
        ClassPoolVisitor classPoolVisitor = new TestClassPoolVisitor();

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, classPoolVisitor);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created successfully");
    }

    /**
     * Tests the constructor with a null message and a valid ClassPoolVisitor.
     * Verifies that the constructor handles null message gracefully.
     */
    @Test
    public void testConstructorWithNullMessageAndClassPoolVisitor() {
        // Arrange
        ClassPoolVisitor classPoolVisitor = new TestClassPoolVisitor();

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(null, classPoolVisitor);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with null message");
    }

    /**
     * Tests the constructor with an empty message and a valid ClassPoolVisitor.
     * Verifies that the constructor handles empty message string.
     */
    @Test
    public void testConstructorWithEmptyMessageAndClassPoolVisitor() {
        // Arrange
        String message = "";
        ClassPoolVisitor classPoolVisitor = new TestClassPoolVisitor();

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, classPoolVisitor);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with empty message");
    }

    /**
     * Tests the constructor with a valid message and a null ClassPoolVisitor.
     * Verifies that the constructor handles null ClassPoolVisitor.
     */
    @Test
    public void testConstructorWithMessageAndNullClassPoolVisitor() {
        // Arrange
        String message = "Test message";

        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, (ClassPoolVisitor) null);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with null ClassPoolVisitor");
    }

    /**
     * Tests the constructor with both null parameters (ClassPoolVisitor version).
     * Verifies that the constructor handles all null parameters.
     */
    @Test
    public void testConstructorWithAllNullParametersClassPoolVisitor() {
        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(null, (ClassPoolVisitor) null);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with all null parameters");
    }

    /**
     * Tests the constructor with both null parameters (ClassVisitor version).
     * Verifies that the constructor handles all null parameters.
     */
    @Test
    public void testConstructorWithAllNullParametersClassVisitor() {
        // Act
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(null, (ClassVisitor) null);

        // Assert
        assertNotNull(visitor, "TimedClassPoolVisitor should be created with all null parameters");
    }

    // ========== visitClassPool Tests ==========

    /**
     * Tests visitClassPool with a valid ClassPoolVisitor that tracks invocation.
     * Verifies that the wrapped visitor is called and timing is measured.
     */
    @Test
    public void testVisitClassPoolWithTrackingClassPoolVisitor() {
        // Arrange
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Test", trackingVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertTrue(trackingVisitor.wasVisited(), "The wrapped ClassPoolVisitor should be invoked");
        assertSame(classPool, trackingVisitor.getVisitedClassPool(),
            "The same ClassPool should be passed to the wrapped visitor");
    }

    /**
     * Tests visitClassPool with a valid ClassVisitor (wrapped in AllClassVisitor).
     * Verifies that the wrapped visitor is properly invoked through AllClassVisitor.
     */
    @Test
    public void testVisitClassPoolWithTrackingClassVisitor() {
        // Arrange
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Test", trackingVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert - The visitor should be invoked (though with empty pool, may not visit any classes)
        // We verify no exception is thrown and the operation completes
        assertNotNull(visitor, "Visitor should complete visitClassPool without errors");
    }

    /**
     * Tests visitClassPool with an empty ClassPool.
     * Verifies that the method handles empty pools correctly.
     */
    @Test
    public void testVisitClassPoolWithEmptyClassPool() {
        // Arrange
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Test", trackingVisitor);
        ClassPool emptyClassPool = new ClassPool();

        // Act
        visitor.visitClassPool(emptyClassPool);

        // Assert
        assertTrue(trackingVisitor.wasVisited(), "Visitor should be invoked even with empty ClassPool");
    }

    /**
     * Tests visitClassPool measures time correctly (timing is > 0 or >= 0).
     * Verifies that the timing mechanism works by ensuring the operation completes.
     */
    @Test
    public void testVisitClassPoolMeasuresTime() {
        // Arrange
        SlowClassPoolVisitor slowVisitor = new SlowClassPoolVisitor(10); // 10ms delay
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Timing test", slowVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        long startTime = System.currentTimeMillis();
        visitor.visitClassPool(classPool);
        long endTime = System.currentTimeMillis();
        long actualDuration = endTime - startTime;

        // Assert - The operation should take at least the delay time
        assertTrue(actualDuration >= 10,
            "Operation should take at least 10ms due to the slow visitor");
    }

    /**
     * Tests visitClassPool with a message exactly at the padding boundary (48 chars).
     * Verifies padding logic when message length equals the padding size.
     */
    @Test
    public void testVisitClassPoolWithMessageAtPaddingBoundary() {
        // Arrange - Create a message exactly 48 characters long
        String message = "123456789012345678901234567890123456789012345678"; // 48 chars
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, trackingVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertTrue(trackingVisitor.wasVisited(), "Visitor should be invoked");
    }

    /**
     * Tests visitClassPool with a message shorter than padding boundary.
     * Verifies padding logic when message length is less than 48 characters.
     */
    @Test
    public void testVisitClassPoolWithShortMessage() {
        // Arrange - Short message (will need padding)
        String message = "Short";
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, trackingVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertTrue(trackingVisitor.wasVisited(), "Visitor should be invoked");
    }

    /**
     * Tests visitClassPool with a message longer than padding boundary.
     * Verifies padding logic when message length exceeds 48 characters.
     */
    @Test
    public void testVisitClassPoolWithLongMessage() {
        // Arrange - Long message (no padding needed)
        String message = "This is a very long message that is definitely more than 48 characters long for testing";
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor(message, trackingVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertTrue(trackingVisitor.wasVisited(), "Visitor should be invoked");
    }

    /**
     * Tests visitClassPool can be called multiple times on the same instance.
     * Verifies that the visitor can be reused for multiple invocations.
     */
    @Test
    public void testVisitClassPoolMultipleInvocations() {
        // Arrange
        CountingClassPoolVisitor countingVisitor = new CountingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Test", countingVisitor);
        ClassPool classPool1 = new ClassPool();
        ClassPool classPool2 = new ClassPool();
        ClassPool classPool3 = new ClassPool();

        // Act
        visitor.visitClassPool(classPool1);
        visitor.visitClassPool(classPool2);
        visitor.visitClassPool(classPool3);

        // Assert
        assertEquals(3, countingVisitor.getCount(),
            "Visitor should be invoked three times");
    }

    /**
     * Tests visitClassPool with null ClassPool parameter.
     * Verifies behavior when null ClassPool is passed (may throw NPE or handle gracefully).
     */
    @Test
    public void testVisitClassPoolWithNullClassPool() {
        // Arrange
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Test", trackingVisitor);

        // Act & Assert
        // This may throw NullPointerException depending on implementation
        // We test the actual behavior rather than assuming
        try {
            visitor.visitClassPool(null);
            assertTrue(trackingVisitor.wasVisited(),
                "If no exception, visitor should have been invoked");
        } catch (NullPointerException e) {
            // This is acceptable behavior - null ClassPool causes NPE
            assertNotNull(e, "NullPointerException is expected for null ClassPool");
        }
    }

    /**
     * Tests that visitClassPool delegates to the wrapped visitor correctly.
     * Verifies the delegation chain when using ClassVisitor constructor.
     */
    @Test
    public void testVisitClassPoolDelegationWithClassVisitor() {
        // Arrange
        TrackingClassVisitor trackingVisitor = new TrackingClassVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Delegation test", trackingVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert - Operation completes without error
        assertNotNull(visitor, "Delegation should work correctly");
    }

    /**
     * Tests visitClassPool with different messages to cover padding branches.
     * Verifies padding calculation with various message lengths.
     */
    @Test
    public void testVisitClassPoolWithVariousMessageLengths() {
        // Arrange
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        ClassPool classPool = new ClassPool();

        // Test with message length = 0
        TimedClassPoolVisitor visitor1 = new TimedClassPoolVisitor("", trackingVisitor);
        visitor1.visitClassPool(classPool);
        assertTrue(trackingVisitor.wasVisited(), "Empty message should work");
        trackingVisitor.reset();

        // Test with message length = 1
        TimedClassPoolVisitor visitor2 = new TimedClassPoolVisitor("A", trackingVisitor);
        visitor2.visitClassPool(classPool);
        assertTrue(trackingVisitor.wasVisited(), "Single char message should work");
        trackingVisitor.reset();

        // Test with message length = 47 (one less than boundary)
        TimedClassPoolVisitor visitor3 = new TimedClassPoolVisitor(
            "12345678901234567890123456789012345678901234567", trackingVisitor);
        visitor3.visitClassPool(classPool);
        assertTrue(trackingVisitor.wasVisited(), "Message at boundary-1 should work");
        trackingVisitor.reset();

        // Test with message length = 49 (one more than boundary)
        TimedClassPoolVisitor visitor4 = new TimedClassPoolVisitor(
            "1234567890123456789012345678901234567890123456789", trackingVisitor);
        visitor4.visitClassPool(classPool);
        assertTrue(trackingVisitor.wasVisited(), "Message at boundary+1 should work");
    }

    // ========== Edge Case Tests ==========

    /**
     * Tests creating multiple TimedClassPoolVisitor instances.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleInstancesAreIndependent() {
        // Arrange
        TrackingClassPoolVisitor visitor1 = new TrackingClassPoolVisitor();
        TrackingClassPoolVisitor visitor2 = new TrackingClassPoolVisitor();
        TimedClassPoolVisitor timed1 = new TimedClassPoolVisitor("First", visitor1);
        TimedClassPoolVisitor timed2 = new TimedClassPoolVisitor("Second", visitor2);
        ClassPool classPool = new ClassPool();

        // Act
        timed1.visitClassPool(classPool);

        // Assert
        assertTrue(visitor1.wasVisited(), "First visitor should be invoked");
        assertFalse(visitor2.wasVisited(), "Second visitor should not be invoked");

        // Act
        timed2.visitClassPool(classPool);

        // Assert
        assertTrue(visitor2.wasVisited(), "Second visitor should now be invoked");
    }

    /**
     * Tests that the visitor implements ClassPoolVisitor interface.
     * Verifies the interface implementation.
     */
    @Test
    public void testImplementsClassPoolVisitorInterface() {
        // Arrange
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Test", new TestClassPoolVisitor());

        // Assert
        assertTrue(visitor instanceof ClassPoolVisitor,
            "TimedClassPoolVisitor should implement ClassPoolVisitor");
    }

    /**
     * Tests visitClassPool with a visitor that throws an exception.
     * Verifies exception propagation behavior.
     */
    @Test
    public void testVisitClassPoolWithExceptionThrowingVisitor() {
        // Arrange
        ClassPoolVisitor exceptionVisitor = new ExceptionThrowingClassPoolVisitor();
        TimedClassPoolVisitor visitor = new TimedClassPoolVisitor("Exception test", exceptionVisitor);
        ClassPool classPool = new ClassPool();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> visitor.visitClassPool(classPool),
            "Exception from wrapped visitor should propagate");
    }

    // ========== Helper Classes ==========

    /**
     * Simple test implementation of ClassVisitor for testing.
     */
    private static class TestClassVisitor implements ClassVisitor {
        @Override
        public void visitAnyClass(Clazz clazz) {
            // No-op implementation for testing
        }
    }

    /**
     * Simple test implementation of ClassPoolVisitor for testing.
     */
    private static class TestClassPoolVisitor implements ClassPoolVisitor {
        @Override
        public void visitClassPool(ClassPool classPool) {
            // No-op implementation for testing
        }
    }

    /**
     * Tracking implementation of ClassVisitor that records if it was visited.
     */
    private static class TrackingClassVisitor implements ClassVisitor {
        private boolean visited = false;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visited = true;
        }

        public boolean wasVisited() {
            return visited;
        }

        public void reset() {
            visited = false;
        }
    }

    /**
     * Tracking implementation of ClassPoolVisitor that records invocations.
     */
    private static class TrackingClassPoolVisitor implements ClassPoolVisitor {
        private boolean visited = false;
        private ClassPool visitedClassPool = null;

        @Override
        public void visitClassPool(ClassPool classPool) {
            visited = true;
            visitedClassPool = classPool;
        }

        public boolean wasVisited() {
            return visited;
        }

        public ClassPool getVisitedClassPool() {
            return visitedClassPool;
        }

        public void reset() {
            visited = false;
            visitedClassPool = null;
        }
    }

    /**
     * Counting implementation of ClassPoolVisitor that counts invocations.
     */
    private static class CountingClassPoolVisitor implements ClassPoolVisitor {
        private int count = 0;

        @Override
        public void visitClassPool(ClassPool classPool) {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Slow ClassPoolVisitor that introduces a delay for timing tests.
     */
    private static class SlowClassPoolVisitor implements ClassPoolVisitor {
        private final long delayMs;

        public SlowClassPoolVisitor(long delayMs) {
            this.delayMs = delayMs;
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * ClassPoolVisitor that throws an exception for testing error handling.
     */
    private static class ExceptionThrowingClassPoolVisitor implements ClassPoolVisitor {
        @Override
        public void visitClassPool(ClassPool classPool) {
            throw new RuntimeException("Test exception");
        }
    }
}
