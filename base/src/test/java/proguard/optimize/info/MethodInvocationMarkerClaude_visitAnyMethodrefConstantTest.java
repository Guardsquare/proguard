package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodInvocationMarker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
 *
 * The visitAnyMethodrefConstant method processes method reference constants (both regular method
 * references and interface method references) by calling referencedMethodAccept with the filtered
 * method marker. This allows the marker to track method invocations for all method references
 * found in the constant pool, which is the primary mechanism for counting method invocations.
 */
public class MethodInvocationMarkerClaude_visitAnyMethodrefConstantTest {

    private MethodInvocationMarker marker;
    private Clazz clazz;
    private AnyMethodrefConstant methodrefConstant;

    @BeforeEach
    public void setUp() {
        marker = new MethodInvocationMarker();
        clazz = mock(ProgramClass.class);
        methodrefConstant = mock(MethodrefConstant.class);
    }

    /**
     * Tests that visitAnyMethodrefConstant calls referencedMethodAccept on the method reference.
     * This is the core behavior - the method should delegate to the method reference constant
     * to visit the referenced method through the filtered method marker.
     */
    @Test
    public void testVisitAnyMethodrefConstant_callsReferencedMethodAccept() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify that referencedMethodAccept was called with a MemberVisitor
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant passes a MemberVisitor to referencedMethodAccept.
     * The visitor should be the filteredMethodMarker (an OptimizationInfoMemberFilter wrapping the marker).
     */
    @Test
    public void testVisitAnyMethodrefConstant_passesFilteredMethodMarkerAsVisitor() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify a MemberVisitor was passed (the filteredMethodMarker)
        verify(methodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyMethodrefConstant(clazz, methodrefConstant));
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyMethodrefConstant(null, methodrefConstant));

        // Verify the method still calls referencedMethodAccept
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant with null AnyMethodrefConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls methods on the constant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullMethodrefConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> marker.visitAnyMethodrefConstant(clazz, null));
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called multiple times in succession.
     * Each call should invoke referencedMethodAccept.
     */
    @Test
    public void testVisitAnyMethodrefConstant_calledMultipleTimes_invokesReferencedMethodAcceptEachTime() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify referencedMethodAccept was called three times
        verify(methodrefConstant, times(3)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMethodrefConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyMethodrefConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = marker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyMethodrefConstant(clazz, methodrefConstant));
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyMethodrefConstant(realClass, methodrefConstant));
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyMethodrefConstant(libraryClass, methodrefConstant));
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works with different AnyMethodrefConstant instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentMethodrefConstants_callsAcceptOnEach() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(MethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz, methodref1);
        marker.visitAnyMethodrefConstant(clazz, methodref2);
        marker.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert - verify each constant had referencedMethodAccept called
        verify(methodref1).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref3).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works with InterfaceMethodrefConstant.
     * AnyMethodrefConstant is a base class for both MethodrefConstant and InterfaceMethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withInterfaceMethodrefConstant_callsReferencedMethodAccept() {
        // Arrange
        AnyMethodrefConstant interfaceMethodref = mock(InterfaceMethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz, interfaceMethodref);

        // Assert - verify referencedMethodAccept was called
        verify(interfaceMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that multiple MethodInvocationMarker instances work independently.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        MethodInvocationMarker marker1 = new MethodInvocationMarker();
        MethodInvocationMarker marker2 = new MethodInvocationMarker();
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(MethodrefConstant.class);

        // Act
        marker1.visitAnyMethodrefConstant(clazz, methodref1);
        marker2.visitAnyMethodrefConstant(clazz, methodref2);

        // Assert - verify each marker processed its respective constant
        verify(methodref1).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant verifies marker implements MemberVisitor.
     */
    @Test
    public void testVisitAnyMethodrefConstant_markerImplementsMemberVisitor() {
        // Assert - verify marker is a MemberVisitor
        assertTrue(marker instanceof MemberVisitor,
                "MethodInvocationMarker should implement MemberVisitor");
    }

    /**
     * Tests that visitAnyMethodrefConstant handles the case where the constant has no referenced method.
     * The method should still attempt to call referencedMethodAccept.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNoReferencedMethod_stillCallsReferencedMethodAccept() {
        // Arrange
        AnyMethodrefConstant methodrefNoMethod = mock(MethodrefConstant.class);
        doNothing().when(methodrefNoMethod).referencedMethodAccept(any(MemberVisitor.class));

        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefNoMethod);

        // Assert - verify the method was called
        verify(methodrefNoMethod).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant can handle rapid successive calls.
     */
    @Test
    public void testVisitAnyMethodrefConstant_rapidSuccessiveCalls_allProcessed() {
        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitAnyMethodrefConstant(clazz, methodrefConstant);
        }

        // Assert
        verify(methodrefConstant, times(100)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works correctly with different clazz instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentClazzInstances_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz1, methodrefConstant);
        marker.visitAnyMethodrefConstant(clazz2, methodrefConstant);

        // Assert - verify both calls processed the method reference constant
        verify(methodrefConstant, times(2)).referencedMethodAccept(any(MemberVisitor.class));

        // Verify neither clazz was interacted with
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    /**
     * Tests that visitAnyMethodrefConstant only interacts with the AnyMethodrefConstant parameter.
     */
    @Test
    public void testVisitAnyMethodrefConstant_onlyInteractsWithMethodrefConstant() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify only methodrefConstant was interacted with
        verify(methodrefConstant).referencedMethodAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(methodrefConstant);
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMethodrefConstant can be invoked through the ConstantVisitor interface.
     */
    @Test
    public void testVisitAnyMethodrefConstant_throughConstantVisitorInterface_worksCorrectly() {
        // Arrange - use marker as ConstantVisitor
        proguard.classfile.constant.visitor.ConstantVisitor constantVisitor = marker;

        // Act
        constantVisitor.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert
        verify(methodrefConstant).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant with both null clazz and different method references works.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullClazzAndDifferentMethodrefs_worksCorrectly() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(InterfaceMethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(null, methodref1);
        marker.visitAnyMethodrefConstant(null, methodref2);

        // Assert
        verify(methodref1).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant preserves the marker's visitor implementation.
     * This ensures the marker can properly visit referenced methods through the filtered visitor.
     */
    @Test
    public void testVisitAnyMethodrefConstant_preservesVisitorImplementation() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify the filtered marker was passed as MemberVisitor
        verify(methodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works correctly when called on the same
     * method reference constant multiple times with different clazz instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_sameMethodrefDifferentClazz_allProcessed() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(LibraryClass.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz1, methodrefConstant);
        marker.visitAnyMethodrefConstant(clazz2, methodrefConstant);
        marker.visitAnyMethodrefConstant(clazz3, methodrefConstant);

        // Assert
        verify(methodrefConstant, times(3)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant correctly delegates to the method reference constant
     * without attempting to perform the method visitation itself.
     */
    @Test
    public void testVisitAnyMethodrefConstant_delegatesToMethodrefConstant_doesNotVisitDirectly() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - only the method reference constant should be interacted with
        verify(methodrefConstant).referencedMethodAccept(any(MemberVisitor.class));

        // The clazz parameter should not be touched
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMethodrefConstant uses the filteredMethodMarker, not the marker itself.
     * This is important because the filtered marker only processes methods with optimization info.
     */
    @Test
    public void testVisitAnyMethodrefConstant_usesFilteredMethodMarker_notMarkerDirectly() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify a MemberVisitor was passed (which is the filteredMethodMarker)
        // We can't directly verify it's the filteredMethodMarker, but we can verify it's a MemberVisitor
        verify(methodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant is stateless and can be called repeatedly
     * without affecting subsequent calls.
     */
    @Test
    public void testVisitAnyMethodrefConstant_isStateless_repeatedCallsIndependent() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(InterfaceMethodrefConstant.class);

        // Act - call multiple times
        marker.visitAnyMethodrefConstant(clazz, methodref1);
        marker.visitAnyMethodrefConstant(clazz, methodref2);
        marker.visitAnyMethodrefConstant(clazz, methodref1);

        // Assert - verify each call was independent
        verify(methodref1, times(2)).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant executes quickly as it's a simple delegation.
     */
    @Test
    public void testVisitAnyMethodrefConstant_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnyMethodrefConstant(clazz, methodrefConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyMethodrefConstant should execute quickly");
    }

    /**
     * Tests that visitAnyMethodrefConstant is thread-safe when called concurrently.
     * Since the method uses an instance field (filteredMethodMarker), it should still be thread-safe.
     */
    @Test
    public void testVisitAnyMethodrefConstant_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitAnyMethodrefConstant
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitAnyMethodrefConstant(clazz, methodrefConstant);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify referencedMethodAccept was called the expected number of times
        verify(methodrefConstant, times(1000)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant processes both MethodrefConstant and InterfaceMethodrefConstant.
     * This verifies the method handles all subclasses of AnyMethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withBothMethodrefTypes_processesAll() {
        // Arrange
        AnyMethodrefConstant regularMethodref = mock(MethodrefConstant.class);
        AnyMethodrefConstant interfaceMethodref = mock(InterfaceMethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz, regularMethodref);
        marker.visitAnyMethodrefConstant(clazz, interfaceMethodref);

        // Assert - verify both types were processed
        verify(regularMethodref).referencedMethodAccept(any(MemberVisitor.class));
        verify(interfaceMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant processes method references for invocation counting.
     * This verifies the method's purpose in the context of MethodInvocationMarker.
     */
    @Test
    public void testVisitAnyMethodrefConstant_processesMethodReferencesForInvocationCounting() {
        // Arrange
        AnyMethodrefConstant methodrefWithMethod = mock(MethodrefConstant.class);

        // Act - this should trigger processing of the referenced method
        marker.visitAnyMethodrefConstant(clazz, methodrefWithMethod);

        // Assert - verify the method reference was asked to accept a member visitor
        verify(methodrefWithMethod).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant can handle method references from different sources.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMethodrefsFromDifferentSources_allProcessed() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class, "methodref1");
        AnyMethodrefConstant methodref2 = mock(InterfaceMethodrefConstant.class, "methodref2");
        AnyMethodrefConstant methodref3 = mock(MethodrefConstant.class, "methodref3");

        // Act
        marker.visitAnyMethodrefConstant(clazz, methodref1);
        marker.visitAnyMethodrefConstant(clazz, methodref2);
        marker.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert - verify all were processed
        verify(methodref1).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref3).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant maintains correct behavior across different marker instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_acrossDifferentMarkerInstances_consistentBehavior() {
        // Arrange
        MethodInvocationMarker marker1 = new MethodInvocationMarker();
        MethodInvocationMarker marker2 = new MethodInvocationMarker();
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act
        marker1.visitAnyMethodrefConstant(clazz, methodref);
        marker2.visitAnyMethodrefConstant(clazz, methodref);

        // Assert - both should have called referencedMethodAccept
        verify(methodref, times(2)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant does not modify the AnyMethodrefConstant.
     * The method should only read/visit, not modify.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotModifyMethodrefConstant() {
        // Act
        marker.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify only the read operation (referencedMethodAccept) was called
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(methodrefConstant);
    }

    /**
     * Tests that visitAnyMethodrefConstant handles the case where the method reference's
     * referencedMethodAccept is called but does nothing (no referenced method).
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNoOpReferencedMethodAccept_completesSuccessfully() {
        // Arrange - setup the mock to do nothing when referencedMethodAccept is called
        AnyMethodrefConstant noOpMethodref = mock(MethodrefConstant.class);
        doNothing().when(noOpMethodref).referencedMethodAccept(any(MemberVisitor.class));

        // Act & Assert - should complete without error
        assertDoesNotThrow(() -> marker.visitAnyMethodrefConstant(clazz, noOpMethodref));
        verify(noOpMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant handles virtual method invocations.
     * This represents INVOKEVIRTUAL instructions.
     */
    @Test
    public void testVisitAnyMethodrefConstant_forVirtualMethodInvocation_processesCorrectly() {
        // Arrange - method reference for a virtual method call
        AnyMethodrefConstant virtualMethodref = mock(MethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz, virtualMethodref);

        // Assert
        verify(virtualMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant handles static method invocations.
     * This represents INVOKESTATIC instructions.
     */
    @Test
    public void testVisitAnyMethodrefConstant_forStaticMethodInvocation_processesCorrectly() {
        // Arrange - method reference for a static method call
        AnyMethodrefConstant staticMethodref = mock(MethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz, staticMethodref);

        // Assert
        verify(staticMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant handles special method invocations.
     * This represents INVOKESPECIAL instructions (constructors, private methods, super calls).
     */
    @Test
    public void testVisitAnyMethodrefConstant_forSpecialMethodInvocation_processesCorrectly() {
        // Arrange - method reference for a special method call
        AnyMethodrefConstant specialMethodref = mock(MethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz, specialMethodref);

        // Assert
        verify(specialMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant handles interface method invocations.
     * This represents INVOKEINTERFACE instructions.
     */
    @Test
    public void testVisitAnyMethodrefConstant_forInterfaceMethodInvocation_processesCorrectly() {
        // Arrange - interface method reference for an interface method call
        AnyMethodrefConstant interfaceMethodref = mock(InterfaceMethodrefConstant.class);

        // Act
        marker.visitAnyMethodrefConstant(clazz, interfaceMethodref);

        // Assert
        verify(interfaceMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }
}
