/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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

package proguard.gradle.plugin.android.dsl

enum class DefaultConfiguration(val filename: String) {

    ANDROID_DEBUG("proguard-android-debug.txt"),
    ANDROID_RELEASE("proguard-android.txt"),
    ANDROID_RELEASE_OPTIMIZE("proguard-android-optimize.txt");

    override fun toString(): String = filename

    companion object {
        fun fromString(string: String): DefaultConfiguration =
                values().find { it.filename == string }
                ?: throw IllegalArgumentException("""
                        The default ProGuard configuration '$string' is not invalid. 
                        
                        Choose from: 
                            ${values().joinToString(separator = ", ")}
                        """.trimIndent())
    }
}
