package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.BootstrapMethodsAttribute;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
 *
 * The visitBootstrapMethodsAttribute method performs the following:
 * 1. Accepts the checker as a visitor for all bootstrap method entries
 * 2. This allows the checker to unmark simple enum classes referenced in bootstrap methods
 *
 * This method is responsible for checking bootstrap methods that may reference simple enums
 * and ensuring they are marked as complex if they are used in complex ways.
 */
public class SimpleEnumUseCheckerClaude_visitBootstrapMethodsAttributeTest {

    private SimpleEnumUseChecker checker;
    private ProgramClass programClass;
    private BootstrapMethodsAttribute bootstrapMethodsAttribute;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        programClass = new ProgramClass();
        bootstrapMethodsAttribute = spy(new BootstrapMethodsAttribute());
    }

    /**
     * Tests that visitBootstrapMethodsAttribute calls bootstrapMethodEntriesAccept.
     * This is the main behavior of the method.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_callsBootstrapMethodEntriesAccept() {
        // Act
        checker.visitBootstrapMethodsAttribute(programClass, bootstrapMethodsAttribute);

        // Assert - should call bootstrapMethodEntriesAccept exactly once with the class and checker
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntriesAccept(programClass, checker);
    }

    /**
     * Tests that visitBootstrapMethodsAttribute doesn't throw exception with minimal setup.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_minimalSetup_doesNotThrowException() {
        // Arrange
        BootstrapMethodsAttribute minimalAttribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBootstrapMethodsAttribute(programClass, minimalAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called multiple times.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitBootstrapMethodsAttribute(programClass, attribute);
            checker.visitBootstrapMethodsAttribute(programClass, attribute);
            checker.visitBootstrapMethodsAttribute(programClass, attribute);
        });
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with different classes.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_differentClasses_doesNotThrowException() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitBootstrapMethodsAttribute(class1, attribute);
            checker.visitBootstrapMethodsAttribute(class2, attribute);
            checker.visitBootstrapMethodsAttribute(class3, attribute);
        });
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with different attributes.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_differentAttributes_doesNotThrowException() {
        // Arrange
        BootstrapMethodsAttribute attr1 = new BootstrapMethodsAttribute();
        BootstrapMethodsAttribute attr2 = new BootstrapMethodsAttribute();
        BootstrapMethodsAttribute attr3 = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitBootstrapMethodsAttribute(programClass, attr1);
            checker.visitBootstrapMethodsAttribute(programClass, attr2);
            checker.visitBootstrapMethodsAttribute(programClass, attr3);
        });
    }

    /**
     * Tests that visitBootstrapMethodsAttribute passes correct arguments to bootstrapMethodEntriesAccept.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesCorrectArguments() {
        // Arrange
        ProgramClass specificClass = new ProgramClass();

        // Act
        checker.visitBootstrapMethodsAttribute(specificClass, bootstrapMethodsAttribute);

        // Assert - verify correct class is passed
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntriesAccept(eq(specificClass), eq(checker));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with multiple checker instances.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_multipleCheckers_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitBootstrapMethodsAttribute(programClass, attribute);
            checker2.visitBootstrapMethodsAttribute(programClass, attribute);
            checker3.visitBootstrapMethodsAttribute(programClass, attribute);
        });
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called through AttributeVisitor interface.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_throughAttributeVisitorInterface_worksCorrectly() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = checker;
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitBootstrapMethodsAttribute(programClass, attribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with custom PartialEvaluator works correctly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withCustomPartialEvaluator_worksCorrectly() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> customChecker.visitBootstrapMethodsAttribute(programClass, attribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute handles successive calls correctly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_successiveCalls_eachCallsBootstrapMethodEntriesAccept() {
        // Arrange
        BootstrapMethodsAttribute attr1 = spy(new BootstrapMethodsAttribute());
        BootstrapMethodsAttribute attr2 = spy(new BootstrapMethodsAttribute());

        // Act
        checker.visitBootstrapMethodsAttribute(programClass, attr1);
        checker.visitBootstrapMethodsAttribute(programClass, attr2);

        // Assert - both attributes should have bootstrapMethodEntriesAccept called
        verify(attr1, times(1)).bootstrapMethodEntriesAccept(programClass, checker);
        verify(attr2, times(1)).bootstrapMethodEntriesAccept(programClass, checker);
    }

    /**
     * Tests that visitBootstrapMethodsAttribute handles rapid successive calls.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_rapidSuccessiveCalls_doesNotThrowException() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                checker.visitBootstrapMethodsAttribute(programClass, attribute);
            }
        });
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works correctly with different class types.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentClassTypes_worksCorrectly() {
        // Arrange
        ProgramClass regularClass = new ProgramClass();
        regularClass.u2accessFlags = 0;

        ProgramClass publicClass = new ProgramClass();
        publicClass.u2accessFlags = AccessConstants.PUBLIC;

        ProgramClass annotationClass = new ProgramClass();
        annotationClass.u2accessFlags = AccessConstants.ANNOTATION;

        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitBootstrapMethodsAttribute(regularClass, attribute);
            checker.visitBootstrapMethodsAttribute(publicClass, attribute);
            checker.visitBootstrapMethodsAttribute(annotationClass, attribute);
        });
    }

    /**
     * Tests that visitBootstrapMethodsAttribute maintains checker state correctly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_maintainsCheckerState_doesNotCorruptState() {
        // Arrange
        BootstrapMethodsAttribute attr1 = new BootstrapMethodsAttribute();
        BootstrapMethodsAttribute attr2 = new BootstrapMethodsAttribute();

        // Act - call multiple times
        checker.visitBootstrapMethodsAttribute(programClass, attr1);
        checker.visitBootstrapMethodsAttribute(programClass, attr2);
        checker.visitBootstrapMethodsAttribute(programClass, attr1); // revisit first attribute

        // Assert - should not throw, indicating state is maintained correctly
        assertDoesNotThrow(() -> checker.visitBootstrapMethodsAttribute(programClass, attr2));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute is called correctly from visitProgramClass.
     * This tests integration with the parent calling context.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_calledFromVisitProgramClass_worksCorrectly() {
        // Arrange
        ProgramClass classWithBootstrapAttribute = spy(new ProgramClass());
        classWithBootstrapAttribute.u2accessFlags = 0;

        // Act
        checker.visitProgramClass(classWithBootstrapAttribute);

        // Assert - visitProgramClass should call attributesAccept which may invoke this method
        verify(classWithBootstrapAttribute, times(1)).attributesAccept(checker);
    }

    /**
     * Tests that visitBootstrapMethodsAttribute handles null-like scenarios gracefully.
     * Note: Since we can't pass null (would violate method contract), we test with
     * default initialized objects.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDefaultInitializedObjects_doesNotThrowException() {
        // Arrange
        ProgramClass defaultClass = new ProgramClass();
        BootstrapMethodsAttribute defaultAttribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBootstrapMethodsAttribute(defaultClass, defaultAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute behavior is consistent across multiple invocations.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_consistentBehavior_acrossMultipleInvocations() {
        // Arrange
        BootstrapMethodsAttribute attr1 = spy(new BootstrapMethodsAttribute());
        BootstrapMethodsAttribute attr2 = spy(new BootstrapMethodsAttribute());
        BootstrapMethodsAttribute attr3 = spy(new BootstrapMethodsAttribute());

        // Act
        checker.visitBootstrapMethodsAttribute(programClass, attr1);
        checker.visitBootstrapMethodsAttribute(programClass, attr2);
        checker.visitBootstrapMethodsAttribute(programClass, attr3);

        // Assert - all should have bootstrapMethodEntriesAccept called exactly once
        verify(attr1, times(1)).bootstrapMethodEntriesAccept(programClass, checker);
        verify(attr2, times(1)).bootstrapMethodEntriesAccept(programClass, checker);
        verify(attr3, times(1)).bootstrapMethodEntriesAccept(programClass, checker);
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can handle interleaved calls with different classes and attributes.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_interleavedCalls_allProcessCorrectly() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        BootstrapMethodsAttribute attr1 = spy(new BootstrapMethodsAttribute());
        BootstrapMethodsAttribute attr2 = spy(new BootstrapMethodsAttribute());

        // Act
        checker.visitBootstrapMethodsAttribute(class1, attr1);
        checker.visitBootstrapMethodsAttribute(class2, attr2);
        checker.visitBootstrapMethodsAttribute(class1, attr2);
        checker.visitBootstrapMethodsAttribute(class2, attr1);

        // Assert
        verify(attr1, times(2)).bootstrapMethodEntriesAccept(any(Clazz.class), eq(checker));
        verify(attr2, times(2)).bootstrapMethodEntriesAccept(any(Clazz.class), eq(checker));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with enum classes.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withEnumClass_worksCorrectly() {
        // Arrange
        ProgramClass enumClass = new ProgramClass();
        enumClass.u2accessFlags = AccessConstants.ENUM;
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBootstrapMethodsAttribute(enumClass, attribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with interface classes.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withInterfaceClass_worksCorrectly() {
        // Arrange
        ProgramClass interfaceClass = new ProgramClass();
        interfaceClass.u2accessFlags = AccessConstants.INTERFACE;
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBootstrapMethodsAttribute(interfaceClass, attribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with abstract classes.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withAbstractClass_worksCorrectly() {
        // Arrange
        ProgramClass abstractClass = new ProgramClass();
        abstractClass.u2accessFlags = AccessConstants.ABSTRACT;
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBootstrapMethodsAttribute(abstractClass, attribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with final classes.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withFinalClass_worksCorrectly() {
        // Arrange
        ProgramClass finalClass = new ProgramClass();
        finalClass.u2accessFlags = AccessConstants.FINAL;
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBootstrapMethodsAttribute(finalClass, attribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with various class flag combinations.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_variousClassFlags_allWorkCorrectly() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        ProgramClass publicFinalClass = new ProgramClass();
        publicFinalClass.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.FINAL;

        ProgramClass publicAbstractClass = new ProgramClass();
        publicAbstractClass.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.ABSTRACT;

        ProgramClass syntheticClass = new ProgramClass();
        syntheticClass.u2accessFlags = AccessConstants.SYNTHETIC;

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitBootstrapMethodsAttribute(publicFinalClass, attribute);
            checker.visitBootstrapMethodsAttribute(publicAbstractClass, attribute);
            checker.visitBootstrapMethodsAttribute(syntheticClass, attribute);
        });
    }
}
