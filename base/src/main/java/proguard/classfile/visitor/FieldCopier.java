package proguard.classfile.visitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.ClassEditor;

public class FieldCopier implements MemberVisitor, ConstantVisitor {

    private final ClassBuilder classBuilder;
    private final ClassEditor classEditor;
    private final FieldRenamer fieldRenamer;
    private ProgramField lastCopiedField;
    private boolean hasCopiedField = false;
    private static final Logger logger = LogManager.getLogger(FieldCopier.class);

    public FieldCopier(ClassBuilder builder, FieldRenamer renamer)
    {
        this.classBuilder = builder;
        this.classEditor = new ClassEditor(builder.getProgramClass());
        this.fieldRenamer = renamer;
    }

    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        String fieldName = programField.getName(programClass);
        if (this.fieldRenamer != null)
        {
            fieldName = fieldRenamer.getNextFieldName();
        }

        String fieldDescriptor = programField.getDescriptor(programClass);
        Field oldField = classBuilder.getProgramClass().findField(fieldName, null);
        Field oldFieldSameDescriptor = classBuilder.getProgramClass().findField(fieldName, fieldDescriptor);
        if (oldField != null && oldFieldSameDescriptor == null)
        {
            String oldFieldDescriptor = oldField.getDescriptor(classBuilder.getProgramClass());
            //logger.warn("Field " + fieldName + " already exists in class " + classBuilder.getProgramClass() + " with different descriptor: " + oldFieldDescriptor + " <-> " + fieldDescriptor + ". The field will be duplicated with different descriptors.");
                // Merge the field types: generalise to a common super type
                //fieldDescriptor = ClassConstants.TYPE_JAVA_LANG_OBJECT;
        }
        else if (oldFieldSameDescriptor != null)
        {
            this.classEditor.removeField(oldFieldSameDescriptor);
        }
        ProgramField copiedField = classBuilder.addAndReturnField(programField.u2accessFlags, fieldName, fieldDescriptor);
        if (this.fieldRenamer != null)
        {
            this.fieldRenamer.visitProgramField(classBuilder.getProgramClass(), copiedField);
        }
        this.lastCopiedField = copiedField;
        this.hasCopiedField = true;
    }

    @Override
    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
        fieldrefConstant.referencedFieldAccept(this);
    }

    public ProgramField getLastCopiedField()
    {
        return this.lastCopiedField;
    }

    public boolean hasCopiedField()
    {
        return this.hasCopiedField;
    }

    public void reset()
    {
        this.hasCopiedField = false;
    }
}
