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
package proguard.util;

/**
 * Base interface for entities that need flags when they are processed.
 * The processing flags provide details on how the entity should be
 * processed, possibly across processing steps.
 *
 * @see ProcessingFlags
 *
 * @author Johan Leys
 */
public interface Processable
{
    /**
     * Returns the processing flags for this entity.
     */
    public int getProcessingFlags();


    /**
     * Sets the processing flags for this entity.
     *
     * @param processingFlags the processing flags to be set.
     */
    public void setProcessingFlags(int processingFlags);
}
