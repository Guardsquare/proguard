package proguard.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class JavaUtilDiffblueTest {
  /**
   * Test {@link JavaUtil#currentJavaVersion()}.
   *
   * <p>Method under test: {@link JavaUtil#currentJavaVersion()}
   */
  @Test
  @DisplayName("Test currentJavaVersion()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int JavaUtil.currentJavaVersion()"})
  void testCurrentJavaVersion() {
    // Arrange, Act and Assert
    assertEquals(8, JavaUtil.currentJavaVersion());
  }

  /**
   * Test {@link JavaUtil#getCurrentJavaHome()}.
   *
   * <p>Method under test: {@link JavaUtil#getCurrentJavaHome()}
   */
  @Test
  @DisplayName("Test getCurrentJavaHome()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"File JavaUtil.getCurrentJavaHome()"})
  void testGetCurrentJavaHome() {
    // Arrange and Act
    File actualCurrentJavaHome = JavaUtil.getCurrentJavaHome();

    // Assert
    assertEquals("amazon-corretto-8.462.08.1-linux-x64", actualCurrentJavaHome.getName());
    assertTrue(actualCurrentJavaHome.isAbsolute());
  }

  /**
   * Test {@link JavaUtil#getRtJar()}.
   *
   * <p>Method under test: {@link JavaUtil#getRtJar()}
   */
  @Test
  @DisplayName("Test getRtJar()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"File JavaUtil.getRtJar()"})
  void testGetRtJar() {
    // Arrange and Act
    File actualRtJar = JavaUtil.getRtJar();

    // Assert
    assertEquals("rt.jar", actualRtJar.getName());
    assertTrue(actualRtJar.isAbsolute());
  }

  /**
   * Test {@link JavaUtil#getJmodBase()}.
   *
   * <p>Method under test: {@link JavaUtil#getJmodBase()}
   */
  @Test
  @DisplayName("Test getJmodBase()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"File JavaUtil.getJmodBase()"})
  void testGetJmodBase() {
    // Arrange and Act
    File actualJmodBase = JavaUtil.getJmodBase();

    // Assert
    assertEquals("java.base.jmod", actualJmodBase.getName());
    assertTrue(actualJmodBase.isAbsolute());
  }
}
