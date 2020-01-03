## Analyzing all instructions

If you want to analyze bytecode, you'll probably want to visit specified
instructions of specified code attributes of specified methods of specified
classes. The visitor classes and filters quickly get you to the right place:

    programClassPool.classesAccept(
        new AllMethodVisitor(
        new AllAttributeVisitor(
        new AllInstructionVisitor(
	new MyInstructionAnalyzer()))));

You then only need to implement the methods to visit and analyze the
instructions:

    class      MyInstructionAnalyzer
    implements InstructionVisitor
    {
        public void visitSimpleInstruction(Clazz clazz, .....) ...
        public void visitVariableInstruction(Clazz clazz, .....) ...
        public void visitConstantInstruction(Clazz clazz, .....) ...
        public void visitBranchInstruction(Clazz clazz, .....) ...
        public void visitTableSwitchInstruction(Clazz clazz, .....) ...
        public void visitLookUpSwitchInstruction(Clazz clazz, .....) ...
    }

The library already provides classes to analyze the code for you, finding
branching information, performing partial evaluation, finding the control flow
and data flow, etc, as introduced in the following sections.

Complete example: EvaluateCode.java

## Collecting basic branching information

You can extract basic information about branches in a method with the class
BranchTargetFinder. The results are defined at the instruction level: each
instruction is properly labeled as a branch target, branch origin, exception
handler, etc.

    BranchTargetFinder branchTargetFinder =
        new BranchTargetFinder();

    branchTargetFinder.visitCodeAttribute(clazz, method, codeAttribute);

    if (branchTargetFinder.isBranchOrigin(offset)) ...

    if (branchTargetFinder.isBranchTarget(offset)) ...

Complete example: ApplyPeepholeOptimizations.java

## Basic control flow analysis

You can extract a basic control flow graph of the instructions in a method
with partial evaluation (often called abstract evaluation). The resulting
graph is still defined at the instruction level: each instruction is labeled
with potential branch targets and branch origins.

    ValueFactory valueFactory =
        new BasicValueFactory();

    PartialEvaluator partialEvaluator =
        new PartialEvaluator(                                                           new BasicInvocationUnit(valueFactory),
            false);

    partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

    InstructionOffsetValue branchOrigins = partialEvaluator.branchOrigins(offset));
    InstructionOffsetValue branchTargets = partialEvaluator.branchTargets(offset));

Complete example: VisualizeControlFlow.java

## Partial evaluation

You can extract more information from a method, by tuning the precision of the
partial evaluation with different value factories and different invocation
units:

- The value factories define the level of detail in representing values like
  integers or reference types. The values can be very generic (any primitive
  integer, a reference to any object) or more precise (the integer 42, or an
  integer between 0 and 5, or a non-null reference to an instance of
  java\/lang\/String).

- The invocation units define the values returned from retrieved fields and
  invoked methods. The values can again be very generic (any integer) or they
  can also be values that were cached in prior evaluations of the code base.

    ValueFactory valueFactory =
        new RangeValueFactory(
        new ArrayReferenceValueFactory());

    PartialEvaluator partialEvaluator =
        new PartialEvaluator(                                                           new BasicInvocationUnit(valueFactory),
            false);

    partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

    TracedStack stack = partialEvaluator.getStackAfter(offset);
    Value       value = stack.getTop(index);

Complete example: EvaluateCode.java
