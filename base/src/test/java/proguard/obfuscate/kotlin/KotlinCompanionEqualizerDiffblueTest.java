package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.util.SimpleProcessable;

class KotlinCompanionEqualizerDiffblueTest {
  /**
   * Test {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinCompanionEqualizer.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata() {
    // Arrange
    KotlinCompanionEqualizer kotlinCompanionEqualizer = new KotlinCompanionEqualizer();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("$");

    LibraryClass libraryClass2 = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass2.setProcessingInfo("Kotlin Class Kind Metadata");

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

    LibraryField libraryField19 = new LibraryField(1, "Name", "Descriptor");
    libraryField19.setProcessingInfo(libraryField18);
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinClassKindMetadata.companionObjectName = "Kotlin Class Kind Metadata";
    kotlinClassKindMetadata.referencedCompanionClass = libraryClass;
    kotlinClassKindMetadata.referencedClass = libraryClass2;
    kotlinClassKindMetadata.referencedCompanionField = libraryField19;

    // Act
    kotlinCompanionEqualizer.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert
    Field field = kotlinClassKindMetadata.referencedCompanionField;
    assertTrue(field instanceof LibraryField);
    assertSame(libraryField2, field.getProcessingInfo());
  }

  /**
   * Test {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinCompanionEqualizer.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata2() {
    // Arrange
    KotlinCompanionEqualizer kotlinCompanionEqualizer = new KotlinCompanionEqualizer();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo(1);

    LibraryClass libraryClass2 = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass2.setProcessingInfo("Kotlin Class Kind Metadata");

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

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinClassKindMetadata.companionObjectName = "Kotlin Class Kind Metadata";
    kotlinClassKindMetadata.referencedCompanionClass = libraryClass;
    kotlinClassKindMetadata.referencedClass = libraryClass2;
    kotlinClassKindMetadata.referencedCompanionField = libraryField18;

    // Act
    kotlinCompanionEqualizer.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert that nothing has changed
    Clazz clazz2 = kotlinClassKindMetadata.referencedCompanionClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Field field = kotlinClassKindMetadata.referencedCompanionField;
    Object processingInfo = field.getProcessingInfo();
    assertTrue(processingInfo instanceof LibraryField);
    Object processingInfo2 = ((LibraryField) processingInfo).getProcessingInfo();
    assertTrue(processingInfo2 instanceof LibraryField);
    assertTrue(field instanceof LibraryField);
    assertEquals("Descriptor", ((LibraryField) processingInfo).descriptor);
    assertEquals("Name", ((LibraryField) processingInfo).name);
    assertEquals(1, ((Integer) clazz2.getProcessingInfo()).intValue());
    assertEquals(1, ((LibraryField) processingInfo).getAccessFlags());
    assertSame(libraryField16, processingInfo2);
  }

  /**
   * Test {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinCompanionEqualizer.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata3() {
    // Arrange
    KotlinCompanionEqualizer kotlinCompanionEqualizer = new KotlinCompanionEqualizer();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("Processing Info");

    LibraryClass libraryClass2 = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass2.setProcessingInfo("Kotlin Class Kind Metadata");

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

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinClassKindMetadata.companionObjectName = "Kotlin Class Kind Metadata";
    kotlinClassKindMetadata.referencedCompanionClass = libraryClass;
    kotlinClassKindMetadata.referencedClass = libraryClass2;
    kotlinClassKindMetadata.referencedCompanionField = libraryField18;

    // Act
    kotlinCompanionEqualizer.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert that nothing has changed
    Clazz clazz2 = kotlinClassKindMetadata.referencedCompanionClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Field field = kotlinClassKindMetadata.referencedCompanionField;
    Object processingInfo = field.getProcessingInfo();
    assertTrue(processingInfo instanceof LibraryField);
    Object processingInfo2 = ((LibraryField) processingInfo).getProcessingInfo();
    assertTrue(processingInfo2 instanceof LibraryField);
    assertTrue(field instanceof LibraryField);
    assertEquals("Descriptor", ((LibraryField) processingInfo).descriptor);
    assertEquals("Name", ((LibraryField) processingInfo).name);
    assertEquals("Processing Info", clazz2.getProcessingInfo());
    assertEquals(1, ((LibraryField) processingInfo).getAccessFlags());
    assertSame(libraryField16, processingInfo2);
  }

  /**
   * Test {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinCompanionEqualizer.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata4() {
    // Arrange
    KotlinCompanionEqualizer kotlinCompanionEqualizer = new KotlinCompanionEqualizer();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("$");

    LibraryClass libraryClass2 = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass2.setProcessingInfo("Kotlin Class Kind Metadata");

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

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(simpleProcessable);
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinClassKindMetadata.companionObjectName = "Kotlin Class Kind Metadata";
    kotlinClassKindMetadata.referencedCompanionClass = libraryClass;
    kotlinClassKindMetadata.referencedClass = libraryClass2;
    kotlinClassKindMetadata.referencedCompanionField = libraryField17;

    // Act
    kotlinCompanionEqualizer.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert that nothing has changed
    Clazz clazz2 = kotlinClassKindMetadata.referencedCompanionClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Field field = kotlinClassKindMetadata.referencedCompanionField;
    Object processingInfo = field.getProcessingInfo();
    Object processingInfo2 = ((SimpleProcessable) processingInfo).getProcessingInfo();
    assertTrue(processingInfo2 instanceof LibraryField);
    assertTrue(field instanceof LibraryField);
    assertTrue(processingInfo instanceof SimpleProcessable);
    assertEquals("$", clazz2.getProcessingInfo());
    assertSame(libraryField16, processingInfo2);
  }

  /**
   * Test {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinCompanionEqualizer.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata5() {
    // Arrange
    KotlinCompanionEqualizer kotlinCompanionEqualizer = new KotlinCompanionEqualizer();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("$");

    LibraryClass libraryClass2 = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass2.setProcessingInfo("Kotlin Class Kind Metadata");

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo("$");

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

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinClassKindMetadata.companionObjectName = "Kotlin Class Kind Metadata";
    kotlinClassKindMetadata.referencedCompanionClass = libraryClass;
    kotlinClassKindMetadata.referencedClass = libraryClass2;
    kotlinClassKindMetadata.referencedCompanionField = libraryField18;

    // Act
    kotlinCompanionEqualizer.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert
    Clazz clazz2 = kotlinClassKindMetadata.referencedCompanionClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Field field = kotlinClassKindMetadata.referencedCompanionField;
    Object processingInfo = field.getProcessingInfo();
    assertTrue(processingInfo instanceof LibraryField);
    assertTrue(field instanceof LibraryField);
    assertEquals("", ((LibraryField) processingInfo).getProcessingInfo());
    assertEquals("$", clazz2.getProcessingInfo());
    assertEquals("Descriptor", ((LibraryField) processingInfo).descriptor);
    assertEquals("Name", ((LibraryField) processingInfo).name);
    assertEquals(1, ((LibraryField) processingInfo).getAccessFlags());
  }

  /**
   * Test {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinCompanionEqualizer#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinCompanionEqualizer.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata6() {
    // Arrange
    KotlinCompanionEqualizer kotlinCompanionEqualizer = new KotlinCompanionEqualizer();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("$");

    LibraryClass libraryClass2 = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass2.setProcessingInfo("Kotlin Class Kind Metadata");

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

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.addProcessingFlags(-1, 1, 2, 1);
    libraryField18.setProcessingInfo(libraryField17);
    KotlinClassKindMetadata kotlinClassKindMetadata =
        new KotlinClassKindMetadata(new int[] {1, -1, 1, -1}, 1, "Xs", "Pn");
    kotlinClassKindMetadata.companionObjectName = "Kotlin Class Kind Metadata";
    kotlinClassKindMetadata.referencedCompanionClass = libraryClass;
    kotlinClassKindMetadata.referencedClass = libraryClass2;
    kotlinClassKindMetadata.referencedCompanionField = libraryField18;

    // Act
    kotlinCompanionEqualizer.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert
    Clazz clazz2 = kotlinClassKindMetadata.referencedCompanionClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Field field = kotlinClassKindMetadata.referencedCompanionField;
    Object processingInfo = field.getProcessingInfo();
    assertTrue(processingInfo instanceof LibraryField);
    Object processingInfo2 = ((LibraryField) processingInfo).getProcessingInfo();
    assertTrue(processingInfo2 instanceof LibraryField);
    assertTrue(field instanceof LibraryField);
    assertEquals(
        "Kotlin Class Kind Metadata$Kotlin Class Kind Metadata", clazz2.getProcessingInfo());
    assertSame(libraryField16, processingInfo2);
  }
}
