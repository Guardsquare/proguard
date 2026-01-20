package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.visitor.ClassPoolVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link RepeatedClassPoolVisitor}.
 *
 * This class tests the RepeatedClassPoolVisitor which repeatedly delegates to a given
 * class pool visitor as long as it keeps setting a given flag. Tests cover:
 * - Constructor initialization
 * - visitClassPool with various repeat trigger conditions
 * - Proper iteration behavior based on the MutableBoolean flag
 */
public class RepeatedClassPoolVisitorClaudeTest {

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the constructor successfully creates a RepeatedClassPoolVisitor instance
     * with valid parameters.
     */
    @Test
    public void testConstructor_createsInstanceWithValidParameters() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        ClassPoolVisitor classPoolVisitor = new TestClassPoolVisitor();

        // Act
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, classPoolVisitor);

        // Assert
        assertNotNull(visitor, "RepeatedClassPoolVisitor should be created successfully");
    }

    /**
     * Tests constructor with null MutableBoolean parameter.
     * The implementation does not validate this, so it should create the instance.
     */
    @Test
    public void testConstructor_withNullRepeatTrigger() {
        // Arrange
        ClassPoolVisitor classPoolVisitor = new TestClassPoolVisitor();

        // Act
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(null, classPoolVisitor);

        // Assert
        assertNotNull(visitor, "RepeatedClassPoolVisitor should be created with null repeatTrigger");
    }

    /**
     * Tests constructor with null ClassPoolVisitor parameter.
     * The implementation does not validate this, so it should create the instance.
     */
    @Test
    public void testConstructor_withNullClassPoolVisitor() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, null);

        // Assert
        assertNotNull(visitor, "RepeatedClassPoolVisitor should be created with null classPoolVisitor");
    }

    /**
     * Tests constructor with both null parameters.
     */
    @Test
    public void testConstructor_withBothNullParameters() {
        // Act
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(null, null);

        // Assert
        assertNotNull(visitor, "RepeatedClassPoolVisitor should be created with all null parameters");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleIndependentInstances() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();
        ClassPoolVisitor visitor1 = new TestClassPoolVisitor();
        ClassPoolVisitor visitor2 = new TestClassPoolVisitor();

        // Act
        RepeatedClassPoolVisitor repeated1 = new RepeatedClassPoolVisitor(trigger1, visitor1);
        RepeatedClassPoolVisitor repeated2 = new RepeatedClassPoolVisitor(trigger2, visitor2);

        // Assert
        assertNotNull(repeated1, "First instance should be created");
        assertNotNull(repeated2, "Second instance should be created");
        assertNotSame(repeated1, repeated2, "Instances should be different");
    }

    // =========================================================================
    // visitClassPool Tests - No Repetition
    // =========================================================================

    /**
     * Tests visitClassPool when the repeat trigger is never set.
     * The visitor should be called exactly once.
     */
    @Test
    public void testVisitClassPool_whenRepeatTriggerNeverSet() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        CountingClassPoolVisitor countingVisitor = new CountingClassPoolVisitor();
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, countingVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertEquals(1, countingVisitor.getCount(),
            "Visitor should be called exactly once when trigger is never set");
        assertFalse(repeatTrigger.isSet(),
            "Repeat trigger should remain false after execution");
    }

    /**
     * Tests visitClassPool with an empty ClassPool when trigger is not set.
     */
    @Test
    public void testVisitClassPool_withEmptyClassPoolNoRepeat() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        TrackingClassPoolVisitor trackingVisitor = new TrackingClassPoolVisitor();
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, trackingVisitor);
        ClassPool emptyClassPool = new ClassPool();

        // Act
        visitor.visitClassPool(emptyClassPool);

        // Assert
        assertTrue(trackingVisitor.wasVisited(),
            "Visitor should be invoked even with empty ClassPool");
        assertEquals(1, trackingVisitor.getCount(),
            "Visitor should be called exactly once");
    }

    // =========================================================================
    // visitClassPool Tests - Single Repetition
    // =========================================================================

    /**
     * Tests visitClassPool when the repeat trigger is set once.
     * The visitor should be called twice (initial call + one repeat).
     */
    @Test
    public void testVisitClassPool_whenRepeatTriggerSetOnce() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        SetTriggerOnceVisitor setOnceVisitor = new SetTriggerOnceVisitor(repeatTrigger);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, setOnceVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertEquals(2, setOnceVisitor.getCount(),
            "Visitor should be called twice (initial + one repeat)");
        assertFalse(repeatTrigger.isSet(),
            "Repeat trigger should be false after execution completes");
    }

    // =========================================================================
    // visitClassPool Tests - Multiple Repetitions
    // =========================================================================

    /**
     * Tests visitClassPool when the repeat trigger is set multiple times.
     * The visitor should be called multiple times until the trigger stops being set.
     */
    @Test
    public void testVisitClassPool_whenRepeatTriggerSetMultipleTimes() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        SetTriggerNTimesVisitor setNTimesVisitor = new SetTriggerNTimesVisitor(repeatTrigger, 3);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, setNTimesVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertEquals(4, setNTimesVisitor.getCount(),
            "Visitor should be called 4 times (initial + 3 repeats)");
        assertFalse(repeatTrigger.isSet(),
            "Repeat trigger should be false after execution completes");
    }

    /**
     * Tests visitClassPool with 5 repetitions.
     */
    @Test
    public void testVisitClassPool_withFiveRepetitions() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        SetTriggerNTimesVisitor setNTimesVisitor = new SetTriggerNTimesVisitor(repeatTrigger, 5);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, setNTimesVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertEquals(6, setNTimesVisitor.getCount(),
            "Visitor should be called 6 times (initial + 5 repeats)");
    }

    /**
     * Tests visitClassPool with 10 repetitions to verify higher iteration counts.
     */
    @Test
    public void testVisitClassPool_withTenRepetitions() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        SetTriggerNTimesVisitor setNTimesVisitor = new SetTriggerNTimesVisitor(repeatTrigger, 10);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, setNTimesVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertEquals(11, setNTimesVisitor.getCount(),
            "Visitor should be called 11 times (initial + 10 repeats)");
    }

    // =========================================================================
    // visitClassPool Tests - Trigger Reset Behavior
    // =========================================================================

    /**
     * Tests that the repeat trigger is properly reset before each iteration.
     * This ensures the do-while loop correctly resets the trigger.
     */
    @Test
    public void testVisitClassPool_properlyResetsTriggerBeforeEachIteration() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        TriggerStateTrackingVisitor trackingVisitor = new TriggerStateTrackingVisitor(repeatTrigger);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, trackingVisitor);
        ClassPool classPool = new ClassPool();

        // The tracking visitor will set the trigger on first call, then check it's reset on second call
        trackingVisitor.setTriggerOnCall(0); // Set trigger on first call
        trackingVisitor.expectResetOnCall(1); // Expect trigger to be reset before second call

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertTrue(trackingVisitor.wasResetDetected(),
            "Trigger should have been reset before the second iteration");
        assertEquals(2, trackingVisitor.getCount(),
            "Should have two iterations");
    }

    /**
     * Tests that the trigger state is correctly checked after each iteration.
     */
    @Test
    public void testVisitClassPool_checksRepeatTriggerAfterEachIteration() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        ConditionalTriggerVisitor conditionalVisitor = new ConditionalTriggerVisitor(repeatTrigger);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, conditionalVisitor);
        ClassPool classPool = new ClassPool();

        // Set trigger on calls 0, 1, 2 but not on call 3
        conditionalVisitor.setTriggerOnCalls(0, 1, 2);

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertEquals(4, conditionalVisitor.getCount(),
            "Visitor should be called 4 times (stops when trigger not set after 3rd call)");
    }

    // =========================================================================
    // visitClassPool Tests - Multiple Invocations
    // =========================================================================

    /**
     * Tests that visitClassPool can be called multiple times on the same instance.
     * Each invocation should be independent.
     */
    @Test
    public void testVisitClassPool_multipleInvocationsAreIndependent() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        CountingClassPoolVisitor countingVisitor = new CountingClassPoolVisitor();
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, countingVisitor);
        ClassPool classPool = new ClassPool();

        // Act - First invocation (no repeat)
        visitor.visitClassPool(classPool);
        int countAfterFirst = countingVisitor.getCount();

        // Act - Second invocation (no repeat)
        visitor.visitClassPool(classPool);
        int countAfterSecond = countingVisitor.getCount();

        // Act - Third invocation (no repeat)
        visitor.visitClassPool(classPool);
        int countAfterThird = countingVisitor.getCount();

        // Assert
        assertEquals(1, countAfterFirst, "First invocation should call visitor once");
        assertEquals(2, countAfterSecond, "Second invocation should call visitor once more");
        assertEquals(3, countAfterThird, "Third invocation should call visitor once more");
    }

    /**
     * Tests multiple invocations where the trigger is set in each invocation.
     */
    @Test
    public void testVisitClassPool_multipleInvocationsWithTriggerSet() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        SetTriggerOnceVisitor setOnceVisitor = new SetTriggerOnceVisitor(repeatTrigger);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, setOnceVisitor);
        ClassPool classPool = new ClassPool();

        // Act - First invocation (trigger set once, so 2 calls)
        visitor.visitClassPool(classPool);
        int countAfterFirst = setOnceVisitor.getCount();
        setOnceVisitor.reset();

        // Act - Second invocation (trigger set once again, so 2 more calls)
        visitor.visitClassPool(classPool);
        int countAfterSecond = setOnceVisitor.getCount();

        // Assert
        assertEquals(2, countAfterFirst, "First invocation should result in 2 calls");
        assertEquals(2, countAfterSecond, "Second invocation should result in 2 more calls");
    }

    // =========================================================================
    // visitClassPool Tests - Edge Cases
    // =========================================================================

    /**
     * Tests visitClassPool with null ClassPool parameter.
     * This tests how the visitor handles null input.
     */
    @Test
    public void testVisitClassPool_withNullClassPool() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        NullHandlingClassPoolVisitor nullHandlingVisitor = new NullHandlingClassPoolVisitor();
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, nullHandlingVisitor);

        // Act
        visitor.visitClassPool(null);

        // Assert
        assertTrue(nullHandlingVisitor.wasVisited(),
            "Visitor should be called even with null ClassPool");
        assertTrue(nullHandlingVisitor.wasNullReceived(),
            "Visitor should receive null ClassPool");
    }

    /**
     * Tests that different ClassPool instances can be passed in multiple invocations.
     */
    @Test
    public void testVisitClassPool_withDifferentClassPools() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        ClassPoolTrackingVisitor trackingVisitor = new ClassPoolTrackingVisitor();
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, trackingVisitor);
        ClassPool classPool1 = new ClassPool();
        ClassPool classPool2 = new ClassPool();
        ClassPool classPool3 = new ClassPool();

        // Act
        visitor.visitClassPool(classPool1);
        visitor.visitClassPool(classPool2);
        visitor.visitClassPool(classPool3);

        // Assert
        assertEquals(3, trackingVisitor.getDistinctClassPoolsCount(),
            "Three different ClassPool instances should have been visited");
    }

    /**
     * Tests that the same ClassPool is passed to the wrapped visitor in all iterations.
     */
    @Test
    public void testVisitClassPool_passesSameClassPoolToAllIterations() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        ClassPoolIdentityTrackingVisitor identityVisitor = new ClassPoolIdentityTrackingVisitor(repeatTrigger);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, identityVisitor);
        ClassPool classPool = new ClassPool();

        // Configure to repeat 3 times
        identityVisitor.setTriggerForNCalls(3);

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertTrue(identityVisitor.allCallsReceivedSameClassPool(),
            "All iterations should receive the same ClassPool instance");
        assertEquals(4, identityVisitor.getCount(),
            "Should have 4 iterations");
    }

    // =========================================================================
    // visitClassPool Tests - Exception Handling
    // =========================================================================

    /**
     * Tests that exceptions thrown by the wrapped visitor are propagated.
     */
    @Test
    public void testVisitClassPool_propagatesExceptionsFromWrappedVisitor() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        ExceptionThrowingClassPoolVisitor exceptionVisitor = new ExceptionThrowingClassPoolVisitor();
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, exceptionVisitor);
        ClassPool classPool = new ClassPool();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> visitor.visitClassPool(classPool),
            "Exception from wrapped visitor should propagate");
    }

    /**
     * Tests that exceptions are propagated even during repeated iterations.
     */
    @Test
    public void testVisitClassPool_propagatesExceptionDuringRepetition() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        ExceptionOnNthCallVisitor exceptionVisitor = new ExceptionOnNthCallVisitor(repeatTrigger, 3);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, exceptionVisitor);
        ClassPool classPool = new ClassPool();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> visitor.visitClassPool(classPool),
            "Exception on 3rd call should propagate");
        assertEquals(3, exceptionVisitor.getCount(),
            "Visitor should have been called 3 times before throwing exception");
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests a realistic scenario where the visitor iterates until convergence.
     * This simulates an optimization pass that needs multiple iterations.
     */
    @Test
    public void testVisitClassPool_realisticConvergenceScenario() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        SimulatedOptimizationVisitor optimizationVisitor =
            new SimulatedOptimizationVisitor(repeatTrigger, 7); // Converges after 7 iterations
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, optimizationVisitor);
        ClassPool classPool = new ClassPool();

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertTrue(optimizationVisitor.hasConverged(),
            "Optimization should have converged");
        assertEquals(7, optimizationVisitor.getCount(),
            "Should iterate until convergence");
    }

    /**
     * Tests that the visitor implements ClassPoolVisitor interface.
     */
    @Test
    public void testImplementsClassPoolVisitorInterface() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        ClassPoolVisitor delegateVisitor = new TestClassPoolVisitor();
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, delegateVisitor);

        // Assert
        assertTrue(visitor instanceof ClassPoolVisitor,
            "RepeatedClassPoolVisitor should implement ClassPoolVisitor interface");
    }

    /**
     * Tests the complete lifecycle with state transitions.
     */
    @Test
    public void testVisitClassPool_completeLifecycleWithStateTransitions() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        StateTransitionTrackingVisitor stateVisitor = new StateTransitionTrackingVisitor(repeatTrigger);
        RepeatedClassPoolVisitor visitor = new RepeatedClassPoolVisitor(repeatTrigger, stateVisitor);
        ClassPool classPool = new ClassPool();

        // Configure: set trigger on calls 0, 1, 2 (so 4 total calls)
        stateVisitor.setTriggerOnCalls(0, 1, 2);

        // Act
        visitor.visitClassPool(classPool);

        // Assert
        assertEquals(4, stateVisitor.getCount(), "Should have 4 iterations");
        assertTrue(stateVisitor.allTransitionsCorrect(),
            "All state transitions should be correct (reset before each iteration)");
    }

    // =========================================================================
    // Helper Classes
    // =========================================================================

    /**
     * Simple test implementation of ClassPoolVisitor for basic testing.
     */
    private static class TestClassPoolVisitor implements ClassPoolVisitor {
        @Override
        public void visitClassPool(ClassPool classPool) {
            // No-op implementation for testing
        }
    }

    /**
     * Tracking implementation that records if it was visited.
     */
    private static class TrackingClassPoolVisitor implements ClassPoolVisitor {
        private boolean visited = false;
        private int count = 0;

        @Override
        public void visitClassPool(ClassPool classPool) {
            visited = true;
            count++;
        }

        public boolean wasVisited() {
            return visited;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Counting implementation that tracks the number of invocations.
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

        public void reset() {
            count = 0;
        }
    }

    /**
     * Visitor that sets the trigger flag exactly once (on the first call).
     */
    private static class SetTriggerOnceVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private int count = 0;

        public SetTriggerOnceVisitor(MutableBoolean repeatTrigger) {
            this.repeatTrigger = repeatTrigger;
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            count++;
            if (count == 1) {
                repeatTrigger.set();
            }
        }

        public int getCount() {
            return count;
        }

        public void reset() {
            count = 0;
        }
    }

    /**
     * Visitor that sets the trigger flag N times.
     */
    private static class SetTriggerNTimesVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private final int timesToSet;
        private int count = 0;

        public SetTriggerNTimesVisitor(MutableBoolean repeatTrigger, int timesToSet) {
            this.repeatTrigger = repeatTrigger;
            this.timesToSet = timesToSet;
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            count++;
            if (count <= timesToSet) {
                repeatTrigger.set();
            }
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Visitor that tracks trigger state and verifies it's reset properly.
     */
    private static class TriggerStateTrackingVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private int count = 0;
        private int callToSetTrigger = -1;
        private int callToExpectReset = -1;
        private boolean resetDetected = false;

        public TriggerStateTrackingVisitor(MutableBoolean repeatTrigger) {
            this.repeatTrigger = repeatTrigger;
        }

        public void setTriggerOnCall(int call) {
            this.callToSetTrigger = call;
        }

        public void expectResetOnCall(int call) {
            this.callToExpectReset = call;
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            if (count == callToExpectReset && !repeatTrigger.isSet()) {
                resetDetected = true;
            }
            if (count == callToSetTrigger) {
                repeatTrigger.set();
            }
            count++;
        }

        public boolean wasResetDetected() {
            return resetDetected;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Visitor that conditionally sets the trigger based on call number.
     */
    private static class ConditionalTriggerVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private final java.util.Set<Integer> callsToSetTrigger = new java.util.HashSet<>();
        private int count = 0;

        public ConditionalTriggerVisitor(MutableBoolean repeatTrigger) {
            this.repeatTrigger = repeatTrigger;
        }

        public void setTriggerOnCalls(int... calls) {
            for (int call : calls) {
                callsToSetTrigger.add(call);
            }
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            if (callsToSetTrigger.contains(count)) {
                repeatTrigger.set();
            }
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Visitor that handles null ClassPool.
     */
    private static class NullHandlingClassPoolVisitor implements ClassPoolVisitor {
        private boolean visited = false;
        private boolean nullReceived = false;

        @Override
        public void visitClassPool(ClassPool classPool) {
            visited = true;
            if (classPool == null) {
                nullReceived = true;
            }
        }

        public boolean wasVisited() {
            return visited;
        }

        public boolean wasNullReceived() {
            return nullReceived;
        }
    }

    /**
     * Visitor that tracks distinct ClassPool instances.
     */
    private static class ClassPoolTrackingVisitor implements ClassPoolVisitor {
        private final java.util.Set<ClassPool> visitedClassPools = new java.util.HashSet<>();

        @Override
        public void visitClassPool(ClassPool classPool) {
            visitedClassPools.add(classPool);
        }

        public int getDistinctClassPoolsCount() {
            return visitedClassPools.size();
        }
    }

    /**
     * Visitor that verifies the same ClassPool is used across iterations.
     */
    private static class ClassPoolIdentityTrackingVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private final java.util.List<ClassPool> receivedClassPools = new java.util.ArrayList<>();
        private int triggerCallsRemaining = 0;

        public ClassPoolIdentityTrackingVisitor(MutableBoolean repeatTrigger) {
            this.repeatTrigger = repeatTrigger;
        }

        public void setTriggerForNCalls(int n) {
            this.triggerCallsRemaining = n;
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            receivedClassPools.add(classPool);
            if (triggerCallsRemaining > 0) {
                repeatTrigger.set();
                triggerCallsRemaining--;
            }
        }

        public boolean allCallsReceivedSameClassPool() {
            if (receivedClassPools.isEmpty()) {
                return true;
            }
            ClassPool first = receivedClassPools.get(0);
            for (ClassPool pool : receivedClassPools) {
                if (pool != first) {
                    return false;
                }
            }
            return true;
        }

        public int getCount() {
            return receivedClassPools.size();
        }
    }

    /**
     * Visitor that always throws an exception.
     */
    private static class ExceptionThrowingClassPoolVisitor implements ClassPoolVisitor {
        @Override
        public void visitClassPool(ClassPool classPool) {
            throw new RuntimeException("Test exception from visitor");
        }
    }

    /**
     * Visitor that throws an exception on the Nth call.
     */
    private static class ExceptionOnNthCallVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private final int callToThrowOn;
        private int count = 0;

        public ExceptionOnNthCallVisitor(MutableBoolean repeatTrigger, int callToThrowOn) {
            this.repeatTrigger = repeatTrigger;
            this.callToThrowOn = callToThrowOn;
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            count++;
            if (count < callToThrowOn) {
                repeatTrigger.set(); // Keep triggering repeats until we reach the exception
            }
            if (count == callToThrowOn) {
                throw new RuntimeException("Exception on call " + count);
            }
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Visitor that simulates an optimization pass that converges after N iterations.
     */
    private static class SimulatedOptimizationVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private final int iterationsToConverge;
        private int count = 0;
        private boolean converged = false;

        public SimulatedOptimizationVisitor(MutableBoolean repeatTrigger, int iterationsToConverge) {
            this.repeatTrigger = repeatTrigger;
            this.iterationsToConverge = iterationsToConverge;
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            count++;
            if (count < iterationsToConverge) {
                repeatTrigger.set(); // Continue iterating
            } else {
                converged = true; // Stop iterating
            }
        }

        public boolean hasConverged() {
            return converged;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Visitor that tracks state transitions to verify reset behavior.
     */
    private static class StateTransitionTrackingVisitor implements ClassPoolVisitor {
        private final MutableBoolean repeatTrigger;
        private final java.util.Set<Integer> callsToSetTrigger = new java.util.HashSet<>();
        private final java.util.List<Boolean> triggerStateAtStartOfEachCall = new java.util.ArrayList<>();
        private int count = 0;

        public StateTransitionTrackingVisitor(MutableBoolean repeatTrigger) {
            this.repeatTrigger = repeatTrigger;
        }

        public void setTriggerOnCalls(int... calls) {
            for (int call : calls) {
                callsToSetTrigger.add(call);
            }
        }

        @Override
        public void visitClassPool(ClassPool classPool) {
            // Record the trigger state at the start of this call
            triggerStateAtStartOfEachCall.add(repeatTrigger.isSet());

            if (callsToSetTrigger.contains(count)) {
                repeatTrigger.set();
            }
            count++;
        }

        public boolean allTransitionsCorrect() {
            // After the first call, all subsequent calls should start with trigger = false
            // because reset() is called before each iteration
            for (int i = 1; i < triggerStateAtStartOfEachCall.size(); i++) {
                if (triggerStateAtStartOfEachCall.get(i)) {
                    return false; // Trigger should be reset before each iteration
                }
            }
            return true;
        }

        public int getCount() {
            return count;
        }
    }
}
