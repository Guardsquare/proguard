package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}.
 */
public class KeepClassSpecificationVisitorFactoryClaude_createClassPoolVisitorTest {

    // Test helper visitors
    private static class TestClassVisitor implements ClassVisitor {
        public boolean visited = false;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visited = true;
        }
    }

    private static class TestMemberVisitor implements MemberVisitor {
        public boolean visited = false;

        @Override
        public void visitAnyMember(Clazz clazz, proguard.classfile.Member member) {
            visited = true;
        }
    }

    private static class TestAttributeVisitor implements AttributeVisitor {
        public boolean visited = false;

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
            visited = true;
        }
    }

    /**
     * Tests createClassPoolVisitor with null visitors returns a non-null result.
     * Tests that the method handles all null optional parameters gracefully.
     */
    @Test
    public void testCreateClassPoolVisitor_AllNullVisitors() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, null, classSpec
        );

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(keepSpec, null, null, null, null);

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor even with all null visitors");
    }

    /**
     * Tests createClassPoolVisitor with all visitors provided.
     */
    @Test
    public void testCreateClassPoolVisitor_AllVisitorsProvided() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, fieldVisitor, methodVisitor, attributeVisitor
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with markDescriptorClasses=true.
     * This should wrap field and method visitors with MemberDescriptorReferencedClassVisitor.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkDescriptorClasses() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, true, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, fieldVisitor, methodVisitor, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with markDescriptorClasses=true and null fieldVisitor.
     * Should create a MemberDescriptorReferencedClassVisitor for the methodVisitor.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkDescriptorClasses_NullFieldVisitor() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, true, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, methodVisitor, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with markDescriptorClasses=true and null methodVisitor.
     * Should create a MemberDescriptorReferencedClassVisitor for the fieldVisitor.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkDescriptorClasses_NullMethodVisitor() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, true, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, fieldVisitor, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with markClasses=false and markConditionally=false.
     * Should set classVisitor to null internally.
     */
    @Test
    public void testCreateClassPoolVisitor_NoMarkClasses_NoMarkConditionally() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            false, false, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with markCodeAttributes=true and attributeVisitor provided.
     * Should filter attributes to only CODE attributes.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkCodeAttributes() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, true, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, attributeVisitor
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with markCodeAttributes=false and attributeVisitor provided.
     * Should not filter attributes (attributeVisitor becomes null).
     */
    @Test
    public void testCreateClassPoolVisitor_NoMarkCodeAttributes() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, attributeVisitor
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with a condition specified.
     * Should create a conditional keep class pool visitor.
     */
    @Test
    public void testCreateClassPoolVisitor_WithCondition() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification condition = new ClassSpecification("Cond", 0, 0, null, "CondClass", null, null);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, condition, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor with condition");
    }

    /**
     * Tests createClassPoolVisitor with markConditionally=true.
     * Should create a conditional marking visitor.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkConditionally() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, true, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor with conditional marking");
    }

    /**
     * Tests createClassPoolVisitor with both condition and markConditionally.
     */
    @Test
    public void testCreateClassPoolVisitor_WithConditionAndMarkConditionally() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification condition = new ClassSpecification("Cond", 0, 0, null, "CondClass", null, null);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, true, false, false, false, false, false, condition, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor with both condition and conditional marking");
    }

    /**
     * Tests createClassPoolVisitor with wildcard class name.
     */
    @Test
    public void testCreateClassPoolVisitor_WildcardClassName() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "com.example.*", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor with wildcard class name");
    }

    /**
     * Tests createClassPoolVisitor with annotation type specified.
     */
    @Test
    public void testCreateClassPoolVisitor_WithAnnotationType() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, "Ljava/lang/Deprecated;", "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor with annotation type");
    }

    /**
     * Tests createClassPoolVisitor with extends class name specified.
     */
    @Test
    public void testCreateClassPoolVisitor_WithExtendsClassName() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, "BaseClass");
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor with extends class name");
    }

    /**
     * Tests createClassPoolVisitor with access flags specified.
     */
    @Test
    public void testCreateClassPoolVisitor_WithAccessFlags() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor with access flags");
    }

    /**
     * Tests createClassPoolVisitor with markDescriptorClasses=true but null classVisitor.
     * Should not create descriptor referenced class visitors since classVisitor is null.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkDescriptorClasses_NullClassVisitor() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, true, false, false, false, false, null, classSpec
        );
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, null, fieldVisitor, methodVisitor, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with markClasses=true and markConditionally=false.
     * ClassVisitor should be used.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkClassesOnly() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, false, false, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with only markConditionally=true.
     * ClassVisitor should be used even though markClasses is false.
     */
    @Test
    public void testCreateClassPoolVisitor_MarkConditionallyOnly() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            false, false, true, false, false, false, false, false, null, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, null, null, null
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor");
    }

    /**
     * Tests createClassPoolVisitor with complex scenario: all flags true and all visitors provided.
     */
    @Test
    public void testCreateClassPoolVisitor_ComplexScenario() {
        // Arrange
        KeepClassSpecificationVisitorFactory factory =
            new KeepClassSpecificationVisitorFactory(true, true, true);
        ClassSpecification condition = new ClassSpecification("Cond", 1, 2, "CondAnn", "CondClass", null, null);
        ClassSpecification classSpec = new ClassSpecification("Test", 4, 8, "Ann", "TestClass", "ExtAnn", "ExtClass");
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, true, true, true, false, false, false, condition, classSpec
        );
        TestClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor fieldVisitor = new TestMemberVisitor();
        TestMemberVisitor methodVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();

        // Act
        ClassPoolVisitor result = factory.createClassPoolVisitor(
            keepSpec, classVisitor, fieldVisitor, methodVisitor, attributeVisitor
        );

        // Assert
        assertNotNull(result, "Should return non-null ClassPoolVisitor in complex scenario");
    }
}
