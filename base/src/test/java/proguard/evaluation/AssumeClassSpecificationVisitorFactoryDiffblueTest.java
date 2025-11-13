package proguard.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.MemberSpecification;
import proguard.MemberValueSpecification;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.visitor.AllFieldVisitor;
import proguard.classfile.visitor.AllMethodVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.MultiClassPoolVisitor;
import proguard.classfile.visitor.NamedFieldVisitor;
import proguard.classfile.visitor.NamedMethodVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.util.ConstantStringFunction;
import proguard.util.WildcardManager;

class AssumeClassSpecificationVisitorFactoryDiffblueTest {
  /**
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor2() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberSpecification memberSpecification =
        new MemberSpecification(0, 1, "Annotation Type", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor3() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor4() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    Number[] values = new Number[] {Integer.valueOf(1)};
    MemberValueSpecification memberSpecification =
        new MemberValueSpecification(1, 1, "Annotation Type", "Name", "Descriptor", values);
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
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
    assertEquals(1, memberSpecification.values.length);
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor5() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    Number[] values = new Number[] {Integer.valueOf(1)};
    MemberValueSpecification memberSpecification =
        new MemberValueSpecification(1, 1, "Annotation Type", null, "Descriptor", values);
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Descriptor", memberSpecification.descriptor);
    assertNull(memberSpecification.name);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.values.length);
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor6() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    Number[] values = new Number[] {Integer.valueOf(1)};
    MemberValueSpecification memberSpecification =
        new MemberValueSpecification(1, 1, "Annotation Type", "Name", null, values);
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertEquals("Annotation Type", memberSpecification.annotationType);
    assertEquals("Name", memberSpecification.name);
    assertNull(memberSpecification.descriptor);
    assertNull(memberSpecification.attributeNames);
    assertEquals(0, memberVisitor.getCount());
    assertEquals(1, memberSpecification.values.length);
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor7() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberValueSpecification memberSpecification =
        new MemberValueSpecification(
            1,
            1,
            "Annotation Type",
            "Name",
            "Descriptor",
            new Number[] {Integer.valueOf(1), Integer.valueOf(1)});
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
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
    assertEquals(2, memberSpecification.values.length);
    assertEquals(8096, attributeVisitor.deleted.length);
    assertEquals(8096, attributeVisitor.postInsertions.length);
    assertEquals(8096, attributeVisitor.preInsertions.length);
    assertEquals(8096, attributeVisitor.preOffsetInsertions.length);
    assertEquals(8096, attributeVisitor.replacements.length);
    assertFalse(attributeVisitor.isModified());
  }

  /**
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor8() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, null, attributeVisitor, wildcardManager);
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    actualCreateNonTestingClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor9() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, true, memberVisitor, null, wildcardManager);
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    actualCreateNonTestingClassVisitorResult.visitAnyClass(libraryClass);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>Then {@link MemberValueSpecification#MemberValueSpecification()} {@link
   *       MemberValueSpecification#values} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager); then MemberValueSpecification() values is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor_thenMemberValueSpecificationValuesIsNull() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberValueSpecification memberSpecification = new MemberValueSpecification();
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, true, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllFieldVisitor);
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertNull(memberSpecification.values);
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>Then return {@link AllMethodVisitor}.
   * </ul>
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager); then return AllMethodVisitor")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor_thenReturnAllMethodVisitor() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberValueSpecification memberSpecification = new MemberValueSpecification();
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act and Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
                memberSpecification, false, memberVisitor, attributeVisitor, wildcardManager)
            instanceof AllMethodVisitor);
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(
        wildcardManager.createMatchedStringFunction("Expression")
            instanceof ConstantStringFunction);
    assertNull(memberSpecification.values);
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
   * Test {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then return {@link NamedMethodVisitor}.
   * </ul>
   *
   * <p>Method under test: {@link
   * AssumeClassSpecificationVisitorFactory#createNonTestingClassVisitor(MemberSpecification,
   * boolean, MemberVisitor, AttributeVisitor, WildcardManager)}
   */
  @Test
  @DisplayName(
      "Test createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager); when 'false'; then return NamedMethodVisitor")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassVisitor AssumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(MemberSpecification, boolean, MemberVisitor, AttributeVisitor, WildcardManager)"
  })
  void testCreateNonTestingClassVisitor_whenFalse_thenReturnNamedMethodVisitor() {
    // Arrange
    AssumeClassSpecificationVisitorFactory assumeClassSpecificationVisitorFactory =
        new AssumeClassSpecificationVisitorFactory(new ParticularReferenceValueFactory());
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    CodeAttributeEditor attributeVisitor = new CodeAttributeEditor();
    WildcardManager wildcardManager = new WildcardManager();

    // Act
    ClassVisitor actualCreateNonTestingClassVisitorResult =
        assumeClassSpecificationVisitorFactory.createNonTestingClassVisitor(
            memberSpecification, false, memberVisitor, attributeVisitor, wildcardManager);

    // Assert
    assertTrue(
        assumeClassSpecificationVisitorFactory.createClassPoolVisitor(null, null, null)
            instanceof MultiClassPoolVisitor);
    assertTrue(actualCreateNonTestingClassVisitorResult instanceof NamedMethodVisitor);
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
}
