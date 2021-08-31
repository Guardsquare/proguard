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

import proguard.gradle.ProGuardTask.DEFAULT_CONFIG_RESOURCE_PREFIX

sealed class ProGuardConfiguration(val filename: String) {
    open val path: String = filename
    override fun toString(): String = filename
}

class UserProGuardConfiguration(filename: String) : ProGuardConfiguration(filename)

class DefaultProGuardConfiguration private constructor(filename: String) : ProGuardConfiguration(filename) {

    override val path: String
        get() = "$DEFAULT_CONFIG_RESOURCE_PREFIX/$filename"

    companion object {
        private val ANDROID_DEBUG = DefaultProGuardConfiguration("proguard-android-debug.txt")
        private val ANDROID_RELEASE = DefaultProGuardConfiguration("proguard-android.txt")
        private val ANDROID_RELEASE_OPTIMIZE = DefaultProGuardConfiguration("proguard-android-optimize.txt")

        fun fromString(filename: String): ProGuardConfiguration {
            return when (filename) {
                ANDROID_DEBUG.filename -> ANDROID_DEBUG
                ANDROID_RELEASE.filename -> ANDROID_RELEASE
                ANDROID_RELEASE_OPTIMIZE.filename -> ANDROID_RELEASE_OPTIMIZE
                else -> throw IllegalArgumentException("""
                        The default ProGuard configuration '$filename' is not invalid.

                        Choose from:
                           $ANDROID_DEBUG, $ANDROID_RELEASE, $ANDROID_RELEASE_OPTIMIZE
                        """.trimIndent())
            }
        }
    }
}
