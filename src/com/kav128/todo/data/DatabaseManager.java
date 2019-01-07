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
public class DatabaseManager implements AutoCloseable
{
    private final String host = "localhost\\SQLEXPRESS";
    private final String dbName = "ToDo";
    private final String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s", host, dbName);

    protected Connection connection;

    public DatabaseManager() throws Exception
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

    public UsersDAO getUsersDAO() throws Exception
    {
        return new UsersDAO(connection);
    }

    public TasksDAO getTasksDAO() throws Exception
    {
        return new TasksDAO(connection);
    }

    public NotificationsDAO getNotificationsDAO() throws Exception
    {
        return new NotificationsDAO(connection);
    }

    public void setAutoCommit(boolean autoCommit) throws Exception
    {
        connection.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws Exception
    {
        return connection.getAutoCommit();
    }

    public void commit() throws Exception
    {
        connection.commit();
    }

    public void rollback() throws Exception
    {
        connection.rollback();
    }

    public Connection getConnection()
    {
        return connection;
    }
}