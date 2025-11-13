package proguard.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.kotlin.KotlinConstants;

class ConfigurationLoggingInstructionSequenceConstantsDiffblueTest {
  /**
   * Test {@link
   * ConfigurationLoggingInstructionSequenceConstants#ConfigurationLoggingInstructionSequenceConstants(ClassPool,
   * ClassPool)}.
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceConstants#ConfigurationLoggingInstructionSequenceConstants(ClassPool,
   * ClassPool)}
   */
  @Test
  @DisplayName("Test new ConfigurationLoggingInstructionSequenceConstants(ClassPool, ClassPool)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationLoggingInstructionSequenceConstants.<init>(ClassPool, ClassPool)"
  })
  void testNewConfigurationLoggingInstructionSequenceConstants() {
    // Arrange and Act
    ConfigurationLoggingInstructionSequenceConstants
        actualConfigurationLoggingInstructionSequenceConstants =
            new ConfigurationLoggingInstructionSequenceConstants(
                KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool);

    // Assert
    assertEquals(106, actualConfigurationLoggingInstructionSequenceConstants.CONSTANTS.length);
    assertEquals(
        ConfigurationLogger.ALL_PUBLIC_FIELDS_KEPT,
        actualConfigurationLoggingInstructionSequenceConstants.RESOURCE.length);
  }

  /**
   * Test {@link
   * ConfigurationLoggingInstructionSequenceConstants#ConfigurationLoggingInstructionSequenceConstants(ClassPool,
   * ClassPool)}.
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceConstants#ConfigurationLoggingInstructionSequenceConstants(ClassPool,
   * ClassPool)}
   */
  @Test
  @DisplayName("Test new ConfigurationLoggingInstructionSequenceConstants(ClassPool, ClassPool)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationLoggingInstructionSequenceConstants.<init>(ClassPool, ClassPool)"
  })
  void testNewConfigurationLoggingInstructionSequenceConstants2() {
    // Arrange and Act
    ConfigurationLoggingInstructionSequenceConstants
        actualConfigurationLoggingInstructionSequenceConstants =
            new ConfigurationLoggingInstructionSequenceConstants(
                null, KotlinConstants.dummyClassPool);

    // Assert
    assertEquals(106, actualConfigurationLoggingInstructionSequenceConstants.CONSTANTS.length);
    assertEquals(
        ConfigurationLogger.ALL_PUBLIC_FIELDS_KEPT,
        actualConfigurationLoggingInstructionSequenceConstants.RESOURCE.length);
  }
}
