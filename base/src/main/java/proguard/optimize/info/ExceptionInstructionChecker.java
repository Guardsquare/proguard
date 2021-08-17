/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
package proguard.optimize.info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;

/**
 * This class can tell whether an instruction might throw exceptions.
 *
 * @author Eric Lafortune
 */
public class ExceptionInstructionChecker
implements   InstructionVisitor
//             ConstantVisitor,
//             MemberVisitor
{
    private static final Logger logger = LogManager.getLogger(ExceptionInstructionChecker.class);


    // A return value for the visitor methods.
    private boolean mayThrowExceptions;


    /**
     * Returns whether the specified method may throw exceptions.
     */
    public boolean mayThrowExceptions(Clazz         clazz,
                                      Method        method,
                                      CodeAttribute codeAttribute)
    {
        return mayThrowExceptions(clazz,
                                  method,
                                  codeAttribute,
                                  0,
                                  codeAttribute.u4codeLength);
    }


    /**
     * Returns whether the specified block of code may throw exceptions.
     */
    public boolean mayThrowExceptions(Clazz         clazz,
                                      Method        method,
                                      CodeAttribute codeAttribute,
                                      int           startOffset,
                                      int           endOffset)
    {
        logger.debug("ExceptionInstructionChecker.mayThrowExceptions [{}.{}{}]: {} -> {}",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz),
                     startOffset,
                     endOffset
        );

        return firstExceptionThrowingInstructionOffset(clazz,
                                                       method,
                                                       codeAttribute,
                                                       startOffset,
                                                       endOffset) < endOffset;
    }


    /**
     * Returns the offset of the first instruction in the specified block of
     * code that may throw exceptions, or the end offset if there is none.
     */
    public int firstExceptionThrowingInstructionOffset(Clazz         clazz,
                                                       Method        method,
                                                       CodeAttribute codeAttribute,
                                                       int           startOffset,
                                                       int           endOffset)
    {
        logger.debug("ExceptionInstructionChecker.firstExceptionThrowingInstructionOffset [{}.{}{}]: {} -> {}",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz),
                     startOffset,
                     endOffset
        );

        byte[] code = codeAttribute.code;

        // Go over all instructions.
        int offset = startOffset;
        while (offset < endOffset)
        {
            // Get the current instruction.
            Instruction instruction = InstructionFactory.create(code, offset);

            // Check if it may be throwing exceptions.
            if (mayThrowExceptions(clazz,
                                   method,
                                   codeAttribute,
                                   offset,
                                   instruction))
            {
                logger.debug("  {}", instruction.toString(offset));

                return offset;
            }

            // Go to the next instruction.
            offset += instruction.length(offset);
        }

        return endOffset;
    }


    /**
     * Returns the offset after the last instruction in the specified block of
     * code that may throw exceptions, or the start offset if there is none.
     */
    public int lastExceptionThrowingInstructionOffset(Clazz         clazz,
                                                       Method        method,
                                                       CodeAttribute codeAttribute,
                                                       int           startOffset,
                                                       int           endOffset)
    {
        logger.debug("ExceptionInstructionChecker.lastExceptionThrowingInstructionOffset [{}.{}{}]: {} -> {}",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz),
                     startOffset,
                     endOffset
        );

        byte[] code = codeAttribute.code;

        int lastOffset = startOffset;

        // Go over all instructions.
        int offset = startOffset;
        while (offset < endOffset)
        {
            // Get the current instruction.
            Instruction instruction = InstructionFactory.create(code, offset);

            // Check if it may be throwing exceptions.
            if (mayThrowExceptions(clazz,
                                   method,
                                   codeAttribute,
                                   offset,
                                   instruction))
            {
                logger.debug("  {}", instruction.toString(offset));

                // Go to the next instruction.
                offset += instruction.length(offset);

                lastOffset = offset;
            }
            else
            {
                // Go to the next instruction.
                offset += instruction.length(offset);
            }
        }

        return lastOffset;
    }


    /**
     * Returns whether the specified instruction may throw exceptions.
     */
    public boolean mayThrowExceptions(Clazz         clazz,
                                      Method        method,
                                      CodeAttribute codeAttribute,
                                      int           offset)
    {
        Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);

        return mayThrowExceptions(clazz,
                                  method,
                                  codeAttribute,
                                  offset,
                                  instruction);
    }


    /**
     * Returns whether the given instruction may throw exceptions.
     */
    public boolean mayThrowExceptions(Clazz         clazz,
                                      Method        method,
                                      CodeAttribute codeAttribute,
                                      int           offset,
                                      Instruction   instruction)
    {
        return instruction.mayInstanceThrowExceptions(clazz);

//        mayThrowExceptions = false;
//
//        instruction.accept(clazz, method,  codeAttribute, offset, this);
//
//        return mayThrowExceptions;
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        // Check for instructions that may throw exceptions.
        // Note that monitorexit can not sensibly throw exceptions, except the
        // broken and deprecated asynchronous ThreadDeath. Removing the
        // artificial infinite looping exception blocks that recent compilers
        // add does not strictly follow the JVM specs, but it does have the
        // additional benefit of avoiding a bug in the JVM in JDK 1.1.
        switch (simpleInstruction.opcode)
        {
            case Instruction.OP_IDIV:
            case Instruction.OP_LDIV:
            case Instruction.OP_IREM:
            case Instruction.OP_LREM:
            case Instruction.OP_IALOAD:
            case Instruction.OP_LALOAD:
            case Instruction.OP_FALOAD:
            case Instruction.OP_DALOAD:
            case Instruction.OP_AALOAD:
            case Instruction.OP_BALOAD:
            case Instruction.OP_CALOAD:
            case Instruction.OP_SALOAD:
            case Instruction.OP_IASTORE:
            case Instruction.OP_LASTORE:
            case Instruction.OP_FASTORE:
            case Instruction.OP_DASTORE:
            case Instruction.OP_AASTORE:
            case Instruction.OP_BASTORE:
            case Instruction.OP_CASTORE:
            case Instruction.OP_SASTORE:
            case Instruction.OP_NEWARRAY:
            case Instruction.OP_ARRAYLENGTH:
            case Instruction.OP_ATHROW:
            case Instruction.OP_MONITORENTER:
                // These instructions may throw exceptions.
                mayThrowExceptions = true;
        }
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        // Check for instructions that may throw exceptions.
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_GETSTATIC:
            case Instruction.OP_PUTSTATIC:
            case Instruction.OP_GETFIELD:
            case Instruction.OP_PUTFIELD:
            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:
            case Instruction.OP_NEW:
            case Instruction.OP_ANEWARRAY:
            case Instruction.OP_CHECKCAST:
            case Instruction.OP_INSTANCEOF:
            case Instruction.OP_MULTIANEWARRAY:
                // These instructions may throw exceptions.
                mayThrowExceptions = true;

//          case Instruction.OP_INVOKEVIRTUAL:
//          case Instruction.OP_INVOKESPECIAL:
//          case Instruction.OP_INVOKESTATIC:
//          case Instruction.OP_INVOKEINTERFACE:
//            // Check if the invoking the method may throw an exception.
//            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
        }
    }


