/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.retrace;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FrameInfo.getMethodName() method.
 */
public class FrameInfoClaude_getMethodNameTest {

    @Test
    public void testGetMethodNameReturnsCorrectValue() {
        // Arrange
        String methodName = "myMethod";
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", "MyClass.java", 10, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals(methodName, result);
    }

    @Test
    public void testGetMethodNameWithNullValue() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com.example.Test", "Test.java", 5, "int", "field", null, null);

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMethodNameWithEmptyString() {
        // Arrange
        String methodName = "";
        FrameInfo frameInfo = new FrameInfo("TestClass", "Test.java", 1, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetMethodNameWithSimpleName() {
        // Arrange
        String methodName = "execute";
        FrameInfo frameInfo = new FrameInfo("Runner", "Runner.java", 20, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("execute", result);
    }

    @Test
    public void testGetMethodNameWithCamelCase() {
        // Arrange
        String methodName = "processData";
        FrameInfo frameInfo = new FrameInfo("Processor", "Processor.java", 30, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("processData", result);
    }

    @Test
    public void testGetMethodNameWithGetterPrefix() {
        // Arrange
        String methodName = "getName";
        FrameInfo frameInfo = new FrameInfo("Person", "Person.java", 40, "String", null, methodName, "()Ljava/lang/String;");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("getName", result);
    }

    @Test
    public void testGetMethodNameWithSetterPrefix() {
        // Arrange
        String methodName = "setName";
        FrameInfo frameInfo = new FrameInfo("Person", "Person.java", 50, "void", null, methodName, "(Ljava/lang/String;)V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("setName", result);
    }

    @Test
    public void testGetMethodNameWithIsPrefix() {
        // Arrange
        String methodName = "isValid";
        FrameInfo frameInfo = new FrameInfo("Validator", "Validator.java", 60, "boolean", null, methodName, "()Z");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("isValid", result);
    }

    @Test
    public void testGetMethodNameWithHasPrefix() {
        // Arrange
        String methodName = "hasNext";
        FrameInfo frameInfo = new FrameInfo("Iterator", "Iterator.java", 70, "boolean", null, methodName, "()Z");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("hasNext", result);
    }

    @Test
    public void testGetMethodNameWithConstructorName() {
        // Arrange
        String methodName = "<init>";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 80, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("<init>", result);
    }

    @Test
    public void testGetMethodNameWithStaticInitializer() {
        // Arrange
        String methodName = "<clinit>";
        FrameInfo frameInfo = new FrameInfo("StaticClass", "StaticClass.java", 90, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("<clinit>", result);
    }

    @Test
    public void testGetMethodNameWithObfuscatedName() {
        // Arrange
        String methodName = "a";
        FrameInfo frameInfo = new FrameInfo("a.b.c", "SourceFile", 100, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("a", result);
    }

    @Test
    public void testGetMethodNameWithSyntheticMethod() {
        // Arrange
        String methodName = "access$000";
        FrameInfo frameInfo = new FrameInfo("Outer", "Outer.java", 110, "int", null, methodName, "(LOuter;)I");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("access$000", result);
    }

    @Test
    public void testGetMethodNameWithMainMethod() {
        // Arrange
        String methodName = "main";
        FrameInfo frameInfo = new FrameInfo("MainClass", "MainClass.java", 120, "void", null, methodName, "([Ljava/lang/String;)V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("main", result);
    }

    @Test
    public void testGetMethodNameWithRunMethod() {
        // Arrange
        String methodName = "run";
        FrameInfo frameInfo = new FrameInfo("Runnable", "Runnable.java", 130, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("run", result);
    }

    @Test
    public void testGetMethodNameMultipleCalls() {
        // Arrange
        String methodName = "calculate";
        FrameInfo frameInfo = new FrameInfo("Calculator", "Calculator.java", 140, "double", null, methodName, "()D");

        // Act
        String result1 = frameInfo.getMethodName();
        String result2 = frameInfo.getMethodName();

        // Assert
        assertEquals(methodName, result1);
        assertEquals(methodName, result2);
        assertSame(result1, result2); // Should return the same reference
    }

    @Test
    public void testGetMethodNameImmutability() {
        // Arrange
        String originalMethodName = "process";
        FrameInfo frameInfo = new FrameInfo("Processor", "Processor.java", 150, "void", null, originalMethodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals(originalMethodName, result);
        assertSame(originalMethodName, result);
    }

    @Test
    public void testGetMethodNameWithWhitespace() {
        // Arrange
        String methodName = "  method  ";
        FrameInfo frameInfo = new FrameInfo("WhitespaceClass", "WhitespaceClass.java", 160, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("  method  ", result);
    }

    @Test
    public void testGetMethodNameWithUnderscores() {
        // Arrange
        String methodName = "process_data";
        FrameInfo frameInfo = new FrameInfo("DataProcessor", "DataProcessor.java", 170, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("process_data", result);
    }

    @Test
    public void testGetMethodNameWithNumbers() {
        // Arrange
        String methodName = "method123";
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 180, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("method123", result);
    }

    @Test
    public void testGetMethodNameWithDollarSign() {
        // Arrange
        String methodName = "lambda$main$0";
        FrameInfo frameInfo = new FrameInfo("LambdaClass", "LambdaClass.java", 190, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("lambda$main$0", result);
    }

    @Test
    public void testGetMethodNameWithLongName() {
        // Arrange
        String methodName = "veryLongMethodNameThatDescribesWhatThisMethodDoes";
        FrameInfo frameInfo = new FrameInfo("VerboseClass", "VerboseClass.java", 200, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("veryLongMethodNameThatDescribesWhatThisMethodDoes", result);
    }

    @Test
    public void testGetMethodNameWithToStringMethod() {
        // Arrange
        String methodName = "toString";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 210, "String", null, methodName, "()Ljava/lang/String;");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("toString", result);
    }

    @Test
    public void testGetMethodNameWithEqualsMethod() {
        // Arrange
        String methodName = "equals";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 220, "boolean", null, methodName, "(Ljava/lang/Object;)Z");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("equals", result);
    }

    @Test
    public void testGetMethodNameWithHashCodeMethod() {
        // Arrange
        String methodName = "hashCode";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 230, "int", null, methodName, "()I");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("hashCode", result);
    }

    @Test
    public void testGetMethodNameWithCloneMethod() {
        // Arrange
        String methodName = "clone";
        FrameInfo frameInfo = new FrameInfo("Cloneable", "Cloneable.java", 240, "Object", null, methodName, "()Ljava/lang/Object;");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("clone", result);
    }

    @Test
    public void testGetMethodNameWithFinalizeMethod() {
        // Arrange
        String methodName = "finalize";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 250, "void", null, methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("finalize", result);
    }

    @Test
    public void testGetMethodNameWithBridgeMethod() {
        // Arrange
        String methodName = "compare";
        FrameInfo frameInfo = new FrameInfo("Comparator", "Comparator.java", 260, "int", null, methodName, "(Ljava/lang/Object;Ljava/lang/Object;)I");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("compare", result);
    }

    @Test
    public void testGetMethodNameWithCallMethod() {
        // Arrange
        String methodName = "call";
        FrameInfo frameInfo = new FrameInfo("Callable", "Callable.java", 270, "Object", null, methodName, "()Ljava/lang/Object;");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("call", result);
    }

    @Test
    public void testGetMethodNameWithApplyMethod() {
        // Arrange
        String methodName = "apply";
        FrameInfo frameInfo = new FrameInfo("Function", "Function.java", 280, "Object", null, methodName, "(Ljava/lang/Object;)Ljava/lang/Object;");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("apply", result);
    }

    @Test
    public void testGetMethodNameWithAcceptMethod() {
        // Arrange
        String methodName = "accept";
        FrameInfo frameInfo = new FrameInfo("Consumer", "Consumer.java", 290, "void", null, methodName, "(Ljava/lang/Object;)V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("accept", result);
    }

    @Test
    public void testGetMethodNameConsistency() {
        // Arrange
        String methodName = "testMethod";
        FrameInfo frameInfo = new FrameInfo("ConsistentClass", "ConsistentClass.java", 300, "void", null, methodName, "()V");

        // Act
        String result1 = frameInfo.getMethodName();
        String result2 = frameInfo.getMethodName();
        String result3 = frameInfo.getMethodName();

        // Assert
        assertEquals(methodName, result1);
        assertEquals(methodName, result2);
        assertEquals(methodName, result3);
        assertSame(result1, result2);
        assertSame(result2, result3);
    }

    @Test
    public void testGetMethodNameWhenFieldNameIsSet() {
        // Arrange
        String methodName = "increment";
        FrameInfo frameInfo = new FrameInfo("BothSetClass", "BothSetClass.java", 310, "void", "counter", methodName, "()V");

        // Act
        String result = frameInfo.getMethodName();

        // Assert
        assertEquals("increment", result);
    }
}
