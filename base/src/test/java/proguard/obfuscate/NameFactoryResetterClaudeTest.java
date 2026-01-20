package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameFactoryResetter}.
 * Tests the constructor and visitAnyClass method.
 */
public class NameFactoryResetterClaudeTest {

    /**
     * Tests the constructor with a valid NameFactory.
     * Verifies that the resetter can be instantiated with a valid name factory.
     */
    @Test
    public void testConstructorWithValidNameFactory() {
        // Arrange - Create a valid NameFactory
        NameFactory nameFactory = new SimpleNameFactory();

        // Act - Create NameFactoryResetter with valid parameter
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);

        // Assert - Verify the resetter was created successfully
        assertNotNull(resetter, "NameFactoryResetter should be instantiated successfully");
    }

    /**
     * Tests the constructor with null NameFactory.
     * Verifies that the constructor accepts a null name factory.
     */
    @Test
    public void testConstructorWithNullNameFactory() {
        // Act - Create resetter with null name factory
        NameFactoryResetter resetter = new NameFactoryResetter(null);

        // Assert - Verify the resetter was created
        assertNotNull(resetter, "NameFactoryResetter should be instantiated even with null name factory");
    }

    /**
     * Tests the constructor creates an instance that implements ClassVisitor interface.
     * Verifies that NameFactoryResetter can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Arrange - Create a valid NameFactory
        NameFactory nameFactory = new SimpleNameFactory();

        // Act - Create resetter
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);

        // Assert - Verify the resetter implements ClassVisitor
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, resetter,
                "NameFactoryResetter should implement ClassVisitor interface");
    }

    /**
     * Tests visitAnyClass method with a SimpleNameFactory.
     * Verifies that visitAnyClass resets the name factory by checking that
     * the factory generates the same sequence after a visit.
     */
    @Test
    public void testVisitAnyClassResetsSimpleNameFactory() {
        // Arrange - Create a SimpleNameFactory and get some names
        SimpleNameFactory nameFactory = new SimpleNameFactory();
        String firstName = nameFactory.nextName();
        String secondName = nameFactory.nextName();

        // Create the resetter and a mock class
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);
        Clazz mockClazz = mock(ProgramClass.class);

        // Act - Visit a class (which should reset the factory)
        resetter.visitAnyClass(mockClazz);

        // Get the next name after reset
        String nameAfterReset = nameFactory.nextName();

        // Assert - The name after reset should be the same as the first name
        assertEquals(firstName, nameAfterReset,
                "Name factory should be reset to start generating from the beginning");
        assertNotEquals(secondName, nameAfterReset,
                "Name after reset should not continue from where it left off");
    }

    /**
     * Tests visitAnyClass method with a ProgramClass.
     * Verifies that the method works correctly with a ProgramClass instance.
     */
    @Test
    public void testVisitAnyClassWithProgramClass() {
        // Arrange - Create a name factory and resetter
        NameFactory nameFactory = mock(NameFactory.class);
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);
        ProgramClass programClass = mock(ProgramClass.class);

        // Act - Visit the program class
        assertDoesNotThrow(() -> resetter.visitAnyClass(programClass),
                "Visiting a ProgramClass should not throw an exception");

        // Assert - Verify reset was called
        verify(nameFactory, times(1)).reset();
    }

    /**
     * Tests visitAnyClass method with a LibraryClass.
     * Verifies that the method works correctly with a LibraryClass instance.
     */
    @Test
    public void testVisitAnyClassWithLibraryClass() {
        // Arrange - Create a name factory and resetter
        NameFactory nameFactory = mock(NameFactory.class);
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);
        LibraryClass libraryClass = mock(LibraryClass.class);

        // Act - Visit the library class
        assertDoesNotThrow(() -> resetter.visitAnyClass(libraryClass),
                "Visiting a LibraryClass should not throw an exception");

        // Assert - Verify reset was called
        verify(nameFactory, times(1)).reset();
    }

    /**
     * Tests visitAnyClass method called multiple times.
     * Verifies that each call to visitAnyClass resets the factory.
     */
    @Test
    public void testVisitAnyClassCalledMultipleTimes() {
        // Arrange - Create a name factory and resetter
        NameFactory nameFactory = mock(NameFactory.class);
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);
        Clazz mockClazz1 = mock(ProgramClass.class);
        Clazz mockClazz2 = mock(ProgramClass.class);
        Clazz mockClazz3 = mock(ProgramClass.class);

        // Act - Visit multiple classes
        resetter.visitAnyClass(mockClazz1);
        resetter.visitAnyClass(mockClazz2);
        resetter.visitAnyClass(mockClazz3);

        // Assert - Verify reset was called three times
        verify(nameFactory, times(3)).reset();
    }

    /**
     * Tests visitAnyClass with null Clazz parameter.
     * Verifies behavior when visitAnyClass is called with null.
     */
    @Test
    public void testVisitAnyClassWithNullClazz() {
        // Arrange - Create a name factory and resetter
        NameFactory nameFactory = mock(NameFactory.class);
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);

        // Act & Assert - Visit with null (should not throw but should call reset)
        assertDoesNotThrow(() -> resetter.visitAnyClass(null),
                "Visiting with null Clazz should not throw an exception");

        verify(nameFactory, times(1)).reset();
    }

    /**
     * Tests that visitAnyClass with null NameFactory throws NullPointerException.
     * Verifies proper error handling when the factory is null.
     */
    @Test
    public void testVisitAnyClassWithNullNameFactory() {
        // Arrange - Create resetter with null name factory
        NameFactoryResetter resetter = new NameFactoryResetter(null);
        Clazz mockClazz = mock(ProgramClass.class);

        // Act & Assert - Visit should throw NullPointerException
        assertThrows(NullPointerException.class, () -> resetter.visitAnyClass(mockClazz),
                "Visiting a class with null NameFactory should throw NullPointerException");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each resetter instance is independent.
     */
    @Test
    public void testMultipleResetterInstances() {
        // Arrange - Create different name factories for each instance
        NameFactory nameFactory1 = new SimpleNameFactory();
        NameFactory nameFactory2 = new SimpleNameFactory();

        // Act - Create two resetter instances
        NameFactoryResetter resetter1 = new NameFactoryResetter(nameFactory1);
        NameFactoryResetter resetter2 = new NameFactoryResetter(nameFactory2);

        // Assert - Verify both resetters were created successfully
        assertNotNull(resetter1, "First resetter should be created");
        assertNotNull(resetter2, "Second resetter should be created");
        assertNotSame(resetter1, resetter2, "Resetter instances should be different objects");
    }

    /**
     * Tests the resetter with the same name factory used to create multiple resetters.
     * Verifies that the same name factory can be used for multiple resetters.
     */
    @Test
    public void testMultipleResettersWithSameNameFactory() {
        // Arrange - Create a single name factory to use for multiple resetters
        NameFactory nameFactory = new SimpleNameFactory();

        // Act - Create two resetter instances with the same name factory
        NameFactoryResetter resetter1 = new NameFactoryResetter(nameFactory);
        NameFactoryResetter resetter2 = new NameFactoryResetter(nameFactory);

        // Assert - Verify both resetters were created successfully
        assertNotNull(resetter1, "First resetter should be created");
        assertNotNull(resetter2, "Second resetter should be created");
        assertNotSame(resetter1, resetter2, "Resetter instances should be different objects");
    }

    /**
     * Tests visitAnyClass with multiple resetters sharing the same factory.
     * Verifies that both resetters can reset the shared factory.
     */
    @Test
    public void testVisitAnyClassWithSharedNameFactory() {
        // Arrange - Create a shared name factory
        SimpleNameFactory nameFactory = new SimpleNameFactory();
        NameFactoryResetter resetter1 = new NameFactoryResetter(nameFactory);
        NameFactoryResetter resetter2 = new NameFactoryResetter(nameFactory);
        Clazz mockClazz = mock(ProgramClass.class);

        // Advance the factory
        nameFactory.nextName();
        nameFactory.nextName();
        String thirdName = nameFactory.nextName();

        // Act - Reset using first resetter
        resetter1.visitAnyClass(mockClazz);
        String nameAfterFirstReset = nameFactory.nextName();

        // Advance the factory again
        nameFactory.nextName();
        nameFactory.nextName();

        // Reset using second resetter
        resetter2.visitAnyClass(mockClazz);
        String nameAfterSecondReset = nameFactory.nextName();

        // Assert - Both resets should work
        assertNotEquals(thirdName, nameAfterFirstReset,
                "First resetter should reset the factory");
        assertEquals(nameAfterFirstReset, nameAfterSecondReset,
                "Second resetter should also reset the factory to the beginning");
    }

    /**
     * Tests visitAnyClass with NumericNameFactory.
     * Verifies that the resetter works with different NameFactory implementations.
     */
    @Test
    public void testVisitAnyClassWithNumericNameFactory() {
        // Arrange - Create a NumericNameFactory and get some names
        NumericNameFactory nameFactory = new NumericNameFactory();
        String firstName = nameFactory.nextName();
        nameFactory.nextName();
        nameFactory.nextName();

        // Create the resetter and a mock class
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);
        Clazz mockClazz = mock(ProgramClass.class);

        // Act - Visit a class (which should reset the factory)
        resetter.visitAnyClass(mockClazz);
        String nameAfterReset = nameFactory.nextName();

        // Assert - The name after reset should be the same as the first name
        assertEquals(firstName, nameAfterReset,
                "NumericNameFactory should be reset to start generating from the beginning");
    }

    /**
     * Tests that the resetter works in a realistic scenario.
     * Verifies the resetter can be used in a visitor pattern with multiple classes.
     */
    @Test
    public void testRealisticVisitorPatternScenario() {
        // Arrange - Create a name factory that we can observe
        SimpleNameFactory nameFactory = new SimpleNameFactory();
        NameFactoryResetter resetter = new NameFactoryResetter(nameFactory);

        // Create multiple mock classes
        Clazz class1 = mock(ProgramClass.class);
        Clazz class2 = mock(ProgramClass.class);
        Clazz class3 = mock(ProgramClass.class);

        // Get the first name for reference
        String expectedFirstName = nameFactory.nextName();

        // Advance the factory
        nameFactory.nextName();
        nameFactory.nextName();

        // Act - Visit first class and check reset
        resetter.visitAnyClass(class1);
        String nameAfterVisit1 = nameFactory.nextName();

        // Advance factory again
        nameFactory.nextName();

        // Visit second class
        resetter.visitAnyClass(class2);
        String nameAfterVisit2 = nameFactory.nextName();

        // Visit third class
        resetter.visitAnyClass(class3);
        String nameAfterVisit3 = nameFactory.nextName();

        // Assert - Each visit should reset the factory
        assertEquals(expectedFirstName, nameAfterVisit1,
                "After first visit, factory should be reset");
        assertEquals(expectedFirstName, nameAfterVisit2,
                "After second visit, factory should be reset");
        assertEquals(expectedFirstName, nameAfterVisit3,
                "After third visit, factory should be reset");
    }

    /**
     * Tests the constructor with a custom NameFactory implementation.
     * Verifies that the resetter works with custom implementations.
     */
    @Test
    public void testConstructorWithCustomNameFactoryImplementation() {
        // Arrange - Create a custom implementation of NameFactory
        NameFactory customNameFactory = new NameFactory() {
            private int counter = 0;

            @Override
            public void reset() {
                counter = 0;
            }

            @Override
            public String nextName() {
                return "name" + (counter++);
            }
        };

        // Act - Create resetter with custom name factory
        NameFactoryResetter resetter = new NameFactoryResetter(customNameFactory);

        // Assert - Verify the resetter was created successfully
        assertNotNull(resetter, "NameFactoryResetter should be instantiated with custom name factory");
    }

    /**
     * Tests visitAnyClass with a custom NameFactory to verify reset behavior.
     * Verifies that the reset method is actually invoked on custom implementations.
     */
    @Test
    public void testVisitAnyClassWithCustomNameFactoryVerifiesReset() {
        // Arrange - Create a custom implementation that tracks reset calls
        final int[] resetCount = {0};
        NameFactory customNameFactory = new NameFactory() {
            private int counter = 0;

            @Override
            public void reset() {
                counter = 0;
                resetCount[0]++;
            }

            @Override
            public String nextName() {
                return "name" + (counter++);
            }
        };

        NameFactoryResetter resetter = new NameFactoryResetter(customNameFactory);
        Clazz mockClazz = mock(ProgramClass.class);

        // Act - Visit a class
        resetter.visitAnyClass(mockClazz);

        // Assert - Verify reset was called
        assertEquals(1, resetCount[0], "Reset should have been called once");
    }
}
