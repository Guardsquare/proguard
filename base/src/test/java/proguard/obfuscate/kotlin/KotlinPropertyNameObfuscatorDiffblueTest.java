package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.flags.KotlinModalityFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyAccessorFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyFlags;
import proguard.classfile.kotlin.flags.KotlinVisibilityFlags;
import proguard.obfuscate.NumericNameFactory;

class KotlinPropertyNameObfuscatorDiffblueTest {
  /**
   * Test {@link KotlinPropertyNameObfuscator#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <p>Method under test: {@link
   * KotlinPropertyNameObfuscator#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinPropertyNameObfuscator.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testVisitKotlinDeclarationContainerMetadata() {
    // Arrange
    KotlinPropertyNameObfuscator kotlinPropertyNameObfuscator =
        new KotlinPropertyNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinModalityFlags modality = new KotlinModalityFlags();
    modality.isAbstract = true;
    modality.isFinal = true;
    modality.isOpen = true;
    modality.isSealed = true;

    KotlinPropertyFlags flags = new KotlinPropertyFlags(visibility, modality);
    KotlinVisibilityFlags visibility2 = new KotlinVisibilityFlags();
    visibility2.isInternal = true;
    visibility2.isLocal = true;
    visibility2.isPrivate = true;
    visibility2.isPrivateToThis = true;
    visibility2.isProtected = true;
    visibility2.isPublic = true;
    KotlinModalityFlags modality2 = new KotlinModalityFlags();
    modality2.isAbstract = true;
    modality2.isFinal = true;
    modality2.isOpen = true;
    modality2.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags =
        new KotlinPropertyAccessorFlags(visibility2, modality2);
    KotlinVisibilityFlags visibility3 = new KotlinVisibilityFlags();
    visibility3.isInternal = true;
    visibility3.isLocal = true;
    visibility3.isPrivate = true;
    visibility3.isPrivateToThis = true;
    visibility3.isProtected = true;
    visibility3.isPublic = true;
    KotlinModalityFlags modality3 = new KotlinModalityFlags();
    modality3.isAbstract = true;
    modality3.isFinal = true;
    modality3.isOpen = true;
    modality3.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags =
        new KotlinPropertyAccessorFlags(visibility3, modality3);

    KotlinPropertyMetadata kotlinPropertyMetadata =
        new KotlinPropertyMetadata(flags, "Name", getterFlags, setterFlags);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList = new ArrayList<>();
    kotlinPropertyMetadataList.add(kotlinPropertyMetadata);
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinDeclarationContainerMetadata.properties = kotlinPropertyMetadataList;

    // Act
    kotlinPropertyNameObfuscator.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    List<KotlinPropertyMetadata> kotlinPropertyMetadataList2 =
        kotlinDeclarationContainerMetadata.properties;
    assertEquals(1, kotlinPropertyMetadataList2.size());
    assertEquals("1", kotlinPropertyMetadataList2.get(0).getProcessingInfo());
  }

  /**
   * Test {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
   *
   * <p>Method under test: {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinPropertyNameObfuscator.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty() {
    // Arrange
    KotlinPropertyNameObfuscator kotlinPropertyNameObfuscator =
        new KotlinPropertyNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinModalityFlags modality = new KotlinModalityFlags();
    modality.isAbstract = true;
    modality.isFinal = true;
    modality.isOpen = true;
    modality.isSealed = true;

    KotlinPropertyFlags flags = new KotlinPropertyFlags(visibility, modality);
    KotlinVisibilityFlags visibility2 = new KotlinVisibilityFlags();
    visibility2.isInternal = true;
    visibility2.isLocal = true;
    visibility2.isPrivate = true;
    visibility2.isPrivateToThis = true;
    visibility2.isProtected = true;
    visibility2.isPublic = true;
    KotlinModalityFlags modality2 = new KotlinModalityFlags();
    modality2.isAbstract = true;
    modality2.isFinal = true;
    modality2.isOpen = true;
    modality2.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags =
        new KotlinPropertyAccessorFlags(visibility2, modality2);
    KotlinVisibilityFlags visibility3 = new KotlinVisibilityFlags();
    visibility3.isInternal = true;
    visibility3.isLocal = true;
    visibility3.isPrivate = true;
    visibility3.isPrivateToThis = true;
    visibility3.isProtected = true;
    visibility3.isPublic = true;
    KotlinModalityFlags modality3 = new KotlinModalityFlags();
    modality3.isAbstract = true;
    modality3.isFinal = true;
    modality3.isOpen = true;
    modality3.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags =
        new KotlinPropertyAccessorFlags(visibility3, modality3);

    KotlinPropertyMetadata kotlinPropertyMetadata =
        new KotlinPropertyMetadata(flags, "Name", getterFlags, setterFlags);

    // Act
    kotlinPropertyNameObfuscator.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert
    assertEquals("1", kotlinPropertyMetadata.getProcessingInfo());
  }

  /**
   * Test {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
   *
   * <p>Method under test: {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinPropertyNameObfuscator.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty2() {
    // Arrange
    KotlinPropertyNameObfuscator kotlinPropertyNameObfuscator =
        new KotlinPropertyNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinModalityFlags modality = new KotlinModalityFlags();
    modality.isAbstract = true;
    modality.isFinal = true;
    modality.isOpen = true;
    modality.isSealed = true;

    KotlinPropertyFlags flags = new KotlinPropertyFlags(visibility, modality);
    KotlinVisibilityFlags visibility2 = new KotlinVisibilityFlags();
    visibility2.isInternal = true;
    visibility2.isLocal = true;
    visibility2.isPrivate = true;
    visibility2.isPrivateToThis = true;
    visibility2.isProtected = true;
    visibility2.isPublic = true;
    KotlinModalityFlags modality2 = new KotlinModalityFlags();
    modality2.isAbstract = true;
    modality2.isFinal = true;
    modality2.isOpen = true;
    modality2.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags =
        new KotlinPropertyAccessorFlags(visibility2, modality2);
    KotlinVisibilityFlags visibility3 = new KotlinVisibilityFlags();
    visibility3.isInternal = true;
    visibility3.isLocal = true;
    visibility3.isPrivate = true;
    visibility3.isPrivateToThis = true;
    visibility3.isProtected = true;
    visibility3.isPublic = true;
    KotlinModalityFlags modality3 = new KotlinModalityFlags();
    modality3.isAbstract = true;
    modality3.isFinal = true;
    modality3.isOpen = true;
    modality3.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags =
        new KotlinPropertyAccessorFlags(visibility3, modality3);

    KotlinPropertyMetadata kotlinPropertyMetadata =
        new KotlinPropertyMetadata(flags, "Name", getterFlags, setterFlags);
    kotlinPropertyMetadata.referencedBackingField = new LibraryField();
    kotlinPropertyMetadata.referencedGetterMethod = new LibraryMethod();
    kotlinPropertyMetadata.referencedSetterMethod = new LibraryMethod();

    // Act
    kotlinPropertyNameObfuscator.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert
    assertEquals("1", kotlinPropertyMetadata.getProcessingInfo());
  }

  /**
   * Test {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
   *
   * <ul>
   *   <li>Given {@link LibraryField#LibraryField()} addProcessingFlags two and {@code 4194304}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata); given LibraryField() addProcessingFlags two and '4194304'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinPropertyNameObfuscator.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty_givenLibraryFieldAddProcessingFlagsTwoAnd4194304() {
    // Arrange
    KotlinPropertyNameObfuscator kotlinPropertyNameObfuscator =
        new KotlinPropertyNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinModalityFlags modality = new KotlinModalityFlags();
    modality.isAbstract = true;
    modality.isFinal = true;
    modality.isOpen = true;
    modality.isSealed = true;

    KotlinPropertyFlags flags = new KotlinPropertyFlags(visibility, modality);
    KotlinVisibilityFlags visibility2 = new KotlinVisibilityFlags();
    visibility2.isInternal = true;
    visibility2.isLocal = true;
    visibility2.isPrivate = true;
    visibility2.isPrivateToThis = true;
    visibility2.isProtected = true;
    visibility2.isPublic = true;
    KotlinModalityFlags modality2 = new KotlinModalityFlags();
    modality2.isAbstract = true;
    modality2.isFinal = true;
    modality2.isOpen = true;
    modality2.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags =
        new KotlinPropertyAccessorFlags(visibility2, modality2);
    KotlinVisibilityFlags visibility3 = new KotlinVisibilityFlags();
    visibility3.isInternal = true;
    visibility3.isLocal = true;
    visibility3.isPrivate = true;
    visibility3.isPrivateToThis = true;
    visibility3.isProtected = true;
    visibility3.isPublic = true;
    KotlinModalityFlags modality3 = new KotlinModalityFlags();
    modality3.isAbstract = true;
    modality3.isFinal = true;
    modality3.isOpen = true;
    modality3.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags =
        new KotlinPropertyAccessorFlags(visibility3, modality3);

    LibraryField libraryField = new LibraryField();
    libraryField.addProcessingFlags(2, 4194304, 2, 4194304);

    KotlinPropertyMetadata kotlinPropertyMetadata =
        new KotlinPropertyMetadata(flags, "Name", getterFlags, setterFlags);
    kotlinPropertyMetadata.referencedBackingField = libraryField;
    kotlinPropertyMetadata.referencedGetterMethod = new LibraryMethod();
    kotlinPropertyMetadata.referencedSetterMethod = new LibraryMethod();

    // Act
    kotlinPropertyNameObfuscator.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert that nothing has changed
    assertNull(kotlinPropertyMetadata.getProcessingInfo());
  }

  /**
   * Test {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
   *
   * <ul>
   *   <li>Given {@link LibraryMethod#LibraryMethod()} addProcessingFlags two and {@code 4194304}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata); given LibraryMethod() addProcessingFlags two and '4194304'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinPropertyNameObfuscator.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty_givenLibraryMethodAddProcessingFlagsTwoAnd4194304() {
    // Arrange
    KotlinPropertyNameObfuscator kotlinPropertyNameObfuscator =
        new KotlinPropertyNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinModalityFlags modality = new KotlinModalityFlags();
    modality.isAbstract = true;
    modality.isFinal = true;
    modality.isOpen = true;
    modality.isSealed = true;

    KotlinPropertyFlags flags = new KotlinPropertyFlags(visibility, modality);
    KotlinVisibilityFlags visibility2 = new KotlinVisibilityFlags();
    visibility2.isInternal = true;
    visibility2.isLocal = true;
    visibility2.isPrivate = true;
    visibility2.isPrivateToThis = true;
    visibility2.isProtected = true;
    visibility2.isPublic = true;
    KotlinModalityFlags modality2 = new KotlinModalityFlags();
    modality2.isAbstract = true;
    modality2.isFinal = true;
    modality2.isOpen = true;
    modality2.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags =
        new KotlinPropertyAccessorFlags(visibility2, modality2);
    KotlinVisibilityFlags visibility3 = new KotlinVisibilityFlags();
    visibility3.isInternal = true;
    visibility3.isLocal = true;
    visibility3.isPrivate = true;
    visibility3.isPrivateToThis = true;
    visibility3.isProtected = true;
    visibility3.isPublic = true;
    KotlinModalityFlags modality3 = new KotlinModalityFlags();
    modality3.isAbstract = true;
    modality3.isFinal = true;
    modality3.isOpen = true;
    modality3.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags =
        new KotlinPropertyAccessorFlags(visibility3, modality3);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.addProcessingFlags(2, 4194304, 2, 4194304);

    KotlinPropertyMetadata kotlinPropertyMetadata =
        new KotlinPropertyMetadata(flags, "Name", getterFlags, setterFlags);
    kotlinPropertyMetadata.referencedBackingField = new LibraryField();
    kotlinPropertyMetadata.referencedGetterMethod = libraryMethod;
    kotlinPropertyMetadata.referencedSetterMethod = new LibraryMethod();

    // Act
    kotlinPropertyNameObfuscator.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert that nothing has changed
    assertNull(kotlinPropertyMetadata.getProcessingInfo());
  }

  /**
   * Test {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
   *
   * <ul>
   *   <li>Given {@link LibraryMethod#LibraryMethod()} addProcessingFlags two and {@code 4194304}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata); given LibraryMethod() addProcessingFlags two and '4194304'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinPropertyNameObfuscator.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty_givenLibraryMethodAddProcessingFlagsTwoAnd41943042() {
    // Arrange
    KotlinPropertyNameObfuscator kotlinPropertyNameObfuscator =
        new KotlinPropertyNameObfuscator(new NumericNameFactory());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinModalityFlags modality = new KotlinModalityFlags();
    modality.isAbstract = true;
    modality.isFinal = true;
    modality.isOpen = true;
    modality.isSealed = true;

    KotlinPropertyFlags flags = new KotlinPropertyFlags(visibility, modality);
    KotlinVisibilityFlags visibility2 = new KotlinVisibilityFlags();
    visibility2.isInternal = true;
    visibility2.isLocal = true;
    visibility2.isPrivate = true;
    visibility2.isPrivateToThis = true;
    visibility2.isProtected = true;
    visibility2.isPublic = true;
    KotlinModalityFlags modality2 = new KotlinModalityFlags();
    modality2.isAbstract = true;
    modality2.isFinal = true;
    modality2.isOpen = true;
    modality2.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags =
        new KotlinPropertyAccessorFlags(visibility2, modality2);
    KotlinVisibilityFlags visibility3 = new KotlinVisibilityFlags();
    visibility3.isInternal = true;
    visibility3.isLocal = true;
    visibility3.isPrivate = true;
    visibility3.isPrivateToThis = true;
    visibility3.isProtected = true;
    visibility3.isPublic = true;
    KotlinModalityFlags modality3 = new KotlinModalityFlags();
    modality3.isAbstract = true;
    modality3.isFinal = true;
    modality3.isOpen = true;
    modality3.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags =
        new KotlinPropertyAccessorFlags(visibility3, modality3);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.addProcessingFlags(2, 4194304, 2, 4194304);

    KotlinPropertyMetadata kotlinPropertyMetadata =
        new KotlinPropertyMetadata(flags, "Name", getterFlags, setterFlags);
    kotlinPropertyMetadata.referencedBackingField = new LibraryField();
    kotlinPropertyMetadata.referencedGetterMethod = new LibraryMethod();
    kotlinPropertyMetadata.referencedSetterMethod = libraryMethod;

    // Act
    kotlinPropertyNameObfuscator.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert that nothing has changed
    assertNull(kotlinPropertyMetadata.getProcessingInfo());
  }
}
