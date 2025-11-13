package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMember;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.ConstantValueAttribute;
import proguard.classfile.attribute.DeprecatedAttribute;
import proguard.classfile.attribute.EnclosingMethodAttribute;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeInfo;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.RecordAttribute;
import proguard.classfile.attribute.RecordComponentInfo;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationElementValue;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.ArrayElementValue;
import proguard.classfile.attribute.annotation.ClassElementValue;
import proguard.classfile.attribute.annotation.ConstantElementValue;
import proguard.classfile.attribute.annotation.ElementValue;
import proguard.classfile.attribute.annotation.EnumConstantElementValue;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.testutils.cpa.NamedField;
import proguard.util.Processable;

@ExtendWith(MockitoExtension.class)
class ClassShrinkerDiffblueTest {
  @InjectMocks private ClassShrinker classShrinker;

  @Mock private SimpleUsageMarker simpleUsageMarker;

  /**
   * Test {@link ClassShrinker#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnyClass(Clazz)"})
  void testVisitAnyClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class, () -> classShrinker.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 1, constantPool, 1, 1, 1);

    // Act
    classShrinker.visitProgramClass(programClass);

    // Assert that nothing has changed
    assertEquals(0, programClass.subClassCount);
    assertEquals(1, programClass.u2constantPoolCount);
    assertEquals(1, programClass.u2superClass);
    assertEquals(1, programClass.u2thisClass);
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    ClassConstant classConstant = new ClassConstant();
    ClassConstant classConstant2 = new ClassConstant();
    ProgramClass programClass =
        new ProgramClass(
            1, 3, new Constant[] {classConstant, classConstant2, new ClassConstant()}, 1, 4, 4);

    // Act
    classShrinker.visitProgramClass(programClass);

    // Assert
    assertNull(programClass.getSuperName());
    assertNull(programClass.getSuperClass());
    Constant[] constantArray = programClass.constantPool;
    assertNull(constantArray[1]);
    assertNull(constantArray[2]);
    assertEquals(0, programClass.u2superClass);
    assertEquals(0, programClass.u2thisClass);
    assertEquals(1, programClass.u2constantPoolCount);
    assertEquals(3, constantArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass3() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ClassConstant classConstant = new ClassConstant();
    ProgramClass programClass =
        new ProgramClass(1, 2, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classShrinker.visitProgramClass(programClass);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    assertEquals(1, programClass.u2superClass);
    assertEquals(1, programClass.u2thisClass);
    assertEquals(2, programClass.u2constantPoolCount);
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass4() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ClassConstant classConstant = new ClassConstant();

    ProgramClass programClass =
        new ProgramClass(1, 2, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1);
    programClass.addSubClass(new LibraryClass());

    // Act
    classShrinker.visitProgramClass(programClass);

    // Assert that nothing has changed
    verify(simpleUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
    assertEquals(1, programClass.u2superClass);
    assertEquals(1, programClass.u2thisClass);
    assertEquals(2, programClass.u2constantPoolCount);
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass5() {
    // Arrange
    ProgramClass programClass =
        new ProgramClass(1, 2, new Constant[] {new ClassConstant(), null}, 1, 2, 2);

    // Act
    classShrinker.visitProgramClass(programClass);

    // Assert
    assertNull(programClass.getSuperName());
    assertNull(programClass.getSuperClass());
    assertEquals(0, programClass.u2superClass);
    assertEquals(0, programClass.u2thisClass);
    assertEquals(1, programClass.u2constantPoolCount);
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassShrinker#ClassShrinker(SimpleUsageMarker)} with usageMarker is {@link
   *       ShortestUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassShrinker(SimpleUsageMarker) with usageMarker is ShortestUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassShrinkerWithUsageMarkerIsShortestUsageMarker() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    Constant[] constantPool = new Constant[] {new ClassConstant()};

    ProgramClass programClass = new ProgramClass(1, 1, constantPool, 1, 1, 1);
    programClass.addSubClass(new LibraryClass());

    // Act
    classShrinker.visitProgramClass(programClass);

    // Assert
    Clazz[] clazzArray = programClass.subClasses;
    assertNull(clazzArray[0]);
    assertEquals(0, programClass.subClassCount);
    assertEquals(1, clazzArray.length);
    assertEquals(1, programClass.u2constantPoolCount);
    assertEquals(1, programClass.u2superClass);
    assertEquals(1, programClass.u2thisClass);
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    Constant[] constantPool = new Constant[] {new ClassConstant()};

    ProgramClass programClass = new ProgramClass(1, 1, constantPool, 1, 1, 1);
    programClass.addSubClass(new LibraryClass());

    // Act
    classShrinker.visitProgramClass(programClass);

    // Assert
    Clazz[] clazzArray = programClass.subClasses;
    assertNull(clazzArray[0]);
    assertEquals(0, programClass.subClassCount);
    assertEquals(1, clazzArray.length);
    assertEquals(1, programClass.u2constantPoolCount);
    assertEquals(1, programClass.u2superClass);
    assertEquals(1, programClass.u2thisClass);
  }

  /**
   * Test {@link ClassShrinker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    ClassConstant classConstant = new ClassConstant();

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitProgramClass(
                new ProgramClass(
                    1, 3, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassShrinker#ClassShrinker(SimpleUsageMarker)} with usageMarker is {@link
   *       ShortestUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryClass(LibraryClass); given ClassShrinker(SimpleUsageMarker) with usageMarker is ShortestUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenClassShrinkerWithUsageMarkerIsShortestUsageMarker() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());

    LibraryClass libraryClass = new LibraryClass();
    libraryClass.addSubClass(new LibraryClass());

    // Act
    classShrinker.visitLibraryClass(libraryClass);

    // Assert
    Clazz[] clazzArray = libraryClass.subClasses;
    assertNull(clazzArray[0]);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(1, clazzArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());

    LibraryClass libraryClass = new LibraryClass();
    libraryClass.addSubClass(new LibraryClass());

    // Act
    classShrinker.visitLibraryClass(libraryClass);

    // Assert
    Clazz[] clazzArray = libraryClass.subClasses;
    assertNull(clazzArray[0]);
    assertEquals(0, libraryClass.subClassCount);
    assertEquals(1, clazzArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then first element {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then first element LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenFirstElementLibraryClass() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);

    LibraryClass libraryClass = new LibraryClass();
    LibraryClass clazz = new LibraryClass();
    libraryClass.addSubClass(clazz);

    // Act
    classShrinker.visitLibraryClass(libraryClass);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Clazz[] clazzArray = libraryClass.subClasses;
    Clazz clazz2 = clazzArray[0];
    assertTrue(clazz2 instanceof LibraryClass);
    assertEquals(1, clazzArray.length);
    assertEquals(1, libraryClass.subClassCount);
    assertSame(clazz, clazz2);
  }

  /**
   * Test {@link ClassShrinker#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());

    LibraryClass libraryClass = new LibraryClass();
    libraryClass.addSubClass(new LibraryClass());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class, () -> classShrinker.visitLibraryClass(libraryClass));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); when LibraryClass()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_whenLibraryClass() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass libraryClass = new LibraryClass();

    // Act
    classShrinker.visitLibraryClass(libraryClass);

    // Assert that nothing has changed
    assertEquals(0, libraryClass.subClassCount);
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName("Test visitProgramMember(ProgramClass, ProgramMember)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    ProgramClass programClass = new ProgramClass();
    NamedField programMember = new NamedField("Field Name", "Field Descriptor");

    // Act
    classShrinker.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    assertEquals(0, programMember.u2attributesCount);
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Given {@link ClassShrinker#ClassShrinker(SimpleUsageMarker)} with usageMarker is {@link
   *       ShortestUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); given ClassShrinker(SimpleUsageMarker) with usageMarker is ShortestUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_givenClassShrinkerWithUsageMarkerIsShortestUsageMarker() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    ProgramClass programClass = new ProgramClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    ProgramField programMember = new ProgramField(1, 1, 1, 1, attributes, new LibraryClass());

    // Act
    classShrinker.visitProgramMember(programClass, programMember);

    // Assert
    Attribute[] attributeArray = programMember.attributes;
    assertNull(attributeArray[0]);
    assertEquals(0, programMember.u2attributesCount);
    assertEquals(1, attributeArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Then first element {@link ConstantValueAttribute}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); then first element ConstantValueAttribute")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_thenFirstElementConstantValueAttribute() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ProgramClass programClass = new ProgramClass();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();
    Attribute[] attributes = new Attribute[] {constantValueAttribute};
    ProgramField programMember = new ProgramField(1, 1, 1, 1, attributes, new LibraryClass());

    // Act
    classShrinker.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Attribute[] attributeArray = programMember.attributes;
    Attribute attribute = attributeArray[0];
    assertTrue(attribute instanceof ConstantValueAttribute);
    assertEquals(1, attributeArray.length);
    assertEquals(1, programMember.u2attributesCount);
    assertSame(constantValueAttribute, attribute);
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Then first element {@link DeprecatedAttribute}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); then first element DeprecatedAttribute")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_thenFirstElementDeprecatedAttribute() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ProgramClass programClass = new ProgramClass();
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();
    Attribute[] attributes = new Attribute[] {deprecatedAttribute};
    ProgramField programMember = new ProgramField(1, 1, 1, 1, attributes, new LibraryClass());

    // Act
    classShrinker.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Attribute[] attributeArray = programMember.attributes;
    Attribute attribute = attributeArray[0];
    assertTrue(attribute instanceof DeprecatedAttribute);
    assertEquals(1, attributeArray.length);
    assertEquals(1, programMember.u2attributesCount);
    assertSame(deprecatedAttribute, attribute);
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName("Test visitProgramMember(ProgramClass, ProgramMember); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    ProgramClass programClass = new ProgramClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    ProgramField programMember = new ProgramField(1, 1, 1, 1, attributes, new LibraryClass());

    // Act
    classShrinker.visitProgramMember(programClass, programMember);

    // Assert
    Attribute[] attributeArray = programMember.attributes;
    assertNull(attributeArray[0]);
    assertEquals(0, programMember.u2attributesCount);
    assertEquals(1, attributeArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} {@link ProgramMember#u2attributesCount} is
   *       zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); then ProgramMethod() u2attributesCount is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_thenProgramMethodU2attributesCountIsZero() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    ProgramClass programClass = new ProgramClass();
    ProgramMethod programMember = new ProgramMethod();

    // Act
    classShrinker.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    assertEquals(0, programMember.u2attributesCount);
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    ProgramClass programClass = new ProgramClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    ProgramField programMember = new ProgramField(1, 1, 1, 3, attributes, new LibraryClass());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classShrinker.visitProgramMember(programClass, programMember));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>When {@link ProgramField#ProgramField()}.
   *   <li>Then {@link ProgramField#ProgramField()} {@link ProgramMember#u2attributesCount} is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); when ProgramField(); then ProgramField() u2attributesCount is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_whenProgramField_thenProgramFieldU2attributesCountIsZero() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    ProgramClass programClass = new ProgramClass();
    ProgramField programMember = new ProgramField();

    // Act
    classShrinker.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    assertEquals(0, programMember.u2attributesCount);
  }

  /**
   * Test {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz,
   * BootstrapMethodsAttribute)}
   */
  @Test
  @DisplayName("Test visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)"
  })
  void testVisitBootstrapMethodsAttribute() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    BootstrapMethodInfo[] bootstrapMethods = new BootstrapMethodInfo[] {bootstrapMethodInfo};
    BootstrapMethodsAttribute bootstrapMethodsAttribute =
        new BootstrapMethodsAttribute(1, 0, bootstrapMethods);

    // Act
    classShrinker.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

    // Assert that nothing has changed
    assertEquals(0, bootstrapMethodsAttribute.u2bootstrapMethodsCount);
    BootstrapMethodInfo[] bootstrapMethodInfoArray = bootstrapMethodsAttribute.bootstrapMethods;
    assertEquals(1, bootstrapMethodInfoArray.length);
    assertSame(bootstrapMethodInfo, bootstrapMethodInfoArray[0]);
  }

  /**
   * Test {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz,
   * BootstrapMethodsAttribute)}
   */
  @Test
  @DisplayName("Test visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)"
  })
  void testVisitBootstrapMethodsAttribute2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    BootstrapMethodInfo[] bootstrapMethods = new BootstrapMethodInfo[] {new BootstrapMethodInfo()};
    BootstrapMethodsAttribute bootstrapMethodsAttribute =
        new BootstrapMethodsAttribute(1, 1, bootstrapMethods);

    // Act
    classShrinker.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

    // Assert
    BootstrapMethodInfo[] bootstrapMethodInfoArray = bootstrapMethodsAttribute.bootstrapMethods;
    assertNull(bootstrapMethodInfoArray[0]);
    assertEquals(0, bootstrapMethodsAttribute.u2bootstrapMethodsCount);
    assertEquals(1, bootstrapMethodInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz,
   * BootstrapMethodsAttribute)}
   */
  @Test
  @DisplayName("Test visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)"
  })
  void testVisitBootstrapMethodsAttribute3() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    BootstrapMethodInfo[] bootstrapMethods = new BootstrapMethodInfo[] {bootstrapMethodInfo};
    BootstrapMethodsAttribute bootstrapMethodsAttribute =
        new BootstrapMethodsAttribute(1, 1, bootstrapMethods);

    // Act
    classShrinker.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    BootstrapMethodInfo[] bootstrapMethodInfoArray = bootstrapMethodsAttribute.bootstrapMethods;
    assertEquals(1, bootstrapMethodInfoArray.length);
    assertEquals(1, bootstrapMethodsAttribute.u2bootstrapMethodsCount);
    assertSame(bootstrapMethodInfo, bootstrapMethodInfoArray[0]);
  }

  /**
   * Test {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz,
   * BootstrapMethodsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)"
  })
  void testVisitBootstrapMethodsAttribute_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    BootstrapMethodInfo[] bootstrapMethods = new BootstrapMethodInfo[] {new BootstrapMethodInfo()};
    BootstrapMethodsAttribute bootstrapMethodsAttribute =
        new BootstrapMethodsAttribute(1, 1, bootstrapMethods);

    // Act
    classShrinker.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

    // Assert
    BootstrapMethodInfo[] bootstrapMethodInfoArray = bootstrapMethodsAttribute.bootstrapMethods;
    assertNull(bootstrapMethodInfoArray[0]);
    assertEquals(0, bootstrapMethodsAttribute.u2bootstrapMethodsCount);
    assertEquals(1, bootstrapMethodInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitBootstrapMethodsAttribute(Clazz,
   * BootstrapMethodsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)"
  })
  void testVisitBootstrapMethodsAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    BootstrapMethodInfo[] bootstrapMethods = new BootstrapMethodInfo[] {new BootstrapMethodInfo()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitBootstrapMethodsAttribute(
                clazz, new BootstrapMethodsAttribute(1, 1, bootstrapMethods)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName("Test visitRecordAttribute(Clazz, RecordAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    RecordComponentInfo recordComponentInfo = new RecordComponentInfo();
    RecordComponentInfo[] components = new RecordComponentInfo[] {recordComponentInfo};
    RecordAttribute recordAttribute = new RecordAttribute(1, 1, components);

    // Act
    classShrinker.visitRecordAttribute(clazz, recordAttribute);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    RecordComponentInfo[] recordComponentInfoArray = recordAttribute.components;
    assertEquals(1, recordComponentInfoArray.length);
    assertEquals(1, recordAttribute.u2componentsCount);
    assertSame(recordComponentInfo, recordComponentInfoArray[0]);
  }

  /**
   * Test {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Given {@link ClassShrinker#ClassShrinker(SimpleUsageMarker)} with usageMarker is {@link
   *       ShortestUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); given ClassShrinker(SimpleUsageMarker) with usageMarker is ShortestUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_givenClassShrinkerWithUsageMarkerIsShortestUsageMarker() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    RecordComponentInfo[] components = new RecordComponentInfo[] {new RecordComponentInfo()};
    RecordAttribute recordAttribute = new RecordAttribute(1, 1, components);

    // Act
    classShrinker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    RecordComponentInfo[] recordComponentInfoArray = recordAttribute.components;
    assertNull(recordComponentInfoArray[0]);
    assertEquals(0, recordAttribute.u2componentsCount);
    assertEquals(1, recordComponentInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName("Test visitRecordAttribute(Clazz, RecordAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    RecordComponentInfo[] components = new RecordComponentInfo[] {new RecordComponentInfo()};
    RecordAttribute recordAttribute = new RecordAttribute(1, 1, components);

    // Act
    classShrinker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    RecordComponentInfo[] recordComponentInfoArray = recordAttribute.components;
    assertNull(recordComponentInfoArray[0]);
    assertEquals(0, recordAttribute.u2componentsCount);
    assertEquals(1, recordComponentInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then {@link RecordAttribute#RecordAttribute()} {@link RecordAttribute#u2componentsCount}
   *       is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then RecordAttribute() u2componentsCount is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenRecordAttributeU2componentsCountIsZero() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    classShrinker.visitRecordAttribute(clazz, recordAttribute);

    // Assert that nothing has changed
    assertEquals(0, recordAttribute.u2componentsCount);
  }

  /**
   * Test {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    RecordComponentInfo[] components = new RecordComponentInfo[] {new RecordComponentInfo()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classShrinker.visitRecordAttribute(clazz, new RecordAttribute(1, 1, components)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName("Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"})
  void testVisitInnerClassesAttribute() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo[] classes = new InnerClassesInfo[] {new InnerClassesInfo()};
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(1, 1, classes);

    // Act
    classShrinker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert
    InnerClassesInfo[] innerClassesInfoArray = innerClassesAttribute.classes;
    assertNull(innerClassesInfoArray[0]);
    assertEquals(0, innerClassesAttribute.u2classesCount);
    assertEquals(1, innerClassesInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName("Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"})
  void testVisitInnerClassesAttribute2() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo();
    InnerClassesInfo[] classes = new InnerClassesInfo[] {innerClassesInfo};
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(1, 1, classes);

    // Act
    classShrinker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    InnerClassesInfo[] innerClassesInfoArray = innerClassesAttribute.classes;
    assertEquals(1, innerClassesInfoArray.length);
    assertEquals(1, innerClassesAttribute.u2classesCount);
    assertSame(innerClassesInfo, innerClassesInfoArray[0]);
  }

  /**
   * Test {@link ClassShrinker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"})
  void testVisitInnerClassesAttribute_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo[] classes = new InnerClassesInfo[] {new InnerClassesInfo()};
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(1, 1, classes);

    // Act
    classShrinker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert
    InnerClassesInfo[] innerClassesInfoArray = innerClassesAttribute.classes;
    assertNull(innerClassesInfoArray[0]);
    assertEquals(0, innerClassesAttribute.u2classesCount);
    assertEquals(1, innerClassesInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <ul>
   *   <li>Then {@link InnerClassesAttribute#InnerClassesAttribute()} {@link
   *       InnerClassesAttribute#u2classesCount} is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute); then InnerClassesAttribute() u2classesCount is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"})
  void testVisitInnerClassesAttribute_thenInnerClassesAttributeU2classesCountIsZero() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute();

    // Act
    classShrinker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert that nothing has changed
    assertEquals(0, innerClassesAttribute.u2classesCount);
  }

  /**
   * Test {@link ClassShrinker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"})
  void testVisitInnerClassesAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo[] classes = new InnerClassesInfo[] {new InnerClassesInfo()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitInnerClassesAttribute(
                clazz, new InnerClassesAttribute(1, 1, classes)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName("Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    classShrinker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert that nothing has changed
    assertEquals(0, enclosingMethodAttribute.u2nameAndTypeIndex);
  }

  /**
   * Test {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName("Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute(1, 1, 1);
    enclosingMethodAttribute.referencedMethod = new LibraryMethod();

    // Act
    classShrinker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    assertNull(enclosingMethodAttribute.referencedMethod);
    assertEquals(0, enclosingMethodAttribute.u2nameAndTypeIndex);
  }

  /**
   * Test {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName("Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute3() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute(1, 1, 1);
    enclosingMethodAttribute.referencedMethod = new LibraryMethod();

    // Act
    classShrinker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    assertNull(enclosingMethodAttribute.referencedMethod);
    assertEquals(0, enclosingMethodAttribute.u2nameAndTypeIndex);
  }

  /**
   * Test {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName("Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute4() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute(1, 1, 1);
    enclosingMethodAttribute.referencedMethod = new LibraryMethod();

    // Act
    classShrinker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    assertTrue(enclosingMethodAttribute.referencedMethod instanceof LibraryMethod);
    assertEquals(1, enclosingMethodAttribute.u2nameAndTypeIndex);
  }

  /**
   * Test {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute(1, 1, 1);
    enclosingMethodAttribute.referencedMethod = new LibraryMethod();

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classShrinker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doThrow(new UnsupportedOperationException())
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classShrinker.visitCodeAttribute(clazz, method, codeAttribute));
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link ClassShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute} {@link CodeAttribute#attributesAccept(Clazz, Method,
   *       AttributeVisitor)} does nothing.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); when CodeAttribute attributesAccept(Clazz, Method, AttributeVisitor) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_whenCodeAttributeAttributesAcceptDoesNothing() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act
    classShrinker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link ClassShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); when CodeAttribute(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_whenCodeAttribute_thenDoesNotThrow() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertDoesNotThrow(() -> classShrinker.visitCodeAttribute(clazz, method, new CodeAttribute()));
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTableAttribute localVariableTableAttribute = new LocalVariableTableAttribute();

    // Act
    classShrinker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    assertEquals(0, localVariableTableAttribute.u2localVariableTableLength);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    classShrinker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    LocalVariableInfo[] localVariableInfoArray = localVariableTableAttribute.localVariableTable;
    assertNull(localVariableInfoArray[0]);
    assertEquals(0, localVariableTableAttribute.u2localVariableTableLength);
    assertEquals(1, localVariableInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute3() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {localVariableInfo};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    classShrinker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    LocalVariableInfo[] localVariableInfoArray = localVariableTableAttribute.localVariableTable;
    assertEquals(1, localVariableInfoArray.length);
    assertEquals(1, localVariableTableAttribute.u2localVariableTableLength);
    assertSame(localVariableInfo, localVariableInfoArray[0]);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    classShrinker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    LocalVariableInfo[] localVariableInfoArray = localVariableTableAttribute.localVariableTable;
    assertNull(localVariableInfoArray[0]);
    assertEquals(0, localVariableTableAttribute.u2localVariableTableLength);
    assertEquals(1, localVariableInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitLocalVariableTableAttribute(
                clazz,
                method,
                codeAttribute,
                new LocalVariableTableAttribute(1, 1, localVariableTable)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute();

    // Act
    classShrinker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert that nothing has changed
    assertEquals(0, localVariableTypeTableAttribute.u2localVariableTypeTableLength);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {new LocalVariableTypeInfo()};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    classShrinker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    LocalVariableTypeInfo[] localVariableTypeInfoArray =
        localVariableTypeTableAttribute.localVariableTypeTable;
    assertNull(localVariableTypeInfoArray[0]);
    assertEquals(0, localVariableTypeTableAttribute.u2localVariableTypeTableLength);
    assertEquals(1, localVariableTypeInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute3() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo localVariableTypeInfo = new LocalVariableTypeInfo();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {localVariableTypeInfo};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    classShrinker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    LocalVariableTypeInfo[] localVariableTypeInfoArray =
        localVariableTypeTableAttribute.localVariableTypeTable;
    assertEquals(1, localVariableTypeInfoArray.length);
    assertEquals(1, localVariableTypeTableAttribute.u2localVariableTypeTableLength);
    assertSame(localVariableTypeInfo, localVariableTypeInfoArray[0]);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {new LocalVariableTypeInfo()};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    classShrinker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    LocalVariableTypeInfo[] localVariableTypeInfoArray =
        localVariableTypeTableAttribute.localVariableTypeTable;
    assertNull(localVariableTypeInfoArray[0]);
    assertEquals(0, localVariableTypeTableAttribute.u2localVariableTypeTableLength);
    assertEquals(1, localVariableTypeInfoArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {new LocalVariableTypeInfo()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitLocalVariableTypeTableAttribute(
                clazz,
                method,
                codeAttribute,
                new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName("Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    RuntimeInvisibleAnnotationsAttribute annotationsAttribute =
        new RuntimeInvisibleAnnotationsAttribute();

    // Act
    classShrinker.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

    // Assert that nothing has changed
    assertEquals(0, annotationsAttribute.u2annotationsCount);
  }

  /**
   * Test {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName("Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation[] annotations = new Annotation[] {new Annotation()};
    RuntimeInvisibleAnnotationsAttribute annotationsAttribute =
        new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations);

    // Act
    classShrinker.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

    // Assert
    Annotation[] annotationArray = annotationsAttribute.annotations;
    assertNull(annotationArray[0]);
    assertEquals(0, annotationsAttribute.u2annotationsCount);
    assertEquals(1, annotationArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName("Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute3() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    Annotation[] annotations = new Annotation[] {annotation};
    RuntimeInvisibleAnnotationsAttribute annotationsAttribute =
        new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations);

    // Act
    classShrinker.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Annotation[] annotationArray = annotationsAttribute.annotations;
    assertEquals(1, annotationArray.length);
    assertEquals(1, annotationsAttribute.u2annotationsCount);
    assertSame(annotation, annotationArray[0]);
  }

  /**
   * Test {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation[] annotations = new Annotation[] {new Annotation()};
    RuntimeInvisibleAnnotationsAttribute annotationsAttribute =
        new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations);

    // Act
    classShrinker.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

    // Assert
    Annotation[] annotationArray = annotationsAttribute.annotations;
    assertNull(annotationArray[0]);
    assertEquals(0, annotationsAttribute.u2annotationsCount);
    assertEquals(1, annotationArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    Annotation[] annotations = new Annotation[] {new Annotation()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitAnyAnnotationsAttribute(
                clazz, new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    RuntimeInvisibleParameterAnnotationsAttribute parameterAnnotationsAttribute =
        new RuntimeInvisibleParameterAnnotationsAttribute();

    // Act
    classShrinker.visitAnyParameterAnnotationsAttribute(
        clazz, method, parameterAnnotationsAttribute);

    // Assert that nothing has changed
    assertNull(parameterAnnotationsAttribute.u2parameterAnnotationsCount);
  }

  /**
   * Test {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    Annotation annotation = new Annotation();
    Annotation annotation2 = new Annotation();
    Annotation[][] parameterAnnotations =
        new Annotation[][] {new Annotation[] {annotation, annotation2, new Annotation()}};
    RuntimeInvisibleParameterAnnotationsAttribute parameterAnnotationsAttribute =
        new RuntimeInvisibleParameterAnnotationsAttribute(
            1, 1, new int[] {3, 1, 3, 1}, parameterAnnotations);

    // Act
    classShrinker.visitAnyParameterAnnotationsAttribute(
        clazz, method, parameterAnnotationsAttribute);

    // Assert
    Annotation[][] annotationArray = parameterAnnotationsAttribute.parameterAnnotations;
    Annotation[] annotationArray2 = annotationArray[0];
    assertNull(annotationArray2[0]);
    assertNull(annotationArray2[1]);
    assertNull(annotationArray2[2]);
    assertEquals(1, annotationArray.length);
    assertEquals(3, annotationArray2.length);
    assertArrayEquals(
        new int[] {0, 1, 3, 1}, parameterAnnotationsAttribute.u2parameterAnnotationsCount);
  }

  /**
   * Test {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link SimpleUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenCallsIsUsed() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    Annotation annotation = new Annotation();
    Annotation annotation2 = new Annotation();
    Annotation[][] parameterAnnotations =
        new Annotation[][] {new Annotation[] {annotation, annotation2, new Annotation()}};

    // Act
    classShrinker.visitAnyParameterAnnotationsAttribute(
        clazz,
        method,
        new RuntimeInvisibleParameterAnnotationsAttribute(
            1, 1, new int[] {3, 1, 3, 1}, parameterAnnotations));

    // Assert
    verify(simpleUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
  }

  /**
   * Test {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@link Annotation#Annotation()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then first element is Annotation()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenFirstElementIsAnnotation() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    Annotation annotation = new Annotation();
    Annotation[][] parameterAnnotations = new Annotation[][] {new Annotation[] {annotation}};
    RuntimeInvisibleParameterAnnotationsAttribute parameterAnnotationsAttribute =
        new RuntimeInvisibleParameterAnnotationsAttribute(
            1, 1, new int[] {0, 1, 3, 1}, parameterAnnotations);

    // Act
    classShrinker.visitAnyParameterAnnotationsAttribute(
        clazz, method, parameterAnnotationsAttribute);

    // Assert that nothing has changed
    Annotation[][] annotationArray = parameterAnnotationsAttribute.parameterAnnotations;
    Annotation[] annotationArray2 = annotationArray[0];
    assertEquals(1, annotationArray2.length);
    assertEquals(1, annotationArray.length);
    assertSame(annotation, annotationArray2[0]);
    assertArrayEquals(
        new int[] {0, 1, 3, 1}, parameterAnnotationsAttribute.u2parameterAnnotationsCount);
  }

  /**
   * Test {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    Annotation annotation = new Annotation();
    Annotation annotation2 = new Annotation();
    Annotation[][] parameterAnnotations =
        new Annotation[][] {new Annotation[] {annotation, annotation2, new Annotation()}};
    RuntimeInvisibleParameterAnnotationsAttribute parameterAnnotationsAttribute =
        new RuntimeInvisibleParameterAnnotationsAttribute(
            1, 1, new int[] {3, 1, 3, 1}, parameterAnnotations);

    // Act
    classShrinker.visitAnyParameterAnnotationsAttribute(
        clazz, method, parameterAnnotationsAttribute);

    // Assert
    Annotation[][] annotationArray = parameterAnnotationsAttribute.parameterAnnotations;
    Annotation[] annotationArray2 = annotationArray[0];
    assertNull(annotationArray2[0]);
    assertNull(annotationArray2[1]);
    assertNull(annotationArray2[2]);
    assertEquals(1, annotationArray.length);
    assertEquals(3, annotationArray2.length);
    assertArrayEquals(
        new int[] {0, 1, 3, 1}, parameterAnnotationsAttribute.u2parameterAnnotationsCount);
  }

  /**
   * Test {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    Annotation[][] parameterAnnotations = new Annotation[][] {new Annotation[] {new Annotation()}};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitAnyParameterAnnotationsAttribute(
                clazz,
                method,
                new RuntimeInvisibleParameterAnnotationsAttribute(
                    1, 1, new int[] {3, 1, 3, 1}, parameterAnnotations)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName("Test visitRecordComponentInfo(Clazz, RecordComponentInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordComponentInfo(Clazz, RecordComponentInfo)"})
  void testVisitRecordComponentInfo() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    RecordComponentInfo recordComponentInfo = new RecordComponentInfo(1, 1, 1, attributes);

    // Act
    classShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

    // Assert
    Attribute[] attributeArray = recordComponentInfo.attributes;
    assertNull(attributeArray[0]);
    assertEquals(0, recordComponentInfo.u2attributesCount);
    assertEquals(1, attributeArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRecordComponentInfo(Clazz, RecordComponentInfo); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordComponentInfo(Clazz, RecordComponentInfo)"})
  void testVisitRecordComponentInfo_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    RecordComponentInfo recordComponentInfo = new RecordComponentInfo(1, 1, 1, attributes);

    // Act
    classShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

    // Assert
    Attribute[] attributeArray = recordComponentInfo.attributes;
    assertNull(attributeArray[0]);
    assertEquals(0, recordComponentInfo.u2attributesCount);
    assertEquals(1, attributeArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <ul>
   *   <li>Then {@link RecordComponentInfo#RecordComponentInfo()} {@link
   *       RecordComponentInfo#u2attributesCount} is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRecordComponentInfo(Clazz, RecordComponentInfo); then RecordComponentInfo() u2attributesCount is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordComponentInfo(Clazz, RecordComponentInfo)"})
  void testVisitRecordComponentInfo_thenRecordComponentInfoU2attributesCountIsZero() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    RecordComponentInfo recordComponentInfo = new RecordComponentInfo();

    // Act
    classShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

    // Assert that nothing has changed
    assertEquals(0, recordComponentInfo.u2attributesCount);
  }

  /**
   * Test {@link ClassShrinker#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRecordComponentInfo(Clazz, RecordComponentInfo); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitRecordComponentInfo(Clazz, RecordComponentInfo)"})
  void testVisitRecordComponentInfo_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitRecordComponentInfo(
                clazz, new RecordComponentInfo(1, 1, 1, attributes)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};
    Annotation annotation = new Annotation(1, 1, elementValues);

    // Act
    classShrinker.visitAnnotation(clazz, annotation);

    // Assert
    ElementValue[] elementValueArray = annotation.elementValues;
    assertNull(elementValueArray[0]);
    assertEquals(0, annotation.u2elementValuesCount);
    assertEquals(1, elementValueArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation2() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    AnnotationElementValue annotationElementValue = new AnnotationElementValue(1, new Annotation());
    ElementValue[] elementValues = new ElementValue[] {annotationElementValue};

    // Act
    classShrinker.visitAnnotation(clazz, new Annotation(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation3() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    ElementValue[] elementValues = new ElementValue[] {new ArrayElementValue()};

    // Act
    classShrinker.visitAnnotation(clazz, new Annotation(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation4() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    ElementValue[] elementValues = new ElementValue[] {new ClassElementValue()};

    // Act
    classShrinker.visitAnnotation(clazz, new Annotation(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation5() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    ElementValue[] elementValues = new ElementValue[] {new EnumConstantElementValue()};

    // Act
    classShrinker.visitAnnotation(clazz, new Annotation(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <ul>
   *   <li>Then {@link Annotation#Annotation()} {@link Annotation#u2elementValuesCount} is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; then Annotation() u2elementValuesCount is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_thenAnnotationU2elementValuesCountIsZero() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    // Act
    classShrinker.visitAnnotation(clazz, annotation);

    // Assert that nothing has changed
    assertEquals(0, annotation.u2elementValuesCount);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};
    Annotation annotation = new Annotation(1, 1, elementValues);

    // Act
    classShrinker.visitAnnotation(clazz, annotation);

    // Assert
    ElementValue[] elementValueArray = annotation.elementValues;
    assertNull(elementValueArray[0]);
    assertEquals(0, annotation.u2elementValuesCount);
    assertEquals(1, elementValueArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassShrinker.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classShrinker.visitAnnotation(clazz, new Annotation(1, 1, elementValues)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName("Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    AnnotationElementValue annotationElementValue = new AnnotationElementValue(1, new Annotation());

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert that nothing has changed
    assertEquals(0, annotationElementValue.annotationValue.u2elementValuesCount);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName("Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue2() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};
    AnnotationElementValue annotationElementValue =
        new AnnotationElementValue(1, new Annotation(1, 1, elementValues));

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert
    Annotation annotation2 = annotationElementValue.annotationValue;
    ElementValue[] elementValueArray = annotation2.elementValues;
    assertNull(elementValueArray[0]);
    assertEquals(0, annotation2.u2elementValuesCount);
    assertEquals(1, elementValueArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then first element {@link AnnotationElementValue}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then first element AnnotationElementValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenFirstElementAnnotationElementValue() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    AnnotationElementValue annotationElementValue = new AnnotationElementValue(1, new Annotation());
    ElementValue[] elementValues = new ElementValue[] {annotationElementValue};
    AnnotationElementValue annotationElementValue2 =
        new AnnotationElementValue(1, new Annotation(1, 1, elementValues));

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue2);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Annotation annotation2 = annotationElementValue2.annotationValue;
    ElementValue[] elementValueArray = annotation2.elementValues;
    ElementValue elementValue = elementValueArray[0];
    assertTrue(elementValue instanceof AnnotationElementValue);
    assertEquals(1, elementValueArray.length);
    assertEquals(1, annotation2.u2elementValuesCount);
    assertSame(annotationElementValue, elementValue);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then first element {@link ArrayElementValue}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then first element ArrayElementValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenFirstElementArrayElementValue() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ArrayElementValue arrayElementValue = new ArrayElementValue();
    ElementValue[] elementValues = new ElementValue[] {arrayElementValue};
    AnnotationElementValue annotationElementValue =
        new AnnotationElementValue(1, new Annotation(1, 1, elementValues));

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Annotation annotation2 = annotationElementValue.annotationValue;
    ElementValue[] elementValueArray = annotation2.elementValues;
    ElementValue elementValue = elementValueArray[0];
    assertTrue(elementValue instanceof ArrayElementValue);
    assertEquals(1, elementValueArray.length);
    assertEquals(1, annotation2.u2elementValuesCount);
    assertSame(arrayElementValue, elementValue);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassElementValue}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then first element ClassElementValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenFirstElementClassElementValue() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ClassElementValue classElementValue = new ClassElementValue();
    ElementValue[] elementValues = new ElementValue[] {classElementValue};
    AnnotationElementValue annotationElementValue =
        new AnnotationElementValue(1, new Annotation(1, 1, elementValues));

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Annotation annotation2 = annotationElementValue.annotationValue;
    ElementValue[] elementValueArray = annotation2.elementValues;
    ElementValue elementValue = elementValueArray[0];
    assertTrue(elementValue instanceof ClassElementValue);
    assertEquals(1, elementValueArray.length);
    assertEquals(1, annotation2.u2elementValuesCount);
    assertSame(classElementValue, elementValue);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then first element {@link ConstantElementValue}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then first element ConstantElementValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenFirstElementConstantElementValue() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    ConstantElementValue constantElementValue = new ConstantElementValue('\u0003');
    constantElementValue.addProcessingFlags(2, 3, 2, 3);
    ElementValue[] elementValues = new ElementValue[] {constantElementValue};
    AnnotationElementValue annotationElementValue =
        new AnnotationElementValue(1, new Annotation(1, 1, elementValues));

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Annotation annotation2 = annotationElementValue.annotationValue;
    ElementValue[] elementValueArray = annotation2.elementValues;
    ElementValue elementValue = elementValueArray[0];
    assertTrue(elementValue instanceof ConstantElementValue);
    assertEquals(1, elementValueArray.length);
    assertEquals(1, annotation2.u2elementValuesCount);
    assertSame(constantElementValue, elementValue);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then first element {@link EnumConstantElementValue}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then first element EnumConstantElementValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenFirstElementEnumConstantElementValue() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    EnumConstantElementValue enumConstantElementValue = new EnumConstantElementValue();
    ElementValue[] elementValues = new ElementValue[] {enumConstantElementValue};
    AnnotationElementValue annotationElementValue =
        new AnnotationElementValue(1, new Annotation(1, 1, elementValues));

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert that nothing has changed
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
    Annotation annotation2 = annotationElementValue.annotationValue;
    ElementValue[] elementValueArray = annotation2.elementValues;
    ElementValue elementValue = elementValueArray[0];
    assertTrue(elementValue instanceof EnumConstantElementValue);
    assertEquals(1, elementValueArray.length);
    assertEquals(1, annotation2.u2elementValuesCount);
    assertSame(enumConstantElementValue, elementValue);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};
    AnnotationElementValue annotationElementValue =
        new AnnotationElementValue(1, new Annotation(1, 1, elementValues));

    // Act
    classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert
    Annotation annotation2 = annotationElementValue.annotationValue;
    ElementValue[] elementValueArray = annotation2.elementValues;
    assertNull(elementValueArray[0]);
    assertEquals(0, annotation2.u2elementValuesCount);
    assertEquals(1, elementValueArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};
    AnnotationElementValue annotationElementValue =
        new AnnotationElementValue(1, new Annotation(1, 3, elementValues));

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classShrinker.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName("Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};
    ArrayElementValue arrayElementValue = new ArrayElementValue(1, 1, elementValues);

    // Act
    classShrinker.visitArrayElementValue(clazz, annotation, arrayElementValue);

    // Assert
    ElementValue[] elementValueArray = arrayElementValue.elementValues;
    assertNull(elementValueArray[0]);
    assertEquals(0, arrayElementValue.u2elementValuesCount);
    assertEquals(1, elementValueArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName("Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue2() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    AnnotationElementValue annotationElementValue = new AnnotationElementValue(1, new Annotation());
    ElementValue[] elementValues = new ElementValue[] {annotationElementValue};

    // Act
    classShrinker.visitArrayElementValue(
        clazz, annotation, new ArrayElementValue(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName("Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue3() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new ArrayElementValue()};

    // Act
    classShrinker.visitArrayElementValue(
        clazz, annotation, new ArrayElementValue(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName("Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue4() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new ClassElementValue()};

    // Act
    classShrinker.visitArrayElementValue(
        clazz, annotation, new ArrayElementValue(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName("Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue5() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new EnumConstantElementValue()};

    // Act
    classShrinker.visitArrayElementValue(
        clazz, annotation, new ArrayElementValue(1, 1, elementValues));

    // Assert
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <ul>
   *   <li>Then {@link ArrayElementValue#ArrayElementValue()} {@link
   *       ArrayElementValue#u2elementValuesCount} is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue); then ArrayElementValue() u2elementValuesCount is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue_thenArrayElementValueU2elementValuesCountIsZero() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ArrayElementValue arrayElementValue = new ArrayElementValue();

    // Act
    classShrinker.visitArrayElementValue(clazz, annotation, arrayElementValue);

    // Assert that nothing has changed
    assertEquals(0, arrayElementValue.u2elementValuesCount);
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue_thenFirstElementIsNull() {
    // Arrange
    ClassShrinker classShrinker = new ClassShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};
    ArrayElementValue arrayElementValue = new ArrayElementValue(1, 1, elementValues);

    // Act
    classShrinker.visitArrayElementValue(clazz, annotation, arrayElementValue);

    // Assert
    ElementValue[] elementValueArray = arrayElementValue.elementValues;
    assertNull(elementValueArray[0]);
    assertEquals(0, arrayElementValue.u2elementValuesCount);
    assertEquals(1, elementValueArray.length);
  }

  /**
   * Test {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassShrinker#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassShrinker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue_thenThrowUnsupportedOperationException() {
    // Arrange
    when(simpleUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    ElementValue[] elementValues = new ElementValue[] {new AnnotationElementValue()};

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            classShrinker.visitArrayElementValue(
                clazz, annotation, new ArrayElementValue(1, 1, elementValues)));
    verify(simpleUsageMarker).isUsed(isA(Processable.class));
  }
}
