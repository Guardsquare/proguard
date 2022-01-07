package proguard.optimize;

import proguard.Configuration;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.instruction.visitor.InstructionConstantVisitor;
import proguard.classfile.instruction.visitor.InstructionCounter;
import proguard.classfile.instruction.visitor.MultiInstructionVisitor;
import proguard.classfile.util.MethodLinker;
import proguard.classfile.visitor.*;
import proguard.optimize.info.*;
import proguard.optimize.peephole.MethodInliner;
import proguard.optimize.peephole.SameClassMethodInliner;
import proguard.util.*;

public class MethodInlinerWrapper implements ClassPoolVisitor {

    private final Configuration configuration;
    private static final String METHOD_MARKING_STATIC                = "method/marking/static";
    private static final String METHOD_REMOVAL_PARAMETER             = "method/removal/parameter";

    public MethodInlinerWrapper(final Configuration configuration,
                                final ClassPool programClassPool,
                                final ClassPool libraryClassPool)
    {
        this.configuration = configuration;
        markClasses(programClassPool, libraryClassPool);
    }

    private void markClasses(final ClassPool programClassPool,
                             final ClassPool libraryClassPool)
    {

        // Create a matcher for filtering optimizations.
        StringMatcher filter = configuration.optimizations != null ?
                new ListParser(new NameParser()).parse(configuration.optimizations) :
                new ConstantMatcher(true);

        boolean methodMarkingStatic               = filter.matches(METHOD_MARKING_STATIC);
        boolean methodRemovalParameter            = filter.matches(METHOD_REMOVAL_PARAMETER);

        // Clean up any old processing info.
        programClassPool.classesAccept(new ClassCleaner());
        libraryClassPool.classesAccept(new ClassCleaner());

        // Link all methods that should get the same optimization info.
        programClassPool.classesAccept(new BottomClassFilter(
                new MethodLinker()));
        libraryClassPool.classesAccept(new BottomClassFilter(
                new MethodLinker()));

        // Create a visitor for marking the seeds.
        final KeepMarker keepMarker = new KeepMarker();

        // All library classes and library class members remain unchanged.
        libraryClassPool.classesAccept(keepMarker);
        libraryClassPool.classesAccept(new AllMemberVisitor(keepMarker));

        // Mark classes that have the DONT_OPTIMIZE flag set.
        programClassPool.classesAccept(
                new MultiClassVisitor(
                        new ClassProcessingFlagFilter(ProcessingFlags.DONT_OPTIMIZE, 0,
                                keepMarker),

                        new AllMemberVisitor(
                                new MultiMemberVisitor(
                                        new MemberProcessingFlagFilter(ProcessingFlags.DONT_OPTIMIZE, 0,
                                                keepMarker),

                                        new AllAttributeVisitor(
                                                new AttributeProcessingFlagFilter(ProcessingFlags.DONT_OPTIMIZE, 0,
                                                        keepMarker))
                                ))
                ));

        // We also keep all members that have the DONT_OPTIMIZE flag set on their code attribute.
        programClassPool.classesAccept(
                new AllMemberVisitor(
                        new AllAttributeVisitor(
                                new AttributeNameFilter(Attribute.CODE,
                                        new AttributeProcessingFlagFilter(ProcessingFlags.DONT_OPTIMIZE, 0,
                                                new CodeAttributeToMethodVisitor(keepMarker))))));

        // We also keep all classes that are involved in .class constructs.
        // We're not looking at enum classes though, so they can be simplified.
        programClassPool.classesAccept(
                new ClassAccessFilter(0, AccessConstants.ENUM,
                        new AllMethodVisitor(
                                new AllAttributeVisitor(
                                        new AllInstructionVisitor(
                                                new DotClassClassVisitor(keepMarker))))));

        // We also keep all classes that are accessed dynamically.
        programClassPool.classesAccept(
                new AllConstantVisitor(
                        new ConstantTagFilter(Constant.STRING,
                                new ReferencedClassVisitor(keepMarker))));

        // We also keep all class members that are accessed dynamically.
        programClassPool.classesAccept(
                new AllConstantVisitor(
                        new ConstantTagFilter(Constant.STRING,
                                new ReferencedMemberVisitor(keepMarker))));

        // We also keep all bootstrap method signatures.
        programClassPool.classesAccept(
                new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_7,
                        new AllAttributeVisitor(
                                new AttributeNameFilter(Attribute.BOOTSTRAP_METHODS,
                                        new AllBootstrapMethodInfoVisitor(
                                                new BootstrapMethodHandleTraveler(
                                                        new MethodrefTraveler(
                                                                new ReferencedMemberVisitor(keepMarker))))))));

