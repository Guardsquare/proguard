package proguard.optimize.info;

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
import proguard.classfile.ClassPool;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassPoolVisitor;

class RepeatedClassPoolVisitorDiffblueTest {
  /**
   * Test {@link RepeatedClassPoolVisitor#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassPoolVisitor#visitClassPool(ClassPool)}.
   * </ul>
   *
   * <p>Method under test: {@link RepeatedClassPoolVisitor#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName("Test visitClassPool(ClassPool); then calls visitClassPool(ClassPool)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RepeatedClassPoolVisitor.visitClassPool(ClassPool)"})
  void testVisitClassPool_thenCallsVisitClassPool() {
    // Arrange
    ClassPoolVisitor classPoolVisitor = mock(ClassPoolVisitor.class);
    doNothing().when(classPoolVisitor).visitClassPool(Mockito.<ClassPool>any());
    RepeatedClassPoolVisitor repeatedClassPoolVisitor =
        new RepeatedClassPoolVisitor(new MutableBoolean(), classPoolVisitor);

    // Act
    repeatedClassPoolVisitor.visitClassPool(KotlinConstants.dummyClassPool);

    // Assert
    verify(classPoolVisitor).visitClassPool(isA(ClassPool.class));
  }
}
