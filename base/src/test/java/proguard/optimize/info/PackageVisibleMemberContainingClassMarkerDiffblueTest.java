package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.Member;

class PackageVisibleMemberContainingClassMarkerDiffblueTest {
  /**
   * Test {@link PackageVisibleMemberContainingClassMarker#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link PackageVisibleMemberContainingClassMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PackageVisibleMemberContainingClassMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass() {
    // Arrange
    PackageVisibleMemberContainingClassMarker packageVisibleMemberContainingClassMarker =
        new PackageVisibleMemberContainingClassMarker();

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    packageVisibleMemberContainingClassMarker.visitAnyClass(clazz);

    // Assert that nothing has changed
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertFalse(((ProgramClassOptimizationInfo) processingInfo).containsPackageVisibleMembers());
  }

  /**
   * Test {@link PackageVisibleMemberContainingClassMarker#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link PackageVisibleMemberContainingClassMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PackageVisibleMemberContainingClassMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass2() {
    // Arrange
    PackageVisibleMemberContainingClassMarker packageVisibleMemberContainingClassMarker =
        new PackageVisibleMemberContainingClassMarker();

    LibraryClass clazz = new LibraryClass(0, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    packageVisibleMemberContainingClassMarker.visitAnyClass(clazz);

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).containsPackageVisibleMembers());
  }

  /**
   * Test {@link PackageVisibleMemberContainingClassMarker#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link PackageVisibleMemberContainingClassMarker#visitAnyMember(Clazz,
   * Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void PackageVisibleMemberContainingClassMarker.visitAnyMember(Clazz, Member)"
  })
  void testVisitAnyMember() {
    // Arrange
    PackageVisibleMemberContainingClassMarker packageVisibleMemberContainingClassMarker =
        new PackageVisibleMemberContainingClassMarker();

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    packageVisibleMemberContainingClassMarker.visitAnyMember(clazz, new LibraryField());

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).containsPackageVisibleMembers());
  }

  /**
   * Test {@link PackageVisibleMemberContainingClassMarker#containsPackageVisibleMembers(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link
   * PackageVisibleMemberContainingClassMarker#containsPackageVisibleMembers(Clazz)}
   */
  @Test
  @DisplayName(
      "Test containsPackageVisibleMembers(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(Clazz)"
  })
  void testContainsPackageVisibleMembers_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertTrue(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(clazz));
  }

  /**
   * Test {@link PackageVisibleMemberContainingClassMarker#containsPackageVisibleMembers(Clazz)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link
   * PackageVisibleMemberContainingClassMarker#containsPackageVisibleMembers(Clazz)}
   */
  @Test
  @DisplayName("Test containsPackageVisibleMembers(Clazz); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(Clazz)"
  })
  void testContainsPackageVisibleMembers_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(PackageVisibleMemberContainingClassMarker.containsPackageVisibleMembers(clazz));
  }
}
