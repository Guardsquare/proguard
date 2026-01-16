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
 * Test class for FrameInfo.getLineNumber() method.
 */
public class FrameInfoClaude_getLineNumberTest {

    @Test
    public void testGetLineNumberReturnsCorrectValue() {
        // Arrange
        int lineNumber = 42;
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", "MyClass.java", lineNumber, "void", null, "method", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(lineNumber, result);
    }

    @Test
    public void testGetLineNumberWithZero() {
        // Arrange
        int lineNumber = 0;
        FrameInfo frameInfo = new FrameInfo("com.example.Test", "Test.java", lineNumber, "int", "field", "method", "()I");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testGetLineNumberWithOne() {
        // Arrange
        int lineNumber = 1;
        FrameInfo frameInfo = new FrameInfo("TestClass", "Test.java", lineNumber, "String", null, "test", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testGetLineNumberWithSmallValue() {
        // Arrange
        int lineNumber = 5;
        FrameInfo frameInfo = new FrameInfo("SimpleClass", "SimpleClass.java", lineNumber, "boolean", null, "isValid", "()Z");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(5, result);
    }

    @Test
    public void testGetLineNumberWithTypicalValue() {
        // Arrange
        int lineNumber = 150;
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", lineNumber, "void", null, "execute", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(150, result);
    }

    @Test
    public void testGetLineNumberWithLargeValue() {
        // Arrange
        int lineNumber = 10000;
        FrameInfo frameInfo = new FrameInfo("LargeFile", "LargeFile.java", lineNumber, "int", "value", null, null);

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(10000, result);
    }

    @Test
    public void testGetLineNumberWithMaxIntValue() {
        // Arrange
        int lineNumber = Integer.MAX_VALUE;
        FrameInfo frameInfo = new FrameInfo("MaxClass", "MaxClass.java", lineNumber, "String", null, "getName", "()Ljava/lang/String;");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testGetLineNumberWithNegativeValue() {
        // Arrange
        int lineNumber = -1;
        FrameInfo frameInfo = new FrameInfo("NegativeTest", "Test.java", lineNumber, "void", null, "method", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void testGetLineNumberWithMinIntValue() {
        // Arrange
        int lineNumber = Integer.MIN_VALUE;
        FrameInfo frameInfo = new FrameInfo("MinClass", "MinClass.java", lineNumber, "void", null, "run", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(Integer.MIN_VALUE, result);
    }

    @Test
    public void testGetLineNumberMultipleCalls() {
        // Arrange
        int lineNumber = 99;
        FrameInfo frameInfo = new FrameInfo("com.test.TestClass", "TestClass.java", lineNumber, "long", null, "getValue", "()J");

        // Act
        int result1 = frameInfo.getLineNumber();
        int result2 = frameInfo.getLineNumber();

        // Assert
        assertEquals(lineNumber, result1);
        assertEquals(lineNumber, result2);
        assertEquals(result1, result2);
    }

    @Test
    public void testGetLineNumberWithHundred() {
        // Arrange
        int lineNumber = 100;
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", "MyClass.java", lineNumber, "void", null, "method", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(100, result);
    }

    @Test
    public void testGetLineNumberWithThousand() {
        // Arrange
        int lineNumber = 1000;
        FrameInfo frameInfo = new FrameInfo("BigFile", "BigFile.java", lineNumber, "int", null, "test", "()I");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(1000, result);
    }

    @Test
    public void testGetLineNumberForUnknownLine() {
        // Arrange
        // -1 is often used to indicate unknown line number
        int lineNumber = -1;
        FrameInfo frameInfo = new FrameInfo("UnknownClass", "SourceFile", lineNumber, "void", null, "unknownMethod", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void testGetLineNumberWithTen() {
        // Arrange
        int lineNumber = 10;
        FrameInfo frameInfo = new FrameInfo("Example", "Example.java", lineNumber, "double", null, "calculate", "()D");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(10, result);
    }

    @Test
    public void testGetLineNumberWithFifty() {
        // Arrange
        int lineNumber = 50;
        FrameInfo frameInfo = new FrameInfo("MediumClass", "MediumClass.java", lineNumber, "boolean", "flag", null, null);

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(50, result);
    }

    @Test
    public void testGetLineNumberWithTwoHundred() {
        // Arrange
        int lineNumber = 200;
        FrameInfo frameInfo = new FrameInfo("LongerClass", "LongerClass.java", lineNumber, "String", null, "process", "()Ljava/lang/String;");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(200, result);
    }

    @Test
    public void testGetLineNumberWithFiveHundred() {
        // Arrange
        int lineNumber = 500;
        FrameInfo frameInfo = new FrameInfo("BigClass", "BigClass.java", lineNumber, "void", null, "execute", "()V");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(500, result);
    }

    @Test
    public void testGetLineNumberWithOddNumber() {
        // Arrange
        int lineNumber = 73;
        FrameInfo frameInfo = new FrameInfo("OddClass", "OddClass.java", lineNumber, "int", null, "getValue", "()I");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(73, result);
    }

    @Test
    public void testGetLineNumberWithEvenNumber() {
        // Arrange
        int lineNumber = 88;
        FrameInfo frameInfo = new FrameInfo("EvenClass", "EvenClass.java", lineNumber, "long", null, "getCount", "()J");

        // Act
        int result = frameInfo.getLineNumber();

        // Assert
        assertEquals(88, result);
    }

    @Test
    public void testGetLineNumberConsistency() {
        // Arrange
        int lineNumber = 42;
        FrameInfo frameInfo = new FrameInfo("ConsistentClass", "ConsistentClass.java", lineNumber, "void", null, "test", "()V");

        // Act - Call multiple times
        int result1 = frameInfo.getLineNumber();
        int result2 = frameInfo.getLineNumber();
        int result3 = frameInfo.getLineNumber();

        // Assert - All calls should return the same value
        assertEquals(lineNumber, result1);
        assertEquals(lineNumber, result2);
        assertEquals(lineNumber, result3);
        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }
}
