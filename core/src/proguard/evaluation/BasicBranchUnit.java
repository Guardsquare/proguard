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
import proguard.evaluation.value.InstructionOffsetValue;

/**
 * This {@link BranchUnit} remembers the branch unit commands that are invoked on it.
 * It doesn't consider conditions when branching.
 *
 * @author Eric Lafortune
 */
public class BasicBranchUnit
implements   BranchUnit
{
    protected InstructionOffsetValue traceBranchTargets;
    protected boolean                wasCalled;


    /**
     * Resets the accumulated branch targets and the flag that tells whether
     * any of the branch unit methods was called.
     */
    public void reset()
    {
        traceBranchTargets = InstructionOffsetValue.EMPTY_VALUE;

        wasCalled = false;
    }

    /**
     * Returns whether any of the branch unit methods was called.
     */
    public boolean wasCalled()
    {
        return wasCalled;
    }


    /**
     * Returns the accumulated branch targets that were passed to the branch
     * unit methods.
     */
    public InstructionOffsetValue getTraceBranchTargets()
    {
        return traceBranchTargets;
    }


    // Implementations for BranchUnit.

    public void branch(Clazz         clazz,
                       CodeAttribute codeAttribute,
                       int           offset,
                       int           branchTarget)
    {
        // Override the branch targets.
        traceBranchTargets = new InstructionOffsetValue(branchTarget);

        wasCalled = true;
    }


    public void branchConditionally(Clazz         clazz,
                                    CodeAttribute codeAttribute,
                                    int           offset,
                                    int           branchTarget,
                                    int           conditional)
    {
        // Accumulate the branch targets.
        traceBranchTargets =
            traceBranchTargets.add(branchTarget);

        wasCalled = true;
    }


    public void returnFromMethod()
    {
        // Stop processing this block.
        traceBranchTargets = InstructionOffsetValue.EMPTY_VALUE;

        wasCalled = true;
    }


    public void throwException()
    {
        // Stop processing this block.
        traceBranchTargets = InstructionOffsetValue.EMPTY_VALUE;

        wasCalled = true;
    }
}
