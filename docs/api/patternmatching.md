## Basic pattern matching

The library has some powerful support to match patterns in bytecode
instruction sequences. You first define the pattern as a sequence of
instructions, with wildcards. For example:

    final int X = InstructionSequenceMatcher.X;
    final int C = InstructionSequenceMatcher.C;

    InstructionSequenceBuilder ____ =
        new InstructionSequenceBuilder();

    Instruction[] pattern =
        ____.iload(X)
            .bipush(C)
            .istore(X).__();

    Constant[] constants = ____.constants();

You can then find that pattern in given code:

    clazz.accept(
        new AllMethodVisitor(
        new AllAttributeVisitor(
        new AllInstructionVisitor(
        new MyMatchPrinter(
        new InstructionSequenceMatcher(constants, pattern))))));

Complete example: ApplyPeepholeOptimizations.java

## Pattern replacement

Instead of just matching instruction sequences, you can also replace matched
sequences by other instruction sequences, for example to optimize code or
instrument code. Say that you want to replace am instruction sequence
"putstatic/getstatic" by an equivalent "dup/putstatic":

    InstructionSequenceBuilder ____ =
        new InstructionSequenceBuilder();

    Instruction[][] replacements =
    {
        ____.putstatic(X)
            .getstatic(X).__(),

        ____.dup()
            .putstatic(X).__()
    };

    Constant[] constants = ____.constants();

    BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

    clazz.accept(
        new AllMethodVisitor(
        new AllAttributeVisitor(
        new PeepholeEditor(branchTargetFinder, codeAttributeEditor,
        new InstructionSequenceReplacer(constants,
                                        replacements,
                                        branchTargetFinder,
                                        codeAttributeEditor)))))

You can define multiple patterns and their respective replacements in one go,
with the wrapper InstructionSequencesReplacer.

Complete example: ApplyPeepholeOptimizations.java
