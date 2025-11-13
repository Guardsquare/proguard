package proguard;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.MultiClassPoolVisitor;
import proguard.classfile.visitor.NamedClassVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;

class KeepClassSpecificationVisitorFactoryDiffblueTest {
  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor2() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            false, false, false, false, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor3() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            false, false, false, false, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            new KotlinAnnotationCounter(),
            null);
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor4() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            false, false, false, false, true, true, true, null, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor5() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            false, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor6() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            0,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor7() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments", 1, 1, "", "Class Name", "Extends Annotation Type", "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor8() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition = new ClassSpecification();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor9() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name",
            fieldSpecifications,
            new ArrayList<>());
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor10() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);

    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    condition.addField(fieldSpecification);
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor11() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);

    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    condition.addMethod(methodSpecification);
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    condition.addField(fieldSpecification);
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor12() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);

    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    MemberSpecification fieldSpecification =
        new MemberSpecification(0, 1, "Annotation Type", "Name", "Descriptor");
    condition.addField(fieldSpecification);
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor13() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name",
            fieldSpecifications,
            new ArrayList<>());

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor14() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);

    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    classSpecification.addMethod(methodSpecification);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor15() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification, null, fieldVisitor, methodVisitor, new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor16() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification, classVisitor, null, methodVisitor, new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code
   * keepClassSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor},
   * {@code attributeVisitor}.
   *
   * <p>Method under test: {@link
   * KeepClassSpecificationVisitorFactory#createClassPoolVisitor(KeepClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(KeepClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitor17() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecification, classVisitor, fieldVisitor, null, new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(List, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code keepClassSpecifications}, {@code
   * classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code attributeVisitor}.
   *
   * <p>Method under test: {@link KeepClassSpecificationVisitorFactory#createClassPoolVisitor(List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'keepClassSpecifications', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor KeepClassSpecificationVisitorFactory.createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithKeepClassSpecificationsClassVisitorFieldVisitorMethodVisitorAttributeVisitor() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ArrayList<Object> keepClassSpecifications = new ArrayList<>();
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            keepClassSpecifications,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }
}
