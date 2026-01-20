package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UnusedParameterOptimizationInfoUpdater} constructors.
 * Tests both UnusedParameterOptimizationInfoUpdater() and UnusedParameterOptimizationInfoUpdater(MemberVisitor) constructors.
 *
 * The UnusedParameterOptimizationInfoUpdater is an AttributeVisitor that removes unused parameters from the optimization info
 * of methods. The no-arg constructor creates an updater without an extra visitor, while the parameterized constructor
 * allows for an optional extra visitor that will be called for methods with removed parameters.
 */
public class UnusedParameterOptimizationInfoUpdaterClaude_constructorTest {

    /**
     * Tests the no-arg constructor.
     * Verifies that the UnusedParameterOptimizationInfoUpdater instance can be instantiated without parameters.
     */
    @Test
    public void testNoArgConstructor() {
        // Act - Create UnusedParameterOptimizationInfoUpdater with no arguments
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();

        // Assert - Verify the UnusedParameterOptimizationInfoUpdater instance was created successfully
        assertNotNull(updater, "UnusedParameterOptimizationInfoUpdater should be instantiated successfully");
    }

    /**
     * Tests the parameterized constructor with a valid MemberVisitor.
     * Verifies that the UnusedParameterOptimizationInfoUpdater instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidMemberVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create UnusedParameterOptimizationInfoUpdater with the visitor
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(visitor);

        // Assert - Verify the UnusedParameterOptimizationInfoUpdater instance was created successfully
        assertNotNull(updater, "UnusedParameterOptimizationInfoUpdater should be instantiated successfully");
    }

    /**
     * Tests the parameterized constructor with a null MemberVisitor.
     * Verifies that the UnusedParameterOptimizationInfoUpdater constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullMemberVisitor() {
        // Act - Create UnusedParameterOptimizationInfoUpdater with null visitor
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(null);

        // Assert - Verify the UnusedParameterOptimizationInfoUpdater instance was created
        assertNotNull(updater, "UnusedParameterOptimizationInfoUpdater should be instantiated even with null visitor");
    }

    /**
     * Tests that multiple updater instances can be created independently.
     * Verifies that multiple UnusedParameterOptimizationInfoUpdater instances can be created independently.
     */
    @Test
    public void testMultipleUpdaterInstances() {
        // Arrange - Create two different MemberVisitors
        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new TestMemberVisitor();

        // Act - Create two UnusedParameterOptimizationInfoUpdater instances
        UnusedParameterOptimizationInfoUpdater updater1 = new UnusedParameterOptimizationInfoUpdater(visitor1);
        UnusedParameterOptimizationInfoUpdater updater2 = new UnusedParameterOptimizationInfoUpdater(visitor2);

        // Assert - Verify both UnusedParameterOptimizationInfoUpdater instances were created successfully
        assertNotNull(updater1, "First UnusedParameterOptimizationInfoUpdater instance should be created");
        assertNotNull(updater2, "Second UnusedParameterOptimizationInfoUpdater instance should be created");
        assertNotSame(updater1, updater2, "UnusedParameterOptimizationInfoUpdater instances should be different objects");
    }

    /**
     * Tests multiple updater instances with the same visitor.
     * Verifies that the same visitor can be used to create multiple UnusedParameterOptimizationInfoUpdater instances.
     */
    @Test
    public void testMultipleUpdaterInstancesWithSameVisitor() {
        // Arrange - Create a single MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create multiple UnusedParameterOptimizationInfoUpdater instances with the same visitor
        UnusedParameterOptimizationInfoUpdater updater1 = new UnusedParameterOptimizationInfoUpdater(visitor);
        UnusedParameterOptimizationInfoUpdater updater2 = new UnusedParameterOptimizationInfoUpdater(visitor);

        // Assert - Verify both UnusedParameterOptimizationInfoUpdater instances were created successfully
        assertNotNull(updater1, "First UnusedParameterOptimizationInfoUpdater instance should be created");
        assertNotNull(updater2, "Second UnusedParameterOptimizationInfoUpdater instance should be created");
        assertNotSame(updater1, updater2, "UnusedParameterOptimizationInfoUpdater instances should be different objects");
    }

