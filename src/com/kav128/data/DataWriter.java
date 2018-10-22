/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DataWriter
{
    private enum Action
    {
        noAction, create, modify
    }

    private final DataBuffer buffer;
    private final DataSource source;
    private DataRecord record;
    private Action action;

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public DataWriter(DataSource source)
    {
        this.source = source;
        buffer = source.buffer;
    }

    public UUID newRecord()
    {
        record = new DataRecord();
        UUID uuid = UUID.randomUUID();
        record.setValue("uuid", uuid.toString());
        action = Action.create;
        return uuid;
    }

    public void modifyRecord(UUID uuid)
    {
        record = new DataRecord();
        record.setValue("uuid", uuid.toString());
        action = Action.modify;
    }

    public void setField(String field, String value)
    {
        record.setValue(field, value);
    }

    public void setField(String field, int value)
    {
        record.setValue(field, String.valueOf(value));
    }

    public void setField(String field, boolean value)
    {
        record.setValue(field, value ? "1" : "0");
    }

    public void setField(String field, Date value)
    {
        record.setValue(field,dateFormat.format(value));
    }

    public void accept()
    {
        switch (action)
        {
            case create:
                buffer.create(record);
                break;
            case modify:
                buffer.modify(record);
                break;
        }
        action = Action.noAction;
    }

    public void remove(UUID uuid)
    {
        buffer.removeByUUID(uuid.toString());
    }
}
