/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.Task;

public class ShowTaskCommand implements Command
{
    Task task;

    public ShowTaskCommand(Task task)
    {
        this.task = task;
    }

    @Override
    public void execute()
    {
        TaskPrinter.printTask(Character.MIN_VALUE, task);
    }
}
