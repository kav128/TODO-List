/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.Date;
import com.kav128.todo.Task;
import com.kav128.todo.TaskList;

public class EditTaskCommand implements Command
{
    private Task task;
    private String field;
    private String newValue;

    public EditTaskCommand(TaskList taskList, int index, String field, String newValue)
    {
        this(taskList.get(index), field, newValue);
    }

    public EditTaskCommand(Task task, String field, String newValue)
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
