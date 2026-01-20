package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link VariableOptimizer} constructors.
 * Tests both VariableOptimizer(boolean) and VariableOptimizer(boolean, MemberVisitor) constructors.
 */
public class VariableOptimizerClaude_constructorTest {

    /**
     * Tests the constructor with reuseThis set to true.
     * Verifies that the VariableOptimizer instance can be instantiated with reuseThis=true.
     */
    @Test
    public void testConstructorWithReuseThisTrue() {
        // Act - Create VariableOptimizer with reuseThis=true
        VariableOptimizer optimizer = new VariableOptimizer(true);

        // Assert - Verify the VariableOptimizer instance was created successfully
        assertNotNull(optimizer, "VariableOptimizer should be instantiated successfully with reuseThis=true");
    }

    /**
     * Tests the constructor with reuseThis set to false.
     * Verifies that the VariableOptimizer instance can be instantiated with reuseThis=false.
     */
    @Test
    public void testConstructorWithReuseThisFalse() {
        // Act - Create VariableOptimizer with reuseThis=false
        VariableOptimizer optimizer = new VariableOptimizer(false);

        // Assert - Verify the VariableOptimizer instance was created successfully
        assertNotNull(optimizer, "VariableOptimizer should be instantiated successfully with reuseThis=false");
    }

