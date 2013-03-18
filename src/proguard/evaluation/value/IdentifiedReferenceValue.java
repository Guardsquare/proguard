/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.evaluation.value;

import proguard.classfile.Clazz;

/**
 * This LongValue represents a reference value that is identified by a unique ID.
 *
 * @author Eric Lafortune
 */
final class IdentifiedReferenceValue extends ReferenceValue
{
    private final ValueFactory valuefactory;
    private final int          id;


    /**
     * Creates a new long value with the given ID.
     */
    public IdentifiedReferenceValue(String       type,
                                    Clazz        referencedClass,
                                    boolean      mayBeNull,
                                    ValueFactory valuefactory,
                                    int          id)
    {
        super(type, referencedClass, mayBeNull);

        this.valuefactory = valuefactory;
        this.id           = id;
    }


    // Implementations for ReferenceValue.

    public int equal(ReferenceValue other)
    {
        return this.equals(other) ? ALWAYS : MAYBE;
    }


    // Implementations of binary methods of ReferenceValue.

    public ReferenceValue generalize(ReferenceValue other)
    {
        // Remove the ID if both values don't share the same ID.
        return this.equals(other) ?
            this :
            new ReferenceValue(type, referencedClass, mayBeNull).generalize(other);
    }


    // Implementations for Value.

    public boolean isSpecific()
    {
        return true;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.valuefactory.equals(((IdentifiedReferenceValue)object).valuefactory) &&
               this.id == ((IdentifiedReferenceValue)object).id;
    }


    public int hashCode()
    {
        return super.hashCode() ^
               valuefactory.hashCode() ^
               id;
    }


    public String toString()
    {
        return super.toString()+'#'+id;
    }
}