package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
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
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.visitor.MemberVisitor;

class TypeParameterClassCheckerDiffblueTest {
  /**
   * Test {@link TypeParameterClassChecker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#fieldsAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls fieldsAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TypeParameterClassChecker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsFieldsAccept() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());

    // Act
    typeParameterClassChecker.visitProgramClass(programClass);

    // Assert
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)} with {@code clazz}, {@code field}, {@code signatureAttribute}.
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, Field, SignatureAttribute) with 'clazz', 'field', 'signatureAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.visitSignatureAttribute(Clazz, Field, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzFieldSignatureAttribute() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    LibraryField field = new LibraryField();

    // Act
    typeParameterClassChecker.visitSignatureAttribute(
        clazz, (Field) field, new SignatureAttribute());

    // Assert that nothing has changed
    verify(clazz).getString(0);
    assertFalse(typeParameterClassChecker.hasFieldWithTypeParameter);
  }

  /**
   * Test {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)} with {@code clazz}, {@code field}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Given {@code +L}.
   * </ul>
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, Field, SignatureAttribute) with 'clazz', 'field', 'signatureAttribute'; given '+L'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.visitSignatureAttribute(Clazz, Field, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzFieldSignatureAttribute_givenL() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();
    LibraryClass clazz = mock(LibraryClass.class);
    LibraryField field = new LibraryField();

    SignatureAttribute signatureAttribute = mock(SignatureAttribute.class);
    when(signatureAttribute.getSignature(Mockito.<Clazz>any())).thenReturn("+L");

    // Act
    typeParameterClassChecker.visitSignatureAttribute(clazz, (Field) field, signatureAttribute);

    // Assert
    verify(signatureAttribute).getSignature(isA(Clazz.class));
    assertTrue(typeParameterClassChecker.hasFieldWithTypeParameter);
  }

  /**
   * Test {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)} with {@code clazz}, {@code field}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Given {@code T}.
   * </ul>
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, Field, SignatureAttribute) with 'clazz', 'field', 'signatureAttribute'; given 'T'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.visitSignatureAttribute(Clazz, Field, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzFieldSignatureAttribute_givenT() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();
    LibraryClass clazz = mock(LibraryClass.class);
    LibraryField field = new LibraryField();

    SignatureAttribute signatureAttribute = mock(SignatureAttribute.class);
    when(signatureAttribute.getSignature(Mockito.<Clazz>any())).thenReturn("T");

    // Act
    typeParameterClassChecker.visitSignatureAttribute(clazz, (Field) field, signatureAttribute);

    // Assert
    verify(signatureAttribute).getSignature(isA(Clazz.class));
    assertTrue(typeParameterClassChecker.hasFieldWithTypeParameter);
  }

  /**
   * Test {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)} with {@code clazz}, {@code field}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Given {@code <T}.
   * </ul>
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, Field, SignatureAttribute) with 'clazz', 'field', 'signatureAttribute'; given '<T'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.visitSignatureAttribute(Clazz, Field, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzFieldSignatureAttribute_givenT2() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();
    LibraryClass clazz = mock(LibraryClass.class);
    LibraryField field = new LibraryField();

    SignatureAttribute signatureAttribute = mock(SignatureAttribute.class);
    when(signatureAttribute.getSignature(Mockito.<Clazz>any())).thenReturn("<T");

    // Act
    typeParameterClassChecker.visitSignatureAttribute(clazz, (Field) field, signatureAttribute);

    // Assert
    verify(signatureAttribute).getSignature(isA(Clazz.class));
    assertTrue(typeParameterClassChecker.hasFieldWithTypeParameter);
  }

  /**
   * Test {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)} with {@code clazz}, {@code field}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Given {@code ;T}.
   * </ul>
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, Field, SignatureAttribute) with 'clazz', 'field', 'signatureAttribute'; given ';T'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.visitSignatureAttribute(Clazz, Field, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzFieldSignatureAttribute_givenT3() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();
    LibraryClass clazz = mock(LibraryClass.class);
    LibraryField field = new LibraryField();

    SignatureAttribute signatureAttribute = mock(SignatureAttribute.class);
    when(signatureAttribute.getSignature(Mockito.<Clazz>any())).thenReturn(";T");

    // Act
    typeParameterClassChecker.visitSignatureAttribute(clazz, (Field) field, signatureAttribute);

    // Assert
    verify(signatureAttribute).getSignature(isA(Clazz.class));
    assertTrue(typeParameterClassChecker.hasFieldWithTypeParameter);
  }

  /**
   * Test {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)} with {@code clazz}, {@code field}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Given {@code +T}.
   * </ul>
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, Field, SignatureAttribute) with 'clazz', 'field', 'signatureAttribute'; given '+T'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.visitSignatureAttribute(Clazz, Field, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzFieldSignatureAttribute_givenT4() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();
    LibraryClass clazz = mock(LibraryClass.class);
    LibraryField field = new LibraryField();

    SignatureAttribute signatureAttribute = mock(SignatureAttribute.class);
    when(signatureAttribute.getSignature(Mockito.<Clazz>any())).thenReturn("+T");

    // Act
    typeParameterClassChecker.visitSignatureAttribute(clazz, (Field) field, signatureAttribute);

    // Assert
    verify(signatureAttribute).getSignature(isA(Clazz.class));
    assertTrue(typeParameterClassChecker.hasFieldWithTypeParameter);
  }

  /**
   * Test {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)} with {@code clazz}, {@code field}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Given {@code [T}.
   * </ul>
   *
   * <p>Method under test: {@link TypeParameterClassChecker#visitSignatureAttribute(Clazz, Field,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, Field, SignatureAttribute) with 'clazz', 'field', 'signatureAttribute'; given '[T'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.visitSignatureAttribute(Clazz, Field, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzFieldSignatureAttribute_givenT5() {
    // Arrange
    TypeParameterClassChecker typeParameterClassChecker = new TypeParameterClassChecker();
    LibraryClass clazz = mock(LibraryClass.class);
    LibraryField field = new LibraryField();

    SignatureAttribute signatureAttribute = mock(SignatureAttribute.class);
    when(signatureAttribute.getSignature(Mockito.<Clazz>any())).thenReturn("[T");

    // Act
    typeParameterClassChecker.visitSignatureAttribute(clazz, (Field) field, signatureAttribute);

    // Assert
    verify(signatureAttribute).getSignature(isA(Clazz.class));
    assertTrue(typeParameterClassChecker.hasFieldWithTypeParameter);
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link TypeParameterClassChecker}
   *   <li>{@link TypeParameterClassChecker#visitAnyAttribute(Clazz, Attribute)}
   *   <li>{@link TypeParameterClassChecker#visitAnyClass(Clazz)}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TypeParameterClassChecker.<init>()",
    "void TypeParameterClassChecker.visitAnyAttribute(Clazz, Attribute)",
    "void TypeParameterClassChecker.visitAnyClass(Clazz)"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    TypeParameterClassChecker actualTypeParameterClassChecker = new TypeParameterClassChecker();
    LibraryClass clazz = new LibraryClass();
    actualTypeParameterClassChecker.visitAnyAttribute(clazz, new BootstrapMethodsAttribute());
    actualTypeParameterClassChecker.visitAnyClass(new LibraryClass());

    // Assert
    assertFalse(actualTypeParameterClassChecker.hasFieldWithTypeParameter);
  }
}
