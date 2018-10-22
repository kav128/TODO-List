/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

public abstract class DataSource
{
    protected final String connectionString;
    protected boolean opened;
    protected final DataBuffer buffer;

    DataSource(String connectionString)
    {
        this.connectionString = connectionString;
        opened = false;

        buffer = new DataBuffer();
    }

    public void open()
    {
        openResource();
    }

    public void close()
    {
        commit();
        closeResource();
    }

    public void commit()
    {
        if (!buffer.hasChanges())
            return;

        beginWrite();

        int recordCount = buffer.recordsCount();
        for (int i = 0; i < recordCount; i++)
        {
            DataRecord curRecord = buffer.getRecord(i);
            writeRecord(curRecord);
        }

        endWrite();

        buffer.commit();
    }
    public boolean isOpened()
    {
        return opened;
    }

    protected abstract void openResource();
    protected abstract void closeResource();
    boolean loadNextRecord()
    {
        DataRecord record = getNextRecord();
        if (record == null)
            return false;
        buffer.loadRecord(record);
        return true;
    }
    protected abstract DataRecord getNextRecord();

    protected abstract void beginWrite();
    protected abstract void endWrite();
    protected abstract void writeRecord(DataRecord record);

    public DataReader getReader()
    {
        return new DataReader(this);
    }

    public DataWriter getWriter()
    {
        return new DataWriter(this);
    }
}
