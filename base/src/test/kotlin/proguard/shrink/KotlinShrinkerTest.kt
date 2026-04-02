package proguard.shrink

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import io.mockk.verify
import proguard.classfile.ProgramClass
import proguard.classfile.kotlin.KotlinAnnotatable
import proguard.classfile.kotlin.KotlinAnnotation
import proguard.classfile.kotlin.KotlinAnnotationArgument.StringValue
import proguard.classfile.kotlin.KotlinEnumEntryMetadata
import proguard.classfile.kotlin.KotlinFunctionMetadata
import proguard.classfile.kotlin.KotlinTypeMetadata
import proguard.classfile.kotlin.KotlinValueParameterMetadata
import proguard.classfile.kotlin.visitor.AllEnumEntryVisitor
import proguard.classfile.kotlin.visitor.AllFunctionVisitor
import proguard.classfile.kotlin.visitor.AllKotlinAnnotationArgumentVisitor
import proguard.classfile.kotlin.visitor.AllKotlinAnnotationVisitor
import proguard.classfile.kotlin.visitor.AllPropertyVisitor
import proguard.classfile.kotlin.visitor.AllTypeVisitor
import proguard.classfile.kotlin.visitor.KotlinAnnotationArgumentVisitor
import proguard.classfile.kotlin.visitor.KotlinAnnotationVisitor
import proguard.classfile.kotlin.visitor.KotlinClassVisitor
import proguard.classfile.kotlin.visitor.MultiKotlinAnnotationVisitor
import proguard.classfile.kotlin.visitor.MultiKotlinMetadataVisitor
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.KotlinSource

