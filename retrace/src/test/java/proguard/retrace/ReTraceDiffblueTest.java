package proguard.retrace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ReTraceDiffblueTest {
  /**
   * Test {@link ReTrace#retrace(LineNumberReader, PrintWriter)}.
   *
   * <p>Method under test: {@link ReTrace#retrace(LineNumberReader, PrintWriter)}
   */
  @Test
  @DisplayName("Test retrace(LineNumberReader, PrintWriter)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReTrace.retrace(LineNumberReader, PrintWriter)"})
  void testRetrace() throws IOException {
    // Arrange
    ReTrace reTrace =
        new ReTrace(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    LineNumberReader stackTraceReader = new LineNumberReader(new StringReader("foo"), 1);

    // Act
    reTrace.retrace(
        stackTraceReader,
        new PrintWriter(new OutputStreamWriter(new ByteArrayOutputStream(), "UTF-8")));

    // Assert
    Stream<String> linesResult = stackTraceReader.lines();
    assertEquals("", linesResult.collect(Collectors.joining("\n")));
  }

  /**
   * Test {@link ReTrace#retrace(LineNumberReader, PrintWriter)}.
   *
   * <p>Method under test: {@link ReTrace#retrace(LineNumberReader, PrintWriter)}
   */
  @Test
  @DisplayName("Test retrace(LineNumberReader, PrintWriter)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReTrace.retrace(LineNumberReader, PrintWriter)"})
  void testRetrace2() throws IOException {
    // Arrange
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt");
    ReTrace reTrace =
        new ReTrace(
            "(?:[^\\s\":./()]+\\.)*[^\\s\":./()]+",
            "(?:[^\\s\":./()]+\\.)*[^\\s\":./()]+",
            true,
            true,
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    LineNumberReader stackTraceReader =
        new LineNumberReader(new StringReader("(?:[^\\s\":./()]+\\.)*[^\\s\":./()]+"));

    // Act
    reTrace.retrace(
        stackTraceReader,
        new PrintWriter(new OutputStreamWriter(new ByteArrayOutputStream(), "UTF-8")));

    // Assert
    Stream<String> linesResult = stackTraceReader.lines();
    assertEquals("", linesResult.collect(Collectors.joining("\n")));
  }
}
