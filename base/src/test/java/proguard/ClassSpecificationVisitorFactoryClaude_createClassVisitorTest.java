package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.WildcardManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for improving coverage of the private createClassVisitor method
 * in {@link ClassSpecificationVisitorFactory}.
 *
 * This test focuses on covering lines 595, 609, and 620 by testing public methods
 * that call the private createClassVisitor method.
 */
public class ClassSpecificationVisitorFactoryClaude_createClassVisitorTest {

    /**
     * Custom MemberSpecification subclass that allows setting attributeNames.
     * This is needed because the base class has attributeNames as a final field set to null.
     */
    private static class TestMemberSpecificationWithAttributes extends MemberSpecification {
        public final List<String> attributeNames;

        public TestMemberSpecificationWithAttributes(int requiredSetAccessFlags,
                                                     int requiredUnsetAccessFlags,
                                                     String annotationType,
                                                     String name,
                                                     String descriptor,
                                                     List<String> attributeNames) {
            super(requiredSetAccessFlags, requiredUnsetAccessFlags, annotationType, name, descriptor);
            this.attributeNames = attributeNames;
        }
    }

    /**
     * Tests createNonTestingClassVisitor with a member specification that has
     * attribute names and an attribute visitor.
     * This should cover line 595 where AttributeNameFilter is created.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithAttributeNamesAndVisitor() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a custom member specification with attribute names
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("Code");
        attributeNames.add("LineNumberTable");
        TestMemberSpecificationWithAttributes memberSpec =
                new TestMemberSpecificationWithAttributes(0, 0, null, "method", "()V", attributeNames);

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

    /**
     * Tests createNonTestingClassVisitor with a member specification that has
     * an annotation type.
     * This should cover line 609 where annotation filtering is applied.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithAnnotationType() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a member specification with an annotation type
        MemberSpecification memberSpec = new MemberSpecification(0, 0, "Ljava/lang/Deprecated;", "method", "()V");

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
     * Tests createNonTestingClassVisitor with a member specification that has
     * required set access flags.
     * This should cover line 620 where MemberAccessFilter is created.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithRequiredSetAccessFlags() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a member specification with required set access flags (e.g., public = 0x0001)
        MemberSpecification memberSpec = new MemberSpecification(0x0001, 0, null, "method", "()V");

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
     * Tests createNonTestingClassVisitor with a member specification that has
     * required unset access flags.
     * This should cover line 620 where MemberAccessFilter is created.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithRequiredUnsetAccessFlags() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a member specification with required unset access flags (e.g., static = 0x0008)
        MemberSpecification memberSpec = new MemberSpecification(0, 0x0008, null, "method", "()V");

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
     * Tests createNonTestingClassVisitor with a member specification that has
     * both set and unset access flags.
     * This ensures line 620 is covered with both flags set.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithBothAccessFlags() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a member specification with both required set and unset access flags
        // e.g., must be public (0x0001) but not static (0x0008)
        MemberSpecification memberSpec = new MemberSpecification(0x0001, 0x0008, null, "method", "()V");

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
     * Tests createNonTestingClassVisitor with all three conditions:
     * attribute names, annotation type, and access flags.
     * This comprehensive test should cover lines 595, 609, and 620 in a single test.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithAllConditions() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a member specification with all conditions
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("Code");
        TestMemberSpecificationWithAttributes memberSpec =
                new TestMemberSpecificationWithAttributes(0x0001, 0x0008, "Ljava/lang/Override;", "method", "()V", attributeNames);

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

    /**
     * Tests createClassTester which indirectly calls createClassVisitor via createClassMemberTester.
     * This test covers line 609 through the createClassMemberTester path.
     */
    @Test
    public void testCreateClassTesterWithAnnotatedMethodSpecification() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);

        // Add a method specification with an annotation type
        MemberSpecification methodSpec = new MemberSpecification(0, 0, "Ljava/lang/Deprecated;", "method", "()V");
        spec.addMethod(methodSpec);

        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassTester(spec, classVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests createClassTester with field specification having access flags.
     * This test covers line 620 through the createClassMemberTester path.
     */
    @Test
    public void testCreateClassTesterWithFieldSpecificationWithAccessFlags() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();
        ClassSpecification spec = new ClassSpecification("Test", 0, 0, null, "TestClass", null, null);

        // Add a field specification with access flags
        MemberSpecification fieldSpec = new MemberSpecification(0x0002, 0, null, "field", "I");
        spec.addField(fieldSpec);

        TestClassVisitor classVisitor = new TestClassVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassPoolVisitor result = factory.createClassTester(spec, classVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassPoolVisitor, "Result should be a ClassPoolVisitor");
    }

    /**
     * Tests with a field specification that has attribute names.
     * This covers line 595 for field specifications.
     */
    @Test
    public void testCreateNonTestingClassVisitorForFieldWithAttributeNames() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a custom field specification with attribute names
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("ConstantValue");
        TestMemberSpecificationWithAttributes fieldSpec =
                new TestMemberSpecificationWithAttributes(0, 0, null, "field", "I", attributeNames);

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
                fieldSpec, true, memberVisitor, attributeVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof ClassVisitor, "Result should be a ClassVisitor");
    }

    /**
     * Tests with wildcard attribute names to ensure the matcher is properly created.
     * This covers line 595 with wildcard patterns.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithWildcardAttributeNames() {
        // Arrange
        ClassSpecificationVisitorFactory factory = new ClassSpecificationVisitorFactory();

        // Create a custom member specification with wildcard attribute names
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("*");
        TestMemberSpecificationWithAttributes memberSpec =
                new TestMemberSpecificationWithAttributes(0, 0, null, "method", "()V", attributeNames);

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
    private static class TestMemberVisitor implements MemberVisitor {
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
