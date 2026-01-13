package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.Member;
import proguard.classfile.ProgramField;
import proguard.util.SimpleFeatureNamedProcessable;
import proguard.util.SimpleProcessable;

class MemberObfuscatorDiffblueTest {
  /**
   * Test {@link MemberObfuscator#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember() {
    // Arrange
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(false, nameFactory, new HashMap<>());
    LibraryClass clazz = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    LibraryField libraryField2 = new LibraryField();
    libraryField.setProcessingInfo(libraryField2);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(libraryField18);

    // Act
    memberObfuscator.visitAnyMember(clazz, member);

    // Assert
    assertSame(libraryField2, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember2() {
    // Arrange
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());
    LibraryClass clazz =
        new LibraryClass(1, "java/lang/annotation/Annotation", "java/lang/annotation/Annotation");
    LibraryField member =
        new LibraryField(1, "java/lang/annotation/Annotation", "java/lang/annotation/Annotation");

    // Act
    memberObfuscator.visitAnyMember(clazz, member);

    // Assert
    assertEquals("1", member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember3() {
    // Arrange
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(true, nameFactory, new HashMap<>());
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "java/lang/annotation/Annotation");
    LibraryField member =
        new LibraryField(1, "java/lang/annotation/Annotation", "java/lang/annotation/Annotation");

    // Act
    memberObfuscator.visitAnyMember(clazz, member);

    // Assert
    assertEquals("1", member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember4() {
    // Arrange
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(false, nameFactory, new HashMap<>());
    LibraryClass clazz = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new LibraryField());

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    SimpleProcessable simpleProcessable = new SimpleProcessable();
    simpleProcessable.setProcessingInfo(libraryField16);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(simpleProcessable);

    // Act
    memberObfuscator.visitAnyMember(clazz, member);

    // Assert that nothing has changed
    assertSame(simpleProcessable, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember5() {
    // Arrange
    NumericNameFactory nameFactory = new NumericNameFactory();
    MemberObfuscator memberObfuscator = new MemberObfuscator(false, nameFactory, new HashMap<>());
    LibraryClass clazz = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo("Processing Info");

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(libraryField17);

    // Act
    memberObfuscator.visitAnyMember(clazz, member);

    // Assert
    Object processingInfo = member.getProcessingInfo();
    assertTrue(processingInfo instanceof LibraryField);
    assertEquals("Processing Info", ((LibraryField) processingInfo).getProcessingInfo());
    assertSame(libraryField, processingInfo);
  }

  /**
   * Test {@link MemberObfuscator#retrieveNameMap(Map, String)}.
   *
   * <p>Method under test: {@link MemberObfuscator#retrieveNameMap(Map, String)}
   */
  @Test
  @DisplayName("Test retrieveNameMap(Map, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Map MemberObfuscator.retrieveNameMap(Map, String)"})
  void testRetrieveNameMap() {
    // Arrange
    HashMap<Object, Object> descriptorMap = new HashMap<>();

    // Act
    Map actualRetrieveNameMapResult = MemberObfuscator.retrieveNameMap(descriptorMap, "Descriptor");

    // Assert
    assertEquals(1, descriptorMap.size());
    assertTrue(descriptorMap.containsKey("Descriptor"));
    assertTrue(actualRetrieveNameMapResult.isEmpty());
  }

  /**
   * Test {@link MemberObfuscator#setFixedNewMemberName(Member, String)}.
   *
   * <p>Method under test: {@link MemberObfuscator#setFixedNewMemberName(Member, String)}
   */
  @Test
  @DisplayName("Test setFixedNewMemberName(Member, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setFixedNewMemberName(Member, String)"})
  void testSetFixedNewMemberName() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    LibraryField libraryField2 = new LibraryField();
    libraryField.setProcessingInfo(libraryField2);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(libraryField18);

    // Act
    MemberObfuscator.setFixedNewMemberName(member, "Name");

    // Assert
    assertSame(libraryField2, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#setFixedNewMemberName(Member, String)}.
   *
   * <p>Method under test: {@link MemberObfuscator#setFixedNewMemberName(Member, String)}
   */
  @Test
  @DisplayName("Test setFixedNewMemberName(Member, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setFixedNewMemberName(Member, String)"})
  void testSetFixedNewMemberName2() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new LibraryField());

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    SimpleProcessable simpleProcessable = new SimpleProcessable();
    simpleProcessable.setProcessingInfo(libraryField16);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(simpleProcessable);

    // Act
    MemberObfuscator.setFixedNewMemberName(member, "Name");

    // Assert that nothing has changed
    assertSame(simpleProcessable, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#setFixedNewMemberName(Member, String)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#setFixedNewMemberName(Member, String)}
   */
  @Test
  @DisplayName("Test setFixedNewMemberName(Member, String); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setFixedNewMemberName(Member, String)"})
  void testSetFixedNewMemberName_givenProcessingInfo() {
    // Arrange
    LibraryField member = new LibraryField();
    member.setProcessingInfo("Processing Info");

    // Act
    MemberObfuscator.setFixedNewMemberName(member, "Name");

    // Assert
    assertEquals("Name", member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#setFixedNewMemberName(Member, String)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#setFixedNewMemberName(Member, String)}
   */
  @Test
  @DisplayName(
      "Test setFixedNewMemberName(Member, String); when LibraryField(); then LibraryField() ProcessingInfo is 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setFixedNewMemberName(Member, String)"})
  void testSetFixedNewMemberName_whenLibraryField_thenLibraryFieldProcessingInfoIsName() {
    // Arrange
    LibraryField member = new LibraryField();

    // Act
    MemberObfuscator.setFixedNewMemberName(member, "Name");

    // Assert
    assertEquals("Name", member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#setNewMemberName(Member, String)}.
   *
   * <p>Method under test: {@link MemberObfuscator#setNewMemberName(Member, String)}
   */
  @Test
  @DisplayName("Test setNewMemberName(Member, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setNewMemberName(Member, String)"})
  void testSetNewMemberName() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    LibraryField libraryField2 = new LibraryField();
    libraryField.setProcessingInfo(libraryField2);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(libraryField18);

    // Act
    MemberObfuscator.setNewMemberName(member, "Name");

    // Assert
    assertSame(libraryField2, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#setNewMemberName(Member, String)}.
   *
   * <p>Method under test: {@link MemberObfuscator#setNewMemberName(Member, String)}
   */
  @Test
  @DisplayName("Test setNewMemberName(Member, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setNewMemberName(Member, String)"})
  void testSetNewMemberName2() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new LibraryField());

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    SimpleFeatureNamedProcessable simpleFeatureNamedProcessable =
        new SimpleFeatureNamedProcessable();
    simpleFeatureNamedProcessable.setProcessingInfo(libraryField16);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(simpleFeatureNamedProcessable);

    // Act
    MemberObfuscator.setNewMemberName(member, "Name");

    // Assert that nothing has changed
    assertSame(simpleFeatureNamedProcessable, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#setNewMemberName(Member, String)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#setNewMemberName(Member, String)}
   */
  @Test
  @DisplayName("Test setNewMemberName(Member, String); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setNewMemberName(Member, String)"})
  void testSetNewMemberName_givenProcessingInfo() {
    // Arrange
    LibraryField member = new LibraryField();
    member.setProcessingInfo("Processing Info");

    // Act
    MemberObfuscator.setNewMemberName(member, "Name");

    // Assert
    assertEquals("Name", member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#setNewMemberName(Member, String)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#setNewMemberName(Member, String)}
   */
  @Test
  @DisplayName(
      "Test setNewMemberName(Member, String); when LibraryField(); then LibraryField() ProcessingInfo is 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberObfuscator.setNewMemberName(Member, String)"})
  void testSetNewMemberName_whenLibraryField_thenLibraryFieldProcessingInfoIsName() {
    // Arrange
    LibraryField member = new LibraryField();

    // Act
    MemberObfuscator.setNewMemberName(member, "Name");

    // Assert
    assertEquals("Name", member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#hasFixedNewMemberName(Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#hasFixedNewMemberName(Member)}
   */
  @Test
  @DisplayName("Test hasFixedNewMemberName(Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MemberObfuscator.hasFixedNewMemberName(Member)"})
  void testHasFixedNewMemberName() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    LibraryField libraryField2 = new LibraryField();
    libraryField.setProcessingInfo(libraryField2);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(libraryField18);

    // Act
    MemberObfuscator.hasFixedNewMemberName(member);

    // Assert
    assertSame(libraryField2, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#hasFixedNewMemberName(Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#hasFixedNewMemberName(Member)}
   */
  @Test
  @DisplayName("Test hasFixedNewMemberName(Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MemberObfuscator.hasFixedNewMemberName(Member)"})
  void testHasFixedNewMemberName2() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new LibraryField());

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    SimpleProcessable simpleProcessable = new SimpleProcessable();
    simpleProcessable.setProcessingInfo(libraryField16);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(simpleProcessable);

    // Act and Assert
    assertTrue(MemberObfuscator.hasFixedNewMemberName(member));
    assertSame(simpleProcessable, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#hasFixedNewMemberName(Member)}.
   *
   * <ul>
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#hasFixedNewMemberName(Member)}
   */
  @Test
  @DisplayName(
      "Test hasFixedNewMemberName(Member); then LibraryField() ProcessingInfo is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MemberObfuscator.hasFixedNewMemberName(Member)"})
  void testHasFixedNewMemberName_thenLibraryFieldProcessingInfoIsProcessingInfo() {
    // Arrange
    LibraryField member = new LibraryField();
    member.setProcessingInfo("Processing Info");

    // Act
    boolean actualHasFixedNewMemberNameResult = MemberObfuscator.hasFixedNewMemberName(member);

    // Assert
    assertEquals("Processing Info", member.getProcessingInfo());
    assertTrue(actualHasFixedNewMemberNameResult);
  }

  /**
   * Test {@link MemberObfuscator#hasFixedNewMemberName(Member)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#hasFixedNewMemberName(Member)}
   */
  @Test
  @DisplayName(
      "Test hasFixedNewMemberName(Member); when LibraryField(); then LibraryField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MemberObfuscator.hasFixedNewMemberName(Member)"})
  void testHasFixedNewMemberName_whenLibraryField_thenLibraryFieldProcessingInfoIsNull() {
    // Arrange
    LibraryField member = new LibraryField();

    // Act
    boolean actualHasFixedNewMemberNameResult = MemberObfuscator.hasFixedNewMemberName(member);

    // Assert
    assertNull(member.getProcessingInfo());
    assertTrue(actualHasFixedNewMemberNameResult);
  }

  /**
   * Test {@link MemberObfuscator#hasFixedNewMemberName(Member)}.
   *
   * <ul>
   *   <li>When {@link ProgramField#ProgramField()}.
   *   <li>Then {@link ProgramField#ProgramField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#hasFixedNewMemberName(Member)}
   */
  @Test
  @DisplayName(
      "Test hasFixedNewMemberName(Member); when ProgramField(); then ProgramField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MemberObfuscator.hasFixedNewMemberName(Member)"})
  void testHasFixedNewMemberName_whenProgramField_thenProgramFieldProcessingInfoIsNull() {
    // Arrange
    ProgramField member = new ProgramField();

    // Act
    boolean actualHasFixedNewMemberNameResult = MemberObfuscator.hasFixedNewMemberName(member);

    // Assert
    assertNull(member.getProcessingInfo());
    assertFalse(actualHasFixedNewMemberNameResult);
  }

  /**
   * Test {@link MemberObfuscator#newMemberName(Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#newMemberName(Member)}
   */
  @Test
  @DisplayName("Test newMemberName(Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String MemberObfuscator.newMemberName(Member)"})
  void testNewMemberName() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    LibraryField libraryField2 = new LibraryField();
    libraryField.setProcessingInfo(libraryField2);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(libraryField18);

    // Act
    MemberObfuscator.newMemberName(member);

    // Assert
    assertSame(libraryField2, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#newMemberName(Member)}.
   *
   * <p>Method under test: {@link MemberObfuscator#newMemberName(Member)}
   */
  @Test
  @DisplayName("Test newMemberName(Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String MemberObfuscator.newMemberName(Member)"})
  void testNewMemberName2() {
    // Arrange
    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new LibraryField());

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    SimpleProcessable simpleProcessable = new SimpleProcessable();
    simpleProcessable.setProcessingInfo(libraryField16);

    LibraryField member = new LibraryField(1, "Name", "Descriptor");
    member.setProcessingInfo(simpleProcessable);

    // Act and Assert
    assertNull(MemberObfuscator.newMemberName(member));
    assertSame(simpleProcessable, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#newMemberName(Member)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   *   <li>Then return {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#newMemberName(Member)}
   */
  @Test
  @DisplayName("Test newMemberName(Member); given 'Processing Info'; then return 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String MemberObfuscator.newMemberName(Member)"})
  void testNewMemberName_givenProcessingInfo_thenReturnProcessingInfo() {
    // Arrange
    LibraryField member = new LibraryField();
    member.setProcessingInfo("Processing Info");

    // Act and Assert
    assertEquals("Processing Info", MemberObfuscator.newMemberName(member));
    assertEquals("Processing Info", member.getProcessingInfo());
  }

  /**
   * Test {@link MemberObfuscator#newMemberName(Member)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberObfuscator#newMemberName(Member)}
   */
  @Test
  @DisplayName(
      "Test newMemberName(Member); when LibraryField(); then LibraryField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String MemberObfuscator.newMemberName(Member)"})
  void testNewMemberName_whenLibraryField_thenLibraryFieldProcessingInfoIsNull() {
    // Arrange
    LibraryField member = new LibraryField();

    // Act
    String actualNewMemberNameResult = MemberObfuscator.newMemberName(member);

    // Assert
    assertNull(member.getProcessingInfo());
    assertNull(actualNewMemberNameResult);
  }
}
