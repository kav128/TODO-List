/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO extends AbstractDAO
{
    private final PreparedStatement getUserByIdStatement;
    private final PreparedStatement insertUserStatement;
    private final PreparedStatement authUserStatement;

    UsersDAO(Connection dbConnection) throws SQLException
    {
        super(dbConnection);
        getUserByIdStatement = dbConnection.prepareStatement(
                "SELECT id, name\n" +
                "FROM Users\n" +
                "WHERE id = ?");
        insertUserStatement = dbConnection.prepareStatement(
                "INSERT INTO Users (name, passHash) VALUES\n" +
                "(?, ?)");
        authUserStatement = dbConnection.prepareStatement(
                "SELECT id\n" +
                "FROM Users\n" +
                "WHERE name = ? AND passHash = ?");
    }

    @Override
    protected String[] getFields()
    {
        return new String[]{"id", "name"};
    }

    public DataRecord getUserByID(int id) throws SQLException
    {
        getUserByIdStatement.setInt(1, id);
        return getSingleRecord(getUserByIdStatement);
    }

    private static byte[] getHash(String value) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(value.getBytes());
    }

    public boolean insertUser(String userName, String password) throws SQLException
    {
        try
        {
            byte[] passHash = getHash(password);
            insertUserStatement.setString(1, userName);
            insertUserStatement.setObject(2, passHash);
            int updated = insertUserStatement.executeUpdate();
            return updated > 0;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int authUser(String userName, String password) throws SQLException
    {
        try
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
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void close() throws Exception
    {
        authUserStatement.close();
        insertUserStatement.close();
        getUserByIdStatement.close();
    }
}
