package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.constant.*;
import proguard.classfile.editor.CodeAttributeEditor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GsonConstructorPatcher#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is a no-op implementation of the AttributeVisitor interface.
 * It serves as a fallback for attributes that are not specifically handled by other visitor methods.
 * The actual work of GsonConstructorPatcher is done in visitCodeAttribute, not in visitAnyAttribute.
 */
public class GsonConstructorPatcherClaude_visitAnyAttributeTest {

    private GsonConstructorPatcher patcher;
    private CodeAttributeEditor codeAttributeEditor;

    /**
     * Sets up fresh instances before each test.
     */
    @BeforeEach
    public void setUp() {
        codeAttributeEditor = new CodeAttributeEditor();
        patcher = new GsonConstructorPatcher(codeAttributeEditor, false);
    }

    // =========================================================================
    // Tests for visitAnyAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/attribute/Attribute;)V
    // =========================================================================

    /**
     * Tests that visitAnyAttribute does nothing when called with a valid Clazz and Attribute.
     * The method is a no-op implementation of the AttributeVisitor interface.
     */
    @Test
    public void testVisitAnyAttribute_withValidClazzAndAttribute_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        patcher.visitAnyAttribute(clazz, attribute);

        // Assert - no interactions should occur since it's a no-op
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyAttribute_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Attribute attribute1 = mock(Attribute.class);
        Clazz clazz2 = mock(Clazz.class);
        Attribute attribute2 = mock(Attribute.class);

        // Act
        patcher.visitAnyAttribute(clazz1, attribute1);
        patcher.visitAnyAttribute(clazz2, attribute2);
        patcher.visitAnyAttribute(clazz1, attribute1);

