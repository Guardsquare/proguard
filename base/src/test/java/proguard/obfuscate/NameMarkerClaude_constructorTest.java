package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NameMarker} constructor.
 * Tests the default constructor NameMarker().
 */
public class NameMarkerClaude_constructorTest {

    /**
     * Tests that the default constructor creates a valid NameMarker instance.
     * Verifies that the instance is not null.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act - Create a NameMarker using the default constructor
        NameMarker nameMarker = new NameMarker();

        // Assert - Verify the instance was created successfully
        assertNotNull(nameMarker, "NameMarker should be instantiated successfully");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor.
     * Verifies that NameMarker can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Act - Create a NameMarker
        NameMarker nameMarker = new NameMarker();

        // Assert - Verify the instance implements ClassVisitor
        assertInstanceOf(ClassVisitor.class, nameMarker,
                "NameMarker should implement ClassVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements MemberVisitor.
     * Verifies that NameMarker can be used as a MemberVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Act - Create a NameMarker
        NameMarker nameMarker = new NameMarker();

        // Assert - Verify the instance implements MemberVisitor
        assertInstanceOf(MemberVisitor.class, nameMarker,
                "NameMarker should implement MemberVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements AttributeVisitor.
     * Verifies that NameMarker can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Act - Create a NameMarker
        NameMarker nameMarker = new NameMarker();

        // Assert - Verify the instance implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, nameMarker,
                "NameMarker should implement AttributeVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements ConstantVisitor.
     * Verifies that NameMarker can be used as a ConstantVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfConstantVisitor() {
        // Act - Create a NameMarker
        NameMarker nameMarker = new NameMarker();

        // Assert - Verify the instance implements ConstantVisitor
        assertInstanceOf(ConstantVisitor.class, nameMarker,
                "NameMarker should implement ConstantVisitor interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testConstructorCreatesMultipleIndependentInstances() {
        // Act - Create multiple NameMarker instances
        NameMarker nameMarker1 = new NameMarker();
        NameMarker nameMarker2 = new NameMarker();

        // Assert - Verify both instances are created and are distinct
        assertNotNull(nameMarker1, "First NameMarker instance should be created");
        assertNotNull(nameMarker2, "Second NameMarker instance should be created");
        assertNotSame(nameMarker1, nameMarker2,
                "Multiple instances should be distinct objects");
    }
}
