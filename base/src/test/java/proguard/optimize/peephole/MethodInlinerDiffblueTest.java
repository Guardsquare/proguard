package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.ExtendedLineNumberInfo;
import proguard.classfile.attribute.LineNumberInfo;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.ExceptionInfoVisitor;
import proguard.classfile.attribute.visitor.LineNumberInfoVisitor;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.testutils.cpa.NamedClass;

class MethodInlinerDiffblueTest {
  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute() throws UnsupportedEncodingException {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 3, "AXAXAXAX".getBytes("UTF-8"));

    // Act
    shortMethodInliner.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute2() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getProcessingFlags()).thenThrow(new IllegalArgumentException());
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .exceptionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<ExceptionInfoVisitor>any());
    doNothing()
        .when(codeAttribute)
        .instructionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<InstructionVisitor>any());

    // Act
    shortMethodInliner.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(codeAttribute).accept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .exceptionsAccept(isA(Clazz.class), isA(Method.class), isA(ExceptionInfoVisitor.class));
    verify(codeAttribute)
        .instructionsAccept(isA(Clazz.class), isA(Method.class), isA(InstructionVisitor.class));
    verify(method).getProcessingFlags();
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute3() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getProcessingFlags()).thenReturn(1);
    doNothing().when(method).accept(Mockito.<ProgramClass>any(), Mockito.<MemberVisitor>any());
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .exceptionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<ExceptionInfoVisitor>any());
    doNothing()
        .when(codeAttribute)
        .instructionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<InstructionVisitor>any());

    // Act
    shortMethodInliner.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getName(isA(Clazz.class));
    verify(method).accept(isA(ProgramClass.class), isA(MemberVisitor.class));
    verify(codeAttribute).accept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .exceptionsAccept(isA(Clazz.class), isA(Method.class), isA(ExceptionInfoVisitor.class));
    verify(codeAttribute)
        .instructionsAccept(isA(Clazz.class), isA(Method.class), isA(InstructionVisitor.class));
    verify(method).getProcessingFlags();
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@code Descriptor}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute); given 'Descriptor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_givenDescriptor() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    shortMethodInliner.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@code Descriptor}.
   *   <li>When {@code null}.
   *   <li>Then calls {@link ProgramMethod#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given 'Descriptor'; when 'null'; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_givenDescriptor_whenNull_thenCallsGetDescriptor() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    shortMethodInliner.visitCodeAttribute(clazz, method, null);

    // Assert
    verify(method).getDescriptor(isA(Clazz.class));
    verify(method).getName(isA(Clazz.class));
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@code <init>}.
   *   <li>When {@link ProgramMethod} {@link ProgramMethod#getName(Clazz)} return {@code <init>}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given '<init>'; when ProgramMethod getName(Clazz) return '<init>'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_givenInit_whenProgramMethodGetNameReturnInit() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getProcessingFlags()).thenReturn(1);
    doNothing().when(method).accept(Mockito.<ProgramClass>any(), Mockito.<MemberVisitor>any());
    when(method.getName(Mockito.<Clazz>any())).thenReturn("<init>");

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .exceptionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<ExceptionInfoVisitor>any());
    doNothing()
        .when(codeAttribute)
        .instructionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<InstructionVisitor>any());

    // Act
    shortMethodInliner.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getName(isA(Clazz.class));
    verify(method).accept(isA(ProgramClass.class), isA(MemberVisitor.class));
    verify(codeAttribute).accept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .exceptionsAccept(isA(Clazz.class), isA(Method.class), isA(ExceptionInfoVisitor.class));
    verify(codeAttribute)
        .instructionsAccept(isA(Clazz.class), isA(Method.class), isA(InstructionVisitor.class));
    verify(method).getProcessingFlags();
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then calls {@link ProgramMethod#accept(ProgramClass, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given one; then calls accept(ProgramClass, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_givenOne_thenCallsAccept() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getProcessingFlags()).thenReturn(1);
    doNothing().when(method).accept(Mockito.<ProgramClass>any(), Mockito.<MemberVisitor>any());
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .exceptionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<ExceptionInfoVisitor>any());
    doNothing()
        .when(codeAttribute)
        .instructionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<InstructionVisitor>any());

    // Act
    shortMethodInliner.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getName(isA(Clazz.class));
    verify(method).accept(isA(ProgramClass.class), isA(MemberVisitor.class));
    verify(codeAttribute).accept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .exceptionsAccept(isA(Clazz.class), isA(Method.class), isA(ExceptionInfoVisitor.class));
    verify(codeAttribute)
        .instructionsAccept(isA(Clazz.class), isA(Method.class), isA(InstructionVisitor.class));
    verify(method).getProcessingFlags();
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenThrowIllegalArgumentException() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenThrow(new IllegalArgumentException());
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> shortMethodInliner.visitCodeAttribute(clazz, method, new CodeAttribute()));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
  }

  /**
   * Test {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenThrowIllegalArgumentException2()
      throws UnsupportedEncodingException {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    NamedClass clazz = new NamedClass("Unexpected error while inlining method:");

    ProgramMethod method = mock(ProgramMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenThrow(new IllegalArgumentException());
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 3, "AXAXAXAX".getBytes("UTF-8"));

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> shortMethodInliner.visitCodeAttribute(clazz, method, codeAttribute));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
  }

  /**
   * Test {@link MethodInliner#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link ExtendedLineNumberInfo#getSource()}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then calls getSource()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodInliner.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenCallsGetSource() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    ExtendedLineNumberInfo extendedLineNumberInfo = mock(ExtendedLineNumberInfo.class);
    when(extendedLineNumberInfo.getSource()).thenThrow(new IllegalArgumentException());
    LineNumberInfo[] lineNumberTable = new LineNumberInfo[] {extendedLineNumberInfo};

    // Act
    shortMethodInliner.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, new LineNumberTableAttribute(1, 1, lineNumberTable));

    // Assert
    verify(extendedLineNumberInfo).getSource();
  }

  /**
   * Test {@link MethodInliner#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LineNumberTableAttribute#lineNumbersAccept(Clazz, Method,
   *       CodeAttribute, LineNumberInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then calls lineNumbersAccept(Clazz, Method, CodeAttribute, LineNumberInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodInliner.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenCallsLineNumbersAccept() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
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
    shortMethodInliner.visitLineNumberTableAttribute(
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
   * Test {@link MethodInliner#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link InterfaceMethodrefConstant#referencedMethodAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); then calls referencedMethodAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"})
  void testVisitAnyMethodrefConstant_thenCallsReferencedMethodAccept() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    LibraryClass clazz = new LibraryClass();

    InterfaceMethodrefConstant anyMethodrefConstant = mock(InterfaceMethodrefConstant.class);
    doNothing().when(anyMethodrefConstant).referencedMethodAccept(Mockito.<MemberVisitor>any());

    // Act
    shortMethodInliner.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

    // Assert
    verify(anyMethodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link MethodInliner#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod} {@link LibraryMethod#accept(Clazz, MemberVisitor)} does
   *       nothing.
   *   <li>Then calls {@link LibraryMethod#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); when LibraryMethod accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"})
  void testVisitAnyMethodrefConstant_whenLibraryMethodAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod referencedMethod = mock(LibraryMethod.class);
    doNothing().when(referencedMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    shortMethodInliner.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, new LibraryClass(), referencedMethod));

    // Assert
    verify(referencedMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
  }

  /**
   * Test {@link MethodInliner#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>Then calls {@link LibraryMethod#getName(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); given 'Name'; then calls getName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInliner.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_givenName_thenCallsGetName() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = mock(LibraryMethod.class);
    when(libraryMethod.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    shortMethodInliner.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(libraryMethod).getName(isA(Clazz.class));
  }

  /**
   * Test {@link MethodInliner#visitLineNumberInfo(Clazz, Method, CodeAttribute, LineNumberInfo)}.
   *
   * <ul>
   *   <li>Given {@link IllegalArgumentException#IllegalArgumentException()}.
   *   <li>Then calls {@link ExtendedLineNumberInfo#getSource()}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInliner#visitLineNumberInfo(Clazz, Method, CodeAttribute,
   * LineNumberInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberInfo(Clazz, Method, CodeAttribute, LineNumberInfo); given IllegalArgumentException(); then calls getSource()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodInliner.visitLineNumberInfo(Clazz, Method, CodeAttribute, LineNumberInfo)"
  })
  void testVisitLineNumberInfo_givenIllegalArgumentException_thenCallsGetSource() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    ExtendedLineNumberInfo lineNumberInfo = mock(ExtendedLineNumberInfo.class);
    when(lineNumberInfo.getSource()).thenThrow(new IllegalArgumentException());

    // Act
    shortMethodInliner.visitLineNumberInfo(clazz, method, codeAttribute, lineNumberInfo);

    // Assert
    verify(lineNumberInfo).getSource();
  }
}
