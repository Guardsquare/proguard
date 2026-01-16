package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to achieve coverage of the extendList private method in ConfigurationTask.
 * Tests focus on the appendTo method which calls extendList for various configuration lists.
 *
 * Target coverage for extendList:
 * - Line 482: if (list == null)
 * - Line 484: list = new ArrayList();
 * - Line 487: list.addAll(additionalList);
 */
public class ConfigurationTaskClaude_extendListTest {

    private ConfigurationTask configurationTask;
    private Project project;

    @BeforeEach
    public void setUp() {
        configurationTask = new ConfigurationTask();
        project = new Project();
        project.init();
        configurationTask.setProject(project);
    }

    /**
     * Test extendList coverage via appendTo with keepDirectories.
     * This covers the case where the target list is null and additionalList is not null.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithKeepDirectories_NullTargetList() {
        // Add keepDirectories to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com/example/**");
        configurationTask.addConfiguredKeepdirectory(filterElement);

        // Verify the source configuration has keepDirectories
        assertNotNull(configurationTask.configuration.keepDirectories,
            "Source configuration should have keepDirectories");
        assertFalse(configurationTask.configuration.keepDirectories.isEmpty(),
            "Source keepDirectories should not be empty");

        // Create a target configuration with null keepDirectories
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.keepDirectories, "Target keepDirectories should start as null");

        // Call appendTo - this will trigger extendList with null list and non-null additionalList
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the keepDirectories list initialized and populated
        assertNotNull(targetConfig.keepDirectories, "Target keepDirectories should be initialized");
        assertFalse(targetConfig.keepDirectories.isEmpty(), "Target keepDirectories should be populated");
    }

    /**
     * Test extendList coverage via appendTo with keepPackageNames.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithKeepPackageNames_NullTargetList() {
        // Add keepPackageNames to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");
        configurationTask.addConfiguredKeeppackagename(filterElement);

        assertNotNull(configurationTask.configuration.keepPackageNames,
            "Source configuration should have keepPackageNames");

        // Create a target configuration with null keepPackageNames
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.keepPackageNames, "Target keepPackageNames should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the list
        assertNotNull(targetConfig.keepPackageNames, "Target keepPackageNames should be initialized");
        assertFalse(targetConfig.keepPackageNames.isEmpty(), "Target keepPackageNames should be populated");
    }

    /**
     * Test extendList coverage via appendTo with keepAttributes.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithKeepAttributes_NullTargetList() {
        // Add keepAttributes to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("Signature,InnerClasses");
        configurationTask.addConfiguredKeepattribute(filterElement);

        assertNotNull(configurationTask.configuration.keepAttributes,
            "Source configuration should have keepAttributes");

        // Create a target configuration with null keepAttributes
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.keepAttributes, "Target keepAttributes should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the list
        assertNotNull(targetConfig.keepAttributes, "Target keepAttributes should be initialized");
        assertFalse(targetConfig.keepAttributes.isEmpty(), "Target keepAttributes should be populated");
    }

    /**
     * Test extendList coverage via appendTo with adaptClassStrings.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithAdaptClassStrings_NullTargetList() {
        // Add adaptClassStrings to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");
        configurationTask.addConfiguredAdaptclassstrings(filterElement);

        assertNotNull(configurationTask.configuration.adaptClassStrings,
            "Source configuration should have adaptClassStrings");

        // Create a target configuration with null adaptClassStrings
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.adaptClassStrings, "Target adaptClassStrings should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the list
        assertNotNull(targetConfig.adaptClassStrings, "Target adaptClassStrings should be initialized");
        assertFalse(targetConfig.adaptClassStrings.isEmpty(), "Target adaptClassStrings should be populated");
    }

    /**
     * Test extendList coverage via appendTo with adaptResourceFileNames.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithAdaptResourceFileNames_NullTargetList() {
        // Add adaptResourceFileNames to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("**.properties");
        configurationTask.addConfiguredAdaptresourcefilenames(filterElement);

        assertNotNull(configurationTask.configuration.adaptResourceFileNames,
            "Source configuration should have adaptResourceFileNames");

        // Create a target configuration with null adaptResourceFileNames
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.adaptResourceFileNames, "Target adaptResourceFileNames should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the list
        assertNotNull(targetConfig.adaptResourceFileNames, "Target adaptResourceFileNames should be initialized");
        assertFalse(targetConfig.adaptResourceFileNames.isEmpty(), "Target adaptResourceFileNames should be populated");
    }

    /**
     * Test extendList coverage via appendTo with adaptResourceFileContents.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithAdaptResourceFileContents_NullTargetList() {
        // Add adaptResourceFileContents to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("**.xml");
        configurationTask.addConfiguredAdaptresourcefilecontents(filterElement);

        assertNotNull(configurationTask.configuration.adaptResourceFileContents,
            "Source configuration should have adaptResourceFileContents");

        // Create a target configuration with null adaptResourceFileContents
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.adaptResourceFileContents, "Target adaptResourceFileContents should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the list
        assertNotNull(targetConfig.adaptResourceFileContents, "Target adaptResourceFileContents should be initialized");
        assertFalse(targetConfig.adaptResourceFileContents.isEmpty(), "Target adaptResourceFileContents should be populated");
    }

    /**
     * Test extendList coverage via appendTo with note list.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithNote_NullTargetList() {
        // Add note filter to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");
        configurationTask.addConfiguredDontnote(filterElement);

        assertNotNull(configurationTask.configuration.note,
            "Source configuration should have note");

        // Create a target configuration with null note
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.note, "Target note should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the list
        assertNotNull(targetConfig.note, "Target note should be initialized");
        assertFalse(targetConfig.note.isEmpty(), "Target note should be populated");
    }

    /**
     * Test extendList coverage via appendTo with warn list.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithWarn_NullTargetList() {
        // Add warn filter to the source configuration
        FilterElement filterElement = new FilterElement();
        filterElement.setName("org.test.**");
        configurationTask.addConfiguredDontwarn(filterElement);

        assertNotNull(configurationTask.configuration.warn,
            "Source configuration should have warn");

        // Create a target configuration with null warn
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.warn, "Target warn should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target now has the list
        assertNotNull(targetConfig.warn, "Target warn should be initialized");
        assertFalse(targetConfig.warn.isEmpty(), "Target warn should be populated");
    }

    /**
     * Test extendList with multiple items to ensure addAll works correctly.
     * Coverage: lines 482, 484, 487
     */
    @Test
    public void testAppendToWithMultipleKeepDirectories_NullTargetList() {
        // Add multiple keepDirectories to the source configuration
        FilterElement filter1 = new FilterElement();
        filter1.setName("com/example/**");
        configurationTask.addConfiguredKeepdirectory(filter1);

        FilterElement filter2 = new FilterElement();
        filter2.setName("org/test/**");
        configurationTask.addConfiguredKeepdirectories(filter2);

        // Verify the source has multiple entries
        assertTrue(configurationTask.configuration.keepDirectories.size() >= 2,
            "Source should have at least 2 keepDirectories entries");

        // Create a target configuration with null keepDirectories
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.keepDirectories, "Target keepDirectories should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify all entries were copied
        assertNotNull(targetConfig.keepDirectories, "Target keepDirectories should be initialized");
        assertTrue(targetConfig.keepDirectories.size() >= 2,
            "Target should have at least 2 keepDirectories entries");
    }

