package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.KeepClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KeepSpecificationElement.setAllowobfuscation method.
 */
public class KeepSpecificationElementClaude_setAllowobfuscationTest {

    /**
     * Test that setAllowobfuscation accepts true value.
     */
    @Test
    public void testSetAllowobfuscationWithTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowobfuscation(true),
            "Should accept true value");
    }

    /**
     * Test that setAllowobfuscation accepts false value.
     */
    @Test
    public void testSetAllowobfuscationWithFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowobfuscation(false),
            "Should accept false value");
    }

    /**
     * Test that setAllowobfuscation with true propagates to KeepClassSpecification.
     */
    @Test
    public void testSetAllowobfuscationTruePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true when setAllowobfuscation(true) is called");
    }

    /**
     * Test that setAllowobfuscation with false propagates to KeepClassSpecification.
     */
    @Test
    public void testSetAllowobfuscationFalsePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.allowObfuscation,
            "allowObfuscation should be false when setAllowobfuscation(false) is called");
    }

    /**
     * Test that default value (when setAllowobfuscation is not called) is false.
     */
    @Test
    public void testAllowobfuscationDefaultValueIsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        // Don't call setAllowobfuscation

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.allowObfuscation,
            "allowObfuscation should be false by default");
    }

    /**
     * Test that setAllowobfuscation can be called multiple times,
     * with the last value taking effect.
     */
    @Test
    public void testSetAllowobfuscationMultipleTimes() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowobfuscation(false);
        element.setAllowobfuscation(true);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowObfuscation,
            "allowObfuscation should be false (the last set value)");
    }

    /**
     * Test that setAllowobfuscation can be toggled from false to true.
     */
    @Test
    public void testSetAllowobfuscationToggleFalseToTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowobfuscation(false);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true after toggling from false to true");
    }

    /**
     * Test that setAllowobfuscation can be toggled from true to false.
     */
    @Test
    public void testSetAllowobfuscationToggleTrueToFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowobfuscation(true);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowObfuscation,
            "allowObfuscation should be false after toggling from true to false");
    }

    /**
     * Test that setAllowobfuscation works correctly with other setters.
     */
    @Test
    public void testSetAllowobfuscationWithOtherSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(true);
        element.setAllowshrinking(false);
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
        assertTrue(keepSpec.markCodeAttributes, "markCodeAttributes should be true");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
    }

    /**
     * Test that setAllowobfuscation works correctly with Ant project.
     */
    @Test
    public void testSetAllowobfuscationWithAntProject() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);
        element.setName("com.example.**");

        assertDoesNotThrow(() -> element.setAllowobfuscation(true),
            "Should work with Ant project");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should be populated");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test that setAllowobfuscation works with different appendTo parameters.
     */
    @Test
    public void testSetAllowobfuscationWithDifferentAppendToParameters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();

        element.appendTo(list1, true, true, true);
        element.appendTo(list2, false, false, false);
        element.appendTo(list3, true, false, true);

        // All should have allowObfuscation set to true
        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);
        KeepClassSpecification spec3 = (KeepClassSpecification) list3.get(0);

        assertTrue(spec1.allowObfuscation,
            "allowObfuscation should be true regardless of appendTo parameters");
        assertTrue(spec2.allowObfuscation,
            "allowObfuscation should be true regardless of appendTo parameters");
        assertTrue(spec3.allowObfuscation,
            "allowObfuscation should be true regardless of appendTo parameters");
    }

    /**
     * Test that setAllowobfuscation is independent across different instances.
     */
    @Test
    public void testSetAllowobfuscationIndependentInstances() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        element1.setName("com.example.first.**");
        element2.setName("com.example.second.**");

        element1.setAllowobfuscation(true);
        element2.setAllowobfuscation(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element1.appendTo(list1, true, true, false);
        element2.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.allowObfuscation,
            "First element should have allowObfuscation set to true");
        assertFalse(spec2.allowObfuscation,
            "Second element should have allowObfuscation set to false");
    }

    /**
     * Test that setAllowobfuscation works correctly when called before setName.
     */
    @Test
    public void testSetAllowobfuscationBeforeSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAllowobfuscation(true);
        element.setName("com.example.**");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true regardless of setter call order");
    }

    /**
     * Test that setAllowobfuscation works correctly when called after setName.
     */
    @Test
    public void testSetAllowobfuscationAfterSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setName("com.example.**");
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true regardless of setter call order");
    }

    /**
     * Test that setAllowobfuscation can be called on a freshly created element.
     */
    @Test
    public void testSetAllowobfuscationOnFreshElement() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowobfuscation(true),
            "Should be able to call setAllowobfuscation immediately after construction");
    }

    /**
     * Test that setAllowobfuscation works with parent class setters.
     */
    @Test
    public void testSetAllowobfuscationWithParentSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAccess("public");
        element.setType("class");
        element.setName("com.example.MyClass");
        element.setExtends("com.example.BaseClass");
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true when used with parent class setters");
    }

    /**
     * Test that setAllowobfuscation true is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetAllowobfuscationTruePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.allowObfuscation,
            "First appendTo should create specification with allowObfuscation true");
        assertTrue(spec2.allowObfuscation,
            "Second appendTo should create specification with allowObfuscation true");
    }

    /**
     * Test that setAllowobfuscation false is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetAllowobfuscationFalsePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertFalse(spec1.allowObfuscation,
            "First appendTo should create specification with allowObfuscation false");
        assertFalse(spec2.allowObfuscation,
            "Second appendTo should create specification with allowObfuscation false");
    }

    /**
     * Test that setAllowobfuscation works with all other keep specification setters.
     */
    @Test
    public void testSetAllowobfuscationWithAllKeepSpecificationSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(false);
        element.setAllowshrinking(true);
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true");
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true");
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true");
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true");
    }

    /**
     * Test that setAllowobfuscation can be used without setting a name.
     */
    @Test
    public void testSetAllowobfuscationWithoutSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true even without a name set");
    }

    /**
     * Test that setAllowobfuscation works correctly when the element extends a class.
     */
    @Test
    public void testSetAllowobfuscationWithExtendsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setExtends("com.example.BaseClass");
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true with extends clause");
    }

    /**
     * Test that setAllowobfuscation works correctly when the element implements an interface.
     */
    @Test
    public void testSetAllowobfuscationWithImplementsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setImplements("com.example.MyInterface");
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true with implements clause");
    }

    /**
     * Test that setAllowobfuscation true allows obfuscation.
     */
    @Test
    public void testSetAllowobfuscationTrueAllowsObfuscation() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation,
            "Setting allowobfuscation to true should allow obfuscation");
    }

    /**
     * Test that setAllowobfuscation false prevents obfuscation.
     */
    @Test
    public void testSetAllowobfuscationFalsePreventsObfuscation() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowObfuscation,
            "Setting allowobfuscation to false should prevent obfuscation");
    }

    /**
     * Test that setAllowobfuscation is independent from other allow flags.
     */
    @Test
    public void testSetAllowobfuscationIndependentFromOtherAllowFlags() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);
        element.setAllowshrinking(false);
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
    }

    /**
     * Test that all allow flags can be set to true independently.
     */
    @Test
    public void testSetAllowobfuscationWithAllAllowFlagsTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);
        element.setAllowshrinking(true);
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
    }

    /**
     * Test that all allow flags can be set to false independently.
     */
    @Test
    public void testSetAllowobfuscationWithAllAllowFlagsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(false);
        element.setAllowshrinking(false);
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
    }

    /**
     * Test that setAllowobfuscation can be used with different combinations of other allow flags.
     */
    @Test
    public void testSetAllowobfuscationWithMixedAllowFlags() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);
        element.setAllowshrinking(false);
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
    }

    /**
     * Test that setAllowobfuscation is independent from allowShrinking.
     */
    @Test
    public void testSetAllowobfuscationIndependentFromAllowShrinking() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);
        element.setAllowshrinking(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
    }

    /**
     * Test that setAllowobfuscation is independent from allowOptimization.
     */
    @Test
    public void testSetAllowobfuscationIndependentFromAllowOptimization() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowobfuscation(true);
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
    }
}
