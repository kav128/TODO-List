/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.core.TaskList;

class RemoveTaskCommand implements Command
{
    private final TaskList taskList;
    private final int index;

    private RemoveTaskCommand(TaskList taskList, int index)
    {
        this.taskList = taskList;
        this.index = index;
    }

    static Command parse(String[] args)
    {
        int index = Integer.parseInt(args[0]) - 1;
        return new RemoveTaskCommand(UI.instance().getTaskList(), index);
    }

    @Override
    public void execute()
    {
        //taskList.remove(index);
    }
}
