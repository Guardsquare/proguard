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
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.flags.KotlinTypeFlags;
import proguard.classfile.kotlin.visitors.*;
import proguard.util.*;

import java.util.*;

public class KotlinTypeMetadata
extends SimpleProcessable
implements Processable
{
    public KmVariance variance;

    public List<KotlinTypeMetadata> typeArguments;

    // The alias by which this type was referenced.
    public KotlinTypeMetadata abbreviation;

    // Classifier. One of these is set.

    public String className;
    public Clazz  referencedClass;

    public int                         typeParamID = -1;
    public KotlinTypeParameterMetadata referencedParameter; //TODO probably not necessary.

    public String                  aliasName;
    public KotlinTypeAliasMetadata referencedTypeAlias;

    // In case of class or type alias, these are possibly set.

    public KotlinTypeMetadata outerClassType;

    // If this is a flexible type, this type is the lower bound.

    public String flexibilityID;

    public List<KotlinTypeMetadata> upperBounds;


    // Extensions.

    public boolean isRaw = false;

    public List<KotlinMetadataAnnotation> annotations;

    public KotlinTypeFlags flags;


    public KotlinTypeMetadata(int flags)
    {
        this(flags, null);
    }


    public KotlinTypeMetadata(int flags, KmVariance variance)
    {
        this.variance = variance;
        this.flags    = new KotlinTypeFlags(flags);
    }


    public void accept(Clazz                   clazz,
                       KotlinClassKindMetadata kotlinClassKindMetadata,
                       KotlinTypeVisitor       kotlinTypeVisitor)
    {
        kotlinTypeVisitor.visitSuperType(clazz, kotlinClassKindMetadata, this);
    }


    public void upperBoundsAccept(Clazz             clazz,
                                  KotlinTypeVisitor kotlinTypeVisitor)
    {
        for (KotlinTypeMetadata upperBound : upperBounds)
        {
            upperBound.acceptAsUpperBound(clazz, this, kotlinTypeVisitor);
        }
    }

    public void typeArgumentsAccept(Clazz clazz, KotlinTypeVisitor kotlinTypeVisitor)
    {
        for (KotlinTypeMetadata typeArgument : typeArguments)
        {
            if (typeArgument.isStarProjection())
            {
                kotlinTypeVisitor.visitStarProjection(clazz, this);
            }
            else
            {
                kotlinTypeVisitor.visitTypeArgument(clazz, this, typeArgument);
            }
        }
    }

    public void outerClassAccept(Clazz clazz, KotlinTypeVisitor kotlinTypeVisitor)
    {
        if (outerClassType != null)
        {
            kotlinTypeVisitor.visitOuterClass(clazz, this, outerClassType);
        }
    }

    private void acceptAsUpperBound(Clazz              clazz,
                                    KotlinTypeMetadata boundedType,
                                    KotlinTypeVisitor  kotlinTypeVisitor)
    {
        kotlinTypeVisitor.visitTypeUpperBound(clazz, boundedType, this);
    }


    public void abbreviationAccept(Clazz clazz, KotlinTypeVisitor kotlinTypeVisitor)
    {
        if (abbreviation != null)
        {
            abbreviation.acceptAsAbbreviation(clazz, this, kotlinTypeVisitor);
        }
    }


    private void acceptAsAbbreviation(Clazz              clazz,
                                      KotlinTypeMetadata abbreviatedType,
                                      KotlinTypeVisitor  kotlinTypeVisitor)
    {
        kotlinTypeVisitor.visitAbbreviation(clazz, abbreviatedType, this);
    }


    public void accept(Clazz                       clazz,
                       KotlinTypeParameterMetadata boundedTypeParameter,
                       KotlinTypeVisitor           kotlinTypeVisitor)
    {
        kotlinTypeVisitor.visitParameterUpperBound(clazz, boundedTypeParameter, this);
    }

    public void annotationsAccept(Clazz                   clazz,
                                  KotlinAnnotationVisitor kotlinAnnotationVisitor)
    {
        for (KotlinMetadataAnnotation annotation : annotations)
        {
            kotlinAnnotationVisitor.visitTypeAnnotation(clazz, this, annotation);
        }
    }

    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin type";
    }


    // Small helper methods.

    static KotlinTypeMetadata starProjection()
    {
        return STAR_PROJECTION;
    }


    protected boolean isStarProjection()
    {
        return false;
    }


    // Small helper classes.

    private static final KotlinTypeMetadata STAR_PROJECTION = new KotlinStarProjectionMetadata();

    /**
     * This KotlinTypeMetadata represents a star projection (a generics wildcard). It can be
     * stored in the typeArguments array by calling KotlinTypeMetadata.starProjection().
     *
     */
    private static class KotlinStarProjectionMetadata
    extends KotlinTypeMetadata
    {
        KotlinStarProjectionMetadata() { super(0); }

        @Override
        protected boolean isStarProjection()
        {
            return true;
        }
    }
}
