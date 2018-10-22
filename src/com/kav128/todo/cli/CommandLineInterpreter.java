/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.DataSource;
import com.kav128.data.Date;
import com.kav128.todo.TaskList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class CommandLineInterpreter
{
    private TaskList taskList;
    private DataSource dataSource;
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public CommandLineInterpreter(TaskList taskList, DataSource dataSource)
    {
        this.taskList = taskList;
        this.dataSource = dataSource;
    }

    public void run()
    {
        Scanner scanner = new Scanner(System.in);
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

    private Command parseCommand(String commandLine)
    {
        String[] args = commandLine.split(" ");
        switch (args[0])
        {
            case "add":
                return parseNewTaskCommand(args);
            case "edit":
                return parseEditTaskCommand(args);
            case "remove":
                return parseRemoveTaskCommand(args);
            case "show":
                return parseShowCommand(args);
            case "commit":
                return new CommitCommand(dataSource);
        }
        return null;
    }

    private NewTaskCommand parseNewTaskCommand(String[] args)
    {

        String title = "";
        String description = "";
        String deadlineString = "";
        Date deadline = null;

        for (int i = 1; i < args.length; i++)
        {
            switch (args[i++])
            {
                case "-title":
                    title = args[i];
                    break;
                case "-description":
                    description = args[i];
                    break;
                case "-deadline":
                    deadlineString = args[i];
                    break;
            }
        }

        deadline = new Date(deadlineString);

        return new NewTaskCommand(title, description, deadline, taskList);
    }

    private EditTaskCommand parseEditTaskCommand(String[] args)
    {
        int index;
        String field;
        String value;

        index = Integer.parseInt(args[1]) - 1;
        field = args[2].substring(1);
        value = args[3];

        return new EditTaskCommand(taskList, index, field, value);
    }

    private RemoveTaskCommand parseRemoveTaskCommand(String[] args)
    {
        int index = Integer.parseInt(args[1]) - 1;
        return new RemoveTaskCommand(taskList, index);
    }

    private Command parseShowCommand(String[] args)
    {
        switch (args[1])
        {
            case "task":
                return new ShowTaskCommand(taskList.get(Integer.parseInt(args[2]) - 1));
            case "tasks":
                return new ShowListCommand(taskList);
        }
        return null;
    }
}