        // Assert
        verifyNoInteractions(clazz1, attribute1, clazz2, attribute2);
    }

    /**
     * Tests that visitAnyAttribute does not throw exceptions with null Clazz argument.
     * While not a recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(null, attribute),
                "visitAnyAttribute should not throw exception with null Clazz");
    }

    /**
     * Tests that visitAnyAttribute does not throw exceptions with a null Attribute argument.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrow() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(clazz, null),
                "visitAnyAttribute should not throw exception with null Attribute");
    }

    /**
     * Tests that visitAnyAttribute does not throw exceptions when both arguments are null.
     */
    @Test
    public void testVisitAnyAttribute_withBothNull_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(null, null),
                "visitAnyAttribute should not throw exception with null arguments");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with a CodeAttribute.
     * Note: While visitAnyAttribute is the generic handler, visitCodeAttribute will be
     * called instead for CodeAttribute objects when properly dispatched through the visitor pattern.
     * This test verifies that calling visitAnyAttribute directly with a CodeAttribute is harmless.
     */
    @Test
    public void testVisitAnyAttribute_withCodeAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        CodeAttribute codeAttribute = createMinimalCodeAttribute();

        // Act
        patcher.visitAnyAttribute(programClass, codeAttribute);

        // Assert - verify it's a no-op
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, codeAttribute),
                "visitAnyAttribute should not process CodeAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with a SourceFileAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withSourceFileAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();
        sourceFileAttribute.u2attributeNameIndex = 3;
        sourceFileAttribute.u2sourceFileIndex = 4;

        // Act
        patcher.visitAnyAttribute(programClass, sourceFileAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, sourceFileAttribute),
                "visitAnyAttribute should handle SourceFileAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with a ConstantValueAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withConstantValueAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ConstantValueAttribute constantValueAttribute = new ConstantValueAttribute();
        constantValueAttribute.u2attributeNameIndex = 3;
        constantValueAttribute.u2constantValueIndex = 4;

        // Act
        patcher.visitAnyAttribute(programClass, constantValueAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, constantValueAttribute),
                "visitAnyAttribute should handle ConstantValueAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with an ExceptionsAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withExceptionsAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();
        exceptionsAttribute.u2attributeNameIndex = 3;
        exceptionsAttribute.u2exceptionIndexTableLength = 0;
        exceptionsAttribute.u2exceptionIndexTable = new int[0];

        // Act
        patcher.visitAnyAttribute(programClass, exceptionsAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, exceptionsAttribute),
                "visitAnyAttribute should handle ExceptionsAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with a SignatureAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withSignatureAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        SignatureAttribute signatureAttribute = new SignatureAttribute();
        signatureAttribute.u2attributeNameIndex = 3;
        signatureAttribute.u2signatureIndex = 4;

        // Act
        patcher.visitAnyAttribute(programClass, signatureAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, signatureAttribute),
                "visitAnyAttribute should handle SignatureAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with a DeprecatedAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withDeprecatedAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();
        deprecatedAttribute.u2attributeNameIndex = 3;

        // Act
        patcher.visitAnyAttribute(programClass, deprecatedAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, deprecatedAttribute),
                "visitAnyAttribute should handle DeprecatedAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with a SyntheticAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withSyntheticAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        SyntheticAttribute syntheticAttribute = new SyntheticAttribute();
        syntheticAttribute.u2attributeNameIndex = 3;

        // Act
        patcher.visitAnyAttribute(programClass, syntheticAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, syntheticAttribute),
                "visitAnyAttribute should handle SyntheticAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with an InnerClassesAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withInnerClassesAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute();
        innerClassesAttribute.u2attributeNameIndex = 3;
        innerClassesAttribute.u2classesCount = 0;
        innerClassesAttribute.classes = new InnerClassesInfo[0];

        // Act
        patcher.visitAnyAttribute(programClass, innerClassesAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, innerClassesAttribute),
                "visitAnyAttribute should handle InnerClassesAttribute");
    }

    /**
     * Tests that visitAnyAttribute works correctly when called with a LineNumberTableAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withLineNumberTableAttribute_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();
        lineNumberTableAttribute.u2attributeNameIndex = 3;
        lineNumberTableAttribute.u2lineNumberTableLength = 0;
        lineNumberTableAttribute.lineNumberTable = new LineNumberInfo[0];

        // Act
        patcher.visitAnyAttribute(programClass, lineNumberTableAttribute);

        // Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, lineNumberTableAttribute),
                "visitAnyAttribute should handle LineNumberTableAttribute");
    }

    /**
     * Tests that visitAnyAttribute is truly a no-op by verifying it doesn't interact
     * with the patcher's internal state.
     */
    @Test
    public void testVisitAnyAttribute_isNoOp_noInternalStateChanges() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act - call it many times
        for (int i = 0; i < 100; i++) {
            patcher.visitAnyAttribute(clazz, attribute);
        }

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute with addExcluder=true still does nothing.
     * The addExcluder flag only affects visitCodeAttribute, not visitAnyAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withAddExcluderTrue_stillDoesNothing() {
        // Arrange
        CodeAttributeEditor editor = new CodeAttributeEditor();
        GsonConstructorPatcher patcherWithExcluder = new GsonConstructorPatcher(editor, true);
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        patcherWithExcluder.visitAnyAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute with addExcluder=false still does nothing.
     */
    @Test
    public void testVisitAnyAttribute_withAddExcluderFalse_stillDoesNothing() {
        // Arrange
        CodeAttributeEditor editor = new CodeAttributeEditor();
        GsonConstructorPatcher patcherNoExcluder = new GsonConstructorPatcher(editor, false);
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        patcherNoExcluder.visitAnyAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that different patcher instances behave the same way for visitAnyAttribute.
     * Since visitAnyAttribute is a no-op, all instances should behave identically.
     */
    @Test
    public void testVisitAnyAttribute_multipleInstances_sameNoOpBehavior() {
        // Arrange
        CodeAttributeEditor editor1 = new CodeAttributeEditor();
        CodeAttributeEditor editor2 = new CodeAttributeEditor();
        GsonConstructorPatcher patcher1 = new GsonConstructorPatcher(editor1, true);
        GsonConstructorPatcher patcher2 = new GsonConstructorPatcher(editor2, false);

        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        patcher1.visitAnyAttribute(clazz, attribute);
        patcher2.visitAnyAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same objects repeatedly
     * without any state accumulation or side effects.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsSameObjects_consistent() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> patcher.visitAnyAttribute(clazz, attribute),
                    "Repeated calls should not throw exceptions");
        }

        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute maintains its no-op behavior regardless of
     * the type of Clazz (ProgramClass vs LibraryClass).
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzTypes_allAreNoOp() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("TestClass");
        LibraryClass libraryClass = mock(LibraryClass.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        patcher.visitAnyAttribute(programClass, attribute);
        patcher.visitAnyAttribute(libraryClass, attribute);

        // Assert
        verifyNoInteractions(libraryClass);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute maintains its no-op behavior regardless of
     * the type of Attribute (various attribute types).
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeTypes_allAreNoOp() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        CodeAttribute codeAttribute = createMinimalCodeAttribute();
        SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();
        DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();
        SyntheticAttribute syntheticAttribute = new SyntheticAttribute();

        // Act
        patcher.visitAnyAttribute(clazz, codeAttribute);
        patcher.visitAnyAttribute(clazz, sourceFileAttribute);
        patcher.visitAnyAttribute(clazz, deprecatedAttribute);
        patcher.visitAnyAttribute(clazz, syntheticAttribute);

        // Assert - all should be no-ops, no exceptions thrown
        assertDoesNotThrow(() -> {
            patcher.visitAnyAttribute(clazz, codeAttribute);
            patcher.visitAnyAttribute(clazz, sourceFileAttribute);
            patcher.visitAnyAttribute(clazz, deprecatedAttribute);
            patcher.visitAnyAttribute(clazz, syntheticAttribute);
        });
    }

    /**
     * Tests that visitAnyAttribute does not modify the CodeAttributeEditor
     * passed to the GsonConstructorPatcher constructor.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor editor = spy(new CodeAttributeEditor());
        GsonConstructorPatcher testPatcher = new GsonConstructorPatcher(editor, false);
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        testPatcher.visitAnyAttribute(clazz, attribute);

        // Assert - the editor should not have been interacted with
        verifyNoInteractions(editor);
    }

    /**
     * Tests that calling visitAnyAttribute before visitProgramMethod has no effect
     * on subsequent visitor calls.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentVisitorCalls() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act - call visitAnyAttribute first
        patcher.visitAnyAttribute(clazz, attribute);

        // Then verify we can still use the patcher normally (no state corruption)
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(clazz, attribute),
                "visitAnyAttribute should not corrupt patcher state");
    }

    /**
     * Tests that visitAnyAttribute behavior is deterministic.
     * Multiple calls with the same input should behave the same way.
     */
    @Test
    public void testVisitAnyAttribute_deterministic_sameInputSameBehavior() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> patcher.visitAnyAttribute(clazz, attribute),
                    "Deterministic behavior should not throw exceptions");
        }

        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works with a mixture of real and mocked objects.
     */
    @Test
    public void testVisitAnyAttribute_withRealProgramClass_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/example/TestClass");
        Attribute attribute = mock(Attribute.class);

        // Act
        patcher.visitAnyAttribute(programClass, attribute);

        // Assert
        verifyNoInteractions(attribute);
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, attribute),
                "visitAnyAttribute should handle real ProgramClass instances");
    }

    /**
     * Tests that visitAnyAttribute works with all real objects (no mocks).
     */
    @Test
    public void testVisitAnyAttribute_withAllRealObjects_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/example/TestClass");
        CodeAttribute codeAttribute = createMinimalCodeAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> patcher.visitAnyAttribute(programClass, codeAttribute),
                "visitAnyAttribute should handle all real objects");
    }

    /**
     * Tests that visitAnyAttribute can be called in rapid succession without issues.
     */
    @Test
    public void testVisitAnyAttribute_rapidSuccessiveCalls_noIssues() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                patcher.visitAnyAttribute(clazz, attribute);
            }
        }, "visitAnyAttribute should handle rapid successive calls");

        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works correctly with attributes from different classes.
     */
    @Test
    public void testVisitAnyAttribute_withAttributesFromDifferentClasses_allAreNoOp() {
        // Arrange
        ProgramClass class1 = createMinimalProgramClass("Class1");
        ProgramClass class2 = createMinimalProgramClass("Class2");
        ProgramClass class3 = createMinimalProgramClass("Class3");

        CodeAttribute attr1 = createMinimalCodeAttribute();
        CodeAttribute attr2 = createMinimalCodeAttribute();
        CodeAttribute attr3 = createMinimalCodeAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            patcher.visitAnyAttribute(class1, attr1);
            patcher.visitAnyAttribute(class2, attr2);
            patcher.visitAnyAttribute(class3, attr3);
        }, "visitAnyAttribute should handle attributes from different classes");
    }

    // =========================================================================
    // Helper methods to create test objects
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
     *
     * @param className the name of the class
     * @return a configured ProgramClass instance
     */
    private ProgramClass createMinimalProgramClass(String className) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        Constant[] constantPool = new Constant[10];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;
        programClass.fields = new ProgramField[0];
        programClass.u2fieldsCount = 0;

        return programClass;
    }

    /**
     * Creates a minimal CodeAttribute for testing.
     *
     * @return a configured CodeAttribute instance
     */
    private CodeAttribute createMinimalCodeAttribute() {
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributeNameIndex = 3;
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 10;
        codeAttribute.u4codeLength = 1;
        codeAttribute.code = new byte[1];
        codeAttribute.u2exceptionTableLength = 0;
        codeAttribute.exceptionTable = new ExceptionInfo[0];
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        return codeAttribute;
    }
}
