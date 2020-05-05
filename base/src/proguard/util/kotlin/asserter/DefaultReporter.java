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

package proguard.util.kotlin.asserter;

import proguard.classfile.util.WarningPrinter;

/**
 * @author James Hamilton
 */
class DefaultReporter implements Reporter
{
    private final WarningPrinter warningPrinter;
    private       int            count;
    private       String         errorMessage = "";
    private       String         contextName  = "";


    DefaultReporter(WarningPrinter warningPrinter)
    {
        this.warningPrinter = warningPrinter;
        count = 0;
    }


    @Override
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    @Override
    public void report(String error)
    {
        if (count == 0)
        {
            warningPrinter.print(this.contextName, String.format(this.errorMessage, this.contextName));
        }
        count++;
        warningPrinter.print(this.contextName, "  " + error);
    }


    @Override
    public void resetCounter(String contextName)
    {
        this.contextName = contextName;
        this.count       = 0;
    }


    @Override
    public int getCount()
    {
        return count;
    }


    @Override
    public void print(String className, String s)
    {
        this.warningPrinter.print(className, s);
    }
}
