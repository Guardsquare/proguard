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

import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;

/**
 * This class provides methods to create and reuse IntegerValue objects.
 *
 * @author Eric Lafortune
 */
public class IdentifiedValueFactory
extends      SpecificValueFactory
{
    private int integerID;
    private int longID;
    private int floatID;
    private int doubleID;
    private int referenceID;


    // Implementations for ValueFactory.

    public IntegerValue createIntegerValue()
    {
        return new IdentifiedIntegerValue(this, integerID++);
    }


    public LongValue createLongValue()
    {
        return new IdentifiedLongValue(this, longID++);
    }


    public FloatValue createFloatValue()
    {
        return new IdentifiedFloatValue(this, floatID++);
    }


    public DoubleValue createDoubleValue()
    {
        return new IdentifiedDoubleValue(this, doubleID++);
    }


    public ReferenceValue createReferenceValue(String  type,
                                               Clazz   referencedClass,
                                               boolean mayBeNull)
    {
        return type == null ?
            REFERENCE_VALUE_NULL :
            new IdentifiedReferenceValue(type, referencedClass, mayBeNull, this, referenceID++);
    }
}