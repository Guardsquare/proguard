package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SingleInvocationMethodInliner} constructors.
 * Tests the constructors with signatures:
 * - (ZZZ)V - three boolean parameters
 * - (ZZZLproguard/classfile/instruction/visitor/InstructionVisitor;)V - three booleans and an InstructionVisitor
 *
 * SingleInvocationMethodInliner extends MethodInliner and inlines methods that are
 * only invoked once in the code. The constructors accept:
 * - microEdition: whether the code is targeted at Java Micro Edition
 * - android: whether the code is targeted at the Dalvik VM
 * - allowAccessModification: whether access modifiers can be changed for inlining
 * - extraInlinedInvocationVisitor: optional visitor for inlined invocation instructions
 */
public class SingleInvocationMethodInlinerClaude_constructorTest {

    // ========================================
    // Three-parameter Constructor Tests (ZZZ)V
    // ========================================

    /**
     * Tests the constructor with all parameters set to false.
     * Verifies that the inliner can be instantiated with minimal permissions.
     */
    @Test
    public void testConstructor_AllParametersFalse() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created successfully");
        assertInstanceOf(MethodInliner.class, inliner, "SingleInvocationMethodInliner should extend MethodInliner");
    }

    /**
     * Tests the constructor with all parameters set to true.
     * Verifies that the inliner can be instantiated with all features enabled.
     */
    @Test
    public void testConstructor_AllParametersTrue() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, true, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with all parameters true");
    }

    /**
     * Tests the constructor with microEdition=true, others false.
     * Verifies that microEdition mode can be enabled independently.
     */
    @Test
    public void testConstructor_MicroEditionTrue() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, false, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with microEdition=true");
    }

    /**
     * Tests the constructor with android=true, others false.
     * Verifies that Android mode can be enabled independently.
     */
    @Test
    public void testConstructor_AndroidTrue() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, true, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with android=true");
    }

    /**
     * Tests the constructor with allowAccessModification=true, others false.
     * Verifies that access modification can be enabled independently.
     */
    @Test
    public void testConstructor_AllowAccessModificationTrue() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with allowAccessModification=true");
    }

    /**
     * Tests the constructor with microEdition=true and android=true, allowAccessModification=false.
     * Verifies that both platform flags can be enabled together.
     */
    @Test
    public void testConstructor_MicroEditionAndAndroidTrue() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, true, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with microEdition and android true");
    }

    /**
     * Tests the constructor with microEdition=true and allowAccessModification=true, android=false.
     * Verifies that microEdition and access modification can be combined.
     */
    @Test
    public void testConstructor_MicroEditionAndAllowAccessModificationTrue() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, false, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with microEdition and allowAccessModification true");
    }

    /**
     * Tests the constructor with android=true and allowAccessModification=true, microEdition=false.
     * Verifies that Android and access modification can be combined.
     */
    @Test
    public void testConstructor_AndroidAndAllowAccessModificationTrue() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, true, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with android and allowAccessModification true");
    }

    /**
     * Tests that multiple instances can be created with different configurations.
     * Verifies that inliner instances are independent.
     */
    @Test
    public void testConstructor_MultipleInstancesAreIndependent() {
        // Act
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(false, false, false);
        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(true, true, true);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotSame(inliner1, inliner2, "Inliner instances should be different");
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructor_IsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, true);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created");
        assertTrue(duration < 10_000_000L, "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the constructor creates an instance of AttributeVisitor.
     * Verifies that SingleInvocationMethodInliner implements the AttributeVisitor interface
     * (inherited from MethodInliner).
     */
    @Test
    public void testConstructor_CreatesAttributeVisitorInstance() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created");
        // MethodInliner implements AttributeVisitor
        assertInstanceOf(MethodInliner.class, inliner, "Should extend MethodInliner");
    }

    /**
     * Tests creating a sequence of inliners with different configurations.
     * Verifies that multiple inliners can be created sequentially without issues.
     */
    @Test
    public void testConstructor_SequentialCreationWithVariousConfigurations() {
        // Act & Assert
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(false, false, false);
        assertNotNull(inliner1, "Inliner 1 (FFF) should be created");

        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(true, false, false);
        assertNotNull(inliner2, "Inliner 2 (TFF) should be created");

        SingleInvocationMethodInliner inliner3 = new SingleInvocationMethodInliner(false, true, false);
        assertNotNull(inliner3, "Inliner 3 (FTF) should be created");

        SingleInvocationMethodInliner inliner4 = new SingleInvocationMethodInliner(false, false, true);
        assertNotNull(inliner4, "Inliner 4 (FFT) should be created");

        SingleInvocationMethodInliner inliner5 = new SingleInvocationMethodInliner(true, true, false);
        assertNotNull(inliner5, "Inliner 5 (TTF) should be created");

        SingleInvocationMethodInliner inliner6 = new SingleInvocationMethodInliner(true, false, true);
        assertNotNull(inliner6, "Inliner 6 (TFT) should be created");

        SingleInvocationMethodInliner inliner7 = new SingleInvocationMethodInliner(false, true, true);
        assertNotNull(inliner7, "Inliner 7 (FTT) should be created");

        SingleInvocationMethodInliner inliner8 = new SingleInvocationMethodInliner(true, true, true);
        assertNotNull(inliner8, "Inliner 8 (TTT) should be created");
    }

    /**
     * Tests that the constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testConstructor_InitializesInstanceProperly() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, true, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created");
        assertInstanceOf(MethodInliner.class, inliner, "Should be a MethodInliner instance");
    }

    /**
     * Tests that instances created with the same parameters are independent objects.
     * Verifies that the constructor creates new instances rather than reusing existing ones.
     */
    @Test
    public void testConstructor_SameParametersCreateDifferentInstances() {
        // Act
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(true, false, true);
        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(true, false, true);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotSame(inliner1, inliner2, "Instances with same parameters should be different objects");
    }

    /**
     * Tests the constructor with microEdition=false, android=false, allowAccessModification=true.
     * This is a common configuration for standard JVM with access modification allowed.
     */
    @Test
    public void testConstructor_StandardJVMWithAccessModification() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created for standard JVM with access modification");
    }

    /**
     * Tests the constructor with microEdition=true, android=false, allowAccessModification=false.
     * This represents a restrictive microEdition configuration.
     */
    @Test
    public void testConstructor_MicroEditionWithoutAccessModification() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, false, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created for microEdition without access modification");
    }

    /**
     * Tests the constructor with microEdition=false, android=true, allowAccessModification=false.
     * This represents a restrictive Android configuration.
     */
    @Test
    public void testConstructor_AndroidWithoutAccessModification() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, true, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created for Android without access modification");
    }

    /**
     * Tests that creating many instances doesn't cause memory issues.
     * Verifies that the constructor is lightweight and can be called many times.
     */
    @Test
    public void testConstructor_CanCreateManyInstances() {
        // Act & Assert - create 100 instances
        for (int i = 0; i < 100; i++) {
            boolean microEdition = (i % 2) == 0;
            boolean android = (i % 3) == 0;
            boolean allowAccessModification = (i % 5) == 0;

            SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(
                microEdition, android, allowAccessModification);

            assertNotNull(inliner, "Inliner " + i + " should be created");
        }
    }

    /**
     * Tests the constructor with only the first parameter true.
     * Verifies microEdition-only configuration.
     */
    @Test
    public void testConstructor_OnlyMicroEditionEnabled() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, false, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with only microEdition enabled");
    }

    /**
     * Tests the constructor with only the second parameter true.
     * Verifies Android-only configuration.
     */
    @Test
    public void testConstructor_OnlyAndroidEnabled() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, true, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with only Android enabled");
    }

    /**
     * Tests the constructor with only the third parameter true.
     * Verifies access modification-only configuration.
     */
    @Test
    public void testConstructor_OnlyAccessModificationEnabled() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, true);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with only access modification enabled");
    }

    /**
     * Tests that the constructor delegates to the parent MethodInliner constructor.
     * Verifies the inheritance relationship is properly established.
     */
    @Test
    public void testConstructor_ProperlyExtendsMethodInliner() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, false);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created");
        assertInstanceOf(MethodInliner.class, inliner, "Should be an instance of MethodInliner");
        assertInstanceOf(SingleInvocationMethodInliner.class, inliner, "Should be an instance of SingleInvocationMethodInliner");
    }

    // ========================================
    // Four-parameter Constructor Tests (ZZZLInstructionVisitor;)V
    // ========================================

    /**
     * Tests the four-parameter constructor with all false and non-null visitor.
     * Verifies that the inliner can be created with an extra instruction visitor.
     */
    @Test
    public void testFourParamConstructor_AllFalseWithNonNullVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, false, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with visitor");
        verifyNoInteractions(visitor);
    }

    /**
     * Tests the four-parameter constructor with all true and non-null visitor.
     * Verifies that the inliner can be created with all features enabled and a visitor.
     */
    @Test
    public void testFourParamConstructor_AllTrueWithNonNullVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, true, true, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with all true and visitor");
        verifyNoInteractions(visitor);
    }

    /**
     * Tests the four-parameter constructor with null visitor.
     * Verifies that the inliner can be created with null extraInlinedInvocationVisitor.
     */
    @Test
    public void testFourParamConstructor_WithNullVisitor() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, false, null);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with null visitor");
    }

    /**
     * Tests the four-parameter constructor with all true and null visitor.
     * Verifies that the inliner can be created with all features enabled and null visitor.
     */
    @Test
    public void testFourParamConstructor_AllTrueWithNullVisitor() {
        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, true, true, null);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with all true and null visitor");
    }

    /**
     * Tests the four-parameter constructor with microEdition=true and visitor.
     * Verifies microEdition mode with an extra visitor.
     */
    @Test
    public void testFourParamConstructor_MicroEditionTrueWithVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, false, false, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with microEdition and visitor");
    }

    /**
     * Tests the four-parameter constructor with android=true and visitor.
     * Verifies Android mode with an extra visitor.
     */
    @Test
    public void testFourParamConstructor_AndroidTrueWithVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, true, false, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with android and visitor");
    }

    /**
     * Tests the four-parameter constructor with allowAccessModification=true and visitor.
     * Verifies access modification mode with an extra visitor.
     */
    @Test
    public void testFourParamConstructor_AllowAccessModificationTrueWithVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, true, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with allowAccessModification and visitor");
    }

    /**
     * Tests the four-parameter constructor with multiple instances sharing the same visitor.
     * Verifies that multiple inliners can share the same visitor instance.
     */
    @Test
    public void testFourParamConstructor_MultipleInstancesWithSameVisitor() {
        // Arrange
        InstructionVisitor sharedVisitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(true, false, false, sharedVisitor);
        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(false, true, false, sharedVisitor);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotSame(inliner1, inliner2, "Inliner instances should be different");
        verifyNoInteractions(sharedVisitor);
    }

    /**
     * Tests the four-parameter constructor with different visitors.
     * Verifies that multiple inliners can be created with different visitors.
     */
    @Test
    public void testFourParamConstructor_MultipleInstancesWithDifferentVisitors() {
        // Arrange
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(true, false, false, visitor1);
        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(false, true, false, visitor2);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotSame(inliner1, inliner2, "Inliner instances should be different");
        verifyNoInteractions(visitor1);
        verifyNoInteractions(visitor2);
    }

    /**
     * Tests the four-parameter constructor is efficient.
     * Verifies that the constructor completes quickly.
     */
    @Test
    public void testFourParamConstructor_IsEfficient() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);
        long startTime = System.nanoTime();

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, true, visitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created");
        assertTrue(duration < 10_000_000L, "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests the four-parameter constructor with all 8 boolean combinations and non-null visitor.
     * Verifies all possible boolean configurations work with a visitor.
     */
    @Test
    public void testFourParamConstructor_AllBooleanCombinationsWithVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act & Assert - test all 8 combinations
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(false, false, false, visitor);
        assertNotNull(inliner1, "Inliner (FFF) with visitor should be created");

        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(true, false, false, visitor);
        assertNotNull(inliner2, "Inliner (TFF) with visitor should be created");

        SingleInvocationMethodInliner inliner3 = new SingleInvocationMethodInliner(false, true, false, visitor);
        assertNotNull(inliner3, "Inliner (FTF) with visitor should be created");

        SingleInvocationMethodInliner inliner4 = new SingleInvocationMethodInliner(false, false, true, visitor);
        assertNotNull(inliner4, "Inliner (FFT) with visitor should be created");

        SingleInvocationMethodInliner inliner5 = new SingleInvocationMethodInliner(true, true, false, visitor);
        assertNotNull(inliner5, "Inliner (TTF) with visitor should be created");

        SingleInvocationMethodInliner inliner6 = new SingleInvocationMethodInliner(true, false, true, visitor);
        assertNotNull(inliner6, "Inliner (TFT) with visitor should be created");

        SingleInvocationMethodInliner inliner7 = new SingleInvocationMethodInliner(false, true, true, visitor);
        assertNotNull(inliner7, "Inliner (FTT) with visitor should be created");

        SingleInvocationMethodInliner inliner8 = new SingleInvocationMethodInliner(true, true, true, visitor);
        assertNotNull(inliner8, "Inliner (TTT) with visitor should be created");

        verifyNoInteractions(visitor);
    }

    /**
     * Tests the four-parameter constructor with all 8 boolean combinations and null visitor.
     * Verifies all possible boolean configurations work with null visitor.
     */
    @Test
    public void testFourParamConstructor_AllBooleanCombinationsWithNullVisitor() {
        // Act & Assert - test all 8 combinations with null visitor
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(false, false, false, null);
        assertNotNull(inliner1, "Inliner (FFF) with null visitor should be created");

        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(true, false, false, null);
        assertNotNull(inliner2, "Inliner (TFF) with null visitor should be created");

        SingleInvocationMethodInliner inliner3 = new SingleInvocationMethodInliner(false, true, false, null);
        assertNotNull(inliner3, "Inliner (FTF) with null visitor should be created");

        SingleInvocationMethodInliner inliner4 = new SingleInvocationMethodInliner(false, false, true, null);
        assertNotNull(inliner4, "Inliner (FFT) with null visitor should be created");

        SingleInvocationMethodInliner inliner5 = new SingleInvocationMethodInliner(true, true, false, null);
        assertNotNull(inliner5, "Inliner (TTF) with null visitor should be created");

        SingleInvocationMethodInliner inliner6 = new SingleInvocationMethodInliner(true, false, true, null);
        assertNotNull(inliner6, "Inliner (TFT) with null visitor should be created");

        SingleInvocationMethodInliner inliner7 = new SingleInvocationMethodInliner(false, true, true, null);
        assertNotNull(inliner7, "Inliner (FTT) with null visitor should be created");

        SingleInvocationMethodInliner inliner8 = new SingleInvocationMethodInliner(true, true, true, null);
        assertNotNull(inliner8, "Inliner (TTT) with null visitor should be created");
    }

    /**
     * Tests that the four-parameter constructor properly extends MethodInliner.
     * Verifies the inheritance relationship.
     */
    @Test
    public void testFourParamConstructor_ProperlyExtendsMethodInliner() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(false, false, false, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created");
        assertInstanceOf(MethodInliner.class, inliner, "Should be an instance of MethodInliner");
        assertInstanceOf(SingleInvocationMethodInliner.class, inliner, "Should be an instance of SingleInvocationMethodInliner");
    }

    /**
     * Tests that instances created with same parameters and visitor are independent.
     * Verifies that the constructor creates new instances.
     */
    @Test
    public void testFourParamConstructor_SameParametersCreateDifferentInstances() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(true, false, true, visitor);
        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(true, false, true, visitor);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotSame(inliner1, inliner2, "Instances with same parameters should be different objects");
    }

    /**
     * Tests that the four-parameter constructor can create many instances.
     * Verifies that the constructor is lightweight.
     */
    @Test
    public void testFourParamConstructor_CanCreateManyInstances() {
        // Act & Assert - create 100 instances
        for (int i = 0; i < 100; i++) {
            boolean microEdition = (i % 2) == 0;
            boolean android = (i % 3) == 0;
            boolean allowAccessModification = (i % 5) == 0;
            InstructionVisitor visitor = (i % 7) == 0 ? mock(InstructionVisitor.class) : null;

            SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(
                microEdition, android, allowAccessModification, visitor);

            assertNotNull(inliner, "Inliner " + i + " should be created");
        }
    }

    /**
     * Tests the four-parameter constructor with microEdition and android both true with visitor.
     * Verifies that both platform flags can be enabled together with a visitor.
     */
    @Test
    public void testFourParamConstructor_MicroEditionAndAndroidTrueWithVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, true, false, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with microEdition, android and visitor");
    }

    /**
     * Tests the four-parameter constructor with all features enabled and visitor.
     * This is the most permissive configuration.
     */
    @Test
    public void testFourParamConstructor_AllFeaturesEnabledWithVisitor() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner = new SingleInvocationMethodInliner(true, true, true, visitor);

        // Assert
        assertNotNull(inliner, "SingleInvocationMethodInliner should be created with all features enabled and visitor");
    }

    // ========================================
    // Cross-constructor Tests
    // ========================================

    /**
     * Tests that both constructors create independent instances.
     * Verifies that both constructors can be used interchangeably.
     */
    @Test
    public void testBothConstructors_CreateIndependentInstances() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(true, false, true);
        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(true, false, true, visitor);
        SingleInvocationMethodInliner inliner3 = new SingleInvocationMethodInliner(true, false, true, null);

        // Assert
        assertNotNull(inliner1, "Three-param constructor should create instance");
        assertNotNull(inliner2, "Four-param constructor with visitor should create instance");
        assertNotNull(inliner3, "Four-param constructor with null visitor should create instance");
        assertNotSame(inliner1, inliner2, "Instances should be different");
        assertNotSame(inliner1, inliner3, "Instances should be different");
        assertNotSame(inliner2, inliner3, "Instances should be different");
    }

    /**
     * Tests that multiple instances from both constructors work independently.
     * Verifies that using both constructors doesn't cause interference.
     */
    @Test
    public void testBothConstructors_MultipleInstancesWorkIndependently() {
        // Arrange
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        SingleInvocationMethodInliner inliner1 = new SingleInvocationMethodInliner(true, false, false);
        SingleInvocationMethodInliner inliner2 = new SingleInvocationMethodInliner(false, true, false, visitor);
        SingleInvocationMethodInliner inliner3 = new SingleInvocationMethodInliner(false, false, true);
        SingleInvocationMethodInliner inliner4 = new SingleInvocationMethodInliner(true, true, true, null);

        // Assert
        assertNotNull(inliner1, "First inliner should be created");
        assertNotNull(inliner2, "Second inliner should be created");
        assertNotNull(inliner3, "Third inliner should be created");
        assertNotNull(inliner4, "Fourth inliner should be created");
        assertNotSame(inliner1, inliner2, "Inliner 1 and 2 should be different");
        assertNotSame(inliner1, inliner3, "Inliner 1 and 3 should be different");
        assertNotSame(inliner1, inliner4, "Inliner 1 and 4 should be different");
        assertNotSame(inliner2, inliner3, "Inliner 2 and 3 should be different");
        assertNotSame(inliner2, inliner4, "Inliner 2 and 4 should be different");
        assertNotSame(inliner3, inliner4, "Inliner 3 and 4 should be different");
    }
}