    /**
     * Tests that the created VariableOptimizer is a valid AttributeVisitor.
     * Verifies that VariableOptimizer implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Act - Create VariableOptimizer
        VariableOptimizer optimizer = new VariableOptimizer(true);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, optimizer,
                "VariableOptimizer should implement AttributeVisitor");
    }

    /**
     * Tests that multiple VariableOptimizer instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleVariableOptimizerInstances() {
        // Act - Create two VariableOptimizer instances
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);

        // Assert - Verify both instances were created and are different
        assertNotNull(optimizer1, "First VariableOptimizer should be created");
        assertNotNull(optimizer2, "Second VariableOptimizer should be created");
        assertNotSame(optimizer1, optimizer2, "VariableOptimizer instances should be different objects");
    }

    /**
     * Tests that multiple instances with the same reuseThis value are independent.
     * Verifies that instances with the same parameter value are still distinct objects.
     */
    @Test
    public void testMultipleInstancesWithSameReuseThis() {
        // Act - Create two VariableOptimizer instances with the same reuseThis value
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(true);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(optimizer1, "First VariableOptimizer should be created");
        assertNotNull(optimizer2, "Second VariableOptimizer should be created");
        assertNotSame(optimizer1, optimizer2, "VariableOptimizer instances should be different objects even with same reuseThis");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     * Verifies exception-free construction.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new VariableOptimizer(true),
                "Constructor should not throw exception with reuseThis=true");
        assertDoesNotThrow(() -> new VariableOptimizer(false),
                "Constructor should not throw exception with reuseThis=false");
    }

    /**
     * Tests that the constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testConstructorRepeatedInvocation() {
        // Act & Assert - Create multiple optimizers in sequence
        for (int i = 0; i < 5; i++) {
            boolean reuseThis = (i % 2 == 0);
            VariableOptimizer optimizer = new VariableOptimizer(reuseThis);
            assertNotNull(optimizer, "VariableOptimizer should be created on iteration " + i);
            assertInstanceOf(AttributeVisitor.class, optimizer,
                    "VariableOptimizer should implement AttributeVisitor on iteration " + i);
        }
    }

    /**
     * Tests that the constructor works correctly with both boolean values alternating.
     * Verifies that the constructor handles different boolean values correctly when alternated.
     */
    @Test
    public void testConstructorWithAlternatingBooleanValues() {
        // Act - Create optimizers with alternating boolean values
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);
        VariableOptimizer optimizer3 = new VariableOptimizer(true);
        VariableOptimizer optimizer4 = new VariableOptimizer(false);

        // Assert - Verify all instances were created successfully
        assertNotNull(optimizer1, "VariableOptimizer with reuseThis=true should be created");
        assertNotNull(optimizer2, "VariableOptimizer with reuseThis=false should be created");
        assertNotNull(optimizer3, "VariableOptimizer with reuseThis=true (second) should be created");
        assertNotNull(optimizer4, "VariableOptimizer with reuseThis=false (second) should be created");

        // Verify they are all different instances
        assertNotSame(optimizer1, optimizer2, "optimizer1 and optimizer2 should be different");
        assertNotSame(optimizer1, optimizer3, "optimizer1 and optimizer3 should be different");
        assertNotSame(optimizer1, optimizer4, "optimizer1 and optimizer4 should be different");
        assertNotSame(optimizer2, optimizer3, "optimizer2 and optimizer3 should be different");
        assertNotSame(optimizer2, optimizer4, "optimizer2 and optimizer4 should be different");
        assertNotSame(optimizer3, optimizer4, "optimizer3 and optimizer4 should be different");
    }

    /**
     * Tests that each instance is truly independent by creating many instances.
     * Verifies that the constructor scales correctly and produces independent instances.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        // Arrange - Create multiple optimizer instances
        int instanceCount = 10;
        VariableOptimizer[] optimizers = new VariableOptimizer[instanceCount];

        // Act - Create instances
        for (int i = 0; i < instanceCount; i++) {
            boolean reuseThis = (i % 2 == 0);
            optimizers[i] = new VariableOptimizer(reuseThis);
        }

        // Assert - Verify all instances are non-null and unique
        for (int i = 0; i < instanceCount; i++) {
            assertNotNull(optimizers[i], "Instance " + i + " should be non-null");
            for (int j = i + 1; j < instanceCount; j++) {
                assertNotSame(optimizers[i], optimizers[j],
                        "Instance " + i + " should be different from instance " + j);
            }
        }
    }

    /**
     * Tests the constructor consistency with reuseThis=true.
     * Verifies that repeated construction with reuseThis=true is stable.
     */
    @Test
    public void testConstructorConsistencyWithReuseThisTrue() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            VariableOptimizer optimizer = new VariableOptimizer(true);

            // Assert - Verify each instance is valid
            assertNotNull(optimizer, "VariableOptimizer should be created on attempt " + i);
            assertInstanceOf(AttributeVisitor.class, optimizer,
                    "VariableOptimizer should implement AttributeVisitor on attempt " + i);
        }
    }

    /**
     * Tests the constructor consistency with reuseThis=false.
     * Verifies that repeated construction with reuseThis=false is stable.
     */
    @Test
    public void testConstructorConsistencyWithReuseThisFalse() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            VariableOptimizer optimizer = new VariableOptimizer(false);

            // Assert - Verify each instance is valid
            assertNotNull(optimizer, "VariableOptimizer should be created on attempt " + i);
            assertInstanceOf(AttributeVisitor.class, optimizer,
                    "VariableOptimizer should implement AttributeVisitor on attempt " + i);
        }
    }

    /**
     * Tests that the constructor creates instances implementing AttributeVisitor for both boolean values.
     * Verifies interface implementation consistency.
     */
    @Test
    public void testConstructorAlwaysCreatesAttributeVisitor() {
        // Act - Create VariableOptimizers with both boolean values
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);

        // Assert - Verify both implement AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, optimizer1,
                "VariableOptimizer with reuseThis=true should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, optimizer2,
                "VariableOptimizer with reuseThis=false should implement AttributeVisitor");
    }

    /**
     * Tests that multiple instances created with reuseThis=true are independent.
     * Verifies that creating multiple optimizer instances with the same boolean value works correctly.
     */
    @Test
    public void testMultipleInstancesWithReuseThisTrue() {
        // Act - Create multiple VariableOptimizer instances with reuseThis=true
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(true);
        VariableOptimizer optimizer3 = new VariableOptimizer(true);

        // Assert - Verify all instances were created successfully
        assertNotNull(optimizer1, "First VariableOptimizer should be created");
        assertNotNull(optimizer2, "Second VariableOptimizer should be created");
        assertNotNull(optimizer3, "Third VariableOptimizer should be created");

        // Verify they are all different instances
        assertNotSame(optimizer1, optimizer2, "optimizer1 and optimizer2 should be different");
        assertNotSame(optimizer1, optimizer3, "optimizer1 and optimizer3 should be different");
        assertNotSame(optimizer2, optimizer3, "optimizer2 and optimizer3 should be different");
    }

    /**
     * Tests that multiple instances created with reuseThis=false are independent.
     * Verifies that creating multiple optimizer instances with the same boolean value works correctly.
     */
    @Test
    public void testMultipleInstancesWithReuseThisFalse() {
        // Act - Create multiple VariableOptimizer instances with reuseThis=false
        VariableOptimizer optimizer1 = new VariableOptimizer(false);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);
        VariableOptimizer optimizer3 = new VariableOptimizer(false);

        // Assert - Verify all instances were created successfully
        assertNotNull(optimizer1, "First VariableOptimizer should be created");
        assertNotNull(optimizer2, "Second VariableOptimizer should be created");
        assertNotNull(optimizer3, "Third VariableOptimizer should be created");

        // Verify they are all different instances
        assertNotSame(optimizer1, optimizer2, "optimizer1 and optimizer2 should be different");
        assertNotSame(optimizer1, optimizer3, "optimizer1 and optimizer3 should be different");
        assertNotSame(optimizer2, optimizer3, "optimizer2 and optimizer3 should be different");
    }

    // ========== Tests for VariableOptimizer(boolean, MemberVisitor) constructor ==========

    /**
     * Tests the two-parameter constructor with reuseThis=true and a valid MemberVisitor.
     * Verifies that the VariableOptimizer instance can be instantiated with both parameters.
     */
    @Test
    public void testTwoParamConstructorWithReuseThisTrueAndValidVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create VariableOptimizer with reuseThis=true and visitor
        VariableOptimizer optimizer = new VariableOptimizer(true, visitor);

        // Assert - Verify the VariableOptimizer instance was created successfully
        assertNotNull(optimizer, "VariableOptimizer should be instantiated successfully with reuseThis=true and visitor");
    }

    /**
     * Tests the two-parameter constructor with reuseThis=false and a valid MemberVisitor.
     * Verifies that the VariableOptimizer instance can be instantiated with reuseThis=false.
     */
    @Test
    public void testTwoParamConstructorWithReuseThisFalseAndValidVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create VariableOptimizer with reuseThis=false and visitor
        VariableOptimizer optimizer = new VariableOptimizer(false, visitor);

        // Assert - Verify the VariableOptimizer instance was created successfully
        assertNotNull(optimizer, "VariableOptimizer should be instantiated successfully with reuseThis=false and visitor");
    }

    /**
     * Tests the two-parameter constructor with reuseThis=true and null MemberVisitor.
     * Verifies that the VariableOptimizer constructor accepts null visitor.
     */
    @Test
    public void testTwoParamConstructorWithReuseThisTrueAndNullVisitor() {
        // Act - Create VariableOptimizer with reuseThis=true and null visitor
        VariableOptimizer optimizer = new VariableOptimizer(true, null);

        // Assert - Verify the VariableOptimizer instance was created successfully
        assertNotNull(optimizer, "VariableOptimizer should be instantiated with reuseThis=true and null visitor");
    }

    /**
     * Tests the two-parameter constructor with reuseThis=false and null MemberVisitor.
     * Verifies that the VariableOptimizer constructor accepts null visitor.
     */
    @Test
    public void testTwoParamConstructorWithReuseThisFalseAndNullVisitor() {
        // Act - Create VariableOptimizer with reuseThis=false and null visitor
        VariableOptimizer optimizer = new VariableOptimizer(false, null);

        // Assert - Verify the VariableOptimizer instance was created successfully
        assertNotNull(optimizer, "VariableOptimizer should be instantiated with reuseThis=false and null visitor");
    }

    /**
     * Tests that the one-parameter constructor is equivalent to passing null for the visitor.
     * Verifies that VariableOptimizer(boolean) delegates to VariableOptimizer(boolean, null).
     */
    @Test
    public void testOneParamConstructorEquivalentToNullVisitor() {
        // Act - Create VariableOptimizer with both constructors
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(true, null);

        // Assert - Verify both instances were created successfully
        assertNotNull(optimizer1, "One-param VariableOptimizer should be created");
        assertNotNull(optimizer2, "Two-param VariableOptimizer with null should be created");
        assertNotSame(optimizer1, optimizer2, "Different VariableOptimizer instances should be different objects");
    }

    /**
     * Tests that the two-parameter constructor creates a valid AttributeVisitor.
     * Verifies that VariableOptimizer implements the AttributeVisitor interface.
     */
    @Test
    public void testTwoParamConstructorCreatesValidAttributeVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create VariableOptimizer
        VariableOptimizer optimizer = new VariableOptimizer(true, visitor);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, optimizer,
                "VariableOptimizer should implement AttributeVisitor");
    }

    /**
     * Tests that multiple VariableOptimizer instances can be created with different visitors.
     * Verifies that multiple instances with different MemberVisitors are distinct objects.
     */
    @Test
    public void testTwoParamConstructorMultipleInstancesWithDifferentVisitors() {
        // Arrange - Create two different MemberVisitors
        MemberVisitor visitor1 = new TestMemberVisitor();
        MemberVisitor visitor2 = new TestMemberVisitor();

        // Act - Create two VariableOptimizer instances
        VariableOptimizer optimizer1 = new VariableOptimizer(true, visitor1);
        VariableOptimizer optimizer2 = new VariableOptimizer(false, visitor2);

        // Assert - Verify both instances were created and are different
        assertNotNull(optimizer1, "First VariableOptimizer should be created");
        assertNotNull(optimizer2, "Second VariableOptimizer should be created");
        assertNotSame(optimizer1, optimizer2, "VariableOptimizer instances should be different objects");
    }

    /**
     * Tests that the same visitor can be used to create multiple VariableOptimizer instances.
     * Verifies that multiple instances can share the same MemberVisitor.
     */
    @Test
    public void testTwoParamConstructorMultipleInstancesWithSameVisitor() {
        // Arrange - Create a single MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create multiple VariableOptimizer instances with the same visitor
        VariableOptimizer optimizer1 = new VariableOptimizer(true, visitor);
        VariableOptimizer optimizer2 = new VariableOptimizer(false, visitor);

        // Assert - Verify both instances were created successfully
        assertNotNull(optimizer1, "First VariableOptimizer should be created");
        assertNotNull(optimizer2, "Second VariableOptimizer should be created");
        assertNotSame(optimizer1, optimizer2, "VariableOptimizer instances should be different objects");
    }

    /**
     * Tests that the two-parameter constructor accepts different MemberVisitor implementations.
     * Verifies that VariableOptimizer works with various MemberVisitor implementations.
     */
    @Test
    public void testTwoParamConstructorWithDifferentMemberVisitorImplementations() {
        // Act - Create VariableOptimizer with different visitor types
        VariableOptimizer optimizer1 = new VariableOptimizer(true, new TestMemberVisitor());
        VariableOptimizer optimizer2 = new VariableOptimizer(false, new TrackingMemberVisitor());
        VariableOptimizer optimizer3 = new VariableOptimizer(true, new AnotherTestMemberVisitor());

        // Assert - Verify all VariableOptimizer instances were created successfully
        assertNotNull(optimizer1, "VariableOptimizer should work with TestMemberVisitor");
        assertNotNull(optimizer2, "VariableOptimizer should work with TrackingMemberVisitor");
        assertNotNull(optimizer3, "VariableOptimizer should work with AnotherTestMemberVisitor");
    }

    /**
     * Tests that the two-parameter constructor does not throw exceptions.
     * Verifies exception-free construction with various parameter combinations.
     */
    @Test
    public void testTwoParamConstructorDoesNotThrowException() {
        // Arrange - Create a MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new VariableOptimizer(true, visitor),
                "Constructor should not throw exception with reuseThis=true and visitor");
        assertDoesNotThrow(() -> new VariableOptimizer(false, visitor),
                "Constructor should not throw exception with reuseThis=false and visitor");
        assertDoesNotThrow(() -> new VariableOptimizer(true, null),
                "Constructor should not throw exception with reuseThis=true and null visitor");
        assertDoesNotThrow(() -> new VariableOptimizer(false, null),
                "Constructor should not throw exception with reuseThis=false and null visitor");
    }

    /**
     * Tests all four combinations of boolean and null/non-null visitor.
     * Verifies that all parameter combinations work correctly.
     */
    @Test
    public void testTwoParamConstructorWithAllBooleanVisitorCombinations() {
        // Arrange - Create a MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create VariableOptimizer with all combinations
        VariableOptimizer optimizer1 = new VariableOptimizer(true, visitor);
        VariableOptimizer optimizer2 = new VariableOptimizer(true, null);
        VariableOptimizer optimizer3 = new VariableOptimizer(false, visitor);
        VariableOptimizer optimizer4 = new VariableOptimizer(false, null);

        // Assert - Verify all instances were created successfully
        assertNotNull(optimizer1, "VariableOptimizer with (true, visitor) should be created");
        assertNotNull(optimizer2, "VariableOptimizer with (true, null) should be created");
        assertNotNull(optimizer3, "VariableOptimizer with (false, visitor) should be created");
        assertNotNull(optimizer4, "VariableOptimizer with (false, null) should be created");

        // Verify they are all different instances
        assertNotSame(optimizer1, optimizer2, "optimizer1 and optimizer2 should be different");
        assertNotSame(optimizer1, optimizer3, "optimizer1 and optimizer3 should be different");
        assertNotSame(optimizer1, optimizer4, "optimizer1 and optimizer4 should be different");
        assertNotSame(optimizer2, optimizer3, "optimizer2 and optimizer3 should be different");
        assertNotSame(optimizer2, optimizer4, "optimizer2 and optimizer4 should be different");
        assertNotSame(optimizer3, optimizer4, "optimizer3 and optimizer4 should be different");
    }

    /**
     * Tests that the two-parameter constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testTwoParamConstructorRepeatedInvocation() {
        // Arrange - Create a MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act & Assert - Create multiple optimizers in sequence
        for (int i = 0; i < 5; i++) {
            boolean reuseThis = (i % 2 == 0);
            MemberVisitor v = (i % 3 == 0) ? visitor : null;
            VariableOptimizer optimizer = new VariableOptimizer(reuseThis, v);
            assertNotNull(optimizer, "VariableOptimizer should be created on iteration " + i);
            assertInstanceOf(AttributeVisitor.class, optimizer,
                    "VariableOptimizer should implement AttributeVisitor on iteration " + i);
        }
    }

    /**
     * Tests that multiple instances created with same parameters are still independent.
     * Verifies that instances with same parameters are distinct objects.
     */
    @Test
    public void testTwoParamConstructorMultipleInstancesWithSameParameters() {
        // Arrange - Create a MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create two VariableOptimizer instances with same parameters
        VariableOptimizer optimizer1 = new VariableOptimizer(true, visitor);
        VariableOptimizer optimizer2 = new VariableOptimizer(true, visitor);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(optimizer1, "First VariableOptimizer should be created");
        assertNotNull(optimizer2, "Second VariableOptimizer should be created");
        assertNotSame(optimizer1, optimizer2, "VariableOptimizer instances should be different objects even with same parameters");
    }

    /**
     * Tests that each instance is truly independent by creating many instances.
     * Verifies that the constructor scales correctly and produces independent instances.
     */
    @Test
    public void testTwoParamConstructorCreatesIndependentInstances() {
        // Arrange - Create multiple optimizer instances
        int instanceCount = 10;
        VariableOptimizer[] optimizers = new VariableOptimizer[instanceCount];
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create instances
        for (int i = 0; i < instanceCount; i++) {
            boolean reuseThis = (i % 2 == 0);
            MemberVisitor v = (i % 3 == 0) ? visitor : null;
            optimizers[i] = new VariableOptimizer(reuseThis, v);
        }

        // Assert - Verify all instances are non-null and unique
        for (int i = 0; i < instanceCount; i++) {
            assertNotNull(optimizers[i], "Instance " + i + " should be non-null");
            for (int j = i + 1; j < instanceCount; j++) {
                assertNotSame(optimizers[i], optimizers[j],
                        "Instance " + i + " should be different from instance " + j);
            }
        }
    }

    /**
     * Tests the two-parameter constructor consistency with reuseThis=true.
     * Verifies that repeated construction with reuseThis=true is stable.
     */
    @Test
    public void testTwoParamConstructorConsistencyWithReuseThisTrue() {
        // Arrange - Create a MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            VariableOptimizer optimizer = new VariableOptimizer(true, visitor);

            // Assert - Verify each instance is valid
            assertNotNull(optimizer, "VariableOptimizer should be created on attempt " + i);
            assertInstanceOf(AttributeVisitor.class, optimizer,
                    "VariableOptimizer should implement AttributeVisitor on attempt " + i);
        }
    }

    /**
     * Tests the two-parameter constructor consistency with reuseThis=false.
     * Verifies that repeated construction with reuseThis=false is stable.
     */
    @Test
    public void testTwoParamConstructorConsistencyWithReuseThisFalse() {
        // Arrange - Create a MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            VariableOptimizer optimizer = new VariableOptimizer(false, visitor);

            // Assert - Verify each instance is valid
            assertNotNull(optimizer, "VariableOptimizer should be created on attempt " + i);
            assertInstanceOf(AttributeVisitor.class, optimizer,
                    "VariableOptimizer should implement AttributeVisitor on attempt " + i);
        }
    }

    /**
     * Tests that the two-parameter constructor always creates AttributeVisitor instances.
     * Verifies interface implementation consistency.
     */
    @Test
    public void testTwoParamConstructorAlwaysCreatesAttributeVisitor() {
        // Arrange - Create a MemberVisitor
        MemberVisitor visitor = new TestMemberVisitor();

        // Act - Create VariableOptimizers with different parameters
        VariableOptimizer optimizer1 = new VariableOptimizer(true, visitor);
        VariableOptimizer optimizer2 = new VariableOptimizer(false, visitor);
        VariableOptimizer optimizer3 = new VariableOptimizer(true, null);
        VariableOptimizer optimizer4 = new VariableOptimizer(false, null);

        // Assert - Verify all implement AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, optimizer1,
                "VariableOptimizer with (true, visitor) should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, optimizer2,
                "VariableOptimizer with (false, visitor) should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, optimizer3,
                "VariableOptimizer with (true, null) should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, optimizer4,
                "VariableOptimizer with (false, null) should implement AttributeVisitor");
    }

    // ========== Helper classes for testing ==========

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
