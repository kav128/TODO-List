/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.Date;
import com.kav128.todo.Task;
import com.kav128.todo.TaskList;

class EditTaskCommand implements Command
{
    private final Task task;
    private final String field;
    private final String newValue;

    EditTaskCommand(TaskList taskList, int index, String field, String newValue)
    {
        this(taskList.get(index), field, newValue);
    }

    private EditTaskCommand(Task task, String field, String newValue)
    {
        this.task = task;
        this.field = field;
        this.newValue = newValue;
    }

    @Override
    public void execute()
    {
        switch (field)
        {
            case "title":
                task.setTitle(newValue);
                break;
            case "description":
                task.setDescription(newValue);
                break;
            case "deadline":
                task.setDeadline(new Date(newValue));
            case "completed":
                task.setCompleted(newValue);
        }
    }
}
