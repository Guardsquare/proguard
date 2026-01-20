package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LocalOrAnonymousClassChecker}.
 * Tests all methods including constructor, isLocalOrAnonymous, visitAnyClass,
 * visitProgramClass, and visitInnerClassesInfo.
 *
 * The LocalOrAnonymousClassChecker determines whether a class is a local or anonymous
 * inner class by examining InnerClassesInfo entries.
 */
public class LocalOrAnonymousClassCheckerClaudeTest {

    private LocalOrAnonymousClassChecker checker;

    @BeforeEach
    public void setUp() {
        checker = new LocalOrAnonymousClassChecker();
    }

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor initializes successfully.
     */
    @Test
    public void testConstructor_initializesSuccessfully() {
        // Act
        LocalOrAnonymousClassChecker newChecker = new LocalOrAnonymousClassChecker();

        // Assert
        assertNotNull(newChecker, "Checker should be instantiated successfully");
        assertFalse(newChecker.isLocalOrAnonymous(), "Initial state should be false");
    }

    /**
     * Tests that the constructor initializes with false as default value.
     */
    @Test
    public void testConstructor_defaultsToFalse() {
        // Act
        LocalOrAnonymousClassChecker newChecker = new LocalOrAnonymousClassChecker();

        // Assert
        assertFalse(newChecker.isLocalOrAnonymous(), "Default value should be false");
    }

    /**
     * Tests that multiple instances are independent.
     */
    @Test
    public void testConstructor_multipleInstancesAreIndependent() {
        // Act
        LocalOrAnonymousClassChecker checker1 = new LocalOrAnonymousClassChecker();
        LocalOrAnonymousClassChecker checker2 = new LocalOrAnonymousClassChecker();

        // Assert
        assertNotSame(checker1, checker2, "Instances should be different");
        assertFalse(checker1.isLocalOrAnonymous());
        assertFalse(checker2.isLocalOrAnonymous());
    }

    // ========== isLocalOrAnonymous Tests ==========

    /**
     * Tests isLocalOrAnonymous returns false by default.
     */
    @Test
    public void testIsLocalOrAnonymous_defaultValue() {
        // Assert
        assertFalse(checker.isLocalOrAnonymous(), "Default value should be false");
    }

    /**
     * Tests isLocalOrAnonymous can be called multiple times.
     */
    @Test
    public void testIsLocalOrAnonymous_calledMultipleTimes() {
        // Act & Assert
        assertFalse(checker.isLocalOrAnonymous());
        assertFalse(checker.isLocalOrAnonymous());
        assertFalse(checker.isLocalOrAnonymous());
    }

    // ========== visitAnyClass Tests ==========

