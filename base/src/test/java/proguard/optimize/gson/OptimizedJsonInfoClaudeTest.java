package proguard.optimize.gson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OptimizedJsonInfo}.
 * Tests the constructor and assignIndices methods.
 */
public class OptimizedJsonInfoClaudeTest {

    private OptimizedJsonInfo optimizedJsonInfo;

    @BeforeEach
    public void setUp() {
        optimizedJsonInfo = new OptimizedJsonInfo();
    }

    // Tests for constructor: <init>.()V

    /**
     * Tests that the default constructor creates a non-null OptimizedJsonInfo instance.
     */
    @Test
    public void testConstructorCreatesNonNullInstance() {
        // Act
        OptimizedJsonInfo info = new OptimizedJsonInfo();

        // Assert
        assertNotNull(info, "OptimizedJsonInfo instance should not be null");
    }

    /**
     * Tests that the default constructor initializes classIndices to an empty HashMap.
     */
    @Test
    public void testConstructorInitializesClassIndicesToEmptyHashMap() {
        // Act
        OptimizedJsonInfo info = new OptimizedJsonInfo();

        // Assert
        assertNotNull(info.classIndices, "classIndices should not be null");
        assertTrue(info.classIndices instanceof HashMap, "classIndices should be a HashMap");
        assertTrue(info.classIndices.isEmpty(), "classIndices should be empty after construction");
    }

    /**
     * Tests that the default constructor initializes jsonFieldIndices to an empty HashMap.
     */
    @Test
    public void testConstructorInitializesJsonFieldIndicesToEmptyHashMap() {
        // Act
        OptimizedJsonInfo info = new OptimizedJsonInfo();

        // Assert
        assertNotNull(info.jsonFieldIndices, "jsonFieldIndices should not be null");
        assertTrue(info.jsonFieldIndices instanceof HashMap, "jsonFieldIndices should be a HashMap");
        assertTrue(info.jsonFieldIndices.isEmpty(), "jsonFieldIndices should be empty after construction");
    }

    /**
     * Tests that the default constructor initializes classJsonInfos to an empty HashMap.
     */
    @Test
    public void testConstructorInitializesClassJsonInfosToEmptyHashMap() {
        // Act
        OptimizedJsonInfo info = new OptimizedJsonInfo();

        // Assert
        assertNotNull(info.classJsonInfos, "classJsonInfos should not be null");
        assertTrue(info.classJsonInfos instanceof HashMap, "classJsonInfos should be a HashMap");
        assertTrue(info.classJsonInfos.isEmpty(), "classJsonInfos should be empty after construction");
    }

    /**
     * Tests that the maps created by the constructor are mutable and can accept entries.
     */
    @Test
    public void testConstructorCreatesMutableMaps() {
        // Act
        OptimizedJsonInfo info = new OptimizedJsonInfo();

        // Assert - Verify all maps are mutable by adding entries
        assertDoesNotThrow(() -> {
            info.classIndices.put("TestClass", 0);
            info.jsonFieldIndices.put("testField", 0);
            info.classJsonInfos.put("TestClass", new OptimizedJsonInfo.ClassJsonInfo());
        }, "All maps should be mutable and accept entries");

        assertEquals(1, info.classIndices.size(), "classIndices should accept new entries");
        assertEquals(1, info.jsonFieldIndices.size(), "jsonFieldIndices should accept new entries");
        assertEquals(1, info.classJsonInfos.size(), "classJsonInfos should accept new entries");
    }

    /**
     * Tests that multiple OptimizedJsonInfo instances can be created independently.
     */
    @Test
    public void testMultipleInstancesCreation() {
        // Act
        OptimizedJsonInfo info1 = new OptimizedJsonInfo();
        OptimizedJsonInfo info2 = new OptimizedJsonInfo();

        // Assert
        assertNotNull(info1, "First OptimizedJsonInfo instance should not be null");
        assertNotNull(info2, "Second OptimizedJsonInfo instance should not be null");
        assertNotSame(info1, info2, "Each constructor call should create a distinct instance");
        assertNotSame(info1.classIndices, info2.classIndices, "Each instance should have its own classIndices map");
        assertNotSame(info1.jsonFieldIndices, info2.jsonFieldIndices, "Each instance should have its own jsonFieldIndices map");
        assertNotSame(info1.classJsonInfos, info2.classJsonInfos, "Each instance should have its own classJsonInfos map");
    }

