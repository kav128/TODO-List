/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import java.sql.*;
import java.util.Date;

public class Task
{
    private int dbID;
    private String title;
    private String description;
    private User author;
    private Date deadline;
    private boolean completed;
    private int authorID;
    private boolean isDescriptionLoaded;

    private Connection dbConnection;
    private PreparedStatement loadDescriptionStatement;
    private PreparedStatement updateTitleStatement;
    private PreparedStatement updateDescriptionStatement;
    private PreparedStatement updateDeadlineStatement;
    private PreparedStatement updateCompletedStatement;

    Task(int ID, Connection dbConnection)
    {
        this.dbConnection = dbConnection;
        dbID = ID;
        try
        {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT Title, Author, Deadline, Completed " +
                    "FROM Tasks " +
					"WHERE ID = " + String.valueOf(ID));
            resultSet.next();

            title = resultSet.getString("Title");
            authorID = resultSet.getInt("Author");
            deadline = resultSet.getDate("Deadline");
            completed = resultSet.getBoolean("Completed");
            isDescriptionLoaded = false;

            loadDescriptionStatement = dbConnection.prepareStatement(
                    "SELECT Description " +
					"FROM Tasks " +
					"WHERE ID = ?");
            loadDescriptionStatement.setInt(1, ID);

            setStatements();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Task(String title, String description, Date deadline)
    {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        authorID = -1;
        completed = false;
        dbID = -1;
    }

    void setAuthor(User author)
    {
        if (this.author == null)
        {
            this.author = author;
            authorID = author.getID();
        }
    }

    void setDbConnection(Connection dbConnection)
    {
        if (this.dbConnection == null)
            this.dbConnection = dbConnection;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        if (!isDescriptionLoaded)
        {
            try
            {
                ResultSet resultSet = loadDescriptionStatement.executeQuery();
                resultSet.next();
                description = resultSet.getString("Description");
                isDescriptionLoaded = true;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return description;
    }

    public int getAuthorID()
    {
        return authorID;
    }

    public Date getDeadline()
    {
        return deadline;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    private void setStatements()
    {
        try
        {
            updateTitleStatement = dbConnection.prepareStatement(
                    "UPDATE Tasks " +
                    "SET Title = ? " +
                    "WHERE ID = " + String.valueOf(dbID));
            updateDescriptionStatement = dbConnection.prepareStatement(
                    "UPDATE Tasks " +
                    "SET Description = ? " +
                    "WHERE ID = " + String.valueOf(dbID));
            updateDeadlineStatement = dbConnection.prepareStatement(
                    "UPDATE Tasks " +
                    "SET Deadline = ? " +
                    "WHERE ID = " + String.valueOf(dbID));
            updateCompletedStatement = dbConnection.prepareStatement(
                    "UPDATE Tasks " +
                    "SET Completed = ? " +
                    "WHERE ID = " + String.valueOf(dbID));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    void writeToDb()
    {
        if (dbID < 0)
        {
            try
            {
                PreparedStatement ps = dbConnection.prepareStatement(
                        "INSERT INTO Tasks(Title, Description, Author, Deadline, Completed) VALUES" +
                        "(?, ?, ?, ?, 0)");
                ps.setString(1, title);
                ps.setString(2, description);
                ps.setInt(3, authorID);
                ps.setDate(4, new java.sql.Date(deadline.getTime()));

                ps.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setTitle(String title)
    {
        try
        {
            updateTitleStatement.setString(1, title);
            int i = updateTitleStatement.executeUpdate();
            if (i > 0)
                this.title = title;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void setDescription(String description)
    {
        try
        {
            updateDescriptionStatement.setString(1, description);
            int i = updateDescriptionStatement.executeUpdate();
            if (i > 0)
                this.description = description;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void setDeadline(Date deadline)
    {
        try
        {
            updateDeadlineStatement.setDate(1, (java.sql.Date)deadline);
            int i = updateDeadlineStatement.executeUpdate();
            if (i > 0)
                this.deadline = deadline;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void setCompleted(Boolean completed)
    {
        if (completed == this.completed)
            return;

        try
        {
            updateCompletedStatement.setBoolean(1, completed);
            int i = updateCompletedStatement.executeUpdate();
            if (i > 0)
                this.completed = completed;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString()
    {
        return "Task{" +
                "dbID=" + dbID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", deadline=" + deadline +
                ", completed=" + completed +
                ", authorID=" + authorID +
                '}';
    }

    int getDbID()
    {
        return dbID;
    }
}
