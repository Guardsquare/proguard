/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import java.io.File
import java.nio.file.Path

fun File.isJavaFile() = this.name.endsWith(".java")
fun File.isClassFile() = this.name.endsWith(".class")
fun File.isKotlinFile() = this.name.endsWith(".kt") || this.name.endsWith(".kts")
fun Path.isJavaFile() = this.toFile().isJavaFile()
fun Path.isClassFile() = this.toFile().isClassFile()
fun Path.isKotlinFile() = this.toFile().isKotlinFile()
