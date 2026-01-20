package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinTypeAliasMetadata;
import proguard.classfile.kotlin.KotlinTypeMetadata;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAliasNameObfuscator#visitAliasExpandedType(Clazz, KotlinDeclarationContainerMetadata, KotlinTypeAliasMetadata, KotlinTypeMetadata)}.
 * Tests the core logic that decides whether to obfuscate a type alias based on processing flags.
 */
public class KotlinAliasNameObfuscatorClaude_visitAliasExpandedTypeTest {

    private KotlinAliasNameObfuscator obfuscator;
    private NameFactory mockNameFactory;
    private Clazz mockClazz;
    private Clazz mockReferencedClass;
    private KotlinDeclarationContainerMetadata mockContainerMetadata;
    private KotlinTypeAliasMetadata mockTypeAliasMetadata;
    private KotlinTypeMetadata mockTypeMetadata;

    @BeforeEach
    public void setUp() {
        mockNameFactory = mock(NameFactory.class);
        obfuscator = new KotlinAliasNameObfuscator(mockNameFactory);
        mockClazz = mock(Clazz.class);
        mockReferencedClass = mock(Clazz.class);
        mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        mockTypeAliasMetadata = mock(KotlinTypeAliasMetadata.class);
        mockTypeMetadata = mock(KotlinTypeMetadata.class);

        // Set up the mockTypeMetadata to have a referenced class
        mockTypeMetadata.referencedClass = mockReferencedClass;
    }

