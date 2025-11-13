package proguard.optimize.kotlin;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;

class KotlinContextReceiverUsageMarkerDiffblueTest {
  /**
   * Test {@link KotlinContextReceiverUsageMarker#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then calls {@link KotlinClassKindMetadata#constructorsAccept(Clazz,
   *       KotlinConstructorVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinContextReceiverUsageMarker#visitKotlinClassMetadata(Clazz,
   * KotlinClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata); then calls constructorsAccept(Clazz, KotlinConstructorVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinContextReceiverUsageMarker.visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)"
  })
  void testVisitKotlinClassMetadata_thenCallsConstructorsAccept() {
    // Arrange
    KotlinContextReceiverUsageMarker kotlinContextReceiverUsageMarker =
        new KotlinContextReceiverUsageMarker();
    LibraryClass clazz = new LibraryClass();

    KotlinClassKindMetadata kotlinClassKindMetadata = mock(KotlinClassKindMetadata.class);
    doNothing()
        .when(kotlinClassKindMetadata)
        .constructorsAccept(Mockito.<Clazz>any(), Mockito.<KotlinConstructorVisitor>any());

    // Act
    kotlinContextReceiverUsageMarker.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);

    // Assert
    verify(kotlinClassKindMetadata)
        .constructorsAccept(isA(Clazz.class), isA(KotlinConstructorVisitor.class));
  }
}
