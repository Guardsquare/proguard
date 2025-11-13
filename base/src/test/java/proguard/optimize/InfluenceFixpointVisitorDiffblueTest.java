package proguard.optimize;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.InfluenceFixpointVisitor.MemberVisitorFactory;

class InfluenceFixpointVisitorDiffblueTest {
  /**
   * Test {@link InfluenceFixpointVisitor#InfluenceFixpointVisitor(MemberVisitorFactory)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * InfluenceFixpointVisitor#InfluenceFixpointVisitor(MemberVisitorFactory)}
   */
  @Test
  @DisplayName(
      "Test new InfluenceFixpointVisitor(MemberVisitorFactory); then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InfluenceFixpointVisitor.<init>(MemberVisitorFactory)"})
  void testNewInfluenceFixpointVisitor_thenCallsClassesAccept() {
    // Arrange and Act
    InfluenceFixpointVisitor actualInfluenceFixpointVisitor =
        new InfluenceFixpointVisitor(mock(MemberVisitorFactory.class));
    ClassPool classPool = mock(ClassPool.class);
    doNothing().when(classPool).classesAccept(Mockito.<ClassVisitor>any());
    actualInfluenceFixpointVisitor.visitClassPool(classPool);

    // Assert
    verify(classPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link InfluenceFixpointVisitor#InfluenceFixpointVisitor(MemberVisitorFactory)}.
   *
   * <ul>
   *   <li>Then {@link ClassPool#ClassPool()} size is one.
   * </ul>
   *
   * <p>Method under test: {@link
   * InfluenceFixpointVisitor#InfluenceFixpointVisitor(MemberVisitorFactory)}
   */
  @Test
  @DisplayName(
      "Test new InfluenceFixpointVisitor(MemberVisitorFactory); then ClassPool() size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InfluenceFixpointVisitor.<init>(MemberVisitorFactory)"})
  void testNewInfluenceFixpointVisitor_thenClassPoolSizeIsOne() {
    // Arrange and Act
    InfluenceFixpointVisitor actualInfluenceFixpointVisitor =
        new InfluenceFixpointVisitor(mock(MemberVisitorFactory.class));
    ClassPool classPool = new ClassPool();
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    classPool.addClass("Calculating Reverse Dependencies................ took: %6d ms", clazz);
    actualInfluenceFixpointVisitor.visitClassPool(classPool);

    // Assert that nothing has changed
    assertEquals(1, classPool.size());
  }

  /**
   * Test {@link InfluenceFixpointVisitor#InfluenceFixpointVisitor(MemberVisitorFactory)}.
   *
   * <ul>
   *   <li>Then {@link ClassPool#ClassPool()} size is zero.
   * </ul>
   *
   * <p>Method under test: {@link
   * InfluenceFixpointVisitor#InfluenceFixpointVisitor(MemberVisitorFactory)}
   */
  @Test
  @DisplayName(
      "Test new InfluenceFixpointVisitor(MemberVisitorFactory); then ClassPool() size is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InfluenceFixpointVisitor.<init>(MemberVisitorFactory)"})
  void testNewInfluenceFixpointVisitor_thenClassPoolSizeIsZero() {
    // Arrange and Act
    InfluenceFixpointVisitor actualInfluenceFixpointVisitor =
        new InfluenceFixpointVisitor(mock(MemberVisitorFactory.class));
    ClassPool classPool = new ClassPool();
    actualInfluenceFixpointVisitor.visitClassPool(classPool);

    // Assert that nothing has changed
    assertEquals(0, classPool.size());
  }

  /**
   * Test {@link InfluenceFixpointVisitor#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>When {@link ClassPool} {@link ClassPool#classesAccept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link InfluenceFixpointVisitor#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test visitClassPool(ClassPool); when ClassPool classesAccept(ClassVisitor) does nothing; then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InfluenceFixpointVisitor.visitClassPool(ClassPool)"})
  void testVisitClassPool_whenClassPoolClassesAcceptDoesNothing_thenCallsClassesAccept() {
    // Arrange
    InfluenceFixpointVisitor influenceFixpointVisitor =
        new InfluenceFixpointVisitor(mock(MemberVisitorFactory.class));

    ClassPool classPool = mock(ClassPool.class);
    doNothing().when(classPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    influenceFixpointVisitor.visitClassPool(classPool);

    // Assert
    verify(classPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }
}
