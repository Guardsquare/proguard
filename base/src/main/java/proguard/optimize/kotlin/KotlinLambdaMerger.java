package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.ClassConstants;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.instruction.visitor.InstructionCounter;
import proguard.classfile.util.ClassInitializer;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.MethodInlinerWrapper;
import proguard.optimize.info.ProgramClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfoSetter;
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter;
import proguard.optimize.peephole.LineNumberLinearizer;
import proguard.optimize.peephole.SameClassMethodInliner;
import proguard.resources.file.ResourceFilePool;
import proguard.shrink.*;
import proguard.util.ProcessingFlags;

import java.io.IOException;

public class KotlinLambdaMerger {

    public static final String NAME_KOTLIN_LAMBDA     = "kotlin/jvm/internal/Lambda";
    public static final String NAME_KOTLIN_FUNCTION0  = "kotlin/jvm/functions/Function0";
    public static final String NAME_KOTLIN_FUNCTION1  = "kotlin/jvm/functions/Function1";
    public static final String NAME_KOTLIN_FUNCTION2  = "kotlin/jvm/functions/Function2";
    public static final String NAME_KOTLIN_FUNCTION3  = "kotlin/jvm/functions/Function3";
    public static final String NAME_KOTLIN_FUNCTION4  = "kotlin/jvm/functions/Function4";
    public static final String NAME_KOTLIN_FUNCTION5  = "kotlin/jvm/functions/Function5";
    public static final String NAME_KOTLIN_FUNCTION6  = "kotlin/jvm/functions/Function6";
    public static final String NAME_KOTLIN_FUNCTION7  = "kotlin/jvm/functions/Function7";
    public static final String NAME_KOTLIN_FUNCTION8  = "kotlin/jvm/functions/Function8";
    public static final String NAME_KOTLIN_FUNCTION9  = "kotlin/jvm/functions/Function9";
    public static final String NAME_KOTLIN_FUNCTION10 = "kotlin/jvm/functions/Function10";
    public static final String NAME_KOTLIN_FUNCTION11 = "kotlin/jvm/functions/Function11";
    public static final String NAME_KOTLIN_FUNCTION12 = "kotlin/jvm/functions/Function12";
    public static final String NAME_KOTLIN_FUNCTION13 = "kotlin/jvm/functions/Function13";
    public static final String NAME_KOTLIN_FUNCTION14 = "kotlin/jvm/functions/Function14";
    public static final String NAME_KOTLIN_FUNCTION15 = "kotlin/jvm/functions/Function15";
    public static final String NAME_KOTLIN_FUNCTION16 = "kotlin/jvm/functions/Function16";
    public static final String NAME_KOTLIN_FUNCTION17 = "kotlin/jvm/functions/Function17";
    public static final String NAME_KOTLIN_FUNCTION18 = "kotlin/jvm/functions/Function18";
    public static final String NAME_KOTLIN_FUNCTION19 = "kotlin/jvm/functions/Function19";
    public static final String NAME_KOTLIN_FUNCTION20 = "kotlin/jvm/functions/Function20";
    public static final String NAME_KOTLIN_FUNCTION21 = "kotlin/jvm/functions/Function21";
    public static final String NAME_KOTLIN_FUNCTION22 = "kotlin/jvm/functions/Function22";
    public static final String NAME_KOTLIN_FUNCTIONN  = "kotlin/jvm/functions/FunctionN";

    private static final Logger logger = LogManager.getLogger(KotlinLambdaMerger.class);
    private final Configuration configuration;

