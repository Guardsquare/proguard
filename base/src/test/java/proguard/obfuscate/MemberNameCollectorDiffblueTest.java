package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.Member;
import proguard.util.SimpleProcessable;

class MemberNameCollectorDiffblueTest {
  /**
   * Test {@link MemberNameCollector#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberNameCollector#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCollector.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember() {
    // Arrange
    MemberNameCollector memberNameCollector = new MemberNameCollector(false, new HashMap<>());
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
    memberNameCollector.visitAnyMember(clazz, member);

    // Assert
    assertSame(libraryField2, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCollector#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberNameCollector#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCollector.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember2() {
    // Arrange
    MemberNameCollector memberNameCollector = new MemberNameCollector(false, new HashMap<>());
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
    memberNameCollector.visitAnyMember(clazz, member);

    // Assert that nothing has changed
    assertSame(simpleProcessable, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCollector#visitAnyMember(Clazz, Member)}.
   *
   * <p>Method under test: {@link MemberNameCollector#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCollector.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember3() {
    // Arrange
    MemberNameCollector memberNameCollector = new MemberNameCollector(false, new HashMap<>());
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
    memberNameCollector.visitAnyMember(clazz, member);

    // Assert
    assertSame(libraryField, member.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCollector#visitAnyMember(Clazz, Member)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCollector#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMember(Clazz, Member); when LibraryField(); then LibraryField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCollector.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember_whenLibraryField_thenLibraryFieldProcessingInfoIsNull() {
    // Arrange
    MemberNameCollector memberNameCollector = new MemberNameCollector(true, new HashMap<>());
    LibraryClass clazz = new LibraryClass();
    LibraryField member = new LibraryField();

    // Act
    memberNameCollector.visitAnyMember(clazz, member);

    // Assert that nothing has changed
    assertNull(member.getProcessingInfo());
  }
}
