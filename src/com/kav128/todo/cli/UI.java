/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.DataSource;
import com.kav128.todo.TaskList;

public class UI
{
    private static CommandLineInterpreter cli;

    public static void init(TaskList taskList, DataSource source)
    {
        cli = new CommandLineInterpreter(taskList, source);
    }

    public static CommandLineInterpreter instance()
    {
        return cli;
    }
}
