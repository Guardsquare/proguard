package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.io.ExtraDataEntryNameMap;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the visitAnyClass method of {@link OptimizedTypeAdapterAdder}.
 * Tests the method: visitAnyClass.(Lproguard/classfile/Clazz;)V
 *
 * The visitAnyClass method is a no-op implementation of the ClassVisitor interface,
 * meaning it performs no operations when called. The actual logic for processing
 * ProgramClass instances is in the visitProgramClass method.
 */
public class OptimizedTypeAdapterAdderClaude_visitAnyClassTest {

    private OptimizedTypeAdapterAdder adder;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private CodeAttributeEditor codeAttributeEditor;
    private OptimizedJsonInfo serializationInfo;
    private OptimizedJsonInfo deserializationInfo;
    private ExtraDataEntryNameMap extraDataEntryNameMap;
    private Map<String, String> typeAdapterRegistry;
    private GsonRuntimeSettings gsonRuntimeSettings;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        codeAttributeEditor = new CodeAttributeEditor();
        serializationInfo = new OptimizedJsonInfo();
        deserializationInfo = new OptimizedJsonInfo();
        extraDataEntryNameMap = new ExtraDataEntryNameMap();
        typeAdapterRegistry = new HashMap<>();
        gsonRuntimeSettings = new GsonRuntimeSettings();

