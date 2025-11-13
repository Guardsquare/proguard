package proguard.ant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.MemberSpecification;

class MemberSpecificationElementDiffblueTest {
  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   *   <li>Then {@link ArrayList#ArrayList()} size is two.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given '42'; when ArrayList() add '42'; then ArrayList() size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_given42_whenArrayListAdd42_thenArrayListSizeIsTwo() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();

    ArrayList<Object> memberSpecifications = new ArrayList<>();
    memberSpecifications.add("42");

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(2, memberSpecifications.size());
    Object getResult = memberSpecifications.get(1);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertNull(((MemberSpecification) getResult).attributeNames);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((MemberSpecification) getResult).requiredUnsetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElement() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Access is {@code bridge}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Access is 'bridge'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementAccessIsBridge() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("bridge");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(Double.SIZE, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Access is {@code ,}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Access is ','")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementAccessIsComma() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess(" ,");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Access is {@code ..}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Access is '..'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementAccessIsDotDot_whenFalse() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("..");
    memberSpecificationElement.setValues("..");
    memberSpecificationElement.setType("Type");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), false, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Access is {@code !}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Access is '!'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementAccessIsExclamationMark() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("!");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), true, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Access is {@code !}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Access is '!'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementAccessIsExclamationMark_whenFalse() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("!");
    memberSpecificationElement.setValues("..");
    memberSpecificationElement.setType("Type");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), false, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Access is {@code <init>}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Access is '<init>'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementAccessIsInit_thenThrowBuildException() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("<init>");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), true, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Access is {@code
   *       volatile}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Access is 'volatile'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementAccessIsVolatile() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("volatile");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(Double.SIZE, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Parameters is {@code
   *       Parameters}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Parameters is 'Parameters'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementParametersIsParameters_whenFalse() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setParameters("Parameters");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), false, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Parameters is {@code
   *       Parameters}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Parameters is 'Parameters'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementParametersIsParameters_whenFalse2() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setParameters("Parameters");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), true, false));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Type is {@code <init>}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Type is '<init>'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementTypeIsInit_thenThrowBuildException() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setType("<init>");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), true, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Type is {@code Type}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Type is 'Type'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementTypeIsType_whenFalse() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setType("Type");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), true, false));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Values is {@code 42}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Values is '42'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementValuesIs42_thenThrowBuildException() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setValues("42");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), true, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Values is {@code 42}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Values is '42'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementValuesIs42_whenFalse() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setValues("42");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), false, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Values is {@code 42}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Values is '42'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementValuesIs42_whenFalse2() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setValues("42");
    memberSpecificationElement.setType("Type");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), false, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor) Values is {@code ..}.
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor) Values is '..'; when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElementValuesIsDotDot_whenFalse() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setValues("..");
    memberSpecificationElement.setType("Type");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> memberSpecificationElement.appendTo(new ArrayList<>(), false, true));
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor).
   *   <li>Then {@link ArrayList#ArrayList()} first {@link MemberSpecification#name} is {@code
   *       null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor); then ArrayList() first name is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElement_thenArrayListFirstNameIsNull() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, false, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertNull(((MemberSpecification) getResult).name);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link MemberSpecificationElement} (default constructor).
   *   <li>Then {@link ArrayList#ArrayList()} first {@link MemberSpecification#name} is {@code
   *       null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); given MemberSpecificationElement (default constructor); then ArrayList() first name is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_givenMemberSpecificationElement_thenArrayListFirstNameIsNull2() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, false);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertNull(((MemberSpecification) getResult).name);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link MemberSpecification#annotationType} is
   *       {@code L<init>;}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first annotationType is 'L<init>;'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstAnnotationTypeIsLInit() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAnnotation("<init>");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertEquals("L<init>;", ((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link MemberSpecification#descriptor} is {@code
   *       (L<init>;)V}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first descriptor is '(L<init>;)V'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstDescriptorIsLInitV() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setParameters("<init>");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("(L<init>;)V", ((MemberSpecification) getResult).descriptor);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link MemberSpecification#descriptor} is {@code
   *       LType;}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first descriptor is 'LType;'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstDescriptorIsLType() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setType("Type");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, false, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("LType;", ((MemberSpecification) getResult).descriptor);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).name);
    assertEquals(0, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is {@code 1024}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is '1024'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIs1024() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("abstract");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(1024, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is eight.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is eight")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsEight() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("static");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(8, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is four.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsFour() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("protected");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(4, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is one.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsOne() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("public");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(1, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is one hundred twenty-eight.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is one hundred twenty-eight")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsOneHundredTwentyEight() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("transient");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(128, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is {@link Short#SIZE}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is SIZE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsSize() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("final");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(Short.SIZE, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is {@link Integer#SIZE}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is SIZE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsSize2() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("synchronized");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(Integer.SIZE, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       MemberSpecification#requiredSetAccessFlags} is two.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean, boolean); then ArrayList() first requiredSetAccessFlags is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.appendTo(List, boolean, boolean)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsTwo() {
    // Arrange
    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAccess("private");
    ArrayList<Object> memberSpecifications = new ArrayList<>();

    // Act
    memberSpecificationElement.appendTo(memberSpecifications, true, true);

    // Assert
    assertEquals(1, memberSpecifications.size());
    Object getResult = memberSpecifications.get(0);
    assertTrue(getResult instanceof MemberSpecification);
    assertEquals("<init>", ((MemberSpecification) getResult).name);
    assertNull(((MemberSpecification) getResult).annotationType);
    assertNull(((MemberSpecification) getResult).descriptor);
    assertEquals(2, ((MemberSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test new {@link MemberSpecificationElement} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link
   * MemberSpecificationElement}
   */
  @Test
  @DisplayName("Test new MemberSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberSpecificationElement.<init>()"})
  void testNewMemberSpecificationElement() {
    // Arrange and Act
    MemberSpecificationElement actualMemberSpecificationElement = new MemberSpecificationElement();

    // Assert
    Location location = actualMemberSpecificationElement.getLocation();
    assertNull(location.getFileName());
    assertNull(actualMemberSpecificationElement.getDescription());
    assertNull(actualMemberSpecificationElement.getProject());
    assertNull(actualMemberSpecificationElement.getRefid());
    assertEquals(0, location.getColumnNumber());
    assertEquals(0, location.getLineNumber());
    assertFalse(actualMemberSpecificationElement.isReference());
  }
}
