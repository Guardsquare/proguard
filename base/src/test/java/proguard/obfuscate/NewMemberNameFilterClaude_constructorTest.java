package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NewMemberNameFilter} constructor.
 * Tests NewMemberNameFilter(MemberVisitor) constructor.
 */
public class NewMemberNameFilterClaude_constructorTest {

    /**
     * Tests the constructor with a valid MemberVisitor.
     * Verifies that the filter can be instantiated with a valid member visitor.
     */
    @Test
    public void testConstructorWithValidMemberVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor memberVisitor = mock(MemberVisitor.class);

        // Act - Create NewMemberNameFilter with valid parameter
        NewMemberNameFilter filter = new NewMemberNameFilter(memberVisitor);

        // Assert - Verify the filter was created successfully
        assertNotNull(filter, "NewMemberNameFilter should be instantiated successfully");
    }

    /**
     * Tests the constructor with null MemberVisitor.
     * Verifies that the constructor accepts a null member visitor.
     */
    @Test
    public void testConstructorWithNullMemberVisitor() {
        // Act - Create filter with null member visitor
        NewMemberNameFilter filter = new NewMemberNameFilter(null);

        // Assert - Verify the filter was created
        assertNotNull(filter, "NewMemberNameFilter should be instantiated even with null member visitor");
    }

    /**
     * Tests the constructor creates an instance that implements MemberVisitor interface.
     * Verifies that NewMemberNameFilter can be used as a MemberVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Arrange - Create a valid MemberVisitor
        MemberVisitor memberVisitor = mock(MemberVisitor.class);

        // Act - Create filter
        NewMemberNameFilter filter = new NewMemberNameFilter(memberVisitor);

        // Assert - Verify the filter implements MemberVisitor
        assertInstanceOf(MemberVisitor.class, filter,
                "NewMemberNameFilter should implement MemberVisitor interface");
    }

    /**
     * Tests that the constructor properly initializes the filter for use with visit methods.
     * Verifies the filter can be used to visit a ProgramField without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingProgramField() {
        // Arrange - Create a mock member visitor and a filter
        MemberVisitor memberVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(memberVisitor);

        // Create a mock ProgramClass and ProgramField
        ProgramClass programClass = mock(ProgramClass.class);
        ProgramField programField = mock(ProgramField.class);

        // Act - Visit the program field (this should not throw an exception)
        assertDoesNotThrow(() -> filter.visitProgramField(programClass, programField),
                "Filter constructed successfully should handle visitProgramField without throwing");

        // Assert - Verify the filter was constructed properly
        assertNotNull(filter, "Filter should be properly constructed to handle visits");
    }

    /**
     * Tests that the constructor properly initializes the filter for use with visit methods.
     * Verifies the filter can be used to visit a ProgramMethod without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingProgramMethod() {
        // Arrange - Create a mock member visitor and a filter
        MemberVisitor memberVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(memberVisitor);

        // Create a mock ProgramClass and ProgramMethod
        ProgramClass programClass = mock(ProgramClass.class);
        ProgramMethod programMethod = mock(ProgramMethod.class);

        // Act - Visit the program method (this should not throw an exception)
        assertDoesNotThrow(() -> filter.visitProgramMethod(programClass, programMethod),
                "Filter constructed successfully should handle visitProgramMethod without throwing");

        // Assert - Verify the filter was constructed properly
        assertNotNull(filter, "Filter should be properly constructed to handle visits");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each filter instance is independent.
     */
    @Test
    public void testMultipleFilterInstances() {
        // Arrange - Create different member visitors for each instance
        MemberVisitor memberVisitor1 = mock(MemberVisitor.class);
        MemberVisitor memberVisitor2 = mock(MemberVisitor.class);

        // Act - Create two filter instances
        NewMemberNameFilter filter1 = new NewMemberNameFilter(memberVisitor1);
        NewMemberNameFilter filter2 = new NewMemberNameFilter(memberVisitor2);

        // Assert - Verify both filters were created successfully
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotSame(filter1, filter2, "Filter instances should be different objects");
    }

    /**
     * Tests the constructor with the same member visitor used to create multiple filters.
     * Verifies that the same member visitor can be used for multiple filters.
     */
    @Test
    public void testMultipleFiltersWithSameMemberVisitor() {
        // Arrange - Create a single member visitor to use for multiple filters
        MemberVisitor memberVisitor = mock(MemberVisitor.class);

        // Act - Create two filter instances with the same member visitor
        NewMemberNameFilter filter1 = new NewMemberNameFilter(memberVisitor);
        NewMemberNameFilter filter2 = new NewMemberNameFilter(memberVisitor);

        // Assert - Verify both filters were created successfully
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotSame(filter1, filter2, "Filter instances should be different objects");
    }

    /**
     * Tests the constructor with a concrete MemberVisitor implementation.
     * Verifies that the filter works with actual visitor implementations.
     */
    @Test
    public void testConstructorWithConcreteMemberVisitorImplementation() {
        // Arrange - Create a concrete implementation of MemberVisitor
        MemberVisitor concreteMemberVisitor = new MemberVisitor() {
            @Override
            public void visitProgramField(ProgramClass programClass, ProgramField programField) {
                // Simple implementation for testing
            }

            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                // Simple implementation for testing
            }

            @Override
            public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
                // Simple implementation for testing
            }

            @Override
            public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
                // Simple implementation for testing
            }
        };

        // Act - Create filter with concrete member visitor
        NewMemberNameFilter filter = new NewMemberNameFilter(concreteMemberVisitor);

        // Assert - Verify the filter was created successfully
        assertNotNull(filter, "NewMemberNameFilter should be instantiated with concrete member visitor");
    }

    /**
     * Tests the constructor with another NewMemberNameFilter as the member visitor.
     * Verifies that filters can be chained together.
     */
    @Test
    public void testConstructorWithChainedFilters() {
        // Arrange - Create a chain of filters
        MemberVisitor innerVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter innerFilter = new NewMemberNameFilter(innerVisitor);

        // Act - Create outer filter with inner filter as the member visitor
        NewMemberNameFilter outerFilter = new NewMemberNameFilter(innerFilter);

        // Assert - Verify the outer filter was created successfully
        assertNotNull(outerFilter, "Outer filter should be created");
        assertNotNull(innerFilter, "Inner filter should exist");
        assertNotSame(outerFilter, innerFilter, "Chained filters should be different objects");
    }

    /**
     * Tests that the constructor properly initializes the filter for use with LibraryField visits.
     * Verifies the filter can be used to visit a LibraryField without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingLibraryField() {
        // Arrange - Create a mock member visitor and a filter
        MemberVisitor memberVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(memberVisitor);

        // Create a mock LibraryClass and LibraryField
        LibraryClass libraryClass = mock(LibraryClass.class);
        LibraryField libraryField = mock(LibraryField.class);

        // Act - Visit the library field (this should not throw an exception)
        assertDoesNotThrow(() -> filter.visitLibraryField(libraryClass, libraryField),
                "Filter constructed successfully should handle visitLibraryField without throwing");

        // Assert - Verify the filter was constructed properly
        assertNotNull(filter, "Filter should be properly constructed to handle library field visits");
    }

    /**
     * Tests that the constructor properly initializes the filter for use with LibraryMethod visits.
     * Verifies the filter can be used to visit a LibraryMethod without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingLibraryMethod() {
        // Arrange - Create a mock member visitor and a filter
        MemberVisitor memberVisitor = mock(MemberVisitor.class);
        NewMemberNameFilter filter = new NewMemberNameFilter(memberVisitor);

        // Create a mock LibraryClass and LibraryMethod
        LibraryClass libraryClass = mock(LibraryClass.class);
        LibraryMethod libraryMethod = mock(LibraryMethod.class);

        // Act - Visit the library method (this should not throw an exception)
        assertDoesNotThrow(() -> filter.visitLibraryMethod(libraryClass, libraryMethod),
                "Filter constructed successfully should handle visitLibraryMethod without throwing");

        // Assert - Verify the filter was constructed properly
        assertNotNull(filter, "Filter should be properly constructed to handle library method visits");
    }
}
