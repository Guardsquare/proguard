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
package proguard.optimize.lambdainline.lambdalocator;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;

/**
 * A simple class containing information about a Kotlin lambda. These are lambdas implemented using a singleton pattern
 * the singleton is stored in the "INSTANCE" field. A reference to these lambdas is obtained by using the getstatic
 * instruction which obtains a reference to this field. This invokestatic field is what we store in the
 * getstaticInstruction field alongside it's offset getStaticOffset. The clazz, method and code attribute in which this
 * instruction appears is also stored in this class.
 */
public final class Lambda {
    private final Clazz clazz;
    private final Method method;
    private final CodeAttribute codeAttribute;
    private final int getStaticOffset;
    private final ConstantInstruction getstaticInstruction;

    /**
     * @param clazz The clazz in which the lambda usage was found.
     * @param method The method in which the lambda usage was found.
     * @param codeAttribute The code attribute in which the lambda usage was found.
     * @param getStaticOffset The offset of the getstatic instruction that was used to obtain a reference to the lambda
     *                        instance.
     * @param getstaticInstruction The getstatic instruction itself.
     */
    public Lambda(Clazz clazz, Method method, CodeAttribute codeAttribute, int getStaticOffset, ConstantInstruction getstaticInstruction) {
        this.clazz = clazz;
        this.method = method;
        this.codeAttribute = codeAttribute;
        this.getStaticOffset = getStaticOffset;
        this.getstaticInstruction = getstaticInstruction;
    }

    public Clazz clazz() {
        return clazz;
    }

    public Method method() {
        return method;
    }

    public CodeAttribute codeAttribute() {
        return codeAttribute;
    }

    public int getstaticOffset() {
        return getStaticOffset;
    }

    public ConstantInstruction getstaticInstruction() {
        return getstaticInstruction;
    }

    @Override
    public String toString() {
        return getstaticInstruction.toString();
    }
}
