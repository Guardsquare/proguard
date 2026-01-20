package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.io.ExtraDataEntryNameMap;
import proguard.pass.Pass;
import proguard.resources.file.ResourceFilePool;
import proguard.resources.file.visitor.ResourceFileVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameObfuscationReferenceFixer}.
 * Tests the constructor and execute method.
 */
public class NameObfuscationReferenceFixerClaudeTest {

    /**
     * Tests that the constructor creates a valid NameObfuscationReferenceFixer instance.
     * Verifies that the instance is not null.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Arrange - Create a valid Configuration
        Configuration configuration = new Configuration();

        // Act - Create a NameObfuscationReferenceFixer using the constructor
        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Assert - Verify the instance was created successfully
        assertNotNull(fixer, "NameObfuscationReferenceFixer should be instantiated successfully");
    }

    /**
     * Tests that the constructor creates an instance that implements Pass.
     * Verifies that NameObfuscationReferenceFixer can be used as a Pass.
     */
    @Test
    public void testConstructorCreatesInstanceOfPass() {
        // Arrange - Create a valid Configuration
        Configuration configuration = new Configuration();

        // Act - Create a NameObfuscationReferenceFixer
        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Assert - Verify the instance implements Pass
        assertInstanceOf(Pass.class, fixer,
                "NameObfuscationReferenceFixer should implement Pass interface");
    }

    /**
     * Tests the constructor with null Configuration.
     * Verifies that the constructor accepts a null configuration.
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act - Create fixer with null configuration
        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(null);

        // Assert - Verify the fixer was created
        assertNotNull(fixer, "NameObfuscationReferenceFixer should be instantiated even with null configuration");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testConstructorCreatesMultipleIndependentInstances() {
        // Arrange - Create configurations
        Configuration configuration1 = new Configuration();
        Configuration configuration2 = new Configuration();

        // Act - Create multiple NameObfuscationReferenceFixer instances
        NameObfuscationReferenceFixer fixer1 = new NameObfuscationReferenceFixer(configuration1);
        NameObfuscationReferenceFixer fixer2 = new NameObfuscationReferenceFixer(configuration2);

        // Assert - Verify both instances are created and are distinct
        assertNotNull(fixer1, "First NameObfuscationReferenceFixer instance should be created");
        assertNotNull(fixer2, "Second NameObfuscationReferenceFixer instance should be created");
        assertNotSame(fixer1, fixer2,
                "Multiple instances should be distinct objects");
    }

    /**
     * Tests execute method when keepKotlinMetadata is false.
     * Verifies that resourceFilesAccept is not called when Kotlin metadata is not kept.
     */
    @Test
    public void testExecuteWithKeepKotlinMetadataFalse() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to false
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = false;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create mock AppView with spy ResourceFilePool to verify no interactions
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute the fixer
        fixer.execute(appView);