class KotlinShrinkerTest : BehaviorSpec({
    Given("a Kotlin class with a mutable property") {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "TestClass.kt",
                """
                class TestClass {
                    var myProperty: Int
                        get() = 5
                        set(value) {
                            println(value)
                        }
                }
                """.trimIndent(),
            ),
        )

        val clazz = programClassPool.getClass("TestClass")

        When("the property is marked as used and shrinker is applied") {
            val usageMarker = SimpleUsageMarker()
            usageMarker.markAsUsed(programClassPool.getClass("TestClass"))
            clazz.kotlinMetadataAccept(
                AllPropertyVisitor { clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata ->
                    usageMarker.markAsUsed(kotlinPropertyMetadata)
                },
            )
            clazz.kotlinMetadataAccept(KotlinShrinker(usageMarker))

            Then("the property's setter parameters should be empty") {
                var visited = false
                clazz.kotlinMetadataAccept(
                    AllPropertyVisitor { clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata ->
                        visited = true
                        kotlinPropertyMetadata.setterParameter.shouldBeNull()
                        kotlinPropertyMetadata.setterParameters.shouldBeEmpty()
                    },
                )
                visited shouldBe true
            }
        }
    }

    Given("A property with annotated type") {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
            @Target(AnnotationTarget.TYPE)
            annotation class MyTypeAnnotation(val annotationArgument : String)

            val x: @MyTypeAnnotation(annotationArgument = "annotated") String = "foo"
                """.trimIndent(),
            ),
            kotlincArguments = listOf("-Xannotations-in-metadata"),
        )

        val fileFacadeClass = programClassPool.getClass("TestKt")
        val usageMarker = SimpleUsageMarker()

        // Mark the property metadata as used so it does not get removed.
        fileFacadeClass.accept(
            ReferencedKotlinMetadataVisitor(
                AllPropertyVisitor { _, _, kotlinPropertyMetadata ->
                    usageMarker.markAsUsed(
                        kotlinPropertyMetadata,
                    )
                },
            ),
        )

        When("the type annotation is used but its argument is not") {
            fileFacadeClass.kotlinMetadataAccept(
                AllKotlinAnnotationVisitor { _, _, annotation -> usageMarker.markAsUsed(annotation) },
            )
            And("the KotlinShrinker is run") {
                fileFacadeClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(usageMarker),
                    ),
                )

                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()

                fileFacadeClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        AllTypeVisitor(
                            AllKotlinAnnotationVisitor(
                                MultiKotlinAnnotationVisitor(
                                    annotationVisitor,
                                    AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                                ),
                            ),
                        ),
                    ),
                )

                Then("the type annotation is not removed") {
                    verify(exactly = 1) {
                        annotationVisitor.visitAnyAnnotation(
                            fileFacadeClass,
                            ofType<KotlinTypeMetadata>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }

                Then("the type annotation argument is removed") {
                    verify(exactly = 0) {
                        annotationArgVisitor.visitStringArgument(
                            fileFacadeClass,
                            ofType<KotlinAnnotatable>(),
                            ofType<KotlinAnnotation>(),
                            withArg { it.name shouldBe "annotationArgument" },
                            StringValue("annotated"),
                        )
                    }
                }
            }
        }

        When("the type annotation is not used") {
            And("the KotlinShrinker runs") {
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()
                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()

                fileFacadeClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(SimpleUsageMarker()),
                    ),
                )

                Then("the type annotation is shrunk entirely") {
                    fileFacadeClass.accept(
                        ReferencedKotlinMetadataVisitor(
                            AllTypeVisitor(
                                AllKotlinAnnotationVisitor(
                                    MultiKotlinAnnotationVisitor(
                                        annotationVisitor,
                                        AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                                    ),
                                ),
                            ),
                        ),
                    )

                    verify(exactly = 0) {
                        annotationVisitor.visitAnyAnnotation(
                            fileFacadeClass,
                            ofType<KotlinTypeMetadata>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }
            }
        }
    }

    Given("An annotated function") {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
            @Target(AnnotationTarget.FUNCTION)
            annotation class MyFunctionAnnotation(val annotationArgument: String)

            @MyFunctionAnnotation(
                annotationArgument = "annotated") fun foo(): String { return "Foo" }
                """.trimIndent(),
            ),
            kotlincArguments = listOf("-Xannotations-in-metadata"),
        )

        val fileFacadeClass = programClassPool.getClass("TestKt")
        val usageMarker = SimpleUsageMarker()

        // Mark the function metadata as used so it does not get removed.
        fileFacadeClass.accept(
            ReferencedKotlinMetadataVisitor(
                AllFunctionVisitor(
                    { _, _, kotlinFunctionMetadata -> usageMarker.markAsUsed(kotlinFunctionMetadata) },
                ),
            ),
        )

        When("the function annotation is used but its argument is not") {
            fileFacadeClass.kotlinMetadataAccept(
                AllKotlinAnnotationVisitor { _, _, annotation -> usageMarker.markAsUsed(annotation) },
            )
            And("the KotlinShrinker is run") {
                fileFacadeClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(usageMarker),
                    ),
                )

                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()

                fileFacadeClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        AllKotlinAnnotationVisitor(
                            MultiKotlinAnnotationVisitor(
                                annotationVisitor,
                                AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                            ),
                        ),
                    ),
                )

                Then("the function annotation is not removed") {
                    verify(exactly = 1) {
                        annotationVisitor.visitAnyAnnotation(
                            fileFacadeClass,
                            ofType<KotlinFunctionMetadata>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }

                Then("the function annotation argument is removed") {
                    verify(exactly = 0) {
                        annotationArgVisitor.visitStringArgument(
                            fileFacadeClass,
                            ofType<KotlinAnnotatable>(),
                            ofType<KotlinAnnotation>(),
                            withArg { it.name shouldBe "annotationArgument" },
                            StringValue("annotated"),
                        )
                    }
                }
            }
        }

        When("the function annotation is not used") {
            And("the KotlinShrinker runs") {
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()
                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()

                fileFacadeClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(SimpleUsageMarker()),
                    ),
                )

                Then("the function annotation is shrunk entirely") {
                    fileFacadeClass.accept(
                        ReferencedKotlinMetadataVisitor(
                            AllTypeVisitor(
                                AllKotlinAnnotationVisitor(
                                    MultiKotlinAnnotationVisitor(
                                        annotationVisitor,
                                        AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                                    ),
                                ),
                            ),
                        ),
                    )

                    verify(exactly = 0) {
                        annotationVisitor.visitAnyAnnotation(
                            fileFacadeClass,
                            ofType<KotlinTypeMetadata>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }
            }
        }
    }

    Given("Annotated value parameters") {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                @Target(AnnotationTarget.VALUE_PARAMETER)
                annotation class MyValueParameterAnnotation(val annotationArgument: String)
                
                fun foo(
                    @MyValueParameterAnnotation(annotationArgument = "function")
                    valueParam : String = "default" ) : String { return valueParam }

                class Bar(
                    @MyValueParameterAnnotation(annotationArgument = "constructor")
                    valueParam : String = "default")
                        
                var myProperty = "initialized"
                    set(@MyValueParameterAnnotation(annotationArgument = "property") valueParam)
                        { field = valueParam + "concat" }
                """.trimIndent(),
            ),
            kotlincArguments = listOf("-Xannotations-in-metadata"),
        )

        val usageMarker = SimpleUsageMarker()

        // Mark the metadata as used so it does not get removed.
        programClassPool.classesAccept(
            ReferencedKotlinMetadataVisitor(
                MultiKotlinMetadataVisitor(
                    KotlinClassVisitor { _, kotlinClassKindMetadata ->
                        // Don't shrink the class kind metadata
                        usageMarker.markAsUsed(kotlinClassKindMetadata)
                        // Don't shrink the constructor metadata
                        kotlinClassKindMetadata.constructors.forEach { constructorMetadata ->
                            usageMarker.markAsUsed(constructorMetadata)
                            // Don't shrink the value parameter metadata
                            constructorMetadata.valueParameters.forEach {
                                usageMarker.markAsUsed(it)
                            }
                        }
                    },
                    AllFunctionVisitor(
                        { _, _, kotlinFunctionMetadata ->
                            usageMarker.markAsUsed(kotlinFunctionMetadata)
                            kotlinFunctionMetadata.valueParameters.forEach { metadata -> usageMarker.markAsUsed(metadata) }
                        },
                    ),
                    AllPropertyVisitor { _, _, kotlinPropertyMetadata ->
                        if (kotlinPropertyMetadata.setterMetadata != null) {
                            // Don't shrink the property
                            usageMarker.markAsUsed(kotlinPropertyMetadata)
                            // Don't shrink the setter
                            usageMarker.markAsUsed(kotlinPropertyMetadata.setterMetadata)
                            usageMarker.markAsUsed(kotlinPropertyMetadata.setterParameter)
                            usageMarker.markAsUsed(kotlinPropertyMetadata.setterMetadata!!.referencedMethod)
                        }
                    },
                ),
            ),
        )

        When("the annotation metadata is used but its argument is not") {
            programClassPool.classesAccept(
                ReferencedKotlinMetadataVisitor(
                    AllKotlinAnnotationVisitor { _, _, annotation ->
                        usageMarker.markAsUsed(annotation)
                    },
                ),
            )

            And("the KotlinShrinker is run") {
                programClassPool.classesAccept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(usageMarker),
                    ),
                )

                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()

                programClassPool.classesAccept(
                    ReferencedKotlinMetadataVisitor(
                        AllKotlinAnnotationVisitor(
                            MultiKotlinAnnotationVisitor(
                                annotationVisitor,
                                AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                            ),
                        ),
                    ),
                )

                Then("the annotations are not removed") {
                    verify(exactly = 3) {
                        annotationVisitor.visitAnyAnnotation(
                            ofType<ProgramClass>(),
                            ofType<KotlinValueParameterMetadata>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }

                Then("the function annotation argument is removed") {
                    verify(exactly = 0) {
                        annotationArgVisitor.visitStringArgument(
                            ofType<ProgramClass>(),
                            ofType<KotlinAnnotatable>(),
                            ofType<KotlinAnnotation>(),
                            withArg { it.name shouldBe "annotationArgument" },
                            ofType<StringValue>(),
                        )
                    }
                }
            }
        }

        When("the function annotation is not used") {
            And("the KotlinShrinker runs") {
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()
                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()

                programClassPool.classesAccept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(SimpleUsageMarker()),
                    ),
                )

                Then("the function annotation is shrunk entirely") {
                    programClassPool.classesAccept(
                        ReferencedKotlinMetadataVisitor(
                            AllTypeVisitor(
                                AllKotlinAnnotationVisitor(
                                    MultiKotlinAnnotationVisitor(
                                        annotationVisitor,
                                        AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                                    ),
                                ),
                            ),
                        ),
                    )

                    verify(exactly = 0) {
                        annotationVisitor.visitAnyAnnotation(
                            ofType<ProgramClass>(),
                            ofType<KotlinAnnotatable>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }
            }
        }
    }

    Given("An annotated enum value") {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                @Target(AnnotationTarget.FIELD)
                annotation class MyEnumEntryAnnotation(val string: String)

                enum class MyEnumClass {
                    @MyEnumEntryAnnotation(string = "foo")
                    MY_ENUM_ENTRY
                }
                """.trimIndent(),
            ),
            kotlincArguments = listOf("-Xannotations-in-metadata"),
        )

        val myEnumClass = programClassPool.getClass("MyEnumClass")
        val usageMarker = SimpleUsageMarker()

        // Mark the function metadata as used so it does not get removed.
        myEnumClass.accept(
            ReferencedKotlinMetadataVisitor(
                MultiKotlinMetadataVisitor(
                    KotlinClassVisitor { _, classKindMetadata,
                        ->
                        usageMarker.markAsUsed(classKindMetadata)
                    },
                    AllEnumEntryVisitor { _, _, kotlinEnumEntryMetadata ->
                        usageMarker.markAsUsed(kotlinEnumEntryMetadata.referencedEnumEntry)
                        usageMarker.markAsUsed(kotlinEnumEntryMetadata)
                    },
                ),
            ),
        )

        When("the annotation metadata is used but its argument is not") {
            myEnumClass.kotlinMetadataAccept(
                AllKotlinAnnotationVisitor { _, _, annotation -> usageMarker.markAsUsed(annotation) },
            )
            And("the KotlinShrinker is run") {
                myEnumClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(usageMarker),
                    ),
                )

                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()

                myEnumClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        AllKotlinAnnotationVisitor(
                            MultiKotlinAnnotationVisitor(
                                annotationVisitor,
                                AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                            ),
                        ),
                    ),
                )

                Then("the annotation metadata is not removed") {
                    verify(exactly = 1) {
                        annotationVisitor.visitAnyAnnotation(
                            myEnumClass,
                            ofType<KotlinEnumEntryMetadata>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }

                Then("the function annotation argument is removed") {
                    verify(exactly = 0) {
                        annotationArgVisitor.visitStringArgument(
                            myEnumClass,
                            ofType<KotlinAnnotatable>(),
                            ofType<KotlinAnnotation>(),
                            withArg { it.name shouldBe "annotationArgument" },
                            StringValue("annotated"),
                        )
                    }
                }
            }
        }

        When("the annotation metadata is not used") {
            And("the KotlinShrinker runs") {
                val annotationVisitor = spyk<KotlinAnnotationVisitor>()
                val annotationArgVisitor = spyk<KotlinAnnotationArgumentVisitor>()

                myEnumClass.accept(
                    ReferencedKotlinMetadataVisitor(
                        KotlinShrinker(SimpleUsageMarker()),
                    ),
                )

                Then("the annotation metadata is shrunk entirely") {
                    myEnumClass.accept(
                        ReferencedKotlinMetadataVisitor(
                            AllTypeVisitor(
                                AllKotlinAnnotationVisitor(
                                    MultiKotlinAnnotationVisitor(
                                        annotationVisitor,
                                        AllKotlinAnnotationArgumentVisitor(annotationArgVisitor),
                                    ),
                                ),
                            ),
                        ),
                    )

                    verify(exactly = 0) {
                        annotationVisitor.visitAnyAnnotation(
                            myEnumClass,
                            ofType<KotlinAnnotatable>(),
                            ofType<KotlinAnnotation>(),
                        )
                    }
                }
            }
        }
    }
})
