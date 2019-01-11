/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.todo.data.TaskModifyTrigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task
{
    private final int id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean completed;
    private TaskPurpose purposeTag;
    private User author;
    private final TaskModifyTrigger modifyTrigger;
    private List<User> assignedUsers;

    Task(int id, String title, String description, LocalDate deadline, boolean completed, TaskPurpose purposeTag, User author, TaskModifyTrigger modifyTrigger)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.purposeTag = purposeTag;
        this.author = author;
        this.modifyTrigger = modifyTrigger;
        assignedUsers = new ArrayList<>();
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
            if (modifyTrigger.titleModified(title))
                this.title = title;
            else
                throw new RuntimeException("Session is closed");
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
            if (modifyTrigger.descriptionModified(description))
                this.description = description;
            else
                throw new RuntimeException("Session is closed");
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
            if (modifyTrigger.deadlineModified(deadline))
                this.deadline = deadline;
            else
                throw new RuntimeException("Session is closed");
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
            if (modifyTrigger.completedModified(completed))
                this.completed = completed;
            else
                throw new RuntimeException("Session is closed");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public TaskPurpose getPurposeTag()
    {
        return purposeTag;
    }

    public void setPurposeTag(TaskPurpose purposeTag)
    {
        if (!modifyTrigger.allowModification())
            throw new RuntimeException("Session is closed");

        if (modifyTrigger.taskPurposeModified(purposeTag))
            this.purposeTag = purposeTag;
        else
            if (modifyTrigger.taskPurposeSet(purposeTag))
                this.purposeTag = purposeTag;

    }

    public void setCompleted(String completedString)
    {
        setCompleted(Boolean.parseBoolean(completedString));
    }

    public User getAuthor()
    {
        return author;
    }

    void addAssignedUser(User user)
    {
        assignedUsers.add(user);
    }

    public List<User> getAssignedUsers()
    {
        return new ArrayList<>(assignedUsers);
    }

    @Override
    public String toString()
    {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", purposeTag=" + purposeTag +
                ", completed=" + completed +
                ", author=" + author.getUsername() +
                '}';
    }
}
