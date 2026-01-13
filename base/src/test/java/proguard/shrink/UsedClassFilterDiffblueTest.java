package proguard.shrink;

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
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;

class UsedClassFilterDiffblueTest {
  /**
   * Test {@link UsedClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassVisitor#visitLibraryClass(LibraryClass)}.
   * </ul>
   *
   * <p>Method under test: {@link UsedClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz); then calls visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_thenCallsVisitLibraryClass() {
    // Arrange
    ClassVisitor unusedClassVisitor = mock(ClassVisitor.class);
    doNothing().when(unusedClassVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    UsedClassFilter usedClassFilter =
        new UsedClassFilter(new SimpleUsageMarker(), mock(ClassVisitor.class), unusedClassVisitor);

    // Act
    usedClassFilter.visitAnyClass(new LibraryClass());

    // Assert
    verify(unusedClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }
}
