package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

class ClassMergerDiffblueTest {
  /**
   * Test {@link ClassMerger#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassMerger#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassMerger.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    ProgramClass targetClass = mock(ProgramClass.class);
    when(targetClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    ClassMerger classMerger = new ClassMerger(targetClass, true, true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(null);

    // Act
    classMerger.visitProgramClass(programClass);

    // Assert
    verify(targetClass).getProcessingInfo();
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link ClassMerger#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassMerger.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassOptimizationInfo() {
    // Arrange
    ClassMerger classMerger = new ClassMerger(mock(ProgramClass.class), true, true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());

    // Act
    classMerger.visitProgramClass(programClass);

    // Assert
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link ClassMerger#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#isKept()} return {@code
   *       true}.
   *   <li>Then calls {@link ClassOptimizationInfo#isKept()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassOptimizationInfo isKept() return 'true'; then calls isKept()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassMerger.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassOptimizationInfoIsKeptReturnTrue_thenCallsIsKept() {
    // Arrange
    ClassMerger classMerger = new ClassMerger(mock(ProgramClass.class), true, true, true);

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isKept()).thenReturn(true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);

    // Act
    classMerger.visitProgramClass(programClass);

    // Assert
    verify(classOptimizationInfo).isKept();
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link ClassMerger#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassOptimizationInfo#isKept()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls isKept()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassMerger.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsIsKept() {
    // Arrange
    ProgramClass targetClass = mock(ProgramClass.class);
    when(targetClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    ClassMerger classMerger = new ClassMerger(targetClass, true, true, true);

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isKept()).thenReturn(false);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);

    // Act
    classMerger.visitProgramClass(programClass);

    // Assert
    verify(classOptimizationInfo).isKept();
    verify(targetClass).getProcessingInfo();
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link ClassMerger#isMergeable(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#isMergeable(ProgramClass)}
   */
  @Test
  @DisplayName("Test isMergeable(ProgramClass); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMerger.isMergeable(ProgramClass)"})
  void testIsMergeable_givenClassOptimizationInfo() {
    // Arrange
    ProgramClass targetClass = mock(ProgramClass.class);
    doNothing().when(targetClass).addSubClass(Mockito.<Clazz>any());
    targetClass.addSubClass(new LibraryClass());
    ClassMerger classMerger = new ClassMerger(targetClass, true, true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());

    // Act
    boolean actualIsMergeableResult = classMerger.isMergeable(programClass);

    // Assert
    verify(targetClass).addSubClass(isA(Clazz.class));
    verify(programClass).getProcessingInfo();
    assertFalse(actualIsMergeableResult);
  }

  /**
   * Test {@link ClassMerger#isMergeable(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#isKept()} return {@code
   *       true}.
   *   <li>Then calls {@link ProgramClass#addSubClass(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#isMergeable(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test isMergeable(ProgramClass); given ClassOptimizationInfo isKept() return 'true'; then calls addSubClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMerger.isMergeable(ProgramClass)"})
  void testIsMergeable_givenClassOptimizationInfoIsKeptReturnTrue_thenCallsAddSubClass() {
    // Arrange
    ProgramClass targetClass = mock(ProgramClass.class);
    doNothing().when(targetClass).addSubClass(Mockito.<Clazz>any());
    targetClass.addSubClass(new LibraryClass());
    ClassMerger classMerger = new ClassMerger(targetClass, true, true, true);

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isKept()).thenReturn(true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);

    // Act
    boolean actualIsMergeableResult = classMerger.isMergeable(programClass);

    // Assert
    verify(targetClass).addSubClass(isA(Clazz.class));
    verify(classOptimizationInfo).isKept();
    verify(programClass).getProcessingInfo();
    assertFalse(actualIsMergeableResult);
  }

  /**
   * Test {@link ClassMerger#isMergeable(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClass} {@link ProgramClass#getProcessingInfo()} return {@link
   *       ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#isMergeable(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test isMergeable(ProgramClass); given ProgramClass getProcessingInfo() return ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMerger.isMergeable(ProgramClass)"})
  void testIsMergeable_givenProgramClassGetProcessingInfoReturnClassOptimizationInfo() {
    // Arrange
    ProgramClass targetClass = mock(ProgramClass.class);
    when(targetClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    ClassMerger classMerger =
        new ClassMerger(targetClass, true, true, true, mock(ClassVisitor.class));

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isKept()).thenReturn(false);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);

    // Act
    boolean actualIsMergeableResult = classMerger.isMergeable(programClass);

    // Assert
    verify(classOptimizationInfo).isKept();
    verify(targetClass).getProcessingInfo();
    verify(programClass).getProcessingInfo();
    assertFalse(actualIsMergeableResult);
  }

  /**
   * Test {@link ClassMerger#hasSignatureAttribute(Clazz)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass#ProgramClass()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#hasSignatureAttribute(Clazz)}
   */
  @Test
  @DisplayName("Test hasSignatureAttribute(Clazz); when ProgramClass(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMerger.hasSignatureAttribute(Clazz)"})
  void testHasSignatureAttribute_whenProgramClass_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(ClassMerger.hasSignatureAttribute(new ProgramClass()));
  }

  /**
   * Test {@link ClassMerger#isNestHostOrMember(Clazz)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass#ProgramClass()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#isNestHostOrMember(Clazz)}
   */
  @Test
  @DisplayName("Test isNestHostOrMember(Clazz); when ProgramClass(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMerger.isNestHostOrMember(Clazz)"})
  void testIsNestHostOrMember_whenProgramClass_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(ClassMerger.isNestHostOrMember(new ProgramClass()));
  }

  /**
   * Test {@link ClassMerger#setTargetClass(Clazz, Clazz)}.
   *
   * <p>Method under test: {@link ClassMerger#setTargetClass(Clazz, Clazz)}
   */
  @Test
  @DisplayName("Test setTargetClass(Clazz, Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassMerger.setTargetClass(Clazz, Clazz)"})
  void testSetTargetClass() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());
    LibraryClass targetClass = new LibraryClass();

    // Act
    ClassMerger.setTargetClass(clazz, targetClass);

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertSame(targetClass, ((ProgramClassOptimizationInfo) processingInfo).getTargetClass());
  }

  /**
   * Test {@link ClassMerger#getTargetClass(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#getTargetClass(Clazz)}
   */
  @Test
  @DisplayName("Test getTargetClass(Clazz); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Clazz ClassMerger.getTargetClass(Clazz)"})
  void testGetTargetClass_givenClassOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertNull(ClassMerger.getTargetClass(clazz));
  }

  /**
   * Test {@link ClassMerger#getTargetClass(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassMerger#getTargetClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test getTargetClass(Clazz); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Clazz ClassMerger.getTargetClass(Clazz)"})
  void testGetTargetClass_givenProgramClassOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertNull(ClassMerger.getTargetClass(clazz));
  }
}
