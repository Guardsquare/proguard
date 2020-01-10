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
package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link MemberVisitor} lets a given {@link MemberVisitor}
 * travel to all concrete and abstract implementations of the visited methods
 * in their class hierarchies.
 *
 * @author Eric Lafortune
 */
public class MethodImplementationTraveler
extends      SimplifiedVisitor
implements   MemberVisitor
{
    private final boolean       visitThisMethod;
    private final boolean       visitSuperMethods;
    private final boolean       visitInterfaceMethods;
    private final boolean       visitOverridingMethods;
    private final MemberVisitor memberVisitor;


    /**
     * Creates a new MethodImplementationTraveler.
     * @param visitThisMethod        specifies whether to visit the originally
     *                               visited methods.
     * @param visitSuperMethods      specifies whether to visit the method in
     *                               the super classes.
     * @param visitInterfaceMethods  specifies whether to visit the method in
     *                               the interface classes.
     * @param visitOverridingMethods specifies whether to visit the method in
     *                               the subclasses.
     * @param memberVisitor          the <code>MemberVisitor</code> to which
     *                               visits will be delegated.
     */
    public MethodImplementationTraveler(boolean       visitThisMethod,
                                        boolean       visitSuperMethods,
                                        boolean       visitInterfaceMethods,
                                        boolean       visitOverridingMethods,
                                        MemberVisitor memberVisitor)
    {
        this.visitThisMethod        = visitThisMethod;
        this.visitSuperMethods      = visitSuperMethods;
        this.visitInterfaceMethods  = visitInterfaceMethods;
        this.visitOverridingMethods = visitOverridingMethods;
        this.memberVisitor          = memberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (visitThisMethod)
        {
            programMethod.accept(programClass, memberVisitor);
        }

        if (!isSpecial(programClass, programMethod))
        {
            programClass.hierarchyAccept(false,
                                         visitSuperMethods,
                                         visitInterfaceMethods,
                                         visitOverridingMethods,
                                         new NamedMethodVisitor(programMethod.getName(programClass),
                                                                programMethod.getDescriptor(programClass),
                                         new MemberAccessFilter(0,
                                                                AccessConstants.PRIVATE |
                                                                AccessConstants.STATIC,
                                         memberVisitor)));
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        if (visitThisMethod)
        {
            libraryMethod.accept(libraryClass, memberVisitor);
        }

        if (!isSpecial(libraryClass, libraryMethod))
        {
            libraryClass.hierarchyAccept(false,
                                         visitSuperMethods,
                                         visitInterfaceMethods,
                                         visitOverridingMethods,
                                         new NamedMethodVisitor(libraryMethod.getName(libraryClass),
                                                                libraryMethod.getDescriptor(libraryClass),
                                         new MemberAccessFilter(0,
                                                                AccessConstants.PRIVATE |
                                                                AccessConstants.STATIC,
                                         memberVisitor)));
        }
    }


    // Small utility methods.

    private boolean isSpecial(Clazz clazz, Method method)
    {
        return (method.getAccessFlags() &
                (AccessConstants.PRIVATE |
                 AccessConstants.STATIC)) != 0 ||
               method.getName(clazz).equals(ClassConstants.METHOD_NAME_INIT);
    }
}
