/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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
package proguard.optimize.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import java.io.PrintWriter;

/**
 * This {@link ClassVisitor} prints out the merged lambda classes
 * and the lambda groups into which they were merged, including their
 * original arity and the class id in the resulting lambda group.
 *
 * @see proguard.optimize.kotlin.KotlinLambdaMerger
 *
 * @author Joren Van Hecke
 */
public class LambdaGroupMappingPrinter implements ClassVisitor {

    private final PrintWriter pw;

    /**
     * Creates a new LambdaGroupMappingPrinter that prints to the given writer.
     * @param printWriter the writer to which to print.
     */
    public LambdaGroupMappingPrinter(PrintWriter printWriter)
    {
        this.pw = printWriter;
    }


    @Override
    public void visitAnyClass(Clazz clazz) { }

    @Override
    public void visitProgramClass(ProgramClass programClass) {
        String name    = programClass.getName();
        ProgramClassOptimizationInfo optimizationInfo = 
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        Clazz lambdaGroup = optimizationInfo.getLambdaGroup();
        if (lambdaGroup != null)
        {
            String lambdaGroupName = lambdaGroup.getName();
            // Print out the class to lambda group mapping.
            pw.println(ClassUtil.externalClassName(name) +
                    " -> " +
                    ClassUtil.externalClassName(lambdaGroupName) +
                    " (arity " +
                    KotlinLambdaMerger.getArityFromInterface(programClass) +
                    ", case " +
                    optimizationInfo.getLambdaGroupClassId() +
                    ")");
        }
    }
}
