/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.core.TaskList;

public class CompletedCommand implements Command
{
    private final TaskList taskList;
    private final int index;

    private CompletedCommand(TaskList taskList, int index)
    {
        this.taskList = taskList;
        this.index = index;
    }

    static Command parse(String[] args)
    {
        int index = Integer.parseInt(args[0]) - 1;
        return new CompletedCommand(UI.instance().getTaskList(), index);
    }

    @Override
    public void execute()
    {
        taskList.get(index).setCompleted(true);
    }
}
