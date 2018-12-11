/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.data.DatabaseManager;
import com.kav128.data.TasksDAO;
import com.kav128.data.UsersDAO;

import java.sql.SQLException;
import java.time.LocalDate;

public class ToDoApp implements AutoCloseable
{
    private DatabaseManager dbManager;
    private TasksDAO tasksDAO;
    private UsersDAO usersDAO;

    private Session curSession;
    private TaskList taskList;

    public ToDoApp() throws Exception
    {
        dbManager = new DatabaseManager();
        tasksDAO = dbManager.getTasksDAO();
        usersDAO = dbManager.getUsersDAO();
    }

    public boolean register(String username, String password)
    {
        try
        {
            boolean regSuccess = usersDAO.insertUser(username, password);
            if (!regSuccess)
                return false;
            return login(username, password);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String password)
    {
        try
        {
            int uid = usersDAO.authUser(username, password);
            if (uid >= 0)
            {
                curSession = new Session(new User(uid, username));
                taskList = new TaskList(tasksDAO, curSession);
                return true;
            }
            else
                return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void logout()
    {
        if (curSession != null)
            curSession.close();
        taskList = null;
        curSession = null;
    }

    @Override
    public void close() throws Exception
    {
        if (curSession != null)
            curSession.close();
        usersDAO.close();
        tasksDAO.close();
        dbManager.close();
    }

    public TaskList getTaskList()
    {
        return taskList;
    }

    public Task createTask(String title, String description, LocalDate deadline)
    {
        if (taskList != null)
            return taskList.createTask(title, description, deadline, curSession.getUser().getId());
        else
            return null;
    }

    public void remove(int index)
    {
        taskList.remove(index);
    }

    public void load()
    {
        taskList.load();
    }

    public UsersDAO getUsersDAO()
    {
        return usersDAO;
    }

    public boolean isAuthorized()
    {
        return curSession != null && curSession.isOpened();
    }
}
