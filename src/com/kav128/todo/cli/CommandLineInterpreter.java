/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.core.TaskController;
import com.kav128.todo.core.TaskList;
import com.kav128.todo.core.ToDoApp;

import java.util.Arrays;
import java.util.Scanner;

public class CommandLineInterpreter
{
    private class Credentials
    {
        private String login;
        private String password;

        Credentials(String login, String password)
        {
            this.login = login;
            this.password = password;
        }

        public String getLogin()
        {
            return login;
        }

        public String getPassword()
        {
            return password;
        }
    }

    private final ToDoApp app;
    private TaskList taskList;
    private final CommandFactory factory;

    CommandLineInterpreter(ToDoApp app)
    {
        this.app = app;
        factory = new CommandFactory();
    }

    public void run()
    {
        Scanner scanner = new Scanner(System.in);
        while (!app.getUserController().isAuthorized())
        {
            System.out.println("(L)ogin (R)egister or (E)xit?");
            String s = scanner.nextLine();
            if (s.equals("E"))
                return;

            switch (s)
            {
                case "L":
                    login();
                    break;
                case "R":
                    register();
                    break;
            }
        }

        TaskController taskController = app.getTaskController();
        taskController.load();
        taskList = taskController.getTaskList();

        while (true)
        {
            System.out.print("TODO> ");
            String commandLine = scanner.nextLine();
            if (commandLine.equals("exit"))
                break;
            else
            {
                Command command = parseCommand(commandLine);
                if (command != null)
                    command.execute();
                else
                    System.out.println("Invalid command line input");
            }
        }
    }

    private Credentials requestLoginAndPassword()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Login> ");
        String login = sc.nextLine();
        System.out.print("Password> ");
        String pass = sc.nextLine();
        return new Credentials(login, pass);
    }

    private boolean login()
    {
        Credentials credentials = requestLoginAndPassword();
        return app.getUserController().login(credentials.login, credentials.password);
    }

    private boolean register()
    {
        Credentials credentials = requestLoginAndPassword();
        return app.getUserController().register(credentials.login, credentials.password);
    }

    private Command parseCommand(String commandLine)
    {
        String[] args = commandLine.split(" ");
        return factory.getFromString(args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    TaskList getTaskList()
    {
        return taskList;
    }

    ToDoApp getApp()
    {
        return app;
    }
}
