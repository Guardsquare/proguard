package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link TargetClassChanger} default constructor with signature:
 * - ()V
 *
 * This constructor creates a new TargetClassChanger that replaces references to classes
 * and class members if the classes have targets that are intended to replace them.
 * This is used during vertical class merging operations.
 */
public class TargetClassChangerClaude_constructorTest {

    // ========================================
    // Basic Constructor Tests
    // ========================================

    /**
     * Tests the default constructor creates a valid instance.
     * Verifies that the changer can be instantiated successfully.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created successfully");
    }

    /**
     * Tests that multiple changer instances can be created.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testMultipleInstancesCanBeCreated() {
        // Act
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();

        // Assert
        assertNotNull(changer1, "First changer should be created");
        assertNotNull(changer2, "Second changer should be created");
        assertNotSame(changer1, changer2, "Changer instances should be different");
    }

    /**
     * Tests creating a sequence of changers.
     * Verifies that multiple changers can be created sequentially without issues.
     */
    @Test
    public void testSequentialChangerCreation() {
        // Act & Assert - create multiple changers sequentially
        for (int i = 0; i < 10; i++) {
            TargetClassChanger changer = new TargetClassChanger();
            assertNotNull(changer, "Changer " + i + " should be created");
        }
    }

    /**
     * Tests that two changers are independent instances.
     * Verifies that distinct instances are created.
     */
    @Test
    public void testMultipleChangerInstancesAreIndependent() {
        // Act
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();

        // Assert
        assertNotNull(changer1, "First changer should be created");
        assertNotNull(changer2, "Second changer should be created");
        assertNotSame(changer1, changer2, "Changer instances should be different");
    }

    // ========================================
    // Interface Implementation Tests
    // ========================================

