package proguard.optimize.kotlin;

import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import proguard.classfile.instruction.Instruction;

import java.util.ArrayList;
import java.util.List;

public class LambdaGroupInvokeCodeBuilder {

    private int caseIndexCounter = 0;
    // tune the initial list size depending on the expected amount of lambda's that are merged
    private final List<Instruction[]> caseInstructions = new ArrayList<>(5);

    public void addNewCase(Instruction[] instructions)
    {
        int caseIndex = getNewCaseIndex();
        caseInstructions.add(instructions);
    }

    private int getNewCaseIndex()
    {
        int caseIndex = this.caseIndexCounter;
        this.caseIndexCounter++;
        return caseIndex;
    }

    public int getCaseIndexCounter()
    {
        return this.caseIndexCounter;
    }

    public ClassBuilder.CodeBuilder build(ProgramClass lambdaGroup, ProgramField classIdField)
    {
        return composer -> {
            //InstructionSequenceBuilder instructionSequenceBuilder = new InstructionSequenceBuilder();
            // TODO: use instruction builder or code attribute composer to build (a part of)
            //  the code attribute of the invoke method of the lambda group
            int cases = getCaseIndexCounter();

            if (cases == 0)
            {
                composer.return_();
                return;
            }

            CompactCodeAttributeComposer.Label[] caseLabels = new CompactCodeAttributeComposer.Label[cases - 1];
            for (int caseIndex = 0; caseIndex < cases - 1; caseIndex++)
            {
                caseLabels[caseIndex] = composer.createLabel();
            }
            CompactCodeAttributeComposer.Label defaultLabel = composer.createLabel();
            CompactCodeAttributeComposer.Label endOfMethodLabel = composer.createLabel();
            composer
                    .aload_0()
                    .getfield(lambdaGroup, classIdField)
                    // add extra instructions?
                    .tableswitch(defaultLabel, 0, cases - 2, caseLabels);
            for (int caseIndex = 0; caseIndex < cases - 1; caseIndex++)
            {
                composer
                        .label(caseLabels[caseIndex])
                        .appendInstructions(caseInstructions.get(caseIndex))
                        .goto_(endOfMethodLabel);
            }
            composer
                    .label(defaultLabel)
                    .appendInstructions(caseInstructions.get(cases - 1))
                    .label(endOfMethodLabel)
                    .areturn();
        };
    }
}
