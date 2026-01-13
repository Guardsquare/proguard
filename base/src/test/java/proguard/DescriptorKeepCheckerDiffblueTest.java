package proguard;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;

@ExtendWith(MockitoExtension.class)
class DescriptorKeepCheckerDiffblueTest {
  @Mock private ClassPool classPool;

  @Mock private Clazz clazz;

  @InjectMocks private DescriptorKeepChecker descriptorKeepChecker;

  /**
   * Test {@link DescriptorKeepChecker#checkClassSpecifications(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link DescriptorKeepChecker#checkClassSpecifications(List)}
   */
  @Test
  @DisplayName("Test checkClassSpecifications(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DescriptorKeepChecker.checkClassSpecifications(List)"})
  void testCheckClassSpecifications_whenArrayList() {
    // Arrange
    doNothing().when(classPool).accept(Mockito.<ClassPoolVisitor>any());
    doNothing().when(classPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    descriptorKeepChecker.checkClassSpecifications(new ArrayList<>());

    // Assert
    verify(classPool, atLeast(1)).accept(isA(ClassPoolVisitor.class));
    verify(classPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link DescriptorKeepChecker#checkClassSpecifications(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link DescriptorKeepChecker#checkClassSpecifications(List)}
   */
  @Test
  @DisplayName("Test checkClassSpecifications(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DescriptorKeepChecker.checkClassSpecifications(List)"})
  void testCheckClassSpecifications_whenNull() {
    // Arrange
    doNothing().when(classPool).accept(Mockito.<ClassPoolVisitor>any());
    doNothing().when(classPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    descriptorKeepChecker.checkClassSpecifications(null);

    // Assert
    verify(classPool, atLeast(1)).accept(isA(ClassPoolVisitor.class));
    verify(classPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link DescriptorKeepChecker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link Clazz#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link DescriptorKeepChecker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'String'; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DescriptorKeepChecker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenString_thenCallsAccept() {
    // Arrange
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    Clazz[] referencedClasses = new Clazz[] {clazz};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, 3, attributes, referencedClasses);

    // Act
    descriptorKeepChecker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(clazz).accept(isA(ClassVisitor.class));
    verify(programClass).getString(1);
  }
}
