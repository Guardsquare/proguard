package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.attribute.visitor.ExceptionInfoVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReachableCodeMarker} constructor.
 * Tests the no-argument constructor with signature: ()V
 *
 * This class finds all instruction offsets, branch targets, and exception targets
 * in the CodeAttribute objects that it visits. The constructor initializes the
 * internal state including a boolean array for tracking reachable code offsets.
 */
public class ReachableCodeMarkerClaude_constructorTest {

    /**
     * Tests that the no-argument constructor creates a valid instance.
     * Verifies that ReachableCodeMarker can be instantiated.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertNotNull(marker, "ReachableCodeMarker should be created successfully");
    }

    /**
     * Tests that the constructor creates an instance that implements AttributeVisitor.
     * Verifies that ReachableCodeMarker implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertInstanceOf(AttributeVisitor.class, marker,
            "ReachableCodeMarker should implement AttributeVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements InstructionVisitor.
     * Verifies that ReachableCodeMarker implements the InstructionVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfInstructionVisitor() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertInstanceOf(InstructionVisitor.class, marker,
            "ReachableCodeMarker should implement InstructionVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance that implements ExceptionInfoVisitor.
     * Verifies that ReachableCodeMarker implements the ExceptionInfoVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfExceptionInfoVisitor() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertInstanceOf(ExceptionInfoVisitor.class, marker,
            "ReachableCodeMarker should implement ExceptionInfoVisitor interface");
    }

    /**
     * Tests creating multiple independent ReachableCodeMarker instances.
     * Verifies that multiple instances can be created and are independent of each other.
     */
    @Test
    public void testMultipleInstancesAreIndependent() {
        // Act
        ReachableCodeMarker marker1 = new ReachableCodeMarker();
        ReachableCodeMarker marker2 = new ReachableCodeMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotSame(marker1, marker2, "Marker instances should be different");
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(marker, "ReachableCodeMarker should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating a sequence of ReachableCodeMarker instances.
     * Verifies that multiple markers can be created sequentially without issues.
     */
    @Test
    public void testSequentialMarkerCreation() {
        // Act & Assert - create multiple markers sequentially
        for (int i = 0; i < 10; i++) {
            ReachableCodeMarker marker = new ReachableCodeMarker();
            assertNotNull(marker, "Marker " + i + " should be created");
        }
    }

    /**
     * Tests that the constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state by checking it can be used
     * as all three visitor interfaces.
     */
    @Test
    public void testConstructorInitializesInstanceProperly() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertNotNull(marker, "ReachableCodeMarker should be created");

        // The marker should be usable as all three visitor types
        AttributeVisitor attributeVisitor = marker;
        InstructionVisitor instructionVisitor = marker;
        ExceptionInfoVisitor exceptionInfoVisitor = marker;

        assertNotNull(attributeVisitor, "ReachableCodeMarker should be usable as AttributeVisitor");
        assertNotNull(instructionVisitor, "ReachableCodeMarker should be usable as InstructionVisitor");
        assertNotNull(exceptionInfoVisitor, "ReachableCodeMarker should be usable as ExceptionInfoVisitor");
    }

    /**
     * Tests that the marker is initialized to report no offsets as reachable initially.
     * Verifies that before processing any code, the marker doesn't report any offsets as reachable.
     * This test checks that the internal state is properly initialized.
     */
    @Test
    public void testConstructorInitializesReachabilityState() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertNotNull(marker, "ReachableCodeMarker should be created");

        // Before visiting any code attribute, check some offsets - they should be unreachable
        // The initial array size is ClassEstimates.TYPICAL_CODE_LENGTH
        assertFalse(marker.isReachable(0), "Offset 0 should not be reachable initially");
        assertFalse(marker.isReachable(10), "Offset 10 should not be reachable initially");
        assertFalse(marker.isReachable(100), "Offset 100 should not be reachable initially");
    }

    /**
     * Tests that the marker can check reachability of a range of offsets.
     * Verifies that the marker's isReachable(startOffset, endOffset) method works after construction.
     */
    @Test
    public void testConstructorInitializesRangeReachabilityState() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertNotNull(marker, "ReachableCodeMarker should be created");

        // Before visiting any code attribute, check range - should be unreachable
        assertFalse(marker.isReachable(0, 10), "Offset range 0-10 should not be reachable initially");
        assertFalse(marker.isReachable(50, 100), "Offset range 50-100 should not be reachable initially");
    }

    /**
     * Tests creating multiple markers and verifying their independence.
     * Verifies that each marker maintains its own internal state.
     */
    @Test
    public void testMultipleMarkersHaveIndependentState() {
        // Act
        ReachableCodeMarker marker1 = new ReachableCodeMarker();
        ReachableCodeMarker marker2 = new ReachableCodeMarker();
        ReachableCodeMarker marker3 = new ReachableCodeMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");

        // All should have independent state (no offsets reachable)
        assertFalse(marker1.isReachable(0), "First marker: offset 0 should not be reachable");
        assertFalse(marker2.isReachable(0), "Second marker: offset 0 should not be reachable");
        assertFalse(marker3.isReachable(0), "Third marker: offset 0 should not be reachable");

        assertNotSame(marker1, marker2, "First and second markers should be different instances");
        assertNotSame(marker2, marker3, "Second and third markers should be different instances");
        assertNotSame(marker1, marker3, "First and third markers should be different instances");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions.
     * Verifies that the constructor is robust and doesn't fail during instantiation.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ReachableCodeMarker marker = new ReachableCodeMarker();
            assertNotNull(marker, "ReachableCodeMarker should be created without throwing");
        }, "Constructor should not throw any exception");
    }

    /**
     * Tests that multiple markers can be created in rapid succession.
     * Verifies that there are no resource contention or initialization issues.
     */
    @Test
    public void testRapidMarkerCreation() {
        // Act & Assert
        for (int i = 0; i < 100; i++) {
            ReachableCodeMarker marker = new ReachableCodeMarker();
            assertNotNull(marker, "Marker " + i + " should be created in rapid succession");
        }
    }

    /**
     * Tests that the marker instance is reusable across multiple operations.
     * Verifies that a single marker instance can be created and used as different visitor types.
     */
    @Test
    public void testMarkerIsReusableAcrossVisitorTypes() {
        // Act
        ReachableCodeMarker marker = new ReachableCodeMarker();

        // Assert
        assertNotNull(marker, "ReachableCodeMarker should be created");

        // Cast to different visitor types to ensure it's usable as all three
        AttributeVisitor attributeVisitor = (AttributeVisitor) marker;
        InstructionVisitor instructionVisitor = (InstructionVisitor) marker;
        ExceptionInfoVisitor exceptionInfoVisitor = (ExceptionInfoVisitor) marker;

        assertSame(marker, attributeVisitor, "Cast to AttributeVisitor should return same instance");
        assertSame(marker, instructionVisitor, "Cast to InstructionVisitor should return same instance");
        assertSame(marker, exceptionInfoVisitor, "Cast to ExceptionInfoVisitor should return same instance");
    }

    /**
     * Tests the stability of the constructor by creating instances in a loop.
     * Verifies that repeated construction doesn't cause issues.
     */
    @Test
    public void testConstructorStability() {
        // Act & Assert
        ReachableCodeMarker previousMarker = null;
        for (int i = 0; i < 50; i++) {
            ReachableCodeMarker marker = new ReachableCodeMarker();
            assertNotNull(marker, "Marker " + i + " should be created");

            if (previousMarker != null) {
                assertNotSame(previousMarker, marker,
                    "Each new marker should be a different instance");
            }
            previousMarker = marker;
        }
    }
}
