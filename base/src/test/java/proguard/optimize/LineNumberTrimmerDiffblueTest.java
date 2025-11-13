package proguard.optimize;

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

class LineNumberTrimmerDiffblueTest {
  /**
   * Test {@link LineNumberTrimmer#execute(AppView)}.
   *
   * <ul>
   *   <li>When {@link ClassPool} {@link ClassPool#classesAccept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link LineNumberTrimmer#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); when ClassPool classesAccept(ClassVisitor) does nothing; then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineNumberTrimmer.execute(AppView)"})
  void testExecute_whenClassPoolClassesAcceptDoesNothing_thenCallsClassesAccept() {
    // Arrange
    LineNumberTrimmer lineNumberTrimmer = new LineNumberTrimmer();

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    lineNumberTrimmer.execute(new AppView(programClassPool, KotlinConstants.dummyClassPool));

    // Assert
    verify(programClassPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test new {@link LineNumberTrimmer} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link LineNumberTrimmer}
   */
  @Test
  @DisplayName("Test new LineNumberTrimmer (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineNumberTrimmer.<init>()"})
  void testNewLineNumberTrimmer() {
    // Arrange, Act and Assert
    assertEquals("proguard.optimize.LineNumberTrimmer", new LineNumberTrimmer().getName());
  }
}
