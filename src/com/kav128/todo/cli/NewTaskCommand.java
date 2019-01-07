/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.TaskPurpose;
import com.kav128.todo.ToDoApp;

import java.time.LocalDate;

class NewTaskCommand implements Command
{
    private final String title;
    private final String description;
    private final LocalDate deadline;
    private final TaskPurpose purpose;
    private final ToDoApp app;

    private NewTaskCommand(String title, String description, LocalDate deadline, TaskPurpose purpose, ToDoApp app)
    {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.purpose = purpose;
        this.app = app;
    }

    static NewTaskCommand parse(String[] args)
    {
        String title = "";
        String description = "";
        LocalDate deadline = null;
        TaskPurpose purpose = TaskPurpose.noPurpose;

        for (int i = 0; i < args.length; i++)
        {
            switch (args[i++])
            {
                case "-title":
                    title = args[i];
                    break;
                case "-description":
                    description = args[i];
                    break;
                case "-deadline":
                    deadline = LocalDate.parse(args[i]);
                    break;
                case "-purpose":
                    purpose = TaskPurpose.valueOf(args[i]);
                    break;
            }
        }

        return new NewTaskCommand(title, description, deadline, purpose,UI.instance().getApp());
    }

    @Override
    public void execute()
    {
        app.getTaskController().createTask(title, description, purpose, deadline);
    }
}
