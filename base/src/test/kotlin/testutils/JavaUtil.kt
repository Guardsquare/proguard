/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import java.io.File
import javax.lang.model.SourceVersion

val currentJavaVersion: Int by lazy {
    var version = System.getProperty("java.version")

    // Strip early access suffix
    if (version.endsWith("-ea")) {
        version = version.substring(0, version.length - 3)
    }

    if (version.startsWith("1.")) {
        version = version.substring(2, 3)
    } else {
        val dot = version.indexOf(".")
        if (dot != -1) {
            version = version.substring(0, dot)
        }
    }

    return@lazy version.toInt()
}

fun isJava9OrLater(): Boolean = SourceVersion.latestSupported() > SourceVersion.RELEASE_8

fun getCurrentJavaHome(): File =
    if (isJava9OrLater()) {
        File(System.getProperty("java.home"))
    } else {
        File(System.getProperty("java.home")).parentFile
    }
