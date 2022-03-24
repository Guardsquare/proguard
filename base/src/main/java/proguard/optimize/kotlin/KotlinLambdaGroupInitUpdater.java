package proguard.optimize.kotlin;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.BranchTargetFinder;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.InstructionSequenceMatcher;
import proguard.classfile.visitor.MemberVisitor;

public class KotlinLambdaGroupInitUpdater implements AttributeVisitor, MemberVisitor {

    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaGroupInitUpdater.class);

    public KotlinLambdaGroupInitUpdater(ClassPool programClassPool,
                                        ClassPool libraryClassPool)
    {
        this.programClassPool      = programClassPool;
        this.libraryClassPool      = libraryClassPool;
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        logger.trace("Updating method " + programMethod.getName(programClass) + programMethod.getDescriptor(programClass) + " in class " + programClass);
        programMethod.attributesAccept(programClass, this);
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // This attribute visitor should only be used for program classes.
        try
        {
            visitCodeAttribute((ProgramClass) clazz, (ProgramMethod) method, codeAttribute);
        }
        catch (ClassCastException exception)
        {
            logger.error("{} is incorrectly used to visit non-program class / method {} / {}", this.getClass().getName(), clazz, method);
        }
    }

    public void visitCodeAttribute(ProgramClass programClass, ProgramMethod programMethod, CodeAttribute codeAttribute)
    {
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

        // The arity argument is the last of all.
        // Note that the arguments start counting from 1, as the class itself is at byte 0.
        // long and double arguments take 2 bytes.
        // The classId and arity arguments are the 2 last, which take 1 byte each, as they are of type int.
        int arityIndex = KotlinLambdaGroupInitUpdater.countParameterBytes(programMethod.getDescriptor(programClass)) - 1;

        InstructionSequencesReplacer replacer = createInstructionSequenceReplacer(branchTargetFinder, codeAttributeEditor, arityIndex, programClass);

        codeAttribute.accept(programClass,
                             programMethod,
                             new PeepholeEditor(branchTargetFinder,
                                                codeAttributeEditor,
                                                replacer));
    }

    private static int countParameterBytes(String descriptor)
    {
        int counter = 1;
        int index   = 1;

        char oldC = 0;
        char c = descriptor.charAt(index++);
        while (true)
        {
            switch (c)
            {
                case TypeConstants.ARRAY:
                {
                    // Just ignore all array characters.
                    break;
                }
                case TypeConstants.CLASS_START:
                {
                    counter++;

                    // Skip the class name.
                    index = descriptor.indexOf(TypeConstants.CLASS_END, index) + 1;
                    break;
                }
                case TypeConstants.LONG:
                case TypeConstants.DOUBLE:
                    if (oldC != TypeConstants.ARRAY)
                    {
                        counter++;
                    }
                default:
                {
                    counter++;
                    break;
                }
                case TypeConstants.METHOD_ARGUMENTS_CLOSE:
                {
                    return counter;
                }
            }
            oldC = c;
            c = descriptor.charAt(index++);
        }
    }

    private InstructionSequencesReplacer createInstructionSequenceReplacer(BranchTargetFinder branchTargetFinder,
                                                                           CodeAttributeEditor codeAttributeEditor,
                                                                           int arityIndex,
                                                                           ProgramClass lambdaGroup)
    {
        InstructionSequenceBuilder builder = new InstructionSequenceBuilder(programClassPool, libraryClassPool);
        Instruction[][][] replacementPatterns = createReplacementPatternsForInit(builder, arityIndex, lambdaGroup);
        return new InstructionSequencesReplacer(builder.constants(),
                replacementPatterns,
                branchTargetFinder,
                codeAttributeEditor);
    }

    private Instruction[][][] createReplacementPatternsForInit(InstructionSequenceBuilder builder,
                                                               int arityIndex,
                                                               ProgramClass lambdaGroup)
    {
        // TODO: store classId in field
        final int X = InstructionSequenceMatcher.X;
        return new Instruction[][][]
                {
                        {
                                builder.aload_0()
                                       .iconst(X)
                                       .invokespecial(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA, ClassConstants.METHOD_NAME_INIT, "(I)V")
                                       .__(),
                                builder.aload_0()
                                       .iload(arityIndex - 1)
                                       .putfield(lambdaGroup, // store the id in a field
                                                 lambdaGroup
                                                 .findField(KotlinLambdaGroupBuilder.FIELD_NAME_ID,
                                                            KotlinLambdaGroupBuilder.FIELD_TYPE_ID))
                                       .aload_0()
                                       .iload(arityIndex)
                                       .invokespecial(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA, ClassConstants.METHOD_NAME_INIT, "(I)V")
                                       .__()
                        }
                };
    }
}
