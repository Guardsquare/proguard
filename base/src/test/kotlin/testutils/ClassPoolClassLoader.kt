package testutils

import proguard.classfile.ClassPool
import proguard.classfile.ProgramClass
import proguard.classfile.io.ProgramClassWriter
import proguard.classfile.util.ClassUtil.internalClassName
import proguard.classfile.visitor.ProgramClassFilter
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.PrintStream

/**
 * A ClassLoader that can load classes from a ProGuardCORE ClassPool.
 * Copyright: https://github.com/mrjameshamilton/klox/blob/master/src/main/kotlin/eu/jameshamilton/klox/util/ClassPoolClassLoader.kt
 */
class ClassPoolClassLoader(private val classPool: ClassPool) : ClassLoader() {
    override fun findClass(name: String): Class<*> {
        val clazz = classPool.getClass(internalClassName(name))
        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            clazz.accept(ProgramClassFilter(ProgramClassWriter(DataOutputStream(byteArrayOutputStream))))
        } catch (e: Exception) {
            println("Exception for class $name: $e")
            throw e
        }
        val bytes = byteArrayOutputStream.toByteArray()
        if (clazz != null) return defineClass(name, bytes, 0, bytes.size)
        return super.findClass(name)
    }
}


fun captureExecutionOutput(programClassPool: ClassPool, programClassName: String, operation: (Class<*>) -> Unit): String {
    val loader = ClassPoolClassLoader(programClassPool)
    val loadedClass = loader.loadClass(programClassName)
    val stdOutput = System.out
    val capturedOutput = ByteArrayOutputStream()
    val capturedOutputStream = PrintStream(capturedOutput)
    System.setOut(capturedOutputStream)
    try {
        operation(loadedClass)
    } catch (e: Exception) {
        System.setOut(stdOutput)
        println()
        println("Exception: $e")
        println("Output of method call:")
        println(capturedOutput)
    }
    System.setOut(stdOutput)
    return capturedOutput.toString()
}