package proguard.gradle.configuration;

import org.gradle.api.tasks.Classpath;
import proguard.ClassPathEntry;

import java.io.File;

class InputDirectoryClassPathEntry extends ProGuardTaskClassPathEntry {

    public InputDirectoryClassPathEntry(ClassPathEntry entry) {
        super(entry);
    }

    @Classpath
    public File getFile() {
        return entry.getFile();
    }

    @Override
    public String toString() {
        return "indir:" + getFile().getName();
    }
}
