package proguard.optimize.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import java.io.PrintWriter;

public class LambdaGroupMappingPrinter implements ClassVisitor {

    private final PrintWriter pw;

    /**
     * Creates a new LambdaGroupMappingPrinter that prints to the given writer.
     * @param printWriter the writer to which to print.
     */
    public LambdaGroupMappingPrinter(PrintWriter printWriter)
    {
        this.pw = printWriter;
    }


    @Override
    public void visitAnyClass(Clazz clazz) { }

    @Override
    public void visitProgramClass(ProgramClass programClass) {
        String name    = programClass.getName();
        ProgramClassOptimizationInfo optimizationInfo = 
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        Clazz lambdaGroup = optimizationInfo.getLambdaGroup();
        if (lambdaGroup != null)
        {
            String lambdaGroupName = lambdaGroup.getName();
            // Print out the class to lambda group mapping.
            pw.println(ClassUtil.externalClassName(name) +
                    " -> " +
                    ClassUtil.externalClassName(lambdaGroupName) +
                    " (arity " +
                    KotlinLambdaMerger.getArityFromInterface(programClass) +
                    ", case " +
                    optimizationInfo.getLambdaGroupClassId() +
                    ")");
        }
    }
}
