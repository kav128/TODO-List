/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

class BufferRecord
{
    enum RecordState
    {
        NoChanges, Added, Removed, Edited
    }

    private final DataRecord data;
    private final DataRecord newData;
    private RecordState state;

    BufferRecord()
    {
        state = RecordState.NoChanges;
        data = new DataRecord();
        newData = new DataRecord();
    }

    void initField(String field, String value)
    {
        if (state == RecordState.NoChanges && !data.hasField(field))
            data.setValue(field, value);
    }

    void changeValue(String field, String newValue)
    {
        if (!data.hasField(field) && state != RecordState.Added)
            return;

        if (!newValue.equals(data.getValue(field)))
        {
            newData.setValue(field, newValue);
            if (state == RecordState.NoChanges)
                state = RecordState.Edited;
        }
    }

    public String getData(String field)
    {
        switch (state)
        {
            case NoChanges:
            case Added:
                return data.getValue(field);
            case Edited:
                return newData.hasField(field) ? newData.getValue(field) : data.getValue(field);
            default:
                return null;
        }
    }

    public RecordState getState()
    {
        return state;
    }

    public void setAdded()
    {
        state = RecordState.Added;
    }

    public void setRemoved()
    {
        state = RecordState.Removed;
    }

    DataRecord toDataRecord()
    {
        if (state == RecordState.Removed)
            return null;

        DataRecord record = new DataRecord();
        for (String field : data.getFieldArray())
            record.setValue(field, data.getValue(field));
        for (String field : newData.getFieldArray())
            record.setValue(field, newData.getValue(field));
        return record;
    }

    public void applyChanges()
    {
        for (String editedField : newData.getFieldArray())
            data.setValue(editedField, newData.getValue(editedField));
        state = RecordState.NoChanges;
    }
}
