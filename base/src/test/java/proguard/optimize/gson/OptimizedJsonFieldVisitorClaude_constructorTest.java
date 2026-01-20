package proguard.optimize.gson;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.constant.*;
import proguard.classfile.visitor.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the constructor of {@link OptimizedJsonFieldVisitor}.
 * Tests the constructor: <init>.(Lproguard/classfile/visitor/ClassVisitor;Lproguard/classfile/visitor/MemberVisitor;)V
 *
 * The OptimizedJsonFieldVisitor constructor takes a ClassVisitor and a MemberVisitor as parameters,
 * which are used to delegate visited classes and fields respectively.
 */
public class OptimizedJsonFieldVisitorClaude_constructorTest {

    // =========================================================================
    // Tests for constructor: <init>.(Lproguard/classfile/visitor/ClassVisitor;Lproguard/classfile/visitor/MemberVisitor;)V
    // =========================================================================

    /**
     * Tests that the constructor successfully creates an instance with valid ClassVisitor and MemberVisitor.
     */
    @Test
    public void testConstructor_withValidVisitors_createsInstance() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(classVisitor, memberVisitor);

        // Assert
        assertNotNull(visitor, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(classVisitor, memberVisitor);

        // Assert
        assertTrue(visitor instanceof ClassVisitor,
                "OptimizedJsonFieldVisitor should implement ClassVisitor");
    }

    /**
     * Tests that the constructor creates an instance that implements MemberVisitor.
     */
    @Test
    public void testConstructor_implementsMemberVisitor() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(classVisitor, memberVisitor);

