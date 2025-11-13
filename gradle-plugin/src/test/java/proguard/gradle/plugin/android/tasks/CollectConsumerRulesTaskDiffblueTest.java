package proguard.gradle.plugin.android.tasks;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.gradle.api.tasks.TaskInstantiationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class CollectConsumerRulesTaskDiffblueTest {
  /**
   * Test new {@link CollectConsumerRulesTask} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link CollectConsumerRulesTask}
   */
  @Test
  @DisplayName("Test new CollectConsumerRulesTask (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CollectConsumerRulesTask.<init>()"})
  void testNewCollectConsumerRulesTask() {
    // Arrange, Act and Assert
    assertThrows(TaskInstantiationException.class, () -> new CollectConsumerRulesTask());
  }
}
