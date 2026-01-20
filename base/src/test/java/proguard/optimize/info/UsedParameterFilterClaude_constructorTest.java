package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.ParameterVisitor;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UsedParameterFilter constructors:
 * - {@link UsedParameterFilter#UsedParameterFilter(ParameterVisitor)}
 * - {@link UsedParameterFilter#UsedParameterFilter(ParameterVisitor, ParameterVisitor)}
 *
 * The single-argument constructor creates a UsedParameterFilter that delegates visits to used
 * parameters to the given parameter visitor. Internally, it calls the two-argument constructor
 * with null as the second parameter (for unused parameters).
 *
 * The two-argument constructor creates a UsedParameterFilter that delegates to one of the two
 * given parameter visitors, depending on whether a parameter is used or unused.
 *
 * Key behavior:
 * - Stores the provided ParameterVisitor(s) in private final fields
 * - The single-arg constructor delegates to the two-arg constructor with null for unusedParameterVisitor
 * - Both constructors accept null values for visitor parameters
 */
public class UsedParameterFilterClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates a non-null instance.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(visitor);

        // Assert
        assertNotNull(filter, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor stores the usedParameterVisitor parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesUsedParameterVisitor() throws Exception {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(visitor);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        field.setAccessible(true);
        ParameterVisitor storedVisitor = (ParameterVisitor) field.get(filter);
        assertSame(visitor, storedVisitor, "Constructor should store the provided usedParameterVisitor");
    }

    /**
     * Tests that the constructor sets the unusedParameterVisitor field to null.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_setsUnusedParameterVisitorToNull() throws Exception {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(visitor);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        field.setAccessible(true);
        ParameterVisitor storedVisitor = (ParameterVisitor) field.get(filter);
        assertNull(storedVisitor, "Constructor should set unusedParameterVisitor to null");
    }

    /**
     * Tests that the constructor works with a null visitor.
     */
    @Test
    public void testConstructor_withNullVisitor() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            UsedParameterFilter filter = new UsedParameterFilter(null);
            assertNotNull(filter, "Constructor should accept null visitor");
        }, "Constructor should not throw when provided null visitor");
    }

    /**
     * Tests that the constructor stores null when provided a null visitor.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesNullVisitorCorrectly() throws Exception {
        // Act
        UsedParameterFilter filter = new UsedParameterFilter(null);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        field.setAccessible(true);
        ParameterVisitor storedVisitor = (ParameterVisitor) field.get(filter);
        assertNull(storedVisitor, "Constructor should store null when provided null visitor");
    }

    /**
     * Tests that multiple instances can be created independently with the same visitor.
     */
    @Test
    public void testConstructor_multipleInstancesWithSameVisitor() {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(visitor);
        UsedParameterFilter filter2 = new UsedParameterFilter(visitor);

        // Assert
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotSame(filter1, filter2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that multiple instances can be created independently with different visitors.
     */
    @Test
    public void testConstructor_multipleInstancesWithDifferentVisitors() {
        // Arrange
        TrackingParameterVisitor visitor1 = new TrackingParameterVisitor();
        TrackingParameterVisitor visitor2 = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(visitor1);
        UsedParameterFilter filter2 = new UsedParameterFilter(visitor2);

        // Assert
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotSame(filter1, filter2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new UsedParameterFilter(visitor),
            "Constructor should not throw any exception with valid input");
    }

    /**
     * Tests that the constructed instance implements ParameterVisitor interface.
     */
    @Test
    public void testConstructor_implementsParameterVisitor() {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(visitor);

        // Assert
        assertTrue(filter instanceof ParameterVisitor,
            "UsedParameterFilter should implement ParameterVisitor interface");
    }

    /**
     * Tests that the constructor can be called multiple times consecutively.
     */
    @Test
    public void testConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            TrackingParameterVisitor visitor = new TrackingParameterVisitor();
            UsedParameterFilter filter = new UsedParameterFilter(visitor);
            assertNotNull(filter, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests that the constructor stores the correct reference to the visitor.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesExactVisitorReference() throws Exception {
        // Arrange
        TrackingParameterVisitor visitor1 = new TrackingParameterVisitor();
        TrackingParameterVisitor visitor2 = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(visitor1);
        UsedParameterFilter filter2 = new UsedParameterFilter(visitor2);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        field.setAccessible(true);

        ParameterVisitor stored1 = (ParameterVisitor) field.get(filter1);
        ParameterVisitor stored2 = (ParameterVisitor) field.get(filter2);

        assertSame(visitor1, stored1, "Filter1 should store visitor1");
        assertSame(visitor2, stored2, "Filter2 should store visitor2");
        assertNotSame(stored1, stored2, "Different filters should store different visitors");
    }

    /**
     * Tests that the constructor properly delegates to the two-argument constructor.
     * This is verified by checking both fields are set correctly.
     * Reflection is necessary because the fields are private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_delegatesToTwoArgConstructor() throws Exception {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(visitor);

        // Assert - verify both fields are set as expected
        Field usedField = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        usedField.setAccessible(true);
        ParameterVisitor storedUsed = (ParameterVisitor) usedField.get(filter);
        assertSame(visitor, storedUsed, "usedParameterVisitor should be set to provided visitor");

        Field unusedField = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        unusedField.setAccessible(true);
        ParameterVisitor storedUnused = (ParameterVisitor) unusedField.get(filter);
        assertNull(storedUnused, "unusedParameterVisitor should be set to null");
    }

    /**
     * Tests that constructed filters with null visitors don't interfere with each other.
     */
    @Test
    public void testConstructor_multipleNullVisitors_independentInstances() {
        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(null);
        UsedParameterFilter filter2 = new UsedParameterFilter(null);
        UsedParameterFilter filter3 = new UsedParameterFilter(null);

        // Assert
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotNull(filter3, "Third filter should be created");
        assertNotSame(filter1, filter2, "filter1 and filter2 should be distinct");
        assertNotSame(filter2, filter3, "filter2 and filter3 should be distinct");
        assertNotSame(filter1, filter3, "filter1 and filter3 should be distinct");
    }

    /**
     * Tests that the constructor works correctly as part of a chain of ParameterVisitors.
     */
    @Test
    public void testConstructor_asPartOfVisitorChain() {
        // Arrange
        TrackingParameterVisitor innerVisitor = new TrackingParameterVisitor();
        UsedParameterFilter middleFilter = new UsedParameterFilter(innerVisitor);

        // Act
        UsedParameterFilter outerFilter = new UsedParameterFilter(middleFilter);

        // Assert
        assertNotNull(outerFilter, "Outer filter should be created");
        assertNotNull(middleFilter, "Middle filter should be created");
        assertNotSame(outerFilter, middleFilter, "Filters should be distinct instances");
    }

    /**
     * Tests that the constructor can handle the same visitor being used in multiple filters.
     */
    @Test
    public void testConstructor_sameVisitorInMultipleFilters() throws Exception {
        // Arrange
        TrackingParameterVisitor sharedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(sharedVisitor);
        UsedParameterFilter filter2 = new UsedParameterFilter(sharedVisitor);
        UsedParameterFilter filter3 = new UsedParameterFilter(sharedVisitor);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        field.setAccessible(true);

        ParameterVisitor stored1 = (ParameterVisitor) field.get(filter1);
        ParameterVisitor stored2 = (ParameterVisitor) field.get(filter2);
        ParameterVisitor stored3 = (ParameterVisitor) field.get(filter3);

        assertSame(sharedVisitor, stored1, "Filter1 should reference shared visitor");
        assertSame(sharedVisitor, stored2, "Filter2 should reference shared visitor");
        assertSame(sharedVisitor, stored3, "Filter3 should reference shared visitor");
        assertSame(stored1, stored2, "All filters should reference the same visitor");
        assertSame(stored2, stored3, "All filters should reference the same visitor");
    }

    // ==================== Two-Argument Constructor Tests ====================

    /**
     * Tests that the two-arg constructor successfully creates a non-null instance.
     */
    @Test
    public void testTwoArgConstructor_createsNonNullInstance() {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedVisitor);

        // Assert
        assertNotNull(filter, "Two-arg constructor should create a non-null instance");
    }

    /**
     * Tests that the two-arg constructor stores the usedParameterVisitor parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_storesUsedParameterVisitor() throws Exception {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedVisitor);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        field.setAccessible(true);
        ParameterVisitor storedVisitor = (ParameterVisitor) field.get(filter);
        assertSame(usedVisitor, storedVisitor, "Constructor should store the provided usedParameterVisitor");
    }

    /**
     * Tests that the two-arg constructor stores the unusedParameterVisitor parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_storesUnusedParameterVisitor() throws Exception {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedVisitor);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        field.setAccessible(true);
        ParameterVisitor storedVisitor = (ParameterVisitor) field.get(filter);
        assertSame(unusedVisitor, storedVisitor, "Constructor should store the provided unusedParameterVisitor");
    }

    /**
     * Tests that the two-arg constructor stores both visitors correctly.
     * Reflection is necessary because the fields are private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_storesBothVisitorsCorrectly() throws Exception {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedVisitor);

        // Assert
        Field usedField = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        usedField.setAccessible(true);
        ParameterVisitor storedUsed = (ParameterVisitor) usedField.get(filter);
        assertSame(usedVisitor, storedUsed, "usedParameterVisitor should be stored correctly");

        Field unusedField = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        unusedField.setAccessible(true);
        ParameterVisitor storedUnused = (ParameterVisitor) unusedField.get(filter);
        assertSame(unusedVisitor, storedUnused, "unusedParameterVisitor should be stored correctly");
    }

    /**
     * Tests that the two-arg constructor works with null for the usedParameterVisitor.
     */
    @Test
    public void testTwoArgConstructor_withNullUsedVisitor() {
        // Arrange
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> {
            UsedParameterFilter filter = new UsedParameterFilter(null, unusedVisitor);
            assertNotNull(filter, "Constructor should accept null for usedParameterVisitor");
        }, "Constructor should not throw when provided null usedParameterVisitor");
    }

    /**
     * Tests that the two-arg constructor stores null correctly for usedParameterVisitor.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_storesNullUsedVisitorCorrectly() throws Exception {
        // Arrange
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(null, unusedVisitor);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        field.setAccessible(true);
        ParameterVisitor storedVisitor = (ParameterVisitor) field.get(filter);
        assertNull(storedVisitor, "Constructor should store null for usedParameterVisitor");
    }

    /**
     * Tests that the two-arg constructor works with null for the unusedParameterVisitor.
     */
    @Test
    public void testTwoArgConstructor_withNullUnusedVisitor() {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> {
            UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, null);
            assertNotNull(filter, "Constructor should accept null for unusedParameterVisitor");
        }, "Constructor should not throw when provided null unusedParameterVisitor");
    }

    /**
     * Tests that the two-arg constructor stores null correctly for unusedParameterVisitor.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_storesNullUnusedVisitorCorrectly() throws Exception {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, null);

        // Assert
        Field field = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        field.setAccessible(true);
        ParameterVisitor storedVisitor = (ParameterVisitor) field.get(filter);
        assertNull(storedVisitor, "Constructor should store null for unusedParameterVisitor");
    }

    /**
     * Tests that the two-arg constructor works with null for both visitors.
     */
    @Test
    public void testTwoArgConstructor_withBothVisitorsNull() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            UsedParameterFilter filter = new UsedParameterFilter(null, null);
            assertNotNull(filter, "Constructor should accept null for both visitors");
        }, "Constructor should not throw when provided null for both visitors");
    }

    /**
     * Tests that the two-arg constructor stores null correctly for both visitors.
     * Reflection is necessary because the fields are private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_storesBothNullVisitorsCorrectly() throws Exception {
        // Act
        UsedParameterFilter filter = new UsedParameterFilter(null, null);

        // Assert
        Field usedField = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        usedField.setAccessible(true);
        ParameterVisitor storedUsed = (ParameterVisitor) usedField.get(filter);
        assertNull(storedUsed, "usedParameterVisitor should be null");

        Field unusedField = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        unusedField.setAccessible(true);
        ParameterVisitor storedUnused = (ParameterVisitor) unusedField.get(filter);
        assertNull(storedUnused, "unusedParameterVisitor should be null");
    }

    /**
     * Tests that the two-arg constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testTwoArgConstructor_doesNotThrowException() {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new UsedParameterFilter(usedVisitor, unusedVisitor),
            "Constructor should not throw any exception with valid input");
    }

    /**
     * Tests that the two-arg constructor creates an instance implementing ParameterVisitor.
     */
    @Test
    public void testTwoArgConstructor_implementsParameterVisitor() {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedVisitor);

        // Assert
        assertTrue(filter instanceof ParameterVisitor,
            "UsedParameterFilter should implement ParameterVisitor interface");
    }

    /**
     * Tests that multiple instances can be created independently using the two-arg constructor.
     */
    @Test
    public void testTwoArgConstructor_multipleIndependentInstances() {
        // Arrange
        TrackingParameterVisitor usedVisitor1 = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor1 = new TrackingParameterVisitor();
        TrackingParameterVisitor usedVisitor2 = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor2 = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(usedVisitor1, unusedVisitor1);
        UsedParameterFilter filter2 = new UsedParameterFilter(usedVisitor2, unusedVisitor2);

        // Assert
        assertNotNull(filter1, "First filter should be created");
        assertNotNull(filter2, "Second filter should be created");
        assertNotSame(filter1, filter2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that the two-arg constructor can be called consecutively multiple times.
     */
    @Test
    public void testTwoArgConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
            TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();
            UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedVisitor);
            assertNotNull(filter, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests that the two-arg constructor can use the same visitor for both parameters.
     */
    @Test
    public void testTwoArgConstructor_sameVisitorForBothParameters() throws Exception {
        // Arrange
        TrackingParameterVisitor sharedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(sharedVisitor, sharedVisitor);

        // Assert
        Field usedField = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        usedField.setAccessible(true);
        ParameterVisitor storedUsed = (ParameterVisitor) usedField.get(filter);

        Field unusedField = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        unusedField.setAccessible(true);
        ParameterVisitor storedUnused = (ParameterVisitor) unusedField.get(filter);

        assertSame(sharedVisitor, storedUsed, "usedParameterVisitor should reference shared visitor");
        assertSame(sharedVisitor, storedUnused, "unusedParameterVisitor should reference shared visitor");
        assertSame(storedUsed, storedUnused, "Both fields should reference the same visitor instance");
    }

    /**
     * Tests that multiple filters can share the same visitor instances.
     */
    @Test
    public void testTwoArgConstructor_sharedVisitorsAcrossFilters() throws Exception {
        // Arrange
        TrackingParameterVisitor sharedUsedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor sharedUnusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(sharedUsedVisitor, sharedUnusedVisitor);
        UsedParameterFilter filter2 = new UsedParameterFilter(sharedUsedVisitor, sharedUnusedVisitor);
        UsedParameterFilter filter3 = new UsedParameterFilter(sharedUsedVisitor, sharedUnusedVisitor);

        // Assert
        Field usedField = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        usedField.setAccessible(true);
        Field unusedField = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        unusedField.setAccessible(true);

        ParameterVisitor used1 = (ParameterVisitor) usedField.get(filter1);
        ParameterVisitor used2 = (ParameterVisitor) usedField.get(filter2);
        ParameterVisitor used3 = (ParameterVisitor) usedField.get(filter3);
        ParameterVisitor unused1 = (ParameterVisitor) unusedField.get(filter1);
        ParameterVisitor unused2 = (ParameterVisitor) unusedField.get(filter2);
        ParameterVisitor unused3 = (ParameterVisitor) unusedField.get(filter3);

        assertSame(sharedUsedVisitor, used1, "Filter1 should reference shared used visitor");
        assertSame(sharedUsedVisitor, used2, "Filter2 should reference shared used visitor");
        assertSame(sharedUsedVisitor, used3, "Filter3 should reference shared used visitor");
        assertSame(sharedUnusedVisitor, unused1, "Filter1 should reference shared unused visitor");
        assertSame(sharedUnusedVisitor, unused2, "Filter2 should reference shared unused visitor");
        assertSame(sharedUnusedVisitor, unused3, "Filter3 should reference shared unused visitor");
    }

    /**
     * Tests that the two-arg constructor can create filters as part of a visitor chain.
     */
    @Test
    public void testTwoArgConstructor_asPartOfVisitorChain() {
        // Arrange
        TrackingParameterVisitor leafVisitor1 = new TrackingParameterVisitor();
        TrackingParameterVisitor leafVisitor2 = new TrackingParameterVisitor();
        UsedParameterFilter middleFilter = new UsedParameterFilter(leafVisitor1, leafVisitor2);

        // Act
        UsedParameterFilter topFilter = new UsedParameterFilter(middleFilter, middleFilter);

        // Assert
        assertNotNull(topFilter, "Top filter should be created");
        assertNotNull(middleFilter, "Middle filter should be created");
        assertNotSame(topFilter, middleFilter, "Filters should be distinct instances");
    }

    /**
     * Tests that the two-arg constructor stores exact references (not copies).
     * Reflection is necessary because the fields are private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_storesExactReferences() throws Exception {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        TrackingParameterVisitor unusedVisitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedVisitor);

        // Modify the original visitors
        usedVisitor.visited = true;
        unusedVisitor.visitCount = 42;

        // Assert - verify the stored references reflect the changes
        Field usedField = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        usedField.setAccessible(true);
        TrackingParameterVisitor storedUsed = (TrackingParameterVisitor) usedField.get(filter);

        Field unusedField = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        unusedField.setAccessible(true);
        TrackingParameterVisitor storedUnused = (TrackingParameterVisitor) unusedField.get(filter);

        assertTrue(storedUsed.visited, "Stored visitor should reflect changes to original");
        assertEquals(42, storedUnused.visitCount, "Stored visitor should reflect changes to original");
    }

    /**
     * Tests that the two-arg constructor works with different visitor types.
     */
    @Test
    public void testTwoArgConstructor_withDifferentVisitorTypes() {
        // Arrange
        TrackingParameterVisitor usedVisitor = new TrackingParameterVisitor();
        UsedParameterFilter unusedFilter = new UsedParameterFilter(usedVisitor);

        // Act
        UsedParameterFilter filter = new UsedParameterFilter(usedVisitor, unusedFilter);

        // Assert
        assertNotNull(filter, "Constructor should work with different visitor types");
    }

    /**
     * Tests that both constructors create instances with the same interface capabilities.
     */
    @Test
    public void testBothConstructors_createEquivalentInterfaces() {
        // Arrange
        TrackingParameterVisitor visitor = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(visitor);
        UsedParameterFilter filter2 = new UsedParameterFilter(visitor, null);

        // Assert
        assertTrue(filter1 instanceof ParameterVisitor, "Single-arg filter should implement ParameterVisitor");
        assertTrue(filter2 instanceof ParameterVisitor, "Two-arg filter should implement ParameterVisitor");
        assertEquals(filter1.getClass(), filter2.getClass(), "Both constructors should create the same type");
    }

    /**
     * Tests that the two-arg constructor stores references independently for each instance.
     * Reflection is necessary because the fields are private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testTwoArgConstructor_independentStorageAcrossInstances() throws Exception {
        // Arrange
        TrackingParameterVisitor visitor1a = new TrackingParameterVisitor();
        TrackingParameterVisitor visitor1b = new TrackingParameterVisitor();
        TrackingParameterVisitor visitor2a = new TrackingParameterVisitor();
        TrackingParameterVisitor visitor2b = new TrackingParameterVisitor();

        // Act
        UsedParameterFilter filter1 = new UsedParameterFilter(visitor1a, visitor1b);
        UsedParameterFilter filter2 = new UsedParameterFilter(visitor2a, visitor2b);

        // Assert
        Field usedField = UsedParameterFilter.class.getDeclaredField("usedParameterVisitor");
        usedField.setAccessible(true);
        Field unusedField = UsedParameterFilter.class.getDeclaredField("unusedParameterVisitor");
        unusedField.setAccessible(true);

        ParameterVisitor used1 = (ParameterVisitor) usedField.get(filter1);
        ParameterVisitor unused1 = (ParameterVisitor) unusedField.get(filter1);
        ParameterVisitor used2 = (ParameterVisitor) usedField.get(filter2);
        ParameterVisitor unused2 = (ParameterVisitor) unusedField.get(filter2);

        assertSame(visitor1a, used1, "Filter1 should store visitor1a");
        assertSame(visitor1b, unused1, "Filter1 should store visitor1b");
        assertSame(visitor2a, used2, "Filter2 should store visitor2a");
        assertSame(visitor2b, unused2, "Filter2 should store visitor2b");

        assertNotSame(used1, used2, "Different filters should store different used visitors");
        assertNotSame(unused1, unused2, "Different filters should store different unused visitors");
    }

    /**
     * Helper class that tracks whether it was visited.
     */
    private static class TrackingParameterVisitor implements ParameterVisitor {
        boolean visited = false;
        int visitCount = 0;

        @Override
        public void visitParameter(Clazz clazz, Member member, int parameterIndex,
                                  int parameterCount, int parameterOffset, int parameterSize,
                                  String parameterType, Clazz referencedClass) {
            visited = true;
            visitCount++;
        }
    }
}
