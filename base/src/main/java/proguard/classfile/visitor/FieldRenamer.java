package proguard.classfile.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;

import java.util.HashMap;
import java.util.Map;

public class FieldRenamer implements MemberVisitor, ConstantVisitor {

    private final String newFieldNamePrefix;
    private int newFieldNameIndex = 0;
    private final boolean useDescriptorBasedNames;
    private final Map<String, Integer> descriptorIndex = new HashMap<>();
    private Field lastVisitedField;
    private Clazz lastVisitedClass;

    public FieldRenamer(String newFieldNamePrefix)
    {
        this.newFieldNamePrefix      = newFieldNamePrefix;
        this.useDescriptorBasedNames = false;
    }

    public FieldRenamer(boolean useDescriptorBasedNames)
    {
        this.newFieldNamePrefix      = "";
        this.useDescriptorBasedNames = useDescriptorBasedNames;
    }

    public void resetIndex()
    {
        this.newFieldNameIndex = 0;
    }

    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        this.lastVisitedClass = programClass;
        this.lastVisitedField = programField;
        programClass.constantPoolEntryAccept(programField.u2nameIndex, this);
    }

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    @Override
    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        String newName = getNextFieldName();
        utf8Constant.setString(newName);
        this.newFieldNameIndex++;
        String descriptor = this.lastVisitedField.getDescriptor(this.lastVisitedClass);
        this.descriptorIndex.put(descriptor, this.descriptorIndex.getOrDefault(descriptor, 0) + 1);

    }

    public String getNextFieldName()
    {
        String newName;
        if (useDescriptorBasedNames)
        {
            // This is non-logical behaviour: the method name suggests a globally correct next name would be
            // returned, but here it depends on the previously visited field, while in practice
            // we don't know whether the next field will have the same descriptor
            String descriptor = this.lastVisitedField.getDescriptor(this.lastVisitedClass);
            newName = descriptor.replace(";", "").replace("/", "").replace("[", "") + this.descriptorIndex.getOrDefault(descriptor, 0);

        }
        else
        {
            newName = this.newFieldNamePrefix + (this.newFieldNameIndex + 1);
        }
        return newName;
    }
}
