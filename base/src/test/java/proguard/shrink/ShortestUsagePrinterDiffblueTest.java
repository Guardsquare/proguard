package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.Processable;

class ShortestUsagePrinterDiffblueTest {
  /**
   * Test {@link ShortestUsagePrinter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = new ShortestUsageMarker();
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, true, new PrintWriter(new StringWriter()));

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> shortestUsagePrinter.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(new ShortestUsageMark("Just cause"));
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, true, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass).getName();
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass2() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");
    LibraryClass clazz = new LibraryClass(1, "  ", "  ");

    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, clazz);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(shortestUsageMark);
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, true, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass).getName();
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link Clazz} {@link Clazz#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link Clazz#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given Clazz accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClazzAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    Clazz clazz = mock(Clazz.class);
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());
    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(new ShortestUsageMark("Just cause"), "Just cause", 1, clazz);

    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(shortestUsageMark);
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, true, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramClass(programClass);

    // Assert
    verify(clazz).accept(isA(ClassVisitor.class));
    verify(programClass).getName();
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   *   <li>Then calls {@link ProgramClass#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given 'Processing Info'; then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenProcessingInfo_thenCallsGetProcessingInfo() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = new ShortestUsageMarker();
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, true, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn("Processing Info");
    when(programClass.getName()).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass).getName();
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(new ShortestUsageMark("Just cause"));
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramField programField = mock(ProgramField.class);
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getName();
    verify(programField).getDescriptor(isA(Clazz.class));
    verify(programField).getName(isA(Clazz.class));
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField2() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");
    LibraryClass clazz = new LibraryClass(1, ".", ".");

    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, clazz);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(shortestUsageMark);
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramField programField = mock(ProgramField.class);
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getName();
    verify(programField).getDescriptor(isA(Clazz.class));
    verify(programField).getName(isA(Clazz.class));
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link Clazz} {@link Clazz#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link Clazz#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given Clazz accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenClazzAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    Clazz clazz = mock(Clazz.class);
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());
    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(new ShortestUsageMark("Just cause"), "Just cause", 1, clazz);

    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(shortestUsageMark);
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramField programField = mock(ProgramField.class);
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramField(programClass, programField);

    // Assert
    verify(clazz).accept(isA(ClassVisitor.class));
    verify(programClass).getName();
    verify(programField).getDescriptor(isA(Clazz.class));
    verify(programField).getName(isA(Clazz.class));
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   *   <li>Then calls {@link ProgramField#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given 'Processing Info'; then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenProcessingInfo_thenCallsGetProcessingInfo() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = new ShortestUsageMarker();
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramField programField = mock(ProgramField.class);
    when(programField.getProcessingInfo()).thenReturn("Processing Info");
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    shortestUsagePrinter.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getName();
    verify(programField).getDescriptor(isA(Clazz.class));
    verify(programField).getName(isA(Clazz.class));
    verify(programField).getProcessingInfo();
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenCallsGetString() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = new ShortestUsageMarker();
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    shortestUsagePrinter.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).getName();
    verify(programClass, atLeast(1)).getString(0);
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenThrowUnsupportedOperationException() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramField programField = mock(ProgramField.class);
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> shortestUsagePrinter.visitProgramField(programClass, programField));
    verify(programClass).getName();
    verify(programField).getDescriptor(isA(Clazz.class));
    verify(programField).getName(isA(Clazz.class));
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = new ShortestUsageMarker();
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    shortestUsagePrinter.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass).getName();
    verify(programClass, atLeast(1)).getString(0);
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod2() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");

    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, clazz);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(shortestUsageMark);
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programMethod.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing()
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());

    // Act
    shortestUsagePrinter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).getName();
    verify(programMethod).getDescriptor(isA(Clazz.class));
    verify(programMethod).getName(isA(Clazz.class));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code Descriptor}.
   *   <li>Then calls {@link ProgramMethod#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'Descriptor'; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenDescriptor_thenCallsGetDescriptor() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(new ShortestUsageMark("Just cause"));
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programMethod.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing()
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());

    // Act
    shortestUsagePrinter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).getName();
    verify(programMethod).getDescriptor(isA(Clazz.class));
    verify(programMethod).getName(isA(Clazz.class));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenLibraryClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());
    doNothing().when(clazz).addSubClass(Mockito.<Clazz>any());
    clazz.addSubClass(new LibraryClass());
    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(new ShortestUsageMark("Just cause"), "Just cause", 1, clazz);

    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(shortestUsageMark);
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programMethod.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing()
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());

    // Act
    shortestUsagePrinter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(clazz).accept(isA(ClassVisitor.class));
    verify(clazz).addSubClass(isA(Clazz.class));
    verify(programClass).getName();
    verify(programMethod).getDescriptor(isA(Clazz.class));
    verify(programMethod).getName(isA(Clazz.class));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsGetString() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenReturn(new ShortestUsageMark("Just cause"));
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    shortestUsagePrinter.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass).getName();
    verify(programClass, atLeast(1)).getString(0);
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenThrowUnsupportedOperationException() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = mock(ShortestUsageMarker.class);
    when(shortestUsageMarker.getShortestUsageMark(Mockito.<Processable>any()))
        .thenThrow(new UnsupportedOperationException());
    when(shortestUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> shortestUsagePrinter.visitProgramMethod(programClass, new ProgramMethod()));
    verify(programClass).getName();
    verify(programClass, atLeast(1)).getString(0);
    verify(shortestUsageMarker).getShortestUsageMark(isA(Processable.class));
    verify(shortestUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link ShortestUsagePrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#attributesAccept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsagePrinter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls attributesAccept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsagePrinter.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsAttributesAccept() {
    // Arrange
    ShortestUsageMarker shortestUsageMarker = new ShortestUsageMarker();
    ShortestUsagePrinter shortestUsagePrinter =
        new ShortestUsagePrinter(shortestUsageMarker, true, new PrintWriter(new StringWriter()));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act
    shortestUsagePrinter.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }
}
