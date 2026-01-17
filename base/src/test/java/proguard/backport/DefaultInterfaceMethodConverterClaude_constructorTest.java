package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DefaultInterfaceMethodConverter} constructor.
 * Tests the constructor with signature:
 * (Lproguard/classfile/visitor/ClassVisitor;Lproguard/classfile/visitor/MemberVisitor;)V
 */
public class DefaultInterfaceMethodConverterClaude_constructorTest {

    /**
     * Tests the constructor with valid non-null parameters.
     * Verifies that the converter can be instantiated with both visitors provided.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Arrange
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter =
            new DefaultInterfaceMethodConverter(modifiedClassVisitor, extraMemberVisitor);

        // Assert
        assertNotNull(converter, "Converter should be created successfully");
    }

    /**
     * Tests the constructor with null modifiedClassVisitor.
     * Verifies that the converter can be instantiated with null ClassVisitor.
     */
    @Test
    public void testConstructorWithNullModifiedClassVisitor() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter =
            new DefaultInterfaceMethodConverter(null, extraMemberVisitor);

        // Assert
        assertNotNull(converter, "Converter should be created with null modifiedClassVisitor");
    }

    /**
     * Tests the constructor with null extraMemberVisitor.
     * Verifies that the converter can be instantiated with null MemberVisitor.
     */
    @Test
    public void testConstructorWithNullExtraMemberVisitor() {
        // Arrange
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter =
            new DefaultInterfaceMethodConverter(modifiedClassVisitor, null);

        // Assert
        assertNotNull(converter, "Converter should be created with null extraMemberVisitor");
    }

    /**
     * Tests the constructor with both parameters null.
     * Verifies that the converter can be instantiated with all null parameters.
     */
    @Test
    public void testConstructorWithBothParametersNull() {
        // Act
        DefaultInterfaceMethodConverter converter =
            new DefaultInterfaceMethodConverter(null, null);

        // Assert
        assertNotNull(converter, "Converter should be created with both parameters null");
    }

    /**
     * Tests creating multiple converter instances with the same visitors.
     * Verifies that multiple instances can be created using the same visitor objects.
     */
    @Test
    public void testMultipleConvertersWithSameVisitors() {
        // Arrange
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter1 =
            new DefaultInterfaceMethodConverter(modifiedClassVisitor, extraMemberVisitor);
        DefaultInterfaceMethodConverter converter2 =
            new DefaultInterfaceMethodConverter(modifiedClassVisitor, extraMemberVisitor);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests creating multiple converter instances with different visitors.
     * Verifies that converters can be created independently with different visitor instances.
     */
    @Test
    public void testMultipleConvertersWithDifferentVisitors() {
        // Arrange
        ClassVisitor classVisitor1 = mock(ClassVisitor.class);
        ClassVisitor classVisitor2 = mock(ClassVisitor.class);
        MemberVisitor memberVisitor1 = mock(MemberVisitor.class);
        MemberVisitor memberVisitor2 = mock(MemberVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter1 =
            new DefaultInterfaceMethodConverter(classVisitor1, memberVisitor1);
        DefaultInterfaceMethodConverter converter2 =
            new DefaultInterfaceMethodConverter(classVisitor2, memberVisitor2);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with the same ClassVisitor but different MemberVisitor.
     * Verifies that converters can share some visitors while being independent.
     */
    @Test
    public void testConstructorWithSharedClassVisitor() {
        // Arrange
        ClassVisitor sharedClassVisitor = mock(ClassVisitor.class);
        MemberVisitor memberVisitor1 = mock(MemberVisitor.class);
        MemberVisitor memberVisitor2 = mock(MemberVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter1 =
            new DefaultInterfaceMethodConverter(sharedClassVisitor, memberVisitor1);
        DefaultInterfaceMethodConverter converter2 =
            new DefaultInterfaceMethodConverter(sharedClassVisitor, memberVisitor2);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with the same MemberVisitor but different ClassVisitor.
     * Verifies that converters can share some visitors while being independent.
     */
    @Test
    public void testConstructorWithSharedMemberVisitor() {
        // Arrange
        ClassVisitor classVisitor1 = mock(ClassVisitor.class);
        ClassVisitor classVisitor2 = mock(ClassVisitor.class);
        MemberVisitor sharedMemberVisitor = mock(MemberVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter1 =
            new DefaultInterfaceMethodConverter(classVisitor1, sharedMemberVisitor);
        DefaultInterfaceMethodConverter converter2 =
            new DefaultInterfaceMethodConverter(classVisitor2, sharedMemberVisitor);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with different combinations of null and non-null visitors.
     * Verifies that all combinations of null/non-null parameters are handled.
     */
    @Test
    public void testConstructorWithVariousNullCombinations() {
        // Test case 1: both non-null
        ClassVisitor cv1 = mock(ClassVisitor.class);
        MemberVisitor mv1 = mock(MemberVisitor.class);
        DefaultInterfaceMethodConverter converter1 =
            new DefaultInterfaceMethodConverter(cv1, mv1);
        assertNotNull(converter1, "Converter with both non-null should be created");

        // Test case 2: first null, second non-null
        MemberVisitor mv2 = mock(MemberVisitor.class);
        DefaultInterfaceMethodConverter converter2 =
            new DefaultInterfaceMethodConverter(null, mv2);
        assertNotNull(converter2, "Converter with null ClassVisitor should be created");

        // Test case 3: first non-null, second null
        ClassVisitor cv3 = mock(ClassVisitor.class);
        DefaultInterfaceMethodConverter converter3 =
            new DefaultInterfaceMethodConverter(cv3, null);
        assertNotNull(converter3, "Converter with null MemberVisitor should be created");

        // Test case 4: both null
        DefaultInterfaceMethodConverter converter4 =
            new DefaultInterfaceMethodConverter(null, null);
        assertNotNull(converter4, "Converter with both null should be created");
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);
        long startTime = System.nanoTime();

        // Act
        DefaultInterfaceMethodConverter converter =
            new DefaultInterfaceMethodConverter(modifiedClassVisitor, extraMemberVisitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(converter, "Converter should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests the constructor doesn't invoke any methods on the visitor parameters.
     * Verifies that the constructor only stores the visitors without using them.
     */
    @Test
    public void testConstructorDoesNotInvokeVisitors() {
        // Arrange
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        DefaultInterfaceMethodConverter converter =
            new DefaultInterfaceMethodConverter(modifiedClassVisitor, extraMemberVisitor);

        // Assert
        assertNotNull(converter, "Converter should be created");
        verifyNoInteractions(modifiedClassVisitor);
        verifyNoInteractions(extraMemberVisitor);
    }
}
