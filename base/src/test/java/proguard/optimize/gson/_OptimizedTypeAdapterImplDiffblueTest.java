package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class _OptimizedTypeAdapterImplDiffblueTest {
  /**
   * Test {@link _OptimizedTypeAdapterImpl#read(JsonReader)}.
   *
   * <p>Method under test: {@link _OptimizedTypeAdapterImpl#read(JsonReader)}
   */
  @Test
  @DisplayName("Test read(JsonReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Object _OptimizedTypeAdapterImpl.read(JsonReader)"})
  void testRead() throws IOException {
    // Arrange
    Gson gson = new Gson();
    _OptimizedJsonReaderImpl optimizedJsonReader = new _OptimizedJsonReaderImpl();

    _OptimizedTypeAdapterImpl _OptimizedTypeAdapterImpl =
        new _OptimizedTypeAdapterImpl(gson, optimizedJsonReader, new _OptimizedJsonWriterImpl());

    // Act
    Object actualReadResult =
        _OptimizedTypeAdapterImpl.read(new JsonReader(new StringReader("foo")));

    // Assert
    assertNull(actualReadResult);
  }
}
