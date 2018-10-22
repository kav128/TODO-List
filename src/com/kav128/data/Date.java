/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date implements Comparable<Date>
{
    private java.util.Date date;
    private final Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat;

    public Date(java.util.Date date)
    {
        this.date = date;
        calendar.setTime(date);
    }

    public Date(String dateString)
    {
        try
        {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            this.date = dateFormat.parse(dateString);
            calendar.setTime(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public int getDay()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public int getMonth()
    {
        return calendar.get(Calendar.MONTH);
    }
    public int getYear()
    {
        return calendar.get(Calendar.YEAR);
    }

    @Override
    public String toString()
    {
        return dateFormat.format(date);
    }

    @Override
    public int compareTo(Date o)
    {
        return date.compareTo(o.date);
    }
}
