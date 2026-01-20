package proguard.fixer.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinTypeMetadata;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAnnotationFlagFixer#visitFunctionReceiverType(Clazz, KotlinMetadata, KotlinFunctionMetadata, KotlinTypeMetadata)}.
 * Tests the visitFunctionReceiverType method which resets the annotation counter and accepts it on the referenced method.
 */
public class KotlinAnnotationFlagFixerClaude_visitFunctionReceiverTypeTest {

    private KotlinAnnotationFlagFixer fixer;
    private Clazz mockClazz;
    private KotlinMetadata mockMetadata;
    private KotlinFunctionMetadata mockFunctionMetadata;
    private KotlinTypeMetadata mockTypeMetadata;

    @BeforeEach
    public void setUp() {
        fixer = new KotlinAnnotationFlagFixer();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinMetadata.class);
        mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        mockTypeMetadata = mock(KotlinTypeMetadata.class);
    }

    /**
     * Tests that visitFunctionReceiverType can be called without throwing exceptions.
     */
    @Test
    public void testVisitFunctionReceiverType_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);
        }, "visitFunctionReceiverType should not throw an exception");
    }

    /**
     * Tests that visitFunctionReceiverType calls referencedMethodAccept on the function metadata.
     * This verifies that the method delegates to the function metadata's referenced method.
     */
    @Test
    public void testVisitFunctionReceiverType_callsReferencedMethodAccept() {
        // Act
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);

        // Assert
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType can be called multiple times.
     * This verifies that the method is stateless and can be called repeatedly.
     */
    @Test
    public void testVisitFunctionReceiverType_canBeCalledMultipleTimes() {
        // Act
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);

        // Assert - verify that each call triggers referencedMethodAccept
        verify(mockFunctionMetadata, times(3)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType works with different clazz instances.
     * This verifies that the method properly handles different clazz parameters.
     */
    @Test
    public void testVisitFunctionReceiverType_withDifferentClazz_callsReferencedMethodAccept() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);
        fixer.visitFunctionReceiverType(mockClazz2, mockMetadata, mockFunctionMetadata, mockTypeMetadata);

        // Assert - verify that both calls trigger referencedMethodAccept
        verify(mockFunctionMetadata, times(2)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitFunctionReceiverType_withDifferentMetadata_callsReferencedMethodAccept() {
        // Arrange
        KotlinMetadata mockMetadata2 = mock(KotlinMetadata.class);

        // Act
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata2, mockFunctionMetadata, mockTypeMetadata);

        // Assert - verify that both calls trigger referencedMethodAccept
        verify(mockFunctionMetadata, times(2)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType works with different function metadata instances.
     * This verifies that the method properly handles different function metadata parameters.
     */
    @Test
    public void testVisitFunctionReceiverType_withDifferentFunctionMetadata_callsCorrectInstance() {
        // Arrange
        KotlinFunctionMetadata mockFunctionMetadata2 = mock(KotlinFunctionMetadata.class);

        // Act
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata2, mockTypeMetadata);

        // Assert - verify that each function metadata instance's referencedMethodAccept is called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
        verify(mockFunctionMetadata2, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType works with different type metadata instances.
     * This verifies that the method properly handles different type metadata parameters.
     */
    @Test
    public void testVisitFunctionReceiverType_withDifferentTypeMetadata_callsReferencedMethodAccept() {
        // Arrange
        KotlinTypeMetadata mockTypeMetadata2 = mock(KotlinTypeMetadata.class);

        // Act
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata);
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, mockTypeMetadata2);

        // Assert - verify that both calls trigger referencedMethodAccept
        verify(mockFunctionMetadata, times(2)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType with null Clazz does not throw.
     * The method doesn't use the clazz parameter, so null should be acceptable.
     */
    @Test
    public void testVisitFunctionReceiverType_withNullClazz_callsReferencedMethodAccept() {
        // Act
        fixer.visitFunctionReceiverType(null, mockMetadata, mockFunctionMetadata, mockTypeMetadata);

        // Assert - verify that referencedMethodAccept is still called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType with null metadata does not throw.
     * The method doesn't use the metadata parameter, so null should be acceptable.
     */
    @Test
    public void testVisitFunctionReceiverType_withNullMetadata_callsReferencedMethodAccept() {
        // Act
        fixer.visitFunctionReceiverType(mockClazz, null, mockFunctionMetadata, mockTypeMetadata);

        // Assert - verify that referencedMethodAccept is still called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType with null function metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitFunctionReceiverType_withNullFunctionMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitFunctionReceiverType(mockClazz, mockMetadata, null, mockTypeMetadata);
        }, "Should throw NullPointerException when function metadata is null");
    }

    /**
     * Tests that visitFunctionReceiverType with null type metadata does not throw.
     * The method doesn't use the type metadata parameter, so null should be acceptable.
     */
    @Test
    public void testVisitFunctionReceiverType_withNullTypeMetadata_callsReferencedMethodAccept() {
        // Act
        fixer.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata, null);

        // Assert - verify that referencedMethodAccept is still called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that multiple instances of KotlinAnnotationFlagFixer behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitFunctionReceiverType_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinAnnotationFlagFixer fixer1 = new KotlinAnnotationFlagFixer();
        KotlinAnnotationFlagFixer fixer2 = new KotlinAnnotationFlagFixer();
        KotlinFunctionMetadata mockFunctionMetadata1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata2 = mock(KotlinFunctionMetadata.class);

        // Act
        fixer1.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata1, mockTypeMetadata);
        fixer2.visitFunctionReceiverType(mockClazz, mockMetadata, mockFunctionMetadata2, mockTypeMetadata);

        // Assert - both should make the same calls
        verify(mockFunctionMetadata1, times(1)).referencedMethodAccept(any(MemberVisitor.class));
        verify(mockFunctionMetadata2, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType with all parameters null except function metadata
     * only throws when function metadata is accessed.
     */
    @Test
    public void testVisitFunctionReceiverType_withAllNullExceptFunctionMetadata_callsReferencedMethodAccept() {
        // Act
        fixer.visitFunctionReceiverType(null, null, mockFunctionMetadata, null);

        // Assert - verify that referencedMethodAccept is still called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitFunctionReceiverType with all parameters null throws NullPointerException.
     * This is expected since function metadata is required.
     */
    @Test
    public void testVisitFunctionReceiverType_withAllNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitFunctionReceiverType(null, null, null, null);
        }, "Should throw NullPointerException when function metadata is null");
    }
}
