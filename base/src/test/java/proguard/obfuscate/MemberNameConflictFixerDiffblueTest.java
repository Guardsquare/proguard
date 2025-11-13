package proguard.obfuscate;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.util.WarningPrinter;

class MemberNameConflictFixerDiffblueTest {
  /**
   * Test {@link MemberNameConflictFixer#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameConflictFixer.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(false, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    memberNameConflictFixer.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass, atLeast(1)).getString(0);
  }

  /**
   * Test {@link MemberNameConflictFixer#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code false}.
   *   <li>Then calls {@link ProgramField#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given 'false'; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameConflictFixer.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenFalse_thenCallsGetDescriptor() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(true, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.extendsOrImplements(Mockito.<String>any())).thenReturn(false);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getProcessingInfo()).thenReturn("Processing Info");
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    memberNameConflictFixer.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).extendsOrImplements("java/lang/annotation/Annotation");
    verify(programField).getDescriptor(isA(Clazz.class));
    verify(programField).getName(isA(Clazz.class));
    verify(programField, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link MemberNameConflictFixer#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code true}.
   *   <li>When {@link ProgramClass} {@link ProgramClass#extendsOrImplements(String)} return {@code
   *       true}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given 'true'; when ProgramClass extendsOrImplements(String) return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameConflictFixer.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenTrue_whenProgramClassExtendsOrImplementsReturnTrue() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(true, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.extendsOrImplements(Mockito.<String>any())).thenReturn(true);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    memberNameConflictFixer.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).extendsOrImplements("java/lang/annotation/Annotation");
    verify(programClass, atLeast(1)).getString(0);
  }

  /**
   * Test {@link MemberNameConflictFixer#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code true}.
   *   <li>When {@link ProgramField#ProgramField()} ProcessingInfo is {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given 'true'; when ProgramField() ProcessingInfo is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameConflictFixer.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenTrue_whenProgramFieldProcessingInfoIsProcessingInfo() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(true, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.extendsOrImplements(Mockito.<String>any())).thenReturn(true);
    when(programClass.getString(anyInt())).thenReturn("String");

    ProgramField programField = new ProgramField();
    programField.setProcessingInfo("Processing Info");

    // Act
    memberNameConflictFixer.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).extendsOrImplements("java/lang/annotation/Annotation");
    verify(programClass, atLeast(1)).getString(0);
  }

  /**
   * Test {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberNameConflictFixer.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(false, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    memberNameConflictFixer.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass, atLeast(1)).getString(0);
  }

  /**
   * Test {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code false}.
   *   <li>Then calls {@link ProgramMethod#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'false'; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberNameConflictFixer.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenFalse_thenCallsGetDescriptor() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(true, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.extendsOrImplements(Mockito.<String>any())).thenReturn(false);

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getProcessingInfo()).thenReturn("Processing Info");
    when(programMethod.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(programMethod.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    memberNameConflictFixer.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).extendsOrImplements("java/lang/annotation/Annotation");
    verify(programMethod).getDescriptor(isA(Clazz.class));
    verify(programMethod, atLeast(1)).getName(isA(Clazz.class));
    verify(programMethod, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code true}.
   *   <li>When {@link ProgramClass} {@link ProgramClass#extendsOrImplements(String)} return {@code
   *       true}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'true'; when ProgramClass extendsOrImplements(String) return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberNameConflictFixer.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenTrue_whenProgramClassExtendsOrImplementsReturnTrue() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(true, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.extendsOrImplements(Mockito.<String>any())).thenReturn(true);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    memberNameConflictFixer.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass).extendsOrImplements("java/lang/annotation/Annotation");
    verify(programClass, atLeast(1)).getString(0);
  }

  /**
   * Test {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code true}.
   *   <li>When {@link ProgramMethod#ProgramMethod()} ProcessingInfo is {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameConflictFixer#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'true'; when ProgramMethod() ProcessingInfo is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberNameConflictFixer.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenTrue_whenProgramMethodProcessingInfoIsProcessingInfo() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());

    MemberNameConflictFixer memberNameConflictFixer =
        new MemberNameConflictFixer(true, descriptorMap, warningPrinter, memberObfuscator);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.extendsOrImplements(Mockito.<String>any())).thenReturn(true);
    when(programClass.getString(anyInt())).thenReturn("String");

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo("Processing Info");

    // Act
    memberNameConflictFixer.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).extendsOrImplements("java/lang/annotation/Annotation");
    verify(programClass, atLeast(1)).getString(0);
  }
}
