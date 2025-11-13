package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.optimize.gson.OptimizedJsonInfo.ClassJsonInfo;

class OptimizedJsonInfoDiffblueTest {
  /**
   * Test {@link OptimizedJsonInfo#assignIndices()}.
   *
   * <ul>
   *   <li>Then {@link OptimizedJsonInfo} (default constructor) {@link
   *       OptimizedJsonInfo#classIndices} size is one.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedJsonInfo#assignIndices()}
   */
  @Test
  @DisplayName(
      "Test assignIndices(); then OptimizedJsonInfo (default constructor) classIndices size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedJsonInfo.assignIndices()"})
  void testAssignIndices_thenOptimizedJsonInfoClassIndicesSizeIsOne() {
    // Arrange
    HashMap<String, Integer> stringIntegerMap = new HashMap<>();
    stringIntegerMap.put("foo", 1);
    OptimizedJsonInfo optimizedJsonInfo = new OptimizedJsonInfo();
    optimizedJsonInfo.classIndices = stringIntegerMap;
    optimizedJsonInfo.jsonFieldIndices = new HashMap<>();

    // Act
    optimizedJsonInfo.assignIndices();

    // Assert
    Map<String, Integer> stringIntegerMap2 = optimizedJsonInfo.classIndices;
    assertEquals(1, stringIntegerMap2.size());
    assertEquals(0, stringIntegerMap2.get("foo").intValue());
  }

  /**
   * Test ClassJsonInfo new {@link ClassJsonInfo} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ClassJsonInfo}
   */
  @Test
  @DisplayName("Test ClassJsonInfo new ClassJsonInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassJsonInfo.<init>()"})
  void testClassJsonInfoNewClassJsonInfo() {
    // Arrange and Act
    ClassJsonInfo actualClassJsonInfo = new ClassJsonInfo();

    // Assert
    assertTrue(actualClassJsonInfo.javaToJsonFieldNames.isEmpty());
    assertTrue(actualClassJsonInfo.exposedJavaFieldNames.isEmpty());
  }

  /**
   * Test new {@link OptimizedJsonInfo} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link OptimizedJsonInfo}
   */
  @Test
  @DisplayName("Test new OptimizedJsonInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedJsonInfo.<init>()"})
  void testNewOptimizedJsonInfo() {
    // Arrange and Act
    OptimizedJsonInfo actualOptimizedJsonInfo = new OptimizedJsonInfo();

    // Assert
    assertTrue(actualOptimizedJsonInfo.classIndices.isEmpty());
    assertTrue(actualOptimizedJsonInfo.classJsonInfos.isEmpty());
    assertTrue(actualOptimizedJsonInfo.jsonFieldIndices.isEmpty());
  }
}
