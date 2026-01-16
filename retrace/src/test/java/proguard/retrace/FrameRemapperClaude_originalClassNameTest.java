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
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FrameRemapper.originalClassName() method.
 */
public class FrameRemapperClaude_originalClassNameTest {

    private FrameRemapper frameRemapper;

    @BeforeEach
    public void setUp() {
        frameRemapper = new FrameRemapper();
    }

    @Test
    public void testOriginalClassNameReturnsOriginalWhenMappingExists() {
        // Arrange
        frameRemapper.processClassMapping("com.example.OriginalClass", "a.b.c");

        // Act
        String result = frameRemapper.originalClassName("a.b.c");

        // Assert
        assertEquals("com.example.OriginalClass", result);
    }

    @Test
    public void testOriginalClassNameReturnsInputWhenNoMappingExists() {
        // Arrange - no mapping added

        // Act
        String result = frameRemapper.originalClassName("com.example.UnmappedClass");

        // Assert
        assertEquals("com.example.UnmappedClass", result);
    }

    @Test
    public void testOriginalClassNameWithSimpleObfuscatedName() {
        // Arrange
        frameRemapper.processClassMapping("com.example.MyClass", "a");

        // Act
        String result = frameRemapper.originalClassName("a");

        // Assert
        assertEquals("com.example.MyClass", result);
    }

    @Test
    public void testOriginalClassNameWithFullyQualifiedOriginalName() {
        // Arrange
        frameRemapper.processClassMapping("com.example.package.subpackage.ClassName", "a.b.c.d");

        // Act
        String result = frameRemapper.originalClassName("a.b.c.d");

        // Assert
        assertEquals("com.example.package.subpackage.ClassName", result);
    }

    @Test
    public void testOriginalClassNameWithInnerClass() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Outer$Inner", "a$b");

        // Act
        String result = frameRemapper.originalClassName("a$b");

