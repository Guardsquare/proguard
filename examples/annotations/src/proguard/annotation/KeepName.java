/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2007 Eric Lafortune (eric@graphics.cornell.edu)
 */
package proguard.annotation;

import java.lang.annotation.*;

/**
 * This annotation specifies not to optimize or obfuscate the annotated class or
 * class member as an entry point.
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface KeepName {}
