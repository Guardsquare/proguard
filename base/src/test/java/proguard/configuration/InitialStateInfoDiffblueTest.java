package proguard.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;

class InitialStateInfoDiffblueTest {
  /**
   * Test {@link InitialStateInfo#size()}.
   *
   * <p>Method under test: {@link InitialStateInfo#size()}
   */
  @Test
  @DisplayName("Test size()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int InitialStateInfo.size()"})
  void testSize() {
    // Arrange, Act and Assert
    assertEquals(0, new InitialStateInfo(new ClassPool()).size());
  }

  /**
   * Test {@link InitialStateInfo#getSuperClassName(String)}.
   *
   * <p>Method under test: {@link InitialStateInfo#getSuperClassName(String)}
   */
  @Test
  @DisplayName("Test getSuperClassName(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String InitialStateInfo.getSuperClassName(String)"})
  void testGetSuperClassName() {
    // Arrange, Act and Assert
    assertNull(new InitialStateInfo(new ClassPool()).getSuperClassName("Class Name"));
  }

  /**
   * Test {@link InitialStateInfo#getMethodHashMap(String)}.
   *
   * <p>Method under test: {@link InitialStateInfo#getMethodHashMap(String)}
   */
  @Test
  @DisplayName("Test getMethodHashMap(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.util.Map InitialStateInfo.getMethodHashMap(String)"})
  void testGetMethodHashMap() {
    // Arrange, Act and Assert
    assertTrue(new InitialStateInfo(new ClassPool()).getMethodHashMap("Class Name").isEmpty());
  }

  /**
   * Test {@link InitialStateInfo#getFieldHashMap(String)}.
   *
   * <p>Method under test: {@link InitialStateInfo#getFieldHashMap(String)}
   */
  @Test
  @DisplayName("Test getFieldHashMap(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.util.Map InitialStateInfo.getFieldHashMap(String)"})
  void testGetFieldHashMap() {
    // Arrange, Act and Assert
    assertTrue(new InitialStateInfo(new ClassPool()).getFieldHashMap("Class Name").isEmpty());
  }
}
