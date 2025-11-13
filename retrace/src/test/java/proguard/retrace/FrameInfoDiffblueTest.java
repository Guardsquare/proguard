package proguard.retrace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class FrameInfoDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link FrameInfo#FrameInfo(String, String, int, String, String, String, String)}
   *   <li>{@link FrameInfo#toString()}
   *   <li>{@link FrameInfo#getArguments()}
   *   <li>{@link FrameInfo#getClassName()}
   *   <li>{@link FrameInfo#getFieldName()}
   *   <li>{@link FrameInfo#getLineNumber()}
   *   <li>{@link FrameInfo#getMethodName()}
   *   <li>{@link FrameInfo#getSourceFile()}
   *   <li>{@link FrameInfo#getType()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void FrameInfo.<init>(String, String, int, String, String, String, String)",
    "String FrameInfo.getArguments()",
    "String FrameInfo.getClassName()",
    "String FrameInfo.getFieldName()",
    "int FrameInfo.getLineNumber()",
    "String FrameInfo.getMethodName()",
    "String FrameInfo.getSourceFile()",
    "String FrameInfo.getType()",
    "String FrameInfo.toString()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    FrameInfo actualFrameInfo =
        new FrameInfo(
            "Class Name", "Source File", 2, "Type", "Field Name", "Method Name", "Arguments");
    String actualToStringResult = actualFrameInfo.toString();
    String actualArguments = actualFrameInfo.getArguments();
    String actualClassName = actualFrameInfo.getClassName();
    String actualFieldName = actualFrameInfo.getFieldName();
    int actualLineNumber = actualFrameInfo.getLineNumber();
    String actualMethodName = actualFrameInfo.getMethodName();
    String actualSourceFile = actualFrameInfo.getSourceFile();

    // Assert
    assertEquals("Arguments", actualArguments);
    assertEquals("Class Name", actualClassName);
    assertEquals("Field Name", actualFieldName);
    assertEquals("Method Name", actualMethodName);
    assertEquals("Source File", actualSourceFile);
    assertEquals("Type", actualFrameInfo.getType());
    assertEquals(
        "proguard.retrace.FrameInfo(class=[Class Name], line=[2], type=[Type], field=[Field Name], method=[Method"
            + " Name], arguments=[Arguments]",
        actualToStringResult);
    assertEquals(2, actualLineNumber);
  }
}
