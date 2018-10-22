/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.TaskList;

class RemoveTaskCommand implements Command
{
    private TaskList taskList;
    private int index;

    RemoveTaskCommand(TaskList taskList, int index)
    {
        this.taskList = taskList;
        this.index = index;
    }

    @Override
    public void execute()
    {
        taskList.remove(index);
    }
}
