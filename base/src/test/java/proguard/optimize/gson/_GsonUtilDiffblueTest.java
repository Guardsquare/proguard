package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class _GsonUtilDiffblueTest {
  /**
   * Test {@link _GsonUtil#getTypeAdapter(Gson, TypeToken, Object)} with {@code gson}, {@code
   * declaredTypeToken}, {@code value}.
   *
   * <ul>
   *   <li>Then return {@link ObjectTypeAdapter}.
   * </ul>
   *
   * <p>Method under test: {@link _GsonUtil#getTypeAdapter(Gson, TypeToken, Object)}
   */
  @Test
  @DisplayName(
      "Test getTypeAdapter(Gson, TypeToken, Object) with 'gson', 'declaredTypeToken', 'value'; then return ObjectTypeAdapter")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeAdapter _GsonUtil.getTypeAdapter(Gson, TypeToken, Object)"})
  void testGetTypeAdapterWithGsonDeclaredTypeTokenValue_thenReturnObjectTypeAdapter() {
    // Arrange
    Gson gson = new Gson();
    Class<Object> type = Object.class;
    TypeToken<Object> declaredTypeToken = TypeToken.get(type);
    Gson gson2 = new Gson();
    _OptimizedJsonReaderImpl optimizedJsonReader = new _OptimizedJsonReaderImpl();

    _OptimizedTypeAdapterImpl _OptimizedTypeAdapterImpl =
        new _OptimizedTypeAdapterImpl(gson2, optimizedJsonReader, new _OptimizedJsonWriterImpl());

    // Act
    TypeAdapter actualTypeAdapter =
        _GsonUtil.getTypeAdapter(gson, declaredTypeToken, _OptimizedTypeAdapterImpl);

    // Assert
    assertTrue(actualTypeAdapter instanceof ObjectTypeAdapter);
  }

  /**
   * Test {@link _GsonUtil#getTypeAdapter(Gson, TypeToken, Object)} with {@code gson}, {@code
   * declaredTypeToken}, {@code value}.
   *
   * <ul>
   *   <li>Then return {@link ObjectTypeAdapter}.
   * </ul>
   *
   * <p>Method under test: {@link _GsonUtil#getTypeAdapter(Gson, TypeToken, Object)}
   */
  @Test
  @DisplayName(
      "Test getTypeAdapter(Gson, TypeToken, Object) with 'gson', 'declaredTypeToken', 'value'; then return ObjectTypeAdapter")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeAdapter _GsonUtil.getTypeAdapter(Gson, TypeToken, Object)"})
  void testGetTypeAdapterWithGsonDeclaredTypeTokenValue_thenReturnObjectTypeAdapter2() {
    // Arrange
    Gson gson = new Gson();
    Class<Object> type = Object.class;
    TypeToken<Object> declaredTypeToken = TypeToken.get(type);

    // Act
    TypeAdapter actualTypeAdapter = _GsonUtil.getTypeAdapter(gson, declaredTypeToken, null);

    // Assert
    assertTrue(actualTypeAdapter instanceof ObjectTypeAdapter);
  }

  /**
   * Test {@link _GsonUtil#getTypeAdapter(Gson, TypeToken, Object)} with {@code gson}, {@code
   * declaredTypeToken}, {@code value}.
   *
   * <ul>
   *   <li>Then return toJson {@code Value} is {@code "Value"}.
   * </ul>
   *
   * <p>Method under test: {@link _GsonUtil#getTypeAdapter(Gson, TypeToken, Object)}
   */
  @Test
  @DisplayName(
      "Test getTypeAdapter(Gson, TypeToken, Object) with 'gson', 'declaredTypeToken', 'value'; then return toJson 'Value' is '\"Value\"'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeAdapter _GsonUtil.getTypeAdapter(Gson, TypeToken, Object)"})
  void testGetTypeAdapterWithGsonDeclaredTypeTokenValue_thenReturnToJsonValueIsValue() {
    // Arrange
    Gson gson = new Gson();
    Class<Object> type = Object.class;
    TypeToken<Object> declaredTypeToken = TypeToken.get(type);

    // Act
    TypeAdapter actualTypeAdapter = _GsonUtil.getTypeAdapter(gson, declaredTypeToken, "Value");

    // Assert
    assertEquals("\"Value\"", actualTypeAdapter.toJson("Value"));
  }

  /**
   * Test {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)} with {@code gson}, {@code
   * declaredType}, {@code value}.
   *
   * <ul>
   *   <li>Then return {@link ObjectTypeAdapter}.
   * </ul>
   *
   * <p>Method under test: {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)}
   */
  @Test
  @DisplayName(
      "Test getTypeAdapter(Gson, Class, Object) with 'gson', 'declaredType', 'value'; then return ObjectTypeAdapter")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeAdapter _GsonUtil.getTypeAdapter(Gson, Class, Object)"})
  void testGetTypeAdapterWithGsonDeclaredTypeValue_thenReturnObjectTypeAdapter() {
    // Arrange
    Gson gson = new Gson();
    Class<Object> declaredType = Object.class;
    Gson gson2 = new Gson();
    _OptimizedJsonReaderImpl optimizedJsonReader = new _OptimizedJsonReaderImpl();

    _OptimizedTypeAdapterImpl _OptimizedTypeAdapterImpl =
        new _OptimizedTypeAdapterImpl(gson2, optimizedJsonReader, new _OptimizedJsonWriterImpl());

    // Act
    TypeAdapter actualTypeAdapter =
        _GsonUtil.getTypeAdapter(gson, declaredType, _OptimizedTypeAdapterImpl);

    // Assert
    assertTrue(actualTypeAdapter instanceof ObjectTypeAdapter);
  }

  /**
   * Test {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)} with {@code gson}, {@code
   * declaredType}, {@code value}.
   *
   * <ul>
   *   <li>Then return toJson {@code Value} is {@code "Value"}.
   * </ul>
   *
   * <p>Method under test: {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)}
   */
  @Test
  @DisplayName(
      "Test getTypeAdapter(Gson, Class, Object) with 'gson', 'declaredType', 'value'; then return toJson 'Value' is '\"Value\"'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeAdapter _GsonUtil.getTypeAdapter(Gson, Class, Object)"})
  void testGetTypeAdapterWithGsonDeclaredTypeValue_thenReturnToJsonValueIsValue() {
    // Arrange
    Gson gson = new Gson();
    Class<Object> declaredType = Object.class;

    // Act
    TypeAdapter actualTypeAdapter = _GsonUtil.getTypeAdapter(gson, declaredType, "Value");

    // Assert
    assertEquals("\"Value\"", actualTypeAdapter.toJson("Value"));
  }

  /**
   * Test {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)} with {@code gson}, {@code
   * declaredType}, {@code value}.
   *
   * <ul>
   *   <li>Then return toJson {@code Value} is {@code "Value"}.
   * </ul>
   *
   * <p>Method under test: {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)}
   */
  @Test
  @DisplayName(
      "Test getTypeAdapter(Gson, Class, Object) with 'gson', 'declaredType', 'value'; then return toJson 'Value' is '\"Value\"'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeAdapter _GsonUtil.getTypeAdapter(Gson, Class, Object)"})
  void testGetTypeAdapterWithGsonDeclaredTypeValue_thenReturnToJsonValueIsValue2() {
    // Arrange
    Gson gson = new Gson();
    Class<Entry> declaredType = Entry.class;

    // Act
    TypeAdapter actualTypeAdapter = _GsonUtil.getTypeAdapter(gson, declaredType, "Value");

    // Assert
    assertEquals("\"Value\"", actualTypeAdapter.toJson("Value"));
  }

  /**
   * Test {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)} with {@code gson}, {@code
   * declaredType}, {@code value}.
   *
   * <ul>
   *   <li>When {@code null}.
   *   <li>Then return {@link ObjectTypeAdapter}.
   * </ul>
   *
   * <p>Method under test: {@link _GsonUtil#getTypeAdapter(Gson, Class, Object)}
   */
  @Test
  @DisplayName(
      "Test getTypeAdapter(Gson, Class, Object) with 'gson', 'declaredType', 'value'; when 'null'; then return ObjectTypeAdapter")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeAdapter _GsonUtil.getTypeAdapter(Gson, Class, Object)"})
  void testGetTypeAdapterWithGsonDeclaredTypeValue_whenNull_thenReturnObjectTypeAdapter() {
    // Arrange
    Gson gson = new Gson();
    Class<Object> declaredType = Object.class;

    // Act
    TypeAdapter actualTypeAdapter = _GsonUtil.getTypeAdapter(gson, declaredType, null);

    // Assert
    assertTrue(actualTypeAdapter instanceof ObjectTypeAdapter);
  }
}
