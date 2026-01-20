package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.Test;
import proguard.obfuscate.NameFactory;
import proguard.resources.file.visitor.ResourceFileVisitor;
import proguard.resources.kotlinmodule.KotlinModule;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinModuleNameObfuscator}.
 * Tests all public methods:
 * - Constructor: KotlinModuleNameObfuscator(NameFactory)
 * - visitKotlinModule(KotlinModule)
 */
public class KotlinModuleNameObfuscatorClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests that the constructor creates a valid instance with a NameFactory.
     * Verifies that the instance is created successfully.
     */
    @Test
    public void testConstructor_withValidNameFactory_createsInstance() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor calls reset() on the NameFactory.
     * Verifies that the NameFactory is initialized properly.
     */
    @Test
    public void testConstructor_callsResetOnNameFactory() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        verify(nameFactory, times(1)).reset();
    }

    /**
     * Tests that the constructor works with different NameFactory implementations.
     * Verifies that any NameFactory implementation can be used.
     */
    @Test
    public void testConstructor_withDifferentNameFactories() {
        // Arrange
        NameFactory factory1 = mock(NameFactory.class);
        NameFactory factory2 = mock(NameFactory.class);
        NameFactory factory3 = mock(NameFactory.class);

        // Act
        KotlinModuleNameObfuscator obfuscator1 = new KotlinModuleNameObfuscator(factory1);
        KotlinModuleNameObfuscator obfuscator2 = new KotlinModuleNameObfuscator(factory2);
        KotlinModuleNameObfuscator obfuscator3 = new KotlinModuleNameObfuscator(factory3);

        // Assert
        assertNotNull(obfuscator1);
        assertNotNull(obfuscator2);
        assertNotNull(obfuscator3);
        verify(factory1).reset();
        verify(factory2).reset();
        verify(factory3).reset();
    }

    /**
     * Tests that the constructor throws NullPointerException with null NameFactory.
     * Verifies that null is not accepted.
     */
    @Test
    public void testConstructor_withNullNameFactory_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new KotlinModuleNameObfuscator(null));
    }

    /**
     * Tests that the constructor creates instances that implement ResourceFileVisitor.
     * Verifies the correct interface implementation.
     */
    @Test
    public void testConstructor_implementsResourceFileVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        assertTrue(obfuscator instanceof ResourceFileVisitor,
                "KotlinModuleNameObfuscator should implement ResourceFileVisitor");
    }

    /**
     * Tests that multiple instances can be created with the same NameFactory.
     * Verifies that the same NameFactory can be reused.
     */
    @Test
    public void testConstructor_multipleInstancesWithSameNameFactory() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinModuleNameObfuscator obfuscator1 = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModuleNameObfuscator obfuscator2 = new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        assertNotNull(obfuscator1);
        assertNotNull(obfuscator2);
        assertNotSame(obfuscator1, obfuscator2);
        // reset() should be called twice (once per constructor)
        verify(nameFactory, times(2)).reset();
    }

    /**
     * Tests that the constructor doesn't call nextName() on the NameFactory.
     * Verifies that only reset() is called during construction.
     */
    @Test
    public void testConstructor_doesNotCallNextName() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        verify(nameFactory, never()).nextName();
    }

    /**
     * Tests that the constructor completes without throwing exceptions.
     * Verifies exception-safe construction.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act & Assert
        assertDoesNotThrow(() -> new KotlinModuleNameObfuscator(nameFactory));
    }

    /**
     * Tests that the constructor can be called multiple times in succession.
     * Verifies constructor stability.
     */
    @Test
    public void testConstructor_multipleSequentialCalls() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
            assertNotNull(obfuscator, "Constructor should create instance " + i);
        }

        verify(nameFactory, times(10)).reset();
    }

    /**
     * Tests that the constructor assigns to a ResourceFileVisitor variable.
     * Verifies polymorphic usage.
     */
    @Test
    public void testConstructor_assignableToResourceFileVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        ResourceFileVisitor visitor = new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        assertNotNull(visitor, "Should be assignable to ResourceFileVisitor");
        assertTrue(visitor instanceof KotlinModuleNameObfuscator);
    }

    /**
     * Tests the constructor creates distinct instances.
     * Verifies that each call creates a new object.
     */
    @Test
    public void testConstructor_createsDistinctInstances() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        KotlinModuleNameObfuscator obfuscator1 = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModuleNameObfuscator obfuscator2 = new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        assertNotSame(obfuscator1, obfuscator2,
                "Each constructor call should create a new instance");
    }

    /**
     * Tests that constructor calls reset() only once per invocation.
     * Verifies precise reset() call count.
     */
    @Test
    public void testConstructor_callsResetOnlyOnce() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);

        // Act
        new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        verify(nameFactory, times(1)).reset();
        verify(nameFactory, only()).reset();
    }

    // ========== visitKotlinModule Tests ==========

    /**
     * Tests that visitKotlinModule calls nextName() on the NameFactory.
     * Verifies basic interaction with NameFactory.
     */
    @Test
    public void testVisitKotlinModule_callsNextName() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("a");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitKotlinModule can be called multiple times.
     * Verifies that the method is reusable.
     */
    @Test
    public void testVisitKotlinModule_calledMultipleTimes() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("name1", "name2", "name3");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(kotlinModule);
        obfuscator.visitKotlinModule(kotlinModule);
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(3)).nextName();
    }

    /**
     * Tests that visitKotlinModule works with different KotlinModule instances.
     * Verifies that each module is processed independently.
     */
    @Test
    public void testVisitKotlinModule_withDifferentModules() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("alpha", "beta", "gamma");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        KotlinModule module1 = mock(KotlinModule.class);
        KotlinModule module2 = mock(KotlinModule.class);
        KotlinModule module3 = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(module1);
        obfuscator.visitKotlinModule(module2);
        obfuscator.visitKotlinModule(module3);

        // Assert
        verify(nameFactory, times(3)).nextName();
    }

    /**
     * Tests that visitKotlinModule throws NullPointerException with null module.
     * Verifies that null is not accepted.
     */
    @Test
    public void testVisitKotlinModule_withNullModule_throwsNullPointerException() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("name");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> obfuscator.visitKotlinModule(null));
    }

    /**
     * Tests that visitKotlinModule uses the NameFactory only once per call.
     * Verifies that nextName() is called exactly once per visit.
     */
    @Test
    public void testVisitKotlinModule_callsNextNameOnce() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("testName");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitKotlinModule does not call reset() on the NameFactory.
     * Verifies that reset() is only called in the constructor.
     */
    @Test
    public void testVisitKotlinModule_doesNotCallReset() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("name");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Reset the mock to clear the constructor's reset() call
        reset(nameFactory);
        when(nameFactory.nextName()).thenReturn("name");

        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, never()).reset();
        verify(nameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitKotlinModule completes without throwing exceptions.
     * Verifies exception-safe execution.
     */
    @Test
    public void testVisitKotlinModule_doesNotThrowException() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("name");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitKotlinModule(kotlinModule));
    }

    /**
     * Tests that multiple obfuscators can work independently.
     * Verifies that instances don't interfere with each other.
     */
    @Test
    public void testVisitKotlinModule_multipleObfuscatorsIndependent() {
        // Arrange
        NameFactory factory1 = mock(NameFactory.class);
        NameFactory factory2 = mock(NameFactory.class);
        when(factory1.nextName()).thenReturn("name1");
        when(factory2.nextName()).thenReturn("name2");

        KotlinModuleNameObfuscator obfuscator1 = new KotlinModuleNameObfuscator(factory1);
        KotlinModuleNameObfuscator obfuscator2 = new KotlinModuleNameObfuscator(factory2);

        KotlinModule module1 = mock(KotlinModule.class);
        KotlinModule module2 = mock(KotlinModule.class);

        // Act
        obfuscator1.visitKotlinModule(module1);
        obfuscator2.visitKotlinModule(module2);

        // Assert
        verify(factory1, times(1)).nextName();
        verify(factory2, times(1)).nextName();
    }

    /**
     * Tests that visitKotlinModule works correctly through the interface.
     * Verifies polymorphic behavior.
     */
    @Test
    public void testVisitKotlinModule_throughResourceFileVisitorInterface() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("obfuscated");
        ResourceFileVisitor visitor = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        visitor.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(1)).nextName();
    }

    /**
     * Tests visitKotlinModule with sequential name generation.
     * Verifies that name generation progresses correctly through multiple calls.
     */
    @Test
    public void testVisitKotlinModule_sequentialNameGeneration() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("a", "b", "c", "d", "e");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Act
        for (int i = 0; i < 5; i++) {
            KotlinModule module = mock(KotlinModule.class);
            obfuscator.visitKotlinModule(module);
        }

        // Assert
        verify(nameFactory, times(5)).nextName();
    }

    /**
     * Tests visitKotlinModule when NameFactory returns empty string.
     * Verifies handling of edge case.
     */
    @Test
    public void testVisitKotlinModule_withEmptyGeneratedName() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(1)).nextName();
        // The method should still complete successfully even with empty string
    }

    /**
     * Tests visitKotlinModule with special characters in generated name.
     * Verifies that any string from NameFactory is used.
     */
    @Test
    public void testVisitKotlinModule_withSpecialCharactersInGeneratedName() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("name_with$special@chars");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(1)).nextName();
    }

    /**
     * Tests visitKotlinModule with very long generated name.
     * Verifies handling of long strings.
     */
    @Test
    public void testVisitKotlinModule_withLongGeneratedName() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("veryLongObfuscatedModuleNameThatIsReallyQuiteLong");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(1)).nextName();
    }

    // ========== Integration Tests ==========

    /**
     * Tests the complete workflow: constructor then visitKotlinModule.
     * Verifies that both methods work together correctly.
     */
    @Test
    public void testCompleteWorkflow_constructorAndVisit() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("obfuscatedName");

        // Act
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        KotlinModule kotlinModule = mock(KotlinModule.class);
        obfuscator.visitKotlinModule(kotlinModule);

        // Assert
        verify(nameFactory, times(1)).reset();
        verify(nameFactory, times(1)).nextName();
    }

    /**
     * Tests processing multiple modules in sequence.
     * Verifies that the obfuscator can handle multiple modules.
     */
    @Test
    public void testCompleteWorkflow_multipleModules() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("a", "b", "c", "d", "e");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        KotlinModule[] modules = new KotlinModule[5];
        for (int i = 0; i < 5; i++) {
            modules[i] = mock(KotlinModule.class);
        }

        // Act
        for (KotlinModule module : modules) {
            obfuscator.visitKotlinModule(module);
        }

        // Assert
        verify(nameFactory, times(5)).nextName();
    }

    /**
     * Tests that the obfuscator maintains state across multiple visits.
     * Verifies that the NameFactory progresses through its sequence.
     */
    @Test
    public void testCompleteWorkflow_maintainsStateAcrossVisits() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName())
                .thenReturn("first")
                .thenReturn("second")
                .thenReturn("third");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Act
        obfuscator.visitKotlinModule(mock(KotlinModule.class));
        obfuscator.visitKotlinModule(mock(KotlinModule.class));
        obfuscator.visitKotlinModule(mock(KotlinModule.class));

        // Assert
        verify(nameFactory, times(3)).nextName();
    }

    /**
     * Tests the obfuscator with a realistic scenario of many modules.
     * Verifies behavior in a typical use case with many invocations.
     */
    @Test
    public void testRealisticScenario_obfuscateManyModules() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName())
                .thenReturn("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Act - Process 10 modules
        for (int i = 0; i < 10; i++) {
            obfuscator.visitKotlinModule(mock(KotlinModule.class));
        }

        // Assert
        verify(nameFactory, times(1)).reset();
        verify(nameFactory, times(10)).nextName();
    }

    /**
     * Tests that constructor and visitKotlinModule work correctly together.
     * Verifies the complete lifecycle of an obfuscator.
     */
    @Test
    public void testLifecycle_createAndUseObfuscator() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("name1", "name2");

        // Act - Create, use, and verify
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);
        assertNotNull(obfuscator);

        obfuscator.visitKotlinModule(mock(KotlinModule.class));
        obfuscator.visitKotlinModule(mock(KotlinModule.class));

        // Assert
        verify(nameFactory, times(1)).reset();
        verify(nameFactory, times(2)).nextName();
    }

    /**
     * Tests behavior when visitKotlinModule is called many times rapidly.
     * Verifies stability under repeated invocation.
     */
    @Test
    public void testStressTest_manyVisits() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("name");
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Act - Visit 100 times
        for (int i = 0; i < 100; i++) {
            obfuscator.visitKotlinModule(mock(KotlinModule.class));
        }

        // Assert
        verify(nameFactory, times(100)).nextName();
    }

    /**
     * Tests that the obfuscator correctly uses ResourceFileVisitor interface.
     * Verifies polymorphic usage through interface.
     */
    @Test
    public void testInterfaceCompliance_resourceFileVisitor() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        when(nameFactory.nextName()).thenReturn("obfuscated");

        // Act - Create as interface type
        ResourceFileVisitor visitor = new KotlinModuleNameObfuscator(nameFactory);
        visitor.visitKotlinModule(mock(KotlinModule.class));

        // Assert - Should work through interface
        verify(nameFactory).nextName();
        assertTrue(visitor instanceof KotlinModuleNameObfuscator);
    }

    /**
     * Tests that class name and package are correct.
     * Verifies class metadata.
     */
    @Test
    public void testMetadata_classNameAndPackage() {
        // Arrange
        NameFactory nameFactory = mock(NameFactory.class);
        KotlinModuleNameObfuscator obfuscator = new KotlinModuleNameObfuscator(nameFactory);

        // Assert
        assertEquals("KotlinModuleNameObfuscator", obfuscator.getClass().getSimpleName());
        assertEquals("proguard.obfuscate.kotlin", obfuscator.getClass().getPackage().getName());
    }
}
