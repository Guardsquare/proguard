import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("DefaultPackage")
class DummyJupiterDiffblueTest {

  @Test
  void testEquality() {
    // The following line will fail to compile if the project uses the groovy compiler for java
    // sources
    String[] array = {"foo", "bar", "baz"};
    for (int i = 0; i < array.length; i++) {
      assertEquals(array[i], array[i]);
    }
  }
}