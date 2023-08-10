package proguard.optimize.lambdainline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.AppView;
import proguard.classfile.AccessConstants;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.InitializationUtil;
import proguard.optimize.lambdainline.lambdalocator.Lambda;
import proguard.optimize.lambdainline.lambdalocator.LambdaLocator;
import proguard.pass.Pass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements the lambda inlining pass that operates on the entire program.
 */
public class LambdaInliner implements Pass {
    private final Logger logger = LogManager.getLogger();
    private final String classNameFilter;
    private boolean inlinedAllUsages;
    public LambdaInliner(String classNameFilter) {
        this.classNameFilter = classNameFilter;
        this.inlinedAllUsages = true;
    }

    public LambdaInliner() {
        this("");
    }

    @Override
    public void execute(AppView appView) {
        LambdaLocator lambdaLocator = new LambdaLocator(appView.programClassPool, classNameFilter);

        for (Lambda lambda : lambdaLocator.getKotlinLambdas()) {
            logger.debug("Inlining : " + lambda);
            logger.debug("Class : " + lambda.clazz().getName());
            logger.debug("Method : " + lambda.method().getName(lambda.clazz()));
            logger.debug("Descriptor : " + lambda.method().getDescriptor(lambda.clazz()));
            Set<InstructionAtOffset> remainder = new HashSet<>();
            inlinedAllUsages = true;
            InitializationUtil.initialize(appView.programClassPool, appView.libraryClassPool);
            lambda.codeAttribute().accept(lambda.clazz(), lambda.method(),
                    new LambdaUsageFinder(lambda, lambdaLocator.getKotlinLambdaMap(),
                            new LambdaUsageHandler(appView.programClassPool, appView.libraryClassPool, remainder)
                    )
            );

            /*
             * Only remove the code needed to obtain a reference to the lambda if we were able to inline everything, if we
             * could not do that then we still need the lambda and cannot remove it.
             */
            if (inlinedAllUsages) {
                // Removing lambda obtaining code because all usages could be inlined!
                CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
                codeAttributeEditor.reset(lambda.codeAttribute().u4codeLength);
                for (InstructionAtOffset instrAtOffset : remainder) {
                    codeAttributeEditor.deleteInstruction(instrAtOffset.offset());
                }
                codeAttributeEditor.visitCodeAttribute(lambda.clazz(), lambda.method(), lambda.codeAttribute());
            }
        }
    }


    public class LambdaUsageHandler {

        private final ClassPool programClassPool;
        private final ClassPool libraryClassPool;
        private final Set<InstructionAtOffset> remainder;

        public LambdaUsageHandler(ClassPool programClassPool, ClassPool libraryClassPool, Set<InstructionAtOffset> remainder) {
            this.programClassPool = programClassPool;
            this.libraryClassPool = libraryClassPool;
            this.remainder = remainder;
        }

        boolean handle(Lambda lambda, Clazz consumingClazz, Method consumingMethod, int lambdaArgumentIndex, int consumingCallOffset, Clazz consumingCallClass, Method consumingCallMethod, CodeAttribute consumingCallCodeAttribute, List<InstructionAtOffset> sourceTrace, List<Lambda> possibleLambdas) {
            // Try inlining the lambda in consumingMethod
            BaseLambdaInliner baseLambdaInliner = new ShortLambdaInliner(programClassPool, libraryClassPool, consumingClazz, consumingMethod, lambdaArgumentIndex, lambda);
            Method inlinedLambamethod = baseLambdaInliner.inline();

            // We didn't inline anything so no need to change any call instructions.
            if (inlinedLambamethod == null) {
                inlinedAllUsages = false;
                return false;
            }

            if (possibleLambdas.size() > 1) {
                // This lambda is part of a collection of lambdas that might potentially be used, but we do not know which one is actually used. Because of that we cannot inline it.
                inlinedAllUsages = false;
                return false;
            }

            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
            codeAttributeEditor.reset(consumingCallCodeAttribute.u4codeLength);

            /*
             * Remove usages that bring the variable on the stack so all the way up until a load
             * The store operations before the load might also be used by other functions calls that consume the
             * lambda, that's why we need to keep them.
             */
            for (int i = 0; i < sourceTrace.size(); i++) {
                InstructionAtOffset instrAtOffset = sourceTrace.get(i);
                codeAttributeEditor.deleteInstruction(instrAtOffset.offset());
                if (instrAtOffset.instruction().canonicalOpcode() == Instruction.OP_ALOAD || instrAtOffset.instruction().canonicalOpcode() == Instruction.OP_INVOKESTATIC) {
                    remainder.addAll(sourceTrace.subList(i + 1, sourceTrace.size()));
                    break;
                }
            }

            // Replace invokestatic call to a call with the new function
            ConstantPoolEditor constantPoolEditor = new ConstantPoolEditor((ProgramClass) consumingCallClass);
            int methodWithoutLambdaParameterIndex = constantPoolEditor.addMethodrefConstant(consumingClazz, inlinedLambamethod);

            // Replacing at consumingCallOffset
            if ((consumingMethod.getAccessFlags() & AccessConstants.STATIC) == 0) {
                codeAttributeEditor.replaceInstruction(consumingCallOffset, new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, methodWithoutLambdaParameterIndex));
            } else {
                codeAttributeEditor.replaceInstruction(consumingCallOffset, new ConstantInstruction(Instruction.OP_INVOKESTATIC, methodWithoutLambdaParameterIndex));
            }

            codeAttributeEditor.visitCodeAttribute(consumingCallClass, consumingCallMethod, consumingCallCodeAttribute);
            logger.info("Inlined a lambda into {}#{}{}", consumingClazz.getName(), consumingMethod.getName(consumingClazz), consumingMethod.getDescriptor(consumingClazz));
            return true;
        }
    }
}
