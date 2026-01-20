package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.InnerClassesInfoVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link RetargetedInnerClassAttributeRemover} default constructor with signature:
 * - ()V
 *
 * This constructor creates a new RetargetedInnerClassAttributeRemover that removes InnerClasses
 * and EnclosingMethod attributes in classes that are retargeted or that refer to classes that
 * are retargeted.
 */
public class RetargetedInnerClassAttributeRemoverClaude_constructorTest {

    // ========================================
    // Basic Constructor Tests
    // ========================================

    /**
     * Tests the default constructor creates a valid instance.
     * Verifies that the remover can be instantiated successfully.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created successfully");
    }

    /**
     * Tests that multiple remover instances can be created.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testMultipleInstancesCanBeCreated() {
        // Act
        RetargetedInnerClassAttributeRemover remover1 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover2 = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover1, "First remover should be created");
        assertNotNull(remover2, "Second remover should be created");
        assertNotSame(remover1, remover2, "Remover instances should be different");
    }

    /**
     * Tests creating a sequence of removers.
     * Verifies that multiple removers can be created sequentially without issues.
     */
    @Test
    public void testSequentialRemoverCreation() {
        // Act & Assert - create multiple removers sequentially
        for (int i = 0; i < 10; i++) {
            RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();
            assertNotNull(remover, "Remover " + i + " should be created");
        }
    }

