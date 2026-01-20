package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReadWriteFieldMarker} constructors.
 *
 * Tests both constructors:
 * 1. ReadWriteFieldMarker(MutableBoolean) - delegates to three-parameter constructor with markReading=true and markWriting=true
 * 2. ReadWriteFieldMarker(MutableBoolean, boolean, boolean) - full constructor that allows specifying whether to mark reading and/or writing
 *
 * The MutableBoolean parameter acts as a repeat trigger that gets set whenever a field receives a new mark (read or written status).
 */
public class ReadWriteFieldMarkerClaude_constructorTest {

    // =========================================================================
    // Tests for ReadWriteFieldMarker(MutableBoolean) - One-parameter constructor
    // =========================================================================

    /**
     * Tests that the constructor successfully creates a ReadWriteFieldMarker instance with a non-null MutableBoolean.
     */
    @Test
    public void testConstructor_createsInstanceWithMutableBoolean() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger);

        // Assert
        assertNotNull(marker, "ReadWriteFieldMarker instance should be created");
    }

    /**
     * Tests that the constructor accepts a null MutableBoolean parameter.
     * The constructor does not validate the parameter, so null should be accepted.
     */
    @Test
    public void testConstructor_acceptsNullMutableBoolean() {
        // Act & Assert
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(null),
                "Constructor should accept null MutableBoolean parameter");
    }

    /**
     * Tests that the constructed ReadWriteFieldMarker is an instance of InstructionVisitor.
     */
    @Test
    public void testConstructor_createsInstructionVisitor() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger);

        // Assert
        assertInstanceOf(InstructionVisitor.class, marker,
                "ReadWriteFieldMarker should be an instance of InstructionVisitor");
    }

    /**
     * Tests that the constructed ReadWriteFieldMarker is an instance of ConstantVisitor.
     */
    @Test
    public void testConstructor_createsConstantVisitor() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger);

        // Assert
        assertInstanceOf(ConstantVisitor.class, marker,
                "ReadWriteFieldMarker should be an instance of ConstantVisitor");
    }

    /**
     * Tests that the constructed ReadWriteFieldMarker is an instance of MemberVisitor.
     */
    @Test
    public void testConstructor_createsMemberVisitor() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger);

        // Assert
        assertInstanceOf(MemberVisitor.class, marker,
                "ReadWriteFieldMarker should be an instance of MemberVisitor");
    }

    /**
     * Tests that multiple instances can be created independently with different MutableBoolean instances.
     */
    @Test
    public void testConstructor_createsMultipleIndependentInstances() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();
        MutableBoolean trigger3 = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(trigger2);
        ReadWriteFieldMarker marker3 = new ReadWriteFieldMarker(trigger3);

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "First and second markers should be different instances");
        assertNotSame(marker2, marker3, "Second and third markers should be different instances");
        assertNotSame(marker1, marker3, "First and third markers should be different instances");
    }

    /**
     * Tests that multiple instances can share the same MutableBoolean instance.
     * This is a valid use case where multiple markers might trigger the same repeat flag.
     */
    @Test
    public void testConstructor_allowsSharedMutableBoolean() {
        // Arrange
        MutableBoolean sharedTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(sharedTrigger);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(sharedTrigger);

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances even with shared trigger");
    }

    /**
     * Tests that the constructor completes quickly without expensive initialization.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        long startTime = System.nanoTime();

        // Act - create many instances
        for (int i = 0; i < 1000; i++) {
            new ReadWriteFieldMarker(repeatTrigger);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 instances)
        assertTrue(durationMs < 100, "Constructor should execute quickly with minimal overhead");
    }

    /**
     * Tests that the constructor is thread-safe and can be called concurrently.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int instancesPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final ReadWriteFieldMarker[][] results = new ReadWriteFieldMarker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                MutableBoolean trigger = new MutableBoolean();
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new ReadWriteFieldMarker(trigger);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < instancesPerThread; j++) {
                assertNotNull(results[i][j],
                        "Instance [" + i + "][" + j + "] should be created");
            }
        }
    }

    /**
     * Tests that constructed instances can be used as visitor immediately.
     * The marker should be usable without throwing NPE on visitor method calls.
     */
    @Test
    public void testConstructor_instanceIsUsableImmediately() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger);

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker.visitAnyInstruction(null, null, null, 0, null),
                "Newly constructed marker should be usable as an InstructionVisitor");
        assertDoesNotThrow(() -> marker.visitAnyConstant(null, null),
                "Newly constructed marker should be usable as a ConstantVisitor");
        assertDoesNotThrow(() -> marker.visitAnyMember(null, null),
                "Newly constructed marker should be usable as a MemberVisitor");
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(trigger2);

        // Assert
        assertEquals(marker1.getClass(), marker2.getClass(),
                "Both instances should be of the same class");
        assertNotSame(marker1, marker2,
                "Instances should be separate objects in memory");
    }

    /**
     * Tests that the constructor can be called repeatedly in quick succession.
     */
    @Test
    public void testConstructor_rapidSuccession() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                new ReadWriteFieldMarker(repeatTrigger);
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed markers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        java.util.Set<ReadWriteFieldMarker> markers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            markers.add(new ReadWriteFieldMarker(repeatTrigger));
        }

        // Assert - all instances should be distinct
        assertEquals(10, markers.size(),
                "All 10 instances should be distinct and stored in the set");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid parameters.
     */
    @Test
    public void testConstructor_doesNotThrowExceptionWithValidParameter() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act & Assert
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(repeatTrigger),
                "Constructor should not throw any exception with valid parameter");
    }

    /**
     * Tests that the constructor properly stores the MutableBoolean reference by verifying
     * the trigger is not modified during construction.
     */
    @Test
    public void testConstructor_doesNotModifyMutableBoolean() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        repeatTrigger.reset(); // Ensure it's false

        // Act
        new ReadWriteFieldMarker(repeatTrigger);

        // Assert
        assertFalse(repeatTrigger.isSet(),
                "Constructor should not modify the MutableBoolean state");
    }

    /**
     * Tests that the constructor can accept a MutableBoolean that is already set to true.
     */
    @Test
    public void testConstructor_acceptsPreSetMutableBoolean() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        repeatTrigger.set(); // Pre-set to true

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger);

        // Assert
        assertNotNull(marker, "Marker should be created with pre-set MutableBoolean");
        assertTrue(repeatTrigger.isSet(), "MutableBoolean should remain set");
    }

    /**
     * Tests that the constructor can be used with MutableBoolean in various initial states.
     */
    @Test
    public void testConstructor_worksWithVariousMutableBooleanStates() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean(); // false by default
        MutableBoolean trigger2 = new MutableBoolean();
        trigger2.set(); // true
        MutableBoolean trigger3 = new MutableBoolean();
        trigger3.set();
        trigger3.reset(); // set then reset to false

        // Act & Assert
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(trigger1),
                "Should work with default false state");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(trigger2),
                "Should work with set true state");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(trigger3),
                "Should work with reset false state");
    }

    /**
     * Tests that the constructor creates functionally independent instances that don't interfere.
     * Each marker should operate independently even when sharing a trigger.
     */
    @Test
    public void testConstructor_instancesAreFunctionallyIndependent() {
        // Arrange
        MutableBoolean sharedTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(sharedTrigger);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(sharedTrigger);

        // Assert - both should be usable independently
        assertDoesNotThrow(() -> {
            marker1.visitAnyInstruction(null, null, null, 0, null);
            marker2.visitAnyConstant(null, null);
        }, "Instances should be functionally independent");
    }

    /**
     * Tests constructor with newly created MutableBoolean inline.
     */
    @Test
    public void testConstructor_acceptsInlineMutableBoolean() {
        // Act & Assert
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(new MutableBoolean()),
                "Constructor should accept inline MutableBoolean creation");
    }

    // =========================================================================
    // Tests for ReadWriteFieldMarker(MutableBoolean, boolean, boolean) - Three-parameter constructor
    // =========================================================================

    /**
     * Tests that the three-parameter constructor creates an instance with all parameters.
     */
    @Test
    public void testThreeParamConstructor_createsInstance() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger, true, true);

        // Assert
        assertNotNull(marker, "ReadWriteFieldMarker instance should be created");
    }

    /**
     * Tests the three-parameter constructor with markReading=true and markWriting=true.
     * This should behave the same as the one-parameter constructor.
     */
    @Test
    public void testThreeParamConstructor_withBothTrue() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger, true, true);

        // Assert
        assertNotNull(marker, "Marker should be created with both flags true");
        assertInstanceOf(InstructionVisitor.class, marker);
        assertInstanceOf(ConstantVisitor.class, marker);
        assertInstanceOf(MemberVisitor.class, marker);
    }

    /**
     * Tests the three-parameter constructor with markReading=false and markWriting=true.
     * This creates a marker that only tracks writes, not reads.
     */
    @Test
    public void testThreeParamConstructor_withOnlyMarkWriting() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger, false, true);

        // Assert
        assertNotNull(marker, "Marker should be created with markReading=false, markWriting=true");
    }

    /**
     * Tests the three-parameter constructor with markReading=true and markWriting=false.
     * This creates a marker that only tracks reads, not writes.
     */
    @Test
    public void testThreeParamConstructor_withOnlyMarkReading() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger, true, false);

        // Assert
        assertNotNull(marker, "Marker should be created with markReading=true, markWriting=false");
    }

    /**
     * Tests the three-parameter constructor with markReading=false and markWriting=false.
     * This creates a marker that doesn't track reads or writes (effectively disabled).
     */
    @Test
    public void testThreeParamConstructor_withBothFalse() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger, false, false);

        // Assert
        assertNotNull(marker, "Marker should be created with both flags false");
    }

    /**
     * Tests the three-parameter constructor with null MutableBoolean.
     */
    @Test
    public void testThreeParamConstructor_acceptsNullMutableBoolean() {
        // Act & Assert
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(null, true, true),
                "Three-parameter constructor should accept null MutableBoolean");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(null, false, false),
                "Three-parameter constructor should accept null MutableBoolean with any boolean values");
    }

    /**
     * Tests all four boolean combinations for the three-parameter constructor.
     */
    @Test
    public void testThreeParamConstructor_allBooleanCombinations() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act & Assert - all combinations should work
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(repeatTrigger, true, true),
                "Constructor should accept markReading=true, markWriting=true");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(repeatTrigger, true, false),
                "Constructor should accept markReading=true, markWriting=false");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(repeatTrigger, false, true),
                "Constructor should accept markReading=false, markWriting=true");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(repeatTrigger, false, false),
                "Constructor should accept markReading=false, markWriting=false");
    }

    /**
     * Tests that the three-parameter constructor does not modify the MutableBoolean.
     */
    @Test
    public void testThreeParamConstructor_doesNotModifyMutableBoolean() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        repeatTrigger.reset(); // Ensure it's false

        // Act
        new ReadWriteFieldMarker(repeatTrigger, true, true);
        boolean afterTrueTrue = repeatTrigger.isSet();

        repeatTrigger.reset();
        new ReadWriteFieldMarker(repeatTrigger, false, false);
        boolean afterFalseFalse = repeatTrigger.isSet();

        // Assert
        assertFalse(afterTrueTrue, "Constructor should not modify MutableBoolean (true, true)");
        assertFalse(afterFalseFalse, "Constructor should not modify MutableBoolean (false, false)");
    }

    /**
     * Tests that multiple instances can be created with different boolean combinations.
     */
    @Test
    public void testThreeParamConstructor_createsMultipleInstances() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(trigger, true, true);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(trigger, false, true);
        ReadWriteFieldMarker marker3 = new ReadWriteFieldMarker(trigger, true, false);
        ReadWriteFieldMarker marker4 = new ReadWriteFieldMarker(trigger, false, false);

        // Assert
        assertNotNull(marker1, "Marker 1 should be created");
        assertNotNull(marker2, "Marker 2 should be created");
        assertNotNull(marker3, "Marker 3 should be created");
        assertNotNull(marker4, "Marker 4 should be created");
        assertNotSame(marker1, marker2, "Markers 1 and 2 should be different instances");
        assertNotSame(marker1, marker3, "Markers 1 and 3 should be different instances");
        assertNotSame(marker1, marker4, "Markers 1 and 4 should be different instances");
    }

    /**
     * Tests that the three-parameter constructor creates immediately usable instances.
     */
    @Test
    public void testThreeParamConstructor_instanceIsUsableImmediately() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(repeatTrigger, true, true);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(repeatTrigger, false, false);

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker1.visitAnyInstruction(null, null, null, 0, null),
                "Marker with both true should be usable");
        assertDoesNotThrow(() -> marker2.visitAnyConstant(null, null),
                "Marker with both false should be usable");
    }

    /**
     * Tests that the three-parameter constructor completes quickly.
     */
    @Test
    public void testThreeParamConstructor_completesQuickly() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        long startTime = System.nanoTime();

        // Act - create many instances with various boolean combinations
        for (int i = 0; i < 1000; i++) {
            boolean markReading = (i % 2 == 0);
            boolean markWriting = (i % 3 == 0);
            new ReadWriteFieldMarker(repeatTrigger, markReading, markWriting);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 instances)
        assertTrue(durationMs < 100, "Constructor should execute quickly with minimal overhead");
    }

    /**
     * Tests that the three-parameter constructor is thread-safe.
     */
    @Test
    public void testThreeParamConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int instancesPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final ReadWriteFieldMarker[][] results = new ReadWriteFieldMarker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads with various boolean values
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                MutableBoolean trigger = new MutableBoolean();
                for (int j = 0; j < instancesPerThread; j++) {
                    boolean markReading = (j % 2 == 0);
                    boolean markWriting = (j % 3 == 0);
                    results[threadIndex][j] = new ReadWriteFieldMarker(trigger, markReading, markWriting);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < instancesPerThread; j++) {
                assertNotNull(results[i][j],
                        "Instance [" + i + "][" + j + "] should be created");
            }
        }
    }

    /**
     * Tests that the three-parameter constructor can be called in rapid succession.
     */
    @Test
    public void testThreeParamConstructor_rapidSuccession() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                new ReadWriteFieldMarker(repeatTrigger, true, false);
                new ReadWriteFieldMarker(repeatTrigger, false, true);
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that markers created with three-parameter constructor can be stored in collections.
     */
    @Test
    public void testThreeParamConstructor_instancesAreCollectable() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        java.util.Set<ReadWriteFieldMarker> markers = new java.util.HashSet<>();

        // Act - create instances with various boolean combinations
        for (int i = 0; i < 10; i++) {
            boolean markReading = (i % 2 == 0);
            boolean markWriting = (i % 3 == 0);
            markers.add(new ReadWriteFieldMarker(repeatTrigger, markReading, markWriting));
        }

        // Assert - all instances should be distinct
        assertEquals(10, markers.size(),
                "All 10 instances should be distinct and stored in the set");
    }

    /**
     * Tests that the three-parameter constructor accepts pre-set MutableBoolean.
     */
    @Test
    public void testThreeParamConstructor_acceptsPreSetMutableBoolean() {
        // Arrange
        MutableBoolean repeatTrigger = new MutableBoolean();
        repeatTrigger.set(); // Pre-set to true

        // Act
        ReadWriteFieldMarker marker = new ReadWriteFieldMarker(repeatTrigger, true, true);

        // Assert
        assertNotNull(marker, "Marker should be created with pre-set MutableBoolean");
        assertTrue(repeatTrigger.isSet(), "MutableBoolean should remain set");
    }

    /**
     * Tests that the three-parameter constructor works with various MutableBoolean states.
     */
    @Test
    public void testThreeParamConstructor_worksWithVariousMutableBooleanStates() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean(); // false by default
        MutableBoolean trigger2 = new MutableBoolean();
        trigger2.set(); // true

        // Act & Assert
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(trigger1, true, false),
                "Should work with false MutableBoolean");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(trigger2, false, true),
                "Should work with true MutableBoolean");
    }

    /**
     * Tests creating markers with the same MutableBoolean but different boolean flags.
     */
    @Test
    public void testThreeParamConstructor_sharedTriggerDifferentFlags() {
        // Arrange
        MutableBoolean sharedTrigger = new MutableBoolean();

        // Act
        ReadWriteFieldMarker markerReadOnly = new ReadWriteFieldMarker(sharedTrigger, true, false);
        ReadWriteFieldMarker markerWriteOnly = new ReadWriteFieldMarker(sharedTrigger, false, true);
        ReadWriteFieldMarker markerBoth = new ReadWriteFieldMarker(sharedTrigger, true, true);
        ReadWriteFieldMarker markerNeither = new ReadWriteFieldMarker(sharedTrigger, false, false);

        // Assert
        assertNotNull(markerReadOnly, "Read-only marker should be created");
        assertNotNull(markerWriteOnly, "Write-only marker should be created");
        assertNotNull(markerBoth, "Both marker should be created");
        assertNotNull(markerNeither, "Neither marker should be created");

        // All should be distinct instances
        assertNotSame(markerReadOnly, markerWriteOnly);
        assertNotSame(markerReadOnly, markerBoth);
        assertNotSame(markerReadOnly, markerNeither);
    }

    /**
     * Tests that the three-parameter constructor with inline MutableBoolean creation.
     */
    @Test
    public void testThreeParamConstructor_acceptsInlineMutableBoolean() {
        // Act & Assert
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(new MutableBoolean(), true, true),
                "Constructor should accept inline MutableBoolean with true, true");
        assertDoesNotThrow(() -> new ReadWriteFieldMarker(new MutableBoolean(), false, false),
                "Constructor should accept inline MutableBoolean with false, false");
    }

    /**
     * Tests that both constructors create compatible instances.
     * A marker created with one-param constructor should be equivalent in type to one created with three-param constructor.
     */
    @Test
    public void testBothConstructors_createCompatibleInstances() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();

        // Act
        ReadWriteFieldMarker markerOneParam = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker markerThreeParam = new ReadWriteFieldMarker(trigger2, true, true);

        // Assert
        assertEquals(markerOneParam.getClass(), markerThreeParam.getClass(),
                "Both constructors should create instances of the same class");
        assertInstanceOf(InstructionVisitor.class, markerOneParam);
        assertInstanceOf(InstructionVisitor.class, markerThreeParam);
    }

    /**
     * Tests that instances from both constructors are usable in the same way.
     */
    @Test
    public void testBothConstructors_instancesUsableSameWay() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();

        // Act
        ReadWriteFieldMarker markerOneParam = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker markerThreeParam = new ReadWriteFieldMarker(trigger2, true, true);

        // Assert - both should support the same visitor methods
        assertDoesNotThrow(() -> markerOneParam.visitAnyInstruction(null, null, null, 0, null));
        assertDoesNotThrow(() -> markerThreeParam.visitAnyInstruction(null, null, null, 0, null));
        assertDoesNotThrow(() -> markerOneParam.visitAnyConstant(null, null));
        assertDoesNotThrow(() -> markerThreeParam.visitAnyConstant(null, null));
    }
}
