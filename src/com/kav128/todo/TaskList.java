/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import java.sql.*;
import java.util.Hashtable;

public class TaskList
{
    private UserList userList;
    private Connection connection;

    private static final int loadPackageSize = 200;

    private int lastLoadedId;
    private Hashtable<Integer, Task> taskTable;
    private int totalDbSize = -1;

    public TaskList(Connection connection)
    {
        this.connection = connection;
        taskTable = new Hashtable<>();
        lastLoadedId = -1;

        userList = new UserList(connection);
    }

    public int loadMore()
    {
        int loaded = 0;
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT TOP (?) ID " +
                    "FROM Tasks " +
                    "WHERE ID > ?");
            ps.setInt(1, loadPackageSize);
            ps.setInt(2, lastLoadedId);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                int curID = rs.getInt("ID");
                taskTable.put(curID, new Task(curID, connection));
                loaded++;
                lastLoadedId = curID;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return loaded;
    }

    public int totalCount()
    {
        if (totalDbSize < 0)
        {
            try
            {
                Statement st = connection.createStatement();
                ResultSet rs =  st.executeQuery(
                        "SELECT COUNT(*) FROM Tasks");
                if (rs.next())
                    totalDbSize = rs.getInt(0);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return totalDbSize;
    }

    private Task load(int ID)
    {
        if (taskTable.contains(ID))
            return taskTable.get(ID);

        Task task = new Task(ID, connection);
        taskTable.put(ID, task);
        return task;
    }

    public int loadedCount()
    {
        return taskTable.size();
    }

    public Task getTask(int ID)
    {
        Task task = taskTable.get(ID);
        if (task == null)
        {
            try
            {
                PreparedStatement checkTaskStatement = connection.prepareStatement(
                        "SELECT COUNT(*) " +
                        "FROM Tasks " +
                        "WHERE ID = ?");
                checkTaskStatement.setInt(1, ID);
                ResultSet resultSet = checkTaskStatement.executeQuery();
                resultSet.next();
                int result = resultSet.getInt(1);
                if (result > 0)
                    task = load(ID);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return task;
    }

    public UserList getUserList()
    {
        return userList;
    }

    public void add(Task task)
    {
        User curUser = userList.getCurUser();
        if (curUser == null)
        {
            // Здесь могло бы быть исключение
            return;
        }
        task.setAuthor(curUser);
        task.setDbConnection(connection);
        task.writeToDb();
        taskTable.put(task.getDbID(), task);
    }

    public void remove(int dbID)
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Tasks WHERE ID = ?");
            ps.setInt(1, dbID);
            int deletedCount = ps.executeUpdate();
            if (deletedCount > 0)
                taskTable.remove(dbID);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
