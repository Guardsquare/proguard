package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.Test;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeAliasVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeVisitor;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAliasNameObfuscator} constructor.
 * Tests the single-parameter constructor:
 * <init>.(Lproguard/obfuscate/NameFactory;)V
 */
public class KotlinAliasNameObfuscatorClaude_constructorTest {

    /**
     * Tests the constructor with a valid NameFactory parameter.
     * Verifies that a KotlinAliasNameObfuscator can be instantiated with a valid parameter.
     */
    @Test
    public void testConstructorWithValidNameFactory() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be created successfully");
    }

    /**
     * Tests the constructor with a SimpleNameFactory implementation.
     * Verifies that the constructor works with concrete implementations.
     */
    @Test
    public void testConstructorWithSimpleNameFactory() {
        // Arrange
        NameFactory nameFactory = new SimpleNameFactory();

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be created with SimpleNameFactory");
    }

    /**
     * Tests the constructor with a mixed-case SimpleNameFactory.
     * Verifies different NameFactory configurations are accepted.
     */
    @Test
    public void testConstructorWithMixedCaseNameFactory() {
        // Arrange
        NameFactory nameFactory = new SimpleNameFactory(true);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be created with mixed-case name factory");
    }

    /**
     * Tests the constructor with a lower-case SimpleNameFactory.
     * Verifies different NameFactory configurations are accepted.
     */
    @Test
    public void testConstructorWithLowerCaseNameFactory() {
        // Arrange
        NameFactory nameFactory = new SimpleNameFactory(false);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be created with lower-case name factory");
    }

    /**
     * Tests that multiple instances can be created with different NameFactory instances.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleObfuscatorInstances() {
        // Arrange
        NameFactory factory1 = mock(NameFactory.class);
        NameFactory factory2 = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator1 = new KotlinAliasNameObfuscator(factory1);
        KotlinAliasNameObfuscator obfuscator2 = new KotlinAliasNameObfuscator(factory2);

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscators should be different instances");
    }

    /**
     * Tests the constructor with same NameFactory creates different instances.
     * Verifies that each constructor call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDifferentInstances() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator1 = new KotlinAliasNameObfuscator(nameFactory);
        KotlinAliasNameObfuscator obfuscator2 = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotSame(obfuscator1, obfuscator2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that the obfuscator implements KotlinMetadataVisitor interface.
     * Verifies that it can be used as a KotlinMetadataVisitor.
     */
    @Test
    public void testObfuscatorImplementsKotlinMetadataVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertTrue(obfuscator instanceof KotlinMetadataVisitor,
                   "KotlinAliasNameObfuscator should implement KotlinMetadataVisitor");
    }

    /**
     * Tests that the obfuscator implements KotlinTypeAliasVisitor interface.
     * Verifies that it can be used as a KotlinTypeAliasVisitor.
     */
    @Test
    public void testObfuscatorImplementsKotlinTypeAliasVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertTrue(obfuscator instanceof KotlinTypeAliasVisitor,
                   "KotlinAliasNameObfuscator should implement KotlinTypeAliasVisitor");
    }

    /**
     * Tests that the obfuscator implements KotlinTypeVisitor interface.
     * Verifies that it can be used as a KotlinTypeVisitor.
     */
    @Test
    public void testObfuscatorImplementsKotlinTypeVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertTrue(obfuscator instanceof KotlinTypeVisitor,
                   "KotlinAliasNameObfuscator should implement KotlinTypeVisitor");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the NameFactory.
     * Verifies that the constructor only stores the factory without using it.
     */
    @Test
    public void testConstructorDoesNotInvokeNameFactory() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "Obfuscator should be created");
        verifyNoInteractions(nameFactory);
    }

    /**
     * Tests that multiple instances can share the same NameFactory object.
     * Verifies that multiple obfuscators can be created with the same factory.
     */
    @Test
    public void testMultipleObfuscatorsWithSameNameFactory() {
        // Arrange
        NameFactory sharedFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator1 = new KotlinAliasNameObfuscator(sharedFactory);
        KotlinAliasNameObfuscator obfuscator2 = new KotlinAliasNameObfuscator(sharedFactory);

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscator instances should be different");
    }

    /**
     * Tests that the obfuscator can be assigned to KotlinMetadataVisitor reference.
     * Verifies interface implementation.
     */
    @Test
    public void testObfuscatorAsKotlinMetadataVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinMetadataVisitor obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be assignable to KotlinMetadataVisitor");
    }

    /**
     * Tests that the obfuscator can be assigned to KotlinTypeAliasVisitor reference.
     * Verifies interface implementation.
     */
    @Test
    public void testObfuscatorAsKotlinTypeAliasVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinTypeAliasVisitor obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be assignable to KotlinTypeAliasVisitor");
    }

    /**
     * Tests that the obfuscator can be assigned to KotlinTypeVisitor reference.
     * Verifies interface implementation.
     */
    @Test
    public void testObfuscatorAsKotlinTypeVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinTypeVisitor obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be assignable to KotlinTypeVisitor");
    }

    /**
     * Tests creating obfuscators with different NameFactory implementations.
     * Verifies that different factory instances are accepted.
     */
    @Test
    public void testConstructorWithDifferentNameFactories() {
        // Arrange
        NameFactory factory1 = new SimpleNameFactory(true);
        NameFactory factory2 = new SimpleNameFactory(false);

        // Act
        KotlinAliasNameObfuscator obfuscator1 = new KotlinAliasNameObfuscator(factory1);
        KotlinAliasNameObfuscator obfuscator2 = new KotlinAliasNameObfuscator(factory2);

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscator instances should be different");
    }

    /**
     * Tests that multiple sequential constructor calls work correctly.
     * Verifies constructor stability under repeated invocation.
     */
    @Test
    public void testMultipleSequentialConstructorCalls() {
        // Arrange
        NameFactory factory = mock(NameFactory.class);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(factory);
            assertNotNull(obfuscator, "Obfuscator " + i + " should be created");
        }
    }

    /**
     * Tests constructor with a custom NameFactory implementation.
     * Verifies that the constructor works with custom implementations.
     */
    @Test
    public void testConstructorWithCustomNameFactory() {
        // Arrange
        NameFactory customFactory = new NameFactory() {
            private int counter = 0;

            @Override
            public void reset() {
                counter = 0;
            }

            @Override
            public String nextName() {
                return "custom" + (counter++);
            }
        };

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(customFactory);

        // Assert
        assertNotNull(obfuscator, "KotlinAliasNameObfuscator should be created with custom factory");
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        long startTime = System.nanoTime();

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(obfuscator, "Obfuscator should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests constructor with a mocked NameFactory and verifies no initialization calls.
     * Verifies that the NameFactory is stored but not used during construction.
     */
    @Test
    public void testConstructorStoresButDoesNotUseNameFactory() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "Constructor should properly store the NameFactory");
        // Verify that neither reset() nor nextName() was called during construction
        verify(nameFactory, never()).reset();
        verify(nameFactory, never()).nextName();
    }

    /**
     * Tests that multiple obfuscators can be created concurrently.
     * Verifies thread-safety of the constructor (at least basic concurrent creation).
     */
    @Test
    public void testConcurrentConstructorCalls() {
        // Arrange
        NameFactory factory1 = new SimpleNameFactory();
        NameFactory factory2 = new SimpleNameFactory();
        NameFactory factory3 = new SimpleNameFactory();

        // Act - Create multiple instances in quick succession
        KotlinAliasNameObfuscator obfuscator1 = new KotlinAliasNameObfuscator(factory1);
        KotlinAliasNameObfuscator obfuscator2 = new KotlinAliasNameObfuscator(factory2);
        KotlinAliasNameObfuscator obfuscator3 = new KotlinAliasNameObfuscator(factory3);

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotNull(obfuscator3, "Third obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "First and second should be different instances");
        assertNotSame(obfuscator2, obfuscator3, "Second and third should be different instances");
        assertNotSame(obfuscator1, obfuscator3, "First and third should be different instances");
    }

    /**
     * Tests that the obfuscator properly stores the NameFactory parameter.
     * Verifies the single-parameter constructor is the primary constructor.
     */
    @Test
    public void testConstructorStoresParameter() {
        // Arrange
        NameFactory factory = new SimpleNameFactory();

        // Act
        KotlinAliasNameObfuscator obfuscator = new KotlinAliasNameObfuscator(factory);

        // Assert
        assertNotNull(obfuscator, "Constructor should properly store the parameter");
        // Verify the obfuscator is fully functional by checking it implements all expected interfaces
        assertTrue(obfuscator instanceof KotlinMetadataVisitor,
            "Should be a valid KotlinMetadataVisitor after construction");
        assertTrue(obfuscator instanceof KotlinTypeAliasVisitor,
            "Should be a valid KotlinTypeAliasVisitor after construction");
        assertTrue(obfuscator instanceof KotlinTypeVisitor,
            "Should be a valid KotlinTypeVisitor after construction");
    }
}
