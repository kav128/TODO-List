/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

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

    public void setValue(String field, String value)
    {
        data.put(field, value);
    }

    public void setValue(String field, int value)
    {
        data.put(field, Integer.toString(value));
    }

    public String getValue(String field)
    {
        return data.get(field);
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
