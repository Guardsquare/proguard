/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import proguard.util.ProcessingFlags.DONT_OBFUSCATE
import proguard.util.ProcessingFlags.DONT_OPTIMIZE
import proguard.util.ProcessingFlags.DONT_PROCESS_KOTLIN_MODULE
import proguard.util.ProcessingFlags.DONT_SHRINK

fun hasFlag(flag: Int) =
    object : Matcher<Int> {
        override fun test(value: Int): MatcherResult =
            MatcherResult(
                (value and flag) != 0,
                { "Flag ${flag.asProcessingFlagString} should be set" },
                { "Flag ${flag.asProcessingFlagString} should not be set" },
            )
    }

infix fun Int.shouldHaveFlag(flag: Int) = this should hasFlag(flag)

infix fun Int.shouldNotHaveFlag(flag: Int) = this shouldNot hasFlag(flag)

val Int.asProcessingFlagString: String
    get() =
        when (this) {
            DONT_OBFUSCATE -> "DONT_OBFUSCATE"
            DONT_SHRINK -> "DONT_SHRINK"
            DONT_OPTIMIZE -> "DONT_OPTIMIZE"
            DONT_PROCESS_KOTLIN_MODULE -> "DONT_PROCESS_KOTLIN_MODULE"
            else -> this.toString()
        }
