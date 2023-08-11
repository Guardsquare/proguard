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
package proguard.optimize.lambdainline;

import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.editor.AttributeAdder;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.ClassEditor;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;
import proguard.obfuscate.UniqueMemberNameFactory;
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter;

import java.util.function.Function;

/**
 * A class that takes a method and creates a new method with a different name that has an updated descriptor. The
 * descriptor is made using a lambda that takes in the old descriptor and then returns the new descriptor. Optionally
 * the original method can be deleted.
 */
public class DescriptorModifier {
    private final ClassBuilder classBuilder;
    private final NameFactory nameFactory;
    private final Clazz clazz;

    DescriptorModifier(Clazz clazz) {
        classBuilder = new ClassBuilder((ProgramClass) clazz);
        nameFactory = new UniqueMemberNameFactory(new SimpleNameFactory(), clazz);
        this.clazz = clazz;
    }
    
    public ProgramMethod modify(ProgramMethod sourceMethod, Function<String, String> modifier) {
        return modify(sourceMethod, modifier, false);
    }

    public ProgramMethod modify(ProgramMethod sourceMethod, Function<String, String> modifier, boolean removeOriginal) {
        ProgramMethod newMethod = classBuilder.addAndReturnMethod(
                sourceMethod.u2accessFlags,
                nameFactory.nextName(),
                modifier.apply(sourceMethod.getDescriptor(clazz))
        );

        sourceMethod.attributesAccept((ProgramClass) clazz,
                new AttributeAdder((ProgramClass) clazz, newMethod, true));

        if (removeOriginal)
            removeOriginal(sourceMethod);

        newMethod.accept(clazz, new ProgramMemberOptimizationInfoSetter());
        return newMethod;
    }

    private void removeOriginal(ProgramMethod method) {
        ClassEditor classEditor = new ClassEditor((ProgramClass) clazz);
        classEditor.removeMethod(method);
    }
}
