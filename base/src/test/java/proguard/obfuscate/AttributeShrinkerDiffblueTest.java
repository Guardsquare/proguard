package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMember;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.RecordAttribute;
import proguard.classfile.attribute.RecordComponentInfo;

class AttributeShrinkerDiffblueTest {
  /**
   * Test {@link AttributeShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <p>Method under test: {@link AttributeShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName("Test visitProgramMember(ProgramClass, ProgramMember)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AttributeShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember() {
    // Arrange
    AttributeShrinker attributeShrinker = new AttributeShrinker();
    ProgramClass programClass = new ProgramClass();
    ProgramField programMember = new ProgramField(1, 1, 1, new LibraryClass());
    programMember.u2attributesCount = 0;

    // Act
    attributeShrinker.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    assertEquals(0, programMember.u2attributesCount);
  }

  /**
   * Test {@link AttributeShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <p>Method under test: {@link AttributeShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName("Test visitProgramMember(ProgramClass, ProgramMember)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AttributeShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember2() {
    // Arrange
    AttributeShrinker attributeShrinker = new AttributeShrinker();
    ProgramClass programClass = new ProgramClass();
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};
    ProgramMethod programMember = new ProgramMethod(1, 1, 1, referencedClasses);

    // Act
    attributeShrinker.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    assertEquals(0, programMember.u2attributesCount);
  }

  /**
   * Test {@link AttributeShrinker#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link AttributeShrinker#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName("Test visitProgramMember(ProgramClass, ProgramMember); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AttributeShrinker.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_thenFirstElementIsNull() {
    // Arrange
    AttributeShrinker attributeShrinker = new AttributeShrinker();
    ProgramClass programClass = new ProgramClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    ProgramField programMember = new ProgramField(1, 1, 1, 1, attributes, new LibraryClass());

    // Act
    attributeShrinker.visitProgramMember(programClass, programMember);

    // Assert
    Attribute[] attributeArray = programMember.attributes;
    assertNull(attributeArray[0]);
    assertEquals(0, programMember.u2attributesCount);
    assertEquals(1, attributeArray.length);
  }

  /**
   * Test {@link AttributeShrinker#visitRecordAttribute(Clazz, RecordAttribute)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link AttributeShrinker#visitRecordAttribute(Clazz, RecordAttribute)}
   */
  @Test
  @DisplayName("Test visitRecordAttribute(Clazz, RecordAttribute); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AttributeShrinker.visitRecordAttribute(Clazz, RecordAttribute)"})
  void testVisitRecordAttribute_thenFirstElementIsNull() {
    // Arrange
    AttributeShrinker attributeShrinker = new AttributeShrinker();
    LibraryClass clazz = new LibraryClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    RecordComponentInfo[] components =
        new RecordComponentInfo[] {new RecordComponentInfo(1, 1, 1, attributes)};
    RecordAttribute recordAttribute = new RecordAttribute(1, 1, components);

    // Act
    attributeShrinker.visitRecordAttribute(clazz, recordAttribute);

    // Assert
    RecordComponentInfo[] recordComponentInfoArray = recordAttribute.components;
    RecordComponentInfo recordComponentInfo = recordComponentInfoArray[0];
    Attribute[] attributeArray = recordComponentInfo.attributes;
    assertNull(attributeArray[0]);
    assertEquals(0, recordComponentInfo.u2attributesCount);
    assertEquals(1, recordComponentInfoArray.length);
    assertEquals(1, attributeArray.length);
  }

  /**
   * Test {@link AttributeShrinker#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <ul>
   *   <li>Then first element {@link BootstrapMethodsAttribute}.
   * </ul>
   *
   * <p>Method under test: {@link AttributeShrinker#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRecordComponentInfo(Clazz, RecordComponentInfo); then first element BootstrapMethodsAttribute")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AttributeShrinker.visitRecordComponentInfo(Clazz, RecordComponentInfo)"})
  void testVisitRecordComponentInfo_thenFirstElementBootstrapMethodsAttribute() {
    // Arrange
    AttributeShrinker attributeShrinker = new AttributeShrinker();
    LibraryClass clazz = new LibraryClass();
    BootstrapMethodsAttribute bootstrapMethodsAttribute = new BootstrapMethodsAttribute();
    Attribute[] attributes = new Attribute[] {bootstrapMethodsAttribute};
    RecordComponentInfo recordComponentInfo = new RecordComponentInfo(1, 1, 0, attributes);

    // Act
    attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

    // Assert that nothing has changed
    Attribute[] attributeArray = recordComponentInfo.attributes;
    Attribute attribute = attributeArray[0];
    assertTrue(attribute instanceof BootstrapMethodsAttribute);
    assertEquals(0, recordComponentInfo.u2attributesCount);
    assertEquals(1, attributeArray.length);
    assertSame(bootstrapMethodsAttribute, attribute);
  }

  /**
   * Test {@link AttributeShrinker#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <ul>
   *   <li>Then first element is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link AttributeShrinker#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRecordComponentInfo(Clazz, RecordComponentInfo); then first element is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AttributeShrinker.visitRecordComponentInfo(Clazz, RecordComponentInfo)"})
  void testVisitRecordComponentInfo_thenFirstElementIsNull() {
    // Arrange
    AttributeShrinker attributeShrinker = new AttributeShrinker();
    LibraryClass clazz = new LibraryClass();
    Attribute[] attributes = new Attribute[] {new BootstrapMethodsAttribute()};
    RecordComponentInfo recordComponentInfo = new RecordComponentInfo(1, 1, 1, attributes);

    // Act
    attributeShrinker.visitRecordComponentInfo(clazz, recordComponentInfo);

    // Assert
    Attribute[] attributeArray = recordComponentInfo.attributes;
    assertNull(attributeArray[0]);
    assertEquals(0, recordComponentInfo.u2attributesCount);
    assertEquals(1, attributeArray.length);
  }
}
