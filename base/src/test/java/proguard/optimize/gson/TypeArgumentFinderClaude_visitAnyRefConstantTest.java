package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TypeArgumentFinder#visitAnyRefConstant(Clazz, RefConstant)}.
 *
 * The visitAnyRefConstant method sets the typeArgumentClasses field to a single-element array
 * containing the class name from the RefConstant. This is used to track type arguments found
 * in reference constants during bytecode analysis.
 */
public class TypeArgumentFinderClaude_visitAnyRefConstantTest {

    private TypeArgumentFinder finder;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private PartialEvaluator partialEvaluator;
    private Clazz clazz;
    private RefConstant refConstant;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        partialEvaluator = PartialEvaluator.Builder.create().build();
        finder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        clazz = mock(ProgramClass.class);
        refConstant = mock(RefConstant.class);
    }

    /**
     * Tests that visitAnyRefConstant sets typeArgumentClasses to a single-element array
     * containing the class name from the RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_setsTypeArgumentClasses() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("com/example/TestClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("com/example/TestClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitAnyRefConstant calls getClassName on the RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_callsGetClassName() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("com/example/TestClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        verify(refConstant, times(1)).getClassName(clazz);
    }

    /**
     * Tests that visitAnyRefConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitAnyRefConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("com/example/TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyRefConstant(clazz, refConstant));
    }

    /**
     * Tests that visitAnyRefConstant replaces previous typeArgumentClasses value.
     */
    @Test
    public void testVisitAnyRefConstant_replacesPreviousValue() {
        // Arrange
        finder.typeArgumentClasses = new String[] { "OldClass1", "OldClass2" };
        when(refConstant.getClassName(clazz)).thenReturn("NewClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("NewClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitAnyRefConstant can handle multiple calls with different RefConstants.
     * Each call should replace the previous typeArgumentClasses value.
     */
    @Test
    public void testVisitAnyRefConstant_multipleCallsReplaceValue() {
        // Arrange
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        RefConstant rc3 = mock(RefConstant.class);
        when(rc1.getClassName(clazz)).thenReturn("Class1");
        when(rc2.getClassName(clazz)).thenReturn("Class2");
        when(rc3.getClassName(clazz)).thenReturn("Class3");

        // Act & Assert
        finder.visitAnyRefConstant(clazz, rc1);
        assertArrayEquals(new String[] { "Class1" }, finder.typeArgumentClasses);

        finder.visitAnyRefConstant(clazz, rc2);
        assertArrayEquals(new String[] { "Class2" }, finder.typeArgumentClasses);

        finder.visitAnyRefConstant(clazz, rc3);
        assertArrayEquals(new String[] { "Class3" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant with null RefConstant throws NullPointerException.
     */
    @Test
    public void testVisitAnyRefConstant_withNullRefConstant_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> finder.visitAnyRefConstant(clazz, null));
    }

    /**
     * Tests that visitAnyRefConstant can handle a RefConstant that returns null from getClassName.
     */
    @Test
    public void testVisitAnyRefConstant_withNullClassName_setsArrayWithNull() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn(null);

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertNull(finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitAnyRefConstant can handle an empty string from getClassName.
     */
    @Test
    public void testVisitAnyRefConstant_withEmptyClassName_setsArrayWithEmptyString() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitAnyRefConstant passes the correct clazz parameter to getClassName.
     */
    @Test
    public void testVisitAnyRefConstant_passesCorrectClazzToGetClassName() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        verify(refConstant).getClassName(eq(clazz));
    }

    /**
     * Tests that visitAnyRefConstant can be called with null Clazz parameter.
     */
    @Test
    public void testVisitAnyRefConstant_withNullClazz_passesNullToGetClassName() {
        // Arrange
        when(refConstant.getClassName(null)).thenReturn("TestClass");

        // Act
        finder.visitAnyRefConstant(null, refConstant);

        // Assert
        verify(refConstant).getClassName(null);
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant can be used as part of the ConstantVisitor interface.
     */
    @Test
    public void testVisitAnyRefConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = finder;
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyRefConstant(clazz, refConstant));
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant can be called with real ProgramClass instance.
     */
    @Test
    public void testVisitAnyRefConstant_withRealProgramClass_worksCorrectly() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        when(refConstant.getClassName(realClass)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyRefConstant(realClass, refConstant));
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant can be called with real LibraryClass instance.
     */
    @Test
    public void testVisitAnyRefConstant_withRealLibraryClass_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        when(refConstant.getClassName(libraryClass)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyRefConstant(libraryClass, refConstant));
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant creates a new array instance each time.
     */
    @Test
    public void testVisitAnyRefConstant_createsNewArrayInstance() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);
        String[] firstArray = finder.typeArgumentClasses;

        finder.visitAnyRefConstant(clazz, refConstant);
        String[] secondArray = finder.typeArgumentClasses;

        // Assert - should be different instances
        assertNotSame(firstArray, secondArray);
        assertArrayEquals(firstArray, secondArray);
    }

    /**
     * Tests that visitAnyRefConstant works correctly with FieldrefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withFieldrefConstant_worksCorrectly() {
        // Arrange
        FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
        when(fieldrefConstant.getClassName(clazz)).thenReturn("FieldClass");

        // Act
        finder.visitAnyRefConstant(clazz, fieldrefConstant);

        // Assert
        assertArrayEquals(new String[] { "FieldClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant works correctly with MethodrefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withMethodrefConstant_worksCorrectly() {
        // Arrange
        MethodrefConstant methodrefConstant = mock(MethodrefConstant.class);
        when(methodrefConstant.getClassName(clazz)).thenReturn("MethodClass");

        // Act
        finder.visitAnyRefConstant(clazz, methodrefConstant);

        // Assert
        assertArrayEquals(new String[] { "MethodClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant works correctly with InterfaceMethodrefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withInterfaceMethodrefConstant_worksCorrectly() {
        // Arrange
        InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);
        when(interfaceMethodrefConstant.getClassName(clazz)).thenReturn("InterfaceMethodClass");

        // Act
        finder.visitAnyRefConstant(clazz, interfaceMethodrefConstant);

        // Assert
        assertArrayEquals(new String[] { "InterfaceMethodClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant works with all three RefConstant subtypes in sequence.
     */
    @Test
    public void testVisitAnyRefConstant_withAllRefConstantSubtypes_allWorkCorrectly() {
        // Arrange
        FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
        MethodrefConstant methodrefConstant = mock(MethodrefConstant.class);
        InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        when(fieldrefConstant.getClassName(clazz)).thenReturn("FieldClass");
        when(methodrefConstant.getClassName(clazz)).thenReturn("MethodClass");
        when(interfaceMethodrefConstant.getClassName(clazz)).thenReturn("InterfaceMethodClass");

        // Act & Assert
        finder.visitAnyRefConstant(clazz, fieldrefConstant);
        assertArrayEquals(new String[] { "FieldClass" }, finder.typeArgumentClasses);

        finder.visitAnyRefConstant(clazz, methodrefConstant);
        assertArrayEquals(new String[] { "MethodClass" }, finder.typeArgumentClasses);

        finder.visitAnyRefConstant(clazz, interfaceMethodrefConstant);
        assertArrayEquals(new String[] { "InterfaceMethodClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant can be called rapidly in succession.
     */
    @Test
    public void testVisitAnyRefConstant_rapidSequentialCalls_worksCorrectly() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> finder.visitAnyRefConstant(clazz, refConstant),
                    "Call " + i + " should not throw exception");
        }

        // Assert - final state should still be correct
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
        verify(refConstant, times(100)).getClassName(clazz);
    }

    /**
     * Tests that multiple TypeArgumentFinder instances can maintain independent state.
     */
    @Test
    public void testVisitAnyRefConstant_multipleFinderInstances_independentState() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        when(rc1.getClassName(clazz)).thenReturn("Class1");
        when(rc2.getClassName(clazz)).thenReturn("Class2");

        // Act
        finder1.visitAnyRefConstant(clazz, rc1);
        finder2.visitAnyRefConstant(clazz, rc2);

        // Assert - each finder should have independent state
        assertArrayEquals(new String[] { "Class1" }, finder1.typeArgumentClasses);
        assertArrayEquals(new String[] { "Class2" }, finder2.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant handles class names with special characters.
     */
    @Test
    public void testVisitAnyRefConstant_withSpecialCharactersInClassName_worksCorrectly() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("com/example/$InnerClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertArrayEquals(new String[] { "com/example/$InnerClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant handles very long class names.
     */
    @Test
    public void testVisitAnyRefConstant_withLongClassName_worksCorrectly() {
        // Arrange
        String longClassName = "com/example/very/long/package/name/TestClass";
        when(refConstant.getClassName(clazz)).thenReturn(longClassName);

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertArrayEquals(new String[] { longClassName }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant preserves the exact class name returned by getClassName.
     */
    @Test
    public void testVisitAnyRefConstant_preservesExactClassName() {
        // Arrange
        String className = "java/lang/String";
        when(refConstant.getClassName(clazz)).thenReturn(className);

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertSame(className, finder.typeArgumentClasses[0],
                "Should preserve the exact string instance");
    }

    /**
     * Tests that visitAnyRefConstant works correctly in a sequence of different visitor method calls.
     */
    @Test
    public void testVisitAnyRefConstant_inVisitorSequence_worksCorrectly() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        when(refConstant.getClassName(realClass)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(realClass, null, null, 0, null);
            finder.visitAnyRefConstant(realClass, refConstant);
            finder.visitAnyInstruction(realClass, null, null, 0, null);
        });

        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant doesn't modify the state of the clazz parameter.
     */
    @Test
    public void testVisitAnyRefConstant_doesNotModifyClazzState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);
        when(refConstant.getClassName(realClass)).thenReturn("TestClass");

        // Act
        finder.visitAnyRefConstant(realClass, refConstant);

        // Assert
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitAnyRefConstant can handle different clazz types.
     */
    @Test
    public void testVisitAnyRefConstant_withDifferentClazzTypes_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        when(refConstant.getClassName(programClass)).thenReturn("ProgramClass");
        when(refConstant.getClassName(libraryClass)).thenReturn("LibraryClass");

        // Act & Assert
        finder.visitAnyRefConstant(programClass, refConstant);
        assertArrayEquals(new String[] { "ProgramClass" }, finder.typeArgumentClasses);

        finder.visitAnyRefConstant(libraryClass, refConstant);
        assertArrayEquals(new String[] { "LibraryClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant handles mixed real and mock objects.
     */
    @Test
    public void testVisitAnyRefConstant_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Clazz mockClazz = mock(Clazz.class);
        when(refConstant.getClassName(realClass)).thenReturn("RealClass");
        when(refConstant.getClassName(mockClazz)).thenReturn("MockClass");

        // Act & Assert
        finder.visitAnyRefConstant(realClass, refConstant);
        assertArrayEquals(new String[] { "RealClass" }, finder.typeArgumentClasses);

        finder.visitAnyRefConstant(mockClazz, refConstant);
        assertArrayEquals(new String[] { "MockClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant can be interleaved with other constant visitor methods.
     */
    @Test
    public void testVisitAnyRefConstant_interleavedWithOtherVisitorMethods_worksCorrectly() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("RefClass");

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(clazz, refConstant);
            finder.visitAnyRefConstant(clazz, refConstant);
            finder.visitAnyConstant(clazz, refConstant);
        });

        assertArrayEquals(new String[] { "RefClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant creates an array with exactly one element.
     */
    @Test
    public void testVisitAnyRefConstant_alwaysCreatesArrayWithOneElement() {
        // Arrange
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        RefConstant rc3 = mock(RefConstant.class);
        when(rc1.getClassName(clazz)).thenReturn("Class1");
        when(rc2.getClassName(clazz)).thenReturn("Class2");
        when(rc3.getClassName(clazz)).thenReturn("Class3");

        // Act & Assert - each call should create an array with exactly one element
        finder.visitAnyRefConstant(clazz, rc1);
        assertEquals(1, finder.typeArgumentClasses.length);

        finder.visitAnyRefConstant(clazz, rc2);
        assertEquals(1, finder.typeArgumentClasses.length);

        finder.visitAnyRefConstant(clazz, rc3);
        assertEquals(1, finder.typeArgumentClasses.length);
    }

    /**
     * Tests that visitAnyRefConstant resets typeArgumentClasses even when it was previously null.
     */
    @Test
    public void testVisitAnyRefConstant_fromNullState_setsNewArray() {
        // Arrange
        assertNull(finder.typeArgumentClasses, "Initial state should be null");
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("TestClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitAnyRefConstant resets typeArgumentClasses even when it was previously a large array.
     */
    @Test
    public void testVisitAnyRefConstant_fromLargeArray_setsNewSingleElementArray() {
        // Arrange
        finder.typeArgumentClasses = new String[] { "Old1", "Old2", "Old3", "Old4", "Old5" };
        when(refConstant.getClassName(clazz)).thenReturn("NewClass");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertNotNull(finder.typeArgumentClasses);
        assertEquals(1, finder.typeArgumentClasses.length);
        assertEquals("NewClass", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitAnyRefConstant handles class names with package separators.
     */
    @Test
    public void testVisitAnyRefConstant_withPackageSeparators_preservesFormat() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("com/example/package/ClassName");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertEquals("com/example/package/ClassName", finder.typeArgumentClasses[0]);
    }

    /**
     * Tests that visitAnyRefConstant handles primitive type descriptors correctly.
     */
    @Test
    public void testVisitAnyRefConstant_withPrimitiveDescriptor_worksCorrectly() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("I");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertArrayEquals(new String[] { "I" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant can be called through the interface with casting.
     */
    @Test
    public void testVisitAnyRefConstant_throughCastedInterface_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor =
            (proguard.classfile.constant.visitor.ConstantVisitor) finder;
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyRefConstant(clazz, refConstant);
        });

        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant handles the polymorphic nature of RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_polymorphicRefConstant_handlesAllTypes() {
        // Arrange
        RefConstant[] refConstants = {
            mock(FieldrefConstant.class),
            mock(MethodrefConstant.class),
            mock(InterfaceMethodrefConstant.class),
            mock(RefConstant.class)
        };

        for (int i = 0; i < refConstants.length; i++) {
            when(refConstants[i].getClassName(clazz)).thenReturn("Class" + i);
        }

        // Act & Assert
        for (int i = 0; i < refConstants.length; i++) {
            finder.visitAnyRefConstant(clazz, refConstants[i]);
            assertArrayEquals(new String[] { "Class" + i }, finder.typeArgumentClasses);
        }
    }

    /**
     * Tests that visitAnyRefConstant doesn't retain references to the old array.
     */
    @Test
    public void testVisitAnyRefConstant_doesNotRetainOldArray() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("Class1");
        finder.visitAnyRefConstant(clazz, refConstant);
        String[] oldArray = finder.typeArgumentClasses;

        when(refConstant.getClassName(clazz)).thenReturn("Class2");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertNotSame(oldArray, finder.typeArgumentClasses,
                "Should create a new array, not reuse the old one");
    }

    /**
     * Tests that visitAnyRefConstant can be called alternating between different finders.
     */
    @Test
    public void testVisitAnyRefConstant_alternatingFinders_maintainIndependentState() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        when(rc1.getClassName(clazz)).thenReturn("Class1");
        when(rc2.getClassName(clazz)).thenReturn("Class2");

        // Act
        finder1.visitAnyRefConstant(clazz, rc1);
        finder2.visitAnyRefConstant(clazz, rc2);
        finder1.visitAnyRefConstant(clazz, rc2);
        finder2.visitAnyRefConstant(clazz, rc1);

        // Assert
        assertArrayEquals(new String[] { "Class2" }, finder1.typeArgumentClasses);
        assertArrayEquals(new String[] { "Class1" }, finder2.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant works correctly immediately after construction.
     */
    @Test
    public void testVisitAnyRefConstant_immediatelyAfterConstruction_worksCorrectly() {
        // Arrange
        TypeArgumentFinder newFinder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> newFinder.visitAnyRefConstant(clazz, refConstant));
        assertArrayEquals(new String[] { "TestClass" }, newFinder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant handles extreme numbers of calls without issues.
     */
    @Test
    public void testVisitAnyRefConstant_extremeNumberOfCalls_worksCorrectly() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> finder.visitAnyRefConstant(clazz, refConstant));
        }

        // Assert
        assertArrayEquals(new String[] { "TestClass" }, finder.typeArgumentClasses);
        verify(refConstant, times(1000)).getClassName(clazz);
    }

    /**
     * Tests that visitAnyRefConstant array is always length 1 regardless of how many times called.
     */
    @Test
    public void testVisitAnyRefConstant_arrayAlwaysLengthOne() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("TestClass");

        // Act & Assert
        for (int i = 0; i < 50; i++) {
            finder.visitAnyRefConstant(clazz, refConstant);
            assertEquals(1, finder.typeArgumentClasses.length,
                    "Array length should always be 1 after call " + i);
        }
    }

    /**
     * Tests that visitAnyRefConstant properly handles unicode characters in class names.
     */
    @Test
    public void testVisitAnyRefConstant_withUnicodeInClassName_worksCorrectly() {
        // Arrange
        when(refConstant.getClassName(clazz)).thenReturn("com/example/Класс");

        // Act
        finder.visitAnyRefConstant(clazz, refConstant);

        // Assert
        assertArrayEquals(new String[] { "com/example/Класс" }, finder.typeArgumentClasses);
    }

    /**
     * Tests that visitAnyRefConstant behavior with different RefConstant instances is consistent.
     */
    @Test
    public void testVisitAnyRefConstant_consistentBehaviorAcrossInstances() {
        // Arrange
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        String className = "com/example/TestClass";
        when(rc1.getClassName(clazz)).thenReturn(className);
        when(rc2.getClassName(clazz)).thenReturn(className);

        // Act
        finder.visitAnyRefConstant(clazz, rc1);
        String[] result1 = finder.typeArgumentClasses;

        finder.visitAnyRefConstant(clazz, rc2);
        String[] result2 = finder.typeArgumentClasses;

        // Assert - results should be equivalent (same content)
        assertArrayEquals(result1, result2);
    }
}
