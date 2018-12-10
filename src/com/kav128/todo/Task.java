/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.todo.data.TaskModifyTrigger;

import java.time.LocalDate;

public class Task
{
    private final int id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean completed;
    private final TaskModifyTrigger modifyTrigger;

    Task(int id, String title, String description, LocalDate deadline, boolean completed, TaskModifyTrigger modifyTrigger)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.modifyTrigger = modifyTrigger;
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        try
        {
            modifyTrigger.titleModified(title);
            this.title = title;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        try
        {
            modifyTrigger.descriptionModified(description);
            this.description = description;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public LocalDate getDeadline()
    {
        return deadline;
    }

    public void setDeadline(LocalDate deadline)
    {
        try
        {
            modifyTrigger.deadlineModified(deadline);
            this.deadline = deadline;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        try
        {
            modifyTrigger.completedModified(completed);
            this.completed = completed;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setCompleted(String completedString)
    {
        setCompleted(Boolean.parseBoolean(completedString));
    }

    @Override
    public String toString()
    {
        return "Task{" +
                "uuid=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", completed=" + completed +
                '}';
    }
}
