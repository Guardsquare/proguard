package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.util.ClassReferenceInitializer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TypeParameterClassChecker}.
 * Tests the constructor and all visitor methods to ensure proper detection of
 * fields with generic type parameters.
 *
 * The TypeParameterClassChecker visits classes to determine if they have fields
 * with generic type parameters like T, or bounded wildcards.
 */
public class TypeParameterClassCheckerClaudeTest {

    private TypeParameterClassChecker checker;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;

    /**
     * Sets up a fresh TypeParameterClassChecker instance before each test.
     */
    @BeforeEach
    public void setUp() {
        checker = new TypeParameterClassChecker();
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();

        // Add commonly needed library classes
        addLibraryClass("java/lang/Object");
        addLibraryClass("java/lang/String");
    }

    private void addLibraryClass(String className) {
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.thisClassName = className;
        libraryClass.superClassName = className.equals("java/lang/Object") ? null : "java/lang/Object";
        libraryClassPool.addClass(libraryClass);
    }

    // =========================================================================
    // Tests for constructor: <init>.()V
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates an instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        TypeParameterClassChecker newChecker = new TypeParameterClassChecker();

        // Assert
        assertNotNull(newChecker, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor initializes hasFieldWithTypeParameter to false.
     * This is the initial state before any fields have been analyzed.
     */
    @Test
    public void testConstructor_initializesHasFieldWithTypeParameterToFalse() {
        // Act
        TypeParameterClassChecker newChecker = new TypeParameterClassChecker();

        // Assert
        assertFalse(newChecker.hasFieldWithTypeParameter,
                "hasFieldWithTypeParameter should be initialized to false");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Act
        TypeParameterClassChecker newChecker = new TypeParameterClassChecker();

        // Assert
        assertTrue(newChecker instanceof proguard.classfile.visitor.ClassVisitor,
                "TypeParameterClassChecker should implement ClassVisitor");
    }

    /**
     * Tests that the constructor creates an instance that implements AttributeVisitor.
     */
    @Test
    public void testConstructor_implementsAttributeVisitor() {
        // Act
        TypeParameterClassChecker newChecker = new TypeParameterClassChecker();

        // Assert
        assertTrue(newChecker instanceof proguard.classfile.attribute.visitor.AttributeVisitor,
                "TypeParameterClassChecker should implement AttributeVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Act
        TypeParameterClassChecker checker1 = new TypeParameterClassChecker();
        TypeParameterClassChecker checker2 = new TypeParameterClassChecker();
        TypeParameterClassChecker checker3 = new TypeParameterClassChecker();

        // Assert
        assertNotNull(checker1, "First instance should be created");
        assertNotNull(checker2, "Second instance should be created");
        assertNotNull(checker3, "Third instance should be created");
        assertNotSame(checker1, checker2, "Instances should be distinct");
        assertNotSame(checker2, checker3, "Instances should be distinct");
        assertNotSame(checker1, checker3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> new TypeParameterClassChecker(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that constructed instances have independent state.
     */
    @Test
    public void testConstructor_instancesHaveIndependentState() {
        // Arrange
        TypeParameterClassChecker checker1 = new TypeParameterClassChecker();
        TypeParameterClassChecker checker2 = new TypeParameterClassChecker();

        // Act
        checker1.hasFieldWithTypeParameter = true;

        // Assert
        assertTrue(checker1.hasFieldWithTypeParameter, "First checker should have flag set");
        assertFalse(checker2.hasFieldWithTypeParameter, "Second checker should have independent state");
    }

    // =========================================================================
    // Tests for visitAnyClass.(Lproguard/classfile/Clazz;)V
    // =========================================================================

    /**
     * Tests that visitAnyClass is a no-op and does not throw with valid class.
     */
    @Test
    public void testVisitAnyClass_withValidClass_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        boolean initialState = checker.hasFieldWithTypeParameter;

        // Act
        checker.visitAnyClass(clazz);

        // Assert
        assertEquals(initialState, checker.hasFieldWithTypeParameter,
                "visitAnyClass should not modify hasFieldWithTypeParameter");
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass does not throw with null argument.
     */
    @Test
    public void testVisitAnyClass_withNullArgument_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyClass(null),
                "visitAnyClass should not throw exception with null argument");
    }

    /**
     * Tests that visitAnyClass can be called multiple times.
     */
    @Test
    public void testVisitAnyClass_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        checker.visitAnyClass(clazz1);
        checker.visitAnyClass(clazz2);
        checker.visitAnyClass(clazz1);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "hasFieldWithTypeParameter should remain false after multiple visitAnyClass calls");
    }

    /**
     * Tests that visitAnyClass does not affect previously set state.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectPreviouslySetState() {
        // Arrange
        checker.hasFieldWithTypeParameter = true;
        Clazz clazz = mock(Clazz.class);

        // Act
        checker.visitAnyClass(clazz);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "visitAnyClass should not modify previously set hasFieldWithTypeParameter");
    }

    // =========================================================================
    // Tests for visitProgramClass.(Lproguard/classfile/ProgramClass;)V
    // =========================================================================

    /**
     * Tests that visitProgramClass processes a class with a field that has type parameter T.
     */
    @Test
    public void testVisitProgramClass_classWithTypeParameterField_setsFlag() {
        // Arrange
        ProgramClass programClass = createClassWithSignatureField("com/example/Test", "TT;");
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect field with type parameter T");
    }

    /**
     * Tests that visitProgramClass processes a class with a field signature containing <T.
     */
    @Test
    public void testVisitProgramClass_fieldWithBoundedTypeParameter_setsFlag() {
        // Arrange
        ProgramClass programClass = createClassWithSignatureField("com/example/Test", "Ljava/util/List<TT;>;");
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect field with bounded type parameter <T");
    }

    /**
     * Tests that visitProgramClass processes a class with a field signature containing ;T.
     */
    @Test
    public void testVisitProgramClass_fieldWithSemicolonT_setsFlag() {
        // Arrange
        ProgramClass programClass = createClassWithSignatureField("com/example/Test", "Ljava/util/Map<Ljava/lang/String;TValue;>;");
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect field with ;T pattern");
    }

    /**
     * Tests that visitProgramClass processes a class with a field signature containing +L (upper bound wildcard).
     */
    @Test
    public void testVisitProgramClass_fieldWithUpperBoundWildcard_setsFlag() {
        // Arrange
        ProgramClass programClass = createClassWithSignatureField("com/example/Test", "Ljava/util/List<+Ljava/lang/Object;>;");
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect field with upper bound wildcard +L");
    }

    /**
     * Tests that visitProgramClass processes a class with a field signature containing +T.
     */
    @Test
    public void testVisitProgramClass_fieldWithUpperBoundTypeParameter_setsFlag() {
        // Arrange
        ProgramClass programClass = createClassWithSignatureField("com/example/Test", "Ljava/util/List<+TElement;>;");
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect field with upper bound type parameter +T");
    }

    /**
     * Tests that visitProgramClass processes a class with a field signature containing [T (array of type parameter).
     */
    @Test
    public void testVisitProgramClass_fieldWithArrayOfTypeParameter_setsFlag() {
        // Arrange
        ProgramClass programClass = createClassWithSignatureField("com/example/Test", "[TElement;");
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect field with array of type parameter [T");
    }

    /**
     * Tests that visitProgramClass does not set flag for class without signature fields.
     */
    @Test
    public void testVisitProgramClass_classWithoutSignatureFields_doesNotSetFlag() {
        // Arrange
        ProgramClass programClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            "com/example/Simple",
            "java/lang/Object"
        )
        .addField(AccessConstants.PUBLIC, "field", "I")
        .getProgramClass();

        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not detect type parameter in class without signature fields");
    }

    /**
     * Tests that visitProgramClass does not set flag for class with non-type-parameter signature.
     */
    @Test
    public void testVisitProgramClass_fieldWithNonTypeParameterSignature_doesNotSetFlag() {
        // Arrange
        ProgramClass programClass = createClassWithSignatureField("com/example/Test", "Ljava/util/List<Ljava/lang/String;>;");
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not detect type parameter in concrete parameterized type");
    }

    /**
     * Tests that visitProgramClass handles class with multiple fields.
     */
    @Test
    public void testVisitProgramClass_classWithMultipleFields_detectsTypeParameter() {
        // Arrange
        ProgramClass programClass = createClassWithMultipleSignatureFields();
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect type parameter when one of multiple fields has it");
    }

    /**
     * Tests that visitProgramClass sets flag when at least one field has type parameter.
     */
    @Test
    public void testVisitProgramClass_oneFieldWithTypeParameter_setsFlag() {
        // Arrange - Create class with one field having type parameter and one without
        ProgramClass programClass = createClassWithMixedFields();
        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should set flag when at least one field has type parameter");
    }

    /**
     * Tests that visitProgramClass handles empty class (no fields).
     */
    @Test
    public void testVisitProgramClass_emptyClass_doesNotSetFlag() {
        // Arrange
        ProgramClass programClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            "com/example/Empty",
            "java/lang/Object"
        ).getProgramClass();

        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(programClass);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not set flag for empty class");
    }

