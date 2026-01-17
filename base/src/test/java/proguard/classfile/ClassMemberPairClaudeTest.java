package proguard.classfile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassMemberPair}.
 * Tests all methods including constructor, accept, getName, equals, hashCode, and toString.
 */
public class ClassMemberPairClaudeTest {

    private Clazz mockClazz;
    private Member mockMember;
    private MemberVisitor mockVisitor;

    @BeforeEach
    public void setUp() {
        mockClazz = mock(Clazz.class);
        mockMember = mock(Member.class);
        mockVisitor = mock(MemberVisitor.class);
    }

    // ==================== Constructor Tests ====================

    /**
     * Tests the constructor with valid non-null parameters.
     * Verifies that the fields are correctly initialized.
     */
    @Test
    public void testConstructor_withValidParameters_initializesFields() {
        // Act
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Assert
        assertNotNull(pair);
        assertSame(mockClazz, pair.clazz);
        assertSame(mockMember, pair.member);
    }

    /**
     * Tests the constructor with null clazz parameter.
     * Verifies that null clazz is accepted.
     */
    @Test
    public void testConstructor_withNullClazz_acceptsNull() {
        // Act
        ClassMemberPair pair = new ClassMemberPair(null, mockMember);

        // Assert
        assertNotNull(pair);
        assertNull(pair.clazz);
        assertSame(mockMember, pair.member);
    }

    /**
     * Tests the constructor with null member parameter.
     * Verifies that null member is accepted.
     */
    @Test
    public void testConstructor_withNullMember_acceptsNull() {
        // Act
        ClassMemberPair pair = new ClassMemberPair(mockClazz, null);

        // Assert
        assertNotNull(pair);
        assertSame(mockClazz, pair.clazz);
        assertNull(pair.member);
    }

    /**
     * Tests the constructor with both parameters null.
     * Verifies that both nulls are accepted.
     */
    @Test
    public void testConstructor_withBothNull_acceptsBothNull() {
        // Act
        ClassMemberPair pair = new ClassMemberPair(null, null);

        // Assert
        assertNotNull(pair);
        assertNull(pair.clazz);
        assertNull(pair.member);
    }

    // ==================== accept() Tests ====================

    /**
     * Tests accept method delegates to member.accept with correct parameters.
     * Verifies that the member's accept method is called with the clazz and visitor.
     */
    @Test
    public void testAccept_withValidVisitor_delegatesToMember() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        pair.accept(mockVisitor);

