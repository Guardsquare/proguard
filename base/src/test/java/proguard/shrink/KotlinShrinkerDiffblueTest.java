package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
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
import proguard.classfile.kotlin.KotlinAnnotation;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFileFacadeKindMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinMultiFilePartKindMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata.Flavor;
import proguard.classfile.kotlin.KotlinTypeAliasMetadata;
import proguard.classfile.kotlin.KotlinTypeMetadata;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.classfile.kotlin.KotlinVersionRequirementMetadata;
import proguard.classfile.kotlin.flags.KotlinConstructorFlags;
import proguard.classfile.kotlin.flags.KotlinFunctionFlags;
import proguard.classfile.kotlin.flags.KotlinModalityFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyAccessorFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyFlags;
import proguard.classfile.kotlin.flags.KotlinTypeAliasFlags;
import proguard.classfile.kotlin.flags.KotlinTypeFlags;
import proguard.classfile.kotlin.flags.KotlinVisibilityFlags;
import proguard.classfile.kotlin.visitor.KotlinTypeParameterVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeVisitor;
import proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor;
import proguard.classfile.kotlin.visitor.KotlinVersionRequirementVisitor;
import proguard.util.Processable;
import proguard.util.SimpleFeatureNamedProcessable;
import proguard.util.SimpleProcessable;

