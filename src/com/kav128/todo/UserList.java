/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserList
{
    private Connection connection;
    private Map<Integer, User> userTable;

    private PreparedStatement registerStatement;
    private PreparedStatement getNewUserIdStatement;
    private PreparedStatement deleteUserStatement;
    private PreparedStatement getUserCountStatement;

    private User curUser;

    UserList(Connection connection)
    {
        this.connection = connection;
        userTable = new HashMap<>();

        prepareStatements();
    }

    private void prepareStatements()
    {
        try
        {
            registerStatement = connection.prepareStatement(
                    "INSERT INTO Users(Name, Password) VALUES (?, ?) ");
            getNewUserIdStatement = connection.prepareStatement(
                    "SELECT IDENT_CURRENT('Users')");
            deleteUserStatement = connection.prepareStatement(
                    "DELETE FROM Users WHERE ID = ?");
            getUserCountStatement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM Users");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public User getUser(int ID)
    {
        User user = userTable.get(ID);
        if (user == null)
        {
            user = new User(ID, connection);
            userTable.put(ID, user);
        }
        return user;
    }

    public User register(String username, String password)
    {
        User newUser = null;
        try
        {
            registerStatement.setString(1, username);
            registerStatement.setString(2, password);
            int registerResult = registerStatement.executeUpdate();
            if (registerResult == 0)
                return null;
            ResultSet resultSet = getNewUserIdStatement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            newUser = new User(id, username);
            userTable.put(id, newUser);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return newUser;
    }

    public boolean deleteUser(User user)
    {
        boolean success = false;
        try
        {
            deleteUserStatement.setInt(1, user.getID());
            int deletedUsers = deleteUserStatement.executeUpdate();
            if (deletedUsers > 0)
            {
                userTable.remove(user.getID());
                success = true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return success;
    }

    public int count()
    {
        int userCount = 0;
        try
        {
            ResultSet resultSet = getUserCountStatement.executeQuery();
            userCount = resultSet.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return userCount;
    }

    public boolean auth(String login, String password)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT ID " +
                        "FROM Users " +
                        "WHERE Name = ? AND Password = ?");
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {
                int ID = resultSet.getInt("ID");
                curUser = getUser(ID);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return curUser != null;
    }

    public User getCurUser()
    {
        return curUser;
    }
}