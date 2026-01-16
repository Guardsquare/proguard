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
 * Test class for FrameInfo.getType() method.
 */
public class FrameInfoClaude_getTypeTest {

    @Test
    public void testGetTypeReturnsCorrectValue() {
        // Arrange
        String type = "void";
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", "MyClass.java", 10, type, null, "method", "()V");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals(type, result);
    }

    @Test
    public void testGetTypeWithNullValue() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com.example.Test", "Test.java", 5, null, "field", "method", "()I");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetTypeWithEmptyString() {
        // Arrange
        String type = "";
        FrameInfo frameInfo = new FrameInfo("TestClass", "Test.java", 1, type, null, "test", "()V");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetTypeWithIntType() {
        // Arrange
        String type = "int";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 20, type, "value", null, null);

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("int", result);
    }

    @Test
    public void testGetTypeWithBooleanType() {
        // Arrange
        String type = "boolean";
        FrameInfo frameInfo = new FrameInfo("Test", "Test.java", 30, type, null, "isValid", "()Z");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("boolean", result);
    }

    @Test
    public void testGetTypeWithLongType() {
        // Arrange
        String type = "long";
        FrameInfo frameInfo = new FrameInfo("Example", "Example.java", 40, type, "timestamp", null, null);

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("long", result);
    }

    @Test
    public void testGetTypeWithDoubleType() {
        // Arrange
        String type = "double";
        FrameInfo frameInfo = new FrameInfo("Calculator", "Calculator.java", 50, type, null, "calculate", "()D");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("double", result);
    }

    @Test
    public void testGetTypeWithFloatType() {
        // Arrange
        String type = "float";
        FrameInfo frameInfo = new FrameInfo("Graphics", "Graphics.java", 60, type, "x", null, null);

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("float", result);
    }

    @Test
    public void testGetTypeWithByteType() {
        // Arrange
        String type = "byte";
        FrameInfo frameInfo = new FrameInfo("ByteHandler", "ByteHandler.java", 70, type, null, "getByte", "()B");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("byte", result);
    }

    @Test
    public void testGetTypeWithCharType() {
        // Arrange
        String type = "char";
        FrameInfo frameInfo = new FrameInfo("CharHandler", "CharHandler.java", 80, type, "letter", null, null);

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("char", result);
    }

    @Test
    public void testGetTypeWithShortType() {
        // Arrange
        String type = "short";
        FrameInfo frameInfo = new FrameInfo("ShortHandler", "ShortHandler.java", 90, type, null, "getShort", "()S");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("short", result);
    }

    @Test
    public void testGetTypeWithStringType() {
        // Arrange
        String type = "String";
        FrameInfo frameInfo = new FrameInfo("StringHandler", "StringHandler.java", 100, type, null, "getName", "()Ljava/lang/String;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("String", result);
    }

    @Test
    public void testGetTypeWithFullyQualifiedClassName() {
        // Arrange
        String type = "java.lang.String";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 110, type, "name", null, null);

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testGetTypeWithCustomClassName() {
        // Arrange
        String type = "com.example.CustomType";
        FrameInfo frameInfo = new FrameInfo("Handler", "Handler.java", 120, type, null, "getCustom", "()Lcom/example/CustomType;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("com.example.CustomType", result);
    }

    @Test
    public void testGetTypeWithArrayType() {
        // Arrange
        String type = "int[]";
        FrameInfo frameInfo = new FrameInfo("ArrayHandler", "ArrayHandler.java", 130, type, "numbers", null, null);

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("int[]", result);
    }

    @Test
    public void testGetTypeWithMultiDimensionalArray() {
        // Arrange
        String type = "String[][]";
        FrameInfo frameInfo = new FrameInfo("Matrix", "Matrix.java", 140, type, null, "getMatrix", "()[[Ljava/lang/String;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("String[][]", result);
    }

    @Test
    public void testGetTypeWithBytecodeArrayType() {
        // Arrange
        String type = "[Ljava/lang/String;";
        FrameInfo frameInfo = new FrameInfo("BytecodeClass", "BytecodeClass.java", 150, type, null, "getArray", "()[Ljava/lang/String;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("[Ljava/lang/String;", result);
    }

    @Test
    public void testGetTypeWithGenericType() {
        // Arrange
        String type = "List<String>";
        FrameInfo frameInfo = new FrameInfo("GenericHandler", "GenericHandler.java", 160, type, null, "getList", "()Ljava/util/List;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("List<String>", result);
    }

    @Test
    public void testGetTypeWithComplexGenericType() {
        // Arrange
        String type = "Map<String, List<Integer>>";
        FrameInfo frameInfo = new FrameInfo("ComplexHandler", "ComplexHandler.java", 170, type, null, "getMap", "()Ljava/util/Map;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("Map<String, List<Integer>>", result);
    }

    @Test
    public void testGetTypeMultipleCalls() {
        // Arrange
        String type = "int";
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 180, type, "value", null, null);

        // Act
        String result1 = frameInfo.getType();
        String result2 = frameInfo.getType();

        // Assert
        assertEquals(type, result1);
        assertEquals(type, result2);
        assertSame(result1, result2); // Should return the same reference
    }

    @Test
    public void testGetTypeImmutability() {
        // Arrange
        String originalType = "String";
        FrameInfo frameInfo = new FrameInfo("ImmutableClass", "ImmutableClass.java", 190, originalType, null, "method", "()V");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals(originalType, result);
        assertSame(originalType, result);
    }

    @Test
    public void testGetTypeWithWhitespace() {
        // Arrange
        String type = "  String  ";
        FrameInfo frameInfo = new FrameInfo("WhitespaceClass", "WhitespaceClass.java", 200, type, null, "test", "()V");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("  String  ", result);
    }

    @Test
    public void testGetTypeWithVoidType() {
        // Arrange
        String type = "void";
        FrameInfo frameInfo = new FrameInfo("VoidClass", "VoidClass.java", 210, type, null, "execute", "()V");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("void", result);
    }

    @Test
    public void testGetTypeWithObjectType() {
        // Arrange
        String type = "Object";
        FrameInfo frameInfo = new FrameInfo("ObjectHandler", "ObjectHandler.java", 220, type, null, "getObject", "()Ljava/lang/Object;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("Object", result);
    }

    @Test
    public void testGetTypeWithWildcardGeneric() {
        // Arrange
        String type = "List<?>";
        FrameInfo frameInfo = new FrameInfo("WildcardClass", "WildcardClass.java", 230, type, null, "getList", "()Ljava/util/List;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("List<?>", result);
    }

    @Test
    public void testGetTypeWithBoundedGeneric() {
        // Arrange
        String type = "List<? extends Number>";
        FrameInfo frameInfo = new FrameInfo("BoundedClass", "BoundedClass.java", 240, type, null, "getNumbers", "()Ljava/util/List;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("List<? extends Number>", result);
    }

    @Test
    public void testGetTypeWithInnerClassType() {
        // Arrange
        String type = "Outer.Inner";
        FrameInfo frameInfo = new FrameInfo("TypeClass", "TypeClass.java", 250, type, null, "getInner", "()LOuter$Inner;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("Outer.Inner", result);
    }

    @Test
    public void testGetTypeWithObfuscatedType() {
        // Arrange
        String type = "a";
        FrameInfo frameInfo = new FrameInfo("ObfuscatedClass", "SourceFile", 260, type, null, "b", "()La;");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("a", result);
    }

    @Test
    public void testGetTypeWithLongTypeName() {
        // Arrange
        String type = "com.example.very.long.package.name.with.many.parts.VeryLongClassName";
        FrameInfo frameInfo = new FrameInfo("LongClass", "LongClass.java", 270, type, null, "method", "()V");

        // Act
        String result = frameInfo.getType();

        // Assert
        assertEquals("com.example.very.long.package.name.with.many.parts.VeryLongClassName", result);
    }

    @Test
    public void testGetTypeConsistency() {
        // Arrange
        String type = "double";
        FrameInfo frameInfo = new FrameInfo("ConsistentClass", "ConsistentClass.java", 280, type, null, "getValue", "()D");

        // Act
        String result1 = frameInfo.getType();
        String result2 = frameInfo.getType();
        String result3 = frameInfo.getType();

        // Assert
        assertEquals(type, result1);
        assertEquals(type, result2);
        assertEquals(type, result3);
        assertSame(result1, result2);
        assertSame(result2, result3);
    }
}
