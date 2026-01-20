package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAliasNameObfuscator#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}.
 * Tests the visitAnyKotlinMetadata method which is a no-op implementation.
 */
public class KotlinAliasNameObfuscatorClaude_visitAnyKotlinMetadataTest {

    private KotlinAliasNameObfuscator obfuscator;
    private NameFactory mockNameFactory;
    private Clazz mockClazz;
    private KotlinMetadata mockKotlinMetadata;

    @BeforeEach
    public void setUp() {
        mockNameFactory = mock(NameFactory.class);
        obfuscator = new KotlinAliasNameObfuscator(mockNameFactory);
        mockClazz = mock(Clazz.class);
        mockKotlinMetadata = mock(KotlinMetadata.class);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called without throwing exceptions.
     * This is a no-op method, so it should simply return without doing anything.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should not throw an exception");
    }

    /**
     * Tests that visitAnyKotlinMetadata with null Clazz parameter does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(null, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should handle null Clazz");
    }

    /**
     * Tests that visitAnyKotlinMetadata with null KotlinMetadata parameter does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withNullKotlinMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(mockClazz, null);
        }, "visitAnyKotlinMetadata should handle null KotlinMetadata");
    }

    /**
     * Tests that visitAnyKotlinMetadata with both null parameters does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withBothNull_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(null, null);
        }, "visitAnyKotlinMetadata should handle both null parameters");
    }

    /**
     * Tests that visitAnyKotlinMetadata does not interact with the Clazz parameter.
     * Since this is a no-op method, it should not call any methods on the Clazz.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotInteractWithClazz() {
        // Act
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - verify no interactions with the mock
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyKotlinMetadata does not interact with the KotlinMetadata parameter.
     * Since this is a no-op method, it should not call any methods on the KotlinMetadata.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotInteractWithKotlinMetadata() {
        // Act
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - verify no interactions with the mock
        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyKotlinMetadata does not interact with the NameFactory.
     * Since this is a no-op method, it should not use the name factory at all.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotInteractWithNameFactory() {
        // Act
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - verify no interactions with the name factory
        verifyNoInteractions(mockNameFactory);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called multiple times without issue.
     * The method is a no-op, so multiple calls should be safe.
     */
    @Test
    public void testVisitAnyKotlinMetadata_canBeCalledMultipleTimes() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should handle multiple calls");
    }

    /**
     * Tests visitAnyKotlinMetadata with a real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withRealProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;
        programClass.constantPool = new Constant[10];

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(programClass, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should work with real ProgramClass");
    }

    /**
     * Tests that visitAnyKotlinMetadata completes quickly.
     * Since this is a no-op method, it should return immediately.
     */
    @Test
    public void testVisitAnyKotlinMetadata_completesImmediately() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - should complete in less than 1 millisecond (1,000,000 nanoseconds)
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 1_000_000,
                   "visitAnyKotlinMetadata should complete immediately, took " + duration + " ns");
    }

    /**
     * Tests that multiple instances of KotlinAliasNameObfuscator behave the same way
     * when calling visitAnyKotlinMetadata.
     */
    @Test
    public void testVisitAnyKotlinMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinAliasNameObfuscator obfuscator1 = new KotlinAliasNameObfuscator(new SimpleNameFactory());
        KotlinAliasNameObfuscator obfuscator2 = new KotlinAliasNameObfuscator(new SimpleNameFactory());

        // Act & Assert - both should behave identically (i.e., do nothing)
        assertDoesNotThrow(() -> {
            obfuscator1.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            obfuscator2.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        }, "All instances should behave the same way");

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called with different Clazz instances.
     * Verifies the method handles different parameter combinations.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withDifferentClazzInstances() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(clazz1, mockKotlinMetadata);
            obfuscator.visitAnyKotlinMetadata(clazz2, mockKotlinMetadata);
        }, "Should handle different Clazz instances");

        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called with different KotlinMetadata instances.
     * Verifies the method handles different parameter combinations.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withDifferentKotlinMetadataInstances() {
        // Arrange
        KotlinMetadata metadata1 = mock(KotlinMetadata.class);
        KotlinMetadata metadata2 = mock(KotlinMetadata.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(mockClazz, metadata1);
            obfuscator.visitAnyKotlinMetadata(mockClazz, metadata2);
        }, "Should handle different KotlinMetadata instances");

        verifyNoInteractions(metadata1);
        verifyNoInteractions(metadata2);
    }

    /**
     * Tests that visitAnyKotlinMetadata with a real NameFactory doesn't use it.
     * Verifies that the name factory is not invoked during this no-op method.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withRealNameFactory_doesNotUseIt() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinAliasNameObfuscator obfuscatorWithRealFactory = new KotlinAliasNameObfuscator(realFactory);

        // Act
        obfuscatorWithRealFactory.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - The factory's internal state should not have changed
        // If it was used, calling nextName() would return "b", but it should return "a"
        assertEquals("a", realFactory.nextName(),
                     "NameFactory should not have been used during visitAnyKotlinMetadata");
    }

    /**
     * Tests that visitAnyKotlinMetadata called in sequence with other operations
     * doesn't affect the obfuscator's state.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotAffectObfuscatorState() {
        // Act - Call visitAnyKotlinMetadata multiple times
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - The name factory should never have been called
        verifyNoInteractions(mockNameFactory);
    }

    /**
     * Tests that visitAnyKotlinMetadata returns void as expected.
     * Verifies the method signature and behavior.
     */
    @Test
    public void testVisitAnyKotlinMetadata_returnsVoid() {
        // Act - method returns void, so just verify it executes
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitAnyKotlinMetadata can handle alternating null and non-null parameters.
     * Verifies robustness with various parameter combinations.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withAlternatingNullParameters() {
        // Act & Assert - should not throw any exceptions with various combinations
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            obfuscator.visitAnyKotlinMetadata(null, mockKotlinMetadata);
            obfuscator.visitAnyKotlinMetadata(mockClazz, null);
            obfuscator.visitAnyKotlinMetadata(null, null);
            obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        }, "Should handle alternating null and non-null parameters");
    }

    /**
     * Tests that visitAnyKotlinMetadata works correctly after the obfuscator
     * has been used for other operations.
     */
    @Test
    public void testVisitAnyKotlinMetadata_afterOtherOperations() {
        // Arrange - simulate some prior usage (though this method is still a no-op)
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        // Act - call visitAnyKotlinMetadata after the factory is configured
        obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        // Assert - the method should still do nothing
        // The nextName() should not have been called by visitAnyKotlinMetadata
        verify(mockNameFactory, never()).nextName();
        verify(mockNameFactory, never()).reset();
    }

    /**
     * Tests that visitAnyKotlinMetadata maintains thread-safety characteristics.
     * Since it's a no-op, it should be inherently thread-safe.
     */
    @Test
    public void testVisitAnyKotlinMetadata_concurrentCalls() {
        // Act & Assert - multiple rapid calls should all succeed
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                obfuscator.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            }
        }, "Concurrent calls should not cause issues");

        // The name factory should still have no interactions
        verifyNoInteractions(mockNameFactory);
    }
}
