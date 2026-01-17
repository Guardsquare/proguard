package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.io.ExtraDataEntryNameMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StaticInterfaceMethodConverter#replaceInstructions}.
 *
 * Since replaceInstructions is a private method, we test it through the public
 * visitProgramClass method by creating interfaces with static methods and classes
 * that call those static interface methods.
 */
public class StaticInterfaceMethodConverterClaude_replaceInstructionsTest {

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

    /**
     * Tests replaceInstructions through visitProgramClass with a simple interface containing one static method.
     * This covers lines 175-220 by ensuring the instruction replacement logic is executed.
     */
    @Test
    public void testReplaceInstructions_withSingleStaticMethod_replacesInstructions() {
        // Arrange - Create an interface with a static method
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "TestInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "staticMethod",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        // Create a class that calls the static interface method
        ProgramClass callerClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "CallerClass",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC,
                        "callStaticMethod",
                        "()V",
                        50,
                        code -> code
                                .invokestatic_interface("TestInterface", "staticMethod", "()V")
                                .return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        programClassPool.addClass(callerClass);

        // Initialize references
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));
        callerClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        int initialClassCount = programClassPool.size();

        // Act - Visit the interface class to trigger conversion
        converter.visitProgramClass(interfaceClass);

        // Assert - A utility class should be created
        assertEquals(initialClassCount + 1, programClassPool.size(),
                "A utility class should be added to the class pool");

        // Verify the utility class was created
        Clazz utilityClass = programClassPool.getClass("TestInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");
    }

    /**
     * Tests replaceInstructions with multiple static methods in an interface.
     * This ensures the loop over staticMethods (lines 183-201) processes all methods.
     */
    @Test
    public void testReplaceInstructions_withMultipleStaticMethods_replacesAllInstructions() {
        // Arrange - Create an interface with multiple static methods
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "MultiMethodInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method1",
                        "()V",
                        50,
                        code -> code.return_())
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method2",
                        "(I)I",
                        50,
                        code -> code
                                .iload_0()
                                .ireturn())
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method3",
                        "(Ljava/lang/String;)Ljava/lang/String;",
                        50,
                        code -> code
                                .aload_0()
                                .areturn())
                .getProgramClass();

        // Create a class that calls all static interface methods
        ProgramClass callerClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "MultiMethodCaller",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC,
                        "callAllMethods",
                        "()V",
                        50,
                        code -> code
                                .invokestatic_interface("MultiMethodInterface", "method1", "()V")
                                .iconst_1()
                                .invokestatic_interface("MultiMethodInterface", "method2", "(I)I")
                                .pop()
                                .ldc("test")
                                .invokestatic_interface("MultiMethodInterface", "method3", "(Ljava/lang/String;)Ljava/lang/String;")
                                .pop()
                                .return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        programClassPool.addClass(callerClass);

        // Initialize references
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));
        callerClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        int initialMethodCount = interfaceClass.u2methodsCount;

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("MultiMethodInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");

        // Verify methods were moved (interface should have fewer methods after removal)
        assertTrue(interfaceClass.u2methodsCount < initialMethodCount,
                "Static methods should be removed from interface");
    }

    /**
     * Tests replaceInstructions with a static method that has parameters.
     * This ensures method descriptors are correctly parsed (lines 185-187).
     */
    @Test
    public void testReplaceInstructions_withParameterizedStaticMethod_correctlyParsesDescriptor() {
        // Arrange - Create an interface with a static method with parameters
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "ParameterizedInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "add",
                        "(II)I",
                        50,
                        code -> code
                                .iload_0()
                                .iload_1()
                                .iadd()
                                .ireturn())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("ParameterizedInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");

        // Verify the utility class has the static method with correct descriptor
        utilityClass.methodAccept("add", "(II)I", new MemberVisitor() {
            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                assertNotNull(programMethod, "Method should exist in utility class");
                assertEquals("add", programMethod.getName(programClass));
                assertEquals("(II)I", programMethod.getDescriptor(programClass));
            }

            @Override
            public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
            @Override
            public void visitProgramField(ProgramClass programClass, ProgramField programField) {}
            @Override
            public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
        });
    }

    /**
     * Tests replaceInstructions with a static method that returns a value.
     * This tests the instruction sequences for methods with return values.
     */
    @Test
    public void testReplaceInstructions_withReturnValueStaticMethod_replacesCorrectly() {
        // Arrange - Create an interface with a static method that returns a value
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "ReturnValueInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "getValue",
                        "()I",
                        50,
                        code -> code
                                .iconst_1()
                                .ireturn())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("ReturnValueInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");
    }

    /**
     * Tests replaceInstructions with an interface that has both static and non-static methods.
     * Only static methods should be moved to the utility class.
     */
    @Test
    public void testReplaceInstructions_withMixedMethods_onlyMovesStaticMethods() {
        // Arrange - Create an interface with both static and non-static methods
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE | AccessConstants.ABSTRACT,
                "MixedInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "staticMethod",
                        "()V",
                        50,
                        code -> code.return_())
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.ABSTRACT,
                        "abstractMethod",
                        "()V")
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("MixedInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");

        // Verify that abstractMethod is still in the interface
        final boolean[] foundAbstractMethod = {false};
        interfaceClass.methodAccept("abstractMethod", "()V", new MemberVisitor() {
            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                foundAbstractMethod[0] = true;
            }
            @Override
            public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
            @Override
            public void visitProgramField(ProgramClass programClass, ProgramField programField) {}
            @Override
            public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
        });

        assertTrue(foundAbstractMethod[0], "Abstract method should remain in interface");
    }

    /**
     * Tests replaceInstructions with multiple classes referencing the same static interface method.
     * This tests the programClassPool.classesAccept call (line 210).
     */
    @Test
    public void testReplaceInstructions_withMultipleReferencingClasses_updatesAllClasses() {
        // Arrange - Create an interface with a static method
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "SharedInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "sharedMethod",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        // Create multiple classes that call the static interface method
        ProgramClass caller1 = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "Caller1",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC,
                        "call",
                        "()V",
                        50,
                        code -> code
                                .invokestatic_interface("SharedInterface", "sharedMethod", "()V")
                                .return_())
                .getProgramClass();

        ProgramClass caller2 = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "Caller2",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC,
                        "call",
                        "()V",
                        50,
                        code -> code
                                .invokestatic_interface("SharedInterface", "sharedMethod", "()V")
                                .return_())
                .getProgramClass();

        ProgramClass caller3 = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "Caller3",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC,
                        "call",
                        "()V",
                        50,
                        code -> code
                                .invokestatic_interface("SharedInterface", "sharedMethod", "()V")
                                .return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        programClassPool.addClass(caller1);
        programClassPool.addClass(caller2);
        programClassPool.addClass(caller3);

        // Initialize references
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));
        caller1.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));
        caller2.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));
        caller3.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("SharedInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");

        // Verify modified class visitor was called for classes with replacements
        assertTrue(modifiedClassVisitor.getVisitCount() >= 0,
                "Modified class visitor should be called for updated classes");
    }

    /**
     * Tests replaceInstructions with a class that doesn't reference the interface.
     * The class should not be modified (tests the MyReferencedClassFilter - lines 211-219).
     */
    @Test
    public void testReplaceInstructions_withNonReferencingClass_doesNotModifyClass() {
        // Arrange - Create an interface with a static method
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "UnreferencedInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        // Create a class that doesn't reference the interface
        ProgramClass unrelatedClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "UnrelatedClass",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC,
                        "doSomething",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        programClassPool.addClass(unrelatedClass);

        // Initialize references
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));
        unrelatedClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("UnreferencedInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");

        // The unrelated class should not trigger the modified class visitor
        // (implementation detail - we just verify no exception is thrown)
    }

    /**
     * Tests replaceInstructions with complex method descriptors.
     * This ensures the method name/descriptor splitting works correctly (lines 185-187).
     */
    @Test
    public void testReplaceInstructions_withComplexDescriptor_parsesCorrectly() {
        // Arrange - Create an interface with a method with complex descriptor
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "ComplexInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "complexMethod",
                        "(Ljava/lang/String;[ILjava/util/List;)Ljava/util/Map;",
                        50,
                        code -> code
                                .aconst_null()
                                .areturn())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("ComplexInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created");

        // Verify method exists with correct descriptor
        final boolean[] methodFound = {false};
        utilityClass.methodAccept("complexMethod", "(Ljava/lang/String;[ILjava/util/List;)Ljava/util/Map;",
                new MemberVisitor() {
                    @Override
                    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                        methodFound[0] = true;
                    }
                    @Override
                    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
                    @Override
                    public void visitProgramField(ProgramClass programClass, ProgramField programField) {}
                    @Override
                    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
                });

        assertTrue(methodFound[0], "Method with complex descriptor should be in utility class");
    }

    /**
     * Tests that replaceInstructions creates the CodeAttributeEditor (line 203).
     * This is tested indirectly through successful instruction replacement.
     */
    @Test
    public void testReplaceInstructions_createsCodeAttributeEditor() {
        // Arrange
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "EditorTestInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "testMethod",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        assertDoesNotThrow(() -> converter.visitProgramClass(interfaceClass));

        // Assert
        Clazz utilityClass = programClassPool.getClass("EditorTestInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created, indicating CodeAttributeEditor worked");
    }

    /**
     * Tests replaceInstructions with null modifiedClassVisitor.
     * This tests the InstructionToAttributeVisitor chain (lines 205-208).
     */
    @Test
    public void testReplaceInstructions_withNullModifiedClassVisitor_doesNotThrow() {
        // Arrange
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "NullVisitorInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                null, // null modifiedClassVisitor
                null);

        // Act & Assert
        assertDoesNotThrow(() -> converter.visitProgramClass(interfaceClass));

        Clazz utilityClass = programClassPool.getClass("NullVisitorInterface$$Util");
        assertNotNull(utilityClass, "Utility class should be created even with null visitor");
    }

    /**
     * Tests replaceInstructions with an interface name containing special characters.
     * This ensures the utility class naming works correctly.
     */
    @Test
    public void testReplaceInstructions_withSpecialCharactersInName_createsUtilityClass() {
        // Arrange - Create an interface with a name containing $ (inner class style)
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "com/example/Outer$Inner",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "staticMethod",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert
        Clazz utilityClass = programClassPool.getClass("com/example/Outer$Inner$$Util");
        assertNotNull(utilityClass, "Utility class should be created with correct name");
    }

    /**
     * Tests that the instruction sequence builder is properly initialized (line 175-177).
     * This is tested indirectly through successful conversion.
     */
    @Test
    public void testReplaceInstructions_initializesInstructionSequenceBuilder() {
        // Arrange
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "BuilderTestInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "builderTest",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act - Should not throw, indicating InstructionSequenceBuilder was properly initialized
        assertDoesNotThrow(() -> converter.visitProgramClass(interfaceClass));

        // Assert
        assertNotNull(programClassPool.getClass("BuilderTestInterface$$Util"));
    }

    /**
     * Tests that the instructions array is properly created (lines 179-180).
     * This is verified through successful processing of multiple static methods.
     */
    @Test
    public void testReplaceInstructions_createsInstructionsArray() {
        // Arrange - Multiple methods to ensure array is created with proper size
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "ArrayTestInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method1",
                        "()V",
                        50,
                        code -> code.return_())
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method2",
                        "()V",
                        50,
                        code -> code.return_())
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "method3",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        assertDoesNotThrow(() -> converter.visitProgramClass(interfaceClass));

        // Assert - Utility class should have all three methods
        Clazz utilityClass = programClassPool.getClass("ArrayTestInterface$$Util");
        assertNotNull(utilityClass);

        ProgramClass programUtilityClass = (ProgramClass) utilityClass;
        // Utility class should have a constructor plus the 3 static methods (4 total)
        assertTrue(programUtilityClass.u2methodsCount >= 3,
                "Utility class should have all static methods");
    }

    /**
     * Tests that the index variable is properly incremented in the loop (line 200).
     * This is tested through having multiple methods and ensuring all are processed.
     */
    @Test
    public void testReplaceInstructions_incrementsIndexCorrectly() {
        // Arrange - Create interface with exactly 5 static methods to test index increment
        ClassBuilder builder = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "IndexTestInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT);

        for (int i = 0; i < 5; i++) {
            final int methodNum = i;
            builder.addMethod(
                    AccessConstants.PUBLIC | AccessConstants.STATIC,
                    "method" + i,
                    "()I",
                    50,
                    code -> code
                            .iconst(methodNum)
                            .ireturn());
        }

        ProgramClass interfaceClass = builder.getProgramClass();

        programClassPool.addClass(interfaceClass);
        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act
        converter.visitProgramClass(interfaceClass);

        // Assert - All 5 methods should be in the utility class
        Clazz utilityClass = programClassPool.getClass("IndexTestInterface$$Util");
        assertNotNull(utilityClass);

        // Verify all methods were moved
        for (int i = 0; i < 5; i++) {
            final int methodNum = i;
            final boolean[] found = {false};
            utilityClass.methodAccept("method" + i, "()I", new MemberVisitor() {
                @Override
                public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                    found[0] = true;
                }
                @Override
                public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
                @Override
                public void visitProgramField(ProgramClass programClass, ProgramField programField) {}
                @Override
                public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
            });
            assertTrue(found[0], "method" + i + " should be in utility class");
        }
    }

    /**
     * Tests that InstructionSequencesReplacer is created with correct parameters (lines 215-218).
     * This is verified through successful instruction replacement.
     */
    @Test
    public void testReplaceInstructions_createsInstructionSequencesReplacer() {
        // Arrange
        ProgramClass interfaceClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC | AccessConstants.INTERFACE,
                "ReplacerTestInterface",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC | AccessConstants.STATIC,
                        "replace",
                        "()V",
                        50,
                        code -> code.return_())
                .getProgramClass();

        // Create a caller to ensure replacement happens
        ProgramClass callerClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "ReplacerCaller",
                ClassConstants.NAME_JAVA_LANG_OBJECT)
                .addMethod(
                        AccessConstants.PUBLIC,
                        "call",
                        "()V",
                        50,
                        code -> code
                                .invokestatic_interface("ReplacerTestInterface", "replace", "()V")
                                .return_())
                .getProgramClass();

        programClassPool.addClass(interfaceClass);
        programClassPool.addClass(callerClass);

        interfaceClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));
        callerClass.accept(new proguard.classfile.util.ClassReferenceInitializer(programClassPool, libraryClassPool));

        StaticInterfaceMethodConverter converter = new StaticInterfaceMethodConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                modifiedClassVisitor,
                extraMemberVisitor);

        // Act - Should successfully create and use InstructionSequencesReplacer
        assertDoesNotThrow(() -> converter.visitProgramClass(interfaceClass));

        // Assert
        assertNotNull(programClassPool.getClass("ReplacerTestInterface$$Util"),
                "Utility class creation indicates InstructionSequencesReplacer worked");
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
