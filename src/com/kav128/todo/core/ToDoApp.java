/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

import com.kav128.todo.data.DBManagerFactory;
import com.kav128.todo.data.DatabaseManager;

public class ToDoApp implements AutoCloseable
{
    private DatabaseManager dbManager;

    private CUserController userController;
    private TaskController taskController;
    private NotificationController notificationController;

    public ToDoApp() throws Exception
    {
        dbManager = DBManagerFactory.getDBManager();

        assert dbManager != null;
        userController = new CUserController(dbManager);
        userController.setOnLogin((Session session) -> {
            taskController = new CTaskController(this, session);
            notificationController = new CNotificationController(this, session);
        });
        userController.setOnLogout((User user) -> {
            notificationController = null;
            taskController = null;
        });
    }

    public UserController getUserController()
    {
        return userController;
    }

    public CUserController getUserControllerClass()
    {
        return userController;
    }

    public TaskController getTaskController()
    {
        return taskController;
    }

    public NotificationController getNotificationController()
    {
        return notificationController;
    }

    DatabaseManager getDbManager()
    {
        return dbManager;
    }

    @Override
    public void close() throws Exception
    {
        userController.close();
        dbManager.close();
    }
}
