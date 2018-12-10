/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class TasksSingleDAO extends AbstractDAO implements AutoCloseable
{
    private final PreparedStatement getTasksStatement;
    private final PreparedStatement getTaskByIdStatement;
    private final PreparedStatement getTasksByCompletedStatement;
    private final PreparedStatement insertNewTask;
    private final PreparedStatement updateTaskTitleStatement;
    private final PreparedStatement updateTaskDescriptionStatement;
    private final PreparedStatement updateTaskDeadlineStatement;
    private final PreparedStatement updateTaskCompletedStatement;
    private final PreparedStatement deleteTaskStatement;

    TasksSingleDAO(Connection dbConnection) throws Exception
    {
        super(dbConnection);
        getTasksStatement = dbConnection.prepareStatement(
                "SELECT id, title, description, deadline, completed\n" +
                        "FROM Tasks");
        getTaskByIdStatement = dbConnection.prepareStatement(
                "SELECT id, title, description, deadline, completed\n" +
                        "FROM Tasks\n" +
                        "WHERE id = ?");
        getTasksByCompletedStatement = dbConnection.prepareStatement(
                "SELECT id, title, description, deadline, completed\n" +
                        "FROM Tasks\n" +
                        "WHERE completed = ?");
        insertNewTask = dbConnection.prepareStatement(
                "BEGIN TRANSACTION\n" +
                        "INSERT INTO Tasks(title, description, deadline, completed) VALUES\n" +
                        "(?, ?, ?, 0)\n" +
                        "SELECT @@IDENTITY AS taskID\n" +
                        "COMMIT");
        deleteTaskStatement = dbConnection.prepareStatement(
                "DELETE\n" +
                        "FROM Tasks\n" +
                        "WHERE id = ?");
        updateTaskTitleStatement = prepareUpdateTaskStatement(dbConnection, "title");
        updateTaskDescriptionStatement = prepareUpdateTaskStatement(dbConnection, "description");
        updateTaskDeadlineStatement = prepareUpdateTaskStatement(dbConnection, "deadline");
        updateTaskCompletedStatement = prepareUpdateTaskStatement(dbConnection, "completed");
    }

    @Override
    protected String[] getFields()
    {
        return new String[] {"id", "title", "description", "deadline", "completed"};
    }

    private PreparedStatement prepareUpdateTaskStatement(Connection dbConnection, String field) throws Exception
    {
        String sql = String.format(
                "UPDATE Tasks\n" +
                "SET %s = ?\n" +
                "WHERE id = ?", field);
        return dbConnection.prepareStatement(sql);
    }

    public DataRecord getTaskByID(int id) throws Exception
    {
        getTaskByIdStatement.setInt(1, id);
        return getSingleRecord(getTaskByIdStatement);
    }

    public DataRecord[] getTasks() throws Exception
    {
        return getRecordsArray(getTasksStatement);
    }

    public DataRecord[] getTasksByCompleted(boolean completed) throws Exception
    {
        getTasksByCompletedStatement.setBoolean(1, completed);
        return getRecordsArray(getTasksByCompletedStatement);
    }

    public int insertTask(String title, String description, LocalDate deadline) throws Exception
    {
        insertNewTask.setString(1, title);
        insertNewTask.setString(2, description);
        insertNewTask.setObject(3, deadline);
        ResultSet resultSet = insertNewTask.executeQuery();
        resultSet.next();
        int id = resultSet.getInt(1);
        resultSet.close();
        return id;
    }

    public void deleteTask(int id) throws Exception
    {
        deleteTaskStatement.setInt(1, id);
        deleteTaskStatement.executeUpdate();
    }

    private void updateTaskWithStringArg(PreparedStatement statement, int id, String newValue) throws Exception
    {
        statement.setString(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    private void updateTaskWithObjectArg(PreparedStatement statement, int id, Object newValue) throws Exception
    {
        statement.setObject(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    private void updateTaskWithBoolArg(PreparedStatement statement, int id, boolean newValue) throws Exception
    {
        statement.setBoolean(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void updateTaskTitle(int id, String newTitle) throws Exception
    {
        updateTaskWithStringArg(updateTaskTitleStatement, id, newTitle);
    }

    public void updateTaskDescription(int id, String newTitle) throws Exception
    {
        updateTaskWithStringArg(updateTaskDescriptionStatement, id, newTitle);
    }

    public void updateTaskDeadline(int id, LocalDate newTitle) throws Exception
    {
        updateTaskWithObjectArg(updateTaskDeadlineStatement, id, newTitle);
    }

    public void updateTaskCompleted(int id, boolean newTitle) throws Exception
    {
        updateTaskWithBoolArg(updateTaskCompletedStatement, id, newTitle);
    }

    @Override
    public void close() throws Exception
    {
        updateTaskCompletedStatement.close();
        updateTaskDeadlineStatement.close();
        updateTaskDescriptionStatement.close();
        updateTaskTitleStatement.close();
        insertNewTask.close();
        getTasksByCompletedStatement.close();
        getTaskByIdStatement.close();
    }
}
