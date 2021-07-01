/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import java.io.File

sealed class TestSource {
    companion object {
        fun fromFile(file: File): TestSource {
            return when {
                file.isJavaFile() -> JavaSource(file.name, file.readText())
                file.isKotlinFile() -> KotlinSource(file.name, file.readText())
                else -> FileSource(file)
            }
        }
    }

    abstract fun asSourceFile(): SourceFile
}

class JavaSource(val filename: String, @Language("Java") val contents: String) : TestSource() {
    override fun asSourceFile() = SourceFile.java(filename, contents)
}

class KotlinSource(val filename: String, @Language("Kotlin") val contents: String) : TestSource() {
    override fun asSourceFile() = SourceFile.kotlin(filename, contents)
}

class FileSource(val file: File) : TestSource() {
    override fun asSourceFile(): SourceFile = SourceFile.fromPath(file)
}
