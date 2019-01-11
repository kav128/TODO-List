/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.data;

public class DBManagerFactory
{
    public static DatabaseManager getDBManager()
    {
        try
        {
            return new CDatabaseManager();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
