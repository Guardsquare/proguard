package proguard.classfile.pass;

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
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;

class PrimitiveArrayConstantIntroducerDiffblueTest {
  /**
   * Test {@link PrimitiveArrayConstantIntroducer#execute(AppView)}.
   *
   * <ul>
   *   <li>When {@link ClassPool} {@link ClassPool#classesAccept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link PrimitiveArrayConstantIntroducer#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); when ClassPool classesAccept(ClassVisitor) does nothing; then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PrimitiveArrayConstantIntroducer.execute(AppView)"})
  void testExecute_whenClassPoolClassesAcceptDoesNothing_thenCallsClassesAccept() {
    // Arrange
    PrimitiveArrayConstantIntroducer primitiveArrayConstantIntroducer =
        new PrimitiveArrayConstantIntroducer();

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    primitiveArrayConstantIntroducer.execute(
        new AppView(programClassPool, KotlinConstants.dummyClassPool));

    // Assert
    verify(programClassPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test new {@link PrimitiveArrayConstantIntroducer} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link
   * PrimitiveArrayConstantIntroducer}
   */
  @Test
  @DisplayName("Test new PrimitiveArrayConstantIntroducer (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PrimitiveArrayConstantIntroducer.<init>()"})
  void testNewPrimitiveArrayConstantIntroducer() {
    // Arrange, Act and Assert
    assertEquals(
        "proguard.classfile.pass.PrimitiveArrayConstantIntroducer",
        new PrimitiveArrayConstantIntroducer().getName());
  }
}
