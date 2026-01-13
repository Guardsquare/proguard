package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
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
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MethodImplementationTraveler;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.MemberNameCleaner;
import proguard.optimize.BootstrapMethodArgumentShrinker;
import proguard.optimize.KeepMarker;
import proguard.optimize.MethodStaticizer;
import proguard.optimize.evaluation.SimpleEnumDescriptorSimplifier;
import proguard.testutils.cpa.NamedMember;

class UnusedParameterMethodFilterDiffblueTest {
  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasUnusedParameters();
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod2() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new BootstrapMethodArgumentShrinker());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getUsedParameters()).thenReturn(1L);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getUsedParameters();
    verify(methodOptimizationInfo).hasUnusedParameters();
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod3() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new MethodStaticizer());
    ProgramClass programClass = mock(ProgramClass.class);

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isParameterUsed(anyInt())).thenReturn(true);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasUnusedParameters();
    verify(methodOptimizationInfo).isParameterUsed(0);
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod4() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new SimpleEnumDescriptorSimplifier());

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).getString(0);
    verify(methodOptimizationInfo).hasUnusedParameters();
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod5() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = mock(ProgramClass.class);

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);

    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasUnusedParameters();
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod6() {
    // Arrange
    MethodImplementationTraveler memberVisitor =
        new MethodImplementationTraveler(true, true, true, true, new KotlinAnnotationCounter());
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(memberVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());
    when(programClass.getString(anyInt())).thenReturn("String");

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).getString(1);
    verify(programClass)
        .hierarchyAccept(eq(false), eq(true), eq(true), eq(true), isA(ClassVisitor.class));
    verify(methodOptimizationInfo).hasUnusedParameters();
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod7() {
    // Arrange
    MethodImplementationTraveler memberVisitor =
        new MethodImplementationTraveler(true, true, true, true, new MemberNameCleaner());
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(memberVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());
    when(programClass.getString(anyInt())).thenReturn("String");

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).getString(1);
    verify(programClass)
        .hierarchyAccept(eq(false), eq(true), eq(true), eq(true), isA(ClassVisitor.class));
    verify(methodOptimizationInfo).hasUnusedParameters();
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod8() {
    // Arrange
    MethodImplementationTraveler memberVisitor =
        new MethodImplementationTraveler(
            true, true, true, true, new BootstrapMethodArgumentShrinker());
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(memberVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());
    when(programClass.getString(anyInt())).thenReturn("String");

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getUsedParameters()).thenReturn(1L);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).getString(1);
    verify(programClass)
        .hierarchyAccept(eq(false), eq(true), eq(true), eq(true), isA(ClassVisitor.class));
    verify(methodOptimizationInfo).getUsedParameters();
    verify(methodOptimizationInfo).hasUnusedParameters();
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod9() {
    // Arrange
    MethodImplementationTraveler memberVisitor =
        new MethodImplementationTraveler(true, true, true, true, new KeepMarker());
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(memberVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());
    when(programClass.getString(anyInt())).thenReturn("String");

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).getString(1);
    verify(programClass)
        .hierarchyAccept(eq(false), eq(true), eq(true), eq(true), isA(ClassVisitor.class));
    verify(methodOptimizationInfo).hasUnusedParameters();
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertNull(((MethodOptimizationInfo) processingInfo).getReturnValue());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getReturnedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getUsedParameters());
    assertEquals(0, ((MethodOptimizationInfo) processingInfo).getParameterSize());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalReturnValues());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasUnusedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSynchronizedBlock());
    assertTrue(((MethodOptimizationInfo) processingInfo).isKept());
    assertEquals(Integer.MAX_VALUE, ((MethodOptimizationInfo) processingInfo).getInvocationCount());
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod10() {
    // Arrange
    MethodImplementationTraveler memberVisitor =
        new MethodImplementationTraveler(true, true, true, true, new MethodStaticizer());
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(memberVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());
    when(programClass.getString(anyInt())).thenReturn("String");

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isParameterUsed(anyInt())).thenReturn(true);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).getString(1);
    verify(programClass)
        .hierarchyAccept(eq(false), eq(true), eq(true), eq(true), isA(ClassVisitor.class));
    verify(methodOptimizationInfo).hasUnusedParameters();
    verify(methodOptimizationInfo).isParameterUsed(0);
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#hasUnusedParameters()}
   *       return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given MethodOptimizationInfo hasUnusedParameters() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenMethodOptimizationInfoHasUnusedParametersReturnFalse() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(false);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasUnusedParameters();
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo is {@link
   *       MethodOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo is MethodOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoIsMethodOptimizationInfo() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    MethodOptimizationInfo methodOptimizationInfo = new MethodOptimizationInfo();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertSame(methodOptimizationInfo, programMethod.getProcessingInfo());
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoIsNull() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new MemberNameCleaner());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasUnusedParameters();
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link UnusedParameterMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnusedParameterMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    UnusedParameterMethodFilter unusedParameterMethodFilter =
        new UnusedParameterMethodFilter(new KeepMarker());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasUnusedParameters()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    unusedParameterMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasUnusedParameters();
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertNull(((MethodOptimizationInfo) processingInfo).getReturnValue());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getReturnedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getUsedParameters());
    assertEquals(0, ((MethodOptimizationInfo) processingInfo).getParameterSize());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalReturnValues());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasUnusedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSynchronizedBlock());
    assertTrue(((MethodOptimizationInfo) processingInfo).isKept());
    assertEquals(Integer.MAX_VALUE, ((MethodOptimizationInfo) processingInfo).getInvocationCount());
  }
}