class KotlinShrinkerDiffblueTest {
  /**
   * Test {@link KotlinShrinker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testVisitKotlinDeclarationContainerMetadata() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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
    KotlinVisibilityFlags visibility4 = new KotlinVisibilityFlags();
    visibility4.isInternal = true;
    visibility4.isLocal = true;
    visibility4.isPrivate = true;
    visibility4.isPrivateToThis = true;
    visibility4.isProtected = true;
    visibility4.isPublic = true;
    KotlinModalityFlags modality4 = new KotlinModalityFlags();
    modality4.isAbstract = true;
    modality4.isFinal = true;
    modality4.isOpen = true;
    modality4.isSealed = true;

    KotlinFunctionFlags flags2 = new KotlinFunctionFlags(visibility4, modality4);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags2, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinVisibilityFlags visibility5 = new KotlinVisibilityFlags();
    visibility5.isInternal = true;
    visibility5.isLocal = true;
    visibility5.isPrivate = true;
    visibility5.isPrivateToThis = true;
    visibility5.isProtected = true;
    visibility5.isPublic = true;
    KotlinTypeAliasMetadata kotlinTypeAliasMetadata =
        new KotlinTypeAliasMetadata(new KotlinTypeAliasFlags(visibility5), "Name");

    ArrayList<KotlinTypeAliasMetadata> kotlinTypeAliasMetadataList = new ArrayList<>();
    kotlinTypeAliasMetadataList.add(kotlinTypeAliasMetadata);
    KotlinVisibilityFlags visibility6 = new KotlinVisibilityFlags();
    visibility6.isInternal = true;
    visibility6.isLocal = true;
    visibility6.isPrivate = true;
    visibility6.isPrivateToThis = true;
    visibility6.isProtected = true;
    visibility6.isPublic = true;
    KotlinModalityFlags modality5 = new KotlinModalityFlags();
    modality5.isAbstract = true;
    modality5.isFinal = true;
    modality5.isOpen = true;
    modality5.isSealed = true;

    KotlinPropertyFlags flags3 = new KotlinPropertyFlags(visibility6, modality5);
    KotlinVisibilityFlags visibility7 = new KotlinVisibilityFlags();
    visibility7.isInternal = true;
    visibility7.isLocal = true;
    visibility7.isPrivate = true;
    visibility7.isPrivateToThis = true;
    visibility7.isProtected = true;
    visibility7.isPublic = true;
    KotlinModalityFlags modality6 = new KotlinModalityFlags();
    modality6.isAbstract = true;
    modality6.isFinal = true;
    modality6.isOpen = true;
    modality6.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags2 =
        new KotlinPropertyAccessorFlags(visibility7, modality6);
    KotlinVisibilityFlags visibility8 = new KotlinVisibilityFlags();
    visibility8.isInternal = true;
    visibility8.isLocal = true;
    visibility8.isPrivate = true;
    visibility8.isPrivateToThis = true;
    visibility8.isProtected = true;
    visibility8.isPublic = true;
    KotlinModalityFlags modality7 = new KotlinModalityFlags();
    modality7.isAbstract = true;
    modality7.isFinal = true;
    modality7.isOpen = true;
    modality7.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags2 =
        new KotlinPropertyAccessorFlags(visibility8, modality7);

    KotlinPropertyMetadata kotlinPropertyMetadata2 =
        new KotlinPropertyMetadata(flags3, "Name", getterFlags2, setterFlags2);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList2 = new ArrayList<>();
    kotlinPropertyMetadataList2.add(kotlinPropertyMetadata2);
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinDeclarationContainerMetadata.properties = kotlinPropertyMetadataList;
    kotlinDeclarationContainerMetadata.functions = kotlinFunctionMetadataList;
    kotlinDeclarationContainerMetadata.typeAliases = kotlinTypeAliasMetadataList;
    kotlinDeclarationContainerMetadata.localDelegatedProperties = kotlinPropertyMetadataList2;

    // Act
    kotlinShrinker.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    assertTrue(kotlinDeclarationContainerMetadata.functions.isEmpty());
    assertTrue(kotlinDeclarationContainerMetadata.localDelegatedProperties.isEmpty());
    assertTrue(kotlinDeclarationContainerMetadata.properties.isEmpty());
    assertTrue(kotlinDeclarationContainerMetadata.typeAliases.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testVisitKotlinDeclarationContainerMetadata2() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new ShortestUsageMarker());
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
    KotlinVisibilityFlags visibility4 = new KotlinVisibilityFlags();
    visibility4.isInternal = true;
    visibility4.isLocal = true;
    visibility4.isPrivate = true;
    visibility4.isPrivateToThis = true;
    visibility4.isProtected = true;
    visibility4.isPublic = true;
    KotlinModalityFlags modality4 = new KotlinModalityFlags();
    modality4.isAbstract = true;
    modality4.isFinal = true;
    modality4.isOpen = true;
    modality4.isSealed = true;

    KotlinPropertyFlags flags2 = new KotlinPropertyFlags(visibility4, modality4);
    KotlinVisibilityFlags visibility5 = new KotlinVisibilityFlags();
    visibility5.isInternal = true;
    visibility5.isLocal = true;
    visibility5.isPrivate = true;
    visibility5.isPrivateToThis = true;
    visibility5.isProtected = true;
    visibility5.isPublic = true;
    KotlinModalityFlags modality5 = new KotlinModalityFlags();
    modality5.isAbstract = true;
    modality5.isFinal = true;
    modality5.isOpen = true;
    modality5.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags2 =
        new KotlinPropertyAccessorFlags(visibility5, modality5);
    KotlinVisibilityFlags visibility6 = new KotlinVisibilityFlags();
    visibility6.isInternal = true;
    visibility6.isLocal = true;
    visibility6.isPrivate = true;
    visibility6.isPrivateToThis = true;
    visibility6.isProtected = true;
    visibility6.isPublic = true;
    KotlinModalityFlags modality6 = new KotlinModalityFlags();
    modality6.isAbstract = true;
    modality6.isFinal = true;
    modality6.isOpen = true;
    modality6.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags2 =
        new KotlinPropertyAccessorFlags(visibility6, modality6);

    KotlinPropertyMetadata kotlinPropertyMetadata2 =
        new KotlinPropertyMetadata(flags2, "Name", getterFlags2, setterFlags2);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList = new ArrayList<>();
    kotlinPropertyMetadataList.add(kotlinPropertyMetadata2);
    kotlinPropertyMetadataList.add(kotlinPropertyMetadata);
    KotlinVisibilityFlags visibility7 = new KotlinVisibilityFlags();
    visibility7.isInternal = true;
    visibility7.isLocal = true;
    visibility7.isPrivate = true;
    visibility7.isPrivateToThis = true;
    visibility7.isProtected = true;
    visibility7.isPublic = true;
    KotlinModalityFlags modality7 = new KotlinModalityFlags();
    modality7.isAbstract = true;
    modality7.isFinal = true;
    modality7.isOpen = true;
    modality7.isSealed = true;

    KotlinFunctionFlags flags3 = new KotlinFunctionFlags(visibility7, modality7);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags3, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinVisibilityFlags visibility8 = new KotlinVisibilityFlags();
    visibility8.isInternal = true;
    visibility8.isLocal = true;
    visibility8.isPrivate = true;
    visibility8.isPrivateToThis = true;
    visibility8.isProtected = true;
    visibility8.isPublic = true;
    KotlinTypeAliasMetadata kotlinTypeAliasMetadata =
        new KotlinTypeAliasMetadata(new KotlinTypeAliasFlags(visibility8), "Name");

    ArrayList<KotlinTypeAliasMetadata> kotlinTypeAliasMetadataList = new ArrayList<>();
    kotlinTypeAliasMetadataList.add(kotlinTypeAliasMetadata);
    KotlinVisibilityFlags visibility9 = new KotlinVisibilityFlags();
    visibility9.isInternal = true;
    visibility9.isLocal = true;
    visibility9.isPrivate = true;
    visibility9.isPrivateToThis = true;
    visibility9.isProtected = true;
    visibility9.isPublic = true;
    KotlinModalityFlags modality8 = new KotlinModalityFlags();
    modality8.isAbstract = true;
    modality8.isFinal = true;
    modality8.isOpen = true;
    modality8.isSealed = true;

    KotlinPropertyFlags flags4 = new KotlinPropertyFlags(visibility9, modality8);
    KotlinVisibilityFlags visibility10 = new KotlinVisibilityFlags();
    visibility10.isInternal = true;
    visibility10.isLocal = true;
    visibility10.isPrivate = true;
    visibility10.isPrivateToThis = true;
    visibility10.isProtected = true;
    visibility10.isPublic = true;
    KotlinModalityFlags modality9 = new KotlinModalityFlags();
    modality9.isAbstract = true;
    modality9.isFinal = true;
    modality9.isOpen = true;
    modality9.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags3 =
        new KotlinPropertyAccessorFlags(visibility10, modality9);
    KotlinVisibilityFlags visibility11 = new KotlinVisibilityFlags();
    visibility11.isInternal = true;
    visibility11.isLocal = true;
    visibility11.isPrivate = true;
    visibility11.isPrivateToThis = true;
    visibility11.isProtected = true;
    visibility11.isPublic = true;
    KotlinModalityFlags modality10 = new KotlinModalityFlags();
    modality10.isAbstract = true;
    modality10.isFinal = true;
    modality10.isOpen = true;
    modality10.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags3 =
        new KotlinPropertyAccessorFlags(visibility11, modality10);

    KotlinPropertyMetadata kotlinPropertyMetadata3 =
        new KotlinPropertyMetadata(flags4, "Name", getterFlags3, setterFlags3);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList2 = new ArrayList<>();
    kotlinPropertyMetadataList2.add(kotlinPropertyMetadata3);
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinDeclarationContainerMetadata.properties = kotlinPropertyMetadataList;
    kotlinDeclarationContainerMetadata.functions = kotlinFunctionMetadataList;
    kotlinDeclarationContainerMetadata.typeAliases = kotlinTypeAliasMetadataList;
    kotlinDeclarationContainerMetadata.localDelegatedProperties = kotlinPropertyMetadataList2;

    // Act
    kotlinShrinker.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    assertTrue(kotlinDeclarationContainerMetadata.functions.isEmpty());
    assertTrue(kotlinDeclarationContainerMetadata.localDelegatedProperties.isEmpty());
    assertTrue(kotlinDeclarationContainerMetadata.properties.isEmpty());
    assertTrue(kotlinDeclarationContainerMetadata.typeAliases.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinFileFacadeMetadata(Clazz,
   * KotlinFileFacadeKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)"
  })
  void testVisitKotlinFileFacadeMetadata() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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
    KotlinVisibilityFlags visibility4 = new KotlinVisibilityFlags();
    visibility4.isInternal = true;
    visibility4.isLocal = true;
    visibility4.isPrivate = true;
    visibility4.isPrivateToThis = true;
    visibility4.isProtected = true;
    visibility4.isPublic = true;
    KotlinModalityFlags modality4 = new KotlinModalityFlags();
    modality4.isAbstract = true;
    modality4.isFinal = true;
    modality4.isOpen = true;
    modality4.isSealed = true;

    KotlinFunctionFlags flags2 = new KotlinFunctionFlags(visibility4, modality4);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags2, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinVisibilityFlags visibility5 = new KotlinVisibilityFlags();
    visibility5.isInternal = true;
    visibility5.isLocal = true;
    visibility5.isPrivate = true;
    visibility5.isPrivateToThis = true;
    visibility5.isProtected = true;
    visibility5.isPublic = true;
    KotlinTypeAliasMetadata kotlinTypeAliasMetadata =
        new KotlinTypeAliasMetadata(new KotlinTypeAliasFlags(visibility5), "Name");

    ArrayList<KotlinTypeAliasMetadata> kotlinTypeAliasMetadataList = new ArrayList<>();
    kotlinTypeAliasMetadataList.add(kotlinTypeAliasMetadata);
    KotlinVisibilityFlags visibility6 = new KotlinVisibilityFlags();
    visibility6.isInternal = true;
    visibility6.isLocal = true;
    visibility6.isPrivate = true;
    visibility6.isPrivateToThis = true;
    visibility6.isProtected = true;
    visibility6.isPublic = true;
    KotlinModalityFlags modality5 = new KotlinModalityFlags();
    modality5.isAbstract = true;
    modality5.isFinal = true;
    modality5.isOpen = true;
    modality5.isSealed = true;

    KotlinPropertyFlags flags3 = new KotlinPropertyFlags(visibility6, modality5);
    KotlinVisibilityFlags visibility7 = new KotlinVisibilityFlags();
    visibility7.isInternal = true;
    visibility7.isLocal = true;
    visibility7.isPrivate = true;
    visibility7.isPrivateToThis = true;
    visibility7.isProtected = true;
    visibility7.isPublic = true;
    KotlinModalityFlags modality6 = new KotlinModalityFlags();
    modality6.isAbstract = true;
    modality6.isFinal = true;
    modality6.isOpen = true;
    modality6.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags2 =
        new KotlinPropertyAccessorFlags(visibility7, modality6);
    KotlinVisibilityFlags visibility8 = new KotlinVisibilityFlags();
    visibility8.isInternal = true;
    visibility8.isLocal = true;
    visibility8.isPrivate = true;
    visibility8.isPrivateToThis = true;
    visibility8.isProtected = true;
    visibility8.isPublic = true;
    KotlinModalityFlags modality7 = new KotlinModalityFlags();
    modality7.isAbstract = true;
    modality7.isFinal = true;
    modality7.isOpen = true;
    modality7.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags2 =
        new KotlinPropertyAccessorFlags(visibility8, modality7);

    KotlinPropertyMetadata kotlinPropertyMetadata2 =
        new KotlinPropertyMetadata(flags3, "Name", getterFlags2, setterFlags2);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList2 = new ArrayList<>();
    kotlinPropertyMetadataList2.add(kotlinPropertyMetadata2);
    KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata =
        new KotlinFileFacadeKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinFileFacadeKindMetadata.properties = kotlinPropertyMetadataList;
    kotlinFileFacadeKindMetadata.functions = kotlinFunctionMetadataList;
    kotlinFileFacadeKindMetadata.typeAliases = kotlinTypeAliasMetadataList;
    kotlinFileFacadeKindMetadata.localDelegatedProperties = kotlinPropertyMetadataList2;

    // Act
    kotlinShrinker.visitKotlinFileFacadeMetadata(clazz, kotlinFileFacadeKindMetadata);

    // Assert
    assertTrue(kotlinFileFacadeKindMetadata.functions.isEmpty());
    assertTrue(kotlinFileFacadeKindMetadata.localDelegatedProperties.isEmpty());
    assertTrue(kotlinFileFacadeKindMetadata.properties.isEmpty());
    assertTrue(kotlinFileFacadeKindMetadata.typeAliases.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinFileFacadeMetadata(Clazz,
   * KotlinFileFacadeKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)"
  })
  void testVisitKotlinFileFacadeMetadata2() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new ShortestUsageMarker());
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
    KotlinVisibilityFlags visibility4 = new KotlinVisibilityFlags();
    visibility4.isInternal = true;
    visibility4.isLocal = true;
    visibility4.isPrivate = true;
    visibility4.isPrivateToThis = true;
    visibility4.isProtected = true;
    visibility4.isPublic = true;
    KotlinModalityFlags modality4 = new KotlinModalityFlags();
    modality4.isAbstract = true;
    modality4.isFinal = true;
    modality4.isOpen = true;
    modality4.isSealed = true;

    KotlinPropertyFlags flags2 = new KotlinPropertyFlags(visibility4, modality4);
    KotlinVisibilityFlags visibility5 = new KotlinVisibilityFlags();
    visibility5.isInternal = true;
    visibility5.isLocal = true;
    visibility5.isPrivate = true;
    visibility5.isPrivateToThis = true;
    visibility5.isProtected = true;
    visibility5.isPublic = true;
    KotlinModalityFlags modality5 = new KotlinModalityFlags();
    modality5.isAbstract = true;
    modality5.isFinal = true;
    modality5.isOpen = true;
    modality5.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags2 =
        new KotlinPropertyAccessorFlags(visibility5, modality5);
    KotlinVisibilityFlags visibility6 = new KotlinVisibilityFlags();
    visibility6.isInternal = true;
    visibility6.isLocal = true;
    visibility6.isPrivate = true;
    visibility6.isPrivateToThis = true;
    visibility6.isProtected = true;
    visibility6.isPublic = true;
    KotlinModalityFlags modality6 = new KotlinModalityFlags();
    modality6.isAbstract = true;
    modality6.isFinal = true;
    modality6.isOpen = true;
    modality6.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags2 =
        new KotlinPropertyAccessorFlags(visibility6, modality6);

    KotlinPropertyMetadata kotlinPropertyMetadata2 =
        new KotlinPropertyMetadata(flags2, "Name", getterFlags2, setterFlags2);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList = new ArrayList<>();
    kotlinPropertyMetadataList.add(kotlinPropertyMetadata2);
    kotlinPropertyMetadataList.add(kotlinPropertyMetadata);
    KotlinVisibilityFlags visibility7 = new KotlinVisibilityFlags();
    visibility7.isInternal = true;
    visibility7.isLocal = true;
    visibility7.isPrivate = true;
    visibility7.isPrivateToThis = true;
    visibility7.isProtected = true;
    visibility7.isPublic = true;
    KotlinModalityFlags modality7 = new KotlinModalityFlags();
    modality7.isAbstract = true;
    modality7.isFinal = true;
    modality7.isOpen = true;
    modality7.isSealed = true;

    KotlinFunctionFlags flags3 = new KotlinFunctionFlags(visibility7, modality7);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags3, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinVisibilityFlags visibility8 = new KotlinVisibilityFlags();
    visibility8.isInternal = true;
    visibility8.isLocal = true;
    visibility8.isPrivate = true;
    visibility8.isPrivateToThis = true;
    visibility8.isProtected = true;
    visibility8.isPublic = true;
    KotlinTypeAliasMetadata kotlinTypeAliasMetadata =
        new KotlinTypeAliasMetadata(new KotlinTypeAliasFlags(visibility8), "Name");

    ArrayList<KotlinTypeAliasMetadata> kotlinTypeAliasMetadataList = new ArrayList<>();
    kotlinTypeAliasMetadataList.add(kotlinTypeAliasMetadata);
    KotlinVisibilityFlags visibility9 = new KotlinVisibilityFlags();
    visibility9.isInternal = true;
    visibility9.isLocal = true;
    visibility9.isPrivate = true;
    visibility9.isPrivateToThis = true;
    visibility9.isProtected = true;
    visibility9.isPublic = true;
    KotlinModalityFlags modality8 = new KotlinModalityFlags();
    modality8.isAbstract = true;
    modality8.isFinal = true;
    modality8.isOpen = true;
    modality8.isSealed = true;

    KotlinPropertyFlags flags4 = new KotlinPropertyFlags(visibility9, modality8);
    KotlinVisibilityFlags visibility10 = new KotlinVisibilityFlags();
    visibility10.isInternal = true;
    visibility10.isLocal = true;
    visibility10.isPrivate = true;
    visibility10.isPrivateToThis = true;
    visibility10.isProtected = true;
    visibility10.isPublic = true;
    KotlinModalityFlags modality9 = new KotlinModalityFlags();
    modality9.isAbstract = true;
    modality9.isFinal = true;
    modality9.isOpen = true;
    modality9.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags3 =
        new KotlinPropertyAccessorFlags(visibility10, modality9);
    KotlinVisibilityFlags visibility11 = new KotlinVisibilityFlags();
    visibility11.isInternal = true;
    visibility11.isLocal = true;
    visibility11.isPrivate = true;
    visibility11.isPrivateToThis = true;
    visibility11.isProtected = true;
    visibility11.isPublic = true;
    KotlinModalityFlags modality10 = new KotlinModalityFlags();
    modality10.isAbstract = true;
    modality10.isFinal = true;
    modality10.isOpen = true;
    modality10.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags3 =
        new KotlinPropertyAccessorFlags(visibility11, modality10);

    KotlinPropertyMetadata kotlinPropertyMetadata3 =
        new KotlinPropertyMetadata(flags4, "Name", getterFlags3, setterFlags3);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList2 = new ArrayList<>();
    kotlinPropertyMetadataList2.add(kotlinPropertyMetadata3);
    KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata =
        new KotlinFileFacadeKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinFileFacadeKindMetadata.properties = kotlinPropertyMetadataList;
    kotlinFileFacadeKindMetadata.functions = kotlinFunctionMetadataList;
    kotlinFileFacadeKindMetadata.typeAliases = kotlinTypeAliasMetadataList;
    kotlinFileFacadeKindMetadata.localDelegatedProperties = kotlinPropertyMetadataList2;

    // Act
    kotlinShrinker.visitKotlinFileFacadeMetadata(clazz, kotlinFileFacadeKindMetadata);

    // Assert
    assertTrue(kotlinFileFacadeKindMetadata.functions.isEmpty());
    assertTrue(kotlinFileFacadeKindMetadata.localDelegatedProperties.isEmpty());
    assertTrue(kotlinFileFacadeKindMetadata.properties.isEmpty());
    assertTrue(kotlinFileFacadeKindMetadata.typeAliases.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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

    KotlinFunctionFlags flags = new KotlinFunctionFlags(visibility, modality);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);
    kotlinSyntheticClassKindMetadata.functions = kotlinFunctionMetadataList;

    // Act
    kotlinShrinker.visitKotlinSyntheticClassMetadata(clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    assertTrue(kotlinSyntheticClassKindMetadata.functions.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata2() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new ShortestUsageMarker());
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

    KotlinFunctionFlags flags = new KotlinFunctionFlags(visibility, modality);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags, "Name");
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

    KotlinFunctionFlags flags2 = new KotlinFunctionFlags(visibility2, modality2);
    KotlinFunctionMetadata kotlinFunctionMetadata2 = new KotlinFunctionMetadata(flags2, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata2);
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);
    kotlinSyntheticClassKindMetadata.functions = kotlinFunctionMetadataList;

    // Act
    kotlinShrinker.visitKotlinSyntheticClassMetadata(clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    assertTrue(kotlinSyntheticClassKindMetadata.functions.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitKotlinMultiFilePartMetadata(Clazz,
   * KotlinMultiFilePartKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinMultiFilePartMetadata(Clazz,
   * KotlinMultiFilePartKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinMultiFilePartMetadata(Clazz, KotlinMultiFilePartKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinMultiFilePartMetadata(Clazz, KotlinMultiFilePartKindMetadata)"
  })
  void testVisitKotlinMultiFilePartMetadata() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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
    KotlinVisibilityFlags visibility4 = new KotlinVisibilityFlags();
    visibility4.isInternal = true;
    visibility4.isLocal = true;
    visibility4.isPrivate = true;
    visibility4.isPrivateToThis = true;
    visibility4.isProtected = true;
    visibility4.isPublic = true;
    KotlinModalityFlags modality4 = new KotlinModalityFlags();
    modality4.isAbstract = true;
    modality4.isFinal = true;
    modality4.isOpen = true;
    modality4.isSealed = true;

    KotlinFunctionFlags flags2 = new KotlinFunctionFlags(visibility4, modality4);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags2, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinVisibilityFlags visibility5 = new KotlinVisibilityFlags();
    visibility5.isInternal = true;
    visibility5.isLocal = true;
    visibility5.isPrivate = true;
    visibility5.isPrivateToThis = true;
    visibility5.isProtected = true;
    visibility5.isPublic = true;
    KotlinTypeAliasMetadata kotlinTypeAliasMetadata =
        new KotlinTypeAliasMetadata(new KotlinTypeAliasFlags(visibility5), "Name");

    ArrayList<KotlinTypeAliasMetadata> kotlinTypeAliasMetadataList = new ArrayList<>();
    kotlinTypeAliasMetadataList.add(kotlinTypeAliasMetadata);
    KotlinVisibilityFlags visibility6 = new KotlinVisibilityFlags();
    visibility6.isInternal = true;
    visibility6.isLocal = true;
    visibility6.isPrivate = true;
    visibility6.isPrivateToThis = true;
    visibility6.isProtected = true;
    visibility6.isPublic = true;
    KotlinModalityFlags modality5 = new KotlinModalityFlags();
    modality5.isAbstract = true;
    modality5.isFinal = true;
    modality5.isOpen = true;
    modality5.isSealed = true;

    KotlinPropertyFlags flags3 = new KotlinPropertyFlags(visibility6, modality5);
    KotlinVisibilityFlags visibility7 = new KotlinVisibilityFlags();
    visibility7.isInternal = true;
    visibility7.isLocal = true;
    visibility7.isPrivate = true;
    visibility7.isPrivateToThis = true;
    visibility7.isProtected = true;
    visibility7.isPublic = true;
    KotlinModalityFlags modality6 = new KotlinModalityFlags();
    modality6.isAbstract = true;
    modality6.isFinal = true;
    modality6.isOpen = true;
    modality6.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags2 =
        new KotlinPropertyAccessorFlags(visibility7, modality6);
    KotlinVisibilityFlags visibility8 = new KotlinVisibilityFlags();
    visibility8.isInternal = true;
    visibility8.isLocal = true;
    visibility8.isPrivate = true;
    visibility8.isPrivateToThis = true;
    visibility8.isProtected = true;
    visibility8.isPublic = true;
    KotlinModalityFlags modality7 = new KotlinModalityFlags();
    modality7.isAbstract = true;
    modality7.isFinal = true;
    modality7.isOpen = true;
    modality7.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags2 =
        new KotlinPropertyAccessorFlags(visibility8, modality7);

    KotlinPropertyMetadata kotlinPropertyMetadata2 =
        new KotlinPropertyMetadata(flags3, "Name", getterFlags2, setterFlags2);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList2 = new ArrayList<>();
    kotlinPropertyMetadataList2.add(kotlinPropertyMetadata2);
    KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata =
        new KotlinMultiFilePartKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinMultiFilePartKindMetadata.properties = kotlinPropertyMetadataList;
    kotlinMultiFilePartKindMetadata.functions = kotlinFunctionMetadataList;
    kotlinMultiFilePartKindMetadata.typeAliases = kotlinTypeAliasMetadataList;
    kotlinMultiFilePartKindMetadata.localDelegatedProperties = kotlinPropertyMetadataList2;

    // Act
    kotlinShrinker.visitKotlinMultiFilePartMetadata(clazz, kotlinMultiFilePartKindMetadata);

    // Assert
    assertTrue(kotlinMultiFilePartKindMetadata.functions.isEmpty());
    assertTrue(kotlinMultiFilePartKindMetadata.localDelegatedProperties.isEmpty());
    assertTrue(kotlinMultiFilePartKindMetadata.properties.isEmpty());
    assertTrue(kotlinMultiFilePartKindMetadata.typeAliases.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitKotlinMultiFilePartMetadata(Clazz,
   * KotlinMultiFilePartKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitKotlinMultiFilePartMetadata(Clazz,
   * KotlinMultiFilePartKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinMultiFilePartMetadata(Clazz, KotlinMultiFilePartKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitKotlinMultiFilePartMetadata(Clazz, KotlinMultiFilePartKindMetadata)"
  })
  void testVisitKotlinMultiFilePartMetadata2() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new ShortestUsageMarker());
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
    KotlinVisibilityFlags visibility4 = new KotlinVisibilityFlags();
    visibility4.isInternal = true;
    visibility4.isLocal = true;
    visibility4.isPrivate = true;
    visibility4.isPrivateToThis = true;
    visibility4.isProtected = true;
    visibility4.isPublic = true;
    KotlinModalityFlags modality4 = new KotlinModalityFlags();
    modality4.isAbstract = true;
    modality4.isFinal = true;
    modality4.isOpen = true;
    modality4.isSealed = true;

    KotlinPropertyFlags flags2 = new KotlinPropertyFlags(visibility4, modality4);
    KotlinVisibilityFlags visibility5 = new KotlinVisibilityFlags();
    visibility5.isInternal = true;
    visibility5.isLocal = true;
    visibility5.isPrivate = true;
    visibility5.isPrivateToThis = true;
    visibility5.isProtected = true;
    visibility5.isPublic = true;
    KotlinModalityFlags modality5 = new KotlinModalityFlags();
    modality5.isAbstract = true;
    modality5.isFinal = true;
    modality5.isOpen = true;
    modality5.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags2 =
        new KotlinPropertyAccessorFlags(visibility5, modality5);
    KotlinVisibilityFlags visibility6 = new KotlinVisibilityFlags();
    visibility6.isInternal = true;
    visibility6.isLocal = true;
    visibility6.isPrivate = true;
    visibility6.isPrivateToThis = true;
    visibility6.isProtected = true;
    visibility6.isPublic = true;
    KotlinModalityFlags modality6 = new KotlinModalityFlags();
    modality6.isAbstract = true;
    modality6.isFinal = true;
    modality6.isOpen = true;
    modality6.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags2 =
        new KotlinPropertyAccessorFlags(visibility6, modality6);

    KotlinPropertyMetadata kotlinPropertyMetadata2 =
        new KotlinPropertyMetadata(flags2, "Name", getterFlags2, setterFlags2);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList = new ArrayList<>();
    kotlinPropertyMetadataList.add(kotlinPropertyMetadata2);
    kotlinPropertyMetadataList.add(kotlinPropertyMetadata);
    KotlinVisibilityFlags visibility7 = new KotlinVisibilityFlags();
    visibility7.isInternal = true;
    visibility7.isLocal = true;
    visibility7.isPrivate = true;
    visibility7.isPrivateToThis = true;
    visibility7.isProtected = true;
    visibility7.isPublic = true;
    KotlinModalityFlags modality7 = new KotlinModalityFlags();
    modality7.isAbstract = true;
    modality7.isFinal = true;
    modality7.isOpen = true;
    modality7.isSealed = true;

    KotlinFunctionFlags flags3 = new KotlinFunctionFlags(visibility7, modality7);
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags3, "Name");

    ArrayList<KotlinFunctionMetadata> kotlinFunctionMetadataList = new ArrayList<>();
    kotlinFunctionMetadataList.add(kotlinFunctionMetadata);
    KotlinVisibilityFlags visibility8 = new KotlinVisibilityFlags();
    visibility8.isInternal = true;
    visibility8.isLocal = true;
    visibility8.isPrivate = true;
    visibility8.isPrivateToThis = true;
    visibility8.isProtected = true;
    visibility8.isPublic = true;
    KotlinTypeAliasMetadata kotlinTypeAliasMetadata =
        new KotlinTypeAliasMetadata(new KotlinTypeAliasFlags(visibility8), "Name");

    ArrayList<KotlinTypeAliasMetadata> kotlinTypeAliasMetadataList = new ArrayList<>();
    kotlinTypeAliasMetadataList.add(kotlinTypeAliasMetadata);
    KotlinVisibilityFlags visibility9 = new KotlinVisibilityFlags();
    visibility9.isInternal = true;
    visibility9.isLocal = true;
    visibility9.isPrivate = true;
    visibility9.isPrivateToThis = true;
    visibility9.isProtected = true;
    visibility9.isPublic = true;
    KotlinModalityFlags modality8 = new KotlinModalityFlags();
    modality8.isAbstract = true;
    modality8.isFinal = true;
    modality8.isOpen = true;
    modality8.isSealed = true;

    KotlinPropertyFlags flags4 = new KotlinPropertyFlags(visibility9, modality8);
    KotlinVisibilityFlags visibility10 = new KotlinVisibilityFlags();
    visibility10.isInternal = true;
    visibility10.isLocal = true;
    visibility10.isPrivate = true;
    visibility10.isPrivateToThis = true;
    visibility10.isProtected = true;
    visibility10.isPublic = true;
    KotlinModalityFlags modality9 = new KotlinModalityFlags();
    modality9.isAbstract = true;
    modality9.isFinal = true;
    modality9.isOpen = true;
    modality9.isSealed = true;

    KotlinPropertyAccessorFlags getterFlags3 =
        new KotlinPropertyAccessorFlags(visibility10, modality9);
    KotlinVisibilityFlags visibility11 = new KotlinVisibilityFlags();
    visibility11.isInternal = true;
    visibility11.isLocal = true;
    visibility11.isPrivate = true;
    visibility11.isPrivateToThis = true;
    visibility11.isProtected = true;
    visibility11.isPublic = true;
    KotlinModalityFlags modality10 = new KotlinModalityFlags();
    modality10.isAbstract = true;
    modality10.isFinal = true;
    modality10.isOpen = true;
    modality10.isSealed = true;

    KotlinPropertyAccessorFlags setterFlags3 =
        new KotlinPropertyAccessorFlags(visibility11, modality10);

    KotlinPropertyMetadata kotlinPropertyMetadata3 =
        new KotlinPropertyMetadata(flags4, "Name", getterFlags3, setterFlags3);

    ArrayList<KotlinPropertyMetadata> kotlinPropertyMetadataList2 = new ArrayList<>();
    kotlinPropertyMetadataList2.add(kotlinPropertyMetadata3);
    KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata =
        new KotlinMultiFilePartKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinMultiFilePartKindMetadata.properties = kotlinPropertyMetadataList;
    kotlinMultiFilePartKindMetadata.functions = kotlinFunctionMetadataList;
    kotlinMultiFilePartKindMetadata.typeAliases = kotlinTypeAliasMetadataList;
    kotlinMultiFilePartKindMetadata.localDelegatedProperties = kotlinPropertyMetadataList2;

    // Act
    kotlinShrinker.visitKotlinMultiFilePartMetadata(clazz, kotlinMultiFilePartKindMetadata);

    // Assert
    assertTrue(kotlinMultiFilePartKindMetadata.functions.isEmpty());
    assertTrue(kotlinMultiFilePartKindMetadata.localDelegatedProperties.isEmpty());
    assertTrue(kotlinMultiFilePartKindMetadata.properties.isEmpty());
    assertTrue(kotlinMultiFilePartKindMetadata.typeAliases.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata,
   * KotlinPropertyMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinPropertyMetadata#contextReceiverTypesAccept(Clazz,
   *       KotlinMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitAnyProperty(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata); then calls contextReceiverTypesAccept(Clazz, KotlinMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)"
  })
  void testVisitAnyProperty_thenCallsContextReceiverTypesAccept() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinDeclarationContainerMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");

    KotlinPropertyMetadata kotlinPropertyMetadata = mock(KotlinPropertyMetadata.class);
    doNothing()
        .when(kotlinPropertyMetadata)
        .contextReceiverTypesAccept(
            Mockito.<Clazz>any(), Mockito.<KotlinMetadata>any(), Mockito.<KotlinTypeVisitor>any());
    doNothing()
        .when(kotlinPropertyMetadata)
        .receiverTypeAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinDeclarationContainerMetadata>any(),
            Mockito.<KotlinTypeVisitor>any());
    doNothing()
        .when(kotlinPropertyMetadata)
        .setterParametersAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinDeclarationContainerMetadata>any(),
            Mockito.<KotlinValueParameterVisitor>any());
    doNothing()
        .when(kotlinPropertyMetadata)
        .typeAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinDeclarationContainerMetadata>any(),
            Mockito.<KotlinTypeVisitor>any());
    doNothing()
        .when(kotlinPropertyMetadata)
        .typeParametersAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinDeclarationContainerMetadata>any(),
            Mockito.<KotlinTypeParameterVisitor>any());
    doNothing()
        .when(kotlinPropertyMetadata)
        .versionRequirementAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinDeclarationContainerMetadata>any(),
            Mockito.<KotlinVersionRequirementVisitor>any());

    // Act
    kotlinShrinker.visitAnyProperty(
        clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);

    // Assert
    verify(kotlinPropertyMetadata)
        .contextReceiverTypesAccept(
            isA(Clazz.class), isA(KotlinMetadata.class), isA(KotlinTypeVisitor.class));
    verify(kotlinPropertyMetadata)
        .receiverTypeAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinTypeVisitor.class));
    verify(kotlinPropertyMetadata)
        .setterParametersAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinValueParameterVisitor.class));
    verify(kotlinPropertyMetadata)
        .typeAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinTypeVisitor.class));
    verify(kotlinPropertyMetadata)
        .typeParametersAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinTypeParameterVisitor.class));
    verify(kotlinPropertyMetadata, atLeast(1))
        .versionRequirementAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinVersionRequirementVisitor.class));
  }

  /**
   * Test {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}.
   *
   * <ul>
   *   <li>Given {@link KotlinVersionRequirementMetadata} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata); given KotlinVersionRequirementMetadata (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)"
  })
  void testVisitConstructor_givenKotlinVersionRequirementMetadata() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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

    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        mock(KotlinValueParameterMetadata.class);
    doNothing()
        .when(kotlinValueParameterMetadata)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinClassKindMetadata>any(),
            Mockito.<KotlinConstructorMetadata>any(),
            Mockito.<KotlinValueParameterVisitor>any());

    ArrayList<KotlinValueParameterMetadata> kotlinValueParameterMetadataList = new ArrayList<>();
    kotlinValueParameterMetadataList.add(kotlinValueParameterMetadata);
    KotlinConstructorMetadata kotlinConstructorMetadata = new KotlinConstructorMetadata(flags);
    kotlinConstructorMetadata.valueParameters = kotlinValueParameterMetadataList;
    kotlinConstructorMetadata.versionRequirement = new KotlinVersionRequirementMetadata();

    // Act
    kotlinShrinker.visitConstructor(clazz, kotlinClassKindMetadata, kotlinConstructorMetadata);

    // Assert
    verify(kotlinValueParameterMetadata)
        .accept(
            isA(Clazz.class),
            isA(KotlinClassKindMetadata.class),
            isA(KotlinConstructorMetadata.class),
            isA(KotlinValueParameterVisitor.class));
  }

  /**
   * Test {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}.
   *
   * <ul>
   *   <li>Given {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata); given 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)"
  })
  void testVisitConstructor_givenNull() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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

    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        mock(KotlinValueParameterMetadata.class);
    doNothing()
        .when(kotlinValueParameterMetadata)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinClassKindMetadata>any(),
            Mockito.<KotlinConstructorMetadata>any(),
            Mockito.<KotlinValueParameterVisitor>any());

    ArrayList<KotlinValueParameterMetadata> kotlinValueParameterMetadataList = new ArrayList<>();
    kotlinValueParameterMetadataList.add(kotlinValueParameterMetadata);
    KotlinConstructorMetadata kotlinConstructorMetadata = new KotlinConstructorMetadata(flags);
    kotlinConstructorMetadata.valueParameters = kotlinValueParameterMetadataList;
    kotlinConstructorMetadata.versionRequirement = null;

    // Act
    kotlinShrinker.visitConstructor(clazz, kotlinClassKindMetadata, kotlinConstructorMetadata);

    // Assert
    verify(kotlinValueParameterMetadata)
        .accept(
            isA(Clazz.class),
            isA(KotlinClassKindMetadata.class),
            isA(KotlinConstructorMetadata.class),
            isA(KotlinValueParameterVisitor.class));
  }

  /**
   * Test {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinVersionRequirementMetadata#accept(Clazz, KotlinMetadata,
   *       KotlinConstructorMetadata, KotlinVersionRequirementVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata); then calls accept(Clazz, KotlinMetadata, KotlinConstructorMetadata, KotlinVersionRequirementVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)"
  })
  void testVisitConstructor_thenCallsAccept() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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

    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        mock(KotlinValueParameterMetadata.class);
    doNothing()
        .when(kotlinValueParameterMetadata)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinClassKindMetadata>any(),
            Mockito.<KotlinConstructorMetadata>any(),
            Mockito.<KotlinValueParameterVisitor>any());

    ArrayList<KotlinValueParameterMetadata> kotlinValueParameterMetadataList = new ArrayList<>();
    kotlinValueParameterMetadataList.add(kotlinValueParameterMetadata);

    KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata =
        mock(KotlinVersionRequirementMetadata.class);
    doNothing()
        .when(kotlinVersionRequirementMetadata)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinMetadata>any(),
            Mockito.<KotlinConstructorMetadata>any(),
            Mockito.<KotlinVersionRequirementVisitor>any());
    KotlinConstructorMetadata kotlinConstructorMetadata = new KotlinConstructorMetadata(flags);
    kotlinConstructorMetadata.valueParameters = kotlinValueParameterMetadataList;
    kotlinConstructorMetadata.versionRequirement = kotlinVersionRequirementMetadata;

    // Act
    kotlinShrinker.visitConstructor(clazz, kotlinClassKindMetadata, kotlinConstructorMetadata);

    // Assert
    verify(kotlinValueParameterMetadata)
        .accept(
            isA(Clazz.class),
            isA(KotlinClassKindMetadata.class),
            isA(KotlinConstructorMetadata.class),
            isA(KotlinValueParameterVisitor.class));
    verify(kotlinVersionRequirementMetadata)
        .accept(
            isA(Clazz.class),
            isA(KotlinMetadata.class),
            isA(KotlinConstructorMetadata.class),
            isA(KotlinVersionRequirementVisitor.class));
  }

  /**
   * Test {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinConstructorMetadata#valueParametersAccept(Clazz,
   *       KotlinClassKindMetadata, KotlinValueParameterVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata); then calls valueParametersAccept(Clazz, KotlinClassKindMetadata, KotlinValueParameterVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)"
  })
  void testVisitConstructor_thenCallsValueParametersAccept() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");

    KotlinConstructorMetadata kotlinConstructorMetadata = mock(KotlinConstructorMetadata.class);
    doNothing()
        .when(kotlinConstructorMetadata)
        .valueParametersAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinClassKindMetadata>any(),
            Mockito.<KotlinValueParameterVisitor>any());
    doNothing()
        .when(kotlinConstructorMetadata)
        .versionRequirementAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinMetadata>any(),
            Mockito.<KotlinVersionRequirementVisitor>any());

    // Act
    kotlinShrinker.visitConstructor(clazz, kotlinClassKindMetadata, kotlinConstructorMetadata);

    // Assert
    verify(kotlinConstructorMetadata)
        .valueParametersAccept(
            isA(Clazz.class),
            isA(KotlinClassKindMetadata.class),
            isA(KotlinValueParameterVisitor.class));
    verify(kotlinConstructorMetadata)
        .versionRequirementAccept(
            isA(Clazz.class),
            isA(KotlinMetadata.class),
            isA(KotlinVersionRequirementVisitor.class));
  }

  /**
   * Test {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}
   */
  @Test
  @DisplayName("Test visitAnyType(Clazz, KotlinTypeMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.visitAnyType(Clazz, KotlinTypeMetadata)"})
  void testVisitAnyType() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    KotlinTypeFlags flags = new KotlinTypeFlags();
    flags.isDefinitelyNonNull = true;
    flags.isNullable = true;
    flags.isSuspend = true;

    ArrayList<KotlinTypeMetadata> kotlinTypeMetadataList = new ArrayList<>();
    kotlinTypeMetadataList.add(KotlinTypeMetadata.starProjection());
    KotlinTypeMetadata kotlinTypeMetadata = new KotlinTypeMetadata(flags);
    kotlinTypeMetadata.typeArguments = kotlinTypeMetadataList;
    kotlinTypeMetadata.upperBounds = new ArrayList<>();
    kotlinTypeMetadata.abbreviation = null;
    kotlinTypeMetadata.annotations = new ArrayList<>();

    // Act
    kotlinShrinker.visitAnyType(clazz, kotlinTypeMetadata);

    // Assert that nothing has changed
    assertTrue(kotlinTypeMetadata.annotations.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}
   */
  @Test
  @DisplayName("Test visitAnyType(Clazz, KotlinTypeMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.visitAnyType(Clazz, KotlinTypeMetadata)"})
  void testVisitAnyType2() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    KotlinTypeFlags flags = new KotlinTypeFlags();
    flags.isDefinitelyNonNull = true;
    flags.isNullable = true;
    flags.isSuspend = true;

    ArrayList<KotlinTypeMetadata> kotlinTypeMetadataList = new ArrayList<>();
    kotlinTypeMetadataList.add(KotlinTypeMetadata.starProjection());
    KotlinTypeMetadata kotlinTypeMetadata = new KotlinTypeMetadata(flags);
    kotlinTypeMetadata.typeArguments = kotlinTypeMetadataList;
    kotlinTypeMetadata.upperBounds = null;
    kotlinTypeMetadata.abbreviation = null;
    kotlinTypeMetadata.annotations = new ArrayList<>();

    // Act
    kotlinShrinker.visitAnyType(clazz, kotlinTypeMetadata);

    // Assert that nothing has changed
    assertTrue(kotlinTypeMetadata.annotations.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}.
   *
   * <p>Method under test: {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}
   */
  @Test
  @DisplayName("Test visitAnyType(Clazz, KotlinTypeMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.visitAnyType(Clazz, KotlinTypeMetadata)"})
  void testVisitAnyType3() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    KotlinTypeFlags flags = new KotlinTypeFlags();
    flags.isDefinitelyNonNull = true;
    flags.isNullable = true;
    flags.isSuspend = true;
    KotlinTypeMetadata kotlinTypeMetadata = new KotlinTypeMetadata(flags);
    kotlinTypeMetadata.typeArguments = null;
    kotlinTypeMetadata.upperBounds = new ArrayList<>();
    kotlinTypeMetadata.abbreviation = null;
    kotlinTypeMetadata.annotations = new ArrayList<>();

    // Act
    kotlinShrinker.visitAnyType(clazz, kotlinTypeMetadata);

    // Assert that nothing has changed
    assertTrue(kotlinTypeMetadata.annotations.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}.
   *
   * <ul>
   *   <li>Given {@link ArrayList#ArrayList()} add {@link KotlinAnnotation#KotlinAnnotation(String)}
   *       with {@code Class Name}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyType(Clazz, KotlinTypeMetadata); given ArrayList() add KotlinAnnotation(String) with 'Class Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.visitAnyType(Clazz, KotlinTypeMetadata)"})
  void testVisitAnyType_givenArrayListAddKotlinAnnotationWithClassName() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    KotlinTypeFlags flags = new KotlinTypeFlags();
    flags.isDefinitelyNonNull = true;
    flags.isNullable = true;
    flags.isSuspend = true;

    ArrayList<KotlinTypeMetadata> kotlinTypeMetadataList = new ArrayList<>();
    kotlinTypeMetadataList.add(KotlinTypeMetadata.starProjection());

    ArrayList<KotlinAnnotation> kotlinAnnotationList = new ArrayList<>();
    kotlinAnnotationList.add(new KotlinAnnotation("Class Name"));
    KotlinTypeMetadata kotlinTypeMetadata = new KotlinTypeMetadata(flags);
    kotlinTypeMetadata.typeArguments = kotlinTypeMetadataList;
    kotlinTypeMetadata.upperBounds = new ArrayList<>();
    kotlinTypeMetadata.abbreviation = null;
    kotlinTypeMetadata.annotations = kotlinAnnotationList;

    // Act
    kotlinShrinker.visitAnyType(clazz, kotlinTypeMetadata);

    // Assert
    assertTrue(kotlinTypeMetadata.annotations.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}.
   *
   * <ul>
   *   <li>Given {@link KotlinShrinker#KotlinShrinker(SimpleUsageMarker)} with usageMarker is {@link
   *       ShortestUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitAnyType(Clazz, KotlinTypeMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyType(Clazz, KotlinTypeMetadata); given KotlinShrinker(SimpleUsageMarker) with usageMarker is ShortestUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.visitAnyType(Clazz, KotlinTypeMetadata)"})
  void testVisitAnyType_givenKotlinShrinkerWithUsageMarkerIsShortestUsageMarker() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    KotlinTypeFlags flags = new KotlinTypeFlags();
    flags.isDefinitelyNonNull = true;
    flags.isNullable = true;
    flags.isSuspend = true;

    ArrayList<KotlinTypeMetadata> kotlinTypeMetadataList = new ArrayList<>();
    kotlinTypeMetadataList.add(KotlinTypeMetadata.starProjection());
    kotlinTypeMetadataList.add(KotlinTypeMetadata.starProjection());

    ArrayList<KotlinAnnotation> kotlinAnnotationList = new ArrayList<>();
    kotlinAnnotationList.add(new KotlinAnnotation("Class Name"));
    KotlinTypeMetadata kotlinTypeMetadata = new KotlinTypeMetadata(flags);
    kotlinTypeMetadata.typeArguments = kotlinTypeMetadataList;
    kotlinTypeMetadata.upperBounds = new ArrayList<>();
    kotlinTypeMetadata.abbreviation = null;
    kotlinTypeMetadata.annotations = kotlinAnnotationList;

    // Act
    kotlinShrinker.visitAnyType(clazz, kotlinTypeMetadata);

    // Assert
    assertTrue(kotlinTypeMetadata.annotations.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#visitConstructorValParameter(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata, KotlinValueParameterMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinValueParameterMetadata#typeAccept(Clazz, KotlinClassKindMetadata,
   *       KotlinConstructorMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitConstructorValParameter(Clazz,
   * KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinValueParameterMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitConstructorValParameter(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinValueParameterMetadata); then calls typeAccept(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitConstructorValParameter(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinValueParameterMetadata)"
  })
  void testVisitConstructorValParameter_thenCallsTypeAccept() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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
    KotlinConstructorMetadata kotlinConstructorMetadata =
        new KotlinConstructorMetadata(new KotlinConstructorFlags(visibility));

    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        mock(KotlinValueParameterMetadata.class);
    doNothing()
        .when(kotlinValueParameterMetadata)
        .typeAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinClassKindMetadata>any(),
            Mockito.<KotlinConstructorMetadata>any(),
            Mockito.<KotlinTypeVisitor>any());

    // Act
    kotlinShrinker.visitConstructorValParameter(
        clazz, kotlinClassKindMetadata, kotlinConstructorMetadata, kotlinValueParameterMetadata);

    // Assert
    verify(kotlinValueParameterMetadata)
        .typeAccept(
            isA(Clazz.class),
            isA(KotlinClassKindMetadata.class),
            isA(KotlinConstructorMetadata.class),
            isA(KotlinTypeVisitor.class));
  }

  /**
   * Test {@link KotlinShrinker#visitPropertyValParameter(Clazz, KotlinDeclarationContainerMetadata,
   * KotlinPropertyMetadata, KotlinValueParameterMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinValueParameterMetadata#typeAccept(Clazz,
   *       KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#visitPropertyValParameter(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinValueParameterMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitPropertyValParameter(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinValueParameterMetadata); then calls typeAccept(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinShrinker.visitPropertyValParameter(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinValueParameterMetadata)"
  })
  void testVisitPropertyValParameter_thenCallsTypeAccept() {
    // Arrange
    KotlinShrinker kotlinShrinker = new KotlinShrinker(new SimpleUsageMarker());
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

    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        mock(KotlinValueParameterMetadata.class);
    doNothing()
        .when(kotlinValueParameterMetadata)
        .typeAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinDeclarationContainerMetadata>any(),
            Mockito.<KotlinPropertyMetadata>any(),
            Mockito.<KotlinTypeVisitor>any());

    // Act
    kotlinShrinker.visitPropertyValParameter(
        clazz,
        kotlinDeclarationContainerMetadata,
        kotlinPropertyMetadata,
        kotlinValueParameterMetadata);

    // Assert
    verify(kotlinValueParameterMetadata)
        .typeAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinPropertyMetadata.class),
            isA(KotlinTypeVisitor.class));
  }

  /**
   * Test {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)} with {@code
   * usageMarker}, {@code elements}, {@code referencedJavaElements}.
   *
   * <p>Method under test: {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)}
   */
  @Test
  @DisplayName(
      "Test shrinkArray(SimpleUsageMarker, List, List) with 'usageMarker', 'elements', 'referencedJavaElements'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.shrinkArray(SimpleUsageMarker, List, List)"})
  void testShrinkArrayWithUsageMarkerElementsReferencedJavaElements() {
    // Arrange
    SimpleUsageMarker usageMarker = new SimpleUsageMarker();

    ArrayList<Object> elements = new ArrayList<>();
    elements.add("42");

    ArrayList<Processable> referencedJavaElements = new ArrayList<>();
    referencedJavaElements.add(new SimpleProcessable());

    // Act
    KotlinShrinker.shrinkArray(usageMarker, elements, referencedJavaElements);

    // Assert
    assertTrue(elements.isEmpty());
    assertTrue(referencedJavaElements.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)} with {@code
   * usageMarker}, {@code elements}, {@code referencedJavaElements}.
   *
   * <p>Method under test: {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)}
   */
  @Test
  @DisplayName(
      "Test shrinkArray(SimpleUsageMarker, List, List) with 'usageMarker', 'elements', 'referencedJavaElements'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.shrinkArray(SimpleUsageMarker, List, List)"})
  void testShrinkArrayWithUsageMarkerElementsReferencedJavaElements2() {
    // Arrange
    SimpleUsageMarker usageMarker = new SimpleUsageMarker();

    ArrayList<Object> elements = new ArrayList<>();
    elements.add("42");

    ArrayList<Processable> referencedJavaElements = new ArrayList<>();
    referencedJavaElements.add(new SimpleProcessable());
    referencedJavaElements.add(new SimpleProcessable());

    // Act
    KotlinShrinker.shrinkArray(usageMarker, elements, referencedJavaElements);

    // Assert
    assertEquals(1, referencedJavaElements.size());
    assertTrue(elements.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)} with {@code
   * usageMarker}, {@code elements}, {@code referencedJavaElements}.
   *
   * <p>Method under test: {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)}
   */
  @Test
  @DisplayName(
      "Test shrinkArray(SimpleUsageMarker, List, List) with 'usageMarker', 'elements', 'referencedJavaElements'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.shrinkArray(SimpleUsageMarker, List, List)"})
  void testShrinkArrayWithUsageMarkerElementsReferencedJavaElements3() {
    // Arrange
    SimpleUsageMarker usageMarker = new SimpleUsageMarker();

    ArrayList<Object> elements = new ArrayList<>();
    elements.add("42");

    ArrayList<Processable> referencedJavaElements = new ArrayList<>();
    referencedJavaElements.add(new SimpleProcessable());
    referencedJavaElements.add(new SimpleProcessable());
    referencedJavaElements.add(new SimpleProcessable());

    // Act
    KotlinShrinker.shrinkArray(usageMarker, elements, referencedJavaElements);

    // Assert
    assertEquals(2, referencedJavaElements.size());
    assertTrue(elements.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)} with {@code
   * usageMarker}, {@code elements}, {@code referencedJavaElements}.
   *
   * <p>Method under test: {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)}
   */
  @Test
  @DisplayName(
      "Test shrinkArray(SimpleUsageMarker, List, List) with 'usageMarker', 'elements', 'referencedJavaElements'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.shrinkArray(SimpleUsageMarker, List, List)"})
  void testShrinkArrayWithUsageMarkerElementsReferencedJavaElements4() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();

    ArrayList<Object> elements = new ArrayList<>();
    elements.add("42");

    ArrayList<Processable> referencedJavaElements = new ArrayList<>();
    referencedJavaElements.add(new SimpleFeatureNamedProcessable());

    // Act
    KotlinShrinker.shrinkArray(usageMarker, elements, referencedJavaElements);

    // Assert
    assertTrue(elements.isEmpty());
    assertTrue(referencedJavaElements.isEmpty());
  }

  /**
   * Test {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)} with {@code
   * usageMarker}, {@code elements}, {@code referencedJavaElements}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinShrinker#shrinkArray(SimpleUsageMarker, List, List)}
   */
  @Test
  @DisplayName(
      "Test shrinkArray(SimpleUsageMarker, List, List) with 'usageMarker', 'elements', 'referencedJavaElements'; when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinShrinker.shrinkArray(SimpleUsageMarker, List, List)"})
  void testShrinkArrayWithUsageMarkerElementsReferencedJavaElements_whenArrayList() {
    // Arrange
    SimpleUsageMarker usageMarker = new SimpleUsageMarker();
    ArrayList<Object> elements = new ArrayList<>();
    ArrayList<Processable> referencedJavaElements = new ArrayList<>();

    // Act
    KotlinShrinker.shrinkArray(usageMarker, elements, referencedJavaElements);

    // Assert that nothing has changed
    assertTrue(elements.isEmpty());
    assertTrue(referencedJavaElements.isEmpty());
  }
}