        // Assert
        verify(mockMember, times(1)).accept(mockClazz, mockVisitor);
    }

    /**
     * Tests accept method with null visitor.
     * The behavior depends on the member implementation, but the method should delegate.
     */
    @Test
    public void testAccept_withNullVisitor_delegatesToMember() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        pair.accept(null);

        // Assert
        verify(mockMember, times(1)).accept(mockClazz, null);
    }

    /**
     * Tests accept method when member is null.
     * Should throw NullPointerException when trying to call accept on null member.
     */
    @Test
    public void testAccept_withNullMember_throwsNullPointerException() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> pair.accept(mockVisitor));
    }

    /**
     * Tests accept method when clazz is null.
     * Should still delegate to member.accept, passing null clazz.
     */
    @Test
    public void testAccept_withNullClazz_delegatesToMemberWithNullClazz() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(null, mockMember);

        // Act
        pair.accept(mockVisitor);

        // Assert
        verify(mockMember, times(1)).accept(null, mockVisitor);
    }

    /**
     * Tests accept method can be called multiple times.
     * Each call should delegate to the member.
     */
    @Test
    public void testAccept_calledMultipleTimes_delegatesEachTime() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        pair.accept(mockVisitor);
        pair.accept(mockVisitor);
        pair.accept(mockVisitor);

        // Assert
        verify(mockMember, times(3)).accept(mockClazz, mockVisitor);
    }

    /**
     * Tests accept method with different visitors.
     * Each visitor should be passed to the member.
     */
    @Test
    public void testAccept_withDifferentVisitors_delegatesEachVisitor() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);
        MemberVisitor visitor3 = mock(MemberVisitor.class);

        // Act
        pair.accept(visitor1);
        pair.accept(visitor2);
        pair.accept(visitor3);

        // Assert
        verify(mockMember, times(1)).accept(mockClazz, visitor1);
        verify(mockMember, times(1)).accept(mockClazz, visitor2);
        verify(mockMember, times(1)).accept(mockClazz, visitor3);
    }

    // ==================== getName() Tests ====================

    /**
     * Tests getName method delegates to member.getName.
     * Verifies that the member's getName method is called with the clazz.
     */
    @Test
    public void testGetName_withValidMember_delegatesToMember() {
        // Arrange
        when(mockMember.getName(mockClazz)).thenReturn("testMember");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.getName();

        // Assert
        assertEquals("testMember", result);
        verify(mockMember, times(1)).getName(mockClazz);
    }

    /**
     * Tests getName method when member returns null.
     * Should return null as returned by the member.
     */
    @Test
    public void testGetName_whenMemberReturnsNull_returnsNull() {
        // Arrange
        when(mockMember.getName(mockClazz)).thenReturn(null);
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.getName();

        // Assert
        assertNull(result);
        verify(mockMember, times(1)).getName(mockClazz);
    }

    /**
     * Tests getName method when member returns empty string.
     * Should return empty string as returned by the member.
     */
    @Test
    public void testGetName_whenMemberReturnsEmptyString_returnsEmptyString() {
        // Arrange
        when(mockMember.getName(mockClazz)).thenReturn("");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.getName();

        // Assert
        assertEquals("", result);
        verify(mockMember, times(1)).getName(mockClazz);
    }

    /**
     * Tests getName method when clazz is null.
     * Should still delegate to member.getName, passing null clazz.
     */
    @Test
    public void testGetName_withNullClazz_delegatesToMemberWithNullClazz() {
        // Arrange
        when(mockMember.getName(null)).thenReturn("memberName");
        ClassMemberPair pair = new ClassMemberPair(null, mockMember);

        // Act
        String result = pair.getName();

        // Assert
        assertEquals("memberName", result);
        verify(mockMember, times(1)).getName(null);
    }

    /**
     * Tests getName method when member is null.
     * Should throw NullPointerException when trying to call getName on null member.
     */
    @Test
    public void testGetName_withNullMember_throwsNullPointerException() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> pair.getName());
    }

    /**
     * Tests getName method can be called multiple times.
     * Should return consistent results.
     */
    @Test
    public void testGetName_calledMultipleTimes_returnsConsistentResult() {
        // Arrange
        when(mockMember.getName(mockClazz)).thenReturn("consistentName");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result1 = pair.getName();
        String result2 = pair.getName();
        String result3 = pair.getName();

        // Assert
        assertEquals("consistentName", result1);
        assertEquals("consistentName", result2);
        assertEquals("consistentName", result3);
        verify(mockMember, times(3)).getName(mockClazz);
    }

    // ==================== equals() Tests ====================

    /**
     * Tests equals method when comparing with itself.
     * Should return true (reflexive property).
     */
    @Test
    public void testEquals_withSameInstance_returnsTrue() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        assertTrue(pair.equals(pair));
    }

    /**
     * Tests equals method when comparing with null.
     * Should return false.
     */
    @Test
    public void testEquals_withNull_returnsFalse() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        assertFalse(pair.equals(null));
    }

    /**
     * Tests equals method when comparing with different type.
     * Should return false.
     */
    @Test
    public void testEquals_withDifferentType_returnsFalse() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);
        String differentType = "not a ClassMemberPair";

        // Act & Assert
        assertFalse(pair.equals(differentType));
    }

    /**
     * Tests equals method when both clazz and member are equal.
     * Should return true.
     */
    @Test
    public void testEquals_withEqualClazzAndMember_returnsTrue() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        assertTrue(pair1.equals(pair2));
        assertTrue(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method when clazz differs.
     * Should return false.
     */
    @Test
    public void testEquals_withDifferentClazz_returnsFalse() {
        // Arrange
        Clazz otherClazz = mock(Clazz.class);
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(otherClazz, mockMember);

        // Act & Assert
        assertFalse(pair1.equals(pair2));
        assertFalse(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method when member differs.
     * Should return false.
     */
    @Test
    public void testEquals_withDifferentMember_returnsFalse() {
        // Arrange
        Member otherMember = mock(Member.class);
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, otherMember);

        // Act & Assert
        assertFalse(pair1.equals(pair2));
        assertFalse(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method when both clazz and member differ.
     * Should return false.
     */
    @Test
    public void testEquals_withDifferentClazzAndMember_returnsFalse() {
        // Arrange
        Clazz otherClazz = mock(Clazz.class);
        Member otherMember = mock(Member.class);
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(otherClazz, otherMember);

        // Act & Assert
        assertFalse(pair1.equals(pair2));
        assertFalse(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method with both pairs having null clazz.
     * Should return true if members are equal.
     */
    @Test
    public void testEquals_withBothNullClazz_returnsTrue() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(null, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(null, mockMember);

        // Act & Assert
        assertTrue(pair1.equals(pair2));
        assertTrue(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method with both pairs having null member.
     * Should return true if clazz are equal.
     */
    @Test
    public void testEquals_withBothNullMember_returnsTrue() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, null);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, null);

        // Act & Assert
        assertTrue(pair1.equals(pair2));
        assertTrue(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method with both pairs having null clazz and member.
     * Should return true.
     */
    @Test
    public void testEquals_withBothNullClazzAndMember_returnsTrue() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(null, null);
        ClassMemberPair pair2 = new ClassMemberPair(null, null);

        // Act & Assert
        assertTrue(pair1.equals(pair2));
        assertTrue(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method when one has null clazz and other doesn't.
     * Should return false.
     */
    @Test
    public void testEquals_withOneNullClazz_returnsFalse() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(null, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        assertFalse(pair1.equals(pair2));
        assertFalse(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method when one has null member and other doesn't.
     * Should return false.
     */
    @Test
    public void testEquals_withOneNullMember_returnsFalse() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, null);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        assertFalse(pair1.equals(pair2));
        assertFalse(pair2.equals(pair1)); // symmetric
    }

    /**
     * Tests equals method transitivity.
     * If pair1.equals(pair2) and pair2.equals(pair3), then pair1.equals(pair3).
     */
    @Test
    public void testEquals_transitivity() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair3 = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        assertTrue(pair1.equals(pair2));
        assertTrue(pair2.equals(pair3));
        assertTrue(pair1.equals(pair3)); // transitive
    }

    /**
     * Tests equals method consistency.
     * Multiple invocations should return the same result.
     */
    @Test
    public void testEquals_consistency() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            assertTrue(pair1.equals(pair2));
        }
    }

    // ==================== hashCode() Tests ====================

    /**
     * Tests hashCode method returns consistent value.
     * Multiple invocations should return the same hash code.
     */
    @Test
    public void testHashCode_consistency() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        int hash1 = pair.hashCode();
        int hash2 = pair.hashCode();
        int hash3 = pair.hashCode();

        // Assert
        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
    }

    /**
     * Tests hashCode method with equal objects.
     * Equal objects must have equal hash codes.
     */
    @Test
    public void testHashCode_equalObjectsHaveEqualHashCodes() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, mockMember);

        // Act
        int hash1 = pair1.hashCode();
        int hash2 = pair2.hashCode();

        // Assert
        assertTrue(pair1.equals(pair2));
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with different clazz.
     * Different objects typically have different hash codes (though not required).
     */
    @Test
    public void testHashCode_withDifferentClazz_typicallyDifferent() {
        // Arrange
        Clazz otherClazz = mock(Clazz.class);
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(otherClazz, mockMember);

        // Act
        int hash1 = pair1.hashCode();
        int hash2 = pair2.hashCode();

        // Note: We don't assert inequality because hash codes are allowed to collide
        // We just verify they can be computed
        assertNotNull(Integer.valueOf(hash1));
        assertNotNull(Integer.valueOf(hash2));
    }

    /**
     * Tests hashCode method with different member.
     * Different objects typically have different hash codes (though not required).
     */
    @Test
    public void testHashCode_withDifferentMember_typicallyDifferent() {
        // Arrange
        Member otherMember = mock(Member.class);
        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, otherMember);

        // Act
        int hash1 = pair1.hashCode();
        int hash2 = pair2.hashCode();

        // Note: We don't assert inequality because hash codes are allowed to collide
        // We just verify they can be computed
        assertNotNull(Integer.valueOf(hash1));
        assertNotNull(Integer.valueOf(hash2));
    }

    /**
     * Tests hashCode method with null clazz.
     * Should handle null gracefully and compute a hash code.
     */
    @Test
    public void testHashCode_withNullClazz_computesHashCode() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(null, mockMember);

        // Act
        int hash = pair.hashCode();

        // Assert - just verify it computes without error
        assertNotNull(Integer.valueOf(hash));
    }

    /**
     * Tests hashCode method with null member.
     * Should handle null gracefully and compute a hash code.
     */
    @Test
    public void testHashCode_withNullMember_computesHashCode() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(mockClazz, null);

        // Act
        int hash = pair.hashCode();

        // Assert - just verify it computes without error
        assertNotNull(Integer.valueOf(hash));
    }

    /**
     * Tests hashCode method with both null clazz and member.
     * Should handle nulls gracefully and compute a hash code.
     */
    @Test
    public void testHashCode_withBothNull_computesHashCode() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(null, null);

        // Act
        int hash = pair.hashCode();

        // Assert - just verify it computes without error
        assertNotNull(Integer.valueOf(hash));
    }

    /**
     * Tests hashCode method with pairs having same null values.
     * Equal objects must have equal hash codes.
     */
    @Test
    public void testHashCode_withSameNullValues_haveEqualHashCodes() {
        // Arrange
        ClassMemberPair pair1 = new ClassMemberPair(null, null);
        ClassMemberPair pair2 = new ClassMemberPair(null, null);

        // Act
        int hash1 = pair1.hashCode();
        int hash2 = pair2.hashCode();

        // Assert
        assertTrue(pair1.equals(pair2));
        assertEquals(hash1, hash2);
    }

    // ==================== toString() Tests ====================

    /**
     * Tests toString method returns properly formatted string.
     * Format should be: className.memberNameDescriptor
     */
    @Test
    public void testToString_withValidClazzAndMember_returnsFormattedString() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("testMethod");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals("TestClass.testMethod()V", result);
        verify(mockClazz, times(1)).getName();
        verify(mockMember, times(1)).getName(mockClazz);
        verify(mockMember, times(1)).getDescriptor(mockClazz);
    }

    /**
     * Tests toString method with field member.
     * Should include field descriptor.
     */
    @Test
    public void testToString_withFieldMember_includesFieldDescriptor() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("testField");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("I");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals("TestClass.testFieldI", result);
    }

    /**
     * Tests toString method with method member with parameters.
     * Should include full method descriptor.
     */
    @Test
    public void testToString_withMethodWithParameters_includesFullDescriptor() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("processData");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("(Ljava/lang/String;I)Z");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals("TestClass.processData(Ljava/lang/String;I)Z", result);
    }

    /**
     * Tests toString method when clazz name is empty.
     * Should still produce valid output.
     */
    @Test
    public void testToString_withEmptyClassName_returnsValidString() {
        // Arrange
        when(mockClazz.getName()).thenReturn("");
        when(mockMember.getName(mockClazz)).thenReturn("member");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals(".member()V", result);
    }

    /**
     * Tests toString method when member name is empty.
     * Should still produce valid output.
     */
    @Test
    public void testToString_withEmptyMemberName_returnsValidString() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals("TestClass.()V", result);
    }

    /**
     * Tests toString method when descriptor is empty.
     * Should still produce valid output.
     */
    @Test
    public void testToString_withEmptyDescriptor_returnsValidString() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("member");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals("TestClass.member", result);
    }

    /**
     * Tests toString method when clazz is null.
     * Should throw NullPointerException when trying to call getName on null clazz.
     */
    @Test
    public void testToString_withNullClazz_throwsNullPointerException() {
        // Arrange
        ClassMemberPair pair = new ClassMemberPair(null, mockMember);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> pair.toString());
    }

    /**
     * Tests toString method when member is null.
     * Should throw NullPointerException when trying to call methods on null member.
     */
    @Test
    public void testToString_withNullMember_throwsNullPointerException() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> pair.toString());
    }

    /**
     * Tests toString method called multiple times.
     * Should return consistent results.
     */
    @Test
    public void testToString_calledMultipleTimes_returnsConsistentResult() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("testMethod");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result1 = pair.toString();
        String result2 = pair.toString();
        String result3 = pair.toString();

        // Assert
        assertEquals("TestClass.testMethod()V", result1);
        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    /**
     * Tests toString method with fully qualified class name.
     * Should include package in the output.
     */
    @Test
    public void testToString_withFullyQualifiedClassName_includesPackage() {
        // Arrange
        when(mockClazz.getName()).thenReturn("com/example/test/TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("method");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals("com/example/test/TestClass.method()V", result);
    }

    /**
     * Tests toString method with special characters in names.
     * Should handle special characters properly.
     */
    @Test
    public void testToString_withSpecialCharactersInNames_handlesCorrectly() {
        // Arrange
        when(mockClazz.getName()).thenReturn("Test$InnerClass");
        when(mockMember.getName(mockClazz)).thenReturn("<init>");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");
        ClassMemberPair pair = new ClassMemberPair(mockClazz, mockMember);

        // Act
        String result = pair.toString();

        // Assert
        assertEquals("Test$InnerClass.<init>()V", result);
    }

    // ==================== Integration Tests ====================

    /**
     * Tests that two equal pairs have the same hash code and toString.
     * Verifies consistency across equals, hashCode, and toString.
     */
    @Test
    public void testIntegration_equalPairs_haveConsistentBehavior() {
        // Arrange
        when(mockClazz.getName()).thenReturn("TestClass");
        when(mockMember.getName(mockClazz)).thenReturn("method");
        when(mockMember.getDescriptor(mockClazz)).thenReturn("()V");

        ClassMemberPair pair1 = new ClassMemberPair(mockClazz, mockMember);
        ClassMemberPair pair2 = new ClassMemberPair(mockClazz, mockMember);

        // Act & Assert
        assertTrue(pair1.equals(pair2));
        assertEquals(pair1.hashCode(), pair2.hashCode());
        assertEquals(pair1.toString(), pair2.toString());
    }

    /**
     * Tests creating multiple pairs with different combinations.
     * Verifies that the class handles various scenarios correctly.
     */
    @Test
    public void testIntegration_multiplePairs_workIndependently() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);

        // Act
        ClassMemberPair pair1 = new ClassMemberPair(clazz1, member1);
        ClassMemberPair pair2 = new ClassMemberPair(clazz2, member2);
        ClassMemberPair pair3 = new ClassMemberPair(clazz1, member2);
        ClassMemberPair pair4 = new ClassMemberPair(clazz2, member1);

        // Assert - all pairs should be independent
        assertNotNull(pair1);
        assertNotNull(pair2);
        assertNotNull(pair3);
        assertNotNull(pair4);

        assertFalse(pair1.equals(pair2));
        assertFalse(pair1.equals(pair3));
        assertFalse(pair1.equals(pair4));
        assertFalse(pair2.equals(pair3));
        assertFalse(pair2.equals(pair4));
        assertFalse(pair3.equals(pair4));
    }
}
