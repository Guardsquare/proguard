package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.visitor.ClassPoolFiller;
import proguard.classfile.visitor.ImplementedClassFilter;
import proguard.resources.file.ResourceFilePool;

import java.io.IOException;

public class KotlinLambdaMerger {

    private final String KOTLIN_LAMBDA_CLASS = "kotlin/jvm/internal/Lambda";
    private final String KOTLIN_FUNCTION0_INTERFACE = "kotlin/jvm/functions/Function0";

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
            // find all lambda classes of arity 0
            programClassPool.classesAccept(new ImplementedClassFilter(kotlinFunction0Interface, false,
                                           new ImplementedClassFilter(kotlinLambdaClass, false,
                                           new ClassPoolFiller(lambdaClassPool), null), null)
            );
            // filter for lambda's without constructor arguments -> put them in a classpool
            // TODO: filter on classes that have a constructor WITHOUT arguments!
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
    }
}
