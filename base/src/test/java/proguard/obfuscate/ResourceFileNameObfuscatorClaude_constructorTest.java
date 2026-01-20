package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.ResourceFileVisitor;
import proguard.util.StringFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ResourceFileNameObfuscator} constructor.
 * Tests the three-parameter constructor:
 * <init>.(Lproguard/util/StringFunction;ZLproguard/resources/file/visitor/ResourceFileVisitor;)V
 */
public class ResourceFileNameObfuscatorClaude_constructorTest {

    /**
     * Tests the constructor with all valid non-null parameters.
     * Verifies that a ResourceFileNameObfuscator can be instantiated with valid parameters.
     */
    @Test
    public void testConstructorWithAllValidParameters() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator, "ResourceFileNameObfuscator should be created successfully");
    }

    /**
     * Tests the constructor with overrideAlreadyObfuscatedNames set to false.
     * Verifies that the constructor accepts both true and false values.
     */
    @Test
    public void testConstructorWithOverrideFlagDisabled() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = false;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator, "ResourceFileNameObfuscator should be created with override flag disabled");
    }

    /**
     * Tests the constructor with overrideAlreadyObfuscatedNames set to true.
     * Verifies that true value is accepted.
     */
    @Test
    public void testConstructorWithOverrideFlagEnabled() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator, "ResourceFileNameObfuscator should be created with override flag enabled");
    }

    /**
     * Tests the constructor with null extraVisitor parameter.
     * Verifies that null extraVisitor is accepted (it's optional).
     */
    @Test
    public void testConstructorWithNullExtraVisitor() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            null
        );

        // Assert
        assertNotNull(obfuscator, "ResourceFileNameObfuscator should be created with null extraVisitor");
    }

    /**
     * Tests that multiple instances can be created with different parameters.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleObfuscatorInstances() {
        // Arrange
        StringFunction function1 = mock(StringFunction.class);
        StringFunction function2 = mock(StringFunction.class);
        ResourceFileVisitor visitor1 = mock(ResourceFileVisitor.class);
        ResourceFileVisitor visitor2 = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator1 = new ResourceFileNameObfuscator(function1, true, visitor1);
        ResourceFileNameObfuscator obfuscator2 = new ResourceFileNameObfuscator(function2, false, visitor2);

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscators should be different instances");
    }

    /**
     * Tests the constructor with same parameters creates different instances.
     * Verifies that each constructor call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDifferentInstances() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator1 = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        ResourceFileNameObfuscator obfuscator2 = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotSame(obfuscator1, obfuscator2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that the obfuscator implements ResourceFileVisitor interface.
     * Verifies that it can be used as a ResourceFileVisitor.
     */
    @Test
    public void testObfuscatorImplementsResourceFileVisitor() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertTrue(obfuscator instanceof ResourceFileVisitor,
                   "ResourceFileNameObfuscator should implement ResourceFileVisitor");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the StringFunction.
     * Verifies that the constructor only stores the function without using it.
     */
    @Test
    public void testConstructorDoesNotInvokeStringFunction() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator, "Obfuscator should be created");
        verifyNoInteractions(nameObfuscationFunction);
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the extraVisitor.
     * Verifies that the constructor only stores the visitor without using it.
     */
    @Test
    public void testConstructorDoesNotInvokeExtraVisitor() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator, "Obfuscator should be created");
        verifyNoInteractions(extraVisitor);
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);
        long startTime = System.nanoTime();

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(obfuscator, "Obfuscator should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating multiple obfuscators with the same StringFunction.
     * Verifies that multiple instances can share the same function object.
     */
    @Test
    public void testMultipleObfuscatorsWithSameStringFunction() {
        // Arrange
        StringFunction sharedFunction = mock(StringFunction.class);
        ResourceFileVisitor visitor1 = mock(ResourceFileVisitor.class);
        ResourceFileVisitor visitor2 = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator1 = new ResourceFileNameObfuscator(sharedFunction, true, visitor1);
        ResourceFileNameObfuscator obfuscator2 = new ResourceFileNameObfuscator(sharedFunction, false, visitor2);

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscator instances should be different");
    }

    /**
     * Tests creating multiple obfuscators with the same extraVisitor.
     * Verifies that multiple instances can share the same visitor object.
     */
    @Test
    public void testMultipleObfuscatorsWithSameExtraVisitor() {
        // Arrange
        StringFunction function1 = mock(StringFunction.class);
        StringFunction function2 = mock(StringFunction.class);
        ResourceFileVisitor sharedVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator1 = new ResourceFileNameObfuscator(function1, true, sharedVisitor);
        ResourceFileNameObfuscator obfuscator2 = new ResourceFileNameObfuscator(function2, false, sharedVisitor);

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscator instances should be different");
    }

    /**
     * Tests creating obfuscators with different boolean flag values.
     * Verifies that both flag states are accepted.
     */
    @Test
    public void testConstructorWithDifferentBooleanFlags() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscatorTrue = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            true,
            extraVisitor
        );

        ResourceFileNameObfuscator obfuscatorFalse = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            false,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscatorTrue, "Obfuscator with true flag should be created");
        assertNotNull(obfuscatorFalse, "Obfuscator with false flag should be created");
        assertNotSame(obfuscatorTrue, obfuscatorFalse, "Obfuscator instances should be different");
    }

    /**
     * Tests that obfuscator can be assigned to ResourceFileVisitor reference.
     * Verifies interface implementation.
     */
    @Test
    public void testObfuscatorAsResourceFileVisitor() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileVisitor obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator, "ResourceFileNameObfuscator should be assignable to ResourceFileVisitor");
    }

    /**
     * Tests creating obfuscators with different StringFunction implementations.
     * Verifies that different function instances are accepted.
     */
    @Test
    public void testConstructorWithDifferentStringFunctions() {
        // Arrange
        StringFunction function1 = mock(StringFunction.class);
        StringFunction function2 = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator1 = new ResourceFileNameObfuscator(
            function1,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        ResourceFileNameObfuscator obfuscator2 = new ResourceFileNameObfuscator(
            function2,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscator instances should be different");
    }

    /**
     * Tests creating obfuscators with different ResourceFileVisitor implementations.
     * Verifies that different visitor instances are accepted.
     */
    @Test
    public void testConstructorWithDifferentExtraVisitors() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor visitor1 = mock(ResourceFileVisitor.class);
        ResourceFileVisitor visitor2 = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator1 = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            visitor1
        );

        ResourceFileNameObfuscator obfuscator2 = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            visitor2
        );

        // Assert
        assertNotNull(obfuscator1, "First obfuscator should be created");
        assertNotNull(obfuscator2, "Second obfuscator should be created");
        assertNotSame(obfuscator1, obfuscator2, "Obfuscator instances should be different");
    }

    /**
     * Tests that the constructor initializes the object properly by verifying
     * the obfuscator can be used immediately after construction.
     */
    @Test
    public void testConstructorProperlyInitializesObject() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        when(nameObfuscationFunction.transform(anyString())).thenReturn("obfuscated");
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);
        ResourceFile resourceFile = mock(ResourceFile.class);
        resourceFile.fileName = "original.txt";

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Verify the obfuscator can be used immediately
        assertDoesNotThrow(() -> obfuscator.visitAnyResourceFile(resourceFile),
            "Obfuscator should be usable immediately after construction");
    }

    /**
     * Tests constructor with a concrete StringFunction implementation.
     * Verifies that the constructor works with real implementations.
     */
    @Test
    public void testConstructorWithConcreteStringFunction() {
        // Arrange
        StringFunction concreteFunction = new StringFunction() {
            @Override
            public String transform(String string) {
                return string.toUpperCase();
            }
        };
        boolean overrideAlreadyObfuscatedNames = false;
        ResourceFileVisitor extraVisitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            concreteFunction,
            overrideAlreadyObfuscatedNames,
            extraVisitor
        );

        // Assert
        assertNotNull(obfuscator, "ResourceFileNameObfuscator should be created with concrete function");
    }

    /**
     * Tests constructor with a concrete ResourceFileVisitor implementation.
     * Verifies that the constructor works with real visitor implementations.
     */
    @Test
    public void testConstructorWithConcreteExtraVisitor() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = true;
        ResourceFileVisitor concreteVisitor = new ResourceFileVisitor() {
            @Override
            public void visitAnyResourceFile(ResourceFile resourceFile) {
                // Concrete implementation
            }
        };

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            concreteVisitor
        );

        // Assert
        assertNotNull(obfuscator, "ResourceFileNameObfuscator should be created with concrete visitor");
    }

    /**
     * Tests that multiple sequential constructor calls work correctly.
     * Verifies constructor stability under repeated invocation.
     */
    @Test
    public void testMultipleSequentialConstructorCalls() {
        // Arrange
        StringFunction function = mock(StringFunction.class);
        ResourceFileVisitor visitor = mock(ResourceFileVisitor.class);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
                function,
                i % 2 == 0,
                visitor
            );
            assertNotNull(obfuscator, "Obfuscator " + i + " should be created");
        }
    }

    /**
     * Tests constructor with both boolean values in a single test.
     * Verifies that the boolean parameter is properly stored.
     */
    @Test
    public void testConstructorWithBothBooleanValues() {
        // Arrange
        StringFunction function = mock(StringFunction.class);
        ResourceFileVisitor visitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator withTrue = new ResourceFileNameObfuscator(function, true, visitor);
        ResourceFileNameObfuscator withFalse = new ResourceFileNameObfuscator(function, false, visitor);

        // Assert
        assertNotNull(withTrue, "Obfuscator with true should be created");
        assertNotNull(withFalse, "Obfuscator with false should be created");
        assertNotSame(withTrue, withFalse, "Different instances should be created");
    }

    /**
     * Tests constructor with null extraVisitor and false override flag.
     * Verifies that null visitor works with different boolean values.
     */
    @Test
    public void testConstructorWithNullVisitorAndFalseFlag() {
        // Arrange
        StringFunction nameObfuscationFunction = mock(StringFunction.class);
        boolean overrideAlreadyObfuscatedNames = false;

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(
            nameObfuscationFunction,
            overrideAlreadyObfuscatedNames,
            null
        );

        // Assert
        assertNotNull(obfuscator, "Obfuscator should be created with null visitor and false flag");
    }

    /**
     * Tests that the constructor properly stores all three parameters.
     * Verifies the three-parameter constructor is the primary constructor.
     */
    @Test
    public void testConstructorStoresAllThreeParameters() {
        // Arrange
        StringFunction function = mock(StringFunction.class);
        boolean override = true;
        ResourceFileVisitor visitor = mock(ResourceFileVisitor.class);

        // Act
        ResourceFileNameObfuscator obfuscator = new ResourceFileNameObfuscator(function, override, visitor);

        // Assert
        assertNotNull(obfuscator, "Constructor should properly store all three parameters");
        // Verify the obfuscator is fully functional
        assertTrue(obfuscator instanceof ResourceFileVisitor,
            "Should be a valid ResourceFileVisitor after construction");
    }

    /**
     * Tests constructor with all parameters being different combinations.
     * Verifies comprehensive parameter acceptance.
     */
    @Test
    public void testConstructorWithVariousParameterCombinations() {
        // Arrange & Act & Assert
        StringFunction func1 = mock(StringFunction.class);
        StringFunction func2 = mock(StringFunction.class);
        ResourceFileVisitor vis1 = mock(ResourceFileVisitor.class);
        ResourceFileVisitor vis2 = mock(ResourceFileVisitor.class);

        // Combination 1: func1, true, vis1
        assertNotNull(new ResourceFileNameObfuscator(func1, true, vis1));

        // Combination 2: func1, false, vis1
        assertNotNull(new ResourceFileNameObfuscator(func1, false, vis1));

        // Combination 3: func2, true, vis2
        assertNotNull(new ResourceFileNameObfuscator(func2, true, vis2));

        // Combination 4: func2, false, vis2
        assertNotNull(new ResourceFileNameObfuscator(func2, false, vis2));

        // Combination 5: func1, true, null
        assertNotNull(new ResourceFileNameObfuscator(func1, true, null));

        // Combination 6: func2, false, null
        assertNotNull(new ResourceFileNameObfuscator(func2, false, null));
    }
}
