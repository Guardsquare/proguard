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

import proguard.classfile.*;
import proguard.classfile.kotlin.flags.KotlinClassFlags;
import proguard.classfile.kotlin.visitors.*;

import java.util.*;

import static proguard.classfile.kotlin.KotlinConstants.METADATA_KIND_CLASS;

public class KotlinClassKindMetadata
extends KotlinDeclarationContainerMetadata
{

    public String className;
    public Clazz  referencedClass;

    public List<KotlinTypeMetadata> superTypes;

    public String companionObjectName;
    public Clazz  referencedCompanionClass;
    public Field referencedCompanionField;

    public List<KotlinConstructorMetadata> constructors;

    public List<String> enumEntryNames;
    public List<Field>  referencedEnumEntries;
    public List<String> nestedClassNames;
    public List<Clazz>  referencedNestedClasses;
    public List<String> sealedSubclassNames;
    public List<Clazz>  referencedSealedSubClasses;

    public Clazz referencedDefaultImplsClass;

    public List<KotlinTypeParameterMetadata> typeParameters;

    public KotlinVersionRequirementMetadata versionRequirement;

    public KotlinClassFlags flags;


    // Extensions.

    // The JVM internal name of the original class this anonymous object is copied from. Refers to the
    // anonymous objects copied from bodies of inline functions to the use site by the Kotlin compiler.
    public String anonymousObjectOriginName;
    public Clazz  anonymousObjectOriginClass;


    public KotlinClassKindMetadata(int[]  mv,
                                   int[]  bv,
                                   int    xi,
                                   String xs,
                                   String pn)
    {
        super(METADATA_KIND_CLASS, mv, bv, xi, xs, pn);
    }


    @Override
    public void accept(Clazz clazz, KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        kotlinMetadataVisitor.visitKotlinClassMetadata(clazz, this);
    }


    public void companionAccept(KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        if (referencedCompanionClass != null)
        {
            referencedCompanionClass.kotlinMetadataAccept(kotlinMetadataVisitor);
        }
    }


    public void superTypesAccept(Clazz clazz, KotlinTypeVisitor kotlinTypeVisitor)
    {
        for (KotlinTypeMetadata superType : superTypes)
        {
            superType.accept(clazz, this, kotlinTypeVisitor);
        }
    }


    public void constructorsAccept(Clazz clazz, KotlinConstructorVisitor kotlinConstructorVisitor)
    {
        for (KotlinConstructorMetadata constructor : constructors)
        {
            constructor.accept(clazz, this, kotlinConstructorVisitor);
        }
    }


    public void typeParametersAccept(Clazz clazz, KotlinTypeParameterVisitor kotlinTypeParameterVisitor)
    {
        for (KotlinTypeParameterMetadata typeParameter : typeParameters)
        {
            typeParameter.accept(clazz, this, kotlinTypeParameterVisitor);
        }
    }


    public void versionRequirementAccept(Clazz                                   clazz,
                                         KotlinVersionRequirementVisitor kotlinVersionRequirementVisitor)
    {
        if (versionRequirement != null)
        {
            versionRequirement.accept(clazz,
                                      this,
                                      kotlinVersionRequirementVisitor);
        }
    }

    public void setMetadataFlags(int flags)
    {
        this.flags = new KotlinClassFlags(flags);
    }

    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin " +
               (companionObjectName != null ? "accompanied " : "") +
               (flags.isUsualClass      ? "usual "            : "") +
               (flags.isInterface       ? "interface "        : "") +
               (flags.isObject          ? "object "           : "") +
               (flags.isData            ? "data "             : "") +
               (flags.isData            ? "data "             : "") +
               (flags.isCompanionObject ? "companion object " : "") +
               (flags.isEnumEntry       ? "enum entry "       : "") +
               "class(" + className + ")";
    }
}
