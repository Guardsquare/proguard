package proguard.obfuscate;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LineNumberInfo;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.MemberVisitor;

class MappingPrinterDiffblueTest {
  /**
   * Test {@link MappingPrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   *   <li>Then calls {@link ProgramClass#attributesAccept(AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MappingPrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given 'Processing Info'; then calls attributesAccept(AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MappingPrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenProcessingInfo_thenCallsAttributesAccept() {
    // Arrange
    MappingPrinter mappingPrinter = new MappingPrinter(new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).attributesAccept(Mockito.<AttributeVisitor>any());
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());
    when(programClass.getProcessingInfo()).thenReturn("Processing Info");
    when(programClass.getName()).thenReturn("Name");

    // Act
    mappingPrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass).attributesAccept(isA(AttributeVisitor.class));
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
    verify(programClass).getName();
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link MappingPrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#attributesAccept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MappingPrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls attributesAccept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MappingPrinter.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsAttributesAccept() {
    // Arrange
    MappingPrinter mappingPrinter = new MappingPrinter(new PrintWriter(new StringWriter()));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act
    mappingPrinter.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link MappingPrinter#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Given {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MappingPrinter#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); given 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MappingPrinter.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_givenNull() {
    // Arrange
    MappingPrinter mappingPrinter = new MappingPrinter(new PrintWriter(new StringWriter()));
    LibraryClass clazz = new LibraryClass();

    Method method = mock(Method.class);
    when(method.getProcessingInfo()).thenReturn(null);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute = new CodeAttribute();

    LineNumberTableAttribute lineNumberTableAttribute = mock(LineNumberTableAttribute.class);
    when(lineNumberTableAttribute.getLowestLineNumber()).thenReturn(0);
    when(lineNumberTableAttribute.getHighestLineNumber()).thenReturn(2);
    when(lineNumberTableAttribute.getSource(anyInt())).thenReturn("Source");

    // Act
    mappingPrinter.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    verify(method).getDescriptor(isA(Clazz.class));
    verify(method).getName(isA(Clazz.class));
    verify(lineNumberTableAttribute).getHighestLineNumber();
    verify(lineNumberTableAttribute).getLowestLineNumber();
    verify(lineNumberTableAttribute).getSource(0);
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link MappingPrinter#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Given zero.
   *   <li>Then calls {@link Method#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link MappingPrinter#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); given zero; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MappingPrinter.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_givenZero_thenCallsGetDescriptor() {
    // Arrange
    MappingPrinter mappingPrinter = new MappingPrinter(new PrintWriter(new StringWriter()));
    LibraryClass clazz = new LibraryClass();

    Method method = mock(Method.class);
    when(method.getProcessingInfo()).thenReturn("Processing Info");
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute = new CodeAttribute();

    LineNumberTableAttribute lineNumberTableAttribute = mock(LineNumberTableAttribute.class);
    when(lineNumberTableAttribute.getLowestLineNumber()).thenReturn(0);
    when(lineNumberTableAttribute.getHighestLineNumber()).thenReturn(2);
    when(lineNumberTableAttribute.getSource(anyInt())).thenReturn("Source");

    // Act
    mappingPrinter.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    verify(method).getDescriptor(isA(Clazz.class));
    verify(method).getName(isA(Clazz.class));
    verify(lineNumberTableAttribute).getHighestLineNumber();
    verify(lineNumberTableAttribute).getLowestLineNumber();
    verify(lineNumberTableAttribute).getSource(0);
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link MappingPrinter#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LineNumberInfo#getSource()}.
   * </ul>
   *
   * <p>Method under test: {@link MappingPrinter#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then calls getSource()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MappingPrinter.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenCallsGetSource() {
    // Arrange
    MappingPrinter mappingPrinter = new MappingPrinter(new PrintWriter(new StringWriter()));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LineNumberInfo lineNumberInfo = mock(LineNumberInfo.class);
    when(lineNumberInfo.getSource()).thenReturn("Source");
    LineNumberInfo[] lineNumberTable = new LineNumberInfo[] {lineNumberInfo};

    // Act
    mappingPrinter.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, new LineNumberTableAttribute(1, 1, lineNumberTable));

    // Assert
    verify(lineNumberInfo, atLeast(1)).getSource();
  }
}
