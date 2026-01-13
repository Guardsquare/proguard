package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.util.function.BiFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.kotlin.UnsupportedKotlinMetadata;
import proguard.classfile.visitor.ClassVisitor;
import proguard.io.ClassPath;
import proguard.io.ClassPathEntry;
import proguard.io.DataEntryReader;
import proguard.io.util.IOUtil;

class KotlinMetadataAdapterDiffblueTest {
  /**
   * Test {@link KotlinMetadataAdapter#execute(AppView)}.
   *
   * <ul>
   *   <li>Then calls {@link BiFunction#apply(Object, Object)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinMetadataAdapter#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView); then calls apply(Object, Object)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinMetadataAdapter.execute(AppView)"})
  void testExecute_thenCallsApply() throws IOException {
    // Arrange
    KotlinMetadataAdapter kotlinMetadataAdapter = new KotlinMetadataAdapter();

    BiFunction<DataEntryReader, ClassVisitor, DataEntryReader> extraDataEntryReader =
        mock(BiFunction.class);
    when(extraDataEntryReader.apply(Mockito.<DataEntryReader>any(), Mockito.<ClassVisitor>any()))
        .thenReturn(mock(DataEntryReader.class));

    ClassPool programClassPool =
        IOUtil.read(
            new ClassPath(new ClassPathEntry(Configuration.STD_OUT, true)),
            "Adapting Kotlin metadata...",
            true,
            true,
            true,
            true,
            true,
            true,
            extraDataEntryReader);
    UnsupportedKotlinMetadata kotlinMetadata =
        new UnsupportedKotlinMetadata(
            1,
            new int[] {1, 0, 1, 0},
            1,
            "Adapting Kotlin metadata...",
            "Adapting Kotlin metadata...");
    LibraryClass clazz =
        new LibraryClass(
            1, "Adapting Kotlin metadata...", "Adapting Kotlin metadata...", kotlinMetadata);
    programClassPool.addClass("Name", clazz);

    // Act
    kotlinMetadataAdapter.execute(new AppView(programClassPool, KotlinConstants.dummyClassPool));

    // Assert
    verify(extraDataEntryReader).apply(isA(DataEntryReader.class), isA(ClassVisitor.class));
  }

  /**
   * Test new {@link KotlinMetadataAdapter} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link KotlinMetadataAdapter}
   */
  @Test
  @DisplayName("Test new KotlinMetadataAdapter (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinMetadataAdapter.<init>()"})
  void testNewKotlinMetadataAdapter() {
    // Arrange, Act and Assert
    assertEquals("proguard.KotlinMetadataAdapter", new KotlinMetadataAdapter().getName());
  }
}
