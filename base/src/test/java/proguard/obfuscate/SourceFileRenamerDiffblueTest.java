package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.SourceDirAttribute;
import proguard.classfile.attribute.SourceFileAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

class SourceFileRenamerDiffblueTest {
  /**
   * Test {@link SourceFileRenamer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#attributesAccept(AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SourceFileRenamer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then calls attributesAccept(AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SourceFileRenamer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsAttributesAccept() {
    // Arrange
    SourceFileRenamer sourceFileRenamer = new SourceFileRenamer("New Source File Attribute");

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).attributesAccept(Mockito.<AttributeVisitor>any());

    // Act
    sourceFileRenamer.visitProgramClass(programClass);

    // Assert
    verify(programClass).attributesAccept(isA(AttributeVisitor.class));
  }

  /**
   * Test {@link SourceFileRenamer#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link Utf8Constant}.
   * </ul>
   *
   * <p>Method under test: {@link SourceFileRenamer#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then first element Utf8Constant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SourceFileRenamer.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenFirstElementUtf8Constant()
      throws UnsupportedEncodingException {
    // Arrange
    SourceFileRenamer sourceFileRenamer = new SourceFileRenamer("New Source File Attribute");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 0, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    sourceFileRenamer.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof Utf8Constant);
    assertEquals("New Source File Attribute", ((Utf8Constant) constant).getString());
    assertEquals(0, sourceFileAttribute.u2sourceFileIndex);
    assertEquals(1, constant.getTag());
    assertEquals(1, constantArray.length);
    assertEquals(1, clazz.u2constantPoolCount);
    assertFalse(constant.isCategory2());
    assertArrayEquals(
        "New Source File Attribute".getBytes("UTF-8"), ((Utf8Constant) constant).getBytes());
  }

  /**
   * Test {@link SourceFileRenamer#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then second element {@link Utf8Constant}.
   * </ul>
   *
   * <p>Method under test: {@link SourceFileRenamer#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then second element Utf8Constant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SourceFileRenamer.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenSecondElementUtf8Constant()
      throws UnsupportedEncodingException {
    // Arrange
    SourceFileRenamer sourceFileRenamer = new SourceFileRenamer("New Source File Attribute");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 1, constantPool, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    sourceFileRenamer.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof Utf8Constant);
    assertEquals("New Source File Attribute", ((Utf8Constant) constant).getString());
    assertNull(constant.getProcessingInfo());
    assertNull(constantArray[2]);
    assertEquals(0, constant.getProcessingFlags());
    assertEquals(1, constant.getTag());
    assertEquals(1, sourceFileAttribute.u2sourceFileIndex);
    assertEquals(17, constantArray.length);
    assertEquals(2, clazz.u2constantPoolCount);
    assertFalse(constant.isCategory2());
    assertArrayEquals(
        "New Source File Attribute".getBytes("UTF-8"), ((Utf8Constant) constant).getBytes());
  }

  /**
   * Test {@link SourceFileRenamer#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
   *
   * <ul>
   *   <li>Then third element {@link Utf8Constant}.
   * </ul>
   *
   * <p>Method under test: {@link SourceFileRenamer#visitSourceFileAttribute(Clazz,
   * SourceFileAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceFileAttribute(Clazz, SourceFileAttribute); then third element Utf8Constant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SourceFileRenamer.visitSourceFileAttribute(Clazz, SourceFileAttribute)"})
  void testVisitSourceFileAttribute_thenThirdElementUtf8Constant()
      throws UnsupportedEncodingException {
    // Arrange
    SourceFileRenamer sourceFileRenamer = new SourceFileRenamer("New Source File Attribute");
    ClassConstant classConstant = new ClassConstant();
    ProgramClass clazz =
        new ProgramClass(1, 2, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1);
    SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

    // Act
    sourceFileRenamer.visitSourceFileAttribute(clazz, sourceFileAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[2];
    assertTrue(constant instanceof Utf8Constant);
    assertEquals("New Source File Attribute", ((Utf8Constant) constant).getString());
    assertNull(constant.getProcessingInfo());
    assertNull(constantArray[17]);
    assertEquals(0, constant.getProcessingFlags());
    assertEquals(1, constant.getTag());
    assertEquals(18, constantArray.length);
    assertEquals(2, sourceFileAttribute.u2sourceFileIndex);
    assertEquals(3, clazz.u2constantPoolCount);
    assertFalse(constant.isCategory2());
    assertArrayEquals(
        "New Source File Attribute".getBytes("UTF-8"), ((Utf8Constant) constant).getBytes());
  }

  /**
   * Test {@link SourceFileRenamer#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link Utf8Constant}.
   * </ul>
   *
   * <p>Method under test: {@link SourceFileRenamer#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then first element Utf8Constant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SourceFileRenamer.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenFirstElementUtf8Constant()
      throws UnsupportedEncodingException {
    // Arrange
    SourceFileRenamer sourceFileRenamer = new SourceFileRenamer("New Source File Attribute");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 0, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    sourceFileRenamer.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof Utf8Constant);
    assertEquals("New Source File Attribute", ((Utf8Constant) constant).getString());
    assertEquals(0, sourceDirAttribute.u2sourceDirIndex);
    assertEquals(1, constant.getTag());
    assertEquals(1, constantArray.length);
    assertEquals(1, clazz.u2constantPoolCount);
    assertFalse(constant.isCategory2());
    assertArrayEquals(
        "New Source File Attribute".getBytes("UTF-8"), ((Utf8Constant) constant).getBytes());
  }

  /**
   * Test {@link SourceFileRenamer#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then second element {@link Utf8Constant}.
   * </ul>
   *
   * <p>Method under test: {@link SourceFileRenamer#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then second element Utf8Constant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SourceFileRenamer.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenSecondElementUtf8Constant()
      throws UnsupportedEncodingException {
    // Arrange
    SourceFileRenamer sourceFileRenamer = new SourceFileRenamer("New Source File Attribute");
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 1, constantPool, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    sourceFileRenamer.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof Utf8Constant);
    assertEquals("New Source File Attribute", ((Utf8Constant) constant).getString());
    assertNull(constant.getProcessingInfo());
    assertNull(constantArray[2]);
    assertEquals(0, constant.getProcessingFlags());
    assertEquals(1, constant.getTag());
    assertEquals(1, sourceDirAttribute.u2sourceDirIndex);
    assertEquals(17, constantArray.length);
    assertEquals(2, clazz.u2constantPoolCount);
    assertFalse(constant.isCategory2());
    assertArrayEquals(
        "New Source File Attribute".getBytes("UTF-8"), ((Utf8Constant) constant).getBytes());
  }

  /**
   * Test {@link SourceFileRenamer#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
   *
   * <ul>
   *   <li>Then third element {@link Utf8Constant}.
   * </ul>
   *
   * <p>Method under test: {@link SourceFileRenamer#visitSourceDirAttribute(Clazz,
   * SourceDirAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSourceDirAttribute(Clazz, SourceDirAttribute); then third element Utf8Constant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SourceFileRenamer.visitSourceDirAttribute(Clazz, SourceDirAttribute)"})
  void testVisitSourceDirAttribute_thenThirdElementUtf8Constant()
      throws UnsupportedEncodingException {
    // Arrange
    SourceFileRenamer sourceFileRenamer = new SourceFileRenamer("New Source File Attribute");
    ClassConstant classConstant = new ClassConstant();
    ProgramClass clazz =
        new ProgramClass(1, 2, new Constant[] {classConstant, new ClassConstant()}, 1, 1, 1);
    SourceDirAttribute sourceDirAttribute = new SourceDirAttribute();

    // Act
    sourceFileRenamer.visitSourceDirAttribute(clazz, sourceDirAttribute);

    // Assert
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[2];
    assertTrue(constant instanceof Utf8Constant);
    assertEquals("New Source File Attribute", ((Utf8Constant) constant).getString());
    assertNull(constant.getProcessingInfo());
    assertNull(constantArray[17]);
    assertEquals(0, constant.getProcessingFlags());
    assertEquals(1, constant.getTag());
    assertEquals(18, constantArray.length);
    assertEquals(2, sourceDirAttribute.u2sourceDirIndex);
    assertEquals(3, clazz.u2constantPoolCount);
    assertFalse(constant.isCategory2());
    assertArrayEquals(
        "New Source File Attribute".getBytes("UTF-8"), ((Utf8Constant) constant).getBytes());
  }
}
