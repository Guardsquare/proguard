/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2007 Eric Lafortune (eric@graphics.cornell.edu)
 */
package proguard.annotation;

import java.lang.annotation.*;

/**
 * This annotation specifies to keep the annotated class as an application,
 * together with its a main method.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface KeepApplication {}
