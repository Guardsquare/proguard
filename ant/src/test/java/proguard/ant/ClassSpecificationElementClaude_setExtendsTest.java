package proguard.ant;

import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.setExtends(String) method.
 */
public class ClassSpecificationElementClaude_setExtendsTest {

    /**
     * Test that setExtends accepts a simple class name.
     */
    @Test
    public void testSetExtendsWithSimpleClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtends("BaseClass"),
            "Should accept simple class name");
    }

    /**
     * Test that setExtends accepts a fully qualified class name.
     */
    @Test
    public void testSetExtendsWithFullyQualifiedClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtends("com.example.BaseClass"),
            "Should accept fully qualified class name");
    }

    /**
     * Test that setExtends accepts null value.
     */
    @Test
    public void testSetExtendsWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtends(null),
            "Should accept null extends class");
    }

    /**
     * Test that setExtends accepts an empty string.
     */
    @Test
    public void testSetExtendsWithEmptyString() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtends(""),
            "Should accept empty string extends class");
    }

    /**
     * Test that setExtends stores simple class name correctly.
     */
    @Test
    public void testSetExtendsStoresSimpleClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be stored");
        assertEquals("BaseClass", spec.extendsClassName,
            "Simple extends class name should be stored as-is");
    }

    /**
     * Test that setExtends converts fully qualified name to internal format.
     */
    @Test
    public void testSetExtendsConvertsToInternalFormat() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be stored");
        assertEquals("com/example/BaseClass", spec.extendsClassName,
            "Fully qualified extends name should be converted to internal format (dots to slashes)");
    }

    /**
     * Test that setExtends with null results in null extends class name.
     */
    @Test
    public void testSetExtendsWithNullResultsInNullClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.extendsClassName, "Null extends should result in null extends class name");
    }

    /**
     * Test that setExtends with empty string results in empty extends class name.
     */
    @Test
    public void testSetExtendsWithEmptyStringStoresEmptyName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Empty string should result in non-null extends class name");
        assertEquals("", spec.extendsClassName, "Empty string should be stored as empty");
    }

    /**
     * Test that setExtends can be called multiple times (last value wins).
     */
    @Test
    public void testSetExtendsMultipleTimes() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.FirstBase");
        element.setExtends("com.example.SecondBase");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be stored");
        assertEquals("com/example/SecondBase", spec.extendsClassName,
            "Last set extends class should be used");
    }

    /**
     * Test that setExtends can override with null.
     */
    @Test
    public void testSetExtendsOverrideWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");
        element.setExtends(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.extendsClassName, "Setting null should override previous extends class");
    }

    /**
     * Test that setExtends works independently of other element configuration.
     */
    @Test
    public void testSetExtendsIndependentOfOtherConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");
        element.setAnnotation("MyAnnotation");
        element.setType("class");
        element.setExtendsannotation("BaseAnnotation");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName,
            "Extends class should be correctly stored alongside other configuration");
    }

    /**
     * Test that setExtends doesn't affect class annotation.
     */
    @Test
    public void testSetExtendsDoesNotAffectClassAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("MyAnnotation");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Class annotation should be set");
        assertEquals("LMyAnnotation;", spec.annotationType, "Class annotation should be correct");
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
    }

    /**
     * Test that setExtends doesn't affect extends annotation.
     */
    @Test
    public void testSetExtendsDoesNotAffectExtendsAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("BaseAnnotation");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LBaseAnnotation;", spec.extendsAnnotationType, "Extends annotation should be correct");
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName, "Extends class name should be correct");
    }

    /**
     * Test that setExtends doesn't affect access flags.
     */
    @Test
    public void testSetExtendsDoesNotAffectAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public,final");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertTrue(spec.requiredSetAccessFlags != 0, "Access flags should still be set");
    }

    /**
     * Test that setExtends doesn't affect class type.
     */
    @Test
    public void testSetExtendsDoesNotAffectType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("interface");
        element.setExtends("com.example.BaseInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertTrue(spec.requiredSetAccessFlags != 0 || spec.requiredUnsetAccessFlags != 0,
            "Type flags should still be set");
    }

    /**
     * Test that setExtends with nested class name using dollar sign.
     */
    @Test
    public void testSetExtendsWithNestedClass() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.OuterClass$InnerClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Nested class name should be stored");
        assertEquals("com/example/OuterClass$InnerClass", spec.extendsClassName,
            "Nested class name should preserve dollar sign and convert dots to slashes");
    }

    /**
     * Test that setExtends with single character name.
     */
    @Test
    public void testSetExtendsWithSingleCharacter() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("B");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Single character extends name should be stored");
        assertEquals("B", spec.extendsClassName, "Single character name should be stored as-is");
    }

    /**
     * Test that setExtends with wildcard pattern.
     */
    @Test
    public void testSetExtendsWithWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.*Base");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Wildcard pattern should be stored");
        assertEquals("com/example/*Base", spec.extendsClassName,
            "Wildcard should be preserved and dots converted to slashes");
    }

    /**
     * Test that setExtends with double wildcard pattern.
     */
    @Test
    public void testSetExtendsWithDoubleWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.**");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Double wildcard pattern should be stored");
        assertEquals("com/example/**", spec.extendsClassName,
            "Double wildcard should be preserved and dots converted to slashes");
    }

    /**
     * Test that setExtends doesn't affect class name.
     */
    @Test
    public void testSetExtendsDoesNotAffectClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be set");
        assertEquals("com/example/TestClass", spec.className, "Class name should not be affected");
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
    }

    /**
     * Test that setExtends doesn't interfere with field specifications.
     */
    @Test
    public void testSetExtendsDoesNotAffectFieldSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("myField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertNotNull(spec.fieldSpecifications, "Field specifications should be set");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that setExtends doesn't interfere with method specifications.
     */
    @Test
    public void testSetExtendsDoesNotAffectMethodSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("myMethod");
        element.addConfiguredMethod(method);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertNotNull(spec.methodSpecifications, "Method specifications should be set");
        assertEquals(1, spec.methodSpecifications.size(), "Should have one method specification");
    }

    /**
     * Test that setExtends with numeric characters.
     */
    @Test
    public void testSetExtendsWithNumericCharacters() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass123");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends name with numbers should be stored");
        assertEquals("com/example/BaseClass123", spec.extendsClassName,
            "Extends name with numbers should be converted correctly");
    }

    /**
     * Test that setExtends with underscores.
     */
    @Test
    public void testSetExtendsWithUnderscores() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.Base_Class_Name");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends name with underscores should be stored");
        assertEquals("com/example/Base_Class_Name", spec.extendsClassName,
            "Extends name with underscores should be converted correctly");
    }

    /**
     * Test that setExtends with spaces is accepted (even if unusual).
     */
    @Test
    public void testSetExtendsWithSpaces() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.Base Class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends name with spaces should be stored");
        assertTrue(spec.extendsClassName.contains(" "), "Spaces should be preserved");
    }

    /**
     * Test that setExtends with very long qualified name.
     */
    @Test
    public void testSetExtendsWithVeryLongQualifiedName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.very.long.package.name.hierarchy.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Long qualified extends name should be stored");
        assertEquals("com/example/very/long/package/name/hierarchy/BaseClass", spec.extendsClassName,
            "Long qualified extends name should be converted correctly");
    }

    /**
     * Test that setExtends with java.lang.Object.
     */
    @Test
    public void testSetExtendsWithJavaLangObject() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("java.lang.Object");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "java.lang.Object should be stored");
        assertEquals("java/lang/Object", spec.extendsClassName,
            "java.lang.Object should be converted correctly");
    }

    /**
     * Test that setExtends with an interface name (for interface extension).
     */
    @Test
    public void testSetExtendsWithInterfaceName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestInterface");
        element.setType("interface");
        element.setExtends("com.example.BaseInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Interface extends should be stored");
        assertEquals("com/example/BaseInterface", spec.extendsClassName,
            "Interface extends should be converted correctly");
    }

    /**
     * Test that setExtends is called successfully on an element without name.
     */
    @Test
    public void testSetExtendsOnElementWithoutName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends should be set even without class name");
        assertEquals("com/example/BaseClass", spec.extendsClassName,
            "Extends should be correctly stored");
    }

    /**
     * Test that setExtends with multiple consecutive dots.
     */
    @Test
    public void testSetExtendsWithConsecutiveDots() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com..example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends with consecutive dots should be stored");
        assertEquals("com//example/BaseClass", spec.extendsClassName,
            "Consecutive dots should be converted to consecutive slashes");
    }

    /**
     * Test that setExtends with question mark wildcard.
     */
    @Test
    public void testSetExtendsWithQuestionMarkWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.?Base");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Question mark wildcard should be stored");
        assertEquals("com/example/?Base", spec.extendsClassName,
            "Question mark wildcard should be preserved");
    }

    /**
     * Test that setExtends with multiple wildcards.
     */
    @Test
    public void testSetExtendsWithMultipleWildcards() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.*.example.*Base");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Multiple wildcards should be stored");
        assertEquals("com/*/example/*Base", spec.extendsClassName,
            "Multiple wildcards should be preserved and dots converted to slashes");
    }

    /**
     * Test that setExtends with package only (no class).
     */
    @Test
    public void testSetExtendsWithPackageOnly() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Package name should be stored");
        assertEquals("com/example", spec.extendsClassName,
            "Package name should be converted to internal format");
    }

    /**
     * Test that setExtends followed by setImplements (they use the same field).
     */
    @Test
    public void testSetExtendsFollowedBySetImplements() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends/implements class name should be set");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "setImplements should override setExtends (they use the same field)");
    }

    /**
     * Test that setExtends with trailing wildcard.
     */
    @Test
    public void testSetExtendsWithTrailingWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.*");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Trailing wildcard should be stored");
        assertEquals("com/example/*", spec.extendsClassName,
            "Trailing wildcard should be preserved");
    }

    /**
     * Test that setExtends with leading wildcard.
     */
    @Test
    public void testSetExtendsWithLeadingWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("*.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Leading wildcard should be stored");
        assertEquals("*/BaseClass", spec.extendsClassName,
            "Leading wildcard should be preserved");
    }
}
