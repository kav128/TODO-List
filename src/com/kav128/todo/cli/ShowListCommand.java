/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.TaskList;

class ShowListCommand implements Command
{
    private final TaskList taskList;

    ShowListCommand(TaskList taskList)
    {
        this.taskList = taskList;
    }

    @Override
    public void execute()
    {
        for (int i = 0; i < taskList.count(); i++)
        {
            System.out.println("{" + (i + 1) + "}");
            TaskPrinter.printTask('\t', taskList.get(i));
        }
    }
}
