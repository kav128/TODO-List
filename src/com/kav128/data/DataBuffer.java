/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.util.ArrayList;
import java.util.List;

class DataBuffer
{
    private List<BufferRecord> records;
    private List<BufferRecord> removedRecords;

    DataBuffer()
    {
        records = new ArrayList<>();
        removedRecords = new ArrayList<>();
    }

    void loadRecord(DataRecord record)
    {
        BufferRecord bufferRecord = new BufferRecord();
        for (String field : record.getFieldArray())
            bufferRecord.initField(field, record.getValue(field));
        records.add(bufferRecord);
    }

    int recordsCount()
    {
        return records.size();
    }

    DataRecord getRecord(int index)
    {
        if (index >= records.size())
            return null;
        return records.get(index).toDataRecord();
    }

    void removeRecord(int index)
    {
        BufferRecord record = records.get(index);
        removedRecords.add(record);
        records.remove(index);
        record.setRemoved();
    }

    void commit()
    {
        for(BufferRecord record : records)
            record.applyChanges();
        removedRecords.clear();
    }

    boolean hasChanges()
    {
        if (removedRecords.size() > 0)
            return true;
        for (BufferRecord record : records)
            if (record.getState() != BufferRecord.RecordState.NoChanges)
                return true;
        return false;
    }

    private int findRecordIndex(String uuid)
    {
        for (int i = 0; i < records.size(); i++)
            if (records.get(i).getData("uuid").equals(uuid))
                return i;
        return -1;
    }

    private BufferRecord findRecord(String uuid)
    {
        int index = findRecordIndex(uuid);
        if (index < 0)
            return null;
        return records.get(index);
    }

    void modify(DataRecord newData)
    {
        BufferRecord record = findRecord(newData.getValue("uuid"));
        if (record != null)
            for (String field : newData.getFieldArray())
                    record.changeValue(field, newData.getValue(field));
    }

    void create(DataRecord data)
    {
        BufferRecord record = new BufferRecord();
        record.setAdded();
        for (String field : data.getFieldArray())
            record.changeValue(field, data.getValue(field));
        records.add(record);
    }

    void removeByUUID(String uuid)
    {
        int index = findRecordIndex(uuid);
        removeRecord(index);
    }
}
