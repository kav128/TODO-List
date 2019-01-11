/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

//TODO UnitOfWork
class CDatabaseManager implements DatabaseManager
{
    private final String host = "localhost\\SQLEXPRESS";
    private final String dbName = "ToDo";
    private final String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s", host, dbName);

    private Connection connection;

    CDatabaseManager() throws Exception
    {
        Properties props = new Properties();
        props.setProperty("user", "todoapp");
        props.setProperty("password", "todo");
        connection = DriverManager.getConnection(connectionString, props);
        System.out.println("Connected");
    }

    @Override
    public void close() throws Exception
    {
        connection.close();
        System.out.println("Closed");
    }

    @Override
    public UsersDAO getUsersDAO() throws Exception
    {
        return new UsersDAO(connection);
    }

    @Override
    public TasksDAO getTasksDAO() throws Exception
    {
        return new TasksDAO(connection);
    }

    @Override
    public NotificationsDAO getNotificationsDAO() throws Exception
    {
        return new NotificationsDAO(connection);
    }

}