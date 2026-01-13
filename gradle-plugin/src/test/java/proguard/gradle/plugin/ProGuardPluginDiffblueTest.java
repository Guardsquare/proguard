package proguard.gradle.plugin;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.internal.project.LifecycleAwareProject;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.project.ProjectState;
import org.gradle.internal.buildtree.BuildModelParameters;
import org.gradle.internal.cc.impl.CoupledProjectsListener;
import org.gradle.internal.cc.impl.CrossProjectModelAccessInstance;
import org.gradle.internal.cc.impl.CrossProjectModelAccessPattern;
import org.gradle.internal.cc.impl.DynamicCallProblemReporting;
import org.gradle.internal.cc.impl.ProblemReportingCrossProjectModelAccess;
import org.gradle.internal.cc.impl.ProblemReportingCrossProjectModelAccess.ProblemReportingProject;
import org.gradle.internal.configuration.problems.ProblemFactory;
import org.gradle.internal.configuration.problems.ProblemFactory.Builder;
import org.gradle.internal.configuration.problems.ProblemsListener;
import org.gradle.internal.configuration.problems.PropertyProblem;
import org.gradle.internal.configuration.problems.StructuredMessage;
import org.gradle.internal.extensibility.DefaultConvention;
import org.gradle.internal.instantiation.InstanceGenerator;
import org.gradle.invocation.GradleLifecycleActionExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProGuardPluginDiffblueTest {
  /**
   * Test {@link ProGuardPlugin#apply(Project)} with {@code project}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then calls {@link ProjectInternal#compareTo(Object)}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardPlugin#apply(Project)}
   */
  @Test
  @DisplayName("Test apply(Project) with 'project'; given one; then calls compareTo(Object)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardPlugin.apply(Project)"})
  void testApplyWithProject_givenOne_thenCallsCompareTo() {
    // Arrange
    ProGuardPlugin proGuardPlugin = new ProGuardPlugin();

    ProjectInternal delegate = mock(ProjectInternal.class);
    when(delegate.compareTo(Mockito.<Project>any())).thenReturn(1);
    when(delegate.getExtensions()).thenReturn(new DefaultConvention(mock(InstanceGenerator.class)));
    when(delegate.getOwner()).thenReturn(mock(ProjectState.class));

    ProjectInternal referrer = mock(ProjectInternal.class);
    when(referrer.getOwner()).thenReturn(mock(ProjectState.class));

    ProblemsListener problems = mock(ProblemsListener.class);
    doNothing().when(problems).onProblem(Mockito.<PropertyProblem>any());

    CoupledProjectsListener coupledProjectsListener = mock(CoupledProjectsListener.class);
    doNothing()
        .when(coupledProjectsListener)
        .onProjectReference(Mockito.<ProjectState>any(), Mockito.<ProjectState>any());

    Builder builder = mock(Builder.class);
    when(builder.build()).thenReturn(null);

    Builder builder2 = mock(Builder.class);
    when(builder2.exception()).thenReturn(builder);

    ProblemFactory problemFactory = mock(ProblemFactory.class);
    when(problemFactory.problem(
            Mockito.<String>any(), Mockito.<Function1<StructuredMessage.Builder, Unit>>any()))
        .thenReturn(builder2);
    LifecycleAwareProject relativeTo =
        new LifecycleAwareProject(null, null, mock(GradleLifecycleActionExecutor.class));

    ProblemReportingProject project =
        new ProblemReportingProject(
            delegate,
            referrer,
            new CrossProjectModelAccessInstance(CrossProjectModelAccessPattern.DIRECT, relativeTo),
            problems,
            coupledProjectsListener,
            problemFactory,
            mock(BuildModelParameters.class),
            mock(DynamicCallProblemReporting.class));

    // Act and Assert
    assertThrows(GradleException.class, () -> proGuardPlugin.apply(project));
    verify(delegate, atLeast(1)).compareTo(isA(Project.class));
    verify(delegate, atLeast(1)).getExtensions();
    verify(delegate, atLeast(1)).getOwner();
    verify(referrer, atLeast(1)).getOwner();
    verify(coupledProjectsListener, atLeast(1))
        .onProjectReference(isA(ProjectState.class), isA(ProjectState.class));
    verify(problemFactory, atLeast(1))
        .problem(isNull(), Mockito.<Function1<StructuredMessage.Builder, Unit>>any());
    verify(builder, atLeast(1)).build();
    verify(builder2, atLeast(1)).exception();
    verify(problems, atLeast(1)).onProblem(isNull());
  }

  /**
   * Test {@link ProGuardPlugin#apply(Project)} with {@code project}.
   *
   * <ul>
   *   <li>Then calls {@link GradleLifecycleActionExecutor#executeBeforeProjectFor(Project)}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardPlugin#apply(Project)}
   */
  @Test
  @DisplayName("Test apply(Project) with 'project'; then calls executeBeforeProjectFor(Project)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardPlugin.apply(Project)"})
  void testApplyWithProject_thenCallsExecuteBeforeProjectFor() {
    // Arrange
    ProGuardPlugin proGuardPlugin = new ProGuardPlugin();

    ProjectInternal delegate = mock(ProjectInternal.class);
    when(delegate.getExtensions()).thenReturn(new DefaultConvention(mock(InstanceGenerator.class)));

    GradleLifecycleActionExecutor gradleLifecycleActionExecutor =
        mock(GradleLifecycleActionExecutor.class);
    doNothing().when(gradleLifecycleActionExecutor).executeBeforeProjectFor(Mockito.<Project>any());

    LifecycleAwareProject project =
        new LifecycleAwareProject(
            delegate, mock(ProjectInternal.class), gradleLifecycleActionExecutor);

    // Act and Assert
    assertThrows(GradleException.class, () -> proGuardPlugin.apply(project));
    verify(delegate, atLeast(1)).getExtensions();
    verify(gradleLifecycleActionExecutor, atLeast(1)).executeBeforeProjectFor(isA(Project.class));
  }
}
