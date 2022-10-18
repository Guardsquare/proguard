/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.evaluation.*;
import proguard.evaluation.value.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.evaluation.*;
import proguard.optimize.evaluation.InstructionUsageMarker;
import proguard.optimize.info.*;
import proguard.optimize.kotlin.KotlinLambdaMerger;
import proguard.optimize.peephole.*;
import proguard.pass.Pass;
import proguard.util.*;

import java.io.*;
import java.util.*;

/**
 * This pass optimizes class pools according to a given configuration.
 *
 * This pass is stateful. It tracks when no more optimizations are
 * possible, and then all further runs of this pass will have no effect.
 *
 * @author Eric Lafortune
 */
public class Optimizer implements Pass
{
    private static final Logger logger = LogManager.getLogger(Optimizer.class);

    public  static final String LIBRARY_GSON                         = "library/gson";
    private static final String CLASS_MARKING_FINAL                  = "class/marking/final";
    private static final String CLASS_UNBOXING_ENUM                  = "class/unboxing/enum";
    private static final String CLASS_MERGING_VERTICAL               = "class/merging/vertical";
    private static final String CLASS_MERGING_HORIZONTAL             = "class/merging/horizontal";
    private static final String CLASS_MERGING_WRAPPER                = "class/merging/wrapper";
    private static final String CLASS_MERGING_KOTLINLAMBDA           = "class/merging/kotlinlambda";
    private static final String FIELD_REMOVAL_WRITEONLY              = "field/removal/writeonly";
    private static final String FIELD_MARKING_PRIVATE                = "field/marking/private";
    private static final String FIELD_GENERALIZATION_CLASS           = "field/generalization/class";
    private static final String FIELD_SPECIALIZATION_TYPE            = "field/specialization/type";
    private static final String FIELD_PROPAGATION_VALUE              = "field/propagation/value";
    private static final String METHOD_MARKING_PRIVATE               = "method/marking/private";
    private static final String METHOD_MARKING_STATIC                = "method/marking/static";
    private static final String METHOD_MARKING_FINAL                 = "method/marking/final";
    private static final String METHOD_MARKING_SYNCHRONIZED          = "method/marking/synchronized";
    private static final String METHOD_REMOVAL_PARAMETER             = "method/removal/parameter";
    private static final String METHOD_GENERALIZATION_CLASS          = "method/generalization/class";
    private static final String METHOD_SPECIALIZATION_PARAMETER_TYPE = "method/specialization/parametertype";
    private static final String METHOD_SPECIALIZATION_RETURN_TYPE    = "method/specialization/returntype";
    private static final String METHOD_PROPAGATION_PARAMETER         = "method/propagation/parameter";
    private static final String METHOD_PROPAGATION_RETURNVALUE       = "method/propagation/returnvalue";
    private static final String METHOD_INLINING_SHORT                = "method/inlining/short";
    private static final String METHOD_INLINING_UNIQUE               = "method/inlining/unique";
    private static final String METHOD_INLINING_TAILRECURSION        = "method/inlining/tailrecursion";
    private static final String CODE_MERGING                         = "code/merging";
    private static final String CODE_SIMPLIFICATION_VARIABLE         = "code/simplification/variable";
    private static final String CODE_SIMPLIFICATION_ARITHMETIC       = "code/simplification/arithmetic";
    private static final String CODE_SIMPLIFICATION_CAST             = "code/simplification/cast";
    private static final String CODE_SIMPLIFICATION_FIELD            = "code/simplification/field";
    private static final String CODE_SIMPLIFICATION_BRANCH           = "code/simplification/branch";
    private static final String CODE_SIMPLIFICATION_OBJECT           = "code/simplification/object";
    private static final String CODE_SIMPLIFICATION_STRING           = "code/simplification/string";
    private static final String CODE_SIMPLIFICATION_MATH             = "code/simplification/math";
    private static final String CODE_SIMPLIFICATION_ADVANCED         = "code/simplification/advanced";
    private static final String CODE_REMOVAL_ADVANCED                = "code/removal/advanced";
    private static final String CODE_REMOVAL_SIMPLE                  = "code/removal/simple";
    private static final String CODE_REMOVAL_VARIABLE                = "code/removal/variable";
    private static final String CODE_REMOVAL_EXCEPTION               = "code/removal/exception";
    private static final String CODE_ALLOCATION_VARIABLE             = "code/allocation/variable";


    public static final String[] OPTIMIZATION_NAMES = new String[]
    {
        LIBRARY_GSON,
        CLASS_MARKING_FINAL,
        CLASS_MERGING_VERTICAL,
        CLASS_MERGING_HORIZONTAL,
        FIELD_REMOVAL_WRITEONLY,
        FIELD_MARKING_PRIVATE,
        FIELD_GENERALIZATION_CLASS,
        FIELD_SPECIALIZATION_TYPE,
        FIELD_PROPAGATION_VALUE,
        METHOD_MARKING_PRIVATE,
        METHOD_MARKING_STATIC,
        METHOD_MARKING_FINAL,
        METHOD_MARKING_SYNCHRONIZED,
        METHOD_REMOVAL_PARAMETER,
        METHOD_GENERALIZATION_CLASS,
        METHOD_SPECIALIZATION_PARAMETER_TYPE,
        METHOD_SPECIALIZATION_RETURN_TYPE,
        METHOD_PROPAGATION_PARAMETER,
        METHOD_PROPAGATION_RETURNVALUE,
        METHOD_INLINING_SHORT,
        METHOD_INLINING_UNIQUE,
        METHOD_INLINING_TAILRECURSION,
        CODE_MERGING,
        CODE_SIMPLIFICATION_VARIABLE,
        CODE_SIMPLIFICATION_ARITHMETIC,
        CODE_SIMPLIFICATION_CAST,
        CODE_SIMPLIFICATION_FIELD,
        CODE_SIMPLIFICATION_BRANCH,
        CODE_SIMPLIFICATION_STRING,
        CODE_SIMPLIFICATION_MATH,
        CODE_SIMPLIFICATION_ADVANCED,
        CODE_REMOVAL_ADVANCED,
        CODE_REMOVAL_SIMPLE,
        CODE_REMOVAL_VARIABLE,
        CODE_REMOVAL_EXCEPTION,
        CODE_ALLOCATION_VARIABLE,
    };


    private boolean libraryGson;
    private boolean classMarkingFinal;
    private boolean classUnboxingEnum;
    private boolean classMergingVertical;
    private boolean classMergingHorizontal;
    private boolean classMergingWrapper;
    private boolean classMergingKotlinlambda;
    private boolean fieldRemovalWriteonly;
    private boolean fieldMarkingPrivate;
    private boolean fieldGeneralizationClass;
    private boolean fieldSpecializationType;
    private boolean fieldPropagationValue;
    private boolean methodMarkingPrivate;
    private boolean methodMarkingStatic;
    private boolean methodMarkingFinal;
    private boolean methodMarkingSynchronized;
    private boolean methodRemovalParameter;
    private boolean methodGeneralizationClass;
    private boolean methodSpecializationParametertype;
    private boolean methodSpecializationReturntype;
    private boolean methodPropagationParameter;
    private boolean methodPropagationReturnvalue;
    private boolean methodInliningShort;
    private boolean methodInliningUnique;
    private boolean methodInliningTailrecursion;
    private boolean codeMerging;
    private boolean codeSimplificationVariable;
    private boolean codeSimplificationArithmetic;
    private boolean codeSimplificationCast;
    private boolean codeSimplificationField;
    private boolean codeSimplificationBranch;
    private boolean codeSimplificationObject;
    private boolean codeSimplificationString;
    private boolean codeSimplificationMath;
    private boolean codeSimplificationPeephole;
    private boolean codeSimplificationAdvanced;
    private boolean codeRemovalAdvanced;
    private boolean codeRemovalSimple;
    private boolean codeRemovalVariable;
    private boolean codeRemovalException;
    private boolean codeAllocationVariable;


    // The optimizer uses this field to communicate to its following
    // invocations that no further optimizations are possible.
    private boolean moreOptimizationsPossible = true;
    private int     passIndex = 0;

    private final Configuration configuration;