    /**
     * Test extendList when target list already has items (merging case).
     * This tests the path where list != null in extendList.
     * Coverage: lines 487 (with non-null list)
     */
    @Test
    public void testAppendToWithExistingKeepDirectories_MergesLists() {
        // Add keepDirectories to the source configuration
        FilterElement sourceFilter = new FilterElement();
        sourceFilter.setName("com/source/**");
        configurationTask.addConfiguredKeepdirectory(sourceFilter);

        // Create a target configuration with existing keepDirectories
        Configuration targetConfig = new Configuration();
        targetConfig.keepDirectories = new ArrayList();
        targetConfig.keepDirectories.add("com/existing/**");

        int initialSize = targetConfig.keepDirectories.size();
        assertEquals(1, initialSize, "Target should start with 1 keepDirectories entry");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the lists were merged
        assertNotNull(targetConfig.keepDirectories, "Target keepDirectories should still exist");
        assertTrue(targetConfig.keepDirectories.size() > initialSize,
            "Target should have more entries after merge");
    }

    /**
     * Test extendList when source configuration has null list (no operation case).
     * This tests the path where additionalList is null.
     * Coverage: ensures the method returns early when additionalList is null
     */
    @Test
    public void testAppendToWithNullSourceList() {
        // Don't add any keepDirectories to the source configuration
        assertNull(configurationTask.configuration.keepDirectories,
            "Source configuration should have null keepDirectories");

        // Create a target configuration with some keepDirectories
        Configuration targetConfig = new Configuration();
        targetConfig.keepDirectories = new ArrayList();
        targetConfig.keepDirectories.add("com/existing/**");

        int initialSize = targetConfig.keepDirectories.size();

        // Call appendTo - extendList should return early since additionalList is null
        configurationTask.appendTo(targetConfig);

        // Verify the target list remains unchanged
        assertEquals(initialSize, targetConfig.keepDirectories.size(),
            "Target list should remain unchanged when source is null");
    }

    /**
     * Test extendList with empty source list (has list but no entries).
     * Coverage: lines 487 (addAll with empty list)
     */
    @Test
    public void testAppendToWithEmptySourceList() {
        // Create source with an empty list
        configurationTask.configuration.keepDirectories = new ArrayList();
        assertTrue(configurationTask.configuration.keepDirectories.isEmpty(),
            "Source list should be empty");

        // Create a target configuration with null keepDirectories
        Configuration targetConfig = new Configuration();
        assertNull(targetConfig.keepDirectories, "Target keepDirectories should start as null");

        // Call appendTo
        configurationTask.appendTo(targetConfig);

        // Verify the target has an initialized but empty list
        assertNotNull(targetConfig.keepDirectories, "Target keepDirectories should be initialized");
        assertTrue(targetConfig.keepDirectories.isEmpty(), "Target keepDirectories should be empty");
    }
}
