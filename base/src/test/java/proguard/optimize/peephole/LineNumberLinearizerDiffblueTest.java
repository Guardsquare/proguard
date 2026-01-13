package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import proguard.AppView;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LineNumberInfoVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

class LineNumberLinearizerDiffblueTest {
  /**
   * Test {@link LineNumberLinearizer#execute(AppView)}.
   *
   * <ul>
   *   <li>When {@link ClassPool} {@link ClassPool#classesAccept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link LineNumberLinearizer#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); when ClassPool classesAccept(ClassVisitor) does nothing; then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineNumberLinearizer.execute(AppView)"})
  void testExecute_whenClassPoolClassesAcceptDoesNothing_thenCallsClassesAccept() {
    // Arrange
    LineNumberLinearizer lineNumberLinearizer = new LineNumberLinearizer();

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    lineNumberLinearizer.execute(new AppView(programClassPool, KotlinConstants.dummyClassPool));

    // Assert
    verify(programClassPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link LineNumberLinearizer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#methodsAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link LineNumberLinearizer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls methodsAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineNumberLinearizer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsMethodsAccept() {
    // Arrange
    LineNumberLinearizer lineNumberLinearizer = new LineNumberLinearizer();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());

    // Act
    lineNumberLinearizer.visitProgramClass(programClass);

    // Assert
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link LineNumberLinearizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#accept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link LineNumberLinearizer#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls accept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineNumberLinearizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsAccept() {
    // Arrange
    LineNumberLinearizer lineNumberLinearizer = new LineNumberLinearizer();
    ProgramClass programClass = new ProgramClass();

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    Attribute[] attributes = new Attribute[] {annotationDefaultAttribute};
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, 1, attributes, referencedClasses);

    // Act
    lineNumberLinearizer.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(annotationDefaultAttribute)
        .accept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link LineNumberLinearizer#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#attributesAccept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link LineNumberLinearizer#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls attributesAccept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineNumberLinearizer.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsAttributesAccept() {
    // Arrange
    LineNumberLinearizer lineNumberLinearizer = new LineNumberLinearizer();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act
    lineNumberLinearizer.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link LineNumberLinearizer#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LineNumberTableAttribute#lineNumbersAccept(Clazz, Method,
   *       CodeAttribute, LineNumberInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link LineNumberLinearizer#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then calls lineNumbersAccept(Clazz, Method, CodeAttribute, LineNumberInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LineNumberLinearizer.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenCallsLineNumbersAccept() {
    // Arrange
    LineNumberLinearizer lineNumberLinearizer = new LineNumberLinearizer();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LineNumberTableAttribute lineNumberTableAttribute = mock(LineNumberTableAttribute.class);
    doNothing()
        .when(lineNumberTableAttribute)
        .lineNumbersAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<LineNumberInfoVisitor>any());

    // Act
    lineNumberLinearizer.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    verify(lineNumberTableAttribute)
        .lineNumbersAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(LineNumberInfoVisitor.class));
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link LineNumberLinearizer}
   *   <li>{@link LineNumberLinearizer#visitAnyAttribute(Clazz, Attribute)}
   *   <li>{@link LineNumberLinearizer#visitAnyClass(Clazz)}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LineNumberLinearizer.<init>()",
    "void LineNumberLinearizer.visitAnyAttribute(Clazz, Attribute)",
    "void LineNumberLinearizer.visitAnyClass(Clazz)"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    LineNumberLinearizer actualLineNumberLinearizer = new LineNumberLinearizer();
    LibraryClass clazz = new LibraryClass();
    actualLineNumberLinearizer.visitAnyAttribute(clazz, new BootstrapMethodsAttribute());
    actualLineNumberLinearizer.visitAnyClass(new LibraryClass());

    // Assert
    assertEquals(
        "proguard.optimize.peephole.LineNumberLinearizer", actualLineNumberLinearizer.getName());
  }
}
