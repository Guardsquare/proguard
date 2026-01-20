package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodDescriptorShrinker#MethodDescriptorShrinker(MemberVisitor)}.
 *
 * The parameterized constructor in MethodDescriptorShrinker accepts an optional
 * extraMemberVisitor parameter. This visitor is called for any methods whose parameters
 * have been simplified/shrunk. The parameter can be null.
 *
 * The constructor stores the extraMemberVisitor in a private field for later use when
 * visiting program methods.
 *
 * These tests verify that the constructor:
 * 1. Successfully creates an instance with valid parameters
 * 2. Properly handles null extraMemberVisitor
 * 3. Creates functional instances that implement MemberVisitor
 * 4. Can be called repeatedly without issues
 * 5. Creates instances that are immediately usable
 */
public class MethodDescriptorShrinkerClaude_constructorTest {

    /**
     * Tests that the parameterized constructor successfully creates an instance with a non-null visitor.
     * This is the basic happy path with all parameters provided.
     */
    @Test
    public void testConstructor_withValidVisitor_createsInstance() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(extraMemberVisitor);

        // Assert
        assertNotNull(shrinker, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor accepts null for the optional extraMemberVisitor.
     * The extraMemberVisitor can be null since it's an optional callback.
     */
    @Test
    public void testConstructor_withNullVisitor_createsInstance() {
        // Act
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(null);

        // Assert
        assertNotNull(shrinker, "Constructor should accept null extra visitor");
    }

    /**
     * Tests that the constructor creates an instance that implements MemberVisitor.
     * MethodDescriptorShrinker implements MemberVisitor to visit and potentially
     * shrink method descriptors by removing unused parameters.
     */
    @Test
    public void testConstructor_implementsMemberVisitor() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(extraMemberVisitor);

        // Assert
        assertTrue(shrinker instanceof MemberVisitor,
            "MethodDescriptorShrinker should implement MemberVisitor");
    }

