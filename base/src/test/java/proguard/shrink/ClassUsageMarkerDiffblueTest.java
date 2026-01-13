package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMember;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.ConstantValueAttribute;
import proguard.classfile.attribute.DeprecatedAttribute;
import proguard.classfile.attribute.EnclosingMethodAttribute;
import proguard.classfile.attribute.ExceptionInfo;
import proguard.classfile.attribute.ExceptionsAttribute;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.MethodParametersAttribute;
import proguard.classfile.attribute.NestHostAttribute;
import proguard.classfile.attribute.NestMembersAttribute;
import proguard.classfile.attribute.ParameterInfo;
import proguard.classfile.attribute.PermittedSubclassesAttribute;
import proguard.classfile.attribute.RecordAttribute;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.attribute.SourceDebugExtensionAttribute;
import proguard.classfile.attribute.SourceDirAttribute;
import proguard.classfile.attribute.SourceFileAttribute;
import proguard.classfile.attribute.SyntheticAttribute;
import proguard.classfile.attribute.UnknownAttribute;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.annotation.AnnotationElementValue;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.ArrayElementValue;
import proguard.classfile.attribute.annotation.ClassElementValue;
import proguard.classfile.attribute.annotation.ConstantElementValue;
import proguard.classfile.attribute.annotation.EnumConstantElementValue;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.attribute.module.ExportsInfo;
import proguard.classfile.attribute.module.ModuleAttribute;
import proguard.classfile.attribute.module.ModuleMainClassAttribute;
import proguard.classfile.attribute.module.ModulePackagesAttribute;
import proguard.classfile.attribute.module.OpensInfo;
import proguard.classfile.attribute.module.ProvidesInfo;
import proguard.classfile.attribute.module.RequiresInfo;
import proguard.classfile.attribute.preverification.DoubleType;
import proguard.classfile.attribute.preverification.FullFrame;
import proguard.classfile.attribute.preverification.ObjectType;
import proguard.classfile.attribute.preverification.SameOneFrame;
import proguard.classfile.attribute.preverification.StackMapAttribute;
import proguard.classfile.attribute.preverification.StackMapFrame;
import proguard.classfile.attribute.preverification.StackMapTableAttribute;
import proguard.classfile.attribute.preverification.VerificationType;
import proguard.classfile.attribute.preverification.VerificationTypeFactory;
import proguard.classfile.attribute.preverification.visitor.VerificationTypeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.InnerClassesInfoVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.DoubleConstant;
import proguard.classfile.constant.DynamicConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.FloatConstant;
import proguard.classfile.constant.IntegerConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.InvokeDynamicConstant;
import proguard.classfile.constant.LongConstant;
import proguard.classfile.constant.MethodHandleConstant;
import proguard.classfile.constant.MethodTypeConstant;
import proguard.classfile.constant.ModuleConstant;
import proguard.classfile.constant.NameAndTypeConstant;
import proguard.classfile.constant.PackageConstant;
import proguard.classfile.constant.PrimitiveArrayConstant;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.constant.visitor.BootstrapMethodHandleTraveler;
import proguard.classfile.constant.visitor.ConstantTagFilter;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.CodeAttributeEditor.Label;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinAnnotatable;
import proguard.classfile.kotlin.KotlinAnnotation;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinEffectExpressionMetadata;
import proguard.classfile.kotlin.KotlinEffectInvocationKind;
import proguard.classfile.kotlin.KotlinEffectMetadata;
import proguard.classfile.kotlin.KotlinEffectType;
import proguard.classfile.kotlin.KotlinFileFacadeKindMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata.Flavor;
import proguard.classfile.kotlin.KotlinTypeAliasMetadata;
import proguard.classfile.kotlin.KotlinTypeParameterMetadata;
import proguard.classfile.kotlin.KotlinTypeVariance;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.classfile.kotlin.KotlinVersionRequirementMetadata;
import proguard.classfile.kotlin.flags.KotlinConstructorFlags;
import proguard.classfile.kotlin.flags.KotlinFunctionFlags;
import proguard.classfile.kotlin.flags.KotlinModalityFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyAccessorFlags;
import proguard.classfile.kotlin.flags.KotlinPropertyFlags;
import proguard.classfile.kotlin.flags.KotlinTypeParameterFlags;
import proguard.classfile.kotlin.flags.KotlinVisibilityFlags;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeAliasVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.ClassRenamer;
import proguard.obfuscate.MemberNameCleaner;
import proguard.resources.file.ResourceFile;
import proguard.shrink.ClassUsageMarker.KotlinUsageMarker;
import proguard.shrink.ClassUsageMarker.MarkingMode;
import proguard.testutils.cpa.NamedMember;
import proguard.util.Processable;
import proguard.util.SimpleProcessable;