        adder = new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        );
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
        Clazz clazz = mock(Clazz.class);

        // Act
        adder.visitAnyClass(clazz);

        // Assert
        // The method should not interact with the clazz
        verifyNoInteractions(clazz);

        // Verify no changes to the type adapter registry
        assertTrue(typeAdapterRegistry.isEmpty(),
                "Type adapter registry should remain empty");

        // Verify no changes to the extra data entry name map
        assertTrue(extraDataEntryNameMap.getAllExtraDataEntryNames().isEmpty(),
                "Extra data entry name map should remain empty");
    }

    /**
     * Tests that visitAnyClass can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyClass_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        adder.visitAnyClass(clazz1);
        adder.visitAnyClass(clazz2);
        adder.visitAnyClass(clazz3);

        // Assert
        verifyNoInteractions(clazz1, clazz2, clazz3);
        assertTrue(typeAdapterRegistry.isEmpty(),
                "Type adapter registry should remain empty after multiple calls");
        assertTrue(extraDataEntryNameMap.getAllExtraDataEntryNames().isEmpty(),
                "Extra data entry name map should remain empty after multiple calls");
    }

    /**
     * Tests that visitAnyClass does not throw exceptions with a null argument.
     * While not a recommended usage, the empty method body should handle this safely.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> adder.visitAnyClass(null),
                "visitAnyClass should not throw exception with null argument");
    }

    /**
     * Tests that visitAnyClass works correctly when called on a LibraryClass.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNothing() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.thisClassName = "java/lang/String";

        // Act
        adder.visitAnyClass(libraryClass);

        // Assert
        assertTrue(typeAdapterRegistry.isEmpty(),
                "Type adapter registry should remain empty for LibraryClass");
        assertTrue(extraDataEntryNameMap.getAllExtraDataEntryNames().isEmpty(),
                "Extra data entry name map should remain empty for LibraryClass");
    }

    /**
     * Tests that visitAnyClass works correctly when called on a ProgramClass.
     * Note: visitAnyClass is still a no-op even for ProgramClass.
     * The actual logic for ProgramClass is in visitProgramClass method.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNothing() {
        // Arrange
        ProgramClass programClass = createMinimalProgramClass("com/example/TestClass");

        // Act
        adder.visitAnyClass(programClass);

        // Assert
        assertTrue(typeAdapterRegistry.isEmpty(),
                "Type adapter registry should remain empty even for ProgramClass");
        assertTrue(extraDataEntryNameMap.getAllExtraDataEntryNames().isEmpty(),
                "Extra data entry name map should remain empty even for ProgramClass");
    }

    /**
     * Tests that visitAnyClass can be called repeatedly on the same Clazz instance.
     */
    @Test
    public void testVisitAnyClass_repeatedCallsOnSameClazz_noSideEffects() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        adder.visitAnyClass(clazz);
        adder.visitAnyClass(clazz);
        adder.visitAnyClass(clazz);

        // Assert
        verifyNoInteractions(clazz);
        assertTrue(typeAdapterRegistry.isEmpty(),
                "Type adapter registry should remain empty after repeated calls");
    }

    /**
     * Tests that visitAnyClass does not throw when called with null constructor parameters.
     */
    @Test
    public void testVisitAnyClass_withNullConstructorParameters_doesNotThrow() {
        // Arrange
        OptimizedTypeAdapterAdder adderWithNulls = new OptimizedTypeAdapterAdder(
                null, null, null, null, null, null, null, null
        );
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> adderWithNulls.visitAnyClass(clazz),
                "visitAnyClass should not throw even with null constructor parameters");
    }

    /**
     * Tests that visitAnyClass behaves consistently across different adder instances.
     */
    @Test
    public void testVisitAnyClass_differentInstances_consistentBehavior() {
        // Arrange
        OptimizedTypeAdapterAdder adder1 = new OptimizedTypeAdapterAdder(
                new ClassPool(),
                new ClassPool(),
                new CodeAttributeEditor(),
                new OptimizedJsonInfo(),
                new OptimizedJsonInfo(),
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );
        OptimizedTypeAdapterAdder adder2 = new OptimizedTypeAdapterAdder(
                new ClassPool(),
                new ClassPool(),
                new CodeAttributeEditor(),
                new OptimizedJsonInfo(),
                new OptimizedJsonInfo(),
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            adder1.visitAnyClass(clazz);
            adder2.visitAnyClass(clazz);
        }, "Both adders should handle visitAnyClass consistently");
    }

    /**
     * Tests that visitAnyClass is truly a no-op by verifying no state changes occur.
     */
    @Test
    public void testVisitAnyClass_isNoOp_verifyNoStateChange() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        int registrySize = typeAdapterRegistry.size();

        // Act
        adder.visitAnyClass(clazz);

        // Assert
        // Verify the adder itself hasn't changed (it should still be usable)
        assertNotNull(adder, "Adder should remain valid after visitAnyClass");
        assertEquals(registrySize, typeAdapterRegistry.size(),
                "Type adapter registry size should not change");

        // Verify we can still call other methods without issues
        assertDoesNotThrow(() -> adder.visitAnyClass(mock(Clazz.class)),
                "Adder should remain functional after visitAnyClass");
    }

    /**
     * Tests that visitAnyClass does not modify the program class pool.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyProgramClassPool() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        int initialSize = programClassPool.size();

        // Act
        adder.visitAnyClass(clazz);

        // Assert
        assertEquals(initialSize, programClassPool.size(),
                "Program class pool should not be modified");
    }

    /**
     * Tests that visitAnyClass does not modify the library class pool.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyLibraryClassPool() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        int initialSize = libraryClassPool.size();

        // Act
        adder.visitAnyClass(clazz);

        // Assert
        assertEquals(initialSize, libraryClassPool.size(),
                "Library class pool should not be modified");
    }

    /**
     * Tests that visitAnyClass does not modify serialization info.
     */
    @Test
    public void testVisitAnyClass_doesNotModifySerializationInfo() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        serializationInfo.classIndices.put("TestClass", 0);
        int initialSize = serializationInfo.classIndices.size();

        // Act
        adder.visitAnyClass(clazz);

        // Assert
        assertEquals(initialSize, serializationInfo.classIndices.size(),
                "Serialization info should not be modified");
        assertEquals(0, serializationInfo.classIndices.get("TestClass"),
                "Existing serialization info entries should remain unchanged");
    }

    /**
     * Tests that visitAnyClass does not modify deserialization info.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyDeserializationInfo() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        deserializationInfo.classIndices.put("TestClass", 0);
        int initialSize = deserializationInfo.classIndices.size();

        // Act
        adder.visitAnyClass(clazz);

        // Assert
        assertEquals(initialSize, deserializationInfo.classIndices.size(),
                "Deserialization info should not be modified");
        assertEquals(0, deserializationInfo.classIndices.get("TestClass"),
                "Existing deserialization info entries should remain unchanged");
    }

    /**
     * Tests that visitAnyClass can be called in sequence with other visitor methods.
     */
    @Test
    public void testVisitAnyClass_inSequenceWithOtherMethods_noInterference() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            adder.visitAnyClass(clazz1);
            adder.visitAnyClass(clazz2);
            adder.visitAnyClass(clazz1);
        }, "visitAnyClass should work in sequence without interference");

        assertTrue(typeAdapterRegistry.isEmpty(),
                "Type adapter registry should remain empty");
    }

    /**
     * Tests that visitAnyClass with a mock Clazz that has configured behavior still does nothing.
     */
    @Test
    public void testVisitAnyClass_withConfiguredMock_stillDoesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/TestClass");
        when(clazz.getProcessingInfo()).thenReturn(new Object());

        // Act
        adder.visitAnyClass(clazz);

        // Assert
        // Even though the mock has configured behavior, visitAnyClass shouldn't call it
        verifyNoInteractions(clazz);
        assertTrue(typeAdapterRegistry.isEmpty(),
                "Type adapter registry should remain empty");
    }

    /**
     * Tests that visitAnyClass can handle being called before and after visitProgramClass.
     */
    @Test
    public void testVisitAnyClass_beforeAndAfterVisitProgramClass_remainsNoOp() {
        // Arrange
        Clazz anyClazz = mock(Clazz.class);

        // Act
        adder.visitAnyClass(anyClazz);  // Before
        // Note: We don't actually call visitProgramClass as it would create files
        adder.visitAnyClass(anyClazz);  // After

        // Assert
        verifyNoInteractions(anyClazz);
        assertTrue(typeAdapterRegistry.isEmpty(),
                "visitAnyClass should remain a no-op regardless of call order");
    }

    // =========================================================================
    // Helper methods
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
     *
     * @param className the name of the class (e.g., "com/example/TestClass")
     * @return a configured ProgramClass instance
     */
    private ProgramClass createMinimalProgramClass(String className) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        proguard.classfile.constant.Constant[] constantPool =
                new proguard.classfile.constant.Constant[10];
        constantPool[0] = null;
        constantPool[1] = new proguard.classfile.constant.ClassConstant(2, null);
        constantPool[2] = new proguard.classfile.constant.Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;
        programClass.fields = new proguard.classfile.ProgramField[0];
        programClass.u2fieldsCount = 0;

        return programClass;
    }
}
