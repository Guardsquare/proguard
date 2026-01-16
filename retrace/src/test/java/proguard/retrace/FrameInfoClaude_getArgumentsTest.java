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
 * Test class for FrameInfo.getArguments() method.
 */
public class FrameInfoClaude_getArgumentsTest {

    @Test
    public void testGetArgumentsReturnsCorrectValue() {
        // Arrange
        String arguments = "(I)V";
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", "MyClass.java", 10, "void", null, "method", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals(arguments, result);
    }

    @Test
    public void testGetArgumentsWithNullValue() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com.example.Test", "Test.java", 5, "int", "field", null, null);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetArgumentsWithEmptyString() {
        // Arrange
        String arguments = "";
        FrameInfo frameInfo = new FrameInfo("TestClass", "Test.java", 1, "void", null, "test", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetArgumentsWithNoParameters() {
        // Arrange
        String arguments = "()V";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 20, "void", null, "execute", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("()V", result);
    }

    @Test
    public void testGetArgumentsWithSingleIntParameter() {
        // Arrange
        String arguments = "(I)V";
        FrameInfo frameInfo = new FrameInfo("Counter", "Counter.java", 30, "void", null, "increment", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(I)V", result);
    }

    @Test
    public void testGetArgumentsWithSingleStringParameter() {
        // Arrange
        String arguments = "(Ljava/lang/String;)V";
        FrameInfo frameInfo = new FrameInfo("Printer", "Printer.java", 40, "void", null, "print", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(Ljava/lang/String;)V", result);
    }

    @Test
    public void testGetArgumentsWithMultiplePrimitiveParameters() {
        // Arrange
        String arguments = "(IJZ)V";
        FrameInfo frameInfo = new FrameInfo("DataClass", "DataClass.java", 50, "void", null, "setData", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(IJZ)V", result);
    }

    @Test
    public void testGetArgumentsWithMultipleObjectParameters() {
        // Arrange
        String arguments = "(Ljava/lang/String;Ljava/lang/Integer;)V";
        FrameInfo frameInfo = new FrameInfo("Handler", "Handler.java", 60, "void", null, "handle", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(Ljava/lang/String;Ljava/lang/Integer;)V", result);
    }

    @Test
    public void testGetArgumentsWithArrayParameter() {
        // Arrange
        String arguments = "([I)V";
        FrameInfo frameInfo = new FrameInfo("ArrayProcessor", "ArrayProcessor.java", 70, "void", null, "process", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("([I)V", result);
    }

    @Test
    public void testGetArgumentsWithStringArrayParameter() {
        // Arrange
        String arguments = "([Ljava/lang/String;)V";
        FrameInfo frameInfo = new FrameInfo("MainClass", "MainClass.java", 80, "void", null, "main", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("([Ljava/lang/String;)V", result);
    }

    @Test
    public void testGetArgumentsWithMultiDimensionalArray() {
        // Arrange
        String arguments = "([[I)V";
        FrameInfo frameInfo = new FrameInfo("MatrixHandler", "MatrixHandler.java", 90, "void", null, "processMatrix", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("([[I)V", result);
    }

    @Test
    public void testGetArgumentsWithIntReturnType() {
        // Arrange
        String arguments = "(II)I";
        FrameInfo frameInfo = new FrameInfo("Calculator", "Calculator.java", 100, "int", null, "add", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(II)I", result);
    }

    @Test
    public void testGetArgumentsWithStringReturnType() {
        // Arrange
        String arguments = "()Ljava/lang/String;";
        FrameInfo frameInfo = new FrameInfo("Getter", "Getter.java", 110, "String", null, "getName", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("()Ljava/lang/String;", result);
    }

    @Test
    public void testGetArgumentsWithBooleanParameter() {
        // Arrange
        String arguments = "(Z)V";
        FrameInfo frameInfo = new FrameInfo("Flag", "Flag.java", 120, "void", null, "setFlag", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(Z)V", result);
    }

    @Test
    public void testGetArgumentsWithLongParameter() {
        // Arrange
        String arguments = "(J)V";
        FrameInfo frameInfo = new FrameInfo("Timer", "Timer.java", 130, "void", null, "setTime", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(J)V", result);
    }

    @Test
    public void testGetArgumentsWithDoubleParameter() {
        // Arrange
        String arguments = "(D)V";
        FrameInfo frameInfo = new FrameInfo("Calculator", "Calculator.java", 140, "void", null, "setValue", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(D)V", result);
    }

    @Test
    public void testGetArgumentsWithFloatParameter() {
        // Arrange
        String arguments = "(F)V";
        FrameInfo frameInfo = new FrameInfo("Graphics", "Graphics.java", 150, "void", null, "setX", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(F)V", result);
    }

    @Test
    public void testGetArgumentsWithByteParameter() {
        // Arrange
        String arguments = "(B)V";
        FrameInfo frameInfo = new FrameInfo("ByteHandler", "ByteHandler.java", 160, "void", null, "setByte", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(B)V", result);
    }

    @Test
    public void testGetArgumentsWithCharParameter() {
        // Arrange
        String arguments = "(C)V";
        FrameInfo frameInfo = new FrameInfo("CharHandler", "CharHandler.java", 170, "void", null, "setChar", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(C)V", result);
    }

    @Test
    public void testGetArgumentsWithShortParameter() {
        // Arrange
        String arguments = "(S)V";
        FrameInfo frameInfo = new FrameInfo("ShortHandler", "ShortHandler.java", 180, "void", null, "setShort", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(S)V", result);
    }

    @Test
    public void testGetArgumentsWithComplexSignature() {
        // Arrange
        String arguments = "(Ljava/lang/String;ILjava/util/List;[I)Ljava/util/Map;";
        FrameInfo frameInfo = new FrameInfo("ComplexClass", "ComplexClass.java", 190, "Map", null, "process", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(Ljava/lang/String;ILjava/util/List;[I)Ljava/util/Map;", result);
    }

    @Test
    public void testGetArgumentsMultipleCalls() {
        // Arrange
        String arguments = "(I)V";
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 200, "void", null, "method", arguments);

        // Act
        String result1 = frameInfo.getArguments();
        String result2 = frameInfo.getArguments();

        // Assert
        assertEquals(arguments, result1);
        assertEquals(arguments, result2);
        assertSame(result1, result2); // Should return the same reference
    }

    @Test
    public void testGetArgumentsImmutability() {
        // Arrange
        String originalArguments = "(Ljava/lang/String;)V";
        FrameInfo frameInfo = new FrameInfo("ImmutableClass", "ImmutableClass.java", 210, "void", null, "method", originalArguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals(originalArguments, result);
        assertSame(originalArguments, result);
    }

    @Test
    public void testGetArgumentsWithWhitespace() {
        // Arrange
        String arguments = "  (I)V  ";
        FrameInfo frameInfo = new FrameInfo("WhitespaceClass", "WhitespaceClass.java", 220, "void", null, "method", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("  (I)V  ", result);
    }

    @Test
    public void testGetArgumentsWithObjectReturnType() {
        // Arrange
        String arguments = "()Ljava/lang/Object;";
        FrameInfo frameInfo = new FrameInfo("ObjectGetter", "ObjectGetter.java", 230, "Object", null, "get", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("()Ljava/lang/Object;", result);
    }

    @Test
    public void testGetArgumentsWithCustomClassParameter() {
        // Arrange
        String arguments = "(Lcom/example/CustomClass;)V";
        FrameInfo frameInfo = new FrameInfo("Handler", "Handler.java", 240, "void", null, "handle", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(Lcom/example/CustomClass;)V", result);
    }

    @Test
    public void testGetArgumentsWithInnerClassParameter() {
        // Arrange
        String arguments = "(LOuter$Inner;)V";
        FrameInfo frameInfo = new FrameInfo("Processor", "Processor.java", 250, "void", null, "process", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(LOuter$Inner;)V", result);
    }

    @Test
    public void testGetArgumentsWithMixedParameters() {
        // Arrange
        String arguments = "(ILjava/lang/String;Z[DLjava/util/List;)Ljava/lang/Object;";
        FrameInfo frameInfo = new FrameInfo("MixedClass", "MixedClass.java", 260, "Object", null, "process", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(ILjava/lang/String;Z[DLjava/util/List;)Ljava/lang/Object;", result);
    }

    @Test
    public void testGetArgumentsWithConstructorSignature() {
        // Arrange
        String arguments = "(Ljava/lang/String;I)V";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 270, "void", null, "<init>", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(Ljava/lang/String;I)V", result);
    }

    @Test
    public void testGetArgumentsWithStaticInitializerSignature() {
        // Arrange
        String arguments = "()V";
        FrameInfo frameInfo = new FrameInfo("StaticClass", "StaticClass.java", 280, "void", null, "<clinit>", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("()V", result);
    }

    @Test
    public void testGetArgumentsWithVarArgs() {
        // Arrange
        String arguments = "([Ljava/lang/Object;)V";
        FrameInfo frameInfo = new FrameInfo("VarArgsClass", "VarArgsClass.java", 290, "void", null, "varArgs", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("([Ljava/lang/Object;)V", result);
    }

    @Test
    public void testGetArgumentsWithLongSignature() {
        // Arrange
        String arguments = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)Ljava/lang/Boolean;";
        FrameInfo frameInfo = new FrameInfo("LongClass", "LongClass.java", 300, "Boolean", null, "longMethod", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)Ljava/lang/Boolean;", result);
    }

    @Test
    public void testGetArgumentsConsistency() {
        // Arrange
        String arguments = "(I)V";
        FrameInfo frameInfo = new FrameInfo("ConsistentClass", "ConsistentClass.java", 310, "void", null, "method", arguments);

        // Act
        String result1 = frameInfo.getArguments();
        String result2 = frameInfo.getArguments();
        String result3 = frameInfo.getArguments();

        // Assert
        assertEquals(arguments, result1);
        assertEquals(arguments, result2);
        assertEquals(arguments, result3);
        assertSame(result1, result2);
        assertSame(result2, result3);
    }

    @Test
    public void testGetArgumentsWithObfuscatedSignature() {
        // Arrange
        String arguments = "(La;Lb;)Lc;";
        FrameInfo frameInfo = new FrameInfo("a.b.c", "SourceFile", 320, "c", null, "a", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(La;Lb;)Lc;", result);
    }

    @Test
    public void testGetArgumentsWithAllPrimitiveTypes() {
        // Arrange
        String arguments = "(BSCIJFDZ)V";
        FrameInfo frameInfo = new FrameInfo("PrimitiveClass", "PrimitiveClass.java", 330, "void", null, "allPrimitives", arguments);

        // Act
        String result = frameInfo.getArguments();

        // Assert
        assertEquals("(BSCIJFDZ)V", result);
    }
}
