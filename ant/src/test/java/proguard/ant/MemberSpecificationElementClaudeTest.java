package proguard.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.MemberSpecification;
import proguard.MemberValueSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MemberSpecificationElement.
 */
public class MemberSpecificationElementClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Test that the constructor creates a non-null instance.
     */
    @Test
    public void testConstructorCreatesInstance() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertNotNull(element, "Constructor should create a non-null instance");
    }

    /**
     * Test that multiple instances can be created independently.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        MemberSpecificationElement element1 = new MemberSpecificationElement();
        MemberSpecificationElement element2 = new MemberSpecificationElement();

        assertNotNull(element1, "First instance should not be null");
        assertNotNull(element2, "Second instance should not be null");
        assertNotSame(element1, element2, "Instances should be different objects");
    }

    // ========== setAccess Tests ==========

    /**
     * Test that setAccess accepts a valid access modifier.
     */
    @Test
    public void testSetAccessWithPublic() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setAccess("public"),
            "Should accept 'public' access modifier");
    }

    /**
     * Test that setAccess accepts multiple access modifiers.
     */
    @Test
    public void testSetAccessWithMultipleModifiers() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setAccess("public static"),
            "Should accept multiple access modifiers");
    }

    /**
     * Test that setAccess with public modifier affects appendTo.
     */
    @Test
    public void testSetAccessPublicPropagates() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAccess("public");
        element.setName("testMethod");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0,
            "Access flags should be set when access modifier is specified");
    }

    /**
     * Test that setAccess with negated modifier works.
     */
    @Test
    public void testSetAccessWithNegatedModifier() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAccess("!private");
        element.setName("testMethod");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertTrue(spec.requiredUnsetAccessFlags != 0,
            "Unset access flags should be set when negated modifier is specified");
    }

    /**
     * Test that setAccess with invalid modifier throws BuildException.
     */
    @Test
    public void testSetAccessWithInvalidModifierThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAccess("invalidmodifier");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, false, false),
            "Should throw BuildException for invalid access modifier");
    }

    /**
     * Test that setAccess with null works.
     */
    @Test
    public void testSetAccessWithNull() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setAccess(null),
            "Should accept null access modifier");
    }

    // ========== setAnnotation Tests ==========

    /**
     * Test that setAnnotation accepts a valid annotation.
     */
    @Test
    public void testSetAnnotationWithValidAnnotation() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setAnnotation("java.lang.Override"),
            "Should accept valid annotation");
    }

    /**
     * Test that setAnnotation propagates to MemberSpecification.
     */
    @Test
    public void testSetAnnotationPropagates() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAnnotation("java.lang.Override");
        element.setName("toString");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertNotNull(spec.annotationType, "Annotation type should be set");
    }

    /**
     * Test that setAnnotation with null works.
     */
    @Test
    public void testSetAnnotationWithNull() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setAnnotation(null),
            "Should accept null annotation");
    }

    // ========== setType Tests ==========

    /**
     * Test that setType accepts a primitive type.
     */
    @Test
    public void testSetTypeWithPrimitiveType() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setType("int"),
            "Should accept primitive type");
    }

    /**
     * Test that setType accepts a class type.
     */
    @Test
    public void testSetTypeWithClassType() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setType("java.lang.String"),
            "Should accept class type");
    }

    /**
     * Test that setType with void works for methods.
     */
    @Test
    public void testSetTypeWithVoidForMethod() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("void");
        element.setParameters("");
        element.setName("testMethod");

        List memberSpecs = new ArrayList();
        assertDoesNotThrow(() -> element.appendTo(memberSpecs, true, false),
            "Should accept void type for method");
    }

    /**
     * Test that setType with null works.
     */
    @Test
    public void testSetTypeWithNull() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setType(null),
            "Should accept null type");
    }

    // ========== setName Tests ==========

    /**
     * Test that setName accepts a valid name.
     */
    @Test
    public void testSetNameWithValidName() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setName("myField"),
            "Should accept valid name");
    }

    /**
     * Test that setName propagates to MemberSpecification.
     */
    @Test
    public void testSetNamePropagates() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myField");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("myField", spec.name, "Name should match");
    }

    /**
     * Test that setName accepts wildcard patterns.
     */
    @Test
    public void testSetNameWithWildcard() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("test*");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("test*", spec.name, "Wildcard name should match");
    }

    /**
     * Test that setName with null works.
     */
    @Test
    public void testSetNameWithNull() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setName(null),
            "Should accept null name");
    }

    // ========== setParameters Tests ==========

    /**
     * Test that setParameters accepts empty parameters.
     */
    @Test
    public void testSetParametersWithEmptyString() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setParameters(""),
            "Should accept empty parameters");
    }

    /**
     * Test that setParameters accepts single parameter.
     */
    @Test
    public void testSetParametersWithSingleParameter() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("void");
        element.setParameters("int");
        element.setName("testMethod");

        List memberSpecs = new ArrayList();
        assertDoesNotThrow(() -> element.appendTo(memberSpecs, true, false),
            "Should accept single parameter");
    }

    /**
     * Test that setParameters accepts multiple parameters.
     */
    @Test
    public void testSetParametersWithMultipleParameters() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("void");
        element.setParameters("int,java.lang.String");
        element.setName("testMethod");

        List memberSpecs = new ArrayList();
        assertDoesNotThrow(() -> element.appendTo(memberSpecs, true, false),
            "Should accept multiple parameters");
    }

    /**
     * Test that setParameters without type throws exception for methods.
     */
    @Test
    public void testSetParametersWithoutTypeThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setParameters("int");
        element.setName("testMethod");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, true, false),
            "Should throw exception when parameters are set without type for method");
    }

    /**
     * Test that setParameters throws exception for fields.
     */
    @Test
    public void testSetParametersForFieldThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setParameters("int");
        element.setName("myField");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, false, false),
            "Should throw exception when parameters are set for field");
    }

    /**
     * Test that setParameters with null works.
     */
    @Test
    public void testSetParametersWithNull() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setParameters(null),
            "Should accept null parameters");
    }

    // ========== setParam Tests (deprecated) ==========

    /**
     * Test that deprecated setParam works like setParameters.
     */
    @Test
    public void testSetParamWorksLikeSetParameters() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("void");
        element.setParam("int");
        element.setName("testMethod");

        List memberSpecs = new ArrayList();
        assertDoesNotThrow(() -> element.appendTo(memberSpecs, true, false),
            "setParam should work like setParameters");
    }

    /**
     * Test that setParam accepts null.
     */
    @Test
    public void testSetParamWithNull() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setParam(null),
            "Should accept null parameter");
    }

    // ========== setValues Tests ==========

    /**
     * Test that setValues accepts a single integer value.
     */
    @Test
    public void testSetValuesWithSingleIntValue() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("int");
        element.setName("myField");
        element.setValues("42");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertNotNull(spec.values, "Values should be set");
        assertEquals(1, spec.values.length, "Should have one value");
        assertEquals(42, spec.values[0].intValue(), "Value should be 42");
    }

    /**
     * Test that setValues accepts a range of values.
     */
    @Test
    public void testSetValuesWithRange() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("int");
        element.setName("myField");
        element.setValues("10..20");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertNotNull(spec.values, "Values should be set");
        assertEquals(2, spec.values.length, "Should have two values (range)");
        assertEquals(10, spec.values[0].intValue(), "First value should be 10");
        assertEquals(20, spec.values[1].intValue(), "Second value should be 20");
    }

    /**
     * Test that setValues accepts boolean true.
     */
    @Test
    public void testSetValuesWithBooleanTrue() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("boolean");
        element.setName("myField");
        element.setValues("true");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertNotNull(spec.values, "Values should be set");
        assertEquals(1, spec.values[0].intValue(), "true should be represented as 1");
    }

    /**
     * Test that setValues accepts boolean false.
     */
    @Test
    public void testSetValuesWithBooleanFalse() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("boolean");
        element.setName("myField");
        element.setValues("false");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertNotNull(spec.values, "Values should be set");
        assertEquals(0, spec.values[0].intValue(), "false should be represented as 0");
    }

    /**
     * Test that setValues without type throws exception.
     */
    @Test
    public void testSetValuesWithoutTypeThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myField");
        element.setValues("42");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, false, false),
            "Should throw exception when values are set without type");
    }

    /**
     * Test that setValues with invalid type throws exception.
     */
    @Test
    public void testSetValuesWithInvalidTypeThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("java.lang.String");
        element.setName("myField");
        element.setValues("hello");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, false, false),
            "Should throw exception for non-primitive type with values");
    }

    /**
     * Test that setValues with hex value works.
     */
    @Test
    public void testSetValuesWithHexValue() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("int");
        element.setName("myField");
        element.setValues("0xFF");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertFalse(memberSpecs.isEmpty(), "Member specifications list should not be empty");
        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertEquals(255, spec.values[0].intValue(), "0xFF should be 255");
    }

    /**
     * Test that setValues with invalid number format throws exception.
     */
    @Test
    public void testSetValuesWithInvalidNumberFormatThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("int");
        element.setName("myField");
        element.setValues("notanumber");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, false, false),
            "Should throw exception for invalid number format");
    }

    /**
     * Test that setValues with null works.
     */
    @Test
    public void testSetValuesWithNull() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        assertDoesNotThrow(() -> element.setValues(null),
            "Should accept null values");
    }

    /**
     * Test that setValues for constructor throws exception.
     */
    @Test
    public void testSetValuesForConstructorThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setParameters("");
        element.setValues("42");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, true, true),
            "Should throw exception when values are set for constructor");
    }

    // ========== appendTo Tests ==========

    /**
     * Test appendTo for a simple field.
     */
    @Test
    public void testAppendToForSimpleField() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myField");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("myField", spec.name, "Field name should match");
    }

    /**
     * Test appendTo for a field with type.
     */
    @Test
    public void testAppendToForFieldWithType() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myField");
        element.setType("int");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertNotNull(spec.descriptor, "Descriptor should be set for typed field");
    }

    /**
     * Test appendTo for a simple method.
     */
    @Test
    public void testAppendToForSimpleMethod() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myMethod");
        element.setType("void");
        element.setParameters("");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, true, false);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("myMethod", spec.name, "Method name should match");
        assertNotNull(spec.descriptor, "Descriptor should be set for method");
    }

    /**
     * Test appendTo for a constructor.
     */
    @Test
    public void testAppendToForConstructor() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setParameters("");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, true, true);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("<init>", spec.name, "Constructor should have <init> name");
    }

    /**
     * Test that constructor with type throws exception.
     */
    @Test
    public void testAppendToForConstructorWithTypeThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("void");
        element.setParameters("");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, true, true),
            "Should throw exception when type is set for constructor");
    }

    /**
     * Test appendTo adds to existing list.
     */
    @Test
    public void testAppendToAddsToExistingList() {
        MemberSpecificationElement element1 = new MemberSpecificationElement();
        element1.setName("field1");

        MemberSpecificationElement element2 = new MemberSpecificationElement();
        element2.setName("field2");

        List memberSpecs = new ArrayList();
        element1.appendTo(memberSpecs, false, false);
        element2.appendTo(memberSpecs, false, false);

        assertEquals(2, memberSpecs.size(), "Should have two specifications");
    }

    /**
     * Test appendTo with annotation converts to internal format.
     */
    @Test
    public void testAppendToConvertsAnnotationToInternalFormat() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAnnotation("java.lang.Override");
        element.setName("toString");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertTrue(spec.annotationType.startsWith("L"),
            "Annotation should be in internal format starting with L");
    }

    /**
     * Test appendTo multiple times creates multiple specifications.
     */
    @Test
    public void testAppendToMultipleTimes() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myField");

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, false, false);
        element.appendTo(list2, false, false);

        assertEquals(1, list1.size(), "First list should have one specification");
        assertEquals(1, list2.size(), "Second list should have one specification");
    }

    /**
     * Test appendTo with all attributes set.
     */
    @Test
    public void testAppendToWithAllAttributesSet() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAccess("public static");
        element.setAnnotation("java.lang.Deprecated");
        element.setType("int");
        element.setName("myField");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("myField", spec.name, "Name should match");
        assertNotNull(spec.annotationType, "Annotation should be set");
        assertNotNull(spec.descriptor, "Descriptor should be set");
        assertTrue(spec.requiredSetAccessFlags != 0, "Access flags should be set");
    }

    /**
     * Test appendTo with method having parameters.
     */
    @Test
    public void testAppendToForMethodWithParameters() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myMethod");
        element.setType("void");
        element.setParameters("int,java.lang.String");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, true, false);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("myMethod", spec.name, "Method name should match");
        assertTrue(spec.descriptor.contains("("), "Descriptor should contain parameter list");
    }

    /**
     * Test appendTo for constructor with parameters.
     */
    @Test
    public void testAppendToForConstructorWithParameters() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setParameters("int,java.lang.String");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, true, true);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("<init>", spec.name, "Constructor should have <init> name");
        assertNotNull(spec.descriptor, "Constructor should have descriptor");
    }

    /**
     * Test appendTo creates MemberValueSpecification when values are set.
     */
    @Test
    public void testAppendToCreatesMemberValueSpecification() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("int");
        element.setName("myField");
        element.setValues("42");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        Object spec = memberSpecs.get(0);
        assertInstanceOf(MemberValueSpecification.class, spec,
            "Should create MemberValueSpecification when values are set");
    }

    /**
     * Test appendTo creates regular MemberSpecification when values are not set.
     */
    @Test
    public void testAppendToCreatesMemberSpecification() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("myField");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertEquals(1, memberSpecs.size(), "Should add exactly one specification");
        Object spec = memberSpecs.get(0);
        assertTrue(spec instanceof MemberSpecification,
            "Should create MemberSpecification when values are not set");
        assertFalse(spec instanceof MemberValueSpecification,
            "Should not create MemberValueSpecification when values are not set");
    }

    /**
     * Test appendTo with Ant project set.
     */
    @Test
    public void testAppendToWithAntProject() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);
        element.setName("myField");

        List memberSpecs = new ArrayList();
        assertDoesNotThrow(() -> element.appendTo(memberSpecs, false, false),
            "Should work with Ant project set");
        assertEquals(1, memberSpecs.size(), "Should add specification");
    }

    /**
     * Test that byte type works with values.
     */
    @Test
    public void testSetValuesWithByteType() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("byte");
        element.setName("myField");
        element.setValues("100");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertEquals(100, spec.values[0].intValue(), "Byte value should be 100");
    }

    /**
     * Test that char type works with values.
     */
    @Test
    public void testSetValuesWithCharType() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("char");
        element.setName("myField");
        element.setValues("65");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertEquals(65, spec.values[0].intValue(), "Char value should be 65");
    }

    /**
     * Test that short type works with values.
     */
    @Test
    public void testSetValuesWithShortType() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("short");
        element.setName("myField");
        element.setValues("1000");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertEquals(1000, spec.values[0].intValue(), "Short value should be 1000");
    }

    /**
     * Test that invalid boolean value throws exception.
     */
    @Test
    public void testSetValuesWithInvalidBooleanThrowsException() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("boolean");
        element.setName("myField");
        element.setValues("notaboolean");

        List memberSpecs = new ArrayList();
        assertThrows(BuildException.class, () -> element.appendTo(memberSpecs, false, false),
            "Should throw exception for invalid boolean value");
    }

    /**
     * Test that access flags are correctly parsed.
     */
    @Test
    public void testSetAccessWithAllValidModifiers() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAccess("public,static,final");
        element.setName("myField");

        List memberSpecs = new ArrayList();
        assertDoesNotThrow(() -> element.appendTo(memberSpecs, false, false),
            "Should accept comma-separated modifiers");
    }

    /**
     * Test that mixed positive and negative access flags work.
     */
    @Test
    public void testSetAccessWithMixedModifiers() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setAccess("public,!static");
        element.setName("myField");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0, "Required flags should be set");
        assertTrue(spec.requiredUnsetAccessFlags != 0, "Required unset flags should be set");
    }

    /**
     * Test appendTo with empty name.
     */
    @Test
    public void testAppendToWithEmptyName() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setName("");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        assertEquals(1, memberSpecs.size(), "Should add specification");
        MemberSpecification spec = (MemberSpecification) memberSpecs.get(0);
        assertEquals("", spec.name, "Empty name should be preserved");
    }

    /**
     * Test that values range with same start and end works.
     */
    @Test
    public void testSetValuesWithSameStartAndEndRange() {
        MemberSpecificationElement element = new MemberSpecificationElement();
        element.setType("int");
        element.setName("myField");
        element.setValues("42..42");

        List memberSpecs = new ArrayList();
        element.appendTo(memberSpecs, false, false);

        MemberValueSpecification spec = (MemberValueSpecification) memberSpecs.get(0);
        assertEquals(2, spec.values.length, "Should have two values");
        assertEquals(42, spec.values[0].intValue(), "Start should be 42");
        assertEquals(42, spec.values[1].intValue(), "End should be 42");
    }
}
