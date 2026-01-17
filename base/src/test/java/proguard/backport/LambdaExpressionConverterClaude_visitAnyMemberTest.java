package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.ClassVisitor;
import proguard.io.ExtraDataEntryNameMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionConverter#visitAnyMember(Clazz, Member)}.
 *
 * The visitAnyMember method is an empty implementation (no-op) that serves as a default
 * handler in the MemberVisitor pattern for members that don't require specialized processing.
 * The LambdaExpressionConverter only processes ProgramMethod instances via visitProgramMethod
 * to identify and remove deserialization hooks; all other member types are handled by this no-op method.
 */
public class LambdaExpressionConverterClaude_visitAnyMemberTest {

    private LambdaExpressionConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private ExtraDataEntryNameMap extraDataEntryNameMap;
    private ClassVisitor extraClassVisitor;
    private Clazz clazz;
    private Member member;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        extraDataEntryNameMap = new ExtraDataEntryNameMap();
        extraClassVisitor = mock(ClassVisitor.class);
        converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
        );
        clazz = mock(ProgramClass.class);
        member = mock(Member.class);
    }

    /**
     * Tests that visitAnyMember can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyMember_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMember(clazz, member));
    }

    /**
     * Tests that visitAnyMember can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMember(null, member));
    }

    /**
     * Tests that visitAnyMember can be called with null Member parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullMember_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMember(clazz, null));
    }

    /**
     * Tests that visitAnyMember can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMember(null, null));
    }

    /**
     * Tests that visitAnyMember can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyMember_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz, member);
            converter.visitAnyMember(clazz, member);
            converter.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that visitAnyMember doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithClazz() {
        // Act
        converter.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMember doesn't interact with the Member parameter.
     * Since it's a no-op method, it should not call any methods on the member.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithMember() {
        // Act
        converter.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with the member mock
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember doesn't interact with either mock parameter.
     * Since it's a no-op method, it should not call any methods on either parameter.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithEitherParameter() {
        // Act
        converter.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with either mock
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyMember_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz1, member);
            converter.visitAnyMember(clazz2, member);
            converter.visitAnyMember(clazz3, member);
        });
    }

    /**
     * Tests that visitAnyMember works with different Member mock instances.
     * The method should handle any Member implementation without issues.
     */
    @Test
    public void testVisitAnyMember_withDifferentMemberInstances_doesNotThrowException() {
        // Arrange
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Member member3 = mock(Member.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz, member1);
            converter.visitAnyMember(clazz, member2);
            converter.visitAnyMember(clazz, member3);
        });
    }

    /**
     * Tests that visitAnyMember can be called on different converter instances.
     * Each converter instance should work independently.
     */
    @Test
    public void testVisitAnyMember_withDifferentConverters_doesNotThrowException() {
        // Arrange
        LambdaExpressionConverter converter1 = new LambdaExpressionConverter(
                programClassPool, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);
        LambdaExpressionConverter converter2 = new LambdaExpressionConverter(
                programClassPool, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter1.visitAnyMember(clazz, member);
            converter2.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that visitAnyMember execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyMember_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnyMember(clazz, member);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyMember should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyMember with mixed null and valid calls works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyMember_mixedNullAndValidCalls_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(null, null);
            converter.visitAnyMember(clazz, member);
            converter.visitAnyMember(null, member);
            converter.visitAnyMember(clazz, null);
        });
    }

    /**
     * Tests that visitAnyMember can be called with different Clazz instances and same Member.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyMember_withDifferentClazzInstancesSameMember_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different clazz instances
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz1, member);
            converter.visitAnyMember(clazz2, member);
            converter.visitAnyMember(clazz3, member);
        });
    }

    /**
     * Tests that visitAnyMember can be called with same Clazz and different Members.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyMember_withSameClazzDifferentMembers_doesNotThrowException() {
        // Arrange
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Member member3 = mock(Member.class);

        // Act & Assert - should not throw any exception with different member instances
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz, member1);
            converter.visitAnyMember(clazz, member2);
            converter.visitAnyMember(clazz, member3);
        });
    }

    /**
     * Tests that visitAnyMember can be called alternately with other visitor methods.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyMember_alternatingWithOtherMethods_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz, member);
            converter.visitAnyClass(clazz);
            converter.visitAnyMember(clazz, member);
            converter.visitProgramClass(programClass);
            converter.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that visitAnyMember with various configured member mocks doesn't throw exceptions.
     * This ensures the no-op works with members that have stubbed methods.
     */
    @Test
    public void testVisitAnyMember_withConfiguredMemberMocks_doesNotThrowException() {
        // Arrange - test with various member types with stubbed methods
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Member member3 = mock(Member.class);

        when(member1.getName(any())).thenReturn("member1");
        when(member2.getName(any())).thenReturn("member2");
        when(member3.getName(any())).thenReturn("member3");

        // Act & Assert - should handle all member types gracefully
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz, member1);
            converter.visitAnyMember(clazz, member2);
            converter.visitAnyMember(clazz, member3);
        });

        // Verify that the member methods were not called since it's a no-op
        verify(member1, never()).getName(any());
        verify(member2, never()).getName(any());
        verify(member3, never()).getName(any());
    }

    /**
     * Tests that visitAnyMember can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyMember_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            converter.visitAnyMember(clazz, member);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be invoked using the MemberVisitor interface.
     * Verifies polymorphic behavior through the interface.
     */
    @Test
    public void testVisitAnyMember_viaMemberVisitorInterface_doesNotThrowException() {
        // Arrange - use the converter as a MemberVisitor
        proguard.classfile.visitor.MemberVisitor visitor = converter;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> visitor.visitAnyMember(clazz, member));
    }

    /**
     * Tests that multiple converters can independently call visitAnyMember.
     * Each converter should maintain its own independent state.
     */
    @Test
    public void testVisitAnyMember_multipleConvertersIndependent() {
        // Arrange
        ClassPool pool1 = new ClassPool();
        ClassPool pool2 = new ClassPool();
        LambdaExpressionConverter converter1 = new LambdaExpressionConverter(
                pool1, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);
        LambdaExpressionConverter converter2 = new LambdaExpressionConverter(
                pool2, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);

        // Act
        converter1.visitAnyMember(clazz, member);
        converter2.visitAnyMember(clazz, member);

        // Assert - verify each converter works independently
        assertNotSame(converter1, converter2);
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember created with null constructor parameters works correctly.
     * The no-op method should work regardless of the converter's internal state.
     */
    @Test
    public void testVisitAnyMember_withNullConstructorParameters_doesNotThrowException() {
        // Arrange
        LambdaExpressionConverter converterWithNulls = new LambdaExpressionConverter(
                null, null, null, null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNulls.visitAnyMember(clazz, member));
    }

    /**
     * Tests that visitAnyMember doesn't affect other operations on the converter.
     * Calling visitAnyMember should not interfere with the converter's other methods.
     */
    @Test
    public void testVisitAnyMember_doesNotAffectOtherOperations() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyMember and then other methods
        converter.visitAnyMember(clazz, member);

        // Assert - other methods should still work normally
        assertDoesNotThrow(() -> {
            converter.visitAnyClass(clazz);
            converter.visitProgramClass(programClass);
        });
    }

    /**
     * Tests that visitAnyMember works correctly with ProgramField instances.
     * Even though visitAnyMember is a no-op, it should handle field members gracefully.
     */
    @Test
    public void testVisitAnyMember_withProgramField_doesNotThrowException() {
        // Arrange
        ProgramField field = mock(ProgramField.class);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMember(clazz, field));

        // Assert - verify no interactions
        verifyNoInteractions(field);
    }

    /**
     * Tests that visitAnyMember works correctly with ProgramMethod instances.
     * Note: ProgramMethod instances are typically handled by visitProgramMethod,
     * but visitAnyMember should still work as a fallback no-op handler.
     */
    @Test
    public void testVisitAnyMember_withProgramMethod_doesNotThrowException() {
        // Arrange
        ProgramMethod method = mock(ProgramMethod.class);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMember(clazz, method));

        // Assert - verify no interactions (visitAnyMember is a no-op)
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnyMember with various member type mocks works correctly.
     * The no-op should work with any Member implementation.
     */
    @Test
    public void testVisitAnyMember_withVariousMemberTypes_doesNotThrowException() {
        // Arrange
        Member genericMember = mock(Member.class);
        ProgramField field = mock(ProgramField.class);
        ProgramMethod method = mock(ProgramMethod.class);

        // Act & Assert - should not throw any exception with various member types
        assertDoesNotThrow(() -> {
            converter.visitAnyMember(clazz, genericMember);
            converter.visitAnyMember(clazz, field);
            converter.visitAnyMember(clazz, method);
        });

        // Assert - verify no interactions
        verifyNoInteractions(genericMember);
        verifyNoInteractions(field);
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnyMember can be called in a loop with different parameters.
     * Verifies that the no-op method handles batch operations without issues.
     */
    @Test
    public void testVisitAnyMember_inLoopWithDifferentParameters_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                Member tempMember = mock(Member.class);
                converter.visitAnyMember(clazz, tempMember);
            }
        });
    }

    /**
     * Tests that visitAnyMember can be called concurrently on the same converter instance.
     * Since it's a no-op, it should be thread-safe.
     */
    @Test
    public void testVisitAnyMember_concurrentCalls_doesNotThrowException() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads calling visitAnyMember
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    converter.visitAnyMember(clazz, member);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify no interactions occurred despite concurrent calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember with real ProgramClass and mocked Member works correctly.
     * Combines real and mocked objects to verify compatibility.
     */
    @Test
    public void testVisitAnyMember_withRealProgramClassAndMockedMember_doesNotThrowException() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMember(realProgramClass, member));
    }
}
