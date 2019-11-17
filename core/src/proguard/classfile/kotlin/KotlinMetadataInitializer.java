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

import kotlinx.metadata.*;
import kotlinx.metadata.jvm.*;
import kotlinx.metadata.jvm.KotlinClassMetadata;
import proguard.classfile.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.ClassVisitor;

import java.util.*;

import static proguard.classfile.kotlin.KotlinConstants.*;

/**
 * Initializes the kotlin metadata for each Kotlin class. After initialization, all
 * info from the annotation is represented in the Clazz's `kotlinMetadata` field. All
 * lists in kotlinMetadata are initialized, even if empty.
 */
public class KotlinMetadataInitializer
extends    SimplifiedVisitor
implements AnnotationVisitor,

           // Implementation interfaces.
           ElementValueVisitor,
           ConstantVisitor
{
    private int      k;
    private int[]    mv;
    private int[]    bv;
    private String[] d1;
    private String[] d2;
    private int      xi;
    private String   xs;
    private String   pn;

    // For Constant visiting
    private MetadataType currentType;


    // Implementations for AnnotationVisitor.
    @Override
    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        // Collect the metadata.
        this.k  = -1;
        this.mv = null; //new int[] { 1, 0, 0 };
        this.bv = null; //new int[] { 1, 0, 0 };
        this.d1 = null; //new String[0];
        this.d2 = null; //new String[0];
        this.xi = -1;
        this.xs = null;
        this.pn = null;

        annotation.elementValuesAccept(clazz, this);


        // Parse the collected metadata.
        KotlinClassMetadata md = KotlinClassMetadata.read(new KotlinClassHeader(k, mv, bv, d1, d2, xs, pn, xi));
        if (md == null)
        {
            throw new UnsupportedOperationException("Encountered corrupt @kotlin/Metadata for class " + clazz.getName() + ".");
        }

        try
        {
            switch (k)
            {
                case METADATA_KIND_CLASS:
                    KotlinClassKindMetadata kotlinClassKindMetadata = new KotlinClassKindMetadata(mv, bv, xi, xs, pn);

                    ((KotlinClassMetadata.Class)md).accept(new ClassReader(kotlinClassKindMetadata));

                    clazz.accept(new SimpleKotlinMetadataSetter(kotlinClassKindMetadata));
                    break;

                case METADATA_KIND_FILE_FACADE: // For package level functions/properties
                    KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata = new KotlinFileFacadeKindMetadata(mv,
                                                                                                                 bv,
                                                                                                                 xi,
                                                                                                                 xs,
                                                                                                                 pn);

                    ((KotlinClassMetadata.FileFacade)md).accept(new PackageReader(kotlinFileFacadeKindMetadata));

                    clazz.accept(new SimpleKotlinMetadataSetter(kotlinFileFacadeKindMetadata));
                    break;

                case METADATA_KIND_SYNTHETIC_CLASS:
                    KotlinSyntheticClassKindMetadata.Kind kind;

                    KotlinClassMetadata.SyntheticClass smd = ((KotlinClassMetadata.SyntheticClass)md);

                    if (smd.isLambda())
                    {
                        kind = KotlinSyntheticClassKindMetadata.Kind.LAMBDA;
                    }
                    else if (clazz.getName().endsWith(DEFAULT_IMPLEMENTATIONS_SUFFIX))
                    {
                        kind = KotlinSyntheticClassKindMetadata.Kind.DEFAULT_IMPLS;
                    }
                    else if (clazz.getName().endsWith(WHEN_MAPPINGS_SUFFIX))
                    {
                        kind = KotlinSyntheticClassKindMetadata.Kind.WHEN_MAPPINGS;
                    }
                    else
                    {
                        kind = KotlinSyntheticClassKindMetadata.Kind.UNKNOWN;
                    }

                    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
                        new KotlinSyntheticClassKindMetadata(mv, bv, xi, xs, pn, kind);

                    if (smd.isLambda())
                    {
                        smd.accept(new LambdaReader(kotlinSyntheticClassKindMetadata));
                    }
                    else
                    {
                        kotlinSyntheticClassKindMetadata.functions = trimmed(new ArrayList<>(0));
                    }

                    clazz.accept(new SimpleKotlinMetadataSetter(kotlinSyntheticClassKindMetadata));
                    break;

                case METADATA_KIND_MULTI_FILE_CLASS_FACADE:
                    // The relevant data for this kind is in d1. It is a list of Strings
                    // representing the part class names.
                    clazz.accept(new SimpleKotlinMetadataSetter(new KotlinMultiFileFacadeKindMetadata(mv,
                                                                                                      bv,
                                                                                                      d1,
                                                                                                      xi,
                                                                                                      xs,
                                                                                                      pn)));
                    break;

                case METADATA_KIND_MULTI_FILE_CLASS_PART:
                    KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata =
                        new KotlinMultiFilePartKindMetadata(mv, bv, xi, xs, pn);

                    ((KotlinClassMetadata.MultiFileClassPart)md).accept(new PackageReader(
                        kotlinMultiFilePartKindMetadata));

                    clazz.accept(new SimpleKotlinMetadataSetter(kotlinMultiFilePartKindMetadata));
                    break;

                default:
                    // This happens when the library is outdated and a newer type of Kotlin class is passed.
                    System.err.println("Unknown Kotlin class kind in class " +
                                       clazz.getName() +
                                       ". The metadata for this class will not be processed.");
                    break;
            }
        }
        catch (InconsistentKotlinMetadataException e)
        {
            System.err.println("Encountered corrupt Kotlin metadata in class " +
                               clazz.getName() +
                               ". The metadata for this class will not be processed (" + e.getMessage() + ")");

        }
    }


    // Implementations for ElementValueVisitor.
    @Override
    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
        this.currentType = MetadataType.valueOf(constantElementValue.getMethodName(clazz));
        clazz.constantPoolEntryAccept(constantElementValue.u2constantValueIndex, this);
    }

    @Override
    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        MetadataType arrayElementType = MetadataType.valueOf(arrayElementValue.getMethodName(clazz));
        switch (arrayElementType)
        {
            case mv: this.mv = new int   [arrayElementValue.u2elementValuesCount]; break;
            case bv: this.bv = new int   [arrayElementValue.u2elementValuesCount]; break;
            case d1: this.d1 = new String[arrayElementValue.u2elementValuesCount]; break;
            case d2: this.d2 = new String[arrayElementValue.u2elementValuesCount]; break;
        }

        arrayElementValue.elementValuesAccept(clazz,
                                              annotation,
                                              new ArrayElementValueCollector(arrayElementType));
    }


    // Implementations for ConstantVisitor
    @Override
    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        if (this.currentType == MetadataType.xs)
        {
            xs = utf8Constant.getString();
        }
        else if (this.currentType == MetadataType.pn)
        {
            pn = utf8Constant.getString();
        }
        else
        {
            throw new UnsupportedOperationException("Cannot store Utf8Constant in int");
        }
    }

    @Override
    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant)
    {
        if (this.currentType == MetadataType.k)
        {
            k = integerConstant.getValue();
        }
        else if (this.currentType == MetadataType.xi)
        {
            xi = integerConstant.getValue();
        }
        else
        {
            throw new UnsupportedOperationException("Cannot store Utf8Constant in int");
        }
    }


    private class ArrayElementValueCollector
    extends    SimplifiedVisitor
    implements ElementValueVisitor,

               // Implementation interfaces.
               ConstantVisitor
    {
        private final MetadataType arrayType;
        private       int          index;

        ArrayElementValueCollector(MetadataType array)
        {
            this.arrayType = array;
            this.index = 0;
        }


        // Implementations for ElementValueVisitor
        @Override
        public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
        {
            clazz.constantPoolEntryAccept(constantElementValue.u2constantValueIndex, this);
        }


        // Implementations for ConstantVisitor
        @Override
        public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
        {
            if (this.arrayType == MetadataType.d1)
            {
                d1[index++] = utf8Constant.getString();
            }
            else if (this.arrayType == MetadataType.d2)
            {
                d2[index++] = utf8Constant.getString();
            }
            else
            {
                throw new UnsupportedOperationException("Cannot store UTF8Constant in int[]");
            }
        }

        @Override
        public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant)
        {
            if (this.arrayType == MetadataType.mv)
            {
                mv[index++] = integerConstant.getValue();
            }
            else if (this.arrayType == MetadataType.bv)
            {
                bv[index++] = integerConstant.getValue();
            }
            else
            {
                throw new UnsupportedOperationException("Cannot store IntegerConstant in String[]");
            }
        }
    }


    enum MetadataType
    {
        k, mv, bv, d1, d2, xi, xs, pn
    }


    private class SimpleKotlinMetadataSetter
    implements ClassVisitor
    {
        private final KotlinMetadata kmd;
        SimpleKotlinMetadataSetter(KotlinMetadata kmd) { this.kmd = kmd; }

        @Override public void visitProgramClass(ProgramClass programClass) { programClass.kotlinMetadata = kmd; }
        @Override public void visitLibraryClass(LibraryClass libraryClass) { libraryClass.kotlinMetadata = kmd; }
    }


    private class ClassReader
    extends KmClassVisitor
    {
        private final KotlinClassKindMetadata kotlinClassKindMetadata;

        private final ArrayList<KotlinTypeMetadata>          superTypes;
        private final ArrayList<KotlinConstructorMetadata>   constructors;
        private final ArrayList<KotlinFunctionMetadata>      functions;
        private final ArrayList<KotlinPropertyMetadata>      properties;
        private final ArrayList<KotlinPropertyMetadata>      localDelegatedProperties;
        private final ArrayList<String>                      enumEntryNames;
        private final ArrayList<String>                      nestedClassNames;
        private final ArrayList<String>                      sealedSubClassNames;
        private final ArrayList<KotlinTypeAliasMetadata>     typeAliases;
        private final ArrayList<KotlinTypeParameterMetadata> typeParameters;

        ClassReader(KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            this.kotlinClassKindMetadata = kotlinClassKindMetadata;

            this.superTypes               = new ArrayList<>(1);
            this.constructors             = new ArrayList<>(4);
            this.enumEntryNames           = new ArrayList<>(4);
            this.nestedClassNames         = new ArrayList<>(1);
            this.sealedSubClassNames      = new ArrayList<>(2);
            this.typeParameters           = new ArrayList<>(2);

            this.properties               = new ArrayList<>(8);
            this.functions                = new ArrayList<>(8);
            this.typeAliases              = new ArrayList<>(2);
            this.localDelegatedProperties = new ArrayList<>(2);
        }


        /**
         * Must be called first.
         */
        @Override
        public void visit(int flags, String className)
        {
            if (className.startsWith("."))
            {
                // If the class has a "local class name", the passed String starts with a dot. This appears to be safe to ignore
                className = className.substring(1);
            }

            // Inner classes are marked with a dot after the enclosing class instead
            // of '$' (only here, not in the actual d2 array).
            // TODO Maybe need to replace it again when writing it out to the writer?
            className = className.replace('.', '$');

            kotlinClassKindMetadata.setMetadataFlags(flags);
            kotlinClassKindMetadata.className = className;
        }

        @Override
        public KmTypeVisitor visitSupertype(int flags)
        {
            KotlinTypeMetadata superType = new KotlinTypeMetadata(flags);
            superTypes.add(superType);

            return new TypeReader(superType);
        }

        @Override
        public void visitCompanionObject(String companionName)
        {
            kotlinClassKindMetadata.companionObjectName = companionName;
        }

        @Override
        public KmConstructorVisitor visitConstructor(int flags)
        {
            KotlinConstructorMetadata constructor = new KotlinConstructorMetadata(flags);
            constructors.add(constructor);

            return new ConstructorReader(!kotlinClassKindMetadata.flags.isAnnotationClass,
                                         constructor);
        }

        @Override
        public void visitEnumEntry(String enumName)
        {
            enumEntryNames.add(enumName);
        }

        @Override
        public void visitNestedClass(String nestedClassName)
        {
            //TODO there have been cases where the whole enclosing class + nested class is renamed to a single new class, but this String refers to ONLY the nested name.

            nestedClassNames.add(nestedClassName);
        }

        @Override
        public void visitSealedSubclass(String subClassName)
        {
            subClassName = subClassName.replace(".","$");
            sealedSubClassNames.add(subClassName);
        }

        /**
         * @param id the id of the type parameter, useful to be able to uniquely identify the type parameter in different contexts where
         *           the name isn't enough (e.g. `class A<T> { fun <T> foo(t: T) }`)
         * @param variance the declaration-site variance of the type parameter
         */
        @Override
        public KmTypeParameterVisitor visitTypeParameter(int flags, String parameterName, int id, KmVariance variance)
        {
            KotlinTypeParameterMetadata kotlinTypeParameterMetadata = new KotlinTypeParameterMetadata(flags, parameterName, id, variance);
            typeParameters.add(kotlinTypeParameterMetadata);

            return new TypeParameterReader(kotlinTypeParameterMetadata);
        }

        @Override
        public KmVersionRequirementVisitor visitVersionRequirement()
        {
            KotlinVersionRequirementMetadata versionReq = new KotlinVersionRequirementMetadata();
            kotlinClassKindMetadata.versionRequirement = versionReq;

            return new VersionRequirementReader(versionReq);
        }


        // Implementations for KmDeclarationContainerVisitor
        @Override
        public KmFunctionVisitor visitFunction(int flags, String name)
        {
            KotlinFunctionMetadata function = new KotlinFunctionMetadata(flags, name);
            functions.add(function);

            return new FunctionReader(function);
        }

        @Override
        public KmPropertyVisitor visitProperty(int flags, String name, int getterFlags, int setterFlags)
        {
            KotlinPropertyMetadata property = new KotlinPropertyMetadata(flags, name, getterFlags, setterFlags);
            properties.add(property);

            return new PropertyReader(property);
        }

        @Override
        public KmTypeAliasVisitor visitTypeAlias(int flags, String name)
        {
            // Currently only top-level typeAlias declarations are allowed, so this
            // visit method will never be called but you can disable the compiler
            // error and allow typeAlias declarations here with this annotation:
            // @Suppress("TOPLEVEL_TYPEALIASES_ONLY")
            KotlinTypeAliasMetadata typeAlias = new KotlinTypeAliasMetadata(flags, name);
            typeAliases.add(typeAlias);

            return new TypeAliasReader(typeAlias);
        }

        @Override
        public KmClassExtensionVisitor visitExtensions(KmExtensionType extensionType)
        {
            return new ClassExtensionReader();
        }

        @Override
        public void visitEnd()
        {
            kotlinClassKindMetadata.superTypes               = trimmed(this.superTypes);
            kotlinClassKindMetadata.constructors             = trimmed(this.constructors);
            kotlinClassKindMetadata.enumEntryNames           = trimmed(this.enumEntryNames);
            kotlinClassKindMetadata.nestedClassNames         = trimmed(this.nestedClassNames);
            kotlinClassKindMetadata.sealedSubclassNames      = trimmed(this.sealedSubClassNames);
            kotlinClassKindMetadata.typeParameters           = trimmed(this.typeParameters);

            kotlinClassKindMetadata.properties               = trimmed(this.properties);
            kotlinClassKindMetadata.functions                = trimmed(this.functions);
            kotlinClassKindMetadata.typeAliases              = trimmed(this.typeAliases);
            kotlinClassKindMetadata.localDelegatedProperties = trimmed(this.localDelegatedProperties);
        }


        private class ClassExtensionReader
        extends JvmClassExtensionVisitor
        {
            /**
             * Visits the JVM internal name of the original class this anonymous object is copied from. This method is called for
             * anonymous objects copied from bodies of inline functions to the use site by the Kotlin compiler.
             */
            @Override
            public void visitAnonymousObjectOriginName(String internalName)
            {
                kotlinClassKindMetadata.anonymousObjectOriginName = internalName;
            }

            @Override
            public KmPropertyVisitor visitLocalDelegatedProperty(int flags, String name, int getterFlags, int setterFlags)
            {
                KotlinPropertyMetadata delegatedProperty = new KotlinPropertyMetadata(flags, name, getterFlags, setterFlags);
                localDelegatedProperties.add(delegatedProperty);

                return new PropertyReader(delegatedProperty);
            }

            @Override
            public void visitEnd() {}
        }
    }


    private class LambdaReader
    extends KmLambdaVisitor
    {
        private final KotlinSyntheticClassKindMetadata  kotlinSyntheticClassKindMetadata;
        private final ArrayList<KotlinFunctionMetadata> functions;

        LambdaReader(KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
        {
            this.kotlinSyntheticClassKindMetadata = kotlinSyntheticClassKindMetadata;

            this.functions = new ArrayList<>(1);
        }


        /**
         * @param name the name of the function (usually `"<anonymous>"` or `"<no name provided>"` for lambdas emitted by the Kotlin compiler)
         */
        @Override
        public KmFunctionVisitor visitFunction(int flags, String name)
        {
            KotlinFunctionMetadata function = new KotlinFunctionMetadata(flags, name);
            functions.add(function);

            return new FunctionReader(function);
        }

        @Override
        public void visitEnd()
        {
            kotlinSyntheticClassKindMetadata.functions = trimmed(this.functions);
        }
    }


    private class ConstructorReader
    extends KmConstructorVisitor
    {
        private final boolean                   hasValidJvmSignature;
        private final KotlinConstructorMetadata kotlinConstructorMetadata;

        private final ArrayList<KotlinValueParameterMetadata> valueParameters;

        ConstructorReader(boolean                   hasValidJvmSignature,
                          KotlinConstructorMetadata kotlinConstructorMetadata)
        {
            this.hasValidJvmSignature          = hasValidJvmSignature;
            this.kotlinConstructorMetadata     = kotlinConstructorMetadata;

            this.valueParameters = new ArrayList<>(4);
        }

        @Override
        public KmValueParameterVisitor visitValueParameter(int flags, String name)
        {
            KotlinValueParameterMetadata valueParameter = new KotlinValueParameterMetadata(flags, valueParameters.size(), name);
            valueParameters.add(valueParameter);

            return new ValueParameterReader(valueParameter);
        }

        @Override
        public KmVersionRequirementVisitor visitVersionRequirement()
        {
            KotlinVersionRequirementMetadata versionReq = new KotlinVersionRequirementMetadata();
            kotlinConstructorMetadata.versionRequirement = versionReq;

            return new VersionRequirementReader(versionReq);
        }

        @Override
        public KmConstructorExtensionVisitor visitExtensions(KmExtensionType extensionType)
        {
            return new ConstructorExtensionReader();
        }

        @Override
        public void visitEnd()
        {
            kotlinConstructorMetadata.valueParameters = trimmed(this.valueParameters);
        }


        private class ConstructorExtensionReader
        extends JvmConstructorExtensionVisitor
        {
            /**
             * For annotation classes, the metadata will have a JVM signature for a constructor,
             * while this is impossible to correspond to a real constructor. We set the jvmSignature
             * to null in this case.
             *
             * @param jvmSignature may be null
             */
            @Override
            public void visit(JvmMethodSignature jvmSignature)
            {
                //TODO maybe "" instead of descr.getName() ? otherwise "<init>" will appear in the metadata, but it doesn't appear there by default.
                if (hasValidJvmSignature)
                {
                    kotlinConstructorMetadata.jvmSignature = jvmSignature;
                }
            }
        }
    }


    private class PropertyReader
    extends KmPropertyVisitor
    {
        private final KotlinPropertyMetadata kotlinPropertyMetadata;

        private final ArrayList<KotlinValueParameterMetadata> setterParameters;
        private final ArrayList<KotlinTypeParameterMetadata>  typeParameters;


        PropertyReader(KotlinPropertyMetadata kotlinPropertyMetadata)
        {
            this.kotlinPropertyMetadata = kotlinPropertyMetadata;

            this.setterParameters = new ArrayList<>(4);
            this.typeParameters   = new ArrayList<>(1);
        }

        /**
         * This method is called for extension properties.
         */
        @Override
        public KmTypeVisitor visitReceiverParameterType(int flags)
        {
            KotlinTypeMetadata receiverType = new KotlinTypeMetadata(flags);
            kotlinPropertyMetadata.receiverType = receiverType;

            return new TypeReader(receiverType);
        }

        /**
         * Visits the type of the property.
         */
        @Override
        public KmTypeVisitor visitReturnType(int flags)
        {
            KotlinTypeMetadata returnType = new KotlinTypeMetadata(flags);
            kotlinPropertyMetadata.type = returnType;

            return new TypeReader(returnType);
        }

        /**
         * Visits a value parameter of the setter of this property, if this is a `var` property.
         *
         * @param name the name of the value parameter (`"<set-?>"` for properties emitted by the Kotlin compiler)
         */
        @Override
        public KmValueParameterVisitor visitSetterParameter(int flags, String name)
        {
            KotlinValueParameterMetadata valueParameter = new KotlinValueParameterMetadata(flags, setterParameters.size(), name);
            setterParameters.add(valueParameter);

            return new ValueParameterReader(valueParameter);
        }

        @Override
        public KmTypeParameterVisitor visitTypeParameter(int flags, String name, int id, KmVariance variance)
        {
            KotlinTypeParameterMetadata kotlinTypeParameterMetadata = new KotlinTypeParameterMetadata(flags, name, id, variance);
            typeParameters.add(kotlinTypeParameterMetadata);

            return new TypeParameterReader(kotlinTypeParameterMetadata);
        }

        @Override
        public KmVersionRequirementVisitor visitVersionRequirement()
        {
            KotlinVersionRequirementMetadata versionReq = new KotlinVersionRequirementMetadata();
            kotlinPropertyMetadata.versionRequirement = versionReq;

            return new VersionRequirementReader(versionReq);
        }

        @Override
        public KmPropertyExtensionVisitor visitExtensions(KmExtensionType type)
        {
            return new PropertyExtensionReader();
        }

        @Override
        public void visitEnd()
        {
            kotlinPropertyMetadata.setterParameters = trimmed(this.setterParameters);
            kotlinPropertyMetadata.typeParameters   = trimmed(this.typeParameters);
        }


        private class PropertyExtensionReader
        extends JvmPropertyExtensionVisitor
        {
            /**
             * @param jvmFlags        JVM specific flags, in addition to standard property flags
             * @param fieldSignature  may be null.
             * @param getterSignature may be null. May have a parameter if it is an extension property.
             * @param setterSignature may be null.
             */
            @Override
            public void visit(int jvmFlags, JvmFieldSignature fieldSignature, JvmMethodSignature getterSignature, JvmMethodSignature setterSignature)
            {
                kotlinPropertyMetadata.backingFieldSignature = fieldSignature;

//                if (getterSignature          !=      null          &&
//                    getterSignature.getName().equals("getContext") &&
//                    getterSignature.getDesc().equals(")Lkotlin/coroutines/experimental/CoroutineContext"))
//                {
//                    //TODO just set to null
//                    System.err.print("Fixing getter signature [" + getterSignature + "] into [");
//
//                    String name = getterSignature.getName();
//                    getterSignature = new JvmMethodSignature(
//                        name,
//                        "()Lkotlin/coroutines/experimental/CoroutineContext;");
//
//                    System.err.println(getterSignature + "]");
//                }

                kotlinPropertyMetadata.getterSignature       = getterSignature;
                kotlinPropertyMetadata.setterSignature       = setterSignature;
                kotlinPropertyMetadata.flags.setJvmFlags(jvmFlags);
            }

            /**
             * @param descriptor may be null
             */
            @Override
            public void visitSyntheticMethodForAnnotations(JvmMethodSignature descriptor)
            {
                kotlinPropertyMetadata.syntheticMethodForAnnotations = descriptor;
            }

            @Override
            public void visitEnd() {}
        }
    }


    private class TypeAliasReader
    extends KmTypeAliasVisitor
    {
        private final KotlinTypeAliasMetadata kotlinTypeAliasMetadata;

        private final ArrayList<KotlinMetadataAnnotation>    annotations;
        private final ArrayList<KotlinTypeParameterMetadata> typeParameters;

        TypeAliasReader(KotlinTypeAliasMetadata kotlinTypeAliasMetadata)
        {
            this.kotlinTypeAliasMetadata = kotlinTypeAliasMetadata;

            this.annotations    = new ArrayList<>(1);
            this.typeParameters = new ArrayList<>(1);
        }

        @Override
        public void visitAnnotation(KmAnnotation annotation)
        {
            annotations.add(new KotlinMetadataAnnotation(annotation));
        }

        /**
         * @param id the id of the type parameter, useful to be able to uniquely identify the type parameter in different contexts where
         *           the name isn't enough (e.g. `class A<T> { fun <T> foo(t: T) }`)
         * @param variance the declaration-site variance of the type parameter
         */
        @Override
        public KmTypeParameterVisitor visitTypeParameter(int flags, String name, int id, KmVariance variance)
        {
            KotlinTypeParameterMetadata kotlinTypeParameterMetadata = new KotlinTypeParameterMetadata(flags, name, id, variance);
            typeParameters.add(kotlinTypeParameterMetadata);

            return new TypeParameterReader(kotlinTypeParameterMetadata);
        }

        /**
         * Visit the right-hand side of the type alias declaration.
         */
        @Override
        public KmTypeVisitor visitUnderlyingType(int flags)
        {
            KotlinTypeMetadata underlyingType = new KotlinTypeMetadata(flags);
            kotlinTypeAliasMetadata.underlyingType = underlyingType;

            return new TypeReader(underlyingType);
        }

        /**
         * Visit the full expansion of the underlying type.
         */
        @Override
        public KmTypeVisitor visitExpandedType(int flags)
        {
            KotlinTypeMetadata expandedType = new KotlinTypeMetadata(flags);
            kotlinTypeAliasMetadata.expandedType = expandedType;

            return new TypeReader(expandedType);
        }

        @Override
        public KmVersionRequirementVisitor visitVersionRequirement()
        {
            KotlinVersionRequirementMetadata versionReq = new KotlinVersionRequirementMetadata();
            kotlinTypeAliasMetadata.versionRequirement = versionReq;

            return new VersionRequirementReader(versionReq);
        }

        @Override
        public void visitEnd()
        {
            kotlinTypeAliasMetadata.annotations    = trimmed(this.annotations);
            kotlinTypeAliasMetadata.typeParameters = trimmed(this.typeParameters);
        }
    }


    private class PackageReader
    extends KmPackageVisitor
    {
        private final KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata;

        private final ArrayList<KotlinPropertyMetadata>  properties;
        private final ArrayList<KotlinFunctionMetadata>  functions;
        private final ArrayList<KotlinTypeAliasMetadata> typeAliases;
        private final ArrayList<KotlinPropertyMetadata>  localDelegatedProperties;

        PackageReader(KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
        {
            this.kotlinDeclarationContainerMetadata = kotlinDeclarationContainerMetadata;

            this.properties               = new ArrayList<>(8);
            this.functions                = new ArrayList<>(8);
            this.typeAliases              = new ArrayList<>(2);
            this.localDelegatedProperties = new ArrayList<>(2);
        }


        // Implementations for KmDeclarationContainerVisitor
        @Override
        public KmFunctionVisitor visitFunction(int flags, String name)
        {
            KotlinFunctionMetadata function = new KotlinFunctionMetadata(flags, name);
            functions.add(function);

            return new FunctionReader(function);
        }

        @Override
        public KmPropertyVisitor visitProperty(int flags, String name, int getterFlags, int setterFlags)
        {
            KotlinPropertyMetadata property = new KotlinPropertyMetadata(flags, name, getterFlags, setterFlags);
            properties.add(property);

            return new PropertyReader(property);
        }

        @Override
        public KmTypeAliasVisitor visitTypeAlias(int flags, String name)
        {
            KotlinTypeAliasMetadata typeAlias = new KotlinTypeAliasMetadata(flags, name);
            typeAliases.add(typeAlias);

            return new TypeAliasReader(typeAlias);
        }

        @Override
        public KmPackageExtensionVisitor visitExtensions(KmExtensionType type)
        {
            return new PackageExtensionReader();
        }

        @Override
        public void visitEnd()
        {
            kotlinDeclarationContainerMetadata.properties               = trimmed(this.properties);
            kotlinDeclarationContainerMetadata.functions                = trimmed(this.functions);
            kotlinDeclarationContainerMetadata.typeAliases              = trimmed(this.typeAliases);
            kotlinDeclarationContainerMetadata.localDelegatedProperties = trimmed(this.localDelegatedProperties);
        }


        private class PackageExtensionReader
        extends JvmPackageExtensionVisitor
        {
            @Override
            public KmPropertyVisitor visitLocalDelegatedProperty(int flags, String name, int getterFlags, int setterFlags)
            {
                KotlinPropertyMetadata delegatedProperty = new KotlinPropertyMetadata(flags, name, getterFlags, setterFlags);
                localDelegatedProperties.add(delegatedProperty);

                return new PropertyReader(delegatedProperty);
            }

            @Override
            public void visitEnd() {}
        }
    }


    private class FunctionReader
    extends KmFunctionVisitor
    {
        private final KotlinFunctionMetadata  kotlinFunctionMetadata;

        private final ArrayList<KotlinContractMetadata>       contracts;
        private final ArrayList<KotlinValueParameterMetadata> valueParameters;
        private final ArrayList<KotlinTypeParameterMetadata>  typeParameters;

        FunctionReader(KotlinFunctionMetadata kotlinFunctionMetadata)
        {
            this.kotlinFunctionMetadata = kotlinFunctionMetadata;

            this.contracts       = new ArrayList<>(1);
            this.valueParameters = new ArrayList<>(4);
            this.typeParameters  = new ArrayList<>(1);
        }

        @Override
        public KmContractVisitor visitContract()
        {
            KotlinContractMetadata contract = new KotlinContractMetadata();
            contracts.add(contract);

            return new ContractReader(contract);
        }

        @Override
        public KmTypeVisitor visitReceiverParameterType(int flags)
        {
            KotlinTypeMetadata receiverType = new KotlinTypeMetadata(flags);
            kotlinFunctionMetadata.receiverType = receiverType;

            return new TypeReader(receiverType);
        }

        @Override
        public KmTypeVisitor visitReturnType(int flags)
        {
            KotlinTypeMetadata returnType = new KotlinTypeMetadata(flags);
            kotlinFunctionMetadata.returnType = returnType;

            return new TypeReader(returnType);
        }

        @Override
        public KmTypeParameterVisitor visitTypeParameter(int flags, String name, int id, KmVariance variance)
        {
            KotlinTypeParameterMetadata kotlinTypeParameterMetadata = new KotlinTypeParameterMetadata(flags, name, id, variance);
            typeParameters.add(kotlinTypeParameterMetadata);

            return new TypeParameterReader(kotlinTypeParameterMetadata);
        }

        @Override
        public KmValueParameterVisitor visitValueParameter(int flags, String name)
        {
            KotlinValueParameterMetadata valueParameter = new KotlinValueParameterMetadata(flags, valueParameters.size(), name);
            valueParameters.add(valueParameter);

            return new ValueParameterReader(valueParameter);
        }

        @Override
        public KmVersionRequirementVisitor visitVersionRequirement()
        {
            KotlinVersionRequirementMetadata versionReq = new KotlinVersionRequirementMetadata();
            kotlinFunctionMetadata.versionRequirement = versionReq;

            return new VersionRequirementReader(versionReq);
        }

        @Override
        public KmFunctionExtensionVisitor visitExtensions(KmExtensionType extensionType)
        {
            return new FunctionExtensionReader();
        }

        @Override
        public void visitEnd()
        {
            kotlinFunctionMetadata.contracts       = trimmed(this.contracts);
            kotlinFunctionMetadata.valueParameters = trimmed(this.valueParameters);
            kotlinFunctionMetadata.typeParameters  = trimmed(this.typeParameters);
        }


        private class FunctionExtensionReader
        extends JvmFunctionExtensionVisitor
        {
            /**
             * @param signature may be null
             */
            @Override
            public void visit(JvmMethodSignature signature)
            {
                // For synthetic functions, the jvmSignature could be null and
                // the actual method is in the enclosing class.
                // We fix this in the KotlinInterClassReferenceInitializer.

//                if (signature != null)
//                {
//                    if (signature.getName().equals("iterator") &&
//                        signature.getDesc().equals("()L;"))
//                    {
//                        //TODO just set to null
//                        System.err.print("Fixing signature [" + signature + "] into [");
//
//                        String name = signature.getName();
//                        signature = new JvmMethodSignature(
//                            name,
//                            "()Ljava/util/Iterator;");
//
//                        System.err.println(signature + "]");
//                    }
//
//                    if ((signature.getName().equals("resumeWithException") || signature.getName().equals("invoke")) &&
//                        signature.getDesc().equals("(L;)V"))
//                    {
//                        //TODO just set to null
//                        System.err.print("Fixing signature [" + signature + "] into [");
//
//                        String name = signature.getName();
//                        signature = new JvmMethodSignature(
//                            name,
//                            "(Ljava/lang/Throwable;)V");
//
//                        System.err.println(signature + "]");
//                    }
//
//                    if (signature.getName().equals("nvok") &&
//                        signature.getDesc().equals("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
//                    {
//                        System.err.print("Fixing signature [" + signature + "] into [");
//
//                        signature = new JvmMethodSignature(
//                            "invoke",
//                            signature.getDesc()
//                        );
//
//                        System.err.println(signature + "]");
//                    }
//
//                    if (signature.getName().equals("invoke") &&
//                        signature.getDesc().equals("kotlin/Any"))
//                    {
//                        System.err.print("Fixing signature [" + signature + "] into [");
//
//                        signature = new JvmMethodSignature(
//                            "invoke",
//                            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
//                        );
//
//                        System.err.println(signature + "]");
//                    }
//
//                    if (signature.getName().equals("invoke") &&
//                        signature.getDesc().equals("kotlin/Array"))
//                    {
//                        System.err.print("Fixing signature [" + signature + "] into [");
//
//                        signature = new JvmMethodSignature(
//                            "invoke",
//                            "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
//                        );
//
//                        System.err.println(signature + "]");
//                    }
//
//                    if (signature.getName().equals("collect") &&
//                        signature.getDesc().contains("LL"))
//                    {
//                        System.err.print("Fixing signature [" + signature + "] into [");
//
//                        if (signature.getDesc().contains("LLkotlinx/coroutines/flow/FlowCollector;;"))
//                        {
//                            signature = new JvmMethodSignature(
//                                "collect",
//                                signature.getDesc().replace("LLkotlinx/coroutines/flow/FlowCollector;;",
//                                                            "Lkotlinx/coroutines/flow/FlowCollector;")
//                            );
//                        }
//                        if (signature.getDesc().contains("LLkotlin/coroutines/Continuation;;"))
//                        {
//                            signature = new JvmMethodSignature(
//                                "collect",
//                                signature.getDesc().replace("LLkotlin/coroutines/Continuation;;", "Lkotlin/coroutines/Continuation;")
//                            );
//                        }
//
//                        System.err.println(signature + "]");
//                    }
//
//                    if (signature.getName().equals("invoke") &&
//                        signature.getDesc().equals("Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object"))
//                    {
//                        System.err.print("Fixing signature [" + signature + "] into [");
//
//                        signature = new JvmMethodSignature(
//                            signature.getName(),
//                            "(" + signature.getDesc() + ";"
//                        );
//
//                        System.err.println(signature + "]");
//                    }
//                }

                kotlinFunctionMetadata.jvmSignature = signature;
            }


            /**
             * Visit the JVM internal name of the original class the lambda class for this function is copied from.
             * This information is present for lambdas copied from bodies of inline functions to the use site by the Kotlin compiler.
             */
            @Override
            public void visitLambdaClassOriginName(String internalName)
            {
                kotlinFunctionMetadata.lambdaClassOriginName = internalName;
            }

            @Override
            public void visitEnd() {}
        }
    }


    //TODO verify for Kotlin 1.3
    private class ContractReader
    extends KmContractVisitor
    {
        private final KotlinContractMetadata kotlinContractMetadata;

        private final ArrayList<KotlinEffectMetadata> effects;

        ContractReader(KotlinContractMetadata kotlinContractMetadata)
        {
            this.kotlinContractMetadata = kotlinContractMetadata;

            this.effects = new ArrayList<>(2);
        }

        /**
         * @param invocationKind number of invocations of the lambda parameter of this function, may be null
         */
        @Override
        public KmEffectVisitor visitEffect(KmEffectType effectType, KmEffectInvocationKind invocationKind)
        {
            KotlinEffectMetadata effect = new KotlinEffectMetadata(effectType, invocationKind);
            effects.add(effect);

            return new EffectReader(effect);
        }

        @Override
        public void visitEnd()
        {
            kotlinContractMetadata.effects = trimmed(this.effects);
        }
    }


    private class EffectReader
    extends KmEffectVisitor
    {
        private final KotlinEffectMetadata kotlinEffectMetadata;

        EffectReader(KotlinEffectMetadata kotlinEffectMetadata)
        {
            this.kotlinEffectMetadata = kotlinEffectMetadata;
        }

        /**
         * Visits the optional conclusion of the effect. If this method is called, the effect represents an implication with the
         * right-hand side handled by the returned visitor.
         */
        @Override
        public KmEffectExpressionVisitor visitConclusionOfConditionalEffect()
        {
            KotlinEffectExpressionMetadata conclusion = new KotlinEffectExpressionMetadata();
            kotlinEffectMetadata.conclusionOfConditionalEffect = conclusion;

            return new EffectExpressionReader(conclusion);
        }

        /**
         * Visits the optional argument of the effect constructor, i.e. the constant value for the [KmEffectType.RETURNS_CONSTANT] effect,
         * or the parameter reference for the [KmEffectType.CALLS] effect.
         */
        @Override
        public KmEffectExpressionVisitor visitConstructorArgument()
        {
            KotlinEffectExpressionMetadata constructorArg = new KotlinEffectExpressionMetadata();
            kotlinEffectMetadata.constructorArgument = constructorArg;

            return new EffectExpressionReader(constructorArg);
        }

        @Override
        public void visitEnd() {}
    }


    private class EffectExpressionReader
    extends KmEffectExpressionVisitor
    {
        private final KotlinEffectExpressionMetadata       kotlinEffectExpressionMetadata;
        private final List<KotlinEffectExpressionMetadata> andRightHandSides = new ArrayList<>();
        private final List<KotlinEffectExpressionMetadata> orRightHandSides  = new ArrayList<>();

        EffectExpressionReader(KotlinEffectExpressionMetadata kotlinEffectExpressionMetadata)
        {
            this.kotlinEffectExpressionMetadata = kotlinEffectExpressionMetadata;
        }

        /**
         * @param parameterIndex optional 1-based index of the value parameter of the function, for effects which assert something about
         *                       the function parameters. The index 0 means the extension receiver parameter. May be null
         */
        @Override
        public void visit(int flags, Integer parameterIndex)
        {
            kotlinEffectExpressionMetadata.setMetadataFlags(flags);

            if (parameterIndex != null)
            {
                kotlinEffectExpressionMetadata.parameterIndex = parameterIndex;
            }
        }

        /**
         * Visits the constant value used in the effect expression. May be `true`, `false` or `null`.
         * @param o may be null
         */
        @Override
        public void visitConstantValue(Object o)
        {
            kotlinEffectExpressionMetadata.hasConstantValue = true;
            kotlinEffectExpressionMetadata.constantValue    = o;
        }

        /**
         * Visits the type used as the target of an `is`-expression in the effect expression.
         */
        @Override
        public KmTypeVisitor visitIsInstanceType(int flags)
        {
            KotlinTypeMetadata typeOfIs = new KotlinTypeMetadata(flags);
            kotlinEffectExpressionMetadata.typeOfIs = typeOfIs;

            return new TypeReader(typeOfIs);
        }

        /**
         * Visits the argument of an `&&`-expression. If this method is called, the expression represents the left-hand side and
         * the returned visitor handles the right-hand side.
         */
        @Override
        public KmEffectExpressionVisitor visitAndArgument()
        {
            KotlinEffectExpressionMetadata andRHS = new KotlinEffectExpressionMetadata();
            this.andRightHandSides.add(andRHS);
            return new EffectExpressionReader(andRHS);
        }


        /**
         * Visits the argument of an `||`-expression. If this method is called, the expression represents the left-hand side and
         * the returned visitor handles the right-hand side.
         */
        @Override
        public KmEffectExpressionVisitor visitOrArgument()
        {
            KotlinEffectExpressionMetadata orRHS = new KotlinEffectExpressionMetadata();
            this.orRightHandSides.add(orRHS);
            return new EffectExpressionReader(orRHS);
        }

        @Override
        public void visitEnd() {
            kotlinEffectExpressionMetadata.andRightHandSides = andRightHandSides;
            kotlinEffectExpressionMetadata.orRightHandSides = orRightHandSides;
        }
    }


    /**
     * Note: visitType will always be called, and visitVararg on top of that if the val parameter is a valarg
     */
    private class ValueParameterReader
    extends KmValueParameterVisitor
    {
        private final KotlinValueParameterMetadata kotlinValueParameterMetadata;

        ValueParameterReader(KotlinValueParameterMetadata kotlinValueParameterMetadata)
        {
            this.kotlinValueParameterMetadata = kotlinValueParameterMetadata;
        }

        @Override
        public KmTypeVisitor visitType(int flags)
        {
            KotlinTypeMetadata type = new KotlinTypeMetadata(flags);
            kotlinValueParameterMetadata.type = type;

            return new TypeReader(type);
        }

        @Override
        public KmTypeVisitor visitVarargElementType(int flags)
        {
            KotlinTypeMetadata varArgType = new KotlinTypeMetadata(flags);
            kotlinValueParameterMetadata.varArgElementType = varArgType;

            return new TypeReader(varArgType);
        }

        @Override
        public void visitEnd() {}
    }


    /**
     * A visitor to visit a type. The type must have a classifier which is one of: a class [visitClass], type parameter [visitTypeParameter]
     * or type alias [visitTypeAlias]. If the type's classifier is a class or a type alias, it can have type arguments ([visitArgument] and
     * [visitStarProjection]). If the type's classifier is an inner class, it can have the outer type ([visitOuterType]), which captures
     * the generic type arguments of the outer class. Also, each type can have an abbreviation ([visitAbbreviatedType]) in case a type alias
     * was used originally at this site in the declaration (all types are expanded by default for metadata produced by the Kotlin compiler).
     * If [visitFlexibleTypeUpperBound] is called, this type is regarded as a flexible type, and its contents represent the lower bound,
     * and the result of the call represents the upper bound.
     *
     * When using this class, [visitEnd] must be called exactly once and after calls to all other visit* methods.
     */
    private class TypeReader
    extends KmTypeVisitor
    {
        private KotlinTypeMetadata kotlinTypeMetadata;

        private final ArrayList<KotlinTypeMetadata>       typeArguments;
        private final ArrayList<KotlinTypeMetadata>       upperBounds;
        private final ArrayList<KotlinMetadataAnnotation> annotations;

        TypeReader(KotlinTypeMetadata kotlinTypeMetadata)
        {
            this.kotlinTypeMetadata = kotlinTypeMetadata;

            this.typeArguments = new ArrayList<>(2);
            this.annotations   = new ArrayList<>(1);
            this.upperBounds   = new ArrayList<>();
        }

        /**
         * Visits the abbreviation of this type. Note that all types are expanded for metadata produced by the Kotlin compiler. For example:
         *
         *     typealias A<T> = MutableList<T>
         *
         *     fun foo(a: A<Any>) {}
         *
         * The type of the `foo`'s parameter in the metadata is actually `MutableList<Any>`, and its abbreviation is `A<Any>`.
         */
        @Override
        public KmTypeVisitor visitAbbreviatedType(int flags)
        {
            KotlinTypeMetadata abbreviatedType = new KotlinTypeMetadata(flags);
            kotlinTypeMetadata.abbreviation = abbreviatedType;

            return new TypeReader(abbreviatedType);
        }

        /**
         * Visits the name of the class, if this type's classifier is a class.
         */
        @Override
        public void visitClass(String className)
        {
            //PROBBUG
            if (className.startsWith("L") && className.endsWith(";")){
                className = className.substring(1, className.length() - 1);
            }
            kotlinTypeMetadata.className = className;
        }

        @Override
        public void visitTypeParameter(int id)
        {
            kotlinTypeMetadata.typeParamID = id;
        }

        /**
         * Visits the name of the type alias, if this type's classifier is a type alias. Note that all types are expanded for metadata produced
         * by the Kotlin compiler, so the the type with a type alias classifier may only appear in a call to [visitAbbreviatedType].
         */
        @Override
        public void visitTypeAlias(String aliasName)
        {
            kotlinTypeMetadata.aliasName = aliasName;
        }

        /**
         * Visits the outer type, if this type's classifier is an inner class. For example:
         *
         *     class A<T> { inner class B<U> }
         *
         *     fun foo(a: A<*>.B<Byte?>) {}
         *
         * The type of the `foo`'s parameter in the metadata is `B<Byte>` (a type whose classifier is class `B`, and it has one type argument,
         * type `Byte?`), and its outer type is `A<*>` (a type whose classifier is class `A`, and it has one type argument, star projection).
         */
        @Override
        public KmTypeVisitor visitOuterType(int flags)
        {
            KotlinTypeMetadata outerType = new KotlinTypeMetadata(flags);
            kotlinTypeMetadata.outerClassType = outerType;

            return new TypeReader(outerType);
        }

        /**
         * Visits the type projection used in a type argument of the type based on a class or on a type alias.
         * For example, in `MutableMap<in String?, *>`, `in String?` is the type projection which is the first type argument of the type.
         */
        @Override
        public KmTypeVisitor visitArgument(int flags, KmVariance variance)
        {
            KotlinTypeMetadata typeArgument = new KotlinTypeMetadata(flags, variance);
            typeArguments.add(typeArgument);

            return new TypeReader(typeArgument);
        }

        @Override
        public void visitStarProjection()
        {
            typeArguments.add(KotlinTypeMetadata.starProjection());
        }

        /**
         * Visits the upper bound of the type, marking it as flexible and its contents as the lower bound. Flexible types in Kotlin include
         * platform types in Kotlin/JVM and `dynamic` type in Kotlin/JS.
         *
         * @param typeFlexibilityId id of the kind of flexibility this type has. For example, "kotlin.jvm.PlatformType" for JVM platform types,
         *                          or "kotlin.DynamicType" for JS dynamic type, may be null
         */
        @Override
        public KmTypeVisitor visitFlexibleTypeUpperBound(int flags, String typeFlexibilityId)
        {
            kotlinTypeMetadata.flexibilityID = typeFlexibilityId;

            KotlinTypeMetadata upperBound = new KotlinTypeMetadata(flags);
            this.upperBounds.add(upperBound);

            return new TypeReader(upperBound);
        }

        @Override
        public KmTypeExtensionVisitor visitExtensions(KmExtensionType extensionType)
        {
            return new TypeExtensionReader();
        }

        @Override
        public void visitEnd()
        {
            kotlinTypeMetadata.typeArguments = trimmed(this.typeArguments);
            kotlinTypeMetadata.annotations   = trimmed(this.annotations);
            kotlinTypeMetadata.upperBounds   = trimmed(this.upperBounds);

            //PROBBUG if a value parameter or a type parameter has an annotation then
            //        the annotation will be stored there but the flag will be
            //        incorrectly set on this type. Sometimes the flag is not set
            //        when there are annotations, sometimes the flag is set but there are no annotations.
            kotlinTypeMetadata.flags.common.hasAnnotations = !annotations.isEmpty();
        }


        private class TypeExtensionReader
        extends JvmTypeExtensionVisitor
        {
            /**
             * @param isRaw whether the type is seen as a raw type in Java
             */
            @Override
            public void visit(boolean isRaw)
            {
                kotlinTypeMetadata.isRaw = isRaw;
            }

            @Override
            public void visitAnnotation(KmAnnotation annotation)
            {
                // e.g. @ParameterName("prefix") [map, throw away if shrunk], @UnsafeVariance [throw away?]
                annotations.add(new KotlinMetadataAnnotation(annotation));
            }

            @Override
            public void visitEnd() {}
        }
    }


    private class TypeParameterReader
    extends KmTypeParameterVisitor
    {
        private final KotlinTypeParameterMetadata    kotlinTypeParameterMetadata;
        private final ArrayList<KotlinTypeMetadata>  upperBounds;

        TypeParameterReader(KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
        {
            this.kotlinTypeParameterMetadata = kotlinTypeParameterMetadata;
            this.upperBounds = new ArrayList<>();
        }

        @Override
        public KmTypeParameterExtensionVisitor visitExtensions(KmExtensionType type)
        {
            return new TypeParameterExtensionReader();
        }

        @Override
        public KmTypeVisitor visitUpperBound(int flags)
        {
            KotlinTypeMetadata upperBound = new KotlinTypeMetadata(flags);
            this.upperBounds.add(upperBound);

            return new TypeReader(upperBound);
        }

        @Override
        public void visitEnd() {
            kotlinTypeParameterMetadata.upperBounds = this.upperBounds;
        }


        private class TypeParameterExtensionReader
        extends JvmTypeParameterExtensionVisitor
        {
            private final ArrayList<KotlinMetadataAnnotation> annotations = new ArrayList<>(1);

            @Override
            public void visitAnnotation(KmAnnotation annotation)
            {
                annotations.add(new KotlinMetadataAnnotation(annotation));
            }

            @Override
            public void visitEnd()
            {
                kotlinTypeParameterMetadata.annotations = trimmed(this.annotations);

                //PROBBUG if a value parameter or a type parameter has an annotation then
                //        the annotation will be stored there but the flag will be
                //        incorrectly set on this type. Sometimes the flag is not set
                //        when there are annotations, sometimes the flag is set but there are no annotations.
                kotlinTypeParameterMetadata.flags.common.hasAnnotations = !annotations.isEmpty();
            }
        }
    }


    private class VersionRequirementReader
    extends KmVersionRequirementVisitor
    {
        private final KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata;

        VersionRequirementReader(KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
        {
            this.kotlinVersionRequirementMetadata = kotlinVersionRequirementMetadata;
        }

        /**
         * @param errorCode may be null
         * @param message   may be null
         */
        @Override
        public void visit(KmVersionRequirementVersionKind kind, KmVersionRequirementLevel level, Integer errorCode, String message)
        {
            kotlinVersionRequirementMetadata.kind      = kind;
            kotlinVersionRequirementMetadata.level     = level;
            kotlinVersionRequirementMetadata.errorCode = errorCode;
            kotlinVersionRequirementMetadata.message   = message;
        }

        @Override
        public void visitVersion(int major, int minor, int patch)
        {
            kotlinVersionRequirementMetadata.major = major;
            kotlinVersionRequirementMetadata.minor = minor;
            kotlinVersionRequirementMetadata.patch = patch;
        }

        @Override
        public void visitEnd() {}
    }


    // Small helper methods.

    private static <K> List<K> trimmed(ArrayList<K> list)
    {
        list.trimToSize();
        return list;
    }
}
