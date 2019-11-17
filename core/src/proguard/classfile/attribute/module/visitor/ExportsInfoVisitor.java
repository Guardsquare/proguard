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
package proguard.classfile.attribute.module.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.module.ExportsInfo;

/**
 * This interface specifies the methods for a visitor of
 * <code>ExportsInfo</code> objects. Note that there is only a single
 * implementation of <code>ExportsInfo</code>, such that this interface
 * is not strictly necessary as a visitor.
 *
 * @author Joachim Vandersmissen
 */
public interface ExportsInfoVisitor
{
    public void visitExportsInfo(Clazz clazz, ExportsInfo exportsInfo);
}
