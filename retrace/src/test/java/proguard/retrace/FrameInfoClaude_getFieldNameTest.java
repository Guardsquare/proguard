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
 * Test class for FrameInfo.getFieldName() method.
 */
public class FrameInfoClaude_getFieldNameTest {

    @Test
    public void testGetFieldNameReturnsCorrectValue() {
        // Arrange
        String fieldName = "myField";
        FrameInfo frameInfo = new FrameInfo("com.example.MyClass", "MyClass.java", 10, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals(fieldName, result);
    }

    @Test
    public void testGetFieldNameWithNullValue() {
        // Arrange
        FrameInfo frameInfo = new FrameInfo("com.example.Test", "Test.java", 5, "int", null, "method", "()I");

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetFieldNameWithEmptyString() {
        // Arrange
        String fieldName = "";
        FrameInfo frameInfo = new FrameInfo("TestClass", "Test.java", 1, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetFieldNameWithSimpleName() {
        // Arrange
        String fieldName = "count";
        FrameInfo frameInfo = new FrameInfo("Counter", "Counter.java", 20, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("count", result);
    }

    @Test
    public void testGetFieldNameWithCamelCase() {
        // Arrange
        String fieldName = "firstName";
        FrameInfo frameInfo = new FrameInfo("Person", "Person.java", 30, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("firstName", result);
    }

    @Test
    public void testGetFieldNameWithUnderscores() {
        // Arrange
        String fieldName = "user_name";
        FrameInfo frameInfo = new FrameInfo("User", "User.java", 40, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("user_name", result);
    }

    @Test
    public void testGetFieldNameWithUpperCase() {
        // Arrange
        String fieldName = "MAX_VALUE";
        FrameInfo frameInfo = new FrameInfo("Constants", "Constants.java", 50, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("MAX_VALUE", result);
    }

    @Test
    public void testGetFieldNameWithDollarSign() {
        // Arrange
        String fieldName = "this$0";
        FrameInfo frameInfo = new FrameInfo("Outer$Inner", "Outer.java", 60, "Outer", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("this$0", result);
    }

    @Test
    public void testGetFieldNameWithSyntheticField() {
        // Arrange
        String fieldName = "val$capture";
        FrameInfo frameInfo = new FrameInfo("MyClass$1", "MyClass.java", 70, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("val$capture", result);
    }

    @Test
    public void testGetFieldNameWithObfuscatedName() {
        // Arrange
        String fieldName = "a";
        FrameInfo frameInfo = new FrameInfo("a.b.c", "SourceFile", 80, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("a", result);
    }

    @Test
    public void testGetFieldNameWithSingleLetter() {
        // Arrange
        String fieldName = "x";
        FrameInfo frameInfo = new FrameInfo("Point", "Point.java", 90, "double", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("x", result);
    }

    @Test
    public void testGetFieldNameWithNumbers() {
        // Arrange
        String fieldName = "field123";
        FrameInfo frameInfo = new FrameInfo("TestClass", "TestClass.java", 100, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("field123", result);
    }

    @Test
    public void testGetFieldNameMultipleCalls() {
        // Arrange
        String fieldName = "value";
        FrameInfo frameInfo = new FrameInfo("ValueHolder", "ValueHolder.java", 110, "long", fieldName, null, null);

        // Act
        String result1 = frameInfo.getFieldName();
        String result2 = frameInfo.getFieldName();

        // Assert
        assertEquals(fieldName, result1);
        assertEquals(fieldName, result2);
        assertSame(result1, result2); // Should return the same reference
    }

    @Test
    public void testGetFieldNameImmutability() {
        // Arrange
        String originalFieldName = "data";
        FrameInfo frameInfo = new FrameInfo("DataHolder", "DataHolder.java", 120, "Object", originalFieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals(originalFieldName, result);
        assertSame(originalFieldName, result);
    }

    @Test
    public void testGetFieldNameWithWhitespace() {
        // Arrange
        String fieldName = "  field  ";
        FrameInfo frameInfo = new FrameInfo("WhitespaceClass", "WhitespaceClass.java", 130, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("  field  ", result);
    }

    @Test
    public void testGetFieldNameWithStaticField() {
        // Arrange
        String fieldName = "INSTANCE";
        FrameInfo frameInfo = new FrameInfo("Singleton", "Singleton.java", 140, "Singleton", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("INSTANCE", result);
    }

    @Test
    public void testGetFieldNameWithPrivateField() {
        // Arrange
        String fieldName = "privateField";
        FrameInfo frameInfo = new FrameInfo("MyClass", "MyClass.java", 150, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("privateField", result);
    }

    @Test
    public void testGetFieldNameWithLongName() {
        // Arrange
        String fieldName = "veryLongFieldNameThatDescribesWhatThisFieldIsUsedFor";
        FrameInfo frameInfo = new FrameInfo("VerboseClass", "VerboseClass.java", 160, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("veryLongFieldNameThatDescribesWhatThisFieldIsUsedFor", result);
    }

    @Test
    public void testGetFieldNameWithBooleanPrefix() {
        // Arrange
        String fieldName = "isEnabled";
        FrameInfo frameInfo = new FrameInfo("Config", "Config.java", 170, "boolean", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("isEnabled", result);
    }

    @Test
    public void testGetFieldNameWithHasPrefix() {
        // Arrange
        String fieldName = "hasValue";
        FrameInfo frameInfo = new FrameInfo("Container", "Container.java", 180, "boolean", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("hasValue", result);
    }

    @Test
    public void testGetFieldNameWithMultipleUnderscores() {
        // Arrange
        String fieldName = "field_name_with_underscores";
        FrameInfo frameInfo = new FrameInfo("NamingClass", "NamingClass.java", 190, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("field_name_with_underscores", result);
    }

    @Test
    public void testGetFieldNameWithLeadingUnderscore() {
        // Arrange
        String fieldName = "_privateField";
        FrameInfo frameInfo = new FrameInfo("Convention", "Convention.java", 200, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("_privateField", result);
    }

    @Test
    public void testGetFieldNameWithTrailingUnderscore() {
        // Arrange
        String fieldName = "field_";
        FrameInfo frameInfo = new FrameInfo("TrailingClass", "TrailingClass.java", 210, "String", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("field_", result);
    }

    @Test
    public void testGetFieldNameWithMultipleDollarSigns() {
        // Arrange
        String fieldName = "field$1$2";
        FrameInfo frameInfo = new FrameInfo("Complex$Class", "Complex.java", 220, "int", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("field$1$2", result);
    }

    @Test
    public void testGetFieldNameWithMixedCase() {
        // Arrange
        String fieldName = "MyMixedCaseField";
        FrameInfo frameInfo = new FrameInfo("MixedCase", "MixedCase.java", 230, "Object", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("MyMixedCaseField", result);
    }

    @Test
    public void testGetFieldNameWithSerialVersionUID() {
        // Arrange
        String fieldName = "serialVersionUID";
        FrameInfo frameInfo = new FrameInfo("Serializable", "Serializable.java", 240, "long", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("serialVersionUID", result);
    }

    @Test
    public void testGetFieldNameWithArraySuffix() {
        // Arrange
        String fieldName = "dataArray";
        FrameInfo frameInfo = new FrameInfo("ArrayClass", "ArrayClass.java", 250, "int[]", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("dataArray", result);
    }

    @Test
    public void testGetFieldNameWithCollectionSuffix() {
        // Arrange
        String fieldName = "itemList";
        FrameInfo frameInfo = new FrameInfo("CollectionClass", "CollectionClass.java", 260, "List", fieldName, null, null);

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("itemList", result);
    }

    @Test
    public void testGetFieldNameConsistency() {
        // Arrange
        String fieldName = "testField";
        FrameInfo frameInfo = new FrameInfo("ConsistentClass", "ConsistentClass.java", 270, "String", fieldName, null, null);

        // Act
        String result1 = frameInfo.getFieldName();
        String result2 = frameInfo.getFieldName();
        String result3 = frameInfo.getFieldName();

        // Assert
        assertEquals(fieldName, result1);
        assertEquals(fieldName, result2);
        assertEquals(fieldName, result3);
        assertSame(result1, result2);
        assertSame(result2, result3);
    }

    @Test
    public void testGetFieldNameWhenMethodNameIsSet() {
        // Arrange
        String fieldName = "counter";
        FrameInfo frameInfo = new FrameInfo("BothSetClass", "BothSetClass.java", 280, "int", fieldName, "increment", "()V");

        // Act
        String result = frameInfo.getFieldName();

        // Assert
        assertEquals("counter", result);
    }
}
