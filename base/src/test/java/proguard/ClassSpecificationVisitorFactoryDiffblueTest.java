package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.AllClassVisitor;
import proguard.classfile.visitor.AllFieldVisitor;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.MultiClassPoolVisitor;
import proguard.classfile.visitor.MultiClassVisitor;
import proguard.classfile.visitor.NamedClassVisitor;
import proguard.classfile.visitor.NamedFieldVisitor;
import proguard.classfile.visitor.NamedMethodVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.util.ConstantStringFunction;
import proguard.util.WildcardManager;

class ClassSpecificationVisitorFactoryDiffblueTest {
  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager2() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            0,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager3() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, "", "Class Name", "Extends Annotation Type", "Extends Class Name");
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager4() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification = new ClassSpecification();

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classVisitor, atLeast(1)).visitProgramClass(Mockito.<ProgramClass>any());
    assertTrue(actualCreateClassPoolVisitorResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager5() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
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
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager6() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    classSpecification.addField(fieldSpecification);

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classVisitor, atLeast(1)).visitProgramClass(Mockito.<ProgramClass>any());
    assertTrue(actualCreateClassPoolVisitorResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager7() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    classSpecification.addMethod(methodSpecification);

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classVisitor, atLeast(1)).visitProgramClass(Mockito.<ProgramClass>any());
    assertTrue(actualCreateClassPoolVisitorResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager8() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification = new ClassSpecification();

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            null,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classVisitor, atLeast(1)).visitProgramClass(Mockito.<ProgramClass>any());
    assertTrue(actualCreateClassPoolVisitorResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager9() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
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
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            null,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager10() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
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
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            null,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager11() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    classSpecification.addField(new MemberSpecification());

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classVisitor, atLeast(1)).visitProgramClass(Mockito.<ProgramClass>any());
    assertTrue(actualCreateClassPoolVisitorResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager12() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    classSpecification.addField(fieldSpecification);

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            null,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classVisitor, atLeast(1)).visitProgramClass(Mockito.<ProgramClass>any());
    assertTrue(actualCreateClassPoolVisitorResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)} with {@code
   * classSpecification}, {@code classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code
   * attributeVisitor}, {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassPoolVisitor(ClassSpecification, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(ClassSpecification, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationClassVisitorFieldVisitorMethodVisitorAttributeVisitorWildcardManager13() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    classSpecification.addMethod(new MemberSpecification());

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecification,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            new WildcardManager());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classVisitor, atLeast(1)).visitProgramClass(Mockito.<ProgramClass>any());
    assertTrue(actualCreateClassPoolVisitorResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code classSpecifications}, {@code
   * classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code attributeVisitor}.
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'classSpecifications', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationsClassVisitorFieldVisitorMethodVisitorAttributeVisitor() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ArrayList<Object> classSpecifications = new ArrayList<>();
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecifications,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code classSpecifications}, {@code
   * classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code attributeVisitor}.
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'classSpecifications', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationsClassVisitorFieldVisitorMethodVisitorAttributeVisitor2() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            null, classVisitor, fieldVisitor, methodVisitor, new CodeAttributeEditor());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List, ClassVisitor,
   * MemberVisitor, MemberVisitor, AttributeVisitor)} with {@code classSpecifications}, {@code
   * classVisitor}, {@code fieldVisitor}, {@code methodVisitor}, {@code attributeVisitor}.
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor) with 'classSpecifications', 'classVisitor', 'fieldVisitor', 'methodVisitor', 'attributeVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor)"
  })
  void
      testCreateClassPoolVisitorWithClassSpecificationsClassVisitorFieldVisitorMethodVisitorAttributeVisitor3() {
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

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List, ClassVisitor,
   * MemberVisitor)} with {@code classSpecifications}, {@code classVisitor}, {@code memberVisitor}.
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List,
   * ClassVisitor, MemberVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(List, ClassVisitor, MemberVisitor) with 'classSpecifications', 'classVisitor', 'memberVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(List, ClassVisitor, MemberVisitor)"
  })
  void testCreateClassPoolVisitorWithClassSpecificationsClassVisitorMemberVisitor() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ArrayList<Object> classSpecifications = new ArrayList<>();
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecifications, classVisitor, new KotlinAnnotationCounter());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List, ClassVisitor,
   * MemberVisitor)} with {@code classSpecifications}, {@code classVisitor}, {@code memberVisitor}.
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List,
   * ClassVisitor, MemberVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(List, ClassVisitor, MemberVisitor) with 'classSpecifications', 'classVisitor', 'memberVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(List, ClassVisitor, MemberVisitor)"
  })
  void testCreateClassPoolVisitorWithClassSpecificationsClassVisitorMemberVisitor2() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        classSpecificationVisitorFactory.createClassPoolVisitor(
            null, classVisitor, new KotlinAnnotationCounter());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List, ClassVisitor,
   * MemberVisitor)} with {@code classSpecifications}, {@code classVisitor}, {@code memberVisitor}.
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createClassPoolVisitor(List,
   * ClassVisitor, MemberVisitor)}
   */
  @Test
  @DisplayName(
      "Test createClassPoolVisitor(List, ClassVisitor, MemberVisitor) with 'classSpecifications', 'classVisitor', 'memberVisitor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassPoolVisitor(List, ClassVisitor, MemberVisitor)"
  })
  void testCreateClassPoolVisitorWithClassSpecificationsClassVisitorMemberVisitor3() {
    // Arrange
    KeepClassSpecificationVisitorFactory keepClassSpecificationVisitorFactory =
        new KeepClassSpecificationVisitorFactory(true, true, true);
    ArrayList<Object> classSpecifications = new ArrayList<>();
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassPoolVisitorResult =
        keepClassSpecificationVisitorFactory.createClassPoolVisitor(
            classSpecifications, classVisitor, new KotlinAnnotationCounter());
    actualCreateClassPoolVisitorResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassPoolVisitorResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List, List, List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   *   <li>Then {@link ArrayList#ArrayList()} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List,
   * List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager); given '42'; when ArrayList() add '42'; then ArrayList() size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateCombinedClassVisitor_given42_whenArrayListAdd42_thenArrayListSizeIsOne() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ArrayList<Object> attributeNames = new ArrayList<>();
    attributeNames.add("42");
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ArrayList<Object> methodSpecifications = new ArrayList<>();

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateCombinedClassVisitorResult =
        classSpecificationVisitorFactory.createCombinedClassVisitor(
            attributeNames,
            fieldSpecifications,
            methodSpecifications,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            wildcardManager);
    LibraryClass libraryClass = new LibraryClass();
    actualCreateCombinedClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateCombinedClassVisitorResult instanceof MultiClassVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals(1, attributeNames.size());
    assertEquals("42", attributeNames.get(0));
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getName());
    assertNull(libraryClass.getSuperName());
    assertNull(libraryClass.getFeatureName());
    assertNull(libraryClass.interfaceNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(libraryClass.fields);
    assertNull(libraryClass.methods);
    assertNull(libraryClass.kotlinMetadata);
    assertEquals(0, libraryClass.getAccessFlags());
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, fieldVisitor.getCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List, List, List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   *   <li>Then {@link ArrayList#ArrayList()} size is two.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List,
   * List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager); given '42'; when ArrayList() add '42'; then ArrayList() size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateCombinedClassVisitor_given42_whenArrayListAdd42_thenArrayListSizeIsTwo() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ArrayList<Object> attributeNames = new ArrayList<>();
    attributeNames.add("42");
    attributeNames.add("42");
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ArrayList<Object> methodSpecifications = new ArrayList<>();

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateCombinedClassVisitorResult =
        classSpecificationVisitorFactory.createCombinedClassVisitor(
            attributeNames,
            fieldSpecifications,
            methodSpecifications,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            wildcardManager);
    LibraryClass libraryClass = new LibraryClass();
    actualCreateCombinedClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateCombinedClassVisitorResult instanceof MultiClassVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals(2, attributeNames.size());
    assertEquals("42", attributeNames.get(0));
    assertEquals("42", attributeNames.get(1));
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getName());
    assertNull(libraryClass.getSuperName());
    assertNull(libraryClass.getFeatureName());
    assertNull(libraryClass.interfaceNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(libraryClass.fields);
    assertNull(libraryClass.methods);
    assertNull(libraryClass.kotlinMetadata);
    assertEquals(0, libraryClass.getAccessFlags());
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, fieldVisitor.getCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List, List, List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List,
   * List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager); then ArrayList() Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateCombinedClassVisitor_thenArrayListEmpty() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ArrayList<Object> attributeNames = new ArrayList<>();
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ArrayList<Object> methodSpecifications = new ArrayList<>();

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateCombinedClassVisitorResult =
        classSpecificationVisitorFactory.createCombinedClassVisitor(
            attributeNames,
            fieldSpecifications,
            methodSpecifications,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            wildcardManager);
    LibraryClass libraryClass = new LibraryClass();
    actualCreateCombinedClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateCombinedClassVisitorResult instanceof MultiClassVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getName());
    assertNull(libraryClass.getSuperName());
    assertNull(libraryClass.getFeatureName());
    assertNull(libraryClass.interfaceNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(libraryClass.fields);
    assertNull(libraryClass.methods);
    assertNull(libraryClass.kotlinMetadata);
    assertEquals(0, libraryClass.getAccessFlags());
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, fieldVisitor.getCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
    assertTrue(attributeNames.isEmpty());
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List, List, List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>When {@link ClassVisitor}.
   *   <li>Then {@link ArrayList#ArrayList()} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List,
   * List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager); when ClassVisitor; then ArrayList() Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateCombinedClassVisitor_whenClassVisitor_thenArrayListEmpty() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ArrayList<Object> attributeNames = new ArrayList<>();
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ArrayList<Object> methodSpecifications = new ArrayList<>();
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateCombinedClassVisitorResult =
        classSpecificationVisitorFactory.createCombinedClassVisitor(
            attributeNames,
            fieldSpecifications,
            methodSpecifications,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            attributeVisitor,
            wildcardManager);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateCombinedClassVisitorResult instanceof MultiClassVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals(0, fieldVisitor.getCount());
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
    assertTrue(attributeNames.isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List, List, List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>When {@code null}.
   *   <li>Then {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List,
   * List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager); when 'null'; then 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateCombinedClassVisitor_whenNull_thenNull() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ArrayList<Object> attributeNames = new ArrayList<>();
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ArrayList<Object> methodSpecifications = new ArrayList<>();

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateCombinedClassVisitorResult =
        classSpecificationVisitorFactory.createCombinedClassVisitor(
            attributeNames,
            fieldSpecifications,
            methodSpecifications,
            classVisitor,
            null,
            methodVisitor,
            attributeVisitor,
            wildcardManager);
    LibraryClass libraryClass = new LibraryClass();
    actualCreateCombinedClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateCombinedClassVisitorResult instanceof MultiClassVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getName());
    assertNull(libraryClass.getSuperName());
    assertNull(libraryClass.getFeatureName());
    assertNull(libraryClass.interfaceNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(libraryClass.fields);
    assertNull(libraryClass.methods);
    assertNull(libraryClass.kotlinMetadata);
    assertNull(null);
    assertEquals(0, libraryClass.getAccessFlags());
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, methodVisitor.getCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
    assertTrue(attributeNames.isEmpty());
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List, List, List,
   * ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>When {@code null}.
   *   <li>Then {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationVisitorFactory#createCombinedClassVisitor(List,
   * List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager); when 'null'; then 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createCombinedClassVisitor(List, List, List, ClassVisitor, MemberVisitor, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateCombinedClassVisitor_whenNull_thenNull2() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ArrayList<Object> attributeNames = new ArrayList<>();
    ArrayList<Object> fieldSpecifications = new ArrayList<>();
    ArrayList<Object> methodSpecifications = new ArrayList<>();

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KotlinAnnotationCounter fieldVisitor = new KotlinAnnotationCounter();
    KotlinAnnotationCounter methodVisitor = new KotlinAnnotationCounter();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateCombinedClassVisitorResult =
        classSpecificationVisitorFactory.createCombinedClassVisitor(
            attributeNames,
            fieldSpecifications,
            methodSpecifications,
            classVisitor,
            fieldVisitor,
            methodVisitor,
            null,
            wildcardManager);
    LibraryClass libraryClass = new LibraryClass();
    actualCreateCombinedClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateCombinedClassVisitorResult instanceof MultiClassVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getName());
    assertNull(libraryClass.getSuperName());
    assertNull(libraryClass.getFeatureName());
    assertNull(libraryClass.interfaceNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(libraryClass.fields);
    assertNull(libraryClass.methods);
    assertNull(null);
    assertNull(libraryClass.kotlinMetadata);
    assertEquals(0, libraryClass.getAccessFlags());
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, fieldVisitor.getCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertTrue(attributeNames.isEmpty());
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedFieldVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertEquals("Name", memberSpecification.name);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor2() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(0, 1, "Annotation Type", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedFieldVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertEquals("Name", memberSpecification.name);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(0, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor3() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, null, "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedFieldVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertEquals("Name", memberSpecification.name);
    assertNull(memberSpecification.annotationType);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor4() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedFieldVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertEquals("Name", memberSpecification.name);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor5() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "///", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("///", memberSpecification.name);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor6() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", null, "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertNull(memberSpecification.name);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor7() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "///");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("///", memberSpecification.descriptor);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Name", memberSpecification.name);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor8() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", null);
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Name", memberSpecification.name);
    assertNull(memberSpecification.descriptor);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor9() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, null, attributeVisitor, wildcardManager);
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    actualCreateNonTestingClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedFieldVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertEquals("Name", memberSpecification.name);
    assertEquals("Super Class Name", libraryClass.getSuperName());
    assertEquals("This Class Name", libraryClass.getName());
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getFeatureName());
    assertNull(memberSpecification.attributeNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(libraryClass.kotlinMetadata);
    assertNull(null);
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.fields.length);
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.interfaceNames.length);
    assertEquals(0, libraryClass.methods.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(1, libraryClass.getAccessFlags());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor10() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, null, wildcardManager);
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    actualCreateNonTestingClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedFieldVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertEquals("Name", memberSpecification.name);
    assertEquals("Super Class Name", libraryClass.getSuperName());
    assertEquals("This Class Name", libraryClass.getName());
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getFeatureName());
    assertNull(memberSpecification.attributeNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(null);
    assertNull(libraryClass.kotlinMetadata);
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, memberVisitor.getCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.fields.length);
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.interfaceNames.length);
    assertEquals(0, libraryClass.methods.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(1, libraryClass.getAccessFlags());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>Then {@link MemberSpecification#MemberSpecification()} {@link
   *       MemberSpecification#annotationType} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager); then MemberSpecification() annotationType is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor_thenMemberSpecificationAnnotationTypeIsNull() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification = new MemberSpecification();
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertNull(memberSpecification.annotationType);
    assertNull(memberSpecification.descriptor);
    assertNull(memberSpecification.name);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(0, memberSpecification.requiredSetAccessFlags);
    assertEquals(0, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then return {@link NamedMethodVisitor}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification, boolean,
   * MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager); when 'false'; then return NamedMethodVisitor")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor ClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor_whenFalse_thenReturnNamedMethodVisitor() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        classSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, false, null, attributeVisitor, wildcardManager);
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    actualCreateNonTestingClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    assertTrue(
        classSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedMethodVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertEquals("Name", memberSpecification.name);
    assertEquals("Super Class Name", libraryClass.getSuperName());
    assertEquals("This Class Name", libraryClass.getName());
    assertNull(libraryClass.getProcessingInfo());
    assertNull(libraryClass.getFeatureName());
    assertNull(memberSpecification.attributeNames);
    assertNull(libraryClass.getSuperClass());
    assertNull(libraryClass.kotlinMetadata);
    assertNull(null);
    assertEquals(0, libraryClass.getInterfaceCount());
    assertEquals(0, libraryClass.getProcessingFlags());
    assertEquals(0, libraryClass.fields.length);
    assertEquals(0, libraryClass.interfaceClasses.length);
    assertEquals(0, libraryClass.interfaceNames.length);
    assertEquals(0, libraryClass.methods.length);
    assertEquals(0, libraryClass.subClasses.length);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(1, libraryClass.getAccessFlags());
    assertEquals(1, memberSpecification.requiredSetAccessFlags);
    assertEquals(1, memberSpecification.requiredUnsetAccessFlags);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
    assertTrue(libraryClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager2() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            0,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager3() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, "", "Class Name", "Extends Annotation Type", "Extends Class Name");
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager4() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
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
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager5() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    classSpecification.addField(fieldSpecification);
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager6() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    classSpecification.addMethod(methodSpecification);
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager7() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    classSpecification.addField(new MemberSpecification());
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassPoolVisitor, WildcardManager)} with {@code classSpecification}, {@code classPoolVisitor},
   * {@code wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassPoolVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager) with 'classSpecification', 'classPoolVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassPoolVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassPoolVisitorWildcardManager8() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    classSpecification.addMethod(new MemberSpecification());
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classPoolVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof MultiClassPoolVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager2() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            0,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager3() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, "", "Class Name", "Extends Annotation Type", "Extends Class Name");
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager4() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
    ClassSpecification classSpecification = new ClassSpecification();
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(new ClassPool());

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager5() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();
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
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager6() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification =
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
    classSpecification.addField(fieldSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager7() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

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
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof NamedClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager8() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    classSpecification.addField(new MemberSpecification());
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(new ClassPool());

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager9() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification = new ClassSpecification();
    classSpecification.addMethod(new MemberSpecification());
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(new ClassPool());

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof AllClassVisitor);
  }

  /**
   * Test {@link ClassSpecificationVisitorFactory#createClassTester(ClassSpecification,
   * ClassVisitor, WildcardManager)} with {@code classSpecification}, {@code classVisitor}, {@code
   * wildcardManager}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationVisitorFactory#createClassTester(ClassSpecification, ClassVisitor,
   * WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createClassTester(ClassSpecification, ClassVisitor, WildcardManager) with 'classSpecification', 'classVisitor', 'wildcardManager'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassPoolVisitor ClassSpecificationVisitorFactory.createClassTester(ClassSpecification, ClassVisitor, WildcardManager)"
  })
  void testCreateClassTesterWithClassSpecificationClassVisitorWildcardManager10() {
    // Arrange
    ClassSpecificationVisitorFactory classSpecificationVisitorFactory =
        new ClassSpecificationVisitorFactory();

    ClassSpecification classSpecification =
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
    classSpecification.addField(fieldSpecification);
    ClassVisitor classVisitor = mock(ClassVisitor.class);

    // Act
    ClassPoolVisitor actualCreateClassTesterResult =
        classSpecificationVisitorFactory.createClassTester(
            classSpecification, classVisitor, new WildcardManager());
    actualCreateClassTesterResult.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    assertTrue(actualCreateClassTesterResult instanceof NamedClassVisitor);
  }
}
