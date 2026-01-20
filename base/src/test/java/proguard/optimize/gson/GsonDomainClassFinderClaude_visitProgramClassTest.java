package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.WarningPrinter;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GsonDomainClassFinder.visitProgramClass method.
 * Tests the visitProgramClass(ProgramClass) method which is the entry point
 * for analyzing classes for GSON domain class optimization.
 */
public class GsonDomainClassFinderClaude_visitProgramClassTest {

    private GsonRuntimeSettings gsonRuntimeSettings;
    private ClassPool gsonDomainClassPool;
    private WarningPrinter warningPrinter;
    private GsonDomainClassFinder finder;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;

    @BeforeEach
    public void setUp() {
        gsonRuntimeSettings = new GsonRuntimeSettings();
        gsonDomainClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        finder = new GsonDomainClassFinder(gsonRuntimeSettings, gsonDomainClassPool, warningPrinter);

        // Add commonly needed library classes
        addLibraryClass("java/lang/Object");
        addLibraryClass("java/lang/Enum");
        addLibraryClass("java/lang/String");
    }

    private void addLibraryClass(String className) {
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.thisClassName = className;
        libraryClass.superClassName = className.equals("java/lang/Object") ? null : "java/lang/Object";
        libraryClassPool.addClass(libraryClass);
    }

    /**
     * Tests that visitProgramClass adds a simple valid class to the domain class pool.
     */
    @Test
    public void testVisitProgramClass_simpleClass_addsToPool() {
        // Arrange
        ProgramClass simpleClass = createSimpleClass("com/example/SimpleClass", "java/lang/Object");
        programClassPool.addClass(simpleClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(simpleClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/SimpleClass"),
            "Simple class should be added to domain class pool");
    }

    /**
     * Tests that visitProgramClass can be called without throwing exceptions.
     */
    @Test
    public void testVisitProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass testClass = createSimpleClass("com/example/Test", "java/lang/Object");
        programClassPool.addClass(testClass);
        initializeClassHierarchy(programClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitProgramClass(testClass),
            "visitProgramClass should not throw exception");
    }

