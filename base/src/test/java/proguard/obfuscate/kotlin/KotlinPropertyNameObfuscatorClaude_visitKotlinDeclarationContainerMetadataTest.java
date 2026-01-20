package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinPropertyNameObfuscator#visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)}.
 * Tests the visitKotlinDeclarationContainerMetadata method which resets the name factory
 * and delegates to propertiesAccept.
 */
public class KotlinPropertyNameObfuscatorClaude_visitKotlinDeclarationContainerMetadataTest {

    private KotlinPropertyNameObfuscator obfuscator;
    private NameFactory mockNameFactory;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        mockNameFactory = mock(NameFactory.class);
        obfuscator = new KotlinPropertyNameObfuscator(mockNameFactory);
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinDeclarationContainerMetadata.class);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called without throwing exceptions.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "visitKotlinDeclarationContainerMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls reset on the NameFactory.
     * This verifies that the name factory is reset before processing properties.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsNameFactoryReset() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockNameFactory, times(1)).reset();
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls propertiesAccept on the metadata.
     * This verifies that the method delegates to process properties.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsPropertiesAccept() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the obfuscator itself as the visitor.
     * This verifies that the same obfuscator instance is used for visitor callbacks.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesObfuscatorAsVisitor() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that the obfuscator instance is passed as the visitor
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, obfuscator);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls reset before propertiesAccept.
     * This verifies the correct order of operations.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsResetBeforePropertiesAccept() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockNameFactory, mockMetadata);

        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify reset is called before propertiesAccept
        inOrder.verify(mockNameFactory).reset();
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, obfuscator);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_canBeCalledMultipleTimes() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers reset and propertiesAccept
        verify(mockNameFactory, times(3)).reset();
        verify(mockMetadata, times(3)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, obfuscator);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz2, obfuscator);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withDifferentMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        // And reset is still called for each
        verify(mockNameFactory, times(2)).reset();
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null Clazz does not throw but delegates.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullClazz_delegatesToMetadata() {
        // Act & Assert - should delegate to metadata (may or may not throw depending on metadata implementation)
        obfuscator.visitKotlinDeclarationContainerMetadata(null, mockMetadata);

        // Verify that reset was called and propertiesAccept was made with null clazz
        verify(mockNameFactory, times(1)).reset();
        verify(mockMetadata, times(1)).propertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitKotlinDeclarationContainerMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata resets the name factory even with null metadata
     * would fail due to NPE before that point.
     * This test verifies that reset is called first but NPE is thrown when accessing null metadata.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullMetadata_resetsBeforeThrowingNPE() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, null);
        });

        // Reset should have been called before the NPE
        verify(mockNameFactory, times(1)).reset();
    }

    /**
     * Tests that multiple instances of KotlinPropertyNameObfuscator behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        NameFactory mockFactory1 = mock(NameFactory.class);
        NameFactory mockFactory2 = mock(NameFactory.class);
        KotlinPropertyNameObfuscator obfuscator1 = new KotlinPropertyNameObfuscator(mockFactory1);
        KotlinPropertyNameObfuscator obfuscator2 = new KotlinPropertyNameObfuscator(mockFactory2);
        KotlinDeclarationContainerMetadata mockMetadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        obfuscator1.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata1);
        obfuscator2.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same calls
        verify(mockFactory1, times(1)).reset();
        verify(mockMetadata1, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));

        verify(mockFactory2, times(1)).reset();
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with a real NameFactory properly resets it.
     * Verifies the reset operation with a concrete implementation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withRealNameFactory_resetsFactory() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinPropertyNameObfuscator obfuscatorWithRealFactory = new KotlinPropertyNameObfuscator(realFactory);

        // Advance the factory state
        assertEquals("a", realFactory.nextName());
        assertEquals("b", realFactory.nextName());
        assertEquals("c", realFactory.nextName());

        // Act - calling visitKotlinDeclarationContainerMetadata should reset the factory
        obfuscatorWithRealFactory.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - after reset, nextName should return "a" again
        assertEquals("a", realFactory.nextName(),
                     "NameFactory should have been reset during visitKotlinDeclarationContainerMetadata");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata resets the factory each time it's called.
     * Verifies that reset is consistently called on every invocation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_resetsFactoryEachTime() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinPropertyNameObfuscator obfuscatorWithRealFactory = new KotlinPropertyNameObfuscator(realFactory);

        // Act & Assert - each call should reset the factory
        obfuscatorWithRealFactory.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        assertEquals("a", realFactory.nextName(), "First reset: should return 'a'");

        realFactory.nextName(); // advance to 'c'
        obfuscatorWithRealFactory.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        assertEquals("a", realFactory.nextName(), "Second reset: should return 'a'");

        realFactory.nextName(); // advance to 'c'
        realFactory.nextName(); // advance to 'd'
        obfuscatorWithRealFactory.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        assertEquals("a", realFactory.nextName(), "Third reset: should return 'a'");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata only calls reset once per invocation.
     * Verifies that reset is not called multiple times unnecessarily.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsResetExactlyOnce() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify reset is called exactly once, not more
        verify(mockNameFactory, times(1)).reset();
        verify(mockNameFactory, never()).nextName(); // nextName should not be called by this method
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does not call nextName on the factory.
     * Verifies that only reset is called, not nextName.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotCallNextName() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does not interact with the Clazz parameter directly.
     * The clazz is only passed to propertiesAccept, not used directly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotInteractWithClazz() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify no direct interactions with clazz (it's only passed to propertiesAccept)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata delegates all work to propertiesAccept.
     * Verifies that only propertiesAccept is called on the metadata, not other methods.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_onlyCallsPropertiesAccept() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify only propertiesAccept is called, not typeAliasesAccept, functionsAccept, etc.
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, obfuscator);
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests the complete workflow: reset then propertiesAccept.
     * Verifies that both operations happen in sequence.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_completeWorkflow() {
        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify both operations occurred
        verify(mockNameFactory, times(1)).reset();
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, obfuscator);

        // And verify the order
        org.mockito.InOrder inOrder = inOrder(mockNameFactory, mockMetadata);
        inOrder.verify(mockNameFactory).reset();
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, obfuscator);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes correct parameters through.
     * Verifies parameter integrity throughout the method execution.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesCorrectParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata specificMetadata = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(specificClazz, specificMetadata);

        // Assert - verify the exact parameters are passed
        verify(specificMetadata, times(1)).propertiesAccept(specificClazz, obfuscator);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with sequential calls
     * resets the factory before each propertiesAccept call.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_sequentialCalls_resetEachTime() {
        // Arrange
        KotlinDeclarationContainerMetadata metadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata2 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata3 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, metadata1);
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, metadata2);
        obfuscator.visitKotlinDeclarationContainerMetadata(mockClazz, metadata3);

        // Assert - reset should be called before each propertiesAccept
        verify(mockNameFactory, times(3)).reset();
        verify(metadata1, times(1)).propertiesAccept(mockClazz, obfuscator);
        verify(metadata2, times(1)).propertiesAccept(mockClazz, obfuscator);
        verify(metadata3, times(1)).propertiesAccept(mockClazz, obfuscator);
    }
}
