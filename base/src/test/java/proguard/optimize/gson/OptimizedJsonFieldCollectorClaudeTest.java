package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.constant.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OptimizedJsonFieldCollector}.
 * Tests the constructor and all visitor methods to ensure proper collection of JSON field information.
 *
 * The OptimizedJsonFieldCollector visits classes and fields to collect information about
 * which fields are involved in JSON (de)serialization and register their Java to JSON field
 * name mappings in an OptimizedJsonInfo object.
 */
public class OptimizedJsonFieldCollectorClaudeTest {

    private OptimizedJsonInfo optimizedJsonInfo;
    private OptimizedJsonFieldCollector collector;

    /**
     * Sets up fresh instances before each test.
     */
    @BeforeEach
    public void setUp() {
        optimizedJsonInfo = new OptimizedJsonInfo();
    }

    // =========================================================================
    // Tests for constructor: <init>.(Lproguard/optimize/gson/OptimizedJsonInfo;Lproguard/optimize/gson/OptimizedJsonFieldCollector$Mode;)V
    // =========================================================================

    /**
     * Tests that the constructor successfully creates an instance with valid parameters for serialize mode.
     */
    @Test
    public void testConstructor_withSerializeMode_createsInstance() {
        // Act
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);

        // Assert
        assertNotNull(collector, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor successfully creates an instance with valid parameters for deserialize mode.
     */
    @Test
    public void testConstructor_withDeserializeMode_createsInstance() {
        // Act
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.deserialize);

        // Assert
        assertNotNull(collector, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Act
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);

        // Assert
        assertTrue(collector instanceof proguard.classfile.visitor.ClassVisitor,
                "OptimizedJsonFieldCollector should implement ClassVisitor");
    }

    /**
     * Tests that the constructor creates an instance that implements MemberVisitor.
     */
    @Test
    public void testConstructor_implementsMemberVisitor() {
        // Act
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);

        // Assert
        assertTrue(collector instanceof proguard.classfile.visitor.MemberVisitor,
                "OptimizedJsonFieldCollector should implement MemberVisitor");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that multiple instances can be created independently with different modes.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnMode() {
        // Act
        OptimizedJsonFieldCollector collector1 = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        OptimizedJsonFieldCollector collector2 = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.deserialize);
        OptimizedJsonFieldCollector collector3 = new OptimizedJsonFieldCollector(new OptimizedJsonInfo(), OptimizedJsonFieldCollector.Mode.serialize);

