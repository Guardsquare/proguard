package proguard.gradle.configuration;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import proguard.ClassPath;
import proguard.ClassPathEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Gradle-aware wrapper for the ProGuard `ClassPathEntry`, allowing inputs and outputs to be correctly declared.
 * For each combination of input/output and file/directory there is a concrete subtype with correctly annotated
 * {@link #getFile()} method.
 */
public abstract class ProGuardTaskClassPathEntry {
    protected final ClassPathEntry entry;

    public ProGuardTaskClassPathEntry(ClassPathEntry entry) {
        this.entry = entry;
    }

    /**
     * This method is implemented in different subtypes in order to present the correct annotations.
     */
    public abstract File getFile();

    @Internal
    public String getName() {
        return entry.getName();
    }

    @Optional
    @Input
    public String getFeatureName() {
        return entry.getFeatureName();
    }

    @Optional
    @Input
    public List getFilter() {
        return entry.getFilter();
    }

    @Optional
    @Input
    public List getJarFilter() {
        return entry.getJarFilter();
    }

    @Optional
    @Input
    public List getWarFilter() {
        return entry.getWarFilter();
    }

    @Optional
    @Input
    public List getJmodFilter() {
        return entry.getJmodFilter();
    }

    @Optional
    @Input
    public List getZipFilter() {
        return entry.getZipFilter();
    }

    @Optional
    @Input
    public List getApkFilter() {
        return entry.getApkFilter();
    }

    @Optional
    @Input
    public List getAabFilter() {
        return entry.getAabFilter();
    }

    @Optional
    @Input
    public List getAarFilter() {
        return entry.getAarFilter();
    }

    @Optional
    @Input
    public List getEarFilter() {
        return entry.getEarFilter();
    }
}
