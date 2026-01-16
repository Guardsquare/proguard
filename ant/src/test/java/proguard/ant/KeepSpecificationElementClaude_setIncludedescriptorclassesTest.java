package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.KeepClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KeepSpecificationElement.setIncludedescriptorclasses method.
 */
public class KeepSpecificationElementClaude_setIncludedescriptorclassesTest {

    /**
     * Test that setIncludedescriptorclasses accepts true value.
     */
    @Test
    public void testSetIncludedescriptorclassesWithTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setIncludedescriptorclasses(true),
            "Should accept true value");
    }

    /**
     * Test that setIncludedescriptorclasses accepts false value.
     */
    @Test
    public void testSetIncludedescriptorclassesWithFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setIncludedescriptorclasses(false),
            "Should accept false value");
    }

    /**
     * Test that setIncludedescriptorclasses with true propagates to KeepClassSpecification.
     */
    @Test
    public void testSetIncludedescriptorclassesTruePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true when setIncludedescriptorclasses(true) is called");
    }

    /**
     * Test that setIncludedescriptorclasses with false propagates to KeepClassSpecification.
     */
    @Test
    public void testSetIncludedescriptorclassesFalsePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludedescriptorclasses(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false when setIncludedescriptorclasses(false) is called");
    }

    /**
     * Test that default value (when setIncludedescriptorclasses is not called) is false.
     */
    @Test
    public void testIncludedescriptorclassesDefaultValueIsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        // Don't call setIncludedescriptorclasses

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false by default");
    }

    /**
     * Test that setIncludedescriptorclasses can be called multiple times,
     * with the last value taking effect.
     */
    @Test
    public void testSetIncludedescriptorclassesMultipleTimes() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setIncludedescriptorclasses(false);
        element.setIncludedescriptorclasses(true);
        element.setIncludedescriptorclasses(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false (the last set value)");
    }

    /**
     * Test that setIncludedescriptorclasses can be toggled from false to true.
     */
    @Test
    public void testSetIncludedescriptorclassesToggleFalseToTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setIncludedescriptorclasses(false);
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true after toggling from false to true");
    }

    /**
     * Test that setIncludedescriptorclasses can be toggled from true to false.
     */
    @Test
    public void testSetIncludedescriptorclassesToggleTrueToFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setIncludedescriptorclasses(true);
        element.setIncludedescriptorclasses(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false after toggling from true to false");
    }

    /**
     * Test that setIncludedescriptorclasses works correctly with other setters.
     */
    @Test
    public void testSetIncludedescriptorclassesWithOtherSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludedescriptorclasses(true);
        element.setIncludecode(true);
        element.setAllowshrinking(false);
        element.setAllowoptimization(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertTrue(keepSpec.markCodeAttributes, "markCodeAttributes should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that setIncludedescriptorclasses works correctly with Ant project.
     */
    @Test
    public void testSetIncludedescriptorclassesWithAntProject() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);
        element.setName("com.example.**");

        assertDoesNotThrow(() -> element.setIncludedescriptorclasses(true),
            "Should work with Ant project");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should be populated");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
    }

    /**
     * Test that setIncludedescriptorclasses works with different appendTo parameters.
     */
    @Test
    public void testSetIncludedescriptorclassesWithDifferentAppendToParameters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludedescriptorclasses(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();

        element.appendTo(list1, true, true, true);
        element.appendTo(list2, false, false, false);
        element.appendTo(list3, true, false, true);

        // All should have markDescriptorClasses set to true
        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);
        KeepClassSpecification spec3 = (KeepClassSpecification) list3.get(0);

        assertTrue(spec1.markDescriptorClasses,
            "markDescriptorClasses should be true regardless of appendTo parameters");
        assertTrue(spec2.markDescriptorClasses,
            "markDescriptorClasses should be true regardless of appendTo parameters");
        assertTrue(spec3.markDescriptorClasses,
            "markDescriptorClasses should be true regardless of appendTo parameters");
    }

    /**
     * Test that setIncludedescriptorclasses is independent across different instances.
     */
    @Test
    public void testSetIncludedescriptorclassesIndependentInstances() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        element1.setName("com.example.first.**");
        element2.setName("com.example.second.**");

        element1.setIncludedescriptorclasses(true);
        element2.setIncludedescriptorclasses(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element1.appendTo(list1, true, true, false);
        element2.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.markDescriptorClasses,
            "First element should have markDescriptorClasses set to true");
        assertFalse(spec2.markDescriptorClasses,
            "Second element should have markDescriptorClasses set to false");
    }

    /**
     * Test that setIncludedescriptorclasses works correctly when called before setName.
     */
    @Test
    public void testSetIncludedescriptorclassesBeforeSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setIncludedescriptorclasses(true);
        element.setName("com.example.**");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true regardless of setter call order");
    }

    /**
     * Test that setIncludedescriptorclasses works correctly when called after setName.
     */
    @Test
    public void testSetIncludedescriptorclassesAfterSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setName("com.example.**");
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true regardless of setter call order");
    }

    /**
     * Test that setIncludedescriptorclasses can be called on a freshly created element.
     */
    @Test
    public void testSetIncludedescriptorclassesOnFreshElement() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setIncludedescriptorclasses(true),
            "Should be able to call setIncludedescriptorclasses immediately after construction");
    }

    /**
     * Test that setIncludedescriptorclasses works with parent class setters.
     */
    @Test
    public void testSetIncludedescriptorclassesWithParentSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAccess("public");
        element.setType("class");
        element.setName("com.example.MyClass");
        element.setExtends("com.example.BaseClass");
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true when used with parent class setters");
    }

    /**
     * Test that setIncludedescriptorclasses true is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetIncludedescriptorclassesTruePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludedescriptorclasses(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.markDescriptorClasses,
            "First appendTo should create specification with markDescriptorClasses true");
        assertTrue(spec2.markDescriptorClasses,
            "Second appendTo should create specification with markDescriptorClasses true");
    }

    /**
     * Test that setIncludedescriptorclasses false is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetIncludedescriptorclassesFalsePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludedescriptorclasses(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertFalse(spec1.markDescriptorClasses,
            "First appendTo should create specification with markDescriptorClasses false");
        assertFalse(spec2.markDescriptorClasses,
            "Second appendTo should create specification with markDescriptorClasses false");
    }

    /**
     * Test that setIncludedescriptorclasses works with all other keep specification setters.
     */
    @Test
    public void testSetIncludedescriptorclassesWithAllKeepSpecificationSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setIncludedescriptorclasses(true);
        element.setIncludecode(false);
        element.setAllowshrinking(true);
        element.setAllowoptimization(true);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true");
        assertFalse(keepSpec.markCodeAttributes,
            "markCodeAttributes should be false");
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true");
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true");
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true");
    }

    /**
     * Test that setIncludedescriptorclasses can be used without setting a name.
     */
    @Test
    public void testSetIncludedescriptorclassesWithoutSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true even without a name set");
    }

    /**
     * Test that setIncludedescriptorclasses works correctly when the element extends a class.
     */
    @Test
    public void testSetIncludedescriptorclassesWithExtendsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setExtends("com.example.BaseClass");
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true with extends clause");
    }

    /**
     * Test that setIncludedescriptorclasses works correctly when the element implements an interface.
     */
    @Test
    public void testSetIncludedescriptorclassesWithImplementsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setImplements("com.example.MyInterface");
        element.setIncludedescriptorclasses(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be true with implements clause");
    }
}