    /**
     * Tests visitAnyClass with a valid mock clazz.
     * The method has an empty implementation and should do nothing.
     */
    @Test
    public void testVisitAnyClass_withValidMock() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> checker.visitAnyClass(clazz));

        // State should remain unchanged
        assertFalse(checker.isLocalOrAnonymous());
    }

    /**
     * Tests visitAnyClass with ProgramClass.
     */
    @Test
    public void testVisitAnyClass_withProgramClass() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        checker.visitAnyClass(programClass);

        // Assert - State should remain unchanged
        assertFalse(checker.isLocalOrAnonymous());
    }

    /**
     * Tests visitAnyClass called multiple times.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(clazz);
            checker.visitAnyClass(clazz);
            checker.visitAnyClass(clazz);
        });
    }

    /**
     * Tests visitAnyClass does not interact with the clazz parameter.
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithClazz() {
        // Arrange
        Clazz clazz = mock(Clazz.class);

        // Act
        checker.visitAnyClass(clazz);

        // Assert - No interactions expected
        verifyNoInteractions(clazz);
    }

    // ========== visitProgramClass Tests ==========

    /**
     * Tests visitProgramClass resets the localOrAnonymous flag to false.
     */
    @Test
    public void testVisitProgramClass_resetsFlag() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Simulate that the flag was previously set to true by using reflection
        // But first, let's test without that assumption

        // Act
        checker.visitProgramClass(programClass);

        // Assert - The flag should be false after visiting
        assertFalse(checker.isLocalOrAnonymous(), "Flag should be reset to false");

        // Verify attributesAccept was called
        verify(programClass, times(1)).attributesAccept(any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramClass calls attributesAccept with AllInnerClassesInfoVisitor.
     */
    @Test
    public void testVisitProgramClass_callsAttributesAccept() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        verify(programClass, times(1)).attributesAccept(any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramClass with a class that has no InnerClassesAttribute.
     */
    @Test
    public void testVisitProgramClass_withNoInnerClassesAttribute() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;
        programClass.u2attributesCount = 0;
        programClass.attributes = new Attribute[0];

        // Act
        checker.visitProgramClass(programClass);

        // Assert - Should remain false since no inner classes info
        assertFalse(checker.isLocalOrAnonymous());
    }

    /**
     * Tests visitProgramClass with a regular (non-inner) class.
     */
    @Test
    public void testVisitProgramClass_withRegularClass() {
        // Arrange
        ProgramClass programClass = createProgramClassWithInnerClassesInfo(
            /* thisClassIndex */ 1,
            /* innerClassIndex */ 1,
            /* outerClassIndex */ 2,  // Non-zero
            /* innerNameIndex */ 3    // Non-zero
        );

        // Act
        checker.visitProgramClass(programClass);

        // Assert - Should be false (not local or anonymous)
        assertFalse(checker.isLocalOrAnonymous());
    }

    /**
     * Tests visitProgramClass with an anonymous inner class (outerClassIndex == 0).
     */
    @Test
    public void testVisitProgramClass_withAnonymousClass_zeroOuterClassIndex() {
        // Arrange
        ProgramClass programClass = createProgramClassWithInnerClassesInfo(
            /* thisClassIndex */ 1,
            /* innerClassIndex */ 1,
            /* outerClassIndex */ 0,  // Zero indicates anonymous/local
            /* innerNameIndex */ 3
        );

        // Act
        checker.visitProgramClass(programClass);

        // Assert - Should be true (anonymous)
        assertTrue(checker.isLocalOrAnonymous(), "Should detect anonymous class with zero outer class index");
    }

    /**
     * Tests visitProgramClass with a local inner class (innerNameIndex == 0).
     */
    @Test
    public void testVisitProgramClass_withLocalClass_zeroInnerNameIndex() {
        // Arrange
        ProgramClass programClass = createProgramClassWithInnerClassesInfo(
            /* thisClassIndex */ 1,
            /* innerClassIndex */ 1,
            /* outerClassIndex */ 2,
            /* innerNameIndex */ 0    // Zero indicates local/anonymous
        );

        // Act
        checker.visitProgramClass(programClass);

        // Assert - Should be true (local)
        assertTrue(checker.isLocalOrAnonymous(), "Should detect local class with zero inner name index");
    }

    /**
     * Tests visitProgramClass with both outerClassIndex and innerNameIndex zero.
     */
    @Test
    public void testVisitProgramClass_withBothIndicesZero() {
        // Arrange
        ProgramClass programClass = createProgramClassWithInnerClassesInfo(
            /* thisClassIndex */ 1,
            /* innerClassIndex */ 1,
            /* outerClassIndex */ 0,  // Zero
            /* innerNameIndex */ 0    // Zero
        );

        // Act
        checker.visitProgramClass(programClass);

        // Assert - Should be true (anonymous/local)
        assertTrue(checker.isLocalOrAnonymous(), "Should detect class with both indices zero");
    }

    /**
     * Tests visitProgramClass with InnerClassesInfo not matching thisClass.
     */
    @Test
    public void testVisitProgramClass_withNonMatchingInnerClassIndex() {
        // Arrange
        ProgramClass programClass = createProgramClassWithInnerClassesInfo(
            /* thisClassIndex */ 1,
            /* innerClassIndex */ 2,  // Different from thisClass
            /* outerClassIndex */ 0,
            /* innerNameIndex */ 0
        );

        // Act
        checker.visitProgramClass(programClass);

        // Assert - Should be false (info doesn't apply to this class)
        assertFalse(checker.isLocalOrAnonymous(), "Should not detect when inner class index doesn't match thisClass");
    }

    /**
     * Tests visitProgramClass resets flag even if it was previously true.
     */
    @Test
    public void testVisitProgramClass_resetsPreviousState() {
        // Arrange - First visit sets it to true
        ProgramClass anonymousClass = createProgramClassWithInnerClassesInfo(1, 1, 0, 3);
        checker.visitProgramClass(anonymousClass);
        assertTrue(checker.isLocalOrAnonymous(), "Should be true after first visit");

        // Act - Visit a regular class
        ProgramClass regularClass = createProgramClassWithInnerClassesInfo(2, 2, 3, 4);
        checker.visitProgramClass(regularClass);

        // Assert - Should be reset to false
        assertFalse(checker.isLocalOrAnonymous(), "Should be reset to false on subsequent visit");
    }

    /**
     * Tests visitProgramClass with multiple InnerClassesInfo entries.
     */
    @Test
    public void testVisitProgramClass_withMultipleInnerClassesInfoEntries() {
        // Arrange - Class with multiple inner class entries
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 5;

        InnerClassesInfo info1 = new InnerClassesInfo();
        info1.u2innerClassIndex = 1;  // Different class
        info1.u2outerClassIndex = 0;
        info1.u2innerNameIndex = 0;

        InnerClassesInfo info2 = new InnerClassesInfo();
        info2.u2innerClassIndex = 5;  // This class - matching
        info2.u2outerClassIndex = 0;  // Anonymous
        info2.u2innerNameIndex = 3;

        InnerClassesAttribute attr = new InnerClassesAttribute();
        attr.u2attributeNameIndex = 1;
        attr.u2classesCount = 2;
        attr.classes = new InnerClassesInfo[]{info1, info2};

        programClass.u2attributesCount = 1;
        programClass.attributes = new Attribute[]{attr};

        // Act
        checker.visitProgramClass(programClass);

        // Assert - Should detect the matching anonymous class
        assertTrue(checker.isLocalOrAnonymous(), "Should detect anonymous class from matching entry");
    }

    /**
     * Tests visitProgramClass called multiple times with different classes.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimesWithDifferentClasses() {
        // Arrange
        ProgramClass class1 = createProgramClassWithInnerClassesInfo(1, 1, 0, 3);
        ProgramClass class2 = createProgramClassWithInnerClassesInfo(2, 2, 3, 4);
        ProgramClass class3 = createProgramClassWithInnerClassesInfo(3, 3, 4, 0);

        // Act & Assert
        checker.visitProgramClass(class1);
        assertTrue(checker.isLocalOrAnonymous(), "First class should be local/anonymous");

        checker.visitProgramClass(class2);
        assertFalse(checker.isLocalOrAnonymous(), "Second class should not be local/anonymous");

        checker.visitProgramClass(class3);
        assertTrue(checker.isLocalOrAnonymous(), "Third class should be local/anonymous");
    }

    // ========== visitInnerClassesInfo Tests ==========

    /**
     * Tests visitInnerClassesInfo with a matching anonymous class (zero outer class index).
     */
    @Test
    public void testVisitInnerClassesInfo_withAnonymousClass() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 10;  // Matches thisClass
        info.u2outerClassIndex = 0;   // Anonymous
        info.u2innerNameIndex = 5;

        // Act
        checker.visitInnerClassesInfo(clazz, info);

        // Assert
        assertTrue(checker.isLocalOrAnonymous(), "Should detect anonymous class");
    }

    /**
     * Tests visitInnerClassesInfo with a matching local class (zero inner name index).
     */
    @Test
    public void testVisitInnerClassesInfo_withLocalClass() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 10;  // Matches thisClass
        info.u2outerClassIndex = 5;
        info.u2innerNameIndex = 0;    // Local

        // Act
        checker.visitInnerClassesInfo(clazz, info);

        // Assert
        assertTrue(checker.isLocalOrAnonymous(), "Should detect local class");
    }

    /**
     * Tests visitInnerClassesInfo with both indices zero.
     */
    @Test
    public void testVisitInnerClassesInfo_withBothIndicesZero() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 10;  // Matches thisClass
        info.u2outerClassIndex = 0;
        info.u2innerNameIndex = 0;

        // Act
        checker.visitInnerClassesInfo(clazz, info);

        // Assert
        assertTrue(checker.isLocalOrAnonymous(), "Should detect class with both indices zero");
    }

    /**
     * Tests visitInnerClassesInfo with a regular inner class (non-zero indices).
     */
    @Test
    public void testVisitInnerClassesInfo_withRegularInnerClass() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 10;  // Matches thisClass
        info.u2outerClassIndex = 5;   // Non-zero
        info.u2innerNameIndex = 7;    // Non-zero

        // Act
        checker.visitInnerClassesInfo(clazz, info);

        // Assert
        assertFalse(checker.isLocalOrAnonymous(), "Should not detect regular inner class as local/anonymous");
    }

    /**
     * Tests visitInnerClassesInfo when inner class index doesn't match thisClass.
     */
    @Test
    public void testVisitInnerClassesInfo_withNonMatchingInnerClassIndex() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 15;  // Doesn't match thisClass
        info.u2outerClassIndex = 0;
        info.u2innerNameIndex = 0;

        // Act
        checker.visitInnerClassesInfo(clazz, info);

        // Assert
        assertFalse(checker.isLocalOrAnonymous(), "Should not set flag when index doesn't match");
    }

    /**
     * Tests visitInnerClassesInfo overrides false with true.
     */
    @Test
    public void testVisitInnerClassesInfo_overridesFalseWithTrue() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        // First, a non-matching entry
        InnerClassesInfo info1 = new InnerClassesInfo();
        info1.u2innerClassIndex = 15;
        info1.u2outerClassIndex = 3;
        info1.u2innerNameIndex = 4;

        // Then, a matching anonymous entry
        InnerClassesInfo info2 = new InnerClassesInfo();
        info2.u2innerClassIndex = 10;
        info2.u2outerClassIndex = 0;
        info2.u2innerNameIndex = 5;

        // Act
        assertFalse(checker.isLocalOrAnonymous(), "Should start as false");
        checker.visitInnerClassesInfo(clazz, info1);
        assertFalse(checker.isLocalOrAnonymous(), "Should remain false after non-matching");
        checker.visitInnerClassesInfo(clazz, info2);

        // Assert
        assertTrue(checker.isLocalOrAnonymous(), "Should be true after matching anonymous entry");
    }

    /**
     * Tests visitInnerClassesInfo with LibraryClass.
     * This tests the cast to ProgramClass in the implementation.
     */
    @Test
    public void testVisitInnerClassesInfo_withLibraryClass() {
        // Note: The implementation casts to ProgramClass, so this will work
        // with any Clazz type, but we test with actual ProgramClass
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 10;
        info.u2outerClassIndex = 0;
        info.u2innerNameIndex = 5;

        // Act & Assert - Should work fine
        assertDoesNotThrow(() -> checker.visitInnerClassesInfo(clazz, info));
        assertTrue(checker.isLocalOrAnonymous());
    }

    /**
     * Tests visitInnerClassesInfo called multiple times on same instance.
     */
    @Test
    public void testVisitInnerClassesInfo_calledMultipleTimes() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 10;

        InnerClassesInfo info1 = new InnerClassesInfo();
        info1.u2innerClassIndex = 10;
        info1.u2outerClassIndex = 3;
        info1.u2innerNameIndex = 4;

        InnerClassesInfo info2 = new InnerClassesInfo();
        info2.u2innerClassIndex = 10;
        info2.u2outerClassIndex = 0;
        info2.u2innerNameIndex = 5;

        // Act
        checker.visitInnerClassesInfo(clazz, info1);
        assertFalse(checker.isLocalOrAnonymous());

        checker.visitInnerClassesInfo(clazz, info2);
        assertTrue(checker.isLocalOrAnonymous());
    }

    /**
     * Tests the full visitor pattern flow: visitProgramClass -> visitInnerClassesInfo.
     */
    @Test
    public void testFullVisitorFlow() {
        // Arrange
        ProgramClass programClass = createProgramClassWithInnerClassesInfo(1, 1, 0, 3);

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.isLocalOrAnonymous(), "Full flow should detect anonymous class");
    }

    /**
     * Tests edge case with u2thisClass = 0.
     */
    @Test
    public void testVisitInnerClassesInfo_withZeroThisClass() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 0;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 0;
        info.u2outerClassIndex = 0;
        info.u2innerNameIndex = 0;

        // Act
        checker.visitInnerClassesInfo(clazz, info);

        // Assert
        assertTrue(checker.isLocalOrAnonymous(), "Should detect when all indices are zero");
    }

    /**
     * Tests with high index values.
     */
    @Test
    public void testVisitInnerClassesInfo_withHighIndexValues() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        clazz.u2thisClass = 65535;  // Max u2 value

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = 65535;
        info.u2outerClassIndex = 0;
        info.u2innerNameIndex = 100;

        // Act
        checker.visitInnerClassesInfo(clazz, info);

        // Assert
        assertTrue(checker.isLocalOrAnonymous(), "Should work with high index values");
    }

    /**
     * Tests that checker is reusable across multiple class visits.
     */
    @Test
    public void testChecker_isReusable() {
        // Arrange
        ProgramClass anonymousClass = createProgramClassWithInnerClassesInfo(1, 1, 0, 3);
        ProgramClass regularClass = createProgramClassWithInnerClassesInfo(2, 2, 3, 4);
        ProgramClass localClass = createProgramClassWithInnerClassesInfo(3, 3, 4, 0);

        // Act & Assert - Multiple uses of same checker
        checker.visitProgramClass(anonymousClass);
        assertTrue(checker.isLocalOrAnonymous());

        checker.visitProgramClass(regularClass);
        assertFalse(checker.isLocalOrAnonymous());

        checker.visitProgramClass(localClass);
        assertTrue(checker.isLocalOrAnonymous());

        checker.visitProgramClass(regularClass);
        assertFalse(checker.isLocalOrAnonymous());
    }

    // ========== Helper Methods ==========

    /**
     * Creates a ProgramClass with an InnerClassesAttribute containing the specified indices.
     */
    private ProgramClass createProgramClassWithInnerClassesInfo(
        int thisClassIndex,
        int innerClassIndex,
        int outerClassIndex,
        int innerNameIndex) {

        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = thisClassIndex;

        InnerClassesInfo info = new InnerClassesInfo();
        info.u2innerClassIndex = innerClassIndex;
        info.u2outerClassIndex = outerClassIndex;
        info.u2innerNameIndex = innerNameIndex;
        info.u2innerClassAccessFlags = 0;

        InnerClassesAttribute attr = new InnerClassesAttribute();
        attr.u2attributeNameIndex = 1;
        attr.u2classesCount = 1;
        attr.classes = new InnerClassesInfo[]{info};

        programClass.u2attributesCount = 1;
        programClass.attributes = new Attribute[]{attr};

        return programClass;
    }
}