    /**
     * Tests that multiple instances can be created with different visitors.
     * Verifies each instance maintains its own state.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);
        MemberVisitor visitor3 = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker1 = new MethodDescriptorShrinker(visitor1);
        MethodDescriptorShrinker shrinker2 = new MethodDescriptorShrinker(visitor2);
        MethodDescriptorShrinker shrinker3 = new MethodDescriptorShrinker(visitor3);

        // Assert
        assertNotNull(shrinker1, "First instance should be created");
        assertNotNull(shrinker2, "Second instance should be created");
        assertNotNull(shrinker3, "Third instance should be created");
        assertNotSame(shrinker1, shrinker2, "Instances should be distinct");
        assertNotSame(shrinker2, shrinker3, "Instances should be distinct");
        assertNotSame(shrinker1, shrinker3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor can be called repeatedly without issues.
     */
    @Test
    public void testConstructor_repeatedConstruction_succeeds() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(
                    i % 2 == 0 ? extraMemberVisitor : null
                );
                assertNotNull(shrinker, "Instance " + i + " should be non-null");
            }
        }, "Should be able to construct many instances without issues");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(extraMemberVisitor);

        // Assert
        assertNotNull(shrinker, "Instance should be created");
        assertTrue(shrinker instanceof MethodDescriptorShrinker,
            "Should be instance of MethodDescriptorShrinker");
        assertEquals(MethodDescriptorShrinker.class, shrinker.getClass(),
            "Class should be MethodDescriptorShrinker");
    }

    /**
     * Tests that construction completes quickly without performing expensive operations.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        long startTime = System.nanoTime();
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(extraMemberVisitor);
        long endTime = System.nanoTime();

        // Assert
        assertNotNull(shrinker, "Instance should be created");
        long durationNanos = endTime - startTime;
        long oneMillisecondInNanos = 1_000_000L;
        assertTrue(durationNanos < oneMillisecondInNanos,
            "Constructor should complete very quickly, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that the constructor works in a multi-threaded environment.
     * Verifies there are no concurrency issues with construction.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final Thread[] threads = new Thread[threadCount];
        final MethodDescriptorShrinker[] shrinkers = new MethodDescriptorShrinker[threadCount];
        final Exception[] exceptions = new Exception[threadCount];

        // Act - create shrinkers in parallel threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    MemberVisitor visitor = mock(MemberVisitor.class);
                    shrinkers[index] = new MethodDescriptorShrinker(
                        index % 2 == 0 ? visitor : null
                    );
                } catch (Exception e) {
                    exceptions[index] = e;
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        for (int i = 0; i < threadCount; i++) {
            assertNull(exceptions[i], "No exceptions should occur in thread " + i);
            assertNotNull(shrinkers[i], "Shrinker should be created in thread " + i);
        }
    }

    /**
     * Tests that the constructor does not throw exceptions with valid parameters.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act & Assert
        assertDoesNotThrow(() -> new MethodDescriptorShrinker(extraMemberVisitor),
            "Constructor should not throw with valid parameters");
    }

    /**
     * Tests that the instance's toString() method works after construction.
     */
    @Test
    public void testConstructor_toStringWorks() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(extraMemberVisitor);
        String toString = shrinker.toString();

        // Assert
        assertNotNull(toString, "toString() should return a non-null value");
        assertTrue(toString.contains("MethodDescriptorShrinker"),
            "toString() should contain the class name");
    }

    /**
     * Tests that hashCode() works on the constructed instance.
     */
    @Test
    public void testConstructor_hashCodeWorks() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(extraMemberVisitor);

        // Assert
        assertDoesNotThrow(() -> shrinker.hashCode(),
            "hashCode() should work on constructed instance");
    }

    /**
     * Tests that equals() works on the constructed instance.
     */
    @Test
    public void testConstructor_equalsWorks() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker1 = new MethodDescriptorShrinker(extraMemberVisitor);
        MethodDescriptorShrinker shrinker2 = new MethodDescriptorShrinker(extraMemberVisitor);

        // Assert
        assertDoesNotThrow(() -> shrinker1.equals(shrinker2),
            "equals() should work on constructed instances");
        assertTrue(shrinker1.equals(shrinker1),
            "Instance should equal itself");
    }

    /**
     * Tests that the same MemberVisitor can be shared across multiple instances.
     * Verifies the constructor doesn't claim exclusive ownership of the visitor.
     */
    @Test
    public void testConstructor_sharedVisitor_succeeds() {
        // Arrange
        MemberVisitor sharedVisitor = mock(MemberVisitor.class);

        // Act
        MethodDescriptorShrinker shrinker1 = new MethodDescriptorShrinker(sharedVisitor);
        MethodDescriptorShrinker shrinker2 = new MethodDescriptorShrinker(sharedVisitor);
        MethodDescriptorShrinker shrinker3 = new MethodDescriptorShrinker(sharedVisitor);

        // Assert
        assertNotNull(shrinker1, "First instance with shared visitor should be created");
        assertNotNull(shrinker2, "Second instance with shared visitor should be created");
        assertNotNull(shrinker3, "Third instance with shared visitor should be created");
    }

    /**
     * Tests that the constructor works with real (non-mock) MemberVisitor implementations.
     */
    @Test
    public void testConstructor_withRealVisitor_createsInstance() {
        // Arrange
        MemberVisitor realVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodDescriptorShrinker shrinker = new MethodDescriptorShrinker(realVisitor);

        // Assert
        assertNotNull(shrinker, "Constructor should work with real visitor implementations");
    }

    /**
     * Tests that construction with various visitor implementations succeeds.
     */
    @Test
    public void testConstructor_variousVisitorTypes_succeed() {
        // Arrange
        MemberVisitor mockVisitor = mock(MemberVisitor.class);
        MemberVisitor realVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act & Assert
        assertDoesNotThrow(() -> {
            new MethodDescriptorShrinker(null);
            new MethodDescriptorShrinker(mockVisitor);
            new MethodDescriptorShrinker(realVisitor);
        }, "All visitor types should be valid");
    }
}
