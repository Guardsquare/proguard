/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.lambdainline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.optimize.lambdainline.lambdalocator.Lambda;

import java.util.Optional;

/**
 * This class is an implementation of the {@link proguard.optimize.lambdainline.BaseLambdaInliner BaseLambdaInliner } that
 * inlines lambdas depending on the length of the lambda implementation method and the length of the consuming method.
 * The length of the consuming method is taken into account because the consuming method will be copied when inlining
 * this lambda because one method can take multiple different lambdas as an input.
 */
public class ShortLambdaInliner extends BaseLambdaInliner {
    private final Logger logger = LogManager.getLogger();
    private static final int MAXIMUM_CONSUMING_METHOD_LENGTH = 2000;
    private static final int MAXIMUM_LAMBDA_IMPL_METHOD_LENGTH = 64;

    public ShortLambdaInliner(ClassPool programClassPool, ClassPool libraryClassPool, Clazz consumingClass, Method consumingMethod, int calledLambdaIndex, Lambda lambda) {
        super(programClassPool, libraryClassPool, consumingClass, consumingMethod, calledLambdaIndex, lambda);
    }

    @Override
    protected boolean shouldInline(Clazz consumingClass, Method consumingMethod, Clazz lambdaClass, Method lambdaImplMethod) {
        Optional<Integer> consumingMethodLength = MethodLengthFinder.getMethodCodeLength(consumingClass, consumingMethod);
        Optional<Integer> lambdaImplMethodLength = MethodLengthFinder.getMethodCodeLength(lambdaClass, lambdaImplMethod);

        if (!consumingMethodLength.isPresent()) {
            logger.error("Will not attempt to inline lambda because of error:");
            logger.error("The consuming method of a lambda has to have an implementation. Consuming method = {}#{}{}", consumingClass.getName(), consumingMethod.getName(consumingClass), consumingMethod.getDescriptor(consumingClass));
            return false;
        }
        if (!lambdaImplMethodLength.isPresent()) {
            logger.error("Will not attempt to inline lambda because of error:");
            logger.error("The lambda implementation method has to have an implementation. Lambda implementation method = {}#{}{}", lambdaClass.getName(), lambdaImplMethod.getName(lambdaClass), lambdaImplMethod.getDescriptor(lambdaClass));
            return false;
        }

        boolean inline = lambdaImplMethodLength.get() < MAXIMUM_LAMBDA_IMPL_METHOD_LENGTH && consumingMethodLength.get() < MAXIMUM_CONSUMING_METHOD_LENGTH;
        if (!inline) {
            logger.info("Will not attempt inlining lambda because methods are too long, maximum consuming method length = {}, maximum lambda implementation method length = {}", MAXIMUM_CONSUMING_METHOD_LENGTH, MAXIMUM_LAMBDA_IMPL_METHOD_LENGTH);
            logger.info("Consuming method = {}#{}{}", consumingClass.getName(), consumingMethod.getName(consumingClass), consumingMethod.getDescriptor(consumingClass));
            logger.info("Lambda implementation method = {}#{}{}", lambdaClass.getName(), lambdaImplMethod.getName(lambdaClass), lambdaImplMethod.getDescriptor(lambdaClass));
            logger.info("Consuming method length = {}, lambda implementation method length = {}", consumingMethodLength.get(), lambdaImplMethodLength.get());
        }
        return inline;
    }
}
