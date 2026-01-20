package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ClassPool;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.Constant;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.reflect.CallableReferenceInfo;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinCallableReferenceFixer#visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)}.
 * Tests the visitKotlinSyntheticClassMetadata method which processes synthetic class metadata
 * for callable references (function and property references).
 */
public class KotlinCallableReferenceFixerClaude_visitKotlinSyntheticClassMetadataTest {

    private KotlinCallableReferenceFixer fixer;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private Clazz mockClazz;
    private KotlinSyntheticClassKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        fixer = new KotlinCallableReferenceFixer(programClassPool, libraryClassPool);
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinSyntheticClassKindMetadata.class);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can be called without throwing exceptions
     * when callableReferenceInfo is null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullCallableReferenceInfo_doesNotThrowException() {
        // Arrange
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "visitKotlinSyntheticClassMetadata should not throw an exception with null callableReferenceInfo");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata does not interact with clazz
     * when callableReferenceInfo is null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullCallableReferenceInfo_doesNotInteractWithClazz() {
        // Arrange
        mockMetadata.callableReferenceInfo = null;

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify no interactions since callableReferenceInfo is null
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls clazz.accept
     * when callableReferenceInfo is not null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withCallableReferenceInfo_callsClazzAccept() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify clazz.accept was called
        verify(mockClazz, atLeastOnce()).accept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null clazz throws NullPointerException
     * when callableReferenceInfo is not null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullClazzAndCallableReferenceInfo_throwsNullPointerException() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinSyntheticClassMetadata(null, mockMetadata);
        }, "Should throw NullPointerException when clazz is null and processing is needed");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null metadata throws NullPointerException.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinSyntheticClassMetadata(null, null);
        }, "Should throw NullPointerException when both parameters are null");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can be called multiple times
     * with null callableReferenceInfo.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_multipleCallsWithNullCallableReferenceInfo() {
        // Arrange
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Multiple calls should work with null callableReferenceInfo");

        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata works with different clazz instances.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withDifferentClazzInstances() {
        // Arrange
        mockMetadata.callableReferenceInfo = null;
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinSyntheticClassMetadata(mockClazz2, mockMetadata);

        // Assert - no interactions since callableReferenceInfo is null
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockClazz2);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata works with different metadata instances.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withDifferentMetadataInstances() {
        // Arrange
        KotlinSyntheticClassKindMetadata mockMetadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata mockMetadata2 = mock(KotlinSyntheticClassKindMetadata.class);
        mockMetadata1.callableReferenceInfo = null;
        mockMetadata2.callableReferenceInfo = null;

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata1);
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata2);

        // Assert
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with real ClassPool instances works correctly.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withRealClassPools() {
        // Arrange
        ClassPool realProgramPool = new ClassPool();
        ClassPool realLibraryPool = new ClassPool();
        KotlinCallableReferenceFixer fixerWithRealPools =
            new KotlinCallableReferenceFixer(realProgramPool, realLibraryPool);
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixerWithRealPools.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should work with real ClassPool instances");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata accesses getName() from callableReferenceInfo
     * when it is not null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withCallableReferenceInfo_accessesGetName() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify getName was called
        verify(mockCallableRefInfo, atLeastOnce()).getName();
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata accesses getSignature() from callableReferenceInfo
     * when it is not null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withCallableReferenceInfo_accessesGetSignature() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify getSignature was called
        verify(mockCallableRefInfo, atLeastOnce()).getSignature();
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls findMethod when callableReferenceInfo is not null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withCallableReferenceInfo_callsFindMethod() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;
        when(mockClazz.findMethod(anyString(), anyString())).thenReturn(null);

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify findMethod was called to check for getOwner method
        verify(mockClazz, atLeastOnce()).findMethod(anyString(), anyString());
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls callableReferenceInfoAccept
     * when getOwner method is found.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withGetOwnerMethod_callsCallableReferenceInfoAccept() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Mock a method to simulate getOwner method exists
        Method mockMethod = mock(Method.class);
        when(mockClazz.findMethod(anyString(), anyString())).thenReturn(mockMethod);

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify callableReferenceInfoAccept was called
        verify(mockMetadata, atLeastOnce()).callableReferenceInfoAccept(any());
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata does not call callableReferenceInfoAccept
     * when getOwner method is not found.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withoutGetOwnerMethod_doesNotCallCallableReferenceInfoAccept() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // getOwner method does not exist
        when(mockClazz.findMethod(anyString(), anyString())).thenReturn(null);

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify callableReferenceInfoAccept was not called
        verify(mockMetadata, never()).callableReferenceInfoAccept(any());
    }

    /**
     * Tests that multiple instances of KotlinCallableReferenceFixer behave consistently.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinCallableReferenceFixer fixer1 = new KotlinCallableReferenceFixer(programClassPool, libraryClassPool);
        KotlinCallableReferenceFixer fixer2 = new KotlinCallableReferenceFixer(programClassPool, libraryClassPool);
        KotlinSyntheticClassKindMetadata metadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata metadata2 = mock(KotlinSyntheticClassKindMetadata.class);
        metadata1.callableReferenceInfo = null;
        metadata2.callableReferenceInfo = null;

        // Act
        fixer1.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);
        fixer2.visitKotlinSyntheticClassMetadata(mockClazz, metadata2);

        // Assert - both should behave the same way (do nothing)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata works with a real ProgramClass.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withRealProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;
        programClass.constantPool = new Constant[10];
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(programClass, mockMetadata);
        }, "Should work with real ProgramClass");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null clazz and null callableReferenceInfo
     * does not throw an exception.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullClazzAndNullCallableReferenceInfo_doesNotThrow() {
        // Arrange
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert - should not throw since no processing is done
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(null, mockMetadata);
        }, "Should not throw when clazz is null but callableReferenceInfo is also null");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata handles empty name in callableReferenceInfo.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withEmptyName() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("");
        when(mockCallableRefInfo.getSignature()).thenReturn("testSignature");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Act & Assert - should handle empty name without throwing
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should handle empty name in callableReferenceInfo");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata handles empty signature in callableReferenceInfo.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withEmptySignature() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("testName");
        when(mockCallableRefInfo.getSignature()).thenReturn("");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Act & Assert - should handle empty signature without throwing
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should handle empty signature in callableReferenceInfo");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata handles both empty name and signature.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withEmptyNameAndSignature() {
        // Arrange
        CallableReferenceInfo mockCallableRefInfo = mock(CallableReferenceInfo.class);
        when(mockCallableRefInfo.getName()).thenReturn("");
        when(mockCallableRefInfo.getSignature()).thenReturn("");
        mockMetadata.callableReferenceInfo = mockCallableRefInfo;

        // Act & Assert - should handle both empty without throwing
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should handle both empty name and signature");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can be called sequentially with different configurations.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_sequentialCallsWithDifferentConfigurations() {
        // Arrange
        KotlinSyntheticClassKindMetadata metadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata metadata2 = mock(KotlinSyntheticClassKindMetadata.class);
        metadata1.callableReferenceInfo = null;

        CallableReferenceInfo callableRefInfo = mock(CallableReferenceInfo.class);
        when(callableRefInfo.getName()).thenReturn("name");
        when(callableRefInfo.getSignature()).thenReturn("signature");
        metadata2.callableReferenceInfo = callableRefInfo;

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, metadata2);
        }, "Sequential calls with different configurations should work");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null ClassPools in fixer
     * still works when callableReferenceInfo is null.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullClassPools_andNullCallableReferenceInfo() {
        // Arrange
        KotlinCallableReferenceFixer fixerWithNullPools = new KotlinCallableReferenceFixer(null, null);
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert - should work since no processing is done
        assertDoesNotThrow(() -> {
            fixerWithNullPools.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should work with null ClassPools when callableReferenceInfo is null");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata preserves the metadata's state.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_preservesMetadataState() {
        // Arrange
        mockMetadata.callableReferenceInfo = null;

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - metadata's callableReferenceInfo should still be null
        assertNull(mockMetadata.callableReferenceInfo,
                   "Metadata's callableReferenceInfo should remain unchanged");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can handle rapid successive calls.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_rapidSuccessiveCalls() {
        // Arrange
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
            }
        }, "Should handle rapid successive calls");
    }

    /**
     * Tests visitKotlinSyntheticClassMetadata through the KotlinMetadataVisitor interface.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_throughVisitorInterface() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinMetadataVisitor visitor = fixer;
        mockMetadata.callableReferenceInfo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should work when called through the visitor interface");
    }
}
