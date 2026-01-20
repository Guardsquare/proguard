package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NameMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
 *
 * The visitLibraryMethod method in NameMarker is responsible for marking method names
 * to be kept during obfuscation, unless they are initializers (<init> or <clinit>).
 *
 * The method performs the following actions:
 * 1. Calls keepMethodName(libraryClass, libraryMethod) which:
 *    a. Gets the method name via method.getName(clazz)
 *    b. Checks if the name is an initializer using ClassUtil.isInitializer(name)
 *    c. If NOT an initializer, calls MemberObfuscator.setFixedNewMemberName(method, name)
 */
public class NameMarkerClaude_visitLibraryMethodTest {

    private NameMarker nameMarker;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
        libraryClass = new LibraryClass();
        libraryClass.thisClassName = "com/example/TestClass";
    }

    /**
     * Tests that visitLibraryMethod marks a regular method name to be kept.
     * The fixed new member name should match the original name.
     */
    @Test
    public void testVisitLibraryMethod_regularMethod_marksName() {
        // Arrange
        String methodName = "toString";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()Ljava/lang/String;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod preserves various regular method names.
     */
    @Test
    public void testVisitLibraryMethod_variousMethodNames() {
        // Arrange
        String[] methodNames = {
            "toString",
            "equals",
            "hashCode",
            "getValue",
            "setValue",
            "processData",
            "a",
            "myMethod123"
        };

        for (String methodName : methodNames) {
            LibraryMethod testMethod = new LibraryMethod();
            testMethod.name = methodName;
            testMethod.descriptor = "()V";

            // Act
            nameMarker.visitLibraryMethod(libraryClass, testMethod);

            // Assert
            assertEquals(methodName, MemberObfuscator.newMemberName(testMethod),
                "Method name '" + methodName + "' should be preserved");
        }
    }

    /**
     * Tests that visitLibraryMethod does NOT mark <init> (constructor) names.
     * Initializers should not have their names fixed as they are special methods.
     */
    @Test
    public void testVisitLibraryMethod_constructorInit_doesNotMarkName() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "<init>";
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - should not set a new member name for initializers
        assertNull(MemberObfuscator.newMemberName(libraryMethod),
            "Constructor <init> should not have a fixed new name");
    }

    /**
     * Tests that visitLibraryMethod does NOT mark <clinit> (static initializer) names.
     * Static initializers should not have their names fixed as they are special methods.
     */
    @Test
    public void testVisitLibraryMethod_staticInitializer_doesNotMarkName() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "<clinit>";
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - should not set a new member name for static initializers
        assertNull(MemberObfuscator.newMemberName(libraryMethod),
            "Static initializer <clinit> should not have a fixed new name");
    }

    /**
     * Tests that visitLibraryMethod works with getter method names.
     */
    @Test
    public void testVisitLibraryMethod_getterMethod() {
        // Arrange
        String methodName = "getName";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()Ljava/lang/String;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with setter method names.
     */
    @Test
    public void testVisitLibraryMethod_setterMethod() {
        // Arrange
        String methodName = "setName";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "(Ljava/lang/String;)V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with boolean getter method names.
     */
    @Test
    public void testVisitLibraryMethod_booleanGetterMethod() {
        // Arrange
        String methodName = "isEmpty";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()Z";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times on the same NameMarker instance.
     */
    @Test
    public void testVisitLibraryMethod_multipleCallsOnSameInstance() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        method1.name = "method1";
        method1.descriptor = "()V";

        LibraryMethod method2 = new LibraryMethod();
        method2.name = "method2";
        method2.descriptor = "()I";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, method1);
        nameMarker.visitLibraryMethod(libraryClass, method2);

        // Assert
        assertEquals("method1", MemberObfuscator.newMemberName(method1));
        assertEquals("method2", MemberObfuscator.newMemberName(method2));
    }

    /**
     * Tests that visitLibraryMethod can be called with the same LibraryMethod multiple times.
     * The fixed new name should remain unchanged.
     */
    @Test
    public void testVisitLibraryMethod_sameMethodCalledMultipleTimes() {
        // Arrange
        String methodName = "toString";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()Ljava/lang/String;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod doesn't throw exceptions on valid input.
     */
    @Test
    public void testVisitLibraryMethod_noExceptionThrown() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "validMethod";
        libraryMethod.descriptor = "()V";

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> nameMarker.visitLibraryMethod(libraryClass, libraryMethod));
    }

    /**
     * Tests that multiple NameMarker instances can process the same LibraryMethod independently.
     * The result should be the same.
     */
    @Test
    public void testVisitLibraryMethod_multipleNameMarkerInstances() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "toString";
        libraryMethod.descriptor = "()Ljava/lang/String;";

        // Act
        marker1.visitLibraryMethod(libraryClass, libraryMethod);
        marker2.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - both should set the same fixed new name
        assertEquals("toString", MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod preserves the exact method name by setting it as the fixed new name.
     */
    @Test
    public void testVisitLibraryMethod_preservesMethodName() {
        // Arrange
        String originalName = "myMethod";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = originalName;
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - the fixed new name should match the original
        assertEquals(originalName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with single character method names.
     */
    @Test
    public void testVisitLibraryMethod_singleCharacterMethodName() {
        // Arrange
        String singleCharName = "a";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = singleCharName;
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(singleCharName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with very long method names.
     */
    @Test
    public void testVisitLibraryMethod_veryLongMethodName() {
        // Arrange
        String longName = "thisIsAVeryLongMethodNameThatMightExistInSomeJavaCode";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = longName;
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(longName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with method names containing underscores.
     */
    @Test
    public void testVisitLibraryMethod_methodNameWithUnderscores() {
        // Arrange
        String methodName = "get_user_name";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()Ljava/lang/String;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with method names containing dollar signs.
     */
    @Test
    public void testVisitLibraryMethod_methodNameWithDollarSigns() {
        // Arrange
        String methodName = "access$000";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with method names containing numbers.
     */
    @Test
    public void testVisitLibraryMethod_methodNameWithNumbers() {
        // Arrange
        String methodName = "method123Test456";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with different LibraryClass instances.
     */
    @Test
    public void testVisitLibraryMethod_differentLibraryClasses() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        class1.thisClassName = "com/example/Class1";
        LibraryClass class2 = new LibraryClass();
        class2.thisClassName = "com/example/Class2";

        LibraryMethod method1 = new LibraryMethod();
        method1.name = "method1";
        method1.descriptor = "()V";

        LibraryMethod method2 = new LibraryMethod();
        method2.name = "method2";
        method2.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(class1, method1);
        nameMarker.visitLibraryMethod(class2, method2);

        // Assert
        assertEquals("method1", MemberObfuscator.newMemberName(method1));
        assertEquals("method2", MemberObfuscator.newMemberName(method2));
    }

    /**
     * Tests that visitLibraryMethod handles Java standard library method names.
     */
    @Test
    public void testVisitLibraryMethod_standardLibraryMethodNames() {
        // Arrange
        String[] standardMethodNames = {
            "toString",
            "equals",
            "hashCode",
            "clone",
            "finalize",
            "wait",
            "notify",
            "notifyAll"
        };

        for (String methodName : standardMethodNames) {
            LibraryMethod testMethod = new LibraryMethod();
            testMethod.name = methodName;
            testMethod.descriptor = "()V";

            // Act
            nameMarker.visitLibraryMethod(libraryClass, testMethod);

            // Assert
            assertEquals(methodName, MemberObfuscator.newMemberName(testMethod),
                "Method name '" + methodName + "' should be preserved");
        }
    }

    /**
     * Tests the difference between regular methods and initializers.
     * Regular methods should have their names fixed, initializers should not.
     */
    @Test
    public void testVisitLibraryMethod_differenceFromInitializers() {
        // Arrange
        LibraryMethod regularMethod = new LibraryMethod();
        regularMethod.name = "regularMethod";
        regularMethod.descriptor = "()V";

        LibraryMethod constructorMethod = new LibraryMethod();
        constructorMethod.name = "<init>";
        constructorMethod.descriptor = "()V";

        LibraryMethod staticInitMethod = new LibraryMethod();
        staticInitMethod.name = "<clinit>";
        staticInitMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, regularMethod);
        nameMarker.visitLibraryMethod(libraryClass, constructorMethod);
        nameMarker.visitLibraryMethod(libraryClass, staticInitMethod);

        // Assert
        assertEquals("regularMethod", MemberObfuscator.newMemberName(regularMethod));
        assertNull(MemberObfuscator.newMemberName(constructorMethod));
        assertNull(MemberObfuscator.newMemberName(staticInitMethod));
    }

    /**
     * Tests that visitLibraryMethod preserves camelCase method names.
     */
    @Test
    public void testVisitLibraryMethod_camelCaseMethodName() {
        // Arrange
        String camelCaseName = "getUserNameFromDatabase";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = camelCaseName;
        libraryMethod.descriptor = "()Ljava/lang/String;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(camelCaseName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod preserves method names with mixed case.
     */
    @Test
    public void testVisitLibraryMethod_mixedCaseMethodName() {
        // Arrange
        String mixedCaseName = "toXMLString";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = mixedCaseName;
        libraryMethod.descriptor = "()Ljava/lang/String;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(mixedCaseName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod handles method names that start with special Java keywords
     * (but aren't keywords because of case or context).
     */
    @Test
    public void testVisitLibraryMethod_methodNameLikeKeyword() {
        // Arrange
        String methodName = "returnValue";  // starts with 'return' but is valid
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()Ljava/lang/Object;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with methods that have complex descriptors.
     */
    @Test
    public void testVisitLibraryMethod_complexDescriptor() {
        // Arrange
        String methodName = "processData";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "(Ljava/lang/String;[ILjava/util/Map;)Ljava/util/List;";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod correctly handles the fixed vs non-fixed name concept.
     * A fixed name means it will not be obfuscated.
     */
    @Test
    public void testVisitLibraryMethod_fixedNameNotObfuscated() {
        // Arrange
        String methodName = "importantMethod";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - the new member name should be fixed to the original name
        String newName = MemberObfuscator.newMemberName(libraryMethod);
        assertEquals(methodName, newName,
            "The method name should be fixed to prevent obfuscation");
    }

    /**
     * Tests that visitLibraryMethod works with empty descriptor (edge case).
     */
    @Test
    public void testVisitLibraryMethod_emptyDescriptor() {
        // Arrange
        String methodName = "testMethod";
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = methodName;
        libraryMethod.descriptor = "";

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> nameMarker.visitLibraryMethod(libraryClass, libraryMethod));
        assertEquals(methodName, MemberObfuscator.newMemberName(libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod works with methods from different packages.
     */
    @Test
    public void testVisitLibraryMethod_differentPackages() {
        // Arrange
        LibraryClass javaLangClass = new LibraryClass();
        javaLangClass.thisClassName = "java/lang/String";

        LibraryClass customClass = new LibraryClass();
        customClass.thisClassName = "com/example/custom/MyClass";

        LibraryMethod method1 = new LibraryMethod();
        method1.name = "length";
        method1.descriptor = "()I";

        LibraryMethod method2 = new LibraryMethod();
        method2.name = "customMethod";
        method2.descriptor = "()V";

        // Act
        nameMarker.visitLibraryMethod(javaLangClass, method1);
        nameMarker.visitLibraryMethod(customClass, method2);

        // Assert
        assertEquals("length", MemberObfuscator.newMemberName(method1));
        assertEquals("customMethod", MemberObfuscator.newMemberName(method2));
    }
}
