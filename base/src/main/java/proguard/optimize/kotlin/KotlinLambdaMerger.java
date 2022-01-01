package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.ClassConstants;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.DebugAttributeVisitor;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.ConstantTagFilter;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.instruction.visitor.InstructionConstantVisitor;
import proguard.classfile.util.ClassSubHierarchyInitializer;
import proguard.classfile.util.ClassSuperHierarchyInitializer;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.OptimizationInfoMemberFilter;
import proguard.optimize.TimedClassPoolVisitor;
import proguard.optimize.info.OptimizationCodeAttributeFilter;
import proguard.optimize.info.ParameterUsageMarker;
import proguard.optimize.peephole.MethodInliner;
import proguard.resources.file.ResourceFilePool;

import java.io.IOException;

public class KotlinLambdaMerger {

    public static final String NAME_KOTLIN_LAMBDA = "kotlin/jvm/internal/Lambda";
    public static final String NAME_KOTLIN_FUNCTION0 = "kotlin/jvm/functions/Function0";
    public static final String NAME_KOTLIN_FUNCTION1= "kotlin/jvm/functions/Function1";
    public static final String NAME_KOTLIN_FUNCTION2= "kotlin/jvm/functions/Function2";
    public static final String NAME_KOTLIN_FUNCTION3= "kotlin/jvm/functions/Function3";
    public static final String NAME_KOTLIN_FUNCTION4= "kotlin/jvm/functions/Function4";
    public static final String NAME_KOTLIN_FUNCTION5= "kotlin/jvm/functions/Function5";
    public static final String NAME_KOTLIN_FUNCTION6= "kotlin/jvm/functions/Function6";
    public static final String NAME_KOTLIN_FUNCTION7= "kotlin/jvm/functions/Function7";
    public static final String NAME_KOTLIN_FUNCTION8= "kotlin/jvm/functions/Function8";
    public static final String NAME_KOTLIN_FUNCTION9= "kotlin/jvm/functions/Function9";
    public static final String NAME_KOTLIN_FUNCTION10= "kotlin/jvm/functions/Function10";
    public static final String NAME_KOTLIN_FUNCTION11= "kotlin/jvm/functions/Function11";
    public static final String NAME_KOTLIN_FUNCTION12= "kotlin/jvm/functions/Function12";
    public static final String NAME_KOTLIN_FUNCTION13= "kotlin/jvm/functions/Function13";
    public static final String NAME_KOTLIN_FUNCTION14= "kotlin/jvm/functions/Function14";
    public static final String NAME_KOTLIN_FUNCTION15= "kotlin/jvm/functions/Function15";
    public static final String NAME_KOTLIN_FUNCTION16= "kotlin/jvm/functions/Function16";
    public static final String NAME_KOTLIN_FUNCTION17= "kotlin/jvm/functions/Function17";
    public static final String NAME_KOTLIN_FUNCTION18= "kotlin/jvm/functions/Function18";
    public static final String NAME_KOTLIN_FUNCTION19= "kotlin/jvm/functions/Function19";
    public static final String NAME_KOTLIN_FUNCTION20= "kotlin/jvm/functions/Function20";
    public static final String NAME_KOTLIN_FUNCTION21= "kotlin/jvm/functions/Function21";
    public static final String NAME_KOTLIN_FUNCTION22= "kotlin/jvm/functions/Function22";
    public static final String NAME_KOTLIN_FUNCTIONN= "kotlin/jvm/functions/FunctionN";

    private static final Logger logger = LogManager.getLogger(KotlinLambdaMerger.class);
    private final Configuration configuration;

    public KotlinLambdaMerger(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public ClassPool execute(ClassPool        programClassPool,
                             ClassPool        libraryClassPool,
                             ResourceFilePool resourceFilePool) throws IOException
    {
        // A class pool where the applicable lambda's will be stored
        ClassPool lambdaClassPool = new ClassPool();

        // get the Lambda class and the Function0 interface
        Clazz kotlinLambdaClass = getKotlinLambdaClass(programClassPool, libraryClassPool);
        Clazz kotlinFunction0Interface = getKotlinFunction0Interface(programClassPool, libraryClassPool);
        if (kotlinLambdaClass == null) {
            logger.warn("The Kotlin class '{}' is not found, but it is needed to perform lambda merging.", KOTLIN_LAMBDA_CLASS);
        }
        if (kotlinFunction0Interface == null) {
            logger.warn("The Kotlin class '{}' is not found, but it is needed to perform lambda merging.", KOTLIN_FUNCTION0_INTERFACE);
        }

        if (kotlinLambdaClass != null && kotlinFunction0Interface != null) {
            // find all lambda classes of arity 0 and with an empty closure
            // assume that the lambda classes have exactly 1 instance constructor, which has descriptor ()V
            //  (i.e. no arguments) if the closure is empty
            programClassPool.classesAccept(new ImplementedClassFilter(kotlinFunction0Interface, false,
                                           new ImplementedClassFilter(kotlinLambdaClass, false,
                                           new ClassMethodFilter("<init>", "()V",
                                           new ClassPoolFiller(lambdaClassPool), null), null), null)
            );



        }
        return programClassPool;
    }

    private Clazz getKotlinLambdaClass(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        Clazz kotlinLambdaClass = programClassPool.getClass(KOTLIN_LAMBDA_CLASS);
        if (kotlinLambdaClass == null) {
            kotlinLambdaClass = libraryClassPool.getClass(KOTLIN_LAMBDA_CLASS);
        }
        return kotlinLambdaClass;
    }

    private Clazz getKotlinFunction0Interface(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        Clazz kotlinFunction0Interface = programClassPool.getClass(KOTLIN_FUNCTION0_INTERFACE);
        if (kotlinFunction0Interface == null) {
            kotlinFunction0Interface = libraryClassPool.getClass(KOTLIN_FUNCTION0_INTERFACE);
        }
        return kotlinFunction0Interface;
    }
}
