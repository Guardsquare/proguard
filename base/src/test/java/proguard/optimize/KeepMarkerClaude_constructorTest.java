package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#KeepMarker()} constructor.
 * Tests the initialization of the KeepMarker class using its default constructor.
 */
public class KeepMarkerClaude_constructorTest {

    /**
     * Tests that the default constructor creates a valid instance.
     * Verifies that a KeepMarker instance can be created.
     */
    @Test
    public void testDefaultConstructor() {
        // Act
        KeepMarker keepMarker = new KeepMarker();

        // Assert
        assertNotNull(keepMarker, "KeepMarker instance should not be null");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor interface.
     * Verifies that KeepMarker can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Act
        KeepMarker keepMarker = new KeepMarker();

        // Assert
        assertInstanceOf(ClassVisitor.class, keepMarker,
                "KeepMarker should implement ClassVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements MemberVisitor interface.
     * Verifies that KeepMarker can be used as a MemberVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Act
        KeepMarker keepMarker = new KeepMarker();

        // Assert
        assertInstanceOf(MemberVisitor.class, keepMarker,
                "KeepMarker should implement MemberVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements AttributeVisitor interface.
     * Verifies that KeepMarker can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Act
        KeepMarker keepMarker = new KeepMarker();

        // Assert
        assertInstanceOf(AttributeVisitor.class, keepMarker,
                "KeepMarker should implement AttributeVisitor interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each KeepMarker instance is independent.
     */
    @Test
    public void testMultipleInstances() {
        // Act
        KeepMarker keepMarker1 = new KeepMarker();
        KeepMarker keepMarker2 = new KeepMarker();

        // Assert
        assertNotNull(keepMarker1, "First KeepMarker should be created");
        assertNotNull(keepMarker2, "Second KeepMarker should be created");
        assertNotSame(keepMarker1, keepMarker2, "KeepMarker instances should be different objects");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions.
     * Verifies basic exception-free instantiation.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> new KeepMarker(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that the constructed instance can be assigned to all its interface types.
     * Verifies type compatibility with all implemented interfaces.
     */
    @Test
    public void testConstructorCreatesInstanceCompatibleWithAllInterfaces() {
        // Act
        KeepMarker keepMarker = new KeepMarker();

        // Assert - All these assignments should be valid
        ClassVisitor classVisitor = keepMarker;
        MemberVisitor memberVisitor = keepMarker;
        AttributeVisitor attributeVisitor = keepMarker;

        assertNotNull(classVisitor, "KeepMarker should be assignable to ClassVisitor");
        assertNotNull(memberVisitor, "KeepMarker should be assignable to MemberVisitor");
        assertNotNull(attributeVisitor, "KeepMarker should be assignable to AttributeVisitor");
    }

    /**
     * Tests that multiple consecutive instantiations work correctly.
     * Verifies that the constructor can be called repeatedly without issues.
     */
    @Test
    public void testMultipleConsecutiveInstantiations() {
        // Act - Create multiple instances in a loop
        KeepMarker[] keepMarkers = new KeepMarker[5];
        for (int i = 0; i < keepMarkers.length; i++) {
            keepMarkers[i] = new KeepMarker();
        }

        // Assert - All instances should be created and be distinct
        for (int i = 0; i < keepMarkers.length; i++) {
            assertNotNull(keepMarkers[i], "KeepMarker instance at index " + i + " should not be null");
            for (int j = i + 1; j < keepMarkers.length; j++) {
                assertNotSame(keepMarkers[i], keepMarkers[j],
                        "KeepMarker instances at index " + i + " and " + j + " should be different objects");
            }
        }
    }

    /**
     * Tests that the constructor initializes the object in a usable state.
     * Verifies that the created instance is ready to be used as a visitor.
     */
    @Test
    public void testConstructorCreatesUsableInstance() {
        // Act
        KeepMarker keepMarker = new KeepMarker();

        // Assert - The instance should be in a usable state
        assertNotNull(keepMarker, "KeepMarker instance should not be null");
        assertDoesNotThrow(() -> {
            // Verify it can be used as different visitor types
            ClassVisitor cv = keepMarker;
            MemberVisitor mv = keepMarker;
            AttributeVisitor av = keepMarker;
        }, "Created instance should be usable as all visitor types");
    }

    /**
     * Tests that the constructed instance can accept ClassVisitor method calls.
     * Verifies the constructor properly initializes for ClassVisitor usage.
     */
    @Test
    public void testConstructorAllowsClassVisitorMethodCalls() {
        // Arrange
        KeepMarker keepMarker = new KeepMarker();
        Clazz mockClazz = mock(ProgramClass.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitAnyClass(mockClazz),
                "visitAnyClass should be callable on constructed instance");
    }

    /**
     * Tests that the constructed instance can accept MemberVisitor method calls for fields.
     * Verifies the constructor properly initializes for MemberVisitor usage with fields.
     */
    @Test
    public void testConstructorAllowsMemberVisitorFieldMethodCalls() {
        // Arrange
        KeepMarker keepMarker = new KeepMarker();
        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramField mockField = mock(ProgramField.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitProgramField(mockClass, mockField),
                "visitProgramField should be callable on constructed instance");
    }

    /**
     * Tests that the constructed instance can accept MemberVisitor method calls for methods.
     * Verifies the constructor properly initializes for MemberVisitor usage with methods.
     */
    @Test
    public void testConstructorAllowsMemberVisitorMethodMethodCalls() {
        // Arrange
        KeepMarker keepMarker = new KeepMarker();
        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramMethod mockMethod = mock(ProgramMethod.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitProgramMethod(mockClass, mockMethod),
                "visitProgramMethod should be callable on constructed instance");
    }

    /**
     * Tests that the constructed instance can accept AttributeVisitor method calls.
     * Verifies the constructor properly initializes for AttributeVisitor usage.
     */
    @Test
    public void testConstructorAllowsAttributeVisitorMethodCalls() {
        // Arrange
        KeepMarker keepMarker = new KeepMarker();
        Clazz mockClazz = mock(Clazz.class);
        Attribute mockAttribute = mock(Attribute.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(mockClazz, mockAttribute),
                "visitAnyAttribute should be callable on constructed instance");
    }

    /**
     * Tests that the constructed instance can accept AttributeVisitor method calls for CodeAttribute.
     * Verifies the constructor properly initializes for visiting code attributes.
     */
    @Test
    public void testConstructorAllowsCodeAttributeVisitorMethodCalls() {
        // Arrange
        KeepMarker keepMarker = new KeepMarker();
        Clazz mockClazz = mock(Clazz.class);
        Method mockMethod = mock(Method.class);
        CodeAttribute mockCodeAttribute = mock(CodeAttribute.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitCodeAttribute(mockClazz, mockMethod, mockCodeAttribute),
                "visitCodeAttribute should be callable on constructed instance");
    }

    /**
     * Tests that the constructed instance can accept library class member visitor calls.
     * Verifies the constructor properly initializes for visiting library members.
     */
    @Test
    public void testConstructorAllowsLibraryMemberVisitorCalls() {
        // Arrange
        KeepMarker keepMarker = new KeepMarker();
        LibraryClass mockLibraryClass = mock(LibraryClass.class);
        LibraryField mockLibraryField = mock(LibraryField.class);
        LibraryMethod mockLibraryMethod = mock(LibraryMethod.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> {
            keepMarker.visitLibraryField(mockLibraryClass, mockLibraryField);
            keepMarker.visitLibraryMethod(mockLibraryClass, mockLibraryMethod);
        }, "Library member visitor methods should be callable on constructed instance");
    }

    /**
     * Tests that the default constructor produces consistent instances.
     * Verifies that all instances created by the constructor are of the same type.
     */
    @Test
    public void testConstructorProducesConsistentInstances() {
        // Act
        KeepMarker keepMarker1 = new KeepMarker();
        KeepMarker keepMarker2 = new KeepMarker();

        // Assert
        assertEquals(keepMarker1.getClass(), keepMarker2.getClass(),
                "All instances should be of the same class");
        assertEquals(KeepMarker.class, keepMarker1.getClass(),
                "Instance should be of KeepMarker class");
        assertEquals(KeepMarker.class, keepMarker2.getClass(),
                "Instance should be of KeepMarker class");
    }
}
