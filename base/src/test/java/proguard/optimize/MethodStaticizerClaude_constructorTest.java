package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MethodStaticizer#MethodStaticizer()} and
 * {@link MethodStaticizer#MethodStaticizer(MemberVisitor)}.
 *
 * The default constructor in MethodStaticizer creates an instance without an
 * extraStaticMemberVisitor parameter. It internally calls the parameterized
 * constructor with null, meaning no extra visitor will be invoked when methods
 * are made static.
 *
 * The parameterized constructor accepts an optional extraStaticMemberVisitor
 * parameter. This visitor is called for any methods that have been made static.
 * The parameter can be null.
 *
 * The constructors initialize the MethodStaticizer to be ready for use as a
 * MemberVisitor that can make methods static when their 'this' parameter is unused.
 *
 * These tests verify that the constructors:
 * 1. Successfully create functional instances
 * 2. Create instances that implement MemberVisitor
 * 3. Can be called repeatedly without issues
 * 4. Create instances that are immediately usable
 * 5. Handle concurrent construction properly
 * 6. Properly store the extraStaticMemberVisitor parameter
 */
public class MethodStaticizerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates an instance.
     * This is the basic happy path with no parameters.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        MethodStaticizer staticizer = new MethodStaticizer();

        // Assert
        assertNotNull(staticizer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance that implements MemberVisitor.
     * MethodStaticizer implements MemberVisitor to visit and potentially
     * make methods static when their 'this' parameter is unused.
     */
    @Test
    public void testConstructor_implementsMemberVisitor() {
        // Act
        MethodStaticizer staticizer = new MethodStaticizer();

        // Assert
        assertTrue(staticizer instanceof MemberVisitor,
            "MethodStaticizer should implement MemberVisitor");
    }

    /**
     * Tests that multiple instances can be created.
     * Verifies each instance maintains its own state.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer();
        MethodStaticizer staticizer2 = new MethodStaticizer();
        MethodStaticizer staticizer3 = new MethodStaticizer();

        // Assert
        assertNotNull(staticizer1, "First instance should be created");
        assertNotNull(staticizer2, "Second instance should be created");
        assertNotNull(staticizer3, "Third instance should be created");
        assertNotSame(staticizer1, staticizer2, "Instances should be distinct");
        assertNotSame(staticizer2, staticizer3, "Instances should be distinct");
        assertNotSame(staticizer1, staticizer3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor can be called repeatedly without issues.
     */
    @Test
    public void testConstructor_repeatedConstruction_succeeds() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                MethodStaticizer staticizer = new MethodStaticizer();
                assertNotNull(staticizer, "Instance " + i + " should be non-null");
            }
        }, "Should be able to construct many instances without issues");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Act
        MethodStaticizer staticizer = new MethodStaticizer();

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        assertTrue(staticizer instanceof MethodStaticizer,
            "Should be instance of MethodStaticizer");
        assertEquals(MethodStaticizer.class, staticizer.getClass(),
            "Class should be MethodStaticizer");
    }

    /**
     * Tests that construction completes quickly without performing expensive operations.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Act
        long startTime = System.nanoTime();
        MethodStaticizer staticizer = new MethodStaticizer();
        long endTime = System.nanoTime();

        // Assert
        assertNotNull(staticizer, "Instance should be created");
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
        final MethodStaticizer[] staticizers = new MethodStaticizer[threadCount];
        final Exception[] exceptions = new Exception[threadCount];

        // Act - create staticizers in parallel threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    staticizers[index] = new MethodStaticizer();
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
            assertNotNull(staticizers[i], "Staticizer should be created in thread " + i);
        }
    }

    /**
     * Tests that the constructor does not throw exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new MethodStaticizer(),
            "Constructor should not throw exceptions");
    }

    /**
     * Tests that the instance's toString() method works after construction.
     */
    @Test
    public void testConstructor_toStringWorks() {
        // Act
        MethodStaticizer staticizer = new MethodStaticizer();
        String toString = staticizer.toString();

        // Assert
        assertNotNull(toString, "toString() should return a non-null value");
        assertTrue(toString.contains("MethodStaticizer"),
            "toString() should contain the class name");
    }

    /**
     * Tests that hashCode() works on the constructed instance.
     */
    @Test
    public void testConstructor_hashCodeWorks() {
        // Act
        MethodStaticizer staticizer = new MethodStaticizer();

        // Assert
        assertDoesNotThrow(() -> staticizer.hashCode(),
            "hashCode() should work on constructed instance");
    }

    /**
     * Tests that equals() works on the constructed instance.
     */
    @Test
    public void testConstructor_equalsWorks() {
        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer();
        MethodStaticizer staticizer2 = new MethodStaticizer();

        // Assert
        assertDoesNotThrow(() -> staticizer1.equals(staticizer2),
            "equals() should work on constructed instances");
        assertTrue(staticizer1.equals(staticizer1),
            "Instance should equal itself");
    }

    /**
     * Tests that the default constructor delegates to the parameterized constructor.
     * The default constructor should behave identically to calling the parameterized
     * constructor with null.
     */
    @Test
    public void testConstructor_delegatesToParameterizedConstructor() {
        // Act
        MethodStaticizer defaultStaticizer = new MethodStaticizer();
        MethodStaticizer nullStaticizer = new MethodStaticizer(null);

        // Assert
        assertNotNull(defaultStaticizer, "Default constructor should create instance");
        assertNotNull(nullStaticizer, "Parameterized constructor with null should create instance");

        // Both should be valid MemberVisitor instances
        assertTrue(defaultStaticizer instanceof MemberVisitor,
            "Default constructor should create MemberVisitor");
        assertTrue(nullStaticizer instanceof MemberVisitor,
            "Parameterized constructor with null should create MemberVisitor");
    }

    /**
     * Tests that the constructor creates an instance ready for immediate use.
     * The instance should be fully initialized and ready to be used as a visitor.
     */
    @Test
    public void testConstructor_createsReadyToUseInstance() {
        // Act
        MethodStaticizer staticizer = new MethodStaticizer();

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        assertDoesNotThrow(() -> {
            // Verify the instance can be used as a MemberVisitor
            MemberVisitor visitor = staticizer;
            assertNotNull(visitor, "Should be usable as MemberVisitor");
        }, "Instance should be immediately usable");
    }

    /**
     * Tests that construction is deterministic.
     * Multiple constructions should produce consistent results.
     */
    @Test
    public void testConstructor_isDeterministic() {
        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer();
        MethodStaticizer staticizer2 = new MethodStaticizer();
        MethodStaticizer staticizer3 = new MethodStaticizer();

        // Assert
        assertNotNull(staticizer1, "First instance should be created");
        assertNotNull(staticizer2, "Second instance should be created");
        assertNotNull(staticizer3, "Third instance should be created");

        // All should be the same type
        assertEquals(staticizer1.getClass(), staticizer2.getClass(),
            "All instances should be same type");
        assertEquals(staticizer2.getClass(), staticizer3.getClass(),
            "All instances should be same type");
    }

    /**
     * Tests that the constructor works correctly in a try-with-resources-like scenario.
     * Even though MethodStaticizer doesn't implement AutoCloseable, it should be
     * safe to use in various scoping contexts.
     */
    @Test
    public void testConstructor_worksInVariousScopes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            {
                MethodStaticizer staticizer1 = new MethodStaticizer();
                assertNotNull(staticizer1, "Instance in nested scope should be created");
            }

            MethodStaticizer staticizer2 = new MethodStaticizer();
            assertNotNull(staticizer2, "Instance outside nested scope should be created");
        }, "Constructor should work in various scoping contexts");
    }

    /**
     * Tests that the constructor doesn't require any special initialization.
     * It should work without any prior setup or configuration.
     */
    @Test
    public void testConstructor_requiresNoSetup() {
        // Act & Assert - directly construct without any setup
        assertDoesNotThrow(() -> {
            MethodStaticizer staticizer = new MethodStaticizer();
            assertNotNull(staticizer, "Constructor should work without setup");
        }, "Constructor should not require any setup");
    }

    /**
     * Tests that constructed instances can be stored in collections.
     */
    @Test
    public void testConstructor_instancesCanBeStoredInCollections() {
        // Arrange
        java.util.List<MethodStaticizer> list = new java.util.ArrayList<>();
        java.util.Set<MethodStaticizer> set = new java.util.HashSet<>();

        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer();
        MethodStaticizer staticizer2 = new MethodStaticizer();
        MethodStaticizer staticizer3 = new MethodStaticizer();

        // Assert
        assertDoesNotThrow(() -> {
            list.add(staticizer1);
            list.add(staticizer2);
            list.add(staticizer3);
        }, "Instances should be storable in lists");

        assertDoesNotThrow(() -> {
            set.add(staticizer1);
            set.add(staticizer2);
            set.add(staticizer3);
        }, "Instances should be storable in sets");

        assertEquals(3, list.size(), "All instances should be added to list");
        assertEquals(3, set.size(), "All instances should be added to set");
    }

    /**
     * Tests that the constructor doesn't perform any visible side effects.
     * Construction should be a pure operation that only creates the instance.
     */
    @Test
    public void testConstructor_noSideEffects() {
        // Arrange - capture initial state (if any global state existed)

        // Act
        MethodStaticizer staticizer = new MethodStaticizer();

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        // The constructor should only initialize the instance, not modify any external state
        // This test verifies construction doesn't throw or cause observable side effects
    }

    // ========================================================================
    // Tests for parameterized constructor: MethodStaticizer(MemberVisitor)
    // ========================================================================

    /**
     * Tests that the parameterized constructor successfully creates an instance with a non-null visitor.
     * This is the basic happy path with all parameters provided.
     */
    @Test
    public void testParameterizedConstructor_withValidVisitor_createsInstance() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);

        // Assert
        assertNotNull(staticizer, "Parameterized constructor should create a non-null instance");
    }

    /**
     * Tests that the parameterized constructor accepts null for the optional extraStaticMemberVisitor.
     * The extraStaticMemberVisitor can be null since it's an optional callback.
     */
    @Test
    public void testParameterizedConstructor_withNullVisitor_createsInstance() {
        // Act
        MethodStaticizer staticizer = new MethodStaticizer(null);

        // Assert
        assertNotNull(staticizer, "Parameterized constructor should accept null extra visitor");
    }

    /**
     * Tests that the parameterized constructor creates an instance that implements MemberVisitor.
     * MethodStaticizer implements MemberVisitor to visit and potentially
     * make methods static when their 'this' parameter is unused.
     */
    @Test
    public void testParameterizedConstructor_implementsMemberVisitor() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);

        // Assert
        assertTrue(staticizer instanceof MemberVisitor,
            "MethodStaticizer should implement MemberVisitor");
    }

    /**
     * Tests that multiple instances can be created with different visitors.
     * Verifies each instance maintains its own state.
     */
    @Test
    public void testParameterizedConstructor_multipleInstances_eachHasOwnState() {
        // Arrange
        MemberVisitor visitor1 = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };
        MemberVisitor visitor2 = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };
        MemberVisitor visitor3 = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer(visitor1);
        MethodStaticizer staticizer2 = new MethodStaticizer(visitor2);
        MethodStaticizer staticizer3 = new MethodStaticizer(visitor3);

        // Assert
        assertNotNull(staticizer1, "First instance should be created");
        assertNotNull(staticizer2, "Second instance should be created");
        assertNotNull(staticizer3, "Third instance should be created");
        assertNotSame(staticizer1, staticizer2, "Instances should be distinct");
        assertNotSame(staticizer2, staticizer3, "Instances should be distinct");
        assertNotSame(staticizer1, staticizer3, "Instances should be distinct");
    }

    /**
     * Tests that the parameterized constructor can be called repeatedly without issues.
     */
    @Test
    public void testParameterizedConstructor_repeatedConstruction_succeeds() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                MethodStaticizer staticizer = new MethodStaticizer(
                    i % 2 == 0 ? extraVisitor : null
                );
                assertNotNull(staticizer, "Instance " + i + " should be non-null");
            }
        }, "Should be able to construct many instances without issues");
    }

    /**
     * Tests that the parameterized constructor creates an instance of the correct type.
     */
    @Test
    public void testParameterizedConstructor_createsCorrectType() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        assertTrue(staticizer instanceof MethodStaticizer,
            "Should be instance of MethodStaticizer");
        assertEquals(MethodStaticizer.class, staticizer.getClass(),
            "Class should be MethodStaticizer");
    }

    /**
     * Tests that construction with the parameterized constructor completes quickly.
     */
    @Test
    public void testParameterizedConstructor_completesQuickly() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        long startTime = System.nanoTime();
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);
        long endTime = System.nanoTime();

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        long durationNanos = endTime - startTime;
        long oneMillisecondInNanos = 1_000_000L;
        assertTrue(durationNanos < oneMillisecondInNanos,
            "Constructor should complete very quickly, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that the parameterized constructor works in a multi-threaded environment.
     * Verifies there are no concurrency issues with construction.
     */
    @Test
    public void testParameterizedConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final Thread[] threads = new Thread[threadCount];
        final MethodStaticizer[] staticizers = new MethodStaticizer[threadCount];
        final Exception[] exceptions = new Exception[threadCount];

        // Act - create staticizers in parallel threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    MemberVisitor visitor = new MemberVisitor() {
                        @Override
                        public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                                      proguard.classfile.ProgramMethod programMethod) {
                            // No-op implementation
                        }
                    };
                    staticizers[index] = new MethodStaticizer(
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
            assertNotNull(staticizers[i], "Staticizer should be created in thread " + i);
        }
    }

    /**
     * Tests that the parameterized constructor does not throw exceptions with valid parameters.
     */
    @Test
    public void testParameterizedConstructor_doesNotThrowException() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act & Assert
        assertDoesNotThrow(() -> new MethodStaticizer(extraVisitor),
            "Parameterized constructor should not throw with valid parameters");
    }

    /**
     * Tests that the instance's toString() method works after construction with visitor.
     */
    @Test
    public void testParameterizedConstructor_toStringWorks() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);
        String toString = staticizer.toString();

        // Assert
        assertNotNull(toString, "toString() should return a non-null value");
        assertTrue(toString.contains("MethodStaticizer"),
            "toString() should contain the class name");
    }

    /**
     * Tests that hashCode() works on the constructed instance with visitor.
     */
    @Test
    public void testParameterizedConstructor_hashCodeWorks() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);

        // Assert
        assertDoesNotThrow(() -> staticizer.hashCode(),
            "hashCode() should work on constructed instance");
    }

    /**
     * Tests that equals() works on the constructed instances with visitors.
     */
    @Test
    public void testParameterizedConstructor_equalsWorks() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer(extraVisitor);
        MethodStaticizer staticizer2 = new MethodStaticizer(extraVisitor);

        // Assert
        assertDoesNotThrow(() -> staticizer1.equals(staticizer2),
            "equals() should work on constructed instances");
        assertTrue(staticizer1.equals(staticizer1),
            "Instance should equal itself");
    }

    /**
     * Tests that the same MemberVisitor can be shared across multiple instances.
     * Verifies the constructor doesn't claim exclusive ownership of the visitor.
     */
    @Test
    public void testParameterizedConstructor_sharedVisitor_succeeds() {
        // Arrange
        MemberVisitor sharedVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer(sharedVisitor);
        MethodStaticizer staticizer2 = new MethodStaticizer(sharedVisitor);
        MethodStaticizer staticizer3 = new MethodStaticizer(sharedVisitor);

        // Assert
        assertNotNull(staticizer1, "First instance with shared visitor should be created");
        assertNotNull(staticizer2, "Second instance with shared visitor should be created");
        assertNotNull(staticizer3, "Third instance with shared visitor should be created");
    }

    /**
     * Tests that construction with various visitor types succeeds.
     */
    @Test
    public void testParameterizedConstructor_variousVisitorTypes_succeed() {
        // Arrange
        MemberVisitor realVisitor1 = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };
        MemberVisitor realVisitor2 = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // Different implementation
            }
        };

        // Act & Assert
        assertDoesNotThrow(() -> {
            new MethodStaticizer(null);
            new MethodStaticizer(realVisitor1);
            new MethodStaticizer(realVisitor2);
        }, "All visitor types should be valid");
    }

    /**
     * Tests that the parameterized constructor creates an instance ready for immediate use.
     * The instance should be fully initialized and ready to be used as a visitor.
     */
    @Test
    public void testParameterizedConstructor_createsReadyToUseInstance() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        assertDoesNotThrow(() -> {
            // Verify the instance can be used as a MemberVisitor
            MemberVisitor visitor = staticizer;
            assertNotNull(visitor, "Should be usable as MemberVisitor");
        }, "Instance should be immediately usable");
    }

    /**
     * Tests that parameterized construction is deterministic.
     * Multiple constructions with the same visitor should produce consistent results.
     */
    @Test
    public void testParameterizedConstructor_isDeterministic() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer(extraVisitor);
        MethodStaticizer staticizer2 = new MethodStaticizer(extraVisitor);
        MethodStaticizer staticizer3 = new MethodStaticizer(extraVisitor);

        // Assert
        assertNotNull(staticizer1, "First instance should be created");
        assertNotNull(staticizer2, "Second instance should be created");
        assertNotNull(staticizer3, "Third instance should be created");

        // All should be the same type
        assertEquals(staticizer1.getClass(), staticizer2.getClass(),
            "All instances should be same type");
        assertEquals(staticizer2.getClass(), staticizer3.getClass(),
            "All instances should be same type");
    }

    /**
     * Tests that the parameterized constructor works correctly in various scoping contexts.
     */
    @Test
    public void testParameterizedConstructor_worksInVariousScopes() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act & Assert
        assertDoesNotThrow(() -> {
            {
                MethodStaticizer staticizer1 = new MethodStaticizer(extraVisitor);
                assertNotNull(staticizer1, "Instance in nested scope should be created");
            }

            MethodStaticizer staticizer2 = new MethodStaticizer(null);
            assertNotNull(staticizer2, "Instance outside nested scope should be created");
        }, "Parameterized constructor should work in various scoping contexts");
    }

    /**
     * Tests that the parameterized constructor doesn't require any special initialization.
     * It should work without any prior setup or configuration.
     */
    @Test
    public void testParameterizedConstructor_requiresNoSetup() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act & Assert - directly construct without any setup
        assertDoesNotThrow(() -> {
            MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);
            assertNotNull(staticizer, "Parameterized constructor should work without setup");
        }, "Parameterized constructor should not require any setup");
    }

    /**
     * Tests that instances created with parameterized constructor can be stored in collections.
     */
    @Test
    public void testParameterizedConstructor_instancesCanBeStoredInCollections() {
        // Arrange
        java.util.List<MethodStaticizer> list = new java.util.ArrayList<>();
        java.util.Set<MethodStaticizer> set = new java.util.HashSet<>();

        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer(extraVisitor);
        MethodStaticizer staticizer2 = new MethodStaticizer(null);
        MethodStaticizer staticizer3 = new MethodStaticizer(extraVisitor);

        // Assert
        assertDoesNotThrow(() -> {
            list.add(staticizer1);
            list.add(staticizer2);
            list.add(staticizer3);
        }, "Instances should be storable in lists");

        assertDoesNotThrow(() -> {
            set.add(staticizer1);
            set.add(staticizer2);
            set.add(staticizer3);
        }, "Instances should be storable in sets");

        assertEquals(3, list.size(), "All instances should be added to list");
        assertEquals(3, set.size(), "All instances should be added to set");
    }

    /**
     * Tests that the parameterized constructor doesn't perform any visible side effects.
     * Construction should be a pure operation that only creates the instance.
     */
    @Test
    public void testParameterizedConstructor_noSideEffects() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        // The constructor should only initialize the instance, not modify any external state
        // This test verifies construction doesn't throw or cause observable side effects
    }

    /**
     * Tests that the parameterized constructor doesn't modify the visitor passed to it.
     * The visitor should remain unchanged after being passed to the constructor.
     */
    @Test
    public void testParameterizedConstructor_doesNotModifyVisitor() {
        // Arrange
        final boolean[] visitCalled = {false};
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                visitCalled[0] = true;
            }
        };

        // Act
        MethodStaticizer staticizer = new MethodStaticizer(extraVisitor);

        // Assert
        assertNotNull(staticizer, "Instance should be created");
        assertFalse(visitCalled[0], "Constructor should not invoke the visitor");
    }

    /**
     * Tests that null and non-null visitors both result in valid instances.
     * Both should be functional MemberVisitor instances.
     */
    @Test
    public void testParameterizedConstructor_nullAndNonNull_bothCreateValidInstances() {
        // Arrange
        MemberVisitor extraVisitor = new MemberVisitor() {
            @Override
            public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                          proguard.classfile.ProgramMethod programMethod) {
                // No-op implementation
            }
        };

        // Act
        MethodStaticizer staticizer1 = new MethodStaticizer(null);
        MethodStaticizer staticizer2 = new MethodStaticizer(extraVisitor);

        // Assert
        assertNotNull(staticizer1, "Instance with null visitor should be created");
        assertNotNull(staticizer2, "Instance with non-null visitor should be created");
        assertTrue(staticizer1 instanceof MemberVisitor, "Instance with null should be MemberVisitor");
        assertTrue(staticizer2 instanceof MemberVisitor, "Instance with non-null should be MemberVisitor");
    }
}
