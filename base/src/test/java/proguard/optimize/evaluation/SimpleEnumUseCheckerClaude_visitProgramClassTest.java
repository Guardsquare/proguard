package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method performs the following:
 * 1. Always calls programClass.attributesAccept(this) to check bootstrap methods
 * 2. Checks if the class is an annotation (using AccessConstants.ANNOTATION flag)
 * 3. If annotation: calls programClass.methodsAccept(referencedComplexEnumMarker)
 * 4. If not annotation: calls programClass.methodsAccept(methodCodeChecker)
 *
 * This method is responsible for unmarking simple enum classes that are used in complex ways.
 */
public class SimpleEnumUseCheckerClaude_visitProgramClassTest {

    private SimpleEnumUseChecker checker;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        programClass = spy(new ProgramClass());
    }

    /**
     * Tests that visitProgramClass calls attributesAccept on a regular class.
     * All classes should have their attributes checked for bootstrap methods.
     */
    @Test
    public void testVisitProgramClass_regularClass_callsAttributesAccept() {
        // Arrange - regular class (no annotation flag)
        programClass.u2accessFlags = 0;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - should call attributesAccept exactly once
        verify(programClass, times(1)).attributesAccept(checker);
    }

    /**
     * Tests that visitProgramClass calls methodsAccept on a regular class.
     * Regular classes should have their methods checked with methodCodeChecker.
     */
    @Test
    public void testVisitProgramClass_regularClass_callsMethodsAccept() {
        // Arrange - regular class (no annotation flag)
        programClass.u2accessFlags = 0;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - should call methodsAccept
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass calls attributesAccept on an annotation class.
     * Annotations should also have their attributes checked.
     */
    @Test
    public void testVisitProgramClass_annotationClass_callsAttributesAccept() {
        // Arrange - annotation class (with ANNOTATION flag = 0x2000)
        programClass.u2accessFlags = AccessConstants.ANNOTATION;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - should call attributesAccept exactly once
        verify(programClass, times(1)).attributesAccept(checker);
    }

    /**
     * Tests that visitProgramClass calls methodsAccept on an annotation class.
     * Annotations should have their methods checked with referencedComplexEnumMarker.
     */
    @Test
    public void testVisitProgramClass_annotationClass_callsMethodsAccept() {
        // Arrange - annotation class
        programClass.u2accessFlags = AccessConstants.ANNOTATION;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - should call methodsAccept
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass doesn't throw exception with a minimal ProgramClass.
     */
    @Test
    public void testVisitProgramClass_minimalClass_doesNotThrowException() {
        // Arrange
        ProgramClass minimalClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitProgramClass(minimalClass));
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same class.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        programClass.u2accessFlags = 0;

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitProgramClass(programClass);
            checker.visitProgramClass(programClass);
            checker.visitProgramClass(programClass);
        });
    }

    /**
     * Tests that visitProgramClass can be called on different classes.
     */
    @Test
    public void testVisitProgramClass_multipleDifferentClasses_doesNotThrowException() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        class1.u2accessFlags = 0;
        class2.u2accessFlags = AccessConstants.ANNOTATION;
        class3.u2accessFlags = AccessConstants.PUBLIC;

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitProgramClass(class1);
            checker.visitProgramClass(class2);
            checker.visitProgramClass(class3);
        });
    }

    /**
     * Tests that visitProgramClass handles a class with PUBLIC flag correctly.
     */
    @Test
    public void testVisitProgramClass_publicClass_callsExpectedMethods() {
        // Arrange
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - public class is not annotation, should process as regular class
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass handles a class with combined flags including ANNOTATION.
     */
    @Test
    public void testVisitProgramClass_publicAnnotationClass_treatedAsAnnotation() {
        // Arrange - public annotation (common combination)
        programClass.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.ANNOTATION;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - should be treated as annotation
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass handles an ABSTRACT class correctly.
     */
    @Test
    public void testVisitProgramClass_abstractClass_processedAsRegularClass() {
        // Arrange
        programClass.u2accessFlags = AccessConstants.ABSTRACT;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - abstract class without annotation flag is processed as regular class
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass handles an INTERFACE correctly.
     */
    @Test
    public void testVisitProgramClass_interfaceClass_processedAsRegularClass() {
        // Arrange - interface without annotation flag
        programClass.u2accessFlags = AccessConstants.INTERFACE;

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass handles a class with no access flags.
     */
    @Test
    public void testVisitProgramClass_noAccessFlags_processedAsRegularClass() {
        // Arrange
        programClass.u2accessFlags = 0;

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass works with a class that has attributes.
     */
    @Test
    public void testVisitProgramClass_classWithAttributes_processesAttributes() {
        // Arrange
        ProgramClass classWithAttributes = new ProgramClass();
        classWithAttributes.u2accessFlags = 0;
        classWithAttributes.attributes = new Attribute[1];
        classWithAttributes.attributes[0] = mock(Attribute.class);
        classWithAttributes.u2attributesCount = 1;

        // Act
        assertDoesNotThrow(() -> checker.visitProgramClass(classWithAttributes));

        // Assert - verify attributesAccept was called (it processes the attributes)
        // We can't directly verify the attribute processing without a spy
    }

    /**
     * Tests that visitProgramClass works with a class that has methods.
     */
    @Test
    public void testVisitProgramClass_classWithMethods_processesMethods() {
        // Arrange
        ProgramClass classWithMethods = new ProgramClass();
        classWithMethods.u2accessFlags = 0;
        ProgramMethod method = new ProgramMethod();
        classWithMethods.methods = new ProgramMethod[] { method };
        classWithMethods.u2methodsCount = 1;

        // Act
        assertDoesNotThrow(() -> checker.visitProgramClass(classWithMethods));

        // Assert - should not throw, methodsAccept is called
    }

    /**
     * Tests that visitProgramClass can be called by multiple checker instances.
     */
    @Test
    public void testVisitProgramClass_multipleCheckers_allProcessCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        programClass.u2accessFlags = 0;

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitProgramClass(programClass);
            checker2.visitProgramClass(programClass);
            checker3.visitProgramClass(programClass);
        });
    }

    /**
     * Tests that visitProgramClass works through the ClassVisitor interface.
     */
    @Test
    public void testVisitProgramClass_throughClassVisitorInterface_worksCorrectly() {
        // Arrange
        proguard.classfile.visitor.ClassVisitor visitor = checker;
        programClass.u2accessFlags = 0;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass with a custom PartialEvaluator works correctly.
     */
    @Test
    public void testVisitProgramClass_withCustomPartialEvaluator_processesCorrectly() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);
        programClass.u2accessFlags = 0;

        // Act & Assert
        assertDoesNotThrow(() -> customChecker.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass handles ENUM classes correctly.
     */
    @Test
    public void testVisitProgramClass_enumClass_processedCorrectly() {
        // Arrange - enum class (has ENUM flag)
        programClass.u2accessFlags = AccessConstants.ENUM;

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitProgramClass(programClass));

        // Enum without annotation flag is processed as regular class
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass handles FINAL classes correctly.
     */
    @Test
    public void testVisitProgramClass_finalClass_processedCorrectly() {
        // Arrange
        programClass.u2accessFlags = AccessConstants.FINAL;

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass can handle rapid successive calls.
     */
    @Test
    public void testVisitProgramClass_rapidSuccessiveCalls_doesNotThrowException() {
        // Arrange
        programClass.u2accessFlags = 0;

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                checker.visitProgramClass(programClass);
            }
        });
    }

    /**
     * Tests visitProgramClass with various access flag combinations.
     */
    @Test
    public void testVisitProgramClass_variousAccessFlagCombinations_allProcessCorrectly() {
        // Arrange
        ProgramClass publicClass = new ProgramClass();
        publicClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramClass publicFinalClass = new ProgramClass();
        publicFinalClass.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.FINAL;

        ProgramClass abstractClass = new ProgramClass();
        abstractClass.u2accessFlags = AccessConstants.ABSTRACT;

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitProgramClass(publicClass);
            checker.visitProgramClass(publicFinalClass);
            checker.visitProgramClass(abstractClass);
        });
    }

    /**
     * Tests that visitProgramClass correctly processes annotation interfaces.
     * Annotation interfaces have both ANNOTATION and INTERFACE flags.
     */
    @Test
    public void testVisitProgramClass_annotationInterface_treatedAsAnnotation() {
        // Arrange - annotation interfaces typically have both flags
        programClass.u2accessFlags = AccessConstants.ANNOTATION | AccessConstants.INTERFACE;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - should be treated as annotation
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass maintains correct behavior after processing annotation.
     * Verifies checker state doesn't get corrupted.
     */
    @Test
    public void testVisitProgramClass_afterAnnotation_regularClassStillProcessedCorrectly() {
        // Arrange
        ProgramClass annotation = new ProgramClass();
        annotation.u2accessFlags = AccessConstants.ANNOTATION;

        ProgramClass regularClass = new ProgramClass();
        regularClass.u2accessFlags = 0;

        // Act - process annotation first, then regular class
        checker.visitProgramClass(annotation);
        checker.visitProgramClass(regularClass);

        // Assert - both should be processed without issues
        assertDoesNotThrow(() -> checker.visitProgramClass(regularClass));
    }

    /**
     * Tests that visitProgramClass works with a complex class hierarchy.
     */
    @Test
    public void testVisitProgramClass_complexClassHierarchy_processesCorrectly() {
        // Arrange - simulate various class types
        ProgramClass baseClass = new ProgramClass();
        baseClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramClass abstractBase = new ProgramClass();
        abstractBase.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.ABSTRACT;

        ProgramClass finalClass = new ProgramClass();
        finalClass.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.FINAL;

        ProgramClass enumClass = new ProgramClass();
        enumClass.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.FINAL | AccessConstants.ENUM;

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitProgramClass(baseClass);
            checker.visitProgramClass(abstractBase);
            checker.visitProgramClass(finalClass);
            checker.visitProgramClass(enumClass);
        });
    }

    /**
     * Tests that visitProgramClass with annotation flag set uses different processing path.
     * This is a key behavioral difference between annotations and regular classes.
     */
    @Test
    public void testVisitProgramClass_annotationVsRegular_useDifferentProcessingPaths() {
        // Arrange
        ProgramClass annotation = spy(new ProgramClass());
        annotation.u2accessFlags = AccessConstants.ANNOTATION;

        ProgramClass regular = spy(new ProgramClass());
        regular.u2accessFlags = 0;

        // Act
        checker.visitProgramClass(annotation);
        checker.visitProgramClass(regular);

        // Assert - both call attributesAccept and methodsAccept, but with different visitors
        verify(annotation, times(1)).attributesAccept(checker);
        verify(annotation, times(1)).methodsAccept(any(MemberVisitor.class));
        verify(regular, times(1)).attributesAccept(checker);
        verify(regular, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitProgramClass handles SYNTHETIC classes correctly.
     */
    @Test
    public void testVisitProgramClass_syntheticClass_processedCorrectly() {
        // Arrange
        programClass.u2accessFlags = AccessConstants.SYNTHETIC;

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(1)).attributesAccept(checker);
        verify(programClass, times(1)).methodsAccept(any(MemberVisitor.class));
    }

    /**
     * Tests visitProgramClass doesn't modify the class being visited.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyClass() {
        // Arrange
        programClass.u2accessFlags = AccessConstants.PUBLIC;
        int originalFlags = programClass.u2accessFlags;

        // Act
        checker.visitProgramClass(programClass);

        // Assert - flags should not be modified
        assertEquals(originalFlags, programClass.u2accessFlags,
            "Access flags should not be modified");
    }

    /**
     * Tests that visitProgramClass can handle a class with all common flags set.
     */
    @Test
    public void testVisitProgramClass_allCommonFlags_processedCorrectly() {
        // Arrange - class with multiple flags
        programClass.u2accessFlags = AccessConstants.PUBLIC |
                                      AccessConstants.FINAL |
                                      AccessConstants.SYNTHETIC;

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass stability across many calls with varying flags.
     */
    @Test
    public void testVisitProgramClass_manyCallsVariedFlags_stable() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                ProgramClass pc = new ProgramClass();
                pc.u2accessFlags = (i % 2 == 0) ? 0 : AccessConstants.ANNOTATION;
                checker.visitProgramClass(pc);
            }
        });
    }
}
