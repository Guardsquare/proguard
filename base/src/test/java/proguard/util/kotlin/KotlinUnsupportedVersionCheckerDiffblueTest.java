package proguard.util.kotlin;

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

class KotlinUnsupportedVersionCheckerDiffblueTest {
  /**
   * Test {@link KotlinUnsupportedVersionChecker#execute(AppView)}.
   *
   * <p>Method under test: {@link KotlinUnsupportedVersionChecker#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinUnsupportedVersionChecker.execute(AppView)"})
  void testExecute() throws Exception {
    // Arrange
    KotlinUnsupportedVersionChecker kotlinUnsupportedVersionChecker =
        new KotlinUnsupportedVersionChecker();

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    kotlinUnsupportedVersionChecker.execute(
        new AppView(programClassPool, KotlinConstants.dummyClassPool));

    // Assert
    verify(programClassPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link KotlinUnsupportedVersionChecker#execute(AppView)}.
   *
   * <p>Method under test: {@link KotlinUnsupportedVersionChecker#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinUnsupportedVersionChecker.execute(AppView)"})
  void testExecute2() throws Exception {
    // Arrange
    KotlinUnsupportedVersionChecker kotlinUnsupportedVersionChecker =
        new KotlinUnsupportedVersionChecker();

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = mock(ClassPool.class);
    doNothing().when(libraryClassPool).classesAccept(Mockito.<ClassVisitor>any());

    AppView appView = new AppView(programClassPool, libraryClassPool);

    // Act
    kotlinUnsupportedVersionChecker.execute(appView);

    // Assert
    verify(programClassPool).classesAccept(isA(ClassVisitor.class));
    verify(libraryClassPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test new {@link KotlinUnsupportedVersionChecker} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link
   * KotlinUnsupportedVersionChecker}
   */
  @Test
  @DisplayName("Test new KotlinUnsupportedVersionChecker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinUnsupportedVersionChecker.<init>()"})
  void testNewKotlinUnsupportedVersionChecker() {
    // Arrange, Act and Assert
    assertEquals(
        "proguard.util.kotlin.KotlinUnsupportedVersionChecker",
        new KotlinUnsupportedVersionChecker().getName());
  }
}
