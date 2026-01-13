package proguard.optimize;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.MemberVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;

class SideEffectVisitorMarkerFactoryDiffblueTest {
  /**
   * Test {@link SideEffectVisitorMarkerFactory#createMemberVisitor(MemberVisitor)}.
   *
   * <p>Method under test: {@link SideEffectVisitorMarkerFactory#createMemberVisitor(MemberVisitor)}
   */
  @Test
  @DisplayName("Test createMemberVisitor(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MemberVisitor SideEffectVisitorMarkerFactory.createMemberVisitor(MemberVisitor)"
  })
  void testCreateMemberVisitor() {
    // Arrange
    SideEffectVisitorMarkerFactory sideEffectVisitorMarkerFactory =
        new SideEffectVisitorMarkerFactory(true);

    // Act and Assert
    assertTrue(
        sideEffectVisitorMarkerFactory.createMemberVisitor(new KotlinAnnotationCounter())
            instanceof OptimizationInfoMemberFilter);
  }
}
