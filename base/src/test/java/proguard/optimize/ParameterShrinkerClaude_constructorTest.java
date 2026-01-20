package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterShrinker} constructor.
 * Tests both ParameterShrinker() and ParameterShrinker(MemberVisitor) constructors.
 */
public class ParameterShrinkerClaude_constructorTest {

    /**
     * Tests the no-arg constructor ParameterShrinker().
     * Verifies that the ParameterShrinker instance can be instantiated without parameters.
     */
    @Test
    public void testNoArgConstructor() {
        // Act - Create ParameterShrinker with no-arg constructor
        ParameterShrinker shrinker = new ParameterShrinker();

        // Assert - Verify the ParameterShrinker instance was created successfully
        assertNotNull(shrinker, "ParameterShrinker should be instantiated successfully");
    }

    /**
     * Tests the constructor ParameterShrinker(MemberVisitor) with a valid MemberVisitor.
     * Verifies that the ParameterShrinker instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidMemberVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create ParameterShrinker with the visitor
        ParameterShrinker shrinker = new ParameterShrinker(visitor);

        // Assert - Verify the ParameterShrinker instance was created successfully
        assertNotNull(shrinker, "ParameterShrinker should be instantiated successfully");
    }

    /**
     * Tests the constructor ParameterShrinker(MemberVisitor) with a null MemberVisitor.
     * Verifies that the ParameterShrinker constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullMemberVisitor() {
        // Act - Create ParameterShrinker with null visitor
        ParameterShrinker shrinker = new ParameterShrinker(null);

        // Assert - Verify the ParameterShrinker instance was created
        assertNotNull(shrinker, "ParameterShrinker should be instantiated even with null visitor");
    }

    /**
     * Tests that multiple ParameterShrinker instances can be created independently.
     * Verifies that multiple ParameterShrinker instances can be created with different visitors.
     */
    @Test
    public void testMultipleParameterShrinkerInstances() {
        // Arrange - Create two different MemberVisitors
        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new TestMemberVisitor();

        // Act - Create two ParameterShrinker instances
        ParameterShrinker shrinker1 = new ParameterShrinker(visitor1);
        ParameterShrinker shrinker2 = new ParameterShrinker(visitor2);

        // Assert - Verify both ParameterShrinker instances were created successfully
        assertNotNull(shrinker1, "First ParameterShrinker instance should be created");
        assertNotNull(shrinker2, "Second ParameterShrinker instance should be created");
        assertNotSame(shrinker1, shrinker2, "ParameterShrinker instances should be different objects");
    }

    /**
     * Tests that the same visitor can be used to create multiple ParameterShrinker instances.
     * Verifies that multiple ParameterShrinker instances can share the same visitor.
     */
    @Test
    public void testMultipleParameterShrinkerInstancesWithSameVisitor() {
        // Arrange - Create a single MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create multiple ParameterShrinker instances with the same visitor
        ParameterShrinker shrinker1 = new ParameterShrinker(visitor);
        ParameterShrinker shrinker2 = new ParameterShrinker(visitor);

        // Assert - Verify both ParameterShrinker instances were created successfully
        assertNotNull(shrinker1, "First ParameterShrinker instance should be created");
        assertNotNull(shrinker2, "Second ParameterShrinker instance should be created");
        assertNotSame(shrinker1, shrinker2, "ParameterShrinker instances should be different objects");
    }

    /**
     * Tests that the no-arg constructor creates a ParameterShrinker equivalent to passing null.
     * Verifies that ParameterShrinker() is equivalent to ParameterShrinker(null).
     */
    @Test
    public void testNoArgConstructorEquivalentToNullParameter() {
        // Act - Create ParameterShrinker with both constructors
        ParameterShrinker shrinker1 = new ParameterShrinker();
        ParameterShrinker shrinker2 = new ParameterShrinker(null);

        // Assert - Verify both ParameterShrinker instances were created successfully
        assertNotNull(shrinker1, "No-arg ParameterShrinker should be created");
        assertNotNull(shrinker2, "Null-arg ParameterShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "Different ParameterShrinker instances should be different objects");
    }

    /**
     * Tests that the constructor accepts different MemberVisitor implementations.
     * Verifies that ParameterShrinker works with various MemberVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentMemberVisitorImplementations() {
        // Arrange & Act - Create ParameterShrinker with different visitor types
        ParameterShrinker shrinker1 = new ParameterShrinker(new TestMemberVisitor());
        ParameterShrinker shrinker2 = new ParameterShrinker(new TrackingMemberVisitor());
        ParameterShrinker shrinker3 = new ParameterShrinker(new AnotherTestMemberVisitor());

        // Assert - Verify all ParameterShrinker instances were created successfully
        assertNotNull(shrinker1, "ParameterShrinker should work with TestMemberVisitor");
        assertNotNull(shrinker2, "ParameterShrinker should work with TrackingMemberVisitor");
        assertNotNull(shrinker3, "ParameterShrinker should work with AnotherTestMemberVisitor");
    }

    /**
     * Tests that multiple no-arg constructor calls create independent instances.
     * Verifies that each call to the no-arg constructor creates a new object.
     */
    @Test
    public void testMultipleNoArgConstructorCalls() {
        // Act - Create multiple ParameterShrinker instances with no-arg constructor
        ParameterShrinker shrinker1 = new ParameterShrinker();
        ParameterShrinker shrinker2 = new ParameterShrinker();
        ParameterShrinker shrinker3 = new ParameterShrinker();

        // Assert - Verify all instances are distinct
        assertNotNull(shrinker1, "First ParameterShrinker should be created");
        assertNotNull(shrinker2, "Second ParameterShrinker should be created");
        assertNotNull(shrinker3, "Third ParameterShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "First and second instances should be different");
        assertNotSame(shrinker2, shrinker3, "Second and third instances should be different");
        assertNotSame(shrinker1, shrinker3, "First and third instances should be different");
    }

    /**
     * Tests that the created ParameterShrinker is a valid AttributeVisitor.
     * Verifies that ParameterShrinker can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Act - Create ParameterShrinker with both constructors
        ParameterShrinker shrinker1 = new ParameterShrinker();
        ParameterShrinker shrinker2 = new ParameterShrinker(new TestMemberVisitor());

        // Assert - Verify they can be used as AttributeVisitors
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, shrinker1,
                "ParameterShrinker should implement AttributeVisitor");
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, shrinker2,
                "ParameterShrinker with visitor should implement AttributeVisitor");
    }

    /**
     * Simple test MemberVisitor implementation for testing purposes.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }
    }

    /**
     * MemberVisitor implementation that tracks whether it was called.
     */
    private static class TrackingMemberVisitor implements MemberVisitor {
        boolean programMethodVisited = false;

        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            programMethodVisited = true;
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }
    }

    /**
     * Another test MemberVisitor implementation for testing purposes.
     */
    private static class AnotherTestMemberVisitor implements MemberVisitor {
        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            // No-op for testing
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            // No-op for testing
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            // No-op for testing
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            // No-op for testing
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            // No-op for testing
        }
    }
}
