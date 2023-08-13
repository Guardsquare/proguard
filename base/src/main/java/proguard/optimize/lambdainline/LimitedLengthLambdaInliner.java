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
 * This class is an implementation of the {@link proguard.optimize.lambdainline.BaseLambdaInliner BaseLambdaInliner }
 * that inlines lambdas depending on the length of the lambda implementation method and the length of the consuming
 * method. The sum of the lengths of the lambda implementation and consuming method is used to determine if we will
 * attempt to inline this lambda. We do this because inlining the lambda will result in a new method that has a length
 * close to this sum of these 2 methods.
 */
public class LimitedLengthLambdaInliner extends BaseLambdaInliner {
    private final Logger logger = LogManager.getLogger();
    private static final int MAXIMUM_INLINED_LENGTH = 1000;

    public LimitedLengthLambdaInliner(ClassPool programClassPool, ClassPool libraryClassPool, Clazz consumingClass, Method consumingMethod, int calledLambdaIndex, Lambda lambda) {
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

        boolean inline = lambdaImplMethodLength.get() + consumingMethodLength.get() < MAXIMUM_INLINED_LENGTH;
        if (!inline) {
            logger.info("Will not attempt inlining lambda because methods are too long, maximum consuming method length + lambda implementation method length = {}", MAXIMUM_INLINED_LENGTH);
            logger.info("Consuming method = {}#{}{}", consumingClass.getName(), consumingMethod.getName(consumingClass), consumingMethod.getDescriptor(consumingClass));
            logger.info("Lambda implementation method = {}#{}{}", lambdaClass.getName(), lambdaImplMethod.getName(lambdaClass), lambdaImplMethod.getDescriptor(lambdaClass));
            logger.info("Consuming method length = {}, lambda implementation method length = {}, sum = {}", consumingMethodLength.get(), lambdaImplMethodLength.get(), consumingMethodLength.get() + lambdaImplMethodLength.get());
        }
        return inline;
    }
}
