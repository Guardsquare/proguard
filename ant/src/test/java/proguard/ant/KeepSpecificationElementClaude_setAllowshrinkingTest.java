package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.KeepClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KeepSpecificationElement.setAllowshrinking method.
 */
public class KeepSpecificationElementClaude_setAllowshrinkingTest {

    /**
     * Test that setAllowshrinking accepts true value.
     */
    @Test
    public void testSetAllowshrinkingWithTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowshrinking(true),
            "Should accept true value");
    }

    /**
     * Test that setAllowshrinking accepts false value.
     */
    @Test
    public void testSetAllowshrinkingWithFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowshrinking(false),
            "Should accept false value");
    }

    /**
     * Test that setAllowshrinking with true propagates to KeepClassSpecification.
     */
    @Test
    public void testSetAllowshrinkingTruePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true when setAllowshrinking(true) is called");
    }

    /**
     * Test that setAllowshrinking with false propagates to KeepClassSpecification.
     */
    @Test
    public void testSetAllowshrinkingFalsePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.allowShrinking,
            "allowShrinking should be false when setAllowshrinking(false) is called");
    }

    /**
     * Test that default value (when setAllowshrinking is not called) is false.
     */
    @Test
    public void testAllowshrinkingDefaultValueIsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        // Don't call setAllowshrinking

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.allowShrinking,
            "allowShrinking should be false by default");
    }

    /**
     * Test that setAllowshrinking can be called multiple times,
     * with the last value taking effect.
     */
    @Test
    public void testSetAllowshrinkingMultipleTimes() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowshrinking(false);
        element.setAllowshrinking(true);
        element.setAllowshrinking(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowShrinking,
            "allowShrinking should be false (the last set value)");
    }

    /**
     * Test that setAllowshrinking can be toggled from false to true.
     */
    @Test
    public void testSetAllowshrinkingToggleFalseToTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowshrinking(false);
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true after toggling from false to true");
    }

    /**
     * Test that setAllowshrinking can be toggled from true to false.
     */
    @Test
    public void testSetAllowshrinkingToggleTrueToFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowshrinking(true);
        element.setAllowshrinking(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowShrinking,
            "allowShrinking should be false after toggling from true to false");
    }

    /**
     * Test that setAllowshrinking works correctly with other setters.
     */
    @Test
    public void testSetAllowshrinkingWithOtherSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(true);
        element.setAllowoptimization(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertTrue(keepSpec.markCodeAttributes, "markCodeAttributes should be true");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that setAllowshrinking works correctly with Ant project.
     */
    @Test
    public void testSetAllowshrinkingWithAntProject() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);
        element.setName("com.example.**");

        assertDoesNotThrow(() -> element.setAllowshrinking(true),
            "Should work with Ant project");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should be populated");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
    }

    /**
     * Test that setAllowshrinking works with different appendTo parameters.
     */
    @Test
    public void testSetAllowshrinkingWithDifferentAppendToParameters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();

        element.appendTo(list1, true, true, true);
        element.appendTo(list2, false, false, false);
        element.appendTo(list3, true, false, true);

        // All should have allowShrinking set to true
        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);
        KeepClassSpecification spec3 = (KeepClassSpecification) list3.get(0);

        assertTrue(spec1.allowShrinking,
            "allowShrinking should be true regardless of appendTo parameters");
        assertTrue(spec2.allowShrinking,
            "allowShrinking should be true regardless of appendTo parameters");
        assertTrue(spec3.allowShrinking,
            "allowShrinking should be true regardless of appendTo parameters");
    }

    /**
     * Test that setAllowshrinking is independent across different instances.
     */
    @Test
    public void testSetAllowshrinkingIndependentInstances() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        element1.setName("com.example.first.**");
        element2.setName("com.example.second.**");

        element1.setAllowshrinking(true);
        element2.setAllowshrinking(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element1.appendTo(list1, true, true, false);
        element2.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.allowShrinking,
            "First element should have allowShrinking set to true");
        assertFalse(spec2.allowShrinking,
            "Second element should have allowShrinking set to false");
    }

    /**
     * Test that setAllowshrinking works correctly when called before setName.
     */
    @Test
    public void testSetAllowshrinkingBeforeSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAllowshrinking(true);
        element.setName("com.example.**");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true regardless of setter call order");
    }

    /**
     * Test that setAllowshrinking works correctly when called after setName.
     */
    @Test
    public void testSetAllowshrinkingAfterSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setName("com.example.**");
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true regardless of setter call order");
    }

    /**
     * Test that setAllowshrinking can be called on a freshly created element.
     */
    @Test
    public void testSetAllowshrinkingOnFreshElement() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowshrinking(true),
            "Should be able to call setAllowshrinking immediately after construction");
    }

    /**
     * Test that setAllowshrinking works with parent class setters.
     */
    @Test
    public void testSetAllowshrinkingWithParentSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAccess("public");
        element.setType("class");
        element.setName("com.example.MyClass");
        element.setExtends("com.example.BaseClass");
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true when used with parent class setters");
    }

    /**
     * Test that setAllowshrinking true is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetAllowshrinkingTruePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.allowShrinking,
            "First appendTo should create specification with allowShrinking true");
        assertTrue(spec2.allowShrinking,
            "Second appendTo should create specification with allowShrinking true");
    }

    /**
     * Test that setAllowshrinking false is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetAllowshrinkingFalsePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertFalse(spec1.allowShrinking,
            "First appendTo should create specification with allowShrinking false");
        assertFalse(spec2.allowShrinking,
            "Second appendTo should create specification with allowShrinking false");
    }

    /**
     * Test that setAllowshrinking works with all other keep specification setters.
     */
    @Test
    public void testSetAllowshrinkingWithAllKeepSpecificationSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(false);
        element.setAllowoptimization(true);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true");
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true");
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true");
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true");
    }

    /**
     * Test that setAllowshrinking can be used without setting a name.
     */
    @Test
    public void testSetAllowshrinkingWithoutSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true even without a name set");
    }

    /**
     * Test that setAllowshrinking works correctly when the element extends a class.
     */
    @Test
    public void testSetAllowshrinkingWithExtendsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setExtends("com.example.BaseClass");
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true with extends clause");
    }

    /**
     * Test that setAllowshrinking works correctly when the element implements an interface.
     */
    @Test
    public void testSetAllowshrinkingWithImplementsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setImplements("com.example.MyInterface");
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true with implements clause");
    }

    /**
     * Test that setAllowshrinking true allows shrinking.
     */
    @Test
    public void testSetAllowshrinkingTrueAllowsShrinking() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setAllowshrinking(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking,
            "Setting allowshrinking to true should allow shrinking");
    }

    /**
     * Test that setAllowshrinking false prevents shrinking.
     */
    @Test
    public void testSetAllowshrinkingFalsePreventsShrinking() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setAllowshrinking(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowShrinking,
            "Setting allowshrinking to false should prevent shrinking");
    }

    /**
     * Test that setAllowshrinking is independent from other allow flags.
     */
    @Test
    public void testSetAllowshrinkingIndependentFromOtherAllowFlags() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);
        element.setAllowoptimization(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that all allow flags can be set to true independently.
     */
    @Test
    public void testSetAllowshrinkingWithAllAllowFlagsTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);
        element.setAllowoptimization(true);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test that all allow flags can be set to false independently.
     */
    @Test
    public void testSetAllowshrinkingWithAllAllowFlagsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(false);
        element.setAllowoptimization(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that setAllowshrinking can be used with different combinations of other allow flags.
     */
    @Test
    public void testSetAllowshrinkingWithMixedAllowFlags() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowshrinking(true);
        element.setAllowoptimization(false);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }
}
