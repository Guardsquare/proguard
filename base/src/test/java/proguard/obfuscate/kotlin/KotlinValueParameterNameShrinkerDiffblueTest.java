package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata.Flavor;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.classfile.kotlin.flags.KotlinConstructorFlags;
import proguard.classfile.kotlin.flags.KotlinFunctionFlags;
import proguard.classfile.kotlin.flags.KotlinModalityFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyAccessorFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyFlags;
import proguard.classfile.kotlin.flags.KotlinValueParameterFlags;
import proguard.classfile.kotlin.flags.KotlinVisibilityFlags;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;

class KotlinValueParameterNameShrinkerDiffblueTest {
  /**
   * Test {@link KotlinValueParameterNameShrinker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinClassKindMetadata#functionsAccept(Clazz, KotlinFunctionVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * KotlinValueParameterNameShrinker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata); then calls functionsAccept(Clazz, KotlinFunctionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterNameShrinker.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testVisitKotlinDeclarationContainerMetadata_thenCallsFunctionsAccept() {
    // Arrange
    KotlinValueParameterNameShrinker kotlinValueParameterNameShrinker =
        new KotlinValueParameterNameShrinker();
    LibraryClass clazz = new LibraryClass();

    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        mock(KotlinClassKindMetadata.class);
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .propertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());

    // Act
    kotlinValueParameterNameShrinker.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    verify(kotlinDeclarationContainerMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .propertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterNameShrinker#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinClassKindMetadata#constructorsAccept(Clazz,
   *       KotlinConstructorVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinValueParameterNameShrinker#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata); then calls constructorsAccept(Clazz, KotlinConstructorVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterNameShrinker.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata_thenCallsConstructorsAccept() {
    // Arrange
    KotlinValueParameterNameShrinker kotlinValueParameterNameShrinker =
        new KotlinValueParameterNameShrinker();
    LibraryClass clazz = new LibraryClass();

    KotlinClassKindMetadata kotlinClassKindMetadata = mock(KotlinClassKindMetadata.class);
    doNothing()
        .when(kotlinClassKindMetadata)
        .constructorsAccept(Mockito.<Clazz>any(), Mockito.<KotlinConstructorVisitor>any());
    doNothing()
        .when(kotlinClassKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());
    doNothing()
        .when(kotlinClassKindMetadata)
        .propertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());

    // Act
    kotlinValueParameterNameShrinker.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert
    verify(kotlinClassKindMetadata)
        .constructorsAccept(isA(Clazz.class), isA(KotlinConstructorVisitor.class));
    verify(kotlinClassKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(kotlinClassKindMetadata)
        .propertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterNameShrinker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinSyntheticClassKindMetadata#functionsAccept(Clazz,
   *       KotlinFunctionVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * KotlinValueParameterNameShrinker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata); then calls functionsAccept(Clazz, KotlinFunctionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterNameShrinker.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata_thenCallsFunctionsAccept() {
    // Arrange
    KotlinValueParameterNameShrinker kotlinValueParameterNameShrinker =
        new KotlinValueParameterNameShrinker();
    LibraryClass clazz = new LibraryClass();

    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        mock(KotlinSyntheticClassKindMetadata.class);
    doNothing()
        .when(kotlinSyntheticClassKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());

    // Act
    kotlinValueParameterNameShrinker.visitKotlinSyntheticClassMetadata(
        clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    verify(kotlinSyntheticClassKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterNameShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}.
   *
   * <p>Method under test: {@link KotlinValueParameterNameShrinker#visitConstructor(Clazz,
   * KotlinClassKindMetadata, KotlinConstructorMetadata)}
   */
  @Test
  @DisplayName("Test visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterNameShrinker.visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)"
  })
  void testVisitConstructor() {
    // Arrange
    KotlinValueParameterNameShrinker kotlinValueParameterNameShrinker =
        new KotlinValueParameterNameShrinker();
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    visibility.isInternal = true;
    visibility.isLocal = true;
    visibility.isPrivate = true;
    visibility.isPrivateToThis = true;
    visibility.isProtected = true;
    visibility.isPublic = true;
    KotlinConstructorFlags flags = new KotlinConstructorFlags(visibility);
    KotlinValueParameterFlags flags2 = new KotlinValueParameterFlags();
    flags2.hasAnnotations = true;
    flags2.hasDefaultValue = true;
    flags2.isCrossInline = true;
    flags2.isNoInline = true;
    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        new KotlinValueParameterMetadata(flags2, 1, "Parameter Name");

    ArrayList<KotlinValueParameterMetadata> kotlinValueParameterMetadataList = new ArrayList<>();
    kotlinValueParameterMetadataList.add(kotlinValueParameterMetadata);
    KotlinConstructorMetadata kotlinConstructorMetadata = new KotlinConstructorMetadata(flags);
    kotlinConstructorMetadata.valueParameters = kotlinValueParameterMetadataList;

    // Act
    kotlinValueParameterNameShrinker.visitConstructor(
        clazz, kotlinClassKindMetadata, kotlinConstructorMetadata);

    // Assert
    List<KotlinValueParameterMetadata> kotlinValueParameterMetadataList2 =
        kotlinConstructorMetadata.valueParameters;
    assertEquals(1, kotlinValueParameterMetadataList2.size());
    assertEquals("p0", kotlinValueParameterMetadataList2.get(0).parameterName);
  }

  /**
   * Test {@link KotlinValueParameterNameShrinker#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
   *
   * <p>Method under test: {@link KotlinValueParameterNameShrinker#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterNameShrinker.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty() {
    // Arrange
    KotlinValueParameterNameShrinker kotlinValueParameterNameShrinker =
        new KotlinValueParameterNameShrinker();
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
    KotlinValueParameterFlags flags2 = new KotlinValueParameterFlags();
    flags2.hasAnnotations = true;
    flags2.hasDefaultValue = true;
    flags2.isCrossInline = true;
    flags2.isNoInline = true;
    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        new KotlinValueParameterMetadata(flags2, 1, "Parameter Name");

    ArrayList<KotlinValueParameterMetadata> kotlinValueParameterMetadataList = new ArrayList<>();
    kotlinValueParameterMetadataList.add(kotlinValueParameterMetadata);

    KotlinPropertyMetadata kotlinPropertyMetadata =
        new KotlinPropertyMetadata(flags, "Name", getterFlags, setterFlags);
    kotlinPropertyMetadata.setterParameters = kotlinValueParameterMetadataList;

    // Act
    kotlinValueParameterNameShrinker.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert
    List<KotlinValueParameterMetadata> kotlinValueParameterMetadataList2 =
        kotlinPropertyMetadata.setterParameters;
    assertEquals(1, kotlinValueParameterMetadataList2.size());
    assertEquals("p0", kotlinValueParameterMetadataList2.get(0).parameterName);
  }

  /**
   * Test {@link KotlinValueParameterNameShrinker#visitAnyFunction(Clazz, KotlinMetadata,
   * KotlinFunctionMetadata)}.
   *
   * <p>Method under test: {@link KotlinValueParameterNameShrinker#visitAnyFunction(Clazz,
   * KotlinMetadata, KotlinFunctionMetadata)}
   */
  @Test
  @DisplayName("Test visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterNameShrinker.visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)"
  })
  void testVisitAnyFunction() {
    // Arrange
    KotlinValueParameterNameShrinker kotlinValueParameterNameShrinker =
        new KotlinValueParameterNameShrinker();
    LibraryClass clazz = new LibraryClass();
    KotlinSyntheticClassKindMetadata kotlinMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);
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

    KotlinFunctionFlags flags = new KotlinFunctionFlags(visibility, modality);
    KotlinValueParameterFlags flags2 = new KotlinValueParameterFlags();
    flags2.hasAnnotations = true;
    flags2.hasDefaultValue = true;
    flags2.isCrossInline = true;
    flags2.isNoInline = true;
    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        new KotlinValueParameterMetadata(flags2, 1, "Parameter Name");

    ArrayList<KotlinValueParameterMetadata> kotlinValueParameterMetadataList = new ArrayList<>();
    kotlinValueParameterMetadataList.add(kotlinValueParameterMetadata);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags, "Name");
    kotlinFunctionMetadata.valueParameters = kotlinValueParameterMetadataList;

    // Act
    kotlinValueParameterNameShrinker.visitAnyFunction(
        clazz, kotlinMetadata, kotlinFunctionMetadata);

    // Assert
    List<KotlinValueParameterMetadata> kotlinValueParameterMetadataList2 =
        kotlinFunctionMetadata.valueParameters;
    assertEquals(1, kotlinValueParameterMetadataList2.size());
    assertEquals("p0", kotlinValueParameterMetadataList2.get(0).parameterName);
  }
}
