package proguard.optimize.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeInfo;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;

class VariableOptimizerDiffblueTest {
  /**
   * Test {@link VariableOptimizer#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    variableOptimizer.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    LocalVariableInfo[] localVariableInfoArray = localVariableTableAttribute.localVariableTable;
    assertEquals(0, localVariableInfoArray[0].u2length);
    assertEquals(1, localVariableInfoArray.length);
  }

  /**
   * Test {@link VariableOptimizer#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute2() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo(1, 3, 1, 1, 1);
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {localVariableInfo};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    variableOptimizer.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    LocalVariableInfo[] localVariableInfoArray = localVariableTableAttribute.localVariableTable;
    assertEquals(0, localVariableInfoArray[0].u2length);
    assertEquals(1, localVariableInfoArray.length);
  }

  /**
   * Test {@link VariableOptimizer#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableTypeTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {new LocalVariableTypeInfo()};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    variableOptimizer.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert that nothing has changed
    LocalVariableTypeInfo[] localVariableTypeInfoArray =
        localVariableTypeTableAttribute.localVariableTypeTable;
    assertEquals(0, localVariableTypeInfoArray[0].u2length);
    assertEquals(1, localVariableTypeInfoArray.length);
  }

  /**
   * Test {@link VariableOptimizer#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableTypeTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute2() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo localVariableTypeInfo = new LocalVariableTypeInfo(1, 3, 1, 1, 1);
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {localVariableTypeInfo};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    variableOptimizer.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    LocalVariableTypeInfo[] localVariableTypeInfoArray =
        localVariableTypeTableAttribute.localVariableTypeTable;
    assertEquals(0, localVariableTypeInfoArray[0].u2length);
    assertEquals(1, localVariableTypeInfoArray.length);
  }

  /**
   * Test {@link VariableOptimizer#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableInfo(Clazz, Method,
   * CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName("Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo(1, 3, 1, 1, 1);

    // Act
    variableOptimizer.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

    // Assert
    assertEquals(0, localVariableInfo.u2length);
  }

  /**
   * Test {@link VariableOptimizer#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <ul>
   *   <li>Then {@link LocalVariableInfo#LocalVariableInfo()} {@link LocalVariableInfo#u2length} is
   *       zero.
   * </ul>
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableInfo(Clazz, Method,
   * CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo); then LocalVariableInfo() u2length is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo_thenLocalVariableInfoU2lengthIsZero() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo();

    // Act
    variableOptimizer.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableInfo);

    // Assert that nothing has changed
    assertEquals(0, localVariableInfo.u2length);
  }

  /**
   * Test {@link VariableOptimizer#visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute,
   * LocalVariableTypeInfo)}.
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableTypeInfo(Clazz, Method,
   * CodeAttribute, LocalVariableTypeInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo)"
  })
  void testVisitLocalVariableTypeInfo() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo localVariableTypeInfo = new LocalVariableTypeInfo(1, 3, 1, 1, 1);

    // Act
    variableOptimizer.visitLocalVariableTypeInfo(
        clazz, method, codeAttribute, localVariableTypeInfo);

    // Assert
    assertEquals(0, localVariableTypeInfo.u2length);
  }

  /**
   * Test {@link VariableOptimizer#visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute,
   * LocalVariableTypeInfo)}.
   *
   * <ul>
   *   <li>Then {@link LocalVariableTypeInfo#LocalVariableTypeInfo()} {@link
   *       LocalVariableTypeInfo#u2length} is zero.
   * </ul>
   *
   * <p>Method under test: {@link VariableOptimizer#visitLocalVariableTypeInfo(Clazz, Method,
   * CodeAttribute, LocalVariableTypeInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo); then LocalVariableTypeInfo() u2length is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableOptimizer.visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo)"
  })
  void testVisitLocalVariableTypeInfo_thenLocalVariableTypeInfoU2lengthIsZero() {
    // Arrange
    VariableOptimizer variableOptimizer = new VariableOptimizer(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo localVariableTypeInfo = new LocalVariableTypeInfo();

    // Act
    variableOptimizer.visitLocalVariableTypeInfo(
        clazz, method, codeAttribute, localVariableTypeInfo);

    // Assert that nothing has changed
    assertEquals(0, localVariableTypeInfo.u2length);
  }
}
