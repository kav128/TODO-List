/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.todo.Task;
import com.kav128.todo.TaskList;
import com.kav128.todo.User;
import com.kav128.todo.UserList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args)
    {
        String dbURL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=ToDo;integratedSecurity=true;";
        Connection connection;
        try
        {
            connection = DriverManager.getConnection(dbURL);
            System.out.println("Successfully connected to SQL Server");
            TaskList taskList = new TaskList(connection);
            UserList userList = taskList.getUserList();
            Scanner cliScanner = new Scanner(System.in);

            auth(userList);

            boolean cliExecute = true;
            while (cliExecute)
            {
                System.out.print("TODO @localhost> ");
                String command = cliScanner.next();

                int dbID;
                Task task;
                switch (command)
                {
                    case "new":
                        String obj = cliScanner.next();
                        cliScanner.nextLine();
                        switch (obj)
                        {
                            case "user":
                                System.out.print("username> ");
                                String login = cliScanner.nextLine();
                                System.out.print("password> ");
                                String password = cliScanner.nextLine();
                                User newUser = userList.register(login, password);
                                System.out.println(newUser != null ? "Success" : "Error");
                                break;
                            case "task":
                                System.out.print("Title> ");
                                String title = cliScanner.nextLine();
                                System.out.print("Descr> ");
                                String description = cliScanner.nextLine();
                                Date deadline = null;
                                while(deadline == null)
                                {
                                    System.out.print("Dline> ");

                                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                    try
                                    {
                                        deadline = format.parse(cliScanner.nextLine());
                                    }
                                    catch (ParseException e)
                                    {
                                        System.out.println("Incorrect date");
                                        deadline = null;
                                        e.printStackTrace();
                                    }
                                }

                                task = new Task(title, description, deadline);
                                taskList.add(task);
                                break;
                        }
                        break;
                    case "edit":
                        dbID = cliScanner.nextInt();
                        cliScanner.nextLine();
                        System.out.print("Title> ");
                        String title = cliScanner.nextLine();
                        System.out.print("Descr> ");
                        String description = cliScanner.nextLine();
                        System.out.print("Dline> ");
                        String deadlineString = cliScanner.nextLine();

                        task = taskList.getTask(dbID);
                        if (!title.isEmpty())
                            task.setTitle(title);
                        if (!description.isEmpty())
                            task.setDescription(description);
                        if (!deadlineString.isEmpty())
                        {
                            Date deadline;
                            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                            try
                            {
                                deadline = format.parse(cliScanner.nextLine());
                            }
                            catch (ParseException e)
                            {
                                System.out.println("Incorrect date. Skipped");
                                deadline = null;
                            }

                            if (deadline != null)
                                task.setDeadline(deadline);
                        }
                        break;
                    case "remove":
                        dbID = cliScanner.nextInt();
                        taskList.remove(dbID);
                        break;
                    case "details":
                        dbID = cliScanner.nextInt();
                        task = taskList.getTask(dbID);
                        System.out.println("Title:");
                        System.out.println('\t' + task.getTitle());
                        System.out.println("Description:");
                        System.out.println('\t' + task.getDescription());
                        System.out.println("Author:");
                        System.out.println('\t' + taskList.getUserList().getUser(task.getAuthorID()).getName());
                        System.out.println("Deadline:");
                        System.out.println('\t' + task.getDeadline().toString());
                        System.out.println("Completed:");
                        System.out.printf("\t%b\n", task.isCompleted());
                        break;
                    case "completed":
                        dbID = cliScanner.nextInt();
                        taskList.getTask(dbID).setCompleted(true);
                        break;
                    case "uncompleted":
                        dbID = cliScanner.nextInt();
                        taskList.getTask(dbID).setCompleted(false);
                        break;
                    case "logout":
                        auth(userList);
                        break;
                    case "exit":
                        cliExecute = false;
                        break;
                }
            }

            connection.close();
            System.out.println("Successfully disconnected from SQL Server");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void auth(UserList userList)
    {
        Scanner cliScanner = new Scanner(System.in);
        boolean auth = false;
        while (!auth)
        {
        System.out.print("username> ");
        String login = cliScanner.nextLine();
        System.out.print("password> ");
        String password = cliScanner.nextLine();
            auth = userList.auth(login, password);
            if (!auth)
                System.out.println("Incorrect username/password");
        }
    }
}
