package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

class HorizontalClassMergerDiffblueTest {
  /**
   * Test {@link HorizontalClassMerger#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Given {@link ArrayList#ArrayList()}.
   *   <li>Then calls {@link ClassPool#classes()}.
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName("Test visitClassPool(ClassPool); given ArrayList(); then calls classes()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HorizontalClassMerger.visitClassPool(ClassPool)"})
  void testVisitClassPool_givenArrayList_thenCallsClasses() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    ClassPool classPool = mock(ClassPool.class);
    when(classPool.classes()).thenReturn(new ArrayList<>());

    // Act
    horizontalClassMerger.visitClassPool(classPool);

    // Assert
    verify(classPool).classes();
  }

  /**
   * Test {@link HorizontalClassMerger#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#getTargetClass()} return
   *       {@link ProgramClass}.
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test visitClassPool(ClassPool); given ClassOptimizationInfo getTargetClass() return ProgramClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HorizontalClassMerger.visitClassPool(ClassPool)"})
  void testVisitClassPool_givenClassOptimizationInfoGetTargetClassReturnProgramClass() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isKept()).thenReturn(false);
    when(classOptimizationInfo.getTargetClass()).thenReturn(programClass);

    ProgramClass programClass2 = mock(ProgramClass.class);
    when(programClass2.getProcessingInfo()).thenReturn(classOptimizationInfo);
    doNothing().when(programClass2).addSubClass(Mockito.<Clazz>any());
    programClass2.addSubClass(new LibraryClass());

    ArrayList<Clazz> clazzList = new ArrayList<>();
    clazzList.add(programClass2);

    ClassPool classPool = mock(ClassPool.class);
    when(classPool.classes()).thenReturn(clazzList);

    // Act
    horizontalClassMerger.visitClassPool(classPool);

    // Assert
    verify(classPool).classes();
    verify(programClass2).addSubClass(isA(Clazz.class));
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(classOptimizationInfo).getTargetClass();
    verify(classOptimizationInfo).isKept();
    verify(programClass).getProcessingInfo();
    verify(programClass2, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link HorizontalClassMerger#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#isKept()} return {@code
   *       true}.
   *   <li>Then calls {@link ClassOptimizationInfo#isKept()}.
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test visitClassPool(ClassPool); given ClassOptimizationInfo isKept() return 'true'; then calls isKept()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HorizontalClassMerger.visitClassPool(ClassPool)"})
  void testVisitClassPool_givenClassOptimizationInfoIsKeptReturnTrue_thenCallsIsKept() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isKept()).thenReturn(true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    ArrayList<Clazz> clazzList = new ArrayList<>();
    clazzList.add(programClass);

    ClassPool classPool = mock(ClassPool.class);
    when(classPool.classes()).thenReturn(clazzList);

    // Act
    horizontalClassMerger.visitClassPool(classPool);

    // Assert
    verify(classPool).classes();
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(classOptimizationInfo).isKept();
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link HorizontalClassMerger#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClass} {@link ProgramClass#getProcessingInfo()} return {@link
   *       ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test visitClassPool(ClassPool); given ProgramClass getProcessingInfo() return ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HorizontalClassMerger.visitClassPool(ClassPool)"})
  void testVisitClassPool_givenProgramClassGetProcessingInfoReturnClassOptimizationInfo() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    ArrayList<Clazz> clazzList = new ArrayList<>();
    clazzList.add(programClass);

    ClassPool classPool = mock(ClassPool.class);
    when(classPool.classes()).thenReturn(clazzList);

    // Act
    horizontalClassMerger.visitClassPool(classPool);

    // Assert
    verify(classPool).classes();
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link HorizontalClassMerger#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName("Test visitClassPool(ClassPool); then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HorizontalClassMerger.visitClassPool(ClassPool)"})
  void testVisitClassPool_thenCallsAccept() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).accept(Mockito.<ClassVisitor>any());
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());
    when(programClass.getAccessFlags()).thenReturn(1);
    when(programClass.getProcessingFlags()).thenReturn(1);
    when(programClass.getName()).thenReturn("Name");
    when(programClass.getSuperClass()).thenReturn(new LibraryClass());
    doNothing().when(programClass).attributesAccept(Mockito.<AttributeVisitor>any());
    when(programClass.getProcessingInfo()).thenReturn(new ProgramClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    ArrayList<Clazz> clazzList = new ArrayList<>();
    clazzList.add(programClass);

    ClassPool classPool = mock(ClassPool.class);
    when(classPool.classes()).thenReturn(clazzList);

    // Act
    horizontalClassMerger.visitClassPool(classPool);

    // Assert
    verify(classPool).classes();
    verify(programClass).accept(isA(ClassVisitor.class));
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(programClass).attributesAccept(isA(AttributeVisitor.class));
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
    verify(programClass).getAccessFlags();
    verify(programClass).getName();
    verify(programClass, atLeast(1)).getSuperClass();
    verify(programClass).getProcessingFlags();
    verify(programClass, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link HorizontalClassMerger#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassOptimizationInfo#isDotClassed()}.
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName("Test visitClassPool(ClassPool); then calls isDotClassed()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void HorizontalClassMerger.visitClassPool(ClassPool)"})
  void testVisitClassPool_thenCallsIsDotClassed() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isDotClassed()).thenReturn(true);
    when(classOptimizationInfo.isKept()).thenReturn(false);
    when(classOptimizationInfo.getTargetClass()).thenReturn(null);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).attributesAccept(Mockito.<AttributeVisitor>any());
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    ArrayList<Clazz> clazzList = new ArrayList<>();
    clazzList.add(programClass);

    ClassPool classPool = mock(ClassPool.class);
    when(classPool.classes()).thenReturn(clazzList);

    // Act
    horizontalClassMerger.visitClassPool(classPool);

    // Assert
    verify(classPool).classes();
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(programClass).attributesAccept(isA(AttributeVisitor.class));
    verify(classOptimizationInfo).getTargetClass();
    verify(classOptimizationInfo).isDotClassed();
    verify(classOptimizationInfo).isKept();
    verify(programClass, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link HorizontalClassMerger#isCandidateForMerging(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#isCandidateForMerging(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isCandidateForMerging(Clazz); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean HorizontalClassMerger.isCandidateForMerging(Clazz)"})
  void testIsCandidateForMerging_givenClassOptimizationInfo() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertFalse(horizontalClassMerger.isCandidateForMerging(clazz));
  }

  /**
   * Test {@link HorizontalClassMerger#isCandidateForMerging(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link HorizontalClassMerger#isCandidateForMerging(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isCandidateForMerging(Clazz); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean HorizontalClassMerger.isCandidateForMerging(Clazz)"})
  void testIsCandidateForMerging_givenProgramClassOptimizationInfo() {
    // Arrange
    HorizontalClassMerger horizontalClassMerger =
        new HorizontalClassMerger(true, true, new HashSet<>());

    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(horizontalClassMerger.isCandidateForMerging(clazz));
  }
}
