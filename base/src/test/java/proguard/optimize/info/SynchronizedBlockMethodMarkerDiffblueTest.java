package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.SimpleInstruction;

class SynchronizedBlockMethodMarkerDiffblueTest {
  /**
   * Test {@link SynchronizedBlockMethodMarker#visitSimpleInstruction(Clazz, Method, CodeAttribute,
   * int, SimpleInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setHasSynchronizedBlock()}.
   * </ul>
   *
   * <p>Method under test: {@link SynchronizedBlockMethodMarker#visitSimpleInstruction(Clazz,
   * Method, CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); then calls setHasSynchronizedBlock()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SynchronizedBlockMethodMarker.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_thenCallsSetHasSynchronizedBlock() {
    // Arrange
    SynchronizedBlockMethodMarker synchronizedBlockMethodMarker =
        new SynchronizedBlockMethodMarker();
    LibraryClass clazz = new LibraryClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setHasSynchronizedBlock();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    synchronizedBlockMethodMarker.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -62));

    // Assert
    verify(programMethodOptimizationInfo).setHasSynchronizedBlock();
  }

  /**
   * Test {@link SynchronizedBlockMethodMarker#hasSynchronizedBlock(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SynchronizedBlockMethodMarker#hasSynchronizedBlock(Method)}
   */
  @Test
  @DisplayName(
      "Test hasSynchronizedBlock(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SynchronizedBlockMethodMarker.hasSynchronizedBlock(Method)"})
  void testHasSynchronizedBlock_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(SynchronizedBlockMethodMarker.hasSynchronizedBlock(method));
  }
}
