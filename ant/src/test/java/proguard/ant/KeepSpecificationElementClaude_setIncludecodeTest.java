package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.KeepClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KeepSpecificationElement.setIncludecode method.
 */
public class KeepSpecificationElementClaude_setIncludecodeTest {

    /**
     * Test that setIncludecode accepts true value.
     */
    @Test
    public void testSetIncludecodeWithTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setIncludecode(true),
            "Should accept true value");
    }

    /**
     * Test that setIncludecode accepts false value.
     */
    @Test
    public void testSetIncludecodeWithFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setIncludecode(false),
            "Should accept false value");
    }

    /**
     * Test that setIncludecode with true propagates to KeepClassSpecification.
     */
    @Test
    public void testSetIncludecodeTruePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true when setIncludecode(true) is called");
    }

    /**
     * Test that setIncludecode with false propagates to KeepClassSpecification.
     */
    @Test
    public void testSetIncludecodeFalsePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markCodeAttributes,
            "markCodeAttributes should be false when setIncludecode(false) is called");
    }

    /**
     * Test that default value (when setIncludecode is not called) is false.
     */
    @Test
    public void testIncludecodeDefaultValueIsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        // Don't call setIncludecode

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markCodeAttributes,
            "markCodeAttributes should be false by default");
    }

    /**
     * Test that setIncludecode can be called multiple times,
     * with the last value taking effect.
     */
    @Test
    public void testSetIncludecodeMultipleTimes() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setIncludecode(false);
        element.setIncludecode(true);
        element.setIncludecode(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.markCodeAttributes,
            "markCodeAttributes should be false (the last set value)");
    }

    /**
     * Test that setIncludecode can be toggled from false to true.
     */
    @Test
    public void testSetIncludecodeToggleFalseToTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setIncludecode(false);
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true after toggling from false to true");
    }

    /**
     * Test that setIncludecode can be toggled from true to false.
     */
    @Test
    public void testSetIncludecodeToggleTrueToFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setIncludecode(true);
        element.setIncludecode(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.markCodeAttributes,
            "markCodeAttributes should be false after toggling from true to false");
    }

    /**
     * Test that setIncludecode works correctly with other setters.
     */
    @Test
    public void testSetIncludecodeWithOtherSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(true);
        element.setAllowshrinking(false);
        element.setAllowoptimization(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes, "markCodeAttributes should be true");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that setIncludecode works correctly with Ant project.
     */
    @Test
    public void testSetIncludecodeWithAntProject() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);
        element.setName("com.example.**");

        assertDoesNotThrow(() -> element.setIncludecode(true),
            "Should work with Ant project");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should be populated");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes, "markCodeAttributes should be true");
    }

    /**
     * Test that setIncludecode works with different appendTo parameters.
     */
    @Test
    public void testSetIncludecodeWithDifferentAppendToParameters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();

        element.appendTo(list1, true, true, true);
        element.appendTo(list2, false, false, false);
        element.appendTo(list3, true, false, true);

        // All should have markCodeAttributes set to true
        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);
        KeepClassSpecification spec3 = (KeepClassSpecification) list3.get(0);

        assertTrue(spec1.markCodeAttributes,
            "markCodeAttributes should be true regardless of appendTo parameters");
        assertTrue(spec2.markCodeAttributes,
            "markCodeAttributes should be true regardless of appendTo parameters");
        assertTrue(spec3.markCodeAttributes,
            "markCodeAttributes should be true regardless of appendTo parameters");
    }

    /**
     * Test that setIncludecode is independent across different instances.
     */
    @Test
    public void testSetIncludecodeIndependentInstances() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        element1.setName("com.example.first.**");
        element2.setName("com.example.second.**");

        element1.setIncludecode(true);
        element2.setIncludecode(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element1.appendTo(list1, true, true, false);
        element2.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.markCodeAttributes,
            "First element should have markCodeAttributes set to true");
        assertFalse(spec2.markCodeAttributes,
            "Second element should have markCodeAttributes set to false");
    }

    /**
     * Test that setIncludecode works correctly when called before setName.
     */
    @Test
    public void testSetIncludecodeBeforeSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setIncludecode(true);
        element.setName("com.example.**");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true regardless of setter call order");
    }

    /**
     * Test that setIncludecode works correctly when called after setName.
     */
    @Test
    public void testSetIncludecodeAfterSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setName("com.example.**");
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true regardless of setter call order");
    }

    /**
     * Test that setIncludecode can be called on a freshly created element.
     */
    @Test
    public void testSetIncludecodeOnFreshElement() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setIncludecode(true),
            "Should be able to call setIncludecode immediately after construction");
    }

    /**
     * Test that setIncludecode works with parent class setters.
     */
    @Test
    public void testSetIncludecodeWithParentSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAccess("public");
        element.setType("class");
        element.setName("com.example.MyClass");
        element.setExtends("com.example.BaseClass");
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true when used with parent class setters");
    }

    /**
     * Test that setIncludecode true is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetIncludecodeTruePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.markCodeAttributes,
            "First appendTo should create specification with markCodeAttributes true");
        assertTrue(spec2.markCodeAttributes,
            "Second appendTo should create specification with markCodeAttributes true");
    }

    /**
     * Test that setIncludecode false is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetIncludecodeFalsePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertFalse(spec1.markCodeAttributes,
            "First appendTo should create specification with markCodeAttributes false");
        assertFalse(spec2.markCodeAttributes,
            "Second appendTo should create specification with markCodeAttributes false");
    }

    /**
     * Test that setIncludecode works with all other keep specification setters.
     */
    @Test
    public void testSetIncludecodeWithAllKeepSpecificationSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(false);
        element.setAllowshrinking(true);
        element.setAllowoptimization(true);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true");
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true");
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true");
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true");
    }

    /**
     * Test that setIncludecode can be used without setting a name.
     */
    @Test
    public void testSetIncludecodeWithoutSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true even without a name set");
    }

    /**
     * Test that setIncludecode works correctly when the element extends a class.
     */
    @Test
    public void testSetIncludecodeWithExtendsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setExtends("com.example.BaseClass");
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true with extends clause");
    }

    /**
     * Test that setIncludecode works correctly when the element implements an interface.
     */
    @Test
    public void testSetIncludecodeWithImplementsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setImplements("com.example.MyInterface");
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true with implements clause");
    }

    /**
     * Test that setIncludecode true results in code attributes being marked.
     */
    @Test
    public void testSetIncludecodeTrueMarksCodeAttributes() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setIncludecode(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "Setting includecode to true should mark code attributes");
    }

    /**
     * Test that setIncludecode false results in code attributes not being marked.
     */
    @Test
    public void testSetIncludecodeFalseDoesNotMarkCodeAttributes() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setIncludecode(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.markCodeAttributes,
            "Setting includecode to false should not mark code attributes");
    }

    /**
     * Test that setIncludecode can be used in combination with setIncludedescriptorclasses.
     */
    @Test
    public void testSetIncludecodeWithSetIncludedescriptorclasses() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true");
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true");
    }

    /**
     * Test that setIncludecode and setIncludedescriptorclasses are independent.
     */
    @Test
    public void testSetIncludecodeAndSetIncludedescriptorclassesAreIndependent() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        element1.setName("com.example.first.**");
        element1.setIncludecode(true);
        element1.setIncludedescriptorclasses(false);

        element2.setName("com.example.second.**");
        element2.setIncludecode(false);
        element2.setIncludedescriptorclasses(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element1.appendTo(list1, true, true, false);
        element2.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.markCodeAttributes, "First element should mark code attributes");
        assertFalse(spec1.markDescriptorClasses, "First element should not mark descriptor classes");
        assertFalse(spec2.markCodeAttributes, "Second element should not mark code attributes");
        assertTrue(spec2.markDescriptorClasses, "Second element should mark descriptor classes");
    }
}
