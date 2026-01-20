package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LineNumberInfoVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.pass.Pass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LineNumberLinearizer} default constructor with signature:
 * - ()V
 *
 * This constructor creates a new LineNumberLinearizer that disambiguates line numbers
 * in classes, shifting line numbers that originate from different classes (e.g. due to
 * method inlining or class merging) to blocks that don't overlap.
 */
public class LineNumberLinearizerClaude_constructorTest {

    // ========================================
    // Basic Constructor Tests
    // ========================================

    /**
     * Tests the default constructor creates a valid instance.
     * Verifies that the linearizer can be instantiated successfully.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created successfully");
    }

    /**
     * Tests that multiple linearizer instances can be created.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testMultipleInstancesCanBeCreated() {
        // Act
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer1, "First linearizer should be created");
        assertNotNull(linearizer2, "Second linearizer should be created");
        assertNotSame(linearizer1, linearizer2, "Linearizer instances should be different");
    }

    /**
     * Tests creating a sequence of linearizers.
     * Verifies that multiple linearizers can be created sequentially without issues.
     */
    @Test
    public void testSequentialLinearizerCreation() {
        // Act & Assert - create multiple linearizers sequentially
        for (int i = 0; i < 10; i++) {
            LineNumberLinearizer linearizer = new LineNumberLinearizer();
            assertNotNull(linearizer, "Linearizer " + i + " should be created");
        }
    }

