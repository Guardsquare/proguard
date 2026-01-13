package proguard.fixer.kotlin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.shrink.ShortestUsageMarker;
import proguard.shrink.SimpleUsageMarker;
import proguard.util.Processable;

class KotlinAnnotationCounterDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link KotlinAnnotationCounter#KotlinAnnotationCounter()}
   *   <li>{@link KotlinAnnotationCounter#visitAnyAttribute(Clazz, Attribute)}
   *   <li>{@link KotlinAnnotationCounter#getCount()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.<init>()",
    "void KotlinAnnotationCounter.<init>(SimpleUsageMarker)",
    "int KotlinAnnotationCounter.getCount()",
    "void KotlinAnnotationCounter.visitAnyAttribute(Clazz, Attribute)"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    KotlinAnnotationCounter actualKotlinAnnotationCounter = new KotlinAnnotationCounter();
    LibraryClass clazz = new LibraryClass();
    actualKotlinAnnotationCounter.visitAnyAttribute(clazz, new BootstrapMethodsAttribute());

    // Assert
    assertEquals(0, actualKotlinAnnotationCounter.getCount());
  }

  /**
   * Test getters and setters.
   *
   * <ul>
   *   <li>When {@link SimpleUsageMarker} (default constructor).
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link KotlinAnnotationCounter#KotlinAnnotationCounter(SimpleUsageMarker)}
   *   <li>{@link KotlinAnnotationCounter#visitAnyAttribute(Clazz, Attribute)}
   *   <li>{@link KotlinAnnotationCounter#getCount()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters; when SimpleUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.<init>()",
    "void KotlinAnnotationCounter.<init>(SimpleUsageMarker)",
    "int KotlinAnnotationCounter.getCount()",
    "void KotlinAnnotationCounter.visitAnyAttribute(Clazz, Attribute)"
  })
  void testGettersAndSetters_whenSimpleUsageMarker() {
    // Arrange and Act
    KotlinAnnotationCounter actualKotlinAnnotationCounter =
        new KotlinAnnotationCounter(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();
    actualKotlinAnnotationCounter.visitAnyAttribute(clazz, new BootstrapMethodsAttribute());

    // Assert
    assertEquals(0, actualKotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#getParameterAnnotationCount(int)}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#getParameterAnnotationCount(int)}
   */
  @Test
  @DisplayName("Test getParameterAnnotationCount(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int KotlinAnnotationCounter.getParameterAnnotationCount(int)"})
  void testGetParameterAnnotationCount() {
    // Arrange, Act and Assert
    assertEquals(-1, new KotlinAnnotationCounter().getParameterAnnotationCount(1));
  }

  /**
   * Test {@link KotlinAnnotationCounter#getParameterAnnotationCount(int)}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#getParameterAnnotationCount(int)}
   */
  @Test
  @DisplayName("Test getParameterAnnotationCount(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int KotlinAnnotationCounter.getParameterAnnotationCount(int)"})
  void testGetParameterAnnotationCount2() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    kotlinAnnotationCounter.visitAnyParameterAnnotationsAttribute(
        clazz, method, new RuntimeInvisibleParameterAnnotationsAttribute());

    // Act and Assert
    assertEquals(-1, kotlinAnnotationCounter.getParameterAnnotationCount(1));
  }

  /**
   * Test {@link KotlinAnnotationCounter#reset()}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#reset()}
   */
  @Test
  @DisplayName("Test reset()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"KotlinAnnotationCounter KotlinAnnotationCounter.reset()"})
  void testReset() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter();

    // Act
    KotlinAnnotationCounter actualResetResult = kotlinAnnotationCounter.reset();

    // Assert
    assertSame(kotlinAnnotationCounter, actualResetResult);
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName("Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter =
        new KotlinAnnotationCounter(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();

    Annotation annotation = mock(Annotation.class);
    when(annotation.getProcessingInfo()).thenReturn("Processing Info");
    when(annotation.getType(Mockito.<Clazz>any())).thenReturn("Type");
    Annotation[] annotations = new Annotation[] {annotation};

    // Act
    kotlinAnnotationCounter.visitAnyAnnotationsAttribute(
        clazz, new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations));

    // Assert that nothing has changed
    verify(annotation).getType(isA(Clazz.class));
    verify(annotation).getProcessingInfo();
    assertEquals(0, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName("Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute2() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter =
        new KotlinAnnotationCounter(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();

    Annotation annotation = mock(Annotation.class);
    when(annotation.getProcessingInfo()).thenReturn("Processing Info");
    when(annotation.getType(Mockito.<Clazz>any())).thenReturn("Type");
    Annotation[] annotations = new Annotation[] {annotation};

    // Act
    kotlinAnnotationCounter.visitAnyAnnotationsAttribute(
        clazz, new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations));

    // Assert that nothing has changed
    verify(annotation).getType(isA(Clazz.class));
    verify(annotation).getProcessingInfo();
    assertEquals(0, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName("Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute3() {
    // Arrange
    ShortestUsageMarker javaUsageMarker = mock(ShortestUsageMarker.class);
    when(javaUsageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter(javaUsageMarker);
    LibraryClass clazz = new LibraryClass();

    Annotation annotation = mock(Annotation.class);
    when(annotation.getType(Mockito.<Clazz>any())).thenReturn("Type");
    Annotation[] annotations = new Annotation[] {annotation};

    // Act
    kotlinAnnotationCounter.visitAnyAnnotationsAttribute(
        clazz, new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations));

    // Assert
    verify(annotation).getType(isA(Clazz.class));
    verify(javaUsageMarker).isUsed(isA(Processable.class));
    assertEquals(1, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link RuntimeInvisibleAnnotationsAttribute#annotationsAccept(Clazz,
   *       AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); then calls annotationsAccept(Clazz, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_thenCallsAnnotationsAccept() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter();
    LibraryClass clazz = new LibraryClass();

    RuntimeInvisibleAnnotationsAttribute annotationsAttribute =
        mock(RuntimeInvisibleAnnotationsAttribute.class);
    doNothing()
        .when(annotationsAttribute)
        .annotationsAccept(Mockito.<Clazz>any(), Mockito.<AnnotationVisitor>any());

    // Act
    kotlinAnnotationCounter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

    // Assert that nothing has changed
    verify(annotationsAttribute).annotationsAccept(isA(Clazz.class), isA(AnnotationVisitor.class));
    assertEquals(0, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then {@link KotlinAnnotationCounter#KotlinAnnotationCounter()} Count is one.
   * </ul>
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); then KotlinAnnotationCounter() Count is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_thenKotlinAnnotationCounterCountIsOne() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter();
    LibraryClass clazz = new LibraryClass();

    Annotation annotation = mock(Annotation.class);
    when(annotation.getType(Mockito.<Clazz>any())).thenReturn("Type");
    Annotation[] annotations = new Annotation[] {annotation};

    // Act
    kotlinAnnotationCounter.visitAnyAnnotationsAttribute(
        clazz, new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations));

    // Assert
    verify(annotation).getType(isA(Clazz.class));
    assertEquals(1, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>When {@link RuntimeInvisibleAnnotationsAttribute#RuntimeInvisibleAnnotationsAttribute()}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); when RuntimeInvisibleAnnotationsAttribute()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_whenRuntimeInvisibleAnnotationsAttribute() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter();
    LibraryClass clazz = new LibraryClass();

    // Act
    kotlinAnnotationCounter.visitAnyAnnotationsAttribute(
        clazz, new RuntimeInvisibleAnnotationsAttribute());

    // Assert that nothing has changed
    assertEquals(0, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link RuntimeInvisibleParameterAnnotationsAttribute#annotationsAccept(Clazz,
   *       Method, AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * KotlinAnnotationCounter#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then calls annotationsAccept(Clazz, Method, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinAnnotationCounter.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenCallsAnnotationsAccept() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    RuntimeInvisibleParameterAnnotationsAttribute parameterAnnotationsAttribute =
        mock(RuntimeInvisibleParameterAnnotationsAttribute.class);
    doNothing()
        .when(parameterAnnotationsAttribute)
        .annotationsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AnnotationVisitor>any());

    // Act
    kotlinAnnotationCounter.visitAnyParameterAnnotationsAttribute(
        clazz, method, parameterAnnotationsAttribute);

    // Assert
    verify(parameterAnnotationsAttribute)
        .annotationsAccept(isA(Clazz.class), isA(Method.class), isA(AnnotationVisitor.class));
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnnotation(Clazz, Annotation)} with {@code clazz},
   * {@code annotation}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinAnnotationCounter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter =
        new KotlinAnnotationCounter(new SimpleUsageMarker());
    LibraryClass clazz = new LibraryClass();

    // Act
    kotlinAnnotationCounter.visitAnnotation(clazz, new Annotation());

    // Assert that nothing has changed
    assertEquals(0, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnnotation(Clazz, Annotation)} with {@code clazz},
   * {@code annotation}.
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinAnnotationCounter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation2() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter =
        new KotlinAnnotationCounter(new ShortestUsageMarker());
    LibraryClass clazz = new LibraryClass();

    // Act
    kotlinAnnotationCounter.visitAnnotation(clazz, new Annotation());

    // Assert that nothing has changed
    assertEquals(0, kotlinAnnotationCounter.getCount());
  }

  /**
   * Test {@link KotlinAnnotationCounter#visitAnnotation(Clazz, Annotation)} with {@code clazz},
   * {@code annotation}.
   *
   * <ul>
   *   <li>Then {@link KotlinAnnotationCounter#KotlinAnnotationCounter()} Count is one.
   * </ul>
   *
   * <p>Method under test: {@link KotlinAnnotationCounter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; then KotlinAnnotationCounter() Count is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinAnnotationCounter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_thenKotlinAnnotationCounterCountIsOne() {
    // Arrange
    KotlinAnnotationCounter kotlinAnnotationCounter = new KotlinAnnotationCounter();
    LibraryClass clazz = new LibraryClass();

    // Act
    kotlinAnnotationCounter.visitAnnotation(clazz, new Annotation());

    // Assert
    assertEquals(1, kotlinAnnotationCounter.getCount());
  }
}
