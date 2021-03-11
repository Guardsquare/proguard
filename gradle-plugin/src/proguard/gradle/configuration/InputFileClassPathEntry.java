package proguard.gradle.configuration;

import org.gradle.api.tasks.Classpath;
import proguard.ClassPathEntry;

import java.io.File;

class InputFileClassPathEntry extends ProGuardTaskClassPathEntry {

    public InputFileClassPathEntry(ClassPathEntry entry) {
        super(entry);
    }

    @Classpath
    public File getFile() {
        return entry.getFile();
    }

    @Override
    public String toString() {
        return "infile:" + getFile().getName();
    }
}