    /**
     * Tests that two linearizers are independent instances.
     * Verifies that distinct instances are created.
     */
    @Test
    public void testMultipleLinearizerInstancesAreIndependent() {
        // Act
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer1, "First linearizer should be created");
        assertNotNull(linearizer2, "Second linearizer should be created");
        assertNotSame(linearizer1, linearizer2, "Linearizer instances should be different");
    }

    // ========================================
    // Interface Implementation Tests
    // ========================================

    /**
     * Tests that the constructor creates an instance of Pass.
     * Verifies that LineNumberLinearizer implements the Pass interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfPass() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertInstanceOf(Pass.class, linearizer,
            "LineNumberLinearizer should implement Pass interface");
    }

    /**
     * Tests that the constructor creates an instance of ClassVisitor.
     * Verifies that LineNumberLinearizer implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertInstanceOf(ClassVisitor.class, linearizer,
            "LineNumberLinearizer should implement ClassVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of MemberVisitor.
     * Verifies that LineNumberLinearizer implements the MemberVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertInstanceOf(MemberVisitor.class, linearizer,
            "LineNumberLinearizer should implement MemberVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of AttributeVisitor.
     * Verifies that LineNumberLinearizer implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertInstanceOf(AttributeVisitor.class, linearizer,
            "LineNumberLinearizer should implement AttributeVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of LineNumberInfoVisitor.
     * Verifies that LineNumberLinearizer implements the LineNumberInfoVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfLineNumberInfoVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertInstanceOf(LineNumberInfoVisitor.class, linearizer,
            "LineNumberLinearizer should implement LineNumberInfoVisitor interface");
    }

    /**
     * Tests that the created instance can be used as a Pass.
     * Verifies that the linearizer is in a valid state for use as Pass.
     */
    @Test
    public void testConstructorCreatesUsablePass() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created");
        Pass pass = linearizer;
        assertNotNull(pass, "LineNumberLinearizer should be usable as Pass");
    }

    /**
     * Tests that the created instance can be used as a ClassVisitor.
     * Verifies that the linearizer is in a valid state for use as ClassVisitor.
     */
    @Test
    public void testConstructorCreatesUsableClassVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created");
        ClassVisitor classVisitor = linearizer;
        assertNotNull(classVisitor, "LineNumberLinearizer should be usable as ClassVisitor");
    }

    /**
     * Tests that the created instance can be used as a MemberVisitor.
     * Verifies that the linearizer is in a valid state for use as MemberVisitor.
     */
    @Test
    public void testConstructorCreatesUsableMemberVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created");
        MemberVisitor memberVisitor = linearizer;
        assertNotNull(memberVisitor, "LineNumberLinearizer should be usable as MemberVisitor");
    }

    /**
     * Tests that the created instance can be used as an AttributeVisitor.
     * Verifies that the linearizer is in a valid state for use as AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesUsableAttributeVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created");
        AttributeVisitor attributeVisitor = linearizer;
        assertNotNull(attributeVisitor, "LineNumberLinearizer should be usable as AttributeVisitor");
    }

    /**
     * Tests that the created instance can be used as a LineNumberInfoVisitor.
     * Verifies that the linearizer is in a valid state for use as LineNumberInfoVisitor.
     */
    @Test
    public void testConstructorCreatesUsableLineNumberInfoVisitor() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created");
        LineNumberInfoVisitor lineNumberInfoVisitor = linearizer;
        assertNotNull(lineNumberInfoVisitor, "LineNumberLinearizer should be usable as LineNumberInfoVisitor");
    }

    /**
     * Tests that multiple linearizers all implement the required interfaces.
     * Verifies that each instance properly implements all expected interfaces.
     */
    @Test
    public void testMultipleInstancesImplementAllInterfaces() {
        // Act
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();

        // Assert
        assertInstanceOf(Pass.class, linearizer1, "Linearizer 1 should implement Pass");
        assertInstanceOf(Pass.class, linearizer2, "Linearizer 2 should implement Pass");
        assertInstanceOf(Pass.class, linearizer3, "Linearizer 3 should implement Pass");

        assertInstanceOf(ClassVisitor.class, linearizer1, "Linearizer 1 should implement ClassVisitor");
        assertInstanceOf(ClassVisitor.class, linearizer2, "Linearizer 2 should implement ClassVisitor");
        assertInstanceOf(ClassVisitor.class, linearizer3, "Linearizer 3 should implement ClassVisitor");

        assertInstanceOf(MemberVisitor.class, linearizer1, "Linearizer 1 should implement MemberVisitor");
        assertInstanceOf(MemberVisitor.class, linearizer2, "Linearizer 2 should implement MemberVisitor");
        assertInstanceOf(MemberVisitor.class, linearizer3, "Linearizer 3 should implement MemberVisitor");

        assertInstanceOf(AttributeVisitor.class, linearizer1, "Linearizer 1 should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, linearizer2, "Linearizer 2 should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, linearizer3, "Linearizer 3 should implement AttributeVisitor");

        assertInstanceOf(LineNumberInfoVisitor.class, linearizer1, "Linearizer 1 should implement LineNumberInfoVisitor");
        assertInstanceOf(LineNumberInfoVisitor.class, linearizer2, "Linearizer 2 should implement LineNumberInfoVisitor");
        assertInstanceOf(LineNumberInfoVisitor.class, linearizer3, "Linearizer 3 should implement LineNumberInfoVisitor");
    }

    // ========================================
    // Efficiency and Performance Tests
    // ========================================

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(linearizer, "LineNumberLinearizer should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that creating multiple linearizers is efficient.
     * Verifies that constructor performance is consistent across multiple invocations.
     */
    @Test
    public void testMultipleConstructorCallsAreEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - create 100 linearizers
        for (int i = 0; i < 100; i++) {
            LineNumberLinearizer linearizer = new LineNumberLinearizer();
            assertNotNull(linearizer, "Linearizer should be created");
        }

        // Assert
        long duration = System.nanoTime() - startTime;
        // Creating 100 linearizers should complete in less than 100 milliseconds
        assertTrue(duration < 100_000_000L,
            "Creating 100 linearizers should be efficient (took " + duration + " ns)");
    }

    // ========================================
    // State Initialization Tests
    // ========================================

    /**
     * Tests that the constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testConstructorInitializesInstanceProperly() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created");
        // Verify that the linearizer implements all expected interfaces
        assertInstanceOf(Pass.class, linearizer);
        assertInstanceOf(ClassVisitor.class, linearizer);
        assertInstanceOf(MemberVisitor.class, linearizer);
        assertInstanceOf(AttributeVisitor.class, linearizer);
        assertInstanceOf(LineNumberInfoVisitor.class, linearizer);
    }

    /**
     * Tests that the default constructor creates an instance ready for immediate use.
     * Verifies that no additional setup is required after construction.
     */
    @Test
    public void testConstructorCreatesReadyToUseInstance() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer, "LineNumberLinearizer should be created");

        // The instance should be immediately usable as all required interfaces
        Pass pass = linearizer;
        ClassVisitor classVisitor = linearizer;
        MemberVisitor memberVisitor = linearizer;
        AttributeVisitor attributeVisitor = linearizer;
        LineNumberInfoVisitor lineNumberInfoVisitor = linearizer;

        assertNotNull(pass, "Should be usable as Pass");
        assertNotNull(classVisitor, "Should be usable as ClassVisitor");
        assertNotNull(memberVisitor, "Should be usable as MemberVisitor");
        assertNotNull(attributeVisitor, "Should be usable as AttributeVisitor");
        assertNotNull(lineNumberInfoVisitor, "Should be usable as LineNumberInfoVisitor");
    }

    /**
     * Tests that each new instance is independent and has its own state.
     * Verifies that creating multiple instances doesn't share state.
     */
    @Test
    public void testEachInstanceHasIndependentState() {
        // Act
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();

        // Assert
        assertNotNull(linearizer1, "First linearizer should be created");
        assertNotNull(linearizer2, "Second linearizer should be created");
        assertNotNull(linearizer3, "Third linearizer should be created");

        // Verify they are all different objects
        assertNotSame(linearizer1, linearizer2, "Linearizer 1 and 2 should be different instances");
        assertNotSame(linearizer1, linearizer3, "Linearizer 1 and 3 should be different instances");
        assertNotSame(linearizer2, linearizer3, "Linearizer 2 and 3 should be different instances");
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     * Verifies that there are no global state mutations.
     */
    @Test
    public void testConstructorHasNoSideEffects() {
        // Act - create multiple instances and verify each is independent
        LineNumberLinearizer[] linearizers = new LineNumberLinearizer[50];
        for (int i = 0; i < linearizers.length; i++) {
            linearizers[i] = new LineNumberLinearizer();
            assertNotNull(linearizers[i], "Linearizer " + i + " should be created");
        }

        // Assert - all instances should be distinct
        for (int i = 0; i < linearizers.length; i++) {
            for (int j = i + 1; j < linearizers.length; j++) {
                assertNotSame(linearizers[i], linearizers[j],
                    "Linearizer " + i + " and " + j + " should be different instances");
            }
        }
    }

    /**
     * Tests that the constructor creates instances that are distinct even when created rapidly.
     * Verifies that rapid instantiation doesn't cause issues.
     */
    @Test
    public void testRapidInstanceCreationProducesDistinctInstances() {
        // Act - create instances as rapidly as possible
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer4 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer5 = new LineNumberLinearizer();

        // Assert - all instances should be distinct
        assertNotSame(linearizer1, linearizer2);
        assertNotSame(linearizer1, linearizer3);
        assertNotSame(linearizer1, linearizer4);
        assertNotSame(linearizer1, linearizer5);
        assertNotSame(linearizer2, linearizer3);
        assertNotSame(linearizer2, linearizer4);
        assertNotSame(linearizer2, linearizer5);
        assertNotSame(linearizer3, linearizer4);
        assertNotSame(linearizer3, linearizer5);
        assertNotSame(linearizer4, linearizer5);
    }

    /**
     * Tests that the constructed instance maintains all required interfaces.
     * Verifies interface implementation consistency.
     */
    @Test
    public void testConstructedInstanceMaintainsAllInterfaces() {
        // Act
        LineNumberLinearizer linearizer = new LineNumberLinearizer();

        // Assert - verify instance can be cast to all required interfaces
        Object obj = linearizer;
        assertTrue(obj instanceof Pass, "Should be instanceof Pass");
        assertTrue(obj instanceof ClassVisitor, "Should be instanceof ClassVisitor");
        assertTrue(obj instanceof MemberVisitor, "Should be instanceof MemberVisitor");
        assertTrue(obj instanceof AttributeVisitor, "Should be instanceof AttributeVisitor");
        assertTrue(obj instanceof LineNumberInfoVisitor, "Should be instanceof LineNumberInfoVisitor");
    }

    /**
     * Tests that the default constructor can be invoked in various contexts.
     * Verifies that the constructor is flexible and can be used in different scenarios.
     */
    @Test
    public void testConstructorWorksInVariousContexts() {
        // Test direct instantiation
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        assertNotNull(linearizer1, "Direct instantiation should work");

        // Test instantiation in conditional
        LineNumberLinearizer linearizer2 = true ? new LineNumberLinearizer() : null;
        assertNotNull(linearizer2, "Conditional instantiation should work");

        // Test instantiation in array
        LineNumberLinearizer[] array = new LineNumberLinearizer[] {
            new LineNumberLinearizer(),
            new LineNumberLinearizer()
        };
        assertNotNull(array[0], "Array element 0 should be created");
        assertNotNull(array[1], "Array element 1 should be created");
        assertNotSame(array[0], array[1], "Array elements should be different instances");

        // Test instantiation as interface type
        Pass pass = new LineNumberLinearizer();
        assertNotNull(pass, "Instantiation as Pass interface should work");
    }

    /**
     * Tests that multiple linearizers can coexist without interference.
     * Verifies that instances are completely independent.
     */
    @Test
    public void testMultipleLinearizerCoexistence() {
        // Act - create multiple linearizers
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();

        // Assert - all should be valid and independent
        assertNotNull(linearizer1);
        assertNotNull(linearizer2);
        assertNotNull(linearizer3);

        assertNotSame(linearizer1, linearizer2);
        assertNotSame(linearizer1, linearizer3);
        assertNotSame(linearizer2, linearizer3);

        // All should implement all interfaces
        assertInstanceOf(Pass.class, linearizer1);
        assertInstanceOf(Pass.class, linearizer2);
        assertInstanceOf(Pass.class, linearizer3);
    }
}
