package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.KeepClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KeepSpecificationElement.setAllowoptimization method.
 */
public class KeepSpecificationElementClaude_setAllowoptimizationTest {

    /**
     * Test that setAllowoptimization accepts true value.
     */
    @Test
    public void testSetAllowoptimizationWithTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowoptimization(true),
            "Should accept true value");
    }

    /**
     * Test that setAllowoptimization accepts false value.
     */
    @Test
    public void testSetAllowoptimizationWithFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowoptimization(false),
            "Should accept false value");
    }

    /**
     * Test that setAllowoptimization with true propagates to KeepClassSpecification.
     */
    @Test
    public void testSetAllowoptimizationTruePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true when setAllowoptimization(true) is called");
    }

    /**
     * Test that setAllowoptimization with false propagates to KeepClassSpecification.
     */
    @Test
    public void testSetAllowoptimizationFalsePropagates() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.allowOptimization,
            "allowOptimization should be false when setAllowoptimization(false) is called");
    }

    /**
     * Test that default value (when setAllowoptimization is not called) is false.
     */
    @Test
    public void testAllowoptimizationDefaultValueIsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        // Don't call setAllowoptimization

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");
        assertEquals(1, keepSpecifications.size(), "Should have exactly one specification");

        Object spec = keepSpecifications.get(0);
        assertInstanceOf(KeepClassSpecification.class, spec,
            "Should create a KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.allowOptimization,
            "allowOptimization should be false by default");
    }

    /**
     * Test that setAllowoptimization can be called multiple times,
     * with the last value taking effect.
     */
    @Test
    public void testSetAllowoptimizationMultipleTimes() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowoptimization(false);
        element.setAllowoptimization(true);
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowOptimization,
            "allowOptimization should be false (the last set value)");
    }

    /**
     * Test that setAllowoptimization can be toggled from false to true.
     */
    @Test
    public void testSetAllowoptimizationToggleFalseToTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowoptimization(false);
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true after toggling from false to true");
    }

    /**
     * Test that setAllowoptimization can be toggled from true to false.
     */
    @Test
    public void testSetAllowoptimizationToggleTrueToFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        element.setAllowoptimization(true);
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowOptimization,
            "allowOptimization should be false after toggling from true to false");
    }

    /**
     * Test that setAllowoptimization works correctly with other setters.
     */
    @Test
    public void testSetAllowoptimizationWithOtherSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(true);
        element.setAllowshrinking(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertTrue(keepSpec.markCodeAttributes, "markCodeAttributes should be true");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that setAllowoptimization works correctly with Ant project.
     */
    @Test
    public void testSetAllowoptimizationWithAntProject() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);
        element.setName("com.example.**");

        assertDoesNotThrow(() -> element.setAllowoptimization(true),
            "Should work with Ant project");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should be populated");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
    }

    /**
     * Test that setAllowoptimization works with different appendTo parameters.
     */
    @Test
    public void testSetAllowoptimizationWithDifferentAppendToParameters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();

        element.appendTo(list1, true, true, true);
        element.appendTo(list2, false, false, false);
        element.appendTo(list3, true, false, true);

        // All should have allowOptimization set to true
        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);
        KeepClassSpecification spec3 = (KeepClassSpecification) list3.get(0);

        assertTrue(spec1.allowOptimization,
            "allowOptimization should be true regardless of appendTo parameters");
        assertTrue(spec2.allowOptimization,
            "allowOptimization should be true regardless of appendTo parameters");
        assertTrue(spec3.allowOptimization,
            "allowOptimization should be true regardless of appendTo parameters");
    }

    /**
     * Test that setAllowoptimization is independent across different instances.
     */
    @Test
    public void testSetAllowoptimizationIndependentInstances() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        element1.setName("com.example.first.**");
        element2.setName("com.example.second.**");

        element1.setAllowoptimization(true);
        element2.setAllowoptimization(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element1.appendTo(list1, true, true, false);
        element2.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.allowOptimization,
            "First element should have allowOptimization set to true");
        assertFalse(spec2.allowOptimization,
            "Second element should have allowOptimization set to false");
    }

    /**
     * Test that setAllowoptimization works correctly when called before setName.
     */
    @Test
    public void testSetAllowoptimizationBeforeSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAllowoptimization(true);
        element.setName("com.example.**");

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true regardless of setter call order");
    }

    /**
     * Test that setAllowoptimization works correctly when called after setName.
     */
    @Test
    public void testSetAllowoptimizationAfterSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setName("com.example.**");
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true regardless of setter call order");
    }

    /**
     * Test that setAllowoptimization can be called on a freshly created element.
     */
    @Test
    public void testSetAllowoptimizationOnFreshElement() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setAllowoptimization(true),
            "Should be able to call setAllowoptimization immediately after construction");
    }

    /**
     * Test that setAllowoptimization works with parent class setters.
     */
    @Test
    public void testSetAllowoptimizationWithParentSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        element.setAccess("public");
        element.setType("class");
        element.setName("com.example.MyClass");
        element.setExtends("com.example.BaseClass");
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true when used with parent class setters");
    }

    /**
     * Test that setAllowoptimization true is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetAllowoptimizationTruePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertTrue(spec1.allowOptimization,
            "First appendTo should create specification with allowOptimization true");
        assertTrue(spec2.allowOptimization,
            "Second appendTo should create specification with allowOptimization true");
    }

    /**
     * Test that setAllowoptimization false is preserved across multiple appendTo calls.
     */
    @Test
    public void testSetAllowoptimizationFalsePreservedAcrossMultipleAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(false);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element.appendTo(list1, true, true, false);
        element.appendTo(list2, true, true, false);

        KeepClassSpecification spec1 = (KeepClassSpecification) list1.get(0);
        KeepClassSpecification spec2 = (KeepClassSpecification) list2.get(0);

        assertFalse(spec1.allowOptimization,
            "First appendTo should create specification with allowOptimization false");
        assertFalse(spec2.allowOptimization,
            "Second appendTo should create specification with allowOptimization false");
    }

    /**
     * Test that setAllowoptimization works with all other keep specification setters.
     */
    @Test
    public void testSetAllowoptimizationWithAllKeepSpecificationSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);
        element.setIncludecode(true);
        element.setIncludedescriptorclasses(false);
        element.setAllowshrinking(true);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true");
        assertTrue(keepSpec.markCodeAttributes,
            "markCodeAttributes should be true");
        assertFalse(keepSpec.markDescriptorClasses,
            "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking,
            "allowShrinking should be true");
        assertTrue(keepSpec.allowObfuscation,
            "allowObfuscation should be true");
    }

    /**
     * Test that setAllowoptimization can be used without setting a name.
     */
    @Test
    public void testSetAllowoptimizationWithoutSetName() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true even without a name set");
    }

    /**
     * Test that setAllowoptimization works correctly when the element extends a class.
     */
    @Test
    public void testSetAllowoptimizationWithExtendsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setExtends("com.example.BaseClass");
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true with extends clause");
    }

    /**
     * Test that setAllowoptimization works correctly when the element implements an interface.
     */
    @Test
    public void testSetAllowoptimizationWithImplementsClause() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setImplements("com.example.MyInterface");
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should not be empty");

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "allowOptimization should be true with implements clause");
    }

    /**
     * Test that setAllowoptimization true allows optimization.
     */
    @Test
    public void testSetAllowoptimizationTrueAllowsOptimization() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setAllowoptimization(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization,
            "Setting allowoptimization to true should allow optimization");
    }

    /**
     * Test that setAllowoptimization false prevents optimization.
     */
    @Test
    public void testSetAllowoptimizationFalsePreventsOptimization() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.MyClass");
        element.setAllowoptimization(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowOptimization,
            "Setting allowoptimization to false should prevent optimization");
    }

    /**
     * Test that setAllowoptimization is independent from other allow flags.
     */
    @Test
    public void testSetAllowoptimizationIndependentFromOtherAllowFlags() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);
        element.setAllowshrinking(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that all allow flags can be set to true independently.
     */
    @Test
    public void testSetAllowoptimizationWithAllAllowFlagsTrue() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);
        element.setAllowshrinking(true);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test that all allow flags can be set to false independently.
     */
    @Test
    public void testSetAllowoptimizationWithAllAllowFlagsFalse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(false);
        element.setAllowshrinking(false);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that setAllowoptimization can be used with different combinations of other allow flags.
     */
    @Test
    public void testSetAllowoptimizationWithMixedAllowFlags() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);
        element.setAllowshrinking(false);
        element.setAllowobfuscation(true);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test that setAllowoptimization is independent from allowShrinking.
     */
    @Test
    public void testSetAllowoptimizationIndependentFromAllowShrinking() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);
        element.setAllowshrinking(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
    }

    /**
     * Test that setAllowoptimization is independent from allowObfuscation.
     */
    @Test
    public void testSetAllowoptimizationIndependentFromAllowObfuscation() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");
        element.setAllowoptimization(true);
        element.setAllowobfuscation(false);

        List keepSpecifications = new ArrayList();
        element.appendTo(keepSpecifications, true, true, false);

        KeepClassSpecification keepSpec = (KeepClassSpecification) keepSpecifications.get(0);
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }
}
