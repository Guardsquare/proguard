/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import proguard.Configuration
import proguard.ConfigurationParser
import proguard.ConfigurationWriter
import java.io.PrintWriter
import java.io.StringWriter

fun String.asConfiguration(): Configuration {
    val configuration = Configuration()
    ConfigurationParser(this, "test configuration", null, System.getProperties()).use {
        it.parse(configuration)
    }
    return configuration
}

fun Configuration.asString(): String {
    val out = StringWriter()
    val configuration = Configuration()
    ConfigurationWriter(PrintWriter(out)).use {
        it.write(configuration)
    }
    return out.toString().trim()
}
