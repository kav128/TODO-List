/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.todo.data.DatabaseManager;

public class ToDoApp implements AutoCloseable
{
    private DatabaseManager dbManager;

    private UserController userController;
    private TaskController taskController;
    private NotificationController notificationController;

    public ToDoApp() throws Exception
    {
        dbManager = new DatabaseManager();

        userController = new UserController(dbManager);
        userController.setOnLogin((Session session) -> {
            taskController = new TaskController(this, session);
            notificationController = new NotificationController(this, session);
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
