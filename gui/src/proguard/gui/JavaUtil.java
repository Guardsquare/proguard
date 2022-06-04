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

package proguard.gui;

import java.io.File;

/**
 * @author James Hamilton
 */
public class JavaUtil
{
    public static int currentJavaVersion() {
        String version = System.getProperty("java.version");
        
        if (version.endsWith("-ea")) {
            version = version.substring(0, version.length() - 3);
        }
        
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf('.');
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        
        return Integer.parseInt(version);
    }

    public static File getCurrentJavaHome() {
        if (currentJavaVersion() > 8) {
            return new File(System.getProperty("java.home"));
        } else {
            return new File(System.getProperty("java.home")).getParentFile();
        }
    }
    
    public static File getRtJar() {
        File currentJavaHome = getCurrentJavaHome();
        if (new File(currentJavaHome, "jre").exists()) {
            return new File(currentJavaHome, "jre" + File.separator + "lib" + File.separator + "rt.jar");
        } else {
            return new File(currentJavaHome, "lib" + File.separator + "rt.jar");
        }
    }

    public static File getJmodBase() {
        return new File(JavaUtil.getCurrentJavaHome(), "jmods" + File.separator + "java.base.jmod");
    }
}
