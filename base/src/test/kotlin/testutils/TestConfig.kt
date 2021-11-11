/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.DiscoveryExtension
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Suppress("UNUSED")
object TestConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> {
        return listOf(RequiresJavaVersionAnnotationFilter())
    }
}

class RequiresJavaVersionAnnotationFilter : DiscoveryExtension {
    override fun afterScan(classes: List<KClass<out Spec>>): List<KClass<out Spec>> =
        classes.filter {
            with(it.findAnnotation<RequiresJavaVersion>()) {
                (this == null || (currentJavaVersion >= this.from && currentJavaVersion <= this.to))
            }
        }
}

@Target(AnnotationTarget.CLASS)
annotation class RequiresJavaVersion(val from: Int, val to: Int = Int.MAX_VALUE)
