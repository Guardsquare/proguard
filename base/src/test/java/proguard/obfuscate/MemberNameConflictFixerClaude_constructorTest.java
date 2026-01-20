package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.util.WarningPrinter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberNameConflictFixer} constructor.
 * Tests the constructor:
 * <init>.(ZLjava/util/Map;Lproguard/classfile/util/WarningPrinter;Lproguard/obfuscate/MemberObfuscator;)V
 */
public class MemberNameConflictFixerClaude_constructorTest {

    /**
     * Tests the constructor with all valid non-null parameters.
     * Verifies that a MemberNameConflictFixer can be instantiated with valid parameters.
     */
    @Test
    public void testConstructorWithAllValidParameters() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be created successfully");
    }

    /**
     * Tests the constructor with allowAggressiveOverloading set to false.
     * Verifies that the constructor accepts both true and false values.
     */
    @Test
    public void testConstructorWithAggressiveOverloadingDisabled() {
        // Arrange
        boolean allowAggressiveOverloading = false;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be created with aggressive overloading disabled");
    }

    /**
     * Tests the constructor with an empty descriptor map.
     * Verifies that an empty map is accepted.
     */
    @Test
    public void testConstructorWithEmptyDescriptorMap() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> emptyDescriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            emptyDescriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be created with empty descriptor map");
    }

    /**
     * Tests the constructor with a populated descriptor map.
     * Verifies that a pre-populated map is accepted.
     */
    @Test
    public void testConstructorWithPopulatedDescriptorMap() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("newName", "oldName");
        descriptorMap.put("()V", nameMap);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be created with populated descriptor map");
    }

    /**
     * Tests the constructor with null WarningPrinter.
     * Verifies that null WarningPrinter is accepted (it's optional).
     */
    @Test
    public void testConstructorWithNullWarningPrinter() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            null,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be created with null WarningPrinter");
    }

    /**
     * Tests that multiple instances can be created with different parameters.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleFixerInstances() {
        // Arrange
        Map<String, Map<String, String>> descriptorMap1 = new HashMap<>();
        Map<String, Map<String, String>> descriptorMap2 = new HashMap<>();
        StringWriter stringWriter1 = new StringWriter();
        PrintWriter printWriter1 = new PrintWriter(stringWriter1);
        WarningPrinter warningPrinter1 = new WarningPrinter(printWriter1);
        StringWriter stringWriter2 = new StringWriter();
        PrintWriter printWriter2 = new PrintWriter(stringWriter2);
        WarningPrinter warningPrinter2 = new WarningPrinter(printWriter2);
        MemberObfuscator memberObfuscator1 = mock(MemberObfuscator.class);
        MemberObfuscator memberObfuscator2 = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer1 = new MemberNameConflictFixer(
            true,
            descriptorMap1,
            warningPrinter1,
            memberObfuscator1
        );

        MemberNameConflictFixer fixer2 = new MemberNameConflictFixer(
            false,
            descriptorMap2,
            warningPrinter2,
            memberObfuscator2
        );

        // Assert
        assertNotNull(fixer1, "First fixer should be created");
        assertNotNull(fixer2, "Second fixer should be created");
        assertNotSame(fixer1, fixer2, "Fixers should be different instances");
    }

    /**
     * Tests the constructor with same parameters creates different instances.
     * Verifies that each constructor call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDifferentInstances() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer1 = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        MemberNameConflictFixer fixer2 = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotSame(fixer1, fixer2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that the fixer implements MemberVisitor interface.
     * Verifies that it can be used as a MemberVisitor.
     */
    @Test
    public void testFixerImplementsMemberVisitor() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertTrue(fixer instanceof proguard.classfile.visitor.MemberVisitor,
                   "MemberNameConflictFixer should implement MemberVisitor");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the MemberObfuscator.
     * Verifies that the constructor only stores the obfuscator without using it.
     */
    @Test
    public void testConstructorDoesNotInvokeMemberObfuscator() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "Fixer should be created");
        verifyNoInteractions(memberObfuscator);
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);
        long startTime = System.nanoTime();

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(fixer, "Fixer should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating multiple fixers with the same descriptor map.
     * Verifies that multiple instances can share the same map object.
     */
    @Test
    public void testMultipleFixersWithSameDescriptorMap() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> sharedDescriptorMap = new HashMap<>();
        StringWriter stringWriter1 = new StringWriter();
        PrintWriter printWriter1 = new PrintWriter(stringWriter1);
        WarningPrinter warningPrinter1 = new WarningPrinter(printWriter1);
        StringWriter stringWriter2 = new StringWriter();
        PrintWriter printWriter2 = new PrintWriter(stringWriter2);
        WarningPrinter warningPrinter2 = new WarningPrinter(printWriter2);
        MemberObfuscator memberObfuscator1 = mock(MemberObfuscator.class);
        MemberObfuscator memberObfuscator2 = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer1 = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            sharedDescriptorMap,
            warningPrinter1,
            memberObfuscator1
        );

        MemberNameConflictFixer fixer2 = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            sharedDescriptorMap,
            warningPrinter2,
            memberObfuscator2
        );

        // Assert
        assertNotNull(fixer1, "First fixer should be created");
        assertNotNull(fixer2, "Second fixer should be created");
        assertNotSame(fixer1, fixer2, "Fixer instances should be different");
    }

    /**
     * Tests creating fixers with different boolean flag values.
     * Verifies that both flag states are accepted.
     */
    @Test
    public void testConstructorWithDifferentBooleanFlags() {
        // Arrange
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixerTrue = new MemberNameConflictFixer(
            true,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        MemberNameConflictFixer fixerFalse = new MemberNameConflictFixer(
            false,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixerTrue, "Fixer with true flag should be created");
        assertNotNull(fixerFalse, "Fixer with false flag should be created");
        assertNotSame(fixerTrue, fixerFalse, "Fixer instances should be different");
    }

    /**
     * Tests the constructor with a large descriptor map.
     * Verifies that the constructor handles large maps efficiently.
     */
    @Test
    public void testConstructorWithLargeDescriptorMap() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> largeDescriptorMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            Map<String, String> nameMap = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                nameMap.put("newName" + j, "oldName" + j);
            }
            largeDescriptorMap.put("descriptor" + i, nameMap);
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            largeDescriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be created with large descriptor map");
    }

    /**
     * Tests that fixer can be assigned to MemberVisitor reference.
     * Verifies interface implementation.
     */
    @Test
    public void testFixerAsMemberVisitor() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        proguard.classfile.visitor.MemberVisitor fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be assignable to MemberVisitor");
    }

    /**
     * Tests the constructor with complex nested map structure.
     * Verifies that nested maps are handled correctly.
     */
    @Test
    public void testConstructorWithComplexNestedMap() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        Map<String, String> nameMap1 = new HashMap<>();
        nameMap1.put("a", "originalA");
        nameMap1.put("b", "originalB");
        Map<String, String> nameMap2 = new HashMap<>();
        nameMap2.put("x", "originalX");
        nameMap2.put("y", "originalY");
        descriptorMap.put("()V", nameMap1);
        descriptorMap.put("(I)V", nameMap2);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator memberObfuscator = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            memberObfuscator
        );

        // Assert
        assertNotNull(fixer, "MemberNameConflictFixer should be created with complex nested map");
    }

    /**
     * Tests creating fixers with different obfuscators.
     * Verifies that different obfuscator instances are accepted.
     */
    @Test
    public void testConstructorWithDifferentObfuscators() {
        // Arrange
        boolean allowAggressiveOverloading = true;
        Map<String, Map<String, String>> descriptorMap = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        MemberObfuscator obfuscator1 = mock(MemberObfuscator.class);
        MemberObfuscator obfuscator2 = mock(MemberObfuscator.class);

        // Act
        MemberNameConflictFixer fixer1 = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            obfuscator1
        );

        MemberNameConflictFixer fixer2 = new MemberNameConflictFixer(
            allowAggressiveOverloading,
            descriptorMap,
            warningPrinter,
            obfuscator2
        );

        // Assert
        assertNotNull(fixer1, "First fixer should be created");
        assertNotNull(fixer2, "Second fixer should be created");
        assertNotSame(fixer1, fixer2, "Fixer instances should be different");
    }
}
