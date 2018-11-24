/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.util.UUID;

public class DataReader
{
    private final DataBuffer buffer;
    private final DataSource source;

    private DataRecord record;
    private int bufferPosition;

    DataReader(DataSource source)
    {
        this.source = source;
        buffer = source.getBuffer();
        bufferPosition = 0;
    }

    public boolean read()
    {
        if (bufferPosition == buffer.recordsCount())
            source.loadNextRecord();
        record = buffer.getRecord(bufferPosition++);
        return record != null;
    }

    public String getString(String keyName)
    {
        if (record == null)
            return null;
        return record.getValue(keyName);
    }
    public int getInt(String keyName)
    {
        return Integer.parseInt(getString(keyName));
    }
    public boolean getBoolean(String keyName)
    {
        switch (getString(keyName))
        {
            case "0":
                return false;
            case "1":
                return true;
            default:
                    // throw an exception
                    return false;
        }
    }
    public Date getDate(String keyName)
    {
        return new Date(getString(keyName));
    }
    public UUID getUUID()
    {
        return UUID.fromString(getString("uuid"));
    }
}
