package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.MultiValueMap;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReverseDependencyStore.InfluencedMethodTraveller#InfluencedMethodTraveller(MemberVisitor)}.
 *
 * The InfluencedMethodTraveller is an inner class of ReverseDependencyStore that implements MemberVisitor.
 * Its constructor accepts a MemberVisitor parameter that will be used to visit influenced methods when
 * side effects for a certain method are derived.
 *
 * These tests verify that the constructor:
 * 1. Successfully creates an instance with a valid MemberVisitor
 * 2. Properly handles null MemberVisitor (if applicable)
 * 3. Creates instances that are immediately usable
 * 4. Can be called repeatedly without issues
 * 5. Creates distinct instances with independent state
 */
public class ReverseDependencyStoreClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates an instance with a valid MemberVisitor.
     * This is the basic happy path with a properly initialized MemberVisitor.
     */
    @Test
    public void testConstructor_withValidMemberVisitor_createsInstance() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller =
            store.new InfluencedMethodTraveller(memberVisitor);

        // Assert
        assertNotNull(traveller, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor accepts null for the MemberVisitor parameter.
     * Null may be accepted if the traveller is meant to be configured later,
     * or may fail if MemberVisitor is required.
     */
    @Test
    public void testConstructor_withNullMemberVisitor_createsInstance() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);

        // Act & Assert
        assertDoesNotThrow(() -> {
            ReverseDependencyStore.InfluencedMethodTraveller traveller =
                store.new InfluencedMethodTraveller(null);
            assertNotNull(traveller, "Constructor should accept null MemberVisitor");
        }, "Constructor should not throw with null MemberVisitor");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller =
            store.new InfluencedMethodTraveller(memberVisitor);

        // Assert
        assertNotNull(traveller, "Instance should be created");
        assertTrue(traveller instanceof ReverseDependencyStore.InfluencedMethodTraveller,
            "Should be instance of InfluencedMethodTraveller");
        assertTrue(traveller instanceof MemberVisitor,
            "Should be instance of MemberVisitor");
    }

    /**
     * Tests that multiple instances can be created with different MemberVisitors.
     * Verifies each instance maintains its own state.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);

        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new AnotherTestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller1 =
            store.new InfluencedMethodTraveller(visitor1);
        ReverseDependencyStore.InfluencedMethodTraveller traveller2 =
            store.new InfluencedMethodTraveller(visitor2);
        ReverseDependencyStore.InfluencedMethodTraveller traveller3 =
            store.new InfluencedMethodTraveller(visitor1); // Reuse visitor1

        // Assert
        assertNotNull(traveller1, "First instance should be created");
        assertNotNull(traveller2, "Second instance should be created");
        assertNotNull(traveller3, "Third instance should be created");
        assertNotSame(traveller1, traveller2, "Instances should be distinct");
        assertNotSame(traveller2, traveller3, "Instances should be distinct");
        assertNotSame(traveller1, traveller3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor can be called repeatedly without issues.
     */
    @Test
    public void testConstructor_repeatedConstruction_succeeds() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor visitor = new TestMemberVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ReverseDependencyStore.InfluencedMethodTraveller traveller =
                    store.new InfluencedMethodTraveller(visitor);
                assertNotNull(traveller, "Instance " + i + " should be non-null");
            }
        }, "Should be able to construct many instances without issues");
    }

    /**
     * Tests that construction completes quickly without performing expensive operations.
     * The constructor should only store the MemberVisitor reference, not process it.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor visitor = new TestMemberVisitor();

        // Act
        long startTime = System.nanoTime();
        ReverseDependencyStore.InfluencedMethodTraveller traveller =
            store.new InfluencedMethodTraveller(visitor);
        long endTime = System.nanoTime();

        // Assert
        assertNotNull(traveller, "Instance should be created");
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
        final ReverseDependencyStore.InfluencedMethodTraveller[] travellers =
            new ReverseDependencyStore.InfluencedMethodTraveller[threadCount];
        final Exception[] exceptions = new Exception[threadCount];

        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        final ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - create travellers in parallel threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    travellers[index] = store.new InfluencedMethodTraveller(visitor);
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
            assertNotNull(travellers[i], "Traveller should be created in thread " + i);
        }
    }

    /**
     * Tests that the constructor does not throw exceptions with valid parameters.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor visitor = new TestMemberVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> store.new InfluencedMethodTraveller(visitor),
            "Constructor should not throw with valid parameters");
    }

    /**
     * Tests that the instance's toString() method works after construction.
     */
    @Test
    public void testConstructor_toStringWorks() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor visitor = new TestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller =
            store.new InfluencedMethodTraveller(visitor);
        String toString = traveller.toString();

        // Assert
        assertNotNull(toString, "toString() should return a non-null value");
        assertTrue(toString.contains("InfluencedMethodTraveller") ||
                   toString.contains("ReverseDependencyStore"),
            "toString() should contain a relevant class name");
    }

    /**
     * Tests that hashCode() works on the constructed instance.
     */
    @Test
    public void testConstructor_hashCodeWorks() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor visitor = new TestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller =
            store.new InfluencedMethodTraveller(visitor);

        // Assert
        assertDoesNotThrow(() -> traveller.hashCode(),
            "hashCode() should work on constructed instance");
    }

    /**
     * Tests that equals() works on the constructed instance.
     */
    @Test
    public void testConstructor_equalsWorks() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor visitor = new TestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller1 =
            store.new InfluencedMethodTraveller(visitor);
        ReverseDependencyStore.InfluencedMethodTraveller traveller2 =
            store.new InfluencedMethodTraveller(visitor);

        // Assert
        assertDoesNotThrow(() -> traveller1.equals(traveller2),
            "equals() should work on constructed instances");
        assertTrue(traveller1.equals(traveller1),
            "Instance should equal itself");
    }

    /**
     * Tests that the same MemberVisitor can be shared across multiple traveller instances.
     * Verifies the constructor doesn't claim exclusive ownership of the MemberVisitor.
     */
    @Test
    public void testConstructor_sharedMemberVisitor_succeeds() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);
        MemberVisitor sharedVisitor = new TestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller1 =
            store.new InfluencedMethodTraveller(sharedVisitor);
        ReverseDependencyStore.InfluencedMethodTraveller traveller2 =
            store.new InfluencedMethodTraveller(sharedVisitor);
        ReverseDependencyStore.InfluencedMethodTraveller traveller3 =
            store.new InfluencedMethodTraveller(sharedVisitor);

        // Assert
        assertNotNull(traveller1, "First instance with shared MemberVisitor should be created");
        assertNotNull(traveller2, "Second instance with shared MemberVisitor should be created");
        assertNotNull(traveller3, "Third instance with shared MemberVisitor should be created");
    }

    /**
     * Tests that multiple InfluencedMethodTraveller instances can be created from the same store.
     * Verifies that the store can create multiple traveller instances.
     */
    @Test
    public void testConstructor_multipleFromSameStore_succeeds() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);

        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new AnotherTestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller1 =
            store.new InfluencedMethodTraveller(visitor1);
        ReverseDependencyStore.InfluencedMethodTraveller traveller2 =
            store.new InfluencedMethodTraveller(visitor2);

        // Assert
        assertNotNull(traveller1, "First traveller should be created");
        assertNotNull(traveller2, "Second traveller should be created");
        assertNotSame(traveller1, traveller2, "Travellers should be distinct instances");
    }

    /**
     * Tests that InfluencedMethodTraveller instances from different stores are independent.
     */
    @Test
    public void testConstructor_fromDifferentStores_independent() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy1 = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo1 = new MultiValueMap<>();
        ReverseDependencyStore store1 = new ReverseDependencyStore(calledBy1, methodsByInfo1);

        MultiValueMap<Method, ClassMemberPair> calledBy2 = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo2 = new MultiValueMap<>();
        ReverseDependencyStore store2 = new ReverseDependencyStore(calledBy2, methodsByInfo2);

        MemberVisitor visitor = new TestMemberVisitor();

        // Act
        ReverseDependencyStore.InfluencedMethodTraveller traveller1 =
            store1.new InfluencedMethodTraveller(visitor);
        ReverseDependencyStore.InfluencedMethodTraveller traveller2 =
            store2.new InfluencedMethodTraveller(visitor);

        // Assert
        assertNotNull(traveller1, "First traveller should be created");
        assertNotNull(traveller2, "Second traveller should be created");
        assertNotSame(traveller1, traveller2, "Travellers from different stores should be distinct");
    }

    /**
     * Tests that the constructor works with different MemberVisitor implementations.
     */
    @Test
    public void testConstructor_withDifferentMemberVisitorImplementations_succeeds() {
        // Arrange
        MultiValueMap<Method, ClassMemberPair> calledBy = new MultiValueMap<>();
        MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByInfo = new MultiValueMap<>();
        ReverseDependencyStore store = new ReverseDependencyStore(calledBy, methodsByInfo);

        // Act & Assert
        assertDoesNotThrow(() -> {
            ReverseDependencyStore.InfluencedMethodTraveller traveller1 =
                store.new InfluencedMethodTraveller(new TestMemberVisitor());
            ReverseDependencyStore.InfluencedMethodTraveller traveller2 =
                store.new InfluencedMethodTraveller(new AnotherTestMemberVisitor());

            assertNotNull(traveller1, "Should create traveller with TestMemberVisitor");
            assertNotNull(traveller2, "Should create traveller with AnotherTestMemberVisitor");
        });
    }

    /**
     * Simple test MemberVisitor implementation for testing purposes.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }
    }

    /**
     * Another test MemberVisitor implementation for testing purposes.
     */
    private static class AnotherTestMemberVisitor implements MemberVisitor {
        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }
    }
}
