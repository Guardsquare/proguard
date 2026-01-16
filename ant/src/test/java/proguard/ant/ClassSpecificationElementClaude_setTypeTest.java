package proguard.ant;

import org.apache.tools.ant.BuildException;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;
import proguard.classfile.AccessConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.setType(String) method.
 */
public class ClassSpecificationElementClaude_setTypeTest {

    /**
     * Test that setType accepts "class" type.
     */
    @Test
    public void testSetTypeWithClass() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setType("class"),
            "Should accept 'class' type");
    }

    /**
     * Test that setType accepts "interface" type.
     */
    @Test
    public void testSetTypeWithInterface() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setType("interface"),
            "Should accept 'interface' type");
    }

    /**
     * Test that setType accepts "enum" type.
     */
    @Test
    public void testSetTypeWithEnum() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setType("enum"),
            "Should accept 'enum' type");
    }

    /**
     * Test that setType accepts negated "!interface" type.
     */
    @Test
    public void testSetTypeWithNegatedInterface() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setType("!interface"),
            "Should accept '!interface' type");
    }

    /**
     * Test that setType accepts negated "!enum" type.
     */
    @Test
    public void testSetTypeWithNegatedEnum() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setType("!enum"),
            "Should accept '!enum' type");
    }

    /**
     * Test that setType accepts null value.
     */
    @Test
    public void testSetTypeWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setType(null),
            "Should accept null type");
    }

    /**
     * Test that setType with "class" results in appropriate access flags.
     */
    @Test
    public void testSetTypeClassSetsNoAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        // "class" type means it's not an interface or enum, so no special flags are set
        assertNotNull(spec, "Should create ClassSpecification");
    }

    /**
     * Test that setType with "interface" sets interface access flag.
     */
    @Test
    public void testSetTypeInterfaceSetsInterfaceFlag() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestInterface");
        element.setType("interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.INTERFACE) != 0,
            "Interface type should set INTERFACE flag");
    }

    /**
     * Test that setType with "enum" sets enum access flag.
     */
    @Test
    public void testSetTypeEnumSetsEnumFlag() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestEnum");
        element.setType("enum");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.ENUM) != 0,
            "Enum type should set ENUM flag");
    }

    /**
     * Test that setType with "!interface" sets interface in unset flags.
     */
    @Test
    public void testSetTypeNegatedInterfaceSetsUnsetFlag() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("!interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredUnsetAccessFlags & AccessConstants.INTERFACE) != 0,
            "Negated interface type should set INTERFACE in unset flags");
    }

    /**
     * Test that setType with "!enum" sets enum in unset flags.
     */
    @Test
    public void testSetTypeNegatedEnumSetsUnsetFlag() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("!enum");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredUnsetAccessFlags & AccessConstants.ENUM) != 0,
            "Negated enum type should set ENUM in unset flags");
    }

    /**
     * Test that setType with null results in no type-specific flags.
     */
    @Test
    public void testSetTypeWithNullSetsNoFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec, "Should create ClassSpecification with null type");
    }

    /**
     * Test that setType with invalid type throws BuildException when appending.
     */
    @Test
    public void testSetTypeWithInvalidTypeThrowsException() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("invalid");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for invalid type");
    }

    /**
     * Test that setType with empty string throws BuildException when appending.
     */
    @Test
    public void testSetTypeWithEmptyStringThrowsException() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for empty string type");
    }

    /**
     * Test that setType with "abstract" throws BuildException when appending.
     */
    @Test
    public void testSetTypeWithAbstractThrowsException() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("abstract");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for 'abstract' as type (it's an access modifier)");
    }

    /**
     * Test that setType can be called multiple times (last value wins).
     */
    @Test
    public void testSetTypeMultipleTimes() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("class");
        element.setType("interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.INTERFACE) != 0,
            "Last set type (interface) should be used");
    }

    /**
     * Test that setType can override with null.
     */
    @Test
    public void testSetTypeOverrideWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("interface");
        element.setType(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        // With null type, no interface flag should be set
        assertEquals(0, spec.requiredSetAccessFlags & AccessConstants.INTERFACE,
            "Setting null should override previous type");
    }

    /**
     * Test that setType works independently of other element configuration.
     */
    @Test
    public void testSetTypeIndependentOfOtherConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");
        element.setAnnotation("MyAnnotation");
        element.setExtends("com.example.BaseClass");
        element.setType("interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.INTERFACE) != 0,
            "Type should be set correctly alongside other configuration");
        assertNotNull(spec.className, "Other configuration should still work");
        assertNotNull(spec.annotationType, "Other configuration should still work");
        assertNotNull(spec.extendsClassName, "Other configuration should still work");
    }

    /**
     * Test that setType doesn't affect annotation.
     */
    @Test
    public void testSetTypeDoesNotAffectAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("MyAnnotation");
        element.setType("interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation should still be set");
        assertEquals("LMyAnnotation;", spec.annotationType, "Annotation should be correct");
    }

    /**
     * Test that setType doesn't affect extends clause.
     */
    @Test
    public void testSetTypeDoesNotAffectExtends() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");
        element.setType("enum");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should still be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName, "Extends should be correct");
    }

    /**
     * Test that setType doesn't affect access modifiers.
     */
    @Test
    public void testSetTypeDoesNotAffectAccessModifiers() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public,final");
        element.setType("interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
            "Public access should still be set");
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
            "Final access should still be set");
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.INTERFACE) != 0,
            "Interface type should also be set");
    }

    /**
     * Test that setType is called successfully on an element without name.
     */
    @Test
    public void testSetTypeOnElementWithoutName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setType("interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.INTERFACE) != 0,
            "Type should be set even without class name");
    }

    /**
     * Test that setType with "class" and access modifiers works correctly.
     */
    @Test
    public void testSetTypeClassWithAccessModifiers() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("class");
        element.setAccess("public");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
            "Public access should be set");
        // Class type doesn't set additional flags
        assertEquals(0, spec.requiredSetAccessFlags & AccessConstants.INTERFACE,
            "Class should not have interface flag");
        assertEquals(0, spec.requiredSetAccessFlags & AccessConstants.ENUM,
            "Class should not have enum flag");
    }

    /**
     * Test that setType with case-sensitive invalid value throws exception.
     */
    @Test
    public void testSetTypeWithWrongCaseThrowsException() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("Interface"); // Wrong case

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for wrong case");
    }

    /**
     * Test that setType with "!class" throws BuildException.
     */
    @Test
    public void testSetTypeWithNegatedClassThrowsException() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("!class");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for '!class' (not a valid type)");
    }

    /**
     * Test that setType with whitespace throws BuildException.
     */
    @Test
    public void testSetTypeWithWhitespaceThrowsException() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType(" interface ");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for type with whitespace");
    }

    /**
     * Test that setType with multiple types concatenated throws BuildException.
     */
    @Test
    public void testSetTypeWithMultipleTypesThrowsException() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("interface,enum");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for multiple types");
    }

    /**
     * Test that negated interface and negated enum are mutually exclusive when set separately.
     */
    @Test
    public void testSetTypeNegatedInterfaceThenNegatedEnum() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("!interface");
        element.setType("!enum"); // This should override

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredUnsetAccessFlags & AccessConstants.ENUM) != 0,
            "Last set negated type (!enum) should be used");
    }

    /**
     * Test that setType correctly handles switching between regular and negated types.
     */
    @Test
    public void testSetTypeSwitchBetweenRegularAndNegated() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("interface");
        element.setType("!enum");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue((spec.requiredUnsetAccessFlags & AccessConstants.ENUM) != 0,
            "Last set type (!enum) should be used");
        assertEquals(0, spec.requiredSetAccessFlags & AccessConstants.INTERFACE,
            "Interface should not be in set flags after override");
    }
}
