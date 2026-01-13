package proguard;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import org.apache.logging.log4j.message.FormattedMessageFactory;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;
import org.apache.logging.log4j.status.StatusLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.util.WarningLogger;
import proguard.classfile.util.WarningPrinter;

class DuplicateClassPrinterDiffblueTest {
  /**
   * Test {@link DuplicateClassPrinter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateClassPrinter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateClassPrinter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    WarningPrinter notePrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    DuplicateClassPrinter duplicateClassPrinter = new DuplicateClassPrinter(notePrinter);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> duplicateClassPrinter.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateClassPrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());
    WarningLogger notePrinter = new WarningLogger(logger2, new ArrayList<>());
    DuplicateClassPrinter duplicateClassPrinter = new DuplicateClassPrinter(notePrinter);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    duplicateClassPrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass, atLeast(1)).getName();
  }

  /**
   * Test {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateClassPrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass2() {
    // Arrange
    PrintWriter printWriter = new PrintWriter(new StringWriter());
    WarningPrinter notePrinter = new WarningPrinter(printWriter, new ArrayList<>());
    DuplicateClassPrinter duplicateClassPrinter = new DuplicateClassPrinter(notePrinter);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    duplicateClassPrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass, atLeast(1)).getName();
  }

  /**
   * Test {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ArrayList#ArrayList()} add {@code Note: duplicate definition of program
   *       class [}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ArrayList() add 'Note: duplicate definition of program class ['")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateClassPrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenArrayListAddNoteDuplicateDefinitionOfProgramClass() {
    // Arrange
    ArrayList<String> classFilter = new ArrayList<>();
    classFilter.add("Note: duplicate definition of program class [");
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());

    WarningLogger notePrinter = new WarningLogger(logger2, classFilter);
    DuplicateClassPrinter duplicateClassPrinter = new DuplicateClassPrinter(notePrinter);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    duplicateClassPrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass, atLeast(1)).getName();
  }

  /**
   * Test {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger} {@link WarningLogger#print(String, String)} does nothing.
   *   <li>Then calls {@link WarningLogger#print(String, String)}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given WarningLogger print(String, String) does nothing; then calls print(String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateClassPrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenWarningLoggerPrintDoesNothing_thenCallsPrint() {
    // Arrange
    WarningLogger notePrinter = mock(WarningLogger.class);
    doNothing().when(notePrinter).print(Mockito.<String>any(), Mockito.<String>any());
    DuplicateClassPrinter duplicateClassPrinter = new DuplicateClassPrinter(notePrinter);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    duplicateClassPrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass, atLeast(1)).getName();
    verify(notePrinter).print("Name", "Note: duplicate definition of program class [Name]");
  }

  /**
   * Test {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger#WarningLogger(Logger)} with logger is {@link
   *       ExtendedLoggerWrapper#ExtendedLoggerWrapper(ExtendedLogger, String, MessageFactory)}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given WarningLogger(Logger) with logger is ExtendedLoggerWrapper(ExtendedLogger, String, MessageFactory)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateClassPrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenWarningLoggerWithLoggerIsExtendedLoggerWrapper() {
    // Arrange
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());
    DuplicateClassPrinter duplicateClassPrinter =
        new DuplicateClassPrinter(new WarningLogger(logger2));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    duplicateClassPrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass, atLeast(1)).getName();
  }

  /**
   * Test {@link DuplicateClassPrinter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger} {@link WarningLogger#print(String, String)} does nothing.
   *   <li>Then calls {@link WarningLogger#print(String, String)}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateClassPrinter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryClass(LibraryClass); given WarningLogger print(String, String) does nothing; then calls print(String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateClassPrinter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenWarningLoggerPrintDoesNothing_thenCallsPrint() {
    // Arrange
    WarningLogger notePrinter = mock(WarningLogger.class);
    doNothing().when(notePrinter).print(Mockito.<String>any(), Mockito.<String>any());
    DuplicateClassPrinter duplicateClassPrinter = new DuplicateClassPrinter(notePrinter);
    LibraryClass libraryClass =
        new LibraryClass(
            1,
            "Note: duplicate definition of library class [",
            "Note: duplicate definition of library class [");

    // Act
    duplicateClassPrinter.visitLibraryClass(libraryClass);

    // Assert
    verify(notePrinter)
        .print(
            "Note: duplicate definition of library class [",
            "Note: duplicate definition of library class [Note: duplicate definition of library class []");
  }
}
