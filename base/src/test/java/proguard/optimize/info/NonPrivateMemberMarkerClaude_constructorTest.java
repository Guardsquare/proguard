package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NonPrivateMemberMarker} default constructor.
 *
 * The NonPrivateMemberMarker has an implicit default no-arg constructor since no constructor
 * is explicitly defined. This constructor creates a new instance and initializes the two member
 * fields (filteredMemberMarker and implementedMethodMarker) with their declared values.
 */
public class NonPrivateMemberMarkerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a NonPrivateMemberMarker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        NonPrivateMemberMarker marker = new NonPrivateMemberMarker();

        // Assert
        assertNotNull(marker, "NonPrivateMemberMarker instance should be created");
    }

    /**
     * Tests that the constructed NonPrivateMemberMarker is an instance of ClassVisitor.
     */
    @Test
    public void testConstructor_createsClassVisitor() {
        // Act
        NonPrivateMemberMarker marker = new NonPrivateMemberMarker();

        // Assert
        assertInstanceOf(ClassVisitor.class, marker,
                "NonPrivateMemberMarker should be an instance of ClassVisitor");
    }

    /**
     * Tests that the constructed NonPrivateMemberMarker is an instance of ConstantVisitor.
     */
    @Test
    public void testConstructor_createsConstantVisitor() {
        // Act
        NonPrivateMemberMarker marker = new NonPrivateMemberMarker();

        // Assert
        assertInstanceOf(ConstantVisitor.class, marker,
                "NonPrivateMemberMarker should be an instance of ConstantVisitor");
    }

    /**
     * Tests that the constructed NonPrivateMemberMarker is an instance of MemberVisitor.
     */
    @Test
    public void testConstructor_createsMemberVisitor() {
        // Act
        NonPrivateMemberMarker marker = new NonPrivateMemberMarker();

        // Assert
        assertInstanceOf(MemberVisitor.class, marker,
                "NonPrivateMemberMarker should be an instance of MemberVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        NonPrivateMemberMarker marker1 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker2 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker3 = new NonPrivateMemberMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "First and second markers should be different instances");
        assertNotSame(marker2, marker3, "Second and third markers should be different instances");
        assertNotSame(marker1, marker3, "First and third markers should be different instances");
    }

    /**
     * Tests that the constructor completes quickly without expensive initialization.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - create many instances
        for (int i = 0; i < 1000; i++) {
            new NonPrivateMemberMarker();
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
        final NonPrivateMemberMarker[][] results = new NonPrivateMemberMarker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new NonPrivateMemberMarker();
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
     * Tests that constructed instances can be used as ClassVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableAsClassVisitorImmediately() {
        // Act
        NonPrivateMemberMarker marker = new NonPrivateMemberMarker();

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker.visitAnyClass(null),
                "Newly constructed marker should be usable as a ClassVisitor");
    }

    /**
     * Tests that constructed instances can be used as ConstantVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableAsConstantVisitorImmediately() {
        // Act
        NonPrivateMemberMarker marker = new NonPrivateMemberMarker();

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker.visitAnyConstant(null, null),
                "Newly constructed marker should be usable as a ConstantVisitor");
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        NonPrivateMemberMarker marker1 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker2 = new NonPrivateMemberMarker();

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
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                new NonPrivateMemberMarker();
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed markers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<NonPrivateMemberMarker> markers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            markers.add(new NonPrivateMemberMarker());
        }

        // Assert - all instances should be distinct
        assertEquals(10, markers.size(),
                "All 10 instances should be distinct and stored in the set");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new NonPrivateMemberMarker(),
                "Constructor should not throw any exception");
    }
}
