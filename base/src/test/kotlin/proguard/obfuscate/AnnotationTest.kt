package proguard.obfuscate

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import proguard.classfile.ProgramClass
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import java.util.HashMap

class AnnotationTest : FreeSpec({
    val (programClassPool, _) =
        ClassPoolBuilder.fromSource(
            JavaSource(
                "Test.java",
                """
                public @interface Test {
                    boolean testBoolean1() default false;
                    boolean testBoolean2() default true;
                    String testString1() default "";
                }
                """.trimIndent(),
            ),
        )
    val testClass = programClassPool.getClass("Test") as ProgramClass

    "Annotation members should be excluded from aggressive overloading" {
        val descriptorMap = HashMap<String, Map<String, String>>()

        testClass.methodsAccept(
            MemberObfuscator(
                true,
                SimpleNameFactory(),
                descriptorMap,
            ),
        )

        descriptorMap shouldBeEqual
            mapOf(
                "()" to
                    mapOf(
                        "a" to "testBoolean1",
                        "b" to "testBoolean2",
                        "c" to "testString1",
                    ),
            )
    }
})
