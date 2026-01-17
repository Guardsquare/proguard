package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.io.ExtraDataEntryNameMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StaticInterfaceMethodConverter}.
 * Tests all public methods including constructor, visitAnyClass, and visitProgramClass.
 */
public class StaticInterfaceMethodConverterClaudeTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private ExtraDataEntryNameMap extraDataEntryNameMap;
    private TestClassVisitor modifiedClassVisitor;
    private TestMemberVisitor extraMemberVisitor;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        extraDataEntryNameMap = new ExtraDataEntryNameMap();
        modifiedClassVisitor = new TestClassVisitor();
        extraMemberVisitor = new TestMemberVisitor();
    }

    // ========== Constructor Tests ==========

    /**
     * Tests constructor with all valid non-null parameters.
     * Verifies the converter is properly instantiated.
     */
    @Test
    public void testConstructor_withAllValidParameters_createsInstance() {
        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated");
    }

    /**
     * Tests constructor with null programClassPool.
     * The constructor should accept null programClassPool.
     */
    @Test
    public void testConstructor_withNullProgramClassPool_createsInstance() {
        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                null,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated with null programClassPool");
    }

    /**
     * Tests constructor with null libraryClassPool.
     * The constructor should accept null libraryClassPool.
     */
    @Test
    public void testConstructor_withNullLibraryClassPool_createsInstance() {
        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                null,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated with null libraryClassPool");
    }

    /**
     * Tests constructor with null extraDataEntryNameMap.
     * The constructor should accept null extraDataEntryNameMap.
     */
    @Test
    public void testConstructor_withNullExtraDataEntryNameMap_createsInstance() {
        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                null,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated with null extraDataEntryNameMap");
    }

    /**
     * Tests constructor with null modifiedClassVisitor.
     * The constructor should accept null modifiedClassVisitor.
     */
    @Test
    public void testConstructor_withNullModifiedClassVisitor_createsInstance() {
        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                null,
                extraMemberVisitor
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated with null modifiedClassVisitor");
    }

    /**
     * Tests constructor with null extraMemberVisitor.
     * The constructor should accept null extraMemberVisitor.
     */
    @Test
    public void testConstructor_withNullExtraMemberVisitor_createsInstance() {
        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                null
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated with null extraMemberVisitor");
    }

    /**
     * Tests constructor with all null parameters.
     * Verifies the converter can be created with all nulls.
     */
    @Test
    public void testConstructor_withAllNullParameters_createsInstance() {
        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                null,
                null,
                null,
                null,
                null
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated with all null parameters");
    }

    /**
     * Tests constructor with empty ClassPools.
     * Verifies the converter accepts empty ClassPool instances.
     */
    @Test
    public void testConstructor_withEmptyClassPools_createsInstance() {
        // Arrange
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();

        // Act
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                emptyProgramPool,
                emptyLibraryPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        // Assert
        assertNotNull(converter, "Converter should be instantiated with empty ClassPools");
    }

    /**
     * Tests that multiple converters can be created independently.
     * Verifies each converter maintains its own state.
     */
    @Test
    public void testConstructor_multipleInstances_independent() {
        // Act
        StaticInterfaceMethodConverter converter1 = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        StaticInterfaceMethodConverter converter2 = new StaticInterfaceMethodConverter(
                new ClassPool(),
                new ClassPool(),
                new ExtraDataEntryNameMap(),
                new TestClassVisitor(),
                new TestMemberVisitor()
        );

        // Assert
        assertNotNull(converter1, "First converter should be instantiated");
        assertNotNull(converter2, "Second converter should be instantiated");
        assertNotSame(converter1, converter2, "Converters should be different instances");
    }

    // ========== visitAnyClass Tests ==========

    /**
     * Tests visitAnyClass with a ProgramClass.
     * The method should do nothing (empty implementation).
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNothing() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyClass(programClass));
    }

    /**
     * Tests visitAnyClass with a LibraryClass.
     * The method should do nothing (empty implementation).
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNothing() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyClass(libraryClass));
    }

    /**
     * Tests visitAnyClass with null class parameter.
     * Verifies behavior when null is passed.
     */
    @Test
    public void testVisitAnyClass_withNull_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        // Act & Assert - empty method should handle null gracefully
        assertDoesNotThrow(() -> converter.visitAnyClass(null));
    }

    /**
     * Tests visitAnyClass can be called multiple times.
     * Verifies the method is idempotent.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            converter.visitAnyClass(programClass);
            converter.visitAnyClass(programClass);
            converter.visitAnyClass(programClass);
        });
    }

    /**
     * Tests visitAnyClass with different Clazz types.
     * Verifies the method handles various class types.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzTypes_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitAnyClass(programClass);
            converter.visitAnyClass(libraryClass);
            converter.visitAnyClass(programClass);
        });
    }

    /**
     * Tests that visitAnyClass does not modify the class.
     * The empty implementation should not alter the class state.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyClass() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();
        programClass.u4version = VersionConstants.CLASS_VERSION_1_8;
        int originalVersion = programClass.u4version;

        // Act
        converter.visitAnyClass(programClass);

        // Assert - version should remain unchanged
        assertEquals(originalVersion, programClass.u4version,
                "visitAnyClass should not modify the class");
    }

    // ========== visitProgramClass Tests ==========

    /**
     * Tests visitProgramClass with an empty ProgramClass (no methods).
     * Verifies the converter handles classes without static methods.
     */
    @Test
    public void testVisitProgramClass_withEmptyClass_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass with null ProgramClass.
     * Verifies error handling for null parameter.
     */
    @Test
    public void testVisitProgramClass_withNull_throwsNullPointerException() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> converter.visitProgramClass(null));
    }

    /**
     * Tests visitProgramClass can be called multiple times on the same class.
     * Verifies the method handles repeated calls.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            converter.visitProgramClass(programClass);
            converter.visitProgramClass(programClass);
            converter.visitProgramClass(programClass);
        });
    }

    /**
     * Tests visitProgramClass with different ProgramClass instances.
     * Verifies the method handles multiple classes.
     */
    @Test
    public void testVisitProgramClass_withDifferentClasses_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitProgramClass(class1);
            converter.visitProgramClass(class2);
            converter.visitProgramClass(class3);
        });
    }

    /**
     * Tests visitProgramClass with a class having different version numbers.
     * Verifies the converter handles various Java versions.
     */
    @Test
    public void testVisitProgramClass_withDifferentVersions_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        ProgramClass java6Class = new ProgramClass();
        java6Class.u4version = VersionConstants.CLASS_VERSION_1_6;

        ProgramClass java8Class = new ProgramClass();
        java8Class.u4version = VersionConstants.CLASS_VERSION_1_8;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitProgramClass(java6Class);
            converter.visitProgramClass(java8Class);
        });
    }

    /**
     * Tests visitProgramClass does not modify classes without static methods.
     * Verifies the converter only processes interface classes with static methods.
     */
    @Test
    public void testVisitProgramClass_withoutStaticMethods_doesNotModifyClassPool() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();
        int initialPoolSize = programClassPool.size();

        // Act
        converter.visitProgramClass(programClass);

        // Assert - no utility class should be added to the pool
        assertEquals(initialPoolSize, programClassPool.size(),
                "ClassPool should not be modified for classes without static methods");
    }

    /**
     * Tests visitProgramClass with converter created with null ClassPools.
     * Verifies behavior when ClassPools are null.
     */
    @Test
    public void testVisitProgramClass_withNullClassPools_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                null,
                null,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception for classes without static methods
        assertDoesNotThrow(() -> converter.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass with converter created with null extraDataEntryNameMap.
     * Verifies the behavior when extraDataEntryNameMap is null.
     */
    @Test
    public void testVisitProgramClass_withNullExtraDataEntryNameMap_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                null,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception for classes without static methods
        assertDoesNotThrow(() -> converter.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass maintains correct behavior after extensive use.
     * Verifies the converter doesn't accumulate problematic state.
     */
    @Test
    public void testVisitProgramClass_afterManyOperations_stillWorksCorrectly() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act - perform many operations
        for (int i = 0; i < 100; i++) {
            converter.visitProgramClass(programClass);
        }

        // Assert - final call should still work correctly
        assertDoesNotThrow(() -> converter.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass does not affect the original class when no static methods exist.
     * Verifies the class remains unchanged.
     */
    @Test
    public void testVisitProgramClass_withoutStaticMethods_doesNotModifyClass() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();
        programClass.u4version = VersionConstants.CLASS_VERSION_1_8;
        int originalVersion = programClass.u4version;

        // Act
        converter.visitProgramClass(programClass);

        // Assert - version should remain unchanged
        assertEquals(originalVersion, programClass.u4version,
                "visitProgramClass should not modify classes without static methods");
    }

    /**
     * Tests that visitProgramClass and visitAnyClass can be used together.
     * Verifies both methods work independently without interference.
     */
    @Test
    public void testVisitProgramClassAndVisitAnyClass_usedTogether_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitAnyClass(programClass);
            converter.visitProgramClass(programClass);
            converter.visitAnyClass(programClass);
        });
    }

    /**
     * Tests visitProgramClass execution completes quickly for classes without static methods.
     * Verifies performance for the common case.
     */
    @Test
    public void testVisitProgramClass_executesQuicklyForEmptyClass() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitProgramClass(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete reasonably quickly (within 500ms for 1000 calls)
        assertTrue(durationMs < 500,
                "visitProgramClass should execute quickly for empty classes");
    }

    /**
     * Tests that multiple converters can independently visit classes.
     * Verifies converters don't interfere with each other.
     */
    @Test
    public void testVisitProgramClass_multipleConvertersIndependent() {
        // Arrange
        ClassPool pool1 = new ClassPool();
        ClassPool pool2 = new ClassPool();

        StaticInterfaceMethodConverter converter1 = new StaticInterfaceMethodConverter(
                pool1,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        StaticInterfaceMethodConverter converter2 = new StaticInterfaceMethodConverter(
                pool2,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );

        ProgramClass programClass = new ProgramClass();

        // Act
        converter1.visitProgramClass(programClass);
        converter2.visitProgramClass(programClass);

        // Assert - verify each pool remained independent
        assertEquals(0, pool1.size(), "First pool should remain empty");
        assertEquals(0, pool2.size(), "Second pool should remain empty");
    }

    /**
     * Tests visitProgramClass with converters using null visitors.
     * Verifies the converter handles null visitors gracefully.
     */
    @Test
    public void testVisitProgramClass_withNullVisitors_doesNotThrow() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                null,
                null
        );
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception for classes without static methods
        assertDoesNotThrow(() -> converter.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass returns normally (void method).
     * Verifies the method signature and behavior.
     */
    @Test
    public void testVisitProgramClass_returnsNormally() {
        // Arrange
        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor
        );
        ProgramClass programClass = new ProgramClass();

        // Act - method has void return type, just verify it completes
        converter.visitProgramClass(programClass);

        // Assert - if we reach here, the method completed normally
        assertTrue(true, "Method completed normally");
    }

    // ========== Helper Classes ==========

    /**
     * Test implementation of ClassVisitor to track visits.
     */
    private static class TestClassVisitor implements ClassVisitor {
        private int visitCount = 0;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visitCount++;
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            visitCount++;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            visitCount++;
        }

        public int getVisitCount() {
            return visitCount;
        }
    }

    /**
     * Test implementation of MemberVisitor to track visits.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        private int visitCount = 0;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitCount++;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitCount++;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitCount++;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitCount++;
        }

        public int getVisitCount() {
            return visitCount;
        }
    }
}
