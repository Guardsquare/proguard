package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link RenamedFlagSetter} default constructor.
 * Tests RenamedFlagSetter() constructor.
 */
public class RenamedFlagSetterClaude_constructorTest {

    /**
     * Tests that the default constructor creates a valid RenamedFlagSetter instance.
     * Verifies the object is instantiated successfully.
     */
    @Test
    public void testDefaultConstructorCreatesInstance() {
        // Act - Create RenamedFlagSetter using default constructor
        RenamedFlagSetter setter = new RenamedFlagSetter();

        // Assert - Verify the setter was created successfully
        assertNotNull(setter, "RenamedFlagSetter should be instantiated successfully");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor interface.
     * Verifies RenamedFlagSetter can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Act - Create setter
        RenamedFlagSetter setter = new RenamedFlagSetter();

        // Assert - Verify the setter implements ClassVisitor
        assertInstanceOf(ClassVisitor.class, setter,
                "RenamedFlagSetter should implement ClassVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements MemberVisitor interface.
     * Verifies RenamedFlagSetter can be used as a MemberVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Act - Create setter
        RenamedFlagSetter setter = new RenamedFlagSetter();

        // Assert - Verify the setter implements MemberVisitor
        assertInstanceOf(MemberVisitor.class, setter,
                "RenamedFlagSetter should implement MemberVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements AttributeVisitor interface.
     * Verifies RenamedFlagSetter can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Act - Create setter
        RenamedFlagSetter setter = new RenamedFlagSetter();

        // Assert - Verify the setter implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, setter,
                "RenamedFlagSetter should implement AttributeVisitor interface");
    }

    /**
     * Tests that the constructor properly initializes the setter for use with visitAnyClass.
     * Verifies the setter can visit a class without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingAnyClass() {
        // Arrange - Create a setter and a mock Clazz
        RenamedFlagSetter setter = new RenamedFlagSetter();
        Clazz clazz = mock(Clazz.class);

        // Act & Assert - Visit the class (this should not throw an exception)
        assertDoesNotThrow(() -> setter.visitAnyClass(clazz),
                "Setter constructed successfully should handle visitAnyClass without throwing");
    }

    /**
     * Tests that the constructor properly initializes the setter for use with visitProgramClass.
     * Verifies the setter can visit a ProgramClass without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingProgramClass() {
        // Arrange - Create a setter and a mock ProgramClass
        RenamedFlagSetter setter = new RenamedFlagSetter();
        ProgramClass programClass = mock(ProgramClass.class);

        // Set up mock behavior to avoid NullPointerException
        when(programClass.getName()).thenReturn("TestClass");

        // Act & Assert - Visit the program class (this should not throw an exception)
        assertDoesNotThrow(() -> setter.visitProgramClass(programClass),
                "Setter constructed successfully should handle visitProgramClass without throwing");
    }

    /**
     * Tests that the constructor properly initializes the setter for use with visitProgramMember.
     * Verifies the setter can visit a ProgramField without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingProgramField() {
        // Arrange - Create a setter and mock objects
        RenamedFlagSetter setter = new RenamedFlagSetter();
        ProgramClass programClass = mock(ProgramClass.class);
        ProgramField programField = mock(ProgramField.class);

        // Set up mock behavior
        when(programField.getName(programClass)).thenReturn("testField");

        // Act & Assert - Visit the program field (this should not throw an exception)
        assertDoesNotThrow(() -> setter.visitProgramField(programClass, programField),
                "Setter constructed successfully should handle visitProgramField without throwing");
    }

    /**
     * Tests that the constructor properly initializes the setter for use with visitProgramMember.
     * Verifies the setter can visit a ProgramMethod without throwing exceptions.
     */
    @Test
    public void testConstructorAllowsVisitingProgramMethod() {
        // Arrange - Create a setter and mock objects
        RenamedFlagSetter setter = new RenamedFlagSetter();
        ProgramClass programClass = mock(ProgramClass.class);
        ProgramMethod programMethod = mock(ProgramMethod.class);

        // Set up mock behavior
        when(programMethod.getName(programClass)).thenReturn("testMethod");

        // Act & Assert - Visit the program method (this should not throw an exception)
        assertDoesNotThrow(() -> setter.visitProgramMethod(programClass, programMethod),
                "Setter constructed successfully should handle visitProgramMethod without throwing");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each setter instance is independent.
     */
    @Test
    public void testMultipleSetterInstances() {
        // Act - Create two setter instances
        RenamedFlagSetter setter1 = new RenamedFlagSetter();
        RenamedFlagSetter setter2 = new RenamedFlagSetter();

        // Assert - Verify both setters were created successfully
        assertNotNull(setter1, "First setter should be created");
        assertNotNull(setter2, "Second setter should be created");
        assertNotSame(setter1, setter2, "Setter instances should be different objects");
    }

    /**
     * Tests that the constructed setter is ready to be used in a visitor chain.
     * Verifies the setter can be used as part of a visitor pattern.
     */
    @Test
    public void testConstructorCreatesVisitorReadyForChaining() {
        // Arrange - Create setter
        RenamedFlagSetter setter = new RenamedFlagSetter();

        // Act - Use setter in different visitor contexts
        ClassVisitor classVisitor = setter;
        MemberVisitor memberVisitor = setter;
        AttributeVisitor attributeVisitor = setter;

        // Assert - Verify the same instance can be used in different visitor contexts
        assertNotNull(classVisitor, "Should be usable as ClassVisitor");
        assertNotNull(memberVisitor, "Should be usable as MemberVisitor");
        assertNotNull(attributeVisitor, "Should be usable as AttributeVisitor");
        assertSame(setter, classVisitor, "ClassVisitor reference should point to same instance");
        assertSame(setter, memberVisitor, "MemberVisitor reference should point to same instance");
        assertSame(setter, attributeVisitor, "AttributeVisitor reference should point to same instance");
    }

    /**
     * Tests that the default constructor creates a stateless visitor.
     * Verifies that multiple visits can be performed with the same instance.
     */
    @Test
    public void testConstructorCreatesStatelessVisitor() {
        // Arrange - Create a single setter and multiple mock objects
        RenamedFlagSetter setter = new RenamedFlagSetter();

        ProgramClass programClass1 = mock(ProgramClass.class);
        when(programClass1.getName()).thenReturn("Class1");

        ProgramClass programClass2 = mock(ProgramClass.class);
        when(programClass2.getName()).thenReturn("Class2");

        // Act - Visit multiple classes with the same setter instance
        assertDoesNotThrow(() -> {
            setter.visitProgramClass(programClass1);
            setter.visitProgramClass(programClass2);
        }, "Setter should handle multiple visits without issues");

        // Assert - Verify the setter is still valid after multiple visits
        assertNotNull(setter, "Setter should remain valid after multiple visits");
    }
}
