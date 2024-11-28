/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.filter.SpecFilter
import io.kotest.core.filter.SpecFilterResult
import io.kotest.core.filter.SpecFilterResult.Exclude
import io.kotest.core.filter.SpecFilterResult.Include
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Suppress("UNUSED")
object TestConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> {
        return listOf(RequiresJavaVersionAnnotationFilter())
    }
}

class RequiresJavaVersionAnnotationFilter : SpecFilter {
    override fun filter(kclass: KClass<*>): SpecFilterResult =
        if (with(kclass.findAnnotation<RequiresJavaVersion>()) {
                (this == null || (currentJavaVersion >= this.from && currentJavaVersion <= this.to))
            }
        ) {
            Include
        } else {
            Exclude("Required Java version is not in range.")
        }
}

@Target(AnnotationTarget.CLASS)
annotation class RequiresJavaVersion(val from: Int, val to: Int = Int.MAX_VALUE)
