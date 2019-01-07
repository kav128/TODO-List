/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationsDAO extends AbstractDAO
{

    NotificationsDAO(Connection dbConnection) throws SQLException
    {
        super(dbConnection);
    }

    @Override
    protected String[] getFields()
    {
        return new String[] {"id", "senderUser", "taskId", "seen", "accepted"};
    }

    public DataRecord[] getNotificationsByUser(int userId)
    {
        try(PreparedStatement getNotificationByUserStatement = dbConnection.prepareStatement(
            "SELECT id, taskId, senderUser, seen, accepted\n" +
                "FROM Notifications\n" +
                "WHERE assignedToUser = ?"))
        {
            getNotificationByUserStatement.setInt(1, userId);
            return this.getRecordsArray(getNotificationByUserStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getNotificationCount(int userId)
    {
        try(PreparedStatement getNotificationCountStatement = dbConnection.prepareStatement(
            "SELECT COUNT(*)\n" +
                "FROM Notifications\n" +
                "WHERE assignedToUser = ?"))
        {
            getNotificationCountStatement.setInt(1, userId);
            return this.getSingleIntValue(getNotificationCountStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public DataRecord[] getUnseenNotificationsByUser(int userId)
    {
        try(PreparedStatement getUnseenNotificationByUserStatement = dbConnection.prepareStatement(
            "SELECT id, taskId, senderUser, seen, accepted\n" +
                "FROM Notifications\n" +
                "WHERE assignedToUser = ? AND seen = 0"))
        {
            getUnseenNotificationByUserStatement.setInt(1, userId);
            return this.getRecordsArray(getUnseenNotificationByUserStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getUnseenNotificationCount(int userId)
    {
        try(PreparedStatement getUnseenNotificationCountStatement = dbConnection.prepareStatement(
            "SELECT COUNT(*)\n" +
                "FROM Notifications\n" +
                "WHERE assignedToUser = ? AND seen = 0"))
        {
            getUnseenNotificationCountStatement.setInt(1, userId);
            return this.getSingleIntValue(getUnseenNotificationCountStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setSeen(int notificationID)
    {
        try(PreparedStatement setSeenStatement = dbConnection.prepareStatement(
            "UPDATE Notifications\n" +
                "SET seen = 1\n" +
                "WHERE id = ?"))
        {
            setSeenStatement.setInt(1, notificationID);
            int result = setSeenStatement.executeUpdate();
            return result > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int addNotification(int senderId, int assignedToId, int taskId)
    {
        try (PreparedStatement addNotificationStatement = dbConnection.prepareStatement(
            "INSERT INTO Notifications (assignedToUser, senderUser, taskId, seen, accepted) VALUES\n" +
                "(?, ?, ?, 0, 0)" +
                "SELECT @@IDENTITY"))
        {
            addNotificationStatement.setInt(1, assignedToId);
            addNotificationStatement.setInt(2, senderId);
            addNotificationStatement.setInt(3, taskId);
            return getSingleIntValue(addNotificationStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean acceptNotification(int id)
    {
        try (PreparedStatement acceptNotificationStatement = dbConnection.prepareStatement("" +
                "UPDATE Notifications\n" +
                "SET accepted = 1\n" +
                "WHERE id = ?"))
        {
            acceptNotificationStatement.setInt(1, id);
            int result = acceptNotificationStatement.executeUpdate();
            return result > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
