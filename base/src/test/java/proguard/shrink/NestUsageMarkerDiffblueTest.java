package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
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
import proguard.classfile.attribute.NestHostAttribute;
import proguard.classfile.attribute.NestMembersAttribute;
import proguard.classfile.attribute.PermittedSubclassesAttribute;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.util.Processable;

class NestUsageMarkerDiffblueTest {
  /**
   * Test {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}.
   *
   * <p>Method under test: {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}
   */
  @Test
  @DisplayName("Test visitNestHostAttribute(Clazz, NestHostAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitNestHostAttribute(Clazz, NestHostAttribute)"})
  void testVisitNestHostAttribute() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    NestUsageMarker nestUsageMarker =
        new NestUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    NestHostAttribute nestHostAttribute = new NestHostAttribute();

    // Act
    nestUsageMarker.visitNestHostAttribute(clazz, nestHostAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, nestHostAttribute.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
  }

  /**
   * Test {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}.
   *
   * <p>Method under test: {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}
   */
  @Test
  @DisplayName("Test visitNestHostAttribute(Clazz, NestHostAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitNestHostAttribute(Clazz, NestHostAttribute)"})
  void testVisitNestHostAttribute2() {
    // Arrange
    NestUsageMarker nestUsageMarker = new NestUsageMarker(new ClassUsageMarker());
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    nestUsageMarker.visitNestHostAttribute(clazz, new NestHostAttribute());

    // Assert that nothing has changed
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[0] instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}.
   *
   * <p>Method under test: {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}
   */
  @Test
  @DisplayName("Test visitNestHostAttribute(Clazz, NestHostAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitNestHostAttribute(Clazz, NestHostAttribute)"})
  void testVisitNestHostAttribute3() {
    // Arrange
    NestUsageMarker nestUsageMarker =
        new NestUsageMarker(new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause"));
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    nestUsageMarker.visitNestHostAttribute(clazz, new NestHostAttribute());

    // Assert that nothing has changed
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[0] instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}.
   *
   * <ul>
   *   <li>Then {@link NestHostAttribute#NestHostAttribute()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link NestUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitNestHostAttribute(Clazz, NestHostAttribute); then NestHostAttribute() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitNestHostAttribute(Clazz, NestHostAttribute)"})
  void testVisitNestHostAttribute_thenNestHostAttributeProcessingInfoIsNull() {
    // Arrange
    NestUsageMarker nestUsageMarker = new NestUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    NestHostAttribute nestHostAttribute = new NestHostAttribute();

    // Act
    nestUsageMarker.visitNestHostAttribute(clazz, nestHostAttribute);

    // Assert that nothing has changed
    assertNull(nestHostAttribute.getProcessingInfo());
  }

  /**
   * Test {@link NestUsageMarker#visitNestMembersAttribute(Clazz, NestMembersAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link NestMembersAttribute#memberClassConstantsAccept(Clazz,
   *       ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link NestUsageMarker#visitNestMembersAttribute(Clazz,
   * NestMembersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitNestMembersAttribute(Clazz, NestMembersAttribute); then calls memberClassConstantsAccept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitNestMembersAttribute(Clazz, NestMembersAttribute)"})
  void testVisitNestMembersAttribute_thenCallsMemberClassConstantsAccept() {
    // Arrange
    NestUsageMarker nestUsageMarker = new NestUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();

    NestMembersAttribute nestMembersAttribute = mock(NestMembersAttribute.class);
    doNothing()
        .when(nestMembersAttribute)
        .memberClassConstantsAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());

    // Act
    nestUsageMarker.visitNestMembersAttribute(clazz, nestMembersAttribute);

    // Assert
    verify(nestMembersAttribute)
        .memberClassConstantsAccept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link NestUsageMarker#visitPermittedSubclassesAttribute(Clazz,
   * PermittedSubclassesAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link PermittedSubclassesAttribute#permittedSubclassConstantsAccept(Clazz,
   *       ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link NestUsageMarker#visitPermittedSubclassesAttribute(Clazz,
   * PermittedSubclassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitPermittedSubclassesAttribute(Clazz, PermittedSubclassesAttribute); then calls permittedSubclassConstantsAccept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NestUsageMarker.visitPermittedSubclassesAttribute(Clazz, PermittedSubclassesAttribute)"
  })
  void testVisitPermittedSubclassesAttribute_thenCallsPermittedSubclassConstantsAccept() {
    // Arrange
    NestUsageMarker nestUsageMarker = new NestUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();

    PermittedSubclassesAttribute permittedSubclassesAttribute =
        mock(PermittedSubclassesAttribute.class);
    doNothing()
        .when(permittedSubclassesAttribute)
        .permittedSubclassConstantsAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());

    // Act
    nestUsageMarker.visitPermittedSubclassesAttribute(clazz, permittedSubclassesAttribute);

    // Assert
    verify(permittedSubclassesAttribute)
        .permittedSubclassConstantsAccept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link NestUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link NestUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    NestUsageMarker nestUsageMarker =
        new NestUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant();

    // Act
    nestUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    assertSame(usageMarker.currentUsageMark, classConstant.getProcessingInfo());
  }

  /**
   * Test {@link NestUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link NestUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant2() {
    // Arrange
    NestUsageMarker nestUsageMarker = new NestUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act
    nestUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link NestUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link NestUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant3() {
    // Arrange
    NestUsageMarker nestUsageMarker =
        new NestUsageMarker(new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act
    nestUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link NestUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}.
   *
   * <p>Method under test: {@link NestUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}
   */
  @Test
  @DisplayName("Test visitUtf8Constant(Clazz, Utf8Constant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitUtf8Constant(Clazz, Utf8Constant)"})
  void testVisitUtf8Constant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    NestUsageMarker nestUsageMarker =
        new NestUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    Utf8Constant utf8Constant = new Utf8Constant();

    // Act
    nestUsageMarker.visitUtf8Constant(clazz, utf8Constant);

    // Assert
    assertSame(usageMarker.currentUsageMark, utf8Constant.getProcessingInfo());
  }

  /**
   * Test {@link NestUsageMarker#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link NestUsageMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    NestUsageMarker nestUsageMarker = new NestUsageMarker(new ClassUsageMarker());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> nestUsageMarker.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link NestUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#isUsed(Processable)} return {@code
   *       true}.
   *   <li>Then calls {@link ClassUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link NestUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassUsageMarker isUsed(Processable) return 'true'; then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NestUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassUsageMarkerIsUsedReturnTrue_thenCallsIsUsed() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    NestUsageMarker nestUsageMarker = new NestUsageMarker(classUsageMarker);

    // Act
    nestUsageMarker.visitProgramClass(new ProgramClass());

    // Assert
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }
}
