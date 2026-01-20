package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberReferenceGeneralizer#MemberReferenceGeneralizer(boolean, boolean, CodeAttributeEditor, InstructionVisitor, InstructionVisitor)}.
 *
 * The constructor in MemberReferenceGeneralizer accepts five parameters:
 * - fieldGeneralizationClass: boolean flag to control field class generalization
 * - methodGeneralizationClass: boolean flag to control method class generalization
 * - codeAttributeEditor: editor for accumulating code changes
 * - extraFieldInstructionVisitor: optional visitor for generalized field instructions (can be null)
 * - extraMethodInstructionVisitor: optional visitor for generalized method instructions (can be null)
 *
 * The constructor stores these parameters in private fields for later use when visiting instructions.
 *
 * These tests verify that the constructor:
 * 1. Successfully creates an instance with valid parameters
 * 2. Properly handles null optional visitors
 * 3. Handles various combinations of boolean flags
 * 4. Creates functional instances that can be used as visitors
 */
public class MemberReferenceGeneralizerClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates an instance with all valid parameters.
     * This is the basic happy path with all parameters provided.
     */
    @Test
    public void testConstructor_withAllValidParameters_createsInstance() {
        // Arrange
        boolean fieldGeneralizationClass = true;
        boolean methodGeneralizationClass = true;
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraFieldInstructionVisitor = mock(InstructionVisitor.class);
        InstructionVisitor extraMethodInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            fieldGeneralizationClass,
            methodGeneralizationClass,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );

        // Assert
        assertNotNull(generalizer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor accepts null for optional extra visitors.
     * Both extraFieldInstructionVisitor and extraMethodInstructionVisitor can be null.
     */
    @Test
    public void testConstructor_withNullExtraVisitors_createsInstance() {
        // Arrange
        boolean fieldGeneralizationClass = true;
        boolean methodGeneralizationClass = true;
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            fieldGeneralizationClass,
            methodGeneralizationClass,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertNotNull(generalizer, "Constructor should accept null extra visitors");
    }

    /**
     * Tests that the constructor works with both boolean flags set to true.
     * Verifies generalization is enabled for both fields and methods.
     */
    @Test
    public void testConstructor_withBothFlagsTrue_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertNotNull(generalizer, "Constructor should accept both flags as true");
    }

    /**
     * Tests that the constructor works with both boolean flags set to false.
     * Verifies generalization is disabled for both fields and methods.
     */
    @Test
    public void testConstructor_withBothFlagsFalse_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            false,
            false,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertNotNull(generalizer, "Constructor should accept both flags as false");
    }

    /**
     * Tests that the constructor works with field generalization enabled and method disabled.
     */
    @Test
    public void testConstructor_withFieldTrueMethodFalse_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            false,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertNotNull(generalizer, "Constructor should accept field=true, method=false");
    }

    /**
     * Tests that the constructor works with field generalization disabled and method enabled.
     */
    @Test
    public void testConstructor_withFieldFalseMethodTrue_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            false,
            true,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertNotNull(generalizer, "Constructor should accept field=false, method=true");
    }

    /**
     * Tests that the constructor works with only field extra visitor provided.
     */
    @Test
    public void testConstructor_withOnlyFieldVisitor_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraFieldInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            null
        );

        // Assert
        assertNotNull(generalizer, "Constructor should accept only field visitor");
    }

    /**
     * Tests that the constructor works with only method extra visitor provided.
     */
    @Test
    public void testConstructor_withOnlyMethodVisitor_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraMethodInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            extraMethodInstructionVisitor
        );

        // Assert
        assertNotNull(generalizer, "Constructor should accept only method visitor");
    }

    /**
     * Tests that the constructor creates an instance that implements InstructionVisitor.
     * MemberReferenceGeneralizer implements InstructionVisitor to visit and potentially
     * generalize field/method invocation instructions.
     */
    @Test
    public void testConstructor_implementsInstructionVisitor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertTrue(generalizer instanceof InstructionVisitor,
            "MemberReferenceGeneralizer should implement InstructionVisitor");
    }

    /**
     * Tests that multiple instances can be created with different parameters.
     * Verifies each instance maintains its own state.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor3 = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, false, editor1, null, null
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            false, true, editor2, null, null
        );
        MemberReferenceGeneralizer generalizer3 = new MemberReferenceGeneralizer(
            true, true, editor3, null, null
        );

        // Assert
        assertNotNull(generalizer1, "First instance should be created");
        assertNotNull(generalizer2, "Second instance should be created");
        assertNotNull(generalizer3, "Third instance should be created");
        assertNotSame(generalizer1, generalizer2, "Instances should be distinct");
        assertNotSame(generalizer2, generalizer3, "Instances should be distinct");
        assertNotSame(generalizer1, generalizer3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor can be called repeatedly without issues.
     */
    @Test
    public void testConstructor_repeatedConstruction_succeeds() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
                    i % 2 == 0,
                    i % 3 == 0,
                    codeAttributeEditor,
                    null,
                    null
                );
                assertNotNull(generalizer, "Instance " + i + " should be non-null");
            }
        }, "Should be able to construct many instances without issues");
    }

    /**
     * Tests that the constructed instance can be used immediately.
     * Verifies the object is properly initialized and usable as an InstructionVisitor.
     */
    @Test
    public void testConstructor_instanceUsableImmediately() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );

        Clazz clazz = mock(Clazz.class);
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = mock(Instruction.class);

        // Act & Assert - should not throw
        assertDoesNotThrow(() ->
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
            "Constructed instance should be immediately usable"
        );
    }

    /**
     * Tests that the same CodeAttributeEditor can be shared across multiple instances.
     * Verifies the constructor doesn't claim exclusive ownership of the editor.
     */
    @Test
    public void testConstructor_sharedEditor_succeeds() {
        // Arrange
        CodeAttributeEditor sharedEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, true, sharedEditor, null, null
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            false, false, sharedEditor, null, null
        );
        MemberReferenceGeneralizer generalizer3 = new MemberReferenceGeneralizer(
            true, false, sharedEditor, null, null
        );

        // Assert
        assertNotNull(generalizer1, "First instance with shared editor should be created");
        assertNotNull(generalizer2, "Second instance with shared editor should be created");
        assertNotNull(generalizer3, "Third instance with shared editor should be created");
    }

    /**
     * Tests that the same extra visitors can be shared across multiple instances.
     */
    @Test
    public void testConstructor_sharedExtraVisitors_succeeds() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor sharedFieldVisitor = mock(InstructionVisitor.class);
        InstructionVisitor sharedMethodVisitor = mock(InstructionVisitor.class);

        // Act
        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, true, codeAttributeEditor, sharedFieldVisitor, sharedMethodVisitor
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            true, true, codeAttributeEditor, sharedFieldVisitor, sharedMethodVisitor
        );

        // Assert
        assertNotNull(generalizer1, "First instance with shared visitors should be created");
        assertNotNull(generalizer2, "Second instance with shared visitors should be created");
    }

    /**
     * Tests that the constructor works with real (non-mock) InstructionVisitor implementations.
     */
    @Test
    public void testConstructor_withRealVisitors_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        InstructionVisitor realFieldVisitor = new InstructionVisitor() {
            @Override
            public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                           int offset, Instruction instruction) {
                // No-op implementation
            }
        };

        InstructionVisitor realMethodVisitor = new InstructionVisitor() {
            @Override
            public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                           int offset, Instruction instruction) {
                // No-op implementation
            }
        };

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            realFieldVisitor,
            realMethodVisitor
        );

        // Assert
        assertNotNull(generalizer, "Constructor should work with real visitor implementations");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertNotNull(generalizer, "Instance should be created");
        assertTrue(generalizer instanceof MemberReferenceGeneralizer,
            "Should be instance of MemberReferenceGeneralizer");
        assertEquals(MemberReferenceGeneralizer.class, generalizer.getClass(),
            "Class should be MemberReferenceGeneralizer");
    }

    /**
     * Tests that construction completes quickly without performing expensive operations.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        long startTime = System.nanoTime();
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );
        long endTime = System.nanoTime();

        // Assert
        assertNotNull(generalizer, "Instance should be created");
        long durationNanos = endTime - startTime;
        long oneMillisecondInNanos = 1_000_000L;
        assertTrue(durationNanos < oneMillisecondInNanos,
            "Constructor should complete very quickly, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that the constructor works in a multi-threaded environment.
     * Verifies there are no concurrency issues with construction.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final Thread[] threads = new Thread[threadCount];
        final MemberReferenceGeneralizer[] generalizers = new MemberReferenceGeneralizer[threadCount];
        final Exception[] exceptions = new Exception[threadCount];

        // Act - create generalizers in parallel threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    CodeAttributeEditor editor = mock(CodeAttributeEditor.class);
                    generalizers[index] = new MemberReferenceGeneralizer(
                        index % 2 == 0,
                        index % 3 == 0,
                        editor,
                        null,
                        null
                    );
                } catch (Exception e) {
                    exceptions[index] = e;
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        for (int i = 0; i < threadCount; i++) {
            assertNull(exceptions[i], "No exceptions should occur in thread " + i);
            assertNotNull(generalizers[i], "Generalizer should be created in thread " + i);
        }
    }

    /**
     * Tests that the constructor does not throw exceptions with valid parameters.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act & Assert
        assertDoesNotThrow(() -> new MemberReferenceGeneralizer(
            true, true, codeAttributeEditor, null, null
        ), "Constructor should not throw with valid parameters");
    }

    /**
     * Tests that the instance's toString() method works after construction.
     */
    @Test
    public void testConstructor_toStringWorks() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );
        String toString = generalizer.toString();

        // Assert
        assertNotNull(toString, "toString() should return a non-null value");
        assertTrue(toString.contains("MemberReferenceGeneralizer"),
            "toString() should contain the class name");
    }

    /**
     * Tests that hashCode() works on the constructed instance.
     */
    @Test
    public void testConstructor_hashCodeWorks() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );

        // Assert
        assertDoesNotThrow(() -> generalizer.hashCode(),
            "hashCode() should work on constructed instance");
    }

    /**
     * Tests that equals() works on the constructed instance.
     */
    @Test
    public void testConstructor_equalsWorks() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, true, codeAttributeEditor, null, null
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            true, true, codeAttributeEditor, null, null
        );

        // Assert
        assertDoesNotThrow(() -> generalizer1.equals(generalizer2),
            "equals() should work on constructed instances");
        assertTrue(generalizer1.equals(generalizer1),
            "Instance should equal itself");
    }

    /**
     * Tests that all four boolean flag combinations can be constructed.
     */
    @Test
    public void testConstructor_allBooleanCombinations_succeed() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act & Assert - all 4 combinations
        assertDoesNotThrow(() -> {
            new MemberReferenceGeneralizer(false, false, codeAttributeEditor, null, null);
            new MemberReferenceGeneralizer(false, true, codeAttributeEditor, null, null);
            new MemberReferenceGeneralizer(true, false, codeAttributeEditor, null, null);
            new MemberReferenceGeneralizer(true, true, codeAttributeEditor, null, null);
        }, "All boolean combinations should be valid");
    }

    /**
     * Tests that all four combinations of null/non-null extra visitors can be constructed.
     */
    @Test
    public void testConstructor_allVisitorCombinations_succeed() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor fieldVisitor = mock(InstructionVisitor.class);
        InstructionVisitor methodVisitor = mock(InstructionVisitor.class);

        // Act & Assert - all 4 combinations
        assertDoesNotThrow(() -> {
            new MemberReferenceGeneralizer(true, true, codeAttributeEditor, null, null);
            new MemberReferenceGeneralizer(true, true, codeAttributeEditor, fieldVisitor, null);
            new MemberReferenceGeneralizer(true, true, codeAttributeEditor, null, methodVisitor);
            new MemberReferenceGeneralizer(true, true, codeAttributeEditor, fieldVisitor, methodVisitor);
        }, "All visitor combinations should be valid");
    }
}