    public Optimizer(Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void execute(AppView appView) throws Exception
    {
        if (!moreOptimizationsPossible)
        {
            if (KotlinLambdaMerger.lambdaMergingDone)
            {
                return;
            }
            else
            {
                new KotlinLambdaMerger(configuration).execute(appView);
            }
        }

        // Create a matcher for filtering optimizations.
        StringMatcher filter = configuration.optimizations != null ?
            new ListParser(new NameParser()).parse(configuration.optimizations) :
            new ConstantMatcher(true);

        libraryGson                       = filter.matches(LIBRARY_GSON);
        classMarkingFinal                 = filter.matches(CLASS_MARKING_FINAL);
        classUnboxingEnum                 = filter.matches(CLASS_UNBOXING_ENUM);
        classMergingVertical              = filter.matches(CLASS_MERGING_VERTICAL);
        classMergingHorizontal            = filter.matches(CLASS_MERGING_HORIZONTAL);
        classMergingWrapper               = filter.matches(CLASS_MERGING_WRAPPER);
        classMergingKotlinlambda          = filter.matches(CLASS_MERGING_KOTLINLAMBDA);
        fieldRemovalWriteonly             = filter.matches(FIELD_REMOVAL_WRITEONLY);
        fieldMarkingPrivate               = filter.matches(FIELD_MARKING_PRIVATE);
        fieldGeneralizationClass          = filter.matches(FIELD_GENERALIZATION_CLASS);
        fieldSpecializationType           = filter.matches(FIELD_SPECIALIZATION_TYPE);
        fieldPropagationValue             = filter.matches(FIELD_PROPAGATION_VALUE);
        methodMarkingPrivate              = filter.matches(METHOD_MARKING_PRIVATE);
        methodMarkingStatic               = filter.matches(METHOD_MARKING_STATIC);
        methodMarkingFinal                = filter.matches(METHOD_MARKING_FINAL);
        methodMarkingSynchronized         = filter.matches(METHOD_MARKING_SYNCHRONIZED);
        methodRemovalParameter            = filter.matches(METHOD_REMOVAL_PARAMETER);
        methodGeneralizationClass         = filter.matches(METHOD_GENERALIZATION_CLASS);
        methodSpecializationParametertype = filter.matches(METHOD_SPECIALIZATION_PARAMETER_TYPE);
        methodSpecializationReturntype    = filter.matches(METHOD_SPECIALIZATION_RETURN_TYPE);
        methodPropagationParameter        = filter.matches(METHOD_PROPAGATION_PARAMETER);
        methodPropagationReturnvalue      = filter.matches(METHOD_PROPAGATION_RETURNVALUE);
        methodInliningShort               = filter.matches(METHOD_INLINING_SHORT);
        methodInliningUnique              = filter.matches(METHOD_INLINING_UNIQUE);
        methodInliningTailrecursion       = filter.matches(METHOD_INLINING_TAILRECURSION);
        codeMerging                       = filter.matches(CODE_MERGING);
        codeSimplificationVariable        = filter.matches(CODE_SIMPLIFICATION_VARIABLE);
        codeSimplificationArithmetic      = filter.matches(CODE_SIMPLIFICATION_ARITHMETIC);
        codeSimplificationCast            = filter.matches(CODE_SIMPLIFICATION_CAST);
        codeSimplificationField           = filter.matches(CODE_SIMPLIFICATION_FIELD);
        codeSimplificationBranch          = filter.matches(CODE_SIMPLIFICATION_BRANCH);
        codeSimplificationObject          = filter.matches(CODE_SIMPLIFICATION_OBJECT);
        codeSimplificationString          = filter.matches(CODE_SIMPLIFICATION_STRING);
        codeSimplificationMath            = filter.matches(CODE_SIMPLIFICATION_MATH);
        codeSimplificationAdvanced        = filter.matches(CODE_SIMPLIFICATION_ADVANCED);
        codeRemovalAdvanced               = filter.matches(CODE_REMOVAL_ADVANCED);
        codeRemovalSimple                 = filter.matches(CODE_REMOVAL_SIMPLE);
        codeRemovalVariable               = filter.matches(CODE_REMOVAL_VARIABLE);
        codeRemovalException              = filter.matches(CODE_REMOVAL_EXCEPTION);
        codeAllocationVariable            = filter.matches(CODE_ALLOCATION_VARIABLE);

        // Some optimizations are required by other optimizations.
        codeSimplificationAdvanced =
            codeSimplificationAdvanced ||
            fieldPropagationValue      ||
            methodPropagationParameter ||
            methodPropagationReturnvalue;

        codeRemovalAdvanced =
            codeRemovalAdvanced   ||
            fieldRemovalWriteonly ||
            methodMarkingStatic   ||
            methodRemovalParameter;

        codeRemovalSimple =
            codeRemovalSimple ||
            codeSimplificationBranch;

        codeRemovalException =
            codeRemovalException ||
            codeRemovalAdvanced  ||
            codeRemovalSimple;

        codeSimplificationPeephole =
            codeSimplificationVariable   ||
            codeSimplificationArithmetic ||
            codeSimplificationCast       ||
            codeSimplificationField      ||
            codeSimplificationBranch     ||
            codeSimplificationObject     ||
            codeSimplificationString     ||
            codeSimplificationMath       ||
            fieldGeneralizationClass     ||
            methodGeneralizationClass;

        logger.info("Optimizing (pass {}/{})...", passIndex + 1, configuration.optimizationPasses);

        optimize(configuration,
                 appView.programClassPool,
                 appView.libraryClassPool,
                 appView.extraDataEntryNameMap);

        passIndex++;
    }


    /**
     * Performs optimization of the given program class pool.
     */
    private void optimize(Configuration         configuration,
                          ClassPool             programClassPool,
                          ClassPool             libraryClassPool,
                          ExtraDataEntryNameMap extraDataEntryNameMap)
    throws IOException
    {
        // Check if we have at least some keep commands.
        if (configuration.keep         == null &&
            configuration.applyMapping == null &&
            configuration.printMapping == null)
        {
            throw new IOException("You have to specify '-keep' options for the optimization step.");
        }

        // Create counters to count the numbers of optimizations.
        final ClassCounter         classMarkingFinalCounter                 = new ClassCounter();
        final ClassCounter         classUnboxingEnumCounter                 = new ClassCounter();
        final ClassCounter         classMergingVerticalCounter              = new ClassCounter();
        final ClassCounter         classMergingHorizontalCounter            = new ClassCounter();
        final ClassCounter         classMergingWrapperCounter               = new ClassCounter();
        final MemberCounter        fieldRemovalWriteonlyCounter             = new MemberCounter();
        final MemberCounter        fieldMarkingPrivateCounter               = new MemberCounter();
        final InstructionCounter   fieldGeneralizationClassCounter          = new InstructionCounter();
        final MemberCounter        fieldSpecializationTypeCounter           = new MemberCounter();
        final MemberCounter        fieldPropagationValueCounter             = new MemberCounter();
        final MemberCounter        methodMarkingPrivateCounter              = new MemberCounter();
        final MemberCounter        methodMarkingStaticCounter               = new MemberCounter();
        final MemberCounter        methodMarkingFinalCounter                = new MemberCounter();
        final MemberCounter        methodMarkingSynchronizedCounter         = new MemberCounter();
        final MemberCounter        methodRemovalParameterCounter1           = new MemberCounter();
        final MemberCounter        methodRemovalParameterCounter2           = new MemberCounter();
        final InstructionCounter   methodGeneralizationClassCounter         = new InstructionCounter();
        final MemberCounter        methodSpecializationParametertypeCounter = new MemberCounter();
        final MemberCounter        methodSpecializationReturntypeCounter    = new MemberCounter();
        final MemberCounter        methodPropagationParameterCounter        = new MemberCounter();
        final MemberCounter        methodPropagationReturnvalueCounter      = new MemberCounter();
        final InstructionCounter   methodInliningShortCounter               = new InstructionCounter();
        final InstructionCounter   methodInliningUniqueCounter              = new InstructionCounter();
        final InstructionCounter   methodInliningTailrecursionCounter       = new InstructionCounter();
        final InstructionCounter   codeMergingCounter                       = new InstructionCounter();
        final InstructionCounter   codeSimplificationVariableCounter        = new InstructionCounter();
        final InstructionCounter   codeSimplificationArithmeticCounter      = new InstructionCounter();
        final InstructionCounter   codeSimplificationCastCounter            = new InstructionCounter();
        final InstructionCounter   codeSimplificationFieldCounter           = new InstructionCounter();
        final InstructionCounter   codeSimplificationBranchCounter          = new InstructionCounter();
        final InstructionCounter   codeSimplificationObjectCounter          = new InstructionCounter();
        final InstructionCounter   codeSimplificationStringCounter          = new InstructionCounter();
        final InstructionCounter   codeSimplificationMathCounter            = new InstructionCounter();
        final InstructionCounter   codeSimplificationAndroidMathCounter     = new InstructionCounter();
        final InstructionCounter   codeSimplificationAdvancedCounter        = new InstructionCounter();
        final InstructionCounter   deletedCounter                           = new InstructionCounter();
        final InstructionCounter   addedCounter                             = new InstructionCounter();
        final MemberCounter        codeRemovalVariableCounter               = new MemberCounter();
        final ExceptionCounter     codeRemovalExceptionCounter              = new ExceptionCounter();
        final MemberCounter        codeAllocationVariableCounter            = new MemberCounter();
        final MemberCounter        initializerFixCounter1                   = new MemberCounter();
        final MemberCounter        initializerFixCounter2                   = new MemberCounter();

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

        if (configuration.assumeNoSideEffects != null)
        {
            // Create a visitor for marking classes and methods that don't have
            // any side effects.
            NoSideEffectClassMarker  noSideEffectClassMarker  = new NoSideEffectClassMarker();
            NoSideEffectMethodMarker noSideEffectMethodMarker = new NoSideEffectMethodMarker();
            ClassPoolVisitor classPoolVisitor =
                new ClassSpecificationVisitorFactory()
                    .createClassPoolVisitor(configuration.assumeNoSideEffects,
                                            noSideEffectClassMarker,
                                            noSideEffectMethodMarker);

            // Mark the seeds.
            programClassPool.accept(classPoolVisitor);
            libraryClassPool.accept(classPoolVisitor);
        }

        if (configuration.assumeNoExternalSideEffects != null)
        {
            // Create a visitor for marking classes and methods that don't have
            // any external side effects.
            NoSideEffectClassMarker          noSideEffectClassMarker  = new NoSideEffectClassMarker();
            NoExternalSideEffectMethodMarker noSideEffectMethodMarker = new NoExternalSideEffectMethodMarker();
            ClassPoolVisitor classPoolVisitor =
                new ClassSpecificationVisitorFactory()
                    .createClassPoolVisitor(configuration.assumeNoExternalSideEffects,
                                            noSideEffectClassMarker,
                                            noSideEffectMethodMarker);

            // Mark the seeds.
            programClassPool.accept(classPoolVisitor);
            libraryClassPool.accept(classPoolVisitor);
        }

        if (configuration.assumeNoEscapingParameters != null)
        {
            // Create a visitor for marking methods that don't let any
            // reference parameters escape.
            NoEscapingParametersMethodMarker noEscapingParametersMethodMarker = new NoEscapingParametersMethodMarker();
            ClassPoolVisitor classPoolVisitor =
                new ClassSpecificationVisitorFactory()
                    .createClassPoolVisitor(configuration.assumeNoEscapingParameters,
                                            null,
                                            noEscapingParametersMethodMarker);

            // Mark the seeds.
            programClassPool.accept(classPoolVisitor);
            libraryClassPool.accept(classPoolVisitor);
        }

        if (configuration.assumeNoExternalReturnValues != null)
        {
            // Create a visitor for marking methods that don't let any
            // reference parameters escape.
            NoExternalReturnValuesMethodMarker noExternalReturnValuesMethodMarker = new NoExternalReturnValuesMethodMarker();
            ClassPoolVisitor classPoolVisitor =
                new ClassSpecificationVisitorFactory()
                    .createClassPoolVisitor(configuration.assumeNoExternalReturnValues,
                                            null,
                                            noExternalReturnValuesMethodMarker);

            // Mark the seeds.
            programClassPool.accept(classPoolVisitor);
            libraryClassPool.accept(classPoolVisitor);
        }

        if (classMarkingFinal)
        {
            // Make classes final, whereever possible.
            programClassPool.classesAccept(
                new ClassFinalizer(classMarkingFinalCounter));
        }

        if (methodMarkingFinal)
        {
            // Make methods final, whereever possible.
            programClassPool.classesAccept(
                new ClassAccessFilter(0, AccessConstants.INTERFACE,
                new AllMethodVisitor(
                new MethodFinalizer(methodMarkingFinalCounter))));
        }

        // Give initial marks to read/written fields. side-effect methods, and
        // escaping parameters.
        final MutableBoolean mutableBoolean = new MutableBoolean();

        if (fieldRemovalWriteonly)
        {
            // Mark fields that are read or written. The written flag is
            // currently only needed for the write-only counter later on.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new AllInstructionVisitor(
                new ReadWriteFieldMarker(mutableBoolean)))));
        }
        else
        {
            // Mark all fields as read and written.
            programClassPool.classesAccept(
                new AllFieldVisitor(
                new ReadWriteFieldMarker(mutableBoolean)));
        }

        // Mark methods based on their headers.
        programClassPool.classesAccept(
            new AllMethodVisitor(
            new OptimizationInfoMemberFilter(
            new MultiMemberVisitor(
                new SideEffectMethodMarker(),
                new ParameterEscapeMarker()
            ))));

        programClassPool.accept(new InfluenceFixpointVisitor(
                                new SideEffectVisitorMarkerFactory()));

        if (methodMarkingSynchronized)
        {
            // Mark all superclasses of escaping (kept) classes.
            programClassPool.classesAccept(
                new EscapingClassFilter(
                new ClassHierarchyTraveler(false, true, true, false,
                new EscapingClassMarker())));

            ParallelAllClassVisitor.ClassVisitorFactory markingEscapingClassVisitor =
                new ParallelAllClassVisitor.ClassVisitorFactory()
                {
                    public ClassVisitor createClassVisitor()
                    {
                        return
                            new AllMethodVisitor(
                            new AllAttributeVisitor(
                            new EscapingClassMarker()));
                    }
                };

            // Mark classes that escape to the heap.
            programClassPool.accept(
                new TimedClassPoolVisitor("Marking escaping classes",
                new ParallelAllClassVisitor(
                markingEscapingClassVisitor)));

            // Desynchronize all non-static methods whose classes don't escape.
            programClassPool.classesAccept(
                new EscapingClassFilter(null,
                new AllMethodVisitor(
                new OptimizationInfoMemberFilter(
                new MemberAccessFilter(AccessConstants.SYNCHRONIZED, AccessConstants.STATIC,
                new MultiMemberVisitor(
                    new MemberAccessFlagCleaner(AccessConstants.SYNCHRONIZED),
                    methodMarkingSynchronizedCounter
                ))))));
        }

        if (fieldRemovalWriteonly)
        {
            // Count the write-only fields.
            programClassPool.classesAccept(
                new AllFieldVisitor(
                new WriteOnlyFieldFilter(fieldRemovalWriteonlyCounter)));
        }

        if (classUnboxingEnum)
        {
            ClassCounter counter = new ClassCounter();

            // Mark all final enums that qualify as simple enums.
            programClassPool.classesAccept(
                new ClassAccessFilter(AccessConstants.FINAL |
                                      AccessConstants.ENUM, 0,
                new OptimizationInfoClassFilter(
                new SimpleEnumClassChecker())));

            // Count the preliminary number of simple enums.
            programClassPool.classesAccept(
                new SimpleEnumFilter(counter));

            // Only continue checking simple enums if there are any candidates.
            if (counter.getCount() > 0)
            {
                // Unmark all simple enums that are explicitly used as objects.
                programClassPool.classesAccept(
                    new SimpleEnumUseChecker());

                // Unmark all simple enums that are used in descriptors of
                // kept class members. Changing their names could upset
                // the name parameters of invokedynamic instructions.
                programClassPool.classesAccept(
                    new SimpleEnumFilter(null,
                    new AllMemberVisitor(
                    new KeptMemberFilter(
                    new MemberDescriptorReferencedClassVisitor(
                    new OptimizationInfoClassFilter(
                    new SimpleEnumMarker(false)))))));

                // Count the definitive number of simple enums.
                programClassPool.classesAccept(
                    new SimpleEnumFilter(classUnboxingEnumCounter));

                // Only start handling simple enums if there are any.
                if (classUnboxingEnumCounter.getCount() > 0)
                {
                    // Simplify the use of the enum classes in code.
                    programClassPool.accept(
                        new TimedClassPoolVisitor("Simplify use of simple enums",
                        new AllMethodVisitor(
                        new AllAttributeVisitor(
                        new SimpleEnumUseSimplifier()))));

                    // Simplify the static initializers of simple enum classes.
                    programClassPool.classesAccept(
                        new SimpleEnumFilter(
                        new SimpleEnumClassSimplifier()));

                    // Simplify the use of the enum classes in descriptors.
                    programClassPool.classesAccept(
                        new SimpleEnumDescriptorSimplifier());

                    // Update references to class members with simple enum classes.
                    programClassPool.classesAccept(new MemberReferenceFixer(configuration.android));
                }
            }
        }

        // Mark all used parameters, including the 'this' parameters.
        ParallelAllClassVisitor.ClassVisitorFactory markingUsedParametersClassVisitor =
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
            };

        programClassPool.accept(
            new TimedClassPoolVisitor("Marking used parameters",
            new ParallelAllClassVisitor(
            markingUsedParametersClassVisitor)));

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

