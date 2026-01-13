package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata.Flavor;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.classfile.kotlin.flags.KotlinFunctionFlags;
import proguard.classfile.kotlin.flags.KotlinModalityFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyAccessorFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyFlags;
import proguard.classfile.kotlin.flags.KotlinValueParameterFlags;
import proguard.classfile.kotlin.flags.KotlinVisibilityFlags;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.Processable;
import proguard.util.SimpleProcessable;

class KotlinValueParameterUsageMarkerDiffblueTest {
  /**
   * Test {@link KotlinValueParameterUsageMarker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinClassKindMetadata#accept(Clazz, KotlinMetadataVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * KotlinValueParameterUsageMarker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata); then calls accept(Clazz, KotlinMetadataVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterUsageMarker.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testVisitKotlinDeclarationContainerMetadata_thenCallsAccept() {
    // Arrange
    KotlinValueParameterUsageMarker kotlinValueParameterUsageMarker =
        new KotlinValueParameterUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        mock(KotlinClassKindMetadata.class);
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .accept(Mockito.<Clazz>any(), Mockito.<KotlinMetadataVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());

    // Act
    kotlinValueParameterUsageMarker.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    verify(kotlinDeclarationContainerMetadata)
        .accept(isA(Clazz.class), isA(KotlinMetadataVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterUsageMarker#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinClassKindMetadata#accept(Clazz, KotlinMetadataVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinValueParameterUsageMarker#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata); then calls accept(Clazz, KotlinMetadataVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterUsageMarker.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata_thenCallsAccept() {
    // Arrange
    KotlinValueParameterUsageMarker kotlinValueParameterUsageMarker =
        new KotlinValueParameterUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinClassKindMetadata kotlinClassKindMetadata = mock(KotlinClassKindMetadata.class);
    doNothing()
        .when(kotlinClassKindMetadata)
        .accept(Mockito.<Clazz>any(), Mockito.<KotlinMetadataVisitor>any());
    doNothing()
        .when(kotlinClassKindMetadata)
        .constructorsAccept(Mockito.<Clazz>any(), Mockito.<KotlinConstructorVisitor>any());
    doNothing()
        .when(kotlinClassKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());

    // Act
    kotlinValueParameterUsageMarker.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert
    verify(kotlinClassKindMetadata).accept(isA(Clazz.class), isA(KotlinMetadataVisitor.class));
    verify(kotlinClassKindMetadata)
        .constructorsAccept(isA(Clazz.class), isA(KotlinConstructorVisitor.class));
    verify(kotlinClassKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinSyntheticClassKindMetadata#functionsAccept(Clazz,
   *       KotlinFunctionVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * KotlinValueParameterUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata); then calls functionsAccept(Clazz, KotlinFunctionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterUsageMarker.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata_thenCallsFunctionsAccept() {
    // Arrange
    KotlinValueParameterUsageMarker kotlinValueParameterUsageMarker =
        new KotlinValueParameterUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        mock(KotlinSyntheticClassKindMetadata.class);
    doNothing()
        .when(kotlinSyntheticClassKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());

    // Act
    kotlinValueParameterUsageMarker.visitKotlinSyntheticClassMetadata(
        clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    verify(kotlinSyntheticClassKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterUsageMarker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinConstructorMetadata#referencedMethodAccept(Clazz,
   *       MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinValueParameterUsageMarker#visitConstructor(Clazz,
   * KotlinClassKindMetadata, KotlinConstructorMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata); then calls referencedMethodAccept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterUsageMarker.visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)"
  })
  void testVisitConstructor_thenCallsReferencedMethodAccept() {
    // Arrange
    KotlinValueParameterUsageMarker kotlinValueParameterUsageMarker =
        new KotlinValueParameterUsageMarker();
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");

    KotlinConstructorMetadata kotlinConstructorMetadata = mock(KotlinConstructorMetadata.class);
    doNothing()
        .when(kotlinConstructorMetadata)
        .referencedMethodAccept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    kotlinValueParameterUsageMarker.visitConstructor(
        clazz, kotlinClassKindMetadata, kotlinConstructorMetadata);

    // Assert
    verify(kotlinConstructorMetadata)
        .referencedMethodAccept(isA(Clazz.class), isA(MemberVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterUsageMarker#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
   *
   * <ul>
   *   <li>Given {@link LibraryMethod} {@link LibraryMethod#accept(Clazz, MemberVisitor)} does
   *       nothing.
   *   <li>Then calls {@link LibraryMethod#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinValueParameterUsageMarker#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata); given LibraryMethod accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterUsageMarker.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty_givenLibraryMethodAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    KotlinValueParameterUsageMarker kotlinValueParameterUsageMarker =
        new KotlinValueParameterUsageMarker();
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

    LibraryMethod libraryMethod = mock(LibraryMethod.class);
    doNothing().when(libraryMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
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
    kotlinPropertyMetadata.referencedSetterMethod = libraryMethod;
    kotlinPropertyMetadata.setterParameters = kotlinValueParameterMetadataList;

    // Act
    kotlinValueParameterUsageMarker.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert
    verify(libraryMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterUsageMarker#visitAnyFunction(Clazz, KotlinMetadata,
   * KotlinFunctionMetadata)}.
   *
   * <ul>
   *   <li>Given {@link LibraryMethod} {@link LibraryMethod#accept(Clazz, MemberVisitor)} does
   *       nothing.
   *   <li>Then calls {@link LibraryMethod#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinValueParameterUsageMarker#visitAnyFunction(Clazz,
   * KotlinMetadata, KotlinFunctionMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata); given LibraryMethod accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterUsageMarker.visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)"
  })
  void testVisitAnyFunction_givenLibraryMethodAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    KotlinValueParameterUsageMarker kotlinValueParameterUsageMarker =
        new KotlinValueParameterUsageMarker();
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

    LibraryMethod libraryMethod = mock(LibraryMethod.class);
    doNothing().when(libraryMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
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
    kotlinFunctionMetadata.referencedMethod = libraryMethod;
    kotlinFunctionMetadata.referencedMethodClass = new LibraryClass();
    kotlinFunctionMetadata.valueParameters = kotlinValueParameterMetadataList;

    // Act
    kotlinValueParameterUsageMarker.visitAnyFunction(clazz, kotlinMetadata, kotlinFunctionMetadata);

    // Assert
    verify(libraryMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterUsageMarker#visitAnyFunction(Clazz, KotlinMetadata,
   * KotlinFunctionMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinFunctionMetadata#referencedMethodAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinValueParameterUsageMarker#visitAnyFunction(Clazz,
   * KotlinMetadata, KotlinFunctionMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata); then calls referencedMethodAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinValueParameterUsageMarker.visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)"
  })
  void testVisitAnyFunction_thenCallsReferencedMethodAccept() {
    // Arrange
    KotlinValueParameterUsageMarker kotlinValueParameterUsageMarker =
        new KotlinValueParameterUsageMarker();
    LibraryClass clazz = new LibraryClass();
    KotlinSyntheticClassKindMetadata kotlinMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);

    KotlinFunctionMetadata kotlinFunctionMetadata = mock(KotlinFunctionMetadata.class);
    doNothing().when(kotlinFunctionMetadata).referencedMethodAccept(Mockito.<MemberVisitor>any());

    // Act
    kotlinValueParameterUsageMarker.visitAnyFunction(clazz, kotlinMetadata, kotlinFunctionMetadata);

    // Assert
    verify(kotlinFunctionMetadata).referencedMethodAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link KotlinValueParameterUsageMarker#isUsed(Processable)}.
   *
   * <ul>
   *   <li>When {@link SimpleProcessable#SimpleProcessable()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinValueParameterUsageMarker#isUsed(Processable)}
   */
  @Test
  @DisplayName("Test isUsed(Processable); when SimpleProcessable(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KotlinValueParameterUsageMarker.isUsed(Processable)"})
  void testIsUsed_whenSimpleProcessable_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(KotlinValueParameterUsageMarker.isUsed(new SimpleProcessable()));
  }
}
