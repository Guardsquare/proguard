package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.ClassConstant;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TypeArgumentFinder#visitClassConstant(Clazz, ClassConstant)}.
 *
 * The visitClassConstant method sets the typeArgumentClasses field to a single-element array
 * containing the class name from the ClassConstant. This is used to track type arguments found
 * in class constants during bytecode analysis for GSON optimization.
 */
public class TypeArgumentFinderClaude_visitClassConstantTest {

    private TypeArgumentFinder finder;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private PartialEvaluator partialEvaluator;
    private Clazz clazz;
    private ClassConstant classConstant;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        partialEvaluator = PartialEvaluator.Builder.create().build();
        finder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        clazz = mock(ProgramClass.class);
        classConstant = mock(ClassConstant.class);
    }

    /**
     * Tests that visitClassConstant sets typeArgumentClasses to a single-element array
     * containing the class name from the ClassConstant.
     */
    @Test
    public void testVisitClassConstant_setsTypeArgumentClasses() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/TestClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("com/example/TestClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant calls getName on the ClassConstant.
     */
    @Test
    public void testVisitClassConstant_callsGetName() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/TestClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, times(1)).getName(clazz);
    }

    /**
     * Tests that visitClassConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitClassConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitClassConstant(clazz, classConstant));
    }

    /**
     * Tests that visitClassConstant replaces previous typeArgumentClasses value.
     */
    @Test
    public void testVisitClassConstant_replacesPreviousValue() {
        // Arrange
        finder.typeArgumentClasses = new String[] { "OldClass1", "OldClass2" };
        when(classConstant.getName(clazz)).thenReturn("NewClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("NewClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant can handle multiple calls with different ClassConstants.
     * Each call should replace the previous typeArgumentClasses value.
     */
    @Test
    public void testVisitClassConstant_multipleCallsReplaceValue() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        ClassConstant cc3 = mock(ClassConstant.class);
        when(cc1.getName(clazz)).thenReturn("Class1");
        when(cc2.getName(clazz)).thenReturn("Class2");
        when(cc3.getName(clazz)).thenReturn("Class3");

        // Act & Assert
        finder.visitClassConstant(clazz, cc1);
        assertArrayEquals(new String[] { "Class1" }, finder.typeArgumentClasses);

        finder.visitClassConstant(clazz, cc2);
        assertArrayEquals(new String[] { "Class2" }, finder.typeArgumentClasses);

        finder.visitClassConstant(clazz, cc3);
        assertArrayEquals(new String[] { "Class3" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant with null ClassConstant throws NullPointerException.
     */
    @Test
    public void testVisitClassConstant_withNullClassConstant_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> finder.visitClassConstant(clazz, null));
    }

    /**
     * Tests that visitClassConstant can handle a ClassConstant that returns null from getName.
     */
    @Test
    public void testVisitClassConstant_withNullClassName_setsArrayWithNull() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn(null);

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertNull(finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant can handle an empty string from getName.
     */
    @Test
    public void testVisitClassConstant_withEmptyClassName_setsArrayWithEmptyString() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant passes the correct clazz parameter to getName.
     */
    @Test
    public void testVisitClassConstant_passesCorrectClazzToGetName() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant).getName(eq(clazz));
    }

    /**
     * Tests that visitClassConstant can be called with null Clazz parameter.
     */
    @Test
    public void testVisitClassConstant_withNullClazz_passesNullToGetName() {
        // Arrange
        when(classConstant.getName(null)).thenReturn("TestClass");

        // Act
        finder.visitClassConstant(null, classConstant);

        // Assert
        verify(classConstant).getName(null);
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant can be used as part of the ConstantVisitor interface.
     */
    @Test
    public void testVisitClassConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = finder;
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitClassConstant(clazz, classConstant));
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant can be called with real ProgramClass instance.
     */
    @Test
    public void testVisitClassConstant_withRealProgramClass_worksCorrectly() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        when(classConstant.getName(realClass)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitClassConstant(realClass, classConstant));
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant can be called with real LibraryClass instance.
     */
    @Test
    public void testVisitClassConstant_withRealLibraryClass_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        when(classConstant.getName(libraryClass)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitClassConstant(libraryClass, classConstant));
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant creates a new array instance each time.
     */
    @Test
    public void testVisitClassConstant_createsNewArrayInstance() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);
        String[] firstArray = finder.typeArgumentClasses;

        finder.visitClassConstant(clazz, classConstant);
        String[] secondArray = finder.typeArgumentClasses;

        // Assert - should be different instances
        assertNotSame(firstArray, secondArray);
        assertArrayEquals(firstArray, secondArray);
    }

    /**
     * Tests that visitClassConstant can be called rapidly in succession.
     */
    @Test
    public void testVisitClassConstant_rapidSequentialCalls_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> finder.visitClassConstant(clazz, classConstant),
                    "Call " + i + " should not throw exception");
        }

        // Assert - final state should still be correct
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
        verify(classConstant, times(100)).getName(clazz);
    }

    /**
     * Tests that multiple TypeArgumentFinder instances can maintain independent state.
     */
    @Test
    public void testVisitClassConstant_multipleFinderInstances_independentState() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        when(cc1.getName(clazz)).thenReturn("Class1");
        when(cc2.getName(clazz)).thenReturn("Class2");

        // Act
        finder1.visitClassConstant(clazz, cc1);
        finder2.visitClassConstant(clazz, cc2);

        // Assert - each finder should have independent state
        assertArrayEquals(new String[] { "Class1" }, finder1.typeArgumentClasses);
        assertArrayEquals(new String[] { "Class2" }, finder2.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles class names with special characters.
     */
    @Test
    public void testVisitClassConstant_withSpecialCharactersInClassName_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/$InnerClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "com/example/$InnerClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles very long class names.
     */
    @Test
    public void testVisitClassConstant_withLongClassName_worksCorrectly() {
        // Arrange
        String longClassName = "com/example/very/long/package/name/TestClass";
        when(classConstant.getName(clazz)).thenReturn(longClassName);

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { longClassName }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant preserves the exact class name returned by getName.
     */
    @Test
    public void testVisitClassConstant_preservesExactClassName() {
        // Arrange
        String className = "java/lang/String";
        when(classConstant.getName(clazz)).thenReturn(className);

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertSame(className, finder.typeArgumentClasses[0],
                "Should preserve the exact string instance");
    }

    /**
     * Tests that visitClassConstant works correctly in a sequence of different visitor method calls.
     */
    @Test
    public void testVisitClassConstant_inVisitorSequence_worksCorrectly() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        when(classConstant.getName(realClass)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(realClass, null, null, 0, null);
            finder.visitClassConstant(realClass, classConstant);
            finder.visitAnyInstruction(realClass, null, null, 0, null);
        });

        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant doesn't modify the state of the clazz parameter.
     */
    @Test
    public void testVisitClassConstant_doesNotModifyClazzState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);
        when(classConstant.getName(realClass)).thenReturn("TestClass");

        // Act
        finder.visitClassConstant(realClass, classConstant);

        // Assert
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitClassConstant can handle different clazz types.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazzTypes_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        when(classConstant.getName(programClass)).thenReturn("ProgramClass");
        when(classConstant.getName(libraryClass)).thenReturn("LibraryClass");

        // Act & Assert
        finder.visitClassConstant(programClass, classConstant);
        assertArrayEquals(new String[] { "ProgramClass" }, finder.typeArgumentClasses);

        finder.visitClassConstant(libraryClass, classConstant);
        assertArrayEquals(new String[] { "LibraryClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles mixed real and mock objects.
     */
    @Test
    public void testVisitClassConstant_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Clazz mockClazz = mock(Clazz.class);
        when(classConstant.getName(realClass)).thenReturn("RealClass");
        when(classConstant.getName(mockClazz)).thenReturn("MockClass");

        // Act & Assert
        finder.visitClassConstant(realClass, classConstant);
        assertArrayEquals(new String[] { "RealClass" }, finder.typeArgumentClasses);

        finder.visitClassConstant(mockClazz, classConstant);
        assertArrayEquals(new String[] { "MockClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant can be interleaved with other constant visitor methods.
     */
    @Test
    public void testVisitClassConstant_interleavedWithOtherVisitorMethods_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("ClassConstantClass");

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(clazz, classConstant);
            finder.visitClassConstant(clazz, classConstant);
            finder.visitAnyConstant(clazz, classConstant);
        });

        assertArrayEquals(new String[] { "ClassConstantClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant creates an array with exactly one element.
     */
    @Test
    public void testVisitClassConstant_alwaysCreatesArrayWithOneElement() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        ClassConstant cc3 = mock(ClassConstant.class);
        when(cc1.getName(clazz)).thenReturn("Class1");
        when(cc2.getName(clazz)).thenReturn("Class2");
        when(cc3.getName(clazz)).thenReturn("Class3");

        // Act & Assert - each call should create an array with exactly one element
        finder.visitClassConstant(clazz, cc1);
        assertEquals(1, finder.typeArgumentClasses.length);

        finder.visitClassConstant(clazz, cc2);
        assertEquals(1, finder.typeArgumentClasses.length);

        finder.visitClassConstant(clazz, cc3);
        assertEquals(1, finder.typeArgumentClasses.length);
    }

    /**
     * Tests that visitClassConstant resets typeArgumentClasses even when it was previously null.
     */
    @Test
    public void testVisitClassConstant_fromNullState_setsNewArray() {
        // Arrange
        assertNull(finder.typeArgumentClasses, "Initial state should be null");
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("TestClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant resets typeArgumentClasses even when it was previously a large array.
     */
    @Test
    public void testVisitClassConstant_fromLargeArray_setsNewSingleElementArray() {
        // Arrange
        finder.typeArgumentClasses = new String[] { "Old1", "Old2", "Old3", "Old4", "Old5" };
        when(classConstant.getName(clazz)).thenReturn("NewClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("NewClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant handles class names with package separators.
     */
    @Test
    public void testVisitClassConstant_withPackageSeparators_preservesFormat() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/package/ClassName");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertEquals("com/example/package/ClassName", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant handles primitive type descriptors correctly.
     */
    @Test
    public void testVisitClassConstant_withPrimitiveDescriptor_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("I");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "I" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant can be called through the interface with casting.
     */
    @Test
    public void testVisitClassConstant_throughCastedInterface_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor =
            (proguard.classfile.constant.visitor.ConstantVisitor) finder;
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitClassConstant(clazz, classConstant);
        });

        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant doesn't retain references to the old array.
     */
    @Test
    public void testVisitClassConstant_doesNotRetainOldArray() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("Class1");
        finder.visitClassConstant(clazz, classConstant);
        String[] oldArray = finder.typeArgumentClasses;

        when(classConstant.getName(clazz)).thenReturn("Class2");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertNotSame(oldArray, finder.typeArgumentClasses,
                "Should create a new array, not reuse the old one");
    }

    /**
     * Tests that visitClassConstant can be called alternating between different finders.
     */
    @Test
    public void testVisitClassConstant_alternatingFinders_maintainIndependentState() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        when(cc1.getName(clazz)).thenReturn("Class1");
        when(cc2.getName(clazz)).thenReturn("Class2");

        // Act
        finder1.visitClassConstant(clazz, cc1);
        finder2.visitClassConstant(clazz, cc2);
        finder1.visitClassConstant(clazz, cc2);
        finder2.visitClassConstant(clazz, cc1);

        // Assert
        assertArrayEquals(new String[] { "Class2" }, finder1.typeArgumentClasses);
        assertArrayEquals(new String[] { "Class1" }, finder2.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant works correctly immediately after construction.
     */
    @Test
    public void testVisitClassConstant_immediatelyAfterConstruction_worksCorrectly() {
        // Arrange
        TypeArgumentFinder newFinder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> newFinder.visitClassConstant(clazz, classConstant));
        assertArrayEquals(new String[] { "TestClass" }, newFinder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles extreme numbers of calls without issues.
     */
    @Test
    public void testVisitClassConstant_extremeNumberOfCalls_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> finder.visitClassConstant(clazz, classConstant));
        }

        // Assert
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
        verify(classConstant, times(1000)).getName(clazz);
    }

    /**
     * Tests that visitClassConstant array is always length 1 regardless of how many times called.
     */
    @Test
    public void testVisitClassConstant_arrayAlwaysLengthOne() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act & Assert
        for (int i = 0; i < 50; i++) {
            finder.visitClassConstant(clazz, classConstant);
            assertEquals(1, finder.typeArgumentClasses.length,
                    "Array length should always be 1 after call " + i);
        }
    }

    /**
     * Tests that visitClassConstant properly handles unicode characters in class names.
     */
    @Test
    public void testVisitClassConstant_withUnicodeInClassName_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/Класс");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "com/example/Класс" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant behavior with different ClassConstant instances is consistent.
     */
    @Test
    public void testVisitClassConstant_consistentBehaviorAcrossInstances() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        String className = "com/example/TestClass";
        when(cc1.getName(clazz)).thenReturn(className);
        when(cc2.getName(clazz)).thenReturn(className);

        // Act
        finder.visitClassConstant(clazz, cc1);
        String[] result1 = finder.typeArgumentClasses;

        finder.visitClassConstant(clazz, cc2);
        String[] result2 = finder.typeArgumentClasses;

        // Assert - results should be equivalent (same content)
        assertArrayEquals(result1, result2);
    }

    /**
     * Tests that visitClassConstant handles standard Java library classes.
     */
    @Test
    public void testVisitClassConstant_withJavaLibraryClass_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/Object");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "java/lang/Object" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles array type descriptors.
     */
    @Test
    public void testVisitClassConstant_withArrayDescriptor_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("[Ljava/lang/String;");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "[Ljava/lang/String;" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles nested inner class names.
     */
    @Test
    public void testVisitClassConstant_withNestedInnerClass_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/Outer$Inner$DeepInner");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "com/example/Outer$Inner$DeepInner" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles anonymous class names.
     */
    @Test
    public void testVisitClassConstant_withAnonymousClass_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/Outer$1");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "com/example/Outer$1" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant can be called in a complex visitor workflow.
     */
    @Test
    public void testVisitClassConstant_inComplexWorkflow_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        when(cc1.getName(programClass)).thenReturn("Class1");
        when(cc2.getName(programClass)).thenReturn("Class2");

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(programClass, null, null, 0, null);
            finder.visitClassConstant(programClass, cc1);
            finder.visitAnyConstant(programClass, cc1);
            finder.visitClassConstant(programClass, cc2);
        });

        // Final state should be Class2
        assertArrayEquals(new String[] { "Class2" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant maintains correct behavior after null clazz calls.
     */
    @Test
    public void testVisitClassConstant_afterNullClazz_behaviorRemainsConsistent() {
        // Arrange
        when(classConstant.getName(null)).thenReturn("NullClazzClass");
        when(classConstant.getName(clazz)).thenReturn("NonNullClazzClass");

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitClassConstant(null, classConstant);
            finder.visitClassConstant(clazz, classConstant);
            finder.visitClassConstant(null, classConstant);
        });

        assertArrayEquals(new String[] { "NullClazzClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant with same ClassConstant but different Clazz works correctly.
     */
    @Test
    public void testVisitClassConstant_sameConstantDifferentClazz_callsGetNameWithCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        when(classConstant.getName(clazz1)).thenReturn("Class1");
        when(classConstant.getName(clazz2)).thenReturn("Class2");

        // Act & Assert
        finder.visitClassConstant(clazz1, classConstant);
        assertArrayEquals(new String[] { "Class1" }, finder.typeArgumentClasses);

        finder.visitClassConstant(clazz2, classConstant);
        assertArrayEquals(new String[] { "Class2" }, finder.typeArgumentClasses);

        // Verify getName was called with each clazz
        verify(classConstant).getName(clazz1);
        verify(classConstant).getName(clazz2);
    }

    /**
     * Tests that visitClassConstant handles multi-dimensional array descriptors.
     */
    @Test
    public void testVisitClassConstant_withMultiDimensionalArray_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("[[Ljava/lang/String;");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "[[Ljava/lang/String;" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant handles primitive array descriptors.
     */
    @Test
    public void testVisitClassConstant_withPrimitiveArray_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("[I");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "[I" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant doesn't cause memory leaks by retaining old arrays.
     */
    @Test
    public void testVisitClassConstant_doesNotCauseMemoryLeaks() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act - create many arrays
        for (int i = 0; i < 100; i++) {
            finder.visitClassConstant(clazz, classConstant);
        }

        // Assert - only the last array should be referenced
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("TestClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitClassConstant is idempotent when called with same parameters.
     */
    @Test
    public void testVisitClassConstant_isIdempotent() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("TestClass");

        // Act
        finder.visitClassConstant(clazz, classConstant);
        String[] firstResult = finder.typeArgumentClasses;

        finder.visitClassConstant(clazz, classConstant);
        String[] secondResult = finder.typeArgumentClasses;

        finder.visitClassConstant(clazz, classConstant);
        String[] thirdResult = finder.typeArgumentClasses;

        // Assert - all results have the same content (but different instances)
        assertArrayEquals(firstResult, secondResult);
        assertArrayEquals(secondResult, thirdResult);
        assertNotSame(firstResult, secondResult);
        assertNotSame(secondResult, thirdResult);
    }

    /**
     * Tests that visitClassConstant works correctly with ClassConstants representing interfaces.
     */
    @Test
    public void testVisitClassConstant_withInterfaceClass_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/io/Serializable");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "java/io/Serializable" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitClassConstant correctly handles generic type representations.
     */
    @Test
    public void testVisitClassConstant_withGenericTypeRepresentation_worksCorrectly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/util/List");

        // Act
        finder.visitClassConstant(clazz, classConstant);

        // Assert
        assertArrayEquals(new String[] { "java/util/List" }, finder.typeArgumentClasses);
    }
}
