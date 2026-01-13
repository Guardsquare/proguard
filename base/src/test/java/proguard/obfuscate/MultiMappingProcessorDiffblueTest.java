package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.WarningPrinter;

class MultiMappingProcessorDiffblueTest {
  /**
   * Test {@link MultiMappingProcessor#processClassMapping(String, String)}.
   *
   * <ul>
   *   <li>Given {@link WarningPrinter#WarningPrinter(PrintWriter)} with printWriter is {@code
   *       null}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MultiMappingProcessor#processClassMapping(String, String)}
   */
  @Test
  @DisplayName(
      "Test processClassMapping(String, String); given WarningPrinter(PrintWriter) with printWriter is 'null'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MultiMappingProcessor.processClassMapping(String, String)"})
  void testProcessClassMapping_givenWarningPrinterWithPrintWriterIsNull_thenReturnFalse() {
    // Arrange
    MappingKeeper mappingKeeper =
        new MappingKeeper(KotlinConstants.dummyClassPool, new WarningPrinter(null));
    MappingProcessor[] mappingProcessors = new MappingProcessor[] {mappingKeeper};
    MultiMappingProcessor multiMappingProcessor = new MultiMappingProcessor(mappingProcessors);
    MappingProcessor[] mappingProcessors2 = new MappingProcessor[] {multiMappingProcessor};
    MultiMappingProcessor multiMappingProcessor2 = new MultiMappingProcessor(mappingProcessors2);

    // Act and Assert
    assertFalse(multiMappingProcessor2.processClassMapping("Class Name", "New Class Name"));
  }

  /**
   * Test {@link MultiMappingProcessor#processClassMapping(String, String)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MultiMappingProcessor#processClassMapping(String, String)}
   */
  @Test
  @DisplayName("Test processClassMapping(String, String); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MultiMappingProcessor.processClassMapping(String, String)"})
  void testProcessClassMapping_thenReturnFalse() {
    // Arrange
    MappingKeeper mappingKeeper =
        new MappingKeeper(KotlinConstants.dummyClassPool, mock(WarningPrinter.class));
    MappingProcessor[] mappingProcessors = new MappingProcessor[] {mappingKeeper};
    MultiMappingProcessor multiMappingProcessor = new MultiMappingProcessor(mappingProcessors);

    // Act and Assert
    assertFalse(multiMappingProcessor.processClassMapping("Class Name", "New Class Name"));
  }
}
