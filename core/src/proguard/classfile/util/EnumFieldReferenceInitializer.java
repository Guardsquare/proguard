/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This ElementValueVisitor initializes the field references of the
 * EnumConstantElementValue instances that it visits.
 *
 * @author Eric Lafortune
 */
public class EnumFieldReferenceInitializer
extends      SimplifiedVisitor
implements   ElementValueVisitor,
             InstructionVisitor,
             ConstantVisitor
{
    //*
    private static final boolean DEBUG = false;
    /*/
    private static       boolean DEBUG = System.getProperty("efri") != null;
    //*/

    private MemberVisitor enumFieldFinder = new AllAttributeVisitor(
                                            new AllInstructionVisitor(this));

    // Fields acting as parameters and return values for the visitors.
    private String  enumTypeName;
    private String  enumConstantName;
    private boolean enumConstantNameFound;
    private Clazz   referencedEnumClass;
    private Field   referencedEnumField;


    // Implementations for ElementValueVisitor.

    public void visitAnyElementValue(Clazz clazz, Annotation annotation, ElementValue elementValue) {}


    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        if (enumConstantElementValue.referencedClasses != null    &&
            enumConstantElementValue.referencedClasses.length > 0)
        {
            referencedEnumClass = enumConstantElementValue.referencedClasses[0];
            if (referencedEnumClass != null)
            {
                // Try to find the enum field through the static enum
                // initialization code (at least for program classes).
                enumTypeName        = enumConstantElementValue.getTypeName(clazz);
                enumConstantName    = enumConstantElementValue.getConstantName(clazz);
                referencedEnumField = null;
                referencedEnumClass.methodAccept(ClassConstants.METHOD_NAME_CLINIT,
                                                 ClassConstants.METHOD_TYPE_CLINIT,
                                                 enumFieldFinder);

                // Otherwise try to find the enum field through its name.
                // The constant name could be different from the field name, if
                // the latter is already obfuscated.
                if (referencedEnumField == null)
                {
                    referencedEnumField =
                        referencedEnumClass.findField(enumConstantName,
                                                      enumTypeName);
                }

                if (DEBUG)
                {
                    System.out.println("EnumFieldReferenceInitializer: ["+referencedEnumClass.getName()+"."+enumConstantName+"] -> "+referencedEnumField);
                }

                enumConstantElementValue.referencedField = referencedEnumField;
            }
        }
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            case InstructionConstants.OP_LDC:
            case InstructionConstants.OP_LDC_W:
            case InstructionConstants.OP_PUTSTATIC:
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        enumConstantNameFound =
            enumConstantName.equals(stringConstant.getString(clazz));
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        if (enumConstantNameFound)
        {
            if (enumTypeName.equals(fieldrefConstant.getType(clazz)))
            {
                referencedEnumField = (Field)fieldrefConstant.referencedField;
            }

            enumConstantNameFound = false;
        }
    }
}