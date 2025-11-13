package proguard.backport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.backport.AbstractAPIConverter.TypeReplacement;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.FixedStringMatcher;

@ExtendWith(MockitoExtension.class)
class StreamSupportConverterDiffblueTest {
  @Mock private ClassPool classPool;

  @Mock private InstructionVisitor instructionVisitor;

  @Mock private WarningPrinter warningPrinter;

  /**
   * Test {@link StreamSupportConverter#StreamSupportConverter(ClassPool, ClassPool, WarningPrinter,
   * ClassVisitor, InstructionVisitor)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassPool#getClass(String)}.
   * </ul>
   *
   * <p>Method under test: {@link StreamSupportConverter#StreamSupportConverter(ClassPool,
   * ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new StreamSupportConverter(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor); then calls getClass(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StreamSupportConverter.<init>(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)"
  })
  void testNewStreamSupportConverter_thenCallsGetClass() {
    // Arrange
    when(classPool.getClass(Mockito.<String>any())).thenReturn(new LibraryClass());

    // Act
    StreamSupportConverter actualStreamSupportConverter =
        new StreamSupportConverter(
            classPool, classPool, warningPrinter, mock(ClassVisitor.class), instructionVisitor);

    // Assert
    verify(classPool, atLeast(1)).getClass(Mockito.<String>any());
    TypeReplacement missingResult = actualStreamSupportConverter.missing("Class Name");
    assertTrue(missingResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", missingResult.matchingClassName);
    assertNull(missingResult.replacementClassName);
  }

  /**
   * Test {@link StreamSupportConverter#StreamSupportConverter(ClassPool, ClassPool, WarningPrinter,
   * ClassVisitor, InstructionVisitor)}.
   *
   * <ul>
   *   <li>When {@link KotlinConstants#dummyClassPool}.
   * </ul>
   *
   * <p>Method under test: {@link StreamSupportConverter#StreamSupportConverter(ClassPool,
   * ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new StreamSupportConverter(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor); when dummyClassPool")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StreamSupportConverter.<init>(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)"
  })
  void testNewStreamSupportConverter_whenDummyClassPool() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    // Act
    StreamSupportConverter actualStreamSupportConverter =
        new StreamSupportConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            new CodeAttributeEditor());

    // Assert
    TypeReplacement missingResult = actualStreamSupportConverter.missing("Class Name");
    assertTrue(missingResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", missingResult.matchingClassName);
    assertNull(missingResult.replacementClassName);
  }
}
