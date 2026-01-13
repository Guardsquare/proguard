package proguard.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class HashUtilDiffblueTest {
  /**
   * Test {@link HashUtil#hashFnv1a32_UTF8(String)}.
   *
   * <p>Method under test: {@link HashUtil#hashFnv1a32_UTF8(String)}
   */
  @Test
  @DisplayName("Test hashFnv1a32_UTF8(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int HashUtil.hashFnv1a32_UTF8(String)"})
  void testHashFnv1a32_UTF8() {
    // Arrange, Act and Assert
    assertEquals(1615808600, HashUtil.hashFnv1a32_UTF8("String"));
  }

  /**
   * Test {@link HashUtil#hash(byte[], int)} with {@code data}, {@code init}.
   *
   * <p>Method under test: {@link HashUtil#hash(byte[], int)}
   */
  @Test
  @DisplayName("Test hash(byte[], int) with 'data', 'init'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int HashUtil.hash(byte[], int)"})
  void testHashWithDataInit() throws UnsupportedEncodingException {
    // Arrange, Act and Assert
    assertEquals(1673693303, HashUtil.hash("AXAXAXAX".getBytes("UTF-8"), 19088743));
  }

  /**
   * Test {@link HashUtil#hash(String, int)} with {@code string}, {@code init}.
   *
   * <p>Method under test: {@link HashUtil#hash(String, int)}
   */
  @Test
  @DisplayName("Test hash(String, int) with 'string', 'init'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int HashUtil.hash(String, int)"})
  void testHashWithStringInit() {
    // Arrange, Act and Assert
    assertEquals(-1392318702, HashUtil.hash("String", 19088743));
  }

  /**
   * Test {@link HashUtil#hashFnv1a32(byte[])}.
   *
   * <p>Method under test: {@link HashUtil#hashFnv1a32(byte[])}
   */
  @Test
  @DisplayName("Test hashFnv1a32(byte[])")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int HashUtil.hashFnv1a32(byte[])"})
  void testHashFnv1a32() throws UnsupportedEncodingException {
    // Arrange, Act and Assert
    assertEquals(-324582187, HashUtil.hashFnv1a32("AXAXAXAX".getBytes("UTF-8")));
  }
}
