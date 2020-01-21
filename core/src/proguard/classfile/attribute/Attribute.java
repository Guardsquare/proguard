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
package proguard.classfile.attribute;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.util.SimpleProcessable;

/**
 * This abstract class represents an attribute that is attached to a class,
 * a class member, or a code attribute. Specific types of attributes are
 * subclassed from it.
 *
 * @author Eric Lafortune
 * @noinspection AbstractClassWithoutAbstractMethods
 */
public abstract class Attribute extends SimpleProcessable
{
    public static final String BOOTSTRAP_METHODS                       = "BootstrapMethods";
    public static final String SOURCE_FILE                             = "SourceFile";
    public static final String SOURCE_DIR                              = "SourceDir";
    public static final String INNER_CLASSES                           = "InnerClasses";
    public static final String ENCLOSING_METHOD                        = "EnclosingMethod";
    public static final String NEST_HOST                               = "NestHost";
    public static final String NEST_MEMBERS                            = "NestMembers";
    public static final String DEPRECATED                              = "Deprecated";
    public static final String SYNTHETIC                               = "Synthetic";
    public static final String SIGNATURE                               = "Signature";
    public static final String CONSTANT_VALUE                          = "ConstantValue";
    public static final String METHOD_PARAMETERS                       = "MethodParameters";
    public static final String EXCEPTIONS                              = "Exceptions";
    public static final String CODE                                    = "Code";
    public static final String STACK_MAP                               = "StackMap";
    public static final String STACK_MAP_TABLE                         = "StackMapTable";
    public static final String LINE_NUMBER_TABLE                       = "LineNumberTable";
    public static final String LOCAL_VARIABLE_TABLE                    = "LocalVariableTable";
    public static final String LOCAL_VARIABLE_TYPE_TABLE               = "LocalVariableTypeTable";
    public static final String RUNTIME_VISIBLE_ANNOTATIONS             = "RuntimeVisibleAnnotations";
    public static final String RUNTIME_INVISIBLE_ANNOTATIONS           = "RuntimeInvisibleAnnotations";
    public static final String RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS   = "RuntimeVisibleParameterAnnotations";
    public static final String RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = "RuntimeInvisibleParameterAnnotations";
    public static final String RUNTIME_VISIBLE_TYPE_ANNOTATIONS        = "RuntimeVisibleTypeAnnotations";
    public static final String RUNTIME_INVISIBLE_TYPE_ANNOTATIONS      = "RuntimeInvisibleTypeAnnotations";
    public static final String ANNOTATION_DEFAULT                      = "AnnotationDefault";
    public static final String MODULE                                  = "Module";
    public static final String MODULE_MAIN_CLASS                       = "ModuleMainClass";
    public static final String MODULE_PACKAGES                         = "ModulePackages";
    // TODO: More attributes.
    public static final String SOURCE_DEBUG_EXTENSION                  = "SourceDebugExtension";
    public static final String CHARACTER_RANGE_TABLE                   = "CharacterRangeTable";
    public static final String COMPILATION_I_D                         = "CompilationID";
    public static final String SOURCE_I_D                              = "SourceID";


    public int u2attributeNameIndex;
    //public int  u4attributeLength;
    //public byte info[];

    /**
     * Create an uninitialized Attribute.
     */
    protected Attribute()
    {
    }


    /**
     * Create an initialized Attribute.
     */
    protected Attribute(int u2attributeNameIndex)
    {
        this.u2attributeNameIndex = u2attributeNameIndex;
    }


    /**
     * Returns the String name of the attribute.
     */
    public String getAttributeName(Clazz clazz)
    {
        return clazz.getString(u2attributeNameIndex);
    }


    // Methods to be implemented by extensions, if applicable.

    /**
     * Accepts the given visitor.
     */
    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        throw new UnsupportedOperationException("Method must be overridden in ["+this.getClass().getName()+"] if ever called");
    }

    /**
     * Accepts the given visitor in the context of the given field.
     */
    public void accept(Clazz clazz, Field field, AttributeVisitor attributeVisitor)
    {
        // Delegate to the default invocation if the field is null anyway.
        if (field == null)
        {
            accept(clazz, attributeVisitor);
        }
        else
        {
            throw new UnsupportedOperationException("Method must be overridden in ["+this.getClass().getName()+"] if ever called");
        }
    }

    /**
     * Accepts the given visitor in the context of the given method.
     */
    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        // Delegate to the default invocation if the method is null anyway.
        if (method == null)
        {
            accept(clazz, (Field)null, attributeVisitor);
        }
        else
        {
            throw new UnsupportedOperationException("Method must be overridden in ["+this.getClass().getName()+"] if ever called");
        }
    }

    /**
     * Accepts the given visitor in the context of the given code attribute.
     */
    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, AttributeVisitor attributeVisitor)
    {
        // Delegate to the default invocation if the code attribute is null
        // anyway.
        if (codeAttribute == null)
        {
            accept(clazz, method, attributeVisitor);
        }
        else
        {
            throw new UnsupportedOperationException("Method must be overridden in ["+this.getClass().getName()+"] if ever called");
        }
    }
}
