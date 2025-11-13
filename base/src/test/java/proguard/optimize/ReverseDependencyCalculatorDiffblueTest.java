package proguard.optimize;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ClassPool;
import proguard.classfile.visitor.ClassVisitor;

class ReverseDependencyCalculatorDiffblueTest {
  /**
   * Test {@link ReverseDependencyCalculator#reverseDependencyStore()}.
   *
   * <ul>
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ReverseDependencyCalculator#reverseDependencyStore()}
   */
  @Test
  @DisplayName("Test reverseDependencyStore(); then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "proguard.optimize.ReverseDependencyStore ReverseDependencyCalculator.reverseDependencyStore()"
  })
  void testReverseDependencyStore_thenCallsClassesAccept() {
    // Arrange
    ClassPool classPool = mock(ClassPool.class);
    doNothing().when(classPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    new ReverseDependencyCalculator(classPool).reverseDependencyStore();

    // Assert
    verify(classPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }
}
