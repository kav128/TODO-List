/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */


package com.kav128.todo.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO
{
    protected final Connection dbConnection;

    AbstractDAO(Connection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    protected DataRecord resultSetToDataRecord(ResultSet resultSet) throws SQLException
    {
        DataRecord record = new DataRecord();
        String[] fields = getFields();
        for (String field : fields)
            record.setString(field, resultSet.getString(field));
        return record;
    }

    protected DataRecord getSingleRecord(PreparedStatement statement) throws SQLException
    {
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        DataRecord record = resultSetToDataRecord(resultSet);
        resultSet.close();
        return record;
    }

    protected DataRecord[] getRecordsArray(PreparedStatement statement) throws SQLException
    {
        ResultSet resultSet = statement.executeQuery();
        DataRecord[] records = getDataRecords(resultSet);
        resultSet.close();
        return records;
    }

    protected DataRecord[] getDataRecords(ResultSet resultSet) throws SQLException
    {
        List<DataRecord> records = new ArrayList<>();
        while (resultSet.next())
            records.add(resultSetToDataRecord(resultSet));
        DataRecord[] recordsArray = new DataRecord[records.size()];
        return records.toArray(recordsArray);
    }

    protected int getSingleIntValue(PreparedStatement statement) throws SQLException
    {
        ResultSet resultSet = statement.executeQuery();
        int value;
        if (resultSet.next())
            value = resultSet.getInt(1);
        else
            value = -1;
        resultSet.close();
        return value;
    }

    protected abstract String[] getFields();
}
