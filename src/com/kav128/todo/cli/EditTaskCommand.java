/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.Task;
import com.kav128.todo.TaskList;

import java.util.Date;

public class EditTaskCommand implements Command
{
    private Task task;
    private String field;
    private Object newValue;

    public EditTaskCommand(TaskList taskList, int index, String field, Object newValue)
    {
        this(taskList.get(index), field, newValue);
    }

    public EditTaskCommand(Task task, String field, Object newValue)
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
                task.setTitle((String)newValue);
                break;
            case "description":
                task.setDescription((String)newValue);
                break;
            case "deadline":
                task.setDeadline((Date)newValue);
            case "completed":
                task.setCompleted((String)newValue);
        }
    }
}
