package proguard.gradle.configuration;

import org.gradle.api.tasks.OutputFile;
import proguard.ClassPathEntry;

import java.io.File;

class OutputFileClassPathEntry extends ProGuardTaskClassPathEntry {

    public OutputFileClassPathEntry(ClassPathEntry entry) {
        super(entry);
    }

    @OutputFile
    public File getFile() {
        return entry.getFile();
    }

    @Override
    public String toString() {
        return "outfile:" + getFile().getName();
    }
}
