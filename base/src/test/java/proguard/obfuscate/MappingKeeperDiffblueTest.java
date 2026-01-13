package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.WarningPrinter;

class MappingKeeperDiffblueTest {
  /**
   * Test {@link MappingKeeper#processClassMapping(String, String)}.
   *
   * <ul>
   *   <li>When {@code Class Name}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MappingKeeper#processClassMapping(String, String)}
   */
  @Test
  @DisplayName("Test processClassMapping(String, String); when 'Class Name'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MappingKeeper.processClassMapping(String, String)"})
  void testProcessClassMapping_whenClassName_thenReturnFalse() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    MappingKeeper mappingKeeper = new MappingKeeper(KotlinConstants.dummyClassPool, warningPrinter);

    // Act and Assert
    assertFalse(mappingKeeper.processClassMapping("Class Name", "New Class Name"));
  }
}
