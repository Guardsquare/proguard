package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.ClassRenamer;
import proguard.obfuscate.MemberNameCleaner;
import proguard.testutils.cpa.NamedMember;

class MethodFinalizerDiffblueTest {
  /**
   * Test {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodFinalizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    MethodFinalizer methodFinalizer = new MethodFinalizer();
    ProgramClass programClass = new ProgramClass();
    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");

    // Act
    methodFinalizer.visitProgramMethod(programClass, programMethod);

    // Assert
    assertEquals(Short.SIZE, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodFinalizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod2() {
    // Arrange
    MethodFinalizer methodFinalizer = new MethodFinalizer(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();
    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");

    // Act
    methodFinalizer.visitProgramMethod(programClass, programMethod);

    // Assert
    assertEquals(Short.SIZE, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodFinalizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod3() {
    // Arrange
    MethodFinalizer methodFinalizer = new MethodFinalizer();
    ProgramClass programClass = new ProgramClass();
    NamedMember programMethod = new NamedMember("<init>", "Descriptor");

    // Act
    methodFinalizer.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertEquals(0, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodFinalizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod4() {
    // Arrange
    MethodFinalizer methodFinalizer = new MethodFinalizer(new MemberNameCleaner());
    ProgramClass programClass = new ProgramClass();
    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");

    // Act
    methodFinalizer.visitProgramMethod(programClass, programMethod);

    // Assert
    assertEquals(Short.SIZE, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodFinalizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod5() {
    // Arrange
    MethodFinalizer methodFinalizer = new MethodFinalizer();

    ProgramClass programClass = new ProgramClass();
    LibraryClass clazz = new LibraryClass(1050, "<init>", "<init>");
    programClass.addSubClass(clazz);
    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");

    // Act
    methodFinalizer.visitProgramMethod(programClass, programMethod);

    // Assert
    assertEquals(Short.SIZE, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodFinalizer#MethodFinalizer(MemberVisitor)} with extraMemberVisitor is
   *       {@link ClassRenamer#ClassRenamer()}.
   * </ul>
   *
   * <p>Method under test: {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given MethodFinalizer(MemberVisitor) with extraMemberVisitor is ClassRenamer()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodFinalizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenMethodFinalizerWithExtraMemberVisitorIsClassRenamer() {
    // Arrange
    MethodFinalizer methodFinalizer = new MethodFinalizer(new ClassRenamer());
    ProgramClass programClass = new ProgramClass();
    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");

    // Act
    methodFinalizer.visitProgramMethod(programClass, programMethod);

    // Assert
    assertEquals(Short.SIZE, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given zero.
   * </ul>
   *
   * <p>Method under test: {@link MethodFinalizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod); given zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodFinalizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenZero() {
    // Arrange
    MethodFinalizer methodFinalizer = new MethodFinalizer();
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1050, 3, constantPool, 1050, 1050, 1050);
    programClass.subClassCount = 0;
    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");

    // Act
    methodFinalizer.visitProgramMethod(programClass, programMethod);

    // Assert
    assertEquals(Short.SIZE, programMethod.getAccessFlags());
  }
}
