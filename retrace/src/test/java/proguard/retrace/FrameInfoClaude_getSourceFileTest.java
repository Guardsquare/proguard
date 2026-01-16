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
 * Test class for FrameInfo.getSourceFile() method.
 */
public class FrameInfoClaude_getSourceFileTest {

    @Test
    public void testGetSourceFileReturnsCorrectValue() {
        // Arrange
        String sourceFile = "MyClass.java";
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", sourceFile, 10, "void", null, "method", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals(sourceFile, result);
    }

    @Test
    public void testGetSourceFileWithNullValue() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com.example.Test", null, 5, "int", "field", "method", "()I");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetSourceFileWithEmptyString() {
        // Arrange
        String sourceFile = "";
        FrameInfo frameInfo = new FrameInfo("TestClass", sourceFile, 1, "String", null, "test", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetSourceFileWithSimpleFileName() {
        // Arrange
        String sourceFile = "Test.java";
        FrameInfo frameInfo = new FrameInfo("Test", sourceFile, 20, "boolean", null, "isValid", "()Z");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("Test.java", result);
    }

    @Test
    public void testGetSourceFileWithPath() {
        // Arrange
        String sourceFile = "src/main/java/MyClass.java";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 50, "void", null, "execute", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("src/main/java/MyClass.java", result);
    }

    @Test
    public void testGetSourceFileWithAbsolutePath() {
        // Arrange
        String sourceFile = "/home/user/project/src/MyClass.java";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 100, "int", "value", null, null);

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("/home/user/project/src/MyClass.java", result);
    }

    @Test
    public void testGetSourceFileWithWindowsPath() {
        // Arrange
        String sourceFile = "C:\\Users\\Project\\src\\MyClass.java";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 30, "String", null, "getName", "()Ljava/lang/String;");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("C:\\Users\\Project\\src\\MyClass.java", result);
    }

    @Test
    public void testGetSourceFileWithObfuscatedName() {
        // Arrange
        String sourceFile = "SourceFile";
        FrameInfo frameInfo = new FrameInfo("a.b.c", sourceFile, 1, "void", null, "a", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("SourceFile", result);
    }

    @Test
    public void testGetSourceFileWithoutExtension() {
        // Arrange
        String sourceFile = "MyClass";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 75, "void", null, "run", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("MyClass", result);
    }

    @Test
    public void testGetSourceFileMultipleCalls() {
        // Arrange
        String sourceFile = "TestClass.java";
        FrameInfo frameInfo = new FrameInfo("com.test.TestClass", sourceFile, 15, "long", null, "getValue", "()J");

        // Act
        String result1 = frameInfo.getSourceFile();
        String result2 = frameInfo.getSourceFile();

        // Assert
        assertEquals(sourceFile, result1);
        assertEquals(sourceFile, result2);
        assertSame(result1, result2); // Should return the same reference
    }

    @Test
    public void testGetSourceFileImmutability() {
        // Arrange
        String originalSourceFile = "TestClass.java";
        FrameInfo frameInfo = new FrameInfo("com.immutable.TestClass", originalSourceFile, 25, "void", null, "method", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals(originalSourceFile, result);
        // Verify that the returned value is the same as the original (immutability)
        assertSame(originalSourceFile, result);
    }

    @Test
    public void testGetSourceFileWithWhitespace() {
        // Arrange
        String sourceFile = "  MyFile.java  ";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 10, "int", null, "test", "()I");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("  MyFile.java  ", result);
    }

    @Test
    public void testGetSourceFileWithSpecialCharacters() {
        // Arrange
        String sourceFile = "My-File_v1.0.java";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 200, "void", null, "method", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("My-File_v1.0.java", result);
    }

    @Test
    public void testGetSourceFileWithDifferentExtension() {
        // Arrange
        String sourceFile = "MyClass.kt";
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", sourceFile, 45, "void", null, "run", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("MyClass.kt", result);
    }

    @Test
    public void testGetSourceFileWithScalaExtension() {
        // Arrange
        String sourceFile = "MyClass.scala";
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", sourceFile, 60, "String", null, "process", "()Ljava/lang/String;");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("MyClass.scala", result);
    }

    @Test
    public void testGetSourceFileWithUnknownSource() {
        // Arrange
        String sourceFile = "Unknown Source";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 0, "void", null, "method", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("Unknown Source", result);
    }

    @Test
    public void testGetSourceFileWithRelativePath() {
        // Arrange
        String sourceFile = "../lib/MyClass.java";
        FrameInfo frameInfo = new FrameInfo("lib.MyClass", sourceFile, 88, "int", "count", null, null);

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("../lib/MyClass.java", result);
    }

    @Test
    public void testGetSourceFileWithJarReference() {
        // Arrange
        String sourceFile = "rt.jar";
        FrameInfo frameInfo = new FrameInfo("java.lang.String", sourceFile, 150, "String", null, "substring", "(II)Ljava/lang/String;");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("rt.jar", result);
    }

    @Test
    public void testGetSourceFileWithLongPath() {
        // Arrange
        String sourceFile = "src/main/java/com/example/very/long/package/path/with/many/directories/MyVeryLongFileName.java";
        FrameInfo frameInfo = new FrameInfo("com.example.very.long.package.path.with.many.directories.MyVeryLongFileName", sourceFile, 999, "void", null, "method", "()V");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals(sourceFile, result);
    }

    @Test
    public void testGetSourceFileWithDots() {
        // Arrange
        String sourceFile = "My.Source.File.java";
        FrameInfo frameInfo = new FrameInfo("MyClass", sourceFile, 42, "double", null, "calculate", "()D");

        // Act
        String result = frameInfo.getSourceFile();

        // Assert
        assertEquals("My.Source.File.java", result);
    }
}
