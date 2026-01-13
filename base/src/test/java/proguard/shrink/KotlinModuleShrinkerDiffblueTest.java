package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.resources.kotlinmodule.KotlinModule;
import proguard.resources.kotlinmodule.KotlinModulePackage;
import proguard.resources.kotlinmodule.visitor.KotlinModulePackageVisitor;
import proguard.util.Processable;

class KotlinModuleShrinkerDiffblueTest {
  /**
   * Test {@link KotlinModuleShrinker#visitKotlinModule(KotlinModule)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinModule#modulePackagesAccept(KotlinModulePackageVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinModuleShrinker#visitKotlinModule(KotlinModule)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinModule(KotlinModule); then calls modulePackagesAccept(KotlinModulePackageVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinModuleShrinker.visitKotlinModule(KotlinModule)"})
  void testVisitKotlinModule_thenCallsModulePackagesAccept() {
    // Arrange
    KotlinModuleShrinker kotlinModuleShrinker = new KotlinModuleShrinker(new SimpleUsageMarker());

    KotlinModule kotlinModule = mock(KotlinModule.class);
    doNothing().when(kotlinModule).modulePackagesAccept(Mockito.<KotlinModulePackageVisitor>any());

    // Act
    kotlinModuleShrinker.visitKotlinModule(kotlinModule);

    // Assert
    verify(kotlinModule).modulePackagesAccept(isA(KotlinModulePackageVisitor.class));
  }

  /**
   * Test {@link KotlinModuleShrinker#visitKotlinModulePackage(KotlinModule, KotlinModulePackage)}.
   *
   * <p>Method under test: {@link KotlinModuleShrinker#visitKotlinModulePackage(KotlinModule,
   * KotlinModulePackage)}
   */
  @Test
  @DisplayName("Test visitKotlinModulePackage(KotlinModule, KotlinModulePackage)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinModuleShrinker.visitKotlinModulePackage(KotlinModule, KotlinModulePackage)"
  })
  void testVisitKotlinModulePackage() {
    // Arrange
    KotlinModuleShrinker kotlinModuleShrinker = new KotlinModuleShrinker(new SimpleUsageMarker());
    ArrayList<String> fileFacadeNames = new ArrayList<>();
    KotlinModulePackage kotlinModulePart =
        new KotlinModulePackage("Fq Name", fileFacadeNames, new HashMap<>());

    // Act
    kotlinModuleShrinker.visitKotlinModulePackage(null, kotlinModulePart);

    // Assert that nothing has changed
    assertTrue(kotlinModulePart.fileFacadeNames.isEmpty());
    assertTrue(kotlinModulePart.referencedFileFacades.isEmpty());
  }

  /**
   * Test {@link KotlinModuleShrinker#visitKotlinModulePackage(KotlinModule, KotlinModulePackage)}.
   *
   * <p>Method under test: {@link KotlinModuleShrinker#visitKotlinModulePackage(KotlinModule,
   * KotlinModulePackage)}
   */
  @Test
  @DisplayName("Test visitKotlinModulePackage(KotlinModule, KotlinModulePackage)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinModuleShrinker.visitKotlinModulePackage(KotlinModule, KotlinModulePackage)"
  })
  void testVisitKotlinModulePackage2() {
    // Arrange
    SimpleUsageMarker usageMarker = mock(SimpleUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinModuleShrinker kotlinModuleShrinker = new KotlinModuleShrinker(usageMarker);

    ArrayList<String> fileFacadeNames = new ArrayList<>();
    fileFacadeNames.add("42");
    KotlinModulePackage kotlinModulePart =
        new KotlinModulePackage("Fq Name", fileFacadeNames, new HashMap<>());

    // Act
    kotlinModuleShrinker.visitKotlinModulePackage(null, kotlinModulePart);

    // Assert that nothing has changed
    verify(usageMarker).isUsed(isNull());
    assertEquals(1, kotlinModulePart.fileFacadeNames.size());
    assertEquals(1, kotlinModulePart.referencedFileFacades.size());
  }

  /**
   * Test {@link KotlinModuleShrinker#visitKotlinModulePackage(KotlinModule, KotlinModulePackage)}.
   *
   * <ul>
   *   <li>Given {@link SimpleUsageMarker} {@link SimpleUsageMarker#isUsed(Processable)} return
   *       {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinModuleShrinker#visitKotlinModulePackage(KotlinModule,
   * KotlinModulePackage)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinModulePackage(KotlinModule, KotlinModulePackage); given SimpleUsageMarker isUsed(Processable) return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinModuleShrinker.visitKotlinModulePackage(KotlinModule, KotlinModulePackage)"
  })
  void testVisitKotlinModulePackage_givenSimpleUsageMarkerIsUsedReturnFalse() {
    // Arrange
    SimpleUsageMarker usageMarker = mock(SimpleUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    KotlinModuleShrinker kotlinModuleShrinker = new KotlinModuleShrinker(usageMarker);

    ArrayList<String> fileFacadeNames = new ArrayList<>();
    fileFacadeNames.add("42");
    KotlinModulePackage kotlinModulePart =
        new KotlinModulePackage("Fq Name", fileFacadeNames, new HashMap<>());

    // Act
    kotlinModuleShrinker.visitKotlinModulePackage(null, kotlinModulePart);

    // Assert
    verify(usageMarker).isUsed(isNull());
    assertTrue(kotlinModulePart.fileFacadeNames.isEmpty());
    assertTrue(kotlinModulePart.referencedFileFacades.isEmpty());
  }
}
