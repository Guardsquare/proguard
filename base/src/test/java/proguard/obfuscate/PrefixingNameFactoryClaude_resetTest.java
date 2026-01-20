package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PrefixingNameFactory.reset() method.
 * Tests that the method properly resets the delegate NameFactory.
 */
public class PrefixingNameFactoryClaude_resetTest {

    /**
     * Test that reset allows name generation to start from the beginning.
     */
    @Test
    public void testResetStartsFromBeginning() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "prefix_");

        String name1 = factory.nextName();
        String name2 = factory.nextName();

        assertEquals("prefix_a", name1);
        assertEquals("prefix_b", name2);

        factory.reset();

        String name3 = factory.nextName();
        assertEquals("prefix_a", name3,
            "After reset, nextName should start from the beginning");
    }

    /**
     * Test that reset can be called immediately after construction.
     */
    @Test
    public void testResetImmediatelyAfterConstruction() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "test_");

        assertDoesNotThrow(() -> factory.reset(),
            "reset should work immediately after construction");

        String name = factory.nextName();
        assertEquals("test_a", name,
            "After reset on fresh factory, should start with first name");
    }

    /**
     * Test that reset works after generating many names.
     */
    @Test
    public void testResetAfterManyNames() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "many_");

        // Generate many names
        for (int i = 0; i < 100; i++) {
            factory.nextName();
        }

        factory.reset();

        String name = factory.nextName();
        assertEquals("many_a", name,
            "After reset, should start from beginning even after many generations");
    }

    /**
     * Test that multiple resets work correctly.
     */
    @Test
    public void testMultipleResets() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "multi_");

        String name1 = factory.nextName();
        assertEquals("multi_a", name1);

        factory.reset();
        String name2 = factory.nextName();
        assertEquals("multi_a", name2);

        factory.reset();
        String name3 = factory.nextName();
        assertEquals("multi_a", name3,
            "Multiple resets should each start from beginning");
    }

    /**
     * Test that reset works correctly in a cycle of generate and reset.
     */
    @Test
    public void testResetInGenerationCycle() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "cycle_");

        String name1 = factory.nextName();
        String name2 = factory.nextName();
        assertEquals("cycle_a", name1);
        assertEquals("cycle_b", name2);

        factory.reset();
        String name3 = factory.nextName();
        assertEquals("cycle_a", name3);

        String name4 = factory.nextName();
        assertEquals("cycle_b", name4);

        factory.reset();
        String name5 = factory.nextName();
        assertEquals("cycle_a", name5,
            "Should be able to cycle through generate and reset multiple times");
    }

    /**
     * Test that reset doesn't affect the prefix.
     */
    @Test
    public void testResetDoesNotAffectPrefix() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "unchanged_");

        String name1 = factory.nextName();
        assertTrue(name1.startsWith("unchanged_"));

        factory.reset();

        String name2 = factory.nextName();
        assertTrue(name2.startsWith("unchanged_"),
            "Prefix should remain unchanged after reset");
        assertEquals("unchanged_a", name2);
    }

    /**
     * Test that reset on one instance doesn't affect another with shared delegate.
     */
    @Test
    public void testResetWithSharedDelegate() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory1 = new PrefixingNameFactory(delegate, "first_");
        PrefixingNameFactory factory2 = new PrefixingNameFactory(delegate, "second_");

        String name1 = factory1.nextName();
        assertEquals("first_a", name1);

        String name2 = factory2.nextName();
        assertEquals("second_b", name2); // delegate is shared, so it's at 'b'

        // Reset through factory1
        factory1.reset();

        String name3 = factory1.nextName();
        assertEquals("first_a", name3,
            "After reset, should start from beginning");

        String name4 = factory2.nextName();
        assertEquals("second_b", name4,
            "Shared delegate was reset, so continues from reset point");
    }

    /**
     * Test that reset with independent delegates works correctly.
     */
    @Test
    public void testResetWithIndependentDelegates() {
        NameFactory delegate1 = new SimpleNameFactory();
        NameFactory delegate2 = new SimpleNameFactory();
        PrefixingNameFactory factory1 = new PrefixingNameFactory(delegate1, "indie1_");
        PrefixingNameFactory factory2 = new PrefixingNameFactory(delegate2, "indie2_");

        factory1.nextName();
        factory1.nextName();
        factory2.nextName();

        factory1.reset();

        String name1 = factory1.nextName();
        assertEquals("indie1_a", name1,
            "Factory1 should reset to beginning");

        String name2 = factory2.nextName();
        assertEquals("indie2_b", name2,
            "Factory2 should not be affected by factory1's reset");
    }

    /**
     * Test that consecutive resets work correctly.
     */
    @Test
    public void testConsecutiveResets() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "consec_");

        factory.nextName();
        factory.nextName();

        factory.reset();
        factory.reset();
        factory.reset();

        String name = factory.nextName();
        assertEquals("consec_a", name,
            "Multiple consecutive resets should still work correctly");
    }

    /**
     * Test that reset followed by immediate nextName works.
     */
    @Test
    public void testResetFollowedByNextName() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "follow_");

        factory.nextName();
        factory.nextName();
        factory.nextName();

        factory.reset();
        String name = factory.nextName();

        assertEquals("follow_a", name,
            "Should get first name immediately after reset");
    }

    /**
     * Test that reset works with empty prefix.
     */
    @Test
    public void testResetWithEmptyPrefix() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "");

        String name1 = factory.nextName();
        String name2 = factory.nextName();

        factory.reset();

        String name3 = factory.nextName();
        assertEquals(name1, name3,
            "After reset with empty prefix, should get same first name");
    }

    /**
     * Test that reset maintains correct sequence after reset.
     */
    @Test
    public void testResetMaintainsSequence() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "seq_");

        String name1 = factory.nextName();
        String name2 = factory.nextName();
        String name3 = factory.nextName();

        factory.reset();

        String name4 = factory.nextName();
        String name5 = factory.nextName();
        String name6 = factory.nextName();

        assertEquals(name1, name4, "First sequence should match after reset");
        assertEquals(name2, name5, "Second sequence should match after reset");
        assertEquals(name3, name6, "Third sequence should match after reset");
    }

    /**
     * Test that reset doesn't throw exceptions.
     */
    @Test
    public void testResetDoesNotThrow() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "nothrow_");

        assertDoesNotThrow(() -> factory.reset(),
            "reset should not throw exceptions");
    }

    /**
     * Test that reset after single nextName works.
     */
    @Test
    public void testResetAfterSingleNextName() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "single_");

        String name1 = factory.nextName();
        assertEquals("single_a", name1);

        factory.reset();

        String name2 = factory.nextName();
        assertEquals("single_a", name2,
            "After reset following single nextName, should start from beginning");
    }

    /**
     * Test that reset works correctly with different prefix types.
     */
    @Test
    public void testResetWithDifferentPrefixTypes() {
        NameFactory delegate1 = new SimpleNameFactory();
        NameFactory delegate2 = new SimpleNameFactory();
        NameFactory delegate3 = new SimpleNameFactory();

        PrefixingNameFactory factory1 = new PrefixingNameFactory(delegate1, "_");
        PrefixingNameFactory factory2 = new PrefixingNameFactory(delegate2, "MyPrefix");
        PrefixingNameFactory factory3 = new PrefixingNameFactory(delegate3, "123_");

        factory1.nextName();
        factory2.nextName();
        factory3.nextName();

        factory1.reset();
        factory2.reset();
        factory3.reset();

        assertEquals("_a", factory1.nextName());
        assertEquals("MyPrefixa", factory2.nextName());
        assertEquals("123_a", factory3.nextName());
    }

    /**
     * Test that reset works with lower-case only delegate.
     */
    @Test
    public void testResetWithLowerCaseDelegate() {
        NameFactory delegate = new SimpleNameFactory(false);
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "lower_");

        factory.nextName();
        factory.nextName();

        factory.reset();

        String name = factory.nextName();
        assertEquals("lower_a", name,
            "Reset should work with lower-case only delegate");
    }

    /**
     * Test that reset allows generating same sequence multiple times.
     */
    @Test
    public void testResetAllowsRepeatingSequence() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "repeat_");

        String[] firstRun = new String[5];
        for (int i = 0; i < 5; i++) {
            firstRun[i] = factory.nextName();
        }

        factory.reset();

        String[] secondRun = new String[5];
        for (int i = 0; i < 5; i++) {
            secondRun[i] = factory.nextName();
        }

        assertArrayEquals(firstRun, secondRun,
            "After reset, should generate same sequence of names");
    }

    /**
     * Test that reset works correctly after partial sequence generation.
     */
    @Test
    public void testResetAfterPartialSequence() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "partial_");

        factory.nextName();
        factory.nextName();
        factory.nextName();

        factory.reset();

        String name1 = factory.nextName();
        String name2 = factory.nextName();

        assertEquals("partial_a", name1);
        assertEquals("partial_b", name2);
    }

    /**
     * Test that reset is idempotent when called without nextName in between.
     */
    @Test
    public void testResetIsIdempotent() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "idem_");

        factory.nextName();

        factory.reset();
        String name1 = factory.nextName();

        factory.nextName();

        factory.reset();
        factory.reset();
        String name2 = factory.nextName();

        assertEquals(name1, name2,
            "Multiple resets should produce same result");
    }

    /**
     * Test that reset works in alternating pattern with nextName.
     */
    @Test
    public void testResetAlternatingWithNextName() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "alt_");

        String name1 = factory.nextName();
        factory.reset();
        String name2 = factory.nextName();
        factory.reset();
        String name3 = factory.nextName();

        assertEquals("alt_a", name1);
        assertEquals("alt_a", name2);
        assertEquals("alt_a", name3,
            "Alternating reset and nextName should keep returning first name");
    }

    /**
     * Test that reset handles state correctly after long generation.
     */
    @Test
    public void testResetAfterLongGeneration() {
        NameFactory delegate = new SimpleNameFactory();
        PrefixingNameFactory factory = new PrefixingNameFactory(delegate, "long_");

        String firstNameBeforeReset = factory.nextName();

        // Generate many names
        for (int i = 0; i < 500; i++) {
            factory.nextName();
        }

        factory.reset();

        String firstNameAfterReset = factory.nextName();

        assertEquals(firstNameBeforeReset, firstNameAfterReset,
            "After long generation and reset, should match initial name");
    }
}