        // Assert
        assertTrue(visitor instanceof MemberVisitor,
                "OptimizedJsonFieldVisitor should implement MemberVisitor");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldVisitor(classVisitor, memberVisitor),
                "Constructor should not throw any exception with valid input");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_eachIsIndependent() {
        // Arrange
        ClassVisitor classVisitor1 = new TestClassVisitor();
        MemberVisitor memberVisitor1 = new TestMemberVisitor();
        ClassVisitor classVisitor2 = new TestClassVisitor();
        MemberVisitor memberVisitor2 = new TestMemberVisitor();

        // Act
        OptimizedJsonFieldVisitor visitor1 = new OptimizedJsonFieldVisitor(classVisitor1, memberVisitor1);
        OptimizedJsonFieldVisitor visitor2 = new OptimizedJsonFieldVisitor(classVisitor2, memberVisitor2);
        OptimizedJsonFieldVisitor visitor3 = new OptimizedJsonFieldVisitor(classVisitor1, memberVisitor2);

        // Assert
        assertNotNull(visitor1, "First instance should be created");
        assertNotNull(visitor2, "Second instance should be created");
        assertNotNull(visitor3, "Third instance should be created");
        assertNotSame(visitor1, visitor2, "Instances should be distinct");
        assertNotSame(visitor2, visitor3, "Instances should be distinct");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(
                    new TestClassVisitor(),
                    new TestMemberVisitor());
            assertNotNull(visitor, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests constructor with null ClassVisitor parameter.
     * This should not throw during construction, but may throw during usage.
     */
    @Test
    public void testConstructor_withNullClassVisitor_createsInstance() {
        // Arrange
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldVisitor(null, memberVisitor),
                "Constructor should accept null ClassVisitor without throwing");
    }

    /**
     * Tests constructor with null MemberVisitor parameter.
     * This should not throw during construction, but may throw during usage.
     */
    @Test
    public void testConstructor_withNullMemberVisitor_createsInstance() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldVisitor(classVisitor, null),
                "Constructor should accept null MemberVisitor without throwing");
    }

    /**
     * Tests constructor with both parameters null.
     */
    @Test
    public void testConstructor_withBothParametersNull_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldVisitor(null, null),
                "Constructor should accept null parameters without throwing");
    }

    /**
     * Tests that multiple visitors can share the same ClassVisitor instance.
     */
    @Test
    public void testConstructor_sharedClassVisitor_visitorsShareReference() {
        // Arrange
        ClassVisitor sharedClassVisitor = new TestClassVisitor();
        MemberVisitor memberVisitor1 = new TestMemberVisitor();
        MemberVisitor memberVisitor2 = new TestMemberVisitor();

        // Act
        OptimizedJsonFieldVisitor visitor1 = new OptimizedJsonFieldVisitor(sharedClassVisitor, memberVisitor1);
        OptimizedJsonFieldVisitor visitor2 = new OptimizedJsonFieldVisitor(sharedClassVisitor, memberVisitor2);

        // Assert
        assertNotNull(visitor1);
        assertNotNull(visitor2);
        assertNotSame(visitor1, visitor2, "Visitors should be different instances");
    }

    /**
     * Tests that multiple visitors can share the same MemberVisitor instance.
     */
    @Test
    public void testConstructor_sharedMemberVisitor_visitorsShareReference() {
        // Arrange
        ClassVisitor classVisitor1 = new TestClassVisitor();
        ClassVisitor classVisitor2 = new TestClassVisitor();
        MemberVisitor sharedMemberVisitor = new TestMemberVisitor();

        // Act
        OptimizedJsonFieldVisitor visitor1 = new OptimizedJsonFieldVisitor(classVisitor1, sharedMemberVisitor);
        OptimizedJsonFieldVisitor visitor2 = new OptimizedJsonFieldVisitor(classVisitor2, sharedMemberVisitor);

        // Assert
        assertNotNull(visitor1);
        assertNotNull(visitor2);
        assertNotSame(visitor1, visitor2, "Visitors should be different instances");
    }

    /**
     * Tests that the constructor properly stores the ClassVisitor parameter
     * by verifying it's used during visitProgramClass.
     */
    @Test
    public void testConstructor_classVisitorParameter_isUsedDuringVisitation() {
        // Arrange
        TestClassVisitor testClassVisitor = new TestClassVisitor();
        MemberVisitor memberVisitor = new TestMemberVisitor();
        OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(testClassVisitor, memberVisitor);
        ProgramClass programClass = createMinimalProgramClass("TestClass");

        // Act
        visitor.visitProgramClass(programClass);

        // Assert
        assertTrue(testClassVisitor.wasVisited, "ClassVisitor should be invoked when visiting a ProgramClass");
    }

    /**
     * Tests that the constructor properly stores the MemberVisitor parameter
     * by verifying it's used during visitProgramField.
     */
    @Test
    public void testConstructor_memberVisitorParameter_isUsedDuringVisitation() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();
        TestMemberVisitor testMemberVisitor = new TestMemberVisitor();
        OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(classVisitor, testMemberVisitor);
        ProgramClass programClass = createProgramClassWithField("TestClass", "testField");

        // Act
        visitor.visitProgramClass(programClass);

        // Assert
        assertTrue(testMemberVisitor.wasVisited, "MemberVisitor should be invoked when visiting fields");
    }

    /**
     * Tests that the constructor creates an instance that can accept the same visitor for both parameters.
     */
    @Test
    public void testConstructor_sameVisitorForBothParameters_createsInstance() {
        // Arrange
        TestDualVisitor dualVisitor = new TestDualVisitor();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedJsonFieldVisitor(dualVisitor, dualVisitor),
                "Constructor should accept the same visitor for both parameters");
    }

    /**
     * Tests that the constructor works with anonymous ClassVisitor.
     */
    @Test
    public void testConstructor_withAnonymousClassVisitor_createsInstance() {
        // Arrange
        ClassVisitor anonymousClassVisitor = new ClassVisitor() {
            @Override
            public void visitAnyClass(Clazz clazz) {}
            @Override
            public void visitProgramClass(ProgramClass programClass) {}
            @Override
            public void visitLibraryClass(LibraryClass libraryClass) {}
        };
        MemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(anonymousClassVisitor, memberVisitor);

        // Assert
        assertNotNull(visitor, "Constructor should work with anonymous ClassVisitor");
    }

    /**
     * Tests that the constructor works with anonymous MemberVisitor.
     */
    @Test
    public void testConstructor_withAnonymousMemberVisitor_createsInstance() {
        // Arrange
        ClassVisitor classVisitor = new TestClassVisitor();
        MemberVisitor anonymousMemberVisitor = new MemberVisitor() {
            @Override
            public void visitAnyMember(Clazz clazz, Member member) {}
            @Override
            public void visitProgramField(ProgramClass programClass, ProgramField programField) {}
            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {}
            @Override
            public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
            @Override
            public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
        };

        // Act
        OptimizedJsonFieldVisitor visitor = new OptimizedJsonFieldVisitor(classVisitor, anonymousMemberVisitor);

        // Assert
        assertNotNull(visitor, "Constructor should work with anonymous MemberVisitor");
    }

    // =========================================================================
    // Helper classes and methods
    // =========================================================================

    /**
     * Test implementation of ClassVisitor for verifying constructor behavior.
     */
    private static class TestClassVisitor implements ClassVisitor {
        boolean wasVisited = false;

        @Override
        public void visitAnyClass(Clazz clazz) {
            wasVisited = true;
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            wasVisited = true;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            wasVisited = true;
        }
    }

    /**
     * Test implementation of MemberVisitor for verifying constructor behavior.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        boolean wasVisited = false;

        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            wasVisited = true;
        }

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            wasVisited = true;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            wasVisited = true;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            wasVisited = true;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            wasVisited = true;
        }
    }

    /**
     * Test implementation that implements both ClassVisitor and MemberVisitor.
     */
    private static class TestDualVisitor implements ClassVisitor, MemberVisitor {
        @Override
        public void visitAnyClass(Clazz clazz) {}

        @Override
        public void visitProgramClass(ProgramClass programClass) {}

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {}

        @Override
        public void visitAnyMember(Clazz clazz, Member member) {}

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {}

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {}

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
    }

    /**
     * Creates a minimal but valid ProgramClass for testing.
     *
     * @param className the name of the class (e.g., "TestClass" or "com/example/TestClass")
     * @return a configured ProgramClass instance
     */
    private ProgramClass createMinimalProgramClass(String className) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        Constant[] constantPool = new Constant[10];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;
        programClass.fields = new ProgramField[0];
        programClass.u2fieldsCount = 0;

        return programClass;
    }

    /**
     * Creates a ProgramClass with a single field.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return a configured ProgramClass instance with one field
     */
    private ProgramClass createProgramClassWithField(String className, String fieldName) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with class name and field info
        Constant[] constantPool = new Constant[20];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);
        constantPool[3] = new Utf8Constant(fieldName);
        constantPool[4] = new Utf8Constant("I"); // field descriptor for int

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 20;

        // Create a field
        ProgramField field = new ProgramField();
        field.u2accessFlags = 0;
        field.u2nameIndex = 3;
        field.u2descriptorIndex = 4;
        field.u2attributesCount = 0;
        field.attributes = new Attribute[0];

        programClass.fields = new ProgramField[]{field};
        programClass.u2fieldsCount = 1;

        return programClass;
    }
}
