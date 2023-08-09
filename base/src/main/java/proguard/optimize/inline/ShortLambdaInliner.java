package proguard.optimize.inline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.AppView;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.optimize.inline.lambda_locator.Lambda;

/**
 * This class is an implementation of the {@link proguard.optimize.inline.BaseLambdaInliner BaseLambdaInliner } that
 * inlines lambdas depending on the length of the lambda implementation method and the length of the consuming method.
 * The length of the consuming method is taken into account because the consuming method will be copied when inlining
 * this lambda because one method can take multiple different lambdas as an input.
 */
public class ShortLambdaInliner extends BaseLambdaInliner {
    private final Logger logger = LogManager.getLogger();
    private static final int MAXIMUM_CONSUMING_METHOD_LENGTH = 2000;
    private static final int MAXIMUM_LAMBDA_IMPL_METHOD_LENGTH = 64;

    public ShortLambdaInliner(AppView appView, Clazz consumingClass, Method consumingMethod, int calledLambdaIndex, Lambda lambda) {
        super(appView, consumingClass, consumingMethod, calledLambdaIndex, lambda);
    }

    @Override
    protected boolean shouldInline(Clazz consumingClass, Method consumingMethod, Clazz lambdaClass, Method lambdaImplMethod) {
        int consumingMethodLength = MethodLengthFinder.getMethodCodeLength(consumingClass, consumingMethod);
        int lambdaImplMethodLength = MethodLengthFinder.getMethodCodeLength(lambdaClass, lambdaImplMethod);

        boolean inline = lambdaImplMethodLength < MAXIMUM_LAMBDA_IMPL_METHOD_LENGTH && consumingMethodLength < MAXIMUM_CONSUMING_METHOD_LENGTH;
        if (!inline) {
            logger.info("Will not attempt inlining lambda because methods are too long, maximum consuming method length = {}, maximum lambda implementation method length = {}", MAXIMUM_CONSUMING_METHOD_LENGTH, MAXIMUM_LAMBDA_IMPL_METHOD_LENGTH);
            logger.info("Consuming method = {}#{}{}", consumingClass.getName(), consumingMethod.getName(consumingClass), consumingMethod.getDescriptor(consumingClass));
            logger.info("Lambda implementation method = {}#{}{}", lambdaClass.getName(), lambdaImplMethod.getName(lambdaClass), lambdaImplMethod.getDescriptor(lambdaClass));
            logger.info("Consuming method length = {}, lambda implementation method length = {}", consumingMethodLength, lambdaImplMethodLength);
        }
        return inline;
    }
}
