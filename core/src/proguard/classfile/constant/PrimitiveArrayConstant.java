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

package proguard.classfile.constant;

import proguard.classfile.*;
import proguard.classfile.constant.visitor.*;

/**
 * This unofficial Constant represents an array of primitives in the constant
 * pool. It is not supported by any Java specification and therefore only for
 * internal use.
 *
 * @author Eric Lafortune
 */
public class PrimitiveArrayConstant extends Constant
{
    public Object values;


    /**
     * Creates an uninitialized PrimitiveArrayConstant.
     */
    public PrimitiveArrayConstant()
    {
    }


    /**
     * Creates a new PrimitiveArrayConstant with the given array of values.
     */
    public PrimitiveArrayConstant(Object values)
    {
        this.values = values;
    }


    /**
     * Returns the type of the elements of the primitive array.
     */
    public char getPrimitiveType()
    {
        return values instanceof boolean[] ? TypeConstants.BOOLEAN :
               values instanceof byte[]    ? TypeConstants.BYTE    :
               values instanceof char[]    ? TypeConstants.CHAR    :
               values instanceof short[]   ? TypeConstants.SHORT   :
               values instanceof int[]     ? TypeConstants.INT     :
               values instanceof float[]   ? TypeConstants.FLOAT   :
               values instanceof long[]    ? TypeConstants.LONG    :
               values instanceof double[]  ? TypeConstants.DOUBLE  :
                                             0;
    }


    /**
     * Returns the length of the primitive array.
     */
    public int getLength()
    {
        return values instanceof boolean[] ? ((boolean[])values).length :
               values instanceof byte[]    ? ((byte[]   )values).length :
               values instanceof char[]    ? ((char[]   )values).length :
               values instanceof short[]   ? ((short[]  )values).length :
               values instanceof int[]     ? ((int[]    )values).length :
               values instanceof float[]   ? ((float[]  )values).length :
               values instanceof long[]    ? ((long[]   )values).length :
               values instanceof double[]  ? ((double[] )values).length :
                                             0;
    }


    /**
     * Returns the values.
     */
    public Object getValues()
    {
        return values;
    }


    /**
     * Applies the given PrimitiveArrayConstantVisitor to the primitive array.
     */
    public void primitiveArrayAccept(Clazz clazz, PrimitiveArrayConstantVisitor primitiveArrayConstantVisitor)
    {
        // The primitive arrays themselves don't accept visitors, so we have to
        // use instanceof tests.
        if (values instanceof boolean[])
        {
            primitiveArrayConstantVisitor.visitBooleanArrayConstant(clazz, this, (boolean[])values);
        }
        else if (values instanceof byte[])
        {
            primitiveArrayConstantVisitor.visitByteArrayConstant(clazz, this, (byte[])values);
        }
        else if (values instanceof char[])
        {
            primitiveArrayConstantVisitor.visitCharArrayConstant(clazz, this, (char[])values);
        }
        else if (values instanceof short[])
        {
            primitiveArrayConstantVisitor.visitShortArrayConstant(clazz, this, (short[])values);
        }
        else if (values instanceof int[])
        {
            primitiveArrayConstantVisitor.visitIntArrayConstant(clazz, this, (int[])values);
        }
        else if (values instanceof float[])
        {
            primitiveArrayConstantVisitor.visitFloatArrayConstant(clazz, this, (float[])values);
        }
        else if (values instanceof long[])
        {
            primitiveArrayConstantVisitor.visitLongArrayConstant(clazz, this, (long[])values);
        }
        else if (values instanceof double[])
        {
            primitiveArrayConstantVisitor.visitDoubleArrayConstant(clazz, this, (double[])values);
        }
    }


    /**
     * Applies the given PrimitiveArrayConstantElementVisitor to all elements
     * of the primitive array.
     */
    public void primitiveArrayElementsAccept(Clazz clazz, PrimitiveArrayConstantElementVisitor primitiveArrayConstantElementVisitor)
    {
        // The primitive arrays themselves don't accept visitors, so we have to
        // use instanceof tests.
        if (values instanceof boolean[])
        {
            boolean[] booleanValues = (boolean[])this.values;
            for (int index = 0; index < booleanValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitBooleanArrayConstantElement(clazz, this, index, booleanValues[index]);
            }
        }
        else if (values instanceof byte[])
        {
            byte[] byteValues = (byte[])this.values;
            for (int index = 0; index < byteValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitByteArrayConstantElement(clazz, this, index, byteValues[index]);
            }
        }
        else if (values instanceof char[])
        {
            char[] charValues = (char[])this.values;
            for (int index = 0; index < charValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitCharArrayConstantElement(clazz, this, index, charValues[index]);
            }
        }
        else if (values instanceof short[])
        {
            short[] shortValues = (short[])this.values;
            for (int index = 0; index < shortValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitShortArrayConstantElement(clazz, this, index, shortValues[index]);
            }
        }
        else if (values instanceof int[])
        {
            int[] intValues = (int[])this.values;
            for (int index = 0; index < intValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitIntArrayConstantElement(clazz, this, index, intValues[index]);
            }
        }
        else if (values instanceof float[])
        {
            float[] floatValues = (float[])this.values;
            for (int index = 0; index < floatValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitFloatArrayConstantElement(clazz, this, index, floatValues[index]);
            }
        }
        else if (values instanceof long[])
        {
            long[] longValues = (long[])this.values;
            for (int index = 0; index < longValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitLongArrayConstantElement(clazz, this, index, longValues[index]);
            }
        }
        else if (values instanceof double[])
        {
            double[] doubleValues = (double[])this.values;
            for (int index = 0; index < doubleValues.length; index++)
            {
                primitiveArrayConstantElementVisitor.visitDoubleArrayConstantElement(clazz, this, index, doubleValues[index]);
            }
        }
    }


    // Implementations for Constant.

    public int getTag()
    {
        return Constant.PRIMITIVE_ARRAY;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitPrimitiveArrayConstant(clazz, this);
    }
}
