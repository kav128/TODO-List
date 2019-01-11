/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO extends AbstractDAO
{
    UsersDAO(Connection dbConnection) throws SQLException
    {
        super(dbConnection);
    }

    @Override
    protected String[] getFields()
    {
        return new String[]{"id", "name"};
    }

    public DataRecord getUserByID(int id)
    {
        DataRecord record;
        try (PreparedStatement getUserByIdStatement = dbConnection.prepareStatement(
                "SELECT id, name\n" +
                    "FROM Users\n" +
                    "WHERE id = ?"))
        {
            getUserByIdStatement.setInt(1, id);
            record = getSingleRecord(getUserByIdStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            record = null;
        }
        return record;
    }

    public DataRecord getUserByName(String name)
    {
        try (PreparedStatement getUserByIdStatement = dbConnection.prepareStatement(
                "SELECT id, name\n" +
                    "FROM Users\n" +
                    "WHERE name = ?"))
        {
            getUserByIdStatement.setString(1, name);
            return getSingleRecord(getUserByIdStatement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getHash(String value) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(value.getBytes());
    }

    public boolean insertUser(String userName, String password)
    {
        try (PreparedStatement insertUserStatement = dbConnection.prepareStatement(
                "INSERT INTO Users (name, passHash) VALUES\n" +
                    "(?, ?)"))
        {
            byte[] passHash = getHash(password);
            insertUserStatement.setString(1, userName);
            insertUserStatement.setObject(2, passHash);
            int updated = insertUserStatement.executeUpdate();
            return updated > 0;
        }
        catch (NoSuchAlgorithmException | SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int authUser(String userName, String password)
    {
        try (PreparedStatement authUserStatement = dbConnection.prepareStatement(
                "SELECT id\n" +
                    "FROM Users\n" +
                    "WHERE name = ? AND passHash = ?"))
        {
            byte[] passHash = getHash(password);
            authUserStatement.setString(1, userName);
            authUserStatement.setObject(2, passHash);
            ResultSet resultSet = authUserStatement.executeQuery();
            if (resultSet.next())
            {
                int id = resultSet.getInt(1);
                resultSet.close();
                return id;
            }
            else
                return -1;
        }
        catch (NoSuchAlgorithmException | SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
