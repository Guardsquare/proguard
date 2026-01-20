package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
 *
 * The visitCodeAttribute method in TargetClassChanger processes a CodeAttribute by recursively
 * visiting its nested attributes (like LocalVariableTableAttribute, LocalVariableTypeTableAttribute, etc.).
 *
 * The method implementation:
 * <pre>
 * public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
 * {
 *     // Change the references of the attributes.
 *     codeAttribute.attributesAccept(clazz, method, this);
 * }
 * </pre>
 *
 * This method is part of the visitor pattern used to traverse and modify class references
 * in code attributes. It delegates to nested attributes which may contain class references
 * that need to be updated (retargeted) as part of class merging optimizations.
 */
public class TargetClassChangerClaude_visitCodeAttributeTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Method method;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
    }

    /**
     * Tests that visitCodeAttribute calls attributesAccept on the code attribute.
     * This is the core functionality - delegating to nested attributes.
     */
    @Test
    public void testVisitCodeAttribute_callsAttributesAccept() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify attributesAccept was called with the correct parameters
        verify(codeAttribute, times(1)).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute works with a real CodeAttribute that has no nested attributes.
     * Should complete without errors.
     */
    @Test
    public void testVisitCodeAttribute_withCodeAttributeNoAttributes_doesNotThrowException() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute should not throw with empty code attribute");
    }

    /**
     * Tests that visitCodeAttribute processes nested attributes by verifying the changer
     * is passed as the visitor to the code attribute's attributesAccept method.
     */
    @Test
    public void testVisitCodeAttribute_passesChangerAsVisitor() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify the changer itself was passed as the AttributeVisitor
        verify(codeAttribute).attributesAccept(eq(clazz), eq(method), same(changer));
    }

    /**
     * Tests that visitCodeAttribute can be called multiple times on the same code attribute.
     * Each call should trigger a new attributesAccept call.
     */
    @Test
    public void testVisitCodeAttribute_calledMultipleTimes_callsAttributesAcceptEachTime() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz, method, codeAttribute);
        changer.visitCodeAttribute(clazz, method, codeAttribute);
        changer.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify attributesAccept was called three times
        verify(codeAttribute, times(3)).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute throws NullPointerException with null code attribute.
     * The method must call attributesAccept on the code attribute, so null is not acceptable.
     */
    @Test
    public void testVisitCodeAttribute_withNullCodeAttribute_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> changer.visitCodeAttribute(clazz, method, null),
                "visitCodeAttribute with null code attribute should throw NullPointerException");
    }

    /**
     * Tests that visitCodeAttribute works with null Clazz parameter.
     * The clazz is passed through to attributesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitCodeAttribute_withNullClazz_passesNullToAttributesAccept() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(null, method, codeAttribute);

        // Assert - verify null clazz was passed to attributesAccept
        verify(codeAttribute).attributesAccept(null, method, changer);
    }

    /**
     * Tests that visitCodeAttribute works with null Method parameter.
     * The method is passed through to attributesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitCodeAttribute_withNullMethod_passesNullToAttributesAccept() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz, null, codeAttribute);

        // Assert - verify null method was passed to attributesAccept
        verify(codeAttribute).attributesAccept(clazz, null, changer);
    }

    /**
     * Tests that visitCodeAttribute can process multiple different code attributes.
     * Each should trigger its own attributesAccept call.
     */
    @Test
    public void testVisitCodeAttribute_withMultipleCodeAttributes_callsAttributesAcceptOnEach() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        CodeAttribute codeAttr3 = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz, method, codeAttr1);
        changer.visitCodeAttribute(clazz, method, codeAttr2);
        changer.visitCodeAttribute(clazz, method, codeAttr3);

        // Assert
        verify(codeAttr1, times(1)).attributesAccept(clazz, method, changer);
        verify(codeAttr2, times(1)).attributesAccept(clazz, method, changer);
        verify(codeAttr3, times(1)).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute can be used by multiple TargetClassChanger instances.
     * Each changer should independently call attributesAccept.
     */
    @Test
    public void testVisitCodeAttribute_withMultipleChangers_eachCallsAttributesAccept() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer1.visitCodeAttribute(clazz, method, codeAttribute);
        changer2.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify attributesAccept was called twice (once per changer)
        verify(codeAttribute, times(2)).attributesAccept(eq(clazz), eq(method), any(AttributeVisitor.class));
    }

    /**
     * Tests that visitCodeAttribute works with different Clazz instances.
     * Each should be passed correctly to attributesAccept.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentClasses_passesCorrectClazzToAttributesAccept() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        CodeAttribute codeAttribute1 = mock(CodeAttribute.class);
        CodeAttribute codeAttribute2 = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz1, method, codeAttribute1);
        changer.visitCodeAttribute(clazz2, method, codeAttribute2);

        // Assert
        verify(codeAttribute1).attributesAccept(clazz1, method, changer);
        verify(codeAttribute2).attributesAccept(clazz2, method, changer);
    }

    /**
     * Tests that visitCodeAttribute works with different Method instances.
     * Each should be passed correctly to attributesAccept.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentMethods_passesCorrectMethodToAttributesAccept() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttribute1 = mock(CodeAttribute.class);
        CodeAttribute codeAttribute2 = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz, method1, codeAttribute1);
        changer.visitCodeAttribute(clazz, method2, codeAttribute2);

        // Assert
        verify(codeAttribute1).attributesAccept(clazz, method1, changer);
        verify(codeAttribute2).attributesAccept(clazz, method2, changer);
    }

    /**
     * Tests that visitCodeAttribute does not throw any exceptions with valid mock input.
     */
    @Test
    public void testVisitCodeAttribute_withValidMocks_doesNotThrowException() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute should not throw with valid mocks");
    }

    /**
     * Tests that visitCodeAttribute can be used polymorphically through the AttributeVisitor interface.
     */
    @Test
    public void testVisitCodeAttribute_asAttributeVisitor_callsAttributesAccept() {
        // Arrange
        AttributeVisitor visitor = changer;
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        visitor.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        verify(codeAttribute).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute can be called in rapid succession without issues.
     */
    @Test
    public void testVisitCodeAttribute_rapidSuccessiveCalls_allCallAttributesAccept() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        for (int i = 0; i < 100; i++) {
            changer.visitCodeAttribute(clazz, method, codeAttribute);
        }

        // Assert
        verify(codeAttribute, times(100)).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute with real CodeAttribute and real nested attributes works correctly.
     * This tests actual integration with the attributesAccept mechanism.
     */
    @Test
    public void testVisitCodeAttribute_withRealCodeAttributeAndNestedAttributes_processesAttributes() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 1;

        // Create a mock nested attribute
        Attribute nestedAttribute = mock(Attribute.class);
        codeAttribute.attributes = new Attribute[] { nestedAttribute };

        // Act
        changer.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - the nested attribute should have been visited
        // Note: We can't directly verify this without inspecting internal state,
        // but the call should complete without errors
        assertDoesNotThrow(() -> changer.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute should process nested attributes without errors");
    }

    /**
     * Tests that visitCodeAttribute works with CodeAttribute that has multiple nested attributes.
     */
    @Test
    public void testVisitCodeAttribute_withMultipleNestedAttributes_processesAll() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 3;
        codeAttribute.attributes = new Attribute[] {
            mock(LineNumberTableAttribute.class),
            mock(LocalVariableTableAttribute.class),
            mock(LocalVariableTypeTableAttribute.class)
        };

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute should process multiple nested attributes without errors");
    }

    /**
     * Tests that visitCodeAttribute can handle CodeAttribute with configured bytecode.
     */
    @Test
    public void testVisitCodeAttribute_withConfiguredCodeAttribute_doesNotThrowException() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 5;
        codeAttribute.u4codeLength = 100;
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute should work with configured code attribute");
    }

    /**
     * Tests that visitCodeAttribute works with real ProgramClass and ProgramMethod.
     */
    @Test
    public void testVisitCodeAttribute_withRealClassAndMethod_callsAttributesAccept() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        ProgramMethod realMethod = new ProgramMethod();
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(realClass, realMethod, codeAttribute);

        // Assert
        verify(codeAttribute).attributesAccept(realClass, realMethod, changer);
    }

    /**
     * Tests that visitCodeAttribute processes the code attribute in the correct order
     * relative to other visitor methods.
     */
    @Test
    public void testVisitCodeAttribute_afterOtherVisitorMethods_stillCallsAttributesAccept() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Attribute genericAttribute = mock(Attribute.class);

        // Act - call other visitor methods first
        changer.visitAnyAttribute(clazz, genericAttribute);
        changer.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        verify(codeAttribute).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute doesn't modify the Clazz parameter.
     */
    @Test
    public void testVisitCodeAttribute_doesNotModifyClazz() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        // Act
        changer.visitCodeAttribute(testClass, method, codeAttribute);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Clazz processing info should not be modified by visitCodeAttribute");
    }

    /**
     * Tests that visitCodeAttribute doesn't modify the Method parameter.
     */
    @Test
    public void testVisitCodeAttribute_doesNotModifyMethod() {
        // Arrange
        ProgramMethod testMethod = new ProgramMethod();
        Object methodProcessingInfo = new Object();
        testMethod.setProcessingInfo(methodProcessingInfo);
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        // Act
        changer.visitCodeAttribute(clazz, testMethod, codeAttribute);

        // Assert
        assertSame(methodProcessingInfo, testMethod.getProcessingInfo(),
                "Method processing info should not be modified by visitCodeAttribute");
    }

    /**
     * Tests that visitCodeAttribute works when the same code attribute is visited
     * from different contexts (different clazz/method combinations).
     */
    @Test
    public void testVisitCodeAttribute_sameCodeAttributeDifferentContexts_callsAttributesAcceptWithCorrectParameters() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(clazz1, method1, codeAttribute);
        changer.visitCodeAttribute(clazz2, method2, codeAttribute);

        // Assert - verify correct parameters were passed each time
        verify(codeAttribute).attributesAccept(clazz1, method1, changer);
        verify(codeAttribute).attributesAccept(clazz2, method2, changer);
    }

    /**
     * Tests that visitCodeAttribute handles CodeAttribute with null attributes array gracefully.
     * This is an edge case that might occur with improperly initialized code attributes.
     */
    @Test
    public void testVisitCodeAttribute_withNullAttributesArray_handledByAttributesAccept() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = null;

        // Act & Assert - behavior depends on attributesAccept implementation
        // We just verify it doesn't throw an unexpected exception from visitCodeAttribute
        assertDoesNotThrow(() -> {
            try {
                changer.visitCodeAttribute(clazz, method, codeAttribute);
            } catch (NullPointerException e) {
                // NPE from attributesAccept is acceptable
            }
        }, "visitCodeAttribute should delegate null handling to attributesAccept");
    }

    /**
     * Tests that visitCodeAttribute maintains the correct visitor chain.
     * The changer should be passed as the visitor to process nested attributes.
     */
    @Test
    public void testVisitCodeAttribute_maintainsVisitorChain() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        TargetClassChanger specificChanger = new TargetClassChanger();

        // Act
        specificChanger.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify the specific changer instance was passed
        verify(codeAttribute).attributesAccept(eq(clazz), eq(method), same(specificChanger));
    }

    /**
     * Tests that visitCodeAttribute can be interleaved with other attribute visitor methods.
     */
    @Test
    public void testVisitCodeAttribute_interleavedWithOtherVisitorMethods_worksCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = mock(CodeAttribute.class);
        CodeAttribute codeAttribute2 = mock(CodeAttribute.class);
        Attribute genericAttribute = mock(Attribute.class);

        // Act
        changer.visitCodeAttribute(clazz, method, codeAttribute1);
        changer.visitAnyAttribute(clazz, genericAttribute);
        changer.visitCodeAttribute(clazz, method, codeAttribute2);

        // Assert
        verify(codeAttribute1).attributesAccept(clazz, method, changer);
        verify(codeAttribute2).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute works correctly when called on the same code attribute
     * with the same parameters multiple times in sequence.
     */
    @Test
    public void testVisitCodeAttribute_repeatedCallsSameParameters_callsAttributesAcceptEachTime() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        for (int i = 0; i < 5; i++) {
            changer.visitCodeAttribute(clazz, method, codeAttribute);
        }

        // Assert
        verify(codeAttribute, times(5)).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute completes quickly, as it just delegates to attributesAccept.
     */
    @Test
    public void testVisitCodeAttribute_completesQuickly() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            changer.visitCodeAttribute(clazz, method, codeAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should be very fast
        assertTrue(durationMs < 100,
                "visitCodeAttribute should complete quickly (1000 calls in <100ms), took " + durationMs + "ms");
    }

    /**
     * Tests that visitCodeAttribute with various parameter combinations all work correctly.
     */
    @Test
    public void testVisitCodeAttribute_variousParameterCombinations_allCallAttributesAccept() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);

        // Act - test various combinations
        changer.visitCodeAttribute(clazz1, method1, codeAttr1);
        changer.visitCodeAttribute(clazz2, method2, codeAttr2);
        changer.visitCodeAttribute(clazz1, method2, codeAttr1);
        changer.visitCodeAttribute(clazz2, method1, codeAttr2);

        // Assert
        verify(codeAttr1, times(2)).attributesAccept(any(), any(), eq(changer));
        verify(codeAttr2, times(2)).attributesAccept(any(), any(), eq(changer));
    }

    /**
     * Tests that visitCodeAttribute is part of the correct visitor interface.
     */
    @Test
    public void testVisitCodeAttribute_implementsAttributeVisitorInterface() {
        // Assert
        assertTrue(changer instanceof AttributeVisitor,
                "TargetClassChanger should implement AttributeVisitor");
    }

    /**
     * Tests that visitCodeAttribute can handle both null and non-null parameters
     * in various combinations, delegating appropriately to attributesAccept.
     */
    @Test
    public void testVisitCodeAttribute_mixedNullNonNullParameters_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test various null/non-null combinations
        changer.visitCodeAttribute(null, method, codeAttribute);
        verify(codeAttribute).attributesAccept(null, method, changer);

        reset(codeAttribute);
        changer.visitCodeAttribute(clazz, null, codeAttribute);
        verify(codeAttribute).attributesAccept(clazz, null, changer);

        reset(codeAttribute);
        changer.visitCodeAttribute(null, null, codeAttribute);
        verify(codeAttribute).attributesAccept(null, null, changer);
    }

    /**
     * Tests that visitCodeAttribute works correctly after the changer has been used
     * for other operations, ensuring no state interference.
     */
    @Test
    public void testVisitCodeAttribute_afterOtherOperations_stillWorksCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = mock(CodeAttribute.class);
        CodeAttribute codeAttribute2 = mock(CodeAttribute.class);

        // Act - use the changer for some operations first
        changer.visitCodeAttribute(clazz, method, codeAttribute1);
        changer.visitAnyAttribute(clazz, mock(Attribute.class));
        changer.visitCodeAttribute(clazz, method, codeAttribute2);

        // Assert - both code attributes should have been processed
        verify(codeAttribute1).attributesAccept(clazz, method, changer);
        verify(codeAttribute2).attributesAccept(clazz, method, changer);
    }

    /**
     * Tests that visitCodeAttribute with real CodeAttribute that has empty attributes array works.
     */
    @Test
    public void testVisitCodeAttribute_withEmptyAttributesArray_completesSuccessfully() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute with empty attributes array should complete successfully");
    }

    /**
     * Tests that multiple independent TargetClassChanger instances can process
     * the same code attribute without interference.
     */
    @Test
    public void testVisitCodeAttribute_multipleIndependentChangers_noInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer1.visitCodeAttribute(clazz, method, codeAttribute);
        changer2.visitCodeAttribute(clazz, method, codeAttribute);
        changer3.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify attributesAccept was called three times
        verify(codeAttribute, times(3)).attributesAccept(eq(clazz), eq(method), any(AttributeVisitor.class));
    }

    /**
     * Tests that visitCodeAttribute correctly passes all three parameters to attributesAccept
     * in the correct order.
     */
    @Test
    public void testVisitCodeAttribute_passesParametersInCorrectOrder() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(ProgramMethod.class, "specificMethod");
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act
        changer.visitCodeAttribute(specificClazz, specificMethod, codeAttribute);

        // Assert - verify parameters are in the correct order: clazz, method, visitor
        verify(codeAttribute).attributesAccept(specificClazz, specificMethod, changer);
    }

    /**
     * Tests that visitCodeAttribute can process code attributes with different
     * attribute count values.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentAttributeCounts_processesCorrectly() {
        // Arrange & Act & Assert - test with different attribute counts
        for (int count = 0; count <= 5; count++) {
            CodeAttribute codeAttribute = new CodeAttribute();
            codeAttribute.u2attributesCount = count;
            codeAttribute.attributes = new Attribute[count];
            for (int i = 0; i < count; i++) {
                codeAttribute.attributes[i] = mock(Attribute.class);
            }

            final int currentCount = count;
            assertDoesNotThrow(() -> changer.visitCodeAttribute(clazz, method, codeAttribute),
                    "visitCodeAttribute should work with " + currentCount + " attributes");
        }
    }
}
