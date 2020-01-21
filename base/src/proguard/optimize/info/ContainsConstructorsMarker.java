package proguard.optimize.info;

import proguard.classfile.*;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

/**
 * This MemberVisitor marks all classes that contain any kind of constructors.
 *
 * @author Joachim Vandersmissen
 */
public class ContainsConstructorsMarker
extends      SimplifiedVisitor
implements   MemberVisitor
{
    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz clazz, Member member) {}


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (programMethod.getName(programClass).equals(ClassConstants.METHOD_NAME_INIT))
        {
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setContainsConstructors();
        }
    }


    public static boolean containsConstructors(Clazz clazz)
    {
        return ClassOptimizationInfo.getClassOptimizationInfo(clazz).containsConstructors();
    }
}
