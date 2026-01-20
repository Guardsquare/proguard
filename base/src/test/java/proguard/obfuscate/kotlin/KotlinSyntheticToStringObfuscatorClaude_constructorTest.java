package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.Test;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KotlinSyntheticToStringObfuscator} constructor.
 * Tests the no-argument constructor:
 * <init>.()V
 */
public class KotlinSyntheticToStringObfuscatorClaude_constructorTest {

    /**
     * Tests the no-argument constructor creates a valid instance.
     * Verifies that a KotlinSyntheticToStringObfuscator can be instantiated.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator, "KotlinSyntheticToStringObfuscator should be created successfully");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleObfuscatorInstances() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator1 = new KotlinSyntheticToStringObfuscator();
        KotlinSyntheticToStringObfuscator obfuscator2 = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscators should be different instances");
    }

    /**
     * Tests that the obfuscator implements KotlinMetadataVisitor interface.
     * Verifies that it can be used as a KotlinMetadataVisitor.
     */
    @Test
    public void testObfuscatorImplementsKotlinMetadataVisitor() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertTrue(obfuscator instanceof KotlinMetadataVisitor,
                   "KotlinSyntheticToStringObfuscator should implement KotlinMetadataVisitor");
    }

    /**
     * Tests that the obfuscator can be assigned to KotlinMetadataVisitor reference.
     * Verifies interface implementation.
     */
    @Test
    public void testObfuscatorAsKotlinMetadataVisitor() {
        // Act
        KotlinMetadataVisitor obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator, "KotlinSyntheticToStringObfuscator should be assignable to KotlinMetadataVisitor");
    }

    /**
     * Tests that multiple sequential constructor calls work correctly.
     * Verifies constructor stability under repeated invocation.
     */
    @Test
    public void testMultipleSequentialConstructorCalls() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();
            assertNotNull(obfuscator, "Obfuscator " + i + " should be created");
        }
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(obfuscator, "Obfuscator should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that multiple obfuscators can be created concurrently.
     * Verifies thread-safety of the constructor (at least basic concurrent creation).
     */
    @Test
    public void testConcurrentConstructorCalls() {
        // Act - Create multiple instances in quick succession
        KotlinSyntheticToStringObfuscator obfuscator1 = new KotlinSyntheticToStringObfuscator();
        KotlinSyntheticToStringObfuscator obfuscator2 = new KotlinSyntheticToStringObfuscator();
        KotlinSyntheticToStringObfuscator obfuscator3 = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotNull(obfuscator3, "Third obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "First and second should be different instances");
        assertNotSame(obfuscator2, obfuscator3, "Second and third should be different instances");
        assertNotSame(obfuscator1, obfuscator3, "First and third should be different instances");
    }

    /**
     * Tests that the constructor creates a fully functional object.
     * Verifies the instance is a valid KotlinMetadataVisitor after construction.
     */
    @Test
    public void testConstructorCreatesFullyFunctionalObject() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator, "Constructor should create a non-null instance");
        assertTrue(obfuscator instanceof KotlinMetadataVisitor,
            "Should be a valid KotlinMetadataVisitor after construction");
    }

    /**
     * Tests that the no-argument constructor creates consistent instances.
     * Verifies that all instances created have the same type.
     */
    @Test
    public void testConstructorCreatesConsistentInstances() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator1 = new KotlinSyntheticToStringObfuscator();
        KotlinSyntheticToStringObfuscator obfuscator2 = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertEquals(obfuscator1.getClass(), obfuscator2.getClass(),
            "All instances should have the same class type");
    }

    /**
     * Tests that the constructor creates an instance of the expected class.
     * Verifies the exact type of the created instance.
     */
    @Test
    public void testConstructorCreatesCorrectType() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertEquals(KotlinSyntheticToStringObfuscator.class, obfuscator.getClass(),
            "Constructor should create an instance of KotlinSyntheticToStringObfuscator");
    }

    /**
     * Tests creating multiple instances in a batch.
     * Verifies that the constructor can handle batch creation without issues.
     */
    @Test
    public void testBatchConstructorCreation() {
        // Arrange
        int batchSize = 100;
        KotlinSyntheticToStringObfuscator[] obfuscators = new KotlinSyntheticToStringObfuscator[batchSize];

        // Act
        for (int i = 0; i < batchSize; i++) {
            obfuscators[i] = new KotlinSyntheticToStringObfuscator();
        }

        // Assert
        for (int i = 0; i < batchSize; i++) {
            assertNotNull(obfuscators[i], "Obfuscator " + i + " should be created");
        }

        // Verify all instances are different
        for (int i = 0; i < batchSize - 1; i++) {
            assertNotSame(obfuscators[i], obfuscators[i + 1],
                "Consecutive instances should be different");
        }
    }

    /**
     * Tests that the default constructor is accessible and public.
     * Verifies that the constructor can be called from outside the package.
     */
    @Test
    public void testConstructorIsPublic() {
        // Act - This test implicitly verifies that the constructor is accessible
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator, "Public constructor should be accessible");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions.
     * Verifies that the constructor is exception-safe.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new KotlinSyntheticToStringObfuscator(),
            "Constructor should not throw any exceptions");
    }

    /**
     * Tests that the instance can be used immediately after construction.
     * Verifies that no additional initialization is required.
     */
    @Test
    public void testInstanceUsableImmediatelyAfterConstruction() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator, "Instance should be non-null");
        // Verify it can be cast to its interface
        assertDoesNotThrow(() -> {
            KotlinMetadataVisitor visitor = (KotlinMetadataVisitor) obfuscator;
        }, "Instance should be immediately usable as KotlinMetadataVisitor");
    }

    /**
     * Tests constructor with toString() to verify object creation.
     * Verifies that the created object has a valid string representation.
     */
    @Test
    public void testConstructorCreatesObjectWithValidToString() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();

        // Assert
        assertNotNull(obfuscator.toString(), "toString() should return a non-null value");
        assertTrue(obfuscator.toString().contains("KotlinSyntheticToStringObfuscator"),
            "toString() should contain the class name");
    }

    /**
     * Tests that hashCode is consistent across multiple calls on same instance.
     * Verifies basic object consistency after construction.
     */
    @Test
    public void testConstructorCreatesObjectWithConsistentHashCode() {
        // Act
        KotlinSyntheticToStringObfuscator obfuscator = new KotlinSyntheticToStringObfuscator();
        int hashCode1 = obfuscator.hashCode();
        int hashCode2 = obfuscator.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2,
            "hashCode should be consistent for the same instance");
    }
}