        // Assert
        assertEquals("com.example.Outer$Inner", result);
    }

    @Test
    public void testOriginalClassNameWithAnonymousInnerClass() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Outer$1", "a$1");

        // Act
        String result = frameRemapper.originalClassName("a$1");

        // Assert
        assertEquals("com.example.Outer$1", result);
    }

    @Test
    public void testOriginalClassNameWithMultipleInnerClasses() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Outer$Inner$DeepInner", "a$b$c");

        // Act
        String result = frameRemapper.originalClassName("a$b$c");

        // Assert
        assertEquals("com.example.Outer$Inner$DeepInner", result);
    }

    @Test
    public void testOriginalClassNameWithNullInput() {
        // Act
        String result = frameRemapper.originalClassName(null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testOriginalClassNameWithEmptyString() {
        // Act
        String result = frameRemapper.originalClassName("");

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testOriginalClassNameWithMultipleMappings() {
        // Arrange
        frameRemapper.processClassMapping("com.example.ClassOne", "a.b.c");
        frameRemapper.processClassMapping("com.example.ClassTwo", "d.e.f");
        frameRemapper.processClassMapping("com.example.ClassThree", "g.h.i");

        // Act & Assert
        assertEquals("com.example.ClassOne", frameRemapper.originalClassName("a.b.c"));
        assertEquals("com.example.ClassTwo", frameRemapper.originalClassName("d.e.f"));
        assertEquals("com.example.ClassThree", frameRemapper.originalClassName("g.h.i"));
    }

    @Test
    public void testOriginalClassNameAfterMultipleCallsReturnsSameResult() {
        // Arrange
        frameRemapper.processClassMapping("com.example.OriginalClass", "a.b.c");

        // Act
        String result1 = frameRemapper.originalClassName("a.b.c");
        String result2 = frameRemapper.originalClassName("a.b.c");
        String result3 = frameRemapper.originalClassName("a.b.c");

        // Assert
        assertEquals("com.example.OriginalClass", result1);
        assertEquals("com.example.OriginalClass", result2);
        assertEquals("com.example.OriginalClass", result3);
    }

    @Test
    public void testOriginalClassNameWithSlashSeparators() {
        // Arrange
        frameRemapper.processClassMapping("com/example/OriginalClass", "a/b/c");

        // Act
        String result = frameRemapper.originalClassName("a/b/c");

        // Assert
        assertEquals("com/example/OriginalClass", result);
    }

    @Test
    public void testOriginalClassNameWithSameOriginalAndObfuscatedName() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Class", "com.example.Class");

        // Act
        String result = frameRemapper.originalClassName("com.example.Class");

        // Assert
        assertEquals("com.example.Class", result);
    }

    @Test
    public void testOriginalClassNameLookupNotFoundReturnsInput() {
        // Arrange
        frameRemapper.processClassMapping("com.example.MappedClass", "a.b.c");

        // Act - looking up a different class
        String result = frameRemapper.originalClassName("x.y.z");

        // Assert
        assertEquals("x.y.z", result);
    }

    @Test
    public void testOriginalClassNameWithComplexInnerClassHierarchy() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Outer$Inner$1$Local", "a$b$1$c");

        // Act
        String result = frameRemapper.originalClassName("a$b$1$c");

        // Assert
        assertEquals("com.example.Outer$Inner$1$Local", result);
    }

    @Test
    public void testOriginalClassNameWithSingleCharacterNames() {
        // Arrange
        frameRemapper.processClassMapping("OriginalClass", "a");

        // Act
        String result = frameRemapper.originalClassName("a");

        // Assert
        assertEquals("OriginalClass", result);
    }

    @Test
    public void testOriginalClassNameWithVeryLongNames() {
        // Arrange
        String longOriginalName = "com.very.long.package.name.with.many.parts.and.more.parts.OriginalClassName";
        String longObfuscatedName = "a.b.c.d.e.f.g.h.i.j.k";
        frameRemapper.processClassMapping(longOriginalName, longObfuscatedName);

        // Act
        String result = frameRemapper.originalClassName(longObfuscatedName);

        // Assert
        assertEquals(longOriginalName, result);
    }

    @Test
    public void testOriginalClassNameWithSpecialCharactersInInnerClass() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Class$1$2$3", "a$1$2$3");

        // Act
        String result = frameRemapper.originalClassName("a$1$2$3");

        // Assert
        assertEquals("com.example.Class$1$2$3", result);
    }

    @Test
    public void testOriginalClassNamePreservesUnmappedClassesAcrossMappings() {
        // Arrange
        frameRemapper.processClassMapping("com.example.MappedClass", "a.b.c");

        // Act - query unmapped class before and after adding mapping
        String result1 = frameRemapper.originalClassName("com.unmapped.Class");
        frameRemapper.processClassMapping("com.example.AnotherMapped", "d.e.f");
        String result2 = frameRemapper.originalClassName("com.unmapped.Class");

        // Assert
        assertEquals("com.unmapped.Class", result1);
        assertEquals("com.unmapped.Class", result2);
    }

    @Test
    public void testOriginalClassNameWithWhitespaceInInput() {
        // Act - whitespace in class names is unusual but the method should handle it
        String result = frameRemapper.originalClassName("  a.b.c  ");

        // Assert
        assertEquals("  a.b.c  ", result);
    }

    @Test
    public void testOriginalClassNameWithMixedDotAndDollarSeparators() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Outer$Inner.Nested", "a.b$c.d");

        // Act
        String result = frameRemapper.originalClassName("a.b$c.d");

        // Assert
        assertEquals("com.example.Outer$Inner.Nested", result);
    }

    @Test
    public void testOriginalClassNameIndependentOfMappingOrder() {
        // Arrange - add mappings in different order
        frameRemapper.processClassMapping("com.example.First", "a");
        frameRemapper.processClassMapping("com.example.Second", "b");
        frameRemapper.processClassMapping("com.example.Third", "c");

        // Act - query in different order
        String result3 = frameRemapper.originalClassName("c");
        String result1 = frameRemapper.originalClassName("a");
        String result2 = frameRemapper.originalClassName("b");

        // Assert
        assertEquals("com.example.First", result1);
        assertEquals("com.example.Second", result2);
        assertEquals("com.example.Third", result3);
    }

    @Test
    public void testOriginalClassNameWithPrimitiveTypeName() {
        // Act - testing with primitive-like names
        String result = frameRemapper.originalClassName("int");

        // Assert
        assertEquals("int", result);
    }

    @Test
    public void testOriginalClassNameWithArrayNotation() {
        // Act - array notation in class name (unusual but test edge case)
        String result = frameRemapper.originalClassName("a[]");

        // Assert
        assertEquals("a[]", result);
    }

    @Test
    public void testOriginalClassNameCaseSensitivity() {
        // Arrange - class names are case sensitive in Java
        frameRemapper.processClassMapping("com.example.MyClass", "abc");

        // Act
        String resultLower = frameRemapper.originalClassName("abc");
        String resultUpper = frameRemapper.originalClassName("ABC");
        String resultMixed = frameRemapper.originalClassName("Abc");

        // Assert
        assertEquals("com.example.MyClass", resultLower);
        assertEquals("ABC", resultUpper); // Not mapped, returns input
        assertEquals("Abc", resultMixed); // Not mapped, returns input
    }

    @Test
    public void testOriginalClassNameWithNumericObfuscatedName() {
        // Arrange
        frameRemapper.processClassMapping("com.example.OriginalClass", "a1b2c3");

        // Act
        String result = frameRemapper.originalClassName("a1b2c3");

        // Assert
        assertEquals("com.example.OriginalClass", result);
    }

    @Test
    public void testOriginalClassNameWithUnderscoresInNames() {
        // Arrange
        frameRemapper.processClassMapping("com.example.Original_Class", "a_b_c");

        // Act
        String result = frameRemapper.originalClassName("a_b_c");

        // Assert
        assertEquals("com.example.Original_Class", result);
    }

    @Test
    public void testOriginalClassNameOverwriteExistingMapping() {
        // Arrange - process same obfuscated name twice (last one wins)
        frameRemapper.processClassMapping("com.example.FirstOriginal", "a.b.c");
        frameRemapper.processClassMapping("com.example.SecondOriginal", "a.b.c");

        // Act
        String result = frameRemapper.originalClassName("a.b.c");

        // Assert
        // The second mapping should overwrite the first
        assertEquals("com.example.SecondOriginal", result);
    }

    @Test
    public void testOriginalClassNameWithJavaLangClasses() {
        // Arrange
        frameRemapper.processClassMapping("java.lang.String", "a");

        // Act
        String result = frameRemapper.originalClassName("a");

        // Assert
        assertEquals("java.lang.String", result);
    }
}
