package proguard;

import proguard.io.DataEntry;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Data entry created by {@link JrtDataEntrySource}.
 */
public class JrtDataEntry implements DataEntry {
    private final Path jrtPath;
    private final String name;
    private final long size;

    private InputStream inputStream = null;

    public JrtDataEntry(Path jrtPath, String name, long size) {
        this.jrtPath = jrtPath;
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalName() {
        return getName();
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = new BufferedInputStream(Files.newInputStream(jrtPath));
        }
        return inputStream;
    }

    @Override
    public void closeInputStream() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
    }

    @Override
    public DataEntry getParent() {
        return null;
    }

    @Override
    public String toString() {
        return "jrt:/" + jrtPath;
    }
}
