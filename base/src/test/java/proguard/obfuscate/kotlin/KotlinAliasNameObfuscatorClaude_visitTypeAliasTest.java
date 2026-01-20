package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinTypeAliasMetadata;
import proguard.classfile.kotlin.visitor.KotlinTypeVisitor;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAliasNameObfuscator#visitTypeAlias(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata)}.
 * Tests the visitTypeAlias method which delegates to expandedTypeAccept.
 */
public class KotlinAliasNameObfuscatorClaude_visitTypeAliasTest {

    private KotlinAliasNameObfuscator obfuscator;
    private NameFactory mockNameFactory;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockContainerMetadata;
    private KotlinTypeAliasMetadata mockTypeAliasMetadata;

    @BeforeEach
    public void setUp() {
        mockNameFactory = mock(NameFactory.class);
        obfuscator = new KotlinAliasNameObfuscator(mockNameFactory);
        mockClazz = mock(Clazz.class);
        mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        mockTypeAliasMetadata = mock(KotlinTypeAliasMetadata.class);
    }

    /**
     * Tests that visitTypeAlias can be called without throwing exceptions.
     */
    @Test
    public void testVisitTypeAlias_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        }, "visitTypeAlias should not throw an exception");
    }

    /**
     * Tests that visitTypeAlias calls expandedTypeAccept on the type alias metadata.
     * This verifies that the method delegates to process the expanded type.
     */
    @Test
    public void testVisitTypeAlias_callsExpandedTypeAccept() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(eq(mockClazz), eq(mockContainerMetadata), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitTypeAlias passes the obfuscator itself as the visitor.
     * This verifies that the same obfuscator instance is used for visitor callbacks.
     */
    @Test
    public void testVisitTypeAlias_passesObfuscatorAsVisitor() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify that the obfuscator instance is passed as the visitor
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
    }

    /**
     * Tests that visitTypeAlias passes all parameters correctly to expandedTypeAccept.
     * Verifies parameter integrity throughout the delegation.
     */
    @Test
    public void testVisitTypeAlias_passesAllParametersCorrectly() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify exact parameters are passed
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
    }

    /**
     * Tests that visitTypeAlias can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitTypeAlias_canBeCalledMultipleTimes() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify that each call triggers expandedTypeAccept
        verify(mockTypeAliasMetadata, times(3))
            .expandedTypeAccept(eq(mockClazz), eq(mockContainerMetadata), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitTypeAlias works with different Clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitTypeAlias_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        obfuscator.visitTypeAlias(mockClazz2, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz2, mockContainerMetadata, obfuscator);
    }

    /**
     * Tests that visitTypeAlias works with different container metadata instances.
     * This verifies that the method properly handles different container parameters.
     */
    @Test
    public void testVisitTypeAlias_withDifferentContainer_passesCorrectContainer() {
        // Arrange
        KotlinDeclarationContainerMetadata mockContainer2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        obfuscator.visitTypeAlias(mockClazz, mockContainer2, mockTypeAliasMetadata);

        // Assert - verify that the correct container is passed to each call
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz, mockContainer2, obfuscator);
    }

    /**
     * Tests that visitTypeAlias works with different type alias metadata instances.
     * This verifies that the method properly handles different type alias parameters.
     */
    @Test
    public void testVisitTypeAlias_withDifferentTypeAlias_callsCorrectMetadata() {
        // Arrange
        KotlinTypeAliasMetadata mockTypeAlias2 = mock(KotlinTypeAliasMetadata.class);

        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAlias2);

        // Assert - verify that each type alias metadata's method is called
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(eq(mockClazz), eq(mockContainerMetadata), any(KotlinTypeVisitor.class));
        verify(mockTypeAlias2, times(1))
            .expandedTypeAccept(eq(mockClazz), eq(mockContainerMetadata), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitTypeAlias with null Clazz delegates to the metadata.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitTypeAlias_withNullClazz_delegatesToMetadata() {
        // Act
        obfuscator.visitTypeAlias(null, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify that the call was made with null clazz
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(eq(null), eq(mockContainerMetadata), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitTypeAlias with null container metadata delegates to the metadata.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitTypeAlias_withNullContainer_delegatesToMetadata() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, null, mockTypeAliasMetadata);

        // Assert - verify that the call was made with null container
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(eq(mockClazz), eq(null), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitTypeAlias with null type alias metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitTypeAlias_withNullTypeAlias_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, null);
        }, "Should throw NullPointerException when type alias metadata is null");
    }

    /**
     * Tests that visitTypeAlias with all parameters null throws NullPointerException.
     */
    @Test
    public void testVisitTypeAlias_withAllNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitTypeAlias(null, null, null);
        }, "Should throw NullPointerException when type alias metadata is null");
    }

    /**
     * Tests that visitTypeAlias does not directly interact with the Clazz parameter.
     * The clazz is only passed to expandedTypeAccept, not used directly.
     */
    @Test
    public void testVisitTypeAlias_doesNotInteractWithClazz() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify no direct interactions with clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitTypeAlias does not directly interact with the container metadata.
     * The container is only passed to expandedTypeAccept, not used directly.
     */
    @Test
    public void testVisitTypeAlias_doesNotInteractWithContainer() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify no direct interactions with container
        verifyNoInteractions(mockContainerMetadata);
    }

    /**
     * Tests that visitTypeAlias does not interact with the NameFactory.
     * This method only delegates, it doesn't use the name factory.
     */
    @Test
    public void testVisitTypeAlias_doesNotInteractWithNameFactory() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify no interactions with the name factory
        verifyNoInteractions(mockNameFactory);
    }

    /**
     * Tests that visitTypeAlias only calls expandedTypeAccept on the type alias metadata.
     * Verifies that only the expected method is called.
     */
    @Test
    public void testVisitTypeAlias_onlyCallsExpandedTypeAccept() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify only expandedTypeAccept is called
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
        verifyNoMoreInteractions(mockTypeAliasMetadata);
    }

    /**
     * Tests that multiple instances of KotlinAliasNameObfuscator behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitTypeAlias_consistentBehaviorAcrossInstances() {
        // Arrange
        NameFactory mockFactory1 = mock(NameFactory.class);
        NameFactory mockFactory2 = mock(NameFactory.class);
        KotlinAliasNameObfuscator obfuscator1 = new KotlinAliasNameObfuscator(mockFactory1);
        KotlinAliasNameObfuscator obfuscator2 = new KotlinAliasNameObfuscator(mockFactory2);
        KotlinTypeAliasMetadata mockAlias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata mockAlias2 = mock(KotlinTypeAliasMetadata.class);

        // Act
        obfuscator1.visitTypeAlias(mockClazz, mockContainerMetadata, mockAlias1);
        obfuscator2.visitTypeAlias(mockClazz, mockContainerMetadata, mockAlias2);

        // Assert - both should make the same calls
        verify(mockAlias1, times(1))
            .expandedTypeAccept(eq(mockClazz), eq(mockContainerMetadata), any(KotlinTypeVisitor.class));
        verify(mockAlias2, times(1))
            .expandedTypeAccept(eq(mockClazz), eq(mockContainerMetadata), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitTypeAlias with a real NameFactory doesn't use it.
     * Verifies that the name factory is not invoked during this delegation method.
     */
    @Test
    public void testVisitTypeAlias_withRealNameFactory_doesNotUseIt() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinAliasNameObfuscator obfuscatorWithRealFactory = new KotlinAliasNameObfuscator(realFactory);

        // Act
        obfuscatorWithRealFactory.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - The factory's internal state should not have changed
        assertEquals("a", realFactory.nextName(),
                     "NameFactory should not have been used during visitTypeAlias");
    }

    /**
     * Tests that visitTypeAlias preserves the state of a real NameFactory across multiple calls.
     * Verifies that the factory state is completely untouched.
     */
    @Test
    public void testVisitTypeAlias_preservesNameFactoryState() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinAliasNameObfuscator obfuscatorWithRealFactory = new KotlinAliasNameObfuscator(realFactory);

        // Advance the factory state
        assertEquals("a", realFactory.nextName());
        assertEquals("b", realFactory.nextName());
        assertEquals("c", realFactory.nextName());

        // Act - call visitTypeAlias multiple times
        obfuscatorWithRealFactory.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        obfuscatorWithRealFactory.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
        obfuscatorWithRealFactory.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - the factory should continue from where it left off (should return "d", not "a")
        assertEquals("d", realFactory.nextName(),
                     "NameFactory state should be preserved - visitTypeAlias should not reset or use it");
    }

    /**
     * Tests that visitTypeAlias returns void as expected.
     * Verifies the method signature and behavior.
     */
    @Test
    public void testVisitTypeAlias_returnsVoid() {
        // Act - method returns void, so just verify it executes
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitTypeAlias with various combinations of parameters.
     * Verifies the method handles different scenarios correctly.
     */
    @Test
    public void testVisitTypeAlias_withVariousParameterCombinations() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        KotlinDeclarationContainerMetadata container1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata container2 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinTypeAliasMetadata alias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias2 = mock(KotlinTypeAliasMetadata.class);

        // Act & Assert - various combinations should all work
        assertDoesNotThrow(() -> {
            obfuscator.visitTypeAlias(clazz1, container1, alias1);
            obfuscator.visitTypeAlias(clazz2, container1, alias1);
            obfuscator.visitTypeAlias(clazz1, container2, alias1);
            obfuscator.visitTypeAlias(clazz1, container1, alias2);
            obfuscator.visitTypeAlias(clazz2, container2, alias2);
        }, "Should handle various parameter combinations");
    }

    /**
     * Tests that visitTypeAlias delegates the complete workflow to expandedTypeAccept.
     * Verifies the delegation chain.
     */
    @Test
    public void testVisitTypeAlias_completeWorkflow() {
        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);

        // Assert - verify the delegation occurred
        verify(mockTypeAliasMetadata, times(1))
            .expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);

        // And verify no other interactions
        verifyNoInteractions(mockNameFactory);
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockContainerMetadata);
    }

    /**
     * Tests that visitTypeAlias with sequential calls on different type aliases
     * delegates to each one correctly.
     */
    @Test
    public void testVisitTypeAlias_sequentialCalls_delegateToEachAlias() {
        // Arrange
        KotlinTypeAliasMetadata alias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias2 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias3 = mock(KotlinTypeAliasMetadata.class);

        // Act
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, alias1);
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, alias2);
        obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, alias3);

        // Assert - each alias should have expandedTypeAccept called once
        verify(alias1, times(1)).expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
        verify(alias2, times(1)).expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
        verify(alias3, times(1)).expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
    }

    /**
     * Tests that visitTypeAlias correctly passes all three parameters in the right order.
     * Verifies parameter order is preserved.
     */
    @Test
    public void testVisitTypeAlias_passesParametersInCorrectOrder() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata specificContainer = mock(KotlinDeclarationContainerMetadata.class);
        KotlinTypeAliasMetadata specificAlias = mock(KotlinTypeAliasMetadata.class);

        // Act
        obfuscator.visitTypeAlias(specificClazz, specificContainer, specificAlias);

        // Assert - verify the exact parameters in exact order
        verify(specificAlias, times(1))
            .expandedTypeAccept(specificClazz, specificContainer, obfuscator);
    }

    /**
     * Tests that visitTypeAlias can handle rapid sequential calls.
     * Verifies stability under repeated invocation.
     */
    @Test
    public void testVisitTypeAlias_rapidSequentialCalls() {
        // Act & Assert - should handle many rapid calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                obfuscator.visitTypeAlias(mockClazz, mockContainerMetadata, mockTypeAliasMetadata);
            }
        }, "Should handle rapid sequential calls");

        // Verify the expected number of calls occurred
        verify(mockTypeAliasMetadata, times(50))
            .expandedTypeAccept(mockClazz, mockContainerMetadata, obfuscator);
    }
}
