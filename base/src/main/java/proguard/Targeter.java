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
package proguard;

import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassVersionSetter;
import proguard.pass.Pass;

import java.io.IOException;
import java.util.*;

/**
 * This pass sets the target version on program classes.
 *
 * @author Eric Lafortune
 */
public class Targeter implements Pass
{
    /**
     * Sets the target version on classes in the given program class pool.
     */
    @Override
    public void execute(AppView appView) throws IOException
    {
        Set newerClassVersions = appView.configuration.warn != null ? null : new HashSet();

        appView.programClassPool.classesAccept(new ClassVersionSetter(appView.configuration.targetClassVersion,
                                                                      newerClassVersions));

        if (newerClassVersions != null &&
            newerClassVersions.size() > 0)
        {
            System.err.print("Warning: some classes have more recent versions (");

            Iterator iterator = newerClassVersions.iterator();
            while (iterator.hasNext())
            {
                Integer classVersion = (Integer)iterator.next();
                System.err.print(ClassUtil.externalClassVersion(classVersion));

                if (iterator.hasNext())
                {
                    System.err.print(",");
                }
            }

            System.err.println(")");
            System.err.println("         than the target version ("+ClassUtil.externalClassVersion(appView.configuration.targetClassVersion)+").");

            if (!appView.configuration.ignoreWarnings)
            {
                System.err.println("         If you are sure this is not a problem,");
                System.err.println("         you could try your luck using the '-ignorewarnings' option.");
                throw new IOException("Please correct the above warnings first.");
            }
        }
    }
}
