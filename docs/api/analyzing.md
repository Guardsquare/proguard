## Basic control flow analysis

You can extract a basic control flow graph of the instructions in a method
with the class BranchTargetFinder. The resulting graph is defined at the
instruction level: each instruction is labeled with potential branch targets
and branch origins.

    BranchTargetFinder branchTargetFinder =
        new BranchTargetFinder();

    branchTargetFinder.visitCodeAttribute(clazz, method, codeAttribute);

Complete example: ApplyPeepholeOptimizations.java

## Partial evaluation

You can extract more information from a method, with partial evaluation (often
called abstract evaluation). You can tune the precision and the convergence
speed by injecting different value factories and different invocation units:

- The value factories define the level of detail in representing values like
  integers or reference types. The values can be very generic (any primitive
  integer, a reference to any object) or more precise (the integer 42, or an
  integer between 0 and 5, or a non-null reference to an instance of
  java/lang/String).

- The invocation units define the values returned from retrieved fields and
  invoked methods. The values can again be very generic (any integer) or they
  can also be values that were cached in previous evaluations.

    RangeValueFactory valueFactory =
        new RangeValueFactory(
        new ArrayReferenceValueFactory());

    PartialEvaluator partialEvaluator =
        new PartialEvaluator(                                                           new BasicInvocationUnit(valueFactory),
            false);

    partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

Complete example: EvaluateCode.java
