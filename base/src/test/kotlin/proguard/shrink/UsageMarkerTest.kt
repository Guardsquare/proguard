package proguard.shrink

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import proguard.Configuration
import proguard.classfile.ClassPool
import proguard.classfile.Clazz
import proguard.classfile.attribute.Attribute
import proguard.classfile.attribute.PermittedSubclassesAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.attribute.visitor.AttributeVisitor
import proguard.classfile.constant.ClassConstant
import proguard.classfile.constant.Constant
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.visitor.AllMemberVisitor
import proguard.classfile.visitor.MultiClassVisitor
import proguard.resources.file.ResourceFilePool
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import proguard.testutils.RequiresJavaVersion
import proguard.util.ProcessingFlagSetter
import proguard.util.ProcessingFlags.DONT_SHRINK

@RequiresJavaVersion(15)
class Java15UsageMarkerTest : BehaviorSpec({
    Given("A class pool containing a sealed interface extending another sealed interface, and final classes implementing both") {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("sample/Animal.java","""
                package sample;
                public sealed interface Animal permits Fish, Mammal {

                	static Animal ofType(String type) {

                		if(Cat.TYPE.matches(type)){
                			return new Cat();
                		}else if(Dog.TYPE.matches(type)){
                			return new Dog();
                		}else if(Fish.TYPE.matches(type)){
                			return new Fish();
                		}
                		throw new IllegalArgumentException("Wrong animal type: " + type);

                	}
                }
            """.trimIndent()),
            JavaSource("sample/AnimalType.java","""
                package sample;

                public enum AnimalType {

                	CAT("CAT"),
                	DOG("DOG"),
                	FISH("FISH");


                	private final String typeString;

                	AnimalType(String typeString){
                		this.typeString = typeString;
                	}


                	public boolean matches(String typeString) {
                		return this.typeString.equalsIgnoreCase(typeString);
                	}

                }
            """.trimIndent()),
            JavaSource("sample/Mammal.java", """
                package sample;
                public sealed interface Mammal extends Animal permits Cat, Dog {
                }

            """.trimIndent()),
            JavaSource("sample/Cat.java","""
                package sample;
                public final class Cat implements Mammal{

                	public static final AnimalType TYPE = AnimalType.CAT;

                }

            """.trimIndent()),
            JavaSource("sample/Dog.java","""
                package sample;
                public final class Dog implements Mammal{

                	public static final AnimalType TYPE = AnimalType.DOG;

                }
            """.trimIndent()),
            JavaSource("sample/Fish.java","""
                package sample;
                public final class Fish implements Animal{

                	public static final AnimalType TYPE = AnimalType.FISH;

                }
            """.trimIndent()),
            JavaSource("sample/Main.java","""
                package sample;
                public class Main {

                	public static void main(String[] args) {
                		System.out.println("Trying to create animal");
                		Animal animal = Animal.ofType("fish");
                		System.out.println("Successfully created animal");
                	}
                }

            """.trimIndent()
            ), javacArguments = listOf("--enable-preview", "--release", "15"),
        )
        val animal = programClassPool.getClass("sample/Animal")

        val main = programClassPool.getClass("sample/Main")
        main.accept(
            MultiClassVisitor(ProcessingFlagSetter(DONT_SHRINK), AllMemberVisitor(
                ProcessingFlagSetter(DONT_SHRINK)
            )))
        When("marking") {
            val simpleUsageMarker = SimpleUsageMarker()
            UsageMarker(Configuration()).mark(programClassPool,ClassPool(), ResourceFilePool(),simpleUsageMarker)
            Then("The Animal class permitted subclasses constants should all be marked as used") {
                var visited = false
                animal.accept(AllAttributeVisitor(object : AttributeVisitor, ConstantVisitor {
                    override fun visitAnyAttribute(
                        clazz: Clazz,
                        attribute: Attribute
                    ) {
                    }

                    override fun visitPermittedSubclassesAttribute(
                        clazz: Clazz,
                        permittedSubclassesAttribute: PermittedSubclassesAttribute
                    ) {
                        permittedSubclassesAttribute.permittedSubclassConstantsAccept(clazz,this)
                    }

                    override fun visitAnyConstant(
                        clazz: Clazz,
                        constant: Constant
                    ) {
                    }

                    override fun visitClassConstant(
                        clazz: Clazz,
                        classConstant: ClassConstant
                    ) {
                        visited = true
                        simpleUsageMarker.isUsed(classConstant) shouldBe true
                    }
                }))
                visited shouldBe true
            }
        }
    }
})