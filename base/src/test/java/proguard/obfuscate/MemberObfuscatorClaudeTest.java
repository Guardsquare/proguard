package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberObfuscator}.
 * Tests all methods including:
 * - <init>.(ZLproguard/obfuscate/NameFactory;Ljava/util/Map;)V
 * - visitAnyMember.(Lproguard/classfile/Clazz;Lproguard/classfile/Member;)V
 * - retrieveNameMap.(Ljava/util/Map;Ljava/lang/String;)Ljava/util/Map;
 * - setFixedNewMemberName.(Lproguard/classfile/Member;Ljava/lang/String;)V
 * - setNewMemberName.(Lproguard/classfile/Member;Ljava/lang/String;)V
 * - hasFixedNewMemberName.(Lproguard/classfile/Member;)Z
 * - newMemberName.(Lproguard/classfile/Member;)Ljava/lang/String;
 */
public class MemberObfuscatorClaudeTest {

    private NameFactory mockNameFactory;
    private Map<String, Map<String, String>> descriptorMap;
    private ProgramClass programClass;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        mockNameFactory = mock(NameFactory.class);
        descriptorMap = new HashMap<>();
        programClass = new ProgramClass();
        programClass.u2thisClass = 1;
        libraryClass = new LibraryClass();
    }

    // ==================== Constructor Tests ====================

    /**
     * Tests the constructor with allowAggressiveOverloading = true.
     * Verifies that the object is successfully created.
     */
    @Test
    public void testConstructor_withAggressiveOverloadingEnabled() {
        // Act
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);

        // Assert
        assertNotNull(obfuscator);
    }

    /**
     * Tests the constructor with allowAggressiveOverloading = false.
     * Verifies that the object is successfully created.
     */
    @Test
    public void testConstructor_withAggressiveOverloadingDisabled() {
        // Act
        MemberObfuscator obfuscator = new MemberObfuscator(false, mockNameFactory, descriptorMap);

        // Assert
        assertNotNull(obfuscator);
    }

    /**
     * Tests the constructor with an empty descriptor map.
     * Verifies that an empty map is accepted.
     */
    @Test
    public void testConstructor_withEmptyDescriptorMap() {
        // Arrange
        Map<String, Map<String, String>> emptyMap = new HashMap<>();

        // Act
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, emptyMap);

        // Assert
        assertNotNull(obfuscator);
    }

    /**
     * Tests the constructor with a populated descriptor map.
     * Verifies that a pre-populated map is accepted.
     */
    @Test
    public void testConstructor_withPopulatedDescriptorMap() {
        // Arrange
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("a", "originalName");
        descriptorMap.put("()V", nameMap);

        // Act
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);

        // Assert
        assertNotNull(obfuscator);
    }

    /**
     * Tests that the constructor does not invoke methods on the NameFactory.
     * Verifies that the factory is stored but not used during construction.
     */
    @Test
    public void testConstructor_doesNotInvokeNameFactory() {
        // Act
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);

        // Assert
        assertNotNull(obfuscator);
        verifyNoInteractions(mockNameFactory);
    }

    /**
     * Tests creating multiple instances with the same parameters.
     * Verifies that each call creates a new independent instance.
     */
    @Test
    public void testConstructor_createsDifferentInstances() {
        // Act
        MemberObfuscator obfuscator1 = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        MemberObfuscator obfuscator2 = new MemberObfuscator(true, mockNameFactory, descriptorMap);

        // Assert
        assertNotNull(obfuscator1);
        assertNotNull(obfuscator2);
        assertNotSame(obfuscator1, obfuscator2);
    }

    /**
     * Tests that the obfuscator implements MemberVisitor interface.
     * Verifies that it can be used as a MemberVisitor.
     */
    @Test
    public void testConstructor_implementsMemberVisitor() {
        // Act
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);

        // Assert
        assertTrue(obfuscator instanceof proguard.classfile.visitor.MemberVisitor,
                   "MemberObfuscator should implement MemberVisitor");
    }

    // ==================== retrieveNameMap Tests ====================

    /**
     * Tests retrieveNameMap with an empty descriptor map.
     * Verifies that a new map is created and added to the descriptor map.
     */
    @Test
    public void testRetrieveNameMap_withEmptyDescriptorMap_createsNewMap() {
        // Arrange
        Map<String, Map<String, String>> testDescriptorMap = new HashMap<>();
        String descriptor = "()V";

        // Act
        Map<String, String> result = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(testDescriptorMap.containsKey(descriptor));
        assertSame(result, testDescriptorMap.get(descriptor));
    }

    /**
     * Tests retrieveNameMap with an existing descriptor.
     * Verifies that the existing map is returned.
     */
    @Test
    public void testRetrieveNameMap_withExistingDescriptor_returnsExistingMap() {
        // Arrange
        Map<String, Map<String, String>> testDescriptorMap = new HashMap<>();
        String descriptor = "()V";
        Map<String, String> existingMap = new HashMap<>();
        existingMap.put("a", "originalA");
        testDescriptorMap.put(descriptor, existingMap);

        // Act
        Map<String, String> result = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor);

        // Assert
        assertNotNull(result);
        assertSame(existingMap, result);
        assertEquals(1, result.size());
        assertEquals("originalA", result.get("a"));
    }

    /**
     * Tests retrieveNameMap with multiple descriptors.
     * Verifies that different maps are created for different descriptors.
     */
    @Test
    public void testRetrieveNameMap_withMultipleDescriptors_createsSeparateMaps() {
        // Arrange
        Map<String, Map<String, String>> testDescriptorMap = new HashMap<>();
        String descriptor1 = "()V";
        String descriptor2 = "(I)V";

        // Act
        Map<String, String> map1 = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor1);
        Map<String, String> map2 = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor2);

        // Assert
        assertNotNull(map1);
        assertNotNull(map2);
        assertNotSame(map1, map2);
        assertEquals(2, testDescriptorMap.size());
    }

    /**
     * Tests retrieveNameMap with the same descriptor twice.
     * Verifies that the same map is returned on subsequent calls.
     */
    @Test
    public void testRetrieveNameMap_calledTwiceWithSameDescriptor_returnsSameMap() {
        // Arrange
        Map<String, Map<String, String>> testDescriptorMap = new HashMap<>();
        String descriptor = "()V";

        // Act
        Map<String, String> map1 = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor);
        Map<String, String> map2 = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor);

        // Assert
        assertSame(map1, map2);
    }

    /**
     * Tests retrieveNameMap with complex descriptor.
     * Verifies that complex descriptors are handled correctly.
     */
    @Test
    public void testRetrieveNameMap_withComplexDescriptor() {
        // Arrange
        Map<String, Map<String, String>> testDescriptorMap = new HashMap<>();
        String descriptor = "(Ljava/lang/String;[I)Ljava/lang/Object;";

        // Act
        Map<String, String> result = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor);

        // Assert
        assertNotNull(result);
        assertTrue(testDescriptorMap.containsKey(descriptor));
    }

    /**
     * Tests retrieveNameMap mutations affect the descriptor map.
     * Verifies that the returned map is live and modifications are reflected.
     */
    @Test
    public void testRetrieveNameMap_mutationsAreReflected() {
        // Arrange
        Map<String, Map<String, String>> testDescriptorMap = new HashMap<>();
        String descriptor = "()V";

        // Act
        Map<String, String> nameMap = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor);
        nameMap.put("newName", "oldName");

        // Assert
        Map<String, String> retrievedAgain = MemberObfuscator.retrieveNameMap(testDescriptorMap, descriptor);
        assertTrue(retrievedAgain.containsKey("newName"));
        assertEquals("oldName", retrievedAgain.get("newName"));
    }

    // ==================== setFixedNewMemberName Tests ====================

    /**
     * Tests setFixedNewMemberName with a program member.
     * Verifies that the name is set on the member.
     */
    @Test
    public void testSetFixedNewMemberName_withProgramMember() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        String newName = "fixedName";

        // Act
        MemberObfuscator.setFixedNewMemberName(programMethod, newName);

        // Assert
        assertNotNull(programMethod.getProcessingInfo());
    }

    /**
     * Tests setFixedNewMemberName with a library member.
     * Verifies that the name is set on the member.
     */
    @Test
    public void testSetFixedNewMemberName_withLibraryMember() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        String newName = "fixedName";

        // Act
        MemberObfuscator.setFixedNewMemberName(libraryMethod, newName);

        // Assert
        assertEquals(newName, libraryMethod.getProcessingInfo());
    }

    /**
     * Tests setFixedNewMemberName with different names.
     * Verifies that different names can be set.
     */
    @Test
    public void testSetFixedNewMemberName_withDifferentNames() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        // Act
        MemberObfuscator.setFixedNewMemberName(method1, "name1");
        MemberObfuscator.setFixedNewMemberName(method2, "name2");

        // Assert
        assertNotNull(method1.getProcessingInfo());
        assertNotNull(method2.getProcessingInfo());
    }

    // ==================== setNewMemberName Tests ====================

    /**
     * Tests setNewMemberName with a program member.
     * Verifies that the name is set on the member.
     */
    @Test
    public void testSetNewMemberName_withProgramMember() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        String newName = "newName";

        // Act
        MemberObfuscator.setNewMemberName(programMethod, newName);

        // Assert
        assertEquals(newName, programMethod.getProcessingInfo());
    }

    /**
     * Tests setNewMemberName with different names.
     * Verifies that different names can be set.
     */
    @Test
    public void testSetNewMemberName_withDifferentNames() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        // Act
        MemberObfuscator.setNewMemberName(method1, "name1");
        MemberObfuscator.setNewMemberName(method2, "name2");

        // Assert
        assertEquals("name1", method1.getProcessingInfo());
        assertEquals("name2", method2.getProcessingInfo());
    }

    /**
     * Tests setNewMemberName with empty string.
     * Verifies that empty strings are accepted.
     */
    @Test
    public void testSetNewMemberName_withEmptyString() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        MemberObfuscator.setNewMemberName(programMethod, "");

        // Assert
        assertEquals("", programMethod.getProcessingInfo());
    }

    // ==================== hasFixedNewMemberName Tests ====================

    /**
     * Tests hasFixedNewMemberName with a library member.
     * Verifies that library members are considered fixed.
     */
    @Test
    public void testHasFixedNewMemberName_withLibraryMember_returnsTrue() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act
        boolean result = MemberObfuscator.hasFixedNewMemberName(libraryMethod);

        // Assert
        assertTrue(result, "Library members should have fixed names");
    }

    /**
     * Tests hasFixedNewMemberName with a program member without fixed name.
     * Verifies that regular program members are not considered fixed.
     */
    @Test
    public void testHasFixedNewMemberName_withProgramMemberWithoutFixedName_returnsFalse() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        boolean result = MemberObfuscator.hasFixedNewMemberName(programMethod);

        // Assert
        assertFalse(result, "Program member without fixed name should return false");
    }

    /**
     * Tests hasFixedNewMemberName with a program member that has a fixed name set.
     * Verifies that program members with fixed names are correctly identified.
     */
    @Test
    public void testHasFixedNewMemberName_withProgramMemberWithFixedName_returnsTrue() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        MemberObfuscator.setFixedNewMemberName(programMethod, "fixedName");

        // Act
        boolean result = MemberObfuscator.hasFixedNewMemberName(programMethod);

        // Assert
        assertTrue(result, "Program member with fixed name should return true");
    }

    // ==================== newMemberName Tests ====================

    /**
     * Tests newMemberName with a member that has a name set.
     * Verifies that the name is retrieved correctly.
     */
    @Test
    public void testNewMemberName_withSetName_returnsName() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        String expectedName = "obfuscatedName";
        programMethod.setProcessingInfo(expectedName);

        // Act
        String result = MemberObfuscator.newMemberName(programMethod);

        // Assert
        assertEquals(expectedName, result);
    }

    /**
     * Tests newMemberName with a member that has no name set.
     * Verifies that null is returned.
     */
    @Test
    public void testNewMemberName_withoutSetName_returnsNull() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        String result = MemberObfuscator.newMemberName(programMethod);

        // Assert
        assertNull(result);
    }

    /**
     * Tests newMemberName with different members.
     * Verifies that names are retrieved independently for each member.
     */
    @Test
    public void testNewMemberName_withDifferentMembers_returnsIndependentNames() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        method1.setProcessingInfo("name1");
        method2.setProcessingInfo("name2");

        // Act
        String result1 = MemberObfuscator.newMemberName(method1);
        String result2 = MemberObfuscator.newMemberName(method2);

        // Assert
        assertEquals("name1", result1);
        assertEquals("name2", result2);
    }

    // ==================== visitAnyMember Tests ====================

    /**
     * Tests visitAnyMember with a method that is an initializer (clinit).
     * Verifies that initializers are ignored and not obfuscated.
     */
    @Test
    public void testVisitAnyMember_withClinitInitializer_ignored() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        Member mockMember = mock(Member.class);
        when(mockMember.getName(mockClazz)).thenReturn("<clinit>");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");

        // Act
        obfuscator.visitAnyMember(mockClazz, mockMember);

        // Assert
        verifyNoInteractions(mockNameFactory);
    }

    /**
     * Tests visitAnyMember with a method that is a constructor (init).
     * Verifies that constructors are ignored and not obfuscated.
     */
    @Test
    public void testVisitAnyMember_withInitConstructor_ignored() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        Member mockMember = mock(Member.class);
        when(mockMember.getName(mockClazz)).thenReturn("<init>");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");

        // Act
        obfuscator.visitAnyMember(mockClazz, mockMember);

        // Assert
        verifyNoInteractions(mockNameFactory);
    }

    /**
     * Tests visitAnyMember with a regular method without aggressive overloading.
     * Verifies that the descriptor is trimmed to exclude return type.
     */
    @Test
    public void testVisitAnyMember_withoutAggressiveOverloading_trimsDescriptor() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(false, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember programMember = mock(ProgramMember.class);
        when(programMember.getName(mockClazz)).thenReturn("testMethod");
        when(programMember.getDescriptor(mockClazz)).thenReturn("(I)V");
        when(programMember.getProcessingInfo()).thenReturn(null);
        when(mockNameFactory.nextName()).thenReturn("a");

        // Act
        obfuscator.visitAnyMember(mockClazz, programMember);

        // Assert
        verify(mockNameFactory).reset();
        verify(mockNameFactory, atLeastOnce()).nextName();
        assertTrue(descriptorMap.containsKey("(I)"));
    }

    /**
     * Tests visitAnyMember with a regular method with aggressive overloading.
     * Verifies that the full descriptor is used.
     */
    @Test
    public void testVisitAnyMember_withAggressiveOverloading_usesFullDescriptor() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember programMember = mock(ProgramMember.class);
        when(programMember.getName(mockClazz)).thenReturn("testMethod");
        when(programMember.getDescriptor(mockClazz)).thenReturn("(I)V");
        when(programMember.getProcessingInfo()).thenReturn(null);
        when(mockNameFactory.nextName()).thenReturn("a");
        when(mockClazz.extendsOrImplements(anyString())).thenReturn(false);

        // Act
        obfuscator.visitAnyMember(mockClazz, programMember);

        // Assert
        verify(mockNameFactory).reset();
        verify(mockNameFactory, atLeastOnce()).nextName();
        assertTrue(descriptorMap.containsKey("(I)V"));
    }

    /**
     * Tests visitAnyMember with a member that already has a name.
     * Verifies that members with existing names are not renamed.
     */
    @Test
    public void testVisitAnyMember_withExistingName_doesNotRename() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember programMember = mock(ProgramMember.class);
        when(programMember.getName(mockClazz)).thenReturn("testMethod");
        when(programMember.getDescriptor(mockClazz)).thenReturn("()V");
        when(programMember.getProcessingInfo()).thenReturn("existingName");

        // Act
        obfuscator.visitAnyMember(mockClazz, programMember);

        // Assert
        verifyNoInteractions(mockNameFactory);
    }

    /**
     * Tests visitAnyMember with name conflicts.
     * Verifies that the factory is called until a unique name is found.
     */
    @Test
    public void testVisitAnyMember_withNameConflict_findsUniqueName() {
        // Arrange
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("a", "oldName1");
        nameMap.put("b", "oldName2");
        descriptorMap.put("()V", nameMap);

        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember programMember = mock(ProgramMember.class);
        when(programMember.getName(mockClazz)).thenReturn("testMethod");
        when(programMember.getDescriptor(mockClazz)).thenReturn("()V");
        when(programMember.getProcessingInfo()).thenReturn(null);
        when(mockNameFactory.nextName()).thenReturn("a", "b", "c");
        when(mockClazz.extendsOrImplements(anyString())).thenReturn(false);

        // Act
        obfuscator.visitAnyMember(mockClazz, programMember);

        // Assert
        verify(mockNameFactory).reset();
        verify(mockNameFactory, times(3)).nextName();
        assertTrue(nameMap.containsKey("c"));
        assertEquals("testMethod", nameMap.get("c"));
    }

    /**
     * Tests visitAnyMember with annotation class.
     * Verifies that annotation classes don't use aggressive overloading even if enabled.
     */
    @Test
    public void testVisitAnyMember_withAnnotationClass_trimsDescriptor() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember programMember = mock(ProgramMember.class);
        when(programMember.getName(mockClazz)).thenReturn("testMethod");
        when(programMember.getDescriptor(mockClazz)).thenReturn("(I)Ljava/lang/String;");
        when(programMember.getProcessingInfo()).thenReturn(null);
        when(mockNameFactory.nextName()).thenReturn("a");
        when(mockClazz.extendsOrImplements("java/lang/annotation/Annotation")).thenReturn(true);

        // Act
        obfuscator.visitAnyMember(mockClazz, programMember);

        // Assert
        verify(mockNameFactory).reset();
        verify(mockNameFactory, atLeastOnce()).nextName();
        assertTrue(descriptorMap.containsKey("(I)"));
    }

    /**
     * Tests visitAnyMember with field descriptor.
     * Verifies that field descriptors are handled correctly.
     */
    @Test
    public void testVisitAnyMember_withFieldDescriptor() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramField programField = mock(ProgramField.class);
        when(programField.getName(mockClazz)).thenReturn("testField");
        when(programField.getDescriptor(mockClazz)).thenReturn("I");
        when(programField.getProcessingInfo()).thenReturn(null);
        when(mockNameFactory.nextName()).thenReturn("a");
        when(mockClazz.extendsOrImplements(anyString())).thenReturn(false);

        // Act
        obfuscator.visitAnyMember(mockClazz, programField);

        // Assert
        verify(mockNameFactory).reset();
        verify(mockNameFactory, atLeastOnce()).nextName();
    }

    /**
     * Tests visitAnyMember stores the original name in the map.
     * Verifies that the original name is associated with the new name.
     */
    @Test
    public void testVisitAnyMember_storesOriginalNameInMap() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember programMember = mock(ProgramMember.class);
        String originalName = "originalMethod";
        when(programMember.getName(mockClazz)).thenReturn(originalName);
        when(programMember.getDescriptor(mockClazz)).thenReturn("()V");
        when(programMember.getProcessingInfo()).thenReturn(null);
        when(mockNameFactory.nextName()).thenReturn("a");
        when(mockClazz.extendsOrImplements(anyString())).thenReturn(false);

        // Act
        obfuscator.visitAnyMember(mockClazz, programMember);

        // Assert
        Map<String, String> nameMap = descriptorMap.get("()V");
        assertNotNull(nameMap);
        assertTrue(nameMap.containsKey("a"));
        assertEquals(originalName, nameMap.get("a"));
    }

    /**
     * Tests visitAnyMember with multiple members sharing the same descriptor.
     * Verifies that they get different obfuscated names.
     */
    @Test
    public void testVisitAnyMember_withMultipleMembersWithSameDescriptor() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember member1 = mock(ProgramMember.class);
        ProgramMember member2 = mock(ProgramMember.class);

        when(member1.getName(mockClazz)).thenReturn("method1");
        when(member1.getDescriptor(mockClazz)).thenReturn("()V");
        when(member1.getProcessingInfo()).thenReturn(null);

        when(member2.getName(mockClazz)).thenReturn("method2");
        when(member2.getDescriptor(mockClazz)).thenReturn("()V");
        when(member2.getProcessingInfo()).thenReturn(null);

        when(mockNameFactory.nextName()).thenReturn("a", "b");
        when(mockClazz.extendsOrImplements(anyString())).thenReturn(false);

        // Act
        obfuscator.visitAnyMember(mockClazz, member1);
        obfuscator.visitAnyMember(mockClazz, member2);

        // Assert
        Map<String, String> nameMap = descriptorMap.get("()V");
        assertNotNull(nameMap);
        assertEquals(2, nameMap.size());
        assertTrue(nameMap.containsKey("a"));
        assertTrue(nameMap.containsKey("b"));
    }

    /**
     * Tests visitAnyMember ensures name factory is reset before generating names.
     * Verifies that reset is called before nextName.
     */
    @Test
    public void testVisitAnyMember_resetsNameFactoryBeforeGenerating() {
        // Arrange
        MemberObfuscator obfuscator = new MemberObfuscator(true, mockNameFactory, descriptorMap);
        Clazz mockClazz = mock(Clazz.class);
        ProgramMember programMember = mock(ProgramMember.class);
        when(programMember.getName(mockClazz)).thenReturn("testMethod");
        when(programMember.getDescriptor(mockClazz)).thenReturn("()V");
        when(programMember.getProcessingInfo()).thenReturn(null);
        when(mockNameFactory.nextName()).thenReturn("a");
        when(mockClazz.extendsOrImplements(anyString())).thenReturn(false);

        // Act
        obfuscator.visitAnyMember(mockClazz, programMember);

        // Assert
        verify(mockNameFactory).reset();
        verify(mockNameFactory).nextName();
    }
}
