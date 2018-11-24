/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.DataSource;
import com.kav128.todo.TaskList;

import java.util.Arrays;
import java.util.Scanner;

public class CommandLineInterpreter
{
    private final TaskList taskList;
    private final DataSource dataSource;
    private final CommandFactory factory;

    CommandLineInterpreter(TaskList taskList, DataSource dataSource)
    {
        this.taskList = taskList;
        this.dataSource = dataSource;
        factory = new CommandFactory();
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
        return factory.getFromString(args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    TaskList getTaskList()
    {
        return taskList;
    }

    DataSource getDataSource()
    {
        return dataSource;
    }
}
