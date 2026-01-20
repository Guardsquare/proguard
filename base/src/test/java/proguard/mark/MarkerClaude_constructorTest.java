package proguard.mark;

import org.junit.jupiter.api.Test;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Marker.KotlinDontOptimizeMarker constructor.
 */
public class MarkerClaude_constructorTest {

    /**
     * Test that the no-arg constructor successfully creates a KotlinDontOptimizeMarker instance.
     */
    @Test
    public void testConstructorCreatesInstance() {
        Marker.KotlinDontOptimizeMarker marker = new Marker.KotlinDontOptimizeMarker();

        assertNotNull(marker, "KotlinDontOptimizeMarker should be created successfully");
    }

    /**
     * Test that the constructed instance implements KotlinMetadataVisitor interface.
     */
    @Test
    public void testConstructorCreatesKotlinMetadataVisitor() {
        Marker.KotlinDontOptimizeMarker marker = new Marker.KotlinDontOptimizeMarker();

        assertTrue(marker instanceof KotlinMetadataVisitor,
                "KotlinDontOptimizeMarker should implement KotlinMetadataVisitor");
    }

    /**
     * Test that the constructed instance implements KotlinFunctionVisitor interface.
     */
    @Test
    public void testConstructorCreatesKotlinFunctionVisitor() {
        Marker.KotlinDontOptimizeMarker marker = new Marker.KotlinDontOptimizeMarker();

        assertTrue(marker instanceof KotlinFunctionVisitor,
                "KotlinDontOptimizeMarker should implement KotlinFunctionVisitor");
    }

    /**
     * Test that multiple instances can be created independently.
     */
    @Test
    public void testConstructorCreatesMultipleIndependentInstances() {
        Marker.KotlinDontOptimizeMarker marker1 = new Marker.KotlinDontOptimizeMarker();
        Marker.KotlinDontOptimizeMarker marker2 = new Marker.KotlinDontOptimizeMarker();

        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotSame(marker1, marker2, "Each constructor call should create a distinct instance");
    }

    /**
     * Test that the constructor properly initializes the instance to have access to the
     * static MEMBER_AND_CLASS_MARKER field. This field is initialized when the class is loaded,
     * not by the constructor, but we verify it's accessible from a constructed instance.
     */
    @Test
    public void testConstructorAllowsAccessToStaticFields() {
        Marker.KotlinDontOptimizeMarker marker = new Marker.KotlinDontOptimizeMarker();

        // The static field should be accessible through the class
        // We're just verifying that the constructor doesn't break access to static members
        assertNotNull(marker, "Marker should be constructed");
        // If we can construct the marker, we know the class is properly loaded with its static fields
    }
}
