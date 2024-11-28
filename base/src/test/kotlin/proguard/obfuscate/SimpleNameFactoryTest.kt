/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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
package proguard.obfuscate

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeIn

class SimpleNameFactoryTest : StringSpec({

    val reservedNames =
        listOf(
            "AUX", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "CON", "LPT1", "LPT2",
            "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", "NUL", "PRN",
        )

    "Should not generate WINDOWS reserved names with mixed case enabled" {

        val factory = SimpleNameFactory(true)
        // 115895 is index for generating PRN with mixed case enabled.
        repeat(115896) {
            val name = factory.nextName()
            name.uppercase() shouldNotBeIn reservedNames
        }
    }

    "Should not generate WINDOWS reserved names with mixed case disabled" {

        val factory = SimpleNameFactory(false)
        // 44213 is index for generating prn with mixed case disabled.
        repeat(44214) {
            val name = factory.nextName()
            name.uppercase() shouldNotBeIn reservedNames
        }
    }
})
