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
package proguard.classfile.attribute.preverification;

/**
 * This class provides methods to create and reuse {@link IntegerType} instances.
 *
 * @author Eric Lafortune
 */
public class VerificationTypeFactory
{
    // Shared copies of Type objects, to avoid creating a lot of objects.
    static final IntegerType           INTEGER_TYPE            = new IntegerType();
    static final LongType              LONG_TYPE               = new LongType();
    static final FloatType             FLOAT_TYPE              = new FloatType();
    static final DoubleType            DOUBLE_TYPE             = new DoubleType();
    static final TopType               TOP_TYPE                = new TopType();
    static final NullType              NULL_TYPE               = new NullType();
    static final UninitializedThisType UNINITIALIZED_THIS_TYPE = new UninitializedThisType();


    /**
     * Creates a new IntegerType.
     */
    public static IntegerType createIntegerType()
    {
        return INTEGER_TYPE;
    }

    /**
     * Creates a new LongType.
     */
    public static LongType createLongType()
    {
        return LONG_TYPE;
    }

    /**
     * Creates a new FloatType.
     */
    public static FloatType createFloatType()
    {
        return FLOAT_TYPE;
    }

    /**
     * Creates a new DoubleType.
     */
    public static DoubleType createDoubleType()
    {
        return DOUBLE_TYPE;
    }

    /**
     * Creates a new TopType.
     */
    public static TopType createTopType()
    {
        return TOP_TYPE;
    }

    /**
     * Creates a new NullType.
     */
    public static NullType createNullType()
    {
        return NULL_TYPE;
    }

    /**
     * Creates a new UninitializedThisType.
     */
    public static UninitializedThisType createUninitializedThisType()
    {
        return UNINITIALIZED_THIS_TYPE;
    }

    /**
     * Creates a new UninitializedType for an instance that was created at
     * the given offset.
     */
    public static UninitializedType createUninitializedType(int newInstructionOffset)
    {
        return new UninitializedType(newInstructionOffset);
    }

    /**
     * Creates a new ObjectType of the given type.
     */
    public static ObjectType createObjectType(int classIndex)
    {
        return new ObjectType(classIndex);
    }
}
