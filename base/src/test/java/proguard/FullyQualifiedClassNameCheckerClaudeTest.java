package proguard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.util.WarningPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link FullyQualifiedClassNameChecker}.
 * Tests constructor, checkClassSpecifications, and visitAnyClass methods.
 */
public class FullyQualifiedClassNameCheckerClaudeTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private FullyQualifiedClassNameChecker checker;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        checker = new FullyQualifiedClassNameChecker(programClassPool, libraryClassPool, warningPrinter);
    }

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor with valid non-null parameters.
     */
    @Test
    public void testConstructor_withValidParameters() {
        // Act
        FullyQualifiedClassNameChecker newChecker = new FullyQualifiedClassNameChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated successfully");
    }

    /**
     * Tests the constructor with all null parameters.
     */
    @Test
    public void testConstructor_withAllNullParameters() {
        // Act
        FullyQualifiedClassNameChecker newChecker = new FullyQualifiedClassNameChecker(null, null, null);

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated with null parameters");
    }

    /**
     * Tests the constructor with null program class pool.
     */
    @Test
    public void testConstructor_withNullProgramClassPool() {
        // Act
        FullyQualifiedClassNameChecker newChecker = new FullyQualifiedClassNameChecker(
                null,
                libraryClassPool,
                warningPrinter
        );

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated with null program class pool");
    }

    /**
     * Tests the constructor with null library class pool.
     */
    @Test
    public void testConstructor_withNullLibraryClassPool() {
        // Act
        FullyQualifiedClassNameChecker newChecker = new FullyQualifiedClassNameChecker(
                programClassPool,
                null,
                warningPrinter
        );

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated with null library class pool");
    }

    /**
     * Tests the constructor with null warning printer.
     */
    @Test
    public void testConstructor_withNullWarningPrinter() {
        // Act
        FullyQualifiedClassNameChecker newChecker = new FullyQualifiedClassNameChecker(
                programClassPool,
                libraryClassPool,
                null
        );

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated with null warning printer");
    }

    /**
     * Tests the constructor with empty class pools.
     */
    @Test
    public void testConstructor_withEmptyClassPools() {
        // Arrange
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();

        // Act
        FullyQualifiedClassNameChecker newChecker = new FullyQualifiedClassNameChecker(
                emptyProgramPool,
                emptyLibraryPool,
                warningPrinter
        );

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated with empty class pools");
    }

    /**
     * Tests the constructor with same class pool for both parameters.
     */
    @Test
    public void testConstructor_withSameClassPoolForBoth() {
        // Arrange
        ClassPool singlePool = new ClassPool();

        // Act
        FullyQualifiedClassNameChecker newChecker = new FullyQualifiedClassNameChecker(
                singlePool,
                singlePool,
                warningPrinter
        );

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated with same pool for both parameters");
    }

    /**
     * Tests that the checker implements ClassVisitor interface.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Assert
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, checker,
                "Checker should implement ClassVisitor interface");
    }

    // ========== checkClassSpecifications Tests ==========

    /**
     * Tests checkClassSpecifications with null list.
     */
    @Test
    public void testCheckClassSpecifications_withNullList() {
        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(null));
    }

    /**
     * Tests checkClassSpecifications with empty list.
     */
    @Test
    public void testCheckClassSpecifications_withEmptyList() {
        // Arrange
        List<ClassSpecification> emptyList = new ArrayList<>();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(emptyList));
    }

    /**
     * Tests checkClassSpecifications with a single class specification with null class name.
     */
    @Test
    public void testCheckClassSpecifications_withNullClassName() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with a class that exists in program pool.
     */
    @Test
    public void testCheckClassSpecifications_withExistingClassInProgramPool() {
        // Arrange
        ProgramClass existingClass = mock(ProgramClass.class);
        when(existingClass.getName()).thenReturn("com/example/ExistingClass");
        programClassPool.addClass(existingClass);

        ClassSpecification spec = new ClassSpecification(
                null, 0, 0, null, "com/example/ExistingClass", null, null
        );
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act
        checker.checkClassSpecifications(specs);

        // Assert - no warning should be printed for existing class
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests checkClassSpecifications with a class that exists in library pool.
     */
    @Test
    public void testCheckClassSpecifications_withExistingClassInLibraryPool() {
        // Arrange
        ProgramClass existingClass = mock(ProgramClass.class);
        when(existingClass.getName()).thenReturn("java/lang/String");
        libraryClassPool.addClass(existingClass);

        ClassSpecification spec = new ClassSpecification(
                null, 0, 0, null, "java/lang/String", null, null
        );
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act
        checker.checkClassSpecifications(specs);

        // Assert - no warning should be printed for existing class
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests checkClassSpecifications with a class name that contains wildcards.
     * Wildcard class names should not trigger warnings.
     */
    @Test
    public void testCheckClassSpecifications_withWildcardClassName() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification(
                null, 0, 0, null, "com/example/*", null, null
        );
        ClassSpecification spec2 = new ClassSpecification(
                null, 0, 0, null, "com/example/**", null, null
        );
        ClassSpecification spec3 = new ClassSpecification(
                null, 0, 0, null, "com/example/Test?", null, null
        );
        List<ClassSpecification> specs = Arrays.asList(spec1, spec2, spec3);

        // Act
        checker.checkClassSpecifications(specs);

        // Assert - no warnings should be printed for wildcard class names
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests checkClassSpecifications with an unknown class.
     * Should trigger warning for unknown class.
     */
    @Test
    public void testCheckClassSpecifications_withUnknownClass() {
        // Arrange
        when(warningPrinter.accepts(anyString())).thenReturn(true);

        ClassSpecification spec = new ClassSpecification(
                null, 0, 0, null, "com/example/UnknownClass", null, null
        );
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act
        checker.checkClassSpecifications(specs);

        // Assert - warning should be printed for unknown class
        verify(warningPrinter, atLeastOnce()).accepts(anyString());
    }

    /**
     * Tests checkClassSpecifications with annotation types.
     */
    @Test
    public void testCheckClassSpecifications_withAnnotationType() {
        // Arrange
        ClassSpecification spec = new ClassSpecification(
                null, 0, 0, "Ljava/lang/Override;", "com/example/MyClass", null, null
        );
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with extends class name.
     */
    @Test
    public void testCheckClassSpecifications_withExtendsClassName() {
        // Arrange
        ClassSpecification spec = new ClassSpecification(
                null, 0, 0, null, null, null, "java/lang/Object"
        );
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with extends annotation type.
     */
    @Test
    public void testCheckClassSpecifications_withExtendsAnnotationType() {
        // Arrange
        ClassSpecification spec = new ClassSpecification(
                null, 0, 0, null, null, "Ljava/lang/annotation/Retention;", null
        );
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with field specifications.
     */
    @Test
    public void testCheckClassSpecifications_withFieldSpecifications() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification fieldSpec = new MemberSpecification(0, 0, null, "myField", "Ljava/lang/String;");
        spec.addField(fieldSpec);
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with method specifications.
     */
    @Test
    public void testCheckClassSpecifications_withMethodSpecifications() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "myMethod", "(Ljava/lang/String;)V"
        );
        spec.addMethod(methodSpec);
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with multiple class specifications.
     */
    @Test
    public void testCheckClassSpecifications_withMultipleSpecifications() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification(null, 0, 0, null, null, null, null);
        ClassSpecification spec2 = new ClassSpecification(null, 0, 0, null, "**", null, null);
        ClassSpecification spec3 = new ClassSpecification(null, 0, 0, null, null, null, "java/lang/Object");
        List<ClassSpecification> specs = Arrays.asList(spec1, spec2, spec3);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with complex descriptors in method specifications.
     */
    @Test
    public void testCheckClassSpecifications_withComplexMethodDescriptor() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "complexMethod",
                "(Ljava/lang/String;ILjava/util/List;)Ljava/lang/Object;"
        );
        spec.addMethod(methodSpec);
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with both field and method specifications.
     */
    @Test
    public void testCheckClassSpecifications_withBothFieldAndMethodSpecs() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification fieldSpec = new MemberSpecification(0, 0, null, "field", "I");
        MemberSpecification methodSpec = new MemberSpecification(0, 0, null, "method", "()V");
        spec.addField(fieldSpec);
        spec.addMethod(methodSpec);
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with member specification having null descriptor.
     */
    @Test
    public void testCheckClassSpecifications_withNullDescriptor() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification fieldSpec = new MemberSpecification(0, 0, null, "field", null);
        spec.addField(fieldSpec);
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications with member specification having wildcard descriptor.
     */
    @Test
    public void testCheckClassSpecifications_withWildcardDescriptor() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification fieldSpec = new MemberSpecification(0, 0, null, "field", "*");
        spec.addField(fieldSpec);
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    /**
     * Tests checkClassSpecifications called multiple times.
     */
    @Test
    public void testCheckClassSpecifications_calledMultipleTimes() {
        // Arrange
        List<ClassSpecification> specs = Arrays.asList(new ClassSpecification());

        // Act & Assert - should not throw exception when called multiple times
        assertDoesNotThrow(() -> {
            checker.checkClassSpecifications(specs);
            checker.checkClassSpecifications(specs);
            checker.checkClassSpecifications(specs);
        });
    }

    /**
     * Tests checkClassSpecifications with all special wildcard characters.
     */
    @Test
    public void testCheckClassSpecifications_withAllWildcardCharacters() {
        // Arrange - testing !, *, ?, ,, ///, <
        ClassSpecification spec1 = new ClassSpecification(null, 0, 0, null, "!com/example/Class", null, null);
        ClassSpecification spec2 = new ClassSpecification(null, 0, 0, null, "com,example,Class", null, null);
        ClassSpecification spec3 = new ClassSpecification(null, 0, 0, null, "com///example", null, null);
        ClassSpecification spec4 = new ClassSpecification(null, 0, 0, null, "com/example/<Class>", null, null);
        List<ClassSpecification> specs = Arrays.asList(spec1, spec2, spec3, spec4);

        // Act
        checker.checkClassSpecifications(specs);

        // Assert - no warnings for wildcard patterns
        verify(warningPrinter, never()).print(anyString(), anyString());
    }

    /**
     * Tests checkClassSpecifications with primitive types in descriptors.
     */
    @Test
    public void testCheckClassSpecifications_withPrimitiveTypeDescriptors() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification fieldSpec1 = new MemberSpecification(0, 0, null, "intField", "I");
        MemberSpecification fieldSpec2 = new MemberSpecification(0, 0, null, "boolField", "Z");
        MemberSpecification methodSpec = new MemberSpecification(0, 0, null, "method", "(IJZ)V");
        spec.addField(fieldSpec1);
        spec.addField(fieldSpec2);
        spec.addMethod(methodSpec);
        List<ClassSpecification> specs = Arrays.asList(spec);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specs));
    }

    // ========== visitAnyClass Tests ==========

    /**
     * Tests visitAnyClass with a valid mock clazz.
     * The method logs a suggestion for the fully qualified name.
     */
    @Test
    public void testVisitAnyClass_withValidMock() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/SuggestedClass");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with null clazz.
     * The method should handle null gracefully (may cause NullPointerException when accessing getName).
     */
    @Test
    public void testVisitAnyClass_withNullClazz() {
        // Act & Assert - may throw NPE when trying to get name from null
        assertThrows(NullPointerException.class, () -> checker.visitAnyClass(null));
    }

    /**
     * Tests visitAnyClass called multiple times with same clazz.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/TestClass");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(clazz);
            checker.visitAnyClass(clazz);
            checker.visitAnyClass(clazz);
        });
    }

    /**
     * Tests visitAnyClass with different clazz objects.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        when(clazz1.getName()).thenReturn("com/example/Class1");

        Clazz clazz2 = mock(Clazz.class);
        when(clazz2.getName()).thenReturn("com/example/Class2");

        Clazz clazz3 = mock(Clazz.class);
        when(clazz3.getName()).thenReturn("com/example/Class3");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(clazz1);
            checker.visitAnyClass(clazz2);
            checker.visitAnyClass(clazz3);
        });
    }

    /**
     * Tests visitAnyClass with clazz having empty name.
     */
    @Test
    public void testVisitAnyClass_withEmptyName() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with clazz having null name.
     */
    @Test
    public void testVisitAnyClass_withNullName() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn(null);

        // Act & Assert - may throw NPE when processing null name
        assertThrows(NullPointerException.class, () -> checker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with various package structures.
     */
    @Test
    public void testVisitAnyClass_withVariousPackageStructures() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        when(clazz1.getName()).thenReturn("SimpleClass");

        Clazz clazz2 = mock(Clazz.class);
        when(clazz2.getName()).thenReturn("com/example/DeepPackage");

        Clazz clazz3 = mock(Clazz.class);
        when(clazz3.getName()).thenReturn("com/example/deep/nested/VeryDeepClass");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(clazz1);
            checker.visitAnyClass(clazz2);
            checker.visitAnyClass(clazz3);
        });
    }

    /**
     * Tests visitAnyClass on checker with null class pools.
     */
    @Test
    public void testVisitAnyClass_withNullClassPools() {
        // Arrange
        FullyQualifiedClassNameChecker nullPoolChecker =
                new FullyQualifiedClassNameChecker(null, null, warningPrinter);
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/TestClass");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> nullPoolChecker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass on checker with null warning printer.
     */
    @Test
    public void testVisitAnyClass_withNullWarningPrinter() {
        // Arrange
        FullyQualifiedClassNameChecker nullPrinterChecker =
                new FullyQualifiedClassNameChecker(programClassPool, libraryClassPool, null);
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/TestClass");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> nullPrinterChecker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with inner class name format.
     */
    @Test
    public void testVisitAnyClass_withInnerClassName() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/OuterClass$InnerClass");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with special characters in name.
     */
    @Test
    public void testVisitAnyClass_withSpecialCharactersInName() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/Class_123$Special");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass retrieves name from clazz.
     */
    @Test
    public void testVisitAnyClass_retrievesNameFromClazz() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/TestClass");

        // Act
        checker.visitAnyClass(clazz);

        // Assert - verify getName was called
        verify(clazz, atLeastOnce()).getName();
    }
}
