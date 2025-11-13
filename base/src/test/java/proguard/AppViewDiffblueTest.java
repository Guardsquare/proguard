package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.io.ExtraDataEntryNameMap;

class AppViewDiffblueTest {
  /**
   * Test {@link AppView#AppView()}.
   *
   * <p>Method under test: {@link AppView#AppView()}
   */
  @Test
  @DisplayName("Test new AppView()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AppView.<init>()"})
  void testNewAppView() {
    // Arrange and Act
    AppView actualAppView = new AppView();

    // Assert
    ExtraDataEntryNameMap extraDataEntryNameMap = actualAppView.extraDataEntryNameMap;
    assertNull(extraDataEntryNameMap.getDefaultExtraDataEntryNames());
    assertNull(actualAppView.initialStateInfo);
    assertEquals(0, actualAppView.libraryClassPool.size());
    assertEquals(0, actualAppView.programClassPool.size());
    assertEquals(0, actualAppView.resourceFilePool.size());
    assertTrue(extraDataEntryNameMap.getAllExtraDataEntryNames().isEmpty());
    assertTrue(extraDataEntryNameMap.getKeyDataEntryNames().isEmpty());
  }
}
