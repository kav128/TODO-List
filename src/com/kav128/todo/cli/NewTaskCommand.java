/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.Date;
import com.kav128.todo.TaskList;

class NewTaskCommand implements Command
{
    private final String title;
    private final String description;
    private final Date deadline;
    private final TaskList taskList;

    NewTaskCommand(String title, String description, Date deadline, TaskList taskList)
    {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.taskList = taskList;
    }

    @Override
    public void execute()
    {
        taskList.createTask(title, description, deadline);
    }
}
