package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.attribute.visitor.AttributeVisitor;

class LocalOrAnonymousClassCheckerDiffblueTest {
  /**
   * Test {@link LocalOrAnonymousClassChecker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#attributesAccept(AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link LocalOrAnonymousClassChecker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then calls attributesAccept(AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LocalOrAnonymousClassChecker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsAttributesAccept() {
    // Arrange
    LocalOrAnonymousClassChecker localOrAnonymousClassChecker = new LocalOrAnonymousClassChecker();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).attributesAccept(Mockito.<AttributeVisitor>any());

    // Act
    localOrAnonymousClassChecker.visitProgramClass(programClass);

    // Assert
    verify(programClass).attributesAccept(isA(AttributeVisitor.class));
  }

  /**
   * Test {@link LocalOrAnonymousClassChecker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <ul>
   *   <li>Then {@link LocalOrAnonymousClassChecker} (default constructor) LocalOrAnonymous.
   * </ul>
   *
   * <p>Method under test: {@link LocalOrAnonymousClassChecker#visitInnerClassesInfo(Clazz,
   * InnerClassesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesInfo(Clazz, InnerClassesInfo); then LocalOrAnonymousClassChecker (default constructor) LocalOrAnonymous")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalOrAnonymousClassChecker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"
  })
  void testVisitInnerClassesInfo_thenLocalOrAnonymousClassCheckerLocalOrAnonymous() {
    // Arrange
    LocalOrAnonymousClassChecker localOrAnonymousClassChecker = new LocalOrAnonymousClassChecker();
    ProgramClass clazz = new ProgramClass();

    InnerClassesInfo innerClassesInfo = mock(InnerClassesInfo.class);
    doNothing().when(innerClassesInfo).addProcessingFlags((int[]) Mockito.any());
    innerClassesInfo.addProcessingFlags(2, 1, 2, 1);

    // Act
    localOrAnonymousClassChecker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert
    verify(innerClassesInfo).addProcessingFlags((int[]) Mockito.any());
    assertTrue(localOrAnonymousClassChecker.isLocalOrAnonymous());
  }

  /**
   * Test {@link LocalOrAnonymousClassChecker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <ul>
   *   <li>Then not {@link LocalOrAnonymousClassChecker} (default constructor) LocalOrAnonymous.
   * </ul>
   *
   * <p>Method under test: {@link LocalOrAnonymousClassChecker#visitInnerClassesInfo(Clazz,
   * InnerClassesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesInfo(Clazz, InnerClassesInfo); then not LocalOrAnonymousClassChecker (default constructor) LocalOrAnonymous")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalOrAnonymousClassChecker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"
  })
  void testVisitInnerClassesInfo_thenNotLocalOrAnonymousClassCheckerLocalOrAnonymous() {
    // Arrange
    LocalOrAnonymousClassChecker localOrAnonymousClassChecker = new LocalOrAnonymousClassChecker();
    ProgramClass clazz = new ProgramClass();
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo(1, 0, 0, 1);

    // Act
    localOrAnonymousClassChecker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert that nothing has changed
    assertFalse(localOrAnonymousClassChecker.isLocalOrAnonymous());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link LocalOrAnonymousClassChecker}
   *   <li>{@link LocalOrAnonymousClassChecker#visitAnyClass(Clazz)}
   *   <li>{@link LocalOrAnonymousClassChecker#isLocalOrAnonymous()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalOrAnonymousClassChecker.<init>()",
    "boolean LocalOrAnonymousClassChecker.isLocalOrAnonymous()",
    "void LocalOrAnonymousClassChecker.visitAnyClass(Clazz)"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    LocalOrAnonymousClassChecker actualLocalOrAnonymousClassChecker =
        new LocalOrAnonymousClassChecker();
    actualLocalOrAnonymousClassChecker.visitAnyClass(new LibraryClass());

    // Assert
    assertFalse(actualLocalOrAnonymousClassChecker.isLocalOrAnonymous());
  }
}
