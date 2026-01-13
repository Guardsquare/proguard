package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeInfo;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;

class LocalVariableTypeUsageMarkerDiffblueTest {
  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link
   * LocalVariableTypeUsageMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute() {
    // Arrange
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTableAttribute localVariableTableAttribute = new LocalVariableTableAttribute();

    // Act
    localVariableTypeUsageMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    assertNull(localVariableTableAttribute.getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link
   * LocalVariableTypeUsageMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute2() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    localVariableTypeUsageMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    LocalVariableInfo[] localVariableInfoArray = localVariableTableAttribute.localVariableTable;
    assertEquals(1, localVariableInfoArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, localVariableTableAttribute.getProcessingInfo());
    assertSame(shortestUsageMark, localVariableInfoArray[0].getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}.
   *
   * <ul>
   *   <li>Then array length is one.
   * </ul>
   *
   * <p>Method under test: {@link
   * LocalVariableTypeUsageMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute); then array length is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute_thenArrayLengthIsOne() {
    // Arrange
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    localVariableTypeUsageMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    assertEquals(1, localVariableTableAttribute.localVariableTable.length);
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link
   * LocalVariableTypeUsageMarker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute() {
    // Arrange
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute();

    // Act
    localVariableTypeUsageMarker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert that nothing has changed
    assertNull(localVariableTypeTableAttribute.getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link
   * LocalVariableTypeUsageMarker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute2() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {new LocalVariableTypeInfo()};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    localVariableTypeUsageMarker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    LocalVariableTypeInfo[] localVariableTypeInfoArray =
        localVariableTypeTableAttribute.localVariableTypeTable;
    assertEquals(1, localVariableTypeInfoArray.length);
    ShortestUsageMark shortestUsageMark = usageMarker.currentUsageMark;
    assertSame(shortestUsageMark, localVariableTypeTableAttribute.getProcessingInfo());
    assertSame(shortestUsageMark, localVariableTypeInfoArray[0].getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <ul>
   *   <li>Then array length is one.
   * </ul>
   *
   * <p>Method under test: {@link
   * LocalVariableTypeUsageMarker#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute); then array length is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute_thenArrayLengthIsOne() {
    // Arrange
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ClassUsageMarker());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {new LocalVariableTypeInfo()};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    localVariableTypeUsageMarker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    assertEquals(1, localVariableTypeTableAttribute.localVariableTypeTable.length);
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <p>Method under test: {@link LocalVariableTypeUsageMarker#visitLocalVariableInfo(Clazz, Method,
   * CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName("Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo();

    // Act
    localVariableTypeUsageMarker.visitLocalVariableInfo(
        clazz, method, codeAttribute, localVariableInfo);

    // Assert
    assertSame(usageMarker.currentUsageMark, localVariableInfo.getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <p>Method under test: {@link LocalVariableTypeUsageMarker#visitLocalVariableInfo(Clazz, Method,
   * CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName("Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo2() {
    // Arrange
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ClassUsageMarker());
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo(1, 3, 1, 1, 1);
    localVariableInfo.referencedClass = new ProgramClass();

    // Act
    localVariableTypeUsageMarker.visitLocalVariableInfo(
        null, method, codeAttribute, localVariableInfo);

    // Assert that nothing has changed
    assertNull(localVariableInfo.getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <p>Method under test: {@link LocalVariableTypeUsageMarker#visitLocalVariableInfo(Clazz, Method,
   * CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName("Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo3() {
    // Arrange
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(
            new ShortestClassUsageMarker(new ShortestUsageMarker(), "Just cause"));
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo(1, 3, 1, 1, 1);
    localVariableInfo.referencedClass = new ProgramClass();

    // Act
    localVariableTypeUsageMarker.visitLocalVariableInfo(
        null, method, codeAttribute, localVariableInfo);

    // Assert that nothing has changed
    assertNull(localVariableInfo.getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitLocalVariableTypeInfo(Clazz, Method,
   * CodeAttribute, LocalVariableTypeInfo)}.
   *
   * <p>Method under test: {@link LocalVariableTypeUsageMarker#visitLocalVariableTypeInfo(Clazz,
   * Method, CodeAttribute, LocalVariableTypeInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LocalVariableTypeUsageMarker.visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo)"
  })
  void testVisitLocalVariableTypeInfo() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo localVariableTypeInfo = new LocalVariableTypeInfo();

    // Act
    localVariableTypeUsageMarker.visitLocalVariableTypeInfo(
        clazz, method, codeAttribute, localVariableTypeInfo);

    // Assert
    assertSame(usageMarker.currentUsageMark, localVariableTypeInfo.getProcessingInfo());
  }

  /**
   * Test {@link LocalVariableTypeUsageMarker#visitAnyConstant(Clazz, Constant)}.
   *
   * <p>Method under test: {@link LocalVariableTypeUsageMarker#visitAnyConstant(Clazz, Constant)}
   */
  @Test
  @DisplayName("Test visitAnyConstant(Clazz, Constant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LocalVariableTypeUsageMarker.visitAnyConstant(Clazz, Constant)"})
  void testVisitAnyConstant() {
    // Arrange
    ShortestUsageMarker usageMarker = new ShortestUsageMarker();
    LocalVariableTypeUsageMarker localVariableTypeUsageMarker =
        new LocalVariableTypeUsageMarker(new ShortestClassUsageMarker(usageMarker, "Just cause"));
    LibraryClass clazz = new LibraryClass();
    ClassConstant constant = new ClassConstant();

    // Act
    localVariableTypeUsageMarker.visitAnyConstant(clazz, constant);

    // Assert
    assertSame(usageMarker.currentUsageMark, constant.getProcessingInfo());
  }
}