    /**
     * Tests that modifications to one instance do not affect another instance.
     */
    @Test
    public void testInstancesAreIndependent() {
        // Act
        OptimizedJsonInfo info1 = new OptimizedJsonInfo();
        OptimizedJsonInfo info2 = new OptimizedJsonInfo();

        info1.classIndices.put("TestClass", 1);
        info1.jsonFieldIndices.put("testField", 2);

        // Assert
        assertTrue(info2.classIndices.isEmpty(), "Modifications to info1 should not affect info2 classIndices");
        assertTrue(info2.jsonFieldIndices.isEmpty(), "Modifications to info1 should not affect info2 jsonFieldIndices");
    }

    // Tests for assignIndices method: assignIndices.()V

    /**
     * Tests assignIndices with empty maps - should not throw any exceptions.
     */
    @Test
    public void testAssignIndicesWithEmptyMaps() {
        // Arrange
        OptimizedJsonInfo info = new OptimizedJsonInfo();

        // Act & Assert
        assertDoesNotThrow(() -> info.assignIndices(), "assignIndices should handle empty maps without throwing");
        assertTrue(info.classIndices.isEmpty(), "classIndices should remain empty");
        assertTrue(info.jsonFieldIndices.isEmpty(), "jsonFieldIndices should remain empty");
    }

    /**
     * Tests assignIndices with a single class entry.
     */
    @Test
    public void testAssignIndicesWithSingleClassEntry() {
        // Arrange
        optimizedJsonInfo.classIndices.put("com.example.TestClass", null);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert
        assertEquals(0, optimizedJsonInfo.classIndices.get("com.example.TestClass"),
                "Single class should be assigned index 0");
    }

    /**
     * Tests assignIndices with a single field entry.
     */
    @Test
    public void testAssignIndicesWithSingleFieldEntry() {
        // Arrange
        optimizedJsonInfo.jsonFieldIndices.put("fieldName", null);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert
        assertEquals(0, optimizedJsonInfo.jsonFieldIndices.get("fieldName"),
                "Single field should be assigned index 0");
    }

    /**
     * Tests assignIndices with multiple class entries assigns contiguous indices starting from 0.
     */
    @Test
    public void testAssignIndicesWithMultipleClasses() {
        // Arrange
        optimizedJsonInfo.classIndices.put("com.example.ClassA", null);
        optimizedJsonInfo.classIndices.put("com.example.ClassB", null);
        optimizedJsonInfo.classIndices.put("com.example.ClassC", null);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert
        assertEquals(3, optimizedJsonInfo.classIndices.size(), "Should have 3 class entries");

        // Verify all values are between 0 and 2 (contiguous)
        Map<Integer, String> indexToClass = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.classIndices.entrySet()) {
            Integer index = entry.getValue();
            assertNotNull(index, "Index should not be null");
            assertTrue(index >= 0 && index <= 2, "Index should be between 0 and 2");
            assertNull(indexToClass.put(index, entry.getKey()),
                    "Each index should be unique (no duplicate indices)");
        }

