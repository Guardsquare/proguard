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
 * Base interface for entities that are part of a feature, as indicated by a
 * feature name.
 *
 * The entities can for example be classes and resource files, and the feature
 * can be a dynamic feature in an Android app.
 *
 *
 * @author Eric Lafortune
 */
public interface FeatureNamed
{
    /**
     * Returns the feature name for this entity.
     */
    public String getFeatureName();


    /**
     * Sets the feature name for this entity.
     *
     * @param featureName the feature Name to be set.
     */
    public void setFeatureName(String featureName);
}