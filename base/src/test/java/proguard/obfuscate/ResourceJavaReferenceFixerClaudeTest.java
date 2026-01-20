package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.Clazz;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.ResourceJavaReference;
import proguard.resources.kotlinmodule.KotlinModule;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ResourceJavaReferenceFixer}.
 *
 * Tests all methods:
 * - <init>.()V - Constructor
 * - visitResourceFile.(Lproguard/resources/file/ResourceFile;)V
 * - visitKotlinModule.(Lproguard/resources/kotlinmodule/KotlinModule;)V
 *
 * The ResourceJavaReferenceFixer updates the external class names of Java references
 * in resource files to match their obfuscated names.
 */
public class ResourceJavaReferenceFixerClaudeTest {

    private ResourceJavaReferenceFixer fixer;

    @BeforeEach
    public void setUp() {
        fixer = new ResourceJavaReferenceFixer();
    }

    // ========== Constructor Tests ==========

    /**
     * Tests that the constructor creates a valid instance.
     * The constructor takes no parameters.
     */
    @Test
    public void testConstructor_createsValidInstance() {
        // Act
        ResourceJavaReferenceFixer newFixer = new ResourceJavaReferenceFixer();

        // Assert
        assertNotNull(newFixer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that multiple constructor calls create independent instances.
     */
    @Test
    public void testConstructor_createsIndependentInstances() {
        // Act
        ResourceJavaReferenceFixer fixer1 = new ResourceJavaReferenceFixer();
        ResourceJavaReferenceFixer fixer2 = new ResourceJavaReferenceFixer();

        // Assert
        assertNotSame(fixer1, fixer2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that the constructor completes quickly.
     */
    @Test
    public void testConstructor_isEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        ResourceJavaReferenceFixer newFixer = new ResourceJavaReferenceFixer();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(newFixer, "Fixer should be created");
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that multiple instances can be created without issues.
     */
    @Test
    public void testConstructor_canBeCalledMultipleTimes() {
        // Act & Assert
        for (int i = 0; i < 100; i++) {
            ResourceJavaReferenceFixer newFixer = new ResourceJavaReferenceFixer();
            assertNotNull(newFixer, "Fixer " + i + " should be created successfully");
        }
    }

    // ========== visitResourceFile Tests ==========

    /**
     * Tests visitResourceFile with null references.
     * When references is null, the method should complete without error.
     */
    @Test
    public void testVisitResourceFile_withNullReferences_doesNotThrow() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        resourceFile.references = null;

        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitResourceFile(resourceFile));
    }

    /**
     * Tests visitResourceFile with empty references set.
     * When references is empty, the method should complete without error.
     */
    @Test
    public void testVisitResourceFile_withEmptyReferences_doesNotThrow() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        resourceFile.references = new HashSet<>();

        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitResourceFile(resourceFile));
    }

