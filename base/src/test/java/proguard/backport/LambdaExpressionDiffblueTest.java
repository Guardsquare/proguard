package proguard.backport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.testutils.cpa.NamedClass;

class LambdaExpressionDiffblueTest {
  /**
   * Test {@link LambdaExpression#LambdaExpression(ProgramClass, int, BootstrapMethodInfo, String,
   * String[], String[], String, String, int, String, String, String, Clazz, Method)}.
   *
   * <p>Method under test: {@link LambdaExpression#LambdaExpression(ProgramClass, int,
   * BootstrapMethodInfo, String, String[], String[], String, String, int, String, String, String,
   * Clazz, Method)}
   */
  @Test
  @DisplayName(
      "Test new LambdaExpression(ProgramClass, int, BootstrapMethodInfo, String, String[], String[], String, String, int, String, String, String, Clazz, Method)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LambdaExpression.<init>(ProgramClass, int, BootstrapMethodInfo, String, String[], String[], String, String, int, String, String, String, Clazz, Method)"
  })
  void testNewLambdaExpression() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    // Act
    LambdaExpression actualLambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Assert
    BootstrapMethodInfo bootstrapMethodInfo2 = actualLambdaExpression.bootstrapMethodInfo;
    assertNull(bootstrapMethodInfo2.getProcessingInfo());
    ProgramClass programClass = actualLambdaExpression.referencedClass;
    assertNull(programClass.getProcessingInfo());
    assertNull(programClass.getSuperName());
    assertNull(programClass.getFeatureName());
    assertNull(programClass.getSuperClass());
    assertEquals(0, programClass.getAccessFlags());
    assertEquals(0, programClass.getInterfaceCount());
    assertEquals(0, bootstrapMethodInfo2.getProcessingFlags());
    assertEquals(0, programClass.getProcessingFlags());
    assertTrue(programClass.getExtraFeatureNames().isEmpty());
  }

  /**
   * Test {@link LambdaExpression#getLambdaClassName()}.
   *
   * <ul>
   *   <li>Then return {@code %s$$Lambda$%d$$Lambda$1}.
   * </ul>
   *
   * <p>Method under test: {@link LambdaExpression#getLambdaClassName()}
   */
  @Test
  @DisplayName("Test getLambdaClassName(); then return '%s$$Lambda$%d$$Lambda$1'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String LambdaExpression.getLambdaClassName()"})
  void testGetLambdaClassName_thenReturnSLambdaDLambda1() {
    // Arrange
    NamedClass referencedClass = new NamedClass("%s$$Lambda$%d");
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertEquals("%s$$Lambda$%d$$Lambda$1", lambdaExpression.getLambdaClassName());
  }

  /**
   * Test {@link LambdaExpression#isSerializable()}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link LambdaExpression#isSerializable()}
   */
  @Test
  @DisplayName("Test isSerializable(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.isSerializable()"})
  void testIsSerializable_thenReturnFalse() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.isSerializable());
  }

  /**
   * Test {@link LambdaExpression#isSerializable()}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link LambdaExpression#isSerializable()}
   */
  @Test
  @DisplayName("Test isSerializable(); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.isSerializable()"})
  void testIsSerializable_thenReturnTrue() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            new String[] {"java/io/Serializable", "Interfaces"},
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertTrue(lambdaExpression.isSerializable());
  }

  /**
   * Test {@link LambdaExpression#isMethodReference()}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link LambdaExpression#isMethodReference()}
   */
  @Test
  @DisplayName("Test isMethodReference(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.isMethodReference()"})
  void testIsMethodReference_thenReturnFalse() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "lambda$",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.isMethodReference());
  }

  /**
   * Test {@link LambdaExpression#isMethodReference()}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link LambdaExpression#isMethodReference()}
   */
  @Test
  @DisplayName("Test isMethodReference(); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.isMethodReference()"})
  void testIsMethodReference_thenReturnTrue() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertTrue(lambdaExpression.isMethodReference());
  }

  /**
   * Test {@link LambdaExpression#invokesStaticInterfaceMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#invokesStaticInterfaceMethod()}
   */
  @Test
  @DisplayName("Test invokesStaticInterfaceMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.invokesStaticInterfaceMethod()"})
  void testInvokesStaticInterfaceMethod() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.invokesStaticInterfaceMethod());
  }

  /**
   * Test {@link LambdaExpression#invokesStaticInterfaceMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#invokesStaticInterfaceMethod()}
   */
  @Test
  @DisplayName("Test invokesStaticInterfaceMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.invokesStaticInterfaceMethod()"})
  void testInvokesStaticInterfaceMethod2() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            6,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.invokesStaticInterfaceMethod());
  }

  /**
   * Test {@link LambdaExpression#invokesStaticInterfaceMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#invokesStaticInterfaceMethod()}
   */
  @Test
  @DisplayName("Test invokesStaticInterfaceMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.invokesStaticInterfaceMethod()"})
  void testInvokesStaticInterfaceMethod3() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            6,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            null,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.invokesStaticInterfaceMethod());
  }

  /**
   * Test {@link LambdaExpression#referencesPrivateSyntheticInterfaceMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#referencesPrivateSyntheticInterfaceMethod()}
   */
  @Test
  @DisplayName("Test referencesPrivateSyntheticInterfaceMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.referencesPrivateSyntheticInterfaceMethod()"})
  void testReferencesPrivateSyntheticInterfaceMethod() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.referencesPrivateSyntheticInterfaceMethod());
  }

  /**
   * Test {@link LambdaExpression#referencesPrivateSyntheticInterfaceMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#referencesPrivateSyntheticInterfaceMethod()}
   */
  @Test
  @DisplayName("Test referencesPrivateSyntheticInterfaceMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.referencesPrivateSyntheticInterfaceMethod()"})
  void testReferencesPrivateSyntheticInterfaceMethod2() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass =
        new LibraryClass(512, "This Class Name", "Super Class Name");

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.referencesPrivateSyntheticInterfaceMethod());
  }

  /**
   * Test {@link LambdaExpression#needsAccessorMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#needsAccessorMethod()}
   */
  @Test
  @DisplayName("Test needsAccessorMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.needsAccessorMethod()"})
  void testNeedsAccessorMethod() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass =
        new LibraryClass(1, "This Class Name", "Super Class Name");

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertTrue(lambdaExpression.needsAccessorMethod());
  }

  /**
   * Test {@link LambdaExpression#needsAccessorMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#needsAccessorMethod()}
   */
  @Test
  @DisplayName("Test needsAccessorMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.needsAccessorMethod()"})
  void testNeedsAccessorMethod2() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass =
        new LibraryClass(1, "This Class Name", "Super Class Name");

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            null,
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertTrue(lambdaExpression.needsAccessorMethod());
  }

  /**
   * Test {@link LambdaExpression#needsAccessorMethod()}.
   *
   * <p>Method under test: {@link LambdaExpression#needsAccessorMethod()}
   */
  @Test
  @DisplayName("Test needsAccessorMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.needsAccessorMethod()"})
  void testNeedsAccessorMethod3() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass =
        new LibraryClass(1, "This Class Name", "Super Class Name");

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            null,
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertTrue(lambdaExpression.needsAccessorMethod());
  }

  /**
   * Test {@link LambdaExpression#needsAccessorMethod()}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link LambdaExpression#needsAccessorMethod()}
   */
  @Test
  @DisplayName("Test needsAccessorMethod(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.needsAccessorMethod()"})
  void testNeedsAccessorMethod_thenReturnFalse() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            null,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.needsAccessorMethod());
  }

  /**
   * Test {@link LambdaExpression#referencesPrivateConstructor()}.
   *
   * <p>Method under test: {@link LambdaExpression#referencesPrivateConstructor()}
   */
  @Test
  @DisplayName("Test referencesPrivateConstructor()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.referencesPrivateConstructor()"})
  void testReferencesPrivateConstructor() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            1,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.referencesPrivateConstructor());
  }

  /**
   * Test {@link LambdaExpression#referencesPrivateConstructor()}.
   *
   * <p>Method under test: {@link LambdaExpression#referencesPrivateConstructor()}
   */
  @Test
  @DisplayName("Test referencesPrivateConstructor()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.referencesPrivateConstructor()"})
  void testReferencesPrivateConstructor2() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            8,
            "Invoked Class Name",
            "<init>",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.referencesPrivateConstructor());
  }

  /**
   * Test {@link LambdaExpression#referencesPrivateConstructor()}.
   *
   * <p>Method under test: {@link LambdaExpression#referencesPrivateConstructor()}
   */
  @Test
  @DisplayName("Test referencesPrivateConstructor()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean LambdaExpression.referencesPrivateConstructor()"})
  void testReferencesPrivateConstructor3() {
    // Arrange
    ProgramClass referencedClass = new ProgramClass();
    BootstrapMethodInfo bootstrapMethodInfo = new BootstrapMethodInfo();
    String[] interfaces = new String[] {"Interfaces"};
    String[] bridgeMethodDescriptors = new String[] {"Bridge Method Descriptors"};
    LibraryClass referencedInvokedClass = new LibraryClass();

    LambdaExpression lambdaExpression =
        new LambdaExpression(
            referencedClass,
            1,
            bootstrapMethodInfo,
            "Factory Method Descriptor",
            interfaces,
            bridgeMethodDescriptors,
            "Interface Method",
            "Interface Method Descriptor",
            8,
            "Invoked Class Name",
            "Invoked Method Name",
            "Invoked Method Desc",
            referencedInvokedClass,
            new LibraryMethod());

    // Act and Assert
    assertFalse(lambdaExpression.referencesPrivateConstructor());
  }
}
