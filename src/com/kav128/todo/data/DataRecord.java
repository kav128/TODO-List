/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataRecord
{
    private final Map<String, String> data;

    DataRecord()
    {
        data = new HashMap<>();
    }

    public void setString(String field, String value)
    {
        data.put(field, value);
    }

    public void setInt(String field, int value)
    {
        data.put(field, Integer.toString(value));
    }

    public void setDate(String field, LocalDate value)
    {
        data.put(field, value.toString());
    }

    public void setBoolean(String field, Boolean value)
    {
        data.put(field, value.toString());
    }

    public String getString(String field)
    {
        return data.get(field);
    }

    public int getInt(String field)
    {
        return Integer.parseInt(data.get(field));
    }

    public LocalDate getDate(String field)
    {
        return LocalDate.parse(data.get(field));
    }

    public boolean getBoolean(String field)
    {
        return Boolean.parseBoolean(field);
    }

    public boolean hasField(String field)
    {
        return data.containsKey(field);
    }

    public String[] getFieldArray()
    {
        Set<String> strings = data.keySet();
        String[] arr = new String[strings.size()];
        strings.toArray(arr);
        return arr;
    }
}
