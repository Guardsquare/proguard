package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.visitor.ClassVisitor;
import proguard.io.ExtraDataEntryNameMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionConverter} constructor.
 * Tests LambdaExpressionConverter(ClassPool, ClassPool, ExtraDataEntryNameMap, ClassVisitor) constructor.
 */
public class LambdaExpressionConverterClaude_constructorTest {

    /**
     * Tests the constructor with valid non-null parameters.
     * Verifies that the converter can be instantiated with valid parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create LambdaExpressionConverter with valid parameters
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter was created successfully
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated successfully");
    }

    /**
     * Tests the constructor with all null parameters.
     * Verifies that the constructor accepts null values.
     */
    @Test
    public void testConstructorWithAllNullParameters() {
        // Act - Create converter with all null parameters
        LambdaExpressionConverter converter = new LambdaExpressionConverter(null, null, null, null);

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated even with null parameters");
    }

    /**
     * Tests the constructor with null program class pool.
     * Verifies that the constructor accepts a null program class pool.
     */
    @Test
    public void testConstructorWithNullProgramClassPool() {
        // Arrange - Create valid parameters except program class pool
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter with null program class pool
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                null,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with null program class pool");
    }

    /**
     * Tests the constructor with null library class pool.
     * Verifies that the constructor accepts a null library class pool.
     */
    @Test
    public void testConstructorWithNullLibraryClassPool() {
        // Arrange - Create valid parameters except library class pool
        ClassPool programClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter with null library class pool
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                null,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with null library class pool");
    }

    /**
     * Tests the constructor with null extra data entry name map.
     * Verifies that the constructor accepts a null extra data entry name map.
     */
    @Test
    public void testConstructorWithNullExtraDataEntryNameMap() {
        // Arrange - Create valid parameters except extra data entry name map
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter with null extra data entry name map
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                null,
                extraClassVisitor
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with null extra data entry name map");
    }

    /**
     * Tests the constructor with null extra class visitor.
     * Verifies that the constructor accepts a null extra class visitor.
     */
    @Test
    public void testConstructorWithNullExtraClassVisitor() {
        // Arrange - Create valid parameters except extra class visitor
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();

        // Act - Create converter with null extra class visitor
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                null
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with null extra class visitor");
    }

    /**
     * Tests the constructor with empty class pools.
     * Verifies that the converter can be created with empty but non-null class pools.
     */
    @Test
    public void testConstructorWithEmptyClassPools() {
        // Arrange - Create empty class pools and other parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter with empty class pools
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter was created successfully
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with empty class pools");
    }

    /**
     * Tests the constructor with same class pool for both program and library.
     * Verifies that the same class pool instance can be used for both parameters.
     */
    @Test
    public void testConstructorWithSameClassPoolForBoth() {
        // Arrange - Create a single class pool to use for both parameters
        ClassPool classPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter with same class pool for both parameters
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                classPool,
                classPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter was created successfully
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with same class pool for both parameters");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each converter instance is independent.
     */
    @Test
    public void testMultipleConverterInstances() {
        // Arrange - Create different parameters for each instance
        ClassPool programClassPool1 = new ClassPool();
        ClassPool libraryClassPool1 = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap1 = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor1 = mock(ClassVisitor.class);

        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap2 = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor2 = mock(ClassVisitor.class);

        // Act - Create two converter instances
        LambdaExpressionConverter converter1 = new LambdaExpressionConverter(
                programClassPool1,
                libraryClassPool1,
                extraDataEntryNameMap1,
                extraClassVisitor1
        );
        LambdaExpressionConverter converter2 = new LambdaExpressionConverter(
                programClassPool2,
                libraryClassPool2,
                extraDataEntryNameMap2,
                extraClassVisitor2
        );

        // Assert - Verify both converters were created successfully
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different objects");
    }

    /**
     * Tests the constructor with the same parameters used to create multiple converters.
     * Verifies that the same parameters can be used for multiple converters.
     */
    @Test
    public void testMultipleConvertersWithSameParameters() {
        // Arrange - Create single instances to use for multiple converters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create two converter instances with the same parameters
        LambdaExpressionConverter converter1 = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );
        LambdaExpressionConverter converter2 = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify both converters were created successfully
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different objects");
    }

    /**
     * Tests the constructor implements ClassVisitor interface.
     * Verifies that LambdaExpressionConverter can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter implements ClassVisitor
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, converter,
                "LambdaExpressionConverter should implement ClassVisitor interface");
    }

    /**
     * Tests the constructor implements MemberVisitor interface.
     * Verifies that LambdaExpressionConverter can be used as a MemberVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter implements MemberVisitor
        assertInstanceOf(proguard.classfile.visitor.MemberVisitor.class, converter,
                "LambdaExpressionConverter should implement MemberVisitor interface");
    }

    /**
     * Tests the constructor implements AttributeVisitor interface.
     * Verifies that LambdaExpressionConverter can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter implements AttributeVisitor
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, converter,
                "LambdaExpressionConverter should implement AttributeVisitor interface");
    }

    /**
     * Tests the constructor implements InstructionVisitor interface.
     * Verifies that LambdaExpressionConverter can be used as an InstructionVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfInstructionVisitor() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );

        // Assert - Verify the converter implements InstructionVisitor
        assertInstanceOf(proguard.classfile.instruction.visitor.InstructionVisitor.class, converter,
                "LambdaExpressionConverter should implement InstructionVisitor interface");
    }

    /**
     * Tests the constructor with only program class pool provided.
     * Verifies that converter can be created with only program class pool and nulls for other parameters.
     */
    @Test
    public void testConstructorWithOnlyProgramClassPool() {
        // Arrange - Create only program class pool
        ClassPool programClassPool = new ClassPool();

        // Act - Create converter with only program class pool
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                programClassPool,
                null,
                null,
                null
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with only program class pool");
    }

    /**
     * Tests the constructor with only library class pool provided.
     * Verifies that converter can be created with only library class pool and nulls for other parameters.
     */
    @Test
    public void testConstructorWithOnlyLibraryClassPool() {
        // Arrange - Create only library class pool
        ClassPool libraryClassPool = new ClassPool();

        // Act - Create converter with only library class pool
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                null,
                libraryClassPool,
                null,
                null
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with only library class pool");
    }

    /**
     * Tests the constructor with only extra data entry name map provided.
     * Verifies that converter can be created with only extra data entry name map and nulls for other parameters.
     */
    @Test
    public void testConstructorWithOnlyExtraDataEntryNameMap() {
        // Arrange - Create only extra data entry name map
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();

        // Act - Create converter with only extra data entry name map
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                null,
                null,
                extraDataEntryNameMap,
                null
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with only extra data entry name map");
    }

    /**
     * Tests the constructor with only extra class visitor provided.
     * Verifies that converter can be created with only extra class visitor and nulls for other parameters.
     */
    @Test
    public void testConstructorWithOnlyExtraClassVisitor() {
        // Arrange - Create only extra class visitor
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act - Create converter with only extra class visitor
        LambdaExpressionConverter converter = new LambdaExpressionConverter(
                null,
                null,
                null,
                extraClassVisitor
        );

        // Assert - Verify the converter was created
        assertNotNull(converter, "LambdaExpressionConverter should be instantiated with only extra class visitor");
    }
}
