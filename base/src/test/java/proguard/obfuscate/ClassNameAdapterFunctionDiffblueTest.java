package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.testutils.cpa.NamedClass;

class ClassNameAdapterFunctionDiffblueTest {
  /**
   * Test {@link ClassNameAdapterFunction#ClassNameAdapterFunction(ClassPool)}.
   *
   * <ul>
   *   <li>Given {@link NamedClass#NamedClass(String)} with {@code Member Name}.
   * </ul>
   *
   * <p>Method under test: {@link ClassNameAdapterFunction#ClassNameAdapterFunction(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test new ClassNameAdapterFunction(ClassPool); given NamedClass(String) with 'Member Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassNameAdapterFunction.<init>(ClassPool)"})
  void testNewClassNameAdapterFunction_givenNamedClassWithMemberName() {
    // Arrange
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");

    ClassPool classPool = new ClassPool(libraryClass);
    classPool.addClass(new NamedClass("Member Name"));

    // Act and Assert
    assertNull(new ClassNameAdapterFunction(classPool).transform("foo.txt"));
  }

  /**
   * Test {@link ClassNameAdapterFunction#ClassNameAdapterFunction(ClassPool)}.
   *
   * <ul>
   *   <li>When {@link ClassPool#ClassPool(Clazz[])} with classes is {@link
   *       LibraryClass#LibraryClass(int, String, String)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassNameAdapterFunction#ClassNameAdapterFunction(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test new ClassNameAdapterFunction(ClassPool); when ClassPool(Clazz[]) with classes is LibraryClass(int, String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassNameAdapterFunction.<init>(ClassPool)"})
  void testNewClassNameAdapterFunction_whenClassPoolWithClassesIsLibraryClass() {
    // Arrange
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");

    // Act and Assert
    assertNull(new ClassNameAdapterFunction(new ClassPool(libraryClass)).transform("foo.txt"));
  }

  /**
   * Test {@link ClassNameAdapterFunction#ClassNameAdapterFunction(ClassPool)}.
   *
   * <ul>
   *   <li>When {@link ClassPool#ClassPool()}.
   *   <li>Then return transform {@code foo.txt} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassNameAdapterFunction#ClassNameAdapterFunction(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test new ClassNameAdapterFunction(ClassPool); when ClassPool(); then return transform 'foo.txt' is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassNameAdapterFunction.<init>(ClassPool)"})
  void testNewClassNameAdapterFunction_whenClassPool_thenReturnTransformFooTxtIsNull() {
    // Arrange, Act and Assert
    assertNull(new ClassNameAdapterFunction(new ClassPool()).transform("foo.txt"));
  }

  /**
   * Test {@link ClassNameAdapterFunction#transform(String)}.
   *
   * <ul>
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassNameAdapterFunction#transform(String)}
   */
  @Test
  @DisplayName("Test transform(String); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassNameAdapterFunction.transform(String)"})
  void testTransform_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(new ClassNameAdapterFunction(new ClassPool()).transform("foo.txt"));
  }
}