    public KotlinLambdaMerger(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public ClassPool execute(final ClassPool        programClassPool,
                             final ClassPool        libraryClassPool,
                             final ResourceFilePool resourceFilePool,
                             final ExtraDataEntryNameMap extraDataEntryNameMap) throws IOException
    {

        // Remove old processing info
        programClassPool.classesAccept(new ClassCleaner());
        libraryClassPool.classesAccept(new ClassCleaner());

        // get the Lambda class and the Function0 interface
        Clazz kotlinLambdaClass = getKotlinLambdaClass(programClassPool, libraryClassPool);
        if (kotlinLambdaClass == null) {
            logger.warn("The Kotlin class '{}' is not found, but it is needed to perform lambda merging.", NAME_KOTLIN_LAMBDA);
        }
        else
        {
            // A class pool where the applicable lambda's will be stored
            ClassPool lambdaClassPool = new ClassPool();
            ClassPool newProgramClassPool = new ClassPool();
            ClassPoolFiller newProgramClassPoolFiller = new ClassPoolFiller(newProgramClassPool);

            // find all lambda classes with an empty closure
            // assume that the lambda classes have exactly 1 instance constructor, which has descriptor ()V
            //  (i.e. no arguments) if the closure is empty
            programClassPool.classesAccept(new ClassProcessingFlagFilter(ProcessingFlags.DONT_OPTIMIZE, 0, newProgramClassPoolFiller));
            programClassPool.classesAccept(new ClassProcessingFlagFilter(0, ProcessingFlags.DONT_OPTIMIZE,
                                           new ImplementedClassFilter(kotlinLambdaClass, false,
                                           new ClassMethodFilter(ClassConstants.METHOD_NAME_INIT, ClassConstants.METHOD_TYPE_INIT,
                                           new ClassPoolFiller(lambdaClassPool),
                                           newProgramClassPoolFiller),
                                           newProgramClassPoolFiller))
            );

            // group the lambda's per package
            PackageGrouper packageGrouper = new PackageGrouper();
            lambdaClassPool.classesAccept(packageGrouper);

            // add optimisation info to the lambda's, so that it can be filled out later
            lambdaClassPool.classesAccept(new ProgramClassOptimizationInfoSetter());
            lambdaClassPool.classesAccept(new AllMemberVisitor(
                                          new ProgramMemberOptimizationInfoSetter()));

            ClassPool lambdaGroupClassPool = new ClassPool();

            // merge the lambda's per package
            KotlinLambdaClassMerger merger = new KotlinLambdaClassMerger(this.configuration,
                                                                         programClassPool,
                                                                         libraryClassPool,
                                                                         new MultiClassVisitor(
                                                                         new ClassPoolFiller(lambdaGroupClassPool),
                                                                         newProgramClassPoolFiller),
                                                                         extraDataEntryNameMap);
            packageGrouper.packagesAccept(merger);

            // initialise the references from and to the newly created lambda groups and their enclosing classes
            newProgramClassPool.classesAccept(new ClassInitializer(newProgramClassPool, libraryClassPool));

            // inline the helper invoke methods into the general invoke method
            inlineMethodsInsideLambdaGroups(newProgramClassPool, libraryClassPool, lambdaGroupClassPool);

            // remove the unused helper methods from the lambda groups
            shrinkLambdaGroups(newProgramClassPool, libraryClassPool, resourceFilePool, lambdaGroupClassPool);

            logger.info("{} lambda class(es) found that can be merged.", lambdaClassPool.size());
            logger.info("{} lambda group(s) created.", lambdaGroupClassPool.size());
            logger.info("#lambda groups/#lambda classes ratio = {}/{} = {}%", lambdaGroupClassPool.size(), lambdaClassPool.size(), 100 * lambdaGroupClassPool.size() / lambdaClassPool.size());
            logger.info("Size of original program class pool: {}", programClassPool.size());
            logger.info("Size of new program class pool: {}", newProgramClassPool.size());

            return newProgramClassPool;
        }
        return programClassPool;
    }

    private void inlineMethodsInsideLambdaGroups(ClassPool programClassPool, ClassPool libraryClassPool, ClassPool lambdaGroupClassPool)
    {
        InstructionCounter methodInliningCounter = new InstructionCounter();

        lambdaGroupClassPool.classesAccept(new MultiClassVisitor(
                                           new ProgramClassOptimizationInfoSetter(),
                                           new AllMemberVisitor(
                                           new ProgramMemberOptimizationInfoSetter()),
                                           new AllMethodVisitor(
                                           new AllAttributeVisitor(
                                           new SameClassMethodInliner(configuration.microEdition,
                                                                      configuration.android,
                                                                      configuration.allowAccessModification,
                                                                      methodInliningCounter)))));
        logger.debug("{} methods inlined inside lambda groups.", methodInliningCounter.getCount());
    }

    private void shrinkLambdaGroups(ClassPool programClassPool, ClassPool libraryClassPool, ResourceFilePool resourceFilePool, ClassPool lambdaGroupClassPool)
    {
        SimpleUsageMarker simpleUsageMarker = new SimpleUsageMarker();
        ClassUsageMarker classUsageMarker = new ClassUsageMarker(simpleUsageMarker);

        // make sure that the used methods of the lambda groups are marked as used
        // by marking all classes and methods
        // note: if -dontshrink is
        /*new UsageMarker(configuration).mark(programClassPool,
                                            libraryClassPool,
                                            resourceFilePool,
                                            simpleUsageMarker,
                                            classUsageMarker);*/
        libraryClassPool.classesAccept(classUsageMarker);
        // but don't mark the lambda groups and their members in case they are not used,
        // e.g. inlined helper invoke methods
        programClassPool.classesAccept(new ClassNameFilter(
                                       "**/" + KotlinLambdaClassMerger.NAME_LAMBDA_GROUP,
                                       (ClassVisitor) null,
                                       new MultiClassVisitor(
                                       classUsageMarker,
                                       new AllMemberVisitor(
                                       classUsageMarker))));

        // ensure that the interfaces of the lambda group are not removed
        lambdaGroupClassPool.classesAccept(new InterfaceUsageMarker(
                                           classUsageMarker));

        // remove the unused parts of the lambda groups, such as the inlined invoke helper methods
        // and make sure that the line numbers are updated
        lambdaGroupClassPool.classesAccept(new MultiClassVisitor(
                                           new ClassShrinker(simpleUsageMarker),
                                           new LineNumberLinearizer()));
    }

    private Clazz getKotlinLambdaClass(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        Clazz kotlinLambdaClass = programClassPool.getClass(NAME_KOTLIN_LAMBDA);
        if (kotlinLambdaClass == null) {
            kotlinLambdaClass = libraryClassPool.getClass(NAME_KOTLIN_LAMBDA);
        }
        return kotlinLambdaClass;
    }

    private Clazz getKotlinFunction0Interface(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        Clazz kotlinFunction0Interface = programClassPool.getClass(NAME_KOTLIN_FUNCTION0);
        if (kotlinFunction0Interface == null) {
            kotlinFunction0Interface = libraryClassPool.getClass(NAME_KOTLIN_FUNCTION0);
        }
        return kotlinFunction0Interface;
    }

    /**
     * Checks whether the given lambda class should still be merged.
     * Returns true if the lambda class has not yet been merged and is allowed to be merged.
     * @param lambdaClass the lambda class for which should be checked whether it should be merged
     */
    public static boolean shouldMerge(ProgramClass lambdaClass)
    {
        ProgramClassOptimizationInfo optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass);
        return (lambdaClass.getProcessingFlags() & ProcessingFlags.DONT_OPTIMIZE) == 0
                && optimizationInfo.getLambdaGroup() == null
                && optimizationInfo.mayBeMerged()
                && lambdaClass.extendsOrImplements(NAME_KOTLIN_LAMBDA);
    }
}