        // We also keep classes and methods referenced from bootstrap
        // method arguments.
        programClassPool.classesAccept(
                new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_7,
                        new AllAttributeVisitor(
                                new AttributeNameFilter(Attribute.BOOTSTRAP_METHODS,
                                        new AllBootstrapMethodInfoVisitor(
                                                new AllBootstrapMethodArgumentVisitor(
                                                        new MultiConstantVisitor(
                                                                // Class constants refer to additional functional
                                                                // interfaces (with LambdaMetafactory.altMetafactory).
                                                                new ConstantTagFilter(Constant.CLASS,
                                                                        new ReferencedClassVisitor(
                                                                                new FunctionalInterfaceFilter(
                                                                                        new ClassHierarchyTraveler(true, false, true, false,
                                                                                                new MultiClassVisitor(
                                                                                                        keepMarker,
                                                                                                        new AllMethodVisitor(
                                                                                                                new MemberAccessFilter(AccessConstants.ABSTRACT, 0,
                                                                                                                        keepMarker))
                                                                                                ))))),

                                                                // Method handle constants refer to synthetic lambda
                                                                // methods (with LambdaMetafactory.metafactory and
                                                                // altMetafactory).
                                                                new MethodrefTraveler(
                                                                        new ReferencedMemberVisitor(keepMarker)))))))));

        // We also keep the classes and abstract methods of functional
        // interfaces that are returned by dynamic method invocations.
        // These functional interfaces have to remain suitable for the
        // dynamic method invocations with LambdaMetafactory.
        programClassPool.classesAccept(
                new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_7,
                        new AllConstantVisitor(
                                new DynamicReturnedClassVisitor(
                                        new FunctionalInterfaceFilter(
                                                new ClassHierarchyTraveler(true, false, true, false,
                                                        new MultiClassVisitor(
                                                                keepMarker,
                                                                new AllMethodVisitor(
                                                                        new MemberAccessFilter(AccessConstants.ABSTRACT, 0,
                                                                                keepMarker))
                                                        )))))));

        // Attach some optimization info to all classes and class members, so
        // it can be filled out later.
        programClassPool.classesAccept(new ProgramClassOptimizationInfoSetter());

        programClassPool.classesAccept(new AllMemberVisitor(
                new ProgramMemberOptimizationInfoSetter()));

        // Give initial marks to read/written fields. side-effect methods, and
        // escaping parameters.
        final MutableBoolean mutableBoolean = new MutableBoolean();


        // Mark all fields as read and written.
        programClassPool.classesAccept(
                new AllFieldVisitor(
                        new ReadWriteFieldMarker(mutableBoolean)));


        // Mark methods based on their headers.
        /*programClassPool.classesAccept(
                new AllMethodVisitor(
                        new OptimizationInfoMemberFilter(
                                new MultiMemberVisitor(
                                        new SideEffectMethodMarker(),
                                        new ParameterEscapeMarker()
                                ))));*/

        /*programClassPool.accept(new InfluenceFixpointVisitor(
                new SideEffectVisitorMarkerFactory()));*/

        // Mark all used parameters, including the 'this' parameters.
        /*ParallelAllClassVisitor.ClassVisitorFactory markingUsedParametersClassVisitor =
                new ParallelAllClassVisitor.ClassVisitorFactory()
                {
                    public ClassVisitor createClassVisitor()
                    {
                        return
                                new AllMethodVisitor(
                                        new OptimizationInfoMemberFilter(
                                                new ParameterUsageMarker(!methodMarkingStatic,
                                                        !methodRemovalParameter)));
                    }
                };*/

        /*programClassPool.accept(
                new TimedClassPoolVisitor("Marking used parameters",
                        new ParallelAllClassVisitor(
                                markingUsedParametersClassVisitor)));*/

        // Mark all parameters of referenced methods in methods whose code must
        // be kept. This prevents shrinking of method descriptors which may not
        // be propagated correctly otherwise.
        programClassPool.accept(
                new TimedClassPoolVisitor("Marking used parameters in kept code attributes",
                        new AllClassVisitor(
                                new AllMethodVisitor(
                                        new OptimizationInfoMemberFilter(
                                                null,

                                                // visit all methods that are kept
                                                new AllAttributeVisitor(
                                                        new OptimizationCodeAttributeFilter(
                                                                null,

                                                                // visit all code attributes that are kept
                                                                new AllInstructionVisitor(
                                                                        new InstructionConstantVisitor(
                                                                                new ConstantTagFilter(new int[] { Constant.METHODREF,
                                                                                        Constant.INTERFACE_METHODREF },
                                                                                        new ReferencedMemberVisitor(
                                                                                                new OptimizationInfoMemberFilter(
                                                                                                        // Mark all parameters including "this" of referenced methods
                                                                                                        new ParameterUsageMarker(true, true, false))))))))
                                        )))));


        // Mark all classes with package visible members.
        // Mark all exception catches of methods.
        // Count all method invocations.
        // Mark super invocations and other access of methods.
        StackSizeComputer stackSizeComputer = new StackSizeComputer();

        programClassPool.accept(
                new TimedClassPoolVisitor("Marking method and referenced class properties",
                        new MultiClassVisitor(
                                // Mark classes.
                                new OptimizationInfoClassFilter(
                                        new MultiClassVisitor(
                                                new PackageVisibleMemberContainingClassMarker(),
                                                new WrapperClassMarker(),

                                                new AllConstantVisitor(
                                                        new PackageVisibleMemberInvokingClassMarker()),

                                                new AllMemberVisitor(
                                                        new ContainsConstructorsMarker())
                                        )),

                                // Mark methods.
                                new AllMethodVisitor(
                                        new OptimizationInfoMemberFilter(
                                                new AllAttributeVisitor(
                                                        new DebugAttributeVisitor("Marking method properties",
                                                                new MultiAttributeVisitor(
                                                                        stackSizeComputer,
                                                                        new CatchExceptionMarker(),

                                                                        new AllInstructionVisitor(
                                                                                new MultiInstructionVisitor(
                                                                                        new SuperInvocationMarker(),
                                                                                        new DynamicInvocationMarker(),
                                                                                        new BackwardBranchMarker(),
                                                                                        new AccessMethodMarker(),
                                                                                        new SynchronizedBlockMethodMarker(),
                                                                                        new FinalFieldAssignmentMarker(),
                                                                                        new NonEmptyStackReturnMarker(stackSizeComputer)
                                                                                ))
                                                                ))))),

                                // Mark referenced classes and methods.
                                new AllMethodVisitor(
                                        new AllAttributeVisitor(
                                                new DebugAttributeVisitor("Marking referenced class properties",
                                                        new MultiAttributeVisitor(
                                                                new AllExceptionInfoVisitor(
                                                                        new ExceptionHandlerConstantVisitor(
                                                                                new ReferencedClassVisitor(
                                                                                        new OptimizationInfoClassFilter(
                                                                                                new CaughtClassMarker())))),

                                                                new AllInstructionVisitor(
                                                                        new MultiInstructionVisitor(
                                                                                new InstantiationClassMarker(),
                                                                                new InstanceofClassMarker(),
                                                                                new DotClassMarker(),
                                                                                new MethodInvocationMarker()
                                                                        ))
                                                        ))))
                        )));
    }

    @Override
    public void visitClassPool(ClassPool classPool) {
        InstructionCounter methodInliningUniqueCounter = new InstructionCounter();
        classPool.accept(new TimedClassPoolVisitor("Inlining single methods",
                         new AllMethodVisitor(
                         new AllAttributeVisitor(
                         new DebugAttributeVisitor("Inlining single methods",
                         new OptimizationCodeAttributeFilter(
                         new SameClassMethodInliner(configuration.microEdition,
                                                    configuration.android,
                                                    configuration.allowAccessModification,
                                                    methodInliningUniqueCounter)))))));
        System.out.println("Lambda methods inlined: " + methodInliningUniqueCounter.getCount());
    }
}
