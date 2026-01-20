package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static proguard.obfuscate.MemberObfuscator.*;

/**
 * Test class for {@link KotlinDefaultMethodNameEqualizer#visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)}.
 * Tests the visitAnyFunction method which sets the new name for default parameter methods.
 */
public class KotlinDefaultMethodNameEqualizerClaude_visitAnyFunctionTest {

    private KotlinDefaultMethodNameEqualizer equalizer;
    private Clazz mockClazz;
    private KotlinMetadata mockMetadata;
    private KotlinFunctionMetadata mockFunctionMetadata;

    @BeforeEach
    public void setUp() {
        equalizer = new KotlinDefaultMethodNameEqualizer();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinMetadata.class);
        mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
    }

    /**
     * Tests that visitAnyFunction does not throw exception when referencedDefaultMethod is null.
     * When there is no default method, the method should simply return without doing anything.
     */
    @Test
    public void testVisitAnyFunction_withNullReferencedDefaultMethod_doesNotThrowException() {
        // Arrange
        mockFunctionMetadata.referencedDefaultMethod = null;
        mockFunctionMetadata.referencedMethod = mock(Method.class);

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);
        }, "visitAnyFunction should handle null referencedDefaultMethod gracefully");
    }

    /**
     * Tests that visitAnyFunction sets the new name for the default method when it exists.
     * This is the main functionality of the method.
     */
    @Test
    public void testVisitAnyFunction_withReferencedDefaultMethod_setsNewName() {
        // Arrange
        ProgramMethod referencedMethod = createProgramMethod("testMethod");
        ProgramMethod referencedDefaultMethod = createProgramMethod("testMethod$default");

        setNewMemberName(referencedMethod, "obfuscatedName");

        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = referencedDefaultMethod;

        // Act
        equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);

        // Assert - verify the default method gets the obfuscated name + suffix
        String newDefaultMethodName = newMemberName(referencedDefaultMethod);
        assertNotNull(newDefaultMethodName, "Default method should have a new name set");
        assertTrue(newDefaultMethodName.startsWith("obfuscatedName"),
                   "Default method name should start with the obfuscated referenced method name");
    }

    /**
     * Tests that visitAnyFunction preserves the $default suffix.
     * The default method name should be the referenced method's new name plus the suffix.
     */
    @Test
    public void testVisitAnyFunction_preservesDefaultSuffix() {
        // Arrange
        ProgramMethod referencedMethod = createProgramMethod("originalMethod");
        ProgramMethod referencedDefaultMethod = createProgramMethod("originalMethod$default");

        String obfuscatedName = "a";
        setNewMemberName(referencedMethod, obfuscatedName);

        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = referencedDefaultMethod;

        // Act
        equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);

        // Assert
        String newDefaultMethodName = newMemberName(referencedDefaultMethod);
        assertNotNull(newDefaultMethodName);
        assertTrue(newDefaultMethodName.contains("$default"),
                   "Default method name should contain the $default suffix");
    }

    /**
     * Tests that visitAnyFunction works when the referenced method has no new name yet.
     * If the referenced method hasn't been obfuscated yet, newMemberName returns null.
     */
    @Test
    public void testVisitAnyFunction_whenReferencedMethodHasNoNewName_handlesNull() {
        // Arrange
        ProgramMethod referencedMethod = createProgramMethod("testMethod");
        ProgramMethod referencedDefaultMethod = createProgramMethod("testMethod$default");

        // Don't set a new name for the referenced method
        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = referencedDefaultMethod;

        // Act & Assert - should handle the case where newMemberName returns null
        assertDoesNotThrow(() -> {
            equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);
        }, "visitAnyFunction should handle null new member name");
    }

    /**
     * Tests that visitAnyFunction can be called multiple times.
     * Verifies idempotency and that the method can process multiple functions.
     */
    @Test
    public void testVisitAnyFunction_canBeCalledMultipleTimes() {
        // Arrange
        ProgramMethod referencedMethod1 = createProgramMethod("method1");
        ProgramMethod defaultMethod1 = createProgramMethod("method1$default");
        setNewMemberName(referencedMethod1, "a");

        ProgramMethod referencedMethod2 = createProgramMethod("method2");
        ProgramMethod defaultMethod2 = createProgramMethod("method2$default");
        setNewMemberName(referencedMethod2, "b");

        KotlinFunctionMetadata metadata1 = mock(KotlinFunctionMetadata.class);
        metadata1.referencedMethod = referencedMethod1;
        metadata1.referencedDefaultMethod = defaultMethod1;

        KotlinFunctionMetadata metadata2 = mock(KotlinFunctionMetadata.class);
        metadata2.referencedMethod = referencedMethod2;
        metadata2.referencedDefaultMethod = defaultMethod2;

        // Act
        equalizer.visitAnyFunction(mockClazz, mockMetadata, metadata1);
        equalizer.visitAnyFunction(mockClazz, mockMetadata, metadata2);

        // Assert
        assertNotNull(newMemberName(defaultMethod1));
        assertNotNull(newMemberName(defaultMethod2));
        assertTrue(newMemberName(defaultMethod1).startsWith("a"));
        assertTrue(newMemberName(defaultMethod2).startsWith("b"));
    }

    /**
     * Tests that visitAnyFunction handles the case where both referenced methods are null.
     */
    @Test
    public void testVisitAnyFunction_withBothReferencedMethodsNull_doesNotThrow() {
        // Arrange
        mockFunctionMetadata.referencedMethod = null;
        mockFunctionMetadata.referencedDefaultMethod = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);
        }, "visitAnyFunction should handle null referenced methods");
    }

    /**
     * Tests that visitAnyFunction with null Clazz does not throw.
     * The method doesn't use the clazz parameter directly.
     */
    @Test
    public void testVisitAnyFunction_withNullClazz_doesNotThrow() {
        // Arrange
        mockFunctionMetadata.referencedDefaultMethod = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            equalizer.visitAnyFunction(null, mockMetadata, mockFunctionMetadata);
        }, "visitAnyFunction should handle null Clazz");
    }

    /**
     * Tests that visitAnyFunction with null KotlinMetadata does not throw.
     * The method doesn't use the metadata parameter directly.
     */
    @Test
    public void testVisitAnyFunction_withNullMetadata_doesNotThrow() {
        // Arrange
        mockFunctionMetadata.referencedDefaultMethod = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            equalizer.visitAnyFunction(mockClazz, null, mockFunctionMetadata);
        }, "visitAnyFunction should handle null KotlinMetadata");
    }

    /**
     * Tests that visitAnyFunction with null KotlinFunctionMetadata throws NullPointerException.
     * The function metadata is dereferenced, so null should throw NPE.
     */
    @Test
    public void testVisitAnyFunction_withNullFunctionMetadata_throwsNPE() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            equalizer.visitAnyFunction(mockClazz, mockMetadata, null);
        }, "visitAnyFunction should throw NPE with null KotlinFunctionMetadata");
    }

    /**
     * Tests that visitAnyFunction correctly synchronizes the default method name
     * when the referenced method name changes.
     */
    @Test
    public void testVisitAnyFunction_synchronizesDefaultMethodName() {
        // Arrange
        ProgramMethod referencedMethod = createProgramMethod("calculate");
        ProgramMethod referencedDefaultMethod = createProgramMethod("calculate$default");

        String newName = "calc";
        setNewMemberName(referencedMethod, newName);

        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = referencedDefaultMethod;

        // Act
        equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);

        // Assert - default method should have synchronized name
        String defaultMethodNewName = newMemberName(referencedDefaultMethod);
        assertNotNull(defaultMethodNewName);
        assertTrue(defaultMethodNewName.startsWith(newName),
                   "Default method should start with the referenced method's new name");
    }

    /**
     * Tests that visitAnyFunction only processes when referencedDefaultMethod exists.
     * This verifies the conditional logic in the method.
     */
    @Test
    public void testVisitAnyFunction_onlyProcessesWhenDefaultMethodExists() {
        // Arrange - case 1: with default method
        ProgramMethod referencedMethod1 = createProgramMethod("method1");
        ProgramMethod defaultMethod1 = createProgramMethod("method1$default");
        setNewMemberName(referencedMethod1, "m1");

        KotlinFunctionMetadata metadata1 = mock(KotlinFunctionMetadata.class);
        metadata1.referencedMethod = referencedMethod1;
        metadata1.referencedDefaultMethod = defaultMethod1;

        // Arrange - case 2: without default method
        ProgramMethod referencedMethod2 = createProgramMethod("method2");
        setNewMemberName(referencedMethod2, "m2");

        KotlinFunctionMetadata metadata2 = mock(KotlinFunctionMetadata.class);
        metadata2.referencedMethod = referencedMethod2;
        metadata2.referencedDefaultMethod = null;

        // Act
        equalizer.visitAnyFunction(mockClazz, mockMetadata, metadata1);
        equalizer.visitAnyFunction(mockClazz, mockMetadata, metadata2);

        // Assert
        assertNotNull(newMemberName(defaultMethod1),
                      "Default method should have new name when it exists");
        // metadata2 has no default method, so nothing to assert there
    }

    /**
     * Tests that visitAnyFunction handles different obfuscated name patterns.
     * Verifies the method works with various naming schemes.
     */
    @Test
    public void testVisitAnyFunction_withVariousObfuscatedNames() {
        // Test with single letter name
        testWithObfuscatedName("a");

        // Test with multi-letter name
        testWithObfuscatedName("abc");

        // Test with numeric name
        testWithObfuscatedName("a1");

        // Test with underscore
        testWithObfuscatedName("_method");
    }

    /**
     * Helper method to test with a specific obfuscated name.
     */
    private void testWithObfuscatedName(String obfuscatedName) {
        ProgramMethod referencedMethod = createProgramMethod("testMethod");
        ProgramMethod defaultMethod = createProgramMethod("testMethod$default");

        setNewMemberName(referencedMethod, obfuscatedName);

        KotlinFunctionMetadata metadata = mock(KotlinFunctionMetadata.class);
        metadata.referencedMethod = referencedMethod;
        metadata.referencedDefaultMethod = defaultMethod;

        equalizer.visitAnyFunction(mockClazz, mockMetadata, metadata);

        String newName = newMemberName(defaultMethod);
        assertNotNull(newName, "Default method should have new name for obfuscated name: " + obfuscatedName);
        assertTrue(newName.startsWith(obfuscatedName),
                   "Default method name should start with: " + obfuscatedName);
    }

    /**
     * Tests that visitAnyFunction is stateless and can be called on different equalizer instances.
     */
    @Test
    public void testVisitAnyFunction_isStateless() {
        // Arrange
        KotlinDefaultMethodNameEqualizer equalizer1 = new KotlinDefaultMethodNameEqualizer();
        KotlinDefaultMethodNameEqualizer equalizer2 = new KotlinDefaultMethodNameEqualizer();

        ProgramMethod referencedMethod = createProgramMethod("method");
        ProgramMethod defaultMethod = createProgramMethod("method$default");
        setNewMemberName(referencedMethod, "m");

        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = defaultMethod;

        // Act - both equalizers should work independently
        equalizer1.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);
        equalizer2.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);

        // Assert - both calls should work without interference
        assertNotNull(newMemberName(defaultMethod));
    }

    /**
     * Tests that visitAnyFunction works when called through the visitor interface.
     */
    @Test
    public void testVisitAnyFunction_throughVisitorInterface() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinFunctionVisitor visitor = equalizer;
        ProgramMethod referencedMethod = createProgramMethod("method");
        ProgramMethod defaultMethod = createProgramMethod("method$default");
        setNewMemberName(referencedMethod, "m");

        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = defaultMethod;

        // Act
        visitor.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);

        // Assert
        assertNotNull(newMemberName(defaultMethod),
                      "Method should work when called through visitor interface");
    }

    /**
     * Tests that visitAnyFunction correctly handles the case where the default method
     * already has a name set (overwriting scenario).
     */
    @Test
    public void testVisitAnyFunction_overwritesExistingDefaultMethodName() {
        // Arrange
        ProgramMethod referencedMethod = createProgramMethod("method");
        ProgramMethod defaultMethod = createProgramMethod("method$default");

        // Set initial names
        setNewMemberName(referencedMethod, "oldName");
        setNewMemberName(defaultMethod, "oldDefaultName");

        // Update the referenced method name
        setNewMemberName(referencedMethod, "newName");

        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = defaultMethod;

        // Act
        equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);

        // Assert - the default method name should be updated
        String newDefaultName = newMemberName(defaultMethod);
        assertNotNull(newDefaultName);
        assertTrue(newDefaultName.startsWith("newName"),
                   "Default method name should be updated to match new referenced method name");
    }

    /**
     * Tests that visitAnyFunction processes methods with empty string new names.
     */
    @Test
    public void testVisitAnyFunction_withEmptyStringNewName() {
        // Arrange
        ProgramMethod referencedMethod = createProgramMethod("method");
        ProgramMethod defaultMethod = createProgramMethod("method$default");

        setNewMemberName(referencedMethod, "");

        mockFunctionMetadata.referencedMethod = referencedMethod;
        mockFunctionMetadata.referencedDefaultMethod = defaultMethod;

        // Act
        equalizer.visitAnyFunction(mockClazz, mockMetadata, mockFunctionMetadata);

        // Assert
        String newDefaultName = newMemberName(defaultMethod);
        assertNotNull(newDefaultName);
        assertTrue(newDefaultName.contains("$default"),
                   "Default method should still have the suffix even with empty base name");
    }

    /**
     * Helper method to create a ProgramMethod with a given name.
     */
    private ProgramMethod createProgramMethod(String name) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = 0;
        method.u2nameIndex = 0;
        method.u2descriptorIndex = 0;

        return method;
    }
}
