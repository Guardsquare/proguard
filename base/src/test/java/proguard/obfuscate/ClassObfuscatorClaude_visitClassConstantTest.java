package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.constant.ClassConstant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassObfuscator#visitClassConstant(Clazz, ClassConstant)}.
 *
 * This class tests the visitClassConstant method which is part of the ConstantVisitor interface.
 * The method ensures that the outer class referenced by the ClassConstant has an obfuscated name
 * by delegating to referencedClassAccept, which will invoke the ClassObfuscator as a ClassVisitor
 * on the referenced class.
 *
 * The method's purpose is to ensure proper processing of class constants that reference outer
 * classes during obfuscation.
 */
public class ClassObfuscatorClaude_visitClassConstantTest {

    private ClassObfuscator classObfuscator;
    private Clazz mockClazz;
    private ClassConstant mockClassConstant;

    @BeforeEach
    public void setUp() {
        // Create a ClassObfuscator with minimal configuration
        classObfuscator = new ClassObfuscator(
            new proguard.classfile.ClassPool(),  // programClassPool
            new proguard.classfile.ClassPool(),  // libraryClassPool
            null,                                // classNameFactory
            null,                                // packageNameFactory
            true,                                // useMixedCaseClassNames
            null,                                // keepPackageNames
            null,                                // flattenPackageHierarchy
            null,                                // repackageClasses
            false,                               // allowAccessModification
            false                                // adaptKotlin
        );

        mockClazz = mock(Clazz.class);
        mockClassConstant = mock(ClassConstant.class);
    }

    // ========== Tests for visitClassConstant - Basic Functionality ==========

