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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SpecialNameFactory#reset()}.
 * Tests the reset method which delegates to the wrapped NameFactory.
 */
public class SpecialNameFactoryClaude_resetTest {

    /**
     * Tests that reset() delegates to the wrapped name factory.
     * Verifies that after reset, the name sequence restarts from the beginning.
     */
    @Test
    public void testResetDelegatesToWrappedFactory() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Get first few names with special suffix
        String firstName = specialFactory.nextName();
        String secondName = specialFactory.nextName();
        String thirdName = specialFactory.nextName();

        // Expected first names are "a_", "b_", "c_"
        assertEquals("a_", firstName, "First name should be 'a_'");
        assertEquals("b_", secondName, "Second name should be 'b_'");
        assertEquals("c_", thirdName, "Third name should be 'c_'");

        // Reset
        specialFactory.reset();

        // Act - Get names after reset
        String firstNameAfterReset = specialFactory.nextName();
        String secondNameAfterReset = specialFactory.nextName();
        String thirdNameAfterReset = specialFactory.nextName();

        // Assert - Names should restart from the beginning
        assertEquals(firstName, firstNameAfterReset,
                "After reset, first name should match original first name");
        assertEquals(secondName, secondNameAfterReset,
                "After reset, second name should match original second name");
        assertEquals(thirdName, thirdNameAfterReset,
                "After reset, third name should match original third name");
    }

    /**
     * Tests reset() after generating many names.
     * Verifies that reset works correctly even after a long sequence.
     */
    @Test
    public void testResetAfterManyNames() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Get first name
        String firstName = specialFactory.nextName();

        // Generate many names
        for (int i = 0; i < 500; i++) {
            specialFactory.nextName();
        }

        // Reset
        specialFactory.reset();

        // Get name after reset
        String nameAfterReset = specialFactory.nextName();

        // Assert - Should get the first name again
        assertEquals(firstName, nameAfterReset,
                "After reset (even after many names), should get first name again");
    }

    /**
     * Tests reset() multiple times in succession.
     * Verifies that consecutive resets work correctly.
     */
    @Test
    public void testResetMultipleTimes() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Get first name
        String firstName = specialFactory.nextName();

        // Reset and check multiple times
        for (int i = 0; i < 5; i++) {
            specialFactory.nextName();
            specialFactory.nextName();
            specialFactory.reset();
            String nameAfterReset = specialFactory.nextName();
            assertEquals(firstName, nameAfterReset,
                    "After reset #" + (i + 1) + ", should get first name");
        }
    }

    /**
     * Tests reset() immediately after construction.
     * Verifies that reset on a fresh factory doesn't cause issues.
     */
    @Test
    public void testResetImmediatelyAfterConstruction() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Reset before generating any names
        specialFactory.reset();
        String name = specialFactory.nextName();

        // Assert - Should work without issues
        assertNotNull(name, "Name should not be null");
        assertFalse(name.isEmpty(), "Name should not be empty");
        assertTrue(name.endsWith("_"), "Name should end with special suffix '_'");
    }

    /**
     * Tests that reset() preserves the special suffix behavior.
     * Verifies that names after reset still have the special suffix.
     */
    @Test
    public void testResetPreservesSpecialSuffix() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate some names
        String name1 = specialFactory.nextName();
        String name2 = specialFactory.nextName();

        // Reset
        specialFactory.reset();

        // Generate more names
        String name3 = specialFactory.nextName();
        String name4 = specialFactory.nextName();

        // Assert - All names should have the special suffix
        assertTrue(name1.endsWith("_"), "Name before reset should have suffix");
        assertTrue(name2.endsWith("_"), "Name before reset should have suffix");
        assertTrue(name3.endsWith("_"), "Name after reset should have suffix");
        assertTrue(name4.endsWith("_"), "Name after reset should have suffix");
    }

    /**
     * Tests reset() with mixed-case name factory.
     * Verifies that reset works correctly with different configurations.
     */
    @Test
    public void testResetWithMixedCaseFactory() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory(true);
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate names and collect sequence
        String[] originalSequence = new String[10];
        for (int i = 0; i < 10; i++) {
            originalSequence[i] = specialFactory.nextName();
        }

        // Reset
        specialFactory.reset();

        // Generate names again
        String[] resetSequence = new String[10];
        for (int i = 0; i < 10; i++) {
            resetSequence[i] = specialFactory.nextName();
        }

        // Assert - Both sequences should be identical
        assertArrayEquals(originalSequence, resetSequence,
                "Sequences before and after reset should be identical");
    }

    /**
     * Tests reset() with lower-case only name factory.
     * Verifies that reset works correctly with lower-case configuration.
     */
    @Test
    public void testResetWithLowerCaseFactory() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory(false);
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate names
        String[] originalSequence = new String[10];
        for (int i = 0; i < 10; i++) {
            originalSequence[i] = specialFactory.nextName();
        }

        // Reset
        specialFactory.reset();

        // Generate names again
        String[] resetSequence = new String[10];
        for (int i = 0; i < 10; i++) {
            resetSequence[i] = specialFactory.nextName();
        }

        // Assert - Both sequences should be identical
        assertArrayEquals(originalSequence, resetSequence,
                "Sequences before and after reset should be identical");
    }

    /**
     * Tests reset() in a practical scenario.
     * Verifies that reset allows reusing the factory for multiple obfuscation passes.
     */
    @Test
    public void testResetInPracticalScenario() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Simulate first obfuscation pass
        String firstPassName1 = specialFactory.nextName();
        String firstPassName2 = specialFactory.nextName();
        String firstPassName3 = specialFactory.nextName();

        // Reset for second pass
        specialFactory.reset();

        // Simulate second obfuscation pass
        String secondPassName1 = specialFactory.nextName();
        String secondPassName2 = specialFactory.nextName();
        String secondPassName3 = specialFactory.nextName();

        // Assert - Second pass should produce same names as first pass
        assertEquals(firstPassName1, secondPassName1,
                "Second pass name 1 should match first pass name 1");
        assertEquals(firstPassName2, secondPassName2,
                "Second pass name 2 should match first pass name 2");
        assertEquals(firstPassName3, secondPassName3,
                "Second pass name 3 should match first pass name 3");
    }

    /**
     * Tests that reset() doesn't affect the wrapped factory configuration.
     * Verifies that the factory continues to work with the same configuration after reset.
     */
    @Test
    public void testResetDoesNotAffectConfiguration() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory(true);
        SpecialNameFactory specialFactory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate enough names to see both upper and lower case
        for (int i = 0; i < 30; i++) {
            specialFactory.nextName();
        }

        // Reset
        specialFactory.reset();

        // Generate more names after reset
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        for (int i = 0; i < 30; i++) {
            String name = specialFactory.nextName();
            // Remove the underscore suffix to check the base name
            String baseName = name.substring(0, name.length() - 1);
            for (char c : baseName.toCharArray()) {
                if (Character.isLowerCase(c)) hasLowerCase = true;
                if (Character.isUpperCase(c)) hasUpperCase = true;
            }
        }

        // Assert - Should still generate mixed-case names after reset
        assertTrue(hasLowerCase, "Should still generate lower-case after reset");
        assertTrue(hasUpperCase, "Should still generate upper-case after reset (mixed-case mode)");
    }

    /**
     * Tests reset() through the NameFactory interface.
     * Verifies that polymorphic behavior works correctly.
     */
    @Test
    public void testResetThroughInterface() {
        // Arrange
        SimpleNameFactory wrappedFactory = new SimpleNameFactory();
        NameFactory factory = new SpecialNameFactory(wrappedFactory);

        // Act - Generate names
        String firstName = factory.nextName();
        factory.nextName();
        factory.nextName();

        // Reset through interface
        factory.reset();
        String nameAfterReset = factory.nextName();

        // Assert
        assertEquals(firstName, nameAfterReset,
                "Reset through interface should work correctly");
    }
}
