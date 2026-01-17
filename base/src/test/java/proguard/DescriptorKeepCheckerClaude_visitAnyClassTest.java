package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DescriptorKeepChecker#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in DescriptorKeepChecker is a no-op method.
 * It serves as the default implementation from the ClassVisitor interface
 * and is only called for non-program classes (e.g., library classes).
 * These tests verify the method can be invoked without errors under various conditions.
 */
public class DescriptorKeepCheckerClaude_visitAnyClassTest {

    private DescriptorKeepChecker descriptorKeepChecker;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private Clazz clazz;

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

        // Create mock instance for Clazz
        clazz = mock(Clazz.class);
    }

    /**
     * Tests that visitAnyClass can be called with a valid mock object without throwing exceptions.
     * Since the method is a no-op, it should simply return without doing anything.
     */
    @Test
    public void testVisitAnyClass_withValidMock_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> descriptorKeepChecker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with null clazz parameter.
     * Since the method doesn't access the parameter, null should not cause issues.
     */
    @Test
    public void testVisitAnyClass_withNullClazz() {
        // Act & Assert - should not throw any exception since method is no-op
        assertDoesNotThrow(() -> descriptorKeepChecker.visitAnyClass(null));
    }

    /**
     * Tests that visitAnyClass can be called multiple times on the same instance.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes() {
        // Act & Assert - call multiple times
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitAnyClass(clazz);
            descriptorKeepChecker.visitAnyClass(clazz);
            descriptorKeepChecker.visitAnyClass(clazz);
        });
    }

    /**
     * Tests visitAnyClass with different Clazz objects sequentially.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitAnyClass(clazz1);
            descriptorKeepChecker.visitAnyClass(clazz2);
            descriptorKeepChecker.visitAnyClass(clazz3);
        });
    }

    /**
     * Tests visitAnyClass with alternating null and non-null parameters.
     */
    @Test
    public void testVisitAnyClass_alternatingNullAndNonNull() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitAnyClass(clazz);
            descriptorKeepChecker.visitAnyClass(null);
            descriptorKeepChecker.visitAnyClass(clazz);
            descriptorKeepChecker.visitAnyClass(null);
            descriptorKeepChecker.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass does not modify any state.
     * Verifies the method truly is a no-op by checking no interactions occur.
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithParameter() {
        // Arrange - create strict mock to verify no interactions
        Clazz strictClazz = mock(Clazz.class);

        // Act
        descriptorKeepChecker.visitAnyClass(strictClazz);

        // Assert - verify no methods were called on the mock
        verifyNoInteractions(strictClazz);
    }

    /**
     * Tests visitAnyClass on multiple DescriptorKeepChecker instances.
     * Verifies that different instances behave consistently.
     */
    @Test
    public void testVisitAnyClass_multipleCheckerInstances() {
        // Arrange
        DescriptorKeepChecker checker1 = new DescriptorKeepChecker(
                new ClassPool(), new ClassPool(), mock(WarningPrinter.class)
        );
        DescriptorKeepChecker checker2 = new DescriptorKeepChecker(
                new ClassPool(), new ClassPool(), mock(WarningPrinter.class)
        );

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitAnyClass(clazz);
            checker2.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that DescriptorKeepChecker with null warning printer can still call visitAnyClass.
     * Since visitAnyClass doesn't use the warning printer, this should work.
     */
    @Test
    public void testVisitAnyClass_withNullWarningPrinter() {
        // Arrange
        DescriptorKeepChecker checkerWithNullPrinter = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                null
        );

        // Act & Assert
        assertDoesNotThrow(() -> checkerWithNullPrinter.visitAnyClass(clazz));
    }

    /**
     * Tests that DescriptorKeepChecker with null class pools can still call visitAnyClass.
     * Since visitAnyClass doesn't access the class pools, this should work.
     */
    @Test
    public void testVisitAnyClass_withNullClassPools() {
        // Arrange
        DescriptorKeepChecker checkerWithNullPools = new DescriptorKeepChecker(
                null,
                null,
                warningPrinter
        );

        // Act & Assert - should not throw since method doesn't use class pools
        assertDoesNotThrow(() -> checkerWithNullPools.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with the same Clazz object multiple times.
     * Verifies idempotency of the method.
     */
    @Test
    public void testVisitAnyClass_sameClazzMultipleTimes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                descriptorKeepChecker.visitAnyClass(clazz);
            }
        });
    }

    /**
     * Tests visitAnyClass after calling other visitor methods.
     * Verifies that the visitor can handle different visit methods in sequence.
     */
    @Test
    public void testVisitAnyClass_afterOtherVisitorMethods() {
        // Arrange
        proguard.classfile.ProgramClass programClass = mock(proguard.classfile.ProgramClass.class);
        proguard.classfile.ProgramField programField = mock(proguard.classfile.ProgramField.class);

        // Act & Assert - call visitProgramField then visitAnyClass
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitProgramField(programClass, programField);
            descriptorKeepChecker.visitAnyClass(clazz);
        });
    }

    /**
     * Tests visitAnyClass with real (non-mock) class pools.
     * Verifies behavior with actual ClassPool instances.
     */
    @Test
    public void testVisitAnyClass_withRealClassPools() {
        // Arrange
        ClassPool realProgramPool = new ClassPool();
        ClassPool realLibraryPool = new ClassPool();
        DescriptorKeepChecker checkerWithRealPools = new DescriptorKeepChecker(
                realProgramPool,
                realLibraryPool,
                warningPrinter
        );

        // Act & Assert
        assertDoesNotThrow(() -> checkerWithRealPools.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass does not call the warning printer.
     * Since the method is a no-op, the warning printer should never be used.
     */
    @Test
    public void testVisitAnyClass_doesNotUseWarningPrinter() {
        // Arrange - create a mock warning printer to verify no interactions
        WarningPrinter mockPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                mockPrinter
        );

        // Act
        checker.visitAnyClass(clazz);

        // Assert - verify the warning printer was never used
        verifyNoInteractions(mockPrinter);
    }

    /**
     * Tests that visitAnyClass returns without errors even when called
     * in a rapid sequence with various parameter combinations.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls() {
        // Arrange
        Clazz[] clazzes = new Clazz[100];
        for (int i = 0; i < clazzes.length; i++) {
            clazzes[i] = mock(Clazz.class);
        }

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                descriptorKeepChecker.visitAnyClass(c);
            }
        });
    }

    /**
     * Tests visitAnyClass with a mix of null and mocked Clazz objects
     * to ensure robustness under various conditions.
     */
    @Test
    public void testVisitAnyClass_mixedNullAndMockedClazzes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            descriptorKeepChecker.visitAnyClass(mock(Clazz.class));
            descriptorKeepChecker.visitAnyClass(null);
            descriptorKeepChecker.visitAnyClass(mock(Clazz.class));
            descriptorKeepChecker.visitAnyClass(null);
            descriptorKeepChecker.visitAnyClass(mock(Clazz.class));
            descriptorKeepChecker.visitAnyClass(null);
        });
    }

    /**
     * Tests that visitAnyClass can be invoked on a checker instance
     * that has all null constructor parameters.
     */
    @Test
    public void testVisitAnyClass_onCheckerWithAllNullParameters() {
        // Arrange
        DescriptorKeepChecker checkerWithAllNulls = new DescriptorKeepChecker(null, null, null);

        // Act & Assert
        assertDoesNotThrow(() -> checkerWithAllNulls.visitAnyClass(clazz));
    }
}
