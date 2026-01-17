package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DescriptorKeepChecker#visitProgramField(ProgramClass, ProgramField)}.
 *
 * The visitProgramField method in DescriptorKeepChecker is currently a no-op method.
 * All logic is commented out with a note that field types are not checked because
 * they are not required for introspection. These tests verify the method can be
 * invoked without errors under various conditions.
 */
public class DescriptorKeepCheckerClaude_visitProgramFieldTest {

    private DescriptorKeepChecker descriptorKeepChecker;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ProgramClass programClass;
    private ProgramField programField;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        descriptorKeepChecker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Create mock instances for ProgramClass and ProgramField
        programClass = mock(ProgramClass.class);
        programField = mock(ProgramField.class);
    }

    /**
     * Tests that visitProgramField can be called with valid mock objects without throwing exceptions.
     * Since the method is a no-op, it should simply return without doing anything.
     */
    @Test
    public void testVisitProgramField_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> descriptorKeepChecker.visitProgramField(programClass, programField));
    }

    /**
     * Tests visitProgramField with null program class.
     * Since the method doesn't access the parameters, null should not cause issues.
     */
    @Test
    public void testVisitProgramField_withNullProgramClass() {
        // Act & Assert - should not throw any exception since method is no-op
        assertDoesNotThrow(() -> descriptorKeepChecker.visitProgramField(null, programField));
    }

    /**
     * Tests visitProgramField with null program field.
     * Since the method doesn't access the parameters, null should not cause issues.
     */
    @Test
    public void testVisitProgramField_withNullProgramField() {
        // Act & Assert - should not throw any exception since method is no-op
        assertDoesNotThrow(() -> descriptorKeepChecker.visitProgramField(programClass, null));
    }

    /**
     * Tests visitProgramField with both null parameters.
     * Since the method doesn't access the parameters, nulls should not cause issues.
     */
    @Test
    public void testVisitProgramField_withBothParametersNull() {
        // Act & Assert - should not throw any exception since method is no-op
        assertDoesNotThrow(() -> descriptorKeepChecker.visitProgramField(null, null));
    }

    /**
     * Tests that visitProgramField can be called multiple times on the same instance.
     */
    @Test
    public void testVisitProgramField_calledMultipleTimes() {
        // Act & Assert - call multiple times
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitProgramField(programClass, programField);
            descriptorKeepChecker.visitProgramField(programClass, programField);
            descriptorKeepChecker.visitProgramField(programClass, programField);
        });
    }

    /**
     * Tests visitProgramField with different program classes sequentially.
     */
    @Test
    public void testVisitProgramField_withDifferentProgramClasses() {
        // Arrange
        ProgramClass programClass1 = mock(ProgramClass.class);
        ProgramClass programClass2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitProgramField(programClass1, programField);
            descriptorKeepChecker.visitProgramField(programClass2, programField);
        });
    }

    /**
     * Tests visitProgramField with different program fields sequentially.
     */
    @Test
    public void testVisitProgramField_withDifferentProgramFields() {
        // Arrange
        ProgramField field1 = mock(ProgramField.class);
        ProgramField field2 = mock(ProgramField.class);
        ProgramField field3 = mock(ProgramField.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitProgramField(programClass, field1);
            descriptorKeepChecker.visitProgramField(programClass, field2);
            descriptorKeepChecker.visitProgramField(programClass, field3);
        });
    }

    /**
     * Tests visitProgramField after calling visitProgramMethod.
     * Verifies that the visitor can handle different visit methods in sequence.
     */
    @Test
    public void testVisitProgramField_afterVisitProgramMethod() {
        // Arrange
        ProgramClass methodClass = mock(ProgramClass.class);
        proguard.classfile.ProgramMethod programMethod = mock(proguard.classfile.ProgramMethod.class);
        when(methodClass.getName()).thenReturn("com/example/TestClass");
        when(programMethod.getName(methodClass)).thenReturn("testMethod");
        when(programMethod.getDescriptor(methodClass)).thenReturn("()V");

        // Act & Assert - call visitProgramMethod then visitProgramField
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitProgramMethod(methodClass, programMethod);
            descriptorKeepChecker.visitProgramField(programClass, programField);
        });
    }

    /**
     * Tests that DescriptorKeepChecker with null warning printer can still call visitProgramField.
     * Since visitProgramField doesn't use the warning printer, this should work.
     */
    @Test
    public void testVisitProgramField_withNullWarningPrinter() {
        // Arrange
        DescriptorKeepChecker checkerWithNullPrinter = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                null
        );

        // Act & Assert
        assertDoesNotThrow(() -> checkerWithNullPrinter.visitProgramField(programClass, programField));
    }

    /**
     * Tests that DescriptorKeepChecker with null class pools can still call visitProgramField.
     * Since visitProgramField doesn't access the class pools, this should work.
     */
    @Test
    public void testVisitProgramField_withNullClassPools() {
        // Arrange
        DescriptorKeepChecker checkerWithNullPools = new DescriptorKeepChecker(
                null,
                null,
                warningPrinter
        );

        // Act & Assert - should not throw since method doesn't use class pools
        assertDoesNotThrow(() -> checkerWithNullPools.visitProgramField(programClass, programField));
    }

    /**
     * Tests visitProgramField with the same field object multiple times.
     * Verifies idempotency of the method.
     */
    @Test
    public void testVisitProgramField_sameProgramFieldMultipleTimes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                descriptorKeepChecker.visitProgramField(programClass, programField);
            }
        });
    }

    /**
     * Tests visitProgramField with alternating null and non-null parameters.
     */
    @Test
    public void testVisitProgramField_alternatingNullAndNonNull() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitProgramField(programClass, programField);
            descriptorKeepChecker.visitProgramField(null, programField);
            descriptorKeepChecker.visitProgramField(programClass, null);
            descriptorKeepChecker.visitProgramField(null, null);
            descriptorKeepChecker.visitProgramField(programClass, programField);
        });
    }

    /**
     * Tests that visitProgramField does not modify any state.
     * Verifies the method truly is a no-op by checking no interactions occur.
     */
    @Test
    public void testVisitProgramField_doesNotInteractWithParameters() {
        // Arrange - create strict mocks to verify no interactions
        ProgramClass strictProgramClass = mock(ProgramClass.class);
        ProgramField strictProgramField = mock(ProgramField.class);

        // Act
        descriptorKeepChecker.visitProgramField(strictProgramClass, strictProgramField);

        // Assert - verify no methods were called on the mocks
        verifyNoInteractions(strictProgramClass);
        verifyNoInteractions(strictProgramField);
    }

    /**
     * Tests visitProgramField on multiple DescriptorKeepChecker instances.
     * Verifies that different instances behave consistently.
     */
    @Test
    public void testVisitProgramField_multipleCheckerInstances() {
        // Arrange
        DescriptorKeepChecker checker1 = new DescriptorKeepChecker(
                new ClassPool(), new ClassPool(), mock(WarningPrinter.class)
        );
        DescriptorKeepChecker checker2 = new DescriptorKeepChecker(
                new ClassPool(), new ClassPool(), mock(WarningPrinter.class)
        );

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitProgramField(programClass, programField);
            checker2.visitProgramField(programClass, programField);
        });
    }

    /**
     * Tests visitProgramField with real (non-mock) class pools.
     * Verifies behavior with actual ClassPool instances.
     */
    @Test
    public void testVisitProgramField_withRealClassPools() {
        // Arrange
        ClassPool realProgramPool = new ClassPool();
        ClassPool realLibraryPool = new ClassPool();
        DescriptorKeepChecker checkerWithRealPools = new DescriptorKeepChecker(
                realProgramPool,
                realLibraryPool,
                warningPrinter
        );

        // Act & Assert
        assertDoesNotThrow(() -> checkerWithRealPools.visitProgramField(programClass, programField));
    }
}
