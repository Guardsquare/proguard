package proguard.obfuscate.kotlin;

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
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinTypeAliasMetadata;
import proguard.classfile.kotlin.visitor.KotlinTypeAliasVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeVisitor;
import proguard.obfuscate.NumericNameFactory;

class KotlinAliasNameObfuscatorDiffblueTest {
  /**
   * Test {@link KotlinAliasNameObfuscator#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinClassKindMetadata#typeAliasesAccept(Clazz,
   *       KotlinTypeAliasVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * KotlinAliasNameObfuscator#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata); then calls typeAliasesAccept(Clazz, KotlinTypeAliasVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAliasNameObfuscator.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testVisitKotlinDeclarationContainerMetadata_thenCallsTypeAliasesAccept() {
    // Arrange
    KotlinAliasNameObfuscator kotlinAliasNameObfuscator =
        new KotlinAliasNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();

    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        mock(KotlinClassKindMetadata.class);
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .typeAliasesAccept(Mockito.<Clazz>any(), Mockito.<KotlinTypeAliasVisitor>any());

    // Act
    kotlinAliasNameObfuscator.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    verify(kotlinDeclarationContainerMetadata)
        .typeAliasesAccept(isA(Clazz.class), isA(KotlinTypeAliasVisitor.class));
  }

  /**
   * Test {@link KotlinAliasNameObfuscator#visitTypeAlias(Clazz, KotlinDeclarationContainerMetadata,
   * KotlinTypeAliasMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinTypeAliasMetadata#expandedTypeAccept(Clazz,
   *       KotlinDeclarationContainerMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinAliasNameObfuscator#visitTypeAlias(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitTypeAlias(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata); then calls expandedTypeAccept(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAliasNameObfuscator.visitTypeAlias(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata)"
  })
  void testVisitTypeAlias_thenCallsExpandedTypeAccept() {
    // Arrange
    KotlinAliasNameObfuscator kotlinAliasNameObfuscator =
        new KotlinAliasNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");

    KotlinTypeAliasMetadata kotlinTypeAliasMetadata = mock(KotlinTypeAliasMetadata.class);
    doNothing()
        .when(kotlinTypeAliasMetadata)
        .expandedTypeAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinDeclarationContainerMetadata>any(),
            Mockito.<KotlinTypeVisitor>any());

    // Act
    kotlinAliasNameObfuscator.visitTypeAlias(
        clazz, kotlinDeclarationContainerMetadata, kotlinTypeAliasMetadata);

    // Assert
    verify(kotlinTypeAliasMetadata)
        .expandedTypeAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinTypeVisitor.class));
  }
}