class ClassUsageMarkerDiffblueTest {
  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitAnyAnnotation(Clazz, KotlinAnnotatable,
   * KotlinAnnotation)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitAnyAnnotation(Clazz, KotlinAnnotatable,
   * KotlinAnnotation)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitAnyAnnotation(Clazz, KotlinAnnotatable, KotlinAnnotation); then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitAnyAnnotation(Clazz, KotlinAnnotatable, KotlinAnnotation)"
  })
  void testKotlinUsageMarkerVisitAnyAnnotation_thenCallsIsUsed() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();
    KotlinAnnotatable annotatable = mock(KotlinAnnotatable.class);

    // Act
    kotlinUsageMarker.visitAnyAnnotation(clazz, annotatable, new KotlinAnnotation("Class Name"));

    // Assert
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitAnyEffectExpression(Clazz,
   * KotlinEffectMetadata, KotlinEffectExpressionMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitAnyEffectExpression(Clazz,
   * KotlinEffectMetadata, KotlinEffectExpressionMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitAnyEffectExpression(Clazz, KotlinEffectMetadata, KotlinEffectExpressionMetadata); then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitAnyEffectExpression(Clazz, KotlinEffectMetadata, KotlinEffectExpressionMetadata)"
  })
  void testKotlinUsageMarkerVisitAnyEffectExpression_thenCallsIsUsed() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();
    KotlinEffectMetadata kotlinEffectMetadata =
        new KotlinEffectMetadata(
            KotlinEffectType.RETURNS_CONSTANT, KotlinEffectInvocationKind.AT_MOST_ONCE);

    // Act
    kotlinUsageMarker.visitAnyEffectExpression(
        clazz, kotlinEffectMetadata, new KotlinEffectExpressionMetadata());

    // Assert
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitAnyTypeParameter(Clazz,
   * KotlinTypeParameterMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitAnyTypeParameter(Clazz,
   * KotlinTypeParameterMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitAnyTypeParameter(Clazz, KotlinTypeParameterMetadata); then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitAnyTypeParameter(Clazz, KotlinTypeParameterMetadata)"
  })
  void testKotlinUsageMarkerVisitAnyTypeParameter_thenCallsIsUsed() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();
    KotlinTypeParameterFlags flags = new KotlinTypeParameterFlags();
    flags.isReified = true;
    KotlinTypeParameterMetadata kotlinTypeParameterMetadata =
        new KotlinTypeParameterMetadata(flags, "Name", 1, KotlinTypeVariance.INVARIANT);

    // Act
    kotlinUsageMarker.visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);

    // Assert
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitAnyVersionRequirement(Clazz,
   * KotlinVersionRequirementMetadata)}.
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitAnyVersionRequirement(Clazz,
   * KotlinVersionRequirementMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitAnyVersionRequirement(Clazz, KotlinVersionRequirementMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitAnyVersionRequirement(Clazz, KotlinVersionRequirementMetadata)"
  })
  void testKotlinUsageMarkerVisitAnyVersionRequirement() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    KotlinUsageMarker kotlinUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause").new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();
    KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata =
        new KotlinVersionRequirementMetadata();

    // Act
    kotlinUsageMarker.visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);

    // Assert
    assertSame(usageMarker.currentUsageMark, kotlinVersionRequirementMetadata.getProcessingInfo());
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitConstructorValParameter(Clazz,
   * KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinValueParameterMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinValueParameterMetadata#typeAccept(Clazz, KotlinClassKindMetadata,
   *       KotlinConstructorMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitConstructorValParameter(Clazz,
   * KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinValueParameterMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitConstructorValParameter(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinValueParameterMetadata); then calls typeAccept(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitConstructorValParameter(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata, KotlinValueParameterMetadata)"
  })
  void testKotlinUsageMarkerVisitConstructorValParameter_thenCallsTypeAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
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
    kotlinUsageMarker.visitConstructorValParameter(
        clazz, kotlinClassKindMetadata, kotlinConstructorMetadata, kotlinValueParameterMetadata);

    // Assert
    verify(kotlinValueParameterMetadata)
        .typeAccept(
            isA(Clazz.class),
            isA(KotlinClassKindMetadata.class),
            isA(KotlinConstructorMetadata.class),
            isA(KotlinTypeVisitor.class));
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitConstructor(Clazz,
   * KotlinClassKindMetadata, KotlinConstructorMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassUsageMarker#isUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitConstructor(Clazz, KotlinClassKindMetadata,
   * KotlinConstructorMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata); then calls isUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)"
  })
  void testKotlinUsageMarkerVisitConstructor_thenCallsIsUsed() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
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

    // Act
    kotlinUsageMarker.visitConstructor(
        clazz,
        kotlinClassKindMetadata,
        new KotlinConstructorMetadata(new KotlinConstructorFlags(visibility)));

    // Assert
    verify(classUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitFunctionValParameter(Clazz,
   * KotlinMetadata, KotlinFunctionMetadata, KotlinValueParameterMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinValueParameterMetadata#typeAccept(Clazz, KotlinMetadata,
   *       KotlinFunctionMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitFunctionValParameter(Clazz, KotlinMetadata,
   * KotlinFunctionMetadata, KotlinValueParameterMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitFunctionValParameter(Clazz, KotlinMetadata, KotlinFunctionMetadata, KotlinValueParameterMetadata); then calls typeAccept(Clazz, KotlinMetadata, KotlinFunctionMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitFunctionValParameter(Clazz, KotlinMetadata, KotlinFunctionMetadata, KotlinValueParameterMetadata)"
  })
  void testKotlinUsageMarkerVisitFunctionValParameter_thenCallsTypeAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
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
    KotlinFunctionMetadata kotlinFunctionMetadata = new KotlinFunctionMetadata(flags, "Name");

    KotlinValueParameterMetadata kotlinValueParameterMetadata =
        mock(KotlinValueParameterMetadata.class);
    doNothing()
        .when(kotlinValueParameterMetadata)
        .typeAccept(
            Mockito.<Clazz>any(),
            Mockito.<KotlinMetadata>any(),
            Mockito.<KotlinFunctionMetadata>any(),
            Mockito.<KotlinTypeVisitor>any());

    // Act
    kotlinUsageMarker.visitFunctionValParameter(
        clazz, kotlinMetadata, kotlinFunctionMetadata, kotlinValueParameterMetadata);

    // Assert
    verify(kotlinValueParameterMetadata)
        .typeAccept(
            isA(Clazz.class),
            isA(KotlinMetadata.class),
            isA(KotlinFunctionMetadata.class),
            isA(KotlinTypeVisitor.class));
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testKotlinUsageMarkerVisitKotlinDeclarationContainerMetadata() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata =
        mock(KotlinDeclarationContainerMetadata.class);
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .delegatedPropertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .propertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .typeAliasesAccept(Mockito.<Clazz>any(), Mockito.<KotlinTypeAliasVisitor>any());

    // Act
    kotlinUsageMarker.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    verify(kotlinDeclarationContainerMetadata)
        .delegatedPropertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .propertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .typeAliasesAccept(isA(Clazz.class), isA(KotlinTypeAliasVisitor.class));
    verify(classUsageMarker, atLeast(1)).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitKotlinFileFacadeMetadata(Clazz,
   * KotlinFileFacadeKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitKotlinFileFacadeMetadata(Clazz,
   * KotlinFileFacadeKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)"
  })
  void testKotlinUsageMarkerVisitKotlinFileFacadeMetadata() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata =
        mock(KotlinFileFacadeKindMetadata.class);
    doNothing()
        .when(kotlinFileFacadeKindMetadata)
        .delegatedPropertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());
    doNothing()
        .when(kotlinFileFacadeKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());
    doNothing()
        .when(kotlinFileFacadeKindMetadata)
        .propertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());
    doNothing()
        .when(kotlinFileFacadeKindMetadata)
        .typeAliasesAccept(Mockito.<Clazz>any(), Mockito.<KotlinTypeAliasVisitor>any());

    // Act
    kotlinUsageMarker.visitKotlinFileFacadeMetadata(clazz, kotlinFileFacadeKindMetadata);

    // Assert
    verify(kotlinFileFacadeKindMetadata)
        .delegatedPropertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
    verify(kotlinFileFacadeKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(kotlinFileFacadeKindMetadata)
        .propertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
    verify(kotlinFileFacadeKindMetadata)
        .typeAliasesAccept(isA(Clazz.class), isA(KotlinTypeAliasVisitor.class));
    verify(classUsageMarker, atLeast(1)).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testKotlinUsageMarkerVisitKotlinSyntheticClassMetadata() {
    // Arrange
    KotlinUsageMarker kotlinUsageMarker = new ClassUsageMarker().new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        mock(KotlinSyntheticClassKindMetadata.class);
    doNothing()
        .when(kotlinSyntheticClassKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());
    doNothing().when(kotlinSyntheticClassKindMetadata).setProcessingInfo(Mockito.<Object>any());

    // Act
    kotlinUsageMarker.visitKotlinSyntheticClassMetadata(clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    verify(kotlinSyntheticClassKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(kotlinSyntheticClassKindMetadata).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testKotlinUsageMarkerVisitKotlinSyntheticClassMetadata2() {
    // Arrange
    KotlinUsageMarker kotlinUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause")
        .new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        mock(KotlinSyntheticClassKindMetadata.class);
    when(kotlinSyntheticClassKindMetadata.getProcessingInfo()).thenReturn("Processing Info");
    doNothing()
        .when(kotlinSyntheticClassKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());
    doNothing().when(kotlinSyntheticClassKindMetadata).setProcessingInfo(Mockito.<Object>any());

    // Act
    kotlinUsageMarker.visitKotlinSyntheticClassMetadata(clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    verify(kotlinSyntheticClassKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(kotlinSyntheticClassKindMetadata).getProcessingInfo();
    verify(kotlinSyntheticClassKindMetadata).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassUsageMarker#markAsUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata); then calls markAsUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testKotlinUsageMarkerVisitKotlinSyntheticClassMetadata_thenCallsMarkAsUsed() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    doNothing().when(classUsageMarker).markAsUsed(Mockito.<Processable>any());
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        mock(KotlinSyntheticClassKindMetadata.class);
    doNothing()
        .when(kotlinSyntheticClassKindMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());

    // Act
    kotlinUsageMarker.visitKotlinSyntheticClassMetadata(clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    verify(kotlinSyntheticClassKindMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(classUsageMarker).markAsUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitPropertyValParameter(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinValueParameterMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinValueParameterMetadata#typeAccept(Clazz,
   *       KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitPropertyValParameter(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinValueParameterMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitPropertyValParameter(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinValueParameterMetadata); then calls typeAccept(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitPropertyValParameter(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata, KotlinValueParameterMetadata)"
  })
  void testKotlinUsageMarkerVisitPropertyValParameter_thenCallsTypeAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
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
    kotlinUsageMarker.visitPropertyValParameter(
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
    verify(classUsageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test KotlinUsageMarker {@link KotlinUsageMarker#visitTypeAlias(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata)}.
   *
   * <ul>
   *   <li>When {@link Clazz}.
   *   <li>Then calls {@link KotlinTypeAliasMetadata#expandedTypeAccept(Clazz,
   *       KotlinDeclarationContainerMetadata, KotlinTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinUsageMarker#visitTypeAlias(Clazz,
   * KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata)}
   */
  @Test
  @DisplayName(
      "Test KotlinUsageMarker visitTypeAlias(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata); when Clazz; then calls expandedTypeAccept(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUsageMarker.visitTypeAlias(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata)"
  })
  void testKotlinUsageMarkerVisitTypeAlias_whenClazz_thenCallsExpandedTypeAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = mock(ClassUsageMarker.class);
    when(classUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(false);
    KotlinUsageMarker kotlinUsageMarker = classUsageMarker.new KotlinUsageMarker();
    Clazz clazz = mock(Clazz.class);
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
    kotlinUsageMarker.visitTypeAlias(
        clazz, kotlinDeclarationContainerMetadata, kotlinTypeAliasMetadata);

    // Assert
    verify(kotlinTypeAliasMetadata)
        .expandedTypeAccept(
            isA(Clazz.class),
            isA(KotlinDeclarationContainerMetadata.class),
            isA(KotlinTypeVisitor.class));
    verify(classUsageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
  }

  /**
   * Test {@link ClassUsageMarker#ClassUsageMarker()}.
   *
   * <p>Method under test: {@link ClassUsageMarker#ClassUsageMarker()}
   */
  @Test
  @DisplayName("Test new ClassUsageMarker()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.<init>()"})
  void testNewClassUsageMarker() {
    // Arrange, Act and Assert
    assertNull(new ClassUsageMarker().getExtraConstantVisitor());
  }

  /**
   * Test {@link ClassUsageMarker#ClassUsageMarker(SimpleUsageMarker)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#ClassUsageMarker(SimpleUsageMarker)}
   */
  @Test
  @DisplayName("Test new ClassUsageMarker(SimpleUsageMarker)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.<init>(SimpleUsageMarker)"})
  void testNewClassUsageMarker2() {
    // Arrange
    SimpleUsageMarker usageMarker = new SimpleUsageMarker();

    // Act
    ClassUsageMarker actualClassUsageMarker = new ClassUsageMarker(usageMarker);

    // Assert
    assertNull(actualClassUsageMarker.getExtraConstantVisitor());
    assertSame(usageMarker, actualClassUsageMarker.getUsageMarker());
  }

  /**
   * Test {@link ClassUsageMarker#ClassUsageMarker(SimpleUsageMarker, MarkingMode)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#ClassUsageMarker(SimpleUsageMarker, MarkingMode)}
   */
  @Test
  @DisplayName("Test new ClassUsageMarker(SimpleUsageMarker, MarkingMode)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.<init>(SimpleUsageMarker, MarkingMode)"})
  void testNewClassUsageMarker3() {
    // Arrange
    SimpleUsageMarker usageMarker = new SimpleUsageMarker();

    // Act
    ClassUsageMarker actualClassUsageMarker =
        new ClassUsageMarker(usageMarker, MarkingMode.SHRINKING);

    // Assert
    assertNull(actualClassUsageMarker.getExtraConstantVisitor());
    assertSame(usageMarker, actualClassUsageMarker.getUsageMarker());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassUsageMarker#setExtraConstantVisitor(ConstantVisitor)}
   *   <li>{@link ClassUsageMarker#setExtraMethodVisitor(MemberVisitor)}
   *   <li>{@link ClassUsageMarker#visitAnnotationElementValue(Clazz, Annotation,
   *       AnnotationElementValue)}
   *   <li>{@link ClassUsageMarker#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}
   *   <li>{@link ClassUsageMarker#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   *       Instruction)}
   *   <li>{@link ClassUsageMarker#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   *       ParameterAnnotationsAttribute)}
   *   <li>{@link ClassUsageMarker#visitAnyStackMapFrame(Clazz, Method, CodeAttribute, int,
   *       StackMapFrame)}
   *   <li>{@link ClassUsageMarker#visitAnyVerificationType(Clazz, Method, CodeAttribute, int,
   *       VerificationType)}
   *   <li>{@link ClassUsageMarker#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}
   *   <li>{@link ClassUsageMarker#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}
   *   <li>{@link ClassUsageMarker#visitClassElementValue(Clazz, Annotation, ClassElementValue)}
   *   <li>{@link ClassUsageMarker#visitConstantElementValue(Clazz, Annotation,
   *       ConstantElementValue)}
   *   <li>{@link ClassUsageMarker#visitLibraryField(LibraryClass, LibraryField)}
   *   <li>{@link ClassUsageMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   *       LocalVariableTableAttribute)}
   *   <li>{@link ClassUsageMarker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   *       CodeAttribute, LocalVariableTypeTableAttribute)}
   *   <li>{@link ClassUsageMarker#visitNestHostAttribute(Clazz, NestHostAttribute)}
   *   <li>{@link ClassUsageMarker#visitNestMembersAttribute(Clazz, NestMembersAttribute)}
   *   <li>{@link ClassUsageMarker#visitPermittedSubclassesAttribute(Clazz,
   *       PermittedSubclassesAttribute)}
   *   <li>{@link ClassUsageMarker#getExtraConstantVisitor()}
   *   <li>{@link ClassUsageMarker#getUsageMarker()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ConstantVisitor ClassUsageMarker.getExtraConstantVisitor()",
    "SimpleUsageMarker ClassUsageMarker.getUsageMarker()",
    "void ClassUsageMarker.setExtraConstantVisitor(ConstantVisitor)",
    "void ClassUsageMarker.setExtraMethodVisitor(MemberVisitor)",
    "void ClassUsageMarker.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)",
    "void ClassUsageMarker.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)",
    "void ClassUsageMarker.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)",
    "void ClassUsageMarker.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)",
    "void ClassUsageMarker.visitAnyStackMapFrame(Clazz, Method, CodeAttribute, int, StackMapFrame)",
    "void ClassUsageMarker.visitAnyVerificationType(Clazz, Method, CodeAttribute, int, VerificationType)",
    "void ClassUsageMarker.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)",
    "void ClassUsageMarker.visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)",
    "void ClassUsageMarker.visitClassElementValue(Clazz, Annotation, ClassElementValue)",
    "void ClassUsageMarker.visitConstantElementValue(Clazz, Annotation, ConstantElementValue)",
    "void ClassUsageMarker.visitLibraryField(LibraryClass, LibraryField)",
    "void ClassUsageMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)",
    "void ClassUsageMarker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)",
    "void ClassUsageMarker.visitNestHostAttribute(Clazz, NestHostAttribute)",
    "void ClassUsageMarker.visitNestMembersAttribute(Clazz, NestMembersAttribute)",
    "void ClassUsageMarker.visitPermittedSubclassesAttribute(Clazz, PermittedSubclassesAttribute)"
  })
  void testGettersAndSetters() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    ClassRenamer extraConstantVisitor = new ClassRenamer();

    // Act
    classUsageMarker.setExtraConstantVisitor(extraConstantVisitor);
    classUsageMarker.setExtraMethodVisitor(new KotlinAnnotationCounter());
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();
    classUsageMarker.visitAnnotationElementValue(clazz, annotation, new AnnotationElementValue());
    LibraryClass clazz2 = new LibraryClass();
    classUsageMarker.visitAnyAnnotationsAttribute(
        clazz2, new RuntimeInvisibleAnnotationsAttribute());
    LibraryClass clazz3 = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    classUsageMarker.visitAnyInstruction(clazz3, method, codeAttribute, 2, new Label(1));
    LibraryClass clazz4 = new LibraryClass();
    LibraryMethod method2 = new LibraryMethod();
    classUsageMarker.visitAnyParameterAnnotationsAttribute(
        clazz4, method2, new RuntimeInvisibleParameterAnnotationsAttribute());
    LibraryClass clazz5 = new LibraryClass();
    LibraryMethod method3 = new LibraryMethod();
    CodeAttribute codeAttribute2 = new CodeAttribute();
    classUsageMarker.visitAnyStackMapFrame(clazz5, method3, codeAttribute2, 2, new FullFrame());
    LibraryClass clazz6 = new LibraryClass();
    LibraryMethod method4 = new LibraryMethod();
    CodeAttribute codeAttribute3 = new CodeAttribute();
    classUsageMarker.visitAnyVerificationType(
        clazz6, method4, codeAttribute3, 2, VerificationTypeFactory.createDoubleType());
    LibraryClass clazz7 = new LibraryClass();
    Annotation annotation2 = new Annotation();
    classUsageMarker.visitArrayElementValue(clazz7, annotation2, new ArrayElementValue());
    LibraryClass clazz8 = new LibraryClass();
    classUsageMarker.visitBootstrapMethodsAttribute(clazz8, new BootstrapMethodsAttribute());
    LibraryClass clazz9 = new LibraryClass();
    Annotation annotation3 = new Annotation();
    classUsageMarker.visitClassElementValue(clazz9, annotation3, new ClassElementValue());
    LibraryClass clazz10 = new LibraryClass();
    Annotation annotation4 = new Annotation();
    classUsageMarker.visitConstantElementValue(clazz10, annotation4, new ConstantElementValue('A'));
    LibraryClass programClass = new LibraryClass();
    classUsageMarker.visitLibraryField(programClass, new LibraryField());
    LibraryClass clazz11 = new LibraryClass();
    LibraryMethod method5 = new LibraryMethod();
    CodeAttribute codeAttribute4 = new CodeAttribute();
    classUsageMarker.visitLocalVariableTableAttribute(
        clazz11, method5, codeAttribute4, new LocalVariableTableAttribute());
    LibraryClass clazz12 = new LibraryClass();
    LibraryMethod method6 = new LibraryMethod();
    CodeAttribute codeAttribute5 = new CodeAttribute();
    classUsageMarker.visitLocalVariableTypeTableAttribute(
        clazz12, method6, codeAttribute5, new LocalVariableTypeTableAttribute());
    LibraryClass clazz13 = new LibraryClass();
    classUsageMarker.visitNestHostAttribute(clazz13, new NestHostAttribute());
    LibraryClass clazz14 = new LibraryClass();
    classUsageMarker.visitNestMembersAttribute(clazz14, new NestMembersAttribute());
    LibraryClass clazz15 = new LibraryClass();
    classUsageMarker.visitPermittedSubclassesAttribute(clazz15, new PermittedSubclassesAttribute());
    ConstantVisitor actualExtraConstantVisitor = classUsageMarker.getExtraConstantVisitor();
    classUsageMarker.getUsageMarker();

    // Assert
    assertTrue(actualExtraConstantVisitor instanceof ClassRenamer);
    assertSame(extraConstantVisitor, actualExtraConstantVisitor);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classUsageMarker.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    shortestClassUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    Object processingInfo = constant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("is extended by   ", ((ShortestUsageMark) processingInfo).getReason());
    assertEquals(1, constantArray.length);
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, programClass.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass2() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    shortestClassUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    Object processingInfo = constant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("is extended by   ", ((ShortestUsageMark) processingInfo).getReason());
    assertEquals(1, constantArray.length);
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, programClass.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass3() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    shortestClassUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    Object processingInfo = constant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("is extended by   ", ((ShortestUsageMark) processingInfo).getReason());
    assertEquals(1, constantArray.length);
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, programClass.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass4() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    shortestClassUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    Object processingInfo = constant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("is extended by   ", ((ShortestUsageMark) processingInfo).getReason());
    assertEquals(1, constantArray.length);
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, programClass.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass5() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    shortestClassUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    Object processingInfo = constant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("is extended by   ", ((ShortestUsageMark) processingInfo).getReason());
    assertEquals(1, constantArray.length);
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, programClass.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementClassConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementDoubleConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementDynamicConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementFieldrefConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementFloatConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof FloatConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementIntegerConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link InterfaceMethodrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then first element InterfaceMethodrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementInterfaceMethodrefConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new InterfaceMethodrefConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof InterfaceMethodrefConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass programClass = new ProgramClass(5, 3, constantPool, 5, 0, 0);

    // Act
    classUsageMarker.visitProgramClass(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[0] instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}
   */
  @Test
  @DisplayName("Test markProgramClassBody(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramClassBody(ProgramClass)"})
  void testMarkProgramClassBody() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    DoubleConstant doubleConstant = new DoubleConstant();
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {doubleConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.markProgramClassBody(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}
   */
  @Test
  @DisplayName("Test markProgramClassBody(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramClassBody(ProgramClass)"})
  void testMarkProgramClassBody2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    DynamicConstant dynamicConstant = new DynamicConstant();
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {dynamicConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.markProgramClassBody(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}
   */
  @Test
  @DisplayName("Test markProgramClassBody(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramClassBody(ProgramClass)"})
  void testMarkProgramClassBody3() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    FieldrefConstant fieldrefConstant = new FieldrefConstant();
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {fieldrefConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.markProgramClassBody(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}
   */
  @Test
  @DisplayName("Test markProgramClassBody(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramClassBody(ProgramClass)"})
  void testMarkProgramClassBody4() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    FloatConstant floatConstant = new FloatConstant();
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {floatConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.markProgramClassBody(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}
   */
  @Test
  @DisplayName("Test markProgramClassBody(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramClassBody(ProgramClass)"})
  void testMarkProgramClassBody5() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    IntegerConstant integerConstant = new IntegerConstant();
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {integerConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.markProgramClassBody(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramClassBody(ProgramClass)}
   */
  @Test
  @DisplayName("Test markProgramClassBody(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramClassBody(ProgramClass)"})
  void testMarkProgramClassBody6() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    InterfaceMethodrefConstant interfaceMethodrefConstant = new InterfaceMethodrefConstant();
    ProgramClass programClass =
        new ProgramClass(
            1, 3, new Constant[] {interfaceMethodrefConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.markProgramClassBody(programClass);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass libraryClass = new LibraryClass(5, "This Class Name", "Super Class Name");

    // Act
    shortestClassUsageMarker.visitLibraryClass(libraryClass);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, libraryClass.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    LibraryClass libraryClass = new LibraryClass(5, "This Class Name", "Super Class Name");

    // Act
    classUsageMarker.visitLibraryClass(libraryClass);

    // Assert that nothing has changed
    assertNull(libraryClass.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given array of {@link Clazz} with {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); given array of Clazz with 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenArrayOfClazzWithNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    LibraryClass libraryClass2 = new LibraryClass(5, "This Class Name", "Super Class Name");
    libraryClass.superClass = libraryClass2;
    libraryClass.interfaceClasses = new Clazz[] {null};

    // Act
    classUsageMarker.visitLibraryClass(libraryClass);

    // Assert
    assertTrue(libraryClass.getSuperClass() instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then first element {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then first element LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenFirstElementLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    LibraryClass libraryClass2 = new LibraryClass(5, "This Class Name", "Super Class Name");
    libraryClass.superClass = libraryClass2;
    LibraryClass libraryClass3 = new LibraryClass(5, "This Class Name", "Super Class Name");
    libraryClass.interfaceClasses = new Clazz[] {libraryClass3};

    // Act
    classUsageMarker.visitLibraryClass(libraryClass);

    // Assert
    assertTrue(libraryClass.getSuperClass() instanceof LibraryClass);
    Clazz[] clazzArray = libraryClass.interfaceClasses;
    assertTrue(clazzArray[0] instanceof LibraryClass);
    assertEquals(1, clazzArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}
   */
  @Test
  @DisplayName("Test visitAnyKotlinMetadata(Clazz, KotlinMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyKotlinMetadata(Clazz, KotlinMetadata)"})
  void testVisitAnyKotlinMetadata() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    KotlinSyntheticClassKindMetadata kotlinMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);

    // Act and Assert
    assertDoesNotThrow(
        () -> shortestClassUsageMarker.visitAnyKotlinMetadata(clazz, kotlinMetadata));
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyKotlinMetadata(Clazz, KotlinMetadata); given ClassUsageMarker(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyKotlinMetadata(Clazz, KotlinMetadata)"})
  void testVisitAnyKotlinMetadata_givenClassUsageMarker_thenDoesNotThrow() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    KotlinSyntheticClassKindMetadata kotlinMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);

    // Act and Assert
    assertDoesNotThrow(() -> classUsageMarker.visitAnyKotlinMetadata(clazz, kotlinMetadata));
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyKotlinMetadata(Clazz, KotlinMetadata); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyKotlinMetadata(Clazz, KotlinMetadata)"})
  void testVisitAnyKotlinMetadata_thenThrowUnsupportedOperationException() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    doThrow(new UnsupportedOperationException())
        .when(usageMarker)
        .markAsUsed(Mockito.<Processable>any());
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    doNothing().when(usageMarker).setCurrentUsageMark(Mockito.<ShortestUsageMark>any());
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    KotlinSyntheticClassKindMetadata kotlinMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> shortestClassUsageMarker.visitAnyKotlinMetadata(clazz, kotlinMetadata));
    verify(usageMarker).isUsed(isA(Processable.class));
    verify(usageMarker).markAsUsed(isA(Processable.class));
    verify(usageMarker).setCurrentUsageMark(isA(ShortestUsageMark.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}.
   *
   * <ul>
   *   <li>When {@link KotlinMetadata} {@link KotlinMetadata#accept(Clazz, KotlinMetadataVisitor)}
   *       does nothing.
   *   <li>Then calls {@link KotlinMetadata#accept(Clazz, KotlinMetadataVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitAnyKotlinMetadata(Clazz, KotlinMetadata); when KotlinMetadata accept(Clazz, KotlinMetadataVisitor) does nothing; then calls accept(Clazz, KotlinMetadataVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyKotlinMetadata(Clazz, KotlinMetadata)"})
  void testVisitAnyKotlinMetadata_whenKotlinMetadataAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    doNothing().when(usageMarker).setCurrentUsageMark(Mockito.<ShortestUsageMark>any());
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();

    KotlinMetadata kotlinMetadata = mock(KotlinMetadata.class);
    doNothing()
        .when(kotlinMetadata)
        .accept(Mockito.<Clazz>any(), Mockito.<KotlinMetadataVisitor>any());

    // Act
    shortestClassUsageMarker.visitAnyKotlinMetadata(clazz, kotlinMetadata);

    // Assert
    verify(kotlinMetadata).accept(isA(Clazz.class), isA(KotlinMetadataVisitor.class));
    verify(usageMarker).isUsed(isA(Processable.class));
    verify(usageMarker).setCurrentUsageMark(isA(ShortestUsageMark.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinDeclarationContainerMetadata#delegatedPropertiesAccept(Clazz,
   *       KotlinPropertyVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitKotlinDeclarationContainerMetadata(Clazz,
   * KotlinDeclarationContainerMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata); then calls delegatedPropertiesAccept(Clazz, KotlinPropertyVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)"
  })
  void testVisitKotlinDeclarationContainerMetadata_thenCallsDelegatedPropertiesAccept() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    doNothing().when(usageMarker).setCurrentUsageMark(Mockito.<ShortestUsageMark>any());
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();

    KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata =
        mock(KotlinDeclarationContainerMetadata.class);
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .accept(Mockito.<Clazz>any(), Mockito.<KotlinMetadataVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .delegatedPropertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .functionsAccept(Mockito.<Clazz>any(), Mockito.<KotlinFunctionVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .propertiesAccept(Mockito.<Clazz>any(), Mockito.<KotlinPropertyVisitor>any());
    doNothing()
        .when(kotlinDeclarationContainerMetadata)
        .typeAliasesAccept(Mockito.<Clazz>any(), Mockito.<KotlinTypeAliasVisitor>any());

    // Act
    shortestClassUsageMarker.visitKotlinDeclarationContainerMetadata(
        clazz, kotlinDeclarationContainerMetadata);

    // Assert
    verify(kotlinDeclarationContainerMetadata)
        .delegatedPropertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .functionsAccept(isA(Clazz.class), isA(KotlinFunctionVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .propertiesAccept(isA(Clazz.class), isA(KotlinPropertyVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .typeAliasesAccept(isA(Clazz.class), isA(KotlinTypeAliasVisitor.class));
    verify(kotlinDeclarationContainerMetadata)
        .accept(isA(Clazz.class), isA(KotlinMetadataVisitor.class));
    verify(usageMarker, atLeast(1)).isUsed(Mockito.<Processable>any());
    verify(usageMarker).setCurrentUsageMark(isA(ShortestUsageMark.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then {@link ProgramField#ProgramField()} ProcessingInfo {@link ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then ProgramField() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenProgramFieldProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    ProgramClass programClass = new ProgramClass();
    ProgramField programField = new ProgramField();

    // Act
    shortestClassUsageMarker.visitProgramField(programClass, programField);

    // Assert
    Object processingInfo = programField.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertFalse(((ShortestUsageMark) processingInfo).isCertain());
  }

  /**
   * Test {@link ClassUsageMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    ProgramClass programClass = new ProgramClass();
    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");

    // Act
    shortestClassUsageMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertFalse(((ShortestUsageMark) processingInfo).isCertain());
  }

  /**
   * Test {@link ClassUsageMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass libraryClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    LibraryMethod libraryMethod = new LibraryMethod(5, "Name", "Descriptor");

    // Act
    shortestClassUsageMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, libraryMethod.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    classUsageMarker.setExtraMethodVisitor(new MemberNameCleaner());
    LibraryClass libraryClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    LibraryMethod libraryMethod = new LibraryMethod(5, "Name", "Descriptor");

    // Act
    classUsageMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    assertNull(libraryMethod.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#markProgramFieldBody(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramFieldBody(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test markProgramFieldBody(ProgramClass, ProgramField); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramFieldBody(ProgramClass, ProgramField)"})
  void testMarkProgramFieldBody_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass programClass = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.markProgramFieldBody(programClass, new ProgramField());

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markProgramMethodBody(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code <init>}.
   *   <li>When {@link ProgramMethod} {@link ProgramMethod#getName(Clazz)} return {@code <init>}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramMethodBody(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test markProgramMethodBody(ProgramClass, ProgramMethod); given '<init>'; when ProgramMethod getName(Clazz) return '<init>'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramMethodBody(ProgramClass, ProgramMethod)"})
  void testMarkProgramMethodBody_givenInit_whenProgramMethodGetNameReturnInit() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    classUsageMarker.setExtraMethodVisitor(new KotlinAnnotationCounter());

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doNothing()
        .when(programMethod)
        .accept(Mockito.<ProgramClass>any(), Mockito.<MemberVisitor>any());
    doNothing().when(programMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    when(programMethod.getName(Mockito.<Clazz>any())).thenReturn("<init>");
    doNothing()
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());
    doNothing().when(programMethod).referencedClassesAccept(Mockito.<ClassVisitor>any());

    // Act
    classUsageMarker.markProgramMethodBody(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
    verify(programMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(programMethod).getName(isA(Clazz.class));
    verify(programMethod).accept(isA(ProgramClass.class), isA(MemberVisitor.class));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
    verify(programMethod).referencedClassesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#markProgramMethodBody(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>When {@link ProgramMethod} {@link ProgramMethod#accept(ProgramClass, MemberVisitor)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramMethodBody(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test markProgramMethodBody(ProgramClass, ProgramMethod); given 'Name'; when ProgramMethod accept(ProgramClass, MemberVisitor) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramMethodBody(ProgramClass, ProgramMethod)"})
  void testMarkProgramMethodBody_givenName_whenProgramMethodAcceptDoesNothing() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    classUsageMarker.setExtraMethodVisitor(new KotlinAnnotationCounter());

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doNothing()
        .when(programMethod)
        .accept(Mockito.<ProgramClass>any(), Mockito.<MemberVisitor>any());
    doNothing().when(programMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    when(programMethod.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing()
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());
    doNothing().when(programMethod).referencedClassesAccept(Mockito.<ClassVisitor>any());

    // Act
    classUsageMarker.markProgramMethodBody(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
    verify(programMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(programMethod).getName(isA(Clazz.class));
    verify(programMethod).accept(isA(ProgramClass.class), isA(MemberVisitor.class));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
    verify(programMethod).referencedClassesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#markProgramMethodBody(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>When {@link ProgramMethod} {@link ProgramMethod#getName(Clazz)} return {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramMethodBody(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test markProgramMethodBody(ProgramClass, ProgramMethod); given 'Name'; when ProgramMethod getName(Clazz) return 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramMethodBody(ProgramClass, ProgramMethod)"})
  void testMarkProgramMethodBody_givenName_whenProgramMethodGetNameReturnName() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doNothing().when(programMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    when(programMethod.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing()
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());
    doNothing().when(programMethod).referencedClassesAccept(Mockito.<ClassVisitor>any());

    // Act
    classUsageMarker.markProgramMethodBody(programClass, programMethod);

    // Assert
    verify(programClass, atLeast(1)).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
    verify(programMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(programMethod).getName(isA(Clazz.class));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
    verify(programMethod).referencedClassesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#markProgramMethodBody(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramMethodBody(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test markProgramMethodBody(ProgramClass, ProgramMethod); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramMethodBody(ProgramClass, ProgramMethod)"})
  void testMarkProgramMethodBody_thenCallsGetString() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).kotlinMetadataAccept(Mockito.<KotlinMetadataVisitor>any());
    when(programClass.getString(anyInt())).thenReturn("String");
    doNothing()
        .when(programClass)
        .constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    // Act
    classUsageMarker.markProgramMethodBody(programClass, new ProgramMethod());

    // Assert
    verify(programClass, atLeast(1)).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
    verify(programClass).getString(0);
    verify(programClass, atLeast(1)).kotlinMetadataAccept(Mockito.<KotlinMetadataVisitor>any());
  }

  /**
   * Test {@link ClassUsageMarker#markProgramMethodBody(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramMethodBody(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test markProgramMethodBody(ProgramClass, ProgramMethod); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramMethodBody(ProgramClass, ProgramMethod)"})
  void testMarkProgramMethodBody_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    classUsageMarker.setExtraMethodVisitor(new KotlinAnnotationCounter());

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doThrow(new UnsupportedOperationException())
        .when(programMethod)
        .accept(Mockito.<ProgramClass>any(), Mockito.<MemberVisitor>any());
    doNothing()
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());
    doNothing().when(programMethod).referencedClassesAccept(Mockito.<ClassVisitor>any());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> classUsageMarker.markProgramMethodBody(programClass, programMethod));
    verify(programClass, atLeast(1)).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
    verify(programMethod).accept(isA(ProgramClass.class), isA(MemberVisitor.class));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
    verify(programMethod).referencedClassesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#markProgramMethodBody(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>When {@link NamedMember#NamedMember(String, String)} with memberName is {@code <init>}
   *       and descriptor is {@code <init>}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markProgramMethodBody(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test markProgramMethodBody(ProgramClass, ProgramMethod); when NamedMember(String, String) with memberName is '<init>' and descriptor is '<init>'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markProgramMethodBody(ProgramClass, ProgramMethod)"})
  void testMarkProgramMethodBody_whenNamedMemberWithMemberNameIsInitAndDescriptorIsInit() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing()
        .when(programClass)
        .constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    // Act
    classUsageMarker.markProgramMethodBody(programClass, new NamedMember("<init>", "<init>"));

    // Assert
    verify(programClass, atLeast(1)).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#markMethodHierarchy(Clazz, Method)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then calls {@link LibraryMethod#getAccessFlags()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markMethodHierarchy(Clazz, Method)}
   */
  @Test
  @DisplayName("Test markMethodHierarchy(Clazz, Method); given one; then calls getAccessFlags()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markMethodHierarchy(Clazz, Method)"})
  void testMarkMethodHierarchy_givenOne_thenCallsGetAccessFlags() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getAccessFlags()).thenReturn(1);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    classUsageMarker.markMethodHierarchy(clazz, method);

    // Assert
    verify(clazz, atLeast(1)).accept(Mockito.<ClassVisitor>any());
    verify(method).getAccessFlags();
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
  }

  /**
   * Test {@link ClassUsageMarker#markMethodHierarchy(Clazz, Method)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markMethodHierarchy(Clazz, Method)}
   */
  @Test
  @DisplayName("Test markMethodHierarchy(Clazz, Method); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markMethodHierarchy(Clazz, Method)"})
  void testMarkMethodHierarchy_givenString_thenCallsGetString() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());

    // Act
    classUsageMarker.markMethodHierarchy(clazz, new ProgramMethod());

    // Assert
    verify(clazz, atLeast(1)).accept(Mockito.<ClassVisitor>any());
    verify(clazz, atLeast(1)).getString(0);
  }

  /**
   * Test {@link ClassUsageMarker#markMethodHierarchy(Clazz, Method)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markMethodHierarchy(Clazz, Method)}
   */
  @Test
  @DisplayName(
      "Test markMethodHierarchy(Clazz, Method); when LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markMethodHierarchy(Clazz, Method)"})
  void testMarkMethodHierarchy_whenLibraryClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());

    // Act
    classUsageMarker.markMethodHierarchy(clazz, new LibraryMethod());

    // Assert
    verify(clazz, atLeast(1)).accept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link ClassUsageMarker#visitIntegerConstant(Clazz, IntegerConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitIntegerConstant(Clazz, IntegerConstant)}
   */
  @Test
  @DisplayName("Test visitIntegerConstant(Clazz, IntegerConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitIntegerConstant(Clazz, IntegerConstant)"})
  void testVisitIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    IntegerConstant integerConstant = new IntegerConstant();

    // Act
    shortestClassUsageMarker.visitIntegerConstant(clazz, integerConstant);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, integerConstant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitIntegerConstant(Clazz, IntegerConstant)}.
   *
   * <ul>
   *   <li>Then {@link IntegerConstant#IntegerConstant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitIntegerConstant(Clazz, IntegerConstant)}
   */
  @Test
  @DisplayName(
      "Test visitIntegerConstant(Clazz, IntegerConstant); then IntegerConstant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitIntegerConstant(Clazz, IntegerConstant)"})
  void testVisitIntegerConstant_thenIntegerConstantProcessingInfoIsNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    classUsageMarker.setExtraConstantVisitor(null);
    LibraryClass clazz = new LibraryClass();
    IntegerConstant integerConstant = new IntegerConstant();

    // Act
    classUsageMarker.visitIntegerConstant(clazz, integerConstant);

    // Assert that nothing has changed
    assertNull(integerConstant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitLongConstant(Clazz, LongConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLongConstant(Clazz, LongConstant)}
   */
  @Test
  @DisplayName("Test visitLongConstant(Clazz, LongConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLongConstant(Clazz, LongConstant)"})
  void testVisitLongConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    LongConstant longConstant = new LongConstant();

    // Act
    shortestClassUsageMarker.visitLongConstant(clazz, longConstant);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, longConstant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitLongConstant(Clazz, LongConstant)}.
   *
   * <ul>
   *   <li>Then {@link LongConstant#LongConstant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLongConstant(Clazz, LongConstant)}
   */
  @Test
  @DisplayName(
      "Test visitLongConstant(Clazz, LongConstant); then LongConstant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitLongConstant(Clazz, LongConstant)"})
  void testVisitLongConstant_thenLongConstantProcessingInfoIsNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LongConstant longConstant = new LongConstant();

    // Act
    classUsageMarker.visitLongConstant(clazz, longConstant);

    // Assert that nothing has changed
    assertNull(longConstant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitFloatConstant(Clazz, FloatConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitFloatConstant(Clazz, FloatConstant)}
   */
  @Test
  @DisplayName("Test visitFloatConstant(Clazz, FloatConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitFloatConstant(Clazz, FloatConstant)"})
  void testVisitFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    FloatConstant floatConstant = new FloatConstant();

    // Act
    shortestClassUsageMarker.visitFloatConstant(clazz, floatConstant);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, floatConstant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitFloatConstant(Clazz, FloatConstant)}.
   *
   * <ul>
   *   <li>Then {@link FloatConstant#FloatConstant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitFloatConstant(Clazz, FloatConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFloatConstant(Clazz, FloatConstant); then FloatConstant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitFloatConstant(Clazz, FloatConstant)"})
  void testVisitFloatConstant_thenFloatConstantProcessingInfoIsNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    FloatConstant floatConstant = new FloatConstant();

    // Act
    classUsageMarker.visitFloatConstant(clazz, floatConstant);

    // Assert that nothing has changed
    assertNull(floatConstant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitDoubleConstant(Clazz, DoubleConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDoubleConstant(Clazz, DoubleConstant)}
   */
  @Test
  @DisplayName("Test visitDoubleConstant(Clazz, DoubleConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDoubleConstant(Clazz, DoubleConstant)"})
  void testVisitDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    DoubleConstant doubleConstant = new DoubleConstant();

    // Act
    shortestClassUsageMarker.visitDoubleConstant(clazz, doubleConstant);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, doubleConstant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDoubleConstant(Clazz, DoubleConstant)}.
   *
   * <ul>
   *   <li>Then {@link DoubleConstant#DoubleConstant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDoubleConstant(Clazz, DoubleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDoubleConstant(Clazz, DoubleConstant); then DoubleConstant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDoubleConstant(Clazz, DoubleConstant)"})
  void testVisitDoubleConstant_thenDoubleConstantProcessingInfoIsNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    DoubleConstant doubleConstant = new DoubleConstant();

    // Act
    classUsageMarker.visitDoubleConstant(clazz, doubleConstant);

    // Assert that nothing has changed
    assertNull(doubleConstant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitPrimitiveArrayConstant(Clazz, PrimitiveArrayConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPrimitiveArrayConstant(Clazz,
   * PrimitiveArrayConstant)}
   */
  @Test
  @DisplayName("Test visitPrimitiveArrayConstant(Clazz, PrimitiveArrayConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitPrimitiveArrayConstant(Clazz, PrimitiveArrayConstant)"
  })
  void testVisitPrimitiveArrayConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    PrimitiveArrayConstant primitiveArrayConstant = new PrimitiveArrayConstant();

    // Act
    shortestClassUsageMarker.visitPrimitiveArrayConstant(clazz, primitiveArrayConstant);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, primitiveArrayConstant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitPrimitiveArrayConstant(Clazz, PrimitiveArrayConstant)}.
   *
   * <ul>
   *   <li>Then {@link PrimitiveArrayConstant#PrimitiveArrayConstant()} ProcessingInfo is {@code
   *       null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPrimitiveArrayConstant(Clazz,
   * PrimitiveArrayConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPrimitiveArrayConstant(Clazz, PrimitiveArrayConstant); then PrimitiveArrayConstant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitPrimitiveArrayConstant(Clazz, PrimitiveArrayConstant)"
  })
  void testVisitPrimitiveArrayConstant_thenPrimitiveArrayConstantProcessingInfoIsNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    classUsageMarker.setExtraConstantVisitor(null);
    LibraryClass clazz = new LibraryClass();
    PrimitiveArrayConstant primitiveArrayConstant = new PrimitiveArrayConstant();

    // Act
    classUsageMarker.visitPrimitiveArrayConstant(clazz, primitiveArrayConstant);

    // Assert that nothing has changed
    assertNull(primitiveArrayConstant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new SimpleUsageMarker());
    classUsageMarker.setExtraConstantVisitor(null);
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = new LibraryField();

    // Act
    classUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert that nothing has changed
    assertTrue(stringConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant2() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    stringConstant.referencedClass = libraryClass;
    stringConstant.referencedMember = null;

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Clazz clazz2 = stringConstant.referencedClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Object processingInfo = stringConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(usageMarker.currentUsageMark, clazz2.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant3() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new SimpleUsageMarker());
    classUsageMarker.setExtraConstantVisitor(new BootstrapMethodHandleTraveler(new ClassRenamer()));
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = new LibraryField();

    // Act
    classUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert that nothing has changed
    assertTrue(stringConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant4() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new SimpleUsageMarker());
    ConstantTagFilter extraConstantVisitor = new ConstantTagFilter(5, new ClassRenamer());
    classUsageMarker.setExtraConstantVisitor(extraConstantVisitor);
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = new LibraryField();

    // Act
    classUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert that nothing has changed
    assertTrue(stringConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant5() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new SimpleUsageMarker());
    classUsageMarker.setExtraConstantVisitor(new AnnotationUsageMarker(new ClassUsageMarker()));
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = new LibraryField();

    // Act
    classUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert that nothing has changed
    assertTrue(stringConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant6() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    classUsageMarker.setExtraConstantVisitor(null);
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = new LibraryField();

    // Act
    classUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert that nothing has changed
    assertTrue(stringConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant7() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new SimpleUsageMarker());
    classUsageMarker.setExtraConstantVisitor(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = new LibraryField();

    // Act
    classUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert that nothing has changed
    assertTrue(stringConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    StringConstant stringConstant = new StringConstant();

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stringConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    StringConstant stringConstant = new StringConstant();

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stringConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    StringConstant stringConstant = new StringConstant();

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stringConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    StringConstant stringConstant = new StringConstant();

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stringConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    StringConstant stringConstant = new StringConstant();

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stringConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    StringConstant stringConstant = new StringConstant();

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stringConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then {@link StringConstant#StringConstant()} ProcessingInfo {@link ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then StringConstant() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenStringConstantProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    StringConstant stringConstant = new StringConstant();

    // Act
    shortestClassUsageMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    Object processingInfo = stringConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}
   */
  @Test
  @DisplayName("Test visitUtf8Constant(Clazz, Utf8Constant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUtf8Constant(Clazz, Utf8Constant)"})
  void testVisitUtf8Constant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    Utf8Constant utf8Constant = new Utf8Constant();

    // Act
    shortestClassUsageMarker.visitUtf8Constant(clazz, utf8Constant);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, utf8Constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}.
   *
   * <ul>
   *   <li>Then {@link Utf8Constant#Utf8Constant()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUtf8Constant(Clazz, Utf8Constant)}
   */
  @Test
  @DisplayName(
      "Test visitUtf8Constant(Clazz, Utf8Constant); then Utf8Constant() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUtf8Constant(Clazz, Utf8Constant)"})
  void testVisitUtf8Constant_thenUtf8ConstantProcessingInfoIsNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();
    Utf8Constant utf8Constant = new Utf8Constant();

    // Act
    classUsageMarker.visitUtf8Constant(clazz, utf8Constant);

    // Assert that nothing has changed
    assertNull(utf8Constant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    DynamicConstant dynamicConstant = new DynamicConstant();

    // Act
    shortestClassUsageMarker.visitDynamicConstant(clazz, dynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, dynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    DynamicConstant dynamicConstant = new DynamicConstant();

    // Act
    shortestClassUsageMarker.visitDynamicConstant(clazz, dynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, dynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    DynamicConstant dynamicConstant = new DynamicConstant();

    // Act
    shortestClassUsageMarker.visitDynamicConstant(clazz, dynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, dynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    DynamicConstant dynamicConstant = new DynamicConstant();

    // Act
    shortestClassUsageMarker.visitDynamicConstant(clazz, dynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, dynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    DynamicConstant dynamicConstant = new DynamicConstant();

    // Act
    shortestClassUsageMarker.visitDynamicConstant(clazz, dynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, dynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    DynamicConstant dynamicConstant = new DynamicConstant();

    // Act
    shortestClassUsageMarker.visitDynamicConstant(clazz, dynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, dynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);

    // Act
    classUsageMarker.visitDynamicConstant(clazz, new DynamicConstant());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    InvokeDynamicConstant invokeDynamicConstant = new InvokeDynamicConstant();

    // Act
    shortestClassUsageMarker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, invokeDynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    InvokeDynamicConstant invokeDynamicConstant = new InvokeDynamicConstant();

    // Act
    shortestClassUsageMarker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, invokeDynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    InvokeDynamicConstant invokeDynamicConstant = new InvokeDynamicConstant();

    // Act
    shortestClassUsageMarker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, invokeDynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    InvokeDynamicConstant invokeDynamicConstant = new InvokeDynamicConstant();

    // Act
    shortestClassUsageMarker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, invokeDynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    InvokeDynamicConstant invokeDynamicConstant = new InvokeDynamicConstant();

    // Act
    shortestClassUsageMarker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, invokeDynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    InvokeDynamicConstant invokeDynamicConstant = new InvokeDynamicConstant();

    // Act
    shortestClassUsageMarker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, invokeDynamicConstant.getProcessingInfo());
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);

    // Act
    classUsageMarker.visitInvokeDynamicConstant(clazz, new InvokeDynamicConstant());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName("Test visitMethodHandleConstant(Clazz, MethodHandleConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Object processingInfo = methodHandleConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodHandleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodHandleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodHandleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodHandleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodHandleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodHandleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodHandleConstant methodHandleConstant = new MethodHandleConstant();

    // Act
    shortestClassUsageMarker.visitMethodHandleConstant(clazz, methodHandleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodHandleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);

    // Act
    classUsageMarker.visitMethodHandleConstant(clazz, new MethodHandleConstant());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryClass referencedClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    FieldrefConstant refConstant = new FieldrefConstant(1, 1, referencedClass, new LibraryField());

    // Act
    shortestClassUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    Clazz clazz2 = refConstant.referencedClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Object processingInfo = refConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(usageMarker.currentUsageMark, clazz2.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryClass referencedClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    FieldrefConstant refConstant = new FieldrefConstant(1, 1, referencedClass, new LibraryField());

    // Act
    classUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    assertTrue(refConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then {@link FieldrefConstant#FieldrefConstant()} ProcessingInfo {@link
   *       ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyRefConstant(Clazz, RefConstant); then FieldrefConstant() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenFieldrefConstantProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    shortestClassUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    Object processingInfo = refConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    shortestClassUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, refConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    shortestClassUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, refConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    shortestClassUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, refConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    shortestClassUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, refConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    shortestClassUsageMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, refConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryClass referencedClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Clazz clazz2 = classConstant.referencedClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Object processingInfo = classConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(usageMarker.currentUsageMark, clazz2.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    classUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    assertTrue(classConstant.referencedClass instanceof LibraryClass);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then {@link ClassConstant#ClassConstant()} ProcessingInfo {@link ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then ClassConstant() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenClassConstantProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    ClassConstant classConstant = new ClassConstant();

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Object processingInfo = classConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ClassConstant classConstant = new ClassConstant();

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, classConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ClassConstant classConstant = new ClassConstant();

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, classConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ClassConstant classConstant = new ClassConstant();

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, classConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ClassConstant classConstant = new ClassConstant();

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, classConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ClassConstant classConstant = new ClassConstant();

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, classConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ClassConstant classConstant = new ClassConstant();

    // Act
    shortestClassUsageMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, classConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName("Test visitMethodTypeConstant(Clazz, MethodTypeConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Object processingInfo = methodTypeConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName("Test visitMethodTypeConstant(Clazz, MethodTypeConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant2() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    Clazz[] referencedClasses = new Clazz[] {null};
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant(1, referencedClasses);

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Object processingInfo = methodTypeConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName("Test visitMethodTypeConstant(Clazz, MethodTypeConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant3() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    Clazz[] referencedClasses = new Clazz[] {libraryClass};
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant(1, referencedClasses);

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Clazz[] clazzArray = methodTypeConstant.referencedClasses;
    Clazz clazz2 = clazzArray[0];
    assertTrue(clazz2 instanceof LibraryClass);
    Object processingInfo = methodTypeConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertEquals(1, clazzArray.length);
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(usageMarker.currentUsageMark, clazz2.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName("Test visitMethodTypeConstant(Clazz, MethodTypeConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant4() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryClass libraryClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    Clazz[] referencedClasses = new Clazz[] {libraryClass};
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant(1, referencedClasses);

    // Act
    classUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Clazz[] clazzArray = methodTypeConstant.referencedClasses;
    assertTrue(clazzArray[0] instanceof LibraryClass);
    assertEquals(1, clazzArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant();

    // Act
    shortestClassUsageMarker.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitMethodTypeConstant(Clazz, MethodTypeConstant)"})
  void testVisitMethodTypeConstant_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);

    // Act
    classUsageMarker.visitMethodTypeConstant(clazz, new MethodTypeConstant());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName("Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Object processingInfo = nameAndTypeConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, nameAndTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, nameAndTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, nameAndTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, nameAndTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, nameAndTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, nameAndTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    NameAndTypeConstant nameAndTypeConstant = new NameAndTypeConstant();

    // Act
    shortestClassUsageMarker.visitNameAndTypeConstant(clazz, nameAndTypeConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, nameAndTypeConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz, NameAndTypeConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitNameAndTypeConstant(Clazz,
   * NameAndTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitNameAndTypeConstant(Clazz, NameAndTypeConstant); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitNameAndTypeConstant(Clazz, NameAndTypeConstant)"})
  void testVisitNameAndTypeConstant_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);

    // Act
    classUsageMarker.visitNameAndTypeConstant(clazz, new NameAndTypeConstant());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName("Test visitModuleConstant(Clazz, ModuleConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName("Test visitModuleConstant(Clazz, ModuleConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitModuleConstant(Clazz, ModuleConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitModuleConstant(Clazz, ModuleConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName("Test visitModuleConstant(Clazz, ModuleConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitModuleConstant(Clazz, ModuleConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitModuleConstant(Clazz, ModuleConstant); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitModuleConstant(Clazz, ModuleConstant); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);

    // Act
    classUsageMarker.visitModuleConstant(clazz, new ModuleConstant());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}.
   *
   * <ul>
   *   <li>Then {@link ModuleConstant#ModuleConstant()} ProcessingInfo {@link ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleConstant(Clazz, ModuleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitModuleConstant(Clazz, ModuleConstant); then ModuleConstant() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleConstant(Clazz, ModuleConstant)"})
  void testVisitModuleConstant_thenModuleConstantProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    ModuleConstant moduleConstant = new ModuleConstant();

    // Act
    shortestClassUsageMarker.visitModuleConstant(clazz, moduleConstant);

    // Assert
    Object processingInfo = moduleConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, packageConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, packageConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, packageConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, packageConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, packageConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, packageConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, packageConstant.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(5, 3, constantPool, 5, 5, 5);

    // Act
    classUsageMarker.visitPackageConstant(clazz, new PackageConstant());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}.
   *
   * <ul>
   *   <li>Then {@link PackageConstant#PackageConstant()} ProcessingInfo {@link ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitPackageConstant(Clazz, PackageConstant)}
   */
  @Test
  @DisplayName(
      "Test visitPackageConstant(Clazz, PackageConstant); then PackageConstant() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitPackageConstant(Clazz, PackageConstant)"})
  void testVisitPackageConstant_thenPackageConstantProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    PackageConstant packageConstant = new PackageConstant();

    // Act
    shortestClassUsageMarker.visitPackageConstant(clazz, packageConstant);

    // Assert
    Object processingInfo = packageConstant.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    UnknownAttribute unknownAttribute = new UnknownAttribute(1, 3);

    // Act
    shortestClassUsageMarker.visitUnknownAttribute(clazz, unknownAttribute);

    // Assert
    Object processingInfo = unknownAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    DoubleConstant doubleConstant = new DoubleConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {doubleConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute3() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    DynamicConstant dynamicConstant = new DynamicConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {dynamicConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute4() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    FieldrefConstant fieldrefConstant = new FieldrefConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {fieldrefConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute5() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    FloatConstant floatConstant = new FloatConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {floatConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute6() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    IntegerConstant integerConstant = new IntegerConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {integerConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute7() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    InterfaceMethodrefConstant interfaceMethodrefConstant = new InterfaceMethodrefConstant();
    ProgramClass clazz =
        new ProgramClass(
            1, 3, new Constant[] {interfaceMethodrefConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute8() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    InvokeDynamicConstant invokeDynamicConstant = new InvokeDynamicConstant();
    ProgramClass clazz =
        new ProgramClass(
            1, 3, new Constant[] {invokeDynamicConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute9() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    DoubleConstant doubleConstant = new DoubleConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {doubleConstant, new FieldrefConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof FieldrefConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute10() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    Clazz[] referencedClasses = new Clazz[] {null};
    DynamicConstant dynamicConstant = new DynamicConstant(1, 1, referencedClasses);
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {dynamicConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute11() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    FieldrefConstant fieldrefConstant = new FieldrefConstant(1, 1, null, new LibraryField());
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {fieldrefConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName("Test visitUnknownAttribute(Clazz, UnknownAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute12() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    FloatConstant floatConstant = new FloatConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {floatConstant, new FieldrefConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    assertTrue(constantArray[1] instanceof FieldrefConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    ClassConstant classConstant = new ClassConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1);
    UnknownAttribute unknownAttribute = new UnknownAttribute(1, 3);

    // Act
    shortestClassUsageMarker.visitUnknownAttribute(clazz, unknownAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    Constant constant2 = constantArray[1];
    assertTrue(constant2 instanceof ClassConstant);
    assertEquals(2, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, unknownAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, processingInfo);
    assertSame(shortestUsageMark, constant2.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    DoubleConstant doubleConstant = new DoubleConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {doubleConstant, new ClassConstant()}, 1, 1, 1);
    UnknownAttribute unknownAttribute = new UnknownAttribute(1, 3);

    // Act
    shortestClassUsageMarker.visitUnknownAttribute(clazz, unknownAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof ClassConstant);
    Constant constant2 = constantArray[0];
    assertTrue(constant2 instanceof DoubleConstant);
    assertEquals(2, constantArray.length);
    Object processingInfo = constant2.getProcessingInfo();
    assertSame(processingInfo, unknownAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, processingInfo);
    assertSame(shortestUsageMark, constant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    DynamicConstant dynamicConstant = new DynamicConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {dynamicConstant, new ClassConstant()}, 1, 1, 1);
    UnknownAttribute unknownAttribute = new UnknownAttribute(1, 3);

    // Act
    shortestClassUsageMarker.visitUnknownAttribute(clazz, unknownAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof ClassConstant);
    Constant constant2 = constantArray[0];
    assertTrue(constant2 instanceof DynamicConstant);
    assertEquals(2, constantArray.length);
    Object processingInfo = constant2.getProcessingInfo();
    assertSame(processingInfo, unknownAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, processingInfo);
    assertSame(shortestUsageMark, constant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    FieldrefConstant fieldrefConstant = new FieldrefConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {fieldrefConstant, new ClassConstant()}, 1, 1, 1);
    UnknownAttribute unknownAttribute = new UnknownAttribute(1, 3);

    // Act
    shortestClassUsageMarker.visitUnknownAttribute(clazz, unknownAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof ClassConstant);
    Constant constant2 = constantArray[0];
    assertTrue(constant2 instanceof FieldrefConstant);
    assertEquals(2, constantArray.length);
    Object processingInfo = constant2.getProcessingInfo();
    assertSame(processingInfo, unknownAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, processingInfo);
    assertSame(shortestUsageMark, constant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    FloatConstant floatConstant = new FloatConstant();
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {floatConstant, new ClassConstant()}, 1, 1, 1);
    UnknownAttribute unknownAttribute = new UnknownAttribute(1, 3);

    // Act
    shortestClassUsageMarker.visitUnknownAttribute(clazz, unknownAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof ClassConstant);
    Constant constant2 = constantArray[0];
    assertTrue(constant2 instanceof FloatConstant);
    assertEquals(2, constantArray.length);
    Object processingInfo = constant2.getProcessingInfo();
    assertSame(processingInfo, unknownAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, processingInfo);
    assertSame(shortestUsageMark, constant.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass libraryClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    Clazz[] referencedClasses = new Clazz[] {libraryClass};
    DynamicConstant dynamicConstant = new DynamicConstant(1, 1, referencedClasses);
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {dynamicConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    Clazz[] clazzArray = ((DynamicConstant) constant).referencedClasses;
    assertTrue(clazzArray[0] instanceof LibraryClass);
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, clazzArray.length);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(1, referencedClass);
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link RefConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitUnknownAttribute(Clazz, UnknownAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitUnknownAttribute(Clazz, UnknownAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitUnknownAttribute(Clazz, UnknownAttribute)"})
  void testVisitUnknownAttribute_thenFirstElementReferencedClassLibraryClass2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    FieldrefConstant fieldrefConstant =
        new FieldrefConstant(1, 1, referencedClass, new LibraryField());
    ProgramClass clazz =
        new ProgramClass(1, 3, new Constant[] {fieldrefConstant, new ClassConstant()}, 1, 1, 1);

    // Act
    classUsageMarker.visitUnknownAttribute(clazz, new UnknownAttribute(1, 3));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((FieldrefConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constantArray[1] instanceof ClassConstant);
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(2, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName("Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Object processingInfo = sourceDebugExtensionAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName("Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitSourceDebugExtensionAttribute(clazz, new SourceDebugExtensionAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDebugExtensionAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDebugExtensionAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDebugExtensionAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDebugExtensionAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDebugExtensionAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDebugExtensionAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDebugExtensionAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, recordAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, recordAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, recordAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, recordAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, recordAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, recordAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, recordAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitRecordAttribute(clazz, new RecordAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then {@link RecordAttribute#RecordAttribute()} ProcessingInfo {@link ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitRecordAttribute(Clazz, RecordAttribute); then RecordAttribute() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenRecordAttributeProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    RecordAttribute recordAttribute = new RecordAttribute();

    // Act
    shortestClassUsageMarker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    Object processingInfo = recordAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName("Test visitSourceFileAttribute(Clazz, SourceFileAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Object processingInfo = sourceFileAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceFileAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceFileAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceFileAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceFileAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceFileAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceFileAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    shortestClassUsageMarker.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceFileAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitSourceFileAttribute(clazz, new SourceFileAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName("Test visitSourceDirAttribute(Clazz, SourceDirAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Object processingInfo = sourceDirAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDirAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDirAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDirAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDirAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDirAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDirAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    shortestClassUsageMarker.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, sourceDirAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitSourceDirAttribute(clazz, new SourceDirAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link InnerClassesAttribute#innerClassEntriesAccept(Clazz,
   *       InnerClassesInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInnerClassesAttribute(Clazz,
   * InnerClassesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesAttribute(Clazz, InnerClassesAttribute); then calls innerClassEntriesAccept(Clazz, InnerClassesInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitInnerClassesAttribute(Clazz, InnerClassesAttribute)"
  })
  void testVisitInnerClassesAttribute_thenCallsInnerClassEntriesAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();

    InnerClassesAttribute innerClassesAttribute = mock(InnerClassesAttribute.class);
    doNothing()
        .when(innerClassesAttribute)
        .innerClassEntriesAccept(Mockito.<Clazz>any(), Mockito.<InnerClassesInfoVisitor>any());

    // Act
    classUsageMarker.visitInnerClassesAttribute(clazz, innerClassesAttribute);

    // Assert
    verify(innerClassesAttribute)
        .innerClassEntriesAccept(isA(Clazz.class), isA(InnerClassesInfoVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName("Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Object processingInfo = enclosingMethodAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, enclosingMethodAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, enclosingMethodAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, enclosingMethodAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, enclosingMethodAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, enclosingMethodAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, enclosingMethodAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();

    // Act
    shortestClassUsageMarker.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, enclosingMethodAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnclosingMethodAttribute(Clazz,
   * EnclosingMethodAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)"
  })
  void testVisitEnclosingMethodAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitEnclosingMethodAttribute(clazz, new EnclosingMethodAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleAttribute(Clazz, ModuleAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleAttribute(Clazz, ModuleAttribute)}
   */
  @Test
  @DisplayName("Test visitModuleAttribute(Clazz, ModuleAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitModuleAttribute(Clazz, ModuleAttribute)"})
  void testVisitModuleAttribute() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    ModuleAttribute moduleAttribute = new ModuleAttribute();

    // Act
    shortestClassUsageMarker.visitModuleAttribute(clazz, moduleAttribute);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, moduleAttribute.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName("Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Object processingInfo = moduleMainClassAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleMainClassAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleMainClassAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleMainClassAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleMainClassAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleMainClassAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleMainClassAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModuleMainClassAttribute moduleMainClassAttribute = new ModuleMainClassAttribute();

    // Act
    shortestClassUsageMarker.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, moduleMainClassAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModuleMainClassAttribute(Clazz,
   * ModuleMainClassAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)"
  })
  void testVisitModuleMainClassAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitModuleMainClassAttribute(clazz, new ModuleMainClassAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName("Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Object processingInfo = modulePackagesAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, modulePackagesAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, modulePackagesAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, modulePackagesAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, modulePackagesAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, modulePackagesAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, modulePackagesAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    ModulePackagesAttribute modulePackagesAttribute = new ModulePackagesAttribute();

    // Act
    shortestClassUsageMarker.visitModulePackagesAttribute(clazz, modulePackagesAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, modulePackagesAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitModulePackagesAttribute(Clazz,
   * ModulePackagesAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitModulePackagesAttribute(Clazz, ModulePackagesAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)"
  })
  void testVisitModulePackagesAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitModulePackagesAttribute(clazz, new ModulePackagesAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Object processingInfo = deprecatedAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute2() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, deprecatedAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute3() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, deprecatedAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute4() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, deprecatedAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute5() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, deprecatedAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute6() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, deprecatedAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute7() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, deprecatedAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute8() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();

    // Act
    shortestClassUsageMarker.visitDeprecatedAttribute(clazz, deprecatedAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, deprecatedAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)} with {@code
   * clazz}, {@code deprecatedAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitDeprecatedAttribute(Clazz,
   * DeprecatedAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitDeprecatedAttribute(Clazz, DeprecatedAttribute) with 'clazz', 'deprecatedAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitDeprecatedAttribute(Clazz, DeprecatedAttribute)"})
  void testVisitDeprecatedAttributeWithClazzDeprecatedAttribute9() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitDeprecatedAttribute(clazz, new DeprecatedAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Object processingInfo = syntheticAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute2() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, syntheticAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute3() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, syntheticAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute4() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, syntheticAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute5() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, syntheticAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute6() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, syntheticAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute7() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, syntheticAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute8() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

    // Act
    shortestClassUsageMarker.visitSyntheticAttribute(clazz, syntheticAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, syntheticAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSyntheticAttribute(Clazz, SyntheticAttribute)} with {@code
   * clazz}, {@code syntheticAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSyntheticAttribute(Clazz,
   * SyntheticAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSyntheticAttribute(Clazz, SyntheticAttribute) with 'clazz', 'syntheticAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSyntheticAttribute(Clazz, SyntheticAttribute)"})
  void testVisitSyntheticAttributeWithClazzSyntheticAttribute9() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitSyntheticAttribute(clazz, new SyntheticAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Object processingInfo = signatureAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute2() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, signatureAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute3() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, signatureAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute4() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, signatureAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute5() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, signatureAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute6() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, signatureAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute7() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, signatureAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute8() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    SignatureAttribute signatureAttribute = new SignatureAttribute();

    // Act
    shortestClassUsageMarker.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, signatureAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute9() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitSignatureAttribute(clazz, new SignatureAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Object processingInfo = constantValueAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, constantValueAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, constantValueAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, constantValueAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, constantValueAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, constantValueAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, constantValueAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();
    ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();

    // Act
    shortestClassUsageMarker.visitConstantValueAttribute(clazz, field, constantValueAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, constantValueAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field = new LibraryField();

    // Act
    classUsageMarker.visitConstantValueAttribute(clazz, field, new ConstantValueAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName("Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Object processingInfo = methodParametersAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodParametersAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodParametersAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodParametersAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodParametersAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodParametersAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodParametersAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    MethodParametersAttribute methodParametersAttribute = new MethodParametersAttribute();

    // Act
    shortestClassUsageMarker.visitMethodParametersAttribute(
        clazz, method, methodParametersAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, methodParametersAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitMethodParametersAttribute(Clazz, Method,
   * MethodParametersAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)"
  })
  void testVisitMethodParametersAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();

    // Act
    classUsageMarker.visitMethodParametersAttribute(clazz, method, new MethodParametersAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName("Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Object processingInfo = exceptionsAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, exceptionsAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, exceptionsAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, exceptionsAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, exceptionsAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, exceptionsAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, exceptionsAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

    // Act
    shortestClassUsageMarker.visitExceptionsAttribute(clazz, method, exceptionsAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, exceptionsAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionsAttribute(Clazz, Method,
   * ExceptionsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)"
  })
  void testVisitExceptionsAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();

    // Act
    classUsageMarker.visitExceptionsAttribute(clazz, method, new ExceptionsAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then {@link CodeAttribute#CodeAttribute()} ProcessingInfo {@link ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then CodeAttribute() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCodeAttributeProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Object processingInfo = codeAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, codeAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, codeAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, codeAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, codeAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, codeAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, codeAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, codeAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();

    // Act
    classUsageMarker.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    classUsageMarker.visitStackMapAttribute(clazz, method, codeAttribute, new StackMapAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method, CodeAttribute,
   * StackMapAttribute)}.
   *
   * <ul>
   *   <li>Then {@link StackMapAttribute#StackMapAttribute()} ProcessingInfo {@link
   *       ShortestUsageMark}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapAttribute(Clazz, Method,
   * CodeAttribute, StackMapAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute); then StackMapAttribute() ProcessingInfo ShortestUsageMark")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)"
  })
  void testVisitStackMapAttribute_thenStackMapAttributeProcessingInfoShortestUsageMark() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapAttribute stackMapAttribute = new StackMapAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapAttribute(
        clazz, method, codeAttribute, stackMapAttribute);

    // Assert
    Object processingInfo = stackMapAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method, CodeAttribute,
   * StackMapTableAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method,
   * CodeAttribute, StackMapTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)"
  })
  void testVisitStackMapTableAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapTableAttribute(
        clazz, method, codeAttribute, stackMapTableAttribute);

    // Assert
    Object processingInfo = stackMapTableAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method, CodeAttribute,
   * StackMapTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method,
   * CodeAttribute, StackMapTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)"
  })
  void testVisitStackMapTableAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapTableAttribute(
        clazz, method, codeAttribute, stackMapTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method, CodeAttribute,
   * StackMapTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method,
   * CodeAttribute, StackMapTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)"
  })
  void testVisitStackMapTableAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapTableAttribute(
        clazz, method, codeAttribute, stackMapTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method, CodeAttribute,
   * StackMapTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method,
   * CodeAttribute, StackMapTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)"
  })
  void testVisitStackMapTableAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapTableAttribute(
        clazz, method, codeAttribute, stackMapTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method, CodeAttribute,
   * StackMapTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method,
   * CodeAttribute, StackMapTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)"
  })
  void testVisitStackMapTableAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapTableAttribute(
        clazz, method, codeAttribute, stackMapTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method, CodeAttribute,
   * StackMapTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method,
   * CodeAttribute, StackMapTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)"
  })
  void testVisitStackMapTableAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapTableAttribute(
        clazz, method, codeAttribute, stackMapTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method, CodeAttribute,
   * StackMapTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitStackMapTableAttribute(Clazz, Method,
   * CodeAttribute, StackMapTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)"
  })
  void testVisitStackMapTableAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();

    // Act
    shortestClassUsageMarker.visitStackMapTableAttribute(
        clazz, method, codeAttribute, stackMapTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, stackMapTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute() {
    // Arrange
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Object processingInfo = lineNumberTableAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof ShortestUsageMark);
    assertEquals("Just cause", ((ShortestUsageMark) processingInfo).getReason());
    assertTrue(((ShortestUsageMark) processingInfo).isCertain());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, lineNumberTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, lineNumberTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, lineNumberTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, lineNumberTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, lineNumberTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, lineNumberTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

    // Act
    shortestClassUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, lineNumberTableAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    Object processingInfo = constant.getProcessingInfo();
    assertSame(processingInfo, lineNumberTableAttribute.getProcessingInfo());
    assertSame(processingInfo, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
    assertSame(usageMarker.currentUsageMark, processingInfo);
  }

  /**
   * Test {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute,
   * LineNumberTableAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitLineNumberTableAttribute(Clazz, Method,
   * CodeAttribute, LineNumberTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)"
  })
  void testVisitLineNumberTableAttribute_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    classUsageMarker.visitLineNumberTableAttribute(
        clazz, method, codeAttribute, new LineNumberTableAttribute());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitAnnotationDefaultAttribute(Clazz, Method,
   * AnnotationDefaultAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#defaultValueAccept(Clazz,
   *       ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitAnnotationDefaultAttribute(Clazz, Method,
   * AnnotationDefaultAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute); then calls defaultValueAccept(Clazz, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute)"
  })
  void testVisitAnnotationDefaultAttribute_thenCallsDefaultValueAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .defaultValueAccept(Mockito.<Clazz>any(), Mockito.<ElementValueVisitor>any());

    // Act
    classUsageMarker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

    // Assert
    verify(annotationDefaultAttribute)
        .defaultValueAccept(isA(Clazz.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExceptionInfo(Clazz, Method, CodeAttribute,
   * ExceptionInfo)}
   */
  @Test
  @DisplayName("Test visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)"
  })
  void testVisitExceptionInfo() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    ExceptionInfo exceptionInfo = new ExceptionInfo();

    // Act
    shortestClassUsageMarker.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, exceptionInfo.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName("Test visitInnerClassesInfo(Clazz, InnerClassesInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());
    when(clazz.getClassName(anyInt())).thenReturn("Name");
    when(clazz.getName()).thenReturn("Name");
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo(1, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert
    verify(clazz, atLeast(1)).constantPoolEntryAccept(eq(1), isA(ConstantVisitor.class));
    verify(clazz).getClassName(1);
    verify(clazz).getName();
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, innerClassesInfo.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName("Test visitInnerClassesInfo(Clazz, InnerClassesInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());
    when(clazz.getClassName(anyInt())).thenReturn("Name");
    when(clazz.getName()).thenReturn("Name");
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo(1, 0, 1, 1);

    // Act
    classUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert
    verify(clazz, atLeast(1)).constantPoolEntryAccept(eq(1), isA(ConstantVisitor.class));
    verify(clazz).getClassName(1);
    verify(clazz).getName();
  }

  /**
   * Test {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName("Test visitInnerClassesInfo(Clazz, InnerClassesInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo3() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());
    when(clazz.getClassName(anyInt())).thenReturn("Name");
    when(clazz.getName()).thenReturn("Name");
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo(1, 1, 0, 1);

    // Act
    classUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert
    verify(clazz, atLeast(1)).constantPoolEntryAccept(eq(1), isA(ConstantVisitor.class));
    verify(clazz).getClassName(1);
    verify(clazz).getName();
  }

  /**
   * Test {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <ul>
   *   <li>Given {@code Class Name}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName("Test visitInnerClassesInfo(Clazz, InnerClassesInfo); given 'Class Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo_givenClassName() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName()).thenReturn("Name");
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo(1, 1, 1, 1);

    // Act
    classUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert that nothing has changed
    verify(clazz).getClassName(1);
    verify(clazz).getName();
  }

  /**
   * Test {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#constantPoolEntryAccept(int, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesInfo(Clazz, InnerClassesInfo); then calls constantPoolEntryAccept(int, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo_thenCallsConstantPoolEntryAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());
    when(clazz.getClassName(anyInt())).thenReturn("Name");
    when(clazz.getName()).thenReturn("Name");
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo(1, 1, 1, 1);

    // Act
    classUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert
    verify(clazz, atLeast(1)).constantPoolEntryAccept(eq(1), isA(ConstantVisitor.class));
    verify(clazz).getClassName(1);
    verify(clazz).getName();
  }

  /**
   * Test {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}.
   *
   * <ul>
   *   <li>Then {@link InnerClassesInfo#InnerClassesInfo()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitInnerClassesInfo(Clazz, InnerClassesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitInnerClassesInfo(Clazz, InnerClassesInfo); then InnerClassesInfo() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitInnerClassesInfo(Clazz, InnerClassesInfo)"})
  void testVisitInnerClassesInfo_thenInnerClassesInfoProcessingInfoIsNull() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    InnerClassesInfo innerClassesInfo = new InnerClassesInfo();

    // Act
    classUsageMarker.visitInnerClassesInfo(clazz, innerClassesInfo);

    // Assert that nothing has changed
    assertNull(innerClassesInfo.getProcessingInfo());
  }

  /**
   * Test {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute, int,
   * SameOneFrame)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute,
   * int, SameOneFrame)}
   */
  @Test
  @DisplayName(
      "Test visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame)"
  })
  void testVisitSameOneFrame_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitSameOneFrame(
        clazz, method, codeAttribute, 2, new SameOneFrame(new ObjectType()));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute, int,
   * SameOneFrame)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute,
   * int, SameOneFrame)}
   */
  @Test
  @DisplayName(
      "Test visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame)"
  })
  void testVisitSameOneFrame_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitSameOneFrame(
        clazz, method, codeAttribute, 2, new SameOneFrame(new ObjectType()));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute, int,
   * SameOneFrame)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute,
   * int, SameOneFrame)}
   */
  @Test
  @DisplayName(
      "Test visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame)"
  })
  void testVisitSameOneFrame_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitSameOneFrame(
        clazz, method, codeAttribute, 2, new SameOneFrame(new ObjectType()));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute, int,
   * SameOneFrame)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute,
   * int, SameOneFrame)}
   */
  @Test
  @DisplayName(
      "Test visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame)"
  })
  void testVisitSameOneFrame_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitSameOneFrame(
        clazz, method, codeAttribute, 2, new SameOneFrame(new ObjectType()));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute, int,
   * SameOneFrame)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute,
   * int, SameOneFrame)}
   */
  @Test
  @DisplayName(
      "Test visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame)"
  })
  void testVisitSameOneFrame_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitSameOneFrame(
        clazz, method, codeAttribute, 2, new SameOneFrame(new ObjectType()));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute, int,
   * SameOneFrame)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitSameOneFrame(Clazz, Method, CodeAttribute,
   * int, SameOneFrame)}
   */
  @Test
  @DisplayName(
      "Test visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitSameOneFrame(Clazz, Method, CodeAttribute, int, SameOneFrame)"
  })
  void testVisitSameOneFrame_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitSameOneFrame(
        clazz, method, codeAttribute, 2, new SameOneFrame(new ObjectType()));

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame)}.
   *
   * <ul>
   *   <li>Then calls {@link DoubleType#variablesAccept(Clazz, Method, CodeAttribute, int, int,
   *       VerificationTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitFullFrame(Clazz, Method, CodeAttribute, int,
   * FullFrame)}
   */
  @Test
  @DisplayName(
      "Test visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame); then calls variablesAccept(Clazz, Method, CodeAttribute, int, int, VerificationTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame)"
  })
  void testVisitFullFrame_thenCallsVariablesAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    DoubleType doubleType = mock(DoubleType.class);
    doNothing()
        .when(doubleType)
        .variablesAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            anyInt(),
            Mockito.<VerificationTypeVisitor>any());
    VerificationType[] variables = new VerificationType[] {doubleType};
    VerificationType[] stack = new VerificationType[] {VerificationTypeFactory.createDoubleType()};

    FullFrame fullFrame = new FullFrame(2, variables, stack);
    fullFrame.variablesCount = 1;
    fullFrame.stackCount = 0;

    // Act
    classUsageMarker.visitFullFrame(clazz, method, codeAttribute, 2, fullFrame);

    // Assert
    verify(doubleType)
        .variablesAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            eq(0),
            isA(VerificationTypeVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame)}.
   *
   * <ul>
   *   <li>When {@link DoubleType} {@link DoubleType#stackAccept(Clazz, Method, CodeAttribute, int,
   *       int, VerificationTypeVisitor)} does nothing.
   *   <li>Then calls {@link DoubleType#stackAccept(Clazz, Method, CodeAttribute, int, int,
   *       VerificationTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitFullFrame(Clazz, Method, CodeAttribute, int,
   * FullFrame)}
   */
  @Test
  @DisplayName(
      "Test visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame); when DoubleType stackAccept(Clazz, Method, CodeAttribute, int, int, VerificationTypeVisitor) does nothing; then calls stackAccept(Clazz, Method, CodeAttribute, int, int, VerificationTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame)"
  })
  void testVisitFullFrame_whenDoubleTypeStackAcceptDoesNothing_thenCallsStackAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    DoubleType doubleType = mock(DoubleType.class);
    doNothing()
        .when(doubleType)
        .stackAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            anyInt(),
            Mockito.<VerificationTypeVisitor>any());
    VerificationType[] stack = new VerificationType[] {doubleType};
    VerificationType[] variables =
        new VerificationType[] {VerificationTypeFactory.createDoubleType()};

    FullFrame fullFrame = new FullFrame(2, variables, stack);
    fullFrame.variablesCount = 0;
    fullFrame.stackCount = 1;

    // Act
    classUsageMarker.visitFullFrame(clazz, method, codeAttribute, 2, fullFrame);

    // Assert
    verify(doubleType)
        .stackAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            eq(0),
            isA(VerificationTypeVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame)}.
   *
   * <ul>
   *   <li>When {@link FullFrame} {@link FullFrame#stackAccept(Clazz, Method, CodeAttribute, int,
   *       VerificationTypeVisitor)} does nothing.
   *   <li>Then calls {@link FullFrame#stackAccept(Clazz, Method, CodeAttribute, int,
   *       VerificationTypeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitFullFrame(Clazz, Method, CodeAttribute, int,
   * FullFrame)}
   */
  @Test
  @DisplayName(
      "Test visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame); when FullFrame stackAccept(Clazz, Method, CodeAttribute, int, VerificationTypeVisitor) does nothing; then calls stackAccept(Clazz, Method, CodeAttribute, int, VerificationTypeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitFullFrame(Clazz, Method, CodeAttribute, int, FullFrame)"
  })
  void testVisitFullFrame_whenFullFrameStackAcceptDoesNothing_thenCallsStackAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    FullFrame fullFrame = mock(FullFrame.class);
    doNothing()
        .when(fullFrame)
        .stackAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<VerificationTypeVisitor>any());
    doNothing()
        .when(fullFrame)
        .variablesAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<VerificationTypeVisitor>any());

    // Act
    classUsageMarker.visitFullFrame(clazz, method, codeAttribute, 2, fullFrame);

    // Assert
    verify(fullFrame)
        .stackAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            isA(VerificationTypeVisitor.class));
    verify(fullFrame)
        .variablesAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            isA(VerificationTypeVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitObjectType(Clazz, Method, CodeAttribute,
   * int, ObjectType)}
   */
  @Test
  @DisplayName(
      "Test visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitObjectType(Clazz, Method, CodeAttribute, int, ObjectType)"
  })
  void testVisitObjectType_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    classUsageMarker.visitObjectType(clazz, method, codeAttribute, 2, new ObjectType());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitParameterInfo(Clazz, Method, int, ParameterInfo)}.
   *
   * <ul>
   *   <li>Then calls {@link ParameterInfo#nameConstantAccept(Clazz, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitParameterInfo(Clazz, Method, int,
   * ParameterInfo)}
   */
  @Test
  @DisplayName(
      "Test visitParameterInfo(Clazz, Method, int, ParameterInfo); then calls nameConstantAccept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitParameterInfo(Clazz, Method, int, ParameterInfo)"})
  void testVisitParameterInfo_thenCallsNameConstantAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    ParameterInfo parameterInfo = mock(ParameterInfo.class);
    doNothing()
        .when(parameterInfo)
        .nameConstantAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());

    // Act
    classUsageMarker.visitParameterInfo(clazz, method, 1, parameterInfo);

    // Assert
    verify(parameterInfo).nameConstantAccept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName("Test visitRequiresInfo(Clazz, RequiresInfo); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName("Test visitRequiresInfo(Clazz, RequiresInfo); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName("Test visitRequiresInfo(Clazz, RequiresInfo); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName("Test visitRequiresInfo(Clazz, RequiresInfo); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName("Test visitRequiresInfo(Clazz, RequiresInfo); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName("Test visitRequiresInfo(Clazz, RequiresInfo); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRequiresInfo(Clazz, RequiresInfo); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitRequiresInfo(Clazz, RequiresInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRequiresInfo(Clazz, RequiresInfo); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitRequiresInfo(Clazz, RequiresInfo)"})
  void testVisitRequiresInfo_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitRequiresInfo(clazz, new RequiresInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName("Test visitExportsInfo(Clazz, ExportsInfo); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName("Test visitExportsInfo(Clazz, ExportsInfo); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName("Test visitExportsInfo(Clazz, ExportsInfo); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName("Test visitExportsInfo(Clazz, ExportsInfo); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName("Test visitExportsInfo(Clazz, ExportsInfo); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName("Test visitExportsInfo(Clazz, ExportsInfo); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName(
      "Test visitExportsInfo(Clazz, ExportsInfo); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitExportsInfo(Clazz, ExportsInfo)}
   */
  @Test
  @DisplayName(
      "Test visitExportsInfo(Clazz, ExportsInfo); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitExportsInfo(Clazz, ExportsInfo)"})
  void testVisitExportsInfo_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitExportsInfo(clazz, new ExportsInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName("Test visitOpensInfo(Clazz, OpensInfo); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName("Test visitOpensInfo(Clazz, OpensInfo); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName("Test visitOpensInfo(Clazz, OpensInfo); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName("Test visitOpensInfo(Clazz, OpensInfo); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName("Test visitOpensInfo(Clazz, OpensInfo); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName("Test visitOpensInfo(Clazz, OpensInfo); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName("Test visitOpensInfo(Clazz, OpensInfo); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitOpensInfo(Clazz, OpensInfo)}
   */
  @Test
  @DisplayName(
      "Test visitOpensInfo(Clazz, OpensInfo); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitOpensInfo(Clazz, OpensInfo)"})
  void testVisitOpensInfo_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitOpensInfo(clazz, new OpensInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName("Test visitProvidesInfo(Clazz, ProvidesInfo); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName("Test visitProvidesInfo(Clazz, ProvidesInfo); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName("Test visitProvidesInfo(Clazz, ProvidesInfo); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName("Test visitProvidesInfo(Clazz, ProvidesInfo); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName("Test visitProvidesInfo(Clazz, ProvidesInfo); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName("Test visitProvidesInfo(Clazz, ProvidesInfo); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitProvidesInfo(Clazz, ProvidesInfo); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    shortestClassUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitProvidesInfo(Clazz, ProvidesInfo)}
   */
  @Test
  @DisplayName(
      "Test visitProvidesInfo(Clazz, ProvidesInfo); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.visitProvidesInfo(Clazz, ProvidesInfo)"})
  void testVisitProvidesInfo_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    classUsageMarker.visitProvidesInfo(clazz, new ProvidesInfo());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#visitEnumConstantElementValue(Clazz, Annotation,
   * EnumConstantElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link EnumConstantElementValue#referencedFieldAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitEnumConstantElementValue(Clazz, Annotation,
   * EnumConstantElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitEnumConstantElementValue(Clazz, Annotation, EnumConstantElementValue); then calls referencedFieldAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitEnumConstantElementValue(Clazz, Annotation, EnumConstantElementValue)"
  })
  void testVisitEnumConstantElementValue_thenCallsReferencedFieldAccept() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    EnumConstantElementValue enumConstantElementValue = mock(EnumConstantElementValue.class);
    doNothing().when(enumConstantElementValue).referencedFieldAccept(Mockito.<MemberVisitor>any());

    // Act
    classUsageMarker.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

    // Assert
    verify(enumConstantElementValue).referencedFieldAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementClassConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link DoubleConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element DoubleConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementDoubleConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DoubleConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link DynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element DynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof DynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link FieldrefConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element FieldrefConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementFieldrefConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FieldrefConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FieldrefConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link FloatConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element FloatConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementFloatConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof FloatConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link IntegerConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element IntegerConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementIntegerConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof IntegerConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link InvokeDynamicConstant}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element InvokeDynamicConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementInvokeDynamicConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    shortestClassUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof InvokeDynamicConstant);
    assertEquals(1, constantArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, constant.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant#referencedClass} {@link LibraryClass}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then first element referencedClass LibraryClass")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassUsageMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenFirstElementReferencedClassLibraryClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    LibraryClass referencedClass = new LibraryClass(5, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(0, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    classUsageMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(((ClassConstant) constant).referencedClass instanceof LibraryClass);
    assertTrue(constant instanceof ClassConstant);
    assertEquals(1, constantArray.length);
  }

  /**
   * Test {@link ClassUsageMarker#markAsUsed(Processable)}.
   *
   * <p>Method under test: {@link ClassUsageMarker#markAsUsed(Processable)}
   */
  @Test
  @DisplayName("Test markAsUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markAsUsed(Processable)"})
  void testMarkAsUsed() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    ShortestClassUsageMarker shortestClassUsageMarker =
        new ShortestClassUsageMarker(usageMarker, "Just cause");
    SimpleProcessable processable = new SimpleProcessable();

    // Act
    shortestClassUsageMarker.markAsUsed(processable);

    // Assert
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, processable.getProcessingInfo());
    assertSame(shortestUsageMark, shortestClassUsageMarker.getUsageMarker().currentUsageMark);
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsUsed(Processable)} with {@code processable}.
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsUsed(Processable)}
   */
  @Test
  @DisplayName("Test shouldBeMarkedAsUsed(Processable) with 'processable'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsUsed(Processable)"})
  void testShouldBeMarkedAsUsedWithProcessable() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsUsed(Processable)} with {@code processable}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker()}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsUsed(Processable)}
   */
  @Test
  @DisplayName(
      "Test shouldBeMarkedAsUsed(Processable) with 'processable'; given ClassUsageMarker(); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsUsed(Processable)"})
  void testShouldBeMarkedAsUsedWithProcessable_givenClassUsageMarker_thenReturnTrue() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass)} with {@code programClass}.
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass)}
   */
  @Test
  @DisplayName("Test shouldBeMarkedAsUsed(ProgramClass) with 'programClass'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsUsed(ProgramClass)"})
  void testShouldBeMarkedAsUsedWithProgramClass() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsUsed(new ProgramClass()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass, ProgramMember)} with {@code
   * programClass}, {@code programMember}.
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass,
   * ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test shouldBeMarkedAsUsed(ProgramClass, ProgramMember) with 'programClass', 'programMember'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsUsed(ProgramClass, ProgramMember)"})
  void testShouldBeMarkedAsUsedWithProgramClassProgramMember() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsUsed(programClass, new ProgramField()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass, ProgramMember)} with {@code
   * programClass}, {@code programMember}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass,
   * ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test shouldBeMarkedAsUsed(ProgramClass, ProgramMember) with 'programClass', 'programMember'; given ClassUsageMarker()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsUsed(ProgramClass, ProgramMember)"})
  void testShouldBeMarkedAsUsedWithProgramClassProgramMember_givenClassUsageMarker() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsUsed(programClass, new ProgramField()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass)} with {@code programClass}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker()}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsUsed(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test shouldBeMarkedAsUsed(ProgramClass) with 'programClass'; given ClassUsageMarker(); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsUsed(ProgramClass)"})
  void testShouldBeMarkedAsUsedWithProgramClass_givenClassUsageMarker_thenReturnTrue() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsUsed(new ProgramClass()));
  }

  /**
   * Test {@link ClassUsageMarker#isUsed(Processable)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker(SimpleUsageMarker)} with usageMarker is
   *       {@link ShortestUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#isUsed(Processable)}
   */
  @Test
  @DisplayName(
      "Test isUsed(Processable); given ClassUsageMarker(SimpleUsageMarker) with usageMarker is ShortestUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.isUsed(Processable)"})
  void testIsUsed_givenClassUsageMarkerWithUsageMarkerIsShortestUsageMarker() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());

    // Act and Assert
    assertFalse(classUsageMarker.isUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#isUsed(Processable)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker()}.
   *   <li>When {@link SimpleProcessable#SimpleProcessable()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#isUsed(Processable)}
   */
  @Test
  @DisplayName(
      "Test isUsed(Processable); given ClassUsageMarker(); when SimpleProcessable(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.isUsed(Processable)"})
  void testIsUsed_givenClassUsageMarker_whenSimpleProcessable_thenReturnFalse() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    // Act and Assert
    assertFalse(classUsageMarker.isUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#markAsPossiblyUsed(Processable)}.
   *
   * <ul>
   *   <li>Then calls {@link ShortestUsageMarker#markAsPossiblyUsed(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markAsPossiblyUsed(Processable)}
   */
  @Test
  @DisplayName("Test markAsPossiblyUsed(Processable); then calls markAsPossiblyUsed(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markAsPossiblyUsed(Processable)"})
  void testMarkAsPossiblyUsed_thenCallsMarkAsPossiblyUsed() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    doNothing().when(usageMarker).markAsPossiblyUsed(Mockito.<Processable>any());
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(usageMarker);

    // Act
    classUsageMarker.markAsPossiblyUsed(new SimpleProcessable());

    // Assert that nothing has changed
    verify(usageMarker).markAsPossiblyUsed(isA(Processable.class));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(Processable)} with {@code
   * processable}.
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(Processable)}
   */
  @Test
  @DisplayName("Test shouldBeMarkedAsPossiblyUsed(Processable) with 'processable'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsPossiblyUsed(Processable)"})
  void testShouldBeMarkedAsPossiblyUsedWithProcessable() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsPossiblyUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(Processable)} with {@code
   * processable}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(Processable)}
   */
  @Test
  @DisplayName(
      "Test shouldBeMarkedAsPossiblyUsed(Processable) with 'processable'; given ClassUsageMarker()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.shouldBeMarkedAsPossiblyUsed(Processable)"})
  void testShouldBeMarkedAsPossiblyUsedWithProcessable_givenClassUsageMarker() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsPossiblyUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(ProgramClass, ProgramMember)} with
   * {@code programClass}, {@code programMember}.
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(ProgramClass,
   * ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test shouldBeMarkedAsPossiblyUsed(ProgramClass, ProgramMember) with 'programClass', 'programMember'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassUsageMarker.shouldBeMarkedAsPossiblyUsed(ProgramClass, ProgramMember)"
  })
  void testShouldBeMarkedAsPossiblyUsedWithProgramClassProgramMember() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsPossiblyUsed(programClass, new ProgramField()));
  }

  /**
   * Test {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(ProgramClass, ProgramMember)} with
   * {@code programClass}, {@code programMember}.
   *
   * <p>Method under test: {@link ClassUsageMarker#shouldBeMarkedAsPossiblyUsed(ProgramClass,
   * ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test shouldBeMarkedAsPossiblyUsed(ProgramClass, ProgramMember) with 'programClass', 'programMember'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassUsageMarker.shouldBeMarkedAsPossiblyUsed(ProgramClass, ProgramMember)"
  })
  void testShouldBeMarkedAsPossiblyUsedWithProgramClassProgramMember2() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertTrue(classUsageMarker.shouldBeMarkedAsPossiblyUsed(programClass, new ProgramField()));
  }

  /**
   * Test {@link ClassUsageMarker#isPossiblyUsed(Processable)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker(SimpleUsageMarker)} with usageMarker is
   *       {@link ShortestUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#isPossiblyUsed(Processable)}
   */
  @Test
  @DisplayName(
      "Test isPossiblyUsed(Processable); given ClassUsageMarker(SimpleUsageMarker) with usageMarker is ShortestUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.isPossiblyUsed(Processable)"})
  void testIsPossiblyUsed_givenClassUsageMarkerWithUsageMarkerIsShortestUsageMarker() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(new ShortestUsageMarker());

    // Act and Assert
    assertFalse(classUsageMarker.isPossiblyUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#isPossiblyUsed(Processable)}.
   *
   * <ul>
   *   <li>Given {@link ClassUsageMarker#ClassUsageMarker()}.
   *   <li>When {@link SimpleProcessable#SimpleProcessable()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#isPossiblyUsed(Processable)}
   */
  @Test
  @DisplayName(
      "Test isPossiblyUsed(Processable); given ClassUsageMarker(); when SimpleProcessable(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassUsageMarker.isPossiblyUsed(Processable)"})
  void testIsPossiblyUsed_givenClassUsageMarker_whenSimpleProcessable_thenReturnFalse() {
    // Arrange
    ClassUsageMarker classUsageMarker = new ClassUsageMarker();

    // Act and Assert
    assertFalse(classUsageMarker.isPossiblyUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link ClassUsageMarker#markAsUnused(Processable)}.
   *
   * <ul>
   *   <li>Then calls {@link ShortestUsageMarker#markAsUnused(Processable)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassUsageMarker#markAsUnused(Processable)}
   */
  @Test
  @DisplayName("Test markAsUnused(Processable); then calls markAsUnused(Processable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassUsageMarker.markAsUnused(Processable)"})
  void testMarkAsUnused_thenCallsMarkAsUnused() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    doNothing().when(usageMarker).markAsUnused(Mockito.<Processable>any());
    ClassUsageMarker classUsageMarker = new ClassUsageMarker(usageMarker);

    // Act
    classUsageMarker.markAsUnused(new SimpleProcessable());

    // Assert
    verify(usageMarker).markAsUnused(isA(Processable.class));
  }
}
