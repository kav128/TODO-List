/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.data.Date;
import com.kav128.todo.data.TaskModifyTrigger;

import java.util.UUID;

public class Task
{
    private final UUID uuid;
    private String title;
    private String description;
    private Date deadline;
    private boolean completed;
    private final TaskModifyTrigger modifyTrigger;

    Task(UUID uuid, String title, String description, Date deadline, boolean completed, TaskModifyTrigger modifyTrigger)
    {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.modifyTrigger = modifyTrigger;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
        modifyTrigger.titleModified(title);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
        modifyTrigger.descriptionModified(description);
    }

    public Date getDeadline()
    {
        return deadline;
    }

    public void setDeadline(Date deadline)
    {
        this.deadline = deadline;
        modifyTrigger.deadlineModified(deadline);
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
        modifyTrigger.completedModified(completed);
    }

    public void setCompleted(String completedString)
    {
        setCompleted(Boolean.parseBoolean(completedString));
    }

    @Override
    public String toString()
    {
        return "Task{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", completed=" + completed +
                '}';
    }
}
