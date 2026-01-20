package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramClassOptimizationInfoSetter}.
 *
 * This ClassVisitor attaches a ProgramClassOptimizationInfo instance to every
 * ProgramClass that it visits. The tests cover:
 * - Both constructors (default and with overwrite parameter)
 * - visitAnyClass (no-op implementation)
 * - visitProgramClass with various scenarios (null processing info, existing info, overwrite flag)
 */
public class ProgramClassOptimizationInfoSetterClaudeTest {

    private ProgramClassOptimizationInfoSetter setter;
    private ProgramClass mockProgramClass;
    private Clazz mockClazz;

    @BeforeEach
    public void setUp() {
        mockProgramClass = mock(ProgramClass.class);
        mockClazz = mock(Clazz.class);

        when(mockProgramClass.getName()).thenReturn("com/example/TestClass");
        when(mockClazz.getName()).thenReturn("com/example/AnotherClass");
    }

    // =========================================================================
    // Constructor Tests - <init>.()V
    // =========================================================================

    /**
     * Tests that the no-arg constructor successfully creates an instance.
     */
    @Test
    public void testDefaultConstructor_createsInstance() {
        // Act
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter();

        // Assert
        assertNotNull(newSetter, "ProgramClassOptimizationInfoSetter instance should be created");
    }

    /**
     * Tests that the default constructor sets overwrite to false.
     * This is tested by observing behavior: existing processing info should not be overwritten.
     */
    @Test
    public void testDefaultConstructor_doesNotOverwriteExistingInfo() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter();

