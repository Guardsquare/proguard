package proguard.backport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyBoolean;
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
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.visitor.ClassCleaner;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberAccessFilter;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.MemberNameCleaner;

class DefaultInterfaceMethodConverterDiffblueTest {
  /**
   * Test {@link DefaultInterfaceMethodConverter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass} {@link ProgramClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link ProgramClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); when ProgramClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DefaultInterfaceMethodConverter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_whenProgramClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(modifiedClassVisitor, new KotlinAnnotationCounter());

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).accept(Mockito.<ClassVisitor>any());
    doNothing()
        .when(programClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());

    // Act
    defaultInterfaceMethodConverter.visitProgramClass(programClass);

    // Assert
    verify(programClass).accept(isA(ClassVisitor.class));
    verify(programClass)
        .hierarchyAccept(eq(false), eq(false), eq(false), eq(true), isA(ClassVisitor.class));
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(modifiedClassVisitor, new KotlinAnnotationCounter());
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute2() {
    // Arrange
    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(mock(ClassVisitor.class), null);
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute3() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(modifiedClassVisitor, new MemberNameCleaner());
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute4() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(modifiedClassVisitor, new ClassCleaner());
    defaultInterfaceMethodConverter.visitProgramClass(new ProgramClass());
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute5() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(
            modifiedClassVisitor, new MemberAccessFilter(1, 1, new KotlinAnnotationCounter()));
    defaultInterfaceMethodConverter.visitProgramClass(new ProgramClass());
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute6() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(
            modifiedClassVisitor, new MemberAccessFilter(1, 1024, new KotlinAnnotationCounter()));
    defaultInterfaceMethodConverter.visitProgramClass(new ProgramClass());
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute7() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(
            modifiedClassVisitor, new MemberAccessFilter(1024, 1, new KotlinAnnotationCounter()));
    defaultInterfaceMethodConverter.visitProgramClass(new ProgramClass());
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }

  /**
   * Test {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link DefaultInterfaceMethodConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultInterfaceMethodConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute8() {
    // Arrange
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    DefaultInterfaceMethodConverter defaultInterfaceMethodConverter =
        new DefaultInterfaceMethodConverter(
            modifiedClassVisitor, new MemberAccessFilter(-1, 1, new KotlinAnnotationCounter()));
    defaultInterfaceMethodConverter.visitProgramClass(new ProgramClass());
    ProgramClass clazz = new ProgramClass();
    ProgramMethod method = new ProgramMethod();

    // Act
    defaultInterfaceMethodConverter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    assertEquals(1024, method.getAccessFlags());
  }
}