    /**
     * Tests that visitClassConstant calls referencedClassAccept on the ClassConstant.
     * This is the primary behavior of the method - it delegates to the ClassConstant
     * to visit the referenced class.
     */
    @Test
    public void testVisitClassConstant_callsReferencedClassAccept() {
        // Act
        classObfuscator.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitClassConstant_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> classObfuscator.visitClassConstant(mockClazz, mockClassConstant));
    }

    /**
     * Tests that visitClassConstant can be called multiple times on the same ClassConstant.
     * Each call should delegate to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes() {
        // Act
        classObfuscator.visitClassConstant(mockClazz, mockClassConstant);
        classObfuscator.visitClassConstant(mockClazz, mockClassConstant);
        classObfuscator.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(3)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant works with different ClassConstant instances.
     */
    @Test
    public void testVisitClassConstant_withDifferentClassConstants() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        // Act
        classObfuscator.visitClassConstant(mockClazz, constant1);
        classObfuscator.visitClassConstant(mockClazz, constant2);
        classObfuscator.visitClassConstant(mockClazz, constant3);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(classObfuscator));
        verify(constant2, times(1)).referencedClassAccept(eq(classObfuscator));
        verify(constant3, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant works with different Clazz instances.
     * The Clazz parameter represents the class containing the constant pool with this constant.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        classObfuscator.visitClassConstant(clazz1, mockClassConstant);
        classObfuscator.visitClassConstant(clazz2, mockClassConstant);
        classObfuscator.visitClassConstant(clazz3, mockClassConstant);

        // Assert
        // The method should call referencedClassAccept regardless of the Clazz parameter
        verify(mockClassConstant, times(3)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant passes the correct ClassObfuscator instance
     * (itself) as the visitor to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_passesCorrectVisitor() {
        // Arrange
        ClassObfuscator anotherObfuscator = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );

        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act
        classObfuscator.visitClassConstant(mockClazz, constant1);
        anotherObfuscator.visitClassConstant(mockClazz, constant2);

        // Assert
        // Each obfuscator should pass itself as the visitor
        verify(constant1, times(1)).referencedClassAccept(eq(classObfuscator));
        verify(constant2, times(1)).referencedClassAccept(eq(anotherObfuscator));
    }

    // ========== Tests for Visitor Pattern Integration ==========

    /**
     * Tests that visitClassConstant correctly integrates with the ClassObfuscator's
     * role as a ConstantVisitor by verifying the visitor pattern works end-to-end.
     */
    @Test
    public void testVisitClassConstant_visitorPatternIntegration() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Use a flag to track if the visitor pattern was properly invoked
        final boolean[] visitorWasCalled = {false};

        doAnswer(invocation -> {
            visitorWasCalled[0] = true;
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        classObfuscator.visitClassConstant(clazz, constant);

        // Assert
        assertTrue(visitorWasCalled[0], "The visitor pattern should have been invoked");
        verify(constant, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant works correctly when called as part of
     * the ConstantVisitor interface implementation.
     */
    @Test
    public void testVisitClassConstant_asConstantVisitor() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);

        // Act - Call through the ConstantVisitor interface
        proguard.classfile.constant.visitor.ConstantVisitor visitor = classObfuscator;
        visitor.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant properly delegates when the ClassConstant
     * references a class that needs to be visited.
     */
    @Test
    public void testVisitClassConstant_delegatesToReferencedClass() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz referencedClass = mock(Clazz.class);

        // Simulate the constant calling back to the visitor with the referenced class
        doAnswer(invocation -> {
            ClassObfuscator visitor = invocation.getArgument(0);
            // The ClassConstant would internally call visitor.visitProgramClass(referencedClass)
            // or visitor.visitLibraryClass(referencedClass)
            // We just verify the visitor was passed correctly
            assertNotNull(visitor);
            assertEquals(classObfuscator, visitor);
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        classObfuscator.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that multiple ClassObfuscator instances can independently process
     * ClassConstants without interfering with each other.
     */
    @Test
    public void testVisitClassConstant_multipleObfuscatorsIndependent() {
        // Arrange
        ClassObfuscator obfuscator1 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );
        ClassObfuscator obfuscator2 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );

        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        obfuscator1.visitClassConstant(clazz1, constant1);
        obfuscator2.visitClassConstant(clazz2, constant2);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(obfuscator1));
        verify(constant2, times(1)).referencedClassAccept(eq(obfuscator2));
    }

    /**
     * Tests that visitClassConstant maintains proper state when processing
     * multiple constants in sequence.
     */
    @Test
    public void testVisitClassConstant_sequentialProcessing() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act - Process multiple constants in sequence
        classObfuscator.visitClassConstant(clazz1, constant1);
        classObfuscator.visitClassConstant(clazz2, constant2);
        classObfuscator.visitClassConstant(clazz3, constant3);

        // Assert - Each should have been processed exactly once with correct parameters
        verify(constant1, times(1)).referencedClassAccept(eq(classObfuscator));
        verify(constant2, times(1)).referencedClassAccept(eq(classObfuscator));
        verify(constant3, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant works with different ClassObfuscator configurations.
     * This ensures the method behavior is consistent regardless of the obfuscator's settings.
     */
    @Test
    public void testVisitClassConstant_withDifferentConfigurations() {
        // Arrange - Create obfuscators with different configurations
        ClassObfuscator obfuscator1 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );

        ClassObfuscator obfuscator2 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, false, null, null, null, true, true
        );

        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act
        obfuscator1.visitClassConstant(mockClazz, constant1);
        obfuscator2.visitClassConstant(mockClazz, constant2);

        // Assert - Both should delegate correctly regardless of configuration
        verify(constant1, times(1)).referencedClassAccept(eq(obfuscator1));
        verify(constant2, times(1)).referencedClassAccept(eq(obfuscator2));
    }

    /**
     * Tests that visitClassConstant handles a scenario where the same ClassConstant
     * is visited multiple times with different Clazz contexts.
     */
    @Test
    public void testVisitClassConstant_sameConstantDifferentClazzes() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        classObfuscator.visitClassConstant(clazz1, constant);
        classObfuscator.visitClassConstant(clazz2, constant);
        classObfuscator.visitClassConstant(clazz3, constant);

        // Assert
        verify(constant, times(3)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant works correctly when the ClassConstant
     * references an outer class that needs obfuscation.
     */
    @Test
    public void testVisitClassConstant_withOuterClassReference() {
        // Arrange
        ClassConstant outerClassConstant = mock(ClassConstant.class);
        Clazz innerClass = mock(Clazz.class);

        // Track that referencedClassAccept was called
        final int[] callCount = {0};
        doAnswer(invocation -> {
            callCount[0]++;
            return null;
        }).when(outerClassConstant).referencedClassAccept(any());

        // Act
        classObfuscator.visitClassConstant(innerClass, outerClassConstant);

        // Assert
        assertEquals(1, callCount[0], "referencedClassAccept should be called once");
        verify(outerClassConstant, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that the same obfuscator instance can be reused for visiting multiple constants.
     */
    @Test
    public void testVisitClassConstant_reuseObfuscatorInstance() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        // Act - Reuse the same obfuscator instance
        classObfuscator.visitClassConstant(mockClazz, constant1);
        classObfuscator.visitClassConstant(mockClazz, constant2);
        classObfuscator.visitClassConstant(mockClazz, constant3);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(classObfuscator));
        verify(constant2, times(1)).referencedClassAccept(eq(classObfuscator));
        verify(constant3, times(1)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant properly handles the delegation pattern
     * where the ClassConstant may internally call back to the ClassObfuscator.
     */
    @Test
    public void testVisitClassConstant_handlesCallbackPattern() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Simulate a callback scenario where referencedClassAccept might trigger
        // additional processing
        final boolean[] callbackOccurred = {false};
        doAnswer(invocation -> {
            ClassObfuscator visitor = invocation.getArgument(0);
            // Verify the visitor is the correct instance
            if (visitor == classObfuscator) {
                callbackOccurred[0] = true;
            }
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        classObfuscator.visitClassConstant(clazz, constant);

        // Assert
        assertTrue(callbackOccurred[0], "Callback should have occurred with correct visitor");
    }

    /**
     * Tests that visitClassConstant works correctly in a mixed scenario with
     * both Clazz and ClassConstant variations.
     */
    @Test
    public void testVisitClassConstant_mixedScenarios() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act - Mix of different Clazz and ClassConstant combinations
        classObfuscator.visitClassConstant(clazz1, constant1);
        classObfuscator.visitClassConstant(clazz1, constant2);
        classObfuscator.visitClassConstant(clazz2, constant1);
        classObfuscator.visitClassConstant(clazz2, constant2);

        // Assert
        verify(constant1, times(2)).referencedClassAccept(eq(classObfuscator));
        verify(constant2, times(2)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that visitClassConstant properly integrates with the overall obfuscation
     * workflow by ensuring the visitor pattern is correctly implemented.
     */
    @Test
    public void testVisitClassConstant_integrationWithObfuscationWorkflow() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Track the number of times referencedClassAccept is called
        final int[] acceptCount = {0};
        doAnswer(invocation -> {
            acceptCount[0]++;
            ClassObfuscator visitor = invocation.getArgument(0);
            assertSame(classObfuscator, visitor, "Visitor should be the same ClassObfuscator instance");
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        classObfuscator.visitClassConstant(clazz, constant);

        // Assert
        assertEquals(1, acceptCount[0], "referencedClassAccept should be called exactly once");
        verify(constant).referencedClassAccept(classObfuscator);
    }

    /**
     * Tests that visitClassConstant is idempotent - calling it multiple times
     * with the same parameters produces consistent behavior.
     */
    @Test
    public void testVisitClassConstant_idempotent() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Act - Call multiple times with same parameters
        classObfuscator.visitClassConstant(clazz, constant);
        classObfuscator.visitClassConstant(clazz, constant);
        classObfuscator.visitClassConstant(clazz, constant);

        // Assert - Should call referencedClassAccept each time
        verify(constant, times(3)).referencedClassAccept(eq(classObfuscator));
    }

    /**
     * Tests that different ClassObfuscator instances maintain independent state
     * when processing the same ClassConstant.
     */
    @Test
    public void testVisitClassConstant_independentObfuscatorState() {
        // Arrange
        ClassObfuscator obfuscator1 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );
        ClassObfuscator obfuscator2 = new ClassObfuscator(
            new proguard.classfile.ClassPool(),
            new proguard.classfile.ClassPool(),
            null, null, true, null, null, null, false, false
        );

        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Act
        obfuscator1.visitClassConstant(clazz, constant);
        obfuscator2.visitClassConstant(clazz, constant);

        // Assert - The constant should accept each obfuscator instance
        verify(constant, times(1)).referencedClassAccept(eq(obfuscator1));
        verify(constant, times(1)).referencedClassAccept(eq(obfuscator2));
        verify(constant, times(2)).referencedClassAccept(any(ClassObfuscator.class));
    }
}
