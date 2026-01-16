package proguard.ant;

import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.setImplements(String) method.
 */
public class ClassSpecificationElementClaude_setImplementsTest {

    /**
     * Test that setImplements accepts a simple interface name.
     */
    @Test
    public void testSetImplementsWithSimpleInterfaceName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setImplements("Serializable"),
            "Should accept simple interface name");
    }

    /**
     * Test that setImplements accepts a fully qualified interface name.
     */
    @Test
    public void testSetImplementsWithFullyQualifiedInterfaceName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setImplements("java.io.Serializable"),
            "Should accept fully qualified interface name");
    }

    /**
     * Test that setImplements accepts null value.
     */
    @Test
    public void testSetImplementsWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setImplements(null),
            "Should accept null implements interface");
    }

    /**
     * Test that setImplements accepts an empty string.
     */
    @Test
    public void testSetImplementsWithEmptyString() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setImplements(""),
            "Should accept empty string implements interface");
    }

    /**
     * Test that setImplements stores simple interface name correctly.
     */
    @Test
    public void testSetImplementsStoresSimpleInterfaceName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be stored in extendsClassName");
        assertEquals("Serializable", spec.extendsClassName,
            "Simple implements interface name should be stored as-is");
    }

    /**
     * Test that setImplements converts fully qualified name to internal format.
     */
    @Test
    public void testSetImplementsConvertsToInternalFormat() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be stored");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "Fully qualified implements name should be converted to internal format (dots to slashes)");
    }

    /**
     * Test that setImplements with null results in null interface name.
     */
    @Test
    public void testSetImplementsWithNullResultsInNullInterfaceName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.extendsClassName, "Null implements should result in null extendsClassName");
    }

    /**
     * Test that setImplements with empty string results in empty interface name.
     */
    @Test
    public void testSetImplementsWithEmptyStringStoresEmptyName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Empty string should result in non-null extendsClassName");
        assertEquals("", spec.extendsClassName, "Empty string should be stored as empty");
    }

    /**
     * Test that setImplements can be called multiple times (last value wins).
     */
    @Test
    public void testSetImplementsMultipleTimes() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");
        element.setImplements("java.lang.Cloneable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be stored");
        assertEquals("java/lang/Cloneable", spec.extendsClassName,
            "Last set implements interface should be used");
    }

    /**
     * Test that setImplements can override with null.
     */
    @Test
    public void testSetImplementsOverrideWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");
        element.setImplements(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.extendsClassName, "Setting null should override previous implements interface");
    }

    /**
     * Test that setImplements works independently of other element configuration.
     */
    @Test
    public void testSetImplementsIndependentOfOtherConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");
        element.setAnnotation("MyAnnotation");
        element.setType("class");
        element.setExtendsannotation("InterfaceAnnotation");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "Implements interface should be correctly stored alongside other configuration");
    }

    /**
     * Test that setImplements doesn't affect class annotation.
     */
    @Test
    public void testSetImplementsDoesNotAffectClassAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("MyAnnotation");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Class annotation should be set");
        assertEquals("LMyAnnotation;", spec.annotationType, "Class annotation should be correct");
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
    }

    /**
     * Test that setImplements doesn't affect extends annotation.
     */
    @Test
    public void testSetImplementsDoesNotAffectExtendsAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("InterfaceAnnotation");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LInterfaceAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correct");
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "Implements interface name should be correct");
    }

    /**
     * Test that setImplements doesn't affect access flags.
     */
    @Test
    public void testSetImplementsDoesNotAffectAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public,final");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
        assertTrue(spec.requiredSetAccessFlags != 0, "Access flags should still be set");
    }

    /**
     * Test that setImplements doesn't affect class type.
     */
    @Test
    public void testSetImplementsDoesNotAffectType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("class");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
        // Type "class" doesn't set specific flags, but verify it doesn't break anything
        assertNotNull(spec, "ClassSpecification should be created");
    }

    /**
     * Test that setImplements with nested interface name using dollar sign.
     */
    @Test
    public void testSetImplementsWithNestedInterface() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.OuterInterface$InnerInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Nested interface name should be stored");
        assertEquals("com/example/OuterInterface$InnerInterface", spec.extendsClassName,
            "Nested interface name should preserve dollar sign and convert dots to slashes");
    }

    /**
     * Test that setImplements with single character name.
     */
    @Test
    public void testSetImplementsWithSingleCharacter() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("I");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Single character interface name should be stored");
        assertEquals("I", spec.extendsClassName, "Single character name should be stored as-is");
    }

    /**
     * Test that setImplements with wildcard pattern.
     */
    @Test
    public void testSetImplementsWithWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.*Interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Wildcard pattern should be stored");
        assertEquals("com/example/*Interface", spec.extendsClassName,
            "Wildcard should be preserved and dots converted to slashes");
    }

    /**
     * Test that setImplements with double wildcard pattern.
     */
    @Test
    public void testSetImplementsWithDoubleWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.**");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Double wildcard pattern should be stored");
        assertEquals("com/example/**", spec.extendsClassName,
            "Double wildcard should be preserved and dots converted to slashes");
    }

    /**
     * Test that setImplements doesn't affect class name.
     */
    @Test
    public void testSetImplementsDoesNotAffectClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be set");
        assertEquals("com/example/TestClass", spec.className, "Class name should not be affected");
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
    }

    /**
     * Test that setImplements doesn't interfere with field specifications.
     */
    @Test
    public void testSetImplementsDoesNotAffectFieldSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("myField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
        assertNotNull(spec.fieldSpecifications, "Field specifications should be set");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that setImplements doesn't interfere with method specifications.
     */
    @Test
    public void testSetImplementsDoesNotAffectMethodSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("myMethod");
        element.addConfiguredMethod(method);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements interface name should be set");
        assertNotNull(spec.methodSpecifications, "Method specifications should be set");
        assertEquals(1, spec.methodSpecifications.size(), "Should have one method specification");
    }

    /**
     * Test that setImplements with numeric characters.
     */
    @Test
    public void testSetImplementsWithNumericCharacters() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.Interface123");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Interface name with numbers should be stored");
        assertEquals("com/example/Interface123", spec.extendsClassName,
            "Interface name with numbers should be converted correctly");
    }

    /**
     * Test that setImplements with underscores.
     */
    @Test
    public void testSetImplementsWithUnderscores() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.My_Interface_Name");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Interface name with underscores should be stored");
        assertEquals("com/example/My_Interface_Name", spec.extendsClassName,
            "Interface name with underscores should be converted correctly");
    }

    /**
     * Test that setImplements with spaces is accepted (even if unusual).
     */
    @Test
    public void testSetImplementsWithSpaces() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.My Interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Interface name with spaces should be stored");
        assertTrue(spec.extendsClassName.contains(" "), "Spaces should be preserved");
    }

    /**
     * Test that setImplements with very long qualified name.
     */
    @Test
    public void testSetImplementsWithVeryLongQualifiedName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.very.long.package.name.hierarchy.MyInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Long qualified interface name should be stored");
        assertEquals("com/example/very/long/package/name/hierarchy/MyInterface", spec.extendsClassName,
            "Long qualified interface name should be converted correctly");
    }

    /**
     * Test that setImplements with java.io.Serializable.
     */
    @Test
    public void testSetImplementsWithJavaIoSerializable() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "java.io.Serializable should be stored");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "java.io.Serializable should be converted correctly");
    }

    /**
     * Test that setImplements with java.lang.Cloneable.
     */
    @Test
    public void testSetImplementsWithJavaLangCloneable() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.lang.Cloneable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "java.lang.Cloneable should be stored");
        assertEquals("java/lang/Cloneable", spec.extendsClassName,
            "java.lang.Cloneable should be converted correctly");
    }

    /**
     * Test that setImplements is called successfully on an element without name.
     */
    @Test
    public void testSetImplementsOnElementWithoutName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements should be set even without class name");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "Implements should be correctly stored");
    }

    /**
     * Test that setImplements with multiple consecutive dots.
     */
    @Test
    public void testSetImplementsWithConsecutiveDots() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com..example.MyInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Interface with consecutive dots should be stored");
        assertEquals("com//example/MyInterface", spec.extendsClassName,
            "Consecutive dots should be converted to consecutive slashes");
    }

    /**
     * Test that setImplements with question mark wildcard.
     */
    @Test
    public void testSetImplementsWithQuestionMarkWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.?Interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Question mark wildcard should be stored");
        assertEquals("com/example/?Interface", spec.extendsClassName,
            "Question mark wildcard should be preserved");
    }

    /**
     * Test that setImplements with multiple wildcards.
     */
    @Test
    public void testSetImplementsWithMultipleWildcards() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.*.example.*Interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Multiple wildcards should be stored");
        assertEquals("com/*/example/*Interface", spec.extendsClassName,
            "Multiple wildcards should be preserved and dots converted to slashes");
    }

    /**
     * Test that setImplements with package only (no interface).
     */
    @Test
    public void testSetImplementsWithPackageOnly() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Package name should be stored");
        assertEquals("com/example", spec.extendsClassName,
            "Package name should be converted to internal format");
    }

    /**
     * Test that setImplements followed by setExtends (they use the same field).
     * setExtends should override setImplements.
     */
    @Test
    public void testSetImplementsFollowedBySetExtends() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends/implements class name should be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName,
            "setExtends should override setImplements (they use the same field)");
    }

    /**
     * Test that setImplements with trailing wildcard.
     */
    @Test
    public void testSetImplementsWithTrailingWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("com.example.*");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Trailing wildcard should be stored");
        assertEquals("com/example/*", spec.extendsClassName,
            "Trailing wildcard should be preserved");
    }

    /**
     * Test that setImplements with leading wildcard.
     */
    @Test
    public void testSetImplementsWithLeadingWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("*.MyInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Leading wildcard should be stored");
        assertEquals("*/MyInterface", spec.extendsClassName,
            "Leading wildcard should be preserved");
    }

    /**
     * Test that setImplements uses extendsClassName field (not a separate field).
     * This verifies the implementation detail that implements uses the extends mechanism.
     */
    @Test
    public void testSetImplementsUsesExtendsClassNameField() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        // The implements value should be in extendsClassName, not a separate field
        assertNotNull(spec.extendsClassName,
            "Implements should use extendsClassName field");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "Implements value should be stored in extendsClassName");
    }

    /**
     * Test that setImplements on an interface type (interface extending interface).
     */
    @Test
    public void testSetImplementsOnInterfaceType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestInterface");
        element.setType("interface");
        element.setImplements("com.example.BaseInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Interface implements/extends should be stored");
        assertEquals("com/example/BaseInterface", spec.extendsClassName,
            "Interface implements should be converted correctly");
    }
}
