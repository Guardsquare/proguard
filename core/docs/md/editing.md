## Editing classes

You can edit existing classes with ClassEditor and related editors like
InterfacesEditor, AttributesEditor, and ConstantPoolEditor.

    ClassEditor classEditor =
        new ClassEditor(targetClass);

    classEditor.addField(field);
    
    classEditor.addMethod(method);

If you want to create and add new fields or methods from scratch, you can use
the more convenient ClassBuilder:

    ProgramClass programClass =
        new ClassBuilder(existingClass)
            .addField(
                AccessConstants.PUBLIC |
                AccessConstants.STATIC,
                "someField",
                TypeConstants.INT);

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

## Editing code

Perhaps more interestingly, you can edit the bytecode of method bodies with
CodeAttributeEditor.

    CodeAttributeEditor codeAttributeEditor =
        new CodeAttributeEditor();

    InstructionSequenceBuilder builder =
        new InstructionSequenceBuilder(targetClass);

    Instructions[] replacementInstructions = builder
        .getstatic("java/lang/System", "out", "Ljava/io/
        .ldc("Hello")
        .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V"        .instructions();

    // Prepare the editor for this code.
    codeAttributeEditor.reset(codeAttribute.u4codeLength);

    // Insert the instruction sequence before a specified offset.
    codeAttributeEditor.insertBeforeOffset(offset, replacementInstructions);

    // Apply the changes.
    codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