    /**
     * Tests that visitProgramClass handles a class with fields.
     */
    @Test
    public void testVisitProgramClass_classWithFields_addsToPool() {
        // Arrange
        ProgramClass classWithFields = createClassWithFields("com/example/WithFields", "java/lang/Object");
        programClassPool.addClass(classWithFields);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(classWithFields);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/WithFields"),
            "Class with fields should be added to domain class pool");
    }

    /**
     * Tests that visitProgramClass excludes interface types.
     */
    @Test
    public void testVisitProgramClass_interface_notAddedToPool() {
        // Arrange
        ProgramClass interfaceClass = createInterface("com/example/MyInterface");
        programClassPool.addClass(interfaceClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(interfaceClass);

        // Assert
        assertNull(gsonDomainClassPool.getClass("com/example/MyInterface"),
            "Interface should not be added to domain class pool");
    }

    /**
     * Tests that visitProgramClass processes multiple classes when called multiple times.
     */
    @Test
    public void testVisitProgramClass_multipleClasses_allProcessed() {
        // Arrange
        ProgramClass class1 = createSimpleClass("com/example/Class1", "java/lang/Object");
        ProgramClass class2 = createSimpleClass("com/example/Class2", "java/lang/Object");
        ProgramClass class3 = createSimpleClass("com/example/Class3", "java/lang/Object");
        programClassPool.addClass(class1);
        programClassPool.addClass(class2);
        programClassPool.addClass(class3);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(class1);
        finder.visitProgramClass(class2);
        finder.visitProgramClass(class3);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/Class1"),
            "First class should be in pool");
        assertNotNull(gsonDomainClassPool.getClass("com/example/Class2"),
            "Second class should be in pool");
        assertNotNull(gsonDomainClassPool.getClass("com/example/Class3"),
            "Third class should be in pool");
    }

    /**
     * Tests that visitProgramClass handles the same class being visited twice.
     * The second visit should not add the class again.
     */
    @Test
    public void testVisitProgramClass_sameClassTwice_onlyAddedOnce() {
        // Arrange
        ProgramClass testClass = createSimpleClass("com/example/Duplicate", "java/lang/Object");
        programClassPool.addClass(testClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(testClass);
        int countAfterFirst = gsonDomainClassPool.size();
        finder.visitProgramClass(testClass);
        int countAfterSecond = gsonDomainClassPool.size();

        // Assert
        assertEquals(countAfterFirst, countAfterSecond,
            "Class should not be added twice");
    }

    /**
     * Tests that visitProgramClass handles a class with transient fields.
     * Transient fields are excluded by default GSON behavior.
     */
    @Test
    public void testVisitProgramClass_classWithTransientField_addsToPool() {
        // Arrange
        ProgramClass classWithTransient = createClassWithTransientField("com/example/WithTransient");
        programClassPool.addClass(classWithTransient);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(classWithTransient);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/WithTransient"),
            "Class with transient field should be added to pool");
    }

    /**
     * Tests that visitProgramClass handles a class with static fields.
     * Static fields are excluded by default GSON behavior.
     */
    @Test
    public void testVisitProgramClass_classWithStaticField_addsToPool() {
        // Arrange
        ProgramClass classWithStatic = createClassWithStaticField("com/example/WithStatic");
        programClassPool.addClass(classWithStatic);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(classWithStatic);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/WithStatic"),
            "Class with static field should be added to pool");
    }

    /**
     * Tests that visitProgramClass handles a class hierarchy (class with superclass).
     */
    @Test
    public void testVisitProgramClass_classWithSuperclass_processesHierarchy() {
        // Arrange
        ProgramClass superClass = createSimpleClass("com/example/SuperClass", "java/lang/Object");
        ProgramClass subClass = createSimpleClass("com/example/SubClass", "com/example/SuperClass");
        programClassPool.addClass(superClass);
        programClassPool.addClass(subClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(subClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/SubClass"),
            "Subclass should be added to pool");
    }

    /**
     * Tests that visitProgramClass handles enum classes.
     * Enums should be processed but sub-enums without additional fields are excluded.
     */
    @Test
    public void testVisitProgramClass_enumClass_addsToPool() {
        // Arrange
        ProgramClass enumClass = createEnumClass("com/example/MyEnum");
        programClassPool.addClass(enumClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(enumClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/MyEnum"),
            "Enum class should be added to pool");
    }

    /**
     * Tests that visitProgramClass handles an abstract class.
     */
    @Test
    public void testVisitProgramClass_abstractClass_addsToPool() {
        // Arrange
        ProgramClass abstractClass = createAbstractClass("com/example/AbstractClass");
        programClassPool.addClass(abstractClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(abstractClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/AbstractClass"),
            "Abstract class should be added to pool");
    }

    /**
     * Tests that visitProgramClass works with excludeFieldsWithModifiers=true.
     */
    @Test
    public void testVisitProgramClass_withExcludeFieldsWithModifiersTrue_addsToPool() {
        // Arrange
        gsonRuntimeSettings.excludeFieldsWithModifiers = true;
        GsonDomainClassFinder finderWithExclusion = new GsonDomainClassFinder(
            gsonRuntimeSettings, gsonDomainClassPool, warningPrinter);

        ProgramClass testClass = createClassWithFields("com/example/TestClass", "java/lang/Object");
        programClassPool.addClass(testClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finderWithExclusion.visitProgramClass(testClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/TestClass"),
            "Class should be added even with excludeFieldsWithModifiers=true");
    }

    /**
     * Tests that visitProgramClass works with excludeFieldsWithModifiers=false.
     */
    @Test
    public void testVisitProgramClass_withExcludeFieldsWithModifiersFalse_addsToPool() {
        // Arrange
        gsonRuntimeSettings.excludeFieldsWithModifiers = false;
        GsonDomainClassFinder finderWithoutExclusion = new GsonDomainClassFinder(
            gsonRuntimeSettings, gsonDomainClassPool, warningPrinter);

        ProgramClass testClass = createClassWithFields("com/example/TestClass", "java/lang/Object");
        programClassPool.addClass(testClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finderWithoutExclusion.visitProgramClass(testClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/TestClass"),
            "Class should be added with excludeFieldsWithModifiers=false");
    }

    /**
     * Tests that visitProgramClass handles a class with synthetic field.
     * Synthetic fields are excluded by the implementation.
     */
    @Test
    public void testVisitProgramClass_classWithSyntheticField_addsToPool() {
        // Arrange
        ProgramClass classWithSynthetic = createClassWithSyntheticField("com/example/WithSynthetic");
        programClassPool.addClass(classWithSynthetic);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(classWithSynthetic);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/WithSynthetic"),
            "Class with synthetic field should be added to pool");
    }

    /**
     * Tests that visitProgramClass works with null WarningPrinter.
     */
    @Test
    public void testVisitProgramClass_withNullWarningPrinter_addsToPool() {
        // Arrange
        GsonDomainClassFinder finderWithNullPrinter = new GsonDomainClassFinder(
            gsonRuntimeSettings, gsonDomainClassPool, null);

        ProgramClass testClass = createSimpleClass("com/example/TestClass", "java/lang/Object");
        programClassPool.addClass(testClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finderWithNullPrinter.visitProgramClass(testClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/TestClass"),
            "Class should be added even with null WarningPrinter");
    }

    /**
     * Tests that visitProgramClass works with a kept class (has DONT_SHRINK and DONT_OBFUSCATE flags).
     */
    @Test
    public void testVisitProgramClass_keptClass_addsToPool() {
        // Arrange
        ProgramClass keptClass = createSimpleClass("com/example/KeptClass", "java/lang/Object");
        keptClass.setProcessingFlags(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OBFUSCATE);
        programClassPool.addClass(keptClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(keptClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/KeptClass"),
            "Kept class should be added to pool");
    }

    /**
     * Tests that visitProgramClass handles multiple classes with different configurations.
     */
    @Test
    public void testVisitProgramClass_mixedClasses_processesCorrectly() {
        // Arrange
        ProgramClass simpleClass = createSimpleClass("com/example/Simple", "java/lang/Object");
        ProgramClass classWithFields = createClassWithFields("com/example/WithFields", "java/lang/Object");
        ProgramClass interfaceClass = createInterface("com/example/Interface");

        programClassPool.addClass(simpleClass);
        programClassPool.addClass(classWithFields);
        programClassPool.addClass(interfaceClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(simpleClass);
        finder.visitProgramClass(classWithFields);
        finder.visitProgramClass(interfaceClass);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/Simple"),
            "Simple class should be in pool");
        assertNotNull(gsonDomainClassPool.getClass("com/example/WithFields"),
            "Class with fields should be in pool");
        assertNull(gsonDomainClassPool.getClass("com/example/Interface"),
            "Interface should not be in pool");
    }

    /**
     * Tests that visitProgramClass handles a complex class hierarchy with multiple levels.
     */
    @Test
    public void testVisitProgramClass_deepHierarchy_processesCorrectly() {
        // Arrange
        ProgramClass level1 = createSimpleClass("com/example/Level1", "java/lang/Object");
        ProgramClass level2 = createSimpleClass("com/example/Level2", "com/example/Level1");
        ProgramClass level3 = createSimpleClass("com/example/Level3", "com/example/Level2");

        programClassPool.addClass(level1);
        programClassPool.addClass(level2);
        programClassPool.addClass(level3);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(level3);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/Level3"),
            "Level 3 class should be in pool");
    }

    /**
     * Tests that visitProgramClass handles a class with methods.
     */
    @Test
    public void testVisitProgramClass_classWithMethods_addsToPool() {
        // Arrange
        ProgramClass classWithMethods = createClassWithMethods("com/example/WithMethods");
        programClassPool.addClass(classWithMethods);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(classWithMethods);

        // Assert
        assertNotNull(gsonDomainClassPool.getClass("com/example/WithMethods"),
            "Class with methods should be added to pool");
    }

    /**
     * Tests that visitProgramClass processes the class immediately (not deferred).
     */
    @Test
    public void testVisitProgramClass_processesImmediately() {
        // Arrange
        ProgramClass testClass = createSimpleClass("com/example/Immediate", "java/lang/Object");
        programClassPool.addClass(testClass);
        initializeClassHierarchy(programClassPool);

        // Act
        finder.visitProgramClass(testClass);

        // Assert - immediately after calling visitProgramClass, the class should be processed
        assertNotNull(gsonDomainClassPool.getClass("com/example/Immediate"),
            "Class should be processed immediately, not deferred");
    }

    // Helper methods to create test classes

    private ProgramClass createSimpleClass(String name, String superName) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            superName
        ).getProgramClass();
    }

    private ProgramClass createClassWithFields(String name, String superName) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            superName
        )
        .addField(AccessConstants.PUBLIC, "field1", "I")
        .addField(AccessConstants.PRIVATE, "field2", "Ljava/lang/String;")
        .getProgramClass();
    }

    private ProgramClass createClassWithTransientField(String name) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            "java/lang/Object"
        )
        .addField(AccessConstants.PUBLIC | AccessConstants.TRANSIENT, "transientField", "I")
        .getProgramClass();
    }

    private ProgramClass createClassWithStaticField(String name) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            "java/lang/Object"
        )
        .addField(AccessConstants.PUBLIC | AccessConstants.STATIC, "staticField", "I")
        .getProgramClass();
    }

    private ProgramClass createClassWithSyntheticField(String name) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            "java/lang/Object"
        )
        .addField(AccessConstants.PUBLIC | AccessConstants.SYNTHETIC, "syntheticField", "I")
        .getProgramClass();
    }

    private ProgramClass createInterface(String name) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC | AccessConstants.INTERFACE | AccessConstants.ABSTRACT,
            name,
            "java/lang/Object"
        ).getProgramClass();
    }

    private ProgramClass createEnumClass(String name) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC | AccessConstants.ENUM,
            name,
            "java/lang/Enum"
        ).getProgramClass();
    }

    private ProgramClass createAbstractClass(String name) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC | AccessConstants.ABSTRACT,
            name,
            "java/lang/Object"
        ).getProgramClass();
    }

    private ProgramClass createClassWithMethods(String name) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            "java/lang/Object"
        )
        .addMethod(AccessConstants.PUBLIC, "method1", "()V", 50, code -> code.return_())
        .addMethod(AccessConstants.PRIVATE, "method2", "(I)I", 50, code -> code.iload_1().ireturn())
        .getProgramClass();
    }

    /**
     * Initialize class hierarchy by setting up references between classes.
     * This is necessary for hierarchyAccept calls to work properly.
     */
    private void initializeClassHierarchy(ClassPool classPool) {
        // Initialize references between classes in the pool
        classPool.classesAccept(new ClassReferenceInitializer(classPool, libraryClassPool));
    }
}
