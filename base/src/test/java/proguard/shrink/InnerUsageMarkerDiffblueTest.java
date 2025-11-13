package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Utf8Constant;
import proguard.util.Processable;

class InnerUsageMarkerDiffblueTest {
  /**
   * Test {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName("Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InnerUsageMarker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"
  })
  void testVisitInnerClassesAttribute() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    InnerUsageMarker innerUsageMarker =
        new InnerUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo[] classes = new InnerClassesInfo[] {new InnerClassesInfo()};
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(1, 1, classes);

    // Act
    innerUsageMarker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert
    InnerClassesInfo[] innerClassesInfoArray = innerClassesAttribute.classes;
    assertEquals(1, innerClassesInfoArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, innerClassesAttribute.getProcessingInfo());
    assertSame(shortestUsageMark, innerClassesInfoArray[0].getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName("Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InnerUsageMarker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"
  })
  void testVisitInnerClassesAttribute2() {
    // Arrange
    InnerUsageMarker innerUsageMarker = new InnerUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo(1, 1, 1, 5);
    InnerClassesInfo[] classes = new InnerClassesInfo[] {innerClassesInfo};
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(1, 1, classes);

    // Act
    innerUsageMarker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert
    assertEquals(1, innerClassesAttribute.classes.length);
  }

  /**
   * Test {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName("Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InnerUsageMarker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"
  })
  void testVisitInnerClassesAttribute3() {
    // Arrange
    InnerUsageMarker innerUsageMarker =
        new InnerUsageMarker(new ClassUsageMarker(new ShortestUsageMarker()));
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo[] classes = new InnerClassesInfo[] {new InnerClassesInfo()};
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(1, 1, classes);

    // Act
    innerUsageMarker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert that nothing has changed
    assertEquals(1, innerClassesAttribute.classes.length);
  }

  /**
   * Test {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <ul>
   *   <li>Then array length is one.
   * </ul>
   *
   * <p>Method under test: {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute); then array length is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InnerUsageMarker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"
  })
  void testVisitInnerClassesAttribute_thenArrayLengthIsOne() {
    // Arrange
    InnerUsageMarker innerUsageMarker = new InnerUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo[] classes = new InnerClassesInfo[] {new InnerClassesInfo()};
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(1, 1, classes);

    // Act
    innerUsageMarker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert
    assertEquals(1, innerClassesAttribute.classes.length);
  }

  /**
   * Test {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <ul>
   *   <li>Then {@link InnerClassesAttribute#InnerClassesAttribute()} ProcessingInfo is {@code
   *       null}.
   * </ul>
   *
   * <p>Method under test: {@link InnerUsageMarker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute); then InnerClassesAttribute() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InnerUsageMarker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"
  })
  void testVisitInnerClassesAttribute_thenInnerClassesAttributeProcessingInfoIsNull() {
    // Arrange
    InnerUsageMarker innerUsageMarker = new InnerUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute();

    // Act
    innerUsageMarker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert that nothing has changed
    assertNull(innerClassesAttribute.getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName("Test visitInnerClassesInfo(Clazz, InnerClassesInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    InnerUsageMarker innerUsageMarker =
        new InnerUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo();

    // Act
    innerUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert
    assertSame(usageMarker.currentUsageMark, innerClassesInfo.getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <ul>
   *   <li>Then {@link InnerClassesInfo#InnerClassesInfo()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link InnerUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesInfo(Clazz, InnerClassesInfo); then InnerClassesInfo() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo_thenInnerClassesInfoProcessingInfoIsNull() {
    // Arrange
    InnerUsageMarker innerUsageMarker =
        new InnerUsageMarker(new ClassUsageMarker(new ShortestUsageMarker()));
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo();

    // Act
    innerUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert that nothing has changed
    assertNull(innerClassesInfo.getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    InnerUsageMarker innerUsageMarker =
        new InnerUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant();

    // Act
    innerUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    assertSame(usageMarker.currentUsageMark, classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant2() {
    // Arrange
    InnerUsageMarker innerUsageMarker = new InnerUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act
    innerUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant3() {
    // Arrange
    InnerUsageMarker innerUsageMarker =
        new InnerUsageMarker(new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act
    innerUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}.
   *
   * <p>Method under test: {@link InnerUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}
   */
  @Test
  @DisplayName("Test visitUtf8Constant(Clazz, Utf8Constant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitUtf8Constant(Clazz, Utf8Constant)"})
  void testVisitUtf8Constant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    InnerUsageMarker innerUsageMarker =
        new InnerUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    Utf8Constant utf8Constant = new Utf8Constant();

    // Act
    innerUsageMarker.visitUtf8Constant(clazz, utf8Constant);

    // Assert
    assertSame(usageMarker.currentUsageMark, utf8Constant.getProcessingInfo());
  }

  /**
   * Test {@link InnerUsageMarker#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link InnerUsageMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    InnerUsageMarker innerUsageMarker = new InnerUsageMarker(new ClassUsageMarker());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> innerUsageMarker.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link InnerUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#isUsed(Processable)} return {@code
   *       true}.
   *   <li>Then calls {@link ClassUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link InnerUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassUsageMarker isUsed(Processable) return 'true'; then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InnerUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassUsageMarkerIsUsedReturnTrue_thenCallsIsUsed() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    InnerUsageMarker innerUsageMarker = new InnerUsageMarker(classUsageMarker);

    // Act
    innerUsageMarker.visitProgramClass(new ProgramClass());

    // Assert
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }
}
