package proguard.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author James Hamilton
 */
public class OneTimeDataEntryWriter
implements   DataEntryWriter
{
    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return false;
    }

    @Override
    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2) throws IOException
    {
        return false;
    }

    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        return null;
    }

    @Override
    public void close() throws IOException
    {

    }

    @Override
    public void println(PrintWriter pw, String prefix)
    {

    }
}
