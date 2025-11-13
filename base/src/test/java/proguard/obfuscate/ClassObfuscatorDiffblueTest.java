package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;

class ClassObfuscatorDiffblueTest {
  /**
   * Test {@link ClassObfuscator#setNewClassName(Clazz, String)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then {@link LibraryClass#LibraryClass()} ProcessingInfo is {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link ClassObfuscator#setNewClassName(Clazz, String)}
   */
  @Test
  @DisplayName(
      "Test setNewClassName(Clazz, String); when LibraryClass(); then LibraryClass() ProcessingInfo is 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassObfuscator.setNewClassName(Clazz, String)"})
  void testSetNewClassName_whenLibraryClass_thenLibraryClassProcessingInfoIsName() {
    // Arrange
    LibraryClass clazz = new LibraryClass();

    // Act
    ClassObfuscator.setNewClassName(clazz, "Name");

    // Assert
    assertEquals("Name", clazz.getProcessingInfo());
  }

  /**
   * Test {@link ClassObfuscator#hasOriginalClassName(Clazz)}.
   *
   * <p>Method under test: {@link ClassObfuscator#hasOriginalClassName(Clazz)}
   */
  @Test
  @DisplayName("Test hasOriginalClassName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassObfuscator.hasOriginalClassName(Clazz)"})
  void testHasOriginalClassName() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo("Clazz");

    // Act and Assert
    assertFalse(ClassObfuscator.hasOriginalClassName(clazz));
  }

  /**
   * Test {@link ClassObfuscator#hasOriginalClassName(Clazz)}.
   *
   * <p>Method under test: {@link ClassObfuscator#hasOriginalClassName(Clazz)}
   */
  @Test
  @DisplayName("Test hasOriginalClassName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassObfuscator.hasOriginalClassName(Clazz)"})
  void testHasOriginalClassName2() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");

    // Act and Assert
    assertFalse(ClassObfuscator.hasOriginalClassName(clazz));
  }

  /**
   * Test {@link ClassObfuscator#hasOriginalClassName(Clazz)}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassObfuscator#hasOriginalClassName(Clazz)}
   */
  @Test
  @DisplayName("Test hasOriginalClassName(Clazz); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassObfuscator.hasOriginalClassName(Clazz)"})
  void testHasOriginalClassName_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "Clazz", "Super Class Name");
    clazz.setProcessingInfo("Clazz");

    // Act and Assert
    assertTrue(ClassObfuscator.hasOriginalClassName(clazz));
  }

  /**
   * Test {@link ClassObfuscator#newClassName(Clazz)}.
   *
   * <ul>
   *   <li>Given {@code Clazz}.
   *   <li>Then return {@code Clazz}.
   * </ul>
   *
   * <p>Method under test: {@link ClassObfuscator#newClassName(Clazz)}
   */
  @Test
  @DisplayName("Test newClassName(Clazz); given 'Clazz'; then return 'Clazz'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassObfuscator.newClassName(Clazz)"})
  void testNewClassName_givenClazz_thenReturnClazz() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo("Clazz");

    // Act and Assert
    assertEquals("Clazz", ClassObfuscator.newClassName(clazz));
  }

  /**
   * Test {@link ClassObfuscator#newClassName(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassObfuscator#newClassName(Clazz)}
   */
  @Test
  @DisplayName("Test newClassName(Clazz); when LibraryClass(); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassObfuscator.newClassName(Clazz)"})
  void testNewClassName_whenLibraryClass_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(ClassObfuscator.newClassName(new LibraryClass()));
  }
}
