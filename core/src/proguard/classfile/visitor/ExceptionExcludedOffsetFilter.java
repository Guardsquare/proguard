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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.ExceptionInfoVisitor;

/**
 * This {@link ExceptionInfoVisitor} delegates its visits to another given
 * {@link ExceptionInfoVisitor}, but only when the visited exception
 * does not cover the instruction at the given offset.
 *
 * @author Eric Lafortune
 */
public class ExceptionExcludedOffsetFilter
implements   ExceptionInfoVisitor
{
    private final int                  instructionOffset;
    private final ExceptionInfoVisitor exceptionInfoVisitor;


    /**
     * Creates a new ExceptionExcludedOffsetFilter.
     * @param instructionOffset    the instruction offset.
     * @param exceptionInfoVisitor the ExceptionInfoVisitor to which visits
     *                             will be delegated.
     */
    public ExceptionExcludedOffsetFilter(int                  instructionOffset,
                                         ExceptionInfoVisitor exceptionInfoVisitor)
    {
        this.instructionOffset    = instructionOffset;
        this.exceptionInfoVisitor = exceptionInfoVisitor;
    }


    // Implementations for ExceptionInfoVisitor.

    public void visitExceptionInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, ExceptionInfo exceptionInfo)
    {
        if (!exceptionInfo.isApplicable(instructionOffset))
        {
            exceptionInfoVisitor.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);
        }
    }
}
