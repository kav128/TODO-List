/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class DataRecord
{
    private Map<String, String> data;

    DataRecord()
    {
        data = new HashMap<>();
    }

    void setValue(String field, String value)
    {
        data.put(field, value);
    }

    String getValue(String field)
    {
        return data.get(field);
    }

    boolean hasField(String field)
    {
        return data.containsKey(field);
    }

    String[] getFieldArray()
    {
        Set<String> strings = data.keySet();
        String[] arr = new String[strings.size()];
        strings.toArray(arr);
        return arr;
    }
}
