/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.SpecExtension
import io.kotest.core.spec.Spec
import proguard.testutils.currentJavaVersion


@Suppress("UNUSED")
object TestConfig : AbstractProjectConfig() {}

class RequiresJavaExtension(private val from: Int, private val to: Int = Int.MAX_VALUE) : SpecExtension {
    override suspend fun intercept(
        spec: Spec,
        execute: suspend (Spec) -> Unit,
    ) {
        if (currentJavaVersion in from..to) {
            execute(spec)
        }
    }
}
