package proguard.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.kotlin.KotlinConstants;

class ConfigurationLoggingAdderDiffblueTest {
  /**
   * Test {@link ConfigurationLoggingAdder#execute(AppView)}.
   *
   * <p>Method under test: {@link ConfigurationLoggingAdder#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationLoggingAdder.execute(AppView)"})
  void testExecute() throws IOException {
    // Arrange
    ConfigurationLoggingAdder configurationLoggingAdder = new ConfigurationLoggingAdder();
    AppView appView = new AppView(new ClassPool(), KotlinConstants.dummyClassPool);

    // Act
    configurationLoggingAdder.execute(appView);

    // Assert
    assertEquals(3, appView.programClassPool.size());
  }

  /**
   * Test {@link ConfigurationLoggingAdder#execute(AppView)}.
   *
   * <p>Method under test: {@link ConfigurationLoggingAdder#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationLoggingAdder.execute(AppView)"})
  void testExecute2() throws IOException {
    // Arrange
    ConfigurationLoggingAdder configurationLoggingAdder = new ConfigurationLoggingAdder();

    ClassPool programClassPool = new ClassPool();
    LibraryClass clazz = new LibraryClass(4, "This Class Name", "Super Class Name");
    programClassPool.addClass("java/lang/Class", clazz);
    AppView appView = new AppView(programClassPool, KotlinConstants.dummyClassPool);

    // Act
    configurationLoggingAdder.execute(appView);

    // Assert
    assertEquals(4, appView.programClassPool.size());
  }

  /**
   * Test {@link ConfigurationLoggingAdder#execute(AppView)}.
   *
   * <p>Method under test: {@link ConfigurationLoggingAdder#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationLoggingAdder.execute(AppView)"})
  void testExecute3() throws IOException {
    // Arrange
    ConfigurationLoggingAdder configurationLoggingAdder = new ConfigurationLoggingAdder();

    LibraryClass clazz = new LibraryClass(4, "This Class Name", "Super Class Name");
    clazz.addProcessingFlags(-1, 4, 2, 4);

    ClassPool programClassPool = new ClassPool();
    programClassPool.addClass("java/lang/Class", clazz);
    AppView appView = new AppView(programClassPool, KotlinConstants.dummyClassPool);

    // Act
    configurationLoggingAdder.execute(appView);

    // Assert
    assertEquals(4, appView.programClassPool.size());
  }

  /**
   * Test {@link ConfigurationLoggingAdder#execute(AppView)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClass#ProgramClass()}.
   *   <li>When {@link ClassPool#ClassPool()} addClass {@code java/lang/Class} and {@link
   *       ProgramClass#ProgramClass()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationLoggingAdder#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); given ProgramClass(); when ClassPool() addClass 'java/lang/Class' and ProgramClass()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationLoggingAdder.execute(AppView)"})
  void testExecute_givenProgramClass_whenClassPoolAddClassJavaLangClassAndProgramClass()
      throws IOException {
    // Arrange
    ConfigurationLoggingAdder configurationLoggingAdder = new ConfigurationLoggingAdder();

    ClassPool programClassPool = new ClassPool();
    programClassPool.addClass("java/lang/Class", new ProgramClass());
    AppView appView = new AppView(programClassPool, KotlinConstants.dummyClassPool);

    // Act
    configurationLoggingAdder.execute(appView);

    // Assert
    assertEquals(4, appView.programClassPool.size());
  }

  /**
   * Test {@link ConfigurationLoggingAdder#execute(AppView)}.
   *
   * <ul>
   *   <li>When {@link AppView#AppView()}.
   *   <li>Then {@link AppView#AppView()} {@link AppView#programClassPool} size is three.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationLoggingAdder#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); when AppView(); then AppView() programClassPool size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationLoggingAdder.execute(AppView)"})
  void testExecute_whenAppView_thenAppViewProgramClassPoolSizeIsThree() throws IOException {
    // Arrange
    ConfigurationLoggingAdder configurationLoggingAdder = new ConfigurationLoggingAdder();
    AppView appView = new AppView();

    // Act
    configurationLoggingAdder.execute(appView);

    // Assert
    assertEquals(3, appView.programClassPool.size());
  }

  /**
   * Test new {@link ConfigurationLoggingAdder} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ConfigurationLoggingAdder}
   */
  @Test
  @DisplayName("Test new ConfigurationLoggingAdder (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationLoggingAdder.<init>()"})
  void testNewConfigurationLoggingAdder() {
    // Arrange, Act and Assert
    assertEquals(
        "proguard.configuration.ConfigurationLoggingAdder",
        new ConfigurationLoggingAdder().getName());
  }
}
