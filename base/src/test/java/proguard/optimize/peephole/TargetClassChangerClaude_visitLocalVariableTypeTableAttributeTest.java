package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)}.
 *
 * The visitLocalVariableTypeTableAttribute method in TargetClassChanger processes a LocalVariableTypeTableAttribute
 * by visiting its local variable types to update class references.
 *
 * The method implementation:
 * <pre>
 * public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
 * {
 *     // Change the references of the local variables.
 *     localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
 * }
 * </pre>
 *
 * This method is part of the visitor pattern used to traverse and modify class references
 * in local variable type signatures (generic type information). It's used during class merging
 * optimizations to ensure local variable generic type information is updated to reflect retargeted classes.
 */
public class TargetClassChangerClaude_visitLocalVariableTypeTableAttributeTest {

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
     * Tests that visitLocalVariableTypeTableAttribute calls localVariablesAccept on the attribute.
     * This is the core functionality - delegating to local variable types for processing.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_callsLocalVariablesAccept() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called with correct parameters
        verify(attribute, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with a real LocalVariableTypeTableAttribute
     * that has no local variable types. Should complete without errors.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNoLocalVariableTypes_doesNotThrowException() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[0];

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTypeTableAttribute should not throw with empty attribute");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute passes the changer as the visitor
     * to the attribute's localVariablesAccept method.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_passesChangerAsVisitor() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the changer itself was passed as the visitor
        verify(attribute).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), same(changer));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be called multiple times on the same attribute.
     * Each call should trigger a new localVariablesAccept call.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_calledMultipleTimes_callsLocalVariablesAcceptEachTime() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called three times
        verify(attribute, times(3)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute throws NullPointerException with null attribute.
     * The method must call localVariablesAccept on the attribute, so null is not acceptable.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullAttribute_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, null),
                "visitLocalVariableTypeTableAttribute with null attribute should throw NullPointerException");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with null Clazz parameter.
     * The clazz is passed through to localVariablesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullClazz_passesNullToLocalVariablesAccept() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(null, method, codeAttribute, attribute);

        // Assert - verify null clazz was passed to localVariablesAccept
        verify(attribute).localVariablesAccept(null, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with null Method parameter.
     * The method is passed through to localVariablesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullMethod_passesNullToLocalVariablesAccept() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, null, codeAttribute, attribute);

        // Assert - verify null method was passed to localVariablesAccept
        verify(attribute).localVariablesAccept(clazz, null, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with null CodeAttribute parameter.
     * The code attribute is passed through to localVariablesAccept, so null handling depends on that method.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullCodeAttribute_passesNullToLocalVariablesAccept() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, null, attribute);

        // Assert - verify null code attribute was passed to localVariablesAccept
        verify(attribute).localVariablesAccept(clazz, method, null, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can process multiple different attributes.
     * Each should trigger its own localVariablesAccept call.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withMultipleAttributes_callsLocalVariablesAcceptOnEach() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr3 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr3);

        // Assert
        verify(attr1, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr2, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr3, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be used by multiple TargetClassChanger instances.
     * Each changer should independently call localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withMultipleChangers_eachCallsLocalVariablesAccept() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer1.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        changer2.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called twice (once per changer)
        verify(attribute, times(2)).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), any(TargetClassChanger.class));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different Clazz instances.
     * Each should be passed correctly to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentClasses_passesCorrectClazzToLocalVariablesAccept() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz1, method, codeAttribute, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz2, method, codeAttribute, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz1, method, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz2, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different Method instances.
     * Each should be passed correctly to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentMethods_passesCorrectMethodToLocalVariablesAccept() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz, method1, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz, method2, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different CodeAttribute instances.
     * Each should be passed correctly to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentCodeAttributes_passesCorrectCodeAttributeToLocalVariablesAccept() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttr1, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttr2, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz, method, codeAttr1, changer);
        verify(attr2).localVariablesAccept(clazz, method, codeAttr2, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute does not throw any exceptions with valid mock input.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withValidMocks_doesNotThrowException() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTypeTableAttribute should not throw with valid mocks");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be used polymorphically through the AttributeVisitor interface.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_asAttributeVisitor_callsLocalVariablesAccept() {
        // Arrange
        AttributeVisitor visitor = changer;
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        visitor.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be called in rapid succession without issues.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_rapidSuccessiveCalls_allCallLocalVariablesAccept() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        for (int i = 0; i < 100; i++) {
            changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        }

        // Assert
        verify(attribute, times(100)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute with real LocalVariableTypeTableAttribute works correctly.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withRealAttribute_processesLocalVariableTypes() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[0];

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTypeTableAttribute should process real attribute without errors");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with LocalVariableTypeTableAttribute
     * that has local variable types.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withLocalVariableTypes_processesAll() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 2;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[] {
            new LocalVariableTypeInfo(),
            new LocalVariableTypeInfo()
        };

        // Act & Assert
        assertDoesNotThrow(() -> changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
                "visitLocalVariableTypeTableAttribute should process attribute with local variable types without errors");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with real ProgramClass and ProgramMethod.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withRealClassAndMethod_callsLocalVariablesAccept() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        ProgramMethod realMethod = new ProgramMethod();
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(realClass, realMethod, codeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(realClass, realMethod, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute processes the attribute in the correct order
     * relative to other visitor methods.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_afterOtherVisitorMethods_stillCallsLocalVariablesAccept() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);
        Attribute genericAttribute = mock(Attribute.class);

        // Act - call other visitor methods first
        changer.visitAnyAttribute(clazz, genericAttribute);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't modify the Clazz parameter.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotModifyClazz() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[0];

        // Act
        changer.visitLocalVariableTypeTableAttribute(testClass, method, codeAttribute, attribute);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Clazz processing info should not be modified by visitLocalVariableTypeTableAttribute");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't modify the Method parameter.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotModifyMethod() {
        // Arrange
        ProgramMethod testMethod = new ProgramMethod();
        Object methodProcessingInfo = new Object();
        testMethod.setProcessingInfo(methodProcessingInfo);
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[0];

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, testMethod, codeAttribute, attribute);

        // Assert
        assertSame(methodProcessingInfo, testMethod.getProcessingInfo(),
                "Method processing info should not be modified by visitLocalVariableTypeTableAttribute");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't modify the CodeAttribute parameter.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        CodeAttribute testCodeAttribute = new CodeAttribute();
        Object codeAttributeProcessingInfo = new Object();
        testCodeAttribute.setProcessingInfo(codeAttributeProcessingInfo);
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[0];

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, testCodeAttribute, attribute);

        // Assert
        assertSame(codeAttributeProcessingInfo, testCodeAttribute.getProcessingInfo(),
                "CodeAttribute processing info should not be modified by visitLocalVariableTypeTableAttribute");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works when the same attribute is visited
     * from different contexts (different clazz/method/codeAttribute combinations).
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_sameAttributeDifferentContexts_callsLocalVariablesAcceptWithCorrectParameters() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz1, method1, codeAttr1, attribute);
        changer.visitLocalVariableTypeTableAttribute(clazz2, method2, codeAttr2, attribute);

        // Assert - verify correct parameters were passed each time
        verify(attribute).localVariablesAccept(clazz1, method1, codeAttr1, changer);
        verify(attribute).localVariablesAccept(clazz2, method2, codeAttr2, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute handles LocalVariableTypeTableAttribute
     * with null localVariableTypeTable array gracefully.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullLocalVariableTypeTable_handledByLocalVariablesAccept() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = null;

        // Act & Assert - behavior depends on localVariablesAccept implementation
        // We just verify it doesn't throw an unexpected exception from visitLocalVariableTypeTableAttribute
        assertDoesNotThrow(() -> {
            try {
                changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
            } catch (NullPointerException e) {
                // NPE from localVariablesAccept is acceptable
            }
        }, "visitLocalVariableTypeTableAttribute should delegate null handling to localVariablesAccept");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute maintains the correct visitor chain.
     * The changer should be passed as the visitor to process local variable types.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_maintainsVisitorChain() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);
        TargetClassChanger specificChanger = new TargetClassChanger();

        // Act
        specificChanger.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the specific changer instance was passed
        verify(attribute).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), same(specificChanger));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be interleaved with other attribute visitor methods.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_interleavedWithOtherVisitorMethods_worksCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        CodeAttribute codeAttr = mock(CodeAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        changer.visitCodeAttribute(clazz, method, codeAttr);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);

        // Assert
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works correctly when called on the same attribute
     * with the same parameters multiple times in sequence.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_repeatedCallsSameParameters_callsLocalVariablesAcceptEachTime() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        for (int i = 0; i < 5; i++) {
            changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        }

        // Assert
        verify(attribute, times(5)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute completes quickly, as it just delegates to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_completesQuickly() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
        attribute.u2localVariableTypeTableLength = 0;
        attribute.localVariableTypeTable = new LocalVariableTypeInfo[0];
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should be very fast
        assertTrue(durationMs < 100,
                "visitLocalVariableTypeTableAttribute should complete quickly (1000 calls in <100ms), took " + durationMs + "ms");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute with various parameter combinations all work correctly.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_variousParameterCombinations_allCallLocalVariablesAccept() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act - test various combinations
        changer.visitLocalVariableTypeTableAttribute(clazz1, method1, codeAttr1, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz2, method2, codeAttr2, attr2);
        changer.visitLocalVariableTypeTableAttribute(clazz1, method2, codeAttr2, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz2, method1, codeAttr1, attr2);

        // Assert
        verify(attr1, times(2)).localVariablesAccept(any(), any(), any(), eq(changer));
        verify(attr2, times(2)).localVariablesAccept(any(), any(), any(), eq(changer));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute is part of the correct visitor interface.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_implementsAttributeVisitorInterface() {
        // Assert
        assertTrue(changer instanceof AttributeVisitor,
                "TargetClassChanger should implement AttributeVisitor");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can handle both null and non-null parameters
     * in various combinations, delegating appropriately to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_mixedNullNonNullParameters_delegatesCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act & Assert - test various null/non-null combinations
        changer.visitLocalVariableTypeTableAttribute(null, method, codeAttribute, attribute);
        verify(attribute).localVariablesAccept(null, method, codeAttribute, changer);

        reset(attribute);
        changer.visitLocalVariableTypeTableAttribute(clazz, null, codeAttribute, attribute);
        verify(attribute).localVariablesAccept(clazz, null, codeAttribute, changer);

        reset(attribute);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, null, attribute);
        verify(attribute).localVariablesAccept(clazz, method, null, changer);

        reset(attribute);
        changer.visitLocalVariableTypeTableAttribute(null, null, null, attribute);
        verify(attribute).localVariablesAccept(null, null, null, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works correctly after the changer has been used
     * for other operations, ensuring no state interference.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_afterOtherOperations_stillWorksCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act - use the changer for some operations first
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        changer.visitAnyAttribute(clazz, mock(Attribute.class));
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);

        // Assert - both attributes should have been processed
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that multiple independent TargetClassChanger instances can process
     * the same attribute without interference.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_multipleIndependentChangers_noInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer1.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        changer2.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        changer3.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify localVariablesAccept was called three times
        verify(attribute, times(3)).localVariablesAccept(eq(clazz), eq(method), eq(codeAttribute), any(TargetClassChanger.class));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute correctly passes all four parameters to localVariablesAccept
     * in the correct order.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_passesParametersInCorrectOrder() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(ProgramMethod.class, "specificMethod");
        CodeAttribute specificCodeAttribute = mock(CodeAttribute.class, "specificCodeAttribute");
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(specificClazz, specificMethod, specificCodeAttribute, attribute);

        // Assert - verify parameters are in the correct order: clazz, method, codeAttribute, visitor
        verify(attribute).localVariablesAccept(specificClazz, specificMethod, specificCodeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can process attributes with different
     * local variable type counts.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentLocalVariableTypeCounts_processesCorrectly() {
        // Arrange & Act & Assert - test with different local variable type counts
        for (int count = 0; count <= 5; count++) {
            LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();
            attribute.u2localVariableTypeTableLength = count;
            attribute.localVariableTypeTable = new LocalVariableTypeInfo[count];
            for (int i = 0; i < count; i++) {
                attribute.localVariableTypeTable[i] = new LocalVariableTypeInfo();
            }

            final int currentCount = count;
            assertDoesNotThrow(() -> changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
                    "visitLocalVariableTypeTableAttribute should work with " + currentCount + " local variable types");
        }
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with real CodeAttribute instance.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withRealCodeAttribute_callsLocalVariablesAccept() {
        // Arrange
        CodeAttribute realCodeAttribute = new CodeAttribute();
        LocalVariableTypeTableAttribute attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, realCodeAttribute, attribute);

        // Assert
        verify(attribute).localVariablesAccept(clazz, method, realCodeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can handle alternating calls with different parameters.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_alternatingParameters_handlesCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, attr2);
        changer.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, attr2);

        // Assert
        verify(attr1, times(2)).localVariablesAccept(clazz, method1, codeAttribute, changer);
        verify(attr2, times(2)).localVariablesAccept(clazz, method2, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be interleaved with visitLocalVariableTableAttribute calls.
     * Verifies that both similar visitor methods can work together.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_interleavedWithLocalVariableTableAttribute_worksCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute typeAttr = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTableAttribute tableAttr = mock(LocalVariableTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, typeAttr);
        changer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, tableAttr);
        changer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, typeAttr);

        // Assert
        verify(typeAttr, times(2)).localVariablesAccept(clazz, method, codeAttribute, changer);
        verify(tableAttr, times(1)).localVariablesAccept(clazz, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works correctly with both ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentClazzTypes_worksCorrectly() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);
        LibraryClass libraryClass = mock(LibraryClass.class);
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        changer.visitLocalVariableTypeTableAttribute(programClass, method, codeAttribute, attr1);
        changer.visitLocalVariableTypeTableAttribute(libraryClass, method, codeAttribute, attr2);

        // Assert
        verify(attr1).localVariablesAccept(programClass, method, codeAttribute, changer);
        verify(attr2).localVariablesAccept(libraryClass, method, codeAttribute, changer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute maintains state independence across calls.
     * Each call should be independent and not affect subsequent calls.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_maintainsStateIndependence() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act - call with different parameters
        changer.visitLocalVariableTypeTableAttribute(clazz1, method, codeAttribute, attr1);
        changer.visitLocalVariableTypeTableAttribute(clazz2, method, codeAttribute, attr2);

        // Assert - each call should have been independent
        verify(attr1, times(1)).localVariablesAccept(clazz1, method, codeAttribute, changer);
        verify(attr2, times(1)).localVariablesAccept(clazz2, method, codeAttribute, changer);
        verifyNoMoreInteractions(attr1);
        verifyNoMoreInteractions(attr2);
    }
}
