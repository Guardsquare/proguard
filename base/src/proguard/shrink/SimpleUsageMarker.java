/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.shrink;

import proguard.util.Processable;

/**
 * This class marks processables, in order to remember whether they are
 * unused, possibly used, or definitely used.
 */
public class SimpleUsageMarker
{
    private final Object POSSIBLY_USED = new Object();
    private final Object USED          = new Object();


    /**
     * Marks the given processable as possibly being used.
     */
    public void markAsPossiblyUsed(Processable processable)
    {
        processable.setProcessingInfo(POSSIBLY_USED);
    }


    /**
     * Returns whether the given processable has been marked as possibly
     * being used.
     */
    public boolean isPossiblyUsed(Processable processable)
    {
        return processable.getProcessingInfo() == POSSIBLY_USED;
    }


    /**
     * Marks the given processable as being used.
     */
    public void markAsUsed(Processable processable)
    {
        processable.setProcessingInfo(USED);
    }


    /**
     * Clears any usage marks from the given processable.
     */
    public void markAsUnused(Processable processable)
    {
        processable.setProcessingInfo(null);
    }


    /**
     * Returns whether the given processable has been marked as being used.
     */
    public boolean isUsed(Processable processable)
    {
        return processable.getProcessingInfo() == USED;
    }
}