        // Create existing processing info
        Object existingInfo = new Object();
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert - setProcessingInfo should NOT be called because existing info exists
        verify(mockProgramClass, never()).setProcessingInfo(any());
    }

    /**
     * Tests that the default constructor sets optimization info when none exists.
     */
    @Test
    public void testDefaultConstructor_setsInfoWhenNull() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter();
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert - setProcessingInfo should be called with new optimization info
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    // =========================================================================
    // Constructor Tests - <init>.(Z)V
    // =========================================================================

    /**
     * Tests that the boolean constructor with false creates an instance.
     */
    @Test
    public void testBooleanConstructor_withFalse_createsInstance() {
        // Act
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter(false);

        // Assert
        assertNotNull(newSetter, "ProgramClassOptimizationInfoSetter instance should be created");
    }

    /**
     * Tests that the boolean constructor with true creates an instance.
     */
    @Test
    public void testBooleanConstructor_withTrue_createsInstance() {
        // Act
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter(true);

        // Assert
        assertNotNull(newSetter, "ProgramClassOptimizationInfoSetter instance should be created");
    }

    /**
     * Tests that constructor with false behaves the same as the default constructor
     * (does not overwrite existing info).
     */
    @Test
    public void testBooleanConstructor_withFalse_doesNotOverwrite() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter(false);

        Object existingInfo = new Object();
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass, never()).setProcessingInfo(any());
    }

    /**
     * Tests that constructor with true enables overwriting existing info.
     */
    @Test
    public void testBooleanConstructor_withTrue_overwritesExistingInfo() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter(true);

        Object existingInfo = new Object();
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert - setProcessingInfo SHOULD be called because overwrite is true
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that constructor with false sets info when processing info is null.
     */
    @Test
    public void testBooleanConstructor_withFalse_setsInfoWhenNull() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter(false);
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that constructor with true sets info when processing info is null.
     */
    @Test
    public void testBooleanConstructor_withTrue_setsInfoWhenNull() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter(true);
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    // =========================================================================
    // visitAnyClass Tests
    // =========================================================================

    /**
     * Tests that visitAnyClass does nothing (no-op implementation).
     * This method should not throw any exceptions and should not modify the class.
     */
    @Test
    public void testVisitAnyClass_doesNothing() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();

        // Act - should not throw any exceptions
        assertDoesNotThrow(() -> setter.visitAnyClass(mockClazz));

        // Assert - no interactions with the class
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyClass with null does not throw exceptions.
     */
    @Test
    public void testVisitAnyClass_withNull_doesNothing() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> setter.visitAnyClass(null));
    }

    /**
     * Tests that visitAnyClass can be called multiple times.
     */
    @Test
    public void testVisitAnyClass_canBeCalledMultipleTimes() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();

        // Act - should not throw any exceptions
        assertDoesNotThrow(() -> {
            setter.visitAnyClass(mockClazz);
            setter.visitAnyClass(mockClazz);
            setter.visitAnyClass(mockClazz);
        });

        // Assert - no interactions with the class
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyClass with overwrite=true still does nothing.
     */
    @Test
    public void testVisitAnyClass_withOverwrite_doesNothing() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(true);

        // Act - should not throw any exceptions
        assertDoesNotThrow(() -> setter.visitAnyClass(mockClazz));

        // Assert - no interactions with the class
        verifyNoInteractions(mockClazz);
    }

    // =========================================================================
    // visitProgramClass Tests - null processing info
    // =========================================================================

    /**
     * Tests that visitProgramClass sets optimization info when processing info is null.
     * This is the main use case: attaching optimization info to a fresh class.
     */
    @Test
    public void testVisitProgramClass_withNullProcessingInfo_setsOptimizationInfo() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).getProcessingInfo();
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramClass with overwrite=false sets info when processing info is null.
     */
    @Test
    public void testVisitProgramClass_overwriteFalse_withNullInfo_setsInfo() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(false);
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramClass with overwrite=true sets info when processing info is null.
     */
    @Test
    public void testVisitProgramClass_overwriteTrue_withNullInfo_setsInfo() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(true);
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    // =========================================================================
    // visitProgramClass Tests - existing processing info with overwrite=false
    // =========================================================================

    /**
     * Tests that visitProgramClass does NOT overwrite existing processing info
     * when overwrite is false (default behavior).
     */
    @Test
    public void testVisitProgramClass_withExistingInfo_doesNotOverwrite() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();
        Object existingInfo = new Object();
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert - getProcessingInfo is called, but setProcessingInfo is NOT
        verify(mockProgramClass).getProcessingInfo();
        verify(mockProgramClass, never()).setProcessingInfo(any());
    }

    /**
     * Tests that visitProgramClass with explicit overwrite=false does not overwrite
     * existing optimization info.
     */
    @Test
    public void testVisitProgramClass_overwriteFalse_withExistingOptimizationInfo_doesNotOverwrite() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(false);
        ProgramClassOptimizationInfo existingInfo = new ProgramClassOptimizationInfo();
        existingInfo.setInstantiated();  // Set some state
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).getProcessingInfo();
        verify(mockProgramClass, never()).setProcessingInfo(any());
    }

    /**
     * Tests that visitProgramClass does not overwrite when processing info
     * is of a different type (not ProgramClassOptimizationInfo).
     */
    @Test
    public void testVisitProgramClass_overwriteFalse_withDifferentTypeInfo_doesNotOverwrite() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(false);
        String existingInfo = "Some other processing info";
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).getProcessingInfo();
        verify(mockProgramClass, never()).setProcessingInfo(any());
    }

    // =========================================================================
    // visitProgramClass Tests - existing processing info with overwrite=true
    // =========================================================================

    /**
     * Tests that visitProgramClass DOES overwrite existing processing info
     * when overwrite is true.
     */
    @Test
    public void testVisitProgramClass_overwriteTrue_withExistingInfo_overwrites() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(true);
        Object existingInfo = new Object();
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert - both getProcessingInfo and setProcessingInfo are called
        verify(mockProgramClass).getProcessingInfo();
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramClass with overwrite=true overwrites existing
     * optimization info with a fresh instance.
     */
    @Test
    public void testVisitProgramClass_overwriteTrue_withExistingOptimizationInfo_overwrites() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(true);
        ProgramClassOptimizationInfo existingInfo = new ProgramClassOptimizationInfo();
        existingInfo.setInstantiated();
        existingInfo.setEscaping();
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert - a new ProgramClassOptimizationInfo is set
        verify(mockProgramClass).getProcessingInfo();
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramClass with overwrite=true overwrites
     * even when the existing info is of a different type.
     */
    @Test
    public void testVisitProgramClass_overwriteTrue_withDifferentTypeInfo_overwrites() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(true);
        String existingInfo = "Some other processing info";
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        setter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).getProcessingInfo();
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    // =========================================================================
    // visitProgramClass Tests - multiple invocations
    // =========================================================================

    /**
     * Tests that visitProgramClass can be called multiple times on the same class
     * with overwrite=false (only first call sets info).
     */
    @Test
    public void testVisitProgramClass_overwriteFalse_multipleInvocations_setsOnlyOnce() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(false);
        when(mockProgramClass.getProcessingInfo())
            .thenReturn(null)                              // First call: null
            .thenReturn(new ProgramClassOptimizationInfo()); // Subsequent calls: has info

        // Act
        setter.visitProgramClass(mockProgramClass);
        setter.visitProgramClass(mockProgramClass);
        setter.visitProgramClass(mockProgramClass);

        // Assert - setProcessingInfo is called only once (when info was null)
        verify(mockProgramClass, times(1)).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same class
     * with overwrite=true (sets info every time).
     */
    @Test
    public void testVisitProgramClass_overwriteTrue_multipleInvocations_setsEveryTime() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter(true);
        ProgramClassOptimizationInfo info = new ProgramClassOptimizationInfo();
        when(mockProgramClass.getProcessingInfo()).thenReturn(info);

        // Act
        setter.visitProgramClass(mockProgramClass);
        setter.visitProgramClass(mockProgramClass);
        setter.visitProgramClass(mockProgramClass);

        // Assert - setProcessingInfo is called every time
        verify(mockProgramClass, times(3)).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramClass can be used on multiple different classes.
     */
    @Test
    public void testVisitProgramClass_multipleDifferentClasses_setsEach() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();

        ProgramClass mockClass1 = mock(ProgramClass.class);
        ProgramClass mockClass2 = mock(ProgramClass.class);
        ProgramClass mockClass3 = mock(ProgramClass.class);

        when(mockClass1.getProcessingInfo()).thenReturn(null);
        when(mockClass2.getProcessingInfo()).thenReturn(null);
        when(mockClass3.getProcessingInfo()).thenReturn(null);

        // Act
        setter.visitProgramClass(mockClass1);
        setter.visitProgramClass(mockClass2);
        setter.visitProgramClass(mockClass3);

        // Assert - each class gets its info set
        verify(mockClass1).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
        verify(mockClass2).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
        verify(mockClass3).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    // =========================================================================
    // Integration Tests - Complete Workflows
    // =========================================================================

    /**
     * Tests the complete workflow: create setter, visit class, verify info is attached.
     */
    @Test
    public void testIntegration_completeWorkflow_defaultConstructor() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter();
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests the complete workflow with overwrite=true.
     */
    @Test
    public void testIntegration_completeWorkflow_withOverwrite() {
        // Arrange
        ProgramClassOptimizationInfoSetter newSetter = new ProgramClassOptimizationInfoSetter(true);
        ProgramClassOptimizationInfo existingInfo = new ProgramClassOptimizationInfo();
        when(mockProgramClass.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        newSetter.visitProgramClass(mockProgramClass);

        // Assert - old info is replaced
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }

    /**
     * Tests that the setter correctly implements ClassVisitor interface.
     * Both visitAnyClass and visitProgramClass should be callable through the interface.
     */
    @Test
    public void testIntegration_classVisitorInterface() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();
        when(mockProgramClass.getProcessingInfo()).thenReturn(null);

        // Act - call through ClassVisitor interface
        proguard.classfile.visitor.ClassVisitor visitor = setter;
        assertDoesNotThrow(() -> {
            visitor.visitAnyClass(mockClazz);
            visitor.visitProgramClass(mockProgramClass);
        });

        // Assert
        verify(mockProgramClass).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests behavior when visitProgramClass is called with null.
     * This should throw a NullPointerException when trying to call getProcessingInfo().
     */
    @Test
    public void testVisitProgramClass_withNull_throwsNullPointerException() {
        // Arrange
        setter = new ProgramClassOptimizationInfoSetter();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            setter.visitProgramClass(null);
        });
    }

    /**
     * Tests that different setter instances are independent.
     */
    @Test
    public void testMultipleSetters_areIndependent() {
        // Arrange
        ProgramClassOptimizationInfoSetter setter1 = new ProgramClassOptimizationInfoSetter(false);
        ProgramClassOptimizationInfoSetter setter2 = new ProgramClassOptimizationInfoSetter(true);

        ProgramClass mockClass1 = mock(ProgramClass.class);
        ProgramClass mockClass2 = mock(ProgramClass.class);

        Object existingInfo = new Object();
        when(mockClass1.getProcessingInfo()).thenReturn(existingInfo);
        when(mockClass2.getProcessingInfo()).thenReturn(existingInfo);

        // Act
        setter1.visitProgramClass(mockClass1);
        setter2.visitProgramClass(mockClass2);

        // Assert - setter1 (overwrite=false) should not set, setter2 (overwrite=true) should set
        verify(mockClass1, never()).setProcessingInfo(any());
        verify(mockClass2).setProcessingInfo(any(ProgramClassOptimizationInfo.class));
    }
}
