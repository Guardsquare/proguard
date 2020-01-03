The easiest way to create a new class from scratch is with ClassBuilder. It
provides a fluent API to add fields and methods. For example, to create a
class that prints out "Hello, world!":

    ProgramClass programClass =
        new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            "HelloWorld",
            ClassConstants.NAME_JAVA_LANG_OBJECT)

            .addMethod(
                AccessConstants.PUBLIC |
                AccessConstants.STATIC,
                "main",
                "([Ljava/lang/String;)V",
                50,

                code -> code
                    .getstatic("java/lang/System", "out", "Ljava/io/PrintStream;")
                    .ldc("Hello, world!")
                    .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
                    .return_())

            .getProgramClass();

You can also use it to add fields and methods to an existing class:

    ProgramClass programClass =
        new ClassBuilder(existingClass)
            .....

Complete example: CreateHelloWorldClass.java
