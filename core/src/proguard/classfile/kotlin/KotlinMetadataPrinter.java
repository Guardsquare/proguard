/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.kotlin;

import kotlinx.metadata.KmVariance;
import proguard.classfile.*;
import proguard.classfile.kotlin.flags.*;
import proguard.classfile.kotlin.visitors.*;

import java.io.PrintWriter;
import java.util.List;

/**
 * @see KotlinMetadata .main
 */
public class KotlinMetadataPrinter
implements KotlinMetadataVisitor,
           KotlinConstructorVisitor,
           KotlinTypeParameterVisitor,
           KotlinTypeVisitor,
           KotlinValueParameterVisitor,
           KotlinVersionRequirementVisitor,
           KotlinFunctionVisitor,
           KotlinContractVisitor,
           KotlinEffectVisitor,
           KotlinTypeAliasVisitor,
           KotlinPropertyVisitor,
           KotlinEffectExprVisitor,
           KotlinAnnotationVisitor
{

    private static final String INDENTATION = "  ";

    private final PrintWriter pw;

    private int indentation;

    public KotlinMetadataPrinter()
    {
        this(new PrintWriter(System.out, true));
    }

    public KotlinMetadataPrinter(PrintWriter pw)
    {
        this.pw = pw;
    }

    // Implementations for KotlinMetadataVisitor

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz, KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
        kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
        kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
        kotlinDeclarationContainerMetadata.typeAliasesAccept(        clazz, this);
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        println("_____________________________________________________________________");
        print(
            hasRefIndicator(kotlinClassKindMetadata.referencedClass) +
            "Kotlin " + classFlags(kotlinClassKindMetadata.flags) +
            "class(" + hasAnnotationsFlag(kotlinClassKindMetadata.flags.common) +
            kotlinClassKindMetadata.className + ")"
        );

        if (kotlinClassKindMetadata.companionObjectName != null)
        {
            pw.print(" accompanied");
            if (!kotlinClassKindMetadata.companionObjectName.equals("Companion"))
            {
                pw.print(" by " +
                         hasRefIndicator(kotlinClassKindMetadata.referencedCompanionClass) +
                         kotlinClassKindMetadata.companionObjectName);
            }
        }

        if (kotlinClassKindMetadata.anonymousObjectOriginName != null)
        {
            pw.print(" from anonymous object class (" + kotlinClassKindMetadata.anonymousObjectOriginName + ")");
        }

        pw.println();
        indent();

        kotlinClassKindMetadata.typeParametersAccept(clazz, this);
        kotlinClassKindMetadata.superTypesAccept(    clazz, this);

        printArray("Nested classnames",     kotlinClassKindMetadata.referencedNestedClasses,    kotlinClassKindMetadata.nestedClassNames);
        printArray("Enum entry names",      kotlinClassKindMetadata.referencedEnumEntries,      kotlinClassKindMetadata.enumEntryNames);
        printArray("Sealed subclass names", kotlinClassKindMetadata.referencedSealedSubClasses, kotlinClassKindMetadata.sealedSubclassNames);

        kotlinClassKindMetadata.constructorsAccept(clazz, this);

        visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);

        outdent();
    }

    @Override
    public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
    {
        println("_____________________________________________________________________");
        println("Kotlin file facade: from Java class(" + clazz.getName() + ")"
        );

        indent();
        visitKotlinDeclarationContainerMetadata(clazz, kotlinFileFacadeKindMetadata);
        outdent();
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        println("_____________________________________________________________________");
        println("Kotlin synthetic " + kotlinSyntheticClassKindMetadata.kind.toString().toLowerCase() + " class(" +
                hasRefIndicator(kotlinSyntheticClassKindMetadata.referencedClass) +
                kotlinSyntheticClassKindMetadata.className + ") ");

        indent();
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
        outdent();
    }

    @Override
    public void visitKotlinMultiFileFacadeMetadata(Clazz                             clazz,
                                                   KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
    {
        println("_____________________________________________________________________");
        println(
            hasRefIndicator(kotlinMultiFileFacadeKindMetadata.referencedPartClasses) +
            "Kotlin multi file facade (" + clazz.getName() + ")"
        );
        indent();
        kotlinMultiFileFacadeKindMetadata.partClassNames.forEach(this::println);
        outdent();
    }

    @Override
    public void visitKotlinMultiFilePartMetadata(Clazz                           clazz,
                                                 KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
    {
        println("_____________________________________________________________________");
        println(
            hasRefIndicator(kotlinMultiFilePartKindMetadata.referencedFacade) +
            "Kotlin multi file part metadata: " +
            kotlinMultiFilePartKindMetadata.xs +
            " from " +
            "Java class(" + clazz.getName() + ")"
        );

        indent();
        visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);
        outdent();
    }

    // Implementations for KotlinConstructorVisitor

    @Override
    public void visitConstructor(Clazz                     clazz,
                                 KotlinClassKindMetadata   kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        print("[CTOR] ");
        if (kotlinClassKindMetadata.flags.isAnnotationClass)
        {
            pw.println(
                constructorFlags(kotlinConstructorMetadata.flags) +
                hasAnnotationsFlag(kotlinConstructorMetadata.flags.common)
            );
        }
        else
        {
            pw.println(
                hasRefIndicator(kotlinConstructorMetadata.referencedMethod) +
                constructorFlags(kotlinConstructorMetadata.flags) +
                hasAnnotationsFlag(kotlinConstructorMetadata.flags.common) +
                kotlinConstructorMetadata.jvmSignature
            );
        }

        indent();
        kotlinConstructorMetadata.valueParametersAccept(   clazz, kotlinClassKindMetadata, this);
        kotlinConstructorMetadata.versionRequirementAccept(clazz, kotlinClassKindMetadata, this);
        outdent();
    }

    // Implementations for KotlinTypeParameterVisitor

    @Override
    public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        print("[TPRM] " + kotlinTypeParameterMetadata.id + ": ");

        if (!KmVariance.INVARIANT.equals(kotlinTypeParameterMetadata.variance))
        {
            pw.print(kotlinTypeParameterMetadata.variance + " ");
        }

        pw.println(
            typeParameterFlags(kotlinTypeParameterMetadata.flags) +
            hasAnnotationsFlag(kotlinTypeParameterMetadata.flags.common) +
            kotlinTypeParameterMetadata.name
        );

        indent();
        kotlinTypeParameterMetadata.annotationsAccept(clazz, this);
        kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);
        outdent();
    }


    // Implementations for KotlinValueParameterVisitor.

    @Override
    public void visitAnyValueParameter(Clazz clazz, KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        println(
            "[VALP] " +
            valueParameterFlags(kotlinValueParameterMetadata.flags) +
            hasAnnotationsFlag(kotlinValueParameterMetadata.flags.common) + "\"" +
            kotlinValueParameterMetadata.parameterName + "\" "
        );
    }

    @Override
    public void visitPropertyValParameter(Clazz                              clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata       kotlinValueParameterMetadata)
    {
        visitAnyValueParameter(clazz, kotlinValueParameterMetadata);

        indent();
        kotlinValueParameterMetadata.typeAccept(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, this);
        outdent();
    }

    @Override
    public void visitFunctionValParameter(Clazz                        clazz,
                                          KotlinMetadata               kotlinMetadata,
                                          KotlinFunctionMetadata       kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        visitAnyValueParameter(clazz, kotlinValueParameterMetadata);

        indent();
        kotlinValueParameterMetadata.typeAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
        outdent();
    }

    @Override
    public void visitConstructorValParameter(Clazz                        clazz,
                                             KotlinClassKindMetadata      kotlinClassKindMetadata,
                                             KotlinConstructorMetadata    kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        visitAnyValueParameter(clazz, kotlinValueParameterMetadata);

        indent();
        kotlinValueParameterMetadata.typeAccept(clazz, kotlinClassKindMetadata, kotlinConstructorMetadata,this);
        outdent();
    }

    // Implementations for KotlinPropertyVisitor

    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        //TODO print if the declaring classes for backing field / synth antns method differs from clazz.
        print(
            propertyFlags(kotlinPropertyMetadata.flags) +
            hasAnnotationsFlag(kotlinPropertyMetadata.flags.common) + "\"" +
            kotlinPropertyMetadata.name + "\" "
        );

        String getString = kotlinPropertyMetadata.flags.hasGetter ?
                           (propertyAccessorFlags(kotlinPropertyMetadata.getterFlags) +
                            hasAnnotationsFlag(kotlinPropertyMetadata.getterFlags.common) +
                            "get") : "";

        String setString = kotlinPropertyMetadata.flags.hasSetter ?
                           (propertyAccessorFlags(kotlinPropertyMetadata.setterFlags) +
                            hasAnnotationsFlag(kotlinPropertyMetadata.setterFlags.common) +
                            "set") : "";

        pw.println(
            "[" +
            getString +
            (kotlinPropertyMetadata.flags.hasGetter && kotlinPropertyMetadata.flags.hasSetter ? "/" : "") +
            setString +
            "] "
        );

        indent();

        if (kotlinPropertyMetadata.backingFieldSignature != null)
        {
            println(
                "Backing field: " +
                hasRefIndicator(kotlinPropertyMetadata.referencedBackingField) +
                kotlinPropertyMetadata.backingFieldSignature
            );
        }

        if (kotlinPropertyMetadata.getterSignature != null)
        {
            println(
                "Getter:        " +
                hasRefIndicator(kotlinPropertyMetadata.referencedGetterMethod) +
                kotlinPropertyMetadata.getterSignature
            );
        }

        if (kotlinPropertyMetadata.setterSignature != null)
        {
            println(
                "Setter:        " +
                hasRefIndicator(kotlinPropertyMetadata.referencedSetterMethod) +
                kotlinPropertyMetadata.setterSignature
            );
        }

        if (kotlinPropertyMetadata.syntheticMethodForAnnotations != null)
        {
            println(
                "Synthetic method for annotations: " +
                (clazz != kotlinPropertyMetadata.referencedSyntheticMethodClass ?
                    kotlinPropertyMetadata.referencedSyntheticMethodClass.getName() + "." : "") +
                hasRefIndicator(kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations) +
                kotlinPropertyMetadata.syntheticMethodForAnnotations
            );
        }

        kotlinPropertyMetadata.receiverTypeAccept(      clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.typeParametersAccept(    clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.typeAccept(              clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.setterParametersAccept(  clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.versionRequirementAccept(clazz, kotlinDeclarationContainerMetadata, this);

        outdent();
    }

    @Override
    public void visitProperty(Clazz                              clazz,
                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                              KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        print("[PROP] ");

        visitAnyProperty(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);
    }

    @Override
    public void visitDelegatedProperty(Clazz                              clazz,
                                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                       KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        print("[DPRP] ");

        visitAnyProperty(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);
    }

    // Implementations for KotlinTypeVisitor

    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[TYPE] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitAbbreviation(Clazz              clazz,
                                  KotlinTypeMetadata kotlinTypeMetadata,
                                  KotlinTypeMetadata abbreviation)
    {
        print("[ABBR] ");
        indent();
        printKotlinTypeMetadata(clazz, abbreviation);
        outdent();
    }

    @Override
    public void visitOuterClass(Clazz              clazz,
                                KotlinTypeMetadata innerClass,
                                KotlinTypeMetadata outerClass)
    {
        print("[OUTR] ");
        indent();
        printKotlinTypeMetadata(clazz, outerClass);
        outdent();
    }

    @Override
    public void visitTypeArgument(Clazz              clazz,
                                  KotlinTypeMetadata kotlinTypeMetadata,
                                  KotlinTypeMetadata typeArgument)
    {
        print("[TARG] ");
        indent();
        printKotlinTypeMetadata(clazz, typeArgument);
        outdent();
    }

    @Override
    public void visitStarProjection(Clazz              clazz,
                                    KotlinTypeMetadata typeWithStarArg)
    {
        println("[SARG] *");
    }

    @Override
    public void visitTypeUpperBound(Clazz              clazz,
                                    KotlinTypeMetadata boundedType,
                                    KotlinTypeMetadata upperBound)
    {
        print("[UPPB] ");
        indent();
        printKotlinTypeMetadata(clazz, upperBound);
        outdent();
    }

    @Override
    public void visitSuperType(Clazz clazz,
                               KotlinClassKindMetadata kotlinMetadata,
                               KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[SUPT] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitFunctionReturnType(Clazz clazz,
                                        KotlinMetadata kotlinMetadata,
                                        KotlinFunctionMetadata kotlinFunctionMetadata,
                                        KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[RTRN] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitConstructorValParamVarArgType(Clazz clazz,
                                                   KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                   KotlinConstructorMetadata kotlinConstructorMetadata,
                                                   KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                   KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[VTYP] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitPropertyValParamVarArgType(Clazz clazz,
                                                KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                KotlinPropertyMetadata kotlinPropertyMetadata,
                                                KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[VTYP] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitFunctionValParamVarArgType(Clazz clazz,
                                                KotlinMetadata kotlinMetadata,
                                                KotlinFunctionMetadata kotlinFunctionMetadata,
                                                KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[VTYP] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitAliasUnderlyingType(Clazz                              clazz,
                                         KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                         KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                         KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        print("[UNDR] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitAliasExpandedType(Clazz                              clazz,
                                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                       KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                       KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        print("[XPAN] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitPropertyReceiverType(Clazz clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata kotlinPropertyMetadata,
                                          KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[RECT] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitFunctionReceiverType(Clazz clazz,
                                          KotlinMetadata kotlinMetadata,
                                          KotlinFunctionMetadata kotlinFunctionMetadata,
                                          KotlinTypeMetadata kotlinTypeMetadata)
    {
        print("[RECT] ");
        indent();
        printKotlinTypeMetadata(clazz, kotlinTypeMetadata);
        outdent();
    }

    @Override
    public void visitParameterUpperBound(Clazz                       clazz,
                                         KotlinTypeParameterMetadata boundedTypeParameter,
                                         KotlinTypeMetadata          upperBound)
    {
        print("[UPPB] ");
        indent();
        printKotlinTypeMetadata(clazz, upperBound);
        outdent();
    }


    // Implementations for KotlinTypeAliasVisitor.

    @Override
    public void visitTypeAlias(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
    {
        println("[ALIA] " + hasAnnotationsFlag(kotlinTypeAliasMetadata.flags.common) +
                            hasRefIndicator(kotlinTypeAliasMetadata.referencedDeclarationContainer) +
                            kotlinTypeAliasMetadata.name + " ");
        indent();
        kotlinTypeAliasMetadata.annotationsAccept(       clazz, this);
        kotlinTypeAliasMetadata.typeParametersAccept(    clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.underlyingTypeAccept(    clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.expandedTypeAccept(      clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.versionRequirementAccept(clazz, kotlinDeclarationContainerMetadata, this);
        outdent();
    }


    // Implementations for VersionRequirementVisitor.

    @Override
    public void visitAnyVersionRequirement(Clazz clazz,
                                           KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
    {
        println(
            "[VRSN] " +
            kotlinVersionRequirementMetadata.major + "." +
            kotlinVersionRequirementMetadata.minor + "." +
            kotlinVersionRequirementMetadata.patch +
            ", level: " + kotlinVersionRequirementMetadata.level.toString() +
            ", kind: " + kotlinVersionRequirementMetadata.kind.toString()
        );

        indent();

        if (kotlinVersionRequirementMetadata.message != null)
        {
            println("Message: " + kotlinVersionRequirementMetadata.message);
        }

        if (kotlinVersionRequirementMetadata.errorCode != null)
        {
            println("Error code: " + kotlinVersionRequirementMetadata.errorCode);
        }

        outdent();
    }

    // Implementations for KotlinFunctionVisitor.

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        pw.print(
            hasRefIndicator(kotlinFunctionMetadata.referencedMethod) +
            functionFlags(kotlinFunctionMetadata.flags) +
            hasAnnotationsFlag(kotlinFunctionMetadata.flags.common) + "\"" +
            kotlinFunctionMetadata.name + "\" [" +
            (!clazz.equals(kotlinFunctionMetadata.referencedMethodClass)
                 ? kotlinFunctionMetadata.referencedMethodClass.getName() + "."
                 : "") +
            kotlinFunctionMetadata.jvmSignature + "] "
        );

        if (kotlinFunctionMetadata.referencedDefaultImplementationMethod != null)
        {
            pw.print("defaultImpl: [" + kotlinFunctionMetadata.referencedDefaultImplementationMethodClass.getName() +
                     "." + kotlinFunctionMetadata.referencedDefaultImplementationMethod.getName(
                         kotlinFunctionMetadata.referencedDefaultImplementationMethodClass) + "] ");
        }

        indent();

        println();

        kotlinFunctionMetadata.typeParametersAccept(    clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.receiverTypeAccept(      clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.valueParametersAccept(   clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.returnTypeAccept(        clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.contractsAccept(         clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.versionRequirementAccept(clazz, kotlinMetadata, this);

        if (kotlinFunctionMetadata.lambdaClassOriginName != null)
        {
            println(
                hasRefIndicator(kotlinFunctionMetadata.referencedLambdaClassOrigin) +
                "Lambda class original name: " + kotlinFunctionMetadata.lambdaClassOriginName
            );
        }

        outdent();
    }

    @Override
    public void visitFunction(Clazz clazz,
                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                              KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        print("[FUNC] ");
        visitAnyFunction(clazz, kotlinDeclarationContainerMetadata, kotlinFunctionMetadata);
    }

    @Override
    public void visitSyntheticFunction(Clazz clazz,
                                       KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                       KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        print("[SFUN] ");
        visitAnyFunction(clazz, kotlinSyntheticClassKindMetadata, kotlinFunctionMetadata);
    }


    // Implementations for KotlinAnnotationVisitor.

    @Override
    public void visitAnyAnnotation(Clazz                    clazz,
                                   KotlinMetadataAnnotation annotation)
    {
        println("[ANTN] " +
                hasRefIndicator(annotation.referencedAnnotationClass) +
                hasRefIndicator(annotation.referencedArgumentMethods.values().toArray()) +
                annotation.toString());
    }

    // Implementations for KotlinContractVisitor.

    @Override
    public void visitContract(Clazz clazz,
                              KotlinMetadata kotlinMetadata,
                              KotlinFunctionMetadata kotlinFunctionMetadata,
                              KotlinContractMetadata kotlinContractMetadata)
    {

        println("[CTRT] ");
        indent();
        kotlinContractMetadata.effectsAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
        outdent();
    }

    // Implementations for KotlinEffectVisitor.

    @Override
    public void visitEffect(Clazz                  clazz,
                            KotlinMetadata         kotlinMetadata,
                            KotlinFunctionMetadata kotlinFunctionMetadata,
                            KotlinContractMetadata kotlinContractMetadata,
                            KotlinEffectMetadata   kotlinEffectMetadata)
    {
        print("[EFFT] ");
        pw.print(kotlinEffectMetadata.effectType);
        if (kotlinEffectMetadata.invocationKind != null)
        {
            pw.print(" " + kotlinEffectMetadata.invocationKind);
        }
        pw.println();
        indent();
        println("Constructor argument: ");
        indent();
        kotlinEffectMetadata.constructorArgumentAccept(clazz, this);
        outdent();
        println("Conclusion of conditional effect: ");
        indent();
        kotlinEffectMetadata.conclusionOfConditionalEffectAccept(clazz, this);
        outdent();
        outdent();
    }

    // Implementations for KotlinEffectExprVisitor.

    @Override
    public void visitEffectExpression(Clazz                          clazz,
                                      KotlinEffectMetadata           kotlinEffectMetadata,
                                      KotlinEffectExpressionMetadata kotlinEffectExpressionMetadata)
    {
        if(kotlinEffectExpressionMetadata.hasRightHandSides()){
            println("(");
        }
        indent();
        print("");

        if (kotlinEffectExpressionMetadata.parameterIndex >= 0)
        {
            // 1-based, optional. 0 means receiver parameter.
            if (kotlinEffectExpressionMetadata.parameterIndex == 0)
            {
                pw.print("receiver param ");
            }
            else
            {
                pw.print("param " + (kotlinEffectExpressionMetadata.parameterIndex - 1) + " ");
            }
        }

        if (kotlinEffectExpressionMetadata.flags.isNegated)
        {
            pw.print("negated ");
        }

        if (kotlinEffectExpressionMetadata.flags.isNullCheckPredicate)
        {
            pw.print("nullCheckPredicate ");
        }

        if (kotlinEffectExpressionMetadata.hasConstantValue)
        {
            pw.print(" " + kotlinEffectExpressionMetadata.constantValue);
        }

        pw.println();
        kotlinEffectExpressionMetadata.typeOfIsAccept(clazz, this);

        kotlinEffectExpressionMetadata.orRightHandSides.forEach(it -> {
            println("OR");
            it.accept(clazz, kotlinEffectMetadata, this);
        });
        kotlinEffectExpressionMetadata.andRightHandSides.forEach(it -> {
            println("AND");
            it.accept(clazz, kotlinEffectMetadata, this);
        });
        outdent();
        if(kotlinEffectExpressionMetadata.hasRightHandSides()){
            println(")");
        }
    }

    // Small helper methods.

    private void printKotlinTypeMetadata(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
    {
        if (kotlinTypeMetadata.className != null)
        {
            pw.print(hasRefIndicator(kotlinTypeMetadata.referencedClass));
        }
        else if (kotlinTypeMetadata.aliasName != null)
        {
            pw.print(hasRefIndicator(kotlinTypeMetadata.referencedTypeAlias));
        }

        pw.print(typeFlags(kotlinTypeMetadata.flags));

        if (kotlinTypeMetadata.isRaw)
        {
            pw.print("raw ");
        }

        if ( kotlinTypeMetadata.variance != null &&
            !kotlinTypeMetadata.variance.equals(KmVariance.INVARIANT))
        {
            pw.print(kotlinTypeMetadata.variance + " ");
        }

        if (kotlinTypeMetadata.typeParamID >= 0)
        {
            pw.print("param " + kotlinTypeMetadata.typeParamID);
        }
        else
        {
            pw.print(hasAnnotationsFlag(kotlinTypeMetadata.flags.common));

            if (kotlinTypeMetadata.className != null)
            {
                pw.print(kotlinTypeMetadata.className);
            }
            //TODO or just 'else'
            else if (kotlinTypeMetadata.aliasName != null)
            {
                pw.print("used as: " + kotlinTypeMetadata.aliasName);
            }
        }

        if (kotlinTypeMetadata.flags.isNullable)
        {
            pw.print("?");
        }

        pw.print(" ");

        if (kotlinTypeMetadata.flexibilityID != null)
        {
            pw.print("(flexibilityID: " + kotlinTypeMetadata.flexibilityID + ") ");
        }

        pw.println();

        kotlinTypeMetadata.annotationsAccept(  clazz, this);
        kotlinTypeMetadata.abbreviationAccept( clazz, this);
        kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
        kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
        kotlinTypeMetadata.outerClassAccept(   clazz, this);
    }

    /*
    private class IndexedTypeArgumentVisitor extends SimplifiedVisitor implements KotlinTypeVisitor
    {
        private int typeIndex = 0;

        @Override
        public void visitTypeArgument(KotlinTypeMetadata kotlinTypeMetadata, KotlinTypeMetadata typeArgument)
        {
            println(typeIndex++ + ":");
            indent();
            printKotlinTypeMetadata(typeArgument);
            outdent();
        }
    }*/

    // Small utility methods.

    private void indent()
    {
        indentation++;
    }

    private void outdent()
    {
        indentation--;
    }

    private void println(String string)
    {
        print(string);
        println();
    }

    private void print(String string)
    {
        for (int index = 0; index < indentation; index++)
        {
            pw.print(INDENTATION);
        }

        pw.print(string);
    }

    private void println()
    {
        pw.println();
    }

    private void printArray(String prepend, List<?> objects, List<String> names)
    {
        if (objects.size() > 0)
        {
            println(hasRefIndicator(objects) + prepend + ": " + String.join(", ", names));
        }
    }

    // Helper methods for the indicator showing if the references were initialised

    private static final String HAS_REF_INDICATOR           = "";
    private static final String DOES_NOT_HAVE_REF_INDICATOR = "!";

    private String hasRefIndicator(Object arg)
    {
        return arg == null ? DOES_NOT_HAVE_REF_INDICATOR + " " : HAS_REF_INDICATOR;
    }

    private String hasRefIndicator(List<?> objects)
    {
        if (objects == null)
        {
            return DOES_NOT_HAVE_REF_INDICATOR + " ";
        }
        else if (objects.isEmpty())
        {
            return "";
        }

        int count = countNonNull(objects);

        if (objects.size() == count)
        {
            return HAS_REF_INDICATOR;
        }
        else
        {
            return DOES_NOT_HAVE_REF_INDICATOR + "(" + (objects.size() - count) + ") ";
        }
    }

    private int countNonNull(List<?> arr)
    {
        int count = 0;

        for (Object o : arr)
        {
            if (o != null)
            {
                count++;
            }
        }

        return count;
    }

    // Flag printing helpers

    private String modalityFlags(KotlinModalityFlags flags)
    {
        return
            (flags.isFinal         ? "final "          : "") +
            (flags.isOpen          ? "open "           : "") +
            (flags.isAbstract      ? "abstract "       : "") +
            (flags.isSealed        ? "sealed "         : "");
    }

    private String visibilityFlags(KotlinVisibilityFlags flags)
    {
        return
            (flags.isInternal      ? "internal "       : "") +
            (flags.isPrivate       ? "private "        : "") +
            (flags.isPublic        ? "public "         : "") +
            (flags.isProtected     ? "protected "      : "") +
            (flags.isPrivateToThis ? "privateToThis "  : "") +
            (flags.isLocal         ? "local "          : "");
    }

    private String classFlags(KotlinClassFlags flags)
    {
       return
           visibilityFlags(flags.visibility) + modalityFlags(flags.modality) +
           (flags.isAnnotationClass ? "annotation "       : "") +
           (flags.isUsualClass      ? "usual "            : "") +
           (flags.isInterface       ? "interface "        : "") +
           (flags.isObject          ? "object "           : "") +
           (flags.isData            ? "data "             : "") +
           (flags.isInline          ? "inline "           : "") +
           (flags.isInner           ? "inner "            : "") +
           (flags.isExpect          ? "expect "           : "") +
           (flags.isExternal        ? "external "         : "") +
           (flags.isCompanionObject ? "companion object " : "") +
           (flags.isEnumEntry       ? "enum entry "       : "") +
           (flags.isEnumClass       ? "enum "             : "");
    }

    private String constructorFlags(KotlinConstructorFlags flags)
    {
        return visibilityFlags(flags.visibility) + (flags.isPrimary ? "primary " : "secondary ");
    }

    private String effectExpressionFlags(KotlinEffectExpressionFlags flags)
    {
        return
            (flags.isNegated ? "negated " : "") +
            (flags.isNullCheckPredicate ? "nullCheckPredicate " : "");
    }

    private String functionFlags(KotlinFunctionFlags flags)
    {
        return
            visibilityFlags(flags.visibility) + modalityFlags(flags.modality) +
            (flags.isDeclaration  ? "fun "          : "undeclared fun ") +
            (flags.isFakeOverride ? "fakeOverride " : "") +
            (flags.isDelegation   ? "by "           : "") +
            (flags.isSynthesized  ? "synthetic "    : "") +
            (flags.isOperator     ? "operator "     : "") +
            (flags.isInfix        ? "infix "        : "") +
            (flags.isInline       ? "inline "       : "") +
            (flags.isTailrec      ? "tailrec "      : "") +
            (flags.isExternal     ? "external "     : "") +
            (flags.isSuspend      ? "suspend "      : "") +
            (flags.isExpect       ? "expect "       : "");
    }

    private String propertyAccessorFlags(KotlinPropertyAccessorFlags flags)
    {
        return
            visibilityFlags(flags.visibility) + modalityFlags(flags.modality) +
            (flags.isDefault  ? ""          : "nonDefault ") +
            (flags.isExternal ? "external " : "") +
            (flags.isInline   ? "inline "   : "");
    }

    private String propertyFlags(KotlinPropertyFlags flags)
    {
        return
            visibilityFlags(flags.visibility) + modalityFlags(flags.modality) +
            (flags.isDeclared     ? ""              : "undeclared ") +
            (flags.isFakeOverride ? "fakeOverride " : "") +
            (flags.isDelegation   ? "by "           : "") +
            (flags.isSynthesized  ? "synthetic "    : "") +
            (flags.isVar          ? "var "          : "") +
            (flags.isConst        ? "const "        : "") +
            (flags.isLateinit     ? "lateinit "     : "") +
            (flags.hasConstant    ? "hasConstant "  : "") +
            (flags.isExternal     ? "external "     : "") +
            (flags.isDelegated    ? "delegated "    : "") +
            (flags.isExpect       ? "expect "       : "") +
            //JVM specific flags
            (flags.isMovedFromInterfaceCompanion ? "movedFromInterfaceCompanion " : "");
    }

    private String typeFlags(KotlinTypeFlags flags)
    {
        return
            //(flags.isNullable ? "nullable " : "") + //printed as ? after name in printKotlinTypeMetadata
            (flags.isSuspend  ? "suspend " : "");
    }

    private String typeParameterFlags(KotlinTypeParameterFlags flags)
    {
        return flags.isReified ? "reified " : "";
    }

    private String valueParameterFlags(KotlinValueParameterFlags flags)
    {
        return
            (flags.isCrossInline   ? "crossinline " : "") +
            (flags.isNoInline      ? "noinline "    : "") +
            (flags.hasDefaultValue ? "hasDefault "  : "");
    }

    private String hasAnnotationsFlag(KotlinCommonFlags flags)
    {
        return flags.hasAnnotations ? "@" : "";
    }
}
