/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import javenue.csv.Csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CSVDataSource extends DataSource
{
    private Csv.Reader reader;
    private Csv.Writer writer;
    private List<String> titleRow;
    private String[] fields;

    public CSVDataSource(String fileName, String[] fields)
    {
        super(fileName);
        this.fields = fields;
    }

    @Override
    protected void openResource()
    {
        if (opened)
            return;

        try
        {
            reader = new Csv.Reader(new FileReader(connectionString)).delimiter(';').ignoreComments(true);
            titleRow = reader.readLine();
            Collection<String> titleCollection = new HashSet<>(titleRow);
            opened = csvCorrect(titleCollection);
            if (!opened)
                closeResource();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void closeResource()
    {
        if (!opened)
            return;

        reader.close();
        opened = false;
    }

    @Override
    protected DataRecord getNextRecord()
    {
        DataRecord record = new DataRecord();
        List<String> curRow = reader.readLine();
        if (curRow == null)
            return null;

        for (int i = 0; i < titleRow.size(); i++)
            record.setValue(titleRow.get(i), curRow.get(i));

        return record;
    }

    @Override
    protected void beginWrite()
    {
        writer = new Csv.Writer(connectionString);
        csvInit();
    }

    @Override
    protected void endWrite()
    {
        writer.close();
    }

    @Override
    protected void writeRecord(DataRecord record)
    {
        for (String fieldName : fields)
            writer.value(record.getValue(fieldName));
        writer.newLine();
    }

    private void csvInit()
    {
        for (String fieldName : fields)
            writer.value(fieldName);
        writer.newLine();
    }

    private boolean csvCorrect(Collection<String> titleCollection)
    {
        for (String fieldName : fields)
            if (!titleCollection.contains(fieldName))
                return false;
        return true;
    }
}
