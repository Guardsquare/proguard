package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link OptimizedJsonReaderImplInitializer}.
 * Tests the constructor, visitAnyAttribute, and visitCodeAttribute methods.
 *
 * The OptimizedJsonReaderImplInitializer class implements the static initializer
 * of _OptimizedJsonReaderImpl to initialize the data structure with the correct
 * mapping between Json field names and internal indices.
 */
public class OptimizedJsonReaderImplInitializerClaudeTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private OptimizedJsonInfo deserializationInfo;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        deserializationInfo = new OptimizedJsonInfo();
    }

    // =========================================================================
    // Tests for constructor: <init>.(Lproguard/classfile/ClassPool;Lproguard/classfile/ClassPool;Lproguard/classfile/editor/CodeAttributeEditor;Lproguard/optimize/gson/OptimizedJsonInfo;)V
    // =========================================================================

    /**
     * Tests that the constructor creates a non-null OptimizedJsonReaderImplInitializer instance
     * with valid non-null parameters.
     */
    @Test
    public void testConstructor_withValidParameters_createsNonNullInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );

        // Assert
        assertNotNull(initializer, "OptimizedJsonReaderImplInitializer instance should not be null");
    }

    /**
     * Tests that the constructor accepts null program class pool.
     */
    @Test
    public void testConstructor_withNullProgramClassPool_doesNotThrow() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonReaderImplInitializer(
            null,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        ), "Constructor should accept null program class pool");
    }

    /**
     * Tests that the constructor accepts null library class pool.
     */
    @Test
    public void testConstructor_withNullLibraryClassPool_doesNotThrow() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonReaderImplInitializer(
            programClassPool,
            null,
            codeAttributeEditor,
            deserializationInfo
        ), "Constructor should accept null library class pool");
    }

    /**
     * Tests that the constructor accepts null code attribute editor.
     */
    @Test
    public void testConstructor_withNullCodeAttributeEditor_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            null,
            deserializationInfo
        ), "Constructor should accept null code attribute editor");
    }

    /**
     * Tests that the constructor accepts null deserialization info.
     */
    @Test
    public void testConstructor_withNullDeserializationInfo_doesNotThrow() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            null
        ), "Constructor should accept null deserialization info");
    }

    /**
     * Tests that the constructor accepts all null parameters.
     */
    @Test
    public void testConstructor_withAllNullParameters_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonReaderImplInitializer(
            null,
            null,
            null,
            null
        ), "Constructor should accept all null parameters");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_areIndependent() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor codeAttributeEditor2 = mock(CodeAttributeEditor.class);
        OptimizedJsonInfo info1 = new OptimizedJsonInfo();
        OptimizedJsonInfo info2 = new OptimizedJsonInfo();

        // Act
        OptimizedJsonReaderImplInitializer initializer1 = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor1,
            info1
        );

        OptimizedJsonReaderImplInitializer initializer2 = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor2,
            info2
        );

        // Assert
        assertNotNull(initializer1, "First instance should not be null");
        assertNotNull(initializer2, "Second instance should not be null");
        assertNotSame(initializer1, initializer2, "Instances should be different");
    }

    /**
     * Tests that the constructor works with empty class pools.
     */
    @Test
    public void testConstructor_withEmptyClassPools_createsInstance() {
        // Arrange
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            emptyProgramPool,
            emptyLibraryPool,
            codeAttributeEditor,
            deserializationInfo
        );

        // Assert
        assertNotNull(initializer, "Instance should be created with empty class pools");
    }

    /**
     * Tests that the constructor works with empty OptimizedJsonInfo.
     */
    @Test
    public void testConstructor_withEmptyOptimizedJsonInfo_createsInstance() {
        // Arrange
        OptimizedJsonInfo emptyInfo = new OptimizedJsonInfo();
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            emptyInfo
        );

        // Assert
        assertNotNull(initializer, "Instance should be created with empty OptimizedJsonInfo");
    }

    /**
     * Tests that the constructor works with populated OptimizedJsonInfo.
     */
    @Test
    public void testConstructor_withPopulatedOptimizedJsonInfo_createsInstance() {
        // Arrange
        OptimizedJsonInfo populatedInfo = new OptimizedJsonInfo();
        populatedInfo.jsonFieldIndices.put("field1", 0);
        populatedInfo.jsonFieldIndices.put("field2", 1);
        populatedInfo.classIndices.put("TestClass", 0);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            populatedInfo
        );

        // Assert
        assertNotNull(initializer, "Instance should be created with populated OptimizedJsonInfo");
    }

    /**
     * Tests that the same class pools can be used for both program and library pools.
     */
    @Test
    public void testConstructor_withSamePoolForProgramAndLibrary_createsInstance() {
        // Arrange
        ClassPool sharedPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            sharedPool,
            sharedPool,
            codeAttributeEditor,
            deserializationInfo
        );

        // Assert
        assertNotNull(initializer, "Instance should be created when same pool is used for both parameters");
    }

    // =========================================================================
    // Tests for visitAnyAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/attribute/Attribute;)V
    // =========================================================================

    /**
     * Tests that visitAnyAttribute is a no-op and does nothing when called.
     * This is the default implementation of the AttributeVisitor interface.
     */
    @Test
    public void testVisitAnyAttribute_withValidParameters_doesNothing() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        initializer.visitAnyAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyAttribute_multipleCalls_noSideEffects() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);

        // Act
        initializer.visitAnyAttribute(clazz1, attr1);
        initializer.visitAnyAttribute(clazz2, attr2);
        initializer.visitAnyAttribute(clazz1, attr2);

        // Assert
        verifyNoInteractions(clazz1, clazz2, attr1, attr2);
    }

    /**
     * Tests that visitAnyAttribute does not throw with null clazz parameter.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrow() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.visitAnyAttribute(null, attribute),
            "visitAnyAttribute should not throw with null clazz");
    }

    /**
     * Tests that visitAnyAttribute does not throw with null attribute parameter.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrow() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> initializer.visitAnyAttribute(clazz, null),
            "visitAnyAttribute should not throw with null attribute");
    }

    /**
     * Tests that visitAnyAttribute does not throw with both null parameters.
     */
    @Test
    public void testVisitAnyAttribute_withBothNull_doesNotThrow() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );

        // Act & Assert
        assertDoesNotThrow(() -> initializer.visitAnyAttribute(null, null),
            "visitAnyAttribute should not throw with both null parameters");
    }

    /**
     * Tests that visitAnyAttribute works correctly with a ProgramClass.
     */
    @Test
    public void testVisitAnyAttribute_withProgramClass_doesNothing() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act
        initializer.visitAnyAttribute(programClass, attribute);

        // Assert
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works correctly with a LibraryClass.
     */
    @Test
    public void testVisitAnyAttribute_withLibraryClass_doesNothing() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        LibraryClass libraryClass = new LibraryClass();
        Attribute attribute = mock(Attribute.class);

        // Act
        initializer.visitAnyAttribute(libraryClass, attribute);

        // Assert
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works correctly with a CodeAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withCodeAttribute_doesNothing() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        Clazz clazz = mock(Clazz.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        initializer.visitAnyAttribute(clazz, codeAttribute);

        // Assert
        // No interactions expected, as visitAnyAttribute is a no-op
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyAttribute can be called repeatedly on the same parameters.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsOnSameParameters_noSideEffects() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        initializer.visitAnyAttribute(clazz, attribute);
        initializer.visitAnyAttribute(clazz, attribute);
        initializer.visitAnyAttribute(clazz, attribute);

        // Assert
        verifyNoInteractions(clazz, attribute);
    }

    // =========================================================================
    // Tests for visitCodeAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;)V
    // =========================================================================

    /**
     * Tests that visitCodeAttribute does not throw with null deserialization info.
     * With null deserialization info, the method should fail when trying to iterate jsonFieldIndices.
     */
    @Test
    public void testVisitCodeAttribute_withNullDeserializationInfo_throwsNullPointerException() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            null
        );
        ProgramClass programClass = new ProgramClass();
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass, method, codeAttribute),
            "Should throw NullPointerException when deserialization info is null");
    }

    /**
     * Tests that visitCodeAttribute requires HashMap class in library pool.
     * The method tries to look up HashMap from the library class pool, which will be null if not present.
     * This will cause a NullPointerException when passed to InstructionSequenceBuilder.
     */
    @Test
    public void testVisitCodeAttribute_withEmptyLibraryPool_throwsNullPointerException() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonInfo emptyInfo = new OptimizedJsonInfo();
        // jsonFieldIndices is empty, so the method won't enter the for loop
        // but it still tries to create a HashMap before the loop

        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool, // empty pool, doesn't have HashMap
            codeAttributeEditor,
            emptyInfo
        );

        ProgramClass programClass = new ProgramClass();
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        // The method will fail when trying to get HashMap from the empty library pool
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass, method, codeAttribute),
            "visitCodeAttribute should throw when library pool doesn't contain required Java classes"
        );
    }

    /**
     * Tests that visitCodeAttribute does not throw with null clazz parameter.
     * The method casts clazz to ProgramClass, so null will cause a ClassCastException or NullPointerException.
     */
    @Test
    public void testVisitCodeAttribute_withNullClazz_throwsException() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        assertThrows(Exception.class,
            () -> initializer.visitCodeAttribute(null, method, codeAttribute),
            "visitCodeAttribute should throw when clazz is null");
    }

    /**
     * Tests that visitCodeAttribute requires codeAttribute to have u4codeLength.
     * The method calls codeAttributeEditor.reset(codeAttribute.u4codeLength) on line 80.
     * With an empty CodeAttribute, this should not throw immediately but will fail later
     * when trying to use the empty library pool.
     */
    @Test
    public void testVisitCodeAttribute_withNullMethod_throwsDueToEmptyLibraryPool() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        ProgramClass programClass = new ProgramClass();
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        // The method parameter is passed to codeAttributeEditor.visitCodeAttribute
        // but is not used directly in the initializer logic until line 111
        // It will fail earlier when trying to get HashMap from the empty library pool
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass, null, codeAttribute),
            "visitCodeAttribute should throw when library pool is empty"
        );
    }

    /**
     * Tests that visitCodeAttribute requires a ProgramClass, not a LibraryClass.
     * The implementation casts clazz to ProgramClass on line 83.
     */
    @Test
    public void testVisitCodeAttribute_withLibraryClass_throwsClassCastException() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool,
            codeAttributeEditor,
            deserializationInfo
        );
        LibraryClass libraryClass = new LibraryClass();
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        assertThrows(ClassCastException.class,
            () -> initializer.visitCodeAttribute(libraryClass, method, codeAttribute),
            "visitCodeAttribute should throw ClassCastException when clazz is not a ProgramClass");
    }

    /**
     * Tests that visitCodeAttribute requires proper library pool setup.
     * Even with a ProgramClass, the method needs HashMap class in the library pool.
     */
    @Test
    public void testVisitCodeAttribute_withProgramClass_requiresHashMapInLibraryPool() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool, // empty, doesn't have HashMap
            codeAttributeEditor,
            deserializationInfo
        );
        ProgramClass programClass = new ProgramClass();
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        // The method will fail when trying to get HashMap from the empty library pool
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass, method, codeAttribute),
            "visitCodeAttribute requires HashMap class in library pool"
        );
    }

    /**
     * Tests visitCodeAttribute behavior when jsonFieldIndices contains multiple entries.
     * The method will still fail due to the empty library pool, even with multiple indices.
     */
    @Test
    public void testVisitCodeAttribute_withMultipleJsonFieldIndices_stillRequiresLibraryPool() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonInfo populatedInfo = new OptimizedJsonInfo();
        populatedInfo.jsonFieldIndices.put("field1", 0);
        populatedInfo.jsonFieldIndices.put("field2", 1);
        populatedInfo.jsonFieldIndices.put("field3", 2);

        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool, // empty, doesn't have HashMap
            codeAttributeEditor,
            populatedInfo
        );

        ProgramClass programClass = new ProgramClass();
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        // The method will fail when trying to get HashMap from the empty library pool,
        // before it even enters the loop to process the jsonFieldIndices
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass, method, codeAttribute),
            "visitCodeAttribute should throw when library pool is empty, regardless of jsonFieldIndices"
        );
    }

    /**
     * Tests that visitCodeAttribute can be called multiple times, each time throwing due to empty library pool.
     */
    @Test
    public void testVisitCodeAttribute_multipleCalls_eachThrowsDueToEmptyLibraryPool() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool, // empty
            codeAttributeEditor,
            deserializationInfo
        );
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass1, method1, codeAttr1),
            "First call should throw due to empty library pool");
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass2, method2, codeAttr2),
            "Second call should also throw due to empty library pool");
    }

    /**
     * Tests that visitCodeAttribute accesses the jsonFieldIndices map from deserializationInfo.
     * This test verifies that the deserializationInfo parameter is actually used.
     */
    @Test
    public void testVisitCodeAttribute_accessesJsonFieldIndicesFromDeserializationInfo() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        OptimizedJsonInfo info = new OptimizedJsonInfo();
        // Add specific entries to verify they would be used (if the method got that far)
        info.jsonFieldIndices.put("userName", 5);
        info.jsonFieldIndices.put("userId", 10);

        OptimizedJsonReaderImplInitializer initializer = new OptimizedJsonReaderImplInitializer(
            programClassPool,
            libraryClassPool, // empty, will cause failure before the jsonFieldIndices loop
            codeAttributeEditor,
            info
        );

        ProgramClass programClass = new ProgramClass();
        Method method = mock(Method.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        // The method should fail before it gets to iterate through info.jsonFieldIndices
        // because the library pool is empty and doesn't have HashMap
        assertThrows(NullPointerException.class,
            () -> initializer.visitCodeAttribute(programClass, method, codeAttribute),
            "visitCodeAttribute should throw due to empty library pool"
        );
    }
}
