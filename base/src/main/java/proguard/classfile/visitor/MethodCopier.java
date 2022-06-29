package proguard.classfile.visitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import java.util.Objects;

public class MethodCopier implements MemberVisitor, AttributeVisitor, InstructionVisitor
{
    private final ProgramClass destinationClass;
    private final ClassBuilder classBuilder;
    private final String newMethodNamePrefix;
    private final String newMethodDescriptor;
    private final int accessFlags;
    private final ConstantAdder constantAdder;
    private final CodeAttributeComposer codeAttributeComposer = new CodeAttributeComposer();
    private final ExceptionInfoAdder exceptionInfoAdder;
    private int methodCounter = 0;
    private final FieldRenamer fieldRenamer;
    private final FieldCopier fieldCopier;
    private static final Logger logger = LogManager.getLogger(MethodCopier.class);

    public MethodCopier(ProgramClass destinationClass, String newMethodNamePrefix, int accessFlags)
    {
        this(destinationClass, newMethodNamePrefix, null, accessFlags);
    }

    public MethodCopier(ProgramClass destinationClass, String newMethodNamePrefix, String newMethodDescriptor, int accessFlags)
    {
        this(destinationClass, newMethodNamePrefix, newMethodDescriptor, accessFlags, null);
    }

    public MethodCopier(ProgramClass destinationClass, String newMethodNamePrefix, String newMethodDescriptor, int accessFlags, FieldRenamer fieldRenamer)
    {
        this.destinationClass    = destinationClass;
        this.classBuilder        = new ClassBuilder(destinationClass);
        this.newMethodNamePrefix = newMethodNamePrefix;
        this.newMethodDescriptor = newMethodDescriptor;
        this.accessFlags         = accessFlags;
        this.fieldRenamer        = fieldRenamer;
        this.fieldCopier         = new FieldCopier(this.classBuilder, this.fieldRenamer);
        this.constantAdder       = new ConstantAdder(destinationClass);
        this.exceptionInfoAdder  = new ExceptionInfoAdder(this.destinationClass, this.codeAttributeComposer);
    }

    private int getNewMethodIndex()
    {
        int methodIndex = this.methodCounter;
        this.methodCounter++;
        return methodIndex;
    }

    @Override
    public void visitAnyMember(Clazz clazz, Member member) {
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        codeAttributeComposer.reset();
        if (this.fieldRenamer != null)
        {
            this.fieldRenamer.resetIndex();
        }
        programMethod.attributesAccept(programClass, this);
        int methodIndex = getNewMethodIndex();
        String newMethodName = newMethodNamePrefix;
        if (methodIndex > 1)
        {
            logger.warn(methodIndex + " methods were visited by MethodCopier(" + destinationClass + ", " + newMethodNamePrefix +").");
            newMethodName += "$" + methodIndex;
        }
        String methodDescriptor = programMethod.getDescriptor(programClass);
        if (this.newMethodDescriptor != null)
        {
            methodDescriptor = this.newMethodDescriptor;
        }
        ProgramMethod newMethod = classBuilder.addAndReturnMethod(accessFlags, newMethodName, methodDescriptor);
        codeAttributeComposer.addCodeAttribute(this.destinationClass, newMethod);
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        codeAttributeComposer.beginCodeFragment(codeAttribute.u4codeLength);
        // copy code and exceptions
        codeAttribute.instructionsAccept(clazz, method, this);
        codeAttribute.exceptionsAccept(clazz, method, this.exceptionInfoAdder);
        codeAttribute.attributesAccept(clazz, method, this);
        codeAttributeComposer.endCodeFragment();
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
        // copy instruction
        codeAttributeComposer.appendInstruction(offset, instruction);
    }

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        // TODO: Replace references to the lambda class itself by references to the new lambda group.
        //  (WIP)
        this.fieldCopier.reset();
        clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new ConstantVisitor() {

            @Override
            public void visitAnyConstant(Clazz clazz, Constant constant) {}

            @Override
            public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
            {
                // TODO: replace lambda reference by lambda group reference
                //  Note: is it sufficient to only replace the class constant?
                //        or should the name of the class also be updated
                if (Objects.equals(fieldrefConstant.referencedClass, clazz))
                {
                    // copy the field to the lambda group
                    fieldrefConstant.referencedFieldAccept(fieldCopier);
                }
            }

            @Override
            public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
            {
                if (Objects.equals(classConstant.referencedClass, clazz))
                {
                    logger.info("Class " + clazz + " references itself in a constant instruction: " + constantInstruction);
                }
            }
        });
        if (this.fieldCopier.hasCopiedField())
        {
            // add the necessary constants to the lambda group
            constantInstruction.constantIndex = classBuilder.getConstantPoolEditor().addFieldrefConstant(destinationClass, fieldCopier.getLastCopiedField());
        }
        else
        {
            // ensure the referenced constant is in the constant pool at the correct index
            constantInstruction.constantIndex = this.constantAdder.addConstant(clazz, constantInstruction.constantIndex);
        }
        // copy instruction
        codeAttributeComposer.appendInstruction(offset, constantInstruction);
    }
}
