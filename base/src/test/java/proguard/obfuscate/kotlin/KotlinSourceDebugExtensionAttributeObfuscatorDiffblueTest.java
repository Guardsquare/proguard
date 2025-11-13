package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.SourceDebugExtensionAttribute;

class KotlinSourceDebugExtensionAttributeObfuscatorDiffblueTest {
  /**
   * Test {@link
   * KotlinSourceDebugExtensionAttributeObfuscator#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}.
   *
   * <p>Method under test: {@link
   * KotlinSourceDebugExtensionAttributeObfuscator#visitSourceDebugExtensionAttribute(Clazz,
   * SourceDebugExtensionAttribute)}
   */
  @Test
  @DisplayName("Test visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinSourceDebugExtensionAttributeObfuscator.visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)"
  })
  void testVisitSourceDebugExtensionAttribute() throws UnsupportedEncodingException {
    // Arrange
    KotlinSourceDebugExtensionAttributeObfuscator kotlinSourceDebugExtensionAttributeObfuscator =
        new KotlinSourceDebugExtensionAttributeObfuscator();
    LibraryClass clazz = new LibraryClass();
    SourceDebugExtensionAttribute sourceDebugExtensionAttribute =
        new SourceDebugExtensionAttribute();

    // Act
    kotlinSourceDebugExtensionAttributeObfuscator.visitSourceDebugExtensionAttribute(
        clazz, sourceDebugExtensionAttribute);

    // Assert
    assertEquals(45, sourceDebugExtensionAttribute.u4attributeLength);
    assertArrayEquals(
        "SMAP\n\nKotlin\n*S Kotlin\n*F\n+ 1 \n\n*L\n1#1,1:1\n*E".getBytes("UTF-8"),
        sourceDebugExtensionAttribute.info);
  }
}
