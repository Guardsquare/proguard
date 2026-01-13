package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.util.Processable;

@ExtendWith(MockitoExtension.class)
class InterfaceUsageMarkerDiffblueTest {
  @Mock private ClassUsageMarker classUsageMarker;

  @InjectMocks private InterfaceUsageMarker interfaceUsageMarker;

  /**
   * Test {@link InterfaceUsageMarker#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass_thenThrowUnsupportedOperationException() {
    // Arrange
    InterfaceUsageMarker interfaceUsageMarker = new InterfaceUsageMarker(new ClassUsageMarker());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    when(classUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitProgramClass(new ProgramClass()));
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass2() {
    // Arrange
    doThrow(new UnsupportedOperationException())
        .when(classUsageMarker)
        .markAsUsed(Mockito.<Processable>any());
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(true);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitProgramClass(new ProgramClass()));
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker).isUsed(isA(Processable.class));
    verify(classUsageMarker).markAsUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass3() {
    // Arrange
    doThrow(new UnsupportedOperationException())
        .when(classUsageMarker)
        .markAsUnused(Mockito.<Processable>any());
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(true);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitProgramClass(new ProgramClass()));
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker).isUsed(isA(Processable.class));
    verify(classUsageMarker).markAsUnused(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass4() {
    // Arrange
    doNothing().when(classUsageMarker).markAsUsed(Mockito.<Processable>any());
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(true);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ClassConstant classConstant = new ClassConstant();
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 0);
    programClass.u2interfacesCount = 0;

    // Act
    interfaceUsageMarker.visitProgramClass(programClass);

    // Assert
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
    verify(classUsageMarker).markAsUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass5() {
    // Arrange
    doNothing().when(classUsageMarker).markAsUsed(Mockito.<Processable>any());
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(true);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ClassConstant classConstant = new ClassConstant();
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1);
    programClass.u2interfacesCount = 0;

    // Act
    interfaceUsageMarker.visitProgramClass(programClass);

    // Assert
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
    verify(classUsageMarker).markAsUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#isPossiblyUsed(Processable)}
   *       return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassUsageMarker isPossiblyUsed(Processable) return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassUsageMarkerIsPossiblyUsedReturnFalse() {
    // Arrange
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(false);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);

    // Act
    interfaceUsageMarker.visitProgramClass(new ProgramClass());

    // Assert
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#isPossiblyUsed(Processable)}
   *       return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassUsageMarker isPossiblyUsed(Processable) return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassUsageMarkerIsPossiblyUsedReturnFalse2() {
    // Arrange
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(false);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);

    // Act
    interfaceUsageMarker.visitProgramClass(new ProgramClass());

    // Assert
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#markAsUnused(Processable)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassUsageMarker markAsUnused(Processable) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassUsageMarkerMarkAsUnusedDoesNothing() {
    // Arrange
    doNothing().when(classUsageMarker).markAsUnused(Mockito.<Processable>any());
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(true);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);

    // Act
    interfaceUsageMarker.visitProgramClass(new ProgramClass());

    // Assert
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker).isUsed(isA(Processable.class));
    verify(classUsageMarker).markAsUnused(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    InterfaceUsageMarker interfaceUsageMarker =
        new InterfaceUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new LibraryClass());

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    assertSame(usageMarker.currentUsageMark, classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant2() {
    // Arrange
    InterfaceUsageMarker interfaceUsageMarker =
        new InterfaceUsageMarker(
            new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant3() {
    // Arrange
    InterfaceUsageMarker interfaceUsageMarker = new InterfaceUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant4() {
    // Arrange
    when(classUsageMarker.isUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitClassConstant(clazz, new ClassConstant()));
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant5() {
    // Arrange
    doThrow(new UnsupportedOperationException())
        .when(classUsageMarker)
        .markAsUsed(Mockito.<Processable>any());
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new LibraryClass());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitClassConstant(clazz, classConstant));
    verify(classUsageMarker).isUsed(isA(Processable.class));
    verify(classUsageMarker).markAsUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant6() {
    // Arrange
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitClassConstant(clazz, classConstant));
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant7() {
    // Arrange
    doThrow(new UnsupportedOperationException())
        .when(classUsageMarker)
        .markAsUnused(Mockito.<Processable>any());
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(true);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> interfaceUsageMarker.visitClassConstant(clazz, classConstant));
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
    verify(classUsageMarker).markAsUnused(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#isUsed(Processable)} return {@code
   *       true}.
   *   <li>When {@link ClassConstant#ClassConstant()}.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ClassUsageMarker isUsed(Processable) return 'true'; when ClassConstant()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenClassUsageMarkerIsUsedReturnTrue_whenClassConstant() {
    // Arrange
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    LibraryClass clazz = new LibraryClass();

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, new ClassConstant());

    // Assert
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#markAsUnused(Processable)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ClassUsageMarker markAsUnused(Processable) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenClassUsageMarkerMarkAsUnusedDoesNothing() {
    // Arrange
    doNothing().when(classUsageMarker).markAsUnused(Mockito.<Processable>any());
    when(classUsageMarker.isPossiblyUsed(Mockito.<Processable>any())).thenReturn(true);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new ProgramClass());

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    verify(classUsageMarker).isPossiblyUsed(isA(Processable.class));
    verify(classUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
    verify(classUsageMarker).markAsUnused(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker} {@link ClassUsageMarker#markAsUsed(Processable)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ClassUsageMarker markAsUsed(Processable) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenClassUsageMarkerMarkAsUsedDoesNothing() {
    // Arrange
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    doNothing().when(classUsageMarker).markAsUsed(Mockito.<Processable>any());
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new LibraryClass());

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    verify(classUsageMarker).isUsed(isA(Processable.class));
    verify(classUsageMarker).markAsUsed(isA(Processable.class));
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then {@link ClassConstant#ClassConstant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then ClassConstant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenClassConstantProcessingInfoIsNull() {
    // Arrange
    InterfaceUsageMarker interfaceUsageMarker = new InterfaceUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant();

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then {@link ClassConstant#ClassConstant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then ClassConstant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenClassConstantProcessingInfoIsNull2() {
    // Arrange
    InterfaceUsageMarker interfaceUsageMarker =
        new InterfaceUsageMarker(
            new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant();

    // Act
    interfaceUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert that nothing has changed
    assertNull(classConstant.getProcessingInfo());
  }

  /**
   * Test {@link InterfaceUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}.
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}
   */
  @Test
  @DisplayName("Test visitUtf8Constant(Clazz, Utf8Constant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitUtf8Constant(Clazz, Utf8Constant)"})
  void testVisitUtf8Constant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    InterfaceUsageMarker interfaceUsageMarker =
        new InterfaceUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    Utf8Constant utf8Constant = new Utf8Constant();

    // Act
    interfaceUsageMarker.visitUtf8Constant(clazz, utf8Constant);

    // Assert
    assertSame(usageMarker.currentUsageMark, utf8Constant.getProcessingInfo());
  }

  /**
   * Test {@link InterfaceUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}.
   *
   * <ul>
   *   <li>Then {@link Utf8Constant#Utf8Constant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link InterfaceUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}
   */
  @Test
  @DisplayName(
      "Test visitUtf8Constant(Clazz, Utf8Constant); then Utf8Constant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InterfaceUsageMarker.visitUtf8Constant(Clazz, Utf8Constant)"})
  void testVisitUtf8Constant_thenUtf8ConstantProcessingInfoIsNull() {
    // Arrange
    InterfaceUsageMarker interfaceUsageMarker =
        new InterfaceUsageMarker(new ClassUsageMarker(new ShortestUsageMarker()));
    LibraryClass clazz = new LibraryClass();
    Utf8Constant utf8Constant = new Utf8Constant();

    // Act
    interfaceUsageMarker.visitUtf8Constant(clazz, utf8Constant);

    // Assert that nothing has changed
    assertNull(utf8Constant.getProcessingInfo());
  }
}
