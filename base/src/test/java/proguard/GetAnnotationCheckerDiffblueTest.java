package proguard;

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
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.util.WarningLogger;
import proguard.classfile.util.WarningPrinter;

class GetAnnotationCheckerDiffblueTest {
  /**
   * Test {@link GetAnnotationChecker#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <p>Method under test: {@link GetAnnotationChecker#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName("Test visitMethodrefConstant(Clazz, MethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GetAnnotationChecker.visitMethodrefConstant(Clazz, MethodrefConstant)"})
  void testVisitMethodrefConstant() {
    // Arrange
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());
    WarningLogger notePrinter = new WarningLogger(logger2, new ArrayList<>());
    GetAnnotationChecker getAnnotationChecker = new GetAnnotationChecker(notePrinter);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName()).thenReturn("Name");
    when(clazz.getName(anyInt())).thenReturn("getAnnotation");
    when(clazz.getClassName(anyInt())).thenReturn("java/lang/Class");

    // Act
    getAnnotationChecker.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz, atLeast(1)).getName();
    verify(clazz).getName(0);
  }

  /**
   * Test {@link GetAnnotationChecker#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <p>Method under test: {@link GetAnnotationChecker#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName("Test visitMethodrefConstant(Clazz, MethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GetAnnotationChecker.visitMethodrefConstant(Clazz, MethodrefConstant)"})
  void testVisitMethodrefConstant2() {
    // Arrange
    PrintWriter printWriter = new PrintWriter(new StringWriter());
    WarningPrinter notePrinter = new WarningPrinter(printWriter, new ArrayList<>());
    GetAnnotationChecker getAnnotationChecker = new GetAnnotationChecker(notePrinter);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName()).thenReturn("Name");
    when(clazz.getName(anyInt())).thenReturn("getAnnotation");
    when(clazz.getClassName(anyInt())).thenReturn("java/lang/Class");

    // Act
    getAnnotationChecker.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz, atLeast(1)).getName();
    verify(clazz).getName(0);
  }

  /**
   * Test {@link GetAnnotationChecker#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@code Class Name}.
   * </ul>
   *
   * <p>Method under test: {@link GetAnnotationChecker#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName("Test visitMethodrefConstant(Clazz, MethodrefConstant); given 'Class Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GetAnnotationChecker.visitMethodrefConstant(Clazz, MethodrefConstant)"})
  void testVisitMethodrefConstant_givenClassName() {
    // Arrange
    WarningPrinter notePrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    GetAnnotationChecker getAnnotationChecker = new GetAnnotationChecker(notePrinter);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");

    // Act
    getAnnotationChecker.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
  }

  /**
   * Test {@link GetAnnotationChecker#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger} {@link WarningLogger#print(String, String)} does nothing.
   *   <li>Then calls {@link WarningLogger#print(String, String)}.
   * </ul>
   *
   * <p>Method under test: {@link GetAnnotationChecker#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodrefConstant(Clazz, MethodrefConstant); given WarningLogger print(String, String) does nothing; then calls print(String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GetAnnotationChecker.visitMethodrefConstant(Clazz, MethodrefConstant)"})
  void testVisitMethodrefConstant_givenWarningLoggerPrintDoesNothing_thenCallsPrint() {
    // Arrange
    WarningLogger notePrinter = mock(WarningLogger.class);
    doNothing().when(notePrinter).print(Mockito.<String>any(), Mockito.<String>any());
    GetAnnotationChecker getAnnotationChecker = new GetAnnotationChecker(notePrinter);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName()).thenReturn("Name");
    when(clazz.getName(anyInt())).thenReturn("getAnnotation");
    when(clazz.getClassName(anyInt())).thenReturn("java/lang/Class");

    // Act
    getAnnotationChecker.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz, atLeast(1)).getName();
    verify(clazz).getName(0);
    verify(notePrinter).print("Name", "Note: Name calls 'Class.getAnnotation'");
  }

  /**
   * Test {@link GetAnnotationChecker#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger#WarningLogger(Logger)} with logger is {@link
   *       ExtendedLoggerWrapper#ExtendedLoggerWrapper(ExtendedLogger, String, MessageFactory)}.
   * </ul>
   *
   * <p>Method under test: {@link GetAnnotationChecker#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodrefConstant(Clazz, MethodrefConstant); given WarningLogger(Logger) with logger is ExtendedLoggerWrapper(ExtendedLogger, String, MessageFactory)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GetAnnotationChecker.visitMethodrefConstant(Clazz, MethodrefConstant)"})
  void testVisitMethodrefConstant_givenWarningLoggerWithLoggerIsExtendedLoggerWrapper() {
    // Arrange
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());
    GetAnnotationChecker getAnnotationChecker =
        new GetAnnotationChecker(new WarningLogger(logger2));

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName()).thenReturn("Name");
    when(clazz.getName(anyInt())).thenReturn("getAnnotation");
    when(clazz.getClassName(anyInt())).thenReturn("java/lang/Class");

    // Act
    getAnnotationChecker.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz, atLeast(1)).getName();
    verify(clazz).getName(0);
  }

  /**
   * Test {@link GetAnnotationChecker#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link WarningPrinter#WarningPrinter(PrintWriter)} with printWriter is {@link
   *       PrintWriter#PrintWriter(Writer)}.
   * </ul>
   *
   * <p>Method under test: {@link GetAnnotationChecker#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodrefConstant(Clazz, MethodrefConstant); given WarningPrinter(PrintWriter) with printWriter is PrintWriter(Writer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GetAnnotationChecker.visitMethodrefConstant(Clazz, MethodrefConstant)"})
  void testVisitMethodrefConstant_givenWarningPrinterWithPrintWriterIsPrintWriter() {
    // Arrange
    WarningPrinter notePrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    GetAnnotationChecker getAnnotationChecker = new GetAnnotationChecker(notePrinter);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName()).thenReturn("Name");
    when(clazz.getName(anyInt())).thenReturn("getAnnotation");
    when(clazz.getClassName(anyInt())).thenReturn("java/lang/Class");

    // Act
    getAnnotationChecker.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz, atLeast(1)).getName();
    verify(clazz).getName(0);
  }

  /**
   * Test {@link GetAnnotationChecker#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass} {@link LibraryClass#getName(int)} return {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link GetAnnotationChecker#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodrefConstant(Clazz, MethodrefConstant); when LibraryClass getName(int) return 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GetAnnotationChecker.visitMethodrefConstant(Clazz, MethodrefConstant)"})
  void testVisitMethodrefConstant_whenLibraryClassGetNameReturnName() {
    // Arrange
    WarningPrinter notePrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    GetAnnotationChecker getAnnotationChecker = new GetAnnotationChecker(notePrinter);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getClassName(anyInt())).thenReturn("java/lang/Class");

    // Act
    getAnnotationChecker.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz).getName(0);
  }
}
