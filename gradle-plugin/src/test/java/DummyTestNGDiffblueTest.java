import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

@SuppressWarnings("DefaultPackage")
public class DummyTestNGDiffblueTest {

  @Test
  public void testEquality() {
    // The following line will fail to compile if the project uses the groovy compiler for java
    // sources
    String[] array = {"foo", "bar", "baz"};
    for (int i = 0; i < array.length; i++) {
      assertEquals(array[i], array[i]);
    }
  }
}