//    // Implementations for ConstantVisitor.
//
//    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
//    {
//        Member referencedMember = anyMethodrefConstant.referencedMethod;
//
//        // Do we have a reference to the method?
//        if (referencedMember == null)
//        {
//            // We'll have to assume invoking the unknown method may throw an
//            // an exception.
//            mayThrowExceptions = true;
//        }
//        else
//        {
//            // First check the referenced method itself.
//            anyMethodrefConstant.referencedMethodAccept(this);
//
//            // If the result isn't conclusive, check down the hierarchy.
//            if (!mayThrowExceptions)
//            {
//                Clazz  referencedClass  = anyMethodrefConstant.referencedClass;
//                Method referencedMethod = (Method)referencedMember;
//
//                // Check all other implementations of the method in the class
//                // hierarchy.
//                referencedClass.methodImplementationsAccept(referencedMethod,
//                                                            false,
//                                                            false,
//                                                            true,
//                                                            true,
//                                                            this);
//            }
//        }
//    }
//
//
//    // Implementations for MemberVisitor.
//
//    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
//    {
//        mayThrowExceptions = mayThrowExceptions ||
//                             ExceptionMethodMarker.mayThrowExceptions(programMethod);
//    }
//
//
//    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
//    {
//        mayThrowExceptions = mayThrowExceptions ||
//                             !NoExceptionMethodMarker.doesntThrowExceptions(libraryMethod);
//    }
}
