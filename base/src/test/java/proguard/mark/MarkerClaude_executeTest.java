package proguard.mark;

import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Marker.execute method that takes an AppView parameter.
 */
public class MarkerClaude_executeTest {

    /**
     * Test that execute method handles an empty AppView without throwing exceptions.
     */
    @Test
    public void testExecuteWithEmptyAppView() {
        Configuration configuration = new Configuration();
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle empty AppView without throwing exceptions");
    }

    /**
     * Test that execute method throws NullPointerException when AppView is null.
     */
    @Test
    public void testExecuteWithNullAppView() {
        Configuration configuration = new Configuration();
        Marker marker = new Marker(configuration);

        assertThrows(NullPointerException.class, () -> marker.execute(null),
            "execute should throw NullPointerException when AppView is null");
    }

    /**
     * Test that execute method handles AppView with empty class pools.
     */
    @Test
    public void testExecuteWithEmptyClassPools() {
        Configuration configuration = new Configuration();
        Marker marker = new Marker(configuration);
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle empty class pools without throwing exceptions");
    }

    /**
     * Test that execute method works with keepKotlinMetadata disabled.
     */
    @Test
    public void testExecuteWithKeepKotlinMetadataDisabled() {
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = false;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle keepKotlinMetadata disabled without throwing exceptions");
    }

    /**
     * Test that execute method works with keepKotlinMetadata enabled.
     */
    @Test
    public void testExecuteWithKeepKotlinMetadataEnabled() {
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle keepKotlinMetadata enabled without throwing exceptions");
    }

    /**
     * Test that execute method works with optimize disabled.
     */
    @Test
    public void testExecuteWithOptimizeDisabled() {
        Configuration configuration = new Configuration();
        configuration.optimize = false;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle optimize disabled without throwing exceptions");
    }

    /**
     * Test that execute method works with optimize enabled.
     */
    @Test
    public void testExecuteWithOptimizeEnabled() {
        Configuration configuration = new Configuration();
        configuration.optimize = true;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle optimize enabled without throwing exceptions");
    }

    /**
     * Test that execute method works with both keepKotlinMetadata and optimize enabled.
     */
    @Test
    public void testExecuteWithBothKotlinMetadataAndOptimizeEnabled() {
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;
        configuration.optimize = true;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle both keepKotlinMetadata and optimize enabled without throwing exceptions");
    }

    /**
     * Test that execute method can be called multiple times on the same AppView.
     */
    @Test
    public void testExecuteMultipleTimesOnSameAppView() {
        Configuration configuration = new Configuration();
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> {
            marker.execute(appView);
            marker.execute(appView);
        }, "execute should handle being called multiple times on the same AppView");
    }

    /**
     * Test that execute method works with optimizeConservatively enabled.
     */
    @Test
    public void testExecuteWithOptimizeConservatively() {
        Configuration configuration = new Configuration();
        configuration.optimize = true;
        configuration.optimizeConservatively = true;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle optimizeConservatively enabled without throwing exceptions");
    }

    /**
     * Test that execute method works with shrink disabled.
     */
    @Test
    public void testExecuteWithShrinkDisabled() {
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle shrink disabled without throwing exceptions");
    }

    /**
     * Test that execute method works with all optimization and obfuscation disabled.
     */
    @Test
    public void testExecuteWithAllProcessingDisabled() {
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle all processing disabled without throwing exceptions");
    }

    /**
     * Test that execute method works with a fully initialized AppView.
     */
    @Test
    public void testExecuteWithFullyInitializedAppView() {
        Configuration configuration = new Configuration();
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool,
            new proguard.resources.file.ResourceFilePool(), new proguard.io.ExtraDataEntryNameMap());
        Marker marker = new Marker(configuration);

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle fully initialized AppView without throwing exceptions");
    }

    /**
     * Test that execute method can be called by different Marker instances on the same AppView.
     */
    @Test
    public void testExecuteWithDifferentMarkerInstances() {
        Configuration configuration1 = new Configuration();
        Configuration configuration2 = new Configuration();
        Marker marker1 = new Marker(configuration1);
        Marker marker2 = new Marker(configuration2);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> {
            marker1.execute(appView);
            marker2.execute(appView);
        }, "execute should handle different marker instances operating on the same AppView");
    }

    /**
     * Test that execute method with null configuration throws NullPointerException.
     * This tests the case where Marker was constructed with null configuration.
     */
    @Test
    public void testExecuteWithNullConfiguration() {
        Marker marker = new Marker(null);
        AppView appView = new AppView();

        assertThrows(NullPointerException.class, () -> marker.execute(appView),
            "execute should throw NullPointerException when configuration is null");
    }

    /**
     * Test that execute method works with default Configuration values.
     */
    @Test
    public void testExecuteWithDefaultConfiguration() {
        Configuration configuration = new Configuration();
        // Default values: shrink=true, optimize=true, keepKotlinMetadata=false
        Marker marker = new Marker(configuration);
        AppView appView = new AppView();

        assertDoesNotThrow(() -> marker.execute(appView),
            "execute should handle default configuration values without throwing exceptions");
    }
}
