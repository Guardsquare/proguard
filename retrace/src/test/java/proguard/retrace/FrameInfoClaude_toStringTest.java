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
 * Test class for FrameInfo.toString() method.
 */
public class FrameInfoClaude_toStringTest {

    @Test
    public void testToStringWithAllFieldsPopulated() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", "MyClass.java", 42, "void", "myField", "myMethod", "(I)V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("proguard.retrace.FrameInfo"));
        assertTrue(result.contains("class=[com.example.MyClass]"));
        assertTrue(result.contains("line=[42]"));
        assertTrue(result.contains("type=[void]"));
        assertTrue(result.contains("field=[myField]"));
        assertTrue(result.contains("method=[myMethod]"));
        assertTrue(result.contains("arguments=[(I)V]"));
    }

    @Test
    public void testToStringWithAllNullFields() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo(null, null, 0, null, null, null, null);

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("proguard.retrace.FrameInfo"));
        assertTrue(result.contains("class=[null]"));
        assertTrue(result.contains("line=[0]"));
        assertTrue(result.contains("type=[null]"));
        assertTrue(result.contains("field=[null]"));
        assertTrue(result.contains("method=[null]"));
        assertTrue(result.contains("arguments=[null]"));
    }

    @Test
    public void testToStringWithNullClassName() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo(null, "Test.java", 10, "int", "value", "getValue", "()I");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[null]"));
        assertTrue(result.contains("line=[10]"));
        assertTrue(result.contains("type=[int]"));
        assertTrue(result.contains("field=[value]"));
        assertTrue(result.contains("method=[getValue]"));
        assertTrue(result.contains("arguments=[()I]"));
    }

    @Test
    public void testToStringWithNullType() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 20, null, "field", "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[MyClass]"));
        assertTrue(result.contains("line=[20]"));
        assertTrue(result.contains("type=[null]"));
        assertTrue(result.contains("field=[field]"));
        assertTrue(result.contains("method=[method]"));
    }

    @Test
    public void testToStringWithNullFieldName() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 30, "void", null, "testMethod", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[TestClass]"));
        assertTrue(result.contains("line=[30]"));
        assertTrue(result.contains("type=[void]"));
        assertTrue(result.contains("field=[null]"));
        assertTrue(result.contains("method=[testMethod]"));
        assertTrue(result.contains("arguments=[()V]"));
    }

    @Test
    public void testToStringWithNullMethodName() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("FieldClass", "FieldClass.java", 40, "String", "name", null, null);

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[FieldClass]"));
        assertTrue(result.contains("line=[40]"));
        assertTrue(result.contains("type=[String]"));
        assertTrue(result.contains("field=[name]"));
        assertTrue(result.contains("method=[null]"));
        assertTrue(result.contains("arguments=[null]"));
    }

    @Test
    public void testToStringWithNullArguments() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 50, "int", "count", "getCount", null);

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[MyClass]"));
        assertTrue(result.contains("line=[50]"));
        assertTrue(result.contains("type=[int]"));
        assertTrue(result.contains("field=[count]"));
        assertTrue(result.contains("method=[getCount]"));
        assertTrue(result.contains("arguments=[null]"));
    }

    @Test
    public void testToStringWithZeroLineNumber() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("ZeroLine", "ZeroLine.java", 0, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("line=[0]"));
    }

    @Test
    public void testToStringWithNegativeLineNumber() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("NegativeLine", "NegativeLine.java", -1, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("line=[-1]"));
    }

    @Test
    public void testToStringWithLargeLineNumber() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("LargeLine", "LargeLine.java", 99999, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("line=[99999]"));
    }

    @Test
    public void testToStringWithEmptyStringFields() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("", "", 1, "", "", "", "");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[]"));
        assertTrue(result.contains("line=[1]"));
        assertTrue(result.contains("type=[]"));
        assertTrue(result.contains("field=[]"));
        assertTrue(result.contains("method=[]"));
        assertTrue(result.contains("arguments=[]"));
    }

    @Test
    public void testToStringWithComplexClassName() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com.example.package.Outer$Inner", "Outer.java", 100, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[com.example.package.Outer$Inner]"));
    }

    @Test
    public void testToStringWithComplexMethodSignature() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("ComplexClass", "ComplexClass.java", 200, "Map", null, "processData", "(Ljava/lang/String;ILjava/util/List;)Ljava/util/Map;");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("method=[processData]"));
        assertTrue(result.contains("arguments=[(Ljava/lang/String;ILjava/util/List;)Ljava/util/Map;]"));
    }

    @Test
    public void testToStringMultipleCalls() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 50, "int", "value", "getValue", "()I");

        // Act
        String result1 = frameInfo.toString();
        String result2 = frameInfo.toString();

        // Assert
        assertEquals(result1, result2);
    }

    @Test
    public void testToStringStartsWithClassName() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 10, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.startsWith("proguard.retrace.FrameInfo"));
    }

    @Test
    public void testToStringContainsAllRequiredComponents() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 15, "String", "name", "getName", "()Ljava/lang/String;");

        // Act
        String result = frameInfo.toString();

        // Assert
        // Check that result contains all the expected components in order
        int classIndex = result.indexOf("class=[TestClass]");
        int lineIndex = result.indexOf("line=[15]");
        int typeIndex = result.indexOf("type=[String]");
        int fieldIndex = result.indexOf("field=[name]");
        int methodIndex = result.indexOf("method=[getName]");
        int argumentsIndex = result.indexOf("arguments=[()Ljava/lang/String;]");

        assertTrue(classIndex != -1, "Should contain class");
        assertTrue(lineIndex != -1, "Should contain line");
        assertTrue(typeIndex != -1, "Should contain type");
        assertTrue(fieldIndex != -1, "Should contain field");
        assertTrue(methodIndex != -1, "Should contain method");
        assertTrue(argumentsIndex != -1, "Should contain arguments");

        // Verify order
        assertTrue(classIndex < lineIndex);
        assertTrue(lineIndex < typeIndex);
        assertTrue(typeIndex < fieldIndex);
        assertTrue(fieldIndex < methodIndex);
        assertTrue(methodIndex < argumentsIndex);
    }

    @Test
    public void testToStringWithConstructor() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 5, "void", null, "<init>", "(Ljava/lang/String;)V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("method=[<init>]"));
        assertTrue(result.contains("arguments=[(Ljava/lang/String;)V]"));
    }

    @Test
    public void testToStringWithStaticInitializer() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("StaticClass", "StaticClass.java", 1, "void", null, "<clinit>", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("method=[<clinit>]"));
    }

    @Test
    public void testToStringWithObfuscatedNames() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("a.b.c", "SourceFile", 1, "d", "e", "f", "(La;)Lb;");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[a.b.c]"));
        assertTrue(result.contains("type=[d]"));
        assertTrue(result.contains("field=[e]"));
        assertTrue(result.contains("method=[f]"));
        assertTrue(result.contains("arguments=[(La;)Lb;]"));
    }

    @Test
    public void testToStringNotNull() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 10, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testToStringNotEmpty() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 10, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    public void testToStringWithSpecialCharactersInClassName() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com/example/MyClass$1$2", "MyClass.java", 25, "void", null, "run", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("class=[com/example/MyClass$1$2]"));
    }

    @Test
    public void testToStringWithPrimitiveType() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("IntegerHandler", "IntegerHandler.java", 30, "int", "count", null, null);

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("type=[int]"));
    }

    @Test
    public void testToStringWithArrayType() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("ArrayClass", "ArrayClass.java", 40, "int[]", "data", null, null);

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("type=[int[]]"));
    }

    @Test
    public void testToStringWithSyntheticField() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("Outer$Inner", "Outer.java", 50, "Outer", "this$0", null, null);

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("field=[this$0]"));
    }

    @Test
    public void testToStringWithLambdaMethod() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("LambdaClass", "LambdaClass.java", 60, "void", null, "lambda$main$0", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("method=[lambda$main$0]"));
    }

    @Test
    public void testToStringWithMaxIntLineNumber() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("MaxLine", "MaxLine.java", Integer.MAX_VALUE, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("line=[" + Integer.MAX_VALUE + "]"));
    }

    @Test
    public void testToStringWithMinIntLineNumber() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("MinLine", "MinLine.java", Integer.MIN_VALUE, "void", null, "method", "()V");

        // Act
        String result = frameInfo.toString();

        // Assert
        assertTrue(result.contains("line=[" + Integer.MIN_VALUE + "]"));
    }

    @Test
    public void testToStringConsistency() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("ConsistentClass", "ConsistentClass.java", 100, "String", "value", "getValue", "()Ljava/lang/String;");

        // Act
        String result1 = frameInfo.toString();
        String result2 = frameInfo.toString();
        String result3 = frameInfo.toString();

        // Assert
        assertEquals(result1, result2);
        assertEquals(result2, result3);
        assertEquals(result1, result3);
    }

    @Test
    public void testToStringFormat() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("FormatClass", "FormatClass.java", 123, "boolean", "flag", "isEnabled", "()Z");

        // Act
        String result = frameInfo.toString();

        // Assert
        // Verify the exact format expected
        String expectedPattern = "proguard.retrace.FrameInfo\\(class=\\[FormatClass\\], line=\\[123\\], type=\\[boolean\\], field=\\[flag\\], method=\\[isEnabled\\], arguments=\\[\\(\\)Z\\]";
        assertTrue(result.matches(expectedPattern));
    }
}
