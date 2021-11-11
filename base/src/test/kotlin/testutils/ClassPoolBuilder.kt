/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import com.tschuchort.compiletesting.KotlinCompilation
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.AutoScan
import io.kotest.core.test.TestCase
import proguard.JbcReader
import proguard.classfile.ClassPool
import proguard.classfile.Clazz
import proguard.classfile.attribute.Attribute.RUNTIME_VISIBLE_ANNOTATIONS
import proguard.classfile.attribute.annotation.visitor.AllAnnotationVisitor
import proguard.classfile.attribute.annotation.visitor.AnnotationTypeFilter
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.attribute.visitor.AttributeNameFilter
import proguard.classfile.kotlin.KotlinConstants.TYPE_KOTLIN_METADATA
import proguard.classfile.util.ClassReferenceInitializer
import proguard.classfile.util.ClassSubHierarchyInitializer
import proguard.classfile.util.ClassSuperHierarchyInitializer
import proguard.classfile.util.WarningPrinter
import proguard.classfile.util.kotlin.KotlinMetadataInitializer
import proguard.classfile.visitor.ClassPoolFiller
import proguard.io.ClassFilter
import proguard.io.ClassReader
import proguard.io.DataEntryReader
import proguard.io.FileDataEntry
import proguard.io.JarReader
import proguard.io.NameFilteredDataEntryReader
import proguard.io.StreamingDataEntry
import java.io.ByteArrayInputStream
import java.io.File
import java.io.OutputStream
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Files.createTempDirectory
import java.util.Objects
import kotlin.reflect.KProperty

data class ClassPools(val programClassPool: ClassPool, val libraryClassPool: ClassPool)

class ClassPoolBuilder private constructor() {

    companion object {
        private val compiler = KotlinCompilation()
        private val libraryClassPool by LibraryClassPoolBuilder(compiler)

        fun fromClasses(vararg clazz: Clazz): ClassPool {
            return ClassPool().apply {
                clazz.forEach(::addClass)
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun fromDirectory(dir: File): ClassPools =
            fromSource(
                *Files.walk(dir.toPath())
                    .filter { it.isJavaFile() || it.isKotlinFile() }
                    .map { TestSource.fromFile(it.toFile()) }
                    .toArray() as Array<out TestSource>
            )

        fun fromFiles(vararg file: File): ClassPools =
            fromSource(*file.map { TestSource.fromFile(it) }.toTypedArray())

        fun fromSource(
            vararg source: TestSource,
            javacArguments: List<String> = emptyList(),
            kotlincArguments: List<String> = emptyList(),
            jdkHome: File = getCurrentJavaHome()
        ): ClassPools {

            compiler.apply {
                this.sources = source.filterNot { it is AssemblerSource }.map { it.asSourceFile() }
                this.inheritClassPath = false
                this.workingDir = createTempDirectory("ClassPoolBuilder").toFile()
                this.javacArguments = javacArguments.toMutableList()
                this.kotlincArguments = kotlincArguments
                this.verbose = false
                this.jdkHome = jdkHome
            }

            val result = compiler.compile()

            val programClassPool = ClassPool()

            val classReader: DataEntryReader = NameFilteredDataEntryReader(
                "**.jbc",
                JbcReader(ClassPoolFiller(programClassPool)),
                ClassReader(
                    false,
                    false,
                    false,
                    true,
                    WarningPrinter(PrintWriter(System.err)),
                    ClassPoolFiller(programClassPool)
                )
            )

            result.compiledClassAndResourceFiles.filter { it.isClassFile() }.forEach {
                classReader.read(FileDataEntry(it))
            }

            source.filterIsInstance<AssemblerSource>().forEach {
                classReader.read(StreamingDataEntry(it.filename, it.getInputStream()))
            }

            val classReferenceInitializer = ClassReferenceInitializer(programClassPool, libraryClassPool)
            val classSuperHierarchyInitializer = ClassSuperHierarchyInitializer(programClassPool, libraryClassPool)
            val classSubHierarchyInitializer = ClassSubHierarchyInitializer()

            programClassPool.classesAccept(classSuperHierarchyInitializer)
            libraryClassPool.classesAccept(classSuperHierarchyInitializer)

            if (source.count { it is KotlinSource } > 0) initializeKotlinMetadata(programClassPool)

            programClassPool.classesAccept(classReferenceInitializer)
            libraryClassPool.classesAccept(classReferenceInitializer)

            programClassPool.accept(classSubHierarchyInitializer)
            libraryClassPool.accept(classSubHierarchyInitializer)

            compiler.workingDir.deleteRecursively()

            return ClassPools(programClassPool, libraryClassPool)
        }
    }
}

private fun initializeKotlinMetadata(classPool: ClassPool) {
    val kotlinMetadataInitializer =
        AllAttributeVisitor(
            AttributeNameFilter(
                RUNTIME_VISIBLE_ANNOTATIONS,
                AllAnnotationVisitor(
                    AnnotationTypeFilter(
                        TYPE_KOTLIN_METADATA,
                        KotlinMetadataInitializer(
                            WarningPrinter(
                                PrintWriter(object : OutputStream() {
                                    override fun write(b: Int) { }
                                })
                            )
                        )
                    )
                )
            )
        )

    classPool.classesAccept(kotlinMetadataInitializer)
}

private fun AssemblerSource.getInputStream() =
    ByteArrayInputStream(this.contents.toByteArray(StandardCharsets.UTF_8))

private class LibraryClassPoolBuilder(private val compiler: KotlinCompilation) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): ClassPool {
        val key = Objects.hash(compiler.jdkHome, compiler.kotlinStdLibJdkJar, compiler.kotlinStdLibJdkJar)

        if (!libraryClassPools.containsKey(key)) {
            val libraryClassPool = getJavaRuntimeClassPool(compiler.jdkHome)
            compiler.kotlinStdLibJar?.let { libraryClassPool.fromFile(it) }
            compiler.kotlinReflectJar?.let { libraryClassPool.fromFile(it) }
            libraryClassPools[key] = libraryClassPool
        }

        return libraryClassPools[key]!!
    }

    private fun getJavaRuntimeClassPool(jdkHome: File?): ClassPool {
        val libraryClassPool = ClassPool()

        if (jdkHome != null && jdkHome.exists()) {
            if (jdkHome.resolve("jmods").exists()) {
                jdkHome.resolve("jmods").listFiles().filter {
                    it.name == "java.base.jmod"
                }.forEach {
                    libraryClassPool.fromFile(it)
                }
            } else {
                val runtimeJar = if (jdkHome.resolve("jre").exists()) {
                    jdkHome.resolve("jre").resolve("lib").resolve("rt.jar")
                } else {
                    jdkHome.resolve("lib").resolve("rt.jar")
                }

                libraryClassPool.fromFile(runtimeJar)
            }
        }

        return libraryClassPool
    }

    private fun ClassPool.fromFile(file: File) {
        val classReader: DataEntryReader = ClassReader(
            true,
            false,
            false,
            true,
            null,
            ClassPoolFiller(this)
        )

        JarReader(ClassFilter(classReader)).read(FileDataEntry(file))
    }

    companion object {

        private val libraryClassPools: MutableMap<Int, ClassPool> = mutableMapOf()

        @AutoScan
        object LibraryClassPoolProcessingInfoCleaningListener : TestListener {
            override suspend fun beforeTest(testCase: TestCase) {
                libraryClassPools.values.forEach {
                    it.classesAccept { clazz ->
                        clazz.processingInfo = null
                        clazz.processingFlags = 0
                    }
                }
            }
        }
    }
}
