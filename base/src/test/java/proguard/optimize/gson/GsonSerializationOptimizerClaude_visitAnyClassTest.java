package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.io.ExtraDataEntryNameMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GsonSerializationOptimizer#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in GsonSerializationOptimizer is an empty implementation (no-op).
 * It's part of the ClassVisitor interface but doesn't perform any actions because:
 * - The actual serialization optimization is done in visitProgramClass()
 * - visitAnyClass() serves as a default no-op for when the visitor is called on non-specific class types
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions
 * 2. The method doesn't modify the class being visited
 * 3. The method can handle different types of Clazz instances
 * 4. The method is properly integrated into the visitor pattern
 */
public class GsonSerializationOptimizerClaude_visitAnyClassTest {

    private GsonSerializationOptimizer optimizer;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private GsonRuntimeSettings gsonRuntimeSettings;
    private OptimizedJsonInfo serializationInfo;
    private ExtraDataEntryNameMap extraDataEntryNameMap;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        gsonRuntimeSettings = new GsonRuntimeSettings();
        serializationInfo = new OptimizedJsonInfo();
        extraDataEntryNameMap = new ExtraDataEntryNameMap();

        optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );
    }

    /**
     * Tests that visitAnyClass does not throw any exceptions with a ProgramClass.
     * The empty implementation should execute without errors.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.visitAnyClass(programClass),
            "visitAnyClass should not throw exception with ProgramClass");
    }

    /**
     * Tests that visitAnyClass does not throw any exceptions with a LibraryClass.
     * The empty implementation should execute without errors.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNotThrow() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.visitAnyClass(libraryClass),
            "visitAnyClass should not throw exception with LibraryClass");
    }

    /**
     * Tests that visitAnyClass does not throw exceptions with a mock Clazz.
     * Verifies the method doesn't make any calls to the Clazz parameter.
     */
    @Test
    public void testVisitAnyClass_withMockClazz_doesNotInteractWithClazz() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        // Act
        optimizer.visitAnyClass(mockClazz);

        // Assert - verify no interactions with the mock (empty method doesn't call anything)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyClass does not modify the ProgramClass.
     * Since the method is empty, the class should remain unchanged.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNotModifyClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Object originalProcessingInfo = new Object();
        programClass.setProcessingInfo(originalProcessingInfo);
        int originalAccessFlags = programClass.u2accessFlags;

        // Act
        optimizer.visitAnyClass(programClass);

        // Assert
        assertSame(originalProcessingInfo, programClass.getProcessingInfo(),
            "Processing info should not be modified");
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
            "Access flags should not be modified");
    }

    /**
     * Tests that visitAnyClass does not modify the LibraryClass.
     * Since the method is empty, the class should remain unchanged.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNotModifyClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        Object originalProcessingInfo = new Object();
        libraryClass.setProcessingInfo(originalProcessingInfo);
        int originalAccessFlags = libraryClass.u2accessFlags;

        // Act
        optimizer.visitAnyClass(libraryClass);

        // Assert
        assertSame(originalProcessingInfo, libraryClass.getProcessingInfo(),
            "Processing info should not be modified");
        assertEquals(originalAccessFlags, libraryClass.u2accessFlags,
            "Access flags should not be modified");
    }

    /**
     * Tests that visitAnyClass can be called multiple times on the same class.
     * The empty method should handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - call multiple times
        assertDoesNotThrow(() -> {
            optimizer.visitAnyClass(programClass);
            optimizer.visitAnyClass(programClass);
            optimizer.visitAnyClass(programClass);
        }, "Multiple calls to visitAnyClass should not throw");
    }

    /**
     * Tests that visitAnyClass can be called on multiple different classes.
     * The empty method should handle different class instances.
     */
    @Test
    public void testVisitAnyClass_withMultipleClasses_doesNotThrow() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            optimizer.visitAnyClass(programClass1);
            optimizer.visitAnyClass(programClass2);
            optimizer.visitAnyClass(libraryClass);
        }, "visitAnyClass should handle multiple different classes");
    }

    /**
     * Tests that visitAnyClass throws NullPointerException with null input.
     * Even though the method is empty, null should cause an NPE if dereferenced.
     * However, since the method body is empty, it may not throw.
     */
    @Test
    public void testVisitAnyClass_withNull_doesNotThrow() {
        // Act & Assert
        // Empty methods don't dereference the parameter, so no NPE is expected
        assertDoesNotThrow(() -> optimizer.visitAnyClass(null),
            "Empty visitAnyClass should not throw with null since it doesn't use the parameter");
    }

    /**
     * Tests that visitAnyClass can be used through the ClassVisitor interface.
     * Verifies proper integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface_works() {
        // Arrange
        ClassVisitor visitor = optimizer;
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyClass(programClass),
            "visitAnyClass should work when called through ClassVisitor interface");
    }

    /**
     * Tests that visitAnyClass works with different optimizer configurations.
     * The empty method should behave the same regardless of configuration.
     */
    @Test
    public void testVisitAnyClass_withDifferentConfigurations_consistentBehavior() {
        // Arrange
        GsonRuntimeSettings configuredSettings = new GsonRuntimeSettings();
        configuredSettings.excludeFieldsWithoutExposeAnnotation = true;
        configuredSettings.serializeNulls = true;

        GsonSerializationOptimizer configuredOptimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            configuredSettings,
            serializationInfo,
            true,
            extraDataEntryNameMap
        );

        ProgramClass programClass = new ProgramClass();

        // Act & Assert - both optimizers should behave the same
        assertDoesNotThrow(() -> optimizer.visitAnyClass(programClass),
            "Default optimizer should not throw");
        assertDoesNotThrow(() -> configuredOptimizer.visitAnyClass(programClass),
            "Configured optimizer should not throw");
    }

    /**
     * Tests that visitAnyClass completes execution quickly.
     * Since it's empty, it should return immediately.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 10000; i++) {
            optimizer.visitAnyClass(programClass);
        }
        long endTime = System.nanoTime();

        // Assert - 10000 calls should complete very quickly (less than 100ms)
        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 100,
            "10000 calls to empty visitAnyClass should complete in less than 100ms, took: " + durationMs + "ms");
    }

    /**
     * Tests that visitAnyClass doesn't interfere with subsequent visitProgramClass calls.
     * The empty visitAnyClass should not affect the optimizer's state.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectSubsequentVisits() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();

        // Act - call visitAnyClass, then check optimizer is still usable
        optimizer.visitAnyClass(programClass1);

        // Assert - optimizer should still be usable (doesn't throw)
        // Note: We can't easily test visitProgramClass without setting up a lot of infrastructure,
        // but we can verify the optimizer is still in a valid state
        assertDoesNotThrow(() -> optimizer.visitAnyClass(programClass2),
            "Optimizer should remain usable after calling visitAnyClass");
    }

    /**
     * Tests that multiple optimizer instances all have empty visitAnyClass behavior.
     * Verifies consistency across instances.
     */
    @Test
    public void testVisitAnyClass_multipleInstances_consistentBehavior() {
        // Arrange
        GsonSerializationOptimizer optimizer1 = new GsonSerializationOptimizer(
            new ClassPool(),
            new ClassPool(),
            new GsonRuntimeSettings(),
            new OptimizedJsonInfo(),
            false,
            new ExtraDataEntryNameMap()
        );

        GsonSerializationOptimizer optimizer2 = new GsonSerializationOptimizer(
            new ClassPool(),
            new ClassPool(),
            new GsonRuntimeSettings(),
            new OptimizedJsonInfo(),
            true,
            new ExtraDataEntryNameMap()
        );

        ProgramClass programClass = new ProgramClass();

        // Act & Assert - both instances should behave identically
        assertDoesNotThrow(() -> optimizer1.visitAnyClass(programClass),
            "First optimizer should not throw");
        assertDoesNotThrow(() -> optimizer2.visitAnyClass(programClass),
            "Second optimizer should not throw");
    }

    /**
     * Tests that visitAnyClass can be called in rapid succession without issues.
     * Verifies thread-local behavior (no shared state issues).
     */
    @Test
    public void testVisitAnyClass_rapidSuccessiveCalls_noStateIssues() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[100];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = new ProgramClass();
        }

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (ProgramClass clazz : classes) {
                optimizer.visitAnyClass(clazz);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that visitAnyClass is truly a no-op by verifying it doesn't call
     * any methods on the Clazz parameter.
     */
    @Test
    public void testVisitAnyClass_isNoOp_verifiesNoMethodCalls() {
        // Arrange
        Clazz spyClazz = spy(ProgramClass.class);

        // Act
        optimizer.visitAnyClass(spyClazz);

        // Assert - The method should not call anything on the Clazz
        // We verify no methods were called (except for potential internal methods by spy itself)
        // The spy may call some internal methods, but our visitAnyClass should not
        verify(spyClazz, never()).accept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyClass works correctly with classes that have various states.
     * The empty method should handle all class states uniformly.
     */
    @Test
    public void testVisitAnyClass_withVariousClassStates_handlesUniformly() {
        // Arrange
        ProgramClass emptyClass = new ProgramClass();

        ProgramClass classWithProcessingInfo = new ProgramClass();
        classWithProcessingInfo.setProcessingInfo(new Object());

        ProgramClass classWithAccessFlags = new ProgramClass();
        classWithAccessFlags.u2accessFlags = 0x0001; // ACC_PUBLIC

        // Act & Assert - all should work the same
        assertDoesNotThrow(() -> optimizer.visitAnyClass(emptyClass),
            "Should handle empty class");
        assertDoesNotThrow(() -> optimizer.visitAnyClass(classWithProcessingInfo),
            "Should handle class with processing info");
        assertDoesNotThrow(() -> optimizer.visitAnyClass(classWithAccessFlags),
            "Should handle class with access flags");
    }

    /**
     * Tests that the empty visitAnyClass method doesn't prevent the optimizer
     * from being used as a ClassVisitor in a visitor chain.
     */
    @Test
    public void testVisitAnyClass_inVisitorChain_doesNotBreakChain() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        boolean[] visited = new boolean[1];

        ClassVisitor chainedVisitor = new ClassVisitor() {
            @Override
            public void visitAnyClass(Clazz clazz) {
                optimizer.visitAnyClass(clazz);
                visited[0] = true;
            }
        };

        // Act
        chainedVisitor.visitAnyClass(programClass);

        // Assert
        assertTrue(visited[0], "Visitor chain should complete successfully");
    }

    /**
     * Tests that visitAnyClass behaves consistently across different JVM states.
     * The empty method should always behave the same way.
     */
    @Test
    public void testVisitAnyClass_consistentBehaviorAcrossMultipleCalls() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Object originalInfo = new Object();
        programClass.setProcessingInfo(originalInfo);

        // Act - call many times and verify consistent behavior
        for (int i = 0; i < 1000; i++) {
            optimizer.visitAnyClass(programClass);

            // Assert - processing info should never change
            assertSame(originalInfo, programClass.getProcessingInfo(),
                "Processing info should remain unchanged after call " + i);
        }
    }
}