        // Assert
        assertNotNull(collector1, "First instance should be created");
        assertNotNull(collector2, "Second instance should be created");
        assertNotNull(collector3, "Third instance should be created");
        assertNotSame(collector1, collector2, "Instances should be distinct");
        assertNotSame(collector2, collector3, "Instances should be distinct");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            OptimizedJsonFieldCollector newCollector = new OptimizedJsonFieldCollector(
                    new OptimizedJsonInfo(),
                    i % 2 == 0 ? OptimizedJsonFieldCollector.Mode.serialize : OptimizedJsonFieldCollector.Mode.deserialize);
            assertNotNull(newCollector, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests constructor with null OptimizedJsonInfo parameter.
     * This should not throw during construction, but may throw during usage.
     */
    @Test
    public void testConstructor_withNullOptimizedJsonInfo_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldCollector(null, OptimizedJsonFieldCollector.Mode.serialize),
                "Constructor should accept null OptimizedJsonInfo without throwing");
    }

    /**
     * Tests constructor with null Mode parameter.
     * This should not throw during construction, but may throw during usage.
     */
    @Test
    public void testConstructor_withNullMode_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldCollector(optimizedJsonInfo, null),
                "Constructor should accept null Mode without throwing");
    }

    /**
     * Tests constructor with both parameters null.
     */
    @Test
    public void testConstructor_withBothParametersNull_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldCollector(null, null),
                "Constructor should accept null parameters without throwing");
    }

    /**
     * Tests that multiple collectors can share the same OptimizedJsonInfo instance.
     */
    @Test
    public void testConstructor_sharedOptimizedJsonInfo_collectorsShareState() {
        // Arrange
        OptimizedJsonInfo sharedInfo = new OptimizedJsonInfo();

        // Act
        OptimizedJsonFieldCollector collector1 = new OptimizedJsonFieldCollector(sharedInfo, OptimizedJsonFieldCollector.Mode.serialize);
        OptimizedJsonFieldCollector collector2 = new OptimizedJsonFieldCollector(sharedInfo, OptimizedJsonFieldCollector.Mode.deserialize);

        // Assert
        assertNotNull(collector1);
        assertNotNull(collector2);
        assertNotSame(collector1, collector2, "Collectors should be different instances");
    }

    // =========================================================================
    // Tests for visitAnyClass.(Lproguard/classfile/Clazz;)V
    // =========================================================================

    /**
     * Tests that visitAnyClass does nothing when called with a valid Clazz.
     * The method is a no-op implementation of the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_withValidClazz_doesNothing() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        int initialClassCount = optimizedJsonInfo.classJsonInfos.size();

        // Act
        collector.visitAnyClass(clazz);

        // Assert
        assertEquals(initialClassCount, optimizedJsonInfo.classJsonInfos.size(),
                "visitAnyClass should not modify OptimizedJsonInfo");
    }

    /**
     * Tests that visitAnyClass can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyClass_multipleCalls_noSideEffects() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass clazz1 = createMinimalProgramClass("Class1");
        ProgramClass clazz2 = createMinimalProgramClass("Class2");
        int initialClassCount = optimizedJsonInfo.classJsonInfos.size();

        // Act
        collector.visitAnyClass(clazz1);
        collector.visitAnyClass(clazz2);
        collector.visitAnyClass(clazz1);

        // Assert
        assertEquals(initialClassCount, optimizedJsonInfo.classJsonInfos.size(),
                "Multiple calls to visitAnyClass should not modify OptimizedJsonInfo");
    }

    /**
     * Tests that visitAnyClass does not throw exceptions with a null argument.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrow() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);

        // Act & Assert
        assertDoesNotThrow(() -> collector.visitAnyClass(null),
                "visitAnyClass should not throw exception with null argument");
    }

    /**
     * Tests that visitAnyClass is truly a no-op by verifying no state changes.
     */
    @Test
    public void testVisitAnyClass_isNoOp() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass clazz = createMinimalProgramClass("TestClass");

        // Act
        for (int i = 0; i < 10; i++) {
            collector.visitAnyClass(clazz);
        }

        // Assert
        assertEquals(0, optimizedJsonInfo.classJsonInfos.size(),
                "visitAnyClass should remain a no-op regardless of call count");
    }

    // =========================================================================
    // Tests for visitProgramClass.(Lproguard/classfile/ProgramClass;)V
    // =========================================================================

    /**
     * Tests that visitProgramClass with null throws NullPointerException.
     */
    @Test
    public void testVisitProgramClass_withNull_throwsNullPointerException() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> collector.visitProgramClass(null),
                "visitProgramClass should throw NullPointerException for null input");
    }

    /**
     * Tests that visitProgramClass creates a ClassJsonInfo for the class.
     */
    @Test
    public void testVisitProgramClass_createsClassJsonInfo() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("com/example/TestClass");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("com/example/TestClass"),
                "visitProgramClass should create ClassJsonInfo for the class");
        assertNotNull(optimizedJsonInfo.classJsonInfos.get("com/example/TestClass"),
                "ClassJsonInfo should not be null");
    }

    /**
     * Tests that visitProgramClass registers the class in classIndices.
     */
    @Test
    public void testVisitProgramClass_registersClassInIndices() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("com/example/TestClass");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        assertTrue(optimizedJsonInfo.classIndices.containsKey("com/example/TestClass"),
                "visitProgramClass should register the class in classIndices");
    }

    /**
     * Tests that visitProgramClass works with serialize mode.
     */
    @Test
    public void testVisitProgramClass_withSerializeMode_executesCorrectly() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("TestClass"));
        assertTrue(optimizedJsonInfo.classIndices.containsKey("TestClass"));
    }

    /**
     * Tests that visitProgramClass works with deserialize mode.
     */
    @Test
    public void testVisitProgramClass_withDeserializeMode_executesCorrectly() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.deserialize);
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("TestClass"));
        assertTrue(optimizedJsonInfo.classIndices.containsKey("TestClass"));
    }

    /**
     * Tests that visitProgramClass can be called multiple times on different classes.
     */
    @Test
    public void testVisitProgramClass_multipleCalls_eachClassRegistered() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass class1 = createMinimalProgramClass("Class1");
        ProgramClass class2 = createMinimalProgramClass("Class2");
        ProgramClass class3 = createMinimalProgramClass("Class3");

        // Act
        collector.visitProgramClass(class1);
        collector.visitProgramClass(class2);
        collector.visitProgramClass(class3);

        // Assert
        assertEquals(3, optimizedJsonInfo.classJsonInfos.size(), "All three classes should be registered");
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("Class1"));
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("Class2"));
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("Class3"));
    }

    /**
     * Tests that visitProgramClass replaces existing ClassJsonInfo for the same class.
     */
    @Test
    public void testVisitProgramClass_calledTwiceOnSameClass_replacesClassJsonInfo() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        collector.visitProgramClass(programClass);
        OptimizedJsonInfo.ClassJsonInfo firstInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");

        collector.visitProgramClass(programClass);
        OptimizedJsonInfo.ClassJsonInfo secondInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");

        // Assert
        assertNotSame(firstInfo, secondInfo, "Second visit should create a new ClassJsonInfo instance");
        assertEquals(1, optimizedJsonInfo.classJsonInfos.size(), "Should still have only one class registered");
    }

    /**
     * Tests that visitProgramClass works with simple class names.
     */
    @Test
    public void testVisitProgramClass_withSimpleClassName_executesCorrectly() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("Simple");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("Simple"));
    }

    /**
     * Tests that visitProgramClass works with fully qualified class names.
     */
    @Test
    public void testVisitProgramClass_withQualifiedClassName_executesCorrectly() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("com/example/model/User");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("com/example/model/User"));
    }

    /**
     * Tests that ClassJsonInfo has the correct initial state.
     */
    @Test
    public void testVisitProgramClass_classJsonInfo_hasCorrectInitialState() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");
        assertNotNull(classJsonInfo);
        assertNotNull(classJsonInfo.javaToJsonFieldNames, "javaToJsonFieldNames should be initialized");
        assertNotNull(classJsonInfo.exposedJavaFieldNames, "exposedJavaFieldNames should be initialized");
        assertEquals(0, classJsonInfo.javaToJsonFieldNames.size(), "javaToJsonFieldNames should be empty initially");
        assertEquals(0, classJsonInfo.exposedJavaFieldNames.size(), "exposedJavaFieldNames should be empty initially");
    }

    /**
     * Tests that classIndices value is initially null (to be assigned later).
     */
    @Test
    public void testVisitProgramClass_classIndices_valueIsNull() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        collector.visitProgramClass(programClass);

        // Assert
        assertTrue(optimizedJsonInfo.classIndices.containsKey("TestClass"));
        assertNull(optimizedJsonInfo.classIndices.get("TestClass"),
                "classIndices value should be null initially (assigned later)");
    }

    // =========================================================================
    // Tests for visitLibraryClass.(Lproguard/classfile/LibraryClass;)V
    // =========================================================================

    /**
     * Tests that visitLibraryClass does nothing when called with a valid LibraryClass.
     * The method is a no-op implementation of the ClassVisitor interface.
     */
    @Test
    public void testVisitLibraryClass_withValidLibraryClass_doesNothing() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        LibraryClass libraryClass = new LibraryClass();
        int initialClassCount = optimizedJsonInfo.classJsonInfos.size();

        // Act
        collector.visitLibraryClass(libraryClass);

        // Assert
        assertEquals(initialClassCount, optimizedJsonInfo.classJsonInfos.size(),
                "visitLibraryClass should not modify OptimizedJsonInfo");
    }

    /**
     * Tests that visitLibraryClass can be called multiple times without side effects.
     */
    @Test
    public void testVisitLibraryClass_multipleCalls_noSideEffects() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();
        int initialClassCount = optimizedJsonInfo.classJsonInfos.size();

        // Act
        collector.visitLibraryClass(libraryClass1);
        collector.visitLibraryClass(libraryClass2);
        collector.visitLibraryClass(libraryClass1);

        // Assert
        assertEquals(initialClassCount, optimizedJsonInfo.classJsonInfos.size(),
                "Multiple calls to visitLibraryClass should not modify OptimizedJsonInfo");
    }

    /**
     * Tests that visitLibraryClass does not throw exceptions with a null argument.
     */
    @Test
    public void testVisitLibraryClass_withNullLibraryClass_doesNotThrow() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);

        // Act & Assert
        assertDoesNotThrow(() -> collector.visitLibraryClass(null),
                "visitLibraryClass should not throw exception with null argument");
    }

    /**
     * Tests that visitLibraryClass is truly a no-op.
     */
    @Test
    public void testVisitLibraryClass_isNoOp() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        LibraryClass libraryClass = new LibraryClass();

        // Act
        for (int i = 0; i < 10; i++) {
            collector.visitLibraryClass(libraryClass);
        }

        // Assert
        assertEquals(0, optimizedJsonInfo.classJsonInfos.size(),
                "visitLibraryClass should remain a no-op regardless of call count");
    }

    // =========================================================================
    // Tests for visitAnyMember.(Lproguard/classfile/Clazz;Lproguard/classfile/Member;)V
    // =========================================================================

    /**
     * Tests that visitAnyMember does nothing when called with valid parameters.
     * The method is a no-op implementation of the MemberVisitor interface.
     */
    @Test
    public void testVisitAnyMember_withValidParameters_doesNothing() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramField field = createSimpleField("testField");
        int initialFieldCount = optimizedJsonInfo.jsonFieldIndices.size();

        // Act
        collector.visitAnyMember(clazz, field);

        // Assert
        assertEquals(initialFieldCount, optimizedJsonInfo.jsonFieldIndices.size(),
                "visitAnyMember should not modify OptimizedJsonInfo");
    }

    /**
     * Tests that visitAnyMember can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyMember_multipleCalls_noSideEffects() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramField field1 = createSimpleField("field1");
        ProgramField field2 = createSimpleField("field2");
        int initialFieldCount = optimizedJsonInfo.jsonFieldIndices.size();

        // Act
        collector.visitAnyMember(clazz, field1);
        collector.visitAnyMember(clazz, field2);
        collector.visitAnyMember(clazz, field1);

        // Assert
        assertEquals(initialFieldCount, optimizedJsonInfo.jsonFieldIndices.size(),
                "Multiple calls to visitAnyMember should not modify OptimizedJsonInfo");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with null Clazz.
     */
    @Test
    public void testVisitAnyMember_withNullClazz_doesNotThrow() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramField field = createSimpleField("testField");

        // Act & Assert
        assertDoesNotThrow(() -> collector.visitAnyMember(null, field),
                "visitAnyMember should not throw exception with null Clazz");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with null Member.
     */
    @Test
    public void testVisitAnyMember_withNullMember_doesNotThrow() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass clazz = createMinimalProgramClass("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> collector.visitAnyMember(clazz, null),
                "visitAnyMember should not throw exception with null Member");
    }

    /**
     * Tests that visitAnyMember does not throw exceptions with both parameters null.
     */
    @Test
    public void testVisitAnyMember_withBothParametersNull_doesNotThrow() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);

        // Act & Assert
        assertDoesNotThrow(() -> collector.visitAnyMember(null, null),
                "visitAnyMember should not throw exception with null parameters");
    }

    /**
     * Tests that visitAnyMember is truly a no-op.
     */
    @Test
    public void testVisitAnyMember_isNoOp() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramField field = createSimpleField("testField");

        // Act
        for (int i = 0; i < 10; i++) {
            collector.visitAnyMember(clazz, field);
        }

        // Assert
        assertEquals(0, optimizedJsonInfo.jsonFieldIndices.size(),
                "visitAnyMember should remain a no-op regardless of call count");
    }

    // =========================================================================
    // Tests for visitProgramField.(Lproguard/classfile/ProgramClass;Lproguard/classfile/ProgramField;)V
    // =========================================================================

    /**
     * Tests that visitProgramField throws NullPointerException with null ProgramClass.
     */
    @Test
    public void testVisitProgramField_withNullProgramClass_throwsNullPointerException() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramField field = createSimpleField("testField");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> collector.visitProgramField(null, field),
                "visitProgramField should throw NullPointerException for null ProgramClass");
    }

    /**
     * Tests that visitProgramField throws NullPointerException with null ProgramField.
     */
    @Test
    public void testVisitProgramField_withNullProgramField_throwsNullPointerException() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithField("TestClass", "field1");
        collector.visitProgramClass(programClass); // Initialize ClassJsonInfo

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> collector.visitProgramField(programClass, null),
                "visitProgramField should throw NullPointerException for null ProgramField");
    }

    /**
     * Tests that visitProgramField registers a simple field without annotations.
     */
    @Test
    public void testVisitProgramField_withSimpleField_registersField() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithField("TestClass", "myField");
        collector.visitProgramClass(programClass); // Initialize ClassJsonInfo

        // Act
        collector.visitProgramField(programClass, programClass.fields[0]);

        // Assert
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");
        assertTrue(classJsonInfo.javaToJsonFieldNames.containsKey("myField"),
                "Field should be registered in javaToJsonFieldNames");
        assertArrayEquals(new String[]{"myField"}, classJsonInfo.javaToJsonFieldNames.get("myField"),
                "Field should map to itself by default");
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("myField"),
                "Field should be registered in jsonFieldIndices");
    }

    /**
     * Tests that visitProgramField works in serialize mode.
     */
    @Test
    public void testVisitProgramField_withSerializeMode_registersField() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");
        collector.visitProgramClass(programClass);

        // Act
        collector.visitProgramField(programClass, programClass.fields[0]);

        // Assert
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");
        assertTrue(classJsonInfo.javaToJsonFieldNames.containsKey("testField"));
    }

    /**
     * Tests that visitProgramField works in deserialize mode.
     */
    @Test
    public void testVisitProgramField_withDeserializeMode_registersField() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.deserialize);
        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");
        collector.visitProgramClass(programClass);

        // Act
        collector.visitProgramField(programClass, programClass.fields[0]);

        // Assert
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");
        assertTrue(classJsonInfo.javaToJsonFieldNames.containsKey("testField"));
    }

    /**
     * Tests that visitProgramField can process multiple fields in the same class.
     */
    @Test
    public void testVisitProgramField_multipleFields_allRegistered() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithMultipleFields("TestClass",
                new String[]{"field1", "field2", "field3"});
        collector.visitProgramClass(programClass);

        // Act
        for (ProgramField field : programClass.fields) {
            collector.visitProgramField(programClass, field);
        }

        // Assert
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");
        assertEquals(3, classJsonInfo.javaToJsonFieldNames.size(), "All three fields should be registered");
        assertTrue(classJsonInfo.javaToJsonFieldNames.containsKey("field1"));
        assertTrue(classJsonInfo.javaToJsonFieldNames.containsKey("field2"));
        assertTrue(classJsonInfo.javaToJsonFieldNames.containsKey("field3"));
    }

    /**
     * Tests that visitProgramField does not register the same field twice if called again.
     * Second call should not overwrite the first registration.
     */
    @Test
    public void testVisitProgramField_calledTwice_doesNotDuplicate() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithField("TestClass", "myField");
        collector.visitProgramClass(programClass);
        ProgramField field = programClass.fields[0];

        // Act
        collector.visitProgramField(programClass, field);
        String[] firstMapping = optimizedJsonInfo.classJsonInfos.get("TestClass").javaToJsonFieldNames.get("myField");

        collector.visitProgramField(programClass, field);
        String[] secondMapping = optimizedJsonInfo.classJsonInfos.get("TestClass").javaToJsonFieldNames.get("myField");

        // Assert
        assertArrayEquals(firstMapping, secondMapping, "Field mapping should remain the same");
        assertEquals(1, optimizedJsonInfo.classJsonInfos.get("TestClass").javaToJsonFieldNames.size(),
                "Should still have only one field registered");
    }

    /**
     * Tests that visitProgramField registers jsonFieldIndices with null value (to be assigned later).
     */
    @Test
    public void testVisitProgramField_jsonFieldIndices_valueIsNull() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithField("TestClass", "myField");
        collector.visitProgramClass(programClass);

        // Act
        collector.visitProgramField(programClass, programClass.fields[0]);

        // Assert
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("myField"));
        assertNull(optimizedJsonInfo.jsonFieldIndices.get("myField"),
                "jsonFieldIndices value should be null initially (assigned later)");
    }

    /**
     * Tests that visitProgramField with field that has @SerializedName annotation.
     * The annotation processing is delegated to internal visitors, so we test basic registration.
     */
    @Test
    public void testVisitProgramField_withSerializedNameAnnotation_processesField() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithAnnotatedField("TestClass", "javaField", "jsonField");
        collector.visitProgramClass(programClass);

        // Act
        collector.visitProgramField(programClass, programClass.fields[0]);

        // Assert
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");
        assertTrue(classJsonInfo.javaToJsonFieldNames.containsKey("javaField"),
                "Field with @SerializedName should be processed");
    }

    /**
     * Tests that visitProgramField handles fields with different names correctly.
     */
    @Test
    public void testVisitProgramField_withDifferentFieldNames_eachRegisteredSeparately() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithMultipleFields("TestClass",
                new String[]{"id", "name", "email", "age"});
        collector.visitProgramClass(programClass);

        // Act
        for (ProgramField field : programClass.fields) {
            collector.visitProgramField(programClass, field);
        }

        // Assert
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = optimizedJsonInfo.classJsonInfos.get("TestClass");
        assertEquals(4, classJsonInfo.javaToJsonFieldNames.size());
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("id"));
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("name"));
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("email"));
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("age"));
    }

    /**
     * Tests that visitProgramField works correctly when ClassJsonInfo doesn't exist yet.
     * This tests error handling when visitProgramClass wasn't called first.
     */
    @Test
    public void testVisitProgramField_withoutPriorVisitProgramClass_throwsNullPointerException() {
        // Arrange
        collector = new OptimizedJsonFieldCollector(optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        ProgramClass programClass = createProgramClassWithField("TestClass", "field1");
        // Note: NOT calling collector.visitProgramClass(programClass)

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> collector.visitProgramField(programClass, programClass.fields[0]),
                "visitProgramField should throw when ClassJsonInfo doesn't exist");
    }

    /**
     * Tests that multiple collectors can work independently with the same OptimizedJsonInfo.
     */
    @Test
    public void testVisitProgramField_multipleCollectors_shareOptimizedJsonInfo() {
        // Arrange
        OptimizedJsonFieldCollector collector1 = new OptimizedJsonFieldCollector(
                optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.serialize);
        OptimizedJsonFieldCollector collector2 = new OptimizedJsonFieldCollector(
                optimizedJsonInfo, OptimizedJsonFieldCollector.Mode.deserialize);

        ProgramClass programClass1 = createProgramClassWithField("Class1", "field1");
        ProgramClass programClass2 = createProgramClassWithField("Class2", "field2");

        // Act
        collector1.visitProgramClass(programClass1);
        collector1.visitProgramField(programClass1, programClass1.fields[0]);

        collector2.visitProgramClass(programClass2);
        collector2.visitProgramField(programClass2, programClass2.fields[0]);

        // Assert
        assertEquals(2, optimizedJsonInfo.classJsonInfos.size(), "Both classes should be registered");
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("Class1"));
        assertTrue(optimizedJsonInfo.classJsonInfos.containsKey("Class2"));
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("field1"));
        assertTrue(optimizedJsonInfo.jsonFieldIndices.containsKey("field2"));
    }

    // =========================================================================
    // Tests for Mode.values.()[Lproguard/optimize/gson/OptimizedJsonFieldCollector$Mode;
    // =========================================================================

    /**
     * Tests that Mode.values() returns all enum values.
     */
    @Test
    public void testModeValues_returnsAllEnumValues() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        assertNotNull(modes, "values() should return a non-null array");
        assertEquals(2, modes.length, "Mode enum should have exactly 2 values");
    }

    /**
     * Tests that Mode.values() contains the serialize value.
     */
    @Test
    public void testModeValues_containsSerialize() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        boolean containsSerialize = false;
        for (OptimizedJsonFieldCollector.Mode mode : modes) {
            if (mode == OptimizedJsonFieldCollector.Mode.serialize) {
                containsSerialize = true;
                break;
            }
        }
        assertTrue(containsSerialize, "values() should contain Mode.serialize");
    }

    /**
     * Tests that Mode.values() contains the deserialize value.
     */
    @Test
    public void testModeValues_containsDeserialize() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        boolean containsDeserialize = false;
        for (OptimizedJsonFieldCollector.Mode mode : modes) {
            if (mode == OptimizedJsonFieldCollector.Mode.deserialize) {
                containsDeserialize = true;
                break;
            }
        }
        assertTrue(containsDeserialize, "values() should contain Mode.deserialize");
    }

    /**
     * Tests that Mode.values() returns values in the correct order.
     */
    @Test
    public void testModeValues_correctOrder() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        assertEquals(OptimizedJsonFieldCollector.Mode.serialize, modes[0],
                "First value should be serialize");
        assertEquals(OptimizedJsonFieldCollector.Mode.deserialize, modes[1],
                "Second value should be deserialize");
    }

    /**
     * Tests that Mode.values() returns a new array each time (defensive copy).
     */
    @Test
    public void testModeValues_returnsNewArrayEachTime() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes1 = OptimizedJsonFieldCollector.Mode.values();
        OptimizedJsonFieldCollector.Mode[] modes2 = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        assertNotSame(modes1, modes2, "values() should return a new array each time");
        assertArrayEquals(modes1, modes2, "Arrays should contain the same values");
    }

    /**
     * Tests that modifying the array returned by Mode.values() doesn't affect subsequent calls.
     */
    @Test
    public void testModeValues_modifyingReturnedArray_doesNotAffectSubsequentCalls() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes1 = OptimizedJsonFieldCollector.Mode.values();
        modes1[0] = OptimizedJsonFieldCollector.Mode.deserialize;
        OptimizedJsonFieldCollector.Mode[] modes2 = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        assertEquals(OptimizedJsonFieldCollector.Mode.serialize, modes2[0],
                "Modifying returned array should not affect subsequent calls");
    }

    /**
     * Tests that Mode.values() is consistent across multiple calls.
     */
    @Test
    public void testModeValues_consistentAcrossMultipleCalls() {
        // Act
        for (int i = 0; i < 10; i++) {
            OptimizedJsonFieldCollector.Mode[] modes = OptimizedJsonFieldCollector.Mode.values();

            // Assert
            assertEquals(2, modes.length, "Should always return 2 values");
            assertEquals(OptimizedJsonFieldCollector.Mode.serialize, modes[0]);
            assertEquals(OptimizedJsonFieldCollector.Mode.deserialize, modes[1]);
        }
    }

    // =========================================================================
    // Tests for Mode.valueOf.(Ljava/lang/String;)Lproguard/optimize/gson/OptimizedJsonFieldCollector$Mode;
    // =========================================================================

    /**
     * Tests that Mode.valueOf() returns the correct enum value for "serialize".
     */
    @Test
    public void testModeValueOf_withSerialize_returnsSerializeEnum() {
        // Act
        OptimizedJsonFieldCollector.Mode mode = OptimizedJsonFieldCollector.Mode.valueOf("serialize");

        // Assert
        assertNotNull(mode, "valueOf should return a non-null value");
        assertEquals(OptimizedJsonFieldCollector.Mode.serialize, mode,
                "valueOf(\"serialize\") should return Mode.serialize");
    }

    /**
     * Tests that Mode.valueOf() returns the correct enum value for "deserialize".
     */
    @Test
    public void testModeValueOf_withDeserialize_returnsDeserializeEnum() {
        // Act
        OptimizedJsonFieldCollector.Mode mode = OptimizedJsonFieldCollector.Mode.valueOf("deserialize");

        // Assert
        assertNotNull(mode, "valueOf should return a non-null value");
        assertEquals(OptimizedJsonFieldCollector.Mode.deserialize, mode,
                "valueOf(\"deserialize\") should return Mode.deserialize");
    }

    /**
     * Tests that Mode.valueOf() throws IllegalArgumentException for invalid enum name.
     */
    @Test
    public void testModeValueOf_withInvalidName_throwsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf("invalid"),
                "valueOf should throw IllegalArgumentException for invalid enum name");
    }

    /**
     * Tests that Mode.valueOf() throws IllegalArgumentException for empty string.
     */
    @Test
    public void testModeValueOf_withEmptyString_throwsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf(""),
                "valueOf should throw IllegalArgumentException for empty string");
    }

    /**
     * Tests that Mode.valueOf() throws NullPointerException for null input.
     */
    @Test
    public void testModeValueOf_withNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf(null),
                "valueOf should throw NullPointerException for null input");
    }

    /**
     * Tests that Mode.valueOf() is case-sensitive.
     */
    @Test
    public void testModeValueOf_isCaseSensitive() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf("SERIALIZE"),
                "valueOf should be case-sensitive and reject uppercase");
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf("Serialize"),
                "valueOf should be case-sensitive and reject mixed case");
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf("DESERIALIZE"),
                "valueOf should be case-sensitive and reject uppercase");
    }

    /**
     * Tests that Mode.valueOf() does not accept extra whitespace.
     */
    @Test
    public void testModeValueOf_withWhitespace_throwsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf(" serialize"),
                "valueOf should not accept leading whitespace");
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf("serialize "),
                "valueOf should not accept trailing whitespace");
        assertThrows(IllegalArgumentException.class,
                () -> OptimizedJsonFieldCollector.Mode.valueOf(" serialize "),
                "valueOf should not accept whitespace around the name");
    }

    /**
     * Tests that Mode.valueOf() returns the same instance for repeated calls with same name.
     */
    @Test
    public void testModeValueOf_repeatedCalls_returnsSameInstance() {
        // Act
        OptimizedJsonFieldCollector.Mode mode1 = OptimizedJsonFieldCollector.Mode.valueOf("serialize");
        OptimizedJsonFieldCollector.Mode mode2 = OptimizedJsonFieldCollector.Mode.valueOf("serialize");

        // Assert
        assertSame(mode1, mode2, "valueOf should return the same instance for the same name");
    }

    /**
     * Tests that Mode.valueOf() can retrieve all enum values by their names.
     */
    @Test
    public void testModeValueOf_canRetrieveAllEnumValues() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        for (OptimizedJsonFieldCollector.Mode mode : modes) {
            OptimizedJsonFieldCollector.Mode retrievedMode =
                    OptimizedJsonFieldCollector.Mode.valueOf(mode.name());
            assertSame(mode, retrievedMode,
                    "valueOf should retrieve the same enum instance using its name()");
        }
    }

    /**
     * Tests that Mode.valueOf() works correctly with name() for serialize.
     */
    @Test
    public void testModeValueOf_withNameMethod_serialize() {
        // Arrange
        OptimizedJsonFieldCollector.Mode original = OptimizedJsonFieldCollector.Mode.serialize;
        String name = original.name();

        // Act
        OptimizedJsonFieldCollector.Mode retrieved = OptimizedJsonFieldCollector.Mode.valueOf(name);

        // Assert
        assertSame(original, retrieved, "valueOf(mode.name()) should return the same instance");
        assertEquals("serialize", name, "name() should return the correct string");
    }

    /**
     * Tests that Mode.valueOf() works correctly with name() for deserialize.
     */
    @Test
    public void testModeValueOf_withNameMethod_deserialize() {
        // Arrange
        OptimizedJsonFieldCollector.Mode original = OptimizedJsonFieldCollector.Mode.deserialize;
        String name = original.name();

        // Act
        OptimizedJsonFieldCollector.Mode retrieved = OptimizedJsonFieldCollector.Mode.valueOf(name);

        // Assert
        assertSame(original, retrieved, "valueOf(mode.name()) should return the same instance");
        assertEquals("deserialize", name, "name() should return the correct string");
    }

    /**
     * Tests that Mode.valueOf() works correctly with toString() for serialize.
     */
    @Test
    public void testModeValueOf_withToString_serialize() {
        // Arrange
        OptimizedJsonFieldCollector.Mode original = OptimizedJsonFieldCollector.Mode.serialize;
        String stringValue = original.toString();

        // Act
        OptimizedJsonFieldCollector.Mode retrieved = OptimizedJsonFieldCollector.Mode.valueOf(stringValue);

        // Assert
        assertSame(original, retrieved, "valueOf(mode.toString()) should return the same instance");
        assertEquals("serialize", stringValue, "toString() should return the correct string");
    }

    /**
     * Tests that Mode.valueOf() works correctly with toString() for deserialize.
     */
    @Test
    public void testModeValueOf_withToString_deserialize() {
        // Arrange
        OptimizedJsonFieldCollector.Mode original = OptimizedJsonFieldCollector.Mode.deserialize;
        String stringValue = original.toString();

        // Act
        OptimizedJsonFieldCollector.Mode retrieved = OptimizedJsonFieldCollector.Mode.valueOf(stringValue);

        // Assert
        assertSame(original, retrieved, "valueOf(mode.toString()) should return the same instance");
        assertEquals("deserialize", stringValue, "toString() should return the correct string");
    }

    // =========================================================================
    // Helper methods to create test objects
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
     * The class has a name set up in the constant pool, but no fields.
     *
     * @param className the name of the class (e.g., "TestClass" or "com/example/TestClass")
     * @return a configured ProgramClass instance
     */
    private ProgramClass createMinimalProgramClass(String className) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        Constant[] constantPool = new Constant[10];
        constantPool[0] = null; // Index 0 is always null in Java constant pools
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;
        programClass.fields = new ProgramField[0];
        programClass.u2fieldsCount = 0;

        return programClass;
    }

    /**
     * Creates a ProgramClass with a single field.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return a configured ProgramClass instance with one field
     */
    private ProgramClass createProgramClassWithField(String className, String fieldName) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with class name and field info
        Constant[] constantPool = new Constant[20];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);
        constantPool[3] = new Utf8Constant(fieldName);
        constantPool[4] = new Utf8Constant("I"); // field descriptor for int

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 20;

        // Create a field
        ProgramField field = new ProgramField();
        field.u2accessFlags = 0;
        field.u2nameIndex = 3;
        field.u2descriptorIndex = 4;
        field.u2attributesCount = 0;
        field.attributes = new Attribute[0];

        programClass.fields = new ProgramField[]{field};
        programClass.u2fieldsCount = 1;

        return programClass;
    }

    /**
     * Creates a ProgramClass with multiple fields.
     *
     * @param className the name of the class
     * @param fieldNames array of field names
     * @return a configured ProgramClass instance with multiple fields
     */
    private ProgramClass createProgramClassWithMultipleFields(String className, String[] fieldNames) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool large enough for class name and all fields
        int poolSize = 100 + (fieldNames.length * 2);
        Constant[] constantPool = new Constant[poolSize];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        int nextIndex = 3;
        ProgramField[] fields = new ProgramField[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++) {
            // Add field name to constant pool
            constantPool[nextIndex] = new Utf8Constant(fieldNames[i]);
            int nameIndex = nextIndex++;

            // Add field descriptor to constant pool
            constantPool[nextIndex] = new Utf8Constant("I"); // int type
            int descriptorIndex = nextIndex++;

            // Create field
            ProgramField field = new ProgramField();
            field.u2accessFlags = 0;
            field.u2nameIndex = nameIndex;
            field.u2descriptorIndex = descriptorIndex;
            field.u2attributesCount = 0;
            field.attributes = new Attribute[0];

            fields[i] = field;
        }

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = poolSize;
        programClass.fields = fields;
        programClass.u2fieldsCount = fieldNames.length;

        return programClass;
    }

    /**
     * Creates a simple ProgramField for testing.
     *
     * @param fieldName the name of the field
     * @return a configured ProgramField instance
     */
    private ProgramField createSimpleField(String fieldName) {
        ProgramField field = new ProgramField();
        field.u2accessFlags = 0;
        field.u2nameIndex = 1; // Dummy index
        field.u2descriptorIndex = 2; // Dummy index
        field.u2attributesCount = 0;
        field.attributes = new Attribute[0];
        return field;
    }

    /**
     * Creates a ProgramClass with a field that has @SerializedName annotation.
     *
     * @param className the name of the class
     * @param fieldName the Java field name
     * @param jsonName the JSON field name specified in @SerializedName
     * @return a configured ProgramClass instance
     */
    private ProgramClass createProgramClassWithAnnotatedField(String className, String fieldName, String jsonName) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with class name, field info, and annotation
        Constant[] constantPool = new Constant[30];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);
        constantPool[3] = new Utf8Constant(fieldName);
        constantPool[4] = new Utf8Constant("I"); // field descriptor
        constantPool[5] = new Utf8Constant("RuntimeVisibleAnnotations");
        constantPool[6] = new Utf8Constant(GsonClassConstants.ANNOTATION_TYPE_SERIALIZED_NAME);
        constantPool[7] = new Utf8Constant("value");
        constantPool[8] = new Utf8Constant(jsonName);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 30;

        // Create annotation
        ElementValue[] elementValues = new ElementValue[1];
        elementValues[0] = new ConstantElementValue('s', 7, 8);

        Annotation annotation = new Annotation();
        annotation.u2typeIndex = 6;
        annotation.u2elementValuesCount = 1;
        annotation.elementValues = elementValues;

        // Create annotation attribute
        Annotation[] annotations = new Annotation[1];
        annotations[0] = annotation;

        AnnotationsAttribute annotationsAttribute = new RuntimeVisibleAnnotationsAttribute();
        annotationsAttribute.u2attributeNameIndex = 5;
        annotationsAttribute.u2annotationsCount = 1;
        annotationsAttribute.annotations = annotations;

        // Create field with annotation
        ProgramField field = new ProgramField();
        field.u2accessFlags = 0;
        field.u2nameIndex = 3;
        field.u2descriptorIndex = 4;
        field.u2attributesCount = 1;
        field.attributes = new Attribute[]{annotationsAttribute};

        programClass.fields = new ProgramField[]{field};
        programClass.u2fieldsCount = 1;

        return programClass;
    }
}
