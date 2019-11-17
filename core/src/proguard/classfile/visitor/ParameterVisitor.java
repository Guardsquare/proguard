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
package proguard.classfile.visitor;

import proguard.classfile.*;

/**
 * This interface specifies the methods for a visitor of method parameters or
 * field types (which can be considered parameters when storing values). The
 * parameters do not include or count the 'this' parameter or the method return
 * value.
 *
 * @author Eric Lafortune
 */
public interface ParameterVisitor
{
    /**
     * Visits the given parameter.
     * @param clazz           the class of the method.
     * @param member          the field or method of the parameter.
     * @param parameterIndex  the index of the parameter.
     * @param parameterCount  the total number of parameters.
     * @param parameterOffset the offset of the parameter, accounting for
     *                        longs and doubles taking up two entries.
     * @param parameterSize   the total size of the parameters, accounting for
     *                        longs and doubles taking up two entries.
     * @param parameterType   the parameter type.
     * @param referencedClass the class contained in the parameter type, if any.
     */
    public void visitParameter(Clazz  clazz,
                               Member member,
                               int    parameterIndex,
                               int    parameterCount,
                               int    parameterOffset,
                               int    parameterSize,
                               String parameterType,
                               Clazz  referencedClass);
}
