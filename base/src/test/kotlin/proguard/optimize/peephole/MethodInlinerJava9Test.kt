package proguard.optimize.peephole

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.ProgramClass
import proguard.classfile.ProgramMethod
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.ClassVisitor
import proguard.classfile.visitor.MultiClassVisitor
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import testutils.RequiresJavaVersion

@RequiresJavaVersion(9)
class MethodInlinerJava9Test : FreeSpec({
    isolationMode = IsolationMode.InstancePerTest

    "Given a method calling a private method in the same interface" - {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Foo.java",
                    """interface Foo {
                default void f1() {
                    f2();
                }
        
                private static void f2() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(System.currentTimeMillis());
                    System.out.println(sb.toString());
                }
            }""",
                ),
            )

        val clazz = programClassPool.getClass("Foo") as ProgramClass
        val method = clazz.findMethod("f1", "()V") as ProgramMethod
        val codeAttr = method.attributes.filterIsInstance<CodeAttribute>()[0]

        val lengthBefore = codeAttr.u4codeLength

        // Initialize optimization info (used when inlining).
        val optimizationInfoInitializer: ClassVisitor =
            MultiClassVisitor(
                ProgramClassOptimizationInfoSetter(),
                AllMethodVisitor(
                    ProgramMemberOptimizationInfoSetter(),
                ),
            )

        programClassPool.classesAccept(optimizationInfoInitializer)

        // Create a mock method inliner which always returns true.
        val methodInliner =
            object : MethodInliner(false, true, true) {
                override fun shouldInline(
                    clazz: Clazz?,
                    method: Method?,
                    codeAttribute: CodeAttribute?,
                ): Boolean = true
            }

        "Then the interface method is inlined" {
            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner,
                    ),
                ),
            )

            val lengthAfter = codeAttr.u4codeLength

            lengthAfter shouldBeGreaterThan lengthBefore
        }
    }
})
