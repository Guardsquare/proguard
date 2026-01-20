package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.Method;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinPropertyNameObfuscator#visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
 * Tests the visitAnyProperty method which checks if a property should be obfuscated based on
 * DONT_OBFUSCATE flags on its backing field, getter, or setter, and if not flagged,
 * assigns a new name from the NameFactory.
 */
public class KotlinPropertyNameObfuscatorClaude_visitAnyPropertyTest {

    private KotlinPropertyNameObfuscator obfuscator;
    private NameFactory mockNameFactory;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockContainerMetadata;
    private KotlinPropertyMetadata mockPropertyMetadata;

    @BeforeEach
    public void setUp() {
        mockNameFactory = mock(NameFactory.class);
        obfuscator = new KotlinPropertyNameObfuscator(mockNameFactory);
        mockClazz = mock(Clazz.class);
        mockContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        mockPropertyMetadata = mock(KotlinPropertyMetadata.class);

        // Set up default behavior - no referenced members
        mockPropertyMetadata.referencedBackingField = null;
        mockPropertyMetadata.referencedGetterMethod = null;
        mockPropertyMetadata.referencedSetterMethod = null;
    }

    /**
     * Tests that visitAnyProperty can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnyProperty_doesNotThrowException() {
        // Arrange
        when(mockNameFactory.nextName()).thenReturn("a");

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);
        }, "visitAnyProperty should not throw an exception");
    }

    /**
     * Tests that visitAnyProperty sets processing info when property has no referenced members.
     * When there are no backing field, getter, or setter, the property should be obfuscated.
     */
    @Test
    public void testVisitAnyProperty_noReferencedMembers_setsProcessingInfo() {
        // Arrange
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("newName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty does not obfuscate when backing field has DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_backingFieldHasDontObfuscateFlag_doesNotObfuscate() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedBackingField = mockBackingField;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should not set processing info or call nextName
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty does not obfuscate when getter method has DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_getterHasDontObfuscateFlag_doesNotObfuscate() {
        // Arrange
        Method mockGetter = mock(Method.class);
        when(mockGetter.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedGetterMethod = mockGetter;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should not set processing info or call nextName
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty does not obfuscate when setter method has DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_setterHasDontObfuscateFlag_doesNotObfuscate() {
        // Arrange
        Method mockSetter = mock(Method.class);
        when(mockSetter.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedSetterMethod = mockSetter;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should not set processing info or call nextName
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty obfuscates when backing field exists but has no DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_backingFieldWithoutFlag_obfuscates() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(0); // No flags set
        mockPropertyMetadata.referencedBackingField = mockBackingField;
        when(mockNameFactory.nextName()).thenReturn("obfuscatedName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should set processing info
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("obfuscatedName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty obfuscates when getter exists but has no DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_getterWithoutFlag_obfuscates() {
        // Arrange
        Method mockGetter = mock(Method.class);
        when(mockGetter.getProcessingFlags()).thenReturn(0); // No flags set
        mockPropertyMetadata.referencedGetterMethod = mockGetter;
        when(mockNameFactory.nextName()).thenReturn("obfuscatedName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should set processing info
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("obfuscatedName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty obfuscates when setter exists but has no DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_setterWithoutFlag_obfuscates() {
        // Arrange
        Method mockSetter = mock(Method.class);
        when(mockSetter.getProcessingFlags()).thenReturn(0); // No flags set
        mockPropertyMetadata.referencedSetterMethod = mockSetter;
        when(mockNameFactory.nextName()).thenReturn("obfuscatedName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should set processing info
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("obfuscatedName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty does not obfuscate when any member has DONT_OBFUSCATE flag.
     * Even if only one member is flagged, the property should not be obfuscated.
     */
    @Test
    public void testVisitAnyProperty_anyMemberHasFlag_doesNotObfuscate() {
        // Arrange - backing field is flagged, but getter and setter are not
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedBackingField = mockBackingField;

        Method mockGetter = mock(Method.class);
        when(mockGetter.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedGetterMethod = mockGetter;

        Method mockSetter = mock(Method.class);
        when(mockSetter.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedSetterMethod = mockSetter;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should not obfuscate
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty obfuscates when all members exist but none have DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_allMembersWithoutFlag_obfuscates() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedBackingField = mockBackingField;

        Method mockGetter = mock(Method.class);
        when(mockGetter.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedGetterMethod = mockGetter;

        Method mockSetter = mock(Method.class);
        when(mockSetter.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedSetterMethod = mockSetter;

        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should obfuscate
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("newName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty checks backing field flag even when getter and setter are null.
     */
    @Test
    public void testVisitAnyProperty_onlyBackingFieldFlagged_doesNotObfuscate() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedBackingField = mockBackingField;
        mockPropertyMetadata.referencedGetterMethod = null;
        mockPropertyMetadata.referencedSetterMethod = null;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty checks getter flag even when backing field and setter are null.
     */
    @Test
    public void testVisitAnyProperty_onlyGetterFlagged_doesNotObfuscate() {
        // Arrange
        mockPropertyMetadata.referencedBackingField = null;
        Method mockGetter = mock(Method.class);
        when(mockGetter.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedGetterMethod = mockGetter;
        mockPropertyMetadata.referencedSetterMethod = null;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty checks setter flag even when backing field and getter are null.
     */
    @Test
    public void testVisitAnyProperty_onlySetterFlagged_doesNotObfuscate() {
        // Arrange
        mockPropertyMetadata.referencedBackingField = null;
        mockPropertyMetadata.referencedGetterMethod = null;
        Method mockSetter = mock(Method.class);
        when(mockSetter.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedSetterMethod = mockSetter;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty uses different names from NameFactory for multiple properties.
     */
    @Test
    public void testVisitAnyProperty_multipleProperties_usesDifferentNames() {
        // Arrange
        when(mockNameFactory.nextName()).thenReturn("a", "b", "c");
        KotlinPropertyMetadata prop1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata prop2 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata prop3 = mock(KotlinPropertyMetadata.class);

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, prop1);
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, prop2);
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, prop3);

        // Assert
        verify(prop1, times(1)).setProcessingInfo("a");
        verify(prop2, times(1)).setProcessingInfo("b");
        verify(prop3, times(1)).setProcessingInfo("c");
        verify(mockNameFactory, times(3)).nextName();
    }

    /**
     * Tests that visitAnyProperty with real NameFactory generates sequential names.
     */
    @Test
    public void testVisitAnyProperty_withRealNameFactory_generatesSequentialNames() {
        // Arrange
        SimpleNameFactory realFactory = new SimpleNameFactory();
        KotlinPropertyNameObfuscator realObfuscator = new KotlinPropertyNameObfuscator(realFactory);

        KotlinPropertyMetadata prop1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata prop2 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata prop3 = mock(KotlinPropertyMetadata.class);

        // Act
        realObfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, prop1);
        realObfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, prop2);
        realObfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, prop3);

        // Assert
        verify(prop1, times(1)).setProcessingInfo("a");
        verify(prop2, times(1)).setProcessingInfo("b");
        verify(prop3, times(1)).setProcessingInfo("c");
    }

    /**
     * Tests that visitAnyProperty handles flags with other bits set correctly.
     * If DONT_OBFUSCATE bit is set along with other flags, it should still not obfuscate.
     */
    @Test
    public void testVisitAnyProperty_flagWithOtherBits_checksCorrectly() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        // Set multiple flags including DONT_OBFUSCATE
        when(mockBackingField.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE | 0x04);
        mockPropertyMetadata.referencedBackingField = mockBackingField;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - should not obfuscate because DONT_OBFUSCATE flag is set
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty does not call nextName when property should not be obfuscated.
     */
    @Test
    public void testVisitAnyProperty_whenNotObfuscating_doesNotCallNextName() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedBackingField = mockBackingField;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty does not interact with Clazz parameter.
     * The clazz parameter is not used in the method logic.
     */
    @Test
    public void testVisitAnyProperty_doesNotInteractWithClazz() {
        // Arrange
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - no interactions with clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyProperty does not interact with container metadata.
     * The container metadata parameter is not used in the method logic.
     */
    @Test
    public void testVisitAnyProperty_doesNotInteractWithContainerMetadata() {
        // Arrange
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - no interactions with container metadata
        verifyNoInteractions(mockContainerMetadata);
    }

    /**
     * Tests that visitAnyProperty can be called with null Clazz parameter.
     * Since Clazz is not used in the logic, null should be acceptable.
     */
    @Test
    public void testVisitAnyProperty_withNullClazz_obfuscates() {
        // Arrange
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyProperty(null, mockContainerMetadata, mockPropertyMetadata);
        });

        verify(mockPropertyMetadata, times(1)).setProcessingInfo("newName");
    }

    /**
     * Tests that visitAnyProperty can be called with null container metadata.
     * Since container metadata is not used in the logic, null should be acceptable.
     */
    @Test
    public void testVisitAnyProperty_withNullContainerMetadata_obfuscates() {
        // Arrange
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyProperty(mockClazz, null, mockPropertyMetadata);
        });

        verify(mockPropertyMetadata, times(1)).setProcessingInfo("newName");
    }

    /**
     * Tests that visitAnyProperty with null property metadata throws NullPointerException.
     * This is expected since we need to access fields on the property metadata.
     */
    @Test
    public void testVisitAnyProperty_withNullPropertyMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, null);
        }, "Should throw NullPointerException when property metadata is null");
    }

    /**
     * Tests that visitAnyProperty correctly evaluates short-circuit logic.
     * If backing field is flagged, it should return early without checking getter/setter.
     */
    @Test
    public void testVisitAnyProperty_shortCircuitOnBackingField_doesNotCheckOthers() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedBackingField = mockBackingField;

        Method mockGetter = mock(Method.class);
        mockPropertyMetadata.referencedGetterMethod = mockGetter;

        Method mockSetter = mock(Method.class);
        mockPropertyMetadata.referencedSetterMethod = mockSetter;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert - backing field should be checked, but we can't verify if getter/setter were checked
        // due to short-circuit evaluation. Just verify the outcome.
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that multiple obfuscator instances work independently.
     */
    @Test
    public void testVisitAnyProperty_multipleObfuscators_workIndependently() {
        // Arrange
        NameFactory factory1 = mock(NameFactory.class);
        NameFactory factory2 = mock(NameFactory.class);
        when(factory1.nextName()).thenReturn("name1");
        when(factory2.nextName()).thenReturn("name2");

        KotlinPropertyNameObfuscator obfuscator1 = new KotlinPropertyNameObfuscator(factory1);
        KotlinPropertyNameObfuscator obfuscator2 = new KotlinPropertyNameObfuscator(factory2);

        KotlinPropertyMetadata prop1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata prop2 = mock(KotlinPropertyMetadata.class);

        // Act
        obfuscator1.visitAnyProperty(mockClazz, mockContainerMetadata, prop1);
        obfuscator2.visitAnyProperty(mockClazz, mockContainerMetadata, prop2);

        // Assert
        verify(prop1, times(1)).setProcessingInfo("name1");
        verify(prop2, times(1)).setProcessingInfo("name2");
        verify(factory1, times(1)).nextName();
        verify(factory2, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty correctly handles a property with only a backing field.
     */
    @Test
    public void testVisitAnyProperty_onlyBackingFieldNoFlag_obfuscates() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedBackingField = mockBackingField;
        mockPropertyMetadata.referencedGetterMethod = null;
        mockPropertyMetadata.referencedSetterMethod = null;
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("newName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty correctly handles a property with only a getter.
     */
    @Test
    public void testVisitAnyProperty_onlyGetterNoFlag_obfuscates() {
        // Arrange
        mockPropertyMetadata.referencedBackingField = null;
        Method mockGetter = mock(Method.class);
        when(mockGetter.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedGetterMethod = mockGetter;
        mockPropertyMetadata.referencedSetterMethod = null;
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("newName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty correctly handles a property with only a setter.
     */
    @Test
    public void testVisitAnyProperty_onlySetterNoFlag_obfuscates() {
        // Arrange
        mockPropertyMetadata.referencedBackingField = null;
        mockPropertyMetadata.referencedGetterMethod = null;
        Method mockSetter = mock(Method.class);
        when(mockSetter.getProcessingFlags()).thenReturn(0);
        mockPropertyMetadata.referencedSetterMethod = mockSetter;
        when(mockNameFactory.nextName()).thenReturn("newName");

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setProcessingInfo("newName");
        verify(mockNameFactory, times(1)).nextName();
    }

    /**
     * Tests that visitAnyProperty does not obfuscate when all three members have DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitAnyProperty_allMembersFlagged_doesNotObfuscate() {
        // Arrange
        Field mockBackingField = mock(Field.class);
        when(mockBackingField.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedBackingField = mockBackingField;

        Method mockGetter = mock(Method.class);
        when(mockGetter.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedGetterMethod = mockGetter;

        Method mockSetter = mock(Method.class);
        when(mockSetter.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        mockPropertyMetadata.referencedSetterMethod = mockSetter;

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, never()).setProcessingInfo(any());
        verify(mockNameFactory, never()).nextName();
    }

    /**
     * Tests that visitAnyProperty uses the exact name returned by NameFactory.
     */
    @Test
    public void testVisitAnyProperty_usesExactNameFromFactory() {
        // Arrange
        String expectedName = "verySpecificName123";
        when(mockNameFactory.nextName()).thenReturn(expectedName);

        // Act
        obfuscator.visitAnyProperty(mockClazz, mockContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setProcessingInfo(expectedName);
    }
}
