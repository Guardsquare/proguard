package proguard.ant;

import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.setName(String) method.
 */
public class ClassSpecificationElementClaude_setNameTest {

    /**
     * Test that setName accepts a simple class name.
     */
    @Test
    public void testSetNameWithSimpleClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setName("MyClass"),
            "Should accept simple class name");
    }

    /**
     * Test that setName accepts a fully qualified class name.
     */
    @Test
    public void testSetNameWithFullyQualifiedClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setName("com.example.MyClass"),
            "Should accept fully qualified class name");
    }

    /**
     * Test that setName accepts null value.
     */
    @Test
    public void testSetNameWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setName(null),
            "Should accept null name");
    }

    /**
     * Test that setName accepts an empty string.
     */
    @Test
    public void testSetNameWithEmptyString() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setName(""),
            "Should accept empty string name");
    }

    /**
     * Test that setName accepts the wildcard "*".
     */
    @Test
    public void testSetNameWithWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setName("*"),
            "Should accept wildcard '*'");
    }

    /**
     * Test that setName stores simple class name correctly.
     */
    @Test
    public void testSetNameStoresSimpleClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("MyClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be stored");
        assertEquals("MyClass", spec.className, "Simple class name should be stored as-is");
    }

    /**
     * Test that setName converts fully qualified name to internal format.
     */
    @Test
    public void testSetNameConvertsToInternalFormat() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.MyClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be stored");
        assertEquals("com/example/MyClass", spec.className,
            "Fully qualified name should be converted to internal format (dots to slashes)");
    }

    /**
     * Test that setName with null results in null class name.
     */
    @Test
    public void testSetNameWithNullResultsInNullClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.className, "Null name should result in null class name");
    }

    /**
     * Test that setName with empty string results in empty class name.
     */
    @Test
    public void testSetNameWithEmptyStringStoresEmptyName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Empty string should result in non-null class name");
        assertEquals("", spec.className, "Empty string should be stored as empty");
    }

    /**
     * Test that setName with wildcard "*" results in null class name (backward compatibility).
     */
    @Test
    public void testSetNameWithWildcardResultsInNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("*");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.className,
            "Wildcard '*' should be converted to null for any class matching (backward compatibility)");
    }

    /**
     * Test that setName with package wildcard pattern.
     */
    @Test
    public void testSetNameWithPackageWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.**");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Package wildcard pattern should be stored");
        assertEquals("com/example/**", spec.className,
            "Package wildcard pattern should preserve wildcards and convert dots to slashes");
    }

    /**
     * Test that setName with class name wildcard pattern.
     */
    @Test
    public void testSetNameWithClassNameWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.*Class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name wildcard pattern should be stored");
        assertEquals("com/example/*Class", spec.className,
            "Class name wildcard should preserve wildcard and convert dots to slashes");
    }

    /**
     * Test that setName can be called multiple times (last value wins).
     */
    @Test
    public void testSetNameMultipleTimes() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.FirstClass");
        element.setName("com.example.SecondClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be stored");
        assertEquals("com/example/SecondClass", spec.className,
            "Last set name should be used");
    }

    /**
     * Test that setName can override with null.
     */
    @Test
    public void testSetNameOverrideWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.MyClass");
        element.setName(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.className, "Setting null should override previous name");
    }

    /**
     * Test that setName can override with wildcard.
     */
    @Test
    public void testSetNameOverrideWithWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.MyClass");
        element.setName("*");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.className, "Setting wildcard should override and result in null");
    }

    /**
     * Test that setName works independently of other element configuration.
     */
    @Test
    public void testSetNameIndependentOfOtherConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setAccess("public");
        element.setAnnotation("MyAnnotation");
        element.setType("class");
        element.setExtends("com.example.BaseClass");
        element.setName("com.example.TestClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be set");
        assertEquals("com/example/TestClass", spec.className,
            "Class name should be correctly stored alongside other configuration");
    }

    /**
     * Test that setName doesn't affect annotation.
     */
    @Test
    public void testSetNameDoesNotAffectAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setAnnotation("MyAnnotation");
        element.setName("com.example.TestClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation should still be set");
        assertEquals("LMyAnnotation;", spec.annotationType, "Annotation should be correct");
    }

    /**
     * Test that setName doesn't affect extends clause.
     */
    @Test
    public void testSetNameDoesNotAffectExtends() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setExtends("com.example.BaseClass");
        element.setName("com.example.TestClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should still be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName, "Extends should be correct");
    }

    /**
     * Test that setName doesn't affect access modifiers.
     */
    @Test
    public void testSetNameDoesNotAffectAccessModifiers() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setAccess("public,final");
        element.setName("com.example.TestClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0, "Access flags should still be set");
    }

    /**
     * Test that setName doesn't affect type.
     */
    @Test
    public void testSetNameDoesNotAffectType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setType("interface");
        element.setName("com.example.TestInterface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0 || spec.requiredUnsetAccessFlags != 0,
            "Type flags should still be set");
    }

    /**
     * Test that setName with nested class name using dollar sign.
     */
    @Test
    public void testSetNameWithNestedClass() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.OuterClass$InnerClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Nested class name should be stored");
        assertEquals("com/example/OuterClass$InnerClass", spec.className,
            "Nested class name should preserve dollar sign and convert dots to slashes");
    }

    /**
     * Test that setName with single character name.
     */
    @Test
    public void testSetNameWithSingleCharacter() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("A");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Single character name should be stored");
        assertEquals("A", spec.className, "Single character name should be stored as-is");
    }

    /**
     * Test that setName with multiple wildcards in different positions.
     */
    @Test
    public void testSetNameWithMultipleWildcards() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.*.example.*Class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Multiple wildcards should be stored");
        assertEquals("com/*/example/*Class", spec.className,
            "Multiple wildcards should be preserved and dots converted to slashes");
    }

    /**
     * Test that setName with question mark wildcard.
     */
    @Test
    public void testSetNameWithQuestionMarkWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.?Class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Question mark wildcard should be stored");
        assertEquals("com/example/?Class", spec.className,
            "Question mark wildcard should be preserved");
    }

    /**
     * Test that setName with just a package name (no class).
     */
    @Test
    public void testSetNameWithPackageOnly() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Package name should be stored");
        assertEquals("com/example", spec.className,
            "Package name should be converted to internal format");
    }

    /**
     * Test that setName with spaces is accepted (even if unusual).
     */
    @Test
    public void testSetNameWithSpaces() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.My Class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Name with spaces should be stored");
        assertTrue(spec.className.contains(" "), "Spaces should be preserved");
    }

    /**
     * Test that setName with very long qualified name.
     */
    @Test
    public void testSetNameWithVeryLongQualifiedName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.very.long.package.name.hierarchy.MyClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Long qualified name should be stored");
        assertEquals("com/example/very/long/package/name/hierarchy/MyClass", spec.className,
            "Long qualified name should be converted correctly");
    }

    /**
     * Test that setName with trailing wildcard.
     */
    @Test
    public void testSetNameWithTrailingWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.*");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Trailing wildcard should be stored");
        assertEquals("com/example/*", spec.className,
            "Trailing wildcard should be preserved");
    }

    /**
     * Test that setName with leading wildcard.
     */
    @Test
    public void testSetNameWithLeadingWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("*.example.MyClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Leading wildcard should be stored");
        assertEquals("*/example/MyClass", spec.className,
            "Leading wildcard should be preserved");
    }

    /**
     * Test that setName doesn't interfere with field specifications.
     */
    @Test
    public void testSetNameDoesNotAffectFieldSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("myField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be set");
        assertNotNull(spec.fieldSpecifications, "Field specifications should be set");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that setName doesn't interfere with method specifications.
     */
    @Test
    public void testSetNameDoesNotAffectMethodSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("myMethod");
        element.addConfiguredMethod(method);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be set");
        assertNotNull(spec.methodSpecifications, "Method specifications should be set");
        assertEquals(1, spec.methodSpecifications.size(), "Should have one method specification");
    }

    /**
     * Test that setName with numeric characters.
     */
    @Test
    public void testSetNameWithNumericCharacters() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.MyClass123");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Name with numbers should be stored");
        assertEquals("com/example/MyClass123", spec.className,
            "Name with numbers should be converted correctly");
    }

    /**
     * Test that setName with underscores.
     */
    @Test
    public void testSetNameWithUnderscores() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.My_Class_Name");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Name with underscores should be stored");
        assertEquals("com/example/My_Class_Name", spec.className,
            "Name with underscores should be converted correctly");
    }

    /**
     * Test that setName with multiple consecutive dots.
     */
    @Test
    public void testSetNameWithConsecutiveDots() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com..example.MyClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Name with consecutive dots should be stored");
        assertEquals("com//example/MyClass", spec.className,
            "Consecutive dots should be converted to consecutive slashes");
    }

    /**
     * Test that only a single asterisk "*" is treated specially, not other asterisk patterns.
     */
    @Test
    public void testSetNameWithAsteriskInPattern() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("*Test"); // Not just "*", so should not become null

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Asterisk in pattern should not result in null");
        assertEquals("*Test", spec.className, "Asterisk should be preserved in pattern");
    }
}
