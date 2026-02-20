package proguard.optimize.kotlin;

import static proguard.classfile.AccessConstants.STATIC;
import static proguard.optimize.info.ParameterUsageMarker.markParameterUsed;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.optimize.MethodDescriptorShrinker;
import proguard.optimize.info.ParameterUsageMarker;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

/**
 * This Kotlin metadata visitor marks the parameters of methods used, using the {@link
 * ParameterUsageMarker}, if those parameters correspond to Kotlin context parameters.
 *
 * <p>This is done to ensure that the list of context parameters does not get out of sync, as
 * otherwise unused context receiver parameters may be removed.
 *
 * <p>On the Java method level, each context parameter is passed as a leading parameter to a
 * function's JVM method or a property getter/setter method.
 *
 * <p><code>
 *    context(foo: Foo, bar: Bar)
 *    fun foo(string:String) { }
 *    // -> public static void foo(LFoo;LBar;Ljava/lang/String;)V
 * </code>
 *
 * <p>TODO(T18173): Implement proper shrinking of context receivers when the underlying parameter is
 * not used i.e. consistently remove context parameters when the underlying parameter is removed in
 * e.g. {@link MethodDescriptorShrinker}
 *
 * @see ParameterUsageMarker
 * @see MethodDescriptorShrinker
 */
public class KotlinContextParameterUsageMarker
        implements KotlinMetadataVisitor, KotlinFunctionVisitor, KotlinPropertyVisitor {

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitAnyFunction(
            Clazz clazz, KotlinMetadata kotlinMetadata, KotlinFunctionMetadata kotlinFunctionMetadata) {
        markContextParameters(
                kotlinFunctionMetadata.contextParameters,
                kotlinFunctionMetadata.referencedMethod,
                kotlinFunctionMetadata.referencedDefaultMethod);
    }

    @Override
    public void visitAnyProperty(
            Clazz clazz,
            KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
            KotlinPropertyMetadata kotlinPropertyMetadata) {
        markContextParameters(
                kotlinPropertyMetadata.contextParameters,
                kotlinPropertyMetadata.getterMetadata.referencedMethod,
                kotlinPropertyMetadata.setterMetadata != null
                        ? kotlinPropertyMetadata.setterMetadata.referencedMethod
                        : null);
    }

    private void markContextParameters(
            List<KotlinValueParameterMetadata> contextParameters, Method... methods) {
        if (contextParameters == null) {
            return;
        }

        Function<Method, Boolean> isStatic = method -> (method.getAccessFlags() & STATIC) != 0;

        // Mark all the parameters of the given methods
        // at each context receiver index as used.
        IntStream.range(0, contextParameters.size())
                .forEach(
                        paramIndex ->
                                Arrays.stream(methods)
                                        .filter(Objects::nonNull)
                                        .filter(it -> it.getProcessingInfo() instanceof ProgramMethodOptimizationInfo)
                                        .forEachOrdered(
                                                it ->
                                                        markParameterUsed(
                                                                it, isStatic.apply(it) ? paramIndex : paramIndex + 1)));
    }
}
