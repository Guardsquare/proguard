package proguard.optimize.evaluation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.VariableInstruction;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.evaluation.value.ArrayReferenceValue;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;
import proguard.resources.file.ResourceFile;

@ExtendWith(MockitoExtension.class)
class SimpleEnumUseSimplifierDiffblueTest {
  @Mock private Clazz clazz;

  @Mock private CodeAttribute codeAttribute;

  @Mock private Method method;

  @InjectMocks private SimpleEnumUseSimplifier simpleEnumUseSimplifier;

  /**
   * Test {@link SimpleEnumUseSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@link SimpleEnumUseSimplifier#SimpleEnumUseSimplifier()}.
   *   <li>Then calls {@link ClassOptimizationInfo#isSimpleEnum()}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given SimpleEnumUseSimplifier(); then calls isSimpleEnum()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_givenSimpleEnumUseSimplifier_thenCallsIsSimpleEnum() {
    // Arrange
    SimpleEnumUseSimplifier simpleEnumUseSimplifier = new SimpleEnumUseSimplifier();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.isSimpleEnum()).thenReturn(true);

    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo(classOptimizationInfo);
    LibraryMethod method = new LibraryMethod();

    // Act
    simpleEnumUseSimplifier.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(classOptimizationInfo).isSimpleEnum();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>Given {@link Clazz} {@link Clazz#getString(int)} return empty string.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitSimpleInstruction(Clazz, Method,
   * CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); given Clazz getString(int) return empty string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_givenClazzGetStringReturnEmptyString() {
    // Arrange
    when(clazz.getString(anyInt())).thenReturn("");
    Clazz[] referencedClasses = new Clazz[] {clazz};
    ProgramMethod method = new ProgramMethod(1, 1, 1, referencedClasses);

    // Act
    simpleEnumUseSimplifier.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -80));

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>Given {@link Clazz} {@link Clazz#getString(int)} return {@code String}.
   *   <li>Then calls {@link Clazz#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitSimpleInstruction(Clazz, Method,
   * CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); given Clazz getString(int) return 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_givenClazzGetStringReturnString_thenCallsGetString() {
    // Arrange
    when(clazz.getString(anyInt())).thenReturn("String");
    Clazz[] referencedClasses = new Clazz[] {clazz};
    ProgramMethod method = new ProgramMethod(1, 1, 1, referencedClasses);

    // Act
    simpleEnumUseSimplifier.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -80));

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitVariableInstruction(Clazz, Method, CodeAttribute, int,
   * VariableInstruction)}.
   *
   * <ul>
   *   <li>Given {@link PartialEvaluator} {@link PartialEvaluator#isSubroutineStart(int)} return
   *       {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitVariableInstruction(Clazz, Method,
   * CodeAttribute, int, VariableInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction); given PartialEvaluator isSubroutineStart(int) return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction)"
  })
  void testVisitVariableInstruction_givenPartialEvaluatorIsSubroutineStartReturnTrue() {
    // Arrange
    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    when(partialEvaluator.isSubroutineStart(anyInt())).thenReturn(true);
    SimpleEnumUseSimplifier simpleEnumUseSimplifier =
        new SimpleEnumUseSimplifier(partialEvaluator, new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    simpleEnumUseSimplifier.visitVariableInstruction(
        clazz, method, codeAttribute, 2, new VariableInstruction((byte) ':'));

    // Assert
    verify(partialEvaluator).isSubroutineStart(2);
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitVariableInstruction(Clazz, Method, CodeAttribute, int,
   * VariableInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link PartialEvaluator#getStackBefore(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitVariableInstruction(Clazz, Method,
   * CodeAttribute, int, VariableInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction); then calls getStackBefore(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction)"
  })
  void testVisitVariableInstruction_thenCallsGetStackBefore() {
    // Arrange
    ArrayReferenceValue arrayReferenceValue = mock(ArrayReferenceValue.class);
    when(arrayReferenceValue.getReferencedClass()).thenReturn(null);

    TracedStack tracedStack = mock(TracedStack.class);
    when(tracedStack.getTop(anyInt())).thenReturn(arrayReferenceValue);

    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    when(partialEvaluator.isSubroutineStart(anyInt())).thenReturn(false);
    when(partialEvaluator.getStackBefore(anyInt())).thenReturn(tracedStack);
    SimpleEnumUseSimplifier simpleEnumUseSimplifier =
        new SimpleEnumUseSimplifier(partialEvaluator, new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    simpleEnumUseSimplifier.visitVariableInstruction(
        clazz, method, codeAttribute, 2, new VariableInstruction((byte) ':'));

    // Assert
    verify(partialEvaluator).getStackBefore(2);
    verify(partialEvaluator).isSubroutineStart(2);
    verify(tracedStack).getTop(0);
    verify(arrayReferenceValue).getReferencedClass();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction() {
    // Arrange
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    // Act
    simpleEnumUseSimplifier.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction((byte) -77, 1));

    // Assert
    verify(clazz).constantPoolEntryAccept(eq(1), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName("Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction() {
    // Arrange
    ArrayReferenceValue arrayReferenceValue = mock(ArrayReferenceValue.class);
    when(arrayReferenceValue.getReferencedClass()).thenReturn(null);

    TracedStack tracedStack = mock(TracedStack.class);
    when(tracedStack.getTop(anyInt())).thenReturn(arrayReferenceValue);

    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    when(partialEvaluator.getStackBefore(anyInt())).thenReturn(tracedStack);
    SimpleEnumUseSimplifier simpleEnumUseSimplifier =
        new SimpleEnumUseSimplifier(partialEvaluator, new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    simpleEnumUseSimplifier.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction((byte) -90, 1));

    // Assert
    verify(partialEvaluator).getStackBefore(2);
    verify(tracedStack).getTop(0);
    verify(arrayReferenceValue).getReferencedClass();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName("Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction2() {
    // Arrange
    ArrayReferenceValue arrayReferenceValue = mock(ArrayReferenceValue.class);
    when(arrayReferenceValue.getReferencedClass()).thenReturn(null);

    TracedStack tracedStack = mock(TracedStack.class);
    when(tracedStack.getTop(anyInt())).thenReturn(arrayReferenceValue);

    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    when(partialEvaluator.getStackBefore(anyInt())).thenReturn(tracedStack);
    SimpleEnumUseSimplifier simpleEnumUseSimplifier =
        new SimpleEnumUseSimplifier(partialEvaluator, new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    simpleEnumUseSimplifier.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction((byte) -58, 1));

    // Assert
    verify(partialEvaluator).getStackBefore(2);
    verify(tracedStack).getTop(0);
    verify(arrayReferenceValue).getReferencedClass();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName("Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction3() {
    // Arrange
    ArrayReferenceValue arrayReferenceValue = mock(ArrayReferenceValue.class);
    when(arrayReferenceValue.getReferencedClass()).thenReturn(null);

    TracedStack tracedStack = mock(TracedStack.class);
    when(tracedStack.getTop(anyInt())).thenReturn(arrayReferenceValue);

    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    when(partialEvaluator.getStackBefore(anyInt())).thenReturn(tracedStack);
    SimpleEnumUseSimplifier simpleEnumUseSimplifier =
        new SimpleEnumUseSimplifier(partialEvaluator, new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    simpleEnumUseSimplifier.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction((byte) -57, 1));

    // Assert
    verify(partialEvaluator).getStackBefore(2);
    verify(tracedStack).getTop(0);
    verify(arrayReferenceValue).getReferencedClass();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link PartialEvaluator#getStackBefore(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); then calls getStackBefore(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_thenCallsGetStackBefore() {
    // Arrange
    ArrayReferenceValue arrayReferenceValue = mock(ArrayReferenceValue.class);
    when(arrayReferenceValue.getReferencedClass()).thenReturn(null);

    TracedStack tracedStack = mock(TracedStack.class);
    when(tracedStack.getTop(anyInt())).thenReturn(arrayReferenceValue);

    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    when(partialEvaluator.getStackBefore(anyInt())).thenReturn(tracedStack);
    SimpleEnumUseSimplifier simpleEnumUseSimplifier =
        new SimpleEnumUseSimplifier(partialEvaluator, new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    simpleEnumUseSimplifier.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction((byte) -91, 1));

    // Assert
    verify(partialEvaluator).getStackBefore(2);
    verify(tracedStack).getTop(0);
    verify(arrayReferenceValue).getReferencedClass();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link Clazz#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitStringConstant(Clazz,
   * StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant); then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SimpleEnumUseSimplifier.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsGetProcessingInfo() {
    // Arrange
    when(clazz.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = clazz;

    // Act
    simpleEnumUseSimplifier.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SimpleEnumUseSimplifier.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenClassOptimizationInfo() {
    // Arrange
    when(clazz.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());

    // Act
    simpleEnumUseSimplifier.visitClassConstant(clazz, new ClassConstant(1, clazz));

    // Assert
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor) SimpleEnum is {@code
   *       true}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ProgramClassOptimizationInfo (default constructor) SimpleEnum is 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SimpleEnumUseSimplifier.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenProgramClassOptimizationInfoSimpleEnumIsTrue() {
    // Arrange
    ProgramClassOptimizationInfo programClassOptimizationInfo = new ProgramClassOptimizationInfo();
    programClassOptimizationInfo.setSimpleEnum(true);
    when(clazz.getProcessingInfo()).thenReturn(programClassOptimizationInfo);

    // Act
    simpleEnumUseSimplifier.visitClassConstant(clazz, new ClassConstant(1, clazz));

    // Assert
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitParameter(Clazz, Member, int, int, int, int, String,
   * Clazz)}.
   *
   * <ul>
   *   <li>Given {@link Clazz} {@link Clazz#getProcessingInfo()} return {@link
   *       ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitParameter(Clazz, Member, int, int,
   * int, int, String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitParameter(Clazz, Member, int, int, int, int, String, Clazz); given Clazz getProcessingInfo() return ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitParameter(Clazz, Member, int, int, int, int, String, Clazz)"
  })
  void testVisitParameter_givenClazzGetProcessingInfoReturnClassOptimizationInfo() {
    // Arrange
    when(clazz.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());

    // Act
    simpleEnumUseSimplifier.visitParameter(
        clazz, new LibraryField(), 1, 3, 1, 3, "Parameter Type", clazz);

    // Assert
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link SimpleEnumUseSimplifier#visitParameter(Clazz, Member, int, int, int, int, String,
   * Clazz)}.
   *
   * <ul>
   *   <li>Given {@link Clazz} {@link Clazz#getProcessingInfo()} return {@link
   *       ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumUseSimplifier#visitParameter(Clazz, Member, int, int,
   * int, int, String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitParameter(Clazz, Member, int, int, int, int, String, Clazz); given Clazz getProcessingInfo() return ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumUseSimplifier.visitParameter(Clazz, Member, int, int, int, int, String, Clazz)"
  })
  void testVisitParameter_givenClazzGetProcessingInfoReturnProgramClassOptimizationInfo() {
    // Arrange
    when(clazz.getProcessingInfo()).thenReturn(new ProgramClassOptimizationInfo());

    // Act
    simpleEnumUseSimplifier.visitParameter(
        clazz, new LibraryField(), 1, 3, 1, 3, "Parameter Type", clazz);

    // Assert
    verify(clazz).getProcessingInfo();
  }
}
