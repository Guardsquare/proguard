package proguard.optimize;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ClassMemberPair;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.optimize.ReverseDependencyStore.InfluencedMethodTraveller;
import proguard.optimize.info.ProgramMethodOptimizationInfo;
import proguard.util.MultiValueMap;

class ReverseDependencyStoreDiffblueTest {
  /**
   * Test InfluencedMethodTraveller {@link
   * InfluencedMethodTraveller#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link InfluencedMethodTraveller#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test InfluencedMethodTraveller visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InfluencedMethodTraveller.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testInfluencedMethodTravellerVisitProgramMethod() {
    // Arrange
    MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByProgramMethodOptimizationInfo =
        mock(MultiValueMap.class);
    when(methodsByProgramMethodOptimizationInfo.get(Mockito.<ProgramMethodOptimizationInfo>any()))
        .thenReturn(new HashSet<>());
    doNothing()
        .when(methodsByProgramMethodOptimizationInfo)
        .put(Mockito.<ProgramMethodOptimizationInfo>any(), Mockito.<Method>any());
    doNothing()
        .when(methodsByProgramMethodOptimizationInfo)
        .putAll(Mockito.<Set<ProgramMethodOptimizationInfo>>any(), Mockito.<Method>any());
    HashSet<ProgramMethodOptimizationInfo> key = new HashSet<>();
    methodsByProgramMethodOptimizationInfo.putAll(key, new LibraryMethod());
    methodsByProgramMethodOptimizationInfo.put(null, new LibraryMethod());
    ReverseDependencyStore reverseDependencyStore =
        new ReverseDependencyStore(new MultiValueMap<>(), methodsByProgramMethodOptimizationInfo);
    InfluencedMethodTraveller influencedMethodTraveller =
        reverseDependencyStore.new InfluencedMethodTraveller(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getProcessingInfo()).thenReturn(mock(ProgramMethodOptimizationInfo.class));
    doNothing().when(programMethod).addProcessingFlags((int[]) Mockito.any());
    doNothing().when(programMethod).setProcessingInfo(Mockito.<Object>any());
    programMethod.addProcessingFlags(2, 1, 2, 1);
    programMethod.setProcessingInfo(1);

    // Act
    influencedMethodTraveller.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodsByProgramMethodOptimizationInfo).get(isA(ProgramMethodOptimizationInfo.class));
    verify(methodsByProgramMethodOptimizationInfo).put(isNull(), isA(Method.class));
    verify(methodsByProgramMethodOptimizationInfo).putAll(isA(Set.class), isA(Method.class));
    verify(programMethod).addProcessingFlags((int[]) Mockito.any());
    verify(programMethod, atLeast(1)).getProcessingInfo();
    verify(programMethod).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test InfluencedMethodTraveller {@link
   * InfluencedMethodTraveller#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link InfluencedMethodTraveller#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test InfluencedMethodTraveller visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InfluencedMethodTraveller.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testInfluencedMethodTravellerVisitProgramMethod2() {
    // Arrange
    HashSet<Method> methodSet = new HashSet<>();
    methodSet.add(new LibraryMethod());

    MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByProgramMethodOptimizationInfo =
        mock(MultiValueMap.class);
    when(methodsByProgramMethodOptimizationInfo.get(Mockito.<ProgramMethodOptimizationInfo>any()))
        .thenReturn(methodSet);
    doNothing()
        .when(methodsByProgramMethodOptimizationInfo)
        .put(Mockito.<ProgramMethodOptimizationInfo>any(), Mockito.<Method>any());
    doNothing()
        .when(methodsByProgramMethodOptimizationInfo)
        .putAll(Mockito.<Set<ProgramMethodOptimizationInfo>>any(), Mockito.<Method>any());
    HashSet<ProgramMethodOptimizationInfo> key = new HashSet<>();
    methodsByProgramMethodOptimizationInfo.putAll(key, new LibraryMethod());
    methodsByProgramMethodOptimizationInfo.put(null, new LibraryMethod());
    ReverseDependencyStore reverseDependencyStore =
        new ReverseDependencyStore(new MultiValueMap<>(), methodsByProgramMethodOptimizationInfo);
    InfluencedMethodTraveller influencedMethodTraveller =
        reverseDependencyStore.new InfluencedMethodTraveller(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getProcessingInfo()).thenReturn(mock(ProgramMethodOptimizationInfo.class));
    doNothing().when(programMethod).addProcessingFlags((int[]) Mockito.any());
    doNothing().when(programMethod).setProcessingInfo(Mockito.<Object>any());
    programMethod.addProcessingFlags(2, 1, 2, 1);
    programMethod.setProcessingInfo(1);

    // Act
    influencedMethodTraveller.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodsByProgramMethodOptimizationInfo).get(isA(ProgramMethodOptimizationInfo.class));
    verify(methodsByProgramMethodOptimizationInfo).put(isNull(), isA(Method.class));
    verify(methodsByProgramMethodOptimizationInfo).putAll(isA(Set.class), isA(Method.class));
    verify(programMethod).addProcessingFlags((int[]) Mockito.any());
    verify(programMethod, atLeast(1)).getProcessingInfo();
    verify(programMethod).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test InfluencedMethodTraveller {@link
   * InfluencedMethodTraveller#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MultiValueMap#keySet()}.
   * </ul>
   *
   * <p>Method under test: {@link InfluencedMethodTraveller#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test InfluencedMethodTraveller visitProgramMethod(ProgramClass, ProgramMethod); then calls keySet()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InfluencedMethodTraveller.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testInfluencedMethodTravellerVisitProgramMethod_thenCallsKeySet() {
    // Arrange
    MultiValueMap<Method, ClassMemberPair> calledBy = mock(MultiValueMap.class);
    when(calledBy.keySet()).thenReturn(new HashSet<>());

    HashSet<Method> methodSet = new HashSet<>();
    methodSet.add(new LibraryMethod());

    MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByProgramMethodOptimizationInfo =
        mock(MultiValueMap.class);
    when(methodsByProgramMethodOptimizationInfo.get(Mockito.<ProgramMethodOptimizationInfo>any()))
        .thenReturn(methodSet);
    doNothing()
        .when(methodsByProgramMethodOptimizationInfo)
        .put(Mockito.<ProgramMethodOptimizationInfo>any(), Mockito.<Method>any());
    doNothing()
        .when(methodsByProgramMethodOptimizationInfo)
        .putAll(Mockito.<Set<ProgramMethodOptimizationInfo>>any(), Mockito.<Method>any());
    HashSet<ProgramMethodOptimizationInfo> key = new HashSet<>();
    methodsByProgramMethodOptimizationInfo.putAll(key, new LibraryMethod());
    methodsByProgramMethodOptimizationInfo.put(null, new LibraryMethod());

    ReverseDependencyStore reverseDependencyStore =
        new ReverseDependencyStore(calledBy, methodsByProgramMethodOptimizationInfo);
    InfluencedMethodTraveller influencedMethodTraveller =
        reverseDependencyStore.new InfluencedMethodTraveller(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getProcessingInfo()).thenReturn(mock(ProgramMethodOptimizationInfo.class));
    doNothing().when(programMethod).addProcessingFlags((int[]) Mockito.any());
    doNothing().when(programMethod).setProcessingInfo(Mockito.<Object>any());
    programMethod.addProcessingFlags(2, 1, 2, 1);
    programMethod.setProcessingInfo(1);

    // Act
    influencedMethodTraveller.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodsByProgramMethodOptimizationInfo).get(isA(ProgramMethodOptimizationInfo.class));
    verify(calledBy).keySet();
    verify(methodsByProgramMethodOptimizationInfo).put(isNull(), isA(Method.class));
    verify(methodsByProgramMethodOptimizationInfo).putAll(isA(Set.class), isA(Method.class));
    verify(programMethod).addProcessingFlags((int[]) Mockito.any());
    verify(programMethod, atLeast(1)).getProcessingInfo();
    verify(programMethod).setProcessingInfo(isA(Object.class));
  }
}