//        System.out.println("Optimizer.execute: before evaluation simplification");
//        programClassPool.classAccept("abc/Def", new NamedMethodVisitor("abc", null, new ClassPrinter()));

        // Perform partial evaluation for filling out fields, method parameters,
        // and method return values, so they can be propagated.
        if (fieldSpecializationType           ||
            methodSpecializationParametertype ||
            methodSpecializationReturntype    ||
            fieldPropagationValue             ||
            methodPropagationParameter        ||
            methodPropagationReturnvalue      ||
            classMergingWrapper)
        {
            // We'll create values to be stored with fields, method parameters,
            // and return values.
            ValueFactory valueFactory         = new BasicRangeValueFactory();
            ValueFactory detailedValueFactory = new DetailedArrayValueFactory();

            InvocationUnit storingInvocationUnit =
                new StoringInvocationUnit(valueFactory,
                                          fieldSpecializationType           || fieldPropagationValue,
                                          methodSpecializationParametertype || methodPropagationParameter || classMergingWrapper,
                                          methodSpecializationReturntype    || methodPropagationReturnvalue);

            // Evaluate synthetic classes in more detail, notably to propagate
            // the arrays of the classes generated for enum switch statements.
            programClassPool.classesAccept(
                new ClassAccessFilter(AccessConstants.SYNTHETIC, 0,
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Filling out fields, method parameters, and return values in synthetic classes",
                new PartialEvaluator(detailedValueFactory, storingInvocationUnit, false))))));

            // Evaluate non-synthetic classes. We may need to evaluate all
            // casts, to account for downcasts when specializing descriptors.

            ParallelAllClassVisitor.ClassVisitorFactory fillingOutValuesClassVisitor =
                new ParallelAllClassVisitor.ClassVisitorFactory()
                {
                    public ClassVisitor createClassVisitor()
                    {
                        ValueFactory valueFactory = new ParticularValueFactory();

                        InvocationUnit storingInvocationUnit =
                            new StoringInvocationUnit(valueFactory,
                                                      fieldSpecializationType           || fieldPropagationValue,
                                                      methodSpecializationParametertype || methodPropagationParameter || classMergingWrapper,
                                                      methodSpecializationReturntype    || methodPropagationReturnvalue);

                        return
                            new ClassAccessFilter(0, AccessConstants.SYNTHETIC,
                            new AllMethodVisitor(
                            new AllAttributeVisitor(
                            new DebugAttributeVisitor("Filling out fields, method parameters, and return values",
                            new PartialEvaluator(valueFactory, storingInvocationUnit,
                                                 fieldSpecializationType           ||
                                                 methodSpecializationParametertype ||
                                                 methodSpecializationReturntype)))));
                    }
                };

            programClassPool.accept(
                new TimedClassPoolVisitor("Filling out values in non-synthetic classes",
                new ParallelAllClassVisitor(
                fillingOutValuesClassVisitor)));

            if (fieldSpecializationType           ||
                methodSpecializationParametertype ||
                methodSpecializationReturntype)
            {
                // Specialize class member descriptors, based on partial evaluation.
                programClassPool.classesAccept(
                    new AllMemberVisitor(
                    new OptimizationInfoMemberFilter(
                    new MemberDescriptorSpecializer(fieldSpecializationType,
                                                    methodSpecializationParametertype,
                                                    methodSpecializationReturntype,
                                                    fieldSpecializationTypeCounter,
                                                    methodSpecializationParametertypeCounter,
                                                    methodSpecializationReturntypeCounter))));

                if (fieldSpecializationTypeCounter.getCount()           > 0 ||
                    methodSpecializationParametertypeCounter.getCount() > 0 ||
                    methodSpecializationReturntypeCounter.getCount()    > 0)
                {
                    // Fix all references to specialized members.
                    programClassPool.classesAccept(new MemberReferenceFixer(configuration.android));
                }
            }

            if (configuration.assumeValues != null)
            {
                // Create a visitor for setting assumed values.
                ClassPoolVisitor classPoolVisitor =
                    new AssumeClassSpecificationVisitorFactory(valueFactory)
                        .createClassPoolVisitor(configuration.assumeValues,
                                                null,
                                                new MultiMemberVisitor());

                // Set the assumed values.
                programClassPool.accept(classPoolVisitor);
                libraryClassPool.accept(classPoolVisitor);
            }

            if (fieldPropagationValue)
            {
                // Count the constant fields.
                programClassPool.classesAccept(
                    new AllFieldVisitor(
                    new ConstantMemberFilter(fieldPropagationValueCounter)));
            }

            if (methodPropagationParameter)
            {
                // Count the constant method parameters.
                programClassPool.classesAccept(
                    new AllMethodVisitor(
                    new ConstantParameterFilter(methodPropagationParameterCounter)));
            }

            if (methodPropagationReturnvalue)
            {
                // Count the constant method return values.
                programClassPool.classesAccept(
                    new AllMethodVisitor(
                    new ConstantMemberFilter(methodPropagationReturnvalueCounter)));
            }

            if (classUnboxingEnumCounter.getCount() > 0)
            {
                // Propagate the simple enum constant counts.
                programClassPool.classesAccept(
                    new SimpleEnumFilter(
                    new SimpleEnumArrayPropagator()));
            }

            if (codeSimplificationAdvanced)
            {
                // Fill out constants into the arrays of synthetic classes,
                // notably the arrays of the classes generated for enum switch
                // statements.
                InvocationUnit loadingInvocationUnit =
                    new LoadingInvocationUnit(valueFactory,
                                              fieldPropagationValue,
                                              methodPropagationParameter,
                                              methodPropagationReturnvalue);

                programClassPool.classesAccept(
                    new ClassAccessFilter(AccessConstants.SYNTHETIC, 0,
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new PartialEvaluator(valueFactory, loadingInvocationUnit, false)))));
            }
        }

        if (codeSimplificationAdvanced)
        {
            ParallelAllClassVisitor.ClassVisitorFactory simplifyingCodeVisitor =
                new ParallelAllClassVisitor.ClassVisitorFactory()
                {
                    public ClassVisitor createClassVisitor()
                    {
                        // Perform partial evaluation again, now loading any previously stored
                        // values for fields, method parameters, and method return values.
                        ValueFactory valueFactory = new IdentifiedValueFactory();

                        SimplifiedInvocationUnit loadingInvocationUnit =
                            new LoadingInvocationUnit(valueFactory,
                                                      fieldPropagationValue,
                                                      methodPropagationParameter,
                                                      methodPropagationReturnvalue);

                        return
                            new AllMethodVisitor(
                            new AllAttributeVisitor(
                            new DebugAttributeVisitor("Simplifying code",
                            new OptimizationCodeAttributeFilter(
                            new EvaluationSimplifier(
                            new PartialEvaluator(valueFactory, loadingInvocationUnit, false),
                            codeSimplificationAdvancedCounter)))));
                    }
                };

            // Simplify based on partial evaluation, propagating constant
            // field values, method parameter values, and return values.
            programClassPool.accept(
                new TimedClassPoolVisitor("Simplifying code",
                new ParallelAllClassVisitor(
                simplifyingCodeVisitor)));
        }

        if (codeRemovalAdvanced)
        {
            ParallelAllClassVisitor.ClassVisitorFactory shrinkingCodeVisitor =
                new ParallelAllClassVisitor.ClassVisitorFactory()
                {
                    public ClassVisitor createClassVisitor()
                    {
                        // Perform partial evaluation again, now loading any previously stored
                        // values for fields, method parameters, and method return values.
                        ValueFactory valueFactory = new IdentifiedValueFactory();

                        SimplifiedInvocationUnit loadingInvocationUnit =
                            new LoadingInvocationUnit(valueFactory,
                                                      fieldPropagationValue,
                                                      methodPropagationParameter,
                                                      methodPropagationReturnvalue);

                        // Trace the construction of reference values.
                        ReferenceTracingValueFactory referenceTracingValueFactory =
                            new ReferenceTracingValueFactory(valueFactory);

                        return
                            new AllMethodVisitor(
                            new AllAttributeVisitor(
                            new DebugAttributeVisitor("Shrinking code",
                            new OptimizationCodeAttributeFilter(
                            new EvaluationShrinker(
                            new InstructionUsageMarker(
                            new PartialEvaluator(referenceTracingValueFactory,
                                                 new ParameterTracingInvocationUnit(loadingInvocationUnit),
                                                 !codeSimplificationAdvanced,
                                                 referenceTracingValueFactory),
                            true), true, deletedCounter, addedCounter)))));
                    }
                };

            // Remove code based on partial evaluation, also removing unused
            // parameters from method invocations, and making methods static
            // if possible.
            programClassPool.accept(
                new TimedClassPoolVisitor("Shrinking code",
                new ParallelAllClassVisitor(
                shrinkingCodeVisitor)));
        }

        if (methodRemovalParameter)
        {
            // Shrink the parameters in the method descriptors.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new UnusedParameterMethodFilter(
                new OptimizationInfoMemberFilter(
                new MethodDescriptorShrinker(methodRemovalParameterCounter1)))));
        }

        if (methodMarkingStatic)
        {
            // Make all non-static methods that don't require the 'this'
            // parameter static.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new OptimizationInfoMemberFilter(
                new MemberAccessFilter(0, AccessConstants.STATIC,
                new MethodStaticizer(methodMarkingStaticCounter)))));
        }

        if (methodRemovalParameterCounter1.getCount() > 0)
        {
            // Fix all references to class members.
            // This operation also updates the stack sizes.
            programClassPool.classesAccept(new MemberReferenceFixer(configuration.android));

            // Remove unused bootstrap method arguments.
            programClassPool.classesAccept(
                new AllAttributeVisitor(
                new AllBootstrapMethodInfoVisitor(
                new BootstrapMethodArgumentShrinker())));
        }

        if (methodRemovalParameterCounter1.getCount() > 0 ||
            methodMarkingPrivate                          ||
            // Methods are only marked private later on.
            //methodMarkingPrivateCounter   .getCount() > 0 ||
            methodMarkingStaticCounter    .getCount() > 0)
        {
            // Remove all unused parameters from the corresponding byte code,
            // shifting all remaining variables.
            // This operation also updates the local variable frame sizes.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new UnusedParameterMethodFilter(
                new AllAttributeVisitor(
                new ParameterShrinker(methodRemovalParameterCounter2)))));

            // Remove all unused parameters in the optimization info.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new UnusedParameterMethodFilter(
                new AllAttributeVisitor(
                new UnusedParameterOptimizationInfoUpdater(new ProcessingFlagSetter(ProcessingFlags.MODIFIED))))));
        }
        else if (codeRemovalAdvanced)
        {
            // Just update the local variable frame sizes.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new OptimizationCodeAttributeFilter(
                new StackSizeUpdater()))));
        }

        if (methodRemovalParameter &&
            methodRemovalParameterCounter2.getCount() > 0)
        {
            // Tweak the descriptors of duplicate initializers, due to removed
            // method parameters.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new DuplicateInitializerFixer(initializerFixCounter1)));

            if (initializerFixCounter1.getCount() > 0)
            {
                // Fix all invocations of tweaked initializers.
                programClassPool.classesAccept(
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new DuplicateInitializerInvocationFixer(addedCounter))));

                // Fix all references to tweaked initializers.
                programClassPool.classesAccept(new MemberReferenceFixer(configuration.android));
            }
        }

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

        if (classMergingWrapper)
        {
            // Merge wrapper classes into their wrapped classes.
            programClassPool.accept(
                new TimedClassPoolVisitor("Merging wrapper classes",
                // Exclude injected classes - they might not end up in the output.
                new InjectedClassFilter(null,
                new WrapperClassMerger(configuration.allowAccessModification,
                                       classMergingWrapperCounter))));

            if (classMergingWrapperCounter.getCount() > 0)
            {
                // Fix all uses of wrapper classes.
                programClassPool.classesAccept(
                    new RetargetedClassFilter(null,
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new WrapperClassUseSimplifier()))));
            }
        }

        if (classMergingVertical)
        {
            // Merge subclasses up into their superclasses or
            // merge interfaces down into their implementing classes.
            programClassPool.accept(
                new TimedClassPoolVisitor("Merging classes vertically",
                // Exclude injected classes - they might not end up in the output.
                new InjectedClassFilter(null,
                new VerticalClassMerger(configuration.allowAccessModification,
                                        configuration.mergeInterfacesAggressively,
                                        classMergingVerticalCounter))));
        }

        if (classMergingHorizontal)
        {
            long start = System.currentTimeMillis();

            Set<String> forbiddenClassNames = new HashSet<>();

            // Extra Data Entries are not allowed to be merged
            extraDataEntryNameMap.getAllExtraDataEntryNames()
                .stream()
                .filter(s -> s.endsWith(".class"))
                .map(s -> s.substring(0, s.length() - 6))
                .forEachOrdered(forbiddenClassNames::add);


            programClassPool.accept(
                new HorizontalClassMerger(configuration.allowAccessModification,
                                          configuration.mergeInterfacesAggressively,
                                          forbiddenClassNames,
                                          classMergingHorizontalCounter)
            );

            long end = System.currentTimeMillis();
            logger.trace("Merging classes horizontally.................... took: %6d ms", (end - start));

        }

        if (classMergingVerticalCounter  .getCount() > 0 ||
            classMergingHorizontalCounter.getCount() > 0 ||
            classMergingWrapperCounter   .getCount() > 0)
        {
            // Clean up inner class attributes to avoid loops.
            programClassPool.classesAccept(new RetargetedInnerClassAttributeRemover());

            // Update references to merged classes: first the referenced
            // classes, then the various actual descriptors.
            // Leave retargeted classes themselves unchanged and valid,
            // in case they aren't shrunk later on.
            programClassPool.classesAccept(new RetargetedClassFilter(null, new TargetClassChanger()));
            programClassPool.classesAccept(new RetargetedClassFilter(null, new ClassReferenceFixer(true)));
            programClassPool.classesAccept(new RetargetedClassFilter(null, new MemberReferenceFixer(configuration.android)));

            if (configuration.allowAccessModification)
            {
                // Fix the access flags of referenced merged classes and their
                // class members.
                programClassPool.classesAccept(new AccessFixer());
            }

            // Fix the access flags of the inner classes information.
            // DGD-63: don't change the access flags of inner classes
            // that have not been renamed (Guice).
            programClassPool.classesAccept(
                new KeptClassFilter(null,
                new AllAttributeVisitor(
                new AllInnerClassesInfoVisitor(
                new InnerClassesAccessFixer()))));

            // Tweak the descriptors of duplicate initializers, due to merged
            // parameter classes.
            programClassPool.classesAccept(
                new AllMethodVisitor(
                new DuplicateInitializerFixer(initializerFixCounter2)));

            if (initializerFixCounter2.getCount() > 0)
            {
                // Fix all invocations of tweaked initializers.
                programClassPool.classesAccept(
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new DuplicateInitializerInvocationFixer(addedCounter))));

                // Fix all references to tweaked initializers.
                programClassPool.classesAccept(new MemberReferenceFixer(configuration.android));
            }
        }

        if (methodInliningUnique)
        {
            // Inline methods that are only invoked once.
            programClassPool.accept(
                new TimedClassPoolVisitor("Inlining single methods",
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Inlining single methods",
                new OptimizationCodeAttributeFilter(
                new SingleInvocationMethodInliner(configuration.microEdition,
                                                  configuration.android,
                                                  configuration.allowAccessModification,
                                                  methodInliningUniqueCounter)))))));
        }

        if (methodInliningShort)
        {
            // Inline short methods.
            programClassPool.accept(
                new TimedClassPoolVisitor("Inlining short methods",
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Inlining short methods",
                new OptimizationCodeAttributeFilter(
                new ShortMethodInliner(configuration.microEdition,
                                       configuration.android,
                                       configuration.allowAccessModification,
                                       methodInliningShortCounter)))))));
        }

        if (methodInliningTailrecursion)
        {
            // Simplify tail recursion calls.
            programClassPool.accept(
                new TimedClassPoolVisitor("Simplifying tail recursion",
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Simplifying tail recursion",
                new OptimizationCodeAttributeFilter(
                new TailRecursionSimplifier(methodInliningTailrecursionCounter)))))));
        }

        if (fieldMarkingPrivate ||
            methodMarkingPrivate)
        {
            // Mark all class members that can not be made private.
            programClassPool.classesAccept(new NonPrivateMemberMarker());
        }

        if (fieldMarkingPrivate)
        {
            // Make all non-private fields private, whereever possible.
            programClassPool.classesAccept(
                new ClassAccessFilter(0, AccessConstants.INTERFACE,
                new AllFieldVisitor(
                new MemberAccessFilter(0, AccessConstants.PRIVATE,
                new MemberPrivatizer(fieldMarkingPrivateCounter)))));
        }

        if (methodMarkingPrivate)
        {
            // Make all non-private methods private, whereever possible.
            programClassPool.classesAccept(
                new ClassAccessFilter(0, AccessConstants.INTERFACE,
                new AllMethodVisitor(
                new MemberAccessFilter(0, AccessConstants.PRIVATE,
                new MemberPrivatizer(methodMarkingPrivateCounter)))));
        }

        if ((methodInliningUniqueCounter       .getCount() > 0 ||
             methodInliningShortCounter        .getCount() > 0 ||
             methodInliningTailrecursionCounter.getCount() > 0) &&
            configuration.allowAccessModification)
        {
            // Fix the access flags of referenced classes and class members,
            // for MethodInliner.
            programClassPool.classesAccept(new AccessFixer());
        }

        if (methodRemovalParameterCounter2.getCount() > 0 ||
            classMergingVerticalCounter   .getCount() > 0 ||
            classMergingHorizontalCounter .getCount() > 0 ||
            classMergingWrapperCounter    .getCount() > 0 ||
            methodMarkingPrivateCounter   .getCount() > 0 ||
            ((methodInliningUniqueCounter       .getCount() > 0 ||
              methodInliningShortCounter        .getCount() > 0 ||
              methodInliningTailrecursionCounter.getCount() > 0) &&
             configuration.allowAccessModification))
        {
            // Fix invocations of interface methods, or methods that have become
            // non-abstract or private, and of methods that have moved to a
            // different package.
            programClassPool.classesAccept(
                new AllMemberVisitor(
                new AllAttributeVisitor(
                new MethodInvocationFixer())));
        }

        if (codeMerging)
        {
            // Share common blocks of code at branches.
            programClassPool.accept(
                new TimedClassPoolVisitor("Sharing common code",
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Sharing common code",
                new OptimizationCodeAttributeFilter(
                new GotoCommonCodeReplacer(codeMergingCounter)))))));
        }

        if (codeSimplificationPeephole)
        {
            ParallelAllClassVisitor.ClassVisitorFactory peepHoleOptimizer =
                new ParallelAllClassVisitor.ClassVisitorFactory()
                {
                    public ClassVisitor createClassVisitor()
                    {
                        // Create a branch target marker and a code attribute editor that can
                        // be reused for all code attributes.
                        BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
                        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

                        InstructionSequenceConstants sequences =
                            new InstructionSequenceConstants(programClassPool,
                                                             libraryClassPool);

                        List<InstructionVisitor> peepholeOptimizations = createPeepholeOptimizations(configuration,
                                                                                 sequences,
                                                                                 branchTargetFinder,
                                                                                 codeAttributeEditor,
                                                                                 codeSimplificationVariableCounter,
                                                                                 codeSimplificationArithmeticCounter,
                                                                                 codeSimplificationCastCounter,
                                                                                 codeSimplificationFieldCounter,
                                                                                 codeSimplificationBranchCounter,
                                                                                 codeSimplificationObjectCounter,
                                                                                 codeSimplificationStringCounter,
                                                                                 codeSimplificationMathCounter,
                                                                                 codeSimplificationAndroidMathCounter,
                                                                                 fieldGeneralizationClassCounter,
                                                                                 methodGeneralizationClassCounter);

                        // Convert the list into an array.
                        InstructionVisitor[] peepholeOptimizationsArray =
                            new InstructionVisitor[peepholeOptimizations.size()];
                        peepholeOptimizations.toArray(peepholeOptimizationsArray);

                        return
                            new AllMethodVisitor(
                            new AllAttributeVisitor(
                            new DebugAttributeVisitor("Peephole optimizations",
                            new OptimizationCodeAttributeFilter(
                            new PeepholeEditor(branchTargetFinder, codeAttributeEditor,
                                               new MultiInstructionVisitor(
                            peepholeOptimizationsArray))))));
                    }
                };

            // Perform the peephole optimisations.
            programClassPool.accept(
                new TimedClassPoolVisitor("Peephole optimizations",
                new ParallelAllClassVisitor(
                peepHoleOptimizer)));
        }

        if (codeRemovalException)
        {
            // Remove unnecessary exception handlers.
            programClassPool.accept(
                new TimedClassPoolVisitor("Unreachable exception removal",
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Unreachable exception removal",
                new OptimizationCodeAttributeFilter(
                new UnreachableExceptionRemover(codeRemovalExceptionCounter)))))));
        }

        if (codeRemovalSimple)
        {
            // Remove unreachable code.
            programClassPool.accept(
                new TimedClassPoolVisitor("Unreachable code removal",
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Unreachable code removal",
                new OptimizationCodeAttributeFilter(
                new UnreachableCodeRemover(deletedCounter)))))));
        }

        if (codeRemovalVariable)
        {
            // Remove all unused local variables.
            programClassPool.accept(
                new TimedClassPoolVisitor("Variable shrinking",
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new DebugAttributeVisitor("Variable shrinking",
                new OptimizationCodeAttributeFilter(
                new VariableShrinker(codeRemovalVariableCounter)))))));
        }

        if (codeAllocationVariable)
        {
            ParallelAllClassVisitor.ClassVisitorFactory optimizingVariablesVisitor =
                new ParallelAllClassVisitor.ClassVisitorFactory()
                {
                    public ClassVisitor createClassVisitor()
                    {
                        return
                            new AllMethodVisitor(
                            new AllAttributeVisitor(
                            new DebugAttributeVisitor("Variable optimizations",
                            new OptimizationCodeAttributeFilter(
                            new VariableOptimizer(false, codeAllocationVariableCounter)))));
                    }
                };

            // Optimize the variables.
            programClassPool.accept(
                new TimedClassPoolVisitor("Variable optimizations",
                new ParallelAllClassVisitor(
                optimizingVariablesVisitor)));
        }

        // Remove unused constants.
        programClassPool.accept(
            new TimedClassPoolVisitor("Shrinking constant pool",
            new ConstantPoolShrinker()));

        int classMarkingFinalCount                 = classMarkingFinalCounter                .getCount();
        int classUnboxingEnumCount                 = classUnboxingEnumCounter                .getCount();
        int classMergingVerticalCount              = classMergingVerticalCounter             .getCount();
        int classMergingHorizontalCount            = classMergingHorizontalCounter           .getCount();
        int classMergingWrapperCount               = classMergingWrapperCounter              .getCount();
        int fieldRemovalWriteonlyCount             = fieldRemovalWriteonlyCounter            .getCount();
        int fieldMarkingPrivateCount               = fieldMarkingPrivateCounter              .getCount();
        int fieldGeneralizationClassCount          = fieldGeneralizationClassCounter         .getCount();
        int fieldSpecializationTypeCount           = fieldSpecializationTypeCounter          .getCount();
        int fieldPropagationValueCount             = fieldPropagationValueCounter            .getCount();
        int methodMarkingPrivateCount              = methodMarkingPrivateCounter             .getCount();
        int methodMarkingStaticCount               = methodMarkingStaticCounter              .getCount();
        int methodMarkingFinalCount                = methodMarkingFinalCounter               .getCount();
        int methodMarkingSynchronizedCount         = methodMarkingSynchronizedCounter        .getCount();
        int methodRemovalParameterCount1           = methodRemovalParameterCounter1          .getCount() - initializerFixCounter1.getCount() - initializerFixCounter2.getCount();
        int methodRemovalParameterCount2           = methodRemovalParameterCounter2          .getCount() - methodMarkingStaticCounter.getCount() - initializerFixCounter1.getCount() - initializerFixCounter2.getCount();
        int methodGeneralizationClassCount         = methodGeneralizationClassCounter        .getCount();
        int methodSpecializationParametertypeCount = methodSpecializationParametertypeCounter.getCount();
        int methodSpecializationReturntypeCount    = methodSpecializationReturntypeCounter   .getCount();
        int methodPropagationParameterCount        = methodPropagationParameterCounter       .getCount();
        int methodPropagationReturnvalueCount      = methodPropagationReturnvalueCounter     .getCount();
        int methodInliningShortCount               = methodInliningShortCounter              .getCount();
        int methodInliningUniqueCount              = methodInliningUniqueCounter             .getCount();
        int methodInliningTailrecursionCount       = methodInliningTailrecursionCounter      .getCount();
        int codeMergingCount                       = codeMergingCounter                      .getCount();
        int codeSimplificationVariableCount        = codeSimplificationVariableCounter       .getCount();
        int codeSimplificationArithmeticCount      = codeSimplificationArithmeticCounter     .getCount();
        int codeSimplificationCastCount            = codeSimplificationCastCounter           .getCount();
        int codeSimplificationFieldCount           = codeSimplificationFieldCounter          .getCount();
        int codeSimplificationBranchCount          = codeSimplificationBranchCounter         .getCount();
        int codeSimplificationObjectCount          = codeSimplificationObjectCounter         .getCount();
        int codeSimplificationStringCount          = codeSimplificationStringCounter         .getCount();
        int codeSimplificationMathCount            = codeSimplificationMathCounter           .getCount();
        int codeSimplificationAndroidMathCount     = codeSimplificationAndroidMathCounter    .getCount();
        int codeSimplificationAdvancedCount        = codeSimplificationAdvancedCounter       .getCount();
        int codeRemovalCount                       = deletedCounter                          .getCount() - addedCounter.getCount();
        int codeRemovalVariableCount               = codeRemovalVariableCounter              .getCount();
        int codeRemovalExceptionCount              = codeRemovalExceptionCounter             .getCount();
        int codeAllocationVariableCount            = codeAllocationVariableCounter           .getCount();

        // Forget about constant fields, parameters, and return values, if they
        // didn't lead to any useful optimizations. We want to avoid fruitless
        // additional optimization passes.
        if (codeSimplificationAdvancedCount == 0)
        {
            fieldPropagationValueCount        = 0;
            methodPropagationParameterCount   = 0;
            methodPropagationReturnvalueCount = 0;
        }

        logger.info("  Number of finalized classes:                   {}{}", classMarkingFinalCount,                  disabled(classMarkingFinal));
        logger.info("  Number of unboxed enum classes:                {}{}", classUnboxingEnumCount,                  disabled(classUnboxingEnum));
        logger.info("  Number of vertically merged classes:           {}{}", classMergingVerticalCount,               disabled(classMergingVertical));
        logger.info("  Number of horizontally merged classes:         {}{}", classMergingHorizontalCount,             disabled(classMergingHorizontal));
        logger.info("  Number of merged wrapper classes:              {}{}", classMergingWrapperCount,                disabled(classMergingWrapper));
        logger.info("  Number of removed write-only fields:           {}{}", fieldRemovalWriteonlyCount,              disabled(fieldRemovalWriteonly));
        logger.info("  Number of privatized fields:                   {}{}", fieldMarkingPrivateCount,                disabled(fieldMarkingPrivate));
        logger.info("  Number of generalized field accesses:          {}{}", fieldGeneralizationClassCount,           disabled(fieldGeneralizationClass));
        logger.info("  Number of specialized field types:             {}{}", fieldSpecializationTypeCount,            disabled(fieldSpecializationType));
        logger.info("  Number of inlined constant fields:             {}{}", fieldPropagationValueCount,              disabled(fieldPropagationValue));
        logger.info("  Number of privatized methods:                  {}{}", methodMarkingPrivateCount,               disabled(methodMarkingPrivate));
        logger.info("  Number of staticized methods:                  {}{}", methodMarkingStaticCount,                disabled(methodMarkingStatic));
        logger.info("  Number of finalized methods:                   {}{}", methodMarkingFinalCount,                 disabled(methodMarkingFinal));
        logger.info("  Number of desynchronized methods:              {}{}", methodMarkingSynchronizedCount,          disabled(methodMarkingSynchronized));
        logger.info("  Number of simplified method signatures:        {}{}", methodRemovalParameterCount1,            disabled(methodRemovalParameter));
        logger.info("  Number of removed method parameters:           {}{}", methodRemovalParameterCount2,            disabled(methodRemovalParameter));
        logger.info("  Number of generalized method invocations:      {}{}", methodGeneralizationClassCount,          disabled(methodGeneralizationClass));
        logger.info("  Number of specialized method parameter types:  {}{}", methodSpecializationParametertypeCount,  disabled(methodSpecializationParametertype));
        logger.info("  Number of specialized method return types:     {}{}", methodSpecializationReturntypeCount,     disabled(methodSpecializationReturntype));
        logger.info("  Number of inlined constant parameters:         {}{}", methodPropagationParameterCount,         disabled(methodPropagationParameter));
        logger.info("  Number of inlined constant return values:      {}{}", methodPropagationReturnvalueCount,       disabled(methodPropagationReturnvalue));
        logger.info("  Number of inlined short method calls:          {}{}", methodInliningShortCount,                disabled(methodInliningShort));
        logger.info("  Number of inlined unique method calls:         {}{}", methodInliningUniqueCount,               disabled(methodInliningUnique));
        logger.info("  Number of inlined tail recursion calls:        {}{}", methodInliningTailrecursionCount,        disabled(methodInliningTailrecursion));
        logger.info("  Number of merged code blocks:                  {}{}", codeMergingCount,                        disabled(codeMerging));
        logger.info("  Number of variable peephole optimizations:     {}{}", codeSimplificationVariableCount,         disabled(codeSimplificationVariable));
        logger.info("  Number of arithmetic peephole optimizations:   {}{}", codeSimplificationArithmeticCount,       disabled(codeSimplificationArithmetic));
        logger.info("  Number of cast peephole optimizations:         {}{}", codeSimplificationCastCount,             disabled(codeSimplificationCast));
        logger.info("  Number of field peephole optimizations:        {}{}", codeSimplificationFieldCount,            disabled(codeSimplificationField));
        logger.info("  Number of branch peephole optimizations:       {}{}", codeSimplificationBranchCount,           disabled(codeSimplificationBranch));
        logger.info("  Number of object peephole optimizations:       {}{}", codeSimplificationObjectCount,           disabled(codeSimplificationObject));
        logger.info("  Number of string peephole optimizations:       {}{}", codeSimplificationStringCount,           disabled(codeSimplificationString));
        logger.info("  Number of math peephole optimizations:         {}{}", codeSimplificationMathCount,             disabled(codeSimplificationMath));
        if (configuration.android)
        {
            logger.info("  Number of Android math peephole optimizations: {}{}", codeSimplificationAndroidMathCount,  disabled(codeSimplificationMath));
        }
        logger.info("  Number of simplified instructions:             {}{}", codeSimplificationAdvancedCount,         disabled(codeSimplificationAdvanced));
        logger.info("  Number of removed instructions:                {}{}", codeRemovalCount,                        disabled(codeRemovalAdvanced));
        logger.info("  Number of removed local variables:             {}{}", codeRemovalVariableCount,                disabled(codeRemovalVariable));
        logger.info("  Number of removed exception blocks:            {}{}", codeRemovalExceptionCount,               disabled(codeRemovalException));
        logger.info("  Number of optimized local variable frames:     {}{}", codeAllocationVariableCount,             disabled(codeAllocationVariable));

        moreOptimizationsPossible =
               classMarkingFinalCount                 > 0 ||
               classUnboxingEnumCount                 > 0 ||
               classMergingVerticalCount              > 0 ||
               classMergingHorizontalCount            > 0 ||
               classMergingWrapperCount               > 0 ||
               fieldRemovalWriteonlyCount             > 0 || // TODO: The write-only field counter may be optimistic about removal.
               fieldMarkingPrivateCount               > 0 ||
               methodMarkingPrivateCount              > 0 ||
               methodMarkingStaticCount               > 0 ||
               methodMarkingFinalCount                > 0 ||
               fieldGeneralizationClassCount          > 0 ||
               fieldSpecializationTypeCount           > 0 ||
               fieldPropagationValueCount             > 0 ||
               methodRemovalParameterCount1           > 0 ||
               methodRemovalParameterCount2           > 0 ||
               methodGeneralizationClassCount         > 0 ||
               methodSpecializationParametertypeCount > 0 ||
               methodSpecializationReturntypeCount    > 0 ||
               methodPropagationParameterCount        > 0 ||
               methodPropagationReturnvalueCount      > 0 ||
               methodInliningShortCount               > 0 ||
               methodInliningUniqueCount              > 0 ||
               methodInliningTailrecursionCount       > 0 ||
               codeMergingCount                       > 0 ||
               codeSimplificationVariableCount        > 0 ||
               codeSimplificationArithmeticCount      > 0 ||
               codeSimplificationCastCount            > 0 ||
               codeSimplificationFieldCount           > 0 ||
               codeSimplificationBranchCount          > 0 ||
               codeSimplificationObjectCount          > 0 ||
               codeSimplificationStringCount          > 0 ||
               codeSimplificationMathCount            > 0 ||
               codeSimplificationAndroidMathCount     > 0 ||
               codeSimplificationAdvancedCount        > 0 ||
               codeRemovalCount                       > 0 ||
               codeRemovalVariableCount               > 0 ||
               codeRemovalExceptionCount              > 0 ||
               codeAllocationVariableCount            > 0;
    }


    private List<InstructionVisitor> createPeepholeOptimizations(Configuration configuration,
                                             InstructionSequenceConstants sequences,
                                             BranchTargetFinder           branchTargetFinder,
                                             CodeAttributeEditor          codeAttributeEditor,
                                             InstructionCounter           codeSimplificationVariableCounter,
                                             InstructionCounter           codeSimplificationArithmeticCounter,
                                             InstructionCounter           codeSimplificationCastCounter,
                                             InstructionCounter           codeSimplificationFieldCounter,
                                             InstructionCounter           codeSimplificationBranchCounter,
                                             InstructionCounter           codeSimplificationObjectCounter,
                                             InstructionCounter           codeSimplificationStringCounter,
                                             InstructionCounter           codeSimplificationMathCounter,
                                             InstructionCounter           codeSimplificationAndroidMathCounter,
                                             InstructionCounter           fieldGeneralizationClassCounter,
                                             InstructionCounter           methodGeneralizationClassCounter)
    {
        List<InstructionVisitor> peepholeOptimizations = new ArrayList<>();

        if (codeSimplificationVariable)
        {
            // Peephole optimizations involving local variables.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                       sequences.VARIABLE_SEQUENCES,
                                                                       branchTargetFinder,
                                                                       codeAttributeEditor,
                                                                       codeSimplificationVariableCounter));
        }

        if (codeSimplificationArithmetic)
        {
            // Peephole optimizations involving arithmetic operations.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                       sequences.ARITHMETIC_SEQUENCES,
                                                                       branchTargetFinder,
                                                                       codeAttributeEditor,
                                                                       codeSimplificationArithmeticCounter));
        }

        if (codeSimplificationCast)
        {
            // Peephole optimizations involving cast operations.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                       sequences.CAST_SEQUENCES,
                                                                       branchTargetFinder,
                                                                       codeAttributeEditor,
                                                                       codeSimplificationCastCounter));
        }

        if (codeSimplificationField)
        {
            // Peephole optimizations involving fields.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                       sequences.FIELD_SEQUENCES,
                                                                       branchTargetFinder,
                                                                       codeAttributeEditor,
                                                                       codeSimplificationFieldCounter));
        }

        if (codeSimplificationBranch)
        {
            // Peephole optimizations involving branches.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                       sequences.BRANCH_SEQUENCES,
                                                                       branchTargetFinder,
                                                                       codeAttributeEditor,
                                                                       codeSimplificationBranchCounter));
        }

        if (codeSimplificationObject)
        {
            // Peephole optimizations involving objects.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                     sequences.OBJECT_SEQUENCES,
                                                                     branchTargetFinder,
                                                                     codeAttributeEditor,
                                                                     codeSimplificationObjectCounter));

            // Include optimizations of instance references on classes without
            // constructors.
            peepholeOptimizations.add(
                    new NoConstructorReferenceReplacer(codeAttributeEditor, codeSimplificationObjectCounter));
        }

        if (codeSimplificationString)
        {
            // Peephole optimizations involving branches.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                       sequences.STRING_SEQUENCES,
                                                                       branchTargetFinder, codeAttributeEditor,
                                                                       codeSimplificationStringCounter));
        }

        if (codeSimplificationMath)
        {
            // Peephole optimizations involving math.
            peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                       sequences.MATH_SEQUENCES,
                                                                       branchTargetFinder,
                                                                       codeAttributeEditor,
                                                                       codeSimplificationMathCounter));

            if (configuration.android)
            {
                peepholeOptimizations.add(new InstructionSequencesReplacer(sequences.CONSTANTS,
                                                                           sequences.MATH_ANDROID_SEQUENCES,
                                                                           branchTargetFinder,
                                                                           codeAttributeEditor,
                                                                           codeSimplificationAndroidMathCounter));
            }
        }

        if(codeSimplificationBranch)
        {
            // Include optimization of branches to branches and returns.
            peepholeOptimizations.add(
                new GotoGotoReplacer(codeAttributeEditor, codeSimplificationBranchCounter));
            peepholeOptimizations.add(
                new GotoReturnReplacer(codeAttributeEditor, codeSimplificationBranchCounter));
        }

        if (fieldGeneralizationClass ||
            methodGeneralizationClass)
        {
            // Generalize the target classes of method invocations, to
            // reduce the number of descriptors.
            peepholeOptimizations.add(
                new MemberReferenceGeneralizer(fieldGeneralizationClass,
                                               methodGeneralizationClass,
                                               codeAttributeEditor,
                                               fieldGeneralizationClassCounter,
                                               methodGeneralizationClassCounter));
        }


        return peepholeOptimizations;
    }


    /**
     * Returns a String indicating whether the given flag is enabled or
     * disabled.
     */
    private String disabled(boolean flag)
    {
        return flag ? "" : "   (disabled)";
    }


    /**
     * Returns a String indicating whether the given flags are enabled or
     * disabled.
     */
    private String disabled(boolean flag1, boolean flag2)
    {
        return flag1 && flag2 ? "" :
               flag1 || flag2 ? "   (partially disabled)" :
                                "   (disabled)";
    }
}
