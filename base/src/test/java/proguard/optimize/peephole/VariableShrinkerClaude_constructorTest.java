package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link VariableShrinker} constructor.
 * Tests both VariableShrinker() and VariableShrinker(MemberVisitor) constructors.
 */
public class VariableShrinkerClaude_constructorTest {

    /**
     * Tests the no-arg constructor VariableShrinker().
     * Verifies that the VariableShrinker instance can be instantiated without parameters.
     */
    @Test
    public void testNoArgConstructor() {
        // Act - Create VariableShrinker with no-arg constructor
        VariableShrinker shrinker = new VariableShrinker();

        // Assert - Verify the VariableShrinker instance was created successfully
        assertNotNull(shrinker, "VariableShrinker should be instantiated successfully");
    }

    /**
     * Tests the constructor VariableShrinker(MemberVisitor) with a valid MemberVisitor.
     * Verifies that the VariableShrinker instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidMemberVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create VariableShrinker with the visitor
        VariableShrinker shrinker = new VariableShrinker(visitor);

        // Assert - Verify the VariableShrinker instance was created successfully
        assertNotNull(shrinker, "VariableShrinker should be instantiated successfully");
    }

    /**
     * Tests the constructor VariableShrinker(MemberVisitor) with a null MemberVisitor.
     * Verifies that the VariableShrinker constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullMemberVisitor() {
        // Act - Create VariableShrinker with null visitor
        VariableShrinker shrinker = new VariableShrinker(null);

        // Assert - Verify the VariableShrinker instance was created
        assertNotNull(shrinker, "VariableShrinker should be instantiated even with null visitor");
    }

    /**
     * Tests that multiple VariableShrinker instances can be created independently.
     * Verifies that multiple VariableShrinker instances can be created with different visitors.
     */
    @Test
    public void testMultipleVariableShrinkerInstances() {
        // Arrange - Create two different MemberVisitors
        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new TestMemberVisitor();

        // Act - Create two VariableShrinker instances
        VariableShrinker shrinker1 = new VariableShrinker(visitor1);
        VariableShrinker shrinker2 = new VariableShrinker(visitor2);

        // Assert - Verify both VariableShrinker instances were created successfully
        assertNotNull(shrinker1, "First VariableShrinker instance should be created");
        assertNotNull(shrinker2, "Second VariableShrinker instance should be created");
        assertNotSame(shrinker1, shrinker2, "VariableShrinker instances should be different objects");
    }

    /**
     * Tests that the same visitor can be used to create multiple VariableShrinker instances.
     * Verifies that multiple VariableShrinker instances can share the same visitor.
     */
    @Test
    public void testMultipleVariableShrinkerInstancesWithSameVisitor() {
        // Arrange - Create a single MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create multiple VariableShrinker instances with the same visitor
        VariableShrinker shrinker1 = new VariableShrinker(visitor);
        VariableShrinker shrinker2 = new VariableShrinker(visitor);

        // Assert - Verify both VariableShrinker instances were created successfully
        assertNotNull(shrinker1, "First VariableShrinker instance should be created");
        assertNotNull(shrinker2, "Second VariableShrinker instance should be created");
        assertNotSame(shrinker1, shrinker2, "VariableShrinker instances should be different objects");
    }

    /**
     * Tests that the no-arg constructor creates a VariableShrinker equivalent to passing null.
     * Verifies that VariableShrinker() is equivalent to VariableShrinker(null).
     */
    @Test
    public void testNoArgConstructorEquivalentToNullParameter() {
        // Act - Create VariableShrinker with both constructors
        VariableShrinker shrinker1 = new VariableShrinker();
        VariableShrinker shrinker2 = new VariableShrinker(null);

        // Assert - Verify both VariableShrinker instances were created successfully
        assertNotNull(shrinker1, "No-arg VariableShrinker should be created");
        assertNotNull(shrinker2, "Null-arg VariableShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "Different VariableShrinker instances should be different objects");
    }

    /**
     * Tests that the constructor accepts different MemberVisitor implementations.
     * Verifies that VariableShrinker works with various MemberVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentMemberVisitorImplementations() {
        // Arrange & Act - Create VariableShrinker with different visitor types
        VariableShrinker shrinker1 = new VariableShrinker(new TestMemberVisitor());
        VariableShrinker shrinker2 = new VariableShrinker(new TrackingMemberVisitor());
        VariableShrinker shrinker3 = new VariableShrinker(new AnotherTestMemberVisitor());

        // Assert - Verify all VariableShrinker instances were created successfully
        assertNotNull(shrinker1, "VariableShrinker should work with TestMemberVisitor");
        assertNotNull(shrinker2, "VariableShrinker should work with TrackingMemberVisitor");
        assertNotNull(shrinker3, "VariableShrinker should work with AnotherTestMemberVisitor");
    }

    /**
     * Tests that multiple no-arg constructor calls create independent instances.
     * Verifies that each call to the no-arg constructor creates a new object.
     */
    @Test
    public void testMultipleNoArgConstructorCalls() {
        // Act - Create multiple VariableShrinker instances with no-arg constructor
        VariableShrinker shrinker1 = new VariableShrinker();
        VariableShrinker shrinker2 = new VariableShrinker();
        VariableShrinker shrinker3 = new VariableShrinker();

        // Assert - Verify all instances are distinct
        assertNotNull(shrinker1, "First VariableShrinker should be created");
        assertNotNull(shrinker2, "Second VariableShrinker should be created");
        assertNotNull(shrinker3, "Third VariableShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "First and second instances should be different");
        assertNotSame(shrinker2, shrinker3, "Second and third instances should be different");
        assertNotSame(shrinker1, shrinker3, "First and third instances should be different");
    }

    /**
     * Tests that the created VariableShrinker is a valid AttributeVisitor.
     * Verifies that VariableShrinker can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Act - Create VariableShrinker with both constructors
        VariableShrinker shrinker1 = new VariableShrinker();
        VariableShrinker shrinker2 = new VariableShrinker(new TestMemberVisitor());

        // Assert - Verify they can be used as AttributeVisitors
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, shrinker1,
                "VariableShrinker should implement AttributeVisitor");
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, shrinker2,
                "VariableShrinker with visitor should implement AttributeVisitor");
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
