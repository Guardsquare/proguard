package proguard.shrink;

import static org.mockito.ArgumentMatchers.isA;
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
import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.classfile.visitor.ClassVisitor;
import proguard.resources.file.ResourceFilePool;

class UsageMarkerDiffblueTest {
  /**
   * Test {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker)} with
   * {@code programClassPool}, {@code libraryClassPool}, {@code resourceFilePool}, {@code
   * simpleUsageMarker}.
   *
   * <p>Method under test: {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool,
   * SimpleUsageMarker)}
   */
  @Test
  @DisplayName(
      "Test mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker) with 'programClassPool', 'libraryClassPool', 'resourceFilePool', 'simpleUsageMarker'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UsageMarker.mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker)"
  })
  void testMarkWithProgramClassPoolLibraryClassPoolResourceFilePoolSimpleUsageMarker() {
    // Arrange
    UsageMarker usageMarker = new UsageMarker(mock(Configuration.class));
    ClassPool programClassPool = new ClassPool();

    ClassPool libraryClassPool = mock(ClassPool.class);
    doNothing().when(libraryClassPool).classesAccept(Mockito.<ClassVisitor>any());
    ResourceFilePool resourceFilePool = new ResourceFilePool();

    // Act
    usageMarker.mark(programClassPool, libraryClassPool, resourceFilePool, new SimpleUsageMarker());

    // Assert
    verify(libraryClassPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker)} with
   * {@code programClassPool}, {@code libraryClassPool}, {@code resourceFilePool}, {@code
   * simpleUsageMarker}.
   *
   * <p>Method under test: {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool,
   * SimpleUsageMarker)}
   */
  @Test
  @DisplayName(
      "Test mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker) with 'programClassPool', 'libraryClassPool', 'resourceFilePool', 'simpleUsageMarker'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UsageMarker.mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker)"
  })
  void testMarkWithProgramClassPoolLibraryClassPoolResourceFilePoolSimpleUsageMarker2() {
    // Arrange
    UsageMarker usageMarker = new UsageMarker(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = mock(ClassPool.class);
    doNothing().when(libraryClassPool).classesAccept(Mockito.<ClassVisitor>any());
    ResourceFilePool resourceFilePool = new ResourceFilePool();

    // Act
    usageMarker.mark(programClassPool, libraryClassPool, resourceFilePool, new SimpleUsageMarker());

    // Assert
    verify(libraryClassPool).classesAccept(isA(ClassVisitor.class));
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker,
   * ClassUsageMarker)} with {@code programClassPool}, {@code libraryClassPool}, {@code
   * resourceFilePool}, {@code simpleUsageMarker}, {@code classUsageMarker}.
   *
   * <p>Method under test: {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool,
   * SimpleUsageMarker, ClassUsageMarker)}
   */
  @Test
  @DisplayName(
      "Test mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker, ClassUsageMarker) with 'programClassPool', 'libraryClassPool', 'resourceFilePool', 'simpleUsageMarker', 'classUsageMarker'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UsageMarker.mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker, ClassUsageMarker)"
  })
  void
      testMarkWithProgramClassPoolLibraryClassPoolResourceFilePoolSimpleUsageMarkerClassUsageMarker() {
    // Arrange
    UsageMarker usageMarker = new UsageMarker(mock(Configuration.class));
    ClassPool programClassPool = new ClassPool();

    ClassPool libraryClassPool = mock(ClassPool.class);
    doNothing().when(libraryClassPool).classesAccept(Mockito.<ClassVisitor>any());
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    SimpleUsageMarker simpleUsageMarker = new SimpleUsageMarker();

    // Act
    usageMarker.mark(
        programClassPool,
        libraryClassPool,
        resourceFilePool,
        simpleUsageMarker,
        new ClassUsageMarker());

    // Assert
    verify(libraryClassPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker,
   * ClassUsageMarker)} with {@code programClassPool}, {@code libraryClassPool}, {@code
   * resourceFilePool}, {@code simpleUsageMarker}, {@code classUsageMarker}.
   *
   * <p>Method under test: {@link UsageMarker#mark(ClassPool, ClassPool, ResourceFilePool,
   * SimpleUsageMarker, ClassUsageMarker)}
   */
  @Test
  @DisplayName(
      "Test mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker, ClassUsageMarker) with 'programClassPool', 'libraryClassPool', 'resourceFilePool', 'simpleUsageMarker', 'classUsageMarker'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UsageMarker.mark(ClassPool, ClassPool, ResourceFilePool, SimpleUsageMarker, ClassUsageMarker)"
  })
  void
      testMarkWithProgramClassPoolLibraryClassPoolResourceFilePoolSimpleUsageMarkerClassUsageMarker2() {
    // Arrange
    UsageMarker usageMarker = new UsageMarker(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = mock(ClassPool.class);
    doNothing().when(libraryClassPool).classesAccept(Mockito.<ClassVisitor>any());
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    SimpleUsageMarker simpleUsageMarker = new SimpleUsageMarker();

    // Act
    usageMarker.mark(
        programClassPool,
        libraryClassPool,
        resourceFilePool,
        simpleUsageMarker,
        new ClassUsageMarker());

    // Assert
    verify(libraryClassPool).classesAccept(isA(ClassVisitor.class));
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }
}