    /**
     * Tests visitResourceFile with a reference that has null referencedClass.
     * When referencedClass is null, the external class name should not be updated.
     */
    @Test
    public void testVisitResourceFile_withNullReferencedClass_doesNotUpdate() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        ResourceJavaReference reference = mock(ResourceJavaReference.class);
        reference.referencedClass = null;
        reference.externalClassName = "OriginalName";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(reference);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("OriginalName", reference.externalClassName,
            "External class name should not be updated when referencedClass is null");
    }

    /**
     * Tests visitResourceFile with a single reference that has a valid referencedClass.
     * The external class name should be updated to match the obfuscated name.
     */
    @Test
    public void testVisitResourceFile_withValidReference_updatesExternalClassName() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("com/example/ObfuscatedClass");

        ResourceJavaReference reference = mock(ResourceJavaReference.class);
        reference.referencedClass = referencedClass;
        reference.externalClassName = "com.example.OriginalClass";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(reference);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("com.example.ObfuscatedClass", reference.externalClassName,
            "External class name should be updated to match obfuscated name");
        verify(referencedClass).getName();
    }

    /**
     * Tests visitResourceFile with multiple references.
     * All references with valid referencedClass should be updated.
     */
    @Test
    public void testVisitResourceFile_withMultipleReferences_updatesAll() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);

        Clazz class1 = mock(Clazz.class);
        when(class1.getName()).thenReturn("a/b/C");
        ResourceJavaReference ref1 = mock(ResourceJavaReference.class);
        ref1.referencedClass = class1;
        ref1.externalClassName = "com.example.Class1";

        Clazz class2 = mock(Clazz.class);
        when(class2.getName()).thenReturn("x/y/Z");
        ResourceJavaReference ref2 = mock(ResourceJavaReference.class);
        ref2.referencedClass = class2;
        ref2.externalClassName = "com.example.Class2";

        Clazz class3 = mock(Clazz.class);
        when(class3.getName()).thenReturn("p/q/R");
        ResourceJavaReference ref3 = mock(ResourceJavaReference.class);
        ref3.referencedClass = class3;
        ref3.externalClassName = "com.example.Class3";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(ref1);
        references.add(ref2);
        references.add(ref3);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("a.b.C", ref1.externalClassName,
            "First reference should be updated");
        assertEquals("x.y.Z", ref2.externalClassName,
            "Second reference should be updated");
        assertEquals("p.q.R", ref3.externalClassName,
            "Third reference should be updated");

        verify(class1).getName();
        verify(class2).getName();
        verify(class3).getName();
    }

    /**
     * Tests visitResourceFile with mixed references (some with null referencedClass).
     * Only references with valid referencedClass should be updated.
     */
    @Test
    public void testVisitResourceFile_withMixedReferences_updatesOnlyValid() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);

        Clazz validClass = mock(Clazz.class);
        when(validClass.getName()).thenReturn("a/b/ValidClass");
        ResourceJavaReference validRef = mock(ResourceJavaReference.class);
        validRef.referencedClass = validClass;
        validRef.externalClassName = "com.example.Valid";

        ResourceJavaReference nullRef = mock(ResourceJavaReference.class);
        nullRef.referencedClass = null;
        nullRef.externalClassName = "com.example.Null";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(validRef);
        references.add(nullRef);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("a.b.ValidClass", validRef.externalClassName,
            "Valid reference should be updated");
        assertEquals("com.example.Null", nullRef.externalClassName,
            "Null reference should not be updated");
    }

    /**
     * Tests visitResourceFile with class names containing inner classes.
     * ClassUtil.externalClassName converts / to . but preserves $.
     */
    @Test
    public void testVisitResourceFile_withInnerClass_convertsCorrectly() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("com/example/Outer$Inner");

        ResourceJavaReference reference = mock(ResourceJavaReference.class);
        reference.referencedClass = referencedClass;
        reference.externalClassName = "com.example.Outer.OldInner";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(reference);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("com.example.Outer$Inner", reference.externalClassName,
            "Inner class name should convert / to . and preserve $");
    }

    /**
     * Tests visitResourceFile with simple class name (no package).
     */
    @Test
    public void testVisitResourceFile_withSimpleClassName_updatesCorrectly() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("SimpleClass");

        ResourceJavaReference reference = mock(ResourceJavaReference.class);
        reference.referencedClass = referencedClass;
        reference.externalClassName = "OldSimpleClass";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(reference);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("SimpleClass", reference.externalClassName,
            "Simple class name should be updated");
    }

    /**
     * Tests visitResourceFile with deeply nested package.
     */
    @Test
    public void testVisitResourceFile_withDeeplyNestedPackage_updatesCorrectly() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("com/example/deep/nested/package/structure/MyClass");

        ResourceJavaReference reference = mock(ResourceJavaReference.class);
        reference.referencedClass = referencedClass;
        reference.externalClassName = "com.example.OldClass";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(reference);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("com.example.deep.nested.package.structure.MyClass",
            reference.externalClassName,
            "Deeply nested class name should be converted correctly");
    }

    /**
     * Tests visitResourceFile can be called multiple times on the same fixer.
     */
    @Test
    public void testVisitResourceFile_canBeCalledMultipleTimes() {
        // Arrange
        ResourceFile file1 = mock(ResourceFile.class);
        ResourceFile file2 = mock(ResourceFile.class);
        ResourceFile file3 = mock(ResourceFile.class);

        file1.references = new HashSet<>();
        file2.references = new HashSet<>();
        file3.references = new HashSet<>();

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitResourceFile(file1);
            fixer.visitResourceFile(file2);
            fixer.visitResourceFile(file3);
        });
    }

    /**
     * Tests visitResourceFile with null ResourceFile throws NullPointerException.
     */
    @Test
    public void testVisitResourceFile_withNullFile_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> fixer.visitResourceFile(null));
    }

    /**
     * Tests visitResourceFile is stateless across invocations.
     */
    @Test
    public void testVisitResourceFile_statelessBehavior() {
        // Arrange
        ResourceFile file1 = mock(ResourceFile.class);
        Clazz class1 = mock(Clazz.class);
        when(class1.getName()).thenReturn("a/A");
        ResourceJavaReference ref1 = mock(ResourceJavaReference.class);
        ref1.referencedClass = class1;
        ref1.externalClassName = "original.A";
        Set<ResourceJavaReference> refs1 = new HashSet<>();
        refs1.add(ref1);
        file1.references = refs1;

        ResourceFile file2 = mock(ResourceFile.class);
        Clazz class2 = mock(Clazz.class);
        when(class2.getName()).thenReturn("b/B");
        ResourceJavaReference ref2 = mock(ResourceJavaReference.class);
        ref2.referencedClass = class2;
        ref2.externalClassName = "original.B";
        Set<ResourceJavaReference> refs2 = new HashSet<>();
        refs2.add(ref2);
        file2.references = refs2;

        // Act
        fixer.visitResourceFile(file1);
        fixer.visitResourceFile(file2);

        // Assert - each call should be independent
        assertEquals("a.A", ref1.externalClassName, "First file reference should be updated");
        assertEquals("b.B", ref2.externalClassName, "Second file reference should be updated");
    }

    /**
     * Tests visitResourceFile with obfuscated single-letter class names.
     */
    @Test
    public void testVisitResourceFile_withObfuscatedNames_updatesCorrectly() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("a");

        ResourceJavaReference reference = mock(ResourceJavaReference.class);
        reference.referencedClass = referencedClass;
        reference.externalClassName = "com.example.LongOriginalClassName";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(reference);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert
        assertEquals("a", reference.externalClassName,
            "Obfuscated single-letter name should be set correctly");
    }

    /**
     * Tests visitResourceFile updates the same reference only once per call.
     */
    @Test
    public void testVisitResourceFile_updatesReferenceOnce() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        Clazz referencedClass = mock(Clazz.class);
        when(referencedClass.getName()).thenReturn("a/b/C");

        ResourceJavaReference reference = mock(ResourceJavaReference.class);
        reference.referencedClass = referencedClass;
        reference.externalClassName = "original";

        Set<ResourceJavaReference> references = new HashSet<>();
        references.add(reference);
        resourceFile.references = references;

        // Act
        fixer.visitResourceFile(resourceFile);

        // Assert - getName should be called once
        verify(referencedClass, times(1)).getName();
        assertEquals("a.b.C", reference.externalClassName);
    }

    // ========== visitKotlinModule Tests ==========

    /**
     * Tests visitKotlinModule with valid KotlinModule.
     * The method is a no-op, so it should complete without error.
     */
    @Test
    public void testVisitKotlinModule_withValidModule_doesNotThrow() {
        // Arrange
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitKotlinModule(kotlinModule));
    }

    /**
     * Tests visitKotlinModule with null KotlinModule.
     * The method is a no-op, so it should complete without error even with null.
     */
    @Test
    public void testVisitKotlinModule_withNull_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitKotlinModule(null));
    }

    /**
     * Tests visitKotlinModule can be called multiple times.
     */
    @Test
    public void testVisitKotlinModule_canBeCalledMultipleTimes() {
        // Arrange
        KotlinModule module1 = mock(KotlinModule.class);
        KotlinModule module2 = mock(KotlinModule.class);
        KotlinModule module3 = mock(KotlinModule.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitKotlinModule(module1);
            fixer.visitKotlinModule(module2);
            fixer.visitKotlinModule(module3);
        });
    }

    /**
     * Tests visitKotlinModule does not interact with the module.
     * Since it's a no-op, the module should not be accessed.
     */
    @Test
    public void testVisitKotlinModule_doesNotInteractWithModule() {
        // Arrange
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        fixer.visitKotlinModule(kotlinModule);

        // Assert - verify no interactions with the module
        verifyNoInteractions(kotlinModule);
    }

    /**
     * Tests visitKotlinModule is stateless.
     */
    @Test
    public void testVisitKotlinModule_statelessBehavior() {
        // Arrange
        KotlinModule module1 = mock(KotlinModule.class);
        KotlinModule module2 = mock(KotlinModule.class);

        // Act
        fixer.visitKotlinModule(module1);
        fixer.visitKotlinModule(module2);

        // Assert - each call should be independent (no state maintained)
        verifyNoInteractions(module1);
        verifyNoInteractions(module2);
    }

    /**
     * Tests that visitKotlinModule completes quickly.
     */
    @Test
    public void testVisitKotlinModule_isEfficient() {
        // Arrange
        KotlinModule kotlinModule = mock(KotlinModule.class);
        long startTime = System.nanoTime();

        // Act
        fixer.visitKotlinModule(kotlinModule);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 1_000_000L,
            "visitKotlinModule should complete very quickly (took " + duration + " ns)");
    }

    /**
     * Tests visitKotlinModule after visitResourceFile.
     * Both methods should work independently.
     */
    @Test
    public void testVisitKotlinModule_afterVisitResourceFile() {
        // Arrange
        ResourceFile resourceFile = mock(ResourceFile.class);
        resourceFile.references = new HashSet<>();
        KotlinModule kotlinModule = mock(KotlinModule.class);

        // Act
        fixer.visitResourceFile(resourceFile);
        fixer.visitKotlinModule(kotlinModule);

        // Assert - both should complete without error
        verifyNoInteractions(kotlinModule);
    }

    /**
     * Tests visitResourceFile after visitKotlinModule.
     * Both methods should work independently in any order.
     */
    @Test
    public void testVisitResourceFile_afterVisitKotlinModule() {
        // Arrange
        KotlinModule kotlinModule = mock(KotlinModule.class);
        ResourceFile resourceFile = mock(ResourceFile.class);
        resourceFile.references = new HashSet<>();

        // Act & Assert
        assertDoesNotThrow(() -> {
            fixer.visitKotlinModule(kotlinModule);
            fixer.visitResourceFile(resourceFile);
        });
    }

    // ========== Integration Tests ==========

    /**
     * Tests that the fixer can be used in a visitor pattern.
     * Simulates visiting multiple resource files in sequence.
     */
    @Test
    public void testFixer_visitorPatternUsage() {
        // Arrange
        ResourceFile file1 = mock(ResourceFile.class);
        Clazz class1 = mock(Clazz.class);
        when(class1.getName()).thenReturn("a/ClassA");
        ResourceJavaReference ref1 = mock(ResourceJavaReference.class);
        ref1.referencedClass = class1;
        ref1.externalClassName = "com.example.ClassA";
        Set<ResourceJavaReference> refs1 = new HashSet<>();
        refs1.add(ref1);
        file1.references = refs1;

        ResourceFile file2 = mock(ResourceFile.class);
        Clazz class2 = mock(Clazz.class);
        when(class2.getName()).thenReturn("b/ClassB");
        ResourceJavaReference ref2 = mock(ResourceJavaReference.class);
        ref2.referencedClass = class2;
        ref2.externalClassName = "com.example.ClassB";
        Set<ResourceJavaReference> refs2 = new HashSet<>();
        refs2.add(ref2);
        file2.references = refs2;

        KotlinModule module = mock(KotlinModule.class);

        // Act - simulate visitor pattern
        fixer.visitResourceFile(file1);
        fixer.visitKotlinModule(module);
        fixer.visitResourceFile(file2);

        // Assert
        assertEquals("a.ClassA", ref1.externalClassName, "First file reference should be updated");
        assertEquals("b.ClassB", ref2.externalClassName, "Second file reference should be updated");
        verifyNoInteractions(module);
    }

    /**
     * Tests the complete workflow with mixed content.
     */
    @Test
    public void testFixer_completeWorkflow() {
        // Arrange - create multiple resources with different scenarios
        ResourceFile file1 = mock(ResourceFile.class);
        file1.references = null; // null references

        ResourceFile file2 = mock(ResourceFile.class);
        file2.references = new HashSet<>(); // empty references

        ResourceFile file3 = mock(ResourceFile.class);
        Clazz validClass = mock(Clazz.class);
        when(validClass.getName()).thenReturn("obfuscated/a");
        ResourceJavaReference validRef = mock(ResourceJavaReference.class);
        validRef.referencedClass = validClass;
        validRef.externalClassName = "com.original.ClassName";
        Set<ResourceJavaReference> refs3 = new HashSet<>();
        refs3.add(validRef);
        file3.references = refs3;

        KotlinModule module = mock(KotlinModule.class);

        // Act - process all resources
        assertDoesNotThrow(() -> {
            fixer.visitResourceFile(file1);
            fixer.visitResourceFile(file2);
            fixer.visitKotlinModule(module);
            fixer.visitResourceFile(file3);
        });

        // Assert
        assertEquals("obfuscated.a", validRef.externalClassName,
            "Valid reference should be updated to obfuscated name");
    }
}
