package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 */
public class AbstractAPIConverterClaude_visitAnyAttributeTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Attribute attribute;

    /**
     * Creates a concrete test subclass of AbstractAPIConverter for testing purposes.
     * This is necessary because AbstractAPIConverter is abstract.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter,
                        ClassVisitor modifiedClassVisitor,
                        InstructionVisitor extraInstructionVisitor) {
            super(programClassPool, libraryClassPool, warningPrinter,
                  modifiedClassVisitor, extraInstructionVisitor);

            // Initialize with empty replacements to avoid NullPointerExceptions
            setTypeReplacements(new TypeReplacement[0]);
            setMethodReplacements(new MethodReplacement[0]);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        modifiedClassVisitor = mock(ClassVisitor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);

        converter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        clazz = mock(ProgramClass.class);
        attribute = mock(Attribute.class);
    }

    /**
     * Tests that visitAnyAttribute can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(clazz, attribute);
            converter.visitAnyAttribute(clazz, attribute);
            converter.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Attribute parameter.
     * Since it's a no-op method, it should not call any methods on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAttribute() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the converter's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyConverterState() {
        // Arrange - create another converter with the same configuration
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act - call visitAnyAttribute on the first converter
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - both converters should be functionally equivalent
        // Since visitAnyAttribute is a no-op, we verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(clazz1, attribute);
            converter.visitAnyAttribute(clazz2, attribute);
            converter.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute works with different Attribute mock instances.
     * The method should handle any Attribute implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeInstances_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(clazz, attr1);
            converter.visitAnyAttribute(clazz, attr2);
            converter.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't trigger any warning printing.
     * Since it's a no-op method, it should not generate any warnings.
     */
    @Test
    public void testVisitAnyAttribute_doesNotTriggerWarnings() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnyAttribute doesn't trigger the modified class visitor.
     * Since it's a no-op method, it should not mark any classes as modified.
     */
    @Test
    public void testVisitAnyAttribute_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnyAttribute doesn't trigger the extra instruction visitor.
     * Since it's a no-op method, it should not affect instruction processing.
     */
    @Test
    public void testVisitAnyAttribute_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitAnyAttribute can be called on a converter with null warning printer.
     * The method should work even if optional dependencies are null.
     */
    @Test
    public void testVisitAnyAttribute_withNullWarningPrinter_doesNotThrowException() {
        // Arrange - create converter with null warning printer
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNullPrinter.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a converter with null class visitor.
     * The method should work even if optional dependencies are null.
     */
    @Test
    public void testVisitAnyAttribute_withNullClassVisitor_doesNotThrowException() {
        // Arrange - create converter with null class visitor
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNullVisitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a converter with null instruction visitor.
     * The method should work even if optional dependencies are null.
     */
    @Test
    public void testVisitAnyAttribute_withNullInstructionVisitor_doesNotThrowException() {
        // Arrange - create converter with null instruction visitor
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNullInstrVisitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a converter with all null dependencies.
     * The method should work even when the converter is minimally configured.
     */
    @Test
    public void testVisitAnyAttribute_withAllNullDependencies_doesNotThrowException() {
        // Arrange - create converter with all null dependencies
        TestAPIConverter converterWithNulls = new TestAPIConverter(
            null, null, null, null, null
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNulls.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }
}
