/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SpecialNameFactory#nextName()}.
 * Tests the nextName method which delegates to the wrapped NameFactory and appends a special suffix.
 */
public class SpecialNameFactoryClaude_nextNameTest {

    private static final char SPECIAL_SUFFIX = '_';

    /**
     * Tests that nextName() appends the special suffix to names from wrapped factory.
     * Verifies the basic behavior of adding underscore suffix.
     */
    @Test
    public void testNextNameAppendsSpecialSuffix() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act
        String name = specialFactory.nextName();

        // Assert
        assertNotNull(name, "nextName should not return null");
        assertTrue(name.endsWith("_"), "Name should end with special suffix '_'");
        assertEquals("a_", name, "First name should be 'a_'");
    }

    /**
     * Tests that nextName() returns non-null values.
     * Verifies the method never returns null.
     */
    @Test
    public void testNextNameReturnsNonNull() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - Generate multiple names
        for (int i = 0; i < 50; i++) {
            String name = specialFactory.nextName();
            assertNotNull(name, "nextName should never return null");
        }
    }

    /**
     * Tests that nextName() returns non-empty strings.
     * Verifies all generated names have content.
     */
    @Test
    public void testNextNameReturnsNonEmpty() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert
        for (int i = 0; i < 50; i++) {
            String name = specialFactory.nextName();
            assertFalse(name.isEmpty(), "nextName should never return empty string");
            assertTrue(name.length() > 1, "Name should have at least base name + suffix");
        }
    }

    /**
     * Tests that nextName() generates a sequence of names.
     * Verifies the expected sequence: a_, b_, c_, etc.
     */
    @Test
    public void testNextNameSequence() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - Check first few names
        assertEquals("a_", specialFactory.nextName());
        assertEquals("b_", specialFactory.nextName());
        assertEquals("c_", specialFactory.nextName());
        assertEquals("d_", specialFactory.nextName());
        assertEquals("e_", specialFactory.nextName());
    }

    /**
     * Tests that nextName() generates unique names.
     * Verifies each call returns a different name.
     */
    @Test
    public void testNextNameGeneratesUniqueNames() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);
        Set<String> generatedNames = new HashSet<>();

        // Act - Generate many names
        for (int i = 0; i < 200; i++) {
            String name = specialFactory.nextName();
            generatedNames.add(name);
        }

        // Assert - All names should be unique
        assertEquals(200, generatedNames.size(),
                "All 200 generated names should be unique");
    }

    /**
     * Tests that all generated names have the special suffix.
     * Verifies the suffix is consistently applied.
     */
    @Test
    public void testNextNameAlwaysHasSpecialSuffix() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - Generate many names
        for (int i = 0; i < 100; i++) {
            String name = specialFactory.nextName();
            assertTrue(name.endsWith("_"),
                    "Name '" + name + "' should end with special suffix '_'");
            assertEquals('_', name.charAt(name.length() - 1),
                    "Last character should be underscore");
        }
    }

    /**
     * Tests that nextName() works with mixed-case delegate factory.
     * Verifies correct behavior with mixed-case name generation.
     */
    @Test
    public void testNextNameWithMixedCaseDelegate() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory(true);
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate first few names
        assertEquals("a_", specialFactory.nextName());
        assertEquals("b_", specialFactory.nextName());

        // Generate enough to get to uppercase
        for (int i = 2; i < 26; i++) {
            specialFactory.nextName();
        }

        // Assert - 27th name should be "A_"
        assertEquals("A_", specialFactory.nextName());
    }

    /**
     * Tests that nextName() works with lower-case only delegate factory.
     * Verifies correct behavior with lower-case configuration.
     */
    @Test
    public void testNextNameWithLowerCaseDelegate() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory(false);
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate names and check they're all lowercase (plus suffix)
        for (int i = 0; i < 50; i++) {
            String name = specialFactory.nextName();
            // Remove the suffix to check base name
            String baseName = name.substring(0, name.length() - 1);
            assertEquals(baseName.toLowerCase(), baseName,
                    "Base name should be lowercase, but got: " + name);
        }
    }

    /**
     * Tests nextName() progression to longer names.
     * Verifies that names get longer as the sequence progresses.
     */
    @Test
    public void testNextNameProgression() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory(true);
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate 52 single-char base names (a-z, A-Z) + suffix
        for (int i = 0; i < 52; i++) {
            String name = specialFactory.nextName();
            assertEquals(2, name.length(),
                    "First 52 names should have length 2 (1 char + suffix)");
        }

        // Act - Next names should have 2-char base + suffix
        String name53 = specialFactory.nextName();
        assertEquals(3, name53.length(),
                "After 52 names, should have length 3 (2 chars + suffix)");
        assertEquals("aa_", name53);
    }

    /**
     * Tests that nextName() delegates to the wrapped factory.
     * Verifies delegation by checking the base names match.
     */
    @Test
    public void testNextNameDelegatesToWrappedFactory() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Create a second wrapped factory to compare
        SimpleNameFactory referenceFactory = new SimpleNameFactory();

        // Act & Assert - Compare sequences
        for (int i = 0; i < 30; i++) {
            String specialName = specialFactory.nextName();
            String referenceName = referenceFactory.nextName();

            // Remove suffix and compare
            String baseName = specialName.substring(0, specialName.length() - 1);
            assertEquals(referenceName, baseName,
                    "Base name should match wrapped factory output");
        }
    }

    /**
     * Tests nextName() after reset.
     * Verifies that sequence restarts correctly after reset.
     */
    @Test
    public void testNextNameAfterReset() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate names
        String firstName = specialFactory.nextName();
        String secondName = specialFactory.nextName();
        String thirdName = specialFactory.nextName();

        // Reset
        specialFactory.reset();

        // Generate names again
        String firstNameAfterReset = specialFactory.nextName();
        String secondNameAfterReset = specialFactory.nextName();
        String thirdNameAfterReset = specialFactory.nextName();

        // Assert
        assertEquals(firstName, firstNameAfterReset);
        assertEquals(secondName, secondNameAfterReset);
        assertEquals(thirdName, thirdNameAfterReset);
    }

    /**
     * Tests nextName() through the NameFactory interface.
     * Verifies polymorphic behavior.
     */
    @Test
    public void testNextNameThroughInterface() {
        // Arrange
        NameFactory factory = new SpecialNameFactory(new SimpleNameFactory());

        // Act
        String name1 = factory.nextName();
        String name2 = factory.nextName();

        // Assert
        assertNotNull(name1);
        assertNotNull(name2);
        assertNotEquals(name1, name2);
        assertTrue(name1.endsWith("_"));
        assertTrue(name2.endsWith("_"));
    }

    /**
     * Tests nextName() with multiple independent instances.
     * Verifies that instances maintain separate state.
     */
    @Test
    public void testNextNameWithMultipleInstances() {
        // Arrange
        SpecialNameFactory factory1 = new SpecialNameFactory(new SimpleNameFactory());
        SpecialNameFactory factory2 = new SpecialNameFactory(new SimpleNameFactory());

        // Act
        String name1_1 = factory1.nextName();
        String name2_1 = factory2.nextName();
        String name1_2 = factory1.nextName();
        String name2_2 = factory2.nextName();

        // Assert - Both should generate same sequence independently
        assertEquals(name1_1, name2_1, "Both factories should generate same first name");
        assertEquals(name1_2, name2_2, "Both factories should generate same second name");
        assertEquals("a_", name1_1);
        assertEquals("b_", name1_2);
    }

    /**
     * Tests nextName() generates valid identifiers.
     * Verifies names are suitable as Java identifiers.
     */
    @Test
    public void testNextNameGeneratesValidIdentifiers() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - Generate many names
        for (int i = 0; i < 200; i++) {
            String name = specialFactory.nextName();
            // Check format: letters + underscore
            assertTrue(name.matches("[a-zA-Z]+_"),
                    "Name '" + name + "' should be letters followed by underscore");
        }
    }

    /**
     * Tests that nextName() can be called many times without error.
     * Verifies stability over long sequences.
     */
    @Test
    public void testNextNameManyGenerations() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - Generate many names without exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                String name = specialFactory.nextName();
                assertNotNull(name);
                assertTrue(name.endsWith("_"));
            }
        });
    }

    /**
     * Tests nextName() consistency with the same delegate.
     * Verifies deterministic behavior.
     */
    @Test
    public void testNextNameDeterministic() {
        // Arrange
        SpecialNameFactory factory1 = new SpecialNameFactory(new SimpleNameFactory(true));
        SpecialNameFactory factory2 = new SpecialNameFactory(new SimpleNameFactory(true));

        // Act - Generate sequences from both
        String[] sequence1 = new String[50];
        String[] sequence2 = new String[50];
        for (int i = 0; i < 50; i++) {
            sequence1[i] = factory1.nextName();
            sequence2[i] = factory2.nextName();
        }

        // Assert - Both sequences should be identical
        assertArrayEquals(sequence1, sequence2,
                "Same configuration should produce identical sequences");
    }

    /**
     * Tests that nextName() preserves the wrapped factory's behavior.
     * Verifies that special names follow the same pattern as base names.
     */
    @Test
    public void testNextNamePreservesWrappedBehavior() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory(false);
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Skip to where two-letter names start (after 'z')
        for (int i = 0; i < 26; i++) {
            specialFactory.nextName();
        }

        // Assert - 27th name should be "aa_"
        String name27 = specialFactory.nextName();
        assertEquals("aa_", name27,
                "Should follow wrapped factory pattern: aa + suffix");
    }

    /**
     * Tests nextName() with consecutive calls.
     * Verifies that names increment properly.
     */
    @Test
    public void testNextNameConsecutiveCalls() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert
        String prev = specialFactory.nextName();
        for (int i = 0; i < 50; i++) {
            String current = specialFactory.nextName();
            assertNotEquals(prev, current,
                    "Consecutive calls should return different names");
            prev = current;
        }
    }

    /**
     * Tests that nextName() creates names recognizable by isSpecialName.
     * Verifies integration with the utility method.
     */
    @Test
    public void testNextNameCreatesSpecialNames() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - All generated names should be special
        for (int i = 0; i < 50; i++) {
            String name = specialFactory.nextName();
            assertTrue(SpecialNameFactory.isSpecialName(name),
                    "Name '" + name + "' should be recognized as special");
        }
    }

    /**
     * Tests nextName() first name is always "a_".
     * Verifies the starting point of the sequence.
     */
    @Test
    public void testNextNameFirstNameIsA() {
        // Arrange
        SpecialNameFactory factory1 = new SpecialNameFactory(new SimpleNameFactory(true));
        SpecialNameFactory factory2 = new SpecialNameFactory(new SimpleNameFactory(false));

        // Act
        String firstMixed = factory1.nextName();
        String firstLower = factory2.nextName();

        // Assert
        assertEquals("a_", firstMixed, "First name with mixed-case should be 'a_'");
        assertEquals("a_", firstLower, "First name with lower-case should be 'a_'");
    }

    /**
     * Tests nextName() with shared delegate factory.
     * Verifies behavior when multiple SpecialNameFactory instances share a delegate.
     */
    @Test
    public void testNextNameWithSharedDelegate() {
        // Arrange
        SimpleNameFactory sharedDelegate = new SimpleNameFactory();
        SpecialNameFactory factory1 = new SpecialNameFactory(sharedDelegate);
        SpecialNameFactory factory2 = new SpecialNameFactory(sharedDelegate);

        // Act
        String name1 = factory1.nextName();
        String name2 = factory2.nextName();
        String name3 = factory1.nextName();

        // Assert - They share state through the delegate
        assertEquals("a_", name1);
        assertEquals("b_", name2);  // Continues from where factory1 left off
        assertEquals("c_", name3);
    }

    /**
     * Tests nextName() doesn't modify the suffix character.
     * Verifies the suffix is always underscore.
     */
    @Test
    public void testNextNameSuffixIsAlwaysUnderscore() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            String name = specialFactory.nextName();
            char lastChar = name.charAt(name.length() - 1);
            assertEquals('_', lastChar,
                    "Last character should always be underscore");
        }
    }

    /**
     * Tests nextName() with cycle of generation and reset.
     * Verifies consistent behavior across multiple cycles.
     */
    @Test
    public void testNextNameWithGenerationResetCycles() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - Multiple cycles
        for (int cycle = 0; cycle < 5; cycle++) {
            assertEquals("a_", specialFactory.nextName());
            assertEquals("b_", specialFactory.nextName());
            assertEquals("c_", specialFactory.nextName());
            specialFactory.reset();
        }
    }

    /**
     * Tests nextName() generates at least minimum expected names.
     * Verifies the factory can generate a reasonable number of names.
     */
    @Test
    public void testNextNameGeneratesMinimumExpectedNames() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);
        Set<String> names = new HashSet<>();

        // Act - Generate 500 names
        for (int i = 0; i < 500; i++) {
            names.add(specialFactory.nextName());
        }

        // Assert - All should be unique
        assertEquals(500, names.size(),
                "Should generate at least 500 unique names");
    }

    /**
     * Tests that nextName() immediately after construction works.
     * Verifies no initialization issues.
     */
    @Test
    public void testNextNameImmediatelyAfterConstruction() {
        // Arrange & Act
        SpecialNameFactory specialFactory = new SpecialNameFactory(new SimpleNameFactory());
        String name = specialFactory.nextName();

        // Assert
        assertNotNull(name);
        assertEquals("a_", name);
    }

    /**
     * Tests nextName() produces names compatible with obfuscation.
     * Verifies names are suitable for their intended purpose.
     */
    @Test
    public void testNextNameProducesObfuscationSuitableNames() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act & Assert - Check names are short and valid
        for (int i = 0; i < 100; i++) {
            String name = specialFactory.nextName();
            assertTrue(name.length() <= 10,
                    "Name '" + name + "' should be reasonably short for obfuscation");
            assertTrue(Character.isLetter(name.charAt(0)),
                    "Name should start with a letter");
        }
    }
}
