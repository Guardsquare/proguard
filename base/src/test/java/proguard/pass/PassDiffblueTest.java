package proguard.pass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.KotlinMetadataAdapter;

class PassDiffblueTest {
  /**
   * Test {@link Pass#getName()}.
   *
   * <p>Method under test: {@link Pass#getName()}
   */
  @Test
  @DisplayName("Test getName()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String Pass.getName()"})
  void testGetName() {
    // Arrange, Act and Assert
    assertEquals("proguard.KotlinMetadataAdapter", new KotlinMetadataAdapter().getName());
  }
}
