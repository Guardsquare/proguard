package proguard.shrink;

import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.Processable;

class UsagePrinterDiffblueTest {
  /**
   * Test {@link UsagePrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ShortestUsageMarker} {@link ShortestUsageMarker#isUsed(Processable)} return
   *       {@code false}.
   *   <li>Then calls {@link ProgramClass#getName()}.
   * </ul>
   *
   * <p>Method under test: {@link UsagePrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ShortestUsageMarker isUsed(Processable) return 'false'; then calls getName()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsagePrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenShortestUsageMarkerIsUsedReturnFalse_thenCallsGetName() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    UsagePrinter usagePrinter =
        new UsagePrinter(usageMarker, true, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    usagePrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass).getName();
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsagePrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#fieldsAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link UsagePrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls fieldsAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsagePrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsFieldsAccept() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    UsagePrinter usagePrinter =
        new UsagePrinter(usageMarker, true, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());

    // Act
    usagePrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
    verify(programClass).getName();
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsagePrinter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#getName()}.
   * </ul>
   *
   * <p>Method under test: {@link UsagePrinter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls getName()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsagePrinter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsGetName() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    UsagePrinter usagePrinter =
        new UsagePrinter(usageMarker, false, new PrintWriter(new StringWriter()));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    usagePrinter.visitProgramClass(programClass);

    // Assert
    verify(programClass).getName();
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsagePrinter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link ShortestUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link UsagePrinter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField); then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsagePrinter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenCallsIsUsed() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    UsagePrinter usagePrinter =
        new UsagePrinter(usageMarker, true, new PrintWriter(new StringWriter()));
    ProgramClass programClass = new ProgramClass();

    // Act
    usagePrinter.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ShortestUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link UsagePrinter#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsagePrinter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsIsUsed() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    UsagePrinter usagePrinter =
        new UsagePrinter(usageMarker, true, new PrintWriter(new StringWriter()));
    ProgramClass programClass = new ProgramClass();

    // Act
    usagePrinter.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsagePrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#attributesAccept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link UsagePrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls attributesAccept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsagePrinter.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsAttributesAccept() {
    // Arrange
    SimpleUsageMarker usageMarker = new SimpleUsageMarker();
    UsagePrinter usagePrinter =
        new UsagePrinter(usageMarker, true, new PrintWriter(new StringWriter()));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act
    usagePrinter.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }
}
