package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setRenamesourcefileattribute method.
 *
 * This test class verifies the behavior of the setRenamesourcefileattribute(String) method
 * which sets the new string to be put in the source file attributes.
 *
 * The method directly sets configuration.newSourceFileAttribute field.
 * Default value is null.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setRenamesourcefileattributeTest {

    @Test
    void testSetRenamesourcefileattributeWithNonNullValue() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithDifferentValue() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("MySource.java");
        assertEquals("MySource.java", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithEmptyString() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("");
        assertEquals("", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithNull() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("SourceFile");
        task.setRenamesourcefileattribute(null);
        assertNull(task.configuration.newSourceFileAttribute);
    }

    @Test
    void testDefaultValueIsNull() {
        ProGuardTask task = new ProGuardTask();
        assertNull(task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeOverwritesPreviousValue() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("FirstValue");
        assertEquals("FirstValue", task.configuration.newSourceFileAttribute);
        task.setRenamesourcefileattribute("SecondValue");
        assertEquals("SecondValue", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeMultipleTimes() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("Value1");
        task.setRenamesourcefileattribute("Value2");
        task.setRenamesourcefileattribute("Value3");
        assertEquals("Value3", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithSpecialCharacters() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("Source-File_123.java");
        assertEquals("Source-File_123.java", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithWhitespace() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("Source File");
        assertEquals("Source File", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithLeadingTrailingSpaces() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("  SourceFile  ");
        assertEquals("  SourceFile  ", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setRenamesourcefileattribute("Task1Source");
        task2.setRenamesourcefileattribute("Task2Source");

        assertEquals("Task1Source", task1.configuration.newSourceFileAttribute);
        assertEquals("Task2Source", task2.configuration.newSourceFileAttribute);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setRenamesourcefileattribute("SourceFile");

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setRenamesourcefileattribute("SourceFile");
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setRenamesourcefileattribute("SourceFile");
        assertNotNull(task.configuration);
    }

    @Test
    void testSetRenamesourcefileattributeWithLongString() {
        ProGuardTask task = new ProGuardTask();
        String longString = "ThisIsAVeryLongSourceFileAttributeNameThatContainsManyCharacters";
        task.setRenamesourcefileattribute(longString);
        assertEquals(longString, task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithSingleCharacter() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("S");
        assertEquals("S", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeNullThenValue() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute(null);
        assertNull(task.configuration.newSourceFileAttribute);
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeEmptyThenValue() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("");
        assertEquals("", task.configuration.newSourceFileAttribute);
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeValueThenEmpty() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
        task.setRenamesourcefileattribute("");
        assertEquals("", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeValueThenNull() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
        task.setRenamesourcefileattribute(null);
        assertNull(task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithPath() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("com/example/SourceFile.java");
        assertEquals("com/example/SourceFile.java", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithUnicodeCharacters() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("Source文件.java");
        assertEquals("Source文件.java", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setRenamesourcefileattribute("SourceFile");

        assertTrue(task.configuration.obfuscate);
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setRenamesourcefileattribute("SourceFile");
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testSetRenamesourcefileattributeWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setRenamesourcefileattribute("SourceFile");

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }

    @Test
    void testSetRenamesourcefileattributeSameValueTwice() {
        ProGuardTask task = new ProGuardTask();
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
        task.setRenamesourcefileattribute("SourceFile");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute);
    }
}
