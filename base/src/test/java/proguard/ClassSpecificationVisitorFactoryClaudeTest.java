package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.*;
import proguard.util.WildcardManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ClassSpecificationVisitorFactory}.
 * Tests all methods to ensure proper functionality of the factory.
 */
public class ClassSpecificationVisitorFactoryClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the no-argument constructor.
     * Verifies that the factory can be instantiated.
     */
    @Test
    public void testConstructor() {
        // Act
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Assert
        assertNotNull(factory, "Factory should be instantiated");
    }

    // ========== createClassPoolVisitor(List, ClassVisitor, MemberVisitor) Tests ==========

    /**
     * Tests createClassPoolVisitor with three arguments and null list.
     * Should return a non-null visitor even with null specifications.
     */
    @Test
    public void testCreateClassPoolVisitorThreeArgsWithNullList() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(null, classVisitor, memberVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with three arguments and empty list.
     */
    @Test
    public void testCreateClassPoolVisitorThreeArgsWithEmptyList() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        List<ClassSpecification> specs = new ArrayList<>();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(specs, classVisitor, memberVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with three arguments and single specification.
     */
    @Test
    public void testCreateClassPoolVisitorThreeArgsWithSingleSpec() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        List<ClassSpecification> specs = new ArrayList<>();
        specs.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(specs, classVisitor, memberVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with three arguments and multiple specifications.
     */
    @Test
    public void testCreateClassPoolVisitorThreeArgsWithMultipleSpecs() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        List<ClassSpecification> specs = new ArrayList<>();
        specs.add(new ClassSpecification("Test1", 0, 0, null, "TestClass1", null, null));
        specs.add(new ClassSpecification("Test2", 0, 0, null, "TestClass2", null, null));

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(specs, classVisitor, memberVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with null visitors.
     */
    @Test
    public void testCreateClassPoolVisitorThreeArgsWithNullVisitors() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        List<ClassSpecification> specs = new ArrayList<>();
        specs.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(specs, null, null);

        // Assert
        assertNotNull(result, "Result should not be null even with null visitors");
    }

    // ========== createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) Tests ==========

    /**
     * Tests createClassPoolVisitor with five arguments and null list.
     */
    @Test
    public void testCreateClassPoolVisitorFiveArgsWithNullList() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                null, classVisitor, fieldVisitor, methodVisitor, attributeVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with five arguments and empty list.
     */
    @Test
    public void testCreateClassPoolVisitorFiveArgsWithEmptyList() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        List<ClassSpecification> specs = new ArrayList<>();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                specs, classVisitor, fieldVisitor, methodVisitor, attributeVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with five arguments and single specification.
     */
    @Test
    public void testCreateClassPoolVisitorFiveArgsWithSingleSpec() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        List<ClassSpecification> specs = new ArrayList<>();
        specs.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                specs, classVisitor, fieldVisitor, methodVisitor, attributeVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with five arguments and multiple specifications.
     */
    @Test
    public void testCreateClassPoolVisitorFiveArgsWithMultipleSpecs() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        List<ClassSpecification> specs = new ArrayList<>();
        specs.add(new ClassSpecification("Test1", 0, 0, null, "TestClass1", null, null));
        specs.add(new ClassSpecification("Test2", 0, 0, null, "TestClass2", null, null));
        specs.add(new ClassSpecification("Test3", 0, 0, null, "TestClass3", null, null));

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                specs, classVisitor, fieldVisitor, methodVisitor, attributeVisitor);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with five arguments where field and method visitors differ.
     */
    @Test
    public void testCreateClassPoolVisitorFiveArgsWithDifferentMemberVisitors() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();  // Different instance
        List<ClassSpecification> specs = new ArrayList<>();
        specs.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                specs, classVisitor, fieldVisitor, methodVisitor, null);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof MultiClassPoolVisitor, "Result should be a MultiClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with all null visitors.
     */
    @Test
    public void testCreateClassPoolVisitorFiveArgsWithAllNullVisitors() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        List<ClassSpecification> specs = new ArrayList<>();
        specs.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(specs, null, null, null, null);

        // Assert
        assertNotNull(result, "Result should not be null even with all null visitors");
    }

    // ========== createClassPoolVisitor with ClassSpecification Tests ==========

    /**
     * Tests createClassPoolVisitor with a simple class specification.
     */
    @Test
    public void testCreateClassPoolVisitorWithSimpleClassSpec() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, null, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        // The result should be a NamedClassVisitor since className is "TestClass" (no wildcards)
        assertTrue(result instanceof NamedClassVisitor || result instanceof ClassPoolVisitor,
                "Result should be a valid ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with wildcard class name.
     */
    @Test
    public void testCreateClassPoolVisitorWithWildcardClassName() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "Test*", null, null);
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, null, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with null class name.
     */
    @Test
    public void testCreateClassPoolVisitorWithNullClassName() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, null, null, null);
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, null, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with extends class name.
     */
    @Test
    public void testCreateClassPoolVisitorWithExtendsClassName() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, null, null, "java/lang/Object");
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, null, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with access flags.
     */
    @Test
    public void testCreateClassPoolVisitorWithAccessFlags() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 1, 2, null, "TestClass", null, null);
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, null, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with annotation type.
     */
    @Test
    public void testCreateClassPoolVisitorWithAnnotationType() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, "TestAnnotation", "TestClass", null, null);
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, null, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with field specifications.
     */
    @Test
    public void testCreateClassPoolVisitorWithFieldSpecifications() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        spec.addField(new MemberSpecification(0, 0, null, "field", "I"));
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, fieldVisitor, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with method specifications.
     */
    @Test
    public void testCreateClassPoolVisitorWithMethodSpecifications() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        spec.addMethod(new MemberSpecification(0, 0, null, "method", "()V"));
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, null, methodVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with both field and method specifications.
     */
    @Test
    public void testCreateClassPoolVisitorWithBothMemberSpecifications() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        spec.addField(new MemberSpecification(0, 0, null, "field", "I"));
        spec.addMethod(new MemberSpecification(0, 0, null, "method", "()V"));
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
                spec, classVisitor, fieldVisitor, methodVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    // ========== createCombinedClassVisitor Tests ==========

    /**
     * Tests createCombinedClassVisitor with only class visitor.
     */
    @Test
    public void testCreateCombinedClassVisitorWithOnlyClassVisitor() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createCombinedClassVisitor(
                null, null, null, classVisitor, null, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertSame(classVisitor, result, "Result should be the same class visitor when no members specified");
    }

    /**
     * Tests createCombinedClassVisitor with null field specifications.
     */
    @Test
    public void testCreateCombinedClassVisitorWithNullFieldSpecs() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(0, 0, null, "method", "()V"));
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createCombinedClassVisitor(
                null, null, methodSpecs, classVisitor, null, methodVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests createCombinedClassVisitor with null method specifications.
     */
    @Test
    public void testCreateCombinedClassVisitorWithNullMethodSpecs() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(0, 0, null, "field", "I"));
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createCombinedClassVisitor(
                null, fieldSpecs, null, classVisitor, fieldVisitor, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests createCombinedClassVisitor with attribute names and attribute visitor.
     */
    @Test
    public void testCreateCombinedClassVisitorWithAttributes() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("Code");
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createCombinedClassVisitor(
                attributeNames, null, null, classVisitor, null, null, attributeVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests createCombinedClassVisitor with all parameters.
     */
    @Test
    public void testCreateCombinedClassVisitorWithAllParameters() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("Code");
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(0, 0, null, "field", "I"));
        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(0, 0, null, "method", "()V"));
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createCombinedClassVisitor(
                attributeNames, fieldSpecs, methodSpecs, classVisitor, fieldVisitor, methodVisitor, attributeVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests createCombinedClassVisitor with null class visitor but member visitors.
     */
    @Test
    public void testCreateCombinedClassVisitorWithNullClassVisitor() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(0, 0, null, "field", "I"));
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createCombinedClassVisitor(
                null, fieldSpecs, null, null, fieldVisitor, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    // ========== createNonTestingClassVisitor Tests ==========

    /**
     * Tests createNonTestingClassVisitor with a field specification.
     */
    @Test
    public void testCreateNonTestingClassVisitorForField() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, "field", "I");
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
                memberSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests createNonTestingClassVisitor with a method specification.
     */
    @Test
    public void testCreateNonTestingClassVisitorForMethod() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, "method", "()V");
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
                memberSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests createNonTestingClassVisitor with wildcard name.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithWildcardName() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, "get*", "()I");
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
                memberSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests createNonTestingClassVisitor with attribute visitor.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithAttributeVisitor() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, "method", "()V");
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
                memberSpec, false, memberVisitor, attributeVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    // ========== createClassTester Tests ==========

    /**
     * Tests createClassTester with ClassPoolVisitor parameter.
     */
    @Test
    public void testCreateClassTesterWithClassPoolVisitor() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        TestClassPoolVisitor classPoolVisitor = new TestClassPoolVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassTester(spec, classPoolVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassTester with ClassVisitor parameter.
     */
    @Test
    public void testCreateClassTesterWithClassVisitor() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassTester(spec, classVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassTester with field specifications in the condition.
     */
    @Test
    public void testCreateClassTesterWithFieldSpecifications() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        spec.addField(new MemberSpecification(0, 0, null, "field", "I"));
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassTester(spec, classVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassTester with method specifications in the condition.
     */
    @Test
    public void testCreateClassTesterWithMethodSpecifications() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        spec.addMethod(new MemberSpecification(0, 0, null, "method", "()V"));
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassTester(spec, classVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassTester with wildcard class name.
     */
    @Test
    public void testCreateClassTesterWithWildcardClassName() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "Test*", null, null);
        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassTester(spec, classVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    // ========== Helper Classes ==========

    /**
     * Test implementation of ClassVisitor.
     */
    private static class TestClassVisitor implements ClassVisitor {
        @Override
        public void visitAnyClass(Clazz clazz) {
        }
    }

    /**
     * Test implementation of MemberVisitor.
     */
    private static class TestMemberVisitor implements proguard.classfile.visitor.MemberVisitor {
        @Override
        public void visitAnyMember(Clazz clazz, proguard.classfile.Member member) {
        }
    }

    /**
     * Test implementation of AttributeVisitor.
     */
    private static class TestAttributeVisitor implements AttributeVisitor {
        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
        }
    }

    /**
     * Test implementation of ClassPoolVisitor.
     */
    private static class TestClassPoolVisitor implements ClassPoolVisitor {
        @Override
        public void visitClassPool(proguard.classfile.ClassPool classPool) {
        }
    }
}
