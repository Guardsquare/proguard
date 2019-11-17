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
package proguard.evaluation;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.CodeAttribute;

/**
 * This InstructionVisitor evaluates the instructions that it visits.
 *
 * @author Eric Lafortune
 */
public interface BranchUnit
{
    /**
     * Sets the new instruction offset.
     */
    public void branch(Clazz         clazz,
                       CodeAttribute codeAttribute,
                       int           offset,
                       int           branchTarget);


    /**
     * Sets the new instruction offset, depending on the certainty of the
     * conditional branch.
     */
    public void branchConditionally(Clazz         clazz,
                                    CodeAttribute codeAttribute,
                                    int           offset,
                                    int           branchTarget,
                                    int           conditional);


    /**
     * Returns from the method with the given value.
     */
    public void returnFromMethod();


    /**
     * Handles the throwing of an exception.
     */
    public void throwException();
}
