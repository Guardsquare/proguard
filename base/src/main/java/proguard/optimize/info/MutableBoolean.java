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

package proguard.optimize.info;

/**
 * This class provides a mutable boolean flag.
 */
public class MutableBoolean
{
    private boolean flag;
    /*
    private int     resetCounter;
    private int     setCounter;
    private int     totalCounter;
    //*/


    public void set()
    {
        flag = true;

        /*
        System.out.println("MutableBoolean.set: "+resetCounter+", "+setCounter++ +", "+totalCounter++);
        if (totalCounter > 5000)
        {
            Thread.dumpStack();
        }
        //*/
    }


    public void reset()
    {
        flag = false;

        /*
        resetCounter++;
        setCounter = 0;
        //*/
    }


    public boolean isSet()
    {
        return flag;
    }
}