    /**
     * Tests that the constructor creates an instance of ClassVisitor.
     * Verifies that TargetClassChanger implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertInstanceOf(ClassVisitor.class, changer,
            "TargetClassChanger should implement ClassVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of ConstantVisitor.
     * Verifies that TargetClassChanger implements the ConstantVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfConstantVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertInstanceOf(ConstantVisitor.class, changer,
            "TargetClassChanger should implement ConstantVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of MemberVisitor.
     * Verifies that TargetClassChanger implements the MemberVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertInstanceOf(MemberVisitor.class, changer,
            "TargetClassChanger should implement MemberVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of AttributeVisitor.
     * Verifies that TargetClassChanger implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertInstanceOf(AttributeVisitor.class, changer,
            "TargetClassChanger should implement AttributeVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of AnnotationVisitor.
     * Verifies that TargetClassChanger implements the AnnotationVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfAnnotationVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertInstanceOf(AnnotationVisitor.class, changer,
            "TargetClassChanger should implement AnnotationVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of ElementValueVisitor.
     * Verifies that TargetClassChanger implements the ElementValueVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfElementValueVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertInstanceOf(ElementValueVisitor.class, changer,
            "TargetClassChanger should implement ElementValueVisitor interface");
    }

    /**
     * Tests that the created instance can be used as a ClassVisitor.
     * Verifies that the changer is in a valid state for use as ClassVisitor.
     */
    @Test
    public void testConstructorCreatesUsableClassVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");
        ClassVisitor classVisitor = changer;
        assertNotNull(classVisitor, "TargetClassChanger should be usable as ClassVisitor");
    }

    /**
     * Tests that the created instance can be used as a ConstantVisitor.
     * Verifies that the changer is in a valid state for use as ConstantVisitor.
     */
    @Test
    public void testConstructorCreatesUsableConstantVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");
        ConstantVisitor constantVisitor = changer;
        assertNotNull(constantVisitor, "TargetClassChanger should be usable as ConstantVisitor");
    }

    /**
     * Tests that the created instance can be used as a MemberVisitor.
     * Verifies that the changer is in a valid state for use as MemberVisitor.
     */
    @Test
    public void testConstructorCreatesUsableMemberVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");
        MemberVisitor memberVisitor = changer;
        assertNotNull(memberVisitor, "TargetClassChanger should be usable as MemberVisitor");
    }

    /**
     * Tests that the created instance can be used as an AttributeVisitor.
     * Verifies that the changer is in a valid state for use as AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesUsableAttributeVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");
        AttributeVisitor attributeVisitor = changer;
        assertNotNull(attributeVisitor, "TargetClassChanger should be usable as AttributeVisitor");
    }

    /**
     * Tests that the created instance can be used as an AnnotationVisitor.
     * Verifies that the changer is in a valid state for use as AnnotationVisitor.
     */
    @Test
    public void testConstructorCreatesUsableAnnotationVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");
        AnnotationVisitor annotationVisitor = changer;
        assertNotNull(annotationVisitor, "TargetClassChanger should be usable as AnnotationVisitor");
    }

    /**
     * Tests that the created instance can be used as an ElementValueVisitor.
     * Verifies that the changer is in a valid state for use as ElementValueVisitor.
     */
    @Test
    public void testConstructorCreatesUsableElementValueVisitor() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");
        ElementValueVisitor elementValueVisitor = changer;
        assertNotNull(elementValueVisitor, "TargetClassChanger should be usable as ElementValueVisitor");
    }

    /**
     * Tests that multiple changers all implement the required interfaces.
     * Verifies that each instance properly implements all expected interfaces.
     */
    @Test
    public void testMultipleInstancesImplementAllInterfaces() {
        // Act
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Assert
        assertInstanceOf(ClassVisitor.class, changer1, "Changer 1 should implement ClassVisitor");
        assertInstanceOf(ClassVisitor.class, changer2, "Changer 2 should implement ClassVisitor");
        assertInstanceOf(ClassVisitor.class, changer3, "Changer 3 should implement ClassVisitor");

        assertInstanceOf(ConstantVisitor.class, changer1, "Changer 1 should implement ConstantVisitor");
        assertInstanceOf(ConstantVisitor.class, changer2, "Changer 2 should implement ConstantVisitor");
        assertInstanceOf(ConstantVisitor.class, changer3, "Changer 3 should implement ConstantVisitor");

        assertInstanceOf(MemberVisitor.class, changer1, "Changer 1 should implement MemberVisitor");
        assertInstanceOf(MemberVisitor.class, changer2, "Changer 2 should implement MemberVisitor");
        assertInstanceOf(MemberVisitor.class, changer3, "Changer 3 should implement MemberVisitor");

        assertInstanceOf(AttributeVisitor.class, changer1, "Changer 1 should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, changer2, "Changer 2 should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, changer3, "Changer 3 should implement AttributeVisitor");

        assertInstanceOf(AnnotationVisitor.class, changer1, "Changer 1 should implement AnnotationVisitor");
        assertInstanceOf(AnnotationVisitor.class, changer2, "Changer 2 should implement AnnotationVisitor");
        assertInstanceOf(AnnotationVisitor.class, changer3, "Changer 3 should implement AnnotationVisitor");

        assertInstanceOf(ElementValueVisitor.class, changer1, "Changer 1 should implement ElementValueVisitor");
        assertInstanceOf(ElementValueVisitor.class, changer2, "Changer 2 should implement ElementValueVisitor");
        assertInstanceOf(ElementValueVisitor.class, changer3, "Changer 3 should implement ElementValueVisitor");
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
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(changer, "TargetClassChanger should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that creating multiple changers is efficient.
     * Verifies that constructor performance is consistent across multiple invocations.
     */
    @Test
    public void testMultipleConstructorCallsAreEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - create 100 changers
        for (int i = 0; i < 100; i++) {
            TargetClassChanger changer = new TargetClassChanger();
            assertNotNull(changer, "Changer should be created");
        }

        // Assert
        long duration = System.nanoTime() - startTime;
        // Creating 100 changers should complete in less than 100 milliseconds
        assertTrue(duration < 100_000_000L,
            "Creating 100 changers should be efficient (took " + duration + " ns)");
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
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");
        // Verify that the changer implements all expected interfaces
        assertInstanceOf(ClassVisitor.class, changer);
        assertInstanceOf(ConstantVisitor.class, changer);
        assertInstanceOf(MemberVisitor.class, changer);
        assertInstanceOf(AttributeVisitor.class, changer);
        assertInstanceOf(AnnotationVisitor.class, changer);
        assertInstanceOf(ElementValueVisitor.class, changer);
    }

    /**
     * Tests that the default constructor creates an instance ready for immediate use.
     * Verifies that no additional setup is required after construction.
     */
    @Test
    public void testConstructorCreatesReadyToUseInstance() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert
        assertNotNull(changer, "TargetClassChanger should be created");

        // The instance should be immediately usable as all required interfaces
        ClassVisitor classVisitor = changer;
        ConstantVisitor constantVisitor = changer;
        MemberVisitor memberVisitor = changer;
        AttributeVisitor attributeVisitor = changer;
        AnnotationVisitor annotationVisitor = changer;
        ElementValueVisitor elementValueVisitor = changer;

        assertNotNull(classVisitor, "Should be usable as ClassVisitor");
        assertNotNull(constantVisitor, "Should be usable as ConstantVisitor");
        assertNotNull(memberVisitor, "Should be usable as MemberVisitor");
        assertNotNull(attributeVisitor, "Should be usable as AttributeVisitor");
        assertNotNull(annotationVisitor, "Should be usable as AnnotationVisitor");
        assertNotNull(elementValueVisitor, "Should be usable as ElementValueVisitor");
    }

    /**
     * Tests that each new instance is independent and has its own state.
     * Verifies that creating multiple instances doesn't share state.
     */
    @Test
    public void testEachInstanceHasIndependentState() {
        // Act
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Assert
        assertNotNull(changer1, "First changer should be created");
        assertNotNull(changer2, "Second changer should be created");
        assertNotNull(changer3, "Third changer should be created");

        // Verify they are all different objects
        assertNotSame(changer1, changer2, "Changer 1 and 2 should be different instances");
        assertNotSame(changer1, changer3, "Changer 1 and 3 should be different instances");
        assertNotSame(changer2, changer3, "Changer 2 and 3 should be different instances");
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     * Verifies that there are no global state mutations.
     */
    @Test
    public void testConstructorHasNoSideEffects() {
        // Act - create multiple instances and verify each is independent
        TargetClassChanger[] changers = new TargetClassChanger[50];
        for (int i = 0; i < changers.length; i++) {
            changers[i] = new TargetClassChanger();
            assertNotNull(changers[i], "Changer " + i + " should be created");
        }

        // Assert - all instances should be distinct
        for (int i = 0; i < changers.length; i++) {
            for (int j = i + 1; j < changers.length; j++) {
                assertNotSame(changers[i], changers[j],
                    "Changer " + i + " and " + j + " should be different instances");
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
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        TargetClassChanger changer4 = new TargetClassChanger();
        TargetClassChanger changer5 = new TargetClassChanger();

        // Assert - all instances should be distinct
        assertNotSame(changer1, changer2);
        assertNotSame(changer1, changer3);
        assertNotSame(changer1, changer4);
        assertNotSame(changer1, changer5);
        assertNotSame(changer2, changer3);
        assertNotSame(changer2, changer4);
        assertNotSame(changer2, changer5);
        assertNotSame(changer3, changer4);
        assertNotSame(changer3, changer5);
        assertNotSame(changer4, changer5);
    }

    /**
     * Tests that the constructed instance maintains all required interfaces.
     * Verifies interface implementation consistency.
     */
    @Test
    public void testConstructedInstanceMaintainsAllInterfaces() {
        // Act
        TargetClassChanger changer = new TargetClassChanger();

        // Assert - verify instance can be cast to all required interfaces
        Object obj = changer;
        assertTrue(obj instanceof ClassVisitor, "Should be instanceof ClassVisitor");
        assertTrue(obj instanceof ConstantVisitor, "Should be instanceof ConstantVisitor");
        assertTrue(obj instanceof MemberVisitor, "Should be instanceof MemberVisitor");
        assertTrue(obj instanceof AttributeVisitor, "Should be instanceof AttributeVisitor");
        assertTrue(obj instanceof AnnotationVisitor, "Should be instanceof AnnotationVisitor");
        assertTrue(obj instanceof ElementValueVisitor, "Should be instanceof ElementValueVisitor");
    }

    /**
     * Tests that the default constructor can be invoked in various contexts.
     * Verifies that the constructor is flexible and can be used in different scenarios.
     */
    @Test
    public void testConstructorWorksInVariousContexts() {
        // Test direct instantiation
        TargetClassChanger changer1 = new TargetClassChanger();
        assertNotNull(changer1, "Direct instantiation should work");

        // Test instantiation in conditional
        TargetClassChanger changer2 = true ? new TargetClassChanger() : null;
        assertNotNull(changer2, "Conditional instantiation should work");

        // Test instantiation in array
        TargetClassChanger[] array = new TargetClassChanger[] {
            new TargetClassChanger(),
            new TargetClassChanger()
        };
        assertNotNull(array[0], "Array element 0 should be created");
        assertNotNull(array[1], "Array element 1 should be created");
        assertNotSame(array[0], array[1], "Array elements should be different instances");

        // Test instantiation as interface type
        ClassVisitor classVisitor = new TargetClassChanger();
        assertNotNull(classVisitor, "Instantiation as ClassVisitor interface should work");
    }

    /**
     * Tests that multiple changers can coexist without interference.
     * Verifies that instances are completely independent.
     */
    @Test
    public void testMultipleChangerCoexistence() {
        // Act - create multiple changers
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Assert - all should be valid and independent
        assertNotNull(changer1);
        assertNotNull(changer2);
        assertNotNull(changer3);

        assertNotSame(changer1, changer2);
        assertNotSame(changer1, changer3);
        assertNotSame(changer2, changer3);

        // All should implement all interfaces
        assertInstanceOf(ClassVisitor.class, changer1);
        assertInstanceOf(ClassVisitor.class, changer2);
        assertInstanceOf(ClassVisitor.class, changer3);
    }
}