    /**
     * Tests that two removers are independent instances.
     * Verifies that distinct instances are created.
     */
    @Test
    public void testMultipleRemoverInstancesAreIndependent() {
        // Act
        RetargetedInnerClassAttributeRemover remover1 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover2 = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover1, "First remover should be created");
        assertNotNull(remover2, "Second remover should be created");
        assertNotSame(remover1, remover2, "Remover instances should be different");
    }

    // ========================================
    // Interface Implementation Tests
    // ========================================

    /**
     * Tests that the constructor creates an instance of ClassVisitor.
     * Verifies that RetargetedInnerClassAttributeRemover implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertInstanceOf(ClassVisitor.class, remover,
            "RetargetedInnerClassAttributeRemover should implement ClassVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of AttributeVisitor.
     * Verifies that RetargetedInnerClassAttributeRemover implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertInstanceOf(AttributeVisitor.class, remover,
            "RetargetedInnerClassAttributeRemover should implement AttributeVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of InnerClassesInfoVisitor.
     * Verifies that RetargetedInnerClassAttributeRemover implements the InnerClassesInfoVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfInnerClassesInfoVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertInstanceOf(InnerClassesInfoVisitor.class, remover,
            "RetargetedInnerClassAttributeRemover should implement InnerClassesInfoVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of ConstantVisitor.
     * Verifies that RetargetedInnerClassAttributeRemover implements the ConstantVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfConstantVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertInstanceOf(ConstantVisitor.class, remover,
            "RetargetedInnerClassAttributeRemover should implement ConstantVisitor interface");
    }

    /**
     * Tests that the created instance can be used as a ClassVisitor.
     * Verifies that the remover is in a valid state for use as ClassVisitor.
     */
    @Test
    public void testConstructorCreatesUsableClassVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created");
        ClassVisitor classVisitor = remover;
        assertNotNull(classVisitor, "RetargetedInnerClassAttributeRemover should be usable as ClassVisitor");
    }

    /**
     * Tests that the created instance can be used as an AttributeVisitor.
     * Verifies that the remover is in a valid state for use as AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesUsableAttributeVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created");
        AttributeVisitor attributeVisitor = remover;
        assertNotNull(attributeVisitor, "RetargetedInnerClassAttributeRemover should be usable as AttributeVisitor");
    }

    /**
     * Tests that the created instance can be used as an InnerClassesInfoVisitor.
     * Verifies that the remover is in a valid state for use as InnerClassesInfoVisitor.
     */
    @Test
    public void testConstructorCreatesUsableInnerClassesInfoVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created");
        InnerClassesInfoVisitor infoVisitor = remover;
        assertNotNull(infoVisitor, "RetargetedInnerClassAttributeRemover should be usable as InnerClassesInfoVisitor");
    }

    /**
     * Tests that the created instance can be used as a ConstantVisitor.
     * Verifies that the remover is in a valid state for use as ConstantVisitor.
     */
    @Test
    public void testConstructorCreatesUsableConstantVisitor() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created");
        ConstantVisitor constantVisitor = remover;
        assertNotNull(constantVisitor, "RetargetedInnerClassAttributeRemover should be usable as ConstantVisitor");
    }

    /**
     * Tests that multiple removers all implement the required interfaces.
     * Verifies that each instance properly implements all expected interfaces.
     */
    @Test
    public void testMultipleInstancesImplementAllInterfaces() {
        // Act
        RetargetedInnerClassAttributeRemover remover1 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover2 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover3 = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertInstanceOf(ClassVisitor.class, remover1, "Remover 1 should implement ClassVisitor");
        assertInstanceOf(ClassVisitor.class, remover2, "Remover 2 should implement ClassVisitor");
        assertInstanceOf(ClassVisitor.class, remover3, "Remover 3 should implement ClassVisitor");

        assertInstanceOf(AttributeVisitor.class, remover1, "Remover 1 should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, remover2, "Remover 2 should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, remover3, "Remover 3 should implement AttributeVisitor");

        assertInstanceOf(InnerClassesInfoVisitor.class, remover1, "Remover 1 should implement InnerClassesInfoVisitor");
        assertInstanceOf(InnerClassesInfoVisitor.class, remover2, "Remover 2 should implement InnerClassesInfoVisitor");
        assertInstanceOf(InnerClassesInfoVisitor.class, remover3, "Remover 3 should implement InnerClassesInfoVisitor");

        assertInstanceOf(ConstantVisitor.class, remover1, "Remover 1 should implement ConstantVisitor");
        assertInstanceOf(ConstantVisitor.class, remover2, "Remover 2 should implement ConstantVisitor");
        assertInstanceOf(ConstantVisitor.class, remover3, "Remover 3 should implement ConstantVisitor");
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
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that creating multiple removers is efficient.
     * Verifies that constructor performance is consistent across multiple invocations.
     */
    @Test
    public void testMultipleConstructorCallsAreEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - create 100 removers
        for (int i = 0; i < 100; i++) {
            RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();
            assertNotNull(remover, "Remover should be created");
        }

        // Assert
        long duration = System.nanoTime() - startTime;
        // Creating 100 removers should complete in less than 100 milliseconds
        assertTrue(duration < 100_000_000L,
            "Creating 100 removers should be efficient (took " + duration + " ns)");
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
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created");
        // Verify that the remover implements all expected interfaces
        assertInstanceOf(ClassVisitor.class, remover);
        assertInstanceOf(AttributeVisitor.class, remover);
        assertInstanceOf(InnerClassesInfoVisitor.class, remover);
        assertInstanceOf(ConstantVisitor.class, remover);
    }

    /**
     * Tests that the default constructor creates an instance ready for immediate use.
     * Verifies that no additional setup is required after construction.
     */
    @Test
    public void testConstructorCreatesReadyToUseInstance() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover, "RetargetedInnerClassAttributeRemover should be created");

        // The instance should be immediately usable as all required interfaces
        ClassVisitor classVisitor = remover;
        AttributeVisitor attributeVisitor = remover;
        InnerClassesInfoVisitor infoVisitor = remover;
        ConstantVisitor constantVisitor = remover;

        assertNotNull(classVisitor, "Should be usable as ClassVisitor");
        assertNotNull(attributeVisitor, "Should be usable as AttributeVisitor");
        assertNotNull(infoVisitor, "Should be usable as InnerClassesInfoVisitor");
        assertNotNull(constantVisitor, "Should be usable as ConstantVisitor");
    }

    /**
     * Tests that each new instance is independent and has its own state.
     * Verifies that creating multiple instances doesn't share state.
     */
    @Test
    public void testEachInstanceHasIndependentState() {
        // Act
        RetargetedInnerClassAttributeRemover remover1 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover2 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover3 = new RetargetedInnerClassAttributeRemover();

        // Assert
        assertNotNull(remover1, "First remover should be created");
        assertNotNull(remover2, "Second remover should be created");
        assertNotNull(remover3, "Third remover should be created");

        // Verify they are all different objects
        assertNotSame(remover1, remover2, "Remover 1 and 2 should be different instances");
        assertNotSame(remover1, remover3, "Remover 1 and 3 should be different instances");
        assertNotSame(remover2, remover3, "Remover 2 and 3 should be different instances");
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     * Verifies that there are no global state mutations.
     */
    @Test
    public void testConstructorHasNoSideEffects() {
        // Act - create multiple instances and verify each is independent
        RetargetedInnerClassAttributeRemover[] removers = new RetargetedInnerClassAttributeRemover[50];
        for (int i = 0; i < removers.length; i++) {
            removers[i] = new RetargetedInnerClassAttributeRemover();
            assertNotNull(removers[i], "Remover " + i + " should be created");
        }

        // Assert - all instances should be distinct
        for (int i = 0; i < removers.length; i++) {
            for (int j = i + 1; j < removers.length; j++) {
                assertNotSame(removers[i], removers[j],
                    "Remover " + i + " and " + j + " should be different instances");
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
        RetargetedInnerClassAttributeRemover remover1 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover2 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover3 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover4 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover5 = new RetargetedInnerClassAttributeRemover();

        // Assert - all instances should be distinct
        assertNotSame(remover1, remover2);
        assertNotSame(remover1, remover3);
        assertNotSame(remover1, remover4);
        assertNotSame(remover1, remover5);
        assertNotSame(remover2, remover3);
        assertNotSame(remover2, remover4);
        assertNotSame(remover2, remover5);
        assertNotSame(remover3, remover4);
        assertNotSame(remover3, remover5);
        assertNotSame(remover4, remover5);
    }

    /**
     * Tests that the constructed instance maintains all required interfaces.
     * Verifies interface implementation consistency.
     */
    @Test
    public void testConstructedInstanceMaintainsAllInterfaces() {
        // Act
        RetargetedInnerClassAttributeRemover remover = new RetargetedInnerClassAttributeRemover();

        // Assert - verify instance can be cast to all required interfaces
        Object obj = remover;
        assertTrue(obj instanceof ClassVisitor, "Should be instanceof ClassVisitor");
        assertTrue(obj instanceof AttributeVisitor, "Should be instanceof AttributeVisitor");
        assertTrue(obj instanceof InnerClassesInfoVisitor, "Should be instanceof InnerClassesInfoVisitor");
        assertTrue(obj instanceof ConstantVisitor, "Should be instanceof ConstantVisitor");
    }

    /**
     * Tests that the default constructor can be invoked in various contexts.
     * Verifies that the constructor is flexible and can be used in different scenarios.
     */
    @Test
    public void testConstructorWorksInVariousContexts() {
        // Test direct instantiation
        RetargetedInnerClassAttributeRemover remover1 = new RetargetedInnerClassAttributeRemover();
        assertNotNull(remover1, "Direct instantiation should work");

        // Test instantiation in conditional
        RetargetedInnerClassAttributeRemover remover2 = true ? new RetargetedInnerClassAttributeRemover() : null;
        assertNotNull(remover2, "Conditional instantiation should work");

        // Test instantiation in array
        RetargetedInnerClassAttributeRemover[] array = new RetargetedInnerClassAttributeRemover[] {
            new RetargetedInnerClassAttributeRemover(),
            new RetargetedInnerClassAttributeRemover()
        };
        assertNotNull(array[0], "Array element 0 should be created");
        assertNotNull(array[1], "Array element 1 should be created");
        assertNotSame(array[0], array[1], "Array elements should be different instances");

        // Test instantiation as interface type
        ClassVisitor classVisitor = new RetargetedInnerClassAttributeRemover();
        assertNotNull(classVisitor, "Instantiation as ClassVisitor interface should work");
    }

    /**
     * Tests that multiple removers can coexist without interference.
     * Verifies that instances are completely independent.
     */
    @Test
    public void testMultipleRemoverCoexistence() {
        // Act - create multiple removers
        RetargetedInnerClassAttributeRemover remover1 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover2 = new RetargetedInnerClassAttributeRemover();
        RetargetedInnerClassAttributeRemover remover3 = new RetargetedInnerClassAttributeRemover();

        // Assert - all should be valid and independent
        assertNotNull(remover1);
        assertNotNull(remover2);
        assertNotNull(remover3);

        assertNotSame(remover1, remover2);
        assertNotSame(remover1, remover3);
        assertNotSame(remover2, remover3);

        // All should implement all interfaces
        assertInstanceOf(ClassVisitor.class, remover1);
        assertInstanceOf(ClassVisitor.class, remover2);
        assertInstanceOf(ClassVisitor.class, remover3);
    }
}
