/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TasksDAO extends AbstractDAO implements AutoCloseable
{
    private final PreparedStatement getTaskByIdStatement;
    private final PreparedStatement getTasksByUserStatement;
    private final PreparedStatement getTasksByCompletedStatement;
    private final PreparedStatement insertNewTask;
    private final PreparedStatement updateTaskTitleStatement;
    private final PreparedStatement updateTaskDescriptionStatement;
    private final PreparedStatement updateTaskDeadlineStatement;
    private final PreparedStatement updateTaskCompletedStatement;
    private final PreparedStatement assignUserStatement;
    private final PreparedStatement deleteTaskStatement;

    TasksDAO(Connection dbConnection) throws SQLException
    {
        super(dbConnection);
        getTaskByIdStatement = dbConnection.prepareStatement(
                "SELECT id, title, description, deadline, completed\n" +
                        "FROM Tasks\n" +
                        "WHERE id = ?");
        getTasksByUserStatement = dbConnection.prepareStatement(
                "SELECT taskId, userID, role, id, title, description, deadline, completed\n" +
                        "FROM TaskUser\n" +
                        "INNER JOIN Tasks on TaskUser.taskId = Tasks.id\n" +
                        "WHERE userID = ?");
        getTasksByCompletedStatement = dbConnection.prepareStatement(
                "SELECT taskId, userID, role, id, title, description, deadline, completed\n" +
                        "FROM TaskUser\n" +
                        "INNER JOIN Tasks on TaskUser.taskId = Tasks.id\n" +
                        "WHERE completed = ? AND userID = ?");
        insertNewTask = dbConnection.prepareStatement(
                "BEGIN TRANSACTION\n" +
                        "INSERT INTO Tasks(title, description, deadline, completed) VALUES\n" +
                            "(?, ?, ?, 0)\n" +
                        "IF (@@ERROR <> 0)\n" +
                            "ROLLBACK\n" +
                        "DECLARE @@ID INT\n" +
                        "SET @@ID = @@IDENTITY\n" +
                        "INSERT INTO TaskUser(taskId, userID, role) VALUES\n" +
                            "(@@IDENTITY, ?, 0)\n" +
                        "IF (@@ERROR <> 0)\n" +
                            "ROLLBACK\n" +
                        "SELECT @@ID AS taskID\n" +
                    "COMMIT");
        deleteTaskStatement = dbConnection.prepareStatement(
                "DELETE\n" +
                        "FROM Tasks\n" +
                        "WHERE id = ?");
        assignUserStatement = dbConnection.prepareStatement(
                "INSERT INTO TaskUser(taskId, userID, role) VALUES\n" +
                "(?, ?, ?)");
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

    private PreparedStatement prepareUpdateTaskStatement(Connection dbConnection, String field) throws SQLException
    {
        String sql = String.format(
                "UPDATE Tasks\n" +
                "SET %s = ?\n" +
                "WHERE id = ?", field);
        return dbConnection.prepareStatement(sql);
    }

    public DataRecord getTaskByID(int id) throws SQLException
    {
        getTaskByIdStatement.setInt(1, id);
        return getSingleRecord(getTaskByIdStatement);
    }

    public DataRecord[] getTasksByUser(int id) throws SQLException
    {
        getTasksByUserStatement.setInt(1, id);
        return getRecordsArray(getTasksByUserStatement);
    }

    public DataRecord[] getTasksByCompleted(int id, boolean completed) throws SQLException
    {
        getTasksByCompletedStatement.setBoolean(1, completed);
        getTasksByCompletedStatement.setInt(2, id);
        return getRecordsArray(getTasksByCompletedStatement);
    }

    public int insertTask(String title, String description, LocalDate deadline, int authorId) throws SQLException
    {
        insertNewTask.setString(1, title);
        insertNewTask.setString(2, description);
        insertNewTask.setObject(3, deadline);
        insertNewTask.setInt(4,authorId);
        ResultSet resultSet = insertNewTask.executeQuery();
        resultSet.next();
        int id = resultSet.getInt(1);
        resultSet.close();
        return id;
    }

    private void updateTaskWithStringArg(PreparedStatement statement, int id, String newValue) throws SQLException
    {
        statement.setString(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    private void updateTaskWithObjectArg(PreparedStatement statement, int id, Object newValue) throws SQLException
    {
        statement.setObject(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    private void updateTaskWithBoolArg(PreparedStatement statement, int id, boolean newValue) throws SQLException
    {
        statement.setBoolean(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void updateTaskTitle(int id, String newTitle) throws SQLException
    {
        updateTaskWithStringArg(updateTaskTitleStatement, id, newTitle);
    }

    public void updateTaskDescription(int id, String newTitle) throws SQLException
    {
        updateTaskWithStringArg(updateTaskDescriptionStatement, id, newTitle);
    }

    public void updateTaskDeadline(int id, LocalDate newTitle) throws SQLException
    {
        updateTaskWithObjectArg(updateTaskDeadlineStatement, id, newTitle);
    }

    public void updateTaskCompleted(int id, boolean newTitle) throws SQLException
    {
        updateTaskWithBoolArg(updateTaskCompletedStatement, id, newTitle);
    }

    public void assignUser(int taskId, int userId, int role) throws SQLException
    {
        assignUserStatement.setInt(1, taskId);
        assignUserStatement.setInt(2, userId);
        assignUserStatement.setInt(3, role);
        assignUserStatement.executeUpdate();
    }

    public void deleteTask(int id) throws Exception
    {
        deleteTaskStatement.setInt(1, id);
        deleteTaskStatement.executeUpdate();
    }

    @Override
    public void close() throws Exception
    {
        assignUserStatement.close();
        updateTaskCompletedStatement.close();
        updateTaskDeadlineStatement.close();
        updateTaskDescriptionStatement.close();
        updateTaskTitleStatement.close();
        insertNewTask.close();
        getTasksByCompletedStatement.close();
        getTasksByUserStatement.close();
        getTaskByIdStatement.close();
    }
}
