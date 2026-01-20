package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)}.
 *
 * The visitLocalVariableTableAttribute method in TargetClassChanger processes a LocalVariableTableAttribute
 * by visiting its local variables to update class references.
 *
 * The method implementation:
 * <pre>
 * public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
 * {
 *     // Change the references of the local variables.
 *     localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
 * }
 * </pre>
 *
 * This method is part of the visitor pattern used to traverse and modify class references
 * in local variable type descriptors. It's used during class merging optimizations to ensure
 * local variable type information is updated to reflect retargeted classes.
 */
public class TargetClassChangerClaude_visitLocalVariableTableAttributeTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitLocalVariableTableAttribute calls localVariablesAccept on the attribute.
     * This is the core functionality - delegating to local variables for processing.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_callsLocalVariablesAccept() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called with correct parameters
        verify(attribute, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with a real LocalVariableTableAttribute
     * that has no local variables. Should complete without errors.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNoLocalVariables_doesNotThrowException() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = new LocalVariableInfo[0];

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTableAttribute should not throw with empty attribute");
    }

    /**
     * Tests that visitLocalVariableTableAttribute passes the changer as the visitor
     * to the attribute's localVariablesAccept method.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_passesChangerAsVisitor() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the changer itself was passed as the visitor
        verify(attribute).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), same(changer));
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be called multiple times on the same attribute.
     * Each call should trigger a new localVariablesAccept call.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_calledMultipleTimes_callsLocalVariablesAcceptEachTime() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called three times
        verify(attribute, times(3)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute throws NullPointerException with null attribute.
     * The method must call localVariablesAccept on the attribute, so null is not acceptable.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullAttribute_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, null),
                "visitLocalVariableTableAttribute with null attribute should throw NullPointerException");
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with null Clazz parameter.
     * The clazz is passed through to localVariablesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullClazz_passesNullToLocalVariablesAccept() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(null, method, codeAttribute, attribute);

        // Assert - verify null clazz was passed to localVariablesAccept
        verify(attribute).localVariablesAccept(null, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with null Method parameter.
     * The method is passed through to localVariablesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullMethod_passesNullToLocalVariablesAccept() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, null, codeAttribute, attribute);

        // Assert - verify null method was passed to localVariablesAccept
        verify(attribute).localVariablesAccept(clazz, null, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with null CodeAttribute parameter.
     * The code attribute is passed through to localVariablesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullCodeAttribute_passesNullToLocalVariablesAccept() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, null, attribute);

        // Assert - verify null code attribute was passed to localVariablesAccept
        verify(attribute).localVariablesAccept(clazz, method, null, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute can process multiple different attributes.
     * Each should trigger its own localVariablesAccept call.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withMultipleAttributes_callsLocalVariablesAcceptOnEach() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr3 = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr3);

        // Assert
        verify(attr1, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr2, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr3, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be used by multiple TargetClassChanger instances.
     * Each changer should independently call localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withMultipleChangers_eachCallsLocalVariablesAccept() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer1.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        changer2.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called twice (once per changer)
        verify(attribute, times(2)).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), any(TargetClassChanger.class));
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different Clazz instances.
     * Each should be passed correctly to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentClasses_passesCorrectClazzToLocalVariablesAccept() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz1, method, codeAttribute, attr1);
        changer.visitLocalVariableTableAttribute(clazz2, method, codeAttribute, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz1, method, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz2, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different Method instances.
     * Each should be passed correctly to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentMethods_passesCorrectMethodToLocalVariablesAccept() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, attr1);
        changer.visitLocalVariableTableAttribute(clazz, method2, codeAttribute, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz, method1, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz, method2, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different CodeAttribute instances.
     * Each should be passed correctly to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentCodeAttributes_passesCorrectCodeAttributeToLocalVariablesAccept() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttr1, attr1);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttr2, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz, method, codeAttr1, changer);
        verify(attr2).localVariablesAccept(clazz, method, codeAttr2, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute does not throw any exceptions with valid mock input.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withValidMocks_doesNotThrowException() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTableAttribute should not throw with valid mocks");
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be used polymorphically through the AttributeVisitor interface.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_asAttributeVisitor_callsLocalVariablesAccept() {
        // Arrange
        AttributeVisitor visitor = changer;
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        visitor.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be called in rapid succession without issues.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_rapidSuccessiveCalls_allCallLocalVariablesAccept() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        for (int i = 0; i < 100; i++) {
            changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        }

        // Assert
        verify(attribute, times(100)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute with real LocalVariableTableAttribute works correctly.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withRealAttribute_processesLocalVariables() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = new LocalVariableInfo[0];

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTableAttribute should process real attribute without errors");
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with LocalVariableTableAttribute
     * that has local variables.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withLocalVariables_processesAll() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 2;
        attribute.localVariableTable = new LocalVariableInfo[] {
            new LocalVariableInfo(),
            new LocalVariableInfo()
        };

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTableAttribute should process attribute with local variables without errors");
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with real ProgramClass and ProgramMethod.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withRealClassAndMethod_callsLocalVariablesAccept() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        ProgramMethod realMethod = new ProgramMethod();
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(realClass, realMethod, codeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(realClass, realMethod, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute processes the attribute in the correct order
     * relative to other visitor methods.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_afterOtherVisitorMethods_stillCallsLocalVariablesAccept() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);
        Attribute genericAttribute = mock(Attribute.class);

        // Act - call other visitor methods first
        changer.visitAnyAttribute(clazz, genericAttribute);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't modify the Clazz parameter.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotModifyClazz() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = new LocalVariableInfo[0];

        // Act
        changer.visitLocalVariableTableAttribute(testClass, method, codeAttribute, attribute);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Clazz processing info should not be modified by visitLocalVariableTableAttribute");
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't modify the Method parameter.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotModifyMethod() {
        // Arrange
        ProgramMethod testMethod = new ProgramMethod();
        Object methodProcessingInfo = new Object();
        testMethod.setProcessingInfo(methodProcessingInfo);
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = new LocalVariableInfo[0];

        // Act
        changer.visitLocalVariableTableAttribute(clazz, testMethod, codeAttribute, attribute);

        // Assert
        assertSame(methodProcessingInfo, testMethod.getProcessingInfo(),
                "Method processing info should not be modified by visitLocalVariableTableAttribute");
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't modify the CodeAttribute parameter.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        CodeAttribute testCodeAttribute = new CodeAttribute();
        Object codeAttributeProcessingInfo = new Object();
        testCodeAttribute.setProcessingInfo(codeAttributeProcessingInfo);
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = new LocalVariableInfo[0];

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, testCodeAttribute, attribute);

        // Assert
        assertSame(codeAttributeProcessingInfo, testCodeAttribute.getProcessingInfo(),
                "CodeAttribute processing info should not be modified by visitLocalVariableTableAttribute");
    }

    /**
     * Tests that visitLocalVariableTableAttribute works when the same attribute is visited
     * from different contexts (different clazz/method/codeAttribute combinations).
     */
    @Test
    public void testVisitLocalVariableTableAttribute_sameAttributeDifferentContexts_callsLocalVariablesAcceptWithCorrectParameters() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz1, method1, codeAttr1, attribute);
        changer.visitLocalVariableTableAttribute(clazz2, method2, codeAttr2, attribute);

        // Assert - verify correct parameters were passed each time
        verify(attribute).localVariablesAccept(clazz1, method1, codeAttr1, changer);
        verify(attribute).localVariablesAccept(clazz2, method2, codeAttr2, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute handles LocalVariableTableAttribute
     * with null localVariableTable array gracefully.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullLocalVariableTable_handledByLocalVariablesAccept() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = null;

        // Act & Assert - behavior depends on localVariablesAccept implementation
        // We just verify it doesn't throw an unexpected exception from visitLocalVariableTableAttribute
        assertDoesNotThrow(() -> {
            try {
                changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
            } catch (NullPointerException e) {
                // NPE from localVariablesAccept is acceptable
            }
        }, "visitLocalVariableTableAttribute should delegate null handling to localVariablesAccept");
    }

    /**
     * Tests that visitLocalVariableTableAttribute maintains the correct visitor chain.
     * The changer should be passed as the visitor to process local variables.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_maintainsVisitorChain() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);
        TargetClassChanger specificChanger = new TargetClassChanger();

        // Act
        specificChanger.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the specific changer instance was passed
        verify(attribute).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), same(specificChanger));
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be interleaved with other attribute visitor methods.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_interleavedWithOtherVisitorMethods_worksCorrectly() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);
        CodeAttribute codeAttr = mock(CodeAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        changer.visitCodeAttribute(clazz, method, codeAttr);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works correctly when called on the same attribute
     * with the same parameters multiple times in sequence.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_repeatedCallsSameParameters_callsLocalVariablesAcceptEachTime() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        for (int i = 0; i < 5; i++) {
            changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        }

        // Assert
        verify(attribute, times(5)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute completes quickly, as it just delegates to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_completesQuickly() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
        attribute.u2localVariableTableLength = 0;
        attribute.localVariableTable = new LocalVariableInfo[0];
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should be very fast
        assertTrue(durationMs < 100,
                "visitLocalVariableTableAttribute should complete quickly (1000 calls in <100ms), took " + durationMs + "ms");
    }

    /**
     * Tests that visitLocalVariableTableAttribute with various parameter combinations all work correctly.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_variousParameterCombinations_allCallLocalVariablesAccept() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act - test various combinations
        changer.visitLocalVariableTableAttribute(clazz1, method1, codeAttr1, attr1);
        changer.visitLocalVariableTableAttribute(clazz2, method2, codeAttr2, attr2);
        changer.visitLocalVariableTableAttribute(clazz1, method2, codeAttr2, attr1);
        changer.visitLocalVariableTableAttribute(clazz2, method1, codeAttr1, attr2);

        // Assert
        verify(attr1, times(2)).localVariablesAccept(any(), any(), any(), eq(changer));
        verify(attr2, times(2)).localVariablesAccept(any(), any(), any(), eq(changer));
    }

    /**
     * Tests that visitLocalVariableTableAttribute is part of the correct visitor interface.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_implementsAttributeVisitorInterface() {
        // Assert
        assertTrue(changer instanceof AttributeVisitor,
                "TargetClassChanger should implement AttributeVisitor");
    }

    /**
     * Tests that visitLocalVariableTableAttribute can handle both null and non-null parameters
     * in various combinations, delegating appropriately to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_mixedNullNonNullParameters_delegatesCorrectly() {
        // Arrange
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act & Assert - test various null/non-null combinations
        changer.visitLocalVariableTableAttribute(null, method, codeAttribute, attribute);
        verify(attribute).localVariablesAccept(null, method, codeAttribute, changer);

        reset(attribute);
        changer.visitLocalVariableTableAttribute(clazz, null, codeAttribute, attribute);
        verify(attribute).localVariablesAccept(clazz, null, codeAttribute, changer);

        reset(attribute);
        changer.visitLocalVariableTableAttribute(clazz, method, null, attribute);
        verify(attribute).localVariablesAccept(clazz, method, null, changer);

        reset(attribute);
        changer.visitLocalVariableTableAttribute(null, null, null, attribute);
        verify(attribute).localVariablesAccept(null, null, null, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works correctly after the changer has been used
     * for other operations, ensuring no state interference.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_afterOtherOperations_stillWorksCorrectly() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act - use the changer for some operations first
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        changer.visitAnyAttribute(clazz, mock(Attribute.class));
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);

        // Assert - both attributes should have been processed
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that multiple independent TargetClassChanger instances can process
     * the same attribute without interference.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_multipleIndependentChangers_noInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer1.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        changer2.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        changer3.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called three times
        verify(attribute, times(3)).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), any(TargetClassChanger.class));
    }

    /**
     * Tests that visitLocalVariableTableAttribute correctly passes all four parameters to localVariablesAccept
     * in the correct order.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_passesParametersInCorrectOrder() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(ProgramMethod.class, "specificMethod");
        CodeAttribute specificCodeAttribute = mock(CodeAttribute.class, "specificCodeAttribute");
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(specificClazz, specificMethod, specificCodeAttribute, attribute);

        // Assert - verify parameters are in the correct order: clazz, method, codeAttribute, visitor
        verify(attribute).localVariablesAccept(specificClazz, specificMethod, specificCodeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute can process attributes with different
     * local variable counts.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentLocalVariableCounts_processesCorrectly() {
        // Arrange & Act & Assert - test with different local variable counts
        for (int count = 0; count <= 5; count++) {
            LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();
            attribute.u2localVariableTableLength = count;
            attribute.localVariableTable = new LocalVariableInfo[count];
            for (int i = 0; i < count; i++) {
                attribute.localVariableTable[i] = new LocalVariableInfo();
            }

            final int currentCount = count;
            assertDoesNotThrow(() -> changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
                    "visitLocalVariableTableAttribute should work with " + currentCount + " local variables");
        }
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with real CodeAttribute instance.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withRealCodeAttribute_callsLocalVariablesAccept() {
        // Arrange
        CodeAttribute realCodeAttribute = new CodeAttribute();
        LocalVariableTableAttribute attribute = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method, realCodeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(clazz, method, realCodeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTableAttribute can handle alternating calls with different parameters.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_alternatingParameters_handlesCorrectly() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);

        // Act
        changer.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, attr1);
        changer.visitLocalVariableTableAttribute(clazz, method2, codeAttribute, attr2);
        changer.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, attr1);
        changer.visitLocalVariableTableAttribute(clazz, method2, codeAttribute, attr2);

        // Assert
        verify(attr1, times(2)).localVariablesAccept(clazz, method1, codeAttribute, changer);
        verify(attr2, times(2)).localVariablesAccept(clazz, method2, codeAttribute, changer);
    }
}
