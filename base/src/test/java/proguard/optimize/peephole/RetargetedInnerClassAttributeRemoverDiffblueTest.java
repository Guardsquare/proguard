package proguard.optimize.peephole;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
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
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.constant.visitor.ConstantVisitor;

class RetargetedInnerClassAttributeRemoverDiffblueTest {
  /**
   * Test {@link RetargetedInnerClassAttributeRemover#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link InnerClassesInfo#innerClassConstantAccept(Clazz, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * RetargetedInnerClassAttributeRemover#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute); then calls innerClassConstantAccept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void RetargetedInnerClassAttributeRemover.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"
  })
  void testVisitInnerClassesAttribute_thenCallsInnerClassConstantAccept() {
    // Arrange
    RetargetedInnerClassAttributeRemover retargetedInnerClassAttributeRemover =
        new RetargetedInnerClassAttributeRemover();

    InnerClassesInfo innerClassesInfo = mock(InnerClassesInfo.class);
    doNothing()
        .when(innerClassesInfo)
        .innerClassConstantAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());
    doNothing()
        .when(innerClassesInfo)
        .outerClassConstantAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());
    InnerClassesInfo[] classes = new InnerClassesInfo[] {innerClassesInfo};

    // Act
    retargetedInnerClassAttributeRemover.visitInnerClassesAttribute(
        null, new InnerClassesAttribute(1, 1, classes));

    // Assert
    verify(innerClassesInfo, atLeast(1))
        .innerClassConstantAccept(isNull(), isA(ConstantVisitor.class));
    verify(innerClassesInfo, atLeast(1))
        .outerClassConstantAccept(isNull(), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link RetargetedInnerClassAttributeRemover#visitInnerClassesInfo(Clazz,
   * InnerClassesInfo)}.
   *
   * <ul>
   *   <li>Then calls {@link InnerClassesInfo#innerClassConstantAccept(Clazz, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedInnerClassAttributeRemover#visitInnerClassesInfo(Clazz,
   * InnerClassesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesInfo(Clazz, InnerClassesInfo); then calls innerClassConstantAccept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void RetargetedInnerClassAttributeRemover.visitInnerClassesInfo(Clazz, InnerClassesInfo)"
  })
  void testVisitInnerClassesInfo_thenCallsInnerClassConstantAccept() {
    // Arrange
    RetargetedInnerClassAttributeRemover retargetedInnerClassAttributeRemover =
        new RetargetedInnerClassAttributeRemover();
    LibraryClass clazz = new LibraryClass();

    InnerClassesInfo innerClassesInfo = mock(InnerClassesInfo.class);
    doNothing()
        .when(innerClassesInfo)
        .innerClassConstantAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());
    doNothing()
        .when(innerClassesInfo)
        .outerClassConstantAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());

    // Act
    retargetedInnerClassAttributeRemover.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert
    verify(innerClassesInfo).innerClassConstantAccept(isA(Clazz.class), isA(ConstantVisitor.class));
    verify(innerClassesInfo).outerClassConstantAccept(isA(Clazz.class), isA(ConstantVisitor.class));
  }
}