    /**
     * Tests that visitProgramClass does not throw exception with valid class.
     */
    @Test
    public void testVisitProgramClass_validClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            "com/example/Test",
            "java/lang/Object"
        ).getProgramClass();

        programClassPool.addClass(programClass);
        initializeClassHierarchy();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitProgramClass(programClass),
                "visitProgramClass should not throw exception");
    }

    /**
     * Tests that visitProgramClass can be called multiple times with different classes.
     */
    @Test
    public void testVisitProgramClass_multipleClasses_maintainsState() {
        // Arrange
        ProgramClass class1 = createClassWithSignatureField("com/example/Class1", "Ljava/lang/String;");
        ProgramClass class2 = createClassWithSignatureField("com/example/Class2", "TT;");

        programClassPool.addClass(class1);
        programClassPool.addClass(class2);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(class1); // Should not set flag
        assertFalse(checker.hasFieldWithTypeParameter, "First class should not set flag");

        checker.visitProgramClass(class2); // Should set flag

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Flag should be set after visiting class with type parameter");
    }

    /**
     * Tests that once the flag is set, it remains set (OR behavior).
     */
    @Test
    public void testVisitProgramClass_flagRemainsSetOnceTrue() {
        // Arrange
        ProgramClass classWithParam = createClassWithSignatureField("com/example/WithParam", "TT;");
        ProgramClass classWithoutParam = createClassWithSignatureField("com/example/WithoutParam", "Ljava/lang/String;");

        programClassPool.addClass(classWithParam);
        programClassPool.addClass(classWithoutParam);
        initializeClassHierarchy();

        // Act
        checker.visitProgramClass(classWithParam);
        assertTrue(checker.hasFieldWithTypeParameter, "Flag should be set");

        checker.visitProgramClass(classWithoutParam);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Flag should remain set even after visiting class without type parameter");
    }

    // =========================================================================
    // Tests for visitAnyAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/attribute/Attribute;)V
    // =========================================================================

    /**
     * Tests that visitAnyAttribute is a no-op and does not throw with valid arguments.
     */
    @Test
    public void testVisitAnyAttribute_withValidArguments_doesNothing() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);
        boolean initialState = checker.hasFieldWithTypeParameter;

        // Act
        checker.visitAnyAttribute(clazz, attribute);

        // Assert
        assertEquals(initialState, checker.hasFieldWithTypeParameter,
                "visitAnyAttribute should not modify hasFieldWithTypeParameter");
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute does not throw with null arguments.
     */
    @Test
    public void testVisitAnyAttribute_withNullArguments_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyAttribute(null, null),
                "visitAnyAttribute should not throw exception with null arguments");
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times.
     */
    @Test
    public void testVisitAnyAttribute_multipleCalls_noSideEffects() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);

        // Act
        checker.visitAnyAttribute(clazz1, attribute1);
        checker.visitAnyAttribute(clazz2, attribute2);
        checker.visitAnyAttribute(clazz1, attribute2);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "hasFieldWithTypeParameter should remain false after multiple visitAnyAttribute calls");
    }

    /**
     * Tests that visitAnyAttribute does not affect previously set state.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectPreviouslySetState() {
        // Arrange
        checker.hasFieldWithTypeParameter = true;
        Clazz clazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        checker.visitAnyAttribute(clazz, attribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "visitAnyAttribute should not affect previously set state");
    }

    // =========================================================================
    // Tests for visitSignatureAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/Field;Lproguard/classfile/attribute/SignatureAttribute;)V
    // =========================================================================

    /**
     * Tests that visitSignatureAttribute detects signature starting with T.
     */
    @Test
    public void testVisitSignatureAttribute_signatureStartsWithT_setsFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("TElement;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect signature starting with T");
    }

    /**
     * Tests that visitSignatureAttribute detects signature containing <T.
     */
    @Test
    public void testVisitSignatureAttribute_signatureContainsLessThanT_setsFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<TElement;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect signature containing <T");
    }

    /**
     * Tests that visitSignatureAttribute detects signature containing ;T.
     */
    @Test
    public void testVisitSignatureAttribute_signatureContainsSemicolonT_setsFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/Map<Ljava/lang/String;TValue;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect signature containing ;T");
    }

    /**
     * Tests that visitSignatureAttribute detects signature containing +L.
     */
    @Test
    public void testVisitSignatureAttribute_signatureContainsPlusL_setsFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<+Ljava/lang/Number;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect signature containing +L (upper bound wildcard)");
    }

    /**
     * Tests that visitSignatureAttribute detects signature containing +T.
     */
    @Test
    public void testVisitSignatureAttribute_signatureContainsPlusT_setsFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<+TElement;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect signature containing +T");
    }

    /**
     * Tests that visitSignatureAttribute detects signature containing [T (array of type parameter).
     */
    @Test
    public void testVisitSignatureAttribute_signatureContainsBracketT_setsFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("[TElement;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect signature containing [T (array of type parameter)");
    }

    /**
     * Tests that visitSignatureAttribute does not set flag for concrete parameterized type.
     */
    @Test
    public void testVisitSignatureAttribute_concreteParameterizedType_doesNotSetFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<Ljava/lang/String;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not detect type parameter in concrete parameterized type");
    }

    /**
     * Tests that visitSignatureAttribute does not set flag for simple type signature.
     */
    @Test
    public void testVisitSignatureAttribute_simpleTypeSignature_doesNotSetFlag() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/lang/String;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not detect type parameter in simple type signature");
    }

    /**
     * Tests that visitSignatureAttribute maintains OR behavior - once set, stays set.
     */
    @Test
    public void testVisitSignatureAttribute_orBehavior_flagStaysSet() {
        // Arrange
        TestClassWithSignature withParam = createClassWithSignature("TElement;");
        TestClassWithSignature withoutParam = createClassWithSignature("Ljava/lang/String;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(withParam.clazz, field, withParam.signatureAttribute);
        assertTrue(checker.hasFieldWithTypeParameter, "Flag should be set");

        checker.visitSignatureAttribute(withoutParam.clazz, field, withoutParam.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Flag should remain set even after visiting non-type-parameter signature");
    }

    /**
     * Tests that visitSignatureAttribute detects multiple patterns in one signature.
     */
    @Test
    public void testVisitSignatureAttribute_multiplePatterns_setsFlag() {
        // Arrange - signature contains both <T and +T
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/Map<TKey;+TValue;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertTrue(checker.hasFieldWithTypeParameter,
                "Should detect type parameter when multiple patterns present");
    }

    /**
     * Tests that visitSignatureAttribute does not throw with valid arguments.
     */
    @Test
    public void testVisitSignatureAttribute_validArguments_doesNotThrow() {
        // Arrange
        TestClassWithSignature testClass = createClassWithSignature("Ljava/lang/String;");
        ProgramField field = mock(ProgramField.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute),
                "visitSignatureAttribute should not throw with valid arguments");
    }

    /**
     * Tests visitSignatureAttribute with signature containing Tab character (not type parameter T).
     */
    @Test
    public void testVisitSignatureAttribute_signatureWithTabCharacter_doesNotSetFlag() {
        // Arrange - Create signature that has "\t" but not type parameter patterns
        TestClassWithSignature testClass = createClassWithSignature("Ljava/lang/String;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not incorrectly detect tab character as type parameter");
    }

    /**
     * Tests that visitSignatureAttribute detects lower case 't' followed by uppercase (e.g., <Tablet>).
     * This should NOT be detected as type parameter since pattern is uppercase T.
     */
    @Test
    public void testVisitSignatureAttribute_lowerCaseT_doesNotSetFlag() {
        // Arrange - signature with lowercase 't' in class name
        TestClassWithSignature testClass = createClassWithSignature("Lcom/example/tablet/Tablet;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not detect lowercase 't' as type parameter");
    }

    /**
     * Tests edge case where signature contains 'T' in a class name but not as type parameter.
     */
    @Test
    public void testVisitSignatureAttribute_TInClassName_doesNotSetFlag() {
        // Arrange - 'T' appears in class name after 'L' (like LTreeSet) - should not match patterns
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/TreeSet<Ljava/lang/String;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Should not detect T in class name as type parameter");
    }

    /**
     * Tests signature with wildcard lower bound (-T or -*) is NOT detected.
     * The implementation only checks for +L and +T, not -L or -T.
     */
    @Test
    public void testVisitSignatureAttribute_lowerBoundWildcard_doesNotSetFlag() {
        // Arrange - lower bound wildcard is not checked by implementation
        TestClassWithSignature testClass = createClassWithSignature("Ljava/util/List<-Ljava/lang/Object;>;");
        ProgramField field = mock(ProgramField.class);

        // Act
        checker.visitSignatureAttribute(testClass.clazz, field, testClass.signatureAttribute);

        // Assert
        assertFalse(checker.hasFieldWithTypeParameter,
                "Implementation does not check for lower bound wildcards");
    }

    // =========================================================================
    // Helper methods
    // =========================================================================

    /**
     * Creates a ProgramClass with a field that has a SignatureAttribute.
     */
    private ProgramClass createClassWithSignatureField(String className, String fieldSignature) {
        ProgramClass programClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            className,
            "java/lang/Object"
        ).getProgramClass();

        // Create field with signature attribute
        ProgramField field = new ProgramField();
        field.u2accessFlags = AccessConstants.PUBLIC;
        field.u2nameIndex = addUtf8Constant(programClass, "field");
        field.u2descriptorIndex = addUtf8Constant(programClass, "Ljava/lang/Object;");

        // Create signature attribute
        SignatureAttribute signatureAttribute = new SignatureAttribute();
        signatureAttribute.u2signatureIndex = addUtf8Constant(programClass, fieldSignature);
        signatureAttribute.u2attributeNameIndex = addUtf8Constant(programClass, "Signature");

        field.u2attributesCount = 1;
        field.attributes = new Attribute[] { signatureAttribute };

        // Add field to class
        programClass.fields = new ProgramField[] { field };
        programClass.u2fieldsCount = 1;

        return programClass;
    }

    /**
     * Creates a ProgramClass with multiple fields, one with type parameter.
     */
    private ProgramClass createClassWithMultipleSignatureFields() {
        ProgramClass programClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            "com/example/MultiField",
            "java/lang/Object"
        ).getProgramClass();

        // Create first field without type parameter
        ProgramField field1 = new ProgramField();
        field1.u2accessFlags = AccessConstants.PUBLIC;
        field1.u2nameIndex = addUtf8Constant(programClass, "field1");
        field1.u2descriptorIndex = addUtf8Constant(programClass, "Ljava/lang/String;");

        SignatureAttribute sig1 = new SignatureAttribute();
        sig1.u2signatureIndex = addUtf8Constant(programClass, "Ljava/lang/String;");
        sig1.u2attributeNameIndex = addUtf8Constant(programClass, "Signature");
        field1.u2attributesCount = 1;
        field1.attributes = new Attribute[] { sig1 };

        // Create second field with type parameter
        ProgramField field2 = new ProgramField();
        field2.u2accessFlags = AccessConstants.PUBLIC;
        field2.u2nameIndex = addUtf8Constant(programClass, "field2");
        field2.u2descriptorIndex = addUtf8Constant(programClass, "Ljava/lang/Object;");

        SignatureAttribute sig2 = new SignatureAttribute();
        sig2.u2signatureIndex = addUtf8Constant(programClass, "TT;");
        sig2.u2attributeNameIndex = addUtf8Constant(programClass, "Signature");
        field2.u2attributesCount = 1;
        field2.attributes = new Attribute[] { sig2 };

        // Add fields to class
        programClass.fields = new ProgramField[] { field1, field2 };
        programClass.u2fieldsCount = 2;

        return programClass;
    }

    /**
     * Creates a ProgramClass with mixed fields.
     */
    private ProgramClass createClassWithMixedFields() {
        return createClassWithMultipleSignatureFields();
    }

    /**
     * Adds a UTF8 constant to the program class constant pool.
     */
    private int addUtf8Constant(ProgramClass programClass, String value) {
        // Expand constant pool if needed
        if (programClass.constantPool == null) {
            programClass.constantPool = new Constant[10];
            programClass.u2constantPoolCount = 10;
        }

        // Find next available index
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                programClass.constantPool[i] = new Utf8Constant(value);
                return i;
            }
        }

        // Expand pool if full
        Constant[] newPool = new Constant[programClass.constantPool.length * 2];
        System.arraycopy(programClass.constantPool, 0, newPool, 0, programClass.constantPool.length);
        int index = programClass.constantPool.length;
        programClass.constantPool = newPool;
        programClass.u2constantPoolCount = newPool.length;
        programClass.constantPool[index] = new Utf8Constant(value);
        return index;
    }

    /**
     * Creates a test class with a signature attribute.
     */
    private TestClassWithSignature createClassWithSignature(String signatureString) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool
        Constant[] constantPool = new Constant[10];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant("TestClass");

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;

        // Create the SignatureAttribute
        SignatureAttribute signatureAttribute = new SignatureAttribute();
        int signatureIndex = getNextAvailableIndex(programClass);
        programClass.constantPool[signatureIndex] = new Utf8Constant(signatureString);
        signatureAttribute.u2signatureIndex = signatureIndex;

        return new TestClassWithSignature(programClass, signatureAttribute);
    }

    /**
     * Gets the next available index in the constant pool.
     */
    private int getNextAvailableIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("Constant pool is full");
    }

    /**
     * Initialize class hierarchy.
     */
    private void initializeClassHierarchy() {
        programClassPool.classesAccept(new ClassReferenceInitializer(programClassPool, libraryClassPool));
    }

    /**
     * Helper class to hold a ProgramClass and its associated SignatureAttribute.
     */
    private static class TestClassWithSignature {
        final ProgramClass clazz;
        final SignatureAttribute signatureAttribute;

        TestClassWithSignature(ProgramClass clazz, SignatureAttribute signatureAttribute) {
            this.clazz = clazz;
            this.signatureAttribute = signatureAttribute;
        }
    }
}