        // Verify we have indices 0, 1, and 2 (contiguous)
        assertTrue(indexToClass.containsKey(0), "Should have index 0");
        assertTrue(indexToClass.containsKey(1), "Should have index 1");
        assertTrue(indexToClass.containsKey(2), "Should have index 2");
    }

    /**
     * Tests assignIndices with multiple field entries assigns contiguous indices starting from 0.
     */
    @Test
    public void testAssignIndicesWithMultipleFields() {
        // Arrange
        optimizedJsonInfo.jsonFieldIndices.put("fieldA", null);
        optimizedJsonInfo.jsonFieldIndices.put("fieldB", null);
        optimizedJsonInfo.jsonFieldIndices.put("fieldC", null);
        optimizedJsonInfo.jsonFieldIndices.put("fieldD", null);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert
        assertEquals(4, optimizedJsonInfo.jsonFieldIndices.size(), "Should have 4 field entries");

        // Verify all values are between 0 and 3 (contiguous)
        Map<Integer, String> indexToField = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.jsonFieldIndices.entrySet()) {
            Integer index = entry.getValue();
            assertNotNull(index, "Index should not be null");
            assertTrue(index >= 0 && index <= 3, "Index should be between 0 and 3");
            assertNull(indexToField.put(index, entry.getKey()),
                    "Each index should be unique (no duplicate indices)");
        }

        // Verify we have indices 0, 1, 2, and 3 (contiguous)
        assertTrue(indexToField.containsKey(0), "Should have index 0");
        assertTrue(indexToField.containsKey(1), "Should have index 1");
        assertTrue(indexToField.containsKey(2), "Should have index 2");
        assertTrue(indexToField.containsKey(3), "Should have index 3");
    }

    /**
     * Tests assignIndices with both classes and fields populated.
     */
    @Test
    public void testAssignIndicesWithBothClassesAndFields() {
        // Arrange
        optimizedJsonInfo.classIndices.put("com.example.ClassA", null);
        optimizedJsonInfo.classIndices.put("com.example.ClassB", null);
        optimizedJsonInfo.jsonFieldIndices.put("fieldX", null);
        optimizedJsonInfo.jsonFieldIndices.put("fieldY", null);
        optimizedJsonInfo.jsonFieldIndices.put("fieldZ", null);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert - Classes should have indices 0-1
        assertEquals(2, optimizedJsonInfo.classIndices.size(), "Should have 2 class entries");
        Map<Integer, String> classIndexToName = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.classIndices.entrySet()) {
            Integer index = entry.getValue();
            assertTrue(index >= 0 && index <= 1, "Class index should be between 0 and 1");
            classIndexToName.put(index, entry.getKey());
        }
        assertEquals(2, classIndexToName.size(), "Should have 2 unique class indices");

        // Assert - Fields should have indices 0-2
        assertEquals(3, optimizedJsonInfo.jsonFieldIndices.size(), "Should have 3 field entries");
        Map<Integer, String> fieldIndexToName = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.jsonFieldIndices.entrySet()) {
            Integer index = entry.getValue();
            assertTrue(index >= 0 && index <= 2, "Field index should be between 0 and 2");
            fieldIndexToName.put(index, entry.getKey());
        }
        assertEquals(3, fieldIndexToName.size(), "Should have 3 unique field indices");
    }

    /**
     * Tests assignIndices overwrites existing non-null index values.
     */
    @Test
    public void testAssignIndicesOverwritesExistingIndices() {
        // Arrange
        optimizedJsonInfo.classIndices.put("ClassA", 100);
        optimizedJsonInfo.classIndices.put("ClassB", 200);
        optimizedJsonInfo.jsonFieldIndices.put("fieldA", 500);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert - Old values should be replaced with contiguous indices starting from 0
        Map<Integer, String> classIndices = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.classIndices.entrySet()) {
            Integer index = entry.getValue();
            assertTrue(index >= 0 && index <= 1, "Class index should be between 0 and 1, not the old value");
            classIndices.put(index, entry.getKey());
        }
        assertEquals(2, classIndices.size(), "Should have 2 unique class indices");

        Integer fieldIndex = optimizedJsonInfo.jsonFieldIndices.get("fieldA");
        assertEquals(0, fieldIndex, "Field should be assigned index 0, not the old value 500");
    }

    /**
     * Tests calling assignIndices multiple times (idempotency test).
     * Multiple calls should reassign the same contiguous indices.
     */
    @Test
    public void testAssignIndicesMultipleCalls() {
        // Arrange
        optimizedJsonInfo.classIndices.put("ClassA", null);
        optimizedJsonInfo.classIndices.put("ClassB", null);
        optimizedJsonInfo.classIndices.put("ClassC", null);

        // Act - Call assignIndices twice
        optimizedJsonInfo.assignIndices();
        Map<String, Integer> firstAssignment = new HashMap<>(optimizedJsonInfo.classIndices);

        optimizedJsonInfo.assignIndices();
        Map<String, Integer> secondAssignment = new HashMap<>(optimizedJsonInfo.classIndices);

        // Assert - Second call should produce the same assignment
        assertEquals(firstAssignment, secondAssignment,
                "Multiple calls to assignIndices should produce the same index assignments");
    }

    /**
     * Tests assignIndices with entries that have existing indices set to 0.
     */
    @Test
    public void testAssignIndicesWithExistingZeroIndices() {
        // Arrange
        optimizedJsonInfo.classIndices.put("ClassA", 0);
        optimizedJsonInfo.classIndices.put("ClassB", 0);
        optimizedJsonInfo.classIndices.put("ClassC", 0);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert - Should still assign proper contiguous indices
        Map<Integer, String> indexToClass = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.classIndices.entrySet()) {
            indexToClass.put(entry.getValue(), entry.getKey());
        }

        assertEquals(3, indexToClass.size(), "Should have 3 unique indices even though they all started at 0");
        assertTrue(indexToClass.containsKey(0), "Should have index 0");
        assertTrue(indexToClass.containsKey(1), "Should have index 1");
        assertTrue(indexToClass.containsKey(2), "Should have index 2");
    }

    /**
     * Tests that assignIndices does not affect classJsonInfos map.
     */
    @Test
    public void testAssignIndicesDoesNotAffectClassJsonInfos() {
        // Arrange
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();
        classInfo.javaToJsonFieldNames.put("field1", new String[]{"jsonField1"});
        classInfo.exposedJavaFieldNames.add("field1");

        optimizedJsonInfo.classJsonInfos.put("TestClass", classInfo);
        optimizedJsonInfo.classIndices.put("ClassA", null);
        optimizedJsonInfo.jsonFieldIndices.put("fieldA", null);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert
        assertEquals(1, optimizedJsonInfo.classJsonInfos.size(), "classJsonInfos should not be modified");
        assertSame(classInfo, optimizedJsonInfo.classJsonInfos.get("TestClass"),
                "ClassJsonInfo instance should be unchanged");
        assertEquals(1, classInfo.javaToJsonFieldNames.size(),
                "javaToJsonFieldNames should be unchanged");
        assertEquals(1, classInfo.exposedJavaFieldNames.size(),
                "exposedJavaFieldNames should be unchanged");
    }

    /**
     * Tests assignIndices with a large number of entries to verify scalability.
     */
    @Test
    public void testAssignIndicesWithLargeNumberOfEntries() {
        // Arrange
        int numClasses = 1000;
        int numFields = 5000;

        for (int i = 0; i < numClasses; i++) {
            optimizedJsonInfo.classIndices.put("Class" + i, null);
        }
        for (int i = 0; i < numFields; i++) {
            optimizedJsonInfo.jsonFieldIndices.put("field" + i, null);
        }

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert
        // Verify all class indices are unique and contiguous from 0 to numClasses-1
        Map<Integer, String> classIndexMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.classIndices.entrySet()) {
            Integer index = entry.getValue();
            assertTrue(index >= 0 && index < numClasses, "Class index should be in valid range");
            classIndexMap.put(index, entry.getKey());
        }
        assertEquals(numClasses, classIndexMap.size(), "Should have all unique class indices");

        // Verify all field indices are unique and contiguous from 0 to numFields-1
        Map<Integer, String> fieldIndexMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optimizedJsonInfo.jsonFieldIndices.entrySet()) {
            Integer index = entry.getValue();
            assertTrue(index >= 0 && index < numFields, "Field index should be in valid range");
            fieldIndexMap.put(index, entry.getKey());
        }
        assertEquals(numFields, fieldIndexMap.size(), "Should have all unique field indices");
    }

    /**
     * Tests assignIndices preserves all keys in the maps.
     */
    @Test
    public void testAssignIndicesPreservesAllKeys() {
        // Arrange
        optimizedJsonInfo.classIndices.put("com.example.Alpha", null);
        optimizedJsonInfo.classIndices.put("com.example.Beta", null);
        optimizedJsonInfo.classIndices.put("com.example.Gamma", null);
        optimizedJsonInfo.jsonFieldIndices.put("userName", null);
        optimizedJsonInfo.jsonFieldIndices.put("userId", null);

        // Act
        optimizedJsonInfo.assignIndices();

        // Assert - All keys should still be present
        assertTrue(optimizedJsonInfo.classIndices.containsKey("com.example.Alpha"),
                "Alpha class key should be preserved");
        assertTrue(optimizedJsonInfo.classIndices.containsKey("com.example.Beta"),
                "Beta class key should be preserved");
        assertTrue(optimizedJsonInfo.classIndices.containsKey("com.example.Gamma"),
                "Gamma class key should be preserved");
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("userName"),
                "userName field key should be preserved");
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("userId"),
                "userId field key should be preserved");
    }

    // Tests for ClassJsonInfo constructor: <init>.()V

    /**
     * Tests that the ClassJsonInfo default constructor creates a non-null instance.
     */
    @Test
    public void testClassJsonInfoConstructorCreatesNonNullInstance() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();

        // Assert
        assertNotNull(classInfo, "ClassJsonInfo instance should not be null");
    }

    /**
     * Tests that the ClassJsonInfo constructor initializes javaToJsonFieldNames to an empty HashMap.
     */
    @Test
    public void testClassJsonInfoConstructorInitializesJavaToJsonFieldNamesToEmptyHashMap() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();

        // Assert
        assertNotNull(classInfo.javaToJsonFieldNames, "javaToJsonFieldNames should not be null");
        assertTrue(classInfo.javaToJsonFieldNames instanceof HashMap, "javaToJsonFieldNames should be a HashMap");
        assertTrue(classInfo.javaToJsonFieldNames.isEmpty(), "javaToJsonFieldNames should be empty after construction");
    }

    /**
     * Tests that the ClassJsonInfo constructor initializes exposedJavaFieldNames to an empty HashSet.
     */
    @Test
    public void testClassJsonInfoConstructorInitializesExposedJavaFieldNamesToEmptyHashSet() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();

        // Assert
        assertNotNull(classInfo.exposedJavaFieldNames, "exposedJavaFieldNames should not be null");
        assertTrue(classInfo.exposedJavaFieldNames.isEmpty(), "exposedJavaFieldNames should be empty after construction");
    }

    /**
     * Tests that the maps and sets created by the ClassJsonInfo constructor are mutable.
     */
    @Test
    public void testClassJsonInfoConstructorCreatesMutableCollections() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();

        // Assert - Verify collections are mutable by adding entries
        assertDoesNotThrow(() -> {
            classInfo.javaToJsonFieldNames.put("javaField", new String[]{"jsonField"});
            classInfo.exposedJavaFieldNames.add("javaField");
        }, "Collections should be mutable and accept entries");

        assertEquals(1, classInfo.javaToJsonFieldNames.size(), "javaToJsonFieldNames should accept new entries");
        assertEquals(1, classInfo.exposedJavaFieldNames.size(), "exposedJavaFieldNames should accept new entries");
    }

    /**
     * Tests that multiple ClassJsonInfo instances can be created independently.
     */
    @Test
    public void testClassJsonInfoMultipleInstancesCreation() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo info1 = new OptimizedJsonInfo.ClassJsonInfo();
        OptimizedJsonInfo.ClassJsonInfo info2 = new OptimizedJsonInfo.ClassJsonInfo();

        // Assert
        assertNotNull(info1, "First ClassJsonInfo instance should not be null");
        assertNotNull(info2, "Second ClassJsonInfo instance should not be null");
        assertNotSame(info1, info2, "Each constructor call should create a distinct instance");
        assertNotSame(info1.javaToJsonFieldNames, info2.javaToJsonFieldNames,
                "Each instance should have its own javaToJsonFieldNames map");
        assertNotSame(info1.exposedJavaFieldNames, info2.exposedJavaFieldNames,
                "Each instance should have its own exposedJavaFieldNames set");
    }

    /**
     * Tests that modifications to one ClassJsonInfo instance do not affect another instance.
     */
    @Test
    public void testClassJsonInfoInstancesAreIndependent() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo info1 = new OptimizedJsonInfo.ClassJsonInfo();
        OptimizedJsonInfo.ClassJsonInfo info2 = new OptimizedJsonInfo.ClassJsonInfo();

        info1.javaToJsonFieldNames.put("field1", new String[]{"json1"});
        info1.exposedJavaFieldNames.add("field1");

        // Assert
        assertTrue(info2.javaToJsonFieldNames.isEmpty(),
                "Modifications to info1 should not affect info2 javaToJsonFieldNames");
        assertTrue(info2.exposedJavaFieldNames.isEmpty(),
                "Modifications to info1 should not affect info2 exposedJavaFieldNames");
    }

    /**
     * Tests that the ClassJsonInfo constructor can be called multiple times in succession.
     */
    @Test
    public void testClassJsonInfoMultipleSuccessiveConstructorCalls() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();
            assertNotNull(classInfo, "ClassJsonInfo instance " + i + " should not be null");
            assertNotNull(classInfo.javaToJsonFieldNames,
                    "javaToJsonFieldNames should be initialized for instance " + i);
            assertNotNull(classInfo.exposedJavaFieldNames,
                    "exposedJavaFieldNames should be initialized for instance " + i);
            assertTrue(classInfo.javaToJsonFieldNames.isEmpty(),
                    "javaToJsonFieldNames should be empty for instance " + i);
            assertTrue(classInfo.exposedJavaFieldNames.isEmpty(),
                    "exposedJavaFieldNames should be empty for instance " + i);
        }
    }

    /**
     * Tests that ClassJsonInfo fields can store various types of data after construction.
     */
    @Test
    public void testClassJsonInfoCanStoreVariousDataTypes() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();

        // Populate with various data
        classInfo.javaToJsonFieldNames.put("singleName", new String[]{"jsonName"});
        classInfo.javaToJsonFieldNames.put("multipleName", new String[]{"jsonName1", "jsonName2", "jsonName3"});
        classInfo.javaToJsonFieldNames.put("emptyArray", new String[]{});
        classInfo.exposedJavaFieldNames.add("field1");
        classInfo.exposedJavaFieldNames.add("field2");
        classInfo.exposedJavaFieldNames.add("field3");

        // Assert
        assertEquals(3, classInfo.javaToJsonFieldNames.size(),
                "javaToJsonFieldNames should contain 3 entries");
        assertEquals(3, classInfo.exposedJavaFieldNames.size(),
                "exposedJavaFieldNames should contain 3 entries");

        assertArrayEquals(new String[]{"jsonName"}, classInfo.javaToJsonFieldNames.get("singleName"),
                "Single name array should be stored correctly");
        assertArrayEquals(new String[]{"jsonName1", "jsonName2", "jsonName3"},
                classInfo.javaToJsonFieldNames.get("multipleName"),
                "Multiple name array should be stored correctly");
        assertArrayEquals(new String[]{}, classInfo.javaToJsonFieldNames.get("emptyArray"),
                "Empty array should be stored correctly");

        assertTrue(classInfo.exposedJavaFieldNames.contains("field1"), "Should contain field1");
        assertTrue(classInfo.exposedJavaFieldNames.contains("field2"), "Should contain field2");
        assertTrue(classInfo.exposedJavaFieldNames.contains("field3"), "Should contain field3");
    }

    /**
     * Tests that ClassJsonInfo exposedJavaFieldNames behaves as a Set (no duplicates).
     */
    @Test
    public void testClassJsonInfoExposedFieldNamesSetBehavior() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();

        classInfo.exposedJavaFieldNames.add("field1");
        classInfo.exposedJavaFieldNames.add("field2");
        classInfo.exposedJavaFieldNames.add("field1"); // Duplicate

        // Assert
        assertEquals(2, classInfo.exposedJavaFieldNames.size(),
                "Set should only contain unique entries (no duplicates)");
        assertTrue(classInfo.exposedJavaFieldNames.contains("field1"), "Should contain field1");
        assertTrue(classInfo.exposedJavaFieldNames.contains("field2"), "Should contain field2");
    }

    /**
     * Tests that ClassJsonInfo javaToJsonFieldNames map can overwrite values.
     */
    @Test
    public void testClassJsonInfoJavaToJsonFieldNamesCanOverwriteValues() {
        // Act
        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();

        classInfo.javaToJsonFieldNames.put("field1", new String[]{"oldName"});
        classInfo.javaToJsonFieldNames.put("field1", new String[]{"newName"}); // Overwrite

        // Assert
        assertEquals(1, classInfo.javaToJsonFieldNames.size(),
                "Map should only contain one entry for field1");
        assertArrayEquals(new String[]{"newName"}, classInfo.javaToJsonFieldNames.get("field1"),
                "Value should be overwritten with new array");
    }
}