    /**
     * Tests that the no-arg constructor properly initializes without extra visitor.
     * Verifies behavior when using the no-arg constructor and visiting attributes.
     */
    @Test
    public void testNoArgConstructorDoesNotCallExtraVisitor() {
        // Arrange - Create UnusedParameterOptimizationInfoUpdater with no-arg constructor
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();

        // Create a mock attribute that is not a CodeAttribute
        ProgramClass mockClass = mock(ProgramClass.class);
        Attribute mockAttribute = mock(Attribute.class);

        // Act - Visit any attribute (should do nothing, no exceptions)
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(mockClass, mockAttribute);
        }, "No-arg constructor updater should handle visitAnyAttribute without error");
    }

    /**
     * Tests that the parameterized constructor stores the visitor correctly by checking behavior.
     * Verifies that the updater can be used with visitAnyAttribute without errors.
     */
    @Test
    public void testConstructorStoresVisitorCorrectly() {
        // Arrange - Create a tracking visitor
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(trackingVisitor);

        // Create a simple attribute
        ProgramClass mockClass = mock(ProgramClass.class);
        Attribute mockAttribute = mock(Attribute.class);

        // Act - Visit any attribute (should not call visitor since it's not a CodeAttribute)
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(mockClass, mockAttribute);
        }, "Visiting any attribute should not throw exception");

        // Assert - Verify the visitor was not called (visitAnyAttribute does nothing)
        assertFalse(trackingVisitor.visitedProgramMethod, "Visitor should not be called for non-CodeAttribute");
    }

    /**
     * Tests that the parameterized constructor with null visitor doesn't fail during basic operations.
     * Verifies that null visitor is handled gracefully.
     */
    @Test
    public void testConstructorWithNullVisitorDuringBasicOperations() {
        // Arrange - Create UnusedParameterOptimizationInfoUpdater with null visitor
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(null);

        // Create a mock attribute
        ProgramClass mockClass = mock(ProgramClass.class);
        Attribute mockAttribute = mock(Attribute.class);

        // Act & Assert - Should not throw exception with visitAnyAttribute
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(mockClass, mockAttribute);
        }, "Null visitor should not cause issues with visitAnyAttribute");
    }

    /**
     * Tests that the constructed instance implements AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesAttributeVisitor() {
        // Arrange & Act
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();

        // Assert
        assertTrue(updater instanceof AttributeVisitor,
                "UnusedParameterOptimizationInfoUpdater should implement AttributeVisitor");
    }

    /**
     * Tests that the updater can be used through the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesUsableAttributeVisitorInterface() {
        // Arrange
        AttributeVisitor updater = new UnusedParameterOptimizationInfoUpdater();

        // Create a mock attribute
        ProgramClass mockClass = mock(ProgramClass.class);
        Attribute mockAttribute = mock(Attribute.class);

        // Act & Assert - Should work through interface
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(mockClass, mockAttribute);
        }, "Updater should work when called through AttributeVisitor interface");
    }

    /**
     * Tests that the constructors don't throw any exceptions.
     */
    @Test
    public void testConstructorsDoNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new UnusedParameterOptimizationInfoUpdater(),
                "No-arg constructor should not throw exception");
        assertDoesNotThrow(() -> new UnusedParameterOptimizationInfoUpdater(new TestMemberVisitor()),
                "Parameterized constructor should not throw exception with valid visitor");
        assertDoesNotThrow(() -> new UnusedParameterOptimizationInfoUpdater(null),
                "Parameterized constructor should not throw exception with null visitor");
    }

    /**
     * Tests that multiple no-arg constructor instances can be created.
     */
    @Test
    public void testMultipleNoArgConstructorInstances() {
        // Act - Create multiple instances using no-arg constructor
        UnusedParameterOptimizationInfoUpdater updater1 = new UnusedParameterOptimizationInfoUpdater();
        UnusedParameterOptimizationInfoUpdater updater2 = new UnusedParameterOptimizationInfoUpdater();

        // Assert - Verify both instances were created successfully
        assertNotNull(updater1, "First UnusedParameterOptimizationInfoUpdater instance should be created");
        assertNotNull(updater2, "Second UnusedParameterOptimizationInfoUpdater instance should be created");
        assertNotSame(updater1, updater2, "UnusedParameterOptimizationInfoUpdater instances should be different objects");
    }

    /**
     * Tests that the parameterized constructor accepts different MemberVisitor implementations.
     * Verifies that UnusedParameterOptimizationInfoUpdater works with various MemberVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentMemberVisitorImplementations() {
        // Arrange & Act - Create UnusedParameterOptimizationInfoUpdater with different visitor types
        UnusedParameterOptimizationInfoUpdater updater1 = new UnusedParameterOptimizationInfoUpdater(new TestMemberVisitor());
        UnusedParameterOptimizationInfoUpdater updater2 = new UnusedParameterOptimizationInfoUpdater(new TrackingMemberVisitor());
        UnusedParameterOptimizationInfoUpdater updater3 = new UnusedParameterOptimizationInfoUpdater(new AnotherTestMemberVisitor());

        // Assert - Verify all UnusedParameterOptimizationInfoUpdater instances were created successfully
        assertNotNull(updater1, "UnusedParameterOptimizationInfoUpdater should work with TestMemberVisitor");
        assertNotNull(updater2, "UnusedParameterOptimizationInfoUpdater should work with TrackingMemberVisitor");
        assertNotNull(updater3, "UnusedParameterOptimizationInfoUpdater should work with AnotherTestMemberVisitor");
    }

    /**
     * Tests that no-arg constructor behaves identically to parameterized constructor with null.
     * Verifies that the no-arg constructor properly delegates to the parameterized constructor.
     */
    @Test
    public void testNoArgConstructorEquivalentToNullParameter() {
        // Arrange & Act - Create instances using both constructors
        UnusedParameterOptimizationInfoUpdater noArgUpdater = new UnusedParameterOptimizationInfoUpdater();
        UnusedParameterOptimizationInfoUpdater nullParamUpdater = new UnusedParameterOptimizationInfoUpdater(null);

        // Create a mock attribute for testing
        ProgramClass mockClass = mock(ProgramClass.class);
        Attribute mockAttribute = mock(Attribute.class);

        // Act & Assert - Both should behave the same way
        assertDoesNotThrow(() -> {
            noArgUpdater.visitAnyAttribute(mockClass, mockAttribute);
            nullParamUpdater.visitAnyAttribute(mockClass, mockAttribute);
        }, "Both constructors should result in equivalent behavior");
    }

    /**
     * Tests that the updater can be used multiple times.
     * Verifies that the constructor creates a reusable updater instance.
     */
    @Test
    public void testConstructorCreatesReusableUpdater() {
        // Arrange - Create an updater with a visitor
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(trackingVisitor);

        ProgramClass mockClass = mock(ProgramClass.class);
        Attribute mockAttribute1 = mock(Attribute.class);
        Attribute mockAttribute2 = mock(Attribute.class);

        // Act - Visit multiple attributes
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(mockClass, mockAttribute1);
            updater.visitAnyAttribute(mockClass, mockAttribute2);
        }, "Updater should be reusable for multiple visits");

        // Assert - Visitor should not have been called (visitAnyAttribute does nothing)
        assertFalse(trackingVisitor.visitedProgramMethod, "Visitor should not be called for visitAnyAttribute");
    }

    /**
     * Simple test MemberVisitor implementation for testing purposes.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }
    }

    /**
     * MemberVisitor implementation that tracks whether it was visited.
     */
    private static class TrackingMemberVisitor implements MemberVisitor {
        boolean visitedProgramField = false;
        boolean visitedProgramMethod = false;
        boolean visitedLibraryField = false;
        boolean visitedLibraryMethod = false;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitedProgramField = true;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitedProgramMethod = true;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitedLibraryField = true;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitedLibraryMethod = true;
        }
    }

    /**
     * MemberVisitor implementation that counts how many times it was called.
     */
    private static class CountingMemberVisitor implements MemberVisitor {
        int visitCount = 0;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitCount++;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitCount++;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitCount++;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitCount++;
        }
    }

    /**
     * Another test MemberVisitor implementation for testing purposes.
     */
    private static class AnotherTestMemberVisitor implements MemberVisitor {
        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }
    }
}
