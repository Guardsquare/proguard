package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class GsonRuntimeSettingsDiffblueTest {
  /**
   * Test new {@link GsonRuntimeSettings} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link GsonRuntimeSettings}
   */
  @Test
  @DisplayName("Test new GsonRuntimeSettings (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void GsonRuntimeSettings.<init>()"})
  void testNewGsonRuntimeSettings() {
    // Arrange and Act
    GsonRuntimeSettings actualGsonRuntimeSettings = new GsonRuntimeSettings();

    // Assert
    assertFalse(actualGsonRuntimeSettings.addDeserializationExclusionStrategy);
    assertFalse(actualGsonRuntimeSettings.addSerializationExclusionStrategy);
    assertFalse(actualGsonRuntimeSettings.disableInnerClassSerialization);
    assertFalse(actualGsonRuntimeSettings.excludeFieldsWithModifiers);
    assertFalse(actualGsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation);
    assertFalse(actualGsonRuntimeSettings.generateNonExecutableJson);
    assertFalse(actualGsonRuntimeSettings.registerTypeAdapterFactory);
    assertFalse(actualGsonRuntimeSettings.serializeNulls);
    assertFalse(actualGsonRuntimeSettings.serializeSpecialFloatingPointValues);
    assertFalse(actualGsonRuntimeSettings.setExclusionStrategies);
    assertFalse(actualGsonRuntimeSettings.setFieldNamingPolicy);
    assertFalse(actualGsonRuntimeSettings.setFieldNamingStrategy);
    assertFalse(actualGsonRuntimeSettings.setLongSerializationPolicy);
    assertFalse(actualGsonRuntimeSettings.setVersion);
  }
}
