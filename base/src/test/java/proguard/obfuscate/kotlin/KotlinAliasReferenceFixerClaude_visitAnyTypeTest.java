package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinTypeAliasMetadata;
import proguard.classfile.kotlin.KotlinTypeMetadata;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAliasReferenceFixer#visitAnyType(Clazz, KotlinTypeMetadata)}.
 * Tests the visitAnyType method which fixes alias references in Kotlin metadata.
 */
public class KotlinAliasReferenceFixerClaude_visitAnyTypeTest {

    private KotlinAliasReferenceFixer fixer;
    private Clazz mockClazz;
    private KotlinTypeMetadata mockKotlinTypeMetadata;

    @BeforeEach
    public void setUp() {
        fixer = new KotlinAliasReferenceFixer();
        mockClazz = mock(Clazz.class);
        mockKotlinTypeMetadata = mock(KotlinTypeMetadata.class);
    }

    /**
     * Tests that visitAnyType does nothing when aliasName is null.
     * The method should exit early if kotlinTypeMetadata.aliasName is null.
     */
    @Test
    public void testVisitAnyType_withNullAliasName_doesNothing() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = null;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert - aliasName should remain null
        assertNull(mockKotlinTypeMetadata.aliasName, "aliasName should remain null");
    }

    /**
     * Tests that visitAnyType updates aliasName for type alias declared within a class.
     * When referencedDeclarationContainer.k == METADATA_KIND_CLASS, it should use '.' separator.
     */
    @Test
    public void testVisitAnyType_withClassContainerKind_updatesAliasName() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "NewAliasName";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/example/OuterClass";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/example/OuterClass.NewAliasName", mockKotlinTypeMetadata.aliasName,
                "aliasName should be updated with class name and '.' separator");
    }

    /**
     * Tests that visitAnyType updates aliasName for top-level type alias.
     * When referencedDeclarationContainer.k != METADATA_KIND_CLASS, it should use package prefix.
     */
    @Test
    public void testVisitAnyType_withNonClassContainerKind_updatesAliasName() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "TopLevelAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinDeclarationContainerMetadata mockContainer = mock(KotlinDeclarationContainerMetadata.class);
        mockContainer.ownerClassName = "com/example/package/FileFacadeKt";
        mockContainer.k = KotlinConstants.METADATA_KIND_FILE_FACADE;
        mockTypeAlias.referencedDeclarationContainer = mockContainer;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/example/package/TopLevelAlias", mockKotlinTypeMetadata.aliasName,
                "aliasName should be updated with package prefix");
    }

    /**
     * Tests that visitAnyType handles empty package (default package).
     * When owner class is in default package, the alias name should just be the alias name.
     */
    @Test
    public void testVisitAnyType_withDefaultPackage_updatesAliasName() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "MyAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinDeclarationContainerMetadata mockContainer = mock(KotlinDeclarationContainerMetadata.class);
        mockContainer.ownerClassName = "FileFacadeKt";
        mockContainer.k = KotlinConstants.METADATA_KIND_FILE_FACADE;
        mockTypeAlias.referencedDeclarationContainer = mockContainer;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("MyAlias", mockKotlinTypeMetadata.aliasName,
                "aliasName should be just the alias name when in default package");
    }

    /**
     * Tests that visitAnyType handles nested class declaration container.
     * Nested classes should use '.' separator.
     */
    @Test
    public void testVisitAnyType_withNestedClass_updatesAliasName() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "InnerAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/example/Outer$Inner";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/example/Outer$Inner.InnerAlias", mockKotlinTypeMetadata.aliasName,
                "aliasName should include nested class path with '.' separator");
    }

    /**
     * Tests that visitAnyType can be called multiple times with same metadata.
     * Each call should update the aliasName to the same value.
     */
    @Test
    public void testVisitAnyType_multipleCallsWithSameMetadata_updatesConsistently() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "InitialAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "ConsistentAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/example/MyClass";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);
        String firstResult = mockKotlinTypeMetadata.aliasName;

        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);
        String secondResult = mockKotlinTypeMetadata.aliasName;

        // Assert
        assertEquals("com/example/MyClass.ConsistentAlias", firstResult,
                "First call should update aliasName correctly");
        assertEquals(firstResult, secondResult,
                "Multiple calls should produce consistent results");
    }

    /**
     * Tests that visitAnyType handles different Clazz instances.
     * The Clazz parameter should not affect the behavior.
     */
    @Test
    public void testVisitAnyType_withDifferentClazz_behaviorUnchanged() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        mockKotlinTypeMetadata.aliasName = "TestAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "FixedAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/test/TestClass";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(clazz1, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/test/TestClass.FixedAlias", mockKotlinTypeMetadata.aliasName,
                "Clazz parameter should not affect the result");
    }

    /**
     * Tests that visitAnyType with null Clazz still works.
     * The Clazz parameter is not used in the method logic.
     */
    @Test
    public void testVisitAnyType_withNullClazz_stillWorks() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "TestAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "WorkingAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/test/NullClazzTest";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> fixer.visitAnyType(null, mockKotlinTypeMetadata),
                "Should work with null Clazz");

        assertEquals("com/test/NullClazzTest.WorkingAlias", mockKotlinTypeMetadata.aliasName,
                "aliasName should be updated even with null Clazz");
    }

    /**
     * Tests that visitAnyType handles deeply nested package structures.
     * Package prefix extraction should work with deep package hierarchies.
     */
    @Test
    public void testVisitAnyType_withDeeplyNestedPackage_extractsCorrectPrefix() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "DeepAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinDeclarationContainerMetadata mockContainer = mock(KotlinDeclarationContainerMetadata.class);
        mockContainer.ownerClassName = "com/company/project/module/submodule/FileFacadeKt";
        mockContainer.k = KotlinConstants.METADATA_KIND_FILE_FACADE;
        mockTypeAlias.referencedDeclarationContainer = mockContainer;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/company/project/module/submodule/DeepAlias", mockKotlinTypeMetadata.aliasName,
                "aliasName should include full package path");
    }

    /**
     * Tests that visitAnyType handles single character alias names.
     * Short alias names should be handled correctly.
     */
    @Test
    public void testVisitAnyType_withSingleCharacterAliasName_updatesCorrectly() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "A";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/example/TestClass";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/example/TestClass.A", mockKotlinTypeMetadata.aliasName,
                "Single character alias names should be handled correctly");
    }

    /**
     * Tests that visitAnyType preserves special characters in alias names.
     * Alias names with underscores, dollars, etc. should be preserved.
     */
    @Test
    public void testVisitAnyType_withSpecialCharactersInAliasName_preservesThem() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "My_Special$Alias123";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/example/SpecialClass";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/example/SpecialClass.My_Special$Alias123", mockKotlinTypeMetadata.aliasName,
                "Special characters in alias names should be preserved");
    }

    /**
     * Tests that visitAnyType handles empty alias name.
     * While unusual, empty alias names should be handled without errors.
     */
    @Test
    public void testVisitAnyType_withEmptyAliasName_handlesCorrectly() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/example/EmptyClass";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/example/EmptyClass.", mockKotlinTypeMetadata.aliasName,
                "Empty alias name should result in class name with trailing dot");
    }

    /**
     * Tests that visitAnyType distinguishes between class and file facade kinds.
     * METADATA_KIND_CLASS should use className directly, while others use package prefix.
     */
    @Test
    public void testVisitAnyType_distinguishesBetweenClassAndFileFacadeKinds() {
        // Arrange - First test with CLASS kind
        KotlinTypeMetadata typeMetadata1 = mock(KotlinTypeMetadata.class);
        typeMetadata1.aliasName = "Alias1";

        KotlinTypeAliasMetadata typeAlias1 = mock(KotlinTypeAliasMetadata.class);
        typeAlias1.name = "TestAlias";
        typeMetadata1.referencedTypeAlias = typeAlias1;

        KotlinClassKindMetadata classMetadata = mock(KotlinClassKindMetadata.class);
        classMetadata.className = "com/example/TestClassKt";
        classMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        typeAlias1.referencedDeclarationContainer = classMetadata;

        // Arrange - Second test with FILE_FACADE kind
        KotlinTypeMetadata typeMetadata2 = mock(KotlinTypeMetadata.class);
        typeMetadata2.aliasName = "Alias2";

        KotlinTypeAliasMetadata typeAlias2 = mock(KotlinTypeAliasMetadata.class);
        typeAlias2.name = "TestAlias";
        typeMetadata2.referencedTypeAlias = typeAlias2;

        KotlinDeclarationContainerMetadata facadeMetadata = mock(KotlinDeclarationContainerMetadata.class);
        facadeMetadata.ownerClassName = "com/example/TestClassKt";
        facadeMetadata.k = KotlinConstants.METADATA_KIND_FILE_FACADE;
        typeAlias2.referencedDeclarationContainer = facadeMetadata;

        // Act
        fixer.visitAnyType(mockClazz, typeMetadata1);
        fixer.visitAnyType(mockClazz, typeMetadata2);

        // Assert - CLASS kind uses full class name with dot separator
        assertEquals("com/example/TestClassKt.TestAlias", typeMetadata1.aliasName,
                "CLASS kind should use full class name with dot separator");

        // Assert - FILE_FACADE kind uses package prefix
        assertEquals("com/example/TestAlias", typeMetadata2.aliasName,
                "FILE_FACADE kind should use package prefix");
    }

    /**
     * Tests that visitAnyType handles multiple type aliases with different container kinds.
     * Different metadata instances should be updated independently.
     */
    @Test
    public void testVisitAnyType_withMultipleTypeAliases_updatesIndependently() {
        // Arrange - Create multiple independent metadata instances
        KotlinTypeMetadata metadata1 = mock(KotlinTypeMetadata.class);
        metadata1.aliasName = "Alias1";
        KotlinTypeAliasMetadata typeAlias1 = mock(KotlinTypeAliasMetadata.class);
        typeAlias1.name = "FirstAlias";
        metadata1.referencedTypeAlias = typeAlias1;
        KotlinClassKindMetadata classMetadata1 = mock(KotlinClassKindMetadata.class);
        classMetadata1.className = "com/first/FirstClass";
        classMetadata1.k = KotlinConstants.METADATA_KIND_CLASS;
        typeAlias1.referencedDeclarationContainer = classMetadata1;

        KotlinTypeMetadata metadata2 = mock(KotlinTypeMetadata.class);
        metadata2.aliasName = "Alias2";
        KotlinTypeAliasMetadata typeAlias2 = mock(KotlinTypeAliasMetadata.class);
        typeAlias2.name = "SecondAlias";
        metadata2.referencedTypeAlias = typeAlias2;
        KotlinDeclarationContainerMetadata container2 = mock(KotlinDeclarationContainerMetadata.class);
        container2.ownerClassName = "com/second/SecondKt";
        container2.k = KotlinConstants.METADATA_KIND_FILE_FACADE;
        typeAlias2.referencedDeclarationContainer = container2;

        // Act
        fixer.visitAnyType(mockClazz, metadata1);
        fixer.visitAnyType(mockClazz, metadata2);

        // Assert
        assertEquals("com/first/FirstClass.FirstAlias", metadata1.aliasName,
                "First metadata should be updated independently");
        assertEquals("com/second/SecondAlias", metadata2.aliasName,
                "Second metadata should be updated independently");
    }

    /**
     * Tests that visitAnyType does not throw exception when called with valid parameters.
     * All valid inputs should be handled gracefully.
     */
    @Test
    public void testVisitAnyType_withValidParameters_doesNotThrowException() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "ValidAlias";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "NewAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "com/example/ValidClass";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata),
                "Should not throw exception with valid parameters");
    }

    /**
     * Tests that visitAnyType creates correct alias name format for class-contained aliases.
     * The format should be: "className.aliasName"
     */
    @Test
    public void testVisitAnyType_classContainedAlias_usesCorrectFormat() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldFormat";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "AliasName";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinClassKindMetadata mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockClassMetadata.className = "ClassName";
        mockClassMetadata.k = KotlinConstants.METADATA_KIND_CLASS;
        mockTypeAlias.referencedDeclarationContainer = mockClassMetadata;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("ClassName.AliasName", mockKotlinTypeMetadata.aliasName,
                "Class-contained alias should use 'className.aliasName' format");
        assertTrue(mockKotlinTypeMetadata.aliasName.contains("."),
                "Alias name should contain dot separator");
    }

    /**
     * Tests that visitAnyType creates correct alias name format for top-level aliases.
     * The format should be: "packagePrefix + aliasName"
     */
    @Test
    public void testVisitAnyType_topLevelAlias_usesCorrectFormat() {
        // Arrange
        mockKotlinTypeMetadata.aliasName = "OldFormat";

        KotlinTypeAliasMetadata mockTypeAlias = mock(KotlinTypeAliasMetadata.class);
        mockTypeAlias.name = "TopAlias";
        mockKotlinTypeMetadata.referencedTypeAlias = mockTypeAlias;

        KotlinDeclarationContainerMetadata mockContainer = mock(KotlinDeclarationContainerMetadata.class);
        mockContainer.ownerClassName = "com/example/package/FileKt";
        mockContainer.k = KotlinConstants.METADATA_KIND_FILE_FACADE;
        mockTypeAlias.referencedDeclarationContainer = mockContainer;

        // Act
        fixer.visitAnyType(mockClazz, mockKotlinTypeMetadata);

        // Assert
        assertEquals("com/example/package/TopAlias", mockKotlinTypeMetadata.aliasName,
                "Top-level alias should use 'packagePrefix + aliasName' format");
        assertFalse(mockKotlinTypeMetadata.aliasName.contains("."),
                "Top-level alias should not contain dot separator (only package separators '/')");
    }
}
