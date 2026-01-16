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
 * Test class for FrameInfo constructor.
 */
public class FrameInfoClaude_constructorTest {

    @Test
    public void testConstructorWithAllParameters() {
        // Arrange
        String className = "com.example.MyClass";
        String sourceFile = "MyClass.java";
        int lineNumber = 42;
        String type = "void";
        String fieldName = "myField";
        String methodName = "myMethod";
        String arguments = "(Ljava/lang/String;I)V";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithNullClassName() {
        // Arrange
        String className = null;
        String sourceFile = "Test.java";
        int lineNumber = 10;
        String type = "int";
        String fieldName = "field";
        String methodName = "method";
        String arguments = "()I";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertNull(frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithNullSourceFile() {
        // Arrange
        String className = "com.example.Test";
        String sourceFile = null;
        int lineNumber = 20;
        String type = "String";
        String fieldName = "name";
        String methodName = "getName";
        String arguments = "()Ljava/lang/String;";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertNull(frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithNullType() {
        // Arrange
        String className = "MyClass";
        String sourceFile = "MyClass.java";
        int lineNumber = 5;
        String type = null;
        String fieldName = "value";
        String methodName = "getValue";
        String arguments = "()V";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertNull(frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithNullFieldName() {
        // Arrange
        String className = "TestClass";
        String sourceFile = "TestClass.java";
        int lineNumber = 100;
        String type = "boolean";
        String fieldName = null;
        String methodName = "testMethod";
        String arguments = "(Z)V";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertNull(frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithNullMethodName() {
        // Arrange
        String className = "Example";
        String sourceFile = "Example.java";
        int lineNumber = 50;
        String type = "long";
        String fieldName = "count";
        String methodName = null;
        String arguments = "(J)V";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertNull(frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithNullArguments() {
        // Arrange
        String className = "Demo";
        String sourceFile = "Demo.java";
        int lineNumber = 75;
        String type = "double";
        String fieldName = "rate";
        String methodName = "calculate";
        String arguments = null;

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertNull(frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithAllNullValues() {
        // Arrange
        String className = null;
        String sourceFile = null;
        int lineNumber = 0;
        String type = null;
        String fieldName = null;
        String methodName = null;
        String arguments = null;

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertNull(frameInfo.getClassName());
        assertNull(frameInfo.getSourceFile());
        assertEquals(0, frameInfo.getLineNumber());
        assertNull(frameInfo.getType());
        assertNull(frameInfo.getFieldName());
        assertNull(frameInfo.getMethodName());
        assertNull(frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithNegativeLineNumber() {
        // Arrange
        String className = "NegativeTest";
        String sourceFile = "NegativeTest.java";
        int lineNumber = -1;
        String type = "void";
        String fieldName = "field";
        String methodName = "method";
        String arguments = "()V";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(-1, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithEmptyStrings() {
        // Arrange
        String className = "";
        String sourceFile = "";
        int lineNumber = 1;
        String type = "";
        String fieldName = "";
        String methodName = "";
        String arguments = "";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals("", frameInfo.getClassName());
        assertEquals("", frameInfo.getSourceFile());
        assertEquals(1, frameInfo.getLineNumber());
        assertEquals("", frameInfo.getType());
        assertEquals("", frameInfo.getFieldName());
        assertEquals("", frameInfo.getMethodName());
        assertEquals("", frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithSpecialCharacters() {
        // Arrange
        String className = "com/example/Outer$Inner";
        String sourceFile = "Outer.java";
        int lineNumber = 123;
        String type = "[Ljava/lang/String;";
        String fieldName = "array$field";
        String methodName = "access$000";
        String arguments = "([Ljava/lang/Object;)V";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithLargeLineNumber() {
        // Arrange
        String className = "LargeLineNumber";
        String sourceFile = "LargeLineNumber.java";
        int lineNumber = Integer.MAX_VALUE;
        String type = "int";
        String fieldName = "max";
        String methodName = "getMax";
        String arguments = "()I";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(Integer.MAX_VALUE, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertEquals(fieldName, frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }

    @Test
    public void testConstructorWithComplexMethodSignature() {
        // Arrange
        String className = "com.example.ComplexClass";
        String sourceFile = "ComplexClass.java";
        int lineNumber = 200;
        String type = "Map<String, List<Integer>>";
        String fieldName = null;
        String methodName = "processData";
        String arguments = "(Ljava/util/Map;Ljava/util/List;[I)Ljava/util/Map;";

        // Act
        FrameInfo frameInfo = new FrameInfo(className, sourceFile, lineNumber, type, fieldName, methodName, arguments);

        // Assert
        assertEquals(className, frameInfo.getClassName());
        assertEquals(sourceFile, frameInfo.getSourceFile());
        assertEquals(lineNumber, frameInfo.getLineNumber());
        assertEquals(type, frameInfo.getType());
        assertNull(frameInfo.getFieldName());
        assertEquals(methodName, frameInfo.getMethodName());
        assertEquals(arguments, frameInfo.getArguments());
    }
}
