package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setNote method.
 *
 * This test class verifies the behavior of the setNote(boolean) method
 * which controls whether ProGuard prints notes about potential issues.
 *
 * The method has special behavior:
 * - setNote(true): Enables notes by setting configuration.note to null (if it was an empty list)
 * - setNote(false): Disables notes by setting configuration.note to an empty ArrayList
 *
 * Default value is null (notes are enabled by default).
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setNoteTest {

    @Test
    void testSetNoteFalseCreatesEmptyList() {
        ProGuardTask task = new ProGuardTask();
        task.setNote(false);

        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testSetNoteTrueFromEmptyListSetsNull() {
        ProGuardTask task = new ProGuardTask();

        // First disable notes (creates empty list)
        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());

        // Then enable notes (should set to null)
        task.setNote(true);
        assertNull(task.configuration.note);
    }

    @Test
    void testSetNoteTrueFromNullDoesNothing() {
        ProGuardTask task = new ProGuardTask();

        // Default is null
        assertNull(task.configuration.note);

        // Setting to true when already null should keep it null
        task.setNote(true);
        assertNull(task.configuration.note);
    }

    @Test
    void testDefaultValueIsNull() {
        ProGuardTask task = new ProGuardTask();
        assertNull(task.configuration.note);
    }

    @Test
    void testSetNoteFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());

        task.setNote(true);
        assertNull(task.configuration.note);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());

        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());

        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();

        // Start with false to create empty list
        task.setNote(false);

        task.setNote(true);
        assertNull(task.configuration.note);

        task.setNote(true);
        assertNull(task.configuration.note);

        task.setNote(true);
        assertNull(task.configuration.note);
    }

    @Test
    void testSetNoteWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);

        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setNote(false);
        task2.setNote(true);

        assertNotNull(task1.configuration.note);
        assertTrue(task1.configuration.note.isEmpty());
        assertNull(task2.configuration.note);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setNote(false);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setNote(false);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testToggleNoteMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());

        task.setNote(true);
        assertNull(task.configuration.note);

        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());

        task.setNote(true);
        assertNull(task.configuration.note);
    }

    @Test
    void testSetNoteFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setNote(false);

        assertTrue(task.configuration.obfuscate);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testSetNoteTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setNote(false);
        task.setNote(true);

        assertTrue(task.configuration.obfuscate);
        assertNull(task.configuration.note);
    }

    @Test
    void testSetNoteFalseWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setNote(false);

        assertFalse(task.configuration.obfuscate);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testSetNoteBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setNote(false);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setNote(false);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetNoteFalseWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setNote(false);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testSetNoteTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setNote(false);
        task.setNote(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertNull(task.configuration.note);
    }

    @Test
    void testSetNoteFalseOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is null
        assertNull(task.configuration.note);

        // Override to false (empty list)
        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testSetNoteTrueKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is null
        assertNull(task.configuration.note);

        // Set to true explicitly (should stay null)
        task.setNote(true);
        assertNull(task.configuration.note);
    }

    @Test
    void testSetNoteFalseCreatesNewListEachTime() {
        ProGuardTask task = new ProGuardTask();

        task.setNote(false);
        Object firstList = task.configuration.note;
        assertNotNull(firstList);

        task.setNote(false);
        Object secondList = task.configuration.note;
        assertNotNull(secondList);

        // Should be different instances
        assertNotSame(firstList, secondList);
    }

    @Test
    void testSetNoteTrueFromNonEmptyListDoesNothing() {
        ProGuardTask task = new ProGuardTask();

        // Manually create a non-empty list
        task.configuration.note = new ArrayList<>();
        task.configuration.note.add("some.pattern.*");

        // Setting to true should not change non-empty list
        task.setNote(true);
        assertNotNull(task.configuration.note);
        assertFalse(task.configuration.note.isEmpty());
        assertEquals(1, task.configuration.note.size());
        assertEquals("some.pattern.*", task.configuration.note.get(0));
    }

    @Test
    void testSetNoteFalseReplacesNonEmptyList() {
        ProGuardTask task = new ProGuardTask();

        // Manually create a non-empty list
        task.configuration.note = new ArrayList<>();
        task.configuration.note.add("some.pattern.*");

        // Setting to false should replace with empty list
        task.setNote(false);
        assertNotNull(task.configuration.note);
        assertTrue(task.configuration.note.isEmpty());
    }

    @Test
    void testSetNoteTrueOnlyAffectsEmptyList() {
        ProGuardTask task = new ProGuardTask();

        // Test with null (default)
        task.setNote(true);
        assertNull(task.configuration.note);

        // Test with empty list
        task.configuration.note = new ArrayList<>();
        task.setNote(true);
        assertNull(task.configuration.note);

        // Test with non-empty list
        task.configuration.note = new ArrayList<>();
        task.configuration.note.add("pattern");
        task.setNote(true);
        assertNotNull(task.configuration.note);
        assertEquals(1, task.configuration.note.size());
    }
}
