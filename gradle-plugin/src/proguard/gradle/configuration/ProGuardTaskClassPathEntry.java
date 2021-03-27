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

package proguard.gradle.configuration;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import proguard.ClassPathEntry;

import java.io.Serializable;
import java.util.List;

/**
 * A Gradle-aware wrapper for the ProGuard `ClassPathEntry`, representing all non-file details for an entry.
 */
public class ProGuardTaskClassPathEntry implements Serializable
{
    private String  featureName;
    private List    filter;
    private List    apkFilter;
    private List    aabFilter;
    private List    jarFilter;
    private List    aarFilter;
    private List    warFilter;
    private List    earFilter;
    private List    jmodFilter;
    private List    zipFilter;

    public ProGuardTaskClassPathEntry(ClassPathEntry entry)
    {
        this.featureName = entry.getFeatureName();
        this.filter = entry.getFilter();
        this.apkFilter = entry.getApkFilter();
        this.aabFilter = entry.getAabFilter();
        this.jarFilter = entry.getJarFilter();
        this.aarFilter = entry.getAarFilter();
        this.warFilter = entry.getWarFilter();
        this.earFilter = entry.getEarFilter();
        this.jmodFilter = entry.getJmodFilter();
        this.zipFilter = entry.getZipFilter();
    }

    @Optional
    @Input
    public String getFeatureName()
    {
        return featureName;
    }

    @Optional
    @Input
    public List getFilter()
    {
        return filter;
    }

    @Optional
    @Input
    public List getJarFilter()
    {
        return jarFilter;
    }

    @Optional
    @Input
    public List getWarFilter()
    {
        return warFilter;
    }

    @Optional
    @Input
    public List getJmodFilter()
    {
        return jmodFilter;
    }

    @Optional
    @Input
    public List getZipFilter()
    {
        return zipFilter;
    }

    @Optional
    @Input
    public List getApkFilter()
    {
        return apkFilter;
    }

    @Optional
    @Input
    public List getAabFilter()
    {
        return aabFilter;
    }

    @Optional
    @Input
    public List getAarFilter()
    {
        return aarFilter;
    }

    @Optional
    @Input
    public List getEarFilter()
    {
        return earFilter;
    }
}
