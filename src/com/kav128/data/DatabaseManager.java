/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager implements AutoCloseable
{
    private final String host = "localhost\\SQLEXPRESS";
    private final String dbName = "ToDo";
    private final String security = "integratedSecurity=true";
    private final String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;%s", host, dbName, security);

    private Connection connection;

    public DatabaseManager() throws Exception
    {
        connection = DriverManager.getConnection(connectionString);
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
}