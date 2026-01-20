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
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UniqueMemberNameFactory#reset()}.
 * Tests the reset method which delegates to the underlying NameFactory.
 */
public class UniqueMemberNameFactoryClaude_resetTest {

    /**
     * Tests that reset() resets the name sequence by delegating to the underlying factory.
     * Verifies that after reset, nextName returns the first name again.
     */
    @Test
    public void testResetResetsNameSequence() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        SimpleNameFactory delegateFactory = new SimpleNameFactory();
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(delegateFactory, programClass);

        // Act - Generate some names
        String firstName = factory.nextName();
        String secondName = factory.nextName();
        String thirdName = factory.nextName();

        // Reset and generate again
        factory.reset();
        String firstNameAfterReset = factory.nextName();
        String secondNameAfterReset = factory.nextName();
        String thirdNameAfterReset = factory.nextName();

        // Assert - Names should repeat after reset
        assertEquals(firstName, firstNameAfterReset, "First name after reset should match original first name");
        assertEquals(secondName, secondNameAfterReset, "Second name after reset should match original second name");
        assertEquals(thirdName, thirdNameAfterReset, "Third name after reset should match original third name");
    }

    /**
     * Tests that reset() delegates to the underlying delegate factory.
     * Verifies that the delegate's reset method is called.
     */
    @Test
    public void testResetDelegatesToUnderlyingFactory() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        NameFactory mockDelegate = mock(NameFactory.class);
        when(mockDelegate.nextName()).thenReturn("a", "b", "c");

        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(mockDelegate, programClass);

        // Act
        factory.reset();

        // Assert - Verify the delegate's reset was called
        verify(mockDelegate, times(1)).reset();
    }

    /**
     * Tests that reset() can be called multiple times consecutively.
     * Verifies that consecutive resets work correctly.
     */
    @Test
    public void testResetMultipleTimes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        SimpleNameFactory delegateFactory = new SimpleNameFactory();
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(delegateFactory, programClass);

        String firstName = factory.nextName();

        // Act - Reset multiple times
        factory.nextName();
        factory.reset();
        String afterFirst = factory.nextName();

        factory.nextName();
        factory.reset();
        String afterSecond = factory.nextName();

        factory.nextName();
        factory.reset();
        String afterThird = factory.nextName();

        // Assert - Each reset should restart the sequence
        assertEquals(firstName, afterFirst);
        assertEquals(firstName, afterSecond);
        assertEquals(firstName, afterThird);
    }

    /**
     * Tests that reset() immediately after construction works correctly.
     * Verifies that reset on a fresh factory doesn't cause issues.
     */
    @Test
    public void testResetImmediatelyAfterConstruction() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        SimpleNameFactory delegateFactory = new SimpleNameFactory();
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(delegateFactory, programClass);

        // Act
        factory.reset();
        String name = factory.nextName();

        // Assert
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }

    /**
     * Tests that reset() after generating many names works correctly.
     * Verifies that reset works even after a long sequence.
     */
    @Test
    public void testResetAfterManyNames() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        SimpleNameFactory delegateFactory = new SimpleNameFactory();
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(delegateFactory, programClass);

        String firstName = factory.nextName();

        // Generate many names
        for (int i = 0; i < 100; i++) {
            factory.nextName();
        }

        // Act
        factory.reset();
        String nameAfterReset = factory.nextName();

        // Assert
        assertEquals(firstName, nameAfterReset);
    }

    /**
     * Tests reset() with generation and reset cycles.
     * Verifies consistent behavior across multiple cycles.
     */
    @Test
    public void testResetWithGenerationResetCycles() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        SimpleNameFactory delegateFactory = new SimpleNameFactory();
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(delegateFactory, programClass);

        // Act & Assert - Multiple cycles
        for (int cycle = 0; cycle < 5; cycle++) {
            String name1 = factory.nextName();
            String name2 = factory.nextName();
            String name3 = factory.nextName();

            assertEquals("a", name1, "First name should be 'a' in cycle " + cycle);
            assertEquals("b", name2, "Second name should be 'b' in cycle " + cycle);
            assertEquals("c", name3, "Third name should be 'c' in cycle " + cycle);

            factory.reset();
        }
    }

    /**
     * Tests that reset() doesn't affect the class reference.
     * Verifies that reset only affects the name sequence, not the class.
     */
    @Test
    public void testResetDoesNotAffectClassReference() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        SimpleNameFactory delegateFactory = new SimpleNameFactory();
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(delegateFactory, programClass);

        // Act - Generate names, reset, generate again
        factory.nextName();
        factory.nextName();
        factory.reset();
        String nameAfterReset = factory.nextName();

        // Assert - Should still work and generate valid names
        assertNotNull(nameAfterReset);
        assertEquals("a", nameAfterReset);
    }

    /**
     * Tests reset() through the NameFactory interface.
     * Verifies polymorphic behavior works correctly.
     */
    @Test
    public void testResetThroughInterface() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        NameFactory factory = new UniqueMemberNameFactory(new SimpleNameFactory(), programClass);

        // Act
        String name1 = factory.nextName();
        factory.nextName();
        factory.reset();
        String nameAfterReset = factory.nextName();

        // Assert
        assertEquals(name1, nameAfterReset);
    }

    /**
     * Tests that reset() works with injected member name factory.
     * Verifies that reset works with the factory created by newInjectedMemberNameFactory.
     */
    @Test
    public void testResetWithInjectedMemberNameFactory() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        UniqueMemberNameFactory factory = UniqueMemberNameFactory.newInjectedMemberNameFactory(programClass);

        // Act - Generate some names
        String firstName = factory.nextName();
        factory.nextName();
        factory.nextName();

        // Reset
        factory.reset();
        String firstNameAfterReset = factory.nextName();

        // Assert
        assertEquals(firstName, firstNameAfterReset);
        assertTrue(firstName.startsWith("$$"), "Injected member name should start with $$");
    }

    /**
     * Tests that reset() doesn't throw any exceptions.
     * Verifies that reset is safe to call.
     */
    @Test
    public void testResetDoesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(new SimpleNameFactory(), programClass);

        // Act & Assert
        assertDoesNotThrow(() -> factory.reset());
    }

    /**
     * Tests reset() with PrefixingNameFactory as delegate.
     * Verifies that reset works with different delegate implementations.
     */
    @Test
    public void testResetWithPrefixingNameFactoryDelegate() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        PrefixingNameFactory prefixingFactory = new PrefixingNameFactory(new SimpleNameFactory(), "prefix_");
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(prefixingFactory, programClass);

        // Act
        String firstName = factory.nextName();
        factory.nextName();
        factory.nextName();

        factory.reset();
        String firstNameAfterReset = factory.nextName();

        // Assert
        assertEquals(firstName, firstNameAfterReset);
        assertTrue(firstName.startsWith("prefix_"));
    }

    /**
     * Tests that reset() works correctly with multiple independent instances.
     * Verifies that each instance maintains independent state.
     */
    @Test
    public void testResetWithMultipleInstances() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        programClass1.u2constantPoolCount = 1;
        programClass1.constantPool = new proguard.classfile.constant.Constant[1];

        ProgramClass programClass2 = new ProgramClass();
        programClass2.u2constantPoolCount = 1;
        programClass2.constantPool = new proguard.classfile.constant.Constant[1];

        UniqueMemberNameFactory factory1 = new UniqueMemberNameFactory(new SimpleNameFactory(), programClass1);
        UniqueMemberNameFactory factory2 = new UniqueMemberNameFactory(new SimpleNameFactory(), programClass2);

        // Act - Advance factory1, reset factory2
        factory1.nextName();
        factory1.nextName();
        String factory1Name = factory1.nextName(); // Should be "c"

        factory2.nextName();
        factory2.reset();
        String factory2Name = factory2.nextName(); // Should be "a"

        // Assert - Factories should be independent
        assertEquals("c", factory1Name);
        assertEquals("a", factory2Name);
    }

    /**
     * Tests that reset() allows reuse of names that were previously generated.
     * Verifies that the same sequence of names can be regenerated after reset.
     */
    @Test
    public void testResetAllowsNameReuse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new proguard.classfile.constant.Constant[1];

        SimpleNameFactory delegateFactory = new SimpleNameFactory();
        UniqueMemberNameFactory factory = new UniqueMemberNameFactory(delegateFactory, programClass);

        // Act - Generate a sequence
        String[] firstSequence = new String[10];
        for (int i = 0; i < 10; i++) {
            firstSequence[i] = factory.nextName();
        }

        // Reset and generate again
        factory.reset();
        String[] secondSequence = new String[10];
        for (int i = 0; i < 10; i++) {
            secondSequence[i] = factory.nextName();
        }

        // Assert - Both sequences should be identical
        assertArrayEquals(firstSequence, secondSequence);
    }
}