    /**
     * Tests that visitAliasExpandedType can be called without throwing exceptions.
     */
    @Test
    public void testVisitAliasExpandedType_doesNotThrowException() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);
        }, "visitAliasExpandedType should not throw an exception");
    }

    /**
     * Tests that visitAliasExpandedType obfuscates the alias name when DONT_OBFUSCATE flag is NOT set.
     * This is the core behavior: if the referenced class can be obfuscated, so can the alias.
     */
    @Test
    public void testVisitAliasExpandedType_obfuscatesWhenFlagNotSet() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscatedName");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscatedName", mockTypeAliasMetadata.name,
                     "Alias name should be obfuscated when DONT_OBFUSCATE flag is not set");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType does NOT obfuscate the alias name when DONT_OBFUSCATE flag IS set.
     * This is the core behavior: if the referenced class is kept, the alias should be kept too.
     */
    @Test
    public void testVisitAliasExpandedType_doesNotObfuscateWhenFlagSet() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("OriginalName", mockTypeAliasMetadata.name,
                     "Alias name should NOT be obfuscated when DONT_OBFUSCATE flag is set");
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAliasExpandedType preserves the original name when multiple flags are set including DONT_OBFUSCATE.
     */
    @Test
    public void testVisitAliasExpandedType_doesNotObfuscateWhenDontObfuscateInMultipleFlags() {
        // Arrange
        int flags = ProcessingFlags.DONT_OBFUSCATE | ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OPTIMIZE;
        when(mockReferencedClass.getProcessingFlags()).thenReturn(flags);
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("OriginalName", mockTypeAliasMetadata.name,
                     "Alias name should NOT be obfuscated when DONT_OBFUSCATE flag is set along with other flags");
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAliasExpandedType obfuscates when only DONT_SHRINK flag is set (but not DONT_OBFUSCATE).
     * Only DONT_OBFUSCATE should prevent obfuscation.
     */
    @Test
    public void testVisitAliasExpandedType_obfuscatesWhenOnlyDontShrinkSet() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_SHRINK);
        when(mockNameFactory.nextName()).thenReturn("obfuscatedName");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscatedName", mockTypeAliasMetadata.name,
                     "Alias name should be obfuscated when only DONT_SHRINK flag is set");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType obfuscates when only DONT_OPTIMIZE flag is set (but not DONT_OBFUSCATE).
     * Only DONT_OBFUSCATE should prevent obfuscation.
     */
    @Test
    public void testVisitAliasExpandedType_obfuscatesWhenOnlyDontOptimizeSet() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OPTIMIZE);
        when(mockNameFactory.nextName()).thenReturn("obfuscatedName");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscatedName", mockTypeAliasMetadata.name,
                     "Alias name should be obfuscated when only DONT_OPTIMIZE flag is set");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType obfuscates when DONT_SHRINK and DONT_OPTIMIZE are set (but not DONT_OBFUSCATE).
     */
    @Test
    public void testVisitAliasExpandedType_obfuscatesWhenOtherFlagsSetButNotDontObfuscate() {
        // Arrange
        int flags = ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OPTIMIZE;
        when(mockReferencedClass.getProcessingFlags()).thenReturn(flags);
        when(mockNameFactory.nextName()).thenReturn("obfuscatedName");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscatedName", mockTypeAliasMetadata.name,
                     "Alias name should be obfuscated when other flags are set but not DONT_OBFUSCATE");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType uses different names from NameFactory for different aliases.
     */
    @Test
    public void testVisitAliasExpandedType_usesDifferentNamesForDifferentAliases() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("name1", "name2", "name3");

        KotlinTypeAliasMetadata alias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias2 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias3 = mock(KotlinTypeAliasMetadata.class);
        alias1.name = "Original1";
        alias2.name = "Original2";
        alias3.name = "Original3";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias1, mockTypeMetadata);
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias2, mockTypeMetadata);
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias3, mockTypeMetadata);

        // Assert
        assertEquals("name1", alias1.name);
        assertEquals("name2", alias2.name);
        assertEquals("name3", alias3.name);
        verify(mockNameFactory, times(3)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType can handle sequential calls with different flag states.
     */
    @Test
    public void testVisitAliasExpandedType_handlesMixedFlagStates() {
        // Arrange
        Clazz refClass1 = mock(Clazz.class);
        Clazz refClass2 = mock(Clazz.class);
        Clazz refClass3 = mock(Clazz.class);

        KotlinTypeMetadata type1 = mock(KotlinTypeMetadata.class);
        KotlinTypeMetadata type2 = mock(KotlinTypeMetadata.class);
        KotlinTypeMetadata type3 = mock(KotlinTypeMetadata.class);
        type1.referencedClass = refClass1;
        type2.referencedClass = refClass2;
        type3.referencedClass = refClass3;

        when(refClass1.getProcessingFlags()).thenReturn(0); // Should obfuscate
        when(refClass2.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE); // Should NOT obfuscate
        when(refClass3.getProcessingFlags()).thenReturn(0); // Should obfuscate
        when(mockNameFactory.nextName()).thenReturn("obf1", "obf2");

        KotlinTypeAliasMetadata alias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias2 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias3 = mock(KotlinTypeAliasMetadata.class);
        alias1.name = "Original1";
        alias2.name = "Original2";
        alias3.name = "Original3";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias1, type1);
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias2, type2);
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias3, type3);

        // Assert
        assertEquals("obf1", alias1.name, "First alias should be obfuscated");
        assertEquals("Original2", alias2.name, "Second alias should NOT be obfuscated");
        assertEquals("obf2", alias3.name, "Third alias should be obfuscated");
        verify(mockNameFactory, times(2)).nextName(); // Only called for alias1 and alias3
    }

    /**
     * Tests that visitAliasExpandedType with a real SimpleNameFactory generates sequential names.
     */
    @Test
    public void testVisitAliasExpandedType_withRealNameFactory_generatesSequentialNames() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinAliasNameObfuscator obfuscatorWithRealFactory = new KotlinAliasNameObfuscator(realFactory);
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);

        KotlinTypeAliasMetadata alias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias2 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias3 = mock(KotlinTypeAliasMetadata.class);
        alias1.name = "Original1";
        alias2.name = "Original2";
        alias3.name = "Original3";

        // Act
        obfuscatorWithRealFactory.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias1, mockTypeMetadata);
        obfuscatorWithRealFactory.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias2, mockTypeMetadata);
        obfuscatorWithRealFactory.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias3, mockTypeMetadata);

        // Assert
        assertEquals("a", alias1.name);
        assertEquals("b", alias2.name);
        assertEquals("c", alias3.name);
    }

    /**
     * Tests that visitAliasExpandedType preserves NameFactory state when not obfuscating.
     */
    @Test
    public void testVisitAliasExpandedType_preservesNameFactoryStateWhenNotObfuscating() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinAliasNameObfuscator obfuscatorWithRealFactory = new KotlinAliasNameObfuscator(realFactory);
        when(mockReferencedClass.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        mockTypeAliasMetadata.name = "OriginalName";

        // Advance the factory to 'c'
        assertEquals("a", realFactory.nextName());
        assertEquals("b", realFactory.nextName());
        assertEquals("c", realFactory.nextName());

        // Act - call with DONT_OBFUSCATE set, should not use factory
        obfuscatorWithRealFactory.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert - factory should continue from 'd', not 'c' or 'a'
        assertEquals("OriginalName", mockTypeAliasMetadata.name, "Name should not be changed");
        assertEquals("d", realFactory.nextName(), "Factory state should be preserved");
    }

    /**
     * Tests that visitAliasExpandedType doesn't interact with Clazz parameter.
     */
    @Test
    public void testVisitAliasExpandedType_doesNotInteractWithClazz() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert - verify no interactions with the clazz parameter (only with referencedClass)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAliasExpandedType doesn't interact with container metadata parameter.
     */
    @Test
    public void testVisitAliasExpandedType_doesNotInteractWithContainerMetadata() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        verifyNoInteractions(mockContainerMetadata);
    }

    /**
     * Tests that visitAliasExpandedType calls getProcessingFlags on the referenced class.
     */
    @Test
    public void testVisitAliasExpandedType_checksProcessingFlags() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        verify(mockReferencedClass, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitAliasExpandedType with null Clazz parameter still works.
     * The Clazz parameter is not used in the implementation.
     */
    @Test
    public void testVisitAliasExpandedType_withNullClazz_worksCorrectly() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(null, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscated", mockTypeAliasMetadata.name);
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType with null container metadata still works.
     * The container metadata parameter is not used in the implementation.
     */
    @Test
    public void testVisitAliasExpandedType_withNullContainer_worksCorrectly() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, null, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscated", mockTypeAliasMetadata.name);
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType with null type alias metadata throws NullPointerException.
     * This is expected since we need to access the name field.
     */
    @Test
    public void testVisitAliasExpandedType_withNullTypeAlias_throwsNullPointerException() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, null, mockTypeMetadata);
        }, "Should throw NullPointerException when type alias metadata is null");
    }

    /**
     * Tests that visitAliasExpandedType with null type metadata throws NullPointerException.
     * This is expected since we need to access the referencedClass field.
     */
    @Test
    public void testVisitAliasExpandedType_withNullTypeMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, null);
        }, "Should throw NullPointerException when type metadata is null");
    }

    /**
     * Tests that visitAliasExpandedType can handle many sequential calls.
     */
    @Test
    public void testVisitAliasExpandedType_rapidSequentialCalls() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j"
        );

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                KotlinTypeAliasMetadata alias = mock(KotlinTypeAliasMetadata.class);
                alias.name = "Original" + i;
                obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias, mockTypeMetadata);
            }
        }, "Should handle rapid sequential calls");

        verify(mockNameFactory, times(10)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType correctly interprets the bitwise AND operation for flag checking.
     */
    @Test
    public void testVisitAliasExpandedType_bitwiseOperationWorksCorrectly() {
        // Arrange - Set multiple flags including DONT_OBFUSCATE
        int flagsWithDontObfuscate = ProcessingFlags.DONT_OBFUSCATE | ProcessingFlags.DONT_SHRINK;
        int flagsWithoutDontObfuscate = ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OPTIMIZE;

        Clazz refClassWithFlag = mock(Clazz.class);
        Clazz refClassWithoutFlag = mock(Clazz.class);

        KotlinTypeMetadata typeWithFlag = mock(KotlinTypeMetadata.class);
        KotlinTypeMetadata typeWithoutFlag = mock(KotlinTypeMetadata.class);
        typeWithFlag.referencedClass = refClassWithFlag;
        typeWithoutFlag.referencedClass = refClassWithoutFlag;

        when(refClassWithFlag.getProcessingFlags()).thenReturn(flagsWithDontObfuscate);
        when(refClassWithoutFlag.getProcessingFlags()).thenReturn(flagsWithoutDontObfuscate);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        KotlinTypeAliasMetadata alias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias2 = mock(KotlinTypeAliasMetadata.class);
        alias1.name = "Original1";
        alias2.name = "Original2";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias1, typeWithFlag);
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias2, typeWithoutFlag);

        // Assert
        assertEquals("Original1", alias1.name, "Should NOT obfuscate when DONT_OBFUSCATE flag is present");
        assertEquals("obfuscated", alias2.name, "Should obfuscate when DONT_OBFUSCATE flag is absent");
        verify(mockNameFactory, times(1)).nextName(); // Only called once for alias2
    }

    /**
     * Tests that visitAliasExpandedType works correctly with different instances of obfuscator.
     */
    @Test
    public void testVisitAliasExpandedType_consistentBehaviorAcrossInstances() {
        // Arrange
        SimpleNameFactory factory1 = new SimpleNameFactory();
        SimpleNameFactory factory2 = new SimpleNameFactory();
        KotlinAliasNameObfuscator obf1 = new KotlinAliasNameObfuscator(factory1);
        KotlinAliasNameObfuscator obf2 = new KotlinAliasNameObfuscator(factory2);

        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);

        KotlinTypeAliasMetadata alias1 = mock(KotlinTypeAliasMetadata.class);
        KotlinTypeAliasMetadata alias2 = mock(KotlinTypeAliasMetadata.class);
        alias1.name = "Original";
        alias2.name = "Original";

        // Act
        obf1.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias1, mockTypeMetadata);
        obf2.visitAliasExpandedType(mockClazz, mockContainerMetadata, alias2, mockTypeMetadata);

        // Assert - both should obfuscate with their factory's first name
        assertEquals("a", alias1.name);
        assertEquals("a", alias2.name);
    }

    /**
     * Tests that visitAliasExpandedType returns void as expected.
     */
    @Test
    public void testVisitAliasExpandedType_returnsVoid() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");

        // Act - method returns void, so just verify it executes
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitAliasExpandedType with a real ProgramClass as referenced class.
     */
    @Test
    public void testVisitAliasExpandedType_withRealProgramClass() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        realClass.u2thisClass = 1;
        realClass.constantPool = new Constant[10];
        realClass.processingFlags = 0; // No flags set

        mockTypeMetadata.referencedClass = realClass;
        when(mockNameFactory.nextName()).thenReturn("obfuscated");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscated", mockTypeAliasMetadata.name);
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAliasExpandedType with a real ProgramClass with DONT_OBFUSCATE flag set.
     */
    @Test
    public void testVisitAliasExpandedType_withRealProgramClassWithDontObfuscateFlag() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        realClass.u2thisClass = 1;
        realClass.constantPool = new Constant[10];
        realClass.processingFlags = ProcessingFlags.DONT_OBFUSCATE;

        mockTypeMetadata.referencedClass = realClass;
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("OriginalName", mockTypeAliasMetadata.name);
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests the boundary case where processing flags is exactly DONT_OBFUSCATE (value check).
     */
    @Test
    public void testVisitAliasExpandedType_exactDontObfuscateFlagValue() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("OriginalName", mockTypeAliasMetadata.name);

        // Verify the bitwise operation: (DONT_OBFUSCATE & DONT_OBFUSCATE) == DONT_OBFUSCATE, which is NOT 0
        int flags = mockReferencedClass.getProcessingFlags();
        assertNotEquals(0, (flags & ProcessingFlags.DONT_OBFUSCATE),
                       "Bitwise AND should NOT be 0 when DONT_OBFUSCATE is set");
    }

    /**
     * Tests the boundary case where processing flags is 0 (no flags set).
     */
    @Test
    public void testVisitAliasExpandedType_zeroFlagsValue() {
        // Arrange
        when(mockReferencedClass.getProcessingFlags()).thenReturn(0);
        when(mockNameFactory.nextName()).thenReturn("obfuscated");
        mockTypeAliasMetadata.name = "OriginalName";

        // Act
        obfuscator.visitAliasExpandedType(mockClazz, mockContainerMetadata, mockTypeAliasMetadata, mockTypeMetadata);

        // Assert
        assertEquals("obfuscated", mockTypeAliasMetadata.name);

        // Verify the bitwise operation: (0 & DONT_OBFUSCATE) == 0
        int flags = 0;
        assertEquals(0, (flags & ProcessingFlags.DONT_OBFUSCATE),
                    "Bitwise AND should be 0 when no flags are set");
    }
}
