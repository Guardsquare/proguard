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
        return programClassPool;
    }
}
