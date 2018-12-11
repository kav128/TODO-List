/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.ToDoApp;

public class UI
{
    private static CommandLineInterpreter cli;

    public static void init(ToDoApp app)
    {
        cli = new CommandLineInterpreter(app);
    }

    public static CommandLineInterpreter instance()
    {
        return cli;
    }
}
