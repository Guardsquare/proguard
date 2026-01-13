package proguard.ant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.ClassSpecification;
import proguard.MemberSpecification;

@ExtendWith(MockitoExtension.class)
class ClassSpecificationElementDiffblueTest {
  @InjectMocks private ClassSpecificationElement classSpecificationElement;

  @Mock private List list;

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   *   <li>Then {@link ArrayList#ArrayList()} size is two.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given '42'; when ArrayList() add '42'; then ArrayList() size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_given42_whenArrayListAdd42_thenArrayListSizeIsTwo() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ArrayList<Object> classSpecifications = new ArrayList<>();
    classSpecifications.add("42");

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(2, classSpecifications.size());
    Object getResult = classSpecifications.get(1);
    assertTrue(getResult instanceof ClassSpecification);
    assertNull(((ClassSpecification) getResult).annotationType);
    assertNull(((ClassSpecification) getResult).className);
    assertNull(((ClassSpecification) getResult).comments);
    assertNull(((ClassSpecification) getResult).extendsAnnotationType);
    assertNull(((ClassSpecification) getResult).extendsClassName);
    assertNull(((ClassSpecification) getResult).memberComments);
    assertNull(((ClassSpecification) getResult).attributeNames);
    assertNull(((ClassSpecification) getResult).fieldSpecifications);
    assertNull(((ClassSpecification) getResult).methodSpecifications);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor) Access is {@code Access}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given ClassSpecificationElement (default constructor) Access is 'Access'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_givenClassSpecificationElementAccessIsAccess() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess("Access");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());

    // Act and Assert
    assertThrows(BuildException.class, () -> classSpecificationElement.appendTo(new ArrayList<>()));
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor) Access is {@code ,}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given ClassSpecificationElement (default constructor) Access is ','")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_givenClassSpecificationElementAccessIsComma() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess(" ,");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor) Access is {@code !}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given ClassSpecificationElement (default constructor) Access is '!'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_givenClassSpecificationElementAccessIsExclamationMark() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess("!");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());

    // Act and Assert
    assertThrows(BuildException.class, () -> classSpecificationElement.appendTo(new ArrayList<>()));
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor) Name is {@code *}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given ClassSpecificationElement (default constructor) Name is '*'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_givenClassSpecificationElementNameIsAsterisk() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("*");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor) Type is {@code class}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given ClassSpecificationElement (default constructor) Type is 'class'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_givenClassSpecificationElementTypeIsClass() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setType("class");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor) Type is {@code !}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given ClassSpecificationElement (default constructor) Type is '!'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_givenClassSpecificationElementTypeIsExclamationMark() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setType("!");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());

    // Act and Assert
    assertThrows(BuildException.class, () -> classSpecificationElement.appendTo(new ArrayList<>()));
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor) Type is {@code Type}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); given ClassSpecificationElement (default constructor) Type is 'Type'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_givenClassSpecificationElementTypeIsType_thenThrowBuildException() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setType("Type");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());

    // Act and Assert
    assertThrows(BuildException.class, () -> classSpecificationElement.appendTo(new ArrayList<>()));
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link ClassSpecification#annotationType} is
   *       {@code LAnnotation;}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first annotationType is 'LAnnotation;'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstAnnotationTypeIsLAnnotation() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAnnotation("Annotation");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    assertEquals("LAnnotation;", ((ClassSpecification) getResult).annotationType);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link ClassSpecification#annotationType} is
   *       {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first annotationType is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstAnnotationTypeIsNull() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    assertNull(((ClassSpecification) getResult).annotationType);
    assertNull(((ClassSpecification) getResult).className);
    assertNull(((ClassSpecification) getResult).extendsAnnotationType);
    assertNull(((ClassSpecification) getResult).extendsClassName);
    assertNull(((ClassSpecification) getResult).fieldSpecifications);
    assertNull(((ClassSpecification) getResult).methodSpecifications);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link ClassSpecification#className} is {@code
   *       Name}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first className is 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstClassNameIsName() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    assertEquals("Name", ((ClassSpecification) getResult).className);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link ClassSpecification#extendsAnnotationType}
   *       is {@code LExtends Annotation;}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); then ArrayList() first extendsAnnotationType is 'LExtends Annotation;'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstExtendsAnnotationTypeIsLExtendsAnnotation() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtendsannotation("Extends Annotation");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    assertEquals("LExtends Annotation;", ((ClassSpecification) getResult).extendsAnnotationType);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link ClassSpecification#extendsClassName} is
   *       {@code Extends}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first extendsClassName is 'Extends'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstExtendsClassNameIsExtends() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtends("Extends ");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    assertEquals("Extends ", ((ClassSpecification) getResult).extendsClassName);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link ClassSpecification#methodSpecifications}
   *       size is one.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first methodSpecifications size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstMethodSpecificationsSizeIsOne() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.addConfiguredMethod(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).methodSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is {@code 1024}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredSetAccessFlags is '1024'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIs1024() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess("abstract");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(1024, ((ClassSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is {@code 4096}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredSetAccessFlags is '4096'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIs4096() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess("synthetic");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(4096, ((ClassSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is {@code 8192}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredSetAccessFlags is '8192'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIs8192() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess("@");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(8192, ((ClassSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is {@code 16384}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredSetAccessFlags is '16384'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIs16384() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setType("enum");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(16384, ((ClassSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is five hundred twelve.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); then ArrayList() first requiredSetAccessFlags is five hundred twelve")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsFiveHundredTwelve() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setType("interface");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(512, ((ClassSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is one.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredSetAccessFlags is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsOne() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess("public");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(1, ((ClassSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is {@link Short#SIZE}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredSetAccessFlags is SIZE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsSize() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAccess("final");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(Short.SIZE, ((ClassSpecification) getResult).requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredSetAccessFlags} is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredSetAccessFlags is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredSetAccessFlagsIsZero() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredUnsetAccessFlags} is {@code 16384}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName("Test appendTo(List); then ArrayList() first requiredUnsetAccessFlags is '16384'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredUnsetAccessFlagsIs16384() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setType("!enum");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(16384, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#appendTo(List)}.
   *
   * <ul>
   *   <li>Then {@link ArrayList#ArrayList()} first {@link
   *       ClassSpecification#requiredUnsetAccessFlags} is five hundred twelve.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecificationElement#appendTo(List)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List); then ArrayList() first requiredUnsetAccessFlags is five hundred twelve")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.appendTo(List)"})
  void testAppendTo_thenArrayListFirstRequiredUnsetAccessFlagsIsFiveHundredTwelve() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setType("!interface");
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());
    ArrayList<Object> classSpecifications = new ArrayList<>();

    // Act
    classSpecificationElement.appendTo(classSpecifications);

    // Assert
    assertEquals(1, classSpecifications.size());
    Object getResult = classSpecifications.get(0);
    assertTrue(getResult instanceof ClassSpecification);
    List<MemberSpecification> memberSpecificationList =
        ((ClassSpecification) getResult).fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult2 = memberSpecificationList.get(0);
    assertNull(getResult2.annotationType);
    assertNull(getResult2.descriptor);
    assertNull(getResult2.name);
    assertNull(getResult2.attributeNames);
    assertEquals(0, ((ClassSpecification) getResult).requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredSetAccessFlags);
    assertEquals(0, getResult2.requiredUnsetAccessFlags);
    assertEquals(512, ((ClassSpecification) getResult).requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test createClassSpecification(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess("!");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classSpecificationElement.createClassSpecification(classSpecificationElement2));
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test createClassSpecification(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification2() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setType("!interface");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(512, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code Access}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test createClassSpecification(ClassSpecificationElement); given 'Access'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenAccess() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess("Access");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classSpecificationElement.createClassSpecification(classSpecificationElement2));
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code *}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test createClassSpecification(ClassSpecificationElement); given '*'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenAsterisk() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setName("*");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code class}.
   *   <li>When {@link ClassSpecificationElement} (default constructor) Type is {@code class}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given 'class'; when ClassSpecificationElement (default constructor) Type is 'class'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenClass_whenClassSpecificationElementTypeIsClass() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setType("class");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code ,}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test createClassSpecification(ClassSpecificationElement); given ','")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenComma() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess(" ,");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code enum}.
   *   <li>Then return {@link ClassSpecification#requiredSetAccessFlags} is {@code 16384}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given 'enum'; then return requiredSetAccessFlags is '16384'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenEnum_thenReturnRequiredSetAccessFlagsIs16384() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setType("enum");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
    assertEquals(16384, actualCreateClassSpecificationResult.requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code !enum}.
   *   <li>Then return {@link ClassSpecification#requiredUnsetAccessFlags} is {@code 16384}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given '!enum'; then return requiredUnsetAccessFlags is '16384'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenEnum_thenReturnRequiredUnsetAccessFlagsIs16384() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setType("!enum");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(16384, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code Extends}.
   *   <li>Then return {@link ClassSpecification#extendsClassName} is {@code Extends}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given 'Extends'; then return extendsClassName is 'Extends'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenExtends_thenReturnExtendsClassNameIsExtends() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setExtends("Extends ");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertEquals("Extends ", actualCreateClassSpecificationResult.extendsClassName);
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code final}.
   *   <li>Then return {@link ClassSpecification#requiredSetAccessFlags} is {@link Short#SIZE}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given 'final'; then return requiredSetAccessFlags is SIZE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenFinal_thenReturnRequiredSetAccessFlagsIsSize() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess("final");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
    assertEquals(Short.SIZE, actualCreateClassSpecificationResult.requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>Then return {@link ClassSpecification#className} is {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given 'Name'; then return className is 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenName_thenReturnClassNameIsName() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setName("Name");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertEquals("Name", actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code public}.
   *   <li>Then return {@link ClassSpecification#requiredSetAccessFlags} is one.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given 'public'; then return requiredSetAccessFlags is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenPublic_thenReturnRequiredSetAccessFlagsIsOne() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess("public");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
    assertEquals(1, actualCreateClassSpecificationResult.requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code Type}.
   *   <li>When {@link ClassSpecificationElement} (default constructor) Type is {@code Type}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); given 'Type'; when ClassSpecificationElement (default constructor) Type is 'Type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_givenType_whenClassSpecificationElementTypeIsType() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setType("Type");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classSpecificationElement.createClassSpecification(classSpecificationElement2));
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#annotationType} is {@code LAnnotation;}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return annotationType is 'LAnnotation;'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnAnnotationTypeIsLAnnotation() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAnnotation("Annotation");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertEquals("LAnnotation;", actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#extendsAnnotationType} is {@code LExtends
   *       Annotation;}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return extendsAnnotationType is 'LExtends Annotation;'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnExtendsAnnotationTypeIsLExtendsAnnotation() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setExtendsannotation("Extends Annotation");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertEquals(
        "LExtends Annotation;", actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#fieldSpecifications} size is one.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return fieldSpecifications size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnFieldSpecificationsSizeIsOne() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());

    // Act and Assert
    List<MemberSpecification> memberSpecificationList =
        classSpecificationElement.createClassSpecification(new ClassSpecificationElement())
            .fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult = memberSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.descriptor);
    assertNull(getResult.name);
    assertNull(getResult.attributeNames);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#methodSpecifications} size is one.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return methodSpecifications size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnMethodSpecificationsSizeIsOne() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.addConfiguredMethod(new MemberSpecificationElement());

    // Act and Assert
    List<MemberSpecification> memberSpecificationList =
        classSpecificationElement.createClassSpecification(new ClassSpecificationElement())
            .methodSpecifications;
    assertEquals(1, memberSpecificationList.size());
    MemberSpecification getResult = memberSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.descriptor);
    assertNull(getResult.name);
    assertNull(getResult.attributeNames);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#requiredSetAccessFlags} is {@code 1024}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return requiredSetAccessFlags is '1024'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnRequiredSetAccessFlagsIs1024() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess("abstract");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
    assertEquals(1024, actualCreateClassSpecificationResult.requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#requiredSetAccessFlags} is {@code 4096}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return requiredSetAccessFlags is '4096'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnRequiredSetAccessFlagsIs4096() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess("synthetic");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
    assertEquals(4096, actualCreateClassSpecificationResult.requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#requiredSetAccessFlags} is {@code 8192}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return requiredSetAccessFlags is '8192'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnRequiredSetAccessFlagsIs8192() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setAccess("@");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
    assertEquals(8192, actualCreateClassSpecificationResult.requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#requiredSetAccessFlags} is five hundred twelve.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); then return requiredSetAccessFlags is five hundred twelve")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_thenReturnRequiredSetAccessFlagsIsFiveHundredTwelve() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setType("interface");

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(classSpecificationElement2);

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
    assertEquals(512, actualCreateClassSpecificationResult.requiredSetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); when ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_whenClassSpecificationElement() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    // Act
    ClassSpecification actualCreateClassSpecificationResult =
        classSpecificationElement.createClassSpecification(new ClassSpecificationElement());

    // Assert
    assertNull(actualCreateClassSpecificationResult.annotationType);
    assertNull(actualCreateClassSpecificationResult.className);
    assertNull(actualCreateClassSpecificationResult.extendsAnnotationType);
    assertNull(actualCreateClassSpecificationResult.extendsClassName);
    assertEquals(0, actualCreateClassSpecificationResult.requiredSetAccessFlags);
    assertEquals(0, actualCreateClassSpecificationResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor) Type is {@code !}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#createClassSpecification(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test createClassSpecification(ClassSpecificationElement); when ClassSpecificationElement (default constructor) Type is '!'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ClassSpecification ClassSpecificationElement.createClassSpecification(ClassSpecificationElement)"
  })
  void testCreateClassSpecification_whenClassSpecificationElementTypeIsExclamationMark() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    ClassSpecificationElement classSpecificationElement2 = new ClassSpecificationElement();
    classSpecificationElement2.setType("!");

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classSpecificationElement.createClassSpecification(classSpecificationElement2));
  }

  /**
   * Test {@link ClassSpecificationElement#addConfiguredField(MemberSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@code Annotation}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#addConfiguredField(MemberSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredField(MemberSpecificationElement); given 'Annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassSpecificationElement.addConfiguredField(MemberSpecificationElement)"
  })
  void testAddConfiguredField_givenAnnotation() {
    // Arrange
    when(list.add(Mockito.<Object>any())).thenReturn(true);

    MemberSpecificationElement memberSpecificationElement = new MemberSpecificationElement();
    memberSpecificationElement.setAnnotation("Annotation");

    // Act
    classSpecificationElement.addConfiguredField(memberSpecificationElement);

    // Assert
    verify(list).add(isA(Object.class));
  }

  /**
   * Test {@link ClassSpecificationElement#addConfiguredField(MemberSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link MemberSpecificationElement} (default constructor).
   *   <li>Then calls {@link List#add(Object)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#addConfiguredField(MemberSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredField(MemberSpecificationElement); when MemberSpecificationElement (default constructor); then calls add(Object)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassSpecificationElement.addConfiguredField(MemberSpecificationElement)"
  })
  void testAddConfiguredField_whenMemberSpecificationElement_thenCallsAdd() {
    // Arrange
    when(list.add(Mockito.<Object>any())).thenReturn(true);

    // Act
    classSpecificationElement.addConfiguredField(new MemberSpecificationElement());

    // Assert
    verify(list).add(isA(Object.class));
  }

  /**
   * Test {@link ClassSpecificationElement#addConfiguredMethod(MemberSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor).
   *   <li>Then calls {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#addConfiguredMethod(MemberSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredMethod(MemberSpecificationElement); given ClassSpecificationElement (default constructor); then calls appendTo(List, boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassSpecificationElement.addConfiguredMethod(MemberSpecificationElement)"
  })
  void testAddConfiguredMethod_givenClassSpecificationElement_thenCallsAppendTo() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    MemberSpecificationElement memberSpecificationElement = mock(MemberSpecificationElement.class);
    doNothing()
        .when(memberSpecificationElement)
        .appendTo(Mockito.<List<Object>>any(), anyBoolean(), anyBoolean());

    // Act
    classSpecificationElement.addConfiguredMethod(memberSpecificationElement);

    // Assert
    verify(memberSpecificationElement).appendTo(isA(List.class), eq(true), eq(false));
  }

  /**
   * Test {@link ClassSpecificationElement#addConfiguredConstructor(MemberSpecificationElement)}.
   *
   * <ul>
   *   <li>Given {@link ClassSpecificationElement} (default constructor).
   *   <li>Then calls {@link MemberSpecificationElement#appendTo(List, boolean, boolean)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ClassSpecificationElement#addConfiguredConstructor(MemberSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredConstructor(MemberSpecificationElement); given ClassSpecificationElement (default constructor); then calls appendTo(List, boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassSpecificationElement.addConfiguredConstructor(MemberSpecificationElement)"
  })
  void testAddConfiguredConstructor_givenClassSpecificationElement_thenCallsAppendTo() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();

    MemberSpecificationElement memberSpecificationElement = mock(MemberSpecificationElement.class);
    doNothing()
        .when(memberSpecificationElement)
        .appendTo(Mockito.<List<Object>>any(), anyBoolean(), anyBoolean());

    // Act
    classSpecificationElement.addConfiguredConstructor(memberSpecificationElement);

    // Assert
    verify(memberSpecificationElement).appendTo(isA(List.class), eq(true), eq(true));
  }

  /**
   * Test new {@link ClassSpecificationElement} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ClassSpecificationElement}
   */
  @Test
  @DisplayName("Test new ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecificationElement.<init>()"})
  void testNewClassSpecificationElement() {
    // Arrange and Act
    ClassSpecificationElement actualClassSpecificationElement = new ClassSpecificationElement();

    // Assert
    Location location = actualClassSpecificationElement.getLocation();
    assertNull(location.getFileName());
    assertNull(actualClassSpecificationElement.getDescription());
    assertNull(actualClassSpecificationElement.getProject());
    assertNull(actualClassSpecificationElement.getRefid());
    assertEquals(0, location.getColumnNumber());
    assertEquals(0, location.getLineNumber());
    assertFalse(actualClassSpecificationElement.isReference());
  }
}
