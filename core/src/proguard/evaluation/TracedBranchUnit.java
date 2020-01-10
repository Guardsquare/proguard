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
import proguard.evaluation.BasicBranchUnit;
import proguard.evaluation.value.Value;

/**
 * This {@link BranchUnit} remembers the branch unit commands that are invoked on it.
 *
 * @author Eric Lafortune
 */
class   TracedBranchUnit
extends BasicBranchUnit
{
    private boolean isFixed;


    // Implementations for BasicBranchUnit.

    public void reset()
    {
        super.reset();

        isFixed = false;
    }


    // Implementations for BranchUnit.

    public void branch(Clazz         clazz,
                       CodeAttribute codeAttribute,
                       int           offset,
                       int           branchTarget)
    {
        super.branch(clazz, codeAttribute, offset, branchTarget);

        isFixed = true;
    }


    public void branchConditionally(Clazz         clazz,
                                    CodeAttribute codeAttribute,
                                    int           offset,
                                    int           branchTarget,
                                    int           conditional)
    {
        if      (conditional == Value.ALWAYS)
        {
            // Always branch.
            super.branch(clazz, codeAttribute, offset, branchTarget);

            isFixed = true;
        }
        else if (conditional == Value.MAYBE)
        {
            if (!isFixed)
            {
                // Maybe branch.
                super.branchConditionally(clazz, codeAttribute, offset, branchTarget, conditional);
            }
        }
        else
        {
            super.wasCalled = true;
        }
    }
}
