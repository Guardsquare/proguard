/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.gradle

import com.github.zafarkhaja.semver.Version
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class AgpVersionParserTest : FreeSpec({
    "Given a set of AGP versions" - {
        val agpVersions =
            listOf(
                Pair(4, "4.0.0-alpha01"), Pair(4, "4.1.0-beta01"), Pair(4, "4.2.0-rc01"),
                Pair(4, "4.0.0"), Pair(7, "7.1.1"),
            )
        "When semver library is used to parse those versions" - {
            agpVersions.forEach {
                val result = Version.valueOf(it.second)
                "Then resulting majorVersion equals to the given input" {
                    result.majorVersion shouldBe it.first
                }
            }
        }
    }
})
