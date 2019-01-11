/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TasksDAO extends AbstractDAO
{
    TasksDAO(Connection dbConnection) throws Exception
    {
        super(dbConnection);
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

    public DataRecord getTaskByID(int id)
    {
        try(PreparedStatement getTaskByIdStatement = dbConnection.prepareStatement(
                "SELECT id, title, description, deadline, completed\n" +
                    "FROM Tasks\n" +
                    "WHERE id = ?"))
        {
            getTaskByIdStatement.setInt(1, id);
            return getSingleRecord(getTaskByIdStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public DataRecord[] getTasksByUser(int id)
    {
        try(PreparedStatement getTasksByUserStatement = dbConnection.prepareStatement(
                "SELECT taskId, userID, role, id, title, description, deadline, completed\n" +
                    "FROM TaskUser\n" +
                    "INNER JOIN Tasks on TaskUser.taskId = Tasks.id\n" +
                    "WHERE userID = ?"))
        {
            getTasksByUserStatement.setInt(1, id);
            return getRecordsArray(getTasksByUserStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public DataRecord[] getTasksByCompleted(int userId, boolean completed)
    {
        try(PreparedStatement getTasksByCompletedStatement = dbConnection.prepareStatement(
                "SELECT taskId, userID, role, id, title, description, deadline, completed\n" +
                    "FROM TaskUser\n" +
                    "INNER JOIN Tasks on TaskUser.taskId = Tasks.id\n" +
                    "WHERE completed = ? AND userID = ?"))
        {
            getTasksByCompletedStatement.setBoolean(1, completed);
            getTasksByCompletedStatement.setInt(2, userId);
            return getRecordsArray(getTasksByCompletedStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getTaskAuthor(int taskId)
    {
        try(PreparedStatement getTaskAuthorStatement = dbConnection.prepareStatement(
                "SELECT userID\n" +
                    "FROM TaskUser\n" +
                    "WHERE taskId = ? AND role = 0"))
        {
            getTaskAuthorStatement.setInt(1, taskId);
            return getSingleIntValue(getTaskAuthorStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int insertTask(String title, String description, LocalDate deadline, int authorId)
    {
        try(PreparedStatement insertNewTaskStatement = dbConnection.prepareStatement(
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
                    "COMMIT"))
        {
            insertNewTaskStatement.setString(1, title);
            insertNewTaskStatement.setString(2, description);
            insertNewTaskStatement.setObject(3, deadline);
            insertNewTaskStatement.setInt(4, authorId);
            return getSingleIntValue(insertNewTaskStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
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

    public void updateTaskTitle(int id, String newTitle)
    {
        try(PreparedStatement updateTaskTitleStatement = prepareUpdateTaskStatement(dbConnection, "title"))
        {
            updateTaskWithStringArg(updateTaskTitleStatement, id, newTitle);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateTaskDescription(int id, String newTitle)
    {
        try(PreparedStatement updateTaskDescriptionStatement = prepareUpdateTaskStatement(dbConnection, "description"))
        {
            updateTaskWithStringArg(updateTaskDescriptionStatement, id, newTitle);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateTaskDeadline(int id, LocalDate newTitle)
    {
        try(PreparedStatement updateTaskDeadlineStatement = prepareUpdateTaskStatement(dbConnection, "deadline"))
        {
            updateTaskWithObjectArg(updateTaskDeadlineStatement, id, newTitle);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateTaskCompleted(int id, boolean newTitle)
    {
        try(PreparedStatement updateTaskCompletedStatement = prepareUpdateTaskStatement(dbConnection, "completed"))
        {
            updateTaskWithBoolArg(updateTaskCompletedStatement, id, newTitle);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void assignUser(int taskId, int userId, int role)
    {
        try(PreparedStatement assignUserStatement = dbConnection.prepareStatement(
            "INSERT INTO TaskUser(taskId, userID, role) VALUES\n" +
                "(?, ?, ?)"))
        {
            assignUserStatement.setInt(1, taskId);
            assignUserStatement.setInt(2, userId);
            assignUserStatement.setInt(3, role);
            assignUserStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Integer> getAssignedUserIDs(int taskId)
    {
        try(PreparedStatement getAssignedUserIDsStatement = dbConnection.prepareStatement(
                "SELECT userID\n" +
                    "FROM TaskUser\n" +
                    "WHERE role > 0 AND taskId = ?"))
        {
            getAssignedUserIDsStatement.setInt(1, taskId);
            ResultSet resultSet = getAssignedUserIDsStatement.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (resultSet.next())
                ids.add(resultSet.getInt(1));
            return ids;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getTaskGroupId(String taskGroupName)
    {
        try(PreparedStatement getTaskGroupIdStatement = dbConnection.prepareStatement(
            "SELECT id\n" +
                "FROM TagGroups\n" +
                "WHERE name = ?"))
        {
            getTaskGroupIdStatement.setString(1, taskGroupName);
            return getSingleIntValue(getTaskGroupIdStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int getTaskTag(int taskId, String tagName)
    {
        try(PreparedStatement getTaskTagStatement = dbConnection.prepareStatement(
            "SELECT tag\n" +
                "FROM TaskTag\n" +
                "INNER JOIN TagGroups on TaskTag.tagGroupId = TagGroups.id\n" +
                "WHERE name = ? AND taskId = ?"))
        {
            getTaskTagStatement.setString(1, tagName);
            getTaskTagStatement.setInt(2, taskId);
            return getSingleIntValue(getTaskTagStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setTaskTag(int taskId, String tagGroupName, int tag)
    {
        try(PreparedStatement setTaskTagStatement = dbConnection.prepareStatement(
            "DECLARE @@TGID INT\n" +
                "SET @@TGID = (SELECT id FROM TagGroups WHERE name = ?)" +
                "INSERT INTO TaskTag(taskId, tagGroupId, tag) VALUES\n" +
                "(?, @@TGID, ?)"))
        {
            setTaskTagStatement.setString(1, tagGroupName);
            setTaskTagStatement.setInt(2, taskId);
            setTaskTagStatement.setInt(3, tag);
            int inserted = setTaskTagStatement.executeUpdate();
            return inserted > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTaskTag(int taskId, String tagGroupName, int tag)
    {
        try(PreparedStatement updateTaskTagStatement = dbConnection.prepareStatement(
            "DECLARE @@TGID INT\n" +
                "SET @@TGID = (SELECT id FROM TagGroups WHERE name = ?)" +
                "UPDATE TaskTag\n" +
                "SET tag = ?\n" +
                "WHERE taskId = ? AND tagGroupId = @@TGID"))
        {
            updateTaskTagStatement.setString(1, tagGroupName);
            updateTaskTagStatement.setInt(2, tag);
            updateTaskTagStatement.setInt(3, taskId);
            int updated = updateTaskTagStatement.executeUpdate();
            return updated > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeTaskTag(int taskId, String tagGroupName)
    {
        try(PreparedStatement removeTaskTagStatement = dbConnection.prepareStatement(
            "DECLARE @@TGID INT\n" +
                "SET @@TGID = (SELECT id FROM TagGroups WHERE name = ?)" +
                "DELETE FROM TaskTag\n" +
                "WHERE taskId = ? AND tagGroupId = @@TGID"))
        {
            removeTaskTagStatement.setString(1, tagGroupName);
            removeTaskTagStatement.setInt(2, taskId);
            int removed = removeTaskTagStatement.executeUpdate();
            return removed > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteTask(int id)
    {
        try(PreparedStatement deleteTaskStatement = dbConnection.prepareStatement(
            "DELETE\n" +
                "FROM Tasks\n" +
                "WHERE id = ?"))
        {
            deleteTaskStatement.setInt(1, id);
            deleteTaskStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
