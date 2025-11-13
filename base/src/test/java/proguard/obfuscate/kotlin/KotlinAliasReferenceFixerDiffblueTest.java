package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinTypeAliasMetadata;
import proguard.classfile.kotlin.KotlinTypeMetadata;
import proguard.classfile.kotlin.flags.KotlinTypeAliasFlags;
import proguard.classfile.kotlin.flags.KotlinTypeFlags;
import proguard.classfile.kotlin.flags.KotlinVisibilityFlags;

class KotlinAliasReferenceFixerDiffblueTest {
  /**
   * Test {@link KotlinAliasReferenceFixer#visitAnyType(Clazz, KotlinTypeMetadata)}.
   *
   * <p>Method under test: {@link KotlinAliasReferenceFixer#visitAnyType(Clazz, KotlinTypeMetadata)}
   */
  @Test
  @DisplayName("Test visitAnyType(Clazz, KotlinTypeMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinAliasReferenceFixer.visitAnyType(Clazz, KotlinTypeMetadata)"})
  void testVisitAnyType() {
    // Arrange
    KotlinAliasReferenceFixer kotlinAliasReferenceFixer = new KotlinAliasReferenceFixer();
    LibraryClass clazz = new LibraryClass();
    KotlinTypeFlags flags = new KotlinTypeFlags();
    flags.isDefinitelyNonNull = true;
    flags.isNullable = true;
    flags.isSuspend = true;
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinTypeAliasFlags flags2 = new KotlinTypeAliasFlags(visibility);
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinClassKindMetadata.k = 1;
    KotlinTypeAliasMetadata kotlinTypeAliasMetadata = new KotlinTypeAliasMetadata(flags2, "Name");
    kotlinTypeAliasMetadata.referencedDeclarationContainer = kotlinClassKindMetadata;
    KotlinTypeMetadata kotlinTypeMetadata = new KotlinTypeMetadata(flags);
    kotlinTypeMetadata.aliasName = "Kotlin Type Metadata";
    kotlinTypeMetadata.referencedTypeAlias = kotlinTypeAliasMetadata;

    // Act
    kotlinAliasReferenceFixer.visitAnyType(clazz, kotlinTypeMetadata);

    // Assert
    assertEquals("null.Name", kotlinTypeMetadata.aliasName);
  }

  /**
   * Test {@link KotlinAliasReferenceFixer#visitAnyType(Clazz, KotlinTypeMetadata)}.
   *
   * <ul>
   *   <li>When starProjection.
   *   <li>Then starProjection {@link KotlinTypeMetadata#aliasName} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinAliasReferenceFixer#visitAnyType(Clazz, KotlinTypeMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyType(Clazz, KotlinTypeMetadata); when starProjection; then starProjection aliasName is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinAliasReferenceFixer.visitAnyType(Clazz, KotlinTypeMetadata)"})
  void testVisitAnyType_whenStarProjection_thenStarProjectionAliasNameIsNull() {
    // Arrange
    KotlinAliasReferenceFixer kotlinAliasReferenceFixer = new KotlinAliasReferenceFixer();
    LibraryClass clazz = new LibraryClass();
    KotlinTypeMetadata kotlinTypeMetadata = KotlinTypeMetadata.starProjection();

    // Act
    kotlinAliasReferenceFixer.visitAnyType(clazz, kotlinTypeMetadata);

    // Assert that nothing has changed
    assertNull(kotlinTypeMetadata.aliasName);
  }
}
