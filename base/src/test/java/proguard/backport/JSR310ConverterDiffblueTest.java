package proguard.backport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.backport.AbstractAPIConverter.TypeReplacement;
import proguard.classfile.ClassPool;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.FixedStringMatcher;

class JSR310ConverterDiffblueTest {
  /**
   * Test {@link JSR310Converter#JSR310Converter(ClassPool, ClassPool, WarningPrinter, ClassVisitor,
   * InstructionVisitor)}.
   *
   * <p>Method under test: {@link JSR310Converter#JSR310Converter(ClassPool, ClassPool,
   * WarningPrinter, ClassVisitor, InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new JSR310Converter(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void JSR310Converter.<init>(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)"
  })
  void testNewJSR310Converter() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    // Act
    JSR310Converter actualJsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            new CodeAttributeEditor());

    // Assert
    TypeReplacement missingResult = actualJsr310Converter.missing("Class Name");
    assertTrue(missingResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", missingResult.matchingClassName);
    assertNull(missingResult.replacementClassName);
  }
}
