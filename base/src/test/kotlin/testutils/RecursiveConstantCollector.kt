package testutils

import proguard.classfile.Clazz
import proguard.classfile.constant.*
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.editor.ConstantPoolEditor

class RecursiveConstantCollector(val constantPoolEditor: ConstantPoolEditor) : ConstantVisitor {
    var firstAddedConstantIndex = 0
    override fun visitAnyConstant(clazz: Clazz?, constant: Constant?) {
        val constantIndex = constantPoolEditor.addConstant(constant)
        if (firstAddedConstantIndex == 0)
        {
            firstAddedConstantIndex = constantIndex
        }
    }

    override fun visitStringConstant(clazz: Clazz?, stringConstant: StringConstant?) {
        visitAnyConstant(clazz, stringConstant)
        clazz?.constantPoolEntryAccept(stringConstant?.u2stringIndex!!, this)
    }

    override fun visitDynamicConstant(clazz: Clazz?, dynamicConstant: DynamicConstant?) {
        visitAnyConstant(clazz, dynamicConstant)
        clazz?.constantPoolEntryAccept(dynamicConstant?.nameAndTypeIndex!!, this)
        clazz?.constantPoolEntryAccept(dynamicConstant?.bootstrapMethodAttributeIndex!!, this)
    }

    override fun visitInvokeDynamicConstant(clazz: Clazz?, invokeDynamicConstant: InvokeDynamicConstant?) {
        visitAnyConstant(clazz, invokeDynamicConstant)
        clazz?.constantPoolEntryAccept(invokeDynamicConstant?.nameAndTypeIndex!!, this)
        clazz?.constantPoolEntryAccept(invokeDynamicConstant?.bootstrapMethodAttributeIndex!!, this)
    }

    override fun visitMethodHandleConstant(clazz: Clazz?, methodHandleConstant: MethodHandleConstant?) {
        visitAnyConstant(clazz, methodHandleConstant)
        clazz?.constantPoolEntryAccept(methodHandleConstant?.referenceIndex!!, this)
    }

    override fun visitModuleConstant(clazz: Clazz?, moduleConstant: ModuleConstant?) {
        visitAnyConstant(clazz, moduleConstant)
        clazz?.constantPoolEntryAccept(moduleConstant?.u2nameIndex!!, this)
    }

    override fun visitPackageConstant(clazz: Clazz?, packageConstant: PackageConstant?) {
        visitAnyConstant(clazz, packageConstant)
        clazz?.constantPoolEntryAccept(packageConstant?.u2nameIndex!!, this)
    }

    override fun visitAnyRefConstant(clazz: Clazz?, refConstant: RefConstant?) {
        visitAnyConstant(clazz, refConstant)
        clazz?.constantPoolEntryAccept(refConstant?.nameAndTypeIndex!!, this)
        clazz?.constantPoolEntryAccept(refConstant?.classIndex!!, this)
    }

    override fun visitClassConstant(clazz: Clazz?, classConstant: ClassConstant?) {
        visitAnyConstant(clazz, classConstant)
        clazz?.constantPoolEntryAccept(classConstant?.u2nameIndex!!, this)
    }

    override fun visitMethodTypeConstant(clazz: Clazz?, methodTypeConstant: MethodTypeConstant?) {
        visitAnyConstant(clazz, methodTypeConstant)
        clazz?.constantPoolEntryAccept(methodTypeConstant?.descriptorIndex!!, this)
    }

    override fun visitNameAndTypeConstant(clazz: Clazz?, nameAndTypeConstant: NameAndTypeConstant?) {
        visitAnyConstant(clazz, nameAndTypeConstant)
        clazz?.constantPoolEntryAccept(nameAndTypeConstant?.u2descriptorIndex!!, this)
        clazz?.constantPoolEntryAccept(nameAndTypeConstant?.u2nameIndex!!, this)
    }
}
