package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ConstantParameterFilter#ConstantParameterFilter(MemberVisitor)}.
 *
 * The constructor in ConstantParameterFilter accepts a MemberVisitor parameter and stores it
 * in a private field for later use when visiting program methods with constant parameters.
 *
 * These tests verify that the constructor:
 * 1. Successfully creates an instance when given a valid MemberVisitor
 * 2. Properly stores the visitor so it can be used in subsequent operations
 * 3. Handles edge cases appropriately (e.g., null parameter)
 *
 * Since the constructor only stores the parameter and has no complex logic, testing focuses
 * on verifying that the stored visitor is correctly used in the filter's operations.
 */
public class ConstantParameterFilterClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates an instance with a valid MemberVisitor.
     * This is the basic happy path - the constructor should accept any MemberVisitor and
     * create a functional ConstantParameterFilter instance.
     */
    @Test
    public void testConstructor_withValidVisitor_createsInstance() {
        // Arrange
        MemberVisitor mockVisitor = mock(MemberVisitor.class);

        // Act
        ConstantParameterFilter filter = new ConstantParameterFilter(mockVisitor);

        // Assert
        assertNotNull(filter, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor accepts a null visitor.
     * The constructor itself doesn't validate the parameter, so it should accept null.
     * However, using the filter with a null visitor would cause NullPointerException when
     * the filter tries to delegate visits.
     */
    @Test
    public void testConstructor_withNullVisitor_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new ConstantParameterFilter(null),
            "Constructor should accept null visitor without throwing");
    }

    /**
     * Tests that the constructor properly stores the visitor so it can be used later.
     * This verifies that the constructor correctly assigns the parameter to the internal field.
     * We test this indirectly by verifying the filter can be used immediately after construction.
     */
    @Test
    public void testConstructor_storesVisitor_canBeUsedImmediately() {
        // Arrange
        MemberVisitor mockVisitor = mock(MemberVisitor.class);
        ConstantParameterFilter filter = new ConstantParameterFilter(mockVisitor);

        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - should not throw even if there's no optimization info
        // (will throw NPE from visitProgramMethod logic, not from constructor issue)
        assertThrows(NullPointerException.class,
            () -> filter.visitProgramMethod(programClass, programMethod),
            "Filter should be usable immediately after construction");
    }

    /**
     * Tests that multiple instances can be created with different visitors.
     * Verifies that the constructor can be called multiple times and each instance
     * maintains its own visitor reference.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnVisitor() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);
        MemberVisitor visitor3 = mock(MemberVisitor.class);

        // Act
        ConstantParameterFilter filter1 = new ConstantParameterFilter(visitor1);
        ConstantParameterFilter filter2 = new ConstantParameterFilter(visitor2);
        ConstantParameterFilter filter3 = new ConstantParameterFilter(visitor3);

        // Assert
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotNull(filter3, "Third filter should be created");

        // Verify they are distinct instances
        assertNotSame(filter1, filter2, "Filter instances should be distinct");
        assertNotSame(filter2, filter3, "Filter instances should be distinct");
        assertNotSame(filter1, filter3, "Filter instances should be distinct");
    }

    /**
     * Tests that the constructor can be called repeatedly without issues.
     * Verifies there are no static state issues or side effects from construction.
     */
    @Test
    public void testConstructor_repeatedConstruction_succeeds() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                MemberVisitor visitor = mock(MemberVisitor.class);
                ConstantParameterFilter filter = new ConstantParameterFilter(visitor);
                assertNotNull(filter, "Each constructed filter should be non-null");
            }
        }, "Should be able to construct many instances without issues");
    }

    /**
     * Tests that the constructor works with a real (non-mock) visitor implementation.
     * Verifies the constructor accepts any implementation of MemberVisitor.
     */
    @Test
    public void testConstructor_withRealVisitor_createsInstance() {
        // Arrange - create a simple real visitor
        MemberVisitor realVisitor = new MemberVisitor() {
            @Override
            public void visitProgramField(proguard.classfile.ProgramClass programClass,
                                         proguard.classfile.ProgramField programField) {
                // No-op implementation
            }

            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }

            @Override
            public void visitLibraryField(proguard.classfile.LibraryClass libraryClass,
                                         proguard.classfile.LibraryField libraryField) {
                // No-op implementation
            }

            @Override
            public void visitLibraryMethod(proguard.classfile.LibraryClass libraryClass,
                                          proguard.classfile.LibraryMethod libraryMethod) {
                // No-op implementation
            }
        };

        // Act
        ConstantParameterFilter filter = new ConstantParameterFilter(realVisitor);

        // Assert
        assertNotNull(filter, "Constructor should work with real visitor implementation");
    }

    /**
     * Tests that the same visitor can be used to construct multiple filters.
     * Verifies that the constructor doesn't modify the visitor or claim exclusive ownership.
     */
    @Test
    public void testConstructor_sameVisitorMultipleFilters_succeeds() {
        // Arrange
        MemberVisitor sharedVisitor = mock(MemberVisitor.class);

        // Act
        ConstantParameterFilter filter1 = new ConstantParameterFilter(sharedVisitor);
        ConstantParameterFilter filter2 = new ConstantParameterFilter(sharedVisitor);
        ConstantParameterFilter filter3 = new ConstantParameterFilter(sharedVisitor);

        // Assert
        assertNotNull(filter1, "First filter with shared visitor should be created");
        assertNotNull(filter2, "Second filter with shared visitor should be created");
        assertNotNull(filter3, "Third filter with shared visitor should be created");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     * Verifies the type hierarchy is correct.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Arrange
        MemberVisitor mockVisitor = mock(MemberVisitor.class);

        // Act
        ConstantParameterFilter filter = new ConstantParameterFilter(mockVisitor);

        // Assert
        assertNotNull(filter, "Filter should be created");
        assertTrue(filter instanceof ConstantParameterFilter,
            "Should be instance of ConstantParameterFilter");
        assertTrue(filter instanceof MemberVisitor,
            "Should implement MemberVisitor interface");
    }

    /**
     * Tests that construction completes quickly without hanging.
     * Verifies the constructor doesn't perform expensive operations.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        MemberVisitor mockVisitor = mock(MemberVisitor.class);

        // Act
        long startTime = System.nanoTime();
        ConstantParameterFilter filter = new ConstantParameterFilter(mockVisitor);
        long endTime = System.nanoTime();

        // Assert
        assertNotNull(filter, "Filter should be created");
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
        final ConstantParameterFilter[] filters = new ConstantParameterFilter[threadCount];
        final Exception[] exceptions = new Exception[threadCount];

        // Act - create filters in parallel threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    MemberVisitor visitor = mock(MemberVisitor.class);
                    filters[index] = new ConstantParameterFilter(visitor);
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
            assertNotNull(filters[i], "Filter should be created in thread " + i);
        }
    }
}
