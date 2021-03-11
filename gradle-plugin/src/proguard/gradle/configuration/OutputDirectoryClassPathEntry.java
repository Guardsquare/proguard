package proguard.gradle.configuration;

import org.gradle.api.tasks.OutputDirectory;
import proguard.ClassPathEntry;

import java.io.File;

class OutputDirectoryClassPathEntry extends ProGuardTaskClassPathEntry {

    public OutputDirectoryClassPathEntry(ClassPathEntry entry) {
        super(entry);
    }

    @OutputDirectory
    public File getFile() {
        return entry.getFile();
    }

    @Override
    public String toString() {
        return "outdir:" + getFile().getName();
    }
}
