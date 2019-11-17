/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import kotlinx.metadata.*;
import kotlinx.metadata.jvm.*;
import proguard.classfile.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.util.*;
import proguard.classfile.kotlin.KotlinMetadataInitializer.MetadataType;
import proguard.classfile.visitor.ClassVisitor;

import static proguard.classfile.kotlin.KotlinConstants.*;

/**
 * This class visitor writes the information stored in a Clazz's kotlinMetadata field
 * to a @kotlin/Metadata annotation on the class.
 */
public class KotlinMetadataWriter
extends    SimplifiedVisitor
implements KotlinMetadataVisitor,

           // Implementation interfaces.
           ElementValueVisitor
{
    private final ClassVisitor extraClassVisitor;

    private int      k;
    private int[]    mv;
    private int[]    bv;
    private String[] d1;
    private String[] d2;
    private int      xi;
    private String   xs;
    private String   pn;

    private ConstantPoolEditor constantPoolEditor;

    private static ConstantPoolShrinker constantPoolShrinker = new ConstantPoolShrinker();

    private MetadataType currentType;


    public KotlinMetadataWriter()
    {
        this(null);
    }

    public KotlinMetadataWriter(ClassVisitor extraClassVisitor)
    {
        this.extraClassVisitor = extraClassVisitor;
    }


    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        this.k  = kotlinMetadata.k;
        this.mv = kotlinMetadata.mv;
        this.bv = kotlinMetadata.bv;
        this.xi = kotlinMetadata.xi;
        this.xs = kotlinMetadata.xs;
        this.pn = kotlinMetadata.pn;

        switch (k)
        {
            case METADATA_KIND_CLASS:
                kotlinMetadata.accept(clazz, new KotlinClassConstructor());           break;
            case METADATA_KIND_FILE_FACADE:
                kotlinMetadata.accept(clazz, new KotlinFileFacadeConstructor());      break;
            case METADATA_KIND_SYNTHETIC_CLASS:
                kotlinMetadata.accept(clazz, new KotlinSyntheticClassConstructor());  break;
            case METADATA_KIND_MULTI_FILE_CLASS_FACADE:
                kotlinMetadata.accept(clazz, new KotlinMultiFileFacadeConstructor()); break;
            case METADATA_KIND_MULTI_FILE_CLASS_PART:
                kotlinMetadata.accept(clazz, new KotlinMultiFilePartConstructor());   break;
        }

        // Pass the new data to the .read() method as a sanity check.
        KotlinClassMetadata md = KotlinClassMetadata.read(new KotlinClassHeader(k, mv, bv, d1, d2, xs, pn, xi));
        if (md == null)
        {
            System.err.println("Encountered corrupt Kotlin metadata in class " +
                               clazz.getName() +
                               ". Not processing the metadata for this class.");
            return;
        }

        this.constantPoolEditor = new ConstantPoolEditor((ProgramClass) clazz);
        clazz.accept(new AllAttributeVisitor(
                     new AttributeNameFilter(ClassConstants.ATTR_RuntimeVisibleAnnotations,
                     new AllAnnotationVisitor(
                     new AnnotationTypeFilter(TYPE_KOTLIN_METADATA,
                                              new AllElementValueVisitor(this))))));

        // We must shrink the constant pool because Kotlin metadata annotations
        // refer to Utf8 constants which have potentially been renamed or removed after
        // the Kotlin processing, leaving unused (and unobfuscated) constants in the constant pool.
        // TODO maybe this can be done as part of the writing of new Utf8 constants?
        clazz.accept(constantPoolShrinker);

        if (extraClassVisitor != null)
        {
            clazz.accept(extraClassVisitor);
        }
    }


    // Implementations for ElementValueVisitor.
    @Override
    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
        this.currentType = MetadataType.valueOf(constantElementValue.getMethodName(clazz));

        switch (currentType)
        {
            case k:  constantElementValue.u2constantValueIndex = constantPoolEditor.addIntegerConstant(k);  break;
            case xi: constantElementValue.u2constantValueIndex = constantPoolEditor.addIntegerConstant(xi); break;
            case xs: constantElementValue.u2constantValueIndex = constantPoolEditor.addUtf8Constant(xs);    break;
            case pn: constantElementValue.u2constantValueIndex = constantPoolEditor.addUtf8Constant(pn);    break;
        }
    }

    @Override
    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        this.currentType = MetadataType.valueOf(arrayElementValue.getMethodName(clazz));


        switch (currentType)
        {
            case mv:
                arrayElementValue.u2elementValuesCount = mv.length;
                ElementValue[] newMvElementValues = new ElementValue[mv.length];
                for (int k = 0; k < mv.length; k++)
                {
                    newMvElementValues[k] =
                        new ConstantElementValue('I',
                                                 0,
                                                 constantPoolEditor.addIntegerConstant(mv[k]));
                }
                arrayElementValue.elementValues = newMvElementValues;
                break;
            case bv:
                arrayElementValue.u2elementValuesCount = bv.length;
                ElementValue[] newBvElementValues = new ElementValue[bv.length];
                for (int k = 0; k < bv.length; k++)
                {
                    newBvElementValues[k] =
                        new ConstantElementValue('I',
                                                 0,
                                                 constantPoolEditor.addIntegerConstant(bv[k]));
                }
                arrayElementValue.elementValues = newBvElementValues;
                break;
            case d1:
                arrayElementValue.u2elementValuesCount = d1.length;
                ElementValue[] newD1ElementValues = new ElementValue[d1.length];
                for (int k = 0; k < d1.length; k++)
                {
                    newD1ElementValues[k] =
                        new ConstantElementValue('s',
                                                 0,
                                                 constantPoolEditor.addUtf8Constant(d1[k]));
                }
                arrayElementValue.elementValues = newD1ElementValues;
                break;
            case d2:
                arrayElementValue.u2elementValuesCount = d2.length;
                ElementValue[] newD2ElementValues = new ElementValue[d2.length];
                for (int k = 0; k < d2.length; k++)
                {
                    newD2ElementValues[k] =
                        new ConstantElementValue('s',
                                                 0,
                                                 constantPoolEditor.addUtf8Constant(d2[k]));
                }
                arrayElementValue.elementValues = newD2ElementValues;
                break;
        }
    }

    private class KotlinContractConstructor
    extends    SimplifiedVisitor
    implements KotlinContractVisitor
    {
        private KmFunctionVisitor kmdFunctionVisitor;


        KotlinContractConstructor(KmFunctionVisitor kmdFunctionVisitor)
        {
            this.kmdFunctionVisitor = kmdFunctionVisitor;
        }

        // Implementations for KotlinContractVisitor.
        @Override
        public void visitContract(Clazz                  clazz,
                                  KotlinMetadata         kotlinMetadata,
                                  KotlinFunctionMetadata kotlinFunctionMetadata,
                                  KotlinContractMetadata kotlinContractMetadata)
        {
            KmContractVisitor kmContractVisitor = kmdFunctionVisitor.visitContract();

            kotlinContractMetadata.effectsAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, new KotlinEffectConstructor(kmContractVisitor));

            kmContractVisitor.visitEnd();
        }
    }

    private class KotlinEffectConstructor
    implements KotlinEffectVisitor
    {
        private final KmContractVisitor kmContractVisitor;
        private KotlinEffectConstructor(KmContractVisitor kmContractVisitor) { this.kmContractVisitor = kmContractVisitor; }


        // Implementatins for KotlinEffectVisitor.
        @Override
        public void visitEffect(Clazz                  clazz,
                                KotlinMetadata         kotlinMetadata,
                                KotlinFunctionMetadata kotlinFunctionMetadata,
                                KotlinContractMetadata kotlinContractMetadata,
                                KotlinEffectMetadata   kotlinEffectMetadata)
        {
            KmEffectVisitor kmEffectVisitor = kmContractVisitor.visitEffect(kotlinEffectMetadata.effectType,
                                                                            kotlinEffectMetadata.invocationKind);

            kotlinEffectMetadata.conclusionOfConditionalEffectAccept(clazz,
                                                                     new KotlinEffectExprConstructor(
                                                                     kmEffectVisitor.visitConclusionOfConditionalEffect()));

            kotlinEffectMetadata.constructorArgumentAccept(clazz,
                                                           new KotlinEffectExprConstructor(
                                                           kmEffectVisitor.visitConstructorArgument()));

            kmEffectVisitor.visitEnd();
        }
    }

    private class KotlinEffectExprConstructor
    implements KotlinEffectExprVisitor
    {
        private final KmEffectExpressionVisitor kmEffectExpressionVisitor;


        private KotlinEffectExprConstructor(KmEffectExpressionVisitor kmEffectExpressionVisitor)
        {
            this.kmEffectExpressionVisitor = kmEffectExpressionVisitor;
        }


        // Implementations for KotlinEffectExprVisitor.
        @Override
        public void visitEffectExpression(Clazz                          clazz,
                                          KotlinEffectMetadata           kotlinEffectMetadata,
                                          KotlinEffectExpressionMetadata kotlinEffectExpressionMetadata)
        {

            KmEffectExpressionVisitor andVisitor = this.kmEffectExpressionVisitor.visitAndArgument();
            kotlinEffectExpressionMetadata.andRightHandSideAccept(clazz,
                                                                  kotlinEffectMetadata,
                                                                  new KotlinEffectExprConstructor(andVisitor));

            KmEffectExpressionVisitor orVisitor = this.kmEffectExpressionVisitor.visitOrArgument();
            kotlinEffectExpressionMetadata.orRightHandSideAccept(clazz,
                                                                 kotlinEffectMetadata,
                                                                 new KotlinEffectExprConstructor(orVisitor));

            kotlinEffectExpressionMetadata.typeOfIsAccept(clazz,
                                                          new TypeConstructor(kmEffectExpressionVisitor));

            if (kotlinEffectExpressionMetadata.hasConstantValue)
            {
                kmEffectExpressionVisitor.visitConstantValue(kotlinEffectExpressionMetadata.constantValue);
            }

            kmEffectExpressionVisitor.visit(kotlinEffectExpressionMetadata.flags.asInt(),
                                            kotlinEffectExpressionMetadata.parameterIndex);

            kmEffectExpressionVisitor.visitEnd();
        }
    }

    private class KotlinDeclarationContainerConstructor
    implements KotlinPropertyVisitor,
               KotlinFunctionVisitor,
               KotlinTypeAliasVisitor
    {
        KmDeclarationContainerVisitor kmdWriter;

        KmPropertyVisitor                       kmdPropertyVisitor;
        JvmDeclarationContainerExtensionVisitor extensionVisitor;

        KotlinDeclarationContainerConstructor(KmDeclarationContainerVisitor classKmdWriter)
        {
            kmdWriter = classKmdWriter;
        }


        // Simplifications for KotlinPropertyVisitor.
        @Override
        public void visitAnyProperty(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            kotlinPropertyMetadata.typeAccept(clazz,
                                              kotlinDeclarationContainerMetadata,
                                              new TypeConstructor(kmdPropertyVisitor));
            kotlinPropertyMetadata.receiverTypeAccept(clazz,
                                                      kotlinDeclarationContainerMetadata,
                                                      new TypeConstructor(kmdPropertyVisitor));
            kotlinPropertyMetadata.setterParametersAccept(clazz,
                                                          kotlinDeclarationContainerMetadata,
                                                          new ValueParameterConstructor(kmdPropertyVisitor));
            kotlinPropertyMetadata.typeParametersAccept(clazz,
                                                        kotlinDeclarationContainerMetadata,
                                                        new TypeParameterConstructor(kmdPropertyVisitor));
            kotlinPropertyMetadata.versionRequirementAccept(clazz,
                                                            kotlinDeclarationContainerMetadata,
                                                            new VersionRequirementConstructor(kmdPropertyVisitor));

            JvmPropertyExtensionVisitor ext =
                (JvmPropertyExtensionVisitor) kmdPropertyVisitor.visitExtensions(JvmPropertyExtensionVisitor.TYPE);

            ext.visit(kotlinPropertyMetadata.flags.asInt(), kotlinPropertyMetadata.backingFieldSignature,
                      kotlinPropertyMetadata.getterSignature,
                      kotlinPropertyMetadata.setterSignature);

            if (kotlinPropertyMetadata.syntheticMethodForAnnotations != null)
            {
                ext.visitSyntheticMethodForAnnotations(kotlinPropertyMetadata.syntheticMethodForAnnotations);
            }

            ext.visitEnd();

            kmdPropertyVisitor.visitEnd();
        }

        @Override
        public void visitProperty(Clazz                              clazz,
                                  KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                  KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            kmdPropertyVisitor =
                kmdWriter.visitProperty(kotlinPropertyMetadata.flags.asInt(),
                                        kotlinPropertyMetadata.name,
                                        kotlinPropertyMetadata.getterFlags.asInt(),
                                        kotlinPropertyMetadata.setterFlags.asInt());

            visitAnyProperty(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);
        }

        @Override
        public void visitDelegatedProperty(Clazz                              clazz,
                                           KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                           KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            kmdPropertyVisitor =
                extensionVisitor.visitLocalDelegatedProperty(kotlinPropertyMetadata.flags.asInt(),
                                                             kotlinPropertyMetadata.name,
                                                             kotlinPropertyMetadata.getterFlags.asInt(),
                                                             kotlinPropertyMetadata.setterFlags.asInt());

            visitAnyProperty(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);
        }


        // Simplifications for KotlinFunctionVisitor.
        @Override
        public void visitAnyFunction(Clazz clazz, KotlinMetadata kotlinMetadata, KotlinFunctionMetadata kotlinFunctionMetadata) {}

        @Override
        public void visitFunction(Clazz                              clazz,
                                  KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                  KotlinFunctionMetadata             kotlinFunctionMetadata)
        {
            KmFunctionVisitor kmdFunctionVisitor =
                kmdWriter.visitFunction(kotlinFunctionMetadata.flags.asInt(),
                                        kotlinFunctionMetadata.name);

            kotlinFunctionMetadata.valueParametersAccept(clazz,
                                                         kotlinDeclarationContainerMetadata,
                                                         new ValueParameterConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.returnTypeAccept(clazz,
                                                    kotlinDeclarationContainerMetadata,
                                                    new TypeConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.receiverTypeAccept(clazz,
                                                      kotlinDeclarationContainerMetadata,
                                                      new TypeConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.typeParametersAccept(clazz,
                                                        kotlinDeclarationContainerMetadata,
                                                        new TypeParameterConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.versionRequirementAccept(clazz,
                                                            kotlinDeclarationContainerMetadata,
                                                            new VersionRequirementConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.contractsAccept(clazz,
                                                   kotlinDeclarationContainerMetadata,
                                                   new KotlinContractConstructor(kmdFunctionVisitor));

            JvmFunctionExtensionVisitor ext =
                (JvmFunctionExtensionVisitor) kmdFunctionVisitor.visitExtensions(JvmFunctionExtensionVisitor.TYPE);
            ext.visit(kotlinFunctionMetadata.jvmSignature);
            if (kotlinFunctionMetadata.lambdaClassOriginName != null)
            {
                ext.visitLambdaClassOriginName(kotlinFunctionMetadata.lambdaClassOriginName);
            }
            ext.visitEnd();

            kmdFunctionVisitor.visitEnd();
        }


        // Implementations for KotlinTypeAliasVisitor
        @Override
        public void visitTypeAlias(Clazz                              clazz,
                                   KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                   KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
        {
            KmTypeAliasVisitor kmdAliasVisitor =
                kmdWriter.visitTypeAlias(kotlinTypeAliasMetadata.flags.asInt(),
                                         kotlinTypeAliasMetadata.name);

            kotlinTypeAliasMetadata.typeParametersAccept(clazz,
                                                         kotlinDeclarationContainerMetadata,
                                                         new TypeParameterConstructor(kmdAliasVisitor));
            kotlinTypeAliasMetadata.underlyingTypeAccept(clazz,
                                                         kotlinDeclarationContainerMetadata,
                                                         new TypeConstructor(kmdAliasVisitor));
            kotlinTypeAliasMetadata.expandedTypeAccept(clazz,
                                                       kotlinDeclarationContainerMetadata,
                                                       new TypeConstructor(kmdAliasVisitor));
            kotlinTypeAliasMetadata.versionRequirementAccept(clazz,
                                                             kotlinDeclarationContainerMetadata,
                                                             new VersionRequirementConstructor(kmdAliasVisitor));

            for (KotlinMetadataAnnotation antn : kotlinTypeAliasMetadata.annotations)
            {
                kmdAliasVisitor.visitAnnotation(antn.kmAnnotation);
            }

            kmdAliasVisitor.visitEnd();
        }


        // Implementations for KotlinMetadataVisitor.
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}
    }


    /**
     * This utility class constructs the protobuf (d1 and d2 arrays) for Kotlin class (k == 1) metadata.
     */
    private class KotlinClassConstructor
    extends KotlinDeclarationContainerConstructor
    implements KotlinMetadataVisitor,

               // Implementation interfaces.
               KotlinConstructorVisitor
    {
        KotlinClassMetadata.Class.Writer classKmdWriter;

        KotlinClassConstructor()
        {
            this(new KotlinClassMetadata.Class.Writer());
        }

        private KotlinClassConstructor(KotlinClassMetadata.Class.Writer classKmdWriter)
        {
            super(classKmdWriter);
            this.classKmdWriter = classKmdWriter;
        }


        // Implementations for KotlinMetadataVisitor.
        @Override
        public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            classKmdWriter.visit(kotlinClassKindMetadata.flags.asInt(),
                                 kotlinClassKindMetadata.className.replace('$','.'));

            if (kotlinClassKindMetadata.companionObjectName != null)
            {
                classKmdWriter.visitCompanionObject(kotlinClassKindMetadata.companionObjectName);
            }

            kotlinClassKindMetadata.propertiesAccept(clazz,  this);
            kotlinClassKindMetadata.functionsAccept(clazz,   this);
            kotlinClassKindMetadata.typeAliasesAccept(clazz, this);

            for (String enumEntry : kotlinClassKindMetadata.enumEntryNames)
            {
                classKmdWriter.visitEnumEntry(enumEntry);
            }

            for (String nestedClass : kotlinClassKindMetadata.nestedClassNames)
            {
                classKmdWriter.visitNestedClass(nestedClass);
            }

            for (String sealedSubClass : kotlinClassKindMetadata.sealedSubclassNames)
            {
                classKmdWriter.visitSealedSubclass(sealedSubClass.replace('$', '.'));
            }

            kotlinClassKindMetadata.constructorsAccept(clazz,       this);
            kotlinClassKindMetadata.superTypesAccept(clazz,         new TypeConstructor(classKmdWriter));
            kotlinClassKindMetadata.typeParametersAccept(clazz,     new TypeParameterConstructor(classKmdWriter));
            kotlinClassKindMetadata.versionRequirementAccept(clazz, new VersionRequirementConstructor(classKmdWriter));

            // Extensions.
            JvmClassExtensionVisitor ext =
                (JvmClassExtensionVisitor) classKmdWriter.visitExtensions(JvmClassExtensionVisitor.TYPE);

            extensionVisitor = ext;
            kotlinClassKindMetadata.delegatedPropertiesAccept(clazz, this);

            if (kotlinClassKindMetadata.anonymousObjectOriginName != null)
            {
                ext.visitAnonymousObjectOriginName(kotlinClassKindMetadata.anonymousObjectOriginName);
            }

            ext.visitEnd();

            // Finish.
            classKmdWriter.visitEnd();

            // Finally store the protobuf contents in the fields of the enclosing class.
            KotlinClassHeader header = classKmdWriter.write().getHeader();
            d1 = header.getData1();
            d2 = header.getData2();
        }


        // Implementations for KotlinConstructorVisitor.
        @Override
        public void visitConstructor(Clazz                     clazz,
                                     KotlinClassKindMetadata   kotlinClassKindMetadata,
                                     KotlinConstructorMetadata kotlinConstructorMetadata)
        {
            KmConstructorVisitor constructorVis =
                classKmdWriter.visitConstructor(kotlinConstructorMetadata.flags.asInt());

            kotlinConstructorMetadata.valueParametersAccept(clazz,
                                                            kotlinClassKindMetadata,
                                                            new ValueParameterConstructor(constructorVis));

            kotlinConstructorMetadata.versionRequirementAccept(clazz,
                                                               kotlinClassKindMetadata,
                                                               new VersionRequirementConstructor(constructorVis));

            // Extensions.
            if (kotlinConstructorMetadata.jvmSignature != null)
            {
                JvmConstructorExtensionVisitor constExtVis =
                    (JvmConstructorExtensionVisitor)constructorVis.visitExtensions(JvmConstructorExtensionVisitor.TYPE);

                constExtVis.visit(kotlinConstructorMetadata.jvmSignature);

                //TODO no visitEnd() ?
            }

            // Finish.
            constructorVis.visitEnd();
        }
    }


    private class ValueParameterConstructor
    implements KotlinValueParameterVisitor
    {
        private KmValueParameterVisitor valParamVis;

        private KmConstructorVisitor constructorVis;
        ValueParameterConstructor(KmConstructorVisitor constructorVis) { this.constructorVis = constructorVis; }

        private KmPropertyVisitor propertyVis;
        ValueParameterConstructor(KmPropertyVisitor propertyVis) { this.propertyVis = propertyVis; }

        private KmFunctionVisitor functionVis;
        ValueParameterConstructor(KmFunctionVisitor functionVis) { this.functionVis = functionVis; }


        // Implementations for KotlinValueParameterVisitor.
        @Override
        public void visitAnyValueParameter(Clazz clazz,
                                           KotlinValueParameterMetadata kotlinValueParameterMetadata) {}

        @Override
        public void visitConstructorValParameter(Clazz                        clazz,
                                                 KotlinClassKindMetadata      kotlinClassKindMetadata,
                                                 KotlinConstructorMetadata    kotlinConstructorMetadata,
                                                 KotlinValueParameterMetadata kotlinValueParameterMetadata)
        {
            valParamVis =
                constructorVis.visitValueParameter(kotlinValueParameterMetadata.flags.asInt(),
                                                   kotlinValueParameterMetadata.parameterName);

            kotlinValueParameterMetadata.typeAccept(clazz,
                                                    kotlinClassKindMetadata,
                                                    kotlinConstructorMetadata,
                                                    new TypeConstructor(valParamVis));

            valParamVis.visitEnd();
        }

        @Override
        public void visitPropertyValParameter(Clazz                              clazz,
                                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                              KotlinPropertyMetadata             kotlinPropertyMetadata,
                                              KotlinValueParameterMetadata       kotlinValueParameterMetadata)
        {
            valParamVis =
                propertyVis.visitSetterParameter(kotlinValueParameterMetadata.flags.asInt(),
                                                 kotlinValueParameterMetadata.parameterName);

            kotlinValueParameterMetadata.typeAccept(clazz,
                                                    kotlinDeclarationContainerMetadata,
                                                    kotlinPropertyMetadata,
                                                    new TypeConstructor(valParamVis));

            valParamVis.visitEnd();
        }

        @Override
        public void visitFunctionValParameter(Clazz                        clazz,
                                              KotlinMetadata               kotlinMetadata,
                                              KotlinFunctionMetadata       kotlinFunctionMetadata,
                                              KotlinValueParameterMetadata kotlinValueParameterMetadata)
        {
            valParamVis =
                functionVis.visitValueParameter(kotlinValueParameterMetadata.flags.asInt(),
                                                kotlinValueParameterMetadata.parameterName);

            kotlinValueParameterMetadata.typeAccept(clazz,
                                                    kotlinMetadata,
                                                    kotlinFunctionMetadata,
                                                    new TypeConstructor(valParamVis));

            valParamVis.visitEnd();
        }
    }


    private class TypeConstructor
    implements KotlinTypeVisitor
    {
        private KmTypeVisitor typeVis;

        private KmTypeVisitor nestedTypeVis;
        TypeConstructor(KmTypeVisitor nestedTypeVis) { this.nestedTypeVis = nestedTypeVis; }

        private KmValueParameterVisitor valParamVis;
        TypeConstructor(KmValueParameterVisitor valParamVis) { this.valParamVis = valParamVis; }

        private KmClassVisitor classVis;
        TypeConstructor(KmClassVisitor classVis) { this.classVis = classVis; }

        private KmPropertyVisitor propertyVis;
        TypeConstructor(KmPropertyVisitor propertyVis) { this.propertyVis = propertyVis; }

        private KmFunctionVisitor functionVis;
        TypeConstructor(KmFunctionVisitor functionVis) { this.functionVis = functionVis; }

        private KmTypeAliasVisitor aliasVis;
        TypeConstructor(KmTypeAliasVisitor aliasVis) { this.aliasVis = aliasVis; }

        private KmTypeParameterVisitor typeParamVis;
        TypeConstructor(KmTypeParameterVisitor typeParamVis) { this.typeParamVis = typeParamVis; }

        private KmEffectExpressionVisitor effectExpressionVis;
        TypeConstructor(KmEffectExpressionVisitor effectExpressionVis) { this.effectExpressionVis = effectExpressionVis; }


        // Implementations for KotlinTypeVisitor.

        @Override
        public void visitTypeUpperBound(Clazz              clazz,
                                        KotlinTypeMetadata boundedType,
                                        KotlinTypeMetadata upperBound)
        {
            typeVis = nestedTypeVis.visitFlexibleTypeUpperBound(boundedType.flags.asInt(), upperBound.flexibilityID);

            visitAnyType(null, upperBound);
        }

        @Override
        public void visitAbbreviation(Clazz              clazz,
                                      KotlinTypeMetadata abbreviatedType,
                                      KotlinTypeMetadata abbreviation)
        {
            typeVis = nestedTypeVis.visitAbbreviatedType(abbreviatedType.flags.asInt());

            visitAnyType(null, abbreviation);
        }

        @Override
        public void visitParameterUpperBound(Clazz                       clazz,
                                             KotlinTypeParameterMetadata boundedTypeParameter,
                                             KotlinTypeMetadata          upperBound)
        {
            typeVis = typeParamVis.visitUpperBound(upperBound.flags.asInt());

            visitAnyType(null, upperBound);
        }

        @Override
        public void visitEffectExprTypeOfIs(Clazz                          clazz,
                                            KotlinEffectExpressionMetadata kotlinEffectExprMetadata,
                                            KotlinTypeMetadata             typeOfIs)
        {
            typeVis = effectExpressionVis.visitIsInstanceType(typeOfIs.flags.asInt());

            visitAnyType(null, typeOfIs);
        }

        @Override
        public void visitTypeArgument(Clazz              clazz,
                                      KotlinTypeMetadata kotlinTypeMetadata,
                                      KotlinTypeMetadata typeArgument)
        {
            typeVis = nestedTypeVis.visitArgument(typeArgument.flags.asInt(), typeArgument.variance);

            visitAnyType(null, typeArgument);
        }

        @Override
        public void visitStarProjection(Clazz              clazz,
                                        KotlinTypeMetadata typeWithStarArg)
        {
            nestedTypeVis.visitStarProjection();
        }

        @Override
        public void visitOuterClass(Clazz              clazz,
                                    KotlinTypeMetadata innerClass,
                                    KotlinTypeMetadata outerClass)
        {
            typeVis = nestedTypeVis.visitOuterType(outerClass.flags.asInt());

            visitAnyType(null, outerClass);
        }


        @Override
        public void visitConstructorValParamType(Clazz                              clazz,
                                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                 KotlinConstructorMetadata          kotlinConstructorMetadata,
                                                 KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                                 KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = valParamVis.visitType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }


        @Override
        public void visitConstructorValParamVarArgType(Clazz                              clazz,
                                                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                       KotlinConstructorMetadata          kotlinConstructorMetadata,
                                                       KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                                       KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = valParamVis.visitType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitSuperType(Clazz                   clazz,
                                   KotlinClassKindMetadata kotlinMetadata,
                                   KotlinTypeMetadata      kotlinTypeMetadata)
        {
            typeVis = classVis.visitSupertype(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitPropertyType(Clazz                              clazz,
                                      KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                      KotlinPropertyMetadata             kotlinPropertyMetadata,
                                      KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = propertyVis.visitReturnType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitPropertyReceiverType(Clazz                              clazz,
                                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                              KotlinPropertyMetadata             kotlinPropertyMetadata,
                                              KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = propertyVis.visitReceiverParameterType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitPropertyValParamType(Clazz                              clazz,
                                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                              KotlinPropertyMetadata             kotlinPropertyMetadata,
                                              KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                              KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = valParamVis.visitType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitPropertyValParamVarArgType(Clazz                              clazz,
                                                    KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                    KotlinPropertyMetadata             kotlinPropertyMetadata,
                                                    KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                                    KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = valParamVis.visitVarargElementType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitFunctionReturnType(Clazz                  clazz,
                                            KotlinMetadata         kotlinMetadata,
                                            KotlinFunctionMetadata kotlinFunctionMetadata,
                                            KotlinTypeMetadata     kotlinTypeMetadata)
        {
            typeVis = functionVis.visitReturnType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitFunctionReceiverType(Clazz                  clazz,
                                              KotlinMetadata         kotlinMetadata,
                                              KotlinFunctionMetadata kotlinFunctionMetadata,
                                              KotlinTypeMetadata     kotlinTypeMetadata)
        {
            typeVis = functionVis.visitReceiverParameterType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitFunctionValParamType(Clazz                        clazz,
                                              KotlinMetadata               kotlinMetadata,
                                              KotlinFunctionMetadata       kotlinFunctionMetadata,
                                              KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                              KotlinTypeMetadata           kotlinTypeMetadata)
        {
            typeVis = valParamVis.visitType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitFunctionValParamVarArgType(Clazz                        clazz,
                                                    KotlinMetadata               kotlinMetadata,
                                                    KotlinFunctionMetadata       kotlinFunctionMetadata,
                                                    KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                    KotlinTypeMetadata           kotlinTypeMetadata)
        {
            typeVis = valParamVis.visitVarargElementType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitAliasUnderlyingType(Clazz                              clazz,
                                             KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                             KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                             KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = aliasVis.visitUnderlyingType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }

        @Override
        public void visitAliasExpandedType(Clazz                              clazz,
                                           KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                           KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                           KotlinTypeMetadata                 kotlinTypeMetadata)
        {
            typeVis = aliasVis.visitExpandedType(kotlinTypeMetadata.flags.asInt());

            visitAnyType(clazz, kotlinTypeMetadata);
        }


        // Small helper methods.
        @Override
        public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
        {
            //TODO use classifier?
            if (kotlinTypeMetadata.className != null)
            {
                typeVis.visitClass(kotlinTypeMetadata.className);
            }

            if (kotlinTypeMetadata.typeParamID >= 0)
            {
                typeVis.visitTypeParameter(kotlinTypeMetadata.typeParamID);
            }

            if (kotlinTypeMetadata.aliasName != null)
            {
                typeVis.visitTypeAlias(kotlinTypeMetadata.aliasName);
            }

            kotlinTypeMetadata.abbreviationAccept( clazz, new TypeConstructor(typeVis));
            kotlinTypeMetadata.outerClassAccept(   clazz, new TypeConstructor(typeVis));
            kotlinTypeMetadata.typeArgumentsAccept(clazz, new TypeConstructor(typeVis));
            kotlinTypeMetadata.upperBoundsAccept(  clazz, new TypeConstructor(typeVis));

            // Extensions.
            JvmTypeExtensionVisitor typeExtVis =
                (JvmTypeExtensionVisitor)typeVis.visitExtensions(JvmTypeExtensionVisitor.TYPE);

            typeExtVis.visit(kotlinTypeMetadata.isRaw);

            for (KotlinMetadataAnnotation kman : kotlinTypeMetadata.annotations)
            {
                typeExtVis.visitAnnotation(kman.kmAnnotation);
            }

            typeExtVis.visitEnd();

            typeVis.visitEnd();
        }
    }


    private class TypeParameterConstructor
    implements KotlinTypeParameterVisitor
    {
        private KmTypeParameterVisitor typeParamVis;

        private KmClassVisitor classVis;
        TypeParameterConstructor(KmClassVisitor classVis) { this.classVis = classVis; }

        private KmPropertyVisitor propertyVis;
        TypeParameterConstructor(KmPropertyVisitor propertyVis) { this.propertyVis = propertyVis; }

        private KmFunctionVisitor functionVis;
        TypeParameterConstructor(KmFunctionVisitor functionVis) { this.functionVis = functionVis; }

        private KmTypeAliasVisitor aliasVis;
        TypeParameterConstructor(KmTypeAliasVisitor aliasVis) { this.aliasVis = aliasVis; }


        // Implementations for KotlinTypeParameterVisitor.

        @Override
        public void visitClassTypeParameter(Clazz                       clazz,
                                            KotlinMetadata              kotlinMetadata,
                                            KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
        {
            typeParamVis = classVis.visitTypeParameter(kotlinTypeParameterMetadata.flags.asInt(),
                                                       kotlinTypeParameterMetadata.name,
                                                       kotlinTypeParameterMetadata.id,
                                                       kotlinTypeParameterMetadata.variance);

            visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
        }

        @Override
        public void visitPropertyTypeParameter(Clazz                              clazz,
                                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                               KotlinPropertyMetadata             kotlinPropertyMetadata,
                                               KotlinTypeParameterMetadata        kotlinTypeParameterMetadata)
        {
            typeParamVis = propertyVis.visitTypeParameter(kotlinTypeParameterMetadata.flags.asInt(),
                                                          kotlinTypeParameterMetadata.name,
                                                          kotlinTypeParameterMetadata.id,
                                                          kotlinTypeParameterMetadata.variance);

            visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
        }

        @Override
        public void visitFunctionTypeParameter(Clazz                       clazz,
                                               KotlinMetadata              kotlinMetadata,
                                               KotlinFunctionMetadata      kotlinFunctionMetadata,
                                               KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
        {
            typeParamVis = functionVis.visitTypeParameter(kotlinTypeParameterMetadata.flags.asInt(),
                                                          kotlinTypeParameterMetadata.name,
                                                          kotlinTypeParameterMetadata.id,
                                                          kotlinTypeParameterMetadata.variance);

            visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
        }

        @Override
        public void visitAliasTypeParameter(Clazz                              clazz,
                                            KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                            KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                            KotlinTypeParameterMetadata        kotlinTypeParameterMetadata)
        {
            typeParamVis = aliasVis.visitTypeParameter(kotlinTypeParameterMetadata.flags.asInt(),
                                                       kotlinTypeParameterMetadata.name,
                                                       kotlinTypeParameterMetadata.id,
                                                       kotlinTypeParameterMetadata.variance);

            visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
        }


        // Small helper methods.
        @Override
        public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
        {
            kotlinTypeParameterMetadata.upperBoundsAccept(clazz,
                                                          new TypeConstructor(typeParamVis));

            // Extensions.
            JvmTypeParameterExtensionVisitor typeParamExtVis =
                (JvmTypeParameterExtensionVisitor)typeParamVis.visitExtensions(JvmTypeParameterExtensionVisitor.TYPE);

            for (KotlinMetadataAnnotation kman : kotlinTypeParameterMetadata.annotations)
            {
                typeParamExtVis.visitAnnotation(kman.kmAnnotation);
            }

            typeParamExtVis.visitEnd();

            typeParamVis.visitEnd();
        }
    }


    /**
     * This utility class constructs the protobuf (d1 and d2 arrays) for Kotlin file facade (k == 2) metadata.
     */
    private class KotlinFileFacadeConstructor
    extends    KotlinDeclarationContainerConstructor
    implements KotlinMetadataVisitor
    {
        private final KotlinClassMetadata.FileFacade.Writer facadeKmdWriter;

        KotlinFileFacadeConstructor()
        {
            this(new KotlinClassMetadata.FileFacade.Writer());
        }

        private KotlinFileFacadeConstructor(KotlinClassMetadata.FileFacade.Writer facadeKmdWriter)
        {
            super(facadeKmdWriter);
            this.facadeKmdWriter = facadeKmdWriter;
        }


        // Implementations for KotlinMetadataVisitor.
        @Override
        public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
        {
            kotlinFileFacadeKindMetadata.propertiesAccept(clazz, this);
            kotlinFileFacadeKindMetadata.functionsAccept(clazz, this);
            kotlinFileFacadeKindMetadata.typeAliasesAccept(clazz, this);

            JvmPackageExtensionVisitor ext =
                (JvmPackageExtensionVisitor) kmdWriter.visitExtensions(JvmPackageExtensionVisitor.TYPE);

            extensionVisitor = ext;
            kotlinFileFacadeKindMetadata.delegatedPropertiesAccept(clazz, this);

            ext.visitEnd();

            facadeKmdWriter.visitEnd();

            // Finally store the protobuf contents in the fields of the enclosing class.
            KotlinClassHeader header = facadeKmdWriter.write().getHeader();
            d1 = header.getData1();
            d2 = header.getData2();
        }
    }


    /**
     * This utility class constructs the protobuf (d1 and d2 arrays) for Kotlin synthetic class (k == 3) metadata.
     */
    private class KotlinSyntheticClassConstructor
    extends SimplifiedVisitor
    implements KotlinMetadataVisitor,

               // Implementation interfaces.
               KotlinFunctionVisitor
    {
        private       KotlinSyntheticClassKindMetadata          md;
        private final KotlinClassMetadata.SyntheticClass.Writer kmdWriter;


        KotlinSyntheticClassConstructor()
        {
            this.kmdWriter = new KotlinClassMetadata.SyntheticClass.Writer();
        }


        // Implementations for KotlinMetadataVisitor.
        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

        @Override
        public void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
        {
            this.md = kotlinSyntheticClassKindMetadata;

            md.functionsAccept(clazz, this);

            kmdWriter.visitEnd();

            // Finally store the protobuf contents in the fields of the enclosing class.
            KotlinClassHeader header = kmdWriter.write().getHeader();
            d1 = header.getData1();
            d2 = header.getData2();
        }


        // Implementations for KotlinFunctionVisitor.
        @Override
        public void visitAnyFunction(Clazz                  clazz,
                                     KotlinMetadata         kotlinMetadata,
                                     KotlinFunctionMetadata kotlinFunctionMetadata) {}

        @Override
        public void visitSyntheticFunction(Clazz                            clazz,
                                           KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                           KotlinFunctionMetadata           kotlinFunctionMetadata)
        {
            KmFunctionVisitor kmdFunctionVisitor =
                kmdWriter.visitFunction(kotlinFunctionMetadata.flags.asInt(),
                                        kotlinFunctionMetadata.name);

            kotlinFunctionMetadata.valueParametersAccept(clazz,
                                                         kotlinSyntheticClassKindMetadata,
                                                         new ValueParameterConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.returnTypeAccept(clazz,
                                                    kotlinSyntheticClassKindMetadata,
                                                    new TypeConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.receiverTypeAccept(clazz,
                                                      kotlinSyntheticClassKindMetadata,
                                                      new TypeConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.typeParametersAccept(clazz,
                                                        kotlinSyntheticClassKindMetadata,
                                                        new TypeParameterConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.versionRequirementAccept(clazz,
                                                            kotlinSyntheticClassKindMetadata,
                                                            new VersionRequirementConstructor(kmdFunctionVisitor));
            kotlinFunctionMetadata.contractsAccept(clazz,
                                                   kotlinSyntheticClassKindMetadata,
                                                   new KotlinContractConstructor(kmdFunctionVisitor));

            JvmFunctionExtensionVisitor ext =
                (JvmFunctionExtensionVisitor) kmdFunctionVisitor.visitExtensions(JvmFunctionExtensionVisitor.TYPE);

            ext.visit(kotlinFunctionMetadata.jvmSignature);

            if (kotlinFunctionMetadata.lambdaClassOriginName != null)
            {
                ext.visitLambdaClassOriginName(kotlinFunctionMetadata.lambdaClassOriginName);
            }

            ext.visitEnd();

            kmdFunctionVisitor.visitEnd();
        }
    }


    /**
     * This utility class constructs the d1 array for Kotlin file facade (k == 2) metadata.
     */
    private class KotlinMultiFileFacadeConstructor
    implements KotlinMetadataVisitor
    {
        // Implementations for KotlinMetadataVisitor.
        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

        @Override
        public void visitKotlinMultiFileFacadeMetadata(Clazz clazz, KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
        {
            d1 = kotlinMultiFileFacadeKindMetadata.partClassNames.toArray(new String[0]);
            d2 = new String[0];
        }
    }


    /**
     * This utility class constructs the protobuf (d1 and d2 arrays) for Kotlin file facade (k == 2) metadata.
     */
    private class KotlinMultiFilePartConstructor
    extends    KotlinDeclarationContainerConstructor
    implements KotlinMetadataVisitor
    {
        private final KotlinClassMetadata.MultiFileClassPart.Writer multiPartKmdWriter;

        KotlinMultiFilePartConstructor()
        {
            this(new KotlinClassMetadata.MultiFileClassPart.Writer());
        }

        private KotlinMultiFilePartConstructor(KotlinClassMetadata.MultiFileClassPart.Writer multiPartKmdWriter)
        {
            super(multiPartKmdWriter);
            this.multiPartKmdWriter = multiPartKmdWriter;
        }


        // Implementations for KotlinMetadataVisitor
        @Override
        public void visitKotlinMultiFilePartMetadata(Clazz clazz, KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
        {
            kotlinMultiFilePartKindMetadata.propertiesAccept( clazz, this);
            kotlinMultiFilePartKindMetadata.functionsAccept(  clazz, this);
            kotlinMultiFilePartKindMetadata.typeAliasesAccept(clazz, this);

            JvmPackageExtensionVisitor ext =
                (JvmPackageExtensionVisitor) multiPartKmdWriter.visitExtensions(JvmPackageExtensionVisitor.TYPE);

            extensionVisitor = ext;
            kotlinMultiFilePartKindMetadata.delegatedPropertiesAccept(clazz, this);

            ext.visitEnd();

            multiPartKmdWriter.visitEnd();

            // Finally store the protobuf contents in the fields of the enclosing class.
            KotlinClassHeader header = multiPartKmdWriter.write(kotlinMultiFilePartKindMetadata.xs).getHeader();
            d1 = header.getData1();
            d2 = header.getData2();
        }
    }


    private class VersionRequirementConstructor
    implements KotlinVersionRequirementVisitor
    {
        private KmVersionRequirementVisitor versionReqVis;

        private KmConstructorVisitor constructorVis;
        VersionRequirementConstructor(KmConstructorVisitor constructorVis) { this.constructorVis = constructorVis; }

        private KmClassVisitor classVis;
        VersionRequirementConstructor(KmClassVisitor classVis) { this.classVis = classVis; }

        private KmPropertyVisitor propertyVis;
        VersionRequirementConstructor(KmPropertyVisitor propertyVis) { this.propertyVis = propertyVis; }

        private KmFunctionVisitor functionVis;
        VersionRequirementConstructor(KmFunctionVisitor functionVis) { this.functionVis = functionVis; }

        private KmTypeAliasVisitor aliasVis;
        VersionRequirementConstructor(KmTypeAliasVisitor aliasVis) { this.aliasVis = aliasVis; }


        // Implementations for KotlinVersionRequirementVisitor.

        @Override
        public void visitClassVersionRequirement(Clazz                            clazz,
                                                 KotlinMetadata                   kotlinMetadata,
                                                 KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
        {
            versionReqVis = classVis.visitVersionRequirement();
        }

        @Override
        public void visitConstructorVersionRequirement(Clazz                            clazz,
                                                       KotlinMetadata                   kotlinMetadata,
                                                       KotlinConstructorMetadata        kotlinConstructorMetadata,
                                                       KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
        {
            versionReqVis = constructorVis.visitVersionRequirement();

            visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
        }

        @Override
        public void visitPropertyVersionRequirement(Clazz                              clazz,
                                                    KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                    KotlinPropertyMetadata             kotlinPropertyMetadata,
                                                    KotlinVersionRequirementMetadata   kotlinVersionRequirementMetadata)
        {
            versionReqVis = propertyVis.visitVersionRequirement();

            visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
        }

        @Override
        public void visitFunctionVersionRequirement(Clazz                            clazz,
                                                    KotlinMetadata                   kotlinMetadata,
                                                    KotlinFunctionMetadata           kotlinFunctionMetadata,
                                                    KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
        {
            versionReqVis = functionVis.visitVersionRequirement();

            visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
        }

        public void visitTypeAliasVersionRequirement(Clazz clazz,
                                                     KotlinMetadata                   kotlinMetadata,
                                                     KotlinTypeAliasMetadata          kotlinTypeAliasMetadata,
                                                     KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
        {
            versionReqVis = aliasVis.visitVersionRequirement();

            visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
        }


        // Small helper methods.
        @Override
        public void visitAnyVersionRequirement(Clazz                            clazz,
                                               KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
        {
            versionReqVis.visit(kotlinVersionRequirementMetadata.kind,
                                kotlinVersionRequirementMetadata.level,
                                kotlinVersionRequirementMetadata.errorCode,
                                kotlinVersionRequirementMetadata.message);

            versionReqVis.visitVersion(kotlinVersionRequirementMetadata.major,
                                       kotlinVersionRequirementMetadata.minor,
                                       kotlinVersionRequirementMetadata.patch);

            versionReqVis.visitEnd();
        }
    }
}
