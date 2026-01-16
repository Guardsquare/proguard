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
 * Test class for FrameInfo.getClassName() method.
 */
public class FrameInfoClaude_getClassNameTest {

    @Test
    public void testGetClassNameReturnsCorrectValue() {
        // Arrange
        String className = "com.example.MyClass";
        FrameInfo frameInfo = new FrameInfo(className, "MyClass.java", 10, "void", null, "method", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals(className, result);
    }

    @Test
    public void testGetClassNameWithNullValue() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo(null, "Test.java", 5, "int", "field", "method", "()I");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetClassNameWithEmptyString() {
        // Arrange
        String className = "";
        FrameInfo frameInfo = new FrameInfo(className, "File.java", 1, "String", null, "test", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetClassNameWithSimpleClassName() {
        // Arrange
        String className = "SimpleClass";
        FrameInfo frameInfo = new FrameInfo(className, "SimpleClass.java", 20, "boolean", null, "isValid", "()Z");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("SimpleClass", result);
    }

    @Test
    public void testGetClassNameWithPackageName() {
        // Arrange
        String className = "com.example.package.ClassName";
        FrameInfo frameInfo = new FrameInfo(className, "ClassName.java", 50, "void", null, "execute", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("com.example.package.ClassName", result);
    }

    @Test
    public void testGetClassNameWithInnerClass() {
        // Arrange
        String className = "com.example.Outer$Inner";
        FrameInfo frameInfo = new FrameInfo(className, "Outer.java", 100, "int", "value", null, null);

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("com.example.Outer$Inner", result);
    }

    @Test
    public void testGetClassNameWithObfuscatedName() {
        // Arrange
        String className = "a.b.c";
        FrameInfo frameInfo = new FrameInfo(className, "SourceFile", 1, "void", null, "a", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("a.b.c", result);
    }

    @Test
    public void testGetClassNameWithSlashSeparator() {
        // Arrange
        String className = "com/example/MyClass";
        FrameInfo frameInfo = new FrameInfo(className, "MyClass.java", 30, "String", null, "getName", "()Ljava/lang/String;");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("com/example/MyClass", result);
    }

    @Test
    public void testGetClassNameWithSpecialCharacters() {
        // Arrange
        String className = "com.example.Class$1$2";
        FrameInfo frameInfo = new FrameInfo(className, "Class.java", 75, "void", null, "run", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("com.example.Class$1$2", result);
    }

    @Test
    public void testGetClassNameMultipleCalls() {
        // Arrange
        String className = "com.test.TestClass";
        FrameInfo frameInfo = new FrameInfo(className, "TestClass.java", 15, "long", null, "getValue", "()J");

        // Act
        String result1 = frameInfo.getClassName();
        String result2 = frameInfo.getClassName();

        // Assert
        assertEquals(className, result1);
        assertEquals(className, result2);
        assertSame(result1, result2); // Should return the same reference
    }

    @Test
    public void testGetClassNameImmutability() {
        // Arrange
        String originalClassName = "com.immutable.TestClass";
        FrameInfo frameInfo = new FrameInfo(originalClassName, "Test.java", 25, "void", null, "method", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals(originalClassName, result);
        // Verify that the returned value is the same as the original (immutability)
        assertSame(originalClassName, result);
    }

    @Test
    public void testGetClassNameWithWhitespace() {
        // Arrange
        String className = "  com.example.ClassWithSpaces  ";
        FrameInfo frameInfo = new FrameInfo(className, "File.java", 10, "int", null, "test", "()I");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("  com.example.ClassWithSpaces  ", result);
    }

    @Test
    public void testGetClassNameWithLongName() {
        // Arrange
        String className = "com.very.long.package.name.with.many.parts.MyVeryLongClassName";
        FrameInfo frameInfo = new FrameInfo(className, "MyVeryLongClassName.java", 200, "void", null, "longMethod", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals(className, result);
    }

    @Test
    public void testGetClassNameWithAnonymousClass() {
        // Arrange
        String className = "com.example.Outer$1";
        FrameInfo frameInfo = new FrameInfo(className, "Outer.java", 45, "void", null, "run", "()V");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("com.example.Outer$1", result);
    }

    @Test
    public void testGetClassNameWithMultipleInnerClasses() {
        // Arrange
        String className = "com.example.Outer$Inner$DeepInner";
        FrameInfo frameInfo = new FrameInfo(className, "Outer.java", 60, "String", null, "process", "()Ljava/lang/String;");

        // Act
        String result = frameInfo.getClassName();

        // Assert
        assertEquals("com.example.Outer$Inner$DeepInner", result);
    }
}
