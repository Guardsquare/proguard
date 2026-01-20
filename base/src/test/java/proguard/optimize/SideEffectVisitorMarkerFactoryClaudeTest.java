package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SideEffectVisitorMarkerFactory}.
 * Tests all public methods including constructors and createMemberVisitor.
 */
public class SideEffectVisitorMarkerFactoryClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor with optimizeConservatively=true.
     * Verifies that the instance is successfully created.
     */
    @Test
    public void testConstructorWithOptimizeConservativelyTrue() {
        // Arrange & Act
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(true);

        // Assert
        assertNotNull(factory, "Factory should be created successfully");
    }

    /**
     * Tests the constructor with optimizeConservatively=false.
     * Verifies that the instance is successfully created.
     */
    @Test
    public void testConstructorWithOptimizeConservativelyFalse() {
        // Arrange & Act
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(false);

        // Assert
        assertNotNull(factory, "Factory should be created successfully");
    }

    /**
     * Tests that the constructor creates instances implementing the correct interface.
     * Verifies that the factory implements MemberVisitorFactory.
     */
    @Test
    public void testConstructorCreatesCorrectInterfaceImplementation() {
        // Arrange & Act
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(true);

        // Assert
        assertTrue(factory instanceof InfluenceFixpointVisitor.MemberVisitorFactory,
                "Factory should implement InfluenceFixpointVisitor.MemberVisitorFactory");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each constructor call creates a distinct instance.
     */
    @Test
    public void testConstructorCreatesMultipleIndependentInstances() {
        // Arrange & Act
        SideEffectVisitorMarkerFactory factory1 = new SideEffectVisitorMarkerFactory(true);
        SideEffectVisitorMarkerFactory factory2 = new SideEffectVisitorMarkerFactory(false);
        SideEffectVisitorMarkerFactory factory3 = new SideEffectVisitorMarkerFactory(true);

        // Assert
        assertNotNull(factory1, "First factory should be created");
        assertNotNull(factory2, "Second factory should be created");
        assertNotNull(factory3, "Third factory should be created");
        assertNotSame(factory1, factory2, "Each constructor call should create a distinct instance");
        assertNotSame(factory1, factory3, "Each constructor call should create a distinct instance");
        assertNotSame(factory2, factory3, "Each constructor call should create a distinct instance");
    }

    // ========== createMemberVisitor Tests ==========

    /**
     * Tests createMemberVisitor with a non-null MemberVisitor.
     * Verifies that the method returns a non-null MemberVisitor.
     */
    @Test
    public void testCreateMemberVisitorWithNonNullVisitor() {
        // Arrange
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(true);
        MemberVisitor mockVisitor = new TestMemberVisitor();

        // Act
        MemberVisitor result = factory.createMemberVisitor(mockVisitor);

        // Assert
        assertNotNull(result, "createMemberVisitor should return a non-null MemberVisitor");
    }

    /**
     * Tests createMemberVisitor with optimizeConservatively=false.
     * Verifies that the method returns a non-null MemberVisitor regardless of the flag.
     */
    @Test
    public void testCreateMemberVisitorWithOptimizeConservativelyFalse() {
        // Arrange
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(false);
        MemberVisitor mockVisitor = new TestMemberVisitor();

        // Act
        MemberVisitor result = factory.createMemberVisitor(mockVisitor);

        // Assert
        assertNotNull(result, "createMemberVisitor should return a non-null MemberVisitor");
    }

    /**
     * Tests createMemberVisitor with null MemberVisitor parameter.
     * Verifies that the method handles null input gracefully.
     */
    @Test
    public void testCreateMemberVisitorWithNullVisitor() {
        // Arrange
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(true);

        // Act
        MemberVisitor result = factory.createMemberVisitor(null);

        // Assert
        assertNotNull(result, "createMemberVisitor should return a non-null MemberVisitor even with null input");
    }

    /**
     * Tests createMemberVisitor can be called multiple times.
     * Verifies that each call returns a distinct visitor instance.
     */
    @Test
    public void testCreateMemberVisitorMultipleCalls() {
        // Arrange
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(true);
        MemberVisitor mockVisitor = new TestMemberVisitor();

        // Act
        MemberVisitor result1 = factory.createMemberVisitor(mockVisitor);
        MemberVisitor result2 = factory.createMemberVisitor(mockVisitor);
        MemberVisitor result3 = factory.createMemberVisitor(mockVisitor);

        // Assert
        assertNotNull(result1, "First call should return a non-null visitor");
        assertNotNull(result2, "Second call should return a non-null visitor");
        assertNotNull(result3, "Third call should return a non-null visitor");
        assertNotSame(result1, result2, "Each call should return a new instance");
        assertNotSame(result1, result3, "Each call should return a new instance");
        assertNotSame(result2, result3, "Each call should return a new instance");
    }

    /**
     * Tests createMemberVisitor with different MemberVisitor instances.
     * Verifies that the method can handle different input visitors.
     */
    @Test
    public void testCreateMemberVisitorWithDifferentVisitors() {
        // Arrange
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(true);
        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new TestMemberVisitor();

        // Act
        MemberVisitor result1 = factory.createMemberVisitor(visitor1);
        MemberVisitor result2 = factory.createMemberVisitor(visitor2);

        // Assert
        assertNotNull(result1, "First result should be non-null");
        assertNotNull(result2, "Second result should be non-null");
        assertNotSame(result1, result2, "Different input visitors should produce different results");
    }

    /**
     * Tests createMemberVisitor from different factory instances.
     * Verifies that different factories with different configurations can create visitors.
     */
    @Test
    public void testCreateMemberVisitorFromDifferentFactories() {
        // Arrange
        SideEffectVisitorMarkerFactory factory1 = new SideEffectVisitorMarkerFactory(true);
        SideEffectVisitorMarkerFactory factory2 = new SideEffectVisitorMarkerFactory(false);
        MemberVisitor mockVisitor = new TestMemberVisitor();

        // Act
        MemberVisitor result1 = factory1.createMemberVisitor(mockVisitor);
        MemberVisitor result2 = factory2.createMemberVisitor(mockVisitor);

        // Assert
        assertNotNull(result1, "Factory with optimizeConservatively=true should create a visitor");
        assertNotNull(result2, "Factory with optimizeConservatively=false should create a visitor");
        assertNotSame(result1, result2, "Different factories should create different visitor instances");
    }

    // ========== Integration Tests ==========

    /**
     * Tests the complete lifecycle: constructor and createMemberVisitor.
     * Verifies that the factory can be created and used immediately.
     */
    @Test
    public void testCompleteLifecycle() {
        // Arrange & Act
        SideEffectVisitorMarkerFactory factory = new SideEffectVisitorMarkerFactory(true);
        MemberVisitor mockVisitor = new TestMemberVisitor();
        MemberVisitor result = factory.createMemberVisitor(mockVisitor);

        // Assert
        assertNotNull(factory, "Factory should be created");
        assertNotNull(result, "Visitor should be created");
    }

    /**
     * Tests that the factory can be used through the MemberVisitorFactory interface.
     * Verifies polymorphic behavior.
     */
    @Test
    public void testThroughInterface() {
        // Arrange
        InfluenceFixpointVisitor.MemberVisitorFactory factory =
            new SideEffectVisitorMarkerFactory(true);
        MemberVisitor mockVisitor = new TestMemberVisitor();

        // Act
        MemberVisitor result = factory.createMemberVisitor(mockVisitor);

        // Assert
        assertNotNull(result, "Factory through interface should create a non-null visitor");
    }

    /**
     * Tests createMemberVisitor with both true and false conservative optimization settings.
     * Verifies that both configurations work correctly.
     */
    @Test
    public void testCreateMemberVisitorBothConfigurations() {
        // Arrange
        MemberVisitor mockVisitor = new TestMemberVisitor();

        // Act & Assert - True configuration
        SideEffectVisitorMarkerFactory factoryTrue = new SideEffectVisitorMarkerFactory(true);
        MemberVisitor resultTrue = factoryTrue.createMemberVisitor(mockVisitor);
        assertNotNull(resultTrue, "Factory with optimizeConservatively=true should create a visitor");

        // Act & Assert - False configuration
        SideEffectVisitorMarkerFactory factoryFalse = new SideEffectVisitorMarkerFactory(false);
        MemberVisitor resultFalse = factoryFalse.createMemberVisitor(mockVisitor);
        assertNotNull(resultFalse, "Factory with optimizeConservatively=false should create a visitor");
    }

    // ========== Helper Classes ==========

    /**
     * Simple test implementation of MemberVisitor for testing purposes.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        @Override
        public void visitProgramMethod(proguard.classfile.ProgramClass programClass,
                                       proguard.classfile.ProgramMethod programMethod) {
            // No-op implementation for testing
        }

        @Override
        public void visitProgramField(proguard.classfile.ProgramClass programClass,
                                      proguard.classfile.ProgramField programField) {
            // No-op implementation for testing
        }

        @Override
        public void visitLibraryMethod(proguard.classfile.LibraryClass libraryClass,
                                       proguard.classfile.LibraryMethod libraryMethod) {
            // No-op implementation for testing
        }

        @Override
        public void visitLibraryField(proguard.classfile.LibraryClass libraryClass,
                                      proguard.classfile.LibraryField libraryField) {
            // No-op implementation for testing
        }
    }
}