        // Assert - Verify resourceFilesAccept was not called
        verify(resourceFilePool, never()).resourceFilesAccept(any(ResourceFileVisitor.class));
    }

    /**
     * Tests execute method when keepKotlinMetadata is true.
     * Verifies that resourceFilesAccept is called when Kotlin metadata is kept.
     */
    @Test
    public void testExecuteWithKeepKotlinMetadataTrue() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to true
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create mock AppView with spy ResourceFilePool
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute the fixer
        fixer.execute(appView);

        // Assert - Verify resourceFilesAccept was called at least once
        verify(resourceFilePool, atLeastOnce()).resourceFilesAccept(any(ResourceFileVisitor.class));
    }

    /**
     * Tests execute method with null AppView.
     * Verifies behavior when execute is called with null AppView.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange - Create configuration
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Act & Assert - Execute with null AppView should throw NullPointerException
        assertThrows(NullPointerException.class, () -> fixer.execute(null),
                "Execute with null AppView should throw NullPointerException");
    }

    /**
     * Tests execute method with null Configuration stored in fixer.
     * Verifies behavior when the stored configuration is null and execute is called.
     */
    @Test
    public void testExecuteWithNullConfigurationStored() throws Exception {
        // Arrange - Create fixer with null configuration
        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(null);

        // Create AppView
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act & Assert - Execute should throw NullPointerException when accessing configuration.keepKotlinMetadata
        assertThrows(NullPointerException.class, () -> fixer.execute(appView),
                "Execute with null configuration should throw NullPointerException");
    }

    /**
     * Tests execute method multiple times with keepKotlinMetadata true.
     * Verifies that execute can be called multiple times successfully.
     */
    @Test
    public void testExecuteMultipleTimesWithKeepKotlinMetadataTrue() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to true
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create AppView with spy ResourceFilePool
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute the fixer multiple times
        fixer.execute(appView);
        fixer.execute(appView);
        fixer.execute(appView);

        // Assert - Verify resourceFilesAccept was called three times
        verify(resourceFilePool, times(3)).resourceFilesAccept(any(ResourceFileVisitor.class));
    }

    /**
     * Tests execute method multiple times with keepKotlinMetadata false.
     * Verifies that resourceFilesAccept is never called when keepKotlinMetadata is false.
     */
    @Test
    public void testExecuteMultipleTimesWithKeepKotlinMetadataFalse() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to false
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = false;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create AppView with spy ResourceFilePool
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute the fixer multiple times
        fixer.execute(appView);
        fixer.execute(appView);
        fixer.execute(appView);

        // Assert - Verify resourceFilesAccept was never called
        verify(resourceFilePool, never()).resourceFilesAccept(any(ResourceFileVisitor.class));
    }

    /**
     * Tests execute method with different AppView instances.
     * Verifies that execute works correctly with different AppView instances.
     */
    @Test
    public void testExecuteWithDifferentAppViewInstances() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to true
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create multiple AppView instances
        ResourceFilePool resourceFilePool1 = spy(new ResourceFilePool());
        AppView appView1 = new AppView(new ClassPool(), new ClassPool(), resourceFilePool1, new ExtraDataEntryNameMap());

        ResourceFilePool resourceFilePool2 = spy(new ResourceFilePool());
        AppView appView2 = new AppView(new ClassPool(), new ClassPool(), resourceFilePool2, new ExtraDataEntryNameMap());

        // Act - Execute with different AppView instances
        fixer.execute(appView1);
        fixer.execute(appView2);

        // Assert - Verify resourceFilesAccept was called on both pools
        verify(resourceFilePool1, atLeastOnce()).resourceFilesAccept(any(ResourceFileVisitor.class));
        verify(resourceFilePool2, atLeastOnce()).resourceFilesAccept(any(ResourceFileVisitor.class));
    }

    /**
     * Tests getName method from Pass interface.
     * Verifies that getName returns the expected class name.
     */
    @Test
    public void testGetName() {
        // Arrange - Create configuration and fixer
        Configuration configuration = new Configuration();
        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Act - Get the name
        String name = fixer.getName();

        // Assert - Verify the name is the fully qualified class name
        assertEquals("proguard.obfuscate.NameObfuscationReferenceFixer", name,
                "getName should return the fully qualified class name");
    }

    /**
     * Tests execute with empty ResourceFilePool.
     * Verifies that execute works correctly when the ResourceFilePool is empty.
     */
    @Test
    public void testExecuteWithEmptyResourceFilePool() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to true
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create AppView with empty ResourceFilePool
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute the fixer
        assertDoesNotThrow(() -> fixer.execute(appView),
                "Execute should not throw with empty ResourceFilePool");
    }

    /**
     * Tests execute with non-empty ResourceFilePool.
     * Verifies that execute works correctly when the ResourceFilePool is provided.
     */
    @Test
    public void testExecuteWithNonEmptyResourceFilePool() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to true
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create AppView with ResourceFilePool (ResourceFilePool state doesn't affect the test)
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute the fixer
        assertDoesNotThrow(() -> fixer.execute(appView),
                "Execute should not throw with non-empty ResourceFilePool");
    }

    /**
     * Tests execute with configuration that has keepKotlinMetadata changed after construction.
     * Verifies that the fixer uses the configuration's current state.
     */
    @Test
    public void testExecuteWithConfigurationChangedAfterConstruction() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata initially false
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = false;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create AppView
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Execute with keepKotlinMetadata false
        fixer.execute(appView);
        verify(resourceFilePool, never()).resourceFilesAccept(any(ResourceFileVisitor.class));

        // Act - Change configuration to keepKotlinMetadata true and execute again
        configuration.keepKotlinMetadata = true;
        fixer.execute(appView);

        // Assert - Verify resourceFilesAccept is now called
        verify(resourceFilePool, atLeastOnce()).resourceFilesAccept(any(ResourceFileVisitor.class));
    }

    /**
     * Tests execute with default Configuration.
     * Verifies that execute works correctly with a default Configuration where keepKotlinMetadata defaults to false.
     */
    @Test
    public void testExecuteWithDefaultConfiguration() throws Exception {
        // Arrange - Create default configuration (keepKotlinMetadata should be false by default)
        Configuration configuration = new Configuration();

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create AppView
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute the fixer
        fixer.execute(appView);

        // Assert - Verify resourceFilesAccept was not called (keepKotlinMetadata defaults to false)
        verify(resourceFilePool, never()).resourceFilesAccept(any(ResourceFileVisitor.class));
    }

    /**
     * Tests that the same fixer instance can be used with multiple configurations.
     * Verifies that changing the configuration affects the fixer's behavior.
     */
    @Test
    public void testSameFixerInstanceWithChangingConfiguration() throws Exception {
        // Arrange - Create configuration with keepKotlinMetadata set to false
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = false;

        NameObfuscationReferenceFixer fixer = new NameObfuscationReferenceFixer(configuration);

        // Create AppView
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - First execution with keepKotlinMetadata false
        fixer.execute(appView);
        int invocationCountBefore = mockingDetails(resourceFilePool).getInvocations().size();

        // Change configuration
        configuration.keepKotlinMetadata = true;

        // Second execution with keepKotlinMetadata true
        fixer.execute(appView);
        int invocationCountAfter = mockingDetails(resourceFilePool).getInvocations().size();

        // Assert - Verify behavior changed based on configuration
        assertTrue(invocationCountAfter > invocationCountBefore,
                "After setting keepKotlinMetadata to true, resourceFilesAccept should be called");
    }

    /**
     * Tests multiple fixers with the same Configuration instance.
     * Verifies that multiple fixers can share the same Configuration.
     */
    @Test
    public void testMultipleFixersWithSameConfiguration() throws Exception {
        // Arrange - Create shared configuration with keepKotlinMetadata set to true
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        NameObfuscationReferenceFixer fixer1 = new NameObfuscationReferenceFixer(configuration);
        NameObfuscationReferenceFixer fixer2 = new NameObfuscationReferenceFixer(configuration);

        // Create AppView
        ResourceFilePool resourceFilePool = spy(new ResourceFilePool());
        AppView appView = new AppView(new ClassPool(), new ClassPool(), resourceFilePool, new ExtraDataEntryNameMap());

        // Act - Execute both fixers
        fixer1.execute(appView);
        fixer2.execute(appView);

        // Assert - Verify resourceFilesAccept was called twice (once per fixer)
        verify(resourceFilePool, times(2)).resourceFilesAccept(any(ResourceFileVisitor.class));
    }
}
