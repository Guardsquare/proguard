package proguard.optimize.gson;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.testutils.cpa.NamedClass;

@ExtendWith(MockitoExtension.class)
class GsonDomainClassFinderDiffblueTest {
  @Mock private ClassPool classPool;

  @InjectMocks private GsonDomainClassFinder gsonDomainClassFinder;

  /**
   * Test {@link GsonDomainClassFinder#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassPool} {@link ClassPool#getClass(String)} return {@link
   *       LibraryClass#LibraryClass()}.
   *   <li>Then calls {@link ClassPool#getClass(String)}.
   * </ul>
   *
   * <p>Method under test: {@link GsonDomainClassFinder#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassPool getClass(String) return LibraryClass(); then calls getClass(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GsonDomainClassFinder.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassPoolGetClassReturnLibraryClass_thenCallsGetClass() {
    // Arrange
    when(classPool.getClass(Mockito.<String>any())).thenReturn(new LibraryClass());

    NamedClass programClass = new NamedClass("Member Name");
    programClass.addSubClass(new LibraryClass());

    // Act
    gsonDomainClassFinder.visitProgramClass(programClass);

    // Assert
    verify(classPool).getClass("Member Name");
  }
}
