package proguard;

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
import proguard.classfile.util.WarningLogger;
import proguard.classfile.util.WarningPrinter;
import proguard.resources.file.ResourceFile;
import proguard.resources.kotlinmodule.KotlinModule;

class DuplicateResourceFilePrinterDiffblueTest {
  /**
   * Test {@link DuplicateResourceFilePrinter#visitResourceFile(ResourceFile)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger} {@link WarningLogger#print(String, String)} does nothing.
   *   <li>Then calls {@link WarningLogger#print(String, String)}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateResourceFilePrinter#visitResourceFile(ResourceFile)}
   */
  @Test
  @DisplayName(
      "Test visitResourceFile(ResourceFile); given WarningLogger print(String, String) does nothing; then calls print(String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateResourceFilePrinter.visitResourceFile(ResourceFile)"})
  void testVisitResourceFile_givenWarningLoggerPrintDoesNothing_thenCallsPrint() {
    // Arrange
    WarningLogger notePrinter = mock(WarningLogger.class);
    doNothing().when(notePrinter).print(Mockito.<String>any(), Mockito.<String>any());
    DuplicateResourceFilePrinter duplicateResourceFilePrinter =
        new DuplicateResourceFilePrinter(notePrinter);

    // Act
    duplicateResourceFilePrinter.visitResourceFile(new ResourceFile("foo.txt", 3L));

    // Assert
    verify(notePrinter).print("foo.txt", "Note: duplicate definition of resource file [foo.txt]");
  }

  /**
   * Test {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}.
   *
   * <p>Method under test: {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}
   */
  @Test
  @DisplayName("Test visitKotlinModule(KotlinModule)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateResourceFilePrinter.visitKotlinModule(KotlinModule)"})
  void testVisitKotlinModule() {
    // Arrange
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());
    WarningLogger notePrinter = new WarningLogger(logger2, new ArrayList<>());
    DuplicateResourceFilePrinter duplicateResourceFilePrinter =
        new DuplicateResourceFilePrinter(notePrinter);

    KotlinModule kotlinModule = mock(KotlinModule.class);
    when(kotlinModule.getFileName()).thenReturn("foo.txt");

    // Act
    duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

    // Assert
    verify(kotlinModule, atLeast(1)).getFileName();
  }

  /**
   * Test {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}.
   *
   * <p>Method under test: {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}
   */
  @Test
  @DisplayName("Test visitKotlinModule(KotlinModule)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateResourceFilePrinter.visitKotlinModule(KotlinModule)"})
  void testVisitKotlinModule2() {
    // Arrange
    PrintWriter printWriter = new PrintWriter(new StringWriter());
    WarningPrinter notePrinter = new WarningPrinter(printWriter, new ArrayList<>());
    DuplicateResourceFilePrinter duplicateResourceFilePrinter =
        new DuplicateResourceFilePrinter(notePrinter);

    KotlinModule kotlinModule = mock(KotlinModule.class);
    when(kotlinModule.getFileName()).thenReturn("foo.txt");

    // Act
    duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

    // Assert
    verify(kotlinModule, atLeast(1)).getFileName();
  }

  /**
   * Test {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}.
   *
   * <ul>
   *   <li>Given {@link ArrayList#ArrayList()} add {@code Note: duplicate definition of Kotlin
   *       module file [}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinModule(KotlinModule); given ArrayList() add 'Note: duplicate definition of Kotlin module file ['")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateResourceFilePrinter.visitKotlinModule(KotlinModule)"})
  void testVisitKotlinModule_givenArrayListAddNoteDuplicateDefinitionOfKotlinModuleFile() {
    // Arrange
    ArrayList<String> classFilter = new ArrayList<>();
    classFilter.add("Note: duplicate definition of Kotlin module file [");
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());

    WarningLogger notePrinter = new WarningLogger(logger2, classFilter);
    DuplicateResourceFilePrinter duplicateResourceFilePrinter =
        new DuplicateResourceFilePrinter(notePrinter);

    KotlinModule kotlinModule = mock(KotlinModule.class);
    when(kotlinModule.getFileName()).thenReturn("foo.txt");

    // Act
    duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

    // Assert
    verify(kotlinModule, atLeast(1)).getFileName();
  }

  /**
   * Test {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger} {@link WarningLogger#print(String, String)} does nothing.
   *   <li>Then calls {@link WarningLogger#print(String, String)}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinModule(KotlinModule); given WarningLogger print(String, String) does nothing; then calls print(String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateResourceFilePrinter.visitKotlinModule(KotlinModule)"})
  void testVisitKotlinModule_givenWarningLoggerPrintDoesNothing_thenCallsPrint() {
    // Arrange
    WarningLogger notePrinter = mock(WarningLogger.class);
    doNothing().when(notePrinter).print(Mockito.<String>any(), Mockito.<String>any());
    DuplicateResourceFilePrinter duplicateResourceFilePrinter =
        new DuplicateResourceFilePrinter(notePrinter);

    KotlinModule kotlinModule = mock(KotlinModule.class);
    when(kotlinModule.getFileName()).thenReturn("foo.txt");

    // Act
    duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

    // Assert
    verify(notePrinter)
        .print("foo.txt", "Note: duplicate definition of Kotlin module file [foo.txt]");
    verify(kotlinModule, atLeast(1)).getFileName();
  }

  /**
   * Test {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}.
   *
   * <ul>
   *   <li>Given {@link WarningLogger#WarningLogger(Logger)} with logger is {@link
   *       ExtendedLoggerWrapper#ExtendedLoggerWrapper(ExtendedLogger, String, MessageFactory)}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinModule(KotlinModule); given WarningLogger(Logger) with logger is ExtendedLoggerWrapper(ExtendedLogger, String, MessageFactory)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateResourceFilePrinter.visitKotlinModule(KotlinModule)"})
  void testVisitKotlinModule_givenWarningLoggerWithLoggerIsExtendedLoggerWrapper() {
    // Arrange
    StatusLogger logger = StatusLogger.getLogger();
    ExtendedLoggerWrapper logger2 =
        new ExtendedLoggerWrapper(logger, "Name", new FormattedMessageFactory());
    DuplicateResourceFilePrinter duplicateResourceFilePrinter =
        new DuplicateResourceFilePrinter(new WarningLogger(logger2));

    KotlinModule kotlinModule = mock(KotlinModule.class);
    when(kotlinModule.getFileName()).thenReturn("foo.txt");

    // Act
    duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

    // Assert
    verify(kotlinModule, atLeast(1)).getFileName();
  }

  /**
   * Test {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}.
   *
   * <ul>
   *   <li>Given {@link WarningPrinter#WarningPrinter(PrintWriter)} with printWriter is {@link
   *       PrintWriter#PrintWriter(Writer)}.
   * </ul>
   *
   * <p>Method under test: {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinModule(KotlinModule); given WarningPrinter(PrintWriter) with printWriter is PrintWriter(Writer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DuplicateResourceFilePrinter.visitKotlinModule(KotlinModule)"})
  void testVisitKotlinModule_givenWarningPrinterWithPrintWriterIsPrintWriter() {
    // Arrange
    WarningPrinter notePrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    DuplicateResourceFilePrinter duplicateResourceFilePrinter =
        new DuplicateResourceFilePrinter(notePrinter);

    KotlinModule kotlinModule = mock(KotlinModule.class);
    when(kotlinModule.getFileName()).thenReturn("foo.txt");

    // Act
    duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

    // Assert
    verify(kotlinModule, atLeast(1)).getFileName();
  }
}
