/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import java.sql.*;

public class User
{
    private int dbID;

    @Override
    public String toString()
    {
        return String.format("User{dbID=%d, name='%s'}", dbID, name);
    }

    private String name;

    public User(int ID, Connection dbConnection)
    {
        dbID = ID;
        try
        {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Name FROM Users WHERE ID = " + String.valueOf(ID));
            resultSet.next();
            name = resultSet.getString("Name");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    User(int dbID, String name)
    {
        this.dbID = dbID;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    int getID()
    {
        return dbID;
    }
}
